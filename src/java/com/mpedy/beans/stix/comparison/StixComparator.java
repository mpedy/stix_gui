/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.comparison;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mpedy.beans.stix.Bundle;
import com.mpedy.beans.stix.GeneralObject;
import com.mpedy.beans.stix.MatchResult;
import com.mpedy.beans.stix.Utils;
import com.mpedy.beans.stix.sro.Relationship;
import com.mpedy.beans.stix.vocabulary.RELATIONSHIP_TYPE;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author cuore
 */
@ManagedBean(name = "stixComparator")
@RequestScoped
public class StixComparator implements Serializable {

    public JsonObject weights = new JsonObject();

    /**
     *
     * @param b1 - truth
     * @param b2 - to be compared
     * @return Number indicated level of similarity
     */
    public Float compare(Bundle b1, Bundle b2) {
        loadWeights();
        MatchResult result = new MatchResult();
        List<Relationship> r1 = b1.getAllRelationship();
        List<Relationship> r2 = b2.getAllRelationship();
        //Comparo il nodo principale
        GeneralObject main1 = getMainNode(r1);
        GeneralObject main2 = getMainNode(r2);
        for (String typeOfRelationship : new String[]{RELATIONSHIP_TYPE.HAS, RELATIONSHIP_TYPE.TARGETS, RELATIONSHIP_TYPE.IS_FOLLOWED_BY}) {
            List<Relationship> r1has = main1.getRels().stream().filter(rel -> rel.getRelationship_type().equals(typeOfRelationship)).collect(Collectors.toList());
            List<Relationship> r2has = main2.getRels().stream().filter(rel -> rel.getRelationship_type().equals(typeOfRelationship)).collect(Collectors.toList());
            for (Relationship rel1Has : r1has) {
                GeneralObject obj1 = rel1Has.getRight();
                List<Relationship> r2has1 = r2has.stream().filter(rel -> rel.getRight().getType().equals(obj1.getType())).collect(Collectors.toList());
                if (!r2has1.isEmpty()) {
                    MatchResult temp = new MatchResult();
                    for (Relationship rel2Has : r2has1) {
                        MatchResult res = compareRelationship(rel1Has, rel2Has);
                        if (res.getAsFloat() > temp.getAsFloat()) {
                            temp = res;
                        }
                    }
                    result.add(temp);
                }
            }
        }
        //Comparo ciascuna relationship del truth
        for (Relationship rr1 : r1) {
            List<Relationship> r2list = r2.stream().filter(
                    r -> r.getRelationship_type().equals(rr1.getRelationship_type()) && r.getLeft().getType().equals(rr1.getLeft().getType()) && r.getRight().getType().equals(rr1.getRight().getType())
            ).collect(Collectors.toList());
            if (r2list.isEmpty()) {
                result.add(new MatchResult(0.0f, 1.0f));
//                MatchResult temp = compareRelationship(rr1, rr1); //con pesi negativi
//                temp.setResult(temp.getResult()*-1);
//                result.add(temp);
                continue;
            }
            MatchResult temp = new MatchResult();
            for (Relationship rr2 : r2list) {
                MatchResult res = compareRelationship(rr1, rr2);
                if (res.getAsFloat() > temp.getAsFloat()) {
                    temp = res;
                }
            }
            result.add(temp);
        }
        //Comparo ciascuna relationship dell'altro
        for (Relationship rr2 : r2) {
            List<Relationship> r1list = r1.stream().filter(
                    r -> r.getRelationship_type().equals(rr2.getRelationship_type()) && r.getLeft().getType().equals(rr2.getLeft().getType()) && r.getRight().getType().equals(rr2.getRight().getType())
            ).collect(Collectors.toList());
            if (r1list.isEmpty()) {
                result.add(new MatchResult(0.0f, 1.0f));
            }
            MatchResult temp = new MatchResult();
            for (Relationship rr1 : r1list) {
                MatchResult res = compareRelationship(rr2, rr1);
                if (res.getAsFloat() > temp.getAsFloat()) {
                    temp = res;
                }
            }
            result.add(temp);
        }
        System.out.println(result.toString());
        return result.getAsFloat();
    }

    public GeneralObject getMainNode(List<Relationship> rels) {
        HashMap<String, Integer> hashmap = new HashMap<>();
        String id = "";
        Integer maxValue = -1;
        GeneralObject gObj = new GeneralObject();
        for (Relationship rel : rels) {
            hashmap.putIfAbsent(rel.getSource_ref(), hashmap.getOrDefault(rel.getSource_ref(), 0) + 1);
            hashmap.putIfAbsent(rel.getTarget_ref(), hashmap.getOrDefault(rel.getTarget_ref(), 0) + 1);
            Integer actualValue = Integer.max(maxValue, hashmap.getOrDefault(rel.getSource_ref(), 0) + 1);
            if (actualValue > maxValue) {
                maxValue = actualValue;
                id = rel.getSource_ref();
                gObj = rel.getLeft();
            }
            actualValue = Integer.max(maxValue, hashmap.getOrDefault(rel.getTarget_ref(), 0) + 1);
            if (actualValue > maxValue) {
                maxValue = actualValue;
                id = rel.getTarget_ref();
                gObj = rel.getRight();
            }

        }
        return gObj;
    }

    public MatchResult compareRelationship(Relationship r1, Relationship r2) {
        MatchResult result = new MatchResult();
        result.add(compareObject(r1.getLeft(), r2.getLeft()));
        result.add(compareObject(r1.getRight(), r2.getRight()));
        if (r1.getRelationship_type().equals(RELATIONSHIP_TYPE.IS_FOLLOWED_BY) && r1.getRelationship_type().equals(r2.getRelationship_type())) {
            Float weight_source_timeref = 1.0f;
            Float weight_target_timeref = 1.0f;
            try {
                weight_source_timeref = weights.get(r1.getClass().getSimpleName()).getAsJsonObject().get("source_timeref").getAsFloat();
                weight_target_timeref = weights.get(r1.getClass().getSimpleName()).getAsJsonObject().get("target_timeref").getAsFloat();
            } catch (Exception e) {
                System.out.println("Errore nel prendere i pesi per il source_timeref o target_timeref: " + e.toString());
            }
            LocalDateTime t1 = Utils.parseTime(r1.getSource_timeref(), Arrays.asList("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss'Z'"));
            LocalDateTime t2 = Utils.parseTime(r2.getSource_timeref(), Arrays.asList("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss'Z'"));
            if (t2.isAfter(t1.minusMinutes(2)) && t2.isBefore(t1.plusMinutes(2))) {
                result.setResult(result.getResult() + 1.0f * weight_source_timeref);
                result.setComparisons(result.getComparisons() + weight_source_timeref);
            }
            t1 = Utils.parseTime(r1.getTarget_timeref(), Arrays.asList("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss'Z'"));
            t2 = Utils.parseTime(r2.getTarget_timeref(), Arrays.asList("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss'Z'"));
            if (t2.isAfter(t1.minusMinutes(2)) && t2.isBefore(t1.plusMinutes(2))) {
                result.setResult(result.getResult() + 1.0f * weight_target_timeref);
                result.setComparisons(result.getComparisons() + weight_target_timeref);
            }
        }
        return result;
    }

    public MatchResult compareObject(GeneralObject obj1, GeneralObject obj2) {
        MatchResult result = new MatchResult();
        if (!obj1.getType().equals(obj2.getType()) || !obj1.getClass().equals(obj2.getClass())) {
            return result;
        }
        assert obj1.getClass().equals(obj2.getClass()) : "Classes don't match";
        result.add(obj1.compare(obj2, weights.getAsJsonObject(obj1.getClass().getSimpleName())));
        return result;

    }

    public void loadWeights() {
        try {
            weights = new JsonParser().parse(
                    new FileReader(URLDecoder.decode(StixComparator.class.getProtectionDomain().getCodeSource().getLocation().getPath(),StandardCharsets.UTF_8) + "com\\mpedy\\beans\\stix\\comparison\\weights.json")
            ).getAsJsonObject();
        } catch (Exception e) {
            System.out.println("Error loading weights: " + e.toString());
            weights = new JsonObject();
        }
    }

}

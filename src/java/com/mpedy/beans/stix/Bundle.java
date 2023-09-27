/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix;

import com.mpedy.beans.stix.sro.Relationship;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author cuore
 */
public class Bundle implements Serializable {

    private List<GeneralObject> objects;
    private List<Relationship> rels;

    public List<GeneralObject> getAllGeneralObject() {
        Predicate predicate = p -> p.getClass().equals(GeneralObject.class) || p.getClass().getSuperclass().equals(GeneralObject.class);
        if (objects != null && !objects.isEmpty()) {
            return objects.stream().filter(elem -> predicate.test(elem)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<Relationship> getAllRelationship() {
        Predicate predicate = p -> p.getClass().equals(Relationship.class);
        if (rels != null && !rels.isEmpty()) {
            return rels.stream().filter(elem -> predicate.test(elem)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<GeneralObject> getObjects() {
        return objects;
    }

    public void setObjects(List<GeneralObject> objects) {
        this.objects = objects;
    }

    public List<Relationship> getRels() {
        return rels;
    }

    public void setRels(List<Relationship> rels) {
        this.rels = rels;
    }

}

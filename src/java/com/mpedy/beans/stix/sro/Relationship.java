/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sro;

import com.google.gson.JsonObject;
import com.mpedy.beans.stix.GeneralObject;
import com.mpedy.beans.stix.NoInput;
import com.mpedy.beans.stix.NoJSON;
import com.mpedy.beans.stix.NoPrint;
import com.mpedy.beans.stix.Utils;
import static com.mpedy.beans.stix.Utils.getAllFieldFromClass;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

/**
 *
 * @author cuore
 */
public class Relationship implements Serializable {

    @NoInput(true)
    private String relationship_type;
    @NoInput(true)
    private String id;
    private String type;
    @NoJSON(true)
    private GeneralObject left;
    @NoJSON(true)
    private GeneralObject right;
    @NoPrint(true)
    @NoInput(true)
    private LocalDateTime created_time;
    private String created;
    private String source_ref;
    private String target_ref;
//    private String provamia;
    private String source_timeref;
    private String target_timeref;
    @NoInput(true)
    @NoJSON(true)
    private LocalDateTime source_timeref_t;
    @NoPrint(true)
    @NoInput(true)
    @NoJSON(true)
    private LocalDateTime target_timeref_t;
    
    public Relationship() {
        this.type = "relationship";
        this.id = this.type + "--" + UUID.randomUUID();
        this.created = Utils.getActualDateTime_str();
        this.created_time = Utils.getActualDateTime();
//        this.provamia = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(String relationship_type) {
        this.relationship_type = relationship_type;
    }

    public GeneralObject getLeft() {
        return left;
    }

    public void setLeft(GeneralObject left) {
        this.left = left;
        this.source_ref = left.getId();
    }

    public GeneralObject getRight() {
        return right;
    }

    public void setRight(GeneralObject right) {
        this.right = right;
        this.target_ref = right.getId();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder(type + "{");
        ArrayList clazzes = new ArrayList<>();
        clazzes.add(this.getClass());
        for (int i = 0; i < clazzes.size(); i++) {
            Class cla = (Class) clazzes.get(i);
            //output.append("\nCLASSE=").append(cla.getSimpleName());
            for (Field f : cla.getDeclaredFields()) {
                NoPrint noPrint = f.getAnnotation(NoPrint.class);
                if (noPrint != null && noPrint.value()) {
                    continue;
                }
                f.setAccessible(true);
                output.append("\n").append(f.getName()).append("=");
                try {
                    output.append(f.get(this) == null ? "null" : f.get(this).toString());
                } catch (Exception e) {
                    output.append("ERRORE: ").append(e.toString());
                }
            }
        }
        output.append("}");
        return output.toString();
    }

    public LocalDateTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(LocalDateTime created_time) {
        this.created_time = created_time;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSource_ref() {
        return source_ref;
    }

    public void setSource_ref(String source_ref) {
        this.source_ref = source_ref;
    }

    public String getTarget_ref() {
        return target_ref;
    }

    public void setTarget_ref(String target_ref) {
        this.target_ref = target_ref;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.relationship_type);
        hash = 43 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Relationship other = (Relationship) obj;
        if (!Objects.equals(this.relationship_type, other.relationship_type)) {
            return false;
        }
        return Objects.equals(this.type, other.type);
    }

    public String getHTMLInfo() {
        String[] leftHTML = new String[3];
        String[] rightHTML = new String[3];
        StringBuilder result = new StringBuilder("");
        HashMap<String, String> infoMap = new HashMap<>();
        List<Field> fields = getAllFieldFromClass(this.getClass());
        Predicate<Field> pred = f -> {
            NoInput noInput = f.getAnnotation(NoInput.class);
            NoPrint noPrint = f.getAnnotation(NoPrint.class);
            return !((noInput != null && noInput.value()) || (noPrint != null && noPrint.value()));
        };
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if (f.getName().toLowerCase().equals("left")) {
                    leftHTML[0] = f.getName();
                    leftHTML[1] = ((GeneralObject) f.get(this)).getType().replaceAll("-", "_") + ".png.xhtml?ln=serenity-layout&v=1_0_29";
                    leftHTML[2] = ((GeneralObject) f.get(this)).getHTMLInfo(pred);
                } else if (f.getName().toLowerCase().equals("right")) {
                    rightHTML[0] = f.getName();
                    rightHTML[1] = ((GeneralObject) f.get(this)).getType().replaceAll("-", "_") + ".png.xhtml?ln=serenity-layout&v=1_0_29";
                    rightHTML[2] = ((GeneralObject) f.get(this)).getHTMLInfo(pred);
//                    infoMap.put(f.getName(), ((GeneralObject) f.get(this)).getHTMLInfo((pred)));
                } else {
                    infoMap.put(f.getName(), f.get(this).toString());
                }
            } catch (Exception e) {
                infoMap.put(f.getName(), null);
            }
        }
        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
            result
                    .append("<div><span><strong>")
                    .append(entry.getKey().substring(0, 1).toUpperCase()).append(entry.getKey().substring(1))
                    .append(": </strong></span>")
                    .append("<span> ")
                    .append(entry.getValue() == null ? "" : entry.getValue())
                    .append("</span>")
                    .append("</div>");
        }
        result.append("<div style='display: flex; align-items: center;'><div style='flex-grow: 1' class='m-3'>")
                .append("<img src=\"/stix/javax.faces.resource/images/").append(leftHTML[1]).append("\"/>")
                .append(leftHTML[2])
                .append("</div><br><div style='flex-grow: 1'>");
        if (this.relationship_type != null) {
            result.append(this.relationship_type.toUpperCase());
        }
        result.append("</div>").append("<div style='flex-grow: 1' class='m-3'>")
                .append("<img src=\"/stix/javax.faces.resource/images/").append(rightHTML[1]).append("\"/>")
                .append(rightHTML[2]).append("</div></div>");
        return result.toString();

    }

    public void parse(JsonObject json) {
        List<Field> fieldNames = Utils.getAllFieldFromClass(this.getClass(), pred -> true);
        for (Field f : fieldNames) {
            if (json.has(f.getName())) {
                try {
                    f.setAccessible(true);
                    Class type = f.getType();
                    if (type.equals(LocalDateTime.class)) {
                        f.set(this, Utils.parseTimeFromString(json.get(f.getName()).getAsString()));
                    } else {
                        f.set(this, type.cast(json.get(f.getName()).getAsString()));
                    }
                } catch (Exception e) {
                    System.out.println("Errore in parse: " + e.toString());
                }
            }
        }
    }

//    public String getProvamia() {
//        return provamia;
//    }
//
//    public void setProvamia(String provamia) {
//        this.provamia = provamia;
//    }
    public String getSource_timeref() {
        return source_timeref;
    }

    public void setSource_timeref(String source_timeref) {
        this.source_timeref = source_timeref;
    }

    public String getTarget_timeref() {
        return target_timeref;
    }

    public void setTarget_timeref(String target_timeref) {
        this.target_timeref = target_timeref;
    }

    public LocalDateTime getSource_timeref_t() {
        return source_timeref_t;
    }

    public void setSource_timeref_t(LocalDateTime source_timeref_t) {
        this.source_timeref_t = source_timeref_t;
    }

    public LocalDateTime getTarget_timeref_t() {
        return target_timeref_t;
    }

    public void setTarget_timeref_t(LocalDateTime target_timeref_t) {
        this.target_timeref_t = target_timeref_t;
    }

}

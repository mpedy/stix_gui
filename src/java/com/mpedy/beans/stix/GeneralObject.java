package com.mpedy.beans.stix;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mpedy.beans.stix.sro.Relationship;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

/**
 *
 * @author cuore
 */
public class GeneralObject implements Serializable {

    @NoInput(true)
    @NoPrint(true)
    private String id;
    @NoInput(true)
    @NoPrint(true)
    private String type;
    @NoInput(true)
    @NoPrint(true)
    private String created;
    private String name;
    private String description;
    @NoPrint(true)
    @NoInput(true)
    private LocalDateTime created_time;
    @NoPrint(true)
    @NoInput(true)
    @NoJSON(true)
    private ArrayList<Relationship> rels;

    @NoPrint(true)
    @NoJSON(true)
    private List<Attributes> attrs = new ArrayList<>();

    @NoPrint(true)
    @NoInput(true)
    @NoJSON(true)
    private String typeForLabel;

    @NoPrint(true)
    @NoInput(true)
    @NoJSON(true)
    private String imgSrc;

    @NoPrint(true)
    @NoInput(true)
    @NoJSON(true)
    private String oldId;

    public GeneralObject() {
        String nameOfClass = getSTIXNameFromClassName();
        this.type = nameOfClass;
        this.id = this.type + "--" + UUID.randomUUID();
        this.created = Utils.getActualDateTime_str();
        this.created_time = Utils.getActualDateTime();
        rels = new ArrayList<>();
        this.typeForLabel = Utils.getTypeForLabel(this.type);
        this.imgSrc = "images/" + this.type.replace('-', '_') + ".png";
    }

    public void renovateID() {
        this.id = this.type + "--" + UUID.randomUUID();
    }

    public String getVal() {
        try {
            return this.getClass().getMethod("getValue").invoke(this).toString();
        } catch (Exception e) {
            try {
                return this.getClass().getMethod("getName").invoke(this).toString();
            } catch (Exception ex) {
                return "";
            }
        }
    }

    private String getSTIXNameFromClassName() {
        String nameOfClass = this.getClass().getSimpleName();
        if (nameOfClass.equalsIgnoreCase("url")) {
            return "url";
        } else if (nameOfClass.equalsIgnoreCase("ipv4address")) {
            return "ipv4-addr";
        } else if (nameOfClass.equalsIgnoreCase("ipv6address")) {
            return "ipv6-addr";
        } else {
            nameOfClass = nameOfClass.substring(0, 1);
        }
        nameOfClass = (nameOfClass + this.getClass().getSimpleName().substring(1).replaceAll("([A-Z])", "-$1")).toLowerCase();
        return nameOfClass;
    }

    public LinkedHashMap<String, Object> getAllFields_old() {
        LinkedHashMap<String, Object> fieldMap = new LinkedHashMap<>();
        ArrayList clazzes = new ArrayList<>();
        clazzes.add(this.getClass().getSuperclass());
        clazzes.add(this.getClass());
        for (int i = 0; i < clazzes.size(); i++) {
            Class cla = (Class) clazzes.get(i);
            for (Field f : cla.getDeclaredFields()) {
                NoPrint noPrint = f.getAnnotation(NoPrint.class);
                if (noPrint != null && noPrint.value()) {
                    continue;
                }
                NoInput noInput = f.getAnnotation(NoInput.class);
                if (noInput != null && noInput.value()) {
                    continue;
                }
                f.setAccessible(true);
                String key = f.getName();
//                String value = f.getType().getSimpleName();
                Object value = null;
                try {
                    value = f.get(cla.getDeclaredConstructors()[0].newInstance());
                } catch (Exception e) {
                    System.out.println("Errore in getAllFields_old: " + e.toString());
                }
                fieldMap.put(key, value);
            }
        }
        return fieldMap;
    }

    public String getHTMLInfo() {
        StringBuilder result = new StringBuilder("");
        HashMap<String, String> infoMap = new HashMap<>();
        List<Field> fields = Utils.getAllFieldFromClassForUI(this.getClass());
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                infoMap.put(f.getName(), f.get(this).toString());
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
        return result.toString();
    }

    public String getHTMLInfo(Predicate<Field> pred) {
        StringBuilder result = new StringBuilder("");
        HashMap<String, String> infoMap = new HashMap<>();
        List<Field> fields = Utils.getAllFieldFromClass(this.getClass(), pred);
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                infoMap.put(f.getName(), f.get(this).toString());
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
        return result.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Relationship> getRels() {
        return rels;
    }

    public void setRels(ArrayList<Relationship> rels) {
        this.rels = rels;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public LocalDateTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(LocalDateTime created_time) {
        this.created_time = created_time;
    }

    public String getTypeForLabel() {
        return typeForLabel;
    }

    public void setTypeForLabel(String typeForLabel) {
        this.typeForLabel = typeForLabel;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public List<Attributes> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<Attributes> attrs) {
        this.attrs = attrs;
    }

    public String toGeneralString() {
        return "" + this.getClass().getSimpleName() + "{" + "id=" + id + ", type=" + type + ", created=" + created + ", name=" + name + ", description=" + description + ", created_time=" + created_time + ", rels=" + rels + ", attrs=" + attrs + ", typeForLabel=" + typeForLabel + ", imgSrc=" + imgSrc + ", oldId=" + oldId + '}';
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder(type + "{");
        ArrayList clazzes = new ArrayList<>();
        clazzes.add(this.getClass().getSuperclass());
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.description);
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
        final GeneralObject other = (GeneralObject) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.description, other.description);
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
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
                    } else if (type.equals(Boolean.class)) {
                        f.set(this, Boolean.valueOf(json.get(f.getName()).getAsString()));
                    } else if (type.isEnum()) {
                        f.set(this, type.getMethod("valueOf", String.class).invoke(null, json.get(f.getName()).getAsString().toUpperCase()));
                    } else {
                        f.set(this, type.cast(json.get(f.getName()).getAsString()));
                    }

                } catch (Exception e) {
                    System.out.println("Errore in parse: " + e.toString());
                }
            }
        }
    }

    public MatchResult compare(GeneralObject obj) {
        MatchResult result = new MatchResult();
        List<Field> fields = Utils.getAllFieldFromClass(this.getClass());
        for (Field field : fields) {
            if (field.getName().equals("attrs")) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object right = field.get(this);
                Object left = field.get(obj);
                if (field.getName().equals("hash")) {
                    Boolean hashValueComparison = right.toString().equals(left.toString());
                    if (!hashValueComparison) {
                        return new MatchResult();
                    }
                } else {
                    result.add(Utils.compareObj(right, left));
                }
            } catch (Exception e) {
                System.out.println("Impossibile confrontare con " + field.getName() + ": " + e.toString());
            }
        }
        return result;
    }

    public MatchResult compare(GeneralObject obj, JsonObject weights) {
        MatchResult result = new MatchResult();
        List<Field> fields = Utils.getAllFieldFromClass(this.getClass());
        for (Field field : fields) {
            if (field.getName().equals("attrs")) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object right = field.get(this);
                Object left = field.get(obj);
                if (field.getName().equals("hash")) {
                    Boolean hashValueComparison = right.toString().equals(left.toString());
                    if (!hashValueComparison) {
                        return new MatchResult();
                    }
                } else {
                    Float weight = 1.0f;
                    if (weights != null) {
                        JsonElement w = weights.get(field.getName());
                        if (w != null) {
                            weight = w.getAsFloat();
                        }
                    }
                    result.add(Utils.compareObj(right, left, weight));
                }
            } catch (Exception e) {
                System.out.println("Impossibile confrontare con " + field.getName() + ": " + e.toString());
            }
        }
        return result;
    }

}

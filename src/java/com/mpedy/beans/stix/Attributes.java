package com.mpedy.beans.stix;

import com.mpedy.beans.stix.sro.Relationship;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author cuore
 */
public class Attributes implements Serializable {

    private String verb;
    private GeneralObject obj;
    private GeneralObject obj_existing;
    private @Getter
    @Setter
    Relationship rel;
    private @Getter
    @Setter
    String value;
    private @Getter
    @Setter
    String value_1;
    private @Getter
    @Setter
    String value_2;

    private String oldId;

    public Attributes() {
        obj = new GeneralObject();
        rel = new Relationship();
    }

    @Override
    public String toString() {
        return "Attributes{" + "verb=" + verb + ", obj=" + obj + ", obj_existing=" + obj_existing + ", rel=" + rel + ", value=" + value + ", value_1=" + value_1 + ", value_2=" + value_2 + '}';
    }

    public GeneralObject getObj() {
        return obj;
    }

    public void setObj(GeneralObject obj) {
        System.out.println("setOBJ_EXISTING: " + (obj == null ? "null" : (obj.toGeneralString() + " - " + this.value)));
        this.obj = obj;
        if (obj != null) {
            this.obj_existing = null;
            this.rel.setRight(obj);
        }
    }

    public GeneralObject getObj_existing() {
        return obj_existing;
    }

    public void setObj_existing(GeneralObject obj_existing) {
        System.out.println("setOBJ_EXISTING: " + (obj_existing == null ? "null" : (obj_existing.toGeneralString() + " - " + this.value)));
        this.obj_existing = obj_existing;
        if (obj_existing != null) {
            this.obj = null;
            this.rel.setRight(obj_existing);
        }
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
        this.rel.setRelationship_type(verb);
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

}

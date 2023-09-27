package com.mpedy.beans.stix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cuore
 */
public class FieldVAL implements Serializable {

    public Object value;
    public Integer type;
    public List<Object> possibleValues;

    public FieldVAL(Object value, Integer type, List<Object> possibleValues) {
        this.value = value;
        this.type = type;
        if (possibleValues != null) {
            this.possibleValues = possibleValues;
        } else {
            this.possibleValues = new ArrayList<>();
        }
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Object> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<Object> possibleValues) {
        this.possibleValues = possibleValues;
    }

}

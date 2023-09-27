/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sdo;

import com.mpedy.beans.stix.GeneralObject;
import com.mpedy.beans.stix.vocabulary.PatternTypeOv;

/**
 *
 * @author matteo.provendola
 */
public class Indicator extends GeneralObject {

    private String pattern;
    private PatternTypeOv pattern_type;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public PatternTypeOv getPattern_type() {
        return pattern_type;
    }

    public void setPattern_type(PatternTypeOv pattern_type) {
        this.pattern_type = pattern_type;
    }

}

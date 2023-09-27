/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mpedy.beans.stix.vocabulary;

/**
 *
 * @author cuore
 */
public enum PatternTypeOv {
    STIX("stix"),
    PCRE("pcre"),
    SIGMA("sigma"),
    SNORT("snort"),
    SURICATA("suricata"),
    YARA("yara");
    private String value;

    PatternTypeOv(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}

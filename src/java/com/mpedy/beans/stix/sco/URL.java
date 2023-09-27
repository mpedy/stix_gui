/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sco;

import com.mpedy.beans.stix.ForAttributes;
import com.mpedy.beans.stix.GeneralObject;

/**
 *
 * @author cuore
 */
@ForAttributes
public class URL extends GeneralObject {
    
    private String value;
    
    public URL() {
        super();
        setImgSrc("images/url.png");
        setTypeForLabel("URL");
        setType("url");
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
}

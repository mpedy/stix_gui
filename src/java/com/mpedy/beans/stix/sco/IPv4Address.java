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
public class IPv4Address extends GeneralObject {

    private String value;

    public IPv4Address() {
        super();
        setImgSrc("images/ipv4_address.png");
        setTypeForLabel("IPv4");
        setType("ipv4-addr");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

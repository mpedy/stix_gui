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
public class IPv6Address extends GeneralObject {

    private String value;
    
    public IPv6Address() {
        super();
        setImgSrc("images/ipv6_address.png");
        setTypeForLabel("IPv6");
        setType("ipv6-addr");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sco;

import com.mpedy.beans.stix.GeneralObject;

/**
 *
 * @author cuore
 */
public class WindowsRegistryKey extends GeneralObject {

    private String key;
    private Integer number_of_subkeys;
    
    public WindowsRegistryKey(){
        super();
        setType("windows-registry-key");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getNumber_of_subkeys() {
        return number_of_subkeys;
    }

    public void setNumber_of_subkeys(Integer number_of_subkeys) {
        this.number_of_subkeys = number_of_subkeys;
    }

}

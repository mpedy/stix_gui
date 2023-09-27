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
public class Artifact extends GeneralObject {

    private String mime_type;
    private String url;

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

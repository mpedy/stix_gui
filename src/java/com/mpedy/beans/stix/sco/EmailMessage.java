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
public class EmailMessage extends GeneralObject {

    private Boolean is_multipart;
    private String body;
    private String subject;

    public Boolean getIs_multipart() {
        return is_multipart;
    }

    public void setIs_multipart(Boolean is_multipart) {
        this.is_multipart = is_multipart;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}

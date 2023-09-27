/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sdo;

import com.mpedy.beans.stix.GeneralObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cuore
 */
public class Note extends GeneralObject {

    private String content;
    private List<Identity> object_refs;

    public Note() {
        super();
        object_refs = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Identity> getObject_refs() {
        return object_refs;
    }

    public void setObject_refs(List<Identity> object_refs) {
        this.object_refs = object_refs;
    }

}

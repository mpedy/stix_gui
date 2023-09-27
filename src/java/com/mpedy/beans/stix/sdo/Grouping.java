/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sdo;

import com.mpedy.beans.stix.GeneralObject;
import com.mpedy.beans.stix.vocabulary.GroupingContextOv;

/**
 *
 * @author cuore
 */
public class Grouping extends GeneralObject {

    private GroupingContextOv context;

    public GroupingContextOv getContext() {
        return context;
    }

    public void setContext(GroupingContextOv context) {
        this.context = context;
    }

}

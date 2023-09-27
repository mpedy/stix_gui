/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sdo;

import com.mpedy.beans.stix.GeneralObject;
import com.mpedy.beans.stix.vocabulary.OpinionEnum;

/**
 *
 * @author cuore
 */
public class Opinion extends GeneralObject {

    private OpinionEnum opinion;

    public OpinionEnum getOpinion() {
        return opinion;
    }

    public void setOpinion(OpinionEnum opinion) {
        this.opinion = opinion;
    }

}

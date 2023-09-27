/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sdo;

import com.mpedy.beans.stix.GeneralObject;
import com.mpedy.beans.stix.NoPrint;
import java.time.LocalDateTime;

/**
 *
 * @author cuore
 */
public class Report extends GeneralObject {

    @NoPrint(true)
    private LocalDateTime published_time;
    private String published;

    public LocalDateTime getPublished_time() {
        return published_time;
    }

    public void setPublished_time(LocalDateTime published_time) {
        this.published_time = published_time;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

}

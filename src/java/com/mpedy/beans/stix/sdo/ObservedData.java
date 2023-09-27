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
public class ObservedData extends GeneralObject {

    @NoPrint(true)
    private LocalDateTime first_observed_time;
    private String first_observed;

    @NoPrint(true)
    private LocalDateTime last_observed_time;
    private String last_observed;

    private Integer number_observed;

    public LocalDateTime getFirst_observed_time() {
        return first_observed_time;
    }

    public void setFirst_observed_time(LocalDateTime first_observed_time) {
        this.first_observed_time = first_observed_time;
    }

    public String getFirst_observed() {
        return first_observed;
    }

    public void setFirst_observed(String first_observed) {
        this.first_observed = first_observed;
    }

    public LocalDateTime getLast_observed_time() {
        return last_observed_time;
    }

    public void setLast_observed_time(LocalDateTime last_observed_time) {
        this.last_observed_time = last_observed_time;
    }

    public String getLast_observed() {
        return last_observed;
    }

    public void setLast_observed(String last_observed) {
        this.last_observed = last_observed;
    }

    public Integer getNumber_observed() {
        return number_observed;
    }

    public void setNumber_observed(Integer number_observed) {
        this.number_observed = number_observed;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sco;

import com.mpedy.beans.stix.GeneralObject;
import java.time.LocalDateTime;

/**
 *
 * @author cuore
 */
public class Process extends GeneralObject {

    private Boolean is_hidden;
    private Integer pid;
    private String command_line;
    private LocalDateTime created_ref;

    public Boolean getIs_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(Boolean is_hidden) {
        this.is_hidden = is_hidden;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getCommand_line() {
        return command_line;
    }

    public void setCommand_line(String command_line) {
        this.command_line = command_line;
    }

    public LocalDateTime getCreated_ref() {
        return created_ref;
    }

    public void setCreated_ref(LocalDateTime created_ref) {
        this.created_ref = created_ref;
    }

}

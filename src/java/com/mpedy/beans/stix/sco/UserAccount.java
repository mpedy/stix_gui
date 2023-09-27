/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix.sco;

import com.mpedy.beans.stix.ForAttributes;
import com.mpedy.beans.stix.GeneralObject;
import com.mpedy.beans.stix.vocabulary.AccountTypeOv;
import java.time.LocalDateTime;

/**
 *
 * @author cuore
 */
@ForAttributes
public class UserAccount extends GeneralObject {

    private String user_id;
    private String credential;
    private String account_login;
    private AccountTypeOv account_type;
    private String display_name;
    private Boolean is_service_account;
    private Boolean is_privileged;
    private Boolean can_escalate_privs;
    private Boolean is_disabled;
    private LocalDateTime account_created;
    private LocalDateTime account_expires;
    private LocalDateTime credential_last_changed;
    private LocalDateTime account_first_login;
    private LocalDateTime account_last_login;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getAccount_login() {
        return account_login;
    }

    public void setAccount_login(String account_login) {
        this.account_login = account_login;
    }

    public AccountTypeOv getAccount_type() {
        return account_type;
    }

    public void setAccount_type(AccountTypeOv account_type) {
        this.account_type = account_type;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Boolean getIs_service_account() {
        return is_service_account;
    }

    public void setIs_service_account(Boolean is_service_account) {
        this.is_service_account = is_service_account;
    }

    public Boolean getIs_privileged() {
        return is_privileged;
    }

    public void setIs_privileged(Boolean is_privileged) {
        this.is_privileged = is_privileged;
    }

    public Boolean getCan_escalate_privs() {
        return can_escalate_privs;
    }

    public void setCan_escalate_privs(Boolean can_escalate_privs) {
        this.can_escalate_privs = can_escalate_privs;
    }

    public Boolean getIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(Boolean is_disabled) {
        this.is_disabled = is_disabled;
    }

    public LocalDateTime getAccount_created() {
        return account_created;
    }

    public void setAccount_created(LocalDateTime account_created) {
        this.account_created = account_created;
    }

    public LocalDateTime getAccount_expires() {
        return account_expires;
    }

    public void setAccount_expires(LocalDateTime account_expires) {
        this.account_expires = account_expires;
    }

    public LocalDateTime getCredential_last_changed() {
        return credential_last_changed;
    }

    public void setCredential_last_changed(LocalDateTime credential_last_changed) {
        this.credential_last_changed = credential_last_changed;
    }

    public LocalDateTime getAccount_first_login() {
        return account_first_login;
    }

    public void setAccount_first_login(LocalDateTime account_first_login) {
        this.account_first_login = account_first_login;
    }

    public LocalDateTime getAccount_last_login() {
        return account_last_login;
    }

    public void setAccount_last_login(LocalDateTime account_last_login) {
        this.account_last_login = account_last_login;
    }

}

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
public class X509Certificate extends GeneralObject {

    private Boolean is_self_signed;
    private String version;
    private String serial_number;
    private String signature_algorithm;
    private String issuer;
    private String subject;
    private LocalDateTime validity_not_before;
    private LocalDateTime validity_not_after;
    
    public X509Certificate(){
        super();
        setType("x509-certificate");
    }

    public Boolean getIs_self_signed() {
        return is_self_signed;
    }

    public void setIs_self_signed(Boolean is_self_signed) {
        this.is_self_signed = is_self_signed;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getSignature_algorithm() {
        return signature_algorithm;
    }

    public void setSignature_algorithm(String signature_algorithm) {
        this.signature_algorithm = signature_algorithm;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getValidity_not_before() {
        return validity_not_before;
    }

    public void setValidity_not_before(LocalDateTime validity_not_before) {
        this.validity_not_before = validity_not_before;
    }

    public LocalDateTime getValidity_not_after() {
        return validity_not_after;
    }

    public void setValidity_not_after(LocalDateTime validity_not_after) {
        this.validity_not_after = validity_not_after;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mpedy.beans.stix;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author cuore
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoJSON {
    
    boolean value() default true;
    
}

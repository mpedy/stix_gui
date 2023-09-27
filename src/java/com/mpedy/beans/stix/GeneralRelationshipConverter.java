/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix;

import com.mpedy.beans.stix.sro.Relationship;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author matteo.provendola
 */
@FacesConverter("generalRELConverter")
public class GeneralRelationshipConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            String[] pathnames = Utils.getAllSROClassNames();
            for (String s : pathnames) {
                s = s.replaceAll("\\.class", "");
                value = value.replaceAll("-", "");
                if (s.equalsIgnoreCase(value)) {
                    Relationship go = Utils.buildFromClassName(s, Utils.SRO_PREFIX);
                    return go;
                }
            }
        } catch (Exception e) {
            System.out.println("Non è stato possibile trovare alcun valore da: " + value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        return ((Relationship) value).getType();
    }

}

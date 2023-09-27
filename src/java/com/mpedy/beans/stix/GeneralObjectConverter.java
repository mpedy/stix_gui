/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author matteo.provendola
 */
@FacesConverter("generalOBJConverter")
public class GeneralObjectConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            System.out.println("getAsObject: " + value);
            List<GeneralObject> list = context.getApplication().evaluateExpressionGet(context, "#{stixGenerator}", StixGenerator.class).getSdolist();
            if (!list.isEmpty()) {
                for (GeneralObject obj : list) {
                    if (obj.getId().equals(value)) {
                        return obj;
                    }
                }
            }
            list = context.getApplication().evaluateExpressionGet(context, "#{stixGenerator}", StixGenerator.class).getAllsdo();
            if (!list.isEmpty()) {
                for (GeneralObject obj : list) {
                    if (obj.getId().equals(value)) {
                        GeneralObject obj1 = Utils.buildFromClassName(obj.getClass().getName());
                        return obj1;
                    }
                }
            }

//            String[] pathnames = Utils.getAllSDOClassNames();
//            for (String s : pathnames) {
//                s = s.replaceAll("\\.class", "");
//                value = value.replaceAll("-", "");
//                if (s.equalsIgnoreCase(value)) {
//                    GeneralObject go = Utils.buildFromClassName(s, Utils.SDO_PREFIX);
//                    System.out.println("Ritornando: " + go);
//                    return go;
//                }
//            }
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
//        return ((GeneralObject) value).getType(); -- old format
        return ((GeneralObject) value).getId();
//        return value.toString();
    }

}

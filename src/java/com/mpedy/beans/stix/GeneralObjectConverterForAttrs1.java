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
@FacesConverter("generalOBJConverterForAttrs1")
public class GeneralObjectConverterForAttrs1 implements Converter {

//    @Override
    public Object getAsObject_old1(FacesContext context, UIComponent component, String value) {
        try {
            GeneralObject sel = context.getApplication().evaluateExpressionGet(context, "#{stixGenerator}", StixGenerator.class).getSelectedObject();
            for (Attributes attr : sel.getAttrs()) {
                if (attr.getObj() != null && attr.getObj().getId().equals(value)) {
                    return attr.getObj();
                }
                if (attr.getObj_existing() != null && attr.getObj_existing().getId().equals(value)) {
                    return attr.getObj_existing();
                }
            }
            List<GeneralObject> list = context.getApplication().evaluateExpressionGet(context, "#{stixGenerator}", StixGenerator.class).getSdolist();
            if (!list.isEmpty()) {
                for (GeneralObject obj : list) {
                    if (obj.getId().equals(value)) {
                        return obj;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Non è stato possibile trovare alcun valore da: " + value);
        }
        System.out.println("GeneralObjectConverterForAttrs: " + value);
//        return getAsObject_old(context, component, value);
return null;

    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            System.out.println("getAsObjectForAttr: " + value);
            List<GeneralObject> list = context.getApplication().evaluateExpressionGet(context, "#{stixGenerator}", StixGenerator.class).getAllsdo_for_attrs();
            if (!list.isEmpty()) {
                for (GeneralObject obj : list) {
                    if (obj.getId().equals(value)) {
//                        return obj;
                        GeneralObject obj1 = Utils.buildFromClassName(obj.getClass().getName());
//                        obj1.setId(value);
                        return obj1;
                    }
                }
            }
//            list = context.getApplication().evaluateExpressionGet(context, "#{stixGenerator}", StixGenerator.class).getAllsdo();
//            if (!list.isEmpty()) {
//                for (GeneralObject obj : list) {
//                    if (obj.getId().equals(value)) {
//                        return obj;
//                    }
//                }
//            }

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
        System.out.println("GeneralObjectConverterForAttrs: " + value);
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

package com.mpedy.converters;

import com.mpedy.utils.Utils;
import java.util.regex.Pattern;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author matteo
 */
@FacesConverter("com.mpedy.converters.MyTimeConverter")
public class MyTimeConverter implements Converter {

//    private Pattern datePattern = Pattern.compile("'?\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}'?");
    private Pattern datePattern = Pattern.compile("'?\\d{2}:\\d{2}:\\d{2}'?");

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            value = Utils.tFormat(Integer.parseInt(value.toString()));
        }
        if (value != null && datePattern.matcher(value.toString()).matches()) {
            return value.toString();
        }
        return null;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0 && datePattern.matcher(value).matches()) {
            Integer tmp = 0;
            String[] tmps = value.split(":");
            tmp += Integer.parseInt(tmps[2]);
            tmp += Integer.parseInt(tmps[1]) * 60;
            tmp += Integer.parseInt(tmps[0]) * 60 * 60;
            return (Integer) tmp;
        }
        return null;
    }

}

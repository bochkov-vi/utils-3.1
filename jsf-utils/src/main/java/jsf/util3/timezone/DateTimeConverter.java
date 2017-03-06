/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.timezone;

import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.component.calendar.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author viktor
 */


public class DateTimeConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object result = null;
        if (!Strings.isNullOrEmpty(value)) {
            if (component instanceof Calendar) {
                Calendar ui = (Calendar) component;
                result = createDateTimeFormatter(ui).parseDateTime(value);
            } else {
                result = createDateTimeFormatter().parseDateTime(value);
            }
        }
        return result;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = "";
        if (value instanceof DateTime) {
            if (component instanceof Calendar) {
                Calendar ui = (Calendar) component;
                result = ((DateTime) value).toString(createDateTimeFormatter(ui));
            } else {
                result = ((DateTime) value).toString(createDateTimeFormatter());
            }
        }
        return result;
    }

    protected DateTimeFormatter createDateTimeFormatter(Calendar ui) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(ui.getPattern());
        String zone = (String) ui.getTimeZone();
        if (!Strings.isNullOrEmpty(zone)) {
            formatter = formatter.withZone(DateTimeZone.forID(zone));
        }
        return formatter;
    }

    protected DateTimeFormatter createDateTimeFormatter() {
        DateTimeFormatter formatter = DateTimeFormat.shortDate();
        return formatter;
    }

}

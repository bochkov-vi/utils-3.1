/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.timezone;

import com.google.common.base.Strings;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.component.calendar.Calendar;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author viktor
 */
@Component
public class LocalDateConverter implements Converter, org.springframework.core.convert.converter.Converter<String, LocalDate> {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object result = null;
        if (component instanceof Calendar) {
            Calendar ui = (Calendar) component;
            result = createDateTimeFormatter(ui).parseLocalDate(value);

        } else {
            result = convert(value);
        }
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = "";
        if (component instanceof Calendar) {
            if (value instanceof LocalDate) {
                Calendar ui = (Calendar) component;
                result = ((LocalDate) value).toString(createDateTimeFormatter(ui));
            }
        } else if (value instanceof LocalDate) {
            return value.toString();
        }
        return result;
    }

    @Override
    public LocalDate convert(String s) {
        if (!Strings.isNullOrEmpty(s)) {
            return LocalDate.parse(s);
        }
        return null;
    }

    public String toString(LocalDate s) {
        if (s != null) {
            return s.toString();
        }
        return "";
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

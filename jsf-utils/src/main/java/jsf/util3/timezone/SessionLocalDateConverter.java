/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.timezone;

import com.google.common.base.Strings;
import jsf.util3.service.TimeZoneProvider;
import org.joda.time.LocalDate;
import org.primefaces.component.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author viktor
 */
@Component
@Scope("session")
public class SessionLocalDateConverter extends DateTimeFormatterable implements Converter, org.springframework.core.convert.converter.Converter<String, LocalDate> {
    @Autowired(required = false)
    TimeZoneProvider timeZoneProvider;

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
            return createDateTimeFormatter().parseLocalDate(s);
        }
        return null;
    }
}

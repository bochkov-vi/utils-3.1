/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timezone;

import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.primefaces.component.calendar.Calendar;
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
public class SessionDateConverter extends DateTimeFormatterable implements Converter {

    @Override
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

    @Override
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
}

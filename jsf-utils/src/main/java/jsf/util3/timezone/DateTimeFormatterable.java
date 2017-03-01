/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.timezone;

import com.google.common.base.Strings;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.component.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimeZone;

/**
 * @author viktor
 */

public abstract class DateTimeFormatterable {
    @Autowired(required = false)
    TimeZoneProvider timeZoneProvider;


    protected DateTimeFormatter createDateTimeFormatter(Calendar ui) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(ui.getPattern());
        String zone = (String) ui.getTimeZone();
        if (!Strings.isNullOrEmpty(zone)) {
            formatter = formatter.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(zone)));
        }
        return formatter;
    }

    protected DateTimeFormatter createDateTimeFormatter() {
        DateTimeFormatter formatter = DateTimeFormat.shortDate();
        if (timeZoneProvider != null && timeZoneProvider.getDateTimeZone() != null) {
            formatter = formatter.withZone(timeZoneProvider.getDateTimeZone());
        }
        return formatter;
    }

    protected DateTimeFormatter createDateTimeFormatter(String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        if (timeZoneProvider != null && timeZoneProvider.getDateTimeZone() != null) {
            formatter = formatter.withZone(timeZoneProvider.getDateTimeZone());
        }
        return formatter;
    }
}

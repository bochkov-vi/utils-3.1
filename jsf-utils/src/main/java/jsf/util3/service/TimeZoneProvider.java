package jsf.util3.service;

import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public interface TimeZoneProvider {

    TimeZone getTimeZone();

    DateTimeZone getDateTimeZone();

    void setTimeZone(TimeZone timeZone);

    String getTimeZoneId();
}

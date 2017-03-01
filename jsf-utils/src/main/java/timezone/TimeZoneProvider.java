/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timezone;

import org.joda.time.DateTimeZone;

import java.util.TimeZone;

/**
 * @author viktor
 */
public interface TimeZoneProvider {

    TimeZone getTimeZone();

    DateTimeZone getDateTimeZone();

    void setTimeZone(TimeZone zone);

    String getTimeZoneId();

    void setTimeZoneId(String id);

}

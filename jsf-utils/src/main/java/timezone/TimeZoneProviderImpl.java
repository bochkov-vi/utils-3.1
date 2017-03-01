/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timezone;

import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author viktor
 */
@Component("timeZoneProvider")
@Scope("session")
public class TimeZoneProviderImpl implements TimeZoneProvider, Serializable {

    private TimeZone timeZone = TimeZone.getDefault();

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public DateTimeZone getDateTimeZone() {
        return DateTimeZone.forTimeZone(getTimeZone());
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String getTimeZoneId() {
        return timeZone.getID();
    }

    @Value("GMT+12")
    @Override
    public void setTimeZoneId(String id) {
        try {
            this.timeZone = TimeZone.getTimeZone(id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ошибка смены TimeZone", e);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.timezone;

import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author viktor
 */
@Component
public class TimeZoneProvider  implements Serializable{

    private TimeZone timeZone = TimeZone.getDefault();

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public DateTimeZone getDateTimeZone() {
        return DateTimeZone.forTimeZone(getTimeZone());
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeZoneId() {
        return timeZone.getID();
    }

    @Value("Asia/Kamchatka")
    public void setTimeZoneId(String id) {
        try {
            this.timeZone = TimeZone.getTimeZone(id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ошибка смены TimeZone", e);
        }
    }

}

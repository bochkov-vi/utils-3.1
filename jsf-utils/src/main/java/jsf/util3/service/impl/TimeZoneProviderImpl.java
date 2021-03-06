package jsf.util3.service.impl;

import jsf.util3.service.TimeZoneProvider;
import org.joda.time.DateTimeZone;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("timeZoneProvider")
@Scope("session")
public class TimeZoneProviderImpl implements TimeZoneProvider {

    @PostConstruct
    public void postConstruct() {
        LocaleContextHolder.setDefaultTimeZone(TimeZone.getTimeZone("Asia/Kamchatka"));
    }

    @Override
    public TimeZone getTimeZone() {
        return LocaleContextHolder.getTimeZone();
    }

    @Override
    public DateTimeZone getDateTimeZone() {
        return DateTimeZone.forTimeZone(getTimeZone());
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        LocaleContextHolder.setTimeZone(timeZone);
    }

    @Override
    public String getTimeZoneId() {
        return getTimeZone().getID();
    }

    public void setTimeZoneId(String id) {
        try {
            setTimeZone(TimeZone.getTimeZone(id));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ошибка смены TimeZone", e);
        }
    }

}

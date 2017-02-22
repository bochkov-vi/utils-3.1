/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.converter;

import org.joda.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Time;

/**
 * @author bochkov
 */
@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time> {

    public Time convertToDatabaseColumn(LocalTime attribute) {
        return attribute == null ? null : new Time(attribute.toDateTimeToday().getMillis());
    }

    public LocalTime convertToEntityAttribute(Time dbData) {
        return dbData == null ? null : new LocalTime(dbData);
    }

}

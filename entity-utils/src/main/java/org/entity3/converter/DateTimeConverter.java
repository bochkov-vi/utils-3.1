/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.converter;

import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

/**
 * @author bochkov
 */
@Converter(autoApply = true)
public class DateTimeConverter implements AttributeConverter<DateTime, Timestamp> {

    public Timestamp convertToDatabaseColumn(DateTime attribute) {
        return attribute == null ? null : new Timestamp(((DateTime) attribute).getMillis());
    }

    public DateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData == null ? null : new DateTime((Timestamp) dbData);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.converter;

import org.joda.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;

/**
 * @author bochkov
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    public Date convertToDatabaseColumn(LocalDate attribute) {
        return attribute == null ? null : new Date(attribute.toDateTimeAtStartOfDay().getMillis());
    }

    public LocalDate convertToEntityAttribute(Date dbData) {
        return dbData == null ? null : new LocalDate(dbData);
    }

}

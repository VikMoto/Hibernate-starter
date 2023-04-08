package com.vmdev.convertor;

import com.vmdev.entity.BirthDay;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

import java.sql.Date;
import java.util.Optional;
import java.util.OptionalInt;

@Converter(autoApply = true)
public class BirthDateConvertor implements AttributeConverter<BirthDay, Date> {

    @Override
    public Date convertToDatabaseColumn(BirthDay attribute) {
        return Optional.ofNullable(attribute)
                .map(BirthDay::birthDate)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public BirthDay convertToEntityAttribute(Date dbData) {
        return Optional.ofNullable(dbData)
                .map(Date::toLocalDate)
                .map(BirthDay::new)
                .orElse(null);
    }
}

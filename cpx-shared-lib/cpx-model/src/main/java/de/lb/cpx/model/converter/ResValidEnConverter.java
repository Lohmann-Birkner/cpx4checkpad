/* 
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.converter;

import de.lb.cpx.model.enums.ResValidEn;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Shahin
 */
@Converter
public class ResValidEnConverter implements AttributeConverter<ResValidEn, Integer> {

    private final EnumIdConverter<ResValidEn, Integer> converterDelegate;

    public ResValidEnConverter() {
        converterDelegate = new EnumIdConverter<ResValidEn, Integer>() {
            @Override
            protected ResValidEn[] getEnumValues() {
                return ResValidEn.values();
            }
        };
    }

    @Override
    public Integer convertToDatabaseColumn(final ResValidEn attribute) {
        return converterDelegate.convertToDatabaseColumn(attribute);
    }

    @Override
    public ResValidEn convertToEntityAttribute(final Integer dbData) {
        return converterDelegate.convertToEntityAttribute(dbData);
    }

}

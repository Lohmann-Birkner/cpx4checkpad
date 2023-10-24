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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.converter;

import de.lb.cpx.model.enums.FeeGroupEn;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FeeGroupConverter implements AttributeConverter<FeeGroupEn, Integer> {

    private final EnumIdConverter<FeeGroupEn, Integer> converterDelegate;

    public FeeGroupConverter() {
        converterDelegate = new EnumIdConverter<FeeGroupEn, Integer>() {
            @Override
            protected FeeGroupEn[] getEnumValues() {
                return FeeGroupEn.values();
            }
        };
    }

    @Override
    public Integer convertToDatabaseColumn(final FeeGroupEn attribute) {
        return converterDelegate.convertToDatabaseColumn(attribute);
    }

    @Override
    public FeeGroupEn convertToEntityAttribute(final Integer dbData) {
        return converterDelegate.convertToEntityAttribute(dbData);
    }

}

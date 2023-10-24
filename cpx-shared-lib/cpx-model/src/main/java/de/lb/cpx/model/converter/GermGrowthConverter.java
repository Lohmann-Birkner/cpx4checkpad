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

import de.lb.cpx.model.enums.GermGrowthEn;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class GermGrowthConverter implements AttributeConverter<GermGrowthEn, Integer> {

    private final EnumIdConverter<GermGrowthEn, Integer> converterDelegate;

    public GermGrowthConverter() {
        converterDelegate = new EnumIdConverter<GermGrowthEn, Integer>() {
            @Override
            protected GermGrowthEn[] getEnumValues() {
                return GermGrowthEn.values();
            }
        };
    }

    @Override
    public Integer convertToDatabaseColumn(final GermGrowthEn attribute) {
        return converterDelegate.convertToDatabaseColumn(attribute);
    }

    @Override
    public GermGrowthEn convertToEntityAttribute(final Integer dbData) {
        return converterDelegate.convertToEntityAttribute(dbData);
    }

}

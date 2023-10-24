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

import de.lb.cpx.model.enums.IdentClassEn;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IdentClassConverter implements AttributeConverter<IdentClassEn, String> {

    private final EnumIdConverter<IdentClassEn, String> converterDelegate;

    public IdentClassConverter() {
        converterDelegate = new EnumIdConverter<IdentClassEn, String>() {
            @Override
            protected IdentClassEn[] getEnumValues() {
                return IdentClassEn.values();
            }
        };
    }

    @Override
    public String convertToDatabaseColumn(final IdentClassEn attribute) {
        return converterDelegate.convertToDatabaseColumn(attribute);
    }

    @Override
    public IdentClassEn convertToEntityAttribute(final String dbData) {
        return converterDelegate.convertToEntityAttribute(dbData);
    }

}

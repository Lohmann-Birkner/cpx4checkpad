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
package de.lb.cpx.wm.converter;

import de.lb.cpx.model.converter.EnumIdConverter;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EventTypeConverter implements AttributeConverter<WmEventTypeEn, Integer> {

    private final EnumIdConverter<WmEventTypeEn, Integer> converterDelegate;

    public EventTypeConverter() {
        converterDelegate = new EnumIdConverter<WmEventTypeEn, Integer>() {
            @Override
            protected WmEventTypeEn[] getEnumValues() {
                return WmEventTypeEn.values();
            }
        };
    }

    @Override
    public Integer convertToDatabaseColumn(final WmEventTypeEn attribute) {
        return converterDelegate.convertToDatabaseColumn(attribute);
    }

    @Override
    public WmEventTypeEn convertToEntityAttribute(final Integer dbData) {
        return converterDelegate.convertToEntityAttribute(dbData);
    }

}

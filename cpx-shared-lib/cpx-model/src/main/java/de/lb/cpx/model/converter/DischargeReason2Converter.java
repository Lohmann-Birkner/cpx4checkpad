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

import de.lb.cpx.model.enums.DischargeReason2En;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DischargeReason2Converter implements AttributeConverter<DischargeReason2En, String> {

    private final EnumIdConverter<DischargeReason2En, String> converterDelegate;

    public DischargeReason2Converter() {
        converterDelegate = new EnumIdConverter<DischargeReason2En, String>() {
            @Override
            protected DischargeReason2En[] getEnumValues() {
                return DischargeReason2En.values();
            }
        };
    }

    @Override
    public String convertToDatabaseColumn(final DischargeReason2En attribute) {
        return converterDelegate.convertToDatabaseColumn(attribute);
    }

    @Override
    public DischargeReason2En convertToEntityAttribute(final String dbData) {
        return converterDelegate.convertToEntityAttribute(dbData);
    }

}

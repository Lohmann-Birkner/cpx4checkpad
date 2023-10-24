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

import de.lb.cpx.model.enums.AdmissionReason2En;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AdmissionReason2Converter implements AttributeConverter<AdmissionReason2En, String> {

    private final EnumIdConverter<AdmissionReason2En, String> converterDelegate;

    public AdmissionReason2Converter() {
        converterDelegate = new EnumIdConverter<AdmissionReason2En, String>() {
            @Override
            protected AdmissionReason2En[] getEnumValues() {
                return AdmissionReason2En.values();
            }
        };
    }

    @Override
    public String convertToDatabaseColumn(final AdmissionReason2En attribute) {
        return converterDelegate.convertToDatabaseColumn(attribute);
    }

    @Override
    public AdmissionReason2En convertToEntityAttribute(final String dbData) {
        return converterDelegate.convertToEntityAttribute(dbData);
    }

}

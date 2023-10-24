/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.converter;

import de.lb.cpx.model.converter.EnumIdConverter;
import de.lb.cpx.wm.model.enums.WmAuditTypeEn;
import javax.persistence.AttributeConverter;

/**
 * Convert AuditType to database value and vice versa
 *
 * @author wilde
 */
public class AuditTypeConverter implements AttributeConverter<WmAuditTypeEn, Integer> {

    private final EnumIdConverter<WmAuditTypeEn, Integer> converterDelegate;

    public AuditTypeConverter() {
        converterDelegate = new EnumIdConverter<WmAuditTypeEn, Integer>() {
            @Override
            protected WmAuditTypeEn[] getEnumValues() {
                return WmAuditTypeEn.values();
            }
        };
    }

    @Override
    public Integer convertToDatabaseColumn(WmAuditTypeEn attribute) {
        return converterDelegate.convertToDatabaseColumn(attribute);
    }

    @Override
    public WmAuditTypeEn convertToEntityAttribute(Integer dbData) {
        return converterDelegate.convertToEntityAttribute(dbData);
    }

}

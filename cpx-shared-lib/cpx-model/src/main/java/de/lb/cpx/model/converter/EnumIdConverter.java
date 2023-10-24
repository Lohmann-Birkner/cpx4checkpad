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

//import de.lb.cpx.model.enums.IdEnum;
import de.lb.cpx.model.enums.CpxEnumInterface;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public abstract class EnumIdConverter<X extends CpxEnumInterface<Y>, Y> implements AttributeConverter<X, Y> {

    @Override
    public Y convertToDatabaseColumn(final X attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getId();
    }

    @Override
    public X convertToEntityAttribute(final Y dbData) {
        if (dbData == null) {
            return null;
        }

        for (final X enumValue : getEnumValues()) {
            if (enumValue.getId().equals(dbData)) {
                return enumValue;
            }
        }

        return null;
    }

    protected abstract X[] getEnumValues();
}

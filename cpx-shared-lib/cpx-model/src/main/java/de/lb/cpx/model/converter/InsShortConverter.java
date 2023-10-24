/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
//package de.lb.cpx.model.converter;
//
//import de.lb.cpx.model.enums.InsShortEn;
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
//@Converter
//public class InsShortConverter implements AttributeConverter<InsShortEn, String> {
//
//    private final EnumIdConverter<InsShortEn, String> converterDelegate;
//
//    public InsShortConverter() {
//        converterDelegate = new EnumIdConverter<InsShortEn, String>() {
//            @Override
//            protected InsShortEn[] getEnumValues() {
//                return InsShortEn.values();
//            }
//        };
//    }
//
//    @Override
//    public String convertToDatabaseColumn(final InsShortEn attribute) {
//        return converterDelegate.convertToDatabaseColumn(attribute);
//    }
//
//    @Override
//    public InsShortEn convertToEntityAttribute(final String dbData) {
//        return converterDelegate.convertToEntityAttribute(dbData);
//    }
//
//}

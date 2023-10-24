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
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import java.util.logging.Logger;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter Class to store WorkflowState Enum in database Return String instead
 * of int to avoid db changes TODO: Change in T_WM_PROCESS column WM_STATE
 * accordingly
 *
 * @author wilde
 */
@Converter
public class WorkflowStateConverter implements AttributeConverter<WmWorkflowStateEn, String> {

    private static final Logger LOG = Logger.getLogger(WorkflowStateConverter.class.getName());
    private final EnumIdConverter<WmWorkflowStateEn, Integer> converterDelegate;

    public WorkflowStateConverter() {
        converterDelegate = new EnumIdConverter<WmWorkflowStateEn, Integer>() {
            @Override
            protected WmWorkflowStateEn[] getEnumValues() {
                return WmWorkflowStateEn.values();
            }
        };
    }

    @Override
    public String convertToDatabaseColumn(WmWorkflowStateEn attribute) {
        return String.valueOf(converterDelegate.convertToDatabaseColumn(attribute));
    }

    @Override
    public WmWorkflowStateEn convertToEntityAttribute(String dbData) {
        try {
            return converterDelegate.convertToEntityAttribute(Integer.valueOf(dbData));
        } catch (NumberFormatException ex) {
            LOG.severe("Can not parse " + dbData + " to WorkflowState! Set State to geschlossen");
            return WmWorkflowStateEn.geschlossen;
        }
    }

}

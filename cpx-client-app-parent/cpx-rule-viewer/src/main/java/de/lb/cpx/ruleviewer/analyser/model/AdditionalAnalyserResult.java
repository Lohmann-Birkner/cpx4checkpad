/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.analyser.model;

import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 *
 * @author wilde
 */
public class AdditionalAnalyserResult extends Control {

    private static final Logger LOG = Logger.getLogger(AdditionalAnalyserResult.class.getName());

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AdditionalAnalyserResultSkin(this);
    }

    private final ReadOnlyIntegerWrapper columnCountProperty = new ReadOnlyIntegerWrapper(1);

    public ReadOnlyIntegerProperty columnCountProperty() {
        return columnCountProperty.getReadOnlyProperty();
    }

    public Integer getColumnCount() {
        return columnCountProperty().get();
    }

    public void setColumnCount(int pColumnCount) {
        if (pColumnCount < 1) {
            LOG.log(Level.WARNING, "column count must be greater than 1, ignore value: {0} ,resume using {1}", new Object[]{pColumnCount, getColumnCount()});
            return;
        }
        columnCountProperty.set(pColumnCount);
    }

    private ObjectProperty<TransferRuleAnalyseResult> additionalResultProperty;

    public ObjectProperty<TransferRuleAnalyseResult> additionalResultProperty() {
        if (additionalResultProperty == null) {
            additionalResultProperty = new SimpleObjectProperty<>();
        }
        return additionalResultProperty;
    }

    public void setAdditionalResult(TransferRuleAnalyseResult pResult) {
        additionalResultProperty().set(pResult);
    }

    public TransferRuleAnalyseResult getAdditionalResult() {
        return additionalResultProperty().get();
    }
}

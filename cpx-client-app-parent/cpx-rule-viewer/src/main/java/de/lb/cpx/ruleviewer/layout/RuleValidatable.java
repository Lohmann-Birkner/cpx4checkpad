/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import javafx.beans.property.ObjectProperty;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public interface RuleValidatable {
    
    public abstract ObjectProperty<Callback<String,String>> codeSuggestionCallbackProperty();
    public abstract Callback<String,String> getCodeSuggestionCallback();
    public abstract void setCodeSuggestionCallback(Callback<String,String> pCallback);
    
    public abstract ObjectProperty<Callback<CrgRuleTables,byte[]>> validationCalllbackProperty();
    public abstract Callback<CrgRuleTables, byte[]> getValidationCalllback();
    public abstract void setValidationCalllback(Callback<CrgRuleTables, byte[]> pCallback);
}

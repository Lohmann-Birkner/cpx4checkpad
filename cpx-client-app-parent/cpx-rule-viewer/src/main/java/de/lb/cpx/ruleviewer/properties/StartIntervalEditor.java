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
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import javafx.collections.MapChangeListener;
import org.controlsfx.control.PropertySheet;

/**
 * Start interval editor
 *
 * @author wilde
 */
public class StartIntervalEditor extends BasicIntervallEditor {

    public StartIntervalEditor(PropertySheet.Item pItem) {
        super(pItem);
        getIntervals().addAll(TypesAndOperationsManager.instance().getAllIntervals());
        getProperties().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (BasicIntervallEditor.SAVE_VALUE.equals(change.getKey())) {
                        String val = getValue();
                        getTerm().setIntervalFrom(val);
                        if(val.isEmpty()){
                            getTerm().setIntervalTo("");
                        }
                        getProperties().remove(BasicIntervallEditor.SAVE_VALUE);
                    }
                }
            }
        });
    }

    @Override
    public String getIntervall() {
        return getTerm().getIntervalFrom();
    }

}

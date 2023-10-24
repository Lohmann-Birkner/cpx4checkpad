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
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import org.controlsfx.control.PropertySheet;

/**
 * End Interval editor
 *
 * @author wilde
 */
public class EndIntervalEditor extends BasicIntervallEditor {

    public EndIntervalEditor(PropertySheet.Item pItem) {
        super(pItem);
//        getIntervals().setAll(TypesAndOperationsManager.instance().getNoIntervals());
        getTerm().intervalToProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    getIntervals().clear();
                    return;
                }
//                String interval = newValue.split(":")[0];
//                TypesAndOperations.IntervalLimits.IntervalLimit selected = getSelectedInterval();
//                getIntervals().setAll(TypesAndOperationsManager.instance().getIntervals(interval));
//                selectInterval(selected);
                setValue(newValue);
            }
        });
        getTerm().intervalFromProperty().addListener(new ChangeListener<String>() { //update selection when from changes
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    getIntervals().clear();
                    return;
                }
                String interval = newValue.split(":")[0];
                TypesAndOperations.IntervalLimits.IntervalLimit selected = getSelectedInterval();
                getIntervals().setAll(TypesAndOperationsManager.instance().getIntervals(interval));
                selectInterval(selected);
            }
        });
        getProperties().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (BasicIntervallEditor.SAVE_VALUE.equals(change.getKey())) {
                        getTerm().setIntervalTo(getValue());
                        getProperties().remove(BasicIntervallEditor.SAVE_VALUE);
                    }
                }
            }
        });
    }

    @Override
    public String getIntervall() {
        return getTerm().getIntervalTo();
    }

}

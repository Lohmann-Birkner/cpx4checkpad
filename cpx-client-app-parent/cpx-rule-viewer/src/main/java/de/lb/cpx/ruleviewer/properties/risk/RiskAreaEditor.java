/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties.risk;

import de.lb.cpx.rule.element.model.RiskArea;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;

/**
 *
 * @author wilde
 */
public class RiskAreaEditor extends RiskEditor {
    private ComboBox<RiskAreaEn> chkRiskArea;


    public RiskAreaEditor(PropertySheet.Item property) {
        super(property);
    }

    @Override
    public Node getEditor() {
        if (chkRiskArea == null) {
            chkRiskArea = new ComboBox<>();
            chkRiskArea.setMaxWidth(Double.MAX_VALUE);
            chkRiskArea.setMinWidth(70);
            chkRiskArea.setPrefWidth(150);
            chkRiskArea.getItems().addAll(RiskAreaEn.values());
            chkRiskArea.setConverter(new StringConverter<RiskAreaEn>() {
                @Override
                public String toString(RiskAreaEn t) {
                    return t != null ? t.getTranslation().getValue() : "";
                }

                @Override
                public RiskAreaEn fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            chkRiskArea.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RiskAreaEn>() {
                @Override
                public void changed(ObservableValue<? extends RiskAreaEn> observable, RiskAreaEn oldValue, RiskAreaEn newValue) {
                    addRiskArea(newValue);
                    removeRiskArea(oldValue);
                    checkEmptyRiskArea();
                    getRiskItem().getProperties().put(RiskEditorItem.UPDATE_RISK, null);
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(chkRiskArea, saveEvent);
                }
                private void addRiskArea(RiskAreaEn add) {
                    if(add == null){
                        return;
                    }
                    getRiskItem().addRiskArea(add.name());
                }

                private void removeRiskArea(RiskAreaEn removed) {
                    if(removed == null){
                        return;
                    }
                    getRiskItem().removeRiskArea(removed.name());
                }

                private void checkEmptyRiskArea() {
                    getRiskItem().checkEmptyRiskArea();
                }
            });
        }
        return chkRiskArea;
    }

    @Override
    public String getValue() {
        getEditor();
        RiskAreaEn selected = chkRiskArea.getSelectionModel().getSelectedItem();
        return selected!=null?selected.getTranslation().getValue():null;
    }

    @Override
    public void setValue(String value) {
        getEditor();
        for (RiskArea riskArea : getRiskItem().getRisk().getRiskAreas()) {
            RiskAreaEn area = RiskAreaEn.valueOf(riskArea.getRiskAreaName());
            if (area != null) {
                chkRiskArea.getSelectionModel().select(area);//getCheckModel().check(area);
                //stop after first successful find
                break;
            }
        }
    }
}

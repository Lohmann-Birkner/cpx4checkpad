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
package de.lb.cpx.client.core.model.fx.tableview.column.rules;

import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Column class to implement rule number Shows info box to display rule notice
 *
 * @author wilde
 */
public class RuleNumberColumn extends StringColumn<CpxSimpleRuleDTO> {

    /**
     * creates new instance with default header enables extended info to show
     * rule notice
     */
    public RuleNumberColumn() {
        super(Lang.getCaseResolveRulesId());
        setCellFactory(new Callback<TableColumn<CpxSimpleRuleDTO, String>, TableCell<CpxSimpleRuleDTO, String>>() {
            @Override
            public TableCell<CpxSimpleRuleDTO, String> call(TableColumn<CpxSimpleRuleDTO, String> param) {
                return new TableCell<CpxSimpleRuleDTO, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }
                        //20200817:remove extendedInfo, autoFit does not work properly
                        //resulting in wrongly placed popover - at this point fixing the issue 
                        //would take to much time for little gain functionality wise
//                        CpxSimpleRuleDTO value = getTableView().getItems().get(getIndex());
//                        if (value != null) {
                            Label label = new Label(item);
//                            if (value.getRuleNotice() != null && !value.getRuleNotice().isEmpty()) {
//                                Label notice = new Label(value.getRuleNotice());
//                                notice.setStyle("-fx-text-fill:black;");
//                                notice.setWrapText(true);
//                                Pane graph = ExtendedInfoHelper.addInfoPane(label, notice, PopOver.ArrowLocation.TOP_LEFT);
//                                setGraphic(graph);
//                            } else {
                                setGraphic(label);
//                            }
//                        }
                    }

                };
            }
        });
        setMinWidth(80.0);
        setMaxWidth(80.0);
        setResizable(false);
//        widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                LOG.info("width " + newValue.doubleValue());
//            }
//        });
    }

    @Override
    public String extractValue(CpxSimpleRuleDTO pValue) {
        return pValue.getRuleNumber();
    }

}

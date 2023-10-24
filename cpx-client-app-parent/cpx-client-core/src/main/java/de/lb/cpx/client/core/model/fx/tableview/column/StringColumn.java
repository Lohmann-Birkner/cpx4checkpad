/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tableview.column;

import de.lb.cpx.client.core.model.fx.tableview.column.enums.OverrunStyleEn;
import de.lb.cpx.client.core.util.OverrunHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author wilde
 * @param <T> table item
 */
public abstract class StringColumn<T> extends TableColumn<T, String> {

    private static final Logger LOG = Logger.getLogger(StringColumn.class.getName());
    protected static final String DEFAULT = "----";

    public StringColumn(String pTitle) {
        super(pTitle);

        setCellValueFactory(new Callback<CellDataFeatures<T, String>, ObservableValue<String>>() {
//            private Map<T, StringProperty> valueMap = new HashMap<>();

            @Override
            public ObservableValue<String> call(CellDataFeatures<T, String> param) {
                if (param.getValue() != null) {
                    try {
                        T val = param.getValue();
//                        StringProperty property = valueMap.get(val);
//                        if (property == null) {
//                            StringProperty property = getProperty(val);
//                            valueMap.put(val, property);
//                        }
                        SimpleStringProperty property = new SimpleStringProperty();
                        property.set(getDisplayText(val));
                        return property;
                    } catch (ClassCastException ex) {
                        LOG.log(Level.WARNING, "ItemValue is not expected Type!", ex);
                    }
                }
                return null;
            }

        });
    }

    protected String getDisplayText(T pValue) {
        String text = extractValue(pValue);
        if (text != null && !text.isEmpty()) {
            return text;
        } else {
            return DEFAULT;
        }
    }

    public StringColumn(String pTitle, boolean pOverrunable) {
        this(pTitle);
        if (pOverrunable) {
            setCellFactory(new Callback<TableColumn<T, String>, TableCell<T, String>>() {
                @Override
                public TableCell<T, String> call(TableColumn<T, String> param) {
                    return new TableCell<T, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            //sets label in topleft, to prevent layout error 
                            setGraphic(createGraphic(item, pOverrunable));
//                            Label label = new Label(item);
//                            setGraphic(label);
//                            OverrunHelper.addOverrunInfoButton(label, item);
                        }

                    };
                }
            });
        }
    }

    public StringColumn(String pTitle, OverrunStyleEn overrunStyleEn) {
        this(pTitle);
        if (!OverrunStyleEn.None.equals(overrunStyleEn)) {
            setCellFactory(new Callback<TableColumn<T, String>, TableCell<T, String>>() {
                @Override
                public TableCell<T, String> call(TableColumn<T, String> param) {
                    return new TableCell<T, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            //sets label in topleft, to prevent layout error
                            setGraphic(createGraphic(item, overrunStyleEn));
//                            Label label = new Label(item);
//                            setGraphic(label);
//                            if (OverrunStyleEn.Tooltip.equals(overrunStyleEn)) {
//                                OverrunHelper.addInfoTooltip(label, item, empty);
////                                OverrunHelper.addInfoTooltip(label);
//                            } else if (OverrunStyleEn.Button.equals(overrunStyleEn)) {
//                                OverrunHelper.addOverrunInfoButton(label, item);
//                            }
                        }

                    };
                }
            });
        } else {
            // if overrunStyle is None, don't set CellFactory. do something else?
        }
    }

    protected Node createGraphic(String pItem) {
        Label label = new Label(pItem);
        return label;
    }

    protected Node createGraphic(String pItem, OverrunStyleEn pStyle) {
        Node node = createGraphic(pItem);
        if (node instanceof Labeled) {
            if (OverrunStyleEn.Tooltip.equals(pStyle)) {
                OverrunHelper.addInfoTooltip((Labeled) node, pItem, false);
                //                                OverrunHelper.addInfoTooltip(label);
            } else if (OverrunStyleEn.Button.equals(pStyle)) {
                OverrunHelper.addOverrunInfoButton((Label) node, pItem);
            }
        }
        return node;
    }

    protected Node createGraphic(String pItem, Boolean pOverrun) {
        if (pOverrun) {
            return createGraphic(pItem, OverrunStyleEn.Button);
        }
        return createGraphic(pItem);
    }

    /**
     * @param pValue extract value to display as string
     * @return string value to display in cell
     */
    public abstract String extractValue(T pValue);

//    public StringProperty getProperty(T pValue) {
//        return new SimpleStringProperty();
//    }
//    public void setValue(int pIndex,String pValue){
//        Platform.runLater(new Runnable(){
//            @Override
//            public void run() {
//                
//            }
//        });
//    }
}

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
package de.lb.cpx.client.app.cm.fx.department.model;

import de.lb.cpx.client.app.cm.fx.details.CmDepartmentsDetailsScene;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public abstract class IcdHdbColumn extends DepartmentWardColumn<TCaseIcd, Boolean> {

    private static final Logger LOG = Logger.getLogger(IcdHdbColumn.class.getName());
    
    private ToggleGroup group = new ToggleGroup();

    public IcdHdbColumn() {
        super(Lang.getDepartmentMainDiagnosisObj().getAbbreviation());
        setMinWidth(45);
        setMaxWidth(45);
        setResizable(false);
        setSortable(false);
        setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseIcd, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TCaseIcd, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue().getIcdcIsHdbFl());
            }
        });
        setCellFactory(new Callback<TableColumn<TCaseIcd, Boolean>, TableCell<TCaseIcd, Boolean>>() {
            @Override
            public TableCell<TCaseIcd, Boolean> call(TableColumn<TCaseIcd, Boolean> param) {
                return new TableCell<TCaseIcd, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }
                        TCaseIcd currIcd = getTableRow().getItem();
                        RadioButton rbMd = new RadioButton();
                        rbMd.setDisable(!isEditableValue());
//                            rbMd.disableProperty().bind(editableProperty());
                        rbMd.setToggleGroup(group);
                        //padding, needed to avoid "freespace" between text and outline of the radiobutton
                        rbMd.setSelected(item);
                        //listener to react on click, reset hbx value in other icd objects 
                        //and sets ohne hbx value to true
                        //if icd is slected as md that is not used for grouping flag will be set 
                        rbMd.addEventFilter(MouseEvent.MOUSE_CLICKED, (event) -> {
//                                //ignore click if item has hbx flag 
                            LOG.info("process click");

//                            for (TCaseIcd icdItem : currIcd.getCaseDepartment().getCaseIcds()) {
//
//                                if (icdItem.getIcdcIsHdbFl()) {
//                                    icdItem.setIcdcIsHdbFl(false);
//                                    if (isArmed()) {
//                                        saveIcd(icdItem);
////                                        singleCaseBean.get().saveTCaseIcd(icdItem);
//                                        break;
//                                    }
//                                }
//                            }
                            currIcd.setIcdcIsHdbFl(true);
                            if (isArmed()) {
                                updateHdbFlag(currIcd);
//                                saveIcd(currIcd);
//                                singleCaseBean.get().saveTCaseIcd(currIcd);
                            }
                            updateLayout(currIcd);

                        });
                        setGraphic(rbMd);

                    }
                };
            }
        });
    }
    
    public abstract void saveIcd(TCaseIcd pIcd);
    
    public abstract void updateLayout(TCaseIcd pIcd);
    
    public abstract void updateHdbFlag(TCaseIcd pIcd);
}
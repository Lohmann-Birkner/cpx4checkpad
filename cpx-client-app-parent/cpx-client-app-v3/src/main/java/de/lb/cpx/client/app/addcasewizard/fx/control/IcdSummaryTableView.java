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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.addcasewizard.fx.control;

import de.lb.cpx.client.app.addcasewizard.model.table.IcdTableView;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartment;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.shared.lang.Lang;
import java.util.Collection;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author wilde
 */
public class IcdSummaryTableView extends IcdTableView {

    private ToggleGroup tGroup = new ToggleGroup();
    private final AddCaseResolveCaseFXMLController parentCtrl;

    IcdSummaryTableView(AddCaseResolveCaseFXMLController pParentCtrl, int pYearOfValidity) {
//            super();
//CPX-997 Katalog suche orientieren
        super(pParentCtrl.getCurrentCase().getCurrentExtern().getCsdAdmissionDate() != null ? Lang.toYear(pParentCtrl.getCurrentCase().getCurrentExtern().getCsdAdmissionDate()) : CpxClientConfig.instance().getSelectedGrouper().getModelYear());
        parentCtrl = pParentCtrl;
//CPX-997 sort icdCodeColumn
        icdCodeColumn.setCellFactory((TableColumn<TCaseIcd, String> param) -> {
            TableCell<TCaseIcd, String> cell = new TableCell<TCaseIcd, String>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        return;
                    }
                    setText(item);
                }
            };
            cell.itemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                TCaseIcd icd = cell.getTableRow().getItem();
                if (icd != null) {
                    if (!icd.getIcdcIsHdbFl()) {
                        cell.setText(icd.getIcdcCode());
                    } else {
                        CpxDepartment department = CpxDepartmentCatalog.instance().getByCode(icd.getCaseDepartment().getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                        //setValues(icd.getIcdcCode(),Lang.getDepartmentMainDiagnosisTooltip(department!=null?department.getDepDescriptKey():icd.getCaseDepartment()));
//                                     Label label = new Label(icd.getIcdcCode());
                        cell.setTooltip(new Tooltip(Lang.getDepartmentMainDiagnosisTooltip(department != null
                                ? department.getDepKey301() + ": " + department.getDepDescription301() : icd.getCaseDepartment().getDepKey301())
                        ));
//                                    
//                                 cell.setGraphic(label);
//                                    setGraphic().setStyle("-fx-text-fill: goldenrod");
                    }
                }
            });
            return cell;
        });
        principalDiagnosisColumn.setCellFactory((TableColumn<TCaseIcd, TCaseIcd> param) -> {
            TableCell<TCaseIcd, TCaseIcd> cell = new TableCell<>() {
                private final RadioButton rb = new RadioButton();

                @Override
                protected void updateItem(TCaseIcd item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        return;
                    }
                    TCaseIcd icd = item;
                    rb.setUserData(icd);
                    rb.setSelected(icd.getIcdcIsHdxFl());
                    rb.setOnMouseClicked((MouseEvent event) -> {
                        //                                    if (isArmed()) {
                        if (rb.isFocused()) {
                            if (!icd.getIcdcIsHdxFl()) {
                                resetMainDiagnosis();
                                icd.setIcdcIsHdxFl(true);
                                if (!icd.getIcdIsToGroupFl()) {
                                    icd.setIcdIsToGroupFl(true);
                                }
                                refresh();
                                parentCtrl.performGroup();
                            }
                        }
//                                    } else {
//                                        rb.setSelected(icd.getIcdcIsHdxFl());
//                                        refresh();
//                                    }
                    });
                    setGraphic(rb);
                    rb.setToggleGroup(tGroup);
                }
            };
            return cell;
        });
    }

    @Override
    public void refresh() {
        super.refresh();
        ObservableList<TCaseIcd> itms = getItems();
        setItems(null);
        setItems(itms);
    }

    public void addIcds(Collection<TCaseIcd> pIcds) {
        getItems().addAll(pIcds);
    }
}

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

import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author wilde
 * @param <T> underlaying object entity
 */
public class DepartmentWardDetailsSkin<T extends AbstractEntity> extends SkinBase<DepartmentWardDetails<T>> {

    private static final Logger LOG = Logger.getLogger(DepartmentWardDetailsSkin.class.getName());
    private final WeakPropertyAdapter propAdapter;

    public DepartmentWardDetailsSkin(DepartmentWardDetails<T> pDetails) {
        super(pDetails);
        propAdapter = new WeakPropertyAdapter();
        propAdapter.addChangeListener(getSkinnable().itemProperty(), new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> ov, T t, T t1) {
                updateRoot(t1);
            }
        });
        updateRoot(getSkinnable().getItem());
    }
    
    private void updateRoot(T pDepartment){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getChildren().clear();
                getChildren().add(createDetailForDepartment(pDepartment));
            }
        });
    }
    private Parent createDetailForDepartment(T item) {
        if (item == null) {
//            Label emptyContentLabel = new Label("Es ist keine Fachabteilung ausgewählt!");
            Label emptyContentLabel = new Label();
            emptyContentLabel.textProperty().bind(getSkinnable().noItemTextProperty());
            VBox emptyContent = new VBox(emptyContentLabel);
            emptyContent.setAlignment(Pos.CENTER);
            return emptyContent;
        }
        VBox details = new VBox();
        VBox.setVgrow(details, Priority.ALWAYS);
        details.setAlignment(Pos.CENTER);
        details.setSpacing(12);
        details.setPadding(new Insets(0, 0, 0, 10));

        HBox hbDeptMainDiag = new HBox();
        HBox.setHgrow(hbDeptMainDiag, Priority.ALWAYS);
//        Label label = new Label("Fachabteilung: ");
        Label label = new Label();
        label.textProperty().bind(getSkinnable().titleProperty());
        label.setStyle("-fx-text-fill: grey");
        Label labelvalue = new Label(getSkinnable().getItemConverter().toString(item));

        VBox vbMainDiag = new VBox();
        vbMainDiag.setSpacing(6);
        Label lbMainDiagnosis = new Label(Lang.getDepartmentMainDiagnosis());
        lbMainDiagnosis.setStyle("-fx-font-weight: normal");
        lbMainDiagnosis.getStyleClass().add("cpx-title-label");
        Separator sp = new Separator();
        HBox.setHgrow(sp, Priority.ALWAYS);
        sp.getStyleClass().add("cpx-title-separator");

        GridPane gpMainDiagnosis = new GridPane();
        gpMainDiagnosis.setHgap(50);
        gpMainDiagnosis.setVgap(10);
        gpMainDiagnosis.setStyle("-fx-font-size: 12px;");
        gpMainDiagnosis.setPadding(new Insets(0, 0, 0, 5));
        gpMainDiagnosis.add(keyLabel("ICD"), 0, 0);

        Label lbIcd = new Label();
        lbIcd.textProperty().bind(getSkinnable().hdbCodeProperty());
        lbIcd.setStyle("-fx-font-size: 15px;");
        OverrunHelper.addOverrunInfoButton(lbIcd);
        gpMainDiagnosis.add(lbIcd, 0, 1);
        gpMainDiagnosis.add(keyLabel("ICD-Text"), 1, 0);
        Label lbIcdText = new Label();
        lbIcdText.textProperty().bind(getSkinnable().hdbCodeDescriptionProperty());
        lbIcdText.setStyle("-fx-font-size: 15px;");

        OverrunHelper.addOverrunInfoButton(lbIcdText);
        gpMainDiagnosis.add(lbIcdText, 1, 1);

        SplitPane spIcdOps = new SplitPane();
        spIcdOps.setOrientation(Orientation.VERTICAL);
        spIcdOps.setDividerPositions(0.5);
        
        //        List<TCaseIcd> listIcds = null;
//        try {
        List<TCaseIcd> listIcds = /*item.getCaseIcds()*/getSkinnable().getIcdsFromItem(item).stream().collect(Collectors.toList());
//        } catch (NullPointerException ex) {
//            //check for nullPointer, will occure when list is not properly init?
//            LOG.log(Level.INFO, "list of icds of entity " + item.id + " can not be detected. List of Icds not initilized?", ex);
//        }
//        if (listIcds == null) {
//            return new HBox();
//        }
        //if empty-> db lookup to get values
        if (listIcds.isEmpty()) {
            listIcds.addAll(getSkinnable().getFindIcdsCallback().call(item));
        }
        
        TableView<TCaseIcd> icdTableView = new TableView<>();
        icdTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(icdTableView, Priority.ALWAYS);
        IcdHdbColumn hdbColumn = new IcdHdbColumn() {
            @Override
            public void saveIcd(TCaseIcd pIcd) {
                getSkinnable().getSaveIcdCallback().call(pIcd);
            }

            @Override
            public void updateLayout(TCaseIcd pIcd) {
                updateHdbValues(pIcd);
            }

            @Override
            public void updateHdbFlag(TCaseIcd pIcd) {
                for(TCaseIcd icd : listIcds){
                    if(icd.getIcdcIsHdbFl() && !icd.versionEquals(pIcd)){ //chekc here for possible errors in case merge???
                        icd.setIcdcIsHdbFl(false);
                        getSkinnable().getSaveIcdCallback().call(icd);
                        break;
                    }
                }
                getSkinnable().getSaveIcdCallback().call(pIcd);
            }

        };
        IcdCodeTextColumn codeTextColumn = new IcdCodeTextColumn();
        codeTextColumn.setIcdConverter(getSkinnable().getIcdConverter());
        icdTableView.getColumns().addAll(hdbColumn, new IcdCodeColumn(), codeTextColumn);
        int count = 0;
        for (TCaseIcd icd : listIcds) {
            if (icd.getIcdcIsHdbFl()) {
                if (count == 0) {
                    updateHdbValues(icd);
                } else {
                    icd.setIcdcIsHdbFl(false);
                    getSkinnable().getSaveIcdCallback().call(icd);
                }
                count++;
            }
        }

        icdTableView.getItems().addAll(listIcds);//listOfIcdDto);
        TableView<TCaseOps> opsTableView = new TableView<>();
        opsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(opsTableView, Priority.ALWAYS);

        OpsCodeColumn opsCodeColumn = new OpsCodeColumn();
        OpsCodeTextColumn opsTextColumn = new OpsCodeTextColumn();
        opsTextColumn.setOpsConverter(getSkinnable().getOpsConverter());
        opsTableView.getColumns().addAll(opsCodeColumn, opsTextColumn);
        List<TCaseOps> listOpses;
        if (item.id != 0) {
            listOpses = getSkinnable().getFindOpsesCallback().call(item);
        } else {
            listOpses = new ArrayList<>(getSkinnable().getOpsesFromItem(item));
        }
//            listOPss.forEach(new Consumer<TCaseOps>() {
//                @Override
//                public void accept(TCaseOps t) {
//                    OpsOverviewDTO e = new OpsOverviewDTO(t.getOpscCode(), "");
//                    listOfOpsDto.add(e);
//                }
//            });
        opsTableView.getItems().addAll(listOpses);//listOfOpsDto);

        //add list of versions in the ui
        hbDeptMainDiag.getChildren().addAll(label, labelvalue);

        spIcdOps.getItems().addAll(icdTableView, opsTableView);

        vbMainDiag.getChildren().addAll(lbMainDiagnosis, sp, gpMainDiagnosis);
        Separator sep = new Separator(Orientation.HORIZONTAL);
//        sep.setPadding(new Insets(0, 0, 0, 10));
        details.getChildren().addAll(hbDeptMainDiag, vbMainDiag, sep, spIcdOps);
        //}
        return details;
    }

    private Label keyLabel(String pTitle) {
        Label label = new Label(pTitle);
        label.getStyleClass().add("cpx-detail-label");
        return label;
    }

    private void updateHdbValues(TCaseIcd pIcd) {
        getSkinnable().setHdbCode(pIcd.getIcdcCode());
        getSkinnable().setHdbCodeDescription(getSkinnable().getIcdConverter().toString(pIcd));
    }
    
}

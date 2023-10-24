/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
/**
 * FXML Controller class creates and manage the Case Bills and bill positions in the case
 * management / case simulation scene
 *
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.simulation.tables.BillOpenItemsTableView;
import de.lb.cpx.client.app.cm.fx.simulation.tables.BillPositionsTableView;
import de.lb.cpx.client.app.cm.fx.simulation.tables.BillsTableView;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.exceptions.CpxSapException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TSapFiBill;
import de.lb.cpx.model.TSapFiBillposition;
import de.lb.cpx.model.TSapFiOpenItems;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author nandola
 */
public class CmCaseBillsFXMLController extends Controller<CmCaseBillsScene> {

//    @FXML
//    private HBox hBoxUpdateBills;
//    @FXML
//    private Button btnUpdateBills;
    @FXML
    private SplitPane spContent;
    @FXML
    private BillsTableView tvBills;
    @FXML
    private BillPositionsTableView tvBillPositions;
    @FXML
    private BillOpenItemsTableView tvBillOpenItems;

    private CaseServiceFacade caseService;
//    private final EjbProxy<CaseServiceBeanRemote> caseServiceBean;
    @FXML
    private Label labelBills;
    @FXML
    private Button btnUpdateBills;
    @FXML
    private Label labelBillPositions;
    @FXML
    private Label labelBillOpenItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

    }

    @Override
    public void reload() {
        super.reload();
//        getScene().reload();
//        getScene().getController().tvBillOpenItems.refresh();
        clearAllTableViews();
        setUpTableView(caseService.getCurrentCase().getId());

    }

    @Override
    public boolean close() {
        super.close();
//        hBoxUpdateBills.prefHeightProperty().unbind();
        return true;
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
    }

    /**
     * init screen with value
     *
     * @param cFacade caseService to access server functions
     */
    public void init(CaseServiceFacade cFacade) {
        //init caseService in the panes 
        caseService = cFacade;
        btnUpdateBills.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.REFRESH));
        btnUpdateBills.setTooltip(new Tooltip(Lang.getSapBillsUpdateTooltip()));
        labelBills.getStyleClass().add("cpx-main-label");
        labelBillOpenItems.getStyleClass().add("cpx-main-label");
        labelBillPositions.getStyleClass().add("cpx-main-label");
        tvBills.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvBills.getStyleClass().add("stay-selected-table-view");
//        HBox.setHgrow(tvBills, Priority.ALWAYS);
//        VBox.setVgrow(tvBills, Priority.ALWAYS);

        tvBillPositions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvBillOpenItems.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

//        caseServiceBean = Session.instance().getEjbConnector().connectCaseServiceBean();
        TCase tCase = cFacade.getCurrentCase();
        setUpTableView(tCase.getId());

        btnUpdateBills.setOnMouseClicked((MouseEvent event) -> {
            TaskService<Void> updateBillsService = new TaskService<Void>("Aktualisiere Rechnungen!") {
                @Override
                public Void call() {
                    updateBillsHandler();
                    return null;
                }

                @Override
                public void afterTask(Worker.State pState) {
                    if (Worker.State.FAILED.equals(pState)) {
                        Throwable ex = getException();
                        if (ex.getCause() instanceof CpxSapException) {
                            MainApp.showErrorMessageDialog("Anscheinend steht das SAP-System zur Zeit nicht zur Verfügung!\n\n" + ex.getMessage());
                            return;
                        }
                        MainApp.showErrorMessageDialog("Fehler beim der Rechnungen!\n\n" + ex.getMessage());
                    }
                    if (Worker.State.SUCCEEDED.equals(pState)) {
                        reload();
                        MainApp.showInfoMessageDialog("Die Rechnungen wurden erfolgreich aktualisiert!", getScene().getWindow());
                    }
                }

            };
            updateBillsService.start();
        });
    }

    private void updateBillsHandler() {
        caseService.updateBills(caseService.getCurrentCase());
    }

    private void setUpTableView(long caseId) {
        List<TSapFiBill> caseBills = caseService.getAllCaseBills(caseId);
        List<TSapFiOpenItems> caseOpenItems = caseService.getAllCaseOpenItems(caseId);

        if (caseBills != null && !caseBills.isEmpty()) {
            tvBills.setItems(FXCollections.observableArrayList(caseBills));
        }

        // with each bill change, update corresponding bill positions
        if (tvBills != null && tvBills.getItems() != null) {
            tvBills.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TSapFiBill>() {
                @Override
                public void changed(ObservableValue<? extends TSapFiBill> ov, TSapFiBill oldvalue, TSapFiBill newValue) {
                    if (newValue != null) {
                        List<TSapFiBillposition> billPositions = caseService.getAllBillPositionsForBill(newValue.getId());
                        if (billPositions != null) {
//                            tvBillPositions.getItems().clear();
                            tvBillPositions.getItems().setAll(billPositions);
//                            tvBillPositions.setItems(FXCollections.observableArrayList(billPositions));
//                            tvBillPositions.refresh();
                        }
                    }
                }
            });

            //select first table row from Bills tableview
            tvBills.getSelectionModel().select(0);
        }

        if (caseOpenItems != null && !caseOpenItems.isEmpty()) {
            tvBillOpenItems.setItems(FXCollections.observableArrayList(caseOpenItems));
        }
    }

    private void clearAllTableViews() {
        tvBills.getItems().clear();
        tvBillPositions.getItems().clear();
        tvBillOpenItems.getItems().clear();
    }

}

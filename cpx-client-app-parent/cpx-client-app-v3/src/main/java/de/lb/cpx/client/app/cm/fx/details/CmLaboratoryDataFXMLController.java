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
 * FXML Controller class creates and manage the Laboratory data in the case
 * management / case simulation scene
 *
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.cm.fx.simulation.filtermanager.LaboratoryDataListFilterManager;
import de.lb.cpx.client.app.cm.fx.simulation.tables.LaboratoryDataTableView;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TLab;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

/**
 * Controller class to handle Laboratory data.
 *
 * @author nandola
 */
public class CmLaboratoryDataFXMLController extends Controller<CmLaboratoryDataScene> {

    @FXML
    private LaboratoryDataTableView laboratoryDataTableView;
    private List<TLab> allLaboratoryData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    @Override
    public boolean close() {
        super.close();
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

//        laboratoryDataTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        laboratoryDataTableView.getStyleClass().add("stay-selected-table-view");
        TCase tCase = cFacade.getCurrentCase();
        allLaboratoryData = cFacade.getAllLaboratoryData(tCase.getId());
//        laboratoryDataTableView = new LaboratoryDataTableView(LaboratoryDataListFilterManager.getInstance(), allLaboratoryData);
//         laboratoryDataAnchorPane.getChildren().add(laboratoryDataTableView);

        laboratoryDataTableView.setFilterManager(LaboratoryDataListFilterManager.getInstance());
        laboratoryDataTableView.setLaboratoryDataList(allLaboratoryData);
        laboratoryDataTableView.reload();
    }

    /*
    public void init(CaseServiceFacade cFacade) {
        caseServiceFacade = cFacade;

//        laboratoryDataTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        laboratoryDataTableView.getStyleClass().add("stay-selected-table-view");

        TCase tCase = cFacade.getCurrentCase();
        List<TLab> allLaboratoryData = caseServiceFacade.getAllLaboratoryData(tCase.getId());

        if (allLaboratoryData != null && !allLaboratoryData.isEmpty()) {
            laboratoryDataTableView.setItems(FXCollections.observableArrayList(allLaboratoryData));
        }
    }
     */
    public List<TLab> getLaboratoryDataList() {
        return allLaboratoryData == null ? null : new ArrayList<>(allLaboratoryData);
    }
}

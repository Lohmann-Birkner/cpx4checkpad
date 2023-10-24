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
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.app.cm.fx.simulation.tables.BillFeeView;
import de.lb.cpx.client.app.cm.fx.simulation.tables.CaseFeeTableView;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

/**
 * FXML Controller class
 *
 * @author wilde
 */
public class CmCaseFeeFXMLController extends Controller<Scene> {

    @FXML
    private Label labelCaseFee;
    @FXML
    private ScrollPane scFeeData;
    
    private CaseFeeTableView tvCaseFee;
    private BillFeeView billView;
    
    private final VersionStringConverter nameConverter = new VersionStringConverter(VersionStringConverter.DisplayMode.ACTUAL);

    public void init(VersionManager pVersionManager) {
        
        VersionManager versionManager = pVersionManager;
        TCaseDetails extern = versionManager.getCurrentExtern();
        List<TCaseBill>caseBills = pVersionManager.getCaseBills(extern);
        if(caseBills != null && !caseBills.isEmpty()){
            createBillView(caseBills);
        }else{
            List<TCaseFee> caseFees = pVersionManager.getCaseFees(extern);
            if (caseFees != null) {
                tvCaseFee = new CaseFeeTableView();
                scFeeData.setContent(tvCaseFee);
                tvCaseFee.setItems(FXCollections.observableArrayList(caseFees));
            }
        }
        setFeeInfo(extern);
    }

    private void setFeeInfo(TCaseDetails pDetails) {
//        for(VersionContent version : versionManager.getManagedVersions()){
//            if(version.getContent().getCsdIsActualFl() && !version.getContent().getCsdIsLocalFl()){
//                labelCaseFee.setText(Lang.getCasefeeInfo(version.getVersionName()));
//                break;
//            }
//        }
        labelCaseFee.setText(Lang.getCasefeeInfo(nameConverter.toString(pDetails)));
    }

    private void createBillView(List<TCaseBill> caseBills) {
       billView = new BillFeeView(caseBills);
       scFeeData.setContent(billView);

    }

}

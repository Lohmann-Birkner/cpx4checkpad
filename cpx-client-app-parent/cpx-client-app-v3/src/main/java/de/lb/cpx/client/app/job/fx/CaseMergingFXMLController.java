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
package de.lb.cpx.client.app.job.fx;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.job.fx.casemerging.CaseMergingDetailsScene;
import de.lb.cpx.client.app.job.fx.casemerging.tab.CaseMergingOverviewTab;
import de.lb.cpx.client.app.tabController.MergeTabController;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.combobox.GrouperModelsCombobox;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.enums.CaseTypeEn;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * FXML Controller class Controls the CaseMergingScene TODO: For Controller that
 * uses Task to function: create new controller type?
 *
 * @author wilde
 */
public class CaseMergingFXMLController extends MergeTabController<CaseMergingScene> {

    private static final Logger LOG = Logger.getLogger(CaseMergingFXMLController.class.getName());

    @FXML
    private TabPane tpCaseMergeing;
    @FXML
    private HBox hbHeader;
    
//    private GrouperModelsCombobox cbGrouperModel;
    
    protected final ReadOnlyBooleanWrapper taskRunning = new ReadOnlyBooleanWrapper(false);
    private CaseMergingOverviewTab mergeOverviewDrg;
    private CaseMergingOverviewTab mergeOverviewPepp;

    /**
     * Initializes the controller class.
     *
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        cbGrouperModel = new GrouperModelsCombobox();
//       Callback <GDRGModel, Boolean> onUpdateParent = new Callback<GDRGModel, Boolean> () {
//            @Override
//            public Boolean call(GDRGModel p) {
//                reload4GrouperModel();
//                return true;
//            }
//        };
//        cbGrouperModel.setOnUpdateParent(onUpdateParent);
        initGrouperBox();
        hbHeader.getChildren().add(cbGrouperModel);
        try {
            mergeOverviewDrg = new CaseMergingOverviewTab(CaseTypeEn.DRG);
        } catch (CpxIllegalArgumentException ex) {
            Logger.getLogger(CaseMergingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        mergeOverviewDrg.setOnOpenMergeDetails(new Callback<CaseMergingDetailsScene, Void>() {
            @Override
            public Void call(CaseMergingDetailsScene param) {
                openMergedDetails(param);
                return null;
            }
        });
        try {
            mergeOverviewPepp = new CaseMergingOverviewTab(CaseTypeEn.PEPP);
        } catch (CpxIllegalArgumentException ex) {
            Logger.getLogger(CaseMergingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        mergeOverviewPepp.setOnOpenMergeDetails(new Callback<CaseMergingDetailsScene, Void>() {
            @Override
            public Void call(CaseMergingDetailsScene param) {
                openMergedDetails(param);
                return null;
            }
        });
        taskRunning.bind(Bindings.or(mergeOverviewDrg.taskRunningProperty(), mergeOverviewPepp.taskRunningProperty()));
        mergeOverviewDrg.disableProperty().bind(mergeOverviewPepp.taskRunningProperty());
        mergeOverviewPepp.disableProperty().bind(mergeOverviewDrg.taskRunningProperty());
        tpCaseMergeing.getTabs().add(mergeOverviewDrg);
        tpCaseMergeing.getTabs().add(mergeOverviewPepp);

    }

    /**
     * @return readonly property to indicate if task is running
     */
    public ReadOnlyBooleanProperty taskRunningProperty() {
        return taskRunning;
    }

    @Override
    public boolean close() {
        if (super.close()) {
            Iterator<Tab> it = tpCaseMergeing.getTabs().iterator();
            while (it.hasNext()) {
                Tab next = it.next();
                if (next instanceof TwoLineTab) {
                    if (!((TwoLineTab) next).getTabType().equals(TwoLineTab.TabType.CASEMERGING)) {
                        ((TwoLineTab) next).close();
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void reload4GrouperModel(){
        closeMergeTabs(tpCaseMergeing);
         if (mergeOverviewDrg != null) {
            mergeOverviewDrg.reload4GrouperModel();
        }
        if (mergeOverviewPepp != null) {
            mergeOverviewPepp.reload4GrouperModel();
        }
       
    }
    
    @Override
    public void reload() {
        super.reload();
        if (mergeOverviewDrg != null) {
            mergeOverviewDrg.reload();
        }
        if (mergeOverviewPepp != null) {
            mergeOverviewPepp.reload();
        }
    }

        private void openMergedDetails(CaseMergingDetailsScene mrgMergeIdent) {
            getScene().setParentTabPane(tpCaseMergeing);
            super.openMergedDetails(mrgMergeIdent, mergeOverviewDrg, mergeOverviewPepp, tpCaseMergeing);
        }


    public void checkGrouperModelAndReload() {
       if(cbGrouperModel != null){
            cbGrouperModel.getSelectionModel().select(CpxClientConfig.instance().getSelectedGrouper());
       } 
       reload();
    }

}

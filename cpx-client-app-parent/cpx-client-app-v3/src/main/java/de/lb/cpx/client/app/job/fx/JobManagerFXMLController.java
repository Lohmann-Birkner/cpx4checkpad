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
import de.lb.cpx.client.app.job.fx.batchgroup.JobBatchGroupingFXMLController;
import de.lb.cpx.client.app.job.fx.batchgroup.JobBatchGroupingScreen;
import de.lb.cpx.client.app.job.fx.tab.BatchGroupJobTab;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.combobox.GrouperModelsCombobox;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import java.io.IOException;
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
 * FXML Controller class Implements client side job manager view should be
 * considered in the future if there is more logic to add to disable client or
 * some sort if read only mode is implemented
 *
 * @author wilde
 */
public class JobManagerFXMLController extends Controller<CpxScene> {

    @FXML
    private TabPane tpJobs;
    @FXML
    private HBox hbHeader;
    
    GrouperModelsCombobox cbGrouperModel;
    
    private final ReadOnlyBooleanWrapper taskRunning = new ReadOnlyBooleanWrapper(false);

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbGrouperModel = new GrouperModelsCombobox();
        Callback <GDRGModel, Boolean> onUpdateParent = new Callback<GDRGModel, Boolean> () {
            @Override
            public Boolean call(GDRGModel p) {
                refresh();
                return true;
            }
        };
        cbGrouperModel.setOnUpdateParent(onUpdateParent);
        hbHeader.getChildren().add(cbGrouperModel);
        initTabs();
    }

    private void initTabs() {
        try {
            BatchGroupJobTab tab1 = new BatchGroupJobTab();
            tpJobs.getTabs().add(tab1);
            //bind stuff, here should other jobs added to overwatch if one task is running and other stuff must be disabeled
            //like tabs or other stuff
            taskRunning.bind(Bindings.when(tab1.isTaskRunningProperty()).then(true).otherwise(false));
        } catch (IOException ex) {
            Logger.getLogger(JobManagerFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BatchGroupJobTab getBatchGroupJobTab() {
        Iterator<Tab> it = tpJobs.getTabs().iterator();
        while (it.hasNext()) {
            Tab next = it.next();
            if (next instanceof BatchGroupJobTab) {
                return (BatchGroupJobTab) next;
            }
        }
        return null;
    }

    public JobBatchGroupingScreen getJobBatchGroupingScreen() {
        BatchGroupJobTab tab = getBatchGroupJobTab();
        if (tab == null) {
            return null;
        }
        return tab.getScreen();
    }

    public JobBatchGroupingFXMLController getJobBatchGroupingFXMLController() {
        JobBatchGroupingScreen screen = getJobBatchGroupingScreen();
        if (screen == null) {
            return null;
        }
        return screen.getController();
    }

    @Override
    public boolean close() {
        if (super.close()) {
            Iterator<Tab> it = tpJobs.getTabs().iterator();
            while (it.hasNext()) {
                Tab next = it.next();
                if (next instanceof TwoLineTab) {
                    ((TwoLineTab) next).close();
                }
//                if (next instanceof TwoLineTab) {
//                    if (!((TwoLineTab) next).getTabType().equals(TwoLineTab.TabType.JOBBATCHGROUPING)) {
//                        ((TwoLineTab) next).close();
//                    }
//                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void reload() {
        super.reload();
        tpJobs.getTabs().stream().filter((tab) -> (tab instanceof TwoLineTab)).forEachOrdered((tab) -> {
            ((TwoLineTab) tab).reload();
        });
    }

    @Override
    public void refresh() {
        ((TwoLineTab)tpJobs.getSelectionModel().getSelectedItem()).reload();
        super.refresh(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReadOnlyBooleanProperty taskRunningProperty() {
        return taskRunning;
    }

    void checkGrouperModelAndRefresh() {
        if(cbGrouperModel != null){
             cbGrouperModel.getSelectionModel().select(CpxClientConfig.instance().getSelectedGrouper());
        }
        refresh();
    }

}

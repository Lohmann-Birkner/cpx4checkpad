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
package de.lb.cpx.client.app.job.fx.casemerging;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.details.CmDepartmentsDetailsScene;
import de.lb.cpx.client.app.cm.fx.simulation.tables.DrgTableView;
import de.lb.cpx.client.app.cm.fx.simulation.tables.RulesTableView;
import de.lb.cpx.client.app.job.fx.casemerging.details.CaseDetailMergeDetailsScreen;
import de.lb.cpx.client.app.job.fx.casemerging.details.IcdMergeDetailsScreen;
import de.lb.cpx.client.app.job.fx.casemerging.model.CaseMergeContent;
import de.lb.cpx.client.app.menu.fx.event.DataActionEvent;
import de.lb.cpx.client.app.menu.fx.event.Events;
import de.lb.cpx.client.app.menu.model.ListType;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.ObjectConverter;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class Controls CaseMergingDetailsScene similar to simulation
 * controller TODO:Refactor merge with caseManagement controller
 *
 * @author wilde
 */
public class CaseMergingDetailsFXMLController extends Controller<CaseMergingDetailsScene> {

    private static final Logger LOG = Logger.getLogger(CaseMergingDetailsFXMLController.class.getName());

    @FXML
    private SectionHeader shCaseMergeMenu;
    @FXML
    private SplitPane spContent;
    @FXML
    private DrgTableView<CaseMergeContent> tvDrgResults;
    @FXML
    private RulesTableView tvRules;
    @FXML
    private TabPane tabPaneCaseContent;
    @FXML
    private Tab tabIcd;
    @FXML
    private Tab tabCaseData;
    @FXML
    private Tab tabDepartmentData;

    /**
     * Initializes the controller class. setUp listeners, set Lang, init default
     * buttons
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tvDrgResults.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CaseMergeContent>() {
            private ChangeListener<TGroupingResults> listener;

            @Override
            public void changed(ObservableValue<? extends CaseMergeContent> observable, CaseMergeContent oldValue, CaseMergeContent newContent) {
                if (oldValue != null) {
                    oldValue.groupingResultProperty().removeListener(listener);
                }
                if (newContent == null) {
                    return;
                }
                listener = new ChangeListener<>() {
                    @Override
                    public void changed(ObservableValue<? extends TGroupingResults> observable, TGroupingResults oldValue, TGroupingResults newValue) {
                        reloadRules(newContent);
                    }
                };
                newContent.groupingResultProperty().addListener(listener);
                reloadRules(newContent);
            }
        });
        tabIcd.setText(Lang.getCaseResolveICD() + "/" + Lang.getCaseResolveOPS());
        tabCaseData.setText(Lang.getCaseData());
        tabDepartmentData.setText(Lang.getDepartments());

        tabPaneCaseContent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                //check if content is created, if not create new one else refresh(update) existing content
                if (newValue.getContent() == null) {
                    createTabContent(newValue);
                } else {
                    refreshTabContent(newValue);
                }
            }
        });
        //add save button to menu
        Button btnSave = new Button(Lang.getCaseMergingDoSave());
        btnSave.setDisable(true);
        btnSave.setOnAction((event) -> {
            //cleanup when button is pressed
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    getScene().close();
                    getScene().getFacade().removeFromMergeCases(getScene().getMergeIdent());
                }
            });
//            getScene().getFacade().
        });
        Button btnSaveAndOpen = new Button(Lang.getCaseMergingDoSaveAndOpen());
        btnSaveAndOpen.setDisable(true);
        btnSaveAndOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (getScene().close()) {
                            getScene().getFacade().removeFromMergeCases(getScene().getMergeIdent());
                            Events.instance().setNewEvent(new DataActionEvent<>(getScene().getMergedCase().getId(), ListType.WORKING_LIST));
                        }
                    }
                });
            }
        });
//        shCaseMergeMenu.addMenuItems(btnSave, btnSaveAndOpen);
    }

    private void reloadRules(CaseMergeContent pContent) {
        tvRules.getItems().clear();
        if (pContent != null) {
            tvRules.getItems().addAll(pContent.getDetectedRules());
            LOG.finer("updated rules for " + pContent.getVersionName());
        } else {
            LOG.finer("content is null!");
        }
    }

    @Override
    public void afterInitialisingScene() {
        tvDrgResults.setCaseType(getScene().getMergedCase().getCsCaseTypeEn());
        tvRules.setCaseType(getScene().getMergedCase().getCsCaseTypeEn());
        Bindings.bindContent(tvDrgResults.getItems(), getScene().getCaseMergeList());
        createTabContent(tabIcd);
    }

    @Override
    public boolean close() {
        super.close();
        //if close was called try to save case
        //TODO(?):maybe refactor? can result in unexpected behavior if close are called some where outside
        boolean mergeSuccess = getScene().getFacade().saveMergedCase(getScene().getMergedCase(), getScene().getMergeIdent());
        if (!mergeSuccess) {
            MainApp.showErrorMessageDialog("Can not save merged Case! \n :'(");
        }
        return mergeSuccess;
    }

    private void createTabContent(Tab newValue) {
        //icd tab
        if (newValue.equals(tabIcd)) {
            try {
                IcdMergeDetailsScreen screen = new IcdMergeDetailsScreen(getScene().getCaseMergeList(), getScene().getFacade());
                newValue.setContent(screen.getRoot());
                newValue.setUserData(screen);
            } catch (IOException ex) {
                Logger.getLogger(CaseMergingDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //case data (Falldaten)
        if (newValue.equals(tabCaseData)) {
            try {
                CaseDetailMergeDetailsScreen screen = new CaseDetailMergeDetailsScreen(getScene().getCaseMergeList(), getScene().getFacade());
                newValue.setContent(screen.getRoot());
                newValue.setUserData(screen);
            } catch (IOException ex) {
                Logger.getLogger(CaseMergingDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //department (fachabteilungen)
        if (newValue.equals(tabDepartmentData)) {
            try {
                //create scene with object converter to detected case versions to show in Department Overview
                CmDepartmentsDetailsScene<TCase> screen = new CmDepartmentsDetailsScene<>(getScene().getMergedCase(),
                        new ArrayList<TCase>(getScene().getCases()),
                        new ObjectConverter<TCase, TCaseDetails>() {
                    @Override
                    public TCaseDetails to(TCase pObject) {
                        return pObject.getCurrentLocal();
                    }

                    @Override
                    public TCase from(TCaseDetails pObject) {
                        return pObject.getHospitalCase();
                    }
                });
                //set name converter for selection combobox
                screen.setNameConverter(new StringConverter<TCase>() {
                    @Override
                    public String toString(TCase object) {
                        if (object == null) {
                            return "";
                        }
                        return object.getId() == 0 ? Lang.getCaseMergingCaseObj().getAbbreviation() : object.getCsCaseNumber();
                    }

                    @Override
                    public TCase fromString(String string) {
                        return null;
                    }
                });
                screen.setArmed(false);
//                screen.setEditable(false);
                screen.getComboBox().getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        screen.setEditable(newValue.intValue() == 0 ? Boolean.TRUE : Boolean.FALSE);
//                        if(newValue.intValue() == 0){
//                            screen.setEditable(Boolean.TRUE);
//                        }else{
//                            screen.setEditable(Boolean.FALSE);
//                        }
                    }
                });
                newValue.setContent(screen.getRoot());
                newValue.setUserData(screen);
            } catch (IOException ex) {
                Logger.getLogger(CaseMergingDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void refreshTabContent(Tab pTab) {
        if (pTab.getUserData() instanceof CpxScreen) {
            CpxScreen currentScreen = (CpxScreen) pTab.getUserData();
            currentScreen.refresh();
        }
        // reload to reflect added/deleted Icds and/or Opses in departmentDetails tab.
        if (pTab.getUserData() instanceof CpxScene) {
            CpxScene departmentsDetailsScene = (CpxScene) pTab.getUserData();
            departmentsDetailsScene.reload();
        }
    }

}

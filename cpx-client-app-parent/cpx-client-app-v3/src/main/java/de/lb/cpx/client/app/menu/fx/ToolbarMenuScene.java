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
package de.lb.cpx.client.app.menu.fx;

import de.lb.cpx.client.app.cm.fx.CaseManagementFXMLController;
import de.lb.cpx.client.app.cm.fx.CaseManagementScene;
import de.lb.cpx.client.app.job.fx.CaseMergingScene;
import de.lb.cpx.client.app.job.fx.DocumentImportScene;
import de.lb.cpx.client.app.job.fx.JobManagerScene;
import de.lb.cpx.client.app.menu.fx.event.DataActionEvent;
import de.lb.cpx.client.app.menu.fx.event.Events;
import de.lb.cpx.client.app.menu.fx.filterlists.cases.CaseList;
import de.lb.cpx.client.app.menu.fx.filterlists.cases.QuotaList;
import de.lb.cpx.client.app.menu.fx.filterlists.cases.RuleList;
import de.lb.cpx.client.app.menu.fx.filterlists.cases.WorkingListScene;
import de.lb.cpx.client.app.menu.fx.filterlists.processes.WorkflowListScene;
import de.lb.cpx.client.app.menu.model.ListType;
import de.lb.cpx.client.app.wm.fx.WmMainFrameFXMLController;
import de.lb.cpx.client.app.wm.fx.WmMainFrameScene;
import de.lb.cpx.client.app.wm.fx.process.tab.CaseTab;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.fx.BasicToolbarMenuScene;
import de.lb.cpx.client.core.menu.fx.openedEntry.OpenEntryScene;
import de.lb.cpx.client.core.menu.model.ToolbarListMenuItem;
import de.lb.cpx.client.core.menu.model.ToolbarMultiMenuItem;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.util.shortcut.ShortcutHandler;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * menu scene implementation for drg / wm implementation
 *
 * @author wilde
 */
public final class ToolbarMenuScene extends BasicToolbarMenuScene {

    private static final Logger LOG = Logger.getLogger(ToolbarMenuScene.class.getName());
    //toolbar items
    private final ToolbarListMenuItem<OpenEntryScene<CpxScene, TCase>> caseMenuItem;
    private final ToolbarListMenuItem<OpenEntryScene<WmMainFrameScene, TWmProcess>> processMenuItem;
    private final JobMenuItem jobMenuItem;
    private final License license;
//    private CpxLicenseUsage cpxLicenseUsage;

    private final ListChangeListener<OpenEntryScene<? extends CpxScene, ? extends AbstractEntity>> maxListSizeListener = new ListChangeListener<>() {
        private final Integer listMaxSize = 5;

        @Override
        public void onChanged(ListChangeListener.Change<? extends OpenEntryScene<? extends CpxScene, ? extends AbstractEntity>> c) {
            while (c.next()) {
                if (c.wasAdded()) {
                    if (c.getList().size() > listMaxSize) {
                        int addedSize = c.getAddedSubList().size();
                        for (int i = 0; i < addedSize; i++) {
                            OpenEntryScene<? extends CpxScene, ? extends AbstractEntity> entryScene = c.getList().get(i);
                            if (entryScene != null) {
                                entryScene.close();
                            }
                        }
                    }
                }
            }
        }
    };

    private WorkingListScene newWorkingList;
    private WorkflowListScene newWorkflowList;

    private CaseMergingScene caseMerging;
    private JobManagerScene jobManager;
    private DocumentImportScene documentImport;
    private final ObjectProperty<Pane> documentImportPane = new SimpleObjectProperty<>();

    /**
     * no arg constructor creates new instance
     *
     * @throws IOException thrown when user image could not be set
     * @throws CpxIllegalArgumentException happens if fxml is not found
     */
    public ToolbarMenuScene() throws IOException, CpxIllegalArgumentException {
        super();
        license = Session.instance().getLicense();
//        cpxLicenseUsage = new CpxLicenseUsage();
        long start = System.currentTimeMillis();
        Events.instance().actionEventProperty().addListener(new ChangeListener<DataActionEvent<Long>>() {
            @Override
            public void changed(ObservableValue<? extends DataActionEvent<Long>> observable, DataActionEvent<Long> oldValue, DataActionEvent<Long> newValue) {
                handleDataActionEvent(newValue);
            }
        });

//create new toolbar entries
        caseMenuItem = new ToolbarListMenuItem<>(getController().getToolbar());
        caseMenuItem.setCellFactory(new CasesFactory());
        caseMenuItem.disableProperty().bind(getController().disableMenuProperty());
        caseMenuItem.getItems().addListener(maxListSizeListener);
        caseMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                if (cpxLicenseUsage.containsDRGModule() || cpxLicenseUsage.containsPEPPModule()) {
                if (license.isDrgModule() || license.isPeppModule()) {
                    showWorkingList();
                }
            }
        });
        caseMenuItem.setGlyph(FontAwesome.Glyph.FILE);
        caseMenuItem.setExtendedTitle(Lang.getWorkingList());
        caseMenuItem.setTooltip(new BasicTooltip(Lang.getWorkingList(), 0, 1500, 200, true));
        caseMenuItem.setOnRemoveCallback(new Callback<OpenEntryScene<CpxScene, TCase>, Boolean>() {
            @Override
            public Boolean call(OpenEntryScene<CpxScene, TCase> param) {
                return param.close();
            }
        });

        processMenuItem = new ToolbarListMenuItem<>(getController().getToolbar());
        processMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                if (cpxLicenseUsage.containsFMModule()) {
                if (license.isFmModule()) {
                    showWorkflowList();
                }
            }
        });
        processMenuItem.setCellFactory(new ProcessFactory());
        processMenuItem.disableProperty().bind(getController().disableMenuProperty());
        processMenuItem.getItems().addListener(maxListSizeListener);
        processMenuItem.setGlyph(FontAwesome.Glyph.FOLDER);
        processMenuItem.setExtendedTitle(Lang.getWorkflowList());
        processMenuItem.setTooltip(new BasicTooltip(Lang.getWorkflowList(), 0, 1500, 200, true));
        processMenuItem.setOnRemoveCallback(new Callback<OpenEntryScene<WmMainFrameScene, TWmProcess>, Boolean>() {
            @Override
            public Boolean call(OpenEntryScene<WmMainFrameScene, TWmProcess> param) {
                return param.close();
            }
        });

        jobMenuItem = new JobMenuItem();
        jobMenuItem.setGlyph(FontAwesome.Glyph.TASKS);
        jobMenuItem.setExtendedTitle(Lang.getJobs());
        jobMenuItem.setTooltip(new BasicTooltip(Lang.getJobs(), 0, 1500, 200, true));

//add to toolbar
        getController().getToolbar().add(0, jobMenuItem);
        if (license.isFmModule()) {
            getController().getToolbar().add(0, processMenuItem);
        }
        if (license.isDrgModule() || license.isPeppModule()) {
            getController().getToolbar().add(0, caseMenuItem);
        }

//show workinglist as default
//TODO: maybe store, what view was last shown and load it than?
//showWorkingList();
        restoreRecentClientScene();

        LOG.finer("create toolbar menu scene in " + (System.currentTimeMillis() - start));
//caseMenuItem.focusTitle();
    }

    public CaseMergingScene getCaseMerging() {
        return caseMerging;
    }

    public JobManagerScene getJobManager() {
        return jobManager;
    }

    public DocumentImportScene getDocumentImport() {
        return documentImport;
    }

    //Awi-20170612-CPX-542
    private void handleDataActionEvent(DataActionEvent<Long> pEvent) {
        switch (pEvent.getListType()) {
            case WORKING_LIST:
                openCase(pEvent.getData());
                break;
            case WORKFLOW_LIST:
                openProcess(pEvent.getData());
                break;
            case CASE_DETAILS_VIEW:
                openProcess(pEvent.getData());
                break;
            default:
        }
    }

    public boolean openBatchgrouping() {
        if (!isSceneAlreadyShown(JobManagerScene.class)) {
            try {
//                            JobManagerScene jobManager = null;
                if (jobManager == null) {
                    jobManager = new JobManagerScene();
                    jobManager.getController().taskRunningProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            LOG.finer("disable menu: " + newValue);
                            getController().setDisableMenu(newValue);
//                                        setDisable(newValue);
                        }
                    });
                }else{
                    
                    jobManager.checkGrouperModelAndRefresh();
//                    jobManager.refresh();
                }
                displayScene(jobManager);
                processMenuItem.clearSelection();//getSelectionModel().clearSelection();
                caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
                return true;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public boolean openCaseMerging() {
        if (!isSceneAlreadyShown(CaseMergingScene.class)) {
            try {
//                            CaseMergingScene caseMerging = null;
                if (caseMerging == null) {
                    caseMerging = new CaseMergingScene();
                    caseMerging.getController().taskRunningProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            LOG.info("disable menu: " + newValue);
                            getController().setDisableMenu(newValue);
//                                        setDisable(newValue);
                        }
                    });
                    
                } else {

//                    caseMerging.reload();
                     caseMerging.checkGrouperModelAndReload();   
                }
                displayScene(caseMerging);
                processMenuItem.clearSelection();//getSelectionModel().clearSelection();
                caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
                return true;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public Pane getDocumentImportPane() {
        return documentImportPane.get();
    }

    public boolean openDocumentImport() {
        if (!isSceneAlreadyShown(DocumentImportScene.class)) {
            try {
                if (documentImport == null) {
//                    Stage stage = new Stage();
                    documentImport = new DocumentImportScene();
//                    stage.setScene(documentImport);
                }
                //displayScene(documentImport);
                if (documentImportPane.get() == null) {
                    final Pane pane = ((Pane) documentImport.getRoot());
                    documentImportPane.set(pane);
                }
                displayScene(documentImport);
                //displayedSceneProperty.setValue(documentImportPane.get());
                getController().setContent(documentImportPane.get());
                caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
                processMenuItem.clearSelection();//getSelectionModel().clearSelection();
                Platform.runLater(() -> {
                    //documentImportPane.get().requestFocus();
                });
                return true;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public boolean isBatchgroupingShown() {
        return isSceneAlreadyShown(JobManagerScene.class);
    }

    public boolean isCaseMergingShown() {
        return isSceneAlreadyShown(CaseMergingScene.class);
    }

    public boolean isDocumentImportShown() {
        return isSceneAlreadyShown(DocumentImportScene.class);
    }

    public boolean isWorkingListShown() {
        return isSceneAlreadyShown(WorkingListScene.class);
    }

    public boolean isWorkflowListShown() {
        return isSceneAlreadyShown(WorkflowListScene.class);
    }

    public void showWorkingList() {
        if (getController().isDisableMenu()) {
            return;
        }
//        if (NEW_FILTER_LIST_SWITCH) {
        if (!isSceneAlreadyShown(WorkingListScene.class)) {
            long start = System.currentTimeMillis();
            caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
            processMenuItem.clearSelection();//getSelectionModel().clearSelection();
            jobMenuItem.clearFocus();
            LOG.finer("clear focus in " + (System.currentTimeMillis() - start));
            try {
                initWorkingList();
            } catch (IOException | CpxIllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "Cannot show working list!", ex);
            }
            LOG.finer("init workinglist in " + (System.currentTimeMillis() - start));
            displayScene(newWorkingList);
            newWorkingList.getController().getTableView().requestFocus();
            LOG.finer("display scene in " + (System.currentTimeMillis() - start));
        }
        caseMenuItem.focusTitle();
//        } else {
//            if (!isSceneAlreadyShown(CaseTableMasterDetailScene.class)) {
//                lvOpenProcesses.getSelectionModel().clearSelection();
//                lvOpenCases.getSelectionModel().clearSelection();
//                try {
//                    initWorkingList();
//                } catch (IOException | CpxIllegalArgumentException ex) {
//                    LOG.log(Level.SEVERE, "Cannot show working list!", ex);
//                }
//                displayScene(old_workingList);
//            }
//        }
    }

    public void initWorkingList() throws IOException, CpxIllegalArgumentException {
//        if (NEW_FILTER_LIST_SWITCH) {
        if (newWorkingList == null) {
            //CPX-925
            try {
                newWorkingList = new WorkingListScene();
            } catch (NamingException ex) {
                LOG.log(Level.SEVERE, "Cannot initialize working list!", ex);
            }
        }else{
            
            newWorkingList.checkGrouperModel();
        }
        //SonarCube, avoid null pointer if fxml could not be loaded
        //TODO: FIXME, make better implementation after this new filter switch is gone
        if (newWorkingList == null) {
            return;
        }
        //TODO:ENABLE CLICK listeners, CPX-925
        newWorkingList.setRowClickHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2 && newWorkingList.getSelectedItem() != null) {
                    long start = System.currentTimeMillis();
                    WorkingListItemDTO hCase = newWorkingList.getSelectedItem();
                    long id = 0;
                    //                    if (hCase instanceof RuleListItemDTO) {
                    //                        id = ((RuleListItemDTO) hCase).getTCaseId();
                    //                    } else {
                    id = hCase.getId();
                    //                    }
                    openCase(id);
                    LOG.log(Level.FINER, "Time to execute loading cm: " + (System.currentTimeMillis() - start) + " ms");
                }
            }
        });

    }

    public TWmProcess getDisplayedProcess() {
        if (getDisplayedSceneProperty().get() instanceof WmMainFrameScene) {
            WmMainFrameScene wm = (WmMainFrameScene) getDisplayedSceneProperty().get();
            return wm.getController().getProcess();
        }
        return null;
    }

    public long getDisplayedCaseId() {
        TCase cs = getDisplayedCase();
        return cs == null ? 0L : cs.id;
    }

    public long getDisplayedProcessId() {
        TWmProcess process = getDisplayedProcess();
        return process == null ? 0L : process.id;
    }

    public TCase getDisplayedCase() {
        if (getDisplayedSceneProperty().get() instanceof CaseManagementScene) {
            CaseManagementScene cm = (CaseManagementScene) getDisplayedSceneProperty().get();
            return cm.getController().getCase();
        }
        return null;
    }

    public boolean isInWorkingList() {
        return isWorkingListShown() || getDisplayedCaseId() != 0L;
    }

    public boolean isInWorkflowList() {
        return isWorkflowListShown() || getDisplayedProcessId() != 0L;
    }

    public void showWorkflowList() {
        if (getController().isDisableMenu()) {
            return;
        }
        if (!isSceneAlreadyShown(WorkflowListScene.class)) {
            caseMenuItem.clearSelection();
            processMenuItem.clearSelection();
            jobMenuItem.clearFocus();
            try {
                initWorkflowList();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Cannot show workflow list!", ex);
            }
            displayScene(newWorkflowList);
            newWorkflowList.getController().getTableView().requestFocus();
        }
        processMenuItem.focusTitle();

    }

    public void initWorkflowList() throws IOException {
//        if (NEW_FILTER_LIST_SWITCH) {
        if (newWorkflowList == null) {
            //            workflowList = new ProcessTableMasterDetailScene();
            try {
                newWorkflowList = new WorkflowListScene();
            } catch (NamingException ex) {
                LOG.log(Level.SEVERE, "Cannot initialize workflow list!", ex);
            }
        }
        //SonarCube, avoid null pointer if fxml could not be loaded
        //TODO: FIXME, make better implementation after this new filter switch is gone
        if (newWorkflowList == null) {
            return;
        }
        //CPX-925
        newWorkflowList.setRowClickHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2 && newWorkflowList.getSelectedItem() != null) {
                    WorkflowListItemDTO process = newWorkflowList.getSelectedItem();
                    openProcess(process.id);
                }
            }
        });
    }

    public boolean reopenProcess() {
        long processId = getDisplayedProcessId();
        if (processId != 0L) {
            reopenProcess(processId);
            return true;
        }
        return false;
    }

    public boolean reopenCase() {
        long caseId = getDisplayedCaseId();
        if (caseId != 0L) {
            reopenCase(caseId);
            return true;
        }
        return false;
    }

    public void reopenProcess(Long pId) {
        closeProcess(pId);
        openProcess(pId);
    }

    //checks if process exists in one of the open entry scenes
    private boolean processListContainsEntity(long pId) {
        if (processMenuItem.getItems().stream().anyMatch((item) -> (item.getEntity().getId() == pId))) {
            return true;
        }
        return false;
    }

    public void openProcess(Long idToOpen) {
        if (!processListContainsEntity(idToOpen)) {

//            if (NEW_FILTER_LIST_SWITCH) {
            if (newWorkflowList == null) {
                try {
                    initWorkflowList();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    return;
                }
            }
            //CPX-925
            CpxScene processScene = newWorkflowList.openItem(idToOpen);
            if (processScene == null) {
                return;
            }
            if (displayScene(processScene)) {
                try {
                    OpenEntryScene<WmMainFrameScene, TWmProcess> newEntry = createOpenProcessEntryScene(idToOpen);
                    processMenuItem.getItems().add(newEntry);
                    processMenuItem.clearSelection();
                    processMenuItem.select(newEntry);//.getSelectionModel().select(newEntry);
                    caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
                    jobMenuItem.clearFocus();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
//            } else {
//                if (old_workflowList == null) {
//                    try {
//                        initWorkflowList();
//                    } catch (IOException ex) {
//                        LOG.log(Level.SEVERE, null, ex);
//                        return;
//                    }
//                }
//                //CPX-925
//                CpxScene processScene = old_workflowList.openItem(idToOpen);
//                if (processScene == null) {
//                    return;
//                }
//                if (displayScene(processScene)) {
//                    try {
//                        OpenEntryScene newEntry = createOpenEntryScene(ListType.WORKFLOW_LIST, idToOpen);
//                        lvOpenProcesses.getItems().add(newEntry);
//                        lvOpenProcesses.getSelectionModel().select(newEntry);
//                        lvOpenCases.getSelectionModel().clearSelection();
//                    } catch (CpxIllegalArgumentException | IOException ex) {
//                        LOG.log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
        } else {
            OpenEntryScene<WmMainFrameScene, TWmProcess> openEntry = getOpenProcessScene(idToOpen);
            if (openEntry != null) {
                //AWi-20170615:
                //call reload when scene is already openend to update content
                processMenuItem.select(openEntry);  // PNa: cpx-1255 
//                processMenuItem.clearSelection();//getSelectionModel().clearSelection();
                caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
                jobMenuItem.clearFocus();
                openEntry.getStoredScene().reload();
                displayScene(openEntry.getStoredScene());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private OpenEntryScene<CpxScene, TCase> getOpenCaseScene(long pId) {
        return (OpenEntryScene<CpxScene, TCase>) getOpenScene(ListType.WORKING_LIST, pId);
    }

    @SuppressWarnings("unchecked")
    private OpenEntryScene<WmMainFrameScene, TWmProcess> getOpenProcessScene(long pId) {
        return (OpenEntryScene<WmMainFrameScene, TWmProcess>) getOpenScene(ListType.WORKFLOW_LIST, pId);
    }

    //checks if a process exists in one of the open entry scenes
    private OpenEntryScene<? extends CpxScene, ? extends AbstractEntity> getOpenScene(ListType pType, long pId) {
        switch (pType) {
            case WORKFLOW_LIST:
                for (OpenEntryScene<WmMainFrameScene, TWmProcess> item : processMenuItem.getItems()) {
                    if (item.getEntity().id == pId) {
                        return item;
                    }
                }
                break;
            case WORKING_LIST:
                for (OpenEntryScene<CpxScene, TCase> item : caseMenuItem.getItems()) {
                    if (item.getEntity().id == pId) {
                        return item;
                    }
                }
                break;
            default:
                LOG.log(Level.WARNING, "Unknown list type: " + pType);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private OpenEntryScene<CpxScene, TCase> createOpenCaseEntryScene(Long id) throws IOException {
        return (OpenEntryScene<CpxScene, TCase>) createOpenEntryScene(ListType.WORKING_LIST, id);
    }

    @SuppressWarnings("unchecked")
    private OpenEntryScene<WmMainFrameScene, TWmProcess> createOpenProcessEntryScene(Long id) throws IOException {
        return (OpenEntryScene<WmMainFrameScene, TWmProcess>) createOpenEntryScene(ListType.WORKFLOW_LIST, id);
    }

    private OpenEntryScene<? extends CpxScene, ? extends AbstractEntity> createOpenEntryScene(ListType type, Long id) throws IOException {

        switch (type) {
            case WORKING_LIST:
                OpenEntryScene<CpxScene, TCase> openCaseEntry = new OpenCase(getDisplayedSceneProperty().getValue());
                openCaseEntry.getCloseRequestedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                        caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
//                        jobMenuItem.clearFocus();
                        OpenEntryScene<CpxScene, TCase> openCaseEntry = getCaseScene(id);

                        CpxScene displayedScene = getDisplayedSceneProperty().getValue();
                        //needs to becloseCase changed, should determand of scene level, with some identity checks 
                        //nneds to determand if the current open item is already shown and so it must be closed and working list is to show
                        if (getDisplayedSceneProperty().getValue() instanceof CaseManagementScene) {
                            //unclean check in my eyes needs to be changed
                            if (((CaseManagementScene) displayedScene).getDisplayedCase().getId() == id) {
                                setDisplayedSceneProperty(null);
                                if (license.isDrgModule() || license.isPeppModule()) {
                                    showWorkingList();
                                }
                            }
                        }
                        caseMenuItem.getItems().remove(openCaseEntry);
                        if (caseMenuItem.getItems().isEmpty() && (getDisplayedScene() instanceof WorkingListScene)) {
                            caseMenuItem.focusTitle();
                            processMenuItem.clearSelection();//getSelectionModel().clearSelection();
                            jobMenuItem.clearFocus();
                        }
                        openCaseEntry.getRoot().setOnMouseClicked(null);
                        getController().removeContent((Pane) openCaseEntry.getStoredScene().getRoot());
                        openCaseEntry.getCloseRequestedProperty().removeListener(this);
//                        for (ListCell<OpenEntryScene<CpxScene, TCase>> cell : ((CasesFactory) caseMenuItem.getCellFactory()).getCells()) {
//                            cell.setOnMouseClicked(null); //otherwise OpenEntryScene stays open forever -> memory leak
//                        }
                    }
                });
                openCaseEntry.getRoot().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (getController().isDisableMenu()) {
                            return;
                        }
                        if (event.getClickCount() >= 1) {
                            CpxScene displayedScene = getDisplayedSceneProperty().getValue();
                            //dirty check if scene is already displayed
                            if (displayedScene instanceof CaseManagementScene) {
                                if (((CaseManagementScene) displayedScene).getDisplayedCase().getId() == openCaseEntry.getEntity().getId()) {
                                    return;
                                }
                            }
                            displayScene(openCaseEntry.getStoredScene());
                            caseMenuItem.select(openCaseEntry);//.getSelectionModel().select(openEntry);
                            processMenuItem.clearSelection();//getSelectionModel().clearSelection();
                            jobMenuItem.clearFocus();
                        }
                    }
                });
                return openCaseEntry;
            case WORKFLOW_LIST:
                OpenEntryScene<WmMainFrameScene, TWmProcess> openProcessEntry = new OpenProcess((WmMainFrameScene) getDisplayedSceneProperty().getValue());
                openProcessEntry.getCloseRequestedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                        processMenuItem.clearSelection();//getSelectionModel().clearSelection();
//                        jobMenuItem.clearFocus();
                        OpenEntryScene<WmMainFrameScene, TWmProcess> openProcessEntry = getProcessScene(id);
                        CpxScene displayedScene = getDisplayedSceneProperty().getValue();
                        //needs to be changed, should determand of scene level, with some identity checks 
                        //nneds to determand if the current open item is already shown and so it must be closed and working list is to show
                        if (getDisplayedSceneProperty().getValue() instanceof WmMainFrameScene) {
                            //unclean check in my eyes needs to be changed
                            if (((WmMainFrameScene) displayedScene).getController().getProcess().getId() == id) {
                                setDisplayedSceneProperty(null);
                                ((WmMainFrameScene) displayedScene).getController().clearFacade();
                                if (license.isFmModule()) {
                                    showWorkflowList();
                                }
                            }
                        }
                        processMenuItem.getItems().remove(openProcessEntry);
                        if (processMenuItem.getItems().isEmpty() && (getDisplayedScene() instanceof WorkflowListScene)) {
                            processMenuItem.focusTitle();
                            caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
                            jobMenuItem.clearFocus();
                        }
                        getController().removeContent((Pane) openProcessEntry.getStoredScene().getRoot());
                        for (ListCell<OpenEntryScene<WmMainFrameScene, TWmProcess>> cell : ((ProcessFactory) processMenuItem.getCellFactory()).getCells()) {
                            cell.setOnMouseClicked(null); //otherwise OpenEntryScene stays open forever -> memory leak
                        }
//                        if(displayedScene != null && (displayedScene instanceof WmMainFrameScene)){
//                            ((WmMainFrameScene) displayedScene).getController().clearFacade();
//                        }
                        
                    }
                });
                return openProcessEntry;
            default:
                return null;
        }
    }

    @Override
    public boolean isProcessCaseOpen(Long pCaseId) {
        return !getProcessCases(pCaseId).isEmpty();
    }

    @Override
    public boolean isCaseOpen(Long pCaseId) {
        return getCaseScene(pCaseId) != null;
    }

    @Override
    public boolean isProcessOpen(Long pProcessId) {
        return getProcessScene(pProcessId) != null;
    }

    private OpenEntryScene<CpxScene, TCase> getCaseScene(Long pId) {
        final ObjectProperty<OpenEntryScene<CpxScene, TCase>> openCaseEntry = new SimpleObjectProperty<>();
        caseMenuItem.getItems().stream().forEach((t) -> {
            if (t.getEntity().getId() == pId) {
                openCaseEntry.set(t);
            }
        });
        return openCaseEntry.get();
    }

    private OpenEntryScene<WmMainFrameScene, TWmProcess> getProcessScene(Long pId) {
        final ObjectProperty<OpenEntryScene<WmMainFrameScene, TWmProcess>> openProcessEntry = new SimpleObjectProperty<>();
        processMenuItem.getItems().stream().forEach((t) -> {
            if (t.getEntity().getId() == pId) {
                openProcessEntry.set(t);
            }
        });
        return openProcessEntry.get();
    }

    /**
     * unlock process and remove it from scene
     *
     * @param pProcessId process id
     * @return is successfully closed (can be false if not found)?
     */
    @Override
    public boolean closeProcess(final long pProcessId) {
        if (pProcessId == 0L) {
            return false;
        }
        List<OpenEntryScene<WmMainFrameScene, TWmProcess>> tmp = new ArrayList<>(processMenuItem.getItems());
        Iterator<OpenEntryScene<WmMainFrameScene, TWmProcess>> it = tmp.iterator();
        while (it.hasNext()) {
            OpenEntryScene<WmMainFrameScene, TWmProcess> next = it.next();
            TWmProcess process = next.getEntity();
            if (process != null && process.id == pProcessId) {
                next.close();
                return true;
            }
        }
        return false;
    }

    @Override
    public int closeAllCases(final long pCaseId) {
        boolean result = closeCase(pCaseId);
        int count = closeProcessCases(pCaseId);
        return count + (result ? 1 : 0);
    }

    public List<OpenEntryScene<WmMainFrameScene, TWmProcess>> getProcessCases(final Long pCaseId) {
        final List<OpenEntryScene<WmMainFrameScene, TWmProcess>> result = new ArrayList<>();
        if (pCaseId == null || pCaseId.equals(0L)) {
            return result;
        }
        List<OpenEntryScene<WmMainFrameScene, TWmProcess>> tmp = new ArrayList<>(processMenuItem.getItems());
        Iterator<OpenEntryScene<WmMainFrameScene, TWmProcess>> it = tmp.iterator();
        //int count = 0;
        while (it.hasNext()) {
            OpenEntryScene<WmMainFrameScene, TWmProcess> next = it.next();
            TWmProcess process = next.getEntity();
            if (process != null) {
                List<CaseTab> tabs = next.getStoredScene().getController().getCaseTabs();
                for (CaseTab tab : tabs) {
                    if (String.valueOf(pCaseId).equals(tab.getId())) {
                        //result.add(tab);
                        result.add(next);
                        break;
//                        if (next.getStoredScene().getController().closeTab(tab)) {
//                            count++;
//                            break;
//                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * unlock process and remove it from scene
     *
     * @param pCaseId process id
     * @return is successfully closed (can be false if not found)?
     */
    public int closeProcessCases(final Long pCaseId) {
        int count = 0;
        for (OpenEntryScene<WmMainFrameScene, TWmProcess> next : getProcessCases(pCaseId)) {
            TWmProcess process = next.getEntity();
            if (process != null) {
                List<CaseTab> tabs = next.getStoredScene().getController().getCaseTabs();
                for (CaseTab tab : tabs) {
                    if (String.valueOf(pCaseId).equals(tab.getId())) {
                        if (next.getStoredScene().getController().closeTab(tab)) {
                            count++;
                            break;
                        }
                    }
                }
            }
        }
        return count;
    }

    public void closeAllOpenCases() {
        //avoid concurrentModificationException
        List<OpenEntryScene<CpxScene, TCase>> tmp = new ArrayList<>(caseMenuItem.getItems());
        Iterator<OpenEntryScene<CpxScene, TCase>> it = tmp.iterator();
        while (it.hasNext()) {
            OpenEntryScene<CpxScene, TCase> next = it.next();
            next.close();
        }
    }

    public void closeAllOpenProcesses() {
        //avoid concurrentModificationException
        List<OpenEntryScene<WmMainFrameScene, TWmProcess>> tmp = new ArrayList<>(processMenuItem.getItems());
        Iterator<OpenEntryScene<WmMainFrameScene, TWmProcess>> it = tmp.iterator();
        while (it.hasNext()) {
            OpenEntryScene<WmMainFrameScene, TWmProcess> next = it.next();
            next.close();
        }
    }

    /**
     * unlock case and remove it from scene
     *
     * @param pCaseId case id
     * @return is successfully closed (can be false if not found)?
     */
    public boolean closeCase(final long pCaseId) {
        if (pCaseId == 0L) {
            return false;
        }
        List<OpenEntryScene<CpxScene, TCase>> tmp = new ArrayList<>(caseMenuItem.getItems());
        Iterator<OpenEntryScene<CpxScene, TCase>> it = tmp.iterator();
        while (it.hasNext()) {
            OpenEntryScene<CpxScene, TCase> next = it.next();
            TCase cs = next.getEntity();
            if (cs != null && cs.id == pCaseId) {
                next.close();
                return true;
            }
        }
        return false;
    }

    public void reopenCase(Long pId) {
        closeCase(pId);
        openCase(pId);
    }

    //checks if case exists in one of the open entry scenes
    private boolean caseListContainsEntity(long pEntityId) {
        if (caseMenuItem.getItems().stream().anyMatch((item) -> (item.getEntity().getId() == pEntityId))) {
            return true;
        }
        return false;
    }

    public void openCase(Long pId) {
        if (!caseListContainsEntity(pId)) {
            //TODO:ENABLE OPEN ITEM, CPX-925
            CpxScene caseScene;
//            if (NEW_FILTER_LIST_SWITCH) {
            if (newWorkingList == null) {
                try {
                    initWorkingList();
                } catch (IOException | CpxIllegalArgumentException ex) {
                    LOG.log(Level.SEVERE, "Cannot initialize working list!", ex);
                    return;
                }
            }
            caseScene = newWorkingList.openItem(pId);
//            } else {
//                caseScene = old_workingList.openItem(pId);
//            }
//            CpxScene caseScene = null;
            if (caseScene == null) {
                return;
            }
            if (displayScene(caseScene)) {
                try {
                    OpenEntryScene<CpxScene, TCase> newEntry = createOpenCaseEntryScene(pId);
                    caseMenuItem.getItems().add(newEntry);
                    caseMenuItem.clearSelection();
                    caseMenuItem.select(newEntry);//.getSelectionModel().select(newEntry);
                    processMenuItem.clearSelection();//getSelectionModel().clearSelection();
                    jobMenuItem.clearFocus();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        } else {
            OpenEntryScene<CpxScene, TCase> openEntry = getOpenCaseScene(pId);
            if (openEntry != null) {
                //AWi-20170615:
                //call reload when scene is already openend to update content
                caseMenuItem.select(openEntry);//.getSelectionModel().select(openEntry);
                processMenuItem.clearSelection();//getSelectionModel().clearSelection();
                jobMenuItem.clearFocus();
                openEntry.getStoredScene().reload();
                displayScene(openEntry.getStoredScene());
            }
        }
    }

    public WorkingListScene getWorkingList() {
        return newWorkingList;
    }

    public WorkflowListScene getWorkflowList() {
        return newWorkflowList;
    }

    protected void removeWorkingList() {
        newWorkingList = null;
        caseMenuItem.clearItems();
//        old_workingList = null;
    }

    protected void removeWorkflowList() {
        newWorkflowList = null;
        if (processMenuItem != null) {
            processMenuItem.clearItems();
        }
//        old_workflowList = null;
    }

    protected void removeDocumentImport() {
        documentImport = null;
//        old_workflowList = null;
    }

    protected void removeBatchgrouping() {
        jobManager = null;
    }

    protected void removeCaseMerging() {
        caseMerging = null;
    }

    @Override
    public ShortcutHandler getShortcuts() {
        if (isInWorkingList()) {
            CaseList caseList = getWorkingList().getController().getCaseList();
            if (caseList != null) {
                return caseList;
            }
            RuleList ruleList = getWorkingList().getController().getRuleList();
            if (ruleList != null) {
                return ruleList;
            }
            QuotaList quotaList = getWorkingList().getController().getQuotaList();
            if (quotaList != null) {
                return quotaList;
            }
        }
        if (isInWorkflowList()) {
            return getWorkflowList().getController().getProcessList();
        }
        return null;
    }

    protected void saveRecentClientScene() {
//        if (isWorkflowListShown()) {
//            Session.instance().setRecentClientView("WORKFLOW_LIST");
//        } else if (isWorkingListShown()) {
//            if (getWorkingList().isWorkingList()) {
//                Session.instance().setRecentClientView("WORKING_LIST");
//            } else if (getWorkingList().isRuleList()) {
//                Session.instance().setRecentClientView("RULE_LIST");
//            }
//        } else if (isCaseMergingShown()) {
//            Session.instance().setRecentClientView("CASE_MERGING");
//        } else if (isBatchgroupingShown()) {
//            Session.instance().setRecentClientView("BATCH_GROUPING");
//        } else if (isDocumentImportShown()) {
//            Session.instance().setRecentClientView("DOCUMENT_IMPORT");
//        }
        final CpxScene displayedScene = getDisplayedScene();
        try {
            Session.instance().setRecentClientScene(displayedScene == null ? null : displayedScene.getClass().getName());
        } catch (EJBException ex) {
            LOG.log(Level.FINEST, "Was not able to store recent client view, connection to server is broken maybe", ex);
        }
    }

    @Override
    public void restoreRecentClientScene() {
        final String recentScene = StringUtils.trimToEmpty(Session.instance().getRecentClientScene());
        if (!recentScene.isEmpty()) {
            if (recentScene.endsWith(WorkflowListScene.class.getSimpleName())
                    || recentScene.endsWith(WmMainFrameScene.class.getSimpleName())) {
                //Workflow List
                if (license.isFmModule()) {
                    showWorkflowList();
                }
            } else if (recentScene.endsWith(WorkingListScene.class.getSimpleName())
                    || recentScene.endsWith(CaseManagementScene.class.getSimpleName())) {
                //Working List
                if (license.isDrgModule() || license.isPeppModule()) {
                    showWorkingList();
                }
            } else if (recentScene.endsWith(JobManagerScene.class.getSimpleName())) {
                //Batchgrouping
                openBatchgrouping();
            } else if (recentScene.endsWith(CaseMergingScene.class.getSimpleName())) {
                //Case Merging
                openCaseMerging();
            } else if (recentScene.endsWith(DocumentImportScene.class.getSimpleName())) {
                //Document Import
                openDocumentImport();
            } else {
                //Default:
                LOG.log(Level.WARNING, "Recent client scene was '" + recentScene + "', but I don't know what to do with it!");
                if (license.isDrgModule() || license.isPeppModule()) {
                    showWorkingList();
                }
            }
        } else {
            //Default:
            if (license.isDrgModule() || license.isPeppModule()) {
                showWorkingList();
            }
        }
    }

    @Override
    public void cleanUp() {
        saveRecentClientScene();
        if (caseMerging != null) {
            caseMerging.getController().close();
        }
        if (jobManager != null) {
            jobManager.getController().close();
        }
        if (documentImport != null) {
            documentImport.getController().close();
        }
        super.cleanUp();
        displayScene(null);
        removeWorkingList();
        removeWorkflowList();
        removeDocumentImport();
        removeBatchgrouping();
        removeCaseMerging();
        Session.instance().resetCaseCount();
        Session.instance().resetProcessCount();
        Session.instance().resetPatientCount();
        Session.instance().resetCanceledCaseCount();
        Session.instance().resetCanceledProcessCount();
    }

//    public Set<String> getListOfLicensedModules() {
//        Set<String> moduleList = license.getModuleList();
//        return moduleList;
//    }
//
//    public boolean containsFMModule() {
//        return license.getModuleList() == null || license.getModuleList().isEmpty() ? false : license.getModuleList().contains(ModuleManager.PARAMETER_FM);
//    }
//
//    public boolean containsDRGModule() {
//        return license.getModuleList() == null || license.getModuleList().isEmpty() ? false : license.getModuleList().contains(ModuleManager.PARAMETER_DRG);
//    }
//
//    public boolean containsPEPPModule() {
//        return license.getModuleList() == null || license.getModuleList().isEmpty() ? false : license.getModuleList().contains(ModuleManager.PARAMETER_PEPP);
//    }
    private class CasesFactory implements Callback<ListView<OpenEntryScene<CpxScene, TCase>>, ListCell<OpenEntryScene<CpxScene, TCase>>> {

//        private final Set<ListCell<OpenEntryScene<CpxScene, TCase>>> cells = new HashSet<>();
//
//        public Set<ListCell<OpenEntryScene<CpxScene, TCase>>> getCells() {
//            return new HashSet<>(cells);
//        }

        @Override
        public ListCell<OpenEntryScene<CpxScene, TCase>> call(ListView<OpenEntryScene<CpxScene, TCase>> param) {
            return new ListCell<OpenEntryScene<CpxScene, TCase>>() {
                @Override
                public void updateItem(OpenEntryScene<CpxScene, TCase> openEntry, boolean pEmpty) {
                    super.updateItem(openEntry, pEmpty);
                    if (openEntry == null || pEmpty) {
                        setGraphic(null);
                        return;
                    }
                    //TODO: check Groupermodel; 
                    openEntry.checkGrouperModel();
                    setGraphic(openEntry.getRoot());
//                    //2018-08-20 DNi: OpenEntryScene stays open when this is an EventHandler instead of a WeakEventHandler -> memory leak!
//                    //BUT: With a WeakEventHandler you cannot switch between open cases
//                    setOnMouseClicked(new EventHandler<MouseEvent>() {
//                        @Override
//                        public void handle(MouseEvent event) {
//                            if (getController().isDisableMenu()) {
//                                return;
//                            }
//                            if (event.getClickCount() >= 1) {
//                                CpxScene displayedScene = getDisplayedSceneProperty().getValue();
//                                //dirty check if scene is already displayed
//                                if (displayedScene instanceof CaseManagementScene) {
//                                    if (((CaseManagementScene) displayedScene).getDisplayedCase().getId() == openEntry.getEntity().getId()) {
//                                        return;
//                                    }
//                                }
//                                displayScene(openEntry.getStoredScene());
//                                caseMenuItem.select(openEntry);//.getSelectionModel().select(openEntry);
//                                processMenuItem.clearSelection();//getSelectionModel().clearSelection();
//                                jobMenuItem.clearFocus();
//                            }
//                        }
//                    });
//                    cells.add(this);
                }
            };
        }
    }

    private class ProcessFactory implements Callback<ListView<OpenEntryScene<WmMainFrameScene, TWmProcess>>, ListCell<OpenEntryScene<WmMainFrameScene, TWmProcess>>> {

        private final Set<ListCell<OpenEntryScene<WmMainFrameScene, TWmProcess>>> cells = new HashSet<>();

        public Set<ListCell<OpenEntryScene<WmMainFrameScene, TWmProcess>>> getCells() {
            return new HashSet<>(cells);
        }

        @Override
        public ListCell<OpenEntryScene<WmMainFrameScene, TWmProcess>> call(ListView<OpenEntryScene<WmMainFrameScene, TWmProcess>> param) {
            return new ListCell<OpenEntryScene<WmMainFrameScene, TWmProcess>>() {

                @Override
                public void updateItem(OpenEntryScene<WmMainFrameScene, TWmProcess> openEntry, boolean pEmpty) {
                    super.updateItem(openEntry, pEmpty);

                    if (openEntry == null || pEmpty) {
                        setGraphic(null);
                        return;
                    }
                    setGraphic(openEntry.getRoot());
                    //2018-08-20 DNi: OpenEntryScene stays open when this is an EventHandler instead of a WeakEventHandler -> memory leak!
                    //BUT: With a WeakEventHandler you cannot switch between open processes
                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (getController().isDisableMenu()) {
                                return;
                            }
                            if (event.getClickCount() >= 1) {
                                CpxScene displayedScene = getDisplayedSceneProperty().getValue();
                                //needs to be changed, should determand of scene level, with some identity checks 
                                //needs to determand if the current open item is already shown
                                if (getDisplayedSceneProperty().getValue() instanceof WmMainFrameScene) {
                                    //unclean check in my eyes needs to be changed
                                    final TWmProcess entity = openEntry.getEntity();
                                    if (entity == null) {
                                        LOG.log(Level.WARNING, "open entry scene does not own a process entity!");
                                    } else {
                                        if (((WmMainFrameScene) displayedScene).getController().getProcess().getId() == entity.getId()) {
                                            return;
                                        }
                                    }
                                }
                                displayScene(openEntry.getStoredScene());
                                processMenuItem.select(openEntry);//.getSelectionModel().select(openEntry);
                                caseMenuItem.clearSelection();//getSelectionModel().clearSelection();
                                jobMenuItem.clearFocus();
                            }
                        }
                    });
                    cells.add(this);
                }
            };
        }
    }

    private class JobMenuItem extends ToolbarMultiMenuItem {

        public JobMenuItem() {
            super(getController().getToolbar());
            Button btnBatchJob = new Button(Lang.getMenuBatchGrouping()/*Lang.getBatchprocessing()*/);
            btnBatchJob.disableProperty().bind(disableProperty());
            btnBatchJob.setStyle("-fx-alignment: CENTER-LEFT;");
            btnBatchJob.setMaxWidth(Double.MAX_VALUE);
            btnBatchJob.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (openBatchgrouping()) {
                        focusNode(getController().getToolbar().isExtended() ? getExtendedNode() : getReducedNode());
                        hidePopover();
                    }
                }
            });

            Button btnCaseMerging = new Button(Lang.getCaseMerging());
            btnCaseMerging.setMaxWidth(Double.MAX_VALUE);
            btnCaseMerging.disableProperty().bind(disableProperty());
            btnCaseMerging.setStyle("-fx-alignment: CENTER-LEFT;");
            btnCaseMerging.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (openCaseMerging()) {
                        focusNode(getController().getToolbar().isExtended() ? getExtendedNode() : getReducedNode());
                        hidePopover();
                    }
                }
            });

            Button btnDocumentImport = new Button("Dokumentenimport");
            btnDocumentImport.setMaxWidth(Double.MAX_VALUE);
//            btnDocumentImport.disableProperty().bind(getController().disableMenuProperty());
            btnDocumentImport.setStyle("-fx-alignment: CENTER-LEFT;");
            btnDocumentImport.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (openDocumentImport()) {
                        hidePopover();
                        focusNode(getController().getToolbar().isExtended() ? getExtendedNode() : getReducedNode());
                    }
                }
            });
            add(btnBatchJob);
            add(btnCaseMerging);
            add(btnDocumentImport);
        }
    }

    private class OpenCase extends OpenEntryScene<CpxScene, TCase> {

        public OpenCase(CpxScene pScene) throws IOException {
            super(pScene);
        }

        @Override
        protected void setValues(Controller<?> pController) {
            if (pController instanceof CaseManagementFXMLController) {
                getController().setEntryHeaderText(getEntity().getCsCaseNumber());
                TPatient patient = getEntity().getPatient();
                getController().setDescription(getPatientDescription(patient));
                getController().setDescriptionTooltip(getPatientDescriptionTooltip(patient));
            }
        }

        @Override
        public TCase getEntity() {
            if (getStoredScene().getController() instanceof CaseManagementFXMLController) {
                ((CaseManagementScene)getStoredScene()).getDisplayedCase();
//                return ((CaseManagementFXMLController) getStoredScene().getController()).getCase();
                return ((CaseManagementScene)getStoredScene()).getDisplayedCase();
            }
            return null;
        }
        
        private String getPatientDescription(TPatient pPatient) {
            pPatient = Objects.requireNonNullElse(pPatient, new TPatient());
            String patientName = Objects.requireNonNullElse(pPatient.getPatAbrrName(),"");
            return new StringBuilder("Pat. ").append((!patientName.isEmpty())?patientName:"n.V.").toString();
        }
        private String getPatientDescriptionTooltip(TPatient pPatient) {
            pPatient = Objects.requireNonNullElse(pPatient, new TPatient());
            String patientName = Objects.requireNonNullElse(pPatient.getPatFullName(),"");
            return new StringBuilder("Patient ").append((!patientName.isEmpty())?patientName:"n.V.").toString();
        }
        
        @Override
        public void checkGrouperModel() {
            if (getStoredScene().getController() instanceof CaseManagementFXMLController) {
                ((CaseManagementFXMLController)(getStoredScene().getController())).checkGrouperModel();
            }
        }
    }

    private class OpenProcess extends OpenEntryScene<WmMainFrameScene, TWmProcess> {

        public OpenProcess(WmMainFrameScene pScene) throws IOException {
            super(pScene);
        }

        @Override
        protected void setValues(Controller<?> pController) {
            if (pController instanceof WmMainFrameFXMLController) {
                getController().setEntryHeaderText(String.valueOf(((WmMainFrameFXMLController) pController).getProcess().getWorkflowNumber()));
//                TPatient patient = ((WmMainFrameFXMLController) pController).getProcess().getPatient();
                TCase hCase = getEntity()!=null?getEntity().getMainCase():null;
                getController().setDescription(getCaseDescription(hCase));//patient.getPatSecName() != null ? patient.getPatSecName() : "" + (patient.getPatFirstName() != null ? (" ," + patient.getPatFirstName()) : ""));
                getController().setDescriptionTooltip(getCaseDescriptionTooltip(hCase));
            }
        }

        @Override
        public TWmProcess getEntity() {
            if (getStoredScene().getController() instanceof WmMainFrameFXMLController) {
                return getStoredScene().getController().getProcess();
            }
            return null;
        }

        private String getCaseDescription(TCase hCase) {
            hCase = Objects.requireNonNullElse(hCase, new TCase());
            return new StringBuilder("Fall ").append(hCase.getCsCaseNumber()!=null?hCase.getCsCaseNumber():"n.V.").toString();
        }
        private String getCaseDescriptionTooltip(TCase hCase) {
            hCase = Objects.requireNonNullElse(hCase, new TCase());
            return new StringBuilder("Fallnummer ").append(hCase.getCsCaseNumber()!=null?hCase.getCsCaseNumber():"n.V.").toString();
        }
    }

}

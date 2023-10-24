/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filterlists.cases;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.casedetails.section.CmCaseOverviewSection;
import de.lb.cpx.client.app.cm.fx.simulation.menu.ProcessEditingEvent;
import de.lb.cpx.client.app.cm.fx.wizard.AddCaseWizard;
import de.lb.cpx.client.app.menu.fx.event.DataActionEvent;
import de.lb.cpx.client.app.menu.fx.event.Events;
import de.lb.cpx.client.app.menu.fx.table_master_detail.FilterListFXMLController;
import de.lb.cpx.client.app.menu.model.ListType;
import de.lb.cpx.client.app.rulefilter.dialog.RuleFilterApplicationUsage;
import de.lb.cpx.client.app.rulefilter.dialog.RuleFilterDialog;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.alert.LockCallback;
import de.lb.cpx.client.core.model.fx.combobox.GrouperModelsCombobox;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.searchitem.SearchItem;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.CaseDetailsCancelReasonEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Specific controller for working list, init menu accordingly
 *
 * @author wilde
 */
public class WorkingListFXMLController extends FilterListFXMLController<WorkingListItemDTO> {

    private static final Logger LOG = Logger.getLogger(WorkingListFXMLController.class.getName());
    
    private GrouperModelsCombobox cbGrouperModel;

    private final Callback<SearchListAttribute, SearchItem> defaultAdditionalSearchItemFactory = new Callback<SearchListAttribute, SearchItem>() {
        @Override
        public SearchItem call(SearchListAttribute p) {
            SearchItem item = new SearchItem(p.getKey(), "");
            if (WorkingListAttributes.rules.equals(p.getKey()) || RuleListAttributes.rules.equals(p.getKey())) {
                item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        if (MouseButton.PRIMARY.equals(t.getButton())) {
                            addRuleFilterDialog();
                        }
                    }
                });
            }
            return item;
        }
    };
//    private P21ExportDialog p21ExportDialog;

    @Override
    public String getItemsName() {
//        if (isCaseListShown()) {
//            return "Fälle";
//        } else {
//            return "Regeln";
//        }
        return "Fälle";
    }

    @Override
    public int getNumberOfAllIds() {
        return Session.instance().getCaseCount();
    }

    @Override
    public int getNumberOfAllCanceledIds() {
        return Session.instance().getCanceledCaseCount();
    }

//    @Override
//    public FilterOption getFilterOptionCancel() {
//        List<FilterOption> result = getFilterOptions(WorkingListAttributes.isCancel);
//        if (result == null || result.isEmpty()) {
//            return null;
//        }
//        if (result.size() > 1) {
//            LOG.log(Level.WARNING, "why there are multiple cancel filter options in working list (found {0})?! You should check this!", result.size());
//        }
//        return result.iterator().next();
//    }
    @Override
    public ColumnOption getColumnOptionCancel() {
        return getColumnOption(WorkingListAttributes.isCancel);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb); //To change body of generated methods, choose Tools | Templates.
        cbGrouperModel = new GrouperModelsCombobox();
        Callback <GDRGModel, Boolean> onUpdateParent = new Callback<GDRGModel, Boolean> () {
            @Override
            public Boolean call(GDRGModel p) {
                reload();
                return true;
            }
        };
        cbGrouperModel.setOnUpdateParent(onUpdateParent);
        addNodeToMenu(cbGrouperModel);
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
    }

    @Override
    public AsyncTableView<WorkingListItemDTO> updateTableView() {
        switch (getScene().getFilterManager().getListType()) {
            case WORKING:
                CaseList caseList = new CaseList(this);
                caseList.setAdditionalFilterSearchItemFactory(defaultAdditionalSearchItemFactory);
                return caseList;
            case RULE:
                RuleList ruleList = new RuleList(this);
                ruleList.setAdditionalFilterSearchItemFactory(defaultAdditionalSearchItemFactory);
                return ruleList;
            case QUOTA:
                QuotaList quotaList = new QuotaList(this);
                quotaList.setAdditionalFilterSearchItemFactory(defaultAdditionalSearchItemFactory);
                return quotaList;
            default:
                //TODO:throw exception?
                return null;
        }
    }

    public boolean isCaseListShown() {
        return getTableView() instanceof CaseList;
    }

    public boolean isRuleListShown() {
        return getTableView() instanceof RuleList;
    }

    public boolean isQuotaListShown() {
        return getTableView() instanceof QuotaList;
    }

    public CaseList getCaseList() {
        if (isCaseListShown()) {
            return (CaseList) getTableView();
        }
        return null;
    }

    public RuleList getRuleList() {
        if (isRuleListShown()) {
            return (RuleList) getTableView();
        }
        return null;
    }

    public QuotaList getQuotaList() {
        if (isQuotaListShown()) {
            return (QuotaList) getTableView();
        }
        return null;
    }

    @Override
    public Parent getDetailContent(WorkingListItemDTO pItem) {
        //TODO: REDO detail content
        if (pItem != null) {
//            long start = System.currentTimeMillis();
            final long caseId = pItem.id;
//            final long caseId = item instanceof RuleListItemDTO
//                    ? ((RuleListItemDTO) item).getCheckResultId()
//                    : item.id;

            if (caseId == 0L) {
                LOG.log(Level.WARNING, "caseId is equal to 0!");
            }

            TCase hCase = ((WorkingListScene) getScene()).getCaseForCaseDetail(caseId);//getServiceBean().get().getCaseForDetail(caseId);

            if (hCase == null) {
                LOG.log(Level.WARNING, "No hospital case found with this id: " + caseId);
            }

//            LOG.log(Level.FINER, "load case with id " + caseId + " for details in " + (System.currentTimeMillis() - start) + " ms");
//            start = System.currentTimeMillis();
            CmCaseOverviewSection over = new CmCaseOverviewSection(hCase);
//            LOG.log(Level.FINER, "set values for case id " + caseId + " in details in " + (System.currentTimeMillis() - start) + " ms");
//            start = System.currentTimeMillis();
            Parent detail = over.getDetailContent();
//            LOG.log(Level.FINER, "render details for case with id " + caseId + " in " + (System.currentTimeMillis() - start) + " ms");
            return detail;
        }
        return new HBox();
    }

    public Button createAddCaseButton() {
        Button btnAddCase = new Button();

        btnAddCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
        btnAddCase.setTooltip(new Tooltip(Lang.getAddCaseTitle()));
        btnAddCase.getStyleClass().add("cpx-icon-button");
        btnAddCase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //RSH 07.11.2017 CPX-628  check whether the grouper is set to "Automatic" mode.
                if (!CpxClientConfig.instance().getSelectedGrouper().equals(GDRGModel.AUTOMATIC)) {
                    String msg = Lang.getGrouperEinstellungenConfirm(String.valueOf(CpxClientConfig.instance().getSelectedGrouper().getModelYear()));
                    AlertDialog.createConfirmationDialog(msg).showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (t.equals(ButtonType.OK)) {
                                addCaseDialog();
                            }
                        }

                    });
                } else {
                    addCaseDialog();
                }
            }
        });
        return btnAddCase;
    }

    private void addCaseDialog() {
        try {
        boolean doAnonym = (( WorkingListScene)WorkingListFXMLController.this.getScene()).isCreateCaseAnonymized();

        AddCaseWizard addCaseWizard = new AddCaseWizard(MainApp.getWindow(), "Add Case", doAnonym);
            addCaseWizard.showAndWait().ifPresent(result -> {
                if (result == ButtonType.FINISH) {
                    LOG.info("ButtonType " + result.getButtonData().getTypeCode());
                    if ((addCaseWizard.getCreatedCase() == null || addCaseWizard.getCreatedCase().getId() == 0)) {
                        AlertDialog dlg = AlertDialog.createErrorDialog(Lang.getCasecreationFailed(), MainApp.getStage());
                        dlg.showAndWait();
                        reload();
                        return;
                    } else {
                        //Kein Version vom  geöffneten vorhandenen Fall  
                        if (addCaseWizard.getCreatedCase().getId() == -1) {
                            AlertDialog dlg = AlertDialog.createErrorDialog(Lang.getErrorVersionCouldNotCreate() + "\n" + Lang.getItemLockedObj().getTooltip(), MainApp.getStage());
                            dlg.showAndWait();
                            reload();
                            return;
                        }
                        //Datenbank Problem
                        if (addCaseWizard.getCreatedCase().getId() == -2) {
                            AlertDialog dlg = AlertDialog.createErrorDialog(Lang.getErrorOccured(), MainApp.getStage());
                            dlg.showAndWait();
                            reload();
                            return;
                        }
                        //kein Version angelegt 
                        if (addCaseWizard.getCreatedCase().getId() == -3) {
                            AlertDialog dlg = AlertDialog.createErrorDialog(Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber() + "\n" + Lang.getErrorVersionCouldNotCreate(), MainApp.getStage());
                            dlg.showAndWait();
                            reload();
                            return;
                        }
                    }

                    AlertDialog dlg = AlertDialog.createInformationDialog(Lang.getCasecreationSuccess(), MainApp.getStage());
                    dlg.setHeaderText(Lang.getSuccess());
                    dlg.getDialogPane().setContentText(Lang.getCasecreationSuccess());
                    ButtonType type = new ButtonType(Lang.getOpenCase(), ButtonBar.ButtonData.FINISH);
                    dlg.getButtonTypes().add(type);
                    dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (t.getButtonData().equals(ButtonBar.ButtonData.FINISH)) {
                                //opencase
                                //openItem(addCaseWizard.getCreatedCase().getId());
                                /*getTableView().getItems().add(addCaseWizard.getCreatedCase());
                                        getTableView().getSelectionModel().select(addCaseWizard.getCreatedCase());*/

                                Events.instance().setNewEvent(new DataActionEvent<>(addCaseWizard.getCreatedCase().getId(), ListType.WORKING_LIST));
                            }
                            addCaseWizard.getDialog().close();
                        }
                    });
                }
            });
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Button createRuleFilterButton() {
        Button btnRuleFilter = new Button();

        // add proper related Graphic..
        btnRuleFilter.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.LIST_ALT)); //FontAwesome.SORT_AMOUNT_ASC
        btnRuleFilter.setTooltip(new Tooltip(Lang.getRuleFilterButtonText()));
        btnRuleFilter.getStyleClass().add("cpx-icon-button");
        btnRuleFilter.setOnAction((ActionEvent event) -> {
//            SearchListResult searchList = getScene().getFilterManager().getSearchList();
//                List<FilterOption> listOfFilterOptions = searchList.getFilter("rules");
//            FilterOption filterOption = searchList.getFilter("rules", 0);
//            filterOption.getValue();

            addRuleFilterDialog();
        });
        return btnRuleFilter;
    }
//    private RuleFilterDialog ruleFilterDialog;

    private void addRuleFilterDialog() {
//    private String addRuleFilterDialog() {
        RuleFilterDialog ruleFilterDialog = new RuleFilterDialog(RuleFilterApplicationUsage.CaseList);
        if (isCaseListShown()) {
            ruleFilterDialog.restoreSelection(getCaseList().getAdditionalFilterValue(WorkingListAttributes.instance().getByKey(WorkingListAttributes.rules)));
        }
        if (isRuleListShown()) {
            ruleFilterDialog.restoreSelection(getRuleList().getAdditionalFilterValue(RuleListAttributes.instance().getByKey(RuleListAttributes.rules)));
        }
        ruleFilterDialog.showAndWait().ifPresent((ButtonType t) -> {
            if (ButtonType.OK.equals(t)) {
                List<CrgRules> listOfRules = ruleFilterDialog.getResults();
                if (listOfRules == null || listOfRules.isEmpty()) {
                    MainApp.showErrorMessageDialog("Bitte wählen Sie einige Regeln aus, um den Regelfilter anzuwenden....");
                } else {
                    String rulesFilterOption = createRulesFilterOption(listOfRules);

                    SearchListResult filter = getScene().getFilterManager().getSearchList();
                    if (getCaseList() != null) {
                        updateFilterInManager(getCaseList(), filter, true);
                    } else if (getRuleList() != null) {
                        updateFilterInManager(getRuleList(), filter, true);
                    } else {
                        reload();
                    }
                    MenuCache.getMenuCacheSearchLists().saveSearchList(filter, false);

//                    Set<FilterOption> listOfFilters = filter.getFilters();
//                    CpxClientConfig.instance().getSearchList(SearchListTypeEn.WORKING, "rules");
//                    return rulesFilterOption;
                }
            } else if (ButtonType.CANCEL.equals(t)) {
                // don't do anything...
            }
        });
    }

    private String createRulesFilterOption(List<CrgRules> listOfRules) {

        // all selected rule (IDs) with comma seperated values...
        String concatenatedRules = listOfRules.stream().map((t) -> {
            return t.getCrgrRid();
        }).collect(Collectors.joining(","));

        // SearchList (e.g: test2) with type of a list, the list of FilterOptions and list of ColumnOptions.
        SearchListResult searchList = getScene().getFilterManager().getSearchList();

//        Set<FilterOption> filters = searchList.getFilters();
        FilterOption filterOption = new FilterOption("rules", "rules", concatenatedRules);

//        FilterOption option = searchList.getFilter("rules", 0);
        /*       if (option != null) {
            option.setValue(rules);
        } else {
            option = new FilterOption("testRule", "testRule", rules);    // (name, field, value)
        }
         */
//        searchList.removeFilter("rules");
        searchList.addFilter(filterOption);
        // update the tableview (load from the server)
//        reload();
        return filterOption.getValue();
    }

//
//    public void openCase() {
//        openCase(getSelectedItem().getId());
//    }
    public void copyCaseNumber() {
        List<WorkingListItemDTO> itemList = getSelectedItemsDistinct();
        if (itemList.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (WorkingListItemDTO dto : itemList) {
            if (dto.getCaseNumber() == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(dto.getCaseNumber());
        }
        ClipboardEnabler.copyToClipboard(null, sb.toString(), itemList.size() == 1);
    }

    public void openCase() {
        final Long caseId = getSelectedId();
        if (caseId == null) {
            return;
        }
        DataActionEvent<Long> dataEvent = new DataActionEvent<>(caseId, ListType.WORKING_LIST);
        Events.instance().setNewEvent(dataEvent);
    }

    public void cancelCase() {
        WorkingListItemDTO item = getSelectedItem();
        boolean isCanceled = getSelectedItem() != null ? WorkingListFXMLController.this.getScene().isItemCanceled(getSelectedItem().getId()) : false;
        if (item == null) {
            LOG.log(Level.INFO, "item is null!");
            return;
        }
        final long caseId = item.getId();
        final String caseNumber = item.getCsCaseNumber();
        LOG.log(Level.INFO, "Cancel hospital case " + caseNumber + " with id: " + caseId);
        String meassge = Lang.getWorkingListContextMenuCaseCancelConfirm(caseNumber);
        if (isCanceled) {
            meassge = Lang.getWorkingListContextMenuCaseUnCancelConfirm(caseNumber);
        }
        ConfirmDialog confirm = new ConfirmDialog(MainApp.getWindow(), meassge);
        confirm.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (t.equals(ButtonType.YES)) {

                    MainApp.execWithLockDialog(new LockCallback<Object, Object>() {
                        @Override
                        public Object call(Object param) throws LockException {
                            WorkingListFXMLController.this.getScene().checkLocked(caseId);
                            if (!item.isIsCancel()) {
                                try {
                                    WorkingListFXMLController.this.getScene().cancelItem(caseId);
                                    LOG.log(Level.INFO, "Cancel case " + caseNumber + " with id " + caseId + " was successful");
                                    updateTvInfos();
                                    MainApp.showInfoMessageDialog(Lang.getWorkingListContextMenuCaseCancelSuccess(caseNumber), getScene().getWindow());
                                } catch (CpxIllegalArgumentException ex) {
                                    LOG.log(Level.INFO, "Cancel case " + caseNumber + " with id " + caseId + " failed", ex);
                                    MainApp.showErrorMessageDialog(Lang.getWorkingListContextMenuCaseCancelError(caseNumber) + "\n" + ex.getMessage(), getScene().getWindow());
                                }
                            } else {
                                try {
                                    CaseDetailsCancelReasonEn cancelReason =  WorkingListFXMLController.this.getScene().getCancelReason4Case(caseId);
                                    if( cancelReason == null){
                                        MainApp.showInfoMessageDialog("Der Fall ist nicht storniert, bitte Arbeitsliste aktualisieren!", getScene().getWindow());
                                        
                                    }else if(cancelReason.equals(CaseDetailsCancelReasonEn.MANUAL)){

                                        WorkingListFXMLController.this.getScene().unCancelItem(caseId);
                                        LOG.log(Level.INFO, "uncancel case " + caseNumber + " with id " + caseId + " was successful");
                                        updateTvInfos();
                                        MainApp.showInfoMessageDialog(Lang.getWorkingListContextMenuCaseUnCancelSuccess(caseNumber), getScene().getWindow());
                                    }else
                                        if(cancelReason.equals(CaseDetailsCancelReasonEn.MERGE)){
                                            String mergedCaseNumbers =  WorkingListFXMLController.this.getScene().getCaseNumbers4CancelledCaseByMerge(caseId);
                                            String messageMerge = "Der Fall " + caseNumber + " wurde bei Fallzusammenführung der Fälle " + mergedCaseNumbers +
                                                   " storniert.\n Wollen Sie die Fallzusammenführung wirklich aufheben?";
                                                    ConfirmDialog confirmUnMerge = new ConfirmDialog(MainApp.getWindow(), messageMerge);
                                                    confirmUnMerge.showAndWait().ifPresent(new Consumer<ButtonType>() {
                                                        @Override
                                                        public void accept(ButtonType t) {
                                                            if (t.equals(ButtonType.YES)) {
                                                                WorkingListFXMLController.this.getScene().unmergeCases4CancelledCase(caseId);
                                                                LOG.log(Level.INFO, "uncancel cases " + mergedCaseNumbers + " with for " + caseNumber + " was successful");
                                                                updateTvInfos();
                                                                MainApp.showInfoMessageDialog("Die Fallzusammenführung für die Fälle " + mergedCaseNumbers +
                                                                        " wurde erfolgreich zurückgenommen", getScene().getWindow());

                                                            }
                                                        }
                                                    });
                                            
                                        }
                                    else{
                                             MainApp.showInfoMessageDialog("Der Fall " + caseNumber + " wurde in KIS storniert", getScene().getWindow());
                                          
                                    }
                                } catch ( RuntimeException ex) {
                                    LOG.log(Level.INFO, "Cancel case " + caseNumber + " with id " + caseId + " failed", ex);
                                    MainApp.showErrorMessageDialog(Lang.getWorkingListContextMenuCaseUnCancelError(caseNumber) + "\n", getScene().getWindow());
                                }
                                
                            }
                            reload();
                            return null;
                        }
                    });
                }
            }
        });

    }

    public void deleteCase() {
        WorkingListItemDTO item = getSelectedItem();
        if (item == null) {
            LOG.log(Level.INFO, "item is null!");
            return;
        }
        final long caseId = item.getId();
        final String caseNumber = item.getCsCaseNumber();
        LOG.log(Level.INFO, "Delete hospital case " + caseNumber + " with id: " + caseId);
        List<TWmProcess> caseProcesses = ((WorkingListScene) WorkingListFXMLController.this.getScene()).getProcessesOfCase(caseId);

        StringBuilder sb = new StringBuilder();
        for (TWmProcess process : caseProcesses) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(process.getWorkflowNumber());
        }
        final String processNumbers;
        if (sb.length() > 0) {
            processNumbers = Lang.getWorkingListContextMenuCaseDeleteProcesses(sb.toString());
        } else {
            processNumbers = Lang.getWorkingListContextMenuCaseDeleteNoProcesses();
        }

        ConfirmDialog confirm = new ConfirmDialog(MainApp.getWindow(), Lang.getWorkingListContextMenuCaseDeleteConfirm(caseNumber) + "\n\n" + processNumbers);
        confirm.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (t.equals(ButtonType.YES)) {
                    //Yep, let this hospital case get out of my eyes!
                    MainApp.execWithLockDialog((Object param) -> {
                        WorkingListFXMLController.this.getScene().checkLocked(caseId);
                        try {
                            //close opened cases first (has only an effect for the current user on this client)
                            //MainApp.getToolbarMenuScene().getController().closeCase(caseId);
                            WorkingListFXMLController.this.getScene().deleteItem(caseId);
                            LOG.log(Level.INFO, "Deleting case " + caseNumber + " with id " + caseId + " was successful");
                            final Set<WorkingListItemDTO> itemsToRemove = new HashSet<>();
                            itemsToRemove.add(item);
                            for (WorkingListItemDTO otherItem : new ArrayList<>(getTableView().getItems())) {
                                if (otherItem != null && otherItem.getId() == item.getId()) {
                                    itemsToRemove.add(otherItem);
                                }
                            }
                            getTableView().getItems().removeAll(itemsToRemove);
                            updateTvInfos();
                            MainApp.showInfoMessageDialog(Lang.getWorkingListContextMenuCaseDeleteSuccess(caseNumber), getScene().getWindow());
                            //reload();
//                                    removeItem(item);
                        } catch (CpxIllegalArgumentException ex) {
                            LOG.log(Level.INFO, "Deleting case " + caseNumber + " with id " + caseId + " failed", ex);
                            MainApp.showErrorMessageDialog(ex);
                        }
                        return null;
                    });
                }
            }
        });
    }

//    public void createProcessDialog() {
//        createProcessDialog(getSelectedId());
//    }
    public void createProcessDialog() {
        final Long caseId = getSelectedId();
        if (caseId == null) {
            return;
        }
        final List<TWmProcess> processes = ((WorkingListScene) WorkingListFXMLController.this.getScene()).getProcessesOfCase(caseId);
        //TODO: FIXME, change dialog initialisation
        final ProcessEditingEvent openItemEvent = new ProcessEditingEvent(caseId, processes) {
            @Override
            public void handle(ActionEvent event) {
                //hide();
            }
        };
    }

//    public void stopP21Export() {
//        P21ExportDialog tmp = p21ExportDialog;
//        if (tmp != null) {
//            tmp.stop();
//        }
//    }
    @Override
    public boolean close() {
        boolean result = super.close();
//        stopP21Export();
        //stop
        return result;
    }

    public void checkGrouperModel() {
        if(cbGrouperModel != null){
              cbGrouperModel.getSelectionModel().select(CpxClientConfig.instance().getSelectedGrouper());
        }
        reload();
    }

}

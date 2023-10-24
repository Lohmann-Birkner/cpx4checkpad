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
package de.lb.cpx.client.app.menu.fx.filterlists.processes;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.fx.event.DataActionEvent;
import de.lb.cpx.client.app.menu.fx.event.Events;
import de.lb.cpx.client.app.menu.fx.table_master_detail.FilterListFXMLController;
import de.lb.cpx.client.app.menu.model.ListType;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddReminderDialog;
import de.lb.cpx.client.app.wm.fx.process.section.WmGeneralSection;
import de.lb.cpx.client.app.wm.fx.process.section.WmHistorySection;
import de.lb.cpx.client.app.wm.fx.process.section.WmReminderSection;
import static de.lb.cpx.client.core.BasicMainApp.getWindow;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.alert.ProgressWaitingDialog;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import static de.lb.cpx.model.enums.SearchListTypeEn.WORKFLOW;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessCase;
import de.lb.cpx.wm.model.TWmReminder;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javax.ejb.EJBException;
import org.controlsfx.control.Notifications;

/**
 * Controller class for WorkflowList Scene handles ui interactions TODO: FIX
 * Detail Node
 *
 * @author wilde
 */
public class WorkflowListFXMLController extends FilterListFXMLController<WorkflowListItemDTO> {

    private static final Logger LOG = Logger.getLogger(WorkflowListFXMLController.class.getName());

    @Override
    public String getItemsName() {
        return "Vorgänge";
    }

    @Override
    public int getNumberOfAllIds() {
        return Session.instance().getProcessCount();
    }

    @Override
    public int getNumberOfAllCanceledIds() {
        return Session.instance().getCanceledProcessCount();
    }

//    @Override
//    public FilterOption getFilterOptionCancel() {
//        List<FilterOption> result = getFilterOptions(WorkflowListAttributes.isCancel);
//        if (result == null || result.isEmpty()) {
//            return null;
//        }
//        if (result.size() > 1) {
//            LOG.log(Level.WARNING, "why there are multiple cancel filter options in workflow list (found {0})?! You should check this!", result.size());
//        }
//        return result.iterator().next();
//    }
    @Override
    public ColumnOption getColumnOptionCancel() {
        return getColumnOption(WorkflowListAttributes.isCancel);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
    }

    @Override
    public AsyncTableView<WorkflowListItemDTO> updateTableView() {
        switch (getScene().getFilterManager().getListType()) {
            case WORKFLOW:
                return new ProcessList(this);
            default:
                //TODO:throw exception?
                return null;
        }
    }

    public ProcessList getProcessList() {
        return (ProcessList) getTableView();
    }

//    public void openProcess() {
//        openProcess(getSelectedItem().getId());
//    }
    public void copyProcessNumber() {
        List<WorkflowListItemDTO> itemList = getSelectedItemsDistinct();
        if (itemList.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (WorkflowListItemDTO dto : itemList) {
            if (dto.getWorkflowNumber() == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(String.valueOf(dto.getWorkflowNumber()));
        }
        ClipboardEnabler.copyToClipboard(null, sb.toString(), itemList.size() == 1);
    }

    public void openProcess() {
        final Long processId = getSelectedId();
        if (processId == null) {
            return;
        }
        DataActionEvent<Long> dataEvent = new DataActionEvent<>(processId, ListType.WORKFLOW_LIST);
        Events.instance().setNewEvent(dataEvent);
    }

    private TWmReminder getNewReminder() {
        ProcessServiceFacade facade = new ProcessServiceFacade();

        AddReminderDialog dialog = new AddReminderDialog(facade);
        dialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        dialog.showAndWait();

        return dialog.onSave();
    }

    private void closeReminderByReminderIds(List<Long> reminderIds) {
        Session.instance().getEjbConnector().connectProcessServiceBean().get().closeRemindersByIds(reminderIds);
    }

    private void createReminderForAllProcess(List<Long> processIds, TWmReminder reminder) {
        Session.instance().getEjbConnector().connectProcessServiceBean().get().createReminderForAllProcesses(processIds, reminder);
    }

    public void closeAndCreateReminders(List<Long> processIds, List<Long> reminderIds, String pRemSubject) {
        ConfirmDialog confirm = new ConfirmDialog(MainApp.getWindow(), Lang.getWorkflowCloseAndCreateReminderConfirmation());
        confirm.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        confirm.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (t.equals(ButtonType.YES)) {
                    TWmReminder reminder = getNewReminder();
                    Service<Void> service = new Service<Void>() {
                        @Override
                        protected Task<Void> createTask() {
                            return new Task<Void>() {
                                @Override
                                protected Void call() {
                                    createReminderForAllProcess(processIds, reminder);
                                    closeReminderByReminderIds(reminderIds);
                                    return null;
                                }
                            };
                        }
                    };
                    ProgressWaitingDialog dialog = new ProgressWaitingDialog(service);
                    dialog.initOwner(getWindow());
                    dialog.setHeaderText(Lang.getReportDialogHeaderText());
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.setContentText(Lang.getWorkflowCloseAndCreateReminder());
                    service.start();
                    service.stateProperty().addListener(new ChangeListener<Worker.State>() {
                        @Override
                        public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                            if (null != newValue) {
                                switch (newValue) {
                                    case FAILED:
                                        MainApp.showErrorMessageDialog(Lang.getWorkflowCloseAndCreateReminder() + " ist fehlgeschlagen.");
                                        LOG.log(Level.SEVERE, null, "close and add new Remindes is failed");
                                        break;
                                    case CANCELLED:
                                        MainApp.showErrorMessageDialog("Funktion '" + Lang.getWorkflowCloseAndCreateReminder() + "' wird von dem Nutzer abgebrochen.");
                                        LOG.log(Level.SEVERE, null, "close and add new Remindes is cancelled");
                                        break;
                                    case SUCCEEDED:
                                        Notifications notification = NotificationsFactory.instance().createInformationNotification();
                                        notification.text(Lang.getWorkflowCloseAndCreateReminderCreateReminderSuccessfully(processIds.size(),
                                                MenuCache.instance().getReminderSubjectsForInternalId(reminder.getSubject()) != null
                                                ? MenuCache.instance().getReminderSubjectsForInternalId(reminder.getSubject()) : "") + "\n"
                                                + Lang.getWorkflowCloseAndCreateReminderCloseReminderSuccessfully(reminderIds.size(),
                                                        MenuCache.instance().getReminderSubjectsForInternalId(Long.valueOf(pRemSubject)) != null
                                                        ? MenuCache.instance().getReminderSubjectsForInternalId(Long.valueOf(pRemSubject)) : ""));
                                        notification.show();
                                        WorkflowListFXMLController.this.reload();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });

                }
                if (t.equals(ButtonType.NO)) {
                    closeReminderByReminderIds(reminderIds);
                    Notifications notification = NotificationsFactory.instance().createInformationNotification();
                    notification.text(Lang.getWorkflowCloseAndCreateReminderCloseReminderSuccessfully(reminderIds.size(),
                            MenuCache.instance().getReminderSubjectsForInternalId(Long.valueOf(pRemSubject)) != null ? MenuCache.instance().getReminderSubjectsForInternalId(Long.valueOf(pRemSubject)) : ""));
                    notification.showInformation();
                    WorkflowListFXMLController.this.reload();
                }
                if (t.equals(ButtonType.CANCEL)) {
                    Notifications notification = NotificationsFactory.instance().createInformationNotification();
                    notification.text("Fultion '" + Lang.getWorkflowCloseAndCreateReminder() + "' wird von dem Nutzer abgebrochen");
                    notification.showInformation();
                }
            }
        });

    }

    public void deleteProcess() {
        WorkflowListItemDTO item = getSelectedItem();
        if (item == null) {
            LOG.log(Level.INFO, "item is null!");
            return;
        }
        final long processId = item.getId();
        final String workflowNumber = String.valueOf(item.getWorkflowNumber());
        LOG.log(Level.INFO, "Delete process " + workflowNumber + " with id: " + processId);
        //                    EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
        List<TWmProcessCase> processCases = ((WorkflowListScene) WorkflowListFXMLController.this.getScene()).getProcessCases(processId);

        StringBuilder sb = new StringBuilder();
        for (TWmProcessCase processCase : processCases) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(processCase.getHosCase().getCsCaseNumber());
            if (processCase.getMainCase()) {
                sb.append(" (Basisfall)");
            }
        }
        final String caseNumbers;
        if (sb.length() > 0) {
            //caseNumbers = Lang.getWorkingListContextMenuCaseDeleteProcesses(sb.toString());
            caseNumbers = "Der Fall ist folgenden Fällen zugeordnet: " + sb.toString();
        } else {
            //caseNumbers = Lang.getWorkingListContextMenuCaseDeleteNoProcesses();
            caseNumbers = "Der Fall ist keinen Fällen zugeordnet";
        }

        ConfirmDialog confirm = new ConfirmDialog(MainApp.getWindow(), "Möchten Sie die Vorgangsnummer " + workflowNumber + " wirklich löschen?" + "\n\n" + caseNumbers);
        confirm.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (t.equals(ButtonType.YES)) {
                    //Yep, let this process get out of my eyes!
                    MainApp.execWithLockDialog((Object param) -> {
                        try {
                            WorkflowListFXMLController.this.getScene().checkLocked(processId);//lockServiceBean.get().checkProcessLock(processId);
                            //close opened cases first (has only an effect for the current user on this client)
                            WorkflowListFXMLController.this.getScene().deleteItem(processId);//processServiceBean.get().deleteProcess(processId);
                            LOG.log(Level.INFO, "Deleting process " + workflowNumber + " with id " + processId + " was successful");
                            final Set<WorkflowListItemDTO> itemsToRemove = new HashSet<>();
                            itemsToRemove.add(item);
                            for (WorkflowListItemDTO otherItem : new ArrayList<>(getTableView().getItems())) {
                                if (otherItem != null && otherItem.getId() == item.getId()) {
                                    itemsToRemove.add(otherItem);
                                }
                            }
                            getTableView().getItems().removeAll(itemsToRemove);
                            updateTvInfos();
                            MainApp.showInfoMessageDialog("Vorgang " + workflowNumber + " erfolgreich gelöscht!", getScene().getWindow());
                            //reload();
                            //                                    removeItem(item);
//                        } catch (CpxIllegalArgumentException ex) {
//                            LOG.log(Level.INFO, "Deleting process " + workflowNumber + " with id " + processId + " failed", ex);
//                            MainApp.showErrorMessageDialog(ex, "Vorgang " + workflowNumber + " konnte nicht gelöscht werden! Versuchen Sie es erneut oder prüfen Sie den Fehler im Serverprotokoll.");
//                            , "Vorgang " + workflowNumber + " konnte nicht gelöscht werden! Versuchen Sie es erneut oder prüfen Sie den Fehler im Serverprotokoll.");
                        } catch (EJBException ex) {
                            if(ex.getCause() instanceof CpxIllegalArgumentException){
                                LOG.log(Level.INFO, "Deleting process " + workflowNumber + " with id " + processId + " failed", ex.getCause());
                                MainApp.showInfoMessageDialog("Vorgangsnummer "+processId +" konnte nicht gelöscht werden\n" + ex.getCause().getMessage());
                            }else{
                                throw new EJBException(ex.getCausedByException());
                            }
                        }
                        return null;
                    });
                }
            }
        });
    }

    @Override
    public Parent getDetailContent(WorkflowListItemDTO item) {
        if (item == null) {
            return null;
        }
        long start = System.currentTimeMillis();
        HBox detailContent = new HBox();
        if(!getScene().checkExists(item.getId())){
            detailContent.getChildren().add(new Label(Lang.getCaseDoesNotExistWithReason(String.valueOf(item.getId()))));
            detailContent.setAlignment(Pos.CENTER);
            LOG.log(Level.WARNING, "Process for id: {0} does not exists in database!", item.getId());
            return detailContent;
        }
        LOG.log(Level.FINER, "check if process exists in {0} ms", System.currentTimeMillis() - start);
        ProcessServiceFacade facade = new ProcessServiceFacade(item.getId(), false);
        LOG.log(Level.FINER, "setUp facade in {0} ms", System.currentTimeMillis() - start);
        final int limit = 10;
        WmGeneralSection generalSection = new WmGeneralSection(facade);
        generalSection.setArmed(false);
        final boolean finishedFl = false;
        WmReminderSection reminderSection = new WmReminderSection(facade, finishedFl);
        reminderSection.setArmed(false);
        WmHistorySection historySection = new WmHistorySection(facade, limit, false);
        historySection.setArmed(false);

        HBox historyContent = new HBox(historySection.getRoot());
        VBox overviewContent = new VBox(generalSection.getRoot(), reminderSection.getRoot() /*, patientSection.getRoot(), serviceSection.getRoot(), documentSection.getRoot() */);
        detailContent.getChildren().addAll(historyContent, overviewContent);
        ScrollPane content = new ScrollPane(new AnchorPane(detailContent));
        CpxFXMLLoader.setAnchorsInNode(detailContent);
        historyContent.prefWidthProperty().bind(content.widthProperty().divide(2));
        overviewContent.prefWidthProperty().bind(content.widthProperty().divide(2));
        content.setFitToWidth(true);
        LOG.finer("setUp all in " + (System.currentTimeMillis() - start) + " ms");
        return content;
    }

}

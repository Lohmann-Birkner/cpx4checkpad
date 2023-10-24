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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filterlists.processes;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.p21_export.ListExportDialog;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.alert.SendMail;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleCaseLink;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleProcessLink;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.WorkingListStatelessEJBRemote;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.WindowEvent;
import javax.ejb.EJBException;
import javax.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class ProcessContextMenu extends ContextMenu {

    private static final Logger LOG = Logger.getLogger(ProcessContextMenu.class.getName());
    private final WorkflowListFXMLController workflowList;

    public ProcessContextMenu(WorkflowListFXMLController pWorkflowList) {
        super();
        workflowList = pWorkflowList;

        MenuItem unlockProcess = getUnlockItem();
        MenuItem openProcess = getOpenProcessItem();
        MenuItem closeAndCreateReminders = getCloseAndCreateReminderItem();
        MenuItem copyCaseNumber = getCopyCaseNumberItem();
        MenuItem forwardProcessLink = getForwardProcessLinksItem();
        MenuItem copyProcessNumber = getCopyProcessNumberItem();
        MenuItem cancelProcess = getCancelItem();
        MenuItem deleteProcess = getDeleteProcessItem();
        MenuItem exportExcel = getExportExcelItem();
        MenuItem exportCsv = getExportCsvItem();
//            MenuItem unlockAllProcesses = getUnlockAllItems();
        MenuItem exportXml = getExportXmlItem();

        add(unlockProcess);
        add(openProcess);
        add(closeAndCreateReminders);
        add(copyProcessNumber);
        add(copyCaseNumber);
        add(forwardProcessLink);
        add(cancelProcess);
        add(deleteProcess);
//            add(unlockAllProcesses);
        add(exportExcel);
        add(exportCsv);
        add(exportXml);
        
        //Disable or enable (ir-)relevant menu items on showing context menu
        setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (workflowList.getSelectedItem() == null) {
                    return;
                }
                unlockProcess.setDisable(false);
                int selectedItemsSize = workflowList.getSelectedItems().size();

                if (selectedItemsSize == 1) {
                    WorkflowListItemDTO item = workflowList.getSelectedItem();
                    boolean isLocked = workflowList.getScene().isItemLocked(item.getId());//lockServiceBean.get().isCaseLocked(item.getId());
                    unlockProcess.setDisable(!isLocked);
                }
                openProcess.setDisable(selectedItemsSize != 1);
                //copyProcessNumber.setDisable(item.getWorkflowNumber() == null || selectedItemsSize != 1);
                //copyCaseNumber.setDisable(item.getCaseNumber() == null || selectedItemsSize != 1);
                deleteProcess.setDisable(selectedItemsSize != 1);
                cancelProcess.setText(workflowList.getSelectedItem() != null && !workflowList.getScene().isItemCanceled(workflowList.getSelectedItem().getId()) ? Lang.getWorkflowContextMenuCancel() : Lang.getWorkflowContextMenuUncancel());
                exportExcel.setText(selectedItemsSize == 1 ? "Tabelle als XLS exportieren" : "Markierte Einträge als XLS exportieren");
                exportCsv.setText(selectedItemsSize == 1 ? "Tabelle als TXT (CSV) exportieren" : "Markierte Einträge als TXT (CSV) exportieren");
                exportXml.setText(selectedItemsSize == 1 ? "Tabelle als XML (XML) exportieren" : "Markierte Einträge als XML (XML) exportieren");
            }
        });

    }

    public final boolean add(MenuItem pItem) {
        return getItems().add(pItem);
    }

    private MenuItem getOpenProcessItem() {
        MenuItem openProcess = new MenuItem("Vorgang öffnen");
        openProcess.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER_OPEN));
        openProcess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                workflowList.openProcess();
                //                getOnOpenItem().handle(dataEvent);
            }
        });
        return openProcess;
    }

    private MenuItem getCloseAndCreateReminderItem() {
        MenuItem closeAndCreateReminders = new MenuItem(Lang.getWorkflowCloseAndCreateReminder());
        closeAndCreateReminders.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.USER_TIMES));
        closeAndCreateReminders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stream<ColumnOption> columnOptions = workflowList.getSortedColumnOptions();
                columnOptions.forEach(
                        (pColumnOption) -> {
                            if (pColumnOption.getDisplayName().equals(Lang.getReminderSubject())) {
                                List<FilterOption> fopts = workflowList.getFilterOptions(pColumnOption);
                                fopts.forEach((fopt) -> {
                                    if (fopt.getValue().isEmpty()) {
                                        MainApp.showErrorMessageDialog(Lang.getWorkflowCloseAndCreateReminderNoFilter());
                                    } else if (fopt.getValue().contains(",")) {
                                        MainApp.showErrorMessageDialog(Lang.getWorkflowCloseAndCreateReminderNotUnique());
                                    } else {
                                        List<Long> workflowIds = new ArrayList<>();
                                        List<Long> reminderIds = new ArrayList<>();
                                        workflowList.getTableView().getItems().forEach((item) -> {
                                            if (!workflowIds.contains(item.getId())) {
                                                workflowIds.add(item.getId());
                                            }
                                            reminderIds.add(item.getReminderId());
                                        });
                                        workflowList.closeAndCreateReminders(workflowIds, reminderIds, fopt.getValue());
                                    }
                                });
                            }
                        });
            }

        });
        return closeAndCreateReminders;
    }

    private MenuItem getCopyProcessNumberItem() {
        MenuItem copyProcessNumber = new MenuItem("Vorgangsnummer kopieren");
        copyProcessNumber.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLIPBOARD));
        copyProcessNumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                workflowList.copyProcessNumber();
            }
        });
        return copyProcessNumber;
    }

    private MenuItem getCopyCaseNumberItem() {
        MenuItem copyCaseNumber = new MenuItem("Fallnummer kopieren");
        copyCaseNumber.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLIPBOARD));
        copyCaseNumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<WorkflowListItemDTO> itemList = workflowList.getSelectedItemsDistinct();
                if (itemList.isEmpty()) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (WorkflowListItemDTO dto : itemList) {
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
        });
        return copyCaseNumber;
    }

    private MenuItem getForwardProcessLinksItem() {
        MenuItem copyCaseNumber = new MenuItem("Vorgang weiterleiten");
        copyCaseNumber.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
        copyCaseNumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<WorkflowListItemDTO> itemList = workflowList.getSelectedItemsDistinct();
                final String database = Session.instance().getCpxDatabase();
                int nr = 0;

                final StringBuilder body = new StringBuilder();
                body.append("<style>td, th { border: 1px solid black; } table { border-collapse: collapse; } thead td { font-weight: bold; background-color: #c7ccc9; } td { padding: 5px; }</style>");
                body.append("Datenbank: <b>" + database + "</b><br><br>");
                body.append("<table>");
                body.append("<thead>");
                body.append("<tr>");
                body.append("<td>Nr.</td><td>Vorgang</td><td>Basisfall</td><td>Patient</td><td style='width: 200px;'>Bemerkung</td>");
                body.append("</tr>");
                body.append("</thead>");
                body.append("<tbody>");

                for (WorkflowListItemDTO dto : itemList) {
                    nr++;
                    final String patientNumber = StringUtils.trimToEmpty(dto.getPatNumber());
                    final CpxHandleLink caseLink = new CpxHandleCaseLink(dto.getHospitalIdent(), dto.getCaseNumber(), database);
                    final CpxHandleLink processLink = new CpxHandleProcessLink(dto.getWorkflowNumber(), database);

                    body.append("<tr>");
                    body.append("<td>" + nr + "</td>");
                    body.append("<td>" + processLink.getLink() + "</td>");
                    body.append("<td>" + caseLink.getLink() + "</td>");
                    body.append("<td>" + (patientNumber.isEmpty() ? StringUtils.trimToEmpty(dto.getPatName()) : patientNumber) + "</td>");
                    body.append("<td></td>");
                    body.append("</tr>");
                }
                body.append("</tbody>");
                body.append("</table>");
                SendMail sendMail = new SendMail();
                final String receiverMail = "";
                final String subject = "Ausgewählte CPX-Vorgänge";
                final boolean html = true;
                try {
                    sendMail.openDraft(receiverMail, subject, body.toString(), html);
                    //ClipboardEnabler.copyHtmlToClipboard(null, link, false);
                } catch (MessagingException | IOException ex) {
                    LOG.log(Level.SEVERE, "Unable to create mail draft", ex);
                    MainApp.showErrorMessageDialog(ex, "Es konnte kein E-Mail-Entwurf erstellt werden");
                }
            }
        });
        return copyCaseNumber;
    }

    private MenuItem getDeleteProcessItem() {
        MenuItem deleteProcess = new MenuItem(Lang.getCaseDeleteProcess());
        deleteProcess.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
        deleteProcess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                workflowList.deleteProcess();
            }
        });
        return deleteProcess;
    }

//        private MenuItem getUnlockAllItems() {
//            MenuItem unlockAll = new MenuItem("Alle Vorgänge freischalten");
//            unlockAll.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNLOCK));
//            unlockAll.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    int numberOfUnlockedItems = workflowList.getScene().unlockAllItems();
//                    MainApp.showInfoMessageDialog("Es wurden " + numberOfUnlockedItems + " Vorgänge entsperrt", MainApp.getWindow());
//                }
//            });
//            return unlockAll;
//        }
    private MenuItem getUnlockItem() {
        MenuItem unlockProcess = new MenuItem("Vorgang entsperren");
        unlockProcess.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNLOCK));
        unlockProcess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long[] selectedIds = workflowList.getSelectedIdsDistinct();

                if (selectedIds.length == 0) {
                    return;
                }

                if (selectedIds.length == 1) {
                    WorkflowListItemDTO dto = workflowList.getSelectedItem();
                    if (dto == null) {
                        return;
                    }
                    final String workflowNumber = String.valueOf(dto.getWorkflowNumber());
                    LOG.log(Level.INFO, "try to unlock process with number {0}", workflowNumber);

                    if (workflowList.getScene().unlockItem(dto.getId())) {
                        MainApp.showInfoMessageDialog("Vorgang " + workflowNumber + " erfolgreich entsperrt!", MainApp.getWindow());
                    } else {
                        MainApp.showErrorMessageDialog("Vorgang " + workflowNumber + " konnte nicht entsperrt werden!", MainApp.getWindow());
                    }

                    return;
                }

                //unlock multiple entries 
                LOG.log(Level.INFO, "try to unlock " + selectedIds.length + " selected processes");
                int numberOfUnlockedItems;

                numberOfUnlockedItems = workflowList.getScene().unlockItems(selectedIds);
                MainApp.showInfoMessageDialog("Es wurden " + numberOfUnlockedItems + " Vorgänge entsperrt", MainApp.getWindow());

            }
        });
        return unlockProcess;
    }

    private MenuItem getCancelItem() {
        MenuItem cancelProcess = new MenuItem("Vorgang stornieren");
        cancelProcess.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.BAN));
        cancelProcess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long[] selectedIds = workflowList.getSelectedIdsDistinct();

                if (selectedIds.length == 0) {
                    return;
                }

                if (selectedIds.length == 1) {
                    WorkflowListItemDTO dto = workflowList.getSelectedItem();
                    if (dto == null) {
                        return;
                    }
                    final String workflowNumber = String.valueOf(dto.getWorkflowNumber());
                    LOG.log(Level.INFO, "try to cancel process with number {0}", workflowNumber);
                    boolean isCanceled = workflowList.getSelectedItem() != null && workflowList.getScene().isItemCanceled(workflowList.getSelectedItem().getId());

                    String meassge = Lang.getWorkflowContextMenuWorkflowCancelConfirm(workflowNumber);
                    if (isCanceled) {
                        meassge = Lang.getWorkflowContextMenuWorkflowUnCancelConfirm(workflowNumber);
                    }
                    final long processId = dto.getId();
                    ConfirmDialog confirm = new ConfirmDialog(MainApp.getWindow(), meassge);
                    confirm.showAndWait().ifPresent((ButtonType t) -> {
                        if (t.equals(ButtonType.YES)) {

                            MainApp.execWithLockDialog((Object param) -> {
                                workflowList.getScene().checkLocked(processId);
                                if (!isCanceled) {
                                    try {
                                        workflowList.getScene().cancelItem(processId);
                                        LOG.log(Level.INFO, "Cancel workflow {0} with id {1} was successful", new Object[]{workflowNumber, processId});
                                        workflowList.updateTvInfos();
                                        MainApp.showInfoMessageDialog(Lang.getWorkflowContextMenuWorkflowCancelSuccess(workflowNumber), MainApp.getWindow());
                                        //only reload if successful
                                        workflowList.reload();
                                    } catch (EJBException ex) {
                                        if (ex.getCause() instanceof CpxIllegalArgumentException) {
                                            LOG.log(Level.INFO, "Cancel case " + workflowNumber + " with id " + processId + " failed", ex.getCause());
                                            MainApp.showInfoMessageDialog(Lang.getWorkflowContextMenuWorkflowCancelError(workflowNumber) + "\n" + ex.getCause().getMessage());
                                        } else {
                                            throw new EJBException(ex.getCausedByException());
                                        }
                                    }
                                }else{
                                    try{
                                        workflowList.getScene().unCancelItem(processId);
                                        LOG.log(Level.INFO, "UnCancel process {0} with id {1} was successful", new Object[]{workflowNumber, processId});
                                        workflowList.updateTvInfos();
                                        MainApp.showInfoMessageDialog(Lang.getWorkflowContextMenuWorkflowUnCancelSuccess(workflowNumber), MainApp.getWindow());
                                        //only reload if successful
                                        workflowList.reload();
                                    } catch (EJBException ex) {
                                        if (ex.getCause() instanceof CpxIllegalArgumentException) {
                                            LOG.log(Level.INFO, "UnCancel process " + workflowNumber + " with id " + processId + " failed", ex.getCause());
                                            MainApp.showInfoMessageDialog(Lang.getWorkflowContextMenuWorkflowUnCancelError(workflowNumber) + "\n" + ex.getCause().getMessage());
                                        } else {
                                            throw new EJBException(ex.getCausedByException());
                                        }
                                    }
                                }
                                return null;
                            });
                        }
                    });

                    return;
                }

                //unlock multiple entries 
                LOG.log(Level.INFO, "try to unlock {0} selected processes", selectedIds.length);
                int numberOfUnlockedItems;

                numberOfUnlockedItems = workflowList.getScene().unlockItems(selectedIds);
                MainApp.showInfoMessageDialog("Es wurden " + numberOfUnlockedItems + " Vorgänge entsperrt", getScene().getWindow());

            }
        });
        return cancelProcess;
    }

    private MenuItem getExportExcelItem() {
        MenuItem menuItemExportExcel = new MenuItem("Tabelle als XLS exportieren");
        menuItemExportExcel.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FILE_EXCEL_ALT));

        menuItemExportExcel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!Session.instance().isExportDataAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));
                } else {
                    WorkingListStatelessEJBRemote workingListServiceBean = Session.instance().getEjbConnector().connectWorkingListBean().get();
                    final ExportTypeEn exportType = ExportTypeEn.EXCEL;
                    final SearchListTypeEn listType = SearchListTypeEn.WORKFLOW;
                    ListExportDialog listExportDialog = new ListExportDialog() {

                        @Override
                        public long prepareExport() {
                            GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
                            boolean isLocal = Session.instance().isCaseLocal();
                            boolean isShowAllReminders = Session.instance().isShowAllRemindersConfig();
                            final long[] selectedIds = workflowList.getSelectedIdsDistinct(2);
                            return workingListServiceBean.prepareListExport(isLocal, isShowAllReminders, grouperModel, listType, exportType, selectedIds, new HashMap<>(workflowList.getScene().getFilterManager().getFilterOptionMap()));
                        }

                        @Override
                        public boolean stopExport(long pExecutionId) {
                            return workingListServiceBean.stopExport2(pExecutionId);
                        }
                    };
                    listExportDialog.start(listType, exportType); //workflowList.getProcessList()
                }
            }
        });
        return menuItemExportExcel;
    }

    private MenuItem getExportCsvItem() {
        MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
        menuItemExportCsv.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TABLE));
        menuItemExportCsv.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!Session.instance().isExportDataAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));
                } else {
                    WorkingListStatelessEJBRemote workingListServiceBean = Session.instance().getEjbConnector().connectWorkingListBean().get();
                    final ExportTypeEn exportType = ExportTypeEn.CSV;
                    final SearchListTypeEn listType = SearchListTypeEn.WORKFLOW;
                    ListExportDialog listExportDialog = new ListExportDialog() {

                        @Override
                        public long prepareExport() {
                            GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
                            boolean isLocal = Session.instance().isCaseLocal();
                            boolean isShowAllReminders = Session.instance().isShowAllRemindersConfig();
                            final long[] selectedIds = workflowList.getSelectedIdsDistinct(2);
                            return workingListServiceBean.prepareListExport(isLocal, isShowAllReminders, grouperModel, listType, exportType, selectedIds, new HashMap<>(workflowList.getScene().getFilterManager().getFilterOptionMap()));
                        }

                        @Override
                        public boolean stopExport(long pExecutionId) {
                            return workingListServiceBean.stopExport2(pExecutionId);
                        }
                    };
                    listExportDialog.start(listType, exportType); //workflowList.getProcessList()
                }
            }
        });
        return menuItemExportCsv;
    }

    private MenuItem getExportXmlItem(){
        MenuItem menuItemExportXml = new MenuItem("Tabelle als TXT (Xml) in Xml - Format exportieren");
        menuItemExportXml.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TABLE));
        menuItemExportXml.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!Session.instance().isExportDataAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));
                } else {
                    WorkingListStatelessEJBRemote workingListServiceBean = Session.instance().getEjbConnector().connectWorkingListBean().get();
                    final ExportTypeEn exportType = ExportTypeEn.XML;
                    final SearchListTypeEn listType = SearchListTypeEn.WORKFLOW;
                    
                    ListExportDialog listExportDialog = new ListExportDialog() {

                        @Override
                        public long prepareExport() {
                            GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
                            boolean isLocal = Session.instance().isCaseLocal();
                            boolean isShowAllReminders = Session.instance().isShowAllRemindersConfig();
                            final long[] selectedIds = workflowList.getSelectedIdsDistinct(2);
                            return workingListServiceBean.prepareListExport(isLocal, isShowAllReminders, grouperModel, listType, exportType, selectedIds, new HashMap<>(workflowList.getScene().getFilterManager().getFilterOptionMap()));
                        }

                        @Override
                        public boolean stopExport(long pExecutionId) {
                            return workingListServiceBean.stopExport2(pExecutionId);
                        }
                    };
                    listExportDialog.start(listType, exportType); //workingList.getCaseList()
                }
            }
        });
        return menuItemExportXml;
       
    }
}

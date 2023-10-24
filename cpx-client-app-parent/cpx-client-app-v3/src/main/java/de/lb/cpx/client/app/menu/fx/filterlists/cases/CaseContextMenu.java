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
import de.lb.cpx.client.app.job.fx.JobManagerScene;
import de.lb.cpx.client.app.p21_export.ListExportDialog;
import de.lb.cpx.client.app.p21_export.P21ExportDialog;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.SendMail;
import de.lb.cpx.client.core.model.task.CopyCaseToCommonTask;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleCaseLink;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.exceptions.CpxSapException;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.WorkingListStatelessEJBRemote;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.WindowEvent;
import javax.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class CaseContextMenu extends ContextMenu {

//        private CpxLicenseUsage cpxLicenseUsage;
    private static final Logger LOG = Logger.getLogger(CaseContextMenu.class.getName());
    private final License license = Session.instance().getLicense();
    private final WorkingListFXMLController workingList;

    public CaseContextMenu(WorkingListFXMLController pWorkingList) {
        super();
        workingList = pWorkingList;
//            cpxLicenseUsage = new CpxLicenseUsage();

        MenuItem unlockCase = getUnlockItem();
        MenuItem openCase = getOpenCaseItem();
        MenuItem copyCaseNumber = getCopyCaseNumberItem();
        MenuItem forwardCaseLinks = getForwardCaseLinksItem();
        MenuItem deleteCase = getDeleteCaseItem();
        MenuItem cancelCase = getCancelCaseItem();
        MenuItem batchgrouping = getBatchgroupingItem();
        MenuItem editProcess = getEditProcessItem();
        MenuItem exportExcel = getExportExcelItem();
        MenuItem exportCsv = getExportCsvItem();
        MenuItem exportXml = getExportXmlItem();
        MenuItem exportP21 = getExportP21Item();
        MenuItem updateCaseSap = getUpdateCaseSapItem();
        MenuItem copyToCommon = getCopyToCommonItem();
//            MenuItem unlockAllCases = getUnlockAllItems();

        add(unlockCase);
        add(openCase);
        add(copyCaseNumber);
        add(forwardCaseLinks);
        add(deleteCase);
        add(cancelCase);
        add(batchgrouping);
        if (license.isFmModule()) {
            add(editProcess);
        }
//            add(unlockAllCases);
        add(exportExcel);
        add(exportCsv);
        add(exportXml);
        add(exportP21);
        add(updateCaseSap);
        if (Session.instance().getRoleProperties().isEditRuleAllowed()) {
            add(copyToCommon);
        }
        //Disable or enable (ir-)relevant menu items on showing context menu
        setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (workingList.getSelectedItem() == null) {
                    return;
                }
                unlockCase.setDisable(false);
                cancelCase.setDisable(false);
                int selectedItemsSize = workingList.getSelectedItems().size();

                if (selectedItemsSize == 1) {
                    WorkingListItemDTO item = workingList.getSelectedItem();
                    boolean isLocked = workingList.getScene().isItemLocked(item.getId());//lockServiceBean.get().isCaseLocked(item.getId());
                    unlockCase.setDisable(!isLocked);
                }

                openCase.setDisable(selectedItemsSize != 1);
                //copyCaseNumber.setDisable(selectedItemsSize != 1);
                deleteCase.setDisable(selectedItemsSize != 1);
                editProcess.setDisable(selectedItemsSize != 1);
                cancelCase.setDisable(selectedItemsSize != 1);
                cancelCase.setText(workingList.getSelectedItem() != null && !workingList.getSelectedItem().isIsCancel() ? Lang.getWorkingListContextMenuCancel() : Lang.getWorkingListContextMenuUncancel());
                exportExcel.setText(selectedItemsSize == 1 ? "Tabelle als XLS exportieren" : "Markierte Einträge als XLS exportieren");
                exportCsv.setText(selectedItemsSize == 1 ? "Tabelle als TXT (CSV) exportieren" : "Markierte Einträge als TXT (CSV) exportieren");
                exportXml.setText(selectedItemsSize == 1 ? "Tabelle als XML (XML) exportieren" : "Markierte Einträge als XML (XML) exportieren");

                updateCaseSap.setVisible(Session.instance().getEjbConnector().connectConfigurationServiceBean().get().hasActiveSapJobs());
            }
        });
    }

    public final boolean add(MenuItem pItem) {
        return getItems().add(pItem);
    }

    public final MenuItem createMenuItem(String pTitle) {
        return new MenuItem(pTitle);
    }

//        private MenuItem getUnlockAllItems() {
//            MenuItem unlockAll = createMenuItem("Alle Fälle freischalten");
//            unlockAll.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNLOCK));
//            unlockAll.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    int numberOfUnlockedItems = workingList.unlockAllItems();
//                    MainApp.showInfoMessageDialog("Es wurden " + numberOfUnlockedItems + " Fälle entsperrt", MainApp.getWindow());
//                }
//            });
//            return unlockAll;
//        }
    private MenuItem getUnlockItem() {
        MenuItem unlockCase = createMenuItem(Lang.getWorkingListContextMenuUnlock());
        unlockCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNLOCK));
        unlockCase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long[] selectedIds = workingList.getSelectedIdsDistinct();

                if (selectedIds.length == 0) {
                    return;
                }

                if (selectedIds.length == 1) {
                    WorkingListItemDTO dto = workingList.getSelectedItem();
                    if (dto == null) {
                        return;
                    }
                    LOG.log(Level.INFO, "try to unlock case with number {0}", dto.getCsCaseNumber());
//                    if(!Session.instance().getRoleProperties().isCanUnlockAllowed())
//                        MainApp.showErrorMessageDialog("Sie haben Keine Rechte eien Fall entzusperren");

                    if (workingList.getScene().unlockItem(dto.getId())) {
                        MainApp.showInfoMessageDialog(Lang.getWorkingListContextMenuUnlockSuccess(dto.getCsCaseNumber(), MainApp.getWindow()));
                    } else {
                        MainApp.showErrorMessageDialog(Lang.getWorkingListContextMenuUnlockError(dto.getCsCaseNumber(), MainApp.getWindow()));
                    }

                    return;
                }

                //unlock multiple entries 
                LOG.log(Level.INFO, "try to unlock " + selectedIds.length + " selected cases");
                int numberOfUnlockedItems;

                numberOfUnlockedItems = workingList.getScene().unlockItems(selectedIds);
                MainApp.showInfoMessageDialog("Es wurden " + numberOfUnlockedItems + " Fälle entsperrt", MainApp.getWindow());

            }
        });
        return unlockCase;
    }

    private MenuItem getOpenCaseItem() {
        MenuItem openCase = createMenuItem(Lang.getWorkingListContextMenuOpen());
        openCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER_OPEN));
        openCase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                workingList.openCase();
            }
        });
        return openCase;
    }

    private MenuItem getCopyCaseNumberItem() {
        MenuItem copyCaseNumber = new MenuItem("Fallnummer kopieren");
        copyCaseNumber.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLIPBOARD));
        copyCaseNumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                workingList.copyCaseNumber();
            }
        });
        return copyCaseNumber;
    }

    private MenuItem getForwardCaseLinksItem() {
        MenuItem fwdCaseNumber = new MenuItem("Fall weiterleiten");
        fwdCaseNumber.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
        fwdCaseNumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<WorkingListItemDTO> itemList = workingList.getSelectedItemsDistinct();
                final String database = Session.instance().getCpxDatabase();
                int nr = 0;

                final StringBuilder body = new StringBuilder();
                body.append("<style>td, th { border: 1px solid black; } table { border-collapse: collapse; } thead td { font-weight: bold; background-color: #c7ccc9; } td { padding: 5px; }</style>");
                body.append("Datenbank: <b>" + database + "</b><br><br>");
                body.append("<table>");
                body.append("<thead>");
                body.append("<tr>");
                body.append("<td>Nr.</td><td>Fall</td><td>Patient</td><td style='width: 200px;'>Bemerkung</td>");
                body.append("</tr>");
                body.append("</thead>");
                body.append("</tbody>");

                for (WorkingListItemDTO dto : itemList) {
                    nr++;
                    final String patientNumber = StringUtils.trimToEmpty(dto.getPatNumber());
                    final CpxHandleLink caseLink = new CpxHandleCaseLink(dto.getHospitalIdent(), dto.getCaseNumber(), database);
                    //links.add(link);
                    body.append("<tr>");
                    body.append("<td>" + nr + "</td>");
                    body.append("<td>" + caseLink + "</td>");
                    body.append("<td>" + (patientNumber.isEmpty() ? StringUtils.trimToEmpty(dto.getPatName()) : patientNumber) + "</td>");
                    body.append("<td></td>");
                    body.append("</tr>");
                }
                body.append("</tbody>");
                body.append("</table>");
                SendMail sendMail = new SendMail();
                final String receiverMail = "";
                final String subject = "Ausgewählte CPX-Fälle";
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
        return fwdCaseNumber;
    }

    private MenuItem getDeleteCaseItem() {
        MenuItem deleteCase = new MenuItem(Lang.getWorkingListContextMenuDelete());
        deleteCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
        deleteCase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                workingList.deleteCase();
            }
        });
        return deleteCase;
    }

    private MenuItem getCancelCaseItem() {

        MenuItem cancelCase = new MenuItem(Lang.getWorkingListContextMenuCancel());
        cancelCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.BAN));
        cancelCase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                workingList.cancelCase();
            }
        });
        return cancelCase;
    }

    private MenuItem getEditProcessItem() {
        MenuItem menuItemProcess = new MenuItem(Lang.getProcessEditing());
        menuItemProcess.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER));
        menuItemProcess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                workingList.createProcessDialog();
            }
        });
        return menuItemProcess;
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
                    final SearchListTypeEn listType;
                    if(workingList.getRuleList() == null) {
                        listType = SearchListTypeEn.WORKING;
                    } else {
                        listType = SearchListTypeEn.RULE;
                    }
                    ListExportDialog listExportDialog = new ListExportDialog() {

                        @Override
                        public long prepareExport() {
                            GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
                            boolean isLocal = Session.instance().isCaseLocal();
                            boolean isShowAllReminders = Session.instance().isShowAllRemindersConfig();
                            final long[] selectedIds = workingList.getSelectedIdsDistinct(2);
                            return workingListServiceBean.prepareListExport(isLocal, isShowAllReminders, grouperModel, listType, exportType, selectedIds, new HashMap<>(workingList.getScene().getFilterManager().getFilterOptionMap()));
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
                    final SearchListTypeEn listType;
                    if(workingList.getRuleList() == null) {
                        listType = SearchListTypeEn.WORKING;
                    } else {
                        listType = SearchListTypeEn.RULE;
                    }
                    ListExportDialog listExportDialog = new ListExportDialog() {

                        @Override
                        public long prepareExport() {
                            GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
                            boolean isLocal = Session.instance().isCaseLocal();
                            boolean isShowAllReminders = Session.instance().isShowAllRemindersConfig();
                            final long[] selectedIds = workingList.getSelectedIdsDistinct(2);
                            return workingListServiceBean.prepareListExport(isLocal, isShowAllReminders, grouperModel, listType, exportType, selectedIds, new HashMap<>(workingList.getScene().getFilterManager().getFilterOptionMap()));
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
                    final SearchListTypeEn listType;
                    if(workingList.getRuleList() == null) {
                        listType = SearchListTypeEn.WORKING;
                    } else {
                        listType = SearchListTypeEn.RULE;
                    }
                    ListExportDialog listExportDialog = new ListExportDialog() {

                        @Override
                        public long prepareExport() {
                            GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
                            boolean isLocal = Session.instance().isCaseLocal();
                            boolean isShowAllReminders = Session.instance().isShowAllRemindersConfig();
                            final long[] selectedIds = workingList.getSelectedIdsDistinct(2);
                            return workingListServiceBean.prepareListExport(isLocal, isShowAllReminders, grouperModel, listType, exportType, selectedIds, new HashMap<>(workingList.getScene().getFilterManager().getFilterOptionMap()));
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
    
    private MenuItem getUpdateCaseSapItem() {
        MenuItem menuItemUpdateCaseSap = new MenuItem("SAP-Fall aktualisieren");
        menuItemUpdateCaseSap.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.REFRESH));
        menuItemUpdateCaseSap.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                long[] selectedIds = workingList.getSelectedIdsDistinct();
                TaskService<Void> updateSapCaseService = new TaskService<Void>("Aktualisiere " + (selectedIds.length > 1 ? "ausgewählte SAP-Fälle" : "ausgewählten SAP-Fall") + "...") {
                    @Override
                    public Void call() {
                        EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
                        processServiceBean.get().updateCase(Session.instance().getCpxDatabase(), selectedIds);
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
                            MainApp.showErrorMessageDialog("Fehler beim Update " + (selectedIds.length > 1 ? "der ausgewählten SAP-Fälle" : "des ausgewählten SAP-Falls") + "!\n\n" + ex.getMessage());
                        }
                    }

                };
                updateSapCaseService.start();
            }
        });
        return menuItemUpdateCaseSap;
    }

    private MenuItem getCopyToCommonItem() {
        MenuItem menuItemCopyToCommon = new MenuItem("In den Regeleditor kopieren");
        menuItemCopyToCommon.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.COPY));
        menuItemCopyToCommon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                long[] selectedIds = workingList.getSelectedIdsDistinct();
                CopyCaseToCommonTask task = new CopyCaseToCommonTask(selectedIds) {
                    @Override
                    public void afterTask(Worker.State pState) {
                        super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                        if (Worker.State.SUCCEEDED.equals(pState)) {
                            MainApp.showInfoMessageDialog("Es wurden " + getValue() + " Fälle kopiert");
                        }
                    }
                };
                task.start();
//                    EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
//                    int result = processServiceBean.get().copyTCaseToCommon(selectedIds);
//                    MainApp.showInfoMessageDialog("Es wurden " + result + " Fälle kopiert");
            }
        });
        return menuItemCopyToCommon;
    }

    private MenuItem getBatchgroupingItem() {
        MenuItem menuBatchgrouping = new MenuItem("Fälle batchgroupen");
        menuBatchgrouping.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CALCULATOR));
        menuBatchgrouping.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                long[] selectedIds = workingList.getSelectedIdsDistinct();
                MainApp.getToolbarMenuScene().openBatchgrouping();
                JobManagerScene jobManagerScene = MainApp.getToolbarMenuScene().getJobManager();
                jobManagerScene.getController().getJobBatchGroupingFXMLController().setCaseIds(selectedIds);
            }
        });
        return menuBatchgrouping;
    }

    private MenuItem getExportP21Item() {
        MenuItem menuItemExportP21 = new MenuItem("Fälle als P21-Datensatz exportieren");
        menuItemExportP21.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TABLE));
        menuItemExportP21.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!Session.instance().isExportDataAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));

                } else {
                    P21ExportDialog p21ExportDialog = new P21ExportDialog(MainApp.getWindow()) {
                        @Override
                        public long[] getSelectedIds() {
                            final long[] selectedIds = workingList.getSelectedIdsDistinct(2);
//                            final long[] selectedIds = selectedIdsTmp.length == 1 ? new long[0] : selectedIdsTmp; //if only one case is selected then export all!
                            return selectedIds;
                        }
                    };
//                    p21ExportDialog.finishProperty().addListener(new ChangeListener<Boolean>() {
//                        @Override
//                        public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
//                            if (newValue != null && newValue) {
//                                //true -> export was successful!
//                                Platform.runLater(() -> {
//                                    p21ExportDialog.close();
//                                });
//                            }
//                        }
//                    });
                    p21ExportDialog.showAndWait();
//                    p21ExportDialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
//                        @Override
//                        public void accept(ButtonType t) {
//                            if (t.equals(ButtonType.OK)) {
//                                if (p21ExportDialog.start()) {
//                                    p21ExportDialog.close();
//                                }
//                            }
//                        }
//                    });

//                    stopP21Export();
//                    exportP21 = new ExportP21();
//                    exportP21.start();
//                    exportP21 = null;
                }
            }
        });
        return menuItemExportP21;
    }

}

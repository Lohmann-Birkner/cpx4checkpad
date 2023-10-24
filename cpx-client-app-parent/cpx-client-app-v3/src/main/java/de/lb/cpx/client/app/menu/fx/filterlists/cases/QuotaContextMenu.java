/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filterlists.cases;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.p21_export.ListExportDialog;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.SendMail;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleCaseLink;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.WorkingListStatelessEJBRemote;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javax.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class QuotaContextMenu extends ContextMenu {

//        private CpxLicenseUsage cpxLicenseUsage;
    private static final Logger LOG = Logger.getLogger(QuotaContextMenu.class.getName());
//    private final License license = Session.instance().getLicense();
    private final WorkingListFXMLController workingList;

    public QuotaContextMenu(WorkingListFXMLController pWorkingList) {
        super();
        workingList = pWorkingList;
//            cpxLicenseUsage = new CpxLicenseUsage();

        MenuItem forwardCaseLinks = getForwardCaseLinksItem();
        MenuItem exportExcel = getExportExcelItem();
        MenuItem exportCsv = getExportCsvItem();
//            MenuItem unlockAllCases = getUnlockAllItems();
        MenuItem exportXml = getExportXmlItem();

        add(forwardCaseLinks);
        add(exportExcel);
        add(exportCsv);
        add(exportXml);
        //Disable or enable (ir-)relevant menu items on showing context menu
    }

    public final boolean add(MenuItem pItem) {
        return getItems().add(pItem);
    }

    public final MenuItem createMenuItem(String pTitle) {
        return new MenuItem(pTitle);
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
                    final SearchListTypeEn listType = SearchListTypeEn.QUOTA;
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
                    final SearchListTypeEn listType = SearchListTypeEn.QUOTA;
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

    private MenuItem getExportXmlItem() {
        MenuItem menuItemExportCsv = new MenuItem("Tabelle als XML (XML) exportieren");
        menuItemExportCsv.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TABLE));
        menuItemExportCsv.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!Session.instance().isExportDataAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));
                } else {
                    WorkingListStatelessEJBRemote workingListServiceBean = Session.instance().getEjbConnector().connectWorkingListBean().get();
                    final ExportTypeEn exportType = ExportTypeEn.XML;
                    final SearchListTypeEn listType = SearchListTypeEn.QUOTA;
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
}

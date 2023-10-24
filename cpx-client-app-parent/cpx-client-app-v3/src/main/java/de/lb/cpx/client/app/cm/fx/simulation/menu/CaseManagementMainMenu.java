/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.menu;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.app.menu.model.ListType;
import de.lb.cpx.client.app.model.events.report.CreateReportEvent;
import de.lb.cpx.client.app.p21_export.P21ExportDialog;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.exceptions.CpxSapException;
import de.lb.cpx.model.TCase;
//import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import java.util.List;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
//import javafx.scene.text.TextAlignment;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Popover to enable main menu
 *
 * @author wilde
 */
public abstract class CaseManagementMainMenu extends AutoFitPopOver {

    private final VersionManager versionManager;
    private final Button btnSaveAndOpenReport;
    private final Button btnOpenReport;
    private final Button btnCompleteProcess;
//AGe: change on case status is moved to summery
//    private final Button btnChangeCaseStatus;
    private final Button btnUpdateSapCase;
    private final Button btnExportCase;
//    private CpxLicenseUsage cpxLicenseUsage;
    private final License license;

    /**
     * create new instance
     *
     * @param pManager version manager to access cache and server services
     */
    public CaseManagementMainMenu(VersionManager pManager) {
        versionManager = pManager;
        setDetachable(false);
        setAutoFix(true);
        setAutoHide(true);
        setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);

//        cpxLicenseUsage = new CpxLicenseUsage();
        license = Session.instance().getLicense();

        btnCompleteProcess = new Button();
        btnCompleteProcess.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER));
        //btnCompleteProcess.setText(Lang.getCaseCreateProcess());
        btnCompleteProcess.setText(Lang.getProcessEditing());
//        btnCompleteProcess.setOnAction(new OpenItemEvent());

        // save report in temp directory and open it
        btnOpenReport = new Button();
        btnOpenReport.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.DOWNLOAD));
        btnOpenReport.setMaxWidth(Double.MAX_VALUE);
        btnOpenReport.setText(Lang.getReportOpen());
//        btnOpenReport.setOnAction(new CreateReportEvent(versionManager.getServiceFacade().getCurrentCase(), Boolean.TRUE,getOwnerWindow()));

        // save report in user specific directory and open it.
        btnSaveAndOpenReport = new Button();
        btnSaveAndOpenReport.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.DOWNLOAD));
        btnSaveAndOpenReport.setMaxWidth(Double.MAX_VALUE);
        btnSaveAndOpenReport.setText(Lang.getReportSaveAndOpen());
//        btnSaveAndOpenReport.setOnAction(new CreateReportEvent(versionManager.getServiceFacade().getCurrentCase(), Boolean.FALSE, getOwnerWindow()));

//AGe: change on case status is moved to summery
//        btnChangeCaseStatus = new Button();
//        btnChangeCaseStatus.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
//        btnChangeCaseStatus.setMaxWidth(Double.MAX_VALUE);
//        btnChangeCaseStatus.setText("Fallstatus ändern");
        
        if (Session.instance().getEjbConnector().connectConfigurationServiceBean().get().hasActiveSapJobs()) {
            btnUpdateSapCase = new Button();
            btnUpdateSapCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.REFRESH));
            btnUpdateSapCase.setMaxWidth(Double.MAX_VALUE);
            btnUpdateSapCase.setText("SAP-Fall aktualisieren");
        } else {
            btnUpdateSapCase = null;
        }

        btnExportCase = new Button();
        btnExportCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TABLE));
        btnExportCase.setMaxWidth(Double.MAX_VALUE);
        btnExportCase.setText("Fall exportieren (P21)");

        VBox menuContent = new VBox(btnCompleteProcess, btnOpenReport, btnSaveAndOpenReport,
//                btnChangeCaseStatus, 
                btnExportCase);
        if (btnUpdateSapCase != null) {
            menuContent.getChildren().add(btnUpdateSapCase);
        }
        if (!license.isFmModule()) {
            menuContent.getChildren().remove(btnCompleteProcess);
        }
        setContentNode(menuContent);
        menuContent.setDisable(versionManager.getServiceFacade().getCurrentCase().getCsCancellationReasonEn());


    }

    private void setContent() {
        final List<TWmProcess> processes = Session.instance().getEjbConnector().connectProcessServiceBean().get().getProcessesOfCase(versionManager.getServiceFacade().getCurrentCase().getId());
        final long caseId = versionManager.getServiceFacade().getCurrentCase().getId();
        final boolean showImmediatly = false;
        btnCompleteProcess.setOnAction(new ProcessEditingEvent(caseId, processes, showImmediatly) {
            @Override
            public void handle(ActionEvent event) {
                showDialog();
                hide();
            }
        });
        final TCase cs = versionManager.getServiceFacade().getCurrentCase();
        btnOpenReport.setOnAction(new CreateReportEvent(cs, Boolean.TRUE, getOwnerWindow()));
        btnSaveAndOpenReport.setOnAction(new CreateReportEvent(cs, Boolean.FALSE, getOwnerWindow()));
//        btnChangeCaseStatus.setOnAction(new ChangeCaseStatusEvent(cs));
        if (btnUpdateSapCase != null) {
            btnUpdateSapCase.setOnAction(new UpdateSapCase(cs));
        }
        btnExportCase.setOnAction(new ExportCase(cs));
    }

    @Override
    protected void show() {
        super.show(); //To change body of generated methods, choose Tools | Templates.
        setContent();
    }

    public abstract void openItem(Long pId, ListType pType);

    private static class UpdateSapCase implements EventHandler<ActionEvent> {

        private final TCase hCase;

        public UpdateSapCase(TCase pCase) {
            hCase = pCase;
        }

        @Override
        public void handle(ActionEvent t) {
            TaskService<Void> updateSapCaseService = new TaskService<Void>("Aktualisiere den Fall aus SAP!") {
                @Override
                public Void call() {
                    EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
                    processServiceBean.get().updateCase(Session.instance().getCpxDatabase(), hCase.getId());
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
                        MainApp.showErrorMessageDialog("Fehler beim Update des Falles aus SAP!\n\n" + ex.getMessage());
                    }
                }

            };
            updateSapCaseService.start();
        }
    }

    private static class ExportCase implements EventHandler<ActionEvent> {

        private final TCase hCase;

        public ExportCase(TCase pCase) {
            hCase = pCase;
        }

        @Override
        public void handle(ActionEvent t) {
            if (!Session.instance().isExportDataAllowed()) {
                de.lb.cpx.client.core.MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));

            } else {
                P21ExportDialog p21ExportDialog = new P21ExportDialog(MainApp.getWindow()) {
                    @Override
                    public long[] getSelectedIds() {
                        return new long[]{hCase.id};
                    }
                };
                p21ExportDialog.showAndWait();
            }
        }
    }

    /*
    *  private classes
     */
//    private class ChangeCaseStatusEvent implements EventHandler<ActionEvent> {
//
//        private final TCase hCase;
//
//        public ChangeCaseStatusEvent(TCase pCase) {
//            hCase = pCase;
//        }
//
//        @Override
//        public void handle(ActionEvent event) {
//            VBox content = new VBox();
//            content.setFillWidth(true);
//            PopOver over = new AutoFitPopOver(content);
//            over.setDetached(false);
//            over.setArrowLocation(ArrowLocation.RIGHT_CENTER);
//            setAutoFix(true);
//            setAutoHide(true);
//            for (CaseStatusEn status : CaseStatusEn.values()) {
//                Button btn = new Button(status.getTranslation().getValue());
//                btn.setStyle("-fx-alignment: CENTER-LEFT;");
//                btn.setTextAlignment(TextAlignment.LEFT);
//                btn.setMaxWidth(Double.MAX_VALUE);
//                btn.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        hCase.setCsStatusEn(status);
//                        versionManager.getServiceFacade().saveCaseEntity();
//                        versionManager.getServiceFacade().getProperties().put(CaseManagementFXMLController.UPDATE_SUMMARY, Boolean.TRUE);
//                    }
//                });
//                content.getChildren().add(btn);
//            }
//            over.show(btnChangeCaseStatus);
//        }
//
//    }

}

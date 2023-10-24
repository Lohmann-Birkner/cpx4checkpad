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
package de.lb.cpx.client.app.model.events.report;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.ProgressWaitingDialog;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.service.ejb.CaseServiceBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import de.lb.cpx.shared.lang.Lang;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.apache.commons.io.FilenameUtils;

/**
 * Class for the creation of an report to add as an event handler
 *
 * @author wilde
 */
public class CreateReportEvent implements EventHandler<ActionEvent> {

    private static final Logger LOG = Logger.getLogger(CreateReportEvent.class.getName());

    private final TCase hCase;
    private final Boolean tempOnly;
    private final EjbProxy<SingleCaseGroupingEJBRemote> bean;
    private final EjbProxy<CaseServiceBeanRemote> caseServiceBean;
    private final Window owner;

    /**
     * creates new event handler to open/create a report
     *
     * @param pCase case that report is based on
     * @param pTempOnly indicator if report should be only temporary stored
     * @param pOwner owner window for bluring
     */
    public CreateReportEvent(TCase pCase, Boolean pTempOnly, Window pOwner) {
        this.hCase = pCase;
        this.tempOnly = pTempOnly;
        EjbConnector ejbConnector = Session.instance().getEjbConnector();
        bean = ejbConnector.connectSingleCaseGroupingBean();
        caseServiceBean = ejbConnector.connectCaseServiceBean();
        this.owner = pOwner;
    }

    @Override
    public void handle(ActionEvent t) {
        beforeTask();
        Service<byte[]> service = new Service<byte[]>() {
            @Override
            protected Task<byte[]> createTask() {
                return new Task<byte[]>() {
                    @Override
                    protected byte[] call() throws InterruptedException, CpxIllegalArgumentException {
                        byte[] reportContent = bean.get().exportCase(hCase.getCsCaseNumber(), hCase.getCsHospitalIdent(), CpxClientConfig.instance().getSelectedGrouper());
                        return reportContent;
                    }
                };
            }
        };
        service.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (null != newValue) {
                    switch (newValue) {
                        case FAILED:
                            afterTask(newValue);
                            LOG.log(Level.SEVERE, Lang.getReportFailed(), service.getException());
//                            MainApp.showErrorMessageDialog(service.getException(), Lang.getReportFailed());
                            MainApp.showErrorMessageDialog(Lang.getReportFailed(), service.getException().getCause().getLocalizedMessage(), MainApp.getWindow());
                            //MainApp.showErrorMessageDialog(Lang.getReportFailed());
                            //LOG.log(Level.SEVERE, null, "Report generation is failed");
                            break;
                        case CANCELLED:
                            afterTask(newValue);
                            MainApp.showErrorMessageDialog(Lang.getReportCancelled());
                            LOG.log(Level.SEVERE, null, "Report generation is cancelled");
                            break;
                        case SUCCEEDED:
                            afterTask(newValue);
                            byte[] reportContent = service.getValue();
                            if (!tempOnly) {
                                saveAndOpenReport(hCase, reportContent);
                            } else {
                                openTempReport(hCase, reportContent);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            private void openTempReport(TCase pCase, byte[] reportContent) {
                String property = System.getProperty("java.io.tmpdir");  // temp dir on client
                String outputLoc = property + "report_" + pCase.getCsCaseNumber() + ".pdf";
                if (reportContent != null) {
                    try {
                        File fileOnClient = DocumentManager.createFileInTempOrSpecificDir(reportContent, outputLoc);
                        DocumentManager.showFile(fileOnClient);
                        fileOnClient.deleteOnExit();
                        bean.get().deleteReportFromServer();
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            }

            private void saveAndOpenReport(TCase pCase, byte[] reportContent) {
                /*The FileChooser is implemented using native APIs in JavaFX,
                  so it's behavior is platform-dependent.
                  On Mac OS X, the FileChooser will disable the "Save" button if the file name field is empty or wrong.
                 */

                FileChooser fileChooser = FileChooserFactory.instance().createFileChooser();
//                DirectoryChooser chooser = new DirectoryChooser();    //Use DirectoryChooser to select a folder:
//                List<String> supportedFileTypes = java.util.Arrays.asList(new String[]{"*.pdf"});
//                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf", supportedFileTypes));
                ExtensionFilter extensionFilter = new ExtensionFilter("PDF File (*.pdf)", "*.pdf");
                fileChooser.getExtensionFilters().add(extensionFilter);
                fileChooser.setTitle(Lang.getReportFileChooserTile());
                String defaultReportName = DocumentManager.validateFilename("report_" + pCase.getCsCaseNumber());
                fileChooser.setInitialFileName(defaultReportName);
                fileChooser.setSelectedExtensionFilter(extensionFilter);
//                fileChooser.setInitialDirectory(file);
                File file = fileChooser.showSaveDialog(owner);
                // OK button don't do anything, it don't go further if we use "?, *" (or possibly some other chars) as a fileName (that's prob in Windows Dialog. tested with doc and excel files as well).
                //However, windows dialog shows warning message if we use some chars like :, ", /, \, <, >, | in a fileName.

//                boolean isValidFilename = FileUtils.isValidFilename(file.getAbsolutePath());
                CpxClientConfig.instance().setUserRecentFileChooserPath(file);

                if (file != null && reportContent != null) {
                    try {
                        String outputLoc = file.getAbsolutePath();
//                        if (!file.getAbsolutePath().endsWith(".pdf")) {
                        if (!FilenameUtils.getExtension(outputLoc).equalsIgnoreCase("pdf")) {
                            outputLoc = file.getAbsolutePath().concat(".pdf");
                        }
                        File fileOnClient = DocumentManager.createFileInTempOrSpecificDir(reportContent, outputLoc);
                        DocumentManager.showFile(fileOnClient);
                        bean.get().deleteReportFromServer();
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

//        String pdfReportType = caseServiceBean.get().getPdfReportType();
        boolean isAllowedToUse = caseServiceBean.get().isPdfReportAllowedToUse();
//        if (pdfReportType.isEmpty() || StringUtils.containsIgnoreCase(pdfReportType, "NONE")) {
        if (!isAllowedToUse) {
            MainApp.showInfoMessageDialog("Report darf nicht erstellt werden (bitte kontaktieren Sie Administrator)", MainApp.getWindow());
        } else {
            ProgressWaitingDialog diag = new ProgressWaitingDialog(service);
            diag.setHeaderText(Lang.getReportDialogHeaderText());
            diag.initOwner(owner);
            diag.initModality(Modality.APPLICATION_MODAL);
            diag.setContentText(Lang.getReportDialogContentText());
            service.start();
        }
    }

    /*
    LIFECYCLE METHODES
     */
    public void beforeTask() {
    }

    public void afterTask(Worker.State pState) {
    }
}

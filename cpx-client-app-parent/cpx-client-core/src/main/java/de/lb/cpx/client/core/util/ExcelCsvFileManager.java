/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import de.FileUtils;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.alert.ProgressWaitingDialog;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.WeekdayEn;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.table.export.CsvFileWriter;
import de.lb.cpx.table.export.ExcelFileWriter;
import de.lb.cpx.table.export.ExportWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;

/**
 * common task service to export tables as excel and/or csv (txt).
 *
 * @author nandola
 */
public class ExcelCsvFileManager {

//    public enum TYPE {
//        EXCEL, CSV
//    }
    private static final Logger LOG = Logger.getLogger(ExcelCsvFileManager.class.getName());
    //final File file;
    public final ExportTypeEn type;
    public final String defaultFileName;
    private final Iterator<Object[]> dataIterator;
    private final Callback<?, Object[][]> dataCallback;

//    public ExcelCsvFileManager(final TYPE pType, final String pDefaultFileName) {
//        this(pType, pData == null ? null : Arrays.asList(pData).iterator(), pDefaultFileName);
//    }
    public ExcelCsvFileManager(final ExportTypeEn pType, final String pDefaultFileName, final TableView<?> pTableView) {

        this(pType, pDefaultFileName, createCallback(pTableView), null);
    }

    //testing
//    private int getNumberOfVisibleRows( final TableView<?> pTableView)
//  {
//    VirtualFlow<?> vf = loadVirtualFlow(pTableView);
//    vf.getCell(0);
//    vf.getCell(1);
//    return vf.getLastVisibleCell().getIndex() - vf.getFirstVisibleCell().getIndex();
//  }
//    
//  private VirtualFlow<?> loadVirtualFlow( final TableView<?> pTableView)
//  {
//    return (VirtualFlow<?>) ( (TableViewSkin<?>) pTableView.getSkin() ).getChildren().get( 1 );
//  }
    protected static Callback<?, Object[][]> createCallback(final TableView<?> pTableView) {
        if (pTableView == null) {
            throw new IllegalArgumentException("table view cannot be null for excel/csv writer!");
        }
        return new Callback<Object, Object[][]>() {
            @Override
            public Object[][] call(Object param) {

                final List<?> selectedItems = pTableView.getSelectionModel().getSelectedItems();
                final boolean isMultiSelected = selectedItems.size() > 1;

                Object[] rows = isMultiSelected ? selectedItems.toArray() : pTableView.getItems().toArray();
                Object[] columns = pTableView.getColumns().toArray();

                Object[][] data = new Object[rows.length + 1][columns.length];
//                final List<Integer> ignoreColumns = new ArrayList<>();

                for (int i = 0; i < data.length - 1; i++) {
                    for (int j = 0; j < data[i].length; j++) {
//                        if (ignoreColumns.contains(j)) {
//                            continue;
//                        }
                        //skip unselected table ROWS
//                        if (isMultiSelected) {
//                            if (i > 0 && !selectedItems.contains(pTableView.getItems().get(i))) {
//                                //column is not selected
//                                continue;
//                            }
//                        }
                        TableColumn<?, ?> column = pTableView.getColumns().get(j);
//                        final Object cellValue;
//                        SearchListAttribute searchListAttribute = null;
                        String headerValue;
                        String headerName;

//                        final String headerValue;
//                            if (column instanceof FilterTableView.SelectionColumn) {
//                                //ignore column with check box for multi select functionality
//                                //and shift all other columns to the left
//                                ignoreColumns.add(j);
//                                continue;
//                            } else 
                        if (column instanceof FilterColumn) {
                            headerName = ((FilterColumn) column).getName();
                            headerValue = ((FilterColumn) column).getDataKey();
                            ((FilterColumn) column).getTranslation().getValue();

                            /*  if (pTableView instanceof WorkingListFXMLController.CaseList) {
                                Lang.get(WorkingListAttributes.instance().get(((FilterColumn) column).getDataKey()).getLanguageKey()).getValue();
                                searchListAttribute = WorkingListAttributes.instance().get(((FilterColumn) column).getDataKey());
//                                headerValue = WorkingListAttributes.instance().get(((FilterColumn) column).getDataKey()).getKey();
                                headerValue = ((FilterColumn) column).getDataKey();
                            } else if (pTableView instanceof WorkflowListFXMLController.ProcessList) {
                                searchListAttribute = WorkflowListAttributes.instance().get(((FilterColumn) column).getDataKey());
//                                headerValue = WorkflowListAttributes.instance().get(((FilterColumn) column).getDataKey()).getKey();
                                headerValue = ((FilterColumn) column).getDataKey();
                            }   */
                        } else {
                            if (column != null) {
                                headerName = column.getText();
                                headerValue = column.getText();
                            } else {
                                LOG.log(Level.WARNING, "column is null!");
                                continue;
                            }
                        }

                        // the first table row
                        if (i == 0) {
//                            data[i][j - ignoreColumns.size()] = headerValue;

//                            data[i][j] = headerValue;
                            data[i][j] = headerName;
                        }

                        /*
                        if (isMultiSelected) {
                            if (headerValue.equals("Änderung von") || headerValue.equals("Bearbeiter") || headerValue.equals("WV Sender") || headerValue.equals("WV Empfänger")) {
                                Long cellData = (Long) column.getCellData(pTableView.getItems().indexOf(selectedItems.get(i)));
                                if (cellData != null && !cellData.toString().isEmpty()) {
//                                                CdbUsers cdbUsers = wrkFlowBean.get().getCdbUser(cellData);
                                    data[i + 1][j] = MenuCache.instance().getUserNameForId(cellData);//cdbUsers.getUName();
                                }
                            } else if (headerValue.equals("Art der Wiedervorlage")) {
                                Long cellData = (Long) column.getCellData(pTableView.getItems().indexOf(selectedItems.get(i)));
                                if (cellData != null && !cellData.toString().isEmpty()) {
                                    CWmListReminderSubject reminderSubject = ProcessServiceBean.get().getReminderSubjectById(cellData);
                                    data[i + 1][j] = reminderSubject.getWmRsName();
                                }
                            } else if (headerValue.equals("Versicherungsname")) {
                                column.getCellData(pTableView.getItems().indexOf(selectedItems.get(i)));    //String
                            } else if (headerValue.equals("Vorgangsart")) {
                                Long cellData = (Long) column.getCellData(pTableView.getItems().indexOf(selectedItems.get(i)));    //Long
                                CWmListProcessTopic processTopic = ProcessServiceBean.get().getProcessTopic(cellData);
                                data[i + 1][j] = processTopic != null ? processTopic.getWmPtName() : "";
                            } else if (headerValue.equals("Sperre") || headerValue.equals("Prio")) {
                                if (column.getCellData(pTableView.getItems().indexOf(selectedItems.get(i))).equals(false)) {
                                    data[i + 1][j] = "";
                                }  //Boolean
                            } else {
                                data[i + 1][j] = column.getCellData(pTableView.getItems().indexOf(selectedItems.get(i)));
                            }
                        } else if (!isMultiSelected) {
                            if (headerValue.equals("Änderung von") || headerValue.equals("Bearbeiter") || headerValue.equals("WV Sender") || headerValue.equals("WV Empfänger")) {
                                Long cellData = (Long) column.getCellData(i);
                                if (cellData != null && !cellData.toString().isEmpty()) {
//                                                CdbUsers cdbUsers = wrkFlowBean.get().getCdbUser(cellData);
                                    data[i + 1][j] = MenuCache.instance().getUserNameForId(cellData);//cdbUsers.getUName();
                                }
                            } else {
                                data[i + 1][j] = column.getCellData(i);
                            }
                        }       
                         */
//                        data[i + 1][j - ignoreColumns.size()] = cellValue;
//                        data[i + 1][j] = cellValue;
                        Object cell = null;
                        if (isMultiSelected) {
                            cell = column.getCellData(pTableView.getItems().indexOf(selectedItems.get(i)));
                        } else if (!isMultiSelected) {
                            cell = column.getCellData(i);
                        }

//                        TableHeader header = (TableHeader) pTableView.lookup("#header");
//                        TableView tb = (TableView) pTableView.lookup("#header");
//                        if (headerValue.equals("Änderung von") || headerValue.equals("Bearbeiter") || headerValue.equals("WV Sender") || headerValue.equals("WV Empfänger")) {
                        if (WorkflowListAttributes.vmModUser.equals(headerValue) || WorkflowListAttributes.assUser.equals(headerValue) || WorkflowListAttributes.assSender.equals(headerValue) || WorkflowListAttributes.assReceiver.equals(headerValue)) {
                            Long cellData = (Long) cell;
                            if (cellData != null && !cellData.toString().isEmpty()) {
//                                CdbUsers cdbUsers = wrkFlowBean.get().getCdbUser(cellData);
                                data[i + 1][j] = MenuCache.instance().getUserNameForId(cellData);//cdbUsers.getUName();
                            }
//                        } else if (headerValue.equals("Art der Wiedervorlage")) {
                        } else if (WorkflowListAttributes.assSubject.equals(headerValue)) {
                            Long cellData = (Long) cell;
                            if (cellData != null && !cellData.toString().isEmpty()) {
                                String reminderSubject = MenuCache.instance().getReminderSubjectsForInternalId(cellData);
                                data[i + 1][j] = (reminderSubject != null && !reminderSubject.isEmpty()) ? reminderSubject : "";
//                                CWmListReminderSubject reminderSubject = ProcessServiceBean.get().getReminderSubjectById(cellData);
//                                data[i + 1][j] = (reminderSubject != null && reminderSubject.getWmRsName() != null) ? reminderSubject.getWmRsName() : "";
                            }
//                        } else if (headerValue.equals("Versicherungsname")) {
                        } else if (WorkflowListAttributes.insInsCompanyName.equals(headerValue)) {
                            String cellData = (String) cell;
                            if (cellData != null && !cellData.isEmpty()) {
                                String insuCompName = MenuCache.instance().getInsuranceForIkz(cellData);
//                                String insuCompName = ProcessServiceBean.get().getInsuCompName(cellData, AbstractCpxCatalog.DEFAULT_COUNTRY);
                                data[i + 1][j] = (insuCompName != null && !insuCompName.isEmpty()) ? insuCompName : "";
                            }
//                        } else if (headerValue.equals("Vorgangsart")) {
                        } else if (WorkflowListAttributes.processTopic.equals(headerValue)) {
                            Long cellData = (Long) cell;
                            if (cellData != null && cellData > 0) {
                                String processTopic = MenuCache.instance().getProcessTopicForId(cellData);
                                data[i + 1][j] = (processTopic != null && !processTopic.isEmpty()) ? processTopic : "";
//                                CWmListProcessTopic processTopic = ProcessServiceBean.get().getProcessTopic(cellData);
//                                data[i + 1][j] = processTopic != null ? processTopic.getWmPtName() : "";
                            }
//                        } else if (headerValue.equals("Sperre") || headerValue.equals("Prio")) {
                        } else if (WorkflowListAttributes.processResult.equals(headerValue)) {
                            Long cellData = (Long) cell;
                            if (cellData != null && cellData > 0) {
                                String processResult = MenuCache.instance().getProcessResultForId(cellData);
                                data[i + 1][j] = (processResult != null && !processResult.isEmpty()) ? processResult : "";
//                                CWmListProcessResult processResult = ProcessServiceBean.get().getProcessResult(cellData);
//                                data[i + 1][j] = processResult != null ? processResult.getWmPrName() : "";
                            }
                        } else if (WorkflowListAttributes.lock.equals(headerValue) || WorkingListAttributes.lock.equals(headerValue) || WorkflowListAttributes.wvPrio.equals(headerValue)) {
                            if (cell != null) {
                                if (cell.equals(false)) {
                                    data[i + 1][j] = "";
                                } else {
                                    data[i + 1][j] = cell;
                                }
                            }
//                        } else if (headerValue.equals("Status")) {
                        } else if (WorkingListAttributes.csStatusEn.equals(headerValue)) {  //case status
                            String cellData = (String) cell;
                            if (cellData != null && !cellData.isEmpty()) {
//                                if(cell instanceof CaseStatusEn)
//                                if (CaseStatusEn.class.isInstance(cell)) {
//                                    CaseStatusEn caseStatus = CaseStatusEn.valueOf(cellData);
//                                    data[i + 1][j] = caseStatus.getTranslation();
//                                }
                                CaseStatusEn caseStatus = CaseStatusEn.valueOf(cellData);
                                data[i + 1][j] = caseStatus.getTranslation();
                            }
//                        } else if ("MDC".equals(headerValue)) {
                        } else if (WorkingListAttributes.grpresGroup.equals(headerValue)) {
                            String cellData = (String) cell;
                            if (cellData != null && !cellData.isEmpty()) {
                                GrouperMdcOrSkEn grouperMdcOrSk = GrouperMdcOrSkEn.valueOf(cellData);
                                data[i + 1][j] = grouperMdcOrSk.getIdStr();
                            }
//                        } else if (headerValue.equals("Aufn. WT") || headerValue.equals("Entl. WT")) {
                        } else if (WorkingListAttributes.csdAdmissionDateWeekday.equals(headerValue) || WorkingListAttributes.csdDischargeDateWeekday.equals(headerValue)) {
                            int cellData = (int) cell; //java.lang.Integer
//                            DayOfWeek dayOfWeek = DayOfWeek.of(cellData);
//                            data[i + 1][j] = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.GERMANY);
                            WeekdayEn Weekday = WeekdayEn.findById(cellData);
                            data[i + 1][j] = Weekday.getTranslation().getAbbreviation();
                        } else {    // for all other columns, display its values.
                            data[i + 1][j] = cell;
                        }

                    }
                }
                return data;
            }
        };
    }

    /*
     protected static Callback<?, Object[][]> createCallback(final TableView<?> pTableView) {
        if (pTableView == null) {
            throw new IllegalArgumentException("table view cannot be null for excel/csv writer!");
        }
        return new Callback<Object, Object[][]>() {
            @Override
            public Object[][] call(Object param) {

                List<Integer> selectedIndices = new ArrayList<>(pTableView.getSelectionModel().getSelectedIndices());
                final boolean isMultiSelected = selectedIndices.size() > 1;

                Object[] rows = pTableView.getItems().toArray();
                Object[] columns = pTableView.getColumns().toArray();

                Object[][] data = new Object[rows.length + 1][columns.length];
                final List<Integer> ignoreColumns = new ArrayList<>();

                int row = 0;
                for (int i = 0; i < data.length - 1; i++) {
                    final boolean rowIsSelected = !isMultiSelected || selectedIndices.contains(i);
                    for (int j = 0; j < data[i].length; j++) {
                        if (ignoreColumns.contains(j)) {
                            continue;
                        }
                        TableColumn column = (TableColumn) pTableView.getColumns().get(j);
                        final Object cellValue;
                        if (i == 0) {
                            //                                  ((Label) ((CpTableColumn) getController().getTableView().getColumns().get(j)).getHeader().getChildren().get(0)).getText();
                            final Object headerValue;
//                            if (column instanceof FilterTableView.SelectionColumn) {
//                                //ignore column with check box for multi select functionality
//                                //and shift all other columns to the left
//                                ignoreColumns.add(j);
//                                continue;
//                            } else 
                            if (column instanceof FilterColumn) {
                                headerValue = ((FilterColumn) column).getName();
                            } else {
                                headerValue = column.getText();
                            }
                            data[row][j - ignoreColumns.size()] = headerValue;
                        }
                        if (!rowIsSelected) {
                            continue;
                        }
                        cellValue = column.getCellData(i);
                        data[row + 1][j - ignoreColumns.size()] = cellValue;
                    }
                    if (rowIsSelected) {
                        row++;
                    }
                }
                return data;
            }
        };
    }
     */
    public ExcelCsvFileManager(final ExportTypeEn pType, final String pDefaultFileName, final Callback<Void, Object[][]> pDataCallback) {
        this(pType, pDefaultFileName, pDataCallback, null);
    }

//    public ExcelCsvFileManager(final TYPE pType, final String pDefaultFileName, final Callback<Void, TableView<?>> pTableViewCallback) {
//        this(pType, pDefaultFileName, pTableViewCallback, null);
//    }
    public ExcelCsvFileManager(final ExportTypeEn pType, final String pDefaultFileName, final Iterator<Object[]> pDataIterator) {
        this(pType, pDefaultFileName, null, pDataIterator);
    }

    protected ExcelCsvFileManager(final ExportTypeEn pType,
            final String pDefaultFileName,
            final Callback<?, Object[][]> pDataCallback,
            final Iterator<Object[]> pDataIterator) {
        if (pType == null) {
            throw new IllegalArgumentException("excel/csv writer needs type (csv or excel)");
        }
        if (pDataIterator == null && pDataCallback == null) {
            throw new IllegalArgumentException("data iterator and data callback for excel/csv writer cannot be null both!");
        }
        if (pDataIterator != null && pDataCallback != null) {
            throw new IllegalArgumentException("you cannot use data iterator and data callback together for excel/csv writer!");
        }

        type = pType;
        defaultFileName = StringUtils.trimToNull(pDefaultFileName);
        dataIterator = pDataIterator;
        dataCallback = pDataCallback;
//        Session session = Session.instance();
//        EjbConnector connector = session.getEjbConnector();
//        ProcessServiceBean = connector.connectProcessServiceBean();
    }

    public void openDialog(final Window pWindow) {
//        if (pWindow == null) {
//            throw new IllegalArgumentException("excel/csv writer needs a window owner");
//        }
        if (!Session.instance().isExportDataAllowed()) {
            MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));
        } else {
            final Window window = pWindow == null ? BasicMainApp.getWindow() : pWindow;
            final FileChooser.ExtensionFilter extensionFilter;
            final String title;
            if (ExportTypeEn.EXCEL.equals(type)) {
                final List<String> supportedFileTypes = Arrays.asList(new String[]{"*.xlsx", "*.xls"});
                extensionFilter = new FileChooser.ExtensionFilter("xlsx", supportedFileTypes);
                title = "Save Excel File";
            } else {
                final List<String> supportedFileTypes = Arrays.asList(new String[]{"*.csv"});
                extensionFilter = new FileChooser.ExtensionFilter("csv", supportedFileTypes);
                title = "Save CSV File";
            }
            FileChooser fileChooser = FileChooserFactory.instance().createFileChooser();
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setTitle(title);
            fileChooser.setInitialFileName(defaultFileName);
            File file;
            while (true) {
                file = fileChooser.showSaveDialog(window);
                if (file != null && FileUtils.isFileLock(file)) {
                    BasicMainApp.showErrorMessageDialog("Die Datei " + file.getAbsolutePath() + " ist bereits im Zugriff.\n\nSchließen Sie zunächst die offene Anwendung oder wählen Sie einen anderen Dateinamen aus");
                } else {
                    break;
                }
            }
            if (file != null) {
                CpxClientConfig.instance().setUserRecentFileChooserPath(file);
                writeDataToFile(file, window);
            }
        }
    }

//    public ExcelCsvFileManager(final File pFile) {
//        if (pFile == null) {
//            throw new IllegalArgumentException("excel/csv writer needs a target file");
//        }
//        file = pFile;
//        final String fileExtension = FilenameUtils.getExtension(file.getName());
//        if (fileExtension.equalsIgnoreCase("xlsx") || fileExtension.equalsIgnoreCase("xls")) {
//            type = TYPE.EXCEL;
//        } else if (fileExtension.equalsIgnoreCase("csv")) {
//            type = TYPE.CSV;
//        } else {
//            throw new IllegalArgumentException("file type is unknown to excel/csv writer: " + file.getAbsolutePath());
//        }
//    }
    protected void writeDataToFile(final File pFile, final Window pOwner) {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws IOException {
                        final String fileName = pFile.getAbsolutePath();
                        try (final ExportWriter writer = ExportTypeEn.EXCEL.equals(type)
                                ? new ExcelFileWriter(fileName)
                                : new CsvFileWriter(fileName)) {
                            if (dataIterator != null) {
                                writer.writeData(dataIterator);
                            } else {
                                writer.writeData(dataCallback.call(null));
                            }
                        } catch (Exception ex) {
                            LOG.log(Level.WARNING, "Was not able to close writer on file " + fileName, ex);
                        }
                        return null;
                    }
                };
            }
        };

        ProgressWaitingDialog diag = new ProgressWaitingDialog(service);
        diag.initOwner(pOwner);
        diag.setHeaderText(Lang.getReportDialogHeaderText());
        diag.initModality(Modality.APPLICATION_MODAL);
        diag.setContentText("Tabelle wird exportiert");
        service.start();

        service.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (null != newValue) {
                    switch (newValue) {
                        case FAILED:
                            BasicMainApp.showErrorMessageDialog("Tabelle export ist fehlgeschlagen");
                            LOG.log(Level.SEVERE, null, "List export is failed");
                            break;
                        case CANCELLED:
                            BasicMainApp.showErrorMessageDialog("Tabelle export wird abgebrochen");
                            LOG.log(Level.SEVERE, null, "List export is cancelled");
                            break;
                        case SUCCEEDED:
                            Notifications notifications = NotificationsFactory.instance().createInformationNotification();
                            //notifications.position(Pos.TOP_CENTER);
                            //notifications.darkStyle();
                            //notifications.owner(window);
                            notifications.title("Export der Tabelle ist beendet")
                                    .text("Ort: " + pFile.getAbsolutePath())
                                    .onAction((event) -> {
                                        BasicMainApp.openUrl(pFile.getAbsolutePath());
                                    })
                                    .show();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

//    public static void ExcelCsvWriter(File file, Object[][] data, Window window) {
////            try {
////                if (DocumentManager.isFileLock(file)) {
////                    MainApp.showErrorMessageDialog(file.getAbsolutePath() + "is already in use");
////                    return;
////                }
//        Service<Void> service = new Service<Void>() {
//            @Override
//            protected Task<Void> createTask() {
//                return new Task<Void>() {
//                    @Override
//                    protected Void call() throws InterruptedException {
//                        try {
//                            String fileExtension = FilenameUtils.getExtension(file.getName());
//                            if (fileExtension.equalsIgnoreCase("xlsx") || fileExtension.equalsIgnoreCase("xls")) {
//                                ExcelFileWriter excelFileWriter = new ExcelFileWriter(file.getAbsolutePath(), data);
//                            } else if (fileExtension.equalsIgnoreCase("csv")) {
//                                CsvFileWriter csvFileWriter = new CsvFileWriter(file.getAbsolutePath(), data);
//                            } else {
//                                MainApp.showErrorMessageDialog(fileExtension + " ist keine gültige Dateiendung zum Exportieren der Tabelle");
//                                LOG.log(Level.SEVERE, "{0} is not a valid file extension to export the table!", fileExtension);
//                            }
//                        } catch (Throwable ex) {
//                            LOG.log(Level.SEVERE, null, ex);
//                        }
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        });
//                        return null;
//                    }
//                };
//            }
//        };
//
//        ProgressWaitingDialog diag = new ProgressWaitingDialog(service);
//        diag.initOwner(window);
//        diag.setHeaderText(Lang.getReportDialogHeaderText());
//        diag.initModality(Modality.APPLICATION_MODAL);
//        diag.setContentText("Tabelle wird exportiert");
//        service.start();
//
//        service.stateProperty().addListener(new ChangeListener<Worker.State>() {
//            @Override
//            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
//                if (null != newValue) {
//                    switch (newValue) {
//                        case FAILED:
//                            MainApp.showErrorMessageDialog("Tabelle export ist fehlgeschlagen");
//                            LOG.log(Level.SEVERE, null, "List export is failed");
//                            break;
//                        case CANCELLED:
//                            MainApp.showErrorMessageDialog("Tabelle export wird abgebrochen");
//                            LOG.log(Level.SEVERE, null, "List export is cancelled");
//                            break;
//                        case SUCCEEDED:
//                            Notifications notifications = NotificationsFactory.instance().createInformationNotification();
//                            //notifications.position(Pos.TOP_CENTER);
//                            //notifications.darkStyle();
//                            //notifications.owner(window);
//                            notifications.title("Export der Tabelle ist beendet")
//                                    .text("Ort: " + file.getAbsolutePath())
//                                    .onAction((event) -> {
//                                        MainApp.openUrl(file.getAbsolutePath());
//                                    })
//                                    .show();
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//        });
////            }
////            catch (IOException ex) {
////                Logger.getLogger(ExcelCsvFileManager.class.getName()).log(Level.SEVERE, null, ex);
////            }
//    }
}

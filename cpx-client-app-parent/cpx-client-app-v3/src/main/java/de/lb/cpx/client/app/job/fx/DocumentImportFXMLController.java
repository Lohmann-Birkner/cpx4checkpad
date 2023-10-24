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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.job.fx;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddRequestDialog;
import de.lb.cpx.client.app.wm.fx.process.section.WmDocumentSection;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.progress.RingProgressIndicator;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.converter.DocumentConverter;
import de.lb.cpx.converter.DocumentConverterResult;
import de.lb.cpx.cpx.document.viewer.DocumentViewer;
import de.lb.cpx.document.Utils;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.reader.DocumentReader;
import de.lb.cpx.reader.exception.OfficeDisabledException;
import de.lb.cpx.reader.exception.ReaderException;
import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.WmWorkflowTypeEn;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javax.naming.NamingException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 * FXML Controller class
 *
 * @author niemeier
 */
public class DocumentImportFXMLController extends Controller<CpxScene> {

    private static final Logger LOG = Logger.getLogger(DocumentImportFXMLController.class.getName());

    @FXML
    private AnchorPane apRoot;
    @FXML
    private TreeView<FileConvert> fileTree;
    @FXML
    private Label lblFolder;
    @FXML
    private TextField txtSelectedFolder;
    @FXML
    private Button btnSelectFolder;
    @FXML
    private Hyperlink linkOriginal;
    @FXML
    private Hyperlink linkPdf;
    @FXML
    private Hyperlink linkTxt;
    @FXML
    private AnchorPane previewBox;
    @FXML
    private Label lblFileSize;
    @FXML
    private Label lblFileDate;
    @FXML
    private Glyph glExplorer;
//    @FXML
//    private Glyph glClear;
    @FXML
    private StackPane stackDetails;
    private FileConvert selectedFile;
    @FXML
    private Label lblFileOwner;
    @FXML
    private LabeledComboBox<CWmListDocumentType> cbDocumentType;
    @FXML
    private LabeledDatePicker dtReminderDueDate;
    @FXML
    private LabeledTextField txtReminderAssignedTo;
    @FXML
    private LabeledComboBox<CWmListActionSubject> cbActionType;
    @FXML
    private LabeledComboBox<WmRequestTypeEn> cbRequestType;
    @FXML
    private Button editReminderComment;
    @FXML
    private Button editActionComment;
    private final StringProperty reminderComment = new SimpleStringProperty();
    private final StringProperty actionComment = new SimpleStringProperty();
    @FXML
    private CheckBox cbDocumentImportOfficeEnabled;
    @FXML
    private Button btnImport;
    @FXML
    private SectionHeader headerDocument;
    @FXML
    private SectionHeader headerWorkflow;
    @FXML
    private LabeledComboBox<CWmListReminderSubject> cbReminderType;
    private DocumentViewer docViewer;
    private int increment = 0;
    public static final int SIZE_REMINDER_COMMENT = 255;
    public static final int SIZE_ACTION_COMMENT = 255;
    protected ValidationSupport validationSupport = new ValidationSupport();
    private final EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();

//    public static final String[] SUPPORTED_FILE_TYPES = new String[]{
//        "pdf", "xls", "xlsx", "txt", "csv", "doc", "docx", "msg", "jpg", "jpeg", "tiff", "bmp", "png", "gif", "xml"
//    };
    private static final String[] SUPPORTED_FILE_TYPES = WmDocumentSection.getSupportedFileTypes();

    private final EjbProxy<SingleCaseEJBRemote> singleCaseEjb = Session.instance().getEjbConnector().connectSingleCaseBean();

    private final File viewerFile = new File(CpxSystemProperties.getInstance().getCpxClientPdfJsViewerFile());

    private static final int NUMBER_OF_THREADS = 2;
    private final BlockingQueue<FileConvert> converterQueue = new PriorityBlockingQueue<>(25, (o1, o2) -> {
        return Integer.compare(o2.getPriority(), o1.getPriority());
    });
    //final private Set<FileConvert> processeQueueElems = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final DocumentConverterThread[] threads = new DocumentConverterThread[NUMBER_OF_THREADS];
    private final Map<File, DocumentConverterResult> documents = new ConcurrentHashMap<>();
    @FXML
    private SplitPane splitPane;
    @FXML
    private Hyperlink fileName;
    @FXML
    private Glyph glFolderInExplorer;
    @FXML
    private Glyph glRefreshFolder;
    @FXML
    private Button btnImportSelected;
    private final Glyph glClear = new Glyph();

    //private ObservableList<FileConvert> selectedItems = FXCollections.observableArrayList();
    private final SetProperty<FileConvert> selectedItems = new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));
    @FXML
    private GridPane gpCommonGrid;
    @FXML
    private CheckBox cbDocumentImportShowPdfEnabled;
    @FXML
    private LabeledDatePicker dpDocumentDate;

//    public ObservableList<FileConvert> getSelectedItems() {
//        return selectedItems;
//    }
    public File getLastDirectory() {
        String path = CpxClientConfig.instance().getUserRecentFileChooserPath();
        if (path == null || path.trim().isEmpty()) {
            return null;
        }
        File file = new File(path);
        while (file.isFile()) {
            file = file.getParentFile();
        }
        return file;
    }

    @FXML
    private void importDocument(ActionEvent event) {
        importDocument(selectedFile, false);
    }

    private boolean importDocument(final FileConvert pSelectedFile, final boolean pMultiFileImport) {
        final FileConvert fileConvert = pSelectedFile;
        if (fileConvert == null) {
            return false;
        }
        if (!WmDocumentSection.checkFileSize(fileConvert.file)) {
            return false;
        }
        String workflowNumber = StringUtils.trimToEmpty(fileConvert.getWorkflowNumber());
        final Long caseId = fileConvert.getSelectedCaseId();
        final Long processId = workflowNumber.isEmpty() ? null : fileConvert.getWorkflowID(workflowNumber);
        final Long patientId = fileConvert.getSelectedPatientId();

        if (caseId == null) {
            throw new IllegalArgumentException("selected item is null!");
        }

        final TWmProcess process;
        final TPatient patient;
        final TCase cs = singleCaseEjb.get().findSingleCaseForIdForDocumentImport(caseId);
        if (cs == null) {
            throw new IllegalStateException("cannot find case with id " + caseId + ". Maybe it was deleted meantime?");
        }
        final ProcessServiceFacade facade = new ProcessServiceFacade(-1L);
//        final Window parentWindow = getScene().getWindow();

        final boolean isNewProcess;
        if (processId == null) {
            //create a new process
            isNewProcess = true;
            process = new TWmProcessHospital();
            process.setWorkflowType(WmWorkflowTypeEn.statKH);
            process.setWorkflowState(WmWorkflowStateEn.offen);
            process.setCreationUser(Session.instance().getCpxUserId());
            process.setCreationDate(new Date());
            patient = singleCaseEjb.get().findPatientForId(patientId);
            if (patient == null) {
                throw new IllegalStateException("cannot find patient with id " + patientId + ". Maybe it was deleted meantime?");
            }
            process.setPatient(patient);
            process.setMainCase(cs);
            facade.setCurrentProcess(process);
            //proc.set
        } else {
            //load already existing process
            //stupid, because who guarantees that result is TWmProcessHospital instead of TWmProcessInsurance??
            isNewProcess = false;
            facade.loadProcess(processId, true);
            process = facade.getCurrentProcess();
            process.setModificationUser(Session.instance().getCpxUserId());
            process.setModificationDate(new Date());
            process.setLastModificationDate(new Date());
            //TWmProcess p = processServiceBean.get().findSingleProcessForId(processId);
            if (process == null) {
                throw new IllegalStateException("cannot find process with id " + processId + ". Maybe it was deleted meantime?");
            }
//            if (!p.isProcessHospital()) {
//                throw new IllegalStateException("process with id " + processId + " is not a hospital process. Insurance process is not supported yet!");
//            }
            patient = process.getPatient();
        }

        final boolean isNewCase;
        //add case to process
        TWmProcessCase processCase = process.getProcessCase(cs);
        if (processCase == null) {
            //add new case to process
            isNewCase = true;
            final boolean mainCaseFl = process.getMainProcessCase() == null;
            processCase = process.addCase(cs, mainCaseFl);
            TWmEvent newCaseEvent = createNewEvent(process);
            newCaseEvent.setHosCase(cs);
            newCaseEvent.setEventType(WmEventTypeEn.caseAdded);
        } else {
            isNewCase = false;
        }

        //add request (open request dialog) and add request event (optional)
        WmRequestTypeEn requestType = cbRequestType.isVisible() ? cbRequestType.getSelectedItem() : null;
        if (requestType != null) {
            try {
                AddRequestDialog requestDlg = new AddRequestDialog(facade, caseId, requestType);
                //requestDlg.setRequestType(requestType);
                requestDlg.setSave(false);
                final Button buttonFile = new Button(fileConvert.file.getName());
                buttonFile.setMnemonicParsing(false);
                buttonFile.setGraphic(fileConvert.getDocTypeGlyph());
                buttonFile.setOnAction((evt) -> {
                    showFile(fileConvert.file);
                });
                //buttonFile.setMaxWidth(300d);

                Tooltip.install(buttonFile, new Tooltip("Dokument öffnen"));
//                HBox box = new HBox();
//                box.getChildren().add(new Label(requestDlg.getDialogPane().getHeaderText()));
//                box.getChildren().add(linkFile);
                requestDlg.setGraphic(buttonFile);
//                requestDlg.getDialogPane().setHeader(box);
                Optional<ButtonType> res = requestDlg.showAndWait();
                if (res.isPresent() && !res.get().equals(ButtonType.OK)) {
                    //aborted
                    return false;
                }
                TWmRequest request = requestDlg.onSave(true);
                ((TWmProcessHospital) process).getRequests().add(request);
                request.setProcessHospital((TWmProcessHospital) process);

                TWmEvent newRequestEvent = createNewEvent(process);
                if (request.isRequestMdk()) {
//                    request.getRisks().add(facade.createNewRequestRisk(request));
                    List<TWmReminder> reminders = requestDlg.getMdkReminders();
                    if (reminders != null) {
                        for (int i = 0; i < reminders.size(); i++) {
                            reminders.get(i).setProcess(process);
                            TWmEvent newReminderEvent = createNewEvent(process);
                            newReminderEvent.setEventType(WmEventTypeEn.reminderCreated);
                            newReminderEvent.setRequest(request);
                            newReminderEvent.setReminder(reminders.get(i));
                        }
                    }
                    try {
                        if(processServiceBean.get().isSendMDStateToSAPActive()) {
                            processServiceBean.get().sendMDStateToSAP(request);
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "MD-State could not be send to SAP", e);
                        MainApp.showErrorMessageDialog("Der MD-Status konnte nicht an SAP übermittelt werden.");
                    }
                }
                if (request.isRequestAudit()) {
                    List<TWmReminder> reminders = requestDlg.getAuditReminders();
                    if (reminders != null) {
                        for (int i = 0; i < reminders.size(); i++) {
                            reminders.get(i).setProcess(process);
                            TWmEvent newReminderEvent = createNewEvent(process);
                            newReminderEvent.setEventType(WmEventTypeEn.reminderCreated);
                            newReminderEvent.setRequest(request);
                            newReminderEvent.setReminder(reminders.get(i));
                        }
                    }
                }
                newRequestEvent.setEventType(WmEventTypeEn.requestCreated);
                newRequestEvent.setRequest(request);
            } catch (CpxIllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "Cannot create new request", ex);
                MainApp.showErrorMessageDialog(ex);
                return false;
            }
            //TWmRequest request = new TWm
        } else {
            if (!pMultiFileImport) {
                AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie das Dokument " + fileConvert.file.getName() + " jetzt " + (!isNewProcess ? "zum Vorgang " + String.valueOf(process.getWorkflowNumber()) : "") + " importieren?"
                        + (isNewProcess ? "\n\nEs wird ein neuer Vorgang angelegt" : "")
                        + (isNewCase ? "\n\nDer Fall " + cs.getCsHospitalIdent() + "/" + cs.getCsCaseNumber() + " wird dem Vorgang hinzugefügt" : ""));
                dlg.initModality(Modality.WINDOW_MODAL);
                Optional<ButtonType> res = dlg.showAndWait();
                if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                    //
                } else {
                    //don't save
                    return false;
                }
            }
        }

        //add document with document event (mandatory)
        TWmDocument doc = new TWmDocument();
        doc.setFilePath(fileConvert.file.getAbsolutePath());
        doc.setName(fileConvert.file.getName());
        doc.setDocumentType(cbDocumentType.getSelectedItem().getWmDtInternalId());
        doc.setDocumentDate(dpDocumentDate.getDate());
//        doc.setDocumentType(cbDocumentType.getSelectedItem().toString());
        fileConvert.file.length();
        doc.setContent(DocumentManager.getFileContent(fileConvert.file));
        doc.setProcess(process);
        process.getDocuments().add(doc);
        TWmEvent newDocEvent = createNewEvent(process);
        newDocEvent.setDocument(doc);
        newDocEvent.setEventType(WmEventTypeEn.documentAdded);

        //add action with action event (optional)
        CWmListActionSubject actionType = cbActionType.getSelectedItem();
        if (actionType != null) {
            TWmAction action = new TWmAction();
            action.setComment(actionComment.get() == null ? null : actionComment.get().toCharArray());
            //action.setActionType(actionType.getWmAsInternalId());
            action.setActionType(actionType.getWmAsInternalId());
            action.setProcess(process);
            TWmEvent newActionEvent = createNewEvent(process);
            newActionEvent.setEventType(WmEventTypeEn.actionAdded);
            newActionEvent.setAction(action);
        }

        //add reminder with reminder event (optional)
        Long reminderAssignedUserId = getUserID(txtReminderAssignedTo.getText());
        if (reminderAssignedUserId != null && !reminderAssignedUserId.equals(0L)) {
            CWmListReminderSubject reminderType = cbReminderType.getSelectedItem();
            TWmReminder reminder = new TWmReminder();
            reminder.setCreationDate(new Date());
            reminder.setCreationUser(Session.instance().getCpxUserId());
            //reminder.setComment(reminderComment.get() == null ? null : reminderComment.get().toCharArray());
            reminder.setComment(reminderComment.get());
            //reminder.setSubject(reminderType == null ? null : reminderType.getWmRsInternalId());
            reminder.setSubject(reminderType == null ? -1L : reminderType.getWmRsInternalId());
            reminder.setProcess(process);
            reminder.setAssignedUserId(reminderAssignedUserId);
            reminder.setDueDate(dtReminderDueDate.getDate());
            TWmEvent newReminderEvent = createNewEvent(process);
            newReminderEvent.setEventType(WmEventTypeEn.reminderCreated);
            newReminderEvent.setReminder(reminder);
        }

        //save process
        TWmProcess newProcess;
        try {
//            DeadlineList listofDeadlines = new DeadlineList(Session.instance().getEjbConnector().connectProcessServiceBean().get().getAllDeadlines());
            //20191230-AWi: is this intended? only checking if bill is past 6 weeks?
            facade.checkBillFor6WeeksDeadline(cs);
            newProcess = processServiceBean.get().storeProcessForDocumentImport(process);
//            facade.createOrUpdateRiskData();
            Platform.runLater(() -> {
                boolean result = selectedItems.remove(pSelectedFile) || !pMultiFileImport;
                if (DocumentImportedEn.NO_IMPORT.equals(fileConvert.getController().getImportedProperty().get()) && result) {
                    final DocumentImportedEn docImportEn = pMultiFileImport ? DocumentImportedEn.MULTI_FILE_IMPORT : DocumentImportedEn.SINGLE_FILE_IMPORT;
                    fileConvert.getController().getImportedProperty().set(docImportEn);
                }
            });

//            fileConvert.setImported(true);
            //importedProperty.set(true);
            if (!pMultiFileImport) {
                AlertDialog dlg = AlertDialog.createConfirmationDialog("Das Dokument '" + fileConvert.file.getName() + "' wurde dem " + (isNewProcess ? "neu angelegten " : "") + "Vorgang " + String.valueOf(newProcess.getWorkflowNumber()) + " erfolgreich hinzugefügt\n\nMöchten Sie den Vorgang jetzt öffnen?");
                dlg.initModality(Modality.WINDOW_MODAL);
                Optional<ButtonType> res = dlg.showAndWait();
                if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                    MainApp.getToolbarMenuScene().reopenProcess(newProcess.id);
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Was not able to store process " + String.valueOf(process), ex);
            MainApp.showErrorMessageDialog(ex, "Der Vorgang konnte nicht gespeichert werden");
        }

        //facade.saveOrUpdateProcess(process);        
        return true;
    }

    public TWmEvent createNewEvent(final TWmProcess process) {
        TWmEvent newEvent = new TWmEvent();
//        newEvent.setCreationUserId(Session.instance().getCpxUserId());
        newEvent.setCreationUser(Session.instance().getCpxUserId());
        newEvent.setProcess(process);
        newEvent.setCreationDate(new Date());
        process.getEvents().add(newEvent);
        return newEvent;
    }

//    private BooleanBinding selectedInvalidBinding() {
//        return Bindings.when(fileTree.getSelectionModel().selectedItemProperty().isNull())
//                .then(true)
//                .otherwise(Bindings.selectBoolean(fileTree.getSelectionModel().selectedItemProperty().getValue().getValue().invalidProperty(), "type"));
////        return fileTree.getSelectionModel().selectedItemProperty().isNull().or(
////                fileTree.getSelectionModel().selectedItemProperty().isNotEqualTo(new SimpleObjectProperty<Object>(null)).or(
////                fileTree.getSelectionModel().selectedItemProperty().getValue().getValue().invalidProperty())
////        );
////        return Bindings.and(fileTree.getSelectionModel().selectedIndexProperty().greaterThan(-1),
////                fileTree.getSelectionModel().selectedItemProperty().getValue().getValue().invalidProperty()
////        ).not();
//
////        return Bindings.createBooleanBinding(() -> {
////            return fileTree.getSelectionModel().getSelectedItem() == null
////                    ? true
////                    : fileTree.getSelectionModel().getSelectedItem().getValue().isInvalid();
////        }, fileTree.getSelectionModel().selectedItemProperty());
//    }
    /**
     * Initializes the controller class.
     *
     * @param url url
     *
     *
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpLanguage();
        if(!Session.instance().getRoleProperties().isEditActionAllowed()){
           cbActionType.getControl().disableProperty().setValue(Boolean.TRUE);
           editActionComment.disableProperty().setValue(Boolean.TRUE);
        }
        File lastPath = getLastDirectory();
        if (lastPath != null) {
            txtSelectedFolder.setText(lastPath.getAbsolutePath());
        }

        if (!viewerFile.exists() || !viewerFile.isFile()) {
            LOG.log(Level.SEVERE, "WebView is broken! Path to PDF.js viewer file seems to be illegal or does not exist: {0}", viewerFile.getAbsolutePath());
        }

        final Thread thDocDetection = new Thread(() -> {
            final boolean wordFound = DocumentReader.WORD_FOUND;
            Platform.runLater(() -> {
                cbDocumentImportOfficeEnabled.setDisable(!wordFound);
                cbDocumentImportOfficeEnabled.setSelected(wordFound ? Session.instance().isDocumentImportDetection() : false);
                DocumentReader.setOfficeEnabled(cbDocumentImportOfficeEnabled.isSelected());
            });
        });
        thDocDetection.start();

        glFolderInExplorer.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.EXTERNAL_LINK));
        glRefreshFolder.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.REFRESH));

        btnImportSelected.visibleProperty().bind(selectedItems.sizeProperty().greaterThan(1));
        //btnImportSelected.disableProperty().bind(selectedInvalidBinding());
        selectedItems.addListener((ObservableValue<? extends ObservableSet<FileConvert>> ov, ObservableSet<FileConvert> oldValue, ObservableSet<FileConvert> newValue) -> {
            btnImportSelected.setText(selectedItems.size() + " ausgewählte Dokumente importieren");
        });

        btnImport.visibleProperty().bind(btnImportSelected.visibleProperty().not());

        cbRequestType.visibleProperty().bind(btnImport.visibleProperty());

//        cbRequestType.visibleProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!newValue) {
//                    //clear request type when multiple documents are selected
//                    cbRequestType.clear();
//                }
//            }
//        });
        fileName.setText(null);
        //fileTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileTree.setCellFactory((TreeView<FileConvert> param) -> {
            TreeCell<FileConvert> treeCell = new TreeCell<FileConvert>() {
                @Override
                protected void updateItem(FileConvert fileConvert, boolean empty) {
                    super.updateItem(fileConvert, empty);
                    //                        setDisclosureNode(null);
                    HBox box = new HBox();
                    box.setAlignment(Pos.CENTER_LEFT);
                    if (fileConvert != null && fileConvert.file != null) {
                        final Glyph docTypeGlyph = fileConvert.getDocTypeGlyph();
                        final Label label = new Label();
                        FileConvert rootValue = fileTree.getRoot().getValue();
                        if (rootValue == fileConvert) {
                            if (rootValue.file.getParentFile() != null) {
                                //label.setText(rootValue.file.getName());
                                label.setText("..");
                            } else {
                                label.setText(rootValue.file.getAbsolutePath());
                            }
                        } else {
                            label.setText(fileConvert.file.getName());
//                                if (fileConvert.file.isDirectory()) {
//                                    //int count = filterFiles(fileConvert.file).length;
//                                    int count = fileConvert.file.listFiles().length;
//                                    label.setText(fileConvert.file.getName() + " (" + count + ")");
//                                } else {
//                                    label.setText(fileConvert.file.getName());
//                                }
                        }
                        //label.setPrefWidth(Double.MAX_VALUE);
                        label.setGraphic(docTypeGlyph);
//                            if (fileConvert.progressIndicator != null) {
//                                //pi.setPrefSize(10d, 10d);
//                                //pi.maxWidthProperty().bind(glyph.prefWidthProperty());
//                                //pi.maxHeightProperty().bind(label.prefHeightProperty());
//                                fileConvert.progressIndicator.setMaxHeight(15d);
//                                fileConvert.progressIndicator.setMaxWidth(15d);
//                                //fileConvert.progressIndicator.setPrefWidth(0d);
//                                box.getChildren().add(fileConvert.progressIndicator);
//                            }
//Pane glyphPane = fileConvert.getResultGlyph();
                        Pane statusPane = fileConvert.getStatusPane();
                        if (statusPane != null) {
                            box.getChildren().add(statusPane);
                        }
                        box.getChildren().add(label);
//                            if (fileConvert.caseNumberLabel != null) {
//                                fileConvert.caseNumberLabel.setAlignment(Pos.CENTER);
//                                box.getChildren().add(fileConvert.caseNumberLabel);
//                            }
                    }
//                        if (fileConvert != null) {
//                            fileConvert.checkBox.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    //TreeItem<FileConvert> item = fileTree.getSelectionModel().getSelectedItem();
//                                    //TreeItem<FileConvert> item = fileTree.getSelectionModel().getSelectedItem();
//                                    int index = getIndexOf(fileConvert) + 1;
//                                    if (fileConvert.checkBox.isSelected()) {
//                                        fileTree.getSelectionModel().select(index);
//                                    } else {
//                                        fileTree.getSelectionModel().clearSelection(index);
//                                    }
//                                }
//                            });
//                        }
                    if (fileConvert != null) {
                        if (fileConvert.checkBox.isSelected()
                                && DocumentImportedEn.NO_IMPORT.equals(fileConvert.getController().getImportedProperty().get())) {
                            //this.setStyle("-fx-background-color: orange;");
                            this.setStyle("-fx-font-weight: bold;");
                            selectedItems.add(fileConvert);
                        } else {
                            this.setStyle("");
                            selectedItems.remove(fileConvert);
                        }
                        fileConvert.checkBox.setOnAction((ActionEvent t) -> {
                            fileTree.refresh();
                        });
                        fileConvert.checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
//                                if (newValue) {                                    
//                                    fileConvert.getController().presetCase();
//                                }
                        });
                    }
                    setGraphic(box);
                }
            };
            return treeCell;
        });

        Glyph reminderCommentGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.EDIT);
        Glyph actionCommentGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.EDIT);

        editReminderComment.setGraphic(reminderCommentGlyph);
        editActionComment.setGraphic(actionCommentGlyph);
        editReminderComment.setTooltip(new Tooltip(Lang.getComment() + "\n bis " + SIZE_REMINDER_COMMENT + " Zeichnen"));
        editActionComment.setTooltip(new Tooltip(Lang.getComment() + "\n bis " + SIZE_ACTION_COMMENT + " Zeichnen"));

        editReminderComment.disableProperty().bind(cbReminderType.disableProperty());

//        fileTree.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<FileConvert>> ov, TreeItem<FileConvert> t, TreeItem<FileConvert> t1) -> {
//            List<Integer> indexes = new ArrayList<>(fileTree.getSelectionModel().getSelectedIndices());
//            final ArrayList<TreeItem<FileConvert>> treeItems = new ArrayList<>(fileTree.getRoot().getChildren());
//            for (int i = 0; i < treeItems.size(); i++) {
//                TreeItem<FileConvert> treeItem = treeItems.get(i);
//                boolean selected = indexes.contains(i + 1);
//                treeItem.getValue().checkBox.setSelected(selected);
//            }
//        });
//        fileTree.getSelectionModel().selectedItemProperty().addListener((observable) -> {
//            if (fileTree.getSelectionModel().getSelectedItem() == null) {
//                return;
//            }
//            Platform.runLater(() -> {
//                FileConvert fc = fileTree.getSelectionModel().getSelectedItem().getValue();
//                if (fc.file.isFile()) {
//                    selectFile(fc);
//                } else if (fileTree.getRoot().getValue() == fc) {
//                    //is root
//                    if (fc.file.getParentFile() != null) {
//                        String dir = fc.file.getParentFile().getAbsolutePath();
//                        fileTree.getSelectionModel().clearSelection();
//                        fileTree.setRoot(null);
//                        txtSelectedFolder.setText(dir);
//                        setFolder(dir);
//                    }
//                } else {
//                    //is subdirectory
//                    String dir = fc.file.getAbsolutePath();
//                    txtSelectedFolder.setText(dir);
//                    setFolder(dir);
//                }
//            });
//        });
//        fileTree.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeItem.TreeModificationEvent<FileConvert> event) -> {
//            TreeItem<FileConvert> item = event.getTreeItem();
//            if (item == null) {
//                return;
//            }
//            if (item.getValue().isRoot()) {
//                item.setExpanded(true);
//            }
//        });
//
//        fileTree.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent<FileConvert> event) -> {
//            TreeItem<FileConvert> item = event.getTreeItem();
//            if (item == null) {
//                return;
//            }
//            if (!item.getValue().isRoot()) {
//                item.setExpanded(false);
//            }
//        });
//        fileTree.setOnMousePressed((MouseEvent event) -> {
//            event.consume();
//        });
//        fileTree.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
//            //if (e.getClickCount() % 2 == 0 && e.getButton().equals(MouseButton.PRIMARY)) {
//            TreeItem<FileConvert> item = fileTree.getSelectionModel().getSelectedItem();
//            if (item == null) {
//                return;
//            }
//            if (!item.getValue().isRoot()) {
//                item.setExpanded(false);
//            } else {
//                item.setExpanded(true);
//            }
//            //e.consume();
//            //}
//        });
        glClear.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ERASER));
        glClear.setTooltip(new Tooltip("Felder zurücksetzen"));
        glClear.setCursor(Cursor.HAND);
        glClear.setOnMouseClicked((event) -> {
            if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 1) {
                cbDocumentType.clear();
                dpDocumentDate.setDate(null);
                cbRequestType.clear();
                txtReminderAssignedTo.clear();
                cbReminderType.clear();
                dtReminderDueDate.setDate(null);
                reminderComment.set(null);
                cbActionType.clear();
                actionComment.set(null);
                if(!Session.instance().getRoleProperties().isEditActionAllowed()){
                    cbActionType.getControl().disableProperty().setValue(Boolean.TRUE);
                    editActionComment.disableProperty().setValue(Boolean.TRUE);
                }

            }
        });

        //disable collapse/expand of tree items
        fileTree.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            Node n = e.getPickResult().getIntersectedNode();
            if (n != null && (n.getStyleClass().contains("arrow")
                    || n.getStyleClass().contains("tree-disclosure-node"))) {
                e.consume();
            }
//            else {
//                System.out.println("SOURCE: " + e.getSource());
//                System.out.println("TARGET: " + e.getTarget());
//                if (n != null) {
//                    System.out.println(n);
//                    n.getStyleClass().forEach((t) -> {
//                        System.out.println(t);
//                    });
//                }
//            }
//            if (n instanceof StackPane) {
//                StackPane sp = (StackPane) n;
//                if (sp.getStyleClass().contains("arrow")) {
//                    e.consume();
//                }
//            }
            //System.out.println(n);
        });

//        fileTree.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
//            e.consume();
//        });        
        fileTree.setOnMouseClicked((MouseEvent mouseEvent) -> {
            mouseEvent.consume();
            TreeItem<FileConvert> item = fileTree.getSelectionModel().getSelectedItem();
            if (item == null) {
                return;
            }
            FileConvert fc = item.getValue();
            if (fc.isFile() || (fc.isDirectory() && mouseEvent.getClickCount() == 2)) {

                Platform.runLater(() -> {
                    //FileConvert fc = fileTree.getSelectionModel().getSelectedItem().getValue();
                    if (fc.file.isFile()) {
                        selectFile(fc);
                    } else if (fileTree.getRoot().getValue() == fc) {
                        //is root
                        if (fc.file.getParentFile() != null) {
                            String dir = fc.file.getParentFile().getAbsolutePath();
                            fileTree.getSelectionModel().clearSelection();
                            fileTree.setRoot(null);
                            txtSelectedFolder.setText(dir);
                            setFolder(dir);
                        }
                    } else {
                        //is subdirectory
                        String dir = fc.file.getAbsolutePath();
                        txtSelectedFolder.setText(dir);
                        setFolder(dir);
                    }
                });

            }
        });

        for (int i = 1; i <= threads.length; i++) {
            DocumentConverterThread th = new DocumentConverterThread();
            th.setName("Document Converter Thread #" + i);
            threads[i - 1] = th;
            //th.setDaemon(true);
            th.start();
        }

        apRoot.sceneProperty().addListener((observable) -> {
            //focus on folder edit field, so that you just have to press enter to open recent directory
            txtSelectedFolder.requestFocus();
            txtSelectedFolder.positionCaret(txtSelectedFolder.getLength());
        });
//        getScene().getWindow().addEventHandler(WindowEvent.WINDOW_SHOWING, new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent window) {
//            }
//        });

        splitPane.setOnDragDetected((event) -> {
            Dragboard db = splitPane.startDragAndDrop(TransferMode.ANY);
            db.getFiles();
        });

//        splitPane.setOnDragDropped((event) -> {
//            Dragboard db = splitPane.startDragAndDrop(TransferMode.ANY);
//            db.getFiles();
//        });
        splitPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(final DragEvent event) {
                mouseDragOver(event);
            }

            private void mouseDragOver(DragEvent e) {
                //final Dragboard db = e.getDragboard();
                final Dragboard db = e.getDragboard();

//                System.out.println(db.getFiles().size());
//                System.out.println(db.getString());
//                System.out.println(db.getUrl());
//                System.out.println(db.getImage());
//                System.out.println(db.getRtf());
                //db.getContentTypes().forEach(df -> System.out.println(df + " - " + db.getContent(df)));
                boolean isAccepted = false;
                for (File file : db.getFiles()) {
                    //System.out.println(file.getName());
                    Utils.FILE_TYPES fileType = Utils.getFileType(file);
                    if (!fileType.isOther()) {
                        //e.acceptTransferModes(TransferMode.ANY);
                        isAccepted = true;
                        e.acceptTransferModes(TransferMode.COPY);
                        break;
                    }
                }

                if (!isAccepted) {
                    final List<String> fileNames = DocumentManager.getSelectedOutlookAttachments(db);
                    //LOG.log(Level.INFO, "found outlook files: " + fileNames.size());
                    if (!fileNames.isEmpty()) {
//                        isAccepted = true;
                        e.acceptTransferModes(TransferMode.COPY);
                    }
                }

//                DataFormat df = DataFormat.lookupMimeType("CSV");
//                ByteBuffer buffer = (ByteBuffer) db.getContent(df);
//                //String v = new String(buffer, Charset.forName("UTF-8"));
//                String s = StandardCharsets.US_ASCII.decode(buffer).toString();
//                try {
//                    FileUtils.writeStringToFile(new File("E:\\TEMP\\test.csv"), s, Charset.forName("UTF-8"));
//                } catch (IOException ex) {
//                    Logger.getLogger(DocumentImportFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//                }
                e.consume();
            }
        });
        splitPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(final DragEvent event) {
                mouseDragDropped(event);
            }

            private void mouseDragDropped(DragEvent e) {
                final Dragboard db = e.getDragboard();
                boolean success = false;
                final Window parentWindow = getParentWindow();
                List<TreeItem<FileConvert>> treeItems = new ArrayList<>();
                for (File file : db.getFiles()) {
                    Utils.FILE_TYPES fileType = Utils.getFileType(file);
                    if (!fileType.isOther()) {
                        success = true;
                        TreeItem<FileConvert> treeItem;
                        if ((treeItem = findFileConvert(file)) == null) {
                            FileConvert fc = new FileConvert(parentWindow, file, false, ++increment);
                            //System.out.println(fc.priority + "   " + fc.file.getName());
                            converterQueue.add(fc);
                            treeItem = newTreeItem(fc);
                            treeItem.setExpanded(false);
                            //treeItem.setLeaf(true);
                            fileTree.getRoot().getChildren().add(treeItem);
                        }
                        treeItems.add(treeItem);
                    }
                }

                if (!success) {
                    final List<String> fileNames = DocumentManager.getSelectedOutlookAttachments(db);
                    if (!fileNames.isEmpty()) {
                        //LOG.log(Level.INFO, "found outlook files: " + fileNames.size());
                        success = true;
//                        DocumentReader reader = new DocumentReader();
                        for (File file : DocumentReader.getOutlookAttachments(fileNames)) {
                            TreeItem<FileConvert> treeItem;
                            if ((treeItem = findFileConvert(file)) == null) {
                                FileConvert fc = new FileConvert(parentWindow, file, false, ++increment);
                                //System.out.println(fc.priority + "   " + fc.file.getName());
                                converterQueue.add(fc);
                                treeItem = newTreeItem(fc);
                                treeItem.setExpanded(false);
                                //treeItem.setLeaf(true);
                                fileTree.getRoot().getChildren().add(treeItem);
                            }
                            treeItems.add(treeItem);
                        }
                    }
                }
                if (!treeItems.isEmpty()) {
//                    int[] indexes = new int[treeItems.size()];
//                    for(int i = 0; i < treeItems.size(); i++) {
//                        TreeItem<FileConvert> item = treeItems.get(i);
//                        int idx = getIndexOf(item.getValue());
//                        indexes[i] = idx;
//                    }
                    fileTree.getSelectionModel().clearSelection();
                    TreeItem<FileConvert> firstItem = treeItems.iterator().next();
                    int idx = getIndexOf(firstItem.getValue()) + 1;
                    fileTree.getSelectionModel().selectIndices(idx);
                    selectFile(firstItem.getValue());
                    fileTree.scrollTo(idx);
//                    for (TreeItem<FileConvert> item : treeItems) {
//                        //TreeItem<FileConvert> firstItem = treeItems.iterator().next();
//                        int idx = getIndexOf(item.getValue()) + 1;
//                        fileTree.getSelectionModel().selectIndices(idx);
//                        fileTree.scrollTo(idx);
//                    }
                }
                e.setDropCompleted(success);
                e.consume();
            }
        });

        setupValidators();

//        dtReminderDueDate.setDate(new Date());
        //List<CWmListDocumentType> documentTypes = processServiceBean.get().getAllDocumentTypeObjects(new Date());
        List<CWmListDocumentType> documentTypes = MenuCache.getMenuCacheDocumentTypes().values();
        Collections.sort(documentTypes);
        dpDocumentDate.setDate(new Date());
        cbDocumentType.setItems(documentTypes);
        cbDocumentType.setConverter(new StringConverter<CWmListDocumentType>() {
            @Override
            public String toString(CWmListDocumentType object) {
                return object == null ? "" : object.getWmDtName();
            }

            @Override
            public CWmListDocumentType fromString(String string) {
                return null;
            }
        });

        //List<CWmListActionSubject> actionTypes = new ArrayList<>(processServiceBean.get().getAllActionSubjectObjects(new Date()));
        List<CWmListActionSubject> actionTypes = MenuCache.getMenuCacheActionSubjects().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE);
        Collections.sort(actionTypes);
        //actionTypes.add(0, null);
        cbActionType.setItems(actionTypes);
        cbActionType.setConverter(new StringConverter<CWmListActionSubject>() {
            @Override
            public String toString(CWmListActionSubject object) {
                return object == null ? "" : object.getWmAsName();
            }

            @Override
            public CWmListActionSubject fromString(String string) {
                return null;
            }
        });

        //ArrayList<CWmListReminderSubject> reminderTypes = new ArrayList<>(processServiceBean.get().getAllReminderSubjects(new Date()));
        List<CWmListReminderSubject> reminderTypes = MenuCache.getMenuCacheReminderSubjects().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE);
        Collections.sort(reminderTypes);
        //reminderTypes.add(0, null);
        cbReminderType.setItems(reminderTypes);
        cbReminderType.setConverter(new StringConverter<CWmListReminderSubject>() {
            @Override
            public String toString(CWmListReminderSubject object) {
                return object == null ? "" : object.getWmRsName();
            }

            @Override
            public CWmListReminderSubject fromString(String string) {
                return null;
            }
        });

        List<WmRequestTypeEn> requestTypes = new ArrayList<>(Arrays.asList(WmRequestTypeEn.values()));
        Collections.sort(requestTypes);
        //requestTypes.add(0, null);
        cbRequestType.setItems(FXCollections.observableArrayList(requestTypes));
        cbRequestType.setConverter(new StringConverter<WmRequestTypeEn>() {
            @Override
            public String toString(WmRequestTypeEn object) {
                return object == null ? "" : object.getTranslation().getValue();
            }

            @Override
            public WmRequestTypeEn fromString(String string) {
                return null;
            }
        });

        AutoCompletionBinding<UserDTO> user = TextFields.bindAutoCompletion(
                txtReminderAssignedTo.getControl(), 
                (
                        AutoCompletionBinding.ISuggestionRequest param) -> (
                        Session.instance().getRoleProperties().isEditReminderOfOtherUserAllowed()?
                        MenuCache.getMenuCacheUsers().getValidMatchForUser(param.getUserText(), new Date()):
                                MenuCache.getMenuCacheUsers().getValidMatchForUser(Session.instance().getCdbUser().getName(), new Date()))
        /* processServiceBean.get().getMatchingUsers(param.getUserText(), new Date()) */);
        user.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<UserDTO> event) -> {
            //System.out.println(event.getCompletion());
            txtReminderAssignedTo.setUserData(event.getCompletion());
//                cbAssignedUser.setText(cbAssignedUser.getText());
//                        cbAssignedUser.setText(String.valueOf(mReminder.getAssignedUserId()));
//mReminder.setAssignedUserId(cbAssignedUser.getText());
        });
    }

    private void setupValidators() {
        validationSupport.initInitialDecoration();

//        Platform.runLater(() -> {
        //final BooleanProperty caseNumberValidProp = new SimpleBooleanProperty();
        validationSupport.registerValidator(cbDocumentType.getControl(), (Control t, CWmListDocumentType u) -> {
            ValidationResult res = new ValidationResult();
            //if (u != null) {
            res.addErrorIf(t, "Dokumenttyp fehlt", cbDocumentType.getSelectedItem() == null);
            //}
            return res;
        });

//            validationSupport.registerValidator(dtReminderDueDate.getControl(), (Control t, LocalDate u) -> {
//                ValidationResult res = new ValidationResult();
//                //if (u != null) {
//                res.addErrorIf(t, "Ablaufdatum fehlt", dtReminderDueDate.getDate() == null);
//                //}
//                return res;
//            });
        validationSupport.registerValidator(txtReminderAssignedTo.getControl(), (Control t, String u) -> {
            ValidationResult res = new ValidationResult();
            if (!StringUtils.trimToEmpty(u).isEmpty()) {
                //res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), u == null || u.isEmpty());
                res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), getUserID(u) == null);
            }
            return res;
        });
//        });
    }

    private void createContextMenu(final DocumentViewer docViewer) {
        final WebView webView = docViewer.getWebView();
        webView.setContextMenuEnabled(false);

        final ContextMenu contextMenu = new ContextMenu();
        final MenuItem copy = new MenuItem("Kopieren");
        copy.setOnAction((event) -> {
            final String selection = docViewer.getSelectedText();
            ClipboardEnabler.copyToClipboard(selection);
//            final Clipboard clipboard = Clipboard.getSystemClipboard();
//            final ClipboardContent content = new ClipboardContent();
//            content.putString(selection);
            //content.putHtml("<b>Some</b> text");
//            clipboard.setContent(content);
        });
        final MenuItem copyCaseNumber = new MenuItem("X als Fallnummer übernehmen");
        copyCaseNumber.setOnAction((event) -> {
            final String selection = docViewer.getSelectedText();
            selectedFile.getController().setCaseNumber(selection);
//            selectedFile.setPatientNumber();
//            selectedFile.setCaseNumber();
//            selectedFile.setProcessNumber();
        });
        final MenuItem copyProcessNumber = new MenuItem("X als Vorgangsnummer übernehmen");
        copyProcessNumber.setOnAction((event) -> {
            final String selection = docViewer.getSelectedText();
            selectedFile.getController().setProcessNumber(selection);
//            selectedFile.setPatientNumber();
//            selectedFile.setCaseNumber();
//            selectedFile.setProcessNumber();
        });
        final MenuItem copyPatientNumber = new MenuItem("X als Patientennummer übernehmen");
        copyPatientNumber.setOnAction((event) -> {
            final String selection = docViewer.getSelectedText();
            selectedFile.getController().setPatientNumber(selection);
//            selectedFile.setPatientNumber();
//            selectedFile.setCaseNumber();
//            selectedFile.setProcessNumber();
        });
        contextMenu.getItems().addAll(copy, copyCaseNumber, copyProcessNumber, copyPatientNumber);

        webView.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                final String selection = docViewer.getSelectedText();
                final String sel = selection.length() > 10 ? selection.substring(0, 10).trim() + "..." : selection;

                copyCaseNumber.setText("'" + sel + "' als Fallnr. übernehmen");
                copyProcessNumber.setText("'" + sel + "' als Vorgangsnr. übernehmen");
                copyPatientNumber.setText("'" + sel + "' als Patientennr. übernehmen");

                copy.setDisable(selection.isEmpty());
                copyCaseNumber.setVisible(!selection.isEmpty() && selectedFile != null);
                copyProcessNumber.setVisible(!selection.isEmpty() && selectedFile != null);
                copyPatientNumber.setVisible(!selection.isEmpty() && selectedFile != null);
                contextMenu.show(webView, e.getScreenX(), e.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private TreeItem<FileConvert> findFileConvert(final File pFile) {
        if (pFile == null) {
            return null;
        }
        Iterator<TreeItem<FileConvert>> it = new ArrayList<>(fileTree.getRoot().getChildren()).iterator();
        while (it.hasNext()) {
            TreeItem<FileConvert> item = it.next();
            if (item == null) {
                continue;
            }
            FileConvert fc = item.getValue();
            if (fc == null) {
                continue;
            }
            File f = fc.file;
            if (f == null) {
                continue;
            }
            if (f.equals(pFile)) {
                return item;
            }
        }
        return null;
    }

    private void selectFile(final FileConvert pFileConvert) {
        if (pFileConvert == selectedFile) {
            //File is already selected
            return;
        }

        gpCommonGrid.setVisible(true);
        validationSupport.redecorate();

        btnImport.disableProperty().bind(
                Bindings.or(
                        validationSupport.invalidProperty(),
                        pFileConvert.invalidProperty()
                )
        );

        final Utils.FILE_TYPES pFileType = pFileConvert.fileType;
        selectedFile = pFileConvert;
        previewBox.getChildren().clear();
        try {
            docViewer = new DocumentViewer(viewerFile);
        } catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, "viewer file seems to be invalid: " + viewerFile.getAbsolutePath(), ex);
            return;
        }

        createContextMenu(docViewer);

        linkOriginal.setText(FilenameUtils.getExtension(pFileConvert.file.getName()).toUpperCase() + " öffnen");
        linkOriginal.setVisible(true);
        linkOriginal.setOnAction((event) -> {
            showFile(pFileConvert.file);
        });
        Tooltip.install(linkOriginal, new Tooltip(pFileConvert.file.getName()));
        fileName.setText(pFileConvert.file.getName());

        //Check mandatory fields to import multiple documents
        btnImportSelected.disableProperty().bind(
                documentTypeProperty().isNull()
        //                Bindings.or(
        //                        documentTypeProperty().isNull(),
        //                        documentDateProperty().isNull()
        //                )
        );

        DocumentConverterResult docResult = documents.get(pFileConvert.file);

        if (pFileType.isPdf() && cbDocumentImportShowPdfEnabled.isSelected()) {
            //load directly here for faster preview!
            showDocument(pFileConvert.file);
        } else if (pFileType.isText()) {
            showTxt(pFileConvert.file);
        }
        final WebView preview = docViewer.getWebView();
        previewBox.getChildren().add(preview);
        CpxFXMLLoader.setAnchorsInNode(preview);

        if (docResult != null && (docResult.pdfFile == null || docResult.pdfFile.exists() && docResult.pdfFile.length() > 0)) {
            loadDocument(docResult);
        } else {
            Label pleaseWaitLabel = new Label("Vorschau kommt gleich...");
            pleaseWaitLabel.setAlignment(Pos.CENTER);
            CpxFXMLLoader.setAnchorsInNode(pleaseWaitLabel);
            previewBox.getChildren().add(pleaseWaitLabel);
            if (docResult != null && (!docResult.pdfFile.exists() || docResult.pdfFile.length() == 0)) {
                //FileConvert fcNew = pFileConvert.getCopy();
                int newPriority = ++increment;
                pFileConvert.setPriority(newPriority);
                pFileConvert.setStatusProgress();
                if (converterQueue.remove(pFileConvert)) {
                    LOG.log(Level.FINEST, "file convert was removed from queue: {0}", pFileConvert);
                }
                converterQueue.add(pFileConvert);
            } else {
                //give selected file a higher priority for conversion
                Iterator<FileConvert> it = new HashSet<>(converterQueue).iterator();
                boolean found = false;
                while (it.hasNext()) {
                    FileConvert fc = it.next();
                    if (fc == pFileConvert) {
                        int newPriority = ++increment;
                        fc.setPriority(newPriority);
                        fc.setStatusProgress();
                        //you have to remove it and to add it again to achieve the goal of higher priorization
                        //it.remove();
                        if (converterQueue.remove(fc)) {
                            LOG.log(Level.FINEST, "file convert was removed from queue: {0}", fc);
                        }
                        converterQueue.add(fc);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    //ensure that same file is not processes in parallel by several threads
                    for (DocumentConverterThread th : threads) {
                        FileConvert fc = th.getFileConvert();
                        if (fc != null && fc == pFileConvert) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    //FileConvert fcNew = pFileConvert.getCopy();
                    int newPriority = ++increment;
                    pFileConvert.setPriority(newPriority);
                    pFileConvert.setStatusProgress();
                    if (converterQueue.remove(pFileConvert)) {
                        LOG.log(Level.FINEST, "file convert was removed from queue: {0}", pFileConvert);
                    }
                    converterQueue.add(pFileConvert);
                }
            }
        }

        lblFileSize.setVisible(true);
        lblFileSize.setText(pFileConvert.file.length() / 1024 + " KB");
        lblFileDate.setVisible(true);
        lblFileDate.setText(Lang.toDate(new Date(pFileConvert.file.lastModified())));

        glExplorer.setVisible(true);
        glExplorer.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.EXTERNAL_LINK));
        glExplorer.setOnMouseClicked((event) -> {
            if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 1) {
                ToolbarMenuFXMLController.openInExplorer(pFileConvert.file.getAbsolutePath());
            }
        });

        if (!headerDocument.getMenuItems().contains(glClear)) {
            headerDocument.addMenuItems(glClear);
        }
//        glClear.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ERASER));
//        glClear.setOnMouseClicked((event) -> {
//            if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 1) {
//                ToolbarMenuFXMLController.openInExplorer(pFileConvert.file.getAbsolutePath());
//            }
//        });

        UserPrincipal fileOwner = null;
        try {
            fileOwner = java.nio.file.Files.getOwner(pFileConvert.file.toPath());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot get file owner (maybe this function is not supported on this file system)!", ex);
        }
        lblFileOwner.setVisible(true);
        lblFileOwner.setText(fileOwner == null ? null : fileOwner.getName());

        //DocumentImportFieldsFXMLController fieldCtrl = new DocumentImportFieldsFXMLController();
        //DocumentImportFieldsScene scene = (DocumentImportFieldsScene) fieldCtrl.getScene();
//        if (!pFileConvert.hasScene()) {
        //                DocumentImportFieldsScene scene = new DocumentImportFieldsScene();
//                Pane root = (Pane) scene.getRoot();
//                stackDetails.getChildren().add(root);
//root.toFront();
//                DocumentImportFieldsFXMLController ctrl = scene.getController();
//                pFileConvert.setScene(scene);
//                pFileConvert.setController(ctrl);
        DocumentImportFieldsFXMLController ctrl = pFileConvert.getController();
        final ChangeListener<DocumentImportedEn> listener = (ObservableValue<? extends DocumentImportedEn> ov, DocumentImportedEn t, DocumentImportedEn t1) -> {
            archivateFile(pFileConvert, t1);
        };
//        boolean hasController = pFileConvert.hasController();
//        if (!hasController) {
        //remove imported file from file tree on the left
        ctrl.getImportedProperty().addListener(listener);
//        }
//        for(Node root: stackDetails.getChildren()) {
//            ((DocumentImportFieldsScene) ((Pane) root).getScene()).getController().disableValidation();
//        }
        for (TreeItem<FileConvert> item : fileTree.getRoot().getChildren()) {
            FileConvert fc = item.getValue();
            if (fc.hasController()) {
                fc.getController().disableValidation();
            }
        }
        Pane root = pFileConvert.getRoot();
        if (!stackDetails.getChildren().contains(root)) {
            stackDetails.getChildren().add(root);
        }
        root.toFront();
        ctrl.enableValidation();
        if (pFileConvert.getDocumentConverterResult() != null) {
            ctrl.setCases(pFileConvert.getCaseNumber(), pFileConvert.getCases(), pFileConvert.getPotentialCases());
            ctrl.setPatients(pFileConvert.getPatientNumber(), pFileConvert.getPatients(), pFileConvert.getPotentialPatients());
            //pFileConvert.setResultGlyph(ctrl.getGlyphCopy());
        }
//        } else {
//            pFileConvert.getRoot().toFront();
//        }
        linkPdf.setVisible(docResult != null && docResult.pdfFile != null && !pFileType.isPdf());
        linkTxt.setVisible(docResult != null && docResult.txtFile != null && !pFileType.isText() && !pFileType.isCsv());
    }

    @FXML
    private void editReminderCommentHandler(ActionEvent event) {
        editCommentHandler(editReminderComment, reminderComment, SIZE_REMINDER_COMMENT);
    }

    @FXML
    private void editActionCommentHandler(ActionEvent event) {
        editCommentHandler(editActionComment, actionComment, SIZE_ACTION_COMMENT);
    }

    private void editCommentHandler(final Button pCommentButton,
            final StringProperty pComment, final int pMaxSize) {
//        HBox hBox = new HBox();
        VBox vBox = new VBox();
//        Label label = new Label(Lang.getVersioncontrollVersionComment());
//        vBox.getChildren().addAll(label, hBox);
//        LimitedTextArea taComment = new LimitedTextArea(750);
//        taComment.setText(versionComment);
//        taComment.setEditable(true);
//        taComment.setWrapText(true);

        LabeledTextArea taComment = new LabeledTextArea(Lang.getComment(), pMaxSize);
        taComment.setText(pComment.get());
        taComment.setWrapText(true);
        taComment.setEditable(true);

        taComment.getControl().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //ignore focus gain
            if (newValue) {
                return;
            }
            if (pComment.get() == null) {
                pComment.set(taComment.getText());
            } else {
                if (pComment.get().equals(taComment.getText())) {
                    return;
                }
                pComment.set(taComment.getText());
            }
        });
//        DelayedFields.bindDelay(taComment.getControl(), new Callback<String, Boolean>() {
//            @Override
//            public Boolean call(String param) {
//                pComment.set(param.toString());
//                return true;
//            }
//        }, 2);
        vBox.getChildren().addAll(taComment);
        vBox.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
        PopOver popover = new AutoFitPopOver();
        popover.setHideOnEscape(true);
        popover.setAutoHide(true);
        popover.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popover.setDetachable(false);
        popover.setContentNode(vBox);
        popover.show(pCommentButton);
        popover.getContentNode().setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                popover.hide(Duration.ZERO);

            }
        });
        popover.ownerWindowProperty().get().setOnCloseRequest((WindowEvent event) -> {
            popover.hide(Duration.ZERO);
        });
    }

    public ReadOnlyObjectProperty<CWmListDocumentType> documentTypeProperty() {
        return cbDocumentType.getSelectedItemProperty();
    }

//    public ObjectProperty<LocalDate> documentDateProperty() {
//        return dtReminderDueDate.valueProperty();
//    }
    public void showDocument(final File pFile) {
        Platform.runLater(() -> {
            previewBox.getChildren().clear();
            previewBox.getChildren().add(docViewer.getWebView());
            docViewer.loadDocument(pFile);
        });
    }

    public void showTxt(final File pFile) {
        Platform.runLater(() -> {
            previewBox.getChildren().clear();
            previewBox.getChildren().add(docViewer.getWebView());
            docViewer.showFile(pFile);
        });
    }

    public void loadDocument(final DocumentConverterResult docResult) {
        //Document doc = new Document(docResult.inFile, text);
        //preview = docViewer.getWebView();
        previewBox.setStyle(null);
        if (docResult.pdfFile == null) {
            LOG.log(Level.WARNING, "No PDF defined for this file: {0}", docResult.inFile.getAbsolutePath());
        } else if (!docResult.pdfFile.exists() || docResult.pdfFile.length() == 0) {
            LOG.log(Level.WARNING, "PDF file does not exist or is empty: {0}", docResult.pdfFile.getAbsolutePath());
        }
        if (cbDocumentImportShowPdfEnabled.isSelected()) {
            if (!docResult.fileType.isPdf()) {
                //load later if it is not a PDF file
                showDocument(docResult.pdfFile);
            }
        } else {
            final File txtFile;
            if (docResult.txtFile == null || StringUtils.endsWith(docResult.txtFile.getName(), ".txt")) {
                txtFile = docResult.txtFile;
            } else {
                //csv file is not displayed in WebView, so we have to write it to a temporary text file first
                File newFile = null;
                try {
                    newFile = Files.createTempFile("tmp", ".txt").toFile();
                    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), CpxSystemProperties.DEFAULT_ENCODING))) {
                        writer.write(docResult.readText());
                    }
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "unable to create temporary file", ex);
                }
                txtFile = newFile;
            }
            showTxt(txtFile);
        }

        //linkPdf.setVisible(docResult.pdfFile != null);
//            ctrl.setCaseNumber(doc.getCaseNumber());
        linkPdf.setOnAction((event) -> {
            showFile(docResult.pdfFile);
        });
        if (docResult.pdfFile != null) {
            Tooltip.install(linkPdf, new Tooltip(docResult.pdfFile.getName()));
        } else {
            Tooltip.install(linkPdf, null);
        }
        //linkTxt.setVisible(docResult.txtFile != null);
        linkTxt.setVisible(!docResult.fileType.isText());
        linkTxt.setOnAction((event) -> {
            showFile(docResult.txtFile);
        });
        if (docResult.txtFile != null) {
            Tooltip.install(linkTxt, new Tooltip(docResult.txtFile.getName()));
        } else {
            Tooltip.install(linkTxt, null);
        }
        if (docResult.pdfFile == null) {
            Platform.runLater(() -> {
                previewBox.getChildren().clear();
                Label pleaseWaitLabel = new Label("Die Vorschau steht für dieses Dokument nicht zur Verfügung");
                pleaseWaitLabel.setAlignment(Pos.CENTER);
                CpxFXMLLoader.setAnchorsInNode(pleaseWaitLabel);
                previewBox.getChildren().add(pleaseWaitLabel);
            });
        } else {
            previewBox.setStyle("-fx-border-style: solid; -fx-border-width: 0 0 3 0; -fx-border-color: #484848");
        }
    }

    public static boolean showFile(final File pFile) {
        if (pFile == null) {
            return false;
        }
        if (!pFile.exists()) {
            LOG.log(Level.WARNING, "file does not exist: {0}", pFile.getAbsolutePath());
            return false;
        }
        DocumentManager.showFileAsTempFile(pFile);
        return true;
    }

    @FXML
    private void setFolder(ActionEvent event) {
        String folder = txtSelectedFolder.getText().trim();
        setFolder(folder);
    }

    @FXML
    private void importSelectedDocuments(ActionEvent event) {
        LOG.log(Level.INFO, "Import {0} selected documents...", selectedItems.size());
        List<FileConvert> importableItems = new ArrayList<>();
        for (FileConvert fc : selectedItems) {
            //selectFile(fc);
            //fc.getController().disableValidation();
            //fc.getController().enableValidation();

            //LOG.log(Level.INFO, "invalid? -> " + fc.isInvalid());
            //selectFile(fc);
            //selectFile(fc);
            //fc.getController().
//            if (!fc.getController().g) {
//                importDocument(fc, true);
//            }
//            if (!fc.isInvalid()) {
//                importDocument(fc, true);
//                imported++;
//            }
            if (fc.getController().getSelectedCaseId() != 0L
                    && fc.getController().getSelectedWorkflowId() != 0L) {
                importableItems.add(fc);
            }
        }
        if (importableItems.isEmpty()) {
            MainApp.showErrorMessageDialog("Es konnten keine importierbaren Dokumente gefunden werden.\nStellen Sie sicher, dass zu den Dokumenten eine Fall- und Vorgangsnummer ausgewählt wurde.");
        } else {
            AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie " + importableItems.size() + " Dokumente jetzt importieren?");
            dlg.initModality(Modality.WINDOW_MODAL);
            Optional<ButtonType> res = dlg.showAndWait();
            if (res.isPresent() && res.get().equals(ButtonType.OK)) {
//                Service<byte[]> service = new Service<byte[]>() {
//                    @Override
//                    protected Task<byte[]> createTask() {
//                        return new Task<byte[]>() {
//                            @Override
//                            protected byte[] call() throws InterruptedException, CpxIllegalArgumentException {
//                                
//                            }
//                        };
//                    }
//                };
//                ProgressWaitingDialog dialog = new ProgressWaitingDialog();
//                final Window parentWindow = getScene().getWindow();
                final RingProgressIndicator riProgress = new RingProgressIndicator();
                riProgress.setProgress(0.0d);
//                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                AlertDialog dlgProgress = AlertDialog.createInformationDialog("Dokumente werden importiert...", MainApp.getStage());
                dlgProgress.initModality(Modality.WINDOW_MODAL);
                dlgProgress.getDialogPane().setContent(riProgress);
                dlgProgress.getButtonTypes().remove(ButtonType.OK);
                //dlgProgress.getButtonTypes().add(ButtonType.CANCEL);
//                dlgProgress.getButtonTypes().add(ButtonType.CANCEL);
//                Optional<ButtonType> resProgress = dlgProgress.showAndWait();
//                if (resProgress.isPresent() && resProgress.get().equals(ButtonType.CANCEL)) {
//                    //STOP
//                }
                final Thread th = new Thread(() -> {
                    int imported = 0;
                    final ObjectProperty<FileConvert> lastFc = new SimpleObjectProperty<>();
                    for (FileConvert fc : importableItems) {
                        if (Thread.currentThread().isInterrupted()) {
                            LOG.log(Level.WARNING, "import of multiple files was cancelled by user");
                            Platform.runLater(() -> {
                                dlgProgress.close();
                            });
                            return;
                        }
//                            try {
                        if (importDocument(fc, true)) {
                            imported++;
                        }
                        lastFc.set(fc);
//                                Thread.sleep(3000L);
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(DocumentImportFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                    Platform.runLater(() -> {
//                        selectedItems.remove(fc);
//                    });
                        final double progress = (imported * 100) / (double) importableItems.size();
                        final String statusText = imported + " / " + importableItems.size() + " Dokumente";
                        Platform.runLater(() -> {
                            riProgress.setProgress(progress);
                            riProgress.setStatusText(statusText);
                        });
                    }
                    LOG.log(Level.INFO, "Total number of actual imported documents: {0}", imported);
                    MainApp.showInfoMessageDialog(imported + " Dokumente wurden importiert.", MainApp.getStage());
                    Platform.runLater(() -> {
                        selectNextFile(lastFc.get());
                        dlgProgress.close();
                    });
                });
                th.start();

                dlgProgress.getButtonTypes().add(ButtonType.CANCEL);
                //Button cancelBtn = dlgProgress.getSkin().getButton(ButtonType.CANCEL);
                //ButtonType cancelButton = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
                //dlgProgress.getButtonTypes().add(cancelButton);
                dlgProgress.getDialogPane().lookupButton(ButtonType.CANCEL).addEventFilter(
                        ActionEvent.ACTION,
                        evt -> {
                            // to prevent the dialog to close
                            //event.consume();
                            th.interrupt();
                        }
                );

                dlgProgress.show();
                //cancelButton.getButtonData().
                //don't save
                //return;
            }
        }
    }

    private boolean checkFolder(final String pFolder) {
        final String folder = pFolder == null ? "" : pFolder.trim();
        if (folder.isEmpty()) {
            return false;
        }
        if (!DocumentManager.isValidFilepath(folder)) {
            Notifications notif = NotificationsFactory.instance().createErrorNotification();
            notif.text("Ungültiges Verzeichnis: " + folder);
            notif.show();
            return false;
        }
        final File file = new File(folder);
        if (!file.exists()) {
            Notifications notif = NotificationsFactory.instance().createErrorNotification();
            notif.text("Verzeichnis existiert nicht: " + folder);
            notif.show();
            return false;
        }
        if (!file.isDirectory()) {
            Notifications notif = NotificationsFactory.instance().createErrorNotification();
            notif.text("Kein Verzeichnis: " + folder);
            notif.show();
            return false;
        }
        if (!file.canRead()) {
            Notifications notif = NotificationsFactory.instance().createErrorNotification();
            notif.text("Keine Zugriffsrechte: " + folder);
            notif.show();
            return false;
        }
        return true;
    }

    private void setFolder(final String pFolder) {
        final String folder = pFolder == null ? "" : pFolder.trim();
        if (!checkFolder(folder)) {
            return;
        }
        final File file = new File(folder);
        CpxClientConfig.instance().setUserRecentFileChooserPath(file);
        Thread th = new Thread(() -> {
            final File[] files = filterFiles(file);
            int fileCount = 0;
            int dirCount = 0;

            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        dirCount++;
                    } else {
                        fileCount++;
                    }
                }

                final int fCount = fileCount;
                final int dCount = dirCount;

                if (files.length == 0) {
                    Platform.runLater(() -> {
                        Notifications notif = NotificationsFactory.instance().createErrorNotification();
                        notif.text("Enthält keine importierbaren Dateien: " + folder + "\nunterstützt werden " + String.join(", ", SUPPORTED_FILE_TYPES));
                        notif.show();
                    });
                    return;
                }
                Platform.runLater(() -> {
                    Notifications notif = NotificationsFactory.instance().createInformationNotification();
                    notif.text(fCount + " Dateien und " + dCount + " Unterverzeichnisse gefunden");
                    notif.show();
                });
            } else {
                Platform.runLater(() -> {
                    Notifications notif = NotificationsFactory.instance().createErrorNotification();
                    notif.text("Auf das Verzeichnis konnte nicht zugegriffen werden: " + file.getAbsolutePath());
                    notif.show();
                });
                return;
            }

            Platform.runLater(() -> {
                selectedItems.clear();
            });
            converterQueue.clear();
            documents.clear();
            Platform.runLater(() -> {
                fileTree.setRoot(null);
            });
            //processeQueueElems.clear();
            increment = fileCount;
            int incrementTmp = increment;
            final Window parentWindow = getParentWindow();
            TreeItem<FileConvert> rootItem = new TreeItem<>(new FileConvert(parentWindow, file, true));
            rootItem.setExpanded(true);
            TreeItem<FileConvert> firstTreeItem = null;
            for (File f : files) {
                FileConvert fc = new FileConvert(parentWindow, f, false, incrementTmp--);
                //System.out.println(fc.priority + "   " + fc.file.getName());
                converterQueue.add(fc);
                TreeItem<FileConvert> treeItem = newTreeItem(fc); // new TreeItem<>(fc);
                treeItem.setExpanded(false);
                if (firstTreeItem == null) {
                    firstTreeItem = treeItem;
                }
//            treeItem.getChildren().clear();
                rootItem.getChildren().add(treeItem);
            }
            //sort treeview items
            rootItem.getChildren().sort(Comparator.comparing(t -> t.getValue().file.isFile()));

            final TreeItem<FileConvert> fTreeItem = firstTreeItem;

            Platform.runLater(() -> {
                fileTree.setRoot(rootItem);
                fileTree.setShowRoot(true);
                if (fTreeItem != null && fTreeItem.getValue().file.isFile()) {
                    //select first file (but not directory because otherwise you would open the subdirectory!)
                    fileTree.getSelectionModel().select(fTreeItem);
//            firstTreeItem.setExpanded(false);
                    selectFile(fTreeItem.getValue());
                }
            });
        });
        th.start();
    }

    public Window getParentWindow() {
        return MainApp.getToolbarMenuScene().getDocumentImportPane().getScene().getWindow();
        //return ((Pane) (getScene().getRoot())).getScene().getWindow();
    }

    public File[] filterFiles(final File pFile) {
        File[] files = pFile.listFiles((File pathname) -> {
            //                if (pathname.isDirectory()) {
//                    return true;
//                }
            if (pathname.isDirectory()) {
                return true;
            }
            String extension = FilenameUtils.getExtension(pathname.getName());
            if (pathname.getName().startsWith("~$")) {
                return false;
            }
            for (String ext : SUPPORTED_FILE_TYPES) {
                if (extension.equalsIgnoreCase(ext)) {
                    return true;
                }
            }
            return false;
        });
        return files;
    }

//    public File[] filterDirs(final File pFile) {
//        File[] files = pFile.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
////                if (pathname.isDirectory()) {
////                    return true;
////                }
//                return pathname.isDirectory();
//            }
//        });
//        return files;
//    }
    @FXML
    private void chooseFolder(ActionEvent event) {
        DirectoryChooser dirChooser = FileChooserFactory.instance().createDirectoryChooser();
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf", supportedFileTypes));
        dirChooser.setTitle("Verzeichnis auswählen");
        //String defaultReportName = "report_" + pCase.getCsCaseNumber();
        //fileChooser.setInitialFileName(defaultReportName);
        File file = dirChooser.showDialog(getScene().getWindow());
        CpxClientConfig.instance().setUserRecentFileChooserPath(file);
        if (file != null) {
            String dir = file.getAbsolutePath();
            txtSelectedFolder.setText(dir);
            setFolder(dir);
        }

    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean close() {
        LOG.log(Level.INFO, "Send interrupt signal to all document converter threads");
        for (DocumentConverterThread th : threads) {
            th.interrupt();
        }
//        LOG.log(Level.INFO, "Close all word and excel apps of document converter threads");
//        for (DocumentConverterThread th : threads) {
//            th.closeApps();
//        }
        LOG.log(Level.INFO, "Wait until all document converter threads are stopped");
        for (DocumentConverterThread th : threads) {
            final long sleepPeriod = 50L;
            long sleepSum = 0L;
            final long maxSleepSum = 90000L;
            while (!th.stopped && th.isAlive()) {
                if (sleepSum >= maxSleepSum) {
                    //force to kill thread!
                    th.stop();
                }
                try {
                    sleepSum += sleepPeriod;
                    Thread.sleep(sleepPeriod);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
        }
        return true;
    }

    @FXML
    private void openFile(ActionEvent event) {
        linkOriginal.fire();
    }

    @FXML
    private void toggleDocumentDetection(ActionEvent event) {
        if (!cbDocumentImportOfficeEnabled.isDisabled()) {
            DocumentReader.setOfficeEnabled(cbDocumentImportOfficeEnabled.isSelected());
            Session.instance().setDocumentImportOfficeEnabled(cbDocumentImportOfficeEnabled.isSelected());
        }
    }

    @FXML
    private void toggleShowPdf(ActionEvent event) {
        if (!cbDocumentImportShowPdfEnabled.isDisabled()) {
//            DocumentReader.setOfficeEnabled(cbDocumentImportOfficeEnabled.isSelected());
            Session.instance().setDocumentImportShowOfficeEnabled(cbDocumentImportShowPdfEnabled.isSelected());
        }
    }

    private boolean archivateFile(final FileConvert pFileConvert, final DocumentImportedEn pDocumentImportedEn) {

        final File file = pFileConvert.file;
        boolean archivated = false;

        if(processServiceBean.get().isDocumentToArchivateafterImport()) {
            archivated = DocumentManager.archivateFile(file, fileTree.getRoot().getValue().file.getAbsolutePath());
        } else {
            archivated = true;
        }
//        Calendar cal = Calendar.getInstance();
//        String year = String.valueOf(cal.get(Calendar.YEAR));
//        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
//        if (month.length() == 1) {
//            month = "0" + month;
//        }
//        boolean archived = false;
//        final File archiveFile = new File(fileTree.getRoot().getValue().file.getAbsolutePath() + "//Archiv//" + year + "//" + month + "//" + file.getName());
//        if (!file.exists()) {
//            LOG.log(Level.WARNING, "Was not able to move file ''{0}'' to archive folder, because file does not exist (anymore)", file.getName());
//            //MainApp.showErrorMessageDialog("Die Datei '" + file.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil die Datei nicht mehr existiert");
//        } else {
//            if (!archiveFile.getParentFile().exists()) {
//                archiveFile.getParentFile().mkdirs();
//                if (!archiveFile.getParentFile().exists()) {
//                    LOG.log(Level.SEVERE, "was not able to archive/move imported document from {0} to {1} (was not able to create not existing directory)", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
//                    MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil das Verzeichnis nicht angelegt werden konnte: " + archiveFile.getParentFile().getAbsolutePath());
//                }
//            }
//            if (archiveFile.getParentFile().exists()) {
//                if (archiveFile.getParentFile().isFile()) {
//                    LOG.log(Level.SEVERE, "was not able to archive/move imported document from {0} to {1} (a file was found under this path)", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
//                    MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil es bereits eine gleichnamige Datei gibt: " + archiveFile.getParentFile().getAbsolutePath());
//                } else {
//                    if (!archiveFile.getParentFile().canWrite()) {
//                        LOG.log(Level.SEVERE, "was not able to archive/move imported document from {0} to {1} (no permission to write)", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
//                        MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil die Schreibrechte fehlen: " + archiveFile.getParentFile().getAbsolutePath());
//                    } else {
//                        //move file
//                        try {
//                            if (file.renameTo(archiveFile)) {
//                                LOG.log(Level.FINEST, "file was successfully renamed: {0} -> {1}", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
//                            }
//                            LOG.log(Level.INFO, "imported document was archived/move from {0} to {1}", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
//                            archived = true;
//                        } catch (SecurityException ex) {
//                            LOG.log(Level.SEVERE, "was not able to archive/move imported document from " + file.getAbsolutePath() + " to " + archiveFile.getAbsolutePath() + " (maybe file is in access?)", ex);
//                            MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil ein Zugriffsproblem aufgetreten ist: " + archiveFile.getParentFile().getAbsolutePath());
//                        }
//                    }
//                }
//            }
//        }
//        ArrayList<TreeItem<FileConvert>> treeItems = new ArrayList<>(fileTree.getRoot().getChildren());
        if(archivated){
            if (DocumentImportedEn.SINGLE_FILE_IMPORT.equals(pDocumentImportedEn)) {
                selectNextFile(pFileConvert);
            }
            int matchedIndex = getIndexOf(pFileConvert);
            LOG.log(Level.FINE, "matchedIndex: " + matchedIndex);
            if (matchedIndex > -1) {
    //            int nextIndex = (matchedIndex + 1) % fileTree.getRoot().getChildren().size();
    //            if (nextIndex != matchedIndex) {
    //                TreeItem<FileConvert> nextTreeItem = treeItems.get(nextIndex);
    //                if (nextTreeItem != null) {
    //                    fileTree.getSelectionModel().select(nextTreeItem);
    //                    selectFile(nextTreeItem.getValue());
    //                }
    //            }

    
                fileTree.getRoot().getChildren().remove(matchedIndex);
            }
        }
        return archivated;
    }

    private void selectNextFile(final FileConvert pFileConvert) {
        if (pFileConvert == null) {
            return;
        }
        ArrayList<TreeItem<FileConvert>> treeItems = new ArrayList<>(fileTree.getRoot().getChildren());
        int matchedIndex = getIndexOf(pFileConvert);
        if (matchedIndex > -1) {
            int nextIndex = (matchedIndex + 1) % fileTree.getRoot().getChildren().size();
            if (nextIndex != matchedIndex) {
                TreeItem<FileConvert> nextTreeItem = treeItems.get(nextIndex);
                if (nextTreeItem != null) {
                    fileTree.getSelectionModel().select(nextTreeItem);
                    selectFile(nextTreeItem.getValue());
                }
            }
            //fileTree.getRoot().getChildren().remove(matchedIndex);
        }
    }

    public int getIndexOf(final FileConvert pFileConvert) {
        final ArrayList<TreeItem<FileConvert>> treeItems = new ArrayList<>(fileTree.getRoot().getChildren());
        int matchedIndex = -1;
        for (int i = 0; i < treeItems.size(); i++) {
            TreeItem<FileConvert> treeItem = treeItems.get(i);
            if (treeItem.getValue() == pFileConvert) {
                matchedIndex = i;
                break;
            }
        }
        return matchedIndex;
    }

    @FXML
    private void openFolderInExplorer(MouseEvent event) {
        if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 1) {
            String folder = txtSelectedFolder.getText();
            if (checkFolder(folder)) {
                ToolbarMenuFXMLController.openInExplorer(folder);
            }
        }
    }

    @FXML
    private void refreshFolder(MouseEvent event) {
        if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 1) {
            String folder = txtSelectedFolder.getText();
            setFolder(folder);
//            if (checkFolder(folder)) {
//                ToolbarMenuFXMLController.openInExplorer(folder);
//            }
        }
    }

    private void setUpLanguage() {
        cbRequestType.setTitle(Lang.getRequestType());
        cbReminderType.setTitle(Lang.getReminderType());
        cbActionType.setTitle(Lang.getActionType());
        cbDocumentType.setTitle(Lang.getDocumentType());
        dpDocumentDate.setTitle(Lang.getDocumentDate());
        dtReminderDueDate.setTitle(Lang.getDurationTo());
        txtReminderAssignedTo.setTitle(Lang.getReminderReceiver());
    }

    public class DocumentConverterThread extends Thread {

        private final DocumentConverter docConverter = new DocumentConverter();
        private final ObjectProperty<FileConvert> fileConvertProp = new SimpleObjectProperty<>();
        private boolean stopped = false;

        public boolean isStopped() {
            return stopped;
        }

        public DocumentConverter getDocumentConverter() {
            return docConverter;
        }

//        public Set<ActiveXComponent> getWordApps() {
//            return docConverter.getWordApps();
//        }
//
//        public Set<ActiveXComponent> getExcelApps() {
//            return docConverter.getWordApps();
//        }
//        public void closeWordApps() {
//            docConverter.closeWordApps();
//        }
//
//        public void closeExcelApps() {
//            docConverter.closeExcelApps();
//        }
        public void closeApps() {
            docConverter.closeApps();
        }

        public FileConvert getFileConvert() {
            return fileConvertProp.get();
        }

        @Override
        public synchronized void run() {
            while (true) {
                try {
                    //LOG.log(Level.INFO, "Pick next from converterQueue");
                    if (isInterrupted()) {
                        LOG.log(Level.INFO, "Thread was interrupted");
                        break;
                    }
                    FileConvert elem = converterQueue.poll(999, TimeUnit.DAYS);
                    fileConvertProp.set(elem);
                    //processeQueueElems.add(elem);
                    if (isInterrupted()) {
                        LOG.log(Level.INFO, "Thread was interrupted");
                        break;
                    }
                    if (elem == null || elem.file.isDirectory()) {
                        continue;
                    }
                    LOG.log(Level.INFO, "Picked {0} from converterQueue", elem.file.getName());
                    //Platform.runLater(() -> {
                    //elem.progressIndicator.setPrefWidth(USE_COMPUTED_SIZE);
                    //});
                    //Got result from conversion!
                    DocumentConverterResult docResult;
                    boolean error;
//                    if (!cbDocumentDetection.isSelected()) {
//                        error = true;
//                        docResult = docConverter.convert(elem.file.getAbsolutePath());
//                        Platform.runLater(() -> {
//                            final Glyph infoGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.INFO);
//                            infoGlyph.setStyle("-fx-text-fill: blue;");
//                            infoGlyph.setCursor(Cursor.HAND);
//                            infoGlyph.setOnMouseClicked((event) -> {
//                                MainApp.showInfoMessageDialog("Die Datei wurde nicht umgewandelt, weil die Dokumentenerkennung deaktiviert wurde");
//                            });
//                            elem.setStatusResult(infoGlyph);
//                        });
//                        docResult = new DocumentConverterResult(elem.file, elem.fileType, null, null);
//                    } else {
                    //final boolean documentDetection = cbDocumentDetection.isSelected();
                    try {
                        docResult = docConverter.convert(elem.file.getAbsolutePath());
                        error = false;
                    } catch (Exception ex) {
                        error = true;
                        final boolean officeDisabledException = ex instanceof OfficeDisabledException;
                        //final boolean programNotFoundException = ex instanceof ProgramNotFoundException;
                        if (!officeDisabledException) {
                            LOG.log(Level.SEVERE, "Was not able to convert file: " + elem.file.getAbsolutePath(), ex);
                        }
                        Platform.runLater(() -> {
                            final Glyph resultGlyph;
                            if (officeDisabledException) {
                                resultGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.INFO);
                                resultGlyph.setStyle("-fx-text-fill: blue;");
                                Tooltip.install(resultGlyph, new Tooltip("Das Dokumente wurde nicht umgewandelt, weil die Unterstützung von Microsoft Office deaktiviert ist"));
                                resultGlyph.setOnMouseClicked((event) -> {
                                    MainApp.showInfoMessageDialog("Das Dokumente wurde nicht umgewandelt, weil die Unterstützung von Microsoft Office deaktiviert ist", getScene().getWindow());
                                });
                            } else {
                                resultGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION);
                                resultGlyph.setStyle("-fx-text-fill: red;");
                                Tooltip.install(resultGlyph, new Tooltip("Das Dokument konnte nicht konvertiert werden.\r\nSie können das Dokument trotzdem importieren!\r\n(Anklicken um die Fehlermeldung zu sehen)!"));
                                resultGlyph.setOnMouseClicked((event) -> {
                                    if (ex instanceof ReaderException) {
                                        MainApp.showErrorMessageDialog("Die Datei konnte nicht umgewandelt werden: " + elem.file.getAbsolutePath() + "\nGrund: " + ex.getMessage(), getScene().getWindow());
                                    } else {
                                        MainApp.showErrorMessageDialog(ex, "Die Datei konnte nicht umgewandelt werden: " + elem.file.getAbsolutePath(), getScene().getWindow());
                                    }
                                });
                            }
                            resultGlyph.setCursor(Cursor.HAND);
                            elem.setStatusResult(resultGlyph);
//                            MainApp.showErrorMessageDialog(ex, "Die Datei konnte nicht umgewandelt werden: " + elem.file.getAbsolutePath());
                        });
                        docResult = new DocumentConverterResult(elem.file, elem.fileType, null, null);
                    }
//                    }
                    elem.setDocumentConverterResult(docResult);
                    if (!error) {
                        final String caseNumber = docResult.getDocument().getCaseNumber();
                        List<TCase> cases = caseNumber.isEmpty() ? new ArrayList<>() : singleCaseEjb.get().findCasesForCaseNumber(caseNumber);
                        List<TCase> potentialCases = caseNumber.isEmpty() || !cases.isEmpty() ? new ArrayList<>() : singleCaseEjb.get().findPotentialCasesForCaseNumber(caseNumber);

                        final String patientNumber = docResult.getDocument().getPatientNumber();
                        List<TPatient> patients = patientNumber.isEmpty() ? new ArrayList<>() : singleCaseEjb.get().findPatientsForPatientNumber(patientNumber);
                        List<TPatient> potentialPatients = patientNumber.isEmpty() || !patients.isEmpty() ? new ArrayList<>() : singleCaseEjb.get().findPotentialPatientsForPatientNumber(patientNumber);
                        //elem.progressIndicator.setVisible(false);
                        //elem.progressIndicator.setPrefWidth(0.0d);
                        Platform.runLater(() -> {
                            //elem.caseNumberLabel.setText(caseNumber.isEmpty() ? "-" : caseNumber);
                            //if (elem.getController() != null) {
                            //                        if (elem.hasController()) {
                            //                            elem.getController().setCaseNumber(caseNumber);
                            //                        }
                            elem.setCases(caseNumber, cases, potentialCases);
                            elem.setPatients(patientNumber, patients, potentialPatients);
                            elem.setStatusResult(elem.getCaseGlyph());
                            //elem.setResultGlyph(elem.getCaseGlyph());
                            //}
                        });
                    }
                    LOG.log(Level.FINER, "Put {0} result to documents", elem.file.getName());
                    documents.put(elem.file, docResult);
                    if (isInterrupted()) {
                        LOG.log(Level.INFO, "Thread was interrupted");
                        break;
                    }
                    if (selectedFile != null && elem.file.equals(selectedFile.file)) {
                        loadDocument(docResult);
                    }
                } catch (InterruptedException ex) {
                    LOG.log(Level.FINEST, "Thread was interrupted", ex);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            stopped = true;
        }

    }

    private TreeItem<FileConvert> newTreeItem(final FileConvert pFileConvert) {
        TreeItem<FileConvert> treeItem = new TreeItem<>(pFileConvert);
        treeItem.expandedProperty().addListener((observable) -> {
            BooleanProperty bb = (BooleanProperty) observable;
            @SuppressWarnings("unchecked")
            TreeItem<FileConvert> item = (TreeItem<FileConvert>) bb.getBean();
            if (item == null) {
                return;
            }
            if (!item.getValue().isRoot()) {
                item.setExpanded(false);
            }
        });
        pFileConvert.multiSelectProperty().bind(btnImportSelected.visibleProperty());
        return treeItem;
    }

    /**
     * RISKY, may result in alot of server queries!
     *
     * @param pUserName username
     * @return Id der username
     */
    private Long getUserID(final String pUserName) {
        final String userName = StringUtils.trimToEmpty(pUserName);
        if (userName.isEmpty()) {
            return null;
        } else {
            try {
                //return authServiceEjb.get().getIdbyUName(pUName);
                return MenuCache.instance().getUserId(userName);
            } catch (NamingException ex) {
                LOG.log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

}

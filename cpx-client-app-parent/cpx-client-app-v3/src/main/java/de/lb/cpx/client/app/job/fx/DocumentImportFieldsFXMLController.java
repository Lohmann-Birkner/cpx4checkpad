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
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.autocompletion.AutocompletionMultiColDatePicker;
import de.lb.cpx.client.core.model.fx.autocompletion.AutocompletionMultiColTextField;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledAutocompletionMultiColTextField;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.service.ejb.WorkflowListStatelessEJBRemote;
import de.lb.cpx.shared.dto.DocumentSearchCaseItemDto;
import de.lb.cpx.shared.dto.DocumentSearchPatientItemDto;
import de.lb.cpx.shared.dto.DocumentSearchProcessItemDto;
import de.lb.cpx.shared.lang.AbstractLang;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 * FXML Controller class
 *
 * @author niemeier
 */
public class DocumentImportFieldsFXMLController extends Controller<CpxScene> {

    private static final Logger LOG = Logger.getLogger(DocumentImportFieldsFXMLController.class.getName());

    private final EjbProxy<SingleCaseEJBRemote> singleCaseEjb = Session.instance().getEjbConnector().connectSingleCaseBean();
    private final EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();

    @FXML
    private LabeledAutocompletionMultiColTextField<DocumentSearchCaseItemDto> txtCaseNumber;
    @FXML
    private LabeledAutocompletionMultiColTextField<DocumentSearchProcessItemDto> txtProcessNumber;
    @FXML
    private LabeledAutocompletionMultiColTextField<DocumentSearchPatientItemDto> txtPatientNumber;
    @FXML
    private LabeledAutocompletionMultiColTextField<DocumentSearchPatientItemDto> txtPatientName;
    @FXML
    private LabeledAutocompletionMultiColTextField<DocumentSearchPatientItemDto> txtInsuranceNumerPatient;
    @FXML
    private AutocompletionMultiColDatePicker<DocumentSearchPatientItemDto> dtBirthdate;
    @FXML
    private SectionHeader headerCaseProcess;
    @FXML
    private SectionHeader headerPatient;
    @FXML
    private Button btnApplyCaseNumber;
    @FXML
    private HBox boxRecognizedCaseNumber;
    @FXML
    private GridPane gpRoot;

    private Glyph glyph;
    protected ValidationSupport validationSupport = new ValidationSupport();
    public static final int MIN_CASE_NUMBER_LENGTH_FOR_AUTOCOMPLETION = 3;
    public static final int MIN_WORKFLOW_NUMBER_LENGTH_FOR_AUTOCOMPLETION = 2;
    public static final int MIN_PATIENT_NUMBER_LENGTH_FOR_AUTOCOMPLETION = 2;
    public static final int MIN_PATIENT_NAME_LENGTH_FOR_AUTOCOMPLETION = 2;

    private Window parentWindow;
    private FileConvert fileConvert;
    private static final WorkflowListStatelessEJBRemote WF_BEAN = Session.instance().getEjbConnector().connectWorkflowListBean().get();
    public static final int MAX_ENTRIES = 26;
    private final ObjectProperty<DocumentImportedEn> importedProperty = new SimpleObjectProperty<>(DocumentImportedEn.NO_IMPORT);
    @FXML
    private Button openCaseBtn;
    @FXML
    private Button openProcessBtn;

    private final LongProperty selectedCaseIdProp = new SimpleLongProperty(0L);
    private final LongProperty selectedPatientIdProp = new SimpleLongProperty(0L);
    private final LongProperty selectedWorkflowIdProp = new SimpleLongProperty(0L);

    private String caseNumber = "";
    private List<TCase> cases = new ArrayList<>();
    @FXML
    private ProgressIndicator throbberCase;
    @FXML
    private HBox boxRecognizedPatientNumber;
    @FXML
    private ProgressIndicator throbberPatient;
    @FXML
    private Button btnApplyPatientNumber;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpLanguage();
        setUpValidators();
        Glyph openCaseGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FILE);
        Glyph openProcessGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER);
        openCaseBtn.setGraphic(openCaseGlyph);
        openProcessBtn.setGraphic(openProcessGlyph);

        openCaseBtn.disableProperty().bind(selectedCaseIdProp.isEqualTo(0L));
        openProcessBtn.disableProperty().bind(selectedWorkflowIdProp.isEqualTo(0L));

        btnApplyCaseNumber.prefWidthProperty().bind(txtProcessNumber.widthProperty());
        boxRecognizedCaseNumber.prefWidthProperty().bind(txtCaseNumber.widthProperty());

        btnApplyPatientNumber.prefWidthProperty().bind(txtProcessNumber.widthProperty());
        boxRecognizedPatientNumber.prefWidthProperty().bind(txtCaseNumber.widthProperty());

        setCaseNumberSearch();
        setInsuranceNumberPatientSearch();
        setProcessNumberSearch();
        setPatientNumberSearch();
        setPatientNameSearch();
        setPatientDateOfBirthSearch();
        presetCaseWorkflowItem();
    }

    private void setUpLanguage() {
        txtCaseNumber.setTitle(Lang.getCaseNumber());
        txtProcessNumber.setTitle(Lang.getProcessNumber());
        txtPatientName.setTitle(Lang.getPatientFullName());
        txtInsuranceNumerPatient.setTitle(Lang.getInsuranceNumber());
        txtPatientNumber.setTitle(Lang.getPatientNumber());
        dtBirthdate.setTitle(Lang.getDateOfBirth());
        headerPatient.setTitle(Lang.getPatient());
    }

    private void setUpValidators() {
        validationSupport.initInitialDecoration();

//        Platform.runLater(() -> {
        //final BooleanProperty caseNumberValidProp = new SimpleBooleanProperty();
        validationSupport.registerValidator(txtCaseNumber.getControl(), (Control t, Object u) -> {
            ValidationResult res = new ValidationResult();
            if (u != null) {
                String caseNumber1 = StringUtils.trimToEmpty(txtCaseNumber.getControl().getText2());
                String caseNumberUserData = StringUtils.trimToEmpty((String) txtCaseNumber.getUserData());
                final boolean result = caseNumber1.isEmpty() || !caseNumber1.equalsIgnoreCase(caseNumberUserData);
                res.addErrorIf(t, "Fallnummer fehlt", result);
            }
            return res;
        });

        txtCaseNumber.getControl().focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                String caseNumber1 = StringUtils.trimToEmpty(txtCaseNumber.getControl().getText2());
                String caseNumberUserData = StringUtils.trimToEmpty((String) txtCaseNumber.getUserData());
                if (!caseNumber1.isEmpty() && !caseNumber1.equalsIgnoreCase(caseNumberUserData)) {
                    //List<DocumentSearchCaseItemDto> searchResult = findDocumentSearchCaseItems(caseNumber);
                    List<TCase> hosCases = singleCaseEjb.get().findCasesForCaseNumber(caseNumber1);
                    if (hosCases.isEmpty()) {
                        return;
                    }
                    if (hosCases.size() > 1) {
                        showCaseSelectDialog(hosCases, false);
                    } else {
                        TCase cs = hosCases.iterator().next();
                        final DocumentSearchCaseItemDto item = findDocumentSearchCaseItems(cs.id);
                        //selectedCaseIdProp.set(item.caseId);
                        if (item != null) {
                            setDocumentSearchItem(item);
                        }
                    }
                }
            }
        });

        //final BooleanProperty processNumberValidProp = new SimpleBooleanProperty();
        validationSupport.registerValidator(txtProcessNumber.getControl(), (Control t, Object u) -> {
            ValidationResult res = new ValidationResult();
            if (u != null) {
                String processNumber = StringUtils.trimToEmpty(txtProcessNumber.getControl().getText2());
                final Long workflowId = getWorkflowID(processNumber);
                selectedWorkflowIdProp.set(workflowId == null ? 0L : workflowId);
                final boolean result;
                //LOG.log(Level.SEVERE, "fileConvert.multiSelectProperty().get(): " + fileConvert.multiSelectProperty().get());
                if (fileConvert != null && fileConvert.multiSelectProperty().get()) {
                    result = processNumber.isEmpty() || selectedWorkflowIdProp.get() == 0L;
                } else {
                    result = !processNumber.isEmpty() && selectedWorkflowIdProp.get() == 0L;
                }
                res.addErrorIf(t, "Vorgangsnummer fehlt", result);
            }
            return res;
        });

        validationSupport.registerValidator(txtPatientNumber.getControl(), (Control t, Object u) -> {
            ValidationResult res = new ValidationResult();
            if (u != null) {
                String patientNumber1 = StringUtils.trimToEmpty(txtPatientNumber.getControl().getText2());
                String patientNumberUserData = StringUtils.trimToEmpty((String) txtPatientNumber.getUserData());
                res.addErrorIf(t, "Patientennummer fehlt", patientNumber1.isEmpty() || !patientNumber1.equalsIgnoreCase(patientNumberUserData));
            }
            return res;
        });
//        });
    }

    public File getFile() {
        return fileConvert.file;
    }

    public void setFileConvert(final FileConvert pFileConvert) {
        fileConvert = pFileConvert;
        fileConvert.multiSelectProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //force revalidation, because when multiple documents are selected you have to select a process
            String oldText = txtProcessNumber.getText();
            txtProcessNumber.setText(null);
            txtProcessNumber.setText(oldText);
        });

        fileConvert.presetCaseWorkflowItemProperty().addListener((ObservableValue<? extends DocumentSearchCaseItemDto> observable, DocumentSearchCaseItemDto oldValue, DocumentSearchCaseItemDto newValue) -> {
            presetCaseWorkflowItem();
        });
    }

    private void presetCaseWorkflowItem() {
        if (fileConvert == null) {
            return;
        }
        if (fileConvert.getPresetCaseWorkflowItem() != null
                && txtCaseNumber.getText().trim().isEmpty()) {
            setDocumentSearchItem(fileConvert.getPresetCaseWorkflowItem());
        }
    }

    public void setPatients(final String pPatientNumber, final List<TPatient> pPatients, final List<TPatient> pPotentialPatients) {
        btnApplyPatientNumber.setDisable(true);
        btnApplyPatientNumber.setTooltip(new Tooltip("Gefundene Patientennummer übernehmen"));
        boxRecognizedPatientNumber.getChildren().clear();
        String patientNumber = pPatientNumber == null ? "" : pPatientNumber.trim();
        List<TPatient> patients = new ArrayList<>(pPatients);
        List<TPatient> potentialPatients = new ArrayList<>(pPotentialPatients);
        if (patientNumber.isEmpty()) {
            Label label = new Label("Pat.-Nr. nicht erkannt");
            boxRecognizedPatientNumber.getChildren().add(label);
        } else {
            Label label = new Label(patientNumber + (pPatients.size() > 1 ? " (" + pPatients.size() + ")" : ""));
            boxRecognizedPatientNumber.getChildren().add(label);

            if (!patients.isEmpty() || !potentialPatients.isEmpty()) {
                btnApplyPatientNumber.setDisable(false);
            }

            btnApplyPatientNumber.setOnAction(event -> {
//                MainApp.showErrorMessageDialog("This function was implemented yet", parentWindow);
                //TBD: Patientnummer direkt übernommen (wenn patients.size() == 1 oder Auswahldialog in allen anderen Fällen anbieten
                //Patientennummer in txtPatientNumber eintragen und Suche auslösen (Beispiel siehe weiter unten)                                
                if (patients.size() == 1) {
                    final List<DocumentSearchPatientItemDto> result = findDocumentSearchPatientItemsByNumber(patientNumber);
                    final DocumentSearchPatientItemDto item = result.get(0);
                    setDocumentSearchItem(item);

                } else if (potentialPatients.size() == 1) {
                    TPatient potentialPatient = potentialPatients.get(0);
                    final List<DocumentSearchPatientItemDto> result = findDocumentSearchPatientItemsByNumber(potentialPatient.getPatNumber());
                    for (DocumentSearchPatientItemDto item : result) {
                        if (item.getPatientId() == potentialPatient.id) {
                            setDocumentSearchItem(item);
                            break;
                        }
                    }
                } else {
                    final List<TPatient> hosPatients;
                    final boolean potential;
                    Label dlgLabel;
                    if (patients.size() > 1) {
                        hosPatients = patients;
                        potential = false;
                        dlgLabel = new Label("Patienten mit der Patientennummer " + patientNumber);
                    } else {
                        hosPatients = potentialPatients;
                        potential = true;
                        dlgLabel = new Label("Patienten mit einer ähnlichen Patientennummer wie " + patientNumber);
                    }
                    //multiple potential cases -> user has to select

                    TextFlow title = new TextFlow(dlgLabel);

                    final FormularDialog<TPatient> dlg = new DocumentImportPatientSelectDialog(title, hosPatients, parentWindow, new Callback<TPatient, Void>() {
                        @Override
                        public Void call(TPatient selected) {
                            if (selected == null) {
                                return null;
                            }
                            List<DocumentSearchPatientItemDto> res = findDocumentSearchPatientItemsByNumber(selected.getPatNumber());
                            List<DocumentSearchPatientItemDto> items = new ArrayList<>();
                            for (DocumentSearchPatientItemDto item : res) {
                                if (selected.id == item.getPatientId()) {
                                    items.add(item);
                                }
                            }
                            if (items.size() == 1) {
                                final DocumentSearchPatientItemDto item = items.get(0);
                                setDocumentSearchItem(item);
                            } else {
                                txtPatientNumber.getControl().setText(selected.getPatNumber());
                            }
                            close();
                            return null;
                        }
                    });
                    Platform.runLater(() -> dlg.showAndWait());
                }
            });
        }
        Glyph gl = fileConvert.getPatientGlyph();
        boxRecognizedPatientNumber.getChildren().add(gl);
    }

    public void setCases(final String pCaseNumber, final List<TCase> pCases, final List<TCase> pPotentialCases) {
        btnApplyCaseNumber.setDisable(true);
        boxRecognizedCaseNumber.getChildren().clear();
        caseNumber = pCaseNumber == null ? "" : pCaseNumber.trim();
        cases = new ArrayList<>(pCases);
        List<TCase> potentialCases = new ArrayList<>(pPotentialCases);
        if (caseNumber.isEmpty()) {
            Label label = new Label("Fallnr. nicht erkannt");
            boxRecognizedCaseNumber.getChildren().add(label);
        } else {
            //cases = singleCaseEjb.get().findCasesForCaseNumber(pCaseNumber);

            //for(int type = 1; type <= 2; type++) {
            Hyperlink link = new Hyperlink(caseNumber + (cases.size() > 1 ? " (" + cases.size() + ")" : ""));
            boxRecognizedCaseNumber.getChildren().add(link);

            if (!cases.isEmpty() || !potentialCases.isEmpty()) {
                btnApplyCaseNumber.setDisable(false);
            }

            presetCaseWorkflowItem();
            link.setOnAction(event -> {
                if (cases.isEmpty() && potentialCases.isEmpty()) {
                    AlertDialog dlg = AlertDialog.createErrorDialog("Es wurden keine Fälle mit der Nummer " + pCaseNumber + " gefunden!", parentWindow);
                    dlg.initModality(Modality.WINDOW_MODAL);
                    dlg.show();
                } else if (cases.size() == 1) {
                    TCase cs = cases.iterator().next();
                    AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie den Fall " + cs.getCsCaseNumber() + " mit der Krankenhaus-IK " + cs.getCsHospitalIdent() + " öffnen?", parentWindow);
                    dlg.initModality(Modality.WINDOW_MODAL);
                    dlg.showAndWait().ifPresent((ButtonType t) -> {
                        if (t.equals(ButtonType.OK)) {
                            MainApp.getToolbarMenuScene().openCase(cases.get(0).id);
                        }
                    });
                } else if (potentialCases.size() == 1) {
                    TCase cs = potentialCases.iterator().next();
                    AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie den ähnlichen Fall " + cs.getCsCaseNumber() + " mit der Krankenhaus-IK " + cs.getCsHospitalIdent() + " öffnen?", parentWindow);
                    dlg.initModality(Modality.WINDOW_MODAL);
                    dlg.showAndWait().ifPresent((ButtonType t) -> {
                        if (t.equals(ButtonType.OK)) {
                            MainApp.getToolbarMenuScene().openCase(cases.get(0).id);
                        }
                    });
                } else {
                    //multiple cases
                    btnApplyCaseNumber.fire();
                }
            });

            //ProcessServiceBeanRemote processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean().get();
            btnApplyCaseNumber.setOnAction(event -> {
                if (cases.size() == 1) {
                    TCase cs = cases.iterator().next();
                    final DocumentSearchCaseItemDto item = findDocumentSearchCaseItems(cs.id);
                    //selectedCaseIdProp.set(item.caseId);
                    setDocumentSearchItem(item);
                } else if (potentialCases.size() == 1) {
                    TCase potentialCase = potentialCases.get(0);
                    final DocumentSearchCaseItemDto item = findDocumentSearchCaseItems(potentialCase.id);
                    setDocumentSearchItem(item);
                    //selectedCaseIdProp.set(item.caseId);
                } else {
                    final List<TCase> hosCases;
                    final boolean potential;
                    if (cases.size() > 1) {
                        hosCases = cases;
                        potential = false;
                    } else {
                        hosCases = potentialCases;
                        potential = true;
                    }
                    showCaseSelectDialog(hosCases, potential);
                }
            });
        }

        Glyph gl = fileConvert.getCaseGlyph();
        boxRecognizedCaseNumber.getChildren().add(gl);
    }

    private void showCaseSelectDialog(final List<TCase> pCases, final boolean pIsPotentialResult) {
        //multiple potential cases -> user has to select
        Label label;
        if (pIsPotentialResult) {
            label = new Label("Fälle mit einer ähnlichen Fallnummer wie " + caseNumber);
        } else {
            label = new Label("Fälle mit der Fallnummer " + caseNumber);
        }
        TextFlow title = new TextFlow(label);

        final FormularDialog<TCase> dlg = new DocumentImportCaseSelectDialog2(title, pCases, parentWindow, new Callback<TCase, Void>() {
            @Override
            public Void call(TCase selected) {
                if (selected == null) {
                    return null;
                }
                final DocumentSearchCaseItemDto item = findDocumentSearchCaseItems(selected.id);
                if (item != null) {
                    setDocumentSearchItem(item);
                } else {
                    txtCaseNumber.getControl().setText(selected.getCsCaseNumber());
                }
                close();
                return null;
            }
        });

        Platform.runLater(() -> dlg.showAndWait());
    }

    public Glyph getGlyphCopy() {
        final Glyph copy;
        if (glyph == null) {
            copy = null;
        } else {
            copy = glyph.duplicate();
            copy.setStyle(glyph.getStyle());
            copy.setAlignment(Pos.CENTER);
            copy.setPadding(new Insets(0));
            final Tooltip t;
            if (caseNumber.isEmpty()) {
                t = new Tooltip("keine Fallnummer erkannt");
            } else if (cases.isEmpty()) {
                t = new Tooltip("Fallnummer " + caseNumber + " existiert nicht");
            } else if (cases.size() == 1) {
                t = new Tooltip("Fallnummer " + caseNumber + " gefunden");
            } else {
                t = new Tooltip("Mehrere Fälle mit der Fallnummer " + caseNumber + " gefunden");
            }
            Tooltip.install(copy, t);
        }
        return copy;
    }

    public Long getWorkflowID(final String pWorkflowNumber) {
        final String workflowNumberTmp = StringUtils.trimToEmpty(pWorkflowNumber);
        if (workflowNumberTmp.isEmpty()) {
            return null;
        } else {
            final long workflowNumber;
            try {
                workflowNumber = Long.valueOf(workflowNumberTmp);
            } catch (NumberFormatException ex) {
                LOG.log(Level.FINEST, "Is not a valid long: " + pWorkflowNumber, ex);
                return null;
            }
            return processServiceBean.get().findSingleProcessIdForWorkflowNumber(workflowNumber);
        }
    }

    public ObjectProperty<DocumentImportedEn> getImportedProperty() {
        return importedProperty;
    }

    public void setParentWindow(final Window pParentWindow) {
        parentWindow = pParentWindow;
    }

    public void disableValidation() {
        validationSupport.setErrorDecorationEnabled(false);
    }

    public void enableValidation() {
        validationSupport.setErrorDecorationEnabled(true);
    }

    protected static DocumentSearchCaseItemDto findDocumentSearchCaseItems(final long pCaseId) {
        final List<DocumentSearchCaseItemDto> result = WF_BEAN.findCasesForDocumentImport(pCaseId, MAX_ENTRIES /*, null, null */);
        return result.isEmpty() ? null : result.get(0);
    }

    protected static List<DocumentSearchCaseItemDto> findDocumentSearchCaseItems(final String pHospitalIdent, final String pCaseNumber) {
        final String caseNumber = StringUtils.trimToEmpty(pCaseNumber);
        final String hospitalIdent = StringUtils.trimToEmpty(pHospitalIdent);
        final List<DocumentSearchCaseItemDto> result = WF_BEAN.findCasesForDocumentImport(hospitalIdent, caseNumber, MAX_ENTRIES /*, null, null */);
        return result;
    }

    protected static List<DocumentSearchCaseItemDto> findDocumentSearchCaseItems(final String pCaseNumber) {
        final String caseNumber = StringUtils.trimToEmpty(pCaseNumber);
        final List<DocumentSearchCaseItemDto> result = WF_BEAN.findCasesForDocumentImport(caseNumber, MAX_ENTRIES /*, null, null */);
        return result;
    }

    protected static List<DocumentSearchProcessItemDto> findDocumentSearchProcessItems(final String pProcessNumber) {
        final String processNumber = StringUtils.trimToEmpty(pProcessNumber);
        final List<DocumentSearchProcessItemDto> result = WF_BEAN.findProcessesForDocumentImport(processNumber, MAX_ENTRIES /*, null, null */);
        return result;
    }

    protected static List<DocumentSearchPatientItemDto> findDocumentSearchPatientItemsByNumber(final String pPatientNumber) {
        final String patientNumber = StringUtils.trimToEmpty(pPatientNumber);
        final List<DocumentSearchPatientItemDto> result = WF_BEAN.findPatientsForDocumentImportByNumber(patientNumber, MAX_ENTRIES /*, null, null */);
        return result;
    }

    private List<DocumentSearchPatientItemDto> findDocumentSearchPatientItemsByDateOfBirth(Date pDateOfBirth) {

        final List<DocumentSearchPatientItemDto> result = WF_BEAN.findPatientsForDocumentImportByDateOfBirth(pDateOfBirth, MAX_ENTRIES /*, null, null */);
        return result;
    }

    private static List<DocumentSearchPatientItemDto> findDocumentSearchPatientItemsByInsuranceNumber(final String pPatientInsuranceNumber) {
        final String patientInsuranceNumber = StringUtils.trimToEmpty(pPatientInsuranceNumber);
        final List<DocumentSearchPatientItemDto> result = WF_BEAN.findPatientsForDocumentImportByInsuranceNumber(patientInsuranceNumber, MAX_ENTRIES /*, null, null */);
        return result;
    }

    private static List<DocumentSearchPatientItemDto> findDocumentSearchPatientItemsByName(final String pPatientName) {
        final String patientName = StringUtils.trimToEmpty(pPatientName);
        final List<DocumentSearchPatientItemDto> result = WF_BEAN.findPatientsForDocumentImportByName(patientName, MAX_ENTRIES /*, null, null */);
        return result;
    }

    private void setDocumentSearchItem(final DocumentSearchPatientItemDto pItem) {
        MainApp.getToolbarMenuScene().openDocumentImport();
        selectedPatientIdProp.set(pItem.getPatientId());
        txtPatientName.setText(pItem.getPatientFullName());
        txtPatientNumber.setText(pItem.getPatientNumber());
        txtPatientNumber.setUserData(pItem.getPatientNumber());
        txtInsuranceNumerPatient.setText(pItem.getInsuranceNumberPatient());
        txtInsuranceNumerPatient.setUserData(pItem.getInsuranceNumberPatient());
        dtBirthdate.setDate(pItem.getPatientDateOfBirth());
        if (pItem.getCaseNumber() != null) {
            txtCaseNumber.setText(pItem.getCaseNumber());
            txtCaseNumber.setUserData(pItem.getCaseNumber());
        }
        if (pItem.getCases().size() == 1 && pItem.getProcesses().size() == 1
                && pItem.getCases().iterator().next().getCaseId().equals(pItem.getProcesses().iterator().next().getMainCase().getCaseId())) {
            DocumentSearchProcessItemDto process = pItem.getProcesses().iterator().next();
            DocumentSearchCaseItemDto cs = pItem.getCases().iterator().next();
            selectedWorkflowIdProp.set(process.getProcessId());
            selectedCaseIdProp.set(cs.getCaseId());
            txtCaseNumber.setText(cs.getCaseNumber());
            txtCaseNumber.setUserData(cs.getCaseNumber());
            txtProcessNumber.setText(String.valueOf(process.getWorkflowNumber()));
            txtProcessNumber.setUserData(String.valueOf(process.getWorkflowNumber()));
            txtInsuranceNumerPatient.setText(cs.getInsuranceNumberPatient());
        } else {
            if (pItem.getProcesses().size() == 1) {
                DocumentSearchProcessItemDto process = pItem.getProcesses().iterator().next();
                selectedWorkflowIdProp.set(process.getProcessId());
                txtProcessNumber.setText(String.valueOf(process.getWorkflowNumber()));
                txtProcessNumber.setUserData(String.valueOf(process.getWorkflowNumber()));
            } else if (pItem.getCases().size() == 1) {
                DocumentSearchCaseItemDto cs = pItem.getCases().iterator().next();
                selectedCaseIdProp.set(cs.getCaseId());
                txtCaseNumber.setText(cs.getCaseNumber());
                txtCaseNumber.setUserData(cs.getCaseNumber());
                if (cs.getInsuranceNumberPatient() != null) {
                    txtInsuranceNumerPatient.setText(cs.getInsuranceNumberPatient());
                    txtInsuranceNumerPatient.setUserData(cs.getInsuranceNumberPatient());
                }
            }
        }
        if (pItem.getCases().size() > 1 || pItem.getProcesses().size() > 1) {
            //show dialog
            final Label label = new Label("Fälle zum Patienten " + pItem.getPatientNumber());
            final TextFlow title = new TextFlow(label);
            final FormularDialog<DocumentSearchCaseItemDto> dlg = new DocumentImportCaseSelectDialog(title, pItem.getCases(), parentWindow, new Callback<DocumentSearchCaseItemDto, Void>() {
                @Override
                public Void call(DocumentSearchCaseItemDto selected) {
                    if (selected == null) {
                        return null;
                    }
                    setDocumentSearchItem(selected);
                    return null;
                }
            });

            Platform.runLater(() -> dlg.showAndWait());
        }
    }

    private void setDocumentSearchItem(final DocumentSearchProcessItemDto pItem) {
        MainApp.getToolbarMenuScene().openDocumentImport();
        selectedWorkflowIdProp.set(pItem.getProcessId());
        selectedPatientIdProp.set(pItem.getPatientId());
        txtProcessNumber.setText(String.valueOf(pItem.getWorkflowNumber()));
        txtProcessNumber.setUserData(String.valueOf(pItem.getWorkflowNumber()));
        txtPatientName.setText(pItem.getPatientFullName());
        txtPatientNumber.setText(pItem.getPatientNumber());
        txtPatientNumber.setUserData(pItem.getPatientNumber());
        txtInsuranceNumerPatient.setText(pItem.getInsuranceNumberPatient());
        txtInsuranceNumerPatient.setUserData(pItem.getInsuranceNumberPatient());
        dtBirthdate.setDate(pItem.getPatientDateOfBirth());
        DocumentSearchCaseItemDto mainCase = pItem.getMainCase();
        //selectedCaseProp.set(new DocumentSearchCaseItemDto());
        final Set<DocumentSearchCaseItemDto> hosCases = pItem.getCases();
        if (hosCases.isEmpty()) {
            //txtCaseNumber.setText(null);
        } else if (hosCases.size() == 1) {
            selectedCaseIdProp.set(mainCase.getCaseId());
            txtCaseNumber.setText(String.valueOf(mainCase.getCaseNumber()));
            txtCaseNumber.setUserData(String.valueOf(mainCase.getCaseNumber()));
        } else {
            final Label label = new Label("Fälle zum Vorgang");
            final Hyperlink caseLink = new Hyperlink(String.valueOf(pItem.getWorkflowNumber()));
            final TextFlow title = new TextFlow(label, caseLink);
            final FormularDialog<DocumentSearchCaseItemDto> dlg = new DocumentImportCaseSelectDialog(title, hosCases, parentWindow, new Callback<DocumentSearchCaseItemDto, Void>() {
                @Override
                public Void call(DocumentSearchCaseItemDto selected) {
                    if (selected == null) {
                        return null;
                    }
                    setDocumentSearchItem(selected);
                    return null;
                }
            });

            caseLink.setOnAction((evt) -> {
                final Window window = dlg.getDialogPane().getContent().getScene().getWindow();
                AlertDialog dlg2 = AlertDialog.createConfirmationDialog("Möchten Sie den Vorgang " + String.valueOf(pItem.getWorkflowNumber()) + " öffnen?", window);
                dlg2.initModality(Modality.WINDOW_MODAL);
                dlg2.showAndWait().ifPresent((ButtonType t) -> {
                    if (t.equals(ButtonType.OK)) {
                        MainApp.getToolbarMenuScene().openProcess(pItem.getProcessId());
                    }
                });
            });

            Platform.runLater(() -> dlg.showAndWait());
        }
    }

    private void setDocumentSearchItem(final DocumentSearchCaseItemDto pItem) {
        MainApp.getToolbarMenuScene().openDocumentImport();

        selectedCaseIdProp.set(pItem.getCaseId());
        selectedPatientIdProp.set(pItem.getPatientId());
        txtCaseNumber.setText(null); //forces revalidation
        txtCaseNumber.setText(pItem.getCaseNumber());
        txtCaseNumber.setUserData(pItem.getCaseNumber());
        txtInsuranceNumerPatient.setText(pItem.getInsuranceNumberPatient());
        txtInsuranceNumerPatient.setUserData(pItem.getInsuranceNumberPatient());
        txtPatientName.setText(pItem.getPatientFullName());
        txtPatientNumber.setText(pItem.getPatientNumber());
        txtPatientNumber.setUserData(pItem.getPatientNumber());
        dtBirthdate.setDate(pItem.getPatientDateOfBirth());
        DocumentSearchProcessItemDto firstProcess = pItem.getFirstProcess();
        final Set<DocumentSearchProcessItemDto> processes = pItem.getProcesses();
        if (processes.isEmpty()) {
            //txtProcessNumber.setText(null);
        } else if (processes.size() == 1) {
            selectedWorkflowIdProp.set(firstProcess.getProcessId());
            txtProcessNumber.setText(String.valueOf(firstProcess.getWorkflowNumber()));
            txtProcessNumber.setUserData(String.valueOf(firstProcess.getWorkflowNumber()));
        } else {
            Label label = new Label("Vorgänge zum Fall");
            Hyperlink caseLink = new Hyperlink(pItem.getHospitalIdent() + "/" + pItem.getCaseNumber());
            TextFlow title = new TextFlow(label, caseLink);
            final FormularDialog<DocumentSearchProcessItemDto> dlg = new DocumentImportProcessSelectDialog(title, processes, parentWindow, new Callback<DocumentSearchProcessItemDto, Void>() {
                @Override
                public Void call(DocumentSearchProcessItemDto selected) {
                    if (selected == null) {
                        return null;
                    }
                    selectedWorkflowIdProp.set(selected.getProcessId());
                    txtProcessNumber.setText(String.valueOf(selected.getWorkflowNumber()));
                    txtProcessNumber.setUserData(String.valueOf(selected.getWorkflowNumber()));
                    close();
                    return null;
                }
            });

            caseLink.setOnAction((evt) -> {
                final Window window = dlg.getDialogPane().getContent().getScene().getWindow();
                AlertDialog dlg2 = AlertDialog.createConfirmationDialog("Möchten Sie den Fall " + pItem.getCaseNumber() + " mit der Krankenhaus-IK " + pItem.getHospitalIdent() + " öffnen?", window);
                dlg2.initModality(Modality.WINDOW_MODAL);
                dlg2.showAndWait().ifPresent((ButtonType t) -> {
                    if (t.equals(ButtonType.OK)) {
                        MainApp.getToolbarMenuScene().openCase(pItem.getCaseId());
                    }
                });
            });

            Platform.runLater(() -> dlg.showAndWait());
        }
    }

    private void setCaseNumberSearch() {
        AutocompletionMultiColTextField<DocumentSearchCaseItemDto> field = txtCaseNumber.getControl();

        String[] titles = new String[]{
            "Fallnr.",
            "Fallstatus",
            "Pat.-Nr.",
            "Name",
            "Vorname",
            "Geb.dat.",
            "Aufnahme",
            "Entlassung",
            "Vorgänge"
        };

        field.getAutocompletion().setTitles(titles);
        field.getAutocompletion().setMaxEntries(MAX_ENTRIES);
        field.getAutocompletion().setItemHandler((DocumentSearchCaseItemDto param) -> {
            Glyph openCaseGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FILE);
            openCaseGlyph.setCursor(Cursor.HAND);
            openCaseGlyph.setOnMouseClicked(event -> {
                event.consume();
                AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie den Fall " + param.getCaseNumber() + " mit der Krankenhaus-IK " + param.getHospitalIdent() + " öffnen?", parentWindow);
                dlg.initModality(Modality.WINDOW_MODAL);
                dlg.showAndWait().ifPresent((ButtonType t) -> {
                    if (t.equals(ButtonType.OK)) {
                        MainApp.getToolbarMenuScene().openCase(param.getCaseId());
                    }
                });
            });
            List<Node> list = new ArrayList<>();
            list.add(openCaseGlyph);
            return list;
        });

        field.getAutocompletion().setMinFilterTextLength(MIN_CASE_NUMBER_LENGTH_FOR_AUTOCOMPLETION);
        field.getAutocompletion().setOnFilterTextChanged(event -> {
            final String text = StringUtils.trimToEmpty(field.getText2());
            final List<DocumentSearchCaseItemDto> result = findDocumentSearchCaseItems(text);
            field.getAutocompletion().setEntries(result);
        });
        field.getAutocompletion().setOnSelectEntry(event -> {
            final DocumentSearchCaseItemDto item = field.getAutocompletion().getSelectedItem();
            setDocumentSearchItem(item);
        });
    }

    private void setProcessNumberSearch() {
        AutocompletionMultiColTextField<DocumentSearchProcessItemDto> field = txtProcessNumber.getControl();

        String[] titles = new String[]{
            "Vorgangsnr.",
            "Pat.-Nr.",
            "Name",
            "Vorname",
            "Geb.dat.",
            "Fallnr.",
            "Aufnahme",
            "Entlassung",
            "Fälle"
        };

        field.getAutocompletion().setTitles(titles);
        field.getAutocompletion().setMaxEntries(MAX_ENTRIES);
        field.getAutocompletion().setItemHandler((DocumentSearchProcessItemDto param) -> {
            Glyph openProcessGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER);
            openProcessGlyph.setCursor(Cursor.HAND);
            openProcessGlyph.setOnMouseClicked(event -> {
                event.consume();
                AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie den Vorgang " + String.valueOf(param.getWorkflowNumber()) + " öffnen?", parentWindow);
                dlg.initModality(Modality.WINDOW_MODAL);
                dlg.showAndWait().ifPresent((ButtonType t) -> {
                    if (t.equals(ButtonType.OK)) {
                        MainApp.getToolbarMenuScene().openProcess(param.getProcessId());
                    }
                });
            });
            List<Node> list = new ArrayList<>();
            list.add(openProcessGlyph);
            return list;
        });

        field.getAutocompletion().setMinFilterTextLength(MIN_WORKFLOW_NUMBER_LENGTH_FOR_AUTOCOMPLETION);
        field.getAutocompletion().setOnFilterTextChanged(event -> {
            final String text = StringUtils.trimToEmpty(field.getText2());
            final List<DocumentSearchProcessItemDto> result = findDocumentSearchProcessItems(text);
            field.getAutocompletion().setEntries(result);
        });
        field.getAutocompletion().setOnSelectEntry(event -> {
            final DocumentSearchProcessItemDto item = field.getAutocompletion().getSelectedItem();
            setDocumentSearchItem(item);
        });
    }

    private void setInsuranceNumberPatientSearch() {
        AutocompletionMultiColTextField<DocumentSearchPatientItemDto> field = txtInsuranceNumerPatient.getControl();

        String[] titles = new String[]{
            "Vers.Nr.",
            "Geb.dat.",
            "Pat.-Nr.",
            "IKZ.",
            "Fallnr.",
            "Vorgangsnr.", //"Vorgänge",
        //"Fälle"
        };

        field.getAutocompletion().setTitles(titles);
        field.getAutocompletion().setMaxEntries(MAX_ENTRIES);
        field.getAutocompletion().setMinFilterTextLength(MIN_PATIENT_NAME_LENGTH_FOR_AUTOCOMPLETION);
        field.getAutocompletion().setOnFilterTextChanged(event -> {
            final String text = StringUtils.trimToEmpty(field.getText2());
            final List<DocumentSearchPatientItemDto> result = findDocumentSearchPatientItemsByInsuranceNumber(text);
            field.getAutocompletion().setEntries(result);
        });
        field.getAutocompletion().setOnSelectEntry(event -> {
            final DocumentSearchPatientItemDto item = field.getAutocompletion().getSelectedItem();
            setDocumentSearchItem(item);
        });
        field.getAutocompletion().setSplitHandler((DocumentSearchPatientItemDto item) -> new String[]{
            item.getInsuranceNumberPatient() == null ? "" : item.getInsuranceNumberPatient().trim(),
            AbstractLang.toDate(item.getPatientDateOfBirth()),
            item.getPatientNumber() == null ? "" : item.getPatientNumber().trim(),
            item.getHospitalIdent() == null ? "" : item.getHospitalIdent().trim(),
            item.getCaseNumber() == null ? "" : item.getCaseNumber().trim() + (cases.size() > 1 ? "... (" + cases.size() + ")" : ""),
            item.getWorkflowNumber() == null ? "" : String.valueOf(item.getWorkflowNumber()) + (item.getProcesses().size() > 1 ? "... (" + item.getProcesses().size() + ")" : ""), //AbstractLang.toDate(admissionDate),
        });
    }

    private void setPatientDateOfBirthSearch() {
        AutocompletionMultiColDatePicker<DocumentSearchPatientItemDto> field = dtBirthdate;

        String[] titles = new String[]{
            "Geb.dat.",
            "Vers.Nr.",
            "Pat.-Nr.",
            "IKZ.",
            "Fallnr.",
            "Vorgangsnr.",};

        field.getAutocompletion().setTitles(titles);
        field.getAutocompletion().setMaxEntries(MAX_ENTRIES);
        field.getAutocompletion().setMinFilterTextLength(10);
        field.getAutocompletion().setOnFilterTextChanged(event -> {
            final Date text = field.getDate();
            final List<DocumentSearchPatientItemDto> result = findDocumentSearchPatientItemsByDateOfBirth(text);
            field.getAutocompletion().setEntries(result);
        });

        field.getAutocompletion().setOnSelectEntry(event -> {
            final DocumentSearchPatientItemDto item = field.getAutocompletion().getSelectedItem();
            setDocumentSearchItem(item);
        });
        field.getAutocompletion().setSplitHandler((DocumentSearchPatientItemDto item) -> new String[]{
            AbstractLang.toDate(item.getPatientDateOfBirth()),
            item.getInsuranceNumberPatient() == null ? "" : item.getInsuranceNumberPatient().trim(),
            item.getPatientNumber() == null ? "" : item.getPatientNumber().trim(),
            item.getHospitalIdent() == null ? "" : item.getHospitalIdent().trim(),
            item.getCaseNumber() == null ? "" : item.getCaseNumber().trim() + (cases.size() > 1 ? "... (" + cases.size() + ")" : ""),
            item.getWorkflowNumber() == null ? "" : String.valueOf(item.getWorkflowNumber()) + (item.getProcesses().size() > 1 ? "... (" + item.getProcesses().size() + ")" : ""), //AbstractLang.toDate(admissionDate),
        });
    }

    private void setPatientNameSearch() {
        AutocompletionMultiColTextField<DocumentSearchPatientItemDto> field = txtPatientName.getControl();

        String[] titles = new String[]{
            "Name",
            "Vorname",
            "Geb.dat.",
            "Pat.-Nr.",
            "Fallnr.",
            "Vorgangsnr.", //"Vorgänge",
        //"Fälle"
        };

        field.getAutocompletion().setTitles(titles);
        field.getAutocompletion().setMaxEntries(MAX_ENTRIES);
        field.getAutocompletion().setMinFilterTextLength(MIN_PATIENT_NAME_LENGTH_FOR_AUTOCOMPLETION);
        field.getAutocompletion().setOnFilterTextChanged(event -> {
            final String text = StringUtils.trimToEmpty(field.getText2());
            final List<DocumentSearchPatientItemDto> result = findDocumentSearchPatientItemsByName(text);
            field.getAutocompletion().setEntries(result);
        });
        field.getAutocompletion().setOnSelectEntry(event -> {
            final DocumentSearchPatientItemDto item = field.getAutocompletion().getSelectedItem();
            setDocumentSearchItem(item);
        });
//        field.setSplitHandler((DocumentSearchPatientItemDto item) -> new String[]{
//            item.getPatientSecName() == null ? "" : item.getPatientSecName().trim(),
//            item.getPatientFirstName() == null ? "" : item.getPatientFirstName().trim(),
//            AbstractLang.toDate(item.getPatientDateOfBirth()),
//            item.getPatientNumber() == null ? "" : item.getPatientNumber().trim(),
//            caseNumber == null ? "" : caseNumber.trim() + (cases.size() > 1 ? "... (" + cases.size() + ")" : ""),
//            item.getWorkflowNumber() == null ? "" : String.valueOf(item.getWorkflowNumber()) + (item.getProcesses().size() > 1 ? "... (" + item.getProcesses().size() + ")" : ""), //AbstractLang.toDate(admissionDate),
//        });
    }

    private void setPatientNumberSearch() {
        AutocompletionMultiColTextField<DocumentSearchPatientItemDto> field = txtPatientNumber.getControl();

        String[] titles = new String[]{
            "Pat.-Nr.",
            "Name",
            "Vorname",
            "Geb.dat.",
            "Fallnr.",
            "Vorgangsnr.", //"Vorgänge",
        //"Fälle"
        };

        field.getAutocompletion().setTitles(titles);
        field.getAutocompletion().setMaxEntries(MAX_ENTRIES);
        field.getAutocompletion().setMinFilterTextLength(MIN_PATIENT_NUMBER_LENGTH_FOR_AUTOCOMPLETION);
        field.getAutocompletion().setOnFilterTextChanged(event -> {
            final String text = StringUtils.trimToEmpty(field.getText2());
            final List<DocumentSearchPatientItemDto> result = findDocumentSearchPatientItemsByNumber(text);
            field.getAutocompletion().setEntries(result);
        });
        field.getAutocompletion().setOnSelectEntry(event -> {
            final DocumentSearchPatientItemDto item = field.getAutocompletion().getSelectedItem();
            setDocumentSearchItem(item);
        });
    }

    @FXML
    private void openCase(ActionEvent event) {
        final TCase cs = WF_BEAN.findCase(selectedCaseIdProp.get());
        AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie den Fall " + cs.getCsCaseNumber() + " mit der Krankenhaus-IK " + cs.getCsHospitalIdent() + " öffnen?", parentWindow);
        dlg.initModality(Modality.WINDOW_MODAL);
        dlg.showAndWait().ifPresent((ButtonType t) -> {
            if (t.equals(ButtonType.OK)) {
                MainApp.getToolbarMenuScene().openCase(cs.id);
            }
        });
    }

    @FXML
    private void openProcess(ActionEvent event) {
        final ProcessServiceFacade facade = new ProcessServiceFacade(selectedWorkflowIdProp.get(), false);
        TWmProcess process = facade.getCurrentProcess();
        //final Long workflowId = selectedWorkflowIdProp.get();
        //Long workflowNumber = processServiceBean.get().findSingleProcessNumberForId(workflowId);
        //String workflowNumber = txtProcessNumber.getText2();
        AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie den Vorgang " + String.valueOf(process.getWorkflowNumber()) + " öffnen?", parentWindow);
        dlg.initModality(Modality.WINDOW_MODAL);
        dlg.showAndWait().ifPresent((ButtonType t) -> {
            if (t.equals(ButtonType.OK)) {
                MainApp.getToolbarMenuScene().openProcess(process.id);
            }
        });
    }

    public void setCaseNumber(final String pCaseNumber) {
        txtCaseNumber.setText(StringUtils.trimToEmpty(pCaseNumber));
        txtCaseNumber.startSearch();
    }

    public void setProcessNumber(final String pProcessNumber) {
        txtProcessNumber.setText(StringUtils.trimToEmpty(pProcessNumber));
        txtProcessNumber.startSearch();
    }

    public void setPatientNumber(final String pPatientNumber) {
        txtPatientNumber.setText(StringUtils.trimToEmpty(pPatientNumber));
        txtPatientNumber.startSearch();
    }

    public void setInsuranceNumberPatient(final String pPatientNumber) {
        txtInsuranceNumerPatient.setText(StringUtils.trimToEmpty(pPatientNumber));
        txtInsuranceNumerPatient.startSearch();
    }

    public ReadOnlyBooleanProperty invalidProperty() {
        return validationSupport.invalidProperty();
    }

    public boolean isInvalid() {
        return validationSupport.isInvalid();
    }

    public String getWorkflowNumber() {
        return txtProcessNumber.getText();
    }

    public Long getSelectedCaseId() {
        return selectedCaseIdProp.get();
    }

    public Long getSelectedWorkflowId() {
        return selectedWorkflowIdProp.get();
    }

    public Long getSelectedPatientId() {
        return selectedPatientIdProp.get();
    }

}

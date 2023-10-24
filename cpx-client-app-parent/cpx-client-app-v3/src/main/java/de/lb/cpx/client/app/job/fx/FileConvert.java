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

import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.converter.DocumentConverterResult;
import de.lb.cpx.document.Utils;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.shared.dto.DocumentSearchCaseItemDto;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author niemeier
 */
public class FileConvert {

    private static final Logger LOG = Logger.getLogger(FileConvert.class.getName());

    public final File file;
    private int priority;
    public final HBox statusPane;
    public final ProgressIndicator progressIndicator;
    public final CheckBox checkBox;
    //public final Label caseNumberLabel;
    public final Utils.FILE_TYPES fileType;
    //public final HBox resultGlyph;
    private DocumentConverterResult docResult;
    private DocumentImportFieldsScene scene;
    private DocumentImportFieldsFXMLController ctrl;
    private Pane root;
    private final Window parentWindow;
    private final boolean isRoot;

    private String caseNumber = "";
    private String patientNumber = "";
    private List<TCase> cases = new ArrayList<>();
    private List<TCase> potentialCases = new ArrayList<>();
    private List<TPatient> patients = new ArrayList<>();
    private List<TPatient> potentialPatients = new ArrayList<>();
    private ObjectProperty<DocumentSearchCaseItemDto> presetCaseWorkflowItemProperty = new SimpleObjectProperty<>();
    private final BooleanProperty multiSelectProperty = new SimpleBooleanProperty(false);

    public boolean isFile() {
        return file.isFile();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public FileConvert(final Window pParentWindow, final File pFile, final boolean pIsRoot) {
        this(pParentWindow, pFile, pIsRoot, 0);
    }

    public boolean isRoot() {
        return isRoot;
    }

    public HBox getStatusPane() {
        return statusPane;
    }

    public final void setStatusNode(Control pControl) {
        if (statusPane == null) {
            LOG.log(Level.INFO, "status pane is null!");
            return;
        }
        if (pControl == null) {
            statusPane.getChildren().clear();
            return;
        }
        pControl.setMinWidth(15d);
        pControl.setMaxWidth(pControl.getMinWidth());
        pControl.setMinHeight(15d);
        pControl.setMaxHeight(pControl.getMinHeight());
        StackPane.setAlignment(pControl, Pos.CENTER);
        pControl.setPadding(new Insets(0));

        //statusPane.getChildren().clear();
        statusPane.getChildren().setAll(checkBox, pControl);
    }

    public Node getStatusNode() {
        List<Node> nodes = new ArrayList<>(statusPane.getChildren());
        if (nodes.isEmpty()) {
            return null;
        }
        return nodes.get(0);
    }

    public final void setStatusResult(final Glyph pGlyph) {
        if (pGlyph != null) {
            pGlyph.setGraphicTextGap(0d);
            pGlyph.setAlignment(Pos.CENTER);
        }
        setStatusNode(pGlyph);
    }

    public final void setStatusProgress(final ProgressIndicator pProgressIndicator) {
        setStatusNode(pProgressIndicator);
    }

//    public void setResultGlyph(final Glyph pGlyph) {
//        resultGlyph.getChildren().setAll(pGlyph);
//        resultGlyph.setPrefWidth(15d);
//        resultGlyph.setMaxHeight(15d);
//        //resultGlyph.setMaxHeight(15d);            
////        if (pGlyph != null) {
////            resultGlyph.setPrefWidth(15d);
////            resultGlyph.setMaxHeight(15d);            
////        } else {
////            resultGlyph.setPrefWidth(0d);
////            resultGlyph.setMaxHeight(0d);            
////        }
//    }    
//    public FileConvert(final File file) {
//        this(file, 0);
//    }
    public FileConvert(final Window pParentWindow, final File pFile, final boolean pIsRoot, final int pPriority) {
        this.parentWindow = pParentWindow;
        this.file = pFile;
        this.priority = pPriority;
        this.isRoot = pIsRoot;
        this.progressIndicator = new ProgressIndicator();
        this.checkBox = new CheckBox();
        checkBox.setPadding(new Insets(0, 5, 0, 0));
        //this.caseNumberLabel = file.isDirectory() ? null : new Label("...");
        this.fileType = Utils.getFileType(pFile);
        //this.resultGlyph = new HBox();
        //this.resultGlyph.setAlignment(Pos.CENTER);
//        statusPane.setMinWidth(15d);
//        statusPane.setMaxWidth(statusPane.getMinWidth());
//        statusPane.setMinHeight(15d);
//        statusPane.setMaxHeight(statusPane.getMinHeight());
        //if (this.progressIndicator != null) {
        if (isRoot) {
            statusPane = null;
        } else {
            statusPane = new HBox();
            setStatusProgress(progressIndicator);
        }
        if (file.isDirectory()) {
            progressIndicator.setVisible(false);
            checkBox.setVisible(false);
        }
        //}
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int pPriority) {
        priority = pPriority;
    }

    public void setDocumentConverterResult(final DocumentConverterResult pDocResult) {
        this.docResult = pDocResult;
    }

    public DocumentConverterResult getDocumentConverterResult() {
        return this.docResult;
    }

    private void setScene(final DocumentImportFieldsScene pScene) {
        scene = pScene;
        root = scene == null ? null : (Pane) scene.getRoot();
    }

    public boolean hasScene() {
        return scene != null;
    }

    public boolean hasController() {
        return ctrl != null;
    }

    public DocumentImportFieldsScene getScene() {
        if (scene == null) {
            initSceneController();
        }
        return scene;
    }

    private void setController(final DocumentImportFieldsFXMLController pCtrl) {
        ctrl = pCtrl;
    }

    public DocumentImportFieldsFXMLController getController() {
        if (ctrl == null) {
            initSceneController();
        }
        return ctrl;
    }

    public Pane getRoot() {
        return root;
    }

    public ReadOnlyBooleanProperty invalidProperty() {
        return getController().invalidProperty();
    }

    public boolean isInvalid() {
        if (ctrl == null) {
            return true;
        } else {
            return ctrl.isInvalid();
        }
        //return getController().isInvalid();
    }

    private synchronized void initSceneController() {
        if (scene == null && ctrl == null) {
            try {
                LOG.log(Level.INFO, "Create Scene & Controller for Document " + file.getName());
                DocumentImportFieldsScene sc = new DocumentImportFieldsScene();
                //Pane r = (Pane) sc.getRoot();
                //stackDetails.getChildren().add(root);
                DocumentImportFieldsFXMLController ct = sc.getController();
                //ct.setDocumentDate(new Date(file.lastModified()));
                ct.setFileConvert(this);
                ct.setParentWindow(parentWindow);
                //fc.setScene(scene);
                //fc.setController(ctrl);
                setScene(sc);
                setController(ct);
                //root = r;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

//    public Pane getResultGlyph() {
//        return resultGlyph;
//    }
//    public void setResultGlyph(final Glyph pGlyph) {
//        resultGlyph.getChildren().setAll(pGlyph);
//        resultGlyph.setPrefWidth(15d);
//        resultGlyph.setMaxHeight(15d);
//        //resultGlyph.setMaxHeight(15d);            
////        if (pGlyph != null) {
////            resultGlyph.setPrefWidth(15d);
////            resultGlyph.setMaxHeight(15d);            
////        } else {
////            resultGlyph.setPrefWidth(0d);
////            resultGlyph.setMaxHeight(0d);            
////        }
//    }
    @Override
    public String toString() {
        return file.getName();
    }

    public FileConvert getCopy() {
        FileConvert fc = new FileConvert(parentWindow, file, isRoot, priority);
        fc.setDocumentConverterResult(getDocumentConverterResult());
        fc.setScene(getScene());
        fc.setController(getController());
        return fc;
    }

    public void setCases(final String pCaseNumber, final List<TCase> pCases, final List<TCase> pPotentialCases) {
        caseNumber = pCaseNumber == null ? "" : pCaseNumber.trim();
        cases = pCases == null ? new ArrayList<>() : new ArrayList<>(pCases);
        potentialCases = pPotentialCases == null ? new ArrayList<>() : new ArrayList<>(pPotentialCases);
        if (hasController()) {
            ctrl.setCases(caseNumber, pCases, pPotentialCases);
        }
        findPresetCaseWorkflowItem();
    }

    private void findPresetCaseWorkflowItem() {
        if (caseNumber.isEmpty()) {
            return;
        }
        final List<DocumentSearchCaseItemDto> result = DocumentImportFieldsFXMLController.findDocumentSearchCaseItems(caseNumber);
        if (result.size() == 1) {
            //found exactly one case. Preset it as selected.
            final DocumentSearchCaseItemDto item = result.iterator().next();
            if (item.getMainProcesses().size() <= 1) {
                //exactly one case is assigned to one process. Awesome! Will suggest this entry to the user!
                presetCaseWorkflowItemProperty.set(item);
            }
        }
    }

    public DocumentSearchCaseItemDto getPresetCaseWorkflowItem() {
        return presetCaseWorkflowItemProperty.get();
    }

    public ObjectProperty<DocumentSearchCaseItemDto> presetCaseWorkflowItemProperty() {
        return presetCaseWorkflowItemProperty;
    }

    public void setPatients(final String pPatientNumber, final List<TPatient> pPatients, final List<TPatient> pPotentialPatients) {
        patientNumber = pPatientNumber == null ? "" : pPatientNumber.trim();
        patients = pPatients == null ? new ArrayList<>() : new ArrayList<>(pPatients);
        potentialPatients = pPotentialPatients == null ? new ArrayList<>() : new ArrayList<>(pPotentialPatients);
        if (hasController()) {
            ctrl.setPatients(patientNumber, pPatients, pPotentialPatients);
        }
    }

    public List<TCase> getCases() {
        return new ArrayList<>(cases);
    }

    public List<TCase> getPotentialCases() {
        return new ArrayList<>(potentialCases);
    }

    public List<TPatient> getPatients() {
        return new ArrayList<>(patients);
    }

    public List<TPatient> getPotentialPatients() {
        return new ArrayList<>(potentialPatients);
    }

    public Glyph getCaseGlyph() {
        Glyph glyph;
        final Tooltip t;
        //final double doubleGlyphGap = -1d;
        //final double doubleGlyphWidth = 20d;
        if (caseNumber.isEmpty()) {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.TIMES);
            glyph.setStyle("-fx-text-fill: red;");
            t = new Tooltip("Fallnummer nicht erkannt");
        } else if (cases.isEmpty() && potentialCases.isEmpty()) {
            //single red cross if no case were found
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.TIMES);
            glyph.setStyle("-fx-text-fill: red;");
            t = new Tooltip("Fallnummer " + caseNumber + " existiert nicht");
        } else if (cases.size() == 1) {
            //single check glyph if exactly one case where found            
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.CHECK);
            glyph.setStyle("-fx-text-fill: green;");
            t = new Tooltip("Fallnummer " + caseNumber + " gefunden");
        } else if (cases.size() > 1) {
            //show double check glyphs if multiple cases were found
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.LIST);
//            Glyph doubleGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.CHECK);
//            doubleGlyph.setAlignment(Pos.CENTER);
//            glyph.setGraphic(doubleGlyph);
//            glyph.setGraphicTextGap(doubleGlyphGap);
            //glyph.setPrefWidth(doubleGlyphWidth);
            glyph.setStyle("-fx-text-fill: green;");
//            doubleGlyph.setStyle("-fx-text-fill: green;");
            t = new Tooltip("Mehrere Fälle mit der Fallnummer " + caseNumber + " gefunden");
        } else if (potentialCases.size() == 1) {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION);
            glyph.setStyle("-fx-text-fill: blue;");
            t = new Tooltip("Ähnliche Fallnummer zu " + caseNumber + " gefunden");
        } else {
            //show double question glyphs if multiple potential cases were found
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.LIST);
//            Glyph doubleGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION);
//            doubleGlyph.setAlignment(Pos.CENTER);
//            glyph.setGraphic(doubleGlyph);
//            glyph.setGraphicTextGap(doubleGlyphGap);
            //glyph.setPrefWidth(doubleGlyphWidth);
            glyph.setStyle("-fx-text-fill: blue;");
//            doubleGlyph.setStyle("-fx-text-fill: blue;");
            t = new Tooltip("Mehrere ähnliche Fälle zu der Fallnummer " + caseNumber + " gefunden");
        }
        glyph.setPadding(new Insets(0, 0, 0, 3));
        glyph.setAlignment(Pos.CENTER);

        //final String caseNumber = getDocument().getCaseNumber();
        Tooltip.install(glyph, t);
        return glyph;
    }
//same as GetCaseGlyph, only for Patient

    public Glyph getPatientGlyph() {
        Glyph glyph;
        final Tooltip t;

        if (patientNumber.isEmpty()) {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.TIMES);
            glyph.setStyle("-fx-text-fill: red;");
            t = new Tooltip("Patientennummer nicht erkannt");
        } else if (patients.isEmpty() && potentialPatients.isEmpty()) {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.TIMES);
            glyph.setStyle("-fx-text-fill: red;");
            t = new Tooltip("Patientennummer " + patientNumber + " existiert nicht");
        } else if (patients.size() == 1) {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.CHECK);
            glyph.setStyle("-fx-text-fill: green;");
            t = new Tooltip("Patientennummer " + patientNumber + " gefunden");
        } else if (patients.size() > 1) {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.LIST);
            glyph.setStyle("-fx-text-fill: green;");
            t = new Tooltip("Mehrere Patienten mit der Patietennummer " + patientNumber + " gefunden");
        } else if (potentialPatients.size() == 1) {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION);
            glyph.setStyle("-fx-text-fill: blue;");
            t = new Tooltip("Ähnliche Patientennummer zu " + patientNumber + " gefunden");
        } else {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.LIST);
            glyph.setStyle("-fx-text-fill: blue;");
            t = new Tooltip("Mehrere ähnliche Patienten zu der Patientennummer " + patientNumber + " gefunden");
        }
        glyph.setPadding(new Insets(0, 0, 0, 3));
        glyph.setAlignment(Pos.CENTER);
        Tooltip.install(glyph, t);
        return glyph;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.file);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileConvert other = (FileConvert) obj;
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public Date getFileDate() {
        return new Date(file.lastModified());
    }

    public String getFileName() {
        return file.getName();
    }

    public void setStatusProgress() {
        progressIndicator.setVisible(true);
        setStatusProgress(progressIndicator);
    }

    public static Glyph getDocTypeGlyph(Utils.FILE_TYPES fileType) {
        final Glyph docTypeGlyph;
        if (fileType.isPdf()) {
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FILE_PDF_ALT);
            docTypeGlyph.setStyle("-fx-text-fill: red;");
        } else if (fileType.isCsv()) {
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.TABLE);
        } else if (fileType.isExcel()) {
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FILE_EXCEL_ALT);
            docTypeGlyph.setStyle("-fx-text-fill: green;");
        } else if (fileType.isImage()) {
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FILE_IMAGE_ALT);
        } else if (fileType.isText()) {
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FILE_TEXT);
        } else if (fileType.isWord()) {
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FILE_WORD_ALT);
            docTypeGlyph.setStyle("-fx-text-fill: blue;");
            //glyph.setColor(Color.BLUE);
        } else if (fileType.isMessage()) {
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE_ALT);
            docTypeGlyph.setStyle("-fx-text-fill: darkorange;");
        } else {
            //Misc, Other, Unknown
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FILE);
        }
        return docTypeGlyph;
    }

    public Glyph getDocTypeGlyph() {
        final Glyph docTypeGlyph;
        if (isDirectory()) {
            docTypeGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER_OPEN);
        } else {
            docTypeGlyph = getDocTypeGlyph(this.fileType);
        }
        docTypeGlyph.setPrefWidth(20d);
        docTypeGlyph.setAlignment(Pos.CENTER);
        return docTypeGlyph;
    }

    public String getWorkflowNumber() {
        return getController().getWorkflowNumber();
    }

    public Long getSelectedCaseId() {
        return getController().getSelectedCaseId();
    }

    public Long getWorkflowID(String workflowNumber) {
        return getController().getWorkflowID(workflowNumber);
    }

    public Long getSelectedPatientId() {
        return getController().getSelectedPatientId();
    }

    public BooleanProperty multiSelectProperty() {
        return multiSelectProperty;
    }

//    public void setImported(boolean pImported) {
//        getController().setImported(pImported);
//    }
}

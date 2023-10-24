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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.patient_health_status_details;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.catalog.CpxIcd;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.patient.status.HealthStatusApp;
import de.lb.cpx.patient.status.organ.AbstractHumanPartGraphic;
import de.lb.cpx.patient.status.organ.HumanPartGraphic;
import de.lb.cpx.patient.status.organ.OrganSeverity;
import de.lb.cpx.service.ejb.AcgServiceEJBRemote;
import de.lb.cpx.shared.dto.acg.AcgIndexType;
import de.lb.cpx.shared.dto.acg.AcgPatientData;
import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * Class to draw ACG visualization and manage events
 *
 * @author niemeier
 */
public class PatientHealthStatusVisualization {

    private static final Logger LOG = Logger.getLogger(PatientHealthStatusVisualization.class.getName());
//    private TextFlow datenjahrLabel;

    /**
     * Compact or fullscreen version to be displayed
     */
    public enum AcgVisualizationType {
        COMPACT, FULLSCREEN
    };

    private Integer maxDatenjahr = null;
    private Integer minDatenjahr = null;
    public static final int MAX_WIDTH_MORBIDITY = 500;
    public static final int MAX_WIDTH_DISEASES = 800;

    private Map<Integer, AcgPatientData> acgPatientDataMap;
    private static final EjbProxy<AcgServiceEJBRemote> ACG_SERVICE_BEAN = Session.instance().getEjbConnector().connectAcgServiceBean();
    private String patNumber;
    private String gender;
    private final Tooltip sliderToolTip = new Tooltip();

    /**
     * Patient number for this visualization
     *
     * @return unique patient number
     */
    public String getPatNumber() {
        return patNumber;
    }

    /**
     * Creates ACG visualization for a specific patient
     *
     * @param pPatient patient
     */
    public PatientHealthStatusVisualization(final TPatient pPatient) {
        this(pPatient == null ? "" : pPatient.getPatNumber(),
                (pPatient == null || pPatient.getPatGenderEn() == null) ? "" : pPatient.getPatGenderEn().name()
        );
    }

    /**
     * Creates ACG visualization for a specific patient number
     *
     * @param pPatNumber patient number
     * @param pGender use this gender for rendering if there's no ACG data for
     * patient
     */
    public PatientHealthStatusVisualization(final String pPatNumber, final String pGender) {
        final String lPatNumber = pPatNumber == null ? "" : pPatNumber.trim();
        if (lPatNumber.isEmpty()) {
            throw new IllegalArgumentException("Patient number is null or empty!");
        }
        this.patNumber = lPatNumber;
        this.gender = pGender == null ? "" : pGender.trim();
    }

    /**
     * Gives you a compact version of the visualization (for the right bottom
     * details view)
     *
     * @param pCase hospital case to render
     * @return box with all the stuff you need
     */
    public VBox getCompactContent(final TCase pCase) {
        return getContent(AcgVisualizationType.COMPACT, pCase);
    }

    /**
     * Gives you a compact version of the visualization (for the right bottom
     * details view)
     *
     * @return box with all the stuff you need
     */
    public VBox getCompactContent() {
        return getContent(AcgVisualizationType.COMPACT, null);
    }

    /**
     * Gives you a fullscreen version of the visualization
     *
     * @return box with all the stuff you need
     */
    public VBox getFullscreenContent() {
        return getContent(AcgVisualizationType.FULLSCREEN, null);
    }

    /**
     * Creates a box with the morbidity summary
     *
     * @param pAcgPatientData ACG data
     * @return box with information grids
     */
    public VBox createMorbiditySummaryBox(final AcgPatientData pAcgPatientData) {
        final VBox morbiditySummaryBox = new VBox();
        if (pAcgPatientData == null) {
            return morbiditySummaryBox;
        }
        morbiditySummaryBox.setSpacing(10.0d);
        final GridPane patientInfo = new GridPane();
        patientInfo.getStyleClass().add("acg_box");
        patientInfo.addRow(1, new Text("Alter:"), new Text(String.valueOf(pAcgPatientData.alter)));
        patientInfo.addRow(2, new Text("Geschlecht:"), new Text(String.valueOf(pAcgPatientData.getGeschlechtDescription())));
        patientInfo.addRow(3, new Text("Anzahl chronischer Erkrankungen:"), new Text(String.valueOf(pAcgPatientData.chronische_erkrankungen_patient)));
        patientInfo.setHgap(5d);
        patientInfo.setVgap(5d);
        patientInfo.getColumnConstraints().add(0, new ColumnConstraints());
        patientInfo.getColumnConstraints().add(1, new ColumnConstraints());
        patientInfo.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);
        patientInfo.getColumnConstraints().get(1).setHalignment(HPos.RIGHT);
        morbiditySummaryBox.getChildren().add(patientInfo);

        //chronicConditionInfo.setGridLinesVisible(true);
        final Text indexErkrankungenLabel = new Text("Indexerkrankungen");
        indexErkrankungenLabel.getStyleClass().add("acg_title");
        morbiditySummaryBox.getChildren().add(indexErkrankungenLabel);

        if (pAcgPatientData.has_chronic_conditions) {
            final GridPane chronicConditionInfo = new GridPane();
            chronicConditionInfo.getStyleClass().add("acg_box");
            int row = 0;
            Text icdsTitle = new Text("Diagnosen\nerkannt");
            Text medsTitle = new Text("Medikamente\nerkannt");
            Text trtsTitle = new Text("durchgehende\nBehandlung");
            icdsTitle.setTextAlignment(TextAlignment.CENTER);
            medsTitle.setTextAlignment(TextAlignment.CENTER);
            trtsTitle.setTextAlignment(TextAlignment.CENTER);
            icdsTitle.getStyleClass().add("acg_grid_title");
            medsTitle.getStyleClass().add("acg_grid_title");
            trtsTitle.getStyleClass().add("acg_grid_title");
            chronicConditionInfo.addRow(row++, new Text("Erkrankung"), icdsTitle, medsTitle, trtsTitle);
            if (!AcgIndexType.NP.equals(pAcgPatientData.adipositas)) {
                chronicConditionInfo.addRow(row++, new Text("Adipositas"), icd(pAcgPatientData.adipositas), rx(pAcgPatientData.adipositas), trt(pAcgPatientData.adipositas));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.altersbedingte_makuladegeneration)) {
                chronicConditionInfo.addRow(row++, new Text("Altersbedingte Makuladegeneration"), icd(pAcgPatientData.altersbedingte_makuladegeneration), rx(pAcgPatientData.altersbedingte_makuladegeneration), trt(pAcgPatientData.altersbedingte_makuladegeneration));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.asthma)) {
                chronicConditionInfo.addRow(row++, new Text("Asthma"), icd(pAcgPatientData.asthma), rx(pAcgPatientData.asthma), trt(pAcgPatientData.asthma));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.bipolare_stoerung)) {
                chronicConditionInfo.addRow(row++, new Text("Bipolare Störung"), icd(pAcgPatientData.bipolare_stoerung), rx(pAcgPatientData.bipolare_stoerung), trt(pAcgPatientData.bipolare_stoerung));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.bluthochdruck)) {
                chronicConditionInfo.addRow(row++, new Text("Bluthochdruck"), icd(pAcgPatientData.bluthochdruck), rx(pAcgPatientData.bluthochdruck), trt(pAcgPatientData.bluthochdruck));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.copd)) {
                chronicConditionInfo.addRow(row++, new Text("COPD"), icd(pAcgPatientData.copd), rx(pAcgPatientData.copd), trt(pAcgPatientData.copd));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.depression)) {
                chronicConditionInfo.addRow(row++, new Text("Depression"), icd(pAcgPatientData.depression), rx(pAcgPatientData.depression), trt(pAcgPatientData.depression));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.diabetes)) {
                chronicConditionInfo.addRow(row++, new Text("Diabetes"), icd(pAcgPatientData.diabetes), rx(pAcgPatientData.diabetes), trt(pAcgPatientData.diabetes));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.fettstoffwechselstoerung)) {
                chronicConditionInfo.addRow(row++, new Text("Fettstoffwechselstörung"), icd(pAcgPatientData.fettstoffwechselstoerung), rx(pAcgPatientData.fettstoffwechselstoerung), trt(pAcgPatientData.fettstoffwechselstoerung));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.glaukom)) {
                chronicConditionInfo.addRow(row++, new Text("Glaukom"), icd(pAcgPatientData.glaukom), rx(pAcgPatientData.glaukom), trt(pAcgPatientData.glaukom));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.herzinsuffizienz)) {
                chronicConditionInfo.addRow(row++, new Text("Herzinsuffizienz"), icd(pAcgPatientData.herzinsuffizienz), rx(pAcgPatientData.herzinsuffizienz), trt(pAcgPatientData.herzinsuffizienz));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.hiv)) {
                chronicConditionInfo.addRow(row++, new Text("HIV"), icd(pAcgPatientData.hiv), rx(pAcgPatientData.hiv), trt(pAcgPatientData.hiv));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.immunsuppression)) {
                chronicConditionInfo.addRow(row++, new Text("Immunsuppression"), icd(pAcgPatientData.immunsuppression), rx(pAcgPatientData.immunsuppression), trt(pAcgPatientData.immunsuppression));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.koronare_herzerkrankung)) {
                chronicConditionInfo.addRow(row++, new Text("Koronare Herzerkrankung"), icd(pAcgPatientData.koronare_herzerkrankung), rx(pAcgPatientData.koronare_herzerkrankung), trt(pAcgPatientData.koronare_herzerkrankung));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.morbus_parkinson)) {
                chronicConditionInfo.addRow(row++, new Text("Morbus Parkinson"), icd(pAcgPatientData.morbus_parkinson), rx(pAcgPatientData.morbus_parkinson), trt(pAcgPatientData.morbus_parkinson));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.nierenversagen)) {
                chronicConditionInfo.addRow(row++, new Text("Nierenversagen"), icd(pAcgPatientData.nierenversagen), rx(pAcgPatientData.nierenversagen), trt(pAcgPatientData.nierenversagen));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.osteoporose)) {
                chronicConditionInfo.addRow(row++, new Text("Osteoporose"), icd(pAcgPatientData.osteoporose), rx(pAcgPatientData.osteoporose), trt(pAcgPatientData.osteoporose));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.rheumatoide_arthritis)) {
                chronicConditionInfo.addRow(row++, new Text("Rheumatoide Arthritis"), icd(pAcgPatientData.rheumatoide_arthritis), rx(pAcgPatientData.rheumatoide_arthritis), trt(pAcgPatientData.rheumatoide_arthritis));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.rueckenschmerz)) {
                chronicConditionInfo.addRow(row++, new Text("Rückenschmerz"), icd(pAcgPatientData.rueckenschmerz), rx(pAcgPatientData.rueckenschmerz), trt(pAcgPatientData.rueckenschmerz));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.schilddruesenunterfunktion)) {
                chronicConditionInfo.addRow(row++, new Text("Schilddrüsenunterfunktion"), icd(pAcgPatientData.schilddruesenunterfunktion), rx(pAcgPatientData.schilddruesenunterfunktion), trt(pAcgPatientData.schilddruesenunterfunktion));
            }
            if (!AcgIndexType.NP.equals(pAcgPatientData.schizophrenie)) {
                chronicConditionInfo.addRow(row++, new Text("Schizophrenie"), icd(pAcgPatientData.schizophrenie), rx(pAcgPatientData.schizophrenie), trt(pAcgPatientData.schizophrenie));
            }

            chronicConditionInfo.setHgap(5d);
            chronicConditionInfo.setVgap(5d);
            chronicConditionInfo.getColumnConstraints().add(0, new ColumnConstraints());
            chronicConditionInfo.getColumnConstraints().add(1, new ColumnConstraints());
            chronicConditionInfo.getColumnConstraints().add(2, new ColumnConstraints());
            chronicConditionInfo.getColumnConstraints().add(3, new ColumnConstraints());
            chronicConditionInfo.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
            chronicConditionInfo.getColumnConstraints().get(0).setHalignment(HPos.LEFT);
            chronicConditionInfo.getColumnConstraints().get(1).setHalignment(HPos.CENTER);
            chronicConditionInfo.getColumnConstraints().get(2).setHalignment(HPos.CENTER);
            chronicConditionInfo.getColumnConstraints().get(3).setHalignment(HPos.CENTER);

            morbiditySummaryBox.getChildren().add(chronicConditionInfo);
        } else {
            Text noChronicConditionsLabel = new Text("Keine chronischen Erkrankungen gefunden");
            morbiditySummaryBox.getChildren().add(noChronicConditionsLabel);
        }

        final GridPane patientSeverityInfo = new GridPane();
        patientSeverityInfo.getStyleClass().add("acg_box");
        final Text aktuellesFallgewichtText = new Text(String.format("%.1f", pAcgPatientData.relativgewicht_patient));
        final Text ressourcenverbrauchText = new Text(pAcgPatientData.getResourceUtilizationBandDescription());
        final Text gebrechlichText = new Text(pAcgPatientData.getFrailtyFlagDescription());
        if (pAcgPatientData.relativgewicht_patient >= 3.5d) {
            aktuellesFallgewichtText.setStyle("-fx-fill: red; -fx-font-weight: bold;");
        }
        if (pAcgPatientData.ressourcenverbrauchsgruppe_patient >= 4) {
            ressourcenverbrauchText.setStyle("-fx-fill: red; -fx-font-weight: bold;");
        }
        if (pAcgPatientData.gebrechlichkeit) {
            gebrechlichText.setStyle("-fx-fill: red; -fx-font-weight: bold;");
        }
        TextFlow ressourcenverbrauchTextflow = new TextFlow(ressourcenverbrauchText, new Text(" (Gruppe " + pAcgPatientData.ressourcenverbrauchsgruppe_patient + " von 5)"));
        ressourcenverbrauchTextflow.setTextAlignment(TextAlignment.RIGHT);
        patientSeverityInfo.addRow(0, new Text("Aktuelles Fallgewicht:"), aktuellesFallgewichtText);
        patientSeverityInfo.addRow(1, new Text("Ressourcenverbrauch:"), ressourcenverbrauchTextflow);
        patientSeverityInfo.addRow(2, new Text("Gebrechlichkeit:"), gebrechlichText);
        patientSeverityInfo.setHgap(5d);
        patientSeverityInfo.setVgap(5d);
        patientSeverityInfo.getColumnConstraints().add(0, new ColumnConstraints());
        patientSeverityInfo.getColumnConstraints().add(1, new ColumnConstraints());
        patientSeverityInfo.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);
        patientSeverityInfo.getColumnConstraints().get(1).setHalignment(HPos.RIGHT);
        morbiditySummaryBox.getChildren().add(patientSeverityInfo);
        return morbiditySummaryBox;
    }

    /**
     * Creates a box with the diseases and puts it into a scrollable pane
     *
     * @param pAcgPatientData ACG data
     * @param pHumanPartList organs and groups of organs
     * @return pane with avatar and scrollable diseases
     */
    public ScrollPane createDiseasesExplanationScrollPane(final AcgPatientData pAcgPatientData, final Map<Integer, HumanPartGraphic> pHumanPartList) {
        final VBox diseasesExplanationBox = createDiseasesExplanationBox(pAcgPatientData, pHumanPartList);
        final ScrollPane diseasesExplanationScrollBox = new ScrollPane();
        diseasesExplanationScrollBox.setFitToHeight(true);
        diseasesExplanationScrollBox.setFitToWidth(true);
        diseasesExplanationScrollBox.setHbarPolicy(ScrollBarPolicy.NEVER);
        diseasesExplanationScrollBox.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        diseasesExplanationScrollBox.setContent(diseasesExplanationBox);
        return diseasesExplanationScrollBox;
    }

    /**
     * Creates a box with the diseases
     *
     * @param pAcgPatientData ACG data
     * @param pHumanPartList organs and groups of organs
     * @return pane with avatar and diseases
     */
    public VBox createDiseasesExplanationBox(final AcgPatientData pAcgPatientData, final Map<Integer, HumanPartGraphic> pHumanPartList) {
        final VBox diseasesExplanationBox = new VBox();
        if (pAcgPatientData == null) {
            return diseasesExplanationBox;
        }
        diseasesExplanationBox.setSpacing(10.0d);
        final int year = pAcgPatientData.datenjahr;
        for (Map.Entry<Integer, HumanPartGraphic> entry : pHumanPartList.entrySet()) {
            final VBox organEntry = new VBox();
            organEntry.getStyleClass().add("acg_box");
            VBox.setMargin(organEntry, new Insets(0, 0, 10, 0));
            organEntry.setAlignment(Pos.TOP_LEFT);
            organEntry.setSpacing(10.0d);
            //organEntry.setStyle("-fx-background-color: yellow;");
            final HumanPartGraphic organ = entry.getValue();
            final List<IcdFarbeOrgan> icds = AbstractHumanPartGraphic.getOrganInfo(pAcgPatientData, organ);
            if (icds.isEmpty()) {
                continue;
            }
            OrganSeverity maxSeverity = AbstractHumanPartGraphic.getMaxSeverity(icds);
            Set<String> edcList = new TreeSet<>();
            TextFlow icdTexts = new TextFlow();
            Text icdLabel = new Text("ICD: ");
            icdTexts.getChildren().add(icdLabel);
            icdLabel.setStyle("-fx-font-weight: bold; -fx-fill: -green03;");
            for (final IcdFarbeOrgan icdFarbeOrgan : icds) {
                final String icdCode = icdFarbeOrgan.icd;
                CpxIcd icd = CpxIcdCatalog.instance().getByCode(icdCode, "de", year);
                if (icd == null || icd.id == 0) {
                    icd = CpxIcdCatalog.instance().getByCode(icdCode + ".-", "de", year);
                }
                if (icd == null || icd.id == 0) {
                    icd = CpxIcdCatalog.instance().getByCode(icdCode + "-", "de", year);
                }
                if (icdFarbeOrgan.edcText != null && !icdFarbeOrgan.edcText.isEmpty()) {
                    edcList.add(icdFarbeOrgan.edcText);
                }
                final String icdDesc = icd.id == 0 ? icdCode : icd.getDescription();
                final Text icdText = new Text(icd.id == 0 ? icdCode : icd.getCode());
                final Tooltip t = new Tooltip(
                        "ICD " + icdText.getText() + ": " + icdDesc
                        + (icd.getIcdInclusion() != null ? "\n\nInkl.: " + icd.getIcdInclusion() : "")
                        + (icd.getIcdExclusion() != null ? "\n\nExkl.: " + icd.getIcdExclusion() : "")
                );
                Tooltip.install(icdText, t);
                //right, here comes a shitty solution to apply same tooltip stylesheet layout as in CPX Client!
                t.setStyle("    -fx-background: rgba(30,30,30);\n"
                        + "    -fx-text-fill: black;\n"
                        + "    -fx-font-size: 12px;\n"
                        + "    -fx-font-weight: normal;\n"
                        + "    -fx-background-color: white;\n"
                        + "    -fx-background-radius: 6px;\n"
                        + "    -fx-background-insets: 0;\n"
                        + "    -fx-padding: 0.667em 0.75em 0.667em 0.75em;\n"
                        + "    -fx-background-radius: 0 0 0 0;\n"
                        + "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.5) , 10, 0.0 , 0 , 3 );"
                );
                if (icdTexts.getChildren().size() > 1) {
                    icdTexts.getChildren().add(new Text(", "));
                }
                icdText.setStyle("-fx-font-weight: bold; -fx-fill: -green03;");
                icdTexts.getChildren().add(icdText);
            }
            TextFlow diseaseTexts = new TextFlow();
            diseaseTexts.setMaxWidth(Control.USE_PREF_SIZE);
            diseaseTexts.setMaxHeight(Control.USE_PREF_SIZE);
            Text krankheitenLabel = new Text("Krankheiten: ");
            diseaseTexts.getChildren().add(krankheitenLabel);
            for (String edcText : edcList) {
                final Text diseaseText = new Text(edcText);
                if (diseaseTexts.getChildren().size() > 1) {
                    diseaseTexts.getChildren().add(new Text(", "));
                }
                diseaseText.setStyle("-fx-font-weight: bold; -fx-font-style: italic;");
                diseaseTexts.getChildren().add(diseaseText);
            }
            GridPane organTitle = new GridPane();
            Text organText = new Text(organ.getNameGerman());
            organText.getStyleClass().add("acg_organ_title");
            Text severityText = new Text(maxSeverity.getNameGerman());
            Circle circle = new Circle(10d);
            circle.setStrokeWidth(1);
            circle.setStroke(Color.BLACK);
            circle.setFill(maxSeverity.getColor());
            HBox severityBox = new HBox(severityText, circle);
            severityBox.setAlignment(Pos.TOP_RIGHT);
            severityBox.setSpacing(5.0d);
            severityBox.setPrefWidth(500d);

            organTitle.addRow(0, organText, severityBox);
            organTitle.getColumnConstraints().add(0, new ColumnConstraints());
            organTitle.getColumnConstraints().add(1, new ColumnConstraints());
            organTitle.getColumnConstraints().get(1).setFillWidth(true);
            organTitle.getColumnConstraints().get(1).setHalignment(HPos.RIGHT);
            organTitle.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);
            GridPane.setHalignment(organText, HPos.LEFT);
            GridPane.setHalignment(severityBox, HPos.RIGHT);

            organEntry.getChildren().add(organTitle);
            organEntry.getChildren().add(diseaseTexts);
            organEntry.getChildren().add(icdTexts);
            diseasesExplanationBox.getChildren().add(organEntry);

            final Map<HumanPartGraphic, Effect> oldEffects = new HashMap<>();

            organEntry.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    LOG.log(Level.FINE, "OnMouseEntered for Organ: " + organ.getName());
                    for (Map.Entry<Integer, HumanPartGraphic> entry : pHumanPartList.entrySet()) {
                        final HumanPartGraphic tmp = entry.getValue();
                        if (tmp.getNumber() == organ.getNumber()) {
                            continue;
                        }
                        final Effect old = tmp.showNoneColored();
                        oldEffects.put(tmp, old);
                    }
                }
            });
            organEntry.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    LOG.log(Level.FINE, "OnMouseExited for Organ: {0}", organ.getName());
                    for (Map.Entry<Integer, HumanPartGraphic> entry : pHumanPartList.entrySet()) {
                        final HumanPartGraphic tmp = entry.getValue();
                        if (tmp.getNumber() == organ.getNumber()) {
                            continue;
                        }
                        Effect old = oldEffects.get(tmp);
                        tmp.setEffect(old);
                    }
                }
            });
        }

        if (diseasesExplanationBox.getChildren().isEmpty()) {
            Label noDiseasesLabel = new Label("Keine Diagnosen für dieses Jahr gefunden");
            noDiseasesLabel.setWrapText(true);
            noDiseasesLabel.setTextAlignment(TextAlignment.CENTER);
            diseasesExplanationBox.getChildren().add(noDiseasesLabel);
        }

        return diseasesExplanationBox;
    }

    /**
     * creates an avatar and gives you a list of organs or organs groups so that
     * you can add additional events for each graphic or whatever you want
     *
     * @param pAcgPatientData ACG Data
     * @return result with avatar and list of organs
     */
    public HealthBoxResult createHealthBox(final AcgPatientData pAcgPatientData) {
        StackPane healthPane = new StackPane();
        HBox.setHgrow(healthPane, Priority.ALWAYS);
        VBox healthBox = new VBox();
        VBox.setVgrow(healthPane, Priority.ALWAYS);
        healthBox.setAlignment(Pos.CENTER);
        if (pAcgPatientData != null) {
            healthBox.getChildren().add(healthPane);
        }
        healthPane.autosize();
        final Map<Integer, HumanPartGraphic> humanPartList;

        if (pAcgPatientData != null) {

            humanPartList = getHealthVisualization(healthPane, pAcgPatientData, gender);

            healthPane.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    render(healthPane, pAcgPatientData, humanPartList);
                }
            });

            healthPane.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    render(healthPane, pAcgPatientData, humanPartList);
                }
            });
        } else {
            humanPartList = new HashMap<>();
        }
        return new HealthBoxResult(healthBox, humanPartList);
    }

    private void render(final StackPane healthPane, final AcgPatientData pAcgPatientData, final Map<Integer, HumanPartGraphic> humanPartList) {
        Map<Integer, HumanPartGraphic> parts = getHealthVisualization(healthPane, pAcgPatientData, gender);
        humanPartList.clear();
        humanPartList.putAll(parts);
    }

    /**
     * Draws a two column grid with the the morbidity summary and the avatar
     *
     * @param pAcgPatientData ACG data
     * @return grid pane
     */
    public GridPane createMorbidityGrid(final AcgPatientData pAcgPatientData) {
        final GridPane morbidityGrid = new GridPane();
        VBox.setVgrow(morbidityGrid, Priority.ALWAYS);
        morbidityGrid.setStyle("-fx-padding: 15 15 15 15;");

        morbidityGrid.getColumnConstraints().add(0, new ColumnConstraints());
        morbidityGrid.getColumnConstraints().get(0).setPercentWidth(100d);
        morbidityGrid.getRowConstraints().add(0, new RowConstraints());
        morbidityGrid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);

        final VBox morbiditySummaryBox = createMorbiditySummaryBox(pAcgPatientData);

        morbidityGrid.addRow(0, morbiditySummaryBox);

        return morbidityGrid;
    }

    public class DiseasesGridResult {

        public final HealthBoxResult healthBoxResult;
        public final GridPane diseasesGrid;

        public DiseasesGridResult(final HealthBoxResult pHealthBoxResult, final GridPane pDiseasesGrid) {
            healthBoxResult = pHealthBoxResult;
            diseasesGrid = pDiseasesGrid;
        }
    }

    /**
     * Draws a two column grid with the avatar and its diseases
     *
     * @param pAcgPatientData ACG data
     * @param pType compact or fullscreen
     * @return grid pane
     */
    public DiseasesGridResult createDiseasesGrid(final AcgPatientData pAcgPatientData, final AcgVisualizationType pType) {
        final GridPane diseasesGrid = new GridPane();
        VBox.setVgrow(diseasesGrid, Priority.ALWAYS);
        diseasesGrid.setStyle("-fx-padding: 15 15 15 15;");

        diseasesGrid.getColumnConstraints().add(0, new ColumnConstraints());
        diseasesGrid.getColumnConstraints().get(0).setPercentWidth(100d);
        diseasesGrid.getRowConstraints().add(0, new RowConstraints());
        diseasesGrid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);

        final HealthBoxResult healthBoxResult = createHealthBox(pAcgPatientData);
        final Node diseasesExplanationScrollPane = createDiseasesExplanationScrollPane(pAcgPatientData, healthBoxResult.humanPartList);

        diseasesGrid.addRow(0, diseasesExplanationScrollPane);
        return new DiseasesGridResult(healthBoxResult, diseasesGrid);
    }

    public class DiagramsResult {

        public final GridPane diagramsGrid;
        public final Set<BarChart<String, Number>> charts;

        public DiagramsResult(final GridPane pDiagramsGrid, final Set<BarChart<String, Number>> pCharts) {
            diagramsGrid = pDiagramsGrid;
            charts = new HashSet<>(pCharts);
        }
    }

    /**
     * Draws a three column grid with a diagram in each column
     *
     * @param pAcgPatientDataMap ACG data
     * @return grid pane
     */
    public DiagramsResult createDiagramsGrid(final Map<Integer, AcgPatientData> pAcgPatientDataMap) {
        final GridPane diagramsGrid = new GridPane();
        VBox.setVgrow(diagramsGrid, Priority.ALWAYS);
        diagramsGrid.setStyle("-fx-padding: 10 -10 10 -10;");

        diagramsGrid.getColumnConstraints().add(0, new ColumnConstraints());
        diagramsGrid.getColumnConstraints().add(1, new ColumnConstraints());
        diagramsGrid.getColumnConstraints().add(2, new ColumnConstraints());

        diagramsGrid.getColumnConstraints().get(0).setPercentWidth(33.33d);
        diagramsGrid.getColumnConstraints().get(1).setPercentWidth(33.33d);
        diagramsGrid.getColumnConstraints().get(2).setPercentWidth(33.33d);

        diagramsGrid.getRowConstraints().add(0, new RowConstraints());
        diagramsGrid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);
        diagramsGrid.getRowConstraints().get(0).setFillHeight(true);

        final BarChart<String, Number> referenceRescaledWeightChart = createBarChart(1, "Fallgewicht", "Fallgewicht", pAcgPatientDataMap);
        final BarChart<String, Number> chronicConditionCountChart = createBarChart(2, "Chronische Erkrankungen", "Anzahl chronischer Erkrankungen", pAcgPatientDataMap);
        final BarChart<String, Number> resourceUtilizationBandsChart = createBarChart(3, "Ressourcenverbrauchsgruppe", "Ressourcenverbrauch", pAcgPatientDataMap);

        final Set<BarChart<String, Number>> charts = new HashSet<>();
        charts.add(referenceRescaledWeightChart);
        charts.add(chronicConditionCountChart);
        charts.add(resourceUtilizationBandsChart);

        diagramsGrid.addRow(0, referenceRescaledWeightChart, chronicConditionCountChart, resourceUtilizationBandsChart);
        return new DiagramsResult(diagramsGrid, charts);
    }

    /**
     * Prints an information that there is no data available for this datenjahr
     *
     * @return label with the information that there is no data in this
     * datenjahr
     */
    public VBox createNoDataBox() {
        final String noData = "Keine Daten verfügbar für dieses Jahr";
        final Text noDataLabel = new Text(noData);
        noDataLabel.setTextAlignment(TextAlignment.CENTER);
        final VBox box = new VBox();
        box.getChildren().add(noDataLabel);
        box.setAlignment(Pos.CENTER);
        return box;
    }

//    private Pane createNewPane() {
//        
//    }
    /**
     * Gives you a visualization of ACG data.Fullscreen uses tabs.
     *
     * @param pType compact or fullscreen
     * @param pCase hospital case to render
     * @return content in a box
     */
    public VBox getContent(final AcgVisualizationType pType, final TCase pCase) {
        if (pType == null) {
            throw new IllegalArgumentException("No type for visualization passed to method");
        }
        final Map<Integer, AcgPatientData> lAcgPatientDataMap = getAcgPatientData();

        final StackPane stackPaneMorbidity = new StackPane();
        stackPaneMorbidity.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(stackPaneMorbidity, Priority.ALWAYS);
        final StackPane stackPaneDiseases = new StackPane();
        VBox.setVgrow(stackPaneDiseases, Priority.ALWAYS);
        stackPaneDiseases.setAlignment(Pos.TOP_LEFT);
        final StackPane stackPaneDiagrams = new StackPane();
        stackPaneDiagrams.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(stackPaneDiagrams, Priority.ALWAYS);
        final StackPane stackPaneHealthBox = new StackPane();
        VBox.setVgrow(stackPaneHealthBox, Priority.ALWAYS);
        Set<BarChart<String, Number>> charts = new HashSet<>();
        //stackPaneHealthBox.setStyle("-fx-background-color: yellow");

        final VBox vbox = new VBox();
        vbox.getStyleClass().add("health_status_visualization_box");
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(10.0d);
        
        if (pCase != null) {
//            if (lAcgPatientDataMap == null) {
//                lAcgPatientDataMap = new LinkedHashMap<>();
//            }
            final TCaseDetails csdLocal = pCase.getCurrentLocal();
            final TPatient pat = pCase.getPatient();
//            final List<TCaseIcd> icds = csdLocal == null ? new ArrayList<>() : csdLocal.getAllIcds();
            Set<Integer> lineNumbers = new TreeSet<>();
            lineNumbers.add(0);
            final String jahr = csdLocal.getCsdAdmissionYear() + "";
            final String period = Lang.toLocalDate(csdLocal.getCsdAdmissionDate()).get(IsoFields.QUARTER_OF_YEAR) + ""; //"1";
            final String datenjahr = "-1"; //"Fall " + pCase.getCsCaseNumber();
            final String bezeichnung = "Fall " + pCase.getCsCaseNumber();
//            final String versichertennummer = "4711";
            final String geburtsdatum = Lang.toIsoDate(pat.getPatDateOfBirth());
            final String alter = "0";
            final String geschlecht = pat.getPatGenderEn().name(); //use m, f/w, u or i
            //icdInfos
            final String pChronicConditionCount = "0";
            final String pChronicConditionCountGroup = "0";
            final String pChronicCondition = "NP";
            final String pReferenceRescaledWeight = "0";
            final String pReferenceRescaledWeightGroup = "0";
            final String pResourceUtilizationBand = "0";
            final String pResourceUtilizationBandGroup = "0";
            final String pFrailtyFlag = "nein";

            //for(TCaseIcd icd: icds) {
            List<IcdFarbeOrgan> icdInfos = Session.instance().getEjbConnector().connectAcgServiceBean().get().getIcdData(pCase.id);
            //}

            final AcgPatientData acgPatientData = new AcgPatientData(lineNumbers, "", jahr, period, datenjahr, bezeichnung, geburtsdatum, alter, geschlecht, icdInfos,
                    pChronicConditionCount, pChronicConditionCountGroup, pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition,
                    pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition,
                    pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition, pChronicCondition,
                    pChronicCondition, pChronicCondition, pChronicCondition, pReferenceRescaledWeight, pReferenceRescaledWeightGroup,
                    pResourceUtilizationBand, pResourceUtilizationBandGroup, pFrailtyFlag
            );
            lAcgPatientDataMap.put(-1, acgPatientData);
        }

        if (lAcgPatientDataMap == null || lAcgPatientDataMap.isEmpty()) {
            final Text noDataLabel = new Text("Keine Daten verfügbar für diesen Patienten");
            noDataLabel.setTextAlignment(TextAlignment.CENTER);
            vbox.getChildren().add(noDataLabel);
            return vbox;
        }

        final TreeMap<Integer, AcgPatientData> map = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        map.putAll(lAcgPatientDataMap);

        for (final Map.Entry<Integer, AcgPatientData> acgEntry : map.entrySet()) {
            if (minDatenjahr == null || acgEntry.getValue().datenjahr < minDatenjahr) {
                minDatenjahr = acgEntry.getValue().datenjahr;
            }
            if (maxDatenjahr == null || acgEntry.getValue().datenjahr > maxDatenjahr) {
                maxDatenjahr = acgEntry.getValue().datenjahr;
            }
        }
        
        final Map<Integer, Integer> datenjahre = new LinkedHashMap<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int dj = maxDatenjahr; dj >= minDatenjahr; dj--) {
            datenjahre.put(dj, year);
            year--;
        }

        final GridPane grid = new GridPane();
        grid.getColumnConstraints().add(0, new ColumnConstraints());
        grid.getColumnConstraints().add(1, new ColumnConstraints());
        grid.getColumnConstraints().get(0).setPercentWidth(25d);
//        grid.getColumnConstraints().get(0).setHgrow(Priority.NEVER);
//        grid.getColumnConstraints().get(1).setHgrow(Priority.NEVER);
        grid.getColumnConstraints().get(1).setPercentWidth(75d);
        grid.getRowConstraints().add(0, new RowConstraints());
        grid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);
        VBox.setVgrow(grid, Priority.ALWAYS);
//        AnchorPane.setTopAnchor(grid, 0.0d);
//        AnchorPane.setRightAnchor(grid, 0.0d);
//        AnchorPane.setBottomAnchor(grid, 0.0d);
//        AnchorPane.setLeftAnchor(grid, 0.0d);

        final Tab morbidityTab = new Tab("Morbiditäts-Summary");
        morbidityTab.setClosable(false);
        morbidityTab.setContent(stackPaneMorbidity);
        final Tab diseasesTab = new Tab("Erkrankungsgruppen und Diagnosen");
        diseasesTab.setClosable(false);
        diseasesTab.setContent(stackPaneDiseases);
        final Tab diagramsTab = new Tab("Diagramme");
        diagramsTab.setClosable(false);
        diagramsTab.setContent(stackPaneDiagrams);
        final TabPane tabPane = new TabPane(morbidityTab, diseasesTab, diagramsTab);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        grid.addRow(0, stackPaneHealthBox, tabPane);

        for (final Map.Entry<Integer, AcgPatientData> acgEntry : map.entrySet()) {

            final int datenjahr = acgEntry.getKey();
            final AcgPatientData acgPatientData = acgEntry.getValue();
            final String datenjahrId = "datenjahr" + datenjahr;
            if (AcgVisualizationType.COMPACT.equals(pType)) {
                //Compact display for WmPatientSection
                if (acgPatientData == null) {
                    LOG.log(Level.WARNING, "No health status visualization possible for datenjahr {0} because there is no data available", datenjahr);
                    VBox noDataBox = createNoDataBox();
                    noDataBox.setId(datenjahrId);
                    noDataBox.getProperties().put("datenjahr", datenjahr);
                    stackPaneMorbidity.getChildren().add(noDataBox);
                    continue;
                }
                final DiseasesGridResult result = createDiseasesGrid(acgPatientData, AcgVisualizationType.COMPACT);

                final GridPane diseasesGrid = new GridPane();
                diseasesGrid.getColumnConstraints().add(0, new ColumnConstraints());
                diseasesGrid.getColumnConstraints().add(1, new ColumnConstraints());
                diseasesGrid.getColumnConstraints().get(0).setPercentWidth(40d);
                diseasesGrid.getColumnConstraints().get(1).setPercentWidth(60d);
//                diseasesGrid.getColumnConstraints().get(0).setHgrow(Priority.NEVER);
                diseasesGrid.getRowConstraints().add(0, new RowConstraints());
                diseasesGrid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);

                diseasesGrid.addRow(0, result.healthBoxResult.healthBox, result.diseasesGrid);

                diseasesGrid.setId(datenjahrId);
                diseasesGrid.getProperties().put("datenjahr", datenjahr);
                VBox.setVgrow(diseasesGrid, Priority.ALWAYS);
                HBox.setHgrow(diseasesGrid, Priority.ALWAYS);
                stackPaneDiseases.getChildren().add(diseasesGrid);
                continue;
            }

            DiseasesGridResult result;
            if (acgPatientData == null) {
                //Morbidity Grid (but no data)
                LOG.log(Level.WARNING, "No health status visualization possible for datenjahr {0} because there is no data available", datenjahr);
                final VBox noDataBoxMorbidity = createNoDataBox();
                noDataBoxMorbidity.getProperties().put("datenjahr", datenjahr);
                final VBox noDataBoxDiseases = createNoDataBox();
                noDataBoxDiseases.getProperties().put("datenjahr", datenjahr);
                stackPaneMorbidity.getChildren().add(noDataBoxMorbidity);
                stackPaneDiseases.getChildren().add(noDataBoxDiseases);
            } else {
                //Morbidity Grid (with data)
                final GridPane morbidityGrid = createMorbidityGrid(acgPatientData);
                morbidityGrid.getProperties().put("datenjahr", datenjahr);
                morbidityGrid.setMaxWidth(MAX_WIDTH_MORBIDITY);
                stackPaneMorbidity.getChildren().add(morbidityGrid);

                //Diseases Grid
                result = createDiseasesGrid(acgPatientData, pType);
                //healthBoxes.put(datenjahr, result.healthBoxResult);
                result.healthBoxResult.healthBox.getProperties().put("datenjahr", datenjahr);
                stackPaneHealthBox.getChildren().add(result.healthBoxResult.healthBox);
                //final GridPane diseasesGrid = result.diseasesGrid;
                result.diseasesGrid.getProperties().put("datenjahr", datenjahr);
                result.diseasesGrid.setMaxWidth(MAX_WIDTH_DISEASES);
                stackPaneDiseases.getChildren().add(result.diseasesGrid);
                //diseasesTab.setContent(diseasesGrid);
            }

            if (datenjahr == minDatenjahr) {
                //Diagrams Grid
                final DiagramsResult resultDiagrams = createDiagramsGrid(map);
                //final GridPane diagramsGrid = createDiagramsGrid(lAcgPatientDataMap);
                resultDiagrams.diagramsGrid.getProperties().put("datenjahr", datenjahr);
                stackPaneDiagrams.getChildren().add(resultDiagrams.diagramsGrid);
                charts.addAll(resultDiagrams.charts);
                //diagramsTab.setContent(diagramsGrid);
            }
        }

//        datenjahrLabel = new TextFlow();
//
//        datenjahrLabel.setPrefWidth(70d);
//        datenjahrLabel.setStyle("-fx-font-weight: bold;");
        final Slider slDatenjahr = new Slider();

        slDatenjahr.setOrientation(Orientation.HORIZONTAL);
        slDatenjahr.setMax(maxDatenjahr);
        slDatenjahr.setMin(minDatenjahr);
        slDatenjahr.setShowTickLabels(true);
        slDatenjahr.setShowTickMarks(true);
        slDatenjahr.setMajorTickUnit(1);
        slDatenjahr.setMinorTickCount(1);
        slDatenjahr.setBlockIncrement(1);
        slDatenjahr.setPrefWidth(220d);
        slDatenjahr.setTooltip(sliderToolTip);
        slDatenjahr.setLabelFormatter(new StringConverter<Double>() {
            @Override

            public String toString(Double t) {
//                LOG.log(Level.INFO, "t: " + t);
//                LOG.log(Level.INFO, "t.intValue: " + t.intValue());
//                Integer jahr = datenjahre.get(t.intValue());
//                LOG.log(Level.INFO, "jahr: " + jahr);
                if (t != null) {
                    return String.valueOf(t.intValue());
                }
                LOG.log(Level.WARNING, "illegal double value found in slider: {0}", t);
                return "";
            }

            @Override
            public Double fromString(String string) {
                Iterator<Map.Entry<Integer, AcgPatientData>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, AcgPatientData> entry = it.next();
                    final AcgPatientData acgPatientData = entry.getValue();
                    if (acgPatientData.getSliderLabel().equals(string)) {
                        return (double) acgPatientData.datenjahr;
                    }
                }
                LOG.log(Level.WARNING, "illegal string value found in slider: {0}", string);
                return null;
            }
        });

        slDatenjahr.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                final int datenjahr = newValue.intValue();
//                Integer jahr = datenjahre.get(datenjahr);
//                updateSliderText(map, datenjahr);

                //Display the right panes for the selected datenjahr 
                showDatenjahrPane(stackPaneMorbidity, datenjahr);
                showDatenjahrPane(stackPaneDiseases, datenjahr);
                showDatenjahrPane(stackPaneHealthBox, datenjahr);

                //Highlight the selected bars of the selected datenjahr
                for (final BarChart<String, Number> c : charts) {
                    for (Series<String, Number> s : c.getData()) {
                        //Series s = (Series) o;
                        final Iterator<Data<String, Number>> it = s.getData().iterator();
                        while (it.hasNext()) {
                            final Data<String, Number> d = it.next();
                            final Node n = d.getNode();
                            final int dj = (int) n.getProperties().get("datenjahr");
                            n.getStyleClass().remove("acg_highlight_bar");
                            if (dj == datenjahr) {
                                n.getStyleClass().add("acg_highlight_bar");
                            }
                        }
                    }
                }
            }
        });

        slDatenjahr.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number oldval, Number newVal) {
                slDatenjahr.setValue(Math.round(newVal.doubleValue()));
                setTooltipToSlider(slDatenjahr, map);
            }
        });

        
        sliderToolTip.setShowDelay(Duration.ZERO);
        sliderToolTip.setShowDuration(new Duration(6000d));
        
//        updateSliderText(map, MIN_DATENJAHR);
        slDatenjahr.setValue(minDatenjahr);
        slDatenjahr.adjustValue(maxDatenjahr);
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.TOP_CENTER);
        headerBox.setSpacing(5.0d);
        headerBox.getChildren().add(new Text("ACG-Daten "));
        headerBox.getChildren().add(slDatenjahr);
        final AcgPatientData acgPatientData = map.get(maxDatenjahr);

        String sliderDescription = "";
        if(acgPatientData != null) {
            sliderDescription = String.valueOf(acgPatientData.jahr);
        }

        headerBox.getChildren().add(new Text(sliderDescription));
//        headerBox.getChildren().add(datenjahrLabel);
        vbox.getChildren().add(headerBox);
        if (AcgVisualizationType.COMPACT.equals(pType)) {
            vbox.getChildren().add(stackPaneDiseases);
        } else {
            //AWi-20200710:add a scrollpane to avoid resizing issues making the ui unusable
            ScrollPane sp = new ScrollPane(grid);
            sp.setFitToWidth(true);
            sp.setFitToHeight(true);
            VBox.setVgrow(sp, Priority.ALWAYS);
            vbox.getChildren().add(sp);
        }
        return vbox;
    }

//    private void updateSliderText(Map<Integer, AcgPatientData> map, final int datenjahr) {
//        datenjahrLabel.getChildren().clear();
//        TextFlow datenjahrText = new TextFlow();
//        datenjahrText.setTextAlignment(TextAlignment.RIGHT);
//        datenjahrText.setPrefWidth(50d);
//        final AcgPatientData acgPatientData = map.get(datenjahr);
//        final String label;
//        if (acgPatientData != null) {
//            label = acgPatientData.getSliderLabel();
//        } else {
//            label = "";
//        }
//        final Text text = new Text(label);
//        datenjahrLabel.getChildren().addAll(datenjahrText, text);
//    }
    public void showDatenjahrPane(final StackPane pStackPane, final int pDatenjahr) {
        for (Node box : pStackPane.getChildren()) {
            if (((int) box.getProperties().get("datenjahr")) == pDatenjahr) {
                box.setVisible(true);
            } else {
                box.setVisible(false);
            }
        }
    }

    /**
     *
     * @param pTyp 1=Reference Rescaled Weight, 2=Chronic Condition Count,
     * 3=Ressourcenverbrauch
     * @param pTitle
     * @param pAxisTitle
     * @param pAcgPatientData
     * @return chart
     */
    @SuppressWarnings("unchecked")
    private BarChart<String, Number> createBarChart(final int pTyp, final String pTitle, final String pAxisTitle, final Map<Integer, AcgPatientData> pAcgPatientDataMap) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        for (int i = 1; i <= 1; i++) {
            bc.setTitle(pTitle);
            xAxis.setLabel("Datenjahr / Alter");
            yAxis.setLabel(pAxisTitle);
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(0.0d);
            yAxis.setTickMarkVisible(false);
            xAxis.tickLabelFontProperty().set(Font.font(14));
            yAxis.tickLabelFontProperty().set(Font.font(14));
            bc.setLegendSide(Side.BOTTOM);
            if (pTyp == 2) {
                //2=Chronic condition count
                yAxis.setTickUnit(0.5d);
                yAxis.setUpperBound(50.0d);
            } else {
                yAxis.setTickUnit(0.5d);
                yAxis.setUpperBound(5.0d);
            }

            final Map<Integer, String> values = new HashMap<>();

            for (int dj = maxDatenjahr; dj >= minDatenjahr; dj--) {
                AcgPatientData acgPatientData = pAcgPatientDataMap.get(dj);
                String text = acgPatientData == null ? "" : acgPatientData.datenjahr + "/" + acgPatientData.alter;
                values.put(dj, text);
            }

            if (i == 1) {
                final Series<String, Number> series1 = new XYChart.Series<>();
                final List<Data<String, Number>> series1Data = new ArrayList<>();
                series1.setName("Mittelwert Altersgruppe");
                //double prevValue = 0;
                for (int dj = maxDatenjahr; dj >= minDatenjahr; dj--) {
                    final int datenjahr = dj;
                    AcgPatientData acgPatientData = pAcgPatientDataMap.get(dj);
                    String text = values.get(dj);
                    final double value;
                    if (acgPatientData == null) {
                        value = 0.0d;
                    } else {
                        switch (pTyp) {
                            case 1:
                                value = acgPatientData.relativgewicht_altersgruppe;
                                break;
                            case 2:
                                value = acgPatientData.chronische_erkrankungen_altersgruppe;
                                break;
                            case 3:
                                value = acgPatientData.ressourcenverbrauchsgruppe_altersgruppe;
                                break;
                            default:
                                value = 0.0d;
                                break;
                        }
                    }
                    Data<String, Number> data = new XYChart.Data<>(text, value);
                    series1Data.add(0, data);
                    data.nodeProperty().addListener(new ChangeListener<Node>() {
                        @Override
                        public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                            data.getNode().getProperties().put("datenjahr", datenjahr);
                            data.getNode().getStyleClass().add("bar-" + datenjahr);
                            data.getNode().setId("mittelwert-bar-" + datenjahr);
                        }
                    });
                }
                series1.getData().addAll(series1Data);
                bc.getData().addAll(series1);
            }

            final XYChart.Series<String, Number> series2 = new XYChart.Series<>();
            final List<Data<String, Number>> series2Data = new ArrayList<>();
            series2.setName("Versicherter");
            for (int dj = minDatenjahr; dj <= maxDatenjahr; dj++) {
                final int datenjahr = dj;
                AcgPatientData acgPatientData = pAcgPatientDataMap.get(dj);
                String text = values.get(dj);
                final double value;
                if (acgPatientData == null) {
                    value = 0.0d;
                } else {
                    switch (pTyp) {
                        case 1:
                            value = acgPatientData.relativgewicht_patient;
                            break;
                        case 2:
                            value = acgPatientData.chronische_erkrankungen_patient;
                            break;
                        case 3:
                            value = acgPatientData.ressourcenverbrauchsgruppe_patient;
                            break;
                        default:
                            value = 0.0d;
                            break;
                    }
                }
                Data<String, Number> data = new XYChart.Data<>(text, value);
                series2Data.add(0, data);
                data.nodeProperty().addListener(new ChangeListener<Node>() {
                    @Override
                    public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                        data.getNode().getProperties().put("datenjahr", datenjahr);
                        data.getNode().getStyleClass().add("bar-" + datenjahr);
                        data.getNode().setId("versicherter-bar-" + datenjahr);
                    }
                });
            }
            series2.getData().addAll(series2Data);
            bc.getData().addAll(series2);

            bc.setStyle("-fx-font-size: 12px; CHART_COLOR_1: #EFB364; CHART_COLOR_2: #46A877; ");
        }
        return bc;
    }

    /**
     * Will be replaced in future (just for demonstration!) Source of data can
     * be either an CSV file with ACG data or it is collected from the database
     * (ICD-10 catalog and CCL grouper information)
     *
     * @return list of organ information and their diseases and severities
     */
    public synchronized Map<Integer, AcgPatientData> getAcgPatientData() {
        if (this.acgPatientDataMap != null) {
            return this.acgPatientDataMap;
        }
        this.acgPatientDataMap = getAcgPatientData(patNumber);
        return this.acgPatientDataMap;
    }

    /**
     * Returns ACG data for a specific year
     *
     * @param pDatenjahr year
     * @return ACG data
     */
    public synchronized AcgPatientData getAcgPatientData(final Integer pDatenjahr) {
        if (pDatenjahr == null) {
            LOG.log(Level.WARNING, "pDatenjahr is null!");
            return null;
        }
        Map<Integer, AcgPatientData> data = getAcgPatientData();
        if (data == null) {
            LOG.log(Level.WARNING, "No ACG data found!");
            return null;
        }
        AcgPatientData acgPatientData = data.get(pDatenjahr);
        if (acgPatientData == null) {
            LOG.log(Level.WARNING, "No ACG data found for Datenjahr {0}!", pDatenjahr);
        }
        return acgPatientData;
    }

    /**
     * Returns ACG data for a specific patient
     *
     * @param pPatient patient
     * @return map with ACG data for each year
     */
    public static Map<Integer, AcgPatientData> getAcgPatientData(final TPatient pPatient) {
        return getAcgPatientData(pPatient == null ? "" : pPatient.getPatNumber());
    }

    /**
     * Returns ACG data for a specific patient number
     *
     * @param pPatNumber patient number
     * @return map with ACG data for each year
     */
    public static Map<Integer, AcgPatientData> getAcgPatientData(final String pPatNumber) {
        final String patNumber = pPatNumber == null ? "" : pPatNumber.trim();
        final Map<Integer, AcgPatientData> acgData = new HashMap<>();
        if (patNumber.isEmpty()) {
            LOG.log(Level.WARNING, "Passed patient number is null or empty, cannot visualize health status!");
        } else {
//            try {
            acgData.putAll(ACG_SERVICE_BEAN.get().getAcgData(patNumber));
            if (acgData.isEmpty()) {
                LOG.log(Level.WARNING, "Did not find any ACG data for patient number ''{0}''!", patNumber);
            }
//            } catch (IOException | SQLException ex) {
//                final String message = "Was not able to get ACG data for patient number '" + patNumber + "'!";
//                LOG.log(Level.SEVERE, message, ex);
//                MainApp.showErrorMessageDialog(ex, message);
//            }
        }

        if (!acgData.isEmpty()) {
            StringBuilder sb = new StringBuilder("Found ACG data for patient number '" + patNumber + "' for this years:");
            for (Map.Entry<Integer, AcgPatientData> entry : acgData.entrySet()) {
                Integer year = entry.getKey();
//                AcgPatientData data = entry.getValue();
                sb.append(" ").append(year);
            }
            LOG.log(Level.INFO, sb.toString());
        }
        return acgData;
    }

    /**
     * Draws the avatar on the pane that you pass to this method for the most
     * recent year
     *
     * @param pPane pane where the avatar will be drawn
     * @return list of organs or organ groups that were created (use them to add
     * additional events or whatever)
     */
    public Map<Integer, HumanPartGraphic> getHealthVisualization(final StackPane pPane) {
        return getHealthVisualization(pPane, maxDatenjahr);
    }

    /**
     * Draws the avatar on the pane that you pass to this method for a specific
     * year
     *
     * @param pPane pane where the avatar will be drawn
     * @param pDatenjahr year
     * @return list of organs or organ groups that were created (use them to add
     * additional events or whatever)
     */
    public Map<Integer, HumanPartGraphic> getHealthVisualization(final StackPane pPane, final Integer pDatenjahr) {
        AcgPatientData acgPatientData = getAcgPatientData(pDatenjahr);
        return getHealthVisualization(pPane, acgPatientData, gender);
    }

    /**
     * Draws the avatar on the pane that you pass to this method
     *
     * @param pPane pane where the avatar will be drawn
     * @param pAcgPatientData ACG data
     * @param pGender use this gender for rendering if there's no ACG data for
     * patient
     * @return list of organs or organ groups that were created (use them to add
     * additional events or whatever)
     */
    public static Map<Integer, HumanPartGraphic> getHealthVisualization(final StackPane pPane, final AcgPatientData pAcgPatientData, final String pGender) {
        Map<Integer, HumanPartGraphic> humanParts = new HashMap<>();
        if (pAcgPatientData == null) {
            LOG.log(Level.FINE, "AcgPatientData is null!");
        }
        try {
            humanParts = getPatientHealthVisualization(pPane, pAcgPatientData, pGender);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Patient's health visualization failed", ex);
        }
        return humanParts;
    }

    /**
     * Gives you an visualization of this patient's health status
     *
     * @param pTargetPane stack pane where the graphics should be put on
     * @param pAcgPatientData some health information from the server about the
     * patient and organe
     * @return human body parts
     * @param pGender use this gender for rendering if there's no ACG data for
     * patient
     * @throws IOException Error loading images (svgs)
     */
    public static Map<Integer, HumanPartGraphic> getPatientHealthVisualization(final StackPane pTargetPane, final AcgPatientData pAcgPatientData, final String pGender) throws IOException {
        HealthStatusApp healthApp = new HealthStatusApp();
        Map<Integer, HumanPartGraphic> humanParts = healthApp.getHealthVisualization(pTargetPane, pAcgPatientData, pGender);
        return humanParts;
    }

    private Text icd(final AcgIndexType pValue) {
        return new Text(pValue != null && pValue.diagnosis ? "X" : "");
    }

    private Text rx(final AcgIndexType pValue) {
        return new Text(pValue != null && pValue.medicins ? "X" : "");
    }

    private Text trt(final AcgIndexType pValue) {
        return new Text(pValue != null && pValue.treatment ? "X" : "");
    }

    private void setTooltipToSlider(final Slider objSlider, final TreeMap<Integer, AcgPatientData> map) {
        if(objSlider != null) {
            final AcgPatientData acgPatientData = map.get((int)objSlider.getValue());
            if(acgPatientData != null) {
                objSlider.getTooltip().setText(acgPatientData.getSliderLabel());
            }
        }
    }
}

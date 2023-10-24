/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.gridpane;

import de.lb.cpx.client.app.cm.fx.simulation.tables.CaseDetailsScrollPane.RowPosition;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.client.core.model.fx.spinner.NumberSpinner;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.AgeEn;
import de.lb.cpx.model.enums.DischargeReason2En;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.getColumnIndex;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * Implements the Gridpane to show the version based case details in the case
 * details view
 *
 * @author wilde
 * @param <E> compare content
 */
public class CaseVersionGridPane<E extends ComparableContent<? extends AbstractEntity>> extends GridPane {

    private static final Logger LOG = Logger.getLogger(CaseVersionGridPane.class.getName());

    private final E versionContent;
    private NumberSpinner days2;
    private NumberSpinner days;

    /**
     * creates a new instance of the GridPabne to display the version based
     * content of the case details
     *
     * @param pVersion version to show
     * @param pRowConstraints row constraints to set in the gridpane
     */
    public CaseVersionGridPane(E pVersion, List<RowConstraints> pRowConstraints) {
        super();
        versionContent = pVersion;
        //setUp style and width
        getStyleClass().add("case-management-grid");
//        minWidthProperty().bind(pVersion.widthProperty());
//        prefWidthProperty().bind(pVersion.widthProperty());
//        maxWidthProperty().bind(pVersion.widthProperty());
//        cons = new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, HPos.CENTER, true);
//        getColumnConstraints().add(cons);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    setUpContent(pRowConstraints, pVersion);
                } catch (CpxIllegalArgumentException ex) {
                    Logger.getLogger(CaseVersionGridPane.class.getName()).log(Level.SEVERE, null, ex);
                }
                applyCss();
            }
        });
        pVersion.getListenerAdapter().addChangeListener(pVersion.contentProperty(), new ChangeListener<AbstractEntity>() {
            @Override
            public void changed(ObservableValue<? extends AbstractEntity> observable, AbstractEntity oldValue, AbstractEntity newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        getRowConstraints().clear();
                        getChildren().clear();
                        try {
                            setUpContent(pRowConstraints, versionContent);
                        } catch (CpxIllegalArgumentException ex) {
                            Logger.getLogger(CaseVersionGridPane.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
//        pVersion.contentProperty().addListener(new ChangeListener<AbstractEntity>() {
//            @Override
//            public void changed(ObservableValue<? extends AbstractEntity> observable, AbstractEntity oldValue, AbstractEntity newValue) {
//                getRowConstraints().clear();
//                getChildren().clear();
//                try {
//                    setUpContent(pRowConstraints, versionContent);
//                } catch (CpxIllegalArgumentException ex) {
//                    Logger.getLogger(CaseVersionGridPane.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
    }
//    public DoubleProperty colPrefWidthProperty(){
//        return cons.prefWidthProperty();
//    }

    private void saveAndGroup(E pVersion) {
       pVersion.saveCaseDetailsEntity(pVersion.getCaseDetails());
       performGroup(pVersion);
    }
    //create specfic row

    private Region createVersionColumn(int index, E pVersion) throws CpxIllegalArgumentException {
        TCaseDetails pVersionDetails = pVersion.getCaseDetails();
        if (pVersionDetails == null) {
            LOG.log(Level.WARNING, "case details version is null!");
        } else {
            RowPosition rowPos = RowPosition.get2Ordinal(index);
            if (rowPos == null) {
                LOG.log(Level.WARNING, "Unknown index: {0}", index);
                return new VBox();
            }
            switch (rowPos) {
                case AdmReason12:
                    VBox admReasonWrapper = new VBox();
                    admReasonWrapper.setPadding(new Insets(2));
                    ComboBox<AdmissionReasonEn> admReason = new ComboBox<>(FXCollections.observableArrayList(AdmissionReasonEn.values()));
//                admReason.setMaxWidth(Double.MAX_VALUE);
                    admReason.getSelectionModel().select(pVersionDetails.getCsdAdmReason12En());
                    admReason.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AdmissionReasonEn>() {
                        @Override
                        public void changed(ObservableValue<? extends AdmissionReasonEn> observable, AdmissionReasonEn oldValue, AdmissionReasonEn newValue) {
                            pVersionDetails.setCsdAdmReason12En(newValue);
                            saveAndGroup(pVersion);
                        }
                    });
                    admReasonWrapper.getChildren().addAll(admReason);
                    admReason.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    admReasonWrapper.getStyleClass().add("case-management-grid-even");
                    return admReasonWrapper;
                case AdmReason34:
                    VBox admReason2Wrapper = new VBox();
                    admReason2Wrapper.setPadding(new Insets(2));
                    ComboBox<AdmissionReason2En> admReason2 = new ComboBox<>(FXCollections.observableArrayList(AdmissionReason2En.values()));
                    admReason2.setMaxWidth(Double.MAX_VALUE);
                    admReason2.getSelectionModel().select(pVersionDetails.getCsdAdmReason34En());
                    admReason2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AdmissionReason2En>() {
                        @Override
                        public void changed(ObservableValue<? extends AdmissionReason2En> observable, AdmissionReason2En oldValue, AdmissionReason2En newValue) {
                            pVersionDetails.setCsdAdmReason34En(newValue);
                            saveAndGroup(pVersion);
                        }
                    });
                    admReason2Wrapper.getChildren().addAll(admReason2);
                    admReason2.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    admReason2Wrapper.getStyleClass().add("case-management-grid-odd");
                    return admReason2Wrapper;
                case AdmCause:
                    ComboBox<AdmissionCauseEn> admCause = new ComboBox<>(FXCollections.observableArrayList(AdmissionCauseEn.values()));
                    admCause.setMaxWidth(Double.MAX_VALUE);
                    admCause.getSelectionModel().select(pVersionDetails.getCsdAdmCauseEn());
                    admCause.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AdmissionCauseEn>() {
                        @Override
                        public void changed(ObservableValue<? extends AdmissionCauseEn> observable, AdmissionCauseEn oldValue, AdmissionCauseEn newValue) {
                            pVersionDetails.setCsdAdmCauseEn(newValue);
                            saveAndGroup(pVersion);
                        }
                    });
                    admCause.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    VBox admCauseWrapper = new VBox(admCause);
                    admCauseWrapper.setPadding(new Insets(2));
                    admCauseWrapper.getStyleClass().add("case-management-grid-even");
                    return admCauseWrapper;
                case AdmissionText:
                    HBox admDateTimeWrapper = new HBox();
                    admDateTimeWrapper.setPadding(new Insets(2));
                    admDateTimeWrapper.setSpacing(5);
                    Label admDate = new Label(Lang.toDate(pVersionDetails.getCsdAdmissionDate()));
                    Label admTime = new Label(Lang.toTime(pVersionDetails.getCsdAdmissionDate()));
                    admDateTimeWrapper.getChildren().addAll(admDate, admTime);
                    admDateTimeWrapper.getStyleClass().add("case-management-grid-odd");
                    return admDateTimeWrapper;
                case PatientWeight:
                    HBox admWeightWrapper = new HBox();
                    admWeightWrapper.setPadding(new Insets(2));
                    admWeightWrapper.setSpacing(5.0);
                    Label measure = new Label(Lang.getPatientWeightMeasureGram());
                    measure.setMinWidth(USE_PREF_SIZE);
//                LocalDate age = PatientHelper.getCurrentAge(pVersion.getPatient().getPatDateOfBirth());
//                if(age.getYear()>0 || age.getDayOfYear()>=28){
                    if ((pVersionDetails.getCsdAgeYears() != null && pVersionDetails.getCsdAgeYears() > 0)
                            || (pVersionDetails.getCsdAgeDays() != null && pVersionDetails.getCsdAgeDays() >= 28)) {
                        Label lblPatientWeightData = new Label();
                        lblPatientWeightData.setText(String.valueOf(pVersionDetails.getCsdAdmissionWeight()));// + " " + Lang.getPatientWeightMeasureGram());//currentCase.get);
                        lblPatientWeightData.setMaxWidth(Double.MAX_VALUE);
                        HBox.setHgrow(lblPatientWeightData, Priority.ALWAYS);
//                    lblPatientWeightData.prefWidthProperty().bind(admWeightWrapper.widthProperty().subtract(100));
                        admWeightWrapper.getChildren().addAll(lblPatientWeightData, measure);
                    } else {
                        NumberSpinner nsPatientWeightData = new NumberSpinner();
                        nsPatientWeightData.setMaxWidth(Double.MAX_VALUE);
                        HBox.setHgrow(nsPatientWeightData, Priority.ALWAYS);
                        nsPatientWeightData.setInteger(pVersionDetails.getCsdAdmissionWeight());//currentCase.get);
                        nsPatientWeightData.setTextAlignment(Pos.CENTER_LEFT);
                        nsPatientWeightData.getIntegerProperty().addListener(new ChangeListener<Integer>() {
                            @Override
                            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                                if (newValue != null) {
                                    pVersionDetails.setCsdAdmissionWeight(newValue);
                                    pVersion.saveCaseDetailsEntity(pVersionDetails);
                                    performGroup(pVersion);
                                }
                            }
                        });
                        nsPatientWeightData.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
//                    nsPatientWeightData.prefWidthProperty().bind(admWeightWrapper.widthProperty().subtract(100));
                        admWeightWrapper.getChildren().addAll(nsPatientWeightData, measure);
                    }
                    admWeightWrapper.getStyleClass().add("case-management-grid-even");
                    return admWeightWrapper;
                case DisReason12:
                    VBox disReasonWrapper = new VBox();
                    disReasonWrapper.setPadding(new Insets(2));
                    ComboBox<DischargeReasonEn> disReason = new ComboBox<>(FXCollections.observableArrayList(DischargeReasonEn.values()));
                    disReason.setMaxWidth(Double.MAX_VALUE);
                    disReason.getSelectionModel().select(pVersionDetails.getCsdDisReason12En());
                    disReason.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DischargeReasonEn>() {
                        @Override
                        public void changed(ObservableValue<? extends DischargeReasonEn> observable, DischargeReasonEn oldValue, DischargeReasonEn newValue) {
                            pVersionDetails.setCsdDisReason12En(newValue);
                            saveAndGroup(pVersion);
                        }
                    });
                    disReason.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    disReasonWrapper.getChildren().addAll(disReason);
                    disReasonWrapper.getStyleClass().add("case-management-grid-odd");
                    return disReasonWrapper;
                case DisReason3:
                    VBox disReason2Wrapper = new VBox();
                    disReason2Wrapper.setPadding(new Insets(2));
                    ComboBox<DischargeReason2En> disReason2 = new ComboBox<>(FXCollections.observableArrayList(DischargeReason2En.values()));
                    disReason2.setMaxWidth(Double.MAX_VALUE);
                    disReason2.getSelectionModel().select(pVersionDetails.getCsdDisReason3En());
                    disReason2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DischargeReason2En>() {
                        @Override
                        public void changed(ObservableValue<? extends DischargeReason2En> observable, DischargeReason2En oldValue, DischargeReason2En newValue) {
                            pVersionDetails.setCsdDisReason3En(newValue);
                            saveAndGroup(pVersion);
                        }
                    });
                    disReason2.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    disReason2Wrapper.getChildren().addAll(disReason2);
                    disReason2Wrapper.getStyleClass().add("case-management-grid-even");
                    return disReason2Wrapper;
                case DischargeText:
                    HBox disDateTimeWrapper = new HBox();
                    disDateTimeWrapper.setPadding(new Insets(2));
                    disDateTimeWrapper.setSpacing(5);
                    Label disDate = new Label(Lang.toDate(pVersionDetails.getCsdDischargeDate()));
                    Label disTime = new Label(Lang.toTime(pVersionDetails.getCsdDischargeDate()));
                    disDateTimeWrapper.getChildren().addAll(disDate, disTime);
                    disDateTimeWrapper.getStyleClass().add("case-management-grid-odd");
                    return disDateTimeWrapper;
                case AdmMode:
                    ComboBox<AdmissionModeEn> admMode = new ComboBox<>(
                            FXCollections.observableArrayList(AdmissionModeEn.values(versionContent.getCaseDetails().getHospitalCase().getCsCaseTypeEn())));
                    admMode.setMaxWidth(Double.MAX_VALUE);
                    admMode.getSelectionModel().select(pVersionDetails.getCsdAdmodEn());
                    admMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AdmissionModeEn>() {
                        @Override
                        public void changed(ObservableValue<? extends AdmissionModeEn> observable, AdmissionModeEn oldValue, AdmissionModeEn newValue) {
                            pVersionDetails.setCsdAdmodEn(newValue);
                            saveAndGroup(pVersion);
                        }
                    });
                    admMode.setConverter(new StringConverter<AdmissionModeEn>() {
                        @Override
                        public String toString(AdmissionModeEn object) {
                            return object == null ? "" : object.getTranslation().getValue();
                        }

                        @Override
                        public AdmissionModeEn fromString(String string) {
                            return null;
                        }
                    });
                    admMode.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    VBox admModeWrapper = new VBox(admMode);
                    admModeWrapper.setPadding(new Insets(2));
                    admModeWrapper.getStyleClass().add("case-management-grid-even");
                    return admModeWrapper;
                case Hmv:
                    NumberSpinner nsHmv = new NumberSpinner();
                    nsHmv.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(nsHmv, Priority.ALWAYS);
                    nsHmv.setInteger(pVersionDetails.getCsdHmv());//currentCase.get);
                    nsHmv.setTextAlignment(Pos.CENTER_LEFT);
//                nsHmv.getIntegerProperty().addListener(new ChangeListener<Integer>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
//                        if(newValue != null){
//                            pVersionDetails.setCsdHmv(newValue);
//                            pVersion.saveCaseDetailsEntity(pVersionDetails);
//                            pVersion.performGroup();
//                        }
//                    }
//                });
                    nsHmv.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            //ignore focus gain
                            if (newValue) {
                                return;
                            }
                            handleHmvChange(nsHmv.getText(), pVersion, pVersionDetails);
                        }
                    });
                    nsHmv.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (KeyCode.ENTER.equals(event.getCode())) {
                                handleHmvChange(nsHmv.getText(), pVersion, pVersionDetails);
                            }
                        }
                    });
                    nsHmv.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    VBox hmvWrapper = new VBox(nsHmv);
                    hmvWrapper.setPadding(new Insets(2));
                    hmvWrapper.getStyleClass().add("case-management-grid-odd");
                    return hmvWrapper;
                case Age:
                    ComboBox<AgeEn> cbAge = new ComboBox<>();
                    cbAge.setItems(FXCollections.observableArrayList(AgeEn.values()));
                    cbAge.setConverter(new StringConverter<AgeEn>() {
                        @Override
                        public String toString(AgeEn object) {
                            if (object == null) {
                                return "";
                            }
                            return object.getLangKey();
                        }

                        @Override
                        public AgeEn fromString(String string) {
                            return AgeEn.valueOf(string);
                        }
                    });
                    NumberSpinner nsAge = new NumberSpinner();
                    nsAge.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(nsAge, Priority.ALWAYS);
                    if (pVersionDetails.getCsdAgeDays() > 0) {
                        nsAge.setInteger(pVersionDetails.getCsdAgeDays());
                        cbAge.getSelectionModel().select(AgeEn.AGEINDAYS);
                    } else {
                        nsAge.setInteger(pVersionDetails.getCsdAgeYears());//currentCase.get);
                        cbAge.getSelectionModel().select(AgeEn.AGEINYEARS);
                    }
                    nsAge.setTextAlignment(Pos.CENTER_LEFT);
                    nsAge.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            //ignore focus gain
                            if (newValue) {
                                return;
                            }
                            handleAgeChange(nsAge.getText(), cbAge.getSelectionModel().getSelectedItem(), pVersion, pVersionDetails);
                        }
                    });
                    nsAge.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (KeyCode.ENTER.equals(event.getCode())) {
                                handleAgeChange(nsAge.getText(), cbAge.getSelectionModel().getSelectedItem(), pVersion, pVersionDetails);
                            }
                        }
                    });
                    cbAge.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            //ignore focus gain
                            if (newValue) {
                                return;
                            }
                            handleAgeChange(nsAge.getText(), cbAge.getSelectionModel().getSelectedItem(), pVersion, pVersionDetails);
                        }
                    });
                    cbAge.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (KeyCode.ENTER.equals(event.getCode())) {
                                handleAgeChange(nsAge.getText(), cbAge.getSelectionModel().getSelectedItem(), pVersion, pVersionDetails);
                            }
                        }
                    });
                    nsAge.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    HBox ageWrapper = new HBox(nsAge, cbAge);
                    ageWrapper.setSpacing(5);
                    ageWrapper.setPadding(new Insets(2));
                    ageWrapper.getStyleClass().add("case-management-grid-even");
                    return ageWrapper;
                case Los:
                    Label labelLos = new Label(getLosDisplay(pVersionDetails));
//                if(isDisabled()){
//                    labelLos.getText();
//                }
                    VBox losWrapper = new VBox(labelLos);
                    losWrapper.getStyleClass().add("case-management-grid-odd");
                    losWrapper.setPadding(new Insets(2));
                    pVersion.getListenerAdapter().addChangeListener(pVersion.groupingResultProperty(), new ChangeListener<TGroupingResults>() {
                        @Override
                        public void changed(ObservableValue<? extends TGroupingResults> observable, TGroupingResults oldValue, TGroupingResults newValue) {
                            reloadAndUpdateVersionData(pVersion, pVersionDetails, getColumnIndex(losWrapper));
                        }
                    });
//                    pVersion.groupingResultProperty().addListener(new ChangeListener<TGroupingResults>() {
//                        @Override
//                        public void changed(ObservableValue<? extends TGroupingResults> observable, TGroupingResults oldValue, TGroupingResults newValue) {
//                            reloadAndUpdateVersionData(pVersion, pVersionDetails, getColumnIndex(losWrapper));
//                        }
//                    });
                    return losWrapper;
                case SimulDays:

                    Long losAlt = Objects.requireNonNullElse(pVersionDetails.getCsdLosAlteration(), 0L);
                    Integer mdAlt = pVersionDetails.getCsdLosMdAlteration() == null?0:pVersionDetails.getCsdLosMdAlteration();
                    Long negative = Math.abs((Objects.requireNonNullElse(pVersionDetails.getCsdLos(), 0L) - losAlt  + mdAlt + 1)) * -1L;
                    
                    Integer minValue = Integer.parseInt(negative.toString());
                    days = new NumberSpinner(0, minValue, 10000);
                    days.setText(String.valueOf(pVersionDetails.getCsdLosAlteration()));
                    days.setDisable(!pVersionDetails.getCsdIsLocalFl() || pVersion.isCanceled());
                    HBox simDays = new HBox();
//                    LabeledNumberSpinner simDays = new LabeledNumberSpinner("mit KürzungPflege", days);
                    Label lab1 = new Label(Lang.getDaysSimulWithDeductionCare());
                    lab1.setMaxHeight(Double.MAX_VALUE);
                    lab1.setMinWidth(Label.USE_PREF_SIZE);
                    lab1.setMaxWidth(Label.USE_PREF_SIZE);
                    lab1.setWrapText(true);
                    Label lab2 = new Label(Lang.getDaysSimulNoDeductionCare());
                    lab2.setWrapText(true);
                    lab2.setMinWidth(Label.USE_PREF_SIZE);
                    lab2.setMaxWidth(Label.USE_PREF_SIZE);
//                    lab2.setTextAlignment(TextAlignment.JUSTIFY);
                    lab2.setMaxHeight(Double.MAX_VALUE);
                    
                    days2 = new NumberSpinner(0, minValue, 0);
                    
                    simDays.setSpacing(5);
                    simDays.setMaxWidth(Double.MAX_VALUE);
                    days2.setText(String.valueOf(mdAlt));

                    simDays.getChildren().addAll(lab1, days, lab2, days2);
                    days2.disableProperty().set(pVersionDetails.getCsdAdmissionYear() < 2020 
                            && CpxClientConfig.instance().getSelectedGrouper().getCatalogYear() < 2020 
                    || pVersionDetails.getHospitalCase() == null || !CaseTypeEn.DRG.equals(  pVersionDetails.getHospitalCase().getCsCaseTypeEn()));
                    VBox daysWrapper = new VBox(simDays);
                    daysWrapper.setPadding(new Insets(2,4,2,2));

                    days.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            //ignore gain focus
                            if (newValue) {
                                return;
                            }
                            String param = days.getText();
                            handleLosAltChange(param, pVersion, pVersionDetails);
                        }
                    });
                    days.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (KeyCode.ENTER.equals(event.getCode())) {
                                String param = days.getText();
                                handleLosAltChange(param, pVersion, pVersionDetails);
                            }
                        }
                    });
                    days2.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            //ignore gain focus
                            if (newValue) {
                                return;
                            }
                            String param = days2.getText();
                            handleLosAltChange(param, pVersion, pVersionDetails, true);
                        }
                    });
                    days2.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (KeyCode.ENTER.equals(event.getCode())) {
                                String param = days2.getText();
                                handleLosAltChange(param, pVersion, pVersionDetails, true);
                            }
                        }
                    });
//                DelayedFields.bindDelay(days, new Callback<String, Boolean>() {
//                    @Override
//                    public Boolean call(String param) {
//                        if (!param.equals("-")) {
//                            int parsedValue = param.isEmpty() ? 0 : Integer.valueOf(param);
//                            pVersionDetails.setCsdLosAlteration((long) parsedValue);
//                            saveAndGroup(pVersion);
////                            long start = System.currentTimeMillis();
////                            reloadAndUpdateVersionData(pVersion,pVersionDetails,getColumnIndex(daysWrapper));
//                        }
//                        return true;
//                    }
//
//                }, 120);
                    daysWrapper.getStyleClass().add("case-management-grid-even");
                    return daysWrapper;
                case Leave:
                    Label labelLeave = new Label(String.valueOf(pVersionDetails.getCsdLeave()));
                    VBox leaveWrapper = new VBox(labelLeave);
                    leaveWrapper.setPadding(new Insets(2));
                    leaveWrapper.getStyleClass().add("case-management-grid-odd");
                    return leaveWrapper;
                //CPX-1518 Verweildauer Intensiv (T_CASE_DETAILS.LOS_INTENSIV )
                case LosIntensiv:
                    Label labelLosIntensiv = new Label(String.valueOf(pVersionDetails.getCsdLosIntensiv()));
                    VBox losIntinsiveWrapper = new VBox(labelLosIntensiv);
                    losIntinsiveWrapper.setPadding(new Insets(2));
                    losIntinsiveWrapper.getStyleClass().add("case-management-grid-odd");
                    return losIntinsiveWrapper;

                case CareDays:

                    Integer careMinValue = 0;
                    NumberSpinner careDaysSp = new NumberSpinner(0, careMinValue, 10000);
                    careDaysSp.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(careDaysSp, Priority.ALWAYS);
                    VBox careWrapper = new VBox(careDaysSp);
                    careWrapper.setPadding(new Insets(2));
                    careDaysSp.setText("0");
                    careDaysSp.setDisable(true);
                    TGroupingResults res =  pVersion.getLastGroupingResultFromDb();
                    careResultProperty().addListener(new ChangeListener<Integer> (){
                        @Override
                        public void changed(ObservableValue<? extends Integer> ov, Integer oldVal, Integer newVal) {
                            if(newVal != null ){
                                careDaysSp.setText(newVal.toString());
                            }
                        }
                   
                    });
                    careResultProperty().set((res == null||res.getCaseDrg() == null)? 0:res.getCaseDrg().getDrgcCareDays());

//                    careDaysSp.setDisable(!pVersionDetails.getCsdIsLocalFl()
//                            || pVersion.isCanceled()
//                            || pVersionDetails.getHospitalCase() == null
//                            || !pVersionDetails.getHospitalCase().getCsCaseTypeEn().equals(CaseTypeEn.DRG)
//                            || CpxClientConfig.instance().getSelectedGrouper() == null
//                            || CpxClientConfig.instance().getSelectedGrouper().getCatalogYear() < 2020
//                            || CpxClientConfig.instance().getSelectedGrouper().equals(GDRGModel.AUTOMATIC) && pVersionDetails.getCsdAdmissionYear() < 2020);
//                days.setMinValue(Integer.parseInt(negative.toString()));
//                days.setMaxCharacter(Integer.MAX_VALUE);
//                days.setMaxValue(Integer.MAX_VALUE);
//                    careDaysSp.focusedProperty().addListener(new ChangeListener<Boolean>() {
//                        @Override
//                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                            //ignore gain focus
//                            if (newValue) {
//                                return;
//                            }
//                            String param = careDaysSp.getText();
//                            handleCareDaysChange(param, pVersion, pVersionDetails);
//                        }
//                    });
//                    careDaysSp.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//                        @Override
//                        public void handle(KeyEvent event) {
//                            if (KeyCode.ENTER.equals(event.getCode())) {
//                                String param = careDaysSp.getText();
//                                handleCareDaysChange(param, pVersion, pVersionDetails);
//                            }
//                        }
//                    });

                    careWrapper.getStyleClass().add("case-management-grid-even");
                    return careWrapper;
                case Baserate:
                    Double baserate = versionContent.getCaseBaserate(); //pVersionDetails.getCsdAdmissionDate());
                    Label labelBaserate = new Label(Lang.toDecimal(baserate, 2));
                    if (Double.doubleToRawLongBits(baserate) == Double.doubleToRawLongBits(0.0d)) {
                        labelBaserate.getStyleClass().add("red-colored-label");
                    }

                    VBox baseRateWrapper = new VBox(labelBaserate);
                    baseRateWrapper.setPadding(new Insets(2));
                    baseRateWrapper.getStyleClass().add("case-management-grid-even");
                    return baseRateWrapper;
                default:
                    LOG.log(Level.WARNING, "Unknown index: {0}", index);
            }
        }
        return new VBox();
    }

    private void reloadAndUpdateVersionData(E pVersion, TCaseDetails pVersionDetails, int pColIndex) {
        pVersion.reload();
        TCaseDetails updatedDetails = pVersion.getCaseDetails();
        pVersionDetails.setCsdLos(updatedDetails.getCsdLos());
        pVersionDetails.setCsdLeave(updatedDetails.getCsdLeave());
        updateLabel(11, pColIndex, getLosDisplay(pVersionDetails));
        updateLabel(13, pColIndex, "" + pVersionDetails.getCsdLeave());

    }

    private void updateLabel(int rowIndex, Integer columnIndex, String newValue) {
        Node ctrl = getControl(rowIndex, columnIndex);
        if (ctrl != null && ctrl instanceof Label) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((Labeled) ctrl).setText(newValue);
                }
            });
        }
    }

    private Node getControl(Integer pRow, Integer pCol) {
        for (Node child : getChildren()) {
            if (Objects.equals(getRowIndex(child), pRow) && Objects.equals(getColumnIndex(child), pCol)) {
                return ((VBox) child).getChildren().get(0);
            }
        }
        return null;
    }

    private void setUpContent(List<RowConstraints> pRowConstraints, E pVersion) throws CpxIllegalArgumentException {
        for (int index = 0; index < pRowConstraints.size(); index++) {
            Region content = createVersionColumn(index, pVersion);
            RowConstraints parentRow = pRowConstraints.get(index);
            RowConstraints childRow = new RowConstraints();

            getRowConstraints().add(index, childRow);
            addRow(index, content);

            //update constraint when content is shown
            content.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            parentRow.setMinHeight(newValue.doubleValue());
                            parentRow.setPrefHeight(newValue.doubleValue());
                            parentRow.setMaxHeight(newValue.doubleValue());

                            childRow.setMinHeight(newValue.doubleValue());
                            childRow.setPrefHeight(newValue.doubleValue());
                            childRow.setMaxHeight(newValue.doubleValue());
                        }
                    });

                }
            });
        }
    }
    
    private String getMathSign(long pValue){
        return pValue >= 0 ? " + " : " - ";
    }
    
    private String getLosDisplay(TCaseDetails pVersionDetails) {
        long los = Objects.requireNonNullElse(pVersionDetails.getCsdLos(), 0L);
        long losAlt = Objects.requireNonNullElse(pVersionDetails.getCsdLosAlteration(), 0L);
        int losMdAlt =  Objects.requireNonNullElse(pVersionDetails.getCsdLosMdAlteration(), 0);
       return pVersionDetails.getCsdLos()
                + " ("
                + (los - losAlt - losMdAlt)
                + (getMathSign(losAlt)) + Math.abs(losAlt)
                + (losMdAlt == 0?"":(getMathSign(losMdAlt)) + Math.abs(losMdAlt))
                + ")";
    }

    private void handleHmvChange(String text, E pVersion, TCaseDetails pVersionDetails) {
        if (text == null || text.isEmpty()) {
            return;
        }
        //CPX-1288 handle values with bigger char length than numberspinner        
        int numberSpinnerMaxCharLength = 5;
        if (text.length() > numberSpinnerMaxCharLength || Integer.parseInt(text) > 10000) {
            MainApp.showInfoMessageDialog("Der maximal Wert der Beatmungsstunden beträgt 10000.", getScene().getWindow());
            return;
        }
        Integer number = text.isEmpty() ? 0 : Integer.parseInt(text);
        //do not group if value have not changed
        if (number.equals(pVersionDetails.getCsdHmv())) {
            return;
        }
        pVersionDetails.setCsdHmv(number);
        pVersion.saveCaseDetailsEntity(pVersionDetails);
        performGroup(pVersion);
    }
    
    private void performGroup(E pVersion){
        TGroupingResults res = pVersion.performGroup();
        if(res != null && res.getCaseDrg() != null){
             setCareResult( res.getCaseDrg().getDrgcCareDays());
         }else{
            setCareResult(0);
        }
    }

    private void handleAgeChange(String text, AgeEn selectedItem, E pVersion, TCaseDetails pVersionDetails) {
        if (text == null || text.isEmpty()) {
            return;
        }
        Integer number = text.isEmpty() ? 0 : Integer.parseInt(text);
        switch (selectedItem) {
            case AGEINYEARS:
                if (number.equals(pVersionDetails.getCsdAgeYears())) {
                    return;
                }
                pVersionDetails.setCsdAgeYears(number);
                pVersionDetails.setCsdAgeDays(0);
                pVersion.saveCaseDetailsEntity(pVersionDetails);
                performGroup(pVersion);
                break;
            case AGEINDAYS:
                if (number.equals(pVersionDetails.getCsdAgeDays())) {
                    return;
                }
                pVersionDetails.setCsdAgeDays(number);
                pVersionDetails.setCsdAgeYears(0);
                pVersion.saveCaseDetailsEntity(pVersionDetails);
               performGroup(pVersion);
                break;
            default:
                LOG.log(Level.WARNING, "unknown age type: {0}", selectedItem);
                break;
        }

    }

    private void handleLosAltChange(String pText, E pVersion, TCaseDetails pDetails) {
        handleLosAltChange(pText, pVersion, pDetails, false);
    }

    private void handleLosAltChange(String pText, E pVersion, TCaseDetails pDetails, boolean isMdValue) {
        if (pText == null) {
            return;
        }
        if(pDetails == null){
            return;
        }
        if (!pText.equals("-")) {
            Long parsedValue;
            try {
                parsedValue = pText.isEmpty() ? 0 : Long.valueOf(pText);
            } catch (NumberFormatException ex) {
                LOG.log(Level.FINEST, "This is value cannot be parsed as long: " + pText, ex);
                return;
            }

            int altLos = Objects.requireNonNullElse(pDetails.getCsdLosAlteration(), 0L).intValue();
            int altMdLos = pDetails.getCsdLosMdAlteration() == null?0:pDetails.getCsdLosMdAlteration();
            int los =  Objects.requireNonNullElse(pDetails.getCsdLos(), 0L).intValue() - altLos - altMdLos;
            int parsed = parsedValue.intValue();

            if(isMdValue){
                Integer mdLos = Objects.requireNonNullElse(pDetails.getCsdLosMdAlteration(),0);
                if (parsedValue.equals(mdLos.longValue())) {
                     return;
                }
                boolean changed = false;
                while(los + altLos + parsed <= 0 
//                        && parsed < 0
                        ){
                    parsed++;
                    changed = true;
                }
                 pDetails.setCsdLosMdAlteration(parsed);
                 if(changed){
                     days2.setText(String.valueOf(parsed));
                 }
            }else{
                boolean changed = false;
                if (parsedValue.equals(pDetails.getCsdLosAlteration())) {
                     return;
                 }
                while(los + altMdLos + parsed <= 0 
//                        && parsed < 0
                        ){
                    parsed++;
                    changed = true;
                }
                pDetails.setCsdLosAlteration(Long.valueOf(parsed));
                if(changed){
                    days.setText(String.valueOf(parsed));
                }
            }
//            saveAndGroup(pVersion);
            pVersion.saveCaseDetailsEntity(pDetails);

            performGroup(pVersion);
             return;
        }
        return;
    }

//    private void handleCareDaysChange(String pText, E pVersion, TCaseDetails pDetails) {
//        if (pText == null) {
//            return;
//        }
//        if (!pText.equals("-")) {
//            if (!pText.isEmpty()) {
//                try {
//                    Long.parseLong(pText);
//                } catch (NumberFormatException ex) {
//                    LOG.log(Level.FINEST, "This is value cannot be parsed as long: " + pText, ex);
//                    return;
//                }
//            }
////            if (parsedValue.equals(pDetails.getCsdCareDays())) {
////                return;
////            }
////            pDetails.setCsdCareDays(parsedValue);
////            saveAndGroup(pVersion);
//            pVersion.saveCaseDetailsEntity(pDetails);
//            pVersion.performGroup();g
//        }
//    }
    ObjectProperty<Integer>  careResultProperty ;
    
    private ObjectProperty<Integer> careResultProperty(){
        if(careResultProperty == null){
            careResultProperty = new SimpleObjectProperty<>();
        }
        return careResultProperty;
    }
    
//    private Integer getCareResult(){
//        return careResultProperty().get();
//    }
    
    private void setCareResult(Integer pCare){
        careResultProperty().set(pCare);
    }
}

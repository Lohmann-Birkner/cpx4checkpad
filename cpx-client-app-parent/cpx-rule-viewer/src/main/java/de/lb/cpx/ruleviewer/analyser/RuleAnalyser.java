/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.analyser;

import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.TCase;
import de.lb.cpx.server.commonDB.model.CCase;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;

/**
 * Rule Analyser Control Analyse and show test result for Rules against a
 * hospital case
 *
 * @author wilde
 */
public class RuleAnalyser extends Control {

    private static final Logger LOG = Logger.getLogger(RuleAnalyser.class.getName());

    public static final String ANALYSE_VALUE = "analyse";
    private Callback<TCase, TransferRuleAnalyseResult> onAnalyse = (TCase p) -> null;
    private Callback<TCase, TransferRuleAnalyseResult> onHistoryAnalyse = (TCase p) -> null;
    private Callback<TCase, Void> onPerformGroup = (TCase p) -> null;
    private final ObservableList<CCase> cases = FXCollections.observableArrayList();
    private Callback<CCase, Boolean> onSaveAnalyserCase;
    private Callback<CCase, Boolean> onSaveHospitalCase;

    public RuleAnalyser() {
        super();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new RuleAnalyserSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(RuleAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }
    private final Map<Long, TCase> loadedCasesMap = new HashMap<>();

    public synchronized TCase getCase(CCase pId) {
        if (pId == null) {
            return null;
        }
        if (!loadedCasesMap.containsKey(pId.getId())) {
            LOG.info("load case data:  " + pId.getId());
            loadedCasesMap.put(pId.getId(), getOnLoadHospitalCase().call(pId));
        }
        return loadedCasesMap.get(pId.getId());
    }
//    if (ruleResult == null) {
//            ExecutorService pool = Executors.newFixedThreadPool(2);
//            //trigger loading
//            ruleResult = pool.submit(new RuleParser());
//            LOG.log(Level.SEVERE, "fetch rule!");
//        }
//        //wait till future result is finished
//        return ruleResult.get();

    public void updateCase(CCase pCse, TCase changedCase) {
        if (pCse == null) {
            return;
        }
        if (changedCase == null) {
            return;
        }
        if (loadedCasesMap.containsKey(pCse.getId())) {
            loadedCasesMap.put(pCse.getId(), changedCase);
        }
    }
//    public ObjectProperty<TCase> caseProperty() {
//        if (caseProperty == null) {
//            caseProperty = new SimpleObjectProperty<>();
//        }
//        return caseProperty;
//    }
//
//    public TCase getCase() {
//        return caseProperty().get();
//    }
//
//    public void setCase(TCase pRule) {
//        caseProperty().set(pRule);
//    }

    public void setOnAnalyse(@NotNull Callback<TCase, TransferRuleAnalyseResult> pOnAnalyse) {
        onAnalyse = Objects.requireNonNull(pOnAnalyse, "Callback can not be null");
    }

    public Callback<TCase, TransferRuleAnalyseResult> getOnAnalyse() {
        return onAnalyse;
    }

    public Callback<TCase, TransferRuleAnalyseResult> getOnHistoryAnalyse() {
        return onHistoryAnalyse;
    }

    public void setOnPerformGroup(@NotNull Callback<TCase, Void> pOnPerformGroup) {
        onPerformGroup = Objects.requireNonNull(pOnPerformGroup, "Callback can not be null");
    }

    
    public void setOnHistoryAnalyse(@NotNull Callback<TCase, TransferRuleAnalyseResult> pOnAnalyse) {
        onHistoryAnalyse = Objects.requireNonNull(pOnAnalyse, "Callback can not be null");
    }

    public void setOnSaveAnalyserCase(@NotNull Callback<CCase, Boolean> pOnSaveAnalyserCase) {
        onSaveAnalyserCase = Objects.requireNonNull(pOnSaveAnalyserCase, "Callback can not be null");
    }

    public Callback<CCase, Boolean> getOnSaveAnalyserCase() {
        return onSaveAnalyserCase;
    }

    public void setOnSaveHospitalCase(@NotNull Callback<CCase, Boolean> pOnSaveHospitalCase) {
        onSaveHospitalCase = Objects.requireNonNull(pOnSaveHospitalCase, "Callback can not be null");
    }

    public Callback<CCase, Boolean> getOnSaveHospitalCase() {
        return onSaveHospitalCase;
    }

    public ObservableList<CCase> getCaseList() {
        return cases;
    }

    public void addCase(CCase pCase) {
        cases.add(pCase);
    }

    public void addCases(Collection<? extends CCase> pCases) {
        cases.addAll(pCases);
    }

    public void analyse() {
        if (getOnAnalyse() == null && this.getOnHistoryAnalyse() == null
                
                ) {
            return;
        }
        getProperties().put(ANALYSE_VALUE, null);
    }

    private final BooleanProperty showAdditionalResultsProperty = new SimpleBooleanProperty(false);

    public BooleanProperty showAdditionalResultsProperty() {
        return showAdditionalResultsProperty;
    }

    public boolean isShowAdditionalResults() {
        return showAdditionalResultsProperty().get();
    }

    public void setShowAdditionalResults(boolean pShow) {
        showAdditionalResultsProperty().set(pShow);
    }

    protected ObjectProperty<TransferRuleAnalyseResult> analyserResultProperty;

    protected ObjectProperty<TransferRuleAnalyseResult> analyserResultProperty() {
        if (analyserResultProperty == null) {
            analyserResultProperty = new SimpleObjectProperty<>();
        }
        return analyserResultProperty;
    }

    protected TransferRuleAnalyseResult getAnalyserResult() {
        return analyserResultProperty().get();
    }

    protected void setAnalyserResult(TransferRuleAnalyseResult pResult) {
        analyserResultProperty().set(pResult);
    }

    protected Callback<CCase, TCase> onLoadHospitalCase = (CCase param) -> null;
    protected Callback< TCase, TCase> onLoadHospitalCaseGrouped = (TCase param) -> null;

    public Callback<CCase, TCase> getOnLoadHospitalCase() {
        return onLoadHospitalCase;
    }

    public void setOnLoadHospitalCase(@NotNull Callback<CCase, TCase> pOnLoadHospitalCase) {
        onLoadHospitalCase = Objects.requireNonNull(pOnLoadHospitalCase, "onloadHospitalCase can not be null");
    }
    public void setOnLoadHospitalCaseGrouped(@NotNull Callback<TCase, TCase> pOnLoadHospitalCaseGrouped) {
        onLoadHospitalCaseGrouped = Objects.requireNonNull(pOnLoadHospitalCaseGrouped, "onloadHospitalCaseGrouped can not be null");
    }

    public Callback<TCase, TCase> getOnLoadHospitalCaseGrouped() {
        return onLoadHospitalCaseGrouped;
    }

    protected Callback<Boolean, List<CCase>> onReloadHospitalCase = (Boolean param) -> null;

    public Callback<Boolean, List<CCase>> getOnReloadAnalyserCases() {
        return onReloadHospitalCase;
    }

    public void setOnReloadAnalyserCases(@NotNull Callback<Boolean, List<CCase>> pOnReloadHospitalCase) {
        onReloadHospitalCase = Objects.requireNonNull(pOnReloadHospitalCase, "onloadHospitalCase can not be null");
    }

    protected Callback<List<Long>, Boolean> onDeleteAnalyserCases = (List<Long> param) -> false;

    public Callback<List<Long>, Boolean> getOnDeleteAnalyserCases() {
        return onDeleteAnalyserCases;
    }

    public void setOnDeleteAnalyserCases(@NotNull Callback<List<Long>, Boolean> pOnDeleteAnalyserCases) {
        onDeleteAnalyserCases = Objects.requireNonNull(pOnDeleteAnalyserCases, "onDeleteAnalyserCases can not be null");
    }
    private ObjectProperty<AnalyserCaseCompareMode> analyserCaseCompareModeProperty;

    public ObjectProperty<AnalyserCaseCompareMode> analyserCaseCompareModeProperty() {
        if (analyserCaseCompareModeProperty == null) {
            analyserCaseCompareModeProperty = new SimpleObjectProperty<>(AnalyserCaseCompareMode.CATEGORY);
        }
        return analyserCaseCompareModeProperty;
    }

    public AnalyserCaseCompareMode getAnalyserCaseCompareMode() {
        return analyserCaseCompareModeProperty().get();
    }

    public void setAnalyserCaseCompareMode(AnalyserCaseCompareMode pMode) {
        analyserCaseCompareModeProperty.set(pMode);
    }

    public void setHistoryAnalyse(Callback<TCase, TransferRuleAnalyseResult> callback) {
        onHistoryAnalyse = callback;
    }

    Callback<TCase, Void> getOnPerformGroup() {
        return onPerformGroup;
    }

    public enum AnalyserCaseCompareMode {
        DATE("Datum"), CATEGORY("Kategorie"), CASENUMBER("Fallnummer");
        private final String text;

        private AnalyserCaseCompareMode(String pText) {
            text = pText;
        }

        public String getText() {
            return text;
        }

    }
}

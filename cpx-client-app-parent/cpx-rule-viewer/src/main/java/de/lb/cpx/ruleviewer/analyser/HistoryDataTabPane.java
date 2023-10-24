/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.analyser;

import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.TCase;
import de.lb.cpx.ruleviewer.analyser.tabs.HistoryDataTab;
import de.lb.cpx.ruleviewer.analyser.tabs.HistoryFeeDataTab;
import de.lb.cpx.ruleviewer.analyser.tabs.HistoryDataTabIF;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 */
public class HistoryDataTabPane extends TabPane{
 
    // for history cases
    private HistoryDataTab tabHistoryCases;
    private HistoryFeeDataTab tabHistoryFees;
    private HistoryDataTab tabHistoryCoding;
    private HistoryDataTab tabHistoryGroupResults;
    List<TCase> casesFromTreeItem = new ArrayList<>();
    private ObjectProperty<TCase> selectedCaseProperty;
    
    public HistoryDataTabPane(){
        super();
        
        tabHistoryCases = new HistoryDataTab(HistoryDataTab.DATA_TYPE.CASE_DATA);
        tabHistoryFees = new HistoryFeeDataTab();
        tabHistoryCoding = new HistoryDataTab(HistoryDataTab.DATA_TYPE.CODING_DATA);
        tabHistoryGroupResults = new HistoryDataTab(HistoryDataTab.DATA_TYPE.DRG_PEPP_DATA);
        
        getTabs().addAll(tabHistoryCases, tabHistoryFees, tabHistoryCoding, tabHistoryGroupResults);
        
        // Changes are observed in CASE_DATA - Tab and executed in all other tabs
        selectedCaseProperty().addListener(new ChangeListener<TCase>(){
            @Override
            public void changed(ObservableValue<? extends TCase> observable, TCase oldValue, TCase newValue) {
                tabHistoryCoding.setSelection4Case(newValue);
                tabHistoryGroupResults.setSelection4Case(newValue);
                tabHistoryFees.setSelection4Case(newValue);
            }

        });
    }
    
    public void setHistoryCases(List<TCase> pCasesFromTreeItem) {
         ObservableList<Tab> tabs = getTabs();
         casesFromTreeItem = pCasesFromTreeItem;
         for(Tab tab: tabs){
             ((HistoryDataTabIF)tab).setHistoryCases(pCasesFromTreeItem);
             ((HistoryDataTabIF)tab).reload(); 
         }
    }

    public List<TCase> getHistoryCases() {
        return casesFromTreeItem;
    }

    void setOnHistoryAnalyse(Callback <TCase, Void>  onHistoryAnalyse) {
        tabHistoryCases.setOnHistoryAnalyse(onHistoryAnalyse);
    }

    

    void setOnPerformGroup(Callback <TCase, Void>  onPerformGroup) {
        tabHistoryGroupResults.setOnPerformGroup(onPerformGroup);
    }

    void setUpAnalyserResult() {
        tabHistoryCases.setUpAnalyserResult();
    }
    
    private ObjectProperty<TCase> selectedCaseProperty(){
        if(selectedCaseProperty == null){
            selectedCaseProperty = new SimpleObjectProperty<>();
        }
        return selectedCaseProperty;
    }
    
    public void setSelectedCase(TCase pCase){
        selectedCaseProperty().set(pCase);
    }
    
    public TCase getSelectedCase(){
        return selectedCaseProperty().get();
    }
    
}

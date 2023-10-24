/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.analyser.tabs;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.ruleviewer.analyser.HistoryDataTabPane;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 */
public class HistoryDataTab extends Tab implements HistoryDataTabIF{
    private VBox contentBox;
    private HistoryDataTableView  contentTable;
    private ObservableList<TCase> historyCases = FXCollections.observableArrayList();    
    private final DATA_TYPE type;
    private Callback<TCase, Void> onPerformGroup;
    

    public Callback<TCase, Void> getOnPerformGroup() {
        return onPerformGroup;
    }

    public void setOnPerformGroup(Callback<TCase, Void> onPerformGroup) {
        this.onPerformGroup = onPerformGroup;
    }

    public HistoryDataTab(DATA_TYPE pType){
        super(pType.toString());
        type = pType;
        setContent(createContent());
        setClosable(false);
    }
    
    private Node createContent(){
        contentBox = new VBox();
        contentBox.setPadding(new Insets(10, 0, 0, 0));    
        
        contentTable = new HistoryDataTableView(type, this);
        contentBox.getChildren().add(contentTable);

        return contentBox;
    }
    
    public ObservableList<TCase> getHistoryCases(){
        return historyCases;
    };

    public void setHistoryCases(List<TCase> casesFromTreeItem) {
        historyCases.setAll(casesFromTreeItem);
    }

    public void reload() {
        if(contentTable != null){
            if(type.equals(DATA_TYPE.DRG_PEPP_DATA)){
                if(onPerformGroup != null){
                    Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        List<TCase> historyCases = getHistoryCases();
                            for(TCase cs: historyCases){
                            if(cs.getCurrentLocal() == null){
                               continue;
                           }
                           Set<TGroupingResults> allRes = cs.getCurrentLocal().getGroupingResultses();
                           if (allRes == null || allRes.isEmpty()) {
                               onPerformGroup.call(cs);
                           }
                        }
                    contentTable.reload();        
                    }
                });

                } else{
                        contentTable.reload();
                }               
            }else{
                contentTable.reload();
            }
        }

    }

    public HistoryDataTableView getContentTable() {
        return contentTable;
    }


    public void setOnHistoryAnalyse(Callback <TCase, Void> onHistoryAnalyse) {

           contentTable.setOnHistoryAnalyse(onHistoryAnalyse); 

    }
    
    public void setSelectedCase(TCase pCase){
        ((HistoryDataTabPane)getTabPane()).setSelectedCase(pCase);
    }
    
    public void setSelection4Case(TCase pCase){
        contentTable.setSelection4Case(pCase);
    }

    
    public void setUpAnalyserResult() {

            contentTable.setUpAnalyserResult(); 

    }
    
     public enum DATA_TYPE {
        CASE_DATA(){
            public String toString(){
                return "Fall-Daten";
            }
        },
        FEE_DATA(){
            public String toString(){
                return "Entgelt-Daten";
            }

        }, DRG_PEPP_DATA(){
            public String toString(){
                return "DRG/PEPP-Ergebnisse";
            }

        }, CODING_DATA(){
            public String toString(){
                return "Kodierung";
            }
        
        }
    };
}

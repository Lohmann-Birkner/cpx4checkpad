/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.analyser.tabs;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.ruleviewer.util.HistoryFeeTableViewHelper;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

/**
 *
 * @author gerschmann
 */
public class HistoryFeeDataTab extends Tab implements HistoryDataTabIF{
    private VBox contentBox;
    private HistoryFeeTableView contentFeeTable;
    private ObservableList<HistoryFeeTableViewHelper> historyFees = FXCollections.observableArrayList();    
    

    public HistoryFeeDataTab(){
        super(HistoryDataTab.DATA_TYPE.FEE_DATA.toString());

        setContent(createContent());
        setClosable(false);
    }
    
    private Node createContent(){
        contentBox = new VBox();
        contentBox.setPadding(new Insets(10, 0, 0, 0));    

        contentFeeTable = new HistoryFeeTableView(this);
        contentBox.getChildren().add(contentFeeTable);
        
        return contentBox;
    }
    
    public void reload() {
        contentFeeTable.reload();
   
    }

    private void setHistoryFees(List<TCase> casesFromTreeItem) {
        List <HistoryFeeTableViewHelper> fees = new ArrayList<>();
        String csNr = "";
        String setNr = "";
        for(TCase cs: casesFromTreeItem){
            if(cs.getCurrentLocal() == null 
                    || cs.getCurrentLocal().getCaseFees() == null 
                    || cs.getCurrentLocal().getCaseFees().isEmpty()){
                continue;
            }
            for(TCaseFee fee:cs.getCurrentLocal().getCaseFees()){
                if(!csNr.equals(cs.getCsCaseNumber())){
                    csNr = cs.getCsCaseNumber();
                    setNr = csNr;
                }else{
                    setNr = "--\"--";
                }
            
                fees.add(new HistoryFeeTableViewHelper(setNr, fee.getFeecFeekey(), Lang.toDecimal(fee.getFeecValue(), 2) + "€", fee.getFeecCount()));
            }
        }
        historyFees.setAll(fees);
    }
    
    public ObservableList<HistoryFeeTableViewHelper> getHistoryFees(){
        return historyFees;
    }
    public HistoryFeeTableView getContentFeeTable() {
        return contentFeeTable;
    }

    @Override
    public void setSelection4Case(TCase pCase) {
       contentFeeTable.getSelectionModel().clearSelection();
        if(pCase != null){

            String caseNr = pCase.getCsCaseNumber();
            for(HistoryFeeTableViewHelper fee: historyFees){
                if(caseNr.equals(fee.getCaseNr())){
                    contentFeeTable.setSelection4Case(fee);
                    return;
                }
            }
            contentFeeTable.setSelection4Case(null);
        }else{
            contentFeeTable.setSelection4Case(null);
        }
    }

    @Override
    public void setHistoryCases(List<TCase> pCasesFromTreeItem) {
        setHistoryFees(pCasesFromTreeItem);
    }

    String getSelectedCaseNr() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.analyser.tabs;

import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.lang.CpxLanguageInterface;
import de.lb.cpx.shared.lang.Lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javax.ejb.AsyncResult;

/**
 *
 * @author gerschmann
 */
public class HistoryDataTableView extends AsyncTableView<TCase>{

    private static final Logger LOG = Logger.getLogger(HistoryDataTableView.class.getName());

    private final HistoryDataTab parent;
    
    private Callback<TCase, Void> onHistoryAnalyse;
    private ObjectProperty <TCase>  lastSelectedProperty;

    private Callback<TableColumn<TCase, String>, TableCell<TCase, String>> overrunCellFactory
            = (TableColumn<TCase, String> param) -> {
                return new TableCell<TCase, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                if (item == null || empty) {
                    setGraphic(null);
                    return;
                }
                String header = this.getTableColumn().getText();
                Label label = new Label(item);
//                label.setMaxHeight(USE_COMPUTED_SIZE);
//                label.setPrefHeight(USE_COMPUTED_SIZE);
//                label.setFont(Font.font(getFont().getName(), 11));
                setGraphic(label);
                if(header.contains("-Grund") || header.equals("Mdc/Sk")){
                    if(header.startsWith("Aufn.")){
                        AdmissionReasonEn adm = AdmissionReasonEn.findById(item);
                        if(adm != null){
                            label.setTooltip(new Tooltip(adm.getTranslation().getValue()));
                            return;
                        }
                    }else if(header.startsWith("Entl")){
                        DischargeReasonEn dis = DischargeReasonEn.findById(item);
                        if(dis != null){
                            label.setTooltip(new Tooltip(dis.getTranslation().getValue()));
                            return;
                           
                        }
                    }else if(header.equals("Mdc/Sk")){
                        GrouperMdcOrSkEn mdc = GrouperMdcOrSkEn.findById(item);
                        if(mdc != null){
                            label.setTooltip(new Tooltip(mdc.getTranslation().getValue()));
                            return;
                        }
                    }
                }
                OverrunHelper.addInfoTooltip(label);
                
            }
        };
    };
        
    private ChangeListener noChangeSelectListener = new ChangeListener<TCase>(){
            @Override
            public void changed(ObservableValue<? extends TCase> observable, TCase oldValue, TCase newValue) {
                Platform.runLater(() -> resetSelection(newValue));
            
            }
    };
    
        
    public void resetSelection(TCase newValue){
        if(newValue == null ){
             if( getLastSelected() == null){
                 return;
             }
         }
         if(newValue != null){
             if(getLastSelected() == null){
                 getSelectionModel().clearSelection();
             }else{
                 if(newValue.getCsCaseNumber().equals(getLastSelected().getCsCaseNumber())){
                     return;
                 }else{
                     setSelection4Case(getLastSelected());
                 }
             }
         }

     }            

    

    
    public HistoryDataTableView(HistoryDataTab.DATA_TYPE pType, HistoryDataTab pParent){
        super();
        parent = pParent;
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
         getStyleClass().add("stay-selected-table-view");
         getStyleClass().add("table-view-small");
        setColumns(pType);

    }

    public Callback<TCase, Void> getOnHistoryAnalyse() {
        return onHistoryAnalyse;
    }

    public void setOnHistoryAnalyse(Callback <TCase, Void>  onHistoryAnalyse) {
        this.onHistoryAnalyse = onHistoryAnalyse;
    }

    @Override
    public Future<List<TCase>> getFuture() {
        try {
            return new AsyncResult<>(parent.getHistoryCases());
        } catch (CpxIllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "Can not load data, reason: {0}", ex);
        }
        return new AsyncResult<>(new ArrayList<>());
    }

    private void setColumns(HistoryDataTab.DATA_TYPE pType) {

        if(!pType.equals(HistoryDataTab.DATA_TYPE.CASE_DATA)){
            setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event)
                {
                    // no action on mouse click
                    return;
                }
            });
            
            getSelectionModel().selectedItemProperty().addListener(noChangeSelectListener);   
        
        }else{
            getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TCase>(){
                @Override
                public void changed(ObservableValue<? extends TCase> observable, TCase oldValue, TCase newValue) {
                    parent.setSelectedCase(newValue);
                    if(onHistoryAnalyse != null){

                        parent.getHistoryCases();

                        onHistoryAnalyse.call(newValue);
                    }

                }            
            });
            
        }
         switch(pType){
            case CASE_DATA:
                setCaseDataColumns();
                break;
//            case FEE_DATA:
//                setFeeDataColumns();
//                break;
            case DRG_PEPP_DATA:
                setDrgPeppColumns();
                break;
            case CODING_DATA:
                setCodingDataColumns();
                break;
        
            }
            
        }
    
    public void setSelection4Case(TCase pCase){
        setLastSelected(pCase);
        if(pCase != null){

            getSelectionModel().select(pCase);
            refresh();
            this.scrollTo(getSelectionModel().getSelectedIndex() >= 2?getSelectionModel().getSelectedIndex()-2:getSelectionModel().getSelectedIndex());

        }else{
            getSelectionModel().clearSelection();
            refresh();
//            scrollTo(0);

        }
    }
    

    private ObjectProperty<TCase> lastSelectedProperty() {
        if( lastSelectedProperty == null){
            lastSelectedProperty = new SimpleObjectProperty<>();
        };
        return lastSelectedProperty;
    }
    
    private void setLastSelected(TCase pLast){
        lastSelectedProperty().set(pLast);
    }
    
    private TCase getLastSelected(){
        return lastSelectedProperty().get();
    }

    private void setCaseDataColumns() {

       CaseNumberColumn colCaseNumber = new CaseNumberColumn();
        getColumns().add(colCaseNumber);
        HospitalColumn colHosNumber = new HospitalColumn();
        getColumns().add(colHosNumber);
        AdmissionDateColumn colAdmDate = new AdmissionDateColumn();
        getColumns().add(colAdmDate);
        DischargeDateColumn colDisDate = new DischargeDateColumn();
        getColumns().add(colDisDate);
        AdmissionReasonColumn colAdmReason = new AdmissionReasonColumn();
        getColumns().add(colAdmReason);
        DischargeReasonColumn colDisReason = new DischargeReasonColumn();
        getColumns().add(colDisReason);
        LosColumn colLosReason = new LosColumn();
        getColumns().add(colLosReason);
        HmvColumn colHmvReason = new HmvColumn();
        getColumns().add(colHmvReason);
    }


    private void setDrgPeppColumns() {
 
       CaseNumberColumn colCaseNumber = new CaseNumberColumn();
       getColumns().add(colCaseNumber);
       CaseTypeColumn colCaseType = new CaseTypeColumn();
       getColumns().add(colCaseType);
       DrgPeppColumn colDrgPepp = new DrgPeppColumn();
       getColumns().add(colDrgPepp);
       MdcSkColumn colMdc = new MdcSkColumn();
       getColumns().add(colMdc);
       AdrgColumn colAdrg = new AdrgColumn();
       getColumns().add(colAdrg);
       PartitionColumn colPart = new PartitionColumn();
       getColumns().add(colPart);
       HtpColumn colHtp = new HtpColumn();
       getColumns().add(colHtp);
       LtpColumn colLtp = new LtpColumn();
       getColumns().add(colLtp);
       

       
    }

    private void setCodingDataColumns() {
//        setSelectionModel(null);
       CaseNumberColumn colCaseNumber = new CaseNumberColumn();
        getColumns().add(colCaseNumber);
        MainIcdColumn colMainIcd = new MainIcdColumn();
        getColumns().add(colMainIcd);
        AuxIcdColumn colAuxIcd = new AuxIcdColumn();
        getColumns().add(colAuxIcd);
        OpsColumn colOps = new OpsColumn();
        getColumns().add(colOps);
        colAuxIcd.prefWidthProperty().bind(widthProperty().subtract(colMainIcd.widthProperty())
        .subtract(colCaseNumber.widthProperty()).subtract(10.0).divide(2.0));
        colOps.prefWidthProperty().bind(colAuxIcd.widthProperty());
        

    }
    
        private TGroupingResults getAutoResult(TCase pValue){
        if(pValue.getCurrentLocal() == null){
            return null;
        }
        Set<TGroupingResults> allRes = pValue.getCurrentLocal().getGroupingResultses();
        if (allRes == null) {
            return null;
        }
        for (TGroupingResults grpRes : allRes) {
            if (grpRes.getGrpresIsAutoFl() )
                    return grpRes;
            }

            return null;
        }

        public void setUpAnalyserResult() {

            if(onHistoryAnalyse != null && getSelectionModel().getSelectedItem() != null){
                // selected Case is to be placed on the 0 - Position to check Rule toward this case
                parent.getHistoryCases();
                onHistoryAnalyse.call(getSelectionModel().getSelectedItem());
            }
        }



    private class CaseNumberColumn extends StringColumn<TCase> {//TableColumn<TCaseMergeMapping, String> {

         CaseNumberColumn() {
             super(Lang.getCaseNumber());
             setSortable(false);
             setMinWidth(100.0);
             setCellFactory(overrunCellFactory);
         }

         @Override
         public String extractValue(TCase pValue) {
             return pValue.getCsCaseNumber();
         }

     }
// case tab
        private class HospitalColumn extends StringColumn<TCase> {

            HospitalColumn() {
                super(Lang.getHospitalIdentifierObj().getAbbreviation());
                setSortable(false);
                setMaxWidth(100.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return String.valueOf(pValue.getCsHospitalIdent());
            }
        }

        private class LosHoursColumn extends StringColumn<TCase> {

            LosHoursColumn() {
                super("VWD in St.");
                setSortable(false);
                setMaxWidth(100.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return "";          }
        }

        private class HmvColumn extends StringColumn<TCase> {

            HmvColumn() {
                super("Beatm.Dauer");
                setSortable(false);
                setMaxWidth(100.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return  pValue.getCurrentLocal() == null?"":String.valueOf(pValue.getCurrentLocal().getCsdHmv());
            }
        }

        private class LosColumn extends StringColumn<TCase> {

            LosColumn() {
                super("VWD");
                setSortable(false);
                setMaxWidth(50.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return  pValue.getCurrentLocal() == null?"":String.valueOf(pValue.getCurrentLocal().getCsdLos());
            }
        }

       private class AdmissionDateColumn extends StringColumn<TCase> {

            AdmissionDateColumn() {
                super(Lang.getAdmissionDate());
                setSortable(false);
                setMinWidth(110.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return pValue.getCurrentLocal() == null?"":CpxLanguageInterface.toDateTime(pValue.getCurrentLocal().getCsdAdmissionDate(), "dd.MM.yyyy HH:mm");
            }
        }

        private class AdmissionReasonColumn extends StringColumn<TCase> {

            AdmissionReasonColumn() {
                super("Aufn.-Grund");
                setSortable(false);
                setMinWidth(20.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return pValue.getCurrentLocal() == null?"":String.valueOf(pValue.getCurrentLocal().getCsdAdmReason12En().getId());
            }
        }
        private class DischargeReasonColumn extends StringColumn<TCase> {

            DischargeReasonColumn() {
                super("Entl.-Grund");
                setSortable(false);
                setMinWidth(20.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return pValue.getCurrentLocal() == null?"":pValue.getCurrentLocal().getCsdDisReason12En() == null?"":String.valueOf(pValue.getCurrentLocal().getCsdDisReason12En().getId());
            }
        }

       private class DischargeDateColumn extends StringColumn<TCase> {

            DischargeDateColumn() {
                super(Lang.getDischargeDate());
                setSortable(false);
                setMinWidth(110.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return pValue.getCurrentLocal() == null?"":CpxLanguageInterface.toDateTime(pValue.getCurrentLocal().getCsdDischargeDate(), "dd.MM.yyyy HH:mm");
            }
        }
// coding tab
       private class MainIcdColumn extends StringColumn<TCase> {

            MainIcdColumn() {
                super("Hauptdiagnose");
                setSortable(false);
                setMinWidth(100.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                 
                if(pValue.getCurrentLocal() == null || pValue.getCurrentLocal().getCaseDepartments() == null || pValue.getCurrentLocal().getCaseDepartments().isEmpty()){
                    return "";
                }

                for(TCaseDepartment dep: pValue.getCurrentLocal().getCaseDepartments()){
                    Set<TCaseIcd> depIcds = dep.getAllIcds();
                    for(TCaseIcd icd: depIcds){
                        if(icd.getIcdcIsHdxFl()){
                           return icd.getIcdcCode();
                        }
                    }
                }
                return "";
            }
        }
       private class AuxIcdColumn extends StringColumn<TCase> {

            AuxIcdColumn() {
                super("Nebendiagnosen");
                setSortable(false);
                setMinWidth(300.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                
                if(pValue.getCurrentLocal() == null || pValue.getCurrentLocal().getCaseDepartments() == null || pValue.getCurrentLocal().getCaseDepartments().isEmpty()){
                    return "";
                }
                List<String> icds = new ArrayList<>();
                for(TCaseDepartment dep: pValue.getCurrentLocal().getCaseDepartments()){
                    Set<TCaseIcd> depIcds = dep.getAllIcds();
                    for(TCaseIcd icd: depIcds){
                        if(!icd.getIcdcIsHdxFl()){
                            icds.add(icd.getIcdcCode());
                        }
                    }
                }
                Collections.sort(icds);
                return String.join(", ", icds);
            }
        }
       private class OpsColumn extends StringColumn<TCase> {

            OpsColumn() {
                super("Prozeduren");
                setSortable(false);
                setMinWidth(300.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                if(pValue.getCurrentLocal() == null || pValue.getCurrentLocal().getCaseDepartments() == null || pValue.getCurrentLocal().getCaseDepartments().isEmpty()){
                    return "";
                }
                List<String> opss = new ArrayList<>();
                for(TCaseDepartment dep: pValue.getCurrentLocal().getCaseDepartments()){
                    Set<TCaseOps> depOpss = dep.getCaseOpses();
                    for(TCaseOps ops: depOpss){
                            opss.add(ops.getOpscCode());
                        
                    }
                }
                Collections.sort(opss);
                return String.join(", ", opss);
            }
            
        }
// result (DRG/PEPP) tub

        private class DrgPeppColumn extends StringColumn<TCase> {

            DrgPeppColumn() {
                super("DRG/PEPP");
                setSortable(false);
                setMinWidth(20.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                if( getAutoResult(pValue) == null){
                    return "";
                }
                return getAutoResult(pValue).getGrpresCode();
            }
        }

        private class AdrgColumn extends StringColumn<TCase> {

            AdrgColumn() {
                super("ADRG");
                setSortable(false);
                setMinWidth(20.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                if(!pValue.getCsCaseTypeEn().equals(CaseTypeEn.DRG) 
                        || getAutoResult(pValue) == null){
                    return "";
                }
                String ret = getAutoResult(pValue).getGrpresCode();
                return ret == null || ret.length() < 4?"":ret.substring(0, 3);
            }
            
        }
        
        private class PartitionColumn extends StringColumn<TCase> {

            PartitionColumn() {
                super("DRG-Partition");
                setSortable(false);
                setMinWidth(40.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                if(!pValue.getCsCaseTypeEn().equals(CaseTypeEn.DRG) || getAutoResult(pValue) == null){
                    return "";
                }
                String ret = ((TCaseDrg)getAutoResult(pValue)).getDrgcPartitionEn().name();
                return ret == null ?"":ret;
            }
        }
        private class HtpColumn extends StringColumn<TCase> {

            HtpColumn() {
                super("oGVD");
                setSortable(false);
                setMinWidth(20.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                if(!pValue.getCsCaseTypeEn().equals(CaseTypeEn.DRG) 
                        || getAutoResult(pValue) == null){
                    return "";
                }
                return String.valueOf(((TCaseDrg)getAutoResult(pValue) ).getDrgcHtp());

            }
        }
        private class LtpColumn extends StringColumn<TCase> {

            LtpColumn() {
                super("uGVD");
                setSortable(false);
                setMinWidth(20.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                if(!pValue.getCsCaseTypeEn().equals(CaseTypeEn.DRG) 
                        || getAutoResult(pValue) == null){
                    return "";
                }
                return String.valueOf(((TCaseDrg)getAutoResult(pValue)).getDrgcLtp());
            }
        }
       private class MdcSkColumn extends StringColumn<TCase> {

            MdcSkColumn() {
                super("Mdc/Sk");
                setSortable(false);
                setMinWidth(20.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                if( getAutoResult(pValue) == null){
                    return "";
                }
                return getAutoResult(pValue).getGrpresGroup() == null?"":getAutoResult(pValue).getGrpresGroup().getIdStr();
            }
        }

        private class CaseTypeColumn extends StringColumn<TCase> {

            CaseTypeColumn() {
                super("Abrechnungsart");
                setSortable(false);
                setMinWidth(40.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCase pValue) {
                return pValue.getCsCaseTypeEn().getTranslation().getValue();
            }
        }

}




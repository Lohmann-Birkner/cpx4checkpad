/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.analyser.tabs;

import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.ruleviewer.util.HistoryFeeTableViewHelper;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javax.ejb.AsyncResult;

/**
 *
 * @author gerschmann
 */
public class HistoryFeeTableView extends AsyncTableView<HistoryFeeTableViewHelper>{

    private static final Logger LOG = Logger.getLogger(HistoryFeeTableView.class.getName());

    private ObjectProperty <HistoryFeeTableViewHelper>  lastSelectedProperty;
    private final HistoryFeeDataTab parent;    
    private Callback<TableColumn<HistoryFeeTableViewHelper, String>, TableCell<HistoryFeeTableViewHelper, String>> cellFactory
            = (TableColumn<HistoryFeeTableViewHelper, String> param) -> {
                return new TableCell<HistoryFeeTableViewHelper, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                if (item == null || empty) {

                    return;
                }
                Label label = new Label(item);
//                label.setMaxHeight(USE_COMPUTED_SIZE);
//                label.setPrefHeight(USE_COMPUTED_SIZE);
//                label.setFont(Font.font(getFont().getName(), 11));
                setGraphic(label);
            }
        };
    };
    
    public HistoryFeeTableView( HistoryFeeDataTab pParent){
       super();
        parent = pParent;
         getStyleClass().add("stay-selected-table-view");
         getStyleClass().add("table-view-small");
         setColumns();
         setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                // no action on mouse click
                return;
            }
        });
        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HistoryFeeTableViewHelper>(){
            @Override
            public void changed(ObservableValue<? extends HistoryFeeTableViewHelper> observable, HistoryFeeTableViewHelper oldValue, HistoryFeeTableViewHelper newValue) {
                Platform.runLater(() -> resetSelection(newValue));
            }            
        });
        
    }
    
    public void resetSelection(HistoryFeeTableViewHelper newValue){
        if(newValue == null ){
            if( getLastSelected() == null){
                return;
            }
        }

        if(newValue != null){
            if(getLastSelected() == null){
                getSelectionModel().clearSelection();
            }else{
                if(newValue.getCaseNr().equals(getLastSelected().getCaseNr())){
                    return;
                }else{
                    setSelection4Case(getLastSelected());
                }
            }
        }

    }
    @Override
    public Future<List<HistoryFeeTableViewHelper>> getFuture() {
         try {
            return new AsyncResult<>(parent.getHistoryFees());
        } catch (CpxIllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "Can not load data, reason: {0}", ex);
        }
        return new AsyncResult<>(new ArrayList<>());
    }
    
    
    public void setSelection4Case(HistoryFeeTableViewHelper pCase){
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

    private void setColumns() {

        CaseNumberColumn colCaseNumber = new CaseNumberColumn(); 
        getColumns().add(colCaseNumber);
        FeeKeyColumn colFeeKey = new FeeKeyColumn(); 
        getColumns().add(colFeeKey);
        FeeValueColumn colFeeValue = new FeeValueColumn(); 
        getColumns().add(colFeeValue);
        FeeCountColumn colFeeCount = new FeeCountColumn(); 
        getColumns().add(colFeeCount);
        
     }

    private ObjectProperty<HistoryFeeTableViewHelper> lastSelectedProperty() {
        if( lastSelectedProperty == null){
            lastSelectedProperty = new SimpleObjectProperty<>();
        };
        return lastSelectedProperty;
    }
    
    private void setLastSelected(HistoryFeeTableViewHelper pLast){
        lastSelectedProperty().set(pLast);
    }
    
    private HistoryFeeTableViewHelper getLastSelected(){
        return lastSelectedProperty().get();
    }
    
    private class CaseNumberColumn extends StringColumn<HistoryFeeTableViewHelper> {

        CaseNumberColumn() {
            super(Lang.getCaseNumber());
            setSortable(false);
            setMinWidth(100.0);
            setCellFactory(cellFactory);
        }

        @Override
        public String extractValue(HistoryFeeTableViewHelper pValue) {
            return pValue.getCaseNr();
        }
    }
    private class FeeKeyColumn extends StringColumn<HistoryFeeTableViewHelper> {

        FeeKeyColumn() {
            super("Entgeltschlüssel");
            setSortable(false);
            setMinWidth(100.0);
            setCellFactory(cellFactory);
        }

        @Override
        public String extractValue(HistoryFeeTableViewHelper pValue) {
            return pValue.getFeeKey();
        }
    }

    private class FeeValueColumn extends StringColumn<HistoryFeeTableViewHelper> {

        FeeValueColumn() {
            super("Eurowert");
            setSortable(false);
            setMinWidth(100.0);
            setCellFactory(cellFactory);
        }

        @Override
        public String extractValue(HistoryFeeTableViewHelper pValue) {
            return pValue.getFeeValue();
        }
    }
    
    private class FeeCountColumn extends StringColumn<HistoryFeeTableViewHelper> {//TableColumn<TCaseMergeMapping, String> {

        FeeCountColumn() {
            super("Entgeltanzahl");
            setSortable(false);
            setMinWidth(100.0);
            setCellFactory(cellFactory);
        }

        @Override
        public String extractValue(HistoryFeeTableViewHelper pValue) {
            return String.valueOf(pValue.getFeeCount());
        }

     }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.addcasewizard.model.table;

import de.lb.cpx.client.app.addcasewizard.fx.control.BillNode;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import de.lb.cpx.client.app.cm.fx.simulation.tables.CaseFeeTableView;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.textfield.CurrencyTextField;
import de.lb.cpx.client.core.model.fx.textfield.IntegerTextField;
import de.lb.cpx.shared.lang.AbstractLang;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author gerschmann
 */
public class EditableFeeTableView extends CaseFeeTableView{

    private static final Logger LOG = Logger.getLogger(EditableFeeTableView.class.getName());

    private Collection <TCaseFee> feeList;
    private StringProperty patientInsurance = new SimpleStringProperty();
    private final BillNode parentNode;
    
    public EditableFeeTableView(BillNode pParentNode){
        super();
        parentNode = pParentNode;
        setEditableColumns();
    }

    protected void setRowFactory(){
        setRowFactory(new Callback<TableView<TCaseFee>, TableRow<TCaseFee>>() {
            @Override
            public TableRow<TCaseFee> call(TableView<TCaseFee> param) {
                final TableRow<TCaseFee> row = new TableRow<>();
                final ContextMenu contextMenu = new CtrlContextMenu<>();

                MenuItem deleteRow = new MenuItem(Lang.getCatalogDialogRemove());
                deleteRow.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        TCaseFee fee = row.getItem();
                        if(fee.getFeecFeekey() != null){
                            remove(fee);
                        }
                    }
                });


                contextMenu.getItems().addAll(deleteRow);

                // Set context menu on row, but use a binding to make it only show for non-empty rows:
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );

                return row;
            }
        });
    }


    public void setItemSet(Collection<TCaseFee> pFeeList) {
        feeList = pFeeList == null ? new HashSet<TCaseFee>() : pFeeList;
        setItems(FXCollections.observableArrayList(feeList));
        refresh();
    }


    /**
     * @param item remove item from lists
     */
    public void remove(TCaseFee item) {
        getItems().remove(item);
        feeList.remove(item);
        refresh();
    }
    public void add(TCaseFee item) {
        getItems().add(item);
        setItemSet(getItems());
        feeList.add(item);
        refresh();
    }
    
    private void setEditableColumns(){
       feeFromColumn.setCellFactory(getDateCallback());
       feeToColumn.setCellFactory(getDateCallback());
       feeTypeColumn.setCellFactory(getStringCallback());
       insuranceColumn.setCellFactory(getStringCallback());
       feeCountColumn.setCellFactory(getStringCallback());
       feeDaysWithoutColumn.setCellFactory(getStringCallback());
       feeValueColumn.setCellFactory(getStringCallback());
//       feeValueSumColumn.setCellFactory(getStringCallback());
    }
    
 
    @Override
    public void refresh() {
        super.refresh();
        ObservableList<TCaseFee> itms = getItems();
        setItems(null);
        setItems(itms);
    }
   
    private  Callback<TableColumn<TCaseFee, String>, TableCell<TCaseFee, String>> getStringCallback(){
        return new Callback<TableColumn<TCaseFee, String>, TableCell<TCaseFee, String>>(){
            @Override
            public TableCell<TCaseFee, String> call(TableColumn<TCaseFee, String> param) {
                final String  header = param.getText();
                final TextField newFee = getTextField2Header(header);
                TableCell<TCaseFee, String> cell = new TableCell<TCaseFee, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                            setText("");
                            if (empty || getItem() == null) {

                            return;
                        }
                        setText((item));
                    }
                };     
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (cell.isEmpty()) {
                            return;
                        }
                        TCaseFee fee = cell.getTableRow().getItem();
                        if (fee != null) {
                            handleEditEvent(header, fee);
                        } else {
                            refresh();
                        }
                    }

                    private void handleEditEvent(String fieldHeader, TCaseFee fee){
                        if (!fieldHeader.equals(feeTypeColumn.getText()) && !fieldHeader.equals(insuranceColumn.getText())
                                && fee != null && (fee.getFeecFeekey()== null || fee.getFeecFeekey().isEmpty())) {
                            AlertDialog.createErrorDialog(Lang.getFeeCodeValidation(), ButtonType.OK).showAndWait();
                            return;
                        }
                        setValuesInTextField2Header(fieldHeader, newFee, fee);

                        newFee.prefHeightProperty().bind(cell.heightProperty().subtract(10));
                        setupValidation(fieldHeader, newFee);
                                //new TextField(getStringToHeader(fieldHeader, fee));
                        newFee.prefWidthProperty().bind(cell.widthProperty().subtract(10));
                        cell.setGraphic(newFee);
                        Platform.runLater(newFee::requestFocus);

                        newFee.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                            if (newFee.getText() != null  && newFee.getText().length() > getMaxLenght2Header(fieldHeader) && getMaxLenght2Header(fieldHeader) > 0 ) {
                                    newFee.setText(newFee.getText().substring(0, getMaxLenght2Header(fieldHeader)));
                                 
                            }
                        });
                        newFee.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                            if (!newValue) {
                                if (newFee.getText() != null && !trimToEmpty(newFee.getText()).isEmpty()) {
                                    
                                    if(newFee.getText().length() != getMaxLenght2Header(fieldHeader) && getMaxLenght2Header(fieldHeader) > 0 ){
//                                        AlertDialog.createErrorDialog(getInvalidLengthMessage2Header(fieldHeader), ButtonType.OK).showAndWait();
                                        newFee.selectAll();
                                        newFee.requestFocus();
                                    return;
                                    }
                                    refresh();
                                    handleStringValue2header(fieldHeader,newFee, fee);
                                } else {
                                    refresh();
                                }
                            }

                        });
                    }
                    
                    private void setupValidation(String fieldHeader, TextField txtField){
                    
                        if(fieldHeader != null && (fieldHeader.equals(insuranceColumn.getText()) ||fieldHeader.equals(feeTypeColumn.getText()) )){
                           
                            ValidationSupport  validSupport =   parentNode.getValidationSupport();  
                            if (!validSupport.getRegisteredControls().contains(txtField)) {
                                validSupport.registerValidator(txtField, (Control t, String u) -> {
                                    ValidationResult result = new ValidationResult();
                                    result.addErrorIf(t, getInvalidLengthMessage2Header(fieldHeader), u == null || u.isEmpty());

                                    return result;
                                });
                                Platform.runLater(() -> {
                                    validSupport.initInitialDecoration();
                                    validSupport.registerValidator(txtField, (Control t, String u) -> { 
                                        ValidationResult result = new ValidationResult();
                                        result.addErrorIf(t, getInvalidLengthMessage2Header(fieldHeader), u == null || u.isEmpty() || u.length() != getMaxLenght2Header(fieldHeader)); 

                                        return result;
                                    });

                                });
                            }
                        }
                    }

                    private String getInvalidLengthMessage2Header(String fieldHeader){
                        if(fieldHeader != null){
                            if(fieldHeader.equals(insuranceColumn.getText())){
                                return Lang.getBillValidationInvalidInsuranceKeyLength(); 
                            }
                            if(fieldHeader.equals(feeTypeColumn.getText())){
                                return Lang.getBillValidationInvalidFeeKeyLength();
                            }

                        }
                         return "";
                    
                   }
                    private int getMaxLenght2Header(String fieldHeader){
                        if(fieldHeader != null){
                            if(fieldHeader.equals(insuranceColumn.getText())){
                                return 9;
                            }
                            if(fieldHeader.equals(feeTypeColumn.getText())){
                                return 8;
                            }

                        }
                         return -1;
                    
                    }
                    private void setValuesInTextField2Header(String fieldHeader, TextField textField, TCaseFee fee){
                        if(fieldHeader != null){
                            
                            if(fieldHeader.equals(insuranceColumn.getText()) || fieldHeader.equals(feeTypeColumn.getText())){
                                textField.setText(getString2Header(fieldHeader, fee));
                            }
                            if(fieldHeader.equals(feeCountColumn.getText()) || fieldHeader.equals(feeDaysWithoutColumn.getText())){
                                ((IntegerTextField )textField).setValue(getInteger2Header(fieldHeader, fee));

                            }
                            if(fieldHeader.equals(feeValueColumn.getText()) //|| fieldHeader.equals(feeValueSumColumn)
                                    ){
                                ((CurrencyTextField )textField).setValue(getDouble2Header(fieldHeader, fee));

                            }
                           
                        }

                    }
                    
                });
               
                 return cell;       
            }
            
        };
    }
    
    private TextField getTextField2Header(String fieldHeader){
        if(fieldHeader != null){

            if(fieldHeader.equals(insuranceColumn.getText()) || fieldHeader.equals(feeTypeColumn.getText())){
                return new TextField();
            }
            if(fieldHeader.equals(feeCountColumn.getText()) || fieldHeader.equals(feeDaysWithoutColumn.getText())){
                return  new IntegerTextField();

            }
            if(fieldHeader.equals(feeValueColumn.getText()) //|| fieldHeader.equals(feeValueSumColumn)
                    ){
                return  new CurrencyTextField();

            }

        }
        return new TextField();
    }
    
    private void handleStringValue2header(String fieldHeader, TextField valueField, TCaseFee fee){
         if(fieldHeader != null && valueField != null){
            if(fieldHeader.equals(insuranceColumn.getText())){
                fee.setFeecInsurance(trimToEmpty(valueField.getText()));
            }
            if(fieldHeader.equals(feeTypeColumn.getText())){
                fee.setFeecFeekey(trimToEmpty(valueField.getText()));
                if(fee.getFeecInsurance() == null || fee.getFeecInsurance().isEmpty() ){
                    fee.setFeecInsurance(patientInsurance.get());
                }
            }
            if(fieldHeader.equals(feeValueColumn.getText())){
                fee.setFeecValue((Double)((CurrencyTextField)valueField).getValue());
            }
            if(fieldHeader.equals(feeCountColumn.getText()) ){
                fee.setFeecCount(((IntegerTextField)valueField).getValue().intValue());
            }
            if(fieldHeader.equals(feeDaysWithoutColumn.getText())){
                 fee.setFeecUnbilledDays(((IntegerTextField)valueField).getValue().intValue());
            }
            refresh();
            removeBlankRows(getItems());
            TCaseFee newFee = new TCaseFee();
            add(newFee);
        }
       
    }
    public void removeBlankRows(Collection<TCaseFee> objCollection) {
        Iterator<TCaseFee> it = new ArrayList<>(objCollection).iterator();
        while (it.hasNext()) {
            TCaseFee next = it.next();
            if (next.getFeecFeekey()== null && next.getFeecInsurance() == null ) {
                remove(next);
            }
        }
        refresh();
    }
    
    private String getString2Header(String fieldHeader, TCaseFee fee){
        if(fieldHeader != null){
            if(fieldHeader.equals(insuranceColumn.getText())){
                return fee.getFeecInsurance();
            }
            if(fieldHeader.equals(feeTypeColumn.getText())){
                return fee.getFeecFeekey();
            }
            
        }
        return null;
    }
    
    private int getInteger2Header(String fieldHeader, TCaseFee fee){
        if(fieldHeader != null){
            if(fieldHeader.equals(feeCountColumn.getText())){ 
                return fee.getFeecCount();
            } else if(fieldHeader.equals(feeDaysWithoutColumn.getText())){
                return fee.getFeecUnbilledDays();
            };
        }
        return 0;
    }
    
    private double getDouble2Header(String fieldHeader, TCaseFee fee){
        if(fieldHeader != null){
            if(fieldHeader.equals(feeValueColumn.getText())){
                return fee.getFeecValue();
            }
            else if(fieldHeader.equals(feeValueSumColumn)){
                return fee.getFeecValue() * fee.getFeecCount();
            }
        }
        return 0.0;
    }
    

    
    private Callback<TableColumn<TCaseFee, Date>, TableCell<TCaseFee, Date>> getDateCallback(){
        return  new Callback<TableColumn<TCaseFee, Date>, TableCell<TCaseFee, Date>>(){
            @Override
            public TableCell<TCaseFee, Date> call(TableColumn<TCaseFee, Date> param) {
                String header = param.getText();
                TableCell<TCaseFee, Date> cell = new TableCell<TCaseFee, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        setText("");
                        super.updateItem(item, empty);
                        if (empty || getItem() == null) {
                            return;
                        }
                        LOG.log(Level.INFO, "updateItem.Date: {0}", item == null?"null":Lang.toDate(item));
                        LOG.log(Level.INFO, "updateItem.Header: {0} {1}", new Object[]{header, getTableColumn().getText()});
                        setText(Lang.toDate(item));
                    }
                };

                //creates Popover layout to manipulate Date-Object
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (cell.isEmpty()) {
                            return;
                        }
                        TCaseFee fee = getFeeFromRow(cell);
                        if (fee != null && (fee.getFeecFeekey()== null || fee.getFeecFeekey().isEmpty())) {
                            AlertDialog.createErrorDialog(Lang.getFeeCodeValidation(), ButtonType.OK).showAndWait();
                            return;
                        }
                        HBox hBox = new HBox();
                        VBox vBox = new VBox();
                        Label label = new Label(" ");


                        LabeledDatePicker datePicker = new LabeledDatePicker(trimToEmpty(label.getText()));
                        hBox.getChildren().addAll(datePicker);
                        vBox.getChildren().addAll(hBox);
                        datePicker.getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
                        //                                    if (newValue instanceof LocalDate) {
                        LOG.log(Level.WARNING, "add  Date...");
                        cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
//                                       opsDateColumn.getTableView().refresh();

//                                        if (datePicker.getDate().after(department.getDepcAdmDate()) || datePicker.getDate().equals(department.getDepcAdmDate())) {
////                                            cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
//
//                                        } else {
////                                            cell.setItem(null);
////                                            AlertDialog.createErrorDialog("Geben Sie bitte güliges Datum ein", ButtonType.OK).showAndWait();
//                                        }
//                                    }
                    });
//                                .addListener(new ChangeListener<LocalDate>() {
//                            @Override
//                            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
//                                //if (newValue instanceof LocalDate) {
////                                validationSupport.registerValidator(EditableOpsTableView.this, val);
//
//                                cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
//                               
// LOG.log(Level.INFO, "datePicker.changed: {0} {1} {2}", new Object[]{header, cell.getTableColumn().getText(), Lang.toDate(java.sql.Date.valueOf(datePicker.getLocalDate()))});     
//                                updateDateFieldInEntity(header, java.sql.Date.valueOf(datePicker.getLocalDate()), fee);
//                                refresh();
////}
//                            }
//                        });

                        PopOver popover = new AutoFitPopOver();
                        popover.setHideOnEscape(true);
                        popover.setAutoHide(true);
                        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                        popover.setDetachable(false);
                        popover.setContentNode(vBox);
                        popover.show(cell);
                        popover.getContentNode().setOnKeyPressed(new EventHandler<KeyEvent>() {

                            @Override
                            public void handle(KeyEvent ke) {
                           if (ke.getCode().equals(KeyCode.ENTER)) {
                                if (datePicker.getLocalDate() != null) {
                                    cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
                                    final LocalDate date = AbstractLang.toLocalDate(datePicker.getControl().getEditor().getText());
                                    datePicker.setDate(java.sql.Date.valueOf(date));
                                    cell.setText(Lang.toDate(datePicker.getLocalDate()));
                                    updateDateFieldInEntity(header, java.sql.Date.valueOf(datePicker.getLocalDate()), fee);
                                    popover.hide(Duration.ZERO);
                                    

                                } else {
                                    cell.setText(Lang.toDate(getDate4FieldInEntity(header, fee)));
                                }
                                ke.consume();
                            }
                            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                                ke.consume();
                                refresh();
                            }
                        }
                        });
                        popover.ownerWindowProperty().get().setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                popover.hide(Duration.ZERO);
                            }
                        });
                    }

                });
                return cell;
            }

            private TCaseFee getFeeFromRow(TableCell<TCaseFee, Date> pCell) {
                if (pCell.getTableRow().getItem() != null) {
                    return pCell.getTableRow().getItem();
                }
                return null;
                
            }
            
            private void updateDateFieldInEntity(String header, Date date, TCaseFee fee){
                if(header != null){ 
                    if(header.equals(feeFromColumn.getText())){
                        fee.setFeecFrom(date);
                    }
                    if(header.equals(feeToColumn.getText())){
                        fee.setFeecTo(date);
                    }
                    
                }

            }
            private Date getDate4FieldInEntity(String header, TCaseFee fee){
                 if(header != null){ 
                    if(header.equals(feeFromColumn.getText())){
                        return fee.getFeecFrom();
                    }
                    if(header.equals(feeToColumn.getText())){
                        return fee.getFeecTo();
                    }
                    
                }
                 return null;
               
            }
        };
                
    }



    public void setPatientInsuranceCompany(String pPatientInsurance) {
        patientInsurance.set(pPatientInsurance);
    }
}

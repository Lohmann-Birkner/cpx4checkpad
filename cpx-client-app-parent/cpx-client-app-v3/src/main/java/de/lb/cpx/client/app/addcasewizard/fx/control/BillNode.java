/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.addcasewizard.fx.control;

import de.lb.cpx.client.app.addcasewizard.model.table.EditableFeeTableView;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.enums.BillTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author gerschmann
 */
public class BillNode extends VBox{

    private static final Logger LOG = Logger.getLogger(BillNode.class.getName());
    
   private LabeledTextField txtBillNumber;
   private LabeledComboBox<BillTypeEn> cbBillType;
   private LabeledDatePicker dtBillDate;
   private Label lblFees;
   private EditableFeeTableView tabFeeTable;
   private LabeledTextArea txtComment;
   private AddCaseHospitalCaseBillsFXMLController parentCtrl;
   private TCaseBill currentBill;
     private final ValidationSupport validSupport = new ValidationSupport(); 
   
   public BillNode(AddCaseHospitalCaseBillsFXMLController pParentCtrl, TCaseBill pBill){
       super();
       parentCtrl = pParentCtrl;
       currentBill = pBill;
////       validSupport = pParentCtrl.getValidationSupport();
        setMargin(this, new Insets(5));
        setUpCtrls();
        setUpLocalization();
//        setUpValues();
        setUpListeners();
        setUpBindingAndValidation();
        createLayout();
        if (pBill == null) {
            setDisableInBills(true);
        }
   }

    private void setUpCtrls() {
        txtBillNumber = new LabeledTextField ();
        cbBillType = new LabeledComboBox<>();
        dtBillDate = new LabeledDatePicker ();
        lblFees = new Label();
        tabFeeTable = new EditableFeeTableView (this);
        tabFeeTable.setPatientInsuranceCompany(parentCtrl.getParentController().getPatientInsurance());
        txtComment = new LabeledTextArea ();
        cbBillType.setItems(BillTypeEn.values());
        cbBillType.setConverter(new StringConverter<BillTypeEn>() {
            @Override
            public String toString(BillTypeEn object) {
                if (object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public BillTypeEn fromString(String string) {
                return BillTypeEn.valueTo(string);
            }
        });
        // set initial values
        cbBillType.getControl().getSelectionModel().select(BillTypeEn.finalBill);
        dtBillDate.getControl().setValue(Lang.toLocalDate(new Date()));
        currentBill.setBillcFrom(new Date());
    }

    private void setUpLocalization() {
        txtBillNumber.setTitle(Lang.getBillNumber());
        cbBillType.setTitle(Lang.getRulesTxtCritRechnungArtDis());
        dtBillDate.setTitle(Lang.getBillDate());
        lblFees.setText(Lang.getBillPositions());
        txtComment.setTitle(Lang.getComment());
        tabFeeTable.setPlaceholder(new Label(Lang.getFeePlaceholder()));
        txtComment.prefHeightProperty().bind(tabFeeTable.heightProperty().divide(3));
    }

    public void setUpValues() {
        if(currentBill != null){
            Set<TCaseFee> feeSet = currentBill.getCaseFees();
            if(feeSet != null && !feeSet.isEmpty()){
                parentCtrl.setNewFeeData(feeSet);
            }else{
                newFees();
            }
        }else{
            txtBillNumber.setText("");
            cbBillType.getControl().getSelectionModel().clearSelection();
            dtBillDate.setTitle(Lang.getBillDate());
            lblFees.setText(Lang.getBillPositions());
            txtComment.setTitle(Lang.getComment());
            tabFeeTable.setPlaceholder(new Label(Lang.getFeePlaceholder()));
            newFees();
        }
        
    }

    private void setUpListeners() {
        cbBillType.getControl().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends BillTypeEn> observable, BillTypeEn oldValue, BillTypeEn newValue) -> {
            if (newValue != null) {
                if (currentBill != null) {
                    currentBill.setBillcTypeEn(newValue);  
                    currentBill.setBillcType(newValue.getId());
                }
            }
        });

        dtBillDate.getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
                        //                                    if (newValue instanceof LocalDate) {
            LOG.log(Level.INFO, "dtBillDate: set  Date...");
            currentBill.setBillcFrom(java.sql.Date.valueOf(newValue));
        });
        
        txtBillNumber.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(currentBill != null){
                    if(txtBillNumber.getText().length() > 20){
                        txtBillNumber.getControl().selectAll();
                        txtBillNumber.getControl().requestFocus();
                        return;
                    }
                    currentBill.setBillcNumber(txtBillNumber.getText());
                    txtBillNumber.getControl().positionCaret(0);
                    parentCtrl.getTvCaseBillNodes().refresh();
                    tabFeeTable.refresh();
                }
            }
        });

        txtComment.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(currentBill != null){
                    currentBill.setBillComment(txtComment.getText());
                    tabFeeTable.refresh();
                }
            }
        });
    }
    
    public ValidationSupport getValidationSupport(){
        return validSupport;
    }

    private void setUpBindingAndValidation() {
        validSupport.registerValidator(txtBillNumber.getControl(), (Control t, String u) -> {
            ValidationResult result = new ValidationResult();
            result.addErrorIf(t, Lang.getValidationErrorInvalidBill(), u == null || u.isEmpty());

            return result;
        });
        Platform.runLater(() -> {
            validSupport.initInitialDecoration();
            validSupport.registerValidator(txtBillNumber.getControl(), (Control t, String u) -> { 
                ValidationResult result = new ValidationResult();
                result.addErrorIf(t, Lang.getBillValidationInvalidBillNumberLength(), u == null || u.isEmpty() || u.length() > 20); 

                return result;
            });
//            validSupport.registerValidator(tabFeeTable, (Control t, ObservableList<TCaseFee> u) -> { 
//                ValidationResult res = new ValidationResult();
//                if (currentBill != null) {
//                    res.addErrorIf(t, Lang.getBillValidationNoFees(), currentBill.getCaseFees() == null || currentBill.getCaseFees().isEmpty());
//                    Set<TCaseFee > fees = currentBill.getCaseFees();
//                    boolean invalidFee = false;
//                    for(TCaseFee fee:fees){
//                        if(fee.getFeecFeekey() == null || fee.getFeecFeekey().length() != 8){
//                            invalidFee = true;
//                            break;
//                        }
//                    }
//                    res.addErrorIf(t, Lang.getBillValidationInvalidFeeKey(), invalidFee);
//                     parentCtrl.getTvCaseBillNodes().refresh();
//                }
//                return res;
//            });
        });
    }
    /**
     * @return indicator if validtion is invalid
     */
    public boolean isInvalid() {
        return validSupport.isInvalid();
    }
    private void setDisableInBills(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void newFees() {
       List<TCaseFee>tmpFees = new ArrayList<>();
       TCaseFee fee = new TCaseFee(parentCtrl.getParentController().getCurrentUserId());
//       fee.setFeecInsurance(parentCtrl.getParentController().getPatientInsurance());
       tmpFees.add(fee);

        Set<TCaseFee> feeSet = new HashSet<>();
        feeSet.addAll(tabFeeTable.getItems());
        feeSet.addAll(tmpFees);
//        tabFeeTable.setItemSet(feeSet);
        currentBill.setCaseFees(feeSet);
        parentCtrl.setNewFeeData(feeSet);       
    }

    public EditableFeeTableView getFeeTableView() {
        parentCtrl.refresh();
        return tabFeeTable;
    }
    
    private void createLayout(){
        setSpacing(12.0);
        setFillWidth(true);

        HBox firstLine = new HBox(this.txtBillNumber, this.cbBillType, this.dtBillDate);
        firstLine.setSpacing(5.0);
        HBox.setHgrow(txtBillNumber, Priority.ALWAYS);
        cbBillType.setPrefWidth(200);
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(this.tabFeeTable);
        mainPane.setBottom(this.txtComment);
        getChildren().addAll(firstLine, this.lblFees, mainPane);
    }


    public TCaseBill getBill() {
        return currentBill;
    }

    public boolean isEmpty() {
        return tabFeeTable.getItems().isEmpty();
    }


}

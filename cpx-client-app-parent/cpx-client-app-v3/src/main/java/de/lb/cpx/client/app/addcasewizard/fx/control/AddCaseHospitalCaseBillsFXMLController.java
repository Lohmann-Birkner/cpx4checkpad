/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.addcasewizard.fx.control;

import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTreeView;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author gerschmann
 */
public class AddCaseHospitalCaseBillsFXMLController extends Controller<CpxScene>{
    @FXML
    private Button buttonAddBill;
    @FXML
    private Button buttonRemoveBill;
    
    @FXML
    private ScrollPane  spBills;
    @FXML
    private VBox vbBillDetails;
    
    private AddCaseServiceFacade serviceFacade;
    
    
    
    @FXML
    private LabeledTreeView<BillNode> tvCaseBillNodes;
    
    private ObjectProperty<TCaseBill> currentBill = new SimpleObjectProperty<>();
    
    private ObjectProperty<AddCaseHospitalCaseDataFXMLController> parentControlProperty;


    public void init(AddCaseServiceFacade pFacade){
        serviceFacade = pFacade;

    }

    public LabeledTreeView<BillNode> getTvCaseBillNodes() {
        return tvCaseBillNodes;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpLanguage();
        setupTreeView();
        setupBillNodeCtrl();
    }

    private void setUpLanguage() {
        tvCaseBillNodes.setTitle(Lang.getAddCaseBills());
        buttonAddBill.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
        buttonAddBill.setText("");
        buttonAddBill.setTooltip(new Tooltip(Lang.getAddCaseAddBill())); 
        buttonRemoveBill.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.MINUS));
        buttonRemoveBill.setText("");
        buttonRemoveBill.setTooltip(new Tooltip(Lang.getAddCaseRemoveBill())); 
    }
    
    
   private final ListChangeListener<TreeItem<BillNode>> treeSelectionListener = c -> {
        if (!c.getList().isEmpty()) {
            TreeItem<BillNode> selected = c.getList().get(0);
            showBillDetails(selected.getValue());
            selected.getValue();
            selected.getValue();
        }
   };
           
    private final ChangeListener<TCaseBill> billListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            for (TreeItem<BillNode> item : tvCaseBillNodes.getTreeView().getRoot().getChildren()) {
                if (item.getValue().getBill().equals(newValue)) {
                    showBillDetails(item.getValue());
                }
            }
        } else {
//            try {
//            showBillDetails(new BillNode(this, null));
//            } catch (CpxIllegalArgumentException ex) {
//                LOG.log(Level.SEVERE, "Cannot show department, an error occured", ex);
//            }
        }
    };
           
    private void setupTreeView() {
        TreeItem<BillNode> root = new TreeItem<>();
        tvCaseBillNodes.prefHeightProperty().bind(spBills.heightProperty().subtract(5));
        tvCaseBillNodes.getTreeView().setRoot(root);
        tvCaseBillNodes.getTreeView().setShowRoot(false);

        tvCaseBillNodes.getTreeView().setCellFactory((TreeView<BillNode> param)->{ 
            TreeCell<BillNode> cell = new TreeCell<BillNode>(){
               private final Label label = new Label();

                @Override
                protected void updateItem(BillNode myObj, boolean empty) {
                    super.updateItem(myObj, empty);
                    if (empty || myObj == null) {
                        setText(null);
                        setGraphic(null);
                        //reset label
                        label.setText(null);
                        return;
                    }
                    setGraphic(label);
                    if (myObj.getBill().getBillcNumber() != null) { 

                        label.setText("");
                        label.setText(myObj.getBill().getBillcNumber()); 

                    } else {
                        label.setText("");
                        label.setText("####");
                    }
                    if (!getParentController().getValidationSupport().getRegisteredControls().contains(label)) {
                        getParentController().getValidationSupport().registerValidator(label, (Control t, String u) -> {
                            ValidationResult res = new ValidationResult();
                            if (u != null) {
                                res.addErrorIf(t, "Fehler bei Rechnungsnummer" //Lang.getValidationErrorInvalidBill()
                                        , myObj.isInvalid());
                            }
                            return res;
                        });
                    }
                }
            };
         cell.setPadding(new Insets(8, 0, 8, 0));     
        
        return cell;
    });
    tvCaseBillNodes.getTreeView().getSelectionModel().getSelectedItems().addListener(treeSelectionListener);    
    }

    private void setupBillNodeCtrl() {
        currentBill.addListener(billListener); 
    }

    private void showBillDetails(BillNode value) {
        vbBillDetails.getChildren().clear();
        vbBillDetails.getChildren().add(value);
        value.setUpValues(); 
    }

    @FXML
    public void addBill(ActionEvent event) {
        addBill();
    }
    public void addBill() {
        BillNode billNode = new BillNode(this, new TCaseBill(getParentController().getCurrentUserId()));
        TreeItem<BillNode> newBill = new TreeItem<>(billNode);
        tvCaseBillNodes.getTreeView().getRoot().getChildren().add(newBill);
        tvCaseBillNodes.getTreeView().getSelectionModel().select(newBill);
        billNode.setUpValues();
    }
    
    @FXML
    private void removeBill(ActionEvent event) {
        TreeItem<BillNode> selected = tvCaseBillNodes.getTreeView().getSelectionModel().getSelectedItem();
        TreeItem<BillNode> newlySelectedItem = tvCaseBillNodes.removeAndSelectPrevious(selected);
        currentBill.set(newlySelectedItem.getValue().getBill());
        if (serviceFacade.getCurrentCase() != null && serviceFacade.getCurrentCase().getCurrentExtern() != null) {
            if (serviceFacade.getCurrentCase().getCurrentExtern().getCaseDepartments().contains(selected.getValue().getBill())) {
                serviceFacade.getCurrentCase().getCurrentExtern().getCaseDepartments().remove(selected.getValue().getBill());
            }
        }
        LOG.log(Level.INFO, "Bill " + selected.getValue().getBill() + "is deleted..");
        if(tvCaseBillNodes.getTreeView().getRoot().getChildren().isEmpty()){
            vbBillDetails.getChildren().clear();
        }
    }
 
    public void setParentController(AddCaseHospitalCaseDataFXMLController pController) {
        getParentControllerProperty().set(pController);
    }
    
    public AddCaseHospitalCaseDataFXMLController getParentController(){
        return getParentControllerProperty().get();
    }

    private ObjectProperty<AddCaseHospitalCaseDataFXMLController> getParentControllerProperty() {
        if(parentControlProperty == null){
            parentControlProperty = new SimpleObjectProperty<>();
        }
        return parentControlProperty;
    }

    protected void setNewFeeData(Set<TCaseFee> feeSet) {
        TreeItem<BillNode> selected = this.tvCaseBillNodes.getTreeView().getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue().getFeeTableView() != null) { 
            selected.getValue().getFeeTableView().setItemSet(feeSet);
        }
    }
   
    public void setDataIfPossible() {
        if (tvCaseBillNodes.getTreeView().getSelectionModel().getSelectedItem() != null) {
            currentBill.setValue(tvCaseBillNodes.getTreeView().getSelectionModel().getSelectedItem().getValue().getBill());
        }
        if (!tvCaseBillNodes.getTreeView().getRoot().getChildren().isEmpty() && serviceFacade.getCurrentCase() != null) {
            tvCaseBillNodes.getTreeView().getRoot().getChildren().clear();
            createAndAddNewTreeItems(serviceFacade.getCurrentCase().getCurrentExtern().getCaseBills());
            if (currentBill != null) {
                tvCaseBillNodes.getTreeView().getSelectionModel().selectFirst();
                currentBill.setValue(tvCaseBillNodes.getTreeView().getSelectionModel().getSelectedItem().getValue().getBill());
            }
        }


    }
    private void createAndAddNewTreeItems(Set<TCaseBill> pBills) {
        List<TreeItem<BillNode>> items = new ArrayList<>();
        for (TCaseBill bill : pBills) {
            items.add(new TreeItem<>(new BillNode(this, bill)));
        }
        tvCaseBillNodes.getTreeView().getRoot().getChildren().addAll(items);
    }
    
    public void cacheBillData(TCase currentCase){

        Set<TCaseBill> allBills = new HashSet<>();

        for(TreeItem<BillNode> treeItem: tvCaseBillNodes.getTreeView().getRoot().getChildren()){
            BillNode node = treeItem.getValue();

            if(!node.isEmpty()){
                TCaseBill bill = node.getBill();
                allBills.add(bill);
                bill.setCaseDetails(currentCase.getCurrentExtern());
                Set<TCaseFee> fees = bill.getCaseFees();
                if(fees != null){
                    for(TCaseFee fee:fees){
                        fee.setCaseBill(bill);
                        fee.setCaseDetails(currentCase.getCurrentExtern());
                        fee.setCreationUser(bill.getCreationUser());
                    }
                }
            }
        }
        currentCase.getCurrentExtern().setCaseBills(allBills);
    }

    ValidationSupport getValidationSupport() {
        return getParentController().getValidationSupport();
    }

}

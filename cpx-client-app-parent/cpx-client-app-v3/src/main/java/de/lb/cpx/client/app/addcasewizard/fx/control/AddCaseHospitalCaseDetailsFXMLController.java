/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.addcasewizard.fx.control;

import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.easycoder.EasyCoderDialog;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTreeView;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.stage.Modality;
import javafx.stage.Window;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author gerschmann
 */
public class AddCaseHospitalCaseDetailsFXMLController extends Controller<CpxScene>{
    
    @FXML
    private Button buttonAddDepartment;
    @FXML
    private Button buttonRemoveDepartment;
    @FXML
    private ScrollPane spDepartments;
    @FXML
    private LabeledTreeView<DepartmentNode> tvDepartments;
    @FXML
    private VBox vbDepartmentDetails;
    
//    private ValidationSupport validationSupport;
    private AddCaseServiceFacade serviceFacade;
    
    private ObjectProperty<AddCaseHospitalCaseDataFXMLController> parentControlProperty;
    
     private final ObjectProperty<TCaseDepartment> currentDepartment = new SimpleObjectProperty<>();   
    public void init(AddCaseServiceFacade pFacade){
        serviceFacade = pFacade;
//            addDepartment();
//            setDataIfPossible();
//        validationSupport = 
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setUpLanguage();
//        validationSupport = new ValidationSupport();
        setUpDepartmentTreeView();
        setUpDepartmentCtrl();
 
    }

    
    private void setUpLanguage(){
        tvDepartments.setTitle(Lang.getDepartments());
        buttonAddDepartment.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
        buttonAddDepartment.setText("");
        buttonAddDepartment.setTooltip(new Tooltip(Lang.getAddCaseAddDepartment()));
        buttonRemoveDepartment.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.MINUS));
        buttonRemoveDepartment.setText("");
        buttonRemoveDepartment.setTooltip(new Tooltip(Lang.getAddCaseRemoveDepartment()));
        
    }
 
    protected LabeledTreeView<DepartmentNode> getTvDepartments() {
        return tvDepartments;
    }

    private final ListChangeListener<TreeItem<DepartmentNode>> treeDisableListener = c -> {
        if (c.getList().size() <= 1) {
            buttonRemoveDepartment.setDisable(true);
        } else {
            buttonRemoveDepartment.setDisable(false);
        }
        tvDepartments.refresh();
    };

    private final ListChangeListener<TreeItem<DepartmentNode>> treeSelectionListener = c -> {
        if (!c.getList().isEmpty()) {
            TreeItem<DepartmentNode> selected = c.getList().get(0);
            showDepartmentDetails(selected.getValue());
            selected.getValue();
            selected.getValue();
        }
    };


    private final ChangeListener<TCaseDepartment> departmentListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            for (TreeItem<DepartmentNode> item : tvDepartments.getTreeView().getRoot().getChildren()) {
                if (item.getValue().getDepartment().equals(newValue)) {
                    showDepartmentDetails(item.getValue());
                }
            }
        } else {
//            try {
            showDepartmentDetails(new DepartmentNode(this, null));
//            } catch (CpxIllegalArgumentException ex) {
//                LOG.log(Level.SEVERE, "Cannot show department, an error occured", ex);
//            }
        }
    };
    
    private void setUpDepartmentTreeView() {
        TreeItem<DepartmentNode> root = new TreeItem<>();
        tvDepartments.prefHeightProperty().bind(spDepartments.heightProperty().subtract(5));
        tvDepartments.getTreeView().setRoot(root);
        tvDepartments.getTreeView().setShowRoot(false);
        tvDepartments.getTreeView().getSelectionModel().getSelectedItems().addListener(treeSelectionListener);
//        tvDepartments.getTreeView().setSkin(new TreeViewSkin<DepartmentNode>(tvDepartments.getTreeView()){
//            @Override
//            public TreeCell<DepartmentNode> createCell() {
//                TreeCell<DepartmentNode> cell = new TreeCell<DepartmentNode>();
//                cell.setPadding(new Insets(8, 0, 8, 0));
//                cell.updateTreeView(getSkinnable());
//                return cell; 
////                return super.createCell();
//            }
//            
//        });
        tvDepartments.getTreeView().setCellFactory((TreeView<DepartmentNode> param) -> {
            TreeCell<DepartmentNode> cell = new TreeCell<DepartmentNode>() {
                private final Label label = new Label();

                @Override
                protected void updateItem(DepartmentNode myObj, boolean empty) {
                    super.updateItem(myObj, empty);
                    if (empty || myObj == null) {
                        setText(null);
                        setGraphic(null);
                        //reset label
                        label.setText(null);
                        return;
                    }
                    setGraphic(label);
                    if (myObj.getDepartment().getDepKey301() != null) {
                        label.setText("");
                        label.setText(myObj.getDepartment().getDepKey301());

                    } else {
                        label.setText("");
                        label.setText("####");
                    }
                    if (!getParentController().getValidationSupport().getRegisteredControls().contains(label)) {
                        getParentController().getValidationSupport().registerValidator(label, (Control t, String u) -> {
                            ValidationResult res = new ValidationResult();
                            if (u != null) {
                                res.addErrorIf(t, Lang.getDepartmentValidationError(), myObj.isInvalid());
                            }
                            return res;
                        });
                    }
                }
            };
            //
//                //add padding to show validation icon
            cell.setPadding(new Insets(8, 0, 8, 0));
////                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
////                    @Override
////                    public void handle(MouseEvent event) {
////                        if (event.getClickCount() >= 1) {
////                            if (cell.getItem() != null) {
////                                showDepartmentDetails(cell.getItem());
////
////                                cell.getItem().newIcd();
////                                cell.getItem().newOps();
////
////                            }
////                        }
////                    }
//                });
            return cell;
        }//
        );
        tvDepartments.getTreeView().getRoot().getChildren().addListener(treeDisableListener);
    }

    public ValidationSupport getValidationSupport(){
        return getParentController().getValidationSupport();
    }
    
    protected void showDepartmentDetails(DepartmentNode pDetails) {
        vbDepartmentDetails.getChildren().clear();
        vbDepartmentDetails.getChildren().add(pDetails);

        pDetails.setUpValues();
    }
    @FXML
    private void removeDepartment(ActionEvent event) {
        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
        TreeItem<DepartmentNode> newlySelectedItem = tvDepartments.removeAndSelectPrevious(selected);
        currentDepartment.set(newlySelectedItem.getValue().getDepartment());
        if (serviceFacade.getCurrentCase() != null && serviceFacade.getCurrentCase().getCurrentExtern() != null) {
            if (serviceFacade.getCurrentCase().getCurrentExtern().getCaseDepartments().contains(selected.getValue().getDepartment())) {
                serviceFacade.getCurrentCase().getCurrentExtern().getCaseDepartments().remove(selected.getValue().getDepartment());
            }
        }
        LOG.log(Level.INFO, "Department " + selected.getValue().getDepartment() + "is deleted..");
    }
    @FXML
    private void addDepartment(ActionEvent event) {
        addDepartment();
    }
    
//    @Override
//    public void refresh() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//
//    @Override
//    public CpxScene getScene() {
//        return super.getScene();
//    
//    }
    
    public void addDepartment() {
        DepartmentNode department = new DepartmentNode(this, new TCaseDepartment(getParentController().getCurrentUserId()));
        TreeItem<DepartmentNode> newItem = createAndAddNewTreeItem(department);
        department.setUpValues();
        currentDepartment.set(department.getDepartment());
        tvDepartments.getTreeView().getSelectionModel().select(newItem);
        LOG.log(Level.INFO, "Department is added..");
//        department.newIcd();
//        department.newOps();
    }

    public void setDataIfPossible() {
        if (tvDepartments.getTreeView().getSelectionModel().getSelectedItem() != null) {
            currentDepartment.setValue(tvDepartments.getTreeView().getSelectionModel().getSelectedItem().getValue().getDepartment());
        }
        if (!tvDepartments.getTreeView().getRoot().getChildren().isEmpty() && serviceFacade.getCurrentCase() != null) {
            tvDepartments.getTreeView().getRoot().getChildren().clear();
            createAndAddNewTreeItems(serviceFacade.getCurrentCase().getCurrentExtern().getCaseDepartments());
            if (currentDepartment != null) {
                tvDepartments.getTreeView().getSelectionModel().selectFirst();
                currentDepartment.setValue(tvDepartments.getTreeView().getSelectionModel().getSelectedItem().getValue().getDepartment());
            }
        }
        getParentController().setAndComputeGebInGui();

    }

    private TreeItem<DepartmentNode> createAndAddNewTreeItem(DepartmentNode department) {
        TreeItem<DepartmentNode> newItem = new TreeItem<>(department);
        tvDepartments.getTreeView().getRoot().getChildren().add(newItem);
        return newItem;
    }

    private void createAndAddNewTreeItems(Set<TCaseDepartment> departments) {
        List<TreeItem<DepartmentNode>> items = new ArrayList<>();
        for (TCaseDepartment department : departments) {
            items.add(new TreeItem<>(new DepartmentNode(this, department)));
        }
        tvDepartments.getTreeView().getRoot().getChildren().addAll(items);
    }

    protected void setNewIcdData(Set<TCaseIcd> caseIcds) {

        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue().getIcdTableView() != null) {
            selected.getValue().getIcdTableView().setItemSet(caseIcds);
        }
    }

    protected void removeBlankIcds(Set<TCaseIcd> caseIcds) {
        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue().getIcdTableView() != null) {
            selected.getValue().getIcdTableView().removeBlankRows(caseIcds);
        }
    }

    protected void setNewOpsData(Set<TCaseOps> caseOps) {
        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue().getOpsTableView() != null) {
            selected.getValue().getOpsTableView().setItemSet(caseOps);
        }
    }

    protected void removeBlankOpses(Set<TCaseOps> caseOps) {
        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue().getOpsTableView() != null) {
            selected.getValue().getOpsTableView().removeBlankRows(caseOps);
        }
    }

    private void setUpDepartmentCtrl() {

        currentDepartment.addListener(departmentListener);

    }
    
    protected EasyCoderDialog initEasyCoder() {
        final Date admissionDate = getParentController().getDpAdmissionDate().getDate();

        if (admissionDate == null) {
            AlertDialog alert = AlertDialog.createErrorDialog("Bitte geben Sie zuerst ein Aufnahmedatum an!", getScene().getWindow());
            alert.initOwner(getScene().getOwner());
            alert.show();
            return null;
        }
        EasyCoderDialog easyCoder = new EasyCoderDialog(getScene().getWindow(), Modality.APPLICATION_MODAL, "ICD-, OPS-Bearbeitung", admissionDate);
        easyCoder.initOwner(getScene().getOwner());
        return easyCoder;
    }

    
   public void cacheDepartmentData(TCase currentCase) {
        //clear old results, to ensure that only newly cloned Departments are in the list
        int hmv = 0;
//        int tob = 0;
        for (TreeItem<DepartmentNode> treeItem : tvDepartments.getTreeView().getRoot().getChildren()) {
            TCaseDepartment value = treeItem.getValue().getDepartment();
            //CPX-1090 avoid NPE in getSortedDepartments
            if (value.getDepcAdmDate() == null) {
                value.setDepcAdmDate(getParentController().getDpAdmissionDate().getDate());
            }
            //CPX-1097 
            for (TCaseOps op : value.getCaseOpses()) {
                if (op.getOpscDatum() == null) {
                    op.setOpscDatum(value.getDepcAdmDate());
                }
            }
            removeBlankIcds(value.getCaseIcds());
            removeBlankOpses(value.getCaseOpses());
            if (!currentCase.getCurrentExtern().getCaseDepartments().contains(value)) {
                value.setCaseDetails(currentCase.getCurrentExtern());
                currentCase.getCurrentExtern().getCaseDepartments().add(value);
            }
            hmv = hmv + value.getDepcHmv();
            //RSH 06.11.2017 CPX-628
            // Tage in  Pseudo Department as TOB "Tage ohne Berechnung" 
            // AGe nicht nötig - die werden beim Groupen berechnet und eingetragen
//            if (value.isPseudo()) {
//                int daysBetween = daysBetween(value.getDepcAdmDate(), value.getDepcDisDate());
//                if(currentCase.getCsCaseTypeEn().equals(CaseTypeEn.PEPP)){
//                    daysBetween-= 2; // not 100% sure, but will be corrected by grouping
//                }else{
//                    daysBetween -= 1;
//                }
//                if(daysBetween < 0){
//                    daysBetween = 0;
//                }
//                tob = daysBetween + tob;
//                LOG.log(Level.INFO, "Compute TOB ...");
//            }
        }
//        currentCase.getCurrentExtern().setCsdLeave(tob);
        currentCase.getCurrentExtern().setCsdHmv(hmv);
    }
   
       public Window getWindow() {
        CpxScene s = getScene();
        if (s == null) {
            return null;
        }
        return s.getOwner();
    }


    public AddCaseServiceFacade getServiceFacade() {
        return serviceFacade;
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

}

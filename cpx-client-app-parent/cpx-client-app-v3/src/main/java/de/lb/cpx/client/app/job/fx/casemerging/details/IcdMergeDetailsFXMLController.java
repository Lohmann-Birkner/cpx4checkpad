/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.job.fx.casemerging.details;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.details.CmIcdDetailsFXMLController;
import de.lb.cpx.client.app.cm.fx.simulation.tables.IcdTablePane;
import de.lb.cpx.client.app.cm.fx.simulation.tables.OpsTablePane;
import de.lb.cpx.client.app.job.fx.casemerging.model.CaseMergeContent;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.easycoder.EasyCoderDialog;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartment;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparablePaneSkin;
import de.lb.cpx.client.core.model.fx.comparablepane.TableCompPane;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * FXML Controller class TODO: Refactor and merge with cmIcdDetailFXMLController
 * and scene
 *
 * @author wilde
 */
public class IcdMergeDetailsFXMLController extends Controller<IcdMergeDetailsScreen> {

//    private ObservableList<CaseMergeContent> versionList = FXCollections.observableArrayList();
    @FXML
    private HBox hBoxAddDiagnosisMenu;
    @FXML
    private Button btnAddDiagnosis;
    @FXML
    private SplitPane spContent;
    @FXML
    private IcdTablePane<CaseMergeContent> spIcd;
    @FXML
    private OpsTablePane<CaseMergeContent> spOps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        btnAddDiagnosis.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
        spIcd.setDeleteStrategy(TableCompPane.DeleteStrategy.ATLEAST_ONE_EDITABLE);
        spOps.setDeleteStrategy(TableCompPane.DeleteStrategy.ATLEAST_ONE_EDITABLE);
    }

    @FXML
    private void onAddDiagnosis(ActionEvent event) {
        //add a new icd and or ops to the currently selected local case details
        //starts new easycoder dialog (catalogdialog) and add stores the results 

        final Date admissionDate = getScene().getMergedVersion().getCsdAdmissionDate();
        EasyCoderDialog dialog = new EasyCoderDialog(MainApp.getWindow(), admissionDate);
        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (t.equals(ButtonType.OK)) {
                    // get the result list (user selection in the easy coder)
                    List<TCaseIcd> listOfAddedIcd = dialog.getSelectedTCaseIcd();
                    List<TCaseOps> listOfAddedOps = dialog.getSelectedTCaseOps();
                    dialog.closeEasyCoder();

                    if (listOfAddedIcd.isEmpty() && listOfAddedOps.isEmpty()) {
                        return;
                    }
                    //create department dialog, that the user sould choose a department for the selected icd and ops
                    DepartmentDialog dialog = new DepartmentDialog(listOfAddedIcd, listOfAddedOps);
                    dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (t.equals(ButtonType.OK)) {

                                //reload if not empty - loads new list maybe costs alot needs to check if performace is an issue 
                                if (!listOfAddedIcd.isEmpty()) {
                                    //TODO TEST ONLY
//                                    getScene().getFacade().getIcdsForVersion(getScene().getMergedVersion()).addAll(listOfAddedIcd);
//                                    getScene().getMergedVersion()
                                    spIcd.reload();
                                }
                                //reload if not empty - loads new list maybe costs alot needs to check if performace is an issue 
                                if (!listOfAddedOps.isEmpty()) {
                                    //TODO TEST ONLY
//                                    getScene().getFacade().getOpsesForVersion(getScene().getMergedVersion()).addAll(listOfAddedOps);
                                    spOps.reload();
                                }
                                //group all local versions to reflect changes
                                for (CaseMergeContent version : getScene().getListOfVersions()) {
                                    if (version.isEditable()) {
                                        version.performGroup();
                                    }
                                }
                            }

                        }
                    });

                } else if (t.equals(ButtonType.CANCEL)) {
                    dialog.closeEasyCoder();
                }
            }
        });
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene(); //To change body of generated methods, choose Tools | Templates.
        //init facade in the panes 
//        spIcd.initServiceFacade(pFacade);
        hBoxAddDiagnosisMenu.prefHeightProperty().bind(spIcd.prefHeaderHeightProperty());
        spIcd.setShowMenu(true);
        spIcd.setReloadCallback(new Callback<List<Long>, List<IcdOverviewDTO>>() {
            @Override
            public List<IcdOverviewDTO> call(List<Long> param) {
                return getScene().getFacade().getAllIcdCodes(getScene().getMergedVersion(), param);
            }
        });
        ((ComparablePaneSkin) spIcd.getSkin()).setTableToVersionListener(new MergeVersionListener<>(spIcd));
        
        spOps.setCaseType(getScene().getFacade().getGrpresType());
        spOps.setReloadCallback(new Callback<List<Long>, List<OpsOverviewDTO>>() {
            @Override
            public List<OpsOverviewDTO> call(List<Long> param) {
                return getScene().getFacade().getAllOpsCodes(getScene().getMergedVersion(), param);
            }
        });
      ((ComparablePaneSkin) spOps.getSkin()).setTableToVersionListener(new MergeVersionListener<>(spOps));
//        //bind bars to values to scroll both at the same time
        getScene().bindHScrollBar(spOps.getHScrollBar());
        getScene().bindHScrollBar(spIcd.getHScrollBar());

        Bindings.bindContent(spIcd.getVersionList(), getScene().getListOfVersions());
        Bindings.bindContent(spOps.getVersionList(), getScene().getListOfVersions());
        spIcd.setVersionContentWidth(spIcd.getFullCollWidth());
        spOps.setVersionContentWidth(spIcd.getFullCollWidth());
        reload();
    }

    @Override
    public void reload() {
        spIcd.reload();
        spOps.reload();
    }

    
    private class DepartmentDialog extends AlertDialog {

        // list of selection elements for all departments
        private final List<LabeledComboBox<TCaseDepartment>> departments = new ArrayList<>();
        //indicator if the user has atleast selected some department
//        BooleanProperty selected = new SimpleBooleanProperty(true);
        private ValidationSupport validation = new ValidationSupport();
        private final Button ok;

        /**
         * construct a new Departmentdialog
         *
         * @param pIcdList list of icds the user wants to add
         * @param pOpsList list of ops the user wants to add
         */
        DepartmentDialog(List<TCaseIcd> pIcdList, List<TCaseOps> pOpsList) {
            super(Alert.AlertType.WARNING, Lang.getWarning(), Lang.getCaseManagementWarningSelectDepartment(), (Throwable) null, ButtonType.OK, ButtonType.CANCEL);
            ok = getSkin().getButton(ButtonType.OK);
            validation.initInitialDecoration();
            //dialog setup stuff
            initOwner(MainApp.getWindow());
            //create content of the dialog
            VBox content = new VBox();
            content.prefWidthProperty().bind(getDialogPane().widthProperty());
            content.setSpacing(10);
            Label label = new Label(Lang.getCaseManagementWarningSelectDepartment());
            content.getChildren().add(label);

            for (CaseMergeContent version : getScene().getListOfVersions()) {
                //add a department selector to all local versions
                if (version.isEditable()) {
                    //create new Combobox and add to content
                    LabeledComboBox<TCaseDepartment> departmentCb = createDepartmentCombobox(version);
                    content.getChildren().add(departmentCb);

                    //if result of the dialog is OK Button set the department to selected Department in the combobox
                    resultProperty().addListener(new ChangeListener<ButtonType>() {
                        @Override
                        public void changed(ObservableValue<? extends ButtonType> observable, ButtonType oldValue, ButtonType newValue) {
                            if (newValue.equals(ButtonType.OK)) {
                                Long currentUserId = null;
                                try{
                                    currentUserId = Session.instance().getCpxUserId();
                                }catch (IllegalStateException stateExc) {
                                    LOG.log(Level.WARNING, "Can't detect user that want to create a new Version, reason " + stateExc.getMessage(), stateExc);
                                }

                                for (TCaseIcd icd : pIcdList) {
                                    try {
                                        TCaseIcd cloned = (TCaseIcd) icd.cloneWithoutIds(currentUserId);
                                        TCaseDepartment department = departmentCb.getSelectedItem();
                                        cloned.setCaseDepartment(department);
                                        version.saveIcdEntity(cloned);
                                        department.getCaseIcds().add(cloned);
                                    } catch (CloneNotSupportedException ex) {
                                        Logger.getLogger(CmIcdDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
//                                    icd.setCaseDepartment(departmentCb.getSelectedItem());
//                                    serviceFacade.saveIcdEntity(icd);
                                }
                                for (TCaseOps ops : pOpsList) {
                                    try {
                                        TCaseOps cloned = (TCaseOps) ops.cloneWithoutIds(currentUserId);
                                        TCaseDepartment department = departmentCb.getSelectedItem();
                                        cloned.setCaseDepartment(departmentCb.getSelectedItem());
                                        cloned.setOpscDatum(department.getDepcAdmDate());
                                        version.saveOpsEntity(cloned);
                                        department.getCaseOpses().add(cloned);
                                    } catch (CloneNotSupportedException ex) {
                                        Logger.getLogger(CmIcdDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
//                                    ops.setCaseDepartment(departmentCb.getSelectedItem());
//                                    ops.setOpscDatum(departmentCb.getSelectedItem().getDepcAdmDate());
//                                    serviceFacade.saveOpsEntity(ops);
                                }
                            }
                        }
                    });
//                    validation.registerValidator(departmentCb.getControl(), new Validator<TCaseDepartment>() {
//                        @Override
//                        public ValidationResult apply(Control t, TCaseDepartment u) {
//                            ValidationResult result = new ValidationResult();
//                            result.addErrorIf(t, "enter fab", departmentCb.getControl().getSelectionModel().getSelectedItem() == null);
//                            return result;
//                        }
//                    });
                    Platform.runLater(() -> {
                        validation.registerValidator(departmentCb.getControl(), Validator.createEmptyValidator("Bitte Fachabteilung auswählen", Severity.ERROR));
                    });

                }
            }
            //disable ok button of the dialog until the user has selected a department
//            Button ok = getSkin().getButton(ButtonType.OK);
//            ok.disableProperty().bind(selected);//selected);
            ok.disableProperty().bind(validation.invalidProperty());
            getDialogPane().setContent(content);
        }

        /**
         * @return get the selected departments from the user as an list
         */
        public List<TCaseDepartment> getSelectedDepartments() {
            List<TCaseDepartment> dep = new ArrayList<>();
            for (LabeledComboBox<TCaseDepartment> list : this.departments) {
                dep.add(list.getSelectedItem());
            }
            return dep;
        }

        // creates new labeled combobox for a version
        private LabeledComboBox<TCaseDepartment> createDepartmentCombobox(CaseMergeContent pVersion) {
            LabeledComboBox<TCaseDepartment> departmentList = new LabeledComboBox<>(pVersion.getVersionName());
            //sets converter to show the user some more information about the department
            //shows full name of the department and the admission and discharge date 
            departmentList.setConverter(new StringConverter<TCaseDepartment>() {
                @Override
                public String toString(TCaseDepartment object) {
                    if (object == null) {
                        return "";
                    }
                    CpxDepartmentCatalog catalog = CpxDepartmentCatalog.instance();
                    CpxDepartment depCatalog = catalog.getByCode(object.getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    if (depCatalog != null) {
                        return depCatalog.getDepDescription301() + "\n"
                                + "(" + Lang.toDate(object.getDepcAdmDate())
                                + " - "
                                + Lang.toDate(object.getDepcDisDate()) + ")";
                    }
                    return object.getDepKey301();
                }

                @Override
                public TCaseDepartment fromString(String string) {
                    return null;
                }
            });
            //sets a cell factory to bind list view to a max witdh 
            //sets to width of the combobox + magic 150 
            departmentList.setCellFactory(new Callback<ListView<TCaseDepartment>, ListCell<TCaseDepartment>>() {
                @Override
                public ListCell<TCaseDepartment> call(ListView<TCaseDepartment> param) {
                    return new ListCell<TCaseDepartment>() {
                        @Override
                        protected void updateItem(TCaseDepartment item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            getListView().maxWidthProperty().bind(departmentList.widthProperty().add(150));
                            setText(departmentList.getControl().getConverter().toString(item));
                        }
                    };
                }
            });
            //load all departments for the version from the db
            departmentList.setItems(FXCollections.observableArrayList(getScene().getFacade().getDepartmentsForDetailsId(pVersion.getCaseDetails())));
            departments.add(departmentList);
//            validation.registerValidator(departmentList, Validator.createPredicateValidator(new Predicate<TCaseDepartment>() {
//                @Override
//                public boolean test(T t) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//            }, "enter fab"));
            //bind selected property to react when the user selects a department 
//            if(!selected.isBound()){
//                selected.bind(departmentList.getControl().getSelectionModel().selectedItemProperty().isNull());
//            }else{
//                selected.and(departmentList.getControl().getSelectionModel().selectedItemProperty().isNull());
//            }
//            Bindings.and(ok.disableProperty(), departmentList.getControl().getSelectionModel().selectedItemProperty().isNull());
//            selected.and(departmentList.getControl().getSelectionModel().selectedItemProperty().isNull());
            return departmentList;
        }
    }

}

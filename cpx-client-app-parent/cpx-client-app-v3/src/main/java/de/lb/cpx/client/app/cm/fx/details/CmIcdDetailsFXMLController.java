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
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionContent;
import de.lb.cpx.client.app.cm.fx.simulation.model.button.AddVersionButton;
import de.lb.cpx.client.app.cm.fx.simulation.model.combobox.VersionComboBox;
import de.lb.cpx.client.app.cm.fx.simulation.tables.IcdTablePane;
import de.lb.cpx.client.app.cm.fx.simulation.tables.OpsTablePane;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.easycoder.EasyCoderDialog;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartment;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.model.HospitalDevisionIF;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.validation.constraints.NotNull;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * FXML Controller class Manages ui for the icd and ops screen of the case
 * management/ case simulation
 *
 * @author wilde
 */
public class CmIcdDetailsFXMLController extends Controller<CmIcdDetailsScene> {

    @FXML
    private HBox hBoxAddDiagnosisMenu;
    @FXML
    private Button btnAddDiagnosis;
    @FXML
    private IcdTablePane<VersionContent> spIcd;
    @FXML
    private OpsTablePane<VersionContent> spOps;
    @FXML
    private SplitPane spContent;

    private ObservableList<VersionContent> versionList = FXCollections.observableArrayList();
    private CaseServiceFacade icdServiceFacade;
    private boolean isClosed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        btnAddDiagnosis.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));

    }

    @FXML
    private void onAddDiagnosis(ActionEvent event) {
        //add a new icd and or ops to the currently selected local case details
        //starts new easycoder dialog (catalogdialog) and add stores the results 

        final Date admissionDate = icdServiceFacade.getCurrentLocalAdmissionDate();
        EasyCoderDialog dialog = new EasyCoderDialog(MainApp.getWindow(), admissionDate);
        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (t.equals(ButtonType.OK)) {
                    // get the result list (user selection in the easy coder)
                    List<TCaseIcd> listOfAddedIcd = Objects.requireNonNullElse(dialog.getSelectedTCaseIcd(), new ArrayList<>());
                    List<TCaseOps> listOfAddedOps = Objects.requireNonNullElse(dialog.getSelectedTCaseOps(), new ArrayList<>());

                    //close easyCoder
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
                                 //group all local versions to reflect changes
                                for (VersionContent version : versionList) {
                                    if (version.getContent().getCsdIsLocalFl()) {
                                        version.performGroup();
                                    }
                                }
                                if (!listOfAddedIcd.isEmpty()) {
                                    spIcd.reload();
                                }
                                //reload if not empty - loads new list maybe costs alot needs to check if performace is an issue 
                                if (!listOfAddedOps.isEmpty()) {
                                    spOps.reload();
                                }

                            }

                        }
                    });

                } //close easyCoder
                else if (t.equals(ButtonType.CANCEL)) {
                    dialog.closeEasyCoder();
                }
            }
        });

    }

    @Override
    public boolean close() {
        if(isClosed){
            LOG.info("Controller already closed - do nothing");
            return !isClosed;
        }
        super.close();
        versionList.removeListener(contentListener);
        versionList.clear();
        getScene().unBindhScrollBar(spIcd.getHScrollBar());
        getScene().unBindhScrollBar(spOps.getHScrollBar());
        hBoxAddDiagnosisMenu.prefHeightProperty().unbind();
        spIcd.setReloadCallback(null);
        spOps.setReloadCallback(null);
        spIcd.dispose();
        spIcd = null;
        spOps.dispose();
        spOps = null;
        isClosed = true;
        return true;
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
        spIcd.setVersionContentWidth(spIcd.getFullCollWidth());
        spOps.setVersionContentWidth(spIcd.getFullCollWidth());
//        spOps.prefWidthProperty().bind(spIcd.widthProperty());
        spIcd.setVersionCtrlFactory(new Callback<VersionContent, Control>() {
            @Override
            public Control call(VersionContent param) {
                return new VersionComboBox(getScene().getVersionManager(), param);
            }
        });
        spIcd.setComparator(new Comparator<VersionContent>() {
            @Override
            public int compare(VersionContent o1, VersionContent o2) {
                return Boolean.compare(o1.getCaseDetails().getCsdIsLocalFl(), o2.getCaseDetails().getCsdIsLocalFl());
            }
        });
        spOps.setCaseType(icdServiceFacade.getCurrentCase().getCsCaseTypeEn());
        spOps.setComparator(new Comparator<VersionContent>() {
            @Override
            public int compare(VersionContent o1, VersionContent o2) {
                return Boolean.compare(o1.getCaseDetails().getCsdIsLocalFl(), o2.getCaseDetails().getCsdIsLocalFl());
            }
        });
        spIcd.setOnRemove(new Callback<VersionContent, Boolean>() {
            @Override
            public Boolean call(VersionContent param) {
                //safty check for only deleting local version
                //Cpx-1151 avoid NPE
                if (param != null) {
                    if (param.getContent().getCsdIsLocalFl()) {
                        getScene().getVersionManager().removeFromManagedVersions(param);
                        return true;
                    }
                }
                return false;
            }
        });
        spIcd.setMenuButton(new AddVersionButton<>(getScene().getVersionManager()) {
            @Override
            public void afterVersionModified() {
                spIcd.reload();
                spOps.reload();
            }
        });
        spIcd.showMenu();
        btnAddDiagnosis.setDisable(icdServiceFacade.getCurrentCase().getCsCancellationReasonEn());
    }

    /**
     * init screen with value
     *
     * @param pVersionContent list of currently selected versions
     * @param pFacade facade to access server functions
     */
    public void init(ObservableList<VersionContent> pVersionContent, CaseServiceFacade pFacade) {
        //init facade in the panes 
        hBoxAddDiagnosisMenu.prefHeightProperty().bind(spIcd.prefHeaderHeightProperty());
        spIcd.setReloadCallback(new Callback<List<Long>, List<IcdOverviewDTO>>() {
            @Override
            public List<IcdOverviewDTO> call(List<Long> param) {
//                try {
//                    TimeUnit.SECONDS.sleep(10);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(CmIcdDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                waitUntilConditionIsMet(new BooleanSupplier() {
//                    @Override
//                    public boolean getAsBoolean() {
//                        return isGrounpingRunning();
//                    }
//                }, 10);
//                List<IcdOverviewDTO>  dtos = pFacade.getAllIcdCodes(param);
//                for(IcdOverviewDTO dto : dtos){
//                    if(dto.getIcdCode().equals("D50.0")){
//                        TCaseIcd icd = dto.getIcdForVersion(47751);
//                        LOG.log(Level.FINER, "icd: {0}, grResult: {1}", new Object[]
//                            {icd == null?"null":icd.getIcdcCode(),
//                             icd == null?"null":
//                                    ((icd.getGroupingResultses() == null || icd.getGroupingResultses().isEmpty())?"null":
//                                            "has result")
//                        });
//
//                        icd = dto.getIcdForVersion(2177451);
//                        LOG.log(Level.INFO, "icd: {0}, grResult: {1}", new Object[]
//                            {icd == null?"null":icd.getIcdcCode(),
//                             icd == null?"null":
//                                    ((icd.getGroupingResultses() == null || icd.getGroupingResultses().isEmpty())?"null":
//                                            "has result")
//                        });                    }
//                    
//                }
                return pFacade.getAllIcdCodes(param);
            }
        });
        spOps.setReloadCallback(new Callback<List<Long>, List<OpsOverviewDTO>>() {
            @Override
            public List<OpsOverviewDTO> call(List<Long> param) {
                return pFacade.getAllOpsCodes(param);
            }
        });
        icdServiceFacade = pFacade;
        versionList = pVersionContent;

        getScene().bindHScrollBar(spOps.getHScrollBar());
        getScene().bindHScrollBar(spIcd.getHScrollBar());
        
        //add list of versions in the ui
        addVersionList(pVersionContent);
        //set Listener to react to changes in the list in the ui
        pVersionContent.addListener(contentListener);

        btnAddDiagnosis.setDisable(icdServiceFacade.getCurrentCase().getCsCancellationReasonEn());

    }
    

    private void waitUntilConditionIsMet(BooleanSupplier awaitedCondition, int timeoutInSec) {
        boolean done;
        long startTime = System.currentTimeMillis();
        do {
            done = awaitedCondition.getAsBoolean();
        } while (!done && System.currentTimeMillis() - startTime < timeoutInSec * 1000);
    }

    public boolean isGrounpingRunning() {
        boolean running = false;
        for(VersionContent version : versionList){
            if(version.isGrouperRunning()){
                running = true;
            }
        }
        return running;
    }
    private final ListChangeListener<VersionContent> contentListener = new ListChangeListener<>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends VersionContent> c) {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (VersionContent version : c.getAddedSubList()) {
                        spIcd.addVersion(version);
                        spOps.addVersion(version);
                    }
                }
                if (c.wasRemoved()) {
                    //remove versions from the panes and load values from the database
                    for (VersionContent version : c.getRemoved()) {
                        spIcd.removeVersion(version);
                        spOps.removeVersion(version);
                        spIcd.reload();
                        spOps.reload();
                        version.destroy();
                    }

                }

            }

        }
    };

    @Override
    public void refresh() {
        super.refresh();
        spIcd.refreshMenu();
        spOps.refreshMenu();
    }

    @Override
    public void reload() {
        spIcd.reload();
        spOps.reload();
    }

    /*
    * private methodes
     */
    //add a list of versions to the panes
    private void addVersionList(List<? extends VersionContent> pVersionList) {
        for (VersionContent version : pVersionList) {
            spIcd.addVersion(version);
            spOps.addVersion(version);
        }
        spIcd.reload();
        spOps.reload();
    }

    /**
     * department dialog, needed to define a department when an ops icd object
     * is added
     */
    private class DepartmentDialog extends AlertDialog {

        // list of selection elements for all departments
        private final List<LabeledComboBox<HospitalDevisionIF>> departments = new ArrayList<>();
        //indicator if the user has atleast selected some department
        private ValidationSupport validation = new ValidationSupport();
        private final Button ok;

        /**
         * construct a new Departmentdialog
         *
         * @param pIcdList list of icds the user wants to add
         * @param pOpsList list of ops the user wants to add
         */
        DepartmentDialog(List<TCaseIcd> pIcdList, List<TCaseOps> pOpsList) {
            super(AlertType.CONFIRMATION, Lang.getConformation(), Lang.getCaseManagementWarningSelectDepartment(), (Throwable) null, ButtonType.OK, ButtonType.CANCEL);
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

            for (VersionContent version : versionList) {
                //add a department selector to all local versions
                if (version.getContent().getCsdIsLocalFl()) {
                    //create new Combobox and add to content
                    LabeledComboBox<HospitalDevisionIF> departmentCb = createDepartmentCombobox(version);
                    content.getChildren().add(departmentCb);
                    //CPX-1243 Combobox gets the first department
//                    if (departmentCb.getItems().size() == 1) {
                        departmentCb.select(departmentCb.getItems().get(0));
//                    }
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
                                 HospitalDevisionIF selected = departmentCb.getSelectedItem();
                                for (TCaseIcd icd : pIcdList) {
                                    try {
                                        TCaseIcd cloned = (TCaseIcd) icd.cloneWithoutIds(currentUserId);
                                       
                                        if(selected instanceof TCaseDepartment){
                                            cloned.setCaseDepartment((TCaseDepartment)selected);
                                        }else{
                                            cloned.setCaseWard((TCaseWard)selected);
                                            cloned.setCaseDepartment(((TCaseWard)selected).getCaseDepartment());
                                        }
                                        icdServiceFacade.saveIcdEntity(cloned);
                                        departmentCb.getSelectedItem().getCaseIcds().add(cloned);
                                    } catch (CloneNotSupportedException ex) {
                                        Logger.getLogger(CmIcdDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                for (TCaseOps ops : pOpsList) {
                                    try {
                                        TCaseOps cloned = (TCaseOps) ops.cloneWithoutIds(currentUserId);
                                        if(selected instanceof TCaseDepartment){
                                         cloned.setCaseDepartment((TCaseDepartment)selected);
                                        //check if user selected date, if it is not set - set admission date of department
                                            if (cloned.getOpscDatum() == null) {
                                                cloned.setOpscDatum(((TCaseDepartment)selected).getDepcAdmDate());
                                            }
                                        }else{
                                            cloned.setCaseWard((TCaseWard)selected);
                                            cloned.setOpscDatum(((TCaseWard)selected).getWardcAdmdate());
                                            cloned.setCaseDepartment(((TCaseWard)selected).getCaseDepartment());
                                            
                                        }
                                        icdServiceFacade.saveOpsEntity(cloned);
                                        departmentCb.getSelectedItem().getCaseOpses().add(cloned);
                                    } catch (CloneNotSupportedException ex) {
                                        Logger.getLogger(CmIcdDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                    });
                    Platform.runLater(() -> {
                        validation.registerValidator(departmentCb.getControl(), Validator.createEmptyValidator("Bitte Fachabteilung/Station auswählen", Severity.ERROR));
                    });

                }
            }
            //disable ok button of the dialog until the user has selected a department
            ok.disableProperty().bind(validation.invalidProperty());
            getDialogPane().setContent(content);
            setOnHidden(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent event) {
                    departments.clear();
                    content.prefWidthProperty().unbind();
                    content.getChildren().clear();
                }
            });
        }

        /**
         * @return get the selected departments from the user as an list
         */
        public List<HospitalDevisionIF> getSelectedDepartments() {
            List<HospitalDevisionIF> dep = new ArrayList<>();
            for (LabeledComboBox<HospitalDevisionIF> list : this.departments) {
                dep.add(list.getSelectedItem());
            }
            return dep;
        }
        
        private String getDepartmentText(@NotNull TCaseDepartment dept, boolean isDepartment){
           
            CpxDepartmentCatalog catalog = CpxDepartmentCatalog.instance();
            CpxDepartment depCatalog = catalog.getByCode(dept.getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);

            return (depCatalog == null?dept.getDepKey301():depCatalog.getDepDescription301()) 
                    + (isDepartment?"\n":"")
                    + "(" + Lang.toDate(dept.getDepcAdmDate())
                    + " - "
                    + Lang.toDate(dept.getDepcDisDate()) + ")";
            
            
        }

        // creates new labeled combobox for a version
        private LabeledComboBox<HospitalDevisionIF> createDepartmentCombobox(VersionContent pVersion) {
            LabeledComboBox<HospitalDevisionIF> departmentList = new LabeledComboBox<>(pVersion.getVersionName());
            //sets converter to show the user some more information about the department
            //shows full name of the department and the admission and discharge date 
            departmentList.setConverter(new StringConverter<HospitalDevisionIF>() {
                @Override
                public String toString(HospitalDevisionIF object) {
                    if (object == null) {
                        return "";
                    }
                    if(object instanceof TCaseDepartment){
                        return getDepartmentText((TCaseDepartment)object, true);    
                    }else if (object instanceof TCaseWard){
                        TCaseWard ward = (TCaseWard)object;
                        return getDepartmentText(ward.getCaseDepartment(), false) + "\n"
                                + ward.getWardcIdent() + " " + "(" + Lang.toDate(ward.getWardcAdmdate())
                                + " - "
                                + Lang.toDate(ward.getWardcDisdate()) + ")";
                    }
                        
                    return "";
                }

                @Override
                public TCaseDepartment fromString(String string) {
                    return null;
                }
            });
            //sets a cell factory to bind list view to a max witdh 
            //sets to width of the combobox + magic 150 
            departmentList.setCellFactory(new Callback<ListView<HospitalDevisionIF>, ListCell<HospitalDevisionIF>>() {
                @Override
                public ListCell<HospitalDevisionIF> call(ListView<HospitalDevisionIF> param) {
                    return new ListCell<HospitalDevisionIF>() {
                        @Override
                        protected void updateItem(HospitalDevisionIF item, boolean empty) {
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
            
            departmentList.setItems(FXCollections.observableArrayList(icdServiceFacade.getHospitalDivisionsForDetailsId(pVersion.getContent().getId())));
            departments.add(departmentList);
            return departmentList;
        }

    }

}

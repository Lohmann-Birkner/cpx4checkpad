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
import de.lb.cpx.client.app.cm.fx.CaseManagementFXMLController;
import de.lb.cpx.client.app.cm.fx.simulation.lists.CaseDetailsListView;
import de.lb.cpx.client.app.cm.fx.simulation.model.SimulationScreen;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionContent;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.client.core.util.CaseDetailsCommentHelper;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * FXML Controller class Manage ui for the version management in the case
 * management/case simulation Versions are in the instance case details
 *
 * @author wilde
 */
public class CmVersionManagementFXMLController extends Controller<SimulationScreen<TCaseDetails, VersionContent>> {

    private static final Logger LOG = Logger.getLogger(CmVersionManagementFXMLController.class.getName());

    @FXML
    private CaseDetailsListView lvExternVersion;
    @FXML
    private CaseDetailsListView lvLocalVersion;

    private VersionManager versionManager;
    private ObservableList<VersionContent> managedVersions;
    private WeakPropertyAdapter propAdapter;
    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        propAdapter = new WeakPropertyAdapter();
        setUpListViewForExterns();
        setUpListViewForLocals();
    }

    @Override
    public boolean close() {
        if(propAdapter != null){
            propAdapter.dispose();
            propAdapter = null;
        }
        return super.close(); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * init screen with value
     *
     * @param pManager version manager instance to access server function and
     * update ui status of an version
     */
    public void init(VersionManager pManager) {
        versionManager = pManager;
        managedVersions = pManager.getManagedVersions();
        ObservableList<TCaseDetails> availableExterns = versionManager.getAllAvailableExterns();
        ObservableList<TCaseDetails> availableLocals = versionManager.getAllAvailableLocals();
        lvExternVersion.setItems(availableExterns);
        lvLocalVersion.setItems(availableLocals);
    }

    private void setUpListViewForExterns() {
        lvExternVersion.setTitle(Lang.getExternVersions());
        lvExternVersion.addStyleClassToTitle("cpx-detail-label");
        lvExternVersion.setCellFactory(new Callback<ListView<TCaseDetails>, ListCell<TCaseDetails>>() {
            @Override
            public ListCell<TCaseDetails> call(ListView<TCaseDetails> param) {
                return new VersionSelectCell();
            }
        });
        propAdapter.addChangeListener(lvExternVersion.getSelectedItemProperty(),new ChangeListener<TCaseDetails>() {
            @Override
            public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
                managedVersions.get(0).contentProperty().set(newValue);
                versionManager.markAsDisplayed(newValue);
                versionManager.unMarkAsDisplayed(oldValue);
            }
        });
    }

    private void setUpListViewForLocals() {
        lvLocalVersion.setTitle(Lang.getLocalVersions());
        lvLocalVersion.addStyleClassToTitle("cpx-detail-label");
        lvLocalVersion.setCellFactory(new Callback<ListView<TCaseDetails>, ListCell<TCaseDetails>>() {
            @Override
            public ListCell<TCaseDetails> call(ListView<TCaseDetails> param) {
                return new VersionSelectCell();
            }
        });
        propAdapter.addChangeListener(lvLocalVersion.getSelectedItemProperty(),new ChangeListener<TCaseDetails>() {
            @Override
            public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
                if (versionManager.isDisplayed(newValue)) {
                    return;
                }
                if (managedVersions.size() > 1) {
                    if (!managedVersions.get(1).getContent().equals(newValue) && !versionManager.isDisplayed(newValue)) {
                        managedVersions.get(1).setContent(newValue);
                    }
                } else {
                    versionManager.createAndAddVersionContent(newValue);
                }
            }
        });
    }

    /**
     * ListCell for displaying a case details object in the local and extern
     * listviews handels up updates and changes in the displaying status of a
     * version
     */
    private class VersionSelectCell extends ListCell<TCaseDetails> {

        private VersionStringConverter converter = new VersionStringConverter();
        private CheckBox isActual;
        private Button editCommentButton;
        private final Double FIXED_ROW_HEIGHT = 65.0d;
        
        public VersionSelectCell() {
            super();
            setMinHeight(Control.USE_PREF_SIZE);
            setPrefHeight(FIXED_ROW_HEIGHT);
            setMaxHeight(Control.USE_PREF_SIZE);
        }
        
        
        /*
         * event handler for ui interaction with the user, delete, add, edit, change actual  
         */
        private EventHandler<ActionEvent> addVersionHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                TaskService<TCaseDetails> addTask = new TaskService<TCaseDetails>("Neue Fallversion wird erstellt!") {
                    @Override
                    public TCaseDetails call() {
                        TCaseDetails newlyCreated = versionManager.getServiceFacade().createNewVersion(itemProperty().get());
                        setProgress(20.0d);
//                        versionManager.getAllAvailableLocals().add(0, newlyCreated);
                        versionManager.addAsyncToAvailableLocals(0,newlyCreated);
                        ObservableList<TCaseDetails> availableLocals = versionManager.getAllAvailableLocals();
                        lvLocalVersion.setItems(availableLocals);
                        SimpleBooleanProperty prop = new SimpleBooleanProperty(true);
                        setProgress(40.0d);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                lvLocalVersion.select(newlyCreated);
                                prop.set(false);
                            }
                        });
                        setProgress(60.0d);
                        while (prop.get()) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(CmVersionManagementFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                Thread.currentThread().interrupt();
                            }
                        }
                        setProgress(100.0d);
                        return newlyCreated;
                    }

                    @Override
                    public void afterTask(Worker.State pState) {
                        LOG.info("add finished, now add item to ui");
                        super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                        if(Worker.State.FAILED.equals(pState)){
                            MainApp.showErrorMessageDialog(Lang.getErrorVersionCouldNotCreate());
                        }
                        if(Worker.State.SUCCEEDED.equals(pState)){
                            TCaseDetails newlyCreated = getValue();
//                            lvLocalVersion.select(newlyCreated);
//                            ObservableList<TCaseDetails> availableLocals = versionManager.getAllAvailableLocals();
//                            lvLocalVersion.setItems(availableLocals);
//                            lvLocalVersion.select(newlyCreated);
                            LOG.info("new version with versionNumber: " + newlyCreated.getCsdVersion() + " was added!");
                        }
                    }
                };
                addTask.start();
            }

        };

        private final EventHandler<MouseEvent> isActualHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TCaseDetails item = itemProperty().get();
                if(isActual == null || item == null){
                    return;
                }
                if (isActual.isSelected() && item.getCsdIsActualFl()) {
                    return;
                }
                item.setCsdIsActualFl(true);
                for (TCaseDetails details : getListView().getItems()) {
                    if (!details.equals(item) && details.getCsdIsActualFl()) {
                        details.setCsdIsActualFl(false);
                        versionManager.getServiceFacade().saveCaseDetails(details);
                        if (!versionManager.isDisplayed(details)) {
                            versionManager.unMarkAsDisplayed(details);
                        }
                    }
                }
                if (!versionManager.isDisplayed(item)) {
                    getListView().getSelectionModel().select(item);
                    versionManager.markAsDisplayed(item);
                }
                versionManager.getServiceFacade().saveCaseDetails(item);
                isActual.setSelected(!isActual.isSelected());
                versionManager.getServiceFacade().getProperties().put(CaseManagementFXMLController.UPDATE_SUMMARY, null);
                getListView().refresh();
            }
        };
        private final EventHandler<MouseEvent> editCommentHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new EditCommentPopOver(itemProperty().get()).show(editCommentButton);
            }
        };

        @Override
        public void updateItem(TCaseDetails item, boolean empty) {
            super.updateItem(item, empty);
            Node node = updatedLayout(item, empty);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
                    try{
                        setGraphic(node);
                    }catch(java.lang.IllegalStateException ex){
                        LOG.info("update was not executed on ApplicationThread");
                    }
//                }
//            });
//            if(empty){
//                setGraphic(null);
//            }else{
////                        setGraphic(item!=null?new AsyncPane(Boolean.TRUE) {
////                            @Override
////                            public Node loadContent() {
////                                return createLayout(item);
////                            }
////                        }:null);
//                setGraphic(item!=null?createLayout(item):null);
//            }
        }
        private Node updatedLayout(TCaseDetails pItem, boolean pEmpty){
            if(pEmpty){
                return null;
            }else{
                return pItem!=null?createLayout(pItem):null;
            }
        }
        private Node createLayout(TCaseDetails item){
            GridPane root = new GridPane();
            root.prefWidthProperty().bind(getListView().widthProperty().subtract(24.0));
            Label label = new Label(converter.toString(item));
            HBox commentaryBox = new HBox();
            Label commenttitle = new Label(Lang.getVersioncontrollComment());

            VBox commentContent = new VBox();
            Label commentMetaInfo = new Label(CaseDetailsCommentHelper.getMetaData(item));
            Label comment = new Label();
            //CPX-1106 RSH - Verbesserung der Anzeige wenn Commentare lang ist
            commentContent.prefWidthProperty().bind(root.widthProperty().divide(2));
            comment.setText(CaseDetailsCommentHelper.formatUserCommentNoLineBreak(item));
            if (item.getComment() != null) {
                Tooltip tip = new CpxTooltip(CaseDetailsCommentHelper.formatCommentNoSeperator(item), 200, 5000, 100, true);//item.getComment().replace(SEPERATOR, "\n"));
                tip.setWrapText(true);
                Tooltip.install(commentContent, tip);
            }
            commentContent.getChildren().addAll(commentMetaInfo, comment);
            VBox header = new VBox();
            boolean isCanceledVersion = isCanceledVersion(item);
            editCommentButton = createNewButton("\uf044");
            editCommentButton.setDisable(isCanceledVersion);
            editCommentButton.setOnMouseClicked(editCommentHandler);
            Button deleteVersionButton = createNewButton("\uf1f8");
            deleteVersionButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    long start = System.currentTimeMillis();
                    TCaseDetails newLocal = null;
                    try {
                        long startDelete = System.currentTimeMillis();
                        newLocal = versionManager.getServiceFacade().deleteVersion(item);
                        LOG.log(Level.INFO, "DELETE on server took, " + (System.currentTimeMillis() - startDelete) + " ms");
                    } catch (CpxIllegalArgumentException ex) {
                        MainApp.showErrorMessageDialog(ex, Lang.getVersioncontrollError());
                        return;
                    }
                    itemProperty().get().getHospitalCase().getCaseDetails().remove(item);
                    if (versionManager.isDisplayed(newLocal)) {
                        TCaseDetails unselected = versionManager.getFirstUnSelectedLocal();
                        if (unselected != null && unselected.getId() != item.getId()) {
                            updateCaseDetails(newLocal);
                            newLocal = unselected;
                        }
                    }
                    if (getListView().getItems().contains(newLocal)) {
                        updateCaseDetails(newLocal);
                        getListView().getSelectionModel().select(newLocal);
                        versionManager.markAsDisplayed(newLocal);
                    }

                    versionManager.unMarkAsDisplayed(item);
                    versionManager.removeDetailsFromLocals(item);
                    VersionContent version = versionManager.getVersionContentForDetails(item);
                    versionManager.removeFromManagedVersions(version);

                    getListView().refresh();
                    LOG.log(Level.INFO, "DELETE Action took, " + (System.currentTimeMillis() - start) + " ms");
                }

                public void updateCaseDetails(TCaseDetails pToUpdate) {
                    if (getListView().getItems().contains(pToUpdate)) {
                        getListView().getItems().set(getListView().getItems().indexOf(pToUpdate), pToUpdate);
                    }
                }
            });
            Button addVersionButton = createNewButton(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
            addVersionButton.setOnAction(addVersionHandler);
            addVersionButton.setDisable(isCanceledVersion);
            VBox deleteBox = new VBox(deleteVersionButton);
            VersionStringConverter.convertSimple(item);
            updateDeleteButtonBehavior(deleteBox, deleteVersionButton, item);
//            deleteVersionButton.setDisable(isDeleteDisabledForVersion(item));
//            installDeleteBoxTooltip(deleteBox,item);
            isActual = new CheckBox(Lang.getActual());
            isActual.setDisable(isCanceledVersion);
            isActual.setSelected(item.getCsdIsActualFl());
            isActual.setOnMouseClicked(isActualHandler);
            HBox editField ;
            if(item.getCsdIsLocalFl()){
                editField = new HBox(5,isActual,new EditVersionTypeLayout(item, isCanceledVersion),addVersionButton, editCommentButton, deleteBox);
            }else{
                editField = new HBox(5,isActual, addVersionButton , editCommentButton, deleteBox);
            }
//            deleteVersionButton.setDisable(isCanceledVersion);
//            if (item.getHospitalCase().getCaseDetails().stream().filter(p -> p.getCsdIsLocalFl()).count() <= 1) {
//                deleteVersionButton.setDisable(true);
//                Tooltip.install(deleteBox, new Tooltip(Lang.getVersioncontrollError() + Lang.getVersioncontrollHintOneMustRemain()));
//            }
//            if (!item.getCsdIsLocalFl()) {
//                deleteVersionButton.setDisable(true);
//                Tooltip.install(deleteBox, new Tooltip(Lang.getVersioncontrollError() + Lang.getHisVersion()));
//            }
//            deleteVersionButton.setDisable(true);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    String successorDetails = versionManager.getServiceFacade().getSuccessorDetails(item);
//                    if (successorDetails != null) {
//                        Tooltip.install(deleteBox, new Tooltip(Lang.getVersioncontrollError() + Lang.getVersioncontrollHintParentVersion() + successorDetails));
//                    }else{
//                        deleteVersionButton.setDisable(false);
//                    }
//                }
//            });
            header.getChildren().addAll(commenttitle);
            commentaryBox.getChildren().addAll(header, commentContent);
            HBox titleWrapper = new HBox(label);
            if ((!item.getCsdIsLocalFl()) && (versionManager.getServiceFacade().isBasicForProcess(item))) {
                Label lblTest = new Label("(Vorgangs-Version)");
                    lblTest.setStyle("-fx-font-style: italic;");
                    titleWrapper.getChildren().add(lblTest);
                    titleWrapper.setSpacing(5);
                    //TODO: add link to Process to open it? Specify typ of process? like MD-Process or whatever??
            }
            titleWrapper.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(titleWrapper, Priority.ALWAYS);

            editField.setAlignment(Pos.CENTER_RIGHT);
            root.getColumnConstraints().addAll(new ColumnConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true),
                    new ColumnConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true)
            );
            VBox hbCancel = new VBox();
            if (isCanceledVersion) {
                Label lbCancelDate = new Label("Datum der Stornierung: " + Lang.toDate(item.getCsdCancelDate()));
                lbCancelDate.getStyleClass().add("red-colored-label");
                Label lbCancelReaseon = new Label("Stornierungsgrund " + item.getCsdCancelReasonEn().toString());
                lbCancelReaseon.getStyleClass().add("red-colored-label");
                hbCancel.getChildren().addAll(lbCancelDate, lbCancelReaseon);

            }
            root.getChildren().clear();
            root.addRow(0, titleWrapper, editField);
            root.addRow(1, header, commentContent);
            root.addRow(2, hbCancel);
            return root;
        }
        private Button createNewButton(String icon) {
            return createNewButton(ResourceLoader.getGlyph(icon));
        }

        private Button createNewButton(Glyph icon) {
            Button button = new Button();
            button.getStyleClass().add("cpx-icon-button");
            button.setGraphic(icon);
            return button;
        }
        private void updateDisableTooltip(VBox pBox, Button pButton, String pTooltip){
            if(pTooltip == null || pTooltip.isEmpty()){
                pButton.setDisable(false);
                Tooltip.uninstall(pBox, null);
            }else{
                pButton.setDisable(true);
                Tooltip.install(pBox, new Tooltip(pTooltip));
            }
        }
        private void updateDeleteButtonBehavior(VBox pButtonBox,Button pButton, TCaseDetails pDetails){
            if(pDetails == null || isCanceledVersion(pDetails)){
                LOG.warning("can not detect disable status for delete! Version is null or case is canceled!");
                updateDisableTooltip(pButtonBox, pButton, null);
                return;
            }
            StringBuilder sb = new StringBuilder();
            if(!pDetails.getCsdIsLocalFl()){ //is kis can not delete
                sb.append(Lang.getVersioncontrollError()).append(" ").append(Lang.getHisVersion());
                updateDisableTooltip(pButtonBox, pButton, sb.toString());
                return;
            }
            if(!hasSufficentLocalVersions(pDetails)){ // total number of not kis versions must atleast be 1
                sb.append(Lang.getVersioncontrollError()).append(" ").append(Lang.getVersioncontrollHintOneMustRemain());
                updateDisableTooltip(pButtonBox, pButton, sb.toString());
                return;
            }
            Platform.runLater(new Runnable() { //compute info away from MainThread to avoid "lag" when accessing server for successorInformation
                @Override
                public void run() {
                    String successorDetails = versionManager.getServiceFacade().getSuccessorDetails(pDetails); //get info if version has versions based on them due to legacy these versions can not be deleted
                    if (successorDetails != null) {
                        sb.append(Lang.getVersioncontrollError()).append(" ").append(Lang.getVersioncontrollHintParentVersion()).append(successorDetails);
                    }
                    updateDisableTooltip(pButtonBox, pButton, sb.toString());
                }
            });
        }
        
        private boolean hasSufficentLocalVersions(TCaseDetails pItem){
            if(pItem == null){
                return false;
            }
            if(!pItem.getCsdIsLocalFl()){ //if kis ignore
                return false;
            }
            return pItem.getHospitalCase().getCaseDetails().stream().filter(p -> p.getCsdIsLocalFl()).count() > 1;
        }
        private boolean isCanceledVersion(TCaseDetails item){
            if(item == null){
                LOG.warning("Can not detect if version is canceled! Version is null");
                return true;
            }
            return item.getCsdCancelDate() != null && item.getCsdCancelReasonEn() != null;
        }
    }
    
    private class EditVersionTypeLayout extends HBox{
        private static final String TITLE = "Ereignis";

        public EditVersionTypeLayout(TCaseDetails pVersion, boolean isCancel) {
            this(5);
            setAlignment(Pos.CENTER_LEFT);
            Label lblTitle = new Label(TITLE);
            ComboBox<VersionRiskTypeEn> cbVersionType = new ComboBox<>(FXCollections.observableArrayList(VersionRiskTypeEn.values()));
            cbVersionType.getStyleClass().add("version-type-combo-box");
            cbVersionType.getSelectionModel().select(getType(pVersion));
            cbVersionType.setDisable(isCancel);
            cbVersionType.setConverter(new StringConverter<VersionRiskTypeEn>() {
                @Override
                public String toString(VersionRiskTypeEn t) {
                    return t!=null?t.getTranslation().getValue():null;
                }

                @Override
                public VersionRiskTypeEn fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            cbVersionType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<VersionRiskTypeEn>() {
                @Override
                public void changed(ObservableValue<? extends VersionRiskTypeEn> ov, VersionRiskTypeEn t, VersionRiskTypeEn t1) {
                    if(t1== null){
                        cbVersionType.getSelectionModel().select(t);
                        return;
                    }
                    pVersion.setCsdVersRiskTypeEn(t1);
                    versionManager.getServiceFacade().saveCaseDetails(pVersion);
                }
            });
            getChildren().addAll(cbVersionType,lblTitle);
        }

        private EditVersionTypeLayout(double d, Node... nodes) {
            super(d, nodes);
        }
        
        private VersionRiskTypeEn getType(TCaseDetails pVersion){
            if(pVersion == null){
                return VersionRiskTypeEn.NOT_SET;
            }
            return pVersion.getCsdVersRiskTypeEn()!=null?pVersion.getCsdVersRiskTypeEn():VersionRiskTypeEn.NOT_SET;
        }
        
    }
    /**
     * popover to edit the comment in the case details implements autosave
     * function user input is limited to 512 characters
     */
    private class EditCommentPopOver extends AutoFitPopOver {

        EditCommentPopOver(TCaseDetails pDetails) {
            super();
            //popover settings
            setHideOnEscape(true);
            setDetachable(false);
            setAutoHide(true);
            setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
            //CPX-1106 RSH - textArea size has been changed from 512 to 750
            //LimitedTextArea editArea = new LimitedTextArea(750);
            LabeledTextArea editArea = new LabeledTextArea("Versionskommentar", LabeledTextArea.CASE_VERSION_SIZE);
            editArea.getControl().focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue && oldValue) {
                        editArea.requestFocus();
                    }
                }
            });
            editArea.setText(CaseDetailsCommentHelper.getCommentTxt(pDetails));
            editArea.getControl().focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        return;
                    }
                    final String text = StringUtils.trimToNull(editArea.getText());
                    CaseDetailsCommentHelper.replaceUserComment(pDetails, text);
                    versionManager.getServiceFacade().saveCaseDetails(pDetails);
                    lvExternVersion.getListView().refresh();
                    lvLocalVersion.getListView().refresh();
                }
            });
            VBox wrapper = new VBox(editArea);
            wrapper.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
            setContentNode(wrapper);
        }
    }

}

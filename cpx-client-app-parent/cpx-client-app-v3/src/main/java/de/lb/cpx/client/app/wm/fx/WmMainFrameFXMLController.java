/*/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.ProcessTopicChangedEventSubject;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.ProcessUserChangedEventSubject;
import de.lb.cpx.client.app.wm.fx.process.master_detail.ProcessMasterDetailPane;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmProcessFinalisationOperations;
import de.lb.cpx.client.app.wm.fx.process.tab.CaseTab;
import de.lb.cpx.client.app.wm.fx.process.tab.POverviewTab;
import de.lb.cpx.client.app.wm.fx.process.tab.PatientTab;
import de.lb.cpx.client.app.wm.fx.process.tab.ProcessCompletionTab;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.TabController;
import de.lb.cpx.client.core.model.fx.adapter.IWeakAdapter;
import de.lb.cpx.client.core.model.fx.adapter.WeakMapAdapter;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.tab.ShowTabAction;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab.TabType;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * FXML Controller class
 *
 * @author wilde
 */
public class WmMainFrameFXMLController extends TabController <CpxScene>{

    private static final Logger LOG = Logger.getLogger(WmMainFrameFXMLController.class.getSimpleName());
    public static final String REFRESH_SUMMARY = "refresh.summary";
    @FXML
    private TabPane tbpOpenContent;
//    @FXML
//    private ComboBox<MenuOption> cbProcessActions;
    private ProcessServiceFacade serviceFacade;
    @FXML
    private Button btnProcessActions;
    @FXML
    private HBox hBoxMenuWrapper;
    private Label lblProcessNumber;
    private Label lblProcessNumberValue;
    private Label lblStatus;
    private Label lblStatusValue;
    private ProcessMasterDetailPane processMasterDetailPane;
    @FXML
    private HBox hBoxMetaData;
    private Label lblType;
    private ComboBox<CWmListProcessTopic> cbTypeValue;
    private Label lblAssignedUser;
    private ComboBox<UserDTO> cbAssignedUserValue;
    private Label lblLawerFileNumber;
    private Label lblCourtFileNumber;
    private Label lblCourtFileNumberValue;
    private Label lblLawerFileNumberValue;
    @FXML
    private GridPane gpWmMainFrame;
    private WeakPropertyAdapter propAdapter;
//    private ChangeListener<CWmListProcessTopic> typeChangeListener = new ChangeListener<CWmListProcessTopic>() {
//            @Override
//            public void changed(ObservableValue<? extends CWmListProcessTopic> observable, CWmListProcessTopic oldValue, CWmListProcessTopic newValue) {
//                //unsafe cast, but changing should only happen when process is hospital case 
//                //so no check needed?
//                TWmProcessHospital process = (TWmProcessHospital) getProcess();
//                if (process == null) {
//                    LOG.severe("try to change process topic, but no process is found!");
//                    return;
//                }
//                //if null do not check store and return 
////                if (newValue != null) {
//                if (newValue != null && (process.getProcessTopic() == null || newValue.getWmPtInternalId() != process.getProcessTopic())/*process.getProcessTopic() != newValue.getId()*/) {
//                    if (!newValue.getWmPtValid() || newValue.isWmPtDeleted()) {
//                        //if invalid or deleted, do nothing
//                        LOG.warning("selected process topic is invalid or deleted! Can not save value!");
//                        return;
//                    }
//                }
////                }
//                //check if current process stores already this value, store only if not
//                
//                if (newValue != null && (!Objects.equals(newValue.getWmPtInternalId(), process.getProcessTopic()))) {
//                    String desc = new ProcessTopicChangedEventSubject(process, oldValue, newValue).getText();
//                    //String desc = oldValue == null ? (newValue.getWmPtName() + " als Vorgangstyp gesetzt") : Lang.getEventTypeProcessSubjectChangedDescription(oldValue.getWmPtName(), newValue.getWmPtName());
//                    process.setProcessTopic(newValue.getWmPtInternalId());
//                    serviceFacade.createAndStoreEvent(WmEventTypeEn.processSubjectChanged, null, desc);
//                    serviceFacade.saveOrUpdateProcess(process);
//
//                }
//            }
//        };
//    private ChangeListener<UserDTO> userChangeListener = new ChangeListener<UserDTO>() {
//        @Override
//        public void changed(ObservableValue<? extends UserDTO> observable, UserDTO oldUser, UserDTO newUser) {
//            TWmProcess process = getProcess();
//            if (newUser == null || !newUser.getId().equals(process.getAssignedUser())) {
//                process.setAssignedUser(newUser != null ? newUser.getId() : 0L);
//                String desc = new ProcessUserChangedEventSubject(process, oldUser, newUser).getText();
//                serviceFacade.createAndStoreEvent(WmEventTypeEn.processUserChanged, null, desc);
//                serviceFacade.saveOrUpdateProcess(process);
//                LOG.log(Level.INFO, "selected assigned user is updated: {0}", newUser != null ? newUser.getUserName() : "");
//            }
//        }
//    };
//    private ChangeListener<Number> repositionHeaderListener = new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                updateHeaderPosition(newValue.doubleValue(), hBoxMetaData.getWidth());
//            }
//        };
//    private ChangeListener<ShowTabAction> LOAD_AND_SHOW_LISTENER = new ChangeListener<ShowTabAction>() {
//            @Override
//            public void changed(ObservableValue<? extends ShowTabAction> observable, ShowTabAction oldValue, ShowTabAction newValue) {
//                if (newValue != null) {
//                    showTab(newValue);
//                }
//            }
//        };
//    private MapChangeListener<Object, Object> PROPERTIES_LISTENER = new MapChangeListener<Object, Object>() {
//            @Override
//            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
//                if (change.wasAdded() && REFRESH_SUMMARY.equals(change.getKey())) {
//                    setupSummary();
//                    serviceFacade.getProperties().remove(REFRESH_SUMMARY);
//                }
//            }
//        };
    private WeakMapAdapter mapAdapter;
    
//    private void updateHeaderPosition(double pMenuWidth,double pMetaDataWidth){
//        StackPane header = (StackPane) tbpOpenContent.lookup(".tab-header-area");
//        if (header != null) {
//            //compute new Values translate whole header to the left and than set padding to the right
//            //not really nice, but found no other way, should be changed to a more clear way
//            //tbMenu is normaly not present in that controller .. can be solved maybe with an lookup on this controller parent?!
//            header.setTranslateX(-1.0d * ((int)pMenuWidth));
//            header.setPadding(new Insets(0, 0, 0, pMenuWidth + pMetaDataWidth));//tbMenu.getPrefWidth()));
//        }
//    }
    public void setupSummary() {
        TWmProcess process = getProcess();
        String placeholder = "-";

        lblProcessNumber.setText(Lang.getProcessNumber());
        lblCourtFileNumber.setText(Lang.getCourtFileNumberObj().getAbbreviation());
        lblCourtFileNumber.setTooltip(new Tooltip(Lang.getFileNumberEditTooltip(Lang.getCourtFileNumber()) + "\n" + Lang.getFileNumberSaveTooltip()));
        lblLawerFileNumber.setText(Lang.getLawerFileNumberObj().getAbbreviation());
        lblLawerFileNumber.setTooltip(new Tooltip(Lang.getFileNumberEditTooltip(Lang.getLawerFileNumber()) + "\n" + Lang.getFileNumberSaveTooltip()));
        lblStatus.setText(Lang.getWorkflowStateObj().getAbbreviation());
        lblType.setText(Lang.getProcessPreviewProcessTopic());
        lblAssignedUser.setText("Bearbeiter");

        lblProcessNumberValue.setText(process == null ? placeholder : String.valueOf(process.getWorkflowNumber()));

        lblCourtFileNumberValue.setText(process == null || process.getCourtFileNumber() == null ? " " : String.valueOf(process.getCourtFileNumber()));
        lblCourtFileNumberValue.setTooltip(new Tooltip("Um Aktenzeichen (Gericht) zu bearbeiten , Klicken Sie hier."));
        lblLawerFileNumberValue.setText(process == null || process.getLawerFileNumber() == null ? " " : String.valueOf(process.getLawerFileNumber()));
        lblLawerFileNumberValue.setTooltip(new Tooltip("Um Aktenzeichen (RA) zu bearbeiten , Klicken Sie hier."));
        lblStatusValue.setText(process == null ? placeholder : String.valueOf(process.getWorkflowState() == null ? placeholder : process.getWorkflowState().getTranslation()));
        //only do stuff if process hospital
        if (process != null && process instanceof TWmProcessHospital) {
        //20200204-AWi: remove tooltip implementation, tooltips cause memory leak and its current form there are useless
        //its only shown what is already displayed in the combobox
//            Tooltip toolTip = new Tooltip();
//            toolTip.setWrapText(true);
//            cbTypeValue.setOnMouseEntered((MouseEvent event) -> {
//                toolTip.show(cbTypeValue, event.getScreenX() + 10, event.getScreenY() + 10);
//            });
//            cbTypeValue.setOnMouseExited((MouseEvent event) -> {
//                toolTip.hide();
//            });

            //enable control (disabled by default)
            cbTypeValue.setDisable(false);
            //only load list once
            //TODO: check client cache for list in the future
            if (cbTypeValue.getItems().isEmpty()) {
                //costly load?
                List<CWmListProcessTopic> availableProcessTopics = serviceFacade.getAvailableProcessTopics();
                //CPX-1193 ProcessTopics are sortes  by wmPtSort 
                cbTypeValue.setItems(FXCollections.observableArrayList(availableProcessTopics));
            }
            //set topic according to value stored -> by ident
            Long topic = ((TWmProcessHospital) process).getProcessTopic();
            if (topic != null) {
                for (CWmListProcessTopic top : cbTypeValue.getItems()) {
                    if (top.getWmPtInternalId() == topic) {
                        cbTypeValue.getSelectionModel().getSelectedItem();
                        cbTypeValue.getSelectionModel().select(top);
                        break;
                    }
                }
            }
            //if no topic is found in list,
            //due to invalid or deleted 
            //set old value
            if (cbTypeValue.getSelectionModel().getSelectedItem() == null) {
                //only search in cache if at this point nothing is already selected
                CWmListProcessTopic oldTopic = serviceFacade.getProcessTopic(topic);
                if (oldTopic != null) {
                    cbTypeValue.getSelectionModel().select(oldTopic);
                }
            }

//            toolTip.setText(cbTypeValue.getSelectionModel().getSelectedItem() != null ? cbTypeValue.getSelectionModel().getSelectedItem().getWmPtName() : "");
//            cbTypeValue.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends CWmListProcessTopic> ov, CWmListProcessTopic oldProcessTopic, CWmListProcessTopic newProcessTopic) -> {
//                toolTip.setText(newProcessTopic == null ? null : newProcessTopic.getWmPtName());
//            });

        }

        if (process != null) {
        //20200204-AWi: remove tooltip implementation, tooltips cause memory leak and its current form there are useless
        //its only shown what is already displayed in the combobox
//            Tooltip toolTip = new Tooltip();
//            toolTip.setWrapText(true);
//            cbAssignedUserValue.setOnMouseEntered((MouseEvent event) -> {
//                toolTip.show(cbAssignedUserValue, event.getScreenX() + 10, event.getScreenY() + 10);
//            });
//
//            cbAssignedUserValue.setOnMouseExited((MouseEvent event) -> {
//                toolTip.hide();
//            });
//            cbAssignedUserValue.setTooltip(tip);
            if (cbAssignedUserValue.getItems().isEmpty()) {
                // get all active users (valid and undeleted)
                Set<Map.Entry<Long, UserDTO>> userMapEntries = MenuCache.instance().getValidUserMapEntries();
                userMapEntries.forEach((Map.Entry<Long, UserDTO> userDto) -> {
                    cbAssignedUserValue.getItems().add(userDto.getValue());
                });
            }

            //set Assigned User according to value stored -> by ident
            Long assignedUserId = process.getAssignedUser();

            if (assignedUserId != null) {
                for (UserDTO assignedUser : cbAssignedUserValue.getItems()) {
                    if (Objects.equals(assignedUser.getId(), assignedUserId)) {
                        cbAssignedUserValue.getSelectionModel().getSelectedItem();
                        cbAssignedUserValue.getSelectionModel().select(assignedUser);
                        break;
                    }
                }
            }
            if (cbAssignedUserValue.getSelectionModel().getSelectedItem() == null) {
                //if no User found in the list, due to invalid or deleted entries, set the old value
                UserDTO oldActiveUser = MenuCache.instance().getUserMapEntryForId(assignedUserId);//serviceFacade.getActiveUser(assignedUserId);
                if (oldActiveUser != null) {
                    cbAssignedUserValue.getSelectionModel().select(oldActiveUser);
                }
            }
//            if (cbAssignedUserValue.getSelectionModel().getSelectedItem() != null) {
//                toolTip.setText(cbAssignedUserValue.getSelectionModel().getSelectedItem() != null ? cbAssignedUserValue.getSelectionModel().getSelectedItem().getUserName() : "");
//            }
//            cbAssignedUserValue.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends UserDTO> ov, UserDTO oldUserDto, UserDTO newUserDto) -> {
//                toolTip.setText(newUserDto == null ? null : newUserDto.getUserName());
//            });

            updateHeaderPosition();
        }
    }
    @Override
    public boolean close() {
        serviceFacade.unlockCurrentProcess();
        if(disposeAdapter(propAdapter)){
            propAdapter = null;
        }
        if(disposeAdapter(mapAdapter)){
            mapAdapter = null;
        }
        for(CaseTab openCase : getCaseTabs()){
            openCase.close();
        }
        tbpOpenContent.getTabs().clear();
        cbTypeValue.setConverter(null);
//        cbTypeValue.getSelectionModel().selectedItemProperty().removeListener(typeChangeListener);
        cbAssignedUserValue.setConverter(null);
//        cbAssignedUserValue.getSelectionModel().selectedItemProperty().removeListener(userChangeListener);
//        hBoxMenuWrapper.widthProperty().removeListener(repositionHeaderListener);
        lblLawerFileNumberValue.setOnMouseClicked(null);
        lblLawerFileNumber.setOnMouseClicked(null);
        lblCourtFileNumberValue.setOnMouseClicked(null);
        lblCourtFileNumber.setOnMouseClicked(null);
        btnProcessActions.setGraphic(null);
        btnProcessActions.setOnAction(null);
//        serviceFacade.getProperties().removeListener(PROPERTIES_LISTENER);
//        serviceFacade.getLoadAndShowProperty().removeListener(LOAD_AND_SHOW_LISTENER);
//        typeChangeListener = null;
//        userChangeListener = null;
//        repositionHeaderListener = null;
//        PROPERTIES_LISTENER = null;
//        LOAD_AND_SHOW_LISTENER = null;
        processMasterDetailPane.dispose();
        serviceFacade.dispose();
        return super.close();
    }
    private boolean disposeAdapter(IWeakAdapter<?,?> pAdapter){
        if(pAdapter == null){
            return false;
        }
        pAdapter.dispose();
        return true;
    }
    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        propAdapter = new WeakPropertyAdapter();
        mapAdapter = new WeakMapAdapter();
        setUpControlls();
        cbTypeValue.setConverter(new StringConverter<CWmListProcessTopic>() {
            @Override
            public String toString(CWmListProcessTopic object) {
                return object == null ? "" : object.getWmPtName();
            }

            @Override
            public CWmListProcessTopic fromString(String string) {
                return null;
            }
        });
        //add change listener to react to changes 
        //update item if value changed
        propAdapter.addChangeListener(cbTypeValue.getSelectionModel().selectedItemProperty(),new ChangeListener<CWmListProcessTopic>() {
            @Override
            public void changed(ObservableValue<? extends CWmListProcessTopic> observable, CWmListProcessTopic oldValue, CWmListProcessTopic newValue) {
                //unsafe cast, but changing should only happen when process is hospital case 
                //so no check needed?
                TWmProcessHospital process = (TWmProcessHospital) getProcess();
                if (process == null) {
                    LOG.severe("try to change process topic, but no process is found!");
                    return;
                }
                //if null do not check store and return 
//                if (newValue != null) {
                if (newValue != null && (process.getProcessTopic() == null || newValue.getWmPtInternalId() != process.getProcessTopic())/*process.getProcessTopic() != newValue.getId()*/) {
                    if (!newValue.getWmPtValid() || newValue.isWmPtDeleted()) {
                        //if invalid or deleted, do nothing
                        LOG.warning("selected process topic is invalid or deleted! Can not save value!");
                        return;
                    }
                }
//                }
                //check if current process stores already this value, store only if not
                
                if (newValue != null && (!Objects.equals(newValue.getWmPtInternalId(), process.getProcessTopic()))) {
                    String desc = new ProcessTopicChangedEventSubject(process, oldValue, newValue).getText();
                    //String desc = oldValue == null ? (newValue.getWmPtName() + " als Vorgangstyp gesetzt") : Lang.getEventTypeProcessSubjectChangedDescription(oldValue.getWmPtName(), newValue.getWmPtName());
                    process.setProcessTopic(newValue.getWmPtInternalId());
                    serviceFacade.createAndStoreEvent(WmEventTypeEn.processSubjectChanged, null, desc);
                    serviceFacade.saveOrUpdateProcess(process);

                }
            }
        });
        cbAssignedUserValue.setConverter(new StringConverter<UserDTO>() {
            @Override
            public String toString(UserDTO t) {
                return t == null ? "" : t.getName();
            }

            @Override
            public UserDTO fromString(String string) {
                return null;
            }
        });
        // update assigned user if value changes
        propAdapter.addChangeListener(cbAssignedUserValue.getSelectionModel().selectedItemProperty(),new ChangeListener<UserDTO>() {
            @Override
            public void changed(ObservableValue<? extends UserDTO> observable, UserDTO oldUser, UserDTO newUser) {
                TWmProcess process = getProcess();
                if (newUser == null || !newUser.getId().equals(process.getAssignedUser())) {
                    process.setAssignedUser(newUser != null ? newUser.getId() : 0L);
                    String desc = new ProcessUserChangedEventSubject(process, oldUser, newUser).getText();
                    serviceFacade.createAndStoreEvent(WmEventTypeEn.processUserChanged, null, desc);
                    serviceFacade.saveOrUpdateProcess(process);
                    LOG.log(Level.INFO, "selected assigned user is updated: {0}", newUser != null ? newUser.getUserName() : "");
                }
            }
        });

        //needs to compute new freespace once combobox is resized with new values
        propAdapter.addChangeListener(hBoxMenuWrapper.widthProperty(),new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                StackPane header = (StackPane) tbpOpenContent.lookup(".tab-header-area");
//                if (header != null) {
//                    //compute new Values translate whole header to the left and than set padding to the right
//                    //not really nice, but found no other way, should be changed to a more clear way
//                    //tbMenu is normaly not present in that controller .. can be solved maybe with an lookup on this controller parent?!
//                    header.setTranslateX(-1.0d * newValue.intValue());
//                    header.setPadding(new Insets(0, 0, 0, newValue.doubleValue() + hBoxMetaData.getWidth()));//tbMenu.getPrefWidth()));
//                }
                updateHeaderPosition(tbpOpenContent, newValue.doubleValue(), hBoxMetaData.getWidth());
            }
        });

        //set menu as popover
        lblLawerFileNumberValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TWmProcess process = getProcess();
                if (process == null) {
                    event.consume();
                    return;
                }
                editLawerFileNumer(process);
            }

        });
        lblLawerFileNumber.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TWmProcess process = getProcess();
                if (process == null) {
                    event.consume();
                    return;
                }
                editLawerFileNumer(process);
            }

        });

        lblCourtFileNumberValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TWmProcess process = getProcess();
                if (process == null) {
                    event.consume();
                    return;
                }
                editCourtFileNumber(process);
            }

        });
        lblCourtFileNumber.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TWmProcess process = getProcess();
                if (process == null) {
                    event.consume();
                    return;
                }
                editCourtFileNumber(process);
            }

        });
        btnProcessActions.setText(Lang.getProcessSelectTask());
        btnProcessActions.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
        //set menu as popover
        btnProcessActions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PopOver menu = new AutoFitPopOver();
                menu.setDetachable(false);
                menu.setAutoFix(true);
                menu.setAutoHide(true);
                menu.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                Button btnCompleteProcess = new Button();
                btnCompleteProcess.setAlignment(Pos.CENTER_LEFT);
                RoleProperties property = Session.instance().getRoleProperties();
                boolean canOpenFinalisation = property.modules.processManagement.rights.canDoFinalisation();
                if (!canOpenFinalisation) {
                    btnCompleteProcess.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), true);
                }
                btnCompleteProcess.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FLAG));
                btnCompleteProcess.setText(Lang.getProcessCompletion());
                btnCompleteProcess.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ItemEventHandler eh = new WmProcessFinalisationOperations(serviceFacade).openItem(serviceFacade.getCurrentProcessFinalisation());
                        if (eh != null) {
                            eh.handle(event);
                        }
                        menu.hide();
                    }
                });
                
                Button btnPauseProcess = null;
                if(!serviceFacade.isProcessClosed()){
                    btnPauseProcess = new Button();
                    btnPauseProcess.setStyle("-fx-alignment:BOTTOM_LEFT;");
                    btnPauseProcess.setMaxWidth(Double.MAX_VALUE);
                    if (!canOpenFinalisation) {
                        btnPauseProcess.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), true);
                    }
                    boolean isPaused = serviceFacade.isProcessPaused();
                    btnPauseProcess.setGraphic(isPaused?ResourceLoader.getGlyph(FontAwesome.Glyph.PLAY):ResourceLoader.getGlyph(FontAwesome.Glyph.PAUSE));
                    btnPauseProcess.setText(isPaused?"Vorgang fortsetzen":"Vorgang pausieren");
                    btnPauseProcess.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            menu.hide();
                            boolean isPaused = serviceFacade.isProcessPaused();
                            ConfirmDialog diag = new ConfirmDialog(MainApp.getWindow(), isPaused?"Wollen Sie den Vorgang wirklich fortsetzen?":"Wollen Sie den Vorgang wirklich pausieren?");
                            diag.showAndWait().ifPresent(new Consumer<ButtonType>() {
                                @Override
                                public void accept(ButtonType t) {
                                    if(ButtonType.YES.equals(t)){
                                        if(isPaused){
                                            if(!serviceFacade.continueProcess()){
                                                MainApp.showErrorMessageDialog("Der Vorgang konnte nicht fortgesetzt werden!");
                                            }else{
                                                serviceFacade.getProperties().put(REFRESH_SUMMARY, null);
                                            }
                                        }else{
                                            if(!serviceFacade.pauseProcess()){
                                                MainApp.showErrorMessageDialog("Der Vorgang konnte nicht pausiert werden!");
                                            }else{
                                                serviceFacade.getProperties().put(REFRESH_SUMMARY, null);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
                VBox menuContent = new VBox(btnCompleteProcess);
                if(btnPauseProcess != null){
                    menuContent.getChildren().add(0,btnPauseProcess);
                }
                menu.setContentNode(menuContent);
                menu.show(btnProcessActions, 0);
            }
        });
    }

    /**
     * sets process and setUpContent, should be moved outside i guess, is data
     * access, should not be done in the part were the ui is modified/set start
     * serviceFacade
     *
     * @param processId process to be shown
     * @throws LockException throws if process is locked
     */
    public void setProcess(Long processId) throws LockException {

        serviceFacade = new ProcessServiceFacade(processId, false);
        serviceFacade.lockProcess(processId);
        mapAdapter.addChangeListener(serviceFacade.getProperties(),new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded() && REFRESH_SUMMARY.equals(change.getKey())) {
                    setupSummary();
                    serviceFacade.getProperties().remove(REFRESH_SUMMARY);
                }
            }
        });
        processMasterDetailPane = new ProcessMasterDetailPane(serviceFacade);
        createAndAddNewProcessTab(processMasterDetailPane);
//        serviceFacade.setProcessmasterDetailPane(processMasterDetailPane);
        
        // add new tab
        propAdapter.addChangeListener(serviceFacade.getLoadAndShowProperty(),new ChangeListener<ShowTabAction>() {
            @Override
            public void changed(ObservableValue<? extends ShowTabAction> observable, ShowTabAction oldValue, ShowTabAction newValue) {
                if (newValue != null) {
                    showTab(newValue);
                }
            }
        });

        setupSummary();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //keep away from main thread
                serviceFacade.checkKain6WeeksDeadline();
            }
        });
    }

    /**
     * get the process managed by the controller
     *
     * @return current Process
     */
    public TWmProcess getProcess() {
        return serviceFacade.getCurrentProcess();
    }

    public void createAndAddNewProcessTab(Node content) {
        TwoLineTab tab = new POverviewTab(serviceFacade);
        tab.setContent(content);
        clearTabs();
        addTab(tab);
    }

    public void addTab(Tab tab) {
        tbpOpenContent.getTabs().add(tab);
    }

    public void clearTabs() {
        tbpOpenContent.getTabs().clear();
    }

    public List<CaseTab> getCaseTabs() {
        List<CaseTab> tabs = new ArrayList<>();
        for (TwoLineTab tab : getTabs(TabType.CASE)) {
            tabs.add((CaseTab) tab);
        }
        return tabs;
    }

    public List<TwoLineTab> getTabs(final TabType pTabType) {
        List<TwoLineTab> tabs = new ArrayList<>();
        if (pTabType == null) {
            return tabs;
        }
        for (Tab tab : tbpOpenContent.getTabs()) {
            if (!(tab instanceof TwoLineTab)) {
                continue;
            }
            TwoLineTab t = (TwoLineTab) tab;
            if (pTabType.equals(t.getTabType())) {
                tabs.add(t);
            }
        }
        return tabs;
    }

    public boolean closeTab(Tab tab) {
        if (tab instanceof TwoLineTab) {
            ((TwoLineTab) tab).close();
        }
        return tbpOpenContent.getTabs().remove(tab);
    }

    @Override
    public void reload() {
        super.reload();
        if(processMasterDetailPane.getHistorySection() == null){
            return;
        }
        processMasterDetailPane.getHistorySection().reload();
    }

    private void showTab(ShowTabAction newValue) {
        for (Tab tab : tbpOpenContent.getTabs()) {
            if (tab instanceof TwoLineTab) {
                if (((TwoLineTab) tab).getTabType().equals(newValue.getType())) {
                    if (tab.getId().equals(String.valueOf(newValue.getId()))) {
                        tbpOpenContent.getSelectionModel().select(tab);
                        return;
                    }
                }
            }
        }
        TwoLineTab tab = createNewTab(newValue);
        if (tab != null) {
            if (tab.isClosed()) {
                LOG.log(Level.WARNING, "tab was closed and cannot be opened");
            } else {
                tbpOpenContent.getTabs().add(tab);
                tbpOpenContent.getSelectionModel().select(tab);
            }
        } else {
            LOG.log(Level.WARNING, "tab is null!");
        }
    }

    private TwoLineTab createNewTab(ShowTabAction tabToShow) {
        try {
            switch (tabToShow.getType()) {
                case CASE:
                    CaseTab cTab = new CaseTab(tabToShow.getId());//newValue.getId());
                    if(cTab != null && cTab.getContentScene() != null){
                        cTab.getContentScene().setParentTabPane(tbpOpenContent);
                    }
                    return cTab;
                case PATIENT:
                    PatientTab pTab = new PatientTab(tabToShow.getId(),serviceFacade.getPatient().getPatNumber());
                    return pTab;
                case PROCESSCOMPLETION:
                    ProcessCompletionTab compTab = new ProcessCompletionTab(tabToShow.getId());
                    compTab.initFacade(serviceFacade);
                    compTab.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                            if (t1) {
                                //CPX-1447, reload data if tab becomes selected to show newest data/grouping results
                                compTab.reload();
                            }
                        }
                    });
                    return compTab;
                default:
                    return null;
            }
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "tab can not be loaded, reason: " + ex.getMessage(), ex);
            AlertDialog dialog = AlertDialog.createErrorDialog(ex.toString(), MainApp.getWindow());
            dialog.show();
            return null;
        }
    }

    private void setUpControlls() {
        lblProcessNumber = new Label();
        lblProcessNumber.setVisible(Session.instance().isWmMainFrameWVNumber());
        lblProcessNumber.setStyle("-fx-text-fill: #cccccc");
        lblProcessNumberValue = new Label();
        lblProcessNumberValue.setVisible(Session.instance().isWmMainFrameWVNumber());

        lblStatus = new Label();
        lblStatus.setVisible(Session.instance().isWmMainFrameState());
        lblStatus.setStyle("-fx-text-fill: #cccccc");
        lblStatusValue = new Label();
        lblStatusValue.setPrefWidth(64.0);
        lblStatusValue.setVisible(Session.instance().isWmMainFrameState());

        lblLawerFileNumber = new Label();
        lblLawerFileNumber.setVisible(Session.instance().isWmMainFrameFNLawer());
        lblLawerFileNumber.setStyle("-fx-text-fill: #cccccc");
        lblLawerFileNumberValue = new Label();
        lblLawerFileNumberValue.setVisible(Session.instance().isWmMainFrameFNLawer());

        lblCourtFileNumber = new Label();
        lblCourtFileNumber.setVisible(Session.instance().isWmMainFrameFNCourt());
        lblCourtFileNumber.setStyle("-fx-text-fill: #cccccc");
        lblCourtFileNumberValue = new Label();
        lblCourtFileNumberValue.setVisible(Session.instance().isWmMainFrameFNCourt());

        lblAssignedUser = new Label();
        lblAssignedUser.setVisible(Session.instance().isWmMainFrameWvUser());
        lblAssignedUser.setStyle("-fx-text-fill: #cccccc");
        cbAssignedUserValue = new ComboBox<>();
        cbAssignedUserValue.getStyleClass().add("combo-box-top-pane");
        cbAssignedUserValue.setVisible(Session.instance().isWmMainFrameWvUser());

        lblType = new Label();
        lblType.setVisible(Session.instance().isWmMainFrameSubject());
        lblType.setStyle("-fx-text-fill: #cccccc");
        cbTypeValue = new ComboBox<>();
        cbTypeValue.setVisible(Session.instance().isWmMainFrameSubject());
        cbTypeValue.getStyleClass().add("combo-box-top-pane");

        int elemetIndex = 0, columnIndex = 0, columnIndex1 = 0;
        if (Session.instance().isWmMainFrameWVNumber()) {
            gpWmMainFrame.add(lblProcessNumber, columnIndex++, elemetIndex % 2 == 0 ? 0 : 1);
            gpWmMainFrame.add(lblProcessNumberValue, columnIndex++, elemetIndex++ % 2 == 0 ? 0 : 1);
            gpWmMainFrame.addColumn(columnIndex++);
        } else {
            elemetIndex++;
        }
        if (Session.instance().isWmMainFrameState()) {
            gpWmMainFrame.add(lblStatus, columnIndex1++, elemetIndex % 2 == 0 ? 0 : 1);
            gpWmMainFrame.add(lblStatusValue, columnIndex1++, elemetIndex++ % 2 == 0 ? 0 : 1);
            gpWmMainFrame.addColumn(columnIndex1++);
        } else {
            elemetIndex++;
        }
        if (Session.instance().isWmMainFrameFNLawer()) {
            gpWmMainFrame.add(lblLawerFileNumber, columnIndex++, elemetIndex % 2 == 0 ? 0 : 1);
            gpWmMainFrame.add(lblLawerFileNumberValue, columnIndex++, elemetIndex++ % 2 == 0 ? 0 : 1);
            gpWmMainFrame.addColumn(columnIndex++);
        } else {
            elemetIndex++;
        }
        if (Session.instance().isWmMainFrameFNCourt()) {
            gpWmMainFrame.add(lblCourtFileNumber, columnIndex1++, elemetIndex % 2 == 0 ? 0 : 1);
            gpWmMainFrame.add(lblCourtFileNumberValue, columnIndex1++, elemetIndex++ % 2 == 0 ? 0 : 1);
            gpWmMainFrame.addColumn(columnIndex1++);
        } else {
            elemetIndex++;
        }
        if (Session.instance().isWmMainFrameSubject()) {
            gpWmMainFrame.add(lblType, columnIndex++, elemetIndex % 2 == 0 ? 0 : 1);
            gpWmMainFrame.add(cbTypeValue, columnIndex++, elemetIndex++ % 2 == 0 ? 0 : 1);
            gpWmMainFrame.addColumn(columnIndex++);
        } else {
            elemetIndex++;
        }
        if (Session.instance().isWmMainFrameWvUser()) {
            gpWmMainFrame.add(lblAssignedUser, columnIndex1++, elemetIndex % 2 == 0 ? 0 : 1);
            gpWmMainFrame.add(cbAssignedUserValue, columnIndex1++, elemetIndex++ % 2 == 0 ? 0 : 1);
            gpWmMainFrame.addColumn(columnIndex1++);
        } else {
            elemetIndex++;
        }
        if (gpWmMainFrame.getChildren().isEmpty()) {
            hBoxMetaData.getChildren().remove(gpWmMainFrame);
        }
    }

    private void editLawerFileNumer(TWmProcess process) {
        LabeledTextField tfLawerFileNumberValue = new LabeledTextField();
        tfLawerFileNumberValue.setMaxSize(25);
        tfLawerFileNumberValue.setText(process.getLawerFileNumber());
        tfLawerFileNumberValue.setTitle(Lang.getLawerFileNumber());
        VBox vBox = new VBox();
        vBox.getChildren().addAll(tfLawerFileNumberValue);
        vBox.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
        PopOver popover = new AutoFitPopOver();
        popover.setHideOnEscape(true);
        popover.setAutoHide(true);
        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popover.setDetachable(false);
        popover.setContentNode(vBox);
        popover.show(lblLawerFileNumberValue);
        popover.getContentNode().setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                process.setLawerFileNumber(tfLawerFileNumberValue.getText());
                serviceFacade.saveOrUpdateProcess(process);
                lblLawerFileNumberValue.setText(process.getLawerFileNumber() == null ? " " : String.valueOf(process.getLawerFileNumber()));
                popover.hide(Duration.ZERO);

            }
        });
        popover.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                if (!Objects.requireNonNullElse(tfLawerFileNumberValue.getText(),"").equals(process.getLawerFileNumber())) {
                    process.setLawerFileNumber(tfLawerFileNumberValue.getText());
                    serviceFacade.saveOrUpdateProcess(process);
                    lblLawerFileNumberValue.setText(process.getLawerFileNumber() == null ? " " : String.valueOf(process.getLawerFileNumber()));
                }
                updateHeaderPosition();
            }
        });
        popover.ownerWindowProperty().get().setOnCloseRequest((WindowEvent wevent) -> {
            popover.hide(Duration.ZERO);
        });
    }

    private void editCourtFileNumber(TWmProcess process) {
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        LabeledTextField tfCourtFileNumberValue = new LabeledTextField();
        tfCourtFileNumberValue.setText(process.getCourtFileNumber());
        tfCourtFileNumberValue.setMaxSize(25);
        tfCourtFileNumberValue.setTitle(Lang.getCourtFileNumber());
        vBox.getChildren().addAll(tfCourtFileNumberValue);
        vBox.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
        PopOver popover = new AutoFitPopOver();
        popover.setHideOnEscape(true);
        popover.setAutoHide(true);
        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popover.setDetachable(false);
        popover.setContentNode(vBox);
        popover.show(lblCourtFileNumberValue);
        popover.getContentNode().setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                process.setCourtFileNumber(tfCourtFileNumberValue.getText());
                serviceFacade.saveOrUpdateProcess(process);
                lblCourtFileNumberValue.setText(process.getCourtFileNumber() == null ? " " : String.valueOf(process.getCourtFileNumber()));
                popover.hide(Duration.ZERO);
            }
        });
        popover.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                if(!Objects.requireNonNullElse(tfCourtFileNumberValue.getText(),"").equals(process.getCourtFileNumber())){
                process.setCourtFileNumber(tfCourtFileNumberValue.getText());
                serviceFacade.saveOrUpdateProcess(process);
                lblCourtFileNumberValue.setText(process.getCourtFileNumber() == null ? " " : String.valueOf(process.getCourtFileNumber()));
            }
            updateHeaderPosition();
            }
        });
        popover.ownerWindowProperty().get().setOnCloseRequest((WindowEvent wevent) -> {
            popover.hide(Duration.ZERO);
        });
    }

    public ComboBox<UserDTO> getCbAssignedUserValue() {
        return cbAssignedUserValue;
    }

    public ComboBox<CWmListProcessTopic> getCbTypeValue() {
        return cbTypeValue;
    }

    private void updateHeaderPosition() {
        //trigger resize??
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                updateHeaderPosition(tbpOpenContent, hBoxMenuWrapper.getWidth(), hBoxMetaData.getWidth());
            }
        });
    }

    public void clearFacade() {
        serviceFacade.setCurrentProcess(null);
        serviceFacade = null;
    }
}

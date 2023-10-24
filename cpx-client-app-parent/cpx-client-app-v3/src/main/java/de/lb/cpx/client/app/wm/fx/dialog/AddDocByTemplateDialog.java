/* 
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.editor.CatalogValidationResult;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.app.wm.util.bookmarksmapping.CreateBookmarksHashMap;
import de.lb.cpx.client.app.wm.util.doctemplate.SaveWordDocDialog;
import de.lb.cpx.client.app.wm.util.doctemplate.TemplateController;
import de.lb.cpx.client.app.wm.util.doctemplate.TemplateGenerationResult;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialogSkin;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.masterdetail.ListViewMasterDetailPane;
import de.lb.cpx.client.core.model.fx.masterdetail.MasterDetailBorderPane;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.CategoryEn;
import de.lb.cpx.server.commonDB.model.CDrafts;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.server.commonDB.model.CWmListDraftType;
import de.lb.cpx.service.ejb.TemplateServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javax.ejb.AsyncResult;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 * @author nandola
 */
public class AddDocByTemplateDialog extends FormularDialog<File> {

    private static final Logger LOG = Logger.getLogger(AddDocByTemplateDialog.class.getName());
    private static final String LIST_VIEW_STYLE = "stay-selected-list-view";

    private final EjbProxy<TemplateServiceBeanRemote> templateServiceBean;
    private final TCase hCase;
    private final ProcessServiceFacade facade;
    private final TWmProcess currentProcess;
    private List<CDrafts> result = new ArrayList<>();
    private List<CDrafts> filtertResult = new ArrayList<>();
    //private MdTemplateList mdDraftType;
    private AsyncListView<CDrafts> mdDraftType;
    private LabeledComboBox<CWmListDocumentType> cbDocType;
    private LabeledCheckComboBox<CWmListDraftType> cbDraftCat1;
    private LabeledCheckComboBox<CWmListDraftType> cbDraftCat2;
    private LabeledCheckComboBox<CWmListDraftType> cbDraftCat3;
    private LabeledTextField docName;
    private CheckBox lckAllTemplte = new CheckBox("Alle anzeigen");
    
    private ChangeListener<Boolean> ALL_TEMPLATE_LISTENER = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (lckAllTemplte.isSelected()) {
                    mdDraftType.setItems(FXCollections.observableArrayList(result));
                    lckAllTemplte.setSelected(true);

                } else {
                    mdDraftType.setItems(FXCollections.observableArrayList(getNewTemplateListe()));
                    lckAllTemplte.setSelected(false);
                }
            }
        };
    private ChangeListener<Boolean> DOC_NAME_FOCUS_LISTENER = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
            if (!t1) { //when focus lost
                setFormatDocName(docName.getText());

            }
        }
    };
    private EventHandler<MouseEvent> DOC_NAME_MOUSE_EVENT  = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setFormatDocName(docName.getText());
            }
        };
    private EventHandler<KeyEvent> DOC_NAME_KEY_EVENT = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.TAB)) {

                    setFormatDocName(docName.getText());
                }
            }
        };
    private ListChangeListener<CWmListDraftType> DRAFT_1_CHECK_LISTENER = new ListChangeListener<CWmListDraftType>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CWmListDraftType> c) {
                lckAllTemplte.setSelected(false);
                mdDraftType.setItems(FXCollections.observableArrayList(getNewTemplateListe()));

            }
        };
    private ListChangeListener<CWmListDraftType> DRAFT_1_TEXT_UPDATE_LISTENER = new ListChangeListener<CWmListDraftType>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CWmListDraftType> c) {
                List<CWmListDraftType> listDistinct = cbDraftCat1.getCheckModel().getCheckedItems().stream().distinct().collect(Collectors.toList());
                cat1Tip.setText(listDistinct.toString().substring(1, listDistinct.toString().length() - 1));
            }
        };
    private ListChangeListener<CWmListDraftType> DRAFT_2_CHECK_LISTENER = new ListChangeListener<CWmListDraftType>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CWmListDraftType> c) {
                lckAllTemplte.setSelected(false);
                mdDraftType.setItems(FXCollections.observableArrayList(getNewTemplateListe()));

            }
        };
    private ListChangeListener<CWmListDraftType> DRAFT_2_TEXT_UPDATE_LISTENER = new ListChangeListener<CWmListDraftType>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CWmListDraftType> c) {
                List<CWmListDraftType> listDistinct = cbDraftCat2.getCheckModel().getCheckedItems().stream().distinct().collect(Collectors.toList());
                cat2Tip.setText(listDistinct.toString().substring(1, listDistinct.toString().length() - 1));

            }
        };
    private ListChangeListener<CWmListDraftType> DRAFT_3_CHECK_LISTENER = new ListChangeListener<CWmListDraftType>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CWmListDraftType> c) {
                lckAllTemplte.setSelected(false);
                mdDraftType.setItems(FXCollections.observableArrayList(getNewTemplateListe()));
            }
        };
    private ListChangeListener<CWmListDraftType> DRAFT_3_TEXT_UPDATE_LISTENER = new ListChangeListener<CWmListDraftType>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CWmListDraftType> c) {
                List<CWmListDraftType> listDistinct = cbDraftCat3.getCheckModel().getCheckedItems().stream().distinct().collect(Collectors.toList());
                cat3Tip.setText(listDistinct.toString().substring(1, listDistinct.toString().length() - 1));
            }
        };
    private Tooltip cat1Tip;
    private Tooltip cat2Tip;
    private Tooltip cat3Tip;
    private CatalogValidationResult catalogValidationResult;
    
    
    public AddDocByTemplateDialog(final ProcessServiceFacade pFacade, final TWmProcess pCurrentProcess) {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, Lang.getTemplateGeneration());
        this.mdDraftType = new AsyncListView<CDrafts>() {
            @Override
            public Future<List<CDrafts>> getFuture() {
                result = templateServiceBean.get().getAllTemplates();
                Collections.sort(result, new Comparator<CDrafts>() {
                    @Override
                    public int compare(final CDrafts lhsDraft, final CDrafts rhsDraft) {
                        final String lhsDraftName = StringUtils.trimToEmpty(lhsDraft.getDraftName()).toLowerCase();
                        final String rhsDraftName = StringUtils.trimToEmpty(rhsDraft.getDraftName()).toLowerCase();
                        return lhsDraftName.compareTo(rhsDraftName);
                    }
                });

                if (Session.instance().getSelectedDraftCategories() == null) {
                    return new AsyncResult<>(result);
                } else {
                    return new AsyncResult<>(getNewTemplateListe());
                }

            }

        };
        facade = pFacade;
        currentProcess = pCurrentProcess;
        //selectedGrouper = CpxClientConfig.instance().getSelectedGrouper();
        //mdkCatalog = CpxMdkCatalog.instance();
        //insuCompaCatalog = CpxInsuranceCompanyCatalog.instance();
        //departmentCatalog = CpxDepartmentCatalog.instance();
        Session session = Session.instance();
        EjbConnector connector = session.getEjbConnector();
        templateServiceBean = connector.connectTemplateServiceBean();
        //processServiceBean = connector.connectProcessServiceBean();
        //caseEJB = connector.connectSingleCaseBean();
        hCase = currentProcess.getMainCase();
        if (hCase == null) {
            //LOG.log(Level.SEVERE, "hCase is null (main case not found for process + " + String.valueOf(currentProcess) + ")!");
            close();
            throw new IllegalStateException("HospitalCase is not found (process " + String.valueOf(currentProcess) + " has no main case)!");
        }
        //List<CWmListDocumentType> listOfDocumentTypes = pFacade.getAllAvailableDocumentTypes(new Date());
        List<CWmListDocumentType> listOfDocumentTypes = MenuCache.getMenuCacheDocumentTypes().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE);
        List<CWmListDraftType> listOfDraftCat1 = pFacade.getAllAvailabledrafttTypes(CategoryEn.Category1);
        List<CWmListDraftType> listOfDraftCat2 = pFacade.getAllAvailabledrafttTypes(CategoryEn.Category2);
        List<CWmListDraftType> listOfDraftCat3 = pFacade.getAllAvailabledrafttTypes(CategoryEn.Category3);
//CPX-1193  Dokumentn Typen  are sortes  by wmDtSort 
//        Collections.sort(listOfDocumentTypes);
        cbDocType = new LabeledComboBox<>(Lang.getDocumentType());
        cbDocType.setItems(listOfDocumentTypes);
        cbDocType.setConverter(new StringConverter<CWmListDocumentType>() {
            @Override
            public String toString(CWmListDocumentType object) {
                return object == null ? "" : object.getWmDtName();
            }

            @Override
            public CWmListDocumentType fromString(String string) {
                return null;
            }
        });
        if (!listOfDocumentTypes.isEmpty()) {
            cbDocType.getControl().getSelectionModel().selectFirst();
        }
        cbDraftCat1 = new LabeledCheckComboBox<>(CategoryEn.Category1.getTranslation().toString());
        cbDraftCat2 = new LabeledCheckComboBox<>(CategoryEn.Category2.getTranslation().toString());
        cbDraftCat3 = new LabeledCheckComboBox<>(CategoryEn.Category3.getTranslation().toString());
        
        if (listOfDraftCat1 != null && !listOfDraftCat1.isEmpty()) {
            cbDraftCat1.getItems().addAll(new ArrayList<>(listOfDraftCat1));
        }
        cbDraftCat1.setConverter(new StringConverter<CWmListDraftType>() {
            @Override
            public String toString(CWmListDraftType object) {
                return object == null ? "" : object.getWmDrtName();
            }

            @Override
            public CWmListDraftType fromString(String string) {
                return null;
            }
        });

        cbDraftCat1.getControl().getCheckModel().getCheckedItems().addListener(DRAFT_1_CHECK_LISTENER);
        if (listOfDraftCat2 != null && !listOfDraftCat2.isEmpty()) {
            cbDraftCat2.getItems().addAll(new ArrayList<>(listOfDraftCat2));
        }
        cbDraftCat2.setConverter(new StringConverter<CWmListDraftType>() {
            @Override
            public String toString(CWmListDraftType object) {
                return object == null ? "" : object.getWmDrtName();
            }

            @Override
            public CWmListDraftType fromString(String string) {
                return null;
            }
        });
        //Category 1 Tooltip
        cat1Tip = new Tooltip();
        cat1Tip.setWrapText(true);
        cat1Tip.setMaxWidth(500);

        cbDraftCat1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!cbDraftCat1.getControl().getCheckModel().isEmpty()) {
                    cat1Tip.show(cbDraftCat1, event.getScreenX() + 10, event.getScreenY() + 10);
                }
            }
        });
        cbDraftCat1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cat1Tip.hide();
            }
        });

        cbDraftCat1.getControl().getCheckModel().getCheckedItems().addListener(DRAFT_1_TEXT_UPDATE_LISTENER);
        
        cbDraftCat2.getControl().getCheckModel().getCheckedItems().addListener(DRAFT_2_CHECK_LISTENER);
        if (listOfDraftCat3 != null && !listOfDraftCat3.isEmpty()) {
            cbDraftCat3.getItems().addAll(new ArrayList<>(listOfDraftCat3));
        }
        cbDraftCat3.setConverter(new StringConverter<CWmListDraftType>() {
            @Override
            public String toString(CWmListDraftType object) {
                return object == null ? "" : object.getWmDrtName();
            }

            @Override
            public CWmListDraftType fromString(String string) {
                return null;
            }
        });
        cbDraftCat3.getControl().getCheckModel().getCheckedItems().addListener(DRAFT_3_CHECK_LISTENER);

        //Category 2 Tooltip
        cat2Tip = new Tooltip();
        cat2Tip.setWrapText(true);
        cat2Tip.setMaxWidth(500);
        cbDraftCat2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!cbDraftCat2.getControl().getCheckModel().isEmpty()) {
                    cat2Tip.show(cbDraftCat2, event.getScreenX() + 10, event.getScreenY() + 10);
                }
            }
        });
        cbDraftCat2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cat2Tip.hide();
            }
        });

        cbDraftCat2.getControl().getCheckModel().getCheckedItems().addListener(DRAFT_2_TEXT_UPDATE_LISTENER);
        //Category 3 Tooltip
        cat3Tip = new Tooltip();
        cat3Tip.setWrapText(true);
        cat3Tip.setMaxWidth(500);

        cbDraftCat3.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!cbDraftCat3.getControl().getCheckModel().isEmpty()) {
                    cat3Tip.show(cbDraftCat3, event.getScreenX() + 10, event.getScreenY() + 10);
                }
            }
        });
        cbDraftCat3.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cat3Tip.hide();
            }
        });

        cbDraftCat3.getControl().getCheckModel().getCheckedItems().addListener(DRAFT_3_TEXT_UPDATE_LISTENER);
//CPX-1383 Preselect entries in combo box from selected Categorys
        long[] catId = Session.instance().getSelectedDraftCategories();
        if (catId != null) {

            for (long cat1Id : catId) {
                for (int i = 0; i < cbDraftCat1.getCheckModel().getItemCount(); i++) {
                    CWmListDraftType item = cbDraftCat1.getCheckModel().getItem(i);
                    if (cat1Id == item.getWmDrtInternalId()) {
                        cbDraftCat1.getCheckModel().check(item);
                    }
                }
                for (int i = 0; i < cbDraftCat2.getCheckModel().getItemCount(); i++) {
                    CWmListDraftType item = cbDraftCat2.getCheckModel().getItem(i);
                    if (cat1Id == item.getWmDrtInternalId()) {
                        cbDraftCat2.getCheckModel().check(item);
                    }
                }
                for (int i = 0; i < cbDraftCat3.getCheckModel().getItemCount(); i++) {
                    CWmListDraftType item = cbDraftCat3.getCheckModel().getItem(i);
                    if (cat1Id == item.getWmDrtInternalId()) {
                        cbDraftCat3.getCheckModel().check(item);
                    }
                }
            }
        }
        docName = new LabeledTextField("Dateiname");
        docName.getControl().getStyleClass().add("cpx-detail-label");
//        docName.setText("Generierter Dateiname");
        docName.setPromptText("Generierter Dateiname");
        docName.getControl().focusedProperty().addListener(DOC_NAME_FOCUS_LISTENER);
        docName.setTooltip(new Tooltip("Geben Sie %f% um ein Fallnummer  , oder %p% um Versichertennummer des Patienten , oder %i% um IKZ der Versicherung zu ermitteln"));
        docName.getControl().setOnMouseExited(DOC_NAME_MOUSE_EVENT);
        docName.getControl().setOnKeyPressed(DOC_NAME_KEY_EVENT);
//        Label label = new Label(Lang.getTemplateAvailability());
        //mdDraftType = new MdTemplateList();
//        VBox wrapperDraftType = new VBox(label,mdDraftType);
        //LabeledMdList<CDrafts> lbldDraftType = new LabeledMdList<>(Lang.getTemplateAvailability(), mdDraftType);
        mdDraftType.setTitle(Lang.getTemplateAvailability());
        mdDraftType.getSkin();
//        //mdDraftType.setPrefSize(300.0d, 300.0d);
//        mdDraftType.getControl().getStyleClass().add(LIST_VIEW_STYLE);

        //ListViewMasterDetailPane<CDrafts> draftPane = new ListViewMasterDetailPane<>(mdDraftType.getListView());
        MdTemplateList templateList = new MdTemplateList(mdDraftType);
        HBox hbDoc = new HBox();
        hbDoc.setSpacing(FormularDialogSkin.CONTROL_SPACING);
        cbDocType.setPrefWidth(150);
        hbDoc.getChildren().addAll(cbDocType, docName);
        HBox.setHgrow(cbDocType, Priority.ALWAYS);
        HBox.setHgrow(docName, Priority.ALWAYS);
        VBox VbDraft = new VBox();
        VbDraft.setSpacing(FormularDialogSkin.CONTROL_SPACING);
        HBox hbDrCat = new HBox();
        hbDrCat.setSpacing(FormularDialogSkin.CONTROL_SPACING);
        hbDrCat.getChildren().addAll(cbDraftCat2, cbDraftCat3);
        HBox.setHgrow(cbDraftCat2, Priority.ALWAYS);
        HBox.setHgrow(cbDraftCat3, Priority.ALWAYS);
        VBox.setVgrow(hbDrCat, Priority.ALWAYS);
        VBox.setVgrow(cbDraftCat1, Priority.ALWAYS);
        HBox hbTitel = new HBox();
        hbTitel.setSpacing(380);
        hbTitel.setAlignment(Pos.BOTTOM_LEFT);
        Label lbTitel = new Label("Den Kategorien zugeordnete Vorlagen ");
        lbTitel.getStyleClass().add("cpx-detail-label");
        hbTitel.getChildren().addAll(lbTitel, lckAllTemplte);
        VBox wrapper = new VBox(cbDraftCat1, hbDrCat, hbDoc, hbTitel, new Separator(Orientation.HORIZONTAL), templateList);
        wrapper.setPrefWidth(700);
        wrapper.setFillWidth(true);
        wrapper.setSpacing(FormularDialogSkin.CONTROL_SPACING);
        addControlGroup(wrapper, true);
        lckAllTemplte.selectedProperty().addListener(ALL_TEMPLATE_LISTENER);
        //Button ok = getDialogSkin().getButton(ButtonType.OK);
        //disable on button if nothing is selected
        //ok.disableProperty().bind(Bindings.or(mdDraftType.getSelectedItemProperty().isNull(), cbDocType.getSelectedItemProperty().isNull()));
//        mdDraftType.reload();
        ((AsyncListView) mdDraftType).reload();

//        mdDraftType.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (MouseButton.PRIMARY == event.getButton()
//                        && event.getClickCount() == 2) {
//                    onSave(event);
//                }
//            }
//        });
//
//        mdDraftType.getSelectedItemProperty().addListener(new ChangeListener<CDrafts>() {
//            @Override
//            public void changed(ObservableValue<? extends CDrafts> observable, CDrafts oldValue, CDrafts newValue) {
//                //setDetail(createDetailPane(newValue));
//            }
//        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                validationSupport.initInitialDecoration();
                validationSupport.registerValidator(docName.getControl(), new Validator<String>() {
                    @Override
                    public ValidationResult apply(Control t, String u) {
                        // check docName with valid characters
                        ValidationResult res = new ValidationResult();
                        final String documentName = getDocumentName();
                        final boolean isMissing = documentName.isEmpty();
                        final boolean isValid = !isMissing && !DocumentManager.isValidFilepath(documentName);
                        res.addErrorIf(t, "Bitte geben Sie einen Dateinamen an", isMissing);
                        res.addErrorIf(t, "'" + documentName + "' ist aufgrund von Sonderzeichen kein gültiger Dateiname", isValid);
                        return res;
                    }
                });
                validationSupport.registerValidator(mdDraftType, new Validator<Object>() {
                    @Override
                    public ValidationResult apply(Control t, Object u) {
                        ValidationResult res = new ValidationResult();
                        res.addErrorIf(t, "Bitte wählen Sie eine Vorlage aus", mdDraftType.getSelectionModel().getSelectedItem() == null);
                        return res;
                    }
                });
                //        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
//            @Override
//            public boolean test(Control t) {
//                return t instanceof TreeCell;
//            }
//                validationSupport.registerValidator(mdDraftType.getListView(), new Validator<ObservableList>() {
//                    @Override
//                    public ValidationResult apply(Control t, ObservableList u) {
//                        LOG.log(Level.INFO, "Selected Item: " + String.valueOf(mdDraftType.getSelectedItem()));
//                        ValidationResult res = new ValidationResult();
//                        res.addErrorIf(t, "Bitte wählen Sie eine Vorlage aus", mdDraftType.getSelectedItem() == null);
//                        return res;
//                    }
//                });
            }
        });
        
        getWindow().addEventFilter(WindowEvent.WINDOW_HIDDEN, new EventHandler() {
            @Override
            public void handle(Event t) {
                getWindow().removeEventFilter(WindowEvent.WINDOW_HIDDEN, this);
                lckAllTemplte.selectedProperty().removeListener(ALL_TEMPLATE_LISTENER);
                templateList.getListView().setOnMouseClicked(null);
                docName.getControl().setOnMouseExited(null);
                docName.getControl().setOnKeyPressed(null);
                docName.getControl().focusedProperty().removeListener(DOC_NAME_FOCUS_LISTENER);
                cbDocType.setConverter(null);
                
                cbDraftCat1.getControl().getCheckModel().getCheckedItems().removeListener(DRAFT_1_CHECK_LISTENER);
                cbDraftCat1.getControl().getCheckModel().getCheckedItems().removeListener(DRAFT_1_TEXT_UPDATE_LISTENER);
                cbDraftCat1.setOnMouseEntered(null);
                cbDraftCat1.setOnMouseExited(null);
                cbDraftCat1.setConverter(null);
                
                cbDraftCat2.getControl().getCheckModel().getCheckedItems().removeListener(DRAFT_2_CHECK_LISTENER);
                cbDraftCat2.getControl().getCheckModel().getCheckedItems().removeListener(DRAFT_2_TEXT_UPDATE_LISTENER);
                cbDraftCat2.setOnMouseEntered(null);
                cbDraftCat2.setOnMouseExited(null);
                cbDraftCat2.setConverter(null);
                
                cbDraftCat3.getControl().getCheckModel().getCheckedItems().removeListener(DRAFT_3_CHECK_LISTENER);
                cbDraftCat3.getControl().getCheckModel().getCheckedItems().removeListener(DRAFT_3_TEXT_UPDATE_LISTENER);
                cbDraftCat3.setOnMouseEntered(null);
                cbDraftCat3.setOnMouseExited(null);
                cbDraftCat3.setConverter(null);
                
                cat1Tip = null;
                cat2Tip = null;
                cat3Tip = null;
                ALL_TEMPLATE_LISTENER = null;
                DOC_NAME_FOCUS_LISTENER = null;
                DRAFT_1_CHECK_LISTENER = null;
                DRAFT_1_TEXT_UPDATE_LISTENER = null;
                DRAFT_2_CHECK_LISTENER = null;
                DRAFT_2_TEXT_UPDATE_LISTENER = null;
                DRAFT_3_CHECK_LISTENER = null;
                DRAFT_3_TEXT_UPDATE_LISTENER = null;
                
                templateList.dispose();
                getDialogPane().setContent(null);
            }
        });
        
        catalogValidationResult = new CatalogValidationResult();
        catalogValidationResult.add(CpxErrorTypeEn.WARNING, Lang.getValidationMasterdataNoDocumentTypesFound(), (t) -> {
            return Objects.requireNonNullElse(listOfDocumentTypes,new ArrayList<>()).isEmpty();
        });
        catalogValidationResult.add(CpxErrorTypeEn.WARNING, Lang.getValidationMasterdataNoDraftTypesForMainCategoryFound(), (t) -> {
            return Objects.requireNonNullElse(listOfDraftCat1,new ArrayList<>()).isEmpty();
        });
        catalogValidationResult.add(CpxErrorTypeEn.WARNING, Lang.getValidationMasterdataNoDraftTypesForSubCategory1Found(), (t) -> {
            return Objects.requireNonNullElse(listOfDraftCat2,new ArrayList<>()).isEmpty();
        });
        catalogValidationResult.add(CpxErrorTypeEn.WARNING, Lang.getValidationMasterdataNoDraftTypesForSubCategory2Found(), (t) -> {
            return Objects.requireNonNullElse(listOfDraftCat3,new ArrayList<>()).isEmpty();
        });
        setMessageText(catalogValidationResult.getValidationMessages());
        setMessageType(catalogValidationResult.getHighestErrorType());
    }
    
    /**
     * Returns placeholder as default if nothing is set
     *
     * @return document name
     */
    private String getDocumentName() {
        final String docNameText = docName.getControl().getText().trim();
//       
        return docNameText.isEmpty() ? docName.getControl().getPromptText().trim() : docNameText;
    }

    private Notifications createBackgroundProcessNotification(final MouseEvent pMouseEvent, final String pDraftName) {
//        if (pMouseEvent == null) {
//            return null;
//        }
        final Notifications notif = NotificationsFactory.instance().createInformationNotification();
        notif.title(Lang.getTemplateWaitNotification_title()) //  "Word Document will be opened soon!"
                .text(Lang.getTemplateWaitNotification_text(pDraftName)) //"Word Document will be generated from selected draft\n '" + pDraftName + "' and opened afterwards!"
                .hideAfter(Duration.seconds(5));

        //2018-05-28 DNi - Ticket #CPX-924: Removing ControlsFX classes to use our custom alert icons
        //notif. .getStyleClass().remove("progress-dialog");
        //notif.getStyleClass().remove("font-selector-dialog");
        //notif.getStyleClass().remove("login-dialog");
        //notif.getStyleClass().remove("command-links-dialog");
        //notif.getStyleClass().remove("exception-dialog");
        //2018-05-28 DNi: Now insert our custom notice icon
        return notif;
    }

//    private PopOver createBackgroundProcessPopOver(final MouseEvent pMouseEvent, final String pDraftName) {
//        if (pMouseEvent == null) {
//            return null;
//        }
//        final PopOver popoverWaitHint = new AutoFitPopOver();
//        popoverWaitHint.setHideOnEscape(true);
//        popoverWaitHint.setAutoHide(true);
//        popoverWaitHint.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
//        popoverWaitHint.setDetachable(false);
//        //popoverWaitHint.setContentNode(new Label("Word Document will be generated from selected draft\n '" + pDraftName + "' and opened afterwards!"));
//        popoverWaitHint.setContentNode(new Label(Lang.getTemplateWaitNotification(pDraftName)));
//        popoverWaitHint.show((Node)pMouseEvent.getSource(), pMouseEvent.getScreenX(), pMouseEvent.getScreenY());
//        return popoverWaitHint;
//    }
    private File onSave(final MouseEvent pMouseEvent) {
        File inOutFile = null;
        if (mdDraftType.getSelectionModel().getSelectedItem() == null) {
            MainApp.showErrorMessageDialog("Wählen Sie eine beliebige Vorlage aus, um ein Dokument zu generieren!");
            return inOutFile;
        }
        final String draftName = mdDraftType.getSelectionModel().getSelectedItem().getDraftName();
        final long draftId = mdDraftType.getSelectionModel().getSelectedItem().getId();

        final String documentName = getDocumentName();

        if (validationSupport.isInvalid()) {
            AlertDialog alertDialog = AlertDialog.createErrorDialog("Bitte korrigieren Sie zunächst Ihre Angaben!", AddDocByTemplateDialog.this.getDialogPane().getScene().getWindow());
            alertDialog.show();
            return inOutFile;
        }

//        final Notifications notif = Notifications.create();
//        notif.owner(MainApp.getWindow())
//                .title("Word Document will be opened soon!")
//                .text("Word Document will be generated from selected draft '" + draftName + " and opened afterwards.\nThis may take same seconds :)'")
//                .hideAfter(Duration.seconds(5))
//                .show();
        //final PopOver popoverWaitHint = createBackgroundProcessPopOver(pMouseEvent, draftName);
        final Notifications notificationWaitHint = createBackgroundProcessNotification(pMouseEvent, draftName);

        LOG.log(Level.INFO, "Starts to create document from this template: " + draftName + " (ID " + draftId + ") with document name '" + documentName + "'...");
        try {
            byte[] byteArray = mdDraftType.getSelectionModel().getSelectedItem().getDraftContent();   //get the content of the selected Draft
            if (byteArray == null) {
                //new if condition: load content from server because field is lazy loading annotated
                byteArray = templateServiceBean.get().getDraftContent(mdDraftType.getSelectionModel().getSelectedItem().getId()); //new method on server!
                mdDraftType.getSelectionModel().getSelectedItem().setDraftContent(byteArray);
            }
            String property = System.getProperty("java.io.tmpdir");  // temp dir on client
            final String wordExtension = TemplateController.getWordExtension(documentName);
//            String createInTempDir = property + "input_" + System.currentTimeMillis() + "." + wordExtension;
            final String fileName = property + DocumentManager.validateFilename(docName.getText()) + "." + wordExtension;
            inOutFile = DocumentManager.createFileInTempOrSpecificDir(byteArray, fileName);
//            inOutFile = new File(createInTempDir);
//            try (//                        fileOuputStream.write(byteArray);
//                     FileOutputStream fileOuputStream = new FileOutputStream(inOutFile);  OutputStream os = new BufferedOutputStream(fileOuputStream)) {
//                os.write(byteArray);
//                os.flush();
//            }

            CWmListDocumentType selectedDocType = cbDocType.getSelectedItem();

            CreateBookmarksHashMap bookmarksHashMap = new CreateBookmarksHashMap();
//            Map<String, String> hashMap = bookmarksHashMap.fillHashMap(facade, hCase, currentProcess);
            Map<String, String> hashMap = bookmarksHashMap.fillHashMap(facade);
            final TemplateGenerationResult tmplGenerationResult = TemplateController.instance().createDocFromTemplate(inOutFile, documentName, selectedDocType, hashMap, currentProcess, facade);
//            final TemplateGenerationResult tmplGenerationResult = TemplateController.createDocFromTemplate(inOutFile, documentName, selectedDocType, fillHashMap(facade, hCase, currentProcess), currentProcess, facade);

            if (notificationWaitHint != null) {
                notificationWaitHint.show();
            }

            tmplGenerationResult.getDocumentGenerationTask().setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    String msg = "Template Generation failed!";
                    Throwable ex = tmplGenerationResult.getDocumentGenerationTask().getException();
                    LOG.log(Level.SEVERE, msg, ex);
                    MainApp.showErrorMessageDialog(ex, "Template Generation failed!");
                    TemplateController.instance().closeWord(tmplGenerationResult.getWord());
                }
            });

            tmplGenerationResult.getDocumentGenerationTask().stateProperty().addListener(new ChangeListener<State>() {
                @Override
                public void changed(ObservableValue<? extends State> observable, State oldState, State newState) {
                    //Close popover with wait notification if task is not running anymore
                    LOG.log(Level.INFO, "Template generation finisded! old state: {0}, new state: {1}", new Object[]{oldState.toString(), newState.toString()});
                    if (notificationWaitHint != null
                            && (State.SUCCEEDED.equals(newState) || State.CANCELLED.equals(newState) || State.FAILED.equals(newState))) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                LOG.log(Level.FINE, "Hide NotificationWaitHint!");
                                //There's no method to close Notifications immediatly!
                                notificationWaitHint.hideAfter(Duration.ZERO);
                                //popoverWaitHint.hide();
                                
                            }
                        });
                    }
                    //Go back to CPX and ask the user if he or she wants to save the document
                    if (State.SUCCEEDED.equals(newState)) {
                        tmplGenerationResult.getDocumentGenerationTask().stateProperty().removeListener(this);
                        SaveWordDocDialog list = new SaveWordDocDialog(tmplGenerationResult);
                        list.showDialog(AddDocByTemplateDialog.this);
                        list.resultProperty().addListener(new ChangeListener<ButtonType>() { // wait for dialog result - show Dialog is async it can happen that dispose is called before dialog is finished!
                            @Override
                            public void changed(ObservableValue<? extends ButtonType> ov, ButtonType t, ButtonType t1) {
                                list.resultProperty().removeListener(this);
                                tmplGenerationResult.getDocumentGenerationTask().setOnFailed(null);
                                tmplGenerationResult.dispose();
                                TemplateController.destroy();
                            }
                        });
                    }
                }
            });
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Was not able to finish process of word document generation with the selected template '" + draftName + "'", ex);
            MainApp.showErrorMessageDialog(ex, "Beim Erzeugen eines Dokuments aus der Wiedervorlage '" + draftName + "' ist ein Fehler aufgetreten");
        }
        return inOutFile;
    }

    @Override
    public File onSave() {
        return onSave(null);
    }

    private class MdTemplateList extends ListViewMasterDetailPane<CDrafts> {
        private ChangeListener<CDrafts> DETAIL_LISTENER = new ChangeListener<CDrafts>() {
                @Override
                public void changed(ObservableValue<? extends CDrafts> observable, CDrafts oldValue, CDrafts newValue) {
                    setDetail(createDetailPane(newValue));
                }
            };
        public MdTemplateList(ListView<CDrafts> pListView) {
            super(pListView);
            getListView().getStyleClass().add(LIST_VIEW_STYLE);
            //set Pref height for lower resolutions
            pListView.setPrefHeight(300);
            setOrientation(MasterDetailBorderPane.DetailOrientation.BOTTOM);
            getSelectedItemProperty().addListener(DETAIL_LISTENER);

            //create empty detail
            setDetail(createDetailPane(null));
            setCellFactory(new Callback<ListView<CDrafts>, ListCell<CDrafts>>() {
                @Override
                public ListCell<CDrafts> call(ListView<CDrafts> param) {
                    return new ListCell<CDrafts>() {
                        @Override
                        protected void updateItem(CDrafts item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setText("");
                                setTooltip(null);
                                return;
                            }
                            setText(item.getDraftName());
                            setGraphic(null);
                            setTooltip(new Tooltip(item.getDraftName()
                                    + ":\r\n(" + item.getDraftFile() + ")"
                                    + (item.getDraftDescription() == null ? "" : "\r\n\r\n" + item.getDraftDescription())));
                        }
                    };
                }
            });

            //Allow double click to generate document from selected template
            getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (MouseButton.PRIMARY == event.getButton()
                            && event.getClickCount() == 2) {
                        onSave(event);
                    }
                }
            });
        }
        public void dispose(){
            getSelectedItemProperty().removeListener(DETAIL_LISTENER);
            DETAIL_LISTENER = null;
            setCellFactory(null);
            getListView().setOnMouseClicked(null);
            getMasterPane().getChildren().clear();
            getDetailPane().getChildren().clear();
        }
        public void reload() {
            ((AsyncListView) getListView()).reload();
        }

        private Parent createDetailPane(CDrafts newValue) {
            VBox box = new VBox();
            box.setPadding(new Insets(10, 0, 0, 0));
            box.setSpacing(5.0);
            box.setPrefHeight(100);
            box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            if (newValue == null) {
                return box;
            }

            ScrollPane sp = new ScrollPane(new Label(newValue.getDraftDescription()));
            VBox.setVgrow(sp, Priority.ALWAYS);
            Label lblDesc = new Label(Lang.getDescription());
            lblDesc.setStyle("-fx-font-weight: bold;");
//            HBox  hbInfo=new HBox();
//         hbInfo.setSpacing(FormularDialogSkin.CONTROL_SPACING);
//        addControlGroup(hbInfo, true);
//            hbInfo.getChildren().addAll(getUserInfoGrid(newValue), new Label("Datei: " + newValue.getDraftFile()));
            VBox spWrapper = new VBox(lblDesc, sp);
            VBox.setVgrow(spWrapper, Priority.ALWAYS);
//            ScrollPane pane = new ScrollPane(getInfoGrid(newValue));
//            pane.setFitToWidth(true);
            box.getChildren().addAll(getInfoGrid(newValue), spWrapper);
            return box;
        }

        private Pane getInfoGrid(CDrafts pComment) {
            GridPane pane = new GridPane();
            pane.setHgap(50);
            Label lbFileText = new Label("Datei: ");
            lbFileText.setStyle("-fx-font-weight: bold");
            Label lbFileDate = new Label(pComment.getDraftFile());
            if (pComment.getFilenameProPosal() != null) {
                docName.setText(pComment.getFilenameProPosal());
                setFormatDocName(pComment.getFilenameProPosal());
            } else {
                docName.setText(pComment.getDraftFile().substring(0, pComment.getDraftFile().indexOf('.')) + "_Vorgang_" + currentProcess.getWorkflowNumber());
            }
            Label lblCreationDateTxt = new Label(Lang.getCreatedOn(""));
            lblCreationDateTxt.setStyle("-fx-font-weight: bold");
            Label lblCreationDate = new Label();
            Label lblCreationUserTxt = new Label(Lang.getCreatedFrom(""));
            lblCreationUserTxt.setStyle("-fx-font-weight: bold");
            Label lblCreationUser = new Label();
            Label lbCategoryText = new Label("Kategorie: ");
            lbCategoryText.setStyle("-fx-font-weight: bold");
            String cat1 = pComment.getCategory1() != null ? MenuCache.instance().getDraftTypesForInternalId(pComment.getCategory1()) : "";
            String cat2 = pComment.getCategory2() != null ? MenuCache.instance().getDraftTypesForInternalId(pComment.getCategory2()) : "";
            String cat3 = pComment.getCategory3() != null ? MenuCache.instance().getDraftTypesForInternalId(pComment.getCategory3()) : "";
            Label lbCategoryDate = new Label(cat1 + " / " + cat2 + " / " + cat3);
            pane.addRow(0, lbFileText, lbFileDate);
            pane.addRow(1, lblCreationDateTxt, lblCreationDate, lblCreationUserTxt, lblCreationUser);
            pane.addRow(2, lbCategoryText, lbCategoryDate);
            lblCreationDate.setText(pComment.getCreationDate() != null ? Lang.toDateTime(pComment.getCreationDate()) : "  ");
            lblCreationUser.setText(pComment.getCreationUser() != null ? facade.getUserFullName(pComment.getCreationUser()) : "  ");
//
            return pane;
        }
    }

    private List<CDrafts> getNewTemplateListe() {
        List<Long> catIds = new ArrayList<>();
        ObservableList<CWmListDraftType> checkedType1 = cbDraftCat1.getCheckModel().getCheckedItems();
        for (CWmListDraftType check : checkedType1) {
            long key = check.getWmDrtInternalId();
            catIds.add(key);
        }
        ObservableList<CWmListDraftType> checkedType2 = cbDraftCat2.getCheckModel().getCheckedItems();
        for (CWmListDraftType check : checkedType2) {
            long key = check.getWmDrtInternalId();
            catIds.add(key);
        }
        ObservableList<CWmListDraftType> checkedType3 = cbDraftCat3.getCheckModel().getCheckedItems();
        for (CWmListDraftType check : checkedType3) {
            long key = check.getWmDrtInternalId();
            catIds.add(key);
        }
        //CPX-1383 save and restore selected draft categories in client
        Session.instance().setSelectedDraftCategories(catIds.stream().mapToLong(l -> l).toArray());
        //CPX-1383  attempt Client-side implementation : search in draft list without server request ...
        filtertResult.clear();
        for (CDrafts pResult : result) {
            boolean cat1 = false;
            boolean cat2 = false;
            boolean cat3 = false;
            if ((!checkedType1.isEmpty() && catIds.contains(pResult.getCategory1())) || checkedType1.isEmpty()) {
                cat1 = true;
            }
            if ((!checkedType2.isEmpty() && catIds.contains(pResult.getCategory2())) || checkedType2.isEmpty()) {
                cat2 = true;
            }
            if ((!checkedType3.isEmpty() && catIds.contains(pResult.getCategory3())) || checkedType3.isEmpty()) {
                cat3 = true;
            }
            if (cat1 && cat2 && cat3) {
                filtertResult.add(pResult);
            }
        }

        //Achtung!!! findAllTemplatesByCategoryInternalId : Server methode!!!    
//            tempList = (List<CDrafts>) templateServiceBean.get().findAllTemplatesByCategoryInternalId(cat1Ids, cat2Ids, cat3Ids);
//            mdDraftType.setItems(FXCollections.observableArrayList(filtertResult));
        mdDraftType.refresh();
        return filtertResult;
    }

    //CPX-1383 Suggest generated filename 
    private void setFormatDocName(String docNameText) {
        docNameText = docNameText.replaceAll("[^a-zA-Z0-9%_+-]", "");
        if (docName.getText().contains("%v%")) {
            docNameText = docNameText.replace("%v%", "_Vorgang_" + currentProcess.getWorkflowNumber());
        }
        if (docName.getText().contains("%f%")) {
            if (currentProcess.getMainCase().getCsCaseNumber() != null) {
                docNameText = docNameText.replace("%f%", "_Fall_" + currentProcess.getMainCase().getCsCaseNumber());
            } else {

                MainApp.showErrorMessageDialog(Lang.getFallMainFehler(), AddDocByTemplateDialog.this.getDialogPane().getScene().getWindow());
                docNameText = docNameText.replace("%f%", "");
            }
        }

        if (docName.getText().contains("%p%")) {
            if (currentProcess.getMainCase().getInsuranceNumberPatient() != null) {
                docNameText = docNameText.replace("%p%", "_Patient_" + currentProcess.getMainCase().getInsuranceNumberPatient());
            } else {
                MainApp.showErrorMessageDialog(Lang.getInsuranceNumberFehler(), AddDocByTemplateDialog.this.getDialogPane().getScene().getWindow());
                docNameText = docNameText.replace("%p%", "");
            }
        }
        if (docName.getText().contains("%i%")) {
            if (currentProcess.getMainCase().getInsuranceIdentifier() != null) {
                docNameText = docNameText.replace("%i%", "_IKZ_" + currentProcess.getMainCase().getInsuranceIdentifier());
            } else {
                MainApp.showErrorMessageDialog(Lang.getInsuranceFehler(), AddDocByTemplateDialog.this.getDialogPane().getScene().getWindow());
                docNameText = docNameText.replace("%i%", "");
            }
        }
        docName.setText(docNameText);
    }
}

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
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.menu.fx.list_master_detail.ListMasterDetailScene;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartment;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.masterdetail.ListViewMasterDetailPane;
import de.lb.cpx.client.core.util.ObjectConverter;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.service.ejb.CaseServiceBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ejb.AsyncResult;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * screen for the department details in the case management / case simulation.
 * AWi-20171215: TODO-Refactor wards for case merging
 *
 * @author nandola
 * @param <E> Class
 */
//@Deprecated(since = "2.0.2")
public class CmDepartmentsDetailsScene<E extends AbstractEntity> extends ListMasterDetailScene<TCaseDepartment> {

    private static final Logger LOG = Logger.getLogger(CmDepartmentsDetailsScene.class.getName());

    private final CpxDepartmentCatalog departmentCatalog;
    private CpxDepartment department;
    private TableView<TCaseIcd> icdTableView;
    private final EjbProxy<CaseServiceBeanRemote> caseServiceBean;
//    private ObservableList<IcdOverviewDTO> listOfIcdDto = FXCollections.observableArrayList();
//    private ObservableList<OpsOverviewDTO> listOfOpsDto = FXCollections.observableArrayList();
    private TableView<TCaseOps> opsTableView;
    private Label lbIcd;
    private Label lbIcdText;
    private final ComboBox<E> cbLocals = new ComboBox<>();
    private Label labelHeader;
//    private TableView mainIcdDiagTableView;
    private String ward;
    private final ObjectConverter<E, TCaseDetails> objectConverter;
    private final EjbProxy<SingleCaseEJBRemote> singleCaseBean;

    public CmDepartmentsDetailsScene(E pSelected, List<E> pItems, ObjectConverter<E, TCaseDetails> pConverter) throws IOException {
        super();
        departmentCatalog = CpxDepartmentCatalog.instance();
        caseServiceBean = Session.instance().getEjbConnector().connectCaseServiceBean();
        singleCaseBean = Session.instance().getEjbConnector().connectSingleCaseBean();
        getController().addToRoot(0, cbLocals);
        setItems(pItems);
        objectConverter = pConverter;
        setMasterDetail(createMasterDetailListView());
//        if (pSelected != null) {
//            cbLocals.getSelectionModel().select(pSelected);
//        }
        cbLocals.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<E>() {
            @Override
            public void changed(ObservableValue<? extends E> observable, E oldValue, E newValue) {
                if (newValue == null) {
                    getController().getMasterDetailPane().setDetail(getDetailContent(null));
                    return;
                }
                //plain reload after version is changed, to trigger reload of values
                LOG.info("reload department content!");
                reload();
//                setCurrentLocal(pConverter.to(newValue));
            }
        });
        select(pSelected);
    }

    public void setNameConverter(StringConverter<E> pConverter) {
        cbLocals.setConverter(pConverter);
    }

    public ReadOnlyObjectProperty<E> selectedItemProperty() {
        return cbLocals.getSelectionModel().selectedItemProperty();
    }

    public ComboBox<E> getComboBox() {
        return cbLocals;
    }

    public final void setItems(List<E> pItems) {
        pItems = Objects.requireNonNull(pItems, "Item Set can not be null");
        cbLocals.setItems(FXCollections.observableArrayList(pItems));
    }

    public final void select(E pItem) {
        cbLocals.getSelectionModel().select(pItem);
    }

    public E getSelected() {
        return cbLocals.getSelectionModel().getSelectedItem();
    }

    @Override
    public Callback<ListView<TCaseDepartment>, ListCell<TCaseDepartment>> getCellFactory() {
        return new Callback<ListView<TCaseDepartment>, ListCell<TCaseDepartment>>() {
            @Override
            public ListCell<TCaseDepartment> call(ListView<TCaseDepartment> param) {
                ListCell<TCaseDepartment> cell = new ListCell<TCaseDepartment>() {
                    private int i = 0;

                    @Override
                    public void updateItem(TCaseDepartment itemObj, boolean empty) {
                        super.updateItem(itemObj, empty);
                        if (itemObj == null || empty) {
                            setGraphic(null);
                            return;
                        }
                        if (itemObj instanceof TCaseDepartment) {
                            final VBox vbox = new VBox();
                            Button btn = new Button("");
                            btn.autosize();
                            btn.setStyle("-fx-font-size: 3px;");
                            btn.setGraphic(getGlyph("\uf067"));
                            btn.getStyleClass().add("cpx-icon-button");

                            TCaseDepartment item = itemObj;
                            SimpleDateFormat formatter = new SimpleDateFormat(Lang.getDepartmentsWardDateFormat());
                            department = departmentCatalog.getByCode(item.getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                            labelHeader = new Label(department.getDepDescription301()
                                    + " (" + item.getDepCodes() + ")"
                                    + (item.getDepcIsBedIntensivFl() ? "\nIntensivbett" : "")
                                    + "\n"
                                    + "von: " + (item.getDepcAdmDate() == null ? "n.A." : formatter.format(item.getDepcAdmDate())) + " bis: " + (item.getDepcDisDate() == null ? "n.A." : formatter.format(item.getDepcDisDate())));

                            List<TCaseWard> listWards1 = item.getCaseWards() == null? new ArrayList<>():new ArrayList<>(item.getCaseWards());
//                            if (item.getId() != 0) {
//                                listWards = caseServiceBean.get().getWardsOfDept(item.getId());
//                            } else {
//                                listWards = new ArrayList<>(item.getCaseWards());
//                            }
                            int counter = 0;
                            
                            if (listWards1 != null && !listWards1.isEmpty() && counter == 0) {
                                listWards1 = listWards1.stream().sorted(Comparator.comparing(TCaseWard::getStartDate)).collect(Collectors.toList());
                                labelHeader.setGraphic(btn);
                                counter++;
                            } else {
                                labelHeader.setPadding(new Insets(0, 0, 0, 38));
                            }
                            final List<TCaseWard> listWards = listWards1;
                            labelHeader.setGraphicTextGap(10);
                            vbox.getChildren().add(labelHeader);
                            labelHeader.setOnMouseClicked(new EventHandler<MouseEvent>(){
                                public void handle(MouseEvent me) {
                                    Parent details = getDetailContent(itemObj);
                                    if(details != null){
                                        getController().getMasterDetailPane().setDetail(details);
                                    }

                                }
                            });

                            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                private VBox innerVbox;
                                private ListView<TCaseWard> innerListView;

                                @Override
                                public void handle(MouseEvent me) {
                                    if (vbox.getChildren().size() == 1) {
                                        innerListView = new ListView<>();
                                        VBox.setVgrow(innerListView, Priority.ALWAYS);
                                        innerVbox = new VBox();
                                        VBox.setVgrow(innerVbox, Priority.ALWAYS);
                                        innerVbox.setStyle("-fx-background-color: white ;");
                                        innerVbox.setPadding(new Insets(10, 10, 5, 10));
                                        innerListView.getItems().clear();
                                        ObservableList<TCaseWard> obsListOfWards = FXCollections.observableArrayList(listWards);
                                        final int ROW_HEIGHT = 24;
                                        innerListView.setPrefHeight((obsListOfWards.size() + 1) * ROW_HEIGHT + 15d);
                                        innerListView.getItems().addAll(obsListOfWards);

                                        innerVbox.getChildren().addAll(innerListView);
                                        innerListView.getItems().size();
                                        vbox.getChildren().addAll(innerVbox);
//                                        }
//                                        innerListView.setMaxHeight(innerListView.);
                                        i = i + 1;
                                        btn.setGraphic(getGlyph("\uf068"));

                                        innerListView.setCellFactory(new Callback<ListView<TCaseWard>, ListCell<TCaseWard>>() {
                                            @Override
                                            public ListCell<TCaseWard> call(ListView<TCaseWard> param) {
                                                ListCell<TCaseWard> cell = new ListCell<TCaseWard>() {
                                                    @Override
                                                    public void updateItem(TCaseWard item, boolean empty) {
                                                        super.updateItem(item, empty);
                                                        if (item == null || empty) {
                                                            return;
                                                        }
                                                        SimpleDateFormat formatter = new SimpleDateFormat(Lang.getDepartmentsWardDateFormat());
                                                        ward = item.getWardcIdent();
                                                        setText(ward + "\n" + "von: " + (item.getWardcAdmdate() == null ? "n.A." : formatter.format(item.getWardcAdmdate())) + " bis: " + (item.getWardcDisdate() == null ? "n.A." : formatter.format(item.getWardcDisdate())));
                                                    }
                                                };
                                                return cell;
                                            }
                                        });
//                                        ChangeListener chWard = new ChangeListener<TCaseWard>() {
//                                            @Override
//                                            public void changed(ObservableValue<? extends TCaseWard> observable, TCaseWard oldValue, TCaseWard newValue) {
//                                                Parent details = getDetailWardContent(newValue);
//                                                if (details != null) {
//                                                    Platform.runLater(() -> {
//                                                        getMasterDetail().getListView().getSelectionModel().clearSelection();
//                                                        getController().getMasterDetailPane().setDetail(details);
//                                                    });
//                                                }
//                                            }
//                                        };
//                                        ChangeListener chDepartment = new ChangeListener<TCaseDepartment>() {
//                                            @Override
//                                            public void changed(ObservableValue<? extends TCaseDepartment> observable, TCaseDepartment oldValue, TCaseDepartment newValue) {
//                                                Parent details = getDetailContent(newValue);
//                                                if (details != null) {
//                                                    Platform.runLater(new Runnable() {
//                                                        @Override
//                                                        public void run() {
////                                                            innerListView.getSelectionModel().clearSelection();
//                                                            getController().getMasterDetailPane().setDetail(details);
//                                                        }
//                                                    });
//                                                }
//                                            }
//                                        };
                                        ChangeListener chSection = new ChangeListener<AbstractCaseEntity>() {
                                            @Override
                                            public void changed(ObservableValue<? extends AbstractCaseEntity> observable, AbstractCaseEntity oldValue, AbstractCaseEntity newValue) {
                                                setDetails(oldValue, newValue);
                                            }
                                        };
                                        
                                        innerListView.getSelectionModel().selectedItemProperty().addListener(chSection);

                                        getMasterDetail().getListView().getSelectionModel().selectedItemProperty().addListener(chSection);
                                    } else {
                                        VBox.clearConstraints(innerListView);
                                        innerListView.getItems().clear();
                                        innerVbox.getChildren().removeAll(innerListView);
                                        vbox.getChildren().remove(innerVbox);
                                        btn.setGraphic(getGlyph("\uf067"));
                                        i = i + 1;
                                        Parent details = getDetailContent(itemObj);
                                        if(details != null){
                                            getController().getMasterDetailPane().setDetail(details);
                                        }
                                    }
                                }
                            });
                            setGraphic(vbox);
                        }
                    }
                };
                return cell;
            }
        };

    }

    public void setDetails( AbstractCaseEntity oldValue, AbstractCaseEntity newValue){
        if(newValue == null && oldValue == null || newValue == null){
            return;
        }
        Parent  details = null;
        if(newValue instanceof  TCaseDepartment){
            details = getDetailContent((TCaseDepartment)newValue);
        }else{
             details = getDetailWardContent((TCaseWard)newValue);
        }
        if (details != null) {
            final Parent details1 = details;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

//                    innerListView.getSelectionModel().clearSelection();
                    getController().getMasterDetailPane().setDetail(details1);
                }
            });
        }
    }
    
    public Glyph getGlyph(String glyph) {
        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        return fontAwesome.create(glyph);
    }
    private Set<TCaseDepartment> getDepartmentsFromCaseDetails(TCaseDetails pDetail){
        if(pDetail == null){
            return new HashSet<>();
        }
        if (pDetail.getId() != 0) {
            return caseServiceBean.get().findDepartments(pDetail.getId());
        } else {
            return pDetail.getCaseDepartments();
        }
    }
    private Set<TCaseDepartment> getDepartmentsFromCase(TCase phosCase){
        if(phosCase == null){
            return new HashSet<>();
        }
        
        return getDepartmentsFromCaseDetails(phosCase.getCurrentLocal());
    }
    private ListViewMasterDetailPane<TCaseDepartment> createMasterDetailListView() {
        ListViewMasterDetailPane<TCaseDepartment> listViewMasterDetailPane = new ListViewMasterDetailPane<>(new AsyncListView<TCaseDepartment>() {
            @Override
            public Future<List<TCaseDepartment>> getFuture() {
                Set<TCaseDepartment> caseDepartments;
                //check if pDetails has db id, if it has - load from db
                //otherwise try to load data from object - should only occure in case merging
                E selected = cbLocals.getSelectionModel().getSelectedItem();
//                if (!(selected instanceof TCaseDetails)) {
//                    return new AsyncResult<>(new ArrayList<>());
//                }
//                TCaseDetails pDetails = (TCaseDetails) selected;
//                if (pDetails.getId() != 0) {
//                    caseDepartments = caseServiceBean.get().findDepartments(pDetails.getId());
//                } else {
//                    caseDepartments = pDetails.getCaseDepartments();
//                }
                if(selected instanceof TCase){
                    caseDepartments = getDepartmentsFromCase((TCase) selected);
                }else if(selected instanceof TCaseDetails){
                    caseDepartments = getDepartmentsFromCaseDetails((TCaseDetails) selected);
                }else{
                    caseDepartments = new HashSet<>();
                }
                ObservableList<TCaseDepartment> observableListOfDepts = FXCollections.observableArrayList(caseDepartments);
                SortedList<TCaseDepartment> sorted = observableListOfDepts.sorted(new Comparator<TCaseDepartment>() {
                    @Override
                    public int compare(TCaseDepartment o1, TCaseDepartment o2) {
                        return o1.getDepcAdmDate().compareTo(o2.getDepcAdmDate());
                    }
                });
                return new AsyncResult<>(sorted);
            }

            @Override
            public void afterTask(Worker.State pState) {
                super.afterTask(pState);
                //select first
                getSelectionModel().selectFirst();
//                getMasterDetail().select(getItems().iterator().next());
            }

        });
        return listViewMasterDetailPane;
    }

    @SuppressWarnings("unchecked")
    private Parent getDetailWardContent(TCaseWard item) {
        //TCaseWard item = (TCaseWard) itemObj;
        if (item == null) {
            return null;
        }
        VBox details = new VBox();
        VBox.setVgrow(details, Priority.ALWAYS);
        details.setAlignment(Pos.CENTER);
        details.setSpacing(12);
        details.setPadding(new Insets(0, 0, 0, 10));

        HBox hbWardMainDiag = new HBox();
        HBox.setHgrow(hbWardMainDiag, Priority.ALWAYS);
        Label label = new Label("Station: ");
        label.setStyle("-fx-text-fill: grey");
        Label labelvalue = new Label(item.getWardcIdent());

        SplitPane spIcdOps = new SplitPane();
        spIcdOps.setOrientation(Orientation.VERTICAL);
        spIcdOps.setDividerPositions(0.5);

        icdTableView = new TableView<>();
        icdTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        HBox.setHgrow(icdTableView, Priority.ALWAYS);
        VBox.setVgrow(icdTableView, Priority.ALWAYS);
        icdTableView.getColumns().addAll(new IcdHdbColumn(), new IcdCodeColumn(), new IcdCodeTextColumn());

        VBox vbMainDiag = new VBox();
        vbMainDiag.setSpacing(6);
        Label lbMainDiagnosis = new Label(Lang.getDepartmentMainDiagnosis());
        lbMainDiagnosis.setStyle("-fx-font-weight: normal");
        lbMainDiagnosis.getStyleClass().add("cpx-title-label");
        Separator sp = new Separator();
        HBox.setHgrow(sp, Priority.ALWAYS);
        sp.getStyleClass().add("cpx-title-separator");
        GridPane gpMainDiagnosis = new GridPane();
        gpMainDiagnosis.getStyleClass().add("default-grid");
        gpMainDiagnosis.setPadding(new Insets(0, 0, 0, 5));

        gpMainDiagnosis.add(keyLabel("ICD"), 0, 0);
        lbIcd = new Label();
        gpMainDiagnosis.add(lbIcd, 0, 1);
        gpMainDiagnosis.add(keyLabel("ICD-Text"), 1, 0);
        lbIcdText = new Label();
        gpMainDiagnosis.add(lbIcdText, 1, 1);
         List<TCaseIcd> listIcds = new ArrayList<>();
         try {
            listIcds = item.getCaseIcds().stream().sorted(Comparator.comparing(TCaseIcd::getIcdcCode)).collect(Collectors.toList());
        } catch (NullPointerException ex) {
            //check for nullPointer, will occure when list is not properly init?
            LOG.log(Level.INFO, "list of icds of department " + item.getId() + " can not be detected. List of Icds not initilized?", ex);
        }

//        List<TCaseIcd> listIcds;
//        if (item.getId() != 0) {
//            listIcds = caseServiceBean.get().getIcdsOfWard(item.getId());
//        } else {
//            listIcds = new ArrayList<>(item.getCaseIcds());
//        }
//        listIcds = listIcds.stream().sorted(Comparator.comparing(TCaseIcd::getIcdcCode)).collect(Collectors.toList());
//            listIcds.forEach(new Consumer<TCaseIcd>() {
//                @Override
//                public void accept(TCaseIcd t) {
//                    IcdOverviewDTO e = new IcdOverviewDTO(t.getIcdcCode(), t.getIcdcLocEn(), "");
//                    listOfIcdDto.add(e);
//                }
//            });
        icdTableView.getItems().addAll(listIcds);

        opsTableView = new TableView<>();
        opsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        HBox.setHgrow(opsTableView, Priority.ALWAYS);
        VBox.setVgrow(opsTableView, Priority.ALWAYS);

        opsTableView.getColumns().addAll(new OpsCodeColumn(), new OpsCodeTextColumn());
        List<TCaseOps> listOpses = new ArrayList<>();
        try {
            listOpses = item.getCaseOpses().stream().sorted(Comparator.comparing(TCaseOps::getOpscCode)).collect(Collectors.toList());
        } catch (NullPointerException ex) {
            //check for nullPointer, will occure when list is not properly init?
            LOG.log(Level.INFO, "list of icds of department " + item.getId() + " can not be detected. List of Icds not initilized?", ex);
        }

//        if (item.getId() != 0) {
//            listOpses = caseServiceBean.get().getOpsOfWard(item.getId());
//        } else {
//            listOpses = new ArrayList<>(item.getCaseOpses());
//        }
//            listOPss.forEach(new Consumer<TCaseOps>() {
//                @Override
//                public void accept(TCaseOps t) {
//                    OpsOverviewDTO e = new OpsOverviewDTO(t.getOpscCode(), "");
//                    listOfOpsDto.add(e);
//                }
//            });
        opsTableView.getItems().addAll(listOpses);//listOfOpsDto);

        hbWardMainDiag.getChildren().addAll(label, labelvalue);

        spIcdOps.getItems().addAll(icdTableView, opsTableView);
        Separator sep = new Separator(Orientation.HORIZONTAL);
        details.getChildren().addAll(hbWardMainDiag, sep, spIcdOps);

        return details;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Parent getDetailContent(TCaseDepartment itemObj) {
        if (itemObj == null) {
            Label emptyContentLabel = new Label("Es ist keine Fachabteilung ausgewählt!");
            VBox emptyContent = new VBox(emptyContentLabel);
            emptyContent.setAlignment(Pos.CENTER);
            return emptyContent;
        }
        VBox details = new VBox();
        //if (itemObj instanceof TCaseDepartment) {
        TCaseDepartment item = itemObj;
        item.getId();
        VBox.setVgrow(details, Priority.ALWAYS);
        details.setAlignment(Pos.CENTER);
        details.setSpacing(12);
        details.setPadding(new Insets(0, 0, 0, 10));

        HBox hbDeptMainDiag = new HBox();
        HBox.setHgrow(hbDeptMainDiag, Priority.ALWAYS);
        Label label = new Label("Fachabteilung: ");
        label.setStyle("-fx-text-fill: grey");
        department = departmentCatalog.getByCode(item.getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);
        Label labelvalue = new Label(department.getDepDescription301() + " (" + item.getDepCodes() + ")"
                + (item.getDepcIsBedIntensivFl() ? " - Intensivbett" : ""));

        VBox vbMainDiag = new VBox();
        vbMainDiag.setSpacing(6);
        Label lbMainDiagnosis = new Label(Lang.getDepartmentMainDiagnosis());
        lbMainDiagnosis.setStyle("-fx-font-weight: normal");
        lbMainDiagnosis.getStyleClass().add("cpx-title-label");
        Separator sp = new Separator();
        HBox.setHgrow(sp, Priority.ALWAYS);
        sp.getStyleClass().add("cpx-title-separator");

//            mainIcdDiagTableView = new TableView<>();
//            mainIcdDiagTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//            VBox.setVgrow(mainIcdDiagTableView, Priority.ALWAYS);
//            mainIcdDiagTableView.getColumns().addAll(new IcdCodeColumn(), new IcdCodeTextColumn());
        GridPane gpMainDiagnosis = new GridPane();
        gpMainDiagnosis.setHgap(50);
        gpMainDiagnosis.setVgap(10);
        gpMainDiagnosis.setStyle("-fx-font-size: 12px;");
        gpMainDiagnosis.setPadding(new Insets(0, 0, 0, 5));
        gpMainDiagnosis.add(keyLabel("ICD"), 0, 0);
        lbIcd = new Label();
        lbIcd.setStyle("-fx-font-size: 15px;");
        OverrunHelper.addOverrunInfoButton(lbIcd);
        gpMainDiagnosis.add(lbIcd, 0, 1);
        gpMainDiagnosis.add(keyLabel("ICD-Text"), 1, 0);
        lbIcdText = new Label();
        lbIcdText.setStyle("-fx-font-size: 15px;");
        OverrunHelper.addOverrunInfoButton(lbIcdText);
        gpMainDiagnosis.add(lbIcdText, 1, 1);

        SplitPane spIcdOps = new SplitPane();
        spIcdOps.setOrientation(Orientation.VERTICAL);
        spIcdOps.setDividerPositions(0.5);

        icdTableView = new TableView<>();
        icdTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(icdTableView, Priority.ALWAYS);
        icdTableView.getColumns().addAll(new IcdHdbColumn(), new IcdCodeColumn(), new IcdCodeTextColumn());
        List<TCaseIcd> listIcds = new ArrayList<>();
//        if (item.getId() != 0) {
//            listIcds = caseServiceBean.get().getIcdsOfDept(item.getId());
//        } else {
//            listIcds = new ArrayList<>(item.getCaseIcds());
//        }
//        listIcds = listIcds.stream().sorted(Comparator.comparing(TCaseIcd::getIcdcCode)).collect(Collectors.toList());
        try {
            listIcds = item.getCaseIcds().stream().sorted(Comparator.comparing(TCaseIcd::getIcdcCode)).collect(Collectors.toList());
        } catch (NullPointerException ex) {
            //check for nullPointer, will occure when list is not properly init?
            LOG.log(Level.INFO, "list of icds of department " + item.getId() + " can not be detected. List of Icds not initilized?", ex);
        }
        if (listIcds == null) {
            return new HBox();
        }
        //if empty-> db lookup to get values
//        if (listIcds.isEmpty()) {
//            listIcds.addAll(caseServiceBean.get().getIcdsOfDept(item.getId()));
//        }
        int count = 0;
        for (TCaseIcd icd : listIcds) {
            if (icd.getIcdcIsHdbFl()) {
                if (count == 0) {
                    updateMainDiagnosis(icd);
                } else {
                    icd.setIcdcIsHdbFl(false);
                    singleCaseBean.get().saveTCaseIcd(icd);
                }
                count++;
            }
        }
//            listIcds.forEach(new Consumer<TCaseIcd>() {
//                private TCaseIcd mainDiag;
//
//                @Override
//                public void accept(TCaseIcd t) {
//                    IcdOverviewDTO e = new IcdOverviewDTO(t.getIcdcCode(), t.getIcdcLocEn(), "");
//                    listOfIcdDto.add(e);
//                    if (t.getIcdcIsHdbFl()) {
//                        mainDiag = t;
//                        if (mainDiag != null && mainDiag.getIcdcCode() != null) {
//                            lbIcd.setText(mainDiag.getIcdcCode());
//                            OverrunHelper.addOverrunInfoButton(lbIcd);
//                            int year = Lang.toYear(objectConverter.to(getCurrentDetails()).getCsdAdmissionDate());
//                            lbIcdText.setText(CpxIcdCatalog.instance().getDescriptionByCode(mainDiag.getIcdcCode(), "de", year));
//                            OverrunHelper.addOverrunInfoButton(lbIcdText);
//                        }
//                    }
//                }
//            });
        icdTableView.getItems().addAll(listIcds);//listOfIcdDto);
        opsTableView = new TableView<>();
        opsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(opsTableView, Priority.ALWAYS);

        opsTableView.getColumns().addAll(new OpsCodeColumn(), new OpsCodeTextColumn());
        List<TCaseOps> listOpses = new ArrayList<>();
        try {
            listOpses = item.getCaseOpses().stream().sorted(Comparator.comparing(TCaseOps::getOpscCode)).collect(Collectors.toList());
        } catch (NullPointerException ex) {
            //check for nullPointer, will occure when list is not properly init?
            LOG.log(Level.INFO, "list of icds of department " + item.getId() + " can not be detected. List of Icds not initilized?", ex);
        }

//        List<TCaseOps> listOpses;
//        if (item.getId() != 0) {
//            listOpses = caseServiceBean.get().getOpsOfDept(item.getId());
//        } else {
//            listOpses = new ArrayList<>(item.getCaseOpses());
//        }
//            listOPss.forEach(new Consumer<TCaseOps>() {
//                @Override
//                public void accept(TCaseOps t) {
//                    OpsOverviewDTO e = new OpsOverviewDTO(t.getOpscCode(), "");
//                    listOfOpsDto.add(e);
//                }
//            });
        opsTableView.getItems().addAll(listOpses);//listOfOpsDto);

        //add list of versions in the ui
        hbDeptMainDiag.getChildren().addAll(label, labelvalue);

        spIcdOps.getItems().addAll(icdTableView, opsTableView);

        vbMainDiag.getChildren().addAll(lbMainDiagnosis, sp, gpMainDiagnosis);
        Separator sep = new Separator(Orientation.HORIZONTAL);
//        sep.setPadding(new Insets(0, 0, 0, 10));
        details.getChildren().addAll(hbDeptMainDiag, vbMainDiag, sep, spIcdOps);
        //}
        return details;
    }

    private Label keyLabel(String pTitle) {
        Label label = new Label(pTitle);
        label.getStyleClass().add("cpx-detail-label");
        return label;
    }

    @Override
    public CpxScene openItem(Long pId) {
//        try {
//            CpxScene deptDetails = CpxFXMLLoader.getScene(CmDepartmentDetailsFXMLController.class);
//            return deptDetails;
//        } catch (IOException ex) {
//            Logger.getLogger(CmDepartmentDetailsFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            MainApp.showErrorMessageDialog(ex);
//        }
        return null;
    }

    @Override
    public void reload() {
        if (getMasterDetail().getListView() instanceof AsyncListView) {
            ((AsyncListView) getMasterDetail().getListView()).reload();
            return;
        }
        getMasterDetail().getListView().setItems(FXCollections.observableArrayList(
                caseServiceBean.get().findDepartments(
                objectConverter.to(getCurrentDetails()).getId())
        ));
    }

    private E getCurrentDetails() {
        return cbLocals.getSelectionModel().getSelectedItem();
    }

    private void updateMainDiagnosis(TCaseIcd pIcd) {
        lbIcd.setText(pIcd.getIcdcCode());
        int year = Lang.toYear(objectConverter.to(getCurrentDetails()).getCsdAdmissionDate());
        lbIcdText.setText(CpxIcdCatalog.instance().getDescriptionByCode(pIcd.getIcdcCode(), "de", year));
    }

    private class IcdHdbColumn extends TableColumn<TCaseIcd, Boolean> {

        private ToggleGroup group = new ToggleGroup();

        public IcdHdbColumn() {
            super(Lang.getDepartmentMainDiagnosisObj().getAbbreviation());
            setMinWidth(45);
            setMaxWidth(45);
            setResizable(false);
            setSortable(false);
//            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//                @Override
//                public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                    if(newValue == null){
//                        return;
//                    }
//                    if(newValue!=null){
//                        TCaseIcd icd = (TCaseIcd) newValue.getUserData();
//                        icd.setIcdcIsHdbFl(newValue.isSelected());
//                        singleCaseBean.get().saveTCaseIcd(icd);
//                        updateMainDiagnosis(icd);
//                    }
//                    if(oldValue!=null){
//                        TCaseIcd icd = (TCaseIcd) oldValue.getUserData();
////                        updateMainDiagnosis(icd);
//                        icd.setIcdcIsHdbFl(oldValue.isSelected());
//                        singleCaseBean.get().saveTCaseIcd(icd);
//                    }
//                }
//            });
            setCellValueFactory(new Callback<CellDataFeatures<TCaseIcd, Boolean>, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(CellDataFeatures<TCaseIcd, Boolean> param) {
                    return new SimpleBooleanProperty(param.getValue().getIcdcIsHdbFl());
                }
            });
            setCellFactory(new Callback<TableColumn<TCaseIcd, Boolean>, TableCell<TCaseIcd, Boolean>>() {
                @Override
                public TableCell<TCaseIcd, Boolean> call(TableColumn<TCaseIcd, Boolean> param) {
                    return new TableCell<TCaseIcd, Boolean>() {
//                        RadioButton btn;
//                        ChangeListener<Boolean> listenerSelected = new ChangeListener<Boolean>() {
//                                    @Override
//                                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                                        TCaseIcd icd = getTableView().getItems().get(getIndex());
//                                        LOG.info("change selected value from " + icd.getIcdcCode());
////                                        icd.setIcdcIsHdbFl(newValue);
////                                        singleCaseBean.get().saveTCaseIcd(icd);
////                                        updateMainDiagnosis(icd);
//                                    }
//                                };
                        @Override
                        protected void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            TCaseIcd currIcd = getTableRow().getItem();
                            RadioButton rbMd = new RadioButton();
                            rbMd.setDisable(!CmDepartmentsDetailsScene.this.isEditable());
//                            rbMd.disableProperty().bind(editableProperty());
                            rbMd.setToggleGroup(group);
                            //padding, needed to avoid "freespace" between text and outline of the radiobutton
                            rbMd.setSelected(item);
                            //listener to react on click, reset hbx value in other icd objects 
                            //and sets ohne hbx value to true
                            //if icd is slected as md that is not used for grouping flag will be set 
                            rbMd.addEventFilter(MouseEvent.MOUSE_CLICKED, (event) -> {
//                                //ignore click if item has hbx flag 
//                                if (item) {
//                                    rbMd.setSelected(true);
//                                    event.consume();
//                                    return;
//                                }
                                LOG.info("process click");
                             
                                for (TCaseIcd icdItem : currIcd.getCaseDepartment().getCaseIcds()) {
                                    
                                    if (icdItem.getIcdcIsHdbFl()) {
                                        icdItem.setIcdcIsHdbFl(false);
                                        if (CmDepartmentsDetailsScene.this.isArmed()) {
                                            singleCaseBean.get().saveTCaseIcd(icdItem);
                                            break;
                                        }
                                    }
                                }
                                currIcd.setIcdcIsHdbFl(true);
                                if (CmDepartmentsDetailsScene.this.isArmed()) {
                                    singleCaseBean.get().saveTCaseIcd(currIcd);
                                }
                                updateMainDiagnosis(currIcd);
//                                storeOrUpdate(version, currIcd);
//                                    version.saveIcdEntity(item);
//                                    version.performGroup();
//                                    //refresh tableview to show updated values
//                                    getRegionForVersion(version).refresh();
                            });
                            setGraphic(rbMd);
//                            TCaseIcd icd = getTableView().getItems().get(getIndex());
////                            if(btn == null){
//                                RadioButton btn = new RadioButton();
//                                btn.setToggleGroup(group);
//                                btn.setUserData(icd);
//                                btn.setSelected(item);
//                                btn.selectedProperty().addListener(listenerSelected);
//                                setGraphic(btn);
//                            }else{
//                                btn.setUserData(icd);
//                                btn.selectedProperty().removeListener(listenerSelected);
//                                btn.setSelected(item);
//                                btn.selectedProperty().addListener(listenerSelected);
//                            }
                        }
                    };
                }
            });
        }

    }
    private BooleanProperty armedProperty;

    public BooleanProperty armedProperty() {
        if (armedProperty == null) {
            armedProperty = new SimpleBooleanProperty(true);
        }
        return armedProperty;
    }

    public boolean isArmed() {
        return armedProperty().get();
    }

    public void setArmed(Boolean pArmed) {
        armedProperty().set(pArmed);
    }
    private BooleanProperty editableProperty;

    public BooleanProperty editableProperty() {
        if (editableProperty == null) {
            editableProperty = new SimpleBooleanProperty(true);
        }
        return editableProperty;
    }

    public boolean isEditable() {
        return editableProperty().get();
    }

    public void setEditable(Boolean pEditable) {
        editableProperty().set(pEditable);
    }

    private class IcdCodeColumn extends TableColumn<TCaseIcd, String> {

        IcdCodeColumn() {
            super(Lang.getCaseResolveICD());
            setMinWidth(80.0d);
            setMaxWidth(80.0d);
            setResizable(false);
            setSortable(false);
            setCellValueFactory(new Callback<CellDataFeatures<TCaseIcd, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<TCaseIcd, String> param) {
                    return new SimpleObjectProperty<>(param.getValue().getIcdcCode());
                }
            });
//            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, IcdOverviewDTO>, TableCell<IcdOverviewDTO, IcdOverviewDTO>>() {
//                @Override
//                public TableCell<IcdOverviewDTO, IcdOverviewDTO> call(TableColumn<IcdOverviewDTO, IcdOverviewDTO> param) {
//                    return new TableCell<IcdOverviewDTO, IcdOverviewDTO>() {
//                        @Override
//                        protected void updateItem(IcdOverviewDTO item, boolean empty) {
//                            super.updateItem(item, empty);
//                            if (item == null || empty) {
//                                setGraphic(null);
//                                return;
//                            }
//
//                            Label label = new Label(item.getIcdCode());
//                            setGraphic(label);
//                        }
//
//                    };
//                }
//            });
//            listOfIcdDto.clear();
        }
    }

    private class IcdCodeTextColumn extends TableColumn<TCaseIcd, String> {

        IcdCodeTextColumn() {
            super(Lang.getCaseResolveICD_Text());
//            setMinWidth(50.0d);
            setMaxWidth(Double.MAX_VALUE);
            setSortable(false);
            setCellValueFactory(new Callback<CellDataFeatures<TCaseIcd, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<TCaseIcd, String> param) {
                    return new SimpleObjectProperty<>(param.getValue().getIcdcCode());
                }
            });
            setCellFactory(new Callback<TableColumn<TCaseIcd, String>, TableCell<TCaseIcd, String>>() {
                @Override
                public TableCell<TCaseIcd, String> call(TableColumn<TCaseIcd, String> param) {
                    return new TableCell<TCaseIcd, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }

                            int year = Lang.toYear(objectConverter.to(getCurrentDetails()).getCsdAdmissionDate());
                            String icdText = CpxIcdCatalog.instance().getDescriptionByCode(item, "de", year);
                            Label lbIcdText = new Label(icdText);
                            OverrunHelper.addOverrunInfoButton(lbIcdText);

                            setGraphic(lbIcdText);
                        }

                    };
                }
            });
        }
    }

    private class OpsCodeColumn extends TableColumn<TCaseOps, String> {

        OpsCodeColumn() {
            super(Lang.getCaseResolveOPS());
            setMinWidth(80.0d);
            setMaxWidth(80.0d);
            setResizable(false);
            setSortable(false);
            setCellValueFactory(new Callback<CellDataFeatures<TCaseOps, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<TCaseOps, String> param) {
                    return new SimpleStringProperty(param.getValue().getOpscCode());
                }
            });
//            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, OpsOverviewDTO>, TableCell<OpsOverviewDTO, OpsOverviewDTO>>() {
//                @Override
//                public TableCell<OpsOverviewDTO, OpsOverviewDTO> call(TableColumn<OpsOverviewDTO, OpsOverviewDTO> param) {
//                    return new TableCell<OpsOverviewDTO, OpsOverviewDTO>() {
//                        @Override
//                        protected void updateItem(OpsOverviewDTO item, boolean empty) {
//                            super.updateItem(item, empty);
//                            if (item == null || empty) {
//                                setGraphic(null);
//                                return;
//                            }
//                            Label label = new Label(item.getOpsCode());
//
//                            setGraphic(label);
//                        }
//
//                    };
//                }
//            });
//            listOfOpsDto.clear();
        }

    }

    private class OpsCodeTextColumn extends TableColumn<TCaseOps, String> {

        OpsCodeTextColumn() {
            super(Lang.getCaseResolveOPS_TEXT());
//            setMinWidth(50.0d);
            setMaxWidth(Double.MAX_VALUE);
            setSortable(false);
            setCellValueFactory(new Callback<CellDataFeatures<TCaseOps, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<TCaseOps, String> param) {
                    return new SimpleStringProperty(param.getValue().getOpscCode());
                }
            });
            setCellFactory(new Callback<TableColumn<TCaseOps, String>, TableCell<TCaseOps, String>>() {
                @Override
                public TableCell<TCaseOps, String> call(TableColumn<TCaseOps, String> param) {
                    return new TableCell<TCaseOps, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            int year = Lang.toYear(objectConverter.to(getCurrentDetails()).getCsdAdmissionDate());
                            String desc = CpxOpsCatalog.instance().getDescriptionByCode(item, "de", year);
                            if (desc == null || desc.isEmpty()) {
                                desc = Lang.getCatalogOpsError(String.valueOf(year));
                            }

                            Label label = new Label(desc);
                            OverrunHelper.addOverrunInfoButton(label);
                            setGraphic(label);
                        }
                    };
                }
            });
        }
    }

}

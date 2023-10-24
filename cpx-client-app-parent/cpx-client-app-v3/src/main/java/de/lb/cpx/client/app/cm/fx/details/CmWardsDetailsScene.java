/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.menu.fx.list_master_detail.ListMasterDetailScene;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.model.fx.masterdetail.ListViewMasterDetailPane;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.service.ejb.CaseServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * screen for the ward details in the case management / case simulation.
 *
 * @author nandola
 */
public class CmWardsDetailsScene extends ListMasterDetailScene<TCaseWard> {
    //                            innerListView.setCellFactory(new Callback<ListView<TCaseWard>, ListCell<TCaseWard>>() {
//                                @Override
//                                public ListCell<TCaseWard> call(ListView<TCaseWard> param) {
//                                    ListCell<TCaseWard> cell = new ListCell<TCaseWard>() {
//
//                                        @Override
//                                        public void updateItem(TCaseWard item, boolean empty) {
//                                            super.updateItem(item, empty);
//                                            if (item == null || empty) {
//                                                return;
//                                            }
//                                            SimpleDateFormat formatter = new SimpleDateFormat(Lang.getProcessListDateFormat());
//
//                                            ward = item.getWardcIdent();
//
//                                            setText(ward + "\n" + "von: " + formatter.format(item.getWardcAdmdate()) + "   bis: " + formatter.format(item.getWardcDisdate()));
//
////                                                        getDetailContent(item);
//                                        }
//
//                                    };
//                                    return cell;
//                                }
//                            });

    private final EjbProxy<CaseServiceBeanRemote> caseServiceBean;
    private String ward;
    private Label lbIcd;
    private Label lbIcdText;
    private final ObservableList<IcdOverviewDTO> listOfIcdDto = FXCollections.observableArrayList();
    private final ObservableList<OpsOverviewDTO> listOfOpsDto = FXCollections.observableArrayList();
    private TCaseIcd mainDiag;
    private final TCaseDetails currentCaseLocal;

    public CmWardsDetailsScene(CaseServiceFacade pServiceFacade, List<TCaseWard> listWards) throws IOException {
        super();
        //get Current local Version of the Case
        currentCaseLocal = pServiceFacade.getCurrentCaseLocal();
        caseServiceBean = Session.instance().getEjbConnector().connectCaseServiceBean();
        ListViewMasterDetailPane<TCaseWard> listViewMasterDetailPane = new ListViewMasterDetailPane<>();
        setMasterDetail(listViewMasterDetailPane);
        getMasterDetail().setItems(FXCollections.observableArrayList(listWards));
        getMasterDetail().select(listWards.iterator().next());

        getRoot().getChildrenUnmodifiable().get(0).disableProperty().setValue(Boolean.TRUE);
    }

    @Override
    public Callback<ListView<TCaseWard>, ListCell<TCaseWard>> getCellFactory() {
        return new Callback<ListView<TCaseWard>, ListCell<TCaseWard>>() {
            @Override
            public ListCell<TCaseWard> call(ListView<TCaseWard> param) {
                ListCell<TCaseWard> cell = new ListCell<TCaseWard>() {

                    @Override
                    public void updateItem(TCaseWard item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            return;
                        }
                        SimpleDateFormat formatter = new SimpleDateFormat(Lang.getProcessListDateFormat());
//                        ward = wardCatalog.getByCode(item.getWardcIdent(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                        ward = item.getWardcIdent();

                        setText(ward + "\n" + "von: " + formatter.format(item.getWardcAdmdate()) + "   bis: " + formatter.format(item.getWardcDisdate()));
                    }

                };
                return cell;
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Parent getDetailContent(TCaseWard item) {
        VBox details = new VBox();
        VBox.setVgrow(details, Priority.ALWAYS);
        details.setAlignment(Pos.CENTER);
        details.setSpacing(12);
        details.setPadding(new Insets(0, 0, 0, 10));

        HBox hbWardMainDiag = new HBox();
        HBox.setHgrow(hbWardMainDiag, Priority.ALWAYS);
        Label label = new Label("Ward: ");
        label.setStyle("-fx-text-fill: grey");
        Label labelvalue = new Label(item.getWardcIdent());

        SplitPane spIcdOps = new SplitPane();
        spIcdOps.setOrientation(Orientation.VERTICAL);
        spIcdOps.setDividerPositions(0.5);

        TableView<IcdOverviewDTO> icdTableView = new TableView<>();
        HBox.setHgrow(icdTableView, Priority.ALWAYS);
        VBox.setVgrow(icdTableView, Priority.ALWAYS);
        icdTableView.getColumns().addAll(new IcdCodeColumn(), new IcdCodeTextColumn());
//        icdTableView.initServiceFacade(this.pServiceFacade);
//        System.out.println("Dept Id is : " + item.getId());

        VBox vbMainDiag = new VBox();
        vbMainDiag.setSpacing(6);
//        vbMainDiag.setPadding(new Insets(0, 0, 0, 10));
        Label lbMainDiagnosis = new Label(Lang.getDepartmentMainDiagnosis());
        lbMainDiagnosis.setStyle("-fx-font-weight: normal");
        lbMainDiagnosis.getStyleClass().add("cpx-title-label");
        Separator sp = new Separator();
        HBox.setHgrow(sp, Priority.ALWAYS);
        sp.getStyleClass().add("cpx-title-separator");
        GridPane gpMainDiagnosis = new GridPane();
        gpMainDiagnosis.getStyleClass().add("default-grid");
        gpMainDiagnosis.setPadding(new Insets(0, 0, 0, 5));

//        gpMainDiagnosis.setGridLinesVisible(true);
        gpMainDiagnosis.add(keyLabel("ICD"), 0, 0);
        lbIcd = new Label();
        gpMainDiagnosis.add(lbIcd, 0, 1);
        gpMainDiagnosis.add(keyLabel("ICD-Text"), 1, 0);
        lbIcdText = new Label();
        gpMainDiagnosis.add(lbIcdText, 1, 1);

        List<TCaseIcd> listIcds = caseServiceBean.get().getIcdsOfWard(item.getId());
        listIcds.forEach(new Consumer<TCaseIcd>() {

            @Override
            public void accept(TCaseIcd t) {
                IcdOverviewDTO e = new IcdOverviewDTO((t != null ? t.getIcdcCode() : ""), (t != null ? t.getIcdcLocEn() : LocalisationEn.E), "");
                listOfIcdDto.add(e);
                if (t != null && t.getIcdcIsHdbFl()) {
                    mainDiag = t;
                    //if (mainDiag != null) {
                    //  lbMainDiagnosis.setText(mainDiag.getIcdcCode());
                    lbIcd.setText(mainDiag.getIcdcCode());
                    OverrunHelper.addOverrunInfoButton(lbIcd);
                    int year = Lang.toYear(currentCaseLocal.getCsdAdmissionDate());
                    lbIcdText.setText(CpxIcdCatalog.instance().getDescriptionByCode(t.getIcdcCode(), "de", year));
                    OverrunHelper.addOverrunInfoButton(lbIcdText);
                    //} else {
                    //    lbMainDiagnosis.setText("");
                    //    lbIcd.setText("");
                    //    lbIcdText.setText("");
                    //}
                }
            }
        });
        icdTableView.setItems(listOfIcdDto);

        TableView<OpsOverviewDTO> opsTableView = new TableView<>();
        HBox.setHgrow(opsTableView, Priority.ALWAYS);
        VBox.setVgrow(opsTableView, Priority.ALWAYS);
        opsTableView.getColumns().addAll(new OpsCodeColumn(), new OpsCodeTextColumn());

        List<TCaseOps> listOPss = caseServiceBean.get().getOpsOfWard(item.getId());
        listOPss.forEach(new Consumer<TCaseOps>() {
            @Override
            public void accept(TCaseOps t) {
                OpsOverviewDTO e = new OpsOverviewDTO(t.getOpscCode(), "");
                listOfOpsDto.add(e);
            }
        });
        opsTableView.setItems(listOfOpsDto);

        hbWardMainDiag.getChildren().addAll(label, labelvalue);

        spIcdOps.getItems().addAll(icdTableView, opsTableView);

        vbMainDiag.getChildren().addAll(lbMainDiagnosis, sp, gpMainDiagnosis);
        Separator sep = new Separator(Orientation.HORIZONTAL);
//        sep.setPadding(new Insets(0, 0, 0, 10));
        details.getChildren().addAll(hbWardMainDiag, vbMainDiag, sep, spIcdOps);

        return details;
    }

    private Label keyLabel(String pTitle) {
        Label label = new Label(pTitle);
        label.getStyleClass().add("cpx-detail-label");
        return label;
    }

    @Override
    public CpxScene openItem(Long pId) {
        return null;
    }

    private class IcdCodeColumn extends TableColumn<IcdOverviewDTO, IcdOverviewDTO> {

        IcdCodeColumn() {
            super(Lang.getCaseResolveICD());
            setMinWidth(80.0d);
            setMaxWidth(80.0d);
            setResizable(false);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IcdOverviewDTO, IcdOverviewDTO>, ObservableValue<IcdOverviewDTO>>() {
                @Override
                public ObservableValue<IcdOverviewDTO> call(TableColumn.CellDataFeatures<IcdOverviewDTO, IcdOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, IcdOverviewDTO>, TableCell<IcdOverviewDTO, IcdOverviewDTO>>() {
                @Override
                public TableCell<IcdOverviewDTO, IcdOverviewDTO> call(TableColumn<IcdOverviewDTO, IcdOverviewDTO> param) {
                    return new TableCell<IcdOverviewDTO, IcdOverviewDTO>() {
                        @Override
                        protected void updateItem(IcdOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }

                            Label label = new Label(item.getIcdCode());
                            setGraphic(label);
                        }

                    };
                }
            });
            listOfIcdDto.clear();
        }
    }

    private class IcdCodeTextColumn extends TableColumn<IcdOverviewDTO, IcdOverviewDTO> {

        IcdCodeTextColumn() {
            super(Lang.getCaseResolveICD_Text());
//            setMinWidth(50.0d);
            setMaxWidth(Double.MAX_VALUE);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IcdOverviewDTO, IcdOverviewDTO>, ObservableValue<IcdOverviewDTO>>() {
                @Override
                public ObservableValue<IcdOverviewDTO> call(TableColumn.CellDataFeatures<IcdOverviewDTO, IcdOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, IcdOverviewDTO>, TableCell<IcdOverviewDTO, IcdOverviewDTO>>() {
                @Override
                public TableCell<IcdOverviewDTO, IcdOverviewDTO> call(TableColumn<IcdOverviewDTO, IcdOverviewDTO> param) {
                    return new TableCell<IcdOverviewDTO, IcdOverviewDTO>() {
                        @Override
                        protected void updateItem(IcdOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }

                            int year = Lang.toYear(currentCaseLocal.getCsdAdmissionDate());
                            String icdText = CpxIcdCatalog.instance().getDescriptionByCode(item.getIcdCode(), "de", year);
                            Label lbIcdText = new Label(icdText);
                            OverrunHelper.addOverrunInfoButton(lbIcdText);
                            setGraphic(lbIcdText);
                        }

                    };
                }
            });
        }
    }

    private class OpsCodeColumn extends TableColumn<OpsOverviewDTO, OpsOverviewDTO> {

        OpsCodeColumn() {
            super(Lang.getCaseResolveOPS());
            setMinWidth(80.0d);
            setMaxWidth(80.0d);
            setResizable(false);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OpsOverviewDTO, OpsOverviewDTO>, ObservableValue<OpsOverviewDTO>>() {
                @Override
                public ObservableValue<OpsOverviewDTO> call(TableColumn.CellDataFeatures<OpsOverviewDTO, OpsOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, OpsOverviewDTO>, TableCell<OpsOverviewDTO, OpsOverviewDTO>>() {
                @Override
                public TableCell<OpsOverviewDTO, OpsOverviewDTO> call(TableColumn<OpsOverviewDTO, OpsOverviewDTO> param) {
                    return new TableCell<OpsOverviewDTO, OpsOverviewDTO>() {
                        @Override
                        protected void updateItem(OpsOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            Label label = new Label(item.getOpsCode());

                            setGraphic(label);
                        }

                    };
                }
            });
            listOfOpsDto.clear();
        }

    }

    private class OpsCodeTextColumn extends TableColumn<OpsOverviewDTO, OpsOverviewDTO> {

        OpsCodeTextColumn() {
            super(Lang.getCaseResolveOPS_TEXT());
//            setMinWidth(50.0d);
            setMaxWidth(Double.MAX_VALUE);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OpsOverviewDTO, OpsOverviewDTO>, ObservableValue<OpsOverviewDTO>>() {
                @Override
                public ObservableValue<OpsOverviewDTO> call(TableColumn.CellDataFeatures<OpsOverviewDTO, OpsOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, OpsOverviewDTO>, TableCell<OpsOverviewDTO, OpsOverviewDTO>>() {
                @Override
                public TableCell<OpsOverviewDTO, OpsOverviewDTO> call(TableColumn<OpsOverviewDTO, OpsOverviewDTO> param) {
                    return new TableCell<OpsOverviewDTO, OpsOverviewDTO>() {
                        @Override
                        protected void updateItem(OpsOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }

                            int year = Lang.toYear(currentCaseLocal.getCsdAdmissionDate());
                            String desc = CpxOpsCatalog.instance().getDescriptionByCode(item.getOpsCode(), "de", year);
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

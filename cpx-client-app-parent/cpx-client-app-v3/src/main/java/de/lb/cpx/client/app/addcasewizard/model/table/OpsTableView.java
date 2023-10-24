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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.addcasewizard.model.table;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.catalog.CpxOpsAopCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.model.cell.SimpleCellValueFactory;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.catalog.service.ejb.CatalogUtil;
import de.lb.cpx.server.commonDB.model.COpsCatalog;
import de.lb.cpx.shared.lang.Lang;
import java.util.Date;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;

/**
 * Implementation for Basic OpsTableView Supported Columns:
 * GrouperRelevant,Ops-Code,Ops-Text,Supplementary
 * Value,Supplementary,Date,Localisation ToDo: Implement missing ColumnValues
 * like supFeecolumn rename columns for right name
 *
 * @author wilde
 */
public class OpsTableView extends TableView<TCaseOps> {

    private final String[] opsTableHeader = new String[]{Lang.getCaseResolveUsedForGrouping(), Lang.getCaseResolveKind(),
        Lang.getCaseResolveOPS(), Lang.getCaseResolveOPS_TEXT(), Lang.getCaseResolveDate(), Lang.getCaseResolveLocalisation()};
    protected final TableColumn<TCaseOps, TCaseOps> useForGroupColumn = new TableColumn<>(opsTableHeader[0]);//,0.1);
    //TODO ART-Tabellenfeld
    protected final TableColumn<TCaseOps, String> opsCodeColumn = new TableColumn<>(opsTableHeader[2]);//,0.1);
    protected final TableColumn<TCaseOps, TCaseOps> opsTextColumn = new TableColumn<>(opsTableHeader[3]);//,0.1);
    protected final TableColumn<TCaseOps, Date> opsDateColumn = new TableColumn<>(opsTableHeader[4]);//,0.1);
    protected final TableColumn<TCaseOps, TCaseOps> opsLocalisationColumn = new TableColumn<>(opsTableHeader[5]);//,0.1);
    private double weight = 1.0;
    private final String lang = CpxClientConfig.instance().getLanguage();
    private int year = CpxClientConfig.instance().getSelectedGrouper().getModelYear();
    private Controller<?> controller;

    /**
     * Constructor, setUp TableView and set ResizePolicy to
     * CostumColumnResizePolicy
     *
     * @param controller CpxSceneController instance
     */
    public OpsTableView(Controller<?> controller) {
        this();
        this.controller = controller;
    }

    public OpsTableView(Controller<?> controller, double weight) {
        this(controller);
        this.weight = weight;
    }

    public OpsTableView() {
//        super(CleanSide.HORIZONTAL);
        setUpTableColumns();
    }

    public Double computeColumnPercentage(Double val1, Double val2) {

        return val1 * val2;
    }

    private void setUpTableColumns() {
        setUpUseForGroupColumn(computeColumnPercentage(0.03, weight));
        setUpOpsCodeColumn(computeColumnPercentage(0.07, weight));
        setUpOpsTextColumn(computeColumnPercentage(0.7, weight));
        setUpDateColumn(computeColumnPercentage(0.1, weight));
        setUpLocalisationColumn(computeColumnPercentage(0.1, weight));

        resizeColumns();
    }

    private void setUpUseForGroupColumn(double pWidth) {
        useForGroupColumn.setCellValueFactory(new SimpleCellValueFactory<>());
        useForGroupColumn.setCellFactory(new Callback<TableColumn<TCaseOps, TCaseOps>, TableCell<TCaseOps, TCaseOps>>() {
            @Override
            public TableCell<TCaseOps, TCaseOps> call(TableColumn<TCaseOps, TCaseOps> param) {
                TableCell<TCaseOps, TCaseOps> cell = new TableCell<>() {
                    private final CheckBox cb = new CheckBox();

                    @Override
                    protected void updateItem(TCaseOps item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
                        TCaseOps ops = item;
                        cb.setUserData(ops);
                        cb.setSelected(ops.getOpsIsToGroupFl());
                        cb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                boolean selected = cb.isSelected();
//                                if (isArmed()) {
                                ops.setOpsIsToGroupFl(selected);
//                                ((CaseDetailsCaseResolveFXMLController)controller).saveOps(ops);
//                                ((CaseDetailsCaseResolveFXMLController)controller).performGroup();
//                                } else {
//                                    cb.setSelected(ops.getOpsIsToGroupFl());
//                                }
                            }
                        });

                        setGraphic(cb);
                    }
                };
                return cell;
            }
        });
        this.getColumns().add(useForGroupColumn);
//        useForGroupColumn.setPercentageWidth(pWidth);
    }

    private void setUpOpsCodeColumn(double pWidth) {
//        opsCodeColumn.setCellValueFactory(new SimpleCellValueFactory<>());
//CPX-997 Sort opsCodeColumn 
        opsCodeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseOps, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseOps, String> param) {
                return new SimpleObjectProperty<>(param.getValue().getOpscCode());
            }
        });
        opsCodeColumn.setCellFactory(new Callback<TableColumn<TCaseOps, String>, TableCell<TCaseOps, String>>() {
            @Override
            public TableCell<TCaseOps, String> call(TableColumn<TCaseOps, String> param) {
                TableCell<TCaseOps, String> cell = new TableCell<TCaseOps, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
                        String aop = CpxOpsAopCatalog.instance().getCategoryDescriptionByCode(item, "de", year);
                        if(aop == null){
                            setText(item);
                        }else{
                            Label label = new Label(item);
                            HBox wrapper = new HBox(label);
                            wrapper.setAlignment(Pos.CENTER_LEFT);
                            Label content = new Label(aop);
//                                AopCatalogLayout content = new AopCatalogLayout(aop);
                             content.setStyle("-fx-text-fill:black;");
                            setGraphic(ExtendedInfoHelper.addInfoPane(wrapper, content, PopOver.ArrowLocation.TOP_LEFT));                            
                        }
                    }
                };
                return cell;
            }
        });
        this.getColumns().add(opsCodeColumn);
    }

    private void setUpOpsTextColumn(double pWidth) {
        opsTextColumn.setCellValueFactory(new SimpleCellValueFactory<>());
        opsTextColumn.setCellFactory(new Callback<TableColumn<TCaseOps, TCaseOps>, TableCell<TCaseOps, TCaseOps>>() {
            @Override
            public TableCell<TCaseOps, TCaseOps> call(TableColumn<TCaseOps, TCaseOps> param) {
                TableCell<TCaseOps, TCaseOps> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(TCaseOps item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText("");
                            return;
                        }
                        TCaseOps ops = item;
                        //CPX-997 addOverrunInfoButton for Ops Text 

                        COpsCatalog opsCat = null;
                        if (ops.getOpscCode() != null) {
                            opsCat = CpxOpsCatalog.instance().getByCode(ops.getOpscCode(), lang, year);
                        }
                        HBox hLabel = new HBox();
                        hLabel.setMinWidth(450);
                        hLabel.setMaxWidth(470);
                        hLabel.setAlignment(Pos.CENTER_LEFT);
                        Label label = new Label(opsCat == null ? null : opsCat.getOpsDescription());
                        hLabel.getChildren().add(label);
                        OverrunHelper.addOverrunInfoButton(label);
                        setGraphic(hLabel);
                    }
                };
                return cell;
            }
        });
        this.getColumns().add(opsTextColumn);
//        opsTextColumn.setPercentageWidth(pWidth);
    }

    private void setUpDateColumn(double pWidth) {
        opsDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseOps, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<TCaseOps, Date> param) {
                return new SimpleObjectProperty<>(param.getValue().getOpscDatum());
            }
        });
//        opsDateColumn.setCellFactory(new Callback<TableColumn<TCaseOps, Date>, TableCell<TCaseOps, Date>>() {
//            @Override
//            public TableCell<TCaseOps, Date> call(TableColumn<TCaseOps, Date> param) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
        opsDateColumn.setCellFactory(new Callback<TableColumn<TCaseOps, Date>, TableCell<TCaseOps, Date>>() {
            @Override
            public TableCell<TCaseOps, Date> call(TableColumn<TCaseOps, Date> param) {
                TableCell<TCaseOps, Date> cell = new TableCell<TCaseOps, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
//                    Date opsDate = ((TCaseOps) item).getOpscDatum();
//                        setValues(Lang.toDateTime(item));
// CPX-977 OPS Date without hour
                        setText(Lang.toDate(item));
                    }

                };
                return cell;
            }
        });
        this.getColumns().add(opsDateColumn);
//        opsDateColumn.setPercentageWidth(pWidth);
    }

    private void setUpLocalisationColumn(double pWidth) {
        opsLocalisationColumn.setCellValueFactory(new SimpleCellValueFactory<>());
        opsLocalisationColumn.setCellFactory(new Callback<TableColumn<TCaseOps, TCaseOps>, TableCell<TCaseOps, TCaseOps>>() {
            @Override
            public TableCell<TCaseOps, TCaseOps> call(TableColumn<TCaseOps, TCaseOps> param) {
                TableCell<TCaseOps, TCaseOps> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(TCaseOps item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
                        TCaseOps ops = item;
                        if (ops.getOpscLocEn() != null && ops.getOpscLocEn() != LocalisationEn.E) {
                            setText(ops.getOpscLocEn().name());
                        } else {
                            setText(" ");
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        this.getColumns().add(opsLocalisationColumn);
//        opsLocalisationColumn.setPercentageWidth(pWidth);
    }

    /**
     * set new catalogYear refresh table after that
     *
     * @param dateForCatalog date which determines the Catalog to use
     */
    public void setCatalogYear(Date dateForCatalog) {
        this.year = CatalogUtil.getCatalogYearForGrouperModel(dateForCatalog, CpxClientConfig.instance().getSelectedGrouper());
        refresh();
    }

    /**
     * forces TableView to resize/recalculate Column-Widths, for all columns
     * ToDo: dynamic resizing, returns to default sizes
     */
    public void resizeColumns() {

//        useForGroupColumn.setPercentageWidth(computeColumnPercentage(0.03, weight));
        /*
        useForGroupColumn.setFixWidth(35);
        opsCodeColumn.setPercentageWidth(computeColumnPercentage(0.08, weight));
        opsTextColumn.setPercentageWidth(computeColumnPercentage(useForGroupColumn.isVisible()?0.6:0.63, weight));
        opsDateColumn.setPercentageWidth(computeColumnPercentage(0.16, weight));
        opsLocalisationColumn.setPercentageWidth(computeColumnPercentage(0.12, weight)); 
         */
        useForGroupColumn.setMinWidth(35);
        useForGroupColumn.setMaxWidth(35);
        opsCodeColumn.setMinWidth(95);
        opsCodeColumn.setMaxWidth(95);

        opsTextColumn.setMinWidth(200);

        opsDateColumn.setMinWidth(150);
        opsDateColumn.setMaxWidth(150);
        opsLocalisationColumn.setMinWidth(100);
        opsLocalisationColumn.setMaxWidth(100);
    }
}

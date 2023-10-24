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
package de.lb.cpx.client.app.cm.fx.simulation.tables;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.model.catalog.CpxDrg;
import de.lb.cpx.client.core.model.catalog.CpxDrgCatalog;
import de.lb.cpx.client.core.model.catalog.CpxPepp;
import de.lb.cpx.client.core.model.catalog.CpxPeppCatalog;
import de.lb.cpx.client.core.model.catalog.layout.DrgCatalogLayout;
import de.lb.cpx.client.core.model.catalog.layout.PeppCatalogLayout;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableViewSkin;
import de.lb.cpx.client.core.model.fx.tableview.column.PcclColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.RevenueColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.RevenueDeltaColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.UpdatablePropertyCell;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCatalogColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCodeColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCorrectionColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCwCareColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCwEffColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgSuppFeeColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.enums.OverrunStyleEn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCarePercAdultColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCarePercInfColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCatalogColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCodeColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCwEffColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppDailyFeeValueColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppDaysIntensiveColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppSuppFeeColumn;
import de.lb.cpx.client.core.model.fx.tooltip.DrgRevenueDayFeeLayout;
import de.lb.cpx.client.core.model.fx.tooltip.DrgRevenueLayout;
import de.lb.cpx.client.core.model.fx.tooltip.DrgSuppFeeLayout;
import de.lb.cpx.client.core.model.fx.tooltip.PeppDailyFeeLayout;
import de.lb.cpx.client.core.model.fx.tooltip.PeppRevenueLayout;
import de.lb.cpx.client.core.model.fx.tooltip.SimpleMathLayout;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * Implementation of the DRG-Tableview, shows the drg results and computes
 * values based on a base version (TCaseDetails) shows the result of the rule
 * evaluation in an expandable area beneath the Drg Results Base implementaion
 * is a table view
 *
 * @author wilde
 * @param <E> comparable content TODO(?) this tableview sets height according to
 * its displayed items, maybe refactor in base class?
 */
public final class DrgTableView<E extends ComparableContent<? extends AbstractEntity>> extends AsyncTableView<E> {

    private static final Logger LOG = Logger.getLogger(DrgTableView.class.getName());

    private final DoubleProperty headerHeightProperty = new SimpleDoubleProperty(10);
    private final ObjectProperty<CaseTypeEn> caseTypeProperty = new SimpleObjectProperty<>();

    private static final Integer MAX_ROWS = 3;

    public DrgTableView() {
        this(CaseTypeEn.DRG);
    }

    public DrgTableView(CaseTypeEn pCaseType) {
        super();
        //deactivate reodering 
        //setup listeners after javafx rendered content
        skinProperty().addListener((obs, oldSkin, newSkin) -> {
            headerHeightProperty.bind(((AsyncTableViewSkin) newSkin).menuAreaHeight());
            ((AsyncTableViewSkin) newSkin).setReordering(false);
        });
//        setDrgColumns();
//        getStyleClass().add("extendable-table-view");
        //set magic cell size! should move to css to apply on all cells? Could avoid ui shenanigans with upscaled texts in windows?
        setFixedCellSize(32);
        //add listener to react to possible height changes
        getItems().addListener(new ListChangeListener<E>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends E> c) {
//                LOG.info("change item size!");
                computeTableHeight(DrgTableView.this, /*rowHeightProperty.get()*/ getFixedCellSize(), headerHeightProperty.get(), 0);
            }
        });
        headerHeightProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                computeTableHeight(DrgTableView.this, /*rowHeightProperty.get()*/ getFixedCellSize(), headerHeightProperty.get(), 0);
            }
        });
        caseTypeProperty().addListener(new ChangeListener<CaseTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends CaseTypeEn> observable, CaseTypeEn oldValue, CaseTypeEn newValue) {
                setColumns(newValue);
            }
        });
        setCaseType(pCaseType);

    }

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
        if (Worker.State.SUCCEEDED.equals(pState)) {
            computeTableHeight(DrgTableView.this, /*rowHeightProperty.get()*/ getFixedCellSize(), headerHeightProperty.get(), 0);
        }
    }

    public void setCaseType(CaseTypeEn pType) {
        caseTypeProperty.set(pType);
    }

    public CaseTypeEn getCaseType() {
        return caseTypeProperty.get();
    }

    public ObjectProperty<CaseTypeEn> caseTypeProperty() {
        return caseTypeProperty;
    }

    /**
     * helper methode to set new height in table runs async with
     * platform.runlater()
     *
     * @param table tableview to set height
     * @param rowHeight row height of the tableview
     * @param headerHeight header height of the tableview
     * @param margin possible marging value
     */
    public void computeTableHeight(TableView<?> table, double rowHeight, double headerHeight, int margin) {
        computeTableHeight(table, table.getItems().size() , rowHeight, headerHeight, margin);
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                double pref = (table.getItems().size() * rowHeight) + headerHeight;
//                double max = (MAX_ROWS * rowHeight) + headerHeight;
////                LOG.info("compute pref Height " + pref + " compute max height " + max + " for items " + getItems().size());
//                if (pref > max) {
//                    pref = max;
//                }
////                LOG.info("compute min height, new value " + pref);
//                table.setMinHeight(pref);
//                table.setPrefHeight(pref);
//                table.setMaxHeight(pref);
//                table.requestLayout();
//            }
//        });
    }
    
    public void computeTableHeight(TableView<?> table,int pItemsSize, double rowHeight, double headerHeight, int margin) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int size = pItemsSize<2?2:pItemsSize;
                double pref = (size * rowHeight) + headerHeight;
                double max = (MAX_ROWS * rowHeight) + headerHeight;
//                LOG.info("compute pref Height " + pref + " compute max height " + max + " for items " + getItems().size());
                if (pref > max) {
                    pref = max;
                }
//                LOG.info("compute min height, new value " + pref);
                table.setMinHeight(pref);
                table.setPrefHeight(pref);
                table.setMaxHeight(pref);
                table.requestLayout();
            }
        });
    }
    
    /**
     * @return get current header height, some magic 10 as default value
     */
    public double getHeaderHeight() {
        return headerHeightProperty.get();
    }

    private void setColumns(CaseTypeEn pType) {
        getColumns().clear();
        if (pType == null) {
            setDefaultColumns();
            return;
        }
        switch (pType) {
            case DRG:
                setDrgColumns();
                break;
            case PEPP:
                setPeppColumns();
                break;
            default:
                setDefaultColumns();
        }
    }

    //set Columns
    //TableRowExpanderColumn provides the functionality to show rules on demand
    //needs to be initialized in a differend way
    @SuppressWarnings("unchecked")
    private void setDrgColumns() {

        VersionColumn versionColumn = new VersionColumn();
        DrgColumn drgColumn = new DrgColumn();
        DrgTextColumn drgTextColumn = new DrgTextColumn();
        PcclCol pcclColumn = new PcclCol();
        CwEffColumn cwEffColumn = new CwEffColumn();
//        SurchargeColumn surChrgColumn = new SurchargeColumn();
//        DeductionColumn deductionColumn = new DeductionColumn();
        CwCareColumn cwCareColumn = new CwCareColumn();
        CorrectionColumn corrColumn = new CorrectionColumn();
        DrgRevenueColumn revenueColumn = new DrgRevenueColumn();
        DrgRevenueDeltaColumn revenueDeltaColumn = new DrgRevenueDeltaColumn();
        DrgSupplementaryFeeColumn supplementaryFeeColumn = new DrgSupplementaryFeeColumn();

        //workaroung for bug in automatic computing of column sizes 
        //by http://bekwam.blogspot.de/2016/02/getting-around-javafx-tableview.html 
        //Awi:20170330
        drgTextColumn.prefWidthProperty().bind(
                widthProperty()
                        .subtract(versionColumn.widthProperty())
                        .subtract(drgColumn.widthProperty())
                        .subtract(pcclColumn.widthProperty())
                        .subtract(cwEffColumn.widthProperty())
                        .subtract(cwCareColumn.widthProperty())
                        .subtract(corrColumn.widthProperty())
                        .subtract(revenueColumn.widthProperty())
                        .subtract(revenueDeltaColumn.widthProperty())
                        .subtract(supplementaryFeeColumn.widthProperty())
                        .subtract(15) // a border stroke?
        );
        //after setting width, prevent resizing
        drgTextColumn.setResizable(false);

        getColumns().addAll(versionColumn, drgColumn, drgTextColumn, pcclColumn, cwEffColumn, cwCareColumn, corrColumn, revenueColumn, revenueDeltaColumn, supplementaryFeeColumn);
    }

    @SuppressWarnings("unchecked")
    private void setPeppColumns() {

        VersionColumn versionColumn = new VersionColumn();
        PeppColumn peppColumn = new PeppColumn();
        PeppTextColumn peppTextColumn = new PeppTextColumn();
        PcclCol pcclColumn = new PcclCol();
        PeppCwColumn cwColumn = new PeppCwColumn();
        PeppDaysIntColumn intensivColumn = new PeppDaysIntColumn();
        PeppDaysPercAdultColumn percAdultColumn = new PeppDaysPercAdultColumn();
        PeppDaysPercInfColumn percInfColumn = new PeppDaysPercInfColumn();
        PeppRevenueColumn revenueColumn = new PeppRevenueColumn();
        PeppRevenueDeltaColumn revenueDeltaColumn = new PeppRevenueDeltaColumn();
        PeppSpValueColumn spValueColumn = new PeppSpValueColumn();
        PeppDfValueColumn dfValueColumn = new PeppDfValueColumn();

        //workaroung for bug in automatic computing of column sizes 
        //by http://bekwam.blogspot.de/2016/02/getting-around-javafx-tableview.html 
        //Awi:20170330
        peppTextColumn.prefWidthProperty().bind(
                widthProperty()
                        .subtract(versionColumn.widthProperty())
                        .subtract(peppColumn.widthProperty())
                        .subtract(pcclColumn.widthProperty())
                        .subtract(cwColumn.widthProperty())
                        .subtract(intensivColumn.widthProperty())
                        .subtract(percAdultColumn.widthProperty())
                        .subtract(percInfColumn.widthProperty())
                        .subtract(revenueColumn.widthProperty())
                        .subtract(revenueDeltaColumn.widthProperty())
                        .subtract(spValueColumn.widthProperty())
                        .subtract(dfValueColumn.widthProperty())
                        .subtract(15) // a border stroke?
        );
        //after setting width, prevent resizing
        peppTextColumn.setResizable(false);

        getColumns().addAll(versionColumn, peppColumn, peppTextColumn, pcclColumn, cwColumn, intensivColumn, percAdultColumn, percInfColumn, revenueColumn, revenueDeltaColumn, spValueColumn, dfValueColumn);
    }

    @Override
    public Future<List<E>> getFuture() {
        if (getOnReload() == null) {
            return new AsyncResult<>(new ArrayList<>());
        }
        return new AsyncResult<>(getOnReload().call(null));
    }

    private void setDefaultColumns() {
        VersionColumn versionColumn = new VersionColumn();
        getColumns().add(versionColumn);
    }

    //AWi-20170807-CPX-558
    //add new VersionColumn
    private class VersionColumn extends TableColumn<E, ColumnContent> {

        public VersionColumn() {
            //empty header
            super("");
            setSortable(false);
            setResizable(false);
            setMinWidth(150);
            setMaxWidth(150);
            setCellValueFactory(new Callback<CellDataFeatures<E, ColumnContent>, ObservableValue<ColumnContent>>() {
                private final ObjectProperty<ColumnContent> value = new SimpleObjectProperty<>();

                @Override
                public ObservableValue<ColumnContent> call(CellDataFeatures<E, ColumnContent> param) {
                    if (param.getValue() == null) {
                        value.setValue(null);
                        return value;
                    }
                    CpxSimpleRuleDTO rule = param.getValue().getHighestRule();
                    Glyph glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.WARNING);
                    if (rule != null) {
                        switch (rule.getRuleTyp()) {
                            case STATE_ERROR:
                                glyph.setStyle("-fx-text-fill:orangered");
                                break;
                            case STATE_WARNING:
                                glyph.setStyle("-fx-text-fill:gold");
                                break;
                            case STATE_SUGG:
                                glyph.setStyle("-fx-text-fill:skyblue");
                                break;
                            default:
                                glyph.setStyle("-fx-text-fill:lightgrey");
                        }
                    }
                    value.set(new ColumnContent(glyph, param.getValue().getVersionName()));
                    return value;
                }
            });
            setCellFactory(new Callback<TableColumn<E, ColumnContent>, TableCell<E, ColumnContent>>() {
                @Override
                public TableCell<E, ColumnContent> call(TableColumn<E, ColumnContent> param) {
                    TableCell<E, ColumnContent> cell = new TableCell<E, ColumnContent>() {
                        @Override
                        protected void updateItem(ColumnContent item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            // ResourceLoader.getGlyph("\uf103"):ResourceLoader.getGlyph("\uf101")
                            Label label = new Label(item.getTitle(), item.getGlyph());
                            Label indicator = new Label();
                            indicator.setAlignment(Pos.CENTER_RIGHT);
                            indicator.setMaxWidth(Double.MAX_VALUE);
                            indicator.graphicProperty().bind(Bindings.when(getTableRow().selectedProperty()).then(ResourceLoader.getGlyph("\uf103")).otherwise(ResourceLoader.getGlyph("\uf101")));
                            HBox.setHgrow(indicator, Priority.ALWAYS);
                            HBox wrapper = new HBox(label, indicator);
                            wrapper.setAlignment(Pos.CENTER);
                            setGraphic(wrapper);
                        }

                    };
                    return cell;
                }
            });
        }

    }

    private class ColumnContent {

        private final Glyph glyph;
        private final String title;

        public ColumnContent(Glyph pGlyph, String pTitle) {
            this.glyph = pGlyph;
            this.title = pTitle;
        }

        public Glyph getGlyph() {
            return glyph;
        }

        public String getTitle() {
            return title;
        }

    }

    private class DrgColumn extends DrgCodeColumn<E> {

        public DrgColumn() {
            setSortable(false);
//            setResizable(false);
            setMinWidth(65.0);
            setMaxWidth(75.0);
            setCellFactory(new UpdatableCellFactory<String>() {

                @Override
                public ObservableValue<? extends Object> getObservable(E pItem) {
                    return pItem.groupingResultProperty();
                }

                @Override
                public String updateDisplayValue(E pTableRowItem, Object pValue) {
                    String displaytext = getDisplayText(pTableRowItem);
                    return displaytext;
                }

                @Override
                public Node updateCell(TableCell<E, String> pCell) {
                    String pItem = pCell.getItem();
                    E pValue = getItems().get(pCell.getIndex());
                    TCaseDrg caseDrg = (TCaseDrg) pValue.getGroupingResult();
                    if (caseDrg != null) {
                        Label label = new Label(pItem);
                        //AWI - need to implement update on show
                        //if drg result updates but not change drg, catalog values not updated
                        //could change due to change of admission mode
                        //update when info should show
                        Pane graph = ExtendedInfoHelper.addInfoPane(label, new Callback<Void, Node>() {
                            @Override
                            public Node call(Void param) {
                                //get current grouper Result
                                TCaseDrg gr = (TCaseDrg) pValue.getGroupingResult();
                                CpxDrg catalog = CpxDrgCatalog.instance().getByCode(pItem, "de", getYear(gr));
                                //create new layout with updated values
                                DrgCatalogLayout layout = new DrgCatalogLayout(catalog,
                                        pValue.getCaseDetails().getCsdAdmodEn(),
                                        gr/*,
                                        drgBaserate,
                                        careBaserate*/);
                                return layout;
                            }
                        }, PopOver.ArrowLocation.TOP_LEFT);
                        return graph;
                    }
                    return null;
                }
            });
        }

        @Override
        public TCaseDrg getValue(E pValue) {
            return (TCaseDrg) pValue.getGroupingResult();
        }

        public int getYear(TCaseDrg pDrg) {
            if (GDRGModel.AUTOMATIC.equals(pDrg.getModelIdEn())) {
                return Lang.toYear(pDrg.getCaseDetails().getCsdAdmissionDate());
            }
            return pDrg.getModelIdEn().getCatalogYear();
        }
    }

    private class DrgTextColumn extends DrgCatalogColumn<E> {

        public DrgTextColumn() {
            super();
            setSortable(false);
            setMinWidth(50);
            setMaxWidth(Double.MAX_VALUE);
            setCellFactory(new GrouperResultCellFactory<String>() {
                @Override
                public String updateDisplayValue(E pTableItem, Object pValue) {
                    String displaytext = getDisplayText(pTableItem);
                    return displaytext;
                }

                @Override
                public Node updateCell(TableCell<E, String> pCell) {
                    return createGraphic(pCell.getItem(), OverrunStyleEn.Button);
                }

            });
        }

        @Override
        public TCaseDrg getValue(E pValue) {
            return (TCaseDrg) pValue.getGroupingResult();
        }

    }

    private class PcclCol extends PcclColumn<E> {

        public PcclCol() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(48.0);
            setMaxWidth(48.0);
            setCellFactory(new GrouperResultCellFactory<Integer>() {
                @Override
                public Integer updateDisplayValue(E pTableItem, Object pValue) {
                    Integer number = getDisplayNumber(pTableItem);
                    return number;
                }
            });
        }

        @Override
        public TGroupingResults getValue(E pValue) {
            return pValue.getGroupingResult();
        }
    }

    //cw effectiv column, value is set in TCaseDrg Entity - its extansion of TGroupingResult
    private class CwEffColumn extends DrgCwEffColumn<E> {

        public CwEffColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(70.0);
            setMaxWidth(70.0);
            setCellFactory(new GrouperResultCellFactory<String>() {
                @Override
                public String updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayText(pTableItem);
                }
            });
        }

        @Override
        public TCaseDrg getValue(E pValue) {
            return (TCaseDrg) pValue.getGroupingResult();
        }
    }

    //deduction(Abschlag) column
    //cwCorrection value from TCaseDrg if CorrectionType is Deduction or DeductionTransfer
//    private class DeductionColumn extends DrgDeductionColumn<E> {
//
//        public DeductionColumn() {
//            super();
//            setResizable(false);
//            setMinWidth(85.0);
//            setMaxWidth(85.0);
//            setSortable(false);
//            setCellFactory(new GrouperResultCellFactory<String>() {
//                @Override
//                public String updateDisplayValue(E pTableItem, Object pValue) {
//                    return getDisplayText(pTableItem);
//                }
//            });
//        }
//
//        @Override
//        public TCaseDrg getValue(E pValue) {
//            return (TCaseDrg) pValue.getGroupingResult();
//        }
//    }
//
//    //Surcharge (Zuschlag) column
//    //value set in cwCorr in TCaseDrg, value is set of corrType is Surcharge
//    private class SurchargeColumn extends DrgSurchargeColumn<E> {
//
//        public SurchargeColumn() {
//            super();
//            setResizable(false);
//            setMinWidth(85.0);
//            setMaxWidth(85.0);
//            setSortable(false);
//            setCellFactory(new GrouperResultCellFactory<String>() {
//                @Override
//                public String updateDisplayValue(E pTableItem, Object pValue) {
//                    return getDisplayText(pTableItem);
//                }
//            });
//        }
//
//        @Override
//        public TCaseDrg getValue(E pValue) {
//            return (TCaseDrg) pValue.getGroupingResult();
//        }
//    }
    //correction (Zu-/Abschlag) column
    //value set in cwCorr in TCaseDrg, value is set of corrType is Surcharge or deduction
    private class CorrectionColumn extends DrgCorrectionColumn<E> {

        public CorrectionColumn() {
            super();
            setResizable(false);
            setMinWidth(85.0);
            setMaxWidth(85.0);
            setSortable(false);
            setCellFactory(new GrouperResultCellFactory<String>() {
                @Override
                public Node updateCell(TableCell<E, String> pCell) {
                    if (pCell.getItem() != null && !pCell.isEmpty()) {
                        pCell.setTooltip(getCellTooltip(getValue(pCell.getTableRow().getItem())));
                    } else {
                        pCell.setTooltip(null);
                    }
                    return super.updateCell(pCell); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayText(pTableItem);
                }
            });
        }

        @Override
        public TCaseDrg getValue(E pValue) {
            return (TCaseDrg) pValue.getGroupingResult();
        }
    }

    //cw effectiv column, value is set in TCaseDrg Entity - its extansion of TGroupingResult
    private class CwCareColumn extends DrgCwCareColumn<E> {

        public CwCareColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(70.0);
            setMaxWidth(70.0);
            setCellFactory(new GrouperResultCellFactory<String>() {
                @Override
                public String updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayText(pTableItem);
                }
            });
        }

        @Override
        public TCaseDrg getValue(E pValue) {
            return (TCaseDrg) pValue.getGroupingResult();
        }

    }

    //Revenue(Erloes) column
    //calculated value : cwEff * baserate 
    //cwEff is stored in TCaseDrg, baserate is a catalog value (BASE_FEE_VALUE in local database)
    private class DrgRevenueColumn extends RevenueColumn<E> {

        public DrgRevenueColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(105.0);
            setMaxWidth(105.0);
            setCellFactory(new GrouperResultCellFactory<Number>() {
                @Override
                public Node updateCell(TableCell<E, Number> pCell) {
                    E pValue = getItems().get(pCell.getIndex());
                    Double br = pValue.getCaseBaserate();
                    Double careBr = pValue.getCareBaserate();
                    TCaseDrg caseDrg = (TCaseDrg) pValue.getGroupingResult();
                    if (caseDrg != null) {
                        Double rev = caseDrg.getRevenue(br, careBr);//BaserateHelper.computeDrgRevenue(caseDrg, br,careBr);
                        SimpleMathLayout tip;
                        if (caseDrg.isNegotiatedDayFee()) {
                            tip = new DrgRevenueDayFeeLayout(rev, careBr, caseDrg);
                        } else {
                            tip = new DrgRevenueLayout(rev, br, careBr, caseDrg);
                        }
                        Label label = new Label(getDisplayText(pCell.getItem().doubleValue()));
                        HBox wrapper = new HBox(label);
                        wrapper.setAlignment(Pos.CENTER_LEFT);
                        Pane node = ExtendedInfoHelper.addInfoPane(wrapper, tip);
                        return node;
                    }
                    return null;
                }

                @Override
                public Number updateDisplayValue(E pTableItem, Object pValue) {
                    return getValue(pTableItem);
                }
            });
        }

        @Override
        public Double getValue(E pValue) {
            if (pValue == null || pValue.getGroupingResult() == null) {
                return 0.0;
            }
            Double br = pValue.getCaseBaserate();
            Double careBr = pValue.getCareBaserate();
            TCaseDrg caseDrg = (TCaseDrg) pValue.getGroupingResult();
            Double rev = caseDrg.getRevenue(br, careBr);
            return rev;
        }

    }

    //Revenue(erloes) delta column 
    //calculated value: revenue of the base version (Kis-Version) - revenue of current version
    private class DrgRevenueDeltaColumn extends RevenueDeltaColumn<E> {

        public DrgRevenueDeltaColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(110.0);
            setMaxWidth(140.0);
            setCellFactory(new GrouperResultCellFactory<Number>() {
                @Override
                public Node updateCell(TableCell<E, Number> pCell) {
                    Label label = new Label(getDisplayText(pCell.getItem().doubleValue()));
                    return label;
                }

                @Override
                public Number updateDisplayValue(E pTableItem, Object pValue) {
                    return getValue(pTableItem);
                }
            });
        }

        @Override
        public Double getValue(E pValue) {
            if (pValue.getGroupingResult() != null/* && pValue != getItems().get(0)*/) {
                if (getItems().isEmpty()) {
                    return 0.0;
                }
                if (pValue == getItems().get(0)) {
                    return 0.0;
                }
                TGroupingResults grpResult = pValue.getGroupingResult();
                TCaseDrg caseDrg = grpResult == null ? null : grpResult.getCaseDrg();
                double versionRevenue = 0.0;
                if (caseDrg != null) {
                    Double br = pValue.getCaseBaserate();
                    Double careBr = pValue.getCareBaserate();
                    versionRevenue = caseDrg.getRevenue(br, careBr);
                }

                TGroupingResults baseGroupingResult = getItems().get(0).getGroupingResult();
                TCaseDrg baseDrg = baseGroupingResult == null ? null : baseGroupingResult.getCaseDrg();
                double baseRevenue = 0.0;
                if (baseDrg != null) {
                    Double br = getItems().get(0).getCaseBaserate();
                    Double careBr = pValue.getCareBaserate();
                    baseRevenue = baseDrg.getRevenue(br, careBr);
                }
                return versionRevenue - baseRevenue;
            }
            return 0.0;
        }
    }

    //supplemtary fee (Zusatzentgeld-Betrag) column
    //calculated value on the server side only result is transfered to the client
    private class DrgSupplementaryFeeColumn extends DrgSuppFeeColumn<E> {

        public DrgSupplementaryFeeColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(100.0);
            setMaxWidth(100.0);
            setCellFactory(new GrouperResultCellFactory<Number>() {
                @Override
                public Node updateCell(TableCell<E, Number> pCell) {
                    Number item = pCell.getItem();
                    Node node;
                    if (Double.doubleToRawLongBits(item.doubleValue()) != Double.doubleToRawLongBits(0.0d)) {
                        E pValue = getItems().get(pCell.getIndex());
                        DrgSuppFeeLayout tip = new DrgSuppFeeLayout(item.doubleValue(), pValue.getGroupingResult().getCaseOpsGroupeds());
                        node = ExtendedInfoHelper.addInfoPane(new Label(getDisplayText(item.doubleValue())), tip, PopOver.ArrowLocation.TOP_RIGHT);
                    } else {
                        node = new Label(getDisplayText(item.doubleValue()));
                    }
                    return node;
                }

                @Override
                public Double updateDisplayValue(E pTableItem, Object pValue) {
                    return getValue(pTableItem);
                }
            });
        }

        @Override
        public Double getValue(E pValue) {
            Double suppFee = pValue.getSupplFeeValue(SupplFeeTypeEn.ZE);
            if (suppFee != null) {
                return suppFee;
            }
            return 0.0;
        }
    }

    private class PeppColumn extends PeppCodeColumn<E> {

        public PeppColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(65.0);
            setMaxWidth(75.0);
            setCellFactory(new GrouperResultCellFactory<String>() {
                @Override
                public Node updateCell(TableCell<E, String> pCell) {
                    E pValue = getItems().get(pCell.getIndex());
                    String item = pCell.getItem();
                    TCasePepp casePepp = (TCasePepp) pValue.getGroupingResult();
                    if (casePepp != null) {
                        Label label = new Label(item);
                        CpxPepp catalog = CpxPeppCatalog.instance().getByCode(item, "de", getYear(casePepp));
                        PeppCatalogLayout layout = new PeppCatalogLayout(catalog, pValue.getGroupingResult(), casePepp.getCaseDetails().getCsdLos());

                        Pane node = ExtendedInfoHelper.addInfoPane(label, layout, PopOver.ArrowLocation.TOP_LEFT);
                        return node;
                    }
                    return null;
                }

                @Override
                public String updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayText(pTableItem);
                }
            });
        }

        @Override
        public TCasePepp getValue(E pValue) {
            return (TCasePepp) pValue.getGroupingResult();
        }

        public int getYear(TCasePepp pPepp) {
            if (GDRGModel.AUTOMATIC.equals(pPepp.getModelIdEn())) {
                return Lang.toYear(pPepp.getCaseDetails().getCsdAdmissionDate());
            }
            return pPepp.getModelIdEn().getCatalogYear();
        }
    }

    private class PeppTextColumn extends PeppCatalogColumn<E> {

        public PeppTextColumn() {
            super();
            setSortable(false);
            setMinWidth(50);
            setMaxWidth(Double.MAX_VALUE);
            setCellFactory(new GrouperResultCellFactory<String>() {
                @Override
                public String updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayText(pTableItem);
                }

                @Override
                public Node updateCell(TableCell<E, String> pCell) {
                    return createGraphic(pCell.getItem(), OverrunStyleEn.Button);
                }
            });
        }

        @Override
        public TCasePepp getValue(E pValue) {
            return (TCasePepp) pValue.getGroupingResult();
        }
    }

    private class PeppCwColumn extends PeppCwEffColumn<E> {

        public PeppCwColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(80.0);
            setMaxWidth(80.0);
            setCellFactory(new GrouperResultCellFactory<String>() {
                @Override
                public String updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayText(pTableItem);
                }
            });

        }

        @Override
        public TCasePepp getValue(E pValue) {
            return (TCasePepp) pValue.getGroupingResult();
        }
    }

    private class PeppDaysIntColumn extends PeppDaysIntensiveColumn<E> {

        public PeppDaysIntColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(80.0);
            setMaxWidth(80.0);
            setCellFactory(new GrouperResultCellFactory<Integer>() {
                @Override
                public Integer updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayNumber(pTableItem);
                }
            });
        }

        @Override
        public TCasePepp getValue(E pValue) {
            return (TCasePepp) pValue.getGroupingResult();
        }

    }

    private class PeppDaysPercAdultColumn extends PeppCarePercAdultColumn<E> {

        public PeppDaysPercAdultColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(70.0);
            setMaxWidth(70.0);
            setCellFactory(new GrouperResultCellFactory<Integer>() {
                @Override
                public Integer updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayNumber(pTableItem);
                }

            });
        }

        @Override
        public TCasePepp getValue(E pValue) {
            return (TCasePepp) pValue.getGroupingResult();
        }
    }

    private class PeppDaysPercInfColumn extends PeppCarePercInfColumn<E> {

        public PeppDaysPercInfColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(95.0);
            setMaxWidth(95.0);
            setCellFactory(new GrouperResultCellFactory<Integer>() {
                @Override
                public Integer updateDisplayValue(E pTableItem, Object pValue) {
                    return getDisplayNumber(pTableItem);
                }
            });
        }

        @Override
        public TCasePepp getValue(E pValue) {
            return (TCasePepp) pValue.getGroupingResult();
        }
    }

    private class PeppRevenueColumn extends RevenueColumn<E> {

        public PeppRevenueColumn() {
            super();
            setSortable(false);
            setResizable(true);
            setMinWidth(105.0);
            setMaxWidth(105.0);
            setCellFactory(new GrouperResultCellFactory<Number>() {
                @Override
                public Node updateCell(TableCell<E, Number> pCell) {
                    E pValue = getItems().get(pCell.getIndex());
                    Number item = pCell.getItem();
//                    Double br = pValue.getCaseBaserate();

                    if (pValue.getGroupingResult() == null) {
                        LOG.log(Level.WARNING, "grouper result for content is null: {0}", pValue.getContent());
                        return null;
                    }
                    TCasePepp casePepp = (TCasePepp) pValue.getGroupingResult();
                    Double rev = casePepp.getRevenue();
                    PeppRevenueLayout tip = new PeppRevenueLayout(rev, casePepp.getPeppcGrades(), Objects.requireNonNullElse(casePepp.getPeppcPayClass(), 1), casePepp.getModelIdEn().getModelYear());
                    Pane node = ExtendedInfoHelper.addInfoPane(new Label(getDisplayText(item.doubleValue())), tip, PopOver.ArrowLocation.TOP_RIGHT);
                    return node;
                }

                @Override
                public Number updateDisplayValue(E pTableItem, Object pValue) {
                    return getValue(pTableItem);
                }
            });
        }

        @Override
        public Double getValue(E pValue) {
//            Double br = pValue.getCaseBaserate();
            TCasePepp casePepp = (TCasePepp) pValue.getGroupingResult();
            return casePepp == null ? 0 : casePepp.getRevenue();
        }
    }

    //Revenue(erloes) delta column 
    //calculated value: revenue of the base version (Kis-Version) - revenue of current version
    private class PeppRevenueDeltaColumn extends RevenueDeltaColumn<E> {

        public PeppRevenueDeltaColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(110.0);
            setMaxWidth(140.0);
            setCellFactory(new GrouperResultCellFactory<Number>() {
                @Override
                public Node updateCell(TableCell<E, Number> pCell) {
                    Label label = new Label(getDisplayText(pCell.getItem().doubleValue()));
                    return label;
                }

                @Override
                public Number updateDisplayValue(E pTableItem, Object pValue) {
                    return getValue(pTableItem);
                }
            });
        }

        @Override
        public Double getValue(E pValue) {
            if (pValue.getGroupingResult() != null) {
                if (getItems().isEmpty()) {
                    return 0.0;
                }
                if (pValue == getItems().get(0)) {
                    return 0.0;
                }
                TGroupingResults grpResult = pValue.getGroupingResult();
                TCasePepp casePepp = grpResult == null ? null : grpResult.getCasePepp();
                double versionRevenue = 0.0;
                if (casePepp != null) {
                    versionRevenue = casePepp.getRevenue();
                }

                TGroupingResults baseGroupingResult = getItems().get(0).getGroupingResult();
                TCasePepp basePepp = baseGroupingResult == null ? null : baseGroupingResult.getCasePepp();
                double baseRevenue = 0.0;
                if (basePepp != null) {
                    baseRevenue = basePepp.getRevenue();
                }
                return versionRevenue - baseRevenue;
            }
            return 0.0;
        }
    }

    private class PeppSpValueColumn extends PeppSuppFeeColumn<E> {

        public PeppSpValueColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(100.0);
            setMaxWidth(100.0);
            setCellFactory(new GrouperResultCellFactory<Number>() {
                @Override
                public Node updateCell(TableCell<E, Number> pCell) {
                    Label label = new Label(getDisplayText(pCell.getItem().doubleValue()));
                    return label;
                }

                @Override
                public Number updateDisplayValue(E pTableItem, Object pValue) {
                    return getValue(pTableItem);
                }
            });
        }

        @Override
        public Double getValue(E pValue) {
            Double suppFee = pValue.getSupplFeeValue(SupplFeeTypeEn.ZP);
            if (suppFee != null) {
                return suppFee;
            }
            return 0.0;
        }
    }

    private class PeppDfValueColumn extends PeppDailyFeeValueColumn<E> {

        public PeppDfValueColumn() {
            super();
            setSortable(false);
            setResizable(false);
            setMinWidth(100.0);
            setMaxWidth(100.0);
            setCellFactory(new GrouperResultCellFactory<Number>() {
                @Override
                public Node updateCell(TableCell<E, Number> pCell) {
                    Number item = pCell.getItem();
                    Node node;
                    if (Double.doubleToRawLongBits(item.doubleValue()) != Double.doubleToRawLongBits(0.0d)) {
                        E pValue = getItems().get(pCell.getIndex());
                        TCasePepp casePepp = (TCasePepp) pValue.getGroupingResult();
                        PeppDailyFeeLayout tip = new PeppDailyFeeLayout(item.doubleValue(), pValue.getGroupingResult().getCaseOpsGroupeds());
                        node = ExtendedInfoHelper.addInfoPane(new Label(getDisplayText(item.doubleValue())), tip, PopOver.ArrowLocation.TOP_RIGHT);
                    } else {
                        node = new Label(getDisplayText(item.doubleValue()));
                    }
                    return node;
                }

                @Override
                public Number updateDisplayValue(E pTableItem, Object pValue) {
                    return getValue(pTableItem);
                }
            });
        }

        @Override
        public Double getValue(E pValue) {

            Double suppFee = pValue.getSupplFeeValue(SupplFeeTypeEn.ET);
            if (suppFee != null) {
                return suppFee;
            }
            return 0.0;
        }
    }

    private abstract class GrouperResultCellFactory<T> extends UpdatableCellFactory<T> {

        @Override
        public Node updateCell(TableCell<E, T> pCell) {
            return new Label(String.valueOf(pCell.getItem()));
        }

        @Override
        public ObservableValue<? extends Object> getObservable(E pItem) {
            return pItem.groupingResultProperty();
        }
    }

    private abstract class UpdatableCellFactory<T> implements Callback<TableColumn<E, T>, TableCell<E, T>> {

        @Override
        public TableCell<E, T> call(TableColumn<E, T> p) {
            return new UpdatablePropertyCell<E, T>() {
                @Override
                protected void updateItem(T t, boolean bln) {
                    super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
                    if (t == null || bln) {
                        setGraphic(null);
                        return;
                    }

                    setGraphic(updateCell(this));
                }

                @Override
                public ObservableValue<? extends Object> getObservable(E pItem) {
                    return UpdatableCellFactory.this.getObservable(pItem);
                }
                ChangeListener<Object> listener = new ChangeListener<Object>() {
                    @Override
                    public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                        T val = updateDisplayValue(getTableRow().getItem(), newValue);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateItem(val, val == null);
                            }
                        });
                    }
                };

                @Override
                public ChangeListener<? extends Object> getListener() {
                    return listener;
                }

            };
        }

        public abstract Node updateCell(TableCell<E, T> pCell);

        public abstract ObservableValue<? extends Object> getObservable(E pItem);

        public abstract T updateDisplayValue(E pTableItem, Object pValue);
    }

}

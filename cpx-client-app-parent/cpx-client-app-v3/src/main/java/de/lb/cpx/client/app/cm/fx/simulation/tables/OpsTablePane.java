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

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartment;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOpsAop;
import de.lb.cpx.client.core.model.catalog.CpxOpsAopCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.model.catalog.layout.AopCatalogLayout;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.client.core.model.fx.comparablepane.TableCompPane;
import static de.lb.cpx.client.core.model.fx.comparablepane.TableCompPane.DeleteStrategy.ATLEAST_ONE_EDITABLE;
import static de.lb.cpx.client.core.model.fx.comparablepane.TableCompPane.DeleteStrategy.DEPENTING_ON_BASE;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableViewSkin;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgSfValueColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppSpDfValueColumn;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ExcelCsvFileManager;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TCaseSupplFee;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.lang.Lang;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ejb.AsyncResult;
import org.controlsfx.control.PopOver;

/**
 * Implements a new Ops tablePane to show version based ops data in the case
 * mangement scene
 *
 * @author wilde
 * @param <T> content type
 *
 * TODO: -ops data is stored redundantly in all shown tableviews
 */
public class OpsTablePane<T extends ComparableContent<? extends AbstractEntity>> extends TableCompPane<OpsOverviewDTO, T> {

    private static final Logger LOG = Logger.getLogger(OpsTablePane.class.getName());

    /**
     * construct new ops table pane to show versionbased and non-versionbased
     * content
     */
    @SuppressWarnings("unchecked")
    public OpsTablePane() {
        super();
        OpsCodeColumn opsCodeCol = new OpsCodeColumn();
        OpsCodeTextColumn opsTextCol = new OpsCodeTextColumn();
        getInfo().getColumns().addAll(opsCodeCol, opsTextCol);//,supCol);//,opsDateCol);
        getInfo().setRowContextMenu(createContextMenu());
    }

    public void setCaseType(CaseTypeEn pType) {
        switch (pType) {
            case DRG:
                getInfo().getColumns().add(new DrgSupplementaryColumn());
                break;
            case PEPP:
                getInfo().getColumns().add(new PeppSupplementaryColumn());
                break;
            default:
                LOG.log(Level.WARNING, "Unknown case type: {0}", pType.name());
        }
        getInfo().refresh();
    }

    @Override
    public AsyncTableView<OpsOverviewDTO> createNewVersionTableView(T pContent) {
        return new OpsVersionTableView(pContent);
    }

    @Override
    public boolean delete(OpsOverviewDTO pItem) {
        if (pItem == null) {
            return false;
        }
        List<Long> listOfVersions = new ArrayList<>(pItem.getOccurance());

        //TODO:check if something could be removed here, 
        //list gets loaded anew from server, no need to remove items from tableview, only from caseDetails
        Iterator<Long> it = pItem.getOccurance().iterator();
        while (it.hasNext()) {
            Long next = it.next();
            if (deleteInVersion(next, pItem)) {
                it.remove();
            }
        }

        groupVersions(listOfVersions);

        //AWi-CPX-1345-20190104
        //removed due to unneccessary, item should already be removed and remove should always return false
        //resulting in an wrong message
//        if (pItem.getOccurance().isEmpty()) {
//            reload();
//            return getItems().remove(pItem);
//        }
        reload();
        return true;
    }

    /**
     * @param pOps list of ops to add to local version
     */
    public void addOpses(List<TCaseOps> pOps) {
        for (TCaseOps ops : pOps) {
            addOps(ops);
        }
        sortByCode();
    }

    /**
     * @param pOps single ops to add to local versions
     */
    public void addOps(TCaseOps pOps) {
        OpsOverviewDTO opsDto = new OpsOverviewDTO(pOps.getOpscCode(), "");
        for (T version : versionList) {
            if (version.isEditable()) {
                opsDto.getOccurance().add(version.getCaseDetails().getId());
                opsDto.addOpsForVersion(String.valueOf(version.getCaseDetails().getId()), pOps);
            }
        }
        getItems().add(opsDto);
    }

    /**
     * sort items by date,hbx,code,id
     */
    public void sortByCode() {
        getItems().sort(Comparator.comparing(OpsOverviewDTO::getOpsCode).thenComparing(OpsOverviewDTO::getDate, Comparator.nullsFirst(Comparator.naturalOrder())));
    }

    private boolean deleteInVersion(long pOccurance, OpsOverviewDTO pItem) {
        Iterator<T> it = versionList.iterator();
        while (it.hasNext()) {
            T next = it.next();
            if (next.isEditable() && next.getContentId() == pOccurance) {
                TCaseOps icd = pItem.getOpsForVersion(String.valueOf(pOccurance));
                next.deleteOpsEntity(icd);
                pItem.removeOpsForVersion(String.valueOf(pOccurance), icd);
//                it.remove();
                deleteFromTableView(next, pItem);
                return true;
            }
            deleteFromTableView(next, pItem);
        }
        return false;
    }

    private boolean isOccuranceEditable(Long pOccurance) {
        Iterator<T> it = versionList.iterator();
        while (it.hasNext()) {
            T next = it.next();
            if (next.isEditable() && next.getContentId() == pOccurance) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOccurancesEditable(List<Long> pOccurances) {
        for (Long id : pOccurances) {
            if (isOccuranceEditable(id)) {
                return true;
            }
        }
        return false;
    }

    private T getVersionForSupplementaryValue(OpsOverviewDTO pItem) {
        for (T pContent : versionList) {
            TGroupingResults result = pContent.getGroupingResult();
            if (result != null && result.getCaseOpsGroupeds() != null) {
                TCaseOps ops = pItem.getOpsForVersion(String.valueOf(pContent.getContentId()));
                for (TCaseOpsGrouped grOps : result.getCaseOpsGroupeds()) {
                    if (ops != null && grOps.getCaseOps().versionEquals(ops)) {//(grOps.getCaseOps().getId() ==  ops.getId())){
                        if (grOps.getCaseSupplFees() != null) {
                            return pContent;
                        }
                    }
                }
            }
        }
        return null;
    }

//    private String getSupplFeeTooltip(OpsOverviewDTO pValue) {
//
//        for (T pContent : versionList) {
//            if (pContent.getGroupingResult() == null) {
//                continue;
//            }
//            TCaseOps ops = pValue.getOpsForVersion(String.valueOf(pContent.getCaseDetails().getId()));
//            if (ops == null) {
//                continue;
//            }
//            for (TCaseOpsGrouped grOps : pContent.getGroupingResult().getCaseOpsGroupeds()) {
//                if (grOps.getCaseOps().versionEquals(ops)) {//(grOps.getCaseOps().getId() ==  ops.getId())){
//                    if (grOps.getCaseSupplFees() != null) {
//                        return "Cw" + grOps.getCaseSupplFees().getCsuplCwValue() + ",count=" + grOps.getCaseSupplFees().getCsuplCount();
//                    }
//                }
//            }
//
//        }
//        return null;
//    }
    private TCaseOpsGrouped getFirstSupplFee(OpsOverviewDTO pValue) { //,SupplFeeType pType){
        for (T pContent : versionList) {
            if (pContent.getGroupingResult() == null) {
                continue;
            }
            TCaseOps ops = pValue.getOpsForVersion(String.valueOf(pContent.getCaseDetails().getId()));
            if (ops == null) {
                continue;
            }
            for (TCaseOpsGrouped grOps : pContent.getGroupingResult().getCaseOpsGroupeds()) {
                if (grOps.getCaseOps().versionEquals(ops)) {//(grOps.getCaseOps().getId() ==  ops.getId())){
                    if (grOps.getCaseSupplFees() != null) {
                        return grOps;
                    }
                }
            }

        }
        return null;
    }

    private boolean hasSupplFeeInVersion(T pContent, TCaseOps pOps) {
        if (pContent.getGroupingResult() == null) {
            return false;
        }
        if (pOps == null) {
            return false;
        }
        for (TCaseOpsGrouped grOps : pContent.getGroupingResult().getCaseOpsGroupeds()) {
            if (grOps.getCaseOps().versionEquals(pOps)) {//(grOps.getCaseOps().getId() ==  ops.getId())){
                if (grOps.getCaseSupplFees() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasSupplFeeInVersion(T pContent, OpsOverviewDTO pValue) {
        TCaseOps ops = pValue.getOpsForVersion(String.valueOf(pContent.getCaseDetails().getId()));
        return hasSupplFeeInVersion(pContent, ops);
    }

    private boolean hasKisSupplFee(OpsOverviewDTO pValue) {
        for (T version : versionList) {
            if (!version.getCaseDetails().getCsdIsLocalFl()) {
                return hasSupplFeeInVersion(version, pValue);
            }
        }
        return false;
    }

    private boolean hasCpsSupplFee(OpsOverviewDTO pValue) {
        for (T version : versionList) {
            if (version.getCaseDetails().getCsdIsLocalFl()) {
                if (hasSupplFeeInVersion(version, pValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasKisAndAtleastOneCpSupplFee(OpsOverviewDTO pValue) {
        if (hasKisSupplFee(pValue) && hasCpsSupplFee(pValue)) {
            return true;
        }
        return false;
    }

    public void storeOrUpdate(T pVersion, TCaseOps pOps) {
        new UpdateTask(pVersion, pOps).start();
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                setDisable(true);
//                pVersion.saveOpsEntity(pOps);
//                pVersion.performGroup();
//                reload();
//                setDisable(false);
//            }
//        });
    }

    public final ContextMenu createContextMenu() {
        final ContextMenu contextMenu = new CtrlContextMenu<>();

        final Callback<Void, Object[][]> opsExportCallback = (param) -> {
            //final ObservableList<IcdOverviewDTO> items = FXCollections.observableArrayList();
            ObservableList<TableColumn<OpsOverviewDTO, ?>> cols;

            TableView<OpsOverviewDTO> table = new TableView<>();

            //Add columns and corresponding items from basic information table
            table.getColumns().addAll(getInfo().getColumns());
            table.setItems(getItems());

            // getTableViewToVersion().keySet();
            // for each currently selected version
            for (T ver : getVersionList()) {
                TableView<OpsOverviewDTO> tableView = getRegionForVersion(ver);
                cols = tableView.getColumns();
                //items = tableView.getItems();

                //Add columns and corresponding items for all versions
                table.getColumns().addAll(cols);
                //table.setItems(items);
            }

            Object[] rows = table.getItems().toArray();
            Object[] columns = table.getColumns().toArray();

            Object[][] data = new Object[rows.length + 1][columns.length];

            for (int i = 0; i < data.length - 1; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    if (i == 0) {
                        data[i][j] = table.getColumns().get(j).getText();
                    }
                    String label = table.getColumns().get(j).getText();
                    if (label.equals("G")) {
                        TCaseOps cellData = (TCaseOps) table.getColumns().get(j).getCellData(i);
                        if (cellData != null) {
                            data[i + 1][j] = cellData.getOpsIsToGroupFl();
                        }
                    } else if (label.equals("Datum")) {
                        TCaseOps cellData = (TCaseOps) table.getColumns().get(j).getCellData(i);
                        if (cellData != null) {
                            data[i + 1][j] = cellData.getOpscDatum();
                        }
                    } else if (label.equals("Lok.")) {
                        TCaseOps cellData = (TCaseOps) table.getColumns().get(j).getCellData(i);
                        if (cellData != null) {
                            data[i + 1][j] = cellData.getOpscLocEn().getTranslation().getValue();
                        }
                    } else {
                        data[i + 1][j] = table.getColumns().get(j).getCellData(i);
                    }
                }
            }
            return data;
        };

        final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());
        removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!delete(getSelectedItem())) {
                    MainApp.showErrorMessageDialog(Lang.getErrorCouldNotDelete());
                }
            }
        });

        MenuItem menuItemExportExcel = new MenuItem("Tabelle als XLS exportieren");
        menuItemExportExcel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String caseNumber = getBaseVersion().getCaseDetails().getHospitalCase().getCsCaseNumber();
                final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.EXCEL, "FALL_" + caseNumber + "_OPSes", opsExportCallback);
                mgr.openDialog(getScene().getWindow());
            }
        });

        MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
        menuItemExportCsv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String caseNumber = getBaseVersion().getCaseDetails().getHospitalCase().getCsCaseNumber();
                final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.CSV, "FALL_" + caseNumber + "_OPSes", opsExportCallback);
                mgr.openDialog(getScene().getWindow());
            }
        });

        contextMenu.getItems().addAll(removeMenuItem, menuItemExportExcel, menuItemExportCsv);

        contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                boolean disable = true;
                switch (getDeleteStrategy()) {
                    case DEPENTING_ON_BASE:
                        disable = getSelectedItem().isOccuringIn(getBaseVersion().getContentId());
                        break;
                    case ATLEAST_ONE_EDITABLE:
                        disable = !checkOccurancesEditable(getSelectedItem().getOccurance());
                        break;
                }
                removeMenuItem.setDisable(getSelectedItem() != null ? disable : true);
            }
        });
        return contextMenu;
    }

    /*
     * Private Classes 
     */
    //ops code column
    //if ops is not occuring in base version (version on list index 0) code is coloured blue
    private class OpsCodeColumn extends TableColumn<OpsOverviewDTO, String> {

        public OpsCodeColumn() {
            super(Lang.getCaseResolveOPS());
            setMinWidth(80.0d);
            setMaxWidth(80.0d);
            setResizable(false);
            setSortable(false);
            setReorderable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setCellValueFactory(new Callback<CellDataFeatures<OpsOverviewDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<OpsOverviewDTO, String> param) {
                    return new SimpleStringProperty(param.getValue().getOpsCode());
                }
            });
            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, String>, TableCell<OpsOverviewDTO, String>>() {
                @Override
                public TableCell<OpsOverviewDTO, String> call(TableColumn<OpsOverviewDTO, String> param) {
                    return new TableCell<OpsOverviewDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            Label label = new Label(item);
                            OpsOverviewDTO value = getTableView().getItems().get(getIndex());
                            T baseVersion = getBaseVersion();
                            if (!value.isOccuringIn(baseVersion.getCaseDetails().getId())) {
                                label.getStyleClass().add("blue-label");
                            }
                            if (value.isOnlyOccuringIn(baseVersion.getCaseDetails().getId())) {
                                label.getStyleClass().add("blue-label");
                            }
                            String aop = CpxOpsAopCatalog.instance().getCategoryDescriptionByCode(value.getOpsCode(), "de", getBaseVersion().getCatalogYear());
//                            CpxOpsAop aop = CpxOpsAopCatalog.instance().getAopByCode(value.getOpsCode(), "de", getBaseVersion().getCatalogYear());
                            if(aop == null){
                                setGraphic(label);
                            }else{
                                HBox wrapper = new HBox(label);
                                wrapper.setAlignment(Pos.CENTER_LEFT);
                                Label content = new Label(aop);
//                                AopCatalogLayout content = new AopCatalogLayout(aop);
                                 content.setStyle("-fx-text-fill:black;");
                                setGraphic(ExtendedInfoHelper.addInfoPane(wrapper, content, PopOver.ArrowLocation.TOP_LEFT));
                            }
                        }
                    };
                }
            });
        }

    }
    private final Map<String, String> catalogTextCache = new HashMap<>();

    @Override
    public void dispose() {
        getInfo().getColumns().clear();
        getInfo().skinProperty().removeListener(skinListener);
        catalogTextCache.clear();
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }

    //ops catalog text column
    //if ops is not occruning in base version (version with die index 0 in the list) text is coloured blue
    //if supplementary value is set in baseversion it is appended at the end of the catalog text
    private class OpsCodeTextColumn extends TableColumn<OpsOverviewDTO, String> {

        public OpsCodeTextColumn() {
            super(Lang.getCaseResolveOPS_TEXT());
            setMinWidth(50.0d);
            setMaxWidth(Double.MAX_VALUE);
            setSortable(false);
            setReorderable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OpsOverviewDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<OpsOverviewDTO, String> param) {

                    T baseVersion = getBaseVersion();
                    int year = baseVersion.getCatalogYear();
                    String desc = catalogTextCache.get(param.getValue().getOpsCode());
                    if (desc == null) {
                        desc = CpxOpsCatalog.instance().getDescriptionByCode(param.getValue().getOpsCode(), "de", year);
                        if (desc == null || desc.isEmpty()) {
                            desc = Lang.getCatalogOpsError(String.valueOf(year));
                        }
                        catalogTextCache.put(param.getValue().getOpsCode(), desc);
                    }
                    return new SimpleStringProperty(desc);
                }
            });
            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, String>, TableCell<OpsOverviewDTO, String>>() {
                @Override
                public TableCell<OpsOverviewDTO, String> call(TableColumn<OpsOverviewDTO, String> param) {
                    return new TableCell<OpsOverviewDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            //risky call?
                            OpsOverviewDTO value = getTableView().getItems().get(getIndex());
                            T baseVersion = getVersionForSupplementaryValue(value);//getBaseVersion();
                            String supplementary = "";
                            if (baseVersion != null) {
                                if (baseVersion.getGroupingResult() != null) {
                                    TCaseOpsGrouped opsGr = getOpsGroupingResult(value.getOpsForVersion(String.valueOf(baseVersion.getCaseDetails().getId())), baseVersion.getGroupingResult());
                                    if (opsGr != null && opsGr.getCaseSupplFees() != null) {
                                        supplementary = supplementary.concat(" (" + opsGr.getCaseSupplFees().getCsuplfeeCode() + ")");
                                    }
                                }
                            }
 
                            Label label = new Label(item + (supplementary != null ? " " + supplementary : "")/* + (aop == null?"":aop)*/);
                            if (!value.isOccuringIn(getBaseVersion().getCaseDetails().getId())) {
                                label.getStyleClass().add("blue-label");
                            }
                            if (value.isOnlyOccuringIn(getBaseVersion().getCaseDetails().getId())) {
                                label.getStyleClass().add("blue-label");
                            }
                            OverrunHelper.addOverrunInfoButton(label);

                            setGraphic(label);

                        }

                    };
                }
            });

        }
    }
//    private class DrgSupplementaryColumn extends TableColumn<OpsOverviewDTO, Number>{
//
//        public DrgSupplementaryColumn() {
//            super();
//            setMinWidth(77.0);
//            setMaxWidth(77.0);
//            setResizable(false);
//            setSortable(false);
//            setCellValueFactory(new Callback<CellDataFeatures<OpsOverviewDTO, Number>, ObservableValue<Number>>() {
//                @Override
//                public ObservableValue<Number> call(CellDataFeatures<OpsOverviewDTO, Number> p) {
//                    TCaseOpsGrouped suppl = getFirstSupplFee(p.getValue());
//                    return new SimpleDoubleProperty(suppl!=null?suppl.getCaseSupplFees().getCsuplCwValue():0.0d);
//                }
//            });
//        }
//        
//    }

    private class DrgSupplementaryColumn extends DrgSfValueColumn<OpsOverviewDTO> {

        public DrgSupplementaryColumn() {
            super();
            setMinWidth(77.0);
            setMaxWidth(77.0);
            setResizable(false);
            setSortable(false);
            setReorderable(false);
            setCellFactory(new DrgSfCellFactory());
//            sortTypeProperty().addListener(new SortColumnListener(this));
        }

        @Override
        public TCaseOpsGrouped getValue(OpsOverviewDTO pValue) {
            return getFirstSupplFee(pValue);
        }

        protected class DrgSfCellFactory extends DefaultCurrencyCellFactory {

            @Override
            public TableCell<OpsOverviewDTO, Number> call(TableColumn<OpsOverviewDTO, Number> param) {
                return new DrgSfTableCell();
            }
        }

        protected class DrgSfTableCell extends TableCell<OpsOverviewDTO, Number> {

            public DrgSfTableCell() {
                super();
                getStyleClass().add("sf-table-row-cell");
//                setStyle("-fx-background-color:red;");
            }

            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                if (item == null || empty) {
                    setGraphic(null);
                    return;
                }
                OpsOverviewDTO rowItem = getTableRow().getItem();
                if (rowItem == null) {
                    return;
                }
                OpsOverviewDTO pValue = getItems().get(getIndex());
                if (hasSupplFee(pValue)) {
                    Label label = new Label(getDisplayText(item.doubleValue()));

                    HBox wrapper = new HBox(label);
                    wrapper.setAlignment(Pos.CENTER_LEFT);
                    Label content = new Label(getTooltip(pValue));
                    content.setStyle("-fx-text-fill:black;");
                    setGraphic(ExtendedInfoHelper.addInfoPane(wrapper, content));
                    if (!hasKisAndAtleastOneCpSupplFee(rowItem)) {
                        label.setStyle("-fx-text-fill:grey;");
                    }
//                OverrunHelper.addInfoTooltip(label);
//                setGraphic(label);
                } else {
//                              OverrunHelper.addInfoTooltip(label);
                    setGraphic(null);
                }

            }

            public String getTooltip(OpsOverviewDTO pValue) {
                TCaseOpsGrouped grpOps = getValue(pValue);
                if (grpOps != null && grpOps.getCaseSupplFees() != null) {
                    TCaseSupplFee supFee = grpOps.getCaseSupplFees();
                    if (supFee.getCsuplTypeEn().equals(SupplFeeTypeEn.ZE)) {

                        return supFee.getCsuplfeeCode() + ":  " + getDisplayText(supFee.getCsuplValue());
                    }
                }
                return null;
            }

            public boolean hasSupplFee(OpsOverviewDTO pValue) {
                TCaseOpsGrouped grpOps = getValue(pValue);
                return (grpOps != null && grpOps.getCaseSupplFees() != null);
            }
        }
    }

    private class PeppSupplementaryColumn extends PeppSpDfValueColumn<OpsOverviewDTO> {

        public PeppSupplementaryColumn() {
            super();
            setMinWidth(90.0);
            setMaxWidth(90.0);
            setResizable(false);
            setSortable(false);
            setReorderable(false);
            sortTypeProperty().addListener(new SortColumnListener(this));
            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, Number>, TableCell<OpsOverviewDTO, Number>>() {
                @Override
                public TableCell<OpsOverviewDTO, Number> call(TableColumn<OpsOverviewDTO, Number> p) {
                    return new TableCell<OpsOverviewDTO, Number>() {
                        @Override
                        protected void updateItem(Number item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setTooltip(null);
                                setText("");
                                setGraphic(null);
                                return;
                            }
                            OpsOverviewDTO pValue = getItems().get(getIndex());
                            if (hasSupplFee(pValue)) {
                                Label label = new Label(getDisplayText(item.doubleValue()));
                                HBox wrapper = new HBox(label);
                                wrapper.setAlignment(Pos.CENTER_LEFT);
                                Label content = new Label(getTooltip(pValue));
                                content.setStyle("-fx-text-fill:black;");
                                setGraphic(ExtendedInfoHelper.addInfoPane(wrapper, content));

                                OpsOverviewDTO rowItem = getTableRow().getItem();
                                if (rowItem == null) {
                                    return;
                                }
                                if (!hasKisAndAtleastOneCpSupplFee(rowItem)) {
                                    label.setStyle("-fx-text-fill:grey;");
                                }
                            } else {
                                setGraphic(null);
                            }

                        }

                        public String getTooltip(OpsOverviewDTO pValue) {
                            TCaseOpsGrouped grpOps = getValue(pValue);
                            if (grpOps != null && grpOps.getCaseSupplFees() != null) {
                                TCaseSupplFee supFee = grpOps.getCaseSupplFees();
//                                TCasePepp casePepp = grpOps.getGroupingResults().getCasePepp();
                                if (supFee.getCsuplTypeEn().equals(SupplFeeTypeEn.ET)) {
                                    double br = Math.round(supFee.getCsuplValue() / (supFee.getCsuplCwValue() * supFee.getCsuplCount()));
//                                    return Lang.getDfValueTooltip(supFee.getCsuplfeeCode(), Lang.toDecimal(supFee.getCsuplCwValue(), 4), supFee.getCsuplCount(), Lang.toDecimal(br, 2) + Lang.getCurrencySymbol(), Lang.toDate(supFee.getCsuplFrom()), Lang.toDate(supFee.getCsuplTo()));
//                                            supFee.getCsuplfeeCode() + " (" + supFee.getCsuplCwValue() + "X " + supFee.getCsuplCount() + " )X Baserate "+Lang.getCreatedFrom() + Lang.toDate(supFee.getCsuplFrom()) +"bis"
//                                            + Lang.toDate(supFee.getCsuplTo());
                                    return supFee.getCsuplfeeCode() + " : " + Lang.getDailyFeeMathPepp(Lang.toDecimal(supFee.getCsuplCwValue(), 4), supFee.getCsuplCount(), Lang.toDecimal(br, 2),
                                            Lang.toDate(supFee.getCsuplFrom()), Lang.toDate(supFee.getCsuplTo()));

                                }
                                if (supFee.getCsuplTypeEn().equals(SupplFeeTypeEn.ZP)) {

                                    return supFee.getCsuplfeeCode() + ":  " + getDisplayText(supFee.getCsuplValue());
                                }
                            }
                            return null;
                        }

                        public boolean hasSupplFee(OpsOverviewDTO pValue) {
                            TCaseOpsGrouped grpOps = getValue(pValue);
                            return (grpOps != null && grpOps.getCaseSupplFees() != null);
                        }

                    };

                }

//               
            });
        }

        @Override
        public TCaseOpsGrouped getValue(OpsOverviewDTO pValue) {

            return getFirstSupplFee(pValue);

        }

    }

    /*
    * version based columns
     */
    //ops date column displayed with a datepicker
    //changing the date will be result in grouping
    //TODO: check if the new setted date is in range of the department - difficult to set in the ui
    private class OpsDateColumn extends TableColumn<OpsOverviewDTO, TCaseOps> {

        private final T version;
        private BooleanProperty armedProperty = new SimpleBooleanProperty(true);

        public OpsDateColumn(T pVersion) {
            super(Lang.getCaseResolveDate());
            version = pVersion;
            setComparator(new Comparator<TCaseOps>() {
                @Override
                public int compare(TCaseOps o1, TCaseOps o2) {
                    if (o1 == null || o1.getOpscDatum() == null) {
                        return 1;
                    }
                    if (o2 == null || o2.getOpscDatum() == null) {
                        return 0;
                    }
                    return o1.getOpscDatum().compareTo(o2.getOpscDatum());
                }
            });
            setMinWidth(10);
            setMaxWidth(Double.MAX_VALUE);
            setReorderable(false);
            sortTypeProperty().addListener(new SortColumnListener(this));
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OpsOverviewDTO, TCaseOps>, ObservableValue<TCaseOps>>() {
                @Override
                public ObservableValue<TCaseOps> call(TableColumn.CellDataFeatures<OpsOverviewDTO, TCaseOps> param) {
//                    TCaseOps ops = param.getValue().getOpsForVersion(String.valueOf(pVersion.getContentId()));
                    TCaseOps ops = param.getValue().getOpsForVersion(String.valueOf(pVersion.getCaseDetails().getId()));
                    if (ops != null) {
                        return new SimpleObjectProperty<>(ops);
                    }
                    return null;
                }
            });
            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, TCaseOps>, TableCell<OpsOverviewDTO, TCaseOps>>() {
                @Override
                public TableCell<OpsOverviewDTO, TCaseOps> call(TableColumn<OpsOverviewDTO, TCaseOps> param) {
                    return new TableCell<OpsOverviewDTO, TCaseOps>() {
                        private DatePicker dp;
                        private final ChangeListener<LocalDate> listenerDate = new ChangeListener<LocalDate>() {
                            @Override
                            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                                if (newValue != null) {
                                    getItem().setOpscDatum(Date.valueOf(newValue));
                                    storeOrUpdate(version, getItem());
                                }
                            }
                        };

                        @Override
                        protected void updateItem(TCaseOps item, boolean empty) {
//                            long start = System.currentTimeMillis();
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                dp = null;
                                setGraphic(null);
                                return;
                            }
                            if (dp == null) {
                                dp = new DatePicker();
                                dp.setMaxWidth(Double.MAX_VALUE);
                                dp.disableProperty().bind(armedProperty.not());
                                HBox wrapper = new HBox(dp);
                                HBox.setHgrow(dp, Priority.ALWAYS);
                                wrapper.setAlignment(Pos.CENTER_LEFT);
                                setGraphic(wrapper);
//                                if (item == null || empty) {
//                                    dp.setVisible(false);
//                                    return;
//                                }
                                dp.setValue(Lang.toLocalDate(item.getOpscDatum()));
                                dp.valueProperty().addListener(listenerDate);
                                dp.setConverter(new StringConverter<LocalDate>() {
                                    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Lang.getDateFormat());

                                    @Override
                                    public String toString(LocalDate date) {
                                        if (date != null) {
                                            return dateFormatter.format(date);
                                        } else {
                                            return "";
                                        }
                                    }

                                    @Override
                                    public LocalDate fromString(String string) {
                                        if (string != null && !string.isEmpty()) {
                                            LocalDate date = LocalDate.parse(string, dateFormatter);
                                            if (!isNotInRange(date)) {
                                                return date;
                                            } else {
                                                return dp.getValue();
                                            }
                                        } else {
                                            return null;
                                        }
                                    }
                                });
                                dp.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                                    @Override
                                    public DateCell call(DatePicker param) {
                                        return new DateCell() {
                                            @Override
                                            public void updateItem(LocalDate ld, boolean bln) {
                                                super.updateItem(ld, bln);
                                                boolean disable;
                                                disable = isNotInRange(ld);
                                                if (disable) {
                                                    CpxDepartmentCatalog catalog = CpxDepartmentCatalog.instance();
                                                    CpxDepartment depCatalog = catalog.getByCode(item.getCaseDepartment().getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                                                    if (depCatalog != null) {
                                                        String desc = depCatalog.getDepDescription301() + "\n"
                                                                + "(" + Lang.toDate(item.getCaseDepartment().getDepcAdmDate())
                                                                + " - "
                                                                + Lang.toDate(item.getCaseDepartment().getDepcDisDate()) + ")";
                                                        setTooltip(new CpxTooltip(desc, 5, 5000, 200, true));
                                                    }
                                                }
                                                setDisabled(disable);
                                            }

                                        };
                                    }
                                });
                            } else {
//                                long start2 = System.currentTimeMillis();
                                dp.valueProperty().removeListener(listenerDate);
                                dp.setValue(Lang.toLocalDate(item.getOpscDatum()));
                                dp.valueProperty().addListener(listenerDate);
//                                LOG.info("item to set new values " + (System.currentTimeMillis()-start2));
                            }
//                            LOG.info("update item of date in " + (System.currentTimeMillis()-start));
                        }

                        private boolean isNotInRange(LocalDate ld) {
                            return !Lang.toLocalDate(getItem().getCaseDepartment().getDepcAdmDate()).minusDays(1).isBefore(ld)
                                    || !Lang.toLocalDate(getItem().getCaseDepartment().getDepcDisDate()).plusDays(1).isAfter(ld);
                        }

                    };

                }
            });
        }

        public BooleanProperty getArmedProperty() {
            return armedProperty;
        }
    }

    private TCaseOpsGrouped getOpsGroupingResult(TCaseOps pOps, TGroupingResults pResult) {
        if (getBaseVersion().getGroupingResult() == null) {
            return null;
        }
        if (pOps == null) {
            return null;
        }
//        if(Hibernate.isInitialized(pOps.getCaseOpsGroupeds())){
//            if(!pOps.getCaseOpsGroupeds().isEmpty()){
//                return pOps.getCaseOpsGroupeds().iterator().next();
////            }else{
////                return null;
//            }
//        }
        for (TCaseOpsGrouped groupedOps : pResult.getCaseOpsGroupeds()) {//getBaseVersion().getGroupingResult().getCaseOpsGroupeds()){
            if (groupedOps.getCaseOps() == null) {
                LOG.log(Level.WARNING, "TCaseOpsGrouped: {0} has no referenceOps!", groupedOps.getId());
                continue;
            }
            //Awi-version equals for case merging
            if (groupedOps.getCaseOps().versionEquals(pOps)) {
                return groupedOps;
            }
        }
        return null;
    }

    //Localization column shows Localization in an combobox, change selection will result in grouping
    //can disable via armed property
    private class LocalizationColumn extends TableColumn<OpsOverviewDTO, TCaseOps> {

        private final T version;
        private BooleanProperty armedProperty = new SimpleBooleanProperty(true);

        public LocalizationColumn(T pVersion) {
            super(Lang.getCaseResolveLocalisationObj().getAbbreviation());
            version = pVersion;
            setReorderable(false);
            setMinWidth(65.0);
            setMaxWidth(65.0);
            setResizable(false);
            setSortable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setComparator(new Comparator<TCaseOps>() {
                @Override
                public int compare(TCaseOps o1, TCaseOps o2) {
                    if (o1 == null || o1.getOpscLocEn() == null) {
                        return 1;
                    }
                    if (o2 == null || o2.getOpscLocEn() == null) {
                        return 0;
                    }
                    return o2.getOpscLocEn().compareTo(o1.getOpscLocEn());
                }
            });
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OpsOverviewDTO, TCaseOps>, ObservableValue<TCaseOps>>() {
                @Override
                public ObservableValue<TCaseOps> call(TableColumn.CellDataFeatures<OpsOverviewDTO, TCaseOps> param) {
//                    return new SimpleObjectProperty<>(param.getValue().getOpsForVersion(String.valueOf(pVersion.getContentId())));
                    return new SimpleObjectProperty<>(param.getValue().getOpsForVersion(String.valueOf(pVersion.getCaseDetails().getId())));
                }
            });
            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, TCaseOps>, TableCell<OpsOverviewDTO, TCaseOps>>() {
                @Override
                public TableCell<OpsOverviewDTO, TCaseOps> call(TableColumn<OpsOverviewDTO, TCaseOps> param) {
                    return new TableCell<OpsOverviewDTO, TCaseOps>() {
                        private ComboBox<LocalisationEn> chkBoxLoc;
                        private final ChangeListener<LocalisationEn> listenerLoc = new ChangeListener<LocalisationEn>() {
                            @Override
                            public void changed(ObservableValue<? extends LocalisationEn> observable, LocalisationEn oldValue, LocalisationEn newValue) {
                                getItem().setOpscLocEn(newValue);
                                storeOrUpdate(version, getItem());
                            }
                        };

                        @Override
                        protected void updateItem(TCaseOps item, boolean empty) {
//                            long start = System.currentTimeMillis();
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                if (chkBoxLoc != null) {
                                    chkBoxLoc.valueProperty().removeListener(listenerLoc);
                                }
                                chkBoxLoc = null;
                                setGraphic(null);
                                return;
                            }
                            if (chkBoxLoc == null) {
                                chkBoxLoc = new ComboBox<>(FXCollections.observableArrayList(LocalisationEn.values()));

                                chkBoxLoc.disableProperty().bind(armedProperty.not());
                                chkBoxLoc.setButtonCell(new ListCell<LocalisationEn>() {
                                    @Override
                                    protected void updateItem(LocalisationEn item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (item == null || empty) {
                                            return;
                                        }
                                        setText(item.getTranslation().getAbbreviation());
                                    }

                                });
                                chkBoxLoc.setConverter(new StringConverter<LocalisationEn>() {
                                    @Override
                                    public String toString(LocalisationEn object) {
                                        return object == null ? "" : object.getTranslation().getValue();
                                    }

                                    @Override
                                    public LocalisationEn fromString(String string) {
                                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    }
                                });
                                HBox wrapper = new HBox(chkBoxLoc);
                                wrapper.setAlignment(Pos.CENTER_LEFT);
                                setGraphic(wrapper);
                                setStyle("-fx-padding: 0 0 0 0; -fx-label-padding: 0 0 0 8;");
                                chkBoxLoc.setValue(item.getOpscLocEn());
                                chkBoxLoc.valueProperty().addListener(listenerLoc);
                            } else {
                                chkBoxLoc.valueProperty().removeListener(listenerLoc);
                                chkBoxLoc.setValue(item.getOpscLocEn());
                                chkBoxLoc.valueProperty().addListener(listenerLoc);
                            }
//                            LOG.info("update item of loc in " + (System.currentTimeMillis() - start));
                        }
                    };
                }
            });
        }

        private BooleanProperty getArmedProperty() {
            return armedProperty;
        }
    }

    //use for group column
    //displayed as checkbox, is the ops grouper relevant than is the checkbox coloured orange
    //change selection will result in grouping
    private class UseGrouperColumn extends TableColumn<OpsOverviewDTO, TCaseOps> {

        private final T version;
        private BooleanProperty armedProperty = new SimpleBooleanProperty(true);

        public UseGrouperColumn(T pVersion) {
            super(Lang.getCaseResolveUsedForGrouping());
            version = pVersion;
            setReorderable(false);
            setMinWidth(32.0);
            setMaxWidth(32.0);
            setResizable(false);
            setSortable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));

            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OpsOverviewDTO, TCaseOps>, ObservableValue<TCaseOps>>() {
                @Override
                public ObservableValue<TCaseOps> call(TableColumn.CellDataFeatures<OpsOverviewDTO, TCaseOps> param) {
//                    TCaseOps ops = param.getValue().getOpsForVersion(String.valueOf(version.getContentId()));
                    TCaseOps ops = param.getValue().getOpsForVersion(String.valueOf(version.getCaseDetails().getId()));
                    if (ops != null) {
                        return new SimpleObjectProperty<>(ops);
                    }
                    return null;
                }
            });
            setCellFactory(new Callback<TableColumn<OpsOverviewDTO, TCaseOps>, TableCell<OpsOverviewDTO, TCaseOps>>() {
                @Override
                public TableCell<OpsOverviewDTO, TCaseOps> call(TableColumn<OpsOverviewDTO, TCaseOps> param) {
                    return new TableCell<OpsOverviewDTO, TCaseOps>() {
                        private CheckBox chkGr;

                        @Override
                        protected void updateItem(TCaseOps item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                chkGr = null;
                                setGraphic(null);
                                return;
                            }
                            if (chkGr == null) {
                                chkGr = new CheckBox("");
                                setGraphic(chkGr);
                                chkGr.disableProperty().bind(armedProperty.not());
                                chkGr.setPadding(new Insets(0, -7, 0, 0));
                                setAlignment(Pos.CENTER);
                                setStyle("-fx-padding: 0 0 0 0;");
                                chkGr.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        item.setOpsIsToGroupFl(chkGr.isSelected());
                                        chkGr.setSelected(!chkGr.isSelected());
                                        storeOrUpdate(version, item);
                                    }
                                });
                                chkGr.setSelected(item.getOpsIsToGroupFl());
                                //CPX-1494 RSH 19.03.2019 
                                if (isUsedForGrouping(item, version.getGroupingResult())) {
                                    chkGr.setTooltip(new Tooltip("Die Prozedur ist " + version.getGroupingResult().getGrpresType() + " relevant!"));
                                    highlight(chkGr, true);
                                } else {
                                    chkGr.setTooltip(null);
                                    highlight(chkGr, false);
                                }
//                                if ((isUsedForGrouping(item, version.getGroupingResult()))) {
//                                    chkGr.getStyleClass().add("orange-check-box");
//                                }
                            } else {
                                chkGr.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
//                                        if(event.getClickCount()>1){
//                                            event.consume();
//                                            return;
//                                        }
                                        item.setOpsIsToGroupFl(chkGr.isSelected());
                                        chkGr.setSelected(!chkGr.isSelected());
                                        storeOrUpdate(version, item);
                                    }
                                });
                                chkGr.setSelected(item.getOpsIsToGroupFl());
                                if (isUsedForGrouping(item, version.getGroupingResult())) {
                                    chkGr.setTooltip(new Tooltip("Die Prozedur ist " + version.getGroupingResult().getGrpresType() + " relevant!"));
                                    highlight(chkGr, true);
                                } else {
                                    chkGr.setTooltip(null);
                                    highlight(chkGr, false);
                                }
                            }
                        }

                    };
                }
            });
        }
//        private static final String HIGHLIGHTING = "orange-check-box";
//        private void addHighlighting(Control pCtrl){
//            highlight(pCtrl, true);
//        }
//        private void removeHighlighting(Control pCtrl){
//            highlight(pCtrl, false);
//        }
//        private void highlight(Control pControl,boolean highlight){
//            if (highlight) {
//                pControl.getStyleClass().add(HIGHLIGHTING);
//            }else{
//                pControl.getStyleClass().remove(HIGHLIGHTING);
//            }
//        }

        private boolean isUsedForGrouping(TCaseOps pOps, TGroupingResults pGroupingResult) {
            if (pGroupingResult == null) {
                return false;
            }
            for (TCaseOpsGrouped grOps : pGroupingResult.getCaseOpsGroupeds()) {
                //prevent null pointer if somehow the icd reference of the grouped icd is null
                if (grOps.getCaseOps() == null) {
                    LOG.log(Level.WARNING, "TCaseOpsGrouped: {0} has no referenceOps!", grOps.getId());
                    continue;
                }
                if (grOps.getCaseOps().equals(pOps)) {
                    if (grOps.getOpsResU4gFl()) {
                        return true;
                    }
                }
            }
            return false;
        }

        private BooleanProperty getArmedProperty() {
            return armedProperty;
        }
    }

    /**
     * Table view vor version based content shoes ops date and is used for group
     * columns
     */
    protected class OpsVersionTableView extends AsyncTableView<OpsOverviewDTO> {

        private final BooleanProperty armedProperty = new SimpleBooleanProperty(true);
        private RedirectScrollHandler DEFAULT_SCROLL_HANDLER = new RedirectScrollHandler();
        private ChangeListener<Skin<?>> SKIN_LISTENER = new ChangeListener<Skin<?>>() {
            @Override
            public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                for (Node n : lookupAll(".scroll-bar")) {
                    if (n instanceof ScrollBar) {
                        ScrollBar bar = (ScrollBar) n;
                        if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                            bar.setDisable(true);
                            ScrollBar scrollBar = OpsTablePane.this.getVScrollBar();
                            scrollBar.visibleProperty().bindBidirectional(bar.visibleProperty());
                            scrollBar.visibleAmountProperty().bindBidirectional(bar.visibleAmountProperty());
//                                scrollBar.valueProperty().bindBidirectional(bar.valueProperty());
                            scrollBar.minProperty().bindBidirectional(bar.minProperty());
                            scrollBar.maxProperty().bindBidirectional(bar.maxProperty());
                        }
                    }
                }
            }
        };
        private ChangeListener<Number> SCROLL_BAR_LISTENER = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                OpsVersionTableView.this.setVPos(newValue.doubleValue());
            }
        };
        private ChangeListener<Integer> SELECTED_ROW_LISTENER = new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
                        getSelectionModel().select(newValue);
//                    }
//                });
            }
        };
        private ChangeListener<Number> SELECTED_INDEX_LISTENER = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                OpsTablePane.this.setSelectedRow(newValue.intValue());
            }
        };
        private ChangeListener<Skin<?>> REORDER_LISTENER = new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> obs, Skin<?> oldSkin, Skin<?> newSkin) {
                    ((AsyncTableViewSkin) newSkin).setReordering(false);
                }
            };
        @SuppressWarnings("unchecked")
        public OpsVersionTableView(T pVersion) {
            super(false);
            skinProperty().addListener(REORDER_LISTENER);
            setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);
            getStyleClass().add("stay-selected-table-view");
            UseGrouperColumn grCol = new UseGrouperColumn(pVersion);
            grCol.getArmedProperty().bind(armedProperty);

            OpsDateColumn dateCol = new OpsDateColumn(pVersion);
            dateCol.getArmedProperty().bind(armedProperty);

            LocalizationColumn locCol = new LocalizationColumn(pVersion);
            locCol.getArmedProperty().bind(armedProperty);

            //workaroung for bug in automatic computing of column sizes 
            //by http://bekwam.blogspot.de/2016/02/getting-around-javafx-tableview.html 
            //Awi:20170330
            dateCol.prefWidthProperty().bind(
                    widthProperty()
                            .subtract(grCol.widthProperty())
                            .subtract(locCol.widthProperty())
                            .subtract(2) // a border stroke?
            );
            //after setting width, prevent resizing
            dateCol.setResizable(false);
            getColumns().addAll(grCol, dateCol, locCol);
            setRowContextMenu(createContextMenu());
//            rowFactoryProperty().bind(OpsTablePane.this.getRowFactoryProperty());
            if (pVersion.isCanceled()) {
                armedProperty.setValue(false);
            }
            //kis version
            if (pVersion.getContent() != null && !pVersion.isEditable()) {
                armedProperty.set(false);
            }
            getSelectionModel().selectedIndexProperty().addListener(SELECTED_INDEX_LISTENER);
            OpsTablePane.this.getSelectedRowProperty().addListener(SELECTED_ROW_LISTENER);
            //bind scrollbar and hide unnecessary bars
            getStyleClass().add("remove-all-scroll-bars");
            getVScrollBar().valueProperty().addListener(SCROLL_BAR_LISTENER);
            skinProperty().addListener(SKIN_LISTENER);
            addEventFilter(ScrollEvent.ANY, DEFAULT_SCROLL_HANDLER);
        }
        private List<TableColumn<OpsOverviewDTO, ?>> tempSort;

        @Override
        public void beforeTask() {
            tempSort = new ArrayList<>(getSortOrder());
//            getSortOrder().clear();
            super.beforeTask(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Future<List<OpsOverviewDTO>> getFuture() {
            LOG.log(Level.FINE, "update version content with item list size {0}", OpsTablePane.this.getItems().size());
            return new AsyncResult<>(OpsTablePane.this.getItems());
        }

        @Override
        public void afterTask(Worker.State pState) {
            super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
            //UGLY workaround to reapply sorting 
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
//                    TableColumn<OpsOverviewDTO, ?> col = getColumns().get(1);
                    getSortOrder().clear();
                    for (TableColumn<OpsOverviewDTO, ?> col : tempSort) {
                        if (col.getSortType() != null) {
                            getSortOrder().add(col);
                        }
                    }
                    setVPos(getVScrollBar().getValue());
                }
            });
        }

        @Override
        public void dispose() {
            super.dispose(); //To change body of generated methods, choose Tools | Templates.
            removeEventFilter(ScrollEvent.ANY, DEFAULT_SCROLL_HANDLER);
            skinProperty().removeListener(SKIN_LISTENER);
            skinProperty().removeListener(REORDER_LISTENER);
            getVScrollBar().valueProperty().removeListener(SCROLL_BAR_LISTENER);
            getSelectionModel().selectedIndexProperty().removeListener(SELECTED_INDEX_LISTENER);
            OpsTablePane.this.getSelectedRowProperty().removeListener(SELECTED_ROW_LISTENER);
            DEFAULT_SCROLL_HANDLER = null;
            SELECTED_INDEX_LISTENER = null;
            SELECTED_ROW_LISTENER = null;
            SKIN_LISTENER = null;
            SCROLL_BAR_LISTENER = null;
            REORDER_LISTENER = null;
        }
    }
}

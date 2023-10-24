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
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.layout.SimulIcdLayout;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.client.core.model.fx.comparablepane.TableCompPane;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.contextmenu.IcdRefTypeMenu;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableViewSkin;
import de.lb.cpx.client.core.util.ExcelCsvFileManager;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcdGrouped;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ejb.AsyncResult;
import org.controlsfx.control.PopOver;

import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.TCaseDrg;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.FontAwesome;
/**
 * Icd TableCompPane, shows the different version based and non-based
 * informations with TableViews
 *
 * @author wilde
 * @param <T> compareable content instance
 */
public class IcdTablePane<T extends ComparableContent<? extends AbstractEntity>> extends TableCompPane<IcdOverviewDTO, T> {

    private static final Logger LOG = Logger.getLogger(IcdTablePane.class.getName());

    private final Comparator<IcdOverviewDTO> sortByCode = Comparator.comparing(IcdOverviewDTO::hasHbxFl).reversed().thenComparing(IcdOverviewDTO::getIcdCode);

    private SimpleDoubleProperty fullWidth = new SimpleDoubleProperty();
    /**
     * construct new IcdTablePane initialize context menu, disabling reordering
     * Facade for service access must be initialized separately
     */
    @SuppressWarnings("unchecked")
    public IcdTablePane() {
        super();
        getInfo().getColumns().addAll(new IcdCodeColumn(), new IcdCodeTextColumn());//,new LocalisationColumn());
        //deactivate reodering 
        getInfo().skinProperty().addListener(skinListener);
        getInfo().setRowContextMenu(createContextMenu(getInfo()));

    }

    private final Map<String, String> catalogTextCache = new HashMap<>();

    @Override
    public void dispose() {
        getInfo().getColumns().clear();
        getInfo().skinProperty().removeListener(skinListener);
        catalogTextCache.clear();
        super.dispose();
    }
    
    public double getFullCollWidth(){
        return fullWidth.get();
    }
    
    

    @Override
    public AsyncTableView<IcdOverviewDTO> createNewVersionTableView(T pContent) {
        return new IcdVersionTableView(pContent);
    }

    @Override
    public boolean delete(IcdOverviewDTO pItem) {
        if (pItem == null) {
            return false;
        }
        ArrayList<Long> listOfVersions = new ArrayList<>(pItem.getOccurance());

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

    public void addIcds(List<TCaseIcd> pIcds) {
        for (TCaseIcd icd : pIcds) {
            addIcd(icd);
        }
        sortByCode();
    }

    public void sortByCode() {
        getItems().sort(sortByCode);
    }

    public void storeOrUpdate(T pVersion, TCaseIcd pIcd) {
        UpdateTask updateTask = new UpdateTask(pVersion, pIcd);
        updateTask.start();
//        setDisabled(true);
//        pVersion.saveIcdEntity(pIcd);
//        pVersion.performGroup();
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                reload();
//                setDisabled(false);
//            }
//        });
    }

    private boolean deleteInVersion(long pOccurance, IcdOverviewDTO pItem) {
        Iterator<T> it = versionList.iterator();
        while (it.hasNext()) {
            T next = it.next();
            if (next.isEditable() && next.getContentId() == pOccurance) {
                TCaseIcd icd = pItem.getIcdForVersion(pOccurance);
                //CPX-515, cant delete md
                if (icd.getIcdcIsHdbFl()) {
                    return false;
                }
                next.deleteIcdEntity(icd);
                pItem.removeIcdForVersion(String.valueOf(pOccurance), icd);
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

    private void addIcd(TCaseIcd pIcd) {
        IcdOverviewDTO newIcd = new IcdOverviewDTO(pIcd.getIcdcCode(), pIcd.getIcdcLocEn(), "");
        for (T version : versionList) {
            if (version.isEditable()) {
                newIcd.getOccurance().add(version.getCaseDetails().getId());
                newIcd.addIcdForVersion(String.valueOf(version.getCaseDetails().getId()), pIcd);
            }
        }
        getItems().add(newIcd);

    }

    private ContextMenu createContextMenu(AsyncTableView<IcdOverviewDTO> pView) {
        final ContextMenu contextMenu = new CtrlContextMenu<>();

        final Callback<Void, Object[][]> icdExportCallback = (param) -> {
            //final ObservableList<IcdOverviewDTO> items = FXCollections.observableArrayList();
            ObservableList<TableColumn<IcdOverviewDTO, ?>> cols;

            TableView<IcdOverviewDTO> table = new TableView<>();

            //Add columns and corresponding items from basic information table
            table.getColumns().addAll(getInfo().getColumns());
            table.setItems(getItems());
            // for each currently selected version
            for (T ver : getVersionList()) {
                
                TableView<IcdOverviewDTO> tableView = getRegionForVersion(ver);
                cols = tableView.getColumns();
                //items = tableView.getItems();
                //Add columns and corresponding items for all versions
                table.getColumns().addAll(cols);
                //table.setItems(items);
//                                        String csCaseNumber = ver.getCaseDetails().getHospitalCase().getCsCaseNumber();
            }

            Object[] rows = table.getItems().toArray();
            Object[] columns = table.getColumns().toArray();

            Object[][] data = new Object[rows.length + 1][columns.length];

            for (int i = 0;
                    i < data.length - 1; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    if (i == 0) {
                        data[i][j] = table.getColumns().get(j).getText();
                    }
                    String label = table.getColumns().get(j).getText();
                    if (label.equals("Lok.")) {
                        TCaseIcd cellData = (TCaseIcd) table.getColumns().get(j).getCellData(i);
                        if (cellData != null) {
                            if (cellData.getIcdcLocEn().equals(LocalisationEn.E)) {
                                data[i + 1][j] = cellData.getIcdcLocEn().toString();
                            } else {
                                data[i + 1][j] = cellData.getIcdcLocEn().getTranslation().getValue();
                            }
                        }
                    } else if (label.equals("CCL")) {
                        Number cellData = (Number) table.getColumns().get(j).getCellData(i);
                        if (cellData != null) {
                            if (cellData.intValue() != -1) {
                                data[i + 1][j] = cellData.toString();
                            } else {
                                data[i + 1][j] = "";
                            }
                        }
                    } else if (label.equals("G")) {
                        TCaseIcd cellData = (TCaseIcd) table.getColumns().get(j).getCellData(i);
                        if (cellData != null) {
                            data[i + 1][j] = cellData.getIcdIsToGroupFl();
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
                final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.EXCEL, "FALL_" + caseNumber + "_ICDs", icdExportCallback);
                mgr.openDialog(getScene().getWindow());
            }

        });

        MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
        menuItemExportCsv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String caseNumber = getBaseVersion().getCaseDetails().getHospitalCase().getCsCaseNumber();
                final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.CSV, "FALL_" + caseNumber + "_ICDs", icdExportCallback);
                mgr.openDialog(getScene().getWindow());
            }
        });

//                SeparatorMenuItem sep = new SeparatorMenuItem();
        contextMenu.getItems().addAll(removeMenuItem, menuItemExportExcel, menuItemExportCsv);

        contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                boolean disable = true;
                switch (getDeleteStrategy()) {
                    case DEPENTING_ON_BASE:
                        //Check if item in row is main diagnosis, disable in that case
                        disable = getSelectedItem().isOccuringIn(getBaseVersion().getContentId()) || getSelectedItem().hasHbxFl();
                        break;
                    case ATLEAST_ONE_EDITABLE:
                        disable = !checkOccurancesEditable(getSelectedItem().getOccurance());
                        break;
                }
                T version = getVersionForTableView(pView);
//                        disable = row.getItem().hasHbxFl();
                removeMenuItem.setDisable(getSelectedItem() != null ? disable : true);
                if (version == null) {
                    return;
                }
                IcdRefTypeMenu menu = new IcdRefTypeMenu(pView, getCandidateList(version, pView.getItems()), getBaseVersion().getCatalogYear()) {
                    @Override
                    public TCaseIcd getItem() {
                        return getSelectedItem().getIcdForVersion(version.getCaseDetails().getId());
                    }
                };
                menu.setOnChange(new Callback<TCaseIcd, Void>() {
                    @Override
                    public Void call(TCaseIcd param) {
                        version.saveIcdEntity(param);
                        return null;
                    }
                });
                menu.disable(!version.isEditable());
                contextMenu.getItems().clear();
                contextMenu.getItems().addAll(removeMenuItem, menuItemExportExcel, menuItemExportCsv);
                contextMenu.getItems().addAll(menu.getItems());
            }
        });
        return contextMenu;

    }
//    @Override
//    public void applySort(TableColumn pColumn) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    //icd code column, shows code of the over view dto, text is coloured blue if icd do not occure in base version (version on place 0 in the list)

    private class SimulatedGroupResult  extends TableColumn<IcdOverviewDTO, String> {


        public SimulatedGroupResult(T pVersion) {
            super(pVersion.getCaseType().name());
//            version = pVersion;
            setMinWidth(50);
            setMaxWidth(50);
            setSortable(false);
            setReorderable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IcdOverviewDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<IcdOverviewDTO, String> param) {
                    TCaseIcd value = pVersion.getCaseDetails() == null ? null : param.getValue().getIcdForVersion(pVersion.getCaseDetails().getId());
                    LOG.log(Level.FINER, "SimulatedGroupResult: versionId= {0}, param ={1}, value={2}", 
                            new Object[]{pVersion.getCaseDetails().getId(), param.getValue().getIcdCode(), value == null?"null":value.getIcdcCode()});
                    return new SimpleStringProperty(getSimulGroupCode(value, pVersion.getGroupingResult()));
                }
            });
            
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, String>, TableCell<IcdOverviewDTO, String>>() {
                @Override
                public TableCell<IcdOverviewDTO, String> call(TableColumn<IcdOverviewDTO, String> param) {
                    TableCell tmp = new TableCell<IcdOverviewDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//                            long start = System.currentTimeMillis();
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }

                            if (item.isEmpty()) {
                                setGraphic(null);
                                return;
                            }
                            IcdOverviewDTO value = getTableView().getItems().get(getIndex());
                            TCaseIcd currIcd = value.getIcdForVersion(pVersion.getCaseDetails().getId());
                            TGroupingResults gr = getSimulGroupResult(currIcd, pVersion.getGroupingResult());
                            Label label = new Label(item);
                            HBox wrapper = new HBox(label);
                            wrapper.setAlignment(Pos.CENTER_LEFT);
                            SimulIcdLayout content = new SimulIcdLayout(gr, pVersion.getCaseDetails().getCsdLos(), pVersion.getCaseDetails().getCsdAdmReason12En());  

//                            VBox content = getToolTip4Type(pVersion.getCaseType());
                            content.setStyle("-fx-text-fill:black;");
                            setGraphic(ExtendedInfoHelper.addInfoPane(wrapper, content, PopOver.ArrowLocation.TOP_RIGHT));
//                            LOG.info("time to update sec diag " + (System.currentTimeMillis()-start));
                        }

                    };
                    return tmp;
                }
            });
            //remove padding, to avoid text overrun
            setStyle("-fx-padding: 0 0 0 0; -fx-label-padding: 0 0 0 8;");
        }
        

        private String getSimulGroupCode(TCaseIcd pIcd, TGroupingResults pGroupingResult) {
            LOG.log(Level.FINER, "icd: {0}, grResult: {1}, icdDrg: {2}", 
                    new Object[]{pIcd == null?"null":pIcd.getIcdcCode(), 
                        pGroupingResult == null?"null":pGroupingResult.getGrpresCode(),
                        pIcd == null?"null":
                                ( pIcd.getGroupingResultses() == null?"null": 
                                        (pIcd.getGroupingResultses().isEmpty()?"empty":
                                                "has result"))
                    });
            if (pGroupingResult == null || pIcd == null) {
                return "";
            }
 
            Set<TGroupingResults> grRess = pIcd.getGroupingResultses();
            if(grRess != null){
                for(TGroupingResults grRes : grRess){
                    if(grRes.getModelIdEn().equals(pGroupingResult.getModelIdEn()) && grRes.getGrpresIsAutoFl() == pGroupingResult.getGrpresIsAutoFl()){
                        return grRes.getGrpresCode();
                    }
                }
            }


            return "";
        }
        
        
        private TGroupingResults getSimulGroupResult(TCaseIcd pIcd, TGroupingResults pGroupingResult) {
            if (pGroupingResult == null || pIcd == null) {
                return null;
            }
 
            Set<TGroupingResults> grRess = pIcd.getGroupingResultses();
            if(grRess != null){
                for(TGroupingResults grRes : grRess){
                    if(grRes.getModelIdEn().equals(pGroupingResult.getModelIdEn()) && grRes.getGrpresIsAutoFl() == pGroupingResult.getGrpresIsAutoFl()){
//                        if(grRes.getGrpresType().equals(CaseTypeEn.DRG)){
//                            TCaseDrg drg = grRes.getCaseDrg();
//                        LOG.log(Level.INFO, (pIcd.getIcdcIsHdxFl()?"HDX:":"ND") + " ICD: {0}, DRG: {1} uncorr.CW: {2} corrCw: {3} careDays: {4} careCwDay: {5}",
//                                new Object[]{pIcd.getIcdcCode(),
//                                    grRes.getGrpresCode(), 
//                                    drg.getDrgcCwCatalog(),
//                                    drg.getDrgcCwEffectiv(),
//                                    drg.getDrgcCareDays(),
//                                    drg.getDrgcCareCwDay()
//                                });
//                        }
                        
                        return grRes;
                    }
                }
            }


            return null;
        }

    }

    private class IcdCodeColumn extends TableColumn<IcdOverviewDTO, String> {

        public IcdCodeColumn() {
            super(Lang.getCaseResolveICD());
            setReorderable(false);
            setMinWidth(80.0d);
            setMaxWidth(80.0d);
            setResizable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setSortable(false);
            setCellValueFactory(new Callback<CellDataFeatures<IcdOverviewDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<IcdOverviewDTO, String> param) {
                    return new SimpleObjectProperty<>(param.getValue().getIcdCode());
                }
            ;
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, String>, TableCell<IcdOverviewDTO, String>>() {
                @Override
                public TableCell<IcdOverviewDTO, String> call(TableColumn<IcdOverviewDTO, String> param) {
                    return new TableCell<IcdOverviewDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
//                            long start = System.currentTimeMillis();
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            IcdOverviewDTO value = getTableView().getItems().get(getIndex());
                            Label label = new Label(item);
                            T baseVersion = getBaseVersion();
                            if (!value.isOccuringIn(baseVersion.getCaseDetails().getId())) {
                                label.getStyleClass().add("blue-label");
                            }
                            if (value.isOnlyOccuringIn(baseVersion.getCaseDetails().getId())) {
                                label.getStyleClass().add("blue-label");
                            }
                            setGraphic(label);
//                            LOG.info("time to update icd code " + (System.currentTimeMillis()-start));
                        }

                    };
                }
            });
        }
    }

//icd catalog text column
//text coloured blue if icd is not occuring in the base version ( version on index 0 in the list)
    private class IcdCodeTextColumn extends TableColumn<IcdOverviewDTO, String> {

        public IcdCodeTextColumn() {
            super(Lang.getCaseResolveICD_Text());
            setMinWidth(50.0d);
            setMaxWidth(Double.MAX_VALUE);
            setSortable(false);
            setReorderable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IcdOverviewDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<IcdOverviewDTO, String> param) {
                    T baseVersion = getBaseVersion();
                    int year = baseVersion.getCatalogYear();
                    String desc = catalogTextCache.get(param.getValue().getIcdCode());
                    if (desc == null) {
                        desc = CpxIcdCatalog.instance().getDescriptionByCode(param.getValue().getIcdCode(), "de", year);
                        if (desc == null || desc.isEmpty()) {
                            desc = Lang.getCatalogIcdError(String.valueOf(year));
                        }
                        catalogTextCache.put(param.getValue().getIcdCode(), desc);
                    }
                    return new SimpleStringProperty(desc);
//                    T baseVersion = getBaseVersion();
//                    int year = baseVersion.getCatalogYear();
//                    String desc = CpxIcdCatalog.instance().getDescriptionByCode(param.getValue().getIcdCode(), "de", year);
//                    if(desc == null || desc.isEmpty()){
//                        desc = Lang.getCatalogIcdError(String.valueOf(year));
//                    }
//                    return new SimpleStringProperty(desc);
                }
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, String>, TableCell<IcdOverviewDTO, String>>() {
                @Override
                public TableCell<IcdOverviewDTO, String> call(TableColumn<IcdOverviewDTO, String> param) {
                    return new TableCell<IcdOverviewDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
//                            long start = System.currentTimeMillis();
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            T baseVersion = getBaseVersion();
                            //risky call?
                            IcdOverviewDTO value = getTableView().getItems().get(getIndex());
                            Label label = new Label(item);
                            if (!value.isOccuringIn(baseVersion.getCaseDetails().getId())) {
                                label.getStyleClass().add("blue-label");
                            }
                            if (value.isOnlyOccuringIn(baseVersion.getCaseDetails().getId())) {
                                label.getStyleClass().add("blue-label");
                            }
                            OverrunHelper.addOverrunInfoButton(label);

                            setGraphic(label);
//                            LOG.info("time to update icd text " + (System.currentTimeMillis()-start));
                        }

                    };
                }
            });

        }
    }

    /*
    * version specific content
     */
//Localization column shows Localization in an combobox, change selection will result in grouping
//can disable via armed property
    private class LocalizationColumn extends TableColumn<IcdOverviewDTO, TCaseIcd> {

//        private final T version;
        private BooleanProperty armedProperty = new SimpleBooleanProperty(true);

        public LocalizationColumn(T pVersion) {
            super(Lang.getCaseResolveLocalisationObj().getAbbreviation());
//            version = pVersion;
            setMinWidth(58.0);
            setMaxWidth(58.0);
            setResizable(false);
            setSortable(false);
            setReorderable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IcdOverviewDTO, TCaseIcd>, ObservableValue<TCaseIcd>>() {
                @Override
                public ObservableValue<TCaseIcd> call(TableColumn.CellDataFeatures<IcdOverviewDTO, TCaseIcd> param) {
                    return new SimpleObjectProperty<>(pVersion.getCaseDetails() == null ? null : param.getValue().getIcdForVersion(pVersion.getCaseDetails().getId()));
                }
            });
            setComparator(new Comparator<TCaseIcd>() {
                @Override
                public int compare(TCaseIcd o1, TCaseIcd o2) {
                    if (o1 == null || o1.getIcdcLocEn() == null) {
                        return 1;
                    }
                    if (o2 == null || o2.getIcdcLocEn() == null) {
                        return 0;
                    }
                    return o2.getIcdcLocEn().compareTo(o1.getIcdcLocEn());
                }
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, TCaseIcd>, TableCell<IcdOverviewDTO, TCaseIcd>>() {
                @Override
                public TableCell<IcdOverviewDTO, TCaseIcd> call(TableColumn<IcdOverviewDTO, TCaseIcd> param) {
                    return new TableCell<IcdOverviewDTO, TCaseIcd>() {
                        private ComboBox<LocalisationEn> chkBoxLoc;
                        private final ChangeListener<LocalisationEn> listenerLoc = new ChangeListener<LocalisationEn>() {
                            @Override
                            public void changed(ObservableValue<? extends LocalisationEn> observable, LocalisationEn oldValue, LocalisationEn newValue) {

                                getItem().setIcdcLocEn(newValue);
//                                    version.saveIcdEntity(item);
//                                    version.performGroup();
                                storeOrUpdate(pVersion, getItem());
                            }
                        };

                        @Override
                        protected void updateItem(TCaseIcd item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
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
                                        throw new UnsupportedOperationException("Not supported yet.");
                                    }
                                });
                                chkBoxLoc.setValue(item.getIcdcLocEn());
                                HBox wrapper = new HBox(chkBoxLoc);
                                wrapper.setAlignment(Pos.CENTER_LEFT);
                                setGraphic(wrapper);
                                chkBoxLoc.valueProperty().addListener(listenerLoc);
                                setStyle("-fx-padding: 0 0 0 0;");
                                setStyle("-fx-label-padding: 0 0 0 8;");
                            } else {
                                chkBoxLoc.valueProperty().removeListener(listenerLoc);
                                chkBoxLoc.setValue(item.getIcdcLocEn());
                                chkBoxLoc.valueProperty().addListener(listenerLoc);
                            }
                        }

                    };
                }
            });
        }

        private BooleanProperty getArmedProperty() {
            return armedProperty;
        }
    }

//main diagnosis column
//shows main diagnosis with toggled radiobutton, changing radiobutton will result in grouping
//buttons are not in an togglegroup! 
//disable via armedproperty
    private class MainDiagnosisColumn extends TableColumn<IcdOverviewDTO, Boolean> {

//        private final T version;
        private BooleanProperty armedProperty = new SimpleBooleanProperty(true);

        public MainDiagnosisColumn(T pVersion) {
            super(Lang.getCaseResolveMd());
//            version = pVersion;
            setMinWidth(32);
            setMaxWidth(32);
            setReorderable(false);
            setResizable(false);
            setSortable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));

            setCellValueFactory(new Callback<CellDataFeatures<IcdOverviewDTO, Boolean>, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(CellDataFeatures<IcdOverviewDTO, Boolean> param) {
                    TCaseIcd icd = pVersion.getCaseDetails() == null ? null : param.getValue().getIcdForVersion(pVersion.getCaseDetails().getId());
                    return new SimpleObjectProperty<>(icd != null ? icd.getIcdcIsHdxFl() : null);
                }
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, Boolean>, TableCell<IcdOverviewDTO, Boolean>>() {
                @Override
                public TableCell<IcdOverviewDTO, Boolean> call(TableColumn<IcdOverviewDTO, Boolean> param) {
                    return new MainDiagnosisCell(pVersion);
                }
            });
        }

        private BooleanProperty getArmedProperty() {
            return armedProperty;
        }

        private class MainDiagnosisCell extends TableCell<IcdOverviewDTO, Boolean> {

            private final RadioButton rbMd = new RadioButton();
            HBox wrapper = new HBox(rbMd);

            public MainDiagnosisCell(T pVersion) {
                super();
                setStyle("-fx-padding: 0 0 0 0;");
                wrapper.setFillHeight(true);
                wrapper.setAlignment(Pos.CENTER);

                rbMd.setPadding(new Insets(0, -7, 0, 0));
                rbMd.disableProperty().bind(armedProperty.not());
                rbMd.addEventFilter(MouseEvent.MOUSE_CLICKED, (event) -> {
                    if(getTableView().getItems().isEmpty() || (getIndex()>getTableView().getItems().size())){
                        LOG.warning("ignore mouse click event in md cell, client seems occupied grouping already!");
                        event.consume();
                        return;
                    }
                    IcdOverviewDTO value = getTableView().getItems().get(getIndex());
                    TCaseIcd currIcd = value.getIcdForVersion(pVersion.getCaseDetails().getId());
                    //ignore click if item has hbx flag 
                    if (getItem()) {
                        rbMd.setSelected(true);
                        event.consume();
                        return;
                    }
//                    if (!computeSecDiagnosis(currIcd).isEmpty()) {
                    if (isSecondary(currIcd)) {
                        MainApp.showErrorMessageDialog(Lang.getMainDiagnosisError());
                        rbMd.setSelected(false);
                    } else {
                        for (IcdOverviewDTO dto : getItems()) {
                            TCaseIcd icd = dto.getIcdForVersion(pVersion.getCaseDetails().getId());
                            if (icd != null) {
                                if (icd.getIcdcIsHdxFl()) {
                                    icd.setIcdcIsHdxFl(false);
                                    pVersion.saveIcdEntity(icd);
                                }
                            }
                        }
                        currIcd.setIcdcIsHdxFl(true);
                        currIcd.setIcdIsToGroupFl(true);
                        storeOrUpdate
        (pVersion, currIcd);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean t, boolean bln) {
                super.updateItem(t, bln);
                if (t == null || bln) {
                    setGraphic(null);
                    return;
                }
                rbMd.setSelected(t);
                setGraphic(wrapper);
            }

            private boolean isSecondary(TCaseIcd icd) {
//                if (icd.getIcdcReftypeEn() != null) {
                if (IcdcRefTypeEn.Stern.equals(icd.getIcdcReftypeEn()) || IcdcRefTypeEn.Zusatz.equals(icd.getIcdcReftypeEn())) {
                    return true;
                }
//                }
                return false;
            }

        }
    }

//use for grouping column
//displayed with checkbox
//changing selected value in checkbox will result in grouping
//can not deselect when icd is main diagnosis
//if icd is relevant for the grouping result it is colored orange
    private class UseGrouperColumn extends TableColumn<IcdOverviewDTO, TCaseIcd> {

//        private final T version;
        private BooleanProperty armedProperty = new SimpleBooleanProperty(true);

        public UseGrouperColumn(T pVersion) {
            super(Lang.getCaseResolveUsedForGrouping());
//            version = pVersion;
            setMinWidth(32);
            setMaxWidth(32);
            setResizable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setSortable(false);
            setReorderable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IcdOverviewDTO, TCaseIcd>, ObservableValue<TCaseIcd>>() {
                @Override
                public ObservableValue<TCaseIcd> call(TableColumn.CellDataFeatures<IcdOverviewDTO, TCaseIcd> param) {
                    TCaseIcd icd = pVersion.getCaseDetails() == null ? null : param.getValue().getIcdForVersion(pVersion.getCaseDetails().getId());
                    if (icd != null) {
                        return new SimpleObjectProperty<>(icd);
                    }
                    return null;
                }
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, TCaseIcd>, TableCell<IcdOverviewDTO, TCaseIcd>>() {
                @Override
                public TableCell<IcdOverviewDTO, TCaseIcd> call(TableColumn<IcdOverviewDTO, TCaseIcd> param) {
                    return new TableCell<IcdOverviewDTO, TCaseIcd>() {
                        @Override
                        protected void updateItem(TCaseIcd item, boolean empty) {
//                            long start = System.currentTimeMillis();
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            CheckBox chkGr = new CheckBox("");
                            chkGr.disableProperty().bind(armedProperty.not());
                            chkGr.setPadding(new Insets(0, -7, 0, 0));
                            chkGr.setSelected(item.getIcdIsToGroupFl());
                            chkGr.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    if (item.getIcdcIsHdxFl()) {
                                        chkGr.setSelected(true);
                                        return;
                                    }
                                    item.setIcdIsToGroupFl(chkGr.isSelected());
                                    storeOrUpdate(pVersion, item);
//                                    version.saveIcdEntity(item);
//                                    IcdTablePane.this.getRegionForVersion(version).refresh();
//                                    version.performGroup();
                                }
                            });
                            setAlignment(Pos.CENTER);
                            setStyle("-fx-padding: 0 0 0 0;");
//                            if ((isUsedForGrouping(item, version.getGroupingResult()))) {
//                                chkGr.getStyleClass().add("orange-check-box");
//                            }
                            //CPX-1494 RSH 19.03.2019 
                            if (isUsedForGrouping(item, pVersion.getGroupingResult())) {
                                chkGr.setTooltip(new Tooltip("Die Diagnose ist " + pVersion.getGroupingResult().getGrpresType() + " relevant!"));
                                highlight(chkGr, true);
                            } else {
                                chkGr.setTooltip(null);
                                highlight(chkGr, false);
                            }
                            setGraphic(chkGr);
//                            LOG.info("time to update use for group " + (System.currentTimeMillis()-start));
                        }

                    };
                }
            });
        }

        private boolean isUsedForGrouping(TCaseIcd pIcd, TGroupingResults pGroupingResult) {
            if (pGroupingResult == null) {
                return false;
            }
            for (TCaseIcdGrouped grIcd : pGroupingResult.getCaseIcdGroupeds()) {
                //prevent null pointer if somehow the icd reference of the grouped icd is null
                if (grIcd.getCaseIcd() == null) {
                    LOG.log(Level.WARNING, "TCaseIcdGrouped: {0} has no referenceIcd!", grIcd.getId());
                    continue;
                }
                if (grIcd.getCaseIcd().equals(pIcd)) {
                    if (grIcd.getIcdResU4gFl()) {
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

//secundary diagnosis
//not changable
    private class SecondaryDiagnosis extends TableColumn<IcdOverviewDTO, String> {

//        private final T version;
        public SecondaryDiagnosis(T pVersion) {
            super(Lang.getCaseResolveSecundaryICD());
//            version = pVersion;
            setMinWidth(40);
            setMaxWidth(40);
            setSortable(false);
            setReorderable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IcdOverviewDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<IcdOverviewDTO, String> param) {
                    TCaseIcd icd = pVersion.getCaseDetails() == null ? null : param.getValue().getIcdForVersion(pVersion.getCaseDetails().getId());
                    if (icd != null) {
                        String secDiag = computeSecDiagnosis(icd);
                        return new SimpleObjectProperty<>(secDiag);
                    }
                    return null;
                }
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, String>, TableCell<IcdOverviewDTO, String>>() {
                @Override
                public TableCell<IcdOverviewDTO, String> call(TableColumn<IcdOverviewDTO, String> param) {
                    return new TableCell<IcdOverviewDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//                            long start = System.currentTimeMillis();
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            if (item.isEmpty()) {
                                setGraphic(null);
                                return;
                            }
                            IcdOverviewDTO value = getTableView().getItems().get(getIndex());
                            TCaseIcd currIcd = value.getIcdForVersion(pVersion.getCaseDetails().getId());
                            if(currIcd == null || currIcd.getIcdcReftypeEn()== null){
                                return;
                            }
//                            Glyph refIcon = getRefIcon(currIcd.getIcdcReftypeEn());
//                            if(refIcon == null){
//                                return;
//                            }
                                setAlignment(Pos.CENTER);
//                            setStyle("-fx-padding: 0 0 0 0;");
                                setStyle("-fx-padding: 0 0 0 0;");
                                setStyle("-fx-label-padding: 0 0 0 8;");

//                            Label label = new Label("  ", refIcon);
                            Label label = new Label(getRefString(currIcd.getIcdcReftypeEn()));
                            label.setStyle("-fx-font-weight:bold;");
                            label.setAlignment(Pos.CENTER);
                            HBox wrapper = new HBox(label);
                            wrapper.setAlignment(Pos.CENTER_LEFT);
                            Label content = new Label(item);
                            content.setStyle("-fx-text-fill:black;");
                            setGraphic(ExtendedInfoHelper.addInfoPane(wrapper, content));                        }

                    };
                }
            });
            //remove padding, to avoid text overrun
            setStyle("-fx-padding: 0 0 0 0; -fx-label-padding: 0 0 0 8;");
        }

        private  Glyph  getRefIcon(IcdcRefTypeEn pIcdcRefTypeEn){
            switch (pIcdcRefTypeEn){
                case Kreuz:
                    return ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS_SQUARE_ALT);//ARROWS
                case Stern:
                    return ResourceLoader.getGlyph(FontAwesome.Glyph.ASTERISK);
                case Zusatz:
                    return ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION);
                case ZusatzZu:
                 return ResourceLoader.getGlyph(FontAwesome.Glyph.EYE);
            }
            return null;
        }
        private  String  getRefString(IcdcRefTypeEn pIcdcRefTypeEn){
            switch (pIcdcRefTypeEn){
                case Kreuz:
                    return "†";//ARROWS
                case Stern:
                    return "*";
                case Zusatz:
                    return "!";
                case ZusatzZu:
                 return "¹";
            }
            return null;
        }

        /**
         * Private
         */
        //set the secundary diagnosis in the ui after refIcd Code
        private String computeSecDiagnosis(TCaseIcd index) {
           
            // secondary diagnosis knows its main diagnosis, and its main diagnosis does not have the reference to its secondary
            if(index.getIcdcReftypeEn() != null && index.getIcdcReftypeEn().equals(IcdcRefTypeEn.Zusatz)){
                return  index.getRefIcd()== null?"":index.getRefIcd().getIcdcCode();
            }
            //go through Array of referenced Diagnosis and adds identifier
            String secDiag = "";
            if((index.getRefIcds() == null || index.getRefIcds().isEmpty() )&& index.getRefIcd() != null){
                return getOneSecIcd(index.getRefIcd());
            }
            for (TCaseIcd refIcd : index.getRefIcds()) {
                secDiag = secDiag.concat(getOneSecIcd(refIcd));
            }
            return secDiag;
        }
        
        private String getOneSecIcd(TCaseIcd refIcd){
             String secDiag = "";
            if (refIcd.getIcdcReftypeEn() != null) {
               switch (refIcd.getIcdcReftypeEn()) {
                   case Kreuz:
                       secDiag = secDiag.concat(refIcd.getIcdcCode() + "+ ");
                       break;
                   case Stern:
                       secDiag = secDiag.concat(refIcd.getIcdcCode() + "* ");
                       break;
                   case Zusatz:
                       secDiag = secDiag.concat(refIcd.getIcdcCode() + "! ");
                       break;
                   case ZusatzZu:
                       secDiag = secDiag.concat(refIcd.getIcdcCode());
                       break;

               }
           }
           return secDiag;
        }

    }
    
    

    //get the ccl value, ccl value is grouper result and stored in TCaseIcdGrouped
    //needs to find where icd in TCaseIcdGrouped is equal current icd
    private class CclColumn extends TableColumn<IcdOverviewDTO, Number> {

//        private final T version;
        public CclColumn(T pVersion) {
            super(Lang.getCaseResolveCCL());
//            version = pVersion;
            setResizable(false);
            setReorderable(false);
            setMinWidth(35.0);
            setMaxWidth(35.0);
            setSortable(false);
//            sortTypeProperty().addListener(new SortColumnListener(this));
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IcdOverviewDTO, Number>, ObservableValue<Number>>() {
                @Override
                public ObservableValue<Number> call(TableColumn.CellDataFeatures<IcdOverviewDTO, Number> param) {
                    TCaseIcd value = pVersion.getCaseDetails() == null ? null : param.getValue().getIcdForVersion(pVersion.getCaseDetails().getId());
                    LOG.log(Level.FINER, "CCL: versionId= {0}, param ={1}, value={2}", 
                            new Object[]{pVersion.getCaseDetails().getId(), param.getValue().getIcdCode(), value == null?"null":value.getIcdcCode()});
                    return new SimpleIntegerProperty(getCclFromGroupingResult(value, pVersion.getGroupingResult()));
                }
            });
            setCellFactory(new Callback<TableColumn<IcdOverviewDTO, Number>, TableCell<IcdOverviewDTO, Number>>() {
                @Override
                public TableCell<IcdOverviewDTO, Number> call(TableColumn<IcdOverviewDTO, Number> param) {
                    return new TableCell<IcdOverviewDTO, Number>() {
                        @Override
                        protected void updateItem(Number item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setText("");
                                return;
                            }
                            setText(item.intValue() != -1 ? String.valueOf(item.intValue()) : "");
                        }
                    };
                }
            });
        }

        private Integer getCclFromGroupingResult(TCaseIcd pIcd, TGroupingResults pGroupingResult) {
            if (pGroupingResult == null) {
                return -1;
            }
//            if(Hibernate.isInitialized(pIcd.getCaseIcdGroupeds())){
//                if(!pIcd.getCaseIcdGroupeds().isEmpty()){
//                    return String.valueOf(pIcd.getCaseIcdGroupeds().iterator().next().getIcdResCcl());
//                }
//            }
            for (TCaseIcdGrouped grIcd : pGroupingResult.getCaseIcdGroupeds()) {
                if (grIcd.getCaseIcd() == null) {
                    LOG.log(Level.WARNING, "TCaseIcdGrouped: {0} has no referenceIcd!", grIcd.getId());
                    continue;
                }
                //Awi-version equals for case merging
                if (grIcd.getCaseIcd().versionEquals(pIcd)) {
                    return grIcd.getIcdResCcl();
                }
            }
            return -1;
        }

    }

    /**
     * table view for version content of the icds
     */
    protected class IcdVersionTableView extends AsyncTableView<IcdOverviewDTO> {

        private final BooleanProperty armedProperty = new SimpleBooleanProperty(true);
        private RedirectScrollHandler DEFAULT_SCROLL_HANDLER = new RedirectScrollHandler();
        private ChangeListener<Skin<?>> SKIN_LISTENER = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                for (Node n : lookupAll(".scroll-bar")) {
                    if (n instanceof ScrollBar) {
                        ScrollBar bar = (ScrollBar) n;
                        if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                            bar.setDisable(true);
                            ScrollBar scrollBar = IcdTablePane.this.getVScrollBar();
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
        private ChangeListener<Number> SCROLL_BAR_LISTENER = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                IcdVersionTableView.this.setVPos(newValue.doubleValue());
            }
        };
        private ChangeListener<Integer> SELECTED_ROW_LISTENER = new ChangeListener<>() {
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
        private ChangeListener<Number> SELECTED_INDEX_LISTENER = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                IcdTablePane.this.setSelectedRow(newValue.intValue());
            }
        };
        private ChangeListener<Skin<?>> REORDER_LISTENER = new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> obs, Skin<?> oldSkin, Skin<?> newSkin) {
                    ((AsyncTableViewSkin) newSkin).setReordering(false);
                }
            };
        @SuppressWarnings("unchecked")
        public IcdVersionTableView(T pVersion) {
//            super(false);
            super();
            //deactivate reodering 
            skinProperty().addListener(REORDER_LISTENER);
            setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);
            getStyleClass().add("stay-selected-table-view");
            //setUpcolumns
            SecondaryDiagnosis secCol = new SecondaryDiagnosis(pVersion);
            SimulatedGroupResult simulResultCol = new SimulatedGroupResult(pVersion);
            MainDiagnosisColumn mdCol = new MainDiagnosisColumn(pVersion);
            mdCol.getArmedProperty().bind(armedProperty);
            LocalizationColumn locCol = new LocalizationColumn(pVersion);
            locCol.getArmedProperty().bind(armedProperty);
            CclColumn cclCol = new CclColumn(pVersion);
            UseGrouperColumn grCol = new UseGrouperColumn(pVersion);
            grCol.getArmedProperty().bind(armedProperty);

            //workaroung for bug in automatic computing of column sizes 
            //by http://bekwam.blogspot.de/2016/02/getting-around-javafx-tableview.html 
            //Awi:20170330
//            secCol.prefWidthProperty().bind(
//                    widthProperty()
//                    .subtract(mdCol.widthProperty())
//                    .subtract(locCol.widthProperty())
//                    .subtract(cclCol.widthProperty())
//                    .subtract(grCol.widthProperty())
//                    // a border stroke
//                    .subtract(2)
//            );
            secCol.setResizable(false);
            simulResultCol.setResizable(false);
            getColumns().addAll(grCol, mdCol, locCol, cclCol, simulResultCol, secCol);
            setRowContextMenu(createContextMenu(this));
            if (pVersion.isCanceled()) {
                armedProperty.setValue(false);
            }
            //kis version
            if (pVersion.getContent() != null && !pVersion.isEditable()) {
                armedProperty.setValue(false);
//                getColumns().remove(grCol);
//                secCol.prefWidthProperty().bind(
//                        widthProperty()
//                                .subtract(mdCol.widthProperty())
//                                .subtract(locCol.widthProperty())
//                                .subtract(cclCol.widthProperty())
//                                .subtract(grCol.widthProperty())
//                                .subtract(simulResultCol.widthProperty())
//                                .subtract(2)
//                );
            } else {
//                secCol.prefWidthProperty().bind(
//                        widthProperty()
//                                .subtract(mdCol.widthProperty())
//                                .subtract(locCol.widthProperty())
//                                .subtract(cclCol.widthProperty())
//                                .subtract(grCol.widthProperty())
//                                .subtract(simulResultCol.widthProperty())
//                                .subtract(2)
//                );
            }
            getSelectionModel().selectedIndexProperty().addListener(SELECTED_INDEX_LISTENER);
            IcdTablePane.this.getSelectedRowProperty().addListener(SELECTED_ROW_LISTENER);
            //bind scrollbars to move according to scrollbar parent
            //hide unneeded scrollbars
            getStyleClass().add("remove-all-scroll-bars");
            getVScrollBar().valueProperty().addListener(SCROLL_BAR_LISTENER);
            skinProperty().addListener(SKIN_LISTENER);
            addEventFilter(ScrollEvent.ANY, DEFAULT_SCROLL_HANDLER);
            fullWidth.set(secCol.getWidth()
                    + mdCol.getWidth() 
                    + locCol.getWidth()
                    + cclCol.getWidth()
                    + grCol.getWidth()
                    + simulResultCol.getWidth()
//                    + 2
            );
        }

        @Override
        public Future<List<IcdOverviewDTO>> getFuture() {
            return new AsyncResult<>(IcdTablePane.this.getItems());
        }

        @Override
        public void afterTask(Worker.State pState) {
            super.afterTask(pState);
            setVPos(getVScrollBar().getValue());
        }

        @Override
        public void dispose() {
            super.dispose(); //To change body of generated methods, choose Tools | Templates.
            removeEventFilter(ScrollEvent.ANY, DEFAULT_SCROLL_HANDLER);
            skinProperty().removeListener(SKIN_LISTENER);
            skinProperty().removeListener(REORDER_LISTENER);
            getVScrollBar().valueProperty().removeListener(SCROLL_BAR_LISTENER);
            getSelectionModel().selectedIndexProperty().removeListener(SELECTED_INDEX_LISTENER);
            IcdTablePane.this.getSelectedRowProperty().removeListener(SELECTED_ROW_LISTENER);
            for(TableColumn<IcdOverviewDTO, ?> col : getColumns()){
                if(col.prefWidthProperty().isBound()){
                    col.prefWidthProperty().unbind();
                }
            }
            DEFAULT_SCROLL_HANDLER = null;
            SELECTED_INDEX_LISTENER = null;
            SELECTED_ROW_LISTENER = null;
            SKIN_LISTENER = null;
            SCROLL_BAR_LISTENER = null;
            REORDER_LISTENER = null;
        }

    }

    private Collection<TCaseIcd> getCandidateList(T pVersion, ObservableList<IcdOverviewDTO> items) {
        List<TCaseIcd> icds = new ArrayList<>();
        if (pVersion == null) {
            return icds;
        }
        for (IcdOverviewDTO dto : items) {
            TCaseIcd icd = dto.getIcdForVersion(String.valueOf(pVersion.getCaseDetails().getId()));
            if (icd != null) {
                icds.add(icd);
            }
        }
        return icds;
    }
}

/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.table_master_detail;

import de.lb.cpx.client.app.menu.fx.filter.tableview.FilterTableView;
import de.lb.cpx.client.app.util.cpx_handler.CpxHandleManager;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.filter_manager.FilterManager;
import de.lb.cpx.client.core.model.fx.masterdetail.TableViewMasterDetailPane;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.dto.RuleListItemDTO;
import de.lb.cpx.shared.dto.SearchItemDTO;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.lang.Lang;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * FXML Controller class Basic class to handle ui updates on fxml level -like
 * adding filter items in ui -drawing columns -open basic dialogs
 *
 * @param <T> object type of content in filtered list
 *
 * TODO: Write FilterTableView
 * @author wilde
 */
public abstract class FilterListFXMLController<T extends SearchItemDTO> extends Controller<FilterListScene<T>> {

    private static final Logger LOG = Logger.getLogger(FilterListFXMLController.class.getName());

    @FXML
    private HBox hBoxMenu;
    @FXML
    private AnchorPane apContent;
    @FXML
    private Label lblFilter;
    @FXML
    private Label lblFilterRows;
    @FXML
    private Label lblTitle;
    @FXML
    private ComboBox<SearchListTypeEn> cbListType;

    private TableViewMasterDetailPane<T> mdPane;

    private IntegerProperty numberOfFilteredIdsProperty;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //create and add md Pane in View
        mdPane = new TableViewMasterDetailPane<>();
//        mdPane.setShowDetail(Session.instance().isShowFilterListDetailsOverview()); // cpx-1587 (to enable/disable details Anchorpane in the process & case lists)
        AnchorPane.setTopAnchor(mdPane, 0.0);
        AnchorPane.setRightAnchor(mdPane, 0.0);
        AnchorPane.setBottomAnchor(mdPane, 0.0);
        AnchorPane.setLeftAnchor(mdPane, 0.0);
        apContent.getChildren().add(mdPane);
    }

    private final ChangeListener<T> detailListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            long start = System.currentTimeMillis();
//                if (newValue instanceof SearchListDTO) {
//                    return;
//                }
            //CPX-2065
            mdPane.setShowDetail(Session.instance().isShowFilterListDetailsOverview()); // cpx-1587 (to enable/disable details Anchorpane in the process & case lists)
            // cpx-1587
            if (!mdPane.isShowDetail()) {
                return;
            }
            Parent detailNode = getDetailContent(newValue);
            getScene().setDetailNode(detailNode);
            //20181101-AWI:
            //gets focus after detail node is added 
            //enables scrolling by arrow buttons for tableviews
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
            getTableView().requestFocus();
//                }
//            });
            LOG.log(Level.FINER, "change detail in {0} ms", (System.currentTimeMillis() - start));
        }
    };

    private void setTableView(AsyncTableView<T> pTableView) {
        mdPane.setTableView(pTableView);
        getScene().selectedItemProperty().unbind();
        mdPane.getSelectedItemProperty().removeListener(detailListener);
        mdPane.getSelectedItemProperty().addListener(detailListener);
        getScene().selectedItemProperty().bind(mdPane.getSelectedItemProperty());
        mdPane.getTableView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        pTableView.reload();
    }
//    protected TableView getTableView(){
//        return mdPane.getTableView();
//    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    public void reload() {
        super.reload(); //To change body of generated methods, choose Tools | Templates.
        ((AsyncTableView) mdPane.getTableView()).reload();
    }

    @Override
    public boolean close() {
        boolean close = super.close(); //To change body of generated methods, choose Tools | Templates.
        //try to save updated values
        updateFilter();
        return close;
    }

    private void updateFilter() {
        ((FilterTableView) getTableView()).updateColumnSort(getScene().getFilterManager().getSearchList());
//        CpxClientConfig.instance().saveSearchList(getScene().getFilterManager().getSearchList(), false, false);
    }

//    /**
//     * transforms search result object to list of item
//     *
//     * @param pResult searchresult object, return value from server
//     * @return list of items from search result
//     */
//    protected List<T> transfromResult(SearchResult<T> pResult) {
//        if (pResult != null) {
//            return pResult.resultList;
//        }
//        return null;
//    }
    /**
     * @param pNode add node to menu
     * @return if adding was successful
     */
    protected boolean addNodeToMenu(Node pNode) {
        return hBoxMenu.getChildren().add(pNode);
    }

    @Override
    public void afterInitialisingScene() {
        lblTitle.textProperty().bind(getScene().getSceneTitleProperty());
        lblFilter.textProperty().bind(getScene().filterTextProperty());
        lblFilterRows.textProperty().bind(getScene().filterTextRowsProperty());

        getScene().detailNodeProperty().addListener(new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                mdPane.setDetail(newValue);
            }
        });
        setTableView(updateTableView());
        getScene().filterManagerProperty().addListener(new ChangeListener<FilterManager>() {
            @Override
            public void changed(ObservableValue<? extends FilterManager> observable, FilterManager oldValue, FilterManager newValue) {
                setTableView(updateTableView());
            }
        });
        //update detail node
//        mdPane.getSelectedItemProperty().addListener(new ChangeListener<T>() {
//            @Override
//            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
////                if (newValue instanceof SearchListDTO) {
////                    return;
////                }
//                getScene().setDetailNode(getDetailContent(newValue));
//            }
//        });
//        getScene().selectedItemProperty().bind(mdPane.getSelectedItemProperty());
        mdPane.getTableView().getSelectionModel().selectFirst();

        if (mdPane.getTableView() instanceof AsyncTableView) {
            ((AsyncTableView<T>) mdPane.getTableView()).onRowClickProperty().bind(getScene().rowClickHandlerProperty());
            mdPane.masterTableViewProperty().addListener(new ChangeListener<TableView<T>>() {
                @Override
                public void changed(ObservableValue<? extends TableView<T>> observable, TableView<T> oldValue, TableView<T> newValue) {
                    if (oldValue != null && oldValue instanceof AsyncTableView) {
                        ((AsyncTableView) oldValue).onRowClickProperty();
                        ((AsyncTableView) oldValue).dispose();
                    }
                    if (newValue != null) {
                        ((AsyncTableView<T>) mdPane.getTableView()).onRowClickProperty().unbind();
                        ((AsyncTableView<T>) mdPane.getTableView()).onRowClickProperty().bind(getScene().rowClickHandlerProperty());
                    }
                }
            });
        }
        getScene().getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                LOG.log(Level.INFO, "udpate properties key {0}", change.getKey());
                if (change.wasAdded()) {
                    if (FilterTableView.ADD_SEARCH_ITEM.equals(change.getKey())) {
                        if (change.getValueAdded() instanceof SearchListResult) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    LOG.log(Level.INFO, "add filter {0}", ((SearchListResult) change.getValueAdded()).getName());
                                    final SearchListResult searchListResult = (SearchListResult) change.getValueAdded();
                                    @SuppressWarnings("unchecked")
                                    final FilterTableView<T, ?> tableView = (FilterTableView<T, ?>) getTableView();
                                    tableView.clearSelection();
                                    tableView.addSearchList(searchListResult);
                                }
                            });
                            getScene().getProperties().remove(change.getKey());
                        }
                    }
                }
            }
        });
        initSupportedLists();

        mdPane.getTableView().setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                //System.out.println("onDragDetected");

                boolean isTableRow = false;
                if (event.getPickResult() == null) {
                    return;
                }
                if (event.getPickResult().getIntersectedNode() == null) {
                    return;
                }
                Parent parent = event.getPickResult().getIntersectedNode().getParent();
                while (true) {
                    if (parent == null) {
                        break;
                    }
                    if (parent instanceof TableRow) {
                        isTableRow = true;
                        break;
                    }
                    if (parent instanceof TableHeaderRow) {
                        break;
                    }
                    parent = parent.getParent();
                }
                if (!isTableRow) {
                    return;
                }
                /* allow any transfer mode */
                Dragboard db = mdPane.getTableView().startDragAndDrop(TransferMode.ANY);

                /* put a string on dragboard */
                //ClipboardContent content = new ClipboardContent();
                ClipboardContent filesToCopyClipboard = new ClipboardContent();
                //content.putString(mdPane.getTableView().toString());
                List<File> files = new ArrayList<>();
                List<String> html = new ArrayList<>();
                List<String> text = new ArrayList<>();
                List<String> urls = new ArrayList<>();
                //File f = new File(new URI(draggableObj.getFilePath()));
                //File f = new File("C:\\TEMP\\Fall-Liste.csv");
                for (SearchItemDTO dto : getSelectedItemsDistinct()) {
                    if (dto == null) {
                        continue;
                    }
                    final String key;
                    final String type;
                    if (dto instanceof WorkingListItemDTO) {
                        key = dto.getHospitalIdent() + "_" + dto.getCaseNumber();
                        type = "Fall";
                    } else if (dto instanceof RuleListItemDTO) {
                        key = dto.getHospitalIdent() + "_" + dto.getCaseNumber();
                        type = "Fall";
                    } else {
                        key = String.valueOf(((WorkflowListItemDTO) dto).getWorkflowNumber());
                        type = "Vorgang";
                    }
                    //Regex ensures that no illegal characters are used for file name
                    final String name = type + " " + (key.replaceAll("[\\\\/:*?\"<>|]", "_").trim());
                    final String url = "cpx:" + type.toLowerCase() + "_" + key + "@" + Session.instance().getCpxDatabase();
                    final int port = CpxHandleManager.instance().getPort();
                    File f = new File(System.getProperty("java.io.tmpdir") + "//" + name + ".url");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
                        writer.write("[InternetShortcut]\n");
                        writer.write("URL=" + url.replace("\"", "\\\""));
                        writer.write("@Port=" + port);
                    } catch (IOException ex) {
                        Logger.getLogger(FilterListFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    files.add(f);
                    final String link = "<a href=\"" + url + "\">" + name + "</a>";
                    html.add(link);
                    text.add(name);
                    urls.add(url);
                }
                filesToCopyClipboard.putFiles(files);
                filesToCopyClipboard.putHtml(String.join("<br>", html));
                filesToCopyClipboard.putString(String.join(", ", text));
                filesToCopyClipboard.putUrl(String.join("\n", urls));
                db.setContent(filesToCopyClipboard);

                event.consume();
            }
        });

        mdPane.getTableView().setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent me) {
                me.consume();
            }
        });

//        mdPane.getTableView().setOnDragDone(new EventHandler<DragEvent>() {
//
//            public void handle(DragEvent event) {
//                /* the drag-and-drop gesture ended */
//                System.out.println("onDragDone");
//                /* if the data was successfully moved, clear it */
//                if (event.getTransferMode() == TransferMode.MOVE) {
//                    mdPane.getTableView().setText("");
//                }
//
//                event.consume();
//            }
//        });
    }

    public abstract String getItemsName();

    public abstract int getNumberOfAllIds();

    public abstract int getNumberOfAllCanceledIds();

//    public abstract FilterOption getFilterOptionCancel();
    public abstract ColumnOption getColumnOptionCancel();

    public Set<Long> getIds() {
        final Set<Long> loadedIds = new HashSet<>();
        for (T item : new ArrayList<>(getTableView().getItems())) {
            if (item.getId() != 0L) {
                loadedIds.add(item.getId());
            }
        }
        return loadedIds;
    }

    public int getNumberOfLoadedIds() {
        return getIds().size();
    }

    public int getNumberOfLoadedRows() {
        return getTableView().getItems().size();
    }

    public final IntegerProperty numberOfFilteredIdsProperty() {
        if (numberOfFilteredIdsProperty == null) {
            numberOfFilteredIdsProperty = new SimpleIntegerProperty();
        }
        return numberOfFilteredIdsProperty;
    }

    public int getNumberOfFilteredIds() {
        return numberOfFilteredIdsProperty().get();
    }

    public void setNumberOfFilteredIds(final int pNumberOfAllRows) {
        numberOfFilteredIdsProperty().set(pNumberOfAllRows);
    }

    /*
    * update tableview info with current items and max count
     */
    public void updateTvInfos() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final long startTime = System.currentTimeMillis();
                final String itemsName = getItemsName();
                //final int numberOfLoadedIds = getNumberOfLoadedIds();
                final ColumnOption cancelColumn = getColumnOptionCancel();
                int allIdsTmp = getNumberOfAllIds();
                if (cancelColumn != null && cancelColumn.isShouldShow()) {
                    //if canceled items are visible then also consider them in the number of total items
                    allIdsTmp += getNumberOfAllCanceledIds();
                }
                final int numberOfAllIds = getNumberOfFilteredIds() > allIdsTmp ? getNumberOfFilteredIds() : allIdsTmp;
                //final int numberOfLoadedRows = getNumberOfLoadedRows();
                final int numberOfFilteredIds = getNumberOfFilteredIds();
                LOG.log(Level.FINEST, "update table view infos in {0} ms", System.currentTimeMillis() - startTime);

                //FilterListFXMLController.this.getScene().setFilterText("gefiltert: " + pCurrentItems + "/" + pCaseCount);
                //FilterListFXMLController.this.getScene().setFilterText("Zeilen: " + pCurrentItems + ", " + itemsName + ": " + numberOfLoadedIds + "/" + numberOfAllIds);
                //FilterListFXMLController.this.getScene().setFilterText(numberOfLoadedIds + " von " + numberOfAllIds + " " + itemsName);
                //FilterListFXMLController.this.getScene().setFilterTextRows(numberOfLoadedRows + " von " + numberOfAllRows + " Zeilen");
                final double percentFound = numberOfAllIds == 0 ? 0D : (numberOfFilteredIds * 100D) / numberOfAllIds;
                final double percentMissing = numberOfAllIds == 0 ? 0D : (100D - percentFound);
                final int diff = numberOfAllIds - numberOfFilteredIds;

                FilterListFXMLController.this.getScene().setFilterText(Lang.toDecimal(percentFound, 2) + " %" + (percentMissing > 0.0D ? " (-" + Lang.toDecimal(percentMissing, 2) + " %)" : ""));
                FilterListFXMLController.this.getScene().setFilterTextRows(Lang.toDecimal(numberOfFilteredIds) + " " + itemsName + (diff > 0 ? " (-" + Lang.toDecimal(diff) + " " + itemsName + ")" : ""));
            }
        });
    }

    /**
     * @return update tableview instane shown in masterpane of the masterDetail
     */
    public abstract AsyncTableView<T> updateTableView();

    /**
     * @param pItem selected list item
     * @return detail node to show in detail part of master detail
     */
    public abstract Parent getDetailContent(T pItem);

    //init supported list by listtype
    //init selection combobox
    //hides or disables box based on values fetched from supported lists of the scene
    private void initSupportedLists() {
        //TODO: Sort list by translation? 
        cbListType.setItems(FXCollections.observableArrayList(getScene().getSupportedLists()));
        //hides or disables box based on value
        if (cbListType.getItems().isEmpty() || cbListType.getItems().size() == 1) {
            cbListType.setVisible(false);
            return;
        }
        if (cbListType.getItems().size() > 1) {
            cbListType.setDisable(false);
        }
        //select actual list
        cbListType.getSelectionModel().select(getScene().getFilterManager().getListType());
        //handle view update if selection changes
        cbListType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SearchListTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends SearchListTypeEn> observable, SearchListTypeEn oldValue, SearchListTypeEn newValue) {
                if (newValue == null) {
                    cbListType.getSelectionModel().select(oldValue);
                    return;
                }
                updateFilter();
                //((FilterTableView) mdPane.getTableView()).saveCurrentFilter();
                getScene().updateFilterManager(newValue);
            }
        });
        cbListType.setCellFactory(new Callback<ListView<SearchListTypeEn>, ListCell<SearchListTypeEn>>() {
            @Override
            public ListCell<SearchListTypeEn> call(ListView<SearchListTypeEn> param) {
                return new ListTypeCell();
            }
        });
        cbListType.setButtonCell(new ListTypeCell());
    }

    /**
     * update filter in manager and trigger reload of tableview TODO: check if
     * ok .. reloading is async setup after this is discouraged?
     *
     * @param pView tableview to reload
     * @param pFilter filter to update manager with
     * @param pAutoReload auto reload/update tableview
     * @return if filter setting was successful
     */
    public boolean updateFilterInManager(FilterTableView<T, ?> pView, SearchListResult pFilter, boolean pAutoReload) {
        //store old values
        updateFilter();
        getScene().getFilterManager().setUpFilterForSearchList(pFilter);
        if (pAutoReload) {
            pView.reload();
        }
        return true;
    }

    /**
     * @return get sorted stream of columns (currently by sortNumber)
     */
    public Stream<ColumnOption> getSortedColumnOptions() {
        return getScene().getFilterManager().getSortedByNumber();
    }

    public List<FilterOption> getFilterOptions(String columnName) {
        return getScene().getFilterManager().getFilterOptions(getColumnOption(columnName));
    }

    public List<FilterOption> getFilterOptions(ColumnOption opt) {
        return getScene().getFilterManager().getFilterOptions(opt);
    }

    public ColumnOption getColumnOption(String name) {
        return getScene().getFilterManager().getColumnOption(name);
    }

    /**
     * @return current set searchlist in filter manager
     */
    public SearchListResult getSearchList() {
        return getScene().getFilterManager().getSearchList();
    }

    private String getListTypeTranslation(SearchListTypeEn pList) {
        switch (pList) {
            case RULE:
                return "Regelliste";
            case QUOTA:
                return "Prüfquotenliste";
            case WORKING:
                return "Fallliste";
            case WORKFLOW:
                return "Vorgangsliste";
            default:
                return pList.toString();
        }
    }

    private class ListTypeCell extends ListCell<SearchListTypeEn> {

        @Override
        protected void updateItem(SearchListTypeEn item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText("");
                return;
            }
            setText(getListTypeTranslation(item));
        }

    }

    public TableView<? extends T> getTableView() {
        return mdPane.getTableView();
    }

    public final Long getSelectedId() {
        T item = getSelectedItem();
        if (item == null) {
            return null;
        }
        return item.id;
    }

    public final T getSelectedItem() {
        return getTableView().getSelectionModel().getSelectedItem();
    }

    /**
     * Can contain duplicates (e.g. multiple processes because of several
     * reminders)
     *
     * @return selected items
     */
    public final List<T> getSelectedItems() {
        return getSelectedItemsAll();
    }

    /**
     * Can contain duplicates (e.g. multiple processes because of several
     * reminders)
     *
     * @return selected items
     */
    public final List<T> getSelectedItemsAll() {
        return new ArrayList<>(getTableView().getSelectionModel().getSelectedItems());
    }

    /**
     * Does not contain duplicates (ids are unique)
     *
     * @return selected items
     */
    public final List<T> getSelectedItemsDistinct() {
        List<T> list = getSelectedItemsAll();
        Set<Long> ids = new HashSet<>();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T obj = it.next();
            if (ids.contains(obj.getId())) {
                it.remove();
            } else {
                ids.add(obj.getId());
            }
        }
        return list;
    }

    /**
     * Can contain duplicates (e.g. multiple processes because of several
     * reminders)
     *
     * @return selected item ids
     */
    public final long[] getSelectedIds() {
        return getSelectedIdsAll();
    }

    /**
     * Can contain duplicates (e.g. multiple processes because of several
     * reminders)
     *
     * @return selected item ids
     */
    public final long[] getSelectedIdsAll() {
        List<T> selectedItems = getSelectedItemsAll();
        final long[] tmp = new long[selectedItems.size()];
        int i = 0;
        for (T item : selectedItems) {
            tmp[i++] = item.getId();
        }
        return tmp;
    }

    /**
     * Does not contain duplicates (ids are unique)
     *
     * @return selected item ids
     */
    public final long[] getSelectedIdsDistinct() {
        List<T> selectedItems = getSelectedItemsDistinct();
        final long[] tmp = new long[selectedItems.size()];
        int i = 0;
        for (T item : selectedItems) {
            tmp[i++] = item.getId();
        }
        return tmp;
    }

    /**
     * Does not contain duplicates (ids are unique)
     *
     * @param pNumberOfMinimumSelectedIds returns empty array if less than this
     * value
     * @return selected item ids
     */
    public final long[] getSelectedIdsDistinct(final int pNumberOfMinimumSelectedIds) {
        final long[] selectedIdsTmp = getSelectedIdsDistinct();
        final long[] selectedIds = selectedIdsTmp.length < pNumberOfMinimumSelectedIds ? new long[0] : selectedIdsTmp;
        return selectedIds;
    }

}

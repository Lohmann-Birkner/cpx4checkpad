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
package de.lb.cpx.client.core.model.fx.comparablepane;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableViewSkin;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javax.ejb.AsyncResult;

/**
 * Base-Class for the TableCompPane, manage Tableviews to show version based
 * information on different TableViews used in the case management
 *
 * @author wilde
 * @param <T> Object type displayed in the tableviews
 * @param <E> enclosing object type that is used to order it in the x-axis
 */
public abstract class TableCompPane<T, E extends ComparableContent<? extends AbstractEntity>> extends ComparablePane<AsyncTableView<T>, E> {

    private static final Logger LOG = Logger.getLogger(TableCompPane.class.getName());
    public static final String REFRESH_ITEMS = "refresh_items";
    //private static final String HIGHLIGHTING = "orange-check-box";
    //private static final String NON_HIGHLIGHTING = "green-check-box";

    private ObservableList<T> items = FXCollections.observableArrayList();
    private final ObjectProperty<DeleteStrategy> deleteStrategyProperty = new SimpleObjectProperty<>(DeleteStrategy.DEPENTING_ON_BASE);
    private Callback<List<Long>, List<T>> reloadCallback;
    //basis row factory
//    private final Callback<TableView<T>, TableRow<T>> rowFactory = new Callback<TableView<T>, TableRow<T>>() {
//            @Override
//            public TableRow<T> call(TableView<T> param) {
//                
//                final TableRow<T> row = new TableRow<>();  
//                final ContextMenu contextMenu = new CtrlContextMenu();  
//                final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());  
//                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
//                    @Override  
//                    public void handle(ActionEvent event) {  
//                        if(!delete(row.getItem())){
//                            BasicMainApp.showErrorMessageDialog(Lang.getErrorCouldNotDelete());
//                        }
//                    }  
//                });  
//                contextMenu.getItems().addAll(removeMenuItem);  
//               // Set context menu on row, but use a binding to make it only show for non-empty rows:  
//                row.contextMenuProperty().bind(  
//                        Bindings.when(row.emptyProperty())  
//                        .then((ContextMenu)null)  
//                        .otherwise(contextMenu)  
//                );  
//                
//                return row;
//            }
//        };
    private Callback<List<Long>, List<T>> refreshCallback;

//    private final ObjectProperty<Callback<TableView<T>, TableRow<T>>>rowFactoryProperty = new SimpleObjectProperty<>(rowFactory);
    protected final ChangeListener<Skin<?>> skinListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
            ((AsyncTableViewSkin) newValue).setReordering(false);
            for (Node n : getInfo().lookupAll(".scroll-bar")) {
                if (n instanceof ScrollBar) {
                    ScrollBar bar = (ScrollBar) n;
                    if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                        bar.setDisable(true);
                        getVScrollBar().visibleProperty().bindBidirectional(bar.visibleProperty());
                        getVScrollBar().visibleAmountProperty().bindBidirectional(bar.visibleAmountProperty());
//                            getVScrollBar().valueProperty().bindBidirectional(bar.valueProperty());
                        getVScrollBar().minProperty().bindBidirectional(bar.minProperty());
                        getVScrollBar().maxProperty().bindBidirectional(bar.maxProperty());
                        TableHeaderRow tableHeaderRow = (TableHeaderRow) ((SkinBase) newValue).getSkinnable().lookup("TableHeaderRow");
                        tableHeaderRow.heightProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                getVScrollBar().setPadding(new Insets(newValue.doubleValue(), 0.0, 0.0, 0.0));
                            }
                        });
                    }
                }
            }
        }
    };
    protected final ChangeListener<Number> selectionListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            setSelectedRow(newValue.intValue());
        }
    };
    protected final ChangeListener<Integer> selectedRowListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
                    getInfo().getSelectionModel().select(newValue);
//                }
//            });
        }
    };
    private final MapChangeListener<Object, Object> PROPERTIES_LISTENER = new MapChangeListener<>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (REFRESH_ITEMS.equals(change.getKey())) {
                        items.setAll(getInfo().getItems());
                        getProperties().remove(REFRESH_ITEMS);
                    }
                }
            }
        };
    private final ChangeListener<Number> V_BAR_INFO_SCROLL_LISTENER = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                getInfo().setVPos(newValue.doubleValue());
            }
        };
    private final RedirectScrollHandler REDIRECT_HANDLER = new RedirectScrollHandler();
    private LoadingTask task;

    public TableCompPane() {
        super();
        getInfo().addEventFilter(ScrollEvent.ANY, REDIRECT_HANDLER);
//        getInfo().rowFactoryProperty().bind(rowFactoryProperty); 
        //bind scrollbar of the info tableview to the scrollbar in the layout
        //skin is only set after drawing so all needed fields are initialized
        getInfo().skinProperty().addListener(skinListener);
        //stay selected stuff to mark seletion in different tableviews
        getInfo().getSelectionModel().selectedIndexProperty().addListener(selectionListener);
//        getInfo().setItems(items);
        getSelectedRowProperty().addListener(selectedRowListener);
        getProperties().addListener(PROPERTIES_LISTENER);
        getVScrollBar().valueProperty().addListener(V_BAR_INFO_SCROLL_LISTENER);
//        getInfo().reload();
    }

    @Override
    public void dispose() {
        getInfo().skinProperty().removeListener(skinListener);
        getInfo().getSelectionModel().selectedIndexProperty().removeListener(selectionListener);
        getInfo().removeEventFilter(ScrollEvent.ANY, REDIRECT_HANDLER);
        getVScrollBar().valueProperty().removeListener(V_BAR_INFO_SCROLL_LISTENER);
        getSelectedRowProperty().removeListener(selectedRowListener);
        getProperties().removeListener(PROPERTIES_LISTENER);
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return selected Item by Row -null, if no items are currently stored
     * -default value is the first item, when nothing is selected
     *
     */
    public T getSelectedItem() {
        //CPX-1146
        //safty checks for selected item
        if (items.isEmpty()) {
            return null;
        }
        //if item is not found return default the first item

        //avoid NPE
        if (getSelectedRow() == null) {
            return items.get(0);
        }
        if (getSelectedRow() == -1) {
            return items.get(0);
        }
        return items.get(getSelectedRow());
    }

    @Override
    public AsyncTableView<T> createInfoPane() {
        AsyncTableView<T> info = new AsyncTableView<T>(false) {
            @Override
            public Future<List<T>> getFuture() {
                return new AsyncResult<>(items);
            }

            @Override
            public void afterTask(Worker.State pState) {
                if (pState.equals(Worker.State.SUCCEEDED)) {
                    setVPos(getVScrollBar().getValue());
                }
                super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
            }

        };

        HBox.setHgrow(info, Priority.ALWAYS);
        info.getStyleClass().add("remove-all-scroll-bars");
        info.getStyleClass().add("stay-selected-table-view");
        info.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return info;
    }

    public void setItems(List<T> pItems, boolean pBeforeReload) {
        if (pItems != null) {
            double scroll = getVScrollBar().getValue();
            items.setAll(pItems);
            if (pBeforeReload) {
                getInfo().setLoadingLayout();
            } else {
                getInfo().reload();
            }
            Iterator<E> it = getTableViewToVersion().keySet().iterator();
            while (it.hasNext()) {
                E next = it.next();
                if (pBeforeReload) {
                    getTableViewToVersion().get(next).getRegion().setLoadingLayout();
                } else {
                    getTableViewToVersion().get(next).getRegion().reload();
                }
            }
            getVScrollBar().setValue(scroll);
        }
    }

    /**
     * @return list of items
     */
    public ObservableList<T> getItems() {
        return items;
    }

    /**
     * @param pItem item to delete
     * @return if delete was successful
     */
    public abstract boolean delete(T pItem);

//    public abstract void applySort(TableColumn pColumn);
    public void applySort(TableColumn<T, ?> pColumn) {
        LOG.finer("apply sort for column " + pColumn.getText());
        //Apply sorting from one table view in the others
        //TODO:
        //sort on only one shared set of data for all tablesviews?
        //do update async, should remove flickering of selected highlighting 
        //java fx stuck itself in loop to select prev selcted item if runLater is not used
        if (getInfo() != pColumn.getTableView()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    removeSorting(getInfo());
                    getInfo().setItems(pColumn.getTableView().getItems());
                    //2019-02-14 AWi: remove refresh, to avoid items become empty - due to other fix by setting new List Object when task is finished
//                    refreshItems();
                }
            });
        }
        for (E version : getTableViewToVersion().keySet()) {
            AsyncTableView<T> tv = getRegionForVersion(version);
            if (tv != pColumn.getTableView()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        removeSorting(tv);
                        tv.setItems(pColumn.getTableView().getItems());
                    }
                });
            }
        }
    }

    protected void refreshItems() {
        getProperties().put(REFRESH_ITEMS, null);
    }

    protected void removeSorting(TableView<T> pView) {
        for (Object col : pView.getColumns()) {
            if (((TableColumn) col).getSortType() != null) {
                ((TableColumn) col).setSortType(null);
            }
        }
        pView.getSortOrder().clear();
    }

    @Override
    public void reload() {

        super.reload();
        LOG.log(Level.FINER, "start loading task");
        if (task != null) {
//            return;
        }

        beforeTask();
        task = new LoadingTask();
        task.start();
    }

    /**
     * refresh values after grouping
     */
    @Override
    public void refresh() {
        super.refresh();
        if (refreshCallback != null) {
            setItems(FXCollections.observableArrayList(refreshCallback.call(null)), false);
            return;
        }
        getInfo().refresh();
        Iterator<E> it = getTableViewToVersion().keySet().iterator();
        while (it.hasNext()) {
            E next = it.next();
            AsyncTableView<T> tv = getTableViewToVersion().get(next).getRegion();
            tv.refresh();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ItemContainer<AsyncTableView<T>, E> pane = getTableViewToVersion().get(next);
                    if(pane != null){
                        pane.setControl(getVersionCtrlFactory().call(next));
                    }
                }
            });
        }
    }

    public void deleteFromTableView(E next, T pItem) {
        ItemContainer<AsyncTableView<T>, E> container = getTableViewToVersion().get(next);
        TableView<?> tableView = container.getRegion();
        tableView.getItems().remove(pItem);
        tableView.refresh();
    }

    /**
     * @param pCtrl control to add highlight to
     */
    protected void addHighlighting(Control pCtrl) {
        highlight(pCtrl, true);
    }

    /**
     * @param pCtrl control to remove highlight from
     */
    protected void removeHighlighting(Control pCtrl) {
        highlight(pCtrl, false);
    }

    /**
     * highlight control with highlight effect default orange-check-box TODO:
     * make highlighting for a more generic way
     *
     * @param pControl control to highlight
     * @param highlight boolean if it should be highlighted
     */
    protected void highlight(Control pControl, boolean highlight) {
        pControl.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlight"), highlight);
    }

    /**
     * sets new menu control in itemContainer to reflect update Call
     * versionCtrlFactory
     */
    public void refreshMenu() {
        Iterator<E> it = getTableViewToVersion().keySet().iterator();
        while (it.hasNext()) {
            E next = it.next();
            getTableViewToVersion().get(next).setControl(getVersionCtrlFactory().call(next));
        }
    }

    public void setReloadCallback(Callback<List<Long>, List<T>> pCallback) {
        reloadCallback = pCallback;
    }

    public void setRefreshCallback(Callback<List<Long>, List<T>> pCallback) {
        refreshCallback = pCallback;
    }

    public DeleteStrategy getDeleteStrategy() {
        return deleteStrategyProperty.get();
    }

    public void setDeleteStrategy(DeleteStrategy pStategy) {
        deleteStrategyProperty.set(pStategy);
    }

    public ObjectProperty<DeleteStrategy> deleteStrategyProperty() {
        return deleteStrategyProperty;
    }

    private void beforeTask() {
        Iterator<E> it = getTableViewToVersion().keySet().iterator();
        while (it.hasNext()) {
            E next = it.next();
            ItemContainer<AsyncTableView<T>, E> versionIten = getTableViewToVersion().get(next);
            if (versionIten.getRegion() instanceof AsyncTableView) {
                versionIten.getRegion().beforeTask();
            }
        }
    }

    private void afterTask() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                task = null;
            }
        });
    }

    public enum DeleteStrategy {
        ATLEAST_ONE_EDITABLE, DEPENTING_ON_BASE
    }

    /*
     * PRIVATE CLASSES 
     */
    protected class UpdateTask extends LoadingTask {

        private final E version;
        private final AbstractEntity entity;

        public UpdateTask(E pVersion, AbstractEntity pEntity) {
            super();
            version = pVersion;
            entity = pEntity;
        }

        @Override
        public void start() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setItems(FXCollections.observableArrayList(), true);
                    UpdateTask.super.start();
                }
            });
        }

        @Override
        protected List<T> call() throws Exception {
            saveEntity(entity);
            version.performGroup();
            futureResult = new AsyncResult<>(reloadCallback.call(getVersionIds()));
            return futureResult.get();
        }

        private void saveEntity(AbstractEntity entity) {
            if (entity instanceof TCaseIcd) {
                version.saveIcdEntity((TCaseIcd) entity);
            }
            if (entity instanceof TCaseOps) {
                version.saveOpsEntity((TCaseOps) entity);
            }
        }
    }

    private class LoadingTask extends CpxTask<List<T>> {

        protected Future<List<T>> futureResult;

        /**
         * creates new instance
         */
        public LoadingTask() {
//            beforeTask();
            setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent t) {
                    onFailed();
                    dispose();
                }
            });
            setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent t) {
                    onCancel();
                    dispose();
                }
            });
            setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    onSucceed();
                    dispose();
                }
            });
        }

        @Override
        protected List<T> call() throws Exception {

            futureResult = new AsyncResult<>(reloadCallback.call(getVersionIds()));
            return futureResult.get();
        }

        @Override
        public void dispose() {
            afterTask();
            super.dispose();
        }

        //on failed show exception 
        private void onFailed() {
            Throwable ex = getException();
            LOG.log(Level.SEVERE, "Failed to load : " + ex.getMessage(), ex);
            BasicMainApp.showErrorMessageDialog(ex, "Error occured when executing loading");
        }

        //on cancel stop future execution
        private void onCancel() {
            if (futureResult != null && !futureResult.isCancelled()) {
                futureResult.cancel(true);
            }
        }

        //on success transform result if neccessary and set items in table 
        private void onSucceed() {
            List<T> result;
            try {
                result = get();
            } catch (ExecutionException ex) {
                LOG.log(Level.SEVERE, null, ex);
                return;
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
                return;
            }
            if (result != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setItems(FXCollections.observableArrayList(result), false);
                    }
                });
            }

        }
    }

    protected class SortColumnListener implements ChangeListener<SortType> {

        private final TableColumn<T, ?> column;

        public SortColumnListener(TableColumn<T, ?> pColumn) {
            column = pColumn;
            column.setSortType(null);
            column.setSortable(true);
        }

        @Override
        public void changed(ObservableValue<? extends SortType> observable, SortType oldValue, SortType newValue) {
            if (newValue == null) {
                return;
            }
            applySort(column);
        }

    }
//    protected class DateComperator{
//        new Comparator<TCaseOps>() {
//                @Override
//                public int compare(TCaseOps o1, TCaseOps o2) {
//                    if(o1 == null || o1.getOpscDatum() == null){
//                        return 1;
//                    }
//                    if(o2 == null || o2.getOpscDatum() == null){
//                        return 0;
//                    }
//                    return o1.getOpscDatum().compareTo(o2.getOpscDatum());
//                }
//            }
//    }

    protected class SortableTableColumn extends TableColumn<T, E> {

        public SortableTableColumn(String pText) {
            super(pText);
            setSortType(null);
            sortTypeProperty().addListener(new ChangeListener<SortType>() {
                @Override
                public void changed(ObservableValue<? extends SortType> observable, SortType oldValue, SortType newValue) {
//                    sortByDate(newValue);
                    applySort(SortableTableColumn.this);
                }
            });
        }
    }

    protected class RedirectScrollHandler implements EventHandler<ScrollEvent> {

        public RedirectScrollHandler() {
        }

        @Override
        public void handle(ScrollEvent event) {
            double deltaY = event.getDeltaY() * 6;// to make the scrolling a bit faster
            double width = getInfo().getBoundsInLocal().getWidth();//getBounds().getWidth();
            double vvalue = getVScrollBar().getValue();
            double val = vvalue + -deltaY / width;
            if (val > 1.0) {
                val = 1.0;
            }
            if (val < 0) {
                val = 0;
            }
            getVScrollBar().setValue(val);
            event.consume();
        }

    }
}

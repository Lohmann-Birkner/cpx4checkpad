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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tableview;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Skin;
import javafx.scene.control.TableView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Implementation of a Tableview, that can load its content async from main
 * thread
 *
 * @author wilde
 * @param <T> type of table content
 */
public abstract class AsyncTableView<T> extends TableView<T> {

    private static final Logger LOG = Logger.getLogger(AsyncTableView.class.getName());

    private final BooleanProperty showMenuProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty showCurrMaxMenuProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty abortableProperty = new SimpleBooleanProperty(false);
    private final ReadOnlyBooleanWrapper loadTaskRunningProperty = new ReadOnlyBooleanWrapper(false);

    private final ReadOnlyIntegerWrapper maxCountProperty = new ReadOnlyIntegerWrapper(-1);
    private final ReadOnlyIntegerWrapper currCountProperty = new ReadOnlyIntegerWrapper(-1);

    private final StringProperty titleProperty = new SimpleStringProperty("");

    private final ObservableList<Node> menuItems = FXCollections.observableArrayList();
    private final ObservableList<Node> filterItems = FXCollections.observableArrayList();

    private LoadingTask loadingTask;
    private Callback<Void, List<T>> onReload;

    private Node defaultPlaceholder;
    private Node loadingPlaceholder = new LoadingLayout();

    private final BooleanProperty showContextMenuProperty = new SimpleBooleanProperty(true);
    private DoubleProperty vPosProperty;
    private DoubleProperty hPosProperty;
    private ObjectProperty<ContextMenu> rowContextMenuProperty;
    private ObjectProperty<EventHandler<MouseEvent>> onRowClickProperty;
    private ObjectProperty<EventHandler<ContextMenuEvent>> onRowContextMenuRequestedProperty;
    private ReadOnlyBooleanWrapper isEmptyProperty;
    private EventHandler<ContextMenuEvent> CONTEXT_EVENT = new EventHandler<ContextMenuEvent>() {
        @Override
        public void handle(ContextMenuEvent event) {
            if (!isShowContextMenu()) {
                event.consume();
            }
        }
    };
    /**
     * creates new instance
     */
    public AsyncTableView() {
        super();
        defaultPlaceholder = getPlaceholder();
        setPlaceholder(loadingPlaceholder);
        addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, CONTEXT_EVENT);
    }

    /**
     * creates new instance
     *
     * @param pAbortable is task abrotable
     */
    public AsyncTableView(boolean pAbortable) {
        this();
        setAbortable(pAbortable);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AsyncTableViewSkin<>(this);
    }

    public void reload() {
        beforeTask();
        setLoadingLayout();
        loadingTask = new LoadingTask();
        loadingTask.start();
        LOG.finer("start load thread");
    }

    /*
     * OVERRIDE LIFECYCLE METHODES 
     */
    /**
     * Overrideable LifeCycle Methode,executed before reload task is executed
     */
    public void beforeTask() {
        //Cancel previous started search first
        if (loadingTask != null && loadingTask.isRunning()) {
            LOG.log(Level.INFO, "Will cancel previous started loading now...");
            loadingTask.cancel();
            while (loadingTask != null && !loadingTask.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
            loadingTask = null;
            LOG.log(Level.INFO, "Previous started loading was cancelled");
        }
        setLoadingLayout();
        //set hPos of the scrollbar to 0, somehow prevents scrollbar to show faulty ui after reload
        //added for appendable tableview
        setHPos(0);
    }

    public void setLoadingLayout() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setPlaceholder(loadingPlaceholder);
            }
        });
    }

    /**
     * Overrideable LifeCycle Methode,executed after reload task is executed
     *
     * @param pState worker state to handle result of the task, succcess, failed
     * etc
     */
    public void afterTask(State pState) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                setHPos(hPos);
//            }
//        });
        switch (pState) {
            case FAILED:
                setPlaceholder(defaultPlaceholder);
                break;
            case CANCELLED:
                setPlaceholder(defaultPlaceholder);
                break;
            case SUCCEEDED:
                setPlaceholder(defaultPlaceholder);
                break;
            default:
                LOG.log(Level.WARNING, "Unknown state: " + pState);
        }
        LOG.finer("after task! state " + pState.name());
        loadTaskRunningProperty.set(false);
        loadingTask = null;
        refresh();
    }

    /*
     * ABSTRACT PUBLIC METHODES 
     */
    /**
     * @return futre of the list(database call)
     */
    public abstract Future<List<T>> getFuture();

    /**
     * @param pCallback callback called when reload is executed
     */
    public void setOnReload(Callback<Void, List<T>> pCallback) {
        onReload = pCallback;
    }

    /**
     * @return callback set to happen if reload is executed
     */
    public Callback<Void, List<T>> getOnReload() {
        return onReload;
    }

    /*
    * GETTER/SETTER
     */
    /**
     * @return indicator if loading task is running
     */
    public boolean isLoadingTaskRunning() {
        return loadTaskRunningProperty.get();
    }

    /**
     * @return readonly property to check state of loading task running
     */
    public ReadOnlyBooleanProperty loadingTaskRunningProperty() {
        return loadTaskRunningProperty.getReadOnlyProperty();
    }

    /**
     * @return indicator if menu is shown/hidden
     */
    public boolean getShowMenu() {
        return showMenuProperty.get();
    }

    /**
     * @param pShowMenu indicator do show/hide menu
     */
    public void setShowMenu(boolean pShowMenu) {
        showMenuProperty.set(pShowMenu);
    }

    /**
     * @return property if menu is shown/hiden
     */
    public BooleanProperty showMenuProperty() {
        return showMenuProperty;
    }

    /**
     * @return property if menu is shown/hiden
     */
    public BooleanProperty showContextMenuProperty() {
        return showContextMenuProperty;
    }

    /**
     * @param pShowContextMenu indicator do show/hide context menu
     */
    public void setShowContextMenu(boolean pShowContextMenu) {
        showContextMenuProperty.set(pShowContextMenu);
    }

    /**
     * @return indicator if context menu is shown/hidden
     */
    public boolean isShowContextMenu() {
        return showContextMenuProperty.get();
    }

    /**
     * @return is task abrotable
     */
    public boolean isAbortable() {
        return abortableProperty.get();
    }

    /**
     * @param pAbortable set task is abortable
     */
    public final void setAbortable(boolean pAbortable) {
        abortableProperty.set(pAbortable);
    }

    /**
     * @return property if task is abortable
     */
    public BooleanProperty abortableProperty() {
        return abortableProperty;
    }

    /**
     * @return indicator if current/max menu is shown/hidden
     */
    public boolean getShowCurrentMaxMenu() {
        return showCurrMaxMenuProperty.get();
    }

    /**
     * @param pShowCurrentMaxMenu indicator do show/hide current/max menu only
     * affects ui if menu is shown
     */
    public void setShowCurrentMaxMenu(boolean pShowCurrentMaxMenu) {
        showCurrMaxMenuProperty.set(pShowCurrentMaxMenu);
    }

    /**
     * @return property if current/max menu is shown/hiden
     */
    public BooleanProperty showCurrentMaxMenuProperty() {
        return showCurrMaxMenuProperty;
    }

    /**
     * @return max count to show
     */
    public Integer getMaxCount() {
        return maxCountProperty.get();
    }

    /**
     * @param pMaxCount set new max count
     */
    public void setMaxCount(int pMaxCount) {
        maxCountProperty.set(pMaxCount);
    }

    /**
     * @return max count property
     */
    public ReadOnlyIntegerProperty maxCountProperty() {
        return maxCountProperty.getReadOnlyProperty();
    }

    /**
     * @return current count to show
     */
    public Integer getCurrentCount() {
        return currCountProperty.get();
    }

    /**
     * @return current count property
     */
    public ReadOnlyIntegerProperty currentCountProperty() {
        return currCountProperty.getReadOnlyProperty();
    }

    /**
     * TODO: bind value to list size so that it must not be set? maybe
     * troublesome due to appending list sizes, it needs count with bound value
     * count will be 0 if thread is running, and therefore append will not work
     *
     * @param pCurrentCount current list count to set
     */
    public void setCurrentCount(int pCurrentCount) {
        currCountProperty.set(pCurrentCount);
    }

    /**
     * @param pTitle set title in menu affects ui only if menu is shown
     */
    public void setTitle(String pTitle) {
        titleProperty.set(pTitle);
    }

    /**
     * @return title currently set only shown when menu is shown
     */
    public String getTitle() {
        return titleProperty.get();
    }

    /**
     * @return title property
     */
    public StringProperty titleProperty() {
        return titleProperty;
    }

    /**
     * @param pNodes nodes to add to menu
     */
    public void addMenuItem(Node... pNodes) {
        menuItems.addAll(pNodes);
    }

    /**
     * @param index index to add node
     * @param pNodes node to add to menu
     */
    public void addMenuItem(int index, Node pNodes) {
        menuItems.add(index, pNodes);
    }

    /**
     * @param pNode remove node from menu
     */
    public void removeMenuItem(Node pNode) {
        menuItems.remove(pNode);
    }

    /**
     * @return list of menu items
     */
    protected ObservableList<Node> getMenuItems() {
        return menuItems;
    }

    /**
     * @param pNodes nodes to add to filter
     */
    public void addFilterItem(Node... pNodes) {
        filterItems.addAll(pNodes);
    }

    /**
     * @param pNode node to remove
     */
    public void removeFilterItem(Node pNode) {
        filterItems.remove(pNode);
    }

    /**
     * @return list of filter items
     */
    protected ObservableList<Node> getFilterItems() {
        return filterItems;
    }

    /**
     * @return vertical PostionProperty of the vertical scrollbar
     */
    public DoubleProperty vPosProperty() {
        if (vPosProperty == null) {
            vPosProperty = new SimpleDoubleProperty(0.0d);
        }
        return vPosProperty;
    }

    /**
     * @return position of the vertical scrollbar
     */
    public Double getVPos() {
        return vPosProperty().get();
    }

    /**
     * @param pVPos set postion of the vertical scrollbar (values between 0 and
     * 1 are expected)
     */
    public void setVPos(double pVPos) {
        vPosProperty().set(pVPos);
    }

    /**
     * @return horizontal position Proeprty of the scrollbar
     */
    public DoubleProperty hPosProperty() {
        if (hPosProperty == null) {
            hPosProperty = new SimpleDoubleProperty(0.0d);
        }
        return hPosProperty;
    }

    /**
     * @return horizontal position of the scrollbar
     */
    public Double getHPos() {
        return hPosProperty().get();
    }

    /**
     * @param pHPos set position of the horizontal scrollbar
     */
    public void setHPos(double pHPos) {
        hPosProperty().set(pHPos);
    }

    public final ObjectProperty<ContextMenu> rowContextMenuProperty() {
        if (rowContextMenuProperty == null) {
            rowContextMenuProperty = new SimpleObjectProperty<>(new ContextMenu());
        }
        return rowContextMenuProperty;
    }

    public final ContextMenu getRowContextMenu() {
        return rowContextMenuProperty().get();
    }

    public final void setRowContextMenu(ContextMenu pMenu) {
        rowContextMenuProperty().set(pMenu);
    }

    public ObjectProperty<EventHandler<MouseEvent>> onRowClickProperty() {
        if (onRowClickProperty == null) {
            onRowClickProperty = new SimpleObjectProperty<>();
        }
        return onRowClickProperty;
    }

    public void setOnRowClick(EventHandler<MouseEvent> pEventHandler) {
        onRowClickProperty().set(pEventHandler);
    }

    public EventHandler<MouseEvent> getOnRowClicked() {
        return onRowClickProperty().get();
    }

    public ObjectProperty<EventHandler<ContextMenuEvent>> onRowContextMenuRequestedProperty() {
        if (onRowContextMenuRequestedProperty == null) {
            onRowContextMenuRequestedProperty = new SimpleObjectProperty<>();
        }
        return onRowContextMenuRequestedProperty;
    }

    public void setOnRowContextMenuRequested(EventHandler<ContextMenuEvent> pEventHandler) {
        onRowContextMenuRequestedProperty().set(pEventHandler);
    }

    public EventHandler<ContextMenuEvent> getOnRowContextMenuRequested() {
        return onRowContextMenuRequestedProperty().get();
    }
    private ObjectProperty<EventHandler<MouseEvent>> onRowDragDetectedProperty;

    public ObjectProperty<EventHandler<MouseEvent>> onRowDragDetectedProperty() {
        if (onRowDragDetectedProperty == null) {
            onRowDragDetectedProperty = new SimpleObjectProperty<>();
        }
        return onRowDragDetectedProperty;
    }

    public EventHandler<MouseEvent> getOnRowDragDetected() {
        return onRowDragDetectedProperty().get();
    }

    public void setOnRowDragDetected(EventHandler<MouseEvent> pHandler) {
        onRowDragDetectedProperty().set(pHandler);
    }

    public ReadOnlyBooleanProperty isEmptyProperty() {
        if (isEmptyProperty == null) {
            isEmptyProperty = new ReadOnlyBooleanWrapper(getItems().isEmpty());
            isEmptyProperty.bind(Bindings
                    .when(Bindings.size(getItems()).isEqualTo(0))
                    .then(true)
                    .otherwise(false));
        }
        return isEmptyProperty.getReadOnlyProperty();
    }

    public boolean isEmpty() {
        return isEmptyProperty().get();
    }

    public void dispose() {
        if(loadingTask != null){
            loadingTask.dispose();
        }
//        loadingPlaceholder = null;
//        defaultPlaceholder = null;
        getChildren().clear();
        if (isEmptyProperty != null) {
            isEmptyProperty.unbind();
        }
        removeEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, CONTEXT_EVENT);
    }

    /*
     * PRIVATE CLASSES 
     */
    private class LoadingTask extends CpxTask<List<T>> {

        private Future<List<T>> futureResult;

        /**
         * creates new instance
         */
        public LoadingTask() {
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
            futureResult = getFuture();
            List<T> res = futureResult.get();
            return res;
        }

        @Override
        public void dispose() {
            super.dispose();
            afterTask(getState());
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
                final long startTime = System.currentTimeMillis();
                setItems(FXCollections.observableList(result)); // DOES NOT WORK (addListener on item list does not fire anymore with this)
                LOG.log(Level.FINER, "setItems of AsynTableView with result size of {0} took {1} ms ", new Object[]{result.size(), System.currentTimeMillis() - startTime});
            }

        }
    }

    //refactor due to dublicate code (occures in AsyncTableView)
    private class LoadingLayout extends VBox {

        public LoadingLayout() {
            ProgressIndicator pi = new ProgressIndicator(-1);
            setAlignment(Pos.CENTER);
            pi.setMinHeight(40d);
            pi.getStyleClass().add("async-progress-indicator");
            Label status = new Label(Lang.getPleaseWait());
            getChildren().addAll(pi, status);
            setSpacing(10.0);
            if (isAbortable()) {
                Button button = new Button(Lang.getCancel());
                getChildren().add(button);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (loadingTask != null) {
                            loadingTask.stop();
                        } else {
                            afterTask(State.CANCELLED);
                        }
                    }
                });
            }
            parentProperty().addListener(new ChangeListener<Parent>() {
                @Override
                public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                    if (newValue != null && newValue instanceof Pane) {
                        pi.minWidthProperty().bind(((Region) newValue).widthProperty().divide(5));
                    }
                }
            });
        }

    }

}

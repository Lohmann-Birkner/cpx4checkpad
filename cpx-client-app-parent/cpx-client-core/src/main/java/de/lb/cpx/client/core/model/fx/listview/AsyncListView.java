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
package de.lb.cpx.client.core.model.fx.listview;

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
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * ListView which support asyc loading somewhat similar to AsyncTableView maybe
 * refactor to reduce double code?
 *
 * @author wilde
 * @param <T> content of the listview
 */
public abstract class AsyncListView<T> extends ListView<T> {

    private static final Logger LOG = Logger.getLogger(AsyncListView.class.getName());

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
    private long startTime;

    /**
     * no arg contructor creates new instance
     *
     * @param pTitle title
     */
    public AsyncListView(final String pTitle) {
        super();
        currCountProperty.bind(Bindings.size(getItems()));
        defaultPlaceholder = getPlaceholder();
        if (defaultPlaceholder == null) {
            defaultPlaceholder = new Label();
            ((Labeled) defaultPlaceholder).setText("Keine Einträge in der Liste!");
        }
        setPlaceholder(loadingPlaceholder);
        titleProperty.set(pTitle);
    }

    /**
     * no arg contructor creates new instance
     */
    public AsyncListView() {
        this(null);
    }

    /**
     * creates new instance
     *
     * @param pAbortable is task abrotable
     */
    public AsyncListView(boolean pAbortable) {
        this();
        setAbortable(pAbortable);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AsyncListViewSkin<>(this);
    }

    public final void reload() {
        beforeTask();
        loadingTask = new LoadingTask();
        loadingTask.start();
        
    }

    @Override
    public void refresh() {
        super.refresh();
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
            LOG.log(Level.INFO, "Previous started loading was cancelled");
        }
        startTime = System.currentTimeMillis();
        //Platform.runLater(() -> {
        if (getPlaceholder() == null || !getPlaceholder().equals(loadingPlaceholder)) {
            setPlaceholder(loadingPlaceholder);
        }
        if((!(getItems() instanceof FilteredList)) && (!(getItems() instanceof SortedList))){ //listtype in process history there is a filtered list stored -> does not support clear
            getItems().clear();
        }
    }

    /**
     * Overrideable LifeCycle Methode,executed after reload task is executed
     *
     * @param pState worker state to handle result of the task, succcess, failed
     * etc
     */
    public void afterTask(Worker.State pState) {
        //LOG.log(Level.INFO, "Loading of async list finished with status " + pState + " in " + (System.currentTimeMillis() - startTime) + " ms");
        Platform.runLater(() -> {
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
            LOG.log(Level.FINER, "Loading of async list finished with status " + pState + " in " + (System.currentTimeMillis() - startTime) + " ms");
            loadTaskRunningProperty.set(false);
        });
    }

    /*
     * ABSTRACT PUBLIC METHODES 
     */
    public abstract Future<List<T>> getFuture();

//    
//    public abstract List<T> transform(K pResult);
    public void setOnReload(Callback<Void, List<T>> pCallback) {
        onReload = pCallback;
    }

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
     * @return is task abrotable
     */
    public boolean isAbortable() {
        return abortableProperty.get();
    }

    /**
     * @param pAbortable set task is abortable
     */
    public void setAbortable(boolean pAbortable) {
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
     * @param pNode remove node from menu
     */
    public void removeMenuItem(Node pNode) {
        menuItems.remove(pNode);
    }

    /**
     * @return list of menu items
     */
    protected List<Node> getMenuItems() {
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
    protected List<Node> getFilterItems() {
        return filterItems;
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
            final long startTime2 = System.currentTimeMillis();
            futureResult = getFuture();
//            try        
//            {
//                Thread.sleep(5000);
//            } 
//            catch(InterruptedException ex) 
//            {
//                Thread.currentThread().interrupt();
//            }
            List<T> result = futureResult.get();
            LOG.log(Level.FINER, "call() took " + (System.currentTimeMillis() - startTime2) + " ms");
            return result;
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
                setItems(FXCollections.observableArrayList(result));
                LOG.log(Level.FINER, "setItems with result size of " + result.size() + " took " + (System.currentTimeMillis() - startTime) + " ms ");
            }

        }
    }

    //refactor due to dublicate code (occures in AsyncTableView)
    private class LoadingLayout extends VBox {

        public LoadingLayout() {
            setAlignment(Pos.CENTER);
            ProgressIndicator pi = new ProgressIndicator(-1);
//            pi.setMinHeight(100d);
            pi.getStyleClass().add("async-progress-indicator");
            setAlignment(Pos.CENTER);
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
                            afterTask(Worker.State.CANCELLED);
                        }
                    }
                });
            }
            parentProperty().addListener(new ChangeListener<Parent>() {
                @Override
                public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                    if (newValue != null && newValue instanceof Pane) {
//                        pi.setMinHeight(((Pane)newValue).getWidth()/3);
                        pi.minHeightProperty().bind(((Region) newValue).widthProperty().divide(5));
                    }
                }
            });
        }
    }

}

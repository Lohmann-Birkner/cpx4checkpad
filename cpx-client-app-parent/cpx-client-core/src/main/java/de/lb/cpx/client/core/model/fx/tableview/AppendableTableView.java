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
package de.lb.cpx.client.core.model.fx.tableview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Skin;
import javax.ejb.AsyncResult;

/**
 * Creates an appenable TableView appendable TableView loads only a specific
 * amount of items (maxItems) and appends additional items when the end of the
 * tableview is reached maxItems value of -1 is considert as indefinite and it
 * will load all avaiable items
 *
 * NOTE: Listen to "append-event" is handled in skin-class
 *
 * @author wilde
 * @param <T> content of the tableview
 */
public abstract class AppendableTableView<T> extends AsyncTableView<T> {

    private static final Logger LOG = Logger.getLogger(AppendableTableView.class.getName());

    private ArrayList<T> tempItemList;
    private boolean appendData = false;

    /**
     * creates new instance, loading task can not be aborted
     */
    public AppendableTableView() {
        this(false);
    }

    /**
     * creates new instance
     *
     * @param pAbortable if task could be aborted
     */
    public AppendableTableView(boolean pAbortable) {
        super(pAbortable);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AppendableTableViewSkin<>(this);
    }

    private static final Integer MAX_ITEMS_INDEFINITE = -1;
    private IntegerProperty maxItemsProperty;

    /**
     * @return max count property for bindings
     */
    public IntegerProperty maxItemsProperty() {
        if (maxItemsProperty == null) {
            maxItemsProperty = new SimpleIntegerProperty(MAX_ITEMS_INDEFINITE);
        }
        return maxItemsProperty;
    }

    /**
     * @return max count of items possible to be stored in the list
     */
    public Integer getMaxItems() {
        return maxItemsProperty().get();
    }

    /**
     * @param pMaxItems set new max count
     */
    public void setMaxItems(Integer pMaxItems) {
        maxItemsProperty().set(pMaxItems);
    }

    private BooleanProperty stopLoadingProperty;

    public BooleanProperty stopLoadingProperty() {
        if (stopLoadingProperty == null) {
            stopLoadingProperty = new SimpleBooleanProperty(false);
        }
        return stopLoadingProperty;
    }

    public Boolean isStopLoading() {
        return stopLoadingProperty().get();
    }

    public void setStopLoading(boolean pStopLoading) {
        stopLoadingProperty().set(pStopLoading);
    }

    /**
     * @return current page count, returns page currently loaded, due to
     * structure of the ui, returns last loaded page
     */
    public Integer getPageCounts() {
        return getCurrentCount() / getMaxItems();
    }

    @Override
    public void beforeTask() {
        if (!isLoadingTaskRunning()) {
            tempItemList = new ArrayList<>(getItems());
            addEventFilter(EventType.ROOT, suppressEvents);
            getItems().clear(); //CPX-1791, clear list to show loading animation. Important for working and workflow list otherwise on large lists confusing behavior
            //will occure
            super.beforeTask();
        } else {
            LOG.info("do not load! task is running");
        }
    }

    @Override
    public void reload() {
        super.reload();
    }

    /**
     * call append next page
     */
    public void appendData() {
        appendData = true;
        super.reload();
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    /**
     *
     * @param pStartIndex start index of range
     * @param pEndIndex end index of range
     * @return loaded items in range, if range is 0 and -1 than all items should
     * be loaded
     */
    public abstract List<T> loadItems(int pStartIndex, int pEndIndex);

    /**
     * @return apended list, loads task is called for new page, could be a long
     * process
     */
    private List<T> appendList() {
        List<T> currentItems = new ArrayList<>(tempItemList);
        Integer pageCount = getPageCounts();
        Integer nextPageCount = getNextPage();
        List<T> loaded = loadItems(pageCount * getMaxItems(), nextPageCount * getMaxItems());
        currentItems.addAll(loaded);
        LOG.finer("Append Loaded items! Current Count of Items " + loaded.size() + " loaded items " + currentItems.size() + " temp List " + tempItemList.size());
        return currentItems;
    }

    @Override
    public Future<List<T>> getFuture() {
        if (getMaxItems().equals(MAX_ITEMS_INDEFINITE)) {
            //should be interpreted as load all
            List<T> itms = loadItems(0, MAX_ITEMS_INDEFINITE);
            return new AsyncResult<>(itms);
        }
        if (appendData) {
            return new AsyncResult<>(appendList());
        }
        return new AsyncResult<>(loadItems(0, (getPageCounts() * getMaxItems())));
    }

    /**
     * @return index of next page
     */
    private Integer getNextPage() {
        return getPageCounts() + 1;
    }
    private EventHandler<Event> suppressEvents = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            event.consume();
        }

    };

    @Override
    public void afterTask(Worker.State pState) {
        removeEventFilter(EventType.ROOT, suppressEvents);
        super.afterTask(pState);
        if (pState.equals(Worker.State.SUCCEEDED)) {
            if (appendData) {
                if (!tempItemList.isEmpty()) {
//                    int scrollIndex = tempItemList.indexOf(Iterables.getLast(tempItemList));
//                    scrollTo(Iterables.getLast(tempItemList));
                    //AWi-20180928:
                    //somehow in workflow list this 
                    int scrollIndex = tempItemList.size() - 1;
                    scrollTo(scrollIndex);
                    LOG.finer("Append Loaded items! Scroll to " + scrollIndex);
                }
                tempItemList.clear();
                appendData = false;
            }
            setCurrentCount(getItems().size());
        }
    }
}

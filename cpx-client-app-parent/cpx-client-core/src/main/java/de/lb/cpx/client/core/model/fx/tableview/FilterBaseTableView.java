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

import de.lb.cpx.client.core.model.filter.FilterBasicItem;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.client.core.model.fx.tableview.searchitem.SearchItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;

/**
 * Base Implementation for filtered table views handles basic interactions:
 * showing of filter/search items sorting etc.
 *
 * TODO: Remove filterColumn References to make this implementation unattached
 * to filter config stuff
 *
 * @author wilde
 * @param <T> content in tableview
 */
public abstract class FilterBaseTableView<T> extends AppendableTableView<T> {

    public FilterBaseTableView() {
        super(true);
        getStyleClass().add("stay-selected-table-view");
        getStyleClass().add("filter-table-view");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FilterBaseTableViewSkin<>(this);
    }

    /**
     * updates columns, sorting, reordering, amount of shown columns
     */
    protected abstract void updateColumns();

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState);
        if (Worker.State.SUCCEEDED.equals(pState)) {
            getColumns().clear();
            getFilterItems().clear();
            updateColumns();
            if (getSelectionModel().getSelectedItem() == null) {
                getSelectionModel().selectFirst();
            }
        }
    }

    /**
     * add filter item in the ui area
     *
     * @param pColumn filter column
     * @param param filter values
     * @return if adding was successfully
     */
    protected Boolean addFilterItemInUi(FilterColumn<T, ? extends Object> pColumn, FilterBasicItem param) {
        //if value is empty, remove item
        if (removeIfInvalid(param)) {
            return true;
        }
        if (getColumnToSearchItemMap().containsKey(param.getDataKey())) {
            //TODO:remove list, replace with single item
            List<SearchItem> items = getColumnToSearchItemMap().get(param.getDataKey());
            //update item with new values
            items.get(0).setText(param.getLocalizedKey() + ": \n" + param.getLocalizedValue());
        } else {
            SearchItem item = createSearchItem(pColumn, param, param.getDataKey(), param.getValue());
//            if(param.hasTooltip()){
//                item.setTooltip(new BasicTooltip(param.getValue(), param.getTooltipText()));
//            }
            addFilterItem(item);
        }
        return true;
    }

    public Boolean removeIfInvalid(FilterBasicItem pItem) {
        if (pItem.getValue() != null && pItem.getValue().isEmpty()) {
            List<SearchItem> items = getColumnToSearchItemMap().get(pItem.getDataKey());
            if (!items.isEmpty()) {
                removeFilterItem(items.get(0));
            }
            return true;
        }
        return false;
    }

    /**
     * clean occurance of search item from ui
     *
     * @param pDataKey datakey of the filter
     * @param param filter data
     */
    protected void cleanUpSearchItems(String pDataKey, FilterBasicItem param) {
        //remove empty items on commit
        List<SearchItem> items = getColumnToSearchItemMap().get(pDataKey);
        if (items == null) {
            return;
        }
        if (param.getValue().isEmpty()) {
            removeFilterItem(items.get(0));
        }
    }
    private Map<String, List<SearchItem>> columnToSearchItemMap;

    /**
     * @return map of all filters for filter datakey
     */
    protected Map<String, List<SearchItem>> getColumnToSearchItemMap() {
        if (columnToSearchItemMap == null) {
            columnToSearchItemMap = new HashMap<>();
        }
        return columnToSearchItemMap;
    }

    //creates search item form filter values for a specific column
    //register mouse listeners
    private SearchItem createSearchItem(FilterColumn<T, ? extends Object> pColumn, FilterBasicItem pFilter, String pKey, String pFilterValue) {
        if (pFilterValue == null || pFilterValue.isEmpty()) {
            return null;
        }
        SearchItem item = new SearchItem(pKey, pFilter.getLocalizedKey() + ": \n" + pFilter.getLocalizedValue());
        item.setOnCloseEvent(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pFilter.setValue("");
                pColumn.getOnFilterEdit().call(new FilterBasicItem[]{pFilter});
            }
        });
        item.setOnMousePressed( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                scrollToColumn(pColumn);
                pColumn.showFilter();
                pColumn.selectFilterControl(pKey);
            }
        });
        return item;
    }

    //add filter item to map
    private boolean addFilterItem(SearchItem pItem) {
        if (pItem == null) {
            return false;
        }
        if (getColumnToSearchItemMap().get(pItem.getDataKey()) == null) {
            getColumnToSearchItemMap().put(pItem.getDataKey(), new ArrayList<>());
        }
        getColumnToSearchItemMap().get(pItem.getDataKey()).add(pItem);
        return getFilterItems().add(pItem);
    }

    //remove filteritem from map
    private boolean removeFilterItem(SearchItem pItem) {
        if (pItem == null) {
            return false;
        }
        getColumnToSearchItemMap().get(pItem.getDataKey()).remove(pItem);
        if (getColumnToSearchItemMap().get(pItem.getDataKey()).isEmpty()) {
            getColumnToSearchItemMap().remove(pItem.getDataKey());
        }
        pItem.getText();
        return getFilterItems().remove(pItem);
    }

    /**
     * clean filter map
     */
    protected void cleanFilterItems() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getColumnToSearchItemMap().clear();
                getFilterItems().clear();
            }
        });
    }
    private boolean showTooltip = true;

    public void setShowTooltip(boolean pShow) {
        showTooltip = pShow;
    }

    public boolean isShowTooltip() {
        return showTooltip;
    }
}

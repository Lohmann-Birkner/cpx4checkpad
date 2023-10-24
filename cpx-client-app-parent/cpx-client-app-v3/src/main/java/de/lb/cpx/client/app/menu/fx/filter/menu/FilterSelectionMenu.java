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
package de.lb.cpx.client.app.menu.fx.filter.menu;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.searchlist.SearchListResult;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Filter Selection Menu as Button Could possibly implement this as combobox?
 *
 * Contains button to react of user click the selection available filter are
 * shown in popover element with listview listview contains edit and delete
 * buttons new filter could be added
 *
 * @author wilde
 * @param <E> type of serchelist to show filters
 */
public class FilterSelectionMenu<E extends SearchListResult> extends Button {

    private static final String DEFAULT_TEXT = "Filter wählen";
    private SearchListTypeEn listTyp = SearchListTypeEn.WORKING;
    //height property of the popover
    private IntegerProperty popOverHeightProperty;
    //width of the popover
    private IntegerProperty popOverWidthProperty;
    //in some cases no more filters should be added
    //for example ruleList
    private BooleanProperty disableFilterCreationProperty;
    //in some cases filters should not be editable and deleteable
    //to reduce objects, this property enables/disables both
    //if need arises than a new boolean property should be added
    //for example ruleList
    //TODO:Maybe Filters should know on item level if there are editable, deleteable
    private BooleanProperty disableFilterEditProperty;
    //list of items shown on the menu
    private final ObservableList<E> items = FXCollections.observableArrayList();
    //selected item property to get currently choosen item in the menu
    private ObjectProperty<E> selectedItemProperty;
    //callback to executed if delete is requested
    private Callback<E, Boolean> onDeleteCallback;
    //callback to be executed if filter should be copied in new instance
    private Callback<E, E> onCopyCallback;
    //callback to be called if a new filter is requested
    private Callback<String, E> onCreateCallback;
    //update callback to be called if filter is changed
    private Callback<SearchListResult, E> onUpdateCallback;
    //load callback to be called if list of filters is request
    //when popover is shown
    private Callback<Void, List<E>> onLoadCallback;
    //callback to be called if filter selection is changed
    private Callback<E, Boolean> onChangeCallback;
    //callback to persist existing filter in database
    //TODO:could be unifed to some saveOrPersist-callback
    private Callback<SearchListResult, E> onPersistCallback;

    public FilterSelectionMenu() {
        super(DEFAULT_TEXT, ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOWN));
        getStyleClass().setAll("cpx-icon-button");
        setStyle("-fx-content-display:right;");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        FilterSelectionMenuSkin<E> menuSkin = new FilterSelectionMenuSkin<>(this);
        menuSkin.loadData();
        return menuSkin;
    }

    public void setListTyp(SearchListTypeEn pListTyp) {
        listTyp = pListTyp;
    }

    public SearchListTypeEn getListTyp() {
        return listTyp;
    }

    /**
     * @return get active filter
     */
    public E getActive() {
//        for(E item : getItems()){
//            if(item.isSelected()){
//                return item;
//            }
//        }
        return getSelectedItem();//CpxClientConfig.instance().getSelected
    }

    /**
     * @param pItem set filter as active
     * @return if filter could set active
     */
    public boolean setActive(E pItem) {
        if (pItem == null) {
            return false;
        }
        MenuCache.getMenuCacheSearchLists().setSelectedSearchList(pItem);
//        for(E item : getItems()){
//            if(item.isSelected()){
//                item.setSelected(false);
//                break;
//            }
//        }
//        pItem.setSelected(true);
        return true;
    }

    /**
     * @param pName name of the filter
     * @return if filter with the same name exists in items
     */
    public boolean isExisting(String pName) {
        for (E item : getItems()) {
            if (item.getName().equals(pName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return height property of popover
     */
    public IntegerProperty popOverHeightProperty() {
        if (popOverHeightProperty == null) {
            popOverHeightProperty = new SimpleIntegerProperty(250);
        }
        return popOverHeightProperty;
    }

    /**
     * @param pPopOverHeight new height of popover, value applied after redraw
     * of the popover
     */
    public void setPopOverHeight(int pPopOverHeight) {
        popOverHeightProperty().set(pPopOverHeight);
    }

    /**
     * @return current height of the popover
     */
    public int getPopOverHeight() {
        return popOverHeightProperty().get();
    }

    /**
     * @return width property of the popover for listenings or bindings
     */
    public IntegerProperty popOverWidthProperty() {
        if (popOverWidthProperty == null) {
            popOverWidthProperty = new SimpleIntegerProperty(425);
        }
        return popOverWidthProperty;
    }

    /**
     * @param pPopOverWidth new width of the popover
     */
    public void setPopOverWidth(int pPopOverWidth) {
        popOverWidthProperty().set(pPopOverWidth);
    }

    /**
     * @return width of the of popover
     */
    public int getPopOverWidth() {
        return popOverWidthProperty().get();
    }

    /**
     * @return disableFilterProperty for bindings, changes only applied after
     * redraw(?)
     */
    public BooleanProperty disableFilterCreationProperty() {
        if (disableFilterCreationProperty == null) {
            disableFilterCreationProperty = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return disableFilterCreationProperty;
    }

    /**
     * @param pDisableCreation set if filter creation is now disabled, changes
     * only applied after redraw(?)
     */
    public void setDisableFilterCreation(boolean pDisableCreation) {
        disableFilterCreationProperty().set(pDisableCreation);
    }

    /**
     * @return if filter creation is disabled
     */
    public boolean isDisableFilterCreation() {
        return disableFilterCreationProperty().get();
    }

    /**
     * @return property for bindings/listenings
     */
    public BooleanProperty disableFilterEditProperty() {
        if (disableFilterEditProperty == null) {
            disableFilterEditProperty = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return disableFilterEditProperty;
    }

    /**
     * @param pDisableEdit set if edit/delete of the filters is disabled,
     * changes are applied after redraw(?)
     */
    public void setDisableFilterEdit(boolean pDisableEdit) {
        disableFilterEditProperty().set(pDisableEdit);
    }

    /**
     * @return if filters edit/delete is disabled
     */
    public boolean isDisableFilterEdit() {
        return disableFilterEditProperty().get();
    }

    /**
     * @return list of items in the menu
     */
    public ObservableList<E> getItems() {
        return items;
    }

    /**
     * @param pItems items to be shown on the menu
     */
    public void setItems(List<E> pItems) {
        items.setAll(pItems);
    }

    /**
     * @return selected item property for bindings/listening
     */
    public ObjectProperty<E> selectedItemProperty() {
        if (selectedItemProperty == null) {
            selectedItemProperty = new SimpleObjectProperty<>();
        }
        return selectedItemProperty;
    }

    /**
     * @return get selected item in the menu
     */
    public E getSelectedItem() {
        return selectedItemProperty().get();
    }

    /**
     * @param pItem set item as selected
     */
    public void setSelectedItem(E pItem) {
        selectedItemProperty().set(pItem);
    }

    /**
     * @param pCallback callback to be called if delete is requested
     */
    public void setOnDeleteCallback(Callback<E, Boolean> pCallback) {
        onDeleteCallback = pCallback;
    }

    /**
     * @return get currently set callback to be called on delete
     */
    public Callback<E, Boolean> getOnDeleteCallback() {
        return onDeleteCallback;
    }
    /**
     * @param pCallback callback to be called if delete is requested
     */
    public void setOnCopyCallback(Callback<E, E> pCallback) {
        onCopyCallback = pCallback;
    }

    /**
     * @return get currently set callback to be called on delete
     */
    public Callback<E, E> getOnCopyCallback() {
        return onCopyCallback;
    }
    /**
     * @param pCallback callback to be called on creation of a new filter
     */
    public void setOnCreateCallback(Callback<String, E> pCallback) {
        onCreateCallback = pCallback;
    }

    /**
     * @return callback currently called if new filter is requested
     */
    public Callback<String, E> getOnCreateCallback() {
        return onCreateCallback;
    }

    /**
     * @param pCallback set callback to be called if filter is updated
     */
    public void setOnUpdateCallback(Callback<SearchListResult, E> pCallback) {
        onUpdateCallback = pCallback;
    }

    /**
     * @return callback currently set if update is requested
     */
    public Callback<SearchListResult, E> getOnUpdateCallback() {
        return onUpdateCallback;
    }

    /**
     * @param pCallback set load callback to get list of filters called when
     * popover is shown
     */
    public void setOnLoadCallback(Callback<Void, List<E>> pCallback) {
        onLoadCallback = pCallback;
    }

    /**
     * @return callback to be called if list of filters is requested
     */
    public Callback<Void, List<E>> getOnLoadCallback() {
        return onLoadCallback;
    }

    /**
     * @param pCallback set change callback to change selected filter
     */
    public void setOnChangeCallback(Callback<E, Boolean> pCallback) {
        onChangeCallback = pCallback;
    }

    /**
     * @return callback to be called when selected filter changed
     */
    public Callback<E, Boolean> getOnChangeCallback() {
        return onChangeCallback;
    }

    /**
     * @param pCallback to be called of filter should be persisted
     */
    public void setOnPersistCallback(Callback<SearchListResult, E> pCallback) {
        onPersistCallback = pCallback;
    }

    /**
     * @return callback to be called when filter should be persisted
     */
    public Callback<SearchListResult, E> getOnPersistCallback() {
        return onPersistCallback;
    }

    /**
     * check if items contains filter with the filter id iterates through list,
     * maybe an issue if list is very long?
     *
     * @param pFilterId id of the filter
     * @return if a filter is present in the list
     */
    public boolean contrainsFilterId(long pFilterId) {
        for (E item : getItems()) {
            if (item.getId() == pFilterId) {
                return true;
            }
        }
        return false;
    }
}

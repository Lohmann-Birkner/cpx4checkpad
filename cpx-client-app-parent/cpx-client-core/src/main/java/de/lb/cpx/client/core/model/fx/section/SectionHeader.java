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
package de.lb.cpx.client.core.model.fx.section;

import java.util.Arrays;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * Control Implementation of the Section Header, Header is returning ui element
 * that should be reusable in other context than this section Name of section
 * must be refactored
 *
 * @author wilde
 */
public class SectionHeader extends Control {

    /*
  *
  * Properties
  *
     */
    private StringProperty title;
    /*
  * menu items
     */
    private final ObservableList<Node> menuItems = FXCollections.observableArrayList();
    /*
  *  search items
     */
    private final ObservableList<Node> searchItems = FXCollections.observableArrayList();
    /*
    * additional title infos
     */
    private final ObservableList<Node> titleInfoItems = FXCollections.observableArrayList();

    /**
     * creates new instance with title
     *
     * @param pTitle title to set
     */
    public SectionHeader(String pTitle) {
        super();
        setTitle(pTitle);
    }

    /**
     * no-arg constructpor for scene builder sets default title text "Label"
     */
    public SectionHeader() {
        this("Label");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SectionHeaderSkin(this);
    }

    /**
     * @return gets title property
     */
    public final StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "text", "");
        }
        return title;
    }

    /**
     * @param pTitle sets title to value
     */
    public final void setTitle(String pTitle) {
        titleProperty().setValue(pTitle);
    }

    /**
     * @return gets the current title or empty string whne null
     */
    public final String getTitle() {
        return title == null ? "" : title.getValue();
    }

    /**
     * @return list of all additional title infos nodes
     */
    public final ObservableList<Node> titleInfoItems() {
        return titleInfoItems;
    }

    /**
     * @param pElements adds list of elements to additional title infos
     */
    public final void addTitleInfoItems(Node... pElements) {
        titleInfoItems.addAll(pElements);
    }

    /**
     * @return get the list displayed in the additional title info area
     */
    public final ObservableList<Node> getTitleInfoItems() {
        return titleInfoItems == null ? FXCollections.observableArrayList() : titleInfoItems;
    }

    /**
     * clear nodes of additonal title info
     */
    public final void clearTitleInfoItems() {
        if (titleInfoItems != null) {
            titleInfoItems.clear();
        }
    }

    /**
     * @return list of all menu nodes
     */
    public final ObservableList<Node> menuItems() {
        return menuItems;
    }

    /**
     * @param pElements adds list of elements to menu
     */
    public final void addMenuItems(Node... pElements) {
        menuItems.addAll(pElements);
    }
    
    public final void addMenuItems(int pIndex,Node... pElements) {
        if(pIndex>0){
            menuItems.addAll(pIndex, Arrays.asList(pElements));
        }else{
            addMenuItems(pElements);
        }
    }
    
    /**
     * @return get the list displayed in the Menu
     */
    public final ObservableList<Node> getMenuItems() {
        return menuItems == null ? FXCollections.observableArrayList() : menuItems;
    }

    /**
     * clear notes in menu
     */
    public final void clearMenuItems() {
        if (menuItems != null) {
            menuItems.clear();
        }
    }

    /**
     * @return get list of nodes in the search area
     */
    public final ObservableList<Node> searchItems() {
        return searchItems;
    }

    /**
     * @param values add values to search area
     */
    public final void addSearchItems(Node... values) {
        searchItems.addAll(values);
    }

    /**
     * @return get all nodes in search area
     */
    public final ObservableList<Node> getSearchItems() {
        return searchItems == null ? FXCollections.observableArrayList() : searchItems;
    }

    /**
     * clear nodes in search area
     */
    public final void clearSearchItems() {
        if (searchItems != null) {
            searchItems.clear();
        }
    }
    private Button btnSearchItem;

    public Button getSearchItemsButton() {
        if (btnSearchItem == null) {
            btnSearchItem = new Button();
        }
        return btnSearchItem;
    }

    public void setSearchItemButton(Button pButton) {
        btnSearchItem = pButton;
    }
    
    private ObjectProperty<Node> titleGraphicProperty;
    public ObjectProperty<Node> titleGraphicProperty(){
        if(titleGraphicProperty == null){
            titleGraphicProperty = new SimpleObjectProperty<>();
        }
        return titleGraphicProperty;
    }
    public Node getTitleGraphic(){
        return titleGraphicProperty().get();
    }
    public void setTitleGraphic(Node pGraphic){
        titleGraphicProperty().set(pGraphic);
    }
    
    private ObjectProperty<ContentDisplay> titleContentDisplayProperty;
    public ObjectProperty<ContentDisplay> titleContentDisplayProperty(){
        if(titleContentDisplayProperty == null){
            titleContentDisplayProperty = new SimpleObjectProperty<>(ContentDisplay.LEFT);
        }
        return titleContentDisplayProperty;
    }
    public void setTitleContentDisplay(ContentDisplay pDisplay){
        titleContentDisplayProperty().set(pDisplay);
    }
    public ContentDisplay getTitleContentDisplay(){
        return titleContentDisplayProperty().get();
    }
}

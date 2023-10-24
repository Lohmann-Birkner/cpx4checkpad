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
package de.lb.cpx.client.core.model.fx.listview.cell;

import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;

/**
 * Two Line List Cell class Default implementation of list cell to show plain
 * text support displaying text in two lines first for title and second for a
 * description title is mandatory, description is optional if no description is
 * set, only one line is shown in the ui(description label will not be set in
 * the cell) default alignment is CENTER-LEFT default height is 40, regardless
 * if description is shown or not possible to set icon button, for some
 * interaction in the ui default not set, and its added in the left side of the
 * cell
 *
 * @author wilde
 * @param <T> item type of list cell
 */
public class TwoLineCell<T> extends ListCell<T> {

    public static final Integer DEFAULT_HEIGHT = 40;
    private final StringProperty titleProperty = new SimpleStringProperty(null);
    private final StringProperty descriptionProperty = new SimpleStringProperty(null);
    private final ObjectProperty<Pos> alignmentProperty = new SimpleObjectProperty<>(Pos.CENTER_LEFT);
    private final ObjectProperty<Button> iconButtonProperty = new SimpleObjectProperty<>(null);
    private final List<MenuItem> contextMenuItems = new ArrayList<>();

    public TwoLineCell() {
        super();
        //set default height
        setPrefHeight(DEFAULT_HEIGHT);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TwoLineCellSkin<>(this);
    }

    /**
     * populate context menu with list of items creates new context menu
     * instance!
     */
    public void populateContextMenu() {
        ContextMenu menu = new CtrlContextMenu<>();
        menu.getItems().addAll(contextMenuItems);
        //contextMenuItems.addAll(contextMenuItems);
        setContextMenu(menu);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            return;
        }
        //after redrawing set new context menu
        populateContextMenu();
    }

    /**
     * @param pTitle title to show
     */
    public void setTitle(String pTitle) {
        titleProperty.set(pTitle);
    }

    /**
     * @return currently set title
     */
    public String getTitle() {
        return titleProperty.get();
    }

    /**
     * @return title property for binding
     */
    public StringProperty titleProperty() {
        return titleProperty;
    }

    /**
     * @param pDesc description
     */
    public void setDescription(String pDesc) {
        descriptionProperty.set(pDesc);
    }

    /**
     * @return description property for bindings
     */
    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }

    /**
     * @param pPos graphical alignment of the cell
     */
    public void setGraphicAlignment(Pos pPos) {
        alignmentProperty.set(pPos);
    }

    /**
     * @return graphical alignment property for binding
     */
    public ObjectProperty<Pos> graphicAlignmentProperty() {
        return alignmentProperty;
    }

    /**
     * @return icon button property for binding
     */
    public ObjectProperty<Button> iconButtonProperty() {
        return iconButtonProperty;
    }

    /**
     * @return icon button, null if not set
     */
    public Button getIconButton() {
        return iconButtonProperty.get();
    }

    /**
     * @param pIconButton set icon button
     */
    public void setIconButton(Button pIconButton) {
        iconButtonProperty.set(pIconButton);
    }
    
    private ObjectProperty<Insets> rootPaddingProperty;
    public ObjectProperty<Insets> rootPaddingProperty(){
        if(rootPaddingProperty == null){
            rootPaddingProperty = new SimpleObjectProperty<>(new Insets(0));
        }
        return rootPaddingProperty;
    }
    public Insets getRootPadding(){
        return rootPaddingProperty().get();
    }
    public void setRootPaddding(Insets pInsets){
        rootPaddingProperty().set(pInsets);
    }
}

/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;

/**
 *
 * @author wilde
 * @param <T> objects stored in table
 */
public class FixedTable<T> extends Control {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FixedTableSkin<>(this);
    }

    protected final ObservableList<T> items = FXCollections.observableArrayList();

    protected ObservableList<T> getItems() {
        return items;
    }

    protected final ObservableList<T> fixedItems = FXCollections.observableArrayList();

    protected ObservableList<T> getFixedItems() {
        return fixedItems;
    }

    protected final ObservableList<TableColumn<T, ?>> columns = FXCollections.observableArrayList();

    public ObservableList<TableColumn<T, ?>> getColumns() {
        return columns;
    }
    protected final ObservableList<TableColumn<T, ?>> fixedColumns = FXCollections.observableArrayList();

    public ObservableList<TableColumn<T, ?>> getFixedColumns() {
        return fixedColumns;
    }
    protected ReadOnlyBooleanWrapper vBarShowingProperty;

    public ReadOnlyBooleanProperty vBarShowingProperty() {
        if (vBarShowingProperty == null) {
            vBarShowingProperty = new ReadOnlyBooleanWrapper(false);
        }
        return vBarShowingProperty.getReadOnlyProperty();
    }

    public Boolean isVBarShowing() {
        return vBarShowingProperty().get();
    }
    
    private ObjectProperty<DisplayMode> displayModeProperty;
    public ObjectProperty<DisplayMode> displayModeProperty(){
        if(displayModeProperty == null){
            displayModeProperty = new SimpleObjectProperty<>(DisplayMode.NORMAL);
        }
        return displayModeProperty;
    }
    public void setDisplayMode(DisplayMode pMode){
        displayModeProperty().set(pMode);
    }
    public DisplayMode getDisplayMode(){
        return displayModeProperty().get();
    }
    public enum DisplayMode{
        READ_ONLY,NORMAL;
    }
}

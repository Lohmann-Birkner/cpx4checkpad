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
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import java.beans.PropertyDescriptor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 * Selectable control, basic ui class to handle selection and basic events
 *
 * @author wilde
 */
public abstract class SelectableControl extends Control {

    public static final String UPDATE_CONTROL = "update.control";
    public static final String REFRESH_VALUE = "refresh";
//    protected StructureTreeItem treeItem;
    private ObservableList<SelectableControl> controls;
    //selected node
    private ReadOnlyObjectWrapper<Node> selectedNodeProperty;
    private ObjectProperty<Callback<Void, Boolean>> deleteCallbackProperty;
    //specify view mode
    private ObjectProperty<ViewMode> viewModeProperty;
    //specify font
    private ObjectProperty<Font> fontProperty;
    //parent element
    private ObjectProperty<Element> parentElementProperty;
    //highlighted
    private final BooleanProperty highlightedProperty = new SimpleBooleanProperty(false);

    /**
     * no arg constructor to create new selectedable control
     */
    public SelectableControl() {
        super();
        //init listeners
        viewModeProperty().addListener(new ChangeListener<ViewMode>() {
            @Override
            public void changed(ObservableValue<? extends ViewMode> observable, ViewMode oldValue, ViewMode newValue) {
                setContextMenu(populateContextMenu());
            }
        });
        setContextMenu(populateContextMenu());
        getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded() && UPDATE_CONTROL.equals(change.getKey())) {
//                    getTreeItem().getValue().setDisplayText(getDisplayText());
//                    TreeItem.TreeModificationEvent<StructureItem> event = new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), treeItem);
//                    Event.fireEvent(treeItem, event);
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(SelectableControl.this, saveEvent);
                    RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), SelectableControl.this);
                    Event.fireEvent(SelectableControl.this, event);
                    getProperties().remove(UPDATE_CONTROL);
                }
            }
        });

    }

    /**
     * @return get the selected childen, if no childeren this whould reference
     * to current node
     */
    public Node getSelectedNode() {
        return selectedNodeProperty().get();
    }

    /**
     * @param pNode set node to be highlighted, null clears highlight
     */
    public void setSelectedNode(Node pNode) {
        selectedNodeProperty().set(pNode);
    }

    /**
     * @return selected node property for binds
     */
    public ReadOnlyObjectWrapper<Node> selectedNodeProperty() {
        if (selectedNodeProperty == null) {
            selectedNodeProperty = new ReadOnlyObjectWrapper<>();
        }
        return selectedNodeProperty;
    }

    /**
     * @return list of property descriptors for the property sheet display
     */
    public abstract PropertyDescriptor[] getPropertyDescriptors();

    /**
     * @return simple display name for the control, something like
     * term,verknuepfung etc
     */
    public abstract String getDisplayName();

    /**
     * @return whole display text as string
     */
    public abstract String getDisplayText();

    /**
     * @return construct tree item to display control in treeview
     */
//    public StructureTreeItem getTreeItem() {
//        if (treeItem == null) {
//            treeItem = new StructureTreeItem(this);
//            treeItem.getValue().setDisplayText(getDisplayText());
//        }
//        return treeItem;
//    }
    /**
     * @return delete callback property, stores callback that is to be executed
     * if delete happend on control most likly over the context menu
     */
    protected ObjectProperty<Callback<Void, Boolean>> deleteCallbackProperty() {
        if (deleteCallbackProperty == null) {
            deleteCallbackProperty = new SimpleObjectProperty<>();
        }
        return deleteCallbackProperty;
    }

    /**
     * @return delete callback, null if nothing is set
     */
    public Callback<Void, Boolean> getDeleteCallback() {
        return deleteCallbackProperty().get();
    }

    /**
     * @param pCallback callback to be executed on delete, should return boolean
     * if delete was a success
     */
    public void setDeleteCallback(Callback<Void, Boolean> pCallback) {
        deleteCallbackProperty().set(pCallback);
    }

    /**
     * @return populated context menu, methode should be overwritten to interact
     * with the stored context menu
     */
    public ContextMenu populateContextMenu() {
        if (isReadOnly()) {
            return new ContextMenu();
        }
        MenuItem itemDelete = new MenuItem("Löschen");
        itemDelete.disableProperty().bind(Bindings.isNull(deleteCallbackProperty()));
        itemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getDeleteCallback() != null) {
                    if (getDeleteCallback().call(null)) {
                        RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                        Event.fireEvent(SelectableControl.this, saveEvent);
                    }
                }
            }
        });
        ContextMenu menu = new ContextMenu(itemDelete);
//        getTreeItem().getValue().setContextMenu(menu);
        return menu;
    }

    /**
     * @return view mode object property to react to changes
     */
    public final ObjectProperty<ViewMode> viewModeProperty() {
        if (viewModeProperty == null) {
            viewModeProperty = new SimpleObjectProperty<>(ViewMode.READ_ONLY);
        }
        return viewModeProperty;
    }

    /**
     * @param pMode set new mode
     */
    public void setViewMode(ViewMode pMode) {
        viewModeProperty().set(pMode);
    }

    /**
     * @return get current mode, specifies if control is readonly or could be
     * edited
     */
    public ViewMode getViewMode() {
        return viewModeProperty().get();
    }

    /**
     * @return true if control is readonly
     */
    public boolean isReadOnly() {
        return ViewMode.READ_ONLY.equals(getViewMode());
    }

    /**
     * @return list of all child controls for the control, for everthing other
     * than element this should be empty!
     */
    public ObservableList<SelectableControl> getControls() {
        if (controls == null) {
            controls = FXCollections.observableArrayList();
        }
        return controls;
    }

    /**
     * @param pCtrl children control
     * @return index of the control
     */
    public int getIndex(SelectableControl pCtrl) {
        return controls.indexOf(pCtrl);
    }

    /**
     * @return font property that is used in the texts in the control
     */
    public ObjectProperty<Font> fontProperty() {
        if (fontProperty == null) {
            fontProperty = new SimpleObjectProperty<>(Font.getDefault());
        }
        return fontProperty;
    }

    /**
     * @param pFont new font to use in texts
     */
    public void setFont(Font pFont) {
        fontProperty().set(pFont);
    }

    /**
     * @return get currently used font
     */
    public Font getFont() {
        return fontProperty().get();
    }

    /**
     * @return object property for the parent not of the element, should be
     * readonly
     */
    public ObjectProperty<Element> parentElementProperty() {
        if (parentElementProperty == null) {
            parentElementProperty = new SimpleObjectProperty<>();
        }
        return parentElementProperty;
    }

    /**
     * @param pParent set new parent element, should be protected or something
     */
    public void setParentElement(Element pParent) {
        parentElementProperty().set(pParent);
    }

    /**
     * @return get Parentelement of the control
     */
    public Element getParentElement() {
        return parentElementProperty().get();
    }

    /**
     * @return get property if control is in the highlighted state
     */
    public BooleanProperty highlightedProperty() {
        return highlightedProperty;
    }

    /**
     * @return indicator if control is considert highlighted
     */
    public Boolean isHighlighted() {
        return highlightedProperty().get();
    }

    /**
     * @param pHighlighted set control to be highlighted, unsure if it works
     */
    public void setHighlighted(Boolean pHighlighted) {
        highlightedProperty().set(pHighlighted);
    }

    public void refresh() {

    }
}

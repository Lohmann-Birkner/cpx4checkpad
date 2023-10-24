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
package de.lb.cpx.client.core.menu.model;

import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.Toolbar;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * SimpleContainer Class to handle toolbar menu states
 *
 * @author wilde
 */
public abstract class ToolbarMenuItem {

    private static final PseudoClass SELECTED_CLASS = PseudoClass.getPseudoClass("selected");

    private String id = "";
    private Node extendedRoot;
    private Node reducedRoot;
    private final BooleanProperty disableProperty = new SimpleBooleanProperty(false);
//    protected Binding graphicBinding = Bindings
//            .when(glyphProperty().isNull())
//            .then((Node) null)
//            .otherwise(ResourceLoader.getGlyph(getGlyph()));
    protected final Toolbar bar;
    //extended text
    private final StringProperty extendedTitleProperty = new SimpleStringProperty("");
    //glyph
    private ObjectProperty<FontAwesome.Glyph> glyphProperty;// = new SimpleObjectProperty<>();
    //tooltip to show on the menu entries
    private ObjectProperty<Tooltip> tooltipProperty;
    //focused node in the menu
    private ObjectProperty<Node> focusedNodeProperty;

    public ToolbarMenuItem(Toolbar pBar) {
        bar = pBar;
        //init layout, maybe not neccessary
        getExtendedNode();
        getReducedNode();

        focusedNodeProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                if (newValue != null) {
                    newValue.pseudoClassStateChanged(SELECTED_CLASS, true);
                    if (newValue == getExtendedNode()) {
                        getReducedRoot().pseudoClassStateChanged(SELECTED_CLASS, true);
                    }
                    if (newValue == getReducedNode()) {
                        getExtendedRoot().pseudoClassStateChanged(SELECTED_CLASS, true);
                    }
                }
                if (oldValue != null) {
                    oldValue.pseudoClassStateChanged(SELECTED_CLASS, false);
                    if (oldValue == getExtendedNode()) {
                        getReducedNode().pseudoClassStateChanged(SELECTED_CLASS, false);
                    }
                    if (oldValue == getReducedNode()) {
                        getExtendedNode().pseudoClassStateChanged(SELECTED_CLASS, false);
                    }
                }
            }
        });
    }

    /**
     * @return string id of the item to identify it
     */
    public String getId() {
        return id;
    }

    /**
     * @param id id of the menu item
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Menu item when toolbar is extended, as node
     */
    public abstract Node getExtendedNode();

    /**
     * @return Menu item when toolbar is reduced, as node
     */
    public abstract Node getReducedNode();

    /**
     * @return get root node for extended mode
     */
    public Node getExtendedRoot() {
        return extendedRoot;
    }

    /**
     * @param extendedRoot root object to show in extended mode
     */
    public void setExtendedRoot(Node extendedRoot) {
        this.extendedRoot = extendedRoot;
    }

    /**
     * @return root object to show in reduced mode
     */
    public Node getReducedRoot() {
        return reducedRoot;
    }

    /**
     * @param reducedRoot set root for reduced mode
     */
    public void setReducedRoot(Node reducedRoot) {
        this.reducedRoot = reducedRoot;
    }

    /**
     * @return textproperty to show in extended mode
     */
    public final StringProperty extendedTitleProperty() {
        return extendedTitleProperty;
    }

    /**
     * @param pTitle title to show in extended mode
     */
    public final void setExtendedTitle(String pTitle) {
        extendedTitleProperty().set(pTitle);
    }

    /**
     * @return title to show in extended mode
     */
    public final String getExtendedTitle() {
        return extendedTitleProperty().get();
    }

    /**
     * @return font awesome gylph to show indicator for the menu
     */
    public final ObjectProperty<FontAwesome.Glyph> glyphProperty() {
        if (glyphProperty == null) {
            glyphProperty = new SimpleObjectProperty<>();
        }
        return glyphProperty;
    }

    /**
     * @param pGlyph icon to show
     */
    public final void setGlyph(FontAwesome.Glyph pGlyph) {
        glyphProperty().set(pGlyph);
    }

    /**
     * @return icon
     */
    public final FontAwesome.Glyph getGlyph() {
        return glyphProperty().get();
    }

    /**
     * @return proeprty to bind desired tooltip
     */
    public final ObjectProperty<Tooltip> tooltipProperty() {
        if (tooltipProperty == null) {
            tooltipProperty = new SimpleObjectProperty<>();
        }
        return tooltipProperty;
    }

    /**
     * @return get tooltip
     */
    public final Tooltip getTooltip() {
        return tooltipProperty().get();
    }

    /**
     * @param pTip tooltip to show on menu nodes in extended or reduced mode
     */
    public final void setTooltip(Tooltip pTip) {
        tooltipProperty().set(pTip);
    }

    /**
     * @return disable property to disable interaction on the item
     */
    public BooleanProperty disableProperty() {
        return disableProperty;
    }

    /**
     * @return indicator if menu is currently disabled
     */
    public boolean isDisabled() {
        return disableProperty().get();
    }

    /**
     * @param pDisable enable/disable menu item
     */
    public void setDisable(boolean pDisable) {
        disableProperty().set(pDisable);
    }

    /**
     * @return property which contains node that is currently considered as
     * focused
     */
    public final ObjectProperty<Node> focusedNodeProperty() {
        if (focusedNodeProperty == null) {
            focusedNodeProperty = new SimpleObjectProperty<>();
        }
        return focusedNodeProperty;
    }

    /**
     * @return currently focused node
     */
    public final Node getFocusedNode() {
        return focusedNodeProperty().get();
    }

    /**
     * @param pFocus focus a specific node
     */
    public final void focusNode(Node pFocus) {
        focusedNodeProperty().set(pFocus);
    }

    /**
     * focus title of the menu item
     */
    public final void focusTitle() {
        focusNode(getExtendedNode());
    }

    /**
     * refresh menu item, triggers redraw
     */
    public void refresh() {
    }
}

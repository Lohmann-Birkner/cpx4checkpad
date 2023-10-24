/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.sectiontitle;

import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.ResourceLoader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.controlsfx.glyphfont.Glyph;

/**
 * Basic Layout of SectionTitle Item
 *
 * @author wilde
 */
public class SectionTitleSkin {

    private VBox root;
    private AnchorPane content;
    private EventHandler<MouseEvent> contextHandler;
    private final ObjectProperty<ContextMenu> contextMenuProperty = new SimpleObjectProperty<>();
    private SectionHeader headerArea;
    private BooleanProperty headerProperty = new SimpleBooleanProperty(true);
    private SectionTitle header;

    /**
     * construct new Skin instance and load layout
     *
     * @param pHeader section title
     */
    public SectionTitleSkin(SectionTitle pHeader) {
        header = pHeader;
        setUpLayout();
    }

    public SectionTitleSkin(SectionTitle pHeader, BooleanProperty pheaderSection) {
        this(pHeader);
        headerProperty = pheaderSection;
    }
//without HeaderSection  when pheaderProperty is false

    SectionTitleSkin(SectionTitle pHeader, Boolean pheaderProperty) {
        this(pHeader);
        if (!pheaderProperty) {
            setHeaderProperty(false);
        }
//        setUpLayout();
    }

    /**
     * get root pane of the section
     *
     * @return root pane
     */
    public Parent getRoot() {
        return root;
    }
    public void dispose(){
        root.getChildren().clear();
        content.minHeightProperty().unbind();
        content.maxHeightProperty().unbind();
    }
    private void setUpLayout() {
        root = new VBox();
        VBox.setMargin(root, new Insets(10, 10, 10, 10));
        HBox.setMargin(root, new Insets(10, 10, 10, 10));
        HBox.setHgrow(root, Priority.ALWAYS);
        VBox.setVgrow(root, Priority.ALWAYS);
        headerArea = new SectionHeader();
        content = new AnchorPane();
        content.minHeightProperty().bind(header.minHeightProperty());
        content.maxHeightProperty().bind(header.maxHeightProperty());
        VBox.setVgrow(content, Priority.ALWAYS);
        if (isHeaderSection()) {
            root.getChildren().addAll(headerArea, content);
        } else {
            root.getChildren().addAll(content);

        }

        header.registerPropertyListener(contextMenuProperty,new ChangeListener<ContextMenu>() {
            @Override
            public void changed(ObservableValue<? extends ContextMenu> observable, ContextMenu oldValue, ContextMenu newValue) {
                if (contextHandler != null) {
                    content.removeEventHandler(MouseEvent.MOUSE_CLICKED, contextHandler);
                }
                if (newValue != null) {
                    contextHandler = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (newValue == null) {
                                return;
                            }
                            if (event.getButton().equals(MouseButton.SECONDARY)) {
                                newValue.show(content, event.getScreenX(), event.getScreenY());
                            }
                        }
                    };
                    content.addEventHandler(MouseEvent.MOUSE_CLICKED, contextHandler);
                }
            }
        });
    }

    /**
     * sets content in the content area, replaces old one
     *
     * @param newContent new content that should replace old one
     */
    public void setContent(Parent newContent) {
        if (content != null) {
            content.getChildren().clear();
        }
        if (newContent != null) {
            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            if (content != null) {
                content.getChildren().add(newContent);
            }
        }
    }

    /**
     * @return get current content, null if content is empty! get always first
     * child of underlaying anchorpane
     */
    public Parent getContent() {
        if (content.getChildren().isEmpty()) {
            return null;
        }
        return (Parent) content.getChildren().get(0);
    }

    /**
     * set contextMenu, shown when right button is clicked in contentPane
     *
     * @param menu menu to show
     */
    public void setContextMenu(ContextMenu menu) {
        contextMenuProperty.setValue(menu);
    }

    /**
     * adds menu items in the menu, could be all based on node, preferable are
     * buttons
     *
     * @param elements elements to add
     */
    public void setMenu(Node... elements) {
        if (isHeaderSection()) {
            headerArea.addMenuItems(elements);
        }
    }
    public void addMenuItem(int pIndex, Node... pElements){
        if(isHeaderSection()){
            headerArea.addMenuItems(pIndex,pElements);
        }
    }
    /**
     * clears the content in the menu area
     */
    public void clearMenu() {
        if (isHeaderSection()) {
            headerArea.clearMenuItems();
        }
    }

    /**
     * sets the title in the section
     *
     * @param title String title to be set
     */
    public void setTitle(String title) {
        if (isHeaderSection()) {
            headerArea.setTitle(title);
        }

    }

    /**
     * loads glyph from the fontAwesome by its identifier
     *
     * @param glyph identifier of the glyph to load
     * @return glyph object from the font
     */
    public Glyph getGlyph(String glyph) {
        return ResourceLoader.getGlyph(glyph);
    }

    /**
     * try to extract window from Root
     *
     * @return Window object
     */
    public Window getWindow() {
        Scene scene = this.getRoot().getScene();
        return scene == null ? null : scene.getWindow();
    }

    public void addToSearchBox(Node... nodes) {  // to add multiple nodes
        if (isHeaderSection()) {
            headerArea.addSearchItems(nodes);
        }
    }

    public ObjectProperty<ContextMenu> getContextMenuProperty() {
        return contextMenuProperty;
    }

    public final BooleanProperty getHeaderProperty() {
        return headerProperty;
    }

    /**
     * indicates if section controls are armed
     *
     * @return indicator if controls are armed
     */
    public boolean isHeaderSection() {
        return headerProperty.getValue();
    }

    /**
     * set the armed value in property
     *
     * @param pArmed set the armed value to given value
     */
    public void setHeaderProperty(boolean pArmed) {
        headerProperty.setValue(pArmed);
    }
}

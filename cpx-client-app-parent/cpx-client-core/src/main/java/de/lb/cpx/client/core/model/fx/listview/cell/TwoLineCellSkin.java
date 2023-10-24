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

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.skin.ListCellSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Skin Class of the list cell, provides layout for two lines with icon area
 *
 * @author wilde
 * @param <T> content of the cell
 */
public class TwoLineCellSkin<T> extends ListCellSkin<T> {

    //property if description is showns
    private final BooleanProperty showDescriptionProperty = new SimpleBooleanProperty();

    //ui elements
    private final Label lblTitle = new Label();
    private final Label lblDesc = new Label();
    private final VBox boxLabel = new VBox(lblTitle);
    private final HBox root = new HBox(boxLabel);

    private Button iconButton;

    /**
     * contruct new instance
     *
     * @param control skinnable control
     */
    public TwoLineCellSkin(TwoLineCell<T> control) {
        super(control);
        root.setAlignment(Pos.CENTER_LEFT);
        root.paddingProperty().bind(control.rootPaddingProperty());
        HBox.setHgrow(boxLabel, Priority.ALWAYS);
        boxLabel.setFillWidth(true);
        //monitor changes of description property
        //update ui accordingly(adding,removing description label)
        showDescriptionProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (boxLabel.getChildren().contains(lblDesc)) {
                        return;
                    }
                    //always add in second place, other items will be moved
                    boxLabel.getChildren().add(1, lblDesc);
                    updateChildren();
                } else {
                    if (!boxLabel.getChildren().contains(lblDesc)) {
                        return;
                    }
                    boxLabel.getChildren().remove(lblDesc);
                    updateChildren();
                }
            }
        });
        //add binding, checks of description text is not null and not empty
        showDescriptionProperty.bind(Bindings
                .when(Bindings.and(control.descriptionProperty().isNotEmpty(), control.descriptionProperty().isNotNull()))
                .then(true)
                .otherwise(false));
        //checks if iconbutton is set and bind height to root height
        if (control.getIconButton() != null) {
            setIconButton(control.getIconButton());
            iconButton.prefHeightProperty().bind(root.heightProperty());
        }
        //checks if button instance changes
        control.iconButtonProperty().addListener(new ChangeListener<Button>() {
            @Override
            public void changed(ObservableValue<? extends Button> observable, Button oldValue, Button newValue) {
                setIconButton(newValue);
            }
        });
        //bind texts to labels
        boxLabel.alignmentProperty().bind(control.graphicAlignmentProperty());
        lblTitle.textProperty().bindBidirectional(control.titleProperty());
        lblDesc.textProperty().bind(control.descriptionProperty());

        //bind graphic content
        //of item is null or cell is empty place empty box
        //null is not allowed here
        control.graphicProperty().bind(Bindings.when(Bindings.or(control.itemProperty().isNotNull(), control.emptyProperty().not()))
                .then(root)
                .otherwise(new HBox()));
    }

    //set a new icon button 
    //removes the old instance and replace it with new button
    private void setIconButton(Button newValue) {
        if (newValue == null) {
            removeIconButton();
        } else {
            addIconButton(newValue);
        }
        iconButton = newValue;
    }

    /**
     * @param pContent containt to add under the currently setted labels
     */
    protected void addToContent(Node pContent) {
        if (pContent == null) {
            return;
        }
        if (!boxLabel.getChildren().contains(pContent)) {
            boxLabel.getChildren().add(pContent);
        }
    }

    /**
     * @param pContent node to remove from label content
     */
    protected void removeFromContent(Node pContent) {
        if (pContent == null) {
            return;
        }
        if (boxLabel.getChildren().contains(pContent)) {
            boxLabel.getChildren().remove(pContent);
        }
    }

    /**
     * @param newValue icon button to add
     */
    protected void addIconButton(Button newValue) {
        if(newValue == null){
            return;
        }
        if (root.getChildren().contains(newValue)) {
            root.getChildren().remove(newValue);
        }
        root.getChildren().add(0, newValue);
    }

    /**
     * remove currently set icon button
     */
    protected void removeIconButton() {
        if (root.getChildren().contains(iconButton)) {
            root.getChildren().remove(iconButton);
        }
    }
}

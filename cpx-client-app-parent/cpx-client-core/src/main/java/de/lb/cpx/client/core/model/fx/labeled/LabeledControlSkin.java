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
package de.lb.cpx.client.core.model.fx.labeled;

import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Implements the skin for a labeled control
 *
 * @author wilde
 * @param <T> control type
 */
public class LabeledControlSkin<T extends Control> extends SkinBase<LabeledControl<T>> {

    private final VBox rootBox;
//    private final SimpleStringProperty labelProperty = new SimpleStringProperty("");
    private PseudoClass cssStyleClass = PseudoClass.getPseudoClass("highlighted");
    private final Label labelTitle;
    private final Label infoTitle;
    private final HBox contentBox;

    private StringProperty infoProperty;
    private final HBox menuBox;

    public StringProperty infoProperty() {
        if (infoProperty == null) {
            infoProperty = new SimpleStringProperty();
        }
        return infoProperty;
    }

    /**
     * contruct new skin, skin definies the layout of the control
     *
     * @param pLabel label text
     * @param pControl label control Object
     */
    public LabeledControlSkin(String pLabel, LabeledControl<T> pControl) {
        this(pControl);
        pControl.setTitle(pLabel);
    }

    public LabeledControlSkin(String pLabel, LabeledControl<T> pControl, FontWeight pWeight) {
        this(pLabel, pControl);
        applyFontWeightToTitle(pWeight);
    }

    /**
     * construct new Sking, skin definies the layout of the control
     *
     * @param pControl labelControl Object
     */
    public LabeledControlSkin(LabeledControl<T> pControl) {
        super(pControl);
        pControl.setFocusTraversable(false);
        labelTitle = new Label();
        labelTitle.getStyleClass().add("labeled-control-label");
        labelTitle.setId("title");
        labelTitle.setMaxWidth(Double.MAX_VALUE);
//        labelTitle.setId("labeled-control-label");
        labelTitle.textProperty().bind(pControl.titleProperty());
        infoTitle = new Label();
        infoTitle.textProperty().bind(infoProperty());
        labelTitle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pControl.requestFocus();
            }
        });
        // sets the css style in the label if the control gain or lose focus
        pControl.getControl().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyHeaderFocusHighlighting(newValue);
            }
        });
        contentBox = new HBox(pControl.getControl());
        contentBox.setFillHeight(true);
        contentBox.alignmentProperty().bind(pControl.contentAlignment());//setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(contentBox, Priority.ALWAYS);
        VBox.setVgrow(contentBox, Priority.ALWAYS);
        
        menuBox = new HBox();
        Bindings.bindContent(menuBox.getChildren(), getSkinnable().getMenuItems());
        
        GridPane grid = new GridPane();
        grid.addRow(0, labelTitle, infoTitle, menuBox);
        grid.getColumnConstraints().add(0, new ColumnConstraints());
        grid.getColumnConstraints().add(1, new ColumnConstraints());
        grid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().get(0).setHalignment(HPos.LEFT);
        grid.getColumnConstraints().get(1).setHalignment(HPos.RIGHT);
        rootBox = new VBox(grid, contentBox);
        rootBox.setFillWidth(true);
        HBox.setHgrow(pControl.getControl(), Priority.ALWAYS);
        VBox.setVgrow(pControl.getControl(), Priority.ALWAYS);
        pControl.getControl().setMaxWidth(Double.MAX_VALUE);
        rootBox.setAlignment(Pos.TOP_LEFT);
        rootBox.spacingProperty().bindBidirectional(pControl.spacingProperty());
        getChildren().add(rootBox);

        pControl.infoPositionProperty().addListener(new ChangeListener<VPos>() {
            @Override
            public void changed(ObservableValue<? extends VPos> observable, VPos oldValue, VPos newValue) {
                if (newValue == null) {
                    return;
                }
                rootBox.getChildren().clear();
                switch (newValue) {
                    case CENTER:
                    case TOP:
                        rootBox.getChildren().addAll(grid, contentBox);
                        break;
                    case BASELINE:
                    case BOTTOM:
                        rootBox.getChildren().addAll(contentBox, grid);
                        break;
                }
            }
        });
    }
    public void applyHeaderFocusHighlighting(Boolean newValue) {
        labelTitle.pseudoClassStateChanged(cssStyleClass, newValue);
        getNode().pseudoClassStateChanged(cssStyleClass, newValue);
    }
    public Pane getContent() {
        return contentBox;
    }
//    /**
//     * sets the labelText in the Label
//     *
//     * @param pLabel label to set
//     */
//    public void setLabel(String pLabel) {
//        labelProperty.setValue(pLabel);
//    }

    /**
     * get the root object of the layout
     *
     * @return root element of the layout
     */
    public Parent getRoot() {
        return rootBox;
    }

    /**
     * set Title TextFill
     *
     * @param color string color: white,red etc.
     */
    public void setTitleTextFill(String color) {
        labelTitle.setStyle("-fx-text-fill:" + color + ";");
    }

    /**
     * set a specific style class to the title
     *
     * @param pStyleClass style class
     */
    public void addStyleClassToTitle(String pStyleClass) {
        labelTitle.getStyleClass().remove("labeled-control-label");
        labelTitle.getStyleClass().add(pStyleClass);
    }

    /**
     * set the style class for the highlight effect on the title label
     *
     * @param pPseudoStyleClass pseudo class
     */
    public void setPseudoStyleClass(PseudoClass pPseudoStyleClass) {
        cssStyleClass = pPseudoStyleClass;
    }

    /**
     * sets the font to the title label
     *
     * @param pFont font to set
     */
    public void setFontToTitle(Font pFont) {
        labelTitle.setFont(pFont);
    }

    /**
     * apply the font weight, e.g. BOLD to the title
     *
     * @param pWeight weight to set if null, FontWeight.NORMAL is used
     */
    public final void applyFontWeightToTitle(FontWeight pWeight) {
        if (pWeight == null) {
            pWeight = FontWeight.NORMAL;
        }
        switch (pWeight) {
            case THIN:
                labelTitle.setStyle("-fx-font-weight: thin");
                break;
            case LIGHT:
                labelTitle.setStyle("-fx-font-weight: light");
                break;
            case NORMAL:
                labelTitle.setStyle("-fx-font-weight: normal");
                break;
            case BOLD:
                labelTitle.setStyle("-fx-font-weight: bold");
                break;
            default:
                labelTitle.setStyle("-fx-font-weight: normal");
                Logger.getLogger(getClass().getName()).fine("Unknown FontWeight set, use default");
        }
    }

    /**
     * sets a specific padding to the label
     *
     * @param insets insets used to apply the padding
     */
    public void setTitlePadding(Insets insets) {
        labelTitle.setPadding(insets);
    }

    /**
     * @param pTooltip tooltip to install on root
     */
    public void setTooltip(Tooltip pTooltip) {
        Tooltip.install(rootBox, pTooltip);
    }

    /**
     * @param pAlignment alignment of the root
     */
    public void setRootAlignment(Pos pAlignment) {
        rootBox.setAlignment(pAlignment);
    }

    /**
     * @param pAlignment alignment of content
     */
    public void setContentAlignment(Pos pAlignment) {
        contentBox.setAlignment(pAlignment);
    }

    public DoubleProperty contentPrefHeightProperty() {
        return contentBox.prefHeightProperty();
    }
}

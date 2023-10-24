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
package de.lb.cpx.client.core.model.fx.tooltip;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class BasicTooltip extends Tooltip {

    private final StringProperty titleProperty = new SimpleStringProperty("");
    private final ObjectProperty<Node> contentNodeProperty = new SimpleObjectProperty<>();
    private AnchorPane contentPane;

    /**
     * creates new Tooltip instance with default font size 15
     *
     * @param pOpenDelay open delay in ms
     * @param pVisibleDuration time before fading in ms
     * @param pCloseDelay close delay in ms
     * @param pHideOnExit hide on exit flag
     */
    public BasicTooltip(double pOpenDelay, double pVisibleDuration, double pCloseDelay, boolean pHideOnExit) {
        this("", pOpenDelay, pVisibleDuration, pCloseDelay, pHideOnExit);
    }

    /**
     * creates new Tooltip instance with default font size 15
     *
     * @param pTitle title
     * @param pOpenDelay open delay in ms
     * @param pVisibleDuration time before fading in ms
     * @param pCloseDelay close delay in ms
     * @param pHideOnExit hide on exit flag
     */
    public BasicTooltip(String pTitle, double pOpenDelay, double pVisibleDuration, double pCloseDelay, boolean pHideOnExit) {
//        super(pTitle, pOpenDelay, pVisibleDuration, pCloseDelay, pHideOnExit);
        super(pTitle);
        setShowDelay(new Duration(pOpenDelay));
        setShowDuration(new Duration(pVisibleDuration));
        setHideDelay(new Duration(pCloseDelay));
        setHideOnEscape(pHideOnExit);
        createLayout();
        contentNodeProperty.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                contentPane.getChildren().clear();
                if (newValue != null) {
                    AnchorPane.setTopAnchor(newValue, 0.0);
                    AnchorPane.setLeftAnchor(newValue, 0.0);
                    AnchorPane.setBottomAnchor(newValue, 0.0);
                    AnchorPane.setRightAnchor(newValue, 0.0);
                    contentPane.getChildren().add(newValue);
                }
            }
        });
    }

    /**
     * creates default tooltip for details with default font size 15 open delay
     * 200ms visibleDuration 5000ms closeDelay 100ms hideOnExit true
     *
     * @param pTitle title to show
     * @param pContent content
     */
    public BasicTooltip(String pTitle, String pContent) {
        this(pTitle, pContent, FontPosture.REGULAR);
    }
    /**
     * creates default tooltip for details with default font size 15 open delay
     * 200ms visibleDuration 5000ms closeDelay 100ms hideOnExit true
     *
     * @param pTitle title to show
     * @param pContent content
     * @param pContentPosture posture for content
     */
    public BasicTooltip(String pTitle, String pContent,FontPosture pContentPosture) {
         this(200, Double.POSITIVE_INFINITY, 100, true);
        titleProperty.set(pTitle);
        Text label = new Text(pContent);
        label.setFont(Font.font(label.getFont().getFamily(), pContentPosture, label.getFont().getSize()));
        label.wrappingWidthProperty().bind(contentPane.widthProperty());//prefWidthProperty());
//        label.setWrapText(true);
        setContentNode(label);
    }
    public BasicTooltip(String pTitle, String pContent,Font pContentFont) {
        this(200, Double.POSITIVE_INFINITY, 100, true);
        titleProperty.set(pTitle);
        Text label = new Text(pContent);
        label.setFont(pContentFont);
        label.wrappingWidthProperty().bind(contentPane.widthProperty());//prefWidthProperty());
//        label.setWrapText(true);
        setContentNode(label);
    }
    public BasicTooltip(String pTitle, String pContent, String pDetails) {
        this(200, Double.POSITIVE_INFINITY, 100, true);
        titleProperty.set(pTitle);
        Text label = new Text(pContent);
        label.wrappingWidthProperty().bind(contentPane.widthProperty());//prefWidthProperty());
//        label.setWrapText(true);
        VBox box = new VBox(5, label);
        if (pDetails != null && !pDetails.isEmpty()) {
            Text details = new Text(pDetails);
            details.setFont(Font.font(details.getFont().getFamily(), FontPosture.ITALIC, details.getFont().getSize()));
            details.wrappingWidthProperty().bind(contentPane.widthProperty());//prefWidthProperty());
            box.getChildren().add(details);
        }
        setContentNode(box);
    }

    public BasicTooltip() {
        super(null);
    }

    public final BasicTooltip setFontSize(int pFontSize) {
        setFont(new Font(pFontSize));
        return this;
    }

    public void setTitle(String pTitle) {
        titleProperty.set(pTitle);
    }

    public String getTitle() {
        return titleProperty.get();
    }

    //TODO Should be some skin class?
    private void createLayout() {
        getStyleClass().add("basic-tooltip");
        Label title = new Label();
        title.getStyleClass().add("title-label");
        title.textProperty().bind(titleProperty);

        contentPane = new AnchorPane();
        contentPane.prefWidthProperty().bind(prefWidthProperty());
        contentPane.setMinWidth(AnchorPane.USE_PREF_SIZE);
        contentPane.setMaxWidth(AnchorPane.USE_PREF_SIZE);
        VBox root = new VBox(title, contentPane);
        root.setSpacing(10);
        root.setFillWidth(true);
        setGraphic(root);
    }

    public final void setContentNode(Node pNode) {
        contentNodeProperty.set(pNode);
    }

    public Node getContentNode() {
        return contentNodeProperty.get();
    }

    @Override
    protected void show() {
        String css = getClass().getResource("/styles/cpx-default.css").toExternalForm();
        if (!getScene().getStylesheets().contains(css)) {
            getScene().getStylesheets().add(0, css);
        }
        super.show(); //To change body of generated methods, choose Tools | Templates.
    }

}

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
package de.lb.cpx.client.core.model.fx.masterdetail;

import de.lb.cpx.client.core.BasicMainApp;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author wilde TODO:make skin to handle ui part
 */
public abstract class MasterDetailSplitPane extends SplitPane {

    private static final Logger LOG = Logger.getLogger(MasterDetailSplitPane.class.getName());

    private AnchorPane detail;
    protected AnchorPane master;
    private DoubleProperty dividerDefaultPositionProperty = new SimpleDoubleProperty(0.5);

    /**
     * construct new MasterdetailPane, detailPane is placed on the right side
     */
    public MasterDetailSplitPane() {
        this(DetailOrientation.RIGHT);
    }

    /**
     * construct noew MasterdetailPane, detailPane is moved to given location
     *
     * @param oriantation orientation whre detailPane should be placed
     */
    public MasterDetailSplitPane(DetailOrientation oriantation) {
        initMasterPane();
//        getChildren().add(master);
        initDetailPane(oriantation);
        getStyleClass().add("preview-split-pane");

        //2018-08-17 DNi: Use WeakChangeListener instead of ChangeListener. Otherwise memory is not freed when process is closed
        BasicMainApp.getStage().widthProperty().addListener(new WeakChangeListener<>(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setDividerPositions(dividerDefaultPositionProperty.getValue());
            }
        }));
        dividerDefaultPositionProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setDividerPositions(newValue.doubleValue());
            }
        });

        // Observe showDetailProperty to enable/disable entire details Anchorpane.
        showDetailProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (!getItems().contains(detail)) {
                        LOG.info("add detailPane");
                        initDetailPane(oriantation);
                    }
                } else {
                    LOG.info("remove detailPane");
                    getItems().remove(detail);
                }
            }
        });

    }

    private void initDetailPane(DetailOrientation orientation) {
        detail = new AnchorPane();
//        detail.setStyle("-fx-background-color:#eae5e5");
        AnchorPane.setTopAnchor(detail, 0.0);
        AnchorPane.setRightAnchor(detail, 0.0);
        AnchorPane.setLeftAnchor(detail, 0.0);
        AnchorPane.setBottomAnchor(detail, 0.0);
        setDetail(new HBox());

        setOrientation(orientation);
    }

    /**
     * sets the new orientation, Todo: check the function(redrawing)
     *
     * @param orientation new location where details should be placed
     */
    public void setOrientation(DetailOrientation orientation) {
        switch (orientation) {
            case BOTTOM:
                setBottom(detail);
                break;
            case LEFT:
                setLeft(detail);
                break;
            case RIGHT:
                setRight(detail);
                break;
            case TOP:
                setTop(detail);
                break;
        }
    }

    private void initMasterPane() {
        master = new AnchorPane();
        AnchorPane.setTopAnchor(master, 0.0);
        AnchorPane.setRightAnchor(master, 0.0);
        AnchorPane.setLeftAnchor(master, 0.0);
        AnchorPane.setBottomAnchor(master, 0.0);
        setMaster(new HBox());
    }

    /**
     * sets new MasterPane in the Layout, clears center
     *
     * @param masterPane new MasterPane
     */
    protected void setMaster(Parent masterPane) {
        master.getChildren().clear();
        AnchorPane.setTopAnchor(masterPane, 0.0);
        AnchorPane.setRightAnchor(masterPane, 0.0);
        AnchorPane.setLeftAnchor(masterPane, 0.0);
        AnchorPane.setBottomAnchor(masterPane, 0.0);
        master.getChildren().add(masterPane);
    }

    /**
     * get current pane set as master
     *
     * @return current pane in master
     */
    public Node getCurrentMaster() {
        return master.getChildren().get(0);
    }

    /**
     * get current pane set as detail
     *
     * @return current pane in detail
     */
    public Node getCurrentDetail() {
        return detail.getChildren().get(0);
    }

    /**
     * sets new DetailPane, detailPane area is cleared
     *
     * @param detailPane new DetailPane
     */
    protected void setDetail(Parent detailPane) {
        if (detailPane == null) {
            LOG.warning("Can not set DetailPane, given Pane is null! Do nothing instead!");
            return;
        }
        detail.getChildren().clear();
        AnchorPane.setTopAnchor(detailPane, 0.0);
        AnchorPane.setRightAnchor(detailPane, 0.0);
        AnchorPane.setLeftAnchor(detailPane, 0.0);
        AnchorPane.setBottomAnchor(detailPane, 0.0);
        detail.getChildren().add(detailPane);
    }
    protected void setDetailAsync(Parent detailPane){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                setDetail(detailPane);
            }
        });
    }
    /**
     * add new Node to the MasterPane
     *
     * @param node node to add
     */
    public void addToMaster(Node node) {
        master.getChildren().add(node);
    }

    /**
     * add new Node to the DetailPane
     *
     * @param node node to add
     */
    public void addToDetail(Node node) {
        detail.getChildren().add(node);
    }

    /**
     * get MasterPane
     *
     * @return Pane currently used as Master
     */
    public Pane getMasterPane() {
        return master;
    }

    /**
     * get DetailPane
     *
     * @return Pane currently used as Detail
     */
    public Pane getDetailPane() {
        return detail;
    }

    /**
     * get Master as Node
     *
     * @return Node currently used as Master
     */
    public Node getMaster() {
        return master;
    }

    /**
     * @return node stored in master, if empty null is returned, always returns
     * first item if multiply are stored
     */
    public Node getMasterNode() {
        if (master.getChildren().isEmpty()) {
            return null;
        }
        return master.getChildren().get(0);
    }

    /**
     * get Detail as Node
     *
     * @return Node currently used as Detail
     */
    public Node getDetail() {
        return detail;
    }

    /**
     * @return node stored in detail, if empty null is returned, always returns
     * first item if multiply are stored
     */
    public Node getDetailNode() {
        if (detail.getChildren().isEmpty()) {
            return null;
        }
        return detail.getChildren().get(0);
    }

    private void setBottom(AnchorPane pDetail) {
        getItems().clear();
        super.setOrientation(Orientation.VERTICAL);
//        getChildren().addAll(master,pDetail);
        getItems().addAll(master, pDetail);
    }

    private void setLeft(AnchorPane pDetail) {
        getItems().clear();
        super.setOrientation(Orientation.HORIZONTAL);
        getItems().addAll(pDetail, master);
    }

    private void setRight(AnchorPane pDetail) {
        getItems().clear();
        super.setOrientation(Orientation.HORIZONTAL);
        getItems().addAll(master, pDetail);
    }

    private void setTop(AnchorPane pDetail) {
        getItems().clear();
        super.setOrientation(Orientation.VERTICAL);
        getItems().addAll(pDetail, master);
    }

    /**
     * set the default position of the divider in the splitpane
     *
     * @param pDefaultPosition devider gefault position
     */
    public final void setDividerDefaultPosition(double pDefaultPosition) {
        dividerDefaultPositionProperty.setValue(pDefaultPosition);
    }

    private BooleanProperty showDetailProperty;

    public BooleanProperty showDetailProperty() {
        if (showDetailProperty == null) {
            showDetailProperty = new SimpleBooleanProperty(true); // by default "true"
        }
        return showDetailProperty;
    }

    public boolean isShowDetail() {
        return showDetailProperty().get();
    }

    public void setShowDetail(Boolean pShow) {
        showDetailProperty().set(pShow);
    }

    /**
     * Enum to specefiy the orientation of the DetailPane
     */
    public enum DetailOrientation {
        LEFT, RIGHT, TOP, BOTTOM
    }

}

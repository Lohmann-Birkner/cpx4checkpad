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
package de.lb.cpx.client.core.model.fx.masterdetail;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Implements Master-DetailPane, based on a BorderPane, Detail can be moved to
 * Top,Right,Left or botton of the Pane, default is right
 *
 * @author wilde
 */
@Deprecated(since = "1.05")
public class MasterDetailBorderPane extends BorderPane {

    private AnchorPane detail;
    private AnchorPane master;

    /**
     * construct new MasterdetailPane, detailPane is placed on the right side
     */
    public MasterDetailBorderPane() {
        super();
        setDetailPane(DetailOrientation.RIGHT);
        setMasterPane();
        getDetailPane().prefWidthProperty().bind(this.widthProperty().divide(masterDetailRatioProperty()));
    }
    private DoubleProperty masterDetailRatioProperty;

    public final DoubleProperty masterDetailRatioProperty() {
        if (masterDetailRatioProperty == null) {
            masterDetailRatioProperty = new SimpleDoubleProperty(1.3);
        }
        return masterDetailRatioProperty;
    }

    public void setMasterDetailRatio(Double pRatio) {
        masterDetailRatioProperty().set(pRatio);
    }

    public Double getMasterDetailRatio() {
        return masterDetailRatioProperty().get();
    }

    /**
     * construct now MasterdetailPane, detailPane is moved to given location
     *
     * @param oriantation orientation where detailPane should be placed
     */
    public MasterDetailBorderPane(DetailOrientation oriantation) {
        this();
        setDetailPane(oriantation);
//        setMasterPane();
    }

    private void setDetailPane(DetailOrientation orientation) {
        detail = new AnchorPane();
//        detail.setStyle("-fx-background-color:#eae5e5");
        AnchorPane.setTopAnchor(detail, 0.0);
        AnchorPane.setRightAnchor(detail, 0.0);
        AnchorPane.setLeftAnchor(detail, 0.0);
        AnchorPane.setBottomAnchor(detail, 0.0);
        setOrientation(orientation);
    }

    /**
     * sets the new orientation, Todo: check the function(redrawing)
     *
     * @param orientation new location where details should be placed
     */
    public void setOrientation(DetailOrientation orientation) {
        if (getChildren().contains(detail)) {
            getChildren().remove(detail);
        }
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

    private void setMasterPane() {
        master = new AnchorPane();
        AnchorPane.setTopAnchor(master, 0.0);
        AnchorPane.setRightAnchor(master, 0.0);
        AnchorPane.setLeftAnchor(master, 0.0);
        AnchorPane.setBottomAnchor(master, 0.0);
        setCenter(master);
    }

    /**
     * sets new MasterPane in the Layout, clears center
     *
     * @param masterPane new MasterPane
     */
    protected final void setMaster(Node masterPane) {
        master.getChildren().clear();
        AnchorPane.setTopAnchor(masterPane, 0.0);
        AnchorPane.setRightAnchor(masterPane, 0.0);
        AnchorPane.setLeftAnchor(masterPane, 0.0);
        AnchorPane.setBottomAnchor(masterPane, 0.0);
        master.getChildren().add(masterPane);
    }

    /**
     * sets new DetailPane, detailPane area is cleared
     *
     * @param detailPane new DetailPane
     */
    protected void setDetail(Parent detailPane) {
        detail.getChildren().clear();
        AnchorPane.setTopAnchor(detailPane, 0.0);
        AnchorPane.setRightAnchor(detailPane, 0.0);
        AnchorPane.setLeftAnchor(detailPane, 0.0);
        AnchorPane.setBottomAnchor(detailPane, 0.0);
        detail.getChildren().add(detailPane);
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
    public final Pane getDetailPane() {
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
     * get Detail as Node
     *
     * @return Node currently used as Detail
     */
    public Node getDetail() {
        if (!getDetailPane().getChildren().isEmpty()) {
            return getDetailPane().getChildren().get(0);
        }
        return null;
    }

    /**
     * Enum to specefiy the orientation of the DetailPane
     */
    public enum DetailOrientation {
        LEFT, RIGHT, TOP, BOTTOM
    }

}

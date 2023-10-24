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
package de.lb.cpx.client.core.model.fx.comparablepane;

import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Iterator;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Comparable Skin class to handle ui calls
 *
 * @author wilde
 * @param <Z> type of base region gridpane,tableview .. etc
 * @param <E> type
 */
public class ComparablePaneSkin<Z extends Region, E extends ComparableContent<? extends AbstractEntity>> implements Skin<ComparablePane<Z, E>> {

    private final ComparablePane<Z, E> skinnable;

    //root content structure
    private final VBox root = new VBox();
    private final AnchorPane node = new AnchorPane();
    private HBox content = new HBox();
    private final HBox versions = new HBox();
    private final HBox wrapper = new HBox();
    private final HBox baseVerContainer = new HBox();
    private final HBox addVerContainer = new HBox();
    private final AnchorPane apInfoRegion = new AnchorPane();

    //scrollbars
    private ScrollBar hScrollBar;
    private ScrollBar vScrollBar;
    private HBox hbvScrollBar = new HBox();
    private HBox hbhScrollBar = new HBox();

    //scrollpanes
    private ScrollPane scrollPane = new ScrollPane();
    private ScrollPane spContent = new ScrollPane();

    private ItemContainer<Z, E> infoContainer;
    private HBox menuCtrlArea = new HBox();
    private final ChangeListener<Number> vBarVisibleAmountListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            if (Double.doubleToRawLongBits(newValue.doubleValue()) == Double.doubleToRawLongBits(1.0d)) {
                hbvScrollBar.getStyleClass().add("remove-all-scroll-bars");
            } else {
                hbvScrollBar.getStyleClass().remove("remove-all-scroll-bars");
            }
        }
    };
    private final ChangeListener<Number> hBarVisibleAmountListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            if (Double.doubleToRawLongBits(newValue.doubleValue()) == Double.doubleToRawLongBits(1.0d)) {
                hbhScrollBar.getStyleClass().add("remove-all-scroll-bars");
                hScrollBar.setVisible(false);
            } else {
                hbhScrollBar.getStyleClass().remove("remove-all-scroll-bars");
                hScrollBar.setVisible(true);
            }
        }
    };
    private final ChangeListener<Skin<?>> scrollPaneSkinListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {

            for (Node n : scrollPane.lookupAll(".scroll-bar")) {
                if (n instanceof ScrollBar) {
                    ScrollBar bar = (ScrollBar) n;
                    if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                        bar.setDisable(true);
                        getVScrollBar().valueProperty().bindBidirectional(bar.valueProperty());
                        getVScrollBar().minProperty().bindBidirectional(bar.minProperty());
                        getVScrollBar().maxProperty().bindBidirectional(bar.maxProperty());
                    }
                }
            }
        }
    };
    private final ChangeListener<Skin<?>> spContentSkinListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {

            for (Node n : scrollPane.lookupAll(".scroll-bar")) {
                if (n instanceof ScrollBar) {
                    ScrollBar bar = (ScrollBar) n;
                    if (bar.getOrientation().equals(Orientation.HORIZONTAL)) {
                        bar.setDisable(true);
                        getHScrollBar().valueProperty().bindBidirectional(bar.valueProperty());
                        getHScrollBar().minProperty().bindBidirectional(bar.minProperty());
                        getHScrollBar().maxProperty().bindBidirectional(bar.maxProperty());
//                        getHScrollBar().visibleAmountProperty().bindBidirectional(bar.visibleAmountProperty());
//not really cool workaround, for flickering scrollbar
//scrollbar stays after scrollcontent gets to small after window size is changed
//there should be some computation to hide in that case??
//flickering is due to bindBidirectional of both visible amount sizes
                        bar.visibleAmountProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                                LOG.info("Change vis amount to " + newValue);
                                getHScrollBar().setVisibleAmount(newValue.doubleValue());
                            }
                        });
                    }
                }
            }
        }
    };
    private final ListChangeListener<Node> versionListener = new ListChangeListener<>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Node> c) {
            if (c.getList().isEmpty()) {
                if (content.getChildren().contains(spContent)) {
                    content.getChildren().remove(spContent);
                }
            } else {
                if (!content.getChildren().contains(spContent)) {
                    content.getChildren().add(spContent);
                }
            }
        }
    };
    private final ChangeListener<Z> infoRegionListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Z> observable, Z oldValue, Z newValue) {
//                removeFromInfoPane(oldValue);
            clearInfoPane();
            infoContainer = new ItemContainer<>(null, null, newValue, ComparablePaneSkin.this);
            addInInfoPane(infoContainer);
        }

    };
    private final ChangeListener<Boolean> showMenuListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            menuCtrlArea.setVisible(newValue);
            hbvScrollBar.setPadding(new Insets(newValue ? getSkinnable().getPrefHeaderHeight() : 0, 0, 0, 0));
            if (infoContainer != null) {
                infoContainer.setShowMenu(newValue);
            }
            Iterator<E> it = getSkinnable().getTableViewToVersion().keySet().iterator();
            while (it.hasNext()) {
                ComparableContent<? extends AbstractEntity> next = it.next();
                getSkinnable().getTableViewToVersion().get(next).setShowMenu(newValue);
            }
        }

    };
    private final ChangeListener<Number> widthListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            layoutRoot();
        }
    };
    private final ListChangeListener<E> versionListListener = (ListChangeListener.Change<? extends E> c) -> {
        while (c.next()) {
            if (c.wasAdded()) {
//                        setMapForItems(c.getAddedSubList());
                for (E obj : c.getAddedSubList()) {
                    Z tv = getSkinnable().createNewVersionTableView(obj);
                    ItemContainer<Z, E> container = new ItemContainer<>(obj, getSkinnable().getVersionCtrlFactory().call(obj), tv, ComparablePaneSkin.this);
                    container.setShowMenu(getSkinnable().getShowMenu());
                    getSkinnable().getTableViewToVersion().put(obj, container);
//                    if (tv instanceof AsyncTableView) {
//                        ((AsyncTableView) tv).reload();
//                    }
                }

            }
            if (c.wasRemoved()) {
                for (E obj : c.getRemoved()) {
                    ItemContainer<Z, E> item = getSkinnable().getTableViewToVersion().get(obj);
                    getSkinnable().getTableViewToVersion().remove(obj);
                    if (item.getRegion() instanceof AsyncTableView) {
                        ((AsyncTableView) item.getRegion()).getItems().clear();
                        ((AsyncTableView) item.getRegion()).getColumns().clear();
                        ((AsyncTableView) item.getRegion()).dispose();
                    }
                    item.dispose();
                    obj.destroy();
                    obj = null;

                }
            }
        }
    };
    private final MapChangeListener<E, ItemContainer<Z, E>> DEFAULT_TABLE_TO_VERSION_LISTENER = new MapChangeListener<>() {
        @Override
        public void onChanged(MapChangeListener.Change<? extends E, ? extends ItemContainer<Z, E>> change) {
            if (change.wasAdded()) {
                if (change.getMap().keySet().size() > 1) {
                    if (change.getValueAdded().getContent().isEditable()) {
                        addInAddVersionPane(change.getValueAdded());
                    } else {
                        addInBaseVersionPane(change.getValueAdded());
                    }
                } else {
                    addInBaseVersionPane(change.getValueAdded());
                }

            }
            if (change.wasRemoved()) {
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
                        if (change.getMap().keySet().size() >= 1) {
                            removeFromAddVersionPane(change.getValueRemoved());
                        } else {
                            removeFromBaseVersionPane(change.getValueRemoved());
                        }
//                    }
//                });
            }
        }
    };
    private MapChangeListener<E, ItemContainer<Z, E>> tableToVersionListener = DEFAULT_TABLE_TO_VERSION_LISTENER;
    public MapChangeListener<E, ItemContainer<Z, E>> getTableToVersionListener(){
        return tableToVersionListener;
    }
    public void setTableToVersionListener(MapChangeListener<E, ItemContainer<Z, E>> pListener){
        getSkinnable().getTableViewToVersion().removeListener(tableToVersionListener);
        tableToVersionListener = Objects.requireNonNullElse(pListener, DEFAULT_TABLE_TO_VERSION_LISTENER);
        getSkinnable().getTableViewToVersion().addListener(tableToVersionListener);
    }
    private final ChangeListener<Button> menuButtonListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Button> observable, Button oldValue, Button newValue) {
            removeMenuButton();
            addMenuButton(newValue);
        }
    };

    public ComparablePaneSkin(ComparablePane<Z, E> pControl) {
        skinnable = pControl;
        setUpLayout();
        setUpListeners();

    }

    @Override
    public ComparablePane<Z, E> getSkinnable() {
        return skinnable;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void dispose() {
        node.getChildren().clear();
        vScrollBar.visibleAmountProperty().removeListener(vBarVisibleAmountListener);
        hScrollBar.visibleAmountProperty().removeListener(hBarVisibleAmountListener);
        scrollPane.getStyleClass().clear();
        scrollPane.skinProperty().removeListener(scrollPaneSkinListener);
        spContent.getStyleClass().clear();
        spContent.skinProperty().removeListener(spContentSkinListener);
        addVerContainer.getChildren().removeListener(versionListener);
        getSkinnable().infoRegionProperty().removeListener(infoRegionListener);
        getSkinnable().showMenuProperty().removeListener(showMenuListener);
        apInfoRegion.minWidthProperty().unbind();
        root.widthProperty().removeListener(widthListener);
        getSkinnable().menuButtonProperty().removeListener(menuButtonListener);
        getSkinnable().getVersionList().removeListener(versionListListener);
        getSkinnable().getTableViewToVersion().removeListener(getTableToVersionListener());
    }

    public ReadOnlyDoubleProperty versionsWidthProperty() {
        return spContent.widthProperty();
    }

    public void removeScrollBar() {
        wrapper.getChildren().remove(hbvScrollBar);
    }

    public void addScrollBar() {
        wrapper.getChildren().add(hbvScrollBar);
    }

    /**
     * get the vertical scrollbar for the tableviews for bindings
     *
     * @return Scrollbar
     */
    public ScrollBar getVScrollBar() {
        return vScrollBar;
    }

    /**
     * get the horizontal scrollbar for the tableviews for bindings
     *
     * @return Scrollbar
     */
    public ScrollBar getHScrollBar() {
        return hScrollBar;
    }

    public void setMinWidthOfVBar(double pMinWidth) {
        hbvScrollBar.setMinWidth(pMinWidth);
    }

    public void addInInfoPane(ItemContainer<Z, E> pAdd) {
        if (!apInfoRegion.getChildren().contains(pAdd)) {
            AnchorPane.setTopAnchor(pAdd, 0.0);
            AnchorPane.setBottomAnchor(pAdd, 0.0);
            AnchorPane.setRightAnchor(pAdd, 0.0);
            AnchorPane.setLeftAnchor(pAdd, 0.0);
            apInfoRegion.getChildren().add(pAdd);
        }
    }

    public void removeFromInfoPane(ItemContainer<Z, E> pRemove) {
        if (apInfoRegion.getChildren().contains(pRemove)) {
            apInfoRegion.getChildren().remove(pRemove);
        }
    }

    public void clearInfoPane() {
        if (!apInfoRegion.getChildren().isEmpty()) {
            apInfoRegion.getChildren().clear();
        }
    }

    public void addInBaseVersionPane(ItemContainer<Z, E> pAdd) {
        if (!baseVerContainer.getChildren().contains(pAdd)) {
            baseVerContainer.getChildren().clear();
            baseVerContainer.getChildren().add(pAdd);
        }
    }

    public void removeFromBaseVersionPane(ItemContainer<Z, E> pRemove) {
        if (baseVerContainer.getChildren().contains(pRemove)) {
            baseVerContainer.getChildren().remove(pRemove);
        }
    }

    public void addInAddVersionPane(ItemContainer<Z, E> pAdd) {
        if (!addVerContainer.getChildren().contains(pAdd)) {
            addVerContainer.getChildren().add(pAdd);
        }
    }

    public void removeFromAddVersionPane(ItemContainer<Z, E> pRemove) {
        if (addVerContainer.getChildren().contains(pRemove)) {
            addVerContainer.getChildren().remove(pRemove);
        }
    }

    private void setUpListeners() {
        vScrollBar.visibleAmountProperty().addListener(vBarVisibleAmountListener);
        getHScrollBar().visibleAmountProperty().addListener(hBarVisibleAmountListener);
        scrollPane.getStyleClass().add("remove-all-scroll-bars");
        scrollPane.skinProperty().addListener(scrollPaneSkinListener);
        spContent.getStyleClass().add("remove-all-scroll-bars");
        spContent.skinProperty().addListener(spContentSkinListener);
        //workaround for the issue with persisting scrollpane width
        addVerContainer.getChildren().addListener(versionListener);
        getSkinnable().infoRegionProperty().addListener(infoRegionListener);
        menuCtrlArea.setVisible(getSkinnable().getShowMenu());
        getSkinnable().showMenuProperty().addListener(showMenuListener);
        apInfoRegion.minWidthProperty().bindBidirectional(getSkinnable().minInfoWidthProperty());

        //TODO: Workaround, sizes do not resize after windowsize gets changed by os to half of the screen
        //maybe bug? Otherwise make standard skin class that performs this methode automatically
        root.widthProperty().addListener(widthListener);
        getSkinnable().menuButtonProperty().addListener(menuButtonListener);
        getSkinnable().getVersionList().addListener(versionListListener);
        getSkinnable().getTableViewToVersion().addListener(getTableToVersionListener());
    }

    public void layoutRoot() {
        root.layout();
    }

    private void setUpLayout() {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        versions.setSpacing(10.0);

        vScrollBar = new ScrollBar();
        vScrollBar.setOrientation(Orientation.VERTICAL);
        hScrollBar = new ScrollBar();
        hScrollBar.setOrientation(Orientation.HORIZONTAL);

//        hbvScrollBar = new HBox();
        hbvScrollBar.setAlignment(Pos.CENTER_RIGHT);
        hbvScrollBar.setMinWidth(25);
        hbvScrollBar.getChildren().add(vScrollBar);

//        hbhScrollBar = new HBox();
        hbhScrollBar.setAlignment(Pos.CENTER_RIGHT);
        hbhScrollBar.setPadding(new Insets(0, 10, 0, 0));
        hbhScrollBar.getChildren().add(hScrollBar);

        addVerContainer.setSpacing(10.0);
        content.setSpacing(10.0);
//        //set table view with non version based informations
//        tvInfo = createInfoPane();

        spContent.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spContent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spContent.setFitToHeight(true);
        spContent.setFitToWidth(true);
        hScrollBar.minWidthProperty().bind(
                Bindings.when(spContent.widthProperty().greaterThan(0))
                        .then(spContent.widthProperty().add(15))
                        .otherwise(spContent.widthProperty())
        );
        spContent.setContent(addVerContainer);
        content.getChildren().addAll(baseVerContainer);
        HBox.setHgrow(apInfoRegion, Priority.ALWAYS);
//        apInfoRegion.setStyle("-fx-background-color:green");
        versions.getChildren().addAll(apInfoRegion, content);
        scrollPane.setContent(versions);
        wrapper.getChildren().addAll(scrollPane, hbvScrollBar);
        wrapper.setFillHeight(true);
        VBox.setVgrow(wrapper, Priority.ALWAYS);
        root.getChildren().addAll(wrapper, hbhScrollBar);

        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);

        AnchorPane.setTopAnchor(menuCtrlArea, 0.0);
        AnchorPane.setRightAnchor(menuCtrlArea, 0.0);
//        menuCtrlArea.setStyle("-fx-background-color:yellow");
        menuCtrlArea.prefHeightProperty().bind(getSkinnable().prefHeaderHeightProperty());
        menuCtrlArea.setAlignment(Pos.CENTER);
        menuCtrlArea.setMinWidth(27);
        node.getChildren().addAll(root, menuCtrlArea);
    }

    private void addMenuButton(Button pButton) {
        if (menuCtrlArea != null && pButton != null) {
            if (!menuCtrlArea.getChildren().contains(pButton)) {
                getSkinnable().showMenu();
                menuCtrlArea.getChildren().add(pButton);
//                pButton.disableProperty().bind(Bindings.equal(getSkinnable().getMaxDisplayedItems(),Bindings.size(getSkinnable().getVersionList())));
            }
        }
    }

    private void removeMenuButton() {
        getSkinnable().hideMenu();
        if (!menuCtrlArea.getChildren().isEmpty()) {
//            menuCtrlArea.getChildren().get(0).disableProperty().unbind();
            menuCtrlArea.getChildren().clear();
        }
    }

    protected ScrollPane getSpContent() {
        return spContent;
    }

}

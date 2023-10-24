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
package de.lb.cpx.client.core.model.fx.section;

import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Section header skin, handles all visual related access
 *
 * @author wilde
 */
public class SectionHeaderSkin extends SkinBase<SectionHeader> {

    private Label title = new Label();
    private HBox titleInfoBox = new HBox();
    private HBox menuBox = new HBox();
    private HBox searchItemsBox = new HBox();//freeSpaceBox);
    private ScrollPane spSearchItemsBox = new ScrollPane(searchItemsBox);
    private HBox searchButtonBox = new HBox();
    private HBox searchBox = new HBox(spSearchItemsBox, searchButtonBox);
    private HBox header = new HBox(title, searchBox, menuBox);
    private Separator seperator = new Separator(Orientation.HORIZONTAL);
    private VBox root = new VBox(header, seperator);

    /**
     * create new instance for skinnable
     *
     * @param pSkinnable skinnable to apply skin
     */
    public SectionHeaderSkin(SectionHeader pSkinnable) {
        super(pSkinnable);
        setUpHeaderArea();
        getChildren().addAll(root);
        root.setFillWidth(true);
        root.setMaxWidth(Double.MAX_VALUE);
        header.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                LOG.info("handleSearchBoxUpdate due to width change");
                handleSearchBoxUpdate();
            }
        });
        pSkinnable.getSearchItems().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> c) {
//                LOG.info("handleSearchBoxUpdate due to list change");
                handleSearchBoxUpdate();
            }
        });
    }

    private void setUpHeaderArea() {
        header.setAlignment(Pos.CENTER_LEFT);
        VBox.setVgrow(header, Priority.ALWAYS);

        title.getStyleClass().add("cpx-header-label");
        title.textProperty().bindBidirectional(getSkinnable().titleProperty());
        title.graphicProperty().bind(getSkinnable().titleGraphicProperty());
        title.contentDisplayProperty().bind(getSkinnable().titleContentDisplayProperty());
        title.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (t1.isEmpty()) {
                    title.setMinWidth(0);
                    if (header.getChildren().contains(title)) {
                        header.getChildren().remove(title);
                    }
                } else {
                    title.setMinWidth(70);
                    if (!header.getChildren().contains(title)) {
                        header.getChildren().add(title);
                    }
                }
            }
        });
        if (title.getText() == null || title.getText().isEmpty()) {
            header.getChildren().remove(title);
        }
        title.setPrefWidth(Label.USE_COMPUTED_SIZE);
//        title.setMaxWidth(Label.USE_PREF_SIZE);

        titleInfoBox.setAlignment(Pos.CENTER_RIGHT);
        titleInfoBox.setPadding(new Insets(0, 10, 0, 10));
        titleInfoBox.setSpacing(9.0);

        menuBox.setAlignment(Pos.CENTER_RIGHT);
        menuBox.setPadding(new Insets(0, 10, 0, 10));
        menuBox.setSpacing(9.0);
        menuBox.setMinWidth(70);
        menuBox.setMinWidth(Label.USE_PREF_SIZE);
        menuBox.setPrefWidth(Label.USE_COMPUTED_SIZE);
        menuBox.setMinHeight(36);
//        menuBox.setMaxWidth(Label.USE_PREF_SIZE);
        //bind to lists
        Bindings.bindContent(titleInfoBox.getChildren(), getSkinnable().getTitleInfoItems());

        Bindings.bindContent(menuBox.getChildren(), getSkinnable().getMenuItems());

        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.setSpacing(9);
        //bind to lists
        searchBox.setPadding(new Insets(0, 10, 0, 10));
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        searchItemsBox.setAlignment(Pos.CENTER_LEFT);
        searchItemsBox.setSpacing(9.0);

        searchButtonBox.setFillHeight(true);
        searchButtonBox.setAlignment(Pos.CENTER);
        spSearchItemsBox.setFitToHeight(true);
        spSearchItemsBox.setFitToWidth(true);
        spSearchItemsBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spSearchItemsBox.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        searchItemsBox.setAlignment(Pos.CENTER_LEFT);
        getSkinnable().getSearchItemsButton().setText("Weitere: 0");

        searchButtonBox.getChildren().add(getSkinnable().getSearchItemsButton());
        getSkinnable().getSearchItemsButton().setVisible(false);
        HBox.setHgrow(spSearchItemsBox, Priority.ALWAYS);

//        searchBox.getChildren().remove(searchButtonBox);
        searchBox.getChildren().clear();
    }

    private void handleSearchBoxUpdate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                computeSearchBoxContent();
            }
        });
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        computeSearchBoxContent();
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight); //To change body of generated methods, choose Tools | Templates.
//        LOG.info("layout children");
    }

    private SearchItemPopOver popOver;

    private void computeSearchBoxContent() {
//        LOG.info("compute header");
        searchItemsBox.getChildren().clear();
        List<Node> notFittingItems = new ArrayList<>();
        if (getSkinnable().getSearchItems().isEmpty()) {
            searchBox.getChildren().clear();
            return;
        }
        searchItemsBox.getChildren().addAll(getSkinnable().getSearchItems());
        if (searchBox.getChildren().isEmpty()) {
            searchBox.getChildren().addAll(spSearchItemsBox, searchButtonBox);
        }
//        if(!searchItemsBox.getChildren().isEmpty()){
//            if(searchBox.getChildren().isEmpty()){
//                searchBox.getChildren().addAll(spSearchItemsBox,searchButtonBox);
//            }
//        }else{
//            searchBox.getChildren().clear();
//        }
//        searchItemsBox.layout();
        searchBox.layout();
        double areaWidth = spSearchItemsBox.getWidth() - 50;
        double usedArea = 0.0;
        double areaSpacing = searchItemsBox.getSpacing();
        Iterator<Node> it = searchItemsBox.getChildren().iterator();
        while (it.hasNext()) {
            Node item = it.next();
            item.autosize();
            double itemWidth = item.getBoundsInParent().getWidth() + areaSpacing;
            usedArea = itemWidth + usedArea;
            if (areaWidth > usedArea) {//(itemWidth+areaSpacing)){
//                fittingItems.add(item);
            } else {
                it.remove();
                notFittingItems.add(item);
            }
        }
        Button btn = getSkinnable().getSearchItemsButton();
        btn.setText("Weitere: " + String.valueOf(notFittingItems.size()));
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (popOver == null) {
                    popOver = new SearchItemPopOver();
                }
                popOver.setItems(notFittingItems);
                if (!popOver.isShowing()) {
                    popOver.show(btn);
                } else {
                    popOver.hide();
                }
            }
        });
        if (popOver != null) {
            popOver.setItems(notFittingItems);
        }
        if (!notFittingItems.isEmpty()) {
            getSkinnable().getSearchItemsButton().setVisible(true);
        } else {
            getSkinnable().getSearchItemsButton().setVisible(false);
        }
    }

//    private void computeSearchBoxContent2() {
//        searchItemsBox.getChildren().clear();
//        if (getSkinnable().getSearchItems().isEmpty()) {
//            return;
//        }
//        ArrayList<Node> items = new ArrayList<>(getSkinnable().getSearchItems());
//        double areaWidth = spSearchItemsBox.getWidth() - 50;
//        double usedArea = 0.0;
//        double areaSpacing = searchItemsBox.getSpacing();
//        List<Node> fittingItems = new ArrayList<>();
//        List<Node> notFittingItems = new ArrayList<>();
//
//        for (Node item : items) {
//            double itemWidth = item.getBoundsInParent().getWidth() + areaSpacing;
//            usedArea = itemWidth + usedArea;
//            if (areaWidth > usedArea) {//(itemWidth+areaSpacing)){
//                fittingItems.add(item);
//            } else {
//                notFittingItems.add(item);
//            }
//        }
//        Button btn = getSkinnable().getSearchItemsButton();
//        btn.setText("Weitere: " + String.valueOf(notFittingItems.size()));
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                if (popOver == null) {
//                    popOver = new SearchItemPopOver();
//                }
//                popOver.setItems(notFittingItems);
//                if (!popOver.isShowing()) {
//                    popOver.show(btn);
//                } else {
//                    popOver.hide();
//                }
//            }
//        });
//        if (popOver != null) {
//            popOver.setItems(notFittingItems);
//        }
//        searchItemsBox.getChildren().setAll(fittingItems);
//        if (!notFittingItems.isEmpty()) {
//            getSkinnable().getSearchItemsButton().setVisible(true);
//        } else {
//            getSkinnable().getSearchItemsButton().setVisible(false);
//        }
//    }
    /**
     * Popover to be displayed, when items in menu have to be grouped due to
     * insufficient space
     */
    private class SearchItemPopOver extends AutoFitPopOver {

        private final VBox content;

        /**
         * creates item for popover
         *
         * @param pItems list of items to display
         */
        public SearchItemPopOver(List<Node> pItems) {
            this();
            setItems(pItems);
        }

        /**
         * creates new instance
         */
        public SearchItemPopOver() {
            super();
            setDetachable(false);
            content = new VBox();
            content.setPadding(new Insets(5));
            content.setSpacing(5);
            content.setAlignment(Pos.CENTER);
            setContentNode(content);
        }

        /**
         * @param pItems items to show in the content
         */
        public final void setItems(List<Node> pItems) {
            if (pItems.isEmpty() && isShowing()) {
                hide();
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    content.getChildren().setAll(pItems);
                }
            });
        }
    }
}

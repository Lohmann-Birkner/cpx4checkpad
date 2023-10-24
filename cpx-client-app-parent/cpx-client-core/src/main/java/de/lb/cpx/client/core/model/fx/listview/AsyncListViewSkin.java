/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.listview;

import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.ResourceLoader;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.layout.HBox;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * AsyncListView Skin extends ListViewSkin to get ListView specific properties
 * somewhat similar to AsyncTableViewSkin Maybe refactor to reduce double code
 *
 * @author wilde
 * @param <T> content of the list view
 */
public class AsyncListViewSkin<T> extends ListViewSkin<T> {

    private static final String DIVIDER = "/";
    private static final String LABEL_HEADER_STYLE_CLASS = "cpx-header-label-wo-padding";

    private final ReadOnlyDoubleWrapper topAreaHeight = new ReadOnlyDoubleWrapper(10);
    private final ReadOnlyBooleanWrapper reorderingProperty = new ReadOnlyBooleanWrapper(true);
    private SectionHeader menu;
    private HBox currentMaxMenu;
    private Label lblMaxSize;
    private Label lblCurrentSize;

    /**
     * create new Skin
     *
     * @param pSkinnable skinnable control
     */
    public AsyncListViewSkin(AsyncListView<T> pSkinnable) {
        super(pSkinnable);

        if (pSkinnable.getShowMenu()) {
            showMenu();
        }
        pSkinnable.showMenuProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    showMenu();
                } else {
                    hideMenu();
                }
                layoutChildren(getSkinnable().getLayoutX(), getSkinnable().getLayoutY(), getSkinnable().getWidth(), getSkinnable().getHeight());
            }
        });

        if (pSkinnable.getShowCurrentMaxMenu()) {
            showCurrentMaxMenu();
        }
        pSkinnable.showCurrentMaxMenuProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    showCurrentMaxMenu();
                } else {
                    hideCurrentMaxMenu();
                }
            }
        });
    }

    /**
     * @return height property of the top of the tableview (menu plus headerrow)
     */
    public ReadOnlyDoubleProperty topAreaHeight() {
        return topAreaHeight.getReadOnlyProperty();
    }

    /**
     * @param pReordering reordering in tableview
     */
    public void setReordering(boolean pReordering) {
        reorderingProperty.set(pReordering);
    }

    /**
     * @return if table is reorderable
     */
    public boolean getReordering() {
        return reorderingProperty.get();
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        if (menu != null) {
            //add label to layout, computation of position and height with label
            double labelHeight = menu.prefHeight(-1);
            double offset = 0;
            layoutInArea(menu, x, y, w, labelHeight, offset, HPos.LEFT, VPos.CENTER);
            y += labelHeight;
        }
        super.layoutChildren(x, y, w, h);
    }

    /*
    * PRIVATE METHODES
     */
    @SuppressWarnings("unchecked")
    private void showMenu() {
        menu = new SectionHeader();
        Button btnReload = new Button();
        btnReload.getStyleClass().add("cpx-icon-button");
        btnReload.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.REFRESH));
        btnReload.setOnAction((ActionEvent event) -> {
            ((AsyncListView) getSkinnable()).reload();
        });
        menu.addMenuItems(btnReload);
        menu.getMenuItems().addAll(((AsyncListView) getSkinnable()).getMenuItems());
        menu.titleProperty().bind(((AsyncListView) getSkinnable()).titleProperty());
        topAreaHeight.unbind();
        topAreaHeight.bind(menu.heightProperty());
        getChildren().add(menu);
    }

    private void hideMenu() {
        topAreaHeight.unbind();
        topAreaHeight.set(0);
        menu.titleProperty().unbind();
        getChildren().remove(menu);
        menu = null;
    }

    private void showCurrentMaxMenu() {
        if (menu == null) {
            return;
        }
        AsyncListView<T> skinable = (AsyncListView<T>) getSkinnable();
        lblCurrentSize = new Label();
        lblCurrentSize.getStyleClass().add(LABEL_HEADER_STYLE_CLASS);
        lblCurrentSize.textProperty().bind(skinable.currentCountProperty().asString());
        lblMaxSize = new Label();
        lblMaxSize.getStyleClass().add(LABEL_HEADER_STYLE_CLASS);
        lblMaxSize.textProperty().bind(skinable.maxCountProperty().asString());
        Label lblDivider = new Label(DIVIDER);
        lblDivider.getStyleClass().add(LABEL_HEADER_STYLE_CLASS);

        currentMaxMenu = new HBox(lblCurrentSize, lblDivider, lblMaxSize);
        menu.addTitleInfoItems(currentMaxMenu);
    }

    private void hideCurrentMaxMenu() {
        if (menu == null) {
            return;
        }
        lblCurrentSize.textProperty().unbind();
        lblMaxSize.textProperty().unbind();
        menu.getTitleInfoItems().remove(currentMaxMenu);
        currentMaxMenu = null;
        lblCurrentSize = null;
        lblMaxSize = null;
    }
}

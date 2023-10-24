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
package de.lb.cpx.client.core.model.fx.tableview;

import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.controlsfx.glyphfont.Glyph;

/**
 * Skin of the TableView added menu bar to normal tableview skin
 *
 * @author wilde
 * @param <T> objecttype contains in tableview
 */
public class AsyncTableViewSkin<T> extends TableViewSkin<T> {

    private static final Logger LOG = Logger.getLogger(AsyncTableViewSkin.class.getName());

    private static final String DIVIDER = "/";
    private static final String LABEL_HEADER_STYLE_CLASS = "cpx-header-label-wo-padding";
    private static final double CONTEXT_MENU_MOUSE_SPACING = 12.0;

    private final ReadOnlyDoubleWrapper menuAreaHeight = new ReadOnlyDoubleWrapper(10);
    private final ReadOnlyBooleanWrapper reorderingProperty = new ReadOnlyBooleanWrapper(true);
    protected SectionHeader menu;
    private HBox currentMaxMenu;
    private Label lblMaxSize;
    private Label lblCurrentSize;
    private Node dragIndictor;
    private Node dragOverlay;

    /**
     * @param skinable skinable
     */
    public AsyncTableViewSkin(AsyncTableView<T> skinable) {
        super(skinable);
        TableHeaderRow tableHeaderRow = (TableHeaderRow) this.getSkinnable().lookup("TableHeaderRow");
        tableHeaderRow.addEventFilter(MouseEvent.MOUSE_DRAGGED, (event) -> {
            if (!getReordering()) {
                event.consume();
            }
        });

        skinable.setRowFactory(new Callback<TableView<T>, TableRow<T>>() {
            @Override
            public TableRow<T> call(TableView<T> param) {
                TableRow<T> row = new TableRow<>();
                //set row click listener
                //binding contextmenu wil lresult in memory leak when columns are removed from table view

                row.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (skinable.getOnRowDragDetected() == null) {
                            return;
                        }
                        skinable.getOnRowDragDetected().handle(event);
                    }
                });
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (!skinable.isShowContextMenu()) {
                            return;
                        }
                        if (row.isEmpty()) {
                            return;
                        }
                        if (skinable.getOnRowClicked() != null) {
                            skinable.getOnRowClicked().handle(event);
                        }
                        if (skinable.getRowContextMenu() != null) {
                            if (MouseButton.SECONDARY.equals(event.getButton())) {
                                if (!skinable.getRowContextMenu().isShowing()) {
                                    skinable.getRowContextMenu().show(row.getParent().getScene().getWindow(),
                                            event.getScreenX() + CONTEXT_MENU_MOUSE_SPACING,
                                            event.getScreenY() + CONTEXT_MENU_MOUSE_SPACING);//row.getParent().getScene().getWindow());
                                } else {
                                    skinable.getRowContextMenu().hide();
                                }
                            }
                        }
                    }
                });
                return row;

            }
        });
        //DIRTY
        //but there is currently no other way to get the correct nodes to interact with
        //only needed if menu is shown(nodes must be realignt in the ui)
        dragIndictor = getChildren().get(3);
        dragOverlay = getChildren().get(2);

        menuAreaHeight.bind(tableHeaderRow.heightProperty());
        if (skinable.getShowMenu()) {
//            //translate the indicator the amount of the top area height
//            //needed to align porpertly in the ui if user reorder the tableview
            dragIndictor.translateYProperty().bind(menuAreaHeight);
            showMenu();
        }
        skinable.showMenuProperty().addListener(new ChangeListener<Boolean>() {
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

        if (skinable.getShowCurrentMaxMenu()) {
            showCurrentMaxMenu();
        }
        skinable.showCurrentMaxMenuProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    showCurrentMaxMenu();
                } else {
                    hideCurrentMaxMenu();
                }
            }
        });
        ScrollBar vbar = getVBar();
        if (vbar != null) {
            skinable.vPosProperty().bindBidirectional(vbar.valueProperty());
        }
        ScrollBar hbar = getHBar();
        if (hbar != null) {
            skinable.hPosProperty().bindBidirectional(hbar.valueProperty());
        }
    }

    /**
     * @param pSkinnable skinnalbe control
     * @param pReorder indicator if table is reorderable
     */
    public AsyncTableViewSkin(AsyncTableView<T> pSkinnable, boolean pReorder) {
        this(pSkinnable);
        setReordering(pReorder);
    }

    /**
     * @return height property of the top of the tableview (menu plus headerrow)
     */
    public ReadOnlyDoubleProperty menuAreaHeight() {
        return menuAreaHeight.getReadOnlyProperty();
    }

//    private Double getMenuAreaHeight() {
//        return menuAreaHeight.get();
//    }
    /**
     * set Pref Height
     *
     * @param pPrefMenuHeight new pref height
     */
    public void setMenuHeight(double pPrefMenuHeight) {
        if (menu != null) {
            menu.setPrefHeight(pPrefMenuHeight);
        }
    }

    public Double getMenuHeight() {
        if (menu != null) {
            return menu.getHeight();
        }
        return 0.0;
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
            //compute postion and add offset and remove menu height from tableview 
            y += labelHeight;
            h -= labelHeight;
            //dirty part two:
            //drag overlay must be realignt when childs are drawn
            dragOverlay.setTranslateY(y);

        }
        super.layoutChildren(x, y, w, h);
    }

    /*
    * PRIVATE METHODES
     */
    private void showMenu() {
        menu = new SectionHeader();
        Button btnReload = new Button();
        btnReload.getStyleClass().add("cpx-icon-button");
        btnReload.setId("reloadButton");
        btnReload.setTooltip(new Tooltip("Neu laden"));
        //\uf021
        Glyph glyph = ResourceLoader.getGlyph("\uf021");
        if (glyph != null) {
            btnReload.setGraphic(glyph);
        } else {
            btnReload.setText("reload");
        }
        btnReload.setOnAction((ActionEvent event) -> {
            ((AsyncTableView) getSkinnable()).reload();
        });

        Bindings.bindContentBidirectional(menu.getMenuItems(), ((AsyncTableView<T>) getSkinnable()).getMenuItems());
        menu.addMenuItems(btnReload);
        Bindings.bindContentBidirectional(menu.searchItems(), ((AsyncTableView<T>) getSkinnable()).getFilterItems());
        menu.titleProperty().bind(((AsyncTableView<T>) getSkinnable()).titleProperty());
        menuAreaHeight.unbind();
        TableHeaderRow tableHeaderRow = (TableHeaderRow) this.getSkinnable().lookup("TableHeaderRow");
        menuAreaHeight.bind(Bindings.add(menu.heightProperty(), tableHeaderRow.heightProperty()));
        getChildren().add(menu);
    }

    private void hideMenu() {
        menuAreaHeight.unbind();
        TableHeaderRow tableHeaderRow = (TableHeaderRow) this.getSkinnable().lookup("TableHeaderRow");
        menuAreaHeight.bind(tableHeaderRow.heightProperty());
        menu.titleProperty().unbind();
        getChildren().remove(menu);
        menu = null;
    }

    private void showCurrentMaxMenu() {
        if (menu == null) {
            return;
        }
        AsyncTableView<T> skinable = (AsyncTableView<T>) getSkinnable();
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

//    protected final ScrollBar getVBar() {
//        return (ScrollBar) getSkinnable().lookup(".scroll-bar:vertical");
//    }
//
//    protected final ScrollBar getHBar() {
//        return (ScrollBar) getSkinnable().lookup(".scroll-bar:horizontal");
//    }
    protected final ScrollBar getVBar() {
        ScrollBar vbar = getSkinnable().lookupAll(".scroll-bar").stream().map(e -> (ScrollBar) e).filter(e -> e.getOrientation().equals(Orientation.VERTICAL)).findFirst().orElse(null);
        if (vbar == null) {
            LOG.log(Level.WARNING, "vbar is null!");
        }
        return vbar;
    }

    protected final ScrollBar getHBar() {
        ScrollBar hbar = getSkinnable().lookupAll(".scroll-bar").stream().map(e -> (ScrollBar) e).filter(e -> e.getOrientation().equals(Orientation.HORIZONTAL)).findFirst().orElse(null);
        if (hbar == null) {
            LOG.log(Level.WARNING, "hbar is null!");
        }
        return hbar;
    }

    public boolean isVBarShown() {
        ScrollBar vbar = getVBar();
        if (vbar != null) {
            return vbar.isVisible();
        }
        return false;
    }

    public double getVBarHeight() {
        ScrollBar vbar = getVBar();
        if (vbar != null) {
            return vbar.getHeight();
        }
        return 0D;
    }
}

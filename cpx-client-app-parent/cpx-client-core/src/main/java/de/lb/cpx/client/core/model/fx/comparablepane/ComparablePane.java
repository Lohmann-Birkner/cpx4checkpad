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

import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;

/**
 * Comparable Pane base implementation to compare items in the way simulation
 * required
 *
 * @author wilde
 * @param <Z> base control/pane which contains the data gridpane,tableview etc.
 * @param <E> enclosing object of the comparable TODO: auto update
 */
public abstract class ComparablePane<Z extends Region, E extends ComparableContent<? extends AbstractEntity>> extends Control {

    private static final Logger LOG = Logger.getLogger(ComparablePane.class.getName());

    public static final String DESTROY_SELECTION_CONTROL = "destroy_selection_control";
    protected ObservableList<E> versionList = FXCollections.observableArrayList();
    private ObservableMap<E, ItemContainer<Z, E>> tableViewToVersion = FXCollections.observableHashMap();
    private ObjectProperty<Integer> selectedRow = new SimpleObjectProperty<>(null);
    private DoubleProperty minWidthVBar = new SimpleDoubleProperty(15.0);
    private ObjectProperty<Z> infoRegionProperty = new SimpleObjectProperty<>();
    private BooleanProperty showMenuProperty = new SimpleBooleanProperty(false);
    private ObjectProperty<Button> menuButtonProperty = new SimpleObjectProperty<>(null);
    private DoubleProperty minInfoRegionWidth = new SimpleDoubleProperty(250);
    private DoubleProperty versionContentWidth = new SimpleDoubleProperty(230);
    private DoubleProperty prefHeaderHeight = new SimpleDoubleProperty(40);
    private ObjectProperty<Comparator<E>> comperatorProperty = new SimpleObjectProperty<>(null);
    private ObjectProperty<ContextMenu> contextRowMenuProperty = new SimpleObjectProperty<>(new CtrlContextMenu<>());

    private Callback<E, Boolean> onRemove;

    private final Callback<E, Control> defaultVersionCtrlFactory = new Callback<E, Control>() {
        @Override
        public Control call(E param) {
            Label label = new Label(param.getVersionName());
//            label.setStyle("-fx-background-color:white");
            label.setMaxWidth(Double.MAX_VALUE);
            label.setPadding(new Insets(4.0, 5.0, 4.0, 5.0));
            return label;
        }
    };
    private ObjectProperty<Callback<E, Control>> versionCtrlFactoryProperty = new SimpleObjectProperty<>(defaultVersionCtrlFactory);
    private final ChangeListener<Callback<E, Control>> versionFactoryListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Callback<E, Control>> observable, Callback<E, Control> oldValue, Callback<E, Control> newValue) {
            Iterator<E> it = getTableViewToVersion().keySet().iterator();
            while (it.hasNext()) {
                E next = it.next();
                getTableViewToVersion().get(next).setControl(newValue.call(next));
            }
        }
    };

    public ComparablePane() {
        super();
        setSkin(new ComparablePaneSkin<>(this));
        setInfoRegionProperty(createInfoPane());

        versionCtrlFactoryProperty.addListener(versionFactoryListener);
    }

//    @Override
//    protected Skin<?> createDefaultSkin() {
//        return new ComparablePaneSkin<>(this);
//    }
    public void dispose() {
        getSkin().dispose();
        versionList.clear();
        tableViewToVersion.forEach(new BiConsumer<E, ItemContainer<Z, E>>() {
            @Override
            public void accept(E t, ItemContainer<Z, E> u) {
                u.dispose();
            }
        });
        tableViewToVersion.clear();
        comperatorProperty = null;
        contextRowMenuProperty = null;
        infoRegionProperty = null;
        menuButtonProperty = null;
        versionCtrlFactoryProperty.removeListener(versionFactoryListener);
        versionCtrlFactoryProperty = null;
    }

    public abstract Z createInfoPane();

    public abstract Z createNewVersionTableView(E pContent);

    /**
     * get version considered as base version is normaly an kis version, index
     * in the list is 0
     *
     * @return version considered as base
     */
    public E getBaseVersion() {
        return versionList.get(0);
    }

    public ObservableMap<E, ItemContainer<Z, E>> getTableViewToVersion() {
        return tableViewToVersion;
    }

    public E getVersionForTableView(Z pRegion) {
        for (E version : getVersionList()) {
            ItemContainer<Z, E> container = getAreaForVersion(version);
            if (container.getRegion().equals(pRegion)) {
                return version;
            }
        }
        return null;
    }

    public Z getInfoRegion() {
        return infoRegionProperty.get();
    }

    public final void setInfoRegionProperty(Z pInfoRegion) {
        infoRegionProperty.set(pInfoRegion);
    }

    public ObjectProperty<Z> infoRegionProperty() {
        return infoRegionProperty;
    }

    public Callback<E, Control> getVersionCtrlFactory() {
        return versionCtrlFactoryProperty.get();
    }

    public void setVersionCtrlFactory(Callback<E, Control> pFactory) {
        versionCtrlFactoryProperty.set(pFactory);
    }

    public ObjectProperty<Callback<E, Control>> versionCtrlProperty() {
        return versionCtrlFactoryProperty;
    }

    public final void setMinWidthVBar(double pMinWidth) {
        minWidthVBar.set(pMinWidth);
    }

    public double getMinWidthVBar() {
        return minWidthVBar.get();
    }

    public DoubleProperty minWidthVBar() {
        return minWidthVBar;
    }

    public ContextMenu getContextRowMenu() {
        return contextRowMenuProperty.get();
    }

    public void setContextRowMenu(ContextMenu pMenu) {
        contextRowMenuProperty.set(pMenu);
    }

    public void addContextRowMenu(ContextMenu pMenu) {
        if (getContextRowMenu() == null) {
            setContextRowMenu(pMenu);
            return;
        }
        getContextRowMenu().getItems().addAll(pMenu.getItems());
    }

    public void addContextRowMenu(int pIndex, ContextMenu pMenu) {
        if (getContextRowMenu() == null) {
            setContextRowMenu(pMenu);
            return;
        }
        getContextRowMenu().getItems().addAll(pIndex, pMenu.getItems());
    }

    public ObjectProperty<ContextMenu> contextRowMenuProperty() {
        return contextRowMenuProperty;
    }

    /**
     * set the selected row
     *
     * @param pRow row index to select
     */
    public void setSelectedRow(int pRow) {
        selectedRow.setValue(pRow);
    }

    /**
     * get the current selected row index
     *
     * @return row index, null if nothign is selected
     */
    public Integer getSelectedRow() {
        return selectedRow.get();
    }

    /**
     * gets the selected row property as object property, needed because value
     * can be null
     *
     * @return selected row object property, value is null if nothing is
     * selected
     */
    public final ObjectProperty<Integer> getSelectedRowProperty() {
        return selectedRow;
    }

    public BooleanProperty showMenuProperty() {
        return showMenuProperty;
    }

    public void setShowMenu(boolean pShow) {
        showMenuProperty.set(pShow);
    }

    public boolean getShowMenu() {
        return showMenuProperty.get();
    }

    public void showMenu() {
        setShowMenu(true);
    }

    public void hideMenu() {
        setShowMenu(false);
    }

    public ObjectProperty<Button> menuButtonProperty() {
        return menuButtonProperty;
    }

    public void setMenuButton(Button pButton) {
        menuButtonProperty.set(pButton);
    }

    public Button getMenuButton() {
        return menuButtonProperty.get();
    }

    public double getMinInfoWidth() {
        return minInfoRegionWidth.get();
    }

    public void setMinInfoWidth(double pMinWidth) {
        minInfoRegionWidth.set(pMinWidth);
    }

    public DoubleProperty minInfoWidthProperty() {
        return minInfoRegionWidth;
    }

    public double getVersionContentWidth() {
        return versionContentWidth.get();
    }

    public void setVersionContentWidth(double pWidth) {
        versionContentWidth.set(pWidth);
    }

    public DoubleProperty versionContentWidthProperty() {
        return versionContentWidth;
    }

    public double getPrefHeaderHeight() {
        return prefHeaderHeight.get();
    }

    public void setPrefHeaderHeight(double prefHeight) {
        prefHeaderHeight.set(prefHeight);
    }

    public DoubleProperty prefHeaderHeightProperty() {
        return prefHeaderHeight;
    }

    /**
     * get the tableview for a specific version
     *
     * @param pVersion version to get tableview for
     * @return table view for the version
     */
    public ItemContainer<Z, E> getAreaForVersion(E pVersion) {
        return tableViewToVersion.get(pVersion);
    }

    public Z getRegionForVersion(E pVersion) {
        return getAreaForVersion(pVersion).getRegion();
    }

    /**
     * @param pItems list of versions, old ones will be cleared
     */
    public void clearAndSetVersions(ObservableList<E> pItems) {
        for (E version : versionList) {
            version.destroy();
        }
        versionList.clear();
        versionList.addAll(pItems);
    }

    public ObservableList<E> getVersionList() {
        return versionList;
    }
    private final ChangeListener<TGroupingResults> groupingListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends TGroupingResults> observable, TGroupingResults oldValue, TGroupingResults newValue) {
//                long start = System.currentTimeMillis();
            refresh();
//                LOG.info("refresh in " + (System.currentTimeMillis()-start));
        }
    };
    private final ChangeListener<AbstractEntity> contentListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends AbstractEntity> observable, AbstractEntity oldValue, AbstractEntity newValue) {
//                long start = System.currentTimeMillis();
            reload();
//                LOG.info("reload in " + (System.currentTimeMillis()-start));
        }
    };

    /**
     * @param item add new version
     */
    public void addVersion(E item) {
        versionList.add(item);
        item.getListenerAdapter().addChangeListener(item.groupingResultProperty(), groupingListener);
        item.getListenerAdapter().addChangeListener(item.contentProperty(), contentListener);
        reload();
    }

    /**
     * @param item version to remove
     */
    public void removeVersion(E item) {
        versionList.remove(item);
        item.destroy();
//        item.groupingResultProperty().removeListener(groupingListener);
//        item.contentProperty().removeListener(contentListener);
    }

    /**
     * @return area with non-versionbased content
     */
    public final Z getInfo() {
        return infoRegionProperty().get();
    }

    /**
     * reload content, should result in reload data from the server
     */
    public void reload() {
    }

    /**
     * refresh values after grouping
     */
    public void refresh() {

    }

    /**
     * @return id list of all selected versions
     */
    public List<Long> getVersionIds() {
        List<Long> idList = new ArrayList<>();
        for (int index = 0; index < versionList.size(); index++) {
            AbstractEntity contentEntity = versionList.get(index).getCaseDetails();//.getContent();
            if (contentEntity == null) {
                LOG.warning("Content Entity for index " + index + " is null!");
                continue;
            }
            idList.add(contentEntity.id);
        }
        return idList;
    }

    public void groupVersions(List<Long> pVersionIds) {
        for (E version : versionList) {
            if (pVersionIds.contains(version.getContentId())) {
                version.performGroup();
            }
        }
    }

    public ScrollBar getVScrollBar() {
        return ((ComparablePaneSkin) getSkin()).getVScrollBar();
    }

    public ScrollPane getScrollPane() {
        return ((ComparablePaneSkin) getSkin()).getSpContent();
    }

    public ScrollBar getHScrollBar() {
        return ((ComparablePaneSkin) getSkin()).getHScrollBar();
    }

    public Comparator<E> getComparator() {
        return comperatorProperty.get();
    }

    public void setComparator(Comparator<E> pComparator) {
        comperatorProperty.set(pComparator);
    }

    public ObjectProperty<Comparator<E>> comparatorProperty() {
        return comperatorProperty;
    }

    public Callback<E, Boolean> getOnRemoveEvent() {
        return onRemove;
    }

    public void setOnRemove(Callback<E, Boolean> pOnRemove) {
        onRemove = pOnRemove;
    }
}

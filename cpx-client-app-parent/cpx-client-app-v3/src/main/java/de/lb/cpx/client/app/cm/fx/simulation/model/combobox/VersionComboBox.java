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
package de.lb.cpx.client.app.cm.fx.simulation.model.combobox;

import de.lb.cpx.client.app.cm.fx.simulation.model.VersionContent;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.core.model.fx.comparablepane.ItemContainerControl;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCaseDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * implementation of the combobox for the simulation tracks used versions and
 * disable them in the ui
 *
 * @author wilde
 */
public class VersionComboBox extends ComboBox<TCaseDetails> implements ItemContainerControl {

    private static final Logger LOG = Logger.getLogger(VersionComboBox.class.getName());

    private Callback<ListView<TCaseDetails>, ListCell<TCaseDetails>> factory;
    private VersionStringConverter nameConverter;
    private VersionManager versionManager;

    private ChangeListener<TCaseDetails> CONTENT_LISTENER = new ChangeListener<TCaseDetails>() {
        @Override
        public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
            if(oldValue != null){
                versionManager.unMarkAsDisplayed(oldValue);
            }
            if (newValue != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        getSelectionModel().select(newValue);
                        refresh();
                    }
                });
            }
        }
    };
    private ChangeListener<TCaseDetails> VALUE_LISTENER = new ChangeListener<TCaseDetails>() {
        @Override
        public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
            versionManager.markAsDisplayed(newValue);
            versionManager.unMarkAsDisplayed(oldValue);
//            String text = nameConverter.getTooltipText(newValue);
//            if (text != null && !text.isEmpty()) {
                Node graphic = nameConverter.getTooltipGraphic(newValue);
                if(graphic != null){
                    CpxTooltip tip = new CpxTooltip("");
                    tip.setShowDuration(Duration.INDEFINITE);
                    tip.setGraphic(nameConverter.getTooltipGraphic(newValue));
                    setTooltip(newValue != null ? tip : null);
                }
//            }
            version.setContent(newValue);
            if (getSkin() != null && getSkin() instanceof ComboBoxListViewSkin) {
                refresh();
            }
        }
    };
    private VersionContent version;
    private boolean destroyed;

    public VersionComboBox() {
        super();
        nameConverter = new VersionStringConverter(VersionStringConverter.DisplayMode.ACTUAL);
        setConverter(nameConverter);
        factory = new Callback<ListView<TCaseDetails>, ListCell<TCaseDetails>>() {
            @Override
            public ListCell<TCaseDetails> call(ListView<TCaseDetails> param) {
                return new VersionCell();
            }
        };
        setCellFactory(factory);
        setMinWidth(HBox.USE_PREF_SIZE);
        setMaxWidth(Double.MAX_VALUE);
    }

    public VersionComboBox(VersionManager pManager, VersionContent pVersion) {
        this();
        versionManager = pManager;
        version = pVersion;
        valueProperty().addListener(VALUE_LISTENER);

        TCaseDetails details = pVersion.getCaseDetails();//Content();
        if (details != null) {
            List<TCaseDetails> list = new ArrayList<>(details.getCsdIsLocalFl() ? versionManager.getAllAvailableLocals() : versionManager.getAllAvailableExterns());
            setItems(FXCollections.observableArrayList(list));
        }
        getSelectionModel().select(details);
        pVersion.contentProperty().addListener(CONTENT_LISTENER);
    }

    @Override
    public void refresh() {
        if (getSkin() != null) {
            ((ListView) ((ComboBoxListViewSkin) getSkin()).getPopupContent()).refresh();
        }
    }

    @Override
    public void destroy() {
        if (CONTENT_LISTENER != null) {
            version.contentProperty().removeListener(CONTENT_LISTENER);
            CONTENT_LISTENER = null;
        }
        if (VALUE_LISTENER != null) {
            valueProperty().removeListener(VALUE_LISTENER);
            VALUE_LISTENER = null;
        }
        setConverter(null);
        nameConverter = null;
        setCellFactory(null);
        factory = null;
        destroyed = true;
    }
    protected final boolean isDestroyed(){
        return destroyed;
    }
    private class VersionCell extends ListCell<TCaseDetails> {

        public VersionCell() {

        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(selected);
            if (selected && isEmpty()) {
                return;
            }
            updateItem(getItem(), isEmpty());
        }
        
        @Override
        protected void updateItem(TCaseDetails item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty || isDestroyed()) {
                setGraphic(null);
                //prevent cb from drawing cell with null value
                if (!empty) {
                    getListView().getItems().remove(getIndex());
                }
                setTooltip(null);
                return;
            }
            Label versionInfo = new Label(getConverter().toString(item));
            setDisable(versionManager.isDisplayed(item));
            setGraphic(versionInfo);
//            CpxTooltip tip = new CpxTooltip(nameConverter.getTooltipText(item));
            CpxTooltip tip = new CpxTooltip("");
            tip.setGraphic(nameConverter.getTooltipGraphic(item));
            versionInfo.setTooltip(tip);
        }
    }
}

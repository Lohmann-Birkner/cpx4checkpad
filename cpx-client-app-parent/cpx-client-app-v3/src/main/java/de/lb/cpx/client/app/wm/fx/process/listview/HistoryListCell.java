/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.listview;

import de.lb.cpx.client.app.wm.fx.process.listview.entries.HistoryEntry;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.wm.model.TWmEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author wilde
 */
public class HistoryListCell extends ListCell<HistoryEntry<? extends AbstractEntity>> {

    private HistoryListItem<TWmEvent> listItem;

    public HistoryListCell() {
        super();
        setAlignment(Pos.CENTER_LEFT);
        setStyle("-fx-padding:0");
        selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (isHistoryListView()) {
                        HistoryListView history = (HistoryListView) getListView();
                        if (!history.isArmed()) {
                            return;
                        }
                    }
                    getListView().requestFocus();
                }
            }
        });
    }

    @Override
    protected void updateItem(HistoryEntry<? extends AbstractEntity> item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            return;
        }
        if (listItem == null) {
            listItem = new HistoryListItem<>();
            listItem.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (getItem() == null || isEmpty()) {
                        return;
                    }
                    getListView().getSelectionModel().select(getIndex());
                    if (MouseButton.PRIMARY.equals(event.getButton())
                            && event.getClickCount() >= 2 && !getItem().isReadOnly()) {
                        getItem().doDefaultOperation(getItem().getEvent());
                    }
                }
            });
            listItem.prefWidthProperty().bind(getListView().widthProperty().subtract(38));
            updateEvent(item);
            setGraphic(listItem);
            return;
        }
        updateEvent(item);
        if (getGraphic() == null) {
            setGraphic(listItem);
        }
    }

    private void updateEvent(HistoryEntry<? extends AbstractEntity> item) {
        listItem.setEventEntity(item.getEvent());
        listItem.setTitle(item.getHistoryTitle());
        listItem.setDescription(item.getHistoryDescription());
        listItem.setDisplayMode(getItemDisplayMode());
        listItem.setReadOnly(item.isReadOnly());
        listItem.setButtons(item.getMenuButtons());
        listItem.setCompact(Session.instance().isShowHistoryEventDetails());
    }

    private boolean isHistoryListView() {
        return (getListView() instanceof HistoryListView);
    }

    private boolean isToFadeOut() {
        if (!isHistoryListView()) {
            return false;
        }
        HistoryListView view = (HistoryListView) getListView();
        if (view.getLimit() < 0) {
            return false;
        }
        if (view.hasMoreItems()) {
            return true;
        }
        return false;
    }

    private HistoryListItem.DisplayMode getItemDisplayMode() {
        if (getListView().getItems().size() == 1) {
            return HistoryListItem.DisplayMode.SINGLE;
        }
        int idx = getIndex();
        if (idx == -1) {
            return HistoryListItem.DisplayMode.END;
        }
        if (idx == 0) {
            return HistoryListItem.DisplayMode.END;
        }
        if (idx == getListView().getItems().size() - 1) {
            if (isToFadeOut()) {
                return HistoryListItem.DisplayMode.START_FATE_OUT;
            }
            return HistoryListItem.DisplayMode.START;
        }
        if (idx == getListView().getItems().size() - 2) {
            return HistoryListItem.DisplayMode.AFTER_START;
        }
        return HistoryListItem.DisplayMode.ITEM;
    }

}

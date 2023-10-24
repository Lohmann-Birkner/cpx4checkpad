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

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.ActionHistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.CaseHistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.DocumentHistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.HistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.InkaHistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.KainHistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.ProcessFinalisationHistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.ProcessHistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.ReminderHistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.RequestHistoryEntry;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.listview.AsyncListViewSkin;
import de.lb.cpx.client.core.util.SmoothScrollbarHelper;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.wm.model.TWmEvent;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class HistoryListView extends AsyncListView<HistoryEntry<? extends AbstractEntity>> {

    private static final Logger LOG = Logger.getLogger(HistoryListView.class.getName());

    private final Callback<TWmEvent, HistoryEntry<? extends AbstractEntity>> defaultEntryFactory = new Callback<TWmEvent, HistoryEntry<? extends AbstractEntity>>() {
        @Override
        public HistoryEntry<? extends AbstractEntity> call(TWmEvent param) {
            if (param == null) {
                return null;
            }

            if (facade == null) {
                LOG.log(Level.WARNING, "facade should not be null!");
                return null;
            }

            if (param.getEventType().isActionRelated()) {
                return new ActionHistoryEntry(facade, param, isReadOnly());
            }
            if (param.getEventType().isReminderRelated()) {
                return new ReminderHistoryEntry(facade, param, isReadOnly());
            }
            if (param.getEventType().isDocumentRelated()) {
                return new DocumentHistoryEntry(facade, param, isReadOnly());
            }
            if (param.getEventType().isRequestRelated()) {
                return new RequestHistoryEntry(facade, param, isReadOnly());
            }
            if (param.getEventType().isProcessFinalisationRelated()) {
                return new ProcessFinalisationHistoryEntry(facade, param, isReadOnly());
            }
            if (param.getEventType().isProcessRelated()) {
                return new ProcessHistoryEntry(facade, param, isReadOnly());
            }
            if (param.getEventType().isHosCaseRelated()) {
                return new CaseHistoryEntry(facade, param, isReadOnly());
            }
            if (param.getEventType().isInkaRelated()) {
                return new InkaHistoryEntry(facade, param, isReadOnly());
            }
            if (param.getEventType().isKainRelated()) {
                return new KainHistoryEntry(facade, param, isReadOnly());
            }
            LOG.log(Level.WARNING, "unknown history event type found: {0}", param.getEventType());
            return null;
            //return new HistoryEntry(param);
        }
    };
    public static final Integer INDETERMINATE = -1;
    private ProcessServiceFacade facade;
    private int oldSelectedIndex;
    private int oldSize;

    public HistoryListView(@NotNull ProcessServiceFacade pFacade) {
        this();
        setFacade(pFacade);
    }

    public HistoryListView() {
        super();
        setFocusTraversable(false);
        getStyleClass().add("remove-h-scroll-bar");
        getStyleClass().add("cpx-history-list-view");
        setCellFactory(new Callback<ListView<HistoryEntry<? extends AbstractEntity>>, ListCell<HistoryEntry<? extends AbstractEntity>>>() {
            @Override
            public ListCell<HistoryEntry<? extends AbstractEntity>> call(ListView<HistoryEntry<? extends AbstractEntity>> param) {
                return new HistoryListCell();
            }
        });
        addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!isFocused()) {
                    return;
                }
                if (KeyCode.UP.equals(event.getCode())) {
                    getSelectionModel().selectPrevious();
                    event.consume();
                }
                if (KeyCode.DOWN.equals(event.getCode())) {
                    getSelectionModel().selectNext();
                    event.consume();
                }
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        AsyncListViewSkin<HistoryEntry<? extends AbstractEntity>> sk = new AsyncListViewSkin<>(this);
        SmoothScrollbarHelper.smoothVScrollingListView(this, 0.5);
        return sk;
    }

    @Override
    public void beforeTask() {
        super.beforeTask();
        oldSelectedIndex = getSelectionModel().getSelectedIndex();
        oldSize = getItems().size();
    }

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState);
        //restore selection from before reload
        if (pState.equals(Worker.State.SUCCEEDED)) {
            //check sizes of size changes select old index
            if (oldSize != getItems().size()) {
                if (!getItems().isEmpty()) {
                    getFocusModel().getFocusedIndex();
                    getSelectionModel().selectFirst();
                }
            } else {
                int idx = oldSelectedIndex != -1 ? oldSelectedIndex : 0;
                getSelectionModel().select(idx);
                scrollTo(idx);
            }
        }
    }

    @Override
    public Future<List<HistoryEntry<? extends AbstractEntity>>> getFuture() {
        ObservableList<TWmEvent> list = facade.getEventsAsObsList(getLimit() + 1);
        if (getLimit() != INDETERMINATE && list.size() > getLimit()) {
            //whacky?? should only occure for preview
            moreItemsProperty.set(true);
            list.remove(getLimit());
        }
        Stream<HistoryEntry<? extends AbstractEntity>> events = list.stream()
                .map((t) -> {
                    return getHistoryEntryFactory().call(t);
                });
        Stream<HistoryEntry<? extends AbstractEntity>> sorted = events.sorted((HistoryEntry<? extends AbstractEntity> o1, HistoryEntry<? extends AbstractEntity> o2) -> o2.getEvent().getCreationDate().compareTo(o1.getEvent().getCreationDate()));
        return new AsyncResult<>(sorted.collect(Collectors.toList()));
    }
    private ReadOnlyBooleanWrapper moreItemsProperty = new ReadOnlyBooleanWrapper(false);

    private ReadOnlyBooleanProperty moreItemsProperty() {
        return moreItemsProperty.getReadOnlyProperty();
    }

    public boolean hasMoreItems() {
        return moreItemsProperty().get();
    }
    private ObjectProperty<Callback<TWmEvent, HistoryEntry<? extends AbstractEntity>>> historyEntryFactoryProperty;

    public ObjectProperty<Callback<TWmEvent, HistoryEntry<? extends AbstractEntity>>> historyEntryFactoryProperty() {
        if (historyEntryFactoryProperty == null) {
            historyEntryFactoryProperty = new SimpleObjectProperty<>(defaultEntryFactory);
        }
        return historyEntryFactoryProperty;
    }

    public void setHistoryEntryFactory(@NotNull Callback<TWmEvent, HistoryEntry<? extends AbstractEntity>> pFactory) {
        pFactory = Objects.requireNonNull(pFactory, "Factory can not be null!");
        historyEntryFactoryProperty().set(pFactory);
    }

    public Callback<TWmEvent, HistoryEntry<? extends AbstractEntity>> getHistoryEntryFactory() {
        return historyEntryFactoryProperty().get();
    }

    private IntegerProperty limitProperty;

    public IntegerProperty limitProperty() {
        if (limitProperty == null) {
            limitProperty = new SimpleIntegerProperty(-1);
        }
        return limitProperty;
    }

    public void setLimit(Integer pLimit) {
        pLimit = Objects.requireNonNullElse(pLimit, INDETERMINATE);
        limitProperty().set(pLimit);
    }

    public int getLimit() {
        return limitProperty().get();
    }

    private BooleanProperty armedProperty;

    public BooleanProperty armedProperty() {
        if (armedProperty == null) {
            armedProperty = new SimpleBooleanProperty(true);
        }
        return armedProperty;
    }

    public boolean isArmed() {
        return armedProperty().get();
    }

    public void setArmed(boolean pArmed) {
        armedProperty().set(pArmed);
    }

    public boolean isReadOnly() {
        return !isArmed();
    }

    public final void setFacade(@NotNull ProcessServiceFacade pFacade) {
        facade = Objects.requireNonNull(pFacade, "Facade ca not be null");
    }

    protected final ProcessServiceFacade getFacade() {
        return facade;
    }
}

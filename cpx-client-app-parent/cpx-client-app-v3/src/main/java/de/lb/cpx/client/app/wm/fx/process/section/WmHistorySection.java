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
package de.lb.cpx.client.app.wm.fx.process.section;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.listview.HistoryListView;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.HistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.section.details.*;
import de.lb.cpx.client.app.wm.fx.process.section.operations.*;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxCheckComboBox;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.dto.HistoryFilter;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * WmHistorySection is implemented to create and manage the history (timeline)
 * panel.
 *
 * @author wilde, nandola
 */
public final class WmHistorySection extends SectionTitle {

    private static final Logger LOG = Logger.getLogger(WmHistorySection.class.getSimpleName());
    public static final String REFRESH_DETAILS = "refresh_details";

    private HistoryListView historyListView;
    private ObservableList<HistoryEntry<? extends AbstractEntity>> masterData = FXCollections.observableArrayList();
    private final ProcessServiceFacade facade;

    // Wrap the ObservableList in a FilteredList (initially display all events)
    private final FilteredList<HistoryEntry<? extends AbstractEntity>> filteredList = new FilteredList<>(masterData, s -> true);
    private SortedList<HistoryEntry<? extends AbstractEntity>> sortedData;
    private FilteredList<HistoryEntry<? extends AbstractEntity>> newFilteredList = null;
    private FilteredList<HistoryEntry<? extends AbstractEntity>> newTextSearchList = null;
    private TextField historySecTextField;
    private final VBox vboxFilter;
    private ToggleButton tbTextSearch;
    private ToggleButton tbFilter;
    private ToggleButton tbAllEntries;
    private ToggleButton tbKainInka;
    private HBox textSearchFilter;
//    private final EjbProxy<ProcessServiceBeanRemote> processServiceBean;
    private CpxCheckComboBox<CpxEnumInterface<?>> comboBoxEventTypes;
//    private final CpxInsuranceCompanyCatalog insuranceCatalog;
    private final List<String> eventTypeNames = new ArrayList<>();
    private Integer limit;

    private final ListChangeListener<TWmEvent> eventListener = new ListChangeListener<TWmEvent>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends TWmEvent> c) {
            LOG.info("reload event list");
            historyListView.reload();
            setFilterTextField(historySecTextField);
        }
    };

    private final ChangeListener<String> textListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            String filteringText = newValue;
            SortedList<HistoryEntry<? extends AbstractEntity>> sortedDataTmp;
            if (filteringText == null || filteringText.isEmpty()) {
                if (newFilteredList == null || comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty()) {
                    filteredList.setPredicate(s -> true);   //show all listitems of entire listview.
                    sortedDataTmp = new SortedList<>(filteredList);
                } else {
                    newFilteredList.setPredicate(s -> true);    //show all listitems of applied filtered listview.
                    sortedDataTmp = new SortedList<>(newFilteredList);
                }
            } else if (!filteringText.isEmpty() && (newFilteredList == null || comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty())) {
                filteredList.setPredicate(s -> s.checkContent(filteringText));
                sortedDataTmp = new SortedList<>(filteredList);
            } else if (filteringText.isEmpty() && comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty()) {
                filteredList.setPredicate(s -> true);
                sortedDataTmp = new SortedList<>(filteredList);
            } else {
                newFilteredList.setPredicate(s -> s.checkContent(filteringText));
                sortedDataTmp = new SortedList<>(newFilteredList);
            }
            sortedDataTmp.setComparator((HistoryEntry<? extends AbstractEntity> o1, HistoryEntry<? extends AbstractEntity> o2) -> o2.getEvent().getCreationDate().compareTo(o1.getEvent().getCreationDate()));
//20180112-AWi: add items after clear to avoid unsupporedOperationException
            historyListView.setItems(sortedDataTmp);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //try to get correct focus on text field after new items are set in history and history tries to select item
                    //switch focus back
                    historySecTextField.requestFocus();
                    historySecTextField.selectEnd();
                }
            });
            newTextSearchList = new FilteredList<>(historyListView.getItems(), s -> true);
        }
    };

    public WmHistorySection(ProcessServiceFacade pFacade, boolean isArmed) {
        this(pFacade, null, isArmed);
    }

    // this constructor is used to get the test data from the ProcessServiceFacade
    public WmHistorySection(final ProcessServiceFacade pServiceFacade, final Integer pLimit, boolean pIsArmed) {
        super(Lang.getHistorySection());
        facade = pServiceFacade;
        long startTime = System.currentTimeMillis();
        setLimit(pLimit);
        setArmed(pIsArmed);
        LOG.log(Level.FINE, "create new history instance, limitation of events is {0}", String.valueOf(pLimit));
//        processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
//        insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
        historySecTextField = new TextField();
        historySecTextField.setAlignment(Pos.CENTER);
        historySecTextField.setVisible(true);
        String str = Lang.getPromptTextfieldText();
        historySecTextField.setPromptText(str);
        historySecTextField.setBorder(Border.EMPTY);
        historySecTextField.setEditable(true);
        historySecTextField.setPadding(Insets.EMPTY);
        historySecTextField.setPrefSize(190.0, 15.0);
        historySecTextField.textProperty();
        historySecTextField.setPadding(new Insets(5, 5, 5, 5));

        historyListView.setFacade(pServiceFacade);
        historyListView.setArmed(pIsArmed);
        historyListView.setLimit(pLimit);
        historyListView.reload();

        // call this method for text search functionality
        setFilterTextField(historySecTextField);

        registerPropertyListener(historySecTextField.focusedProperty(),((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                //Focus gained
            } else {
                //focus lost
                saveHistoryFilter();
            }
        }));
        LOG.log(Level.INFO, "init first stuff for workflow number {0} in {1} ms", new Object[]{facade.getCurrentProcessNumber(), (System.currentTimeMillis() - startTime)});
//        LOG.log(Level.INFO, "set event listener for " + facade.getCurrentProcessNumber() + " in " + (System.currentTimeMillis() - startTime) + " ms");
        // add checkComboBox to select one or more filtering events types
        comboBoxEventTypes = CpxCheckComboBox.getInstanceEventType();
        comboBoxEventTypes.setMaxWidth(190);
        comboBoxEventTypes.setPrefWidth(190);

        Tooltip eventTypesTT = new Tooltip();
        eventTypesTT.setWrapText(true);
        eventTypesTT.setMaxWidth(500);

        comboBoxEventTypes.setOnMouseEntered((MouseEvent event) -> {
            if (!comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty()) {
                eventTypesTT.show(comboBoxEventTypes, event.getScreenX() + 10, event.getScreenY() + 10);
            }
        });
        comboBoxEventTypes.setOnMouseExited((MouseEvent event) -> {
            eventTypesTT.hide();
        });
        registerListListener(comboBoxEventTypes.getCheckModel().getCheckedItems(), (ListChangeListener) (var change) -> {
            //        comboBoxEventTypes.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends CpxEnumInterface<?>> change) -> {
//filter events based on selected checkComboBoxes. If nothing is selected then simply return whole listview.
            if (comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty()) {

                if ((newTextSearchList == null || historySecTextField.getText().isEmpty())) {
                    filteredList.setPredicate(s -> true);   //show all listitems of entire listview.
                    sortedData = new SortedList<>(filteredList);
                } else {
                    newTextSearchList.setPredicate(s -> true);
                    sortedData = new SortedList<>(newTextSearchList);
                }
            } // if first checkbox is selected, show all events
            else if (!comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty() && (newTextSearchList == null || historySecTextField.getText().isEmpty())) {
                filteredList.setPredicate(s -> !change.getList().isEmpty() ? change.getList().contains(s.getEvent().getEventType()) : true);
                sortedData = new SortedList<>(filteredList);
            } else if (historySecTextField.getText().isEmpty() && comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty()) {
                filteredList.setPredicate(s -> true);   //show all listitems of entire listview.
                sortedData = new SortedList<>(filteredList);
            } else {
                newTextSearchList.setPredicate(s -> !change.getList().isEmpty() ? change.getList().contains(s.getEvent().getEventType()) : true);
                sortedData = new SortedList<>(newTextSearchList);
            }
            sortedData.setComparator((HistoryEntry<? extends AbstractEntity> o1, HistoryEntry<? extends AbstractEntity> o2) -> o2.getEvent().getCreationDate().compareTo(o1.getEvent().getCreationDate()));
            historyListView.setItems(sortedData);
            historyListView.refresh();

            setFilterTextField(historySecTextField);
            while (change.next()) {
                if (change.wasAdded()) {
                    List<? extends CpxEnumInterface<?>> added = change.getAddedSubList();
                    added.stream().forEach((item) -> {
                        if (!(eventTypeNames.stream().anyMatch(t -> t.equals(item.getTranslation().getAbbreviation())))) {
                            eventTypeNames.add(item.getTranslation().getAbbreviation());
                        }
                    });
                }
                if (change.wasRemoved()) {
                    List<? extends CpxEnumInterface<?>> removed = change.getRemoved();
                    removed.stream().forEach((item) -> {
                        eventTypeNames.remove(item.getTranslation().getAbbreviation());
                    });
                }
                eventTypesTT.setText(eventTypeNames.toString().substring(1, eventTypeNames.toString().length() - 1));
            }
        });
        LOG.log(Level.INFO, "init second stuff for workflow number {0} in {1} ms", new Object[]{facade.getCurrentProcessNumber(), (System.currentTimeMillis() - startTime)});
        vboxFilter = new VBox();
        vboxFilter.getChildren().add(comboBoxEventTypes);  // add checkComboBox in vbox
//        PopOver popover = new PopOver();
//        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
//        popover.setContentNode(vboxFilter);   // add vbox as a content node to the popover
//        comboBoxEventTypes.setOnMouseClicked((MouseEvent event) -> {
//            popover.show(comboBoxEventTypes);
//        } // by clicking on the button popover should be shown
//        );

        //save the filter setting with hidden event
        comboBoxEventTypes.addEventHandler(ComboBox.ON_HIDDEN, event -> {
            saveHistoryFilter();
        });

        LOG.log(Level.INFO, "history constructor for workflow number {0} loaded in {1} ms", new Object[]{facade.getCurrentProcessNumber(), (System.currentTimeMillis() - startTime)});
        registerMapListener(pServiceFacade.getProperties(), (MapChangeListener) (var change) -> {
            if (change.wasAdded()) {
                if (REFRESH_DETAILS.equals(change.getKey())) {
                    invalidateRequestDetailProperty();
                    pServiceFacade.getProperties().remove(REFRESH_DETAILS);
                }
            }
        });
//        pServiceFacade.getProperties().addListener((MapChangeListener.Change<? extends Object, ? extends Object> change) -> {
//            if (change.wasAdded()) {
//                if (REFRESH_DETAILS.equals(change.getKey())) {
//                    invalidateRequestDetailProperty();
//                    pServiceFacade.getProperties().remove(REFRESH_DETAILS);
//                }
//            }
//        });
        //restore history filter (for each process opening)
        restoreHistoryFilter();
        LOG.log(Level.INFO, "restore filter for workflow number {0} in {1} ms", new Object[]{facade.getCurrentProcessNumber(), (System.currentTimeMillis() - startTime)});
    }

    private void setLimit(final Integer pLimit) {
        limit = pLimit;
    }

    public Integer getLimit() {
        return limit;
    }

    //....... this calls change Listener many times........
    public void restoreHistoryFilter() {
//        if (comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty()) {
        if (!comboBoxEventTypes.getCheckModel().getCheckedItems().isEmpty()) {
            comboBoxEventTypes.getCheckModel().clearChecks();
        }
        final HistoryFilter historyFilter = Session.instance().getHistoryFilter();
        historySecTextField.setText(historyFilter.getSearchText());

        for (WmEventTypeEn eventType : historyFilter.getSelectedEventTypes()) {
            if (eventType != null) {
                comboBoxEventTypes.getCheckModel().check(eventType);
            }
        }
    }

    // can find a better way to filter and show events.
    public void restoreHistoryFilterAfterReload() {

        final HistoryFilter historyFilter = Session.instance().getHistoryFilter();

        historySecTextField.setText(historyFilter.getSearchText());

        ObservableList<CpxEnumInterface<?>> prevConfiguredFilterItems = FXCollections.observableArrayList(historyFilter.getSelectedEventTypes());

        if (!prevConfiguredFilterItems.isEmpty()
                && !comboBoxEventTypes.getCheckModel().getCheckedItems().containsAll(prevConfiguredFilterItems)
                && !isKainInkaButtonSelected()) {
//                !comboBoxEventTypes.getCheckModel().getCheckedItems().subList(1, comboBoxEventTypes.getCheckModel().getCheckedItems().size()).containsAll(prevConfiguredFilterItems)) {
            prevConfiguredFilterItems.forEach((CpxEnumInterface<?> t) -> {
                if (!comboBoxEventTypes.getCheckModel().isChecked(t)) {
                    comboBoxEventTypes.getCheckModel().check(t);
                }
            });
        } else if (isAllEntriesButtonSelected() || prevConfiguredFilterItems.isEmpty()) {
            //show all
//            historyListView.setItems(new FilteredList<>(historyListView.getItems(), s -> true));
            historyListView.setItems(new FilteredList<>(masterData, s -> true));
        } else if (isKainInkaButtonSelected()) {
            //show only Kain-Inka filtered related events
            historyListView.setItems(new FilteredList<>(historyListView.getItems().filtered((t) -> {
                WmEventTypeEn eventType = t.getEvent().getEventType();
                return eventType.equals(WmEventTypeEn.kainReceived) || eventType.equals(WmEventTypeEn.inkaStored) || eventType.equals(WmEventTypeEn.inkaSent) || eventType.equals(WmEventTypeEn.inkaCancelled);
            }), s -> true));
        } else if (sortedData != null && !sortedData.isEmpty()) {
            historyListView.setItems(sortedData);
            historyListView.refresh();
            newFilteredList = new FilteredList<>(historyListView.getItems(), s -> true);
        } else {
            // show none
            historyListView.setItems(new FilteredList<>(historyListView.getItems(), s -> false));
        }

    }

    public void saveHistoryFilter() {
        LOG.log(Level.INFO, "Will save history filter now...");
        final String searchText = historySecTextField.getText();
//        final List<WmEventTypeEn> selectedEventTypes = comboBoxEventTypes.getCheckModel().getCheckedItems();
        final ObservableList<CpxEnumInterface<?>> selectedEventTypes = comboBoxEventTypes.getCheckModel().getCheckedItems();
        final List<WmEventTypeEn> selectedEvents = new ArrayList<>();
        for (CpxEnumInterface<?> item : selectedEventTypes) {
            if (item instanceof WmEventTypeEn) {
                selectedEvents.add(((WmEventTypeEn) item));
            }
        }
        final WmEventTypeEn[] types = new WmEventTypeEn[selectedEvents.size()];
        selectedEvents.toArray(types);
//        HistoryFilter historyFilter = new HistoryFilter(searchText, selectedEventTypes);
        long[] userIds = null; //TODO: Implement filter feature by users in future release
        HistoryFilter historyFilter = new HistoryFilter(searchText, types, userIds);
        Session.instance().setHistoryFilter(historyFilter);
    }

    public ObservableList<CpxEnumInterface<?>> getHistoryFilterEventTypes() {
        return comboBoxEventTypes.getCheckModel().getCheckedItems();
    }

    public String getHistoryFilterSearchText() {
        return historySecTextField.getText();
    }

    /**
     * text search functionality for different events.
     *
     * @param txtField textField to be used
     */
    public void setFilterTextField(TextField txtField) {
        txtField.textProperty().removeListener(textListener);
        txtField.textProperty().addListener(textListener);
    }

    @Override
    public void reload() {
        super.reload();
        historyListView.reload();
    }

    /**
     * to add main menus to the history panel. They can be used to create
     * different events like, action creation, request creation, etc.
     */
    @Override
    public void setMenu() {
        final PopOver menuOver = new AutoFitPopOver();

        Button createAction = new Button(Lang.getActionCreate());
        createAction.setStyle("-fx-alignment: CENTER-LEFT;");
        createAction.setMaxWidth(Double.MAX_VALUE);
        createAction.setOnMouseClicked((MouseEvent event) -> {
            createNewActionEvent();
            menuOver.hide();
        });

        Button createReminder = new Button(Lang.getReminderCreate());
        createReminder.setStyle("-fx-alignment: CENTER-LEFT;");
        createReminder.setMaxWidth(Double.MAX_VALUE);
        createReminder.setOnMouseClicked((MouseEvent event) -> {
            createNewReminderEvent();
            //historyListView.reload();
        });

        Button createRequest = new Button(Lang.getRequestCreate());
        createRequest.setStyle("-fx-alignment: CENTER-LEFT;");
        createRequest.setMaxWidth(Double.MAX_VALUE);
        createRequest.setOnMouseClicked((MouseEvent event) -> {
            createNewRequestEvent();
            //historyListView.reload();
        });

        Button createService = new Button(Lang.getServiceCreate());
        createService.setStyle("-fx-alignment: CENTER-LEFT;");
        createService.setMaxWidth(Double.MAX_VALUE);
        createService.setOnMouseClicked((MouseEvent event) -> {
            createNewServiceEvent();
            //historyListView.reload();
        });

        Button createDocument = new Button(Lang.getDocumentCreate());
        createDocument.setStyle("-fx-alignment: CENTER-LEFT;");
        createDocument.setMaxWidth(Double.MAX_VALUE);
        createDocument.setOnMouseClicked((MouseEvent event) -> {
            createNewDocumentEvent();
            //historyListView.reload();
        });

        Button showEventDetails = new Button("Vollständige Ereignisdetails");
        showEventDetails.setTooltip(new Tooltip("Wenn die Option ausgewählt ist, dann wird der vollständige Ereignistext angezeigt und nicht nur ein Auszug"));
        final Glyph uncheckGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.SQUARE);
        final Glyph checkGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.CHECK_SQUARE);
        if (Session.instance().isShowHistoryEventDetails()) {
            showEventDetails.setGraphic(checkGlyph);
        } else {
            showEventDetails.setGraphic(uncheckGlyph);
        }
        showEventDetails.setStyle("-fx-alignment: CENTER-LEFT;");
        showEventDetails.setMaxWidth(Double.MAX_VALUE);
        showEventDetails.setOnMouseClicked((MouseEvent event) -> {
            final boolean value = Session.instance().isShowHistoryEventDetails();
            final boolean newValue = !value;
            Session.instance().setShowHistoryEventDetails(newValue);
            if (newValue) {
                showEventDetails.setGraphic(checkGlyph);
            } else {
                showEventDetails.setGraphic(uncheckGlyph);
            }
            historyListView.refresh();
            menuOver.hide();
        });

        Button showDeleted = new Button("Gelöschte Einträge anzeigen");
        showDeleted.setTooltip(new Tooltip("Wenn die Option ausgewählt ist, dann werden verwaiste Einträge angezeigt"));
        final Glyph uncheckGlyph2 = ResourceLoader.getGlyph(FontAwesome.Glyph.SQUARE);
        final Glyph checkGlyph2 = ResourceLoader.getGlyph(FontAwesome.Glyph.CHECK_SQUARE);
        if (Session.instance().isShowHistoryDeleted()) {
            showDeleted.setGraphic(checkGlyph2);
        } else {
            showDeleted.setGraphic(uncheckGlyph2);
        }
        showDeleted.setStyle("-fx-alignment: CENTER-LEFT;");
        showDeleted.setMaxWidth(Double.MAX_VALUE);
        showDeleted.setOnMouseClicked((MouseEvent event) -> {
            final boolean value = Session.instance().isShowHistoryDeleted();
            final boolean newValue = !value;
            Session.instance().setShowHistoryDeleted(newValue);
            if (newValue) {
                showDeleted.setGraphic(checkGlyph2);
            } else {
                showDeleted.setGraphic(uncheckGlyph2);
            }
            if (!Session.instance().isShowHistoryDeleted()) {
                facade.removeOrphanedEvents();

            } else {
                forceReload();
            }
            menuOver.hide();
        });

        Button menu = new Button();
        menu.setGraphic(getSkin().getGlyph("\uf142"));
        menu.getStyleClass().add("cpx-icon-button");

        menuOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);

        VBox menuBox = new VBox(createAction, createRequest, createDocument, createService, createReminder, showEventDetails, showDeleted);
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuOver.setContentNode(menuBox);

        menu.setOnMouseClicked((MouseEvent event) -> {
            menuOver.show(menu);
//                menu.setAlignment(Pos.TOP_RIGHT);
        });

        tbTextSearch = new ToggleButton();
        tbFilter = new ToggleButton();

        tbAllEntries = new ToggleButton();
        Tooltip ttAllEntry = new Tooltip("Zeige alle Historie-Elemente");
        ttAllEntry.setWrapText(true);
        tbAllEntries.setTooltip(ttAllEntry);
        tbAllEntries.setOnMouseEntered((MouseEvent event) -> {
            ttAllEntry.show(tbAllEntries, event.getScreenX() + 10, event.getScreenY() + 10);
        });
        tbAllEntries.setOnMouseExited((MouseEvent event) -> {
            ttAllEntry.hide();
        });

        tbKainInka = new ToggleButton();
        Tooltip ttKainInka = new Tooltip("Schnellfilter Nachrichtenaustausch Krankenkasse - Krankenhaus (KAIN/INKA)");
        ttKainInka.setWrapText(true);
        tbKainInka.setOnMouseEntered((MouseEvent event) -> {
            ttKainInka.show(tbKainInka, event.getScreenX() + 10, event.getScreenY() + 10);
        });
        tbKainInka.setOnMouseExited((MouseEvent event) -> {
            ttKainInka.hide();
        });
        //tbTextSearch.getStyleClass().add("cpx-icon-button");
        tbTextSearch.setGraphic(getSkin().getGlyph("\uf002"));
        //tbFilter.getStyleClass().add("cpx-icon-button");
        tbFilter.setGraphic(getSkin().getGlyph("\uf0b0"));

        //tbKainInka.getStyleClass().add("cpx-icon-button");
        tbKainInka.setGraphic(getSkin().getGlyph("\uf086"));

        tbAllEntries.setGraphic(getSkin().getGlyph("\uf03a"));

        registerPropertyListener(tbTextSearch.selectedProperty(),((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            LOG.log(Level.INFO, "TextSearch is selected value change from {0} to {1}", new Object[]{oldValue, newValue});
            if (tbTextSearch.isSelected()) {
                textSearchFilter.getChildren().addAll(historySecTextField);
            } else {
                HBox.clearConstraints(historySecTextField);
                textSearchFilter.getChildren().remove(historySecTextField); //.clear();
            }
        }));

        registerPropertyListener(tbFilter.selectedProperty(),((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            LOG.log(Level.INFO, "Filter is selected value change from {0} to {1}", new Object[]{oldValue, newValue});
            if (tbFilter.isSelected()) {
                textSearchFilter.getChildren().addAll(vboxFilter);
            } else {
                HBox.clearConstraints(tbFilter);
                textSearchFilter.getChildren().remove(vboxFilter);
            }
        }));

        registerPropertyListener(tbAllEntries.selectedProperty(),((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
            LOG.log(Level.INFO, "All option is selected value change from {0} to {1}", new Object[]{oldValue, newValue});
            if (tbAllEntries.isSelected()) {
                ttAllEntry.setText("zuvor konfigurierten (gespeicherten) Filter anwenden");
                historyListView.setItems(masterData);
                textSearchFilter.setDisable(true);
                tbKainInka.setDisable(true);
            } else {
                // set previous filter
                ttAllEntry.setText("Zeige alle Historie-Elemente");
                HBox.clearConstraints(tbAllEntries);
                restoreHistoryFilterAfterReload(); // set previous filter
                textSearchFilter.setDisable(false);
                tbKainInka.setDisable(false);
//                    textSearch_Filter.setVisible(true);
            }
        }));

        registerPropertyListener(tbKainInka.selectedProperty(),((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            LOG.log(Level.INFO, "KainInka Filter is selected value change from {0} to {1}", new Object[]{oldValue, newValue});
            if (tbKainInka.isSelected()) {
                ttKainInka.setText("zuvor konfigurierten (gespeicherten) Filter anwenden");
                FilteredList<HistoryEntry<? extends AbstractEntity>> kainInkaFilteredItems = masterData.filtered((HistoryEntry<? extends AbstractEntity> t) -> {
                    WmEventTypeEn eventType = t.getEvent().getEventType();
                    return eventType.equals(WmEventTypeEn.kainReceived) || eventType.equals(WmEventTypeEn.inkaStored) || eventType.equals(WmEventTypeEn.inkaSent) || eventType.equals(WmEventTypeEn.inkaCancelled);
                });
                historyListView.setItems(kainInkaFilteredItems);
// make it disabled to untouch previosly configured filter.
                textSearchFilter.setDisable(true);
                tbAllEntries.setDisable(true);
//            textSearch_Filter.setVisible(false);

            } else {
                ttKainInka.setText("Schnellfilter Nachrichtenaustausch Krankenkasse - Krankenhaus (KAIN/INKA)");
                HBox.clearConstraints(tbKainInka);
//          comboBoxEventTypes.getCheckModel().clearChecks();   // clear all checks
//restore previosly set filter.
// Note: If we click on checkComboBox after kainInka filter button, then Filters will be updated with focus lost (ComboBox.ON_HIDDEN), it means we lost previously set filters.
                restoreHistoryFilterAfterReload(); // set previous filter
                textSearchFilter.setDisable(false);
                tbAllEntries.setDisable(false);
//            textSearch_Filter.setVisible(true);
            }
        }));

        getSkin().setMenu(tbKainInka, tbAllEntries, tbFilter, tbTextSearch, menu);
    }

    private boolean isKainInkaButtonSelected() {
        return tbKainInka.isSelected();
    }

    private boolean isAllEntriesButtonSelected() {
        return tbAllEntries.isSelected();
    }

    /**
     * to create main timeline history panel as a listView.
     *
     * @return history_section complete history section as a Parent node
     */
    @Override
    protected Parent createContent() {
        long startTime = System.currentTimeMillis();
        VBox history_section = new VBox();
        history_section.getStyleClass().add("cpx-history-section");
        history_section.setSpacing(5);

        textSearchFilter = new HBox();
        textSearchFilter.setSpacing(25);
        textSearchFilter.managedProperty().bind(textSearchFilter.visibleProperty());  //Pna: 28.02.19 (to add/remove textSearch_Filter HBox based on its visible property.)
        HBox.setHgrow(textSearchFilter, Priority.ALWAYS);

        TextArea textArea = new TextArea();
        double USE_COMPUTED_HEIGTH = 5;
        textArea.setMaxHeight(USE_COMPUTED_HEIGTH);
        textArea.setText(Lang.getHistoryUpcomingEvents());
        textArea.setStyle("-fx-font-size: 20px;");

        HBox hbox_upcomingEvents = new HBox();
        HBox.setHgrow(hbox_upcomingEvents, Priority.ALWAYS);
        hbox_upcomingEvents.getChildren().add(textArea);

        historyListView = new HistoryListView() {
            @Override
            public void afterTask(Worker.State pState) {
                super.afterTask(pState);
                if (pState.equals(Worker.State.SUCCEEDED)) {
                    masterData.setAll(historyListView.getItems());
                    facade.getEventsAsObsList(getLimit()).removeListener(eventListener);
                    facade.getEventsAsObsList(getLimit()).addListener(eventListener);
                    setFilterTextField(historySecTextField);
                    restoreHistoryFilterAfterReload();
                }
            }
        };
        VBox.setVgrow(historyListView, Priority.ALWAYS);
        history_section.getChildren().addAll(textSearchFilter, historyListView);
        LOG.log(Level.FINE, "create content for workflow number " + (facade == null ? "null" : facade.getCurrentProcessNumber()) + " loaded in " + (System.currentTimeMillis() - startTime) + " ms");
        return history_section;
    }

    @Override
    public void dispose() {
        historySecTextField.textProperty().removeListener(textListener);
        facade.getEventsAsObsList(getLimit()).removeListener(eventListener);
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public HistoryEntry<? extends AbstractEntity> getSelectedItem() {
        return historyListView.getSelectionModel().getSelectedItem();
    }

    @Override
    public Parent getDetailContent() {
        HistoryEntry<? extends AbstractEntity> selectedEvent = historyListView.getSelectionModel().getSelectedItem();
        LOG.log(Level.FINEST, "draw item detail for index " + historyListView.getSelectionModel().getSelectedIndex());
        WmDetailSection detailSection = new WmHistoryDetails(facade, selectedEvent).getDetailSection();
        return detailSection.getRoot();
    }

    /**
     * creates new Action and Event Item, adds Event to observable list.
     */
    private void createNewActionEvent() {
        ItemEventHandler eh = new WmActionOperations(facade).createItem();
        if (eh != null) {
            eh.handle(null);
        }
//        CreateActionDialog dialog = new CreateActionDialog(facade);
//        dialog.showAndWait();
    }

    /**
     * creates new Reminder and Event Item, adds Event to observable list.
     */
    private void createNewReminderEvent() {
        ItemEventHandler eh = new WmReminderOperations(facade).createItem();
        if (eh != null) {
            eh.handle(null);
        }
//        AddReminderDialog dialog = new AddReminderDialog(facade);
//        dialog.showAndWait().ifPresent((ButtonType t) -> {
//            if (t.equals(ButtonType.OK)) {
////                    facade.getObsReminder().add(dialog.getReminderObject());
//            }
//        });
    }

    /**
     * creates new Request and Event Item, adds Event to observable list.
     */
    private void createNewRequestEvent() {
        ItemEventHandler eh = new WmRequestOperations(facade).createItem();
        if (eh != null) {
            eh.handle(null);
        }
//        Long hCaseId = facade.getCurrentProcess().getMainProcessCase() == null || facade.getCurrentProcess().getMainProcessCase().getHosCase() == null ? null : facade.getCurrentProcess().getMainProcessCase().getHosCase().getId();
//        if (hCaseId == null || hCaseId == 0) {
//            LOG.log(Level.SEVERE, "MainCase of the process is null or 0");
//            MainApp.showErrorMessageDialog(Lang.getProcessMainCaseError());
//            return;
////            AlertDialog alert = AlertDialog.createErrorDialog("Message", ButtonType.OK, ButtonType.CANCEL);
////            alert.setTitle(Lang.getTemplateErrorTitle());
////            alert.setHeaderText(Lang.getTemplateErrorHeader());
////            alert.setContentText(Lang.getProcessMainCaseError());
////            Optional<ButtonType> buttons = alert.showAndWait();
////            if (buttons.get() == ButtonType.OK) {
//////                AddAvailableCaseDialog dialog = new AddAvailableCaseDialog(facade);
//////                dialog.showAndWait();
////            } else {
////            }
//        }
//        AddRequestDialog dialog = null;
//        try {
//            dialog = new AddRequestDialog(facade, hCaseId); // If the mainCase of the process is null, then it throws null pointer exception.
//        } catch (CpxIllegalArgumentException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//            MainApp.showErrorMessageDialog(ex);
//            return;
//        }
//        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
//            @Override
//            public void accept(ButtonType t) {
//                //20191016 AWi: only reload process when user has created new request
//                //is it really neccessary to reload process??
//                if (ButtonType.OK.equals(t)) {
//                    //2018-06-21 DNi - Ticket CPX-1048: Solution for this error: 
//                    //java.sql.BatchUpdateException: ORA-02292: Integritäts-Constraint (CASEDB.FK_WM_EVENT4REQUEST_ID) verletzt - untergeordneter Datensatz gefunden
//                    facade.loadProcess(facade.getCurrentProcess().id, true);
//                }
//            }
//        });
    }

    /**
     * creates new Service and Event Item, adds Event to observable list.
     */
    private void createNewServiceEvent() {
//        AddServiceDialog dialog = new AddServiceDialog(facade);
//        dialog.showAndWait();
        ItemEventHandler eh = new WmServiceOverviewOperations(facade).createItem();
        if (eh != null) {
            eh.handle(null);
        }
//        wmServiceOverviewSection.createItem();
    }

    /**
     * creates new Document and Event Item, adds Event to observable list.
     */
    private void createNewDocumentEvent() {
//        File file = WmDocumentSection.showDialogWithFileChooser(getSkin().getWindow(), facade);
        ItemEventHandler eh = new WmDocumentOperations(facade).createItem();
        //wmDocumentSection.showDialogWithFileChooser(getSkin().getWindow(), facade);
        if (eh != null) {
            eh.handle(null);
        }
    }

    public ToggleButton getFilterButton() {
        return tbFilter;
    }

    public ToggleButton getTextSearchButton() {
        return tbTextSearch;
    }

    public AsyncListView<HistoryEntry<? extends AbstractEntity>> getListView() {
        return historyListView;
    }

    public void forceReload() {
        Platform.runLater(() -> {
            List<TWmEvent> allEventsForCurrentProcess = facade.getEventsForCurrentProcess();
            facade.getEventsAsObsList().setAll(FXCollections.observableArrayList(allEventsForCurrentProcess));
        });

    }

}

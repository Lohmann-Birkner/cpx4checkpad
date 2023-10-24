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
package de.lb.cpx.client.ruleeditor.model.titledPane;

import de.lb.cpx.ruleviewer.event.RefreshEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

/**
 * TitledPane to load async list view items
 *
 * @author wilde
 * @param <T> type of list content
 */
public abstract class ListViewTitledPane<T> extends AsyncMenuTitledPane<ListView<T>> {

    private ListView<T> view;
    private SearchBox searchBox = new SearchBox();
    private final Label lblCount;
    private FilteredList<T> filterList;
    private ListChangeListener<T> itemsListener = new ListChangeListener<>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends T> change) {
            lblCount.setText(String.valueOf(change.getList().size()));
        }
    };
    private ObjectProperty<T> selectedItemProperty;
    private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty;
    private final ReadOnlyBooleanWrapper showSearchBoxProperty = new ReadOnlyBooleanWrapper(false);
    private Callback<String, Predicate<T>> onSearchTitle = new Callback<String, Predicate<T>>() {
        @Override
        public Predicate<T> call(String p) {
            return (T t) -> true;
        }
    };

    public ListViewTitledPane() {
        super();
        lblCount = new Label("0");
        HBox boxCount = new HBox(new Label("("), lblCount, new Label(")"));
        boxCount.setPadding(new Insets(0, 0, 0, 5));
        boxCount.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(lblCount, Priority.ALWAYS);
//        getMenuItems().add(0, lblCount);

        showSearchBoxProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    if (!getContentNode().getChildren().contains(searchBox)) {
                        getContentNode().getChildren().add(0, searchBox);
                    }
                } else {
                    if (getContentNode().getChildren().contains(searchBox)) {
                        getContentNode().getChildren().remove(searchBox);
                    }
                }
            }
        });
        expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                setHideMenu(!t1);
                if (!(getGraphic() instanceof Pane)) {
                    return;
                }
                if (t1) {
                    ((Pane) getGraphic()).getChildren().remove(boxCount);
                } else {
                    ((Pane) getGraphic()).getChildren().add(1, boxCount);
                }
            }
        });
        addEventFilter(RefreshEvent.refreshEvent(), new EventHandler<RefreshEvent>() {
            @Override
            public void handle(RefreshEvent t) {
                refresh();
            }
        });
        setHideMenu(false);

    }

    public List<T> getItems() {
        return new ArrayList<>(view.getItems());
    }

    public void deselect() {
        selectItem(null);
    }

    public void refresh() {
        if (view != null) {
            view.refresh();
        }
    }
    @Override
    public ListView<T> loadContent() {
        view = new ListView<>();
        view.getStyleClass().add("stay-selected-list-view");
        view.itemsProperty().addListener(new ChangeListener<ObservableList<T>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<T>> ov, ObservableList<T> t, ObservableList<T> t1) {
                if (t != null) {
                    t.removeListener(itemsListener);
                }
                if (t1 != null) {
                    t1.addListener(itemsListener);
                }
            }
        });
        filterList = new FilteredList<>(FXCollections.observableArrayList(fetchList()));
        view.setItems(filterList);
        selectedItemProperty().unbind();
        selectedItemProperty().bind(view.getSelectionModel().selectedItemProperty());
//        lblCount.setText(String.valueOf(view.getItems().size()));
        updateUIDataAsync();
        view.cellFactoryProperty().bind(cellFactoryProperty());
        return view;
    }

    public abstract List<T> fetchList();

    public ObjectProperty<T> selectedItemProperty() {
        if (selectedItemProperty == null) {
            selectedItemProperty = new SimpleObjectProperty<>();
        }
        return selectedItemProperty;
    }

    public T getSelectedItem() {
        return selectedItemProperty().get();
    }

    public void selectItem(T pItem) {
        if (view != null) {
            if (pItem == null) {
                view.getSelectionModel().clearSelection();
                return;
            }
            if (view.getItems().contains(pItem)) {
                view.getSelectionModel().select(pItem);
            } else {
                view.getSelectionModel().clearSelection();
            }
        }
//        selectedItemProperty().set(pItem);
    }

    public ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        if (cellFactoryProperty == null) {
            cellFactoryProperty = new SimpleObjectProperty<>();
        }
        return cellFactoryProperty;
    }

    public Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return cellFactoryProperty().get();
    }

    public void setCellFactory(Callback<ListView<T>, ListCell<T>> pCallback) {
        cellFactoryProperty().set(pCallback);
    }

    public final ReadOnlyBooleanProperty showSearchBoxProperty() {
        return showSearchBoxProperty.getReadOnlyProperty();
    }

    public boolean isShowSearchBox() {
        return showSearchBoxProperty.get();
    }

    public void setShowSearchBox(boolean pShow) {
        showSearchBoxProperty.set(pShow);
    }

    public void setOnSearchTitle(Callback<String, Predicate<T>> pCallback) {
        onSearchTitle = pCallback;
    }

    public Callback<String, Predicate<T>> getOnSearchTitle() {
        return onSearchTitle;
    }

    private void updateUIDataAsync() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblCount.setText(String.valueOf(view.getItems().size()));
            }
        });
    }

    private class SearchBox extends HBox {

        private final TextField field;

        public SearchBox() {
            super();
            field = new TextField();
            field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    filterList.setPredicate(getOnSearchTitle().call(t1));
                }
            });
            field.setPromptText("Nach Pool-Namen suchen");
            setFillHeight(true);
            HBox.setHgrow(field, Priority.ALWAYS);
            getChildren().add(field);
        }

    }

}

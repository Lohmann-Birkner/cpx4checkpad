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
 *    2019  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.checked_combobox;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.behavior.ComboBoxListViewBehavior;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledControl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.skin.VirtualContainerBase;
import javafx.scene.input.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Default skin implementation for the {@link ComboBox} control.
 *
 * @param <T> type
 * @see ComboBox
 * @since 9
 */
public class CpxComboBoxListViewSkin<T> extends CpxComboBoxPopupControl<T> {

    /**
     * *************************************************************************
     *                                                                         *
     * Static fields * *
     * ************************************************************************
     */
    // By default we measure the width of all cells in the ListView. If this
    // is too burdensome, the developer may set a property in the ComboBox
    // properties map with this key to specify the number of rows to measure.
    // This may one day become a property on the ComboBox itself.
    private static final String COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY = "comboBoxRowsToMeasureWidth";

    /**
     * *************************************************************************
     *                                                                         *
     * Private fields * *
     * ************************************************************************
     */
    private final ComboBox<T> comboBox;
    private FilteredList<T> comboBoxItems;

    private ListCell<T> buttonCell;
    private Callback<ListView<T>, ListCell<T>> cellFactory;

    private final ListView<T> listView;
    private ObservableList<T> listViewItems;

    private boolean listSelectionLock = false;
    private boolean listViewSelectionDirty = false;

    private final ComboBoxListViewBehavior<T> behavior;

    /**
     * *************************************************************************
     *                                                                         *
     * Listeners * *
     * ************************************************************************
     */
    private boolean itemCountDirty;
    private final ListChangeListener<T> listViewItemsListener = new ListChangeListener<T>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends T> c) {
            itemCountDirty = true;
            getSkinnable().requestLayout();
        }
    };

    private final InvalidationListener itemsObserver;

    private final WeakListChangeListener<T> weakListViewItemsListener
            = new WeakListChangeListener<>(listViewItemsListener);
    private final TextField filterField;
    private final VBox box;

    /**
     * *************************************************************************
     *                                                                         *
     * Constructors * *
     * ************************************************************************
     */
    /**
     * Creates a new CpxComboBoxListViewSkin instance, installing the necessary
     * child nodes into the Control {@link Control#getChildren() children} list,
     * as well as the necessary input mappings for handling key, mouse, etc
     * events.
     *
     * @param control The control that this skin should be installed onto.
     */
    public CpxComboBoxListViewSkin(final ComboBox<T> control) {
        super(control);

        // install default input map for the control
        this.behavior = new ComboBoxListViewBehavior<>(control);

//        control.setInputMap(behavior.getInputMap());
        this.comboBox = control;
        updateComboBoxItems();

        itemsObserver = observable -> {
            updateComboBoxItems();
            updateListViewItems();
        };
        control.itemsProperty().addListener(new WeakInvalidationListener(itemsObserver));

        //hideOnClick.set(false);
        // listview for popup
        this.listView = createListView();
        this.filterField = createTextField();
        filterField.setPromptText("Suchen...");
//        final Glyph searchGraphic = ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH);
//        final Glyph clearGraphic = ResourceLoader.getGlyph(FontAwesome.Glyph.CLOSE);
        //final Button searchButton = new Button();
        //searchButton.setGraphic(searchGraphic);
        //this.box = new VBox(new HBox(filterField, searchButton), listView);
        this.box = new VBox(filterField, listView);
        //filterField.setPrefWidth(Region.USE_COMPUTED_SIZE);
        box.setPrefHeight(Region.USE_COMPUTED_SIZE);
        box.setVisible(false);
        //box.setPrefWidth(Region.USE_COMPUTED_SIZE);
        //filterField.setVisible(false);
        //box.setStyle("-fx-border-width: 2px; -fx-border-color: black;");

        //VBox.getVgrow(listView);
        //box.setAlignment(Pos.CENTER);
//        box.setPrefWidth(0.0d);
        //filterField.setPrefWidth(0.0d);
        //filterField.prefWidthProperty().bind(listView.widthProperty());
        //box.setStyle("-fx-padding: 10px");
        box.setStyle("-fx-padding: 2px;\n"
                + "-fx-background-color: -fx-box-border, -fx-control-inner-background;\n"
                + "-fx-background-insets: 0, 1;\n"
                + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 8, 0.0 , 0 , 0 );");

//        searchGraphic.setOnMouseClicked((t) -> {
//            t.consume();
//        });
//        
//        clearGraphic.setOnMouseClicked((t) -> {
//            t.consume();
//        });
//        clearGraphic.setOnMousePressed((t) -> {
//            t.consume();
//            clearFilter();
//        });
//
//        searchGraphic.setOnMousePressed((t) -> {
//            t.consume();
//        });
//
//        searchGraphic.setOnMouseReleased((t) -> {
//            t.consume();
//        });
//
//        clearGraphic.setOnMouseReleased((t) -> {
//            t.consume();
//        });
//
//        searchGraphic.setOnMouseClicked((t) -> {
//            t.consume();
//        });
//
//        clearGraphic.setOnMouseClicked((t) -> {
//            t.consume();
//        });
//
//        searchButton.setOnAction((t) -> {
//            t.consume();
//            if (searchButton.getGraphic() == searchGraphic) {
//                //
//            } else {
//                clearFilter();
//            }
//        });
//        this.filterField.setOnKeyReleased((t) -> {
//            listView.refresh();
        filterField.textProperty().addListener((Observable obs) -> {
//            Platform.runLater(() -> {
            String filter = filterField.getText() == null ? "" : filterField.getText().toLowerCase();
//                final Glyph newGraphic;
//            Callback<ListView<T>, ListCell<T>> cf = comboBox.getCellFactory();
//            comboBox.setCellFactory(null);
            final Predicate<T> pred;
            if (filter.length() == 0) {
//                newGraphic = searchGraphic;
                //comboBoxItems.setPredicate();
                pred = (T s) -> true;
            } else {
//                newGraphic = clearGraphic;
                pred = (T s) -> {
                    final String text;
                    //ListCell<T> val = cellFactory.call(listView);
                    //val.getText();
                    if (s == null) {
                        text = "";
                    } else {
                        StringConverter<T> c = getConverter();
//                        Parent p = comboBox.getParent();
//                        while (true) {
//                            if (p == null) {
//                                break;
//                            }
//                            if (p instanceof CpxCheckComboBox) {
//                                c = ((CpxCheckComboBox) p).getConverter();
//                                break;
//                            }
//                            if (p instanceof LabeledComboBox) {
//                                //Object val = ((ComboBox) ((LabeledComboBox) p).getControl()).getCellFactory().call(s);
//                                c = ((ComboBox) ((LabeledComboBox) p).getControl()).getConverter();
//                                break;
//                            }
//                            p = p.getParent();
//                        }
////                        if (comboBox.getParent() instanceof CpxCheckComboBox) {
////                            c = ((CpxCheckComboBox) comboBox.getParent()).getConverter();
////                        }
//                        if (c == null) {
//                            c = comboBox.getConverter();
//                        }
                        if (c == null) {
                            text = s.toString() == null ? "" : s.toString();
                        } else {
                            text = c.toString(s);
                        }
                    }
                    return text.toLowerCase().contains(filter);
                };
            }
            comboBoxItems.setPredicate(pred);
            dirtySelection();
            //            searchButton.setGraphic(newGraphic);
            resizeListView();
//            comboBox.setCellFactory(cf);
            filterField.requestFocus();
//            comboBox.setCellFactory(cf);
//            });
            //listView.setMinHeight(computeMinHeight(0, 0, 0, 0, 0));
            //listView.setPrefHeight(computePrefHeight(0, 0, 0, 0, 0));
            //listView.setMaxHeight(computeMaxHeight(0, 0, 0, 0, 0));
            //reconfigurePopup();
        });
        //String text = this.filterField.getText() == null ? "" : this.filterField.getText().toLowerCase();
        //Iterator<T> it = comboBox.getItems().iterator();
//            this.listViewItems.clear();
//            this.listViewItems.addAll(comboBoxItems);
//            Iterator<T> it = listViewItems.iterator();
//            if (!text.isEmpty()) {
//                while (it.hasNext()) {
//                    T item = it.next();
//                    if (!item.toString().toLowerCase().contains(text.toLowerCase())) {
//                        it.remove();
//                    }
//                }
//            }
//            for(T item: new ArrayList<>(listViewItems)) {
//                
//            }
//        });

        // Fix for RT-21207. Additional code related to this bug is further below.
        //this.listView.setManaged(false);
        //this.filterField.setManaged(false);
        this.box.setManaged(false);
        //this.filterField.setManaged(false);
        //this.listView.setManaged(false);
        getChildren().add(box);
        // -- end of fix

        updateListViewItems();
        updateCellFactory();

        updateButtonCell();
        //filterField.minWidthProperty().bind(buttonCell.widthProperty());
        //filterField.maxWidthProperty().bind(buttonCell.widthProperty());

        // Fix for RT-19431 (also tested via ComboBoxListViewSkinTest)
        updateValue();

        registerChangeListener(control.itemsProperty(), e -> {
            updateComboBoxItems();
            updateListViewItems();
        });
        registerChangeListener(control.promptTextProperty(), e -> updateDisplayNode());
        registerChangeListener(control.cellFactoryProperty(), e -> updateCellFactory());
        registerChangeListener(control.visibleRowCountProperty(), e -> {
            if (listView == null) {
                return;
            }
            listView.requestLayout();
            filterField.requestLayout();
        });
        registerChangeListener(control.converterProperty(), e -> updateListViewItems());
        registerChangeListener(control.buttonCellProperty(), e -> {
            updateButtonCell();
            updateDisplayArea();
        });
        registerChangeListener(control.valueProperty(), e -> {
            updateValue();
            control.fireEvent(new ActionEvent());
        });
        registerChangeListener(control.editableProperty(), e -> updateEditable());

        // Refer to JDK-8095306
        if (comboBox.isShowing()) {
            show();
        }
        comboBox.sceneProperty().addListener(o -> {
            if (((ObservableValue) o).getValue() == null) {
                comboBox.hide();
            }
        });
    }

    /**
     * *************************************************************************
     *                                                                         *
     * Properties * *
     * ************************************************************************
     */
    /**
     * By default this skin hides the popup whenever the ListView is clicked in.
     * By setting hideOnClick to false, the popup will not be hidden when the
     * ListView is clicked in. This is beneficial in some scenarios (for
     * example, when the ListView cells have checkboxes).
     */
    // --- hide on click
    private final BooleanProperty hideOnClick = new SimpleBooleanProperty(this, "hideOnClick", true);

    public final BooleanProperty hideOnClickProperty() {
        return hideOnClick;
    }

    public final boolean isHideOnClick() {
        return hideOnClick.get();
    }

    public final void setHideOnClick(boolean value) {
        hideOnClick.set(value);
    }

    /**
     * *************************************************************************
     *                                                                         *
     * Public API * *
     * ************************************************************************
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();

        if (behavior != null) {
            behavior.dispose();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TextField getEditor() {
        // Return null if editable is false, even if the ComboBox has an editor set.
        // Use getSkinnable() here because this method is called from the super
        // constructor before comboBox is initialized.
        return getSkinnable() != null && getSkinnable().isEditable() ? ((ComboBox) getSkinnable()).getEditor() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected StringConverter<T> getConverter() {
        StringConverter<T> c = null;
        Parent p = comboBox.getParent();
        while (true) {
            if (p == null) {
                break;
            }
            if (p instanceof CpxCheckComboBox) {
                c = ((CpxCheckComboBox<T>) p).getConverter();
                break;
            }
            if (p instanceof LabeledComboBox) {
                //Object val = ((ComboBox) ((LabeledComboBox) p).getControl()).getCellFactory().call(s);
                c = ((ComboBox<T>) ((LabeledControl<CpxComboBox<T>>) p).getControl()).getConverter();
                break;
            }
            p = p.getParent();
        }
        if (c == null) {
            c = comboBox.getConverter();
        }
        if (c == null) {
            c = ((ComboBox<T>) getSkinnable()).getConverter();
        }
        return c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getDisplayNode() {
        Node displayNode;
        if (comboBox.isEditable()) {
            displayNode = getEditableInputNode();
        } else {
            displayNode = buttonCell;
        }

        updateDisplayNode();

        return displayNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getPopupContent() {
        return box;
    }

    @Override
    public void hide() {
        super.hide();
        box.setVisible(false);
    }

    @Override
    public void show() {
//        final String text = filterField.getText();
//        final int position = filterField.getCaretPosition();
//        final IndexRange selection = filterField.getSelection();
//        //reset this to calculate the width/height of popup when combobox is reopened
//        filterField.setText(null);
        //resizeListView();
        //setPopupNeedsReconfiguring(true);
        //reconfigurePopup();
        super.show();
        //popup.prefHeightProperty().bind(box.heightProperty());
//        popup.maxHeightProperty().bind(listView.heightProperty());
//        filterField.minWidthProperty().bind(buttonCell.widthProperty());
//        filterField.maxWidthProperty().bind(buttonCell.widthProperty());
//        if (text != null) {
//            filterField.setText(text);
//            filterField.positionCaret(position);
//            filterField.selectRange(selection.getStart(), selection.getEnd());
//        }
////        LOG.log(Level.INFO, "filterField.requestFocus();");
//        filterField.requestFocus();
        box.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return 60;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double superPrefWidth = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        double listViewWidth = box.prefWidth(height);
        double pw = Math.max(superPrefWidth, listViewWidth);
//        LOG.log(Level.INFO, "prefWidth: " + pw);

        reconfigurePopup();

        return pw;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return super.computeMaxWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void layoutChildren(final double x, final double y,
            final double w, final double h) {
        if (listViewSelectionDirty) {
            dirtySelection();
        }

        super.layoutChildren(x, y, w, h);
    }

    private void dirtySelection() {
        try {
            listSelectionLock = true;
            T item = comboBox.getSelectionModel().getSelectedItem();
            //listView.getSelectionModel().select(-1);
            listView.getSelectionModel().clearSelection();
            listView.getSelectionModel().select(item);
            comboBox.getSelectionModel().select(item);
            comboBox.setValue(item);
        } finally {
            listSelectionLock = false;
            listViewSelectionDirty = false;
        }
    }

//    Switcher switcher = null;
    /**
     * *************************************************************************
     *                                                                         *
     * Private methods * *
     * ************************************************************************
     */
    /**
     * {@inheritDoc}
     */
    @Override
    void updateDisplayNode() {
//        if (switcher != null) {
//            return;
//        }
//        switcher = new Switcher();
//        switcher.showAll();
        if (getEditor() != null) {
            super.updateDisplayNode();
            //superUpdateDisplayNode();
        } else {
            T value = comboBox.getValue();
//            T value = getRecentValue();
            int index = getIndexOfComboBoxValueInItemsList();
            if (index > -1) {
                buttonCell.setItem(null);
                buttonCell.updateIndex(index);
            } else {
                // RT-21336 Show the ComboBox value even though it doesn't
                // exist in the ComboBox items list (part two of fix)
                buttonCell.updateIndex(-1);
                boolean empty = updateDisplayText(buttonCell, value, false);

                // Note that empty boolean collected above. This is used to resolve
                // RT-27834, where we were getting different styling based on whether
                // the cell was updated via the updateIndex method above, or just
                // by directly updating the text. We fake the pseudoclass state
                // for empty, filled, and selected here.
                buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_EMPTY, empty);
                buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_FILLED, !empty);
                buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
            }
        }
//        switcher.reset();
//        switcher = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComboBoxBaseBehavior<T> getBehavior2() {
        return behavior;
    }

//    private void updateComboBoxItems() {
//        comboBoxItems = new FilteredList<>(comboBox.getItems());
//        //comboBoxItems = comboBoxItems == null ? FXCollections.<T>emptyObservableList() : comboBoxItems;
//        comboBoxItems = comboBoxItems == null ? new FilteredList<>(FXCollections.<T>emptyObservableList()) : comboBoxItems;
//    }
    private void updateComboBoxItems() {
//        List<T> backingList = getBackingList(comboBox.getItems());
        if (comboBox.getItems() instanceof FilteredList) {
            comboBoxItems = (FilteredList<T>) comboBox.getItems();
        } else {
            comboBoxItems = new FilteredList<>(comboBox.getItems() == null ? FXCollections.<T>emptyObservableList() : comboBox.getItems());
        }
        //comboBoxItems = comboBoxItems == null ? new FilteredList<>(FXCollections.<T>emptyObservableList()) : comboBoxItems;
//        if (comboBox.getItems().containsAll(comboBox.getItems())) {
//            //2019-02-15 DNi - Ticket CPX-1430: Added to avoid StackOverflowException
//            return;
//        }
        comboBox.setItems(comboBoxItems);
    }

    private void updateListViewItems() {
        if (listViewItems != null) {
            listViewItems.removeListener(weakListViewItemsListener);
        }

        this.listViewItems = comboBoxItems;
        listView.setItems(listViewItems);

        if (listViewItems != null) {
            listViewItems.addListener(weakListViewItemsListener);
        }

        itemCountDirty = true;
        getSkinnable().requestLayout();
    }

    private void updateValue() {
        T newValue = comboBox.getValue();

        SelectionModel<T> listViewSM = listView.getSelectionModel();

        // RT-22386: We need to test to see if the value is in the comboBox
        // items list. If it isn't, then we should clear the listview
        // selection
        final int indexOfNewValue = getIndexOfComboBoxValueInItemsList();

        if (newValue == null && indexOfNewValue == -1) {
            listViewSM.clearSelection();
        } else {
            if (indexOfNewValue == -1) {
                listSelectionLock = true;
                listViewSM.clearSelection();
                listSelectionLock = false;
            } else {
                int index = comboBox.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < comboBoxItems.size()) {
                    T itemsObj = comboBoxItems.get(index);
                    if ((itemsObj != null && itemsObj.equals(newValue)) || (itemsObj == null && newValue == null)) {
                        listViewSM.select(index);
                    } else {
                        listViewSM.select(newValue);
                    }
                } else {
                    // just select the first instance of newValue in the list
                    int listViewIndex = comboBoxItems.indexOf(newValue);
                    if (listViewIndex == -1) {
                        // RT-21336 Show the ComboBox value even though it doesn't
                        // exist in the ComboBox items list (part one of fix)
                        updateDisplayNode();
                    } else {
                        listViewSM.select(listViewIndex);
                    }
                }
            }
        }
    }

    // return a boolean to indicate that the cell is empty (and therefore not filled)
    private boolean updateDisplayText(ListCell<T> cell, T item, boolean empty) {
        if (empty) {
            if (cell == null) {
                return true;
            }
            cell.setGraphic(null);
            cell.setText(null);
            return true;
        } else if (item instanceof Node) {
            Node currentNode = cell.getGraphic();
            Node newNode = (Node) item;
            if (currentNode == null || !currentNode.equals(newNode)) {
                cell.setText(null);
                cell.setGraphic(newNode);
            }
            return newNode == null;
        } else {
            // run item through StringConverter if it isn't null
            //final StringConverter<T> c = comboBox.getConverter();
            //comboBox.setConverter(this.getConverter());
            if (item instanceof String && ((String) item).trim().isEmpty()) {
                return true;
            }
            final StringConverter<T> c = comboBox.getConverter();
            final String promptText = comboBox.getPromptText();
            String s = item == null && promptText != null ? promptText
                    : c == null ? (item == null ? null : item.toString()) : c.toString(item);
            cell.setText(s);
            cell.setGraphic(null);
            return s == null || s.isEmpty();
        }
    }

    private int getIndexOfComboBoxValueInItemsList() {
        T value = comboBox.getValue();
        int index = comboBoxItems.indexOf(value);
        return index;
    }

    private void updateButtonCell() {
        buttonCell = comboBox.getButtonCell() != null
                ? comboBox.getButtonCell() : getDefaultCellFactory().call(listView);
        buttonCell.setMouseTransparent(true);
        buttonCell.updateListView(listView);

        // As long as the screen-reader is concerned this node is not a list item.
        // This matters because the screen-reader counts the number of list item
        // within combo and speaks it to the user.
        buttonCell.setAccessibleRole(AccessibleRole.NODE);
    }

    private void updateCellFactory() {
        Callback<ListView<T>, ListCell<T>> cf = comboBox.getCellFactory();
        cellFactory = cf != null ? cf : getDefaultCellFactory();
        listView.setCellFactory(cellFactory);
    }

    private Callback<ListView<T>, ListCell<T>> getDefaultCellFactory() {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override
            public ListCell<T> call(ListView<T> listView) {
                return new ListCell<T>() {
                    @Override
                    public void updateItem(T item, boolean empty) {
//                        String text = filterField.getText() == null ? "" : filterField.getText().toLowerCase();
//                        if (!text.isEmpty() && (item == null || item.toString() == null || !item.toString().toLowerCase().contains(text))) {
//                            super.updateItem(null, true);
//                        } else {
                        super.updateItem(item, empty);
                        updateDisplayText(this, item, empty);
//                        }
                    }
                };
            }
        };
    }

    private TextField createTextField() {
        final TextField _textField = new TextField() {
            @Override
            protected double computePrefWidth(double height) {
                return listView.getPrefWidth();
            }
        };
        _textField.setOnKeyPressed(t -> {
            // TODO move to behavior, when (or if) this class becomes a SkinBase
//            if (t.getCode() == KeyCode.ENTER
//                    || t.getCode() == KeyCode.SPACE
//                    || t.getCode() == KeyCode.ESCAPE) {
            if (t.getCode() == KeyCode.ESCAPE) {
                comboBox.hide();
            } else if (t.getCode() == KeyCode.DOWN) {
                if (!listView.getItems().isEmpty()) {
                    listView.getSelectionModel().select(0);
                }
                listView.requestFocus();
            } else if (t.getCode() == KeyCode.PAGE_DOWN) {
                listView.requestFocus();
            }
        });
        return _textField;
    }

    private ListView<T> createListView() {
        final ListView<T> _listView = new ListView<T>() {

            {
                getProperties().put("selectFirstRowByDefault", false);
            }

            @Override
            protected double computeMinHeight(double width) {
                return 30;
            }

            @Override
            protected double computePrefWidth(double height) {
                double pw;
                if (getSkin() instanceof ListViewSkin) {
                    ListViewSkin<?> skin = (ListViewSkin<?>) getSkin();
                    if (itemCountDirty) {
                        //skin.updateItemCount();
                        skinUpdateItemCount(skin);
                        itemCountDirty = false;
                    }

                    int rowsToMeasure = -1;
                    if (comboBox.getProperties().containsKey(COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY)) {
                        rowsToMeasure = (Integer) comboBox.getProperties().get(COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY);
                    }

                    //pw = Math.max(comboBox.getWidth(), skin.getMaxCellWidth(rowsToMeasure) + 30);
                    pw = Math.max(comboBox.getWidth(), skinGetMaxCellWidth(skin, rowsToMeasure) + 30);
                } else {
                    pw = Math.max(100, comboBox.getWidth());
                }

                // need to check the ListView pref height in the case that the
                // placeholder node is showing
                if (getItems().isEmpty() && getPlaceholder() != null) {
                    pw = Math.max(super.computePrefWidth(height), pw);
                }

                return Math.max(50, pw);
            }

            @Override
            protected double computePrefHeight(double width) {
                return getListViewPrefHeight();
            }
        };

        _listView.setId("list-view");
        _listView.placeholderProperty().bind(comboBox.placeholderProperty());
        _listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _listView.setFocusTraversable(false);

//        ObjectProperty<KeyCode> recentKey = new SimpleObjectProperty<>();
//        _listView.getSelectionModel().selectedIndexProperty().addListener(o -> {
//            if (listSelectionLock) {
//                return;
//            }
//            int index = listView.getSelectionModel().getSelectedIndex();
//            comboBox.getSelectionModel().select(index);
//            updateDisplayNode();
//            comboBox.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
//            //lastSelectedIndex.set(index);
//        });
        _listView.getSelectionModel().selectedIndexProperty().addListener(o -> {
            if (listSelectionLock) {
                return;
            }
//            LOG.log(Level.INFO, "_listView.getSelectionModel().selectedIndexProperty().addListener");
            int index = listView.getFocusModel().getFocusedIndex();
            //int index = listView.getSelectionModel().getSelectedIndex();
            //LOG.log(Level.INFO, "comboBox.getSelectionModel().select(" + index + ") ...");
            double width = box.getWidth();
            comboBox.getSelectionModel().select(index);
            updateDisplayNode();
            if (width > 0.0d) {
                box.setPrefWidth(width);
            }
            comboBox.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
            //lastSelectedIndex.set(index);
        });

        comboBox.getSelectionModel().selectedItemProperty().addListener(o -> {
            //LOG.log(Level.INFO, "comboBox.getSelectionModel().selectedItemProperty().addListener");
            listViewSelectionDirty = true;
//            T item = listView.getSelectionModel().getSelectedItem();
//            listView.getSelectionModel().select(item);
//            comboBox.getSelectionModel().select(item);
//            updateDisplayNode();
        });

        _listView.addEventFilter(MouseEvent.MOUSE_RELEASED, t -> {
            // RT-18672: Without checking if the user is clicking in the
            // scrollbar area of the ListView, the comboBox will hide. Therefore,
            // we add the check below to prevent this from happening.
            EventTarget target = t.getTarget();
            if (target instanceof Parent) {
                List<String> s = ((Styleable) target).getStyleClass();
                if (s.contains("thumb")
                        || s.contains("track")
                        || s.contains("decrement-arrow")
                        || s.contains("increment-arrow")) {
                    return;
                }
            }

            if (isHideOnClick()) {
                comboBox.hide();
            }
        });

        _listView.setOnKeyPressed(t -> {
            // TODO move to behavior, when (or if) this class becomes a SkinBase
            if (t.getCode() == KeyCode.ENTER
                    || t.getCode() == KeyCode.SPACE) {
                if (isHideOnClick()) {
                    comboBox.hide();
                }
            } else if (t.getCode() == KeyCode.ESCAPE) {
                comboBox.hide();
            } else if (t.getCode().getCode() >= 'a' && t.getCode().getCode() <= 'z'
                    || t.getCode().getCode() >= 'A' && t.getCode().getCode() <= 'Z'
                    || t.getCode().getCode() >= '0' && t.getCode().getCode() <= '9'
                    || t.getCode().getCode() >= 'Ä' || t.getCode().getCode() >= 'Ü' || t.getCode().getCode() >= 'Ö'
                    || t.getCode().getCode() >= 'ä' || t.getCode().getCode() >= 'ü' || t.getCode().getCode() >= 'ö' || t.getCode().getCode() >= 'ß') {
                filterField.requestFocus();
                t.consume();
            } else if (t.getCode() == KeyCode.BACK_SPACE || t.getCode() == KeyCode.LEFT || t.getCode() == KeyCode.RIGHT) {
                filterField.requestFocus();
            }
            //            else if (t.getCode() == KeyCode.DOWN) {
            //                if (comboBox.getSelectionModel().getSelectedIndex() >= comboBox.getItems().size() - 1) {
            //                    filterField.requestFocus();
            //                    //listView.getSelectionModel().select(0);
            //                    //comboBox.getSelectionModel().select(0);
            //                    t.consume();
            //                }
            //            } 
//            else if (t.getCode() == KeyCode.UP) {
//                if (comboBox.getSelectionModel().getSelectedIndex() <= 0) {
//                    filterField.requestFocus();
//                    t.consume();
//                }
//            }
//            recentKey.set(t.getCode());
            //            else {
            //                t.consume();
            //                if (t.getCode() == KeyCode.DOWN) {
            //                    int index = listView.getSelectionModel().getSelectedIndex();
            //                    if (index < comboBox.getItems().size()) {
            //                        comboBox.getSelectionModel().select(index + 1);
            //                        return;
            //                    }
            //                    comboBox.getSelectionModel().select(index);
            //                }
            //                if (t.getCode() == KeyCode.UP) {
            //                    int index = listView.getSelectionModel().getSelectedIndex();
            //                    if (index > 0) {
            //                        comboBox.getSelectionModel().select(index - 1);
            //                        return;
            //                    }
            //                    comboBox.getSelectionModel().select(index);
            //                }
            //            }
            {

            }
        });
        _listView.getStyleClass().add("stay-selected-list-view");

        return _listView;
    }

    private double getListViewPrefHeight() {
        double ph;
        if (listView.getSkin() instanceof VirtualContainerBase) {
            int maxRows = comboBox.getVisibleRowCount();
            VirtualContainerBase<?, ?> skin = (VirtualContainerBase<?, ?>) listView.getSkin();
//            //ph = skin.getVirtualFlowPreferredHeight(maxRows);
            ph = skinGetVirtualFlowPreferredHeight(skin, maxRows);
        } else {
            double ch = comboBoxItems.size() * 25d;
            ph = Math.min(ch, 200);
        }

        return ph;
    }

    public ListView<T> getListView() {
        return listView;
    }

    public VBox getBox() {
        return box;
    }

    public TextField getFilterField() {
        return filterField;
    }

    /**
     * ************************************************************************
     *
     * API for testing
     *
     ************************************************************************
     */
    /**
     * *************************************************************************
     *                                                                         *
     * Stylesheet Handling * *
     * ************************************************************************
     */
    // These three pseudo class states are duplicated from Cell
    private static final PseudoClass PSEUDO_CLASS_SELECTED
            = PseudoClass.getPseudoClass("selected");
    private static final PseudoClass PSEUDO_CLASS_EMPTY
            = PseudoClass.getPseudoClass("empty");
    private static final PseudoClass PSEUDO_CLASS_FILLED
            = PseudoClass.getPseudoClass("filled");

    /**
     * {@inheritDoc}
     */
    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_ITEM: {
                if (comboBox.isShowing()) {
                    /* On Mac, for some reason, changing the selection on the list is not
                     * reported by VoiceOver the first time it shows.
                     * Note that this fix returns a child of the PopupWindow back to the main
                     * Stage, which doesn't seem to cause problems.
                     */
                    return listView.queryAccessibleAttribute(attribute, parameters);
                }
                return null;
            }
            case TEXT: {
                String accText = comboBox.getAccessibleText();
                if (accText != null && !accText.isEmpty()) {
                    return accText;
                }
                String title = comboBox.isEditable() ? getEditor().getText() : buttonCell.getText();
                if (title == null || title.isEmpty()) {
                    title = comboBox.getPromptText();
                }
                return title;
            }
            case SELECTION_START:
                return (getEditor() != null) ? getEditor().getSelection().getStart() : null;
            case SELECTION_END:
                return (getEditor() != null) ? getEditor().getSelection().getEnd() : null;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

//    private void updateEditable() {
//        try {
//            Method m = ComboBoxPopupControl.class.getDeclaredMethod("updateEditable");
//            m.setAccessible(true);
//            m.invoke(this, null);
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//            Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    private void updateDisplayArea() {
        try {
            Method m = ComboBoxPopupControl.class.getDeclaredMethod("updateDisplayArea");
            m.setAccessible(true);
            m.invoke(this, new Object[0]);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private TextField getEditableInputNode() {
//        try {
//            Method m = ComboBoxPopupControl.class.getDeclaredMethod("getEditableInputNode");
//            m.setAccessible(true);
//            return (TextField) m.invoke(this, null);
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//            Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    private void reconfigurePopup() {
//        try {
//            Method m = ComboBoxPopupControl.class.getDeclaredMethod("reconfigurePopup");
//            m.setAccessible(true);
//            m.invoke(this, null);
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//            Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    private void superUpdateDisplayNode() {
//        try {
//            Method m = ComboBoxPopupControl.class.getDeclaredMethod("updateDisplayNode");
//            m.setAccessible(true);
//            m.invoke(this, null);
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//            Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    private List<T> getBackingList(ObservableList<T> pList) {
//        if (pList instanceof ObservableListWrapper) {
//            ObservableListWrapper<T> wrapper = (ObservableListWrapper<T>) comboBox.getItems();
//            try {
//                Field field = ObservableListWrapper.class.getDeclaredField("backingList");
//                field.setAccessible(true);
//                return (List<T>) field.get(wrapper);
//            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
//                Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return null;
//    }
    private void skinUpdateItemCount(ListViewSkin<?> skin) {
        try {
            Method m = skin.getClass().getDeclaredMethod("updateItemCount");
            m.setAccessible(true);
            m.invoke(skin, new Object[0]);
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        } catch (Exception ex) {
            Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private double skinGetMaxCellWidth(ListViewSkin<?> skin, final int pWidth) {
        try {
            Method m = skin.getClass().getSuperclass().getDeclaredMethod("getMaxCellWidth", int.class);
            m.setAccessible(true);
            return (Double) m.invoke(skin, pWidth);
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        } catch (Exception ex) {
            Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0d;
    }

    private double skinGetVirtualFlowPreferredHeight(VirtualContainerBase<?, ?> skin, final int pMaxRows) {
        try {
            Method m = skin.getClass().getSuperclass().getDeclaredMethod("getVirtualFlowPreferredHeight", int.class);
            m.setAccessible(true);
            return (Double) m.invoke(skin, pMaxRows);
            //} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        } catch (Exception ex) {
            Logger.getLogger(CpxComboBoxListViewSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0d;
    }

//    final class Switcher {
//
//        private String text;
//        private int position;
//        private IndexRange selection;
//
//        public void showAll() {
//            text = filterField.getText();
//            position = filterField.getCaretPosition();
//            selection = filterField.getSelection();
////        //reset this to calculate the width/height of popup when combobox is reopened
//            filterField.setText(null);
//        }
//
//        public void reset() {
//            if (text != null) {
//                filterField.setText(text);
//                filterField.positionCaret(position);
//                filterField.selectRange(selection.getStart(), selection.getEnd());
//            }
//        }
//
//    }
//    public T getValue() {
//        final String text = filterField.getText();
//        final int position = filterField.getCaretPosition();
//        final IndexRange selection = filterField.getSelection();
//        //reset this to calculate the width/height of popup when combobox is reopened
//        filterField.setText(null);
//        T value = comboBox.getValue();
//        if (text != null) {
//            filterField.setText(text);
//            filterField.positionCaret(position);
//            filterField.selectRange(selection.getStart(), selection.getEnd());
//        }
//        return value;
//    }
    private void resizeListView() {
//        for (int i = 1; i <= 2; i++) {
        Platform.runLater(() -> {
//            Pane pane = (Pane) box.getParent().getParent();
//            pane.requestLayout();
            box.requestLayout();
            //this.setPopupNeedsReconfiguring(true);
            //reconfigurePopup();
            //listView.setStyle("-fx-background-color: yellow");
            //box.setStyle("-fx-background-color: red");
            double width = box.getWidth();
            listView.autosize();
            box.autosize();
            if (width > 0.0d) {
                box.setPrefWidth(width);
            }
//            popup.setMaxHeight(listView.getHeight());
//            popup.setMinHeight(listView.getHeight());
//            popup.setPrefHeight(listView.getHeight());
            listView.scrollTo(0);
//            popup.getScene().getWindow();
//                listView.requestLayout();
//                listView.refresh();
//                listView.requestLayout();
//                getSkinnable().requestLayout();
//                listView.getParent().requestLayout();
//
//                listView.requestLayout();
//                listView.refresh();
//                reconfigurePopup();
            //listView.requestLayout();
            //getSkinnable().requestLayout();
            //listView.getParent().requestLayout();

//                listView.getParent().requestLayout();
//                getSkinnable().requestLayout();
//                listView.requestLayout();
//                listView.refresh();
        });
//        }
    }

//    private void clearFilter() {
//        //clear filter
//        filterField.setText(null);
//        filterField.requestFocus();
//        resizeListView();
//    }
}

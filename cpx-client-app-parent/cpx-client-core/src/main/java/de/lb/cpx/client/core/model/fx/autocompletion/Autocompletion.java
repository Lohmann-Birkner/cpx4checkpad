/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.autocompletion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

/**
 *
 * @author niemeier
 * @param <T> type
 * @param <C> type
 */
public class Autocompletion<T extends Serializable, C extends Control & AutocompletionMultiColInterface<T>> {

    //Local variables
    //entries to autocomplete
    private final ObjectProperty<List<T>> entriesProperty = new SimpleObjectProperty<>(new ArrayList<>());
    //popup GUI
    private final ContextMenu entriesPopup = new ContextMenu();
    private Callback<T, String[]> splitHandler;
    private EventHandler<? super KeyEvent> filterTextChangeHandler;
    private EventHandler<? super Event> selectEntryHandler;
//    private MenuItem selectedItem;
    private ObjectProperty<String[]> titlesProperty = new SimpleObjectProperty<>(new String[0]);
    private IntegerProperty maxEntriesProperty = new SimpleIntegerProperty(0);
    private ReadOnlyObjectWrapper<T> selectedItemProperty = new ReadOnlyObjectWrapper<>(null);
    private IntegerProperty minFilterTextLengthProperty = new SimpleIntegerProperty(0);
    private Callback<T, List<? extends Node>> itemHandler;
    private final C control;

    public Autocompletion(C pControl) {
        super();
        control = pControl;
        setListener();
    }

    public C getControl() {
        return control;
    }

    public ObjectProperty<List<T>> entriesProperty() {
        return entriesProperty;
    }

//    private AutocompletionMultiColUtil() {
//        //utility class needs no public constructor
//    }
    public ReadOnlyObjectProperty<T> selectedItemProperty() {
        return selectedItemProperty.getReadOnlyProperty();
    }

    /**
     * search will only fire when this minimum of characters was given by the
     * user in text field
     *
     * @param pMinFilterTextLength minimum amount of characters before execute
     * search
     */
    public void setMinFilterTextLength(final int pMinFilterTextLength) {
        minFilterTextLengthProperty.set(pMinFilterTextLength);
    }

    /**
     * gives minimum of characters before search is executed
     *
     * @return search will only fire when this minimum of characters was given
     * by the user in text field
     */
    public int getMinFilterTextLength() {
        return minFilterTextLengthProperty.get();
    }

    public IntegerProperty minFilterTextLengthProperty() {
        return minFilterTextLengthProperty;
    }

    public ObjectProperty<String[]> titlesProperty() {
        return titlesProperty;
    }

    public IntegerProperty maxEntriesProperty() {
        return maxEntriesProperty;
    }

    /**
     * gets maximum number of entries in popup
     *
     * @return maximum amount of entries to be shown
     */
    public int getMaxEntries() {
        return maxEntriesProperty.get();
    }

    /**
     * starts search immediately
     */
    public void startSearch() {
        startSearch(null);
    }

    protected void startSearch(KeyEvent event) {
        //String text = StringUtils.trimToEmpty(getText());
        String text = control.getText2();
        if (filterTextChangeHandler != null) {
            if (minFilterTextLengthProperty.get() <= 0 || (text.length() >= minFilterTextLengthProperty.get())) {
                filterTextChangeHandler.handle(event);
            }
        }

        if (!text.isEmpty()) {
            showPopup(text, 0, 0);
        } else {
            getEntriesPopup().hide();
            entriesProperty.get().clear();
        }
    }

    /**
     * "Suggestion" specific listeners
     */
    protected void setListener() {
        control.setListener();
    }

//    /**
//     * "Suggestion" specific listeners
//     */
//    private void setListener() {
//        //Hide always by focus-in (optional) and out
////        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
////            entriesPopup.hide();
////        });
////
////        addEventHandler(EventType.ROOT, (event) -> {
////            //System.out.println("FOCUS!");
////            //System.out.println(event.getTarget());
////            event.get
////        });
//
//        setOnKeyReleased((KeyEvent event) -> {
////            if (returnHandler != null && event.getCode() == KeyCode.RIGHT) {
////                returnHandler.handle(event);
////            }
//            if (isPopupShowing() && (event.getCode() == KeyCode.BACK_SPACE)) {
//                entriesPopup.hide();
//                //return; //activate this to deactivate search on backspace key
//            }
//
//            if ((isPopupShowing() && (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP
//                    || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT))
//                    || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
//                return;
//            }
//
////            if (isEntriesPopupShowing() && event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
////                entriesPopup.requestFocus();
////            }
////            if ((isEntriesPopupShowing() && (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP))
////                    || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
////                return;
////            }
//            startSearch(event);
//        });
//    }
    /**
     * fires when text input has changed
     *
     * @param pFilterTextChangeHandler change handler
     */
    public void setOnFilterTextChanged(final EventHandler<? super KeyEvent> pFilterTextChangeHandler) {
        filterTextChangeHandler = pFilterTextChangeHandler;
    }

    /**
     * fires when an entry from popup is selected
     *
     * @param pSelectEntryHandler selection handler
     */
    public void setOnSelectEntry(final EventHandler<? super Event> pSelectEntryHandler) {
        selectEntryHandler = pSelectEntryHandler;
    }

    protected static List<String[]> fixWidths(List<String[]> res) {
        List<String[]> finalResults = new LinkedList<>();

        double max = 0;
        for (String[] re : res) {
            Text t = new Text();
            for (String s : re) {
                t.setText(t.getText() == null ? "" : t.getText() + " " + s);
            }
            double d = t.getLayoutBounds().getWidth();
            if (d > max) {
                max = d;
            }
        }
        for (String[] re1 : res) {
            finalResults.add(re1);
        }

        return finalResults;
    }

    /**
     * sets maximum number of entries in popup
     *
     * @param pMaxEntries maximum amount of entries to be shown
     */
    public void setMaxEntries(final int pMaxEntries) {
        maxEntriesProperty.set(pMaxEntries);
    }

    /**
     * Populate the entry set with the given search results. Display is limited
     * to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<T> searchResult, String searchRequest, int position) {
        final List<String[]> values = new ArrayList<>();
        boolean error = false;
        final boolean hasMoreResults = !searchResult.isEmpty() && searchResult.size() == maxEntriesProperty.get();
        if (!searchResult.isEmpty()) {
            values.add(titlesProperty.get() == null ? new String[0] : titlesProperty.get());
            for (int i = 0; i < searchResult.size() - (hasMoreResults ? 1 : 0); i++) {
                T item = searchResult.get(i);
                if (item != null) {
                    final String sa[];
                    if (splitHandler != null) {
                        sa = splitHandler.call(item);
                    } else {
                        sa = (String.valueOf(item)).split(";", -1);
                    }
                    values.add(sa);
                }
            }
        } else {
            error = true;
            getEntriesPopup().hide();
            if (filterTextChangeHandler != null) {
                if (minFilterTextLengthProperty.get() > 0 && searchRequest.length() < minFilterTextLengthProperty.get()) {
                    values.add(new String[]{"mindestens " + minFilterTextLengthProperty.get() + " Zeichen erforderlich"});
                } else {
                    //values.add(new String[]{"kein", "Ergebnis"});
                    values.add(new String[]{"kein Ergebnis"});
                }
            }
        }

        List<String[]> fixedSearchResult = fixWidths(values);
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        //int maxEntries = 10;
        int count = Math.max(Math.min(fixedSearchResult.size(), maxEntriesProperty().get() + 1), 0);
        //Build list as set of labels
        for (int i = 0; i < count; i++) {
            final String[] result = fixedSearchResult.get(i);
            //label with graphic (text flow) to highlight founded subtext in suggestions
            Label entryLabel = new Label();
            final TextFlow textFlow;
            if (i > 0 && result[0].toLowerCase().contains(searchRequest.toLowerCase())) {
                textFlow = buildTextFlow(false, result[0], searchRequest);
            } else {
                textFlow = buildTextFlow(true, result[0], null);
            }
            final double prefWidthSize = 90d;
            List<TextFlow> textFlowList = new ArrayList<>();
            textFlowList.add(textFlow);
            for (int j = 1; j < result.length; j++) {
                String txt = result[j];
                txt = ensureMaxLength(txt);
                Text text2 = new Text(txt);
                TextFlow textFl = new TextFlow(text2);
                //text2.setWrappingWidth(Region.USE_COMPUTED_SIZE);
                //textFlow.setPrefWidth(Region.USE_COMPUTED_SIZE);
                //((Text) textFl.getChildren().get(0))
                textFlowList.add(textFl);
            }

            HBox box = new HBox();
            for (TextFlow fl : textFlowList) {
                fl.setPrefWidth(prefWidthSize);
                //fl.setMaxHeight(10d);
                //fl.setMinWidth(Region.USE_PREF_SIZE);
                if (i == 0) {
                    fl.setStyle("-fx-font-weight: bold;");
                    if (error) {
                        fl.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    }
                    //fl.setPadding(new Insets(10d));
                }
                box.getChildren().add(fl);
            }

            if (itemHandler != null) {
                int k = i - 1;
                if (k >= 0) {
                    T dto = searchResult.get(k);
                    box.getChildren().addAll(itemHandler.call(dto));
                }
            }

//            entryLabel.setText(result[0]);
            entryLabel.setGraphic(box);
            entryLabel.setPrefHeight(10);  //don't sure why it's changed with "graphic"
            //final CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            final CustomMenuItem item;
            if (i == 0) {
                item = new CustomHeaderMenuItem(entryLabel, false);
            } else {
                item = new CustomMenuItem(entryLabel, true);
            }

            Map<HBox, CustomMenuItem> items = new HashMap<>();
            items.put(box, item);

//            item.getContent().focusedProperty().addListener((observable, oldValue, newValue) -> {
//                if (newValue) {
//                    System.out.println("FOCUS!");
//                }
//            });
            menuItems.add(item);
            if (i == 0) {
                //item.setDisable(true);
                //item.setStyle("-fx-background-color: white; -fx-text-fill: black;");
//                Class[] params = new Class[1];
//                params[0] = Direction.class;
//                try {
//                    //Method[] allMethods = item.getClass().getDeclaredMethods();
//                    Method m = item.getClass().getMethod("impl_traverse", params);
//                    m.invoke(item, Direction.NEXT);
////                for (Method m : allMethods) {
////                    String mname = m.getName();
////                    if (mname.startsWith("traverse")) {
////                        m.setAccessible(true);
////                        try {
////                            m.invoke(item, new Object[]{item, Direction.NEXT});
////                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
////                            LOG.log(Level.SEVERE, null, ex);
////                        }
////                    }
////                }
//                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//                    LOG.log(Level.SEVERE, null, ex);
//                }
            }

//            //if any suggestion is select set it into text and close popup
//            String correctedString = "";
//            if (getText().contains(" ")) {
//                correctedString = getText().substring(0, getText().lastIndexOf(' ')) + " ";
//            } else {
//                correctedString = "";
//            }
            item.setOnAction((ActionEvent actionEvent) -> {
                int index = getEntriesPopup().getItems().indexOf(item);
                if (index <= 0) {
                    return;
                }

//                if (index < searchResult.size()) {
//                    return;
//                }
                T dto = searchResult.get(index - 1);
                selectedItemProperty.set(dto);

                control.selectItem(result, position);
                getEntriesPopup().hide();

                if (selectEntryHandler != null) {
                    selectEntryHandler.handle(actionEvent);
                }
            });

//            box.setOnMouseEntered((event) -> {
//                HBox hBox = (HBox) event.getTarget();
//                CustomMenuItem item2 = items.get(hBox);
//                int index = entriesPopup.getItems().indexOf(item2);
//                if (index > 0) {
//                    T dto = searchResult.get(index - 1);
//                    itemHandler.accept(item, dto);
//                }
//            });
//            if (itemHandler != null) {
//                int index = entriesPopup.getItems().indexOf(item);
//                if (index > 0) {
//                    T dto = searchResult.get(index - 1);
//                    itemHandler.accept(item, dto);
//                }
//            }
//            for (int k = 0; k < buttons.size(); k++) {
//                Consumer<T, Button> btnFunction = buttons.get(k);
//                btnFunction.
////                Button btn = buttons.get(k);
////                box.getChildren().add(btn);
////                Callback<T, ?> btnAction = buttonActions.get(k);
////                btn.setOnAction((event) -> {
////                    event.consume();
////                    if (btnAction != null) {
////                        int index = entriesPopup.getItems().indexOf(item);
////                        T dto = searchResult.get(index - 1);
////                        selectedItem = dto;
////                        btnAction.call(selectedItem);
////                    }
////                });
//            }
//            item.addEventHandler(KeyEvent.KEY_PRESSED,
//                                 new EventHandler<KeyEvent>() {
//                                     public void handle(
//                                         final KeyEvent keyEvent) {
//                                         if (keyEvent.getCode() == KeyCode.TAB) {
//                                             System.out.println("Tap is pressed!!");
//                                         }
//                                     }
//                                 });
        }

        if (hasMoreResults) {
            final Label hasMoreLabel = new Label("(es gibt weitere Ergebnisse)");
            hasMoreLabel.setStyle("-fx-font-style: italic;");
            hasMoreLabel.setAlignment(Pos.CENTER);
            HBox box = new HBox(hasMoreLabel);
            HBox.setHgrow(hasMoreLabel, Priority.ALWAYS);
            box.setAlignment(Pos.CENTER);
            //hasMoreLabel.setPrefWidth(USE_PREF_SIZE);
            final CustomMenuItem hasMoreItem = new CustomMenuItem(box, true);
            menuItems.add(hasMoreItem);
            hasMoreItem.setOnAction((event) -> {
                event.consume();
            });
        }

//        Press tab to select the first suggestion
        control.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.TAB) {
                    getEntriesPopup().hide();
                }
//                if (ke.getCode() == KeyCode.TAB) {
//                    if (entriesPopup.isShowing()) {
//                        if (fixedSearchResult.size() > 0) {
//
//                            String[] fullText = getText().trim().split(" ");
//                            fullText[position] = fixedSearchResult.get(0)[0].trim();
//                            String toPrint = String.join(" ", fullText);
//                            setText(toPrint);
//                            int p = calculatePointerPosition(fullText, position);
//                            if (p >= getText().length()) {
//                                p = getText().length();
//                            }
////                positionCaret(toPrint.indexOf(result)+result.length()-1);
//                            positionCaret(p);
//                            entriesPopup.hide();
//
//                        }
//                    }
//                    entriesPopup.requestFocus();
                //ke.consume();
//                }
            }
        });

        //"Refresh" context menu
        getEntriesPopup().getItems().clear();
        getEntriesPopup().getItems().addAll(menuItems);

//        entriesPopup.onShowingProperty().addListener(listener);
//            //menuItems.get(0).setStyle("-fx-padding: 20px !important;");
//            //menuItems.get(0).getStyleableParent()instanceof . .setStyle("-fx-padding: 20px !important;");
//            menuItems.get(0).setContent(new Label("BLA"));
//        });
        //entriesPopup.addEventHandler(EventType.ROOT, (event) -> {
//        entriesPopup.addEventFilter(EventType.ROOT, (Event event) -> {
//            //System.out.println(event.getTarget());
//            if (event.getTarget() instanceof ContextMenuContent.MenuItemContainer) {
//                //&& event.getTarget() == menuItems.get(0)
//                //    }) {
//                ContextMenuContent.MenuItemContainer mic = (ContextMenuContent.MenuItemContainer) event.getTarget();
//                if (mic.getItem() == menuItems.get(0)) {
//                    System.out.println("FIRST ROW!");
//                    event.consume();
//                    if (event instanceof KeyEvent) {
//                        KeyEvent evt = (KeyEvent) event;
//                        KeyEvent ke = evt.copyFor(null, null);
//                        //KeyEvent ke = new KeyEvent(null, null, evt.getEventType(), evt.getText(), evt.getCharacter(), evt.getCode(), false, false, false, false);
//                        mic.fireEvent(ke);
//                        //((ContextMenuContent) mic.getParent()).getChildrenUnmodifiable().
//                    }
//                    
//                    
//                    if (event instanceof MouseEvent) {
//                        MouseEvent evt = (MouseEvent) event;
//                    }
//                }
//            }
//            //System.out.println("SOURCE: " + event.getSource() + ", TARGET: " + event.getTarget());
//        });
    }

//    /**
//     * Calculate the whole length of search text
//     *
//     * @param textArray search text as array
//     * @param index length of the text
//     * @return int
//     */
//    private int calculatePointerPosition(String[] textArray, int index) {
//        int position = 0;
//        for (int i = 0; i < index; i++) {
//            position += textArray[i].length() + 1;
//        }
//        return position + textArray[index].length();
//    }
    /**
     * set the title row for popup
     *
     * @param pTitles titles for popup
     */
    public void setTitles(final String[] pTitles) {
        String[] t = pTitles == null || pTitles.length == 0 ? new String[0] : new String[pTitles.length];
        if (!(pTitles == null || pTitles.length == 0)) {
            System.arraycopy(pTitles, 0, t, 0, pTitles.length);
        }
        this.titlesProperty.set(t);
    }

    /**
     * set the title row for popup
     *
     * @param pTitles titles for popup
     */
    public void setTitles(final List<String> pTitles) {
        if (pTitles == null || pTitles.isEmpty()) {
            this.titlesProperty.set(new String[0]);
        } else {
            String[] t = new String[pTitles.size()];
            pTitles.toArray(t);
            this.titlesProperty.set(t);
        }
    }

    /**
     * gives the title row for popup
     *
     * @return titles for popup
     */
    public String[] getTitles() {
        String[] t = this.titlesProperty.get();
        if (t == null) {
            return new String[0];
        } else {
            String[] s = new String[t.length];
            System.arraycopy(t, 0, s, 0, t.length);
            return s;
        }
    }

    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public List<T> getEntries() {
        List<T> copy = new ArrayList<>(entriesProperty.get());
        return copy;
    }

    /**
     * set list of entries for popup
     *
     * @param pEntries entry list
     */
    public void setEntries(List<T> pEntries) {
        this.entriesProperty.get().clear();
        this.entriesProperty.get().addAll(new ArrayList<>(pEntries));
    }

    /**
     * clears list of entries for popup
     */
    public void clearEntries() {
        this.entriesProperty.get().clear();
    }

    protected void showPopup(final String pFilter) {
        showPopup(pFilter, 0, 0);
    }

    protected void showPopup(final String pFilter,
            final double pX, final int pPosition) {
        String enteredText = pFilter;
        //always hide suggestion if nothing has been entered (only "spacebars" are dissalowed in TextFieldWithLengthLimit)
        if (enteredText == null || enteredText.isEmpty()) {
            getEntriesPopup().hide();
        } else {
            //if (!entries.isEmpty()) {
            //build popup - list of "CustomMenuItem"
            populatePopup(entriesProperty.get(), enteredText, pPosition);
            if (!entriesPopup.isShowing()) { //optional
                getEntriesPopup().show(control, Side.BOTTOM, pX, 0); //position of popup
            }
            //no suggestions -> hide
//            } else {
//                entriesPopup.hide();
//            }
        }
    }

    /**
     * hide popup with entries
     */
    public void hidePopup() {
        getEntriesPopup().hide();
    }

    /**
     * This is maybe a veeeeery awkward way to enforce limitation of width and
     * to prevent word wrap. But it should work!
     *
     * @param text text to check
     * @return original text or truncated text
     */
    private static String ensureMaxLength(final String text) {
        final int maxLength = 11; //this has to match to prefWidthSize = 90d
        if (text.length() > maxLength) {
            return (text.substring(0, maxLength)).trim() + "...";
        }
        return text;
    }

    /**
     * Build TextFlow with selected text. Return "case" dependent.
     *
     * @param pHeader - is header text?
     * @param text - string with text
     * @param filter - string to select in text
     * @return - TextFlow
     */
    protected static TextFlow buildTextFlow(final boolean pHeader, String text, String filter) {
        if (!pHeader) {
            text = ensureMaxLength(text);
        }
        if (filter == null) {
            Text textFilter = new Text(text);
            return new TextFlow(textFilter);
        }
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
        if (filterIndex >= 0) {
            Text textBefore = new Text(text.substring(0, filterIndex));
            Text textAfter = new Text(text.substring(filterIndex + filter.length()));
            Text textFilter = new Text(text.substring(filterIndex, filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
            textFilter.setFill(Color.RED);
            textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
            return new TextFlow(textBefore, textFilter, textAfter);
        } else {
            Text textFilter = new Text(text);
            textFilter.setFill(Color.RED);
            textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
            return new TextFlow(textFilter);
        }
    }

    /**
     * is popup with entries visible to the user?
     *
     * @return popup entries open?
     */
    public boolean isPopupShowing() {
        return getEntriesPopup().isShowing();
    }

    /**
     * get selected item from popup
     *
     * @return selected item
     */
    public T getSelectedItem() {
        return selectedItemProperty.get();
    }

    private class CustomHeaderMenuItem extends CustomMenuItem /* CustomMenuItem, SeparatorMenuItem */ {

        private static final String DEFAULT_STYLE_CLASS = "custom-menu-item";
        private static final String DEFAULT_STYLE_CLASS2 = "separator-menu-item";

        public CustomHeaderMenuItem() {
            getStyleClass().remove(DEFAULT_STYLE_CLASS2);
            getStyleClass().add(DEFAULT_STYLE_CLASS);
//            focusedProperty().addListener((observable, oldValue, newValue) -> {
//                if (newValue) {
//                    LOG.log(Level.INFO, "FOCUS!");
//                }
//            });
//            focusedProperty().addListener((observable) -> {
//                impl_traverse(Direction.NEXT);
//            });
            //this.setDisable(true);
            //getSkin().getNode().setFocusTraversable(true);
        }

        public CustomHeaderMenuItem(Node node) {
            //super();
            getStyleClass().remove(DEFAULT_STYLE_CLASS2);
            getStyleClass().add(DEFAULT_STYLE_CLASS);

            setContent(node);
            //getSkin().getNode().setFocusTraversable(true);
//            setDisabled(true);
//            setDisable(true);
//            focusedProperty().addListener((observable) -> {
//                impl_traverse(Direction.NEXT);
//            });
            //this.setDisable(true);
        }

        public CustomHeaderMenuItem(Node node, boolean hideOnClick) {
            //super();
            getStyleClass().remove(DEFAULT_STYLE_CLASS2);
            getStyleClass().add(DEFAULT_STYLE_CLASS);

            setContent(node);
            //setPadding(new Insets(5d));
            setHideOnClick(hideOnClick);
            control.setPrefHeight(Region.USE_COMPUTED_SIZE);
//            setMinHeight(20d);
            control.setMaxHeight(Region.USE_COMPUTED_SIZE);
            //setFocused(false);
//            setFocusTraversable(false);
            //Pane pane = new Pane(node);
            //pane.setPadding(new Insets(10d));
            //setContent(pane);
            setContent(node);
            setId("custom-header-menu-item");
        }

    }

    /**
     * fires when an item is drawn and can be used to add controls to each popup
     * entry
     *
     * @param pItemHandler item handler
     */
    public void setItemHandler(final Callback<T, List<? extends Node>> pItemHandler) {
        itemHandler = pItemHandler;
    }

    /**
     * fires when values have to be splitted for popup
     *
     * @param pSplitHandler split handler
     */
    public void setSplitHandler(final Callback<T, String[]> pSplitHandler) {
        splitHandler = pSplitHandler;
    }

    /**
     * @return the entriesPopup
     */
    public ContextMenu getEntriesPopup() {
        return entriesPopup;
    }

}

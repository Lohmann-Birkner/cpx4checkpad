/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.autocompletion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author halabieh
 */
public class AutocompletionlTextField extends TextField {

    //Local variables
    //entries to autocomplete
    private final List<String> entries;
    //popup GUI
    private final ContextMenu entriesPopup;
    private List<String> fixedSearchResult;
    private int position;

    public AutocompletionlTextField() {
        super();
        this.entries = new ArrayList<>();
        this.entriesPopup = new ContextMenu();

        setListener();
    }

    /**
     * "Suggestion" specific listners
     */
    private void setListener() {
        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
        });
        entriesPopup.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                pressTab();
                event.consume();
            }

        });
    }

    public static List<String> fixWidths(List<String> res) {
        List<String> finalResults = new LinkedList<>();

        double max = 0;
        for (String re : res) {
            Text t = new Text(re);
            double d = t.getLayoutBounds().getWidth();
            if (d > max) {
                max = d;
            }
        }
        for (String re1 : res) {
            while (new Text(re1).getLayoutBounds().getWidth() < max) {
                re1 = re1 + " ";
            }
            finalResults.add(re1);

        }

        return finalResults;
    }

    /**
     * Populate the entry set with the given search results. Display is limited
     * to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult, String searchReauest, int position) {
        fixedSearchResult = fixWidths(searchResult);
        this.position = position;
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        int maxEntries = 10;
        int count = Math.min(fixedSearchResult.size(), maxEntries);
        //Build list as set of labels
        for (int i = 0; i < count; i++) {
            final String result = fixedSearchResult.get(i);
            //label with graphic (text flow) to highlight founded subtext in suggestions
            Label entryLabel = new Label();
            if (result.toLowerCase().contains(searchReauest.toLowerCase())) {
                entryLabel.setGraphic(buildTextFlow(result, searchReauest));
            } else {
                entryLabel.setGraphic(buildTextFlow(result, null));
            }
            entryLabel.setPrefHeight(10);  //don't sure why it's changed with "graphic"
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            //if any suggestion is select set it into text and close popup
//            String correctedString = "";
//            if (getText().contains(" ")) {
//                correctedString = getText().substring(0, getText().lastIndexOf(' ')) + " ";
//            } else {
//                correctedString = "";
//            }
            item.setOnAction(actionEvent -> {

                String[] fullText = getText().trim().split(" ");
                fullText[position] = result.trim();
                String toPrint = String.join(" ", fullText);
                setText(toPrint);
                int p = calculatePointerPosition(fullText, position);
                if (p >= getText().length()) {
                    p = getText().length();
                }
                positionCaret(p);
                entriesPopup.hide();
            });
        }
        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    /**
     * select fist suggestion from the List by pressing TAB
     */
    public void pressTab() {
        if (entriesPopup.isShowing()) {
            if (!fixedSearchResult.isEmpty()) {
                String[] fullText = getText().trim().split(" ");
                fullText[position] = fixedSearchResult.get(0).trim();
                String toPrint = String.join(" ", fullText);
                setText(toPrint);
                int p = calculatePointerPosition(fullText, position);
                if (p >= getText().length()) {
                    p = getText().length();
                }
                positionCaret(p);
                entriesPopup.hide();
            }
        }
    }

    /**
     * Calculate the whole length of search text
     *
     * @param textArry search text as array
     * @param index length of the text
     * @return int
     */
    public int calculatePointerPosition(String[] textArry, int index) {
        int position = 0;
        for (int i = 0; i < index; i++) {
            position += textArry[i].length() + 1;
        }
        return position + textArry[index].length();
    }

    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public List<String> getEntries() {
        List<String> copy = new ArrayList<>(entries);
        return copy;
    }

    public void setEntries(String[] ent) {
        this.entries.addAll(Arrays.asList(ent));
    }

    public void clearEntries() {
        this.entries.clear();
    }

    public void showPopup(String filter, double x, int position) {
        String enteredText = filter;
        //always hide suggestion if nothing has been entered (only "spacebars" are dissalowed in TextFieldWithLengthLimit)
        if (enteredText == null || enteredText.isEmpty()) {
            entriesPopup.hide();
        } else {
            if (!entries.isEmpty()) {
                //build popup - list of "CustomMenuItem"
                populatePopup(entries, enteredText, position);
                if (!entriesPopup.isShowing()) { //optional
                    entriesPopup.show(AutocompletionlTextField.this, Side.BOTTOM, x, 0); //position of popup
                }
                //no suggestions -> hide
            } else {
                entriesPopup.hide();
            }
        }
    }

    /**
     * hide popup
     */
    public void hide() {
        entriesPopup.hide();
    }

    /**
     * Build TextFlow with selected text. Return "case" dependent.
     *
     * @param text - string with text
     * @param filter - string to select in text
     * @return - TextFlow
     */
    public static TextFlow buildTextFlow(String text, String filter) {
        if (filter == null) {
            Text textFilter = new Text(text);
            return new TextFlow(textFilter);
        }
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
        Text textBefore = new Text(text.substring(0, filterIndex));
        Text textAfter = new Text(text.substring(filterIndex + filter.length()));
        Text textFilter = new Text(text.substring(filterIndex, filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
        textFilter.setFill(Color.RED);
        textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        return new TextFlow(textBefore, textFilter, textAfter);
    }

}

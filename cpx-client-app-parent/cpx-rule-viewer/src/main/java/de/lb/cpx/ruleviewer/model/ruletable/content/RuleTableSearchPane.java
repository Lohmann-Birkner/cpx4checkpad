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
package de.lb.cpx.ruleviewer.model.ruletable.content;

import de.lb.cpx.ruleviewer.model.ruletable.content.model.Item;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;

/**
 *
 * @author wilde
 */
public class RuleTableSearchPane extends RuleTableContentPane {

    private static final Predicate<Node> DEFAULT_PREDICATE = (Node t) -> true;
    private FilteredList<Node> filter;

    public RuleTableSearchPane() {
        super();
        init();
    }

    private void init(){
        filter = new FilteredList<>(FXCollections.observableArrayList());
        predicateProperty().addListener(new ChangeListener<Predicate<Node>>() {
            @Override
            public void changed(ObservableValue<? extends Predicate<Node>> observable, Predicate<Node> oldValue, Predicate<Node> newValue) {
                filter.setPredicate(newValue);
                clearItems();
                displayItems(filter);
            }

            private void clearItems() {
                getChildren().removeIf(new Predicate<Node>() {
                    @Override
                    public boolean test(Node t) {
                        return (t instanceof Item);
                    }
                });
            }
        });
    }
    @Override
    public void add(int pIndex, Item pItem){
        super.add(pIndex, pItem);
        getBackingList().add(pIndex,pItem);
    }
    @Override
    public void remove(Item pItem){
        super.remove(pItem);
        getBackingList().remove(pItem);
    }
    protected final ObservableList<Item> getBackingList(){
        if(filter == null){
            throw new IllegalStateException("FilterList is null, can not access Items");
        }
        return (ObservableList<Item>) filter.getSource();
    }
    @Override
    public void setContent(String pContent, boolean pEditable) {
        getChildren().clear();
        if (pEditable) {
            getChildren().add(createAddButton());
        }
        List<Node> items = createItems(pContent, pEditable);
        filter = new FilteredList<>(FXCollections.observableArrayList(items));
        displayItems(filter);
    }
    
    private ObjectProperty<Predicate<Node>> predicateProperty;

    public final ObjectProperty<Predicate<Node>> predicateProperty() {
        if (predicateProperty == null) {
            predicateProperty = new SimpleObjectProperty<>(DEFAULT_PREDICATE);
        }
        return predicateProperty;
    }

    public void setPredicate(Predicate<Node> pPredicate) {
        predicateProperty().set(pPredicate);
    }

    public Predicate<Node> getPredicate() {
        return predicateProperty().get();
    }

    public void deleteAllItems() {
        setContent("", true);
    }

    public String getContent() {
//        String content = getChildren().stream().filter((Node t) -> t instanceof Item).map(Node::toString).collect(Collectors.joining(","));
        String content = filter.getSource().stream().filter((Node t) -> t instanceof Item).map(Node::toString).collect(Collectors.joining(","));
        return content;
    }

}

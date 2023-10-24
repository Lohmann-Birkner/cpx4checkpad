/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.titledpane;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

/**
 *
 * @author wilde
 * @param <Z> accordion item type
 * @param <T> content Type
 */
public class AccordionTitledPane <Z extends Object,T extends AbstractVersionEntity> extends MenuTitledPane{

    private static final PseudoClass EMPTY_CLASS = PseudoClass.getPseudoClass("content_empty");
    private static final String DEFAULT_STYLE_CLASS = "documentation-titled-pane";

    public AccordionTitledPane() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAnimated(false);
        values.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> c) {
                countProperty().set(c.getList().size());
                refresh();
            }
        });

        setSkin(new MenuTitledPaneSkin(this));
        StackPane titleRegion = (StackPane) lookup(".title");
        StackPane arrowButton = (StackPane) lookup(".arrow-button");
        arrowButton.prefHeightProperty().bind(titleRegion.heightProperty());
        StackPane arrow = (StackPane) lookup(".arrow");
        arrow.setPrefHeight(USE_COMPUTED_SIZE);
        arrow.setMaxHeight(USE_PREF_SIZE);
        titleRegion.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {

                if(shouldConsume(t)){
                    t.consume();
                    //select active?
                    AccordionSelectedItem<T> item = getMostRecentItem(SelectionTarget.ACCORDION_ITEM);
                    getSelectItemCallback().call(item);
                }
            }
            public boolean shouldConsume(MouseEvent t){
                boolean isNotArrow = !t.getTarget().equals(arrowButton) && !t.getTarget().equals(arrow);
                if(isNotArrow && (t.getClickCount()%2==0)){
                    return false;
                }
                return isNotArrow;
            }
        });
        titleRegion.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(!hasCount()){
                    t.consume();
                }
            }
            public boolean isArrowButton(MouseEvent t){
                return t.getTarget().equals(arrowButton) && t.getTarget().equals(arrow);
            }
        });
        refresh();
    }
    public AccordionSelectedItem<T> getMostRecentItem(SelectionTarget pTarget){
        return null;
    }
    public void select(T pItem){
    }
    public void refresh(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setText(updateTitle());
                pseudoClassStateChanged(EMPTY_CLASS, !hasCount());
                setContent(hasCount()?getContentFactory().call(getAccordionItem()):null);
            }
        });
    }
    
    private final Callback<Z,Node> defaultContentFactory = new Callback<Z, Node>() {
        @Override
        public Node call(Z param) {
            Label lbl = new Label(updateTitle()+":\n\nNo Content");
            return lbl;
        }
    };
    private Callback<Z,Node> contentFactory = defaultContentFactory;
    public Callback<Z,Node> getContentFactory(){
        return contentFactory;
    }
    public void setContentFactory(Callback<Z,Node> pFactory){
        contentFactory = Objects.requireNonNullElse(pFactory, defaultContentFactory);
    }
    
    private IntegerProperty countProperty;
    public final IntegerProperty countProperty(){
        if(countProperty == null){
            countProperty = new SimpleIntegerProperty(0);
        }
        return countProperty;
    }
    public Integer getCount(){
        return countProperty().get();
    }
    public void setCount(Integer pCount){
        countProperty().set(pCount);
    }
    private final ObservableList<T> values = FXCollections.observableArrayList();
    public void clearValues(){
        values.clear();
    }
    public void addValues(List<T> pValues) {
        values.addAll(pValues);
    }
    public void setValues(List<T> pValues) {
        values.setAll(pValues);
    }
    public void addValue(T pValue) {
        values.add(pValue);
    }
    public ObservableList<T> getValues(){
        return values;
    }
    private boolean hasCount(){
        Integer count = getCount();
        return !(count == null || count == 0);
    }
    public String updateTitle() {
        StringBuilder sb = new StringBuilder(getTitle());
        if(hasCount()){
            sb.append(" (").append(getCount()).append(")");
        }
        return sb.toString();
    }
    private StringProperty titleProperty;
    public StringProperty titleProperty(){
        if(titleProperty == null){
            titleProperty = new SimpleStringProperty();
        }
        return titleProperty;
    }
    public String getTitle(){
        return titleProperty().get();
    }
    public void setTitle(String pTitle){
        titleProperty().set(pTitle);
    }
    private final Callback<T,T> storeDefaultCallback = new Callback<T,T>() {
        @Override
        public T call(T param) {
            return null;
        }
    };
    private Callback<T,T> storeEntityCallback = storeDefaultCallback;
    public void setStoreEntityCallback(Callback<T,T> pCallback){
        storeEntityCallback = Objects.requireNonNullElse(pCallback, storeDefaultCallback);
    }
    public Callback<T,T> getStoreEntityCallback(){
        return storeEntityCallback;
    }
    public T store(T pEntity){
        T newEntity = getStoreEntityCallback().call(pEntity);
        if(newEntity ==null){
            BasicMainApp.showErrorMessageDialog("Kann das ausgewählte Objekt nicht speichern!");
        }
        return newEntity;
    }
    
    private final Callback<T,Boolean> removeDefaultCallback = new Callback<T,Boolean>() {
        @Override
        public Boolean call(T param) {
            return false;
        }
    };
    private Callback<T,Boolean> removeEntityCallback = removeDefaultCallback;
    public void setRemoveEntityCallback(Callback<T,Boolean> pCallback){
        removeEntityCallback = Objects.requireNonNullElse(pCallback, removeDefaultCallback);
    }
    public Callback<T,Boolean> getRemoveEntityCallback(){
        return removeEntityCallback;
    }
    public Boolean remove(T pEntity){
        Boolean newEntity = getRemoveEntityCallback().call(pEntity);
        if(!newEntity){
            BasicMainApp.showErrorMessageDialog("Kann das ausgewählte Objekt nicht gelöscht werden!");
        }
        return newEntity;
    }
    private final Callback<AccordionSelectedItem<T>,Boolean> selectDefaultCallback = new Callback<AccordionSelectedItem<T>,Boolean>() {
        @Override
        public Boolean call(AccordionSelectedItem<T> param) {
            return false;
        }
    };
    private Callback<AccordionSelectedItem<T>,Boolean> selectItemCallback = selectDefaultCallback;
    public void setSelectItemCallback(Callback<AccordionSelectedItem<T>,Boolean> pCallback){
        selectItemCallback = Objects.requireNonNullElse(pCallback, selectDefaultCallback);
    }
    public Callback<AccordionSelectedItem<T>,Boolean> getSelectItemCallback(){
        return selectItemCallback;
    }
    
    private ObjectProperty<Z> accordionItemProperty;
    public ObjectProperty<Z> accordionItemProperty(){
        if(accordionItemProperty==null){
            accordionItemProperty = new SimpleObjectProperty<>();
        }
        return accordionItemProperty;
    }
    
    public void setAccordionItem(Z pItem){
        accordionItemProperty().set(pItem);
    }
    
    public Z getAccordionItem(){
        return accordionItemProperty().get();
    }
    
    public void clearSelection(){
        
    }
    
    public enum SelectionTarget{
        ACCORDION_ITEM,CONTENT_ITEM,NONE;
    }
}

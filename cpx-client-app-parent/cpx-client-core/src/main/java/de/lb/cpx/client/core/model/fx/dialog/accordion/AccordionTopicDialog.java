/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.dialog.accordion;

import de.lb.cpx.client.core.model.fx.alert.AlertBaseDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;

/**
 *
 * @author wilde
 * @param <T> type to process in dialog
 */
public class AccordionTopicDialog<T> extends AlertBaseDialog {

    private Accordion accordion;
    private ScrollPane sp;

    /**
     * creates new instance of formular dialog
     *
     * @param pAlertType alter type
     * @param pMessage message
     * @param pOwner owner of the dialog
     * @param pModality mode to show window,application or none
     */
    public AccordionTopicDialog(AlertType pAlertType,String pMessage,Window pOwner, Modality pModality) {
        this(pAlertType,getAlertText(pAlertType),pMessage,null,ButtonType.OK);
        initOwner(pOwner);
        initModality(pModality);
    }

    public AccordionTopicDialog(AlertType pAlertType, String pHeaderText, String pMsg, Throwable pException, ButtonType... pButtonTypes) {
        super(pAlertType, pHeaderText, pMsg, pException, pButtonTypes);
        accordion = new Accordion();
        accordion.getPanes().addAll(createAccordionItems(getItems()));
        sp = new ScrollPane(accordion);
        sp.setPrefHeight(300);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        VBox.setVgrow(sp, Priority.ALWAYS);
        addContainer(sp);
        items.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> change) {
                accordion.getPanes().setAll(createAccordionItems(new ArrayList<>(change.getList())));
                expandItem(0);
            }
        });
        expandItem(0);
    }
    public final void addContainer(Node pNode){
        if(!getItemContainer().getChildren().contains(pNode)){
            getItemContainer().getChildren().add(pNode);
        }
    }
    public final void removeContainer(Node pNode){
        if(getItemContainer().getChildren().contains(pNode)){
            getItemContainer().getChildren().remove(pNode);
        }
    }
    private void expandItem(int pIndex){
        if(accordion == null){
            return;
        }
        if(pIndex<0){
            return;
        }
        if(pIndex>=accordion.getPanes().size()){
            return;
        }
        accordion.setExpandedPane(accordion.getPanes().get(pIndex));
    }
    private final ObservableList<T> items = FXCollections.observableArrayList();
    public ObservableList<T> getItems(){
        return items;
    }

//    @Override
//    public Node createContentNode(String pMsg, String pDetails, Throwable pException) {
//        List<T> errors = Objects.requireNonNullElse(getItems(), new ArrayList<>());
//        if(errors.isEmpty()){
//            return null;
//        }
//        if(sp == null){
//            accordion.getPanes().addAll(createAccordionItems(errors));
//            sp = new ScrollPane(accordion);
//            sp.setFitToHeight(true);
//            sp.setFitToWidth(true);
//        }
//        return sp;
//    }

    private List<AccordionTopicItem> createAccordionItems(List<T> pItems) {
        pItems = Objects.requireNonNullElse(pItems, new ArrayList<>());
        return pItems.stream().map((t) -> {
            return getAccordionItemFactory().call(t);
        }).collect(Collectors.toList());
    }
    
    private final Callback<T,AccordionTopicItem> DEFAULT_ACCORDION_ITEM_FACTORY = new Callback<T, AccordionTopicItem>() {
        @Override
        public AccordionTopicItem call(T p) {
            return new AccordionTopicItem(getAlertIcon(AlertType.ERROR),p.getClass().getName(),p.toString());
        }
    };
    
    private Callback<T,AccordionTopicItem> accordionItemFactory = DEFAULT_ACCORDION_ITEM_FACTORY;
    public Callback<T,AccordionTopicItem> getAccordionItemFactory(){
        return accordionItemFactory;
    }
    public void setAccordionItemFactory(Callback<T,AccordionTopicItem> pCallback){
        accordionItemFactory = Objects.requireNonNullElse(pCallback, DEFAULT_ACCORDION_ITEM_FACTORY);
    }
            
}

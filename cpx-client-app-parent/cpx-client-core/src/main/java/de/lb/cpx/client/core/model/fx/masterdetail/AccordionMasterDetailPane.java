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
package de.lb.cpx.client.core.model.fx.masterdetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 *
 * @author wilde
 * @param <T> item of accordion items
 */
public class AccordionMasterDetailPane<T> extends MasterDetailSplitPane{
    
    private static final String ROW_EVEN = "accordion-row-even";
    private static final String ROW_ODD = "accordion-row-odd";
    private final ObservableList<T> menuItems = FXCollections.observableArrayList();
    private final Accordion accordion;

    public AccordionMasterDetailPane() {
        this(DetailOrientation.RIGHT);
    }

    public AccordionMasterDetailPane(DetailOrientation oriantation) {
        super(oriantation);
        accordion = new Accordion();
        accordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            @Override
            public void changed(ObservableValue<? extends TitledPane> ov, TitledPane t, TitledPane t1) {
                if(t1 == null){
                    setDetail(new Pane());
                }
            }
        });
        setMaster(accordion);
        getMenuItems().addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> c) {
                if(c.next()){
                    if(c.wasAdded()){
                        List<TitledPane> panes = new ArrayList<>();
                        for(T added : c.getAddedSubList()){
                            panes.add(getTitledPaneFactory().call(added));
                        }
                        accordion.getPanes().addAll(panes);
                        checkStyleClasses(accordion.getPanes());
                    }
                    if(c.wasRemoved()){ //properly not work??
                        List<TitledPane> panes = new ArrayList<>();
                        for(T added : c.getRemoved()){
                            panes.add(getTitledPaneFactory().call(added));
                        }
                        accordion.getPanes().removeAll(panes);
                    }
                }
                checkAccordionContent();
            }

            private void checkStyleClasses(ObservableList<TitledPane> panes) {
                for(int i = 0;i<panes.size();i++){
                    updateRowStyle(i,panes.get(i));
                }
            }

            private void updateRowStyle(int pIndex, TitledPane pPane) {
                pPane.getStyleClass().removeAll(ROW_EVEN,ROW_ODD);
                if(pIndex%2==0){
                    pPane.getStyleClass().add(ROW_EVEN);
                }else{
                    pPane.getStyleClass().add(ROW_ODD);
                }
            }
        });
        checkAccordionContent();
        setDividerDefaultPosition(0.2);
        HBox detailBox = new HBox();
        detailBox.setAlignment(Pos.CENTER);
        setDetail(detailBox);
        
    }
    public String getPlaceholderText(){
        return "NO ITEMS AVAILABLE TO DISPLAY HERE!";
    }
    private Parent createPlaceholderNode(){
        Label lbl = new Label(getPlaceholderText());
        lbl.setWrapText(true);
        HBox box = new HBox(lbl);
        box.setAlignment(Pos.CENTER);
        return box;
    }
    private void checkAccordionContent(){
        //remove accordion and show placeholder if no entries are present
        if(accordion.getPanes().isEmpty()){
            setMaster(createPlaceholderNode());
        }else{
            if(!master.getChildren().contains(accordion)){
                setMaster(accordion);
            }
        }
    }
    @Override
    public void setDetail(Parent detailPane) {
        AccordionMasterDetailPane.super.setDetail(detailPane); //To change body of generated methods, choose Tools | Templates.
    }
    
    public final ObservableList<T> getMenuItems(){
        return menuItems;
    }
    public void clearAccordion(){
        accordion.getPanes().clear();
    }
    public Accordion getAccordion(){
        return accordion;
    }
    private final Callback<T,TitledPane> titledPaneDefaultFactory = new Callback<T, TitledPane>() {
        @Override
        public TitledPane call(T param) {
            if(param == null){
                return null;
            }
            TitledPane tp = new TitledPane(param.getClass().getSimpleName(), new Label("Kein Eintrag für die Klasse: " + param.getClass().getSimpleName()));
            return tp;
        }
    };
    private Callback<T,TitledPane> titlePaneFactory = titledPaneDefaultFactory;
    public void setTitledPaneFactory(Callback<T,TitledPane> pFactory){
        titlePaneFactory = Objects.requireNonNullElse(pFactory, titledPaneDefaultFactory);
    }
    public Callback<T,TitledPane> getTitledPaneFactory(){
        return titlePaneFactory;
    }
    
}

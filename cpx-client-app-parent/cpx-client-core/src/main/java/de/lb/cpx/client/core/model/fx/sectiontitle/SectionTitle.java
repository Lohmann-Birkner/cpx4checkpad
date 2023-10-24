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
package de.lb.cpx.client.core.model.fx.sectiontitle;

import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.adapter.WeakListAdapter;
import de.lb.cpx.client.core.model.fx.adapter.WeakMapAdapter;
import de.lb.cpx.client.core.model.fx.dialog.MessageNode;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Basicclass of the SectionTitle Controll, gets access to the layout und setup
 * genrell options Layout is designed with header area and content area header
 * contains a title, a menu area and under it a seperator line content contains
 * a simple empty anchorpane, where additional data can be stored
 *
 * @author wilde
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class SectionTitle {

    private static final Logger LOG = Logger.getLogger(SectionTitle.class.getName());
    private SimpleObjectProperty<Parent> contentProperty;
    private SectionTitleSkin skin;
    public BooleanProperty requestDetailProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty armedProperty = new SimpleBooleanProperty(true);
    private WeakPropertyAdapter propertyAdapter;
    private WeakMapAdapter mapAdapter;
    private WeakListAdapter listAdapter;
    /**
     * construct new Section with the given title
     *
     * @param title title to be set
     */
    public SectionTitle(String title) {
        this();
        setTitle(title);
    }

    //SectionTitle without Header when pheaderProperty is false
    public SectionTitle(Boolean pheaderProperty) {
        propertyAdapter = new WeakPropertyAdapter();
        mapAdapter = new WeakMapAdapter();
        listAdapter = new WeakListAdapter();
        skin = new SectionTitleSkin(this, pheaderProperty);
        setMenu2();
        propertyAdapter.addChangeListener(armedProperty,new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                getSkin().clearMenu();
                if (newValue) {
                    setMenu();
                }
            }
        });
        updateContent();
    }

    /**
     * default constructor, creates new section with empty title
     */
    public SectionTitle() {
        propertyAdapter = new WeakPropertyAdapter();
        mapAdapter = new WeakMapAdapter();
        listAdapter = new WeakListAdapter();
        skin = new SectionTitleSkin(this);
        //     setTextField();
        setMenu2();
        propertyAdapter.addChangeListener(armedProperty,new ChangeListener<Boolean>() {
//        armedProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                getSkin().clearMenu();
                if (newValue) {
                    setMenu();
                }
            }
        });
        updateContent();
    }
    private void updateContent(){
        Parent content = createContent();
        if(content !=null){
            VBox contentBox = new VBox(content);
            VBox.setVgrow(content, Priority.ALWAYS);
            getSkin().setContent(contentBox); 
        }else{
           getSkin().setContent(content); 
        }
    }
    public MessageNode showErrorMessageNode(CpxErrorTypeEn pType,String pMessage) {
        MessageNode message = new MessageNode();
        message.setMessageType(pType);
        message.setMessageText(pMessage);
        Parent parent = getContent();
        if (parent instanceof VBox) {
            ((VBox) parent).getChildren().add(0, message);
        } else {
            LOG.warning("Content is not of expected Type (VBox), Type was: " + parent.getClass().getName());
        }
        return message;
    }
    
    public final <T> void registerPropertyListener(final ObservableValue observable, ChangeListener<T> listener){
        propertyAdapter.addChangeListener(observable, listener);
    }
    public final <T> void registerMapListener(final ObservableMap<Object,Object> observable, MapChangeListener listener){
        mapAdapter.addChangeListener(observable, listener);
    }
    public final <T> void registerListListener(final ObservableList observable, ListChangeListener listener){
        listAdapter.addChangeListener(observable, listener);
    }
    /**
     * sets new ContentPane, replaces old one
     *
     * @param pane new Pane to be set
     */
    public void setContent(Parent pane) {
        skin.setContent(pane);
    }

    /**
     * @return content from ContentPane NOTE: Will be null if not content was
     * set
     */
    public Parent getContent() {
        return skin.getContent();
    }

    /**
     * get root pane from the whole section
     *
     * @return get root pane
     */
    public final Parent getRoot() {
        return skin.getRoot();
    }

    /**
     * sets new Title for the section
     *
     * @param title new title to be set
     */
    public final void setTitle(String title) {
        skin.setTitle(title);
    }

    /**
     * get Skinobject that holds all layout related data
     *
     * @return skin object
     */
    public final SectionTitleSkin getSkin() {
        return skin;
    }

//    public void setTextField() {
//        TextField txt = new TextField();
//        txt.setPromptText("Search");
//        getSkin().settextField(txt);
//    }
    private void setMenu2() {
        setMenu();
    }

    public abstract void setMenu();

    protected abstract Parent createContent();

    public abstract Parent getDetailContent();

    /**
     * protected invalidation of the property, sets Value to true and after that
     * to falls forces all registered listeners to fire switch
     */
    protected void invalidateRequestDetailProperty() {
        requestDetailProperty.setValue(Boolean.TRUE);
        requestDetailProperty.setValue(Boolean.FALSE);
    }

    /**
     * sets new MouseClicked Event on getRoot() Pane in the Section, overrides
     * the old eventhandler
     *
     * @param event new Eventhandler
     */
    public void setOnMouseClicked(EventHandler<MouseEvent> event) {
        getRoot().setOnMouseClicked(event);
    }

    /**
     * sets context menu to contentPane, show when right button is clicked
     *
     * @param menu menu to set
     */
    public void setContextMenu(ContextMenu menu) {
        getSkin().setContextMenu(menu);
    }

    /**
     * get the contextmenu property for binding purposes
     *
     * @return the context menu property that handels the context menu
     */
    public final ObjectProperty<ContextMenu> getContextMenuProperty() {
        return getSkin().getContextMenuProperty();
    }

    /**
     * get the armed property taht indicates if the controls of that section
     * title should be armed or not
     *
     * @return boolean property for bindings
     */
    public final BooleanProperty getArmedProperty() {
        return armedProperty;
    }

    /**
     * indicates if section controls are armed
     *
     * @return indicator if controls are armed
     */
    public boolean isArmed() {
        return armedProperty.getValue();
    }

    /**
     * set the armed value in property
     *
     * @param pArmed set the armed value to given value
     */
    public void setArmed(boolean pArmed) {
        armedProperty.setValue(pArmed);
    }

    /**
     * reload section, default to nothing
     */
    public void reload() {
    }

    /**
     * refresh section, default to nothing
     */
    public void refresh() {
    }

    private DoubleProperty minHeightProperty;

    public DoubleProperty minHeightProperty() {
        if (minHeightProperty == null) {
            minHeightProperty = new SimpleDoubleProperty(AnchorPane.USE_COMPUTED_SIZE);
        }
        return minHeightProperty;
    }

    public void setMinHeight(Double pHeight) {
        minHeightProperty().set(pHeight);
    }

    public Double getMinHeight() {
        return minHeightProperty().get();
    }
    private DoubleProperty maxHeightProperty;

    public DoubleProperty maxHeightProperty() {
        if (maxHeightProperty == null) {
            maxHeightProperty = new SimpleDoubleProperty(AnchorPane.USE_COMPUTED_SIZE);
        }
        return maxHeightProperty;
    }

    public void setMaxHeight(Double pHeight) {
        maxHeightProperty().set(pHeight);
    }

    public Double getMaxHeight() {
        return maxHeightProperty().get();
    }
    
    public void dispose() {
        propertyAdapter.dispose();
        propertyAdapter = null;
        mapAdapter.dispose();
        mapAdapter = null;
        listAdapter.dispose();
        listAdapter = null;
        skin.dispose();
        skin = null;
    }
    protected void setContentProperty(Parent parent){

        contentProperty().set(parent);
    }
    
    private SimpleObjectProperty<Parent> contentProperty(){
        if(contentProperty == null){
            contentProperty = new SimpleObjectProperty<Parent> ();
        };
        return contentProperty;
    }
    
    protected Parent getContentProperty(){
        return contentProperty().get();
    }
}

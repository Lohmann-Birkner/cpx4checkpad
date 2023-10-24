/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.control;

import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 */
public class InfoView extends Control {

    public InfoView() {
        //empty implementation (no-args constructor is urgently needed for SceneBuilder!)
    }

    private StringProperty commentProperty;

    public StringProperty commentProperty() {
        if (commentProperty == null) {
            commentProperty = new SimpleStringProperty();
        }
        return commentProperty;
    }

    public void setComment(String pComment) {
        commentProperty().set(pComment);
    }

    public String getComment() {
        return commentProperty().get();
    }
    private ReadOnlyObjectWrapper<EventHandler<ActionEvent>> onCommentEditProperty;

    public ReadOnlyObjectProperty<EventHandler<ActionEvent>> onCommentEditProperty() {
        if (onCommentEditProperty == null) {
            onCommentEditProperty = new ReadOnlyObjectWrapper<>();
        }
        return onCommentEditProperty.getReadOnlyProperty();
    }

    protected void setOnCommentEdit(EventHandler<ActionEvent> pHandler) {
        onCommentEditProperty.set(pHandler);
    }

    public EventHandler<ActionEvent> getOnCommentEdit() {
        return onCommentEditProperty.get();
    }

    private Callback<String, Boolean> onSaveComment;

    public void setOnSaveComment(Callback<String, Boolean> pOnSave) {
        onSaveComment = pOnSave;
    }

    public Callback<String, Boolean> getOnSaveComment() {
        return onSaveComment;
    }
    protected final ObservableList<Node> menuItems = FXCollections.observableArrayList();

    public void setMenuItems(Node... pItems) {
        menuItems.addAll(pItems);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new InfoViewSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(InfoView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin(); //To change body of generated methods, choose Tools | Templates.
    }

    private ObjectProperty<AsyncPane<FlowPane>> infoPaneProperty;

    public ObjectProperty<AsyncPane<FlowPane>> infoPaneProperty() {
        if (infoPaneProperty == null) {
            infoPaneProperty = new SimpleObjectProperty<>();
        }
        return infoPaneProperty;
    }

    public AsyncPane<FlowPane> getInfoPane() {
        return infoPaneProperty().get();
    }

    public void setInfoPane(AsyncPane<FlowPane> pane) {
        infoPaneProperty().set(pane);
    }

    private StringProperty commentHeaderProperty;

    public StringProperty commentHeaderProperty() {
        if (commentHeaderProperty == null) {
            commentHeaderProperty = new SimpleStringProperty();
        }
        return commentHeaderProperty;
    }

    public void setHeaderComment(String pComment) {
        commentHeaderProperty().set(pComment);
    }

    public String getHeaderComment() {
        return commentHeaderProperty().get();
    }

    private StringProperty contentHeaderProperty;

    public StringProperty contentHeaderProperty() {
        if (contentHeaderProperty == null) {
            contentHeaderProperty = new SimpleStringProperty();
        }
        return commentHeaderProperty;
    }

    public void setHeaderContent(String pComment) {
        contentHeaderProperty().set(pComment);
    }

    public String getHeaderContent() {
        return contentHeaderProperty().get();
    }

}

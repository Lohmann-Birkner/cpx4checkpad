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
package de.lb.cpx.client.core.menu.fx.openedEntry;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * OpenEntry Control to display entry in 'multi-tab-pane'
 *
 * @author wilde
 * @param <T> Stored scene type
 * @param <S> type of entity stored in scene
 */
public abstract class OpenEntry<T extends CpxScene, S extends AbstractEntity> extends Control {

    private static final Logger LOG = Logger.getLogger(OpenEntry.class.getName());
    private T openendScene;
    private final StringProperty titleProperty = new SimpleStringProperty();
    private final StringProperty descProperty = new SimpleStringProperty();
    private ObjectProperty<Node> graphicProperty;
    private final EventHandler<ActionEvent> onClose = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            S entity = getEntity();
            LOG.log(Level.FINER, "try to close with id " + entity.id + " is of type " + entity.getClassName());
            if (openendScene.close()) {
                LOG.log(Level.INFO, "close for " + entity.getClassName() + " with id " + entity.id + " successful!");
                if (openendScene.getController() != null) {
                    openendScene.getController().setScene(null);
                }
                openendScene = null;
                return;
            }
            showCloseError();
        }

    };
    private ObjectProperty<EventHandler<ActionEvent>> onCloseProperty;
    private BooleanProperty unsavedContentProperty;
    private SimpleBooleanProperty erroneousProperty;
    private ObjectProperty<Node> errorGrahpicProperty;

    public OpenEntry(T pScene) {
        super();
        this.openendScene = pScene;
        getStylesheets().add(0, getClass().getResource("/styles/ruleeditor.css").toExternalForm());
        getStyleClass().add("open-entry");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new OpenEntrySkin(this);
    }

    public boolean close() {
        getOnClose().handle(new ActionEvent());
        return true;
    }

    public abstract S getEntity();

    /**
     * get the currently stored Scene
     *
     * @return the current hospital case
     */
    public T getStoredScene() {
        return openendScene;
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public void setTitle(String pTitle) {
        titleProperty().set(pTitle);
    }

    public StringProperty descProperty() {
        return descProperty;
    }

    public String getDesc() {
        return descProperty().get();
    }

    public void setDesc(String pDesc) {
        descProperty().set(pDesc);
    }

    public ObjectProperty<Node> graphicProperty() {
        if (graphicProperty == null) {
            graphicProperty = new SimpleObjectProperty<>(ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER_OPEN));
        }
        return graphicProperty;
    }

    public Node getGraphic() {
        return graphicProperty().get();
    }

    public void setGraphic(Node pGraphic) {
        graphicProperty().set(pGraphic);
    }

    protected void clearOpenScene() {
        if (openendScene.getController() != null) {
            openendScene.getController().setScene(null);
        }
        openendScene = null;
    }

    protected void showCloseError() {
        String error = "Close for " + getEntity().getClassName() + " with id " + getEntity().id + " failed";
        LOG.log(Level.SEVERE, error);
        BasicMainApp.showErrorMessageDialog(error);
    }

    public ObjectProperty<EventHandler<ActionEvent>> onCloseProperty() {
        if (onCloseProperty == null) {
            onCloseProperty = new SimpleObjectProperty<>(onClose);
        }
        return onCloseProperty;
    }

    public EventHandler<ActionEvent> getOnClose() {
        return onCloseProperty().get();
    }

    public void setOnClose(EventHandler<ActionEvent> pOnClose) {
        onCloseProperty().set(pOnClose);
    }

    public BooleanProperty unsavedContentProperty() {
        if (unsavedContentProperty == null) {
            unsavedContentProperty = new SimpleBooleanProperty(false);
        }
        return unsavedContentProperty;
    }

    public void setUnSavedContent(boolean pUnsaved) {
        unsavedContentProperty().set(pUnsaved);
    }

    public boolean hasUnSavedContent() {
        return unsavedContentProperty().get();
    }
    
    public BooleanProperty erroneousProperty() {
        if (erroneousProperty == null) {
            erroneousProperty = new SimpleBooleanProperty(false);
        }
        return erroneousProperty;
    }

    public void setErroneous(boolean pErroneous) {
        erroneousProperty().set(pErroneous);
    }

    public boolean isErroneous() {
        return erroneousProperty().get();
    }
    
    public ObjectProperty<Node> errorGraphicProperty() {
        if (errorGrahpicProperty == null) {
            errorGrahpicProperty = new SimpleObjectProperty<>(ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION));
        }
        return errorGrahpicProperty;
    }

    public void setErrorGraphic(Node pErrorGraphic) {
        errorGraphicProperty().set(pErrorGraphic);
    }

    public Node getErrorGraphic() {
        return errorGraphicProperty().get();
    }
    
}

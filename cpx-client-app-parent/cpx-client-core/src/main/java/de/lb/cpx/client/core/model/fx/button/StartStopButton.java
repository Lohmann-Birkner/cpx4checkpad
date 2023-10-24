/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.button;

import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.shared.lang.Lang;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * Start stop button impl
 *
 * @author wilde
 */
public final class StartStopButton extends Button {

    private EventHandler<ActionEvent> onStartEvent;
    private EventHandler<ActionEvent> onStopEvent;
    private EventHandler<ActionEvent> onActionInterrupt;
    private String stopText;
    private String startText;
    private BooleanProperty isStartMode;

    public StartStopButton() {
        this("Start", Lang.getCancel());
    }

    public StartStopButton(String pStartText, String pStopText) {
        super();
        startText = pStartText;
        stopText = pStopText;
        setOnAction((ActionEvent event) -> {
            if (onActionInterrupt != null) {
                onActionInterrupt.handle(event);
                if (event.isConsumed()) {
                    return;
                }
            }
            if (isStartMode()) {
                if (onStartEvent != null) {
                    onStartEvent.handle(event);
                }
            } else {
                if (onStopEvent != null) {
                    onStopEvent.handle(event);
                }
            }
        });
        isStartModeProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                updateButtonText();
            }
        });
        setStartMode();
    }

    /**
     * @param pText set text as start text on button
     */
    public void setStartText(String pText) {
        startText = pText;
        updateButtonText();
    }

    /**
     * @param pText set text as stop text on button
     */
    public void setStopText(String pText) {
        stopText = pText;
        updateButtonText();
    }

    /**
     * set event that should occur on start
     *
     * @param pEvent event to start
     */
    public void setOnStartEvent(EventHandler<ActionEvent> pEvent) {
        onStartEvent = pEvent;
    }

    /**
     * @return the currently set eventhalndler that is executed when start is
     * triggered
     */
    public EventHandler<ActionEvent> getOnStartEvent() {
        return onStartEvent;
    }

    /**
     * set event that should occur on stop
     *
     * @param pEvent event to stop
     */
    public void setOnStopEvent(EventHandler<ActionEvent> pEvent) {
        onStopEvent = pEvent;
    }

    /**
     * set event that should occur on action interrupt eventhandler to stop
     * onAction to occur
     *
     * @param pEvent event that interrupt
     */
    public void setOnActionInterrupt(EventHandler<ActionEvent> pEvent) {
        onActionInterrupt = pEvent;
    }

    private void setButtonText(String pText, final boolean pDisable, FontAwesome.Glyph pGlyph) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Glyph glyph = null;
                if (pGlyph != null) {
                    glyph = ResourceLoader.getGlyph(pGlyph);
                    if (glyph != null) {
                        //due to css 
                        glyph.setStyle("-fx-text-fill: white;");
                    }
                }
                setGraphic(glyph);
                setText(pText);
                setDisable(pDisable);
            }
        });
    }

    public void updateButtonText() {
        if (isStartMode()) {
            setButtonText(startText, false, FontAwesome.Glyph.PLAY);
        } else {
            setButtonText(stopText, false, FontAwesome.Glyph.STOP);
        }
    }

    /**
     * @return is start mode porperty for binding
     */
    public BooleanProperty isStartModeProperty() {
        if (isStartMode == null) {
            isStartMode = new SimpleBooleanProperty();
        }
        return isStartMode;
    }

    /**
     * @return indicator if button is in start mode
     */
    public boolean isStartMode() {
        return isStartModeProperty().get();
    }

    public void setStartMode() {
        isStartMode.set(true);
    }

    public void setStopMode() {
        isStartMode.set(false);
    }

    public void setStopping() {
        setButtonText("wird abgebrochen...", true, null);
    }

    public void setStarting() {
        setButtonText("wird gestartet...", true, null);
    }
}

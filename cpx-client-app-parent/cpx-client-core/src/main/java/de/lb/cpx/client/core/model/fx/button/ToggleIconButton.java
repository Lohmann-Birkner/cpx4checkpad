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
package de.lb.cpx.client.core.model.fx.button;

import de.lb.cpx.client.core.util.ResourceLoader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class ToggleIconButton extends GlyphIconButton{

    public ToggleIconButton() {
        super(FontAwesome.Glyph.TOGGLE_OFF);
        getStyleClass().add("cpx-icon-button");
        toggleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                setGlyph(t1?ResourceLoader.getGlyph(FontAwesome.Glyph.TOGGLE_ON):ResourceLoader.getGlyph(FontAwesome.Glyph.TOGGLE_OFF));
            }
        });
        addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(MouseButton.PRIMARY.equals(t.getButton())){
                    setToggle(!isToggled());
                }
            }
        });
        setContentDisplay(ContentDisplay.CENTER);
    }
    
    private BooleanProperty toggleProperty;
    public final BooleanProperty toggleProperty(){
        if(toggleProperty == null){
            toggleProperty = new SimpleBooleanProperty();
        }
        return toggleProperty;
    }
    public final boolean isToggled(){
        return toggleProperty().get();
    }
    public final void setToggle(boolean pToggle){
        toggleProperty().set(pToggle);
    }
}

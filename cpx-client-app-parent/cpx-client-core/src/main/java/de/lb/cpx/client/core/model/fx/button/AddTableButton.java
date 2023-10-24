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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class AddTableButton extends JoinedGlyphButton{
    
    public AddTableButton() {
        super(FontAwesome.Glyph.TABLE, FontAwesome.Glyph.PLUS);
        getForefrontGlyphe().getStyleClass().add(0,"cpx-icon-button");
        getBackgroundGlyphe().getStyleClass().add(0,"cpx-icon-button");
        setTooltip(new Tooltip("Gegenstand der Tabelle hinzufügen"));
        removeMode().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(t1){
                    setForefrontGlyphe(FontAwesome.Glyph.MINUS);
                }else{
                    setForefrontGlyphe(FontAwesome.Glyph.PLUS);
                }
            }
        });
    }
    
    private BooleanProperty removeMode;
    public final BooleanProperty removeMode(){
        if(removeMode == null){
            removeMode = new SimpleBooleanProperty(false);
        }
        return removeMode;
    }
    public void setRemoveMode(boolean pRemove){
        removeMode().set(pRemove);
    }
    public boolean isRemoveMode(){
        return removeMode().get();
    }
}

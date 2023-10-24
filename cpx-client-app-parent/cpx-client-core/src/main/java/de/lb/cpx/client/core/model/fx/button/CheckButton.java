/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.button;

import javafx.scene.control.Tooltip;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author gerschmann
 */
public class CheckButton extends GlyphIconButton {

    public CheckButton (){
        super(FontAwesome.Glyph.CHECK);
        setTooltip(new Tooltip("Hinzugefügt"));
    }
}

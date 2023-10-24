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
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.StackPane;
import org.controlsfx.glyphfont.FontAwesome.Glyph;

/**
 *
 * @author wilde
 */
public class StackedGlyphButton extends Button{

    private org.controlsfx.glyphfont.Glyph backgroundGlyphe = null;
    private org.controlsfx.glyphfont.Glyph forefrontGlyphe = null;
    private final StackPane stackPane;

    public StackedGlyphButton(Glyph pBackgroundImage, Glyph pForefrontImage) {
        super();
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        stackPane = new StackPane();
        if(pBackgroundImage != null){
            backgroundGlyphe = ResourceLoader.getGlyph(pBackgroundImage);
            stackPane.getChildren().add(backgroundGlyphe);
        }
        if(pForefrontImage != null){
            forefrontGlyphe = ResourceLoader.getGlyph(pForefrontImage);
            stackPane.getChildren().add(forefrontGlyphe);
        }
        setGraphic(stackPane);
    }


    public final org.controlsfx.glyphfont.Glyph getBackgroundGlyphe() {
        return backgroundGlyphe;
    }

    public final org.controlsfx.glyphfont.Glyph getForefrontGlyphe() {
        return forefrontGlyphe;
    }
    public final void setForefrontGlyphe(Glyph pGlyph){
        org.controlsfx.glyphfont.Glyph newGlyph = ResourceLoader.getGlyph(pGlyph);
        newGlyph.getStyleClass().addAll(forefrontGlyphe.getStyleClass());
        forefrontGlyphe = newGlyph;
        if(stackPane.getChildren().size()>1){
            stackPane.getChildren().set(1,forefrontGlyphe);
        }
    }
    protected final StackPane getStackPane(){
        return stackPane;
    }
    
}

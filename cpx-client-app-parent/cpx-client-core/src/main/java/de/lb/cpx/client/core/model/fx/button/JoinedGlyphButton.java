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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class JoinedGlyphButton extends StackedGlyphButton{
    private static final String DEFAULT_STYLE_CLASS = "joined-glyph-button";
    private static final String BACKGROUND_STYLE_CLASS = "background-image";
    private static final String FOREFRONT_STYLE_CLASS = "forefront-image";
    public JoinedGlyphButton(FontAwesome.Glyph pBackgroundImage, FontAwesome.Glyph pForefrontImage) {
        super(pBackgroundImage, pForefrontImage);
        if(getBackgroundGlyphe() != null){
            getBackgroundGlyphe().getStyleClass().add(BACKGROUND_STYLE_CLASS);
            getBackgroundGlyphe().setMaxWidth(Double.MAX_VALUE);
            getBackgroundGlyphe().setMaxHeight(Double.MAX_VALUE);
//            StackPane.setMargin(getBackgroundGlyphe(), new Insets(0,0,0,2));
            StackPane.setMargin(getBackgroundGlyphe(), new Insets(2));
        }
        if(getForefrontGlyphe() != null){
            getForefrontGlyphe().getStyleClass().add(FOREFRONT_STYLE_CLASS);
            getForefrontGlyphe().setFontSize(8);
        }
        getStackPane().setAlignment(Pos.BOTTOM_LEFT);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }
    
}

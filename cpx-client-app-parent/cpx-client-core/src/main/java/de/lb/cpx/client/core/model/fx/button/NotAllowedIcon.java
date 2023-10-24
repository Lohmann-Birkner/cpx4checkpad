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

import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class NotAllowedIcon extends JoinedGlyphButton{
    private static final String DEFAULT_STYLE_CLASS = "not-allowed-icon";
    private static final String BAN_STYLE_CLASS = "ban";
    private static final String BAN_ICON_STYLE_CLASS = "ban-icon";
    
    public NotAllowedIcon(FontAwesome.Glyph pBackgroundImage) {
        super(pBackgroundImage, FontAwesome.Glyph.BAN);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        getForefrontGlyphe().getStyleClass().add(0,BAN_STYLE_CLASS);
//        getForefrontGlyphe().setRotate(-45);
//        getForefrontGlyphe().minWidthProperty().bind(heightProperty());
    }
    
    public NotAllowedIcon() {
        super(FontAwesome.Glyph.BAN,null);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        getBackgroundGlyphe().getStyleClass().add(0,BAN_ICON_STYLE_CLASS);
    }
}

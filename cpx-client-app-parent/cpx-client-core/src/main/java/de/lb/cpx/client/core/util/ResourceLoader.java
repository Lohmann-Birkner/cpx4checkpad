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
package de.lb.cpx.client.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * loader for access files stored in ressource
 *
 * @author wilde
 */
public class ResourceLoader {

    private static final Logger LOG = Logger.getLogger(ResourceLoader.class.getName());

    /**
     * loads javafx image with given name from the resources
     *
     * @param pName name of the image
     * @return javafx image
     */
    public static Image getImage(String pName) {
        return new Image(ResourceLoader.class.getResourceAsStream(pName));
    }
    
    public static Glyph getGlyph(char glyphCode) {
        return getGlyph(String.valueOf(glyphCode));
    }

    /**
     * loads glyph from the fontAwesome by its identifier
     *
     * @param glyphCode identifier of the glyph to load
     * @return glyph object from the font
     */
    public static Glyph getGlyph(String glyphCode) {
        if (glyphCode == null) {
            return null;
        }
        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        //f0c9; menu - f013; gear
        //Glyph glyph = fontAwesome.create('\uf013');//.size(28).color(Color.RED);
        if (fontAwesome == null) {
            LOG.log(Level.WARNING, "did not find FontAwesome");
            return null;
        }
        return fontAwesome.create(glyphCode);
    }

    /**
     * **
     *
     * loads glyph from the fontAwesome by its Enum
     *
     * @param glyph Enum of the glyph to load
     * @return glyph object from the font
     */
    public static Glyph getGlyph(FontAwesome.Glyph glyph) {
        if (glyph == null) {
            return null;
        }
        return getGlyph(glyph.name());
    }

    public static Glyph rotate(Glyph pGlyph, Double pValue) {
        if (pGlyph != null) {
            pGlyph.setRotate(pValue);
        }
        return pGlyph;
    }

    public static Glyph rotate(FontAwesome.Glyph pGlyph, Double pValue) {
        return rotate(getGlyph(pGlyph), pValue);
    }
}

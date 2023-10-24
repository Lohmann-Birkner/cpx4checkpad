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
package de.lb.cpx.client.core.model.fx.button;

import de.lb.cpx.client.core.util.ResourceLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author wilde
 */
public class GlyphIconButton extends Button {

    public GlyphIconButton() {
        super();
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        getStyleClass().add("cpx-icon-button");
        graphicProperty().bind(glyphProperty());
    }

    public GlyphIconButton(FontAwesome.Glyph pGlyph) {
        this(ResourceLoader.getGlyph(pGlyph));
    }

    public GlyphIconButton(Glyph pGylph) {
        this();
        setGlyph(pGylph);
    }
    private ObjectProperty<Glyph> glyphProperty;

    public final ObjectProperty<Glyph> glyphProperty() {
        if (glyphProperty == null) {
            glyphProperty = new SimpleObjectProperty<>(ResourceLoader.getGlyph(FontAwesome.Glyph.REBEL));
        }
        return glyphProperty;
    }

    public final void setGlyph(Glyph pGylph) {
        glyphProperty().set(pGylph);
    }

    public Glyph getGlyph() {
        return glyphProperty().get();
    }

    public void setFontSize(Double pSize) {
        setGraphic(getGlyph().size(pSize));
    }

    public void setFontSizeFactor(int pSize) {
        setGraphic(getGlyph().sizeFactor(pSize));
    }

    public GlyphIconButton setOnActionEvent(EventHandler<ActionEvent> pEvent) {
        setOnAction(pEvent);
        return this;
    }
}

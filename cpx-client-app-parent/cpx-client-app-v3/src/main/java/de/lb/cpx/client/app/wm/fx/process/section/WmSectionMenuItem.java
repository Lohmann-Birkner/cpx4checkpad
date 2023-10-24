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
package de.lb.cpx.client.app.wm.fx.process.section;

import de.lb.cpx.client.core.util.ResourceLoader;
import javafx.event.Event;
import javafx.event.EventHandler;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author wilde
 */
public class WmSectionMenuItem {

    private final String text;
    private final Glyph icon;
    private final EventHandler<Event> handler;
    
    
    public WmSectionMenuItem(String pText, Glyph pIcon, EventHandler< Event> pHandler){
        text = pText;
        icon = pIcon;
        handler = pHandler;
    }

    WmSectionMenuItem(String pText, FontAwesome.Glyph pGlyph, EventHandler<Event> pEventHandler) {
        this(pText,ResourceLoader.getGlyph(pGlyph),pEventHandler);
    }

    public String getText() {
        return text;
    }

    public Glyph getIcon() {
        return icon;
    }

    public EventHandler<Event> getHandler() {
        return handler;
    }
    
}

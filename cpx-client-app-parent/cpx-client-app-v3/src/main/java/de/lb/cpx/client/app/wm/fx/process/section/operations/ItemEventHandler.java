/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.controlsfx.glyphfont.FontAwesome.Glyph;

/**
 *
 * @author niemeier
 */
public abstract class ItemEventHandler implements EventHandler<ActionEvent> {

//    private final EventHandler<Event> handler;
    private final char menuItemGlyph;
    private final String menuItemText;
    private final char historyGlyph;
    private final String historyTooltip;
    private final boolean historyDefaultOperation;
    
    public ItemEventHandler(final char pMenuItemGlyph, final String pMenuItemText, final char pHistoryGlyph, final String pHistoryTooltip, final boolean pHistoryDefaultOperation) {
//        handler = pHandler;
        menuItemGlyph = pMenuItemGlyph;
        menuItemText = pMenuItemText;
        historyGlyph = pHistoryGlyph;
        historyTooltip = pHistoryTooltip;
        historyDefaultOperation = pHistoryDefaultOperation;
    }
    
    public ItemEventHandler(final Glyph pMenuItemGlyph, final String pMenuItemText, final Glyph pHistoryGlyph, final String pHistoryTooltip, final boolean pHistoryDefaultOperation) {
//        handler = pHandler;
        this(
                pMenuItemGlyph == null ? null : pMenuItemGlyph.getChar(), 
                pMenuItemText, 
                pHistoryGlyph == null ? null : pHistoryGlyph.getChar(), 
                pHistoryTooltip, 
                pHistoryDefaultOperation
        );
    }

//    public EventHandler<Event> getHandler() {
//        return handler;
//    }
    public char getMenuItemGlyph() {
        return menuItemGlyph;
    }
    
    public String getMenuItemText() {
        return menuItemText;
    }
    
    public char getHistoryGlyph() {
        return historyGlyph;
    }
    
    public String getHistoryTooltip() {
        return historyTooltip;
    }
    
    public boolean isHistoryDefaultOperation() {
        return historyDefaultOperation;
    }
    
}

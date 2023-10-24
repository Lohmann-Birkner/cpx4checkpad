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
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.core.model.fx.button.GlyphIconButton;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author wilde
 */
public class RulesRiskCell extends CmRiskDocumentationListCell{

    private static final Logger LOG = Logger.getLogger(RulesRiskCell.class.getName());

    private final GlyphIconButton iconButton;
    
    public RulesRiskCell(CheckMode pType) {
        super(pType);
        iconButton = new GlyphIconButton();
//        iconButton.setOnAction(getOnAddEvent());
        setMenuItems(iconButton);
    }

    public GlyphIconButton getIconButton(){
        return iconButton;
    }
    private static final EventHandler<ActionEvent> DEFAULT_ADD_EVENT = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            LOG.info("add event called, no handler yet set!");
        }
    };
    private EventHandler<ActionEvent> onAddEvent = DEFAULT_ADD_EVENT;
    public void setOnAddEvent(EventHandler<ActionEvent> pEvent){
        onAddEvent = Objects.requireNonNullElse(pEvent, DEFAULT_ADD_EVENT);
//        iconButton.setOnAction(onAddEvent);
    }
    public EventHandler<ActionEvent> getOnAddEvent(){
        return onAddEvent;
    }
}
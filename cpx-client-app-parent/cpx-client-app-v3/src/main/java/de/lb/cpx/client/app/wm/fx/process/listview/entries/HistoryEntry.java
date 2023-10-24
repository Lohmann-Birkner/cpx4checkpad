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
package de.lb.cpx.client.app.wm.fx.process.listview.entries;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.EventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmOperations;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.button.GlyphIconButton;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 * Simple Pojo to store values for ListView
 *
 * @author wilde
 * @param <E> type
 */
public abstract class HistoryEntry<E extends AbstractEntity> {

    private static final Logger LOG = Logger.getLogger(HistoryEntry.class.getName());

    protected static final String DEFAULT_TITLE = "Titel:Event";
    protected static final String DEFAULT_CONTENT = "Content:Description";
//    public static final String PLACEHOLDER = "----";
//    public static final String PLACEHOLDER_ID = "--{0}--";

    protected final ProcessServiceFacade facade;
    protected final TWmEvent event;
    private String historyTitle;
    private String historyDescription;
    private String historyCreationUser;
    private boolean readOnly;
    private final List<Button> menuButtons = new ArrayList<>(); // = new ArrayList<>(Arrays.asList(new UpdateButton()));

//    public HistoryEntry(@NotNull ProcessServiceFacade pFacade, @NotNull WmEventTypeEn pType, @NotNull E pItem) {
//        Objects.requireNonNull(pType, "Event Type can not be null");
//        Objects.requireNonNull(pItem, "Item (Event Content) can not be null");
//        TWmEvent newEvent = new TWmEvent();
//        newEvent.setEventType(pType);
//        newEvent.setContent(pItem);
//        if (newEvent.isOrphaned()) {
//            throw new IllegalStateException(MessageFormat.format("passed content ({0}) does not match to the event type {1}", pItem, pType.name()));
//        }
//        facade = pFacade;
//        event = newEvent;
//        readOnly = true;
//    }
    public HistoryEntry(@NotNull ProcessServiceFacade pFacade, @NotNull TWmEvent pEvent, boolean pReadOnly) {
        Objects.requireNonNull(pFacade, "Facade can not be null");
        Objects.requireNonNull(pEvent, "Event can not be null");
        facade = pFacade;
        event = pEvent;
        readOnly = pReadOnly;

        initMenuButtons();
    }

    private void initMenuButtons() {
        final List<ItemEventHandler> defaultOps = getOperations().getDefaultOperations(getContent());
        if (defaultOps != null && !defaultOps.isEmpty()) {
            for (ItemEventHandler op : defaultOps) {
                GlyphIconButton button = new GlyphIconButton();
                button.setGlyph(ResourceLoader.getGlyph(op.getHistoryGlyph()));
                if (op.getHistoryTooltip() != null && !op.getHistoryTooltip().isEmpty()) {
                    button.setTooltip(new Tooltip(op.getHistoryTooltip()));
                }
                button.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        op.handle(null);
                    }
                });
                menuButtons.add(0, button);
            }
        }
    }

    public HistoryEntry(@NotNull ProcessServiceFacade pFacade, @NotNull TWmEvent pEvent) {
        this(pFacade, pEvent, false);
    }

    public boolean checkContent(String filteringText) {
//        return (eventLabel != null && eventLabel.getText() != null ? eventLabel.getText().toLowerCase().contains(filteringText.toLowerCase()) : false)
//                || (c_user != null && c_user.getText() != null ? c_user.getText().toLowerCase().contains(filteringText.toLowerCase()) : false);
        return StringUtils.containsIgnoreCase(getHistoryTitle(), filteringText)
                || StringUtils.containsIgnoreCase(getHistoryCreationUser(), filteringText)
                || StringUtils.containsIgnoreCase(getHistoryDescription(), filteringText);
    }

    public ProcessServiceFacade getFacade() {
        return facade;
    }

    public final TWmEvent getEvent() {
        return event;
    }

    public final String getHistoryTitle() {
        if (historyTitle == null) {
            historyTitle = createHistoryTitle();
        }
        return historyTitle;
    }

    public final String getHistoryDescription() {
        if (historyDescription == null) {
            final String text = createHistoryDescription();
            if (text != null && text.equalsIgnoreCase(event.getSubject())) {
                //avoid redundancy between event subject and description (can happen in process topic change event and process user change event)
                historyDescription = "";
            } else {
                historyDescription = text;
            }
        }
        return historyDescription;
    }

    public final String getHistoryCreationUser() {
        if (historyCreationUser == null) {
            historyCreationUser = createHistoryCreationUser();
        }
        return historyCreationUser;
    }

    public final String createHistoryTitle() {
//        if (event == null) {
//            return "";
//        }
        final WmEventTypeEn type = event.getEventType();
        if (type == null) {
            return EventSubject.PLACEHOLDER; //"----";
        }
        if (event.getSubject() != null && !event.getSubject().isEmpty()) {
            return event.getSubject();
        }
        if (event.isOrphaned()) {
            //content was deleted!
            return getDefaultText();
        }
        return getEventSubject().getText();
        //return type.getTranslation(getTextParameters()).value;
//        switch (type.getOperation()) {
//            case ADD:
//                return getAddedText();
//            case CHANGE:
//                return getChangedText();
//            case REMOVE:
//                return getRemovedText();
//            case OTHER:
//                return getOtherText();
//            default:
//                LOG.log(Level.WARNING, "found unknown type of event operation for event id {0}: {1}", new Object[]{event.id, type.getOperation().name()});
//                return getDefaultText();
//        }
//        if (pEvent.getSubject() == null || pEvent.getSubject().isEmpty()) {
//            return DEFAULT_TITLE;
//        }
    }

//    public String getAddedText() {
//        throw new UnsupportedOperationException("add/create operation is not supported by event of type " + event.getEventType().name());
//    }
//
//    public String getChangedText() {
//        throw new UnsupportedOperationException("change/edit/update operation is not supported by event of type " + event.getEventType().name());
//    }
//
//    public String getRemovedText() {
//        throw new UnsupportedOperationException("remove operation is not supported by event of type " + event.getEventType().name());
//    }
//    public String getSubject() {
//        return event.getSubject();
//    }
    public E getContent() {
        return getEventContent(event);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getEventContent(final TWmEvent pEvent) {
        if (pEvent == null) {
            return null;
        }
        if (pEvent.getEventType().isProcessFinalisationRelated()) {
            return (T) ((TWmProcessHospital) pEvent.getProcess()).getProcessHospitalFinalisation();
        } else {
            return (T) pEvent.getContent();
        }
    }

    public abstract EventSubject<E> getEventSubject();

//    public WmEventTypeEn getEventType() {
//        return event.getEventType();
//    }
//
//    public WmEventTypeEn.EventOperation getEventOperation() {
//        return getEventType().getOperation();
//    }
//    public String getRemovedText(E pItem) {
//        throw new UnsupportedOperationException("generation of remove text is not supported");
//    }
//    public String getOtherText() {
//        throw new UnsupportedOperationException("other operation is not supported by event of type " + event.getEventType().name());
//    }
    public String getDefaultText() {
//        if (pEvent == null) {
//            return "";
//        }
        if (event.getSubject() == null || event.getSubject().isEmpty()) {
            return DEFAULT_TITLE;
        } else {
            return event.getSubject();
        }
    }

//    public boolean isOrphaned() {
//        return event.isOrphaned();
//    }
    protected abstract String createHistoryDescription();

//    protected String createHistoryDescription(TWmEvent pEvent) {
//        if (pEvent == null) {
//            return "";
//        }
//        return pEvent.getDescription();
//    }
    private String createHistoryCreationUser() {
        if (/* pEvent == null || */event.getCreationUser() == null) {
            return EventSubject.PLACEHOLDER; //"----";
        }
        return MenuCache.instance().getUserFullNameForId(event.getCreationUser());
    }

    public final List<Button> getMenuButtons() {
        return menuButtons;
    }

    public final boolean isReadOnly() {
        return readOnly;
    }

    public final void setReadOnly(boolean pReadOnly) {
        readOnly = pReadOnly;
    }

//    public Glyph getUpdateButtonGlyph() {
//        return ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION);
//    }
//    
//    public String getUpdateButtonTooltip() {
//        return "Eintrag ändern";
//    }
//    
//    public void doDefaultOperation(TWmEvent pEvent) {
//        LOG.log(Level.WARNING, "No Default Operation found for Event: {0}", pEvent.getEventType().name());
//    }
//    
//    private class UpdateButton extends GlyphIconButton {
//        
//        public UpdateButton() {
//            super(getUpdateButtonGlyph());
//            setTooltip(new Tooltip(getUpdateButtonTooltip()));
//            addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    doDefaultOperation(getEvent());
//                }
//            });
//        }
//    }
    protected String getComment() {
        throw new UnsupportedOperationException("comment operation is not supported by event of type " + event.getEventType().name());
    }

    public Object[] getTextParameters() {
        return getEventSubject().getTextParameters();
    }

    public abstract WmOperations<E> getOperations();

    public List<ItemEventHandler> getDefaultOperations(final E pItem) {
        return getOperations().getDefaultOperations(pItem);
    }

    public void doDefaultOperation(TWmEvent pEvent) {
        if (pEvent == null) {
            return;
        }
        final E content = HistoryEntry.getEventContent(pEvent);
        final ItemEventHandler eh = getOperations().getFirstDefaultOperation(content);
        if (eh != null) {
            eh.handle(null);
        } else {
            LOG.log(Level.WARNING, "No Default Operation found for Event: {0}", pEvent.getEventType().name());
        }
    }

}

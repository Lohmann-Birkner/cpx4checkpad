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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.ReminderEventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmReminderOperations;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmReminder;

/**
 *
 * @author wilde
 */
public class ReminderHistoryEntry extends HistoryEntry<TWmReminder> {

//    private final ProcessServiceFacade facade;
    public ReminderHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
//        facade = pFacade;
    }

//    public ReminderHistoryEntry(ProcessServiceFacade pFacade, WmEventTypeEn pType, TWmReminder pItem) {
//        super(pFacade, pType, pItem);
//    }
//    @Override
//    public void doDefaultOperation(TWmEvent pEvent) {
//        if (pEvent == null) {
//            return;
//        }
//        EventHandler<Event> eh = getOperations().editItem(pEvent.getReminder()); //new WmReminderOperations(facade).editItem(pEvent.getReminder()); //new WmReminderDetails(facade, pEvent.getReminder()).editItem();
//        if (eh != null) {
//            eh.handle(null);
//            //facade.getProperties().put(WmHistorySection.REFRESH_DETAILS, null); ?
//        }
////        TWmReminder rem = pEvent.getReminder();
////        if (pEvent.getReminder() == null) {
////            return;
////        }
////        AddReminderDialog dialog = new AddReminderDialog(facade, rem);
////        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
////            @Override
////            public void accept(ButtonType t) {
////                if (ButtonType.OK.equals(t)) {
////                    //trigger refresh in history
////                    facade.getProperties().put(WmHistorySection.REFRESH_DETAILS, null);
////                }
////            }
////        });
//    }

//    @Override
//    public List<Button> getMenuButtons() {
//        if (isReadOnly()) {
//            return new ArrayList<>();
//        }
//        if (getEvent().getReminder() == null) {
//            return new ArrayList<>();
//        }
//        return super.getMenuButtons(); //To change body of generated methods, choose Tools | Templates.
//    }

//    @Override
//    public String getUpdateButtonTooltip() {
//        return "Wiedervorlage ändern";
//    }
//
//    @Override
//    public Glyph getUpdateButtonGlyph() {
//        return ResourceLoader.getGlyph("\uf0f3");
//    }

    @Override
    protected String createHistoryDescription() {
//        if (pEvent == null) {
//            return "Event is null?!";
//        }
        return event.isOrphaned() ? event.getDescription() : getComment();
    }

    @Override
    protected String getComment() {
        if (/* pEvent == null || */event.getReminder() == null) {
            return "";
        }
        return event.getReminder().getComment() != null ? event.getReminder().getComment() : "";
    }

    @Override
    public ReminderEventSubject getEventSubject() {
        return new ReminderEventSubject(event);
    }

    @Override
    public WmReminderOperations getOperations() {
        return new WmReminderOperations(facade);
    }

}

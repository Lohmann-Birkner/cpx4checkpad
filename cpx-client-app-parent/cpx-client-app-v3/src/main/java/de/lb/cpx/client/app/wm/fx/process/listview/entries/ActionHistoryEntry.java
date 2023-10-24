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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.ActionEventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmActionOperations;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmEvent;

/**
 * History Entry for Action
 *
 * @author wilde
 */
public class ActionHistoryEntry extends HistoryEntry<TWmAction> {

//    private final ProcessServiceFacade facade;
//    private MailButton mailButton;

    public ActionHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
//        Objects.requireNonNull(pFacade, "Facade can not be null");
////        Objects.requireNonNull(pEvent.getAction(), "Action can not be null for ActionHistoryEntry");
//        facade = pFacade;
    }

    public ActionHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent) {
        this(pFacade, pEvent, false);
    }

//    public ActionHistoryEntry(ProcessServiceFacade pFacade, WmEventTypeEn pType, TWmAction pItem) {
//        super(pFacade, pType, pItem);
//    }
//    @Override
//    public final List<Button> getMenuButtons() {
//        List<Button> buttons = super.getMenuButtons(); //To change body of generated methods, choose Tools | Templates.
//        if (isReadOnly()) {
//            return new ArrayList<>();
//        }
//        if (getEvent().getAction() == null) {
//            return new ArrayList<>();
//        }
//        if (mailButton == null) {
//            mailButton = new MailButton();
//            mailButton.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    EventHandler<Event> eh = getOperations().sendMailItem(getEvent().getAction());
//                    if (eh != null) {
//                        eh.handle(event);
//                    }
////                    MailCreator mailCreator = new MailCreator();
////                    mailCreator.sendMail(facade.getCurrentProcess(), getEvent().getAction());
//                }
//            });
//        }
//        if (!buttons.contains(mailButton)) {
//            buttons.add(0, mailButton);
//        }
////            buttons.add(new EditActionButton());
//        return buttons;
//    }

//    @Override
//    public void doDefaultOperation(TWmEvent pEvent) {
//        if (pEvent == null) {
//            return;
//        }
//        EventHandler<Event> eh = getOperations().editItem(pEvent.getAction()); //new WmActionOperations(facade).editItem(pEvent.getAction()); //new WmActionDetails(facade, pEvent.getAction()).editItem();
//        if (eh != null) {
//            eh.handle(null);
//        }
////        UpdateActionDialog dialog = new UpdateActionDialog(pEvent.getAction(), facade);
////        dialog.showAndWait();
//    }

//    @Override
//    public String getUpdateButtonTooltip() {
//        return "Aktion ändern";
//    }
//
//    @Override
//    public Glyph getUpdateButtonGlyph() {
//        return ResourceLoader.getGlyph("\uf0e5");
//    }

    @Override
    protected String createHistoryDescription() {
//        if (event == null) {
//            return "Event is null!?";
//        }
        return event.isOrphaned() ? event.getDescription() : getComment();
    }

    @Override
    protected String getComment() {
        if (/* pEvent == null || */event.getAction() == null) {
            return "";
        }
        return event.getAction().getComment() != null ? String.valueOf(event.getAction().getComment()) : "";
    }

    @Override
    public ActionEventSubject getEventSubject() {
        return new ActionEventSubject(event);
    }

    @Override
    public WmActionOperations getOperations() {
        return new WmActionOperations(facade);
    }

}

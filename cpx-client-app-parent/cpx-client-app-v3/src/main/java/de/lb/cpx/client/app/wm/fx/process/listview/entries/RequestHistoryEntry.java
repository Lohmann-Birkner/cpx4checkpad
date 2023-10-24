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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.RequestEventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmRequestOperations;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmRequest;

/**
 *
 * @author wilde
 */
public class RequestHistoryEntry extends HistoryEntry<TWmRequest> {
//    private final ProcessServiceFacade facade;

    public RequestHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
//        facade = pFacade;
    }

//    public RequestHistoryEntry(ProcessServiceFacade pFacade, WmEventTypeEn pType, TWmRequest pItem) {
//        super(pFacade, pType, pItem);
//    }
//    @Override
//    public void doDefaultOperation(TWmEvent pEvent) {
//        if (pEvent == null) {
//            return;
//        }
//        EventHandler<Event> eh = getOperations().editItem(pEvent.getRequest()); //new WmRequestOperations(facade).editItem(pEvent.getRequest());//new WmRequestDetails(facade, pEvent.getRequest()).editItem();
//        if (eh != null) {
//            eh.handle(null);
//        }
////        if (pEvent == null) {
////            return;
////        }
////        if (pEvent.getRequest() == null) {
//////            LOG.log(Level.WARNING, "event has no request!");
////            return;
////        }
////        UpdateRequestDialog dialog = createUpdateRequestDialog(facade, pEvent.getRequest());
////        if (dialog == null) {
////            return;
////        }
////        dialog.showAndWait();
//    }

//    @Override
//    public String getUpdateButtonTooltip() {
//        return "Anfrage ändern";
//    }
//
//    @Override
//    public Glyph getUpdateButtonGlyph() {
//        return ResourceLoader.getGlyph("\uf0ec");
//    }

    @Override
    protected String createHistoryDescription() {
//        if (pEvent == null) {
//            return "";
//        }
        return event.isOrphaned() ? event.getDescription() : getComment();
    }

//    @Override
//    public List<Button> getMenuButtons() {
//        if (isReadOnly()) {
//            return new ArrayList<>();
//        }
//        if (getEvent().getRequest() == null) {
//            return new ArrayList<>();
//        }
//        return super.getMenuButtons(); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    protected String getComment() {
        if (/* pEvent == null || */event.getRequest() == null) {
            return "";
        }
        return event.getRequest().getComment() != null ? event.getRequest().getComment() : "";
    }

    @Override
    public RequestEventSubject getEventSubject() {
        return new RequestEventSubject(event);
    }

    @Override
    public WmRequestOperations getOperations() {
        return new WmRequestOperations(facade);
    }

}

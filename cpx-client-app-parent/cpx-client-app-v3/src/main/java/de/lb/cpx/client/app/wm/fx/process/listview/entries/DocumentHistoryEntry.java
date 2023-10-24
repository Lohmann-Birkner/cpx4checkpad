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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.DocumentEventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmDocumentOperations;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmEvent;

/**
 *
 * @author wilde
 */
public class DocumentHistoryEntry extends HistoryEntry<TWmDocument> {

    public DocumentHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
    }

//    public DocumentHistoryEntry(ProcessServiceFacade pFacade, WmEventTypeEn pType, TWmDocument pItem) {
//        super(pFacade, pType, pItem);
//    }
//    @Override
//    public void doDefaultOperation(TWmEvent pEvent) {
//        if (pEvent == null) {
//            return;
//        }
//        EventHandler<Event> eh = getOperations().openItem(pEvent.getDocument()); //new WmDocumentOperations(facade).openItem(pEvent.getDocument()); //new WmDocumentDetails(facade, pEvent.getDocument()).openItem();
//        if (eh != null) {
//            eh.handle(null);
//        }
////        if (pEvent.getDocument() == null) {
////            return;
////        }
////        EjbProxy<ProcessServiceBeanRemote> processBean = Session.instance().getEjbConnector().connectProcessServiceBean();
////        DocumentManager.openDocument(pEvent.getDocument(), processBean.get());
//    }

//    @Override
//    public List<Button> getMenuButtons() {
//        if (isReadOnly()) {
//            return new ArrayList<>();
//        }
//        if (getEvent().getDocument() == null) {
//            return new ArrayList<>();
//        }
//        return super.getMenuButtons(); //To change body of generated methods, choose Tools | Templates.
//    }

//    @Override
//    public String getUpdateButtonTooltip() {
//        return "Dokument ändern";
//    }
//
//    @Override
//    public Glyph getUpdateButtonGlyph() {
//        return ResourceLoader.getGlyph("\uf0c6");
//    }

    @Override
    protected String createHistoryDescription() {
//        if (pEvent == null) {
//            return "";
//        }
        return event.getDescription();
    }

    @Override
    public DocumentEventSubject getEventSubject() {
        return new DocumentEventSubject(event);
    }

    @Override
    public WmDocumentOperations getOperations() {
        return new WmDocumentOperations(facade);
    }

}

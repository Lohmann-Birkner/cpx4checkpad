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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.ProcessEventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmProcessContinuedOperations;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmProcessOperations;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmProcessPausedOperations;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmProcessTopicChangedOperations;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmProcessUserChangedOperations;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;

/**
 *
 * @author wilde
 */
public class ProcessHistoryEntry extends HistoryEntry<TWmProcess> {

    public ProcessHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
    }

//    @Override
//    public void doDefaultOperation(TWmEvent pEvent) {
//        EventHandler<Event> eh = getOperations().editItem(pEvent.getProcess()); //new WmKainInkaOperations(facade).editItem(pEvent.getKainInka()); //new WmKainInkaDetails(facade, pEvent.getKainInka()).addInka();
//        if (eh != null) {
//            eh.handle(null);
//        }
////        super.doDefaultOperation(pEvent); //To change body of generated methods, choose Tools | Templates.
//    }

//    @Override
//    public List<Button> getMenuButtons() {
//        return new ArrayList<>();
//    }

//    @Override
//    public String getUpdateButtonTooltip() {
//        return "";
//    }
//
//    @Override
//    public Glyph getUpdateButtonGlyph() {
//        return null;
//    }

    @Override
    protected String createHistoryDescription() {
//        if (pEvent == null) {
//            return null;
//        }
        return event.getSubject();
    }

//    @Override
//    protected String createHistoryTitle(TWmEvent pEvent) {
//        if (pEvent == null || pEvent.getProcess() == null) {
//            return "";
//        }
//        if (pEvent.getEventType() == null) {
//            return "----";
//        }
//        switch (pEvent.getEventType()) {
//            default:
//            case processSubjectChanged:
//            case processUserChanged:
//            case processClosed:
//            case processReopened:
//                return pEvent.getEventType().getTranslation().getValue();
//        }
//    }
//    @Override
//    protected String getAddedText(TWmEvent pEvent) {
//        throw new UnsupportedOperationException("event operation " + pEvent.getEventType().getOperation().name() + " is not supported by event of type " + pEvent.getEventType().name());
//    }
    @Override
    protected String getComment() {
        if (/* pEvent == null || */event.getProcess() == null) {
            return "";
        }
        return event.getProcess().getComment() != null ? event.getProcess().getComment() : "";
    }

    @Override
    public ProcessEventSubject getEventSubject() {
        return new ProcessEventSubject(event);
    }

    @Override
    public WmProcessOperations getOperations() {
        if (WmEventTypeEn.processUserChanged.equals(event.getEventType())) {
            return new WmProcessUserChangedOperations(facade);
        }
        if (WmEventTypeEn.processSubjectChanged.equals(event.getEventType())) {
            return new WmProcessTopicChangedOperations(facade);
        }
        if (WmEventTypeEn.processPaused.equals(event.getEventType())) {
            return new WmProcessPausedOperations(facade);
        }
        if (WmEventTypeEn.processContinued.equals(event.getEventType())) {
            return new WmProcessContinuedOperations(facade);
        }
        return new WmProcessOperations(facade) {
            //this is just an empty dummy
        };
    }

}

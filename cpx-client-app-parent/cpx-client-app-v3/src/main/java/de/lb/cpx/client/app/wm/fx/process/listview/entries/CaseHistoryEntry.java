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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.CaseEventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmCaseOperations;
import de.lb.cpx.model.TCase;
import de.lb.cpx.wm.model.TWmEvent;

/**
 *
 * @author wilde
 */
public class CaseHistoryEntry extends HistoryEntry<TCase> {

//    private final ProcessServiceFacade facade;
    public CaseHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
//        facade = pFacade;
    }

//    public CaseHistoryEntry(ProcessServiceFacade pFacade, WmEventTypeEn pType, TCase pItem) {
//        super(pFacade, pType, pItem);
//    }
//    @Override
//    public void doDefaultOperation(TWmEvent pEvent) {
//        if (pEvent == null) {
//            return;
//        }
////        TWmProcessCase pc = WmServiceOverviewDetails.createFakeProcessCase(pEvent.getHosCase());
//        EventHandler<Event> eh = getOperations().openItem(pEvent.getHosCase()); //new WmServiceOverviewOperations(facade).editItem(pc); //new WmServiceOverviewDetails(facade, pEvent.getHosCase()).editItem();
//        if (eh != null) {
//            eh.handle(null);
//        }
////        TCase hcase = pEvent.getHosCase();
////        if (hcase == null) {
////            return;
////        }
////        if (facade.isCaseLocked(hcase.getId())) {
////            MainApp.showErrorMessageDialog(Lang.getItemLockedObj().getTooltip());
////            return;
////        }
////        facade.loadAndShow(TwoLineTab.TabType.CASE, pEvent.getHosCase().getId());
//    }

//    @Override
//    public String getUpdateButtonTooltip() {
//        return "Fall öffnen";
//    }

//    @Override
//    public List<Button> getMenuButtons() {
//        if (isReadOnly()) {
//            return new ArrayList<>();
//        }
//        if (getEvent().getHosCase() == null) {
//            return new ArrayList<>();
//        }
//        return super.getMenuButtons(); //To change body of generated methods, choose Tools | Templates.
//    }

//    @Override
//    public Glyph getUpdateButtonGlyph() {
//        return ResourceLoader.getGlyph("\uf016");
//    }

    @Override
    protected String createHistoryDescription() {
        return event.getDescription();
    }

    @Override
    public CaseEventSubject getEventSubject() {
        return new CaseEventSubject(event);
    }

    @Override
    public WmCaseOperations getOperations() {
        return new WmCaseOperations(facade);
    }
}

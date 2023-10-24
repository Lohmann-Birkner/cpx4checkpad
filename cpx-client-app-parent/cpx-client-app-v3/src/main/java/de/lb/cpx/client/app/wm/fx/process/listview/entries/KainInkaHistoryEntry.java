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
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.wm.model.TWmEvent;

/**
 *
 * @author wilde
 * @param <E> type
 */
public abstract class KainInkaHistoryEntry<E extends TP301KainInka> extends HistoryEntry<E> {

//    private final ProcessServiceFacade facade;
    public KainInkaHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
//        facade = pFacade;
    }

//    @Override
//    public final void doDefaultOperation(TWmEvent pEvent) {
//        if (pEvent == null) {
//            return;
//        }
//        EventHandler<Event> eh = getOperations().editItem((E) pEvent.getKainInka()); //new WmKainInkaOperations(facade).editItem(pEvent.getKainInka()); //new WmKainInkaDetails(facade, pEvent.getKainInka()).addInka();
//        if (eh != null) {
//            eh.handle(null);
//        }
////        TP301KainInka kainInka = pEvent.getKainInka();
////        if (kainInka != null && kainInka.isKain()) {
////            AddInkaMessageDialog inkaMessageDialog = new AddInkaMessageDialog(facade, kainInka);
////            inkaMessageDialog.showAndWait();
////        }
//    }

}

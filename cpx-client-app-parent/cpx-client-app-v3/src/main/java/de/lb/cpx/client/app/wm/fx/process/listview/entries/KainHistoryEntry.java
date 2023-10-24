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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.KainEventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmKainOperations;
import de.lb.cpx.model.TP301Kain;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.model.TP301KainInkaPvv;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.enums.Tp301Key30En;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wilde
 */
public class KainHistoryEntry extends KainInkaHistoryEntry<TP301Kain> {

    public static final String KAIN_PROCESS_REF_STORNO = "76";
    private static final Logger LOG = Logger.getLogger(KainHistoryEntry.class.getName());
//    private final ProcessServiceFacade facade;

    public KainHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
    }

//    @Override
//    public String getUpdateButtonTooltip() {
//        return "KAIN-Meldung anzeigen";
//    }

//    @Override
//    public List<Button> getMenuButtons() {
//        if (isReadOnly()) {
//            return new ArrayList<>();
//        }
//        if (getEvent().getKainInka() == null) {
//            return new ArrayList<>();
//        }
//        return super.getMenuButtons(); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    protected String createHistoryDescription() {
        if (/* pEvent == null || */event.getKainInka() == null) {
            return null;
        }
        if (isStorno(event)) {
            return "Diese Kain-Nachricht wurde storniert";
        } else {
            return getKainDescriptionText(event.getKainInka());
        }
    }

//    @Override
//    protected String createHistoryTitle(TWmEvent pEvent) {
//        if (pEvent == null) {
//            return "";
//        }
//        if (pEvent.getKainInka() == null) {
//            return pEvent.getSubject();
//        }
//        return Lang.getEventTypeKainReceived();
//    }
//    @Override
//    public String getAddedText() {
//        //return Lang.getEventTypeKainReceived();
//        return event.getEventType().getTranslation(getTextParameters()).value;
//    }
    protected static String getKainDescriptionText(TP301KainInka pKainInka) {
        if (pKainInka == null) {
            return EventSubject.PLACEHOLDER; //"----";
        }
        StringBuilder sb = new StringBuilder();
        for (TP301KainInkaPvv pvv : pKainInka.getKainInkaPvvs()) {
            if (sb.length() > 0) {
                sb.append("\r\n");
            }
            String key30 = pvv.getInformationKey30();
            if (key30 == null || key30.trim().isEmpty()) {
                LOG.log(Level.SEVERE, "TP301 Key 30 is empty!");
            }
            Tp301Key30En key30En = (Tp301Key30En) CpxEnumInterface.findEnum(Tp301Key30En.values(), key30);
            if (key30En == null) {
                LOG.log(Level.SEVERE, "This TP301 Key 30 seems to be invalid: {0}", key30);
            }
            final String key30Desc = key30En == null ? "Unknown key" : key30En.getTranslation().getValue();
            if(pKainInka instanceof TP301Kain){
                String kainDate = "Eingegangen: " + Lang.toDate(((TP301Kain)pKainInka).getReceivingDate()) +"\r\n";
                
                sb.append(kainDate).append(key30).append(": ");
            }
            sb.append(key30Desc);
        }
        return sb.toString();
    }

    private boolean isStorno(TWmEvent pEvent) {
        if (pEvent == null || pEvent.getKainInka() == null) {
            return true;
        }
        return pEvent.getKainInka() != null && pEvent.getKainInka().isKain() && KAIN_PROCESS_REF_STORNO.equals(pEvent.getKainInka().getProcessingRef());
    }

    @Override
    public KainEventSubject getEventSubject() {
        return new KainEventSubject(event);
    }

//    @Override
//    protected String getComment() {
//        if (/* pEvent == null || */ event.getKainInka() == null) {
//            return "";
//        }
//        return event.getKainInka().getComment() != null ? event.getKainInka().getComment() : "";
//    }
    @Override
    public WmKainOperations getOperations() {
        return new WmKainOperations(facade);
    }

}

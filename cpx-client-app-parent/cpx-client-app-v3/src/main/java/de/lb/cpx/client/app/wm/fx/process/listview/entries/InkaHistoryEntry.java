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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.InkaEventSubject;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmInkaOperations;
import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.wm.model.TWmEvent;

/**
 *
 * @author wilde
 */
public class InkaHistoryEntry extends KainInkaHistoryEntry<TP301Inka> {

    public static final String INKA_PROCESS_REF_STORNO = "76";

    public InkaHistoryEntry(ProcessServiceFacade pFacade, TWmEvent pEvent, boolean pReadOnly) {
        super(pFacade, pEvent, pReadOnly);
    }

//    @Override
//    public List<Button> getMenuButtons() {
//        if (isStorno(getEvent())) {
//            return new ArrayList<>();
//        }
//        if (isCanceled(getEvent())) {
//            return new ArrayList<>();
//        }
//        if (isReadOnly()) {
//            return new ArrayList<>();
//        }
//        if (getEvent().getKainInka() == null) {
//            return new ArrayList<>();
//        }
//        return super.getMenuButtons(); //To change body of generated methods, choose Tools | Templates.
//    }
//    @Override
//    public String getUpdateButtonTooltip() {
//        if (isStorno(getEvent())) {
//            return null;
//        }
//        if (isCanceled(getEvent())) {
//            return null;
//        }
//        return "INKA-Nachricht anpassen";
//    }
//
//    @Override
//    public Glyph getUpdateButtonGlyph() {
//        if (isStorno(getEvent())) {
//            return null;
//        }
//        if (isCanceled(getEvent())) {
//            return null;
//        }
//        return getUpdateGlyph();
//    }
    @Override
    protected String createHistoryDescription() {
        return getInkaDescriptionText(event);
    }

//    @Override
//    protected String createHistoryTitle(TWmEvent pEvent) {
//        if (pEvent == null || pEvent.getEventType() == null) {
//            return "----";
//        }
//        if (pEvent.getKainInka() == null) {
//            return pEvent.getSubject();
//        }
//        switch (pEvent.getEventType()) {
//            case inkaStored:
//                return Lang.getEventTypeInkaStored();
//            case inkaUpdated:
//                return Lang.getEventTypeInkaUpdated();
//            case inkaSent:
//                return Lang.getEventTypeInkaSent();
//            case inkaCancelled:
//                return Lang.getEventTypeInkaCancelled();
//            default:
//                return pEvent.getSubject();
//        }
//    }
//    @Override
//    public String getAddedText() {
//        return event.getEventType().getTranslation(getTextParameters()).value;
//        //return Lang.getEventTypeInkaStored();
//    }
//
//    @Override
//    public String getChangedText() {
//        return event.getEventType().getTranslation(getTextParameters()).value;
//        //return Lang.getEventTypeInkaUpdated();
//    }
//
//    @Override
//    public String getOtherText() {
//        switch (event.getEventType()) {
//            case inkaSent:
//                return event.getEventType().getTranslation(getTextParameters()).value; //Lang.getEventTypeInkaSent();
//            case inkaCancelled:
//                return event.getEventType().getTranslation(getTextParameters()).value; //Lang.getEventTypeInkaCancelled();
//            default:
//                LOG.log(Level.WARNING, "event operation {0} is not supported by event of type {1}", new Object[]{event.getEventType().getOperation().name(), event.getEventType().name()});
//                return event.getSubject();
//        }
//    }
    private String getInkaDescriptionText(TWmEvent pEvent) {
        if (pEvent == null || pEvent.getKainInka() == null) {
            return "";
        }

        if (isStorno(pEvent)) {
            return "Diese INKA-Nachricht wurde storniert";
        } else {
            return getInkaDescriptionText(pEvent.getKainInka());
        }
    }

    private String getInkaDescriptionText(TP301KainInka pInka) {
        return KainHistoryEntry.getKainDescriptionText(pInka); //is the same text actually
//        if (pInka == null) {
//            return PLACEHOLDER; //"----";
//        }
//        StringBuilder sb = new StringBuilder();
//        for (TP301KainInkaPvv pvv : pInka.getKainInkaPvvs()) {
//            if (sb.length() > 0) {
//                sb.append("\r\n");
//            }
//            String key30 = pvv.getInformationKey30();
//            if (key30 == null || key30.trim().isEmpty()) {
//                LOG.log(Level.SEVERE, "TP301 Key 30 is empty!");
//            }
//            Tp301Key30En key30En = (Tp301Key30En) CpxEnumInterface.findEnum(Tp301Key30En.values(), key30);
//            if (key30En == null) {
//                LOG.log(Level.SEVERE, "This TP301 Key 30 seems to be invalid: {0}", key30);
//            }
//            final String key30Desc = key30En == null ? "Unknown key" : key30En.getTranslation().getValue();
//            sb.append(key30).append(": ").append(key30Desc);
//        }
//        return sb.toString();
    }

    private boolean isStorno(TWmEvent pEvent) {
        if (pEvent == null || pEvent.getKainInka() == null) {
            return true;
        }
        return pEvent.getKainInka() != null && pEvent.getKainInka().isInka() && INKA_PROCESS_REF_STORNO.equals(pEvent.getKainInka().getProcessingRef());
    }

//    private Glyph getUpdateGlyph() {
//        final String code;
//        switch (getEvent().getEventType()) {
//            case inkaStored:
//                code = "\uf0c7"; //SAVE
//                break;
//            case inkaUpdated:
//                code = "\uf0c7"; //SAVE
//                break;
//            case inkaSent:
//                code = "\uf2c6"; //TELEGRAM
//                break;
//            case inkaCancelled:
//                code = "\uf05e"; //BAN
//                break;
//            default:
//                LOG.log(Level.WARNING, "unknown event type, cannot return glyph: {0}", getEvent().getEventType());
//                return null;
//        }
//        return ResourceLoader.getGlyph(code);
//    }
//    private boolean isCanceled(TWmEvent event) {
//        if (event == null || event.getEventType() == null) {
//            return false;
//        }
//        return WmEventTypeEn.inkaCancelled.equals(event.getEventType());
//    }
    @Override
    public InkaEventSubject getEventSubject() {
        return new InkaEventSubject(event);
    }

//    @Override
//    protected String getComment() {
//        if (/* pEvent == null || */event.getKainInka() == null) {
//            return "";
//        }
//        return event.getKainInka().getComment() != null ? event.getKainInka().getComment() : "";
//    }
    @Override
    public WmInkaOperations getOperations() {
        return new WmInkaOperations(facade);
    }

}

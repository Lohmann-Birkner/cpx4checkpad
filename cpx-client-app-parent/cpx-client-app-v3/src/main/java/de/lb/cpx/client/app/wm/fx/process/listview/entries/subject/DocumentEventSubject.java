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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.listview.entries.subject;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.text.MessageFormat;

/**
 * Simple Pojo to store values for ListView
 *
 * @author niemeier
 */
public class DocumentEventSubject extends EventSubject<TWmDocument> {

    public DocumentEventSubject(final WmEventTypeEn pType, final TWmDocument pItem) {
        super(pType, pItem);
    }

    public DocumentEventSubject(TWmEvent pEvent) {
        super(pEvent);
    }

    @Override
    public Object[] getTextParameters() {
        return new Object[]{
            getDocumentType(),
            getDocumentName()
        };
    }

    private String getDocumentType() {
//        if (pEvent == null || pEvent.getDocument() == null) {
//            return "";
//        }
        final Long internalId = item.getDocumentType();
        if (internalId == null || internalId.equals(0L)) {
            return PLACEHOLDER;
        }
        String cache = MenuCache.instance().getDocumentTypeName(internalId);
        return cache == null ? MessageFormat.format(PLACEHOLDER_ID, internalId) : cache; //"----";
    }

    private String getDocumentName() {
//        if (pEvent == null || pEvent.getDocument() == null) {
//            return "";
//        }
        return item.getName();
    }

    @Override
    public String getRemovedDescription() {
        return super.getRemovedDescription(Lang.getEventNameDocument()); //Dokument wurde gelöscht
    }

}

/* 
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model.enums;

import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 * Type of Reminder (for Mdk Request)
 *
 * @author shahin
 */
public enum ReminderTypeEn implements CpxEnumInterface<Integer> {
    DEFAULT(0, ""),
    DOCUMENT_DELIVERED(1, Lang.MDK_DOCUMENT_DELIVERED),
    CONTINUATION_FEE(2, Lang.MDK_CONTINUATION_FEE_PAID),
    SUBSEQUENT_PROCEEDING(3, Lang.MDK_SUBSEQUENT_PROCEEDING);

    private final int id;
    private final String langKey;

    private ReminderTypeEn(int id, String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public Translation getTranslation() {
        return Lang.get(getLangKey());
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    @Override
    public String getViewId() {
        return String.valueOf(id);
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }

    @Override
    public String getIdStr() {
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        return id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return getTranslation().getValue();//CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

}

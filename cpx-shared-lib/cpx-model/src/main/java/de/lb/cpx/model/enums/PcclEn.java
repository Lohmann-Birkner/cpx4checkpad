/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author gerschmann
 */
public enum PcclEn implements CpxEnumInterface<Integer> {
    PCCL0(0, Lang.RULES_CRIT_PCCL_TOOLTIP_2),
    PCCL1(1, Lang.RULES_CRIT_PCCL_TOOLTIP_3),
    PCCL2(2, Lang.RULES_CRIT_PCCL_TOOLTIP_4),
    PCCL3(3, Lang.RULES_CRIT_PCCL_TOOLTIP_5),
    PCCL4(4, Lang.RULES_CRIT_PCCL_TOOLTIP_6),
    PCCL5(5, Lang.RULES_CRIT_PCCL_TOOLTIP_9),
    PCCL6(6, Lang.RULES_CRIT_PCCL_TOOLTIP_10);

    private final int id;
    private final String langKey;

    private PcclEn(int id, String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    public static PcclEn getValue2Id(String group) {
        Integer id;
        try {
            id = Integer.parseInt(group);
        } catch (NumberFormatException ex) {
            return null;
        }
        for (PcclEn en : values()) {
            if (en.getId().equals(id)) {
                return en;
            }
        }
        return null;

    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    @Override
    public String getViewId() {
        return name();
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

}

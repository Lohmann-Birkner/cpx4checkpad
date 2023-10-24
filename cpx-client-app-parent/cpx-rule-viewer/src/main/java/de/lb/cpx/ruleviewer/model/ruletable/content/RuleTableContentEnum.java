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
package de.lb.cpx.ruleviewer.model.ruletable.content;

import com.google.common.collect.Lists;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.DischargeReason2En;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author wilde
 */
public enum RuleTableContentEnum {
    ADM_REASON(Lang.getAdmissionReason(), AdmissionReasonEn.class),
    ADM_REASON_2(Lang.getAdmissionReason2(), AdmissionReason2En.class),
    ADM_CAUSE(Lang.getAdmissionCause(), AdmissionCauseEn.class),
    ADM_MODE(Lang.getModeOfAdmission(), AdmissionModeEn.class),
    DIS_REASON(Lang.getDischargeReason(), DischargeReasonEn.class),
    DIS_REASON_2(Lang.getDischargeReason2(), DischargeReason2En.class),
    ICD_TYPE("Diagnose-Typ", IcdcRefTypeEn.class),
    ICD_LOC("Diagnose-Lokalisation", LocalisationEn.class),
    DRG_PARTITION("DRG-Partition", DrgPartitionEn.class),
    GENDER(Lang.getGender(), GenderEn.class),
    FREE_TEXT("Freitext", null);

    private final String title;
    private final Class<? extends CpxEnumInterface<?>> enumClass;
    private final CpxEnumInterface<?>[] values;

    private RuleTableContentEnum(String pTitle, Class<? extends CpxEnumInterface<?>> pClazz) {
        title = pTitle;
        enumClass = pClazz;
        values = pClazz != null ? pClazz.getEnumConstants() : null;
    }

    public String getTitle() {
        return title;
    }

    public Class<? extends CpxEnumInterface<?>> getEnumClass() {
        return enumClass;
    }

    public CpxEnumInterface<?>[] getValues() {
        return values;
    }

    public Boolean isEnum() {
        if (enumClass == null) {
            return false;
        }
        return enumClass.isEnum();
    }

    public static List<RuleTableContentEnum> getSortedList() {
        List<RuleTableContentEnum> list = Lists.newArrayList(values());
        list.sort(Comparator.comparing(RuleTableContentEnum::getTitle));
        return list;
    }

}

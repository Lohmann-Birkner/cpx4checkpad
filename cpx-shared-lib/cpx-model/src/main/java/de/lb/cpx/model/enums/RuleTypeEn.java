/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.checkpoint.server.data.caseRules.DatCaseRuleAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;

/**
 *
 * @author gerschmann
 */
public enum RuleTypeEn implements CpxEnumInterface<Integer> {
    STATE_NO(0, Lang.GENDER_UNDEFINED, ""), // unbestimmt
    STATE_WARNING(1, Lang.WARNING, "warning"),
    STATE_ERROR(2, Lang.ERROR, "error"),
    //replace with information to keep continuity
    STATE_SUGG(3, Lang.INFORMATION, "suggestion");//CASE_STATUS_SUGGESTION);
    //CASE_STATUS_SUGGESTION);

    private static final Logger LOG = Logger.getLogger(RuleTypeEn.class.getName());

    private final int id;
    private final String langKey;
    private final String internalKey;

    private RuleTypeEn(int id, String langKey, String internalKey) {
        this.id = id;
        this.langKey = langKey;
        this.internalKey = internalKey;
    }

    public static RuleTypeEn get2RuleType(int type) {

        switch (type) {
            case DatCaseRuleAttributes.STATE_NO:
                return STATE_NO;
            case DatCaseRuleAttributes.STATE_WARNING:
                return STATE_WARNING;
            case DatCaseRuleAttributes.STATE_ERROR:
                return STATE_ERROR;
            case DatCaseRuleAttributes.STATE_SUGG:
                return STATE_SUGG;
            default:
                LOG.log(Level.WARNING, "Unknown type: " + type);
        }
        return STATE_NO;
    }

    public String getInternalKey() {
        return internalKey;
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
    public Integer getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        return id;
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(RuleTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public String toString() {
        return Lang.get(getLangKey()).value;
    }

    public static RuleTypeEn findByInternalKey(@NotNull final String pKey) {
        Objects.requireNonNull(pKey, "Key can not be null");
        for (RuleTypeEn type : values()) {
            if (pKey.toLowerCase().equals(type.getInternalKey().toLowerCase())) {
                return type;
            }
        }
        return STATE_NO;
    }

    public static RuleTypeEn findById(final Integer pId) {
        return RuleTypeEnMap.getInstance().get(pId);
    }

    public static RuleTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find RuleTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class RuleTypeEnMap extends AbstractCpxEnumMap<RuleTypeEn, Integer> {

    private static final RuleTypeEnMap INSTANCE;

    static {
        INSTANCE = new RuleTypeEnMap();
    }

    protected RuleTypeEnMap() {
        super(RuleTypeEn.class);
    }

    public static RuleTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public RuleTypeEn[] getValues() {
        return RuleTypeEn.values();
    }

}

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

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enumeration for results of grouping. Code für die Zuordnung des Ergebnisses
 * genutzt wurde. 0 = Nicht für die DRG/PEPP-Zuordnung verwendet 1 = Für die
 * DRG/PEPP-Zuordnung verwendet, ausgenommen zur Erfüllung der zeitlichen
 * und/oder logischen Bedingung innerhalb eines OPD-Konstrukts 2 = verwendet zur
 * Erfüllung der logischen und zeitlichen Bedingung eines OPD-Konstrukts 3 =
 * Verwendung sowohl durch ein OPD-Konstrukt wie auch anderweitig (also 1+2)
 * (nur OPS)
 *
 * @author gerschmann
 */
public enum GroupRelevantCodeEn implements CpxEnumInterface<Integer> {

    notUsed(0, Lang.GROUP_RELEVANT_CODE_NOT_USED_FOR_DRG_PEPP_ASSIGNMENT),
    used(1, Lang.GROUP_RELEVANT_CODE_FOR_DRG_PEPP_MAPPING_USED_EXCEPT_TO_SATISFY_THE_TEMPORAL_AND_OR_LOGIC_CONDITION_WITHIN_AN_OPD_CONSTRUCT),
    usedWithLogicAndTime(2, Lang.GROUP_RELEVANT_CODE_USED_TO_MEET_THE_LOGICAL_AND_TEMPORAL_CONDITION_OF_OPD_CONSTRUCT),
    usedCombined(3, Lang.GROUP_RELEVANT_CODE_USING_BOTH_OPD_BY_A_CONSTRUCT_AS_ALSO_OTHERWISE);

    private static final Logger LOG = Logger.getLogger(GroupRelevantCodeEn.class.getName());

    private final int id;
    private final String langKey;

    private GroupRelevantCodeEn(final int id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    /*
  @Override
  public String toString(CpxLanguageInterface cpxLanguage) {
      return this.getViewId() + " - " + cpxLanguage.get(langKey);
  }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        return String.valueOf(id);
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(GroupRelevantCodeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public String getLangKey() {
        return langKey;
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

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static GroupRelevantCodeEn findById(final Integer pId) {
        return GroupRelevantCodeEnMap.getInstance().get(pId);
    }

    public static GroupRelevantCodeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find GroupRelevantCodeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class GroupRelevantCodeEnMap extends AbstractCpxEnumMap<GroupRelevantCodeEn, Integer> {

    private static final GroupRelevantCodeEnMap INSTANCE;

    static {
        INSTANCE = new GroupRelevantCodeEnMap();
    }

    protected GroupRelevantCodeEnMap() {
        super(GroupRelevantCodeEn.class);
    }

    public static GroupRelevantCodeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public GroupRelevantCodeEn[] getValues() {
        return GroupRelevantCodeEn.values();
    }

}

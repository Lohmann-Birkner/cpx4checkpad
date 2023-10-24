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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
/**
 * Enumeration for results of grouping.
 * Code für die Zuordnung des Ergebnisses genutzt wurde.
 * 0 = Nicht für die DRG/PEPP-Zuordnung verwendet
 * 1 = Für die DRG/PEPP-Zuordnung verwendet, ausgenommen zur Erfüllung der zeitlichen und/oder logischen Bedingung innerhalb eines OPD-Konstrukts
 * 2 = verwendet zur Erfüllung der logischen und zeitlichen Bedingung eines OPD-Konstrukts
 * 3 = Verwendung sowohl durch ein OPD-Konstrukt wie auch anderweitig (also 1+2) (nur OPS)
 *
 * @author gerschmann
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 * 0 = SRG (weder OR noch NonOR-Prozedur) 1 = OR-Prozedur 2 = NonOR-Prozedur 3 =
 * SRG mit Konflikt Zusatzkennzeichen 4 = OR-Prozedur mit Konflikt
 * Zusatzkennzeichen 5 = NonOR-Prozedur mit Konflikt Zusatzkennzeichen 6 = SRG
 * mit Konflikt OPS-Datum 7 = OR-Prozedur mit Konflikt OPS-Datum 8 =
 * NonOR-Prozedur mit Konflikt OPS-Datum 9 = SRG mit Konflikt Zusatzkennzeichen
 * und OPS-Datum A = OR-Prozedur mit Konflikt Zusatzkennzeichen und OPS-Datum B
 * = NonOR-Prozedur mit Konflikt Zusatzkennzeichen und OPS-Datum
 *
 * @author gerschmann
 */
public enum GrouperOpsResTypeEn implements CpxEnumInterface<Character> {
    Srg('0', Lang.GROUPER_OPS_RES_TYPE_SRG), //SRG (weder OR noch NonOR-Prozedur) "),
    OrOps('1', Lang.GROUPER_OPS_RES_TYPE_OR_OPS),//OR-Prozedur"),
    NorOps('2', Lang.GROUPER_OPS_RES_TYPE_NOR_OPS), //NonOR-Prozedur "),
    SrgInvalidLoc('3', Lang.GROUPER_OPS_RES_TYPE_SRG_INVALID_LOC),
    OrInvalidLoc('4', Lang.GROUPER_OPS_RES_TYPE_OR_INVALID_DATE),
    NorInvalidLoc('5', Lang.GROUPER_OPS_RES_TYPE_NOR_INVALID_LOC),
    SrgInvalidDate('6', Lang.GROUPER_OPS_RES_TYPE_SRG_INVALID_DATE),
    OrInvalidDate('7', Lang.GROUPER_OPS_RES_TYPE_OR_INVALID_DATE),
    NorInvalidDate('8', Lang.GROUPER_OPS_RES_TYPE_NOR_INVALID_DATE),
    SrgInvalidLocAndDate('9', Lang.GROUPER_OPS_RES_TYPE_SRG_INVALID_LOC_AND_DATE),
    OrInvalidLocAndDate('A', Lang.GROUPER_OPS_RES_TYPE_OR_INVALID_LOC_AND_DATE),
    NorInvalidLocAndDate('B', Lang.GROUPER_OPS_RES_TYPE_NOR_INVALID_LOC_AND_DATE);
    //SrgInvalidDateAndDate('C', Lang.GROUPER_OPS_RES_TYPE_SRG_INVALID_DATE_AND_DATE);

    private final char id;
    private final String langKey;

    private GrouperOpsResTypeEn(final char id, final String langKey) {
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
//            Logger.getLogger(GrouperOpsResTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public Character getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        //throw new UnsupportedOperationException("id as integer is not supported for " + getClass().getSimpleName());
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

    public static GrouperOpsResTypeEn findById(final Character pId) {
        return GrouperOpsResTypeEnMap.getInstance().get(pId);
    }

}

final class GrouperOpsResTypeEnMap extends AbstractCpxEnumMap<GrouperOpsResTypeEn, Character> {

    private static final GrouperOpsResTypeEnMap INSTANCE;

    static {
        INSTANCE = new GrouperOpsResTypeEnMap();
    }

    protected GrouperOpsResTypeEnMap() {
        super(GrouperOpsResTypeEn.class);
    }

    public static GrouperOpsResTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public GrouperOpsResTypeEn[] getValues() {
        return GrouperOpsResTypeEn.values();
    }

}

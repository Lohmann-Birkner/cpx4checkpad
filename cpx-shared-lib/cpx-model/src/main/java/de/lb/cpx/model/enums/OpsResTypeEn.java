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
 *    2018  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author Shahin
 */
public enum OpsResTypeEn implements CpxEnumInterface<Character> {
    SRG('0', Lang.getGrouperOpsResTypeSrg()),
    OrOps('1', Lang.getGrouperOpsResTypeOrOps()),
    NorOps('2', Lang.getGrouperOpsResTypeNorOps()),
    SrgInvalidLoc('3', Lang.getGrouperOpsResTypeSrgInvalidLoc()),
    OrOpsInvalidLoc('4', Lang.getGrouperOpsResTypeOrInvalidLoc()),
    NorOpsInvalidLoc('5', Lang.getGrouperOpsResTypeNorInvalidLoc()),
    SrgInvalidDate('6', Lang.getGrouperOpsResTypeSrgInvalidDate()),
    OrOpsInvalidDate('7', Lang.getGrouperOpsResTypeOrInvalidDate()),
    NorOpsInvalidDate('8', Lang.getGrouperOpsResTypeNorInvalidDate()),
    SrgInvalidLocAndDate('9', Lang.getGrouperOpsResTypeSrgInvalidLocAndDate()),
    OrOpsInvalidLocAndDate('A', Lang.getGrouperOpsResTypeOrInvalidLocAndDate()),
    NorOpsInvalidLocAndDate('B', Lang.getGrouperOpsResTypeNorInvalidLocAndDate());

    private final char id;
    private final String langKey;

    private OpsResTypeEn(final char id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

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

    /*
  @Override
  public String toString(final CpxLanguageInterface cpxLanguage) {
    return this.getViewId() + " - " + cpxLanguage.get(langKey);
  }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        //return String.valueOf(id);
        return name();
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
//            Logger.getLogger(OpsResTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static OpsResTypeEn findById(final Character pId) {
        return OpsResTypeEnMap.getInstance().get(pId);
    }

}

final class OpsResTypeEnMap extends AbstractCpxEnumMap<OpsResTypeEn, Character> {

    private static final OpsResTypeEnMap INSTANCE;

    static {
        INSTANCE = new OpsResTypeEnMap();
    }

    protected OpsResTypeEnMap() {
        super(OpsResTypeEn.class);
    }

    public static OpsResTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public OpsResTypeEn[] getValues() {
        return OpsResTypeEn.values();
    }

}

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
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author gerschmann
 */
public enum GrouperStatusEn implements CpxEnumInterface<String> {

    GST00(0, "00", Lang.GROUPER_STATUS_GST_OK), //Normale Gruppierung"),
    GST01(1, "01", Lang.GROUPER_STATUS_INVALID_PRINCIPAL_ICD),//Ungültige oder fehlende Hauptdiagnose"),
    GST02(2, "02", Lang.GROUPER_STATUS_INACCEPTABLE_PRINCIPAL_ICD), //Diagnosekode als Hauptdiagnose nicht zulässig"),
    GST03(3, "03", Lang.GROUPER_STATUS_INVALID_CASE), //Datensatz entspricht keinem der Kriterien für irgendeine DRG"),
    GST04(4, "04", Lang.GROUPER_STATUS_INVALID_AGE), //Ungültiges Alter"),
    GST05(5, "05", Lang.GROUPER_STATUS_INVALID_GENDER), //Ungültiges Geschlecht"),
    GST06(6, "06", Lang.GROUPER_STATUS_INVALID_ADM_DIS_REASON_MODE), //Ungültiger Entlas-sungsgrund, Aufnah-meanlass oder Auf-nahmegrund"),
    GST07(7, "07", Lang.GROUPER_STATUS_INVALID_WEIGHT), //Ungültiges Aufnahmegewicht"),
    GST08(8, "08", Lang.GROUPER_STATUS_INVALID_LOS); //Ungültige Verweildauer");//Ungültige Verweildauer");

    private final int id;
    private final String idStr;
    private final String langKey;

    private GrouperStatusEn(final int id, final String idStr, final String langKey) {
        this.id = id;
        this.idStr = idStr;
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
        return idStr;
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
//            Logger.getLogger(GrouperStatusEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public String getId() {
        return idStr;
    }

    @Override
    public String getIdStr() {
        return idStr;
    }

    @Override
    public int getIdInt() {
        return id;
    }

    public static GrouperStatusEn get2Id(String id) {
        if (id == null || id.isEmpty()) {
            return GST00;
        }
        for (GrouperStatusEn en : values()) {
            if (id.equals(en.getId())) {
                return en;
            }
        }
        return null;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static GrouperStatusEn findById(final String pId) {
        return GrouperStatusEnMap.getInstance().get(pId);
    }

}

final class GrouperStatusEnMap extends AbstractCpxEnumMap<GrouperStatusEn, String> {

    private static final GrouperStatusEnMap INSTANCE;

    static {
        INSTANCE = new GrouperStatusEnMap();
    }

    protected GrouperStatusEnMap() {
        super(GrouperStatusEn.class);
    }

    public static GrouperStatusEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public GrouperStatusEn[] getValues() {
        return GrouperStatusEn.values();
    }

}

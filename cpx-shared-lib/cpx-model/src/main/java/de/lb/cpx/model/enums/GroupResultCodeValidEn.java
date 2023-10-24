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
 * 0 = Gültiger Code; 1 = Ungültiger Code; 2 = Duplikat (nur Diagnosen) 3 =
 * Geschlechtskonflikt; 4 = Alterskonflikt
 *
 * @author gerschmann
 */
public enum GroupResultCodeValidEn implements CpxEnumInterface<Integer> {
    valid(0, Lang.GROUP_RESULT_CODE_VALID_VALID), //Gültiger Code"),
    invalid(1, Lang.GROUP_RESULT_CODE_VALID_INVALID),//Ungültiger Code"),
    duplicate(2, Lang.GROUP_RESULT_CODE_VALID_DUPLICATE),//Duplikat"),
    genderConflict(3, Lang.GROUP_RESULT_CODE_VALID_GENDER_CONFLICT),//Geschlechtskonflikt"),
    ageConflict(4, Lang.GROUP_RESULT_CODE_VALID_AGE_CONFLICT);//Alterskonflikt");
    //Alterskonflikt");

    private static final Logger LOG = Logger.getLogger(GroupResultCodeValidEn.class.getName());

    private final int id;
    private final String langKey;

    private GroupResultCodeValidEn(final int id, final String langKey) {
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
//            Logger.getLogger(GroupResultCodeValidEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static GroupResultCodeValidEn findById(final Integer pId) {
        return GroupResultCodeValidEnMap.getInstance().get(pId);
    }

    public static GroupResultCodeValidEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find GroupResultCodeValidEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class GroupResultCodeValidEnMap extends AbstractCpxEnumMap<GroupResultCodeValidEn, Integer> {

    private static final GroupResultCodeValidEnMap INSTANCE;

    static {
        INSTANCE = new GroupResultCodeValidEnMap();
    }

    protected GroupResultCodeValidEnMap() {
        super(GroupResultCodeValidEn.class);
    }

    public static GroupResultCodeValidEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public GroupResultCodeValidEn[] getValues() {
        return GroupResultCodeValidEn.values();
    }

}

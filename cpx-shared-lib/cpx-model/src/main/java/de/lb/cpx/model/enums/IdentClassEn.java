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

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author niemeier
 */
public enum IdentClassEn implements CpxEnumInterface<String> {
    id10("10", Lang.IDENT_CLASS_ID10),
    id11("11", Lang.IDENT_CLASS_ID11),
    id12("12", Lang.IDENT_CLASS_ID12),
    id13("13", Lang.IDENT_CLASS_ID13),
    id14("14", Lang.IDENT_CLASS_ID14),
    id15("15", Lang.IDENT_CLASS_ID15),
    id16("16", Lang.IDENT_CLASS_ID16),
    id17("17", Lang.IDENT_CLASS_ID17),
    id18("18", Lang.IDENT_CLASS_ID18),
    id19("19", Lang.IDENT_CLASS_ID19),
    id20("20", Lang.IDENT_CLASS_ID20),
    id21("21", Lang.IDENT_CLASS_ID21),
    id22("22", Lang.IDENT_CLASS_ID22),
    id26("26", Lang.IDENT_CLASS_ID26),
    id27("27", Lang.IDENT_CLASS_ID27),
    id29("29", Lang.IDENT_CLASS_ID29),
    id30("30", Lang.IDENT_CLASS_ID30),
    id31("31", Lang.IDENT_CLASS_ID31),
    id32("32", Lang.IDENT_CLASS_ID32),
    id33("33", Lang.IDENT_CLASS_ID33),
    id34("34", Lang.IDENT_CLASS_ID34),
    id35("35", Lang.IDENT_CLASS_ID35),
    id39("39", Lang.IDENT_CLASS_ID39),
    id40("40", Lang.IDENT_CLASS_ID40),
    id42("42", Lang.IDENT_CLASS_ID42),
    id43("43", Lang.IDENT_CLASS_ID43),
    id44("44", Lang.IDENT_CLASS_ID44),
    id45("45", Lang.IDENT_CLASS_ID45),
    id46("46", Lang.IDENT_CLASS_ID46),
    id47("47", Lang.IDENT_CLASS_ID47),
    id48("48", Lang.IDENT_CLASS_ID48),
    id49("49", Lang.IDENT_CLASS_ID49),
    id50("50", Lang.IDENT_CLASS_ID50),
    id51("51", Lang.IDENT_CLASS_ID51),
    id52("52", Lang.IDENT_CLASS_ID52),
    id53("53", Lang.IDENT_CLASS_ID53),
    id54("54", Lang.IDENT_CLASS_ID54),
    id57("57", Lang.IDENT_CLASS_ID57),
    id59("59", Lang.IDENT_CLASS_ID59),
    id60("60", Lang.IDENT_CLASS_ID60),
    id65("65", Lang.IDENT_CLASS_ID65),
    id66("66", Lang.IDENT_CLASS_ID66),
    id89("89", Lang.IDENT_CLASS_ID89),
    id93("93", Lang.IDENT_CLASS_ID93),
    id94("94", Lang.IDENT_CLASS_ID94),
    id95("95", Lang.IDENT_CLASS_ID95),
    id96("96", Lang.IDENT_CLASS_ID96),
    id97("97", Lang.IDENT_CLASS_ID97);

    private final String id;
    private final String langKey;

    private IdentClassEn(String id, String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return id;
    }

    @Override
    public int getIdInt() {
        return Integer.parseInt(id);
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
//            Logger.getLogger(IdentClassEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    public static IdentClassEn getStaticEnum(final String pValue) throws CpxIllegalArgumentException {
        String value = (pValue == null) ? "" : pValue.trim();
        if (value.length() > 2) {
            value = value.substring(0, 2);
        }
        //System.out.println("IDENT: " + value + ": " + pValue);
        if (value.equalsIgnoreCase("98") || value.equalsIgnoreCase("99")) {
            value = "97"; //97-99: Reserved for internal administrative-free use in the institutions
        }
        return (IdentClassEn) CpxEnumInterface.findEnum(IdentClassEn.values(), value);
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static IdentClassEn findById(final String pId) {
        return IdentClassEnMap.getInstance().get(pId);
    }

}

final class IdentClassEnMap extends AbstractCpxEnumMap<IdentClassEn, String> {

    private static final IdentClassEnMap INSTANCE;

    static {
        INSTANCE = new IdentClassEnMap();
    }

    protected IdentClassEnMap() {
        super(IdentClassEn.class);
    }

    public static IdentClassEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public IdentClassEn[] getValues() {
        return IdentClassEn.values();
    }

}

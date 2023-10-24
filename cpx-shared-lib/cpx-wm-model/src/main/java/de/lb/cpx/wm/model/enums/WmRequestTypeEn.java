/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model.enums;

import de.lb.cpx.model.enums.AbstractCpxEnumMap;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This enum is used to create different Request types. e.g: MKD, Audit, etc.
 *
 * @author Husser
 */
public enum WmRequestTypeEn implements CpxEnumInterface<Integer> {
    bege(Values.BEGE, Lang.REQUEST_TYPE_INSURANCE_ASSOCIATION),
    mdk(Values.MDK, Lang.REQUEST_TYPE_MDK),
    audit(Values.AUDIT, Lang.REQUEST_TYPE_EXAMINATION),
    insurance(Values.INSURANCE, Lang.REQUEST_TYPE_INSURANCE),
    other(Values.OTHER, Lang.REQUEST_TYPE_OTHER), 
    review(Values.REVIEW, Lang.REQUEST_TYPE_EV);

    private static final Logger LOG = Logger.getLogger(WmRequestTypeEn.class.getName());

    public static WmRequestTypeEn[] valuesNoReview() {
        WmRequestTypeEn[] vals = values();
        List<WmRequestTypeEn> list = new ArrayList<>();
        for(WmRequestTypeEn val: vals){
            if(val.equals(review)){
                continue;
            }
            list.add(val);
        }
        list = list.stream().sorted((v1, v2)->v1.toString().compareTo(v2.toString())).collect(Collectors.toList());
        WmRequestTypeEn[] ret = new  WmRequestTypeEn[list.size()] ;
        list.toArray(ret);
        return ret;
    }

    public static WmRequestTypeEn[] valuesWithReview() {
        WmRequestTypeEn[] vals = values();
        List<WmRequestTypeEn> list = Arrays.asList(vals);
        list = list.stream().sorted((v1, v2)->v1.toString().compareTo(v2.toString())).collect(Collectors.toList());
        WmRequestTypeEn[] ret = new  WmRequestTypeEn[list.size()] ;
        list.toArray(ret);
        return ret;
    }

    public static class Values {

        private Values() {
            //utility class needs no public constructor
        }

        public static final Integer BEGE = 1;
        public static final Integer MDK = 2;
        public static final Integer AUDIT = 3;
        public static final Integer INSURANCE = 4;
        public static final Integer OTHER = 5;
        public static final Integer REVIEW = 6;
    }

    private final int id;
    private final String langKey;

    private WmRequestTypeEn(int id, String langKey) {
        this.id = id;
        this.langKey = langKey;
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
    public String getLangKey() {
        return langKey;
    }

    /*
    @Override
    public String toString(final CpxLanguageInterface cpxLanguage) {
        return this.getViewId() + " - " + cpxLanguage.get(langKey);
    }
     */
    @Override
    public String toString() {

        return getTranslation().getValue();//CpxEnumInterface.toStaticString(getViewId(), getLangKey());

    }

    @Override
    public String getViewId() {
        return String.valueOf(id);
        //return name();        
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
//            Logger.getLogger(WmRequestTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static WmRequestTypeEn findById(final Integer pId) {
        return WmRequestTypeEnMap.getInstance().get(pId);
    }

    public static WmRequestTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find WmRequestTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static WmRequestTypeEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (WmRequestTypeEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class WmRequestTypeEnMap extends AbstractCpxEnumMap<WmRequestTypeEn, Integer> {

    private static final WmRequestTypeEnMap INSTANCE;

    static {
        INSTANCE = new WmRequestTypeEnMap();
    }

    protected WmRequestTypeEnMap() {
        super(WmRequestTypeEn.class);
    }

    public static WmRequestTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public WmRequestTypeEn[] getValues() {
        return WmRequestTypeEn.values();
    }

}

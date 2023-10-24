/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author niemeier
 */
public enum SearchListTypeEn implements CpxEnumInterface<String> {

    WORKING("WL", Lang.WORKING_LIST), //WL = Working List
    WORKFLOW("PL", Lang.WORKFLOW_LIST), //PL = Process List
    RULE("RL", Lang.RULE_LIST), //RL = Rule List
    QUOTA("QL", Lang.QUOTA_LIST), //QL = Quota List
    RULEWL("RLWL", "Rule Editor Working List"),
    LABORATORY("LB", "Laboratory Data List");

    private final String id;
    private final String langKey;

    private SearchListTypeEn(final String id, final String langKey) {
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
        throw new UnsupportedOperationException("id as integer is not supported for " + getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    public boolean isRuleList() {
        return this.equals(RULE);
    }

    public boolean isQuotaList() {
        return this.equals(QUOTA);
    }

    public boolean isWorkingList() {
        return this.equals(WORKING);
    }

    public boolean isWorkflowList() {
        return this.equals(WORKFLOW);
    }

    public boolean isRuleWorkingList() {
        return this.equals(RULEWL);
    }

    public boolean isLaboratoryDataList() {
        return this.equals(LABORATORY);
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

    public String getFileName() {
        //return FileUtils.validateFilename();
        final String text = getTranslation().getValue();
        return text.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(CsCaseTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static SearchListTypeEn findById(final String pId) {
        return SearchListTypeEnMap.getInstance().get(pId);
    }

}

final class SearchListTypeEnMap extends AbstractCpxEnumMap<SearchListTypeEn, String> {

    private static final SearchListTypeEnMap INSTANCE;

    static {
        INSTANCE = new SearchListTypeEnMap();
    }

    protected SearchListTypeEnMap() {
        super(SearchListTypeEn.class);
    }

    public static SearchListTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public SearchListTypeEn[] getValues() {
        return SearchListTypeEn.values();
    }

}

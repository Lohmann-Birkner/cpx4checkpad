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
 *    2019  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author shahin
 */
public enum CategoryEn implements CpxEnumInterface<String> {

    Category1("Category1", Lang.TEMPLATE_CATEGORY_MAIN),
    Category2("Category2", Lang.TEMPLATE_CATEGORY_SUB_FIRST),
    Category3("Category3", Lang.TEMPLATE_CATEGORY_SUB_SECOND);

    private final String id;
    private final String langKey;

    private CategoryEn(String id, String langKey) {
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

    @Override
    public String getViewId() {
        return id;
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static CategoryEn findById(final String pId) {
        String id = (pId == null) ? null : pId.toLowerCase().trim();
        if (id == null) {
            return null;
        }

        return CategoryEnMap.getInstance().get(id);
    }

}

final class CategoryEnMap extends AbstractCpxEnumMap<CategoryEn, String> {

    private static final CategoryEnMap INSTANCE;

    static {
        INSTANCE = new CategoryEnMap();
    }

    protected CategoryEnMap() {
        super(CategoryEn.class);
    }

    public static CategoryEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public CategoryEn[] getValues() {
        return CategoryEn.values();
    }

}

/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author niemeier
 */
public enum ExportTypeEn implements CpxEnumInterface<String> {

    CSV("CSV", "csv", Lang.EXPORT_TYPE_CSV), //WL = Working List
    EXCEL("Excel", "xlsx", Lang.EXPORT_TYPE_EXCEL),
    XML("Xml", "xml", Lang.EXPORT_TYPE_XML);//PL = Process List 

    private final String id;
    private final String fileExtension;
    private final String langKey;

    private ExportTypeEn(final String id, final String fileExtension, final String langKey) {
        this.id = id;
        this.fileExtension = fileExtension;
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

    public boolean isCsv() {
        return this.equals(CSV);
    }

    public boolean isExcel() {
        return this.equals(EXCEL);
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

    public String getFileExtension() {
        return fileExtension;
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

    public static ExportTypeEn findById(final String pId) {
        return ExportTypeEnMap.getInstance().get(pId);
    }

}

final class ExportTypeEnMap extends AbstractCpxEnumMap<ExportTypeEn, String> {

    private static final ExportTypeEnMap INSTANCE;

    static {
        INSTANCE = new ExportTypeEnMap();
    }

    protected ExportTypeEnMap() {
        super(ExportTypeEn.class);
    }

    public static ExportTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public ExportTypeEn[] getValues() {
        return ExportTypeEn.values();
    }

}

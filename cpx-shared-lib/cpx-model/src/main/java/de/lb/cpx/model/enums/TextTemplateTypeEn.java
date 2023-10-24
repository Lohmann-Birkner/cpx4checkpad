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
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nandola
 */
public enum TextTemplateTypeEn implements CpxEnumInterface<Integer> {

    ActionContext(1, Lang.TEXT_TEMPLATE_ACTION),
    ReminderContext(2, Lang.TEXT_TEMPLATE_REMINDER, 255),
    RequestContext(3, Lang.TEXT_TEMPLATE_REQUEST, 1500),
    ProcessFinalizationContext(4, Lang.TEXT_TEMPLATE_PROCESS_FINALISATION),
    CaseReportContext(5, Lang.TEXT_TEMPLATE_CASE_REPORT, 50000),
    InkaContext(6, Lang.TEXT_TEMPLATE_INKA);

    private static final Logger LOG = Logger.getLogger(TextTemplateTypeEn.class.getName());

    private final int id;
    private final String langKey;
    private long limit; // = 999999999

    private TextTemplateTypeEn(int id, String langKey, long limit) {
        this.id = id;
        this.langKey = langKey;
        this.limit = limit;
    }

    private TextTemplateTypeEn(int id, String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    public static TextTemplateTypeEn getFromTypeId(int typeId) {
        switch (typeId) {
            case 1:
                return ActionContext;
            case 2:
                return ReminderContext;
            case 3:
                return RequestContext;
            case 4:
                return ProcessFinalizationContext;
            case 5:
                return CaseReportContext;
            default:
                LOG.log(Level.WARNING, "Unknown type id: " + typeId);
        }
        return null;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    @Override
    public String getViewId() {
        return String.valueOf(id);
    }

    @Override
    public boolean isViewRelevant() {
        return true;
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
    public Integer getId() {
        return id;
    }

    public String getTextBasedOnContext() {
        switch (this) {
            case ActionContext:
                return "Aktion Kontext";
            case CaseReportContext:
                return "Fallreport Kontext";
            case ReminderContext:
                return "Wiedervorlage Kontext";
            case RequestContext:
                return "Anfrage Kontext";
            case InkaContext:
                return "INKA-Nachricht Kontext";
            case ProcessFinalizationContext:
                return "Vorgangsabschluss Kontext";
            default:
                //leave it empty
                return "";
        }
    }

}

final class TextTemplateTypeEnMap extends AbstractCpxEnumMap<TextTemplateTypeEn, Integer> {

    private static final TextTemplateTypeEnMap INSTANCE;

    static {
        INSTANCE = new TextTemplateTypeEnMap();
    }

    protected TextTemplateTypeEnMap() {
        super(TextTemplateTypeEn.class);
    }

    public static TextTemplateTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public TextTemplateTypeEn[] getValues() {
        return TextTemplateTypeEn.values();
    }

}

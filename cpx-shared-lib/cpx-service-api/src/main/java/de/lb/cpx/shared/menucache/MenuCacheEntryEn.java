/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.menucache;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.io.Serializable;

/**
 *
 * @author niemeier
 */
public enum MenuCacheEntryEn implements Serializable {

    ACTION_SUBJECTS("Action Subject",Lang.ACTION_SUBJECTS),
    DEADLINES("Deadline", Lang.PROCESS_PREVIEW_DEADLINES),
    DOCUMENT_TYPES("Document Type",Lang.DOCUMENT_TYPES),
    DRAFT_TYPES("Draft Type",Lang.DRAFT_TYPES),
    INSURANCES("Insurance",Lang.INSURANCES),
    INS_SHORT("Insurance Short Name","Insurance Short Name"), //needed??
    AUDIT_REASONS("Audit Reason",Lang.MDK_AUDIT_REASONS),
    MD_AUDIT_QUOTAS("MD Audit Quota",Lang.MD_AUDIT_QUOTA),
    REQUEST_STATES("Request State",Lang.MDK_STATUS),
    PROCESS_RESULTS("Process Result",Lang.PROCESS_FINALISATION_RESULTS),
    PROCESS_TOPICS("Process Topic",Lang.PROCESS_TOPICS),
    REMINDER_SUBJECTS("Reminder Subject",Lang.REMINDER_SUBJECTS),
    ROLES("Role",Lang.ROLES),
    RULES("Rule",Lang.CASE_RESOLVE_RULES),
    SEARCHLISTS("Search Lists",Lang.SETTING_SEARCH_LISTS),
    USERS("User",Lang.USERS),
    BASERATES("Baserates",Lang.CATALOG_DOWNLOAD_BASERATES),
    MD_MASTERDATA("MD Masterdata",Lang.MD_MASTERDATA),
    WORD_TEMPLATES("MS Word Templates",Lang.WORD_TEMPLATES),
    BOOKMARKS("Bookmarks",Lang.BOOKMARKS),
    TEXTTEMPLATES("Texttemplates",Lang.TEXTTEMPLATES),
    DRG("DRG",Lang.DRG_CATALOG),
    PEPP("PEPP",Lang.PEPP_CATALOG),
    HOSPITALS("Hospitals", Lang.CATALOG_DOWNLOAD_HOSPITALS);
    
    private final String label;
    private final String languageKey;

    MenuCacheEntryEn(final String pLabel,final String pLanguageKey) {
        this.label = pLabel;
        this.languageKey = pLanguageKey;
    }

    public String getLanguageKey() {
        return languageKey;
    }

    public String getLabel() {
        return label;
    }
    
    public Translation getTranslation() {
        return Lang.get(getLanguageKey());
    }

}

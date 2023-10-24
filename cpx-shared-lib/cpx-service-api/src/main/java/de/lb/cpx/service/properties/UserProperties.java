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
package de.lb.cpx.service.properties;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.shared.dto.HistoryFilter;
import de.lb.cpx.shared.dto.RuleFilterDTO;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dirk Niemeier
 */
@XmlRootElement(name = "userProperties")
@PropertyDoc(name = "Anwendereinstellungen")
public class UserProperties extends CpxProperties {

    private static final long serialVersionUID = 1L;

    public final Common common = new Common();
    public final Modules modules = new Modules();

    public UserProperties() {
    }

    public static UserProperties deserialize(final String pInput) {
        return XmlSerializer.deserialize(pInput, UserProperties.class);
    }

    public String serialize() {
        return XmlSerializer.serialize(this);
    }

//    public static void main(String[] args) {
//        UserProperties props = new UserProperties();
//        props.modules.processView.setSelectedDraftCategories(new long[]{3, 4, 6});
//        props.modules.processView.setHistoryFilter(new HistoryFilter("test1", new WmEventTypeEn[]{}));
//        for (Map.Entry<String, PropertyEntry<?>> entry : props.getAsMap().entrySet()) {
//            PropertyEntry<?> propEntry = entry.getValue();
//            System.out.println(entry.getKey() + ": " + propEntry.getValueAsString() + " --- " + propEntry.getName());
//        }
//    }
//    protected ArrayList<Integer> selected_column_ids = new ArrayList<>();
    //@XmlRootElement(name = "common")
    @XmlAccessorType(value = XmlAccessType.FIELD)
    @PropertyDoc(name = "Allgemein", desc = "Allgemeine Einstellungen")
    public static class Common implements Serializable {

        private static final long serialVersionUID = 1L;
        @PropertyDoc(name = "Seitenleiste", desc = "Seitenleiste öffnen/schließen")
        private Boolean pinnedSidebar;
        @PropertyDoc(name = "Sprache", desc = "Spracheinstellung")
        private String language;
        @PropertyDoc(name = "Größe der Suchlisten", desc = "Legt fest, wie viele Suchergebnisse vom Server (nach-)geladen werden")
        private Integer searchListFetchSize;
        @PropertyDoc(name = "Fallliste extern/lokal", desc = "Legt fest, ob in der Arbeitsliste lokale oder externe Falldetails angezeigt werden sollen")
        private Boolean workingListLocal;
        @PropertyDoc(name = "FilterListe Details Vorschau", desc = "Legt fest, ob in der Filterliste Details Vorschau angezeigt werden sollen")
        private Boolean isFilterListDetailsOverview;
        @PropertyDoc(name = "Letzte Ansicht im Client", desc = "Die letzte Ansicht im Client wird beim erneuten Start wiederhergestellt")
        private String recentClientScene;
        @PropertyDoc(name = "Ausgewählte Liste", desc = "Der zuletzt ausgewählte Suchfilter in einer Liste")
        private final HashMap<SearchListTypeEn, Long> selected_search_list = new HashMap<>();

        public String getLanguage() {
            return language;
        }

        public void setLanguage(final String pLanguage) {
            this.language = pLanguage;
        }

        public Boolean getPinnedSidebar() {
            return pinnedSidebar;
        }

        public void setPinnedSidebar(final Boolean pPinnedSidebar) {
            this.pinnedSidebar = pPinnedSidebar;
        }

        public Boolean getWorkingListLocal() {
            return workingListLocal;
        }

        public void setWorkingListLocal(final Boolean pWorkingListLocal) {
            this.workingListLocal = pWorkingListLocal;
        }

        public Boolean getFilterListDetailsOverview() {
            return isFilterListDetailsOverview;
        }

        public void setFilterListDetailsOverview(final Boolean isFilterListDetailsOverview) {
            this.isFilterListDetailsOverview = isFilterListDetailsOverview;
        }

        public String getRecentClientScene() {
            return recentClientScene;
        }

        public void setRecentClientScene(final String pRecentClientScene) {
            this.recentClientScene = pRecentClientScene;
        }

        public Integer getSearchListFetchSize() {
            return searchListFetchSize;
        }

        public void setSearchListFetchSize(final Integer pSearchListFetchSize) {
            this.searchListFetchSize = pSearchListFetchSize;
        }

        public void setSelectedSearchList(final SearchListTypeEn pList, final Long pSearchListId) {
            if (pList == null) {
                throw new IllegalArgumentException("List cannot be null!");
            }
            final Long id = (pSearchListId != null && pSearchListId.equals(0L) ? null : pSearchListId);
            selected_search_list.put(pList, id);
        }

        public Map<SearchListTypeEn, Long> getSelectedSearchList() {
            return selected_search_list;
        }

        public Long getSelectedSearchList(final SearchListTypeEn pList) {
            if (pList == null) {
                throw new IllegalArgumentException("List cannot be null!");
            }
            return selected_search_list.get(pList);
        }

    }

    @XmlAccessorType(value = XmlAccessType.FIELD)
    @PropertyDoc(name = "Module", desc = "Einstellungen zu einzelnen Modulen")
    public static class Modules implements Serializable {

        private static final long serialVersionUID = 1L;
        public final WorkingList workingList = new WorkingList();
        public final WorkflowList workflowList = new WorkflowList();
        public final DocumentImport documentImport = new DocumentImport();
        public final ProcessView processView = new ProcessView();
        public final CaseView caseView = new CaseView();
        public final RuleEditor ruleEditor = new RuleEditor();
        public final RuleFilter ruleFilter = new RuleFilter();

        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Arbeitsliste", desc = "Liste der Krankenhausfällen")
        public static class WorkingList implements Serializable {

            private static final long serialVersionUID = 1L;

            @PropertyDoc(name = "P21-Exporteinstellungen", desc = "Die zuletzt verwendeten Einstellungen für den P21-Export")
            private P21ExportSettings p21ExportSettings;

            public P21ExportSettings getP21ExportSettings() {
                return p21ExportSettings;
            }

            public void setP21ExportSettings(final P21ExportSettings pP21ExportSettings) {
                this.p21ExportSettings = pP21ExportSettings;
            }

        }

        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Vorgangsliste", desc = "Liste der Vorgänge")
        public static class WorkflowList implements Serializable {

            private static final long serialVersionUID = 1L;
            @PropertyDoc(name = "Alle Wiedervorlagen", desc = "Legt fest, ob nur die aktuelleste oder ob alle Wiedervorlagen in der Vorgangsliste angezeigt werden sollen")
            private Boolean allReminder;

            //RSH: 19042018 CPX-857
            public Boolean getWorkflowListAllReminder() {
                return allReminder;
            }

            public void setWorkflowListAllReminder(final Boolean pAllReminder) {
                this.allReminder = pAllReminder;
            }

        }

        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Dokumentenimport", desc = "Einstellungen zum Dokumentenimport")
        public static class DocumentImport implements Serializable {

            private static final long serialVersionUID = 1L;
            @PropertyDoc(name = "MS Office-Unterstützung", desc = "Legt fest, Microsoft Office für die Dokumentenumwandlung und Dokumentenerkennung genutzt werden soll")
            private Boolean documentImportOfficeEnabled;

            @PropertyDoc(name = "PDF anzeigen", desc = "Legt fest, ob PDFs im Dokumentenimport gerendert werden sollen")
            private Boolean documentImportShowPdfEnabled;

            public Boolean getDocumentImportOfficeEnabled() {
                return documentImportOfficeEnabled;
            }

            public void setDocumentImportOfficeEnabled(final Boolean pDocumentImportOfficeEnabled) {
                this.documentImportOfficeEnabled = pDocumentImportOfficeEnabled;
            }

            public Boolean getDocumentImportShowPdfEnabled() {
                return documentImportShowPdfEnabled;
            }

            public void setDocumentImportShowPdfEnabled(final Boolean pDocumentImportShowPdfEnabled) {
                this.documentImportShowPdfEnabled = pDocumentImportShowPdfEnabled;
            }

        }

        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Vorgangsansicht", desc = "Einstellungen zur Vorgangsansicht")
        public static class ProcessView implements Serializable {

            private static final long serialVersionUID = 1L;

            @PropertyDoc(name = "Vollständige Ergeignisbeschreibung", desc = "Legt fest, ob die Einträge in der Historie mit vollständigem Beschreibungstext angezeigt werden sollen")
            private Boolean showHistoryEventDetails;
            @PropertyDoc(name = "Gelöschte Ereignisse anzeigen", desc = "Legt fest, ob gelöschte Ereignisse in der Historie mit angezeigt werden sollen")
            private Boolean showHistoryDeleted;
            @PropertyDoc(name = "Zuletzt benutzter MDK", desc = "Der zuletzt benutzte MDK wird bei der nächsten Anfrage vorgeschlagen")
            private Long recentMdkInternalId;
            @PropertyDoc(name = "Filtereinstellungen in der Historie", desc = "Die zuletzt eingestellten Filter in der Ereignishistorie werden wiederhergestellt")
            private HistoryFilter historyFilter;
            @PropertyDoc(name = "Kategorien in der Vorlagengenerierung", desc = "Die zuletzt eingestellten Kategorien für die Vorlagengenerierung werden wiederhergestellt")
            private long[] selectedDraftCategories = new long[0];
            @PropertyDoc(name = "Vorgansansicht Titel", desc = "Legt fest, ob in der Vorgansansicht, Vorgangsart angezeigt werden sollen")
            private Boolean isWmMainFrameSubject;
            @PropertyDoc(name = "Vorgansansicht Titel", desc = "Legt fest, ob in der Vorgansansicht, Vorgangsstatus angezeigt werden sollen")
            private Boolean isWmMainFrameState;
            @PropertyDoc(name = "Vorgansansicht Titel", desc = "Legt fest, ob in der Vorgansansicht, Vorgangsnummer angezeigt werden sollen")
            private Boolean isWmMainFrameWVNumber;
            @PropertyDoc(name = "Vorgansansicht Titel", desc = "Legt fest, ob in der Vorgansansicht, Aktenzeichen (RA) angezeigt werden sollen")
            private Boolean isWmMainFrameFNLawer;
            @PropertyDoc(name = "Vorgansansicht Titel", desc = "Legt fest, ob in der Vorgansansicht, Aktenzeichen (Gericht) angezeigt werden sollen")
            private Boolean isWmMainFrameFNCourt;
            @PropertyDoc(name = "Vorgansansicht Titel", desc = "Legt fest, ob in der Vorgansansicht, Bearbeiter angezeigt werden sollen")
            private Boolean isWmMainFrameWvUser;
            @PropertyDoc(name = "Vorgansansicht Titel", desc = "Legt fest, ob die Information zur Prüfquote beim Anlegen einer MD-Anfrage immer angezeigt werden soll")
            private Boolean showAlwaysInfoForExaminateQuota;

            public long[] getSelectedDraftCategories() {
                return selectedDraftCategories == null ? new long[0] : selectedDraftCategories.clone();
            }

            public void setSelectedDraftCategories(final long[] pSelectedDraftCategories) {
                this.selectedDraftCategories = pSelectedDraftCategories == null ? new long[0] : pSelectedDraftCategories.clone();
            }

            public HistoryFilter getHistoryFilter() {
                return this.historyFilter;
            }

            public void setHistoryFilter(final HistoryFilter pHistoryFilter) {
                this.historyFilter = pHistoryFilter;
            }

            public Long getRecentMdkInternalId() {
                return recentMdkInternalId;
            }

            public void setRecentMdkInternalId(final Long pRecentMdkInternalId) {
                this.recentMdkInternalId = pRecentMdkInternalId;
            }

            public Boolean getShowHistoryEventDetails() {
                return showHistoryEventDetails;
            }

            public void setShowHistoryEventDetails(final Boolean pShowHistoryEventDetails) {
                this.showHistoryEventDetails = pShowHistoryEventDetails;
            }

            public Boolean getShowHistoryDeleted() {
                return showHistoryDeleted;
            }

            public void setShowHistoryDeleted(final Boolean pShowHistoryDeleted) {
                this.showHistoryDeleted = pShowHistoryDeleted;
            }

            public Boolean getWmMainFrameSubject() {
                return isWmMainFrameSubject;
            }

            public Boolean getWmMainFrameFNCourt() {
                return isWmMainFrameFNCourt;
            }

            public Boolean getWmMainFrameFNLawer() {
                return isWmMainFrameFNLawer;
            }

            public Boolean getAlwaysInfoForExaminateQuota() {
                return showAlwaysInfoForExaminateQuota;
            }

            public Boolean getWmMainFrameState() {
                return isWmMainFrameState;
            }

            public Boolean getWmMainFrameWVUser() {
                return isWmMainFrameWvUser;
            }

            public Boolean getWmMainFrameWVNumber() {
                return isWmMainFrameWVNumber;
            }

            public void setWmMainFrameSubject(boolean isWmMainFrameSubject) {
                this.isWmMainFrameSubject = isWmMainFrameSubject;
            }

            public void setWmMainFrameState(boolean isWmMainFrameState) {
                this.isWmMainFrameState = isWmMainFrameState;
            }

            public void setWmMainFrameWVNumber(boolean isWmMainFrameWVNumber) {
                this.isWmMainFrameWVNumber = isWmMainFrameWVNumber;
            }

            public void setWmMainFrameFNLawer(boolean isWmMainFrameFNLawer) {
                this.isWmMainFrameFNLawer = isWmMainFrameFNLawer;
            }

            public void setWmMainFrameFNCourt(boolean isWmMainFrameFNCourt) {
                this.isWmMainFrameFNCourt = isWmMainFrameFNCourt;
            }

            public void setWmMainFrameWvUser(boolean isWmMainFrameWvUser) {
                this.isWmMainFrameWvUser = isWmMainFrameWvUser;
            }

            public void setAlwaysInfoForExaminateQuota(boolean showAlwaysInfoForExaminateQuota) {
                this.showAlwaysInfoForExaminateQuota = showAlwaysInfoForExaminateQuota;
            }
        }

        @XmlRootElement(name = "caseView")
        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Fallansicht", desc = "Einstellungen zur Fallansicht/Fallsimulation")
        public static class CaseView implements Serializable {

            private static final long serialVersionUID = 1L;

            @PropertyDoc(name = "Automatisches Grouping", desc = "Legt fest, ob die Fälle bei Änderungen automatisch neu gegroupt werden sollen")
            private Boolean autoGrouping;

            public Boolean getAutoGrouping() {
                return autoGrouping;
            }

            public void setAutoGrouping(final Boolean pAutoGrouping) {
                this.autoGrouping = pAutoGrouping;
            }

        }

        @XmlRootElement(name = "ruleEditor")
        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Regeleditor", desc = "Einstellungen zum Regelditor")
        public static class RuleEditor implements Serializable {

            private static final long serialVersionUID = 1L;

            @PropertyDoc(name = "Konfliktstrategie", desc = "Die zuletzt ausgewählte Konfliktstrategie beim Kopieren von Regeln")
            private RuleImportCheckFlags ruleImportCheckFlag;
            @PropertyDoc(name = "Regeln überschreiben", desc = "Die letzte Einstellung, ob Regeln überschrieben werden sollen, wird wiederhergestellt")
            private RuleOverrideFlags ruleOverrideFlag;
            @PropertyDoc(name = "Zuletzt ausgewählter Reiter", desc = "Der zuletzt ausgewählte Reiter wird wiederhergestellt")
            private String recentRuleListTab;

            public String getRecentRuleListTab() {
                return recentRuleListTab;
            }

            public void setRecentRuleListTab(final String pRecentRuleListTab) {
                this.recentRuleListTab = pRecentRuleListTab;
            }

            public RuleImportCheckFlags getRuleImportCheckFlag() {
                return ruleImportCheckFlag;
            }

            public void setRuleImportCheckFlag(final RuleImportCheckFlags pRuleImportCheckFlag) {
                this.ruleImportCheckFlag = pRuleImportCheckFlag;
            }

            public RuleOverrideFlags getRuleOverrideFlag() {
                return ruleOverrideFlag;
            }

            public void setRuleOverrideFlag(final RuleOverrideFlags pRuleOverrideFlag) {
                this.ruleOverrideFlag = pRuleOverrideFlag;
            }

        }

        @XmlRootElement(name = "ruleFilter")
        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Regelfilter", desc = "Einstellungen zum Regelfilter")
        public static class RuleFilter implements Serializable {

            private static final long serialVersionUID = 1L;

            @PropertyDoc(name = "Filtereinstellungen in der Batchverwaltung", desc = "Die zuletzt eingestellten Filter in der Batchverwaltung werden wiederhergestellt")
            private RuleFilterDTO ruleFilterBatchAdm;

            @PropertyDoc(name = "Filtereinstellungen in der Fallliste", desc = "Die zuletzt eingestellten Filter in der Fallliste werden wiederhergestellt")
            private RuleFilterDTO ruleFilterCaseList;

            public RuleFilterDTO getRuleFilterCaseList() {
                return this.ruleFilterCaseList;
            }

            public void setRuleFilterCaseList(final RuleFilterDTO pRuleFilterDTO) {
                this.ruleFilterCaseList = pRuleFilterDTO;
            }

            public RuleFilterDTO getRuleFilterBatchAdm() {
                return this.ruleFilterBatchAdm;
            }

            public void setRuleFilterBatchAdm(final RuleFilterDTO pRuleFilterDTO) {
                this.ruleFilterBatchAdm = pRuleFilterDTO;
            }
        }

    }

}

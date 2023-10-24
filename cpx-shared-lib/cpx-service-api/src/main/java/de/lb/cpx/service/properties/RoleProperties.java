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

import de.lb.cpx.service.information.ConnectionString;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dirk Niemeier
 */
@XmlRootElement(name = "roleProperties")
@PropertyDoc(name = "Rollenberechtigungen")
public class RoleProperties extends CpxProperties {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RoleProperties.class.getName());

    public final Common common = new Common();
    public final Modules modules = new Modules();

    public RoleProperties() {
    }

    public static RoleProperties deserialize(final String pInput) {
        return XmlSerializer.deserialize(pInput, RoleProperties.class);
    }

//    public static void main(String[] args) {
//        RoleProperties props = new RoleProperties();
//        props.common.filter.setDatabases(new String[]{"dbsys1:TEST", "dbsys2:TEST2"});
//        for(Map.Entry<String, PropertyEntry<?>> entry: props.getAsMap().entrySet()) {
//            PropertyEntry<?> propEntry = entry.getValue();
//            //System.out.println(entry.getKey() + ": " + propEntry.getValueAsString() + " --- " + propEntry.getName());
//            System.out.println(entry.getValue().getPath() + ": " + propEntry.getValueAsString() + " --- " + propEntry.getName());
//        }
//    }    
//    public static void main(String[] args) {
//        RoleProperties props = new RoleProperties();
//        props.common.filter.setDatabases(new String[]{"dbsys1:TEST", "dbsys2:TEST2"});
//        PropertyEntry<?> root = props.getEntries();
//        printEntries(root);
//        final Set<PropertyEntry<?>> entries = root.getLeafEntries();
//        System.out.println(entries.size());
//    }
//
//    private static void printEntries(final PropertyEntry<?> pRoot) {
//        if (pRoot == null) {
//            return;
//        }
//        for (PropertyEntry<?> entry : pRoot.getEntries()) {
//            //System.out.println(entry.getName());
//            printEntry(1, entry);
//        }
//    }
//
//    private static void printEntry(final int pStage, PropertyEntry<?> pEntry) {
//        String pre = "";
//        for (int i = 1; i < pStage; i++) {
//            pre += "  ";
//        }
//        if (pEntry.isClass()) {
//            System.out.println(pre + pEntry.getName() + (pEntry.getDesc().isEmpty() ? "" : " --- " + pEntry.getDesc()));
//        } else {
//            System.out.println(pre + pEntry.getName() + " => " + pEntry.getValueAsString() + (pEntry.getDesc().isEmpty() ? "" : " --- " + pEntry.getDesc()));
//        }
//        for (PropertyEntry<?> e : pEntry.getEntries()) {
//            printEntry(pStage + 1, e);
//        }
//    }
    public boolean isDatabaseAllowed(final String pDatabase) {
        String database = (pDatabase == null) ? "" : pDatabase.trim();
        if (database.isEmpty()) {
            return false;
        }
        final ConnectionString cs = new ConnectionString(database);
        final String[] tmp = common.filter.getDatabases();
        if (tmp.length == 0) {
            return true;
        }
        for (int i = 0; i < tmp.length; i++) {
            final String db = tmp[i];
            if (db == null || db.trim().isEmpty()) {
                continue;
            }
            final ConnectionString csTmp = new ConnectionString(db);
            if (csTmp.getDatabase().isEmpty() && !csTmp.getPersistenceUnit().isEmpty()
                    && csTmp.getPersistenceUnit().equals(cs.getPersistenceUnit())) {
                //global access to dbsys1/dbsys2/dbsys3...
                return true;
            }
            if (cs.equals(csTmp)) {
                //access to specific database like dbsys1:TEST
                return true;
            }
        }
        return false;
    }

    public boolean isConfigSystemAllowed() {
        return modules.administration.rights.canConfigSystem();
    }

    public boolean isDeleteCaseAllowed() {
        return modules.caseManagement.rights.canDelete();
    }

    public boolean isCancelCaseAllowed() {
        return modules.caseManagement.rights.canCancel();
    }

    public boolean isUnlockCaseAllowed() {
        return modules.caseManagement.rights.canUnlock();
    }

    public boolean isDeleteProcessAllowed() {
        return modules.processManagement.rights.canDelete();
    }

    public boolean isUnlockProcessAllowed() {
        return modules.processManagement.rights.canUnlock();
    }

    public boolean isEditReminderAllowed() {
        return modules.processManagement.rights.canEditReminder();
    }

    public boolean isEditReminderOfOtherUserAllowed() {
        return modules.processManagement.rights.canEditReminderOfOtherUser();
    }
    
    public boolean isEditActionOfOtherUserAllowed() {
        return modules.processManagement.rights.canEditActionOfOtherUser();
    }
    
    public boolean isEditActionAllowed() {
        return modules.processManagement.rights.canEditAction();
    }

    public boolean isEditDocumentAllowed() {
        return modules.processManagement.rights.canEditDocument();
    }

    public boolean isEditDocumentOfOtherUserAllowed() {
        return modules.processManagement.rights.canEditDocumentOfOtherUser();
    }

    public boolean isEditRequestOfOtherUserAllowed() {
        return modules.processManagement.rights.canEditRequestOfOtherUser();
    }

    public boolean isDoFinalisationAllowed() {
        return modules.processManagement.rights.canDoFinalisation();
    }

    public boolean isEditRuleAllowed() {
        return modules.ruleManagement.rights.canEdit();
    }

    public boolean isBatchgroupingAllowed() {
        return modules.administration.rights.canBatchgrouping();
    }

    public boolean isCaseMergingAllowed() {
        return modules.administration.rights.canCaseMerging();
    }

    public boolean isExportDataAllowed() {
        return modules.administration.rights.canExportData();
    }

    public String serialize() {
        return XmlSerializer.serialize(this);
    }

    public boolean canSetRelevanceFlag() {
       return modules.ruleManagement.rights.canSetRelevanceFlag();
    }

    @XmlAccessorType(value = XmlAccessType.FIELD)
    @PropertyDoc(name = "Allgemein", desc = "Allgemeine Berechtigungseinstellungen")
    public static class Common implements Serializable {

        private static final long serialVersionUID = 1L;
//        public final CommonAttributes attributes = new CommonAttributes();
//        public final CommonRights rights = new CommonRights();
        public final CommonFilter filter = new CommonFilter();

//        @XmlAccessorType(value = XmlAccessType.FIELD)
//        public static class CommonRights implements Serializable {
//
//            private static final long serialVersionUID = 1L;
//        }
        @XmlAccessorType(XmlAccessType.FIELD)
        @PropertyDoc(name = "Filter", desc = "Allgemeine Filtereinschränkungen")
        public static class CommonFilter implements Serializable, Filter {

            private static final long serialVersionUID = 1L;

//            protected String fzs = "";
//            protected String fa = "";
//            protected String vip = "";
            @PropertyDoc(name = "Datenbanken", desc = "Liste von erlaubten Datenbanken")
            protected String[] databases;

            public String[] getDatabases() {
                return databases == null ? new String[0] : databases.clone();
            }

            public void setDatabases(final String[] pDatabases) {
                setDatabases(pDatabases == null ? null : Arrays.asList(pDatabases));
                //this.databases = pDatabases == null ? new String[0] : pDatabases.clone();
            }

            public void setDatabases(final List<String> pDatabases) {
                final List<String> dbsTmp = pDatabases == null ? null : new ArrayList<>(pDatabases);
                final String[] tmp;
                if (dbsTmp == null) {
                    tmp = new String[0];
                } else {
                    final List<String> dbs = new ArrayList<>();
                    final Iterator<String> it = dbsTmp.iterator();
                    while (it.hasNext()) {
                        String t = it.next();
                        if (t == null || t.trim().isEmpty()) {
                            continue;
                        }
                        final ConnectionString csTmp = new ConnectionString(t);
                        if (!csTmp.isValidCaseDb() && !csTmp.isValidMasterDb()) {
                            LOG.log(Level.WARNING, "This is not a valid database: " + t);
                            continue;
                        }
                        dbs.add(csTmp.connectionString);
                    }
                    //dbs.removeAll(Collections.singleton(null));
                    //dbs.removeAll(Collections.singleton(""));
                    if (dbs.isEmpty()) {
                        tmp = new String[0];
                    } else {
                        tmp = new String[dbs.size()];
                        dbs.toArray(tmp);
                    }
                }
                this.databases = tmp;
            }

        }

//        @XmlAccessorType(XmlAccessType.FIELD)
//        public static class CommonAttributes implements Serializable {
//
//            private static final long serialVersionUID = 1L;
//
////            public boolean fzsVerwalter = false;
//        }
    }

    @PropertyDoc(name = "Module", desc = "Berechtigungen zu einzelnen Modulen")
    @XmlAccessorType(value = XmlAccessType.FIELD)
    public static class Modules implements Serializable {

        private static final long serialVersionUID = 1L;
//        public final WorkingList workingList = new WorkingList();
        public final CaseManagement caseManagement = new CaseManagement();
        public final ProcessManagement processManagement = new ProcessManagement();
        //public final Administration administration = new Administration();
        public final RuleManagement ruleManagement = new RuleManagement();
        public final Administration administration = new Administration();

        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Fallverwaltung", desc = "Umgang mit Fällen")
        public static class CaseManagement implements Serializable {

            private static final long serialVersionUID = 1L;

            public final CaseManagementRights rights = new CaseManagementRights();
//            public final CaseManagementFilter filter = new CaseManagementFilter();

            @XmlAccessorType(value = XmlAccessType.FIELD)
            public static class CaseManagementRights implements Serializable, Rights {

                private static final long serialVersionUID = 1L;

                @PropertyDoc(name = "Fälle stornieren", desc = "Setzt den Fall in den Status \"storniert\" Der Fall wird nicht gelöscht.")
                private boolean canCancel = true;
                @PropertyDoc(name = "Fälle entsperren", desc = "Wird eine Fall trotz Sperre von mehreren Clients parallel geöffnet, kann das zu einem ungewolltem Überschreiben von Falldaten führen. \n"
                        + "Das Recht soll nur Nutzer zugeordnet werden, die sich dieses Effektes bewusst sind und für den korrekten Datenbestand verantwortlich sind. \n"
                        + "Ein manuelles Entsperren kann notwendig sein, wenn die Sperre auf den Fall verweist ist.\n"
                        + "\n"
                        + "Nutzer, die dieses Recht nicht haben müssen sich mit dem Besitzer der Fallsperre einigen wer den Fall bearbeiten darf.")
                private boolean canUnlock = true;
                @PropertyDoc(name = "Fälle löschen", desc = "Fall wird nach Sicherheitsabfrage gelöscht, ohne Möglichkeit der Wiederherstellung.\n"
                        + "Das Recht sollte nur Mitarbeitern zugeordnet werden, die Verantwortung für den korrekten Datenbestand haben. \n"
                        + "Es kann notwendig sein, z. Bsp. ältere Fälle aus Datenschutzgründen zu löschen. Das Recht sollte explizit erteilt werden und standardmäßig nicht für eine Rolle gesetzt sein.")
                private boolean canDelete = false;

                public boolean canCancel() {
                    return canCancel;
                }

                public void canCancel(boolean canCancel) {
                    this.canCancel = canCancel;
                }

                public boolean canUnlock() {
                    return canUnlock;
                }

                public void canUnlock(boolean canUnlock) {
                    this.canUnlock = canUnlock;
                }

                public boolean canDelete() {
                    return canDelete;
                }

                public void canDelete(boolean canDelete) {
                    this.canDelete = canDelete;
                }
            }

//            @XmlAccessorType(value = XmlAccessType.FIELD)
//            public static class CaseManagementFilter implements Serializable {
//
//                private static final long serialVersionUID = 1L;
//
//            }
        }

        @XmlAccessorType(value = XmlAccessType.FIELD)
        @PropertyDoc(name = "Vorgangsverwaltung", desc = "Umgang mit Vorgängen")
        public static class ProcessManagement implements Serializable {

            private static final long serialVersionUID = 1L;

            public final ProcessManagementRights rights = new ProcessManagementRights();
//            public final ProcessManagementFilter filter = new ProcessManagementFilter();

            @XmlAccessorType(value = XmlAccessType.FIELD)
            public static class ProcessManagementRights implements Serializable, Rights {

                private static final long serialVersionUID = 1L;

                @PropertyDoc(name = "Vorgänge entsperren", desc = "")
                private boolean canUnlock = true;
                @PropertyDoc(name = "Vorgänge löschen", desc = "Fall wird nach Sicherheitsabfrage gelöscht, ohne Möglichkeit der Wiederherstellung.\n"
                        + "Das Recht sollte nur Mitarbeitern zugeordnet werden, die Verantwortung für den korrekten Datenbestand haben. \n"
                        + "Es kann notwendig sein, z. Bsp. ältere Fälle aus Datenschutzgründen zu löschen. Das Recht sollte explizit erteilt werden und standardmäßig nicht für eine Rolle gesetzt sein.")
                private boolean canDelete = false;
                @PropertyDoc(name = "Wiedervolage bearbeiten", desc = "Für Nutzer die alle WV's nur lesen aber nicht ändern dürfen")
                private boolean canEditReminder = true;
                @PropertyDoc(name = "Aktionen bearbeiten", desc = "Berechtigung aus CP-alt so übernommen")
                private boolean canEditAction = true;
                @PropertyDoc(name = "Dokumente bearbeiten", desc = "Berechtigung aus CP-alt so übernommen")
                private boolean canEditDocument = true;
                @PropertyDoc(name = "Wiedervolage anderer Anwender ändern", desc = "Mit dieser Berechtigung wird die Bearbeitung \"fremder\" WV's verhindert.\n"
                        + "Einige Kunden nutzen das andere Kunden nicht. Als Initialeinstellung sollten ale Nutzer alle WV's bearbeiten können.")
                private boolean canEditReminderOfOtherUser = true;
                @PropertyDoc(name = "Dokumente anderer Anwender ändern", desc = "")
                private boolean canEditDocumentOfOtherUser = true;
                @PropertyDoc(name = "Anfragen anderer Anwender ändern", desc = "")
                private boolean canEditRequestOfOtherUser = true;
                @PropertyDoc(name = "Aktionen anderer Anwender ändern", desc = "")
                private boolean canEditActionOfOtherUser = true;
                @PropertyDoc(name = "Vorgänge abschließen", desc = "")
                private boolean canDoFinalisation = true;

                public boolean canUnlock() {
                    return canUnlock;
                }

                public void canUnlock(boolean canUnlock) {
                    this.canUnlock = canUnlock;
                }

                public boolean canDelete() {
                    return canDelete;
                }

                public void canDelete(boolean canDelete) {
                    this.canDelete = canDelete;
                }

                public boolean canEditReminder() {
                    return canEditReminder;
                }

                public void canEditReminder(boolean canEditReminder) {
                    this.canEditReminder = canEditReminder;
                }

                public boolean canEditAction() {
                    return canEditAction;
                }

                public void canEditAction(boolean canEditAction) {
                    this.canEditAction = canEditAction;
                }

                public boolean canEditDocument() {
                    return canEditDocument;
                }

                public void canEditDocument(boolean canEditDocument) {
                    this.canEditDocument = canEditDocument;
                }

                public boolean canEditReminderOfOtherUser() {
                    return canEditReminderOfOtherUser;
                }

                public void canEditReminderOfOtherUser(boolean canEditReminderOfOtherUser) {
                    this.canEditReminderOfOtherUser = canEditReminderOfOtherUser;
                }

                public boolean canEditDocumentOfOtherUser() {
                    return canEditDocumentOfOtherUser;
                }

                public void canEditDocumentOfOtherUser(boolean canEditDocumentOfOtherUser) {
                    this.canEditDocumentOfOtherUser = canEditDocumentOfOtherUser;
                }

                public boolean canEditRequestOfOtherUser() {
                    return canEditRequestOfOtherUser;
                }

                public void canEditRequestOfOtherUser(boolean canEditRequestOfOtherUser) {
                    this.canEditRequestOfOtherUser = canEditRequestOfOtherUser;
                }
                
                public boolean canEditActionOfOtherUser() {
                    return canEditActionOfOtherUser;
                }

                public void canEditActionOfOtherUser(boolean canEdit) {
                    this.canEditActionOfOtherUser = canEdit;
                }
                
                public boolean canDoFinalisation() {
                    return canDoFinalisation;
                }

                public void canDoFinalisation(boolean canDoFinalisation) {
                    this.canDoFinalisation = canDoFinalisation;
                }
            }

//            @XmlAccessorType(value = XmlAccessType.FIELD)
//            public static class ProcessManagementFilter implements Serializable {
//
//                private static final long serialVersionUID = 1L;
//            }
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @PropertyDoc(name = "Regelverwaltung", desc = "Umgang mit Regeln")
        public static class RuleManagement implements Serializable {

            private static final long serialVersionUID = 1L;

            public final RuleManagementRights rights = new RuleManagementRights();
//            public final RuleManagementFilter filter = new RuleManagementFilter();

            @XmlAccessorType(value = XmlAccessType.FIELD)
            public static class RuleManagementRights implements Serializable, Rights {

                private static final long serialVersionUID = 1L;

                @PropertyDoc(name = "Regeln ändern", desc = "Regel sollen nur von Nutzern geändert werden dürfen, die die nötigen Kenntnisse dazu haben, sich über die Auswirkungen im Klaren sind und dafür die Verantwortung tragen (z. Bsp. der Rolle Controller, nicht der Rolle MDK-Bearbeiter). Recht soll explizit zugewiesen werden.")
                private boolean canEdit = false;
                @PropertyDoc(name = "Relevanz - Flag setzen", desc = "Der Benutzer darf in Fallbearbeitung die Regeln markieren, die als besonders Erfolgreich für die Fallanalyse erscheinen")
                private boolean canSetRelevanceFlag = false;

                public boolean canEdit() {
                    return canEdit;
                }

                public void canEdit(boolean canEdit) {
                    this.canEdit = canEdit;
                }
                
                public boolean canSetRelevanceFlag() {
                    return canSetRelevanceFlag;
                }

                public void canSetRelevanceFlag(boolean canEdit) {
                    this.canSetRelevanceFlag = canEdit;
                }
            }

//            @XmlAccessorType(value = XmlAccessType.FIELD)
//            public static class RuleManagementFilter implements Serializable {
//
//                private static final long serialVersionUID = 1L;
//            }
//            @XmlAttribute
//            public boolean visible = false;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @PropertyDoc(name = "Administration", desc = "Umgang mit der WebApp")
        public static class Administration implements Serializable {

            private static final long serialVersionUID = 1L;

            public final AdministrationRights rights = new AdministrationRights();
//            public final AdministrationFilter filter = new AdministrationFilter();

            @XmlAccessorType(value = XmlAccessType.FIELD)
            public static class AdministrationRights implements Serializable, Rights {

                private static final long serialVersionUID = 1L;

                @PropertyDoc(name = "System konfigurieren", desc = "Änderungen an der Systemkonfiguration ebenso wie Datenimporte sollen nur von entsprechend verantwortlichen Nutzern durchgeführt werden können. Das Recht soll explizit zugewiesen werden. Um die Systemkonfiguration initial zu ermöglichen, muss es immer einen eingerichteten Standard-Admin-User geben: Namen admin.")
                private boolean canConfigSystem = false;
                @PropertyDoc(name = "Daten importieren", desc = "Setzt Recht \"System konfigurieren\" voraus. \n"
                        + "Bietet die Möglichkeit, die Berechtigungen zwischen allgemeiner Systemkonfiguration (Listenverwaltung, Templates etc.) und Datenimport zu unterscheiden.")
                private boolean canImportData = true;
                @PropertyDoc(name = "Datenbanken ändern", desc = "Setzt Recht \"System konfigurieren\" voraus. \n"
                        + "Bietet die Möglichkeit, die Berechtigungen zwischen allgemeiner Systemkonfiguration (Listenverwaltung, Templates etc.) und Datenbanken löschen, leeren und neu anlegen zu unterscheiden. \n"
                        + "Recht soll explizit zugewiesen werden.")
                private boolean canChangeDatabase = false;
                @PropertyDoc(name = "Kataloge bearbeiten", desc = "Der Nutzer muss die Möglichkeit haben, die Katalogwerte einsehen zu können auch ohne explizite Berechtigung.\n"
                        + "\n"
                        + "Für die Änderung der Katalogwerte ist die Berechtigung abzuprüfen, da diese nur von qualifizierten Personen (Controller) durchgeführt werden sollte, die sich über die Auswirkungen der Änderungen im Klaren und verantwortlich dafür sind.")
                private boolean canEditCatalog = true;
                @PropertyDoc(name = "Batchgrouping", desc = "Der Nutzer muss die Möglichkeit haben, die Batchgrouping auszuführen.")
                private boolean canBatchgrouping = true;
                @PropertyDoc(name = "Fallzusammenführung", desc = "Der Nutzer muss die Möglichkeit haben, die Fallzusammenführung auszuführen.")
                private boolean canCaseMerging = false;
                @PropertyDoc(name = "Daten exportieren", desc = "Der Nutzer muss die Möglichkeit haben, die P21- sowie Excel-/CSV- zu exportieren.")
                private boolean canExportData = false;

                public boolean canConfigSystem() {
                    return canConfigSystem;
                }

                public void canConfigSystem(boolean canConfigSystem) {
                    this.canConfigSystem = canConfigSystem;
                }

                public boolean canImportData() {
                    return canImportData;
                }

                public void canImportData(boolean canImportData) {
                    this.canImportData = canImportData;
                }

                public boolean canChangeDatabase() {
                    return canChangeDatabase;
                }

                public void canChangeDatabase(boolean canChangeDatabase) {
                    this.canChangeDatabase = canChangeDatabase;
                }

                public boolean canEditCatalog() {
                    return canEditCatalog;
                }

                public void canEditCatalog(boolean canEditCatalog) {
                    this.canEditCatalog = canEditCatalog;
                }

                public boolean canBatchgrouping() {
                    return canBatchgrouping;
                }

                public void canBatchgrouping(boolean canBatchgrouping) {
                    this.canBatchgrouping = canBatchgrouping;
                }

                public boolean canCaseMerging() {
                    return canCaseMerging;
                }

                public void canCaseMerging(boolean canCaseMerging) {
                    this.canCaseMerging = canCaseMerging;
                }

                public void canExportData(boolean canExportData) {
                    this.canExportData = canExportData;
                }

                public boolean canExportData() {
                    return canExportData;
                }
            }

//            @XmlAccessorType(value = XmlAccessType.FIELD)
//            public static class AdministrationFilter implements Serializable {
//
//                private static final long serialVersionUID = 1L;
//            }
//            @XmlAttribute
//            public boolean visible = false;
        }
    }

    @PropertyDoc(name = "Rechte", desc = "Berechtigungen")
    private static interface Rights extends Serializable {
    }

    @PropertyDoc(name = "Filter", desc = "Allgemeine Filtereinschränkungen")
    private static interface Filter extends Serializable {
    }

}

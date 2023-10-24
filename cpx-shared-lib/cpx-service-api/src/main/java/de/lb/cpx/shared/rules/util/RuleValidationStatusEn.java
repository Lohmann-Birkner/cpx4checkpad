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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.rules.util;

import de.lb.cpx.model.enums.AbstractCpxEnumMap;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Validation Information bei rules import
 *
 * @author gerschmann
 */
public enum RuleValidationStatusEn implements CpxEnumInterface<Integer> {
    NO_RULES_4_IMPORT_FOUND(0, Lang.NO_RULES_4_IMPORT_FOUND, ErrPrioEn.SEVERE), // die Datei beinhaltet keine Regeln
    SUCCESS(1, Lang.SUCCESS, ErrPrioEn.INFO),
    NEW_RULE(2, Lang.NEW_RULE, ErrPrioEn.INFO), // neue Regel
    NEW_RULE_TABLE(3, Lang.NEW_RULE_TABLE, ErrPrioEn.INFO),// neue Regeltabelle
    NEW_RULE_TYPE(4, Lang.NEW_RULE_TYPE, ErrPrioEn.INFO),// neuer Regeltyp 
    SAME_TABLE(5, Lang.SAME_TABLE, ErrPrioEn.INFO), // Tabelle mit der gleichen Inhalt in dem Pool vorhanden
    SAME_RULE_FOUND(6, Lang.SAME_RULE_FOUND, ErrPrioEn.INFO), // Regel mit gleichen Ident und der gleichen Inhalt in dem Pool vorhanden
    RULE_TYPE_FOUND_IN_DB(7, Lang.RULE_TYPE_FOUND_IN_DB, ErrPrioEn.INFO), // Regeltyp gefunden in DB
    SAME_RULE_OTHER_NUMBER(8, Lang.SAME_RULE_OTHER_NUMBER, ErrPrioEn.WARNING), // Regel mit gleichen Ident und anderem Nummer in dem Pool vorhanden
    SAME_RULE_OTHER_CAPTION(9, Lang.SAME_RULE_OTHER_CAPTION, ErrPrioEn.WARNING), // Regel mit gleichen Ident und abweichender Bezeichnung in dem Pool vorhanden
    SAME_RULE_OTHER_ERR_TYPE(10, Lang.SAME_RULE_OTHER_ERR_TYPE, ErrPrioEn.WARNING), // Regel mit gleichen Ident und anderem Regelverletzungstyp in dem Pool vorhanden
    SAME_RULE_OTHER_RULE_TYPE(11, Lang.SAME_RULE_OTHER_RULE_TYPE, ErrPrioEn.WARNING), // Regel mit gleichen Ident und anderem Regeltyp in dem Pool vorhanden
    SAME_RULE_OTHER_CATEGORY(12, Lang.SAME_RULE_OTHER_CATEGORY, ErrPrioEn.WARNING), // Regel mit gleichen Ident und anderer Kategorie in dem Pool vorhanden
    RULE_TYPE_NOT_FOUND(13, Lang.RULE_TYPE_NOT_FOUND, ErrPrioEn.WARNING),// Regeltyp is nicht gefunden
    RULE_TYPE_OTHER_DISPLAY_TEXT(14, Lang.RULE_TYPE_OTHER_DISPLAY_TEXT, ErrPrioEn.WARNING),// Regeltyp is nicht gefunden
    NO_TABLE_IN_XML_BUT_IN_DB_FOUND(15, Lang.NO_TABLE_IN_XML_BUT_IN_DB_FOUND, ErrPrioEn.WARNING), // Die in der regellogik benutzte Tabelle ist nicht in der XML gefunden, aber in DB vorhanden
    SAME_RULE_OTHER_LOGIC(16, Lang.SAME_RULE_OTHER_LOGIC, ErrPrioEn.SEVERE), // Regel mit gleichen Ident und abweichenden Logik in dem Pool vorhanden
    SAME_RULE_OTHER_SUGG_LOGIC(17, Lang.SAME_RULE_OTHER_SUGG_LOGIC, ErrPrioEn.SEVERE), // Regel mit gleichen Ident und abweichenden VorschlagsLogik in dem Pool vorhanden
    SAME_TABLE_OTHER_CONTENT(18, Lang.SAME_TABLE_OTHER_CONTENT, ErrPrioEn.SEVERE), // Tabelle mit der abweichenden Inhalt in dem Pool vorhanden
    SAME_TABLE_OTHER_COMMENT(19, Lang.SAME_TABLE_OTHER_COMMENT, ErrPrioEn.WARNING), // Tabelle mit der abweichenden Kommentar in dem Pool vorhanden
    NO_TABLE_FOUND(20, Lang.NO_TABLE_FOUND, ErrPrioEn.SEVERE), // Die in der regellogik benutzte Tabelle ist nicht in der XML und nicht in DB gefunden
    COLLISION(21, Lang.NO_TABLE_FOUND, ErrPrioEn.SEVERE), // Die Konflikte/Kollisionen, die bei Validierung entstanden sind
    ERROR(22, Lang.NO_TABLE_FOUND, ErrPrioEn.SEVERE), // XML kann nicht gelesen werden oder andere schwere Fehler, die den Import nicht möglich machen
    NO_RULE_TYPE_FOUND_4_RULE(23, Lang.NO_RULE_TYPE_FOUND_4_RULE, ErrPrioEn.SEVERE), // in der XML - Regel ist Regeltype definiert, der nicht in DB gefunden ist und micht importiert wurde
    NO_TABLE_FOUND_VALIDATION(24, Lang.NO_TABLE_FOUND_VALIDATION, ErrPrioEn.SEVERE), // Die in der regellogik benutzte Tabelle ist nicht in der XML und nicht in DB gefunden
    NO_RULE_TYPE_FOUND_4_RULE_VALIDATION(25, Lang.NO_RULE_TYPE_FOUND_4_RULE_VALIDATION, ErrPrioEn.WARNING), // Regeltyp is nicht gefunden, wird deim import angelegt 
    SAME_RULE_DIFFERENT_MESSAGE(26, Lang.SAME_RULE_DIFFERENT_MESSAGE, ErrPrioEn.WARNING),
    RULE_HAS_ERRORS(27, Lang.RULE_HAS_ERRORS, ErrPrioEn.SEVERE),
    RULE_TABLE_HAS_ERRORS(28, Lang.RULE_TABLE_HAS_ERRORS, ErrPrioEn.SEVERE);

    private static final Logger LOG = Logger.getLogger(RuleValidationStatusEn.class.getName());

    private final int id;
    private final String langKey;
    private final ErrPrioEn errPrio;

    private RuleValidationStatusEn(int id, String langKey, ErrPrioEn prio) {
        this.id = id; 
        this.langKey = langKey;
        errPrio = prio;
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
        return name();
    }

    @Override
    public boolean isViewRelevant() {
        return true;
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

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            LOG.log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public String toString() {
        return Lang.get(getLangKey()).value;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    public ErrPrioEn getErrPrio() {
        return errPrio;
    }

    public enum ErrPrioEn {
        INFO{
            public String toString(){
                return Lang.getRuleFilterDialogInformationRules();
            }
        },
        WARNING{
            public String toString(){
                return Lang.getRuleFilterDialogWarningRules();
            }
        }, 
        SEVERE{
            public String toString(){
                return Lang.getRuleFilterDialogErrorRules();
            }
        }
    }

    public static RuleValidationStatusEn findById(final Integer pId) {
        return RuleValidationStatusEnMap.getInstance().get(pId);
    }

    public static RuleValidationStatusEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find RuleValidationStatusEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class RuleValidationStatusEnMap extends AbstractCpxEnumMap<RuleValidationStatusEn, Integer> {

    private static final RuleValidationStatusEnMap INSTANCE;

    static {
        INSTANCE = new RuleValidationStatusEnMap();
    }

    protected RuleValidationStatusEnMap() {
        super(RuleValidationStatusEn.class);
    }

    public static RuleValidationStatusEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public RuleValidationStatusEn[] getValues() {
        return RuleValidationStatusEn.values();
    }

}

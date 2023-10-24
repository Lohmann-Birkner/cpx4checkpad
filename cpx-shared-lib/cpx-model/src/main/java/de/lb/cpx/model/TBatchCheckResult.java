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
package de.lb.cpx.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_BATCH_CHECK_RESULT"
 * beinhaltet die Ergebnisse der Einzelfälle, die durch den Groupervorgang mit
 * einem Groupermodell ermittelt wurden. </p >
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_BATCH_CHECK_RESULT",
        indexes = {
            @Index(name = "IDX_BAT_CHECK_RES4B2R_ID", columnList = "T_BATCH_RESULT_2_ROLE_ID", unique = false)})
@SuppressWarnings("serial")
public class TBatchCheckResult extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TBatchResult2Role batchResult2Role;
    private String bcheckresRuleType; // rule type from the xml file cstypes.xml(rtype_ident)
    private int bchechresErrCount; //number of rules of error type for this batch result and role
    private int bchechresWarnCount; // number of rules of warning type for this batch result and role
    private int batchresAdviceCount; // number of rules of the advice type for this batch result and role

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_BATCH_CHECK_RESULT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Referenz auf die Tabelle T_BATCH_RESULT_2_ROLE zurück.
     *
     * @return batchResult2Role
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_BATCH_RESULT_2_ROLE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_BAT_CHECK_RES4B2R_ID"))
    public TBatchResult2Role getBatchResult2Role() {
        return batchResult2Role;
    }

    /**
     *
     * @param batchResult2Role Column T_BATCH_RESULT_2_ROLE_ID :Referenz auf die
     * Tabelle T_BATCH_RESULT_2_ROLE
     */
    public void setBatchResult2Role(TBatchResult2Role batchResult2Role) {
        this.batchResult2Role = batchResult2Role;
    }

    /**
     * Gibt Regeltyp zurück, wie in der Tabelle cpx-common.CRG_RULE_TYPES oder
     * in der xml - Datei csrules_types.xml(rtype_ident) .
     *
     * @return bcheckresRuleType
     */
    @Column(name = "BCHKRES_RULE_TYPE", length = 50)
    public String getBcheckresRuleType() {
        return bcheckresRuleType;
    }

    /**
     *
     * @param bcheckresRuleType Column BCHKRES_RULE_TYPE :Regeltyp, wie in der
     * Tabelle cpx-common.CRG_RULE_TYPES oder in der xml - Datei
     * csrules_types.xml(rtype_ident) .
     */
    public void setBcheckresRuleType(String bcheckresRuleType) {
        this.bcheckresRuleType = bcheckresRuleType;
    }

    /**
     * Gibt Anzahl der Regelanschlägen von dem definierten Typ von Type ERROR
     * zurück.
     *
     * @return bchechresErrCount
     */
    @Column(name = "BCHECKRES_ERR_COUNT", precision = 5)
    public int getBchechresErrCount() {
        return bchechresErrCount;
    }

    /**
     *
     * @param bchechresErrCount Column BCHECKRES_ERR_COUNT :Anzahl der
     * Regelanschlägen von dem definierten Typ von Type ERROR .
     */
    public void setBchechresErrCount(int bchechresErrCount) {
        this.bchechresErrCount = bchechresErrCount;
    }

    /**
     * Gibt Anzahl der Regelanschlägen von dem definierten Typ von Type WARNING
     * zurück.
     *
     * @return bchechresWarnCount
     */
    @Column(name = "BCHECKRES_WARN_COUNT", precision = 5)
    public int getBchechresWarnCount() {
        return bchechresWarnCount;
    }

    /**
     *
     * @param bchechresWarnCount Column BCHECKRES_WARN_COUNT: Anzahl der
     * Regelanschlägen von dem definierten Typ von Type WARNING .
     */
    public void setBchechresWarnCount(int bchechresWarnCount) {
        this.bchechresWarnCount = bchechresWarnCount;
    }

    /**
     * Gibt Anzahl der Regelanschlägen von dem definierten Typ von Type ADVICE
     * zurück.
     *
     * @return batchresAdviceCount
     */
    @Column(name = "BCHECKRES_ADVICE_COUNT", precision = 5)
    public int getBatchresAdviceCount() {
        return batchresAdviceCount;
    }

    /**
     *
     * @param batchresAdviceCount Column BCHECKRES_ADVICE_COUNT:Anzahl der
     * Regelanschlägen von dem definierten Typ von Type ADVICE .
     */
    public void setBatchresAdviceCount(int batchresAdviceCount) {
        this.batchresAdviceCount = batchresAdviceCount;
    }

}

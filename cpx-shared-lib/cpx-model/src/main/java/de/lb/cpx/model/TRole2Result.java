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

import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
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
 * <p style="font-size:1em; color:green;">Die Tabelle "T_ROLE_2_RESULT" ist
 * Zuordnung der Regelprüfauswertung eines Falles zur entsprechenden Rolle.</p>
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_ROLE_2_RESULT",
        indexes = {
            @Index(name = "IDX_Role_2_RESULT_GRPRES_ID ", columnList = "T_GROUPING_RESULTS_ID")})
@SuppressWarnings("serial")
public class TRole2Result extends AbstractEntity {

    //   private long id;
    private static final Logger LOG = Logger.getLogger(TRole2Result.class.getName());

    private static final long serialVersionUID = 1L;

    private TGroupingResults groupingResults;
//    private TCheckResult r2rChkMaxDcwPos;
//    private TCheckResult r2rChkMinDcwNeg;
//    private TCheckResult r2rChkMaxDfeePos;
//    private TCheckResult r2rChkMinDfeeNeg;
    private int r2rErrorCount;
    private int r2rWarningCount;
    private int r2rAdwiseCount;
    private long roleId;
// this values would not be saved in the db, used only to save the min/mav values temporary    
//    private final double m_r2rChkMaxDcwPos = 0;
//    private final double m_r2rChkMinDcwNeg = 0;
//    private final double m_r2rChkMaxDfeePos = 0;
//    private final double m_r2rChkMinDfeeNeg = 0;
    private double minDcwNegative = 0;
    private double maxDcwPositive = 0;
    private double minDcwCareNegative = 0;
    private double maxDcwCarePositive = 0;
    private double minDfeeNegative = 0;
    private double maxDfeePositive = 0;
    private TCheckResult minDcwNegRef;
    private TCheckResult maxDcwPosRef;
    private TCheckResult minDfeeNegRef;
    private TCheckResult maxDfeePosRef;
    private TCheckResult minDcwCareNegRef;
    private TCheckResult maxDcwCarePosRef;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_ROLE_2_RESULT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf die Tablle T_GROUPING_RESULTS zurück.
     *
     * @return groupingResults
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "T_GROUPING_RESULTS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_Role_2_RESULT_GRPRES_ID"))
    public TGroupingResults getGroupingResults() {
        return groupingResults;
    }

    /**
     *
     * @param groupingResults Column T_GROUPING_RESULTS_ID : Verweis auf die
     * Tablle T_GROUPING_RESULTS.
     */
    public void setGroupingResults(TGroupingResults groupingResults) {
        this.groupingResults = groupingResults;
    }

    /**
     * Minimaler Delta-CW Wert ,welcher durch Regelanschläge für eine
     * Fallversion ermittelt wurde .
     *
     * @return minDcwNegative
     */
    @Column(name = "MIN_DCW_NEGATIVE", precision = 4, scale = 3)
    public double getMinDcwNegative() {
        return minDcwNegative;
    }

    /**
     *
     * @param minDcwNegative Column MIN_DCW_NEGATIVE : Minimaler Delta-CW Wert
     * ,welcher durch Regelanschläge für eine Fallversion ermittelt wurde .
     */
    public void setMinDcwNegative(double minDcwNegative) {
        this.minDcwNegative = minDcwNegative;
    }

    /**
     * Maximaler Delta-CW Wert ,welcher durch Regelanschläge für eine
     * Fallversion ermittelt wurde .
     *
     * @return maxDcwPositive
     */
    @Column(name = "MAX_DCW_POSITIVE", precision = 4, scale = 3)
    public double getMaxDcwPositive() {
        return maxDcwPositive;
    }

    /**
     *
     * @param maxDcwPositive Column MAX_DCW_POSITIVE : Maximaler Delta-CW Wert
     * ,welcher durch Regelanschläge für eine Fallversion ermittelt wurde .
     */
    public void setMaxDcwPositive(double maxDcwPositive) {
        this.maxDcwPositive = maxDcwPositive;
    }

    /**
     * Maximaler Delta-CW Pflege Wert ,welcher durch Regelanschläge für eine
     * Fallversion ermittelt wurde .
     *
     * @return maxDcwPositive
     */
    @Column(name = "MAX_DCW_CARE_POSITIVE", precision = 4, scale = 3)
    public double getMaxDcwCarePositive() {
        return maxDcwCarePositive;
    }

    /**
     *
     * @param maxDcwCarePositive Column MAX_DCW_CARE_POSITIVE : Maximaler Delta-CW Pflege Wert
     * ,welcher durch Regelanschläge für eine Fallversion ermittelt wurde .
     */
    public void setMaxDcwCarePositive(double maxDcwPositive) {
        this.maxDcwCarePositive = maxDcwPositive;
    }

    /**
     * Minimaler Delta-CW Pflege Wert ,welcher durch Regelanschläge für eine
     * Fallversion ermittelt wurde .
     *
     * @return minDcwNegative
     */
    @Column(name = "MIN_DCW_CARE_NEGATIVE", precision = 4, scale = 3)
    public double getMinDcwCareNegative() {
        return minDcwCareNegative;
    }

    /**
     *
     * @param minDcwCareNegative Column MIN_DCW_CARE_NEGATIVE : Minimaler Delta-CW Wert
     * ,welcher durch Regelanschläge für eine Fallversion ermittelt wurde .
     */
    public void setMinDcwCareNegative(double minDcwNegative) {
        this.minDcwCareNegative = minDcwNegative;
    }

    /**
     * Minimaler Delta-Entgelt-Wert ,welcher durch Regelanschläge für eine
     * Fallversion ermittelt wurde.
     *
     * @return minDfeeNegative
     */
    @Column(name = "MIN_DFEE_NEGATIVE", precision = 4, scale = 3)
    public double getMinDfeeNegative() {
        return minDfeeNegative;
    }

    /**
     *
     * @param minDfeeNegative Column MIN_DCW_NEGATIVE : Minimaler
     * Delta-Entgelt-Wert ,welcher durch Regelanschläge für eine Fallversion
     * ermittelt wurde.
     */
    public void setMinDfeeNegative(double minDfeeNegative) {
        this.minDfeeNegative = minDfeeNegative;
    }

    /**
     * Maximaler Delta-Entgelt-Wert ,welcher durch Regelanschläge für eine
     * Fallversion ermittelt wurde.
     *
     * @return maxDfeePositive
     */
    @Column(name = "MAX_DFEE_POSITIVE", precision = 4, scale = 3)
    public double getMaxDfeePositive() {
        return maxDfeePositive;
    }

    /**
     *
     * @param maxDfeePositive Column MAX_DFEE_POSITIVE : Maximaler
     * Delta-Entgelt-Wert ,welcher durch Regelanschläge für eine Fallversion
     * ermittelt wurde.
     */
    public void setMaxDfeePositive(double maxDfeePositive) {
        this.maxDfeePositive = maxDfeePositive;
    }

    /**
     * Verweis auf Eintrag in T_CHECK_RESULT (Regelanschlag welcher dieses
     * Ergebnis geliefert hat)
     *
     * @return minDcwNegRef
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"), name = "MIN_DCW_NEG_REF", nullable = true)
//  @Column(name = "MIN_DCW_NEG_REF",nullable = true)
    public TCheckResult getMinDcwNegRef() {
        return minDcwNegRef;
    }

    /**
     *
     * @param minDcwNegRef Column MIN_DCW_NEG_REF : Verweis auf Eintrag in
     * T_CHECK_RESULT (Regelanschlag welcher dieses Ergebnis geliefert hat .
     */
    public void setMinDcwNegRef(TCheckResult minDcwNegRef) {
        this.minDcwNegRef = minDcwNegRef;
    }

    /**
     * Verweis auf Eintrag in T_CHECK_RESULT (Regelanschlag welcher dieses
     * Ergebnis geliefert hat)"
     *
     * @return maxDcwPosRef
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"), name = "MAX_DCW_POS_REF", nullable = true)
//    @Column(name = "MAX_DCW_POS_REF",nullable = true)
    public TCheckResult getMaxDcwPosRef() {
        return maxDcwPosRef;
    }

    /**
     *
     * @param maxDcwPosRef Column MAX_DCW_POSITIVE : Maximaler Delta-CW Wert
     * ,welcher durch Regelanschläge für eine Fallversion ermittelt wurde .
     */
    public void setMaxDcwPosRef(TCheckResult maxDcwPosRef) {
        this.maxDcwPosRef = maxDcwPosRef;
    }

    /**
     * Verweis auf Eintrag in T_CHECK_RESULT (Regelanschlag welcher dieses
     * Ergebnis geliefert hat)
     *
     * @return minDfeeNegativeRef
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"), name = "MIN_DFEE_NEG_REF", nullable = true)
//    @Column(name = "MIN_DFEE_NEG_REF",nullable = true)
    public TCheckResult getMinDfeeNegRef() {
        return minDfeeNegRef;
    }

    /**
     *
     * @param minDfeeNegRef Column MIN_DFEE_NEG_REF : Verweis auf Eintrag in
     * T_CHECK_RESULT (Regelanschlag welcher dieses Ergebnis geliefert hat)".
     */
    public void setMinDfeeNegRef(TCheckResult minDfeeNegRef) {
        this.minDfeeNegRef = minDfeeNegRef;
    }

    /**
     * Verweis auf Eintrag in T_CHECK_RESULT (Regelanschlag welcher dieses
     * Ergebnis geliefert hat)
     *
     * @return maxDfeePositiveRef
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"), name = "MAX_DFEE_POS_REF", nullable = true)
// @Column(name = "MAX_DFEE_POS_REF",nullable = true)
    public TCheckResult getMaxDfeePosRef() {
        return maxDfeePosRef;
    }

    /**
     *
     * @param maxDfeePosRef Column MAX_DFEE_POS_REF : Verweis auf Eintrag in
     * T_CHECK_RESULT (Regelanschlag welcher dieses Ergebnis geliefert hat)".
     */
    public void setMaxDfeePosRef(TCheckResult maxDfeePosRef) {
        this.maxDfeePosRef = maxDfeePosRef;
    }

    /**
     * Gibt Anzahl der Prüfregeln vom Typ Fehler zurück.
     *
     * @return r2rErrorCount
     */
    @Column(name = "R2R_ERROR_COUNT", precision = 3, scale = 0)
    public int getR2rErrorCount() {
        return r2rErrorCount;
    }

    /**
     *
     * @param r2rErrorCount Column R2R_ERROR_COUNT : Anzahl der Prüfregeln vom
     * Typ Fehler .
     */
    public void setR2rErrorCount(int r2rErrorCount) {
        this.r2rErrorCount = r2rErrorCount;
    }

    /**
     * Gibt Anzahl der Prüfregeln vom Typ WARNING zurück.
     *
     * @return r2rWarningCount
     */
    @Column(name = "R2R_WARNING_COUNT", precision = 3, scale = 0)
    public int getR2rWarningCount() {
        return r2rWarningCount;
    }

    /**
     *
     * @param r2rWarningCount Column R2R_WARNING_COUNT: Anzahl der Prüfregeln
     * vom Typ WARNING an.
     */
    public void setR2rWarningCount(int r2rWarningCount) {
        this.r2rWarningCount = r2rWarningCount;
    }

    /**
     * Gibt Anzahl der Prüfregeln vom Typ Hinweis zurück.
     *
     * @return r2rAdwiseCount
     */
    @Column(name = "R2R_ADWISE_COUNT", precision = 3, scale = 0)
    public int getR2rAdwiseCount() {
        return r2rAdwiseCount;
    }

    /**
     *
     * @param r2rAdwiseCount Column R2R_ADWISE_COUNT : Anzahl der Prüfregeln vom
     * Typ Hinweis
     */
    public void setR2rAdwiseCount(int r2rAdwiseCount) {
        this.r2rAdwiseCount = r2rAdwiseCount;
    }

    /**
     * Gibt Verweis auf die Tablle cpx_common.CDB_USER_ROLES zurück.
     *
     * @return roleId
     */
    @Column(name = "USER_ROLE_ID", precision = 19, scale = 0, nullable = false)
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     * summarise the rule results to one case
     *
     * @param res Result
     * @param ruleType Rule Type
     */
    public void checkCheckResult(TCheckResult res, RuleTypeEn ruleType) {
        switch (ruleType) {
            case STATE_WARNING:
                r2rWarningCount++;
                break;
            case STATE_ERROR:
                r2rErrorCount++;
                break;
            case STATE_SUGG:
                r2rAdwiseCount++;
                break;
            default:
                LOG.log(Level.WARNING, "Unknown rule type: " + ruleType);
        }
        double simDiffFee = res.getChkFeeSimulDiff();
        if (simDiffFee > maxDfeePositive) {
            maxDfeePositive = simDiffFee;
            maxDfeePosRef = res;

        }
        if (simDiffFee < minDfeeNegative) {
            this.minDfeeNegative = simDiffFee;
            minDfeeNegRef = res;
        }

        double simDiff = res.getChkCwSimulDiff();
        if (simDiff > maxDcwPositive) {
            maxDcwPositive = simDiff;
            maxDcwPosRef = res;
        }
        if (simDiff < minDcwNegative) {
            minDcwNegative = simDiff;
            minDcwNegRef = res;

        }

        double simCareDiff = res.getChkCwCareSimulDiff();
        if (simCareDiff > maxDcwCarePositive) {
            maxDcwCarePositive = simDiff;
            maxDcwCarePosRef = res;
        }
        if (simCareDiff < minDcwCareNegative) {
            minDcwCareNegative = simCareDiff;
            minDcwCareNegRef = res;

        }

    }

    /**
     * Verweis auf Eintrag in T_CHECK_RESULT (Regelanschlag welcher dieses
     * Ergebnis geliefert hat)
     *
     * @return minDcwCareNegRef
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"), name = "MIN_DCW_CARE_NEG_REF", nullable = true)
    public TCheckResult getMinDcwCareNegRef() {
        return minDcwCareNegRef;
    }

    /**
     *
     * @param minDcwCareNegRef Column MIN_DCW_NEG_REF : Verweis auf Eintrag in
     * T_CHECK_RESULT (Regelanschlag welcher dieses Ergebnis geliefert hat .
     */
    public void setMinDcwCareNegRef(TCheckResult minDcwNegRef) {
        this.minDcwCareNegRef = minDcwNegRef;
    }

    /**
     * Verweis auf Eintrag in T_CHECK_RESULT (Regelanschlag welcher dieses
     * Ergebnis geliefert hat)"
     *
     * @return maxDcwCarePosRef
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"), name = "MAX_DCW_CARE_POS_REF", nullable = true)
//    @Column(name = "MAX_DCW_POS_REF",nullable = true)
    public TCheckResult getMaxDcwCarePosRef() {
        return maxDcwCarePosRef;
    }

    /**
     *
     * @param maxDcwCarePosRef Column MAX_DCW_POSITIVE : Maximaler Delta-CW Wert
     * ,welcher durch Regelanschläge für eine Fallversion ermittelt wurde .
     */
    public void setMaxDcwCarePosRef(TCheckResult maxDcwPosRef) {
        this.maxDcwCarePosRef = maxDcwPosRef;
    }
}

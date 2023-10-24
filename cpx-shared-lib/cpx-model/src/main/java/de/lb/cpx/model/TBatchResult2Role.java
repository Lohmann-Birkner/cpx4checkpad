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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <p style="font-size:1em; color:green;">Die Tabelle "T_BATCH_RESULT_2_Role"
 * speichert die Verteilung der CW-Differenzen nach Rollen. </p>
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_BATCH_RESULT_2_ROLE",
        indexes = {
            @Index(name = "IDX_BATCH_RES_2_ROLE4BATCH_ID", columnList = "T_BATCH_RESULT_ID", unique = false)})
@SuppressWarnings("serial")
public class TBatchResult2Role extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//    private long id;
    private TBatchResult batchResult;
    private long roleId;
    private double b2rMaxDcwPosSum = 0;
    private double b2rMinDcwNegSum = 0;
    private double b2rMaxDfeePosSum = 0;
    private double b2rMinDfeeNegSum = 0;
    private Set<TBatchCheckResult> batchCheckResult = new HashSet<>();
// temporary    
    private HashMap<String, TBatchCheckResult> type2checkResult = null;

    @Transient
    public HashMap<String, TBatchCheckResult> getType2checkResult() {
        if (type2checkResult == null) {
            type2checkResult = new HashMap<>();
        }
        return type2checkResult;
    }

    public void setType2checkResult(HashMap<String, TBatchCheckResult> type2checkResult) {
        this.type2checkResult = type2checkResult;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "batchResult2Role")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TBatchCheckResult> getBatchCheckResult() {
        return batchCheckResult;
    }

    public void setBatchCheckResult(Set<TBatchCheckResult> b2rBcheckResult) {
        this.batchCheckResult = b2rBcheckResult;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_BATCH_RESULT_2_ROLE_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf die Tablle T_BATCH_RESULT zurück.
     *
     * @return batchResult
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_BATCH_RESULT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_BATCH_RES_2_ROLE4BATCH_ID"))
    public TBatchResult getBatchResult() {
        return batchResult;
    }

    /**
     *
     * @param batchResult Column T_BATCH_RESULT_ID: Verweis auf die Tablle
     * T_BATCH_RESULT.
     */
    public void setBatchResult(TBatchResult batchResult) {
        this.batchResult = batchResult;
    }

    /**
     * Gibt Verweis auf die Tablle cpx_common.CDB_USER_ROLES zurück.
     *
     * @return roleId
     */
    @Column(name = "ROLE_ID", precision = 19, scale = 0, nullable = false)
    public long getRoleId() {
        return roleId;
    }

    /**
     *
     * @param roleId Column ROLE_ID:Verweis auf die Tablle
     * cpx_common.CDB_USER_ROLES.
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     * Gibt Summe der maximalen positiven dCWs der Fälle zurück.
     *
     * @return b2rMaxDcwPosSum
     */
    @Column(name = "B2R_MAX_DCW_POS_SUM", precision = 15, scale = 4, nullable = false)
    public double getB2rMaxDcwPosSum() {
        return b2rMaxDcwPosSum;
    }

    /**
     *
     * @param b2rMaxDcwPosSum Column B2R_MAX_DCW_POS_SUM : Summe der maximalen
     * positiven dCWs der Fälle .
     */
    public void setB2rMaxDcwPosSum(double b2rMaxDcwPosSum) {
        this.b2rMaxDcwPosSum = b2rMaxDcwPosSum;
    }

    /**
     * Gibt Summe der minimalen negatiiven dCWs der Fälle zurück.
     *
     * @return b2rMinDcwNegSum
     */
    @Column(name = "B2R_MIN_DCW_NEG_SUM", precision = 15, scale = 4, nullable = false)
    public double getB2rMinDcwNegSum() {
        return b2rMinDcwNegSum;
    }

    /**
     *
     * @param b2rMinDcwNegSum Column B2R_MIN_DCW_NEG_SUM:Summe der minimalen
     * negatiiven dCWs der Fälle.
     */
    public void setB2rMinDcwNegSum(double b2rMinDcwNegSum) {
        this.b2rMinDcwNegSum = b2rMinDcwNegSum;
    }

    /**
     * Gibt Summe der maximalen positiven dFees der Fälle zurück.
     *
     * @return b2rMaxDfeePosSum
     */
    @Column(name = "B2R_MAX_DFEE_POS_SUM", precision = 20, scale = 2, nullable = false)
    public double getB2rMaxDfeePosSum() {
        return b2rMaxDfeePosSum;
    }

    /**
     *
     * @param b2rMaxDfeePosSum Column B2R_MAX_DFEE_POS_SUM: Summe der maximalen
     * positiven dFees der Fälle.
     */
    public void setB2rMaxDfeePosSum(double b2rMaxDfeePosSum) {
        this.b2rMaxDfeePosSum = b2rMaxDfeePosSum;
    }

    /**
     * Gibt Summe der minimalen negatiiven dFeess der Fälle zurück.
     *
     * @return b2rMinDfeeNegSum
     */
    @Column(name = "B2R_MIN_DFEE_NEG_SUM", precision = 20, scale = 2, nullable = false)
    public double getB2rMinDfeeNegSum() {
        return b2rMinDfeeNegSum;
    }

    /**
     *
     * @param b2rMinDfeeNegSum Column B2R_MIN_DFEE_NEG_SUM :Summe der minimalen
     * negatiiven dFeess der Fälle.
     */
    public void setB2rMinDfeeNegSum(double b2rMinDfeeNegSum) {
        this.b2rMinDfeeNegSum = b2rMinDfeeNegSum;
    }

    /**
     * consolidates the results of the distribution of the numbers of rules of
     * the speciffic types to error types
     *
     * @param type2checkResult
     */
    void consolidateCheckResult(HashMap<String, TBatchCheckResult> t_type2checkResult) {
        getType2checkResult();
        Set<String> types = t_type2checkResult.keySet();
        for (String type : types) {
            TBatchCheckResult res = type2checkResult.get(type);
            TBatchCheckResult tres = t_type2checkResult.get(type);
            if (res == null) {
                type2checkResult.put(type, tres);
                tres.setBatchResult2Role(this);
                this.batchCheckResult.add(tres);
            } else {
                res.setBatchresAdviceCount(res.getBatchresAdviceCount() + tres.getBatchresAdviceCount());
                res.setBchechresErrCount(res.getBchechresErrCount() + tres.getBchechresErrCount());
                res.setBchechresWarnCount(res.getBchechresWarnCount() + tres.getBchechresWarnCount());
            }
        }
    }

}

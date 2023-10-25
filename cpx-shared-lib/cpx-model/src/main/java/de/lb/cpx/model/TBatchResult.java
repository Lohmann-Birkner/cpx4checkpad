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
 *    2016  wilde - add transient Methodes for PCCL,CW,LOS
 */
package de.lb.cpx.model;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
//import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_BATCH_RESULT"
 * speichert die statistischen Ergebnisse des Batchgrouping. </p>
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_BATCH_RESULT")
@SuppressWarnings("serial")
public class TBatchResult extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    // private long id;
    private boolean batchresIsLocalFl;
    private GDRGModel modelIdEn;
    private int batchresCaseCount; //Anzahl Fälle
    private int batchresGroupedCount; //Anzahl gegroupte Fälle
    private int batchresHtpCount; //OGVD
    private int batchresLtpCount; //UGVD

    private int batchresTransfCount; //Anzahl Verlegungen (korr. Verl.)
    private int batchresErrDrgCount; //Fehler DRG
    private int batchresAuxdCount; //ND - fuer NDI ND/CaseCount
    private int batchresDeadCount; //Anzahl der Toten
    private int batchresDayCareCount; //Teil-/Vorstationare
    private int batchresPcclSum; //PCCL
    private int batchresCareDaysSum; //Pflegetage
    private int batchresCaseIntensivCount; //Anzahl der Fälle mit Intensivtagen
    private int batchresLosIntensivSum; // Summe VWD intensiv (Intensivtage) 
    private double batchresCwEffectivSum; //cw pos - alle Regeln -alle Rollen
    private double batchresCwCatalogSum; //cw neg - alle Regeln -alle Rollen
    private int batchresNalosSum = 0; //NALOS
    private int batchresAux9Count = 0; // #.9 Diag
    private int rulesCount = 0;
    private Set<TBatchResult2Role> batchres2role = new HashSet<>(0);
    private TBatchGroupParameter batchGroupParameter;

    private HashMap<Long, TBatchResult2Role> role2result2role = null;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_BATCH_RESULT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt ein Flag 1/0 zurück ,der definiert, zu welcher Version der Fälle
     * Ergebnis des Batchgroupens gehört (Locak oder extern).
     *
     * @return batchresIsLocatFl
     */
    @Column(name = "BATCHRES_IS_LOCAL_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isBatchresIsLocalFl() {
        return batchresIsLocalFl;
    }

    /**
     *
     * @param batchresIsLocatFl Column BATCHRES_IS_LOCAL_FL:Flag der definiert,
     * zu welcher Version der Fälle Ergebnis des Batchgroupens gehört.
     */
    public void setBatchresIsLocalFl(boolean batchresIsLocatFl) {
        this.batchresIsLocalFl = batchresIsLocatFl;
    }

    /**
     * Gibt Groupermodell zurück ,z.b.AUTOMATIC, GDRG20112013,..
     *
     * @return modelIdEn
     */
    @Column(name = "MODEL_ID_EN", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    public GDRGModel getModelIdEn() {
        return modelIdEn;
    }

    /**
     *
     * @param modelIdEn Column MODEL_ID_EN: Enumeration für Groupermodell
     * (AUTOMATIC, GDRG20112013,..
     */
    public void setModelIdEn(GDRGModel modelIdEn) {
        this.modelIdEn = modelIdEn;
    }

    /**
     * Gibt Anzahl der Fälle zurück, die zu dem Batchgroupverfahren hingezogen
     * wurden .
     *
     * @return batchresCaseCount
     */
    @Column(name = "BATCHRES_CASE_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresCaseCount() {
        return batchresCaseCount;
    }

    /**
     *
     * @param batchresCaseCount Column BATCHRES_CASE_COUNT:Anzahl der Fälle, die
     * zu dem Batchgroupverfahren hingezogen wurden
     */
    public void setBatchresCaseCount(int batchresCaseCount) {
        this.batchresCaseCount = batchresCaseCount;
    }

    /**
     * Gibt Anzahl der Fälle zurück, die gegroupt wurden.
     *
     * @return batchresGroupedCount
     */
    @Column(name = "BATCHRES_GROUPED_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresGroupedCount() {
        return batchresGroupedCount;
    }

    /**
     *
     * @param batchresGroupedCount Column BATCHRES_GROUPED_COUNT:Anzahl der
     * Fälle, die gegroupt wurden.(Ambulante und nicht geschlösse Fälle werden
     * nicht mitgezählt) .
     */
    public void setBatchresGroupedCount(int batchresGroupedCount) {
        this.batchresGroupedCount = batchresGroupedCount;
    }

    /**
     * Gibt Anzahl der Fälle mit Zuschlägen(bei denen OGVD überschritten hat)
     * zurück.
     *
     * @return batchresHtpCount
     */
    @Column(name = "BATCHRES_HTP_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresHtpCount() {
        return batchresHtpCount;
    }

    /**
     *
     * @param batchresHtpCount Column BATCHRES_HTP_COUNT : Anzahl der Fälle mit
     * Zuschlägen(bei denen OGVD überschritten hat)
     */
    public void setBatchresHtpCount(int batchresHtpCount) {
        this.batchresHtpCount = batchresHtpCount;
    }

    /**
     * Gibt Anzahl der Fälle mit Abschlägen(bei denen UGVD unterschritten hat)
     * zurück.
     *
     * @return batchresLtpCount
     */
    @Column(name = "BATCH_LTP_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresLtpCount() {
        return batchresLtpCount;
    }

    /**
     *
     * @param batchresLtpCount Column BATCH_LTP_COUNT:Anzahl der Fälle mit
     * Abschlägen(bei denen UGVD unterschritten hat).
     */
    public void setBatchresLtpCount(int batchresLtpCount) {
        this.batchresLtpCount = batchresLtpCount;
    }

    /**
     * Gibt :Anzahl der Fälle zurück, die Verlegungsabschlag haben.
     *
     * @return batchresTransfCount
     */
    @Column(name = "BATCH_TRASF_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresTransfCount() {
        return batchresTransfCount;
    }

    /**
     *
     * @param batchresTransfCount Column BATCH_TRASF_COUNT:Anzahl der Fälle, die
     * Verlegungsabschlag haben.
     */
    public void setBatchresTransfCount(int batchresTransfCount) {
        this.batchresTransfCount = batchresTransfCount;
    }

    /**
     * Gibt Anzahl der Fälle mit Fehler DRG/PEPP 960Z, TA/PA99Z zurück.
     *
     * @return batchresErrDrgCount
     */
    @Column(name = "BATCH_ERR_DRG_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresErrDrgCount() {
        return batchresErrDrgCount;
    }

    /**
     *
     * @param batchresErrDrgCount Column BATCH_ERR_DRG_COUNT : Anzahl der Fälle
     * mit Fehler DRG/PEPP 960Z, TA/PA99Z .
     */
    public void setBatchresErrDrgCount(int batchresErrDrgCount) {
        this.batchresErrDrgCount = batchresErrDrgCount;
    }

    /**
     * Gibt :Anzahl der Nebendiagnosen in allen gegroupten Fällen zurück. Auf
     * der Oberfläche wird NDI - Nebendiagnosenindex
     * angezeigt:BATCH_AUXD_COUNT/BATCH_CASE_COUNT.
     *
     * @return batchresAuxdCount
     */
    @Column(name = "BATCH_AUXD_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresAuxdCount() {
        return batchresAuxdCount;
    }

    /**
     *
     * @param batchresAuxdCount Column BATCH_AUXD_COUNT:Anzahl der
     * Nebendiagnosen in allen gegroupten Fällen.Auf der Oberfläche wird NDI -
     * Nebendiagnosenindex angezeigt:BATCH_AUXD_COUNT/BATCH_CASE_COUNT.
     */
    public void setBatchresAuxdCount(int batchresAuxdCount) {
        this.batchresAuxdCount = batchresAuxdCount;
    }

    /**
     * GibtAnzahl der Fälle mit Entlassungsgrund Tod (07) zurück.
     *
     * @return batchresDeadCount
     */
    @Column(name = "BATCH_DEAD_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresDeadCount() {
        return batchresDeadCount;
    }

    /**
     *
     * @param batchresDeadCount Column BATCH_DEAD_COUNT::Anzahl der Fälle mit
     * Entlassungsgrund Tod (07) .
     */
    public void setBatchresDeadCount(int batchresDeadCount) {
        this.batchresDeadCount = batchresDeadCount;
    }

    /**
     * Gibt Anzahl der Fälle vor-/teilstationär zurück.
     *
     * @return batchresDayCareCount
     */
    @Column(name = "BATCH_DAY_CARE_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresDayCareCount() {
        return batchresDayCareCount;
    }

    /**
     *
     * @param batchresDayCareCount ColumnBATCH_DAY_CARE_COUNT :Anzahl der Fälle
     * vor-/teilstationär .
     */
    public void setBatchresDayCareCount(int batchresDayCareCount) {
        this.batchresDayCareCount = batchresDayCareCount;
    }

    /**
     * Gibt : Summe der PCCLs zurück. In der Oberfläche wird durchschnittliche
     * Wert BATCH_PCCL_SUM/BATCHRES_GROUPED_COUNT angezeigt .
     *
     * @return batchresPcclSum
     */
    @Column(name = "BATCH_PCCL_SUM", precision = 15, scale = 0, nullable = false)
    public int getBatchresPcclSum() {
        return batchresPcclSum;
    }

    /**
     *
     * @param batchresPcclSum Column BATCH_PCCL_SUM :Summe der PCCLs. In der
     * Oberfläche wird durchschnittliche Wert
     * BATCH_PCCL_SUM/BATCHRES_GROUPED_COUNT angezeigt .
     */
    public void setBatchresPcclSum(int batchresPcclSum) {
        this.batchresPcclSum = batchresPcclSum;
    }

    /**
     * Gibt Summe der VWD aller betrachteten Fällen zurück.
     *
     * @return batchresCareDaysSum
     */
    @Column(name = "BATCH_CARE_DAYS_SUM", precision = 15, scale = 0, nullable = false)
    public int getBatchresCareDaysSum() {
        return batchresCareDaysSum;
    }

    /**
     *
     * @param batchresCareDaysSum Column BATCH_CARE_DAYS_SUM : Summe der VWD
     * aller betrachteten Fällen.
     */
    public void setBatchresCareDaysSum(int batchresCareDaysSum) {
        this.batchresCareDaysSum = batchresCareDaysSum;
    }

    /**
     * Gibt Anzahl der Fälle zurück, die aufenthalt auf der Intensivstation
     * beinhalten .
     *
     * @return batchresCaseIntensivCount
     */
    @Column(name = "BATCH_CASE_INTENSIV_COUNT", precision = 15, scale = 0, nullable = false)
    public int getBatchresCaseIntensivCount() {
        return batchresCaseIntensivCount;
    }

    /**
     *
     * @param batchresCaseIntensivCount Column BATCH_CASE_INTENSIV_COUNT :Anzahl
     * der Fälle die aufenthalt auf der Intensivstation beinhalten.
     */
    public void setBatchresCaseIntensivCount(int batchresCaseIntensivCount) {
        this.batchresCaseIntensivCount = batchresCaseIntensivCount;
    }

    /**
     * Gibt Summe der Tage in der Intensivstation zurück.
     *
     * @return batchresLosIntensivSum
     */
    @Column(name = "BATCH_LOS_INTENSIV_SUM", precision = 15, scale = 0, nullable = false)
    public int getBatchresLosIntensivSum() {
        return batchresLosIntensivSum;
    }

    /**
     *
     * @param batchresLosIntensivSum ColumnBATCH_LOS_INTENSIV_SUM : Summe der
     * Tage in der Intensivstation .
     */
    public void setBatchresLosIntensivSum(int batchresLosIntensivSum) {
        this.batchresLosIntensivSum = batchresLosIntensivSum;
    }

    /**
     * Gibt Summe der CW - Effektiv aller geroupten Fällenzurück. Auf der
     * Oberfläche wird CaseMix effektiv als
     * BATCH_CW_EFFECTIV_SUM/BATCHRES_CASE_COUNT angezeigt .
     *
     * @return batchresCwEffectivSum
     */
    @Column(name = "BATCH_CW_EFFECTIV_SUM", precision = 15, scale = 4, nullable = false)
    public double getBatchresCwEffectivSum() {
        return batchresCwEffectivSum;
    }

    /**
     *
     * @param batchresCwEffectivSum Column BATCH_CW_EFFECTIV_SUM : Summe der CW
     * - Effektiv aller geroupten Fällen.Auf der Oberfläche wird CaseMix
     * effektiv als BATCH_CW_EFFECTIV_SUM/BATCHRES_CASE_COUNT angezeigt.
     */
    public void setBatchresCwEffectivSum(double batchresCwEffectivSum) {
        this.batchresCwEffectivSum = batchresCwEffectivSum;
    }

    /**
     * Gibt :Summe der CW - Katalog aller geroupten Fällen zurück. Auf der
     * Oberfläche wird CaseMix unkorr als
     * BATCH_CW_CATALOG_SUM/BATCHRES_CASE_COUNT angezeigt.
     *
     * @return batchresCwCatalogSum:Summe der CW - Katalog aller geroupten
     * Fällen.Auf der Oberfläche wird CaseMix unkorr als
     * BATCH_CW_CATALOG_SUM/BATCHRES_CASE_COUNT angezeigt.
     */
    @Column(name = "BATCH_CW_CATALOG_SUM", precision = 15, scale = 4, nullable = false)
    public double getBatchresCwCatalogSum() {
        return batchresCwCatalogSum;
    }

    /**
     *
     * @param batchresCwCatalogSum Column BATCH_CW_CATALOG_SUM:Summe der CW -
     * Katalog aller geroupten Fällen.
     */
    public void setBatchresCwCatalogSum(double batchresCwCatalogSum) {
        this.batchresCwCatalogSum = batchresCwCatalogSum;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "batchResult")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TBatchResult2Role> getBatchres2role() {
        return batchres2role;
    }
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "batchResult")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public TBatchGroupParameter getBatchGroupParameter() {
        return batchGroupParameter;
    }

public void setBatchGrouperParameter(TBatchGroupParameter pParam){
    batchGroupParameter = pParam;
}

    public TBatchResult2Role getB2RForRoleId(long pRoleId) {
        for (TBatchResult2Role role : getBatchres2role()) {
            if (role.getRoleId() == pRoleId) {
                return role;
            }
        }
        return null;
    }

    public void setBatchres2role(Set<TBatchResult2Role> batchres2role) {
        this.batchres2role = batchres2role;
    }

    /**
     * Gibt Anzahl der Belegungstage in einem anderen Krankenhaus
     * (Belegungstageauserhalb) zurück.
     *
     * @return batchresNalosSum
     */
    @Column(name = "BATCH_NALOS_SUM", precision = 15, scale = 0, nullable = false /*,columnDefinition = "int default 0" */)
    @ColumnDefault("0")
    public int getBatchresNalosSum() {
        return batchresNalosSum;
    }

    /**
     *
     * @param batchresNalosSum Column BATCH_NALOS_SUM:Anzahl der Belegungstage
     * in einem anderen Krankenhaus (Belegungstageauserhalb) .
     */
    public void setBatchresNalosSum(int batchresNalosSum) {
        this.batchresNalosSum = batchresNalosSum;
    }

    /**
     * Gibt Anzahl der #.9 Diagnosen zurück.
     *
     * @return batchresAux9Count
     */
    @Column(name = "BATCH_AUX9_COUNT", precision = 15, scale = 0, nullable = false /*,columnDefinition = "int default 0" */)
    @ColumnDefault("0")
    public int getBatchresAux9Count() {
        return batchresAux9Count;
    }


    @Column(name = "RULES_COUNT", precision = 10, scale = 0, nullable = false)
    @ColumnDefault("0")
    public int getRulesCount() {
        return rulesCount;
    }

    public void setRulesCount(int rulesCount) {
        this.rulesCount = rulesCount;
    }

    /**
     *
     * @param batchresAux9Count Column BATCH_AUX9_COUNT:Anzahl der #.9 Diagnosen
     * .
     */
    public void setBatchresAux9Count(int batchresAux9Count) {
        this.batchresAux9Count = batchresAux9Count;
    }

    public void addBatchresCaseCount() {
        batchresCaseCount++;
    }

    public void addBatchresGroupedCount() {
        batchresGroupedCount++;
    }

    public void addBatchresHtpCount() {
        batchresHtpCount++;
    }

    public void addBatchresLtpCount() {
        batchresLtpCount++;
    }

    public void addBatchresTransfCount() {
        batchresTransfCount++;
    }

    public void addBatchresErrDrgCount() {
        batchresErrDrgCount++;
    }

    public void addBatchresAuxdCount(int cnt) {
        batchresAuxdCount += cnt;
    }

    public void addBatchresDeadCount() {
        batchresDeadCount++;
    }

    public void addBatchresDayCareCount() {
        batchresDayCareCount++;
    }

    public void addBatchresPcclSum(int cnt) {
        batchresPcclSum += cnt;
    }

    public void addBatchresCaseIntensivCount() {
        batchresCaseIntensivCount++;
    }

    public void addBatchresCareDaysSum(int cnt) {
        batchresCareDaysSum += cnt;
    }

    public void addBatchresLosIntensivSum(int cnt) {
        batchresLosIntensivSum += cnt;
    }

    public void addBatchresCwEffectivSum(double cw) {
        batchresCwEffectivSum += cw;
    }

    public void addBatchresCwCatalogSum(double cw) {
        batchresCwCatalogSum += cw;
    }

    /**
     * before this object will be persisted, avarage values are to be set Dont
     * need. It can be done on the Client
     */
    public void consolidate() {
        /*        batchresAuxdCount = batchresAuxdCount / batchresCaseCount;
        if (batchresGroupedCount == 0) {
            batchresPcclSum = 0;
        } else {
            batchresPcclSum = batchresPcclSum / batchresGroupedCount;
        }
        batchresCareDaysSum = batchresCareDaysSum / batchresCaseCount;
        batchresCwEffectivSum = batchresCwEffectivSum / batchresCaseCount;
        batchresCwCatalogSum = batchresCwCatalogSum / batchresCaseCount;*/
    }

    @Transient
    public HashMap<Long, TBatchResult2Role> getResult2Role() {
        if (role2result2role == null) {
            role2result2role = new HashMap<>();
        }
        return role2result2role;
    }

    public void consolidateResult2Role(HashMap<Long, TBatchResult2Role> role2result) {
        getResult2Role();
        Set<Long> roles = role2result.keySet();
        for (Long role : roles) {
            TBatchResult2Role tr2r = role2result.get(role);
            TBatchResult2Role r2r = role2result2role.get(role);
            if (r2r == null) {
                role2result2role.put(role, tr2r);
                batchres2role.add(tr2r);
                tr2r.setBatchResult(this);
            } else {
                r2r.consolidateCheckResult(tr2r.getType2checkResult());
            }
        }
    }

    /**
     *
     * @return NDI value as double: Auf der Oberfläche wird NDI -
     * Nebendiagnosenindex angezeigt:BATCH_AUXD_COUNT/BATCH_CASE_COUNT.
     */
    @Transient
    public double getNDI() {

        if (batchresCaseCount > 0) {
            return batchresAuxdCount / (double) batchresCaseCount;
        }

        return 0.0;
    }

    public void addBatchresAux9Count(int aux9count) {
        batchresAux9Count += aux9count;
    }

    public void addBatchresNalosSum(int nalos) {
        batchresNalosSum += nalos;
    }

    /**
     * get the PCCL-Value for the BatchResult, PCCL-Value is computed by
     * batchresPcclSum/batchresCaseCount
     *
     * @return PCCL-Value, if batchresCaseCount &lt; 0 0.0 is returned
     */
    @Transient
    public double getPccl() {
        if (batchresCaseCount > 0) {
            return batchresPcclSum / (double) batchresCaseCount;
        }
        return 0.0;
    }

    /**
     * get Lenght of Stay Value, Los-Value is computed by
     * batchresCareDaysSum/batchresCaseCount
     *
     * @return Los Value, batchresCaseCount &lt; 0 ,0.0 is returned
     */
    @Transient
    public double getLos() {
        if (batchresCaseCount > 0) {
            return batchresCareDaysSum / (double) batchresCaseCount;
        }
        return 0.0;
    }

    /**
     * get CW Effectiv Value, CwEff-Value is computed by
     * batchresCwEffectivSum/batchresCaseCount
     *
     * @return CwEff-Value, if batchresCaseCount &lt; 0 ,0.0 is returned
     */
    @Transient
    public double getCWEff() {
        if (batchresCaseCount > 0) {
            return batchresCwEffectivSum / batchresCaseCount;
        }
        return 0.0;
    }

    /**
     * get CW Unk. Value, CwEff-Value is computed by
     * batchresCwCatalogSum/batchresCaseCount
     *
     * @return CwEff-Value, if batchresCaseCount &lt; 0 ,0.0 is returned
     */
    @Transient
    public double getCWUnk() {
        if (batchresCaseCount > 0) {
            return batchresCwCatalogSum / batchresCaseCount;
        }
        return 0.0;
    }
}

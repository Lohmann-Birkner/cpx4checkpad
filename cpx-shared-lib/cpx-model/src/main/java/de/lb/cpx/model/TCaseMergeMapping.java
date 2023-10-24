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
package de.lb.cpx.model;

import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.lang.Lang;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * TCaseMergeMapping initially generated at 21.01.2016 17:07:59 by Hibernate
 * Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE_MERGE_MAPPING"
 * ist Mapping Fallzusammenführung zu Entgelte. </p>
 * NOTE: CP-Class (DSAWiederaufnahmeListModel)/DRG
 * (DSAPeppWiederaufnahmeListModel)/PEPP Integer values of merged conditions: 0
 * = unknown,1=yes,2=no mrgCondition1 =
 * DRG(INNERHALB_OGVD)/PEPP(READM_INNERHALB_21_TAGE) mrgCondition2 =
 * DRG(INNERHALB_30_T)/PEPP(READM_INNERHALB_120_TAGE) mrgCondition3 =
 * DRG(EQUAL_ADRG)/PEPP(READM_GLEICHES_JAHR) mrgCondition4 =
 * DRG(EQUAL_MDC)/PEPP(READM_SAME_SK) mrgCondition5 =
 * DRG(PARTITION_A_M)/PEPP(READM_BEIDE_BEWERTETE_PEPP) mrgCondition6 =
 * DRG(PARTITION_O)/PEPP(READM_BEIDE_TEIL_VOLL_STATION) mrgCondition7 =
 * DRG(CATALOG_EXCEPTION)/PEPP[not used](READM_ERSTEFALL_VORLETZTEJAHR)
 * mrgCondition8 = DRG(REENTRY)/PEPP[not
 * used](READM_ERSTEFALL_LETZTESJAHR_VORUMSTIEG) mrgCondition9 =
 * DRG(SUMMARY)/PEPP(SUMMARY) mrgCondition10 = DRG(NEW_CASE)/PEPP(NEW_CASE)
 */
@Entity
@Table(name = "T_CASE_MERGE_MAPPING",
        indexes = {
            @Index(name = "IDX_C_MERGE_MAP4MERG_MEMB_C_ID", columnList = "MERGE_MEMBER_CASE_ID", unique = false),
            @Index(name = "IDX_C_MERGE_MAP4TCASE_ID", columnList = "T_CASE_ID", unique = false),
            @Index(name = "IDX_C_MERGE_MAP4GRPRES_ID", columnList = "T_GROUPING_RESULTS_ID", unique = false)})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "GRPRES_TYPE_EN", discriminatorType = DiscriminatorType.STRING)
@SuppressWarnings("serial")
public class TCaseMergeMapping extends AbstractEntity implements java.io.Serializable {

//  private long id;
    private CaseTypeEn grpresType;
    private TCase caseByHosc;
    private TCase caseByMergeMemberCase;

    private TGroupingResults grpres;
    private Integer mrgMergeIdent;
    private Integer mrgCondition1;
    private Integer mrgCondition2;
    private Integer mrgCondition3;
    private Integer mrgCondition4;
    private Integer mrgCondition5;
    private Integer mrgCondition6;
    private Integer mrgCondition7;
    private Integer mrgCondition8;
    private Integer mrgCondition9;
    private Integer mrgCondition10;
    
    public TCaseMergeMapping(){        
    }

    public TCaseMergeMapping(TCase pCaseByMergeMemberCase, TGroupingResults pGrpres, CaseTypeEn pGrpresType){
        caseByMergeMemberCase = pCaseByMergeMemberCase;
        grpres = pGrpres;
        grpresType = pGrpresType;
        mrgMergeIdent = 0;
    }
    /**
     * Gibt Verweis auf die Tablle T_CASE zurück , der als ein Resultat der
     * Zusammenführung der Fälle angelegt wurde.
     *
     * @return caseByHoscId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_MERGE_MAP4T_CASE_ID"))
    public TCase getCaseByHoscId() {
        return this.caseByHosc;
    }

    /**
     * Gibt Verweis auf die Tablle T_CASE zurück, der als Teilfall des
     * zusammengeführten Falls in der Fallzusammenführung benutzt wurde .
     *
     * @return caseByMergeMemberCaseId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERGE_MEMBER_CASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_MERGE_MAP4T_CASE_ID_2"))
    public TCase getCaseByMergeMemberCaseId() {
        return this.caseByMergeMemberCase;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_MERGE_MAPPING_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     *
     * @param caseByHoscId Column T_CASE_ID: Verweis auf die Tablle T_CASE, der
     * als ein Resultat der Zusammenführung der Fälle angelegt wurde.
     */
    public void setCaseByHoscId(final TCase caseByHoscId) {
        this.caseByHosc = caseByHoscId;
    }

    /**
     *
     * @param caseByMergeMemberCaseId Column MERGE_MEMBER_CASE_ID: Verweis auf
     * die Tablle T_CASE, der als Teilfall des zusammengeführten Falls in der
     * Fallzusammenführung benutzt wurde. Nach der erfolgreichen Zusammenführung
     * wird der referenzierte Fall als "storniert wegen der Zusammenführung"
     * markiert
     */
    public void setCaseByMergeMemberCaseId(final TCase caseByMergeMemberCaseId) {
        this.caseByMergeMemberCase = caseByMergeMemberCaseId;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf die Tablle T_Grouping_Results zurück , deren Eitrag für
     * die Anwendung derwiederaufnahmeregeln benutz wurde
     *
     * @return grpres
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "T_GROUPING_RESULTS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_MERGE_MAP4T_GROUP_RESULT_ID"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    public TGroupingResults getGrpresId() {
        return grpres;
    }

    /**
     * Liefert den Ident. Nr. der Gruppe der Fälle die zusammengeführt werden
     * können nach der Zusammenführung wird auf -1 gesetzt
     *
     * @return mrgMergeIdent - Wert des Idents
     */
    @SequenceGenerator(name = "default_gen", sequenceName = "CASE_MERGE_IDENT_SEQ")
    @Column(name = "MRG_MERGE_IDENT", precision = 15, scale = 0)
    public Integer getMrgMergeIdent() {
        return mrgMergeIdent;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 1: DRG - Innerhalb von
     * OGVD PEPP - Innerhalb von 21 Tage
     *
     * @return mrgCondition1 - Wert für die Bedignung 1
     */
    @Column(name = "MRG_CONDITION_1", precision = 5, scale = 0)
    public Integer getMrgCondition1() {
        return mrgCondition1;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 2: DRG - Innerhalb von
     * 30 Tagen PEPP - Innerhalb von 120 Tage
     *
     * @return mrgCondition2 - Wert für die Bedignung 2
     */
    @Column(name = "MRG_CONDITION_2", precision = 5, scale = 0)
    public Integer getMrgCondition2() {
        return mrgCondition2;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 3: DRG - gleiche
     * ADRG(Basis DRG) PEPP - gleiches Jahr
     *
     * @return mrgCondition3 - Wert für die Bedignung 3
     */
    @Column(name = "MRG_CONDITION_3", precision = 5, scale = 0)
    public Integer getMrgCondition3() {
        return mrgCondition3;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 4: DRG - Gleiche MDC
     * PEPP - Gleiche SK
     *
     * @return mrgCondition4 - Wert für die Bedignung 4
     */
    @Column(name = "MRG_CONDITION_4", precision = 5, scale = 0)
    public Integer getMrgCondition4() {
        return mrgCondition4;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 5: DRG - erstee DRG aus
     * partition A oder M PEPP - beide in bewertete PEPP
     *
     * @return mrgCondition5 - Wert für die Bedignung 5
     */
    @Column(name = "MRG_CONDITION_5", precision = 5, scale = 0)
    public Integer getMrgCondition5() {
        return mrgCondition5;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 6: DRG - zweite DRG aus
     * partition O PEPP - beide Fälle vor- oder teilstationär
     *
     * @return mrgCondition6 - Wert für die Bedignung 6
     */
    @Column(name = "MRG_CONDITION_6", precision = 5, scale = 0)
    public Integer getMrgCondition6() {
        return mrgCondition6;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 7: DRG - Kennzeichen im
     * DRG Katalog PEPP - Aufnahmedatum des ersten Falles im vorletzten Jahr
     *
     * @return mrgCondition7 - Wert für die Bedignung 7
     */
    @Column(name = "MRG_CONDITION_7", precision = 5, scale = 0)
    public Integer getMrgCondition7() {
        return mrgCondition7;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 8: DRG - Aufnahme wg.
     * Komplikation PEPP - Aufnahmedatum des ersten Falles im letzten und vor
     * Umstiegszeitpunkt
     *
     * @return mrgCondition8 - Wert für die Bedignung 8
     */
    @Column(name = "MRG_CONDITION_8", precision = 5, scale = 0)
    public Integer getMrgCondition8() {
        return mrgCondition8;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 9: DRG - zusammenfassen
     * und neue DRG PEPP - zusammenfassen und neue PEPP
     *
     * @return mrgCondition9 - Wert für die Bedignung 9
     */
    @Column(name = "MRG_CONDITION_9", precision = 5, scale = 0)
    public Integer getMrgCondition9() {
        return mrgCondition9;
    }

    /**
     * Liefert den Ergebnis der Anwendung der Bedignung 10: DRG - neuer Fall
     * PEPP - neuer Fall
     *
     * @return mrgCondition10 - Wert für die Bedignung 10
     */
    @Column(name = "MRG_CONDITION_10", precision = 5, scale = 0)
    public Integer getMrgCondition10() {
        return mrgCondition10;
    }

    public void setGrpresType(CaseTypeEn type) {
        this.grpresType = type;
    }

    /**
     *
     * @return grpresType: Enumeration für Art des Falles 0-5(OTHER, DRG, PEPP,
     * PSY, PIA ,AmbuOP ,vorstatAbbrecher ) .
     */
    @Column(name = "GRPRES_TYPE_EN", length = 25, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public CaseTypeEn getGrpresType() {
        return grpresType;
    }

    public void setGrpresId(TGroupingResults grpresId) {
        this.grpres = grpresId;
    }

    public void setMrgMergeIdent(int mrgMergeIdent) {
        this.mrgMergeIdent = mrgMergeIdent;
    }

    public void setMrgCondition1(int mrgCondition1) {
        this.mrgCondition1 = mrgCondition1;
    }

    public void setMrgCondition2(int mrgCondition2) {
        this.mrgCondition2 = mrgCondition2;
    }

    public void setMrgCondition3(int mrgCondition3) {
        this.mrgCondition3 = mrgCondition3;
    }

    public void setMrgCondition4(int mrgCondition4) {
        this.mrgCondition4 = mrgCondition4;
    }

    public void setMrgCondition5(int mrgCondition5) {
        this.mrgCondition5 = mrgCondition5;
    }

    public void setMrgCondition6(int mrgCondition6) {
        this.mrgCondition6 = mrgCondition6;
    }

    public void setMrgCondition7(int mrgCondition7) {
        this.mrgCondition7 = mrgCondition7;
    }

    public void setMrgCondition8(int mrgCondition8) {
        this.mrgCondition8 = mrgCondition8;
    }

    public void setMrgCondition9(int mrgCondition9) {
        this.mrgCondition9 = mrgCondition9;
    }

    public void setMrgCondition10(int mrgCondition10) {
        this.mrgCondition10 = mrgCondition10;
    }

    public String getConditionValueText(int pConditionValue) {
        switch (pConditionValue) {
            case 0:
                return "";
            case 1:
                return Lang.getConfirmationYes();
            case 2:
                return Lang.getConfirmationNo();
            default:
                return "-1";
        }
    }

    public String getConditionText(int pCondition) {
        switch (pCondition) {
            case 1:
                return getConditionValueText(mrgCondition1);
            case 2:
                return getConditionValueText(mrgCondition2);
            case 3:
                return getConditionValueText(mrgCondition3);
            case 4:
                return getConditionValueText(mrgCondition4);
            case 5:
                return getConditionValueText(mrgCondition5);
            case 6:
                return getConditionValueText(mrgCondition6);
            case 7:
                return getConditionValueText(mrgCondition7);
            case 8:
                return getConditionValueText(mrgCondition8);
            case 9:
                return getConditionValueText(mrgCondition9);
            case 10:
                return getConditionValueText(mrgCondition10);
            default:
                return "-1";
        }
    }
}

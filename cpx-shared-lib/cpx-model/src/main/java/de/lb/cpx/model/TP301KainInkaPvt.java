/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2017  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author Shahin
 */
@Entity
@Table(name = "T_P301_KAIN_INKA_PVT ",
        indexes = {
            @Index(name = "IDX_PVT4KAIN_INKA_PVV_ID", columnList = "T_P301_KAIN_INKA_PVV_ID", unique = false)})
public class TP301KainInkaPvt extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String text;//TEXT
    private String mainDiadIcd;//MAIN_DIAG_ICD
    private LocalisationEn mainDiagLoc;//MAIN_DIAG_LOC_EN
    private String mainDiagSecondryIcd;//MAIN_DIAG_SECONDARY_ICD
    private LocalisationEn mainDiagSecondryLoc;//MAIN_DIAG_SECONDARY_LOC_EN
    private String secondaryDiagIcd;//SECONDARY_DIAG_ICD
    private LocalisationEn secondaryDiagLoc;//SECONDARY_DIAG_LOC_EN
    private String secondarySecondDiagIcd;//SECONDARY_SECOND_DIAG_ICD
    private LocalisationEn secondarySecondDiagLoc;//SECONDARY_SECOND_DIAG_LOC_EN
    private String opsCode;//OPS_CODE
    private LocalisationEn opsLoc;//OPS_LOCALISATION_EN
    private TP301KainInkaPvv p301KainInkaPvvId;//P301_KAIN_INKA_PVV_ID

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_P301_KAIN_INKA_PVT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return text
     */
    @Column(name = "TEXT", nullable = false, length = 256)
    public String getText() {
        return text;
    }

    /**
     *
     * @param text :PrüfvV-Text Freitext maximal 256 Zeichen erlaubt!!
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return mainDiadIcd
     */
    @Column(name = "MAIN_DIAG_ICD", nullable = true, length = 10)
    public String getMainDiadIcd() {
        return mainDiadIcd;
    }

    /**
     *
     * @param mainDiadIcd :ICD-Code der Hauptdiagnose
     */
    public void setMainDiadIcd(String mainDiadIcd) {
        this.mainDiadIcd = mainDiadIcd;
    }

    /**
     *
     * @return mainDiagLoc
     */
    @Column(name = "MAIN_DIAG_LOC_EN", nullable = true, length = 1)
    @Enumerated(EnumType.STRING)
    public LocalisationEn getMainDiagLoc() {
        return mainDiagLoc;
    }

    /**
     *
     * @param mainDiagLoc :Lokalisierung der obigen Hauptdiagnose
     */
    public void setMainDiagLoc(LocalisationEn mainDiagLoc) {
        this.mainDiagLoc = mainDiagLoc;
    }

    /**
     *
     * @return mainDiagSecondryIcd
     */
    @Column(name = "MAIN_DIAG_SECONDARY_ICD", nullable = true, length = 10)
    public String getMainDiagSecondryIcd() {
        return mainDiagSecondryIcd;
    }

    /**
     *
     * @param mainDiagSecondryIcd :ICD-Code der sekundären Hauptdiagnose
     */
    public void setMainDiagSecondryIcd(String mainDiagSecondryIcd) {
        this.mainDiagSecondryIcd = mainDiagSecondryIcd;
    }

    /**
     *
     * @return mainDiagSecondryLoc
     */
    @Column(name = "MAIN_DIAG_SECONDARY_LOC_EN", nullable = true, length = 1)
    @Enumerated(EnumType.STRING)
    public LocalisationEn getMainDiagSecondryLoc() {
        return mainDiagSecondryLoc;
    }

    /**
     *
     * @param mainDiagSecondryLoc :Lokalisierung der sekundären Hauptdiagnose
     */
    public void setMainDiagSecondryLoc(LocalisationEn mainDiagSecondryLoc) {
        this.mainDiagSecondryLoc = mainDiagSecondryLoc;
    }

    /**
     *
     * @return secondaryDiagIcd
     */
    @Column(name = "SECONDARY_DIAG_ICD", nullable = true, length = 10)
    public String getSecondaryDiagIcd() {
        return secondaryDiagIcd;
    }

    /**
     *
     * @param secondaryDiagIcd :Nebendiagnose ICD-Code
     */
    public void setSecondaryDiagIcd(String secondaryDiagIcd) {
        this.secondaryDiagIcd = secondaryDiagIcd;
    }

    /**
     *
     * @return secondaryDiagLoc
     */
    @Column(name = "SECONDARY_DIAG_LOC_EN", nullable = true, length = 1)
    @Enumerated(EnumType.STRING)
    public LocalisationEn getSecondaryDiagLoc() {
        return secondaryDiagLoc;
    }

    /**
     *
     * @param secondaryDiagLoc :Nebendiagnose Lokalisierung
     */
    public void setSecondaryDiagLoc(LocalisationEn secondaryDiagLoc) {
        this.secondaryDiagLoc = secondaryDiagLoc;
    }

    /**
     *
     * @return secondarySecondDiagIcd
     */
    @Column(name = "SECONDARY_SECOND_DIAG_ICD", nullable = true, length = 10)
    public String getSecondarySecondDiagIcd() {
        return secondarySecondDiagIcd;
    }

    /**
     *
     * @param secondarySecondDiagIcd :Nebendiagnose-Sekundärdiagnose ICD-Code
     */
    public void setSecondarySecondDiagIcd(String secondarySecondDiagIcd) {
        this.secondarySecondDiagIcd = secondarySecondDiagIcd;
    }

    /**
     *
     * @return secondarySecondDiagLoc
     */
    @Column(name = "SECONDARY_SECOND_DIAG_LOC_EN", nullable = true, length = 1)
    @Enumerated(EnumType.STRING)
    public LocalisationEn getSecondarySecondDiagLoc() {
        return secondarySecondDiagLoc;
    }

    /**
     *
     * @param secondarySecondDiagLoc :Nebendiagnose-Sekundärdiagnose
     * Lokalisierung
     */
    public void setSecondarySecondDiagLoc(LocalisationEn secondarySecondDiagLoc) {
        this.secondarySecondDiagLoc = secondarySecondDiagLoc;
    }

    /**
     *
     * @return opsCode
     */
    @Column(name = "OPS_CODE", nullable = true, length = 10)
    public String getOpsCode() {
        return opsCode;
    }

    /**
     *
     * @param opsCode :OPS Code Prozedur
     */
    public void setOpsCode(String opsCode) {
        this.opsCode = opsCode;
    }

    /**
     *
     * @return opsLoc
     */
    @Column(name = "OPS_LOCALISATION_EN", nullable = true, length = 1)
    @Enumerated(EnumType.STRING)
    public LocalisationEn getOPLocalisation() {
        return opsLoc;
    }

    /**
     *
     * @param opsLoc :OPS Lokalisierung
     */
    public void setOPLocalisation(LocalisationEn opsLoc) {
        this.opsLoc = opsLoc;
    }

    /**
     *
     * @return p301KainInkaPvvId
     */
    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne(fetch = FetchType.EAGER)
//    @MapsId("p301KainInkaPvvId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "T_P301_KAIN_INKA_PVV_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PVT4KAIN_INKA_PVV_ID"))
    public TP301KainInkaPvv getP301KainInkaPvvId() {
        return p301KainInkaPvvId;
    }

    /**
     *
     * @param p301KainInkaPvvId :FK auf Tabelle T_P301_KAIN_INKA_PVV  welche das
     * PVV-Serment erhält welches zur KAIN oder INKA-Nachricht gehört
     */
    public void setP301KainInkaPvvId(TP301KainInkaPvv p301KainInkaPvvId) {
        this.p301KainInkaPvvId = p301KainInkaPvvId;
    }

}

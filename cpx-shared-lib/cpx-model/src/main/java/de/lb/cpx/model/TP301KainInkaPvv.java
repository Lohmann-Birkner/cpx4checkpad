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

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author Shahin
 */
@Entity
@Table(name = "T_P301_KAIN_INKA_PVV ",
        indexes = {
            @Index(name = "IDX_PVV4KAIN_INKA", columnList = "T_P301_KAIN_INKA_ID", unique = false)})
public class TP301KainInkaPvv extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String informationKey30;//INFORMATION_KEY_30
    private String billNr;//BILL_NR
    private Date billDate;//BILL_DATE
    private TP301KainInka p301KainInkaId;//P301_KAIN_INKA_ID
    private List<TP301KainInkaPvt> kainInkaPvts;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_P301_KAIN_INKA_PVV_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return informtiontex30 ,tatsächlich laut Spec 5 Zeichen
     */
    @Column(name = "INFORMATION_KEY_30", nullable = false, length = 20)
    public String getInformationKey30() {
        return informationKey30;
    }

    /**
     *
     * @param informationKey30 :Schlüssel 30 Information PrüfvV (primäre
     * Fehlbelegung, Kodierprüfung , etc.)
     */
    public void setInformationKey30(String informationKey30) {
        this.informationKey30 = informationKey30;
    }

    /**
     *
     * @return billNr ,tatsächlich laut Spec 20 Zeichen
     */
    @Column(name = "BILL_NR", nullable = false, length = 50)
    public String getBillNr() {
        return billNr;
    }

    /**
     *
     * @param billNr :Rechnungsnummer des Falles (REC)
     */
    public void setBillNr(String billNr) {
        this.billNr = billNr;
    }

    /**
     *
     * @return billDate
     */
    @Column(name = "BILL_DATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getBillDate() {
        return billDate == null ? null : new Date(billDate.getTime());
    }

    /**
     *
     * @param billDate :Rechnungsdatum (REC)
     */
    public void setBillDate(Date billDate) {
        this.billDate = billDate == null ? null : new Date(billDate.getTime());
    }

    /**
     *
     * @return p301KainInkaId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "T_P301_KAIN_INKA_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PVV4T_P301_KAIN_INKA_ID"))
    public TP301KainInka getP301KainInkaId() {
        return p301KainInkaId;
    }

    /**
     *
     * @param p301KainInkaId :FK auf Tabelle T_P301_KAIN_INKA KAIN oder INKA
     * Nachricht zu der dieses PVV-Segment gehört
     */
    public void setP301KainInkaId(TP301KainInka p301KainInkaId) {
        this.p301KainInkaId = p301KainInkaId;
    }

    /* 05.04.18 by Pna  
    The orphanRemoval directive declares that associated entity instances are to be removed when they are disassociated from the parent,
    or equivalently when the parent is removed.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "p301KainInkaPvvId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<TP301KainInkaPvt> getKainInkaPvts() {
        return this.kainInkaPvts;
    }

//    public void setKainInkaPvts(final List<TP301KainInkaPvt> pKainInkaPvts) {
//        if (this.kainInkaPvts == null) {
//            this.kainInkaPvts = pKainInkaPvts;
//        } else {
//            this.kainInkaPvts.clear();
//            if (pKainInkaPvts != null) {
//                this.kainInkaPvts.addAll(pKainInkaPvts);
//            }
//        }
//    }
    public void setKainInkaPvts(final List<TP301KainInkaPvt> kainInkaPvts) {
        this.kainInkaPvts = kainInkaPvts;
    }

}

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

//import de.lb.cpx.model.converter.CsCaseTypeConverter;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_DRUG" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_DRUG")
@SuppressWarnings("serial")
//@NamedEntityGraph(
//        name = "fetchBatchCaseGraph",
//        attributeNodes = {
//            @NamedAttributeNode(value = "caseDetails", subgraph = "detailsGraph"),
////            @NamedAttributeNode(value = "caseOpsGroupeds", subgraph = "opsGraph"),
////            @NamedAttributeNode(value = "checkResults"),
////            @NamedAttributeNode(value = "caseIcd")
//        },
//        subgraphs = {
//            @NamedSubgraph(name = "detailsGraph",attributeNodes = {
//                        @NamedAttributeNode(value = "caseDepartments"
////            , subgraph = "opsIcdGraph")
////                    }),
////            @NamedSubgraph(name = "opsIcdGraph",attributeNodes = {
////                        @NamedAttributeNode("caseOpses"),
////                        @NamedAttributeNode("caseIcds"
//                    
//                        )})
//        }
//    )
public class TDrug extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//    private long id;
    private TPatient patient;
    private TCase hospitalCase;
    private String dgPicNo; //Belegnummer des Rezeptes 
    private BigDecimal dgFactor; //Faktor
    private BigDecimal dgGross; //Brutto
    private String dgPzn;
    private String dgAtc;
    private Integer dgGenerics; //Generika
    private String dgDf;
    private String dgPackSize; //Packungsgröße
    private Integer dgStandardSize; //Normgröße
    private Integer dgDoctorIdent; //Arztnr.
    private BigDecimal dgGrossTotal; //Brutto Total
    private String dgPharmacyIdent;
    private Integer dgBmg; //what's this?!
    private Integer dgUnfk; //what's this?!
    private Date dgPrescriptionDate; //Verordnungsdatum
    private Date dgSubmissionDate; //Abgabedatum

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_DRUG_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_Patient zurück.
     *
     * @return patient
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_PATIENT_ID", foreignKey = @ForeignKey(name = "FK_T_DRUG4T_PATIENT_ID"), nullable = false)
    public TPatient getPatient() {
        return this.patient;
    }

    /**
     *
     * @param patient :Verweis auf die ID der Tabelle T_Patient.
     */
    public void setPatient(final TPatient patient) {
        this.patient = patient;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_Case zurück.
     *
     * @return patient
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_CASE_ID", foreignKey = @ForeignKey(name = "FK_T_DRUG4T_CASE_ID"), nullable = true)
    public TCase getHospitalCase() {
        return this.hospitalCase;
    }

    /**
     *
     * @param hospitalCase :Verweis auf die ID der Tabelle T_Case.
     */
    public void setHospitalCase(final TCase hospitalCase) {
        this.hospitalCase = hospitalCase;
    }

    /**
     * Belegnummer des Rezeptes (VERORD_LOID)
     *
     * @return dgPicNo
     */
    @Column(name = "DG_PIC_NO", nullable = true)
    public String getDgPicNo() {
        return dgPicNo;
    }

    public void setDgPicNo(String dgPicNo) {
        this.dgPicNo = dgPicNo;
    }

    /**
     * Faktor der Verordnung (ARZNEI_FAKTOR_ANZ)
     *
     * @return dgFactor
     */
    @Column(name = "DG_FACTOR", precision = 8, scale = 2, nullable = false)
    public BigDecimal getDgFactor() {
        return dgFactor;
    }

    public void setDgFactor(BigDecimal dgFactor) {
        this.dgFactor = dgFactor;
    }

    /**
     * Einzelpreis (ARZNEI_EINZEL_BTR)
     *
     * @return dgGross
     */
    @Column(name = "DG_GROSS", precision = 12, scale = 2, nullable = false)
    public BigDecimal getDgGross() {
        return dgGross;
    }

    public void setDgGross(BigDecimal dgGross) {
        this.dgGross = dgGross;
    }

    /**
     * Pharmazentralnummer (ARZNEI_PZ_NR) oder Hilfsmittelnummer bei Heil- und
     * Hilfsmittel stimmt
     *
     * @return dgPzn
     */
    @Column(name = "DG_PZN", length = 12, nullable = true)
    public String getDgPzn() {
        return dgPzn;
    }

    public void setDgPzn(String dgPzn) {
        this.dgPzn = dgPzn;
    }

    /**
     * Wird nicht geliefert
     *
     * @return dgAtc
     */
    @Column(name = "DG_ATC", length = 7, nullable = true)
    public String getDgAtc() {
        return dgAtc;
    }

    public void setDgAtc(String dgAtc) {
        this.dgAtc = dgAtc;
    }

    /**
     * Wird nicht geliefert
     *
     * @return dgGenerics
     */
    @Column(name = "DG_GENERICS", scale = 1, nullable = true)
    public Integer getDgGenerics() {
        return dgGenerics;
    }

    public void setDgGenerics(Integer dgGenerics) {
        this.dgGenerics = dgGenerics;
    }

    /**
     * Wird nicht geliefert
     *
     * @return dgDf
     */
    @Column(name = "DG_DF", length = 4, nullable = true)
    public String getDgDf() {
        return dgDf;
    }

    public void setDgDf(String dgDf) {
        this.dgDf = dgDf;
    }

    /**
     * Wird nicht geliefert
     *
     * @return dgPackSize
     */
    @Column(name = "DG_PACK_SIZE", length = 7, nullable = true)
    public String getDgPackSize() {
        return dgPackSize;
    }

    public void setDgPackSize(String dgPackSize) {
        this.dgPackSize = dgPackSize;
    }

    /**
     * Wird nicht geliefert
     *
     * @return dgStandardSize
     */
    @Column(name = "DG_STANDARD_SIZE", scale = 1, nullable = true)
    public Integer getDgStandardSize() {
        return dgStandardSize;
    }

    public void setDgStandardSize(Integer dgStandardSize) {
        this.dgStandardSize = dgStandardSize;
    }

    /**
     * Wird nicht geliefert
     *
     * @return dgDoctorIdent
     */
    @Column(name = "DG_DOCTOR_IDENT", length = 7, nullable = true)
    public Integer getDgDoctorIdent() {
        return dgDoctorIdent;
    }

    public void setDgDoctorIdent(Integer dgDoctorIdent) {
        this.dgDoctorIdent = dgDoctorIdent;
    }

    /**
     * Brutto des Rezeptes (VERORD_GESAMT_BTR)
     *
     * @return dgGrossTotal
     */
    @Column(name = "DG_GROSS_TOTAL", precision = 12, scale = 2, nullable = false)
    public BigDecimal getDgGrossTotal() {
        return dgGrossTotal;
    }

    public void setDgGrossTotal(BigDecimal dgGrossTotal) {
        this.dgGrossTotal = dgGrossTotal;
    }

    /**
     * IK der Apotheke (INST_LEBRNG_REG_SL……)
     *
     * @return dgPharmacyIdent
     */
    @Column(name = "DG_PHARMACY_IDENT", length = 9, nullable = true)
    public String getDgPharmacyIdent() {
        return dgPharmacyIdent;
    }

    public void setDgPharmacyIdent(String dgPharmacyIdent) {
        this.dgPharmacyIdent = dgPharmacyIdent;
    }

    /**
     * Wird nicht geliefert
     *
     * @return dgBmg
     */
    @Column(name = "DG_BMG", scale = 1, nullable = true)
    public Integer getDgBmg() {
        return dgBmg;
    }

    public void setDgBmg(Integer dgBmg) {
        this.dgBmg = dgBmg;
    }

    /**
     * Wird nicht geliefert
     *
     * @return dgUnfk
     */
    @Column(name = "DG_UNFK", scale = 1, nullable = true)
    public Integer getDgUnfk() {
        return dgUnfk;
    }

    public void setDgUnfk(Integer dgUnfk) {
        this.dgUnfk = dgUnfk;
    }

    /**
     * Verordnungsdatum (VERORD_VDATUM_AM)
     *
     * @return dgPrescriptionDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DG_PRESCRIPTION_DATE", length = 8, nullable = false)
    public Date getDgPrescriptionDate() {
        return dgPrescriptionDate;
    }

    public void setDgPrescriptionDate(Date dgPrescriptionDate) {
        this.dgPrescriptionDate = dgPrescriptionDate;
    }

    /**
     * Abgabedatum (VERORD_ADATUM_AM)
     *
     * @return dgSubmissionDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DG_SUBMISSION_DATE", length = 8, nullable = false)
    public Date getDgSubmissionDate() {
        return dgSubmissionDate;
    }

    public void setDgSubmissionDate(Date dgSubmissionDate) {
        this.dgSubmissionDate = dgSubmissionDate;
    }

}

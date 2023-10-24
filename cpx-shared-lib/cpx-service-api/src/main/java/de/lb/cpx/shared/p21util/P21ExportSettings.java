/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.p21util;

import java.io.File;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author niemeier
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class P21ExportSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String targetFolder;
    private final boolean zip;
    private final String version;
    private final String ident;
    private final String mail;
    private final String mail2;
    private final String hosName;
    private final String hosType;
    private final String hosCostUnit;
    private final Integer bedsDrg;
    private final Integer bedsDrgIntensiv;
    private final Integer bedsPepp;
    private final Integer bedsPeppIntensiv;
    private final Double surcharges;
    private final Boolean regPensionObligation;
    private final boolean anonymizeHospital;
    private final boolean anonymizePatient;
    private final boolean anonymizeCase;

    public P21ExportSettings() {
        this("", false, P21Version.getRecent(), "", "", "", "", null, null, null, null, null, null, null, false, false, false, false);
    }

//    public P21ExportSettings() {
//        this("", false, P21Version.getCurrent(), "", "", "", null, null, null, null, null, false);
//    }
    public P21ExportSettings(final String pTargetFolder, final boolean pZip,
            final P21Version pVersion, final String pIdent,
            final String pMail, final String pMail2, final String pHosName,
            final P21TypeOfHospitalEn pHosType, final P21CostUnitOfHospitalEn pHosCostUnit,
            final Integer pBedsDrg, final Integer pBedsDrgIntensiv, 
            final Integer pBedsPepp, final Integer pBedsPeppIntensiv, final Double pSurcharges,
            final Boolean pRegPensionObligation,
            final boolean pAnonymizeHospital,
            final boolean pAnonymizePatient,
            final boolean pAnonymizeCase
    ) {
        this(pTargetFolder,
                pZip,
                pVersion == null ? null : pVersion.name(),
                pIdent,
                pMail,
                pMail2,
                pHosName,
                pHosType == null ? null : pHosType.name(),
                pHosCostUnit == null ? null : pHosCostUnit.name(),
                pBedsDrg,
                pBedsDrgIntensiv,
                pBedsPepp,
                pBedsPeppIntensiv,
                pSurcharges,
                pRegPensionObligation,
                pAnonymizeHospital,
                pAnonymizePatient,
                pAnonymizeCase
        );
    }

    public P21ExportSettings(final String pTargetFolder, final boolean pZip,
            final String pVersion, final String pIdent,
            final String pMail, final String pMail2, final String pHosName,
            final String pHosType, final String pHosCostUnit,
            final Integer pBedsDrg, final Integer pBedsDrgIntensiv, 
            final Integer pBedsPepp, final Integer pBedsPeppIntensiv, final Double pSurcharges,
            final Boolean pRegPensionObligation,
            final boolean pAnonymizeHospital,
            final boolean pAnonymizePatient,
            final boolean pAnonymizeCase) {
        this.targetFolder = StringUtils.trimToNull(pTargetFolder);
        this.zip = pZip;
        this.version = pVersion;
        this.ident = StringUtils.trimToNull(pIdent);
        this.mail = StringUtils.trimToNull(pMail);
        this.mail2 = StringUtils.trimToNull(pMail2);
        this.hosName = StringUtils.trimToNull(pHosName);
        this.hosType = pHosType;
        this.hosCostUnit = pHosCostUnit;
        this.bedsDrg = pBedsDrg;
        this.bedsDrgIntensiv = pBedsDrgIntensiv;
        this.bedsPepp = pBedsPepp;
        this.bedsPeppIntensiv = pBedsPeppIntensiv;
        this.surcharges = pSurcharges;
        this.regPensionObligation = pRegPensionObligation;
        this.anonymizeHospital = pAnonymizeHospital;
        this.anonymizePatient = pAnonymizePatient;
        this.anonymizeCase = pAnonymizeCase;
    }

    /**
     * @return the targetFolder
     */
//    @XmlElement(name = "targetFolder")
    public String getTargetFolder() {
        return targetFolder;
    }

    public File getTargetFolderFile() {
        return new File(targetFolder);
    }

    /**
     * @return the zip
     */
//    @XmlElement(name = "zip")
    public boolean isZip() {
        return zip;
    }

    /**
     * @return the version
     */
//    @XmlElement(name = "version")
    public String getVersion() {
        return version;
    }

    public P21Version getVersionEn() {
        return (version == null || version.trim().isEmpty()) ? P21Version.getRecent() : P21Version.find(version);
    }

    /**
     * @return the version
     */
//    @XmlElement(name = "ident")
    public String getIdent() {
        return ident;
    }

    /**
     * @return the mail
     */
//    @XmlElement(name = "mail")
    public String getMail() {
        return mail;
    }

    /**
     * @return the mail
     */
//    @XmlElement(name = "mail")
    public String getMail2() {
        return mail2;
    }

    /**
     * @return the hosName
     */
//    @XmlElement(name = "hosName")
    public String getHosName() {
        return hosName;
    }

    /**
     * @return the hosType
     */
//    @XmlElement(name = "hosType")
    public String getHosType() {
        return hosType;
    }

    public P21TypeOfHospitalEn getHosTypeEn() {
        return P21TypeOfHospitalEn.find(hosType);
    }

    /**
     * @return the hosCostUnit
     */
//    @XmlElement(name = "hosCostUnit")
    public String getHosCostUnit() {
        return hosCostUnit;
    }

    public P21CostUnitOfHospitalEn getHosCostUnitEn() {
        return P21CostUnitOfHospitalEn.find(hosCostUnit);
    }

    /**
     * @return the bedsDrg
     */
//    @XmlElement(name = "bedsDrg")
    public Integer getBedsDrg() {
        return bedsDrg;
    }

    /**
     * @return the bedsDrgIntensiv
     */
//    @XmlElement(name = "bedsDrgIntensiv")
    public Integer getBedsDrgIntensiv() {
        return bedsDrgIntensiv;
    }

    /**
     * @return the bedsPepp
     */
//    @XmlElement(name = "bedsPepp")
    public Integer getBedsPepp() {
        return bedsPepp;
    }

    /**
     * @return the bedsPeppIntensiv
     */
//    @XmlElement(name = "bedsPeppIntensiv")
    public Integer getBedsPeppIntensiv() {
        return bedsPeppIntensiv;
    }

    /**
     * @return the surcharges
     */
//    @XmlElement(name = "surcharges")
    public Double getSurcharges() {
        return surcharges;
    }

    /**
     * @return the surcharges
     */
//    @XmlElement(name = "regPensionObligation")
    public Boolean isRegionalPensionObligation() {
        return regPensionObligation;
    }

    /**
     * @return the anonymizeHospital
     */
    public boolean isAnonymizeHospital() {
        return anonymizeHospital;
    }

    /**
     * @return the anonymizePatient
     */
    public boolean isAnonymizePatient() {
        return anonymizePatient;
    }

    /**
     * @return the anonymizeCase
     */
    public boolean isAnonymizeCase() {
        return anonymizeCase;
    }

}

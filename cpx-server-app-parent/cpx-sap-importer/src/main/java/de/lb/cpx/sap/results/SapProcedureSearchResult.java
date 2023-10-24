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
package de.lb.cpx.sap.results;

import com.sap.conn.jco.JCoTable;
import java.util.Date;

/**
 *
 * @author niemeier
 */
public class SapProcedureSearchResult {

    private String client = "";
    private String surprocSeqno = "";
    private String movemntSeqno = "";
    private String sgPrCatalog = "";
    private String sgPrCode = "";
    private String procShorttext = "";
    private String maincode = "";
    private String addNumber = "";
    private Date begindate = null;
    private Date begintime = null;
    private Date endtime = null;
    private String localis = "";
    private String externalId = "";
    private String surgeryType = "";
    private Date updateDate = null;
    private String updateUser = "";
    private String cancelInd = "";
    private String cancelUser = "";
    private Date cancelDate = null;
    private String tcode = "";
    private String institution = "";
    private String patCaseId = "";
    private String procRemark = "";
    private String genEvent = "";
    private String genObject = "";
    private String drgProcSeqno = "";
    private String drgProcCategory = "";
    private String drgRelevant = "";
    private String longText = "";
    private String deptou = "";
    private String perfou = "";
    private Date enddate = null;
    private String procedureType = "";

    /**
     *
     */
    public SapProcedureSearchResult() {
    }

    /**
     * @return the client
     */
    public String getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * @return the surprocSeqno
     */
    public String getSurprocSeqno() {
        return surprocSeqno;
    }

    /**
     * @param surprocSeqno the surprocSeqno to set
     */
    public void setSurprocSeqno(String surprocSeqno) {
        this.surprocSeqno = surprocSeqno;
    }

    /**
     * @return the movemntSeqno
     */
    public String getMovemntSeqno() {
        return movemntSeqno;
    }

    /**
     * @param movemntSeqno the movemntSeqno to set
     */
    public void setMovemntSeqno(String movemntSeqno) {
        this.movemntSeqno = movemntSeqno;
    }

    /**
     * @return the sgPrCatalog
     */
    public String getSgPrCatalog() {
        return sgPrCatalog;
    }

    /**
     * @param sgPrCatalog the sgPrCatalog to set
     */
    public void setSgPrCatalog(String sgPrCatalog) {
        this.sgPrCatalog = sgPrCatalog;
    }

    /**
     * @return the sgPrCode
     */
    public String getSgPrCode() {
        return sgPrCode;
    }

    /**
     * @param sgPrCode the sgPrCode to set
     */
    public void setSgPrCode(String sgPrCode) {
        this.sgPrCode = sgPrCode;
    }

    /**
     * @return the procShorttext
     */
    public String getProcShorttext() {
        return procShorttext;
    }

    /**
     * @param procShorttext the procShorttext to set
     */
    public void setProcShorttext(String procShorttext) {
        this.procShorttext = procShorttext;
    }

    /**
     * @return the maincode
     */
    public String getMaincode() {
        return maincode;
    }

    /**
     * @param maincode the maincode to set
     */
    public void setMaincode(String maincode) {
        this.maincode = maincode;
    }

    /**
     * @return the addNumber
     */
    public String getAddNumber() {
        return addNumber;
    }

    /**
     * @param addNumber the addNumber to set
     */
    public void setAddNumber(String addNumber) {
        this.addNumber = addNumber;
    }

    /**
     * @return the begindate
     */
    public Date getBegindate() {
        return begindate == null ? null : new Date(begindate.getTime());
    }

    /**
     * @param begindate the begindate to set
     */
    public void setBegindate(Date begindate) {
        this.begindate = begindate == null ? null : new Date(begindate.getTime());
    }

    /**
     * @return the begintime
     */
    public Date getBegintime() {
        return begintime == null ? null : new Date(begintime.getTime());
    }

    /**
     * @param begintime the begintime to set
     */
    public void setBegintime(Date begintime) {
        this.begintime = begintime == null ? null : new Date(begintime.getTime());
    }

    /**
     * @return the endtime
     */
    public Date getEndtime() {
        return endtime == null ? null : new Date(endtime.getTime());
    }

    /**
     * @param endtime the endtime to set
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime == null ? null : new Date(endtime.getTime());
    }

    /**
     * @return the localis
     */
    public String getLocalis() {
        return localis;
    }

    /**
     * @param localis the localis to set
     */
    public void setLocalis(String localis) {
        this.localis = localis;
    }

    /**
     * @return the externalId
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * @param externalId the externalId to set
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    /**
     * @return the surgeryType
     */
    public String getSurgeryType() {
        return surgeryType;
    }

    /**
     * @param surgeryType the surgeryType to set
     */
    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate == null ? null : new Date(updateDate.getTime());
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate == null ? null : new Date(updateDate.getTime());
    }

    /**
     * @return the updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the cancelInd
     */
    public String getCancelInd() {
        return cancelInd;
    }

    /**
     * @param cancelInd the cancelInd to set
     */
    public void setCancelInd(String cancelInd) {
        this.cancelInd = cancelInd;
    }

    /**
     * @return the cancelUser
     */
    public String getCancelUser() {
        return cancelUser;
    }

    /**
     * @param cancelUser the cancelUser to set
     */
    public void setCancelUser(String cancelUser) {
        this.cancelUser = cancelUser;
    }

    /**
     * @return the cancelDate
     */
    public Date getCancelDate() {
        return cancelDate == null ? null : new Date(cancelDate.getTime());
    }

    /**
     * @param cancelDate the cancelDate to set
     */
    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate == null ? null : new Date(cancelDate.getTime());
    }

    /**
     * @return the tcode
     */
    public String getTcode() {
        return tcode;
    }

    /**
     * @param tcode the tcode to set
     */
    public void setTcode(String tcode) {
        this.tcode = tcode;
    }

    /**
     * @return the institution
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * @param institution the institution to set
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * @return the patCaseId
     */
    public String getPatCaseId() {
        return patCaseId;
    }

    /**
     * @param patCaseId the patCaseId to set
     */
    public void setPatCaseId(String patCaseId) {
        this.patCaseId = patCaseId;
    }

    /**
     * @return the procRemark
     */
    public String getProcRemark() {
        return procRemark;
    }

    /**
     * @param procRemark the procRemark to set
     */
    public void setProcRemark(String procRemark) {
        this.procRemark = procRemark;
    }

    /**
     * @return the genEvent
     */
    public String getGenEvent() {
        return genEvent;
    }

    /**
     * @param genEvent the genEvent to set
     */
    public void setGenEvent(String genEvent) {
        this.genEvent = genEvent;
    }

    /**
     * @return the genObject
     */
    public String getGenObject() {
        return genObject;
    }

    /**
     * @param genObject the genObject to set
     */
    public void setGenObject(String genObject) {
        this.genObject = genObject;
    }

    /**
     * @return the drgProcSeqno
     */
    public String getDrgProcSeqno() {
        return drgProcSeqno;
    }

    /**
     * @param drgProcSeqno the drgProcSeqno to set
     */
    public void setDrgProcSeqno(String drgProcSeqno) {
        this.drgProcSeqno = drgProcSeqno;
    }

    /**
     * @return the drgProcCategory
     */
    public String getDrgProcCategory() {
        return drgProcCategory;
    }

    /**
     * @param drgProcCategory the drgProcCategory to set
     */
    public void setDrgProcCategory(String drgProcCategory) {
        this.drgProcCategory = drgProcCategory;
    }

    /**
     * @return the drgRelevant
     */
    public String getDrgRelevant() {
        return drgRelevant;
    }

    /**
     * @param drgRelevant the drgRelevant to set
     */
    public void setDrgRelevant(String drgRelevant) {
        this.drgRelevant = drgRelevant;
    }

    /**
     * @return the longText
     */
    public String getLongText() {
        return longText;
    }

    /**
     * @param longText the longText to set
     */
    public void setLongText(String longText) {
        this.longText = longText;
    }

    /**
     * @return the deptou
     */
    public String getDeptou() {
        return deptou;
    }

    /**
     * @param deptou the deptou to set
     */
    public void setDeptou(String deptou) {
        this.deptou = deptou;
    }

    /**
     * @return the perfou
     */
    public String getPerfou() {
        return perfou;
    }

    /**
     * @param perfou the perfou to set
     */
    public void setPerfou(String perfou) {
        this.perfou = perfou;
    }

    /**
     * @return the enddate
     */
    public Date getEnddate() {
        return enddate == null ? null : new Date(enddate.getTime());
    }

    /**
     * @param enddate the enddate to set
     */
    public void setEnddate(Date enddate) {
        this.enddate = enddate == null ? null : new Date(enddate.getTime());
    }

    /**
     * @return the procedureType
     */
    public String getProcedureType() {
        return procedureType;
    }

    /**
     * @param procedureType the procedureType to set
     */
    public void setProcedureType(String procedureType) {
        this.procedureType = procedureType;
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTable(final JCoTable aTable) {
        setClient(aTable.getString(0));
        setSurprocSeqno(aTable.getString(1));
        setMovemntSeqno(aTable.getString(2));
        setSgPrCatalog(aTable.getString(3));
        setSgPrCode(aTable.getString(4));
        setProcShorttext(aTable.getString(5));
        setMaincode(aTable.getString(6));
        setAddNumber(aTable.getString(7));
        setBegindate(aTable.getDate(8));
        setBegintime(aTable.getTime(9));
        setEndtime(aTable.getTime(10));
        setLocalis(aTable.getString(11));
        setExternalId(aTable.getString(12));
        setSurgeryType(aTable.getString(13));
        setUpdateDate(aTable.getDate(14));
        setUpdateUser(aTable.getString(15));
        setCancelInd(aTable.getString(16));
        setCancelUser(aTable.getString(17));
        setCancelDate(aTable.getDate(18));
        setTcode(aTable.getString(19));
        setInstitution(aTable.getString(20));
        setPatCaseId(aTable.getString(21));
        setProcRemark(aTable.getString(22));
        setGenEvent(aTable.getString(23));
        setGenObject(aTable.getString(24));
        setDrgProcSeqno(aTable.getString(25));
        setDrgProcCategory(aTable.getString(26));
        setDrgRelevant(aTable.getString(27));
        setLongText(aTable.getString(28));
        setDeptou(aTable.getString(29));
        setPerfou(aTable.getString(30));
        setEnddate(aTable.getDate(31));
        setProcedureType(aTable.getString(32));
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromStructurTable(final JCoTable aTable) {
        setClient(aTable.getString(0));                       //MANDT
        setSurprocSeqno(aTable.getString(1));                //LNRIC
        setMovemntSeqno(aTable.getString(2));                //LFDBEW
        setSgPrCatalog(aTable.getString(3));                //ICPMK
        setSgPrCode(aTable.getString(4));                   //ICPML
        setProcShorttext(aTable.getString(21));              //BTEXT
        setMaincode(aTable.getString(5));                     //ICPHC
        setAddNumber(aTable.getString(6));                   //ANZOP
        setBegindate(aTable.getDate(7));                      //BGDOP
        setBegintime(aTable.getTime(8));                      //BZTOP
        setEndtime(aTable.getTime(9));                        //EZTOP
        setLocalis(aTable.getString(10));                     //LSLOK
        setExternalId("");                                   //nv, nb
        setSurgeryType("");                                  //nb
        setUpdateDate(aTable.getDate(13));                   //UPDAT
        setUpdateUser(aTable.getString(14));                 //UPUSR
        setCancelInd(aTable.getString(15));                  //STORN
        setCancelUser(aTable.getString(16));                 //STUSR
        setCancelDate(aTable.getDate(17));                   //STDAT
        setTcode(aTable.getString(18));                       //TCODE
        setInstitution(aTable.getString(19));                 //EINRI
        setPatCaseId(aTable.getString(20));                   //FALNR
        setProcRemark(aTable.getString(21));                //BTEXT
        setGenEvent(aTable.getString(32));                   //EVTUP
        setGenObject(aTable.getString(33));                  //OBJUP
        setDrgProcSeqno(aTable.getString(24));              //DRG_PROC_SEQNO
        setDrgProcCategory(aTable.getString(25));           //DRG_PROC_CATEGORY
        setDrgRelevant(aTable.getString(26));                //DRG_RELEVANT
        setLongText("");                                     //nv
        setDeptou(aTable.getString(28));                      //ORGFA
        setPerfou(aTable.getString(29));                      //ORGPF
        setEnddate(aTable.getDate(30));                       //ENDOP
        setProcedureType(aTable.getString(31));              //PRTYP
    }

    @Override
    public String toString() {
        return "Mandant >" + getClient() + "<\n"
                + "Laufende Nummer des Operationscodes >" + getSurprocSeqno() + "<\n"
                + "Laufende Nummer einer Bewegung >" + getMovemntSeqno() + "<\n"
                + "Identifikation eines Operationsleistungskataloges >" + getSgPrCatalog() + "<\n"
                + "Identifikation eines Operationscodes (Leistung) >" + getSgPrCode() + "<\n"
                + "Leistungskurztext 120-stlg. KTXT1 + KTXT2 + KTXT3 >" + getProcShorttext() + "<\n"
                + "Kennzeichen, ob Operation Hauptcode ist >" + getMaincode() + "<\n"
                + "Anzahl weiterer Operationen >" + getAddNumber() + "<\n"
                + "Datum an dem der Operationscode erbracht wurde >" + getBegindate() + "<\n"
                + "Beginnuhrzeit zu der ein Operationscode erbracht wurde >" + getBegintime() + "<\n"
                + "Endeuhrzeit zu der ein Operationscode beendet wurde >" + getEndtime() + "<\n"
                + "Lokalisation einer Leistung >" + getLocalis() + "<\n"
                + "Identifikation aus externem System >" + getExternalId() + "<\n"
                + "Identifikation aus externem System >" + getSurgeryType() + "<\n"
                + "Änderungsdatum >" + getUpdateDate() + "<\n"
                + "Name des ändernden Sachbearbeiters >" + getUpdateUser() + "<\n"
                + "Stornokennzeichen >" + getCancelInd() + "<\n"
                + "Name des stornierenden Sachbearbeiters >" + getCancelUser() + "<\n"
                + "Stornierungsdatum >" + getCancelDate() + "<\n"
                + "Reportsteuerung: Transaktionscode für Anwendung >" + getTcode() + "<\n"
                + "Einrichtung >" + getInstitution() + "<\n"
                + "Fallnummer >" + getPatCaseId() + "<\n"
                + "Freier Bemerkungstext zur Prozedur >" + getProcRemark() + "<\n"
                + "Ereignis, durch welches die Prozedur erzeugt wurde >" + getGenEvent() + "<\n"
                + "Objekt, durch das die Prozedur erzeugt wurde >" + getGenObject() + "<\n"
                + "Erfassreihenfolge einer DRG-relevanten Prozedur >" + getDrgProcSeqno() + "<\n"
                + "Kategorie einer DRG-Prozedur (Haupt-, Nebenprozedur) >" + getDrgProcCategory() + "<\n"
                + "Kennzeichen 'Für DRG - Ermittlung verwendet' >" + getDrgRelevant() + "<\n"
                + "Kennzeichen, ob Langtext vorhanden ist >" + getLongText() + "<\n"
                + "Fachliche Organisationseinheit der Prozedur >" + getDeptou() + "<\n"
                + "Erbringende Organisationseinheit der Prozedur >" + getPerfou() + "<\n"
                + "Datum an dem der Operationscode beendet wurde >" + getEnddate() + "<\n"
                + "Prozedurentyp >" + getProcedureType() + "<";
    }
}

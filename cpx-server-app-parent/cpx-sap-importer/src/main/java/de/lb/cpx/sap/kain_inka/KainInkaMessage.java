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
package de.lb.cpx.sap.kain_inka;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author gerschmann
 */
public class KainInkaMessage extends KainInkaElement implements KainInkaMessageIf {

    private Map<String, KainInkaElement> name2element = new HashMap<>();// mapping of the name of object to object

    public KainInkaMessage() {

    }

    /**
     *
     * @param root root
     * @throws Exception exception
     */
    public KainInkaMessage(Element root) throws Exception {
        super(root);

        children = getChildList(root.getChildNodes(), name2element);
    }

    /**
     *
     * @return check
     */
    @Override
    public boolean check() {

        return super.check();
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setProcessIdent(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_PROCESS_IDENT, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setTransactionNumber(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_TRANSACTION_NUMBER, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setSenderIdent(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_SENDER_IDENT, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setReceptorIdent(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_RECEPTOR_IDENT, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setInsuranceNumber(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_INSURANCE_NUMBER, name2element, str);
    }

    /**
     *
     * @param it it
     */
    @Override
    public void setInsuranceType(int it) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_INSURANCE_TYPE, name2element, it);
    }

    /**
     *
     * @param sc sc
     */
    @Override
    public void setSpecialCirle(int sc) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_SPECIAL_CIRCLE, name2element, sc);
    }

    /**
     *
     * @param da da
     */
    @Override
    public void setDmpAttendance(int da) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_DMP_ATTENDANCE, name2element, da);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setValidTo(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_VALID_TO, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setCaseNumberHospital(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_CASE_NUMBER_HOSPITAL, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setCaseNumberINsurance(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_CASE_NUMBER_INSURANCE, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setRefNumberInsurance(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_REF_NUMBER_INSURANCE, name2element, str);
    }

    /**
     *
     * @param dt dt
     */
    @Override
    public void setStartOfInsurance(Date dt) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_START_OF_INSURANCE, name2element, dt);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setContractIdent(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_CONTRACT_IDENT, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setPatientName(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_PATIENT_NAME, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setPatientFirstName(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_PATIENT_FIRST_NAME, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setGender(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_GENDER, name2element, str);
    }

    /**
     *
     * @param dt dt
     */
    @Override
    public void setBirthDate(Date dt) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_BIRTH_DATE, name2element, dt);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setAddress(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_ADDRESS, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setZipCode(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_ZIP_CODE, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setCity(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_CITY, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setTitle(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_TITLE, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setCountryIdent(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_COUNTRY_IDENT, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setNameAffix(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_NAME_AFFIX, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setNamePrefix(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_NAME_PREFIX, name2element, str);
    }

    /**
     *
     * @param str str
     */
    @Override
    public void setAddressAddOn(String str) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_ADDRESS_ADD_ON, name2element, str);
    }

    /**
     *
     * @return process identifier
     */
    @Override
    public String getProcessIdent() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_PROCESS_IDENT, name2element);
    }

    /**
     *
     * @return transaction number
     */
    @Override
    public String getTransactionNumber() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_TRANSACTION_NUMBER, name2element);
    }

    /**
     *
     * @return sender identifier
     */
    @Override
    public String getSenderIdent() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_SENDER_IDENT, name2element);
    }

    /**
     *
     * @return receptor identifier
     */
    @Override
    public String getReceptorIdent() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_RECEPTOR_IDENT, name2element);
    }

    /**
     *
     * @return insurance number
     */
    @Override
    public String getInsuranceNumber() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_INSURANCE_NUMBER, name2element);
    }

    /**
     *
     * @return insurance type
     */
    @Override
    public int getInsuranceType() {
        return (int) getFieldValue(KainInkaStatics.NAME_PROPERTY_INSURANCE_TYPE, name2element);
    }

    /**
     *
     * @return special circle
     */
    @Override
    public int getSpecialCirle() {
        return (int) getFieldValue(KainInkaStatics.NAME_PROPERTY_SPECIAL_CIRCLE, name2element);
    }

    /**
     *
     * @return dmp attendance
     */
    @Override
    public int getDmpAttendance() {
        return (int) getFieldValue(KainInkaStatics.NAME_PROPERTY_DMP_ATTENDANCE, name2element);
    }

    /**
     *
     * @return valid to
     */
    @Override
    public String getValidTo() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_VALID_TO, name2element);
    }

    /**
     *
     * @return case number hospital
     */
    @Override
    public String getCaseNumberHospital() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_CASE_NUMBER_HOSPITAL, name2element);
    }

    /**
     *
     * @return case number insurance
     */
    @Override
    public String getCaseNumberInsurance() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_CASE_NUMBER_INSURANCE, name2element);
    }

    /**
     *
     * @return reference number insurance
     */
    @Override
    public String getRefNumberInsurance() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_REF_NUMBER_INSURANCE, name2element);
    }

    /**
     *
     * @return start of insurance
     */
    @Override
    public Date getStartOfInsurance() {
        return (Date) getFieldValue(KainInkaStatics.NAME_PROPERTY_START_OF_INSURANCE, name2element);
    }

    /**
     *
     * @return contract identifier
     */
    @Override
    public String getContractIdent() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_CONTRACT_IDENT, name2element);
    }

    /**
     *
     * @return patient name
     */
    @Override
    public String getPatientName() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_PATIENT_NAME, name2element);
    }

    /**
     *
     * @return patient first name
     */
    @Override
    public String getPatientFirstName() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_PATIENT_FIRST_NAME, name2element);
    }

    /**
     *
     * @return gender
     */
    @Override
    public String getGender() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_GENDER, name2element);
    }

    /**
     *
     * @return birth date
     */
    @Override
    public Date getBirthDate() {
        return (Date) getFieldValue(KainInkaStatics.NAME_PROPERTY_BIRTH_DATE, name2element);
    }

    /**
     *
     * @return address
     */
    @Override
    public String getAddress() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_ADDRESS, name2element);
    }

    /**
     *
     * @return zip code
     */
    @Override
    public String getZipCode() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_ZIP_CODE, name2element);
    }

    /**
     *
     * @return city
     */
    @Override
    public String getCity() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_CITY, name2element);
    }

    /**
     *
     * @return title
     */
    @Override
    public String getTitle() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_TITLE, name2element);
    }

    /**
     *
     * @return country identifier
     */
    @Override
    public String getCountryIdent() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_COUNTRY_IDENT, name2element);
    }

    /**
     *
     * @return name affix
     */
    @Override
    public String getNameAffix() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_NAME_AFFIX, name2element);
    }

    /**
     *
     * @return name prefix
     */
    @Override
    public String getNamePrefix() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_NAME_PREFIX, name2element);
    }

    /**
     *
     * @return address add on
     */
    @Override
    public String getAddressAddOn() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_ADDRESS_ADD_ON, name2element);
    }

    /**
     * returns a root element for the generated xml
     *
     * @return Object
     */
    @Override
    protected Object getResultValue() {
        return null;
    }

    /**
     *
     * @param doc document
     */
    protected void createXML(Document doc) {
        addXMLElement(doc, null, KainInkaStatics.ROOT_KAIN_INKA);
    }

    /**
     *
     * @param doc document
     * @param elem element
     */
    @Override
    protected void addXMLElement(Document doc, Element elem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param elem element
     */
    @Override
    protected void fillAttributes(Element elem) {
// there are no attributes for root now
    }

    /**
     *
     * @param pvv pvv
     * @param ident identifier
     */
    public void addPVV(PvvResultIf pvv, int ident) {
        Segment elem = (Segment) name2element.get(KainInkaStatics.NAME_SEGMENT_PVV);
        Segment pvv_pattern = (Segment) name2element.get(KainInkaStatics.NAME_SEGMENT_PVV_CHILD);
        Element xml = pvv_pattern.xmlElement;
        if (ident == 0) {
            elem.removeNotPropertes();
        }
        PvvChildSegment pvvSeg = new PvvChildSegment(xml);
        elem.children.add(pvvSeg);
        pvvSeg.setValue(pvv);
    }

    /**
     *
     * @return list of kain/inka elements
     */
    @Override
    public List<KainInkaElement> getPVVs() {
        Segment pvvs = (Segment) name2element.get(KainInkaStatics.NAME_SEGMENT_PVV);
        return pvvs.getChildren();
    }

    /**
     *
     * @return aggregated pvv
     */
    public String getAggregatedPVVInfos() {
        StringBuilder builder = new StringBuilder();
        List<KainInkaElement> pvvs = getPVVs();
        if (pvvs == null) {
            return builder.toString();
        }
        int size = pvvs.size();
        for (int i = 0; i < size; i++) {
            KainInkaElement ki = pvvs.get(i);
            if (ki instanceof PvvChildSegment) {
                String info = ((PvvResultIf) ki).getInformation();
                if (info == null) {
                    info = "";
                }
                builder.append(info);
                if (i < size - 1) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

}

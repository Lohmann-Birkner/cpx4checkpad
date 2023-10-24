/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package line;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Dirk Niemeier
 */
public interface LineI extends Serializable {

    Set<Field> getFields();

    Map<Field, String> getValues();

    Set<String> getFieldNames();

    Set<Integer> getFieldNumbers();

    Field getFieldByName(final String pFieldName);

    Field getFieldByNumber(final Integer pFieldNumber);

    String setValue(final Field pField, final String pValue);

    String setValue(final String pFieldName, final String pValue);

    String setValue(final Integer pFieldNumber, final String pValue);

    String setValueObj(final Field pField, final Object pValue);

    String setValueObj(final String pFieldName, final Object pValue);

    String setValueObj(final Integer pFieldNumber, final Object pValue);

    //String setValue(final Field pField, final Number pValue);
    //String setTimeValue(final Field pField, final Date pValue);
    //String setDateValue(final Field pField, final Date pValue);
    String getValue(final Field pField);

    String getValue(final String pFieldName);

    String getValue(final Integer pFieldNumber);

    String clearValue(final Field pField);

    String clearValue(final String pFieldName);

    String clearValue(final Integer pFieldNumber);

    String serialize();

    //LineI unserializeWithExc(final String pCsvData) throws InstantiationException, IllegalAccessException;
    void load(final String pCsvData);

    void setNr(final long pNr);

    String getNr();

    void setTpSource(final String pTpSource);

    String getTpSource();

    void setTpId(final String pTpId);

    String getTpId();

    String getHeadline();

    /*
  String getHash() throws UnsupportedEncodingException, NoSuchAlgorithmException;
  
  boolean hasValues();
     */
    void setIkz(final String pIkz);

    String getIkz();

    void setFallNr(final String pFallNr);

    void setFallNr(final String pFallNr, final CaseNumberFormatEn pCaseNumberFormatEn);

    String getFallNr();

    void setPatNr(final String pPatNr);

    String getPatNr();

    //boolean isLineWritten();
    //void lineWritten();
    void check() throws IllegalStateException;

    //int getLengthOverflow();
}

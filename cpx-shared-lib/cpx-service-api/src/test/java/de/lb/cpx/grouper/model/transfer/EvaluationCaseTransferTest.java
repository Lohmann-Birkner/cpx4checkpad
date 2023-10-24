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
package de.lb.cpx.grouper.model.transfer;

import de.lb.cpx.grouper.model.enums.GrouperRefFieldsEn;
import de.lb.cpx.model.enums.GroupResultPdxEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.GrouperStatusEn;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author gerschmann
 */
public class EvaluationCaseTransferTest {

    private static final String REF_CASE = "111111111|1|201401220437|201402220008|E^00|01^00|01^00||2500^00|9^00|0^00|m^00|0^00|F90.1^000~F94.8^000~F51.2^000~F80.28^000~I37.0^002|9-403.2&&20131211^102~9-403.5&&20140102^002||22^00|0^00|U41Z|18A|1|08|2";
    private static final String REF_CASE1 = "111111111|9|201403282112|201404082039|E^00|01^00|01^00||2500^00|1^00|0^00|w^00|0^00|F51.0^100|||11^00|0^00|U64Z|19|0|00|0";
    private static final String REF_CASE2 = "111111111|18|201411281554|201412102035|E^00|01^00|01^00|20140226|7400^00|0^00|275^00|m^00|0^00|G47.0^000~F43.2^000~B37.2^000~Z60^000~H91.9^000|9-403.2&&20140921^102~1-208.x&&20141003^000~1-208.8&&20141003^000||12^00|0^00|U41Z|19|0|00|0";
    private static final String REF_CASE3 = "111111111|14267|201405030450|201406021245|E^00|01^00|01^00||2500^00|58^00|0^00|m^00|0^00|C67.9^100~N13.3^002~B95.2^001~C67.9^020~C77.2^002~Z92.6^000~N18.3^003~T83.5^001|8-137.00&L&20140410^002~8-137.2&L&20140407^002~5-407.2&&20140424^104~5-569.30&L&20140424^001~5-563.01&L&20140424^001~5-454.50&&20140424^001~8-137.2&L&20140424^002~8-137.03&L&20140424^002~5-543.21&&20140424^001~5-568.d0&L&20140424^001~9-401.22&&20140505^000||20^00|0^00|L13A|11|3|00|0";
    private static final Logger LOG = Logger.getLogger(EvaluationCaseTransferTest.class.getName());
    private final SimpleDateFormat m_dateFormatTime = new SimpleDateFormat("yyyyMMddHHmm");
    private final SimpleDateFormat m_dateFormat = new SimpleDateFormat("yyyyMMdd");

    public EvaluationCaseTransferTest() {
    }

    /**
     * Test of getUniqueKey method, of class EvaluationCaseTransfer.
     */
    @Test
    public void testGetUniqueKey() {
        EvaluationCaseTransfer test = new EvaluationCaseTransfer(REF_CASE, true);
        String key = test.getUniqueKey();
        assertEquals(key, "111111111_1");
    }

    /**
     * Test of checkField method, of class EvaluationCaseTransfer.
     */
    @Test
    public void testGetField() {
        EvaluationCaseTransfer test = new EvaluationCaseTransfer(REF_CASE, true);
        String testString = "";
        for (GrouperRefFieldsEn en : GrouperRefFieldsEn.values()) {
            testString = test.getField(en);
            switch (en) {
                case KH_POSITION:
                    assertEquals(testString, "111111111");
                    break;
                case CASE_NUMBER_POSITION:
                    assertEquals(testString, "1");

                    break;
                case ADM_DATE_POSITION:
                    assertEquals(testString, "201401220437");

                    break;
                case DIS_DATE_POSITION:
                    assertEquals(testString, "201402220008");

                    break;
                case ADM_REASON_POSITION:
                    assertEquals(testString, "E^00");

                    break;
                case ADM_MODE12_POSITION:
                    assertEquals(testString, "01^00");

                    break;
                case DIS_MODE12_POSITION:
                    assertEquals(testString, "01^00");

                    break;
                case BIRTH_DATE_POSITION:
                    assertEquals(testString, "");

                    break;
                case ADM_WEIGHT_POSITION:
                    assertEquals(testString, "2500^00");

                    break;
                case AGE_IN_YEARS_POSITION:
                    assertEquals(testString, "9^00");

                    break;
                case AGE_IN_DAYS_POSITION:
                    assertEquals(testString, "0^00");

                    break;
                case SEX_POSITION:
                    assertEquals(testString, "m^00");

                    break;
                case BREATHING_HMV_POSITION:
                    assertEquals(testString, "0^00");

                    break;
                case DIAGNOSIS_POSITION:
                    assertEquals(testString, "F90.1^000~F94.8^000~F51.2^000~F80.28^000~I37.0^002");
                    break;
                // break;
                case PROCEDURES_POSITION:
                    assertEquals(testString, "9-403.2&&20131211^102~9-403.5&&20140102^002");
                    break;
                case DEPARTMENTS_POSITION:
                    assertEquals(testString, "");
                    break;
                case LENGTH_OF_STAY_LOS_POSITION:
                    assertEquals(testString, "22^00");
                    break;
                case LEAVE_DAYS_POSITION:
                    assertEquals(testString, "0^00");

                    break;
                case DRG_PEPP_POSITION:
                    assertEquals(testString, "U41Z");

                    break;
                case MDC_POSITION:
                    assertEquals(testString, "18A");

                    break;
                case PCCL_POSITION:
                    assertEquals(testString, "1");

                    break;
                case GST_POSITION:
                    assertEquals(testString, "08");

                    break;
                case GPDX_POSITION:
                    assertEquals(testString, "2");

                    break;
                case ET_POSITION: // for PEPP only
                    continue;

            }
        }
    }

    /**
     * Test of checkDate method, of class EvaluationCaseTransfer.
     */
    @Test
    public void testCheckDate() {
        //201403220437
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = m_dateFormat.parse("201403220437");
            date2 = m_dateFormatTime.parse("201403220437");

        } catch (ParseException ex) {
        }

        boolean b = EvaluationCaseTransfer.checkDate("201403220437", date1);
        assertTrue(!b);
        b = EvaluationCaseTransfer.checkDate("201403220437", date2);
        assertTrue(b);
        b = EvaluationCaseTransfer.checkDate("", null);
        assertTrue(b);
        b = EvaluationCaseTransfer.checkDate("201403220437", null);
        assertTrue(!b);
        b = EvaluationCaseTransfer.checkDate("", date1);
        assertTrue(!b);
    }

    /**
     * Test of getField method, of class EvaluationCaseTransfer.
     */
    @Test
    public void testCheckField() {
        EvaluationCaseTransfer test = new EvaluationCaseTransfer(REF_CASE, true);
        // date
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = m_dateFormat.parse("201401220437");
            date2 = m_dateFormatTime.parse("201401220437");

        } catch (ParseException ex) {
        }
        boolean b = test.checkField(GrouperRefFieldsEn.ADM_DATE_POSITION, date1);
        assertTrue(!b);
        b = test.checkField(GrouperRefFieldsEn.ADM_DATE_POSITION, date2);
        assertTrue(b);
// integer        
//2500^00|9^00
        b = test.checkField(GrouperRefFieldsEn.ADM_WEIGHT_POSITION, 0);
        assertTrue(b);
        b = test.checkField(GrouperRefFieldsEn.AGE_IN_YEARS_POSITION, 9);
        assertTrue(b);
//String E^00
        b = test.checkField(GrouperRefFieldsEn.ADM_REASON_POSITION, "E");
        assertTrue(b);
// enums  
// gst
        b = test.checkField(GrouperRefFieldsEn.GST_POSITION, GrouperStatusEn.GST08);
        assertTrue(b);
        b = test.checkField(GrouperRefFieldsEn.GST_POSITION, GrouperStatusEn.GST00);
        assertTrue(!b);
// GPDX
        b = test.checkField(GrouperRefFieldsEn.GPDX_POSITION, GroupResultPdxEn.pdx2);
        assertTrue(b);
        b = test.checkField(GrouperRefFieldsEn.GPDX_POSITION, GroupResultPdxEn.pdx0);
        assertTrue(!b);
// MDC
        b = test.checkField(GrouperRefFieldsEn.MDC_POSITION, GrouperMdcOrSkEn.mdc18A);
        assertTrue(b);
        b = test.checkField(GrouperRefFieldsEn.MDC_POSITION, GrouperMdcOrSkEn.PKJ);
        assertTrue(!b);
    }

    /**
     * Test of checkMultiField method, of class EvaluationCaseTransfer.
     *
     * @throws java.io.UnsupportedEncodingException error if unsupported
     */
    @Test
    public void testCheckMultiField() throws UnsupportedEncodingException {
        String[] icds = {"F90.1^000",
            "F94.8^000",
            "F51.2^000",
            "F80.28^000",
            "I37.0^002"};
        String[] opss = {
            "9-403.2&&20131211^102",
            "9-403.5&&20140102^002",};
        String[] opss1 = {
            "1-208.X&&201410030000^000",
            "1-208.8&&201410030000^000",
            "9-403.2&&201409210000^100",};
        ArrayList<String> arrIcds = new ArrayList<>(Arrays.asList(icds));
        ArrayList<String> arrOpss = new ArrayList<>(Arrays.asList(opss));
        EvaluationCaseTransfer test = new EvaluationCaseTransfer(REF_CASE, true);
        boolean b = test.checkMultiField(GrouperRefFieldsEn.DIAGNOSIS_POSITION, arrIcds);
        assertTrue(b);
        b = test.checkMultiField(GrouperRefFieldsEn.PROCEDURES_POSITION, arrOpss);
        assertTrue(b);
        arrIcds.add("I37.0^010");
        b = test.checkMultiField(GrouperRefFieldsEn.DIAGNOSIS_POSITION, arrIcds);
        assertTrue(!b);
        arrIcds.remove(4);
        b = test.checkMultiField(GrouperRefFieldsEn.DIAGNOSIS_POSITION, arrIcds);
        assertTrue(!b);
        arrIcds.remove(4);
        b = test.checkMultiField(GrouperRefFieldsEn.DIAGNOSIS_POSITION, arrIcds);
        assertTrue(!b);
        test = new EvaluationCaseTransfer(REF_CASE1, true);
        arrOpss = new ArrayList<>();
        b = test.checkMultiField(GrouperRefFieldsEn.PROCEDURES_POSITION, arrOpss);
        assertTrue(b);
        arrOpss = new ArrayList<>(Arrays.asList(opss1));
        test = new EvaluationCaseTransfer(REF_CASE2, true);
        b = test.checkMultiField(GrouperRefFieldsEn.PROCEDURES_POSITION, arrOpss);
        assertTrue(b);
    }

    @Test
    public void testOpsDifferentOrder() throws UnsupportedEncodingException {

        String[] opss = {"5-563.01&L&20140424^000",
            "5-407.2&&20140424^000",
            "5-569.30&L&20140424^100",
            "5-543.21&&20140424^000",
            "8-137.00&L&20140410^000",
            "8-137.2&L&20140424^000",
            "8-137.03&L&20140424^000",
            "9-401.22&&20140505^000",
            "5-454.50&&20140424^000",
            "8-137.2&L&20140407^000",
            "5-568.D0&L&20140424^000"};

        ArrayList<String> arrOpss = new ArrayList<>(Arrays.asList(opss));
        EvaluationCaseTransfer test = new EvaluationCaseTransfer(REF_CASE3, true);
        boolean b = test.checkMultiField(GrouperRefFieldsEn.PROCEDURES_POSITION, arrOpss);
        assertTrue(b);
    }
}

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

/**
 *
 * @author gerschmann
 */
public class KainInkaStatics {

    public static final String ROOT_KAIN_INKA = "kain_inka";
    public static final String ELEMENT_SEGMENT = "segment";
    public static final String ELEMENT_PROPERTY = "property";
    public static final String ELEMENT_CODE_ELEMENT = "code_element";
    //attributes
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_COMMENT = "comment";
    public static final String ATTRIBUTE_LENGTH = "length"; // exact length of field
    public static final String ATTRIBUTE_MAXLENGTH = "maxlength";// max length of field
    public static final String ATTRIBUTE_NULLABLE = "nullable"; //true -kann, false must field
    public static final String ATTRIBUTE_MAX_COUNT = "max_count"; // maximum number of child segments
    public static final String ATTRIBUTE_DATA_TYPE = "data_type"; // Class to cast the value, default String
    public static final String ATTRIBUTE_FORMAT = "format"; // wenn data_type is  Date - format auf date string
    public static final String ATTRIBUTE_VALUE = "value"; // value of the field
    public static final String ATTRIBUTE_ENUM = "enum"; // value of the field

    //names
    //segments
    public static final String NAME_SEGMENT_FKT = "FKT";
    public static final String NAME_SEGMENT_INV = "INV";
    public static final String NAME_SEGMENT_NAD = "NAD";
    public static final String NAME_SEGMENT_PVV_CHILD = "PVV_child";
    public static final String NAME_SEGMENT_PVT_CHILD = "PVT_child";
    public static final String NAME_SEGMENT_PVV = "PVV";
    public static final String NAME_SEGMENT_PVT = "PVT";
    //code_element    
    public static final String NAME_CODE_ELEMENT_PRINCIPAL_DIAGNOSIS = "principal_diagnosis";
    public static final String NAME_CODE_ELEMENT_SECONDARY_2_PRINCIPAL_DIAGNOSIS = "secondary_2_principal";
    public static final String NAME_CODE_ELEMENT_AUX_DIAGNOSIS = "aux_diagnosis";
    public static final String NAME_CODE_ELEMENT_SECONDARY_AUX_DIAGNOSIS = "secondary_aux_diagnosis";
    public static final String NAME_CODE_ELEMENT_PROCEDURE = "procedure";
    //properties
    public static final String NAME_PROPERTY_MESSAGE_TYPE = "message_type";
    public static final String NAME_PROPERTY_PROCESS_IDENT = "process_ident";
    public static final String NAME_PROPERTY_TRANSACTION_NUMBER = "transaction_number";
    public static final String NAME_PROPERTY_SENDER_IDENT = "sender_ident";
    public static final String NAME_PROPERTY_RECEPTOR_IDENT = "receptor_ident";
    public static final String NAME_PROPERTY_INSURANCE_NUMBER = "insurance_number";
    public static final String NAME_PROPERTY_INSURANCE_TYPE = "insurance_type";
    public static final String NAME_PROPERTY_SPECIAL_CIRCLE = "special_circle";
    public static final String NAME_PROPERTY_DMP_ATTENDANCE = "dmp_attendance";
    public static final String NAME_PROPERTY_VALID_TO = "valid_to";
    public static final String NAME_PROPERTY_CASE_NUMBER_HOSPITAL = "case_number_hospital";
    public static final String NAME_PROPERTY_CASE_NUMBER_INSURANCE = "case_number_insurance";
    public static final String NAME_PROPERTY_REF_NUMBER_INSURANCE = "ref_number_insurance";
    public static final String NAME_PROPERTY_START_OF_INSURANCE = "start_of_insurance";
    public static final String NAME_PROPERTY_CONTRACT_IDENT = "contract_ident";
    public static final String NAME_PROPERTY_PATIENT_NAME = "patient_name";
    public static final String NAME_PROPERTY_PATIENT_FIRST_NAME = "patient_first_name";
    public static final String NAME_PROPERTY_GENDER = "gender";
    public static final String NAME_PROPERTY_BIRTH_DATE = "birth_date";
    public static final String NAME_PROPERTY_ADDRESS = "address";
    public static final String NAME_PROPERTY_ZIP_CODE = "zip_code";
    public static final String NAME_PROPERTY_CITY = "city";
    public static final String NAME_PROPERTY_TITLE = "title";
    public static final String NAME_PROPERTY_COUNTRY_IDENT = "country_ident";
    public static final String NAME_PROPERTY_NAME_AFFIX = "name_affix";
    public static final String NAME_PROPERTY_NAME_PREFIX = "name_prefix";
    public static final String NAME_PROPERTY_ADDRESS_ADD_ON = "address_add_on";
    public static final String NAME_PROPERTY_INFORMATION = "information";
    public static final String NAME_PROPERTY_BILL_NUMBER = "bill_number";
    public static final String NAME_PROPERTY_BILL_DATE = "bill_date";
    public static final String NAME_PROPERTY_PVT_TEXT = "pvt_text";
    public static final String NAME_PROPERTY_CODE = "code";
    public static final String NAME_PROPERTY_LOCALISATION = "localisation";

}

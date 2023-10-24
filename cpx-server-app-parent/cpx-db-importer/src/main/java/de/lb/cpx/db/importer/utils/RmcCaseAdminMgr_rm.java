/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.importer.utils;

import java.io.*;

public class RmcCaseAdminMgr_rm implements Serializable {

    public static final int BREATHING_ALL = 0;
    public static final int BREATHING_WITHOUT = 1;
    public static final int BREATHING_WITH = 2;
    public static final int BREATHING_LESS24 = 3;
    public static final int BREATHING_BET24_95 = 4;
    public static final int BREATHING_MORE95 = 5;

    public static final int AGE_GROUP_0_4 = 1;
    public static final int AGE_GROUP_5_14 = 2;
    public static final int AGE_GROUP_15_39 = 3;
    public static final int AGE_GROUP_40_64 = 4;
    public static final int AGE_GROUP_65_74 = 5;
    public static final int AGE_GROUP_75 = 6;
    public static final int AGE_GROUP_NO = 7;

    public static final int FILL_EXTERNAL = 1;
    public static final int FILL_INTERNAL = 2;

    public static final int KIS_STATUS_UNBEKANNT = 0;
    public static final int KIS_STATUS_FALLFREIGABE = 1;
    public static final int KIS_STATUS_ABRECHNUNGFREIGABE = 2;
    public static final int KIS_STATUS_MC_ANFRAGE = 3;

    public static final int KIS_MEDICO_STATUS_DUMMY = 5;
    public static final int KIS_MEDICO_STATUS_RUECK = 10;
    public static final int KIS_MEDICO_STATUS_HDAUTO = 15;
    public static final int KIS_MEDICO_STATUS_HDMAN = 20;
    public static final int KIS_MEDICO_STATUS_ARBDRG = 30;
    public static final int KIS_MEDICO_STATUS_FRZUR = 35;
    public static final int KIS_MEDICO_STATUS_USR1 = 36;
    public static final int KIS_MEDICO_STATUS_USR2 = 37;
    public static final int KIS_MEDICO_STATUS_USR3 = 38;
    public static final int KIS_MEDICO_STATUS_USR4 = 39;
    public static final int KIS_MEDICO_STATUS_USR5 = 40;
    public static final int KIS_MEDICO_STATUS_USR6 = 41;
    public static final int KIS_MEDICO_STATUS_USR7 = 42;
    public static final int KIS_MEDICO_STATUS_USR8 = 43;
    public static final int KIS_MEDICO_STATUS_USR9 = 44;
    public static final int KIS_MEDICO_STATUS_FREI = 45;
    public static final int KIS_MEDICO_STATUS_ABGER = 50;

    public static final int KIS_GWI_NOT_ACCOUNTED = 99;
    public static final int KIS_GWI_ACCOUNTED = 1;
    public static final int KIS_GWI_CANCELED = 2;
    public static final int KIS_GWI_PART_ACCOUNTED = 3;
    public static final int KIS_GWI_COMPL_ACCOUNTED = 4;
    public static final int KIS_GWI_COLLECT_ACCOUNT = 5;
    public static final int KIS_GWI_TRANSFERED = 7;
    public static final int KIS_GWI_PLANED = 8;
    public static final int KIS_GWI_PLANED_CANCEL = 9;
    public static final int KIS_GWI_PAD = 10;
    public static final int KIS_GWI_TRANSFERED_END = 11;
    public static final int KIS_GWI_COMPARE = 12;
    public static final int KIS_GWI_NOT_ACCOUNTABLE = 13;
    public static final int KIS_GWI_PAYMENT = 14;
    public static final int KIS_GWI_DISK = 15;

    public static final int KIS_FD_NO = 99;
    public static final int KIS_FD_SUGGES = 1;
    public static final int KIS_FD_DELETED = 2;
    public static final int KIS_FD_EMPTY = 3;
    public static final int KIS_FD_VALIDED = 4;
    public static final int KIS_FD_COMMITED = 5;

    public static final int KISSMED_STATUS_OFFSET = 0;
    public static final int MEDICO_STATUS_OFFSET = 100;
    public static final int GWI_STATUS_OFFSET = 200;
    public static final int FDKLINIKA_STATUS_OFFSET = 300;
    public static final int SAP_STATUS_OFFSET = 400;
    private static final long serialVersionUID = 1L;

}

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

/**
 *
 * @author niemeier
 */
public class Constants {

    public static final String CHECKRESULT_TYP_UNKNOWN = "unbekannt";
    public static final String DB_ADAPTOR_ORA = "de.checkpoint.db.OracleServerInterface";

    public static final int KISSMED_KIS = 0;
    public static final int MEDICO_KIS = 1;
    public static final int ORBIS_KIS = 2;
    public static final int MEDICO_INGRES_KIS = 101;
    public static final int FD_KLINIKA = 3;
    public static final int CLINICOM = 4;
    public static final int ISOFT = 5;
    public static final int NEXUS = 6;

    public static final String POSSIBLE_ENTGELT = "Entgelte";
    public static final String POSSIBLE_LABOR = "Labor";
    public static final String POSSIBLE_HOSWARD = "Stationen";
    public static final String POSSIBLE_SVSN_DIAGPROC = "SV/SN Diag./Proz.";

    private Constants() {

    }

}

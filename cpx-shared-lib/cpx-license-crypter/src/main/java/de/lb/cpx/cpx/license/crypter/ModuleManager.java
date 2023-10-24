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
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.cpx.license.crypter;

/**
 *
 * @author nandola
 */
public class ModuleManager {

    //private static ModuleManager moduleManager = null;
//    public static final String PARAMETER_DRG = "-drg";
//    public static final String PARAMETER_FM = "-fm";
//    public static final String PARAMETER_DATA = "-data";
//    public static final String PARAMETER_BUDGET = "-bg";
//    public static final String PARAMETER_GK = "-gk";
//    public static final String PARAMETER_RULESUITE = "-rs";
//    public static final String PARAMETER_MRSA = "-mrsa";
//    public static final String PARAMETER_GKVM = "-gkvm";
//    public static final String PARAMETER_CLIENT = "-client";
//    public static final String PARAMETER_PEPP = "-pepp";
    public static final String PARAMETER_DRG = "DRG";
    public static final String PARAMETER_FM = "FM";
    public static final String PARAMETER_DATA = "DATA";
    public static final String PARAMETER_BUDGET = "BG";
    public static final String PARAMETER_GK = "GK";
    public static final String PARAMETER_RULESUITE = "RS";
    public static final String PARAMETER_MRSA = "MRSA";
    public static final String PARAMETER_GKVM = "GKVM";
    public static final String PARAMETER_CLIENT = "CLIENT";
    public static final String PARAMETER_PEPP = "PEPP";
    public static final String PARAMETER_ACG = "Acg-Viewer";

    //private int m_cpType = 0;
    public static final int CP_TYPE_DRG = 0;
    public static final int CP_TYPE_FM = 1;
    public static final int CP_TYPE_DATA = 2;
    public static final int CP_TYPE_BUDGET = 3;
    public static final int CP_TYPE_GK = 4;
    public static final int CP_TYPE_RULESUITE = 5;
    public static final int CP_TYPE_MORBIRSA = 6;
    public static final int CP_TYPE_GKVM = 7;
    public static final int CP_TYPE_DRG_PEPP = 8;
    public static final int CP_TYPE_PEPP = 9;
    public static final int CP_TYPE_ACG = 10;

    private ModuleManager() {
        //utility class needs no public constructor
    }

//    private String[] m_cp_parameter = {PARAMETER_DRG, PARAMETER_FM, PARAMETER_DATA,
//        PARAMETER_BUDGET, PARAMETER_GK,
//        PARAMETER_RULESUITE, PARAMETER_MRSA,
//        PARAMETER_GKVM, PARAMETER_CLIENT,
//        PARAMETER_PEPP
//    };

    /*
    public String setCPClientState(String arg) {
        switch (arg) {
            case PARAMETER_FM:
                m_cpType = CP_TYPE_FM;
                CheckpointDRG.APP_NAME = CheckpointDRG.APP_NAME_CHECKPOINT_FM + CheckpointDRG.APP_VERSION_FM;
                break;
            case PARAMETER_DATA:
                m_cpType = CP_TYPE_DATA;
                CheckpointDRG.APP_NAME = CheckpointDRG.APP_NAME_CHECKPOINT_DATA + CheckpointDRG.APP_VERSION_DATA;
                break;
            case PARAMETER_BUDGET:
                m_cpType = CP_TYPE_BUDGET;
                CheckpointDRG.APP_NAME = CheckpointDRG.APP_NAME_CHECKPOINT_BUDGET + CheckpointDRG.APP_VERSION_BUDGET;
                break;
            case PARAMETER_GK:
                m_cpType = CP_TYPE_GK;
                CheckpointDRG.APP_NAME = CheckpointDRG.APP_NAME_CHECKPOINT_GK + CheckpointDRG.APP_VERSION_GK;
                break;
            case PARAMETER_RULESUITE:
                m_cpType = CP_TYPE_RULESUITE;
                CheckpointDRG.APP_NAME = "Regel Suite 1.0";
                break;
            case PARAMETER_MRSA:
                m_cpType = CP_TYPE_MORBIRSA;
                CheckpointDRG.APP_NAME = "Morbi RSA 1.0";
                break;
            case PARAMETER_GKVM:
                m_cpType = CP_TYPE_GKVM;
                CheckpointDRG.APP_NAME = "GKVM 1.0";
                break;
            default:
                CheckpointDRG.APP_NAME = getTitleToLicense();
                break;
        }
        return CheckpointDRG.APP_NAME;
    }
     */
}

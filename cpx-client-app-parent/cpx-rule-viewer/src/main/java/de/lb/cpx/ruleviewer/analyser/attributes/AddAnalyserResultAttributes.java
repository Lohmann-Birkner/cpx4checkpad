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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.analyser.attributes;

import de.lb.cpx.grouper.model.transfer.TransferDrg;
import de.lb.cpx.grouper.model.transfer.TransferGroupResult;
import de.lb.cpx.grouper.model.transfer.TransferPepp;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;

/**
 *
 * @author wilde
 */
public class AddAnalyserResultAttributes extends AnalyserAttributes {

    public static final String KEY_PCCL = "pccl";
    public static final String KEY_DRG_GROUP_ATT = "drgcode";
    public static final String KEY_DRG_PEPP = "code";
    public static final String KEY_DRG = "code";
    public static final String KEY_PEPP = "code";
    public static final String KEY_ADRG = "adrg";
    public static final String KEY_MDC_SK = "group";
    public static final String KEY_SK = "group_SK";
    public static final String KEY_MDC = "group_MDC";
    public static final String KEY_BASERATE = "baserate";
    public static final String KEY_DRG_PARTITION = "drgPartition";
    public static final String KEY_CW_EFF = "cwEffectiv";
    public static final String KEY_CW_DRG = "cwDrg";
    public static final String KEY_CW_PEPP = "cwPepp";
    public static final String KEY_CW_KATALOG = "cw_catalog";
    public static final String KEY_LOS = "lengthOfStay";
    public static final String KEY_UPPER_LOS = "htp";
    public static final String KEY_LOWER_LOS = "ltp";
    public static final String KEY_AVERAGE_LOS = "alos";
    public static final String KEY_DELTA_CW = "deltaCw";
    public static final String KEY_SUGG_SIM_CODE = "suggDrg";
    public static final String KEY_SUGG_DRG = "suggSimDrg";
    public static final String KEY_SUGG_PEPP = "suggSimPepp";
    public static final String KEY_SUGG_SIM_CW = "suggResult";
    private static AddAnalyserResultAttributes INSTANCE;

    private AddAnalyserResultAttributes() {

        addGroupAttribute(KEY_DRG_GROUP_ATT, TransferDrg.class)
                .addAttribute(KEY_DRG)
                .addAttribute(KEY_ADRG, TransferDrg.class)
                .setDisplayName("DRG");
        addSingleAttribute(KEY_PEPP, TransferPepp.class)
                .setDisplayName("PEPP");
        addSingleAttribute(KEY_PCCL, TransferGroupResult.class)
                .setDisplayName("PCCL");
//        addSingleAttribute(KEY_DRG_PEPP, TransferGroupResult.class)
//            .setDisplayName("DRG/PEPP");
//        addSingleAttribute(KEY_ADRG, TransferDrg.class)
//            .setDisplayName("ADRG");
//        addSingleAttribute(KEY_DRG_PEPP, TransferGroupResult.class)
//                .setDisplayName("DRG/PEPP");
//        addSingleAttribute(KEY_MDC_SK, TransferGroupResult.class)
//                .setDisplayName("MDC/SK");
        addSingleAttribute(KEY_MDC, TransferDrg.class, "getGroup", "setGroup")
                .setDisplayName("MDC");
        addSingleAttribute(KEY_SK, TransferPepp.class, "getGroup", "setGroup")
                .setDisplayName("SK");
        addSingleAttribute(KEY_DRG_PARTITION, TransferDrg.class)
                .setDisplayName("DRG Partition");
        addSingleAttribute(KEY_BASERATE, TransferRuleAnalyseResult.class)
                .setDisplayName("Baserate");

        addGroupAttribute(KEY_CW_DRG, TransferDrg.class)
                .addAttribute(KEY_CW_EFF, TransferGroupResult.class)
                .addAttribute(KEY_CW_KATALOG, TransferDrg.class)
                .setDisplayName("CW");
        addGroupAttribute(KEY_CW_PEPP, TransferPepp.class)
                .addAttribute(KEY_CW_EFF, TransferGroupResult.class)
                .setDisplayName("CW");
//        addSingleAttribute(KEY_CW_EFF, TransferGroupResult.class)
//                .setDisplayName("CW(eff.)");
        addSingleAttribute(KEY_LOS, TransferRuleAnalyseResult.class)
                .setDisplayName("Verweildauer");
        addSingleAttribute(KEY_LOWER_LOS, TransferDrg.class)
                .setDisplayName("UGVD");
        addSingleAttribute(KEY_AVERAGE_LOS, TransferDrg.class)
                .setDisplayName("mVWD");
        addSingleAttribute(KEY_UPPER_LOS, TransferDrg.class)
                .setDisplayName("OGVD");
        addGroupAttribute(KEY_SUGG_DRG, TransferDrg.class)
                .addAttribute(KEY_SUGG_SIM_CODE, TransferRuleAnalyseResult.class)
                .setDisplayName("DRG(sim.)");
        addGroupAttribute(KEY_SUGG_PEPP, TransferPepp.class)
                .addAttribute(KEY_SUGG_SIM_CODE, TransferRuleAnalyseResult.class)
                .setDisplayName("PEPP(sim.)");
        addSingleAttribute(KEY_SUGG_SIM_CW, TransferRuleAnalyseResult.class)
                .setDisplayName("CW(sim.)");
        addSingleAttribute(KEY_DELTA_CW, TransferRuleAnalyseResult.class)
                .setDisplayName("dCW");
//        addSingleAttribute(KEY_SUGG_DRG, KTransferRuleAnalyseResult.class)
//                .setDisplayName("DRG(sim.)");
//        addSingleAttribute(KEY_SUGG_PEPP, TransferRuleAnalyseResult.class)
//                .setDisplayName("PEPP(sim.)");

    }

    public static AddAnalyserResultAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new AddAnalyserResultAttributes();
        }
        return INSTANCE;
    }

}

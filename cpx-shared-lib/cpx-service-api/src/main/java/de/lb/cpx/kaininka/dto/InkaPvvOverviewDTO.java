/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.kaininka.dto;

import de.lb.cpx.model.TP301KainInkaPvt;
import de.lb.cpx.model.TP301KainInkaPvv;
import java.io.Serializable;

/**
 *
 * @author nandola
 */
public class InkaPvvOverviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private TP301KainInkaPvv pvv;
    private TP301KainInkaPvt pvt;
    private String infoKey30;
    private String billNo;
    private String pvtText;

    public InkaPvvOverviewDTO(TP301KainInkaPvv pvv, TP301KainInkaPvt pvt) {
        this.pvv = pvv;
        this.pvt = pvt;
        this.infoKey30 = pvv.getInformationKey30();
        this.billNo = pvv.getBillNr();
//        this.pvtText = pvt.gettext();
    }

    public TP301KainInkaPvv getPvv() {
        return pvv;
    }

    public String getInfoKey30() {
        return infoKey30;
    }

    public String getBillNo() {
        return billNo;
    }

    public TP301KainInkaPvt getPvt() {
        return pvt;
    }

    public String getPvtText() {
        return pvtText;
    }

    public void setPvv(TP301KainInkaPvv pvv) {
        this.pvv = pvv;
    }

    public void setInfoKey30(String infoKey30) {
        this.infoKey30 = infoKey30;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public void setPvt(TP301KainInkaPvt pvt) {
        this.pvt = pvt;
    }
//    public void setPvt(TP301KainInkaPvv pvv, TP301KainInkaPvt pvt) {
//        this.pvv = pvv;
//        this.pvt = pvt;
//    }

    public void setPvtText(String pvtText) {
        this.pvtText = pvtText;
    }

}

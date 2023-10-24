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
package de.lb.cpx.sap.dto;

/**
 *
 * @author niemeier
 */
public class SapCaseMessage {

    private String mFallNr;
    private String mIkz;
    private String mHost;
    private String mSysNr;
    private String mSendingFac;
    private String mSendingApp;

    /**
     *
     */
    public SapCaseMessage() {
    }

    /**
     * @return the mFallNr
     */
    public String getmFallNr() {
        return mFallNr;
    }

    /**
     * @param mFallNr the mFallNr to set
     */
    public void setmFallNr(String mFallNr) {
        this.mFallNr = mFallNr;
    }

    /**
     * @return the mIkz
     */
    public String getmIkz() {
        return mIkz;
    }

    /**
     * @param mIkz the mIkz to set
     */
    public void setmIkz(String mIkz) {
        this.mIkz = mIkz;
    }

    /**
     * @return the mHost
     */
    public String getmHost() {
        return mHost;
    }

    /**
     * @param mHost the mHost to set
     */
    public void setmHost(String mHost) {
        this.mHost = mHost;
    }

    /**
     * @return the mSysNr
     */
    public String getmSysNr() {
        return mSysNr;
    }

    /**
     * @param mSysNr the mSysNr to set
     */
    public void setmSysNr(String mSysNr) {
        this.mSysNr = mSysNr;
    }

    /**
     * @return the mSendingFac
     */
    public String getmSendingFac() {
        return mSendingFac;
    }

    /**
     * @param mSendingFac the mSendingFac to set
     */
    public void setmSendingFac(String mSendingFac) {
        this.mSendingFac = mSendingFac;
    }

    /**
     * @return the mSendingApp
     */
    public String getmSendingApp() {
        return mSendingApp;
    }

    /**
     * @param mSendingApp the mSendingApp to set
     */
    public void setmSendingApp(String mSendingApp) {
        this.mSendingApp = mSendingApp;
    }

    @Override
    public String toString() {
        return getmSendingApp() + ";"
                + getmSendingFac() + ";"
                + getmSysNr() + ";"
                + getmHost() + ";"
                + getmFallNr();
    }

}

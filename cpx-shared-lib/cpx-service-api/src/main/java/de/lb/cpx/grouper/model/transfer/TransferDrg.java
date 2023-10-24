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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public class TransferDrg extends TransferGroupResult {

    private static final long serialVersionUID = 1L;

    private int typeOfCorrection;
    private String adrg;
    private int correctionDays;
    private double correctionValue;
    private int htp;//ogvd
    private String drgPartition;
    private boolean isException;
    private double tge2day;
    private int tgeDays;
    private int ltp;//ugvd
    private double alos;
//    private double cwCatalog;
    private double cwCorr2Day;
//    private double careCwDay;
    private int careDays;
    private double careCw;
    private List<TransferDrgCare> careValues = new ArrayList<>();
    public TransferDrg() {
        super();
    }

    public void setAdrg(String adrg) {
        this.adrg = adrg;
    }

    public void setTypeOfCorr(int typeOfCorr) {
        typeOfCorrection = typeOfCorr;
    }

    public void setCwCorr(double cwCorr) {
        correctionValue = cwCorr;
    }

    public void setCorrDays(int corrDays) {
        correctionDays = corrDays;
    }

    public int getTypeOfCorrection() {
        return typeOfCorrection;
    }

    public String getAdrg() {
        return adrg;
    }

    public int getCorrectionDays() {
        return correctionDays;
    }

    public double getCorrectionValue() {
        return correctionValue;
    }

    public void setHtp(int ogvd) {
        htp = ogvd;
    }

    public void setDrgPartition(String drgPartition) {
        this.drgPartition = drgPartition;
    }

    public void setIsException(boolean ausnahmeDrg) {
        isException = ausnahmeDrg;
    }

    public int getHtp() {
        return htp;
    }

    public String getDrgPartition() {
        return drgPartition;
    }

    public boolean isException() {
        return isException;
    }

    public void setTge2Day(double tge2day, int los) {
        this.tge2day = tge2day;
        tgeDays = tge2day > 0 ? los : 0;
    }

    public double getTge2Day() {
        return tge2day;
    }

    public int getTgeDays() {
        return tgeDays;
    }

    public int getLtp() {
        return ltp;
    }

    public void setLtp(int pLtp) {
        this.ltp = pLtp;
    }

    public double getAlos() {
        return alos;
    }

    public void setAlos(double pAlos) {
        this.alos = pAlos;
    }
//
//    public double getCwCatalog() {
//        return cwCatalog;
//    }
//
//    public void setCwCatalog(double pCwCatalog) {
//        super.setCwCatalog(pCwCatalog);
//        this.cwCatalog = pCwCatalog;
//    }

    public double getCwCorr2Day() {
        return cwCorr2Day;
    }

    public void seCwCorr2Day(double pCwCorr2Day) {
        this.cwCorr2Day = pCwCorr2Day;
    }

//    public double getCareCwDay() {
//        return careCwDay;
//    }
//
//    public void setCareCwDay(double pCareCwDay) {
//        this.careCwDay = pCareCwDay;
//    }

    public int getCareDays() {
        return careDays;
    }

    public void setCareDays(int pCareDays) {
        this.careDays = pCareDays;
    }

    public void setCareCw(double d) {
        careCw = d;
    }

    public double getCareCw(){
        return careCw;
    }

    public List<TransferDrgCare> getCareValues() {
        return careValues;
    }
    
    public void addOneCareGrade(int pSortind, int pCareDays, double pCareCwDay, double pBaserate, Date pStartDate, Date pEndDate){
        getCareValues().add(new TransferDrgCare(pSortind, pCareDays, pCareCwDay, pBaserate, pStartDate, pEndDate));
    }

}

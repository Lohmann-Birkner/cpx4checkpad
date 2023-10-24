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
import java.util.List;

/**
 *
 * @author gerschmann
 */
public class TransferPepp extends TransferGroupResult {

    private static final long serialVersionUID = 1L;

    private int m_adultPersonalCare; // tage 1:1 behandlung erwachsene
    private int m_infantsPersonalCare; // tage 1:1 behandlung kinder
    private int m_durationsIntensivCare; // tage intensivbehandlung
    private int m_durationsIntesivPersentage; // Anteil der Intensivbehandlung
    private boolean m_hasGrades; // Vergütungsstufen ermittelt(<=2014)
    private List<TransferPeppGrade> m_grades; // calculated distribution of cws and baserates to calculated paygrades or payclasses
    private int m_payClass;
    private double peppCw4Class;
    
    public TransferPepp() {
        super();
    }

    public void setAdultPersonalCare(int adultPersonalCare) {
        m_adultPersonalCare = adultPersonalCare;
    }

    public void setInfantsPersonalCare(int infantsPersonalCare) {
        m_infantsPersonalCare = infantsPersonalCare;
    }

    public void setDurationsIntensivCare(int durationsIntensivCare) {
        m_durationsIntensivCare = durationsIntensivCare;
    }

    public void setDurationsIntesivPersentage(int durationsIntesivPersentage) {
        m_durationsIntesivPersentage = durationsIntesivPersentage;
    }

    public void setPeppType(boolean hasGrades) {
        m_hasGrades = hasGrades;
    }

    public boolean getHasGrades() {
        return m_hasGrades;
    }

    public void setCalculatedGrades(List<TransferPeppGrade> grades) {
        m_grades = grades == null ? null : new ArrayList<>(grades);
    }

    public int getAdultPersonalCare() {
        return m_adultPersonalCare;
    }

    public int getInfantsPersonalCare() {
        return m_infantsPersonalCare;
    }

    public int getDurationsIntensivCare() {
        return m_durationsIntensivCare;
    }

    public int getDurationsIntesivPersentage() {
        return m_durationsIntesivPersentage;
    }

    public List<TransferPeppGrade> getGrades() {
        return m_grades == null ? null : new ArrayList<>(m_grades);
    }

    public int getPayClass() {
        return m_payClass;
    }

    public void setPayClass(int payClass) {
        this.m_payClass = payClass;
    }

    void setPeppCw4Class(double cw) {
        peppCw4Class = cw;
    }

    public double getPeppCw4Class() {
        return peppCw4Class;
    }
}

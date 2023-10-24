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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.acg;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class AcgPatientData implements Serializable {

    private static final Logger LOG = Logger.getLogger(AcgPatientData.class.getName());
    private static final long serialVersionUID = 1L;

    public final TreeSet<Integer> lineNumbers; //TreeSet is serializable!
    public final String patientennummer;
    public final int jahr;
    public final int periode;
    public final int datenjahr;
    public final String bezeichnung;
    public final Date geburtsdatum;
    public final int alter;
    public final char geschlecht;
    public final TreeSet<IcdFarbeOrgan> icd_code; //TreeSet is serializable!
    public final int chronische_erkrankungen_patient;
    public final double chronische_erkrankungen_altersgruppe;
    public final AcgIndexType altersbedingte_makuladegeneration;
    public final AcgIndexType bipolare_stoerung;
    public final AcgIndexType herzinsuffizienz;
    public final AcgIndexType depression;
    public final AcgIndexType diabetes;
    public final AcgIndexType glaukom;
    public final AcgIndexType hiv;
    public final AcgIndexType fettstoffwechselstoerung;
    public final AcgIndexType bluthochdruck;
    public final AcgIndexType schilddruesenunterfunktion;
    public final AcgIndexType immunsuppression;
    public final AcgIndexType koronare_herzerkrankung;
    public final AcgIndexType osteoporose;
    public final AcgIndexType morbus_parkinson;
    public final AcgIndexType asthma;
    public final AcgIndexType rheumatoide_arthritis;
    public final AcgIndexType schizophrenie;
    public final AcgIndexType adipositas;
    public final AcgIndexType copd;
    public final AcgIndexType nierenversagen;
    public final AcgIndexType rueckenschmerz;
    public final boolean has_chronic_conditions;
    public final double relativgewicht_patient;
    public final double relativgewicht_altersgruppe;
    public final int ressourcenverbrauchsgruppe_patient;
    public final double ressourcenverbrauchsgruppe_altersgruppe;
    public final boolean gebrechlichkeit;

    public AcgPatientData(
            final Collection<Integer> pLineNumbers,
            final String pPatientennummer,
            final String pJahr,
            final String pPeriode,
            final String pDatenjahr,
            final String pBezeichnung,
            final String pGeburtsdatum,
            final String pAlter,
            final String pGeschlecht,
            final Collection<IcdFarbeOrgan> pIcdCode,
            final String pChronischeErkrankungenPatient,
            final String pChronischeErkrankungenAltersgruppe,
            final String pAltersbedingteMakuladegeneration,
            final String pBipolareStoerung,
            final String pHerzinsuffizienz,
            final String pDepression,
            final String pDiabetes,
            final String pGlaukom,
            final String pHiv,
            final String pFettstoffwechselstoerung,
            final String pBluthochdruck,
            final String pSchilddruesenunterfunktion,
            final String pImmunsuppression,
            final String pKoronareHerzerkrankung,
            final String pOsteoporose,
            final String pMorbusParkinson,
            final String pAsthma,
            final String pRheumatoideArthritis,
            final String pSchizophrenie,
            final String pAdipositas,
            final String pCopd,
            final String pNierenversagen,
            final String pRueckenschmerz,
            final String pRelativgewichtPatient,
            final String pRelativgewichtAltersgruppe,
            final String pRessourcenverbrauchsgruppePatient,
            final String pRessourcenverbrauchsgruppeAltersgruppe,
            final String pGebrechlichkeit) {
        this.lineNumbers = pLineNumbers == null ? new TreeSet<>() : new TreeSet<>(pLineNumbers);
        this.jahr = toJahr(pJahr);
        this.periode = toPeriode(pPeriode);
        this.datenjahr = toDatenjahr(pDatenjahr);
        //this.jahr = Calendar.getInstance().get(Calendar.YEAR) + this.datenjahr;
        this.patientennummer = toVersichertennummer(pPatientennummer);
        this.bezeichnung = pBezeichnung == null ? "" : pBezeichnung.trim();
        this.geburtsdatum = toGeburtsdatum(pGeburtsdatum);
        this.alter = toAlter(pAlter);
        this.geschlecht = toGeschlecht(pGeschlecht);
        //this.icd_code = toIcd(icd_code);
        //this.icd_code = splitIcds(pIcdCode);
        this.icd_code = pIcdCode == null ? new TreeSet<>() : new TreeSet<>(pIcdCode);
        if (this.icd_code.isEmpty()) {
            LOG.log(Level.WARNING, "There are no diagnosis assigned to patient with insurance number " + patientennummer + "!");
        }
        this.chronische_erkrankungen_patient = toChronicConditionCountPatient(pChronischeErkrankungenPatient);
        this.chronische_erkrankungen_altersgruppe = toChronicConditionCountAltersgruppe(pChronischeErkrankungenAltersgruppe);
        this.altersbedingte_makuladegeneration = toCondition(pAltersbedingteMakuladegeneration, "Altersbedingte Makuladegeneration");
        this.bipolare_stoerung = toCondition(pBipolareStoerung, "Bipolare Störung");
        this.herzinsuffizienz = toCondition(pHerzinsuffizienz, "Herzinsuffizienz");
        this.depression = toCondition(pDepression, "Depression");
        this.diabetes = toCondition(pDiabetes, "Diabetes");
        this.glaukom = toCondition(pGlaukom, "Glaukom");
        this.hiv = toCondition(pHiv, "HIV");
        this.fettstoffwechselstoerung = toCondition(pFettstoffwechselstoerung, "Fettstoffwechselstörung");
        this.bluthochdruck = toCondition(pBluthochdruck, "Bluthochdruck");
        this.schilddruesenunterfunktion = toCondition(pSchilddruesenunterfunktion, "Schilddrüsenunterfunktion");
        this.immunsuppression = toCondition(pImmunsuppression, "Immunsuppression");
        this.koronare_herzerkrankung = toCondition(pKoronareHerzerkrankung, "Koronare Herzerkrankung");
        this.osteoporose = toCondition(pOsteoporose, "Osteoporose");
        this.morbus_parkinson = toCondition(pMorbusParkinson, "Morbus Parkinson");
        this.asthma = toCondition(pAsthma, "Asthma");
        this.rheumatoide_arthritis = toCondition(pRheumatoideArthritis, "Rheumatoide Arthritis");
        this.schizophrenie = toCondition(pSchizophrenie, "Schizophrenie");
        this.adipositas = toCondition(pAdipositas, "Adipositas");
        this.copd = toCondition(pCopd, "COPD");
        this.nierenversagen = toCondition(pNierenversagen, "Nierenversagen");
        this.rueckenschmerz = toCondition(pRueckenschmerz, "Rückenschmerz");
        this.has_chronic_conditions = !(altersbedingte_makuladegeneration.equals(AcgIndexType.NP)
                && bipolare_stoerung.equals(AcgIndexType.NP)
                && herzinsuffizienz.equals(AcgIndexType.NP)
                && depression.equals(AcgIndexType.NP)
                && diabetes.equals(AcgIndexType.NP)
                && glaukom.equals(AcgIndexType.NP)
                && hiv.equals(AcgIndexType.NP)
                && fettstoffwechselstoerung.equals(AcgIndexType.NP)
                && bluthochdruck.equals(AcgIndexType.NP)
                && schilddruesenunterfunktion.equals(AcgIndexType.NP)
                && immunsuppression.equals(AcgIndexType.NP)
                && koronare_herzerkrankung.equals(AcgIndexType.NP)
                && osteoporose.equals(AcgIndexType.NP)
                && morbus_parkinson.equals(AcgIndexType.NP)
                && asthma.equals(AcgIndexType.NP)
                && rheumatoide_arthritis.equals(AcgIndexType.NP)
                && schizophrenie.equals(AcgIndexType.NP)
                && adipositas.equals(AcgIndexType.NP)
                && copd.equals(AcgIndexType.NP)
                && nierenversagen.equals(AcgIndexType.NP)
                && rueckenschmerz.equals(AcgIndexType.NP));
        this.relativgewicht_patient = toReferenceRescaledWeight(pRelativgewichtPatient);
        this.relativgewicht_altersgruppe = toReferenceRescaledWeight(pRelativgewichtAltersgruppe);
        this.ressourcenverbrauchsgruppe_patient = toResourceUtilizationBandPatient(pRessourcenverbrauchsgruppePatient);
        this.ressourcenverbrauchsgruppe_altersgruppe = toResourceUtilizationBandAltersgruppe(pRessourcenverbrauchsgruppeAltersgruppe);
        this.gebrechlichkeit = toFrailtyFlag(pGebrechlichkeit);
    }

    public String getResourceUtilizationBandDescription() {
        return (ressourcenverbrauchsgruppe_patient >= 5 ? "Sehr hoch"
                : ressourcenverbrauchsgruppe_patient >= 4 ? "Hoch"
                        : ressourcenverbrauchsgruppe_patient >= 3 ? "Mittel"
                                : ressourcenverbrauchsgruppe_patient >= 2 ? "Niedrig"
                                        : ressourcenverbrauchsgruppe_patient >= 1 ? "Sehr niedrig"
                                                : "Keiner");
    }

    public String getGeschlechtDescription() {
        return 'm' == Character.toLowerCase(geschlecht) ? "männlich"
                : 'w' == Character.toLowerCase(geschlecht) ? "weiblich"
                : 'f' == Character.toLowerCase(geschlecht) ? "weiblich"
                : 'u' == Character.toLowerCase(geschlecht) ? "unbekannt"
                : "indifferent"; //"i"        
    }

    public String getFrailtyFlagDescription() {
        return gebrechlichkeit ? "ja" : "nein";
    }

    private static String toStr(final String pValue) {
        if (pValue == null) {
            LOG.log(Level.INFO, "Empty text value found");
            return "";
        }
        return pValue.trim();
    }

    public static Integer toInt(final String pValue) {
//        if (pValue == null) {
//            LOG.log(Level.INFO, "Empty numeric value found, use '0' as default");
//            return 0;
//        }
        final String value = toStr(pValue).toLowerCase();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            //throw new IllegalArgumentException("This is not a valid integer: " + pValue, ex);
            LOG.log(Level.FINE, "This is not a valid integer: " + pValue, ex);
        }
        return null;
    }

    private static Boolean toBool(final String pValue) {
        final String value = toStr(pValue).toLowerCase();
        if (value.equals("wahr") || value.equals("true") || value.equals("ja")
                || value.equals("1") || value.equals("enabled")
                || value.equals("enable") || value.equals("aktiv")
                || value.equals("aktiviert") || value.equals("yes")
                || value.equals("y") || value.equals("j")) {
            return true;
        }
        if (value.equals("falsch") || value.equals("false") || value.equals("nein")
                || value.equals("0") || value.equals("disabled")
                || value.equals("disable") || value.equals("deaktiv")
                || value.equals("deaktiviert") || value.equals("no")
                || value.equals("n")) {
            return false;
        }
        //throw new IllegalArgumentException("This is not a valid boolean: " + pValue);
        return null;
    }

    private static Date toDate(final String pValue) {
        final String value = toStr(pValue).toLowerCase();

        DateFormat sdf;

        sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return new java.sql.Date(sdf.parse(value).getTime());
        } catch (ParseException ex) {
            LOG.log(Level.FINE, null, ex);
        }

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new java.sql.Date(sdf.parse(value).getTime());
        } catch (ParseException ex) {
            LOG.log(Level.FINE, null, ex);
        }
        return null;
    }

    public static char toGeschlecht(final String pValue) {
        if (pValue == null || pValue.trim().isEmpty()) {
            LOG.log(Level.INFO, "Empty gender value found, use 'u' as default");
            return 'u';
        }
        final String value = toStr(pValue).toLowerCase();
        if (value.equals("m") || value.startsWith("m")) {
            return 'm';
        }
        if (value.equals("w") || value.startsWith("w")) {
            return 'w';
        }
        if (value.equals("f") || value.startsWith("f")) {
            return 'w';
        }
        if (value.equals("u") || value.startsWith("u")) {
            return 'u';
        }
        if (value.equals("i") || value.startsWith("i")) {
            return 'i';
        }
        throw new IllegalArgumentException("This is not a valid gender: " + pValue);
    }

    private AcgIndexType toCondition(final String pCondition, final String pCaption) {
        if (pCondition == null || pCondition.trim().isEmpty()) {
            LOG.log(Level.FINE, "Empty numeric value for " + pCaption + " for patient insurance number " + patientennummer + " found, use 'NP' as default");
            return AcgIndexType.NP;
        }
        final AcgIndexType val = toCondition(pCondition);
        if (val == null) {
            throw new IllegalArgumentException("This " + pCaption + " for patient insurance number " + patientennummer + " is not a condition value: " + pCondition);
        }
        return val;
    }

    private static AcgIndexType toCondition(final String pValue) {
//        if (pValue == null) {
//            LOG.log(Level.INFO, "Empty chronic condition value found, use none as default");
//            return "";
//        }
        final String value = toStr(pValue);
        if (value.equalsIgnoreCase("ICD")) {
            return AcgIndexType.ICD;
        }
        if (value.equalsIgnoreCase("NP")) {
            return AcgIndexType.NP;
        }
        if (value.equalsIgnoreCase("TRT")) {
            return AcgIndexType.TRT;
        }
        if (value.equalsIgnoreCase("Rx")) {
            return AcgIndexType.Rx;
        }
        if (value.equalsIgnoreCase("BTH")) {
            return AcgIndexType.BTH;
        }
        return null;
        //throw new IllegalArgumentException("This is not a valid chronic condition: " + pValue);
    }

    private static Double toDouble(final String pValue) {
//        if (pValue == null) {
//            LOG.log(Level.INFO, "Empty floating value found, use '0.0' as default");
//            return 0.0d;
//        }
        final String value = toStr(pValue).toLowerCase();
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            //throw new IllegalArgumentException("This is not a valid double value: " + pValue, ex);
            LOG.log(Level.FINE, null, ex);
        }

        try {
            return Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException ex) {
            //throw new IllegalArgumentException("This is not a valid double value: " + pValue, ex);
            LOG.log(Level.FINE, null, ex);
        }
        //throw new IllegalArgumentException("This is not a valid floating value: " + pValue);
        return null;
    }

//    private String[] split(final String pValue) {
//        final String value = toStr(pValue);
//        return value.split(" ");
//    }
    @Override
    public String toString() {
        return "AcgPatientData{" + "lineNumber=" + lineNumbers + ", jahr=" + jahr + ", datenjahr=" + datenjahr + ", patientennummer=" + patientennummer + ", geburtsdatum=" + geburtsdatum + ", alter=" + alter + ", geschlecht=" + geschlecht + ", icd_code=" + icd_code + ", chronische_erkrankungen_patient=" + chronische_erkrankungen_patient + ", chronische_erkrankungen_altersgruppe=" + chronische_erkrankungen_altersgruppe + ", altersbedingte_makuladegeneration=" + altersbedingte_makuladegeneration + ", bipolare_stoerung=" + bipolare_stoerung + ", herzinsuffizienz=" + herzinsuffizienz + ", depression=" + depression + ", diabetes=" + diabetes + ", glaukom=" + glaukom + ", hiv=" + hiv + ", fettstoffwechselstoerung=" + fettstoffwechselstoerung + ", bluthochdruck=" + bluthochdruck + ", schilddruesenunterfunktion=" + schilddruesenunterfunktion + ", immunsuppression=" + immunsuppression + ", koronare_herzerkrankung=" + koronare_herzerkrankung + ", osteoporose=" + osteoporose + ", morbus_parkinson=" + morbus_parkinson + ", asthma=" + asthma + ", rheumatoide_arthritis=" + rheumatoide_arthritis + ", schizophrenie=" + schizophrenie + ", adipositas=" + adipositas + ", copd=" + copd + ", nierenversagen=" + nierenversagen + ", rueckenschmerz=" + rueckenschmerz + ", has_chronic_conditions=" + has_chronic_conditions + ", relativgewicht_patient=" + relativgewicht_patient + ", relativgewicht_altersgruppe=" + relativgewicht_altersgruppe + ", ressourcenverbrauchsgruppe_patient=" + ressourcenverbrauchsgruppe_patient + ", ressourcenverbrauchsgruppe_altersgruppe=" + ressourcenverbrauchsgruppe_altersgruppe + ", gebrechlichkeit=" + gebrechlichkeit + '}';
    }

//    @Override
//    public int hashCode() {
//        int hash = 3;
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final AcgPatientData other = (AcgPatientData) obj;
//        if (this.datenjahr != other.datenjahr) {
//            return false;
//        }
//        if (!Objects.equals(this.patientennummer, other.patientennummer)) {
//            return false;
//        }
//        return true;
//    }
    private int toDatenjahr(final String pDatenjahr) {
        if (pDatenjahr == null) {
            LOG.log(Level.WARNING, "Empty numeric value for datenjahr for patient insurance number " + patientennummer + " found, use '0' as default");
            return 0;
        }
        final Integer val = toInt(pDatenjahr);
        if (val == null) {
            throw new IllegalArgumentException("This datenjahr for patient insurance number " + patientennummer + " is not a valid number: " + pDatenjahr);
        }
        if (!isDatenjahrValidRange(val)) {
            LOG.log(Level.WARNING, "datenjahr for patient insurance number " + patientennummer + " is suspicious: " + val);
        }
        return val;
    }

    public static boolean isDatenjahrValidRange(final int pDatenjahr) {
        if (pDatenjahr < -5 || pDatenjahr > 0) {
            return false;
        }
        return true;
    }

    private int toPeriode(final String pPeriode) {
        if (pPeriode == null) {
            LOG.log(Level.WARNING, "Empty numeric value for periode for patient insurance number {0} found, use ''1'' as default", patientennummer);
            return 1;
        }
        final Integer val = toInt(pPeriode);
        if (val == null) {
            throw new IllegalArgumentException("This periode for patient insurance number " + patientennummer + " is not a valid number: " + pPeriode);
        }
        if (val < 1 || val > 4) {
            LOG.log(Level.WARNING, "periode for patient insurance number {0} is suspicious: {1}", new Object[]{patientennummer, val});
        }
        return val;
    }

    private int toJahr(final String pJahr) {
        if (pJahr == null) {
            LOG.log(Level.WARNING, "Empty numeric value for jahr for patient insurance number " + patientennummer + " found, use '0' as default");
            return 0;
        }
        final Integer val = toInt(pJahr);
        if (val == null) {
            throw new IllegalArgumentException("This jahr for patient insurance number " + patientennummer + " is not a valid number: " + pJahr);
        }
        if (val < 1990 || val > 2050) {
            LOG.log(Level.WARNING, "jahr for patient insurance number " + patientennummer + " is suspicious: " + val);
        }
        return val;
    }

    private String toVersichertennummer(final String pVersichertennummer) {
        final String val = toStr(pVersichertennummer);
        if (val.isEmpty()) {
            LOG.log(Level.WARNING, "Empty text value for patientennummer found, use '' as default");
            return "";
        }
        return val;
    }

    private Date toGeburtsdatum(final String pGeburtsdatum) {
        final String gebdat = toStr(pGeburtsdatum);
        if (gebdat.isEmpty()) {
            LOG.log(Level.FINE, "Empty date value for geburtsdatum for patient insurance number " + patientennummer + " found, use null as default");
            return null;
        }
        final Date val = toDate(gebdat);
        if (val == null) {
            throw new IllegalArgumentException("This geburtsdatum for patient insurance number " + patientennummer + " is not a valid date: " + pGeburtsdatum);
        }
        return val;
    }

    private int toAlter(final String pAlter) {
        if (pAlter == null) {
            LOG.log(Level.WARNING, "Empty numeric value for alter for patient insurance number " + patientennummer + " found, use '0' as default");
            return 0;
        }
        final Integer val = toInt(pAlter);
        if (val == null) {
            throw new IllegalArgumentException("This alter for patient insurance number " + patientennummer + " is not a valid number: " + pAlter);
        }
        if (val < 0 || val > 120) {
            LOG.log(Level.WARNING, "Alter for patient insurance number " + patientennummer + " is suspicious: " + val);
        }
        return val;
    }

//    private String toIcd(final String pIcdCode) {
//        final String val = toStr(pIcdCode);
//        if (val.isEmpty()) {
//            LOG.log(Level.WARNING, "Empty text value for icd code for patient insurance number " + patientennummer + " found, use '' as default");
//            return "";
//        }
//        return val;
//    }
    private int toChronicConditionCountPatient(final String pChronicConditionCount) {
        if (pChronicConditionCount == null) {
            LOG.log(Level.WARNING, "Empty numeric value for chronic condition count for patient insurance number " + patientennummer + " found, use '0' as default");
            return 0;
        }
        final Integer val = toInt(pChronicConditionCount);
        if (val == null) {
            throw new IllegalArgumentException("This chronic condition count for patient insurance number " + patientennummer + " is not a valid number: " + pChronicConditionCount);
        }
        if (val < 0 || val > 100) {
            LOG.log(Level.WARNING, "chronic condition count for patient insurance number " + patientennummer + " is suspicious: " + val);
        }
        return val;
    }

    private double toChronicConditionCountAltersgruppe(final String pChronicConditionCount) {
        if (pChronicConditionCount == null || pChronicConditionCount.trim().isEmpty()) {
            LOG.log(Level.WARNING, "Empty numeric value for chronic condition altersgruppe count for patient insurance number " + patientennummer + " found, use '0' as default");
            return 0;
        }
        final Double val = toDouble(pChronicConditionCount);
        if (val == null) {
            throw new IllegalArgumentException("This chronic condition count altersgruppe for patient insurance number " + patientennummer + " is not a valid number: " + pChronicConditionCount);
        }
        if (val < 0.0d || val > 100.0d) {
            LOG.log(Level.WARNING, "chronic condition count altersgruppe for patient insurance number " + patientennummer + " is suspicious: " + val);
        }
        return val;
    }

    private double toReferenceRescaledWeight(final String pReferencedRescaledWeight) {
        if (pReferencedRescaledWeight == null || pReferencedRescaledWeight.trim().isEmpty()) {
            LOG.log(Level.WARNING, "Empty floating value for referenced rescaled weight for patient insurance number " + patientennummer + " found, use '0.0' as default");
            return 0.0d;
        }
        final Double val = toDouble(pReferencedRescaledWeight);
        if (val == null) {
            throw new IllegalArgumentException("This referenced rescaled weight for patient insurance number " + patientennummer + " is not a valid float: " + pReferencedRescaledWeight);
        }
        if (val < 0.0d || val > 5.0d) {
            LOG.log(Level.WARNING, "referenced rescale weight for patient insurance number " + patientennummer + " is suspicious: " + val);
        }
        return val;
    }

    private int toResourceUtilizationBandPatient(final String pResourceUtilizationBand) {
        if (pResourceUtilizationBand == null || pResourceUtilizationBand.trim().isEmpty()) {
            LOG.log(Level.WARNING, "Empty numeric value for resource utilization band patient for patient insurance number " + patientennummer + " found, use '0' as default");
            return 0;
        }
        final Integer val = toInt(pResourceUtilizationBand);
        if (val == null) {
            throw new IllegalArgumentException("This resource utilization band patient for patient insurance number " + patientennummer + " is not a valid numeric: " + pResourceUtilizationBand);
        }
        if (val < 0 || val > 5) {
            LOG.log(Level.WARNING, "resource utilization band patient for patient insurance number " + patientennummer + " is suspicious: " + val);
        }
        return val;
    }

    private double toResourceUtilizationBandAltersgruppe(final String pResourceUtilizationAltersgruppe) {
        if (pResourceUtilizationAltersgruppe == null || pResourceUtilizationAltersgruppe.trim().isEmpty()) {
            LOG.log(Level.WARNING, "Empty floating value for resource utilization band altersgruppe for patient insurance number " + patientennummer + " found, use '0.0' as default");
            return 0.0d;
        }
        final Double val = toDouble(pResourceUtilizationAltersgruppe);
        if (val == null) {
            throw new IllegalArgumentException("This resource utilization band altersgruppe for patient insurance number " + patientennummer + " is not a valid floating value: " + pResourceUtilizationAltersgruppe);
        }
        if (val < 0 || val > 5) {
            LOG.log(Level.WARNING, "resource utilization band patient for altersgruppe insurance number " + patientennummer + " is suspicious: " + val);
        }
        return val;
    }

    private boolean toFrailtyFlag(final String pFrailtyFlag) {
        if (pFrailtyFlag == null || pFrailtyFlag.trim().isEmpty()) {
            LOG.log(Level.WARNING, "Empty boolean value for frailty flag for patient insurance number " + patientennummer + " found, use 'false' as default");
            return false;
        }
        final Boolean val = toBool(pFrailtyFlag);
        if (val == null) {
            throw new IllegalArgumentException("This frailty flag for patient insurance number " + patientennummer + " is not a valid boolean: " + pFrailtyFlag);
        }
        return val;
    }

    public String getSliderLabel() {
        String l = jahr + "/" + periode;
        if (bezeichnung != null && !bezeichnung.trim().isEmpty()) {
            l += ": " + bezeichnung;
        }
        return l;
    }

//    private String[] splitIcds(final List<String> icd_code) {
//        if (icd_code == null) {
//            LOG.log(Level.SEVERE, "List of diagnosis for patient insurance number " + patientennummer + " is null!");          
//            return new String[0];
//        }
//        if (icd_code.isEmpty()) {
//            LOG.log(Level.SEVERE, "List of diagnosis for patient insurance number " + patientennummer + " is empty!");                      
//            return new String[0];
//        }
//        StringBuilder sb = new StringBuilder();
//        for(String icd: icd_code) {
//            if (icd == null) {
//                LOG.log(Level.SEVERE, "Empty diagnosis found for patient insurance number " + patientennummer);                      
//                continue;
//            }
//            icd = icd.trim().toUpperCase();
//            if (icd.isEmpty()) {
//                LOG.log(Level.SEVERE, "Empty diagnosis found for patient insurance number " + patientennummer);                      
//                continue;
//            }
//            if (sb.length() > 0) {
//                sb.append(" ");
//            }
//            String icdTmp = icd;
//            while(true) {
//                icd = icdTmp.replace("  ", " ");
//                if (icd.equalsIgnoreCase(icdTmp)) {
//                    break;
//                }
//                icdTmp = icd;
//            }
//            sb.append(icd);
//        }
//        return sb.toString().split(" ");
//    }
}

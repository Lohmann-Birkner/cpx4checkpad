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
package de.lb.cpx.service.ejb;

import de.lb.cpx.grouper.model.transfer.TransferBaseRate;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commonDB.dao.CBaserateDao;
import de.lb.cpx.server.commonDB.model.CBaserate;
import de.lb.cpx.service.information.BaserateTypeEn;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;

/**
 *
 * @author niemeier
 */
@ApplicationScoped
@Local
public class BaserateStore {

    public static final boolean ENABLE_BASERATE_STORE = true;
    private static final Logger LOG = Logger.getLogger(BaserateStore.class.getName());

    @EJB
    private CBaserateDao baseRate;

    public class Entry {

        public final BaserateTypeEn baserateType;
        public final String hosIdent;
//        public final Date admissionDate;
//        public final Date dischargeDate;
        public final String countryCode;
        //final public Date date = new Date();

        public Entry(final BaserateTypeEn pBaserateType, final String pHosIdent, //final Date pAdmissionDate, final Date pDischargeDate, 
                final String pCountryCode) {
            baserateType = pBaserateType;
            hosIdent = pHosIdent;
//            admissionDate = pAdmissionDate;
//            dischargeDate = pDischargeDate;
            countryCode = pCountryCode;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + Objects.hashCode(this.baserateType);
            hash = 59 * hash + Objects.hashCode(this.hosIdent);
//            hash = 59 * hash + Objects.hashCode(this.admissionDate);
//            hash = 59 * hash + Objects.hashCode(this.dischargeDate);
            hash = 59 * hash + Objects.hashCode(this.countryCode);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Entry other = (Entry) obj;
            if (!baserateType.equals(other.baserateType)) {
                return false;
            }
            if (!Objects.equals(this.hosIdent, other.hosIdent)) {
                return false;
            }
            if (!Objects.equals(this.countryCode, other.countryCode)) {
                return false;
            }
//            if (!Objects.equals(this.admissionDate, other.admissionDate)) {
//                return false;
//            }
//            if (!Objects.equals(this.dischargeDate, other.dischargeDate)) {
//                return false;
//            }
            return true;
        }

//        public Date getDate() {
//            return date;
//        }
        private String getFeeKey2Type() {
            return baserateType.getFeeKey();
        }

    }

    private static final Map<Entry, List<CBaserate>> BASERATE_MAP = new HashMap<>();

    public List<TransferBaseRate> getBaserate(final TransferCase pRequestObject) {
        final Date admissionDate = pRequestObject.getDateOfAdmission() == null ? null : new Date(pRequestObject.getDateOfAdmission().getTime());
        // if dischargedate is null -> the case is not closed, it will not be grouped. For pepp, when we activete Fee - Module, has to be checked
        final Date dischargeDate = pRequestObject.getDateOfDischarge() == null ? null : new Date(pRequestObject.getDateOfDischarge().getTime());
        if (admissionDate == null || dischargeDate == null) {
            return new ArrayList<>();// is not possible, was checked by setting of case values
        }
//        final long startTime = System.currentTimeMillis();

        List<TransferBaseRate> trBaserates = new ArrayList<>();

        if (pRequestObject.getCaseType() == CaseTypeEn.PEPP.getId()) {

            List<CBaserate> baseRates = getBaserates4TypeAndIk(BaserateTypeEn.PEPP, pRequestObject.getIkz(), pRequestObject.getCountry());
            double brDefault = baseRate.getDefaultPeppBaserate(pRequestObject.getAdmissionReason12());
            trBaserates = findBaserates4Interval(BaserateTypeEn.PEPP, admissionDate, dischargeDate, trBaserates, baseRates, brDefault);
//            if (!baseRates.isEmpty()) {
//                for (int i = 0, n = baseRates.size(); i < n; i++) {
//
//                    CBaserate br = baseRates.get(i);
//                    if (br.getBaseValidTo() != null && br.getBaseValidFrom() != null) {
//                        if (admissionDate.before(br.getBaseValidFrom()) && i == 0) {
//                            // admission  date of case is before the first baserate
//                            if (!dischargeDate.after(br.getBaseValidFrom())) {
//                                // the whole case is for th first baserate
//                                TransferBaseRate trBR = new TransferBaseRate(BaserateTypeEn.PEPP, brDefault, admissionDate, dischargeDate);
//                                trBaserates.add(trBR);
//                                return trBaserates;
//
//                            } else {
//                                return findBaserates4Interval(BaserateTypeEn.PEPP, 0, br.getBaseValidFrom(), admissionDate,
//                                        dischargeDate, trBaserates,
//                                        baseRates, i);
//
//                            }
//                        }
//                        if (admissionDate.after(br.getBaseValidTo())) {
//                            continue;
//                        }
//                        if (!admissionDate.after(br.getBaseValidTo())) {
//                            if (!dischargeDate.after(br.getBaseValidTo())) {
//                                // the whole case is in one baserate period
//                                TransferBaseRate trBR = new TransferBaseRate(BaserateTypeEn.PEPP, br.getBaseFeeValue(), admissionDate, dischargeDate);
//                                trBaserates.add(trBR);
//                                return trBaserates;
//                            } else {
//                                // first period from admissionDate to br.getBaseValidTo()
//                                return findBaserates4Interval(BaserateTypeEn.PEPP, br.getBaseFeeValue(), br.getBaseValidTo(), admissionDate,
//                                        dischargeDate, trBaserates,
//                                        baseRates, i);
////                                TransferBaseRate trBR = new TransferBaseRate(BaserateTypeEn.PEPP, br.getBaseFeeValue(), admissionDate, br.getBaseValidTo());
////                                trBaserates.add(trBR);
////                                for (int j = i + 1; j < n; j++) {
////                                    br = baseRates.get(j);
////                                    if (br.getBaseValidTo() != null && br.getBaseValidFrom() != null) {
////                                        if (dischargeDate.after(br.getBaseValidTo())) {
////                                            trBR = new TransferBaseRate(BaserateTypeEn.PEPP, br.getBaseFeeValue(), br.getBaseValidFrom(), br.getBaseValidTo());
////                                            trBaserates.add(trBR);
////                                        } else {
////                                            trBR = new TransferBaseRate(BaserateTypeEn.PEPP, br.getBaseFeeValue(), br.getBaseValidFrom(), dischargeDate);
////                                            trBaserates.add(trBR);
////                                            return trBaserates;
////                                        }
////                                    }
////                                }
////
//                            }
//                        }
//                    }
//                }
//                if (trBaserates.isEmpty() || trBaserates.get(baseRates.size() - 1).getValidTo().before(dischargeDate)) {
//                    trBaserates.add(new TransferBaseRate(BaserateTypeEn.PEPP, brDefault, trBaserates.isEmpty() ? admissionDate : trBaserates.get(baseRates.size() - 1).getValidTo(), dischargeDate));
//                }
//
//            } else {
//                trBaserates.add(new TransferBaseRate(BaserateTypeEn.PEPP, brDefault, admissionDate, dischargeDate));
//            }
        } else if (pRequestObject.getCaseType() == CaseTypeEn.DRG.getId()) {
            List<CBaserate> baseRates = getBaserates4TypeAndIk(BaserateTypeEn.DRG, pRequestObject.getIkz(), pRequestObject.getCountry());
            double defaultBr = baseRate.getDefaultDrgBaserate(BaserateTypeEn.DRG, pRequestObject.getDateOfAdmission());
            trBaserates.add(findBaserate4AdmDate(BaserateTypeEn.DRG, pRequestObject, baseRates, defaultBr));
            trBaserates = findBaserates4Interval(BaserateTypeEn.DRG_CARE, 
                    admissionDate, dischargeDate, 
                    trBaserates, 
                    getBaserates4TypeAndIk(BaserateTypeEn.DRG_CARE, pRequestObject.getIkz(), pRequestObject.getCountry()),
                    baseRate.getDefaultDrgBaserate(BaserateTypeEn.DRG_CARE, pRequestObject.getDateOfAdmission()));

        } else {
            //NOTHING!
        }

        return trBaserates;
    }
    
     private List<TransferBaseRate> findBaserates4Interval(BaserateTypeEn pType,   
             Date admissionDate, Date dischargeDate,
            List<TransferBaseRate> trBaserates,
            List<CBaserate> baseRates,
            double brDefault
     ){

            if (!baseRates.isEmpty()) {
                for (int i = 0, n = baseRates.size(); i < n; i++) {

                    CBaserate br = baseRates.get(i);
                    if (br.getBaseValidTo() != null && br.getBaseValidFrom() != null) {
                        if (admissionDate.before(br.getBaseValidFrom()) && i == 0) {
                            // admission  date of case is before the first baserate
                            if (!dischargeDate.after(br.getBaseValidFrom())) {
                                // the whole case is for th first baserate
                                TransferBaseRate trBR = new TransferBaseRate(pType, brDefault, admissionDate, dischargeDate);
                                trBaserates.add(trBR);
                                return trBaserates;

                            } else {
                                return findBaserates4Interval(pType, 0, br.getBaseValidFrom(), admissionDate,
                                        dischargeDate, trBaserates,
                                        baseRates, i);

                            }
                        }
                        if (admissionDate.after(br.getBaseValidTo())) {
                            continue;
                        }
                        if (!admissionDate.after(br.getBaseValidTo())) {
                            if (!dischargeDate.after(br.getBaseValidTo())) {
                                // the whole case is in one baserate period
                                TransferBaseRate trBR = new TransferBaseRate(pType, br.getBaseFeeValue(), admissionDate, dischargeDate);
                                trBaserates.add(trBR);
                                return trBaserates;
                            } else {
                                // first period from admissionDate to br.getBaseValidTo()
                                return findBaserates4Interval(pType, br.getBaseFeeValue(), br.getBaseValidTo(), admissionDate,
                                        dischargeDate, trBaserates,
                                        baseRates, i);

                            }
                        }
                    }
                }
                if (trBaserates.isEmpty() || trBaserates.get(trBaserates.size() - 1).getValidTo().before(dischargeDate)) {
                    trBaserates.add(new TransferBaseRate(pType, brDefault, trBaserates.isEmpty() ? admissionDate : trBaserates.get(trBaserates.size() - 1).getValidTo(), dischargeDate));
                }
            } 
            if (trBaserates.isEmpty()){
                 trBaserates.add(new TransferBaseRate(pType, brDefault, admissionDate , dischargeDate));
            }
            return trBaserates;
     }

    private List<TransferBaseRate> findBaserates4Interval(BaserateTypeEn type, double firstValue,
            Date firstEndDate,
            Date admissionDate, Date dischargeDate,
            List<TransferBaseRate> trBaserates,
            List<CBaserate> baseRates,
            int index
    ) {
        // first period from admissionDate to br.getBaseValidTo()
        TransferBaseRate trBR = new TransferBaseRate(type, firstValue, admissionDate, firstEndDate);
        trBaserates.add(trBR);
        for (int j = index + 1; j < baseRates.size(); j++) {
            CBaserate br = baseRates.get(j);
            if (br.getBaseValidTo() != null && br.getBaseValidFrom() != null) {
                if (dischargeDate.after(br.getBaseValidTo())) {
                    trBR = new TransferBaseRate(type, br.getBaseFeeValue(), br.getBaseValidFrom(), br.getBaseValidTo());
                    trBaserates.add(trBR);
                } else {
                    trBR = new TransferBaseRate(type, br.getBaseFeeValue(), br.getBaseValidFrom(), dischargeDate);
                    trBaserates.add(trBR);
                    return trBaserates;
                }
            }
        }
       if(!trBaserates.isEmpty()){
            TransferBaseRate last = trBaserates.get(trBaserates.size() - 1);
            if(last.getValidTo().before(dischargeDate)){
                TransferBaseRate gap = new TransferBaseRate(type, 0, getNextDay(last.getValidTo()), dischargeDate);
                trBaserates.add(gap);
            }
       }
        return trBaserates;

    }
    
    /**
     * calculates next day for date
     **/
    private static Date getNextDay(@NotNull Date pDate){
        Calendar cal =  Calendar.getInstance();
        cal.setTime(pDate);
        return new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH) + 1
        ).getTime(); 
    }

    private TransferBaseRate findBaserate4AdmDate(BaserateTypeEn pType, TransferCase pRequestObject, List<CBaserate> baseRates, double defaultBr) {

//        List<CBaserate> baseRates = getBaserates4TypeAndIk(pType, pRequestObject.getIkz(), pRequestObject.getCountry());
//        double defaultBr = baseRate.getDefaultDrgBaserate(pType, pRequestObject.getDateOfAdmission());
        final Date admissionDate = pRequestObject.getDateOfAdmission() == null ? null : new Date(pRequestObject.getDateOfAdmission().getTime());
        if (admissionDate == null) {
            LOG.log(Level.WARNING, "admission date is null!", admissionDate);
        } else {
//            if (!baseRates.isEmpty()) {
            for (CBaserate br : baseRates) {
                if (br.getBaseValidFrom() != null && br.getBaseValidTo() != null) {
                    if (!admissionDate.before(br.getBaseValidFrom()) && !admissionDate.after(br.getBaseValidTo())) {
                        return new TransferBaseRate(pType, br.getBaseFeeValue(), admissionDate, pRequestObject.getDateOfDischarge());
                    }
                }
            }
//            }
        }
        return new TransferBaseRate(pType, defaultBr, admissionDate, pRequestObject.getDateOfDischarge());
    }

    private List<CBaserate> getBaserates4TypeAndIk(BaserateTypeEn pBaserateTypeEn, String pIkz, String pCountry) {
        Entry entry = new Entry(pBaserateTypeEn, pIkz, pCountry);
        List<CBaserate> baseRates = BASERATE_MAP.get(entry);

        if (baseRates == null) {
            //Create request for common database
            baseRates = baseRate.findBaserate4CaseType(entry.getFeeKey2Type(), entry.hosIdent, entry.countryCode);
            if (baseRates == null) {
                baseRates = new ArrayList<>();
            }
            BASERATE_MAP.put(entry, baseRates);
        }
        return baseRates;
    }

}

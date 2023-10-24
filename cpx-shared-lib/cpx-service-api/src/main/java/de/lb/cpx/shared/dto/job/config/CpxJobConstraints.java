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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.job.config;

import de.lb.cpx.file.reader.CpxFileReader;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class CpxJobConstraints implements Serializable {

    private static final Logger LOG = Logger.getLogger(CpxJobConstraints.class.getName());

    private static final long serialVersionUID = 1L;

    private final Date mAdmissionDateFrom;
    private final Date mAdmissionDateTo;
    private final Date mDischargeDateFrom;
    private final Date mDischargeDateTo;
    private final Set<AdmissionCauseEn> mAdmissionCauses;
    private final Set<AdmissionReasonEn> mAdmissionReasons;
    private final Set<IcdcTypeEn> mIcdTypes;
    private final Set<String> mCaseNumbers;

    public CpxJobConstraints() {
        this(null, null, null, null, null, null, null, (List<String>) null);
    }

    public CpxJobConstraints(final String pCaseNumbers) throws IOException {
        this(null, null, null, null, null, null, null, split(pCaseNumbers));
    }

    public CpxJobConstraints(final File pCaseNumbers) throws IOException {
        this(null, null, null, null, null, null, null, readFile(pCaseNumbers));
    }

    public CpxJobConstraints(final String[] pCaseNumbers) {
        this(null, null, null, null, null, null, null, pCaseNumbers == null ? null : Arrays.asList(pCaseNumbers));
    }

    public CpxJobConstraints(final Collection<String> pCaseNumbers) {
        this(null, null, null, null, null, null, null, pCaseNumbers);
    }

    public CpxJobConstraints(final Date pAdmissionDateFrom, final Date pAdmissionDateTo, final Date pDischargeDateFrom, final Date pDischargeDateTo, final Collection<AdmissionCauseEn> pAdmissionCauses, final Collection<AdmissionReasonEn> pAdmissionReasons, final Collection<IcdcTypeEn> pIcdTypes) {
        this(pAdmissionDateFrom, pAdmissionDateTo, pDischargeDateFrom, pDischargeDateTo, pAdmissionCauses, pAdmissionReasons, pIcdTypes, (String[]) null);
    }

    public CpxJobConstraints(final Date pAdmissionDateFrom, final Date pAdmissionDateTo, final Date pDischargeDateFrom, final Date pDischargeDateTo, final Collection<AdmissionCauseEn> pAdmissionCauses, final Collection<AdmissionReasonEn> pAdmissionReasons, final Collection<IcdcTypeEn> pIcdTypes, final String pCaseNumbers) throws IOException {
        this(pAdmissionDateFrom, pAdmissionDateTo, pDischargeDateFrom, pDischargeDateTo, pAdmissionCauses, pAdmissionReasons, pIcdTypes, split(pCaseNumbers));
    }

    public CpxJobConstraints(final Date pAdmissionDateFrom, final Date pAdmissionDateTo, final Date pDischargeDateFrom, final Date pDischargeDateTo, final Collection<AdmissionCauseEn> pAdmissionCauses, final Collection<AdmissionReasonEn> pAdmissionReasons, final Collection<IcdcTypeEn> pIcdTypes, final File pCaseNumbers) throws IOException {
        this(pAdmissionDateFrom, pAdmissionDateTo, pDischargeDateFrom, pDischargeDateTo, pAdmissionCauses, pAdmissionReasons, pIcdTypes, readFile(pCaseNumbers));
    }

    public CpxJobConstraints(final Date pAdmissionDateFrom, final Date pAdmissionDateTo, final Date pDischargeDateFrom, final Date pDischargeDateTo, final Collection<AdmissionCauseEn> pAdmissionCauses, final Collection<AdmissionReasonEn> pAdmissionReasons, final Collection<IcdcTypeEn> pIcdTypes, final String[] pCaseNumbers) {
        this(pAdmissionDateFrom, pAdmissionDateTo, pDischargeDateFrom, pDischargeDateTo, pAdmissionCauses, pAdmissionReasons, pIcdTypes, pCaseNumbers == null ? null : Arrays.asList(pCaseNumbers));
    }

    public CpxJobConstraints(final Date pAdmissionDateFrom, final Date pAdmissionDateTo, final Date pDischargeDateFrom, final Date pDischargeDateTo, final Collection<AdmissionCauseEn> pAdmissionCauses, final Collection<AdmissionReasonEn> pAdmissionReasons, final Collection<IcdcTypeEn> pIcdTypes, final Collection<String> pCaseNumbers) {
        this.mAdmissionDateFrom = removeTime(pAdmissionDateFrom);
        this.mAdmissionDateTo = removeTime(pAdmissionDateTo);
        this.mDischargeDateFrom = removeTime(pDischargeDateFrom);
        this.mDischargeDateTo = removeTime(pDischargeDateTo);
        this.mAdmissionCauses = Collections.unmodifiableSet(toFilteredSet(pAdmissionCauses));
        this.mAdmissionReasons = Collections.unmodifiableSet(toFilteredSet(pAdmissionReasons));
        this.mIcdTypes = Collections.unmodifiableSet(toFilteredSet(pIcdTypes));
        this.mCaseNumbers = Collections.unmodifiableSet(toFilteredStringSet(pCaseNumbers));
    }

    private static Date removeTime(final Date pDate) {
        if (pDate == null) {
            return pDate;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private static Date addDays(final Date pDate, final int pDays) {
        if (pDate == null) {
            return pDate;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        cal.add(Calendar.DATE, pDays);
        return cal.getTime();
    }

    private static Set<String> readFile(final File pFile) throws IOException {
        if (pFile == null) {
            return new LinkedHashSet<>();
        }
        LOG.log(Level.INFO, "Read case numbers from list: " + pFile.getAbsolutePath());
        if (!pFile.exists() || !pFile.isFile()) {
            throw new IllegalArgumentException("Passed case numbers list does not exist: " + pFile.getAbsolutePath());
        }
        if (!pFile.canRead()) {
            throw new IllegalArgumentException("Passed case numbers list cannot be read: " + pFile.getAbsolutePath());
        }
        List<String> tmp = new ArrayList<>();
        int countLines = 0;
        try ( CpxFileReader reader = new CpxFileReader(pFile)) {
            while (!reader.eof()) {
                countLines++;
                String line = reader.readLine();
                line = line == null ? "" : line.trim();
                if (!line.isEmpty()) {
                    tmp.add(line);
                }
            }
        }
        LOG.log(Level.INFO, "Found " + countLines + " lines in file " + pFile.getAbsolutePath());
        return toFilteredSet(tmp);
    }

    /**
     * split comma and line breaks
     *
     * @param pCaseNumbers string with case numbers (comma or line separated)
     * @return array of case numbers
     */
    private static Set<String> split(final String pCaseNumbers) throws IOException {
        final String caseNumbers = pCaseNumbers == null ? "" : pCaseNumbers.trim();
        if (caseNumbers.isEmpty()) {
            return new LinkedHashSet<>();
        }
        final File file = new File(caseNumbers);
        if ((file.exists() && file.isFile())
                || caseNumbers.toLowerCase().endsWith(".txt")
                || caseNumbers.toLowerCase().endsWith(".csv")) {
            return readFile(file);
        } else {
            String[] tmp = caseNumbers.trim().split("[\\n,]");
            //String[] tmp = pCaseNumbers.replace(",", "\n").split("\\n");
            return toFilteredSet(Arrays.asList(tmp));
        }
    }

    private static <T> Set<T> toFilteredSet(final Collection<T> pCollection) {
        if (pCollection == null || pCollection.isEmpty()) {
            return new LinkedHashSet<>();
        }
        final List<T> list = new ArrayList<>();
        for (final T value : new ArrayList<>(pCollection)) {
            if (value == null) {
                continue;
            }
            list.add(value);
        }
        final Set<T> result = new LinkedHashSet<>(list);
        return result;
        //String[] tmp = new String[list.size()];
        //list.toArray(tmp);
        //return tmp;
    }

    private static Set<String> toFilteredStringSet(final Collection<String> pCaseNumbers) {
        Set<String> collection = toFilteredSet(pCaseNumbers);
        List<String> list = new ArrayList<>();
        Iterator<String> it = collection.iterator();
        while (it.hasNext()) {
            String value = it.next();
            if (value == null || value.trim().isEmpty()) {
                it.remove();
            } else {
                list.add(value.trim());
            }
        }
        list.sort((o1, o2) -> {
            return o1.compareTo(o2);
        });
        final Set<String> result = new LinkedHashSet<>(list);
        return result;
        //String[] tmp = new String[list.size()];
        //list.toArray(tmp);
        //return tmp;
    }

    /**
     * @return the mAdmissionDateFrom
     */
    public Date getAdmissionDateFrom() {
        return mAdmissionDateFrom == null ? null : new Date(mAdmissionDateFrom.getTime());
    }

    /**
     * @return the mAdmissionDateTo
     */
    public Date getAdmissionDateTo() {
        return mAdmissionDateTo == null ? null : new Date(mAdmissionDateTo.getTime());
    }

    /**
     * @param pDays add number of days
     * @return the mAdmissionDateTo
     */
    public Date getAdmissionDateTo(final int pDays) {
        return addDays(mAdmissionDateTo, pDays);
    }

    /**
     * @return the mDischargeDateFrom
     */
    public Date getDischargeDateFrom() {
        return mDischargeDateFrom == null ? null : new Date(mDischargeDateFrom.getTime());
    }

    /**
     * @param pDays add number of days
     * @return the mDischargeDateTo
     */
    public Date getDischargeDateTo(final int pDays) {
        return addDays(mDischargeDateTo, pDays);
    }

    /**
     * @return the mDischargeDateTo
     */
    public Date getDischargeDateTo() {
        return mDischargeDateTo == null ? null : new Date(mDischargeDateTo.getTime());
    }

    /**
     * @return the mCaseNumbers
     */
    public Set<String> getCaseNumbers() {
        return mCaseNumbers;
    }

    public String[] getCaseNumberValues() {
        final String[] result = new String[mCaseNumbers.size()];
        int i = 0;
        final Iterator<String> it = mCaseNumbers.iterator();
        while (it.hasNext()) {
            String elem = it.next();
            result[i] = elem;
            i++;
        }
        return result;
    }

    /**
     * has case numbers?
     *
     * @return !mCaseNumbers.isEmpty()?
     */
    public boolean hasCaseNumbers() {
        return !mCaseNumbers.isEmpty();
    }

//    /**
//     * @return the mCaseNumbers
//     */
//    public String[] getCaseNumbers() {
//        final String[] tmp = new String[mCaseNumbers.length];
//        System.arraycopy(mCaseNumbers, 0, tmp, 0, mCaseNumbers.length);
//        return tmp;
//    }
//    public boolean containsCaseNumber(final String pCaseNumber) {
//        final String caseNumber = pCaseNumber == null ? "" : pCaseNumber.trim();
//        for(final String tmp: mCaseNumbers) {
//            if (caseNumber.equalsIgnoreCase(tmp)) {
//                return true;
//            }
//        }
//        return false;
//    }
//    public static void main(String[] args) {
//        List<String> bla = new ArrayList<>();
//        bla.add("1");
//        bla.add("3");
//        bla.add("5");
//        bla.add("2");
//        bla.add("");
//        bla.add(null);
//        bla.add("10");
//        bla.add("0");
//        bla.add("40");
//        CpxJobConstraints obj = new CpxJobConstraints(null, null, null, null, bla);
//        for(String value: obj.getCaseNumbers()) {
//            System.out.println(value);
//        }
//    }
//    public static void main(String[] args) {
//        try {
//            CpxJobConstraints obj = new CpxJobConstraints(null, null, null, null, null, null, null, (String) "E:\\case_numbers.txt");
//            System.out.println(obj.getCaseNumbers().isEmpty());
//        } catch (IOException ex) {
//            Logger.getLogger(CpxJobConstraints.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    /**
     * @return the mAdmissionCauses
     */
    public Set<AdmissionCauseEn> getAdmissionCauses() {
        return mAdmissionCauses;
    }

    public String[] getAdmissionCauseValues() {
        final String[] result = new String[mAdmissionCauses.size()];
        int i = 0;
        final Iterator<AdmissionCauseEn> it = mAdmissionCauses.iterator();
        while (it.hasNext()) {
            AdmissionCauseEn elem = it.next();
            result[i] = elem.getViewId();
            i++;
        }
        return result;
    }

    /**
     * has admission causes?
     *
     * @return !mAdmissionCauses.isEmpty()?
     */
    public boolean hasAdmissionCauses() {
        return !mAdmissionCauses.isEmpty();
    }

    /**
     * @return the mAdmissionReasons
     */
    public Set<AdmissionReasonEn> getAdmissionReasons() {
        return mAdmissionReasons;
    }

    public String[] getAdmissionReasonValues() {
        final String[] result = new String[mAdmissionReasons.size()];
        int i = 0;
        final Iterator<AdmissionReasonEn> it = mAdmissionReasons.iterator();
        while (it.hasNext()) {
            AdmissionReasonEn elem = it.next();
            result[i] = elem.getViewId();
            i++;
        }
        return result;
    }

    /**
     * has admission reasons?
     *
     * @return !mAdmissionReasons.isEmpty()?
     */
    public boolean hasAdmissionReasons() {
        return !mAdmissionReasons.isEmpty();
    }

    /**
     * @return the mIcdTypes
     */
    public Set<IcdcTypeEn> getIcdTypes() {
        return mIcdTypes;
    }

    public String[] getIcdTypeValues() {
        final String[] result = new String[mIcdTypes.size()];
        int i = 0;
        final Iterator<IcdcTypeEn> it = mIcdTypes.iterator();
        while (it.hasNext()) {
            IcdcTypeEn elem = it.next();
            result[i] = elem.getViewId();
            i++;
        }
        return result;
    }

    /**
     * has icd types?
     *
     * @return !mIcdTypes.isEmpty()?
     */
    public boolean hasIcdTypes() {
        return !mIcdTypes.isEmpty();
    }

}

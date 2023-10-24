/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author gerschmann
 */
public class TransferMergePatient {

    private static final Logger LOG = Logger.getLogger(TransferMergePatient.class.getName());

 private long  mPatientId = -1;
 private String mHospitalIdent = null;
 private List<TransferMergeCandidate> mCases2merge = new ArrayList<>();
 private List<RmcWiederaufnahmeIF> mRmcWiederaufnahmeList = new ArrayList<>();

    public long getPatientId() {
        return mPatientId;
    }

    public void setPatientId(long mPatientId) {
        this.mPatientId = mPatientId;
    }

    public List<TransferMergeCandidate> getCases2merge() {
        return mCases2merge;
    }

    public String getHospitalIdent() {
        return mHospitalIdent;
    }

    public void setHospitalIdent(String mHospitalIdent) {
        this.mHospitalIdent = mHospitalIdent;
    }

    public void setmCases2merge(List<TransferMergeCandidate> mCases2merge) {
        this.mCases2merge = mCases2merge;
    }

    public void addCandidate(TransferMergeCandidate newCandidate) {
        if(newCandidate != null && newCandidate.getRmcCase() != null && !mCases2merge.contains(newCandidate)){
            mCases2merge.add(newCandidate);
            mRmcWiederaufnahmeList.add(newCandidate.getRmcCase());
        }
    }
    
    public ArrayList<RmcWiederaufnahmeIF> getRmcWiederaufnahmeList(){
        List<RmcWiederaufnahmeIF> ret = mRmcWiederaufnahmeList.stream().sorted(Comparator.comparing(new Function<RmcWiederaufnahmeIF, Date>() {
            @Override
            public Date apply(RmcWiederaufnahmeIF wd) {
                return wd.getAufnahmedatum();
            }
        })).collect(Collectors.toList());
        return new ArrayList<>(ret);
    }
    
    public ArrayList<TransferMergeCandidate> getCases2mergeSortedWithMergeId(){
        List<TransferMergeCandidate> ret = mCases2merge.stream().sorted(Comparator.comparing(new Function<TransferMergeCandidate, Integer>() {
            @Override
            public Integer apply(TransferMergeCandidate candidate) {
                return candidate.getMergeId();
            }
        })).collect(Collectors.toList());
        return new ArrayList<>(ret);
    }

    public void setRmcWiederaufnahmeList(List<RmcWiederaufnahmeIF> mRmcWiederaufnahmeList) {
        this.mRmcWiederaufnahmeList = mRmcWiederaufnahmeList;
    }

    public void removeCaseCandidate(RmcWiederaufnahmeIF candidate) {
       for(TransferMergeCandidate oneCase: mCases2merge){
           if(oneCase.getRmcCase().getIkNr().equals(candidate.getIkNr()) && oneCase.getRmcCase().getFallNr().equals(candidate.getFallNr())){
               mCases2merge.remove(oneCase);
               return;
           }
       }
    }

}

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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.enums.DetailsFilterEn;
import de.lb.cpx.shared.lang.Lang;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class BatchGroupParameter implements Serializable {

//    public enum DetailsFilterEn {
//        ACTUAL_LOCAL, ACTUAL_BOTH, ALL;
//
//        @Override
//        public String toString() {
//            if (ACTUAL_LOCAL.equals(this)) {
//                return Lang.getMenuBatchGroupingDetailsFilterActualLocal(); //"lokale aktuelle Falldetails";
//            }
//            if (ACTUAL_BOTH.equals(this)) {
//                return Lang.getMenuBatchGroupingDetailsFilterActualBoth(); //"aktuelle Falldetails (inkl. KIS)";
//            }
//            if (ALL.equals(this)) {
//                return Lang.getMenuBatchGroupingDetailsFilterAll(); //"alle Falldetails"; bei diese Einstellung können  nur die Zusatzentgelte ermittelt werden, die Simulation, Medikamente, Labor und Regelanwendung wird ausgeschaltet
//            }
//            LOG.log(Level.WARNING, "unknown details filter: {0}", this);
//            return "----";
//        }
//    }

    private static final Logger LOG = Logger.getLogger(BatchGroupParameter.class.getName());

    private static final long serialVersionUID = 1L;
    //private static final Logger LOG = Logger.getLogger(BatchGroupParameter.class.getName());

    private boolean mDoSimulate = true;// simulate DRG/PEPP
    private boolean mDoSupplementaryFees = true; // supplementary Fees
    private boolean mDoMedAndRemedies = false; // use Medicines and Remidies
    private boolean mDoCareData = false; // use CareData
    private boolean mDoLabor = false;
    private boolean mDoDepartmentGrouping = false;
    private boolean mDoStationsgrouping = false;
    private boolean mDoRules = true;
    private boolean mDoRulesSimulate = true;
    private boolean mDo4actualRoleOnly = false;
    private boolean mDoHistoryCases = false;
//    private int mModelId = GDRGModel.AUTOMATIC.getGDRGVersion();
    private ArrayList<Long> mRuleIds = null; // default null - use all rules
    private ArrayList<Long> mCaseIds = null; // default null - group all cases
    private ArrayList<Long> mRoleIds = null; // default null - group for all roles
//    private boolean mDoInclHis = true;
    private DetailsFilterEn mDetailsFilter = DetailsFilterEn.ACTUAL_LOCAL;
    private GDRGModel model = GDRGModel.AUTOMATIC;
    private int mQueueSize = toQueueSize(null);
    private int mBlockSize = toBlockSize(null);
    private int mThreadCount = 1;//toThreadCount(null);
    private boolean mDisableWriter = false;
    private Date mAdmissionDateFrom = null;
    private Date mAdmissionDateUntil = null;
    private Date mDischargeDateFrom = null;
    private Date mDischargeDateUntil = null;
    private boolean grouped = false;

    public GDRGModel getModel() {
        return model;
    }

    public void setModel(GDRGModel model) {
        this.model = model == null ? GDRGModel.AUTOMATIC : model;
//        setModelId(model.getGDRGVersion());
    }

    public boolean isDoSimulate() {
        return mDoSimulate;// && !DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoSimulate(boolean m_doSimulate) {
        this.mDoSimulate = m_doSimulate;
    }

    public boolean isDoSupplementaryFees() {
        return mDoSupplementaryFees;
    }

    public void setDoSupplementaryFees(boolean doSupplementaryFees) {
        this.mDoSupplementaryFees = doSupplementaryFees;
    }

    public boolean isDoMedAndRemedies() {
        return mDoMedAndRemedies && !DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoMedAndRemedies(boolean m_doMedAndRemedies) {
        this.mDoMedAndRemedies = m_doMedAndRemedies;
    }

    public boolean isDoCareData() {
        return mDoCareData && !DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoCareData(boolean m_doCareData) {
        this.mDoCareData = m_doCareData;
    }

    public boolean isDoLabor() {
        return mDoLabor && !DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoLabor(boolean m_doLabor) {
        this.mDoLabor = m_doLabor;
    }

    public boolean isDoDepartmentGrouping() {
        return mDoDepartmentGrouping &&!DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoDepartmentGrouping(boolean m_doDepartmentGrouping) {
        this.mDoDepartmentGrouping = m_doDepartmentGrouping;
    }

    public boolean isDoStationsgrouping() {
        return mDoStationsgrouping  &&!DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoStationsgrouping(boolean m_doStationsgrouping) {
        this.mDoStationsgrouping = m_doStationsgrouping;
    }

    public boolean isDoRules() {
        return mDoRules; // &&!DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoRules(boolean m_doRules) {
        this.mDoRules = m_doRules;
    }

    public boolean isDoRulesSimulate() {
        return mDoRulesSimulate;// &&!DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoRulesSimulate(boolean m_doRulesSimulate) {
        this.mDoRulesSimulate = m_doRulesSimulate;
    }

    public boolean isDoHistoryCases() {
        return mDoHistoryCases  &&!DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public void setDoHistoryCases(boolean mDoHistoryCases) {
        this.mDoHistoryCases = mDoHistoryCases;
    }

    
    public List<Long> getRuleIds() {
        return mRuleIds == null ? null : new ArrayList<>(mRuleIds);
    }

    public void setRuleIds(List<Long> pRuleIds) {
        this.mRuleIds = pRuleIds == null ? null : new ArrayList<>(pRuleIds);
    }

    public List<Long> getCaseIds() {
        return mCaseIds == null ? null : new ArrayList<>(mCaseIds);
    }

    public void setCaseIds(List<Long> pCaseIds) {
        this.mCaseIds = pCaseIds == null ? null : new ArrayList<>(pCaseIds);
    }

    public int getModelId() {
        //return mModelId;
        return model.getGDRGVersion();
    }

//    public void setModelId(int modelId) {
//        this.mModelId = modelId;
//    }
    public List<Long> getRoleIds() {
        return mRoleIds == null ? null : new ArrayList<>(mRoleIds);
    }

    public void setRoleIds(List<Long> roleIds) {
        this.mRoleIds = roleIds == null ? null : new ArrayList<>(roleIds);
    }

    public void addRoleId(long id) {
        if (mRoleIds == null) {
            mRoleIds = new ArrayList<>();
        }
        mRoleIds.add(id);
    }

    public boolean isDo4actualRoleOnly() {
        return mDo4actualRoleOnly;
    }

    public void setDo4actualRoleOnly(boolean do4actualRoleOnly) {
        this.mDo4actualRoleOnly = do4actualRoleOnly;
    }

    public boolean doInclNonActual() {
        return DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public boolean doInclHis() {
        return DetailsFilterEn.ACTUAL_BOTH.equals(this.getDetailsFilter())
                || DetailsFilterEn.ALL.equals(this.getDetailsFilter());
    }

    public DetailsFilterEn getDetailsFilter() {
        return mDetailsFilter;
    }

    public void setDetailsFilter(final DetailsFilterEn pDetailsFilter) {
        this.mDetailsFilter = pDetailsFilter == null ? DetailsFilterEn.ACTUAL_LOCAL : pDetailsFilter;
    }

//    public boolean doInclHis() {
//        return mDoInclHis;
//    }
//
//    public void setDoInclHis(boolean doInclHis) {
//        this.mDoInclHis = doInclHis;
//    }
    public void setRoleIdsString(String roleIds) {
        if (roleIds != null && roleIds.length() > 0) {
            String[] parts = roleIds.split(",");
            for (String part : parts) {
                if (part == null || part.isEmpty()) {
                    continue;
                }
                try {
                    long l = Long.parseLong(part);
                    addRoleId(l);
                } catch (NumberFormatException e) {
                    Logger.getLogger(BatchGroupParameter.class.getName()).log(Level.INFO, "error on parsing roleid " + part, e);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "BatchGroupParameter { "
                + "doSimulate = " + mDoSimulate + ", "
                + "doSupplementaryFees = " + mDoSupplementaryFees + ", "
                + "doMedAndRemedies = " + mDoMedAndRemedies + ", "
                + "doCareData = " + mDoCareData + ", "
                + "doLabor = " + mDoLabor + ", "
                + "doDepartmentGrouping = " + mDoDepartmentGrouping + ", "
                + "doStationsgrouping = " + mDoStationsgrouping + ", "
                + "doRules = " + mDoRules + ", "
                + "doRulesSimulate = " + mDoRulesSimulate + ", "
                + "do4actualRoleOnly = " + mDo4actualRoleOnly + ", "
                + "mDoHistoryCases = " + mDoHistoryCases + ", "
                + "detailsFilter = " + mDetailsFilter + ", "
                //+ "modelId = " + (model == null ? "NULL" : model.name()) + ", "
                + "model = " + (model == null ? "NULL" : model.name()) + ", "
                + "queueSize = " + mQueueSize + ", "
                + "blockSize = " + mBlockSize + ", "
                + "threadCount = " + mThreadCount + ", "
                + "disableWriter = " + mDisableWriter + ", "
                + "ruleIds (count) = " + (mRuleIds == null ? "NULL" : mRuleIds.size()) + ", "
                + "roleIds (count) = " + (mRoleIds == null ? "NULL" : mRoleIds.size())
                + " }";
    }

    public void setQueueSize(final int pQueueSize) {
        this.mQueueSize = pQueueSize;
    }

    public void setBlockSize(final int pBlockSize) {
        this.mBlockSize = pBlockSize;
    }

    public void setThreadCount(final int pThreadCount) {
//        this.mThreadCount = pThreadCount;
    }

    public void setDisableWriter(final boolean pDisableWriter) {
        this.mDisableWriter = pDisableWriter;
    }

    public int getQueueSize() {
        return mQueueSize;
    }

    public int getBlockSize() {
        return mBlockSize;
    }

    public int getThreadCount() {
        return mThreadCount;
    }

    public boolean isDisableWriter() {
        return mDisableWriter;
    }

    public int getNestedQueueSize() {
        int queueSize = getQueueSize() / getBlockSize();
        if (queueSize == 0) {
            return 1;
        }
        return queueSize;
    }

    /**
     * @return the m_admissionDateFrom
     */
    public Date getAdmissionDateFrom() {
        return mAdmissionDateFrom == null ? null : new Date(mAdmissionDateFrom.getTime());
    }

    /**
     * @return the m_admissionDateUntil
     */
    public Date getAdmissionDateUntil() {
        return mAdmissionDateUntil == null ? null : new Date(mAdmissionDateUntil.getTime());
    }

    /**
     * @return the m_dischargeDateFrom
     */
    public Date getDischargeDateFrom() {
        return mDischargeDateFrom == null ? null : new Date(mDischargeDateFrom.getTime());
    }

    /**
     * @return the m_dischargeDateUntil
     */
    public Date getDischargeDateUntil() {
        return mDischargeDateUntil == null ? null : new Date(mDischargeDateUntil.getTime());
    }

    /**
     * @param pAdmissionDateFrom the m_admissionDateFrom to set
     */
    public void setAdmissionDateFrom(Date pAdmissionDateFrom) {
        this.mAdmissionDateFrom = pAdmissionDateFrom == null ? null : new Date(pAdmissionDateFrom.getTime());
    }

    /**
     * @param pAdmissionDateUntil the m_admissionDateUntil to set
     */
    public void setAdmissionDateUntil(Date pAdmissionDateUntil) {
        this.mAdmissionDateUntil = pAdmissionDateUntil == null ? null : new Date(pAdmissionDateUntil.getTime());
    }

    /**
     * @param pDischargeDateFrom the m_dischargeDateFrom to set
     */
    public void setDischargeDateFrom(Date pDischargeDateFrom) {
        this.mDischargeDateFrom = pDischargeDateFrom == null ? null : new Date(pDischargeDateFrom.getTime());
    }

    /**
     * @param pDischargeDateUntil the m_dischargeDateUntil to set
     */
    public void setDischargeDateUntil(Date pDischargeDateUntil) {
        this.mDischargeDateUntil = pDischargeDateUntil == null ? null : new Date(pDischargeDateUntil.getTime());
    }

    /**
     * @return the grouped
     */
    public boolean isGrouped() {
        return grouped;
    }

    /**
     * @param grouped the grouped to set
     */
    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public static int toQueueSize(final String pQueueSize) {
        final int queueSize;
        if (pQueueSize == null || pQueueSize.trim().isEmpty()) {
            LOG.log(Level.FINEST, "Queue Size parameter is empty, assume default");
            queueSize = 1500;
        } else {
            try {
                queueSize = Integer.parseInt(pQueueSize);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Queue Size is not a valid number: " + pQueueSize, ex);
            }
        }
        if (queueSize <= 0 || queueSize > 100000) {
            throw new IllegalArgumentException("Queue Size is invalid: " + pQueueSize);
        }
        return queueSize;
    }

    public static int toBlockSize(final String pBlockSize) {
        final int blockSize;
        if (pBlockSize == null || pBlockSize.trim().isEmpty()) {
            LOG.log(Level.FINEST, "Block Size parameter is empty, assume default");
            blockSize = 3;
        } else {
            try {
                blockSize = Integer.parseInt(pBlockSize);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Block Size is not a valid number: " + pBlockSize, ex);
            }
        }
        if (blockSize <= 0 || blockSize > 1000) {
            throw new IllegalArgumentException("Block Size is invalid: " + pBlockSize);
        }
        return blockSize;
    }

    public static int toThreadCount(final String pThreadCount) {
        final int threadCount;
        if (pThreadCount == null || pThreadCount.trim().isEmpty() || pThreadCount.trim().equalsIgnoreCase("AUTO")) {
            LOG.log(Level.FINEST, "Thread count parameter is empty, assume default");
            threadCount = Runtime.getRuntime().availableProcessors() + 2; //btw: bad performance with only 1 thread (approx. 30 cases/s);
        } else {
            try {
                threadCount = Integer.parseInt(pThreadCount);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Thread count is not a valid number: " + pThreadCount, ex);
            }
        }
        if (threadCount <= 0 || threadCount > 128) {
            throw new IllegalArgumentException("Thread Count is invalid: " + pThreadCount);
        }
        return threadCount;
    }

}

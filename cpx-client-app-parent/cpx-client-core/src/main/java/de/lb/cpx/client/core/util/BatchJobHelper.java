/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import de.lb.cpx.shared.lang.Lang;
import javax.batch.runtime.BatchStatus;

/**
 * Helper classs to handle batc hjob status
 *
 * @author wilde
 */
public class BatchJobHelper {

    /**
     * @param currentStatus batch status
     * @return localized batch job status
     */
    public static String getLocalizedStatus(BatchStatus currentStatus) {
        if (currentStatus == null) {
            return Lang.getJobStautsInitializing();
        }
        switch (currentStatus) {
            case STARTED:
                return Lang.getJobStatusStarted();
            case STARTING:
                return Lang.getJobStatusStarting();
            case COMPLETED:
                return Lang.getJobStatusCompleted();
            case STOPPING:
                return Lang.getJobStatusStopping();
            case STOPPED:
                return Lang.getJobStatusStopped();
            case FAILED:
                return Lang.getJobStatusFailed();
            default:
                return "";
        }
    }

    /**
     * @param currentStatus batch status
     * @return localized batch job status
     */
    public static String getMergeJobStatus(BatchStatus currentStatus) {
        if (currentStatus == null) {
            return Lang.getCaseMergingJobInitializing();
//            return Lang.getJob
        }
        switch (currentStatus) {
            case STARTED:
                return Lang.getCaseMergingJobStarted();
            case STARTING:
                return Lang.getCaseMergingJobStarting();
            case COMPLETED:
                return Lang.getCaseMergingJobCompleted();
            case STOPPING:
                return Lang.getCaseMergingJobStopping();
            case STOPPED:
                return Lang.getCaseMergingJobStopped();
            case FAILED:
                return Lang.getCaseMergingJobFailed();
            default:
                return "";
        }
    }
}

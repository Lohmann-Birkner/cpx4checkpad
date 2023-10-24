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
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TBatchResult;
import javax.ejb.Remote;

/**
 * Remote Interface for Accessing Batch Results
 *
 * @author wilde
 */
@Remote
public interface BatchResultEJBRemote {

    /**
     * find BatchResult for GrouperModel,
     *
     * @param grouperModel grouperModel, to identify BatchGroupingResult
     * @param isLocal flag to distinguish if local or extern versions should be
     * checked
     * @return GroupingResult, lazy loaded
     */
    TBatchResult findBatchResults(GDRGModel grouperModel, boolean isLocal);

    /**
     * find BatchResult for GrouperModel,
     *
     * @param grouperModel grouperModel, to identify BatchGroupingResult
     * @param isLocal flag to distinguish if local or extern versions should be
     * checked
     * @param roleId role id of the current User
     * @return GroupingResult, lazy loaded
     */
    TBatchResult findBatchResultForRole(GDRGModel grouperModel, Boolean isLocal, long roleId);

    /**
     * find BatchResult for GrouperModel, used Role is the current Role of the
     * User
     *
     * @param grouperModel grouperModel, to identify BatchGroupingResult
     * @param isLocal flag to distinguish if local or extern versions should be
     * checked
     * @return GroupingResult, lazy loaded
     */
    TBatchResult findBatchResultForRole(GDRGModel grouperModel, Boolean isLocal);

}

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

import de.lb.cpx.shared.dto.SearchItemDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dirk Niemeier
 * @param <DTO> DTO class
 */
public class SearchResult<DTO extends SearchItemDTO> implements Serializable {

    private static final long serialVersionUID = 1L;
//    private static final Logger LOG = Logger.getLogger(SearchResult.class.getName());

    public final ArrayList<DTO> resultList;
    public final int resultCount;
    //final public int leftResultCount;
    public final boolean hasMoreResults;
    public final int totalResultCount;

    /**
     * creates new instance
     *
     * @param pResultList list of search result
     * @param pHasMoreResults has more results in database?
     * @param pTotalResultCount total number of results
     */
    public SearchResult(final List<DTO> pResultList, final boolean pHasMoreResults, final int pTotalResultCount) {
        resultList = pResultList == null ? new ArrayList<>() : new ArrayList<>(pResultList);
        resultCount = resultList.size();
        totalResultCount = pTotalResultCount;
        //leftResultCount = pLeftResultCount;
        //hasMoreResults = (pLeftResultCount > 0);
        hasMoreResults = pHasMoreResults;
    }

    /**
     * creates dummy instance
     */
    public SearchResult() {
        this(new ArrayList<>(), false, 0);
    }

}

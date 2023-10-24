/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.shared.dto.QuotaListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.Remote;

/**
 *
 * @author niemeier
 */
@Remote
public interface QuotaListStatelessEJBRemote {

    /**
     * find List of WorkingListItemDTO Objects from View VwInputCaseInfo, List
     * is filtered, by given arguments Filtering of the List of Entries in View
     * VwInputCaseInfo is based of Values in FilterOptions DTO only contains
     * Data if Column is shown in Client
     *
     * @param pGrouperModel Grouper Model
     * @param pPage Page
     * @param pFetchSize Amount of results per Page
     * @param options Map which defines Filter behaviour
     * @return List of WorkingListItemDTO Objects according to given FilterMap
     * but only containing Quota relevant columns
     */
    Future<SearchResult<QuotaListItemDTO>> find(GDRGModel pGrouperModel, final int pPage, final int pFetchSize, java.util.Map<ColumnOption, List<FilterOption>> options);
    /**
     * @param pGrouperModel grouper model for grouping data
     * @param pPage page of fetched items
     * @param pFetchSize size of items to be fetched
     * @param options filter values
     * @return list of avaiable cases for quota list displayes ONLY local versions
     */
    SearchResult<QuotaListItemDTO> findQuotaList(GDRGModel pGrouperModel, final int pPage, final int pFetchSize, java.util.Map<ColumnOption, List<FilterOption>> options);
    /**
     * @param pIsLocal indicator if local or extern values
     * @param pGrouperModel grouper model for grouping data
     * @param pPage page of fetched items
     * @param pFetchSize size of items to be fetched
     * @param options filter values
     * @return list of avaiable cases
     */
    SearchResult<QuotaListItemDTO> findQuotaList(boolean pIsLocal,GDRGModel pGrouperModel, final int pPage, final int pFetchSize, java.util.Map<ColumnOption, List<FilterOption>> options);
}

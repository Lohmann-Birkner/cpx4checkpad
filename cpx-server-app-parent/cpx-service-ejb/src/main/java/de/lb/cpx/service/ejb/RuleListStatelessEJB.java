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
 *    2017  Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.shared.dto.RuleListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Bohm
 */
@Stateless
@SecurityDomain("cpx")
public class RuleListStatelessEJB implements RuleListStatelessEJBRemote {

    @EJB
    private RuleListSearchService searchService;

    @Override
    @Asynchronous
    public Future<SearchResult<RuleListItemDTO>> find(GDRGModel pGrouperModel, int pPage, int pFetchSize, Map<ColumnOption, List<FilterOption>> pOptions) {
        SearchResult<RuleListItemDTO> result = searchService.getAllWithCriteriaForFilter(true, pGrouperModel, pPage, pFetchSize, pOptions);

        return new AsyncResult<>(result);
    }

    @Override
    public SearchResult<RuleListItemDTO> findRuleList(GDRGModel pGrouperModel, int pPage, int pFetchSize, Map<ColumnOption, List<FilterOption>> pOptions) {
        return findRuleList(true, pGrouperModel, pPage, pFetchSize, pOptions);
    }
    
    @Override
    public SearchResult<RuleListItemDTO> findRuleList(boolean pIsLocal,GDRGModel pGrouperModel, int pPage, int pFetchSize, Map<ColumnOption, List<FilterOption>> pOptions) {
        return searchService.getAllWithCriteriaForFilter(pIsLocal, pGrouperModel, pPage, pFetchSize, pOptions);
    }
}

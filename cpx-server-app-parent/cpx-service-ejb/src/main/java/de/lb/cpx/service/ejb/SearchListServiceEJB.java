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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CSearchListDao;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CSearchList;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.service.jms.producer.StatusBroadcastProducer;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.service.properties.UserProperties;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Dirk Niemeier
 */
@Singleton
@SecurityDomain("cpx")
public class SearchListServiceEJB implements SearchListServiceEJBRemote {

    private static final Logger LOG = Logger.getLogger(SearchListServiceEJB.class.getName());

    @Inject
    private CSearchListDao searchListDao;
    @Inject
    private CdbUsersDao cdbUsersDao;
    @Inject
    private StatusBroadcastProducer<String> broadcast;

    @Override
    public long saveSearchList(final SearchListResult pSearchListResult, final boolean pIsSelected) {
        return saveSearchList(pSearchListResult, pIsSelected, false);
    }

    private long saveSearchList(final SearchListResult pSearchListResult, final boolean pIsSelected, final boolean pIsStandardFilter) {
        if (pSearchListResult == null) {
            return 0L;
        }
        final Long userId = currentUserId();
        if (!pIsStandardFilter && !pSearchListResult.isPersistable(userId)) {
            LOG.log(Level.FINEST, "won''t store this filter (it is either an \"empty\" filter or it was created by another user than {0})", userId);
            return 0L; //don't store default filter ("empty" filter) or filters that are not assigned to current user
        }
        CSearchList searchList = pSearchListResult.getSearchList();
        if (searchList.getId() != 0L) {
            //if (!searchListDao.exists(searchList.getId())) {
            //CSearchList entity = searchListDao.findById(searchList.getId());
            if (!searchListDao.exists("C_SEARCHLIST", searchList.getId())) {
                LOG.log(Level.WARNING, "filter with id={0}, type={1} and name={2} was maybe deleted meanwhile", new Object[]{searchList.getId(), searchList.getSlType(), searchList.getSlName()});
                return searchList.getId();
            }
            searchList.setModificationDate(new Date());
            searchList.setModificationUser(userId);
        } else {
            searchList.setCreationDate(new Date());
            //don't set creation user here, because DRG/PEPP filter don't have an creation user to display it to all users
            if (pIsStandardFilter) {
                searchList.setCreationUser(null);
            } else {
                searchList.setCreationUser(userId);
            }
        }
        CSearchList newSearchList = searchListDao.saveOrUpdate(searchList);
        searchListDao.flush();
        final boolean update = searchList.getId() == newSearchList.getId();
        SearchListProperties props = pSearchListResult.getSearchListProperties();
        if (props != null) {
//            props.setId(searchList.getId());
//            props.setName(searchList.getSlName());
//            props.setList(searchList.getSlType());
            searchListDao.setSearchListProperties(newSearchList.getId(), props);
        }
        if (pIsSelected) {
            setSelectedSearchList(newSearchList);
        }
        if (!pIsStandardFilter) {
            if (update) {
                broadcast.send(BroadcastOriginEn.CORE_DATA, MenuCacheEntryEn.SEARCHLISTS.name() + ";UPDATE;" + newSearchList.getId());
            } else {
                broadcast.send(BroadcastOriginEn.CORE_DATA, MenuCacheEntryEn.SEARCHLISTS.name() + ";INSERT;" + newSearchList.getId());
            }
        }
        return newSearchList.getId();
    }

    @Override
    public boolean deleteSearchList(CSearchList pSearchList) {
        searchListDao.remove(pSearchList.getId());
        broadcast.send(BroadcastOriginEn.CORE_DATA, MenuCacheEntryEn.SEARCHLISTS.name() + ";DELETE;" + pSearchList.getId());
        return true;
    }

    @Override
    public Map<Long, SearchListResult> getSearchLists(final Long[] pIds) {
        return getSearchLists(null, pIds);
    }

    @Override
    public Map<Long, SearchListResult> getSearchLists() {
        Map<Long, SearchListResult> result = getSearchLists(null, null);
        ensureStandardFilterExists(result);
//        int ruleFilters = filterSearchLists(result, SearchListTypeEn.RULE).size();
//        if (ruleFilters < 2) {
//            //Rule Filters does not seem to exist: create them on the fly!
//            List<SearchListResult> newLists = createRuleFilter();
//            for (SearchListResult searchList : newLists) {
//                result.put(searchList.getId(), searchList);
//            }
//        }
        return result;
    }

    private void ensureStandardFilterExists(final Map<Long, SearchListResult> result) {
        //standard filter for DRG and PEPP in rule list
        final String[] drgFilterNames = new String[]{"DRG", "PEPP"};
        final List<SearchListResult> newFilters = new ArrayList<>();
        for (String name : drgFilterNames) {
            if (!standardSearchListExists(result, SearchListTypeEn.RULE, name)) {
                newFilters.add(createRuleFilter(name));
            }
        }

        //standard filter per admission year in working list
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        for (int y = currentYear - 4; y <= currentYear; y++) {
            String name = "Aufnahmejahr " + y;
            if (!standardSearchListExists(result, SearchListTypeEn.WORKING, name)) {
                newFilters.add(createWorkingFilterAdmissionYear(name, y));
            }
        }

        //standard filter for process finalisation in workflow list
        String finalisationName = "Vorgangsabschluss";
        if (!standardSearchListExists(result, SearchListTypeEn.WORKFLOW, finalisationName)) {
            newFilters.add(createProcessFinalisation(finalisationName));
        }

        //standard filter for deadlines in workflow list
        String deadlinesName = "Wiedervorlagen";
        if (!standardSearchListExists(result, SearchListTypeEn.WORKFLOW, deadlinesName)) {
            newFilters.add(createProcessDeadlines(deadlinesName));
        }

        //standard filter for cancelled cases in working list
        String canceledCaseName = "Stornierte Fälle";
        if (!standardSearchListExists(result, SearchListTypeEn.WORKING, canceledCaseName)) {
            newFilters.add(createWorkingCanceledCases(canceledCaseName));
        }

        //standard filter for cancelled Processes in working list
        String canceledProcessName = "Stornierte Vorgänge";
        if (!standardSearchListExists(result, SearchListTypeEn.WORKFLOW, canceledProcessName)) {
            newFilters.add(createProcessCanceled(canceledProcessName));
        }

        
        final List<CSearchList> tmp = new ArrayList<>();
        for (SearchListResult searchListResult : newFilters) {
            if (searchListResult == null) {
                continue;
            }
            tmp.add(searchListResult.getSearchList());
        }
//        for (SearchListResult searchListResult : newFilters) {
//            if (searchListResult == null) {
//                continue;
//            }
//            result.put(searchListResult.getId(), searchListResult);
//        }
        result.putAll(getSearchLists(tmp));
    }

    private boolean standardSearchListExists(final Map<Long, SearchListResult> result, final SearchListTypeEn pType, final String pName) {
        return !filterStandardSearchLists(result, pType, pName).isEmpty();
    }

    private List<SearchListResult> filterStandardSearchLists(final Map<Long, SearchListResult> result, final SearchListTypeEn pType, final String pName) {
        List<SearchListResult> list = filterStandardSearchLists(result, pType);
        Iterator<SearchListResult> it = list.iterator();
        while (it.hasNext()) {
            SearchListResult item = it.next();
            if (!Objects.equals(pName, item.getName())) {
                it.remove();
            }
        }
        return list;
    }

    private synchronized SearchListResult createRuleFilter(final String pName) {
//        final String[] filterNames = new String[]{"DRG", "PEPP"};
//        List<SearchListResult> result = new ArrayList<>();
//        for (String filterName : filterNames) {
//            final SearchListProperties newRuleList = SearchListTypeFactory.instance().getNewSearchListProperties(SearchListTypeEn.RULE, null);
        //newRuleList.setName("Regelliste " + filterValue);
        //final FilterOption filterOption = new FilterOption("", RuleListAttributes.csCaseTypeEn, filterValue);
        //filterOption.setCounter(1);
        //newRuleList.addFilter(filterOption);
//            try {
        SearchListResult searchListResult = new SearchListResult(null, pName, SearchListTypeEn.RULE);
        final FilterOption filterOption = new FilterOption("", RuleListAttributes.csCaseTypeEn, pName);
        searchListResult.addFilter(filterOption);
        saveSearchList(searchListResult, false, true);
//            result.add(searchListResult);
//        }
//        return result;
        return searchListResult;
    }

    private synchronized SearchListResult createWorkingFilterAdmissionYear(final String pName, final int pYear) {
        SearchListResult searchListResult = new SearchListResult(null, pName, SearchListTypeEn.WORKING);
        final FilterOption filterOptionFrom = new FilterOption("", WorkingListAttributes.csdAdmissionDateFrom, pYear + "-01-01");
        final FilterOption filterOptionTo = new FilterOption("", WorkingListAttributes.csdAdmissionDateTo, pYear + "-12-31");
        searchListResult.addFilter(filterOptionFrom);
        searchListResult.addFilter(filterOptionTo);
        saveSearchList(searchListResult, false, true);
        return searchListResult;
    }

    private synchronized SearchListResult createProcessFinalisation(final String pName) {
        SearchListResult searchListResult = new SearchListResult(null, pName, SearchListTypeEn.WORKFLOW);
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.workflowNumber, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.csCaseNumber, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.csHospitalIdent, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.processTopic, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.wmState, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.processResult, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.cwDiff, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.cwFinal, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.cwInitial, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.drgFinal, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.drgInitial, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.revenueDiff, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.losDiff, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.losFinal, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.losInitial, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.supFeeDiff, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.supFeeFinal, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.supFeeInitial, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.assUser, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.vmModUser, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.assLastModificationDate, true));
        saveSearchList(searchListResult, false, true);
        return searchListResult;
    }

    private synchronized SearchListResult createProcessDeadlines(final String pName) {
        SearchListResult searchListResult = new SearchListResult(null, pName, SearchListTypeEn.WORKFLOW);
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.workflowNumber, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.csCaseNumber, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.remLatestCreationDate, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.assSubject, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.assReceiver, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.remFinished, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.vmModUser, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.assUser, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.assLastModificationDate, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.patNumber, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.patName, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.csdAdmissionDate, true));
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.csdDischargeDate, true));
        saveSearchList(searchListResult, false, true);
        return searchListResult;
    }

    private synchronized SearchListResult createWorkingCanceledCases(final String pName) {
        SearchListResult searchListResult = new SearchListResult(null, pName, SearchListTypeEn.WORKING);
        searchListResult.addColumn(new ColumnOption("", WorkingListAttributes.isCancel, true));
        final FilterOption filterOption = new FilterOption("", WorkingListAttributes.isCancel, "1");
        searchListResult.addFilter(filterOption);
        saveSearchList(searchListResult, false, true);
        return searchListResult;
    }

    private synchronized SearchListResult createProcessCanceled(final String pName) {
        SearchListResult searchListResult = new SearchListResult(null, pName, SearchListTypeEn.WORKFLOW);
        searchListResult.addColumn(new ColumnOption("", WorkflowListAttributes.isCancel, true));
        final FilterOption filterOption = new FilterOption("", WorkflowListAttributes.isCancel, "1");
        searchListResult.addFilter(filterOption);
        saveSearchList(searchListResult, false, true);
        return searchListResult;
    }

    
    @Override
    public SearchListProperties getSearchListProperties(final Long pSearchListId) {
        //return cpxServerConfig.getUserProperties(pUserId);
        CSearchList searchList = searchListDao.findById(pSearchListId);
        SearchListProperties props = null;
        if (searchList != null) {
            props = searchListDao.getSearchListProperties(pSearchListId);
        }
        if (props == null) {
            props = new SearchListProperties();
        }
        return props;
    }

    @Override
    public boolean setSearchListProperties(final Long pSearchListId, final SearchListProperties pSearchListProperties) {
        return searchListDao.setSearchListProperties(pSearchListId, pSearchListProperties);
    }

    private List<SearchListResult> filterStandardSearchLists(final Map<Long, SearchListResult> pMap, final SearchListTypeEn pList) {
        return filterSearchLists(pMap, pList, null);
    }

    private List<SearchListResult> filterSearchLists(final Map<Long, SearchListResult> pMap, final SearchListTypeEn pList, final Long pUserId) {
        List<SearchListResult> result = new ArrayList<>();
        Iterator<Map.Entry<Long, SearchListResult>> it = pMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, SearchListResult> entry = it.next();
            if (pList != null && !pList.equals(entry.getValue().getType())) {
                continue;
            }
            if (!Objects.equals(pUserId, entry.getValue().getCreationUser())) {
                continue;
            }
            result.add(entry.getValue());
        }
        return result;
    }

    @Override
    public Map<Long, SearchListResult> getSearchLists(final SearchListTypeEn pList) {
        return getSearchLists(pList, null);
    }

    @Override
    public Map<Long, SearchListResult> getSearchLists(final SearchListTypeEn pList, final Long[] pIds) {
        final List<CSearchList> searchLists = searchListDao.findAll(pList, pIds);
        return getSearchLists(searchLists);
    }

    private Map<Long, SearchListResult> getSearchLists(final List<CSearchList> pSearchLists) {
        final Map<Long, SearchListResult> result = new LinkedHashMap<>();
        Iterator<CSearchList> it = pSearchLists.iterator();
        final Long userId = ClientManager.getCurrentCpxUserId();
        final List<Long> currentRoleIds = ClientManager.getCurrentCpxRoleIds();
        while (it.hasNext()) {
            CSearchList searchList = it.next();
            if (searchList == null) {
                continue;
            }
//            if (pList != null && !pList.equals(searchList.getSlType())) {
//                continue;
//            }
            boolean writeable = searchList.isWriteable(userId);
            boolean readonly = searchList.isReadonly(userId, currentRoleIds);
            if (writeable || readonly) {
                result.put(searchList.getId(), new SearchListResult(searchList, writeable, readonly));
            }
        }
        return result;
    }

    @Override
    public boolean setSelectedSearchList(final CSearchList pCSearchList) {
        final Long userId = currentUserId();
        UserProperties props = getUserProperties(userId, true);
        props.common.setSelectedSearchList(pCSearchList.getSlType(), pCSearchList.getId());
        return cdbUsersDao.setUserProperties(userId, props);
    }

    @Override
    public Long getSelectedSearchListId(final SearchListTypeEn pList) {
        final Long userId = currentUserId();
        UserProperties props = getUserProperties(userId, false);
        return props == null ? null : props.common.getSelectedSearchList(pList);
    }

    private Long currentUserId() {
        return ClientManager.getCurrentCpxUserId();
    }

    public UserProperties getUserProperties(final Long pUserId, final boolean pCreateNewObject) {
        //return cpxServerConfig.getUserProperties(pUserId);
        CdbUsers user = cdbUsersDao.findById(pUserId);
        UserProperties props = null;
        if (user != null) {
            props = cdbUsersDao.getUserProperties(pUserId);
        }
        if (props == null && pCreateNewObject) {
            props = new UserProperties();
        }
        return props;
    }

}

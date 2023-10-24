/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.cache;

import com.google.common.collect.Lists;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commonDB.model.CSearchList;
import de.lb.cpx.service.ejb.SearchListServiceEJBRemote;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import de.lb.cpx.str.utils.FileNameUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * key = id
 *
 * @author niemeier
 */
public class MenuCacheSearchLists extends MenuCacheEntry<Long, SearchListResult> {

    private static final Logger LOG = Logger.getLogger(MenuCacheSearchLists.class.getName());

    @Override
    public List<SearchListResult> remove(String[] pKeys) {
        List<SearchListResult> result = new ArrayList<>();
        final Long[] keys = toLongArray(pKeys);
        for (Long value : keys) {
            SearchListResult obj = super.remove(value);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

    @Override
    protected MenuCacheRefreshResult<Long, SearchListResult> initialize(final String[] pKeys) {
        EjbProxy<SearchListServiceEJBRemote> bean = Session.instance().getEjbConnector().connectSearchListServiceBean();
        final Long[] keys = toLongArray(pKeys);
        return new MenuCacheRefreshResult<>(keys, bean.get().getSearchLists(keys));
    }

    protected static Long[] toLongArray(final String[] pKeys) {
        Set<Long> values = new TreeSet<>();
        for (String v : pKeys) {
            v = StringUtils.trimToEmpty(v);
            if (v.isEmpty()) {
                continue;
            }
            try {
                final Long val = Long.parseLong(v);
                values.add(val);
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "cannot parse this as a long value: {0}", v);
                LOG.log(Level.FINEST, "illegal long value: {0}", ex);
            }
        }
        Long[] ids = new Long[values.size()];
        values.toArray(ids);
        return ids;
    }

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, SearchListResult> initialize() {
        EjbProxy<SearchListServiceEJBRemote> bean = Session.instance().getEjbConnector().connectSearchListServiceBean();
        return bean.get().getSearchLists();
    }

    @Override
    public MenuCacheSearchLists getCopy() {
        return (MenuCacheSearchLists) super.getCopy(new MenuCacheSearchLists());
    }

    @Override
    public String getName(Long pKey) {
        SearchListResult obj = get(pKey);
        return obj == null ? null : obj.getName();
    }

    public Long getSelectedSearchListId(final SearchListTypeEn pListType) {
        EjbProxy<SearchListServiceEJBRemote> bean = Session.instance().getEjbConnector().connectSearchListServiceBean();
        return bean.get().getSelectedSearchListId(pListType);
    }

    public boolean setSelectedSearchList(final SearchListResult pSearchListResult) {
        EjbProxy<SearchListServiceEJBRemote> bean = Session.instance().getEjbConnector().connectSearchListServiceBean();
        return bean.get().setSelectedSearchList(pSearchListResult.getSearchList());
    }

    public SearchListResult getSelectedSearchList(final SearchListTypeEn pListType) {
        final Long searchListId = getSelectedSearchListId(pListType);
        return get(searchListId);
    }

    public boolean deleteSearchList(final SearchListResult pSearchListResult) {
        EjbProxy<SearchListServiceEJBRemote> bean = Session.instance().getEjbConnector().connectSearchListServiceBean();
        return bean.get().deleteSearchList(pSearchListResult.getSearchList());
    }

    public Map<Long, SearchListResult> get(final String pName) {
        Map<Long, SearchListResult> m = map();
        if (m == null) {
            return null;
        }
        final String name = StringUtils.trimToEmpty(pName);
        Iterator<Map.Entry<Long, SearchListResult>> it = m.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, SearchListResult> entry = it.next();
            if (!name.equalsIgnoreCase(StringUtils.trimToEmpty(entry.getValue().getName()))) {
                it.remove();
            }
        }
        return m;
    }

    public SearchListResult get(final String pName, final SearchListTypeEn pType) {
        Map<Long, SearchListResult> m = get(pName);
        if (m == null) {
            return null;
        }
        if (pType != null) {
            Iterator<Map.Entry<Long, SearchListResult>> it = m.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, SearchListResult> entry = it.next();
                if (!pType.equals(entry.getValue().getType())) {
                    it.remove();
                }
            }
        }
        if (m.isEmpty()) {
            return null;
        }
        if (m.size() > 1) {
            LOG.log(Level.WARNING, "multiple ({0}) search lists found for current user with type={1} and name={2} -> will pick first one", new Object[]{m.size(), pType, pName});
        }
        return m.entrySet().iterator().next().getValue();
    }

    @Override
    public SearchListResult get(final Long pKey) {
        return get(pKey, null);
    }

    public SearchListResult get(final Long pKey, final SearchListTypeEn pType) {
        SearchListResult obj = super.get(pKey);
        if (obj != null) {
            if (pType != null && !pType.equals(obj.getType())) {
                return null;
            }
            initSearchListProperties(obj);
        }
        return obj;
    }

    private void initSearchListProperties(final SearchListResult pObj) {
        if (pObj != null && !pObj.isSearchListPropertiesInitialized()) {
            EjbProxy<SearchListServiceEJBRemote> bean = Session.instance().getEjbConnector().connectSearchListServiceBean();
            pObj.setSearchListProperties(bean.get().getSearchListProperties(pObj.getId()));
        }
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.SEARCHLISTS;
    }

    public Map<Long, SearchListResult> map(final SearchListTypeEn pType) {
        final Map<Long, SearchListResult> result = map();
        if (pType == null || result == null) {
            return result;
        }
        Iterator<Map.Entry<Long, SearchListResult>> it = result.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, SearchListResult> entry = it.next();
            if (!pType.equals(entry.getValue().getType())) {
                it.remove();
            }
        }
        return result;
    }

    public List<SearchListResult> values(final SearchListTypeEn pType) {
        Map<Long, SearchListResult> m = map(pType);
        return m == null ? null : Lists.newArrayList(m.values());
    }

    public Set<Map.Entry<Long, SearchListResult>> entrySet(final SearchListTypeEn pType) {
        Map<Long, SearchListResult> m = map(pType);
        return m == null ? null : map(pType).entrySet();
    }

    public SearchListResult saveSearchList(final SearchListResult pSearchListResult, final boolean pIsSelected) {
        if (SearchListTypeEn.RULE.equals(pSearchListResult.getType())) {
            LOG.log(Level.INFO, "rule filter cannot be created by user!");
            return pSearchListResult;
        }
        final long userId = Session.instance().getCpxUserId();
        if (!pSearchListResult.isPersistable(userId)) {
            LOG.log(Level.FINEST, "won''t store this filter (it is either an \"empty\" filter or it was created by another user than {0})", userId);
            return pSearchListResult; //don't store default filter ("empty" filter) or filters that are not assigned to current user
        }
        if (pSearchListResult.isEmptyFilter()) {
            LOG.log(Level.FINEST, "won''t store default filter (\"empty\" filter)");
            return pSearchListResult;
        }
        CSearchList searchList = pSearchListResult.getSearchList();
        EjbProxy<SearchListServiceEJBRemote> bean = Session.instance().getEjbConnector().connectSearchListServiceBean();
        long id = bean.get().saveSearchList(pSearchListResult, pIsSelected);
        if (id != 0 && searchList != null) {
            searchList.setId(id);
        }
        return pSearchListResult;
    }

    public SearchListResult createNewSearchList(final SearchListTypeEn pList, final String pName, final boolean pIsSelected) {
        SearchListResult searchListResult = new SearchListResult(Session.instance().getCpxUserId(), pName, pList);
        searchListResult = saveSearchList(searchListResult, pIsSelected);
        return searchListResult;
    }
    public SearchListResult copySearchList(@NotNull final SearchListResult pToCopy, final boolean pIsSelected) {
        Objects.requireNonNull(pToCopy, "SearchList to Copy can not be null!");
        SearchListResult searchListResult = new SearchListResult(Session.instance().getCpxUserId(), getSearchListCopyName(pToCopy), pToCopy.getType());
        SearchListTypeEn type = pToCopy.getSearchListProperties().getList();
        Set<ColumnOption> columns = pToCopy.getSearchListProperties().getColumns();
        Set<FilterOption> filters = pToCopy.getSearchListProperties().getFilters();
        searchListResult.getSearchListProperties().setColumns(columns);
        searchListResult.getSearchListProperties().setFilters(filters);
        searchListResult.getSearchListProperties().setList(type);
        searchListResult = saveSearchList(searchListResult, pIsSelected);
        return searchListResult;
    }
    public String getSearchListCopyName(SearchListResult pResult){
        return getSearchListCopyName(pResult, values(pResult.getType()));
    }
    public String getSearchListCopyName(SearchListResult pResult,List<SearchListResult> pSearchLists){
        Map<String,List<String>> entries = new HashMap<>();
        for(SearchListResult result : pSearchLists){
            if(!entries.containsKey(result.getName())){
                entries.put(result.getName(), new ArrayList<>());
            }
            if(!entries.get(result.getName()).contains(result.getName())){
                entries.get(result.getName()).add(result.getName());
            }
            
            String orgName = FileNameUtils.getOriginalName(result.getName());
            if(!entries.containsKey(orgName)){
                entries.put(orgName, new ArrayList<>());
            }
            if(!entries.get(orgName).contains(result.getName())){
                entries.get(orgName).add(result.getName());
            }
        }
        int cnt = entries.get(pResult.getName()).size();
        return FileNameUtils.getCopyName(pResult.getName(), cnt);
    }
}

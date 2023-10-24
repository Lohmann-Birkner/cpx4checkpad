/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018 niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.searchlist;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commonDB.model.CSearchList;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.SearchListTypeFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author niemeier
 */
public class SearchListResult implements Serializable, Comparable<SearchListResult> {

    private static final long serialVersionUID = 1L;

    private final CSearchList searchList;
    private SearchListProperties searchListProperties;
    private boolean propertiesInitialized = false;
    private final boolean writeable;
    private final boolean readonly;

//    public SearchListResult(final CSearchList pSearchList, final boolean pIsReadonly) {
//        this(pSearchList, null);
//    }
    public SearchListResult(final Long pCreationUserId, final SearchListTypeEn pListType) {
        this(createSearchList(pCreationUserId, "", pListType), true, false);
    }

    public SearchListResult(final Long pCreationUserId, final String pName, final SearchListTypeEn pListType) {
        this(createSearchList(pCreationUserId, pName, pListType), true, false);
    }

    private static CSearchList createSearchList(final Long pCreationUserId, final String pName, final SearchListTypeEn pListType) {
        CSearchList sl = new CSearchList();
        sl.setCreationDate(new Date());
        sl.setCreationUser(pCreationUserId);
        sl.setSlName(pName);
        sl.setSlType(pListType);
        return sl;
    }

//    public SearchListResult(final CSearchList pSearchList, final SearchListProperties pSearchListProperties) {
//        this(pSearchList, pSearchListProperties, true, false);
//    }
    public SearchListResult(final CSearchList pSearchList /*, final SearchListProperties pSearchListProperties */, final boolean pIsWriteable, final boolean pIsReadonly) {
        this.searchList = pSearchList;
//        this.searchListProperties = pSearchListProperties;
        this.writeable = pIsWriteable;
        this.readonly = pIsReadonly;

        if (pSearchList == null) {
            throw new IllegalArgumentException("SearchList cannot be null!");
        }
//        if (pSearchList.getCreationUser() == null) {
//            throw new IllegalArgumentException("SearchList creation user cannot be null!");
//        }
        if (pSearchList.getSlType() == null) {
            throw new IllegalArgumentException("SearchList type cannot be null!");
        }
//        if (pSearchList.getSlName() == null || pSearchList.getSlName().trim().isEmpty()) {
//            throw new IllegalArgumentException("SearchList name cannot be null or empty!");
//        }
//        if (pSearchListProperties == null) {
//            throw new IllegalArgumentException("SearchList Properties cannot be null!");
//        }
    }

    /**
     * @return the searchList
     */
    public CSearchList getSearchList() {
        return searchList;
    }

    public SearchListProperties getSearchListProperties() {
        SearchListProperties props = searchListProperties;
//        if (props != null) {
//            return props;
//        }
        if (props == null) {
            props = SearchListTypeFactory.instance().getNewSearchListProperties(getType());
            //props.setId(searchList.getId());
            //props.setName(searchList.getSlName());
            //props.setList(searchList.getSlType());
            searchListProperties = props;
            propertiesInitialized = false;
        }
        updateProperties(props);
        return props;
    }

    public void setSearchListProperties(final SearchListProperties pSearchListProperties) {
        searchListProperties = updateProperties(pSearchListProperties);
        propertiesInitialized = searchListProperties != null;
    }

    /**
     * stores some attributes redundant in properties because they can be
     * exported and imported
     *
     * @param pSearchListProperties properties
     * @return self-reference
     */
    private SearchListProperties updateProperties(final SearchListProperties pSearchListProperties) {
        if (pSearchListProperties == null) {
            return pSearchListProperties;
        }
        pSearchListProperties.setName(getName());
        pSearchListProperties.setId(getId());
        pSearchListProperties.setList(getType());
        return pSearchListProperties;
    }

    public boolean isSearchListPropertiesInitialized() {
        return propertiesInitialized;
    }

    public long getId() {
        return searchList.getId();
    }

    public String getName() {
        return searchList.getSlName();
    }

    public Long getCreationUser() {
        return searchList.getCreationUser();
    }

    public List<Long> getVisibleToRoleIds() {
        return searchList.getVisibleToRoleIds();
    }

    public List<Long> getVisibleToUserIds() {
        return searchList.getVisibleToUserIds();
    }

    public boolean isWriteable(final Long pUserId) {
        return searchList.isWriteable(pUserId);
    }

    public boolean isShared() {
        return searchList.isShared();
    }

    public boolean isReadonlyToAll() {
        return searchList.isReadonlyToAll();
    }

    public boolean isReadonly(final Long pUserId, final List<Long> pRoleIds) {
        return searchList.isReadonly(pUserId, pRoleIds);
    }

    public SearchListTypeEn getType() {
        return searchList.getSlType();
    }

    @Override
    public String toString() {
        return searchList.getSlName();
    }

    public Set<FilterOption> getFilters() {
        return searchListProperties == null ? new HashSet<>() : searchListProperties.getFilters();
    }

    public Set<ColumnOption> getColumns() {
        return searchListProperties == null ? new HashSet<>() : searchListProperties.getColumns();
    }

    public SearchListResult addFilter(final FilterOption opt) {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            prop.addFilter(opt);
        }
        return this;
    }

    public SearchListResult addColumn(final ColumnOption opt) {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            prop.addColumn(opt);
        }
        return this;
    }

    public List<FilterOption> getFilter(final String pName) {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            return prop.getFilter(pName);
        }
        return new ArrayList<>();
    }

    public ColumnOption getColumn(final String pName) {
        SearchListProperties prop = getSearchListProperties();
        ColumnOption opt = null;
        if (prop != null) {
            opt = prop.getColumn(pName);
        }
        return opt;
    }

    public SearchListResult removeFilter(final String pName) {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            prop.removeFilter(pName);
        }
        return this;
    }

    public SearchListResult removeColumn(String pName) {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            prop.removeColumn(pName);
        }
        return this;
    }

    public List<ColumnOption> getColumnsOrdered() {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            return prop.getColumnsOrdered();
        }
        return new ArrayList<>();
    }

    public List<ColumnOption> getColumnsSorted() {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            return prop.getColumnsSorted();
        }
        return new ArrayList<>();
    }

    public void setVisibleToUserIds(final List<Long> pUserIds) {
        searchList.setVisibleToUserIds(pUserIds);
    }

    public void setVisibleToRoleIds(final List<Long> pRoleIds) {
        searchList.setVisibleToRoleIds(pRoleIds);
    }

    /**
     * this is a calculated temporary field for convenience and better
     * performance!
     *
     * @return is filter writeable?
     */
    public boolean isWriteable() {
        return writeable;
    }

    /**
     * this is a calculated temporary field for convenience and better
     * performance!
     *
     * @return is filter readonly?
     */
    public boolean isReadonly() {
        return readonly;
    }

    public String serialize() {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            return prop.serialize();
        }
        return null;
    }

    public void setColumns(final Set<ColumnOption> pColumnOptions) {
        SearchListProperties prop = getSearchListProperties();
        if (prop != null) {
            prop.setColumns(pColumnOptions);
        }
    }

    @Override
    public int compareTo(SearchListResult o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final SearchListResult other = (SearchListResult) obj;
//        return compareTo(other) == 0;
//    }
    /**
     * is this an empty default filter (nameless filters are not stored!)?
     *
     * @return is nameless/empty filter?
     */
    public boolean isEmptyFilter() {
        return searchList.isEmptyFilter();
    }

    /**
     * is this filter stored in database?
     *
     * @param pUserId pass current user id here to check if it is writeable
     * @return storable filter?
     */
    public boolean isPersistable(final Long pUserId) {
        return searchList.isPersistable(pUserId);
    }
}

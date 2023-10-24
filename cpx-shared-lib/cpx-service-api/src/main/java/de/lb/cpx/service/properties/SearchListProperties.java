/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.properties;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dirk Niemeier
 */
@XmlRootElement(name = "searchListProperties")
public class SearchListProperties extends CpxProperties {

    private static final long serialVersionUID = 1L;

//    @XmlAttribute
//    protected final long id;
//    protected String name = "";
//    protected Long creation_user_id = null;
//    protected long[] visible_to_user_ids = new long[0];
//    protected long[] visible_to_role_ids = new long[0];
//    protected Boolean selected = false;
    protected HashSet<ColumnOption> columnOptions = new HashSet<>();
    protected HashSet<FilterOption> filterOptions = new HashSet<>();
    protected SearchListTypeEn listType = null;
//    private Boolean local = true;

    public SearchListProperties() {
        //
    }

//    public SearchList() {
//        id = 0L;
//    }
//
//    public SearchList(final SearchListTypeEn pList) {
//        this.setList(pList);
//        id = generateId();
//    }
//
//    public SearchList(final SearchListTypeEn pList, final String pName) {
//        this.setList(pList);
//        this.setName(pName);
//        id = generateId();
//    }
    public static SearchListProperties deserialize(final String pInput) {
        return XmlSerializer.deserialize(pInput, SearchListProperties.class);
    }

//    private static long generateId() {
//        return System.nanoTime(); //should be unique
////        final long min = 1L;
////        final long max = Long.MAX_VALUE;
////        double d = (Math.random() * ((max - min) + 1)) + min;
////        return (long) d;
//    }
//
//    public long getId() {
//        return id;
//    }
//    public Boolean isSelected() {
//        return selected;
//    }
//    @XmlAttribute
//    public SearchList setCreationUserId(final Long pCreationUserId) {
//        this.creation_user_id = (pCreationUserId != null && pCreationUserId.equals(0L)) ? null : pCreationUserId;
//        return this;
//    }
//
//    public Long getCreationUserId() {
//        return this.creation_user_id;
//    }
//
//    public SearchList addVisibleUserId(final Long pVisibleUserId) {
//        if (pVisibleUserId == null || pVisibleUserId.equals(0L)) {
//            //
//        } else {
//            long[] tmp = new long[this.visible_to_user_ids.length + 1];
//            System.arraycopy(this.visible_to_user_ids, 0, tmp, 0, this.visible_to_user_ids.length);
//            tmp[tmp.length - 1] = pVisibleUserId;
//            this.visible_to_user_ids = tmp;
////            List<Long> list = Arrays.asList(getVisibleToUserIds());
////            list.add(pVisibleUserId);
////            setVisibleToUserIds(list);
//        }
//        return this;
//    }
//
//    public SearchList addVisibleRoleId(final Long pVisibleRoleId) {
//        if (pVisibleRoleId == null || pVisibleRoleId.equals(0L)) {
//            //
//        } else {
//            long[] tmp = new long[this.visible_to_role_ids.length + 1];
//            System.arraycopy(this.visible_to_role_ids, 0, tmp, 0, this.visible_to_role_ids.length);
//            tmp[tmp.length - 1] = pVisibleRoleId;
//            this.visible_to_role_ids = tmp;
////            List<Long> list = Arrays.asList(getVisibleToRoleIds());
////            list.add(pVisibleRoleId);
////            setVisibleToRoleIds(list);
//        }
//        return this;
//    }
//
//    @XmlAttribute
//    public SearchList setVisibleToUserIds(final long[] pVisibleUserIds) {
//        if (pVisibleUserIds == null || pVisibleUserIds.length == 0) {
//            visible_to_user_ids = new long[0];
//        } else {
//            long[] tmp = new long[pVisibleUserIds.length];
//            System.arraycopy(pVisibleUserIds, 0, tmp, 0, pVisibleUserIds.length);
//            visible_to_user_ids = tmp;
//        }
//        return this;
//    }
//
//    public SearchList setVisibleToUserIds(final Long[] pVisibleUserIds) {
//        final List<Long> list = new ArrayList<>();
//        if (pVisibleUserIds != null && pVisibleUserIds.length > 0) {
//            for (final Long userId : pVisibleUserIds) {
//                if (userId != null && !userId.equals(0L)) {
//                    list.add(userId);
//                }
//            }
//        }
//        long[] tmp = new long[list.size()];
//        int i = 0;
//        for (Long value : list) {
//            tmp[i++] = value;
//        }
//        //Arrays.stream(list).distinct().collect(Collectors.toList());
//        //list.toArray(tmp);
//        this.visible_to_user_ids = tmp;
//        return this;
//    }
//
//    public SearchList setVisibleToUserIds(final List<Long> pVisibleUserIds) {
//        final Long[] tmp;
//        if (pVisibleUserIds == null || pVisibleUserIds.isEmpty()) {
//            tmp = new Long[0];
//        } else {
//            tmp = new Long[pVisibleUserIds.size()];
//            pVisibleUserIds.toArray(tmp);
//        }
//        return setVisibleToUserIds(tmp);
//    }
//
//    @XmlAttribute
//    public SearchList setVisibleToRoleIds(final long[] pVisibleRoleIds) {
//        if (pVisibleRoleIds == null || pVisibleRoleIds.length == 0) {
//            visible_to_role_ids = new long[0];
//        } else {
//            long[] tmp = new long[pVisibleRoleIds.length];
//            System.arraycopy(pVisibleRoleIds, 0, tmp, 0, pVisibleRoleIds.length);
//            visible_to_role_ids = tmp;
//        }
//        return this;
//    }
//
//    public SearchList setVisibleToRoleIds(final Long[] pVisibleRoleIds) {
//        final List<Long> list = new ArrayList<>();
//        if (pVisibleRoleIds != null && pVisibleRoleIds.length > 0) {
//            for (final Long roleId : pVisibleRoleIds) {
//                if (roleId != null && !roleId.equals(0L)) {
//                    list.add(roleId);
//                }
//            }
//        }
//        long[] tmp = new long[list.size()];
//        int i = 0;
//        for (Long value : list) {
//            tmp[i++] = value;
//        }
//        //list.toArray(tmp);
//        this.visible_to_role_ids = tmp;
//        return this;
//    }
//
//    public long[] getVisibleToRoleIds() {
//        long[] tmp;
//        if (this.visible_to_role_ids == null || this.visible_to_role_ids.length == 0) {
//            tmp = new long[0];
//        } else {
//            tmp = new long[this.visible_to_role_ids.length];
//            System.arraycopy(this.visible_to_role_ids, 0, tmp, 0, this.visible_to_role_ids.length);
//        }
//        return tmp;
//    }
//
//    public long[] getVisibleToUserIds() {
//        long[] tmp;
//        if (this.visible_to_user_ids == null || this.visible_to_user_ids.length == 0) {
//            tmp = new long[0];
//        } else {
//            tmp = new long[this.visible_to_user_ids.length];
//            System.arraycopy(this.visible_to_user_ids, 0, tmp, 0, this.visible_to_user_ids.length);
//        }
//        return tmp;
//    }
//
//    public SearchList setVisibleToRoleIds(final List<Long> pVisibleRoleIds) {
//        final Long[] tmp;
//        if (pVisibleRoleIds == null || pVisibleRoleIds.isEmpty()) {
//            tmp = new Long[0];
//        } else {
//            tmp = new Long[pVisibleRoleIds.size()];
//            pVisibleRoleIds.toArray(tmp);
//        }
//        return setVisibleToRoleIds(tmp);
//    }
//    @XmlAttribute
//    public SearchList setSelected(final Boolean pSelected) {
//        boolean value = false;
//        if (pSelected != null) {
//            value = pSelected;
//        }
//        selected = value;
//        return this;
//    }
//    @XmlAttribute
//    public final SearchList setName(final String pName) {
//        this.name = pName == null ? "" : pName.trim();
//        return this;
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
    public final SearchListProperties setList(final String pListType) {
        SearchListTypeEn type = SearchListTypeEn.findById(pListType);
        return setList(type);
    }

    @XmlAttribute
    public final SearchListProperties setList(final SearchListTypeEn pListType) {
        this.listType = pListType;
        return this;
    }

    public SearchListTypeEn getList() {
        return this.listType;
    }

    @XmlElementWrapper(name = "Columns")
    @XmlElement(name = "Column")
    public Set<ColumnOption> getColumns() {
        return columnOptions;
    }

    public List<ColumnOption> getColumnsOrdered() {
        List<ColumnOption> options = new ArrayList<>();
        options.addAll(columnOptions);
        Collections.sort(options, new Comparator<ColumnOption>() {
            @Override
            public int compare(ColumnOption o1, ColumnOption o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });
        return options;
    }

    public List<ColumnOption> getColumnsSorted() {
        List<ColumnOption> options = new ArrayList<>();
        options.addAll(columnOptions);
        Collections.sort(options, Comparator.comparingInt(ColumnOption::getSortNumber));
        return options;
    }

    public ColumnOption getColumn(final String pName) {
        String lName = (pName == null) ? "" : pName.trim();
        Iterator<ColumnOption> iterator = columnOptions.iterator();
        while (iterator.hasNext()) {
            ColumnOption columnOption = iterator.next();
            if (columnOption != null && columnOption.attributeName.equalsIgnoreCase(lName)) {
                return columnOption;
            }
        }
        return null;
    }

    public boolean removeColumn(final String pName) {
        ColumnOption columnOption = getColumn(pName);
        if (columnOption == null) {
            return false;
        }
        return columnOptions.remove(columnOption);
    }

    public SearchListProperties addColumn(final ColumnOption pColumnOption) {
        if (pColumnOption != null) {
            if (columnOptions.contains(pColumnOption)) {
                columnOptions.remove(pColumnOption);
            }
            //Only save invisible columns (it's not neccessary to save visible columns, too)!
            //if (pColumnOption.shouldShow) {
            columnOptions.add(pColumnOption);
            //}
        }
        return this;
    }

    public SearchListProperties setColumns(final Set<ColumnOption> pColumnOptions) {
        if (pColumnOptions == null) {
            columnOptions = new HashSet<>();
        } else {
            columnOptions = new HashSet<>(pColumnOptions);
        }
        return this;
    }

    @XmlElementWrapper(name = "Filters")
    @XmlElement(name = "Filter")
    public Set<FilterOption> getFilters() {
        return filterOptions;
    }

    public List<FilterOption> getFilter(final String pName) {
        List<FilterOption> filters = new ArrayList<>();
        String pname = (pName == null) ? "" : pName.trim();
        Iterator<FilterOption> iterator = filterOptions.iterator();
        while (iterator.hasNext()) {
            FilterOption filterOption = iterator.next();
            if (filterOption != null && filterOption.field.equalsIgnoreCase(pname)) {
                filters.add(filterOption);
            }
        }
        Collections.sort(filters);
        return filters;
    }

//    public FilterOption getFilter(final String pName, final int pCounter) {
//        String lName = (pName == null) ? "" : pName.trim();
//        Iterator<FilterOption> iterator = filterOptions.iterator();
//        while (iterator.hasNext()) {
//            FilterOption filterOption = iterator.next();
//            if (filterOption != null && filterOption.field.equalsIgnoreCase(lName)
//                    && filterOption.getCounter() == pCounter) {
//                return filterOption;
//            }
//        }
//        return null;
//    }
    public void removeFilter(final String pName) {
        List<FilterOption> lFilterOptions = getFilter(pName);
        filterOptions.removeAll(lFilterOptions);
//        for (FilterOption filterOption : lFilterOptions) {
//            lFilterOptions.remove(filterOption);
//        }
    }

//    public boolean removeFilter(final String pName, final int pCounter) {
//        FilterOption filterOption = getFilter(pName, pCounter);
//        if (filterOption == null) {
//            return false;
//        }
//        return filterOptions.remove(filterOption);
//    }
    public SearchListProperties addFilter(final FilterOption pFilterOption) {
        if (pFilterOption != null) {
            if (filterOptions.contains(pFilterOption)) {
                filterOptions.remove(pFilterOption);
            }
            //Only save filters with values (it doesn't make sense to save filter with empty values)!
            if ((pFilterOption.getValue() != null && !pFilterOption.getValue().isEmpty())) { // || pFilterOption.getCounter() > 0) {
                filterOptions.add(pFilterOption);
            }
        }
        return this;
    }

    public SearchListProperties setFilters(final Set<FilterOption> pFilterOptions) {
        if (pFilterOptions == null) {
            filterOptions = new HashSet<>();
        } else {
            filterOptions = new HashSet<>(pFilterOptions);
        }
        return this;
    }

//    @Override
//    public boolean equals(final Object pAnotherWorkingList) {
//        if (pAnotherWorkingList == null) {
//            return false;
//        }
//
//        if (pAnotherWorkingList == this) {
//            return true;
//        }
//
////    if (!(pAnotherWorkingList instanceof SearchList)) {
////      return false;
////    }
//        if (this.getClass() != pAnotherWorkingList.getClass()) {
//            return false;
//        }
//
//        SearchListProperties anotherWorkingList = (SearchListProperties) pAnotherWorkingList;
//        return anotherWorkingList.hashCode() == this.hashCode();
//        //return anotherWorkingList.getName().equalsIgnoreCase(this.getName());
//    }
//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 97 * hash + Objects.hashCode(this.name);
//        hash = 97 * hash + Objects.hashCode(this.listType);
//        return hash;
//    }
    public String serialize() {
        return XmlSerializer.serialize(this);
    }
//
//    @Override
//    public String toString() {
//        return this.name;
//    }
//
//    /**
//     * @return the local
//     */
//    public Boolean isLocal() {
//        return local;
//    }

//    /**
//     * @param local the local to set
//     * @return SearchList
//     */
//    @XmlAttribute
//    public SearchListProperties setLocal(Boolean local) {
//        if (SearchListTypeEn.WORKING.equals(listType)) {
//            if (local != null) {
//                this.local = local;
//            }
//        }
//        return this;
//    }
//    /**
//     * Removes and replaces special characters that are not allowed in windows
//     * file names
//     *
//     * @return windows file name
//     */
//    public String getNameForFileName() {
//        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
//    }
//    public boolean isWriteable(final Long pUserId) {
//        if (pUserId == null) {
//            return false;
//        }
//        return pUserId.equals(creation_user_id);
//    }
//
//    //No creation user and not visible to anyone? -> this is a default list (e.g. drg/pepp rule list) that is readonly for everybody!
//    public boolean isReadonlyToAll() {
//        return (creation_user_id == null && visible_to_user_ids.length == 0 && visible_to_role_ids.length == 0);
//    }
//
//    public boolean isReadonly(final Long pUserId, final List<Long> pRoleIds) {
//        if (pUserId == null) {
//            return false;
//        }
//        final boolean writeable = isWriteable(pUserId);
//        if (writeable) {
//            return false;
//        }
//        if (isReadonlyToAll()) {
//            return true;
//        }
//        if (ArrayUtils.contains(visible_to_user_ids, pUserId)) {
//            return true;
//        }
//        if (pRoleIds != null) {
//            for (Long roleId : pRoleIds) {
//                if (roleId == null || roleId.equals(0L)) {
//                    continue;
//                }
//                if (ArrayUtils.contains(visible_to_role_ids, roleId)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * is filter shared to users or roles?
//     *
//     * @return already shared?
//     */
//    public boolean isShared() {
//        return (visible_to_user_ids != null && visible_to_user_ids.length > 0)
//                || (visible_to_role_ids != null && visible_to_role_ids.length > 0);
//    }
//    @Override
//    public SearchListProperties clone() throws CloneNotSupportedException {
//        super.clone();
//        SearchListProperties clonedSl = new SearchListProperties();
////        clonedSl.setList(this.getList());
////        clonedSl.setName(this.getName());
////        clonedSl.setCreationUserId(this.getCreationUserId());
//        clonedSl.setColumns(this.getColumns());
//        clonedSl.setFilters(this.getFilters());
////        clonedSl.setLocal(this.isLocal());
////        clonedSl.setVisibleToUserIds(this.getVisibleToUserIds());
////        clonedSl.setVisibleToRoleIds(this.getVisibleToRoleIds());
//        return clonedSl;
//    }
}

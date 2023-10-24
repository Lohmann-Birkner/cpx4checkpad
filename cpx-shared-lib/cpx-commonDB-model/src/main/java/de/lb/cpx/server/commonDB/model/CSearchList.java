/**
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
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.model.converter.SearchListTypeConverter;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * CPZN CDoctor initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_PZN : Tabelle der PZN
 * (Pharmazentralnummer)</p>
 */
@Entity
@Table(name = "C_SEARCHLIST", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CREATION_USER", "SL_NAME", "SL_TYPE"}, name = "Uni_SearchList")
}, indexes = {
    @Index(name = "IDX_CREATION_USER", columnList = "CREATION_USER", unique = false)})
@SuppressWarnings("serial")
public class CSearchList extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    public static final String SPLITERATOR = ",";
    private static final Logger LOG = Logger.getLogger(CSearchList.class.getName());

    private String slName;
    private SearchListTypeEn slType;
    private String slVisibleToUserIds;
    private String slVisibleToRoleIds;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_SEARCHLIST_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return slName name of search list
     */
    @Column(name = "SL_NAME", length = 50, nullable = false)
    public String getSlName() {
        return this.slName;
    }

    /**
     * name of search list
     *
     * @param slName Column SL_NAME
     */
    public void setSlName(String slName) {
        this.slName = slName;
    }

    /**
     * type of search list (working list, rule list, workflow list)
     *
     * @return slType
     */
    @Column(name = "SL_TYPE", length = 10, nullable = false)
    @Convert(converter = SearchListTypeConverter.class)
    public SearchListTypeEn getSlType() {
        return this.slType;
    }

    /**
     * type of search list (working list, rule list, workflow list)
     *
     * @param slType Column SL_TYPE
     */
    public void setSlType(SearchListTypeEn slType) {
        this.slType = slType;
    }

    /**
     * @return the slVisibleToUserIds
     */
    @Column(name = "SL_VISIBLE_TO_USER_IDS", length = 1000, nullable = true)
    public String getSlVisibleToUserIds() {
        return slVisibleToUserIds;
    }

    /**
     * @param slVisibleToUserIds the slVisibleToUserIds to set
     */
    public void setSlVisibleToUserIds(String slVisibleToUserIds) {
        this.slVisibleToUserIds = toStrList(slVisibleToUserIds);
    }

    /**
     * @return the slVisibleToRoleIds
     */
    @Column(name = "SL_VISIBLE_TO_ROLE_IDS", length = 1000, nullable = true)
    public String getSlVisibleToRoleIds() {
        return slVisibleToRoleIds;
    }

    /**
     * @param slVisibleToRoleIds the slVisibleToRoleIds to set
     */
    public void setSlVisibleToRoleIds(String slVisibleToRoleIds) {
        this.slVisibleToRoleIds = toStrList(slVisibleToRoleIds);
    }

//    private static List<Long> addToList(final Long[] pValues, final Long pNewValue) {
//        List<Long> values = pValues == null ? new ArrayList<>() : Arrays.asList(pValues);
//        if (pNewValue != null && !pNewValue.equals(0L)) {
//            values.add(pNewValue);
//        }
//        return toList(values);
//    }
//
//    private static List<Long> addToList(final List<Long> pValues, final Long pNewValue) {
//        List<Long> values = pValues == null ? new ArrayList<>() : pValues;
//        if (pNewValue != null && !pNewValue.equals(0L)) {
//            values.add(pNewValue);
//        }
//        return toList(values);
//    }
//
//    private static List<Long> toList(final Long[] pValues) {
//        if (pValues == null || pValues.length == 0) {
//            return new ArrayList<>();
//        }
//        final List<Long> result = Arrays.asList(pValues);
//        return toList(result);
//    }
//
//    private static List<Long> toList(final long[] pValues) {
//        if (pValues == null || pValues.length == 0) {
//            return new ArrayList<>();
//        }
//        final List<Long> result = new ArrayList<>();
//        for (long value : pValues) {
//            result.add(value);
//        }
//        return toList(result);
//    }
    private static List<Long> toList(final Collection<Long> pList) {
        final Set<Long> tmp = new TreeSet<>();
        if (pList != null && !pList.isEmpty()) {
            for (final Long id : pList) {
                if (id != null && !id.equals(0L)) {
                    tmp.add(id);
                }
            }
        }
        List<Long> result = new ArrayList<>();
        result.addAll(tmp);
        return result;
    }

//    private static long[] toArray(final List<Long> pValues) {
//        List<Long> tmp = pValues == null ? null : new ArrayList<>(pValues);
//        if (tmp == null || tmp.isEmpty()) {
//            return new long[0];
//        }
//        final long[] result = new long[tmp.size()];
//        for (int i = 0; i < tmp.size(); i++) {
//            Long val = tmp.get(i);
//            result[i] = val;
//        }
//        return result;
//    }
    /**
     * is filter shared to users or roles?
     *
     * @return already shared?
     */
    @Transient
    public boolean isShared() {
        return (slVisibleToUserIds != null && !slVisibleToUserIds.isEmpty())
                || (slVisibleToRoleIds != null && !slVisibleToRoleIds.isEmpty());
    }

//    public void addVisibleUserId(final Long pVisibleUserId) {
//        slVisibleToUserIds = addToList(slVisibleToUserIds, pVisibleUserId);
//    }
//
//    public void addVisibleRoleId(final Long pVisibleRoleId) {
//        slVisibleToRoleIds = addToList(slVisibleToUserIds, pVisibleRoleId);
//    }
//    public void setVisibleToUserIds(final long[] pVisibleUserIds) {
//        setSlVisibleToUserIds(toList(pVisibleUserIds));
//    }
//
//    public void setVisibleToUserIds(final Long[] pVisibleUserIds) {
//        setSlVisibleToUserIds(toList(pVisibleUserIds));
//    }
//
//    public void setVisibleToRoleIds(final long[] pVisibleRoleIds) {
//        setSlVisibleToRoleIds(toList(pVisibleRoleIds));
//    }
//
//    public void setVisibleToRoleIds(final Long[] pVisibleRoleIds) {
//        setSlVisibleToRoleIds(toList(pVisibleRoleIds));
//    }
    private static List<Long> toList(final String pValues) {
        if (pValues == null || pValues.isEmpty()) {
            return new ArrayList<>();
        }
        final Set<Long> result = new TreeSet<>();
        String[] tmp = pValues.split(SPLITERATOR);
        for (String s : tmp) {
            s = s.trim();
            if (s.isEmpty()) {
                continue;
            }
            try {
                long val = Long.parseLong(s);
                if (val != 0) {
                    result.add(val);
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "this cannot be parsed as long: {0}", s);
                LOG.log(Level.FINEST, MessageFormat.format("illegal long value: {0}", s), ex);
            }
        }
        return new ArrayList<>(result);
    }

    private static String toStrList(final Collection<Long> pValues) {
        List<Long> tmp = toList(pValues);
        if (tmp.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Long value : tmp) {
            if (sb.length() > 0) {
                sb.append(SPLITERATOR);
            }
            sb.append(value);
        }
        return sb.toString();
    }

    private static String toStrList(final String pValues) {
        List<Long> tmp = toList(pValues);
        return toStrList(tmp);
    }

    @Transient
    public List<Long> getVisibleToRoleIds() {
        return toList(slVisibleToRoleIds);
    }

    @Transient
    public List<Long> getVisibleToUserIds() {
        return toList(slVisibleToUserIds);
    }

    public void setVisibleToRoleIds(final Collection<Long> pVisibleToRoleIds) {
        slVisibleToRoleIds = toStrList(pVisibleToRoleIds);
    }

    public void setVisibleToUserIds(final Collection<Long> pVisibleToUserIds) {
        slVisibleToUserIds = toStrList(pVisibleToUserIds);
    }

    public boolean isWriteable(final Long pUserId) {
        if (pUserId == null) {
            return false;
        }
        return pUserId.equals(creationUser);
    }

    //No creation user and not visible to anyone? -> this is a default list (e.g. drg/pepp rule list) that is readonly for everybody!
    @Transient
    public boolean isReadonlyToAll() {
        return (creationUser == null && (slVisibleToUserIds == null || slVisibleToUserIds.isEmpty()) && (slVisibleToRoleIds == null || slVisibleToRoleIds.isEmpty()));
    }

    public boolean isReadonly(final Long pUserId, final List<Long> pRoleIds) {
        if (pUserId == null) {
            return false;
        }
        final boolean writeable = isWriteable(pUserId);
        if (writeable) {
            return false;
        }
        if (isReadonlyToAll()) {
            return true;
        }
        final List<Long> visibleToUserIds = getVisibleToUserIds();
        if (visibleToUserIds != null && visibleToUserIds.contains(pUserId)) {
            return true;
        }
        if (pRoleIds != null) {
            final List<Long> visibleToRoleIds = getVisibleToRoleIds();
            for (Long roleId : pRoleIds) {
                if (roleId == null || roleId.equals(0L)) {
                    continue;
                }
                if (visibleToRoleIds != null && visibleToRoleIds.contains(roleId)) {
                    //if (ArrayUtils.contains(visible_to_role_ids, roleId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return slName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.creationUser);
        hash = 67 * hash + Objects.hashCode(this.slName);
        hash = 67 * hash + Objects.hashCode(this.slType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CSearchList other = (CSearchList) obj;
        if (!Objects.equals(this.creationUser, other.creationUser)) {
            return false;
        }
        if (!Objects.equals(this.slName, other.slName)) {
            return false;
        }
        if (this.slType != other.slType) {
            return false;
        }
        return true;
    }

    /**
     * is this an empty default filter (nameless filters are not stored!)?
     *
     * @return is nameless/empty filter?
     */
    @Transient
    public boolean isEmptyFilter() {
        return slName == null || slName.trim().isEmpty();
    }

    /**
     * is this filter stored in database?
     *
     * @param pUserId pass current user id here to check if it is writeable
     * @return storable filter?
     */
    public boolean isPersistable(final Long pUserId) {
        return isWriteable(pUserId) && !isEmptyFilter();
    }

}

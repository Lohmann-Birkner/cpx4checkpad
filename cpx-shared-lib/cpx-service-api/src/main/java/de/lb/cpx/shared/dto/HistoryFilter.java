/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * filter settings in process history
 *
 * @author niemeier
 */
public class HistoryFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String searchText;
    private WmEventTypeEn[] selectedEventTypes;
    private long[] selectedUserIds;

    /**
     * Empty constructor for (de-)serialization
     */
    public HistoryFilter() {
        this("", (WmEventTypeEn[]) null, (long[]) null);
    }

    /**
     * constructor
     *
     * @param pSearchText search text
     * @param pSelectedEventTypes selected event types
     * @param pUserIds selected user ids
     */
    public HistoryFilter(final String pSearchText, final WmEventTypeEn[] pSelectedEventTypes, final long[] pUserIds) {
        setSearchText(pSearchText);
        setSelectedEventTypes(pSelectedEventTypes);
        setSelectedUserIds(pUserIds);
    }

//    /**
//     * constructor
//     *
//     * @param pSearchText search text
//     * @param pSelectedEventTypes selected event types
//     * @param pUserIds selected user ids
//     */
//    public HistoryFilter(final String pSearchText, final List<WmEventTypeEn> pSelectedEventTypes, final long[] pUserIds) {
//        setSearchText(pSearchText);
//        final WmEventTypeEn[] tmp;
//        if (pSelectedEventTypes == null || pSelectedEventTypes.isEmpty()) {
//            tmp = new WmEventTypeEn[0];
//        } else {
//            tmp = new WmEventTypeEn[pSelectedEventTypes.size()];
//            pSelectedEventTypes.toArray(tmp);
//        }
//        setSelectedEventTypes(tmp);
//        setSelectedUserIds(pUserIds);
//    }
    /**
     * copy constructor
     *
     * @param pHistoryFilter history filter
     */
    public HistoryFilter(final HistoryFilter pHistoryFilter) {
        this(pHistoryFilter == null ? "" : pHistoryFilter.getSearchText(),
                pHistoryFilter == null ? null : pHistoryFilter.getSelectedEventTypes(),
                pHistoryFilter == null ? null : pHistoryFilter.getSelectedUserIds());
    }

    /**
     * creates a copy of this instance
     *
     * @return copy of history filter
     */
    @Transient
    public HistoryFilter getCopy() {
        return new HistoryFilter(this);
    }

    /**
     * set search text
     *
     * @param pSearchText search text
     * @return history filter (for method chaining)
     */
    public final HistoryFilter setSearchText(final String pSearchText) {
        searchText = pSearchText == null ? "" : pSearchText.trim();
        return this;
    }

    /**
     * set selected event types (will remove null values)
     *
     * @param pSelectedEventTypes selected event types
     * @return history filter (for method chaining)
     */
    public final HistoryFilter setSelectedEventTypes(final WmEventTypeEn[] pSelectedEventTypes) {
        if (pSelectedEventTypes == null || pSelectedEventTypes.length == 0) {
            selectedEventTypes = new WmEventTypeEn[0];
        } else {
            //will ignore null values here!
            int size = 0;
            for (WmEventTypeEn eventType : pSelectedEventTypes) {
                if (eventType != null) {
                    size++;
                }
            }
            WmEventTypeEn[] tmp = new WmEventTypeEn[size];
            int i = 0;
            for (WmEventTypeEn eventType : pSelectedEventTypes) {
                if (eventType == null) {
                    continue;
                }
                tmp[i++] = eventType;
            }
            //System.arraycopy(pSelectedEventTypes, 0, tmp, 0, pSelectedEventTypes.length);
            selectedEventTypes = tmp;
        }
        return this;
    }

    /**
     * set selected user ids
     *
     * @param pSelectedUserIds selected user ids
     * @return history filter (for method chaining)
     */
    public final HistoryFilter setSelectedUserIds(final long[] pSelectedUserIds) {
        if (pSelectedUserIds == null || pSelectedUserIds.length == 0) {
            selectedUserIds = new long[0];
        } else {
            long[] tmp = new long[pSelectedUserIds.length];
            System.arraycopy(pSelectedUserIds, 0, tmp, 0, pSelectedUserIds.length);
            selectedUserIds = tmp;
        }
        return this;
    }

    /**
     * search text (is never null but an empty string!)
     *
     * @return search text
     */
    @XmlElement(name = "searchText")
    public String getSearchText() {
        return searchText == null ? "" : searchText;
    }

    /**
     * selected event types
     *
     * @return selected event types
     */
    @XmlElementWrapper(name = "selectedEventTypes")
    @XmlElement(name = "eventType")
    public WmEventTypeEn[] getSelectedEventTypes() {
        WmEventTypeEn[] tmp = new WmEventTypeEn[selectedEventTypes.length];
        System.arraycopy(selectedEventTypes, 0, tmp, 0, selectedEventTypes.length);
        return tmp;
    }

    /**
     * selected user ids
     *
     * @return selected user ids
     */
    @XmlElementWrapper(name = "selectedUserIds")
    @XmlElement(name = "id")
    public long[] getSelectedUserIds() {
        long[] tmp = new long[selectedUserIds.length];
        System.arraycopy(selectedUserIds, 0, tmp, 0, selectedUserIds.length);
        return tmp;
    }

    @Override
    public String toString() {
        StringBuilder sbEvents = new StringBuilder();
        if (selectedEventTypes != null) {
            for (WmEventTypeEn eventType : selectedEventTypes) {
                if (sbEvents.length() > 0) {
                    sbEvents.append(", ");
                }
                sbEvents.append(eventType == null ? "null" : eventType.name());
            }
        }
//        StringBuilder sbUsers = new StringBuilder();
//        if (selectedUserIds != null) {
//            for (long userId : selectedUserIds) {
//                if (sbUsers.length() > 0) {
//                    sbUsers.append(", ");
//                }
//                sbUsers.append(userId);
//            }
//        }
        return "HistoryFilter{" + "searchText=" + searchText + ", selectedEventTypes=" + sbEvents.toString() + ", selectedUserIds=" + StringUtils.join(ArrayUtils.toObject(selectedUserIds), ",") + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.searchText);
        hash = 29 * hash + Arrays.deepHashCode(this.selectedEventTypes);
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
        final HistoryFilter other = (HistoryFilter) obj;
        if (!Objects.equals(this.searchText, other.searchText)) {
            return false;
        }
        if (!Arrays.deepEquals(this.selectedEventTypes, other.selectedEventTypes)) {
            return false;
        }
        if (!Arrays.equals(this.selectedUserIds, other.selectedUserIds)) {
            return false;
        }
        return true;
    }

    public boolean hasValues() {
        return selectedEventTypes.length > 0 || selectedUserIds.length > 0 || !searchText.isEmpty();
    }

}

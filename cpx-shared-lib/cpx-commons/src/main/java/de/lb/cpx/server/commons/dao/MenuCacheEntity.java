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
 *    2019  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commons.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * this interface should maybe transformed into an abstract superclass entity
 * similar to AbstractEntity or AbstractCatalogEntity
 *
 * @author Dirk Niemeier
 */
public interface MenuCacheEntity extends Serializable, Comparable<MenuCacheEntity> {

    static final Logger LOG = Logger.getLogger(MenuCacheEntity.class.getName());

    abstract String getName();

    abstract Long getMenuCacheId();

    abstract Date getValidFrom();

    abstract Date getValidTo();

    abstract boolean isValid();

    abstract boolean isValid(Date pDate);

    abstract boolean isDeleted();

    abstract int getSort();

    abstract boolean isInActive();

    abstract boolean isInActive(Date pDate);

    static String getName(final MenuCacheEntity pEntity, final String pName) {
        boolean inactive;
        if (pEntity == null && pName == null) {
            return null;
        }
        if (pEntity == null) {
            inactive = false;
        } else {
            inactive = pEntity.isInActive();
        }
        return (pName == null ? "" : pName) + (inactive ? " - Inaktiv" : "");
    }

    static boolean isValid(final Date pDate, final Date pValidFrom, final Date pValidTo, final boolean isValid) {
        if (!isValid) {
            return false;
        }
        if (pDate == null) {
            return true;
        }
        if (pValidFrom != null && pValidFrom.after(pDate)) {
            return false;
        }
        if (pValidTo != null && pValidTo.before(pDate)) {
            return false;
        }
        return true;
    }

    static <T extends MenuCacheEntity> Map<Long, T> toMap(final Collection<T> pCollection) {
        if (pCollection == null) {
            return null;
        }
        final Map<Long, T> result = new LinkedHashMap<>();
        new ArrayList<>(pCollection).stream().sorted().forEach((item) -> {
            if (item != null) {
                T old = result.put(item.getMenuCacheId(), item);
                if (old != null) {
                    LOG.log(Level.WARNING, "item with menu cache id {0} already exists (name is {1})", new Object[]{old.getMenuCacheId(), old.getName()});
                }
            }
        });
        return result;
    }

    @Override
    public default int compareTo(final MenuCacheEntity other) {
        int c = Integer.compare(this.getSort(), other.getSort());
        if (c != 0) {
            return c;
        }
        final String name1 = StringUtils.trimToEmpty(this.getName());
        final String name2 = StringUtils.trimToEmpty(other.getName());
        return name1.compareToIgnoreCase(name2);
    }

}

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
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author niemeier
 * @param <V> map value type
 */
public abstract class MenuCacheEntryMenuCacheEntity<V extends MenuCacheEntity> extends MenuCacheEntry<Long, V> {

    private static final Logger LOG = Logger.getLogger(MenuCacheEntryMenuCacheEntity.class.getName());
    public static final MenuCacheOptionsEn DEFAULT_OPTION = MenuCacheOptionsEn.IGNORE_INACTIVE;

    /**
     * @param pName name
     * @param pOption filter option
     * @return (internal) id, null if name is not found in map
     */
    public Long getId(String pName, final MenuCacheOptionsEn pOption) {
        if (pName == null) {
            return null;
        }
        Iterator<Long> it = keySet(pOption).iterator();
        List<Long> results = new ArrayList<>();
        while (it.hasNext()) {
            Long next = it.next();
            V obj = get(next);
            if (obj == null) {
                continue;
            }
            if (pName.equals(obj.getName())) {
                results.add(next);
//                return next;
            }
        }
        if (results.isEmpty()) {
            LOG.log(Level.WARNING, "no internal id found for {0} name {1}", new Object[]{getValueName(), pName});
            return null;
        }
        if (results.size() > 1) {
            String ids = StringUtils.join(results, ',');
            LOG.log(Level.WARNING, "multiple internal ids {0} found for {1} name {2} -> pick first one", new Object[]{ids, getValueName(), pName});
        }
        return results.iterator().next();
    }

    public Long getId(String pName) {
        return getId(pName, null);
    }

    public String getName(final Long pKey, final MenuCacheOptionsEn pOption) {
        V obj = get(pKey, pOption);
        return obj == null ? null : obj.getName();
    }

    @Override
    public String getName(final Long pKey) {
        return getName(pKey, null);
    }

    private boolean matches(final V pItem, final Date pDate, final MenuCacheOptionsEn pOption) {
        if (pItem == null) {
            return false;
        }
        final MenuCacheOptionsEn option = pOption == null ? DEFAULT_OPTION : pOption;
//        if (pOption == null) {
//            return true;
//        }
        //final Date date = pDate == null ? new Date() : pDate;
        switch (option) {
            case IGNORE_INVALID:
                return pItem.isValid(pDate);
            case ONLY_INVALID:
                return !pItem.isValid(pDate);
            case IGNORE_DELETED:
                return !pItem.isDeleted();
            case ONLY_DELETED:
                return pItem.isDeleted();
            case IGNORE_INACTIVE:
                return !pItem.isInActive(pDate);
            //return pItem.isValid(date) && !pItem.isDeleted();
            case ONLY_INACTIVE:
                return pItem.isInActive(pDate);
            //return !pItem.isValid(date) || pItem.isDeleted();
            case ALL:
                return true;
            default:
                LOG.log(Level.WARNING, "illegal menu cache option found: {0}", option.name());
                return true;
        }
    }

    @Override
    public Map<Long, V> map() {
        return map(new Date(), DEFAULT_OPTION); //ignore deleted or invalid entries
    }

    public Map<Long, V> map(final Date pDate) {
        return map(pDate, DEFAULT_OPTION); //ignore deleted or invalid entries
    }

    public Map<Long, V> map(final MenuCacheOptionsEn pOption) {
        return map(new Date(), pOption); //ignore deleted or invalid entries
    }

    public Map<Long, V> map(final Date pDate, final MenuCacheOptionsEn pOption) {
        final Map<Long, V> result = super.map();
        if (result == null) {
            return result;
        }
        final Iterator<Map.Entry<Long, V>> it = result.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, V> entry = it.next();
            V item = entry.getValue();
            if (!matches(item, pDate, pOption)) {
                it.remove();
            }
        }
        return result;
    }

    public Set<Map.Entry<Long, V>> entrySet(final MenuCacheOptionsEn pOption) {
        final Map<Long, V> m = map(pOption);
        return m == null ? null : m.entrySet();
    }

    public List<V> values(final MenuCacheOptionsEn pOption) {
        final Map<Long, V> m = map(pOption);
        return m == null ? null : Lists.newArrayList(m.values());
    }

    public List<V> values(final Date pDate, final MenuCacheOptionsEn pOption) {
        final Map<Long, V> m = map(pDate, pOption);
        return m == null ? null : Lists.newArrayList(m.values());
    }

    public List<V> values(final Date pDate) {
        final Map<Long, V> m = map(pDate);
        return m == null ? null : Lists.newArrayList(m.values());
    }

    public Iterable<Long> keySet(final MenuCacheOptionsEn pOption) {
        final Map<Long, V> m = map(pOption);
        return m == null ? null : m.keySet();
    }

    public V get(final Long pKey, final MenuCacheOptionsEn pOption) {
        final Map<Long, V> m = map(pOption);
        return m == null ? null : m.get(pKey);
    }

    public Set<Map.Entry<Long, String>> nameEntrySet(final MenuCacheOptionsEn pOption) {
        final Map<Long, V> m = map(pOption);
        return nameEntrySet(m);
//        if (m == null) {
//            return null;
//        }
//        final Set<Map.Entry<Long, String>> result = new LinkedHashSet<>();
//        final Iterator<Map.Entry<Long, V>> it = m.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<Long, V> entry = it.next();
//            result.add(new Map.Entry<Long, String>() {
//                @Override
//                public Long getKey() {
//                    return entry.getKey();
//                }
//
//                @Override
//                public String getValue() {
//                    return entry.getValue().getName();
//                }
//
//                @Override
//                public String setValue(String arg0) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//
//                @Override
//                public String toString() {
//                    //don't change, (internal) id is passed to search service through this method!
//                    return String.valueOf(getKey());
//                }
//            });
//        }
//        return result;
    }
    public Set<Map.Entry<Long, String>> nameEntrySet(
            final Map<Long, V> pMap){
        if (pMap == null) {
            return null;
        }
        long start = System.currentTimeMillis();
        final Set<Map.Entry<Long, String>> result = new LinkedHashSet<>();
        final Iterator<Map.Entry<Long, V>> it = pMap.entrySet()//.iterator();
                .stream()
                .sorted((Map.Entry<Long, V> o1, Map.Entry<Long, V> o2) -> Integer.valueOf(o1.getValue().getSort()).compareTo(o2.getValue().getSort()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, V> entry = it.next();
            result.add(new Map.Entry<Long, String>() {
                @Override
                public Long getKey() {
                    return entry.getKey();
                }

                @Override
                public String getValue() {
                    return entry.getValue().getName();
                }

                @Override
                public String setValue(String arg0) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String toString() {
                    //don't change, (internal) id is passed to search service through this method!
                    return String.valueOf(getKey());
                }
            });
        }
        LOG.finer("get entry set in " + (System.currentTimeMillis()-start));
        return result;
    }
    public Set<Map.Entry<Long, String>> nameEntrySet() {
        return nameEntrySet(DEFAULT_OPTION);
    }

}

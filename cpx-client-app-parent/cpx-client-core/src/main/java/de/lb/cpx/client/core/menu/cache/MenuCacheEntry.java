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
import de.lb.cpx.shared.lang.Translation;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author niemeier
 * @param <K> map key type
 * @param <V> map value type
 */
public abstract class MenuCacheEntry<K extends Serializable, V extends Serializable> {

    private static final Logger LOG = Logger.getLogger(MenuCacheEntry.class.getName());

    //private final String keyName;
    //private final String valueName;
    private Map<K, V> map = null;

//    /**
//     * @return the keyName
//     */
//    public String getKeyName() {
//        return keyName;
//    }
    /**
     * @return the valueName
     */
    public String getValueName() {
        return getType().getLabel();
    }
    
    public Translation getValueTranslation(){
        return getType().getTranslation();
    }
    /**
     * @return the map (can be null!)
     */
    protected Map<K, V> getMap() {
        return map;
    }

    /**
     * @param pMap the map to set
     */
    private void setMap(final Map<K, V> pMap) {
        this.map = pMap == null ? null : Collections.unmodifiableMap(pMap);
    }

    public boolean clear() {
        if (this.map != null && !this.map.isEmpty()) {
            this.map.clear();
//            this.map = null;
            return true;
        }
        return false;
    }

    public boolean uninitialize() {
        if (this.map != null) {
            this.map = null;
            return true;
        }
        return false;
    }

//    public boolean clearAndUnitialize() {
//        boolean cleared = clear();
//        boolean unitialized = uninitialize();
//        return cleared || unitialized;
//    }
    public int size() {
        return map == null ? 0 : map.size();
    }

    public boolean isEmpty() {
        return map == null ? true : map.isEmpty();
    }

    protected MenuCacheEntry<K, V> getCopy(MenuCacheEntry<K, V> pCopy) {
        //MenuCacheEntry<K, V> entry = new MenuCacheEntry<>(keyName, valueName);
        Map<K, V> m = map;
        pCopy.setMap(m == null ? null : new LinkedHashMap<>(m));
        return pCopy;
    }

    public abstract MenuCacheEntry<K, V> getCopy();

    public boolean isInitialized() {
        return map != null;
    }

    protected abstract Map<K, V> initialize();

    protected MenuCacheRefreshResult<K, V> initialize(final String[] pKeys) {
        LOG.log(Level.WARNING, "partial initialization of specific entries (passed ids: {0}) is not supported for this menu cache ({1}) -> will reinitialize everything!", new Object[]{pKeys, getValueName()});
        final K[] keys = null;
        return new MenuCacheRefreshResult<>(keys, initialize());
    }

    public abstract String getName(final K pKey);

    public void reinitialize() {
        uninitialize();
        initMap();
    }

    public Map<K, V> map() {
        Map<K, V> m = getMap();
        if (m == null) {
            initMap();
        }
        m = getMap();
        return m == null ? m : new LinkedHashMap<>(m);
    }

//    public List<V> remove(final String pKeys) {
//        return remove(split(pKeys));
//    }
    public List<V> remove(final String[] pKeys) {
        LOG.log(Level.WARNING, "partial remove of specific entries (passed ids: {0}) is not implemented for this menu cache ({1})", new Object[]{pKeys, getValueName()});
        return new ArrayList<>();
    }

    public V remove(final K pKey) {
        Map<K, V> m = map();
        if (m == null) {
            return null;
        }
        V old = m.remove(pKey);
        setMap(m);
        return old;
    }

//    protected static String[] split(final String pKeys) {
//        String s = StringUtils.trimToEmpty(pKeys);
//        return s.split(",");
//    }
//    protected static Long[] toLongArray(final String pKeys) {
//        return toLongArray(split(pKeys));
//    }
//    public void refresh(final String pKeys) {
//        refresh(split(pKeys));
//    }
    public void refresh(final String[] pKeys) {
        Map<K, V> m = map();
        if (m == null) {
            return;
        }
        final MenuCacheRefreshResult<K, V> result = initialize(pKeys);
        final Map<K, V> newValues = result.getResponse();
        final K[] requestedKeys = result.getRequestedKeys();
        if (requestedKeys != null) {
            for (final K key : requestedKeys) {
                if (key != null && !newValues.keySet().contains(key)) {
                    //remove entries that are no longer available for this user (maybe entry is deleted or user has no access to it anymore, e.g. on searchlist)
                    m.remove(key);
                }
            }
        }
        m.putAll(newValues);
        //map has to be sorted here again for new keys maybe! Otherwise new entries will appear at the end of the list
        setMap(m);
    }

    private synchronized void initMap() {
        if (map == null) {
            long startTime = System.currentTimeMillis();
            final Map<K, V> m = initialize();
            setMap(m);
            LOG.log(Level.INFO, "cache for {0} initialized in {1} ms with {2} entries", new Object[]{getValueName(), (System.currentTimeMillis() - startTime) + "", m == null ? null : m.size()});
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Map<K, V> m = map();
        return m == null ? null : m.entrySet();
    }

    public List<V> values() {
        Map<K, V> m = map();
        return m == null ? null : Lists.newArrayList(m.values());
    }

    public V get(final K pKey) {
        if (pKey == null) {
            return null;
        }
        Map<K, V> m = map();
        return m == null ? null : m.get(pKey);
    }

//    public K key(final V pValue) {
//        Map<K, V> m = map();
//        return m == null ? null : m.get(pKey);
//    }
    public Iterable<K> keySet() {
        Map<K, V> m = map();
        return m == null ? null : m.keySet();
    }

    public V getValue(final int pIndex) {
        Map<K, V> m = map();
        if (m == null || m.isEmpty()) {
            return null;
        }
        K key = getKey(pIndex);
        if (key == null) {
            return null;
        }
        return m.get(key);
    }

    public List<K> keys() {
        Map<K, V> m = map();
        if (m == null || m.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(m.keySet());
    }

    public K getKey(final int pIndex) {
        List<K> keys = keys();
        if (keys == null || keys.isEmpty()) {
            return null;
        }
        if (keys.size() < pIndex + 1) {
            return null;
        }
        return keys.get(pIndex);
    }

    public K getFirstKey() {
        return getKey(0);
    }

    public V getFirstValue() {
        return getValue(0);
    }

    public abstract MenuCacheEntryEn getType();

}

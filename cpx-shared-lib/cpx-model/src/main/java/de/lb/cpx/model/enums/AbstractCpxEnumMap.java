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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 * @param <T> type
 * @param <TYPE> type
 */
public abstract class AbstractCpxEnumMap<T extends CpxEnumInterface<TYPE>, TYPE> implements CpxEnumMapInterface<T, TYPE> {

    private static final Logger LOG = Logger.getLogger(AbstractCpxEnumMap.class.getName());

    private final Map<TYPE, T> map = new HashMap<>();
    private final Class<T> enumClass;

    public AbstractCpxEnumMap(Class<T> pEnumClass) {
        enumClass = pEnumClass;
    }

    @Override
    public Map<TYPE, T> getMap() {
        return new HashMap<>(map);
    }

    @Override
    public T get(final TYPE pId) {
        if (pId == null) {
            return null;
        }
        if (map.isEmpty()) {
            //initialize first (don't allocate memory as long as mapping feature is not used
            synchronized (this) {
                if (map.isEmpty()) {
                    put(getValues());
                }
            }
        }
        final T result = map.get(pId);
        if (result == null) {
            LOG.log(Level.SEVERE, "cannot find enum of type " + enumClass.getClass().getSimpleName() + " for this value: " + String.valueOf(pId) + " (maybe invalid enum value is stored in database?)");
        }
        return result;
    }

//    @Override
//    public T get(final Integer pId) {
//        return get(pId == null ? null : String.valueOf(pId));
//    }
//    
//    @Override
//    public T put(final String pId, final T pElem) {
//        return map.put(pId, pElem);
//    }
//    @Override
//    public void put(final T[] pElems) {
//        if (pElems == null || pElems.length == 0) {
//            return;
//        }
//        for(T elem: pElems) {
//            map.put(elem.getIdStr(), elem);
//        }
//    }
    @Override
    public T put(final T pElem) {
        if (pElem == null) {
            return null;
        }
        return map.put(pElem.getId(), pElem);
    }

    @Override
    public void put(final T[] pElems) {
        if (pElems == null || pElems.length == 0) {
            return;
        }
        for (T elem : pElems) {
            put(elem);
        }
    }

    @Override
    public void put(final List<T> pElems) {
        if (pElems == null || pElems.isEmpty()) {
            return;
        }
        List<T> l = new ArrayList<>(pElems);
        @SuppressWarnings("unchecked")
        T[] tmp = (T[]) new Object[l.size()];
        l.toArray(tmp);
        put(tmp);
    }

//    @Override
//    public T put(final Integer pId, final T pElem) {
//        return put(pId == null ? null : String.valueOf(pId), pElem);
//    }
    @Override
    public int size() {
        return map.size();
    }

}

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

import java.util.List;
import java.util.Map;

/**
 *
 * @author Dirk Niemeier
 * @param <T> type
 * @param <TYPE> type
 */
public interface CpxEnumMapInterface<T extends CpxEnumInterface<TYPE>, TYPE> {

    /**
     * gets a flat copy of the map
     *
     * @return map with enum entries (flat copy)
     */
    Map<TYPE, T> getMap();

    /**
     * gets an enum entry
     *
     * @param pId id
     * @return old enum entry (can be null)
     */
    T get(final TYPE pId);

//    /**
//     * For your convenience (id is internal handled as string)
//     * @param pId id
//     * @return enum entry
//     */
//    T get(final Integer pId);
    T put(final T pElem);

//    /**
//     * adds an enum entry
//     * @param pId id
//     * @param pElem enum entry
//     * @return old enum entry (can be null)
//     */
//    T put(final String pId, final T pElem);
//    
//    /**
//     * For your convenience (id is internal handled as string)
//     * @param pId id
//     * @param pElem enum entry
//     * @return old enum entry (can be null)
//     */
//    T put(final Integer pId, final T pElem);
    /**
     * adds elements
     *
     * @param pElems array of elements
     */
    void put(final T[] pElems);

    /**
     * For your convenience (elems is internal handled as array)
     *
     * @param pElems array of elements
     */
    void put(final List<T> pElems);

    /**
     * Number of enum entries
     *
     * @return size
     */
    int size();

    /**
     * Enumeration values
     *
     * @return values
     */
    T[] getValues();

}

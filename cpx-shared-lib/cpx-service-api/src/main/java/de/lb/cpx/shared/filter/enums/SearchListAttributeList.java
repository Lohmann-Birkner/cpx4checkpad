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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.shared.filter.enums.SearchListAttribute.CHILD_REL_TYPE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author niemeier
 */
public class SearchListAttributeList implements Iterable<SearchListAttribute> {

    public final CHILD_REL_TYPE childRelType;
    protected final ArrayList<SearchListAttribute> attributes;

//    public SearchListAttributeList(final CHILD_REL_TYPE pChildRelType, final List<SearchListAttribute> pAttributes) {
//        childRelType = pChildRelType;
//        attributes = new ArrayList<>(pAttributes);
//    }
    public SearchListAttributeList(final CHILD_REL_TYPE pChildRelType, final SearchListAttribute... pAttributes) {
        childRelType = pChildRelType == null ? CHILD_REL_TYPE.EQUAL : pChildRelType;
        attributes = pAttributes.length == 0 ? new ArrayList<>() : new ArrayList<>(Arrays.asList(pAttributes));
    }

    public CHILD_REL_TYPE getChildRelType() {
        return childRelType;
    }

    public List<SearchListAttribute> getAttributes() {
        return new ArrayList<>(attributes);
    }

    public boolean isEqual() {
        return CHILD_REL_TYPE.EQUAL.equals(childRelType);
    }

    public boolean isBetween() {
        return CHILD_REL_TYPE.BETWEEN.equals(childRelType);
    }

    public boolean isOpen() {
        return CHILD_REL_TYPE.OPEN.equals(childRelType);
    }

    public boolean isExpired() {
        return CHILD_REL_TYPE.EXPIRED.equals(childRelType);
    }

    public boolean isToday() {
        return CHILD_REL_TYPE.TODAY.equals(childRelType);
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public int size() {
        return attributes.size();
    }

    public SearchListAttribute get(final int index) {
        return attributes.get(index);
    }

    public SearchListAttribute getFirst() {
        return attributes.get(0);
    }

    @Override
    public Iterator<SearchListAttribute> iterator() {
        return attributes.iterator();
    }

}

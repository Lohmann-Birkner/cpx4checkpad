/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019 niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.searchlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author niemeier
 */
public class SearchListUtil implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String SPLITERATOR = ",";
    public static final String JOINERATOR = "&";
    public static final int THRESHOLD = 100;

    private SearchListUtil() {
        //
    }

    public static List<String> split(final String pValues) {
        return split(pValues, SPLITERATOR);
    }

    public static List<String> split(final String pValues, final String pSpliterator) {
        final List<String> result = new ArrayList<>();
        if (pValues == null || pValues.isEmpty()) {
            return result;
        }
        if (pSpliterator == null) {
            result.add(pValues);
            return result;
        }
        for (String value : pValues.split(pSpliterator)) {
            value = value.trim();
            if (value.isEmpty()) {
                continue;
            }
            result.add(value);
        }
//        String[] result = new String[tmp.size()];
//        tmp.toArray(result);
        return result;
    }

}

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
package de.lb.cpx.shared.filter.enums;

import java.util.Map;

/**
 *
 * @author Dirk Niemeier
 */
public class SearchListFormatMap extends SearchListFormat<Class<? extends Map<Long, String>>> {

    private static final long serialVersionUID = 1L;

    public SearchListFormatMap(final Class<? extends Map<Long, String>> pMapClazz) {
        super(pMapClazz);
    }

}

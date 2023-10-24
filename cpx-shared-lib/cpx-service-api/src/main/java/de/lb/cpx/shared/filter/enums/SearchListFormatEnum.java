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

import de.lb.cpx.model.enums.CpxEnumInterface;

/**
 *
 * @author Dirk Niemeier
 */
public class SearchListFormatEnum extends SearchListFormat<Class<? extends CpxEnumInterface<?>>> {

    private static final long serialVersionUID = 1L;

    public SearchListFormatEnum(final Class<? extends CpxEnumInterface<?>> pEnumClazz) {
        super(pEnumClazz);
    }

}

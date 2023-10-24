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

import java.io.Serializable;

/**
 * @author niemeier
 */
public enum MenuCacheOptionsEn implements Serializable {

    IGNORE_INVALID,
    ONLY_INVALID,
    IGNORE_DELETED,
    ONLY_DELETED,
    IGNORE_INACTIVE, //combination of ignore & deleted
    ONLY_INACTIVE, //combination of ignore & deleted
    ALL

}

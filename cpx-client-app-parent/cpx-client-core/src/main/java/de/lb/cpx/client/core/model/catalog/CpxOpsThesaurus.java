/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.catalog;

import de.lb.cpx.server.commonDB.model.COpsThesaurus;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxOpsThesaurus extends COpsThesaurus implements ICpxThesaurus {

    private static final long serialVersionUID = 1L;

    @Override
    public String getPrimKeyNo1() {
        return getKeyNo1();
    }

    @Override
    public String getPrimKeyNo2() {
        return getKeyNo2();
    }

    @Override
    public String getStarKeyNo() {
        return null;
    }

    @Override
    public String getAddKeyNo() {
        return null;
    }

    @Override
    public int getYear() {
        return getOpsYear();
    }
}

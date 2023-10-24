/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.updatedb;

import java.util.Objects;

public final class UpdateQuery {

    public final String query;
    public final int lineCountFrom;
    public final int lineCountTo;
    public final String strategy;

    public UpdateQuery(final String pQuery, final int pLineCountFrom, final int pLineCountTo, final String pStrategy) {
        String queryTmp = (pQuery == null) ? "" : pQuery.trim();
        while (queryTmp.length() > 0 && queryTmp.endsWith(";")) {
            queryTmp = queryTmp.substring(0, queryTmp.length() - 1);
        }
        queryTmp = queryTmp.trim();
        query = queryTmp;
        lineCountFrom = pLineCountFrom;
        lineCountTo = pLineCountTo;
        strategy = (pStrategy == null) ? "" : pStrategy.trim().toLowerCase();
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append(query);
        if (lineCountFrom == lineCountTo) {
            sb.append(" in line " + lineCountFrom);
        } else {
            sb.append(" from line " + lineCountFrom);
            sb.append(" to " + lineCountTo);
        }
        if (!strategy.isEmpty()) {
            sb.append(" with strategy '" + strategy + "'");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return query;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.query);
        hash = 71 * hash + Objects.hashCode(this.strategy);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UpdateQuery other = (UpdateQuery) obj;
        if (!Objects.equals(this.query, other.query)) {
            return false;
        }
        if (!Objects.equals(this.strategy, other.strategy)) {
            return false;
        }
        return true;
    }

}

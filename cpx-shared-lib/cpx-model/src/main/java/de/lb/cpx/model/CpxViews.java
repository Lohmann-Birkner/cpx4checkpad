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
package de.lb.cpx.model;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.dialect.Dialect;

/**
 *
 * @author niemeier
 */
public class CpxViews extends CpxAuxiliaryDatabaseObject {

    private static final long serialVersionUID = 1L;

    @Override
    public String[] sqlCreateStrings(Dialect dlct) {
        if (!isOracle(dlct) && !isSqlsrv(dlct)) {
            return new String[0];
        }

        List<String> queries = new ArrayList<>();
        if (isOracle(dlct)) {
            queries.add("BEGIN EXECUTE IMMEDIATE 'DROP VIEW VW_CASE_DETAILS_EXTERN'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;");
            queries.add("BEGIN EXECUTE IMMEDIATE 'DROP VIEW VW_CASE_DETAILS_LOCAL'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;");
        } else {
            queries.add("IF OBJECT_ID('VW_CASE_DETAILS_EXTERN', 'V') IS NOT NULL DROP VIEW VW_CASE_DETAILS_EXTERN");
            queries.add("IF OBJECT_ID('VW_CASE_DETAILS_LOCAL', 'V') IS NOT NULL DROP VIEW VW_CASE_DETAILS_LOCAL");
        }
        queries.add("CREATE VIEW VW_CASE_DETAILS_EXTERN AS SELECT * FROM T_CASE_DETAILS WHERE LOCAL_FL = 0");
        queries.add("CREATE VIEW VW_CASE_DETAILS_LOCAL AS SELECT * FROM T_CASE_DETAILS WHERE LOCAL_FL = 1");

        String[] list = new String[queries.size()];
        queries.toArray(list);
        return list;
    }

    @Override
    public String[] sqlDropStrings(Dialect dlct) {
        if (!isOracle(dlct) && !isSqlsrv(dlct)) {
            return new String[0];
        }

        List<String> queries = new ArrayList<>();
        queries.add("DROP VIEW T_CASE_DETAILS_EXTERN");
        queries.add("DROP VIEW T_CASE_DETAILS_LOCAL");

        String[] list = new String[queries.size()];
        queries.toArray(list);
        return list;
    }

}

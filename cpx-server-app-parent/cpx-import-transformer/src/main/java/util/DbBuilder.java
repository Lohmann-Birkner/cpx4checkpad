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
package util;

import de.lb.cpx.str.utils.StrUtils;
import static de.lb.cpx.str.utils.StrUtils.cutStr;
import static de.lb.cpx.str.utils.StrUtils.validateDbName;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import line.AbstractLine;
import line.Field;

/**
 *
 * @author Dirk Niemeier
 */
public class DbBuilder {

    private static final Logger LOG = Logger.getLogger(DbBuilder.class.getName());

    public static String getTableName(final String pDatabaseName, final String pTableName) {
        String databaseName = (pDatabaseName == null) ? "" : pDatabaseName.trim();
        if (databaseName.isEmpty()) {
            throw new IllegalArgumentException("No database name given!");
        }
        //validateName(databaseName);
        String tableName = getTableName(pTableName);
        //validateName(tableName);
        return databaseName + "." + tableName;
    }

    public static String getTableName(final String pName) {
        String name = (pName == null) ? "" : pName.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("No table name given!");
        }
        name = StrUtils.getConstantKey(name);
        if (name.endsWith("_LINE")) {
            name = name.substring(0, name.length() - 5).trim();
        }
        name = name.replace("POSITION", "POS");
        name = name.replace("SAP_FI", "FI");
        return name;
    }

    public static String getTableDrop(final String pTableName) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        return "DROP TABLE " + tableName;
    }

    public static String getTableDropSilent(final String pTableName, final boolean pIsOracle) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        if (pIsOracle) {
            return "BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + tableName + "'; EXCEPTION WHEN OTHERS THEN NULL; END;";
        } else {
            //return "DROP TABLE IF EXISTS " + tableName;
            return "IF OBJECT_ID('" + tableName + "', 'U') IS NOT NULL DROP TABLE " + tableName;
        }
    }

    public static String getIndexDropSilent(final String pIndexName, final String pTableName, final boolean pIsOracle) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        String indexName = (pIndexName == null) ? "" : pIndexName.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        if (indexName.isEmpty()) {
            throw new IllegalArgumentException("No index name given");
        }
        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        if (pIsOracle) {
            return "BEGIN EXECUTE IMMEDIATE 'DROP INDEX " + indexName + "'; EXCEPTION WHEN OTHERS THEN NULL; END;";
        } else {
            return "IF OBJECT_ID('" + indexName + "', 'U') IS NOT NULL DROP INDEX " + indexName + " ON " + tableName;
        }
    }

    public static String getIndexCreate(final String pIndexName, final String pTableName, final String pColumnName, final boolean pIsOracle) {
        String indexName = (pIndexName == null) ? "" : pIndexName.trim();
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        String columnName = (pColumnName == null) ? "" : pColumnName.trim();
        if (indexName.isEmpty()) {
            throw new IllegalArgumentException("No index name given");
        }
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        if (columnName.isEmpty()) {
            throw new IllegalArgumentException("No column name given");
        }
        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        return "CREATE INDEX " + indexName + " ON " + tableName + "(" + columnName + ") " + (pIsOracle ? "NOLOGGING PARALLEL 5" : "");
    }

    public static String getIndexCreateSilent(final String pIndexName, final String pTableName, final String pColumnName, final boolean pIsOracle) {
        final String createIndexSql = getIndexCreate(pIndexName, pTableName, pColumnName, pIsOracle);
        if (pIsOracle) {
            return "BEGIN EXECUTE IMMEDIATE '" + createIndexSql + "'; EXCEPTION WHEN OTHERS THEN NULL; END;";
        } else {
            return "IF NOT EXISTS(SELECT * FROM sys.indexes WHERE object_id = object_id('" + pTableName + "') AND NAME ='" + pIndexName + "') " + createIndexSql;
        }
    }

    public static String getTmpImexTableDefinition(final String pTableName, final Set<Field> pFieldSet, final boolean pIsOracle) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        if (pFieldSet == null) {
            throw new IllegalArgumentException("No fieldset given");
        }
        if (pFieldSet.isEmpty()) {
            throw new IllegalArgumentException("fieldset is empty");
        }
        Iterator<Field> it = pFieldSet.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + tableName + " (");
        boolean first = true;
        while (it.hasNext()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            Field field = it.next();
            String colName = field.getName();
            validateDbName(colName);
            final boolean notNull = (colName.equalsIgnoreCase("NR"));
            sb.append(field.getName() + " " + field.getDbType(pIsOracle) + (notNull ? " NOT NULL" : ""));
        }
        //sb.append("PRIMARY KEY (ID)");
        sb.append(")");
        //sb.append(" CHARACTER SET utf8 COLLATE utf8_unicode_ci");
        return sb.toString();
    }

    public static String getImexPkDefinition(final String pTableName) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        Field pkField = AbstractLine.NR;
        String pkFieldName = pkField.getName();
        return getPkDefinition(pTableName, pkFieldName);

        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        //Field pkField = AbstractLine.NR;
        //String pkFieldName = pkField.getName();
        //return "ALTER TABLE " + tableName + " ADD CONSTRAINT " + cutStr("PK_" + tableName + "_", 30 - pkFieldName.length()) + pkFieldName + " PRIMARY KEY (" + pkFieldName + ")";
    }

    public static String getPkDefinition(final String pTableName) {
        return getPkDefinition(pTableName, "ID");
    }

    public static String getPkDefinition(final String pTableName, final String pFieldName) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        String fieldName = (pFieldName == null) ? "" : pFieldName.trim();
        //String indexPrefix = (pIndexPrefix == null)?"":pIndexPrefix.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("No field name given");
        }
        String indexName = buildIndexName(tableName, fieldName);
        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        return "ALTER TABLE " + tableName + " ADD CONSTRAINT " + indexName + " PRIMARY KEY (" + fieldName + ")";
    }

    public static String dropPkDefinition(final String pTableName) {
        return dropPkDefinition(pTableName, "ID");
    }

    public static String dropPkDefinition(final String pTableName, final String pFieldName) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        String fieldName = (pFieldName == null) ? "" : pFieldName.trim();
        //String indexPrefix = (pIndexPrefix == null)?"":pIndexPrefix.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("No field name given");
        }
        String indexName = buildIndexName(tableName, fieldName);
        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        return "ALTER TABLE " + tableName + " DROP CONSTRAINT " + indexName;
    }

    static private String buildIndexName(final String pTableName, final String pFieldName) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        String fieldName = (pFieldName == null) ? "" : pFieldName.trim();
        String indexName = fieldName;
        return cutStr("PK_" + tableName + "_", 30 - indexName.length()) + indexName;
    }

    public static String getImexPreparedStatement(final String pTableName, final Set<Field> pFieldSet) {
        String tableName = (pTableName == null) ? "" : pTableName.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("No table name given");
        }
        //tableName = DbBuilder.getTableName(tableName);
        //validateName(tableName);
        if (pFieldSet == null) {
            throw new IllegalArgumentException("No fieldset given");
        }
        if (pFieldSet.isEmpty()) {
            throw new IllegalArgumentException("fieldset is empty");
        }
        //Iterator<Field> it = pFieldSet.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + tableName + "(");
        boolean first = true;
        for (Field field : pFieldSet) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(field.getName());
        }
        sb.append(") VALUES (");
        first = true;
        for (Field field : pFieldSet) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append("?");
        }
        sb.append(")");
        return sb.toString();
    }

}

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
 *    2019  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nandola
 */
public class LaboratoryDataListAttributes extends SearchListAttributes {

    private static LaboratoryDataListAttributes instance = null;

    public static final String position = "labPosition";
    public static final String category = "labCategory";
    public static final String lockDel = "labLockdel";
    public static final String date = "labDate";
    public static final String dateEqual = "labDateEqual";
    public static final String dateFrom = "labDateFrom";
    public static final String dateTo = "labDateTo";
    public static final String analysisDate = "labAnalysisDate";
    public static final String analysisDateEqual = "labAnalysisDateEqual";
    public static final String analysisDateFrom = "labAnalysisDateFrom";
    public static final String analysisDateTo = "labAnalysisDateTo";
    public static final String valueOrText = "labText";
    public static final String description = "labDescription";
    public static final String group = "labGroup";
    public static final String kisExtern = "labKisExternKey";
    public static final String comment = "labComment";
    public static final String range = "labRange";
    public static final String area = "labBenchmark";
    public static final String analysis = "labAnalysis";
    public static final String unit = "labUnit";
    public static final String method = "labMethod";
    public static final String value1 = "labValue";
    public static final String value2 = "labValue2";
    public static final String minLimit = "labMinLimit";
    public static final String maxLimit = "labMaxLimit";

    //These Columns are selected if a empty,new Filter is created
    protected static final List<String> DEFAULT_COLUMNS = Arrays.asList(position, category, date, description, group);

    public LaboratoryDataListAttributes() {
        removeAll();
        initKeys();
    }

    public static List<String> getDefaultColumns() {
        return new ArrayList<>(DEFAULT_COLUMNS);
    }

    public List<SearchListAttribute> createDefaultColumns() {
        List<SearchListAttribute> list = new ArrayList<>();
        for (String key : getDefaultColumns()) {
            SearchListAttribute attribute = get(key);
            list.add(attribute);
        }
        return list;
    }

    public static synchronized LaboratoryDataListAttributes instance() {
        if (instance == null) {
            instance = new LaboratoryDataListAttributes();
        }
        return instance;
    }

    protected void initNoColumnKeys() {
    }

    @Override
    public SearchListAttribute get(final String pKey) {
        return getByKey(pKey);
    }

//    @Override
//    public SearchListAttribute getByKey(final String pKey) {
//        return super.getByKey(pKey);
//    }
    @Override
    public List<SearchListAttribute> getByField(final String pField) {
        return super.getByField(pField);
    }

    @Override
    public SearchListAttribute getByQualifiedField(final String pTable, final String pField) {
        SearchListAttribute attr = null;
        String table = (pTable == null) ? "" : pTable.trim();
        String field = (pField == null) ? "" : pField.trim();
        for (SearchListAttribute wla : getByField(field)) {
            if (wla.getDatabaseTable().equalsIgnoreCase(table)) {
                attr = wla;
                break;
            }
        }
        return attr;
    }

    @Override
    public Map<String, SearchListAttribute> getAll() {
        return super.getAll();
    }

    @Override
    public Set<String> getKeys() {
        return super.getKeys();
    }

    @Override
    public Set<SearchListAttribute> getAttributes() {
        return super.getAttributes();
    }

    @Override
    protected final void initKeys() {
//
        add(position, "T_LAB", position, Lang.LAB_DATA_POSITION)
                .setFormat(new SearchListFormatInteger())
                //                .setOperator(SearchListAttribute.OPERATOR.EQUAL)
                .setSize(60);

        add(category, "T_LAB", category, Lang.LAB_DATA_CATEGORY)
                .setFormat(new SearchListFormatInteger())
                //                .setOperator(SearchListAttribute.OPERATOR.EQUAL)
                .setSize(90);

        add(lockDel, "T_LAB", lockDel, Lang.LAB_DATA_LOCK_DEL)
                .setFormat(new SearchListFormatInteger())
                //                .setOperator(SearchListAttribute.OPERATOR.EQUAL)
                .setSize(90);

        add(date, "T_LAB", date, Lang.LAB_DATA_LAB_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(120)
                .addEqualChild(add(dateEqual, getByKey(date), Lang.LAB_DATA_LAB_DATE));
//                .addBetweenChildren(
//                        add(dateFrom, getByKey(date), "Datum von"),
//                        add(dateTo, getByKey(date), "Datum bis")
//                );

        add(analysisDate, "T_LAB", analysisDate, Lang.LAB_DATA_ANALYSIS_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(120)
                .addEqualChild(add(analysisDateEqual, getByKey(analysisDate), Lang.LAB_DATA_ANALYSIS_DATE));
//                .addBetweenChildren(
//                        add(analysisDateFrom, getByKey(analysisDate), "Datum Analyse von"),
//                        add(analysisDateTo, getByKey(analysisDate), "Datum Analyse bis")
//                );

        add(valueOrText, "T_LAB", valueOrText, Lang.LAB_DATA_TEXT)
                .setFormat(new SearchListFormatString())
                .setSize(200);

        add(description, "T_LAB", description, Lang.LAB_DATA_DESCRIPTION)
                .setFormat(new SearchListFormatString())
                .setSize(150);

        add(group, "T_LAB", group, Lang.LAB_DATA_GROUP)
                .setFormat(new SearchListFormatString())
                .setSize(200);

        add(kisExtern, "T_LAB", kisExtern, Lang.LAB_DATA_KIS_EXTERN_KEY)
                .setFormat(new SearchListFormatString())
                .setSize(200);

        add(comment, "T_LAB", comment, Lang.LAB_DATA_COMMENT)
                .setFormat(new SearchListFormatString())
                .setSize(300);

        add(range, "T_LAB", range, Lang.LAB_DATA_RANGE)
                .setFormat(new SearchListFormatString())
                .setSize(300);

        add(area, "T_LAB", area, Lang.LAB_DATA_BENCHMARK)
                .setFormat(new SearchListFormatString())
                .setSize(120);

        add(analysis, "T_LAB", analysis, Lang.LAB_DATA_ANALYSIS)
                .setFormat(new SearchListFormatString())
                .setSize(200);

        add(unit, "T_LAB", unit, Lang.LAB_DATA_UNIT)
                .setFormat(new SearchListFormatString())
                .setSize(120);

        add(method, "T_LAB", method, Lang.LAB_DATA_METHOD)
                .setFormat(new SearchListFormatString())
                .setSize(120);

        add(value1, "T_LAB", value1, Lang.LAB_DATA_VALUE)
                .setFormat(new SearchListFormatDouble())
                .setSize(100);

        add(value2, "T_LAB", value2, Lang.LAB_DATA_VALUE_2)
                .setFormat(new SearchListFormatDouble())
                .setSize(100);

        add(minLimit, "T_LAB", minLimit, Lang.LAB_DATA_MIN_LIMIT)
                .setFormat(new SearchListFormatDouble())
                .setSize(100);

        add(maxLimit, "T_LAB", maxLimit, Lang.LAB_DATA_MAX_LIMIT)
                .setFormat(new SearchListFormatDouble())
                .setSize(100);

    }

}

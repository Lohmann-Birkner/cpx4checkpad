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
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.enums.BooleanEn;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commonDB.dao.CInsuranceCompanyDao;
import de.lb.cpx.server.commonDB.model.CDrgCatalog;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.service.searchlist.SearchListUtil;
import de.lb.cpx.serviceutil.parser.CpxDateParser;
import de.lb.cpx.shared.dto.SearchItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.MdkAuditReasonsMap;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.str.utils.StrUtils;
import de.lb.cpx.wm.model.enums.WmProcessTypeOfServiceEn;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/**
 * Data access object for domain model class VwInputCaseInfo. Initially
 * generated at 27.01.2016 13:51:52 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 * @param <DTO> DTO Type
 */
public abstract class AbstractSearchService<DTO extends SearchItemDTO> {

    private static final Logger LOG = Logger.getLogger(AbstractSearchService.class.getName());
    public static final Pattern WEEKDAY_PATTERN = Pattern.compile("\\$WEEKDAY\\((.*?)\\)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    public static final Pattern QUARTER_PATTERN = Pattern.compile("\\$QUARTER\\((.*?)\\)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    public static final Pattern ISNULL_PATTERN = Pattern.compile("\\$ISNULL\\((.*?)\\)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    public static final Pattern IFNULL_PATTERN = Pattern.compile("\\$IFNULL\\(([^\\,]*?)\\,(.*?)\\)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    protected enum QueryType {
        NORMAL, COUNT_DATA, RAW
    }

    //@Inject
    //@CpxEntityManager
    //private EntityManager entityManager;
    //final public int pageSize = 100;
    @Inject
    private TCaseDao caseDao;
    @Inject
    private CInsuranceCompanyDao insuranceCompanyDao;

    public final Set<String> visibleColumnOptions = new HashSet<>();
    public final SearchListAttributes attr;
    public final String mainTable;
    public final String primaryColumn;
    //public Boolean isSqlsrv = null;
    //public Boolean isOracle = null;
    protected List<CDrgCatalog> entries = null;
    protected GDRGModel grouper;
//    protected String grouperViewSuffix = null;
//    protected String reminderViewSuffix = null;
    protected String depTableSuffix = null;
    private List<FilterOption> mFilterOptionMap = new ArrayList<>();
//    private Map<String, String> insuranceCompanyShortNames = null;

    public AbstractSearchService(final SearchListAttributes pAttr) {
        attr = pAttr;
        SearchListAttribute att = attr.get("ID");
        if (att == null) {
            throw new IllegalArgumentException("You need to define an ID column. It seems to be missing!");
        }
        mainTable = att.getDatabaseTable();
        primaryColumn = att.getQualifiedDatabaseField();
    }

    public Session getSession() {
        //return AbstractCpxDao.getSession(entityManager);
        return caseDao.getSession();
    }

    public boolean isSqlSrv() {
        //if (isSqlsrv == null) {
        //isSqlsrv = AbstractCpxDao.isSqlSrv(entityManager);
        //    isSqlsrv = caseDao.isSqlSrv();
        //}
        //return isSqlsrv;
        return caseDao.isSqlSrv();
    }

    public boolean isOracle() {
        //if (isOracle == null) {
        //isOracle = AbstractCpxDao.isOracle(entityManager);
        //    isOracle = caseDao.isOracle();
        //}
        //return isOracle;
        return caseDao.isOracle();
    }

    public Integer getMaxCount(final boolean pIsLocal) {
        //final boolean isLocal = false;
        List<String> columns = new ArrayList<>();
        List<String> where = new ArrayList<>();
        List<String> order = new ArrayList<>();
        List<String> join = new ArrayList<>();

        columns.add("COUNT(*) CNT");

        String sql = buildSql(pIsLocal, false, columns, join, where, order, QueryType.COUNT_DATA);
        @SuppressWarnings("unchecked")
        NativeQuery<Number> query = getSession().createNativeQuery(sql); //query for < WF13
        //NativeQuery query = getSession().createNativeQuery(sql);
        //query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Number> resultTmp = query.list();
        if (resultTmp == null || resultTmp.isEmpty()) {
            return 0;
        }
        return toInt(resultTmp.get(0));
    }

    protected abstract void prepareSql(final boolean pIsLocal, final boolean pIsShowAllReminders, final List<String> pColumns, final List<String> pJoin, final List<String> pWhere, final List<String> pOrder, final int pLimitFrom, final int pLimitTo, final QueryType pQueryType);

    private String buildSql(final boolean pIsLocal, final boolean pIsShowAllReminders, final List<String> columns, final List<String> join, final List<String> where, final List<String> order, final QueryType pQueryType) {
        return buildSql(pIsLocal, pIsShowAllReminders, columns, join, where, order, -1, -1, pQueryType);
    }

    private String buildSql(final boolean pIsLocal, final boolean pIsShowAllReminders, PrepareResult pPrepareResult, final int pLimitFrom, final int pLimitTo, final QueryType pQueryType) {
        return buildSql(pIsLocal, pIsShowAllReminders, pPrepareResult.columns, pPrepareResult.join, pPrepareResult.where, pPrepareResult.order, pLimitFrom, pLimitTo, pQueryType);
    }

    private String buildSql(final boolean pIsLocal, final boolean pIsShowAllReminders, final List<String> pColumns, final List<String> pJoin, final List<String> pWhere, final List<String> pOrder, final int pLimitFrom, final int pLimitTo, final QueryType pQueryType) {

        //if (!QueryType.RAW.equals(pQueryType)) {
        prepareSql(pIsLocal, pIsShowAllReminders, pColumns, pJoin, pWhere, pOrder, pLimitFrom, pLimitTo, pQueryType);
        //}

        List<String> columns = uniqueList(pColumns);
        List<String> join = uniqueList(pJoin);
        List<String> where = uniqueList(pWhere);
        List<String> order = uniqueList(pOrder);

        final String newLine = "\r\n";
        StringBuilder sb = new StringBuilder();
        //sb.append(" SELECT DISTINCT ");
        sb.append(" SELECT ");

        if (isSqlSrv()) {
            String limitEqualsTmp = getFilterValue(SearchListAttributes.rowNumEqual);
            String limitFromTmp = getFilterValue(SearchListAttributes.rowNumFrom);
            String limitToTmp = getFilterValue(SearchListAttributes.rowNumTo);

            if (pLimitFrom >= 0 || !limitEqualsTmp.isEmpty() || !limitFromTmp.isEmpty() || !limitToTmp.isEmpty()) {
                StringBuilder orderSb = new StringBuilder();
                int k = 0;
                for (String str : order) {
                    k++;
                    int pos = str.lastIndexOf(' ');
                    String s = str.substring(0, pos);
                    if (k > 1) {
                        orderSb.append(", ");
                    }
                    orderSb.append("CASE WHEN " + s + " IS NULL THEN 1 ELSE 0 END, ");
                    orderSb.append(str);
                }
                if (orderSb.length() <= 0) {
                    //orderSb.append("T_CASE.ID");
                    orderSb.append(primaryColumn);

                }
                //NULLS LAST
                columns.add("ROW_NUMBER() OVER (ORDER BY " + orderSb.toString() + ") ROWCOUNTER ");
            }
        }

        int k = 0;
        for (String str : columns) {
            k++;
            sb.append(newLine).append("   ").append(str).append((k < columns.size() ? "," : ""));
        }

        //sb.append(newLine + " FROM T_CASE");
        sb.append(newLine).append(" FROM ");
        sb.append(mainTable);

        for (String str : join) {
            sb.append(newLine).append(" ").append(str);
        }

        if (!where.isEmpty()) {
            sb.append(newLine).append(" WHERE");
        }

        k = 0;
        for (String str : where) {
            k++;
            sb.append((k > 1 ? newLine : "")).append(" ").append((k > 1 ? "  AND " : "")).append(str);
        }

        if (pLimitFrom >= 0 && isOracle()) {
            if (!order.isEmpty()) {
                sb.append(newLine).append(" ORDER BY");
            }

            k = 0;
            for (String str : order) {
                k++;
                // TODO
                // not good but works up to 4000 chars
                //without TO_CHAR  Exception :  ORA-00932: inconsistent datatypes: expected - got CLOB
//                 if (str.startsWith("VIEW_LAST_ACTION.ACTION_COMMENT")) {
//                     str=" TO_CHAR(VIEW_LAST_ACTION.ACTION_COMMENT) ASC ";
//                 }
//                if (str.startsWith("T_WM_PROCESS.WORKFLOW_NUMBER")) {
//                    String ascOrdesc = str.toLowerCase().contains("asc") ? "asc" : "desc";
//                    str = "to_number(regexp_substr(T_WM_PROCESS.WORKFLOW_NUMBER, '^[[:digit:]]*')) " + ascOrdesc;
//                }
                sb.append((k > 1 ? ", " + newLine + "          " : " ")).append(str);
                sb.append(" NULLS LAST");
            }
        }

        String sql = sb.toString();

        String limitEqualsTmp = getFilterValue(SearchListAttributes.rowNumEqual);
        String limitFromTmp = getFilterValue(SearchListAttributes.rowNumFrom);
        String limitToTmp = getFilterValue(SearchListAttributes.rowNumTo);

        if (!limitFromTmp.isEmpty()) {
            try {
                limitFromTmp = (Integer.parseInt(limitFromTmp) - 1) + "";
            } catch (NumberFormatException ex) {
                LOG.log(Level.INFO, "This is not a valid integer: " + limitFromTmp, ex);
            }
        }

        String limitFrom = limitFromTmp;
        String limitTo = limitToTmp;

        if (limitFromTmp.isEmpty() && limitToTmp.isEmpty() && pLimitFrom >= 0) {
            limitFrom = pLimitFrom + "";
        }

        if (limitToTmp.isEmpty() && limitFromTmp.isEmpty() && pLimitTo >= 0) {
            limitTo = pLimitTo + "";
        }

        if (!limitEqualsTmp.isEmpty() || !limitFrom.isEmpty() || !limitTo.isEmpty()) {
            if (isOracle()) {
                //Oracle
                StringBuilder sb1 = new StringBuilder("SELECT TMP2.* FROM (SELECT TMP.*, ROWNUM ROWCOUNTER FROM (");
                sb1.append(newLine).append(sql).append(newLine).append(") TMP) ").append(newLine).append(" TMP2 WHERE (");
                String operator = " AND ";
                List<String> rownums = new ArrayList<>();
                if (limitEqualsTmp.isEmpty()) {
                    if (!limitFrom.isEmpty()) {
                        //sb1.append("TMP2.ROWCOUNTER > ").append(pLimitFrom);
                        rownums.add("TMP2.ROWCOUNTER > " + limitFrom);
                    }
                    if (!limitTo.isEmpty()) {
                        //sb1.append("TMP2.ROWCOUNTER > ").append(pLimitFrom);
                        rownums.add("TMP2.ROWCOUNTER <= " + limitTo);
                    }
                } else {
                    String[] tmp = limitEqualsTmp.split(",");
                    operator = " OR ";
                    for (String val : tmp) {
                        val = val.trim();
                        if (!val.isEmpty()) {
                            rownums.add("TMP2.ROWCOUNTER = " + val);
                        }
                    }
                }
                //sql = sb1.toString();
//                if (pLimitTo >= 0) {
//                    sql += " AND TMP2.ROWCOUNTER <= " + pLimitTo;
//                }
                sql = sb1.toString() + String.join(operator, rownums) + ")";
            } else if (isSqlSrv()) {
                //SQL Server
                sql = "SELECT TMP.* FROM (" + newLine
                        + sql + newLine
                        + ") TMP WHERE (";

                String operator = " AND ";
                List<String> rownums = new ArrayList<>();
                if (limitEqualsTmp.isEmpty()) {
                    if (!limitFrom.isEmpty()) {
                        //sb1.append("TMP2.ROWCOUNTER > ").append(pLimitFrom);
                        rownums.add("TMP.ROWCOUNTER > " + limitFrom);
                    }
                    if (!limitTo.isEmpty()) {
                        //sb1.append("TMP2.ROWCOUNTER > ").append(pLimitFrom);
                        rownums.add("TMP.ROWCOUNTER <= " + limitTo);
                    }
                } else {
                    String[] tmp = limitEqualsTmp.split(",");
                    operator = " OR ";
                    for (String val : tmp) {
                        val = val.trim();
                        if (!val.isEmpty()) {
                            rownums.add("TMP.ROWCOUNTER = " + val);
                        }
                    }
                }
                sql += String.join(operator, rownums) + ")";
//                TMP.ROWCOUNTER > " + pLimitFrom;
//
//                if (pLimitTo >= 0) {
//                    sql += " AND TMP.ROWCOUNTER <= " + pLimitTo;
//                }

                if (!order.isEmpty() && !QueryType.COUNT_DATA.equals(pQueryType)) {
                    sb = new StringBuilder();
                    sb.append(newLine + " ORDER BY");

//                    k = 0;
//                    for (String str : order) {
//                        k++;
//                        int idx = str.indexOf(".");
//                        if (idx > -1) {
//                            str = str.substring(idx + 1);
//                        }
//                        sb.append((k > 1 ? ", " + newLine + "          " : " ")).append(str);
//                    }
                    sb.append(" ROWCOUNTER");

                    sql += sb.toString();
                }

            }

            //where.add("ROWNUM > " + pageStart);
            //where.add("ROWNUM <= " + pageEnd);
        }

        if (QueryType.COUNT_DATA.equals(pQueryType)) {
            //sql = "SELECT COUNT(*) CNT FROM ( " + sql + ") TMP3";
            SearchListAttribute att = attr.get("id");
            sql = "SELECT COUNT(DISTINCT " + att.getUniqueDatabaseField() + ") CNT FROM ( " + sql + ") TMP3";
        }

        sql = replaceVars(sql);

        LOG.log(Level.INFO, "{0} query:\r\n{1}", new Object[]{this.getClass().getSimpleName(), sql});

        return sql;
    }

    public List<FilterOption> getFilterOptions() {
        return mFilterOptionMap == null ? null : new ArrayList<>(mFilterOptionMap);
    }

    public boolean isFiltered(final String pColName) {
        for (FilterOption filterOption : getFilterOptions()) {
            if (filterOption == null) {
                continue;
            }
            if (filterOption.field.equalsIgnoreCase(pColName) && (filterOption.getValue() != null && !filterOption.getValue().isEmpty())) {
                return true;
            }
        }
        return false;
    }

    public String getFilterValue(final String pColName) {
        for (FilterOption filterOption : getFilterOptions()) {
            if (filterOption == null) {
                continue;
            }
            if (filterOption.field.equalsIgnoreCase(pColName)) {
                return filterOption.getValue();
            }
        }
        return null;
    }

    public String getQuery(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, Map<ColumnOption, List<FilterOption>> pFilterOptionMap) {
        return getQuery(pIsLocal, pIsShowAllReminders, pGrouperModel, pPage, pFetchSize, pFilterOptionMap, QueryType.RAW);
    }

    public String getQuery(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, Map<ColumnOption, List<FilterOption>> pFilterOptionMap, final QueryType pQueryType) {
        PrepareResult prepareResult = prepare(pIsLocal, pIsShowAllReminders, pGrouperModel, pPage, pFetchSize, pFilterOptionMap);
        String sql = buildSql(pIsLocal, pIsShowAllReminders, prepareResult, prepareResult.limitFrom, prepareResult.limitTo, pQueryType);
        return sql;
    }

    private PrepareResult prepare(final boolean pIsLocal, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, Map<ColumnOption, List<FilterOption>> pFilterOptionMap) {
        return prepare(pIsLocal, false, pGrouperModel, pPage, pFetchSize, pFilterOptionMap);
    }

    private PrepareResult prepare(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, Map<ColumnOption, List<FilterOption>> pFilterOptionMap) {
        final List<String> columns = new ArrayList<>();
        final List<String> where = new ArrayList<>();
        final List<String> join = new ArrayList<>();
        final List<String> order = new ArrayList<>();

        List<FilterOption> options = getFilterOptionsFromMap(pFilterOptionMap);
        mFilterOptionMap = options;
        if (pGrouperModel != null) {
            this.grouper = pGrouperModel;
//            grouperViewSuffix = grouper.name();
        }
//        //RSH: 19042018 CPX-857
//        CpxServerConfigLocal cpxServerConfig = ClientManager.lookup(CpxServerConfigLocal.class);
//        if (cpxServerConfig.getReminderConfig()) {
//            reminderViewSuffix = "ALL";
//        } else {
//            reminderViewSuffix = "OLDEST";
//        }
        visibleColumnOptions.clear();
        for (ColumnOption colOption : pFilterOptionMap.keySet()) {
//            if(colOption.attributeName.equals(WorkingListAttributes.caseFees)){
//                Logger.getLogger(getClass().getSimpleName());
//            }
            if (!colOption.isShouldShow()) {
                continue;
            }
            //CPX-529 check if current column should be considert if it is no_column
            if (attr.get(colOption.attributeName) != null) {
                if (attr.get(colOption.attributeName).isNoColumn()) {
                    //only add if filter is set for that value
                    if (!pFilterOptionMap.get(colOption).isEmpty()) {
                        visibleColumnOptions.add(colOption.attributeName);
                    }
                } else {
                    visibleColumnOptions.add(colOption.attributeName);
                }
            }
        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.insInsCompanyShortName)) {
//            for (FilterOption filterOption : options) {
//                if (filterOption.field.equalsIgnoreCase(WorkingListAttributes.insInsCompanyShortName)) {
//                    initializeInsuranceShortNamesMap(filterOption.getValue());
//                    break;
//                }
//            }
//        }

        Map<Integer, ColumnOption> sortColumns = new TreeMap<>();
        for (ColumnOption colOption : pFilterOptionMap.keySet()) {
            if (colOption.getSortNumber() != null && colOption.getSortType() != null) {
                if (colOption.attributeName.endsWith("_days")) {
                    String name = colOption.attributeName.substring(0, colOption.attributeName.lastIndexOf('_'));
                    for (ColumnOption colOption2 : pFilterOptionMap.keySet()) {
                        if (colOption2 == null) {
                            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Param is not initialized");
                            continue;
                        }
                        if (colOption2.attributeName.equals(name)) {
                            colOption2.setSortNumber(colOption.getSortNumber());
                            colOption2.setSortType(colOption.getSortType());
                            sortColumns.put(colOption2.getSortNumber(), colOption2);
                        }
                    }
                } else if (visibleColumnOptions.contains(colOption.attributeName)) {
                    sortColumns.put(colOption.getSortNumber(), colOption);
                }
            }
        }

        //Set<ColumnOption> columnOptions = filterOptionMap.keySet();
        Iterator<SearchListAttribute> it3 = attr.getAttributes().iterator();
        while (it3.hasNext()) {
            SearchListAttribute att = it3.next();
            if (att.isVisible()) {
                continue;
            }
            String field = att.getDatabaseField().toLowerCase();
            if (field.equalsIgnoreCase("id") || field.endsWith("_id")) {
                columns.add(att.getQualifiedDatabaseField() + " " + att.getUniqueDatabaseField());
            }
        }

        //columns.add("T_CASE.ID"); //Fall-ID
        //columns.add(attr.get("id").getQualifiedDatabaseField()); //Fall-ID
        Iterator<String> it2 = visibleColumnOptions.iterator();
        while (it2.hasNext()) {
            String colName = it2.next();
            SearchListAttribute wla = attr.get(colName);
            if (wla == null || wla.isNoFilter()) {
                continue;
            }
//            if (colName.equals(WorkingListAttributes.csDrg) || colName.equals(WorkingListAttributes.cwEffective) || colName.equals(WorkingListAttributes.grpresGroup)) {
//                //use the View wich corresponds to the Current grouper
//                //wla.database_table = wla.database_table.replaceAll("GDRG[0-9]{1,8}|AUTOMATIC", grouperViewSuffix);
//                wla.setDatabaseTable(wla.getDatabaseTable().replaceAll("GDRG[0-9]{1,8}|AUTOMATIC", grouperViewSuffix));
//                for (SearchListAttribute wlaChild : wla.getChildren()) {
//                    wlaChild.setDatabaseTable(wlaChild.getDatabaseTable().replaceAll("GDRG[0-9]{1,8}|AUTOMATIC", grouperViewSuffix));                    
//                }
//                //wla.qualified_database_field = wla.qualified_database_field.replaceAll("GDRG[0-9]{1,8}|AUTOMATIC", grouperViewSuffix);
//            }

//            //RSH: 19042018 CPX-857
//            if (colName.equals(WorkflowListAttributes.assSender)
//                    || colName.equals(WorkflowListAttributes.assReceiver)
//                    || colName.equals(WorkflowListAttributes.assSubject)
//                    || colName.equals(WorkflowListAttributes.remFinished)
//                    || colName.equals(WorkflowListAttributes.remLatestCreationDate)
//                    || colName.equals(WorkflowListAttributes.wvComment)
//                    || colName.equals(WorkflowListAttributes.wvPrio)) {
//                //wla.database_table = wla.database_table.replaceAll("ALL|OLDEST", reminderViewSuffix);
//                wla.setDatabaseTable(wla.getDatabaseTable().replaceAll("ALL|OLDEST", reminderViewSuffix));
//                //wla.qualified_database_field = wla.qualified_database_field.replaceAll("ALL|OLDEST", reminderViewSuffix);
//                for (SearchListAttribute wlaChild : wla.getChildren()) {
//                    //wlaChild.database_table = wlaChild.database_table.replaceAll("ALL|OLDEST", reminderViewSuffix);
//                    wlaChild.setDatabaseTable(wlaChild.getDatabaseTable().replaceAll("ALL|OLDEST", reminderViewSuffix));
//                    //wlaChild.qualified_database_field = wlaChild.qualified_database_field.replaceAll("ALL|OLDEST", reminderViewSuffix);
//                }
//            }
            String col = wla.getQualifiedDatabaseField();
            //String table = wla.getDatabaseTable();
            if (!wla.getKey().startsWith(SearchListAttributes.rowNum)) {
                if (col.equals("-1") && wla.hasChildren()) {
                    for (SearchListAttribute wlaTmp : wla.getChildren()) {
                        col = wlaTmp.getQualifiedDatabaseField();
                        //table = wlaTmp.getDatabaseTable();
                        if (wlaTmp.isNoColumn()) {
                            //Do not select Fees. Otherwise DISTINCT does not work and rows are displayed multiple times
                            continue;
                        }
                        columns.add(col + " " + wlaTmp.getUniqueDatabaseField());
                    }
                } else {
                    if (wla.getDatabaseTable().isEmpty()) {
                        //Field not implemented yet / not mapped to existing database field, just dummy
                        //col = "-1 " + col;
                        LOG.log(Level.WARNING, "WorkingListAttribute : {0} has no Databasetable!", wla.getDatabaseField());
                        continue;
                    }
                    if (wla.isNoColumn()) {
                        //Do not select Fees. Otherwise DISTINCT does not work and rows are displayed multiple times
                        continue;
                    }
                    columns.add(col + " " + wla.getUniqueDatabaseField());
                }
            }
        }

        for (Map.Entry<Integer, ColumnOption> entry : sortColumns.entrySet()) {
            ColumnOption colOption = entry.getValue();
//            String databaseField = WorkflowListAttributes.getDeadlines().contains(colOption.attributeName)
//                    ? getDatabaseField(colOption.attributeName.substring(0, colOption.attributeName.length() - 4))
//                    : getDatabaseField(colOption.attributeName);

//            if (databaseField.equals("-1")) {
//                continue;
//            }
            SearchListAttribute wla = attr.get(colOption.attributeName);
            if (wla == null) {
                continue;
            }
            String col = wla.getQualifiedDatabaseField();
            //String table = wla.getDatabaseTable();
            if (col.equals("-1") && wla.hasChildren()) {
                for (SearchListAttribute wlaTmp : wla.getChildren()) {
                    //table = wlaTmp.getDatabaseTable();
                    if (wlaTmp.isNoColumn()) {
                        //Do not select Fees. Otherwise DISTINCT does not work and rows are displayed multiple times
                        continue;
                    }
                    col = wlaTmp.getQualifiedDatabaseField();
                    if (wlaTmp.isLongString() && isOracle()) {
                        col = "DBMS_LOB.SUBSTR(" + col + ", 4000)";
                    }
                    order.add(col + " " + colOption.getSortType().toUpperCase());
                }
            } else {
                if (wla.getDatabaseTable().isEmpty()) {
                    //Field not implemented yet / not mapped to existing database field, just dummy
                    //col = "-1 " + col;
                    LOG.log(Level.WARNING, "WorkingListAttribute : {0} has no Databasetable!", wla.getDatabaseField());
                    continue;
                }
                if (wla.isNoColumn()) {
                    //Do not select Fees. Otherwise DISTINCT does not work and rows are displayed multiple times
                    continue;
                }
                col = wla.getQualifiedDatabaseField();
                //without TO_CHAR or DBMS_LOB.SUBSTR  Exception :  ORA-00932: inconsistent datatypes: expected - got CLOB
                if (wla.isLongString() && isOracle()) {
                    col = "DBMS_LOB.SUBSTR(" + col + ", 4000)";
                }
                order.add(col + " " + colOption.getSortType().toUpperCase());
            }

//            if (!databaseField.equals("-1")) {
//                order.add(databaseField + " " + colOption.getSortType().toUpperCase());
//            }
        }

        for (FilterOption next : options) {
            if (next != null) {
//          System.out.println("field " + next.field);
                if (next.field != null && next.getValue() != null) {
                    if (!next.getValue().isEmpty()) {
                        //criteria.add(buildCriteriaForValue(criteriaBuilder,from,next));
                        buildCriteriaForValue(where, next);
                    }
                }
            }
        }

        //final boolean isLocal = false;
        int limitFrom = -1;
        int limitTo = -1;
        if (pPage >= 0) {
            limitFrom = pPage * pFetchSize;
            limitTo = (pPage + 1) * pFetchSize;
        }
        return new PrepareResult(limitFrom, limitTo, columns, where, join, order);
    }

    public SearchResult<DTO> getAllWithCriteriaForFilter(final boolean pIsLocal, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, final Map<ColumnOption, List<FilterOption>> pFilterOptionMap) throws IOException, InterruptedException {
        return getAllWithCriteriaForFilter(pIsLocal, false, pGrouperModel, pPage, pFetchSize, pFilterOptionMap, null);
    }

    public SearchResult<DTO> getAllWithCriteriaForFilter(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, final Map<ColumnOption, List<FilterOption>> pFilterOptionMap) throws IOException, InterruptedException {
        return getAllWithCriteriaForFilter(pIsLocal, pIsShowAllReminders, pGrouperModel, pPage, pFetchSize, pFilterOptionMap, null);
    }

    public SearchResult<DTO> getAllWithCriteriaForFilter(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, final Map<ColumnOption, List<FilterOption>> pFilterOptionMap, final SearchItemCallback<DTO> pCallback) throws IOException, InterruptedException {
        final PrepareResult prepareResult = prepare(pIsLocal, pIsShowAllReminders, pGrouperModel, pPage, pFetchSize, pFilterOptionMap);
//        List<String> columns = new ArrayList<>();
//        List<String> where = new ArrayList<>();
//        List<String> join = new ArrayList<>();
//        List<String> order = new ArrayList<>();

        final boolean retrieveLeftResultCount = false;
        final boolean retrieveTotalResultCount = pCallback == null;

        //Count of left results
        int leftResultCount;
        //leftResultCount = -1; //Don't determine the unseen results count
        if (retrieveLeftResultCount) {
            {
                long startTime = System.currentTimeMillis();
                //columnCount.add("COUNT(*) CNT");

                //String sql = buildSql(pIsLocal, columns, join, where, order, limitTo, -1, true);
                String sql = buildSql(pIsLocal, pIsShowAllReminders, prepareResult, prepareResult.limitTo, -1, QueryType.COUNT_DATA);

                @SuppressWarnings("unchecked")
                NativeQuery<Number> query = getSession().createNativeQuery(sql); //query for < WF13
                //NativeQuery query = getSession().createNativeQuery(sql);
                //query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                List<Number> resultTmp = query.list();
                if (resultTmp == null || resultTmp.isEmpty()) {
                    leftResultCount = 0;
                } else {
                    leftResultCount = toInt(resultTmp.get(0));
                }
                LOG.log(Level.INFO, "fetching left result count (={0}) in {1} seconds", new Object[]{leftResultCount, (System.currentTimeMillis() - startTime) / 1000d});
            }
        }

        //Count of total results
        int totalResultCount;
        {
            long startTime = System.currentTimeMillis();
            //columnCount.add("COUNT(*) CNT");
            String sql = buildSql(pIsLocal, pIsShowAllReminders, prepareResult, -1, -1, QueryType.COUNT_DATA);

            @SuppressWarnings("unchecked")
            NativeQuery<Number> query = getSession().createNativeQuery(sql);
            //query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            List<Number> resultTmp = query.list();
            if (resultTmp == null || resultTmp.isEmpty()) {
                totalResultCount = 0;
            } else {
                totalResultCount = toInt(resultTmp.get(0));
            }
            LOG.log(Level.INFO, "fetching total result (={0}) count in {1} seconds", new Object[]{totalResultCount, (System.currentTimeMillis() - startTime) / 1000d});
        }

        long startTime = System.currentTimeMillis();
        List<DTO> dtoList;
        List<Map<String, Object>> resultTmp;
        {
            long start = System.currentTimeMillis();
            LOG.log(Level.FINE, "Executing working/process-List query");
            String sql = buildSql(pIsLocal, pIsShowAllReminders, prepareResult, prepareResult.limitFrom, prepareResult.limitTo, QueryType.NORMAL);
            @SuppressWarnings("unchecked")
            NativeQuery<Map<String, Object>> query = getSession().createNativeQuery(sql); //query for < WF13
            if (pFetchSize > 0) {
                query.setFetchSize(pFetchSize);
            } else {
                query.setFetchSize(5000);
            }
            //NativeQuery query = getSession().createNativeQuery(sql);            
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            resultTmp = query.list();
            dtoList = new ArrayList<>(resultTmp.size());
            long end = System.currentTimeMillis();
            LOG.log(Level.FINE, "fetch item result (={0}) in {1} seconds", new Object[]{dtoList.size(), (end - start) / 1000d});
        }

        Iterator<Map<String, Object>> it = resultTmp.iterator();
        //Set<SearchListAttribute> attributes = attr.getAttributes();
        Map<String, String> uniqueDatabaseFields = attr.getUniqueDatabaseFields();
        long start = System.currentTimeMillis();
//        final Set<String> visibleColumns = Collections.unmodifiableSet(visibleColumnOptions);
        while (it.hasNext()) {
            Map<String, Object> items = it.next();
            //WorkingListItemDTO dto = new WorkingListItemDTO();
            DTO dto = fillDto(items, uniqueDatabaseFields);
            //dto.setId(toLong((Number) items.get("ID")));
            dto.setId(toLong((Number) items.get(uniqueDatabaseFields.get("id"))));
            if (visibleColumnOptions.contains(WorkingListAttributes.rowNum)) {
                dto.setRowNum(toLong((Number) items.get("ROWCOUNTER")));
            }
            if (visibleColumnOptions.contains(WorkingListAttributes.patDateOfBirth)) {
                //Geburtsdatum
                dto.setPatDateOfBirth((Date) items.get(uniqueDatabaseFields.get(SearchListAttributes.patDateOfBirth)));
            }
            if (visibleColumnOptions.contains(SearchListAttributes.patName)) {
                //Pat.-Nummer
                //columns.add("T_PATIENT.PAT_NUMBER");
                String firstName = (String) items.get(uniqueDatabaseFields.get(SearchListAttributes.patFirstName));
                String secondName = (String) items.get(uniqueDatabaseFields.get(SearchListAttributes.patSecondName));
                firstName = (firstName == null) ? "" : firstName.trim();
                secondName = (secondName == null) ? "" : secondName.trim();

                String name = "";
                if (firstName.isEmpty() && secondName.isEmpty()) {
                    //do nothing
                } else if (firstName.isEmpty()) {
                    name = secondName;
                } else if (secondName.isEmpty()) {
                    name = firstName;
                } else {
                    name = secondName + ", " + firstName;
                }
                dto.setPatName(name);
            }
            if (visibleColumnOptions.contains(SearchListAttributes.insNumber)) {
                dto.setInsNumber((String) items.get(uniqueDatabaseFields.get(SearchListAttributes.insNumber)));
            }

            //}
            if (pCallback != null) {
                pCallback.call(dto /*, visibleColumns */);
            } else {
                dtoList.add(dto);
            }

            //dtoList.add(builder.buildDTOWithColumnOptions(it.next(), colOptions));
            it.remove();
        }
        long end = System.currentTimeMillis();
        LOG.log(Level.FINE, "Done filling DTOs it took {0} Seconds", (end - start) / 1000d);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LOG.log(Level.INFO, "Loading list with {0} entries took {1} seconds", new Object[]{dtoList.size(), (double) duration / 1000});

        //return dtoList;
        final boolean hasMoreResults = dtoList.size() == pFetchSize;
        return new SearchResult<>(dtoList, hasMoreResults, totalResultCount /*, leftResultCount */);
    }

    protected abstract DTO fillDto(final Map<String, Object> pItems, final Map<String, String> pUniqueDatabaseFields);

    private String getDatabaseField(final String pField) {
        SearchListAttribute wla = attr.get(pField);
        if (wla == null) {
            return "-1";
        }
        return wla.getQualifiedDatabaseField();
    }

    private void buildCriteriaForValue(List<String> pWhere, FilterOption pNext) {
        String databaseField = getDatabaseField(pNext.field);

        //String field = WorkingListAttributes.chopKey(next.field);
        SearchListAttribute attribute = attr.get(pNext.field);
        if (attribute == null) {
            LOG.log(Level.INFO, "trying to build criteria failed because SearchListAttribute for {0} was null", pNext.field);
            return;
        }
        if (attribute.isClientSide()) {
            return;
        }
        if (databaseField.equals("-1")) {
            return;
        }
        if (attribute.getDatabaseTable().isEmpty()) {
            //Field not implemented yet / not mapped to existing database field, just dummy
            databaseField = "-1";
        }

        LOG.log(Level.FINE, "try to add criteria name: {0} field: {1} value {2}", new Object[]{pNext.name, pNext.field, pNext.getValue()});
        if (attribute.getKey().equals(WorkingListAttributes.insInsCompanyShortName)
                || attribute.getKey().equals(WorkflowListAttributes.insInsCompanyShortName)) {
//            initializeInsuranceShortNamesMap(values);
            //if (visibleColumnOptions.contains(WorkingListAttributes.insInsCompanyShortName)) {
//            final List<String> insShortNamesList = SearchListUtil.split(values, pSplit);
//            values = String.join(SearchListUtil.SPLITERATOR, getInsuranceShortNamesIdentifiers());
            String value = String.join(SearchListUtil.SPLITERATOR, getInsuranceShortNamesIdentifiers(pNext.getValue()));
            pNext.setValue(value);
        }
        if (!pNext.getValue().contains(",") && !pNext.getValue().contains("&")) {
            //return getCriteria(criteriaBuilder, from, next.name, next.field, next.value);
            String lWhere = getCriteria(databaseField, attribute, pNext.getValue());
            if (lWhere != null && !lWhere.trim().isEmpty()) {
                lWhere = addSubquery(attribute, lWhere);
                pWhere.add(lWhere);
            }
        } else {
            pWhere.add(createWhereString(pNext.getValue(), databaseField, attribute));
        }
//        where.add(createWhereString(next.value, databaseField, attributeName));
    }

//    private boolean isInsuranceShortNamesMapInitialized() {
//        return insuranceCompanyShortNames != null;
//    }
//
//    private void initializeInsuranceShortNamesMap(final String pShortNamesList) {
//        if (isInsuranceShortNamesMapInitialized()) {
//            return;
//        }
//        final List<String> insShortNamesList = SearchListUtil.split(pShortNamesList, SearchListUtil.SPLITERATOR);
//        initializeInsuranceShortNamesMap(insShortNamesList);
//    }
//
//    private void initializeInsuranceShortNamesMap(final List<String> pShortNamesList) {
//        if (isInsuranceShortNamesMapInitialized()) {
//            return;
//        }
//        insuranceCompanyShortNames = insuranceCompanyDao.getInsuCompIdent(pShortNamesList, CountryEn.de.name());
//    }
//    private Map<String, String> getInsuranceShortNamesMap(final List<String> pShortNamesList) {
//        if (insuranceCompanyShortNames == null) {
//            insuranceCompanyShortNames = insuranceCompanyDao.getInsuCompIdent(pShortNamesList, CountryEn.de.name());
//        }
//        return insuranceCompanyShortNames;
//    }
//    protected Collection<String> getInsuranceShortNamesIdentifiers() {
//        return insuranceCompanyShortNames.keySet();
//    }
    protected Collection<String> getInsuranceShortNamesIdentifiers(final String pShortNamesList) {
        final List<String> insShortNamesList = SearchListUtil.split(pShortNamesList, SearchListUtil.SPLITERATOR);
        return insuranceCompanyDao.getInsuCompIdent(insShortNamesList, CountryEn.de.name()).keySet();
    }

//    protected String getInsuranceShortNamesName(final String pInsIdent) {
//        if (insuranceCompanyShortNames == null) {
//            return null;
//        }
//        return insuranceCompanyShortNames.get(pInsIdent);
//    }
    /**
     * creates where part of an query with specialcharacters ','(as or) and
     * '&'(as and)
     *
     * @param pValues values that should be in the where cause of the query
     * @param pDatabaseField specific databasefield where values should be
     * fetched from
     * @param pAttributeName attribute of the searchlist
     * @return converted string for where
     */
    private String createWhereString(String pValues, String pDatabaseField, SearchListAttribute pAttributeName) {
        if (!pValues.contains(",") && !pValues.contains("&")) {
            return "-1";
        }
        if (pAttributeName.getKey().equals(RuleListAttributes.crgrId)) {
            return buildWhereInString(",", pValues, pDatabaseField);
        }
        return buildWherePartString(SearchListUtil.JOINERATOR, pValues, pDatabaseField, pAttributeName);//createAndString(pValues, pDatabaseField, pAttributeName);
    }

    private String buildWhereInString(String pSplit, String pValues, String pDatabaseField) {
        List<String> whereList = new ArrayList<>();

        for (String str : pValues.split(pSplit)) {

            whereList.add("'" + escapeSQL(str) + "'");
        }
        if (!whereList.isEmpty()) {
            return pDatabaseField + " IN ( " + String.join(",", whereList) + ") ";
        }
        return "";
    }

    //build the where part string for split value '&' or ','
    private String buildWherePartString(String pSplit, String pValues, String pDatabaseField, SearchListAttribute pAttributeName) {
        List<String> whereList = new ArrayList<>();
        //counter to determine break, when query should be prevented from going to big
        int threshold = SearchListUtil.THRESHOLD; //previously 5
        int counter = 1;
        for (String str : SearchListUtil.split(pValues, pSplit)) { //pValues.split(pSplit)) {
            String lWhere = "";

            // do we really need this threshold ??
//            if (!pAttributeName.getKey().equals(WorkingListAttributes.rules) || !pAttributeName.getKey().equals(RuleListAttributes.rules)) { //don't apply threshold for the "rules" WorkingListAttributes.
            if (!pAttributeName.getKey().equals(WorkingListAttributes.rules) //don't apply threshold for the "rules" WorkingListAttributes.
                    && !pAttributeName.getKey().equals("id")
                    && !pAttributeName.getKey().equals(WorkingListAttributes.insInsCompanyShortName)
                    && !pAttributeName.getKey().equals(WorkflowListAttributes.insInsCompanyShortName)) { //don't apply threshold for id column
                if (counter > threshold) {
                    LOG.log(Level.WARNING, "Threshold for SubQueries reached! {0}/{1}\n Ignore rest of user input!", new Object[]{counter, threshold});
                    break;
                }
            }

            //first logic or because its weaker than an logical and
            switch (pSplit) {
                case "&":
                    lWhere = buildWherePartString(SearchListUtil.SPLITERATOR, str, pDatabaseField, pAttributeName);
                    lWhere = addSubquery(pAttributeName, lWhere);
                    break;
                case ",":
                    lWhere = getCriteria(pDatabaseField, pAttributeName, str);
                    break;
                default:
                    LOG.log(Level.WARNING, "Unknown spliterator: {0}", pSplit);
            }
            if (lWhere != null && !lWhere.trim().isEmpty() && !whereList.contains(lWhere)) {
                whereList.add(lWhere);
            }
            counter++;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        int i = 0;
        for (String whereTmp : whereList) {
            i++;
            sb.append(whereTmp);
            if (i < whereList.size()) {
                switch (pSplit) {
                    case "&":
                        sb.append(" AND ");
                        break;
                    case ",":
                        sb.append(" OR ");
                        break;
                    default:
                        LOG.log(Level.WARNING, "Unknown spliterator: {0}", pSplit);
                }
            }
        }
        if (whereList.isEmpty()) {
            sb.append("1=1");
        }
        sb.append(")");
        return sb.toString();
    }

    protected String toDate(final String pDate) {
        return toStaticDate(pDate, isSqlSrv());
        /*
      if (isSqlSrv()) {
        return "'" + escapeSQL(pDate.replace("-", "")) + "'";
      }
      return "TO_DATE('" + escapeSQL(pDate) + "', 'YYYY-MM-DD')";
         */
    }

    protected Boolean toBool(final Object pValue) {
        if (pValue == null) {
            Boolean result = null;
            return result;
        }
        if (pValue instanceof Boolean) {
            //Microsoft SQL Server
            return (Boolean) pValue;
        }
        if (pValue instanceof Number) {
            //Oracle
            final Number number = (Number) pValue;
            if (number.byteValue() == 1) {
                return true;
            }
            if (number.byteValue() == 0) {
                return false;
            }
            LOG.log(Level.SEVERE, "This numeric value cannot be identified as true (1) or false (0): {0}. Return null instead!", number.longValue());
            Boolean result = null;
            return result;
        }
        LOG.log(Level.SEVERE, "Class of type {0} is unknown, so I cannot convert value ''{1}'' to boolean. Return null instead!", new Object[]{pValue.getClass().getSimpleName(), String.valueOf(pValue)});
        Boolean result = null;
        return result;
        /*
      if (isSqlSrv()) {
        return "'" + escapeSQL(pDate.replace("-", "")) + "'";
      }
      return "TO_DATE('" + escapeSQL(pDate) + "', 'YYYY-MM-DD')";
         */
    }

    //private Predicate getCriteria(CriteriaBuilder criteriaBuilder,Root<VwInputCaseInfo> from,String attName,String attField,String attValue){
    private String getCriteria(String pDatabaseField, SearchListAttribute pAttribute, String pAttValue) {
        String attField = pAttribute.getKey();
        if (pAttribute.getParent() != null) {
            attField = pAttribute.getParent().getKey();
        }

        Serializable dataType = pAttribute.getDataType();

        if (dataType == null) {
            //this shall not happen!
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Sorry, I''m not able to get Data type class for ''{0}'', will skip this filter", attField);
            return null;
        }

        String attributeValue = (pAttValue == null) ? "" : pAttValue.trim();

        if (attributeValue.isEmpty() /* && !pAttributeName.hasDefaultValue() */) {
            //can happen...
            return null;
        }

        attributeValue = attributeValue.replace('*', '%'); //.replace("(", "").replace(")", "");

        return prepareAttributeValue(attributeValue, pAttribute, pDatabaseField, dataType);
    }

    private List<FilterOption> getFilterOptionsFromMap(Map<ColumnOption, List<FilterOption>> pFilterOptionMap) {
        List<FilterOption> listOfOptions = new ArrayList<>();

        Iterator<ColumnOption> it = pFilterOptionMap.keySet().iterator();
        while (it.hasNext()) {
            listOfOptions.addAll(pFilterOptionMap.get(it.next()));
        }

        return listOfOptions;
    }

    public String replaceVars(final String pSql) {
        if (pSql == null) {
            return pSql;
        }
        if (!pSql.contains("$")) {
            return pSql;
        }
        String sql = pSql;
        if (isOracle()) {
            sql = WEEKDAY_PATTERN.matcher(sql).replaceAll("MOD(TO_CHAR($1, 'D'), 7) + 1");
            sql = IFNULL_PATTERN.matcher(sql).replaceAll("NVL($1,$2)");
            sql = QUARTER_PATTERN.matcher(sql).replaceAll("FLOOR((EXTRACT(MONTH FROM $1) - 1) / 3) + 1");
            //sql = sql.replace("$ISNULL", "NVL");
        } else if (isSqlSrv()) {
            //sql = weekdayPattern.matcher(pSql).replaceAll("WEEKDAY($1, 1)");
            sql = WEEKDAY_PATTERN.matcher(sql).replaceAll("DATEPART(WEEKDAY, $1)");
            sql = IFNULL_PATTERN.matcher(sql).replaceAll("ISNULL($1,$2)");
            sql = QUARTER_PATTERN.matcher(sql).replaceAll("DATEPART(qq, $1)");
            //sql = sql.replace("$ISNULL", "ISNULL");
        }
        sql = ISNULL_PATTERN.matcher(sql).replaceAll("(CASE WHEN $1 IS NULL THEN 0 ELSE 1 END)");
        return sql;
    }

    private String addSubquery(SearchListAttribute pAttributeName, String pAttributeValue) {
//        AWi-20170629-CPX-551:added switch to enable subqueries based on the attribute key
//        possible target for refactoring if additional subqueries are added
        switch (pAttributeName.key) {
            case WorkingListAttributes.caseIcds:
                return " EXISTS (SELECT 1 FROM T_CASE_ICD \n"
                        + " LEFT JOIN T_CASE_DEPARTMENT ON T_CASE_ICD.T_CASE_DEPARTMENT_ID = T_CASE_DEPARTMENT.ID  \n"
                        + " WHERE " + pAttributeValue + " AND T_CASE_ICD.MAIN_DIAG_CASE_FL = 0 AND T_CASE_ICD.MAIN_DIAG_DEP_FL = 0 AND T_CASE_ICD.TO_GROUP_FL = 1 AND T_CASE_DEPARTMENT.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID) \n";
            case WorkingListAttributes.caseOpses:
                return " EXISTS (SELECT 1 FROM T_CASE_OPS \n"
                        + " LEFT JOIN T_CASE_DEPARTMENT ON T_CASE_OPS.T_CASE_DEPARTMENT_ID = T_CASE_DEPARTMENT.ID \n"
                        + " WHERE " + pAttributeValue + " AND T_CASE_OPS.TO_GROUP_FL = 1 AND T_CASE_DEPARTMENT.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID) \n";
            case WorkingListAttributes.departmentMd:
                return " EXISTS (SELECT 1 FROM T_CASE_ICD \n"
                        + " LEFT JOIN T_CASE_DEPARTMENT ON T_CASE_ICD.T_CASE_DEPARTMENT_ID = T_CASE_DEPARTMENT.ID  \n"
                        + " WHERE " + pAttributeValue + " AND T_CASE_ICD.MAIN_DIAG_DEP_FL = 1 AND T_CASE_ICD.TO_GROUP_FL = 1 AND T_CASE_DEPARTMENT.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID ) \n";
            case WorkingListAttributes.hosDiagnosis:
                return " EXISTS (SELECT 1 FROM T_CASE_ICD \n"
                        + " LEFT JOIN T_CASE_DEPARTMENT ON T_CASE_ICD.T_CASE_DEPARTMENT_ID = T_CASE_DEPARTMENT.ID \n"
                        + " WHERE " + pAttributeValue + " AND T_CASE_ICD.ICDC_TYPE_EN = 1 AND T_CASE_ICD.TO_GROUP_FL = 1 AND T_CASE_DEPARTMENT.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID) \n";
            case WorkingListAttributes.admDiagnosis:
                return " EXISTS(SELECT 1 FROM T_CASE_ICD \n"
                        + " LEFT JOIN T_CASE_DEPARTMENT ON T_CASE_ICD.T_CASE_DEPARTMENT_ID = T_CASE_DEPARTMENT.ID \n"
                        + " WHERE " + pAttributeValue + " AND T_CASE_ICD.ICDC_TYPE_EN = 2 AND T_CASE_ICD.TO_GROUP_FL = 1 AND T_CASE_DEPARTMENT.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID) \n";
            case WorkingListAttributes.caseFees:
                return ("EXISTS(SELECT 1 FROM T_CASE_FEE \n"
                        + " WHERE " + pAttributeValue + " AND T_CASE_FEE.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID)");
//            case WorkingListAttributes.caseDepartments:
//                return ("EXISTS(SELECT 1 FROM T_CASE_DEPARTMENT \n"
//                        + " WHERE " + pAttributeValue + " AND T_CASE_DEPARTMENT.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID)");
            case WorkingListAttributes.caseDepartments:
//            case WorkingListAttributes.caseDepartmentsMap:
            case WorkingListAttributes.caseDepartmentsKey301:
                return ("EXISTS(SELECT 1 FROM T_CASE_DEPARTMENT \n"
                        + " WHERE " + pAttributeValue + " AND T_CASE_DEPARTMENT.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID)");
            case WorkingListAttributes.rules:
                return ruleFilterSubQuery(pAttributeValue);
            case WorkflowListAttributes.actionSubjectFilter:
                return " EXISTS(SELECT 1 FROM T_WM_ACTION \n"
                        + " WHERE " + pAttributeValue + " AND T_WM_ACTION.T_WM_PROCESS_ID = T_WM_PROCESS.ID)\n";
            case WorkflowListAttributes.eventTypeFilter:
                return " EXISTS(SELECT 1 FROM T_WM_EVENT \n"
                        + " WHERE " + pAttributeValue + " AND T_WM_EVENT.T_WM_PROCESS_ID = T_WM_PROCESS.ID)\n";
            case WorkflowListAttributes.mdkAuditReasonsFilter:
                return " EXISTS (SELECT 1 FROM T_WM_AUDIT_REASONS \n"
                        + " INNER JOIN T_WM_REQUEST ON T_WM_AUDIT_REASONS.T_WM_REQUEST_ID = T_WM_REQUEST.ID AND T_WM_REQUEST.REQUEST_TYPE = 2 AND T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS.ID \n"
                        + " WHERE " + pAttributeValue + " AND T_WM_AUDIT_REASONS.EXTENDED = 0)\n";
            case WorkflowListAttributes.processFinalAuditReasonsFilter:
                return " EXISTS (SELECT 1 FROM T_WM_AUDIT_REASONS \n"
                        + " INNER JOIN T_WM_PROCESS_HOSPITAL_FINALIS ON T_WM_AUDIT_REASONS.T_WM_PROCESS_FINAL_ID = T_WM_PROCESS_HOSPITAL_FINALIS.ID AND T_WM_PROCESS_HOSPITAL_FINALIS.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS.ID \n"
                        + " WHERE " + pAttributeValue + " )\n";
            case WorkflowListAttributes.requestStatusFilter:
//                return " EXISTS (SELECT 1 FROM T_WM_REQUEST_MDK \n"
//                        + " INNER JOIN T_WM_REQUEST ON T_WM_REQUEST_MDK.ID = T_WM_REQUEST.ID AND T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS.ID \n"
//                        + " WHERE " + pAttributeValue + ") \n";
                return " EXISTS (SELECT 1 FROM T_WM_REQUEST WHERE T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS.ID \n"
                        + " AND " + pAttributeValue + ") \n";

            default:
//                return prepareAttributeValue(attributeValue, pAttributeName, pAttributeName.getQualifiedDatabaseField(), pAttributeName.getDateType());
                return pAttributeValue;
        }
    }

    private String prepareAttributeValue(String pAttributeValue, SearchListAttribute pAttribute, String pDatabaseField, Serializable pDataType) {
        if (Date.class == pDataType || pDataType instanceof Date /* || SearchListFormatDeadLineDateTime.class == pDataType
                || SearchListFormatDeadLineRadioButton.class == pDataType */) {
            //return Restrictions.sqlRestriction("trunc(" + attField + ") = ?", attField, org.hibernate.type.StandardBasicTypes.DATE);
            //Expression expr = criteriaBuilder.function("TRUNC", Date.class, from.get(attField));

            final Date date;
            if (pAttribute.isOpen() || pAttribute.isExpires() || pAttribute.isToday()) {
                Calendar cal = new GregorianCalendar();
                cal.setTime(new Date());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                //cal.add(Calendar.DAY_OF_MONTH, 1);
                date = cal.getTime();
                pAttributeValue = Lang.toIsoDate(date);
                //pAttributeValue = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);
            } else {
                date = CpxDateParser.parseDate(pAttributeValue);
            }
//            if (date == null) {
//                date = (Date) pAttributeName.getDefaultValue();
//            }
            final Date dateFrom = date;

            if (pAttribute.getOperator().equals(SearchListAttribute.OPERATOR.GREATER_THAN_OR_EQUAL_TO)) {
                //return criteriaBuilder.greaterThanOrEqualTo(expr, CpxDateParser.parseDate(attributeValue));
                return pDatabaseField + " >= " + toDate(pAttributeValue);
            }

            Date dateTo_ = new Date(dateFrom.getTime() + TimeUnit.DAYS.toMillis(1));
            String dateTo = dateToString(dateTo_);

            if (pAttribute.getOperator().equals(SearchListAttribute.OPERATOR.LESS_THAN)) {
                return pDatabaseField + " < " + toDate(dateTo);
            }
//            if (pAttributeName.getOperator().equals(SearchListAttribute.OPERATOR.LESS_THAN_OR_EQUAL_TO)) {
//                return pDatabaseField + " <= " + toDate(dateTo);
//            }
            return "( " + pDatabaseField + " >= " + toDate(pAttributeValue) + " AND " + pDatabaseField + " < " + toDate(dateTo) + " )";
        }
        final String operator;
        switch (pAttribute.getOperator()) {
            case LESS_THAN:
                operator = " < ";
                break;
            case LESS_THAN_OR_EQUAL_TO:
                operator = " <= ";
                break;
            case EQUAL:
                operator = " = ";
                break;
            case GREATER_THAN_OR_EQUAL_TO:
                operator = " >= ";
                break;
            default:
                operator = " = ";
        }
        if (pAttribute.getOperator().equals(SearchListAttribute.OPERATOR.LIKE)
                && !pAttribute.isString()) {
            if (!pAttributeValue.contains("%")) {
                pAttributeValue += "%";
            }
            return "CAST(" + pDatabaseField + " AS VARCHAR(255))" + " LIKE '" + escapeSQL(pAttributeValue) + "' ESCAPE '\\' ";
        }
        if (pAttributeValue.contains("%") && !pAttribute.isString()) {
            return "CAST(" + pDatabaseField + " AS VARCHAR(255))" + " LIKE '" + escapeSQL(pAttributeValue) + "' ESCAPE '\\' ";
        }
        if(pAttribute.getOperator().equals(SearchListAttribute.OPERATOR.LIKE_BOTH_SIDES) && !pAttribute.isString()) {
            if (!pAttributeValue.startsWith("%")) {
                pAttributeValue = "%" + pAttributeValue;
            }
            if(!pAttributeValue.endsWith("%")){
                 pAttributeValue += "%";
            }
            return pDatabaseField + " LIKE '" + escapeSQL(pAttributeValue) + "'";
        }
        if (Double.class == pDataType || pDataType instanceof Double) {
            //return databaseField + " LIKE " + escapeSQL(attributeValue);
            //LIKE does not work, you have to use ranges here to similar values or you have to cast float/double to varchar
            return pDatabaseField + operator + escapeSQL(pAttributeValue);
        }
        if (Integer.class == pDataType || pDataType instanceof Integer) {
            //return databaseField + " LIKE " + escapeSQL(attributeValue);
            //LIKE does not work, you have to use ranges here to similar values or you have to cast float/double to varchar
            return pDatabaseField + operator + escapeSQL(pAttributeValue);
        }
        if (Boolean.class == pDataType || pDataType instanceof Boolean) {
            //return databaseField + " LIKE " + escapeSQL(attributeValue);
            //LIKE does not work, you have to use ranges here to similar values or you have to cast float/double to varchar
            return pDatabaseField + " = " + escapeSQL(pAttributeValue);
        }
        //System.out.println(pAttributeName.getDateType());
        if (pAttribute.getDataType() == de.lb.cpx.model.enums.CaseStatusEn.class) {
            return pDatabaseField + " = '" + pAttributeValue + "'";
        }
        if(pAttribute.getDataType() == de.lb.cpx.model.enums.BooleanEn.class){
            if(pAttributeValue.equals(BooleanEn.SELECTED.name())){
                return pDatabaseField + " = 1";
            }else{
                return "(" + pDatabaseField + " = 0 OR " + pDatabaseField + " is NULL)" ;
            }
            
        }
        if (String.class == pDataType || pDataType instanceof String) {
            if (!pAttributeValue.contains("%")) {
                pAttributeValue += "%";
            }
            return pDatabaseField + " LIKE '" + escapeSQL(pAttributeValue) + "' ESCAPE '\\' ";
            //return Restrictions.like(attField, attributeValue); //, MatchMode.END
        }
//        if (SearchListFormatHospital.class == pDataType || pDataType instanceof SearchListFormatHospital) {
//            pAttributeValue += "%";
//            return pDatabaseField + " LIKE '" + escapeSQL(pAttributeValue) + "' ESCAPE '\\' ";
//            //return Restrictions.like(attField, attributeValue); //, MatchMode.END
//        }
//        if (SearchListFormatInsurance.class == pDataType || pDataType instanceof SearchListFormatInsurance) {
//            pAttributeValue += "%";
//            return pDatabaseField + " LIKE '" + escapeSQL(pAttributeValue) + "' ESCAPE '\\' ";
//            //return Restrictions.like(attField, attributeValue); //, MatchMode.END
//        }
//        if (SearchListFormatDepartment.class == pDataType || pDataType instanceof SearchListFormatDepartment) {
//            pAttributeValue += "%";
//            return pDatabaseField + " LIKE '" + escapeSQL(pAttributeValue) + "' ESCAPE '\\' ";
//            //return Restrictions.like(attField, attributeValue); //, MatchMode.END
//        }

//        if (WmReminderStatusEn.class == pDataType || pDataType instanceof WmReminderStatusEn) {
//            if (pAttributeValue.equals("offen")) {
//                return pDatabaseField + " = 0";
//            } else if (pAttributeValue.equals("abgearbeitet")) {
//                return pDatabaseField + " = 1";
//            }
//        }
        if (WmProcessTypeOfServiceEn.class == pDataType || pDataType instanceof WmProcessTypeOfServiceEn) {
            if (pAttributeValue.equals("ambulant")) {
                return pDatabaseField + " = '04'";
            } else if (pAttributeValue.equals("stationär")) {
                return pDatabaseField + " <> '04'";
            }
        }
        if (pAttributeValue.contains("%")) {
            //return Restrictions.like(attField, attributeValue);
            return pDatabaseField + " LIKE '" + escapeSQL(pAttributeValue) + "'";
        }

        //Or throw an exception, this is a real error!
        //Gives you a strong hint, when a new data type for filtering is not fully implemented
//        LOG.log(Level.SEVERE, "Data type '" + (pDataType==null?"NULL":pDataType) + "' of search field " 
//                + pAttributeName + " / " + pDatabaseField + " (search value: '" + pAttributeValue + "')"
//                + " is unknown. Side effects can obtain (filter functionality is maybe broken for this column)");        
        //return Restrictions.eq(attField, attributeValue);
        // it has to have blank for and after the value to distinguish the numbers
        if((MdkAuditReasonsMap.class == pDataType || pDataType instanceof MdkAuditReasonsMap) && pDatabaseField.equals("VW_AGG_AUDITS.AUDIT_NAMES_LIST")){
            return pDatabaseField + " LIKE '" + escapeSQL("% " + pAttributeValue) + " %'";
        }
        return pDatabaseField + " = '" + escapeSQL(pAttributeValue) + "'";
    }

    public static List<String> uniqueList(final List<String> pList) {
        List<String> newList = new ArrayList<>();
        if (pList == null) {
            return newList;
        }
        Iterator<String> it = pList.iterator();
        while (it.hasNext()) {
            String value = it.next();
            if (!newList.contains(value)) {
                newList.add(value);
            }
        }
        return newList;
    }

    protected static String toStaticDate(final Date pDate, final boolean pIsSqlSrv) {
        return StrUtils.toStaticDate(pDate, pIsSqlSrv);
//        String date = dateToString(pDate);
//        return toStaticDate(date, pIsSqlSrv);
    }

    protected static String toStaticDate(final String pDate, final boolean pIsSqlSrv) {
        return StrUtils.toStaticDate(pDate, pIsSqlSrv);
//        if (pIsSqlSrv) {
//            return "'" + escapeSQL(pDate.replace("-", "")) + "'";
//        }
//        return "TO_DATE('" + escapeSQL(pDate) + "', 'YYYY-MM-DD')";
    }

    protected static String dateToString(final Date pDate) {
        return StrUtils.dateToString(pDate);
//        if (pDate == null) {
//            return "";
//        }
//        Calendar cal = new GregorianCalendar();
//        cal.setTime(pDate);
//        String year = String.valueOf(cal.get(Calendar.YEAR));
//        String month = String.valueOf((cal.get(Calendar.MONTH) + 1));
//        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
//
//        if (month.length() == 1) {
//            month = "0" + month;
//        }
//        if (day.length() == 1) {
//            day = "0" + day;
//        }
//
//        String dateStr = year + "-" + month + "-" + day;
//        return dateStr;
    }

    public static Integer toInt(final String pValue) {
        if (pValue == null) {
            return null;
        }
        final String val = pValue.trim();
        if (val.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, "this value cannot be parsed as an integer: {0}", val);
            LOG.log(Level.FINEST, null, ex);
            return null;
        }
    }

    public static Double toDouble(final Number pValue) {
        if (pValue == null) {
            return null;
        }
        return pValue.doubleValue();
    }

    public static Integer toInt(final Number pValue) {
        if (pValue == null) {
            return null;
        }
        return pValue.intValue();
    }

    public static Integer toInt(final BigDecimal pValue) {
        if (pValue == null) {
            return null;
        }
        return pValue.intValue();
    }

    public static Long toLong(final BigDecimal pValue) {
        if (pValue == null) {
            return null;
        }
        return pValue.longValue();
    }

    public static Long toLong(final Number pValue) {
        if (pValue == null) {
            return null;
        }
        return pValue.longValue();
    }

    public static String escapeSQL(String pString) {
        return StrUtils.escapeSQL(pString);
//        int length = pString.length();
//        int newLength = length;
//        // first check for characters that might
//        // be dangerous and calculate a length
//        // of the string that has escapes.
//        for (int i = 0; i < length; i++) {
//            char c = pString.charAt(i);
//            switch (c) {
//                case '_':
//                case '\\':
//                case '\"':
//                case '\'':
//                case '\0': {
//                    newLength += 1;
//                }
//                break;
//            }
//        }
//        if (length == newLength) {
//            // nothing to escape in the string
//            return pString;
//        }
//        StringBuilder sb = new StringBuilder(newLength);
//        for (int i = 0; i < length; i++) {
//            char c = pString.charAt(i);
//            switch (c) {
//                case '\\': {
//                    sb.append("\\\\");
//                }
//                break;
//                case '\"': {
//                    sb.append("\""); //before -> \\\"
//                }
//                break;
//                case '\'': {
//                    sb.append("\\\'");
//                }
//                break;
//                case '\0': {
//                    sb.append("\\0");
//                }
//                break;
//                case '_': {
//                    sb.append("\\_");
//                }
//                break;
//                default: {
//                    sb.append(c);
//                }
//            }
//        }
//        return sb.toString();
    }

    private class PrepareResult {

        public final List<String> columns;
        public final List<String> where;
        public final List<String> join;
        public final List<String> order;
        public final int limitFrom;
        public final int limitTo;

        public PrepareResult(final int pLimitFrom, final int pLimitTo,
                final List<String> pColumns, final List<String> pWhere, final List<String> pJoin, final List<String> pOrder) {
            this.limitFrom = pLimitFrom;
            this.limitTo = pLimitTo;
            this.columns = pColumns;
            this.where = pWhere;
            this.join = pJoin;
            this.order = pOrder;
        }
    }

    protected String ruleFilterSubQuery(String pAttributeValue) {
        return "";
    }

}

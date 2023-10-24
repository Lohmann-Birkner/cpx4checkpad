/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.catalog;

import static de.lb.cpx.client.core.connection.database.CpxDbManager.QStr;
import static de.lb.cpx.client.core.connection.database.CpxDbManager.toParam;
import de.lb.cpx.server.commonDB.model.COpsAop;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import de.lb.cpx.shared.lang.Lang;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class CpxOpsAopCatalog extends AbstractCpxCatalog<CpxOpsAop, COpsAop> {
    private static CpxOpsAopCatalog instance = null;
     public static final CatalogTypeEn CATALOG = CatalogTypeEn.OPS_AOP;
    private static final Logger LOG = Logger.getLogger(CpxOpsAopCatalog.class.getName());
    private static Map<Integer, Map<String, CpxOpsAop>>  aops4year = new HashMap<>();
    private static CpxOpsAop dummy = null;
    
    public static synchronized CpxOpsAopCatalog instance() {
        if (instance == null) {
            instance = new CpxOpsAopCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxOpsAopCatalog() {

    }
    
    private static CpxOpsAop getDummy(){
        if(dummy == null){
            dummy = new CpxOpsAop();
            dummy.setOpsCode("");
            dummy.setOpsCategory("");
            dummy.setCatalogSheet(-1);
        }
        return dummy;
    }

    @Override
    public String createCatalogTable(CpxCatalogOverview pCatalogOverview) {
     final String tableName = getTableName(pCatalogOverview);
        try ( Statement stmt = getCatalogDb(pCatalogOverview).createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName
                    + "(ID                 INTEGER    PRIMARY KEY,"
                    + " CREATION_DATE      DATETIME, "
                    + " MODIFICATION_DATE  DATETIME, "
                    + " COUNTRY_EN         VARCHAR(25), "
                    + " OPS_CODE           VARCHAR(15), "
                    + " OPS_YEAR           INT, "
                    + " AOP_CATALOG_SHEET  INT, "
                    + " OPS_CATEGORY       VARCHAR(255) ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_OPS_AOP ON %s (OPS_CODE)", tableName); //COUNTRY_EN, OPS_YEAR
            stmt.executeUpdate(sql);


            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public void fillCatalog(CpxCatalogOverview pCatalogOverview, List<COpsAop> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, OPS_CODE, OPS_YEAR, AOP_CATALOG_SHEET, OPS_CATEGORY) ", tableName);
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (COpsAop entry : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(entry.getId()).append(", ");
            sql.append(QStr(entry.getCreationDate())).append(", ");
            sql.append(QStr(entry.getModificationDate())).append(", ");
            sql.append(QStr(entry.getCountryEn().name())).append(", ");
            sql.append(QStr(entry.getOpsCode())).append(", ");
            sql.append(QStr(entry.getOpsYear())).append(", ");
           sql.append(QStr(entry.getCatalogSheet())).append(", ");
            sql.append(QStr(entry.getOpsCategory()) );
            //pstmt.addBatch();
            //break;
            if (i % 500 == 0 || i == pList.size()) {
                try ( PreparedStatement pstmt = getCatalogDb(pCatalogOverview).prepareStatement(sql.toString())) {
                    pstmt.execute();
                }
                first = true;
            }
        }
    }

    @Override
    public CpxOpsAop toCpxObject(ResultSet rs) throws SQLException {
         CpxOpsAop obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //Filling datetime fields takes quite a long time, so I don't care here!
        //obj.setCreationDate(CpxDateParser.parseDateTimeExc(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTimeExc(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setOpsCode(rs.getString("OPS_CODE"));
        obj.setOpsYear(rs.getInt("OPS_YEAR"));
        obj.setCatalogSheet(rs.getInt("AOP_CATALOG_SHEET"));
        obj.setOpsCategory(rs.getString("OPS_CATEGORY"));

        return obj;
    }

    @Override
    public List<CpxOpsAop> findManyByCode(String pCode, String pCountryEn, Integer pYear) {
        final String code = toParam(pCode);
        String sql = "SELECT * FROM %s WHERE OPS_CODE " + operator(code) + " ? ORDER BY OPS_CODE";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });
    }

    @Override
    public CatalogTypeEn getCatalogType() {
       return CATALOG;
    }

    @Override
    public CpxOpsAop getNewObject() {
        return new CpxOpsAop();
    }

    public Map<String, CpxOpsAop> getAllAops(String pCountryEn, Integer pYear ) {
        long timeStart = System.currentTimeMillis();
        Map<String, CpxOpsAop> aops =  aops4year.get(pYear);
        if(aops == null){
             aops = new TreeMap<>();
            final Map<Long, CpxOpsAop> opsAopMap = getAll(pCountryEn, pYear);
            for (Map.Entry<Long, CpxOpsAop> entry:  opsAopMap.entrySet()){
                CpxOpsAop aop = entry.getValue();
                aops.put(aop.getOpsCode(), aop);
            }
            aops4year.put(pYear, aops);
        
        }
         LOG.log(Level.INFO, "Time elapsed while loading AOPs from DB: " + (System.currentTimeMillis() - (double) timeStart) / 1000d + "s");       
//        if(aops.isEmpty()){
//            return;
//        }
//        timeStart = System.currentTimeMillis();        
//        Set <String> aopOpsCodes = aops.keySet();
//        for(String code: aopOpsCodes){
//            CpxOps ops = pAllOps.get(code);
//            if(ops != null){
//                ops.addAop(aops.get(code));
//            }
//        }
//        
        LOG.log(Level.INFO, "Time elapsed while loading AOPs into OPS: " + (System.currentTimeMillis() - (double) timeStart) / 1000d + "s");
        return aops;
    }

    public String getCategoryDescriptionByCode(String pCode, String pCountryEn, int pYear) {
        List<CpxOpsAop> lst = findManyByCode(pCode, pCountryEn, pYear);
        if(lst == null || lst.isEmpty()){
            return null;
        }
        CpxOpsAop aop = lst.get(0);
        return Lang.getCatalogAopCatalog() + " " + aop.getCategoryString();

    }
    public CpxOpsAop getAopByCode(String pCode, String pCountryEn, int pYear) {
        List<CpxOpsAop> lst = findManyByCode(pCode, pCountryEn, pYear);
        if(lst == null || lst.isEmpty()){
            return null;
        }
        return lst.get(0);
        

    }

    public String getAopString(CpxOps opsItem) {
        if(opsItem.getAopString() != null){
            return opsItem.getAopString();
        }
       Map<String, CpxOpsAop> aops =  aops4year.get(opsItem.getOpsYear());
       if(aops == null){
           opsItem.addAop(getDummy());
           return "";
       }
       CpxOpsAop aop = aops.get(opsItem.getCode());
       if(aop == null){
           return "";
       }
       opsItem.addAop(aop);
       return aop.getCategoryString();
    }

    
}

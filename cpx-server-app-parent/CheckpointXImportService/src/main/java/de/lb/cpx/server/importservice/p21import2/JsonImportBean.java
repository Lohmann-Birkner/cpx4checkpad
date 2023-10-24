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
package de.lb.cpx.server.importservice.p21import2;

import de.lb.cpx.server.batch.p21import2.P21Importer2;
import de.lb.cpx.service.information.CpxPersistenceUnit;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.dto.LockException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

@Stateless
//@Startup
//@DependsOn("InitBean")
public class JsonImportBean implements JsonImportBeanLocal {
    //int mThreadCount = 1;

    @EJB
    private P21Importer2 p21Importer2;

    //@PostConstruct
    //@Schedule(hour = "*", minute = "*", second = "*", persistent = false)
    //@TransactionTimeout(value = 30, unit = TimeUnit.MINUTES)
    //@TransactionAttribute(REQUIRES_NEW)
    //public void init(Timer timer) throws FileNotFoundException, Throwable {
    @Override
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public JsonObject startImport(final String pDirectoryName, final String pDatabase, final String pCheckType, final Integer pThreadCount, final boolean pDoGroup, final String pGrouperModel, final String pModule) {
        final JsonObject response = p21Importer2.prepareImport(pDirectoryName, pDatabase, pCheckType, pThreadCount, pDoGroup, pGrouperModel);
        int status = response.getInt("status");
        if (status == Response.Status.OK.getStatusCode()) {
            long executionId = response.getJsonNumber("executionId").longValue();
            try {
                p21Importer2.startImportProcess(executionId, pDirectoryName, pDatabase, pCheckType, pThreadCount, pDoGroup, pGrouperModel, pModule);
            } finally {
                p21Importer2.unlockDatabase();
            }
        }
        return response;
    }

    @Override
    public Long startGrouping(final String pDatabase) throws LockException, LoginException {
        p21Importer2.loginForExternalCall();
        final Long executionId = p21Importer2.prepareBatchgrouping(pDatabase);
        if (executionId != null) {
            try {
                p21Importer2.startBatchgrouping(executionId);
            } finally {
                p21Importer2.unlockDatabase();
            }
        }
        return executionId;
    }

//    @Override
//    public Long startBillImport(final String pDatabase, final String pDirectory) throws LockException, LoginException {
//        p21Importer2.loginForExternalCall();
//        final Long executionId = p21Importer2.prepareBatchgrouping(pDatabase); //??
//        if (executionId != null) {
//            try {
//                p21Importer2.startBillImport(executionId, pDirectory);
//            } finally {
//                p21Importer2.unlockDatabase();
//            }
//        }
//        return executionId;
//    }

    @Override
    public int getNewClientId() {
        return p21Importer2.getNewClientId();
    }

    @Override
    public boolean login(final String pClientId, final String pUserName, final String pHashedPassword, final String pDatabase, final String pAppTypeEn) throws LoginException {
        return p21Importer2.login(pClientId, pUserName, pHashedPassword, pDatabase, pAppTypeEn);
    }
    
    @Override
    public String getUserRoleProperties(final String pClientId) {
        return p21Importer2.getUserRolePropertiesAsString(pClientId);
    }
    
    @Override
    public long getUserId(final String pClientId){
        return p21Importer2.getUserId(pClientId);
    }
    
    @Override
    public Map<String,Map<String,String>> getPersistenceUnits(final String pClientId){
         List<CpxPersistenceUnit> listunits = p21Importer2.getPersistenceUnits(pClientId);
         Map<String,Map<String,String>> map = new HashMap<String, Map<String,String>>();
         for(CpxPersistenceUnit unit: listunits){
            DatabaseInfo dbInfoObj = unit.getDatabaseInfo();
            Map<String,String> dbInfo = new HashMap<String, String>();
            dbInfo.put("connectionUrl", dbInfoObj.getConnectionUrl());
            dbInfo.put("connectionString", dbInfoObj.getConnectionString().toString());
            dbInfo.put("database", dbInfoObj.getDatabase());
            dbInfo.put("hostname", dbInfoObj.getHostName());
            dbInfo.put("port", dbInfoObj.getPort());
            dbInfo.put("driverVendor", dbInfoObj.getDriverVendor());
            dbInfo.put("driverName", dbInfoObj.getDriverName());
            map.put(unit.getPersistenceUnit(), dbInfo);
         }
        return map;
    }
}

/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.startup_ejb;

import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.data.CRGRulePool;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.Rules4DbInteractBean;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Table;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.shared.rules.util.CpxRuleManagerIF;
import de.lb.cpx.shared.rules.util.RuleTablesManager;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Lock;
import static javax.ejb.LockType.READ;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedThreadFactory;

/**
 *
 * @author gerschmann
 */
@Singleton
@Lock(READ)
public class CPXDBRulesManager implements CpxRuleManagerIF, CpxDBRulesManagerLocal {

    private static final Logger LOG = Logger.getLogger(CPXDBRulesManager.class.getName());

    private static final RuleTablesManager RULE_TABLES_MANAGER = new RuleTablesManager();

    @Resource(name = "java:comp/DefaultManagedThreadFactory")
    private ManagedThreadFactory tf;

    @EJB
    private Rules4DbInteractBean rules4DbInteractBean;

    @Override
    public String[] getStringArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return RULE_TABLES_MANAGER.getStringArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));
    }

    private String getTabIdent(String poolIdent, String tableName, int year) {
        tableName = tableName.replaceAll("'", "");
        return poolIdent + "_" + tableName + "_" + String.valueOf(year);

    }

    /**
     * gets all rules from CRG_RULES
     *
     * @param cpxRules list of rules
     * @param pPoolType pool type
     * @return array of crg rules
     */
    @Override
    public CRGRule[] getAllRules(List<CrgRules> cpxRules, PoolTypeEn pPoolType) {
        try {

            if (cpxRules == null) {
                return getAllRules(pPoolType);
            }
            RULE_TABLES_MANAGER.resetAllTables();
            if (cpxRules.isEmpty()) {
                LOG.log(Level.INFO, " no rules found");
                return new CRGRule[0];
            }
            long start = System.currentTimeMillis();
            Map<String, CrgRuleTypes> ruleTypes = rules4DbInteractBean.getId2RuleTypeMap(pPoolType);
            List<CRGRule> cpRules = new ArrayList<>();
            for (CrgRules cpxRule : cpxRules) {
//                LOG.info("process rule " + cpxRule.getCrgrNumber() + " id = " + cpxRule.getId());
// tables are to exist before CRGRule was created, because they are to be saved in this rule
                checkTables(cpxRule);

                CRGRule cpRule = RulesConverter.getCpxCpRule(cpxRule, this);
                if (cpRule != null) {
                    if (cpRule.m_errorTypeText != null && !cpRule.m_errorTypeText.isEmpty()) {
                        CrgRuleTypes type = ruleTypes.get(cpRule.m_errorTypeText);
                        cpRule.m_errorTypeText = type.getCrgtShortText();
                    }
                    cpRule.m_rid = String.valueOf(cpxRule.getId());
                    cpRules.add(cpRule);
//                   LOG.info("rule processed, roles = " + (cpRule.m_roles == null?"0":cpRule.m_roles.length));
                }

            }
            CRGRule[] ret = new CRGRule[cpRules.size()];
            cpRules.toArray(ret);
            LOG.log(Level.INFO, "time for load all rules :{0}", System.currentTimeMillis() - start);
            return ret;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on getting rules from db", ex);
            return null;
        }
    }

    @Lock(READ)
    public CRGRule[] getAllRules(PoolTypeEn pPoolType) {
        // get all Pools
        try {
            RULE_TABLES_MANAGER.resetAllTables();
            List<CRGRule> cpRules = new ArrayList<>();
            List<CrgRulePools> pools = rules4DbInteractBean.getCrgRulePools(pPoolType);
//             List<Long> poolInThreads = new ArrayList<>();
            Long userId = pPoolType.equals(PoolTypeEn.DEV) ? ClientManager.getCurrentCpxUserId() : null;
            List<CrgRulePools>usedPools = new ArrayList<>();
            for (CrgRulePools pool : pools) {
                if (rules4DbInteractBean.getRulesCount4PoolAndUser(pPoolType, pool.getId(), userId) == 0L) {
                    LOG.log(Level.INFO, " pool: {0}_{1} has no rules ", new Object[]{pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear()), userId == null ? "" : ("for  " + String.valueOf(userId))});
                    continue;
                }
                usedPools.add(pool);
            }
            if(!usedPools.isEmpty()){
                
                int numberOfThreads = usedPools.size();
                ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numberOfThreads);
                CompletionService<List<CRGRule> > completionService = new ExecutorCompletionService<List<CRGRule> >(executor);
                for(CrgRulePools pool : usedPools){
                    ReadPoolTask task = new ReadPoolTask(pool, pPoolType, this,  userId);
                    completionService.submit(task);
                }
                executor.shutdown();
                try {
                    for (int t = 0; t < numberOfThreads ; t++) {
                        Future<List<CRGRule> > f = completionService.take();
                        List<CRGRule> result = f.get();
                        cpRules.addAll(result);
                    }
                } catch (InterruptedException ex) {
                    //termination of all started tasks (it returns all not started tasks in queue)
                    executor.shutdownNow();
                    LOG.log(Level.SEVERE, "error on getting rules from db", ex);
                    return null;
                } catch (ExecutionException ex) {
                    LOG.log(Level.SEVERE, "error on getting rules from db", ex);
                    return null;
                }
                
            }
//                poolInThreads.add(pool.getId());
////                Thread poolThread = new  ReadPoolThread( cpRules, pool, pPoolType, this, poolInThreads, userId);
////                poolThread.start();
//                tf.newThread(new ReadPoolThread(cpRules, pool, pPoolType, this, poolInThreads, userId)).start();
//
//                List<CrgRules> rules4Pool = null;
//                try{    
//                    rules4Pool = rules4DbInteractBean.findRulesForPool(pool.getId(), pPoolType, null);
//                }catch( Exception ex){
//                    if(ex.getCause() instanceof RuleEditorProcessException){
//                        LOG.log(Level.INFO, "for pool: {0}_{1} no rules found", new Object[]{pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear())});
//                    
//                    }  else{
//                        LOG.log(Level.SEVERE, "",  ex);
//                    }             
//                }
//                if(rules4Pool == null || rules4Pool.isEmpty()){
//                    LOG.log(Level.INFO, "for pool: {0}_{1} no rules found", new Object[]{pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear())});
//                    continue;
//                }
//                LOG.log(Level.INFO, "There are {0} rules for pool: {1}_{2}", new Object[]{rules4Pool.size(), pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear())});
//                List<CrgRuleTables> tables = rules4DbInteractBean.getRuleTables4Pool(pPoolType, pool.getId());
//               LOG.log(Level.INFO, "There are {0} tables for pool: {1}_{2}", new Object[]{tables.size(), pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear())});
//               tables.forEach((table) -> {
//                   String key = getTabIdent(pool.getCrgplIdentifier(), String.valueOf(table.getId()), pool.getCrgplPoolYear());
//                   mRuleTablesManager.addRuleTable(key, table.getCrgtContent());
//                });
//                for(CrgRules cpxRule: rules4Pool){
//                    CRGRule cpRule = RulesConverter.getCpRuleFromCpxRuleDefinition(cpxRule.getCrgrDefinition(), pool.getCrgplIdentifier(), this);
//                    if(cpRule != null){
//                        cpRules.add(cpRule);
//                    }
//                }
//
//            }
//            while (poolInThreads.size() > 0) {
//                LOG.log(Level.FINEST, "poolInThreads is still not empty, waiting for {0} threads", poolInThreads.size());
//                Thread.sleep(500L);
//            }
            LOG.log(Level.INFO, " there are all together {0} rules", cpRules.size());
            CRGRule[] ret = new CRGRule[cpRules.size()];

            cpRules.toArray(ret);
//            for(CRGRule rule: ret){
//                LOG.log(Level.INFO, "m_rid:{0}, rule:{1}", new Object[]{rule.m_rid, rule.toString()});
//            }
            return ret;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on getting rules from db", ex);
            return null;
        }
    }

    /**
     * gets rule tables for cpx rule and saves them in hashmap for cp rule
     */
    private void checkTables(CrgRules cpxRule) {
        try {
            Set<CrgRule2Table> r2rts = cpxRule.getCrgRule2Tables();
            if (r2rts == null || r2rts.isEmpty()) {
                return;
            }
            for (CrgRule2Table r2rt : r2rts) {
                CrgRuleTables tab = r2rt.getCrgRuleTables();
                if (tab == null) {
                    continue;
                }
                CrgRulePools pool = tab.getCrgRulePools();
                if (pool == null) {
                    continue;
                }
                // instead of getCrgtTableName tab.getId()
                String key = getTabIdent(pool.getCrgplIdentifier(), String.valueOf(tab.getId()), pool.getCrgplPoolYear());
                RULE_TABLES_MANAGER.addRuleTable(key, tab.getCrgtContent());
//                String[] content = mTableIdent2Table.get(key);
//                if (content != null) {
//// table is already saved                    
//                    continue;
//                } else {
//                    String cpxContent = tab.getCrgtContent();
//                    if (cpxContent == null || cpxContent.isEmpty()) {
//                        mTableIdent2Table.put(key, new String[0]);
//                    } else {
//                        String[] tabArr = cpxContent.split(",");
//                        Arrays.sort(tabArr);
//                        mTableIdent2Table.put(key, tabArr);
//                    }
//                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on getting of tables for rule " + cpxRule.getCrgrRid(), ex);
        }
    }

    @Override
    public CrgRules findRuleById(long pRId) {
        return rules4DbInteractBean.findRuleById(pRId);
    }

    @Override
    public int[] getIntArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return RULE_TABLES_MANAGER.getIntArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));
    }

    @Override
    public double[] getDoubleArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return RULE_TABLES_MANAGER.getDoubleArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));
    }

    @Override
    public synchronized long[] getLongArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return RULE_TABLES_MANAGER.getLongArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));
    }

    @Override
    public synchronized Date[] getDateArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return RULE_TABLES_MANAGER.getDateArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));

    }

    @Override
    public List<CRGRulePool> getRulePools() throws Exception {
        // woun't be needed
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
class ReadPoolTask implements Callable<List<CRGRule> >{


    private final CrgRulePools mPool;
    private final PoolTypeEn mPoolType;
    private final CPXDBRulesManager mMgr;
    private final Long mUserId;

    public ReadPoolTask(
            final CrgRulePools pPool, 
            final PoolTypeEn pPoolType, 
            final CPXDBRulesManager pMgr, 

            final Long pUserId) {
        mPool = pPool;
        mPoolType = pPoolType;
        mMgr = pMgr;

        mUserId = pUserId;
    }        

    @Override
    public List<CRGRule>  call() throws Exception {
        List<CRGRule> mCpRules = new ArrayList<>();

        try {
            long start = System.currentTimeMillis();
            List<CrgRules> rules4Pool = new ArrayList<>();

            try {
                LOG.log(Level.INFO, "find rules for poolId: {0}, userId: {1}", new Object[]{mPool.getId(), mUserId == null ? "null" : String.valueOf(mUserId)});
                rules4Pool = rules4DbInteractBean.findRulesForPool(mPool.getId(), mPoolType, null, mUserId);
            } catch (Exception ex) {
                if (ex.getCause() instanceof RuleEditorProcessException || ex instanceof RuleEditorProcessException) {
                    LOG.log(Level.INFO, "for pool: {0}_{1} no rules found", new Object[]{mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});

                } else {
                    LOG.log(Level.SEVERE, "", ex);
                }
                return mCpRules;
            }
            if (rules4Pool == null || rules4Pool.isEmpty()) {
                LOG.log(Level.INFO, "for pool: {0}_{1} no rules found", new Object[]{mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});

            } else {
                LOG.log(Level.INFO, "There are {0} rules for pool: {1}_{2}", new Object[]{rules4Pool.size(), mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});

                List<CrgRuleTables> tables = rules4DbInteractBean.getRuleTables4PoolWithContent(mPoolType, mPool.getId());

                LOG.log(Level.INFO, "There are {0} tables for pool: {1}_{2}", new Object[]{tables.size(), mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});
                tables.forEach((table) -> {
                    String key = getTabIdent(mPool.getCrgplIdentifier(), String.valueOf(table.getId()), mPool.getCrgplPoolYear());
                    RULE_TABLES_MANAGER.addRuleTable(key, table.getCrgtContent());
                });
                for (CrgRules cpxRule : rules4Pool) {
                    CRGRule cpRule = RulesConverter.getCpRuleFromCpxRuleDefinition(cpxRule.getCrgrDefinition(), mPool.getCrgplIdentifier(), mMgr);
                    if (cpRule != null) {
                        cpRule.m_rid = String.valueOf(cpxRule.getId());
//                            LOG.log(Level.INFO, "cpRule: rid = {0}, roles: {1}, rule:{2}", new Object[]{cpRule.m_rid, cpRule.m_roles.length, cpRule.toString()});
                        mCpRules.add(cpRule);
                    }
                }
                LOG.log(Level.INFO, "Ready for pool: {0}_{1}, there are {2} rules found", new Object[]{mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear()), rules4Pool.size()});
            }
            LOG.log(Level.INFO, "time: {0}", String.valueOf(System.currentTimeMillis() - start));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " Error on getting of rules for pool:  {0}_{1}", new Object[]{mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});
            LOG.log(Level.SEVERE, " Error on getting of rules for pool", ex);
        }
        return mCpRules;
    }
}
        


    class ReadPoolThread implements Runnable {

        private final List<CRGRule> mCpRules;
        private final CrgRulePools mPool;
        private final PoolTypeEn mPoolType;
        private final CPXDBRulesManager mMgr;
        private final List<Long> mPoolIds;
        private final Long mUserId;

        public ReadPoolThread(final List<CRGRule> cpRules, 
                final CrgRulePools pPool, 
                final PoolTypeEn pPoolType, 
                final CPXDBRulesManager pMgr, 
                final List<Long> pPoolIds, 
                final Long pUserId) {
            mCpRules = cpRules;
            mPool = pPool;
            mPoolType = pPoolType;
            mMgr = pMgr;
            mPoolIds = pPoolIds;
            mUserId = pUserId;
        }

        @Override
        @Asynchronous
        public void run() {
            try {
                long start = System.currentTimeMillis();
                List<CrgRules> rules4Pool;
                try {
                    LOG.log(Level.INFO, "find rules for poolId: {0}, userId: {1}", new Object[]{mPool.getId(), mUserId == null ? "null" : String.valueOf(mUserId)});
                    rules4Pool = rules4DbInteractBean.findRulesForPool(mPool.getId(), mPoolType, null, mUserId);
                } catch (Exception ex) {
                    if (ex.getCause() instanceof RuleEditorProcessException || ex instanceof RuleEditorProcessException) {
                        LOG.log(Level.INFO, "for pool: {0}_{1} no rules found", new Object[]{mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});

                    } else {
                        LOG.log(Level.SEVERE, "", ex);
                    }
                    mPoolIds.remove(mPool.getId());
                    return;
                }
                if (rules4Pool == null || rules4Pool.isEmpty()) {
                    LOG.log(Level.INFO, "for pool: {0}_{1} no rules found", new Object[]{mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});

                } else {
                    LOG.log(Level.INFO, "There are {0} rules for pool: {1}_{2}", new Object[]{rules4Pool.size(), mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});
                                        if(mPool.getId() == 21){
                                            int i = 0;
                                        }
                    List<CrgRuleTables> tables = rules4DbInteractBean.getRuleTables4PoolWithContent(mPoolType, mPool.getId());

                    LOG.log(Level.INFO, "There are {0} tables for pool: {1}_{2}", new Object[]{tables.size(), mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});
                    tables.forEach((table) -> {
                        String key = getTabIdent(mPool.getCrgplIdentifier(), String.valueOf(table.getId()), mPool.getCrgplPoolYear());
                        RULE_TABLES_MANAGER.addRuleTable(key, table.getCrgtContent());
                    });
                    for (CrgRules cpxRule : rules4Pool) {
                        CRGRule cpRule = RulesConverter.getCpRuleFromCpxRuleDefinition(cpxRule.getCrgrDefinition(), mPool.getCrgplIdentifier(), mMgr);
                        if (cpRule != null) {
                            cpRule.m_rid = String.valueOf(cpxRule.getId());
//                            LOG.log(Level.INFO, "cpRule: rid = {0}, roles: {1}, rule:{2}", new Object[]{cpRule.m_rid, cpRule.m_roles.length, cpRule.toString()});
                            mCpRules.add(cpRule);
                        }
                    }
                    LOG.log(Level.INFO, "Ready for pool: {0}_{1}, there are {2} rules found", new Object[]{mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear()), rules4Pool.size()});
                }
                LOG.log(Level.INFO, "time: {0}", String.valueOf(System.currentTimeMillis() - start));
                mPoolIds.remove(mPool.getId());
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, " Error on getting of rules for pool:  {0}_{1}", new Object[]{mPool.getCrgplIdentifier(), String.valueOf(mPool.getCrgplPoolYear())});
                LOG.log(Level.SEVERE, " Error on getting of rules for pool", ex);
                mPoolIds.remove(mPool.getId());
            }
        }

    }

}

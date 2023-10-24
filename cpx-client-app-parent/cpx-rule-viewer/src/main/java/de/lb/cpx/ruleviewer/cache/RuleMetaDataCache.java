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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.cache;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Cache to handle rule Meta data as rule tables etc
 *
 * @author wilde
 */
public class RuleMetaDataCache {

    private static RuleMetaDataCache INSTANCE;

    private RuleMetaDataCache() {

    }

    public static RuleMetaDataCache instance() {
        if (INSTANCE == null) {
            INSTANCE = new RuleMetaDataCache();
        }
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE.clearMapPoolToTable();
        INSTANCE.clearRuleTypes();
        INSTANCE = null;
    }

    private Map<PoolTypeEn, Map<Long, List<CrgRuleTables>>> mapPoolToTable;

    protected Map<Long, List<CrgRuleTables>> getPoolToTableMap(PoolTypeEn pType) {
        if (mapPoolToTable == null) {
            mapPoolToTable = new HashMap<>();
            mapPoolToTable.put(PoolTypeEn.PROD, new HashMap<>());
            mapPoolToTable.put(PoolTypeEn.DEV, new HashMap<>());
        }
        return mapPoolToTable.get(pType);
    }

    public List<CrgRuleTables> getTablesForPool(CrgRulePools pPool) {
//        Map<Long, List<CrgRuleTables>> map = getPoolToTableMap(PoolTypeHelper.getPoolType(pPool));
//        if(!map.containsKey(pPool.getId())){
//            map.put(pPool.getId(), Session.instance().getEjbConnector().connectRuleEditorBean().get().findRuleTablesForPool(pPool.getId(), PoolTypeHelper.getPoolType(pPool)));
//        }
//        return map.get(pPool.getId());
        return getTablesForPool(pPool.getId(), PoolTypeHelper.getPoolType(pPool));
    }

    public List<CrgRuleTables> getTablesForPool(long pPoolId, PoolTypeEn pPoolType) {
        Map<Long, List<CrgRuleTables>> map = getPoolToTableMap(pPoolType);
        if (!map.containsKey(pPoolId)) {
            List<CrgRuleTables> tables = Session.instance().getEjbConnector().connectRuleEditorBean().get().findRuleTablesForPool(pPoolId, pPoolType);
            tables.sort(Comparator.comparing(CrgRuleTables::getCrgtTableName));
            map.put(pPoolId, tables);
        }
        return map.get(pPoolId);
    }

    public void clearMapPoolToTable() {
        if (mapPoolToTable != null) {
            mapPoolToTable.clear();
            mapPoolToTable = null;
        }
    }

    public void clearPoolToTableCache(CrgRulePools pPool) {
        clearPoolToTableCache(PoolTypeHelper.getPoolType(pPool), pPool.getId());
    }

    private void clearPoolToTableCache(PoolTypeEn pPoolType, long pPoolId) {
        Map<Long, List<CrgRuleTables>> map = getPoolToTableMap(pPoolType);
        if (map.containsKey(pPoolId)) {
            map.remove(pPoolId);
        }
    }

    public void addTableInPoolToTableMap(CrgRulePools pPool, CrgRuleTables pTable) {
        addTableInPoolToTableMap(pPool.getId(), PoolTypeHelper.getPoolType(pPool), pTable);
    }

    public void addTableInPoolToTableMap(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
        Map<Long, List<CrgRuleTables>> map = getPoolToTableMap(pPoolType);
        if (map.containsKey(pPoolId)) {
            List<CrgRuleTables> tables = map.get(pPoolId);
            if (tables != null) {
                tables.add(pTable);
            }
        }
    }

    public void updateTableInPoolToTableMap(CrgRulePools pPool, CrgRuleTables pTable) {
        updateTableInPoolToTableMap(pPool.getId(), PoolTypeHelper.getPoolType(pPool), pTable);
    }

    public void updateTableInPoolToTableMap(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
        Map<Long, List<CrgRuleTables>> map = getPoolToTableMap(pPoolType);
        if (map.containsKey(pPoolId)) {
            List<CrgRuleTables> tables = map.get(pPoolId);
            if (tables == null) {
                return;
            }
            for (CrgRuleTables table : tables) {
                if (table.getId() == pTable.getId()) {
                    tables.set(tables.indexOf(table), pTable);
                }
            }
        }
    }

    public void removeTableInPoolToTableMap(CrgRulePools pPool, CrgRuleTables pTable) {
        removeTableInPoolToTableMap(pPool.getId(), PoolTypeHelper.getPoolType(pPool), pTable);
    }

    public void removeTableInPoolToTableMap(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
        Map<Long, List<CrgRuleTables>> map = getPoolToTableMap(pPoolType);
        if (map.containsKey(pPoolId)) {
            List<CrgRuleTables> tables = map.get(pPoolId);
            tables.remove(pTable);
            if(tables.isEmpty()){
                map.remove(pPoolId);
            }
        }
    }

    public String getTableNameInPool(long pPoolId, PoolTypeEn pPoolType, String pId) {
        pId = pId.replace("'", "");
        List<CrgRuleTables> tables = getTablesForPool(pPoolId, pPoolType);
        for (CrgRuleTables table : tables) {
            if (String.valueOf(table.getId()).equals(pId)) {
                return table.getCrgtTableName();
            }
        }
        return null;
    }

    public String getTableNameInPool(CrgRulePools pPool, String pId) {
        if(pId == null ||pId.isEmpty()){
            return null;
        }
        CrgRulePools defaultPool = new CrgRulePools();
        defaultPool.setId(0);
        pPool = Objects.requireNonNullElse(pPool, defaultPool);
        pId = pId.replace("'", "");
        return getTableNameInPool(pPool.getId(), PoolTypeHelper.getPoolType(pPool), pId);
    }

    public String getTableIdInPool(CrgRulePools pPool, String pName) {
        return getTableIdInPool(pPool.getId(), PoolTypeHelper.getPoolType(pPool), pName);
    }

    private String getTableIdInPool(long pPoolId, PoolTypeEn pPoolType, String pName) {
        List<CrgRuleTables> tables = getTablesForPool(pPoolId, pPoolType);
        for (CrgRuleTables table : tables) {
            if (table.getCrgtTableName().toLowerCase().equals(pName.toLowerCase())) {
                return String.valueOf(table.getId());
            }
        }
        return null;
    }

    public CrgRuleTables getTableForIdInPool(CrgRulePools pool, String pId) {
        return getTableForIdInPool(pool.getId(), PoolTypeHelper.getPoolType(pool), pId);
    }

    public String getTableContentForIdInPool(CrgRulePools pool, String pId) {
        CrgRuleTables table = getTableForIdInPool(pool.getId(), PoolTypeHelper.getPoolType(pool), pId);
        if (table == null) {
            return "";
        }
        if (table.getCrgtContent() == null) {
            String content = Session.instance().getEjbConnector().connectRuleEditorBean().get().getRuleTableContent(table.getId(), PoolTypeHelper.getPoolType(pool));
            table.setCrgtContent(content);
        }
        return table.getCrgtContent();
    }

    public String getTableCommentForIdInPool(CrgRulePools pool, String pId) {
        CrgRuleTables table = getTableForIdInPool(pool.getId(), PoolTypeHelper.getPoolType(pool), pId);
        if (table == null) {
            return "";
        }
        if (table.getCrgtComment()== null) {
            String comment = Session.instance().getEjbConnector().connectRuleEditorBean().get().getRuleTableComment(table.getId(), PoolTypeHelper.getPoolType(pool));
            table.setCrgtComment(comment);
        }
        return table.getCrgtComment();
    }

    public CrgRuleTables getTableForNameInPool(CrgRulePools pool, String pName) {
        return getTableForNameInPool(pool.getId(), PoolTypeHelper.getPoolType(pool), pName);
    }


    public String getTableCommentForNameInPool(CrgRulePools pool, String pName) {
        CrgRuleTables table = getTableForNameInPool(pool.getId(), PoolTypeHelper.getPoolType(pool), pName);
        if (table == null) {
            return "";
        }
        if (table.getCrgtComment()== null) {
            String comment = Session.instance().getEjbConnector().connectRuleEditorBean().get().getRuleTableComment(table.getId(), PoolTypeHelper.getPoolType(pool));
            table.setCrgtComment(comment);
        }
        return table.getCrgtComment();
    }

    private CrgRuleTables getTableForIdInPool(long pPoolId, PoolTypeEn pPoolType, String pId) {
        List<CrgRuleTables> tables = getTablesForPool(pPoolId, pPoolType);
        for (CrgRuleTables table : tables) {
            if (String.valueOf(table.getId()).equals(pId)) {
                return table;
            }
        }
        return null;
    }

    private CrgRuleTables getTableForNameInPool(long pPoolId, PoolTypeEn pPoolType, String pName) {
         List<CrgRuleTables> tables = getTablesForPool(pPoolId, pPoolType);
        for (CrgRuleTables table : tables) {
            if (String.valueOf(table.getCrgtTableName()).equals(pName)) {
                return table;
            }
        }
        return null;
    }
    /*
     * Rule Types! 
     */
    private Map<PoolTypeEn, List<CrgRuleTypes>> mapTypeToRuleTypes;

    protected Map<PoolTypeEn, List<CrgRuleTypes>> getTypeToRuleTypesMap() {
        if (mapTypeToRuleTypes == null) {
            mapTypeToRuleTypes = new HashMap<>();
//            mapTypeToRuleTypes.put(PoolTypeEn.PROD,null);
//            mapTypeToRuleTypes.put(PoolTypeEn.DEV,null);
        }
        return mapTypeToRuleTypes;
    }

    public List<CrgRuleTypes> getRuleTypes(PoolTypeEn pType) {
        if (!getTypeToRuleTypesMap().containsKey(pType)) {
            List<CrgRuleTypes> types = Session.instance().getEjbConnector().connectRuleEditorBean().get().findAllRuleTypes(pType);
            sort(types);
            getTypeToRuleTypesMap().put(pType, types);
        }
        return getTypeToRuleTypesMap().get(pType);
    }

    public void addRuleType(CrgRuleTypes pRuleType, PoolTypeEn pType) {
        List<CrgRuleTypes> types = getRuleTypes(pType);
        types.add(pRuleType);
        sort(types);
    }

    public void removeRuleType(CrgRuleTypes pRuleType, PoolTypeEn pType) {
        List<CrgRuleTypes> types = getRuleTypes(pType);
        types.remove(pRuleType);
    }

    public void updateRuleType(CrgRuleTypes pRuleType, PoolTypeEn pType) {
        List<CrgRuleTypes> types = getRuleTypes(pType);
        for (CrgRuleTypes type : types) {
            if (type.getId() == pRuleType.getId()) {
                int idx = types.indexOf(type);
                types.set(idx, pRuleType);
                break;
            }
        }
        sort(types);
    }

    public void sort(List<CrgRuleTypes> pList) {
        pList.sort(Comparator.comparing(CrgRuleTypes::getCrgtShortText));
    }

    public void clearRuleTypesForPoolType(PoolTypeEn pType) {
        if (getTypeToRuleTypesMap().containsKey(pType)) {
            mapTypeToRuleTypes.remove(pType);
        }
    }

    public void clearRuleTypes() {
        if (mapTypeToRuleTypes != null) {
            mapTypeToRuleTypes.clear();
            mapTypeToRuleTypes = null;
        }
    }

    public int getIndexOf(AbstractEntity pEntity, List<? extends AbstractEntity> pEntities) {
        for (int i = 0; i < pEntities.size(); i++) {
            AbstractEntity ent = pEntities.get(i);
            if (ent.id == pEntity.id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * get RuleType for stored ident in rule TODO: Maybe build map and not
     * search in list, list size is unknown map would be a lot faster on very
     * large lists
     *
     * @param pIdent ident of the rule tpye
     * @param poolType type to determine prod or dev
     * @return rule type object
     */
    public CrgRuleTypes getRuleTypeForIdent(int pIdent, PoolTypeEn poolType) {
        List<CrgRuleTypes> types = getRuleTypes(poolType);
        for (CrgRuleTypes type : types) {
            if (type.getCrgtIdent() == pIdent) {
                return type;
            }
        }
        return null;
    }

    /**
     * get RuleType for stored ident in rule TODO: Maybe build map and not
     * search in list, list size is unknown map would be a lot faster on very
     * large lists
     *
     * @param pId id of the rule tpye
     * @param poolType type to determine prod or dev
     * @return rule type object
     */
    public CrgRuleTypes getRuleTypeForId(long pId, PoolTypeEn poolType) {
        List<CrgRuleTypes> types = getRuleTypes(poolType);
        for (CrgRuleTypes type : types) {
            if (type.getId() == pId) {
                return type;
            }
        }
        return null;
    }

}

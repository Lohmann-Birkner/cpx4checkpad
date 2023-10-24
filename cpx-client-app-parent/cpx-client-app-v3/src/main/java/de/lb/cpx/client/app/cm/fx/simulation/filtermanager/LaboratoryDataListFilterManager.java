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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.filtermanager;

import de.lb.cpx.client.core.model.fx.filter_manager.FilterManager;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.shared.filter.enums.LaboratoryDataListAttributes;

/**
 *
 * @author nandola
 */
public class LaboratoryDataListFilterManager extends FilterManager {

    private static LaboratoryDataListFilterManager instance;

//    private SearchList mSearchList;
//    private ObservableMap<ColumnOption, List<FilterOption>> filterOptionMap;
    private LaboratoryDataListFilterManager() {
        super(SearchListTypeEn.LABORATORY, new LaboratoryDataListAttributes());
//        mSearchList = new SearchList();
//        initColumnOptions();
//        mSearchList.setColumns(getColumnOptions());
    }

    public static LaboratoryDataListFilterManager getInstance() {
        if (LaboratoryDataListFilterManager.instance == null) {
            LaboratoryDataListFilterManager.instance = new LaboratoryDataListFilterManager();
        }
        return LaboratoryDataListFilterManager.instance;
    }
//
//    public SearchList getSearchList() {
//        return mSearchList;
//    }
//
//    public void setSearchList(SearchList mSearchList) {
//        this.mSearchList = mSearchList;
//    }
//    /**
//     * Get List with all set Filteroptions
//     *
//     * @return ObservableList
//     */
//    public ObservableMap<ColumnOption, List<FilterOption>> getFilterOptionMap() {
//        return filterOptionMap;
//    }
//    public List<FilterOption> getFilterOptions(ColumnOption opt) {
//        return getFilterOptionMap().get(opt);
//    }
//    public final Set<ColumnOption> getColumnOptions(){
//        return filterOptionMap.keySet();
//    }
//    
//    protected void addColumnOption(ColumnOption pOpt){
//        if(!filterOptionMap.containsKey(pOpt)){
//            filterOptionMap.put(pOpt, new ArrayList<>());
//        }
//    }
//    protected void addColumnOptions(ColumnOption... pOpts){
//        for(ColumnOption opt : pOpts){
//            addColumnOption(opt);
//        }
//    }
//    public ColumnOption getColumnOption(String attString) {
//        for (ColumnOption colOption : getFilterOptionMap().keySet()) {
//            if (colOption.attributeName.equals(attString)) {
//                return colOption;
//            }
//        }
//        return null;
//    }
//    private void initColumnOptions(){
//        ColumnOption pos = new ColumnOption(Lang.LAB_DATA_POSITION, LaboratoryDataListAttributes.position, true);
//        pos.setSize(60);
//        pos.setNumber(1);
//
//        ColumnOption category = new ColumnOption(Lang.LAB_DATA_CATEGORY, LaboratoryDataListAttributes.category, true);
//        category.setSize(90);
//        category.setNumber(2);
//
//        ColumnOption lockDel = new ColumnOption(Lang.LAB_DATA_LOCK_DEL, LaboratoryDataListAttributes.lockDel, true);
//        lockDel.setSize(90);
//        lockDel.setNumber(3);
//
//        ColumnOption date = new ColumnOption(Lang.LAB_DATA_LAB_DATE, LaboratoryDataListAttributes.date, true);
//        date.setSize(130);
//        date.setNumber(4);
//        
//        ColumnOption analysisDate = new ColumnOption(Lang.LAB_DATA_ANALYSIS_DATE, LaboratoryDataListAttributes.analysisDate, true);
//        analysisDate.setSize(130);
//        analysisDate.setNumber(5);
//
//        ColumnOption valueOrText = new ColumnOption(Lang.LAB_DATA_TEXT, LaboratoryDataListAttributes.valueOrText, true);
//        valueOrText.setSize(200);
//        valueOrText.setNumber(6);
//
//        ColumnOption description = new ColumnOption(Lang.LAB_DATA_DESCRIPTION, LaboratoryDataListAttributes.description, true);
//        description.setSize(150);
//        description.setNumber(7);
//
//        ColumnOption group = new ColumnOption(Lang.LAB_DATA_GROUP, LaboratoryDataListAttributes.group, true);
//        group.setSize(200);
//        group.setNumber(8);
//
//        ColumnOption kisExtern = new ColumnOption(Lang.LAB_DATA_KIS_EXTERN_KEY, LaboratoryDataListAttributes.kisExtern, true);
//        kisExtern.setSize(200);
//        kisExtern.setNumber(9);
//
//        ColumnOption comment = new ColumnOption(Lang.LAB_DATA_COMMENT, LaboratoryDataListAttributes.comment, true);
//        comment.setSize(300);
//        comment.setNumber(10);
//
//        ColumnOption range = new ColumnOption(Lang.LAB_DATA_RANGE, LaboratoryDataListAttributes.range, true);
//        range.setSize(300);
//        range.setNumber(11);
//
//        ColumnOption area = new ColumnOption(Lang.LAB_DATA_BENCHMARK, LaboratoryDataListAttributes.area, true);
//        area.setSize(120);
//        area.setNumber(12);
//
//        ColumnOption analysis = new ColumnOption(Lang.LAB_DATA_ANALYSIS, LaboratoryDataListAttributes.analysis, true);
//        analysis.setSize(200);
//        analysis.setNumber(13);
//
//        ColumnOption unit = new ColumnOption(Lang.LAB_DATA_UNIT, LaboratoryDataListAttributes.unit, true);
//        unit.setSize(120);
//        unit.setNumber(14);
//
//        ColumnOption method = new ColumnOption(Lang.LAB_DATA_METHOD, LaboratoryDataListAttributes.method, true);
//        method.setSize(120);
//        method.setNumber(15);
//
//        ColumnOption value1 = new ColumnOption(Lang.LAB_DATA_VALUE, LaboratoryDataListAttributes.value1, true);
//        value1.setSize(100);
//        value1.setNumber(16);
//
//        ColumnOption value2 = new ColumnOption(Lang.LAB_DATA_VALUE_2, LaboratoryDataListAttributes.value2, true);
//        value2.setSize(100);
//        value2.setNumber(17);
//
//        ColumnOption minLimit = new ColumnOption(Lang.LAB_DATA_MIN_LIMIT, LaboratoryDataListAttributes.minLimit, true);
//        minLimit.setSize(100);
//        minLimit.setNumber(18);
//
//        ColumnOption maxLimit = new ColumnOption(Lang.LAB_DATA_MAX_LIMIT, LaboratoryDataListAttributes.maxLimit, true);
//        maxLimit.setSize(100);
//        maxLimit.setNumber(19);
//
//        addColumnOptions(pos,category,lockDel,date,analysisDate,valueOrText,description,group,kisExtern,comment,range,area,analysis,unit,method,value1,value2,minLimit,maxLimit);
//    }
//    public Set<ColumnOption> getColumnOptions() {
//        Set<ColumnOption> list = new HashSet<>();

//        ColumnOption pos = new ColumnOption(Lang.LAB_DATA_POSITION, LaboratoryDataListAttributes.position, true);
//        pos.setSize(60);
//        pos.setNumber(1);
//
//        ColumnOption category = new ColumnOption(Lang.LAB_DATA_CATEGORY, LaboratoryDataListAttributes.category, true);
//        category.setSize(90);
//        category.setNumber(2);
//
//        ColumnOption lockDel = new ColumnOption(Lang.LAB_DATA_LOCK_DEL, LaboratoryDataListAttributes.lockDel, true);
//        lockDel.setSize(90);
//        lockDel.setNumber(3);
//        ColumnOption date = new ColumnOption(Lang.LAB_DATA_LAB_DATE, LaboratoryDataListAttributes.date, true);
//        date.setSize(130);
//        date.setNumber(4);
//        ColumnOption analysisDate = new ColumnOption(Lang.LAB_DATA_ANALYSIS_DATE, LaboratoryDataListAttributes.analysisDate, true);
//        analysisDate.setSize(130);
//        analysisDate.setNumber(5);
//
//        ColumnOption valueOrText = new ColumnOption(Lang.LAB_DATA_TEXT, LaboratoryDataListAttributes.valueOrText, true);
//        valueOrText.setSize(200);
//        valueOrText.setNumber(6);
//
//        ColumnOption description = new ColumnOption(Lang.LAB_DATA_DESCRIPTION, LaboratoryDataListAttributes.description, true);
//        description.setSize(150);
//        description.setNumber(7);
//
//        ColumnOption group = new ColumnOption(Lang.LAB_DATA_GROUP, LaboratoryDataListAttributes.group, true);
//        group.setSize(200);
//        group.setNumber(8);
//
//        ColumnOption kisExtern = new ColumnOption(Lang.LAB_DATA_KIS_EXTERN_KEY, LaboratoryDataListAttributes.kisExtern, true);
//        kisExtern.setSize(200);
//        kisExtern.setNumber(9);
//
//        ColumnOption comment = new ColumnOption(Lang.LAB_DATA_COMMENT, LaboratoryDataListAttributes.comment, true);
//        comment.setSize(300);
//        comment.setNumber(10);
//
//        ColumnOption range = new ColumnOption(Lang.LAB_DATA_RANGE, LaboratoryDataListAttributes.range, true);
//        range.setSize(300);
//        range.setNumber(11);
//
//        ColumnOption area = new ColumnOption(Lang.LAB_DATA_BENCHMARK, LaboratoryDataListAttributes.area, true);
//        area.setSize(120);
//        area.setNumber(12);
//
//        ColumnOption analysis = new ColumnOption(Lang.LAB_DATA_ANALYSIS, LaboratoryDataListAttributes.analysis, true);
//        analysis.setSize(200);
//        analysis.setNumber(13);
//
//        ColumnOption unit = new ColumnOption(Lang.LAB_DATA_UNIT, LaboratoryDataListAttributes.unit, true);
//        unit.setSize(120);
//        unit.setNumber(14);
//
//        ColumnOption method = new ColumnOption(Lang.LAB_DATA_METHOD, LaboratoryDataListAttributes.method, true);
//        method.setSize(120);
//        method.setNumber(15);
//
//        ColumnOption value1 = new ColumnOption(Lang.LAB_DATA_VALUE, LaboratoryDataListAttributes.value1, true);
//        value1.setSize(100);
//        value1.setNumber(16);
//
//        ColumnOption value2 = new ColumnOption(Lang.LAB_DATA_VALUE_2, LaboratoryDataListAttributes.value2, true);
//        value2.setSize(100);
//        value2.setNumber(17);
//
//        ColumnOption minLimit = new ColumnOption(Lang.LAB_DATA_MIN_LIMIT, LaboratoryDataListAttributes.minLimit, true);
//        minLimit.setSize(100);
//        minLimit.setNumber(18);
//
//        ColumnOption maxLimit = new ColumnOption(Lang.LAB_DATA_MAX_LIMIT, LaboratoryDataListAttributes.maxLimit, true);
//        maxLimit.setSize(100);
//        maxLimit.setNumber(19);
//
//        list.add(pos);
//        list.add(category);
//        list.add(lockDel);
//        list.add(date);
//        list.add(analysisDate);
//        list.add(valueOrText);
//        list.add(description);
//        list.add(group);
//        list.add(kisExtern);
//        list.add(comment);
//        list.add(range);
//        list.add(area);
//        list.add(analysis);
//        list.add(unit);
//        list.add(method);
//        list.add(value1);
//        list.add(value2);
//        list.add(minLimit);
//        list.add(maxLimit);
//        list.add(date);
//        return list;
//    }
}

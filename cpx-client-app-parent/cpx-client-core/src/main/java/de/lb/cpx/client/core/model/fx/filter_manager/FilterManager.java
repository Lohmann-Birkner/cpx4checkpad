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
package de.lb.cpx.client.core.model.fx.filter_manager;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListFormat;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import de.lb.cpx.wm.model.enums.Tp301Key30En;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * Class to manage Filteroptions on Clientside. Filteroptions mangaged by
 * ObservableArrayList, with that it's possible to detact easy changes
 *
 * @author Wilde
 */
public class FilterManager {

    private static final Logger LOG = Logger.getLogger(FilterManager.class.getName());
    
    private ObservableMap<ColumnOption, List<FilterOption>> filterOptionMap = FXCollections.observableHashMap();
    public SimpleBooleanProperty reload = new SimpleBooleanProperty(false);
    private final SearchListTypeEn listType;
    private final SearchListAttributes attributes;
    private String listName;
    private SearchListResult searchList;

    public FilterManager(final SearchListTypeEn pList, SearchListAttributes pAttributes) {
        listType = pList;
        attributes = pAttributes;
        if(listType.equals(SearchListTypeEn.RULE)){
           SearchListAttribute sel =  pAttributes.get(RuleListAttributes.ruleSelected);
           sel.setVisible(CpxClientConfig.instance().getShowRelevantRules());
        }
        setUpFilter();
    }

    public static FilterOption createFilterOption(final SearchListAttribute pAttribute) {
        if (pAttribute == null) {
            return null;
        }
        //FilterOption filterOption = new FilterOption(pAttribute.getLanguageKey(), pAttribute.getKey().getName(), null);
        FilterOption filterOption = new FilterOption(pAttribute.getLanguageKey(), pAttribute.getKey(), null);
        //filterOption.setWorkingListAttribute(pAttribute);
        return filterOption;
    }

    public void refreshFilter() {
        setUpFilter();
    }

    public String getListName() {
        return listName;
    }

    public void setUpFilterForSearchList(final SearchListResult pSearchListResult) {
        if(pSearchListResult == null){
            LOG.warning("Can not setUpFilterForSearchList, given SearchListResult is null!");
            return;
        }
//        SearchListProperties props = pSearchListResult.getSearchListProperties();
//        if (props == null) {
//            //CREATES EMPTY SearchListTypeEn WITH DEFAULT COLUMNS
//            props = CpxClientConfig.instance().getNewSearchListProperties(listType);
//        }
//        SearchListProperties workingList = pSearchListResult;
        final SearchListProperties props = pSearchListResult.getSearchListProperties();
        listName = pSearchListResult.getName();
        searchList = pSearchListResult;
        getFilterOptionMap().clear();
        //CpxLanguage lang = Session.instance().cpxLanguage;
        //for(Field f : WorkingListAttributes.class.getFields()){
        //for(Map.Entry<WorkingListKey, SearchListAttribute> entry: WorkingListAttributes.getAll().entrySet()) {
        for (Map.Entry<String, SearchListAttribute> entry : getAttributes().getAll().entrySet()) {
            //WorkingListAttributes att = WorkingListAttributes.valueOf(f.getName());
            //WorkingListAttribute att = WorkingListAttributes.get(f.getName());
            //WorkingListAttribute att = WorkingListAttributes.get(key);
            SearchListAttribute att = entry.getValue();

            /*
            List<WorkingListAttribute> attributeList = new LinkedList<>();
            if (attTmp.hasChildren()) {
              attributeList.addAll(attTmp.getChildren());
            } else {
              attributeList.add(attTmp);
            }
             */
            //for(SearchListAttribute att: attributeList) {
            /*
              if (att.hasChildren()) {
                continue;
              }
             */

 /*
              if (att == null) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot find working list attribute with name '" + key.getName() + "', probably attribute was renamed since working list was saved last time!");
                continue;
              }
             */
//            if (!att.isVisible()) {
//                continue;
//            }
            if (att.getParent() != null) {
                continue;
            }

            List<SearchListAttribute> attributeList = new LinkedList<>();
//            if (att.getDateType() == SearchListFormatDeadLine.class) {
//                attributeList.add(att);
//                attributeList.addAll(att.getChildren());
//            } else
            if (att.hasChildren()) {
                attributeList.addAll(att.getChildren());
            } else {
                attributeList.add(att);
            }

            List<FilterOption> optList = new ArrayList<>();
            for (SearchListAttribute attTmp : attributeList) {
                //List<FilterOption> optListTmp = workingList.getFilter(attTmp.getKey().getName());
                List<FilterOption> optListTmp = props == null ? new ArrayList<>() : props.getFilter(attTmp.getKey());
                if (optListTmp.isEmpty()) {
                    //optList.add(new FilterOption(att.getName(),att.getTableName(), null));
                    optListTmp.add(createFilterOption(attTmp));
                }
                optList.addAll(optListTmp);
            }
            /*
              for(FilterOption filterOption: optList) {
                filterOption.setWorkingListAttribute(att);
              }
             */
            boolean selected = false;
            int size = att.getSize();
            //ColumnOption colOption = workingList.getColumn(att.getKey().getName());
            ColumnOption colOption = props == null ? null : props.getColumn(att.getKey());
            Integer sortNumber = null;
            String sortType = "";
            Integer number = att.getNumber();
            if (colOption != null) {
                selected = colOption.isShouldShow();
                if (colOption.getSize() != null) {
                    size = colOption.getSize();
                }
                sortNumber = colOption.getSortNumber();
                sortType = colOption.getSortType();
                number = colOption.getNumber();
                //colOption.getColumnSize(); //just to clarify that size is not 0
            }

            //workingList.addColumn(colOption);
            //String displayName = lang.get(att.getName());
            //ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey().getName(), selected);
            ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey(), selected);
            //option.setWorkingListAttribute(att);
            option.setSize(size);
            option.setSortNumber(sortNumber);
            option.setSortType(sortType);
            option.setNumber(number);
//            option.isNoColumn = att.isNoColumn();
            getFilterOptionMap().put(option, optList);//, true));
        }

        //}
        Set<ColumnOption> colOptionsList = props == null ? new HashSet<>() : props.getColumns();
        for (ColumnOption colOption : getFilterOptionMap().keySet()) {
            if (!colOptionsList.contains(colOption) && props != null) {
                props.addColumn(colOption);
            }
        }
        translateColumnOption();

    }

    /*
     * SetUp Filterlist with default Values.
     * Values where generated from WorkingListAttributes
     */
    private void setUpFilter() {
        //SearchList searchList = CpxClientConfig.instance().getSelectedSearchList(listType);
        SearchListResult searchListResult = MenuCache.getMenuCacheSearchLists().getSelectedSearchList(listType);
        if (searchListResult == null) {
            searchListResult = new SearchListResult(Session.instance().getCpxUserId(), listType);
//            SearchListProperties props = CpxClientConfig.instance().getNewSearchListProperties(listType); //SearchListTypeFactory.instance().getNewSearchListProperties(SearchListTypeEn.RULEWL, Long.MIN_VALUE);
//            CSearchList sl = new CSearchList();
//            sl.setSlName("Standard");
//            sl.setSlType(listType);
//            searchListResult = new SearchListResult(sl, props);
        }
        setUpFilterForSearchList(searchListResult);
//        getFilterOptionMap().clear();
//        //CpxLanguage lang = Session.instance().cpxLanguage;
//        //for(Field f : WorkingListAttributes.class.getFields()){
//        //for(Map.Entry<WorkingListKey, SearchListAttribute> entry: WorkingListAttributes.getAll().entrySet()) {
//        for (Map.Entry<String, SearchListAttribute> entry : getAttributes().getAll().entrySet()) {
//            //WorkingListAttributes att = WorkingListAttributes.valueOf(f.getName());
//            //WorkingListAttribute att = WorkingListAttributes.get(f.getName());
//            //WorkingListAttribute att = WorkingListAttributes.get(key);
//            SearchListAttribute att = entry.getValue();
//
//            /*
//            List<WorkingListAttribute> attributeList = new LinkedList<>();
//            if (attTmp.hasChildren()) {
//              attributeList.addAll(attTmp.getChildren());
//            } else {
//              attributeList.add(attTmp);
//            }
//             */
//            //for(SearchListAttribute att: attributeList) {
//            /*
//              if (att.hasChildren()) {
//                continue;
//              }
//             */
//
// /*
//              if (att == null) {
//                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot find working list attribute with name '" + key.getName() + "', probably attribute was renamed since working list was saved last time!");
//                continue;
//              }
//             */
//            if (!att.isVisible()) {
//                continue;
//            }
//           
//            if (att.getParent() != null) {
//                continue;
//            }
//
//            List<SearchListAttribute> attributeList = new LinkedList<>();
//            if (att.getDateType() == SearchListFormatDeadLine.class) {
//                attributeList.add(att);
//                attributeList.addAll(att.getChildren());
//            } else if (att.hasChildren()) {
//                attributeList.addAll(att.getChildren());
//            } else {
//                attributeList.add(att);
//            }
//
//            List<FilterOption> optList = new ArrayList<>();
//            for (SearchListAttribute attTmp : attributeList) {
//                //List<FilterOption> optListTmp = workingList.getFilter(attTmp.getKey().getName());
//                List<FilterOption> optListTmp = workingList.getFilter(attTmp.getKey());
//                if (optListTmp.isEmpty()) {
//                    //optList.add(new FilterOption(att.getName(),att.getTableName(), null));
//                    optListTmp.add(createFilterOption(attTmp));
//                }
//                optList.addAll(optListTmp);
//            }
//            /*
//              for(FilterOption filterOption: optList) {
//                filterOption.setWorkingListAttribute(att);
//              }
//             */
//            boolean selected = false;
//            int size = att.getSize();
//            //ColumnOption colOption = workingList.getColumn(att.getKey().getName());
//            ColumnOption colOption = workingList.getColumn(att.getKey());
//            Integer sortNumber = null;
//            String sortType = "";
//            Integer number = att.getNumber();
//            if (colOption != null) {
//                selected = colOption.shouldShow;
//                size = colOption.size;
//                sortNumber = colOption.sortNumber;
//                sortType = colOption.sortType;
//                number = colOption.number;
//                //colOption.getColumnSize(); //just to clarify that size is not 0
//            }
//
//            //workingList.addColumn(colOption);
//            //String displayName = lang.get(att.getName());
//            //ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey().getName(), selected);
//            ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey(), selected);
//            //option.setWorkingListAttribute(att);
//            option.size = size;
//            option.sortNumber = sortNumber;
//            option.sortType = sortType;
//            option.number = number;
////            option.isNoColumn = att.isNoColumn();
//            getFilterOptionMap().put(option, optList);//, true));
//        }
//
//        //}
//        Set<ColumnOption> colOptionsList = workingList.getColumns();
//        for (ColumnOption colOption : getFilterOptionMap().keySet()) {
//            if (!colOptionsList.contains(colOption)) {
//                workingList.addColumn(colOption);
//            }
//        }
//        translateColumnOption();
    }

    /**
     * Get List with all set Filteroptions
     *
     * @return ObservableList
     */
    public ObservableMap<ColumnOption, List<FilterOption>> getFilterOptionMap() {
        return filterOptionMap;
    }

    public boolean hasFilter() {
        return !getFilterOptionsWithValue().isEmpty();
    }

    public List<FilterOption> getFilterOptionsWithValue() {
        ObservableMap<ColumnOption, List<FilterOption>> filterOptionsMap = getFilterOptionMap();
        List<FilterOption> filterOptions = new ArrayList<>();
        for (Iterator<Map.Entry<ColumnOption, List<FilterOption>>> it = filterOptionsMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<ColumnOption, List<FilterOption>> entry = it.next();
            for (FilterOption opt : entry.getValue()) {
                if (opt != null && opt.hasValue()) {
                    filterOptions.add(opt);
                }
            }
        }
        return filterOptions;
    }

//    /**
//     * Checks, if Filteroption with attributeName is present in the List
//     *
//     * @param attributeName Name of the Attribute
//     * @return boolean
//     */
//    public boolean containsAttribute(String attributeName) {
//        return getFilterOptionMap().containsKey(attributeName);//.stream().anyMatch((op) -> (op.name.equals(attributeName)));    
//    }
//
//    /**
//     * Get the Filteroption, with a attributeName.
//     *
//     * @param attributeName Name of the FilterOption-Attribute
//     * @param index index of Filteroption for Attribute
//     * @return FilterOption or null, if list do not contain a Value with given
//     * Name
//     */
//    public FilterOption getFilterOption(String attributeName, int index) {
//        try {
//            return getFilterOptionMap().get(attributeName).get(index);
//        } catch (ArrayIndexOutOfBoundsException ex) {
//            LOG.log(Level.SEVERE, MessageFormat.format("Attribute {0} does not exist", attributeName), ex);
//            return null;
//        }
//    }
    public List<FilterOption> getFilterOptions(ColumnOption opt) {
        return getFilterOptionMap().get(opt);
    }

    public ColumnOption getColumnOption(String attString) {
        for (ColumnOption colOption : getFilterOptionMap().keySet()) {
            if (colOption.attributeName.equals(attString)) {
                return colOption;
            }
        }
        return null;
    }

    public Stream<ColumnOption> getSortedStreamOfColumns() {
        //return filterOptionMap.keySet().stream().sorted((ColumnOption o1, ColumnOption o2) -> o1.displayName.compareTo(o2.displayName));
        return getFilterOptionMap().keySet().stream().sorted((ColumnOption o1, ColumnOption o2) -> o1.compareTo(o2));
    }

    /**
     * @return sorted streams of columns by number
     */
    public Stream<ColumnOption> getSortedByNumber() {
        return getFilterOptionMap().keySet().stream().sorted(Comparator.comparing(ColumnOption::getIndex, nullsLast(naturalOrder())));//(ColumnOption o1, ColumnOption o2) -> Integer.compare(o1.number, o2.number));
    }

    public void removeFromFilter(FilterOption labelText) {
        //WorkingList workingList = CpxClientConfig.instance().getSearchListProperties(Session.instance().selectedWorkingList);
        getFilterOptionMap().keySet().stream().filter((opt) -> (opt.attributeName.equals(labelText.name))).forEach((opt) -> {
            //workingList.removeFilter(labelText.name, labelText.counter);
            getFilterOptionMap().get(opt).remove(labelText);
        });
        //CpxClientConfig.instance().setSearchListProperties(workingList);
    }

//    public int getMaxFilterCounter(final String attName) {
//        int counter = 0;
//        for (ColumnOption opt : getFilterOptionMap().keySet()) {
//            List<FilterOption> list = getFilterOptionMap().get(opt);
//            for (FilterOption filterOption : list) {
//                if (filterOption.name.equals(attName) && filterOption.getCounter() > counter) {
//                    counter = filterOption.getCounter();
//                }
//            }
//        }
//        return counter;
//    }
//
//    public int getNextFilterCounter(final String attName) {
//        return getMaxFilterCounter(attName) + 1;
//    }
    public FilterOption addToFilter(String pKey) {
        //WorkingListAttributes att = WorkingListAttributes.valueOf(attName);
        SearchListAttribute att = getAttributes().get(pKey);
        //FilterOption filterOpt = new FilterOption(att.getName(), att.getTableName(), null);
        FilterOption filterOpt = new FilterOption(att.getLanguageKey(), att.getKey(), null);
        //int counter = getNextFilterCounter(pKey.getName());
//        int counter = getNextFilterCounter(pKey);
//        filterOpt.setCounter(counter);
        for (ColumnOption opt : getFilterOptionMap().keySet()) {
            //if(opt.attributeName.equals(pKey.getName())){
            if (opt.attributeName.equals(pKey)) {
                getFilterOptionMap().get(opt).add(filterOpt);
                return filterOpt;
            }
        }
        //add new Columnoption .. should not happen
        //ColumnOption opt = new ColumnOption(att.getLanguageKey(), att.getKey().getName(), true);
        ColumnOption opt = new ColumnOption(att.getLanguageKey(), att.getKey(), true);
        ArrayList<FilterOption> filterList = new ArrayList<>();
        filterList.add(filterOpt);
        getFilterOptionMap().put(opt, filterList);
        return filterOpt;
    }

    /**
     * translates the Displayname,Tooltip and abbreviation of selected Columns
     */
    @SuppressWarnings("unchecked")
    public void translateColumnOption() {
        for (ColumnOption colOption : getFilterOptionMap().keySet()) {
            //colOption.number = colOption.getNumber();
            //colOption.displayName = Lang.get(WorkingListAttributes.get(colOption.attributeName).getLanguageKey()).value;
            //System.out.println(WorkingListAttributes.instance().get(colOption.attributeName).getLanguageKey());
//            Translation trans;
//            if (listType == SearchListTypeEn.WORKING) {
//                trans = Lang.get(WorkingListAttributes.instance().get(colOption.attributeName).getLanguageKey());
//            } else if (listType == SearchListTypeEn.WORKFLOW) {
//                trans = Lang.get(WorkflowListAttributes.instance().get(colOption.attributeName).getLanguageKey());
//            } else {
//                continue;
//            }
//            StringBuilder b = new StringBuilder();
//            if (trans.hasAbbreviation()) {
//                b.append(trans.abbreviation);
//                if (trans.hasValue() && !trans.abbreviation.contains(trans.value)) {
//                    b.append(" ").append("(").append(trans.value).append(")");
//                }
//            } else {
//                b.append(trans.value);
//            }
            String displayName = colOption.getDisplayName(listType);
            String tooltip = colOption.getTooltip(listType);
            colOption.setDisplayName(displayName);
            StringBuilder b = new StringBuilder(displayName);
            b.delete(0, b.length());
            if (tooltip != null && !tooltip.isEmpty()) {
                b.append(tooltip);
            }
            SearchListAttribute attributeName = getAttributes().get(colOption.attributeName);
            SearchListFormat<?> format = attributeName.getFormat();
            Serializable dataType = format.getDataType();
            final Class<CpxEnumInterface<?>> cpxEnumClazz = (dataType != null && CpxEnumInterface.class.isAssignableFrom(((Class<?>) dataType))) ? (Class<CpxEnumInterface<?>>) dataType : null;
            final Map<String, String> translationMap = new HashMap<>();
            final Map<String, String> abbreviationMap = new HashMap<>();

            //Tooltip on table view header cells
            //if (Enum.class.isAssignableFrom((Class<?>) dataType)) {
            if (cpxEnumClazz != null) {
                Enum<?>[] items = ((Class<Enum<?>>) dataType).getEnumConstants();
                if(attributeName.getLanguageKey().equals(Lang.PROCESS_INKA_KEY)){
                    items = Tp301Key30En.getValues(Tp301Key30En.Values.INKA);
                }
                if(attributeName.getLanguageKey().equals(Lang.PROCESS_KAIN_KEY)){
                    items = Tp301Key30En.getValues(Tp301Key30En.Values.KAIN);
                }
                for (Enum<?> item : items) {
                    if (b.length() > 0) {
                        b.append("\r\n");
                    }
                    CpxEnumInterface<?> obj = ((CpxEnumInterface) item);
                    Translation transTmp = Lang.get(obj.getLangKey());
                    String valTmp = obj.toString();
                    translationMap.put(obj.getViewId(), transTmp.getValue());
                    abbreviationMap.put(obj.getViewId(), transTmp.getAbbreviation());
                    b.append(valTmp);
                }
            }
            if (b.length() > 0) {
                colOption.setToolTipText(b.toString());
            }
        }
    }

    /**
     * @return the attributes
     */
    public SearchListAttributes getAttributes() {
        return attributes;
    }

    public SearchListTypeEn getListType() {
        return listType;
    }

    /**
     * @param filterOptionMap the filterOptionMap to set
     */
    public void setFilterOptionMap(ObservableMap<ColumnOption, List<FilterOption>> filterOptionMap) {
        this.filterOptionMap = filterOptionMap;
    }

    /**
     * @return current searchlist/filter
     */
    public SearchListResult getSearchList() {
        return searchList;
    }

}

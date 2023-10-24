/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filterlists.processes;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.menu.fx.filterlists.SearchListColumn;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.filter.enums.ActionSubjectMap;
import de.lb.cpx.shared.filter.enums.InsShortMap;
import de.lb.cpx.shared.filter.enums.InsuranceMap;
import de.lb.cpx.shared.filter.enums.MdkAuditReasonsMap;
import de.lb.cpx.shared.filter.enums.MdkStatesMap;
import de.lb.cpx.shared.filter.enums.ProcessResultMap;
import de.lb.cpx.shared.filter.enums.ProcessTopicMap;
import de.lb.cpx.shared.filter.enums.ReminderSubjectMap;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.UserMap;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.enums.Tp301Key30En;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Column implemenation for the workflow list
 *
 * @author wilde
 */
public class WorkflowListColumn extends SearchListColumn<WorkflowListItemDTO, Object> {

    private static final Logger LOG = Logger.getLogger(WorkflowListColumn.class.getName());

    public WorkflowListColumn(SearchListAttribute pAttribute) {
        //super(pAttribute.getLanguageKey(), pAttribute.getKey(), pAttribute.getDataType());
        super(pAttribute);
        setDisableFilter(pAttribute.isNoFilter());
        setPrefWidth(pAttribute.getSize());
    }

    @Override
    protected String transfromFilterValue(String pContent, Object pDataType) {
        if (pDataType == ReminderSubjectMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getReminderSubjectsForInternalId(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == MdkStatesMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getRequestStatesForInternalId(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == UserMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getUserNameForId(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == ProcessTopicMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getProcessTopicForId(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == ActionSubjectMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getActionSubjectName(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == ProcessResultMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getProcessResultForId(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == MdkAuditReasonsMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getAuditReasonForNumber(Integer.valueOf(item))).collect(Collectors.joining(","));
        }
        //CPX-994 RSH 20180815
        if (pDataType == InsuranceMap.class) {
            Arrays.stream(pContent.split(",")).map(item -> CpxInsuranceCompanyCatalog.instance().findInsNameByInsuranceNumber(String.valueOf(item), AbstractCpxCatalog.DEFAULT_COUNTRY)).collect(Collectors.joining(","));
        }
        if (pDataType == InsShortMap.class) {
            Arrays.stream(pContent.split(",")).map(item -> CpxInsuranceCompanyCatalog.instance().findInsShortNameByInsuranceNumber(String.valueOf(item), AbstractCpxCatalog.DEFAULT_COUNTRY)).collect(Collectors.joining(","));
        }
//        if (pDataType == WmReminderStatusEn.class) {
//            return Arrays.stream(pContent.split(",")).map(item -> WmReminderStatusEn.findById(Integer.parseInt(item)).getTranslation().getValue()).collect(Collectors.joining(","));
//        }
        if (pDataType == WmWorkflowStateEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> WmWorkflowStateEn.findById(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        if (pDataType == WmRequestTypeEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> WmRequestTypeEn.findById(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        if (pDataType == WmEventTypeEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> WmEventTypeEn.findById(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        if (pDataType == Tp301Key30En.class) {
            return pContent;
        }
        if (pDataType == Boolean.class) {
            if (WorkflowListAttributes.lock.equals(getSearchAttribute().getKey())) {
                return lockConverter.toString(pContent);
            }
            if (WorkflowListAttributes.isCancel.equals(getSearchAttribute().getKey())) {
                return cancelConverter.toString(pContent);
            }
            if (WorkflowListAttributes.wvPrio.equals(getSearchAttribute().getKey())) {
                return prioConverter.toString(pContent);
            }
            if (WorkflowListAttributes.remFinished.equals(getSearchAttribute().getKey())) {
                return remFinishedConverter.toString(pContent);
            }
        }
//        if(pDataType == SearchListFormatDeadLine.class){
//            return Lang.toDate(dateConverter.fromString(pContent));
//        }
        return super.transfromFilterValue(pContent, pDataType);
    }

    @Override
    protected Node transfromCellNode(String pKey, Object pContent, Object pDataType) {
        if (pDataType == Boolean.class) {
            if (WorkflowListAttributes.lock.equals(pKey)) {
                if ((boolean) pContent) {
                    return new Label("", ResourceLoader.getGlyph(FontAwesome.Glyph.LOCK));
                } else {
                    return null;
                }
            }

            if (WorkflowListAttributes.isCancel.equals(pKey)) {
                if ((boolean) pContent) {
                    return new Label("", ResourceLoader.getGlyph(FontAwesome.Glyph.BAN));
                } else {
                    return null;
                }
            }
            if (WorkflowListAttributes.wvPrio.equals(pKey)) {
                if ((boolean) pContent) {
                    return new Label("", ResourceLoader.getGlyph(FontAwesome.Glyph.CHECK));
                } else {
                    return null;
                }
            }
            if (WorkflowListAttributes.remFinished.equals(pKey)) {
                return new Label(((boolean)pContent)?Lang.getReminderFinishedStatus():Lang.getReminderUnfinishedStatus());
            }
            
            if(WorkflowListAttributes.auditNames.equals(pKey)){
               return null; 
            }
        }
        if (pDataType == WmRequestTypeEn.class) {
            final WmRequestTypeEn item = WmRequestTypeEn.findById((Integer) pContent);
            return new Label(item == null ? null : item.getTranslation().getValue());
            //return new Label(WmRequestTypeEn.findById((Integer) pContent).getTranslation().getValue());
        }
        if (pDataType == WmEventTypeEn.class) {
            final WmEventTypeEn item = WmEventTypeEn.findById((Integer) pContent);
            return new Label(item == null ? null : item.getTranslation().getValue());
            //return new Label(WmEventTypeEn.findById((Integer) pContent).getTranslation().getValue());
        }
        if (pDataType == Tp301Key30En.class) {
//            final Tp301Key30En item = Tp301Key30En.findById((String)pContent);
            return new Label( pContent == null?"":(String)pContent);
            
        }
        
        return super.transfromCellNode(pKey, pContent, pDataType); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String transfromCellValue(Object pContent, Object pDataType) {
        if (pDataType == ReminderSubjectMap.class) {
            return MenuCache.instance().getReminderSubjectsForInternalId((Long) pContent);
        }
        if (pDataType == MdkStatesMap.class) {
            return MenuCache.instance().getRequestStatesForInternalId((Long) pContent);
        }
        if (pDataType == UserMap.class) {
//            return MenuCache.instance().getUserNameForId((Long) pContent);
            UserDTO user = MenuCache.instance().getUserMapEntryForId((Long) pContent);

            //return user != null ? user.getUserName() + (user.isInActive() ? " - Inaktiv" : "") : "";
            return user != null ? user.getName() : "";
        }
        if (pDataType == ProcessTopicMap.class) {
            return MenuCache.instance().getProcessTopicForId((Long) pContent);
        }
        if (pDataType == ActionSubjectMap.class) {
            return MenuCache.instance().getActionSubjectName((Long)pContent);
        }
        //CPX-1028 RSH 20180713
        if (pDataType == ProcessResultMap.class) {
            return MenuCache.instance().getProcessResultForId((Long) pContent);
        }
        if (pDataType == MdkAuditReasonsMap.class) {
            if(pContent instanceof Number){
                return MenuCache.instance().getAuditReasonForNumber(((Number) pContent).intValue());
            }else if(pContent instanceof String){
                    String ret = Arrays.stream(((String)pContent).split(", ")).map(item->Integer.parseInt(item.trim())).sorted()
                        .map(item -> MenuCache.instance().getAuditReasonForNumber(item, MenuCacheOptionsEn.ALL))
                                .collect(Collectors.joining(","));
                LOG.log(Level.FINER, "retStr= " + ret);
                return ret;
            }
            return "";
        }
        if(pDataType == Tp301Key30En.class){
            return "abc";
        }


        
        return super.transfromCellValue(pContent, pDataType);
    }

    @Override
    protected List<Node> transformFilterNodes(Object pDataType, final SearchListAttribute.OPERATOR pOperator) {
        List<Node> nodes = new ArrayList<>();
        if (pDataType == Boolean.class) {
            if (WorkflowListAttributes.lock.equals(getSearchAttribute().getKey())) {
                nodes.add(getLockToogleGroup(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
                return nodes;
            }
            if (WorkflowListAttributes.isCancel.equals(getSearchAttribute().getKey())) {
                nodes.add(getCancelToogleGroup(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
                return nodes;
            }
            if (WorkflowListAttributes.wvPrio.equals(getSearchAttribute().getKey())) {
                nodes.add(getPrioToogleGroup(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
                return nodes;
            }
            if (WorkflowListAttributes.remFinished.equals(getSearchAttribute().getKey())) {
                nodes.add(getRemFinishedToogleGroup(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
                return nodes;
            }
        }
        if (pDataType == ReminderSubjectMap.class) {
            nodes.add(getReminderSubjectComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == UserMap.class) {
            nodes.add(getUserNameComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == ProcessTopicMap.class) {
            nodes.add(getProcessTopicComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == ProcessResultMap.class) {
            nodes.add(getProcessResultComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == MdkAuditReasonsMap.class) {
            nodes.add(getMdkAuditReasonsComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == MdkStatesMap.class) {
            nodes.add(getMdkStatesComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == ActionSubjectMap.class) {
            nodes.add(getActionSubjectsComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == InsuranceMap.class) {
            nodes.add(getInsuranceComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == InsShortMap.class) {
            nodes.add(getInsShortComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
//        if (pDataType == DepartmentShortNameMap.class) {
//        }
//        if (pDataType == WmReminderStatusEn.class) {
//            nodes.add(getReminderStatusCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
//            return nodes;
//        }
        if (pDataType == WmWorkflowStateEn.class) {
            nodes.add(getWorkflowStateCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == WmRequestTypeEn.class) {
            nodes.add(getRequestTypeCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == WmEventTypeEn.class) {
            nodes.add(getWmEventTypeCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if(pDataType == Tp301Key30En.class){
            nodes.add(getKainInkaCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
            
        }
        return super.transformFilterNodes(pDataType, pOperator);
    }

    public CheckComboBox<Map.Entry<Long, String>> getMdkAuditReasonsComboBox(String pDataKey, String pLangKey) {
//        Set<Map.Entry<Long, String>> entries = MenuCache.instance().getMdkAuditReasonsEntries();
//        //now... let's convert Map<Integer, String> to Map<Long, String>!
//        Set<Map.Entry<Long, String>> newEntries = new LinkedHashSet<>();
//        for (Map.Entry<Integer, String> entry : entries) {
//            Map.Entry<Long, String> newEntry = new AbstractMap.SimpleEntry<>(Long.valueOf(entry.getKey()), entry.getValue());
//            newEntries.add(newEntry);
//        }
//        ArrayList<Entry<Long, String>> list = new ArrayList<>(newEntries);
        List<Entry<Long, String>> list = new ArrayList<>(MenuCache.instance().getAuditReasonsEntries());
//        final Collator collator = Collator.getInstance(Locale.GERMAN);
//        Collections.sort(list, (o1, o2) -> {
//            return collator.compare(o1.getValue(), o2.getValue());
//            //return o1.getValue().compareToIgnoreCase(o2.getValue());
//        });
        return new MapCheckedComboBox(pDataKey, pLangKey, list);
    }

    public CheckComboBox<Map.Entry<Long, String>> getMdkStatesComboBox(String pDataKey, String pLangKey) {
        List<Entry<Long, String>> list = new ArrayList<>(MenuCache.instance().getRequestStatesEntries());
//        final Collator collator = Collator.getInstance(Locale.GERMAN);
//        Collections.sort(list, (o1, o2) -> {
//            return collator.compare(o1.getValue(), o2.getValue());
//            //return o1.getValue().compareToIgnoreCase(o2.getValue());
//        });
        return new MapCheckedComboBox(pDataKey, pLangKey, list);
    }
    
    public CheckComboBox<Map.Entry<Long, String>> getActionSubjectsComboBox(String pDataKey, String pLangKey) {
        List<Entry<Long, String>> list = new ArrayList<>(MenuCache.instance().getActionSubjectEntries());
//        final Collator collator = Collator.getInstance(Locale.GERMAN);
//        Collections.sort(list, (o1, o2) -> {
//            return collator.compare(o1.getValue(), o2.getValue());
//            //return o1.getValue().compareToIgnoreCase(o2.getValue());
//        });
        return new MapCheckedComboBox(pDataKey, pLangKey, list);
    }

    public CheckComboBox<Map.Entry<Long, String>> getEventTypeComboBox(String pDataKey, String pLangKey) {
        WmEventTypeEn[] values = WmEventTypeEn.values();
        Map<Long, String> map = new LinkedHashMap<>();
        for (WmEventTypeEn val : values) {
            map.put(Long.valueOf(val.getIdInt()), val.getTranslation().abbreviation);
        }
        ArrayList<Entry<Long, String>> list = new ArrayList<>(map.entrySet());
        final Collator collator = Collator.getInstance(Locale.GERMAN);
        Collections.sort(list, (o1, o2) -> {
            return collator.compare(o1.getValue(), o2.getValue());
            //return o1.getValue().compareToIgnoreCase(o2.getValue());
        });
        return new MapCheckedComboBox(pDataKey, pLangKey, list);
    }

    private CheckComboBox<Map.Entry<String, String>> getInsuranceComboBox(String pDataKey, String pLangKey) {
        return new MapCheckedComboBoxString(pDataKey, pLangKey, new ArrayList<>(MenuCache.instance().getInsuranceEntries()));
    }

    private CheckComboBox<Map.Entry<String, String>> getInsShortComboBox(String pDataKey, String pLangKey) {
        final LinkedHashMap<String, String> m = new LinkedHashMap<>();
        for(String shortName: MenuCache.getMenuCacheInsShort().getShortNames()) {
            m.put(shortName, shortName);
        }
        return new MapCheckedComboBoxString(pDataKey, pLangKey, new ArrayList<>(m.entrySet()));
    }

    private CheckComboBox<Map.Entry<Long, String>> getReminderSubjectComboBox(String pDataKey, String pLangKey) {
        return new MapCheckedComboBox(pDataKey, pLangKey, new ArrayList<>(MenuCache.instance().getReminderSubjectEntries()));
    }

    private CheckComboBox<Map.Entry<Long, String>> getUserNameComboBox(String pDataKey, String pLangKey) {
        List<Entry<Long, String>> list = new ArrayList<>(MenuCache.instance().getUserNamesEntries());
        final Collator collator = Collator.getInstance(Locale.GERMAN);
        Collections.sort(list, (o1, o2) -> {
            return collator.compare(o1.getValue(), o2.getValue());
            //return o1.getValue().compareToIgnoreCase(o2.getValue());
        });
//        List<Entry<Long, String>> userNames = users.stream().map((t) -> {
//            return new Entry<Long, String>() {
//                @Override
//                public Long getKey() {
//                    return t.getKey();
//                }
//
//                @Override
//                public String getValue() {
//                    return t.getValue().getUserName() + (t.getValue().isInActive() ? " - Inaktiv" : "");
//                }
//
//                @Override
//                public String setValue(String value) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//            };
//        }).collect(Collectors.toList());
        return new MapCheckedComboBox(pDataKey, pLangKey, list);//new ArrayList<>(MenuCache.instance().getUserNamesEntries()));
    }

    private CheckComboBox<Map.Entry<Long, String>> getProcessTopicComboBox(String pDataKey, String pLangKey) {
        return new MapCheckedComboBox(pDataKey, pLangKey, new ArrayList<>(MenuCache.instance().getProcessTopicsEntries()));
    }

    private CheckComboBox<Map.Entry<Long, String>> getProcessResultComboBox(String pDataKey, String pLangKey) {
        return new MapCheckedComboBox(pDataKey, pLangKey, new ArrayList<>(MenuCache.instance().getProcessResultEntries()));
    }

//    private CheckComboBox<WmReminderStatusEn> getReminderStatusCombobox(String pDataKey, String pLangKey) {
//        return new EnumCheckedComboBox<WmReminderStatusEn>(pDataKey, pLangKey, WmReminderStatusEn.values()) {
//            @Override
//            public String getCheckedItems(ObservableList<WmReminderStatusEn> pItems) {
//                return pItems.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(","));
//            }
//
//            @Override
//            public String getLocalizedCheckedItems(ObservableList<WmReminderStatusEn> pItems) {
//                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
//            }
//
//            @Override
//            public WmReminderStatusEn getEnum(String pStr) {
//                return WmReminderStatusEn.findById(Integer.parseInt(pStr));
//            }
//        };
//    }
    private CheckComboBox<WmWorkflowStateEn> getWorkflowStateCombobox(String pDataKey, String pLangKey) {
        return new EnumCheckedComboBox<WmWorkflowStateEn>(pDataKey, pLangKey, WmWorkflowStateEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<WmWorkflowStateEn> pItems) {
                return pItems.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<WmWorkflowStateEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public WmWorkflowStateEn getEnum(String pStr) {
                return WmWorkflowStateEn.findById(pStr);
            }
        };
    }

    private CheckComboBox<WmEventTypeEn> getWmEventTypeCombobox(String pDataKey, String pLangKey) {
        return new EnumCheckedComboBox<WmEventTypeEn>(pDataKey, pLangKey, WmEventTypeEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<WmEventTypeEn> pItems) {
                return pItems.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<WmEventTypeEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public WmEventTypeEn getEnum(String pStr) {
                return WmEventTypeEn.findById(pStr);
            }
        };
    }
    private CheckComboBox<Tp301Key30En> getKainInkaCombobox(String pDataKey, String pLangKey) {
        return new EnumCheckedComboBox<Tp301Key30En>(pDataKey, pLangKey, 
                Tp301Key30En.getValues(pDataKey.equals(WorkflowListAttributes.kainKeyEn)?Tp301Key30En.Values.KAIN:
                        (pDataKey.equals(WorkflowListAttributes.inkaKeyEn)?Tp301Key30En.Values.INKA:""))) {   
            @Override
            public String getCheckedItems(ObservableList<Tp301Key30En> pItems) {
                return pItems.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<Tp301Key30En> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public Tp301Key30En getEnum(String pStr) {
                return Tp301Key30En.findById(pStr);
            }
        };
    }

    private CheckComboBox<WmRequestTypeEn> getRequestTypeCombobox(String pDataKey, String pLangKey) {
        return new EnumCheckedComboBox<WmRequestTypeEn>(pDataKey, pLangKey, WmRequestTypeEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<WmRequestTypeEn> pItems) {
                return pItems.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<WmRequestTypeEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public WmRequestTypeEn getEnum(String pStr) {
                return WmRequestTypeEn.findById(pStr);
            }
        };
    }

    //TODO:
    //FIX ME: better converter handling
    private BooleanToggleGroup getLockToogleGroup(String pDataKey, String pLangKey) {
        BooleanToggleGroup lockGroup = new BooleanToggleGroup(pDataKey, pLangKey) {
        };
        lockGroup.setTrueText(Lang.getFilterProcessLock());
        lockGroup.setFalseText(Lang.getFilterProcessUnlock());
        lockGroup.setAllText(Lang.getFilterNo());
        lockGroup.setResultConverter(lockConverter);
        return lockGroup;
    }

    private BooleanToggleGroup getCancelToogleGroup(String pDataKey, String pLangKey) {
        BooleanToggleGroup cancelGroup = new BooleanToggleGroup(pDataKey, pLangKey) {
        };
        cancelGroup.setTrueText(Lang.getFilterProcessCancel());
        cancelGroup.setFalseText(Lang.getFilterProcessCancelNot());
        cancelGroup.setAllText(Lang.getFilterNo());
        cancelGroup.setResultConverter(cancelConverter);
        return cancelGroup;
    }

    private BooleanToggleGroup getPrioToogleGroup(String pDataKey, String pLangKey) {
        BooleanToggleGroup prioGroup = new BooleanToggleGroup(pDataKey, pLangKey) {
        };
        prioGroup.setTrueText(Lang.getFilterPrio());
        prioGroup.setFalseText(Lang.getFilterPrioNo());
        prioGroup.setAllText(Lang.getFilterNo());
        prioGroup.setResultConverter(prioConverter);
        return prioGroup;
    }

    private BooleanToggleGroup getRemFinishedToogleGroup(String pDataKey, String pLangKey) {
        BooleanToggleGroup remFinishedGroup = new BooleanToggleGroup(pDataKey, pLangKey) {
        };
        remFinishedGroup.setTrueText(Lang.getFilterRemeinderClose());
        remFinishedGroup.setFalseText(Lang.getFilterRemeinderOpen());
        remFinishedGroup.setAllText(Lang.getFilterNo());
        remFinishedGroup.setResultConverter(remFinishedConverter);
        return remFinishedGroup;
    }

    //TODO: FIXME make better converter handling
    private final StringConverter<String> prioConverter = new StringConverter<String>() {
        @Override
        public String toString(String object) {
            if (object == null) {
                return "";
            }
            switch (object) {
                case "0":
                    return Lang.getFilterPrioNo();
                case "1":
                    return Lang.getFilterPrio();
                default:
                    return "";
            }
        }

        @Override
        public String fromString(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    //TODO: FIXME make better converter handling
    private final StringConverter<String> remFinishedConverter = new StringConverter<String>() {
        @Override
        public String toString(String object) {
            switch (object) {
                case "0":
                    return Lang.getFilterRemeinderOpen();
                case "1":
                    return Lang.getFilterRemeinderClose();
                default:
                    return "";
            }
        }

        @Override
        public String fromString(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };

//    //TODO FIX ME: Do not know if it is good to group 
//    //similar items together or it is smarter to group all till a radiobutton appears
//    //requirements are unclear
//    private class DeadlineToogleGroupLayout extends VBox {
//
//        private final ToggleGroup group = new ToggleGroup();
//        private static final String NO_FILTER = "NO_FILTER";
//
//        public DeadlineToogleGroupLayout(SearchListAttribute pAttribute) {
//            super();
//            setSpacing(8.0);
//            List<DeadlineToogleItemLayout> items = new ArrayList<>();
//            for (SearchListAttribute children : pAttribute.getChildren()) {
//                String filterItem = getFilterValue(children.getKey());
////                if(children.getDateType() == SearchListFormatDeadLineRadioButton.class){
////                    DeadlineToogleItemLayout toogleLayout = createItemLayout(children.getDateType());
////                    toogleLayout.add(createControl(children,toogleLayout.getRadioButton()));
////                    toogleLayout.getRadioButton().setUserData(children);
////                    toogleLayout.getRadioButton().setId(children.getKey());
////                    toogleLayout.getRadioButton().setSelected(filterItem!=null);
////                    items.add(toogleLayout);
////                }else{
////                    //try to search for fitting item layout to group similar items together
////                    DeadlineToogleItemLayout similarEntry = null;
////                    for(DeadlineToogleItemLayout item : items){
////                        if(item.getType() == SearchListFormatDeadLineRadioButton.class){
////                            continue;
////                        }
////                        if(item.getType() == children.getDateType()){
////                            similarEntry = item;
////                            break;
////                        }
////                    }
////                    if(similarEntry != null){
////                        //prevent deselection
////                        if(!similarEntry.getRadioButton().isSelected()){
////                            similarEntry.getRadioButton().setSelected(filterItem!=null);
////                        }
////                        Control ctrl = createControl(children,similarEntry.getRadioButton());
////                        ctrl.disableProperty().bind(similarEntry.getRadioButton().selectedProperty().not());
////                        similarEntry.add(ctrl);
////                    }else{
////                        DeadlineToogleItemLayout newEntry = createItemLayout(children.getDateType());
////                        newEntry.getRadioButton().setSelected(filterItem!=null);
////                        Control ctrl = createControl(children,newEntry.getRadioButton());
////                        newEntry.add(ctrl);
////                        items.add(newEntry);
////                    }
////                }
//            }
//            getChildren().addAll(items);
//            //set default no filter toogle
//            DeadlineToogleItemLayout noFilter = createItemLayout(null);
//            noFilter.add(new Label("Kein Filter"));
//            noFilter.getRadioButton().setId(NO_FILTER);
//            getChildren().add(noFilter);
//            if (group.getSelectedToggle() == null) {
//                noFilter.getRadioButton().setSelected(true);
//            }
//            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//                @Override
//                public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                    if (NO_FILTER.equals(((Styleable) newValue).getId())) {
//                        for (SearchListAttribute att : pAttribute.getChildren()) {
//                            getOnFilterDelete().call(att.getKey());
//                        }
//                        //dummy filter to trigger reload of list, and redraw
//                        filter(pAttribute.getKey(), pAttribute.getLanguageKey(), ""/*,""*/);
//                        return;
//                    }
//                    if (oldValue != null && oldValue.getUserData() != null) {
//                        if (getOnFilterDelete() != null) {
//                            getOnFilterDelete().call(((SearchListAttribute) oldValue.getUserData()).getKey());//pAttribute.getKey());
//                        }
//                    }
//                    SearchListAttribute child = (SearchListAttribute) newValue.getUserData();
////                    if(child!= null && (child.getDateType() == SearchListFormatDeadLineRadioButton.class)){
////                        filter(child.getKey(), child.getLanguageKey(), Lang.toIsoDate(new Date())/*, Lang.toDate(new Date())*/);
////                    }
//                }
//            });
//        }
//
//        private DeadlineToogleItemLayout createItemLayout(Serializable pDataType) {
//            DeadlineToogleItemLayout noFilter = new DeadlineToogleItemLayout(pDataType);
//            noFilter.getRadioButton().setToggleGroup(group);
//            return noFilter;
//        }
//
////        private Control createControl(SearchListAttribute pAttribute, Toggle pToggle) {
//////            if(pAttribute.getDateType() == SearchListFormatDeadLineRadioButton.class){
//////                return new Label(pAttribute.getLanguageKey());
//////            }
//////            if(pAttribute.getDateType() == SearchListFormatDeadLineDateTime.class){
//////                LabeledDatePicker dp = getLabeledDatePicker(pAttribute.getKey(), pAttribute.getLanguageKey(), Lang.get(pAttribute.getLanguageKey()).getValue());
//////                dp.disableProperty().bind(pToggle.selectedProperty().not());
//////                dp.disabledProperty().addListener(new ChangeListener<Boolean>() {
//////                    @Override
//////                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//////                        if(newValue){
//////                            if(getOnFilterDelete() != null){
//////                                getOnFilterDelete().call(pAttribute.getKey());//pAttribute.getKey());
//////                            }
//////                            dp.getControl().setValue(null);
//////                        }
//////                    }
//////                });
//////                return dp;
//////            }
////            return new Label(pAttribute.getDataType().getClass().getSimpleName());
////        }
//    }
//
//    private class DeadlineToogleItemLayout extends HBox {
//
//        private final RadioButton radioBtn = new RadioButton();
//        private final VBox content = new VBox();
//        private final Serializable type;
//
//        public DeadlineToogleItemLayout(Serializable pDataType) {
//            super();
//            getChildren().addAll(radioBtn, content);
//            type = pDataType;
//        }
//
//        public RadioButton getRadioButton() {
//            return radioBtn;
//        }
//
//        public void add(Control pCtrl) {
//            content.getChildren().add(pCtrl);
//        }
//
//        public Serializable getType() {
//            return type;
//        }
//    }
}

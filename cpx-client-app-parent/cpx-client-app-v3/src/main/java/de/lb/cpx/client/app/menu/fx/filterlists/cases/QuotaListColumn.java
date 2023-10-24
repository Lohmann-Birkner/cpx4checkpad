/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filterlists.cases;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxMdk;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.QuarterEn;
import de.lb.cpx.shared.filter.enums.MdkMap;
import de.lb.cpx.shared.filter.enums.MdkStatesMap;
import de.lb.cpx.shared.filter.enums.ProcessResultMap;
import de.lb.cpx.shared.filter.enums.ProcessTopicMap;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.controlsfx.control.CheckComboBox;

/**
 * Column implementation for the audit quota list implements specific filters
 * and behavior
 *
 * @author niemeier
 */
public class QuotaListColumn extends WorkingListColumn {

    public QuotaListColumn(SearchListAttribute pAttribute) {
        super(pAttribute);
        setDisableFilter(pAttribute.isNoFilter());
        //searchAttribute = pAttribute;
        setPrefWidth(pAttribute.getSize());
    }

//    @Override
//    protected Node transfromCellNode(String pKey, Object pContent, Object pDataType) {
//        return super.transfromCellNode(pKey, pContent, pDataType);
//    }
    @Override
    protected List<Node> transformFilterNodes(Object pDataType, final SearchListAttribute.OPERATOR pOperator) {
        List<Node> nodes = new ArrayList<>();
        if (pDataType == ProcessTopicMap.class) {
            nodes.add(getProcessTopicComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == ProcessResultMap.class) {
            nodes.add(getProcessResultComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == MdkStatesMap.class) {
            nodes.add(getMdkStatesComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == MdkMap.class) {
            nodes.add(getMdkComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == WmWorkflowStateEn.class) {
            nodes.add(getWorkflowStateCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == QuarterEn.class) {
            nodes.add(getQuarterCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == PlaceOfRegEn.class) {
            nodes.add(getPlaceOfRegCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
//        if (pDataType == RuleTypeEn.class) {
//            nodes.add(getRuleTypeComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
//            return nodes;
//        }
        return super.transformFilterNodes(pDataType, pOperator);
    }

    @Override
    protected String transfromFilterValue(String pContent, Object pDataType) {
        if (pDataType == CaseStatusEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> CaseStatusEn.valueOf(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        if (pDataType == MdkStatesMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getRequestStatesForInternalId(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == ProcessTopicMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getProcessTopicForId(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == ProcessResultMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getProcessResultForId(Long.parseLong(item))).collect(Collectors.joining(","));
        }
        if (pDataType == MdkMap.class) {
            return Arrays.stream(pContent.split(",")).map(item -> String.valueOf(CpxMdkCatalog.instance().getById(Long.parseLong(item)))).collect(Collectors.joining(","));
        }
        if (pDataType == WmWorkflowStateEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> WmWorkflowStateEn.findById(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        if (pDataType == PlaceOfRegEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> PlaceOfRegEn.findByName(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        return super.transfromFilterValue(pContent, pDataType); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String transfromCellValue(Object pContent, Object pDataType) {
        if (pDataType == MdkStatesMap.class) {
            return MenuCache.instance().getRequestStatesForInternalId((Long) pContent);
        }
        if (pDataType == ProcessTopicMap.class) {
            return MenuCache.instance().getProcessTopicForId((Long) pContent);
        }
        if (pDataType == ProcessResultMap.class) {
            return MenuCache.instance().getProcessResultForId((Long) pContent);
        }
        if (pDataType == MdkMap.class) {
            return String.valueOf(CpxMdkCatalog.instance().getById((Long) pContent));
        }
        return super.transfromCellValue(pContent, pDataType);
    }

    public CheckComboBox<Map.Entry<Long, String>> getMdkStatesComboBox(String pDataKey, String pLangKey) {
        List<Map.Entry<Long, String>> list = new ArrayList<>(MenuCache.instance().getRequestStatesEntries());
        final Collator collator = Collator.getInstance(Locale.GERMAN);
        Collections.sort(list, (o1, o2) -> {
            return collator.compare(o1.getValue(), o2.getValue());
            //return o1.getValue().compareToIgnoreCase(o2.getValue());
        });
        return new MapCheckedComboBox(pDataKey, pLangKey, list);
    }

    public CheckComboBox<Map.Entry<Long, String>> getMdkComboBox(String pDataKey, String pLangKey) {
        List<Map.Entry<Long, String>> list = new ArrayList<>();
        final Iterator<Map.Entry<Long, CpxMdk>> it = CpxMdkCatalog.instance().getAll(AbstractCpxCatalog.DEFAULT_COUNTRY).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, CpxMdk> entry = it.next();
            list.add(new Map.Entry<Long, String>() {
                @Override
                public Long getKey() {
                    return entry.getKey();
                }

                @Override
                public String getValue() {
                    return String.valueOf(entry.getValue());
                }

                @Override
                public String setValue(String value) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String toString() {
                    return String.valueOf(getKey());
                }

            });
        }
        final Collator collator = Collator.getInstance(Locale.GERMAN);
        Collections.sort(list, (o1, o2) -> {
            return collator.compare(o1.getValue(), o2.getValue());
            //return o1.getValue().compareToIgnoreCase(o2.getValue());
        });
        return new MapCheckedComboBox(pDataKey, pLangKey, list);
    }

    private CheckComboBox<Map.Entry<Long, String>> getProcessTopicComboBox(String pDataKey, String pLangKey) {
        return new MapCheckedComboBox(pDataKey, pLangKey, new ArrayList<>(MenuCache.instance().getProcessTopicsEntries()));
    }

    private CheckComboBox<Map.Entry<Long, String>> getProcessResultComboBox(String pDataKey, String pLangKey) {
        return new MapCheckedComboBox(pDataKey, pLangKey, new ArrayList<>(MenuCache.instance().getProcessResultEntries()));
    }

    private CheckComboBox<PlaceOfRegEn> getPlaceOfRegCombobox(String pDataKey, String pLangKey) {
        return new EnumCheckedComboBox<PlaceOfRegEn>(pDataKey, pLangKey, PlaceOfRegEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<PlaceOfRegEn> pItems) {
                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<PlaceOfRegEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public PlaceOfRegEn getEnum(String pStr) {
                return PlaceOfRegEn.findByName(pStr);
            }
        };
    }

    private CheckComboBox<QuarterEn> getQuarterCombobox(String pDataKey, String pLangKey) {
        return new EnumCheckedComboBox<QuarterEn>(pDataKey, pLangKey, QuarterEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<QuarterEn> pItems) {
                return pItems.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<QuarterEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public QuarterEn getEnum(String pStr) {
                return QuarterEn.findById(pStr);
            }
        };
    }

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

}

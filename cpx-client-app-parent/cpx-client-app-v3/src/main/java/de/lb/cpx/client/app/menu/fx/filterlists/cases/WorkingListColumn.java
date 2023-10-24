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
package de.lb.cpx.client.app.menu.fx.filterlists.cases;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.fx.filterlists.SearchListColumn;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.fx.label.TooltipLabel;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.enums.InsShortMap;
import de.lb.cpx.shared.filter.enums.InsuranceMap;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListFormatString;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * WorkignList Column to implement specific working list column behaviour like
 * rendering cell content and filter content
 *
 * @author wilde
 */
public class WorkingListColumn extends SearchListColumn<WorkingListItemDTO, Object> {

    private static final Logger LOG = Logger.getLogger(WorkingListColumn.class.getName());

    public WorkingListColumn(SearchListAttribute pAttribute) {
        //super(pAttribute.getLanguageKey(), pAttribute.getKey(), pAttribute.getDataType());
        super(pAttribute);
        setDisableFilter(pAttribute.isNoFilter());
        setPrefWidth(pAttribute.getSize());
    }

    @Override
    protected List<Node> transformFilterNodes(Object pDataType, final SearchListAttribute.OPERATOR pOperator) {
        List<Node> nodes = new ArrayList<>();
        if (pDataType == GenderEn.class) {
            nodes.add(getGenderComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (WorkingListAttributes.lock.equals(getSearchAttribute().getKey())) {
            nodes.add(getLockToogleGroup(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (WorkingListAttributes.isCancel.equals(getSearchAttribute().getKey())) {
            nodes.add(getCancelToogleGroup(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == AdmissionReasonEn.class) {
            nodes.add(getAdmissionReasonComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == AdmissionReason2En.class) {
            nodes.add(getAdmissionReason2ComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == DischargeReasonEn.class) {
            nodes.add(getDischargeReasonComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if (pDataType == GrouperMdcOrSkEn.class) {
            nodes.add(getMdcComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
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
//        if (pDataType == InsuranceShortMap.class) {
//            nodes.add(getInsuranceShortComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
//            return nodes;
//        }
//        if (pDataType ==DepartmentShortNameMap.class) {
//            nodes.add(getTextField(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
//            return nodes;
//        }
        return super.transformFilterNodes(pDataType, pOperator);
    }

    @Override
    protected Node transfromCellNode(String pKey, Object pContent, Object pDataType) {
        //sets lock on all booleans 
        //TODO:FIX ME
        if (pDataType == Boolean.class) {
            if (WorkingListAttributes.lock.equals(pKey)) {
                if ((boolean) pContent) {
                    return new Label("", ResourceLoader.getGlyph(FontAwesome.Glyph.LOCK));
                } else {
                    return null;
                }
            }
        }
        if (pDataType == Boolean.class) {
            if (WorkingListAttributes.isCancel.equals(pKey)) {
                if ((boolean) pContent) {
                    return new Label("", ResourceLoader.getGlyph(FontAwesome.Glyph.BAN));
                } else {
                    return null;
                }
            }
        }
        if (pDataType == GenderEn.class) {
            return new TooltipLabel(transfromCellValue(pContent, pDataType)) {
                @Override
                public String fetchTooltipText() {
                    final String value = pContent == null ? null : String.valueOf(pContent).trim();
                    if (value == null || value.isEmpty()) {
                        return null;
                    }
                    try {
                        final GenderEn item = GenderEn.valueOf(value);
                        return item.getTranslation().getValue();
                    } catch (IllegalArgumentException ex) {
                        LOG.log(Level.SEVERE, "cannot find enum of type GenderEn for this value: {0} (maybe invalid enum value is stored in database?)", value);
                        LOG.log(Level.FINEST, "Invalid enum value for GenderEn: " + value, ex);
                        return null;
                    }
                    //return GenderEn.valueOf((String) pContent).getTranslation().getValue();
                }

            };
//            nodes.add(getGenderComboBox(searchAttribute.getKey(),Lang.get(searchAttribute.getLanguageKey()).getValue()));
//            return nodes;
        }
        if (pDataType == AdmissionReasonEn.class) {
            return new TooltipLabel(transfromCellValue(pContent, pDataType)) {
                @Override
                public String fetchTooltipText() {
                    final AdmissionReasonEn item = AdmissionReasonEn.findById((String) pContent);
                    return item == null ? null : item.getTranslation().getValue();
                    //return AdmissionReasonEn.findById((String) pContent).getTranslation().getValue();
                }

            };
//            nodes.add(getAdmissionReasonComboBox(searchAttribute.getKey(), Lang.get(searchAttribute.getLanguageKey()).getValue()));
//            return nodes;
        }
        if (pDataType == AdmissionReason2En.class) {
            return new TooltipLabel(transfromCellValue(pContent, pDataType)) {
                @Override
                public String fetchTooltipText() {
                    final AdmissionReason2En item = AdmissionReason2En.findById((String) pContent);
                    return item == null ? null : item.getTranslation().getValue();
                    //return AdmissionReason2En.findById((String) pContent).getTranslation().getValue();
                }

            };
        }
        if (pDataType == GrouperMdcOrSkEn.class) {
            return new TooltipLabel(transfromCellValue(pContent, pDataType)) {
                @Override
                public String fetchTooltipText() {
                    final String value = pContent == null ? null : String.valueOf(pContent).trim();
                    if (value == null || value.isEmpty()) {
                        return null;
                    }
                    try {
                        final GrouperMdcOrSkEn item = GrouperMdcOrSkEn.valueOf(value);
                        return item.getTranslation().getValue();
                    } catch (IllegalArgumentException ex) {
                        LOG.log(Level.SEVERE, "cannot find enum of type GrouperMdcOrSkEn for this value: {0} (maybe invalid enum value is stored in database?)", value);
                        LOG.log(Level.FINEST, "Invalid enum value for GrouperMdcOrSkEn: " + value, ex);
                        return null;
                    }
                    //return GrouperMdcOrSkEn.valueOf(String.valueOf(pContent)).getTranslation().getValue();
                }

            };
//            nodes.add(getAdmissionReason2ComboBox(searchAttribute.getKey(), Lang.get(searchAttribute.getLanguageKey()).getValue()));
//            return nodes;
        }
        if (pDataType == DischargeReasonEn.class) {
            return new TooltipLabel(transfromCellValue(pContent, pDataType)) {
                @Override
                public String fetchTooltipText() {
                    final DischargeReasonEn item = DischargeReasonEn.findById((String) pContent);
                    return item == null ? null : item.getTranslation().getValue();
                    //return DischargeReasonEn.findById((String) pContent).getTranslation().getValue();
                }

            };
//            nodes.add(getDischargeReasonComboBox(searchAttribute.getKey(), Lang.get(searchAttribute.getLanguageKey()).getValue()));
//            return nodes;
        }

        return super.transfromCellNode(pKey, pContent, pDataType);
    }

    @Override
    protected String transfromCellValue(Object pContent, Object pDataType) {
        if (pDataType == CaseStatusEn.class) {
            final String value = pContent == null ? null : String.valueOf(pContent).trim();
            if (value == null || value.isEmpty()) {
                return null;
            }
            try {
                final CaseStatusEn item = CaseStatusEn.valueOf(value);
                return item.getTranslation().getValue();
            } catch (IllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "cannot find enum of type CaseStatusEn for this value: {0} (maybe invalid enum value is stored in database?)", value);
                LOG.log(Level.FINEST, "Invalid enum value for CaseStatusEn: " + value, ex);
                return null;
            }
            //return CaseStatusEn.valueOf(String.valueOf(pContent)).getTranslation().getValue();
        }
        if (pDataType == GrouperMdcOrSkEn.class) {
            final String value = pContent == null ? null : String.valueOf(pContent).trim();
            if (value == null || value.isEmpty()) {
                return null;
            }
            try {
                final GrouperMdcOrSkEn item = GrouperMdcOrSkEn.valueOf(value);
                return item.getId();
            } catch (IllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "cannot find enum of type GrouperMdcOrSkEn for this value: {0} (maybe invalid enum value is stored in database?)", value);
                LOG.log(Level.FINEST, "Invalid enum value for GrouperMdcOrSkEn: " + value, ex);
                return null;
            }
            //return GrouperMdcOrSkEn.valueOf(String.valueOf(pContent)).getId();
        }
        if (pDataType == String.class) {
            if (getSearchAttribute().getFormat() instanceof SearchListFormatString) {
                if (((SearchListFormatString) getSearchAttribute().getFormat()).getPattern() != null) {
                    return ((SearchListFormatString) getSearchAttribute().getFormat()).format((String) pContent);
                }
            }
        }
//        if (pDataType == DepartmentShortNameMap.class) {
//            if (getSearchAttribute().getFormat() instanceof SearchListFormatString) {
//                if (((SearchListFormatString) getSearchAttribute().getFormat()).getPattern() != null) {
//                    return ((SearchListFormatString) getSearchAttribute().getFormat()).format((String) pContent);
//                }
//            }
//        }
        return super.transfromCellValue(pContent, pDataType);
    }

    @Override
    protected String transfromFilterValue(String pContent, Object pDataType) {
        if (pDataType == CaseStatusEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> CaseStatusEn.valueOf(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        if (pDataType == GrouperMdcOrSkEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> GrouperMdcOrSkEn.valueOf(item).getId()).collect(Collectors.joining(","));
        }
        if (pDataType == Boolean.class) {
            if (WorkingListAttributes.lock.equals(getSearchAttribute().getKey())) {
                return lockConverter.toString(pContent);
            }
            if (WorkingListAttributes.isCancel.equals(getSearchAttribute().getKey())) {
                return cancelConverter.toString(pContent);
            }
        }
        if (pDataType == InsuranceMap.class) {
            Arrays.stream(pContent.split(",")).map(item -> CpxInsuranceCompanyCatalog.instance().findInsNameByInsuranceNumber(String.valueOf(item), AbstractCpxCatalog.DEFAULT_COUNTRY)).collect(Collectors.joining(","));
        }
//        if (pDataType == InsShortEn.class) {
//            Arrays.stream(pContent.split(",")).map(item -> InsShortEn.valueOf(item).getId()).collect(Collectors.joining(","));
//        }
//        if (pDataType == DepartmentShortNameMap.class) {
//            Arrays.stream(pContent.split(",")).map(item -> CpxDepartmentCatalog.instance().findDepNameByDepschort(String.valueOf(item), AbstractCpxCatalog.DEFAULT_COUNTRY)).collect(Collectors.joining(","));
//        }
        return super.transfromFilterValue(pContent, pDataType);
    }

    private BooleanToggleGroup getLockToogleGroup(String pDataKey, String pLangKey) {
        BooleanToggleGroup lockGroup = new BooleanToggleGroup(pDataKey, pLangKey) {
        };
        lockGroup.setTrueText(Lang.getFilterCasesLock());
        lockGroup.setFalseText(Lang.getFilterCasesUnlock());
        lockGroup.setAllText(Lang.getFilterNo());
        lockGroup.setResultConverter(lockConverter);
        return lockGroup;
    }

    private BooleanToggleGroup getCancelToogleGroup(String pDataKey, String pLangKey) {
        BooleanToggleGroup StornoGroup = new BooleanToggleGroup(pDataKey, pLangKey) {
        };
        StornoGroup.setTrueText(Lang.getFilterCasesCancel());
        StornoGroup.setFalseText(Lang.getFilterCasesCancelNot());
        StornoGroup.setAllText(Lang.getFilterNo());
        StornoGroup.setResultConverter(cancelConverter);
        return StornoGroup;
    }

    private CheckComboBox<AdmissionReasonEn> getAdmissionReasonComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<AdmissionReasonEn> box = new EnumCheckedComboBox<AdmissionReasonEn>(pDataKey, pLangKey, AdmissionReasonEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<AdmissionReasonEn> pItems) {
                return pItems.stream().map(item -> item.getId()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<AdmissionReasonEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public AdmissionReasonEn getEnum(String pStr) {
                return AdmissionReasonEn.findById(pStr);
            }
        };
        return box;
    }

    private CheckComboBox<AdmissionReason2En> getAdmissionReason2ComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<AdmissionReason2En> box = new EnumCheckedComboBox<AdmissionReason2En>(pDataKey, pLangKey, AdmissionReason2En.values()) {
            @Override
            public String getCheckedItems(ObservableList<AdmissionReason2En> pItems) {
                return pItems.stream().map(item -> item.getId()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<AdmissionReason2En> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public AdmissionReason2En getEnum(String pStr) {
                return AdmissionReason2En.findById(pStr);
            }
        };
        return box;
    }

    private CheckComboBox<DischargeReasonEn> getDischargeReasonComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<DischargeReasonEn> box = new EnumCheckedComboBox<DischargeReasonEn>(pDataKey, pLangKey, DischargeReasonEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<DischargeReasonEn> pItems) {
                return pItems.stream().map(item -> item.getId()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<DischargeReasonEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public DischargeReasonEn getEnum(String pStr) {
                return DischargeReasonEn.findById(pStr);
            }
        };
        return box;
    }

    private CheckComboBox<GenderEn> getGenderComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<GenderEn> box = new EnumCheckedComboBox<GenderEn>(pDataKey, pLangKey, GenderEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<GenderEn> pItems) {
                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<GenderEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public GenderEn getEnum(String pStr) {
                return GenderEn.valueOf(pStr);
            }
        };
        return box;
    }

    private CheckComboBox<GrouperMdcOrSkEn> getMdcComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<GrouperMdcOrSkEn> box = new EnumCheckedComboBox<GrouperMdcOrSkEn>(pDataKey, pLangKey, GrouperMdcOrSkEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<GrouperMdcOrSkEn> pItems) {
                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<GrouperMdcOrSkEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public GrouperMdcOrSkEn getEnum(String pStr) {
                return GrouperMdcOrSkEn.valueOf(pStr);
            }
        };
        return box;
    }

    private CheckComboBox<Map.Entry<String, String>> getInsuranceComboBox(String pDataKey, String pLangKey) {
        return new MapCheckedComboBoxString(pDataKey, pLangKey, new ArrayList<>(MenuCache.instance().getInsuranceEntries()));
    }

    private CheckComboBox<Map.Entry<String, String>> getInsShortComboBox(String pDataKey, String pLangKey) {
        final LinkedHashMap<String, String> m = new LinkedHashMap<>();
        for (String shortName : MenuCache.getMenuCacheInsShort().getShortNames()) {
            m.put(shortName, shortName);
        }
        return new MapCheckedComboBoxString(pDataKey, pLangKey, new ArrayList<>(m.entrySet()));
    }

}

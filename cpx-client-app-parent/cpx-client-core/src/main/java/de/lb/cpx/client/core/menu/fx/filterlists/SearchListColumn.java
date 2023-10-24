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
package de.lb.cpx.client.core.menu.fx.filterlists;

import de.lb.cpx.client.core.menu.fx.filterlists.layout.DateChooser;
import de.lb.cpx.client.core.menu.fx.filterlists.layout.DoubleChooser;
import de.lb.cpx.client.core.menu.fx.filterlists.layout.IntegerChooser;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.catalog.CpxHospitalCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.filter.FilterBasicItem;
import de.lb.cpx.client.core.model.fx.label.TooltipLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.shared.filter.enums.InsShortMap;
import de.lb.cpx.shared.filter.enums.InsuranceMap;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.util.Callback;

/**
 * Basic FilterColumn Implementation of the filtercolumn for SearchLists should
 * unify basic behaviour for searchlists without changing filtercolumn stuff for
 * other filterlists
 *
 * @author wilde
 * @param <S> item type of the tableview
 * @param <T> type
 */
public class SearchListColumn<S, T> extends FilterColumn<S, T> {

    private final SearchListAttribute searchAttribute;
    private static final Logger LOG = Logger.getLogger(SearchListColumn.class.getName());

    public SearchListColumn(SearchListAttribute pAttribute) {
        super(pAttribute.getKey(), pAttribute.getLanguageKey(), pAttribute.getDataType(), pAttribute.getOperator(), pAttribute.getActionAllowed());
//        super(pAttribute.getKey(), pAttribute.getLanguageKey(), pAttribute.getDataType(), pAttribute.getOperator());
        searchAttribute = pAttribute;
    }

    protected final SearchListAttribute getSearchAttribute() {
        return searchAttribute;
    }

    @Override
    protected Node transfromCellNode(String pKey, Object pContent, Object pDataType) {
        //for catalog data fetch values for tooltip if required
        //to shorten loading time tooltip text is set if tooltip should show
        //if (pDataType == SearchListFormatInsurance.class) {
        if (searchAttribute.isInsurance()) {
            Label lbl = new TooltipLabel(String.valueOf(pContent)) {
                @Override
                public String fetchTooltipText() {
                    return CpxInsuranceCompanyCatalog.instance().findInsNameByInsuranceNumber(String.valueOf(pContent), AbstractCpxCatalog.DEFAULT_COUNTRY);
                }

            };
            return lbl;
        }
        if (pDataType == InsuranceMap.class) {
            Label lbl = new TooltipLabel(CpxInsuranceCompanyCatalog.instance().findInsNameByInsuranceNumber(String.valueOf(pContent), AbstractCpxCatalog.DEFAULT_COUNTRY)) {

            };
            return lbl;
        }
        if (pDataType == InsShortMap.class) {
            Label lbl = new TooltipLabel(CpxInsuranceCompanyCatalog.instance().findInsShortNameByInsuranceNumber(String.valueOf(pContent), AbstractCpxCatalog.DEFAULT_COUNTRY)) {

            };
            return lbl;
        }
        //if (pDataType == SearchListFormatHospital.class) {
        if (searchAttribute.isHospital()) {
            Label lbl = new TooltipLabel(String.valueOf(pContent)) {
                @Override
                public String fetchTooltipText() {
                    return CpxHospitalCatalog.instance().findHosNameByHosIdent(String.valueOf(pContent), AbstractCpxCatalog.DEFAULT_COUNTRY);
                }

            };
            return lbl;
        }
        //if (pDataType == SearchListFormatDepartment.class) {
        if (searchAttribute.isDepartment()) {
            Label lbl = new TooltipLabel(String.valueOf(pContent)) {
                @Override
                public String fetchTooltipText() {
                    return CpxDepartmentCatalog.instance().findDepNameByDepKey301(String.valueOf(pContent), AbstractCpxCatalog.DEFAULT_COUNTRY);
                }
            };
            return lbl;
        }
        if (searchAttribute.isDepartmentName()) {
            Label lbl = new TooltipLabel(CpxDepartmentCatalog.instance().findDepNameByDepKey301(String.valueOf(pContent), AbstractCpxCatalog.DEFAULT_COUNTRY)) {
                @Override
                public String fetchTooltipText() {
                    return String.valueOf(pContent);
                }
            };
            return lbl;
        }
        //CPX-1206
        if (searchAttribute.isDate()) {
            Label lbl = new TooltipLabel(Lang.toDate((Date) pContent)) {
                @Override
                public String fetchTooltipText() {
                    return Lang.toTime((Date) pContent);

                }

            };
            return lbl;
        }
        if(pDataType == Double.class && searchAttribute.is4DecimalDigits()){
            return new Label(Lang.toDecimal((Double)pContent, 4));
        }
        if (pDataType == AdmissionCauseEn.class) {
            return new TooltipLabel(transfromCellValue(pContent, pDataType)) {
                @Override
                public String fetchTooltipText() {
                    final String value = pContent == null ? null : String.valueOf(pContent).trim();
                    if (value == null || value.isEmpty()) {
                        return null;
                    }
                    try {
                        final AdmissionCauseEn item = AdmissionCauseEn.valueOf(value);
                        return item.getTranslation().getValue();
                    } catch (IllegalArgumentException ex) {
                        LOG.log(Level.SEVERE, "cannot find enum of type AdmissionCauseEn for this value: " + value + " (maybe invalid enum value is stored in database?)");
                        LOG.log(Level.FINEST, "Invalid enum value for AdmissionCauseEn: " + value, ex);
                        return null;
                    }
                    //return AdmissionCauseEn.valueOf(String.valueOf(pContent)).getTranslation().getValue();
                }

            };
        }
        //default value of datatype could not be processed
        return new Label(transfromCellValue(pContent, pDataType));
    }

    @Override
    protected List<Node> transformFilterNodes(Object pDataType, final SearchListAttribute.OPERATOR pOperator) {
        List<Node> nodes = new ArrayList<>();
        //doubled in Rule and maybe(?) workflow list 
        //due to children handle
        if (pDataType == String.class) {
            if (getSearchAttribute().getChildren().isEmpty()) {
                nodes.add(getTextField(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            } else {
                for (SearchListAttribute child : getSearchAttribute().getChildren()) {
                    nodes.add(getLabeledTextField(child.getKey(), Lang.get(child.getLanguageKey()).getValue(), Lang.get(child.getLanguageKey()).getValue()));
                }
            }
            return nodes;
        }
        if (pDataType == Date.class) {
            if (searchAttribute.getChildren().isEmpty()) {
                nodes.add(getDatePicker(searchAttribute.getKey(), Lang.get(searchAttribute.getLanguageKey()).getValue()));
            } else {
                DateChooser chooser = new DateChooser(searchAttribute);
                chooser.setControlFactory(new Callback<SearchListAttribute, Control>() {
                    @Override
                    public Control call(SearchListAttribute param) {
                        if (param.getDataType() == Date.class) {
                            if (param.isExpires() || param.isOpen() || param.isToday()) {
                                return getFilterLabel(param.getKey(), param.getLanguageKey());
                            } else {
                                LabeledDatePicker picker = getLabeledDatePicker(param.getKey(), Lang.get(param.getLanguageKey()).getValue(), Lang.get(param.getLanguageKey()).getValue()); //new LabeledDatePicker(Lang.get(param.getLanguageKey()).getValue());
                                return picker;
                            }
                        }
                        return new Label("Unbekannter Datentyp: " + param.getDataType().getClass().getSimpleName());
                    }
                });
                chooser.setSingleFilterDelete(new Callback<String, Void>() {
                    @Override
                    public Void call(String param) {
                        if (getOnFilterDelete() != null) {
                            if (filterItemMap.containsKey(param)) {
                                LOG.info("clear param: " + param + " of value: " + filterItemMap.get(param).getValue());
                                filterItemMap.get(param).setValue("");
                            }
                            getOnFilterDelete().call(param);
                        }
                        return null;
                    }
                });
                chooser.setFilterCallback(new Callback<SearchListAttribute, Void>() {
                    @Override
                    public Void call(SearchListAttribute param) {
                        //counted as no fitler selected
                        if (param == null) {
                            enableFilter = true;
                            return null;
                        }
                        if (param.isExpires() || param.isOpen() || param.isToday()) {
                            FilterBasicItem filterItem = getFilterItem(param.getKey(), param.getLanguageKey());
                            filterItem.setValue(Lang.toIsoDate(new Date()));
                            enableFilter = true;
//                        }else{
//                            FilterBasicItem filterItem = getFilterItem(param.getKey(), param.getLanguageKey());
//                            filterItem.setValue("");
                        }
//                        if (param.isExpires() || param.isOpen() || param.isToday()) {
//                            filter(param.getKey(), param.getLanguageKey(), Lang.toIsoDate(new Date()));
//                        } else {
//                            filter(param.getKey(), param.getLanguageKey(), "");
//                        }
                        return null;
                    }
                });
                nodes.add(chooser);
            }
            return nodes;
        }
        if (pDataType == Integer.class) {
            if (searchAttribute.getChildren().isEmpty()) {
                nodes.add(getNumberTextField(searchAttribute.getKey(), searchAttribute.getOperator(), Lang.get(searchAttribute.getLanguageKey()).getValue()));
            } else {
                IntegerChooser chooser = new IntegerChooser(searchAttribute);
                chooser.setControlFactory(new Callback<SearchListAttribute, Control>() {
                    @Override
                    public Control call(SearchListAttribute param) {
                        if (param.getDataType() == Integer.class) {
                            if (param.getOperator() == SearchListAttribute.OPERATOR.EQUAL) {
                                return getLabeledTextField_Number(param.getKey(), Lang.get(param.getLanguageKey()).getValue(), Lang.get(param.getLanguageKey()).getValue(), param.getOperator());
                            } else {
                                return getLabeledNumberField(param.getKey(), Lang.get(param.getLanguageKey()).getValue(), Lang.get(param.getLanguageKey()).getValue(), param.getOperator());//new LabeledDatePicker(Lang.get(param.getLanguageKey()).getValue());
                            }
                        }
                        return new Label("Unbekannter Datentyp: " + param.getDataType().getClass().getSimpleName());
                    }
                });
//                chooser.setSingleFilterDelete(new Callback<String, Void>() {
//                    @Override
//                    public Void call(String param) {
//                        if (getOnFilterDelete() != null) {
//                            getOnFilterDelete().call(param);
//                        }
//                        return null;
//                    }
//                });
//                chooser.setFilterCallback(new Callback<SearchListAttribute, Void>() {
//                    @Override
//                    public Void call(SearchListAttribute param) {
//                        filter(param.getKey(), param.getLanguageKey(), "");
//                        return null;
//                    }
//                });
                chooser.setSingleFilterDelete(new Callback<String, Void>() {
                    @Override
                    public Void call(String param) {
                        if (getOnFilterDelete() != null) {
                            if (filterItemMap.containsKey(param)) {
                                LOG.info("clear param: " + param + " of value: " + filterItemMap.get(param).getValue());
                                filterItemMap.get(param).setValue("");
                            }
                            getOnFilterDelete().call(param);
                        }
                        return null;
                    }
                });
                chooser.setFilterCallback(new Callback<SearchListAttribute, Void>() {
                    @Override
                    public Void call(SearchListAttribute param) {
                        //counted as no fitler selected
                        if (param == null) {
                            enableFilter = true;
                            return null;
                        }
//                        if(param.isExpires() || param.isOpen() || param.isToday()){
                        FilterBasicItem filterItem = getFilterItem(param.getKey(), param.getLanguageKey());
                        filterItem.setValue("");
                        enableFilter = true;
//                        }
                        return null;
                    }
                });

                nodes.add(chooser);
            }
            return nodes;
        }
        if (pDataType == Double.class) {
            if (searchAttribute.getChildren().isEmpty()) {
                nodes.add(getDoubleTextField(searchAttribute.getKey(), Lang.get(searchAttribute.getLanguageKey()).getValue()));
            } else {
//                for(SearchListAttribute child : searchAttribute.getChildren()){
//                    nodes.add(getLabeledNumberField(child.getKey(),Lang.get(child.getLanguageKey()).getValue(),Lang.get(child.getLanguageKey()).getValue()));
//                }
                DoubleChooser chooser = new DoubleChooser(searchAttribute);
                chooser.setControlFactory(new Callback<SearchListAttribute, Control>() {
                    @Override
                    public Control call(SearchListAttribute param) {
                        if (param.getDataType() == Double.class) {
                            return getLabeledDoubleField(param.getKey(), Lang.get(param.getLanguageKey()).getValue(), Lang.get(param.getLanguageKey()).getValue());//new LabeledDatePicker(Lang.get(param.getLanguageKey()).getValue());
                        }
                        return new Label("Unbekannter Datentyp: " + param.getDataType().getClass().getSimpleName());
                    }
                });
//                chooser.setSingleFilterDelete(new Callback<String, Void>() {
//                    @Override
//                    public Void call(String param) {
//                        if (getOnFilterDelete() != null) {
//                            getOnFilterDelete().call(param);
//                        }
//                        return null;
//                    }
//                });
//                chooser.setFilterCallback(new Callback<SearchListAttribute, Void>() {
//                    @Override
//                    public Void call(SearchListAttribute param) {
//                        filter(param.getKey(), param.getLanguageKey(), "");
//                        return null;
//                    }
//                });
                chooser.setSingleFilterDelete(new Callback<String, Void>() {
                    @Override
                    public Void call(String param) {
                        if (getOnFilterDelete() != null) {
                            if (filterItemMap.containsKey(param)) {
                                LOG.info("clear param: " + param + " of value: " + filterItemMap.get(param).getValue());
                                filterItemMap.get(param).setValue("");
                            }
                            getOnFilterDelete().call(param);
                        }
                        return null;
                    }
                });
                chooser.setFilterCallback(new Callback<SearchListAttribute, Void>() {
                    @Override
                    public Void call(SearchListAttribute param) {
                        //counted as no fitler selected
                        if (param == null) {
                            enableFilter = true;
                            return null;
                        }
//                        if(param.isExpires() || param.isOpen() || param.isToday()){
                        FilterBasicItem filterItem = getFilterItem(param.getKey(), param.getLanguageKey());
                        filterItem.setValue("");
                        enableFilter = true;
//                        }
                        return null;
                    }
                });
                nodes.add(chooser);
            }
            return nodes;
        }
        return super.transformFilterNodes(pDataType, pOperator);
    }

//    protected CheckComboBox<InsShortEn> getInsuranceShortComboBox(String pDataKey, String pLangKey) {
//        EnumCheckedComboBox<InsShortEn> box = new EnumCheckedComboBox<InsShortEn>(pDataKey, pLangKey, InsShortEn.values()) {
//            @Override
//            public String getCheckedItems(ObservableList<InsShortEn> pItems) {
//                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
//            }
//
//            @Override
//            public String getLocalizedCheckedItems(ObservableList<InsShortEn> pItems) {
//                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
//            }
//
//            @Override
//            public InsShortEn getEnum(String pStr) {
//                return InsShortEn.valueOf(pStr);
//            }
//        };
//        return box;
//    }

}

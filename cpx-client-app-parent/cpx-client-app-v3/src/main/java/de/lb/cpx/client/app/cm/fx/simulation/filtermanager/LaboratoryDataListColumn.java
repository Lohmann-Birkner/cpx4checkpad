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
/**
 * FXML Controller class creates and manage the Laboratory data in the case
 * management / case simulation scene
 *
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.filtermanager;

import de.lb.cpx.client.core.menu.fx.filterlists.SearchListColumn;
import de.lb.cpx.model.TLab;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.lang.Lang;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author nandola
 */
public class LaboratoryDataListColumn extends SearchListColumn<TLab, Object> {

//    private final SearchListAttribute searchAttribute;
//    private final LaboratoryDataListAttributes LaboratoryDataListAttributes;
    private static final Logger LOG = Logger.getLogger(LaboratoryDataListColumn.class.getName());

    public LaboratoryDataListColumn(SearchListAttribute pAttribute) {
        super(pAttribute);//pAttribute.getKey(), pAttribute.getLanguageKey(), pAttribute.getDataType(), pAttribute.getOperator());
//        searchAttribute = pAttribute;
    }

//    protected final SearchListAttribute getSearchAttribute() {
//        return searchAttribute;
//    }
//    @Override
//    protected Node transfromCellNode(String pKey, Object pContent, Object pDataType) {
//        if (searchAttribute.isDate()) {
//            Label lbl = new TooltipLabel(Lang.toDate((Date) pContent)) {
//                @Override
//                public String fetchTooltipText() {
//                    return Lang.toTime((Date) pContent);
//                }
//            };
//            return lbl;
//        }
//        //default value of datatype could not be processed
//        return new Label(transfromCellValue(pContent, pDataType));
//    }
    @Override
    protected String transfromCellValue(Object pContent, Object pDataType) {
        //try to avoid null-Strings in listview replace with empty string
        if (pContent == null) {
            return "";
        }
        if (pDataType == String.class) {
            if (pContent.equals("null")) {
                LOG.info("detected string value: null on " + dataKey);
                return "";
            }
            return String.valueOf(pContent);
        }
        if (pDataType == Date.class) {
            return Lang.toDate((Date) pContent);
        }
        if (pDataType == Double.class) {
            return Lang.toDecimal((Double) pContent);
        }
        if (pDataType == Long.class) {
            return String.valueOf(pContent);
        }
        if (pDataType == BigDecimal.class) {
            return ((BigDecimal) pContent).setScale(((BigDecimal) pDataType).scale(), RoundingMode.HALF_UP).toString();
        }
        if (pDataType == Integer.class) {
            return String.valueOf(pContent);
        }

        return String.valueOf(pContent);

//        return super.transfromCellValue(pContent, pDataType);
    }

//    @Override
//    protected List<Node> transformFilterNodes(Object pDataType, final SearchListAttribute.OPERATOR pOperator) {
//        List<Node> nodes = new ArrayList<>();
//
//        if (pDataType == String.class) {
//            nodes.add(getTextField(dataKey, translation.getValue()));
//            return nodes;
//        }
//        if (pDataType == Date.class) {
//            nodes.add(getLabeledDatePicker(dataKey, translation.getValue(), translation.getValue()));
//            return nodes;
//        }
//        if (pDataType == Date.class) {
//            if (searchAttribute.getChildren().isEmpty()) {
//                nodes.add(getDatePicker(searchAttribute.getKey(), Lang.get(searchAttribute.getLanguageKey()).getValue()));
//            } else {
//                DateChooser chooser = new DateChooser(searchAttribute);
//                chooser.setControlFactory(new Callback<SearchListAttribute, Control>() {
//                    @Override
//                    public Control call(SearchListAttribute param) {
//                        if (param.getDataType() == Date.class) {
//                            if (param.isExpires() || param.isOpen() || param.isToday()) {
//                                return getFilterLabel(param.getKey(), param.getLanguageKey());
//                            } else {
////                                return getLabeledDatePicker(param.getKey(), Lang.get(param.getLanguageKey()).getValue(), Lang.get(param.getLanguageKey()).getValue());
//                                return getLabeledDatePicker(dataKey, Lang.get(param.getLanguageKey()).getValue(), Lang.get(param.getLanguageKey()).getValue());
////                                return getLabeledDatePicker(searchAttribute.getKey(), Lang.get(param.getLanguageKey()).getValue(), Lang.get(param.getLanguageKey()).getValue());
//                            }
//                        }
//                        return new Label("Unbekannter Datentyp: " + param.getDataType().getClass().getSimpleName());
//                    }
//                });
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
//                        if (param.isExpires() || param.isOpen() || param.isToday()) {
//                            filter(param.getKey(), param.getLanguageKey(), Lang.toIsoDate(new Date()));
//                        } else {
//                            filter(param.getKey(), param.getLanguageKey(), "");
//                        }
//                        return null;
//                    }
//                });
//                nodes.add(chooser);
//            }
//            return nodes;
//        }
//        if (pDataType == Double.class) {
//            nodes.add(getDoubleTextField(dataKey, translation.getValue()));
//            return nodes;
//        }
//        if (pDataType == Integer.class) {
//            nodes.add(getNumberTextField(dataKey, pOperator, translation.getValue()));
//            return nodes;
//        }
//        if (pDataType == Long.class) {
//            nodes.add(getNumberTextField(dataKey, pOperator, translation.getValue()));
//            return nodes;
//        }
//
//        return super.transformFilterNodes(pDataType, pOperator);
//    }
    @Override
    protected String transfromFilterValue(String pContent, Object pDataType) {
        return super.transfromFilterValue(pContent, pDataType);
    }

//        @Override
//    protected String transfromFilterValue(String pContent, Object pDataType) {
//        //cast from iso to localized
//        if (pDataType == Date.class) {
//            return Lang.toDate(dateConverter.fromString(pContent));
//        }
//        return pContent;
//    }
}

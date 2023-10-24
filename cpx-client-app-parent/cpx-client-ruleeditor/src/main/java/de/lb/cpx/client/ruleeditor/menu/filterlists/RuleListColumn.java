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
 *    2019  hasse - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.menu.filterlists;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxCheckComboBox;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import de.lb.cpx.shared.filter.enums.CrgRuleTypesMap;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListRuleAttributes;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author hasse
 */
public class RuleListColumn extends FilterColumn<CrgRules, Object> {

    private static final Logger LOG = Logger.getLogger(RuleListColumn.class.getName());

    private final SearchListAttribute searchAttribute;
    private PoolTypeEn poolTypeEn = PoolTypeEn.DEV;
    private final EjbConnector ejbConnector = Session.instance().getEjbConnector();
    private final EjbProxy<RuleEditorBeanRemote> connectRuleEditorBean = ejbConnector.connectRuleEditorBean();

    public RuleListColumn(SearchListAttribute pAttribute, PoolTypeEn pPoolType) {
        this(pAttribute);
        poolTypeEn = pPoolType;
        setCellFactory(new Callback<TableColumn<CrgRules, Object>, TableCell<CrgRules, Object>>() {
            @Override
            public TableCell<CrgRules, Object> call(TableColumn<CrgRules, Object> param) {
                return new FilterCell(){
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item == null || empty){
                            if(getGraphic()!=null){
                                getGraphic().pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), false);
                            }
                            return;
                        }
                        if(getTableRow().getItem()!=null){
                            getGraphic().pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), getTableRow().getItem().getCrgrMessage()!=null);
//                            if(getTableRow().getItem().getCrgrRid().equals("449810")){
//                                List<RuleMessage> msg = new RuleMessageReader().read(getTableRow().getItem());
//                                LOG.log(Level.INFO, "update rule is msg null? {0}", msg==null);
//                            }
                        }
                        if(SearchListRuleAttributes.ruleMessage.equals(pAttribute.getKey())){
                            ((Label)getGraphic()).prefWidthProperty().bind(widthProperty());
                            setStyle("-fx-padding:0 7 0 0;");
                        }
                    }
                    
                };
            }
        });

    }

    public RuleListColumn(SearchListAttribute pAttribute) {
        super(pAttribute.getKey(), pAttribute.getLanguageKey(), pAttribute.getDataType(), pAttribute.getOperator());
        searchAttribute = pAttribute;
    }

    protected final SearchListAttribute getSearchAttribute() {
        return searchAttribute;
    }

    @Override
    protected Node transfromCellNode(String pKey, Object pContent, Object pDataType) {
        if(SearchListRuleAttributes.ruleMessage.equals(pKey)){
            RuleMessageIndicator indicator = new RuleMessageIndicator();
            indicator.setAlignment(Pos.CENTER_RIGHT);
            indicator.setTooltip(new CpxTooltip("Diese Regel hat eine oder mehrere ungültige Einträge!", 100, 5000, 100, true));
            Label lbl = new Label();
            lbl.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            lbl.setGraphic(indicator);
            return lbl;
        }
        return super.transfromCellNode(pKey, pContent, pDataType); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected String transfromCellValue(Object pContent, Object pDataType) {
        //try to avoid null-Strings in listview replace with empty string
        if (pContent == null) {
            return "";
        }
//        if(pContent instanceof CrgRuleTypes){
//            CrgRuleTypes value = (CrgRuleTypes)pContent;
//            return value.getCrgtShortText();
//        }
        if (pDataType == CrgRuleTypesMap.class) {
            //TODO:
//            return MenuCache.instance().getProcessResultForId((Long) pContent);
            CrgRuleTypes value = (CrgRuleTypes) pContent;
            return value.getCrgtShortText();

        }

        if (pDataType == String.class) {
            if (pContent.equals("null")) {
                //LOG.info("detected string value: null on " + dataKey);
                return "";
            }
            return String.valueOf(pContent);
        }

        if (pDataType == RuleTypeEn.class) {
            //return RuleTypeEn.findById((Integer) pContent).getTranslation().getAbbreviation();
            RuleTypeEn value = (RuleTypeEn) pContent;
            value.getId();
            return RuleTypeEn.findById(value.getId()).getTranslation().toString();

        }

        return CaseRuleManager.getDisplayText(String.valueOf(pContent));//"Can not process datatype " + pDataType;
    }

    @Override
    protected List<Node> transformFilterNodes(Object pDataType, SearchListAttribute.OPERATOR pOperator) {
        List<Node> nodes = new ArrayList<>();
        if (SearchListRuleAttributes.ruleMessage.equals(dataKey)){
            nodes.add(getRuleMessageToggleGroup(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == String.class) {
            nodes.add(getTextField(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == RuleTypeEn.class) {
            //nodes.add(getTextField(dataKey, translation.getValue()));
            nodes.add(getTypeComboBox(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == CrgRuleTypesMap.class) {
            nodes.add(getCrgRuleTypesCombobox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;

        }

        return super.transformFilterNodes(pDataType, pOperator); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String transfromFilterValue(String pContent, Object pDataType) {
        if (SearchListRuleAttributes.ruleMessage.equals(dataKey)){
            return "1".equals(pContent)?"Alle Regeln mit Fehler":"Alle Regeln ohne Fehler";
        }
        if (pDataType == RuleTypeEn.class) {
            //nodes.add(getTextField(dataKey, translation.getValue()));
            return Arrays.stream(pContent.split(",")).map(item -> RuleTypeEn.findById(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        if (pDataType == CrgRuleTypesMap.class) {
            Map<Long, String> map = getRuleTypesMap();
            String[] strIds = pContent.split(",");
            List<String> ret = new ArrayList<>();
            for (String str : strIds) {
                ret.add(map.get(Long.parseLong(str)));
            }
            return ret.stream().collect(Collectors.joining(","));

        }
        return super.transfromFilterValue(pContent, pDataType);
    }

    private CpxCheckComboBox<RuleTypeEn> getTypeComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<RuleTypeEn> box = new EnumCheckedComboBox<RuleTypeEn>(pDataKey, pLangKey, RuleTypeEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<RuleTypeEn> pItems) {
                return pItems.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<RuleTypeEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public RuleTypeEn getEnum(String pStr) {
                return RuleTypeEn.findById(Integer.parseInt(pStr));
            }
        };
        box.setPrefWidth(150);
        box.setMaxWidth(150);
        return box;
    }

    private Node getCrgRuleTypesCombobox(String pDataKey, String pLangKey) {

        MapCheckedComboBox cb = new MapCheckedComboBox(pDataKey, pLangKey, new ArrayList<>(getRuleTypesMap().entrySet())); //MenuCache.instance().getProcessTopicsEntries()
        cb.setPrefWidth(150);
        cb.setMaxWidth(150);
        return cb;
    }

    private Map<Long, String> getRuleTypesMap() {

        Map<Long, String> ret = new HashMap<>();
        List<CrgRuleTypes> ruleTypes = connectRuleEditorBean.get().findAllRuleTypes(poolTypeEn);
        if (ruleTypes != null) {
            for (CrgRuleTypes type : ruleTypes) {
                ret.put(type.getId(), type.getCrgtShortText());
            }
        }
        return ret;
    }

    private Node getRuleMessageToggleGroup(String pDataKey, String pLangKey) {
        BooleanToggleGroup lockGroup = new BooleanToggleGroup(pDataKey, pLangKey) {
        };
        lockGroup.setTrueText("Alle Regeln mit Fehler");
        lockGroup.setFalseText("Alle Regeln ohne Fehler");
        lockGroup.setAllText(Lang.getFilterNo());
        return lockGroup;
    }
    
}

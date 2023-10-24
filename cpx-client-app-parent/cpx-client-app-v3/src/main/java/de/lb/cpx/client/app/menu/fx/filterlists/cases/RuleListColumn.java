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

import de.lb.cpx.client.core.menu.fx.filterlists.SearchListColumn;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.BooleanEn;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.shared.dto.RuleListItemDTO;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * Column implementation for the rule list implements specific filters and
 * behavior
 *
 * @author wilde
 */
public class RuleListColumn extends SearchListColumn<WorkingListItemDTO, Object> {

    private static final Logger LOG = Logger.getLogger(RuleListColumn.class.getName());
    
    

    public RuleListColumn(SearchListAttribute pAttribute) {
        super(pAttribute);
        setDisableFilter(pAttribute.isNoFilter());
        //searchAttribute = pAttribute;
        setPrefWidth(pAttribute.getSize());
        //TODO:FIX ME, comparator should be set in attribute.format??
        if (pAttribute.isClientSide()) {
            Comparator<Object> comp = createComparator(pAttribute);
            if (comp != null) {
                setComparator(comp);
            }
        }
    }

    @Override
    protected Node transfromCellNode(String pKey, Object pContent, Object pDataType) {
        if (pDataType == RuleTypeEn.class) {
            Label icon = new Label();
            icon.setGraphic(getRuleTypeIcon(String.valueOf(pContent)));
            return icon;
        }
        if(pDataType == BooleanEn.class){
            Label icon = new Label();
            if(pContent != null){
                if((Boolean)pContent){
                    Glyph  g = ResourceLoader.getGlyph(FontAwesome.Glyph.CHECK_SQUARE);
                        //g.setStyle("-fx-text-fill: #ffff00;");
                    g.setStyle("-fx-text-fill: #f6a117;");
                    icon.setGraphic(g);
                }
            
            }
            return icon;

        }
        return super.transfromCellNode(pKey, pContent, pDataType);
    }

    @Override
    protected List<Node> transformFilterNodes(Object pDataType, final SearchListAttribute.OPERATOR pOperator) {
        List<Node> nodes = new ArrayList<>();
        if (pDataType == RuleTypeEn.class) {
            nodes.add(getRuleTypeComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        if(pDataType == BooleanEn.class){
            nodes.add(getBooleanTypeComboBox(getSearchAttribute().getKey(), Lang.get(getSearchAttribute().getLanguageKey()).getValue()));
            return nodes;
        }
        return super.transformFilterNodes(pDataType, pOperator);
    }

    @Override
    protected String transfromFilterValue(String pContent, Object pDataType) {
        if (pDataType == CaseStatusEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> CaseStatusEn.valueOf(item).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        return super.transfromFilterValue(pContent, pDataType); //To change body of generated methods, choose Tools | Templates.
    }

    //TODO:FIX ME
    //helper methode to create comperators for client side, should be removed?
    private Comparator<Object> createComparator(SearchListAttribute pAttribute) {

        switch (pAttribute.key) {
            case "ruleDescription":
                return Comparator.comparing((t) -> {
                    return ((RuleListItemDTO) t).getRuleDescription();
                }, nullsLast(naturalOrder()));
            case "ruleSuggestion":
                return Comparator.comparing((t) -> {
                    return ((RuleListItemDTO) t).getRuleSuggestion();
                }, nullsLast(naturalOrder()));
            case "typeText":
                return Comparator.comparing((t) -> {
                    return ((RuleListItemDTO) t).getTypeText();
                }, nullsLast(naturalOrder()));
            case "xRuleNumber":
                return Comparator.comparing((t) -> {
                    return ((RuleListItemDTO) t).getXRuleNumber();
                }, nullsLast(naturalOrder()));
            case "crgrCaption":
                return Comparator.comparing((t) -> {
                    return ((RuleListItemDTO) t).getCrgrCaption();
                }, nullsLast(naturalOrder()));
            default:
                LOG.log(Level.WARNING, "Unknown attribute key for rule list column: " + pAttribute.key);
        }
        return null;
    }

    private CheckComboBox<RuleTypeEn> getRuleTypeComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<RuleTypeEn> box = new EnumCheckedComboBox<RuleTypeEn>(pDataKey, pLangKey, RuleTypeEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<RuleTypeEn> pItems) {
                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<RuleTypeEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public RuleTypeEn getEnum(String pStr) {
                return RuleTypeEn.valueOf(pStr);
            }

        };
        return box;
    }

    
    private Node getBooleanTypeComboBox(String pDataKey, String pLangKey) {
       EnumCheckedComboBox<BooleanEn> box = new EnumCheckedComboBox<BooleanEn>(pDataKey, pLangKey, BooleanEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<BooleanEn> pItems) {
                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<BooleanEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public BooleanEn getEnum(String  b) {
                return BooleanEn.valueOf(b);
            }

        };
        return box;
    
    }    
    
    private Node getRuleTypeIcon(String pRuleType) {
        Label type = new Label();
        String tooltip = null;
        Glyph g = null;
        switch (pRuleType) {
// STATE_NO(0,Lang.GENDER_UNDEFINED), STATE_WARNING(1, Lang.WARNING), STATE_ERROR(2, Lang.ERROR), STATE_SUGG(3, Lang.CASE_STATUS_SUGGESTION);
            case "suggestion":
                tooltip = Lang.get(RuleTypeEn.STATE_SUGG.getLangKey()).value;
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION_CIRCLE);
                //g.setStyle("-fx-text-fill: #0099ff;");
                g.setStyle("-fx-text-fill: #1569a6;");
                break;
            case "warning":
                tooltip = Lang.get(RuleTypeEn.STATE_WARNING.getLangKey()).value;
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_TRIANGLE);
                //g.setStyle("-fx-text-fill: #ffff00;");
                g.setStyle("-fx-text-fill: #f6a117;");
                break;
            case "error":
                tooltip = Lang.get(RuleTypeEn.STATE_ERROR.getLangKey()).value;
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_CIRCLE);
                //g.setStyle("-fx-text-fill: #ff1a1a;");
                g.setStyle("-fx-text-fill: #ca2424;");
                break;
            default:
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION);
                break;
        }
        type.setTooltip(new CpxTooltip(tooltip));
        type.setGraphic(g);
        return type;
//        return ResourceLoader.getGlyph(FontAwesome.Glyph.FLASH);
    }

}

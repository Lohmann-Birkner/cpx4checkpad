/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tableview.column.rules;

import de.lb.cpx.client.core.model.fx.tableview.column.IntegerColumn;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * Column to show rule types of rule dto
 *
 * @author wilde
 */
public class RuleTypeColumn extends NodeColumn<CpxSimpleRuleDTO> {

    /**
     * creates new instance with default header
     */
    public RuleTypeColumn() {
        super(Lang.getCaseResolveKind());
        setMinWidth(40.0);
        setMaxWidth(40.0);
        setResizable(false);
//        widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                LOG.info("width " + newValue.doubleValue());
//            }
//        });
        setCellFactory(new Callback<TableColumn<CpxSimpleRuleDTO, Node>, TableCell<CpxSimpleRuleDTO, Node>>() {
            @Override
            public TableCell<CpxSimpleRuleDTO, Node> call(TableColumn<CpxSimpleRuleDTO, Node> param) {
                return new TableCell<CpxSimpleRuleDTO, Node>() {
                    @Override
                    protected void updateItem(Node item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setText("");
                            setGraphic(null);
                            return;
                        }
                        setGraphic(item);
//                        ErrorLevel level = ErrorLevel.getBySeverity(item);
//                        RuleTypeEn level = RuleTypeEn.findById(item);
//                        setText(level.getTranslation().getValue());
                    }

                };
            }
        });
    }

    @Override
    public Node extractValue(CpxSimpleRuleDTO pValue) {
        Label type = new Label();
        Glyph g = null;
        String tooltip = null;
        RuleTypeEn rType = pValue.getRuleTyp();
        switch(rType){
            case STATE_SUGG:
                tooltip = Lang.get(RuleTypeEn.STATE_SUGG.getLangKey()).value;
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION_CIRCLE);
                //g.setStyle("-fx-text-fill: #0099ff;");
                g.setStyle("-fx-text-fill: #1569a6;");
                break;

            case STATE_WARNING:
                tooltip = Lang.get(RuleTypeEn.STATE_WARNING.getLangKey()).value;
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_TRIANGLE);
                //g.setStyle("-fx-text-fill: #ffff00;");
                g.setStyle("-fx-text-fill: #f6a117;");
                break;
            case STATE_ERROR:
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
    }
//
//    @Override
//    protected String getDisplayText(CpxSimpleRuleDTO pValue) {
//        return super.getDisplayText(pValue); //To change body of generated methods, choose Tools | Templates.
//    }

}

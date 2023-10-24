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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.ruleviewer.layout.RuleTablesViewSkin1.CrgRuleTablesItem;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import java.util.Objects;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class RuleTableListCell extends ListCell<CrgRuleTablesItem> {

    private final Label lbl;
    private final Label iconGrahpic;
    private final HBox box;
    private final RuleMessageIndicator indicator;

    public RuleTableListCell() {
        super();
        lbl = new Label();
        box = new HBox();
        indicator = new RuleMessageIndicator();
        HBox.setHgrow(lbl, Priority.ALWAYS);
        box.setAlignment(Pos.CENTER_LEFT);
//                        lbl.setMaxWidth(Double.MAX_VALUE);
        iconGrahpic = new Label();
        iconGrahpic.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.EYE));
        HBox icon = new HBox(iconGrahpic);
//                        icon.getStyleClass().add("list-view-icon");
        box.getChildren().addAll(icon, lbl);
        box.setSpacing(5.0);
    }

    @Override
    protected void updateItem(CrgRuleTablesItem item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if (item == null || empty) {
            setGraphic(null);
            return;
        }
        if (item.getTable() == null) {
            setText("");
            return;
        }
        lbl.setText(item.isDirty() ? ("*" + item.getTable().getCrgtTableName()) : item.getTable().getCrgtTableName());
        setGraphic(box);
        boolean sameId = Objects.equals(item.getTable().getCreationUser(), RuleView.getFacade().getCurrentUser());
//        LOG.info("check creation user and current user for " + item.getTable().getCrgtTableName() + 
//                "\ncurrentUser" + RuleView.getFacade().getCurrentUser() + 
//                "\ncreation user " + item.getTable().getCreationUser() + 
//                "\nsame? " + sameId
//        );
        if(item.getTable().getCrgtMessage()!=null){
            if(!box.getChildren().contains(indicator)){
                indicator.setTooltip(new CpxTooltip(new RuleTableMessageReader().getRuleTableMessageTooltip(item.getTable()), 100, 5000, 100, true));
                box.getChildren().add(1,indicator);
            }
        }else{
            if(box.getChildren().contains(indicator)){
                box.getChildren().remove(indicator);
            }
        }
        lbl.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), item.getTable().getCrgtMessage()!=null);
        iconGrahpic.setVisible(!sameId);
    }

}

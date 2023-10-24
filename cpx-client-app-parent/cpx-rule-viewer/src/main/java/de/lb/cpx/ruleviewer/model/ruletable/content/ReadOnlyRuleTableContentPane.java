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
package de.lb.cpx.ruleviewer.model.ruletable.content;

import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.util.code.CodeExtraction;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

/**
 *
 * @author wilde
 */
public class ReadOnlyRuleTableContentPane extends AsyncPane<FlowPane> {

    private static final Logger LOG = Logger.getLogger(ReadOnlyRuleTableContentPane.class.getName());

    public ReadOnlyRuleTableContentPane(String pTableName) {
        super();
        setTableName(pTableName);
    }
    private StringProperty tableNameProperty;

    public StringProperty tableNameProperty() {
        if (tableNameProperty == null) {
            tableNameProperty = new SimpleStringProperty();
        }
        return tableNameProperty;
    }

    public String getTableName() {
        return tableNameProperty().get();
    }

    public void setTableName(String pTableName) {
        tableNameProperty().set(pTableName);
    }

    @Override
    public FlowPane loadContent() {
        long start = System.currentTimeMillis();
        FlowPane box = new FlowPane();//VBox();
        box.setVgap(5);
        box.setHgap(5);
        CrgRuleTables table = RuleMetaDataCache.instance().getTableForNameInPool(RuleView.getFacade().getPool(), getTableName());//RuleView.getFacade().findRuleTable(hlSecondCondition.getText());
        LOG.info("get Table in " + (System.currentTimeMillis() - start));
        List<Node> lblList = createContentNodes(table);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                box.getChildren().addAll(lblList);
            }
        });
        LOG.info("create Box in " + (System.currentTimeMillis() - start));
        return box;
    }

    private List<Node> createContentNodes(CrgRuleTables table) {
        ArrayList<Node> nodes = new ArrayList<>();
        if (table == null) {
            return nodes;
        }
        if (table.getCrgtContent() == null) {
            String content = RuleView.getFacade().getRuleTableContent(table);
            if (content == null) {
                return nodes;
            }
            table.setCrgtContent(content);
        }
       if (table.getCrgtComment() == null) {
            String comment = RuleView.getFacade().getRuleTableComment(table); 
            if (comment == null) {
                comment = "";
            }
            table.setCrgtComment(comment);
        }
        for (String str : table.getCrgtContent().split(",")) {
            str = str.replace(" ", "");
            str = str.replace("%", "*");
            Text lbl = new Text(str);
            lbl.setOnMouseEntered(new EventHandler<MouseEvent>() {
                private Tooltip tip;

                @Override
                public void handle(MouseEvent event) {
                    if (tip == null) {
                        String txt = "";
                        lbl.getText();
                        if (checkIcd(lbl.getText())) {
                            txt = CpxIcdCatalog.instance().getByCode(lbl.getText(), "de", Lang.toYear(RuleView.getFacade().getRule().getCrgrValidFrom())).getDescription();
                        }
                        if (checkOps(lbl.getText())) {
                            txt = CpxOpsCatalog.instance().getByCode(lbl.getText(), "de", Lang.toYear(RuleView.getFacade().getRule().getCrgrValidFrom())).getDescription();
                        }
                        tip = new BasicTooltip(lbl.getText(), txt);
//                                                tip.getScene().getStylesheets().add("/styles/cpx-default.css");
                        Tooltip.install(lbl, tip);
                        tip.setOnShowing(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                if (!tip.getScene().getStylesheets().contains("/styles/cpx-default.css")) {
                                    tip.getScene().getStylesheets().add(0, "/styles/cpx-default.css");
                                }
                            }
                        });
                    }
                }
            });
            nodes.add(lbl);
        }
        return nodes;
    }

    public boolean checkOps(String pCode) {
        return !CodeExtraction.getOpsCode(pCode).isEmpty();
    }

    public boolean checkIcd(String pCode) {
        return !CodeExtraction.getIcdCode(pCode).isEmpty();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.model.ruletable.dialog;

import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.control.InfoView;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.model.ruletable.content.ReadOnlyRuleTableContentPane;
import de.lb.cpx.shared.lang.Lang;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.stage.Window;

/**
 *
 * @author gerschmann
 */
public class TableInfoDialog extends TitledDialog {

    private StringProperty tableNameProperty;

    public TableInfoDialog(String tableName, Window pOwner) {
        super(tableName, pOwner);
        setTableName(tableName);
        setContent(createContent());
    }

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

    private Node createContent() {
        InfoView info = new InfoView();
        info.setComment(getTableComment());
        info.setHeaderComment(Lang.getRuleTableNote() + ":");
        info.setHeaderContent(Lang.getRuleTableContent() + ":");
        info.setInfoPane(new ReadOnlyRuleTableContentPane(getTableName()));

        return info;

    }

    private String getTableComment() {

        return RuleMetaDataCache.instance().getTableCommentForNameInPool(RuleView.getFacade().getPool(), getTableName());

    }
}

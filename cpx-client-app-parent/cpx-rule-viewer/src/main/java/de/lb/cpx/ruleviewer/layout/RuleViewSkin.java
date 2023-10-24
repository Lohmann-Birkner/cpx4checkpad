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
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.model.Element;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Skin class of the rule view
 *
 * @author wilde
 */
public class RuleViewSkin extends BasicEditorViewSkin<RuleView> {

    /**
     * construct new instance
     *
     * @param pSkinnable control class
     */
    public RuleViewSkin(final RuleView pSkinnable) {
        super(pSkinnable);
        pSkinnable.ruleProperty().addListener(new ChangeListener<Rule>() {
            @Override
            public void changed(ObservableValue<? extends Rule> observable, Rule oldValue, Rule newValue) {
                updateUi();
            }
        });
        pSkinnable.viewModeProperty().addListener(new ChangeListener<ViewMode>() {
            @Override
            public void changed(ObservableValue<? extends ViewMode> observable, ViewMode oldValue, ViewMode newValue) {
                updateUi();
            }
        });
        if (ViewMode.READ_WRITE.equals(getSkinnable().getViewMode())) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    getSkinnable().selectFirst();
                }
            });
        }
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (BasicEditorView.REFRESH_VALUE.equals(change.getKey())) {
                        LOG.finer("REFRESH ROOT");
                        getSkinnable().getControls().get(0).refresh();
                        pSkinnable.getProperties().remove(BasicEditorView.REFRESH_VALUE);
                    }
                    if (BasicEditorView.REFRESH_RULE_MESSAGE.equals(change.getKey())) {
                        LOG.finer("REFRESH RuleMessage");
                        getSkinnable().getRoot().setRuleMessage(getSkinnable().getRuleMessages());
                        pSkinnable.getProperties().remove(BasicEditorView.REFRESH_RULE_MESSAGE);
                    }
                }
            }
        });

    }
    @Override
    public void updateUi() { 
        LOG.finer("UPDATE RULE VIEW");
        //creates new layout for the stored xml rule
        //creates all new selectable controls to display rule content
        long start = System.currentTimeMillis();
        Rule xmlRule = getSkinnable().getRule();
        if (xmlRule == null) {
            LOG.warning("Rule Value is null! Should not happen! Did another error occured?");
            return;
        }
        getChildren().clear();
        LOG.log(Level.FINER, "perpare ruleview in {0} ms", System.currentTimeMillis() - start);
        Element root = new Element();
        root.setDeleteCallback(new Callback<Void, Boolean>() {
            @Override
            public Boolean call(Void param) {
                root.getControls().clear();
                return true;
            }
        });
        root.fontProperty().bind(getSkinnable().fontProperty());
        getSkinnable().selectedNodeProperty().bindBidirectional(root.selectedNodeProperty());
        root.setViewMode(getSkinnable().getViewMode());
        LOG.log(Level.FINER, "bindings in {0} ms", System.currentTimeMillis() - start);
        root.setRulesElement(0, xmlRule.getRulesElement());
        LOG.log(Level.FINER, "set rule in {0} ms", System.currentTimeMillis() - start);
        //add container to add margin to root element to show correct highlights
        VBox container = new VBox(root);
        VBox.setMargin(root, new Insets(3));
        container.setFillWidth(true);
        container.setMaxHeight(Double.MAX_VALUE);
        LOG.log(Level.FINER, "set containers in {0} ms", System.currentTimeMillis() - start);
        //add container in scrollpane, rules could be occupy much space
        ScrollPane pane = new ScrollPane(container);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        //add scrollpane in layout
        getChildren().add(pane);
        //add root to controls
        getSkinnable().getControls().add(root);
        getSkinnable().setMaxWidth(Double.MAX_VALUE);
        root.setRuleMessage(getSkinnable().getRuleMessages());
        LOG.log(Level.FINER, "update RuleView in {0} ms", System.currentTimeMillis() - start);
    }
    private static final Logger LOG = Logger.getLogger(RuleViewSkin.class.getName());

}

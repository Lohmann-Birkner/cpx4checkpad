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
 *    2018  Your Organisation - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.text.Font;

/**
 * create basic view to reflect basic funtions
 *
 * @author wilde
 */
public class BasicEditorView extends Control {

    public static final String REFRESH_VALUE = "refresh";
    public static final String REFRESH_RULE_MESSAGE = "refresh_rule_message";
    //view mode of the editor view 
    private ObjectProperty<ViewMode> viewModeProperty;
    //xml rule property
    private ObjectProperty<Rule> ruleProperty;
    //selected node
    private ObjectProperty<Node> selectedNodeProperty;
    //control list
    private ObservableList<SelectableControl> controlList = FXCollections.observableList(new ArrayList<>());
    //font property
    private ObjectProperty<Font> fontProperty;

    public BasicEditorView() {
        super();
        getStylesheets().add(getClass().getResource("/styles/rule_visualizer.css").toExternalForm());
    }

    /**
     * @return view mode property of hte editor, specifies if view is in
     * editmode or not
     */
    public ObjectProperty<ViewMode> viewModeProperty() {
        if (viewModeProperty == null) {
            viewModeProperty = new SimpleObjectProperty<>(ViewMode.READ_ONLY);
        }
        return viewModeProperty;
    }

    /**
     * @return view mode
     */
    public ViewMode getViewMode() {
        return viewModeProperty().get();
    }

    /**
     * @param pMode set new view mode
     */
    public void setViewMode(ViewMode pMode) {
        viewModeProperty().set(pMode);
    }

    /**
     * @return indicator if view is readonly
     */
    public boolean isReadOnly() {
        return ViewMode.READ_ONLY.equals(getViewMode());
    }

    /**
     * @return property to store xml rule
     */
    public ObjectProperty<Rule> ruleProperty() {
        if (ruleProperty == null) {
            ruleProperty = new SimpleObjectProperty<>();
        }
        return ruleProperty;
    }

    /**
     * @param pRule set new xml rule
     */
    public void setRule(Rule pRule) {
        ruleProperty().set(pRule);
    }

    /**
     * @return get currently displayed xml rule
     */
    public Rule getRule() {
        return ruleProperty().get();
    }

    /**
     * refresh/redraw editor components currently displayed
     */
    public void refresh() {
        getProperties().put(REFRESH_VALUE, null);
    }
    
    public void refreshRuleMessage(){
        getProperties().put(REFRESH_RULE_MESSAGE, null);
    }
    /**
     * @return selected node property
     */
    public ObjectProperty<Node> selectedNodeProperty() {
        if (selectedNodeProperty == null) {
            selectedNodeProperty = new SimpleObjectProperty<>();
        }
        return selectedNodeProperty;
    }

    /**
     * @return currently selected object
     */
    public Node getSelectedNode() {
        return selectedNodeProperty().get();
    }

    /**
     * @param pNode select node
     */
    public void setSelectedNode(Node pNode) {
        selectedNodeProperty().set(pNode);
    }

    /**
     * @return list of all displayed controls
     */
    public ObservableList<SelectableControl> getControls() {
        return controlList;
    }

    /**
     * @param pList set new control list, do not know if it works
     */
    public void setControls(ObservableList<SelectableControl> pList) {
        controlList = pList;
    }

    /**
     * @return property that stored currently used font
     */
    public ObjectProperty<Font> fontProperty() {
        if (fontProperty == null) {
            fontProperty = new SimpleObjectProperty<>(Font.getDefault());
        }
        return fontProperty;
    }

    /**
     * @param pFont new font to use
     */
    public void setFont(Font pFont) {
        fontProperty().set(pFont);
    }

    /**
     * @return currently used font
     */
    public Font getFont() {
        return fontProperty().get();
    }

    /**
     * select first control in root
     */
    public void selectFirst() {
        if (getControls().isEmpty()) {
            return;
        }
        SelectableControl root = getControls().get(0);
        if (root.getControls().isEmpty()) {
            return;
        }
        setSelectedNode(root.getControls().get(0));
    }

}

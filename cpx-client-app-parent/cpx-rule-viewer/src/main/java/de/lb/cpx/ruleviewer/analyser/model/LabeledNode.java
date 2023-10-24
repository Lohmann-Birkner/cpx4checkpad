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
package de.lb.cpx.ruleviewer.analyser.model;

import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserBeanProperty;
import de.lb.cpx.ruleviewer.util.AnalyserFormater;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class LabeledNode extends VBox {

    private final Label title;
//    protected Callback<PropertySheet.Item, PropertyEditor<?>> editorFactory = new DefaultPropertyEditorFactory();
    private final HBox menu;

    public LabeledNode(String pTitle, AnalyserBeanProperty pBeanProp) {
        this(pTitle, pBeanProp, new DefaultPropertyEditorFactory());
    }

    public LabeledNode(String pTitle, AnalyserBeanProperty pBeanProp, Callback<PropertySheet.Item, PropertyEditor<?>> pEditorFactory) {
        super();
//        getStyleClass().add("attribute-item");
        Node node = pEditorFactory.call(pBeanProp).getEditor();
        node.getStyleClass().add("attribute-item");
        setSpacing(5.0);
        setFillWidth(true);
        title = new Label(pTitle + " (" + (pBeanProp.getValue() != null ?/*String.valueOf(pValue)*/ AnalyserFormater.formatText(pBeanProp) : "-") + ")");
        title.getStyleClass().add("attribute-item");
        setMinWidth(250);
        setMaxWidth(250);

        menu = new HBox(5.0);
        menu.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(menu, Priority.ALWAYS);
        HBox header = new HBox(5.0, title, menu);
        header.setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(header, node);
    }

    public String getTitle() {
        return title.getText();
    }

    public void addToMenu(Node pNode) {
        menu.getChildren().add(pNode);
    }
//        public LabeledNode(String pTitle,BeanProperty pBeanProperty){
//            this(pTitle,pBeanProperty,editorFactory.call(pBeanProperty).getEditor());
//        }

}

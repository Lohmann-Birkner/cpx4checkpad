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

import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

/**
 *
 * @author wilde
 */
public class StringContentViewSkin extends SkinBase<StringContentView> {

    private static final Logger LOG = Logger.getLogger(StringContentViewSkin.class.getName());

    public StringContentViewSkin(StringContentView pSkinnable) {
        super(pSkinnable);

        pSkinnable.contentModeProperty().addListener(new ChangeListener<StringContentView.ContentMode>() {
            @Override
            public void changed(ObservableValue<? extends StringContentView.ContentMode> observable, StringContentView.ContentMode oldValue, StringContentView.ContentMode newValue) {
                setContent(newValue);
            }
        });
        setContent(pSkinnable.getContentMode());
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if(change.wasAdded()){
                    if(StringContentView.REFRESH_VIEW.equals(change.getKey())){
                        if(getChildren().get(0) instanceof AsyncPane){
                            var pane = (AsyncPane) getChildren().get(0);
                            if(pane.getContent() instanceof RuleTableSearchPane){
                                ((RuleTableSearchPane)pane.getContent()).refreshSeverityAsync();
                            }
                        }
                        pSkinnable.getProperties().remove(StringContentView.REFRESH_VIEW);
                    } 
                }
            }
        });
    }

    private void setContent(StringContentView.ContentMode pMode) {
//        getChildren().clear();
        long start = System.currentTimeMillis();
        if (getChildren().isEmpty()) {
            getChildren().add(createContent(pMode));
        }
        getChildren().set(0, createContent(pMode));
        LOG.log(Level.FINER, "add content {0}", System.currentTimeMillis() - start);
    }

    private Node createContent(StringContentView.ContentMode pMode) {
        AsyncPane<Node> pane = new AsyncPane<>() {

            @Override
            public Node loadContent() {
                switch (pMode) {
                    case ITEM:
                        return addItemPane();
                    case TEXT:
                        return addTextPane();
                    default:
                        LOG.warning("Unknown ContentMode found, can not build UI! Mode was " + pMode.name());
                        return new HBox();
                }
            }
        };
        return pane;
    }

    private RuleTableSearchPane addItemPane() {
        long start = System.currentTimeMillis();
        RuleTableSearchPane flowPane = new RuleTableSearchPane(); //RuleTableContentPane();
        flowPane.setValidationCallback(getSkinnable().getCodeSuggestionCallback());
        flowPane.predicateProperty().bind(getSkinnable().predicateProperty());
        flowPane.validationMessageProperty().bind(getSkinnable().validationMessageProperty());
        flowPane.setOnContentChanged(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getSkinnable().setContent(flowPane.getContent());
            }
        });
        flowPane.setContent(getSkinnable().getContent(), getSkinnable().isEditable());
        LOG.finer("create item pane in " + (System.currentTimeMillis() - start));
        return flowPane;
    }

    private TextArea addTextPane() {
        TextArea area = new TextArea();
        area.editableProperty().bind(getSkinnable().editableProperty());
        area.setMaxHeight(Double.MAX_VALUE);
        area.setText(formatFromContent(getSkinnable().getContent()));
        area.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getSkinnable().setContent(formatToContent(newValue));
            }
        });

        return area;
    }

    private String formatToContent(String pText) {
        pText = pText.replace("\n", ",");
        pText = pText.replace("\r", ",");
        pText = pText.replace(" ", ",");
        pText = pText.replace("''", " ").replace("'", "");
        pText = pText.replace("*", "%");
        return pText;
    }

    private String formatFromContent(String pText) {
        pText = pText.replace(",", "\n");
        return pText;
    }
}

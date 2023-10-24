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

import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.ruleviewer.analyser.editors.CaseChangedEvent;
import de.lb.cpx.ruleviewer.util.AnalyserFormater;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.property.BeanProperty;

/**
 *
 * @author wilde
 */
public class RuleAnalyserItemSkin extends SkinBase<RuleAnalyserItem> {

    private VBox root;
    private Label lblText;
    private Button btnDelete;
    private ChangePopOver popover;
    private Label lblTitle;
    private FlowPane fpItems;
    private HBox boxHeader;
    private Button btnExpand;
    private Label lblDivider;

    public RuleAnalyserItemSkin(RuleAnalyserItem pSkinnable) throws IOException {
        super(pSkinnable);
        loadRoot();
        getChildren().add(root);
        lblText.textProperty().bind(pSkinnable.textProperty());
        lblTitle.textProperty().bind(pSkinnable.titleProperty());
        btnDelete.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pSkinnable.getOnDeleteCallback().call(null);
            }
        });
        pSkinnable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (pSkinnable.hasItems()) {
                        pSkinnable.setExpand(!pSkinnable.isExpand());
                    } else {
                        if (!isMenuVisible()) {
                            showMenu();
                        } else {
                            hideMenu();
                        }
                    }

                }
            }
        });
        if (pSkinnable.getBeanProperty() != null) {
            pSkinnable.getEditor().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
                @Override
                public void handle(CaseChangedEvent t) {
                    getSkinnable().setText(AnalyserFormater.formatText(getSkinnable().getBeanProperty()));
                }
            });
        }
        pSkinnable.beanProperty().addListener(new ChangeListener<BeanProperty>() {
            @Override
            public void changed(ObservableValue<? extends BeanProperty> observable, BeanProperty oldValue, BeanProperty newValue) {
                pSkinnable.getEditor().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
                    @Override
                    public void handle(CaseChangedEvent t) {
                        getSkinnable().setText(AnalyserFormater.formatText(getSkinnable().getBeanProperty()));
                    }
                });
            }
        });
        showExpandButton(pSkinnable.hasItems());
        pSkinnable.getItems().addListener(new ListChangeListener<RuleAnalyserItem>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends RuleAnalyserItem> c) {
                showExpandButton(!c.getList().isEmpty());
            }
        });
        showDivider(!pSkinnable.getText().isEmpty());
        pSkinnable.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                showDivider(newValue != null ? (!newValue.isEmpty()) : false);
            }
        });
        if (pSkinnable.hasItems()) {
            handleExpandedButtonIcon(pSkinnable.isExpand());
            showItemsPane(pSkinnable.isExpand());
        }
        pSkinnable.expandProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (pSkinnable.hasItems()) {
                    handleExpandedButtonIcon(newValue);
                    showItemsPane(newValue);
                }
                if (newValue) {
                    pSkinnable.setText("");
                } else {
                    pSkinnable.setText(pSkinnable.getItemsText());
                }
            }
        });
        pSkinnable.setText(pSkinnable.getItemsText());
    }

    private void handleExpandedButtonIcon(boolean pExpand) {
        if (pExpand) {
            btnExpand.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOUBLE_DOWN));
        } else {
            btnExpand.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOUBLE_RIGHT));
        }
    }

    private void showDivider(boolean pHide) {
        lblDivider.setVisible(pHide);
    }

    private void loadRoot() throws IOException {
        root = (VBox) FXMLLoader.load(getClass().getResource("/fxml/RuleAnalyserItem.fxml"));
        root.getStyleClass().add("rule-analyser-item-container");
        lblTitle = (Label) root.lookup("#lblItemTitle");
        lblDivider = (Label) root.lookup("#lblItemDivider");
        lblText = (Label) root.lookup("#lblItemText");
        OverrunHelper.addOverrunCallback(lblText, new Callback<Boolean, Void>() {
            @Override
            public Void call(Boolean param) {
                if (param) {
                    lblText.setTooltip(new Tooltip(lblText.getText()));
                } else {
                    lblText.setTooltip(null);
                }
                return null;
            }
        });
        btnDelete = (Button) root.lookup("#btnDelete");
        boxHeader = (HBox) root.lookup("#boxItemHeader");
        fpItems = (FlowPane) root.lookup("#fpItems");

        btnExpand = (Button) root.lookup("#btnItemExpand");
        btnExpand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getSkinnable().setExpand(!getSkinnable().isExpand());
            }
        });

        Bindings.bindContent(fpItems.getChildren(), getSkinnable().getItems());

        showItemsPane(false);
    }

    private void showMenu() {
        if (getSkinnable().getBeanProperty() == null) {
            return;
        }
        if (popover == null) {
            popover = new ChangePopOver();
        }
        popover.show(root);
    }

    private void hideMenu() {
        popover.hide();
    }

    private boolean isMenuVisible() {
        if (popover != null) {
            return popover.isShowing();
        }
        return false;
    }

    private void showItemsPane(boolean pShow) {
        if (pShow) {
            if (!root.getChildren().contains(fpItems)) {
                root.getChildren().add(fpItems);
            }
        } else {
            if (root.getChildren().contains(fpItems)) {
                root.getChildren().remove(fpItems);
            }
        }
    }

    private void showExpandButton(boolean pShow) {
        if (pShow) {
            if (!boxHeader.getChildren().contains(btnExpand)) {
                boxHeader.getChildren().add(0, btnExpand);
            }
        } else {
            if (boxHeader.getChildren().contains(btnExpand)) {
                boxHeader.getChildren().remove(btnExpand);
            }
        }
    }

    private class ChangePopOver extends AutoFitPopOver {

        public ChangePopOver() {
            super();
            if (getSkinnable().getBeanProperty() == null) {
                return;
            }
            if (getSkinnable().getEditor() != null) {
                setContentNode(getSkinnable().getEditor());
            } else {
                TextField field = new TextField(getSkinnable().getText());
                getSkinnable().textProperty().bind(field.textProperty());
                if (getSkinnable().getEditTextFormatter() != null) {
                    field.textFormatterProperty().bind(getSkinnable().editTextFormatterProperty());
                }
            }
        }

        @Override
        protected void show() {
            super.show(); //To change body of generated methods, choose Tools | Templates.
        }

    }

}

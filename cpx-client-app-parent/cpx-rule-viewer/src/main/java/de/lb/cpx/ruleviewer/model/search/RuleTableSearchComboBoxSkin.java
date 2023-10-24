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
package de.lb.cpx.ruleviewer.model.search;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBox;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBoxListViewSkin;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.ruleviewer.editor.buttontypes.RuleViewerButtonTypes;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.ruleviewer.event.RuleTableChangedEvent;
import de.lb.cpx.ruleviewer.layout.RuleTablesView;
import de.lb.cpx.ruleviewer.model.combobox.RuleTableComboBox;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * control to swarch for Rule Tables
 *
 * @author wilde
 */
public class RuleTableSearchComboBoxSkin extends BasicSearchComboBoxSkin<CrgRuleTables> {

    public RuleTableSearchComboBoxSkin(RuleTableSearchComboBox pSkinnable) {
        super(pSkinnable);
        getListView().scrollTo(getSkinnable().getSelectedItem());
    }

    @Override
    public ComboBox<CrgRuleTables> getComboBox() {
        if (cb == null) {
            CpxComboBox<CrgRuleTables> combo = new RuleTableComboBox();//CpxComboBox<>();
            combo.setSkin(new CpxComboBoxListViewSkin<>(combo));
            combo.setItems(getSkinnable().populateItems());
            combo.getSelectionModel().select(getSkinnable().getSelectedItem());
            return combo;//RuleTableComboBox(getSkinnable().populateItems());
        }
        return cb;
    }

    public final ListView<CrgRuleTables> getListView() {
        ListView<CrgRuleTables> lv = ((CpxComboBoxListViewSkin<CrgRuleTables>) getComboBox().getSkin()).getListView();
        return lv;
    }

    @Override
    public Button getMenuButton() {
        if (btn == null) {
            Button btnMenu = new Button();
            btnMenu.getStyleClass().add("cpx-icon-button");
            btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.WRENCH));
            btnMenu.setOnAction(new EventHandler<ActionEvent>() {
                private AutoFitPopOver popover;
                private RuleTablesView content;

                @Override
                public void handle(ActionEvent event) {
                    if (popover == null) {
                        content = new RuleTablesView();
                        content.setFocusTraversable(true);
                        content.criterionProperty().bind(getSkinnable().criterionProperty());
                        content.setOnSelectedCallback(new Callback<CrgRuleTables, Void>() {
                            @Override
                            public Void call(CrgRuleTables param) {
                                if (param != null) {
                                    getSkinnable().selectItem(param);
                                }
                                return null;
                            }
                        });
                        if(getSkinnable() instanceof RuleTableSearchComboBox){
                            content.setValidationCalllback(new Callback<CrgRuleTables, byte[]>() {
                                @Override
                                public byte[] call(CrgRuleTables param) {
                                    return ((RuleTableSearchComboBox)getSkinnable()).getValidationCalllback().call(param);
                                }
                            });
                            content.setCodeSuggestionCallback(new Callback<String, String>() {
                                @Override
                                public String call(String param) {
                                    return ((RuleTableSearchComboBox)getSkinnable()).getCodeSuggestionCallback().call(param);
                                }
                            });
//                            content.validationCalllbackProperty().bind(((RuleTableSearchComboBox)getSkinnable()).validationCalllbackProperty());
                        }
//                        Bindings.bindContentBidirectional(content.getItems(), getSkinnable().populateItems());
                        content.setPrefWidth(600);
                        content.setMaxHeight(450);
                        getAdapter().addChangeListener(content.selectedItemProperty(), new ChangeListener<CrgRuleTables>() {
                            @Override
                            public void changed(ObservableValue<? extends CrgRuleTables> observable, CrgRuleTables oldValue, CrgRuleTables newValue) {
                                if (getComboBox().getItems().contains(newValue)) {
                                    getComboBox().getSelectionModel().select(newValue);
                                }
                            }
                        });
//                        selectCbValue();
//                        initListeners();
                        popover = new AutoFitPopOver(content);
                        popover.setAutoHide(true);
                        popover.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                if (popover.isFocused()) {
                                    if (KeyCode.ESCAPE.equals(event.getCode())) {
                                        if (event.getTarget() instanceof Control) {
                                            //abord if editable field is hit .. ugly .. should be a static string somewhere
                                            if ("field-edit".equals(((Styleable) event.getTarget()).getId())) {
                                                return;
                                            }
                                        }
                                        popover.hide();
                                        event.consume();
                                    }
                                }
                            }
                        });
                        popover.setOnHidden(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent t) {
                                if (content.hasUnsaved()) {
                                    //TODO:ADD here information that after saving transfer stuff will be lost!
                                    AlertDialog alert = AlertDialog.createWarningDialog("Sie haben ungesicherte Inhalte in ihren Regeltabellen!",
                                            MainApp.getWindow(),
                                            RuleViewerButtonTypes.SAVE_ALL,
                                            RuleViewerButtonTypes.DISCARD,
                                            ButtonType.CANCEL);
                                    alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
                                        @Override
                                        public void accept(ButtonType t) {
                                            if (ButtonType.CANCEL.equals(t)) {
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        popover.show(getComboBox());
                                                    }
                                                });
                                                return;
                                            }
                                            if (RuleViewerButtonTypes.SAVE_ALL.equals(t)) {
                                                content.saveAllUnsaved();
                                            }
                                            if (RuleViewerButtonTypes.DISCARD.equals(t)) {
                                                content.discardAllChanges();
                                                return;
                                            }
                                        }
                                    });
                                }
                            }
                        });

                        content.addEventFilter(RuleChangedEvent.ANY, new EventHandler<RuleChangedEvent>() {
                            @Override
                            public void handle(RuleChangedEvent event) {
                                CrgRuleTables selected = getSkinnable().getSelectedItem();
                                selected.setCrgtTableName(content.getSelectedItem().getCrgtTableName());
                                selected.setCrgtContent(content.getSelectedItem().getCrgtContent());
//                                ((RuleTableComboBox)getComboBox()).refresh();
                                Event.fireEvent(getSkinnable(), event);
                            }
                        });
                        content.addEventFilter(RuleTableChangedEvent.ruleTableChangedEvent(), new EventHandler<RuleTableChangedEvent>() {
                            @Override
                            public void handle(RuleTableChangedEvent event) {
                                getComboBox().setItems(getSkinnable().populateItems());
//                                getComboBox().getSelectionModel().select(getSkinnable().getSelectedItem());
                                for (CrgRuleTables item : getComboBox().getItems()) {
                                    if (item.getId() == getSkinnable().getSelectedItem().getId()) {
                                        getComboBox().getSelectionModel().select(item);
                                    }
                                }
                                Event.fireEvent(getSkinnable(), event);
                            }
                        });
                        popover.setFitOrientation(Orientation.HORIZONTAL);
                        popover.setDetachable(false);
                        popover.setArrowLocation(popover.getAdjustedLocation(PopOver.ArrowLocation.RIGHT_CENTER));
                        popover.setHideOnEscape(true);
                        popover.setFadeOutDuration(Duration.ZERO);
                        popover.setOnHiding(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        content.saveSelectedItem();
                                        getComboBox().requestFocus();
                                    }
                                });
                            }
                        });
                    }
                    if (!popover.isShowing()) {
                        getAdapter().dispose();
                        content.getItems().clear();
                        content.getItems().addAll(getSkinnable().populateItems());
                        ((RuleTablesView) popover.getContentNode()).selectItem(getSkinnable().getSelectedItem());
                        initListeners();
                        getAdapter().addChangeListener(content.selectedItemProperty(), new ChangeListener<CrgRuleTables>() {
                            @Override
                            public void changed(ObservableValue<? extends CrgRuleTables> observable, CrgRuleTables oldValue, CrgRuleTables newValue) {
                                if (getComboBox().getItems().contains(newValue)) {
                                    getComboBox().getSelectionModel().select(newValue);
                                }
                            }
                        });
                        selectCbValue();
                        popover.show(getComboBox());
                    }
                }
                public void selectCbValue(){
                    select(getSkinnable().getSelectedItem());
                }
                public void select(CrgRuleTables pTable){
                    if(content == null){
                        return; // do nothing if content is not initialized
                    }
                    content.selectInListView(pTable);
                }
            });
            return btnMenu;
        }
        return btn;
    }
    
}

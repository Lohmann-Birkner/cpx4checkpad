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
package de.lb.cpx.client.core.menu.fx.filterlists.layout;

import de.lb.cpx.client.core.model.fx.labeled.LabeledControl;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListAttributeList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Toggle;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Skin class for the FilterOptionChooser handles ui related sutff
 *
 * @author wilde
 * @param <T> type
 * @param <E> type
 */
public class FilterOptionChooserSkin<T extends Object, E extends Control> extends SkinBase<FilterOptionChooser<T, E>> {

    private static final Logger LOG = Logger.getLogger(FilterOptionChooserSkin.class.getName());

    public static final String NO_FILTER = "NO_FILTER";
    private final VBox root;
    private ItemLayout noFilterItem;

    /**
     * creates new instance
     *
     * @param pSkinnable skinnable to apply layout
     */
    public FilterOptionChooserSkin(FilterOptionChooser<T, E> pSkinnable) {
        super(pSkinnable);
        this.betweens = new ArrayList<>();
        root = new VBox();
        root.setFillWidth(true);
        root.spacingProperty().bind(pSkinnable.spacingProperty());
        getChildren().add(root);
        if (pSkinnable.getSearchListAttribute() != null) {
            addFilterOptions(pSkinnable.getSearchListAttribute());
        }
        pSkinnable.searchAttributeProperty().addListener(new ChangeListener<SearchListAttribute>() {
            @Override
            public void changed(ObservableValue<? extends SearchListAttribute> observable, SearchListAttribute oldValue, SearchListAttribute newValue) {
                if (newValue == null) {
                    LOG.severe("Can not process Chooser, SearchListAttribute is null");
                    return;
                }
                addFilterOptions(newValue);
            }
        });
    }

    private void addFilterOptions(SearchListAttribute newValue) {
        root.getChildren().clear();
        for (SearchListAttributeList deep : newValue.getDeepChildren()) {
            //react to list type
//            ItemLayout item = new ItemLayout(newValue.getDataType());
//            item.getRadioButton().setToggleGroup(getSkinnable().getToogleGroup());
            if (deep.isBetween()) {
                BetweenLayout item = new BetweenLayout(newValue.getDataType());
                item.getRadioButton().setToggleGroup(getSkinnable().getToogleGroup());
                for (SearchListAttribute att : deep.getAttributes()) {
                    if (att.isFrom()) {
                        item.setFrom(getSkinnable().registerControl(att));
                    }
                    if (att.isTo()) {
                        item.setTo(getSkinnable().registerControl(att));
                    }
                }
                item.getRadioButton().setUserData(deep.getFirst());
                betweens.add(item);
                root.getChildren().add(item);
            }
            if (deep.isEqual()) {
                ItemLayout item = new ItemLayout(newValue.getDataType());
                item.getRadioButton().setToggleGroup(getSkinnable().getToogleGroup());
                item.add(getSkinnable().registerControl(deep.getFirst()));
                item.getRadioButton().setUserData(deep.getFirst());
                root.getChildren().add(item);
            }
            if (deep.isExpired()) {
                ItemLayout item = new ItemLayout(newValue.getDataType());
                item.getRadioButton().setToggleGroup(getSkinnable().getToogleGroup());
                item.add(getSkinnable().registerControl(deep.getFirst()));//new Label(Lang.get(deep.getFirst().getLanguageKey()).getValue()));//getSkinnable().registerControl(deep.getFirst()));
                item.getRadioButton().setUserData(deep.getFirst());
                root.getChildren().add(item);
            }
            if (deep.isOpen()) {
                ItemLayout item = new ItemLayout(newValue.getDataType());
                item.getRadioButton().setToggleGroup(getSkinnable().getToogleGroup());
                item.add(getSkinnable().registerControl(deep.getFirst()));//new Label(Lang.get(deep.getFirst().getLanguageKey()).getValue()));//getSkinnable().registerControl(deep.getFirst()));
                item.getRadioButton().setUserData(deep.getFirst());
                root.getChildren().add(item);
            }
            if (deep.isToday()) {
                ItemLayout item = new ItemLayout(newValue.getDataType());
                item.getRadioButton().setToggleGroup(getSkinnable().getToogleGroup());
                item.add(getSkinnable().registerControl(deep.getFirst()));//new Label(Lang.get(deep.getFirst().getLanguageKey()).getValue()));//getSkinnable().registerControl(deep.getFirst()));
                item.getRadioButton().setUserData(deep.getFirst());
                root.getChildren().add(item);
            }
        }
        noFilterItem = createNoFilterItem();
        root.getChildren().add(noFilterItem);
    }

    public void selectNoFilterItem() {
        noFilterItem.getRadioButton().setSelected(true);
    }

    private ItemLayout createNoFilterItem() {
        ItemLayout item = new ItemLayout(null);
        item.getRadioButton().setId(NO_FILTER);
        item.getRadioButton().setToggleGroup(getSkinnable().getToogleGroup());
        Label lbl = new Label("Kein Filter");
        lbl.setFont(Font.font(15));
        item.add(lbl);
//        item.getRadioButton().setSelected(true);
        return item;
    }

    public ItemLayout getNoFilterItem() {
        return noFilterItem;
    }
    private List<BetweenLayout> betweens;

    public final List<BetweenLayout> getBetweenLayouts() {
        return new ArrayList<>(betweens);
    }

    protected class BetweenLayout extends ItemLayout {

        protected Control from;
        protected Control to;

        public BetweenLayout(Serializable pDataType) {
            super(pDataType);
        }

        public void setTo(Control pCtrl) {
            if (to != null) {
                remove(pCtrl);
            }
            to = pCtrl;
            add(pCtrl);
        }

        public void setFrom(Control pCtrl) {
            if (from != null) {
                remove(pCtrl);
            }
            from = pCtrl;
            add(pCtrl);
        }

        public Control getTo() {
            return to;
        }

        public Control getFrom() {
            return from;
        }
    }

    protected class ItemLayout extends HBox {

        private final RadioButton radioBtn = new RadioButton();
        private final VBox content = new VBox();
        private final Serializable type;

        public ItemLayout(Serializable pDataType) {
            super(5.0d);
            content.setFillWidth(true);
            getChildren().addAll(radioBtn, content);
            HBox.setHgrow(content, Priority.ALWAYS);
            type = pDataType;
            selectedProperty().bind(radioBtn.selectedProperty());
        }

        public RadioButton getRadioButton() {
            return radioBtn;
        }

        public void add(Control pCtrl) {
            HBox box = new HBox(pCtrl);
            content.getChildren().add(box);
            box.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (MouseButton.PRIMARY.equals(event.getButton())) {
                        radioBtn.setSelected(true);
                        if (!getChildren().isEmpty()) {
                            if (pCtrl instanceof LabeledControl) {
                                pCtrl.getProperties().put(LabeledControl.FOCUS_REQUESTED, null);
                                pCtrl.requestFocus();
                            } else {
                                if (!(pCtrl instanceof Label)) {
                                    pCtrl.requestFocus();
                                }
                            }
                        }
                    }
                }
            });
            if (getSkinnable().hasValue(pCtrl)) {
                radioBtn.setSelected(true);
            }
            pCtrl.disableProperty().bind(selectedProperty().not());
            registerPropertiesListener(pCtrl, radioBtn);
        }

        private void registerPropertiesListener(Control pCtrl, Toggle pToggle) {
            if (pCtrl instanceof LabeledControl) {
                pCtrl.getProperties().addListener(new MapChangeListener<Object, Object>() {
                    @Override
                    public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                        if (change.wasAdded()) {
                            if (change.getKey().equals(LabeledControl.FOCUS_REQUESTED)) {
                                getSkinnable().getToogleGroup().selectToggle(pToggle);
                                pCtrl.getProperties().remove(LabeledControl.FOCUS_REQUESTED);
                            }
                        }
                    }
                });
            }
            if (pCtrl instanceof Label) {
                pCtrl.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        getSkinnable().getToogleGroup().selectToggle(pToggle);
                    }
                });
            }
        }

        public void remove(Control pCtrl) {
            content.getChildren().remove(pCtrl);
            pCtrl.disableProperty().unbind();
        }

        public Serializable getType() {
            return type;
        }

        private BooleanProperty selectedProperty;

        public BooleanProperty selectedProperty() {
            if (selectedProperty == null) {
                selectedProperty = new SimpleBooleanProperty(false);
            }
            return selectedProperty;
        }

        public Boolean isSelected() {
            return selectedProperty.get();
        }
    }
}

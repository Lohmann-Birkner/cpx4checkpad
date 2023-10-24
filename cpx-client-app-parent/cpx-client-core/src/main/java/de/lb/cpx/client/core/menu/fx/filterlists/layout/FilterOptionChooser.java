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

import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;

/**
 * Filter option Chooser handles user input if user must choose between filter
 * options if filterung should occure in range or euqal, or whatever option is
 * set in searchListAttribute
 *
 * TODO:FIX for FutureOptions
 *
 * @author wilde
 * @param <T> datatype of the chooser e.g. date, integer, double etc
 * @param <E> Control to display for value
 */
public abstract class FilterOptionChooser<T extends Object, E extends Control> extends Control {

    public static final double DEFAULT_SPACING = 8.0;

    private static final Logger LOG = Logger.getLogger(FilterOptionChooser.class.getName());
    protected Boolean isDirty = false;
    private final ToggleGroup group = new ToggleGroup();
    private Callback<String, Void> singleFilterDelete;
    private Callback<SearchListAttribute, Void> filterCallback;
    private ObjectProperty<SearchListAttribute> searchAttributeProperty;
    private DoubleProperty spacingProperty;
    private Callback<SearchListAttribute, Control> controlFactory;

    public FilterOptionChooser(SearchListAttribute pAttribute) {
        super();
        setSearchAttribute(pAttribute);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        FilterOptionChooserSkin<T, E> sk = new FilterOptionChooserSkin<>(this);
        //not the bestway! should think about properties map?
        registerBetweenValidation(sk.getBetweenLayouts());
        if (getToogleGroup().getSelectedToggle() == null) {
            sk.selectNoFilterItem();
        }
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                clearAttribute(getSearchListAttribute());
                if (isDirty) {
//                    //if no filter is selected 
//                    //reload with empty filters
                    if (FilterOptionChooserSkin.NO_FILTER.equals(((Styleable) newValue).getId())) {
                        clearAttribute(getSearchListAttribute());
                        getFilterCallback().call(null);
                        isDirty = false;
                        return;
                    }
//                    //clear old value
                    if (oldValue != null) {
                        SearchListAttribute child = (SearchListAttribute) oldValue.getUserData();
                        if (child != null) {
                            clearAttribute(child);
                        }
                    }
                    //filter should occure when other radio button is selected
                    if (newValue != null) {
                        SearchListAttribute child = (SearchListAttribute) newValue.getUserData();
                        if (child != null) {
                            clearAttribute(child);
                            getFilterCallback().call(child);
                            isDirty = false;
                        }
                    }
                } else {
                    //should occure when nothing is selected in 
                    //textfields
                    SearchListAttribute child = (SearchListAttribute) newValue.getUserData();
                    if (child != null) {
                        if (child.isToday() || child.isExpires() || child.isOpen()) {
                            getFilterCallback().call(child);
                        }
                    }
                }
            }

            private void clearAttribute(SearchListAttribute pAttribute) {
                if (pAttribute.getChildren().isEmpty()) {
                    callDeleteFilter(pAttribute);
                } else {
                    for (SearchListAttribute att : pAttribute.getChildren()) {
                        callDeleteFilter(att);
                    }
                }
            }

            private void callDeleteFilter(SearchListAttribute att) {
                if (getSingleFilterDelete() != null) {
                    getSingleFilterDelete().call(att.getKey());
                }
            }
        });
//        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//            @Override
//            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                //dirty means value is stored in chooser 
//                //needs to be resetted on value change
//                if (isDirty) {
//                    for (SearchListAttribute att : getSearchListAttribute().getChildren()) {
//                        getSingleFilterDelete().call(att.getKey());
//                    }
//                    //if no filter is selected 
//                    //reload with empty filters
//                    if (FilterOptionChooserSkin.NO_FILTER.equals(((Styleable) newValue).getId())) {
//                        getFilterCallback().call(getSearchListAttribute());
//                        isDirty = false;
//                        return;
//                    }
//                    //filter for new child value
//                    SearchListAttribute child = (SearchListAttribute) newValue.getUserData();
//                    if (child != null) {
//                        getFilterCallback().call(child);
//                        isDirty = false;
//                        return;
//                    }
//                } else {
//                    //should occure when nothing is selected in 
//                    //textfields
//                    SearchListAttribute child = (SearchListAttribute) newValue.getUserData();
//                    if (child != null) {
//                        if (child.isToday() || child.isExpires() || child.isOpen()) {
//                            getFilterCallback().call(child);
//                            return;
//                        }
//                    }
//                }
//            }
//        });
        return sk;
    }

    @SuppressWarnings("unchecked")
    private void registerBetweenValidation(List<FilterOptionChooserSkin<T, E>.BetweenLayout> betweenLayouts) {
        for (FilterOptionChooserSkin<T, E>.BetweenLayout between : betweenLayouts) {
            Control from = between.getFrom();
            Control to = between.getTo();
            if (from == null || to == null) {
                LOG.warning("Ctrl From or To are null!");
                continue;
            }
            registerLowerThreshold((E) from, getValueProperty((E) to));
            registerUpperThreshold((E) to, getValueProperty((E) from));
        }
    }

    public abstract void registerLowerThreshold(E pCtrl, ObjectProperty<T> pValueProperty);

    public abstract void registerUpperThreshold(E pCtrl, ObjectProperty<T> pValueProperty);

    public abstract ObjectProperty<T> getValueProperty(E pCtrl);

    public void setSingleFilterDelete(Callback<String, Void> pCallback) {
        singleFilterDelete = pCallback;
    }

    public Callback<String, Void> getSingleFilterDelete() {
        return singleFilterDelete;
    }

    public void setFilterCallback(Callback<SearchListAttribute, Void> pCallback) {
        filterCallback = pCallback;
    }

    public Callback<SearchListAttribute, Void> getFilterCallback() {
        return filterCallback;
    }

    public final ObjectProperty<SearchListAttribute> searchAttributeProperty() {
        if (searchAttributeProperty == null) {
            searchAttributeProperty = new SimpleObjectProperty<>();
        }
        return searchAttributeProperty;
    }

    public final void setSearchAttribute(SearchListAttribute pAttribute) {
        searchAttributeProperty().set(pAttribute);
    }

    public final SearchListAttribute getSearchListAttribute() {
        return searchAttributeProperty().get();
    }

    public DoubleProperty spacingProperty() {
        if (spacingProperty == null) {
            spacingProperty = new SimpleDoubleProperty(DEFAULT_SPACING);
        }
        return spacingProperty;
    }

    public Double getSpacing() {
        return spacingProperty().get();
    }

    public void setSpacing(double pSpacing) {
        spacingProperty().set(pSpacing);
    }

    public final ToggleGroup getToogleGroup() {
        return group;
    }

    public Callback<SearchListAttribute, Control> getControlFactory() {
        return controlFactory;
    }

    public void setControlFactory(Callback<SearchListAttribute, Control> pFactory) {
        controlFactory = pFactory;
    }

    //unify behaviour,
    //same code in labledControl
    public void showErrorPopOver(Node pNode, String pText, int pDuration) {
        Label label = new Label(Lang.getError() + ": \n" + pText);
        VBox content = new VBox(label);
        label.getStyleClass().add("black-label");
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(5));
        PopOver over = new AutoFitPopOver(content);
        over.setDetachable(false);
        over.setHideOnEscape(true);
        over.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        over.setAutoHide(true);
        over.setAutoFix(true);
        over.show(pNode);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                over.hide();
            }
        }, pDuration, TimeUnit.SECONDS);
    }

    public abstract Control registerControl(SearchListAttribute pAttribute);

    public abstract Boolean hasValue(Control ctrl);

}

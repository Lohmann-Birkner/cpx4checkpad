/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.shared.lang.Lang;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.PopOver;

/**
 * Baseclass for a labeled control, wrappes control in VBox with an additional
 * label Creates a new control Object with a Label and a generic controll in a
 * vBox
 *
 * @param <T> type of wrapped control
 * @author wilde
 */
public abstract class LabeledControl<T extends Control> extends Control {

    protected final SimpleObjectProperty<T> controlProperty = new SimpleObjectProperty<>();
    private final DoubleProperty spacingProperty = new SimpleDoubleProperty(5.0);
    public static final String FOCUS_REQUESTED = "focus_requested";
    public static final String DEFAULT_STYLE_CLASS = "labeled-control";

    /**
     * creates a new labeled controll with a empty text as label and a control
     *
     * @param pCtrl control to be set
     */
    public LabeledControl(T pCtrl) {
        this("", pCtrl);
    }

    /**
     * creates a new labeled controll with a given text as label and a control
     *
     * @param pLabel label text to show
     * @param pCtrl control to be set
     */
    protected LabeledControl(String pLabel, T pCtrl) {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        controlProperty.setValue(pCtrl);
        setTitle(pLabel);
        setSkin(createDefaultSkin());
        setFocusTraversable(false);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledControlSkin<>(this);
    }
    private StringProperty titleProperty;

    public StringProperty titleProperty() {
        if (titleProperty == null) {
            titleProperty = new SimpleStringProperty();
        }
        return titleProperty;
    }

    public final void setTitle(String pTitle) {
        titleProperty().set(pTitle);
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public StringProperty infoProperty() {
        return ((LabeledControlSkin) getSkin()).infoProperty();
    }

    public void setInfo(String pInfo) {
        ((LabeledControlSkin) getSkin()).infoProperty().set(pInfo);
    }

    public String getInfo() {
        return ((LabeledControlSkin) getSkin()).infoProperty().get();
    }
    private ObjectProperty<VPos> infoPositionProperty;

    public ObjectProperty<VPos> infoPositionProperty() {
        if (infoPositionProperty == null) {
            infoPositionProperty = new SimpleObjectProperty<>(VPos.TOP);
        }
        return infoPositionProperty;
    }

    public VPos getInfoPosition() {
        return infoPositionProperty().get();
    }

    public void setInfoPosition(VPos pPos) {
        infoPositionProperty().set(pPos);
    }

    /**
     * returns the current controllproperty that stores the control
     *
     * @return control in an object property
     */
    protected SimpleObjectProperty<T> getControllProperty() {
        return controlProperty;
    }

    /**
     * get the instance of the specific type of control
     *
     * @return instance of the control
     */
    public T getControl() {
        return controlProperty.getValue();
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        getControl().requestFocus();
    }

    /**
     * set the color of the title
     *
     * @param pColor color to set
     */
    public void setTitleColor(String pColor) {
        ((LabeledControlSkin) getSkin()).setTitleTextFill(pColor);
    }

    /**
     * add a specific style class to the title
     *
     * @param pStyleClass styleclass
     */
    public void addStyleClassToTitle(String pStyleClass) {
        ((LabeledControlSkin) getSkin()).addStyleClassToTitle(pStyleClass);
    }

    /**
     * set the pseudo class for the highlight effect on the title label in the
     * skin class
     *
     * @param pCssStyleClass pseudoclass
     */
    public void setPseudoStyleClass(PseudoClass pCssStyleClass) {
        ((LabeledControlSkin) getSkin()).setPseudoStyleClass(pCssStyleClass);
    }

    public void applyFontWeightToTitle(FontWeight pWeight) {
        ((LabeledControlSkin) getSkin()).applyFontWeightToTitle(pWeight);
    }

    public void registerTooltip(Tooltip pTooltip) {
        ((LabeledControlSkin) getSkin()).setTooltip(pTooltip);
        setTooltip(pTooltip);
    }

    /**
     * @param pAlignment new alignment of the root
     */
    public void setRootAlignment(Pos pAlignment) {
        ((LabeledControlSkin) getSkin()).setRootAlignment(pAlignment);
    }

    public DoubleProperty contentPrefHeightProperty() {
        return ((LabeledControlSkin) getSkin()).contentPrefHeightProperty();
    }
//    /**
//     * @param pAlignment new alignment of the root
//     */
//    public void setContentAlignment(Pos pAlignment) {
//        ((LabeledControlSkin) getSkin()).setContentAlignment(pAlignment);
//    }
    private ObjectProperty<Pos> contentAlignment;

    public ObjectProperty<Pos> contentAlignment() {
        if (contentAlignment == null) {
            contentAlignment = new SimpleObjectProperty<>(Pos.CENTER_LEFT);
        }
        return contentAlignment;
    }

    public Pos getContentAlignment() {
        return contentAlignment().get();
    }

    public void setContentAlignment(Pos pAlignment) {
        contentAlignment().set(pAlignment);
    }
    
    private final ObservableList<Node> menuItems = FXCollections.observableArrayList();
    public ObservableList<Node> getMenuItems(){
        return menuItems;
    }
    
    /**
     * shows the ErrorPopOver with the specificed text for a specific the
     * Duration
     *
     * @param pText text to show in popover
     * @param pDuration duration to show popover before fading out
     */
    public void showErrorPopOver(String pText, int pDuration) {
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
        over.show(getControl());

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                over.hide();
            }
        }, pDuration, TimeUnit.SECONDS);
    }

    /**
     * shows error popover with text for 5 seconds
     *
     * @param pText text to show
     */
    public void showErrorPopOver(String pText) {
        showErrorPopOver(pText, 5);
    }

    /**
     * @return spacing property to control space value between label and ctrl
     */
    public DoubleProperty spacingProperty() {
        return spacingProperty;
    }

    /**
     * @return get spacing between label and ctrl
     */
    public Double getSpacing() {
        return spacingProperty.get();
    }

    /**
     * @param pSpacing new spacing between label and ctrl
     */
    public void setSpacing(Double pSpacing) {
        spacingProperty.set(pSpacing);
    }
}

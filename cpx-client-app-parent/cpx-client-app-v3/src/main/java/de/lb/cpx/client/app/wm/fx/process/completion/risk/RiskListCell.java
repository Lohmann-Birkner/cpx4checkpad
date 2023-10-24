/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.completion.risk;

import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.client.core.model.fx.button.InfoButton;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.textfield.CurrencyTextField;
import de.lb.cpx.client.core.model.fx.textfield.PercentTextField;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 *
 * @author wilde
 * @param <Z> instance of risk detail to display in cell
 */
public abstract class RiskListCell<Z> extends ListCell<RiskAreaEn> {

    private static final Logger LOG = Logger.getLogger(RiskListCell.class.getName());

    public RiskListCell() {
        super();
        setMinHeight(USE_PREF_SIZE);
        setPrefHeight(77.0d);
        setMaxHeight(USE_PREF_SIZE);
    }

//    private BooleanProperty editableProperty;
//    public BooleanProperty editableProperty(){
//        if(editableProperty == null){
//            editableProperty = new SimpleBooleanProperty(true);
//        }
//        return editableProperty;
//    }
    @Override
    protected void updateItem(RiskAreaEn item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if (item == null || empty) {
            setGraphic(null);
            return;
        }
        if (getRiskDetailFactory() != null) {
            setGraphic(createLayout(getRiskDetailFactory().call(item)));
        }
    }

    private Callback<RiskAreaEn, Z> riskDetailFactory = new Callback<RiskAreaEn, Z>() {
        @Override
        @SuppressWarnings("unchecked")
        public Z call(RiskAreaEn param) {
            return getDetailForArea(param);
//            if (param instanceof TWmRiskDetails) {
//                return (Z) param;
//            }
//            if (param instanceof TWmFinalisationRiskDetail) {
//                return (Z) param;
//            }
//            return null;
        }
    };

    public void setRiskDetailFactory(Callback<RiskAreaEn, Z> pCallback) {
        riskDetailFactory = pCallback;
    }

    public Callback<RiskAreaEn, Z> getRiskDetailFactory() {
        return riskDetailFactory;
    }
    private Callback<Z, Boolean> deleteCallback;
    private final Callback<Z, Boolean> defaultDeleteCallback = new Callback<Z, Boolean>() {
        @Override
        public Boolean call(Z param) {
            if (param == null) {
                LOG.warning("item can not be deleted, riskDetails is null!");
                return false;
            }
            LOG.log(Level.INFO, "delete callback was called on item {0}", param.getClass().getName()/*param.getRiskArea().getTranslation()*/);
            return true;
        }
    };

    public void setDeleteCallback(Callback<Z, Boolean> pCallback) {
        deleteCallback = pCallback;
    }

    public Callback<Z, Boolean> getDeleteCallback() {
        return deleteCallback == null ? defaultDeleteCallback : deleteCallback;
    }
    private Callback<Z, Boolean> addCallback;
    private final Callback<Z, Boolean> defaultAddCallback = new Callback<Z, Boolean>() {
        @Override
        public Boolean call(Z param) {
            if (param == null) {
                LOG.warning("item can not be added, riskDetails is null!");
                return false;
            }
            LOG.log(Level.INFO, "addcallback was called on item {0}", param.getClass().getName()/*param.getRiskArea().getTranslation()*/);
            return true;
        }
    };

    public void setAddCallback(Callback<Z, Boolean> pCallback) {
        addCallback = pCallback;
    }

    public Callback<Z, Boolean> getAddCallback() {
        return addCallback == null ? defaultAddCallback : addCallback;
    }

    protected Node createLayout(Z pDetails) {
        if (pDetails == null) {
            HBox box = new HBox(new Label("Keine Angabe"));
            box.setAlignment(Pos.CENTER);
            box.maxWidthProperty().bind(getListView().widthProperty().subtract(20));
            return box;
        }

        GridPane pane = new GridPane();
        pane.setHgap(5.0);
        pane.setVgap(5.0);
        PercentTextField percentField = new PercentTextField();
        percentField.setValue(getRiskPercent(pDetails));//.getRiskPercent());
        percentField.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                pDetails.setRiskPercent(newValue.intValue());
                setRiskPercent(pDetails,newValue.intValue());
                Event.fireEvent(getListView().getParent(), new SaveOrUpdateRiskEvent());
            }
        });

        CurrencyTextField valueField = new CurrencyTextField();
        valueField.setValue(getRiskValue(pDetails).doubleValue());
//        valueField.setValue(pDetails.getRiskValue().doubleValue());
        valueField.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                pDetails.setRiskValue(BigDecimal.valueOf(newValue.doubleValue()));
                setRiskValue(pDetails,BigDecimal.valueOf(newValue.doubleValue()));
                Event.fireEvent(getListView().getParent(), new SaveOrUpdateRiskEvent());
            }
        });
//        valueField.valueProperty().addListener(new ChangeListener<Double>() {
//            @Override
//            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
//                pDetails.setRiskValue(new BigDecimal(newValue));
//            }
//        });
        Label percentText = new Label("Risiko in %");
        if(!RiskDisplayMode.DISPLAY_ALL.equals(getDisplayMode())){
            percentField.setVisible(false);
            percentText.setVisible(false);
        }
        pane.addRow(1, percentText, percentField/*new Label(String.valueOf(pDetails.getRiskPercent()))*/);
        pane.addRow(0, new Label("Verlustvol. in €"), valueField/*new Label(Lang.toDecimal(pDetails.getRiskValue().doubleValue(), 2))*/);
        pane.getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        pane.disableProperty().bind(editableProperty().not());
        VBox boxTitleContent = new VBox(5, new Label(getRiskAreaName()), pane);
        AnchorPane menu = new AnchorPane();
//        menu.setAlignment(Pos.CENTER);
        if (isEditable()) {
            DeleteButton btnDelete = new DeleteButton();
            btnDelete.getGraphic().setStyle("-fx-text-fill:-root03;");
            btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //Awi: should here fetch from item to be sure to delete correct entry!
                    getDeleteCallback().call(pDetails);
                }
            });
            HBox deleteWrapper = new HBox(btnDelete);
            deleteWrapper.setAlignment(Pos.CENTER);
            AnchorPane.setTopAnchor(deleteWrapper, 0.0);
            AnchorPane.setRightAnchor(deleteWrapper, 0.0);
            AnchorPane.setLeftAnchor(deleteWrapper, 0.0);
            AnchorPane.setBottomAnchor(deleteWrapper, 0.0);
            menu.getChildren().add(deleteWrapper);
        }
        if (hasComment(pDetails)) {
            InfoButton btnInfo = new InfoButton();
            btnInfo.setOnAction(new EventHandler<ActionEvent>() {
                AutoFitPopOver popover;

                @Override
                public void handle(ActionEvent event) {
                    if (popover == null) {
                        popover = new AutoFitPopOver();
                        popover.setOnHidden(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                popover = null;
                            }
                        });
                    }
                    if (!popover.isShowing()) {
                        popover.setContentNode(getInfoContent(pDetails));
                        popover.show(btnInfo);
                    } else {
                        popover.hide(Duration.ZERO);
                    }
                }
            });
            btnInfo.setStyle("-fx-padding:0 0 0 0;");
            btnInfo.getGraphic().setStyle("-fx-padding:0 0 0 0;");
            HBox infoWrapper = new HBox(btnInfo);
            infoWrapper.setAlignment(Pos.CENTER);
            AnchorPane.setRightAnchor(infoWrapper, 0.0);
            AnchorPane.setLeftAnchor(infoWrapper, 0.0);
            menu.getChildren().add(infoWrapper);
        }
        HBox.setHgrow(boxTitleContent, Priority.ALWAYS);
        HBox box = new HBox(5, boxTitleContent, menu);
        box.maxWidthProperty().bind(getListView().widthProperty().subtract(20));
        return box;
    }

    public boolean hasComment(Z pDetails) {
//        return false;
        String comment = getRiskComment(pDetails);
        return !(comment == null || comment.isEmpty());
    }

    public Node getInfoContent(Z pDetails) {
        Label title = new Label("Kommentar");
        Label comment = new Label(getRiskComment(pDetails));
        comment.setStyle("-fx-font-weight: normal;");
        comment.setWrapText(true);
        ScrollPane sp = new ScrollPane(comment);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        VBox box = new VBox(5, title, sp);
        box.setPadding(new Insets(8));
        return box;
    }
    
    private ObjectProperty<RiskDisplayMode> displayModeProperty;
    public ObjectProperty<RiskDisplayMode> displayModeProperty(){
        if(displayModeProperty == null){
            displayModeProperty = new SimpleObjectProperty<>(RiskDisplayMode.DISPLAY_ALL);
        }
        return displayModeProperty;
    }
    public RiskDisplayMode getDisplayMode(){
        return displayModeProperty().get();
    }
    public void setDisplayMode(RiskDisplayMode pDisplayMode){
        displayModeProperty().set(pDisplayMode);
    }

    public abstract Integer getRiskPercent(Z pDetails);
    
    public abstract void setRiskPercent(Z pDetails, int intValue);

    public abstract BigDecimal getRiskValue(Z pDetails);
    
    public abstract void setRiskValue(Z pDetails, BigDecimal valueOf);

    public String getRiskAreaName(){
        if(getItem() == null || isEmpty()){
            return "Keine Angabe";
        }
        return getItem().getTranslation().getValue();
    }

    public abstract String getRiskComment(Z pDetails);
    
    public abstract Z getDetailForArea(RiskAreaEn pArea);
}

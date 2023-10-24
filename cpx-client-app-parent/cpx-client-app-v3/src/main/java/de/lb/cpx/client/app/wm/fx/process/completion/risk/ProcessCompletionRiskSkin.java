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

import de.lb.cpx.client.core.model.fx.button.InfoButton;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCurrencyTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledPercentTextField;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 * @param <T> risk entity
 * @param <Z> risk detail entity
 */
public class ProcessCompletionRiskSkin<T extends AbstractEntity,Z extends AbstractEntity> extends SkinBase<ProcessCompletionRisk<T,Z>> {

    private Label lblRiskHeader;
    private VBox boxRiskArea;
    private ListView<RiskAreaEn> lvRiskArea;
    private Label lblRiskPlaceholder;
    private LabeledPercentTextField lblRisk;
    private LabeledCurrencyTextField lblRiskValue;
    private VBox boxRiskAreaList;
    private Button btnRiskAreaListMenu;
    private ComboBox<RiskAreaEn> cbRiskAreaListMenu;
    private HBox boxRiskResult;
    private HBox boxRiskAreaListMenu;
    private HBox boxTitle;
    private VBox boxLayout;

    public ProcessCompletionRiskSkin(ProcessCompletionRisk<T,Z> control) throws IOException {
        super(control);
        getChildren().add(createRoot());
        lvRiskArea.getItems().addListener(new ListChangeListener<RiskAreaEn>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends RiskAreaEn> c) {
                if (c.next()) {
                    showPlaceholder(c.getList().isEmpty());
                }
            }
        });
        getSkinnable().editableProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                lvRiskArea.refresh();
            }
        });
        showPlaceholder(lvRiskArea.getItems().isEmpty());
        getSkinnable().showTitleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showTitle(newValue);
            }
        });
        getSkinnable().showVBarProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showVBar(newValue);
            }
        });
        getSkinnable().displayModeProperty().addListener(new ChangeListener<RiskDisplayMode>() {
            @Override
            public void changed(ObservableValue<? extends RiskDisplayMode> observable, RiskDisplayMode oldValue, RiskDisplayMode newValue) {
                updateDisplayMode(newValue);
            }
        });
        showTitle(getSkinnable().isShowTitle());
        showVBar(getSkinnable().isShowVBar());
        updateValues();
        updateDisplayMode(getSkinnable().getDisplayMode());
    }

    public ScrollBar getHBar() {
        return findScrollBar(lvRiskArea, Orientation.VERTICAL);
    }

    @SuppressWarnings("unchecked")
    private Parent createRoot() throws IOException {
        AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/ProcessCompletionRisk.fxml"));

        boxLayout = (VBox) root.lookup("#boxLayout");
        boxTitle = (HBox) root.lookup("#boxTitle");
        lblRiskHeader = (Label) root.lookup("#lblRiskHeader");
        boxRiskArea = (VBox) root.lookup("#boxRiskArea");

        boxRiskAreaList = (VBox) root.lookup("#boxRiskAreaList");
        lvRiskArea = (ListView) root.lookup("#lvRiskArea");

        lvRiskArea.setSkin(new ListViewSkin<>(lvRiskArea));
        Bindings.bindContent(lvRiskArea.getItems(), getSkinnable().riskAreas());
        lvRiskArea.getStyleClass().add("remove-h-scroll-bar");

        lvRiskArea.setCellFactory(new Callback<ListView<RiskAreaEn>, ListCell<RiskAreaEn>>() {
            @Override
            public ListCell<RiskAreaEn> call(ListView<RiskAreaEn> param) {
                if(getSkinnable().getCellFactory() == null){
                    return new ListCell<>(){
                        @Override
                        protected void updateItem(RiskAreaEn item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if(item == null || empty){
                                setText("");
                                return;
                            }
                            setText(item.getTranslation().getValue());
                        }
                        
                    };
                }
                RiskListCell<Z> cell = getSkinnable().getCellFactory().call(getSkinnable().getRisk());//new CompletionRiskCell(getSkinnable().getRisk());
                cell.editableProperty().bind(getSkinnable().editableProperty());
                cell.displayModeProperty().bind(getSkinnable().displayModeProperty());
                cell.setDeleteCallback(new Callback<Z, Boolean>() {
                    @Override
                    public Boolean call(Z param) {
                        Boolean result = getSkinnable().removeRiskDetail(param);
                        if (result) {
                            lvRiskArea.refresh();
//                            Event.fireEvent(getSkinnable(), new SaveOrUpdateRiskEvent());
                        }
                        return result;
                    }
                });
                cell.setAddCallback(new Callback<Z, Boolean>() {
                    @Override
                    public Boolean call(Z param) {
                        Boolean result = getSkinnable().addRiskDetail(param);
                        if (result) {
                            lvRiskArea.refresh();
//                            Event.fireEvent(getSkinnable(), new SaveOrUpdateRiskEvent());
                        }
                        return result;
                    }
                });
                return cell;
            }
        });
        lvRiskArea.setSelectionModel(new NoSelectionModel<>());

        boxRiskAreaListMenu = (HBox) root.lookup("#boxRiskAreaListMenu");
        boxRiskAreaListMenu.disableProperty().bind(getSkinnable().editableProperty().not());

        btnRiskAreaListMenu = (Button) root.lookup("#btnRiskAreaListMenu");
        btnRiskAreaListMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
        btnRiskAreaListMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getSkinnable().addRiskDetail(cbRiskAreaListMenu.getSelectionModel().getSelectedItem())) {
                    //make positive feedback??
                    //make negative feedback if adding failed??
                }
                cbRiskAreaListMenu.getSelectionModel().clearSelection();
            }
        });

        cbRiskAreaListMenu = (ComboBox<RiskAreaEn>) root.lookup("#cbRiskAreaListMenu");
        cbRiskAreaListMenu.getStyleClass().add("risk-combo-box");
        cbRiskAreaListMenu.setItems(FXCollections.observableArrayList(RiskAreaEn.values()));
        cbRiskAreaListMenu.setConverter(new StringConverter<RiskAreaEn>() {
            @Override
            public String toString(RiskAreaEn object) {
                if (object == null) {
                    return null;
                }
                return object.getTranslation().getValue();
            }

            @Override
            public RiskAreaEn fromString(String string) {
                return RiskAreaEn.valueOf(string);
            }
        });
        btnRiskAreaListMenu.disableProperty().bind(Bindings.when(cbRiskAreaListMenu.getSelectionModel().selectedItemProperty().isNull()).then(true).otherwise(false));

        lblRiskPlaceholder = (Label) root.lookup("#lblRiskAreaPlaceholder");

        boxRiskResult = (HBox) root.lookup("#boxRiskResult");
        boxRiskResult.disableProperty().bind(getSkinnable().editableProperty().not());
        boxRiskResult.setAlignment(Pos.CENTER_RIGHT);

        lblRisk = (LabeledPercentTextField) root.lookup("#lblRisk");
        lblRisk.getControl().setMaxHeight(Double.MAX_VALUE);
//        lblRisk.getControl().setMinValue(0);
//        lblRisk.getControl().setMaxValue(100);
//        lblRisk.setStyle("-fx-background-color:white;");

        lblRiskValue = (LabeledCurrencyTextField) root.lookup("#lblRiskValue");
        lblRiskValue.getControl().setMaxWidth(Double.MAX_VALUE);
        lblRiskValue.getControl().prefWidthProperty().bind(boxRiskResult.widthProperty().divide(2).subtract(10));
        lblRiskValue.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                getSkinnable().getRisk().setRiskValueTotal(BigDecimal.valueOf(newValue.doubleValue()));
                getSkinnable().setRiskValueTotal(BigDecimal.valueOf(newValue.doubleValue()));
                Event.fireEvent(getSkinnable(), new SaveOrUpdateRiskEvent());
            }
        });

        lblRisk.getControl().prefWidthProperty().bind(boxRiskResult.widthProperty().divide(2).subtract(10));
        lblRisk.getControl().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                getSkinnable().getRisk().setRiskPercentTotal(newValue.intValue());
                getSkinnable().setRiskPercentTotal(newValue.intValue());
                Event.fireEvent(getSkinnable(), new SaveOrUpdateRiskEvent());
            }
        });
        return root;
    }
    private void updateDisplayMode(RiskDisplayMode newValue) {
        newValue = Objects.requireNonNullElse(newValue, RiskDisplayMode.DISPLAY_ALL);
        showRiskPercent(RiskDisplayMode.DISPLAY_ALL.equals(newValue));
    }

    private void showRiskPercent(boolean pShow) {
        if (pShow) {
            if (!boxRiskResult.getChildren().contains(lblRisk)) {
                boxRiskResult.getChildren().add(lblRisk);
                lblRisk.getControl().prefWidthProperty().unbind();
//                lblRiskValue.getControl().prefWidthProperty().bind(boxRiskResult.widthProperty().divide(2).subtract(10));
            }
        } else {
            if (boxRiskResult.getChildren().contains(lblRisk)) {
                boxRiskResult.getChildren().remove(lblRisk);
                lblRisk.getControl().prefWidthProperty().unbind();
//                lblRiskValue.getControl().prefWidthProperty().bind(boxRiskResult.widthProperty().subtract(10));
            }
        }
    }

    private void showPlaceholder(boolean pShow) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
        if (pShow) {
            //remove listview,add placeholder
            if (boxRiskArea.getChildren().contains(boxRiskAreaList)) {
                boxRiskArea.getChildren().remove(boxRiskAreaList);
            }
            if (!boxRiskArea.getChildren().contains(lblRiskPlaceholder)) {
                boxRiskArea.getChildren().add(lblRiskPlaceholder);
            }
        } else {
            //add listview,remove placeholder
            if (!boxRiskArea.getChildren().contains(boxRiskAreaList)) {
                boxRiskArea.getChildren().add(boxRiskAreaList);
            }
            if (boxRiskArea.getChildren().contains(lblRiskPlaceholder)) {
                boxRiskArea.getChildren().remove(lblRiskPlaceholder);
            }
        }
//            }
//        });
    }

//    private void updateValues(TWmRisk risk) {
//        lblRiskHeader.setText(risk.getRiskPlaceOfReg() != null ? risk.getRiskPlaceOfReg().getTranslation().getValue() : "Keine Angabe");
//        lblRisk.setValue(risk.getRiskPercentTotal());
//        lblRiskValue.setValue(risk.getRiskValueTotal() != null ? risk.getRiskValueTotal().doubleValue() : 0.0);
//        Button infoButton = createInfoButton();
//        if (infoButton != null) {
//            if (boxTitle.getChildren().size() > 2) {
//                boxTitle.getChildren().set(1, infoButton);
//            } else {
//                boxTitle.getChildren().add(infoButton);
//            }
//        }
//    }
    private void updateValues() {
        lblRiskHeader.setText(getSkinnable().getPlaceOfRegEn() != null ? getSkinnable().getPlaceOfRegEn().getTranslation().getValue() : "Keine Angabe");
//        lblRiskHeader.setText(getSkinnable().getRiskType() != null ? getSkinnable().getRiskType().getTranslation().getValue() : VersionRiskTypeEn.NOT_SET.getTranslation().getValue());
        lblRisk.setValue(getSkinnable().getRiskPercentTotal());
        lblRiskValue.setValue(getSkinnable().getRiskValueTotal() != null ? getSkinnable().getRiskValueTotal().doubleValue() : 0.0);
        Button infoButton = createInfoButton();
        if (infoButton != null) {
            if (boxTitle.getChildren().size() > 2) {
                boxTitle.getChildren().set(1, infoButton);
            } else {
                boxTitle.getChildren().add(infoButton);
            }
        }
    }
    
    private Button createInfoButton() {
        if (!getSkinnable().hasComment(getSkinnable().getRisk())) {
            return null;
        }
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
//                        popover.setContentNode(getInfoContent(getSkinnable().getRisk()));
                    popover.setContentNode(getInfoContent(getSkinnable().getRisk()));
                    popover.show(btnInfo);
                } else {
                    popover.hide(Duration.ZERO);
                }
                
            }
        });
        btnInfo.setStyle("-fx-padding:0 0 0 0;");
        btnInfo.getGraphic().setStyle("-fx-padding:0 0 0 0;-fx-text-fill:black;");
        return btnInfo;
    }

//    public boolean hasComment(TWmRisk pRisk) {
//        return !(pRisk.getRiskComment() == null || pRisk.getRiskComment().isEmpty());
//    }

//    private Node getInfoContent(TWmRisk pRisk) {
//        Label title = new Label("Kommentar");
//        title.setStyle("-fx-font-weight: bold;");
//        Label comment = new Label(pRisk.getRiskComment());
//        comment.setStyle("-fx-font-weight: normal;");
//        comment.setWrapText(true);
//        ScrollPane sp = new ScrollPane(comment);
//        sp.setFitToHeight(true);
//        sp.setFitToWidth(true);
//        VBox box = new VBox(5, title, sp);
//        box.setPadding(new Insets(8));
//        return box;
//    }
    private Node getInfoContent(T pRisk) {
        Label title = new Label("Kommentar");
        title.setStyle("-fx-font-weight: bold;");
        Label comment = new Label(getSkinnable().getRiskComment(pRisk));
        comment.setStyle("-fx-font-weight: normal;");
        comment.setWrapText(true);
        ScrollPane sp = new ScrollPane(comment);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        VBox box = new VBox(5, title, sp);
        box.setPadding(new Insets(8));
        return box;
    }
    private void showTitle(Boolean pShow) {
        if (pShow) {
            //add title box if not present
            if (!boxLayout.getChildren().contains(boxTitle)) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boxLayout.getChildren().add(boxTitle);
                    }
                });
            }
        } else {
            if (boxLayout.getChildren().contains(boxTitle)) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boxLayout.getChildren().remove(boxTitle);
                    }
                });
            }
        }
    }

    private void showVBar(boolean pShow) {
        if (pShow) {
//            if (!boxRiskAreaListContent.getChildren().contains(sbHBar)) {
//                boxRiskAreaListContent.getChildren().add(sbHBar);
//            }
            lvRiskArea.getStyleClass().remove("remove-v-scroll-bar");
        } else {
            lvRiskArea.getStyleClass().add("remove-v-scroll-bar");
//            if (boxRiskAreaListContent.getChildren().contains(sbHBar)) {
//                boxRiskAreaListContent.getChildren().remove(sbHBar);
//            }
        }
    }

    private ScrollBar findScrollBar(ListView<?> listView, Orientation orientation) {
        Set<Node> set = listView.lookupAll(".scroll-bar");
        for (Node node : set) {
            ScrollBar bar = (ScrollBar) node;
            if (bar.getOrientation() == orientation) {
                return bar;
            }
        }
        return null;
    }

//    private void syncBar(ScrollBar hBar, ScrollBar bar) {
//        if (bar == null) {
//            return;
//        }
//        if (hBar == null) {
//            return;
//        }
//        hBar.visibleProperty().bindBidirectional(bar.visibleProperty());
//        hBar.visibleAmountProperty().bindBidirectional(bar.visibleAmountProperty());
//        hBar.minProperty().bindBidirectional(bar.minProperty());
//        hBar.maxProperty().bindBidirectional(bar.maxProperty());
//        bar.valueProperty().bindBidirectional(hBar.valueProperty());
//    }
    public class NoSelectionModel<T> extends MultipleSelectionModel<T> {

        @Override
        public ObservableList<Integer> getSelectedIndices() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public ObservableList<T> getSelectedItems() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public void selectIndices(int index, int... indices) {
            //empty implementation
        }

        @Override
        public void selectAll() {
            //empty implementation
        }

        @Override
        public void selectFirst() {
            //empty implementation
        }

        @Override
        public void selectLast() {
            //empty implementation
        }

        @Override
        public void clearAndSelect(int index) {
            //empty implementation
        }

        @Override
        public void select(int index) {
            //empty implementation
        }

        @Override
        public void select(T obj) {
            //empty implementation
        }

        @Override
        public void clearSelection(int index) {
            //empty implementation
        }

        @Override
        public void clearSelection() {
            //empty implementation
        }

        @Override
        public boolean isSelected(int index) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void selectPrevious() {
            //empty implementation
        }

        @Override
        public void selectNext() {
            //empty implementation
        }
    }

}

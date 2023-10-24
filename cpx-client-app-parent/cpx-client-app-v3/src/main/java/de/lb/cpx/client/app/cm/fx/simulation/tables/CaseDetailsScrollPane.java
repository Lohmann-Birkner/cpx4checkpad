/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.tables;

import de.lb.cpx.client.app.cm.fx.simulation.gridpane.CaseVersionGridPane;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.client.core.model.fx.comparablepane.ScrollCompPane;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 * Class to handle the case data in an scrollpane with an gridpane is of type
 * Comaprable pane, used for simulation
 *
 * @author wilde
 * @param <E> comparable content, no further info for content because of problem
 * with tcase and tcasedetails
 */
public class CaseDetailsScrollPane<E extends ComparableContent<? extends AbstractEntity>> extends ScrollCompPane<E> {

    public enum RowPosition {
        AdmReason12, AdmReason34, AdmCause, AdmissionText, PatientWeight, DisReason12, DisReason3, DischargeText,
        AdmMode, Hmv, Age, Los, SimulDays, Leave, CareDays, LosIntensiv, Baserate;

        public static RowPosition get2Ordinal(int pOrdinal) {
            RowPosition[] mValues = RowPosition.values();
            for (RowPosition pVal : mValues) {
                if (pVal.ordinal() == pOrdinal) {
                    return pVal;
                }
            }
            return null;
        }
    }

    private Label lblAdmReason12Text;
    private Label lblAdmReason34Text;
    private Label lblAdmCauseText;
    private Label lblAdmModeText;
    private Label lblHmvText;
    private Label lblAgeText;
    private Label lblLosText;
    private Label lblDays;
    private Label lblPatientWeightText;
    private Label lblAdmissionText;
    private Label lblDischargeText;
    private Label lblBaserateText;
    private Label lblDisReason12Text;
    private Label lblDisReason3Text;
    private Label lblLeaveText;
    private Label lblCareDays;
    private Label lblLosIntensiv;
    private final List<RowConstraints> rowConstraint = new ArrayList<>();

    /**
     * creates new instance
     */
    public CaseDetailsScrollPane() {
        super();
        setUpLanguage();
        //set row constrained to be shared between items
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        rowConstraint.add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.CENTER, true));

//        getInfo().getRowConstraints().addAll(rowConstraint);
    }

    @Override
    public ScrollPane createInfoPane() {
        final String mainLabelClass = "cpx-main-label";
        InfoGridPane info = new InfoGridPane();
        lblAdmReason12Text = new Label();
        lblAdmReason12Text.getStyleClass().add(mainLabelClass);
        lblAdmReason34Text = new Label();
        lblAdmReason34Text.getStyleClass().add(mainLabelClass);
        lblAdmCauseText = new Label();
        lblAdmCauseText.getStyleClass().add(mainLabelClass);
        lblAdmModeText = new Label();
        lblAdmModeText.getStyleClass().add(mainLabelClass);
        lblHmvText = new Label();
        lblHmvText.getStyleClass().add(mainLabelClass);
        lblAgeText = new Label();
        lblAgeText.getStyleClass().add(mainLabelClass);
        lblLosText = new Label();
        lblLosText.getStyleClass().add(mainLabelClass);
        lblDays = new Label();
        lblDays.getStyleClass().add(mainLabelClass);
        lblPatientWeightText = new Label();
        lblPatientWeightText.getStyleClass().add(mainLabelClass);
        lblAdmissionText = new Label();
        lblAdmissionText.getStyleClass().add(mainLabelClass);
        lblDischargeText = new Label();
        lblDischargeText.getStyleClass().add(mainLabelClass);
        lblBaserateText = new Label();
        lblBaserateText.getStyleClass().add(mainLabelClass);
        lblDisReason12Text = new Label();
        lblDisReason12Text.getStyleClass().add(mainLabelClass);
        lblDisReason3Text = new Label();
        lblDisReason3Text.getStyleClass().add(mainLabelClass);
        lblLeaveText = new Label();
        lblLeaveText.getStyleClass().add(mainLabelClass);
        lblCareDays = new Label();
        lblCareDays.getStyleClass().add(mainLabelClass);
        lblLosIntensiv = new Label();
        lblLosIntensiv.getStyleClass().add(mainLabelClass);

        info.addRow(RowPosition.AdmReason12, new RowContent(RowPosition.AdmReason12, lblAdmReason12Text));
        info.addRow(RowPosition.AdmReason34, new RowContent(RowPosition.AdmReason34, lblAdmReason34Text));
        info.addRow(RowPosition.AdmCause, new RowContent(RowPosition.AdmCause, lblAdmCauseText));
        info.addRow(RowPosition.AdmissionText, new RowContent(RowPosition.AdmissionText, lblAdmissionText));
        info.addRow(RowPosition.PatientWeight, new RowContent(RowPosition.PatientWeight, lblPatientWeightText));
        info.addRow(RowPosition.DisReason12, new RowContent(RowPosition.DisReason12, lblDisReason12Text));
        info.addRow(RowPosition.DisReason3, new RowContent(RowPosition.DisReason3, lblDisReason3Text));
        info.addRow(RowPosition.DischargeText, new RowContent(RowPosition.DischargeText, lblDischargeText));
        info.addRow(RowPosition.AdmMode, new RowContent(RowPosition.AdmMode, lblAdmModeText));
        info.addRow(RowPosition.Hmv, new RowContent(RowPosition.Hmv, lblHmvText));
        info.addRow(RowPosition.Age, new RowContent(RowPosition.Age, lblAgeText));
        info.addRow(RowPosition.Los, new RowContent(RowPosition.Los, lblLosText));
        info.addRow(RowPosition.SimulDays, new RowContent(RowPosition.SimulDays, lblDays));
        info.addRow(RowPosition.Leave, new RowContent(RowPosition.Leave, lblLeaveText));
        info.addRow(RowPosition.CareDays, new RowContent(RowPosition.CareDays, lblCareDays));
        info.addRow(RowPosition.LosIntensiv, new RowContent(RowPosition.LosIntensiv, lblLosIntensiv));
        info.addRow(RowPosition.Baserate, new RowContent(RowPosition.Baserate, lblBaserateText));

        HBox.setHgrow(info, Priority.ALWAYS);
        info.minWidthProperty().bindBidirectional(minInfoWidthProperty());
        ScrollPane pane = new ScrollPane(info);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.getStyleClass().add("remove-all-scroll-bars");
        return pane;
    }

    @Override
    public ScrollPane createNewVersionTableView(E pContent) {
        return new GridVersionScrollPane(pContent);
    }

    private void setUpLanguage() {
        lblDisReason3Text.setText(Lang.getDischargeReason2());
        lblAdmCauseText.setText(Lang.getAdmissionCause());
        lblAdmModeText.setText(Lang.getModeOfAdmission());
        lblAdmReason34Text.setText(Lang.getAdmissionReason2());
        lblAdmReason12Text.setText(Lang.getAdmissionReason());
        lblAdmissionText.setText(Lang.getAdmissionDate());
        lblBaserateText.setText(Lang.getBaserate());
        lblDisReason12Text.setText(Lang.getDischargeReason());
        lblDischargeText.setText(Lang.getDischargeDate());
        lblHmvText.setText(Lang.getArtificialRespiration());
        lblAgeText.setText(Lang.getAge());
        lblLosText.setText(Lang.getDaysStay());
        lblPatientWeightText.setText(Lang.getAdmissionWeight());
        lblDays.setText(Lang.getDaysSimulated());
        lblLeaveText.setText(Lang.getDaysCalculated());
        lblCareDays.setText("Pflegetage");
        lblLosIntensiv.setText(Lang.getRulesTxtCritIntensivStayDis());
    }

    /**
     * de facto cell class of the gridpane
     */
    private class RowContent extends VBox {

        /**
         * creates nwe row content to handle content and apply css style to
         * whole cell
         *
         * @param pIndex row index
         * @param pChildren number of children in row
         */
        RowContent(int pIndex, Node... pChildren) {
            super(pChildren);
            setFillWidth(true);
            GridPane.setHgrow(this, Priority.ALWAYS);
            if (pIndex % 2 == 0) {
                // even
                getStyleClass().add("case-management-grid-even");
            } else {
                // odd
                getStyleClass().add("case-management-grid-odd");
            }
        }

        RowContent(RowPosition pRowPosition, Node... pChildren) {
            this(pRowPosition.ordinal(), pChildren);
        }
    }

    private class InfoGridPane extends GridPane {

        InfoGridPane() {
            super();
        }

        public void addRow(RowPosition pRowPosition, VBox pBox) {
            super.addRow(pRowPosition.ordinal(), pBox);
        }

    }

    /**
     * implemenation of the item that is compared
     */
    private class GridVersionScrollPane extends ScrollPane {

        /**
         * creates new instance
         *
         * @param pContent content that shoud be compared
         */
        GridVersionScrollPane(E pContent) {
            super();
            setFitToHeight(true);
            setFitToWidth(true);
            setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            getStyleClass().add("remove-all-scroll-bars");
            disableProperty().bind(pContent.editableProperty().not());
            //bind scrollpane accordingly to generel one, after skin is set (rendered)
            skinProperty().addListener(new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                    for (Node n : lookupAll(".scroll-bar")) {
                        if (n instanceof ScrollBar) {
                            ScrollBar bar = (ScrollBar) n;
                            if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                                bar.setDisable(true);
                                ScrollBar scrollBar = getVScrollBar();
                                scrollBar.valueProperty().bindBidirectional(bar.valueProperty());
                                scrollBar.minProperty().bindBidirectional(bar.minProperty());
                                scrollBar.maxProperty().bindBidirectional(bar.maxProperty());
                            }
                        }
                    }
                }
            });
            setLayout(pContent);
        }

        private void setLayout(E pContent) {
            CaseVersionGridPane<E> gridPane = new CaseVersionGridPane<>(pContent, rowConstraint);
            gridPane.minWidthProperty().bind(versionContentWidthProperty());
            gridPane.prefWidthProperty().bind(versionContentWidthProperty());
            gridPane.maxWidthProperty().bind(versionContentWidthProperty());
            setContent(gridPane);
        }

    }

}

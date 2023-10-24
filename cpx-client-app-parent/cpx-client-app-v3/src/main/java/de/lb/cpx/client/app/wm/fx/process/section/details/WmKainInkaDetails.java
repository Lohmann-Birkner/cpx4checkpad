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
package de.lb.cpx.client.app.wm.fx.process.section.details;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.model.TP301Kain;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.model.TP301KainInkaPvt;
import de.lb.cpx.model.TP301KainInkaPvv;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.enums.Tp301Key30En;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 * @param <E> type
 */
public abstract class WmKainInkaDetails<E extends TP301KainInka> extends WmDetails<E> {

    private static final Logger LOG = Logger.getLogger(WmKainInkaDetails.class.getName());

    public WmKainInkaDetails(ProcessServiceFacade pFacade, E pItem) {
        super(pFacade, pItem);
    }

//    @Override
//    public String getDetailTitle() {
//        return item.isInka() ? "INKA" : "KAIN";
//    }
    protected TP301Inka getInka() {
        if (item != null && item.isInka()) {
            return (TP301Inka) item;
        }
        return null;
    }

    protected TP301Kain getKain() {
        if (item != null && item.isKain()) {
            return (TP301Kain) item;
        }
        return null;
    }

    @Override
    protected final Parent getDetailNode() {
        List<TP301KainInkaPvv> kainInkaPvvs = item.getKainInkaPvvs();
//        Set<TP301KainInkaPvv> kainInkaPvvs = kainInka.getKainInkaPvvs();

        VBox vbox = new VBox();
        VBox.setVgrow(vbox, Priority.ALWAYS);

        //Pna: 13.12.18, putting VBox inside a scrollPane. With this approach, we don't have to worry about setting the prefHeight and prefWidth attributes.
        ScrollPane sc = new ScrollPane(vbox);
        //disabled showing the horizontal ScrollBar
        sc.setFitToWidth(true);
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(Lang.getProcessListDateFormat());

        kainInkaPvvs.forEach((TP301KainInkaPvv pvv) -> {
            Label fullPvtText = new Label();
            //                fullPvtText.getStyleClass().add("cpx-detail-label");
            fullPvtText.setPadding(new Insets(1.0, 1.0, 0, 0));
            fullPvtText.setPrefWidth(700);
            fullPvtText.setWrapText(true);
         
            TitledPane tpGenerelInfos = new TitledPane();

            tpGenerelInfos.setMinWidth(0);
            StringBuilder sb = new StringBuilder();
            if (sb.length() > 0) {
                sb.append("\r\n");
            }
            String key30 = pvv.getInformationKey30();
            if (key30 == null || key30.trim().isEmpty()) {
                LOG.log(Level.SEVERE, "TP301 Key 30 is empty!");
            }
            String kainDate = item.isKain()?("Eingegangen: " + Lang.toDate(getKain().getReceivingDate()) +"\r\n"):"";
            Tp301Key30En key30En = (Tp301Key30En) CpxEnumInterface.findEnum(Tp301Key30En.values(), key30);
            if (key30En == null) {
                LOG.log(Level.SEVERE, "This TP301 Key 30 seems to be invalid: {0}", key30);
            }
            final String key30Desc = key30En == null ? "Unknown key" : key30En.getTranslation().getValue();
            sb.append(key30).append(" - ").append( key30Desc);
            Label lb = new Label(kainDate + "PVV: " + sb);
            HBox.setHgrow(lb, Priority.ALWAYS);
            tpGenerelInfos.setText(lb.getText());

//                tpGenerelInfos.autosize();
// to hide the expansion of the titledpane
//                tpGenerelInfos.expandedProperty().setValue(Boolean.FALSE);
            HBox.setHgrow(tpGenerelInfos, Priority.ALWAYS);
            tpGenerelInfos.setWrapText(true);
            Tooltip tooltip = new Tooltip(sb.toString());
            tooltip.setStyle("-fx-font-size: 14px");
            tooltip.setWrapText(true);
            tooltip.setMaxWidth(600);
            tpGenerelInfos.setTooltip(tooltip);
            GridPane gpInfos = new GridPane();
//                gpInfos.getStyleClass().add("default-grid");
            gpInfos.setVgap(5.0);
            gpInfos.setHgap(10.0);
            Label labelBillNo = new Label("Rechnungsnummer:");
            labelBillNo.getStyleClass().add("cpx-detail-label");
            GridPane.setValignment(labelBillNo, VPos.TOP);
            Label labelBillNoValue = new Label(pvv.getBillNr());
            labelBillNoValue.setWrapText(true);
            Label labelBillDate = new Label("Rechnungsdatum:");
            labelBillDate.getStyleClass().add("cpx-detail-label");
            GridPane.setValignment(labelBillDate, VPos.TOP);
            Label labelBillDateValue = new Label(formatter.format(pvv.getBillDate()).substring(0, 10));
            labelBillDateValue.setWrapText(true);
            gpInfos.add(labelBillNo, 0, 0);
            gpInfos.add(labelBillNoValue, 1, 0);
//                gpInfos.add(labelBillDate, 0, 1);
//                gpInfos.add(labelBillDateValue, 1, 1);
// get all PVT segments of PVV
            List<TP301KainInkaPvt> allPvts;
            if (pvv.getId() > 0L) {
                EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
                allPvts = processServiceBean.get().getAllPvtsForPvv(pvv.getId());
            } else {
                allPvts = pvv.getKainInkaPvts();    //Pna:18.12.2018
            }
//                List<TP301KainInkaPvt> allPvts = pvv.getKainInkaPvts();
            StringBuilder sb2 = new StringBuilder();
            allPvts.forEach((TP301KainInkaPvt pvt) -> {
                sb2.append(pvt.getText()).append(" ");
            });
            fullPvtText.setText(sb2.toString());
//                if (kainInka.isKain()) {
            VBox vboxPvtsTT = new VBox();
            vboxPvtsTT.setSpacing(5);
            VBox vboxPvtsDV = new VBox();
            vboxPvtsDV.setSpacing(5);

            allPvts.forEach(new Consumer<TP301KainInkaPvt>() {
                private int i = 0;

                @Override
                public void accept(TP301KainInkaPvt pvt) {
                    if (pvt != null) {

                        i = i + 1;

                        Label lbPvt = new Label("PVT " + i);
                        lbPvt.getStyleClass().add("cpx-detail-label");

                        Label pvtText = new Label(pvt.getText().isEmpty() ? "" : pvt.getText());
                        pvtText.setWrapText(true);
                        GridPane.setValignment(pvtText, VPos.CENTER);

                        if (isPvtHasIcdsAndOrOpses(pvt)) {
                            GridPane gpWithIcdsAndOrOpses = createGpWithIcdsAndOrOpses(pvt);
                            vboxPvtsTT.getChildren().addAll(lbPvt, gpWithIcdsAndOrOpses, pvtText);
                        } else {
                            vboxPvtsTT.getChildren().addAll(lbPvt, pvtText);
                        }
                    } else {
                        LOG.log(Level.WARNING, "PVT segment is null..");
                    }

                }
            });
            allPvts.forEach((TP301KainInkaPvt pvt) -> {
                if (pvt != null) {
                    Label pvtText = new Label(pvt.getText().isEmpty() ? "" : pvt.getText());
                    pvtText.setWrapText(true);
                    GridPane.setValignment(pvtText, VPos.CENTER);

                    if (isPvtHasIcdsAndOrOpses(pvt)) {
                        GridPane gpWithIcdsAndOrOpses = createGpWithIcdsAndOrOpses(pvt);
                        vboxPvtsDV.getChildren().addAll(gpWithIcdsAndOrOpses, pvtText);
                    } else {
                        vboxPvtsDV.getChildren().addAll(pvtText);
                    }
                } else {
                    LOG.log(Level.WARNING, "PVT segment is null..");
                }
            });
            ColumnConstraints columnConstraintHalf = new ColumnConstraints();
            gpInfos.getColumnConstraints().add(columnConstraintHalf);

            Label labelFullDetails = new Label("PVTs Details");
            labelFullDetails.getStyleClass().add("cpx-detail-label");
            labelFullDetails.setWrapText(true);
            ScrollPane scPvts = new ScrollPane(vboxPvtsTT);
//disabled the horizontal ScrollBar
            scPvts.setFitToWidth(true);
            scPvts.setMaxWidth(700);
            scPvts.setMaxHeight(800);
//
            Pane graph = ExtendedInfoHelper.addInfoPane(labelFullDetails, scPvts, PopOver.ArrowLocation.RIGHT_CENTER);
            graph.setMaxWidth(100);

            VBox vb;
            if (!allPvts.isEmpty()) {
                vb = new VBox(gpInfos, graph, vboxPvtsDV);
//                    vb = new VBox(gpInfos, graph, graphFullText, vboxPvtsDV);
            } else {
                vb = new VBox(gpInfos);
            }
            vb.setSpacing(12);
            tpGenerelInfos.setContent(vb);
            vbox.getChildren().addAll(tpGenerelInfos);
        });
        return sc;
    }

    private boolean isPvtHasIcdsAndOrOpses(TP301KainInkaPvt pvt) {
        if (pvt != null) {
            return (pvt.getMainDiadIcd() != null && !pvt.getMainDiadIcd().isEmpty()) || (pvt.getMainDiagSecondryIcd() != null && !pvt.getMainDiagSecondryIcd().isEmpty())
                    || (pvt.getSecondaryDiagIcd() != null && !pvt.getSecondaryDiagIcd().isEmpty()) || (pvt.getSecondarySecondDiagIcd() != null && !pvt.getSecondarySecondDiagIcd().isEmpty())
                    || (pvt.getOpsCode() != null && !pvt.getOpsCode().isEmpty());
        }
        return false;
    }

    private GridPane createGpWithIcdsAndOrOpses(TP301KainInkaPvt pvt) {
        GridPane gpPvts = new GridPane();

        if (pvt != null && pvt.getMainDiadIcd() != null && !pvt.getMainDiadIcd().isEmpty()) {
            Label lbHd = new Label("HD: ");
            lbHd.getStyleClass().add("cpx-detail-label");
//          Label lbHdValue = new Label(pvt.getMainDiadIcd() == null ? "" : pvt.getMainDiadIcd().concat(" (" + pvt.getMainDiagLoc() == null ? "" : pvt.getMainDiagLoc().getTranslation().getAbbreviation() + ")"));
            final String labelText;
            if (pvt.getMainDiadIcd() == null) {
                labelText = "";
            } else {
                labelText = pvt.getMainDiadIcd().concat(pvt.getMainDiagLoc() == null ? "" : " (" + pvt.getMainDiagLoc().getTranslation().getValue() + ")") + "    ";
            }
            //pvt.getMainDiadIcd() == null ? "" : pvt.getMainDiadIcd().concat(pvt.getMainDiagLoc() == null ? "" : " (" + pvt.getMainDiagLoc().getTranslation().getValue() + ")") + "    "
            Label lbHdValue = new Label(labelText);
            lbHdValue.setWrapText(true);

            gpPvts.addRow(0, lbHd, lbHdValue);
//            gpPvts.add(lbHd, 0, 1);
//            gpPvts.add(lbHdValue, 1, 1);
        }

        if (pvt != null && pvt.getMainDiagSecondryIcd() != null && !pvt.getMainDiagSecondryIcd().isEmpty()) {
            Label lbHdSec = new Label("HD Sek: ");
            lbHdSec.getStyleClass().add("cpx-detail-label");
//                            Label lbHdSecValue = new Label(pvt.getMainDiagSecondryIcd() == null ? "" : pvt.getMainDiagSecondryIcd() + " (" + pvt.getMainDiagSecondryLoc() == null ? "" : pvt.getMainDiagSecondryLoc().getTranslation().getAbbreviation() + ")");
            final String labelText;
            if (pvt.getMainDiagSecondryIcd() == null) {
                labelText = "";
            } else {
                labelText = pvt.getMainDiagSecondryIcd().concat(pvt.getMainDiagSecondryLoc() == null ? "" : " (" + pvt.getMainDiagSecondryLoc().getTranslation().getValue() + ")");
            }
            Label lbHdSecValue = new Label(labelText);
            lbHdSecValue.setWrapText(true);

            gpPvts.addRow(0, lbHdSec, lbHdSecValue);
//            gpPvts.add(lbHdSec, 2, 1);
//            gpPvts.add(lbHdSecValue, 3, 1);
        }

        if (pvt != null && pvt.getSecondaryDiagIcd() != null && !pvt.getSecondaryDiagIcd().isEmpty()) {
            Label lbNd = new Label("ND: ");
            lbNd.getStyleClass().add("cpx-detail-label");
            final String labelText;
            if (pvt.getSecondaryDiagIcd() == null) {
                labelText = "";
            } else {
                labelText = pvt.getSecondaryDiagIcd().concat(pvt.getSecondaryDiagLoc() == null ? "" : " (" + pvt.getSecondaryDiagLoc().getTranslation().getValue() + ")") + "    ";
            }
            Label lbNdValue = new Label(labelText);
            lbNdValue.setWrapText(true);

            gpPvts.addRow(1, lbNd, lbNdValue);
//            gpPvts.add(lbNd, 0, 2);
//            gpPvts.add(lbNdValue, 1, 2);
        }

        if (pvt != null && pvt.getSecondarySecondDiagIcd() != null && !pvt.getSecondarySecondDiagIcd().isEmpty()) {
            Label lbNdSec = new Label("ND Sek: ");
            lbNdSec.getStyleClass().add("cpx-detail-label");
            final String labelText;
            if (pvt.getSecondarySecondDiagIcd() == null) {
                labelText = "";
            } else {
                labelText = pvt.getSecondarySecondDiagIcd().concat(pvt.getSecondarySecondDiagLoc() == null ? "" : " (" + pvt.getSecondarySecondDiagLoc().getTranslation().getValue() + ")");
            }
            Label lbNdSecValue = new Label(labelText);
            lbNdSecValue.setWrapText(true);

            gpPvts.addRow(1, lbNdSec, lbNdSecValue);
//            gpPvts.add(lbNdSec, 2, 2);
//            gpPvts.add(lbNdSecValue, 3, 2);
        }

        if (pvt != null && pvt.getOpsCode() != null && !pvt.getOpsCode().isEmpty()) {
            Label ops = new Label("OPS: ");
            ops.getStyleClass().add("cpx-detail-label");
            final String labelText;
            if (pvt.getOpsCode() == null) {
                labelText = "";
            } else {
                labelText = pvt.getOpsCode().concat(pvt.getOPLocalisation() == null ? "" : " (" + pvt.getOPLocalisation().getTranslation().getValue() + ")") + "    ";
            }
            Label opsValue = new Label(labelText);
            opsValue.setWrapText(true);

            gpPvts.addRow(2, ops, opsValue);
//            gpPvts.add(ops, 0, 3);
//            gpPvts.add(opsValue, 1, 3);
        }

        return gpPvts;
    }

}

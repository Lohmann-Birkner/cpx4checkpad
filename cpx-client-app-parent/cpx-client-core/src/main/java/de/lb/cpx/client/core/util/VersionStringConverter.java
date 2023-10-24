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
package de.lb.cpx.client.core.util;

import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.shared.lang.Lang;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

/**
 *
 * @author wilde
 */
public class VersionStringConverter extends StringConverter<TCaseDetails> {

    private final DisplayMode mode;

    public VersionStringConverter() {
        this(DisplayMode.SIMPLE);
    }

    public VersionStringConverter(DisplayMode pMode) {
        mode = pMode;
    }

    @Override
    public String toString(TCaseDetails item) {
        return convert(item, mode);
    }

    @Override
    public TCaseDetails fromString(String string) {
        return null;
    }

    public static String convert(TCaseDetails item, DisplayMode pMode) {
        if (item == null) {
            return "";
        }
        if (item.getCsdIsLocalFl()) {
            return "CP-Version " + item.getCsdVersion() + addInformation(item, pMode);
        } else {
            return "KIS-Version " + item.getCsdVersion() + addInformation(item, pMode);
        }
    }

    public static String convertSimple(TCaseDetails item) {
        return convert(item, DisplayMode.SIMPLE);
    }
    
    
    public static String convertSimpleWithRiskType(TCaseDetails item){
        return convertSimple(item) + " (" + (item.getCsdVersRiskTypeEn() == null?VersionRiskTypeEn.NOT_SET.getTranslation().getValue():item.getCsdVersRiskTypeEn().getTranslation().getValue()) + ")";
    }

    public BasicTooltip getParentTooltip(TCaseDetails item) {
        if (item != null && item.getCaseDetailsByCsdParentId() != null) {
            BasicTooltip tip = new BasicTooltip("", "");
            tip.setGraphic(getTooltipGraphic(item));
            tip.setFontSize(15);
            return tip;
        }
        return null;
    }

//    public String getTooltipText(TCaseDetails item) {
//        if (item != null && item.getCaseDetailsByCsdParentId() != null) {
//            String msg = "Parent: ";
//            if (item.getCaseDetailsByCsdParentId().getCsdIsLocalFl()) {
//                msg = msg.concat("CP");
//            } else {
//                msg = msg.concat("KIS");
//            }
//            if (item.getComment() != null) {
//                return msg + "-Version " + item.getCaseDetailsByCsdParentId().getCsdVersion() + "\n" + item.getComment().replace("////", "\n");
//            } else {
//                return msg + "-Version " + item.getCaseDetailsByCsdParentId().getCsdVersion();
//            }
//        }
//        return "";
//    }
    public String getTooltipText(TCaseDetails item) {
        StringBuilder builder = new StringBuilder();
        if (item != null && item.getCaseDetailsByCsdParentId() != null) {
            builder.append("Eltern-Version ");
            if (item.getCaseDetailsByCsdParentId().getCsdIsLocalFl()) {
                builder.append("CP");
            } else {
                builder.append("KIS");
            }
            if (item.getComment() != null) {
                String versionComment = "-Version " + item.getCaseDetailsByCsdParentId().getCsdVersion() + "\n" + item.getComment().replace("////", "\n");
                builder.append(versionComment);
            } else {
                String version = "-Version " + item.getCaseDetailsByCsdParentId().getCsdVersion();
                builder.append(version);
            }
        }
        return builder.toString();
    }
    
    public Node getTooltipGraphic(TCaseDetails pDetails){
        if(pDetails == null){
            return new Label("Keine Fallversion gefunden");
        }
        
        GridPane pane = new GridPane();
        pane.setVgap(5);
        pane.setHgap(5);
        int row = 0;
        if(pDetails.getCsdVersRiskTypeEn() != null){
            Label typeTitle = new Label("Ereignis");
            Label typeValue = new Label(pDetails.getCsdVersRiskTypeEn().getTranslation().getValue());
            pane.addRow(row, typeTitle,typeValue);
            row++;
        }
        if (pDetails.getCaseDetailsByCsdParentId() != null) {
            Label parentTitle = new Label("Eltern-Version");
            Label parentValue = new Label(convertSimple(pDetails.getCaseDetailsByCsdParentId()));
            pane.addRow(row, parentTitle, parentValue);
            row++;
        }
        if (pDetails.getComment() != null) {
            Label comment = new Label(pDetails.getComment().replace("////", "\n"));
            GridPane.setColumnSpan(comment, GridPane.REMAINING);
            pane.addRow(row, comment);
        }
        if(pane.getChildren().isEmpty()){
            return null;
        }
        return pane;
    }
    private static String addInformation(TCaseDetails item, DisplayMode pMode) {
        switch (pMode) {
            case ACTUAL:
                return item.getCsdIsActualFl() ? " (" + Lang.getActual() + ")" : "";
            case SIMPLE:
                return "";
            default:
                return "";
        }
    }

    public enum DisplayMode {
        SIMPLE, ACTUAL;
    }
}

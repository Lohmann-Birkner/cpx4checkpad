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

import de.lb.cpx.client.core.model.catalog.CpxDrg;
import de.lb.cpx.client.core.model.catalog.CpxDrgCatalog;
import de.lb.cpx.client.core.model.catalog.CpxPepp;
import de.lb.cpx.client.core.model.catalog.CpxPeppCatalog;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.grouper.model.transfer.TransferDrg;
import de.lb.cpx.grouper.model.transfer.TransferGroupResult;
import de.lb.cpx.grouper.model.transfer.TransferPepp;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.PcclEn;
import de.lb.cpx.ruleviewer.analyser.attributes.AddAnalyserResultAttributes;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttribute;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserGroupAttribute;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserSingleAttribute;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import org.controlsfx.property.BeanProperty;

/**
 *
 * @author wilde
 */
public class AdditionalAnalyserResultSkin extends SkinBase<AdditionalAnalyserResult> {

    private static final Logger LOG = Logger.getLogger(AdditionalAnalyserResultSkin.class.getName());

    protected static final String NOT_AVAILABLE = "N/A";
    private final GridPane gridPane;

    public AdditionalAnalyserResultSkin(AdditionalAnalyserResult pSkinnable) {
        super(pSkinnable);
        gridPane = new GridPane();
        gridPane.setHgap(5.0);
        gridPane.setVgap(5.0);
        ScrollPane sp = new ScrollPane(gridPane);
        sp.setFitToHeight(true);
        getChildren().add(sp);

        pSkinnable.additionalResultProperty().addListener(new ChangeListener<TransferRuleAnalyseResult>() {
            @Override
            public void changed(ObservableValue<? extends TransferRuleAnalyseResult> observable, TransferRuleAnalyseResult oldValue, TransferRuleAnalyseResult newValue) {
                setUpContent(newValue);
            }
        });
        setUpContent(pSkinnable.getAdditionalResult());
    }

    private void setUpContent(TransferRuleAnalyseResult pResult) {
        gridPane.getChildren().clear();
        List<AdditionalAnalyserResultItem> items = new ArrayList<>();
        for (AnalyserAttribute att : AddAnalyserResultAttributes.instance().getAttributes()) {
            try {
                items.add(new AdditionalAnalyserResultItem(att.getDisplayName(), att, pResult));
            } catch (IllegalArgumentException ex) {
                LOG.info("bean not found");
                LOG.log(Level.FINEST, "error occured in bean", ex);
                //ignore most likely to happen when pepp attribute is tried to read in drg result or vice versa
            }
        }

        Iterator<AdditionalAnalyserResultItem> it = items.iterator();
        int row = 0;
        int column = 0;
        while (it.hasNext()) {
            AdditionalAnalyserResultItem item = it.next();
            gridPane.add(item.getTitleLabel(), column, row);
            gridPane.add(item.getTextLabel(), column + 1, row);
            column++;
            column++;
            if ((column / 2) >= getSkinnable().getColumnCount()) {
                row++;
                column = 0;
            }
        }

    }

    public Object getBean(AnalyserAttribute pAtt, TransferRuleAnalyseResult pResult) {
        if (pResult == null) {
            return null;
        }
        if (pAtt.getBeanClass() == TransferDrg.class) {
            if (!(pResult.getGroupResult() instanceof TransferDrg)) {
                return null;
            }
            return pResult.getGroupResult();
        }
        if (pAtt.getBeanClass() == TransferPepp.class) {
            if (!(pResult.getGroupResult() instanceof TransferPepp)) {
                return null;
            }
            return pResult.getGroupResult();
        }
        if (pAtt.getBeanClass() == TransferGroupResult.class) {
            return pResult.getGroupResult();
        }
        if (AddAnalyserResultAttributes.KEY_SUGG_DRG.equals(pAtt.getKey())) {
            if (!(pResult.getGroupResult() instanceof TransferDrg)) {
                return null;
            }
        }
        if (AddAnalyserResultAttributes.KEY_SUGG_PEPP.equals(pAtt.getKey())) {
            if (!(pResult.getGroupResult() instanceof TransferDrg)) {
                return null;
            }
        }
        return pResult;
    }

    private class AdditionalAnalyserResultItem {

        private final Label lblTitle;
        private final Label lblText;

        public AdditionalAnalyserResultItem(String pTitle, AnalyserAttribute pAttribute, TransferRuleAnalyseResult pResult) {
            lblTitle = new Label(pTitle + ":");
//            lblTitle.getStyleClass().add("cpx-detail-label");
            lblText = new Label();
            if (pResult == null) {
                lblText.setText(NOT_AVAILABLE);
                return;
            }
            if (pAttribute instanceof AnalyserSingleAttribute) {

                setLabelTextForAttribute(lblText, (AnalyserSingleAttribute) pAttribute, pResult);

            } else if (pAttribute instanceof AnalyserGroupAttribute) {
                Object bean = getBean(pAttribute, pResult);
                if (bean == null) {
                    if (getSkinnable().getAdditionalResult().getCaseTypeEn() != null) {
                        throw new IllegalArgumentException("bean type could not be found");
                    }
                }
                List<AnalyserSingleAttribute> singleAttributes = new ArrayList<>(((AnalyserGroupAttribute) pAttribute).getAttributes());
                AnalyserSingleAttribute first = singleAttributes.get(0);
                singleAttributes.remove(first);
                lblText.setText(getValue(first));

                if (!singleAttributes.isEmpty()) {
                    lblText.setText(lblText.getText().concat(" ("));
                    lblText.setText(lblText.getText().concat(singleAttributes.stream().map((t) -> {
                        PropertyDescriptor pd = getPropertyDescriptor(t);
                        if (pd == null) {
                            return NOT_AVAILABLE;
                        }
                        return getValue(t);//new BeanProperty(getBean(t, pResult), pd));
                    }).collect(Collectors.joining(", "))));
                    lblText.setText(lblText.getText().concat(")"));
                }
            }

            lblText.setTooltip(getTooltipForAttribute(pAttribute));
        }

        public Label getTitleLabel() {
            return lblTitle;
        }

        public Label getTextLabel() {
            return lblText;
        }

//        private String getValue(AnalyserAttribute pAttribute) {
//            if (pAttribute instanceof AnalyserSingleAttribute) {
//                return getValue((AnalyserSingleAttribute) pAttribute);
//            }
//            return null;
//        }
        private String getValue(AnalyserSingleAttribute pAttribute) {
            try {
                BeanProperty beanProp = new BeanProperty(getBean(pAttribute, getSkinnable().getAdditionalResult()), pAttribute.getPropertyDescriptor());
                Object value = beanProp.getValue();
                if (AddAnalyserResultAttributes.KEY_DELTA_CW.equals(pAttribute.getKey())
                        || AddAnalyserResultAttributes.KEY_CW_EFF.equals(pAttribute.getKey())
                        || AddAnalyserResultAttributes.KEY_CW_KATALOG.equals(pAttribute.getKey())) {
                    return Lang.toDecimal((Double) value, getCwPrecision());
                }
                if (AddAnalyserResultAttributes.KEY_AVERAGE_LOS.equals(pAttribute.getKey())) {
                    return Lang.toDecimal((Double) value);
                }
                if (AddAnalyserResultAttributes.KEY_BASERATE.equals(pAttribute.getKey())) {
                    return Lang.toDecimal((Double) value, 2);
                }
                return value != null ? String.valueOf(value) : NOT_AVAILABLE;
            } catch (Exception ex) {
                return NOT_AVAILABLE;
            }
        }
//        private String getValue(BeanProperty pProperty) {
//            if (pProperty.getValue() == null) {
//                return NOT_AVAILABLE;
//            }
//            Object value = pProperty.getValue();
//            return String.valueOf(value);
//        }

        private Integer getCwPrecision() {
            TransferRuleAnalyseResult result = getSkinnable().getAdditionalResult();
            if (result == null || result.getCaseTypeEn() == null) {
                return 3;
            }
            switch (result.getCaseTypeEn()) {
                case DRG:
                    return 3;
                case PEPP:
                    return 4;
                default:
                    return 3;
            }
        }

        private PropertyDescriptor getPropertyDescriptor(AnalyserSingleAttribute pAttribute) {
            return pAttribute.getPropertyDescriptor();
        }

        private Tooltip getTooltipForAttribute(AnalyserAttribute pAttribute) {
            pAttribute = Objects.requireNonNull(pAttribute, "Attribute can not be null");
            String key = Objects.requireNonNull(pAttribute.getKey(), "AttributeKey can not be null");
            if (key.equals(AddAnalyserResultAttributes.KEY_DRG_GROUP_ATT)) {
                if (!(pAttribute instanceof AnalyserGroupAttribute)) {
                    return null;
                }
                return getDrgTooltip(((AnalyserGroupAttribute) pAttribute).getAttribute(AddAnalyserResultAttributes.KEY_DRG));
            }
            if (key.equals(AddAnalyserResultAttributes.KEY_PEPP) && CaseTypeEn.PEPP.equals(getSkinnable().getAdditionalResult().getCaseTypeEn())) {
                return getPeppTooltip((AnalyserSingleAttribute) pAttribute);
            }
            if (key.equals(AddAnalyserResultAttributes.KEY_DRG) && CaseTypeEn.DRG.equals(getSkinnable().getAdditionalResult().getCaseTypeEn())) {
                return getDrgTooltip((AnalyserSingleAttribute) pAttribute);
            }
            if (key.equals(AddAnalyserResultAttributes.KEY_SK) || key.equals(AddAnalyserResultAttributes.KEY_MDC)) {
                return getMdcSkTooltip((AnalyserSingleAttribute) pAttribute);
            }
            if (key.equals(AddAnalyserResultAttributes.KEY_DRG_PARTITION)) {
                return getDrgPartitionTooltip((AnalyserSingleAttribute) pAttribute);
            }
            if (key.equals(AddAnalyserResultAttributes.KEY_PCCL)) {
                return getPcclTooltip((AnalyserSingleAttribute) pAttribute);
            }
            if (key.equals(AddAnalyserResultAttributes.KEY_SUGG_DRG)) {
                if (!(pAttribute instanceof AnalyserGroupAttribute)) {
                    return null;
                }
                return getSimpleDrgTooltip(((AnalyserGroupAttribute) pAttribute).getAttribute(AddAnalyserResultAttributes.KEY_SUGG_SIM_CODE));
            }
            if (key.equals(AddAnalyserResultAttributes.KEY_SUGG_PEPP)) {
                if (!(pAttribute instanceof AnalyserGroupAttribute)) {
                    return null;
                }
                return getSimplePeppTooltip(((AnalyserGroupAttribute) pAttribute).getAttribute(AddAnalyserResultAttributes.KEY_SUGG_SIM_CODE));
            }
            if (key.equals(AddAnalyserResultAttributes.KEY_CW_PEPP)) {
                if (!(pAttribute instanceof AnalyserGroupAttribute)) {
                    return null;
                }
                return getPeppCwTooltip(((AnalyserGroupAttribute) pAttribute).getAttribute(AddAnalyserResultAttributes.KEY_CW_EFF));
            }
            return null;
        }

        private Tooltip getDrgTooltip(AnalyserSingleAttribute pAttribute) {
            String value = getValue(pAttribute);
            if (value == null) {
                return null;
            }
            BasicTooltip tip = new AddAnalyserTooltip(value, getDrgDescription(value), getGrouperErrorText());
            return tip;
        }

        private Tooltip getSimpleDrgTooltip(AnalyserSingleAttribute pAttribute) {
            String value = getValue(pAttribute);
            if (value == null) {
                return null;
            }
            BasicTooltip tip = new AddAnalyserTooltip(value, getDrgDescription(value));
            return tip;
        }

        private Tooltip getSimplePeppTooltip(AnalyserSingleAttribute pAttribute) {
            String value = getValue(pAttribute);
            if (value == null) {
                return null;
            }
            BasicTooltip tip = new AddAnalyserTooltip(value, getPeppDescription(value));
            return tip;
        }

        private Tooltip getMdcSkTooltip(AnalyserSingleAttribute pAttribute) {
            String value = getValue(pAttribute);
            GrouperMdcOrSkEn mdcOrSk = GrouperMdcOrSkEn.getValue2Id(value);
            BasicTooltip tip = new AddAnalyserTooltip(value, mdcOrSk != null ? mdcOrSk.getTranslation().getValue() : "Nicht verfügbar");
            return tip;
        }

        private Tooltip getDrgPartitionTooltip(AnalyserSingleAttribute pAttribute) {
            String value = getValue(pAttribute);
            DrgPartitionEn drgPart = DrgPartitionEn.getValue2name(value);
            BasicTooltip tip = new AddAnalyserTooltip(value, drgPart != null ? drgPart.getTranslation().getValue() : "Nicht verfügbar");
            return tip;
        }

        private Tooltip getPcclTooltip(AnalyserSingleAttribute pAttribute) {
            String value = getValue(pAttribute);
            PcclEn pcclEn = PcclEn.getValue2Id(value);
            BasicTooltip tip = new AddAnalyserTooltip(value, pcclEn != null ? pcclEn.getTranslation().getValue() : "Nicht verfügbar");
            return tip;
        }

        private Tooltip getPeppCwTooltip(AnalyserSingleAttribute pAttribute) {
            if (!(getSkinnable().getAdditionalResult().getGroupResult() instanceof TransferPepp)) {
                return null;
            }
            TransferPepp pepp = (TransferPepp) getSkinnable().getAdditionalResult().getGroupResult();
            if (pepp == null) {
                return null;
            }
            String title = pepp.getHasGrades() ? Lang.getPayGrade() : Lang.getPayClass();
            BasicTooltip tip = new AddAnalyserTooltip(title, getPeppGradesDescription(pepp));
            return tip;
        }

        private Tooltip getPeppTooltip(AnalyserSingleAttribute pAttribute) {
            String value = getValue(pAttribute);
            if (value == null) {
                return null;
            }
            BasicTooltip tip = new AddAnalyserTooltip(value, getPeppDescription(value), getGrouperErrorText());
            return tip;
        }

        private String getGrouperErrorText() {
            TransferRuleAnalyseResult result = getSkinnable().getAdditionalResult();
            List<CaseValidationGroupErrList> errorList = result.getCaseValidationGroupErrList();
            if (errorList == null) {
                return null;
            }
            if (errorList.isEmpty()) {
                return null;
            }
            String header = "(";
            String end = ")";
            String errors = getDrgGrouperErrors(errorList);
            String warnings = getDrgGrouperWarnings(errorList);
            String divider = "";
            if (!errors.isEmpty() && !warnings.isEmpty()) {
                divider = "\n";
            }
            return header + errors + divider + warnings + end;
        }

        private String getDrgGrouperErrors(List<CaseValidationGroupErrList> pErrorList) {
            if (pErrorList == null) {
                return "";
            }
            if (pErrorList.isEmpty()) {
                return "";
            }
            if (pErrorList.stream().noneMatch((t) -> {
                return t.isSevere();
            })) {
                return "";
            }
            String header = "Fehler:\n";
            String message = pErrorList.stream().filter((t) -> {
                return t.isSevere();
            }).map((t) -> {
                return t.getTranslation().getValue();
            }).collect(Collectors.joining(",\n"));
            return header + message;
        }

        private String getDrgGrouperWarnings(List<CaseValidationGroupErrList> pErrorList) {
            if (pErrorList == null) {
                return "";
            }
            if (pErrorList.isEmpty()) {
                return "";
            }
            if (pErrorList.stream().noneMatch((t) -> {
                return !t.isSevere();
            })) {
                return "";
            }
            String header = "Warnung:\n";
            String message = pErrorList.stream().filter((t) -> {
                return !t.isSevere();
            }).map((t) -> {
                return t.getTranslation().getValue();
            }).collect(Collectors.joining(",\n"));
            return header + message;
        }

        private String getDrgDescription(String value) {
            if (NOT_AVAILABLE.equals(value)) {
                return "Nicht verfügbar";
            }
            CpxDrg catalog = CpxDrgCatalog.instance().getByCode(value, getSkinnable().getAdditionalResult().getCaseYear());
            return catalog.getDrgDescription();
        }

        private String getPeppDescription(String value) {
            if (NOT_AVAILABLE.equals(value)) {
                return "Nicht verfügbar";
            }
            CpxPepp catalog = CpxPeppCatalog.instance().getByCode(value, getSkinnable().getAdditionalResult().getCaseYear());
            return catalog.getPeppDescription();
        }

        private void setLabelTextForAttribute(Label pLabel, AnalyserSingleAttribute pAttribute, TransferRuleAnalyseResult pResult) {
            Object bean = getBean(pAttribute, getSkinnable().getAdditionalResult());
            if (bean == null) {
                if (getSkinnable().getAdditionalResult().getCaseTypeEn() != null) {
                    throw new IllegalArgumentException("bean type could not be found");
                }
            }
            PropertyDescriptor propDesc = getPropertyDescriptor(pAttribute);
            if (propDesc == null) {
                pLabel.setText(NOT_AVAILABLE);
                return;
            }
            String value = getValue(pAttribute);
            setLabelTextInLabel(pLabel, value, pAttribute);
        }

        private void setLabelTextInLabel(Label pLabel, String value, AnalyserSingleAttribute pAttribute) {
            pLabel.setText(value);
            if (AddAnalyserResultAttributes.KEY_DELTA_CW.equals(pAttribute.getKey())) {
                Double num = getDouble(value);
                if (num > 0) {
                    pLabel.setStyle("-fx-text-fill:green;");
                }
                if (num < 0) {
                    pLabel.setStyle("-fx-text-fill:red;");
                }
            }
        }

        private Double getDouble(String pValue) {
            try {
                pValue = pValue.replace(",", ".");
                return Double.parseDouble(pValue);
            } catch (NumberFormatException ex) {
                return 0.0;
            }
        }

        private String getPeppGradesDescription(TransferPepp pepp) {
            if (pepp == null) {
                return NOT_AVAILABLE;
            }
            if (pepp.getGrades().isEmpty()) {
                return NOT_AVAILABLE;
            }
            return pepp.getGrades().stream().map((t) -> {
                return (pepp.getHasGrades() ? Lang.getPayGrade() : Lang.getPayClass()) + ": " + t.getGrade() + ", " + Lang.getRevenueMathPeppCwday(t.getCw());
            }).collect(Collectors.joining("\n"));
        }

        private class AddAnalyserTooltip extends BasicTooltip {

            public AddAnalyserTooltip(String pTitle, String pContent, String pDetails) {
                super(pTitle, pContent, pDetails);
                setPrefWidth(400);
                setWrapText(true);
            }

            public AddAnalyserTooltip(String pTitle, String pContent) {
                super(pTitle, pContent);
                setPrefWidth(400);
                setWrapText(true);
            }
        }
    }
}

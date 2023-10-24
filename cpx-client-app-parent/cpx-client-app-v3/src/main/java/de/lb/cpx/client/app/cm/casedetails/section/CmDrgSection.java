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
 *    2016  adameck - initial API and implementation and/or initial documentation
 *    2017  wilde - add javadoc
 */
package de.lb.cpx.client.app.cm.casedetails.section;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.catalog.CpxDrgCatalog;
import de.lb.cpx.client.core.model.catalog.CpxIcd;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.CpxPeppCatalog;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Section for details in workinglist for Drg Data refactor? due to section
 * title?
 *
 * @author adameck
 */
public class CmDrgSection extends SectionTitle {

    private static final Logger LOG = Logger.getLogger(CmDrgSection.class.getName());

    private final TCaseDetails caseVersion;
    private Label labelDrgCode;
    private Label labelDrgCodeText;
//    private Label labelSuppFee;
    private Label labelMainDiagnosis;
    private final TGroupingResults drgResult;
    private final EjbProxy<SingleCaseGroupingEJBRemote> groupingBean;
    private GridPane gpInfos;

    /**
     * creates new instance
     *
     * @param pCaseVersion case version
     * @param pGroupingResult grouping result for case version
     */
    public CmDrgSection(TCaseDetails pCaseVersion, TGroupingResults pGroupingResult) {
        super(Lang.getDetailCaseData());
        caseVersion = pCaseVersion;
        drgResult = pGroupingResult;
        groupingBean = Session.instance().getEjbConnector().connectSingleCaseGroupingBean();
        setValues();
    }

    @Override
    public void setMenu() {
        //add menu here
    }

    @Override
    protected Parent createContent() {
        //init gridpane and set layout
        gpInfos = new GridPane();
        VBox.setMargin(gpInfos, new Insets(12, 0, 0, 0));
        gpInfos.getStyleClass().add("default-grid");
        gpInfos.getStyleClass().add("default-distances");
        gpInfos.setVgap(12.0);
        gpInfos.setHgap(12.0);
        gpInfos.setPrefHeight(GridPane.USE_COMPUTED_SIZE);

        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setMinWidth(120);
        gpInfos.getColumnConstraints().add(columnConstraint);

        //create description labels with margin and styleclass
        labelDrgCodeText = createLabelForDescription("");
        GridPane.setValignment(labelDrgCodeText, VPos.TOP);

        Label labelMainDiagnosisText = createLabelForDescription(Lang.getICDCode());
        GridPane.setValignment(labelMainDiagnosisText, VPos.TOP);

//        Label labelSuppFeeText = createLabelForDescription(Lang.getSupplementaryValue());
        //content labels
        labelDrgCode = new Label();
        labelDrgCode.setWrapText(true);
        labelDrgCode.setAlignment(Pos.TOP_LEFT);
        GridPane.setValignment(labelDrgCode, VPos.TOP);

        labelMainDiagnosis = new Label();
        labelMainDiagnosis.setWrapText(true);
        GridPane.setValignment(labelMainDiagnosis, VPos.TOP);

//        labelSuppFee = new Label();
        //set position in gridpane
        gpInfos.add(labelDrgCodeText, 0, 0);
        gpInfos.add(labelDrgCode, 1, 0);

        gpInfos.add(labelMainDiagnosisText, 0, 1);
        gpInfos.add(labelMainDiagnosis, 1, 1);

//        gpInfos.add(labelSuppFeeText, 0, 2);
//        gpInfos.add(labelSuppFee, 1, 2);
        return gpInfos;
    }

    @Override
    public Parent getDetailContent() {
        return new HBox();
    }
    //set values from entity in ui
    private void setValues() {
        if (caseVersion == null) {
            gpInfos.getChildren().clear();
            boolean isShowLocal = Session.instance().isCaseLocal();
            showErrorMessageNode(CpxErrorTypeEn.ERROR, "Es wurde keine aktuelle " +(isShowLocal?"lokale":"externe") + " Version für den Fall gefunden");
            return;
        }
        int admYear = Lang.toYear(caseVersion.getCsdAdmissionDate());
        if (drgResult == null) {
            gpInfos.getChildren().remove(labelDrgCodeText);
            gpInfos.getChildren().remove(labelDrgCode);
            // hier "Fall wurde noch nicht gegroupt"
            GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
            if(caseVersion.getCsdDischargeDate() == null){
                showErrorMessageNode(CpxErrorTypeEn.WARNING, Lang.getMessageCaseHasNoDischargeDate());
            }else if(admYear < GDRGModel.getMinModelYear() && grouper.equals(GDRGModel.AUTOMATIC)){
                showErrorMessageNode(CpxErrorTypeEn.WARNING, Lang.getMessageCaseDateNotValid(String.valueOf(admYear) ));  
            }else if(grouper.equals(GDRGModel.GDRG20192022) && caseVersion.getHospitalCase().getCsCaseTypeEn().equals(CaseTypeEn.PEPP)){
                 showErrorMessageNode(CpxErrorTypeEn.WARNING, "Der Grouper für das Groupermodel \"" + grouper.toString() + "\" ist für PEPP Fälle nicht definiert");

            }else {
                showErrorMessageNode(CpxErrorTypeEn.WARNING, Lang.getMessageCaseNotGrouped("\"" + grouper.toString() + "\""));
            }
        } else {
            switch (drgResult.getGrpresType()) {
                case DRG:
                    labelDrgCodeText.setText(Lang.getDRGObj().getAbbreviation());
                    labelDrgCode.setText(getDrgDescription(drgResult.getGrpresCode(), admYear));
                    setDrgFeeValues();
                    break;
                case PEPP:
                    labelDrgCodeText.setText(Lang.getPEPPObj().getAbbreviation());
                    labelDrgCode.setText(getPeppDescription(drgResult.getGrpresCode(), admYear));
                    setPeppFeeValues();
                    break;
                default:
                    LOG.warning("unkown type: " + drgResult.getGrpresType().name());
            }
        }
        //set values according to results
        labelMainDiagnosis.setText(getMdDescription(caseVersion.getHdIcdCode(), admYear));
    }
    
    //get supplementary fee from server with case version id
    private void setDrgFeeValues() {
        Label labelSuppFeeText = createLabelForDescription(Lang.getSupplementaryValue());
        Label labelSuppFee = new Label(getSfValue());
        gpInfos.add(labelSuppFeeText, 0, 2);
        gpInfos.add(labelSuppFee, 1, 2);
    }

    private void setPeppFeeValues() {
        Label labelSpText = createLabelForDescription(Lang.getSupplFeeValuePEPP());
        Label labelSp = new Label(getSpValue());
        gpInfos.add(labelSpText, 0, 2);
        gpInfos.add(labelSp, 1, 2);
        Label labelDfText = createLabelForDescription(Lang.getDailyFeeValue());
        Label labelDf = new Label(getDfValue());
        gpInfos.add(labelDfText, 0, 3);
        gpInfos.add(labelDf, 1, 3);
    }

    private String getSfValue() {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        Double suppFee = groupingBean.get().getSupplFeeValue(grouper, caseVersion.getId(), SupplFeeTypeEn.ZE);
        return Lang.toDecimal(suppFee, 2) + " " + Lang.getCurrencySymbol();
    }

    private String getSpValue() {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        Double suppFee = groupingBean.get().getSupplFeeValue(grouper, caseVersion.getId(), SupplFeeTypeEn.ZP);
        return Lang.toDecimal(suppFee, 2) + " " + Lang.getCurrencySymbol();
    }

    private String getDfValue() {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        Double suppFee = groupingBean.get().getSupplFeeValue(grouper, caseVersion.getId(), SupplFeeTypeEn.ET);
        return Lang.toDecimal(suppFee, 2) + " " + Lang.getCurrencySymbol();
    }
    //get md description with icd entity and admission date to fetch from catalog description

    private String getMdDescription(String pIcd, int pAdmYear) {
        if (pIcd == null) {
            return "";
        }
        //get catalog data
        CpxIcd icdCatalogData = CpxIcdCatalog.instance().getByCode(pIcd, "de", pAdmYear);
        return pIcd + ": " + icdCatalogData.getIcdDescription();
    }
    //build pepp description with catalog

    private String getPeppDescription(String pGrpresCode, int pYear) {
        CpxPeppCatalog peppCatalog = CpxPeppCatalog.instance();
        return peppCatalog.getPeppDescription(pGrpresCode, "de", pYear);
    }
    //build drg description with catalog

    private String getDrgDescription(String pGrpresCode, int pYear) {
        CpxDrgCatalog drgCatalog = CpxDrgCatalog.instance();
        return drgCatalog.getDrgDescription(pGrpresCode, "de", pYear);
    }
    //create label for description

    private Label createLabelForDescription(String pText) {
        Label label = new Label(pText);
        label.getStyleClass().add("cpx-detail-label");
        GridPane.setMargin(label, new Insets(0, 0, 0, 8));
        return label;
    }
}

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

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Section for detail in workinglist, shows general case information such as:
 * admDate,CaseNumber,count of secondary Diagnosis, count of Procedures etc.
 * Maybe refactor to remove section title, detail is not neccessary
 *
 * @author adameck
 */
public class CmCaseGeneralSection extends SectionTitle {

    private static final Logger LOG = Logger.getLogger(CmCaseGeneralSection.class.getName());

    private final TCase hCase;
    private final TCaseDetails caseVersion;
    private Label labelCaseSolved;
    private Label labelAdmDate;
    private Label labelAdmReason;
    private Label labelDisDate;
    private Label labelDisReason;

    /**
     * creates new instance for case shows data according to case current local
     * or current extern version of the case selected by isCaseLocal() in the
     * current Sesstion Object
     *
     * @param pCase case to show data
     */
    public CmCaseGeneralSection(TCase pCase) {
        super(Lang.getCommonCaseData());
        hCase = pCase;
        //set used case version according to selected user input in settins
        if (hCase == null) {
            LOG.log(Level.WARNING, "hCase is null!");
            caseVersion = null;
        } else {
            caseVersion = Session.instance().isCaseLocal() ? hCase.getCurrentLocal() : hCase.getCurrentExtern();
        }
        setValues();
    }

    @Override
    public void setMenu() {
        //set Menu here
    }

    @Override
    protected Parent createContent() {
        VBox detailContent = new VBox();
        //init gridpane to store values
        GridPane gpInfos = new GridPane();
        //set layout
        VBox.setMargin(gpInfos, new Insets(12, 0, 0, 0));
        gpInfos.getStyleClass().add("default-grid");
        gpInfos.getStyleClass().add("default-distances");
        gpInfos.setVgap(12.0);
        gpInfos.setHgap(12.0);
        gpInfos.setPrefHeight(GridPane.USE_COMPUTED_SIZE);

        //init label with style classes and margin
        //lables used display localized discriptions
        Label labelCaseSolvedText = createDiscriptionLabel(Lang.getCaseResolved());
        Label labelAdmDateText = createDiscriptionLabel(Lang.getAdmissionDate());
        Label labelAdmReasonText = createDiscriptionLabel(Lang.getAdmissionReason());
        Label labelDisDateText = createDiscriptionLabel(Lang.getDischargeDate());
        Label labelDisReasonText = createDiscriptionLabel(Lang.getDischargeReason());

        //labels for showing values
        labelCaseSolved = new Label();
        labelAdmDate = new Label();
        labelAdmReason = new Label();
        labelDisDate = new Label();
        labelDisReason = new Label();

        //place labels in girdpane
        gpInfos.add(labelCaseSolvedText, 0, 0);
        gpInfos.add(labelCaseSolved, 1, 0);
        gpInfos.add(labelAdmDateText, 0, 1);
        gpInfos.add(labelAdmDate, 1, 1);
        gpInfos.add(labelDisDateText, 0, 2);
        gpInfos.add(labelDisDate, 1, 2);
        gpInfos.add(labelAdmReasonText, 0, 3);
        gpInfos.add(labelAdmReason, 1, 3);
        gpInfos.add(labelDisReasonText, 0, 4);
        gpInfos.add(labelDisReason, 1, 4);

        detailContent.getChildren().addAll(gpInfos);

        return detailContent;
    }

    @Override
    public Parent getDetailContent() {
        //return empty detail content, not needed
        HBox contentBox = new HBox();
        return contentBox;
    }
    //set values from case version of the case
    //uses either current local or current extern version of the case

    private void setValues() {
        if(hCase == null || caseVersion == null){
            boolean isShowLocal = Session.instance().isCaseLocal();
            showErrorMessageNode(CpxErrorTypeEn.ERROR, "Es wurde keine aktuelle " +(isShowLocal?"lokale":"externe") + " Version für den Fall gefunden");
            return;
        }
        //set values to texts
        final String closedText = hCase.getCsStatusEn().isClosed() ? Lang.getConfirmationYes() : Lang.getConfirmationNo();
        final String admissionDateText = caseVersion.getCsdAdmissionDate() != null ? Lang.toDate(caseVersion.getCsdAdmissionDate()) : "";
        final String admissionReason12Text = caseVersion.getCsdAdmReason12En() != null ? caseVersion.getCsdAdmReason12En().toString() : "";
        final String dischargeDateText = caseVersion.getCsdDischargeDate() != null ? Lang.toDate(caseVersion.getCsdDischargeDate()) : "";
        final String dischargeReason12Text = caseVersion.getCsdDisReason12En() != null ? caseVersion.getCsdDisReason12En().toString() : "";

        labelCaseSolved.setText(closedText);
        labelAdmDate.setText(admissionDateText);
        labelAdmReason.setText(admissionReason12Text);
        labelDisDate.setText(dischargeDateText);
        labelDisReason.setText(dischargeReason12Text);
    }

    /**
     * @param pText text to show in label
     * @return new label with styleclass cpx-detail-label
     */
    private Label createDiscriptionLabel(String pText) {
        Label label = new Label(pText);
        label.getStyleClass().add("cpx-detail-label");
        GridPane.setMargin(label, new Insets(0, 0, 0, 8));
        return label;
    }

}

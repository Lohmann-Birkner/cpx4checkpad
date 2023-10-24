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
 *    2018  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.addcasewizard.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.HashSet;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 *
 * @author Shahin
 */
public final class AddVersionDialog extends FormularDialog<TCaseDetails> {

    private final AddCaseServiceFacade serviceFacade;
    private final Label labelInfo;
    private LabeledTextArea taComment = new LabeledTextArea();

    public AddVersionDialog(AddCaseServiceFacade pServiceFacade) {
        super(MainApp.getStage().getOwner(), Modality.APPLICATION_MODAL, Lang.getCaseStatusNewVersion());
        this.serviceFacade = pServiceFacade;

        labelInfo = new Label(Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber() + "\n" + Lang.getVersionAddConfirm());
        taComment = new LabeledTextArea(Lang.getVersioncontrollVersionComment(), LabeledTextArea.CASE_VERSION_SIZE);
        addControls(labelInfo, taComment);

    }

    @Override
    public TCaseDetails onSave() {
        TCase currentCase = serviceFacade.getCurrentCase();
        TCaseDetails externDetails = new TCaseDetails();
        if (currentCase == null) {
            currentCase = new TCase();
            currentCase.setCsStatusEn(CaseStatusEn.NEW);
            currentCase.setPatient(serviceFacade.getCurrentPatient());

            currentCase.setCurrentExtern(externDetails);
            externDetails.setHospitalCase(currentCase);
            currentCase.setCaseLabor(new HashSet<TLab>());
        } else {
            externDetails.setCsdVersion(externDetails.getCsdVersion() + 1);
        }
        serviceFacade.setCurrentCase(currentCase);

        return externDetails;
    }

    public String getCommentVersion() {
        return trimToEmpty(taComment.getText());
    }

    void setPatientInfo(String patInfo) {
        labelInfo.setText(trimToEmpty(labelInfo.getText()) + "\n" + patInfo);
    }

}

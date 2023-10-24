/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.wm.util.auditquota.MdkAuditQuotaResult;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;

/**
 *
 * @author gerschmann
 */
//public class QuotaInfoDialog extends TitledDialog{
public class QuotaInfoDialog extends AlertDialog {

    private Label firstLine;
    private Label lblCaseCountValue;
    private Label lblActualComplaintsValue;
    private Label lblGivenComplaintsValue;
    private Label lblGivenQuotaValue;

    private QuotaInfoDialog(boolean isInfo) {
        super(isInfo?AlertType.INFORMATION:AlertType.WARNING, isInfo?Lang.getInformation():Lang.getWarning(),"","", ButtonType.OK);
        initOwner(BasicMainApp.getStage());
        initModality(Modality.APPLICATION_MODAL);
        Parent layout = getLayout();
        if (layout == null) {
            return;
        }
        getDialogPane().setContent(layout);
        firstLine = (Label) layout.lookup("#lblMessageLine1Id");
        Label secondLine = (Label) layout.lookup("#lblMessageLine2Id");
        lblCaseCountValue = (Label) layout.lookup("#lblCaseCountValueId");
        lblActualComplaintsValue = (Label) layout.lookup("#lblActualComplaintsValueId");
        lblGivenComplaintsValue = (Label) layout.lookup("#lblGivenComplaintsValueId");
        lblGivenQuotaValue = (Label) layout.lookup("#lblGivenQuotaValueId");
        if (isInfo) {
            secondLine.setText("");
        } else {
            secondLine.setText("Die Anfrage wird mit \"Überschreitung Prüfquote\" markiert.");
        }
    }

    public QuotaInfoDialog(boolean isInfo, MdkAuditQuotaResult result) {
        this(isInfo);
        setData(result, isInfo);
    }

    private Parent getLayout() {
        try {
            return FXMLLoader.load(getClass().getResource("/fxml/QuotaDetailsFXML.fxml"));
        } catch (IOException ex) {
            MainApp.showErrorMessageDialog(ex);
        }
        return null;
    }

    private void setData(MdkAuditQuotaResult result, boolean isInfo) {
        if (isInfo) {
            firstLine.setText("Die Prüfquote für die Kasse " + result.insuranceIdent + " für das Quartal "
                    + result.quarter + "/" + result.year.toString() + " wurde noch nicht erreicht.");
        } else {
            firstLine.setText("Die Prüfquote für die Kasse " + result.insuranceIdent + " für das Quartal "
                    + result.quarter + "/" + result.year.toString() + " ist bereits erreicht!");
        }

        lblCaseCountValue.setText(String.valueOf(result.caseCount));
        lblActualComplaintsValue.setText(String.valueOf(result.actualComplaints));
        lblGivenComplaintsValue.setText(String.valueOf(result.givenComplaints));
        lblGivenQuotaValue.setText(String.valueOf(result.givenQuota) + " %");

    }

}

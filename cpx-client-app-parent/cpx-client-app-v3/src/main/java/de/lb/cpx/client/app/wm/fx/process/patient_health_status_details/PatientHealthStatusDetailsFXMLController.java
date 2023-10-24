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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.patient_health_status_details;

import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.model.TPatient;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class for Patientdetails. ToDo: -Add Telefonnumbers to
 * Patientdata -Add Insuranceduration, maybe with two date fields -Add
 * InsuranceStatus,
 *
 * @author wilde
 */
public class PatientHealthStatusDetailsFXMLController extends Controller<CpxScene> {
    //    private String getText(String key){
//        return lang.get(key);
//    }

    @FXML
    private AnchorPane anchorPaneParent;

//    private final String INSURANCE_IDENT_NUMBER = "InsuranceNumber";
//    private final String INSURANCE_NAME ="NAME_";
//    private final String INSURANCE_CALL_NUMBER="CALL_NUMBER_";
//    private final String INSURANCE_STATUS="STATUS_";
//    private final String INSURANCE_FROM_TO="FROM-TO_";
//    private String INSURANCE_AREA_TITLE = "INSURANCE_DATA_";
    private TPatient actualPatient;
    private PatientHealthStatusVisualization phsViz;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

//    public void setPatient(TPatient patient) {
//        if(patient == null){
//            return;
//        }
//        actualPatient = patient;
//        refresh();
//    }
    public void setPatient(CaseServiceFacade facade) {

        actualPatient = facade.loadPatient();
        refresh();
    }

    public void setPatient(long id) {
        CaseServiceFacade facade = new CaseServiceFacade();
        actualPatient = facade.findPatient(id);
        refresh();
    }

    public TPatient getPatient() {
        return actualPatient;
    }

    @Override
    public void refresh() {
        setUpView(actualPatient);
    }

    private void setUpView(final TPatient pPatient) {
        phsViz = new PatientHealthStatusVisualization(pPatient);
        VBox vbox = phsViz.getFullscreenContent();
        //vbox.setStyle("-fx-background-color: yellow;");
        //vbox.setStyle("-fx-font-size: 16px;");
        //vbox.setPrefWidth(300d);
        AnchorPane.setTopAnchor(vbox, 15d);
        AnchorPane.setRightAnchor(vbox, 15d);
        AnchorPane.setBottomAnchor(vbox, 15d);
        AnchorPane.setLeftAnchor(vbox, 15d);
        anchorPaneParent.getChildren().clear();
        anchorPaneParent.getChildren().add(vbox);
    }

}

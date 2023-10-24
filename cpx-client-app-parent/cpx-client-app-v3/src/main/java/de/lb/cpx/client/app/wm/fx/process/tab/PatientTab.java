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
package de.lb.cpx.client.app.wm.fx.process.tab;

import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.patient_health_status_details.AcgVisualisationWeb;
import de.lb.cpx.client.app.wm.fx.process.patient_health_status_details.PatientHealthStatusDetailsFXMLController;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab.TabType;
import de.lb.cpx.client.core.model.fx.webview.LoadingWebView;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

/**
 * Implementation of a Patienttab, loads the Patient based on the Id ToDo: set
 * Title,Desc and image
 *
 * @author wilde
 */
public class PatientTab extends TwoLineTab {

    /**
     * Construct new PatientTab, initalize scene and controller and display
     * content
     *
     * @param patientId unique db access to be loaded
     * @param pPatientNumber patient number
     * @throws IOException IoException if Fxml file is not found
     */
    public PatientTab(long patientId,String pPatientNumber) throws IOException {
        super();
        setId(String.valueOf(patientId));
        setTitle(pPatientNumber);
        setDescription(Lang.getPatientData());
        setGlyph("\uf007");
//todo: check connection
//        String url = buildUrl(pPatientNumber);
//        if(checkConnection(url)){
//            LoadingWebView webView = new LoadingWebView(url);
//            setContent(webView);
//        }else{
//            setContent(new Pane());
//        }
         AcgVisualisationWeb webView = new AcgVisualisationWeb(pPatientNumber, 4, 2, 1.0); 
//         LoadingWebView webView = new LoadingWebView(pPatientNumber, 2, 4, 0.9);
        VBox vbox = webView.getContent();
        this.setContent(vbox);
        

    }

    @Override
    public TabType getTabType() {
        return TabType.PATIENT;
    }
//    private Image getImage(GenderEn patGenderEn) {
//        switch (patGenderEn) {
//            case M:
//                return new Image(getClass().getResourceAsStream("/img/male_test.jpg"));
//            case W:
//                return new Image(getClass().getResourceAsStream("/img/female_test.jpg"));
//            default:
//                return new Image(getClass().getResourceAsStream("/img/unknown_test.jpg"));
//        }
//    }
//
//    private boolean checkConnection(String url) throws IOException{
//            // We want to check the current URL
//        HttpURLConnection.setFollowRedirects(false);
//        URL u = new URL(url);
//        HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
//
//        // We don't need to get data
//        httpURLConnection.setRequestMethod("HEAD");
//        int responseCode = httpURLConnection.getResponseCode();
//
//           // We check 404 only
//           return responseCode != HttpURLConnection.HTTP_NOT_FOUND;    
//    }
}

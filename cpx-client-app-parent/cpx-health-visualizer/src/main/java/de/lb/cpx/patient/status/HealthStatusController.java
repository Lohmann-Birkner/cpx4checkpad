/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.patient.status;

import de.lb.cpx.patient.status.organ.AbstractHumanPartGraphic;
import de.lb.cpx.patient.status.organ.HumanPartGraphic;
import de.lb.cpx.shared.dto.acg.AcgPatientData;
import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class HealthStatusController implements Initializable {

    private static final Logger LOG = Logger.getLogger(HealthStatusController.class.getName());
    private double zoomFactor = 1.0d;

    @FXML
    private StackPane imagePane;
    @FXML
    private Button showBodyButton;

    private Map<Integer, HumanPartGraphic> humanPartGraphics = new LinkedHashMap<>();
    private AcgPatientData acgPatientData = null;
    @FXML
    private Button zoomOutBtn;
    @FXML
    private Button newDiseasesBtn;
    @FXML
    private ScrollPane scrollPane;
    private String gender = "";

    public StackPane getPane() {
        return imagePane;
    }

    /**
     * Organ information will be sorted by severity (descending)
     *
     * @param pAcgPatientData AcgPatientData
     */
    public void setAcgPatientData(final AcgPatientData pAcgPatientData) {
//        List<OrganInfo> list = Arrays.asList(pAcgPatientData);
//        list.sort(new Comparator<OrganInfo>() {
//            @Override
//            public int compare(final OrganInfo lhsOrganInfo, final OrganInfo rhsOrganInfo) {
//                return Integer.compare(rhsOrganInfo.getSeverity().getCcl(), lhsOrganInfo.getSeverity().getCcl());
//            }
//        });

        //OrganInfo[] src = pOrganInfos == null ? new OrganInfo[0] : pOrganInfos;
        //OrganInfo[] dest = new OrganInfo[src.length];
        //System.arraycopy(src, 0, dest, 0, src.length);
//        OrganInfo[] infos = new OrganInfo[list.size()];
//        list.toArray(infos);
        acgPatientData = pAcgPatientData;
    }

    public AcgPatientData getAcgPatientData() {
//        OrganInfo[] src = acgPatientData;
//        OrganInfo[] dest = new OrganInfo[src.length];
//        System.arraycopy(src, 0, dest, 0, src.length);        
//        return dest;
        return acgPatientData;
    }

    public List<IcdFarbeOrgan> getOrganInfo(final HumanPartGraphic pOrgan) {
        return AbstractHumanPartGraphic.getOrganInfo(acgPatientData, pOrgan);
//        List<IcdFarbeOrgan> tmp = new ArrayList<>();
//        for(IcdFarbeOrgan icdFarbeOrgan: getAcgPatientData().icd_code) {
//            if (pOrgan.getNumber() == icdFarbeOrgan.organNr) {
//                tmp.add(icdFarbeOrgan);
//            }
//        }
//        return tmp;
    }

    public Map<Integer, HumanPartGraphic> getHumanParts() {
        return new LinkedHashMap<>(humanPartGraphics);
    }

    public HumanPartGraphic getHumanPart(Class<? extends HumanPartGraphic> pClass) {
        Iterator<Map.Entry<Integer, HumanPartGraphic>> it = humanPartGraphics.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, HumanPartGraphic> partEntry = it.next();
            HumanPartGraphic part = partEntry.getValue();
            if (part != null && part.getClass().equals(pClass)) {
                return part;
            }
        }
        return null;
    }

    public synchronized void paintBody(final StackPane pTargetPane) {
        paintBody(pTargetPane, 1.0d);
    }

    public char getGender() {
        if (getAcgPatientData() == null) {
            LOG.log(Level.WARNING, "No ACG data found, use gender from CPX patient data for rendering");
            return AcgPatientData.toGeschlecht(gender);
        } else {
            return getAcgPatientData().geschlecht;
        }
    }

    public synchronized void paintBody(final StackPane pTargetPane, final double pZoom) {
        pTargetPane.getChildren().clear();
        Map<Integer, HumanPartGraphic> organs = getHumanPartList(getGender());
        for (Map.Entry<Integer, HumanPartGraphic> organEntry : organs.entrySet()) {
            HumanPartGraphic organ = organEntry.getValue();
            organ.addToPane(pTargetPane, pZoom, getOrganInfo(organ));
        }
        //humanPartGraphics = organs;
    }

    private synchronized Map<Integer, HumanPartGraphic> getHumanPartList(final char pGender) {
        if (humanPartGraphics != null && !humanPartGraphics.isEmpty()) {
            LOG.log(Level.FINE, "Human parts were already loaded. Reuse them!");
            return new LinkedHashMap<>(humanPartGraphics);
        }
        humanPartGraphics = AbstractHumanPartGraphic.getHumanPartList(pGender);
        return new LinkedHashMap<>(humanPartGraphics);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        AnchorPane.setTopAnchor(imagePane, 0d);
        AnchorPane.setRightAnchor(imagePane, 0d);
        AnchorPane.setBottomAnchor(imagePane, 0d);
        AnchorPane.setLeftAnchor(imagePane, 0d);
    }

    @FXML
    private void showBodyButtonAction(ActionEvent event) {
        zoomFactor = 1.0d;
        paintBody(imagePane, zoomFactor);
    }

    @FXML
    private void zoomInBtnAction(ActionEvent event) {
        zoomFactor = zoomFactor * (1 + 0.25d);
        paintBody(imagePane, zoomFactor);
    }

    @FXML
    private void zoomOutBtnAction(ActionEvent event) {
        zoomFactor = zoomFactor * (1 - 0.25d);
        paintBody(imagePane, zoomFactor);
    }

    @FXML
    private void newDiseasesButtonAction(ActionEvent event) throws IOException {
        AcgPatientData newAcgPatientData = HealthStatusApp.getRandomAcgPatientData();
        Map<Integer, HumanPartGraphic> organs = getHumanPartList(getGender());
        for (Map.Entry<Integer, HumanPartGraphic> organEntry : organs.entrySet()) {
            HumanPartGraphic organ = organEntry.getValue();
            organ.getImageView().setImage(null);
        }
        setAcgPatientData(newAcgPatientData);
        showBodyButtonAction(event);
    }

    public void setGender(final String pGender) {
        gender = pGender == null ? "" : pGender.trim();
    }

}

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
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartment;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.StringConverter;
import org.hibernate.Hibernate;

/**
 * screen for displaying wards and departments
 *
 * @author nandola
 */
public class CmDepartmentScene extends CpxScreen {

    private CaseServiceFacade facade;
    private VersionManager versionManager;
    private VersionStringConverter versionNameConverter;

    /**
     * @param pManager manager to access version handling
     * @throws IOException thrown if fxml was not found or erroneous
     */
    public CmDepartmentScene(VersionManager pManager) throws IOException {
        super(CpxFXMLLoader.getLoader(CmDepartmentFXMLController.class));
        if(pManager == null){
            MainApp.showErrorMessageDialog("Es konnte kein Versionsmanager gefunden werden!");
            return;
        }
        facade = pManager.getServiceFacade();
        versionManager = pManager;
        versionNameConverter = new VersionStringConverter(VersionStringConverter.DisplayMode.ACTUAL);
        getController().afterInitialisingScene();
    }

    public VersionManager getVersionManager() {
        return versionManager;
    }
    
    public TCaseDetails getCurrentLocalVersion(){
        return versionManager.getCurrentLocal();
    }
    
    public List<TCaseDetails> getSortedLocalVersions() {
        List<TCaseDetails> versions = versionManager.getCurrentLocals().stream().collect(Collectors.toList());
        versions.sort(new Comparator<TCaseDetails>() {
            @Override
            public int compare(TCaseDetails o1, TCaseDetails o2) {
                if (o1 == null || o2 == null) {
                    return -1;
                }
                return Integer.valueOf(o2.getCsdVersion()).compareTo(o1.getCsdVersion());
            }
        });
        return versions;
    }
    
    public void saveTCaseIcd(TCaseIcd icdItem) {
        facade.saveIcdEntity(icdItem);
    }
    
    /**
     * @return facade set in scene
     */
    public CaseServiceFacade getFacade() {
        return facade;
    }
    
    public List<TCaseWard> findWardsForDepartment(TCaseDepartment pDepartment){
        
        if(Hibernate.isInitialized(pDepartment.getCaseWards()) && !pDepartment.getCaseWards().isEmpty()){
            return new ArrayList<>(pDepartment.getCaseWards());
        }
        return facade.findWardsForDepartment(pDepartment);
    }
    public CpxDepartment getDepartmentCatalog(TCaseDepartment pDepartment){
        CpxDepartmentCatalog departmentCatalog = CpxDepartmentCatalog.instance();
        return departmentCatalog.getByCode(pDepartment.getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    public String getDepartment301Description(TCaseDepartment pDepartment){
        CpxDepartment departmentCatalog = getDepartmentCatalog(pDepartment);
        return departmentCatalog.getDepDescription301();
    }
    public List<TCaseDepartment> getDepartmentsForVersion(TCaseDetails pDetail) {
        if(pDetail == null){ 
            return new ArrayList<>();
        }
        if (pDetail.getId() != 0) {
            return new ArrayList<>(facade.findDepartments(pDetail.getId()));
        } else {
            return new ArrayList<>(pDetail.getCaseDepartments());
        }
    }

    public StringConverter<TCaseDetails> getVersionNameConverter() {
        return versionNameConverter;
    }
    
    
    private BooleanProperty armedProperty; //TODO: make generally accessable in scene?
    public BooleanProperty armedProperty() {
        if (armedProperty == null) {
            armedProperty = new SimpleBooleanProperty(true);
        }
        return armedProperty;
    }

    public boolean isArmed() {
        return armedProperty().get();
    }

    public void setArmed(Boolean pArmed) {
        armedProperty().set(pArmed);
    }
    private BooleanProperty editableProperty; //TODO: make generally accessable in scene?
    public BooleanProperty editableProperty() {
        if (editableProperty == null) {
            editableProperty = new SimpleBooleanProperty(true);
        }
        return editableProperty;
    }

    public boolean isEditable() {
        return editableProperty().get();
    }

    public void setEditable(Boolean pEditable) {
        editableProperty().set(pEditable);
    }

    public String getIcdCodeDescription(TCaseIcd pIcd, int pYear) {
        if(pIcd == null){
            return "";
        }
        return CpxIcdCatalog.instance().getDescriptionByCode(pIcd.getIcdcCode(), "de", pYear);
    }
    
    public String getOpsCodeDescription(TCaseOps pOps, int pYear) {
        if(pOps == null){
            return "";
        }
        return CpxOpsCatalog.instance().getDescriptionByCode(pOps.getOpscCode(), "de", pYear);
    }

    public List<TCaseIcd> findIcdsForDepartment(TCaseDepartment param) {
        return facade.findIcdsForDepartment(param);
    }

    public List<TCaseOps> findOpsesForDepartment(TCaseDepartment param) {
        return facade.findOpsesForDepartment(param);
    }
    
    public List<TCaseIcd> findIcdsForWard(TCaseWard param) {
        return facade.findIcdsForWard(param);
    }

    public List<TCaseOps> findOpsesForWard(TCaseWard param) {
        return facade.findOpsesForWard(param);
    }
  
}

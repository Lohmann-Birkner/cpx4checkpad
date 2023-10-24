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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.model;

import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Class to manage versions for the case simulation
 *
 * @author wilde
 */
public class VersionManager {

    private static final Logger LOG = Logger.getLogger(VersionManager.class.getName());
    
    private final ObservableList<VersionContent> managedVersions = FXCollections.observableArrayList();
    private final CaseServiceFacade versionCaseFacade;
    private final ObservableMap<Boolean, ObservableList<TCaseDetails>> displayedMap = FXCollections.observableMap(new HashMap<>());
    private final ReadOnlyBooleanWrapper allDisplayedProperty = new ReadOnlyBooleanWrapper(false);
    private final ObservableList<TCaseDetails> availableLocals = FXCollections.observableArrayList();
    private final ObservableList<TCaseDetails> availableExterns = FXCollections.observableArrayList();
    private static final Integer LIST_MAX_SIZE = 3;

    private final ListChangeListener<VersionContent> maxListSizeListener = new ListChangeListener<VersionContent>() {

        @Override
        public void onChanged(ListChangeListener.Change<? extends VersionContent> c) {
            while (c.next()) {
                if (c.wasAdded()) {
                    if (c.getList().size() > LIST_MAX_SIZE) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                int addedSize = c.getAddedSubList().size();
                                for (int i = 1; i <= addedSize; i++) {
                                    unMarkAsDisplayed(c.getList().get(i).getContent());
                                    c.getList().remove(i);
                                }
                            }
                        });
                    }
                }
            }
        }
    };
    private boolean isDestroyed;

    /**
     * creates new version manager instance, handels versions (case details) of
     * a specific case
     *
     * @param pCaseId database id of the case to manage versions (case details)
     * for
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException Case does not
     * exist
     */
    public VersionManager(Long pCaseId) throws CpxIllegalArgumentException {
        versionCaseFacade = new CaseServiceFacade();
        TCase curCase = versionCaseFacade.loadCase(pCaseId);

        if (curCase == null) {
            //2018-03-05 DNi - Ticket #CPX-717: throw an exception? open message dialog? crash the world? ignore the problem?
            //2019-08-21 AWi - My vote is: crash the world!
            throw new CpxIllegalArgumentException(Lang.getCaseDoesNotExistWithReason(String.valueOf(pCaseId)));
        }
        List<TCaseDetails> locals = new ArrayList<>(curCase.getLocals());
        locals.sort((a, b) -> Long.compare(b.getCsdVersion(), a.getCsdVersion()));
        List<TCaseDetails> externs = new ArrayList<>(curCase.getExterns());
        externs.sort((a, b) -> Integer.compare(b.getCsdVersion(), a.getCsdVersion()));
        availableLocals.addAll(locals);
        availableExterns.addAll(externs);

        managedVersions.addListener(maxListSizeListener);

        displayedMap.put(Boolean.TRUE, FXCollections.observableArrayList());
        displayedMap.put(Boolean.FALSE, FXCollections.observableArrayList(curCase.getCaseDetails()));
//        displayedMap.size();
    }

    /**
     * add version content to managed version list
     *
     * @param pVersion version to add
     */
    public void addToManagedVersions(VersionContent pVersion) {
        if (pVersion != null) {
            if (pVersion.getCaseDetails().getCsdIsLocalFl()) {
                managedVersions.add(pVersion);
            } else {
                //KIS is added
                managedVersions.add(getLastExternIndex(managedVersions), pVersion);
            }
        }
    }

    private int getLastExternIndex(List<VersionContent> pVersions) {
        for (VersionContent content : pVersions) {
            if (content.getCaseDetails().getCsdIsLocalFl()) {
                int idx = pVersions.indexOf(content) - 1;
                return idx > 0 ? idx : 0;
            }
        }
        return 0;
    }

    /**
     * add version content to managed version list
     *
     * @param pVersions version to add
     */
    public void addAllToManagedVersions(VersionContent... pVersions) {
        if (pVersions != null) {
            managedVersions.addAll(pVersions);
        }
    }
    
    public void addVersionsToManagedVersions(Long pExternVersionId, Long pLocalVersionId){
        TCaseDetails extern = getExternVersion(pExternVersionId);
        TCaseDetails local = getLocalVersion(pLocalVersionId);
        if(extern == null){
            LOG.log(Level.WARNING, "Extern-Version for Id: {0}, is not found! CurrentExtern will be set!", pExternVersionId);
        }
        if(local == null){
            LOG.log(Level.WARNING, "Local-Version for Id: {0}, is not found! CurrentLocal will be set!", pLocalVersionId);
        }
        VersionContent externVersionContent = createVersionContent(extern==null?getCurrentExtern():extern);
        VersionContent localVersionContent = createVersionContent(local==null?getCurrentLocal():local);
        addAllToManagedVersions(externVersionContent,
                localVersionContent);
    }
    /**
     * remove version from the managed version list
     *
     * @param pVersion version to remove
     * @return indicator if remove was successful
     */
    public boolean removeFromManagedVersions(VersionContent pVersion) {
        if (pVersion != null) {
            boolean removed = managedVersions.remove(pVersion);
            if (removed) {
                pVersion.destroy();
            }
            return removed;
        }
        return false;
    }
    public VersionContent getManagedVersionForId(TCaseDetails pDetails){
        if(pDetails == null){
            return null;
        }
        return getManagedVersionForId(pDetails.getId());
    }
    public VersionContent getManagedVersionForId(Long pId){
        if(pId == null){
            return null;
        }
        for (VersionContent content : managedVersions) {
            if (pId.equals(content.getCaseDetails().getId())) {
                return content;
            }
        }
        return null;
    }
    public VersionContent getManagedVersionForDetail(TCaseDetails pDetail) {
        for (VersionContent content : managedVersions) {
            if (content.getCaseDetails().versionEquals(pDetail)) {
                return content;
            }
        }
        return null;
    }

    public void removeDetailsFromLocals(TCaseDetails pDetails) {
        if (pDetails == null || !pDetails.getCsdIsLocalFl()) {
            return;
        }
        getAllAvailableLocals().remove(pDetails);
        unMarkAsDisplayed(pDetails);
        versionCaseFacade.getCurrentCase().getCaseDetails().remove(pDetails);
        displayedMap.get(Boolean.FALSE).remove(pDetails);
//        refreshAllLocalsDisplayed();
        //greater two, additional localversions are open, need to check if 
//        if(managedVersions.size()>2){
//            VersionContent content = getVersionContentForDetails(pDetails);
//            if(content != null){
//                removeFromManagedVersions(content);
//            }
//        }
    }

    public VersionContent getVersionContentForDetails(TCaseDetails pDetails) {
        for (VersionContent version : managedVersions) {
            if (version.getContent().equals(pDetails)) {
                return version;
            }
        }
        return null;
    }

    /**
     * get the managed version List, should be unmodifiable Please note that
     * only the manager should add and remove objects from the managed version
     * list list is only exposed to react to change events of the list
     *
     * @return get the list of managed versions
     */
    public ObservableList<VersionContent> getManagedVersions() {
        return managedVersions;//FXCollections.unmodifiableObservableList(managedVersions);
    }

    /**
     * creates and adds new version content object for the case details
     *
     * @param pDetails details to create version content for
     * @return newly created version content object
     */
    public VersionContent createAndAddVersionContent(TCaseDetails pDetails) {
        VersionContent newVersion = new VersionContent(this, pDetails);
        addToManagedVersions(newVersion);
        return newVersion;
    }

    /**
     * creates and adds new version content object for the case details
     *
     * @param pDetails details to create version content for
     * @return newly created version content object
     */
    public VersionContent createVersionContent(TCaseDetails pDetails) {
        return new VersionContent(this, pDetails);
    }

    /**
     * @return get all available extern case versions
     */
    public ObservableList<TCaseDetails> getAllAvailableExterns() {
        return availableExterns;
    }

    /**
     * @return get all available local case versions
     */
    public ObservableList<TCaseDetails> getAllAvailableLocals() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (block) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VersionManager.class.getName()).log(Level.INFO, null, ex);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
        return availableLocals;
    }

    /**
     * @return get the first available local version
     */
    public TCaseDetails getFirstUnSelectedLocal() {
        if (getAllNotDisplayed().isEmpty()) {
            return null;
        }
        for (TCaseDetails details : getAllNotDisplayed()) {
            if (details != null && details.getCsdIsLocalFl()) {
                return details;
            }
        }
        return null;
    }

    /**
     * @return get the currently set service facade for direct service access
     */
    public CaseServiceFacade getServiceFacade() {
        return versionCaseFacade;
    }

    /**
     * @return get the details currently set as local
     */
    public TCaseDetails getCurrentLocal() {
        return versionCaseFacade.getCurrentCaseLocal();
    }
    
    public Long getCurrentLocalId() {
        TCaseDetails local = getCurrentLocal();
        return local!=null?local.getId():null;
    }
    
    public TPatient getCurrentPatient(){
        return versionCaseFacade.getPatient();
    }
    /**
     * @return get set details currently set as extern
     */
    public Set<TCaseDetails> getCurrentLocals() {
        return versionCaseFacade.getCurrentCaseLocals();
    }

    /**
     * @return get the details currently set as extern
     */
    public TCaseDetails getCurrentExtern() {
        return versionCaseFacade.getCurrentCaseExtern();
    }
    public Long getCurrentExternId() {
        TCaseDetails extern = getCurrentExtern();
        return extern!=null?extern.getId():null;
    }
    public TCaseDetails getExternVersion(Long pExternVersionId){
        return versionCaseFacade.getCaseVersion(pExternVersionId,false);
    }
    
    public TCaseDetails getLocalVersion(Long pLocalVersionId){
        return versionCaseFacade.getCaseVersion(pLocalVersionId,true);
    }
    /**
     * @return get set details currently set as extern
     */
    public Set<TCaseDetails> getCurrentExterns() {
        return versionCaseFacade.getCurrentCaseExterns();
    }

    /**
     * @param pVersionIds list of versions to compute icds for
     * @return list of all icds grouped by icd code
     */
    public List<IcdOverviewDTO> getAllIcdCodes(List<Long> pVersionIds) {
        return versionCaseFacade.getAllIcdCodes(pVersionIds);
    }

    /**
     * @param pVersionIds list of versions to compute ops for
     * @return list of all ops grouped by icd code
     */
    public List<OpsOverviewDTO> getAllOpsCodes(List<Long> pVersionIds) {
        return versionCaseFacade.getAllOpsCodes(pVersionIds);
    }

    /**
     * checks if the details object is currently displayed
     *
     * @param pDetails details to check status
     * @return indicator if its considered as displayed
     */
    public boolean isDisplayed(TCaseDetails pDetails) {
        return displayedMap.get(Boolean.TRUE).contains(pDetails);
    }

    /**
     * @return property to indicate if all details are marked as displayed
     */
    public ReadOnlyBooleanProperty getAllDisplayedProperty() {
        return allDisplayedProperty.getReadOnlyProperty();
    }

    public boolean isAllDisplayed() {
        return allDisplayedProperty.get();
    }

    /**
     * @return get list of all details that are marked as displayed
     */
    public List<TCaseDetails> getAllDisplayed() {
        return displayedMap.get(Boolean.TRUE);
    }

    /**
     * @return list of all details that are not marked as displayed
     */
    public List<TCaseDetails> getAllNotDisplayed() {
        return displayedMap.get(Boolean.FALSE);
    }

    /**
     * @param pDetails marks the case details as displayed
     */
    public void markAsDisplayed(TCaseDetails pDetails) {
        if (displayedMap.get(Boolean.FALSE).contains(pDetails)) {
            displayedMap.get(Boolean.FALSE).remove(pDetails);
        }
        if (!displayedMap.get(Boolean.TRUE).contains(pDetails)) {
            displayedMap.get(Boolean.TRUE).add(pDetails);
        }
//        refreshAllLocalsDisplayed();
        checkIfAllLocalsDisplayed();
    }

    /**
     * @param pDetails unmark case details as displayed
     */
    public void unMarkAsDisplayed(TCaseDetails pDetails) {
        if (displayedMap.get(Boolean.TRUE).contains(pDetails)) {
            displayedMap.get(Boolean.TRUE).remove(pDetails);
        }
        if (!displayedMap.get(Boolean.FALSE).contains(pDetails)) {
            displayedMap.get(Boolean.FALSE).add(pDetails);
        }
//        refreshAllLocalsDisplayed();
        checkIfAllLocalsDisplayed();
    }

    /**
     * @param pDetails version to fetch fees for
     * @return list of case fees for a version
     */
    public List<TCaseFee> getCaseFees(TCaseDetails pDetails) {
        return versionCaseFacade.getCaseFeesForDetails(pDetails);
    }
    
    public List<TCaseBill> getCaseBills(TCaseDetails pDetails){
        return versionCaseFacade.getCaseBills4Details(pDetails);
    }

    /**
     * @return casefee list for current local version
     */
    public List<TCaseFee> getCaseFeesForExternLocal() {
        return getCaseFees(getCurrentExtern());
    }

    private void checkIfAllLocalsDisplayed() {
        //checks of one local is in the displayed part of the map, if not property is set to true
        for (TCaseDetails details : displayedMap.get(Boolean.FALSE)) {
            if (details != null && details.getCsdIsLocalFl()) {
                allDisplayedProperty.set(Boolean.FALSE);
                return;
            }
        }
        allDisplayedProperty.set(Boolean.TRUE);
    }

    /*
        refresh the displayed proeprty and refresh the combobox in version content to reflect changes 
     */
//    private void refreshAllLocalsDisplayed(){
//        checkIfAllLocalsDisplayed();
//        for(VersionContent version : managedVersions){
//            version.refreshComboBox();
//        }
//    }  
    public void destroy() {
        if(isDestroyed){
            return;
        }
        for (VersionContent version : managedVersions) {
            version.destroy();
        }
        managedVersions.clear();
        displayedMap.clear();
        availableLocals.clear();
        availableExterns.clear();
        versionCaseFacade.destroy();
        isDestroyed = true;
    }
    
    public boolean isAdditionalDisplayVersion(TCaseDetails pDetails){
        if(!displayedMap.get(Boolean.TRUE).contains(pDetails)){
            //is not showing, can not be additional version
            return false;
        }
        if(managedVersions.size()<2){
            //if size is smaller than three it can not be a additional version
            return false;
        }
        return getDisplayIndexOf(pDetails)>=2;
    }
    
    public int getDisplayIndexOf(TCaseDetails pDetails){
        if(pDetails == null){
            return -1;
        }
        Iterator<VersionContent> it = managedVersions.iterator();
        while (it.hasNext()) {
            VersionContent next = it.next();
            if(next.getCaseDetails().getId() == pDetails.getId()){
                return managedVersions.indexOf(next);
            }
        }
        return -1;
    }
    private boolean block = false;
    public void addAsyncToAvailableLocals(int i, TCaseDetails newlyCreated) {
        block = true;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getAllAvailableLocals().add(i, newlyCreated);
                block = false;
            }
        });
    }

    public void setOrUpdateRuleSelectFlag(CpxSimpleRuleDTO rule) {
        getServiceFacade().setOrUpdateRuleSelectFlag(rule);
        for(VersionContent vers: managedVersions) {
            vers.setOrUpdateRuleSelectFlag(rule);
        }
        
    }

    public void refreshVersionContent4GrouperModel() {
        for (VersionContent version : managedVersions) {
//            TGroupingResults res = version.getLastGroupingResultFromDb();
//            if(res == null){
                version.performGroup();
//            }
        }
    }
}

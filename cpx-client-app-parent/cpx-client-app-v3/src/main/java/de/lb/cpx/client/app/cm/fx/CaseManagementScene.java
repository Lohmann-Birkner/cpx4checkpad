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
package de.lb.cpx.client.app.cm.fx;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionContent;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.app.job.fx.CaseMergingScene;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.app.tabController.MergeParentTabScene;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * Scene for the case management scene aka case simulation
 *
 * @author wilde
 */
public final class CaseManagementScene extends MergeParentTabScene {
    
    private static final Logger LOG = Logger.getLogger(CaseManagementScene.class.getName());
    
    private final CaseServiceFacade caseService;
    private final VersionManager versionManager;
    


    /**
     * creates new instance of the case management scene / case simulation
     *
     * @param pCaseId database id of the case to create scene for
     * @param pExternVersionId extern version to set
     * @param pLocalVersionId local version to set
     * @throws IOException thrown when fxml is not found
     * @throws LockException thrown if error occured
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException case does not
     * exist
     */
    public CaseManagementScene(long pCaseId, Long pExternVersionId, Long pLocalVersionId) throws IOException, LockException {
        super(CpxFXMLLoader.getLoader(CaseManagementFXMLController.class));
        versionManager = new VersionManager(pCaseId);
        caseService = versionManager.getServiceFacade();
        caseService.lock(pCaseId);
        LOG.log(Level.INFO, "open case: {0}", String.valueOf(caseService.getCurrentCase()));
        getController().afterInitialisingScene();
        getController().initializeContent(versionManager, pExternVersionId, pLocalVersionId);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkGrouperYear(caseService.getCurrentCase());
            }
        });
    }

    public CaseManagementScene(long pCaseId) throws IOException, LockException {
        this(pCaseId, null, null);
    }

    @Override
    public CaseManagementFXMLController getController() {
        return (CaseManagementFXMLController) super.getController();
    }

    /**
     * @return get the currently displayed case
     */
    public TCase getDisplayedCase() {
        return caseService.getCurrentCase();
    }
    
    @Override
    public boolean close() {
        //unlock case and return default true, even if unlock failed scene should be closable
        caseService.unlockCurrentCase();
        return super.close();
    }
    
    @Override
    public Parent getRootWithoutHeader() {
        return getController().getRootWithoutHeader();
    }

    public void setKisAndCpVersion(Long pExternVersionId, Long pLocalVersionId) {
        setKisAndCpVersion(versionManager.getExternVersion(pExternVersionId), versionManager.getLocalVersion(pLocalVersionId));
    }

    public void setKisAndCpVersion(TCaseDetails pKisVersion, TCaseDetails pCpVersion) {
        if (pKisVersion == null) {
            LOG.warning("Kis Version is null, load current extern!");
        }
        if (pKisVersion == null) {
            LOG.warning("CP Version is null, load current local!");
        }
        VersionContent externVersionContent = versionManager.createVersionContent(pKisVersion == null ? versionManager.getCurrentExtern() : pKisVersion);
        VersionContent localVersionContent = versionManager.createVersionContent(pCpVersion == null ? versionManager.getCurrentLocal() : pCpVersion);
        versionManager.getManagedVersions().setAll(externVersionContent, localVersionContent);
    }
    
    private void checkGrouperYear(TCase currentCase) {
        if (currentCase == null) {
            LOG.severe("GrouperYearCheck failed! No Case were provided");
            return;
        }
        checkGrouperYear(currentCase.getCurrentExtern(),currentCase.getCurrentLocal());
    }
    
    public void checkGrouperYear(TCaseDetails... pCaseDetails) {
        if (pCaseDetails == null) {
            LOG.severe("GrouperYearCheck failed! No CaseDetails were provided");
            return;
        }
        if(!CpxClientConfig.instance().isAutomaticGrouperSelected()){ //Change for AGe - do only check year if grouper is automatic
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (TCaseDetails caseDetail : pCaseDetails) {
            Date admDate = Objects.requireNonNullElse(caseDetail.getCsdAdmissionDate(), Date.from(Instant.EPOCH));
            int minYear = GDRGModel.getMinModelYear();
            int admYear = Lang.toYear(admDate);
            if (minYear > admYear) {
                if(!builder.toString().isEmpty()){
                    builder.append("\n\n");
                }
                builder.append("Achtung!\nDas Aufnahmejahr: ")
                        .append(admYear).append(" der Fall-Version: ")
                        .append(VersionStringConverter.convertSimple(caseDetail))
                        .append(" wird nicht mehr vom Grouper unterstützt!\nGroupen der Fallversionen kann zum Datenverlust führen!");
            }
        }
        if(builder.toString().isEmpty()){
            return;//show nothing if there is no message
        }
        Notifications not = NotificationsFactory.instance().createWarningNotification().text(builder.toString()).hideAfter(Duration.seconds(10));
        runNotification(not);
    }
    private void runNotification(Notifications pNotification){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pNotification.show();
            }
        });
    }

    public boolean hasGroupingResult(Long... pIds) {
        return caseService.hasGroupingResult(pIds);
    }
}

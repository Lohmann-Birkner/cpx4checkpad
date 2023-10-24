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
package de.lb.cpx.client.app.job.fx.casemerging;

import de.lb.cpx.client.app.job.fx.casemerging.model.CaseMergeContent;
import de.lb.cpx.client.app.service.facade.CaseMergingFacade;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Case merging detail scene ('simulation of the case merging)
 *
 * @author wilde
 */
public class CaseMergingDetailsScene extends CpxScene {

    private static final Logger LOG = Logger.getLogger(CaseMergingDetailsScene.class.getName());
    private final ObservableList<CaseMergeContent> mergeList = FXCollections.observableArrayList();
    private final Integer mergeIdent;
    private final CaseMergingFacade facade;

    /**
     * init new scene
     *
     * @param pFacade facade to access server services
     * @param pMergeId merge id of case to merge
     * @param pMergedCase merged case
     * @param pGrpResults grouping result of merged case
     * @throws IOException if fxml can not be loaded
     */
    public CaseMergingDetailsScene(CaseMergingFacade pFacade, int pMergeId, TCase pMergedCase, TGroupingResults pGrpResults) throws IOException {
        super(CpxFXMLLoader.getLoader(CaseMergingDetailsFXMLController.class));
        if (!checkCaseType(pMergedCase.getCsCaseTypeEn())) {
            LOG.log(Level.SEVERE, "CaseType: " + pMergedCase.getCsCaseTypeEn().name() + " not yet supported!");
        }

        facade = pFacade;
        mergeIdent = pMergeId;

        mergeList.add(facade.createMergeContent(pMergedCase, pGrpResults));
        for (TCaseMergeMapping merge : facade.getCaseMergeByMergeId(pMergeId)) {
            mergeList.add(facade.createMergeContent(merge.getCaseByMergeMemberCaseId(), merge.getGrpresId()));
        }

        //inform controller that scene is initalised
        if (controller != null) {
            controller.afterInitialisingScene();
        }
    }

    /**
     * @return list of merge case content, handled by scene
     */
    public ObservableList<CaseMergeContent> getCaseMergeList() {
        return mergeList;
    }

    /**
     * @return get facade for service access
     */
    public CaseMergingFacade getFacade() {
        return facade;
    }

    /**
     * @return current case to merge
     */
    public TCase getMergedCase() {
        return getCaseMergeList().get(0).getContent();
    }

    /**
     * @return get cases to merge
     */
    public Set<TCase> getCases() {
        Set<TCase> cases = new HashSet<>();
        for (CaseMergeContent content : mergeList) {
            cases.add(content.getContent());
        }
        return cases;
    }

    /**
     * @return merge ident of merged case
     */
    public Integer getMergeIdent() {
        return mergeIdent;
    }

    @Override
    public boolean close() {
        boolean success = super.close(); //To change body of generated methods, choose Tools | Templates.
        //cleanup on close, can not do, Scene should be closes by x in tab
        //for cleanup
        facade.removeFromMergeCases(mergeIdent);
        return success;
    }

    private boolean checkCaseType(CaseTypeEn pCaseType) {
        switch (pCaseType) {
            case DRG:
                return true;
            case PEPP:
                return true;
            default:
                return false;
        }
    }

}

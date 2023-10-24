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
 *    2017  adameck - initial API and implementation and/or initial documentation
 *    2017  wilde - adding javadoc
 */
package de.lb.cpx.client.app.cm.casedetails.section;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TGroupingResults;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Details base class, organize specific sections of the details
 *
 * @author adameck TODO: can refactor this mess to a more consistant
 * implementation
 */
public class CmCaseOverviewSection extends SectionTitle {

    private static final Logger LOG = Logger.getLogger(CmCaseOverviewSection.class.getName());

    private final TCase tCase;
    private final TGroupingResults drgResult;
    private final TCaseDetails caseVersion;

    /**
     * creates new instance uses case version of the case based on settings in
     * session isCaseLocal fetches drg result based on case version to show
     * specific results
     *
     * @param tcase case to fetch data for
     */
    public CmCaseOverviewSection(TCase tcase) {
        this.tCase = tcase;
        if (tcase == null) {
            LOG.log(Level.WARNING, "tcase is null!");
            this.caseVersion = null;
            this.drgResult = null;
        } else {
            this.caseVersion = Session.instance().isCaseLocal() ? tcase.getCurrentLocal() : tcase.getCurrentExtern();
            if (caseVersion != null) {
                this.drgResult = Session.instance().getEjbConnector().connectSingleCaseBean().get().findGroupingResultsLazy(caseVersion.getId(), CpxClientConfig.instance().getSelectedGrouper());
            } else {
                LOG.log(Level.WARNING, "case details of " + String.valueOf(tcase) + " is null!");
                this.drgResult = null;
            }
        }
    }

    @Override
    public void setMenu() {
        //set Menu here
    }

    @Override
    protected Parent createContent() {
        VBox vb = new VBox();
        return vb;
    }

    @Override
    public Parent getDetailContent() {
        //create content 
        //creates 3 sections with enclosing wrappers due to size computations
        //refactor? 
        long start = System.currentTimeMillis();
        //section for general case info (top left)
        CmCaseGeneralSection caseSection = new CmCaseGeneralSection(tCase);
        caseSection.setArmed(false);
        VBox vbGeneral = new VBox(caseSection.getRoot());
        //section for drg infos (top right)
        CmDrgSection drgSection = new CmDrgSection(caseVersion, drgResult);
        drgSection.setArmed(false);
        VBox vbDRG = new VBox(drgSection.getRoot());
        //section for rules (tableview)
        CmCaseResolveSection caseResolveSection = new CmCaseResolveSection(caseVersion, drgResult);
        caseResolveSection.setArmed(false);
        VBox vbCaseResolve = new VBox(caseResolveSection.getRoot());
        //top box to place general info und drg info besides each other on top
        HBox topContent = new HBox(vbGeneral, vbDRG);
        //enclosing wrapper to place top content and rules, rules occupy all available size in height that is left over
        VBox allContent = new VBox(topContent, vbCaseResolve);
        VBox.setVgrow(vbCaseResolve, Priority.ALWAYS);
        //add scrollpane
        ScrollPane content = new ScrollPane(allContent);
        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.setFitToHeight(true);
        //size computation for drg and general case data, occupy size in width of with of scrollpane divided by to
        //each 50 percent of avaiable width
        vbGeneral.prefWidthProperty().bind(content.widthProperty().divide(2));
        vbDRG.prefWidthProperty().bind(content.widthProperty().divide(2));

        LOG.log(Level.FINER, "render details for case with id " + (tCase == null ? "null" : tCase.id) + " in " + (System.currentTimeMillis() - start) + " ms");
        return content;
    }

}

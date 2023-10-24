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
 *    2016  adameck - initial API and implementation and/or initial documentation
 *    2017  wilde - adding javadoc
 */
package de.lb.cpx.client.app.cm.casedetails.section;

import de.lb.cpx.client.app.cm.fx.simulation.tables.RulesTableView;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.server.catalog.service.ejb.RuleServiceBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Section for detected rules
 *
 * @author adameck
 */
public class CmCaseResolveSection extends SectionTitle {

    private static final Logger LOG = Logger.getLogger(CmCaseResolveSection.class.getName());

    private RulesTableView rulesCpTableView;
    private final TCaseDetails caseVersion;
    private TGroupingResults drgResults;

    /**
     * creates new instance
     *
     * @param pCurrentLocal case version
     * @param tGroupingResult grouping result for version
     */
    public CmCaseResolveSection(TCaseDetails pCurrentLocal, TGroupingResults tGroupingResult) {
        super(Lang.getCaseResolveEstimatedRules());
        drgResults = tGroupingResult;
        caseVersion = pCurrentLocal;
        setValues();
    }

    @Override
    public void setMenu() {
        //set Menu here
    }

    @Override
    protected Parent createContent() {
        //create content with new rules tableview
        //refactor tableview? deprcated
        rulesCpTableView = new RulesTableView();
        rulesCpTableView.setRulesSelect(false);
        rulesCpTableView.setPlaceholder(new Label(Lang.getNoCaseResolves()));

        VBox.setVgrow(rulesCpTableView, Priority.ALWAYS);
        VBox box = new VBox(rulesCpTableView);
        box.setFillWidth(true);
        //prevent tableview from growing
        //dirty fix, do not know why it occures otherwise
        //should be investigated!
        rulesCpTableView.maxWidthProperty().bind(box.widthProperty());
        return box;
    }

    @Override
    public Parent getDetailContent() {
        //set Detail content here
        return null;
    }

    private void setValues() {
        //if results from contructor is null, try to fetch new one and set up table view
        if (caseVersion == null) {
            LOG.log(Level.WARNING, "caseVersion is null!");
        }
        if (drgResults == null && caseVersion != null) {
            EjbProxy<SingleCaseEJBRemote> caseEJB = Session.instance().getEjbConnector().connectSingleCaseBean();
            drgResults = caseEJB.get().findGroupingResultsLazy(caseVersion.getId(), CpxClientConfig.instance().getSelectedGrouper());
        }
        setUpCpRulesTableView(drgResults);
    }

    private void setUpCpRulesTableView(TGroupingResults results) {
        if (results != null) {
            //setup tableview for drg results, find detected rules for grouping results id
            rulesCpTableView.setCaseType(results.getGrpresType());
            List<CpxSimpleRuleDTO> ruleList = findSortedRulesForLocalAndRuleIds(results.getId());
            rulesCpTableView.setItems(FXCollections.observableArrayList(ruleList));
        }
    }
    //find rules for its id

    private List<CpxSimpleRuleDTO> findSortedRulesForLocalAndRuleIds(long resultId) {
        List<CpxSimpleRuleDTO> dtos = findRulesForYearOfValidity(resultId, caseVersion.getCsdAdmissionDate());
        if (dtos != null) {
            dtos.sort(Comparator.comparingInt(CpxSimpleRuleDTO::getRuleTypeSeverity).reversed().thenComparing(CpxSimpleRuleDTO::getErrorTyp));
        }
        return dtos;
    }
    //find rules for id and admission date to lookup in rules catalog

    private List<CpxSimpleRuleDTO> findRulesForYearOfValidity(long resultId, Date date) {
        EjbProxy<RuleServiceBeanRemote> ruleServiceBean = Session.instance().getEjbConnector().connectToRuleServiceBean();
        //check on server, create dto and send back 
        return ruleServiceBean.get().findRulesAdmissionDateAndGroupingId(date, resultId);
    }
}

/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.Remote;

/**
 * Remote Interface for SingleCaseGroupring Service
 *
 * @author wilde
 */
@Remote
public interface SingleCaseGroupingEJBRemote {

    /**
     * Group LocalCase return future Value of AsyncTask
     *
     * @param hospitalCaseId db id of current TCase Entity
     * @param userId current User ID, for Rule selection
     * @param actualRoleId current RoleId of the User, Role can be admin etc.
     * @param grouperModel current GrouperModel selected in Client
     * @return List of GroupingResults
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    Future<List<TGroupingResults>> groupCaseLocal(Long hospitalCaseId, Long userId, Long actualRoleId, GDRGModel grouperModel) throws CpxIllegalArgumentException;

    /**
     * Group LocalCase return future Value of AsyncTask
     *
     * @param hospitalCaseId db id of current TCase Entity
     * @param userId current User ID, for Rule selection
     * @param actualRoleId current RoleId of the User, Role can be admin etc.
     * @param grouperModel current GrouperModel selected in Client
     * @return List of GroupingResults
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    Future<List<TGroupingResults>> groupCaseExtern(Long hospitalCaseId, Long userId, Long actualRoleId, GDRGModel grouperModel) throws CpxIllegalArgumentException;

    /**
     * Group LocalCase return future Value of AsyncTask
     *
     * @param hospitalCase current TCase Entity
     * @param userId current User ID, for Rule selection
     * @param actualRoleId current RoleId of the User, Role can be admin etc.
     * @param grouperModel current GrouperModel selected in Client
     * @return List of GroupingResults
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    Future<List<TGroupingResults>> groupCaseLocal(TCase hospitalCase, Long userId, Long actualRoleId, GDRGModel grouperModel) throws CpxIllegalArgumentException;

    /**
     * Group LocalCase return future Value of AsyncTask
     *
     * @param hospitalCase current TCase Entity
     * @param userId current User ID, for Rule selection
     * @param actualRoleId current RoleId of the User, Role can be admin etc.
     * @param grouperModel current GrouperModel selected in Client
     * @return List of GroupingResults
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    Future<List<TGroupingResults>> groupCaseExtern(TCase hospitalCase, Long userId, Long actualRoleId, GDRGModel grouperModel) throws CpxIllegalArgumentException;

    /**
     * initiate a new grouping process for the case details apply rules but DOES
     * NOT the simulation
     *
     * @param pDetailsId case details id in the database
     * @param pIsLocal indicator if details is a local copy
     * @param pUserId userId of the currently logged in user
     * @param pActualRoleId roleId of the currently logged in user
     * @param pGrouperModel selected groupermodel in the client
     * @return get the newly calculated grouping results for the case version
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    Future<List<TGroupingResults>> groupCaseDetails(Long pDetailsId, boolean pIsLocal, Long pUserId, Long pActualRoleId, GDRGModel pGrouperModel) throws CpxIllegalArgumentException;

    /**
     * initiate a new grouping process for the case details apply rules but DOES
     * NOT the simulation
     *
     * @param pDetailsId case details id in the database
     * @param pIsLocal indicator if details is a local copy
     * @param pUserId userId of the currently logged in user
     * @param pActualRoleId roleId of the currently logged in user
     * @param pGrouperModel selected groupermodel in the client
     * @param pRuleIds ids of rules which are to be used on grouping
     * @return get the newly calculated grouping results for the case version
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    Future<List<TGroupingResults>> groupCaseDetails(Long pDetailsId, boolean pIsLocal, Long pUserId, Long pActualRoleId, GDRGModel pGrouperModel, List<Long> pRuleIds) throws CpxIllegalArgumentException;

    /**
     * get the fee values for the supplementary type computed on the database
     *
     * @param pGrouper grouper
     * @param pCaseDetailsId version to compute values for
     * @param pType type of the fee
     * @return value as double
     */
    Double getSupplFeeValue(GDRGModel pGrouper, long pCaseDetailsId, SupplFeeTypeEn pType);

//    Double getSupplementaryValueForGroupingResultId(long pCaseDetailsId, boolean pCalcOnDb);
    /**
     * get the fee values for the supplementary type computed on the database
     *
     * @param pResult grouping result
     * @param pType type of the fee
     * @return value as double
     */
    Double getSupplFeeValue(TGroupingResults pResult, SupplFeeTypeEn pType);

    /**
     * get Temp Grouping Results, these are not stored in Db, and do not have
     * simulation results or rules but supplementary fees
     *
     * @param hospitalCase TCase Entity
     * @param pIsLocal should local version be grouped
     * @param model GDRG GrouperModel
     * @return temp Grouping Results
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    TGroupingResults getTempGroupingResults(TCase hospitalCase, Boolean pIsLocal, GDRGModel model) throws CpxIllegalArgumentException;

    /**
     * get Temp Grouping Results, these are not stored in Db, and do not have
     * simulation results or rules but supplementary fees
     *
     * @param hospitalCase TCase Entity
     * @param pIsLocal should local version be grouped
     * @param model GDRG GrouperModel
     * @param cmpIcd Comparator used for sorting of icds by client
     * @param cmpOps Comparator used for sorting of ops by client
     * @return temp Grouping Results
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    TGroupingResults getTempGroupingResults(TCase hospitalCase, Boolean pIsLocal, GDRGModel model, Comparator<TCaseIcd> cmpIcd, Comparator<TCaseOps> cmpOps) throws CpxIllegalArgumentException;
    
    /**
     * get Temp grouping results for simulating of the merged case. 
     * Attention: merging will be simulated on local cases only
     * that's why we don't need is local flag
     * @param hospitalCase tCase entity
     * @param model grouper model
     * @return gtouping results for merged case
     * @throws CpxIllegalArgumentException 
     */
    TGroupingResults getTempGroupingResults4merge(TCase hospitalCase, GDRGModel model) throws CpxIllegalArgumentException;

    /**
     * get the file content from the server where report is stored or generated.
     * Additionally, it's responsible to generate xml case data and final pdf.
     *
     * @param caseNumber caseNumber
     * @param hospitalIdent ik of the Hospital
     * @param selectedGrouper selected GrouperModel
     * @throws CpxIllegalArgumentException error
     * @return byte array (content of the report)
     */
    byte[] exportCase(String caseNumber, String hospitalIdent, GDRGModel selectedGrouper) throws CpxIllegalArgumentException;

    /**
     * To delete a report from server after creating on client.
     */
    void deleteReportFromServer();

    /**
     * group extern version of the case
     *
     * @param pCase tcase to group
     * @param pModel model to group with
     * @return grouping results (no ids set, due to temp grouping)
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    TGroupingResults getTempGroupingResultsExtern(TCase pCase, GDRGModel pModel) throws CpxIllegalArgumentException;

    /**
     * group local version of the case
     *
     * @param pCase tcase to group
     * @param pModel model to group with
     * @return grouping results (no ids set, due to temp grouping)
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    TGroupingResults getTempGroupingResultsLocal(TCase pCase, GDRGModel pModel) throws CpxIllegalArgumentException;

    public Future<List<TGroupingResults>> groupPatientCaseDetails(long pPatientId, boolean isLocal, long cpxUserId, long cpxActualRoleId, GDRGModel selectedGrouper);

}

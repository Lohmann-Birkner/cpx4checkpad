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
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface to define mthodes for case merging supported merging
 * mechanisms are merging due to readmission and backtransfer TODO: backtransfer
 * Note: Methodes do not provide extended validity checks
 *
 * @author wilde
 */
@Remote
public interface CaseMergingServiceBeanRemote {

    /**
     * @param pCases list of cases to merge
     * @param isSimul
     * @return merged case
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException thrown is simple
     * validation failed. Not enough elements, inconsistent case type etc.
     * WARNING: need to set references before saving! No Patientreference, no md
     * in casedetails etc.
     */
    TCase getMergedCaseByReadmssion(List<TCase> pCases, boolean isSimul) throws CpxIllegalArgumentException;

    /**
     * perform temp grouping of the current local version of the case
     *
     * @param pCase case to group
     * @param pModel grouper model to use
     * @return grouper result, due temp grouping los and leave is calculated and
     * should be stored in the corresponding case details
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    TGroupingResults performTempGrouping(TCase pCase, GDRGModel pModel) throws CpxIllegalArgumentException;
    
    TGroupingResults performTempGrouping4merge(TCase pCase, GDRGModel pModel) throws CpxIllegalArgumentException;

//    /**
//     * @return all viable merge mapping objects, all objects that do not have a
//     * merged case (reference in db is null)
//     */
//    List<TCaseMergeMapping> findAllViableCasesToMerge();
//
    /**
     * @param pModel gdrg model
     * @return if database meet requirments, for now checks if every case count
     * is equal to grouping result count for local versions
     */
//    Boolean checkDatabaseRequirements(GDRGModel pModel);
    
    /**
     * 
     * @param pModel grouper model
     * @param pType case type: drg/pepp
     * @return 
     */
    Boolean checkDatabaseRequirements(GDRGModel pModel, CaseTypeEn pType);
    /**
     * 
     * @param pModel grouper model
     * @param pType case type: drg/pepp
     * @param pPatientId
     * @return 
     */
    Boolean checkDatabaseRequirements4Patient(GDRGModel pModel, CaseTypeEn pType, Long pPatientId);

    /**
     * @param pMergeId merge id of merge process, needed to store right relation
     * @param pCase merged case to persist
     * @return indicator if saving was successful
     */
    TCase persistMergedCase(Integer pMergeId, TCase pCase);

    /**
     * @param pCases cases to storno
     * @return indicator if process was successful
     */
//    Boolean cancelCases(List<TCase> pCases);

    /**
     * merge and persist case by its merging id
     *
     * @param pIdent merging id to merge
     * @return indicator if successful
     */
    Long persistAndMergeById(Integer pIdent);


    /**
     * save merged case, do db cleanup
     *
     * @param pMergedCase merged case
     * @param pIdent merge ident number
     * @return db id of merged case
     */
    Long saveMergedCase(TCase pMergedCase, Integer pIdent);
    
    Long updateMergedCase(TCase pMergedCase, Integer pIdent);

    Boolean isCaseTypeSupported(CaseTypeEn pCsType);

    /**
     * @param pGrpresType type to use drg,pepp etc
     * @param pGrouperModel
     * @param pPatientId
     * @return list of all viable mergeCaseMapping for grpresType
     */

    List<TCaseMergeMapping> findAllViableCasesForType(CaseTypeEn pGrpresType,  GDRGModel pGrouperModel, Long pPatientId);

    /**
     * 
     * @param grpresType drg/pepp
     * @param grouperModel grouper model
     * @param pPatientId patient id
     * @param csIds ids of cases to exclude( these cases are already merge memders)
     * @return list of temporary merging objects that are not saved in data base - they do not habe mergeid and id
     **/
    List<TCaseMergeMapping> findNotMergingCasesForType(CaseTypeEn grpresType, GDRGModel grouperModel, Long pPatientId, List<Long> csIds);
    
    boolean isSaveCaseMergingAllowed();
    
    List<String> getCaseNumbers4CanceledCase4Merge(long pCaseId);

    public String checkHasMerged(Integer ident);

}

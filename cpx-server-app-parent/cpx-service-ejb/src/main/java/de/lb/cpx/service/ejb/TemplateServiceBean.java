/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.server.commonDB.dao.CBaserateDao;
import de.lb.cpx.server.commonDB.dao.CBookmarksCustomerDao;
import de.lb.cpx.server.commonDB.dao.CDraftsDao;
import de.lb.cpx.server.commonDB.model.CBookmarksCustomer;
import de.lb.cpx.server.commonDB.model.CDrafts;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TCaseDetailsDao;
import de.lb.cpx.server.dao.TCaseIcdDao;
import de.lb.cpx.server.dao.TCaseOpsDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.server.wm.dao.TWmDocumentDao;
import de.lb.cpx.server.wm.dao.TWmProcessDao;
import de.lb.cpx.wm.model.TWmDocument;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author nandola
 */
@Stateless
public class TemplateServiceBean implements TemplateServiceBeanRemote {

    @EJB
    private TCaseDao caseDao;
    @EJB
    private TCaseDetailsDao caseDetailsDao;
    @EJB
    private TWmProcessDao processDao;
    @EJB
    private TCaseIcdDao caseIcdDao;
    @EJB
    private TGroupingResultsDao groupingResultsDao;
    @EJB
    private TCaseOpsDao caseOpsDao;
    @EJB
    private CDraftsDao draftsDao;
    @EJB
    private TWmDocumentDao documentDao;
    @EJB
    private CBaserateDao baseRateCatalogDao;
    @EJB
    private CBookmarksCustomerDao bookmarksCustomerDao;

//    private TCase hCase;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public TCase getEntityForTemplate(long caseId) {
        TCase hCase = caseDao.findById(caseId);

        if (hCase == null) {
            return null;
        }

        hCase.getPatient().getId();

        hCase.getCaseDetails().iterator();

        hCase.getPatient().getInsurances().iterator();

        hCase.getPatient().getPatientDetailList().iterator();

        // local version
//        TCaseIcd icd = caseIcdDao.findMainDiagnosisIcd(hCase.getCurrentLocal().getId());    // find main diagnoses for this case
//        TGroupingResults result = groupingResultsDao.findTGroupingResult(hCase.getCurrentLocal().getId(), GDRGModel.AUTOMATIC, icd.getId());
        // Extern Version
//        TCaseIcd icd = caseIcdDao.findMainDiagnosisIcd(hCase.getCurrentExtern().getId());
//        TGroupingResults result = groupingResultsDao.findTGroupingResult(hCase.getCurrentExtern().getId(), GDRGModel.AUTOMATIC, icd.getId());
//        TWmProcess process = new TWmProcess();
//        process.getReminders().iterator();
//        process.getRequests().iterator();
//        process.getActions().iterator();
        return hCase;
        //      return caseDao.findById(pId);
    }

    @Override
    public TGroupingResults getResultsLocal(long pLocalId, GDRGModel selectedGrouper) {
//        TCaseIcd icd = caseIcdDao.findMainDiagnosisIcd(pLocalId);
//        TGroupingResults result = groupingResultsDao.findTGroupingResult(pLocalId, selectedGrouper, icd == null ? null : icd.getId());
        TGroupingResults result = groupingResultsDao.findGroupingResult_nativ(pLocalId, selectedGrouper);
        return result;
    }

    @Override
    public TGroupingResults getResultsExtern(long pExternId, GDRGModel selectedGrouper) {
//        TCaseIcd icd = caseIcdDao.findMainDiagnosisIcd(pExternId);
//        TGroupingResults result = groupingResultsDao.findTGroupingResult(pExternId, selectedGrouper, icd == null ? null : icd.getId());
        TGroupingResults result = groupingResultsDao.findGroupingResult_nativ(pExternId, selectedGrouper);
        return result;
    }

    @Override
    public TGroupingResults getResultsLocal(TCase hCase, GDRGModel selectedGrouper) {
        hCase = caseDao.merge(hCase);   // call this method on serverside to create a copy of case.
//        TCaseIcd icd = caseIcdDao.findMainDiagnosisIcd(hCase.getCurrentLocal() == null ? null : hCase.getCurrentLocal().getId());
//        TGroupingResults result = groupingResultsDao.findTGroupingResult(hCase.getCurrentLocal() == null ? null : hCase.getCurrentLocal().getId(), selectedGrouper, icd == null ? null : icd.getId());
        if(hCase.getCurrentLocal() == null){
            return null;
        }else{
            TGroupingResults result = groupingResultsDao.findGroupingResult_nativ(hCase.getCurrentLocal().getId(), selectedGrouper);

            return result;
        }
    }

    @Override
    public TGroupingResults getResultsExtern(TCase hCase, GDRGModel selectedGrouper) {
        hCase = caseDao.merge(hCase);
//        TCaseIcd icd = caseIcdDao.findMainDiagnosisIcd(hCase.getCurrentExtern() == null ? null : hCase.getCurrentExtern().getId());
//        TGroupingResults result = groupingResultsDao.findTGroupingResult(hCase.getCurrentExtern() == null ? null : hCase.getCurrentExtern().getId(), selectedGrouper, icd == null ? null : icd.getId());
        if(hCase.getCurrentExtern() == null){
            return null;
        }else{
            TGroupingResults result = groupingResultsDao.findGroupingResult_nativ(hCase.getCurrentExtern().getId(), selectedGrouper);

            return result;
        }
    }

//    @Override
//    public TGroupingResults getResultsLocal(Session session, TCase hCase, GDRGModel selectedGrouper) {
//        TCaseIcd icd = caseIcdDao.findMainDiagnosisIcd(hCase.getCurrentLocal().getId());
//        TGroupingResults result = groupingResultsDao.findTGroupingResult(hCase.getCurrentLocal().getId(), selectedGrouper, icd.getId());
//        return result;
//    }
//
//    @Override
//    public TGroupingResults getResultsExtern(Session session, TCase hCase, GDRGModel selectedGrouper) {
//        TCaseIcd icd = caseIcdDao.findMainDiagnosisIcd(hCase.getCurrentExtern().getId());
//        TGroupingResults result = groupingResultsDao.findTGroupingResult(hCase.getCurrentExtern().getId(), selectedGrouper, icd.getId());
//        return result;
//    }
    @Override
    public List<String> getAllTemplateDirectory() {
        return draftsDao.getAllTemplateDirectory();
    }

//    @Override
//    public List<byte[]> getAllTemplateContent() {
//        return draftsDao.getAllTemplateContent();
//    }
    @Override
    public TWmDocument storeDocument(TWmDocument pDocument) {
        documentDao.persist(pDocument);
        return pDocument;
    }

//    @Override
//    public TWmDocument storeDoc(File myFile) {
//        documentDao.persist(myFile);
//        return myFile;
//    }
//    @Override
//    public List<Lob> getAllTemplateContent() {
//        return draftsDao.getAllTemplateContent();
//    }
    @Override
    public List<byte[]> getAllTemplateContent() {
        return draftsDao.getAllTemplateContent();
    }

    @Override
    public List<CDrafts> getAllTemplates() {
        //return draftsDao.getAllTemplates();
        return draftsDao.findAll();
    }

    @Override
    public List<CDrafts> findAllTemplatesByCategoryInternalId(List<Long> cat1Ids, List<Long> cat2Ids, List<Long> cat13ds) {
        return draftsDao.findAllTemplatesByCategoryInternalId(cat1Ids, cat2Ids, cat13ds);
    }

    @Override
    public Double findDrgBaserateFeeValue(String csHospitalIdent, Date csdAdmissionDate /*, String countryCode */) {
        return baseRateCatalogDao.findDrgBaserateFeeValue(csHospitalIdent, csdAdmissionDate /*, countryCode */);
    }
    @Override
    public Double findCareBaserateFeeValue(String csHospitalIdent, Date csdAdmissionDate /*, String countryCode */) {
        return baseRateCatalogDao.findCareBaserateFeeValue(csHospitalIdent, csdAdmissionDate /*, countryCode */);
    }
    @Override
    public List<CBookmarksCustomer> getAllBookmarksCustomerEntries() {
        //return bookmarksCustomerDao.getAllBookmarksCustomerEntries();
        return bookmarksCustomerDao.findAll();
    }

    @Override
    public byte[] getDraftContent(long draftId) {
        return draftsDao.getDraftContent(draftId);
    }
}

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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.FileUtils;
import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferPatient;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TCaseSupplFee;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.report.generator.fop.ReportGenerator;
import de.lb.cpx.report.generator.xml.XmlGenerator;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TCaseDetailsDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

/**
 * Interface Implementation for SingleCase Grouping Contains Methods for
 * Grouping of extern or local Case-Versions, Cotnains Method for Calc of
 * Supplementory Fee
 *
 * @author wilde
 */
@Stateless
public class SingleCaseGroupingEJB implements SingleCaseGroupingEJBRemote {

    private static final Logger LOG = Logger.getLogger(SingleCaseGroupingEJB.class.getName());

    @EJB
    private TCaseDao hospitalCaseDao;
    @EJB
    private TCaseDetailsDao detailsDao;
//    @EJB
    @Inject
    private GrouperService grouperService;
    @EJB
    private TGroupingResultsDao groupingResultsDao;
    @Inject
    private GrouperCommunication grouperCommunication;

    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

//    @EJB
//    private XmlGenerator xmlGenerator ;
//   @EJB(lookup="java:module/cpx-report-generator!de.lb.cpx.report.generator.xml.IXmlGenerator")
    @Inject
    private XmlGenerator xmlGenerator;
    @Inject
    private ReportGenerator reportGenerator;
//    private String reportPath = null;
//    private File report = null;

    @Override
    @Asynchronous
    public Future<List<TGroupingResults>> groupCaseLocal(Long hospitalCaseId, Long userId, Long actualRoleId, GDRGModel model) throws CpxIllegalArgumentException {
        long timeStart = System.currentTimeMillis();
        long timeStart1 = System.currentTimeMillis();
//        grouperService.resetRuleList();  

//        LOG.log(Level.INFO, "SingleCaseGroupingEJB: resetRuleList " + ((System.currentTimeMillis() - timeStart1)) + " ms");
        TCase hospitalCase = hospitalCaseDao.findById(hospitalCaseId);
        if (hospitalCase == null) {
            LOG.log(Level.SEVERE, "Can''t find HospitalCase for Id {0}! Abort Grouping", hospitalCaseId);
            return new AsyncResult<>(new ArrayList<>());
        }
        LOG.log(Level.FINEST, "SingleCaseGroupingEJB: {0} hospitalCaseDao.findById {1} ms", new Object[]{hospitalCaseId, System.currentTimeMillis() - timeStart});
        List<TGroupingResults> performed = performGroupAndCheck(hospitalCase, true, model.getGDRGVersion(), actualRoleId);

        //AWi 20161206, add persist, otherwise entity is only stored when transaction ends, in singlecase grouping we need the results before that (need db id of new objects for 
        //detected rules
        double startSave = System.currentTimeMillis();
        for (TGroupingResults res : performed) {
            groupingResultsDao.persist(res);
        }
        double timeToSave = System.currentTimeMillis() - startSave;
        LOG.log(Level.FINEST, "SingleCaseGroupingEJB: Time to save new results ({0}) {1} ms. For 1 Entry {2} ms.", new Object[]{performed.size(), timeToSave, performed.isEmpty() ? timeToSave : timeToSave / performed.size()});

        hospitalCaseDao.mergeAndFlush(hospitalCase);
        LOG.log(Level.FINEST, "SingleCaseGroupingEJB: mergeAndFlush {0} ms", System.currentTimeMillis() - timeStart);
        LOG.log(Level.INFO, "SingleCaseGroupingEJB: fullGroup {0} ms", System.currentTimeMillis() - timeStart1);
        return new AsyncResult<>(performed);
    }

    @Override
    @Asynchronous
    public Future<List<TGroupingResults>> groupCaseExtern(Long hospitalCaseId, Long userId, Long actualRoleId, GDRGModel model) throws CpxIllegalArgumentException {

        TCase hospitalCase = hospitalCaseDao.findById(hospitalCaseId);
        if (hospitalCase == null) {
            LOG.log(Level.SEVERE, "Can''t find HospitalCase for Id {0}! Abort Grouping", hospitalCaseId);
            return new AsyncResult<>(new ArrayList<>());
        }
        List<TGroupingResults> performed = performGroup(hospitalCase, false, model.getGDRGVersion());
        hospitalCaseDao.merge(hospitalCase);
        return new AsyncResult<>(performed);
    }

    @Override
    @Asynchronous
    public Future<List<TGroupingResults>> groupCaseLocal(TCase hospitalCase, Long userId, Long actualRoleId, GDRGModel model) throws CpxIllegalArgumentException {
        hospitalCase = hospitalCaseDao.merge(hospitalCase);
        List<TGroupingResults> performed = performGroupAndCheck(hospitalCase, true, model.getGDRGVersion(), actualRoleId);
        hospitalCaseDao.mergeAndFlush(hospitalCase);

        return new AsyncResult<>(performed);
    }

    @Override
    @Asynchronous
    public Future<List<TGroupingResults>> groupCaseExtern(TCase hospitalCase, Long userId, Long actualRoleId, GDRGModel model) throws CpxIllegalArgumentException {
        hospitalCase = hospitalCaseDao.merge(hospitalCase);
        List<TGroupingResults> performed = performGroup(hospitalCase, false, model.getGDRGVersion());
        hospitalCaseDao.mergeAndFlush(hospitalCase);
        return new AsyncResult<>(performed);
    }

    /*
    * group one case and write the results into corresponding Entities
     */
    private List<TGroupingResults> performGroup(TCase hospitalCase, boolean isLocal, int modelId) throws CpxIllegalArgumentException {
        long timeTotal = System.currentTimeMillis();
        TransferCase requestObject = new TransferCase();
        long timeStart = System.currentTimeMillis();
        final boolean groupingRequestPerformed = grouperCommunication.fillGrouperRequest(hospitalCase, requestObject, isLocal, modelId);
        LOG.log(Level.FINEST, "performGroup: fillGrouperRequest {0} ms", System.currentTimeMillis() - timeStart);
        if (!isLocal) {
            requestObject.doSimulate(false);// for extern case we do not do simulate drgs
        }

        //2018-03-01 DNi - Ticket #CPX-848: Stop further processing if no grouping request was performed! (strange errors can occur if we would follow the path here)
        if (!groupingRequestPerformed) {
            LOG.log(Level.WARNING, "No grouping performed for this case: {0} (id {1})", new Object[]{hospitalCase == null ? "NULL" : hospitalCase.getCaseKey(), hospitalCase == null ? "NULL" : String.valueOf(hospitalCase.getId())});
            return new ArrayList<>();
        }

        timeStart = System.currentTimeMillis();
        Object responceObject = grouperService.processGrouperRequest(requestObject);
        LOG.log(Level.FINEST, "performGroup: processGrouperRequest {0} ms", System.currentTimeMillis() - timeStart);
        if (responceObject instanceof GrouperResponseObject) {
            timeStart = System.currentTimeMillis();
            Map<String, List<TGroupingResults>> result = grouperCommunication.fillGrouperResults(hospitalCase, (GrouperResponseObject) responceObject);
            LOG.log(Level.FINEST, "performGroup: fillGrouperResults {0} ms", System.currentTimeMillis() - timeStart);
            return result.get("newResults");
        }
        LOG.log(Level.FINEST, "performGroup: total{0} ms", System.currentTimeMillis() - timeTotal);
        return new ArrayList<>();
    }

    /*
     * group and rule check one case and write the results into corresponding Entities
     */
    private List<TGroupingResults> performGroupAndCheck(TCase hospitalCase, boolean isLocal, int modelId, Long actualRoleId) throws CpxIllegalArgumentException {
        TransferCase requestObject = new TransferCase();
        requestObject.doSimulate(true);
        long timeStart = System.currentTimeMillis();
//        long timeStart1 = System.currentTimeMillis();

        final boolean groupingRequestPerformed = grouperCommunication.fillGrouperRequest(hospitalCase, requestObject, isLocal, modelId);
        requestObject.addRoleId(actualRoleId);

        LOG.log(Level.FINEST, "SingleCaseGroupingEJB: GrouperCommunication.fillGrouperRequest {0} ms", System.currentTimeMillis() - timeStart);
        timeStart = System.currentTimeMillis();

        //2018-03-01 DNi - Ticket #CPX-848: Stop further processing if no grouping request was performed! (strange errors can occur if we would follow the path here)
        if (!groupingRequestPerformed) {
            LOG.log(Level.WARNING, "No grouping performed for this case: {0} (id {1})", new Object[]{hospitalCase == null ? "NULL" : hospitalCase.getCaseKey(), hospitalCase == null ? "NULL" : String.valueOf(hospitalCase.getId())});
            return new ArrayList<>();
        }
        LOG.log(Level.INFO, "SingleCaseGroupingEJB, performGroupAndCheck: rules: {0}", grouperService.getRulesCount());

        Object responceObject = grouperService.processRuleGrouperRequest(requestObject); //don't to this without proper grouping request!
        LOG.log(Level.FINEST, "SingleCaseGroupingEJB: grouperService.processRuleGrouperRequest {0} ms", System.currentTimeMillis() - timeStart);
        LOG.log(Level.INFO, "SingleCaseGroupingEJB, performGroupAndCheck: rules: {0}", grouperService.getRulesCount());

        if (responceObject instanceof GrouperResponseObject) {
            timeStart = System.currentTimeMillis();
            Map<String, List<TGroupingResults>> result = grouperCommunication.fillGrouperResults(hospitalCase, (GrouperResponseObject) responceObject);
            LOG.log(Level.FINEST, "SingleCaseGroupingEJB: GrouperCommunication.fillGrouperResults {0} ms", System.currentTimeMillis() - timeStart);
            return result.get("newResults");
        }
        return new ArrayList<>();
    }

    private void doSetRules(List<Long> ruleIds) {
        if (cpxServerConfig.isRuleEditorClient()) {
            if (ruleIds != null) {
                grouperService.setRuleList(ruleIds);
            }
        }

    }

    /*
     * group and rule check one case and write the results into corresponding Entities
     * methode to apply grouping to case details that are not set to actual in the hospital case
     */
    private List<TGroupingResults> performGroupAndCheck(TCase hospitalCase, TCaseDetails pDetails, int modelId, Long actualRoleId) throws CpxIllegalArgumentException {
        TransferPatient patientTransferObject = new TransferPatient(modelId);
        TransferCase requestObject = new TransferCase();
        requestObject.doSimulate(false);
        long timeStart = System.currentTimeMillis();
        final boolean groupingRequestPerformed = grouperCommunication.fillGrouperRequest(hospitalCase, pDetails, requestObject, modelId);

        requestObject.addRoleId(actualRoleId);
                //so i get the simulated values
        requestObject.doSimulate(true);

        LOG.log(Level.FINEST, "SingleCaseGroupingEJB: GrouperCommunication.fillGrouperRequest {0} ms", System.currentTimeMillis() - timeStart);
        timeStart = System.currentTimeMillis();
        //2018-03-01 DNi - Ticket #CPX-848: Stop further processing if no grouping request was performed! (strange errors can occur if we would follow the path here)
        if (!groupingRequestPerformed) {
            LOG.log(Level.WARNING, "No grouping performed for this case: {0} (id {1})", new Object[]{hospitalCase == null ? "NULL" : hospitalCase.getCaseKey(), hospitalCase == null ? "NULL" : String.valueOf(hospitalCase.getId())});
            return new ArrayList<>();
        }

        patientTransferObject.setMainCase(requestObject);
        if (cpxServerConfig.useHistoryCases4Group() && grouperService.hasHistoryRules4Year(pDetails.getCsdAdmissionYear())) { // check for flag for usage of all cases of this patient
// get history cases    
            List<TCase> historyCases = hospitalCaseDao.findListOfTCaseForPatient(hospitalCase.getPatient().getId());
            boolean isLocal = pDetails.getCsdIsLocalFl();
            for (TCase hisCase : historyCases) {
                if (hisCase.getCsCaseNumber().equals(hospitalCase.getCsCaseNumber())
                        && hisCase.getCsHospitalIdent().equals(hospitalCase.getCsHospitalIdent())) {
                    continue;
                }
                TransferCase trHisCase = new TransferCase();

                TCaseDetails hisDetails = isLocal ? hisCase.getCurrentLocal() : hisCase.getCurrentExtern();
                if (grouperCommunication.fillGrouperRequest(hisCase, hisDetails, trHisCase, modelId)) {
                    patientTransferObject.addHistoryCase(trHisCase);
                } else {
                    LOG.log(Level.WARNING, "No grouping performed for this history case: {0} (id {1})", new Object[]{hisCase.getCaseKey(), String.valueOf(hisCase.getId())});
                }
            }
        }

        LOG.log(Level.INFO, "SingleCaseGroupingEJB, performGroupAndCheck: rules: {0}", grouperService.getRulesCount());
        LOG.log(Level.INFO, "SingleCaseGroupingEJB, performGroupAndCheck: patient, history cases: {0}", patientTransferObject.getHistoryCases().size());
        Object responceObject = grouperService.processRuleGrouperRequest(patientTransferObject); //don't to this without proper grouping request!
        LOG.log(Level.INFO, "SingleCaseGroupingEJB: grouperService.processRuleGrouperRequest {0} ms", System.currentTimeMillis() - timeStart);
        LOG.log(Level.INFO, "SingleCaseGroupingEJB, performGroupAndCheck: rules: {0}", grouperService.getRulesCount());

        if (responceObject instanceof GrouperResponseObject) {
            timeStart = System.currentTimeMillis();
            Map<String, List<TGroupingResults>> result = grouperCommunication.fillGrouperResults(hospitalCase, pDetails, (GrouperResponseObject) responceObject);
            LOG.log(Level.INFO, "SingleCaseGroupingEJB: GrouperCommunication.fillGrouperResults {0} ms", System.currentTimeMillis() - timeStart);

            return result.get("newResults");
        }
        return new ArrayList<>();
    }

//    @Override
//    public Double getSupplementaryValueForGroupingResultId(long pCaseDetailsId, boolean pCalcOnDb) {
//        if (pCalcOnDb) {
//            Double val = (Double) groupingResultsDao.getSupplementaryValueForIdCalculateOnDatabase(pCaseDetailsId);
//            return val != null ? val : 0.0d;
//        } else {
//            return groupingResultsDao.getSupplementaryValueForId(pCaseDetailsId);
//        }
//    }
    @Override
    public Double getSupplFeeValue(GDRGModel pGrouper, long pCaseDetailsId, SupplFeeTypeEn pType) {
        switch (pType) {
            case ZE:
                return (Double) groupingResultsDao.getSupplementaryValueForIdCalculateOnDatabase(pGrouper, pCaseDetailsId, pType);
            case ZP:
                return (Double) groupingResultsDao.getSupplementaryValueForIdCalculateOnDatabase(pGrouper, pCaseDetailsId, pType);
            case ET:
                return (Double) groupingResultsDao.getSupplementaryValueForIdCalculateOnDatabase(pGrouper, pCaseDetailsId, pType);
            default:
                return 0.0;
        }

    }

    @Override
    public Double getSupplFeeValue(TGroupingResults pGroupingResult, SupplFeeTypeEn pType) {
        return computeSupplValue(pGroupingResult, pType);
    }

    @Override
    public TGroupingResults getTempGroupingResults(TCase hospitalCase, Boolean pIsLocal, GDRGModel model) {
        return getTempGroupingResults(hospitalCase, pIsLocal, model, null, null);
    }

    @Override
    public TGroupingResults getTempGroupingResults(TCase hospitalCase, Boolean pIsLocal, 
            GDRGModel model, Comparator<TCaseIcd> cmpIcd, Comparator<TCaseOps> cmpOps
    ) {
        return getTempGroupingResults(hospitalCase, pIsLocal, 
            model, cmpIcd,  cmpOps, false);
    }
    
    @Override
    public TGroupingResults getTempGroupingResults4merge(TCase hospitalCase, GDRGModel model) throws CpxIllegalArgumentException{
        return getTempGroupingResults(hospitalCase, true, 
            model, null, null, true);
    }
        
    
   private TGroupingResults getTempGroupingResults(TCase hospitalCase, Boolean pIsLocal, 
            GDRGModel model, Comparator<TCaseIcd> cmpIcd, Comparator<TCaseOps> cmpOps, boolean doSimulate
    ) {        
        TGroupingResults result = null;
        long start = System.currentTimeMillis();
        TransferCase requestObject = new TransferCase();
        Map<TCaseOps, Long> opsList = new HashMap<>();
        Map<TCaseIcd, Long> icdList = new HashMap<>();
        final boolean groupingRequestPerformed = grouperCommunication.fillGrouperRequest(hospitalCase, requestObject, pIsLocal, model.getGDRGVersion(), icdList, opsList);
//        requestObject.doSimulate(true);
        //2018-03-01 DNi - Ticket #CPX-848: Stop further processing if no grouping request was performed! (strange errors can occur if we would follow the path here)
        if (!groupingRequestPerformed) {
            LOG.log(Level.WARNING, MessageFormat.format("No grouping performed for this case: {0} (id {1})", (hospitalCase == null ? "NULL" : hospitalCase.getCaseKey()), (hospitalCase == null ? "NULL" : hospitalCase.getId())));
            return result;
        }
        requestObject.doSimulate(doSimulate);
        Object responceObject = grouperService.processRuleGrouperRequest(requestObject); //don't to this without proper grouping request!
        if (responceObject instanceof GrouperResponseObject) {
            try {
                result = GrouperCommunication.getTempResult4Group((GrouperResponseObject) responceObject, icdList, opsList, cmpIcd, cmpOps,
                        pIsLocal?hospitalCase.getCurrentLocal():hospitalCase.getCurrentExtern());

            } catch (CpxIllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "Grouping failed!", ex);
            }
        }
        LOG.log(Level.FINEST, "Temp Group in {0} ms", (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public TGroupingResults getTempGroupingResultsExtern(TCase pCase, GDRGModel pModel) {
        return getTempGroupingResults(pCase, false, pModel);
    }

    @Override
    public TGroupingResults getTempGroupingResultsLocal(TCase pCase, GDRGModel pModel) {
        return getTempGroupingResults(pCase, true, pModel);
    }

    @Override
    public byte[] exportCase(String caseNumber, String hospitalIdent, GDRGModel selectedGrouper) {
        if (selectedGrouper == null) {
            throw new IllegalArgumentException("selectedGrouper is null!");
        }
//      XmlGenerator xmlGenerator = new XmlGenerator();
        TCase hospitalCase = hospitalCaseDao.findCaseForCaseNumberAndIdent(caseNumber, hospitalIdent);
        if (hospitalCase == null) {
            throw new IllegalArgumentException("Report generation failed for hospital ident " + hospitalIdent + " and case number " + caseNumber + " (selected grouper is " + selectedGrouper + "): no case found!");
        } else {
            LOG.log(Level.INFO, MessageFormat.format("Start report generation for case {0} (selected grouper is {1})...", String.valueOf(hospitalCase), String.valueOf(selectedGrouper)));
        }
//        String xmlFileName = xmlGenerator.generateCaseDataXML(hospitalCase, selectedGrouper);
        File xmlFile = xmlGenerator.generateCaseDataXML(hospitalCase, selectedGrouper);

//        reportGenerator = new ReportGenerator();
//        String reportPath = null;
//        File report = null;
        byte[] byteArray = null;

        File reportFile = null;
        try {
//          reportPath = reportGenerator.generatePDFContent(xmlFileName, m_inputXSL);
            reportFile = reportGenerator.generatePDFContent(xmlFile,hospitalCase.getCsCaseTypeEn().name());
            //report = new File(reportPath);
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException | IllegalStateException e) {
            final CpxIllegalArgumentException ex = new CpxIllegalArgumentException("Report generation failed for case " + hospitalCase, e);
            LOG.log(Level.SEVERE, null, ex);
            throw ex;
        }

        try (FileInputStream in = new FileInputStream(reportFile)) {
            //convert file object to byte[]
            byteArray = IOUtils.toByteArray(in);
        } catch (IOException e) {
            final CpxIllegalArgumentException ex = new CpxIllegalArgumentException("Cannot read byte array from generated report for case " + hospitalCase + ": " + reportFile.getAbsolutePath(), e);
            LOG.log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            FileUtils.deleteFile(reportFile, true);
        }
        return byteArray;
//        return report;
//        return reportPath;
    }

    @Override
    public void deleteReportFromServer() {
//        ReportGenerator reportGenerator = new ReportGenerator();
//        reportGenerator.deleteFile(report);
//            report.deleteOnExit();
//        try {
//            Path path = report.toPath();
//            Files.delete(path);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, null, ex); // The process cannot access the file because it is being used by another process.
//        }
    }

    @Override
    @Asynchronous
    public Future<List<TGroupingResults>> groupCaseDetails(Long pDetailsId, boolean pIsLocal, Long pUserId, Long pActualRoleId,
            GDRGModel pGrouperModel) {
        return groupCaseDetails(pDetailsId, pIsLocal, pUserId, pActualRoleId,
                pGrouperModel, null);
    }

    @Override
    @Asynchronous
    public Future<List<TGroupingResults>> groupCaseDetails(Long pDetailsId, boolean pIsLocal, Long pUserId, Long pActualRoleId,
            GDRGModel pGrouperModel, List<Long> ruleIds) {
        long timeStart = System.currentTimeMillis();
        doSetRules(ruleIds);

        TCaseDetails caseDetails = detailsDao.findById(pDetailsId);
        if (caseDetails == null) {
            LOG.log(Level.SEVERE, "Can''t find caseDetails for Id {0}! Abort Grouping", caseDetails);
            return new AsyncResult<>(new ArrayList<>());
        }
        final TCase cs = caseDetails.getHospitalCase();
        LOG.log(Level.FINEST, MessageFormat.format("SingleCaseGroupingEJB: {0} and caseDetails id {1} ({2}, detailsDao.findById {3} ms", String.valueOf(cs), caseDetails.id, (caseDetails.getCsdIsLocalFl() ? "local" : "extern"), (System.currentTimeMillis() - timeStart)));
        List<TGroupingResults> performed = performGroupAndCheck(cs, caseDetails, pGrouperModel.getGDRGVersion(), pActualRoleId);

        //AWi 20161206, add persist, otherwise entity is only stored when transaction ends, in singlecase grouping we need the results before that (need db id of new objects for 
        //detected rules
        double startSave = System.currentTimeMillis();
        for (TGroupingResults res : performed) {
            caseDetails.setCsdLos(res.getCaseDetails().getCsdLos());
            detailsDao.persist(caseDetails);
            groupingResultsDao.persist(res);
        }
        double timeToSave = System.currentTimeMillis() - startSave;
        LOG.log(Level.FINEST, MessageFormat.format("SingleCaseGroupingEJB: Time to save new results ({0}) {1} ms. For 1 Entry {2} ms", performed.size(), timeToSave, (performed.isEmpty() ? timeToSave : timeToSave / performed.size())));
//        hospitalCaseDao.mergeAndFlush(caseDetails);
//        LOG.log(Level.FINEST, "SingleCaseGroupingEJB: mergeAndFlush " + ((System.currentTimeMillis() - timeStart)) + " ms");
        return new AsyncResult<>(performed);
    }

    private Double computeSupplValue(TGroupingResults pGroupingResult, SupplFeeTypeEn pType) {
        double supplFee = 0.0;
        for (TCaseOpsGrouped opsGr : pGroupingResult.getCaseOpsGroupeds()) {
            TCaseSupplFee fee = opsGr.getCaseSupplFees();
            if (fee != null && fee.getCsuplTypeEn().equals(pType)) {
                double feeProd = (fee.getCsuplValue() * fee.getCsuplCount());
                supplFee += feeProd;
            }
        }
        return supplFee;
    }

    @Override
    @Asynchronous
    public Future<List<TGroupingResults>> groupPatientCaseDetails(long pPatientId, boolean isLocal, long pUserId, long pActualRoleId, GDRGModel pGrouperModel) {
       List<TCaseDetails> datails2group = detailsDao.getActualDetails4PatientId(pPatientId, isLocal); 
       List<TGroupingResults> retList = new ArrayList<>();
       if(datails2group == null || datails2group.isEmpty()){
           return new AsyncResult<>(retList);
       }
       for(TCaseDetails details: datails2group){
           try {
               retList.addAll(groupCaseDetails(details.getId(), isLocal,  pUserId, pActualRoleId,
                       pGrouperModel).get());
           } catch (InterruptedException | ExecutionException ex) {
               Logger.getLogger(SingleCaseGroupingEJB.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       return new AsyncResult<>(retList);
    } 

}

/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.FileUtils;
import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.serviceutil.export.ProcessCsvItemCallback;
import de.lb.cpx.serviceutil.export.ProcessExcelItemCallback;
import de.lb.cpx.serviceutil.export.ProcessItemCallback;
import de.lb.cpx.service.export.SearchItemDTOMapper;
import de.lb.cpx.service.jms.producer.P21MessageProducer;
import de.lb.cpx.serviceutil.export.ProcessXmlItemCallback;
import de.lb.cpx.shared.dto.MessageDTO;
import de.lb.cpx.shared.dto.QuotaListItemDTO;
import de.lb.cpx.shared.dto.RuleListItemDTO;
import de.lb.cpx.shared.dto.SearchItemDTO;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.batch.runtime.BatchStatus;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author niemeier
 */
@Stateful
@SecurityDomain("cpx")
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(value = TransactionAttributeType.NEVER)
public class ListExportEJB {

    private static final Logger LOG = Logger.getLogger(ListExportEJB.class.getName());

    @EJB
    private WorkingListSearchService workingListSearchService;

    @EJB
    private RuleListSearchService ruleListSearchService;

    @EJB
    private QuotaListSearchService quotaListSearchService;

    @EJB
    private WorkflowListSearchService workflowListSearchService;

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:jboss/exported/jms/queue/CpxBatchImportStatusQueue")
    private Destination destination;

    @Resource(name = "java:comp/DefaultManagedThreadFactory")
    private ManagedThreadFactory tf;

    @EJB
    private TCaseDao caseDao;

    @EJB
    private P21MessageProducer producer;
    
        @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

//    @EJB
//    private SearchListExportHelper searchListExportHelper;
    @EJB
    private SearchItemDTOMapper dtoMapper;

    private BatchStatus batchstatus = BatchStatus.ABANDONED;

    public static final int MAX_PHASES = 16;

//    public static final String DELIMITER = ";";
//    public static final String NEWLINE = "\n";
    public int getMaxPhases() {
        return MAX_PHASES;
    }

    protected <W extends Closeable, T extends SearchItemDTO> void process(final long executionId, final String pFileName, final AbstractSearchService<T> searchService, final ProcessItemCallback<W, T> pProcessItemCb, final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final SearchListTypeEn pListType, final long[] pSelectedIds, Map<ColumnOption, List<FilterOption>> pOptions) {
        final int page = 0;
        final int fetchSize = -1; //-1 -> get all hospital cases that match the filter (paging ignored)!

        final String tempFolder = System.getProperty("java.io.tmpdir") + "\\list_export_" + executionId + "\\";
        LOG.log(Level.INFO, "Temp folder for list export: {0}", tempFolder);
        final File tmp = new File(tempFolder);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }

        final AtomicInteger phase = new AtomicInteger(0);

        //Phase 1: Collecting data! We're only interested in fetching case ids!
        phase.incrementAndGet();
        List<FilterOption> insInsCompanyShortNames = new ArrayList<>();
        sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Ermittle zu exportierende Einträge");
        Iterator<Map.Entry<ColumnOption, List<FilterOption>>> it = pOptions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<ColumnOption, List<FilterOption>> entry = it.next();
            ColumnOption colOption = entry.getKey();
            //if (!colOption.attributeName.equals(WorkingListAttributes.id)) {
//            colOption.setShouldShow(false);
            if (pSelectedIds != null && pSelectedIds.length > 0
                    && (colOption.attributeName.equals(WorkingListAttributes.id) || colOption.attributeName.equals(WorkflowListAttributes.id))) {
                StringBuilder sb = new StringBuilder();
                for (long id : pSelectedIds) {
                    sb.append(id + ",");
                }
                if (!entry.getValue().isEmpty()) {
                    FilterOption option = entry.getValue().iterator().next();
                    option.setValue(sb.toString());
                }
            }

            if(colOption.attributeName.equals(WorkingListAttributes.insInsCompanyShortName)
                    || colOption.attributeName.equals(WorkflowListAttributes.insInsCompanyShortName)){
                 if (!entry.getValue().isEmpty()) {
                      List<FilterOption> fOpts = entry.getValue();
                      for(FilterOption opt : fOpts){
                        insInsCompanyShortNames.add(new FilterOption(opt.name, opt.field, opt.getValue()));
                      }
                 }
            }
        }

//        final int fetchRows = 5000; //very relevant for performance!
        final String query = searchService.getQuery(pIsLocal, pIsShowAllReminders, pGrouperModel, page, fetchSize, pOptions, AbstractSearchService.QueryType.NORMAL);
        //final String charset = CpxSystemProperties.DEFAULT_ENCODING;
        File zipFile = null;

        try {
            int itemCountTmp = -1;
            try (Statement stmt = caseDao.getConnection().createStatement()) {
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Ermittle die Anzahl der Einträge");
                String countQuery = query;
                int pos = countQuery.lastIndexOf(" ORDER BY ");
                if (pos > -1) {
                    int pos2 = countQuery.indexOf('\n', pos);
                    countQuery = countQuery.substring(0, pos).trim();
                    if (pos2 > 0) {
                        countQuery += query.substring(pos2);
                    }
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) CNT FROM (" + countQuery + ") TMP")) {
                    while (rs.next()) {
                        itemCountTmp = rs.getInt("CNT");
                    }
                }
                LOG.log(Level.INFO, "Found {0} items to export as list", itemCountTmp);
            } catch (SQLException ex1) {
                batchstatus = BatchStatus.FAILED;
                sendStatusFailureJobMessage(executionId, "Es ist ein SQL-Fehler aufgetreten", ex1);
                //LOG.log(Level.SEVERE, "Grouping of case id " + cs + " failed (blocking queue is probably full and writer thread died)", ex);
                LOG.log(Level.SEVERE, "Error in sql statement", ex1);
            }
            final int itemCount = itemCountTmp;

            if (BatchStatus.STOPPING.equals(batchstatus)) {
                return;
                //batchstatus = BatchStatus.ABANDONED;
                //break;
            }

            //Phase: write basic csv/excel file
            phase.incrementAndGet();
            sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erstelle die CSV-/Excel/XML-Datei");

            final File targetFile = new File(tempFolder + pFileName);

            final AtomicInteger counter = new AtomicInteger();
            try (W pWriter = pProcessItemCb.createWriter(targetFile)) {
                //final W[] writers = new W[]{pWriter};

                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Schreibe Daten in die CSV-/Excel/XML-Datei");

                final SearchItemCallback<T> cb = new SearchItemCallback<>() {
                    @Override
                    public void call(final T dto) throws IOException, InterruptedException {
                        //write to file
                        int itCount = counter.incrementAndGet();
                        pProcessItemCb.call(pWriter, itCount, dto);

                        //don't send progress message for each case
                        if (itCount % 1000 == 0 || itCount == itemCount) {
                            LOG.log(Level.INFO, "Written {0}/{1} cases to csv/excel/xml file", new Object[]{itCount, itemCount});
                            sendStatusJobMessage(executionId, phase.get(), itCount /* number of files written */, itemCount /* of total number of files */, batchstatus, "Schreibe Eintrag " + String.format(java.util.Locale.GERMAN, "%,d", itCount) + "/" + String.format(java.util.Locale.GERMAN, "%,d", itemCount) + " in die CSV-/Excel-Datei...");
                        }
                        if (BatchStatus.STOPPING.equals(batchstatus)) {
                            throw new InterruptedException("list export is stopping");
                        }
                    }
                };

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                    //batchstatus = BatchStatus.ABANDONED;
                    //break;
                }

                //Phase: execute query
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Führe SQL-Abfrage aus");
                if(!insInsCompanyShortNames.isEmpty()){
                Iterator<Map.Entry<ColumnOption, List<FilterOption>>> it1 = pOptions.entrySet().iterator();
                    while (it1.hasNext()) {
                        Map.Entry<ColumnOption, List<FilterOption>> entry = it1.next();
                        ColumnOption colOption = entry.getKey();
                        if(colOption.attributeName.equals(WorkingListAttributes.insInsCompanyShortName)
                                 || colOption.attributeName.equals(WorkflowListAttributes.insInsCompanyShortName)){
                            entry.setValue(insInsCompanyShortNames);
                            break;
                        }
                    }
                }
                searchService.getAllWithCriteriaForFilter(pIsLocal, pIsShowAllReminders, pGrouperModel, page, fetchSize, pOptions, cb); // .getQuery(pIsLocal, false, pGrouperModel, page, fetchSize, pOptions);
                
                //Phase: Close writers
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Finalisiere Export-Dateien");
                pProcessItemCb.closeWriter(pWriter);
//                for (Writer writer : writers) {
//                    writer.flush();
//                    writer.close();
//                }

                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Nenne Datei um");
                final String newFileName = addFileSuffix(targetFile.getAbsolutePath(), " mit " + itemCount + " Einträgen");
                final File newFile = new File(tempFolder + newFileName);
                if (targetFile.renameTo(newFile)) {
                    LOG.log(Level.FINEST, "target file was renamed to {0}", newFile.getName());
                }

                final File[] files = tmp.listFiles();
                if (!BatchStatus.STOPPED.equals(batchstatus)) {
                    //Phase: Compress files to ZIP for upload
                    zipFile = new File(tmp.getAbsolutePath() + "\\" + tmp.getName() + ".zip");
                    LOG.log(Level.INFO, "Compress list files to {0}...", zipFile.getAbsolutePath());
                    phase.incrementAndGet();
                    sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Packe die Export-Dateien für den Download");
                    FileUtils.zipFiles(files, zipFile);
                    if (zipFile.exists()) {
                        zipFile.deleteOnExit(); //zip file can be deleted later
                    }
                    LOG.log(Level.INFO, "Compressing List files finished!");
                }
                if (!BatchStatus.STOPPED.equals(batchstatus)) {
                    //Phase: finishing export
                    LOG.log(Level.INFO, "Finishing List export...");
                    batchstatus = BatchStatus.COMPLETED;
                    phase.incrementAndGet();
                    sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Listen-Export auf dem Server beendet");
                    LOG.log(Level.INFO, "Finished List export, files were written to {0}", tempFolder);
                }
            } catch (InterruptedException ex) {
                batchstatus = BatchStatus.STOPPED;
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Der Listenexport wurde abgebrochen");
                LOG.log(Level.FINEST, "list export was interrupted", ex);
                Thread.currentThread().interrupt();
            }
        } catch (IOException ex) {
            batchstatus = BatchStatus.FAILED;
            sendStatusFailureJobMessage(executionId, "Eine " + pListType + "-Datei konnte nicht zum Schreiben geöffnet werden", ex);
            LOG.log(Level.SEVERE, "Cannot open file writer", ex);
        } finally {
            LOG.log(Level.INFO, "Delete {0} files", pListType);
            for (File file : tmp.listFiles()) {
                if (zipFile != null && !zipFile.equals(file)) {
                    P21ExportEJB.deleteFile(file, true);
                }
            }
            if (BatchStatus.STOPPING.equals(batchstatus)) {
                LOG.log(Level.INFO, "Stopping list export...");
                batchstatus = BatchStatus.STOPPED;
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Export abgebrochen");
                LOG.log(Level.INFO, "List export stopped!");
                P21ExportEJB.deleteFile(tmp, true);
            }
        }
        batchstatus = BatchStatus.ABANDONED;
    }


    public <W extends Closeable, T extends SearchItemDTO> long prepareExport(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final SearchListTypeEn pListType, final ExportTypeEn pExportType, final long[] pSelectedIds, final Map<ColumnOption, List<FilterOption>> pOptions) {
        final long executionId = System.currentTimeMillis();

        tf.newThread(new Runnable() {
            @Override
            @SuppressWarnings("unchecked")
            public void run() {
                if (!BatchStatus.ABANDONED.equals(batchstatus)) {
                    return;
                }
                batchstatus = BatchStatus.STARTED;

                final Set<ColumnOption> columnOptions = new TreeSet<>(pOptions.keySet());
                Iterator<ColumnOption> it = columnOptions.iterator();
                while (it.hasNext()) {
                    ColumnOption colOption = it.next();
                    if(colOption.attributeName.equals(RuleListAttributes.ruleSelected)){
                        colOption.setShouldShow(cpxServerConfig.getShowRelevantRules());
                    }

                    if (!colOption.isShouldShow()) {
                        it.remove();
                    }
                }

                final AbstractSearchService<T> searchService;
                final ProcessItemCallback<W, T> processItemCb;
                final String fileNameTmp;
                if (pListType.isQuotaList()) {
                    searchService = (AbstractSearchService<T>) quotaListSearchService;
                    if (pExportType.isCsv()) {
                        //CSV WORKING LIST
                        fileNameTmp = "Exportierte Prüfquotenliste.csv";
                        processItemCb = (ProcessItemCallback<W, T>) getQuotaListCsvCb(columnOptions);
                    } else if(pExportType.isExcel()){
                        //EXCEL WORKING LIST
                        fileNameTmp = "Exportierte Prüfquotenliste.xlsx";
                        processItemCb = (ProcessItemCallback<W, T>) getQuotaListExcelCb(columnOptions);
                    }else{
                       fileNameTmp = "Exportierte Prüfquotenliste.xml";
                        processItemCb = (ProcessItemCallback<W, T>) getQuotaListXmlCb(columnOptions);
                        
                    }
                } else if (pListType.isWorkingList()) {
                    searchService = (AbstractSearchService<T>) workingListSearchService;
                    if (pExportType.isCsv()) {
                        //CSV WORKING LIST
                        fileNameTmp = "Exportierte Arbeitsliste.csv";
                        processItemCb = (ProcessItemCallback<W, T>) getWorkingListCsvCb(columnOptions);
                    }  else if(pExportType.isExcel()){
                        //EXCEL WORKING LIST
                        fileNameTmp = "Exportierte Arbeitsliste.xlsx";
                        processItemCb = (ProcessItemCallback<W, T>) getWorkingListExcelCb(columnOptions);
                    }else{
                       fileNameTmp = "Exportierte Arbeitsliste.xml";
                        processItemCb = (ProcessItemCallback<W, T>) getWorkingListXmlCb(columnOptions);
                        
                    }
                } else if (pListType.isRuleList()) {
                    searchService = (AbstractSearchService<T>) ruleListSearchService;
                    if (pExportType.isCsv()) {
                        //CSV WORKING LIST
                        fileNameTmp = "Exportierte Regelliste.csv";
                        processItemCb = (ProcessItemCallback<W, T>) getRuleListCsvCb(columnOptions);
                    }  else if(pExportType.isExcel()){
                        //EXCEL WORKING LIST
                        fileNameTmp = "Exportierte Regelliste.xlsx";
                        processItemCb = (ProcessItemCallback<W, T>) getRuleListExcelCb(columnOptions);
                    }else{
                        fileNameTmp = "Exportierte Regelliste.xml";
                        processItemCb = (ProcessItemCallback<W, T>) getRuleLisXmlCb(columnOptions);
                       
                    }
                } else if (pListType.isWorkflowList()) {
                    searchService = (AbstractSearchService<T>) workflowListSearchService;
                    if (pExportType.isCsv()) {
                        //CSV WORKFLOW LIST
                        fileNameTmp = "Exportierte Vorgangsliste.csv";
                        processItemCb = (ProcessItemCallback<W, T>) getWorkflowListCsvCb(columnOptions);
                    }  else if(pExportType.isExcel()){
                        //EXCEL WORKFLOW LIST
                        fileNameTmp = "Exportierte Vorgangsliste.xlsx";
                        processItemCb = (ProcessItemCallback<W, T>) getWorkflowListExcelCb(columnOptions);
                    }else{
                       fileNameTmp = "Exportierte Vorgangsliste.xml";
                        processItemCb = (ProcessItemCallback<W, T>) getWorkflowListXmlCb(columnOptions);
                        
                    }
                } else {
                    throw new IllegalStateException("Unknown list export type: " + pListType);
                }

                final String date = Lang.toIsoDateTime(new Date()).replace(":", "-");
                final String userName = ClientManager.getCurrentCpxUserName();
                final String suffix = " erstellt von " + userName + " am " + date;
                final String fileName = addFileSuffix(fileNameTmp, suffix);

                process(executionId, fileName, searchService, processItemCb, pIsLocal, pIsShowAllReminders, pGrouperModel, pListType, pSelectedIds, pOptions);
            }
        }).start();

        return executionId;
    }

    private static String addFileSuffix(final String pFileName, final String pSuffix) {
        final String extension = FilenameUtils.getExtension(pFileName);
        final String name = FilenameUtils.getBaseName(pFileName);
        final String fileName = name + pSuffix + "." + extension;
        return fileName;
    }

    public boolean stopExport(final long pToken) {
        if (BatchStatus.ABANDONED.equals(batchstatus)) {
            return true;
        }
        batchstatus = BatchStatus.STOPPING;
        return false;
    }

    private void sendStatusJobMessage(final long pExcecutionId, final int pPhase, final int pSubphase, final int pMaxSubphases, final BatchStatus pBatchStatus, final String pComment) {
        LOG.log(Level.FINEST, "ListExportEJB:sendStatusJobMessage:: ExecutionID: {0}", pExcecutionId);
        sendObjectMessage(pExcecutionId, new MessageDTO(pPhase, MAX_PHASES, pSubphase, pMaxSubphases, pBatchStatus, null, 0, null, pComment), "importStatusMessage");
    }

    private void sendStatusFailureJobMessage(final long pExecutionId, String pReasonForFailure, final Exception pException) {
        LOG.log(Level.SEVERE, "JmsProducer send message. Import failure: {0}", pReasonForFailure);
        final int phase = 0;
        final int maxPhases = 0;
        final int subphase = 0;
        final int maxSubphases = 0;
        sendObjectMessage(pExecutionId, new MessageDTO(phase /* phase */, maxPhases, subphase, maxSubphases, BatchStatus.FAILED, pException, 0, pReasonForFailure, ""), "importStatusMessage");
    }

    public void sendObjectMessage(final long pExecutionId, MessageDTO messageDTO, String type) {
        producer.sendObjectMessage(pExecutionId, messageDTO, type);
    }

    private ProcessItemCallback<Writer, WorkingListItemDTO> getWorkingListCsvCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessCsvItemCallback<WorkingListItemDTO>() {
            @Override
            public void call(Writer pWriter, int pNo, WorkingListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getWorkingListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }
                List<Object> values = dtoMapper.getWorkingListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }

        };
    }

    private ProcessItemCallback<SXSSFWorkbook, WorkingListItemDTO> getWorkingListExcelCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessExcelItemCallback<WorkingListItemDTO>() {

            @Override
            public void call(SXSSFWorkbook pWriter, int pNo, WorkingListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getWorkingListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                    //pWriter.append(String.join(DELIMITER, values) + NEWLINE);
                }
                List<Object> values = dtoMapper.getWorkingListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

    private ProcessItemCallback<Writer, WorkingListItemDTO> getWorkingListXmlCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessXmlItemCallback<WorkingListItemDTO>() {

            @Override
            public void call(Writer pWriter, int pNo, WorkingListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getWorkingListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }
                List<Object> values = dtoMapper.getWorkingListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

    private ProcessItemCallback<Writer, RuleListItemDTO> getRuleListCsvCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessCsvItemCallback<RuleListItemDTO>() {
            @Override
            public void call(Writer pWriter, int pNo, RuleListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getRuleListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }
                List<Object> values = dtoMapper.getRuleListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }

        };
    }

    private ProcessItemCallback<SXSSFWorkbook, RuleListItemDTO> getRuleListExcelCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessExcelItemCallback<RuleListItemDTO>() {

            @Override
            public void call(SXSSFWorkbook pWriter, int pNo, RuleListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getRuleListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                    //pWriter.append(String.join(DELIMITER, values) + NEWLINE);
                }
                List<Object> values = dtoMapper.getRuleListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

    private ProcessItemCallback<Writer, RuleListItemDTO> getRuleLisXmlCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessXmlItemCallback<RuleListItemDTO>() {

            @Override
            public void call(Writer pWriter, int pNo, RuleListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getRuleListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }
                List<Object> values = dtoMapper.getRuleListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

    
    private ProcessItemCallback<Writer, QuotaListItemDTO> getQuotaListCsvCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessCsvItemCallback<QuotaListItemDTO>() {
            @Override
            public void call(Writer pWriter, int pNo, QuotaListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getQuotaListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }
                List<Object> values = dtoMapper.getQuotaListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }

        };
    }

    private ProcessItemCallback<SXSSFWorkbook, QuotaListItemDTO> getQuotaListExcelCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessExcelItemCallback<QuotaListItemDTO>() {

            @Override
            public void call(SXSSFWorkbook pWriter, int pNo, QuotaListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getQuotaListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                    //pWriter.append(String.join(DELIMITER, values) + NEWLINE);
                }
                List<Object> values = dtoMapper.getQuotaListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

    private ProcessItemCallback<Writer, QuotaListItemDTO> getQuotaListXmlCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessXmlItemCallback<QuotaListItemDTO>() {

            @Override
            public void call(Writer pWriter, int pNo, QuotaListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getQuotaListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }

                List<Object> values = dtoMapper.getQuotaListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

    private ProcessItemCallback<Writer, WorkflowListItemDTO> getWorkflowListCsvCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessCsvItemCallback<WorkflowListItemDTO>() {
            @Override
            public void call(Writer pWriter, int pNo, WorkflowListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getWorkflowListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }
                List<Object> values = dtoMapper.getWorkflowListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

    private ProcessItemCallback<SXSSFWorkbook, WorkflowListItemDTO> getWorkflowListExcelCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessExcelItemCallback<WorkflowListItemDTO>() {

            @Override
            public void call(SXSSFWorkbook pWriter, int pNo, WorkflowListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getWorkflowListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }
                List<Object> values = dtoMapper.getWorkflowListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

    private ProcessItemCallback<Writer, WorkflowListItemDTO> getWorkflowListXmlCb(final Set<ColumnOption> pColumnOptions) {
        return new ProcessXmlItemCallback<WorkflowListItemDTO>() {

            @Override
            public void call(Writer pWriter, int pNo, WorkflowListItemDTO pDto) throws IOException {
                if (pNo == 1) {
                    List<String> values = dtoMapper.getWorkflowListTitles(pColumnOptions);
                    writeRow(pWriter, new ArrayList<>(values), 0);
                }
                List<Object> values = dtoMapper.getWorkflowListValues(pColumnOptions, pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }

//    public void csvToEXCEL(String csvFileName, String excelFileName) throws Exception {
//        checkValidFile(csvFileName);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName)));
//        XSSFWorkbook workBook = new XSSFWorkbook();
//        FileOutputStream writer = new FileOutputStream(new File(excelFileName));
//        XSSFSheet sheet = workBook.createSheet();
//        // add additional needed properties
//        String line = "";
//        int rowNo = 0;
//        while ((line = reader.readLine()) != null) {
//            String[] columns = line.split(DELIMITER);
//            XSSFRow row = sheet.createRow(rowNo);
//            for (int i = 0; i < columns.length; i++) {
//                XSSFCell cell = row.createCell(i);
//                cell.setCellValue(columns[i]);
//            }
//            rowNo++;
//        }
//
//        workBook.write(writer);
//        writer.close();
//    }
//
//    private void checkValidFile(String fileName) {
//        boolean valid = true;
//        try {
//            File f = new File(fileName);
//            if (!f.exists() || f.isDirectory()) {
//                valid = false;
//            }
//        } catch (Exception e) {
//            valid = false;
//        }
//        if (!valid) {
//            System.out.println("File doesn't exist: " + fileName);
//            System.exit(0);
//        }
//    }
//
//    public Map<Integer, String> createVisibleColumnsHashMap(Map<ColumnOption, List<FilterOption>> pOptions) {
//        Map<Integer, String> map = new HashMap<>();
//
//        pOptions.forEach((ColumnOption t, List<FilterOption> u) -> {
//            if (t.isShouldShow()) {
//                String attributeName = t.attributeName;
//                Integer index = t.getIndex();
//
//                map.put(index, attributeName);
//            }
//        });
//
//        return map;
//    }
//
//    public Map<String, String> createVisibleColumnsDisplayNameHashMap(Map<ColumnOption, List<FilterOption>> pOptions) {
//        Map<String, String> map = new HashMap<>();
//
//        pOptions.forEach((ColumnOption t, List<FilterOption> u) -> {
//            if (t.isShouldShow()) {
//                String attributeName = t.attributeName;
//                String displayName = t.getDisplayName();
//
//                map.put(attributeName, displayName);
//            }
//        });
//
//        return map;
//    }
//
//    // just for testing
//    public void showAllHashMapKeyvalues(Map<String, String> h) {
//        if (h != null && !h.isEmpty()) {
//            LOG.log(Level.FINEST, "\n \n ------- MAP IS FILLED WITH FOLLOWING KEYS AND VALUES-------- \n");
//            h.forEach((String key, String value) -> {
//                LOG.log(Level.INFO, "{0} = {1}", new Object[]{key, value});
//            });
//        } else {
//            LOG.log(Level.FINEST, "\n ----- MAP IS NULL OR EMPTY ------ \n");
//        }
//    }

    /*
    private ProcessItemCallback<WorkflowListItemDTO> getWorkflowListCsvCb(final Map<ColumnOption, List<FilterOption>> pOptions) {

        Map<Integer, String> createVisibleColumnsHashMap = createVisibleColumnsHashMap(pOptions);
        Map<String, String> createVisibleColumnsDisplayNameHashMap = createVisibleColumnsDisplayNameHashMap(pOptions);

        return new ProcessItemCallback<WorkflowListItemDTO>() {
            @Override
            public void call(Writer pWriter, int pNo, WorkflowListItemDTO pDto, final Set<String> pVisibleColumns) throws IOException {
                if (pNo == 1) {
                    StringBuilder header = new StringBuilder();
                    createVisibleColumnsHashMap.forEach((Integer t, String u) -> {
//                        SearchListAttribute byKey = WorkflowListAttributes.instance().getByKey(u);
//                        String language_key = byKey.language_key;
                        header.append(createVisibleColumnsDisplayNameHashMap.get(u)).append(DELIMITER);
                    });
                    pWriter.append(header + NEWLINE);
                }

                StringBuilder row = new StringBuilder();
                Map<String, String> workflowListAtrrHashMap = pDto.createWorkflowListAtrrHashMap();
//                showAllHashMapKeyvalues(workflowListAtrrHashMap);

//                pVisibleColumns.forEach((String u) -> {
                createVisibleColumnsHashMap.forEach((Integer t, String u) -> {
                    workflowListAtrrHashMap.forEach((String key, String value) -> {
                        if (u.equals(key)) {
                            if (value == null || value.isEmpty()) {
                                row.append(DELIMITER);
                                return;
                            }
                            String workflowListAttributeNameToBeShown = searchListExportHelper.getWorkflowListAttributeNameToBeShown(key, value);
                            row.append(workflowListAttributeNameToBeShown).append(DELIMITER);
                        }

                    });
                });

                pWriter.append(row + NEWLINE);
                row.setLength(0);
            }
        };
    }
     */
 /*
        private ProcessItemCallback<WorkingListItemDTO> getWorkingListCsvCb(final Map<ColumnOption, List<FilterOption>> pOptions) {

        Map<Integer, String> createVisibleColumnsHashMap = createVisibleColumnsHashMap(pOptions);
        Map<String, String> createVisibleColumnsDisplayNameHashMap = createVisibleColumnsDisplayNameHashMap(pOptions);

        return new ProcessItemCallback<WorkingListItemDTO>() {
            @Override
            public void call(Writer pWriter, int pNo, WorkingListItemDTO pDto, final Set<String> pVisibleColumns) throws IOException {
                if (pNo == 1) {
                    StringBuilder header = new StringBuilder();

//                    pVisibleColumns.forEach((String u) -> {
                    createVisibleColumnsHashMap.forEach((Integer t, String u) -> {
//                        SearchListAttribute byKey = WorkflowListAttributes.instance().getByKey(u);
//                        String language_key = byKey.language_key;
                        header.append(createVisibleColumnsDisplayNameHashMap.get(u)).append(DELIMITER);
                    });
                    pWriter.append(header + NEWLINE);
                }

                StringBuilder row = new StringBuilder();
                Map<String, String> workingListAtrrHashMap = pDto.createWorkingListAtrrHashMap();
//                showAllHashMapKeyvalues(workingListAtrrHashMap);

//                pVisibleColumns.forEach((String u) -> {
                createVisibleColumnsHashMap.forEach((Integer t, String u) -> {
                    workingListAtrrHashMap.forEach((String key, String value) -> {
                        if (u.equals(key)) {
                            if (value == null || value.isEmpty()) {
                                row.append(DELIMITER);
                                return;
                            }
                            String workingListAttributeNameToBeShown = searchListExportHelper.getWorkingListAttributeNameToBeShown(key, value);
                            row.append(workingListAttributeNameToBeShown).append(DELIMITER);
                        }
                    });

                });

                pWriter.append(row + NEWLINE);
                row.setLength(0);
            }
        };
    }
     */
}

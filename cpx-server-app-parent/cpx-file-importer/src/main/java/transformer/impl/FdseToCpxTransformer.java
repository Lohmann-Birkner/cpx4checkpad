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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package transformer.impl;

import static de.lb.cpx.str.utils.StrUtils.floatToMoney;
import static de.lb.cpx.str.utils.StrUtils.parseDate;
import static de.lb.cpx.str.utils.StrUtils.toInt;
import static de.lb.cpx.str.utils.StrUtils.toStr;
import dto.AbstractDto;
import dto.impl.Case;
import dto.impl.Department;
import dto.impl.Diagnose;
import dto.impl.Drug;
import dto.impl.Fee;
import dto.impl.Hauptdiagnose;
import dto.impl.Nebendiagnose;
import dto.impl.Patient;
import dto.impl.Procedure;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import line.AbstractLine;
import module.Fdse;
import module.impl.ImportConfig;
import transformer.impl.fdse.versionhelper.FdseFile;
import transformer.impl.fdse.versionhelper.FdseFiles;
import util.CpxWriter;
import util.FileManager;

/**
 *
 * @author Dirk Niemeier
 */
public class FdseToCpxTransformer extends FileToCpxTransformer<Fdse> {

    private static final Logger LOG = Logger.getLogger(FdseToCpxTransformer.class.getName());

    public static final String DELIMITER = ";";
    public static final String FORMAT_DATETIME = "yyyyMMddHHmm";
    public static final String FORMAT_DATE = "yyyyMMdd";
    public static final String AUF_FILENAME = "EK_Auf.txt";
    public static final String ENT_FILENAME = "EK_Entgelte.txt";
    public static final String FAB_FILENAME = "EK_Fab.txt";
    public static final String FALL_FILENAME = "EK_Fall.txt";
    public static final String ICD_FILENAME = "EK_NICD.txt";
    public static final String OPS_FILENAME = "EK_OPS.txt";
    public static final String MED_FILENAME = "EK_MED.txt";
    private final Map<String, LinePosition> mLinePosition = new HashMap<>();
    private final Map<String, Long> mFirstDepartmentNr = new ConcurrentHashMap<>();
    private final Map<String, String[]> mHauptdiagnose = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Long>> mFachabteilung = new ConcurrentHashMap<>();
    private final Map<String, Date> mAufnahmedatum = new ConcurrentHashMap<>();

    public FdseToCpxTransformer(final ImportConfig<Fdse> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        super(pImportConfig);
    }

    @Override
    public TransformResult start() throws IOException, InstantiationException, IllegalAccessException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException {
        //create readers for files in mInputDirectory, for example...
        //try (final FileManager fileManager = new FileManager(pFile.getAbsolutePath())) {
        //    try (BufferedReader br = fileManager.getBufferedReader()) {
        //        while ((line = br.readLine()) != null) {
        //            //do something with line, e.g. String[] sa = AbstractLine.splitLine(line, ";");
        //        }
        //    }
        //}

        //try (final CpxWriter cpxMgr = CpxWriter.getInstance(getOutputDirectory().getAbsolutePath())) {
        //read file content and write information to file
        //Example for some dummy hospital cases from the scratch
        final FdseFiles fdseFiles = getFileNames(getInputDirectory());
        StringBuilder errors = new StringBuilder();
        StringBuilder warnings = new StringBuilder();
        final Iterator<Map.Entry<String, FdseFile>> it = fdseFiles.iterator();
        while (it.hasNext()) {
            //for (P21File p21File : fileNames) {
            final Map.Entry<String, FdseFile> entry = it.next();
            final FdseFile fdseFile = entry.getValue();
            //Map.Entry<String, Boolean> entry = it.next();
            //final String fileName = p21File.getName();
            //final File file = p21File.file;
            //final boolean mandatory = p21File.isMandatory();
            //final boolean found = p21File.isFound();
            //casePointer.put(fileName, null);
            //mLinePosition.put(fileName, new LinePosition("", "", 0));
            mLinePosition.put(fdseFile.fileName, null);
            //final File file = fileNames.get(fileName);
            //final File file = findFilename(files, fileName);            
            //if (file == null) {
            if (!fdseFile.found) {
                if (fdseFile.mandatory) {
                    //mandatory files
                    if (errors.length() > 0) {
                        errors.append("\r\n");
                    }
                    errors.append("File '" + fdseFile.fileName + "' not found in " + getInputDirectory().getAbsolutePath());
                } else {
                    //optional files
                    if (warnings.length() > 0) {
                        warnings.append("\r\n");
                    }
                    warnings.append("File '" + fdseFile.fileName + "' not found in " + getInputDirectory().getAbsolutePath());
                }
            }
//            } else {
//                fileNames.put(fileName, file);
//            }
        }

        if (warnings.length() > 0) {
            LOG.log(Level.WARNING, "At least one file seems to be missing, but that''s not necessarily a big deal (I will skip it!):\r\n{0}", warnings);
        }
        if (errors.length() > 0) {
            throw new IllegalArgumentException("Detected missing files:\r\n" + errors.toString());
        }

        processFallFile(mCpxMgr, fdseFiles.get(FALL_FILENAME));
        processEntFile(mCpxMgr, fdseFiles.get(ENT_FILENAME));
        processFabFile(mCpxMgr, fdseFiles.get(FAB_FILENAME));
        processIcdFile(mCpxMgr, fdseFiles.get(ICD_FILENAME));
        processOpsFile(mCpxMgr, fdseFiles.get(OPS_FILENAME));
        processMedFile(mCpxMgr, fdseFiles.get(MED_FILENAME));

//        Random rand = new SecureRandom();
//        for (int i = 1; i <= 100; i++) {
//            final String patientNumber = "Patient No. " + (rand.nextInt(49) + 1);
//            final String caseNumber = "Case No. " + i;
//            if (!patientKeyExists(patientNumber)) {
//                Patient patient = new Patient();
//                patient.setPatNr(patientNumber);
//                mCpxMgr.write(patient);
//                patientCounter.incrementAndGet();
//            }
//            Case cs = new Case(patientNumber);
//            cs.setIkz("260510461");
//            cs.setFallNr(caseNumber);
//            cs.setFallart("DRG");
//            cs.setAufnahmedatum(new Date());
//            cs.setAufnahmeanlass("E"); //N
//            cs.setAufnahmegrund1("01");
//            cs.setAufnahmegrund2("01");
//            cs.setAlterInJahren(66);
//            cs.setAlterInTagen(0);
//            cs.setUrlaubstage(0);
//            cs.setVwd(0);
//            cs.setVwdSimuliert(0);
//            mCpxMgr.write(cs);
//
//            Department dep = new Department(cs);
//            dep.setCode("0100");
//            dep.setErbringungsart(Erbringungsart.HA);
//            dep.setVerlegungsdatum(new Date());
//            dep.setEntlassungsdatum(new Date());
//            mCpxMgr.write(dep);
//
//            Hauptdiagnose primIcd = new Hauptdiagnose(dep);
//            primIcd.setCode("F10.2");
//            mCpxMgr.write(primIcd);
//
//            Nebendiagnose sekIcd = new Nebendiagnose(dep);
//            sekIcd.setCode("I49.3");
//            sekIcd.setRefIcd(primIcd);
//            sekIcd.setRefIcdType(RefIcdType.Plus);
//            mCpxMgr.write(sekIcd);
//
//            Fee fee1 = new Fee(cs);
//            fee1.setAnzahl(3);
//            fee1.setEntgeltschluessel("0815");
//            mCpxMgr.write(fee1);
//
//            Fee fee2 = new Fee(cs);
//            fee2.setAnzahl(7);
//            fee2.setEntgeltschluessel("0816");
//            mCpxMgr.write(fee2);
//
//            caseCounter.incrementAndGet();
//        }
//        //}
//
//        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
//        final CountDownLatch executionCompleted = new CountDownLatch(2);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final CountDownLatch executionCompletedPatientFallZusatz = new CountDownLatch(2);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                processPatientFile(mCpxMgr, fdseFiles.get(PATIENT_FILENAME));
//                            } catch (Exception ex) {
//                                exceptions.add(ex);
//                                LOG.log(Level.SEVERE, null, ex);
//                            }
//                            executionCompletedPatientFallZusatz.countDown();
//                        }
//                    }).start();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                processFallZusatzFile(mCpxMgr, fdseFiles.get(FALL_ZUSATZ_FILENAME));
//                            } catch (Exception ex) {
//                                exceptions.add(ex);
//                                LOG.log(Level.SEVERE, null, ex);
//                            }
//                            executionCompletedPatientFallZusatz.countDown();
//                        }
//                    }).start();
//                    executionCompletedPatientFallZusatz.await();
//
//                    processEntFile(mCpxMgr, fdseFiles.get(ENT_FILENAME));
//                    int[] tmp = processFallFile(mCpxMgr, fdseFiles.get(FALL_FILENAME));
//                    patientCounter.set(tmp[0]); //Index 0 -> Number of patients
//                    caseCounter.set(tmp[1]); //Index 1 -> Number of hospital cases
//                } catch (Exception ex) {
//                    exceptions.add(ex);
//                    LOG.log(Level.SEVERE, null, ex);
//                }
//                executionCompleted.countDown();
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    processFabFile(mCpxMgr, p21Files.get(FAB_FILENAME));
////                        for(Map.Entry<String, String> entry: mFirstDepartmentNr2.entrySet()) {
////                           System.out.println(entry.getKey() + " => " + entry.getValue());
////                        }
////                        System.exit(0);
//                    final CountDownLatch executionCompletedIcdOps = new CountDownLatch(2);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                processIcdFile(mCpxMgr, fdseFiles.get(ICD_FILENAME));
//                            } catch (Exception ex) {
//                                exceptions.add(ex);
//                                LOG.log(Level.SEVERE, null, ex);
//                            }
//                            executionCompletedIcdOps.countDown();
//                        }
//                    }).start();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                processOpsFile(mCpxMgr, fdseFiles.get(OPS_FILENAME));
//                            } catch (Exception ex) {
//                                exceptions.add(ex);
//                                LOG.log(Level.SEVERE, null, ex);
//                            }
//                            executionCompletedIcdOps.countDown();
//                        }
//                    }).start();
//                    executionCompletedIcdOps.await();
//                } catch (Exception ex) {
//                    exceptions.add(ex);
//                    LOG.log(Level.SEVERE, null, ex);
//                }
//                executionCompleted.countDown();
//            }
//        }).start();
//        executionCompleted.await();
//        mFirstDepartmentNr.clear();
//        //mCaseKeys.clear();
//        //}
        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
    }

    protected void processEntFile(final CpxWriter pCpxMgr, final FdseFile fdseEntgelteFile) {
        try (final FileManager fileManager = getFallManager(fdseEntgelteFile)) {
            try (BufferedReader br = getReader(fileManager)) {
                if (br == null) {
                    LOG.log(Level.WARNING, "cannot open reader for file: {0}", fdseEntgelteFile.actualFile);
                    return;
                }
//                String headLine = br.readLine(); //skip first line (headline)
                String line;
                int lineCount = 0;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] sa = AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER);
                    Fee fee = importEnt(sa);
                    if (fee != null) {
                        pCpxMgr.write(fee);
//                        patientCount.incrementAndGet();
                    }
                    //lBlockingQueue.add(line + DELIMITER + lineCount);
//                    if (!lBlockingQueue.offer(AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER), 10, TimeUnit.SECONDS)) {
//                        LOG.log(Level.FINEST, "the specified waiting time elapsed before space was available");
//                    }
                }
            } catch (ParseException | IOException ex) {
                exceptions.add(ex);
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void processFabFile(final CpxWriter pCpxMgr, final FdseFile fdseFabFile) {
        try (final FileManager fileManager = getFallManager(fdseFabFile)) {
            try (BufferedReader br = getReader(fileManager)) {
                if (br == null) {
                    LOG.log(Level.WARNING, "cannot open reader for file: {0}", fdseFabFile.actualFile);
                    return;
                }
//                String headLine = br.readLine(); //skip first line (headline)
                String line;
                int lineCount = 0;
                Department prevDep = null;
                int depNr = 0;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] sa = AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER);
                    String caseKey = AbstractDto.buildCaseKey(sa[0], sa[1]);
                    if (prevDep != null && !prevDep.getCaseKey().equalsIgnoreCase(caseKey)) {
                        depNr = 0;
                    }
                    int newDepNr = depNr + 1;
                    Department dep = importFab(sa, newDepNr, prevDep);
                    if (dep != null) {
                        pCpxMgr.write(dep);
                        depNr = newDepNr;
//                        patientCount.incrementAndGet();
                    }
                    prevDep = dep;
                    //lBlockingQueue.add(line + DELIMITER + lineCount);
//                    if (!lBlockingQueue.offer(AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER), 10, TimeUnit.SECONDS)) {
//                        LOG.log(Level.FINEST, "the specified waiting time elapsed before space was available");
//                    }
                }
            } catch (ParseException | IOException ex) {
                exceptions.add(ex);
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void processIcdFile(final CpxWriter pCpxMgr, final FdseFile fdseIcdFile) {
        final Iterator<Map.Entry<String, String[]>> it = mHauptdiagnose.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            final String caseKey = entry.getKey();
            final String[] mainIcd = entry.getValue();
            final String code = mainIcd[0];
            final String lok = mainIcd[1];
            Long depNr = mFirstDepartmentNr.get(caseKey);
            String[] key = AbstractDto.splitKey(caseKey);

            if (depNr == null) {
                LOG.log(Level.SEVERE, "department number is null!");
                continue;
            }

            Hauptdiagnose icd = new Hauptdiagnose(key[0], key[1], depNr);
            icd.setCode(code);
            icd.setLokalisation(lok);
            icd.setTpSource(ICD_FILENAME);
            try {
                //icd.setTpId("Line " + sa[sa.length - 1]);
                pCpxMgr.write(icd);
            } catch (IOException ex) {
                exceptions.add(ex);
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        try (final FileManager fileManager = getFallManager(fdseIcdFile)) {
            try (BufferedReader br = getReader(fileManager)) {
                if (br == null) {
                    LOG.log(Level.WARNING, "cannot open reader for file: {0}", fdseIcdFile.actualFile);
                    return;
                }
//                String headLine = br.readLine(); //skip first line (headline)
                String line;
                int lineCount = 0;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] sa = AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER);
                    Diagnose<?> icd = importIcd(sa);
                    if (icd != null) {
                        pCpxMgr.write(icd);
//                        patientCount.incrementAndGet();
                    }

                    //lBlockingQueue.add(line + DELIMITER + lineCount);
//                    if (!lBlockingQueue.offer(AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER), 10, TimeUnit.SECONDS)) {
//                        LOG.log(Level.FINEST, "the specified waiting time elapsed before space was available");
//                    }
                }
            } catch (IOException ex) {
                exceptions.add(ex);
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void processMedFile(final CpxWriter pCpxMgr, final FdseFile fdseMedFile) {
        if (!fdseMedFile.found && !fdseMedFile.mandatory) {
            LOG.log(Level.WARNING, MED_FILENAME + " was not passed. I cannot proceed with it and so I will not import additional case information. Is it okay my friend?");
            return;
        }
        try (final FileManager fileManager = getFallManager(fdseMedFile)) {
            try (BufferedReader br = getReader(fileManager)) {
                if (br == null) {
                    LOG.log(Level.WARNING, "cannot open reader for file: {0}", fdseMedFile.actualFile);
                    return;
                }
//                String headLine = br.readLine(); //skip first line (headline)
                String line;
                int lineCount = 0;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] sa = AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER);
                    Drug drug = importMed(sa);
                    if (drug != null) {
                        pCpxMgr.write(drug);
//                        patientCount.incrementAndGet();
                    }
                    //lBlockingQueue.add(line + DELIMITER + lineCount);
//                    if (!lBlockingQueue.offer(AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER), 10, TimeUnit.SECONDS)) {
//                        LOG.log(Level.FINEST, "the specified waiting time elapsed before space was available");
//                    }
                }
            } catch (ParseException | IOException ex) {
                exceptions.add(ex);
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void processOpsFile(final CpxWriter pCpxMgr, final FdseFile fdseOpsFile) {
        try (final FileManager fileManager = getFallManager(fdseOpsFile)) {
            try (BufferedReader br = getReader(fileManager)) {
                if (br == null) {
                    LOG.log(Level.WARNING, "cannot open reader for file: {0}", fdseOpsFile.actualFile);
                    return;
                }
//                String headLine = br.readLine(); //skip first line (headline)
                String line;
                int lineCount = 0;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] sa = AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER);
                    Procedure proc = importOps(sa);
                    if (proc != null) {
                        pCpxMgr.write(proc);
//                        patientCount.incrementAndGet();
                    }
                    //lBlockingQueue.add(line + DELIMITER + lineCount);
//                    if (!lBlockingQueue.offer(AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER), 10, TimeUnit.SECONDS)) {
//                        LOG.log(Level.FINEST, "the specified waiting time elapsed before space was available");
//                    }
                }
            } catch (ParseException | IOException ex) {
                exceptions.add(ex);
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    protected int[] processFallFile(final CpxWriter pCpxMgr, final FdseFile fdseFallFile) {
        final AtomicInteger patientCount = new AtomicInteger();
        final AtomicInteger caseCount = new AtomicInteger();
//        final int thread = 1; //only 1 thread is mandatory to import Patient.csv
//        try (final FileManager fileManagerPat = getFallManager(p21PatFile);  BufferedReader brPat = getReader(fileManagerPat); final FileManager fileManagerFallZusatz = getFallManager(p21FallZusatzFile);  BufferedReader brFallZusatz = getReader(fileManagerFallZusatz)) {
//            String headLinePat = brPat == null ? null : brPat.readLine(); //skip first line (headline)) 
//            String headLineFallZusatz = brFallZusatz == null ? null : brFallZusatz.readLine(); //skip first line (headline)) 
//        final AtomicInteger patientLine = new AtomicInteger(1);
        try (final FileManager fileManager = getFallManager(fdseFallFile)) {
            try (BufferedReader br = getReader(fileManager)) {
                if (br == null) {
                    LOG.log(Level.WARNING, "cannot open reader for file: {0}", fdseFallFile.actualFile);
                    return new int[]{patientCount.get(), caseCount.get()};
                }
//                String headLine = br.readLine(); //skip first line (headline)
                String line;
                int lineCount = 0;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] sa = AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER);
                    Patient patient = importPatient(sa);
                    if (patient != null) {
                        pCpxMgr.write(patient);
                        patientCount.incrementAndGet();
                    }
                    Case cs = importFall(sa);
                    if (cs != null) {
                        if (cs.getAufnahmedatum() == null) {
                            LOG.log(Level.WARNING, "Case has no admission date, so it will be ignored: {0}", cs.getCaseKey());
                        } else {
                            pCpxMgr.write(cs);
                            //mCaseKeys.add(cs.getCaseKey());
                            caseCount.incrementAndGet();
                        }
                    }
                    //lBlockingQueue.add(line + DELIMITER + lineCount);
//                    if (!lBlockingQueue.offer(AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER), 10, TimeUnit.SECONDS)) {
//                        LOG.log(Level.FINEST, "the specified waiting time elapsed before space was available");
//                    }
                }
            } catch (ParseException | IOException ex) {
                exceptions.add(ex);
                LOG.log(Level.SEVERE, null, ex);
            }
        }
//        processFile(pCpxMgr, fdseFallFile, new P21Process() {
//            @Override
//            public void process(final CpxWriter pCpxMgr, final String[] sa) throws Exception {
////                    int patLineCount = patientLine.incrementAndGet();
////                    int fallZusatzLineCount = patientLine.incrementAndGet();
//
////                    String linePat = brPat == null ? null : brPat.readLine();
////                    String[] saPat = linePat == null ? null : AbstractLine.splitLine(linePat + DELIMITER + patLineCount, DELIMITER);
//                Patient patient = importPatient(sa);
//                if (patient != null) {
//                    pCpxMgr.write(patient);
//                    patientCount.incrementAndGet();
//                }
//
////                    String lineFallZusatz = brFallZusatz == null ? null : brFallZusatz.readLine();
////                    String[] saFallZusatz = lineFallZusatz == null ? null : AbstractLine.splitLine(lineFallZusatz + DELIMITER + fallZusatzLineCount, DELIMITER);
//                Case cs = importFall(sa);
//                if (cs != null) {
//                    if (cs.getAufnahmedatum() == null) {
//                        LOG.log(Level.WARNING, "Case has no admission date, so it will be ignored: " + cs.getCaseKey());
//                    } else {
//                        pCpxMgr.write(cs);
//                        //mCaseKeys.add(cs.getCaseKey());
//                        caseCount.incrementAndGet();
//                    }
//                }
//            }
//        }, thread);
        return new int[]{patientCount.get(), caseCount.get()};
    }

    private FileManager getFallManager(final FdseFile pP21File) {
        final FileManager fileManager = pP21File.found || pP21File.mandatory ? new FileManager(pP21File.actualFile.getAbsolutePath()) : null;
        return fileManager;
    }

    private BufferedReader getReader(final FileManager pFileManager) throws IOException {
        final BufferedReader br = pFileManager != null ? pFileManager.getBufferedReader() : null;
        return br;
    }

    protected Fee importEnt(final String[] sa) throws ParseException {
        if (sa[0].startsWith("KrHs")) {
            //is headline
            return null;
        }
        final String entgeltschluessel = sa[2].trim();
        final int max_length = 8;
        if (entgeltschluessel.length() > max_length) {
            String caseKey = AbstractDto.buildCaseKey(sa[0], sa[1]);
            LOG.log(Level.WARNING, "Entgeltschlüssel in case {0} is too long (maximum of {1} chars is exceeded: {2}", new Object[]{caseKey, max_length, entgeltschluessel});
            return null;
        }
        Fee fee = new Fee(sa[0], sa[1]);
        fee.setEntgeltschluessel(entgeltschluessel);
        fee.setBetrag(Float.valueOf(floatToMoney(sa[3])));
        fee.setVon(parseDate(sa[4], FORMAT_DATE));
        fee.setBis(parseDate(sa[5], FORMAT_DATE));
        fee.setAnzahl(toInt(sa[6]));
        fee.setTob(toInt(sa[7], 0));
        //patient.setVersichertenstatus(sa[27]);
        fee.setTpSource(ENT_FILENAME);
        fee.setTpId("Line " + sa[sa.length - 1]);
        return fee;
    }

    protected Department importFab(final String[] sa, final int pDepNr, final Department pPrevDep) throws ParseException {
        if (sa[0].startsWith("KrHs")) {
            //is headline
            return null;
        }
        Department dep = new Department(sa[0], sa[1]);
//        if (dep.getFallNr().equalsIgnoreCase("F3839803")) {
//            LOG.log(Level.WARNING, "TEST");
//        }
        dep.setCode(sa[2]);
        final Date aufnahmedatum;
        if (pDepNr == 1) {
            aufnahmedatum = mAufnahmedatum.get(dep.getCaseKey());
        } else {
            aufnahmedatum = pPrevDep.getEntlassungsdatum();
        }
        dep.setVerlegungsdatum(aufnahmedatum);
        dep.setEntlassungsdatum(parseDate(sa[3] + sa[4], FORMAT_DATETIME));
        dep.setTpSource(FAB_FILENAME);
        dep.setTpId("Line " + sa[sa.length - 1]);
        Map<String, Long> map = mFachabteilung.get(dep.getCaseKey());
        if (map == null) {
            map = new HashMap<>();
            mFachabteilung.put(dep.getCaseKey(), map);
        }
//        Map<Integer, Long> map2 = map.get(dep.getCode());
//        if (map2 == null) {
//            map = new HashMap<>();
//            mFachabteilung.put(dep.getCaseKey(), map);
//        }
        map.put(dep.getCode(), dep.getNr());
        if (pDepNr == 1) {
            mFirstDepartmentNr.put(dep.getCaseKey(), dep.getNr());
        }
        return dep;
    }

    protected Diagnose<? extends Diagnose<?>> importIcd(final String[] sa) {
        if (sa[0].startsWith("KrHs")) {
            //is headline
            return null;
        }
        String caseKey = AbstractDto.buildCaseKey(sa[0], sa[1]);
//        final String fabCode = 
        //final Integer nr = toInt(sa[2]);
//        Long depNr = mFachabteilung.get(caseKey).get(nr);
//        if (depNr == null) {
//            LOG.log(Level.WARNING, "illegal department number found for icd case key {0}: {1}", new Object[]{caseKey, nr});
//            depNr = mFachabteilung.get(caseKey).get(1); //replace department!
//        }
        final long depNr = mFirstDepartmentNr.get(caseKey);
//        if (depNr == null) {
//            LOG.log(Level.WARNING, "illegal department number found for icd case key {0}", new Object[]{caseKey, nr});
//            depNr = mFachabteilung.get(caseKey).get(1); //replace department!
//        }
        Nebendiagnose icd = new Nebendiagnose(sa[0], sa[1], depNr);
        icd.setCode(sa[3]);
        icd.setLokalisation(sa[4]);
        icd.setTpSource(ICD_FILENAME);
        icd.setTpId("Line " + sa[sa.length - 1]);
        return icd;
    }

    protected Drug importMed(final String[] sa) throws ParseException {
        if (sa[0].startsWith("KrHs")) {
            //is headline
            return null;
        }
        //String caseKey = AbstractDto.buildCaseKey(sa[0], sa[1]);
//        //final Integer nr = toInt(sa[2]);
//        final String fabCode = sa[6];
        Drug drug = new Drug(sa[0], sa[1], sa[2]);
        drug.setPicNr(sa[4]);
        drug.setFaktor(toDouble(sa[5]));
        drug.setBrutto(Double.parseDouble(floatToMoney(sa[6])));
        drug.setPzn(sa[7]);
        drug.setAtc(sa[8]);
        drug.setGenerika(toInt(sa[9]));
        drug.setDf(sa[10]);
        drug.setPackungsgr(sa[11]);
        drug.setNormgr(toInt(sa[12]));
        drug.setArztnr(toInt(sa[14]));
        drug.setBruttoGesamt(Double.parseDouble(floatToMoney(sa[15])));
        drug.setApoik(sa[16]);
        drug.setBmg(toInt(sa[17]));
        drug.setUnfk(toInt(sa[18]));
        drug.setVerord(parseDate(sa[19], FORMAT_DATE));
        drug.setAbg(parseDate(sa[20], FORMAT_DATE));
        return drug;
    }

    protected Procedure importOps(final String[] sa) throws ParseException {
        if (sa[0].startsWith("KrHs")) {
            //is headline
            return null;
        }
        String caseKey = AbstractDto.buildCaseKey(sa[0], sa[1]);
        //final Integer nr = toInt(sa[2]);
        final String fabCode = sa[6];
        final Map<String, Long> map = mFachabteilung.get(caseKey);
        if (map == null) {
            LOG.log(Level.WARNING, "unknown case found for ops: {0}", caseKey);
            return null;
        }
        final Long depNr = map.get(fabCode);
        if (depNr == null) {
            LOG.log(Level.WARNING, "No department number found for case: {0}", caseKey);
            return null;
        }
//        if (depNr == null) {
//            LOG.log(Level.WARNING, "illegal department number found for ops case key {0}: {1}", new Object[]{caseKey, fabCode});
//            depNr = mFachabteilung.get(caseKey); //replace department!
//        }
        Procedure proc = new Procedure(sa[0], sa[1], depNr);
        proc.setCode(sa[3]);
        proc.setDatum(parseDate(sa[7], FORMAT_DATE));
        proc.setLokalisation(sa[8]);
        return proc;
    }

    protected Patient importPatient(final String[] sa) {
        String patKey = AbstractDto.buildPatientKey(sa[3]);
        if (patientKeyExists(patKey)) {
            return null;
        }
        Patient patient = new Patient();
        patient.setPatNr(patKey);
        patient.setGeschlecht(sa[4]);
        patient.setPlz(sa[7]);
        //patient.setVersichertenstatus(sa[27]);
        patient.setTpSource(FALL_FILENAME);
        patient.setTpId("Line " + sa[sa.length - 1]);
        return patient;
    }

//    protected Patient importPatient(final String[] sa) throws ParseException {
//        String patKey = AbstractDto.buildPatientKey(sa[19 + fall_shift + wohnort_shift]);
//        //if (!mPatienten.add(patKey)) {
//        //  return null;
//        //}
//        if (patientKeyExists(patKey)) {
//            return null;
//        }
//        Patient patient = new Patient();
//        patient.setPatNr(patKey);
//        if (fall_shift > 0) {
//            patient.setVersichertennr(sa[4]);
//            //patient.setXXX(sa[5]);
//        }
//        if (wohnort_shift > 0) {
//            patient.setOrt(sa[8 + fall_shift + wohnort_shift]);
//        }
//        patient.setKasse(sa[4 + fall_shift]);
////        String jahr = sa[5 + fall_shift];
////        String monat = sa[6 + fall_shift];
////        patient.setGeburtsdatum(createDate(jahr, monat));
//        patient.setGeschlecht(sa[7 + fall_shift].isEmpty() ? Geschlecht.U : Geschlecht.findByValue(sa[7 + fall_shift]));
//        patient.setPlz(sa[8 + fall_shift]);
//        patient.setTpSource(FALL_FILENAME);
//        patient.setTpId("Line " + sa[sa.length - 1]);
//
//        final String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
//        final String[] saPat = mPatienten.get(caseKey);
//        //final String patCaseKey = saPat == null ? null : AbstractDto.buildCaseKey(saPat[0], saPat[2]);
//        if (saPat != null) {
//            //read additional information from Patient.csv
//            Geschlecht geschlecht = saPat[2].isEmpty() ? null : Geschlecht.findByValue(saPat[2]);
//            if (geschlecht != null) {
//                patient.setGeschlecht(geschlecht);
//            }
//            final String geburtsDatumVal = saPat[3];
//            final Date geburtsdatum;
//            if (geburtsDatumVal.length() <= 8) {
//                geburtsdatum = parseDate(saPat[3], FORMAT_DATE);
//            } else {
//                //parse additional birth time
//                geburtsdatum = parseDate(saPat[3], FORMAT_DATETIME);
//            }
//            if (geburtsdatum != null) {
//                patient.setGeburtsdatum(geburtsdatum);
//            }
//            String name = toStr(saPat[0]);
//            if (!name.isEmpty()) {
//                patient.setNachname(name);
//            }
//            String vorname = toStr(saPat[1]);
//            if (!vorname.isEmpty()) {
//                patient.setVorname(vorname);
//            }
//        }
//        //mPatienten.add(patKey);
//        return patient;
//    }
    protected Case importFall(final String[] sa) throws ParseException {
        if (sa[0].startsWith("KrHs")) {
            //is headline
            return null;
        }
        String patKey = AbstractDto.buildPatientKey(sa[3]);
        Case cs = new Case(patKey);
        cs.setIkz(sa[0]);
        cs.setFallNr(sa[1]);
//        if (cs.getFallNr().equalsIgnoreCase("F3839803")) {
//            LOG.log(Level.WARNING, "TEST");
//        }
        cs.setGeschlecht(sa[4]);
        cs.setAlterInJahren(toInt(sa[5], 0));
        cs.setAlterInTagen(toInt(sa[6], 0));
        cs.setAufnahmedatum(parseDate(sa[8] + sa[9], FORMAT_DATETIME));
        cs.setAufnahmegrund1(sa[10]);
        cs.setAufnahmegrund2(sa[11]);
        cs.setAufnahmeanlass("E");
        cs.setGewicht(toInt(sa[14]));
        cs.setEntlassungsdatum(parseDate(sa[15] + sa[16], FORMAT_DATETIME));
        cs.setEntlassungsgrund12(sa[17].substring(0, 2));
        cs.setEntlassungsgrund3(sa[17].substring(2));
        cs.setBeatmungsstunden(toInt(sa[20]));
        cs.setFallart("DRG");
        cs.setUrlaubstage(0);
        cs.setVwd(0);
        //cs.setVwdIntensiv(0);
        cs.setVwdSimuliert(0);
        mHauptdiagnose.put(cs.getCaseKey(), new String[]{sa[18], sa[19]});
        mAufnahmedatum.put(cs.getCaseKey(), cs.getAufnahmedatum());
        return cs;
    }

//    protected Case importFall(final String[] sa) throws ParseException {
//        //Map<String, Patient> patienten = new HashMap<>();
//        //List<String[]> lines = entry.getValue();
//        //String caseKey = entry.getKey();
//        //List<String[]> lines = mFileAccess.get(FALL_FILENAME);
//        String patKey = AbstractDto.buildPatientKey(sa[19 + fall_shift + wohnort_shift]);
//        //Patient patient = mPatienten.get(patKey);
//
//        Case cs = new Case(patKey);
//        cs.setIkz(sa[0]);
//        cs.setFallNr(sa[3]);
//        cs.setGeschlecht(sa[7 + fall_shift].isEmpty() ? Geschlecht.U : Geschlecht.findByValue(sa[7 + fall_shift]));
//        cs.setAufnahmedatum(sa[9 + fall_shift + wohnort_shift].isEmpty() ? null : parseDate(sa[9 + fall_shift + wohnort_shift], FORMAT_DATETIME));
//        String aufnahmeanlass = sa[10 + fall_shift + wohnort_shift];
//        if (aufnahmeanlass.isEmpty()) {
//            LOG.log(Level.WARNING, "No admission cause found for hospital case " + cs.getCaseKey() + ". Will use default value (E - Einweisung durch Arzt) instead!");
//            aufnahmeanlass = "E"; //Einweisung durch Arzt
//        }
//        cs.setAufnahmeanlass(aufnahmeanlass);
//
//        String aufnahmegrund = toStr(sa[11 + fall_shift + wohnort_shift]);
//        if (aufnahmegrund.length() == 1 || aufnahmegrund.length() == 3) {
//            aufnahmegrund = "0" + aufnahmegrund;
//        }
//        String aufnahmegrund1 = (aufnahmegrund.length() >= 2) ? aufnahmegrund.substring(0, 2) : "";
//        String aufnahmegrund2 = (aufnahmegrund.length() >= 4) ? aufnahmegrund.substring(2, 4) : "01"; //01 = Regular Case
//
//        if (aufnahmegrund1.isEmpty()) {
//            LOG.log(Level.WARNING, "No admission reason 12 found for hospital case " + cs.getCaseKey() + ". Will use default value (01 - Krankenhausbehandlung, vollstationär) instead!");
//            aufnahmegrund1 = "01"; //Krankenhausbehandlung, vollstationär AGE: müsste es nicht 09 sein, 
//        }
//
//        cs.setAufnahmegrund1(aufnahmegrund1);
//        cs.setAufnahmegrund2(aufnahmegrund2);
//        String gewicht = sa[14 + fall_shift + wohnort_shift];
//        gewicht = gewicht.replace(".0", "").trim();
//        //cs.setGewicht(sa[14 + FALL_SHIFT].isEmpty()?null:Integer.valueOf(sa[14 + FALL_SHIFT]));
//        cs.setGewicht(gewicht.isEmpty() ? 0 : Integer.valueOf(gewicht));
//        cs.setEntlassungsdatum(sa[15 + fall_shift + wohnort_shift].isEmpty() ? null : parseDate(sa[15 + fall_shift + wohnort_shift], FORMAT_DATETIME));
//
//        String entlassungsgrund = toStr(sa[16 + fall_shift + wohnort_shift]);
//        if (entlassungsgrund.length() == 2) {
//            entlassungsgrund = "0" + entlassungsgrund;
//        }
//        String entlassungsgrund1 = (entlassungsgrund.length() >= 3) ? entlassungsgrund.substring(0, 2) : "";
//        String entlassungsgrund2 = (entlassungsgrund.length() >= 3) ? entlassungsgrund.substring(2, 3) : "";
//
//        cs.setEntlassungsgrund12(entlassungsgrund1);
//        cs.setEntlassungsgrund3(entlassungsgrund2);
//        cs.setAlterInTagen(sa[17 + fall_shift + wohnort_shift].isEmpty() ? 0 : Integer.valueOf(sa[17 + fall_shift + wohnort_shift]));
//        cs.setAlterInJahren(sa[18 + fall_shift + wohnort_shift].isEmpty() ? 0 : Integer.valueOf(sa[18 + fall_shift + wohnort_shift]));
//        cs.setBeatmungsstunden(sa[21 + fall_shift + wohnort_shift].isEmpty() ? 0 : Integer.valueOf(sa[21 + fall_shift + wohnort_shift]));
//        //cs.setUrlaubstage((sa[28 + FALL_SHIFT].isEmpty()?0:Integer.valueOf(sa[28 + FALL_SHIFT + WOHNORT_SHIFT])) //Beurlaubungstage-PSY+Belegungstage-in-anderem-Entgeltbereich
//        //                + (sa[27 + FALL_SHIFT].isEmpty()?0:Integer.valueOf(sa[27 + FALL_SHIFT + WOHNORT_SHIFT]))); //TODO Klären
//        //cs.setEntgeltbereich(???);
//        Integer tob = mTobMap.get(cs.getCaseKey());
//        cs.setUrlaubstage(tob == null ? 0 : tob);
//        //cs.setVwd((daysBetween(cs.getAufnahmedatum(), cs.getEntlassungsdatum())) - cs.getUrlaubstage()); //Entlassungsdatum - Aufnahmedatum
//        cs.setVwd(0); //Entlassungsdatum - Aufnahmedatum (is calculated by Grouper)
//        Integer vwdIntensiv = 0;
//        if (p21Year >= 2019) {
//            vwdIntensiv = sa[33].isEmpty() ? 0 : Integer.valueOf(sa[33]);
//        }
//        cs.setVwdIntensiv(vwdIntensiv);
//        cs.setVwdSimuliert(0); //TODO
//        cs.setFallart(sa[2]);
//        if (cs.getFallart().isEmpty()) {
//            cs.setFallart("DRG"); //Sometimes there are cases without a given case type, so how can I handle this?!
//        }
//        if (fall_shift > 0) {
//            cs.setVersichertennr(sa[4]);
//            //patient.setXXX(sa[5]);
//        }
//        cs.setKasse(sa[4 + fall_shift]);
//        cs.setTpSource(FALL_FILENAME);
//        cs.setTpId("Line " + sa[sa.length - 1]);
//
////        final String fallZusatzCaseKey = saFallZusatz == null ? null : AbstractDto.buildCaseKey(saFallZusatz[0], saFallZusatz[1]);
//        final String caseKey = cs.getCaseKey();
//        final String[] saFallZusatz = mFallZusatz.get(caseKey);
//        if (saFallZusatz != null) {
////        if (fallZusatzCaseKey != null) {
////            final String caseKey = cs.getCaseKey(); //AbstractDto.buildCaseKey(sa[0], sa[3]);
////            if (caseKey.equalsIgnoreCase(fallZusatzCaseKey)) {
//            Integer vwdIntensiv2 = sa[0].isEmpty() ? null : Integer.valueOf(saFallZusatz[0]);
//            if (vwdIntensiv2 != null) {
//                cs.setVwdIntensiv(vwdIntensiv2);
//            }
//            String fallInfo = toStr(saFallZusatz[1]);
//            if (!fallInfo.isEmpty()) {
//                cs.setString1(fallInfo);
//            }
//        }
//
//        return cs;
//    }
//    protected void processFile(final CpxWriter pCpxMgr, final FdseFile pFile, final P21Process pP21Process) throws InterruptedException {
//        processFile(pCpxMgr, pFile, pP21Process, mThreadCount);
//    }
//
//    protected void processFile(final CpxWriter pCpxMgr, final FdseFile pFile, final P21Process pP21Process, final int pThreadCount) throws InterruptedException {
//        try (final FileManager fileManager = getFallManager(pFile)) {
//            if (fileManager == null) {
//                LOG.log(Level.WARNING, "cannot open file manager for file: {0}", pFile.actualFile);
//                return;
//            }
//            final String fileName = fileManager.getFile().getName();
//            LOG.log(Level.INFO, "Start reading from file " + fileName);
//            final AtomicBoolean readingFinished = new AtomicBoolean(false);
//            //Map<String, List<String[]>> linesMap = new HashMap<>(0);
//            //final ExecutorService lScheduler = Executors.newFixedThreadPool(mThreadCount);
//            final CountDownLatch executionCompleted = new CountDownLatch(pThreadCount + 1);
//            final BlockingQueue<String[]> lBlockingQueue = new LinkedBlockingQueue<>(5000);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try (BufferedReader br = getReader(fileManager)) {
//                        if (br == null) {
//                            LOG.log(Level.WARNING, "cannot open reader for file: {0}", pFile.actualFile);
//                            return;
//                        }
//                        String headLine = br.readLine(); //skip first line (headline)
//                        String line;
//                        int lineCount = 1;
//                        while ((line = br.readLine()) != null) {
//                            lineCount++;
//                            line = line.trim();
//                            if (line.isEmpty()) {
//                                continue;
//                            }
//                            //lBlockingQueue.add(line + DELIMITER + lineCount);
//                            if (!lBlockingQueue.offer(AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER), 10, TimeUnit.SECONDS)) {
//                                LOG.log(Level.FINEST, "the specified waiting time elapsed before space was available");
//                            }
//                        }
//                        readingFinished.set(true);
//                    } catch (InterruptedException | IOException ex) {
//                        exceptions.add(ex);
//                        LOG.log(Level.SEVERE, null, ex);
//                        if (ex instanceof InterruptedException) {
//                            Thread.currentThread().interrupt();
//                        }
//                    }
//                    executionCompleted.countDown();
//                }
//            }).start();
//
//            final AtomicInteger counter = new AtomicInteger(0);
//            for (int i = 1; i <= pThreadCount; i++) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String[] sa = null;
//                        try {
//                            //while(!readingFinished.get() || (sa = lBlockingQueue.poll()) != null) {
//                            while (true) {
//                                while ((sa = lBlockingQueue.poll()) != null) {
//                                    pP21Process.process(pCpxMgr, sa);
//                                    int count = counter.incrementAndGet();
//                                    //int lineCount = sa[sa.length - 1];
//                                    if (count % 100000 == 0) {
//                                        LOG.log(Level.INFO, fileName + ": Count " + count);
//                                    }
//                                }
//                                if (readingFinished.get()) {
//                                    break;
//                                }
//                                Thread.sleep(1000L);
//                            }
//                        } catch (Exception ex) {
//                            int lineNo = getLine(sa);
//                            String msg = "Error in " + pFile.actualFile.getAbsoluteFile() + ", Line " + lineNo + "\r\n" + String.join(";", sa) + "\r\n" + ex.getMessage();
//                            Exception newEx = new IllegalArgumentException(msg, ex);
//                            exceptions.add(newEx);
//                            LOG.log(Level.SEVERE, msg, newEx);
//                        }
//                        executionCompleted.countDown();
//                    }
//                }).start();
//            }
//            executionCompleted.await();
//            LOG.log(Level.INFO, "Finished reading from file " + fileName);
//            //return counter.get();
//        }
//    }
    public static Double toDouble(final String pValue) {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return null;
        }
        return Double.parseDouble(value.replace(",", "."));
    }

    public static Map<String, Boolean> getInputFilenames() {
        final Map<String, Boolean> files = new HashMap<>();
        //false = optional, true = mandatory
        files.put(AUF_FILENAME, false);
        files.put(ENT_FILENAME, true);
        files.put(FAB_FILENAME, true);
        files.put(FALL_FILENAME, true);
        files.put(ICD_FILENAME, true);
        files.put(OPS_FILENAME, true);
        files.put(MED_FILENAME, false);
        return files;
        /*
    String[] files = new String[] {
    AUF_FILENAME,
    ENT_FILENAME,
    FAB_FILENAME,
    ICD_FILENAME,
    OPS_FILENAME
    };
    return files;
         */
    }

    public static FdseFiles getFileNames(final File pInputDirectory) {
        List<File> files = getDirectoryFiles(pInputDirectory);
        if (files.isEmpty()) {
            throw new IllegalArgumentException("Directory has no input files: " + pInputDirectory.getAbsolutePath());
        }
        return getFileNames(files);
    }

    public static FdseFiles getFileNames(final List<File> pFiles) {
        if (pFiles == null || pFiles.isEmpty()) {
            throw new IllegalArgumentException("no input files passed!");
        }
        final Map<String, FdseFile> fileNames = new HashMap<>();
        Iterator<Map.Entry<String, Boolean>> it = getInputFilenames().entrySet().iterator();
        final List<File> existingFiles = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<String, Boolean> entry = it.next();
            String fileName = entry.getKey();
            Boolean mandatory = entry.getValue();
            //casePointer.put(fileName, null);
            //mLinePosition.put(fileName, new LinePosition("", "", 0));
            final File file = findFilename(pFiles, fileName);
            //final boolean found = file != null;
            final FdseFile fdseFile = new FdseFile(fileName, file, mandatory == null ? false : mandatory);
            fileNames.put(fileName, fdseFile);
            if (fdseFile.found) {
                existingFiles.add(file);
            }
        }
//        final FdseVersionHelper p21VersionHelper = new FdseVersionHelper();
//        FdseVersion p21Version = null;
//        try {
//            p21Version = p21VersionHelper.getDatasetVersion(existingFiles);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot detect P21 version", ex);
//        }

        final List<File> inputDirectories = getFileParents(pFiles);

        final File inputDirectory;
        if (inputDirectories.size() == 1) {
            inputDirectory = inputDirectories.iterator().next();
        } else {
            inputDirectory = null;
        }
        //= pFiles.iterator().next().getParentFile();
        final FdseFiles fdseFiles = new FdseFiles(inputDirectory, fileNames);

        final StringBuilder sb = new StringBuilder();
//        sb.append("\n      -> P21 Version: " + p21Files.getP21Year());
        for (Map.Entry<String, FdseFile> entry : fdseFiles.entrySet()) {
            final FdseFile fdseFile = entry.getValue();
            sb.append("\n      -> " + fdseFile.toString());
        }
        LOG.log(Level.INFO, "Result of FDSE directory analysis in {0}: {1}", new Object[]{fdseFiles.getFdseDirectory() == null ? "" : fdseFiles.getFdseDirectory().getAbsolutePath(), sb});

        return fdseFiles;
    }

    private static List<File> getFileParents(final List<File> pFiles) {
        final List<File> inputDirectories = new ArrayList<>();
        for (File file : pFiles) {
            if (file == null) {
                continue;
            }
            boolean exists = false;
            File parent = file.getParentFile();
            for (File parentFile : inputDirectories) {
                try {
                    //if (parentFile.getAbsolutePath().equalsIgnoreCase(parent.getAbsolutePath())) {
                    if (Files.isSameFile(parentFile.toPath(), parent.toPath())) {
                        exists = true;
                        break;
                    }
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
            if (!exists) {
                inputDirectories.add(parent);
            }
        }
        return inputDirectories;
    }

}

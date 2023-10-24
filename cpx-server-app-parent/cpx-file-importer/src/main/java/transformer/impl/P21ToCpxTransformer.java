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

import de.lb.cpx.shared.p21util.P21Version;
import de.lb.cpx.str.utils.StrUtils;
import static de.lb.cpx.str.utils.StrUtils.*;
import dto.AbstractDto;
import dto.impl.Case;
import dto.impl.Department;
import dto.impl.Diagnose;
import dto.impl.Fee;
import dto.impl.Hauptdiagnose;
import dto.impl.Nebendiagnose;
import dto.impl.Patient;
import dto.impl.Procedure;
import dto.types.Erbringungsart;
import dto.types.Geschlecht;
import dto.types.IcdResult;
import dto.types.Lokalisation;
import dto.types.RefIcdType;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import line.AbstractLine;
import module.P21;
import module.impl.ImportConfig;
import transformer.impl.p21.versionhelper.P21File;
import transformer.impl.p21.versionhelper.P21Files;
import transformer.impl.p21.versionhelper.P21VersionHelper;
import util.CpxWriter;
import util.FileManager;

/**
 *
 * @author Dirk Niemeier
 */
public class P21ToCpxTransformer extends FileToCpxTransformer<P21> {

    private static final Logger LOG = Logger.getLogger(P21ToCpxTransformer.class.getName());

    public static final String DELIMITER = ";";
    public static final String FORMAT_DATETIME = "yyyyMMddHHmm";
    public static final String FORMAT_DATE = "yyyyMMdd";
    public static final String ENT_FILENAME = "Entgelte.csv";
    public static final String FAB_FILENAME = "FAB.csv";
    public static final String FALL_FILENAME = "FALL.csv";
    public static final String ICD_FILENAME = "ICD.csv";
    //final public static String INFO_FILENAME = "INFO.csv";
    public static final String OPS_FILENAME = "OPS.csv";
    public static final String INFO_FILENAME = "INFO.csv";
    public static final String PATIENT_FILENAME = "Patient.csv";
    public static final String FALL_ZUSATZ_FILENAME = "FALL_ZUSATZ.csv";
    //final public static String INFO_FILENAME = "INFO.csv";

    private final int mThreadCount = Runtime.getRuntime().availableProcessors();

//    private final Set<String> mPatienten = new HashSet<>();
    private final Map<String, Long> mFirstDepartmentNr = new ConcurrentHashMap<>();
//    private final Map<String, String> mFirstDepartmentNr2 = new ConcurrentHashMap<>();
    //private final Set<String> mCaseKeys = new HashSet<>();
    private final Map<String, Integer> mTobMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> mTobMdMap = new ConcurrentHashMap<>();
    private final Map<String, String[]> mPatienten = new ConcurrentHashMap<>();
    private final Map<String, String[]> mFallZusatz = new ConcurrentHashMap<>();

    //private final Map<String, Map<String, List<String[]>>> mFileAccess = new HashMap<>();
    //private final BlockingQueue<String> mCaseKeyQueue = new LinkedBlockingQueue<>();
    //AtomicInteger mCaseCounter = new AtomicInteger(0);
    //private int mCaseCount = 0;
    protected P21Version p21Version = null;
    protected int p21Year = 0;
    protected String versionskennung = "";
    private int fall_shift = 0;
    private int wohnort_shift = 0;
    private int standort_shift = 0;

//    private BufferedReader mCaseReader;
//    private BufferedReader mFabReader;
//    private BufferedReader mIcdReader;
//    private BufferedReader mOpsReader;
//    private BufferedReader mFeeReader;
    private final Map<String, LinePosition> mLinePosition = new HashMap<>();
    //private HashMap<String, Integer> mLineCounter = new HashMap<>();
    //final Map<String, String> mCasePointer = new HashMap<>();
    //final Map<String, String> mLastLine = new HashMap<>();

    public P21ToCpxTransformer(final ImportConfig<P21> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, SQLException {
        super(pImportConfig);
    }

    public static Date createDate(final String pYear) {
        return createDate(pYear, null, null);
    }

    public static Date createDate(final String pYear, final String pMonth) {
        return createDate(pYear, pMonth, null);
    }

    public static Date createDate(final String pYear, final String pMonth, final String pDay) {
        final int defaultYear = 1800;
        final int defaultMonth = 1;
        final int defaultDay = 1;

        final String year_tmp = toStr(pYear);
        final String month_tmp = toStr(pMonth);
        final String day_tmp = toStr(pDay);

        if (year_tmp.isEmpty() && month_tmp.isEmpty() && day_tmp.isEmpty()) {
            return null;
        }

        final int year = year_tmp.isEmpty() ? defaultYear : Integer.valueOf(year_tmp);
        final int month = month_tmp.isEmpty() ? defaultMonth : Integer.valueOf(month_tmp);
        final int day = day_tmp.trim().isEmpty() ? defaultDay : Integer.valueOf(day_tmp);

        if (year == defaultYear && month == defaultMonth && day == defaultDay) {
            return null;
        }

        final Date dt = new GregorianCalendar(year, month - 1, day).getTime();
        return dt;
    }

    public static int daysBetween(final Date pAufnahmedatum, final Date pEntlassungsdatum) {
        if (pAufnahmedatum == null) {
            return -1;
        }
        if (pEntlassungsdatum == null) {
            return -1;
        }
        final int days = ((int) ((pEntlassungsdatum.getTime() - pAufnahmedatum.getTime()) / (1000 * 60 * 60 * 24))) + 1;
        return days;
    }

    public static String[] getErbringungsart(final String pCode) {
        String erbringungsart = "HA";
        String code = pCode;
        if (code.length() > 4) {
            erbringungsart = code.substring(0, code.length() - 4).trim(); //Erbringungsart? HA/BA?
            if ("BA".equalsIgnoreCase(erbringungsart)) {
                erbringungsart = "Bo"; //?!
            }
            code = pCode.substring(pCode.length() - 4).trim();
        }

//        if (code.equals("2400")) { //FA 2400: Frauenheilkunde und Geburtshilfe Fachabteilung
//            erbringungsart = "HaBh"; //HA/B-heb
//        }
        return new String[]{erbringungsart, code};
    }

    public static Map<String, Boolean> getInputFilenames() {
        final Map<String, Boolean> files = new HashMap<>();
        //false = optional, true = mandatory
        files.put(ENT_FILENAME, false);
        files.put(FAB_FILENAME, true);
        files.put(FALL_FILENAME, true);
        files.put(ICD_FILENAME, true);
        files.put(OPS_FILENAME, true);
        files.put(INFO_FILENAME, false);
        files.put(PATIENT_FILENAME, false);
        files.put(FALL_ZUSATZ_FILENAME, false);
        return files;
        /*
    String[] files = new String[] {
    ENT_FILENAME,
    FAB_FILENAME,
    FALL_FILENAME,
    ICD_FILENAME,
    OPS_FILENAME
    };
    return files;
         */
    }

    public static P21Files getFileNames(final File pInputDirectory) {
        List<File> files = getDirectoryFiles(pInputDirectory);
        if (files.isEmpty()) {
            throw new IllegalArgumentException("Directory has no input files: " + pInputDirectory.getAbsolutePath());
        }
        return getFileNames(files);
    }

    public static P21Files getFileNames(final List<File> pFiles) {
        if (pFiles == null || pFiles.isEmpty()) {
            throw new IllegalArgumentException("no input files passed!");
        }
        final Map<String, P21File> fileNames = new HashMap<>();
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
            final P21File p21File = new P21File(fileName, file, mandatory == null ? false : mandatory);
            fileNames.put(fileName, p21File);
            if (p21File.found) {
                existingFiles.add(file);
            }
        }
        final P21VersionHelper p21VersionHelper = new P21VersionHelper();
        P21Version p21Version = null;
        try {
            p21Version = p21VersionHelper.getDatasetVersion(existingFiles);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot detect P21 version", ex);
        }

        final List<File> inputDirectories = getFileParents(pFiles);

        final File inputDirectory;
        if (inputDirectories.size() == 1) {
            inputDirectory = inputDirectories.iterator().next();
        } else {
            inputDirectory = null;
        }
        //= pFiles.iterator().next().getParentFile();
        final P21Files p21Files = new P21Files(p21Version, inputDirectory, fileNames);

        final StringBuilder sb = new StringBuilder();
        sb.append("\n      -> P21 Version: " + p21Files.getP21Year());
        for (Map.Entry<String, P21File> entry : p21Files.entrySet()) {
            final P21File p21File = entry.getValue();
            sb.append("\n      -> " + p21File.toString());
        }
        LOG.log(Level.INFO, "Result of P21 directory analysis in {0}: {1}", new Object[]{p21Files.getP21Directory() == null ? "" : p21Files.getP21Directory().getAbsolutePath(), sb});

        return p21Files;
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

    @Override
    public TransformResult start() throws InterruptedException {
        final P21Files p21Files = getFileNames(getInputDirectory());
        StringBuilder errors = new StringBuilder();
        StringBuilder warnings = new StringBuilder();
        //Map<String, RandomAccessFile> map = new HashMap<>();
//        final List<File> files = new ArrayList<>();

        //final Map<String, Integer> fileLineCounts = new HashMap<>();
        //mLineCounter = new HashMap<>();
        //for(final String fileName: getInputFilenames()) {
        //Iterator<Map.Entry<String, Boolean>> it = getInputFilenames().entrySet().iterator();
        //while (it.hasNext()) {
        final Iterator<Map.Entry<String, P21File>> it = p21Files.iterator();
        while (it.hasNext()) {
            //for (P21File p21File : fileNames) {
            final Map.Entry<String, P21File> entry = it.next();
            final P21File p21File = entry.getValue();
            //Map.Entry<String, Boolean> entry = it.next();
            //final String fileName = p21File.getName();
            //final File file = p21File.file;
            //final boolean mandatory = p21File.isMandatory();
            //final boolean found = p21File.isFound();
            //casePointer.put(fileName, null);
            //mLinePosition.put(fileName, new LinePosition("", "", 0));
            mLinePosition.put(p21File.fileName, null);
            //final File file = fileNames.get(fileName);
            //final File file = findFilename(files, fileName);            
            //if (file == null) {
            if (!p21File.found) {
                if (p21File.mandatory) {
                    //mandatory files
                    if (errors.length() > 0) {
                        errors.append("\r\n");
                    }
                    errors.append("File '" + p21File.fileName + "' not found in " + getInputDirectory().getAbsolutePath());
                } else {
                    //optional files
                    if (warnings.length() > 0) {
                        warnings.append("\r\n");
                    }
                    warnings.append("File '" + p21File.fileName + "' not found in " + getInputDirectory().getAbsolutePath());
                }
            }
//            } else {
//                fileNames.put(fileName, file);
//            }
        }

        if (warnings.length() > 0) {
            LOG.log(Level.WARNING, "At least one file seems to be missing, but that's not necessarily a big deal (I will skip it!):\r\n" + warnings.toString());
        }
        if (errors.length() > 0) {
            throw new IllegalArgumentException("Detected missing files:\r\n" + errors.toString());
        }

        //VERSIONSKENNUNG = detectVersionskennung(findFilename(files, "INFO.csv"));
        //if (versionskennung.equalsIgnoreCase("20140101")
        //        || versionskennung.equalsIgnoreCase("20150101")) {
        //  FALL_SHIFT = 2;
        //}
//        P21VersionHelper p21VersionHelper = new P21VersionHelper();
//        p21Version = p21VersionHelper.getDatasetVersion(files);
        p21Version = p21Files.getP21Version();
        versionskennung = p21Files.getP21VersionIdentifier();
        p21Year = p21Files.getP21Year();

        LOG.log(Level.INFO, "P21-Versionskennung: " + versionskennung + ", Jahr: " + p21Year);

        if (!p21Files.isP21Supported()) {
            throw new IllegalArgumentException("P21 version " + versionskennung + " was detected, but versions older than " + P21Version.MIN_SUPPORTED_IMPORT_YEAR + " are not supported!");
        }

        if (p21Year >= 2013) {
            fall_shift = 2;
        }

        if (p21Year >= 2016) {
            wohnort_shift = 1;
        } else {
            final String[] saFallHead = p21Files.getFallFile().getHeadline();
            Set<String> vals = new LinkedHashSet<>(Arrays.asList(saFallHead));
            if (vals.contains("Ort")) {
                wohnort_shift = 1;
            }
        }

        if (p21Year >= 2020) {
            standort_shift = 1;
        }

//        if (versionskennung.equalsIgnoreCase("20120101")) {
//            WOHNORT_SHIFT = 1;
//        }
        //try (final CpxWriter cpxMgr = CpxWriter.getInstance(getOutputDirectory().getAbsolutePath())) {
        final CountDownLatch executionCompleted = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final CountDownLatch executionCompletedPatientFallZusatz = new CountDownLatch(2);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                processPatientFile(mCpxMgr, p21Files.get(PATIENT_FILENAME));
                            } catch (Exception ex) {
                                exceptions.add(ex);
                                LOG.log(Level.SEVERE, null, ex);
                            }
                            executionCompletedPatientFallZusatz.countDown();
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                processFallZusatzFile(mCpxMgr, p21Files.get(FALL_ZUSATZ_FILENAME));
                            } catch (Exception ex) {
                                exceptions.add(ex);
                                LOG.log(Level.SEVERE, null, ex);
                            }
                            executionCompletedPatientFallZusatz.countDown();
                        }
                    }).start();
                    executionCompletedPatientFallZusatz.await();

                    processEntFile(mCpxMgr, p21Files.get(ENT_FILENAME));
                    int[] tmp = processFallFile(mCpxMgr, p21Files.get(FALL_FILENAME));
                    patientCounter.set(tmp[0]); //Index 0 -> Number of patients
                    caseCounter.set(tmp[1]); //Index 1 -> Number of hospital cases
                } catch (Exception ex) {
                    exceptions.add(ex);
                    LOG.log(Level.SEVERE, null, ex);
                }
                executionCompleted.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    processFabFile(mCpxMgr, p21Files.get(FAB_FILENAME));
//                        for(Map.Entry<String, String> entry: mFirstDepartmentNr2.entrySet()) {
//                           System.out.println(entry.getKey() + " => " + entry.getValue());
//                        }
//                        System.exit(0);
                    final CountDownLatch executionCompletedIcdOps = new CountDownLatch(2);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                processIcdFile(mCpxMgr, p21Files.get(ICD_FILENAME));
                            } catch (Exception ex) {
                                exceptions.add(ex);
                                LOG.log(Level.SEVERE, null, ex);
                            }
                            executionCompletedIcdOps.countDown();
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                processOpsFile(mCpxMgr, p21Files.get(OPS_FILENAME));
                            } catch (Exception ex) {
                                exceptions.add(ex);
                                LOG.log(Level.SEVERE, null, ex);
                            }
                            executionCompletedIcdOps.countDown();
                        }
                    }).start();
                    executionCompletedIcdOps.await();
                } catch (Exception ex) {
                    exceptions.add(ex);
                    LOG.log(Level.SEVERE, null, ex);
                }
                executionCompleted.countDown();
            }
        }).start();
        executionCompleted.await();
        mFirstDepartmentNr.clear();
        //mCaseKeys.clear();
        //}

        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
    }

    private FileManager getFallManager(final P21File pP21File) {
        final FileManager fileManager = pP21File.found || pP21File.mandatory ? new FileManager(pP21File.actualFile.getAbsolutePath()) : null;
        return fileManager;
    }

    private BufferedReader getReader(final FileManager pFileManager) throws IOException {
        final BufferedReader br = pFileManager != null ? pFileManager.getBufferedReader() : null;
        return br;
    }

    protected int[] processFallFile(final CpxWriter pCpxMgr, final P21File p21FallFile) throws InterruptedException {
        final AtomicInteger patientCount = new AtomicInteger();
        final AtomicInteger caseCount = new AtomicInteger();
        final int thread = 1; //only 1 thread is mandatory to import Patient.csv
//        try (final FileManager fileManagerPat = getFallManager(p21PatFile);  BufferedReader brPat = getReader(fileManagerPat); final FileManager fileManagerFallZusatz = getFallManager(p21FallZusatzFile);  BufferedReader brFallZusatz = getReader(fileManagerFallZusatz)) {
//            String headLinePat = brPat == null ? null : brPat.readLine(); //skip first line (headline)) 
//            String headLineFallZusatz = brFallZusatz == null ? null : brFallZusatz.readLine(); //skip first line (headline)) 
//        final AtomicInteger patientLine = new AtomicInteger(1);
        processFile(pCpxMgr, p21FallFile, new P21Process() {
            @Override
            public void process(final CpxWriter pCpxMgr, final String[] sa) throws Exception {
//                    int patLineCount = patientLine.incrementAndGet();
//                    int fallZusatzLineCount = patientLine.incrementAndGet();

//                    String linePat = brPat == null ? null : brPat.readLine();
//                    String[] saPat = linePat == null ? null : AbstractLine.splitLine(linePat + DELIMITER + patLineCount, DELIMITER);
                Patient patient = importPatient(sa);
                if (patient != null) {
                    pCpxMgr.write(patient);
                    patientCount.incrementAndGet();
                }

//                    String lineFallZusatz = brFallZusatz == null ? null : brFallZusatz.readLine();
//                    String[] saFallZusatz = lineFallZusatz == null ? null : AbstractLine.splitLine(lineFallZusatz + DELIMITER + fallZusatzLineCount, DELIMITER);
                Case cs = importFall(sa);
                if (cs != null) {
                    if (cs.getAufnahmedatum() == null) {
                        LOG.log(Level.WARNING, "Case has no admission date, so it will be ignored: " + cs.getCaseKey());
                    } else {
                        pCpxMgr.write(cs);
                        //mCaseKeys.add(cs.getCaseKey());
                        caseCount.incrementAndGet();
                    }
                }

//                Random r = new SecureRandom();
//                int v = (r.nextInt(10) + 1);
//                for(int i = 1; i <= v; i++) {
//                    Lab lab = new Lab(cs);
//                    lab.setAnalysis("Analysis " + (r.nextInt(9998) + 1));
//                    lab.setBenchmark("Benchm. " + (r.nextInt(98) + 1));
//                    lab.setComment("Dies ist ein Test " + (r.nextInt(9998) + 1) + "!");
//                    lab.setDescription("Description " + (r.nextInt(9998) + 1));
//                    lab.setGroup("Group " + (r.nextInt(9998) + 1));
//                    lab.setAnalysisDate(new Date());
//                    lab.setCategory(r.nextInt(9998) + 1);
//                    lab.setDate(new Date());
//                    lab.setKisExternKey("Kis Extern Key " + (r.nextInt(9998) + 1));
//                    lab.setLockdel(r.nextInt(9998) + 1);
//                    lab.setMaxLimit((r.nextInt(9998) + 1) + r.nextDouble());
//                    lab.setMinLimit((r.nextInt(9998) + 1) + r.nextDouble());
//                    lab.setPosition(r.nextInt(9998) + 1);
//                    lab.setValue((r.nextInt(9998) + 1) + r.nextDouble());
//                    lab.setValue2((r.nextInt(9998) + 1) + r.nextDouble());
//                    lab.setMethod("Method " + (r.nextInt(9998) + 1));;
//                    lab.setRange("Range " + (r.nextInt(9998) + 1));
//                    lab.setText("Text " + (r.nextInt(9998) + 1));
//                    lab.setUnit("Unit " + (r.nextInt(9998) + 1));
//                    pCpxMgr.write(lab);
//                }
                /*
        if (lineCount % 100000 == 0) {
          mLogger.log(Level.INFO, fileName + ": Line " + lineCount);
        }
                 */
            }
        }, thread);
//        }
        return new int[]{patientCount.get(), caseCount.get()};
    }

    protected void processEntFile(final CpxWriter pCpxMgr, final P21File p21File) throws Exception {
        if (!p21File.found && !p21File.mandatory) {
            LOG.log(Level.WARNING, ENT_FILENAME + " was not passed. I cannot proceed with it and so I will not import fees. Is it okay my friend?");
            return;
        }
        final int thread = 1; //only 1 thread is mandatory for fees to detect tob
        processFile(pCpxMgr, p21File, new P21Process() {
            @Override
            public void process(final CpxWriter pCpxMgr, final String[] sa) throws Exception {
                String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
                //String patKey = AbstractDto.buildPatientKey(sa[19 + FALL_SHIFT]);
                Fee fee = importEnt(sa);
                if (fee != null) {
                    pCpxMgr.write(fee);
                }
                /*
        if (lineCount % 100000 == 0) {
          mLogger.log(Level.INFO, fileName + ": Line " + lineCount);
        }
                 */
            }
        }, thread);
    }

    private boolean addDepNr(final String pCaseKey, long pDepNr) {
        if (mFirstDepartmentNr.get(pCaseKey) == null || mFirstDepartmentNr.get(pCaseKey) < pDepNr) {
            mFirstDepartmentNr.put(pCaseKey, pDepNr);
            return true;
        }
        return false;
    }

    protected void processFabFile(final CpxWriter pCpxMgr, final P21File p21File) throws Exception {
        final int thread = 1; //only 1 thread is mandatory for departments to detect the last department no
        processFile(pCpxMgr, p21File, new P21Process() {
            @Override
            public void process(final CpxWriter pCpxMgr, final String[] sa) throws Exception {
                String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
                //String patKey = AbstractDto.buildPatientKey(sa[19 + FALL_SHIFT]);
                Department dep = importFab(sa);
                if (dep != null) {
                    pCpxMgr.write(dep);
                    addDepNr(caseKey, dep.getNr());
                    //mFirstDepartmentNr.put(caseKey, dep.getNr());
                    //addDepNr(caseKey, dep.getNr());
//                    synchronized (mFirstDepartmentNr) {
//                        if (mFirstDepartmentNr.get(caseKey) == null || mFirstDepartmentNr.get(caseKey) > dep.getNr()) {
//                            mFirstDepartmentNr.put(caseKey, dep.getNr());
//                        }
//                    }
                }
                /*
        if (lineCount % 100000 == 0) {
          mLogger.log(Level.INFO, fileName + ": Line " + lineCount);
        }
                 */
            }
        }, thread);
    }

    protected void processPatientFile(final CpxWriter pCpxMgr, final P21File p21File) throws InterruptedException {
        if (!p21File.found && !p21File.mandatory) {
            LOG.log(Level.WARNING, PATIENT_FILENAME + " was not passed. I cannot proceed with it and so I will not import additional patient information. Is it okay my friend?");
            return;
        }
        processFile(pCpxMgr, p21File, new P21Process() {
            @Override
            public void process(final CpxWriter pCpxMgr, final String[] sa) throws Exception {
                //String[] sa = AbstractLine.splitLine(pLine + DELIMITER + pLineCount, DELIMITER);
                //String[] sa = AbstractLine.splitLine(pLine + DELIMITER + pLineCount, DELIMITER);
                String caseKey = AbstractDto.buildCaseKey(sa[0], sa[2]);
                final String[] saTmp = Arrays.copyOfRange(sa, 3, sa.length); //use only part of array to consume less memory!
                mPatienten.put(caseKey, saTmp);
//                if (!mCaseKeys.contains(caseKey)) {
//                    LOG.log(Level.WARNING, "This icd is assigned to an hospital case " + caseKey + " that was not found in fall.csv");
//                    return;
//                }
//                Long depNr = mFirstDepartmentNr.get(caseKey);
//                if (depNr == null) {
//                    Integer lineNo = getLine(sa);
//                    String msg = "This icd in " + p21File.actualFile.getAbsoluteFile() + ", Line " + lineNo + " is assigned to a department or hospital case that was not found in fall.csv/fab.csv: \r\n" + String.join(";", sa);
//                    LOG.log(Level.WARNING, msg);
//                    return;
//                }
//                //String patKey = AbstractDto.buildPatientKey(sa[19 + FALL_SHIFT]);
//                Diagnose<?>[] icd = importIcd(sa, depNr);
//                if (icd != null) {
//                    for (Diagnose<?> diagnose : icd) {
//                        pCpxMgr.write(diagnose);
//                    }
//                }
                /*
        if (lineCount % 100000 == 0) {
          mLogger.log(Level.INFO, fileName + ": Line " + lineCount);
        }
                 */
            }
        });
    }

    protected void processFallZusatzFile(final CpxWriter pCpxMgr, final P21File p21File) throws InterruptedException {
        if (!p21File.found && !p21File.mandatory) {
            LOG.log(Level.WARNING, FALL_ZUSATZ_FILENAME + " was not passed. I cannot proceed with it and so I will not import additional case information. Is it okay my friend?");
            return;
        }
        processFile(pCpxMgr, p21File, new P21Process() {
            @Override
            public void process(final CpxWriter pCpxMgr, final String[] sa) throws Exception {
                //String[] sa = AbstractLine.splitLine(pLine + DELIMITER + pLineCount, DELIMITER);
                //String[] sa = AbstractLine.splitLine(pLine + DELIMITER + pLineCount, DELIMITER);
                String caseKey = AbstractDto.buildCaseKey(sa[0], sa[1]);
                final String[] saTmp = Arrays.copyOfRange(sa, 2, sa.length); //use only part of array to consume less memory!
                mFallZusatz.put(caseKey, saTmp);
//                if (!mCaseKeys.contains(caseKey)) {
//                    LOG.log(Level.WARNING, "This icd is assigned to an hospital case " + caseKey + " that was not found in fall.csv");
//                    return;
//                }
//                Long depNr = mFirstDepartmentNr.get(caseKey);
//                if (depNr == null) {
//                    Integer lineNo = getLine(sa);
//                    String msg = "This icd in " + p21File.actualFile.getAbsoluteFile() + ", Line " + lineNo + " is assigned to a department or hospital case that was not found in fall.csv/fab.csv: \r\n" + String.join(";", sa);
//                    LOG.log(Level.WARNING, msg);
//                    return;
//                }
//                //String patKey = AbstractDto.buildPatientKey(sa[19 + FALL_SHIFT]);
//                Diagnose<?>[] icd = importIcd(sa, depNr);
//                if (icd != null) {
//                    for (Diagnose<?> diagnose : icd) {
//                        pCpxMgr.write(diagnose);
//                    }
//                }
                /*
        if (lineCount % 100000 == 0) {
          mLogger.log(Level.INFO, fileName + ": Line " + lineCount);
        }
                 */
            }
        });
    }

    protected void processIcdFile(final CpxWriter pCpxMgr, final P21File p21File) throws Exception {
        processFile(pCpxMgr, p21File, new P21Process() {
            @Override
            public void process(final CpxWriter pCpxMgr, final String[] sa) throws Exception {
                //String[] sa = AbstractLine.splitLine(pLine + DELIMITER + pLineCount, DELIMITER);
                //String[] sa = AbstractLine.splitLine(pLine + DELIMITER + pLineCount, DELIMITER);
                String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
//                if (!mCaseKeys.contains(caseKey)) {
//                    LOG.log(Level.WARNING, "This icd is assigned to an hospital case " + caseKey + " that was not found in fall.csv");
//                    return;
//                }
                Long depNr = mFirstDepartmentNr.get(caseKey);
                if (depNr == null) {
                    Integer lineNo = getLine(sa);
                    String msg = "This icd in " + p21File.actualFile.getAbsoluteFile() + ", Line " + lineNo + " is assigned to a department or hospital case that was not found in fall.csv/fab.csv: \r\n" + String.join(";", sa);
                    LOG.log(Level.WARNING, msg);
                    return;
                }
                //String patKey = AbstractDto.buildPatientKey(sa[19 + FALL_SHIFT]);
                Diagnose<?>[] icd = importIcd(sa, depNr);
                if (icd != null) {
                    for (Diagnose<?> diagnose : icd) {
                        pCpxMgr.write(diagnose);
                    }
                }
                /*
        if (lineCount % 100000 == 0) {
          mLogger.log(Level.INFO, fileName + ": Line " + lineCount);
        }
                 */
            }
        });
    }

    protected void processOpsFile(final CpxWriter pCpxMgr, final P21File p21File) throws Exception {
        processFile(pCpxMgr, p21File, new P21Process() {
            @Override
            public void process(final CpxWriter pCpxMgr, final String[] sa) throws Exception {
                //String[] sa = AbstractLine.splitLine(pLine + DELIMITER + pLineCount, DELIMITER);
                //String[] sa = AbstractLine.splitLine(pLine);
                String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
//                if (!mCaseKeys.contains(caseKey)) {
//                    LOG.log(Level.WARNING, "This ops is assigned to an hospital case " + caseKey + " that was not found in fall.csv");
//                    return;
//                }
//                Long depNr = mFirstDepartmentNr.get(caseKey);
//                if (depNr == null) {
//                    Integer lineNo = getLine(sa);
//                    String msg = "This ops in " + pFile.getAbsoluteFile() + ", Line " + lineNo + " is assigned to a department or hospital case that was not found in fall.csv/fab.csv: \r\n" + String.join(";", sa);
//                    LOG.log(Level.WARNING, msg);
//                    return;
//                }
                Long depNr = -1L;
                //String patKey = AbstractDto.buildPatientKey(sa[19 + FALL_SHIFT]);
                Procedure ops = importOps(sa, depNr);
                if (ops != null) {
                    pCpxMgr.write(ops);
                }
            }
        });
    }

    protected void processFile(final CpxWriter pCpxMgr, final P21File pFile, final P21Process pP21Process) throws InterruptedException {
        processFile(pCpxMgr, pFile, pP21Process, mThreadCount);
    }

    protected void processFile(final CpxWriter pCpxMgr, final P21File pFile, final P21Process pP21Process, final int pThreadCount) throws InterruptedException {
        try (final FileManager fileManager = getFallManager(pFile)) {
            if (fileManager == null) {
                LOG.log(Level.WARNING, "cannot open file manager for file: {0}", pFile.actualFile);
                return;
            }
            final String fileName = fileManager.getFile().getName();
            LOG.log(Level.INFO, "Start reading from file " + fileName);
            final AtomicBoolean readingFinished = new AtomicBoolean(false);
            //Map<String, List<String[]>> linesMap = new HashMap<>(0);
            //final ExecutorService lScheduler = Executors.newFixedThreadPool(mThreadCount);
            final CountDownLatch executionCompleted = new CountDownLatch(pThreadCount + 1);
            final BlockingQueue<String[]> lBlockingQueue = new LinkedBlockingQueue<>(5000);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try (BufferedReader br = getReader(fileManager)) {
                        if (br == null) {
                            LOG.log(Level.WARNING, "cannot open reader for file: {0}", pFile.actualFile);
                            return;
                        }
                        String headLine = br.readLine(); //skip first line (headline)
                        String line;
                        int lineCount = 1;
                        while ((line = br.readLine()) != null) {
                            lineCount++;
                            line = line.trim();
                            if (line.isEmpty()) {
                                continue;
                            }
                            //lBlockingQueue.add(line + DELIMITER + lineCount);
                            if (!lBlockingQueue.offer(AbstractLine.splitLine(line + DELIMITER + lineCount, DELIMITER), 10, TimeUnit.SECONDS)) {
                                LOG.log(Level.FINEST, "the specified waiting time elapsed before space was available");
                            }
                        }
                        readingFinished.set(true);
                    } catch (InterruptedException | IOException ex) {
                        exceptions.add(ex);
                        LOG.log(Level.SEVERE, null, ex);
                        if (ex instanceof InterruptedException) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    executionCompleted.countDown();
                }
            }).start();

            final AtomicInteger counter = new AtomicInteger(0);
            for (int i = 1; i <= pThreadCount; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] sa = null;
                        try {
                            //while(!readingFinished.get() || (sa = lBlockingQueue.poll()) != null) {
                            while (true) {
                                while ((sa = lBlockingQueue.poll()) != null) {
                                    pP21Process.process(pCpxMgr, sa);
                                    int count = counter.incrementAndGet();
                                    //int lineCount = sa[sa.length - 1];
                                    if (count % 100000 == 0) {
                                        LOG.log(Level.INFO, fileName + ": Count " + count);
                                    }
                                }
                                if (readingFinished.get()) {
                                    break;
                                }
                                Thread.sleep(1000L);
                            }
                        } catch (Exception ex) {
                            int lineNo = getLine(sa);
                            String msg = "Error in " + pFile.actualFile.getAbsoluteFile() + ", Line " + lineNo + "\r\n" + String.join(";", sa) + "\r\n" + ex.getMessage();
                            Exception newEx = new IllegalArgumentException(msg, ex);
                            exceptions.add(newEx);
                            LOG.log(Level.SEVERE, msg, newEx);
                        }
                        executionCompleted.countDown();
                    }
                }).start();
            }
            executionCompleted.await();
            LOG.log(Level.INFO, "Finished reading from file " + fileName);
            //return counter.get();
        }
    }

    protected Patient importPatient(final String[] sa) throws ParseException {
        String patKey = AbstractDto.buildPatientKey(sa[19 + fall_shift + wohnort_shift]);
        //if (!mPatienten.add(patKey)) {
        //  return null;
        //}
        if (patientKeyExists(patKey)) {
            return null;
        }
        Patient patient = new Patient();
        patient.setPatNr(patKey);
        if (fall_shift > 0) {
            patient.setVersichertennr(sa[4]);
            //patient.setXXX(sa[5]);
        }
        if (wohnort_shift > 0) {
            patient.setOrt(sa[8 + fall_shift + wohnort_shift]);
        }
        patient.setKasse(sa[4 + fall_shift]);
//        String jahr = sa[5 + fall_shift];
//        String monat = sa[6 + fall_shift];
//        patient.setGeburtsdatum(createDate(jahr, monat));
        patient.setGeschlecht(sa[7 + fall_shift].isEmpty() ? Geschlecht.U : Geschlecht.findByValue(sa[7 + fall_shift]));
        patient.setPlz(sa[8 + fall_shift]);
        patient.setTpSource(FALL_FILENAME);
        patient.setTpId("Line " + sa[sa.length - 1]);

        final String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
        final String[] saPat = mPatienten.get(caseKey);
        //final String patCaseKey = saPat == null ? null : AbstractDto.buildCaseKey(saPat[0], saPat[2]);
        if (saPat != null) {
            //read additional information from Patient.csv
            Geschlecht geschlecht = saPat[2].isEmpty() ? null : Geschlecht.findByValue(saPat[2]);
            if (geschlecht != null) {
                patient.setGeschlecht(geschlecht);
            }
            final String geburtsDatumVal = saPat[3];
            final Date geburtsdatum;
            if (geburtsDatumVal.length() <= 8) {
                geburtsdatum = parseDate(saPat[3], FORMAT_DATE);
            } else {
                //parse additional birth time
                geburtsdatum = parseDate(saPat[3], FORMAT_DATETIME);
            }
            if (geburtsdatum != null) {
                patient.setGeburtsdatum(geburtsdatum);
            }
            String name = toStr(saPat[0]);
            if (!name.isEmpty()) {
                patient.setNachname(name);
            }
            String vorname = toStr(saPat[1]);
            if (!vorname.isEmpty()) {
                patient.setVorname(vorname);
            }
        }
        //mPatienten.add(patKey);
        return patient;
    }

    protected Case importFall(final String[] sa) throws ParseException {
        //Map<String, Patient> patienten = new HashMap<>();
        //List<String[]> lines = entry.getValue();
        //String caseKey = entry.getKey();
        //List<String[]> lines = mFileAccess.get(FALL_FILENAME);
        String patKey = AbstractDto.buildPatientKey(sa[19 + fall_shift + wohnort_shift]);
        //Patient patient = mPatienten.get(patKey);

        Case cs = new Case(patKey);
        cs.setIkz(sa[0]);
        cs.setFallNr(sa[3]);
        cs.setFallart(sa[2]);
        if (cs.getFallart().isEmpty()) {
            cs.setFallart("DRG"); //Sometimes there are cases without a given case type, so how can I handle this?!
        }

        cs.setGeschlecht(sa[7 + fall_shift].isEmpty() ? Geschlecht.U : Geschlecht.findByValue(sa[7 + fall_shift]));
        cs.setAufnahmedatum(sa[9 + fall_shift + wohnort_shift].isEmpty() ? null : parseDate(sa[9 + fall_shift + wohnort_shift], FORMAT_DATETIME));
        String aufnahmeanlass = sa[10 + fall_shift + wohnort_shift];
        if (aufnahmeanlass.isEmpty()) {
            LOG.log(Level.WARNING, "No admission cause found for hospital case " + cs.getCaseKey() + ". Will use default value (E - Einweisung durch Arzt) instead!");
            aufnahmeanlass = "E"; //Einweisung durch Arzt
        }
        cs.setAufnahmeanlass(aufnahmeanlass);

        String aufnahmegrund = toStr(sa[11 + fall_shift + wohnort_shift]);
        if (aufnahmegrund.length() == 1 || aufnahmegrund.length() == 3) {
            aufnahmegrund = "0" + aufnahmegrund;
        }
        String aufnahmegrund1 = (aufnahmegrund.length() >= 2) ? aufnahmegrund.substring(0, 2) : "";
        String aufnahmegrund2 = (aufnahmegrund.length() >= 4) ? aufnahmegrund.substring(2, 4) : "01"; //01 = Regular Case

        if (aufnahmegrund1.isEmpty()) {
            LOG.log(Level.WARNING, "No admission reason 12 found for hospital case " + cs.getCaseKey() + ". Will use default value (01 - Krankenhausbehandlung, vollstationär) instead!");
            aufnahmegrund1 = "01"; //Krankenhausbehandlung, vollstationär AGE: müsste es nicht 09 sein, 
        }

        cs.setAufnahmegrund1(aufnahmegrund1);
        cs.setAufnahmegrund2(aufnahmegrund2);
        String gewicht = sa[14 + fall_shift + wohnort_shift];
        gewicht = gewicht.replace(".0", "").trim();
        //cs.setGewicht(sa[14 + FALL_SHIFT].isEmpty()?null:Integer.valueOf(sa[14 + FALL_SHIFT]));
        cs.setGewicht(gewicht.isEmpty() ? 0 : Integer.valueOf(gewicht));
        cs.setEntlassungsdatum(sa[15 + fall_shift + wohnort_shift].isEmpty() ? null : parseDate(sa[15 + fall_shift + wohnort_shift], FORMAT_DATETIME));

        String entlassungsgrund = toStr(sa[16 + fall_shift + wohnort_shift]);
        if (entlassungsgrund.length() == 2) {
            entlassungsgrund = "0" + entlassungsgrund;
        }
        String entlassungsgrund1 = (entlassungsgrund.length() >= 3) ? entlassungsgrund.substring(0, 2) : "";
        String entlassungsgrund2 = (entlassungsgrund.length() >= 3) ? entlassungsgrund.substring(2, 3) : "";

        cs.setEntlassungsgrund12(entlassungsgrund1);
        cs.setEntlassungsgrund3(entlassungsgrund2);
        cs.setAlterInTagen(sa[17 + fall_shift + wohnort_shift].isEmpty() ? 0 : Integer.valueOf(sa[17 + fall_shift + wohnort_shift]));
        cs.setAlterInJahren(sa[18 + fall_shift + wohnort_shift].isEmpty() ? 0 : Integer.valueOf(sa[18 + fall_shift + wohnort_shift]));
        cs.setBeatmungsstunden(sa[21 + fall_shift + wohnort_shift].isEmpty() ? 0 : Integer.valueOf(sa[21 + fall_shift + wohnort_shift]));
        //cs.setUrlaubstage((sa[28 + FALL_SHIFT].isEmpty()?0:Integer.valueOf(sa[28 + FALL_SHIFT + WOHNORT_SHIFT])) //Beurlaubungstage-PSY+Belegungstage-in-anderem-Entgeltbereich
        //                + (sa[27 + FALL_SHIFT].isEmpty()?0:Integer.valueOf(sa[27 + FALL_SHIFT + WOHNORT_SHIFT]))); //TODO Klären
        //cs.setEntgeltbereich(???);
        Integer tob = mTobMap.get(cs.getCaseKey());
        Integer mdTob = mTobMdMap.get(cs.getCaseKey());
        tob = tob == null ? 0 : tob;
        if(cs.getFallart().equals("DRG") && mdTob != null){
            cs.setMDTob(mdTob - tob);
        }
        cs.setVwdSimuliert(0); 
        cs.setUrlaubstage(tob);
        //cs.setVwd((daysBetween(cs.getAufnahmedatum(), cs.getEntlassungsdatum())) - cs.getUrlaubstage()); //Entlassungsdatum - Aufnahmedatum
        cs.setVwd(0); //Entlassungsdatum - Aufnahmedatum (is calculated by Grouper)
        Integer vwdIntensiv = 0;
        if (p21Year >= 2019) {
            vwdIntensiv = sa[33].isEmpty() ? 0 : checkInteger((sa[33]));
        }
        cs.setVwdIntensiv(vwdIntensiv);
        if (fall_shift > 0) {
            cs.setVersichertennr(sa[4]);
            //patient.setXXX(sa[5]);
        }
        cs.setKasse(sa[4 + fall_shift]);
        cs.setTpSource(FALL_FILENAME);
        cs.setTpId("Line " + sa[sa.length - 1]);

//        final String fallZusatzCaseKey = saFallZusatz == null ? null : AbstractDto.buildCaseKey(saFallZusatz[0], saFallZusatz[1]);
        final String caseKey = cs.getCaseKey();
        final String[] saFallZusatz = mFallZusatz.get(caseKey);
        if (saFallZusatz != null) {
//        if (fallZusatzCaseKey != null) {
//            final String caseKey = cs.getCaseKey(); //AbstractDto.buildCaseKey(sa[0], sa[3]);
//            if (caseKey.equalsIgnoreCase(fallZusatzCaseKey)) {
            Integer vwdIntensiv2 = sa[0].isEmpty() ? null : Integer.valueOf(saFallZusatz[0]);
            if (vwdIntensiv2 != null) {
                int vwdIntensivDays = 0;
                if (vwdIntensiv2>0)
                { 
                    double vwdintVal = (double)vwdIntensiv2/24d; 
                    vwdIntensivDays = (int) Math.ceil(vwdintVal); 
                } 
                cs.setVwdIntensiv(vwdIntensivDays);
                cs.setNumeric3(vwdIntensiv2);
            }
            String fallInfo = toStr(saFallZusatz[1]);
            if (!fallInfo.isEmpty()) {
                cs.setString3(fallInfo);
            }
        }

//        cs.setBillingDate(readSAPBillingDateFrom301(cs));
        
        return cs;

        /*
        importFab(cs);
        importEnt(cs);

        //mLogger.log(Level.INFO, mCaseCounter.incrementAndGet() + "/" + mCaseCount + ": write case " + cs.getCaseKey() + " to imex file");
        pCpxMgr.write(cs);
        it.remove();
    }
         */
 /*
    Iterator<Map.Entry<String, Patient>> itPat = patienten.entrySet().iterator();
    while(itPat.hasNext()) {
      Map.Entry<String, Patient> entryPat = itPat.next();
      pCpxMgr.write(entryPat.getValue());
      itPat.remove();
    }
         */
    }

    protected Department importFab(final String[] sa) throws IOException, ParseException {
        /*
    List<String[]> lines = mFileAccess.get(FAB_FILENAME).get(pCase.getCaseKey());
    if (lines == null) {
      return;
    }
    int i = 0;
    Iterator<String[]> it = lines.iterator();
    while(it.hasNext()) {
      String[] sa = it.next();
         */
        //String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
        String ikz = sa[0];
        String fallNr = sa[3];
        /*
      if (!pCase.getCaseKey().equalsIgnoreCase(caseKey)) {
        continue;
      }
         */
        //i++;
        Department dep = new Department(ikz, fallNr);
        String[] tmp = getErbringungsart(sa[4 + standort_shift]);
        String erbringungsartVal = tmp[0];
        String code = tmp[1];
        dep.setCode(code);
        if (dep.getCode().isEmpty()) {
            dep.setCode("0000");
        }
        Erbringungsart erbringungsart = Erbringungsart.findByValue(erbringungsartVal);
        dep.setErbringungsart(erbringungsart);
        dep.setVerlegungsdatum(parseDate(sa[5 + standort_shift], FORMAT_DATETIME));
        dep.setEntlassungsdatum(sa[6 + standort_shift].isEmpty() ? null : parseDate(sa[6 + standort_shift], FORMAT_DATETIME));
        boolean bedIntensiv = false;
        if (p21Year >= 2019) {
            bedIntensiv = sa[7 + standort_shift].isEmpty() ? false : toBool(sa[7 + standort_shift]);
        }
        dep.setBedIntensiv(bedIntensiv);

        Integer locateNumber = null;
        if (p21Year >= 2020) {
            locateNumber = sa[0].isEmpty() ? null : Integer.valueOf(sa[4]);
        }
        dep.setLocationNumber(locateNumber);

        dep.setTpSource(FAB_FILENAME);
        dep.setTpId("Line " + sa[sa.length - 1]);
        return dep;
        //Entlassender Standort??
        /*
      if (i == 1) {
        //Alle Diagnosen und Prozeduren der ersten Abteilung zuordnen
        importIcd(dep);
        importOps(dep);
      }
      it.remove();
    }
         */
    }

    protected Diagnose<?>[] importIcd(final String[] sa, final Long pDepartmentNo) throws IOException {
        /*
    List<String[]> lines = mFileAccess.get(ICD_FILENAME).get(pDepartment.getCaseKey());
    if (lines == null) {
      return;
    }
    Iterator<String[]> it = lines.iterator();
    while(it.hasNext()) {
      String[] sa = it.next();
         */
        String ikz = sa[0];
        String fallNr = sa[3];
        /*
      String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
      if (!pDepartment.getCaseKey().equalsIgnoreCase(caseKey)) {
        continue;
      }
         */
        Diagnose<?> diagnose;
        if (sa[4].equalsIgnoreCase("HD")) {
            diagnose = new Hauptdiagnose(ikz, fallNr, pDepartmentNo);
        } else {
            diagnose = new Nebendiagnose(ikz, fallNr, pDepartmentNo);
        }
        final IcdResult icd = new IcdResult(sa[6]);
        diagnose.setCode(icd.code);
        diagnose.setRefIcdType(icd.refType);
        //diagnose.setIcdType(p21Year);
        if (diagnose.getCode().isEmpty()) {
            diagnose.setCode("k.A.");
        }
        diagnose.setLokalisation(sa[7].isEmpty() ? Lokalisation.KEINE : Lokalisation.findByValue(sa[7]));
        diagnose.setTpSource(ICD_FILENAME);
        diagnose.setTpId("Line " + sa[sa.length - 1]);

        Nebendiagnose sekundaerdiagnose = null;
        if (!sa[9].isEmpty()) {
            //Sekundärkode vorhanden
            sekundaerdiagnose = new Nebendiagnose(ikz, fallNr, pDepartmentNo);
            final IcdResult sekIcd = new IcdResult(sa[9]);
            sekundaerdiagnose.setCode(sekIcd.code);
            sekundaerdiagnose.setLokalisation(sa[10].isEmpty() ? Lokalisation.KEINE : Lokalisation.findByValue(sa[10]));
            sekundaerdiagnose.setTpSource(ICD_FILENAME);
            sekundaerdiagnose.setTpId("Line " + sa[sa.length - 1]);
            sekundaerdiagnose.setRefIcd(diagnose);
            sekundaerdiagnose.setRefIcdType(sekIcd.refType == null ? RefIcdType.Mark : sekIcd.refType);
            if(sekundaerdiagnose.getRefIcd() != null 
                    && sekundaerdiagnose.getRefIcdType() != null 
                    && sekundaerdiagnose.getRefIcdType().equals(RefIcdType.Mark)){
                diagnose.setRefIcdType(RefIcdType.ToMark);
            }
        }
        /*   
      it.remove();
    }
         */
        if (sekundaerdiagnose == null) {
            return new Diagnose<?>[]{diagnose};
        } else {
            return new Diagnose<?>[]{diagnose, sekundaerdiagnose};
        }
    }

    protected Procedure importOps(final String[] sa, final Long pDepartmentNo) throws IOException, ParseException {
        /*
    List<String[]> lines = mFileAccess.get(OPS_FILENAME).get(pDepartment.getCaseKey());
    if (lines == null) {
      return;
    }
    Iterator<String[]> it = lines.iterator();
    while(it.hasNext()) {
      String[] sa = it.next();
         */
        String ikz = sa[0];
        String fallNr = sa[3];
        /*
      String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
      if (!pDepartment.getCaseKey().equalsIgnoreCase(caseKey)) {
        continue;
      }
         */
        Procedure procedure = new Procedure(ikz, fallNr, pDepartmentNo);
        procedure.setCode(sa[5]);
        if (procedure.getCode().isEmpty()) {
            procedure.setCode("k.A.");
        }
        procedure.setLokalisation(sa[6].isEmpty() ? Lokalisation.KEINE : Lokalisation.findByValue(sa[6]));
        procedure.setDatum(sa[7].isEmpty() ? null : parseDate(sa[7], FORMAT_DATETIME));
        procedure.setTpSource(OPS_FILENAME);
        procedure.setTpId("Line " + sa[sa.length - 1]);

        procedure.setBelegOP(sa[8].isEmpty() || sa[8].equalsIgnoreCase("N") ? 0 : 1);
        procedure.setBelegAna(sa[9].isEmpty() || sa[9].equalsIgnoreCase("N") ? 0 : 1);
        procedure.setBelegHeb(sa[10].isEmpty() || sa[10].equalsIgnoreCase("N") ? 0 : 1);
        return procedure;
        /*
      it.remove();
    }
         */
    }

    private boolean addTob(final String pCaseKey, final int pTob) {
        mTobMap.put(pCaseKey, pTob);
        return true;
    }

    private boolean addTobMd(final String pCaseKey, final int pTob) {
        mTobMdMap.put(pCaseKey, pTob);
        return true;
    }

    protected Fee importEnt(final String[] sa) throws IOException, ParseException {
        /*
    List<String[]> lines = mFileAccess.get(ENT_FILENAME).get(pCase.getCaseKey());
    if (lines == null) {
      return;
    }
    Iterator<String[]> it = lines.iterator();
    while(it.hasNext()) {
      String[] sa = it.next();
         */
        String ikz = sa[0];
        String fallNr = sa[3];
        String abr = sa[2];
        /*
      String caseKey = AbstractDto.buildCaseKey(sa[0], sa[3]);
      if (!pCase.getCaseKey().equalsIgnoreCase(caseKey)) {
        continue;
      }
         */
        Fee fee = new Fee(ikz, fallNr);
        fee.setAnzahl(Integer.valueOf(sa[9]));
        fee.setBetrag(Float.valueOf(floatToMoney(sa[6])));
        fee.setVon(sa[7].isEmpty() ? null : parseDate(sa[7], FORMAT_DATE));
        fee.setBis(sa[8].isEmpty() ? null : parseDate(sa[8], FORMAT_DATE));
        fee.setEntgeltschluessel(sa[5]);
        fee.setTob(sa[10].isEmpty() ? 0 : Integer.valueOf(sa[10]));
        fee.setKasse(sa[4]);
        fee.setTpSource(ENT_FILENAME);
        fee.setTpId("Line " + sa[sa.length - 1]);

        if ( fee.getEntgeltschluessel().equals("00000000")) {
            addTob(fee.getCaseKey(), fee.getTob());
            //mTobMap.put(fee.getCaseKey(), fee.getTob());
        }

        if( fee.getEntgeltschluessel().equals("00PFLEGE")) {
            addTobMd(fee.getCaseKey(), fee.getTob());
            //mTobMap.put(fee.getCaseKey(), fee.getTob());
        } 

        return fee;
    }

    private static int getLine(String[] pSa) {
        int line = 0;
        if (pSa == null || pSa.length == 0) {
            return line;
        }
        String val = null;
        try {
            val = pSa[pSa.length - 1];
            line = Integer.parseInt(val);
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, val + " is not an integer", ex);
        }
        return line;
    }

    /*
  protected String detectVersionskennung(final File pInfoFile) throws FileNotFoundException, Exception {
  String versionskennung = "";
  if (pInfoFile == null) {
  mLogger.log(Level.INFO, "No info file found");
  versionskennung = "";
  } else {
  try(FileManager fileManager = new FileManager(pInfoFile.getAbsolutePath())) {
  try(BufferedReader br = fileManager.getBufferedReader()) {
  String line = br.readLine(); //skip first line (headline)
  while((line = br.readLine()) != null) {
  String[] sa = AbstractLine.splitLine(line, DELIMITER);
  versionskennung = toStr(sa[6]);
  }
  }
  }
  }
  return versionskennung;
  }
     */
    public interface P21Process {

        void process(final CpxWriter pCpxMgr, final String[] pSa) throws Exception;
    }

    /**
     * Ermittelt das Rechnungsdatum der Schlussrechnung aus dem §301-Datensatz
     *
      * @return 
     */
    private static java.sql.Date readSAPBillingDateFrom301(Case pcs) throws ParseException {
        
        java.sql.Date billingDate = null;
        ArrayList<String> billMessages = new ArrayList<String>();
        String fallnr = pcs.getFallNr();
        if(fallnr.endsWith("1") || fallnr.endsWith("2") || fallnr.endsWith("3")) {
            billMessages.add(new String("UNH+00001+AUFN:07:000:00'FKT+10+01+260500005+100500016'INV+123456001+10001+0812+2008-00001'NAD+Beispielname1+Beispielvorname1+m'DPV+2008'AUF+20080109+1030+0101+0100+20080124+1234567'EAD+I10.00'UNT+8+00001"));
            billMessages.add(new String("UNH+00001+RECH:07:000:00'FKT+10+02+260500005+100500016'INV+123456006+10001+0812+2008-00006+00006+AZ00006'NAD+Beispielname6+Beispielvorname6+w'CUX+EUR'REC+RE20080006+20080115+04+20080109+6932,31'ZLG+60,00+2'FAB+2800'ENT+7010R01B+8450,00+20080109+20080113+1'ENT+7310R01B+987,80+20080109+20080113+2'ENT+76000080+550,00+20080110+20080110+1'ENT+47200002+35,12+20080109+20080113+1'ENT+46005000+1,29+20080109+20080113+1'ENT+48000001+0,90+20080109+20080113+1'ENT+47100001+0,64+20080109+20080113+1'UNT+16+00001'"));
            billMessages.add(new String("UNH+00001+RECH:07:000:00'FKT+10+02+260500005+100500016'INV+123456005+10001+0812+2008-00005+00005+AZ00005'NAD+Beispielname5+Beispielvorname5+m'CUX+EUR'REC+RE20080005+20080108+04+20080102+1742,49'ZLG+40,00+2'FAB+2300'ENT+7010I57C+2277,00+20080102+20080105+1'ENT+7210I57C+162,80+20080102+20080105+3'ENT+47200002+8,94+20080102+20080105+1'ENT+48000001+0,90+20080102+20080105+1'ENT+46005000+1,29+20080102+20080105+1'ENT+47100001+0,64+20080102+20080105+1'UNT+15+00001'"));
            billMessages.add(new String("DPV+2013+2013'REC+5143671+20130109+52+20130104+645,08+1008004+++256,01+17,92'ZLG+0,00+1'RZA+2300+604434610++274488600++M23.33:R'BDG+M23.33:R'PRZ+58125:R+20130107'PRZ+58112h:R+20130107'ENA+00018211+++J+20130104+515+3,536300+18,21+1'ENA+00005211+++J+20130104+270+3,536300+9,55+1'ENA+00005310+++J+20130104+505+3,536300+17,86+1' ENA+00034233+++J+20130104+300+3,536300+10,61+1'ENA+00034230+++J+20130104+220+3,536300+7,78+1'ENA+00031503+++J+20130107+1450+3,536300+51,28+1'ENA+00031822+++J+20130107+3555+3,536300+125,72+1'ENA+00031142++++20130107+6330+3,536300+223,85+1'ENA+00040750++++20130107+++122,00+1'ENA+00040120++++20130107+++0,55+1'EZV+19,75+04+Biopsienadel'EZV+10,00+05+Pauschale+10,00'EZV+10,00+06+Pauschale+5,00'"));
        }
        if(fallnr.endsWith("4") || fallnr.endsWith("5") || fallnr.endsWith("6")) {
            billMessages.add(new String("UNH+00001+RECH:07:000:00'FKT+10+01+260500005+100500016'INV+123456002+10001+0812+2008-00002+00002+AZ00002'NAD+Beispielname2+Beispielvorname2+m'CUX+EUR'REC+RE20080002+20080205+02+20080114+2775,35'ZLG+190,00+2'FAB+2800'ENT+41092800+114,02+20080109+20080111+0+1'ENT+7010B05Z+1064,80+20080114+200801018+1'ENT+7110B05Z+140,80+20080119+20080131+13'ENT+47200002+14,48+20080114+20080131+1'ENT+46005000+1,29+20080114+20080131+1'ENT+48000001+0,90+20080114+20080131+1'ENT+47100001+0,64+20080114+20080131+1'ENT+42092800+40,90+20080114+20080205+2+1'UNT+17+00001'"));
        }
        if(fallnr.endsWith("7") || fallnr.endsWith("8") || fallnr.endsWith("9")) {
            billMessages.add(new String("UNH+00001+RECH:07:000:00'FKT+10+01+260500005+100500016'INV+123456004+10001+0812+2008-00004+00004+AZ00004'NAD+Beispielname4+Beispielvorname4+w'CUX+EUR'REC+RE20080004+20080107+52+20080102+1246,18'FAB+2500'ENT+7010O60D+1249,60+20080102+20080105+1'ENT+47200002+6,25+20080102+20080105+1'ENT+46005000+1,29+20080102+20080105+1'ENT+48000001+0,90+20080102+20080105+1'ENT+47100001+0,64+20080102+20080105+1'UNT+13+00001'"));
        }

        if(billMessages.size() > 0) 
        {
            ArrayList<String> sapBillingSegments = extractBillingSegmentsFrom301(billMessages);
            if(sapBillingSegments.size() > 0) 
            {
                billingDate = extractBillingDatesFromSegments(sapBillingSegments);
            }            
        }

        return billingDate;
    }

    /**
     * Extrahiert das REC-Segment aus der §301-Nachricht
     *
     * @param pBillMessages §301-Nachrichten
     * @return ArrayList<String>
     */
    private static ArrayList<String> extractBillingSegmentsFrom301(final ArrayList<String> pBillMessages) {

        ArrayList<String> extractedBillingSegments = new ArrayList<String>();

        for(int i = 0, n = pBillMessages.size(); i < n; i++)
        {
            String billMessage = pBillMessages.get(i);
            if(billMessage != null)
            {
                String[] arySegments = billMessage.split("\'");
                if (arySegments != null && arySegments.length > 0)
                {
                    for(int j = 0, m = arySegments.length; j < m; j++)
                    {
                        String segment = arySegments[j];
                        if (segment != null && segment.startsWith("REC+")) {
                            extractedBillingSegments.add(segment);
                            break;
                        }
                    }
                }
            }
        }
        return extractedBillingSegments;
    }

    /**
     * Extrahiert das Rechnnungsdatum der REC-Segmente aus der §301-Nachricht der Schlussrechung
     *
     * @param pBillSegments Rechungssegmente der §301-Nachrichten
     * @param pFallnr Fall-Nr.
     * @param fallid Fall-ID
     * @param pIkz IKZ
     * @return java.sql.Date
     */
    private static java.sql.Date extractBillingDatesFromSegments(final ArrayList<String> pBillSegments) throws ParseException{

        java.sql.Date extractedBillingDate = null;
        String strBillDate = null;

        for(int i = 0, n = pBillSegments.size(); i < n; i++)
        {
            String billSegement = pBillSegments.get(i);
            if(billSegement != null)
            {
                String[] aryValues = billSegement.split("\\+");
                if (aryValues != null && aryValues.length >= 4)
                {
                    String billKeyNumber = aryValues[3];
                    if(billKeyNumber != null && (billKeyNumber.equals("02") || billKeyNumber.equals("52"))) {
                        strBillDate = aryValues[2];
                        break;
                    }
                }
            }
        }

        if(strBillDate != null)
        {
            extractedBillingDate = StrUtils.parseDate(strBillDate, FORMAT_DATE);
        }
        return extractedBillingDate;
    }

    private Integer checkInteger(String number)
    {
	int i_number=0;
            try {
                i_number=Integer.parseInt(number);
            } catch(NumberFormatException ex) {
                if(number.contains(",") || number.contains(".")){
                    number = number.replace(',', '.');
                    float fl = Float.parseFloat(number);
                    return Integer.valueOf((int)Math.ceil(fl));
                }

            }

        return i_number;
    }

}

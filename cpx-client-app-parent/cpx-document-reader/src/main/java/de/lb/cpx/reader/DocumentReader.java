/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.reader;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import de.lb.cpx.document.Utils;
import static de.lb.cpx.document.Utils.*;
import de.lb.cpx.document.Utils.FILE_TYPES;
import de.lb.cpx.document.Utils.OCR_TYPES;
import de.lb.cpx.reader.exception.ExcelNotFoundException;
import de.lb.cpx.reader.exception.OfficeDisabledException;
import de.lb.cpx.reader.exception.OutlookNotFoundException;
import de.lb.cpx.reader.exception.ReaderException;
import de.lb.cpx.reader.exception.ReaderExceptionTypeEn;
import de.lb.cpx.reader.exception.WordNotFoundException;
import de.lb.cpx.reader.util.CaseNumberResult;
import de.lb.cpx.reader.util.Document;
import de.lb.cpx.shared.lang.Lang;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author nandola
 */
public class DocumentReader {

    private static final Logger LOG = Logger.getLogger(DocumentReader.class.getSimpleName());
    public static final int NUMBER_OF_THREADS = 4;
    public static final OCR_TYPES OCR_TYPE = OCR_TYPES.WORD;
    public static final double WORD_VERSION;
    public static final double EXCEL_VERSION;
    public static final double OUTLOOK_VERSION;
    public static final double MIN_OFFICE_VERSION = 15.0d; //15 = Office 2013 and higher supports PDF
    private static boolean OFFICE_ENABLE = true;
    private static boolean OFFICE_OLD;

    public static final boolean WORD_FOUND;
    public static final boolean EXCEL_FOUND;
    public static final boolean OUTLOOK_FOUND;
    public static final boolean WORD_OLD;
    public static final boolean EXCEL_OLD;
    public static final boolean OUTLOOK_OLD;

    public static final boolean OFFICE_FOUND;

    static {
        LOG.log(Level.INFO, "Seeking for Microsoft Office...");
        Utils.initJacob();
        WORD_VERSION = getWordVersion();
        EXCEL_VERSION = getExcelVersion();
        OUTLOOK_VERSION = getOutlookVersion();
        WORD_FOUND = WORD_VERSION > 0;//isWordFound();
        EXCEL_FOUND = EXCEL_VERSION > 0;//isExcelFound();
        OUTLOOK_FOUND = OUTLOOK_VERSION > 0;//isOutlookFound();
        
        OFFICE_FOUND = WORD_FOUND || EXCEL_FOUND || OUTLOOK_FOUND;
        WORD_OLD = WORD_VERSION> 0 && WORD_VERSION < MIN_OFFICE_VERSION;
        EXCEL_OLD = EXCEL_VERSION> 0 && EXCEL_VERSION < MIN_OFFICE_VERSION;
        OUTLOOK_OLD = OUTLOOK_VERSION> 0 && OUTLOOK_VERSION < MIN_OFFICE_VERSION;
        OFFICE_OLD = WORD_OLD || EXCEL_OLD || OUTLOOK_OLD;
        
        LOG.log(Level.INFO, getOfficeInformation());
//                OFFICE_FOUND?
//                        ( "Detected Microsoft Office Tools: "
//                + (WORD_FOUND?("Word " + getWordVersionName(WORD_VERSION)):Lang.getMsWordNotFound()) + ", "
//                + (EXCEL_FOUND?("Excel " + getExcelVersionName(EXCEL_VERSION)):Lang.getMsExcelNotFound()) + ", "
//                + (OUTLOOK_FOUND?("Outlook " + getOutlookVersionName(OUTLOOK_VERSION)):Lang.getMsOutlookNotFound()))
//                :Lang.getMsOfficeNotFound());
    }

    public static String getOfficeInformation() {
        if (OFFICE_FOUND) {
            final String placeholder = "---";
            return "Word " + (WORD_FOUND ? getWordVersionName(WORD_VERSION) : placeholder) + ", "
                    + "Excel " + (EXCEL_FOUND ? getExcelVersionName(EXCEL_VERSION) : placeholder) + ", "
                    + "Outlook " + (OUTLOOK_FOUND ? getOutlookVersionName(OUTLOOK_VERSION) : placeholder);
        } else {
            return Lang.getMsOfficeNotFound();
        }
    }

    public DocumentReader() {
        //Utils.initJacob();
        //initTess4j();
    }

//    private void initTess4j() {
//        System.load("C:\\Program Files\\gs\\gs9.25\\bin\\gsdll64.dll");
//    }
//    private void initTess4j() {
//        String p = OS.getSharedLibraryLoaderPath(); //checking the operating system
//        p = new File(p).getParentFile().getAbsolutePath();
//        LOG.log(Level.FINEST, "Set TESS DLL path to '" + p + "'");
//        System.setProperty(LibraryLoader.JACOB_DLL_PATH, p + "\\dlls\\x64\\gsdll64.dll");
//        System.setProperty(LibraryLoader.JACOB_DLL_PATH, p + "\\dlls\\x64\\liblept168.dll");
//        System.setProperty(LibraryLoader.JACOB_DLL_PATH, p + "\\dlls\\x64\\libtesseract302.dll");
//        System.setProperty("com.jacob.debug", "false");
//        LibraryLoader.loadJacobLibrary(); // loading the Jacob library
//    }
    public static Set<Document> getText(final String pInputDirectory) {
        final boolean includeSubDirs = true;
        return getText(pInputDirectory, includeSubDirs);
    }

    public static Set<Document> getText(final String pInputDirectory, final boolean pIncludeSubDirs) {
        final File outputDir = new File(System.getProperty("java.io.tmpdir") + "\\" + System.nanoTime());
        return getText(pInputDirectory, outputDir.getAbsolutePath(), pIncludeSubDirs);
    }

    public static Set<Document> getText(final String pInputDirectory,
            final String pOutputDirectory) {
        final boolean includeSubDirs = true;
        return getText(pInputDirectory, pOutputDirectory, includeSubDirs);
    }

    public static Set<Document> getText(final String pInputDirectory,
            final String pOutputDirectory, final boolean pIncludeSubDirs) {
        if (pInputDirectory == null || pInputDirectory.isEmpty()) {
            throw new IllegalArgumentException("no input directory passed!");
        }

        if (pOutputDirectory == null || pOutputDirectory.isEmpty()) {
            throw new IllegalArgumentException("no input directory passed!");
        }

        final File inputDir = new File(pInputDirectory);
        final File outputDir = new File(pOutputDirectory);

        if (!inputDir.exists()) {
            throw new IllegalArgumentException("input directory does not exist!");
        }

        if (!outputDir.exists()) {
            LOG.log(Level.INFO, "output directory not found, will try to create it: " + outputDir.getAbsolutePath());
            outputDir.mkdirs();
            if (!outputDir.exists()) {
                throw new IllegalArgumentException("input directory does not exist!");
            }
        }

        if (inputDir.isFile() || !inputDir.canRead()) {
            throw new IllegalArgumentException("input path is not a directory but a file or no permissions to read!");
        }

        if (outputDir.isFile() || !outputDir.canWrite() || !Files.isWritable(outputDir.toPath())) {
            throw new IllegalArgumentException("output path is not a directory but a file or no permissions to write!");
        }

        LOG.log(Level.INFO, "Start batch export with input directory " + inputDir.getAbsolutePath() + " and output directory " + outputDir.getAbsolutePath());

        final Set<Document> result = new LinkedHashSet<>();
        File[] files = inputDir.listFiles();
        LOG.log(Level.INFO, "Found " + files.length + " entries in " + inputDir.getAbsolutePath());
        for (File file : files) {
            if (file.isDirectory()) {
                if (pIncludeSubDirs) {
                    final Set<Document> subResult = getText(file.getAbsolutePath(), outputDir.getAbsolutePath() + "\\" + file.getName(), pIncludeSubDirs);
                    result.addAll(subResult);
                }
                continue;
            }
            try {
                Document doc = DocumentReader.getText(file);
                if (doc != null) {
                    final String sourceName = file.getName();
                    final String targetTxtFile = outputDir.getAbsolutePath() + "\\" + FilenameUtils.removeExtension(sourceName) + ".txt";
                    try ( BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetTxtFile), "UTF-8"))) {
                        writer.append(doc.getText());
                    }
                    result.add(doc);
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Was not able to read from file: " + file.getAbsolutePath(), ex);
            }
        }
        return result;
    }

    public static Set<Document> getText(final File[] pInputFiles) {
        final boolean applyCorrections = true;
        final DocumentReader reader = new DocumentReader();
        return reader.getText(pInputFiles, applyCorrections);
    }

    public Set<Document> getText(final File[] pInputFiles, final boolean pApplyCorrections) {
        Set<Document> result = new LinkedHashSet<>();
        if (pInputFiles == null || pInputFiles.length == 0) {
            return result;
        }

//        final File outputDir = new File(System.getProperty("java.io.tmpdir"));
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(NUMBER_OF_THREADS);
        for (File file : pInputFiles) {
//            final String sourcePath = file.getAbsolutePath();
            final String sourceName = file.getName();
//            final String targetTxtFile = outputDir.getAbsolutePath() + "\\" + FilenameUtils.removeExtension(sourceName) + ".txt";
//            if (name.endsWith(".txt")) {
//                continue;
//            }
            if (sourceName.endsWith("_")) {
                continue;
            }
            if (sourceName.startsWith("~$")) {
                continue;
            }
//            if (name.startsWith(".zip")) {
//                continue;
//            }
            scheduler.execute(() -> {
                try {
                    final String text;
                    final FILE_TYPES fileType = getFileType(file);

                    if ((fileType.isTessable() && OCR_TYPE.isTess()) || fileType.isImage()) {
                        //TESS4J
                        String t = extractTextViaTess(file);
                        if (t == null || t.trim().isEmpty()) {
                            //some files cannot be processed with Tesseract, use Word instead as fallback
                            t = extractTextViaWord(file);
                        }
                        text = t;
                    } else {
                        //WORD
                        String t = extractTextViaWord(file);
                        if (t == null || t.trim().isEmpty()) {
                            //maybe word is not available or something went wrong, then use Tesseract instead
                            if (fileType.isTessable() && OCR_TYPE.isTess()) {
                                t = extractTextViaTess(file);
                            }
                        }
                        text = t;
                    }
                    Document doc = new Document(file, text);
                    result.add(doc);
//                    Document doc = DocumentReader.getText(new File(sourcePath));
//                    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetTxtFile), "UTF-8"))) {
//                        writer.append(doc.getText());
//                    }
                    //final String caseNumber = doc.getCaseNumber();
                    //LOG.log(Level.INFO, "detected case number for " + sourceName + ": " + caseNumber);
//                    result.put(file, new File(targetTxtFile));
                } catch (IOException | IllegalArgumentException | ReaderException ex) {
                    LOG.log(Level.SEVERE, "Was not able to read from file: " + file.getAbsolutePath(), ex);
                }
            });
        }
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(60, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }

        return result;
        //return getText(pInFile, pApplyCorrections);
    }

    /**
     * Extracts text from file (primarly to get text from pdf)
     *
     * @param pInputFile (pdf) file
     * @param pApplyCorrections try to correct text?
     * @return text
     * @throws IOException cannot process file
     */
    public static Document getText(final File pInputFile, final boolean pApplyCorrections) {
        if (pInputFile == null) {
            return null;
        }
        final DocumentReader reader = new DocumentReader();
        Set<Document> result = reader.getText(new File[]{pInputFile}, pApplyCorrections);
        if (result.isEmpty()) {
            return null;
        }
        Iterator<Document> it = result.iterator();
        while (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    /**
     * Extracts text from file (primarly to get text from pdf)
     *
     * @param pInputFile (pdf) file
     * @return text
     * @throws IOException cannot process file
     */
    public static Document getText(final File pInputFile) throws IOException {
        final boolean applyCorrections = true;
        return getText(pInputFile, applyCorrections);
    }

    public static String getCorrectedText(final String pText) {
        final DocumentReader documentReader = new DocumentReader();
        final String text = documentReader.applyCorrections(pText);
        return text;
    }

    public static Set<CaseNumberResult> getCaseNumbers(final String pText) {
        final DocumentReader documentReader = new DocumentReader();
        Set<CaseNumberResult> results = documentReader.readCaseNumber(pText);
        return results;
    }

    public static String getCaseNumber(final String pText) {
        final DocumentReader documentReader = new DocumentReader();
        final String caseNumber = documentReader.getSingleCaseNumber(pText);
        return caseNumber;
    }

    public static String getPatientNumber(final String pText) {
        final DocumentReader documentReader = new DocumentReader();
        final String patientNumber = documentReader.readPatientNumber(pText);
        return patientNumber;
    }

    public static String getMdkAddress(final String pText) {
        final DocumentReader documentReader = new DocumentReader();
        final String mdkAddress = documentReader.readMdkAddress(pText);
        return mdkAddress;
    }

    public String getSingleCaseNumber(final String pText) {
        Set<CaseNumberResult> results = readCaseNumber(pText);
        String caseNumber = "";
        if (results == null || results.isEmpty()) {
            return caseNumber;
        }
        final List<String> candidates1 = new ArrayList<>();
        for (CaseNumberResult result : results) {
            for (String val : result.value) {
                if (val.length() > 4 && val.length() <= 14) {
                    candidates1.add(val);
                }
            }
        }
        String bestCandidate = "";
        final int optimalLength = 10;
        for (String val : candidates1) {
            if (bestCandidate.isEmpty() || Math.abs(val.length() - optimalLength) < Math.abs(bestCandidate.length() - optimalLength)) {
                bestCandidate = val;
            }
        }
        return bestCandidate;
    }

    public Set<CaseNumberResult> readCaseNumber(final String pText) {
        Set<CaseNumberResult> results = new TreeSet<>();
        Map<String, Integer> resultCount = new HashMap<>();
        if (pText == null) {
            return results;
        }
        String text = pText.trim().toLowerCase();
        if (text.isEmpty()) {
            return results;
        }
        for (Map.Entry<String, String> entry : CaseNumberResult.getKeys().entrySet()) {
            final String key = entry.getKey();
            final String name = entry.getValue();
            Set<String> values = readIntField(text, key, 5);
            if (!values.isEmpty()) {
                boolean supKeyExists = false;
                for (Map.Entry<String, Integer> entry2 : resultCount.entrySet()) {
                    if (entry2.getKey().contains(key)) {
                        supKeyExists = true;
                    }
                }
                if (supKeyExists) {
                    //nothing here
                } else {
                    String[] vals = new String[values.size()];
                    values.toArray(vals);
                    CaseNumberResult result = new CaseNumberResult(key, name, vals);
                    results.add(result);
                    resultCount.put(key, vals.length);
                }
            }
        }
        return results;
    }

    protected Set<String> readIntField(final String pText, final String pKey, final int pMinLength) {
        String tmp = pText;
        Set<String> values = new LinkedHashSet<>();
        int pos;
        while ((pos = tmp.indexOf(pKey)) > -1) {
            if (pos == 0) {
                continue;
            }
            char c = tmp.charAt(pos - 1);
            boolean isFirst = (c == '\r' || c == '\n'); //is first information in line?
            tmp = tmp.substring(pos + pKey.length()).trim();
            String value = "";
            int i = 0;
            char preCh = ' ';
            boolean cont = false;
            boolean lineBreak = false;
            int lineBreakLine = -1;
            while (true) {
                if (i >= tmp.length()) {
                    break;
                }
                char ch = tmp.charAt(i);
                if (ch == ':') {
                    i++;
                    continue;
                }
                if (ch == '.') {
                    i++;
                    continue;
                }
                if (ch == ' ') {
                    i++;
                    continue;
                }
                if (ch == '\t') {
                    i++;
                    continue;
                }
                if (ch == '\r' || ch == '\n') {
                    lineBreak = true;
                    lineBreakLine = i;
                    i++;
                    continue;
                }
                if (isFirst) {
                    if (!(ch >= '0' && ch <= '9')) {
                        i++;
                        continue;
                    } else {
                        if (!(preCh == '\r' || preCh == '\n') && (lineBreak && i != lineBreakLine + 1) && isFirst) {
                            cont = true;
                            break;
                        }
                    }
                }
                if (i > 600) {
                    cont = true;
                    break;
                }
                preCh = ch;
                break;
            }
            if (cont) {
                continue;
            }
            while (true) {
                if (i > 700) {
                    cont = true;
                    break;
                }
                if (i >= tmp.length()) {
                    break;
                }
                char ch = tmp.charAt(i);
                char nextCh;
                if (tmp.length() > i + 1) {
                    nextCh = tmp.charAt(i + 1);
                } else {
                    nextCh = ' ';
                }
//                try {
//                    nextCh = tmp.charAt(i + 1);
//                } catch (java.lang.StringIndexOutOfBoundsException ex) {
//                    nextCh = ' ';
//                }

                //char overNextCh = tmp.charAt(i+2);
//                    if (ch == 'l') {
//                        value += String.valueOf(ch);
//                    } if (ch == 'c') {
//                        value += String.valueOf(ch);
//                    } if (ch == 'o') {
//                        value += String.valueOf(ch);
//                    } else 
                if ((ch >= '0' && ch <= '9') || ch == 'x') {
                    //is this a date? -> then stop here!
                    String subStr = tmp.length() >= (i + 10) ? tmp.substring(i, i + 10) : "";
                    if (subStr.contains(".")) {
                        try {
                            DateFormat dateformat = new SimpleDateFormat("dd.mm.yyyy");
                            dateformat.parse(subStr);
                            break;
                        } catch (ParseException ex) {
                            LOG.log(Level.FINEST, "is no date: " + subStr, ex);
                        }
                    }
                    value += String.valueOf(ch);
                } else {
                    if (!(ch == ' ' && nextCh >= '0' && nextCh <= '9')) {
                        if (value.length() >= pMinLength) {
                            break;
                        } else {
                            value = "";
                        }
                    }
                }
                i++;
            }
            value = value.trim();
            if (!value.isEmpty()) {
                values.add(value);
            }
        }
        return values;
    }

    public String readPatientNumber(final String pText) {
        String result = "";
        String text = pText.trim();
        //(KV-Nummer|Vers. Nr.|Patientennr.|Patientennummer):?\s([A-Z]\d{8,9}_?K*)
        if (text == null) {
            return result;
        }
        if (text.isEmpty()) {
            return result;
        }
        result = readPatientValue(text);
        return result;
    }

    public String readPatientValue(final String pText) {
        Reader stringReader = new StringReader(pText);
        BufferedReader br = new BufferedReader(stringReader);
        Pattern patientValuePattern = Pattern.compile("[^A-Z]([A-Z][\\d]{8,9}[_[K]*]?)");
        Matcher matcher = null;
        String currentLine = "";
        String patientValue = "";
        try {
            while ((currentLine = br.readLine()) != null) {
                matcher = patientValuePattern.matcher(currentLine);
                if (matcher.find()) {
                    patientValue = matcher.group(1);
                }
//                    currentLine.getChars(currentLine.indexOf(pKey.length()+2),currentLine.indexOf(pKey.length()+12) , ch, 10);
//                    while ((pos = currentLine.indexOf(pKey)) > -1) {
//                        for (int i = 2; i <= 11; i++) {
//                            patientValue += currentLine.charAt(currentLine.indexOf(pKey) + pKey.length() + i);
//                        }
//                        break;
//
//                    }
            }
            stringReader.close();
            br.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't read patient text", ex);
        }

        return patientValue;
    }

    public String readMdkAddress(String pText) {
        String mdkAddress = "";
        String text = pText.trim();

        if (text == null) {
            return mdkAddress;
        }
        if (text.isEmpty()) {
            return mdkAddress;
        }
        mdkAddress = readMdkAddressValue(text);
        return mdkAddress;
    }

    public String readMdkAddressValue(String pText) {
        String mdkName = "";
        String mdkStreet = "";
        String mdkZipCode = "";
        String mdkAddress = "";
        Reader stringReader = new StringReader(pText);
        BufferedReader br = new BufferedReader(stringReader);
        Pattern pattern = Pattern.compile("\\b(MDK Berlin-Brandenburg e. V.|MDK Berlin—Brandenburg e. V.)[?,*?\\s*]?\\s*(.*[.\\w+$]).*[?,*?\\s*]([0-9]\\d{0,4}.\\w+)\\b");
        Matcher matcher = null;
        String currentLine = "";
        try {
            while ((currentLine = br.readLine()) != null) {
                matcher = pattern.matcher(currentLine);
                if (matcher.find()) {
//                    mdkAddress = matcher.group();
                    mdkName = matcher.group(1);
                    mdkStreet = matcher.group(2);
                    mdkZipCode = matcher.group(3);
                }
            }
            br.close();
            stringReader.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't read mdk address", ex);
        }
        mdkAddress = mdkName + ", " + mdkStreet + ", " + mdkZipCode;
        return mdkAddress;
    }

    public String applyCorrections(final String pText) {
        if (pText == null) {
            return "";
        }
        String text = pText.trim();
        if (text.isEmpty()) {
            return "";
        }
//        String text = pText.trim().toLowerCase();
        while (true) {
            String textOld = text;
            text = text.replace("  ", " ");
            if (textOld.equalsIgnoreCase(text)) {
                break;
            }
        }
        final Map<String, String> corrections = new LinkedHashMap<>();
        corrections.put("Aufm-Nr", "Aufn-Nr");
        corrections.put("Aufm.-Nr", "Aufn.-Nr");
        corrections.put("Falnummer", "Fallnummer");
        corrections.put("entspert", "entsperrt");
        corrections.put("KRANKENVERStCHERUNG", "KRANKENVERSICHERUNG");
        corrections.put("KRAÄANKENVERSICHERUNG", "KRANKENVERSICHERUNG");
        corrections.put("Sozialqericht", "Sozialgericht");
        corrections.put("MEDIZIN\\SCHER", "MEDIZINISCHER");
        corrections.put("üiENST", "DIENST");
        corrections.put("Fallinummer", "Fallnummer");
        corrections.put("Falinummer", "Fallnummer");
        corrections.put("KV-Nru:", "KV-Nr.:");
        corrections.put("DiIENST", "DIENST");
        corrections.put(" DEENST", " DIENST");
        corrections.put("MEDRZ\\N$SCHER", "MEDIZINISCHER");
        corrections.put("MEDfZlNlSCHER", "MEDIZINISCHER");
        corrections.put("MEDIZiNlSCHER", "MEDIZINISCHER");
        corrections.put("MEDtziNiscHER DEENST", "MEDIZINISCHER DIENST");
        corrections.put("MEDIZINISCHER DEENST", "MEDIZINISCHER DIENST");
        corrections.put("OER KRANKENVERSSCHERUNG", "DER KRANKENVERSICHERUNG");
        corrections.put("OER KRANKENVERS}CHERUNG", "DER KRANKENVERSICHERUNG");
        corrections.put("nzahi", "Anzahl");
        corrections.put("orliegende", "vorliegende");
        corrections.put("VallLNr,", "Fall-Nr.");
        corrections.put("VallLNr", "Fall-Nr.");
        corrections.put("Fall-Nr. des kKrankenhauses", "Fall-Nr. des Krankenhauses");
        corrections.put("Fall—Nr", "Fall-Nr");
        corrections.put("Fa Il-Nr", "Fall-Nr");
        corrections.put("einweisungsdia nose", "Einweisungsdiagnose");
        corrections.put("grenzverweilda uer", "Grenzverweildauer");
        corrections.put("berl in", "Berlin");
        corrections.put("www", "www.");
        corrections.put(" @ ", "@");
        corrections.put("mok der krankenv", "MDK der Krankenv");
        corrections.put("mok berlin-brandenburg", "MDK Berlin-Brandenburg");
        corrections.put("ubersicht", "übersicht");
        corrections.put("oer krankenversicherung", "der Krankenversicherung");
        corrections.put("fa il-nr.", "Fall-nr.");
        corrections.put("fail-nr.", "Fall-nr.");
        corrections.put("unaussetzung", "um Aussetzung");
        corrections.put("mahny±hr-ens", "Verfahrens");
        corrections.put("wuppertl", "Wuppertal");
        corrections.put("weitæbe", "weitergabe");
        corrections.put("ebindung", "Entbindung");
        corrections.put("auüendungen", "Aufwendungen");
        corrections.put("kranken-ipflegeversicherten", "Kranken-/pflegeversicherten");
        corrections.put("ipfleaeversicherten", "Pflegeversicherten");
        corrections.put("ges. dauer", "Gesamtdauer");
        corrections.put("geschäftszeichen", " Geschäftszeichen");
        corrections.put("berlinv", "Berlin ");
        corrections.put("teiefon", "Telefon");
        corrections.put("servlce", "Service");
        corrections.put("lich enberg", "Lichtenberg");
        corrections.put("k'iniken", "Kliniken");
        corrections.put("mit'freundlichen", "mit freundlichen");
        corrections.put("charite", "Charité");
        corrections.put("charité", "Charité");
        corrections.put("regionalgeschäftsstetle", "Regionalgeschäftsstelle");
        corrections.put("datum:kvnr", "Datum KVNR");
        corrections.put("begr, ndung", "Begründung");
        corrections.put("p)ausibel", "plausibel");
        corrections.put("sperrfns", "Sperrfrist");
        corrections.put("nachrichtenstalus_", "Nachrichtenstatus");
        corrections.put("medizinconlrolhng", "Medizincontrolling");
        corrections.put("•ndheit", "Gesundheit");
        corrections.put("vorsitend", "vorsitzend");
        corrections.put("vereinb.", "Vereinbarung");
        corrections.put("hauptver\"taltung", "Hauptverwaltung");
        corrections.put("lichtcnbcrg", "Lichtenberg");
        corrections.put("fanningcrstrabc32", "Fanningerstr");
        corrections.put("klinikurn", "Klinikum");
        corrections.put("bcrlin", "Berlin");
        corrections.put("universitâtsmedizin", "Universitätsmedizin");
        corrections.put("geschâftsführung", "Geschäftsführung");
        corrections.put("fanningerstrag", "Fanningerstr");
        corrections.put("wirbelkôrp", "Wirbelkörp");
        corrections.put("wirbelkòrperersatz", "Wirbelkörperersatz");
        corrections.put("wirbelsàul", "Wirbelsäul");
        corrections.put("veranl.", "veranlassende");
        corrections.put("medizlncontrol", "Medizincontrol");
        corrections.put("fanningerslraß", "Fanningerstr");
        corrections.put("chrlstlan", "Christian");
        corrections.put("schweigepflichtenentbindung", " Schweigepflichtenentbindung");
        corrections.put("untemehmen", "Unternehmen");
        corrections.put("be ründen", "begründen");
        corrections.put("erei nisrekorder", "Ereignisrekorder");
        corrections.put("fahübersicht", "Fallübersicht");
        corrections.put("fehier", "Fehler");
        corrections.put("3fehier", "3 Fehler");
        corrections.put(" eschah", "geschah");
        corrections.put("oriageart", "Vorlageart");
        corrections.put("abrechnungspröfung", "Abrechnungsprüfung");
        corrections.put("geschäæbereich", "Geschäftsbereich");
        corrections.put("fordem", "fordern");
        corrections.put("wundtoiiett", "Wundtoilett");
        corrections.put("sacraien", "sacralen");
        corrections.put(" rztliche", " ärtzliche");
        corrections.put("teleiax", "Telefax");
        corrections.put("l<rankenhaus", "Krankenhaus");
        corrections.put("notoendigen", "notwendigen");
        corrections.put("rücwragen", "Rückfragen");
        corrections.put("gesetlichen", "gesetzlichen");
        corrections.put("unterstüülgsmaßnahmen", "Unterstützungsmaßnahmen");
        corrections.put("mok be rl i n-brandenbu rg", "MDK Berlin-Brandenburg");
        corrections.put("aufnehmende bteilung", "aufnehmende abteilung");
        corrections.put("fraugeb", "Frau geb");
        corrections.put("patientenverwa[tung", "patientenverwaltung");
        corrections.put("3fehler", "3 Fehler");
        corrections.put("mdr- gutachten", "MDK-Gutachten");
        corrections.put("stelfiger", "stelliger");
        corrections.put("komplexbehandiung", "Komplexbehandlung");
        corrections.put("aufm-nr.", "Aufn-nr.");
        corrections.put("lich+enberg", "Lichtenberg");
        //corrections.put("oer krankenversscherung", "der Krankenversicherung");
        //corrections.put("oer krankenversicherung", "der Krankenversicherung");
        corrections.put("mitwirkungsobläegenheiten", "Mitwirkungsobliegenheiten");
        corrections.put("behandlungszjel", "Behandlungsziel");
        corrections.put("medtzinisch", "medizinisch");
        corrections.put("prufanzeig", "Prüfanzeig");
        corrections.put("bearbeiter:", "Bearbeiter: ");
        corrections.put("medizincontro'l", "Medizincontrol");
        corrections.put("richti ", "richtig");
        corrections.put("qs-zuschl", "qs-zuschl");
        corrections.put("engriffe", "Eingriffe");
        corrections.put("ernail", "E-Mail");
        corrections.put("fâlligkeitsdatum", "Fälligkeitsdatum");
        corrections.put("behand!ung", "Behandlung");
        corrections.put("behandlungjgcg", "Behandlung jgcg");
        corrections.put("RücWragen", "Rückfragen");
        corrections.put("unAussetzung", "um Aussetzung");
        corrections.put("wirkich", "wirklich");
        corrections.put("Fll", "Fall");
        //corrections.put("rztl", "ärztl");

        for (Map.Entry<String, String> entry : corrections.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
            text = text.replace(entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), entry.getValue().substring(0, 1).toUpperCase() + entry.getValue().substring(1));
        }

        while (true) {
            String textOld = text;
            text = text.replace(" ", " ");
            text = text.replace("..", ".");
            text = text.replace(",,", ",");
            text = text.replace("::", ":");
            text = text.replace("//", "/");
            text = text.replace("\\\\", "\\");
            if (textOld.equalsIgnoreCase(text)) {
                break;
            }
        }

        //String[] s = text.split("\r\n|\r|\n|\t|\\s");
        final List<String> tokens = new ArrayList<>();
        String word = "";
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            final boolean ctrlChar = isControlChar(ch);
            if (ctrlChar) {
                if (!word.trim().isEmpty()) {
                    tokens.add(word);
                }
                tokens.add(String.valueOf(ch));
                word = "";
            } else {
                word += String.valueOf(ch);
            }
        }
        if (!word.trim().isEmpty()) {
            tokens.add(word);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.length() == 1 && isControlChar(token.charAt(0))) {
                sb.append(token);
                continue;
            }
            String prevToken = i > 0 ? tokens.get(i - 1) : "";
            String nextToken = i < tokens.size() - 2 ? tokens.get(i + 2) : "";
            String overnextToken = i < tokens.size() - 4 ? tokens.get(i + 4) : "";
            String t = correctToken(prevToken, token, nextToken, overnextToken);
            if (t != null && !t.trim().isEmpty()) {
                sb.append(t + " ");
                tokens.set(i, t);
            }
        }

        sb = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            sb.append(token);
        }
        text = sb.toString().trim();
        return text;
    }

    protected static boolean isControlChar(char pCh) {
        final boolean ctrlChar = pCh == '\n' || pCh == '\r' || pCh == '\t'
                || pCh == ' ' || pCh == ':' || pCh == ',' || pCh == '/'
                || pCh == '\\' || pCh == '(' || pCh == ')' /* || pCh == '-' */
                || pCh == '.' || pCh == ';' || pCh == '@' || pCh == '?'
                || pCh == '!' || pCh == '_';
        return ctrlChar;
    }

    protected String correctToken(final String pPrevToken, final String pToken, final String pNextToken, final String pOverNextToken) {
        if (pToken == null) {
            return "";
        }
        String token = pToken.trim();
        //String token = tokenCs.toLowerCase();
        if (token == null || token.isEmpty()) {
            return "";
        }
//        if (token.length() < 3) {
//            return "";
//        }
//        final String prevToken = pPrevToken == null ? "" : pPrevToken.trim();
        final String nextToken = pNextToken == null ? "" : pNextToken.trim();
//        final String overnextToken = pOverNextToken == null ? "" : pOverNextToken.trim();
//        String t = token;
//        t = t.replace("0", "");
//        t = t.replace("1", "");
//        t = t.replace("2", "");
//        t = t.replace("3", "");
//        t = t.replace("4", "");
//        t = t.replace("5", "");
//        t = t.replace("6", "");
//        t = t.replace("7", "");
//        t = t.replace("8", "");
//        t = t.replace("9", "");
//        t = t.replace(".", "");
//        t = t.replace(",", "");
//        t = t.replace("/", "");
//        t = t.replace("\\", "");
//        t = t.replaceFirst("a", "");
//        t = t.replaceFirst("b", "");
//        t = t.replaceFirst("c", "");
//        t = t.replaceFirst("d", "");
//        t = t.replaceFirst("e", "");
//        t = t.replaceFirst("f", "");
//        t = t.replaceFirst("g", "");
//        t = t.replaceFirst("h", "");
//        t = t.replaceFirst("i", "");
//        t = t.replaceFirst("j", "");
//        t = t.replaceFirst("k", "");
//        t = t.replaceFirst("l", "");
//        t = t.replaceFirst("m", "");
//        t = t.replaceFirst("n", "");
//        t = t.replaceFirst("o", "");
//        t = t.replaceFirst("p", "");
//        t = t.replaceFirst("q", "");
//        t = t.replaceFirst("r", "");
//        t = t.replaceFirst("s", "");
//        t = t.replaceFirst("t", "");
//        t = t.replaceFirst("u", "");
//        t = t.replaceFirst("v", "");
//        t = t.replaceFirst("w", "");
//        t = t.replaceFirst("x", "");
//        t = t.replaceFirst("y", "");
//        t = t.replaceFirst("z", "");
//        t = t.replaceFirst("ä", "");
//        t = t.replaceFirst("ü", "");
//        t = t.replaceFirst("ö", "");
//        t = t.replaceFirst("ß", "");
//        if (t.isEmpty()) {
//            return "";
//        }

        if (token.equals("Medizinåscher")) {
            return "Medizinischer";
        }
        if (token.equals("Medfzlnlscher")) {
            return "Medizinischer";
        }
        if (token.equals("MOK")) {
            return "MDK";
        }

        Map<String, String> corrections = new HashMap<>();
        //corrections.put("Warnun", "Warnung");
        corrections.put("ie", "die");
        corrections.put("zugeoränet", "zugeordnet");
        corrections.put("behand!ung", "Behandlung");
        corrections.put("aufm-nr.", "Aufn-nr.");
        corrections.put("MEDiZiNåSCHER", "MEDIZINISCHER");
        corrections.put("medizinåscher", "medizinischer");
        corrections.put("medfzlnlscher", "medizinischer");
        corrections.put("deenst", "Dienst");
        corrections.put("rankenvers}cherung", "Krankenversicherung");
        corrections.put("xxx", "");
        corrections.put("brandenburg bertin", "Brandenburg Berlin");
        corrections.put("brandenburg-bertin", "Brandenburg-Berlin");
        corrections.put("eleldronisch", "elektronisch");
        corrections.put("usatzentgelt", "Zusatzentgelt");
        corrections.put("kopi", "Kopie");
        corrections.put("bestätig", "bestätigt");
        corrections.put("intemet", "Internet");
        corrections.put("erden", "werden");
        corrections.put("str", "Straße");
        corrections.put("tationär", "stationär");
        corrections.put("nzahl", "Anzahl");
        corrections.put("ohn", "ohne");
        corrections.put("ufwandspunkte", "Aufwandspunkte");
        corrections.put("ufwandspunkt", "Aufwandspunkt");
        corrections.put("ufnehmend", "aufnehmend");
        corrections.put("ufnehmende", "aufnehmende");
        corrections.put("geriatri", "Geriatrie");
        corrections.put("krankenhau", "Krankenhaus");
        corrections.put("ufnahmegrund", "Aufnahmegrund");
        corrections.put("vonstationär", "vorstationär");
        corrections.put("normalfal", "Normalfall");
        corrections.put("fallkategori", "Fallkategorie");
        corrections.put("durchführungsnachwei", "Durchführungsnachweis");
        corrections.put("auftraggeb", "Auftraggeber");
        corrections.put("orlageart", "Vorlageart");
        corrections.put("aktenlag", "Aktenlage");
        corrections.put("ersichert", "versichert");
        corrections.put("ersicherte", "Versicherte");
        corrections.put("arztbrjef", "Arztbrief");
        corrections.put("rztbrief", "Arztbrief");
        corrections.put("ngaben", "Angaben");
        corrections.put("fallkonsteilation", "Fallkonstellation");
        corrections.put("verlaufskurv", "Verlaufskurve");
        corrections.put("stellungnahm", "Stellungnahme");
        corrections.put("jetigen", "jetzigen");
        corrections.put("ergebnisl", "Ergebnis");
        corrections.put("mitalieds", "Mitglieds");
        corrections.put("ergebniss", "Ergebnis");
        corrections.put("grundlag", "Grundlage");
        corrections.put("ergebni", "Ergebnis");
        //corrections.put("usatzentgelt", "Zusatzentgelt");
        corrections.put("fallkonstel", "Fallkonstellation");
        corrections.put("anlag", "Anlage");
        corrections.put("krankenkass", "Krankenkasse");
        corrections.put("uszug", "Auszug");
        corrections.put("berlin.brandenburg", "Berlin-Brandenburg");
        corrections.put("iderspruchsverfahrens", "Widerspruchsverfahrens");
        corrections.put("iderspruchsverfahren", "Widerspruchsverfahren");
        corrections.put("körperpfleg", "Körperpflege");
        corrections.put("maßnahm", "Maßnahme");
        corrections.put("energi", "Energie");
        corrections.put("mellitu", "mellitus");
        //corrections.put("ufnahmegrund", "Aufnahmegrund");
        corrections.put("frührehabilit", "Frührehabilition");
        corrections.put("übersendur*ines", "Übersendung");
        corrections.put("zusammenhan", "Zusammenhang");
        corrections.put("MDK Gutachten", "MDK-Gutachten");

        for (Map.Entry<String, String> entry : corrections.entrySet()) {
            if (token.equals(entry.getKey())) {
                return entry.getValue();
            }
            String key = entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
            if (token.equals(key)) {
                return entry.getValue();
            }
        }

        if (token.equalsIgnoreCase("ana") && nextToken.equalsIgnoreCase("klinikum")) {
            return "Sana";
        }

//        if (token.equalsIgnoreCase("")) {
//            return "";
//        }
//        if (token.equalsIgnoreCase("")) {
//            return "";
//        }
//        if (token.equalsIgnoreCase("")) {
//            return "";
//        }
        return token;
    }

    protected String extractTextViaTess(final File pInFile) {
        throw new IllegalStateException("Text extraction with Tesseract (Tess4j) is not implemented. You have to add tess4j to the dependencies, load library gsdll64.dll from GhostScript and set TESSDATA path to the trainings data directory (you can obtain Tesseract trainings data somewhere from the internet)");
    }

//    protected String extractTextViaTess(final File pInFile) {
//        checkExistingFile(pInFile);
//        final ITesseract instance = initTess();
//        try {
//            String result = instance.doOCR(pInFile);
//            return result.trim();
//        } catch (TesseractException ex) {
//            LOG.log(Level.INFO, "Was not able to extract text from file: " + pInFile.getAbsolutePath(), ex);
//            return "";
//        }
//    }
    public static void writeExcelTextToFile(final Dispatch pDocument, final String pTmpFileName) {
        //details: https://docs.microsoft.com/en-us/office/vba/api/excel.workbook.saveas
        Dispatch.call(pDocument, "SaveAs", pTmpFileName, new Variant(6) /* 6=csv */);
    }

    public static void writeWordTextToFile(final Dispatch pDocument, final String pTmpFileName) {
        //details: https://docs.microsoft.com/en-us/office/vba/api/word.saveas2
        Dispatch.call(pDocument, "SaveAs", pTmpFileName, new Variant(2) /* 2=txt */);
    }

    public static void writeOutlookTextToFile(final Dispatch pDocument, final String pTmpFileName) {
        //details: https://docs.microsoft.com/en-us/office/vba/api/outlook.mailitem.saveas
        //and https://docs.microsoft.com/en-us/office/vba/api/outlook.olsaveastype
        Dispatch.call(pDocument, "SaveAs", pTmpFileName, new Variant(0) /* 2=txt */);
    }

    public String extractTextViaWord(final File pInFile) throws IOException, ReaderException {
        final long startTime = System.currentTimeMillis();
        checkExistingFile(pInFile);
//        OfficeVersion docVersion = new OfficeVersion();
//        //checking the installation of Ms Word (-> and what about Excel?!)
//        if (!docVersion.wordInstallation()) {
//            //MainApp.showErrorMessageDialog("Microsoft Word wird nicht installiert: ");
//            throw new IllegalArgumentException("Microsoft Word is not installed!");
//        }
        // to provide full abs path of created input file.
        final String fileName = pInFile.getAbsolutePath();

        final FILE_TYPES fileType = getFileType(pInFile);
        final boolean excel = fileType.isExcel();
        final boolean word = fileType.isWord();

        LOG.log(Level.INFO, "Try to extract text from file: " + fileName + "...");

        Dispatch document = null;
        ActiveXComponent app;
        checkOfficeDisabled();
        if (excel) {
            // create ActiveXComponent for Ms Excel
            checkExcelVersion();
            app = initExcel();
        } else {
            // create ActiveXComponent for Ms Word
            checkWordVersion();
            app = initWord();
        }
        try {
            //        tmpGenerationResultProp.get().setWord(word);
            app.setProperty("Visible", new Variant(false));
            final Dispatch documents;
            if (excel) {
                // Instantiate the Documents Property
                documents = app.getProperty("Workbooks").toDispatch();
            } else {
                documents = app.getProperty("Documents").toDispatch();
            }
            document = Dispatch.call(
                    documents,
                    "Open",
                    fileName, //FileName
                    true, //ConfirmConversions
                    true //ReadOnly
            /* Weitere Parameter siehe https://msdn.microsoft.com/en-us/library/bb216319(v=office.12).aspx or https://docs.microsoft.com/en-us/office/vba/api/word.documents.open (newer) */
            ).toDispatch(); // open the inputFile to read in ms word
            String tmpFileName = System.getProperty("java.io.tmpdir") + "cpx_text_export_" + System.nanoTime() + (word ? ".txt" : ".csv");
            //word.invoke("FileSaveAs", f.getAbsolutePath());
            LOG.log(Level.INFO, "export text to temporary file: " + tmpFileName);
            if (excel) {
                writeExcelTextToFile(document, tmpFileName);
            } else {
                writeWordTextToFile(document, tmpFileName);
            }
            //Dispatch.call(document, "SaveAs", tmpFileName, word ? new Variant(2) /* 2=txt */ : new Variant(3) /* 3=csv */);
            //        Dispatch content = Dispatch.call(document, "Content").toDispatch();
            //        int end = Dispatch.call(content, "End").getInt();
            //        int start = 1;
            //        Dispatch range = Dispatch.call(document, "Range", start, end).toDispatch();
            //        Dispatch.call(range, "Select");
            //        final Clipboard clipboard = Clipboard.getSystemClipboard();
            //        Dispatch.call(range, "Copy");
            if (closeDocument(document)) {
                document = null;
            }
            if (excel) {
                if (closeExcel(app)) {
                    app = null;
                }
            } else {
                if (closeWord(app)) {
                    app = null;
                }
            }
            app = null;
            byte[] encoded = Files.readAllBytes(Paths.get(tmpFileName));
            final String result = new String(encoded, "Cp1252");
            LOG.log(Level.INFO, "Text with " + result.length() + " characters extracted from file in " + (System.currentTimeMillis() - startTime) + " ms: " + fileName + "...");
            return result.trim();
            //return clipboard.getString();
        } finally {
            if (document != null) {
                closeDocument(document);
            }
            if (app != null) {
                if (excel) {
                    closeExcel(app);
                } else {
                    closeWord(app);
                }
            }
        }
    }

    /**
     * close word document (word application will still run!)
     *
     * @param pDocument word document
     * @return did a ComFailException occur?
     */
    public static boolean closeDocument(final Dispatch pDocument) {
        if (pDocument == null) {
            return true;
        }
        try {
            Dispatch.call(pDocument, "Close", new Variant(false));
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "Was not able to close document", ex);
            return false;
        }
        return true;
    }

    /**
     * close word document (word application will still run!)
     *
     * @param pMail word document
     * @return did a ComFailException occur?
     */
    public static boolean closeMail(final Dispatch pMail) {
        if (pMail == null) {
            return true;
        }
        try {
            Dispatch.call(pMail, "Close", new Variant(1) /* 1 = Changes to the document are discarded. */);
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "Was not able to close mail", ex);
            return false;
        }
        return true;
    }

    /**
     * quit excel background process to release memory and file handlers!
     *
     * @param pExcel excel process
     * @return did a ComFailException occur?
     */
    public static boolean closeExcel(final ActiveXComponent pExcel) {
        if (pExcel == null) {
            return true;
        }
        try {
            pExcel.invoke("Quit");
            ComThread.Release();
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "Was not able to close Excel app", ex);
            return false;
        }
        return true;
    }

    /**
     * quit word background process to release memory and file handlers!
     *
     * @param pWord word process
     * @return did a ComFailException occur?
     */
    public static boolean closeWord(final ActiveXComponent pWord) {
        if (pWord == null) {
            return true;
        }
        try {
            pWord.invoke("Quit", 0);
            ComThread.Release();
            //maybe useful? -> ComThread.Release();
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "Was not able to close Word app", ex);
            return false;
        }
        return true;
    }

    /**
     * quit word background process to release memory and file handlers!
     *
     * @param pOutlook outlook process
     * @return did a ComFailException occur?
     */
    public static boolean closeOutlook(final ActiveXComponent pOutlook) {
        if (pOutlook == null) {
            return true;
        }
        try {
            pOutlook.invoke("Quit", 0);
            ComThread.Release();
            //maybe useful? -> ComThread.Release();
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "Was not able to close Outlook app", ex);
            return false;
        }
        return true;
    }

    public static String getTextFromPdf(final File pFile) throws IOException {
        PDFParser parser = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        PDFTextStripper pdfStripper;
        try ( FileInputStream fis = new FileInputStream(pFile);  RandomAccessBufferedFileInputStream rab = new RandomAccessBufferedFileInputStream(fis)) {
            String parsedText;
            //String fileName = "E:\\Files\\Small Files\\PDF\\JDBC.pdf";
            //File file = new File(fileName);
            parser = new PDFParser(rab);
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
            //String result = parsedText.replaceAll("[^A-Za-z0-9. ]+", "");
            //return new String(parsedText.trim().getBytes("UTF-8"), "Cp1252");
            return parsedText.trim();
        } finally {
            if (cosDoc != null) {
                try {
                    cosDoc.close();
                } catch (IOException ex) {
                    LOG.log(Level.FINEST, null, ex);
                }
            }
            if (pdDoc != null) {
                try {
                    pdDoc.close();
                } catch (IOException ex) {
                    LOG.log(Level.FINEST, null, ex);
                }
            }
        }
    }

    public static double getWordVersion() {
        double version = 0d;
        ActiveXComponent app = null;
        try {
            app = initWord();
            version = extractOfficeVersion(app);
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(0d)) {
                LOG.log(Level.FINEST, "Cannot read MS Word version");
            }
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "COM error occured, cannot connect to MS Word (most likely it is not installed): " + ex.getMessage());
            LOG.log(Level.FINEST, null, ex);
            return 0d;
        } finally {
            closeWord(app);
        }
        return version;
    }

    public static double getExcelVersion() {
        double version = 0d;
        ActiveXComponent app = null;
        try {
            app = initExcel();
            version = extractOfficeVersion(app);
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(0d)) {
                LOG.log(Level.FINEST, "Cannot read MS Excel version");
            }
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "COM error occured, cannot connect to MS Excel (most likely it is not installed): " + ex.getMessage());
            LOG.log(Level.FINEST, null, ex);
            return 0d;
        } finally {
            closeExcel(app);
        }
        return version;
    }

    public static double getOutlookVersion() {
        double version = 0d;
        ActiveXComponent app = null;
        try {
            app = initOutlook();
            version = extractOfficeVersion(app);
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(0d)) {
                LOG.log(Level.FINEST, "Cannot read MS Outlook version");
            }
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "COM error occured, cannot connect to MS Outlook (most likely it is not installed): " + ex.getMessage());
            LOG.log(Level.FINEST, null, ex);
            return 0d;
        } finally {
            //closeOutlook(app);
        }
        return version;
    }

    public static Set<File> getOutlookAttachments(final List<String> pFileNames) {
        final Set<File> files = new HashSet<>();
        ActiveXComponent app = null;
//        try {
        app = initOutlook();
        Dispatch explorer = Dispatch.get(app, "ActiveExplorer").toDispatch();
        Dispatch selection = Dispatch.get(explorer, "Selection").toDispatch();
//            Dispatch firstFolder = Dispatch.call(selection, "Item", new Variant(1)).toDispatch(); 
        Variant count = Dispatch.get(selection, "Count");
        for (int mailIndex = 1; mailIndex <= count.getInt(); mailIndex++) {
            final List<File> filesTmp = new ArrayList<>();
            Dispatch mailItem = Dispatch.call(selection, "Item", new Variant(mailIndex)).toDispatch();

//            Variant subject = Dispatch.get(mailItem, "Subject");
//            Variant received = Dispatch.get(mailItem, "ReceivedTime");
//            Variant cc = Dispatch.get(mailItem, "Cc");
//            Variant to = Dispatch.get(mailItem, "To");
//            Variant body = Dispatch.get(mailItem, "Body");
//            _contactEmailId.setText(to.getString());
//            _ccList.setText(cc.getString());
//            _subject.setText(subject.getString());
//            _body.setText(body.getString());
//            System.out.println("Subject : " + subject);
//            System.out.println("Received :" + received);
//            System.out.println("To :" + to);
//            System.out.println("Cc :" + cc);
//            System.out.println("Body Content :" + body);
            Dispatch attachments = Dispatch.get(mailItem, "Attachments").toDispatch();
            Variant count1 = Dispatch.get(attachments, "Count");
            String directory = System.getProperty("java.io.tmpdir");
            int numberOfAttach = count1.getInt();
            if (numberOfAttach > 0) {
                for (int i = 1; i <= numberOfAttach; i++) {
                    Dispatch attachment = Dispatch.call(attachments, "Item", new Variant(i)).toDispatch();

                    // get the name of the file
                    Variant nameOfFile = Dispatch.get(attachment, "DisplayName");
                    for (String file : pFileNames) {
                        if (file.equalsIgnoreCase(nameOfFile.getString())) {
                            String fileName = directory + "/" + nameOfFile.getString();
                            File f = new File(fileName);
                            Variant saveEmail = Dispatch.call(attachment, "SaveAsFile", fileName);
                            filesTmp.add(f);
                        }
                    }
                }
            }

            if (filesTmp.isEmpty()) {
                //no selected attachments found, so I whill check if whole email (msg file) was possibly selected
                for (String file : pFileNames) {
                    if (file.endsWith(".msg")) {
                        String fileName = directory + "/" + file;
                        File f = new File(fileName);
                        if (files.contains(f)) {
                            continue;
                        }
                        Variant saveEmail = Dispatch.call(mailItem, "SaveAs", fileName);
                        filesTmp.add(f);
                    }
                }
            }
            files.addAll(filesTmp);
        }
        //Variant explorer = app.getProperty("ActiveExplorer");
        //Outlook.Selection selection = oExplorer.Selection;
//            version = extractOfficeVersion(app);
//            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(0d)) {
//                LOG.log(Level.FINEST, "Cannot read MS Outlook version");
//            }
//            }catch (ComFailException ex) {
//            LOG.log(Level.SEVERE, "COM error occured, cannot connect to MS Outlook (most likely it is not installed): " + ex.getMessage());
//            LOG.log(Level.FINEST, null, ex);
//        }finally {
//            //closeOutlook(app);
//        }
//            return version;
//        }
        return files;
    }

    private static double extractOfficeVersion(final ActiveXComponent pApp) {
        double version = 0d;
        if (pApp != null) {
            String ver = pApp.getPropertyAsString("Version");
            if (ver != null) {
                try {
                    ver = ver.replaceAll("[^\\d.]", "");
                    final int pos1 = ver.indexOf('.');
                    final int pos2 = ver.indexOf('.', pos1 + 1);
                    if (pos1 > -1 && pos2 > -1) {
                        LOG.log(Level.FINEST, "Will truncate this MS version: " + ver);
                        ver = ver.substring(0, pos2).trim();
                        //ver = ver.substring(0, pos).trim();
                    }
                    version = Double.valueOf(ver);
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "Cannot read MS Version version", ex);
                }
            }
        }
        return version;
    }

    public static String getWordVersionName() {
        return getWordVersionName(0);
    }

    public static String getWordVersionName(double version) {
        if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(0d)) {
            version = getWordVersion();
        }

        if (version > 0) {
            /*
            1.1 = Word for Windows 1.1
            1.1a = Word for Windows 1.1a
            2.0 = Word for Windows 2.0
            6.0 = Word for Windows 6.0
             */
            if (version < 7) {
                return "Word for Windows 1.1, 1.1a, 2.0 oder 6.0";
            }
            return getVersionName("Word", version);
        }
        return "";
    }

    public static String getExcelVersionName() {
        return getExcelVersionName(0);
    }

    public static String getExcelVersionName(double version) {
        if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(0d)) {
            version = getExcelVersion();
        }

        if (version > 0) {
            /*
            2.0 = Excel 2.0
            3.0 = Excel 3.0
            4.0 = Excel 4.0
            5.0 = Excel 5.0
             */
            if (version < 7) {
                return "Excel 2.0, 3.0, 4.0 oder 5.0";
            }
            return getVersionName("Excel", version);
        }
        return "";
    }

    public static String getOutlookVersionName() {
        return getOutlookVersionName(0);
    }

    public static String getOutlookVersionName(double version) {
        if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(0d)) {
            version = getOutlookVersion();
        }

        if (version > 0) {
            /*
            2.0 = Excel 2.0
            3.0 = Excel 3.0
            4.0 = Excel 4.0
            5.0 = Excel 5.0
             */
            if (version < 8) {
                return "Outlook for MS-DOS, Outlook for Windows 3.1x oder Outlook for Macintosh";
            }
            return getVersionName("Outlook", version);
        }
        return "";
    }

    private static String getVersionName(final String pProgrammName, double version) {
        if (version > 0) {
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(7d)) {
                return "95";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(8d)) {
                return "97";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(8.5d)) {
                return "98";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(9d)) {
                return "2000";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(10d)) {
                return "2002";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(11d)) {
                return "2003";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(12d)) {
                return "2007";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(14d)) {
                return "2010";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(15d)) {
                return "2013";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(15.3d)) {
                return "for Mac";
            }
            if (Double.doubleToRawLongBits(version) == Double.doubleToRawLongBits(16d)) {
                return "2016/2019";
            }
            if (version > 16) {
                return "Aktueller als " + pProgrammName + " 2019 (Version " + version + ")";
            }
            return "Unbekannte " + pProgrammName + "-Version (Version " + version + ")";
        }
        return "";
    }

//AGe:  PLEASE CHANGE EXCEPTION TEXTS IN LOCALE ONLY, THEY ARE USED IN ReaderExceptionTypeEn to find the ID of the Exception
    public static void checkOfficeDisabled() {
        if (!OFFICE_ENABLE) {
            if(!checkOfficeOld()){
            
                throw new OfficeDisabledException(Lang.getMsOfficeDisabled());  
            } 
        }
    }
    
    private static boolean checkOfficeOld() throws RuntimeException {
        if(!OFFICE_FOUND){
            LOG.log(Level.INFO, "checkOfficeOld: office not found");
//            throw new RuntimeException("test");
//try{
//            Thread.sleep(500);
//}catch(Exception ex){}
            throw new ReaderException(Lang.getMsOfficeNotFound(), ReaderExceptionTypeEn.OFFICE_NOT_FOUND);
        }
           if(OFFICE_OLD){
                if(WORD_VERSION == EXCEL_VERSION && EXCEL_VERSION ==OUTLOOK_VERSION){
                    if(WORD_VERSION == 0){
                        throw new ReaderException(Lang.getMsOfficeNotFound(), ReaderExceptionTypeEn.OFFICE_NOT_FOUND);
                    }
                throw new ReaderException(
                        Lang.getMsOfficeVersion() + " '" + DocumentReader.getExcelVersionName(WORD_VERSION) + "' " + Lang.getIsNotSupported() + " " + DocumentReader.getExcelVersionName(MIN_OFFICE_VERSION) + ")!\n", ReaderExceptionTypeEn.OFFICE_DISABLED);
//                        + Lang.getSomeFeachersAreDeactivated());
                }else {
                    String message = "";
                    if(WORD_OLD){
                        message = Lang.getMsWordVersion() + " '" + DocumentReader.getExcelVersionName(WORD_VERSION) + "' ";
                    }else if(!WORD_FOUND){
                        message = Lang.getMsWordNotFound();
                    }
                    if(EXCEL_OLD){
                        message += message.isEmpty()?"":"\n" + Lang.getMsExcelVersion() + " '" + DocumentReader.getExcelVersionName(EXCEL_VERSION) + "' " ;
                    }else if(!EXCEL_FOUND){
                        if(message.isEmpty()){
                            message = Lang.getMsExcelNotFound();
                        }else{
                            if(message.contains(Lang.getMsWordVersion())){
                                message += Lang.getIsNotSupported() + " " + DocumentReader.getExcelVersionName(MIN_OFFICE_VERSION) + ")\n" + Lang.getMsExcelNotFound();
                            }
                        }
                    }
                    if(OUTLOOK_OLD){
                        message += message.isEmpty()?"":"\n" + Lang.getMsOutlookVersion() + " '" + DocumentReader.getExcelVersionName(OUTLOOK_VERSION) + "' " ;
                    }else if(!OUTLOOK_FOUND){
                        if(message.isEmpty()){
                            message = Lang.getMsOutlookNotFound();
                        }else{
                            if(message.contains(Lang.getMsExcelVersion())){
                                 message += Lang.getIsNotSupported() + " " + DocumentReader.getExcelVersionName(MIN_OFFICE_VERSION) + ")\n" + Lang.getMsOutlookNotFound();
                            }
                        }
                    }
                    if(!message.endsWith(Lang.getMsOutlookNotFound())){
                        message += Lang.getIsNotSupported() + " " + DocumentReader.getExcelVersionName(MIN_OFFICE_VERSION) + ")\n";
                    }
                    throw new ReaderException(message, ReaderExceptionTypeEn.OFFICE_DISABLED );
     //                   + Lang.getSomeFeachersAreDeactivated());
                }
           }
           return false;
        
    }

    public static void checkExcelVersion() {
        if (EXCEL_VERSION <= 0d) {
            throw new ExcelNotFoundException(Lang.getMsExcelNotFound());
        }

        if (EXCEL_VERSION < MIN_OFFICE_VERSION) {
            throw new ExcelNotFoundException(Lang.getMsExcelVersion() + " '" + DocumentReader.getExcelVersionName(EXCEL_VERSION) + "' " + Lang.getIsNotSupported() + " " + DocumentReader.getExcelVersionName(MIN_OFFICE_VERSION) + ")!");
        }
    }

    public static void checkWordVersion() {
        if (WORD_VERSION <= 0d) {
            throw new WordNotFoundException(Lang.getMsWordNotFound());
        }
        if (WORD_VERSION < MIN_OFFICE_VERSION) {
            
            throw new WordNotFoundException(Lang.getMsWordVersion() + " '" + DocumentReader.getWordVersionName(WORD_VERSION) + "' " + Lang.getIsNotSupported() + " " + DocumentReader.getWordVersionName(MIN_OFFICE_VERSION) + ")!");
        }
    }

    public static void checkOutlookVersion() {
        if (WORD_VERSION <= 0d) {
            throw new OutlookNotFoundException(Lang.getMsOutlookNotFound());
        }
        if (OUTLOOK_VERSION < MIN_OFFICE_VERSION) {
            throw new OutlookNotFoundException(Lang.getMsOutlookVersion() + " '" + DocumentReader.getOutlookVersionName(OUTLOOK_VERSION) + "' " + Lang.getIsNotSupported() + " " + DocumentReader.getOutlookVersionName(MIN_OFFICE_VERSION) + ")!");
        }
    }

    private static boolean isWordFound() {
        try {
            checkWordVersion();
            return true;
        } catch (WordNotFoundException ex) {
            LOG.log(Level.FINEST, "Word is not installed", ex);
            return false;
        }
    }

    private static boolean isExcelFound() {
        try {
            checkExcelVersion();
            return true;
        } catch (ExcelNotFoundException ex) {
            LOG.log(Level.FINEST, "Excel is not installed", ex);
            return false;
        }
    }

    private static boolean isOutlookFound() {
        try {
            checkOutlookVersion();
            return true;
        } catch (OutlookNotFoundException ex) {
            LOG.log(Level.FINEST, "Outlook is not installed", ex);
            return false;
        }
    }

    public static void checkOfficeFound() {
//        if (!OFFICE_FOUND) {
//             throw new OfficeDisabledException(Lang.getMsOfficeNotFound());
//
//        }
        checkOfficeOld();
    }

    public static void setOfficeEnabled(final boolean pEnable) {
        OFFICE_ENABLE = pEnable;
    }

    public static boolean isOfficeEnabled() {
        return OFFICE_ENABLE;
    }

//    public static boolean isOfficeInstalled() {
//        return isWordInstalled() || isExcelInstalled() || isOutlookInstalled();
//    }
}

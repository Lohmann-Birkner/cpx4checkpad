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
package de.lb.cpx.document;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.LibraryLoader;
import de.lb.cpx.reader.exception.ReaderException;
import de.lb.cpx.reader.util.OS;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author niemeier
 */
public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());
//    private static final Random RANDOMIZER = new SecureRandom();

    public enum OCR_TYPES {
        WORD, TESS;

        public boolean isWord() {
            return WORD.equals(this);
        }

        public boolean isTess() {
            return TESS.equals(this);
        }
    };

    public enum FILE_TYPES {
        EXCEL, WORD, CSV, TEXT, PDF, IMAGE, MESSAGE, OTHER;

        public boolean isExcel() {
            return EXCEL.equals(this);
        }

        public boolean isWord() {
            return WORD.equals(this);
        }

        public boolean isCsv() {
            return CSV.equals(this);
        }

        public boolean isText() {
            return TEXT.equals(this);
        }

        public boolean isPdf() {
            return PDF.equals(this);
        }

        public boolean isImage() {
            return IMAGE.equals(this);
        }

        public boolean isMessage() {
            return MESSAGE.equals(this);
        }

        public boolean isOther() {
            return OTHER.equals(this);
        }

        public boolean isTessable() {
            return isPdf() || isImage();
        }

        public boolean isOfficeable() {
            return isExcel() || isWord() || isPdf() || isImage() || isText() || isCsv() || isMessage() || isOther();
        }
    };

    public static void initJacob() {
        String p = OS.getSharedLibraryLoaderPath(); //checking the operating system
        LOG.log(Level.FINEST, "Set JACOB DLL path to '" + p + "'");
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, p);
        System.setProperty("com.jacob.debug", "false");
        LibraryLoader.loadJacobLibrary(); // loading the Jacob library
    }

    public static void checkExistingFile(final String pFile) {
        if (pFile == null || pFile.trim().isEmpty()) {
            throw new ReaderException("Es wurde keine Datei angegeben");
        }
        checkExistingFile(new File(pFile));
    }

    public static void checkExistingFile(final File pFile) {
        if (pFile == null) {
            throw new ReaderException("Es wurde keine Datei angegeben");
        }
        if (!isValidFilename(pFile)) {
            throw new ReaderException("Dies ist kein gültiger Dateipfad: " + pFile.getAbsolutePath());
        }
        if (!pFile.exists()) {
            //MainApp.showErrorMessageDialog("Input draft file doesn't exist, so document can't be generated: " + InFile.getAbsolutePath());
            throw new ReaderException("Die Datei existiert nicht (mehr): " + pFile.getAbsolutePath());
        }
        if (pFile.isDirectory()) {
            throw new ReaderException(MessageFormat.format("Dies ist keine Datei, sondern ein Verzeichnis: {0}", pFile.getAbsolutePath()));
        }
        if (!pFile.canRead()) {
            throw new ReaderException(MessageFormat.format("Keine Berechtigungen um die Datei zu lesen: {0}", pFile.getAbsolutePath()));
        }
        if (pFile.length() == 0) {
            throw new ReaderException(MessageFormat.format("Die Datei ist leer: {0}", pFile.getAbsolutePath()));
        }
    }

    public static void checkNewFile(final String pFile) {
        if (pFile == null || pFile.trim().isEmpty()) {
            throw new ReaderException("Es wurde keine Datei angegeben");
        }
        checkNewFile(new File(pFile));
    }

    public static void checkNewFile(final File pFile) {
        if (pFile == null) {
            throw new ReaderException("Es wurde keine Datei angegeben");
        }
        if (!isValidFilename(pFile)) {
            throw new ReaderException("This is a not a valid file path: " + pFile.getAbsolutePath());
        }
//        if (!pInFile.exists()) {
//            //MainApp.showErrorMessageDialog("Input draft file doesn't exist, so document can't be generated: " + InFile.getAbsolutePath());
//            throw new IllegalArgumentException("Cannot open, file does not exist: " + pInFile.getAbsolutePath());
//        }
        if (pFile.isDirectory()) {
            throw new ReaderException(MessageFormat.format("Dies ist keine Datei, sondern ein Verzeichnis: {0}", pFile.getAbsolutePath()));
        }
        if (pFile.exists() && !pFile.canWrite() /* || !Files.isWritable(pFile.toPath()) */) {
            throw new ReaderException(MessageFormat.format("Keine Berechtigung um die Datei zu schreiben: {0}", pFile.getAbsolutePath()));
        }
//        if (pInFile.length() == 0) {
//            throw new IllegalArgumentException(MessageFormat.format("File is empty: {0}", pInFile.getAbsolutePath()));
//        }
    }

    /**
     * Is this file name syntactically correct?
     *
     * @param sFile file to check
     * @return is valid?
     */
    public static boolean isValidFilename(final String sFile) {
        if (sFile == null || sFile.trim().isEmpty()) {
            return false;
        }
        File file = new File(sFile);
        return isValidFilename(file);
    }

    /**
     * Is this file name syntactically correct?
     *
     * @param pFile file to check
     * @return is valid?
     */
    public static boolean isValidFilename(final File pFile) {
        if (pFile == null) {
            return false;
        }
        try {
            pFile.getCanonicalPath();
        } catch (IOException ex) {
            LOG.log(Level.FINEST, "Test failed, this file name is syntactically incorrect: " + pFile.getAbsolutePath(), ex);
            return false;
        }
        final char[] illegalCharacters = {'/', '\n', '\r', '\t', '\0', '\f', '?', '*', '\\', '<', '>', '|', '\"', ':'}; //'`', 
        final String name = pFile.getName();
        for (char ch : illegalCharacters) {
            if (name.indexOf(ch) > -1) {
                LOG.log(Level.FINEST, "Found illegal character, so this file name is syntactically incorrect: " + pFile.getAbsolutePath());
                return false;
            }
        }

//        if (name.equalsIgnoreCase(".") || name.equalsIgnoreCase("..")) {
//            LOG.log(Level.FINEST, "This is not a valid file name: " + sFile);
//            return false;
//        }
//        
//        //If you're a very anxious person, than you can also check for reserved names:
//        File file2 = new File(file.getAbsolutePath());
//        final String[] reservedWinNames = new String[]{"CON", "PRN", "AUX",
//            "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7",
//            "COM8", "COM9", "COM0", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5",
//            "LPT6", "LPT7", "LPT8", "LPT9", "LPT0"};
//        while(true) {
//            for (String reservedName : reservedWinNames) {
//                if (file2.getName().equalsIgnoreCase(reservedName)) {
//                    LOG.log(Level.FINEST, "This is not a valid file name, because it contains a reserved windows name: " + sFile);
//                    return false;
//                }
//            }
//            file2 = file2.getParentFile();
//            if (file2 == null) {
//                break;
//            }
//        };
        return true;
    }

    public static FILE_TYPES getFileType(final String pFile) {
        return getFileType(new File(pFile));
    }

    public static FILE_TYPES getFileType(final File pFile) {
        if (pFile == null) {
            return null;
        }
        if (isWordFile(pFile)) {
            return FILE_TYPES.WORD;
        }
        if (isTextFile(pFile)) {
            return FILE_TYPES.TEXT;
        }
        if (isExcelFile(pFile)) {
            return FILE_TYPES.EXCEL;
        }
        if (isCsvFile(pFile)) {
            return FILE_TYPES.CSV;
        }
        if (isPdfFile(pFile)) {
            return FILE_TYPES.PDF;
        }
        if (isImageFile(pFile)) {
            return FILE_TYPES.IMAGE;
        }
        if (isMessageFile(pFile)) {
            return FILE_TYPES.MESSAGE;
        }
        return FILE_TYPES.OTHER;
    }

    public static boolean isExcelFile(final File pFile) {
        final String extension = FilenameUtils.getExtension(pFile.getName()).trim().toLowerCase();
        return "xls".equalsIgnoreCase(extension)
                || "xlsx".equalsIgnoreCase(extension);
    }

    public static boolean isWordFile(final File pFile) {
        final String extension = FilenameUtils.getExtension(pFile.getName()).trim().toLowerCase();
        return "doc".equalsIgnoreCase(extension)
                || "docx".equalsIgnoreCase(extension);
    }

    public static boolean isTextFile(final File pFile) {
        final String extension = FilenameUtils.getExtension(pFile.getName()).trim().toLowerCase();
        return "txt".equalsIgnoreCase(extension);
    }

    public static boolean isCsvFile(final File pFile) {
        final String extension = FilenameUtils.getExtension(pFile.getName()).trim().toLowerCase();
        return "csv".equalsIgnoreCase(extension);
    }

    public static boolean isPdfFile(final File pFile) {
        final String extension = FilenameUtils.getExtension(pFile.getName()).trim().toLowerCase();
        return "pdf".equalsIgnoreCase(extension);
    }

    public static boolean isImageFile(final File pFile) {
        final String extension = FilenameUtils.getExtension(pFile.getName()).trim().toLowerCase();
        return "png".equalsIgnoreCase(extension)
                || "jpg".equalsIgnoreCase(extension)
                || "jpeg".equalsIgnoreCase(extension)
                || "tiff".equalsIgnoreCase(extension)
                || "bmp".equalsIgnoreCase(extension)
                || "gif".equalsIgnoreCase(extension);
    }

    public static boolean isMessageFile(final File pFile) {
        final String extension = FilenameUtils.getExtension(pFile.getName()).trim().toLowerCase();
        return "msg".equalsIgnoreCase(extension);
    }

    /*
    public static ITesseract initTess() {
        final long startTime = System.currentTimeMillis();
        System.load("C:\\Program Files\\gs\\gs9.25\\bin\\gsdll64.dll");
        final ITesseract instance = new Tesseract();  // JNA Interface Mapping
        instance.setLanguage("deu");
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath("E:\\TESSDATA"); // path to tessdata directory
        LOG.log(Level.INFO, "Init Tess in " + (System.currentTimeMillis() - startTime) + " ms");
        return instance;
    }
     */
    public static ActiveXComponent initWord() {
        //TODO CPX-1645: com.jacob.com.ComFailException: Can't get object clsid from progid
        return new ActiveXComponent("Word.Application"); // type of the application (Here, Ms word application)            
    }

    public static ActiveXComponent initOutlook() {
        //TODO CPX-1645: com.jacob.com.ComFailException: Can't get object clsid from progid
        return new ActiveXComponent("Outlook.Application"); // type of the application (Here, Ms word application)            
    }

    public static ActiveXComponent initExcel() {
        //TODO CPX-1645: com.jacob.com.ComFailException: Can't get object clsid from progid
        return new ActiveXComponent("Excel.Application"); // type of the application (Here, Ms excel application)
    }

    //don't use MD5 or SHA here, cause they are very slow (and we don't care about irreversibility of checksum in this case)
    //use Adler32 instead
    public static String getChecksum(final File pFile) throws IOException {
        final int kbMaxSizeForAdler = 15000;
        if (pFile.length() / 1024 > kbMaxSizeForAdler) {
            //Okay, for big files we use a veeeeery very simple checksum algorithm, but it should sufficienct for our use case
            //reason: even the fast Adler32 algorithm takes some time for big files (e.g. 10 seconds for file with 500 MB)
            return String.valueOf("FS_" + pFile.length());
        }
        final long startTime = System.currentTimeMillis();
        Long chksum = null;
        // Open the file and build a CRC32 checksum.
        FileInputStream fis = new FileInputStream(pFile);
        Adler32 chk = new Adler32();
        try ( CheckedInputStream cis = new CheckedInputStream(fis, chk)) {
            byte[] buff = new byte[80];
            while (cis.read(buff) >= 0) ;
        }
        chksum = chk.getValue();
        LOG.log(Level.FINEST, "Adler32 checksum of file took " + (System.currentTimeMillis() - startTime) + " ms: " + pFile.getAbsolutePath());
        return "AD32_" + String.valueOf(chksum);
    }

//    /**
//     * Look at
//     * http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
//     *
//     * @param pFile File
//     * @return Checksum
//     * @throws IllegalArgumentException Invalid file
//     * @throws IOException File could not be opened
//     */
//    public static String getChecksum(File pFile) throws IOException {
//        if (pFile == null) {
//            throw new IllegalArgumentException("File is null!");
//        }
//        MessageDigest complete;
//        final String hashType = "MD5"; //SHA-256
//        try (InputStream fis = new FileInputStream(pFile)) {
//            byte[] buffer = new byte[1024];
//            try {
//                complete = MessageDigest.getInstance(hashType); //MD5, SHA-1, SHA-256
//            } catch (NoSuchAlgorithmException ex) {
//                LOG.log(Level.SEVERE, "Hash type seems to be invalid: " + hashType, ex);
//                return "";
//            }
//            int numRead;
//            do {
//                numRead = fis.read(buffer);
//                if (numRead > 0) {
//                    complete.update(buffer, 0, numRead);
//                }
//            } while (numRead != -1);
//        }
//        byte[] b = complete.digest();
//
//        String result = "";
//
//        for (int i = 0; i < b.length; i++) {
//            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
//        }
//        return result.toLowerCase().trim();
//    }
    public static String getTempPdfFileName(final File pFile) {
        return getTempFileName(pFile, "pdf");
    }

    public static String getTempTxtFileName(final File pFile) {
        return getTempFileName(pFile, "txt");
    }

    public static String getTempCsvFileName(final File pFile) {
        return getTempFileName(pFile, "csv");
    }

    public static String getTempFileName(final File pFile, String pExtension) {
        String checkSum;
        try {
            checkSum = getChecksum(pFile);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
        LOG.log(Level.FINEST, "Checksum " + checkSum + " for file " + pFile.getAbsolutePath());
        //final String extension = FilenameUtils.getExtension(pFile.getName()).trim().toLowerCase();
        final String fileName = System.getProperty("java.io.tmpdir") + "CPX\\" + checkSum + "." + pExtension;
        final File parent = (new File(fileName)).getParentFile();
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                LOG.log(Level.WARNING, "Was not able to create temp directory: " + parent.getAbsolutePath());
            }
        }
//                + (pExtension != null && !pExtension.trim().isEmpty() ? "." + pExtension.trim() : "");
//        return fileName;
        return fileName;
    }

//    public static String getTempPdfFileName() {
//        return getTempFileName("pdf");
//    }
//
//    public static String getTempTxtFileName() {
//        return getTempFileName("txt");
//    }
//
//    public static String getTempCsvFileName() {
//        return getTempFileName("csv");
//    }
//
//    public static String getTempFileName(final String pExtension) {
//        final String fileName = System.getProperty("java.io.tmpdir") + System.nanoTime() + "-" + RANDOMIZER.nextInt()
//                + (pExtension != null && !pExtension.trim().isEmpty() ? "." + pExtension.trim() : "");
//        return fileName;
//    }
}

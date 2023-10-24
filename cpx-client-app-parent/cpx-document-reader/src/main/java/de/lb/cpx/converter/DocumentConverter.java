/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.converter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import static de.lb.cpx.document.Utils.*;
import de.lb.cpx.document.Utils.FILE_TYPES;
import de.lb.cpx.reader.DocumentReader;
import static de.lb.cpx.reader.DocumentReader.checkExcelVersion;
import static de.lb.cpx.reader.DocumentReader.checkOfficeDisabled;
import static de.lb.cpx.reader.DocumentReader.checkOutlookVersion;
import static de.lb.cpx.reader.DocumentReader.checkWordVersion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author niemeier
 */
public class DocumentConverter {

    private final Set<ActiveXComponent> wordApps = new HashSet<>();
    private final Set<ActiveXComponent> excelApps = new HashSet<>();
    private final Set<ActiveXComponent> outlookApps = new HashSet<>();
    private final Set<Dispatch> docs = new HashSet<>();

    public Set<ActiveXComponent> getWordApps() {
        return new HashSet<>(wordApps);
    }

    public Set<ActiveXComponent> getExcelApps() {
        return new HashSet<>(excelApps);
    }

    public Set<ActiveXComponent> getOutlookApps() {
        return new HashSet<>(outlookApps);
    }

    public Set<Dispatch> getDocuments() {
        return new HashSet<>(docs);
    }

    private static final Logger LOG = Logger.getLogger(DocumentConverter.class.getName());
    //public final static int PAGE_FROM = 1;
    //public final static int PAGE_TO = 5;

    public DocumentConverterResult convert(final String pSourceFile) {
        final String targetPdfFile = getTempPdfFileName(new File(pSourceFile));
        return convert(pSourceFile, targetPdfFile);
    }

    public DocumentConverterResult convert(final String pSourceFile, final String pTargetPdfFile) {
        LOG.log(Level.FINEST, "Try to convert this file to PDF: " + pSourceFile + ", target PDF file is " + pTargetPdfFile);
        final File sourceFile = new File(pSourceFile);
        final File targetPdfFile = new File(pTargetPdfFile);
        //final File targetTxtFile = new File(pTargetTxtFile);
        checkExistingFile(sourceFile);
        checkNewFile(targetPdfFile);

        //create temporary copy of file
        final String ext = FilenameUtils.getExtension(sourceFile.getName());
        final File tempSourceFile;
        try {
            final Path tmpFile = Files.createTempFile("document_import_", "." + ext);
            tempSourceFile = tmpFile.toFile();
            //tempSourceFile.delete();
            LOG.log(Level.FINEST, "Copy original source file to temporary file: " + sourceFile.getAbsolutePath() + " -> " + tempSourceFile.getAbsolutePath());
            Files.copy(sourceFile.toPath(), tempSourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Unable to create temporary copy of this file: " + sourceFile.getAbsolutePath(), ex);
            throw new IllegalArgumentException("Unable to create temporary copy of this file: " + sourceFile.getAbsolutePath(), ex);
        }

        tempSourceFile.deleteOnExit();

        //maybe it makes sense to create copy of original source file to prevent accessing conflicts
        //checkNewFile(targetTxtFile);
        final FILE_TYPES fileType = getFileType(tempSourceFile);
        if (fileType.isWord()) {
            LOG.log(Level.FINEST, "This seems to be a Word file: " + tempSourceFile.getAbsolutePath());
            final File targetTxtFile = new File(getTempTxtFileName(tempSourceFile));
            if (!targetPdfFile.exists() || targetPdfFile.length() == 0 || !targetTxtFile.exists() || targetTxtFile.length() == 0) {
                deleteFile(targetPdfFile);
                deleteFile(targetTxtFile);
                wordToPdf(tempSourceFile.getAbsolutePath(), pTargetPdfFile, targetTxtFile.getAbsolutePath());
            }
            //if (tempSourceFile.delete()) {
            if (deleteFile(tempSourceFile)) {
                LOG.log(Level.FINEST, "temp source file successfully deleted: " + tempSourceFile.getAbsolutePath());
            }
            return new DocumentConverterResult(sourceFile, fileType, targetPdfFile, targetTxtFile);
        }
        if (fileType.isText()) {
            LOG.log(Level.FINEST, "This seems to be a Text file: " + tempSourceFile.getAbsolutePath());
            final File targetTxtFile = new File(getTempTxtFileName(tempSourceFile));
            if (!targetPdfFile.exists() || targetPdfFile.length() == 0 || !targetTxtFile.exists() || targetTxtFile.length() == 0) {
                deleteFile(targetPdfFile);
                deleteFile(targetTxtFile);
                textToPdf(tempSourceFile.getAbsolutePath(), pTargetPdfFile, targetTxtFile.getAbsolutePath());
            }
            //if (tempSourceFile.delete()) {
            if (deleteFile(tempSourceFile)) {
                LOG.log(Level.FINEST, "temp source file successfully deleted: " + tempSourceFile.getAbsolutePath());
            }
            return new DocumentConverterResult(sourceFile, fileType, targetPdfFile, targetTxtFile);
        }
        if (fileType.isExcel()) {
            LOG.log(Level.FINEST, "This seems to be an Excel file: " + tempSourceFile.getAbsolutePath());
            final File targetCsvFile = new File(getTempCsvFileName(tempSourceFile));
            if (!targetPdfFile.exists() || targetPdfFile.length() == 0 || !targetCsvFile.exists() || targetCsvFile.length() == 0) {
                deleteFile(targetPdfFile);
                deleteFile(targetCsvFile);
                excelToPdf(tempSourceFile.getAbsolutePath(), pTargetPdfFile, targetCsvFile.getAbsolutePath());
            }
            //if (tempSourceFile.delete()) {
            if (deleteFile(tempSourceFile)) {
                LOG.log(Level.FINEST, "temp source file successfully deleted: " + tempSourceFile.getAbsolutePath());
            }
            return new DocumentConverterResult(sourceFile, fileType, targetPdfFile, targetCsvFile);
        }
        if (fileType.isMessage()) {
            LOG.log(Level.FINEST, "This seems to be an Outlook Message file: " + tempSourceFile.getAbsolutePath());
            final File targetTxtFile = new File(getTempTxtFileName(tempSourceFile));
            if (!targetPdfFile.exists() || targetPdfFile.length() == 0 || !targetTxtFile.exists() || targetTxtFile.length() == 0) {
                deleteFile(targetPdfFile);
                deleteFile(targetTxtFile);
                outlookToPdf(tempSourceFile.getAbsolutePath(), pTargetPdfFile, targetTxtFile.getAbsolutePath());
            }
            //if (tempSourceFile.delete()) {
            if (deleteFile(tempSourceFile)) {
                LOG.log(Level.FINEST, "temp source file successfully deleted: " + tempSourceFile.getAbsolutePath());
            }
            return new DocumentConverterResult(sourceFile, fileType, targetPdfFile, targetTxtFile);
        }
        if (fileType.isCsv()) {
            LOG.log(Level.FINEST, "This seems to be a CSV file: " + tempSourceFile.getAbsolutePath());
            final File targetCsvFile = new File(getTempCsvFileName(tempSourceFile));
            if (!targetPdfFile.exists() || targetPdfFile.length() == 0 || !targetCsvFile.exists() || targetCsvFile.length() == 0) {
                deleteFile(targetPdfFile);
                deleteFile(targetCsvFile);
                csvToPdf(tempSourceFile.getAbsolutePath(), pTargetPdfFile, targetCsvFile.getAbsolutePath());
            }
            //if (tempSourceFile.delete()) {
            if (deleteFile(tempSourceFile)) {
                LOG.log(Level.FINEST, "temp source file successfully deleted: " + tempSourceFile.getAbsolutePath());
            }
            return new DocumentConverterResult(sourceFile, fileType, targetPdfFile, targetCsvFile);
        }
        if (fileType.isPdf()) {
            LOG.log(Level.FINEST, "This seems to be a PDF file: " + tempSourceFile.getAbsolutePath());
            final File targetTxtFile = new File(getTempTxtFileName(tempSourceFile));
            if (!targetPdfFile.exists() || targetPdfFile.length() == 0 || !targetTxtFile.exists() || targetTxtFile.length() == 0) {
                deleteFile(targetPdfFile);
                deleteFile(targetTxtFile);
                pdfToPdf(tempSourceFile.getAbsolutePath(), pTargetPdfFile, targetTxtFile.getAbsolutePath());
            }
            //if (tempSourceFile.delete()) {
            if (deleteFile(tempSourceFile)) {
                LOG.log(Level.FINEST, "temp source file successfully deleted: " + tempSourceFile.getAbsolutePath());
            }
            return new DocumentConverterResult(sourceFile, fileType, targetPdfFile, targetTxtFile);
        }
        if (fileType.isImage()) {
            LOG.log(Level.FINEST, "This seems to be an Image file: " + tempSourceFile.getAbsolutePath());
            final File targetTxtFile = new File(getTempTxtFileName(tempSourceFile));
            if (!targetPdfFile.exists() || targetPdfFile.length() == 0 || !targetTxtFile.exists() || targetTxtFile.length() == 0) {
                deleteFile(targetPdfFile);
                deleteFile(targetTxtFile);
                imageToPdf(tempSourceFile.getAbsolutePath(), pTargetPdfFile, targetTxtFile.getAbsolutePath());
            }
            //if (tempSourceFile.delete()) {
            if (deleteFile(tempSourceFile)) {
                LOG.log(Level.FINEST, "temp source file successfully deleted: " + tempSourceFile.getAbsolutePath());
            }
            return new DocumentConverterResult(sourceFile, fileType, targetPdfFile, targetTxtFile);
        }
        //if (tempSourceFile.delete()) {
        if (deleteFile(tempSourceFile)) {
            LOG.log(Level.FINEST, "temp source file successfully deleted: " + tempSourceFile.getAbsolutePath());
        }
        throw new IllegalArgumentException("Unknown file type, cannot convert to PDF: " + tempSourceFile.getAbsolutePath());
    }

//    public String pdfToPdf(final String pSourceFile) {
//        final String targetTmpFile = getTempPdfFileName();
//        pdfToPdf(pSourceFile, targetTmpFile);
//        return targetTmpFile;
//    }
    public void pdfToPdf(final String pSourceFile, final String pTargetPdfFile, final String pTargetTxtFile) {
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetPdfFile = new File(pTargetPdfFile);
        final File targetTxtFile = new File(pTargetTxtFile);
        LOG.log(Level.INFO, "Try to copy this file: " + sourceFile.getAbsolutePath() + ", target is " + targetPdfFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetPdfFile);
        checkNewFile(targetTxtFile);
        try {
            FileUtils.copyFile(sourceFile, targetPdfFile);
            //try to extract text with pdf box first, after that try with Word
            final String text = DocumentReader.getTextFromPdf(targetPdfFile);
            if (text.isEmpty()) {
                //no text found. Maybe it's an image, so use Word with OCR to get its content
                wordToPdf(pSourceFile, null, pTargetTxtFile);
            } else {
                final String encoding = "Cp1252";
                byte[] data = text.getBytes("UTF-8");
                FileUtils.writeStringToFile(targetTxtFile, new String(data), encoding);
                rewriteFileWithCorrections(targetTxtFile, sourceFile.getName());
            }
        } catch (IOException ex) {
            String message = "Was not able to copy file: " + sourceFile.getAbsolutePath() + ", target: " + targetPdfFile.getAbsolutePath();
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        }
        LOG.log(Level.INFO, "Copied this file in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetPdfFile.getAbsolutePath());
    }

//    public void textToPdf(final String pSourceFile) {
//        final String targetTmpFile = getTempPdfFileName();
//        wordToPdf(pSourceFile, targetTmpFile);
//    }
    public void textToPdf(final String pSourceFile, final String pTargetPdfFile, final String pTargetTxtFile) {
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetPdfFile = new File(pTargetPdfFile);
        final File targetTxtFile = pTargetTxtFile == null ? null : new File(pTargetTxtFile);
        LOG.log(Level.INFO, "Try to convert this Text file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetPdfFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetPdfFile);
        if (targetTxtFile != null) {
            checkNewFile(targetTxtFile);
        }
        final int threshold = 5000;
        final File source;
        if (targetTxtFile == null) {
            source = sourceFile;
        } else if (sourceFile.length() <= threshold) {
            source = targetTxtFile;
            try {
                FileUtils.copyFile(sourceFile, targetTxtFile);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Was not able to copy Text file: " + sourceFile.getAbsolutePath(), ex);
            }
        } else {
            //final File tmpTextFile = new File(getTempTxtFileName(sourceFile));
            //source = tmpTextFile;
            source = targetTxtFile;
            LOG.log(Level.INFO, "Text file is too large (> " + threshold + " bytes), will cut it before conversion: " + sourceFile.getAbsolutePath());
            //checkNewFile(tmpTextFile);
            try {
                final String text = getTextFromFile(sourceFile.getAbsolutePath(), threshold);
                final String newText = text.substring(0, threshold) + "...";
                final String encoding = "Cp1252";
                FileUtils.writeStringToFile(targetTxtFile, newText, encoding);
            } catch (IOException ex) {
                String message = "Was not able to convert Text file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetPdfFile.getAbsolutePath();
                LOG.log(Level.SEVERE, message, ex);
                throw new IllegalStateException(message);
            }
        }
        wordToPdf(source.getAbsolutePath(), pTargetPdfFile, null);
        if (targetTxtFile != null) {
            rewriteFileWithCorrections(source, sourceFile.getName());
        }
        LOG.log(Level.INFO, "Converted this Text file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetPdfFile.getAbsolutePath());
    }

    protected void csvToPdf(final String pSourceFile, final String pTargetPdfFile, final String pTargetCsvFile) {
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetPdfFile = new File(pTargetPdfFile);
        final File targetCsvFile = new File(pTargetCsvFile);
        LOG.log(Level.INFO, "Try to convert this CSV file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetPdfFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetPdfFile);
        checkNewFile(targetCsvFile);
        final int threshold = 5000;
        final File source;
        if (sourceFile.length() <= threshold) {
            source = targetCsvFile;
            try {
                FileUtils.copyFile(sourceFile, targetCsvFile);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Was not able to copy CSV file: " + sourceFile.getAbsolutePath(), ex);
            }
        } else {
            //final File tmpCsvFile = new File(getTempCsvFileName(sourceFile));
            source = targetCsvFile;
            LOG.log(Level.INFO, "CSV file is too large (> " + threshold + " bytes), will cut it before conversion: " + sourceFile.getAbsolutePath());
//            checkNewFile(tmpCsvFile);
            try {
                final String text = getTextFromFile(sourceFile.getAbsolutePath(), threshold);
                final String newtext = text.substring(0, threshold) + "...";
                final String encoding = "Cp1252";
                FileUtils.writeStringToFile(targetCsvFile, newtext, encoding);
            } catch (IOException ex) {
                String message = "Was not able to convert CSV file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetPdfFile.getAbsolutePath();
                LOG.log(Level.SEVERE, message, ex);
                throw new IllegalStateException(message);
            }
        }
        excelToPdf(source.getAbsolutePath(), pTargetPdfFile, null);
        rewriteFileWithCorrections(source, sourceFile.getName());
        LOG.log(Level.INFO, "Converted this CSV file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetPdfFile.getAbsolutePath());
    }

    public String getTextFromFile(final String pFile, final int pThreshold) throws IOException {
        //final String encoding = "Cp1252";
        StringBuilder sb = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new FileReader(pFile))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
                if (pThreshold > 0 && sb.length() > pThreshold) {
                    break;
                }
            }
        }
        return sb.toString();
    }

//    public void wordToPdf(final String pSourceFile) {
//        final String targetTmpFile = getTempPdfFileName();
//        wordToPdf(pSourceFile, targetTmpFile);
//    }
    public void wordToPdf(final String pSourceFile, final String pTargetPdfFile, final String pTargetTxtFile) {
        checkOfficeDisabled();
        checkWordVersion();
        //String sMimeTypeSource = getMimeType(sSourceFile);
        //String sMimeTypeTarget = getMimeType(sTargetFile);
        //ActiveXComponent app = null;
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetTxtFile = pTargetTxtFile == null ? null : new File(pTargetTxtFile);
        final File targetPdfFile = pTargetPdfFile == null ? null : new File(pTargetPdfFile);
        LOG.log(Level.INFO, "Try to convert this Word file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + (targetPdfFile == null ? "null" : targetPdfFile.getAbsolutePath()));
        checkExistingFile(sourceFile);
        if (targetPdfFile != null) {
            checkNewFile(targetPdfFile);
        }
        initJacob();

        ActiveXComponent app = initWord();
        wordApps.add(app);
        Dispatch document = null;

        try {
            app.setProperty("Visible", new Variant(false));

            Dispatch documents = app.getProperty("Documents").toDispatch();

            document = Dispatch.call(
                    documents,
                    "Open",
                    pSourceFile, //FileName
                    false, //ConfirmConversions
                    true, //ReadOnly
                    false //AddToRecentFiles
            /* Weitere Parameter siehe https://msdn.microsoft.com/en-us/library/bb216319(v=office.12).aspx or https://docs.microsoft.com/en-us/office/vba/api/word.documents.open (newer) */
            ).toDispatch();
            docs.add(document);

            if (pTargetPdfFile != null) {
                PdfConverter.exportWordFileToPdf(document, pSourceFile, pTargetPdfFile);
            }
            if (pTargetTxtFile != null) {
                DocumentReader.writeWordTextToFile(document, pTargetTxtFile);
            }
        } catch (ComFailException ex) {
            String message = "Was not able to convert Word file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + (targetPdfFile == null ? "null" : targetPdfFile.getAbsolutePath());
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        } finally {
            if (document != null) {
                DocumentReader.closeDocument(document);
                docs.remove(document);
            }
            if (app != null) {
                DocumentReader.closeWord(app);
                wordApps.remove(app);
            }
        }
        if (targetTxtFile != null) {
            rewriteFileWithCorrections(targetTxtFile, sourceFile.getName());
        }
//                docConverterResultProp.get().isFinished.set(true);
        LOG.log(Level.INFO, "Converted this Word file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + (targetPdfFile == null ? "null" : targetPdfFile.getAbsolutePath()));
    }

    public void outlookToPdf(final String pSourceFile, final String pTargetPdfFile, final String pTargetTxtFile) {
        //String sMimeTypeSource = getMimeType(sSourceFile);
        //String sMimeTypeTarget = getMimeType(sTargetFile);
        //ActiveXComponent app = null;
        checkOfficeDisabled();
        checkOutlookVersion();

        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetTxtFile = new File(pTargetTxtFile);
        final File targetPdfFile = pTargetPdfFile == null ? null : new File(pTargetPdfFile);
        LOG.log(Level.INFO, "Try to convert this Outlook file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + (targetPdfFile == null ? "null" : targetPdfFile.getAbsolutePath()));
        checkExistingFile(sourceFile);
        if (pTargetPdfFile != null) {
            checkNewFile(targetPdfFile);
        }
        initJacob();

        ActiveXComponent app = initOutlook();
//        outlookApps.add(app);
        Dispatch document = null;

        try {
            //app.setProperty("Visible", new Variant(false));

            //Dispatch documents = app.getProperty("Documents").toDispatch();
            document = Dispatch.call(
                    app,
                    "CreateItemFromTemplate",
                    pSourceFile //FileName
            ).toDispatch();
            //docs.add(document);

            DocumentReader.writeOutlookTextToFile(document, pTargetTxtFile);

//            if (pTargetPdfFile != null) {
//                //PdfConverter.exportWordFileToPdf(document, pSourceFile, pTargetPdfFile);
//                exportWordFileToPdf(document, pSourceFile, pTargetPdfFile);
//            }
            textToPdf(pTargetTxtFile, pTargetPdfFile, null);
            //DocumentReader.writeWordTextToFile(document, pTargetTxtFile);
        } catch (ComFailException ex) {
            String message = "Was not able to convert Outlook file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + (targetPdfFile == null ? "null" : targetPdfFile.getAbsolutePath());
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        } finally {
            if (document != null) {
                //DocumentReader.closeMail(document);
                //docs.remove(document);
            }
            if (app != null) {
//                DocumentReader.closeOutlook(app);
//                outlookApps.remove(app);
            }
        }
        rewriteFileWithCorrections(targetTxtFile, sourceFile.getName());
//                docConverterResultProp.get().isFinished.set(true);
        LOG.log(Level.INFO, "Converted this Outlook file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + (targetPdfFile == null ? "null" : targetPdfFile.getAbsolutePath()));
    }

//    public void excelToPdf(final String pSourceFile) {
//        final String targetTmpFile = getTempPdfFileName();
//        excelToPdf(pSourceFile, targetTmpFile);
//    }
    protected void excelToPdf(final String pSourceFile, final String pTargetPdfFile, final String pTargetTxtFile) {
        checkOfficeDisabled();
        checkExcelVersion();
        //String sMimeTypeSource = getMimeType(sSourceFile);
        //String sMimeTypeTarget = getMimeType(sTargetFile);
        //ActiveXComponent app = null;
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetFile = new File(pTargetPdfFile);
        final File targetTxtFile = pTargetTxtFile == null ? null : new File(pTargetTxtFile);
        LOG.log(Level.INFO, "Try to convert this Excel file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetFile);
        if (targetTxtFile != null) {
            checkNewFile(targetTxtFile);
        }
        initJacob();

        ActiveXComponent app = initExcel();
        excelApps.add(app);
        Dispatch document = null;

        try {
            app.setProperty("Visible", new Variant(false));

            Dispatch documents = app.getProperty("Workbooks").toDispatch();

            document = Dispatch.call(
                    documents,
                    "Open",
                    pSourceFile, //FileName
                    false, //ConfirmConversions
                    true //ReadOnly
            /* Weitere Parameter siehe https://msdn.microsoft.com/en-us/library/bb216319(v=office.12).aspx or https://docs.microsoft.com/en-us/office/vba/api/word.documents.open (newer) */
            ).toDispatch();
            docs.add(document);

            PdfConverter.exportExcelFileToPdf(document, pSourceFile, pTargetPdfFile);
            if (pTargetTxtFile != null) {
                DocumentReader.writeExcelTextToFile(document, pTargetTxtFile);
            }
        } catch (ComFailException ex) {
            String message = "Was not able to convert Excel file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetFile.getAbsolutePath();
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        } finally {
            if (document != null) {
                DocumentReader.closeDocument(document);
                docs.remove(document);
            }
            if (app != null) {
                DocumentReader.closeExcel(app);
                excelApps.remove(app);
            }
        }
        if (targetTxtFile != null) {
            rewriteFileWithCorrections(targetTxtFile, sourceFile.getName());
        }
        LOG.log(Level.INFO, "Converted this Excel file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
    }

//    public void imageToPdf(final String sSourceFile) {
//        final String targetTmpFile = getTempPdfFileName();
//        imageToPdf(sSourceFile, targetTmpFile);
//    }
//    public static void imageToPdf(final String sSourceFile, final String sTargetFile) {
//        try {
//            int border = 0;
//            Document document = new Document(PageSize.A4, border, border, border, border);
//            PdfWriter.getInstance(document, new FileOutputStream(sTargetFile));
//            document.open();
//            Image image = Image.getInstance(sSourceFile);
//            document.add(image);
//            document.close();
//        } catch (DocumentException | IOException ex) {
//            LOG.log(Level.SEVERE, "Was not able to convert image file: " + sSourceFile, ex);
//        }
//    }
    /**
     * Converts arbitrary image file to PDF
     * http://stackoverflow.com/a/42937466/241986
     *
     * @param pSourceFile contents of JPEG or PNG file
     * @param pTargetPdfFile stream to write out pdf, always closed after this
     * method execution.
     * @param pTargetTxtFile extracted text from image
     */
    public void imageToPdf(final String pSourceFile, final String pTargetPdfFile, final String pTargetTxtFile) {
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetPdfFile = new File(pTargetPdfFile);
        final File targetTxtFile = new File(pTargetTxtFile);
        LOG.log(Level.INFO, "Try to convert this Image file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetPdfFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetPdfFile);
        checkNewFile(targetTxtFile);

        try {
            final Image image = Image.getInstance(pSourceFile);

            //See http://stackoverflow.com/questions/1373035/how-do-i-scale-one-rectangle-to-the-maximum-size-possible-within-another-rectang
            Rectangle A4 = PageSize.A4;

            float scalePortrait = Math.min(A4.getWidth() / image.getWidth(),
                    A4.getHeight() / image.getHeight());

            float scaleLandscape = Math.min(A4.getHeight() / image.getWidth(),
                    A4.getWidth() / image.getHeight());

            // We try to occupy as much space as possible
            // Sportrait = (w*scalePortrait) * (h*scalePortrait)
            // Slandscape = (w*scaleLandscape) * (h*scaleLandscape)
            // therefore the bigger area is where we have bigger scale
            boolean isLandscape = scaleLandscape > scalePortrait;

            float w;
            float h;
            if (isLandscape) {
                A4 = A4.rotate();
                w = image.getWidth() * scaleLandscape;
                h = image.getHeight() * scaleLandscape;
            } else {
                w = image.getWidth() * scalePortrait;
                h = image.getHeight() * scalePortrait;
            }

            int margin = 0;
            Document document = new Document(A4, margin, margin, margin, margin);

            try ( FileOutputStream fos = new FileOutputStream(pTargetPdfFile)) {
                PdfWriter.getInstance(document, fos);
                document.open();
                try {
                    image.scaleAbsolute(w, h);
                    float posH = (A4.getHeight() - h) / 2;
                    float posW = (A4.getWidth() - w) / 2;

                    image.setAbsolutePosition(posW, posH);
                    image.setBorder(Image.NO_BORDER);
                    image.setBorderWidth(0);

                    document.newPage();
                    document.add(image);
                } finally {
                    document.close();
                }
            }
            wordToPdf(pTargetPdfFile, null, pTargetTxtFile);
//            final de.lb.cpx.reader.util.Document doc = DocumentReader.getText(sourceFile);
//            //wordToPdf(pTargetPdfFile, null, pTargetTxtFile);
//            final String encoding = "Cp1252";
//            FileUtils.writeStringToFile(targetTxtFile, doc.text, encoding);
        } catch (IOException | DocumentException ex) {
            String message = "Was not able to convert Image file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetPdfFile.getAbsolutePath();
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        }
        //rewriteFileWithCorrections(targetTxtFile, sourceFile.getName());
        LOG.log(Level.INFO, "Converted this Image file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetPdfFile.getAbsolutePath());
    }

    private static void rewriteFileWithCorrections(final File pFile, final String pName) {
        StringBuilder sb = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new FileReader(pFile))) {
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            final String origName = pName;
            final String name = pName == null ? "" : FilenameUtils.removeExtension(pName).replace("_", " ").trim();
            final String newText = "Datei: " + origName + "\r\n"
                    + "Titel: " + name + "\r\n"
                    + "Text: " + DocumentReader.getCorrectedText(sb.toString());
            final String encoding = "Cp1252";
            FileUtils.writeStringToFile(pFile, newText, encoding);
        } catch (IOException ex) {
            Logger.getLogger(DocumentConverter.class.getName()).log(Level.SEVERE, "Was not able to rewrite and correct file: " + pFile.getAbsolutePath(), ex);
        }
    }

    private void closeWordApps() {
        for (ActiveXComponent app : getWordApps()) {
            DocumentReader.closeWord(app);
            wordApps.remove(app);
        }
    }

    private void closeExcelApps() {
        for (ActiveXComponent app : getExcelApps()) {
            DocumentReader.closeExcel(app);
            excelApps.remove(app);
        }
    }

    private void closeOutlookApps() {
        for (ActiveXComponent app : getOutlookApps()) {
            DocumentReader.closeOutlook(app);
            outlookApps.remove(app);
        }
    }

    private void closeDocuments() {
        for (Dispatch doc : getDocuments()) {
            DocumentReader.closeDocument(doc);
            docs.remove(doc);
        }
    }

    public void closeApps() {
        closeDocuments();
        closeWordApps();
        closeExcelApps();
        closeOutlookApps();
    }

    public boolean deleteFile(File pFile) {
        if (pFile == null || !pFile.exists()) {
            return true;
        }
        if (pFile.isDirectory()) {
            LOG.log(Level.WARNING, "This is a directory but not a file: " + pFile.getAbsolutePath());
            //throw new IllegalArgumentException("This is a directory but not a file: " + pFile.getAbsolutePath());
            return false;
        }
        boolean deleted = false;
        try {
            Files.delete(pFile.toPath());
            deleted = true;
            LOG.log(Level.FINEST, "deleted file: {0}", pFile.getAbsolutePath());
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "was not able to delete file: " + pFile.getAbsolutePath(), ex);
        }
//        if (!deleted && pDeleteOnExit) {
//            pFile.deleteOnExit();
//            LOG.log(Level.INFO, "file will be deleted on exit: " + pFile.getAbsolutePath());
//        }
        return deleted;
//        if (file == null || !file.exists()) {
//            return;
//        }
//        if (file.delete()) {
//            LOG.log(Level.FINEST, "file successfully deleted: " + file.getAbsolutePath());
//        }
    }
}

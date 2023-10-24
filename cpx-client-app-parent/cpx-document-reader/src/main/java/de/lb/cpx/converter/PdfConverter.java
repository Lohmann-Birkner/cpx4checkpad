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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author niemeier
 */
public class PdfConverter {

    private static final Logger LOG = Logger.getLogger(PdfConverter.class.getName());
    //public final static int PAGE_FROM = 1;
    //public final static int PAGE_TO = 5;

    public void toPdf(final String pSourceFile) {
        final String targetTmpFile = getTempPdfFileName(new File(pSourceFile));
        toPdf(pSourceFile, targetTmpFile);
    }

    public void toPdf(final String pSourceFile, final String pTargetFile) {
        LOG.log(Level.INFO, "Try to convert this file to PDF: " + pSourceFile + ", target is " + pTargetFile);
        final File sourceFile = new File(pSourceFile);
        final File targetFile = new File(pTargetFile);
        checkExistingFile(sourceFile);
        checkNewFile(targetFile);
        final FILE_TYPES fileType = getFileType(sourceFile);
        if (fileType.isWord()) {
            LOG.log(Level.INFO, "This seems to be a Word file: " + sourceFile.getAbsolutePath());
            wordToPdf(pSourceFile, pTargetFile);
            return;
        }
        if (fileType.isText()) {
            LOG.log(Level.INFO, "This seems to be a Text file: " + sourceFile.getAbsolutePath());
            textToPdf(pSourceFile, pTargetFile);
            return;
        }
        if (fileType.isExcel()) {
            LOG.log(Level.INFO, "This seems to be an Excel file: " + sourceFile.getAbsolutePath());
            excelToPdf(pSourceFile, pTargetFile);
            return;
        }
        if (fileType.isCsv()) {
            LOG.log(Level.INFO, "This seems to be a CSV file: " + sourceFile.getAbsolutePath());
            csvToPdf(pSourceFile, pTargetFile);
            return;
        }
        if (fileType.isPdf()) {
            LOG.log(Level.INFO, "This seems to be a PDF file: " + sourceFile.getAbsolutePath());
            PdfConverter.this.pdfToPdf(pSourceFile, pTargetFile);
            return;
        }
        if (fileType.isImage()) {
            LOG.log(Level.INFO, "This seems to be an Image file: " + sourceFile.getAbsolutePath());
            imageToPdf(pSourceFile, pTargetFile);
            return;
        }
        throw new IllegalArgumentException("Unknown file type, cannot convert to PDF: " + sourceFile.getAbsolutePath());
    }

    public String pdfToPdf(final String pSourceFile) {
        final String targetTmpFile = getTempPdfFileName(new File(pSourceFile));
        PdfConverter.this.pdfToPdf(pSourceFile, targetTmpFile);
        return targetTmpFile;
    }

    public void pdfToPdf(final String pSourceFile, final String pTargetFile) {
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetFile = new File(pTargetFile);
        LOG.log(Level.INFO, "Try to copy this file: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetFile);
        try {
            FileUtils.copyFile(sourceFile, targetFile);
        } catch (IOException ex) {
            String message = "Was not able to copy file: " + sourceFile.getAbsolutePath() + ", target: " + targetFile.getAbsolutePath();
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        }
        LOG.log(Level.INFO, "Copied this file in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
    }

    public void textToPdf(final String pSourceFile) {
        final String targetTmpFile = getTempPdfFileName(new File(pSourceFile));
        wordToPdf(pSourceFile, targetTmpFile);
    }

    public void textToPdf(final String pSourceFile, final String pTargetFile) {
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetFile = new File(pTargetFile);
        LOG.log(Level.INFO, "Try to convert this Text file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetFile);
        final int threshold = 10000;
        final File source;
        if (sourceFile.length() <= threshold) {
            source = sourceFile;
        } else {
            final File tmpTextFile = new File(getTempTxtFileName(sourceFile));
            source = tmpTextFile;
            LOG.log(Level.INFO, "Text file is too large (> " + threshold + " bytes), will cut it before conversion: " + sourceFile.getAbsolutePath() + ", target temp file is " + tmpTextFile.getAbsolutePath());
            checkNewFile(tmpTextFile);
            try {
                String text = getTextFromFile(sourceFile.getAbsolutePath());
                text = text.substring(0, threshold) + "...";
                final String encoding = "Cp1252";
                FileUtils.writeStringToFile(tmpTextFile, text, encoding);
            } catch (IOException ex) {
                String message = "Was not able to convert Text file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetFile.getAbsolutePath();
                LOG.log(Level.SEVERE, message, ex);
                throw new IllegalStateException(message);
            }
        }
        wordToPdf(source.getAbsolutePath(), targetFile.getAbsolutePath());
        LOG.log(Level.INFO, "Converted this Text file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
    }

    public void csvToPdf(final String pSourceFile, final String pTargetFile) {
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetFile = new File(pTargetFile);
        LOG.log(Level.INFO, "Try to convert this CSV file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetFile);
        final int threshold = 10000;
        final File source;
        if (sourceFile.length() <= threshold) {
            source = sourceFile;
        } else {
            final File tmpCsvFile = new File(getTempCsvFileName(sourceFile));
            source = tmpCsvFile;
            LOG.log(Level.INFO, "CSV file is too large (> " + threshold + " bytes), will cut it before conversion: " + sourceFile.getAbsolutePath() + ", target temp file is " + tmpCsvFile.getAbsolutePath());
            checkNewFile(tmpCsvFile);
            try {
                String text = getTextFromFile(sourceFile.getAbsolutePath());
                text = text.substring(0, threshold) + "...";
                final String encoding = "Cp1252";
                FileUtils.writeStringToFile(tmpCsvFile, text, encoding);
            } catch (IOException ex) {
                String message = "Was not able to convert CSV file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetFile.getAbsolutePath();
                LOG.log(Level.SEVERE, message, ex);
                throw new IllegalStateException(message);
            }
        }
        excelToPdf(source.getAbsolutePath(), targetFile.getAbsolutePath());
        LOG.log(Level.INFO, "Converted this CSV file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
    }

    public String getTextFromFile(final String pFile) throws IOException {
        //final String encoding = "Cp1252";
        StringBuilder sb = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new FileReader(pFile))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        }
        return sb.toString();
    }

    public static void exportWordFileToPdf(final Dispatch pDocument, String pSourceFile, String pTargetFile) {
        final File file = new File(pSourceFile);
        if (isTextFile(file)) {
            Dispatch pageSetup = Dispatch.get(pDocument, "PageSetup").getDispatch();
            //Dispatch.put(pageSetup, "Zoom", new Variant(false));
            //Fit content on each worksheet to fit in a single page                                        
            //Dispatch.put(pageSetup, "FitToPagesWide", new Variant(2));
            //Dispatch.put(pageSetup, "FitToPagesTall", new Variant(1));
            //set print area to not chop off content
            //set left margin small
            int margin = 5;
            Dispatch.put(pageSetup, "LeftMargin", new Variant(margin));
            Dispatch.put(pageSetup, "RightMargin", new Variant(margin));
            Dispatch.put(pageSetup, "TopMargin", new Variant(margin));
            Dispatch.put(pageSetup, "BottomMargin", new Variant(margin));
            //Dispatch.put(pageSetup, "Orientation", new Variant(2));                
        }

        Dispatch.call(
                pDocument,
                "ExportAsFixedFormat",
                pTargetFile, //OutputFileName
                17, //ExportFormat (17=Export document into PDF format, 18=Export document into XML Paper Specification (XPS) format)
                false, //OpenAfterExport
                1, //OptimizeFor (1=Export for screen, which is a lower quality and results in a smaller file size, 0=Export for print, which is higher quailty and results in a larger file size)
                0, //Range (0=Exports the entire document, 1=Exports the contents of the current selection, 2=Exports the current page, 3=Exports the contents of a range using the starting and ending positions)
                0, //From (Specifies the starting page number, if the Range parameter is set to wdExportFromTo)
                0 //To (Specifies the ending page number, if the Range parameter is set to wdExportFromTo)
        /* Weitere Parameter siehe https://msdn.microsoft.com/en-us/library/bb256835(v=office.12).aspx or https://docs.microsoft.com/en-us/office/vba/api/word.documents.open (newer) */
        );
    }

    public static void exportExcelFileToPdf(final Dispatch document, final String pSourceFile, final String pTargetFile) {
        Dispatch activeWorksheets = Dispatch.call(document, "Worksheets").getDispatch();
        int workSheetCount = Dispatch.call(activeWorksheets, "Count").getInt();
        for (int i = 1; i < workSheetCount + 1; i++) {
            Dispatch currentWorksheet = Dispatch.call(activeWorksheets, "Item", new Variant(i)).getDispatch();
            //String currentWorksheetName = Dispatch.call(currentWorksheet, "Name").getString();
            Dispatch pageSetup = Dispatch.get(currentWorksheet, "PageSetup").getDispatch();
            //Dispatch.put(pageSetup, "Zoom", new Variant(false));
            //Fit content on each worksheet to fit in a single page                                        
            //Dispatch.put(pageSetup, "FitToPagesWide", new Variant(2));
            //Dispatch.put(pageSetup, "FitToPagesTall", new Variant(1));
            //set print area to not chop off content
            Dispatch.put(pageSetup, "PrintArea", new Variant(false));
            //set left margin small
            int margin = 5;
            Dispatch.put(pageSetup, "LeftMargin", new Variant(margin));
            Dispatch.put(pageSetup, "RightMargin", new Variant(margin));
            Dispatch.put(pageSetup, "TopMargin", new Variant(margin));
            Dispatch.put(pageSetup, "BottomMargin", new Variant(margin));
            Dispatch.put(pageSetup, "Orientation", new Variant(2));
        }

        Dispatch.call(
                document,
                "ExportAsFixedFormat",
                0, //Type, XlFixedFormatType (Can be either 0=xlTypePDF or 1=xlTypeXPS)
                pTargetFile, //OutputFileName
                1, //Quality (Can be set to either 0=xlQualityStandard or 1=xlQualityMinimum)
                false, //IncludeDocProperties (Set to True to indicate that document properties should be included or set to False to indicate that they are omitted)
                false, //IgnorePrintAreas (	If set to True , ignores any print areas set when publishing. If set to False , will use the print areas set when publishing.)
                1, //From (Specifies the starting page number, if the Range parameter is set to wdExportFromTo)
                100, //To (Specifies the ending page number, if the Range parameter is set to wdExportFromTo)
                false //OpenAfterPublish (If set to True displays file in viewer after it is published. If set to False the file is published but not displayed)
        /* Weitere Parameter siehe https://msdn.microsoft.com/en-us/library/bb256835(v=office.12).aspx or https://docs.microsoft.com/en-us/office/vba/api/word.documents.open (newer) */
        );
    }

    public void wordToPdf(final String pSourceFile) {
        final String targetTmpFile = getTempPdfFileName(new File(pSourceFile));
        wordToPdf(pSourceFile, targetTmpFile);
    }

    public void wordToPdf(final String pSourceFile, final String pTargetFile) {
        //String sMimeTypeSource = getMimeType(sSourceFile);
        //String sMimeTypeTarget = getMimeType(sTargetFile);
        //ActiveXComponent app = null;
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetFile = new File(pTargetFile);
        LOG.log(Level.INFO, "Try to convert this Word file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetFile);
        initJacob();

        ActiveXComponent app = initWord();
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

            exportWordFileToPdf(document, pSourceFile, pTargetFile);
        } catch (ComFailException ex) {
            String message = "Was not able to convert Word file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetFile.getAbsolutePath();
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        } finally {
            if (document != null) {
                DocumentReader.closeDocument(document);
            }
            if (app != null) {
                DocumentReader.closeWord(app);
            }
        }
//                docConverterResultProp.get().isFinished.set(true);
        LOG.log(Level.INFO, "Converted this Word file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
    }

    public void excelToPdf(final String pSourceFile) {
        final String targetTmpFile = getTempPdfFileName(new File(pSourceFile));
        excelToPdf(pSourceFile, targetTmpFile);
    }

    public void excelToPdf(final String pSourceFile, final String pTargetFile) {
        //String sMimeTypeSource = getMimeType(sSourceFile);
        //String sMimeTypeTarget = getMimeType(sTargetFile);
        //ActiveXComponent app = null;
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetFile = new File(pTargetFile);
        LOG.log(Level.INFO, "Try to convert this Excel file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetFile);
        initJacob();

        ActiveXComponent app = initExcel();
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

            exportExcelFileToPdf(document, pSourceFile, pTargetFile);
        } catch (ComFailException ex) {
            String message = "Was not able to convert Word file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetFile.getAbsolutePath();
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        } finally {
            if (document != null) {
                DocumentReader.closeDocument(document);
            }
            if (app != null) {
                DocumentReader.closeExcel(app);
            }
        }
        LOG.log(Level.INFO, "Converted this Excel file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
    }

    public void imageToPdf(final String pSourceFile) {
        final String targetTmpFile = getTempPdfFileName(new File(pSourceFile));
        imageToPdf(pSourceFile, targetTmpFile);
    }

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
     * @param pTargetFile stream to write out pdf, always closed after this
     * method execution.
     */
    public void imageToPdf(final String pSourceFile, final String pTargetFile) {
        final long startTime = System.currentTimeMillis();
        final File sourceFile = new File(pSourceFile);
        final File targetFile = new File(pTargetFile);
        LOG.log(Level.INFO, "Try to convert this Image file to PDF: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
        checkExistingFile(sourceFile);
        checkNewFile(targetFile);

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

            PdfWriter.getInstance(document, new FileOutputStream(pTargetFile));
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
        } catch (IOException | DocumentException ex) {
            String message = "Was not able to convert Image file to PDF: " + sourceFile.getAbsolutePath() + ", target: " + targetFile.getAbsolutePath();
            LOG.log(Level.SEVERE, message, ex);
            throw new IllegalStateException(message);
        }
        LOG.log(Level.INFO, "Converted this Image file to PDF in " + (System.currentTimeMillis() - startTime) + " ms: " + sourceFile.getAbsolutePath() + ", target is " + targetFile.getAbsolutePath());
    }

}

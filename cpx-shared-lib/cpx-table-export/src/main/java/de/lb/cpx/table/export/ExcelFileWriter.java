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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.table.export;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nandola
 */
public class ExcelFileWriter implements ExportWriter {

    // POI Project’s pure Java implementation of the Excel 2007 OOXML (.xlsx) file format
    private final XSSFWorkbook workbook; // = new XSSFWorkbook();
//    private XSSFDataFormat dataFormat; // = workbook.createDataFormat();
    private final XSSFSheet sheet;//= workbook.createSheet("export");

//    private Font mDefaultFont = null;
//    private CellStyle mCellStyleAlignLeft = null;
//    private CellStyle mCellStyleAlignCenter = null;
//    private CellStyle mCellStyleAlignRight = null;
//    private CellStyle mCellStyleDate = null;
//    private CellStyle mCellStyleDateTime = null;
//    private CellStyle mCellStyleLink = null;
    //private String filePath = "";
    //private File file = null;
    //private Object[][] data = null;
    private final FileOutputStream outputStream;

    public ExcelFileWriter(final String pFilePath) throws IOException {
        //this.filePath = filePath;
        //file = new File(filePath);
        //this.data = Arrays.copyOf(data, data.length);
        workbook = new XSSFWorkbook();
        //XSSFDataFormat dataFormat = workbook.createDataFormat();
//        XSSFSheet sheet = workbook.getSheetAt(0);
        sheet = workbook.createSheet("export");
        sheet.createFreezePane(0, 1);     //Freeze top row alone
        // CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)

        outputStream = new FileOutputStream(pFilePath);

        //WriteDataToSheet(sheet, pData);
//         Freeze top row and first column alone 
//        sheet.createFreezePane(2, 1);
//        sheet.createFreezePane(3, 1, 3, 1);
//        if (fileName.endsWith("xlsx")) {
//            workbook = new XSSFWorkbook();
//        } else if (fileName.endsWith("xls")) {
//            workbook = new HSSFWorkbook();
//        } else {
//            throw new CpxIllegalArgumentException("The specified file is not Excel file");
//        }
    }

    @Override
    public void writeData(final Iterator<Object[]> pDataIterator) {
        if (pDataIterator == null) {
            throw new IllegalArgumentException("excel writer needs a data iterator that is not null!");
        }
        int rowNum = 0;
        //String excelFormatPattern = DateFormatConverter.convert(Locale.GERMANY, "dd MMMM, yyyy");
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
//        cellStyle.setDataFormat(dataFormat.getFormat(excelFormatPattern));
//        cellStyle.setWrapText(true);
//        XSSFFont font = workbook.createFont();
//        font.setFontHeight(11);
//        cellStyle.setFont(font);
        //cellStyle.setAlignment(HorizontalAlignment.LEFT);
        //cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        //cellStyle.setShrinkToFit(true);

        CellStyle labelCellStyle = workbook.createCellStyle();
        XSSFFont createFont = workbook.createFont();
        createFont.setBold(true);
        labelCellStyle.setFont(createFont);
        labelCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        labelCellStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        labelCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        while (pDataIterator.hasNext()) {
            Object[] fields = pDataIterator.next();
            //for (Object[] fields : data) {
            XSSFRow row = sheet.createRow(rowNum++);
            int colNum = 0;

            for (Object field : fields) {
                XSSFCell cell = row.createCell(colNum++);

                if (row.getRowNum() == 0) {
                    cell.setCellStyle(labelCellStyle);
                }
                if (field != null) {
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                        cell.setCellType(CellType.STRING);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    } else if (field instanceof Boolean) {
                        if ((Boolean) field) {
                            cell.setCellValue("Ja");
                        } else {
                            cell.setCellValue("Nein");
                        }
                    } else if (field instanceof Date) {
//                        cell.setCellValue(field.toString());
                        cell.setCellValue((Date) field);
                        cell.setCellStyle(dateCellStyle);
//                        Calendar lDatum = new GregorianCalendar();
//                        lDatum.setTime((Date) field);
//                        if (lDatum.get(Calendar.HOUR) == 0 && lDatum.get(Calendar.MINUTE) == 0 && lDatum.get(Calendar.SECOND) == 0) {
////                            mCellStyleDate = workbook.createCellStyle();
////                            mCellStyleDate.setDataFormat(dataFormat.getFormat("dd.mm.yyyy"));
//                            lCellStyle = mCellStyleDate;
//                        } else {
////                            mCellStyleDateTime = workbook.createCellStyle();
////                            mCellStyleDateTime.setDataFormat(dataFormat.getFormat("dd.mm.yyyy HH:mm:ss"));
//                            lCellStyle = mCellStyleDateTime;
//                        }
                    } else if (field instanceof Calendar) {
                        cell.setCellValue((Calendar) field);
                    } else if (field instanceof Long) {
                        cell.setCellValue(field.toString());
                    } else if (field instanceof Double) {
                        cell.setCellValue((Double) field);
                    } else if (field instanceof Float) {
                        cell.setCellValue((Float) field);
                    } else {
                        cell.setCellValue(field.toString());
                    }
                }
            }
            
           
        }
 //        createPivotSheet();
        
        // after writing all data to the excel file, we can set max cell size
        // here, it takes some time to reconstruct cell width in excel file.
//        for (int i = 0; i < data[0].length; i++) {
//            sheet.autoSizeColumn(i);
//        }
//        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
//            try {
//                workbook.write(outputStream);
//            } catch (IOException ex) {
//                Logger.getLogger(ExcelFileWriter.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            outputStream.close();
//            workbook.close();
//        } catch (FileNotFoundException e) {
//            Logger.getLogger(ExcelFileWriter.class.getName()).log(Level.SEVERE, "Was not able to write Excel file", e);
////            throw new FileNotFoundException(e.getLocalizedMessage());
//        } catch (IOException e) {
//            Logger.getLogger(ExcelFileWriter.class.getName()).log(Level.SEVERE, "Was not able to write Excel file", e);
//        }
//        try {
//            writeWorkBook();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ExcelFileWriter.class.getName()).log(Level.SEVERE, "Was not able to write Excel file", ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ExcelFileWriter.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //return filePath;
    }

//    public String WriteDataToSheet(Object[][] data) {
//        return filePath = WriteDataToSheet(sheet, data);
//    }
//    private String WriteDataToSheet(XSSFSheet sheet, Object[][] data) throws FileNotFoundException {
    @Override
    public void writeData(final Object[][] pData) {
        if (pData == null) {
            throw new IllegalArgumentException("excel writer needs a data that is not null!");
        }
        sheet.setAutoFilter(new CellRangeAddress(0, pData.length, 0, pData[0].length - 1));  // filter and search functionality
        Iterator<Object[]> it = Arrays.asList(pData).iterator();
        writeData(it);
    }

//    protected void writeWorkBook() throws FileNotFoundException, IOException {
//        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
//            workbook.write(outputStream);
//        }
//        workbook.close();
//    }
//    public File getFile() {
//        return file;
//    }
//
//    public String getFilename() {
//        return file.getName();
//    }
    @Override
    public void close() throws Exception {
        if (outputStream != null && workbook != null) {
            autoSizeColumns(15000);
            workbook.write(outputStream);
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (workbook != null) {
            workbook.close();
        }
    }

    public void autoSizeColumns(final int pMaxWidth) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(0);
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                    if (pMaxWidth > 0 && sheet.getColumnWidth(columnIndex) > pMaxWidth) {
                        sheet.setColumnWidth(columnIndex, pMaxWidth);
                    }
                }
            }
        }
    }

    private void createPivotSheet() {
        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        int firstCol = sheet.getRow(0).getFirstCellNum();
        int lastCol = sheet.getRow(0).getLastCellNum();
        CellReference topLeft = new CellReference(firstRow, firstCol);
        CellReference botRight = new CellReference(lastRow, lastCol - 1);
        AreaReference aref = new AreaReference(topLeft, botRight, SpreadsheetVersion.EXCEL2007);
        CellReference pos = new CellReference(firstRow + 4, lastCol + 1);
        XSSFPivotTable pivotTable = sheet.createPivotTable(aref, pos);
        pivotTable.addRowLabel(0);
        pivotTable.addRowLabel(1);
        pivotTable.addColumnLabel(DataConsolidateFunction.SUM,
                      4, "Sum of all" );
    }

}

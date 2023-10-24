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
package de.lb.cpx.serviceutil.export;

import de.lb.cpx.shared.dto.SearchItemDTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 *
 * @author niemeier
 * @param <T> search list dto type
 */
public abstract class ProcessExcelItemCallback<T> implements ProcessItemCallback<SXSSFWorkbook, T> {

    public static final int WINDOW_SIZE = 1000;
    private File targetFile;
    private CellStyle labelCellStyle;
    private CellStyle dateCellStyle;
    private SXSSFSheet sheet;

//    @Override
//    public abstract void call(SXSSFWorkbook pWorkbook, int pNo, T pDto, Set<String> pVisibleColumns) throws IOException, InterruptedException;
    @Override
    public SXSSFWorkbook createWriter(final File pTargetFile) throws IOException {
        targetFile = pTargetFile;

        SXSSFWorkbook workbook = new SXSSFWorkbook(WINDOW_SIZE);
        sheet = workbook.createSheet("export");
        sheet.createFreezePane(0, 1);

        labelCellStyle = workbook.createCellStyle();
        dateCellStyle = workbook.createCellStyle();

        //String excelFormatPattern = DateFormatConverter.convert(Locale.GERMANY, "dd MMMM, yyyy");
        dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
        dateCellStyle.setShrinkToFit(true);

        labelCellStyle = workbook.createCellStyle();
        Font createFont = workbook.createFont();
        createFont.setBold(true);
        labelCellStyle.setFont(createFont);
        labelCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        labelCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return workbook;
    }

    @Override
    public void closeWriter(final SXSSFWorkbook pWorkbook) throws IOException {
        try (pWorkbook) {
            try ( FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                //SXSSFSheet sheet = pWorkbook.getSheetAt(0);
                //sheet.getPhysicalNumberOfRows()

                // set filters to the header
                sheet.setAutoFilter(new CellRangeAddress(0, sheet.getLastRowNum(), 0, sheet.getRow(sheet.getLastRowNum()).getLastCellNum()));  // filter and search functionality (CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol))

//                setMaxCellSize(pWorkbook.getSheetAt(0));
//                autoSizeColumns(pWriter, 15000);
                pWorkbook.write(outputStream);
            }
            pWorkbook.dispose();
        }
    }

//    protected void setMaxCellSize(SXSSFSheet sheet) {
//        for (int i = 0; i < sheet.getHeader(); i++) {
//            sheet.autoSizeColumn(i);
//        }
//    }
//    protected void setSizeColumnsWithHeader(int width) {
//        for (int i = 0; i < headerCellCount; i++) {
//            currentSheet.setColumnWidth(i, width);
//        }
//    }
    @Override
    public void writeRow(final SXSSFWorkbook pWorkbook, final List<Object> pValues, final int pRowNum) {
//        SXSSFSheet sheet = pWorkbook.getSheetAt(0);
        SXSSFRow row = sheet.createRow(pRowNum);
        int colNum = 0;

        for (Object field : pValues) {
            SXSSFCell cell = row.createCell(colNum++);

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

    //auto size of is not supported by SXSSFWorkbook (look at limitations of Streaming version of XSSFWorkbook)!
//    public void autoSizeColumns(final SXSSFWorkbook pWriter, final int pMaxWidth) {
//        if (pWriter == null) {
//            return;
//        }
//        int numberOfSheets = pWriter.getNumberOfSheets();
//        for (int i = 0; i < numberOfSheets; i++) {
//            Sheet sheet = pWriter.getSheetAt(i);
//            if (sheet.getPhysicalNumberOfRows() > 0) {
//                Row row = sheet.getRow(0);
//                if (row == null) {
//                    continue;
//                }
//                Iterator<Cell> cellIterator = row.cellIterator();
//                while (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    int columnIndex = cell.getColumnIndex();
//                    sheet.autoSizeColumn(columnIndex);
//                    if (pMaxWidth > 0 && sheet.getColumnWidth(columnIndex) > pMaxWidth) {
//                        sheet.setColumnWidth(columnIndex, pMaxWidth);
//                    }
//                }
//            }
//        }
//    }
}

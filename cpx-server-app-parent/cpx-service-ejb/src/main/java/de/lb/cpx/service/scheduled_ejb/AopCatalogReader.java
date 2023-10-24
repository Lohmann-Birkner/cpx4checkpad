/*
 * Copyright (c) 2022 Lohmann & Birkner.
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
 *    2022  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.scheduled_ejb;

import de.lb.cpx.server.commonDB.model.COpsAop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author gerschmann
 */
public class AopCatalogReader implements AutoCloseable {
    private static final String SHEET_1 = "Abschnitt 1";
    private static final String SHEET_2 = "Abschnitt 2";
    public static final String SHEET_0 = "AOP-Katalog_";
    XSSFWorkbook book = null;
    private final List<AopCatalogEntry> entries = new LinkedList<>();
    
    public AopCatalogReader(){}
        
    public void readExcel(String pPath, int pYear) throws IOException {


        book = new XSSFWorkbook(pPath);


        XSSFSheet sheet1 = book.getSheet(SHEET_0 + pYear + " " + SHEET_1);
        if(sheet1 == null){
            sheet1 = book.getSheet(pYear + " " + SHEET_1);
        }
        if(sheet1 == null){
            throw new IOException( "excel file is not complete, sheet1 not found");
        }
        XSSFSheet sheet2 = book.getSheet(SHEET_0 + pYear + " " + SHEET_2);
        if(sheet2 == null){
            sheet2 = book.getSheet(pYear + " " + SHEET_2);
        }
        if(sheet2 == null){
            throw new IOException( "excel file is not complete, sheet 2 not found");
        }
// read sheet1
        readSheet(sheet1, pYear >2022?0:3, 1);
    
// read sheet2
        readSheet(sheet2,  pYear >2022?0:5, 2);

    }
    
    private void readSheet(XSSFSheet sheet, int pLastCol, int pSheet){
        int rowsNum = sheet.getLastRowNum();
        for(int i = 0; i < rowsNum; i++){
            XSSFRow row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            Cell cell = row.getCell(0);
            if(cell.getStringCellValue()== null || !cell.getStringCellValue().contains("OPS-Kode")){
                continue;
            }
            
            for(int k = i + 1; k < rowsNum; k++){
                 row = sheet.getRow(k);
                 if(row == null){
                     break;
                 }

                if(row.getCell(0) != null && row.getCell(0).getStringCellValue() != null && row.getCell(0).getStringCellValue().length() <=8){
                   AopCatalogEntry entry = new AopCatalogEntry(row.getCell(0).getStringCellValue(), pLastCol == 0?"":row.getCell(pLastCol).toString(), pSheet);
                   if(!entries.contains(entry)){
                       entries.add(entry);
                   }
                }

            }
        }
        
    }

    public List<AopCatalogEntry> getEntries() {
        return entries;
    }

    @Override
    public void close() throws Exception {
        if (book == null){
            return;
        }
        book.close();
    }


    public int getEntriesCount() {
        return entries.size();
    }

    
    
    class AopCatalogEntry {
        private final String ops;
        private final String category;
        private final int sheet;
        
        public AopCatalogEntry(String pOps, String pCategory, int pSheet){
            ops = pOps;
            category = pCategory.length() > 0?checkCategory(pCategory):"";
            sheet = pSheet;
        }

        public String getOps() {
            return ops;
        }

        public int getSheet() {
            return sheet;
        }

        public String getCategory() {
            return category;
        }
        
        @Override
        public boolean equals(Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AopCatalogEntry)){
                return false;
            }
            return ops.equals(((AopCatalogEntry)other).getOps());
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.ops);
            return hash;
        }
        
        @Override
        public String toString(){
            return ops +( category.length() > 0?(": " + category):"");
        }

        private String checkCategory(String pCategory) {
            if(pCategory.trim().equals("1.0")){
                return "1";
            }
            if(pCategory.trim().equals("2.0")){
                return "2";
            }
            return pCategory;
        }
    }
}

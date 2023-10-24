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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author nandola
 */
public class CsvFileWriter implements ExportWriter {

    private static final char DEFAULT_SEPARATOR = ';';
    private static final String NEW_LINE_SEPARATOR = "\n";
    //private String filePath = "";
//    private File file = null;
//    private Object[][] data = null;
    private final BufferedWriter writer;

    public CsvFileWriter(final String pFilePath) throws IOException {
        //this.filePath = filePath;
        final String defaultEncoding = "Cp1252";
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pFilePath), defaultEncoding));
//        file = new File(filePath);
//        this.data = data == null ? null : Arrays.copyOf(data, data.length);
//        data == null ? null : Arrays.copyOf(data, data.length);

        //writeDataToCsvFile(filePath, data);
    }

    @Override
    public void writeData(final Object[][] pData) throws IOException {
        if (pData == null) {
            throw new IllegalArgumentException("excel writer needs a data that is not null!");
        }
        Iterator<Object[]> it = Arrays.asList(pData).iterator();
        writeData(it);
    }

    @Override
    public void writeData(final Iterator<Object[]> pDataIterator) throws IOException {
        if (pDataIterator == null) {
            throw new IllegalArgumentException("csv writer needs a data iterator that is not null!");
        }
        StringBuilder builder = new StringBuilder();
        while (pDataIterator.hasNext()) {
            Object[] fields = pDataIterator.next();
            //for (Object[] fields : pDataIterator) {  // for each row
            for (Object field : fields) {   // for each column
                if (field != null) {
                    if (field instanceof String) {
                        builder.append(field.toString()).append(DEFAULT_SEPARATOR);
                    } else if (field instanceof Integer) {
                        builder.append(field).append(DEFAULT_SEPARATOR);
                    } else if (field instanceof Boolean) {
                        if ((Boolean) field) {
                            builder.append("Ja").append(DEFAULT_SEPARATOR);
                        } else {
                            builder.append("Nein").append(DEFAULT_SEPARATOR);
                        }
                    } else if (field instanceof Date) {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        Date input = (Date) field;
                        LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                        builder.append(date).append(DEFAULT_SEPARATOR); // gives date format based on zone and system language
                        builder.append(dateFormatter.format(date)).append(DEFAULT_SEPARATOR);
                    } else if (field instanceof Calendar) {
                        builder.append(field).append(DEFAULT_SEPARATOR);
                    } else if (field instanceof Long) {
                        builder.append(field.toString()).append(DEFAULT_SEPARATOR);
                    } else if (field instanceof Double) {
                        builder.append(field).append(DEFAULT_SEPARATOR);
                    } else if (field instanceof Float) {
                        builder.append(field).append(DEFAULT_SEPARATOR);
                    } else {
                        builder.append(field.toString()).append(DEFAULT_SEPARATOR);
                    }
                } else if (field == null) {
                    builder.append(DEFAULT_SEPARATOR);
                }
            }
            builder.append(NEW_LINE_SEPARATOR);//append new line at the end of the row
        }

        writer.write(builder.toString());//save the string representation of the board
        //CpxSystemProperties.DEFAULT_ENCODING = Cp1252
//        final String defaultEncoding = "Cp1252";
//        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), defaultEncoding))) {
//            writer.write(builder.toString());//save the string representation of the board
//            writer.close();
//        } catch (IOException ex) {
//            Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    @Override
    public void close() throws Exception {
        if (writer != null) {
            writer.close();
        }
    }

}

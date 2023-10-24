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

import de.lb.cpx.serviceutil.export.ProcessItemCallback;
import de.lb.cpx.shared.dto.SearchItemDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author niemeier
 * @param <T> search list dto type
 */
public abstract class ProcessCsvItemCallback<T> implements ProcessItemCallback<Writer, T> {

    public static final String DELIMITER = ";";
    public static final String DELIMITER_REPLACEMENT = ",";
    public static final String NEWLINE = "\n";

//    @Override
//    public abstract void call(Writer pWriter, int pNo, T pDto, Set<String> pVisibleColumns) throws IOException, InterruptedException;
    @Override
    public Writer createWriter(final File pTargetFile) throws IOException {
        final String charset = CpxSystemProperties.DEFAULT_ENCODING;
        final Writer listWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pTargetFile), charset));
        return listWriter;
    }

    @Override
    public void closeWriter(final Writer pWriter) throws IOException {
        try (pWriter) {
            pWriter.flush();
        }
    }

    @Override
    public void writeRow(final Writer pWriter, final List<Object> pValues, final int pRowNum) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Object field : pValues) {   // for each column
            if (field != null) {
                if (field instanceof String) {
                    String value = field.toString().replace(DELIMITER, DELIMITER_REPLACEMENT);
                    builder.append(value).append(DELIMITER);
                } else if (field instanceof Integer) {
                    builder.append(field).append(DELIMITER);
                } else if (field instanceof Boolean) {
                    if ((Boolean) field) {
                        builder.append("Ja").append(DELIMITER);
                    } else {
                        builder.append("Nein").append(DELIMITER);
                    }
                } else if (field instanceof Date) {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    Date input = (Date) field;
                    LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                        builder.append(date).append(DEFAULT_SEPARATOR); // gives date format based on zone and system language
                    builder.append(dateFormatter.format(date)).append(DELIMITER);
                } else if (field instanceof Calendar) {
                    builder.append(field).append(DELIMITER);
                } else if (field instanceof Long) {
                    builder.append(field.toString()).append(DELIMITER);
                } else if (field instanceof Double) {
                    builder.append(Lang.toDecimal((Double)field)).append(DELIMITER);
                } else if (field instanceof Float) {
                    (field == null?builder.append(""):builder.append(Lang.toDecimal(((Float)field).doubleValue()))).append(DELIMITER);
                } else {
                    builder.append(field.toString()).append(DELIMITER);
                }
            } else {
                //field is null
                builder.append(DELIMITER);
            }
        }
        //builder.append(DELIMITER);//append new line at the end of the row
        builder.append(NEWLINE);
        pWriter.append(builder.toString());
    }
}

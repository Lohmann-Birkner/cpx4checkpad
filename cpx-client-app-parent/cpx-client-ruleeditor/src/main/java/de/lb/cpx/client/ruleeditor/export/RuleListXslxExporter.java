/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.export;

import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.serviceutil.export.ProcessExcelItemCallback;
import de.lb.cpx.serviceutil.export.ProcessItemCallback;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 *
 * @author wilde
 */
public class RuleListXslxExporter extends RuleListExporter{
    

    @Override
    public ExportTypeEn getExportType() {
        return ExportTypeEn.EXCEL;
    }

    @Override
    public ProcessItemCallback<SXSSFWorkbook, CrgRules> getProcessItemCallback() {
        return new ProcessExcelItemCallback<CrgRules>() {
            @Override
            public void call(SXSSFWorkbook pWriter, int pNo, CrgRules pDto) throws IOException {
                if (pNo == 0) {
                    List<String> values = getListMapper().getTitles();
                    writeRow(pWriter, new ArrayList<>(values), 0);
                    return;
                }
                List<Object> values = getListMapper().mapValues(pDto);
                writeRow(pWriter, values, pNo);
            }
        };
    }
    
}

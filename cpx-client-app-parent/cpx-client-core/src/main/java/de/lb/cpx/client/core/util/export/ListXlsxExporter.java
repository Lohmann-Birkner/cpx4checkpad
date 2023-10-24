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
package de.lb.cpx.client.core.util.export;

import de.lb.cpx.model.enums.ExportTypeEn;

/**
 *
 * @author wilde
 * @param <T> type to export
 */
public abstract class ListXlsxExporter<T> implements ListExporter<T>{

    private ExportListMapper<T> mapper;


    @Override
    public ExportTypeEn getExportType() {
        return ExportTypeEn.EXCEL;
    }

    @Override
    public void setListMapper(ExportListMapper<T> pMapper) {
        this.mapper = pMapper;
    }

    @Override
    public ExportListMapper<T> getListMapper() {
        return mapper;
    }
    
}

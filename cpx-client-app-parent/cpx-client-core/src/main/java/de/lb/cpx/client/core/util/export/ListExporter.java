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
import de.lb.cpx.serviceutil.export.ProcessItemCallback;
import java.io.Closeable;
import java.io.File;

/**
 *
 * @author wilde
 * @param <T> Object type to export
 */
public interface ListExporter<T> {
    public void setListMapper(ExportListMapper<T> pMapper);
    public ExportListMapper<T> getListMapper();
    public ExportTypeEn getExportType();
    public String getExportPath();
    public File export();
    public ProcessItemCallback<? extends Closeable,T> getProcessItemCallback();
    
}

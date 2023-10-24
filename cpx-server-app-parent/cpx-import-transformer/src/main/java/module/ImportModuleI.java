/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package module;

import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import java.io.Serializable;

/**
 * Specifies the import modules in general
 *
 * @author Dirk Niemeier
 * @param <T> type
 */
public interface ImportModuleI<T extends CpxJobImportConfig> extends Serializable {

    /**
     * Import name (P21, SAP...)
     *
     * @return name
     */
    String getName();

    /**
     * Output directory for transformation files (intermedia files)
     *
     * @return output directory
     */
    String getOutputDirectory();

    T getInputConfig();

    /**
     * Returns a temporary output directory for transformation files (intermedia
     * files) You can customize this of couse! That should not be neccessary,
     * but it's faster when you use a SSD instead of HDD here!
     *
     * @return temporary output directory
     */
    public static String getTempOutputDirectory() {
        final String outputDirectory = System.getProperty("java.io.tmpdir") + "\\cpx_transformation";
        return outputDirectory;
    }
    
    void setOutputDirectory(String path);

}

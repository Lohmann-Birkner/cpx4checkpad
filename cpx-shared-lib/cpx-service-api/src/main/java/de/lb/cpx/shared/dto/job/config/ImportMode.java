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
package de.lb.cpx.shared.dto.job.config;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public enum ImportMode {

    Version("version", false, false, "Creates a new version if case data has changed"),
    Overwrite("overwrite", true, false, "Deletes previously created versions of an already existing case"),
    Truncate("truncate", true, true, "Deletes all hospital cases from database");

    private static final Logger LOG = Logger.getLogger(ImportMode.class.getName());

    public final String modeName;
    public final boolean overwrite;
    public final boolean truncate;
    public final String description;

    ImportMode(final String pName, final boolean pOverwrite, final boolean pTruncate, final String pDescription) {
        modeName = pName == null ? "" : pName.trim();
        overwrite = pOverwrite;
        truncate = pTruncate;
        description = pDescription == null ? "" : pDescription.trim();
    }

    /**
     * Detects the import mode of a passed value
     *
     * @param pImportMode potencial import mode
     * @return import mode
     * @throws IllegalArgumentException error if import mode cannot be detected
     */
    public static ImportMode getImportMode(final String pImportMode) throws IllegalArgumentException {
        return getImportMode(pImportMode, null);
    }

    /**
     * Detects the import mode of a passed value
     *
     * @param pImportMode potencial import mode
     * @param pDefault use this as default import mode if no value is passed
     * @return import mode
     * @throws IllegalArgumentException error if import mode cannot be detected
     */
    public static ImportMode getImportMode(final String pImportMode, final ImportMode pDefault) throws IllegalArgumentException {
        final String importMode = pImportMode == null ? "" : pImportMode.trim();
        if (!importMode.isEmpty()) {
            for (ImportMode mode : values()) {
                if (mode.modeName.equalsIgnoreCase(importMode)) {
                    return mode;
                }
            }
        }

        if (importMode.isEmpty() && pDefault != null) {
            LOG.log(Level.WARNING, "You did not pass an import mode. Import mode has to be in [" + ImportMode.getNamesAsCsv() + "]! Assume default '" + pDefault.modeName + "' instead");
            return pDefault;
        } else {
            throw new IllegalArgumentException("Import mode has to be in [" + getNamesAsCsv() + "], but you passed '" + importMode + "'");
        }
    }

    /**
     * Just return an array with the import mode names
     *
     * @return names of import modes (overwrite, version, truncate)
     */
    public static String[] getNames() {
        String[] names = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            ImportMode mode = values()[i];
            names[i] = mode.modeName;
        }
        return names;
    }

    /**
     * Just returns a comma separated list of import mode names
     *
     * @return list of import modes (overwrite, version, truncate)
     */
    public static String getNamesAsCsv() {
        String[] names = getNames();
        return String.join(",", names);
    }

    @Override
    public String toString() {
        return "ImportMode{" + "name=" + modeName + ", overwrite=" + overwrite + ", truncate=" + truncate + ", description=" + description + '}';
    }

}

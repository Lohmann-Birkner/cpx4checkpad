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
package transformer.impl.p21.versionhelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import line.AbstractLine;
import transformer.impl.P21ToCpxTransformer;
import static transformer.impl.P21ToCpxTransformer.DELIMITER;
import util.FileManager;

/**
 *
 * @author niemeier
 */
public class P21File implements Serializable {

    private static final Logger LOG = Logger.getLogger(P21File.class.getName());

    private static final long serialVersionUID = 1L;

    public final String fileName;
    public final File actualFile;
    public final boolean mandatory;
    public final boolean found;

    public P21File(final String pFileName, final File pActualFile, final boolean pMandatory) {
        this.fileName = pFileName;
        this.actualFile = pActualFile;
        this.mandatory = pMandatory;
        this.found = pActualFile != null;
    }

    public String getFileName() {
        return fileName;
    }

    public File getActualFile() {
        return actualFile;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isOptional() {
        return !mandatory;
    }

    public boolean isFound() {
        return found;
    }

    public boolean isMissing() {
        return !found;
    }

    public boolean isIcdFile() {
        return fileName.equalsIgnoreCase(P21ToCpxTransformer.ICD_FILENAME);
    }

    public boolean isOpsFile() {
        return fileName.equalsIgnoreCase(P21ToCpxTransformer.OPS_FILENAME);
    }

    public boolean isFabFile() {
        return fileName.equalsIgnoreCase(P21ToCpxTransformer.FAB_FILENAME);
    }

    public boolean isFallFile() {
        return fileName.equalsIgnoreCase(P21ToCpxTransformer.FALL_FILENAME);
    }

    public boolean isEntgelteFile() {
        return fileName.equalsIgnoreCase(P21ToCpxTransformer.ENT_FILENAME);
    }

    public boolean isInfoFile() {
        return fileName.equalsIgnoreCase(P21ToCpxTransformer.INFO_FILENAME);
    }

    public boolean isPatientFile() {
        return fileName.equalsIgnoreCase(P21ToCpxTransformer.PATIENT_FILENAME);
    }

    public boolean isFallZusatzFile() {
        return fileName.equalsIgnoreCase(P21ToCpxTransformer.FALL_ZUSATZ_FILENAME);
    }

    public String[] getHeadline() {
        File file = actualFile;
        if (!file.exists() || !file.isFile()) {
            LOG.log(Level.INFO, "file does not exist: {0}", file.getAbsolutePath());
            return new String[0];
        }
        if (!file.canRead() || !Files.isReadable(file.toPath())) {
            LOG.log(Level.INFO, "no permission to read from file: {0}", file.getAbsolutePath());
            return new String[0];
        }
        try (final FileManager fileManager = new FileManager(file.getAbsolutePath()); BufferedReader br = fileManager.getBufferedReader()) {
            String line = br.readLine();
            if (line != null) {
                String[] sa = AbstractLine.splitLine(line, DELIMITER);
                return sa;
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        LOG.log(Level.WARNING, "file seems to be empty: {0}", file.getAbsolutePath());
        return new String[0];
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.fileName);
        hash = 41 * hash + Objects.hashCode(this.actualFile);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final P21File other = (P21File) obj;
        if (!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        if (!Objects.equals(this.actualFile, other.actualFile)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String fn = fileName;
        final int length = 12;
        while (fn.length() < length) {
            fn += " ";
        }
        return fn + " [" + (mandatory ? "required" : "optional") + "]: " + (!found ? "NOT FOUND" : "actually found as " + actualFile.getName());
    }

}

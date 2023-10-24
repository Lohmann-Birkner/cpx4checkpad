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
package transformer.impl.fdse.versionhelper;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author niemeier
 */
public class FdseFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    //public static final int MIN_SUPPORTED_YEAR = 2013;
    private final File fdseDirectory;
    private final HashMap<String, FdseFile> fdseFiles;

    public FdseFiles(final File pDirectory, final Map<String, FdseFile> pFdseFiles) {
        this.fdseDirectory = pDirectory;
        this.fdseFiles = pFdseFiles == null ? new HashMap<>() : new HashMap<>(pFdseFiles);
    }

    public File getFdseDirectory() {
        return fdseDirectory;
    }

    public Map<String, FdseFile> getFdseFiles() {
        return new HashMap<>(fdseFiles);
    }

    public FdseFile get(final String pMapKey) {
        return fdseFiles.get(pMapKey);
    }

    public Set<Map.Entry<String, FdseFile>> entrySet() {
        return fdseFiles.entrySet();
    }

    public Iterator<Map.Entry<String, FdseFile>> iterator() {
        return fdseFiles.entrySet().iterator();
    }

    public boolean isEmpty() {
        return fdseFiles.isEmpty();
    }

    public int size() {
        return fdseFiles.size();
    }

    public FdseFile getAufFile() {
        Optional<FdseFile> result = fdseFiles.values().stream().filter(FdseFile::isAufnahmeFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public FdseFile getEntgelteFile() {
        Optional<FdseFile> result = fdseFiles.values().stream().filter(FdseFile::isEntgelteFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public FdseFile getFabFile() {
        Optional<FdseFile> result = fdseFiles.values().stream().filter(FdseFile::isFabFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public FdseFile getFallFile() {
        Optional<FdseFile> result = fdseFiles.values().stream().filter(FdseFile::isFallFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public FdseFile getIcdFile() {
        Optional<FdseFile> result = fdseFiles.values().stream().filter(FdseFile::isIcdFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public FdseFile getOpsFile() {
        Optional<FdseFile> result = fdseFiles.values().stream().filter(FdseFile::isOpsFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    @Override
    public String toString() {
        return "P21 dataset contains " + size() + " files";
    }

}

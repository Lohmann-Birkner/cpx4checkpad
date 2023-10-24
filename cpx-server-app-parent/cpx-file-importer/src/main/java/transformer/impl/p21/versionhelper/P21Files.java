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

import de.lb.cpx.shared.p21util.P21Version;
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
public class P21Files implements Serializable {

    private static final long serialVersionUID = 1L;

    //public static final int MIN_SUPPORTED_YEAR = 2013;
    private final P21Version p21Version;
    private final File p21Directory;
    private final HashMap<String, P21File> p21Files;

    public P21Files(final P21Version p21Version, final File pDirectory, final Map<String, P21File> pP21Files) {
        this.p21Version = p21Version;
        this.p21Directory = pDirectory;
        this.p21Files = pP21Files == null ? new HashMap<>() : new HashMap<>(pP21Files);
    }

    public P21Version getP21Version() {
        return p21Version;
    }

    public File getP21Directory() {
        return p21Directory;
    }

    public Map<String, P21File> getP21Files() {
        return new HashMap<>(p21Files);
    }

    public String getP21VersionIdentifier() {
        return (p21Version == null) ? "" : p21Version.getVersionIdentifier();
    }

    public String getP21VersionName() {
        return (p21Version == null) ? "" : p21Version.name();
    }

    public int getP21Year() {
        return (p21Version == null) ? 0 : p21Version.getYear();
    }

    public P21File get(final String pMapKey) {
        return p21Files.get(pMapKey);
    }

    public Set<Map.Entry<String, P21File>> entrySet() {
        return p21Files.entrySet();
    }

    public Iterator<Map.Entry<String, P21File>> iterator() {
        return p21Files.entrySet().iterator();
    }

    public boolean isEmpty() {
        return p21Files.isEmpty();
    }

    public int size() {
        return p21Files.size();
    }

    public boolean isP21Supported() {
        return getP21Year() >= P21Version.MIN_SUPPORTED_IMPORT_YEAR;
    }

    public P21File getFallFile() {
        Optional<P21File> result = p21Files.values().stream().filter(P21File::isFallFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public P21File getFabFile() {
        Optional<P21File> result = p21Files.values().stream().filter(P21File::isFabFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public P21File getPatientFile() {
        Optional<P21File> result = p21Files.values().stream().filter(P21File::isPatientFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public P21File getEntgelteFile() {
        Optional<P21File> result = p21Files.values().stream().filter(P21File::isEntgelteFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public P21File getOpsFile() {
        Optional<P21File> result = p21Files.values().stream().filter(P21File::isOpsFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public P21File getIcdFile() {
        Optional<P21File> result = p21Files.values().stream().filter(P21File::isIcdFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public P21File getInfoFile() {
        Optional<P21File> result = p21Files.values().stream().filter(P21File::isInfoFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public P21File getFallZusatzFile() {
        Optional<P21File> result = p21Files.values().stream().filter(P21File::isFallZusatzFile).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    @Override
    public String toString() {
        return "P21 dataset for year " + getP21Year() + " contains " + size() + " files";
    }

}

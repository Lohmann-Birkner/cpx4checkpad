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
package transformer.impl.p21.versionhelper;

import de.lb.cpx.shared.p21util.P21Util;
import de.lb.cpx.shared.p21util.P21Version;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static transformer.AbstractCpxTransformer.findFilename;

/**
 * Hilfsklasse zur Ermittlung der verliegenden P21 Version beim Import
 *
 * @author Wilde
 */
public class P21VersionHelper {

    private static final Logger LOG = Logger.getLogger(P21VersionHelper.class.getName());

    public P21Version getDatasetVersion(final List<File> pFiles) throws IOException {
        //List<File> files = getDirectoryFiles(directory);
        File infoFile = findFilename(pFiles, "INFO.csv");
        if (infoFile != null && infoFile.exists()) {
            final P21Version version = getVersionByFileData(P21InfoFileReader.getVersionFromFile(infoFile));
            if (version != null) {
                return version;
            } else {
                LOG.log(Level.INFO, P21Util.FILENAME_P21INFO + " found, but was not able to detect P21 version from it");
            }
        } else {
            LOG.log(Level.INFO, P21Util.FILENAME_P21INFO + " not found");
        }
        return getVersionByColumnCount(pFiles);
    }

    protected Integer getFileColCount(final File file) {

        int count = 0;
        int tries = 0;
        if (file != null && file.exists() && file.isFile() && file.canRead()) {
            //try (FileReader r = new FileReader(file); BufferedReader b = new BufferedReader(r)) {
            try (InputStreamReader lInputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8"); BufferedReader b = new BufferedReader(lInputStreamReader)) {
                final int[] cnt = new int[2];
                /*
         * if(b.ready()) { b.readLine(); // erste Zeile ist bei P21 immer Header }
                 */
                while (b.ready() && tries < 2) {
                    final String l = b.readLine();
                    if (l != null && l.length() > 0) {
                        final String[] vals = l.split(";", -1);
                        cnt[tries] = vals.length;
                        tries++;
                    }
                }
                if (cnt[0] != 0
                        && cnt[0] == cnt[1] /* && cnt[0] == cnt[2] && cnt[0] == cnt[3] && cnt[0] == cnt[4] */) {
                    count = cnt[0];
                }
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Exception reading file column count", e);
            }
        }
        return count;
    }

    private P21Version getVersionByColumnCount(final List<File> pFiles) {
        final P21Version p21Version;
        final int cntFALL = getFileColCount(findFilename(pFiles, P21Util.FILENAME_P21FALL));
        final int cntFAB = getFileColCount(findFilename(pFiles, P21Util.FILENAME_P21FAB));
        final int cntOPS = getFileColCount(findFilename(pFiles, P21Util.FILENAME_P21OPS));
        final int cntENT = getFileColCount(findFilename(pFiles, P21Util.FILENAME_P21ENT));
        final int cntICD = getFileColCount(findFilename(pFiles, P21Util.FILENAME_P21ICD));

        if (cntFALL == 0) {
            p21Version = null;
        } else if (cntFAB == P21Util.COL_COUNT_FAB_2020) {
            p21Version = P21Version.P21V2020;
        } else if (cntFALL == P21Util.COL_COUNT_FALL_2019) {
            p21Version = P21Version.P21V2019;
        } else if (cntFALL == P21Util.COL_COUNT_FALL_2016) {
            p21Version = P21Version.P21V2016;
        } else if (cntFALL == P21Util.COL_COUNT_FALL_2014) {
            p21Version = P21Version.P21V2014;
        } else if (cntFALL == P21Util.COL_COUNT_FALL_2013) {
            p21Version = P21Version.P21V2013;
        } else if (cntFAB == P21Util.COL_COUNT_FAB_2004 && cntOPS == P21Util.COL_COUNT_OPS_2004) {
            p21Version = P21Version.P21V2004;
        } else if (cntFAB == P21Util.COL_COUNT_FAB_2005 && cntOPS == P21Util.COL_COUNT_OPS_2005 && cntENT == P21Util.COL_COUNT_ENTG_2008) {
            p21Version = P21Version.P21V2008;
        } else if (cntFAB == P21Util.COL_COUNT_FAB_2005 && cntOPS == P21Util.COL_COUNT_OPS_2005) {
            p21Version = P21Version.P21V2005;
        } else if (cntFAB == P21Util.COL_COUNT_FAB_2010 && cntOPS == P21Util.COL_COUNT_OPS_2010 && cntICD == P21Util.COL_COUNT_ICD_2011 && cntENT == P21Util.COL_COUNT_ENTG_2012) {
            p21Version = P21Version.P21V2012;
        } else if (cntFAB == P21Util.COL_COUNT_FAB_2010 && cntOPS == P21Util.COL_COUNT_OPS_2010 && cntICD == P21Util.COL_COUNT_ICD_2011) {
            p21Version = P21Version.P21V2011;
        } else if (cntFAB == P21Util.COL_COUNT_FAB_2010 && cntOPS == P21Util.COL_COUNT_OPS_2010) {
            p21Version = P21Version.P21V2010;
//        } else if (cntFAB == P21Util.COL_COUNT_FAB_2004 + 3 && cntOPS == P21Util.COL_COUNT_OPS_2004 + 5) {
//            p21Version = P21Version.P21V2004PLUS;
//        } else if (cntFAB == P21Util.COL_COUNT_FAB_2005 + 3 && cntOPS == P21Util.COL_COUNT_OPS_2005 + 5 && cntENT == P21Util.COL_COUNT_ENTG_2008) {
//            p21Version = P21Version.P21V2008PLUS;
//        } else if (cntFAB == P21Util.COL_COUNT_FAB_2005 + 3 && cntOPS == P21Util.COL_COUNT_OPS_2005 + 5) {
//            p21Version = P21Version.P21V2008PLUS;
//        } else if (cntFAB == P21Util.COL_COUNT_FAB_2010 + 3 && cntOPS == P21Util.COL_COUNT_OPS_2010 + 5 && cntICD == P21Util.COL_COUNT_ICD_2011 && cntENT == P21Util.COL_COUNT_ENTG_2012) {
//            p21Version = P21Version.P21V2012PLUS;
//        } else if (cntFAB == P21Util.COL_COUNT_FAB_2010 + 3 && cntOPS == P21Util.COL_COUNT_OPS_2010 + 5 && cntICD == P21Util.COL_COUNT_ICD_2011) {
//            p21Version = P21Version.P21V2011PLUS;
//        } else if (cntFAB == P21Util.COL_COUNT_FAB_2010 + 3 && cntOPS == P21Util.COL_COUNT_OPS_2010 + 5) {
//            p21Version = P21Version.P21V2010PLUS;
        } else {
            p21Version = null;
        }
        return p21Version;
    }

    private P21Version getVersionByFileData(final String pVersionFromFile) {
        final String versionFromFile = pVersionFromFile == null ? "" : pVersionFromFile.trim();

        if (versionFromFile.isEmpty()) {
            return null;
        }

        LOG.log(Level.FINEST, "Found version string from file: " + versionFromFile);

        for (P21Version version : P21Version.values()) {
            if (version.getVersionIdentifier().equalsIgnoreCase(versionFromFile)) {
                return version;
            }
        }

        LOG.log(Level.WARNING, "Illegal version string in file " + P21Util.FILENAME_P21INFO + ": " + versionFromFile);

        return null;
    }
}

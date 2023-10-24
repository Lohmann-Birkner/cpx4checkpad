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

import static de.lb.cpx.str.utils.StrUtils.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import line.AbstractLine;
import static transformer.impl.P21ToCpxTransformer.DELIMITER;
import util.FileManager;

/**
 *
 * @author Wilde
 */
public class P21InfoFileReader {

//    private static final String P21_COLUMN_VERSION = "Versionskennung";
    private static final Logger LOG = Logger.getLogger(P21InfoFileReader.class.getName());

    public static String getVersionFromFile(final File pInfoFile) throws IOException {
        String versionskennung = "";
        if (pInfoFile == null) {
            LOG.log(Level.INFO, "No info file found");
            versionskennung = "";
        } else {
            try (FileManager fileManager = new FileManager(pInfoFile.getAbsolutePath())) {
                try (BufferedReader br = fileManager.getBufferedReader()) {
                    String line = br.readLine(); //skip first line (headline)
                    while ((line = br.readLine()) != null) {
                        String[] sa = AbstractLine.splitLine(line, DELIMITER);
                        int lastColumn = sa.length - 1;
                        versionskennung = toStr(sa[lastColumn]);
                        if (versionskennung.isEmpty() && lastColumn > 1) {
                            //sometimes there's an additional semicolon at the end of info.csv file
                            versionskennung = toStr(sa[lastColumn - 1]);
                        }
                    }
                }
            }
        }
        return versionskennung;
    }

}

/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.reader;

import de.lb.cpx.reader.util.Document;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String inputDirectory = args.length > 0 ? args[0].trim() : "";
        final String outputDirectory = args.length > 1 ? args[1].trim() : "";
        final long startTime = System.currentTimeMillis();
        final boolean includeSubDirs = true;
        //batchExport(inputDirectory, outputDirectory, includeSubDirs);
        final Set<Document> result;
        if (outputDirectory.isEmpty()) {
            result = DocumentReader.getText(inputDirectory, includeSubDirs);
        } else {
            result = DocumentReader.getText(inputDirectory, outputDirectory, includeSubDirs);
        }
        LOG.log(Level.INFO, "Extracted " + result.size() + " documents");
        LOG.log(Level.INFO, "Finished after " + (System.currentTimeMillis() - startTime) + " ms");
        for (Document doc : result) {
            LOG.log(Level.INFO, "Datei: " + doc.getFile().getAbsolutePath());
            LOG.log(Level.INFO, "Erkannte Fallnummer: " + doc.getCaseNumber());
            LOG.log(Level.INFO, "Text: " + doc.getText());
            LOG.log(Level.INFO, "================================================");
        }
    }

}

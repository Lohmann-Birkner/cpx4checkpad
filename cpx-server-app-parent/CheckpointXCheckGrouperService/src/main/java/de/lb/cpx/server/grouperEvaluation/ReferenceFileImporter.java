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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.grouperEvaluation;

import de.lb.cpx.grouper.model.transfer.EvaluationCaseTransfer;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class ReferenceFileImporter {

    private static final Logger LOG = Logger.getLogger(ReferenceFileImporter.class.getName());

    private HashMap<String, EvaluationCaseTransfer> referenceCases = new HashMap<>();
    private BufferedReader bufferedReader;

    /**
     * import the reference file for grouper
     *
     * @param path path
     * @param isLocal local or external cases?
     * @return Set of keys to the imported Strings
     */
    public Set<String> importReferenceCase(String path, boolean isLocal) {
        referenceCases = new HashMap<>();
        final File input = new File(path);
        if (!input.exists() || !input.isFile()) {
            LOG.log(Level.INFO, "file not found: " + path);
            return null;
        }
        if (!input.canRead()) {
            LOG.log(Level.INFO, "file not is not readable: " + path);
            return null;
        }

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), CpxSystemProperties.DEFAULT_ENCODING));
            return readReferenceCase(bufferedReader, isLocal);
        } catch (IOException e) {
            LOG.log(Level.INFO, "can't read file " + path, e);
            return null;
        }

    }

    private Set<String> readReferenceCase(BufferedReader bufferedReader, boolean isLocal) throws IOException {

        try (bufferedReader) {
            String str;

            while ((str = bufferedReader.readLine()) != null) {
                EvaluationCaseTransfer cs = new EvaluationCaseTransfer(str, isLocal);
                if (cs.getUniqueKey() != null) {
                    referenceCases.put(cs.getUniqueKey(), cs);

                } else {
                    LOG.info("String " + str + " cannot be checked");
                }
            }
            return referenceCases.keySet();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error on reading file", ex);
        }
        return null;
    }

    public EvaluationCaseTransfer get2key(String key) {
        return referenceCases.get(key);
    }
}

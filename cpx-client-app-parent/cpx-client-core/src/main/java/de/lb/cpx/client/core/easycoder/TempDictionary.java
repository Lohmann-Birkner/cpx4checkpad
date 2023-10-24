/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2018  Halabieh - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.easycoder;

import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author halabieh
 */
public class TempDictionary {

    private static final Logger LOG = Logger.getLogger(TempDictionary.class.getName());

    /**
     * create temp dictionary
     *
     * @param text text
     *
     */
    public void setTempDictionary(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();

//        String outPath = cpxProps.getCpxClientDictionariesDir() + "\\temp\\Temp-Dictionary.txt";
        String outPath = cpxProps.getAppDataLocal() + "\\dictionaries\\indexes\\Temp\\Temp-Dictionary.txt";

        Set<String> lines = new LinkedHashSet<>();
        try ( PrintWriter out = new PrintWriter(outPath, "UTF8")) {
            //setting every word in a line
            String[] words = text.replaceAll("[^a-zA-ZäÄüÜöÖß]+", " ").split("\\s+");
            for (String word : words) {

                if (word.length() <= 3) {
                    continue;
                }

                if (lines.contains(word)) {
                    continue;
                }
                lines.add(word);
                out.println(word.toLowerCase());
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "writer Error: " + ex.getMessage(), ex);
        }

    }
}

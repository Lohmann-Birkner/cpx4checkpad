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
package de.lb.cpx.client.app.wm.util.doctemplate;

import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author nandola
 */
public class CheckDocVersion {

    /**
     * It will check the installation of MS Word Office, If it is not installed
     * it will return false and program won't run
     *
     * @return is word installed?
     * @throws IOException I/O-Exception
     */
    public boolean wordInstallation() throws IOException {
        // It will check the installation by a windows run command !
        Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "assoc", ".doc"});
        String extensionType;
        try ( BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream(), CpxSystemProperties.DEFAULT_ENCODING))) {
            extensionType = input.readLine();
        }
        if (extensionType == null) {
            //System.exit(1);
            return false;
        }
        return true;
    }

    /**
     * This method will check the version of Ms Word
     *
     * @return // will return format of word .docx or .doc
     * @throws IOException I/O-Exception
     */
    public String checkWordVersion() throws IOException {
        Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "assoc", ".docx"});
        String extensionType;
        try ( BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream(), CpxSystemProperties.DEFAULT_ENCODING))) {
            extensionType = input.readLine();
        }
        if (extensionType == null) {
            return ".doc";
        }
        return ".docx";
    }
}

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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;

/**
 *
 * @author Dirk Niemeier
 */
@Singleton(name = "ResourceBundleEJB")
public class ResourceBundleEJB implements ResourceBundleEJBRemote {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(ResourceBundleEJB.class.getName());

    @Override
    public String getResourceBundle(String pLocale) {
        String locale = (pLocale == null) ? "" : pLocale.trim().toLowerCase();
        if (locale.isEmpty()) {
            locale = "de";
        }
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        File file = new File(cpxProps.getCpxServerLocaleDir() + locale + ".properties");
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        try ( BufferedReader reader
                = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
            //props.load(reader);    
            String line;
            while ((line = reader.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append(newLine);
                }
                sb.append(line);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return sb.toString();
    }

}

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
package de.checkpoint.server.appServer;

import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * will be used by CPX instead of correspondent class of Checkpoint to hide the
 * inappropriated functionality
 *
 * @author gerschmann
 */
public class AppResources {

    protected static final String LOCAL_RESOURCE_NAME = "de.checkpoint.server.appServer.AppResourceBundle";
    private static ResourceBundle m_localResourceBundle = null;
    private static final Logger LOG = Logger.getLogger(AppResources.class.getName());

    public static String getResource(String key) {
        return getResource(key, null);
    }

    public static String getResource(String key, String defaultText) {
        String ret = defaultText;
        try {
            ResourceBundle rb = getLocalResourceBundle();
            if (rb == null) {
                LOG.log(Level.SEVERE, "ResourceBundle is null!");
                return ret;
            }
            ret = rb.getString(key);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static synchronized String getResourcePath() {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
//      if (cpxProps != null) {
//        try {
        String language = cpxProps.getUserLanguage();
        if (language == null || language.trim().isEmpty()) {
            language = "DE";
        }
        //country = cpxProps.getUserRegion();
//        } catch (Exception ex) {
//          LOG.log(Level.WARNING, "language is not found, DE will be used", ex);
//          language = "DE";
//        }
        //InputStream is = null;
        //Pna: 18.05.18: If system language is English, it will read properties from resources_en file (But,this file doesn't contain all properties.)
//            String resourceFilename = "resources_" + language.toLowerCase() + ".properties";
        String resourceFilename = "resources_" + "de" + ".properties";
        String resourcePath = cpxProps.getCpxServerResourceBundlesDir();

        String path = resourcePath + resourceFilename;
        return path;
    }

    static synchronized ResourceBundle getLocalResourceBundle() throws IOException {
        if (m_localResourceBundle == null) {
            //String country;String
            String path = getResourcePath();
            File f = new File(path);
            if (f.exists()) {
                try ( InputStream is = new FileInputStream(f)) {
                    m_localResourceBundle = new AppPropertyBundle(is);
                } catch (FileNotFoundException ex) {
                    LOG.log(Level.SEVERE, "was not able to load file " + path, ex);
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "was not able to load file " + path, ex);
                    throw new IOException(ex.getMessage(), ex);
                }
            } else {
                LOG.log(Level.WARNING, "resource file " + path + " not found");
            }
//      } else {
//        try {
//          m_localResourceBundle = ResourceBundle.getBundle(LOCAL_RESOURCE_NAME,
//                  Locale.getDefault());
//        } catch (Exception e) {
//          LOG.log(Level.WARNING, "resource  " + LOCAL_RESOURCE_NAME + " not found", e);
//        }
//      }
        }
        return m_localResourceBundle;
    }

    public static String getServerResource(final String key) {
        return "";
    }
}

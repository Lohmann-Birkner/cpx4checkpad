/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.properties;

import de.lb.cpx.db.properties.reader.DbImportConfig;
import de.lb.cpx.db.properties.reader.LaborImportConfig;
import java.io.File;
import java.nio.file.FileSystemException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class LaborProperties {

    public static final String PROPERTIES_NAME = "labor";
    private static final Logger LOG = Logger.getLogger(LaborProperties.class.getName());
    private Map<String, String> mLaborGroupHash = new HashMap<>();
    private LaborImportConfig mImportConfig = new LaborImportConfig();
    public LaborProperties() {
        try{
            mImportConfig.getXmlConfigFile();
        }catch(Exception ex){
            LOG.log(Level.SEVERE, " couldnot open import config file, try to create new one", ex);
            try{
                mImportConfig.createConfigFile();
            }catch(FileSystemException ex1){
                LOG.log(Level.SEVERE, "could not create properties file", ex1);
            }
            
        }
        mLaborGroupHash = mImportConfig.getLaborGroupHash();
    }

    /**
     * @return the mLaborGroupHash
     */
    public Map<String, String> getLaborGroupHash() {
        return Collections.unmodifiableMap(mLaborGroupHash);
    }

//    public String getLaborGroupHash(final String pKey) {
//        return mLaborGroupHash.get(pKey);
//    }
    public String get(final String pKey) {
        return mLaborGroupHash.get(pKey);
    }

}

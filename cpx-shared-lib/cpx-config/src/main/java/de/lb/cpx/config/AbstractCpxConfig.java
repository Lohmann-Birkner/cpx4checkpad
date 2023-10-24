/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.config;

import de.lb.cpx.app.crypter.PasswordDecrypter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.apache.commons.configuration2.Configuration;

/**
 *
 * @author Dirk Niemeier
 */
//@SecurityDomain("cpx")
public abstract class AbstractCpxConfig implements CpxConfigLocal {

    private static final Logger LOG = Logger.getLogger(AbstractCpxConfig.class.getName());
    public static final String DEFAULT_ENCODING = "Cp1252";

    public static String encryptPassword(final String pPlainPassword) {
        String password = (pPlainPassword == null ? "" : pPlainPassword.trim());
        return PasswordDecrypter.getInstance().encrypt2(password);
    }

    public static String decryptPassword(final String pEncodedPassword) {
        String password = (pEncodedPassword == null ? "" : pEncodedPassword.trim());
        try {
            return PasswordDecrypter.getInstance().decrypt2(password);
        } catch (SecurityException ex) {
            //If password is already plaintext then this exception branch will be executed
            LOG.log(Level.FINER, "Was not able to decode password", ex);
            return password;
        }
    }
//
//    public static boolean isPasswordEncoded(final String pPasswordCandidate) {
//        final String candidate = (pPasswordCandidate == null) ? "" : pPasswordCandidate.trim();
//        String decodedCandidate = decodePassword(candidate);
//        return !(candidate != null && candidate.equalsIgnoreCase(decodedCandidate));
//    }

    public static boolean isPasswordEncrypted(final String pPasswordCandidate) {
        return PasswordDecrypter.getInstance().isEncrypted(pPasswordCandidate);
    }

    public static boolean isPasswordDecrypted(final String pPasswordCandidate) {
        return PasswordDecrypter.getInstance().isDecrypted(pPasswordCandidate);
    }

    //Creates an empty XML configuration file if it doesn't exist yet
    protected static synchronized boolean createXmlConfigFile(final String pFileName) throws FileSystemException {
        if (pFileName == null || pFileName.trim().isEmpty()) {
            return true;
        }
        final File pFile = new File(pFileName);
        if (pFile.exists() && pFile.isFile()) {
            return true;
        }
        if (pFile.setExecutable(false)) {
            LOG.log(Level.FINEST, "Was successfully set to unexecutable: " + pFile.getName());
        }
        if (pFile.setReadable(true)) {
            LOG.log(Level.FINEST, "Was successfully set to readable: " + pFile.getName());
        }
        if (pFile.setWritable(true)) {
            LOG.log(Level.FINEST, "Was successfully set to writeable: " + pFile.getName());
        }
        if (!pFile.getParentFile().exists() && !pFile.getParentFile().mkdirs()) {
            throw new FileSystemException("Was not able to create directory '" + pFile.getParentFile().getAbsolutePath() + "'");
        }
        try {
            final boolean created = pFile.createNewFile();
            if (!created) {
                LOG.log(Level.WARNING, "Was not able to create file: " + pFile.getAbsolutePath());
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
        try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pFile), DEFAULT_ENCODING))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            writer.write("<configuration>");
            writer.write("</configuration>");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            try {
                Files.delete(pFile.toPath());
                LOG.log(Level.INFO, "deleted file: {0}", pFile.getAbsolutePath());
            } catch (IOException ex1) {
                LOG.log(Level.WARNING, "was not able to delete file: " + pFile.getAbsolutePath(), ex1);
            }
//            if (!pFile.delete()) {
//                LOG.log(Level.WARNING, "Was not able to delete file '" + pFile.getAbsolutePath() + "'", ex);
//            }
            return false;
        }
        return true;
    }

    /*
  public ExpressionEngine getExpressionEngine() {
  DefaultExpressionEngine engine = new DefaultExpressionEngine(
  DefaultExpressionEngineSymbols.DEFAULT_SYMBOLS,
  NodeNameMatchers.EQUALS_IGNORE_CASE);
  return engine;
  }
     */
    @PostConstruct
    public void init() {
        getConfigs();
    }

    protected String toStr(final String pValue) {
        return (pValue == null) ? "" : pValue.trim();
    }

    protected String toStr(final String pValue, String pDefault) {
        String value = toStr(pValue);
        if (value.isEmpty() && pDefault != null) {
            value = pDefault;
        }
        return value;
    }

    protected boolean toBool(final String pValue) {
        String value = toStr(pValue).toLowerCase();
        if (value.isEmpty()) {
            return false;
        }
        if ("t".equalsIgnoreCase(value)) {
            return true;
        }
        if ("1".equalsIgnoreCase(value)) {
            return true;
        }
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("on".equalsIgnoreCase(value)) {
            return true;
        }
        if ("enabled".equalsIgnoreCase(value)) {
            return true;
        }
        if ("enable".equalsIgnoreCase(value)) {
            return true;
        }
        if ("wahr".equalsIgnoreCase(value)) {
            return true;
        }
        if ("ja".equalsIgnoreCase(value)) {
            return true;
        }
        if ("yes".equalsIgnoreCase(value)) {
            return true;
        }
        if ("j".equalsIgnoreCase(value)) {
            return true;
        }
        if ("y".equalsIgnoreCase(value)) {
            return true;
        }
        return false;
    }

    protected Integer toInt(final String pValue) {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return null;
        }
        Integer lValue = null;
        try {
            lValue = Integer.valueOf(value);
        } catch (NumberFormatException lEx) {
            //Not a valid integer
            lValue = null;
        }
        return lValue;
    }

    protected Long toLong(final String pValue) {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return null;
        }
        Long lValue = null;
        try {
            lValue = Long.valueOf(value);
        } catch (NumberFormatException lEx) {
            //Not a valid longeger
            lValue = null;
        }
        return lValue;
    }

    protected Double toDouble(final String pValue) {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return null;
        }
        Double lValue = null;
        try {
            lValue = Double.valueOf(value);
        } catch (NumberFormatException lEx) {
            //Not a valid double
            lValue = null;
        }
        return lValue;
    }

    public void clearProperty(final Configuration config, final String pKey) {
        config.clearProperty(toStr(pKey));
    }

    public String getString(final Configuration config, final String pKey) {
        return getString(config, pKey, "");
    }

    public String getString(final Configuration config, final String pKey, final String pDefault) {
        return toStr(config.getString(toStr(pKey), pDefault));
    }

    public List<?> getList(final Configuration config, final String pKey, final List<?> pList) {
        return config.getList(pKey, pList);
        //return config.getCollection(pCollectionType, pKey, pCollection);
        //return toStr(config.getString(toStr(pKey), pDefault));
    }

//    public void setObject(final Configuration config, final String pKey, final Serializable pObject) {
//        config.setProperty(pKey, pObject);
//    }
    public void setString(final Configuration config, final String pKey, final String pValue) {
        config.setProperty(toStr(pKey), toStr(pValue));
        /*
    String key = toStr(pKey);
    if (toStr(pValue).isEmpty()) {
      config.clearProperty(key);
    } else {
      config.setProperty(key, toStr(pValue));
    }
         */
    }

    public void setEnum(final Configuration config, final String pKey, final Enum<?> pValue) {
        config.setProperty(toStr(pKey), pValue == null ? "" : toStr(pValue.name()));
        /*
    String key = toStr(pKey);
    if (toStr(pValue).isEmpty()) {
      config.clearProperty(key);
    } else {
      config.setProperty(key, toStr(pValue));
    }
         */
    }

    public boolean getBoolean(final Configuration config, final String pKey) {
        return getBoolean(config, pKey, false);
    }

    public Boolean getBoolean(final Configuration config, final String pKey, final Boolean pDefault) {
        String value = getString(config, pKey);
        if (value.isEmpty()) {
            return pDefault;
        }
        return toBool(value);
    }

    public <T extends Enum<?>> T getEnum(final Configuration config, final String pKey, final T[] pEnumValues) {
        T def = null;
        return getEnum(config, pKey, pEnumValues, def);
    }

    public <T extends Enum<?>> T getEnum(final Configuration config, final String pKey, final T[] pEnumValues, final T pDefault) {
        String value = getString(config, pKey);
        if (value.isEmpty()) {
            return pDefault;
        }
        if (pEnumValues == null) {
            LOG.log(Level.SEVERE, "No enum values passed!");
        } else {
            for (T flag : pEnumValues) {
                if (flag == null) {
                    continue;
                }
                if (value.equalsIgnoreCase(flag.name())) {
                    return flag;
                }
            }
            LOG.log(Level.WARNING, "unknown enum value: {0}", value);
        }
        return pDefault;
    }

    public void setBoolean(final Configuration config, final String pKey, final Boolean pValue) {
        config.setProperty(toStr(pKey), (pValue == null) ? "" : pValue ? "true" : "false");
    }

    public int getInt(final Configuration config, final String pKey) {
        return getInt(config, pKey, 0);
    }

    public Integer getInt(final Configuration config, final String pKey, final Integer pDefault) {
        Integer value = toInt(getString(config, pKey));
        if (value == null) {
            value = pDefault;
        }
        return value;
    }

    public void setInt(final Configuration config, final String pKey, final Integer pValue) {
        config.setProperty(toStr(pKey), (pValue == null) ? "" : String.valueOf(pValue));
    }

    public long getLong(final Configuration config, final String pKey) {
        return getLong(config, pKey, 0L);
    }

    public Long getLong(final Configuration config, final String pKey, final Long pDefault) {
        Long value = toLong(getString(config, pKey));
        if (value == null) {
            value = pDefault;
        }
        return value;
    }

    public void setLong(final Configuration config, final String pKey, final Long pValue) {
        config.setProperty(toStr(pKey), (pValue == null) ? "" : String.valueOf(pValue));
    }

    public double getDouble(final Configuration config, final String pKey) {
        return getDouble(config, pKey, 0d);
    }

    public Double getDouble(final Configuration config, final String pKey, final Double pDefault) {
        Double value = toDouble(getString(config, pKey));
        if (value == null) {
            value = pDefault;
        }
        return value;
    }

    public void setDouble(final Configuration config, final String pKey, final Double pValue) {
        config.setProperty(toStr(pKey), (pValue == null) ? "" : String.valueOf(pValue));
    }

}

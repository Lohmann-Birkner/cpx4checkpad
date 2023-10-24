/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import line.AbstractLine;
import line.LineEntity;
import line.LineI;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxReader implements AutoCloseable {

    private static CpxReader sInstance = null;
    private static final Logger LOG = Logger.getLogger(CpxReader.class.getName());

    public static synchronized CpxReader getInstance() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        return getInstance(null);
    }

    public static synchronized CpxReader getInstance(final String pDirectory) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        //if (sInstance == null) {
        sInstance = new CpxReader(pDirectory);
        //}
        return sInstance;
    }
    private final Map<String, BufferedReader> mFileReader;
    //private final String mDirectory;

    protected CpxReader(final String pDirectory) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        String directory = (pDirectory == null) ? "" : pDirectory.trim();
        if (directory.isEmpty()) {
            throw new IllegalArgumentException("No output directory given!");
        }
        File file = new File(directory + File.separator);
        if (file.exists() && !file.isDirectory()) {
            throw new IllegalArgumentException("Path already exists as a file: " + file.getAbsolutePath());
        }
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IllegalArgumentException("Was not able to create directory: " + file.getAbsolutePath());
            }
        }
        directory = file.getAbsolutePath();
        final Map<String, BufferedReader> mapTmp = new HashMap<>();
        for (LineEntity lineEntity : AbstractLine.getLineEntities()) {
            try (FileManager fileManager = new FileManager(directory + File.separator + lineEntity.imexTmpFileName)) {
                BufferedReader br = fileManager.getBufferedReader();
                mapTmp.put(lineEntity.clazzName, br);
            }
        }
        //mFileManager = Collections.unmodifiableMap(mapTmp);
        mFileReader = mapTmp;
        //mDirectory = directory;
    }

    public BufferedReader getReader(final LineEntity pLineEntity) {
        if (pLineEntity == null) {
            throw new IllegalArgumentException("No line entity given!");
        }
        return getReader(pLineEntity.clazzName);
    }

    public BufferedReader getReader(final Class<? extends LineI> pLineClass) {
        if (pLineClass == null) {
            throw new IllegalArgumentException("No line class given!");
        }
        return getReader(pLineClass.getSimpleName());
    }

    public BufferedReader getReader(final String pLineClassName) {
        String lineClassName = (pLineClassName == null) ? "" : pLineClassName.trim();
        if (lineClassName.isEmpty()) {
            throw new IllegalArgumentException("No line class name given!");
        }
        BufferedReader br = mFileReader.get(lineClassName);
        if (br == null) {
            throw new IllegalArgumentException("Invalid line class name given: " + lineClassName);
        }
        return br;
    }

    public String read(final LineEntity pLineEntity, final String pLine) throws IOException {
        if (pLineEntity == null) {
            throw new IllegalArgumentException("No line entity given!");
        }
        return read(pLineEntity.clazzName);
    }

    public String read(final Class<? extends LineI> pLineClass, final String pLine) throws IOException {
        if (pLineClass == null) {
            throw new IllegalArgumentException("No line class given!");
        }
        return read(pLineClass.getSimpleName());
    }

    public String read(final LineI pLine) throws IOException {
        if (pLine == null) {
            throw new IllegalArgumentException("line is null!");
        }
        String lineClassName = pLine.getClass().getName();
        return read(lineClassName);
    }

    public String read(final String pLineClassName) throws IOException {
        BufferedReader br = getReader(pLineClassName);
        return br.readLine();
    }

    @Override
    public void close() throws IOException {
        for (Map.Entry<String, BufferedReader> lEntry : mFileReader.entrySet()) {
            String key = lEntry.getKey();
            BufferedReader br = lEntry.getValue();
            if (br != null) {
                br.close();
                mFileReader.put(key, null);
            }
        }
    }

}

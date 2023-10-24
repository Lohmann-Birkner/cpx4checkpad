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
 *    2020  Lars Urbach - initial API and implementation and/or initial documentation
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author urbach
 */
public class CPxReaderSinglefile implements AutoCloseable {

    private static CPxReaderSinglefile sInstance = null;
    private static final Logger LOG = Logger.getLogger(CpxReader.class.getName());

    public static synchronized CPxReaderSinglefile getInstance() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        return getInstance(null);
    }

    public static synchronized CPxReaderSinglefile getInstance(final String pDirectory) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        //if (sInstance == null) {
        sInstance = new CPxReaderSinglefile(pDirectory);
        //}
        return sInstance;
    }
    private BufferedReader mFileReader;
    //private final String mDirectory;

    protected CPxReaderSinglefile(final String pFile) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        String inputFile = (pFile == null) ? "" : pFile.trim();
        if (inputFile.isEmpty()) {
            throw new IllegalArgumentException("No input File given!");
        }
        File file = new File(inputFile);
        if (file.exists() && !file.isFile()) {
            throw new IllegalArgumentException("Path already exists not as a file: " + file.getAbsolutePath());
        }
        if (!file.exists()) {
                throw new IllegalArgumentException("File not exists: " + file.getAbsolutePath());
        }
        try (FileManager fileManager = new FileManager(inputFile)) {
                BufferedReader br = fileManager.getBufferedReader();
                mFileReader = br;
        }
        //mFileManager = Collections.unmodifiableMap(mapTmp);
        //mDirectory = directory;
    }

    public BufferedReader getReader() {
        if (mFileReader == null) {
            throw new IllegalArgumentException("Invalid Bufferreader");
        }
        return mFileReader;
    }

    public String read() throws IOException {
        BufferedReader br = getReader();
        return br.readLine();
    }

    @Override
    public void close() throws IOException {
        if (mFileReader != null) {
            mFileReader.close();
            mFileReader = null;
        }
    }

    
}

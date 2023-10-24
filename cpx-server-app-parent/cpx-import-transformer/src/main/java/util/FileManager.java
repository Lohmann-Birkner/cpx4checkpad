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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(FileManager.class.getName());
    private static final String ENCODING = "Cp1252";

    protected String mFilename = "";
    protected File mFile = null;
    protected BufferedWriter mWriter = null;

    public FileManager(String pFilename) {
        mFilename = pFilename;
        mFile = new File(mFilename);
    }

    public File getFile() {
        return mFile;
    }

    public String getFilename() {
        return mFilename;
    }

    public void writeFile(String pLine) throws IOException {
        openFile();
        mWriter.write(pLine + "\r\n");
        //mWriter.newLine();
        //mWriter.flush();
    }

    public synchronized void openFile() throws IOException {
        if (mWriter == null) {
            OutputStreamWriter lOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(getFile()), ENCODING);
            mWriter = new BufferedWriter(lOutputStreamWriter);
        }
    }

    public synchronized void closeFile() {
        if (mWriter != null) {
            try {
                mWriter.close();
                mWriter = null;
            } catch (IOException lEx) {
                //System.err.println(lEx);
                LOG.log(Level.WARNING, "Was not able to close writer", lEx);
            }
        }
    }

    public synchronized void flush() {
        if (mWriter != null) {
            try {
                mWriter.flush();
            } catch (IOException lEx) {
                //System.err.println(lEx);
                LOG.log(Level.WARNING, "Was not able to flush writer", lEx);
            }
        }
    }

    public BufferedReader getBufferedReader() throws FileNotFoundException, UnsupportedEncodingException {
        InputStreamReader lInputStreamReader = new InputStreamReader(new FileInputStream(getFile()), ENCODING);

        BufferedReader lReader = new BufferedReader(lInputStreamReader);
        return lReader;
    }

    @Override
    public void close() {
        closeFile();
    }

}

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
package de.lb.cpx.sap.importer.utils;

import com.google.gson.stream.JsonWriter;
import de.lb.cpx.sap.container.FallContainer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class JsonDumpFileWriter extends JsonDumpFileManager {

    private static final Logger LOG = Logger.getLogger(JsonDumpFileWriter.class.getName());
    private JsonWriter mWriter = null;
    //private int mZaehler = 0;

    /**
     *
     * @param pFilename file name
     */
    public JsonDumpFileWriter(String pFilename) {
        super(pFilename, false);
    }

    /**
     *
     * @throws IOException cannot open file
     */
    @Override
    public synchronized void openFile() throws IOException {
        //Cp1252, windows-1252
        if (mWriter == null) {
            mWriter = new JsonWriter(new OutputStreamWriter(new FileOutputStream(getFile()), "Cp1252"));
            //if (!Iskv21cDownloaderProperties.getInstance().getCompactDump()) {
            mWriter.setIndent("  ");
            //}
            //mWriter.write("[");
        }
    }

    /**
     *
     * @param pFallContainer case container
     * @throws IOException cannot write file
     */
    public synchronized void writeFile(FallContainer pFallContainer) throws IOException {
        if (pFallContainer == null) {
            return;
        }
        openFile();

        getGson().toJson(pFallContainer, FallContainer.class, mWriter);
        mWriter.flush();
    }

    /**
     *
     */
    @Override
    public synchronized void closeFile() {
        if (mWriter != null) {
            try {
                //mWriter.write("]");
                mWriter.close();
            } catch (IOException lEx) {
                LOG.log(Level.SEVERE, lEx.getMessage(), lEx);
            }
        }
    }

}

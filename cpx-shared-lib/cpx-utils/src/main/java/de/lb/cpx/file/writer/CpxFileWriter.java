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
package de.lb.cpx.file.writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple String File Writer. Default Path is in the Source-Path, Default Char
 * encoding is UTF-8
 *
 * @author wilde
 */
public class CpxFileWriter {

    private static final Logger LOG = Logger.getLogger(CpxFileWriter.class.getSimpleName());
    private final StringBuilder content = new StringBuilder();

    /**
     * create new File Writer with the Content as String that should be written
     * in the File
     *
     * @param content String content to be written
     */
    public CpxFileWriter(String content) {
        this.content.append(content);
    }

    public CpxFileWriter() {

    }

    /**
     * Write file with given Name, in the default location and encoded with
     * UTF-8
     *
     * @param fileName Filename
     */
    public void write(String fileName) {
        write("", fileName, StandardCharsets.UTF_8);
    }

    /**
     * Write file with given Name, in the specified location with choosen
     * Charset-Encoding
     *
     * @param filePath path to write file to
     * @param fileName name of the File
     * @param charset choosen Encoding Charset
     */
    public void write(String filePath, String fileName, Charset charset) {
        try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(buildFilePath(filePath, fileName)), charset))) {
            writer.write(content.toString());
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Can not write: +\n" + content.toString() + "\n Reason: ", ex);
        }
    }

    private String buildFilePath(String filePath, String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        if (filePath == null || filePath.isEmpty()) {
            return fileName;
        }

        return filePath + "/" + fileName;
    }

    /**
     * adds a string to content
     *
     * @param str value
     */
    public void add2Content(String str) {
        this.content.append(str);
    }

}

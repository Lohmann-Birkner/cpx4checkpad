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
package de.lb.cpx.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxFileReader implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(CpxFileReader.class.getName());
    public static final String DEFAULT_ENCODING = "Cp1252";
    protected File file = null;
    protected BufferedReader reader = null;
    private boolean eof = true;
    private Integer size = null;
    private String last_line = "";

    public CpxFileReader(final File pFile) throws FileNotFoundException, UnsupportedEncodingException {
        this(pFile, DEFAULT_ENCODING);
    }

    public CpxFileReader(final File pFile, final String pEncoding) throws FileNotFoundException, UnsupportedEncodingException {
        if (pFile == null) {
            throw new IllegalArgumentException("File cannot be null!");
        }
        file = pFile;
        BufferedReader newReader = new BufferedReader(new InputStreamReader(new FileInputStream(pFile), pEncoding));
        reader = newReader;
        eof = false;
    }

    public Map<String, Integer> getHeaderMappings() throws IOException {
        return getHeaderMappings(true);
    }

    public Map<String, Integer> getHeaderMappings(final boolean pNormalizeKeys) throws IOException {
        final Map<String, Integer> mappingColumnNameToIndex = new LinkedHashMap<>();
        try ( BufferedReader newReader = getReaderCopy()) {
            if (newReader == null) {
                return mappingColumnNameToIndex;
            }
            String line = newReader.readLine();
            if (line == null) {
                LOG.log(Level.WARNING, "This file has no rows, so I cannot detect mappings of column names to indexes: " + file.getAbsolutePath());
                return mappingColumnNameToIndex;
            }
            //String[] arr = splitRespectingQuotes(";", line);
            //String[] arr = split(";", line);
            String[] arr = line.trim().split(";", -1);
            for (int i = 0; i < arr.length; i++) {
                String key = arr[i].trim();
                if (pNormalizeKeys) {
                    key = normalizeKey(key);
                }
                if (key.isEmpty()) {
                    LOG.log(Level.WARNING, "Empty column header name found! I'll ignore this column!");
                    continue;
                }
                mappingColumnNameToIndex.put(key, i);
            }
        }
        return mappingColumnNameToIndex;
    }

    /**
     * Transforms a value (header name) into a representation that ignores case
     * sensitivity and multiple blanks and special characters
     *
     * @param pValue value to be normalized
     * @return normalized value
     */
    public static String normalizeKey(final String pValue) {
        String value = pValue == null ? "" : pValue.trim();
        value = value.toLowerCase();
        StringBuilder sb = new StringBuilder(value);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(0);
            if (c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
                //Nothing
            } else {
                sb.setCharAt(i, ' ');
            }
        }
        value = sb.toString();
        String oldValue;
        do {
            oldValue = value;
            value = value.replace(" ", "_");
        } while (!oldValue.equalsIgnoreCase(value));
        return value;
    }

    public synchronized BufferedReader getReaderCopy() throws FileNotFoundException {
        if (reader == null) {
            return reader;
        }
        if (file == null) {
            return reader;
        }
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CpxFileReader.class.getName()).log(Level.SEVERE, "Wrong encoding for file " + file.getAbsolutePath() + "?", ex);
            return null;
        }
    }

    public synchronized int getLineCount() throws IOException {
        if (this.size != null) {
            return this.size;
        }
        int sizeTmp = 0;
        try ( BufferedReader newReader = getReaderCopy()) {
            if (newReader == null) {
                return sizeTmp;
            }
            String line = newReader.readLine();

            while (line != null) {
                if (!line.trim().isEmpty()) {
                    sizeTmp++;
                }
                line = newReader.readLine();
            }

            this.size = sizeTmp;
        }
        return Objects.requireNonNullElse(this.size, 0);
    }

    public synchronized File getFile() {
        return file;
    }

    public synchronized BufferedReader getReader() {
        return reader;
    }

    public synchronized String getFilename() {
        if (file == null) {
            return "";
        }
        return file.getName();
    }

    public synchronized String readLine() throws IOException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader was not instantiated!");
        }
        if (eof) {
            this.last_line = "";
            return this.last_line;
        }
        String line = reader.readLine();
        this.last_line = (line == null) ? "" : line.trim();
        if (line == null) {
            eof = true;
        }
        return this.last_line;
    }

    public synchronized String getLastLine() {
        return this.last_line;
    }

    /**
     * Use this standard method first (but please notice: semicolon in values
     * are not allowed and confuse your returning array!)
     *
     * @param pDelimiter delimiter
     * @return splitted string
     * @throws IOException cannot read line
     */
    public synchronized String[] readLineAsArray(final String pDelimiter) throws IOException {
        String line = readLine();
        String[] arr = line.split(pDelimiter, -1);
        return arr;
    }

    /**
     * Use this standard method first (but please notice: semicolon in values
     * are not allowed and confuse your returning array!)
     *
     * @return splitted string
     * @throws IOException cannot read line
     */
    public synchronized String[] readLineAsArray() throws IOException {
        return readLineAsArray(";");
    }

    /**
     * Use this standard method first (but please notice: semicolon in values
     * are not allowed and confuse your returning array!)
     *
     * @param pDelimiter delimiter
     * @param pMinSize adds additional values to the array if it is to short
     * @return splitted string
     * @throws IOException cannot read line
     */
    public synchronized String[] readLineAsArray(final String pDelimiter, final int pMinSize) throws IOException {
        String[] arr = readLineAsArray(pDelimiter);
        if (arr.length >= pMinSize) {
            return arr;
        }
        String[] newArr = new String[pMinSize];
        Arrays.fill(newArr, "");
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }

    /**
     * Use this standard method first (but please notice: semicolon in values
     * are not allowed and confuse your returning array!)
     *
     * @param pMinSize adds additional values to the array if it is to short
     * @return splitted string
     * @throws IOException cannot read line
     */
    public synchronized String[] readLineAsArray(final int pMinSize) throws IOException {
        return readLineAsArray(";", pMinSize);
    }

    /**
     * Use this method if there can be semicolons in the values (e. g.
     * "Kreisverwaltung Saalekreis Sozialamt; SG Örtl. Träger/Zuwanderung")
     *
     * @return splitted string
     * @throws IOException cannot read line
     */
    public synchronized String[] readLineAsArrayRespectingQuotes() throws IOException {
        String line = readLine();
        String[] arr = splitRespectingQuotes(";", line);
        return arr;
    }

    /**
     * Use this method if there can be semicolons in the values (e. g.
     * "Kreisverwaltung Saalekreis Sozialamt; SG Örtl. Träger/Zuwanderung")
     *
     * @param pMinSize adds additional values to the array if it is to short
     * @return splitted string
     * @throws IOException cannot read line
     */
    public synchronized String[] readLineAsArrayRespectingQuotes(final int pMinSize) throws IOException {
        String[] arr = readLineAsArrayRespectingQuotes();
        if (arr.length >= pMinSize) {
            return arr;
        }
        String[] newArr = new String[pMinSize];
        Arrays.fill(newArr, "");
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }

    //Look at https://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes plz
    private static String[] splitRespectingQuotes(final String pDelimiter, final String pLine) {
        String otherThanQuote = " [^\"] ";
        String quotedString = String.format(" \" %s* \" ", otherThanQuote);
        String regex = String.format("(?x) "
                + // enable comments, ignore white spaces
                pDelimiter + "             "
                + // match a comma or semicolon or whatever
                "(?=                       "
                + // start positive look ahead
                "  (?:                     "
                + //   start non-capturing group 1
                "    %s*                   "
                + //     match 'otherThanQuote' zero or more times
                "    %s                    "
                + //     match 'quotedString'
                "  )*                      "
                + //   end group 1 and repeat it zero or more times
                "  %s*                     "
                + //   match 'otherThanQuote'
                "  $                       "
                + // match the end of the string
                ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);
        //quotedString);

        String[] tokens = pLine.split(regex, -1);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.startsWith("\"") && token.endsWith("\"")) {
                token = token.substring(1);
                if (token.endsWith("\"")) {
                    token = token.substring(0, token.length() - 1);
                }
            }
            token = token.replace("\"\"", "\"");
            token = token.trim();
            tokens[i] = token;
        }
        return tokens;
    }

    public synchronized boolean eof() {
        return eof;
    }

    @Override
    public void close() throws IOException {
        if (reader == null) {
            return;
        }
        reader.close();
    }

}

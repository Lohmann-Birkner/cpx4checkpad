/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.converter;

import de.lb.cpx.document.Utils.FILE_TYPES;
import de.lb.cpx.reader.util.Document;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentConverterResult {

    private static final Logger LOG = Logger.getLogger(DocumentConverterResult.class.getName());

    public final File inFile;
    public final FILE_TYPES fileType;
    public final File pdfFile;
    public final File txtFile;
    private Document doc = null;

    public DocumentConverterResult(final File pInFile,
            final FILE_TYPES pFileType,
            final File pPdfFile, final File pTxtFile) {
        this.inFile = pInFile;
        this.fileType = pFileType;
        this.pdfFile = pPdfFile;
        this.txtFile = pTxtFile;
    }

    public String readText() throws IOException {
        if (txtFile == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new FileReader(txtFile))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        }
        return sb.toString();
    }

//    public String getFileTitle() {
//        String name = inFile.getName().replace("_", " ");
//        return name;
//    }
    public Document getDocument() {
        if (doc == null) {
            String text = "";
            try {
                text = readText();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            doc = new Document(inFile, text);
        }
        return doc;
    }

}

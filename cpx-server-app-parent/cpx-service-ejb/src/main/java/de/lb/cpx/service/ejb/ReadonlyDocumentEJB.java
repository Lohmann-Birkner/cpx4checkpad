/*
 * Copyright (c) 2022 Lohmann & Birkner.
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
 *    2022  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.shared.enums.ReadonlyDocumentsEn;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import org.apache.commons.io.IOUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author gerschmann
 */
@Stateful
@LocalBean
@SecurityDomain("cpx")
@PermitAll
public class ReadonlyDocumentEJB implements ReadonlyDocumentEJBRemote{

    private static final Logger LOG = Logger.getLogger(ReadonlyDocumentEJB.class.getName());


    @Override
    public byte[] getDocumentContent(ReadonlyDocumentsEn readonlyDocumentsEn, String pYear) throws IOException{
        final File file = new File(readonlyDocumentsEn.getServerAbsPath(pYear));
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File not found: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IOException("No permission to read from file: " + file.getAbsolutePath());
        }
        final byte[] content;
        try (FileInputStream in = new FileInputStream(file)) {
            content = IOUtils.toByteArray(in);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read file: " + file.getAbsolutePath(), ex);
            throw ex;
        }

        if (content == null || content.length == 0) {
            LOG.log(Level.SEVERE, "File has no content: {0}", file.getAbsolutePath());
        }

        return content;
    }
    
    @Override
    public List<String> getCatalogYears(ReadonlyDocumentsEn pType) throws RemoteException{

        final File helpDir = new File(CpxSystemProperties.getInstance().getCpxServerHelpDir());
        if(helpDir == null || !helpDir.isDirectory()){
            LOG.log(Level.INFO, "No helpDirectory found {0}", helpDir.getAbsolutePath());
            return null;
        }
        File[] files = helpDir.listFiles();
        if(files == null || files.length == 0){
            LOG.log(Level.INFO, "Help directory is empty {0}", helpDir.getAbsolutePath());
            return null;
        }
        List<String> years = new ArrayList<>();
        for(File file: files){
            if(file.getName().startsWith(pType.getDocName())){
                String[] parts = file.getName().split("_");
                if(parts.length > 0){
                    String year = parts[parts.length - 1];
                    int ind = year.indexOf(".");
                    if(ind > 0){
                        year = year.substring(0, ind);
                    }
                    years.add(year);
                }
            }
        }
        Collections.sort(years, Collections.reverseOrder());
        return years;
    }
         
}

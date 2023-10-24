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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.drgmexport;

import de.checkpoint.drg.GrouperInterfaceBasic;
import de.checkpoint.drg.RuleCodeMgr;
import de.lb.cpx.server.commonDB.model.CPeppCatalog;
import de.lb.cpx.server.commons.dao.AbstractDrgmCatalogEntity;
import de.lb.cpx.service.information.CatalogTypeEn;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class DrgmExportService {

    private static final Logger LOG = Logger.getLogger(DrgmExportService.class.getName());
    private static final String DRGM_SUFFIX = ".drgm";

    public String getFileName2CatalogTypeYear(final CatalogTypeEn pCatalogType, final int pYear) {
        int modelId = GrouperInterfaceBasic.getModelIdByYear(pYear);
        if (modelId == 0) {
            LOG.log(Level.WARNING, "unknown catalog type:" + pCatalogType.toString() + " for year " + pYear + "; this year is not supported");
            return null;
        }
        String fileName = "";
        switch (pCatalogType) {
            case DRG:
                fileName = "drgcatalog_";
                break;
            case PEPP:
                fileName = "peppcatalog_";
                break;
            case ZE:
                fileName = "drgzusatz_";
                break;
            case ZP:
                fileName = "peppzusatz_";
                break;
            case ET:
                fileName = "etzusatz_";
                break;
            default:
                LOG.log(Level.WARNING, "Unknown catalog type: " + pCatalogType);
        }
        if (fileName.isEmpty()) {
            LOG.log(Level.WARNING, "unknown catalog type:" + pCatalogType.toString());
            return null;
        }

        return fileName + modelId + DRGM_SUFFIX;
    }

    /**
     * writes the drgm - file into WD_SERVER\catalog
     *
     * @param pResult list of Catalog entries can't be null
     * @param pFileName destination file name
     * @return was successfully stored?
     */
    public boolean saveDrgmFile(final List<? extends AbstractDrgmCatalogEntity> pResult, final String pFileName) {
        boolean ret = false;
        String catalogPath = RuleCodeMgr.getCatalogPath();
        File destDir = new File(catalogPath);
        if (!destDir.exists() || !destDir.isDirectory() || !destDir.canWrite() || !Files.isWritable(destDir.toPath())) {
            LOG.log(Level.SEVERE, "directory " + catalogPath + " is not found or is not writeable");
        }
        //BufferedWriter br = null;
        final String destFilename = catalogPath + pFileName;
        final String defaultEncoding = "Cp1252";
        try ( BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFilename), defaultEncoding))) {
            //try{
            //    FileWriter destFile = new FileWriter(catalogPath + pFileName);
            //    br = new BufferedWriter(destFile);

            if (pFileName.contains("peppcatalog")) {
                ret = saveDrgmPeppFile(br, pResult);
            } else {
                ret = saveAnyDrgmFile(br, pResult);
            }

            LOG.log(Level.INFO, "Catalog file" + pFileName + "  on path " + catalogPath + "successfully downloaded");
            return ret;
        } catch (IOException exc) {
            LOG.log(Level.SEVERE, "Error on writing file " + pFileName + "  on path " + catalogPath, exc);
        }
        return ret;
    }

    /**
     * Schreibt drgm - Datei für drg, ze, zp oder et
     *
     * @param br Buffered writer
     * @param pResult Liste der Catalog Objekten
     * @return true bei erfolg
     */
    private boolean saveAnyDrgmFile(final Writer br, final List<? extends AbstractDrgmCatalogEntity> pResult) throws IOException {
        for (AbstractDrgmCatalogEntity entity : pResult) {
            br.append(entity.get2DrgmMapping());
        }
        return true;
    }

    /**
     * für PEPP kann die DRGM - Eintrag aus bis 5 Objekten CPeppCatalog
     * aufgebaut werden
     *
     * @param br Buffered Writer
     * @param pResult Liste der CPeppCatalog Objekten sortiert nach pepp, ikz,
     * relationNumber, validFrom
     * @return bei erfolg
     */
    private boolean saveDrgmPeppFile(Writer br, List<? extends AbstractDrgmCatalogEntity> pResult) throws IOException {
        for (int i = 0; i < pResult.size(); i++) {
            CPeppCatalog pepp = (CPeppCatalog) pResult.get(i);
            StringBuilder oneDrgm = new StringBuilder();
            String commonPart = pepp.get2DrgmMapping();

            oneDrgm.append(commonPart);
            if (pepp.getPeppHasClassesFl()) {
                // ab 2015
                oneDrgm.append("-1;-1;0.0;-1;-1;0.0;-1;-1;0.0;-1;-1;0.0;1");
                br.append(oneDrgm.toString());
                br.append("\r\n");
                continue;
            }
            int relationCount = 1;
            i++;
            for (; i < pResult.size(); i++) {
                CPeppCatalog tmpPepp = (CPeppCatalog) pResult.get(i);

                if (pepp.getPeppPepp().equals(tmpPepp.getPeppPepp())
                        && (pepp.getPeppHosIdent() == null && tmpPepp.getPeppHosIdent() == null
                        || pepp.getPeppHosIdent() != null && tmpPepp.getPeppHosIdent() != null
                        && pepp.getPeppHosIdent().equals(tmpPepp.getPeppHosIdent()))
                        && (pepp.getPeppValidFrom() == null && tmpPepp.getPeppValidFrom() == null
                        || pepp.getPeppValidFrom() != null && tmpPepp.getPeppValidFrom() != null
                        && pepp.getPeppValidFrom().equals(tmpPepp.getPeppValidFrom()))) {
                    // gehören in dieselbe Zeile
                    oneDrgm.append(tmpPepp.getRelation2DrgmMapping());
                    relationCount++;
                } else {
                    break;
                }
            }
            boolean setMinus1 = pepp.getRelation2DrgmMapping().startsWith("-1;-1");
            if (relationCount < 5) {
                for (; relationCount < 5; relationCount++) {
                    if (pepp.getPeppIsNegotiatedFl() || setMinus1) {
                        oneDrgm.append("-1;-1;0.0;");
                    } else {
                        oneDrgm.append("0;0;0.0;");
                    }
                }
            }
            oneDrgm.append("0");
            br.append(oneDrgm.toString());
            br.append("\r\n");
            i--;
        }

        return true;
    }

}

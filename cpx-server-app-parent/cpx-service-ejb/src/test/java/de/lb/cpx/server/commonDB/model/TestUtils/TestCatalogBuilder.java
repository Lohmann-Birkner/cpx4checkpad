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
package de.lb.cpx.server.commonDB.model.TestUtils;

import de.checkpoint.drg.GrouperInterfaceBasic;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commonDB.model.CDrgCatalog;
import de.lb.cpx.server.commonDB.model.CPeppCatalog;
import de.lb.cpx.server.commonDB.model.CSupplementaryFee;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import de.lb.cpx.server.commons.dao.AbstractDrgmCatalogEntity;
import de.lb.cpx.service.information.CatalogTypeEn;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.fail;

/**
 * es wird eine DRGM - Datei für entsprechenden Katalog gelesen und in den
 * tempörären DB gespeichert
 *
 * @author gerschmann
 */
public class TestCatalogBuilder {

    private final SimpleDateFormat dateformat = new SimpleDateFormat("yyyyddmm");
    private static final String DRG_CATALOG_PATH = "catalog/drgcatalog_51.drgm";
    private static final String PEPP_CATALOG_PATH_OLD = "catalog/peppcatalog_30.drgm";
    private static final String PEPP_CATALOG_PATH_NEW = "catalog/peppcatalog_45.drgm";
    private static final String DRG_SUPPL_CATALOG_PATH = "catalog/drgzusatz_51.drgm";
    private static final String PEPP_SUPPL_CATALOG_PATH = "catalog/peppzusatz_45.drgm";
    private static final String PEPP_ET_SUPPL_CATALOG_PATH = "catalog/etzusatz_45.drgm";
    private static final String DRG_OUTPUT_CATALOG_PATH = "catalog/output/drgcatalog_51.drgm";
    private static final String PEPP_OUTPUT_CATALOG_PATH_OLD = "catalog/output/peppcatalog_30.drgm";
    private static final String PEPP_OUTPUT_CATALOG_PATH_NEW = "catalog/output/peppcatalog_45.drgm";
    private static final String DRG_SUPPL_OUTPUT_CATALOG_PATH = "catalog/output/drgzusatz_51.drgm";
    private static final String PEPP_SUPPL_OUTPUT_CATALOG_PATH = "catalog/output/peppzusatz_45.drgm";
    private static final String PEPP_ET_SUPPL_OUTPUT_CATALOG_PATH = "catalog/output/etzusatz_45.drgm";
    private static final Logger LOG = Logger.getLogger(TestCatalogBuilder.class.getName());

    private TestCatalogBuilder() {

    }

    public static TestCatalogBuilder getInstance() {
        return new TestCatalogBuilder();
    }

    public HashMap<String, ?> getCatalog(CatalogTypeEn pCatalogType) {
        return getCatalog(pCatalogType, true);
    }

    public HashMap<String, ?> getCatalog(CatalogTypeEn pCatalogType, boolean old) {
        String catalogPath = getCatalogPath(pCatalogType, old, false);
        if (catalogPath == null) {
            fail(" there is no file for catalogtype " + pCatalogType.toString() + " found ");
            return null;
        }
        return readCatalogFromPath(catalogPath, pCatalogType);

    }

    private String getCatalogPath(CatalogTypeEn pCatalogType, boolean old, boolean export) {
        String catalogPath = null;
        switch (pCatalogType) {
            case DRG:
                if (export) {
                    catalogPath = DRG_OUTPUT_CATALOG_PATH;
                } else {
                    catalogPath = DRG_CATALOG_PATH;
                }
                break;
            case PEPP:
                if (export) {
                    if (old) {
                        catalogPath = PEPP_OUTPUT_CATALOG_PATH_OLD;
                    } else {
                        catalogPath = PEPP_OUTPUT_CATALOG_PATH_NEW;
                    }

                } else {
                    if (old) {
                        catalogPath = PEPP_CATALOG_PATH_OLD;
                    } else {
                        catalogPath = PEPP_CATALOG_PATH_NEW;
                    }
                }
                break;
            case ZE:
                if (export) {
                    catalogPath = DRG_SUPPL_OUTPUT_CATALOG_PATH;

                } else {
                    catalogPath = DRG_SUPPL_CATALOG_PATH;
                }
                break;
            case ZP:
                if (export) {
                    catalogPath = PEPP_SUPPL_OUTPUT_CATALOG_PATH;

                } else {
                    catalogPath = PEPP_SUPPL_CATALOG_PATH;
                }
                break;
            case ET:
                if (export) {
                    catalogPath = PEPP_ET_SUPPL_OUTPUT_CATALOG_PATH;

                } else {
                    catalogPath = PEPP_ET_SUPPL_CATALOG_PATH;
                }
                break;

        }
        if (catalogPath == null) {
            LOG.log(Level.INFO, " there is no file for catalogtype {0} found ", pCatalogType.toString());
            fail(" there is no file for catalogtype " + pCatalogType.toString() + " found ");
            return null;
        }
        return catalogPath;
    }

    private HashMap<String, ?> readCatalogFromPath(String pCatalogPath, CatalogTypeEn pCatalogType) {
        HashMap<String, ?> ret = getResultCollection4Type(pCatalogType);
        if (ret == null) {
            LOG.log(Level.INFO, "unknown catalog type {0}", pCatalogType.name());
            return null;
        }
        ClassLoader classLoader = TestCatalogBuilder.class.getClassLoader();
        File file = new File(classLoader.getResource(pCatalogPath).getFile());
        int i = pCatalogPath.lastIndexOf('_');
        String catalogId = pCatalogPath.substring(i + 1, i + 3);
        int catId = 0;
        if (pCatalogType.equals(CatalogTypeEn.PEPP)) {
            try {

                catId = Integer.parseInt(catalogId);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Katalog Id konnte aus dem Dateinamen nicht ermittelt werden", ex);
                return ret;
            }
        }
        i = 0;
        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Logger.getLogger(TestCatalogBuilder.class.getName()).log(Level.INFO, line);
                AbstractCatalogEntity entity = null;
                switch (pCatalogType) {
                    case DRG:
                        entity = createDrgEntity(line, ret);
                        break;
                    case PEPP:
                        createPeppEntity(line, catId, ret);
                        break;
                    case ZP:
                        entity = createSupplEntity(line, SupplFeeTypeEn.ZP, ret);
                        break;

                    case ZE:
                        entity = createSupplEntity(line, SupplFeeTypeEn.ZE, ret);
                        break;

                    case ET:
                        entity = createSupplEntity(line, SupplFeeTypeEn.ET, ret);
                        break;
                }

            }
            scanner.close();

        } catch (IOException ex) {
            LOG.log(Level.WARNING, ex.getMessage());
        }
        return ret;

    }

    private HashMap<String, ?> getResultCollection4Type(CatalogTypeEn pCatalogType) {
        switch (pCatalogType) {

            case PEPP:
                return new HashMap<String, Set<AbstractCatalogEntity>>();
            case ZP:
            case ZE:
            case DRG:
            case ET:
                return new HashMap< String, AbstractCatalogEntity>();

        }
        return null;
    }

    private AbstractCatalogEntity createDrgEntity(String line, HashMap<?, ?> cat) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        HashMap<String, AbstractCatalogEntity> catalog = (HashMap<String, AbstractCatalogEntity>) cat;
        CDrgCatalog drg = new CDrgCatalog();
        catalog.put(line, drg);
        String parts[] = line.split(";");
        for (int i = 0, len = parts.length; i < len; i++) {
            String s = parts[i].trim();
            switch (i) {
                case 0:
                    drg.setId(getInt(s));
                    break;
                case 1: {
                    s = s.replaceAll("\"", "");
                    drg.setDrgHosIdent(s);
                    break;
                }
                case 2:
                    drg.setDrgYear(GrouperInterfaceBasic.getDRGCatalogYear2Id(getInt(s)));
                    break;
                case 3: {
                    s = s.replaceAll("\"", "");
                    drg.setDrgDrg(s);
                    break;
                }
                case 4:
                    drg.setDrgMdCw(this.getBigDecimal(s));
                    break;
                case 5:
                    drg.setDrgMdMCw(this.getBigDecimal(s));
                    break;
                case 6:
                    drg.setDrgMd1DeductionDay(getInt(s));
                    break;
                case 7:
                    drg.setDrgMdCwDeduction(getBigDecimal(s));
                    break;
                case 8:
                    drg.setDrgMd1SurchargeDay(getInt(s));
                    break;
                case 9:
                    drg.setDrgMdCwSurcharge(getBigDecimal(s));
                    break;
                case 10:
                    drg.setDrgMdCwTransfDeduct(getBigDecimal(s));
                    break;
                case 11:
                    drg.setDrgMdIsTransferFl(getInt(s) == 1);
                    break;
                case 12:
                    drg.setDrgMdIsReadmFl(getInt(s) == 1);
                    break;
                case 13:
                    drg.setDrgEoCw(getBigDecimal(s));
                    break;
                case 14:
                    drg.setDrgEoaCw(getBigDecimal(s));
                    break;
                case 15:
                    drg.setDrgEomCw(getBigDecimal(s));
                    break;
                case 16:
                    drg.setDrgEoamCw(getBigDecimal(s));
                    break;
                case 17:
                    drg.setDrgEo1DeductionDay(getInt(s));
                    break;
                case 18:
                    drg.setDrgEoCwDeduction(getBigDecimal(s));
                    break;
                case 19:
                    drg.setDrgEo1SurchargeDay(getInt(s));
                    break;
                case 20:
                    drg.setDrgEoCwSurcharge(getBigDecimal(s));
                    break;
                case 21:
                    drg.setDrgEoCwTransfDeduct(getBigDecimal(s));
                    break;
                case 22:
                    drg.setDrgEoIsTransferFl(getInt(s) == 1);
                    break;
                case 23:
                    drg.setDrgEoIsReadmFl(getInt(s) == 1);
                    break;
                case 24:
                    drg.setDrgMdAlos(getBigDecimal(s));
                    break;
                case 25:
                    drg.setDrgIsNegotiatedFl(getInt(s) == 1);
                    break;
                case 26: {
                    drg.setDrgValidFrom(getDate(s));
                    break;
                }
                case 28:
                    drg.setDrgIsDayCareFl(getInt(s) == 1);
                    break;
                case 29:
                    drg.setDrgPartitionEn(DrgPartitionEn.valueOf(s));
                    break;
                case 30: {
                    drg.setDrgValidTo(getDate(s));
                    break;
                }
                case 31:
                    drg.setDrgEoAlos(getBigDecimal(s));
                    break;
                case 32:
                    drg.setDrgNegoDayFee(getBigDecimal(s).doubleValue());
                    break;
                case 33:
                    drg.setDrgMdMedianCaseCount(getInt(s));
                    break;
                case 34:
                    drg.setDrgEoMedianCaseCount(getInt(s));
                    break;
                case 35:
                    drg.setDrgMdCareCwDay(getBigDecimal(s));
                    break;
                case 36:
                    drg.setDrgEoCareCwDay(getBigDecimal(s));
                    break;
            }

        }
        return drg;
    }

    private BigDecimal getBigDecimal(String sValue) {
        try {
            double d = (sValue != null) ? Double.valueOf(sValue) : 0.0;
            return new BigDecimal(d);
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, "could not convert String {0} to BigDecimal", sValue);
        }
        return null;
    }

    private int getInt(String sValue) {
        try {
            return Integer.parseInt(sValue);

        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, "could not convert String {0} to int", sValue);
        }
        return 0;
    }

    private static Date convertStringToDate(String strDate, String format) {
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        Date datum = null;
        try {
            datum = dateformat.parse(strDate);
        } catch (ParseException ex) {
            LOG.log(Level.WARNING, "error on converting String {0} to date", strDate);

        }
        return datum;
    }

    private static Date getDate(String str) {
        if (str == null) {
            return null;
        }

        String s = str.trim().replaceAll("\"", "");
        Date d = null;

        if (s == null || s.length() < 8) {
            d = null;
        } else if (s.length() < 19) {
            String[] ss = s.split(" ");
            s = ss[0];
            if (s == null || s.length() < 8) {
                d = null;
            } else if (s.length() > 8) {
                d = convertStringToDate(s, "yyyy-MM-dd");
            } else {
                d = convertStringToDate(s, "yyyyMMdd");
            }
        } else {
            d = convertStringToDate(s, "yyyy-MM-dd HH:mm:ss");
        }

        return d;
    }

    public boolean checkResults(Map< String, AbstractDrgmCatalogEntity> cat) {
        if (cat == null) {
            return false;
        }
        Set<String> lines = cat.keySet();
        boolean ret = true;
        for (String line : lines) {
            AbstractDrgmCatalogEntity catEntry = cat.get(line);
            line += "\r\n";
            String result = catEntry.get2DrgmMapping();
            if (!line.equals(result)) {
                LOG.log(Level.INFO, "input: {0}", line);
                LOG.log(Level.INFO, "result: {0}", result);
                ret = false;
            }
        }
        return ret;
    }

    private Set<CPeppCatalog> createPeppEntity(String line, int catalogId, HashMap<?, ?> cat) {
        @SuppressWarnings("unchecked")
        Map<String, Set<CPeppCatalog>> catalog = (HashMap<String, Set<CPeppCatalog>>) cat;

        if (line == null || line.isEmpty()) {
            return null;
        }
        Set<CPeppCatalog> peppList = new HashSet<>();
        catalog.put(line, peppList);
        CPeppCatalog pepp = new CPeppCatalog();
        peppList.add(pepp);
        pepp.setPeppHasClassesFl(catalogId >= 36);
        String parts[] = line.split(";");
        boolean doBreak = false;
        for (int i = 0; i < parts.length; i++) {
            if (doBreak) {
                break;
            }
            String s = parts[i];
            switch (i) {
                case 0:
                    pepp.setId(getInt(s));
                    break;
                case 1:
                    pepp.setPeppHosIdent(s);// ikz
                    break;
                case 2:
                    pepp.setPeppYear(GrouperInterfaceBasic.getDRGCatalogYear2Id(getInt(s)));// catalog_id
                    break;
                case 3:
                    pepp.setPeppValidFrom(getDate(s));// gueltig_von
                    break;
                case 4:
                    pepp.setPeppValidTo(getDate(s));// gueltig bis
                    break;
                case 5:
                    pepp.setPeppPepp(s);//pepp
                    break;
                case 6:
                    pepp.setPeppIsNegotiatedFl(getInt(s) == 1);// verhandelbar
                    break;
                case 7:
                    pepp.setPeppIsDayCareFl(getInt(s) == 1);//teilstat
                    break;
                case 8: // vwd_von
                    if (catalogId >= 36) {
                        int rel = getInt(s);
                        pepp.setPeppRelationNumber(rel);
                        pepp.setPeppRelationFrom(rel);
                    } else {
                        pepp.setPeppRelationNumber(1);
                        pepp.setPeppRelationFrom(getInt(s));
                    }
                    break;
                case 9:
                    pepp.setPeppRelationTo(getInt(s));
                    //vwd bis
                    break;
                case 10:
                    pepp.setPeppRelationCostWeight(getBigDecimal(s));// cw stufe 1
                    break;
                case 11: // vwd_von 2 relevant nur für catalogId < 36
                    if (catalogId >= 36 || getInt(parts[i - 2]) <= 0) {
                        doBreak = true;
                        break;
                    }
                    pepp = getNewPapp(pepp, peppList);

                    pepp.setPeppRelationFrom(getInt(s));
                    pepp.setPeppRelationNumber(2);
                    break;
                case 12:    //vwd bis 2
                case 15:    //vwd bis 3
                case 18:    //vwd bis 4
                case 21:    //vwd bis 5  
                    pepp.setPeppRelationTo(getInt(s));

                    break;
                case 13:// cw stufe 2
                case 16: // cw stufe 3
                case 19: // cw stufe 4                    
                case 22: // cw stufe 5
                    pepp.setPeppRelationCostWeight(getBigDecimal(s));
                    break;
                case 14:
                    if (getInt(parts[i - 2]) <= 0) {// vwd_von 3
                        doBreak = true;
                        break;
                    }
                    pepp = getNewPapp(pepp, peppList);

                    pepp.setPeppRelationFrom(getInt(s));
                    pepp.setPeppRelationNumber(3);
                    break;

                case 17: // vwd_von 4
                    if (getInt(parts[i - 2]) <= 0) {// vwd_von 3
                        doBreak = true;
                        break;
                    }
                    pepp = getNewPapp(pepp, peppList);

                    pepp.setPeppRelationFrom(getInt(s));
                    pepp.setPeppRelationNumber(4);
                    break;

                case 20: // vwd_von 5
                    if (getInt(parts[i - 2]) <= 0) {// vwd_von 3
                        doBreak = true;
                        break;
                    }
                    pepp = getNewPapp(pepp, peppList);

                    pepp.setPeppRelationFrom(getInt(s));
                    pepp.setPeppRelationNumber(5);
                    break;

            }
        }
        return peppList;
    }

    private AbstractCatalogEntity createSupplEntity(String line, SupplFeeTypeEn pCatalogType, HashMap<?, ?> cat) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        CSupplementaryFee suppl = new CSupplementaryFee();
        @SuppressWarnings("unchecked")
        Map<String, AbstractCatalogEntity> catalog = (HashMap<String, AbstractCatalogEntity>) cat;
        catalog.put(line, suppl);
        String[] parts = line.split(";");
        ArrayList<String> partsArray = new ArrayList<>(Arrays.asList(parts));
        suppl.setSupplTypeEn(pCatalogType);
        if (pCatalogType.equals(SupplFeeTypeEn.ZE) && partsArray.size() > 7) {
            // eine Zeile auf position 7 zufügen, damit es einheitlich ist

            partsArray.add(7, "");
        }
        for (int i = 0; i < partsArray.size(); i++) {
            String s = partsArray.get(i);
            switch (i) {
                case 0:
                    suppl.setId(getInt(s));
                    break;
                case 1:
                    suppl.setSupplHosIdent(s);
                    break;
                case 2:
                    suppl.setSupplYear(GrouperInterfaceBasic.getDRGCatalogYear2Id(getInt(s)));
                    break;
                case 3:
                    suppl.setSupplKey(s);
                    break;
                case 4:
                    suppl.setSupplOpsCode(s);
                    break;
                case 5:
                    if (pCatalogType.equals(SupplFeeTypeEn.ET)) {
                        suppl.setSupplCwValue(getBigDecimal(s).doubleValue());
                    } else {
                        suppl.setSupplValue(getBigDecimal(s).doubleValue());
                    }
                    break;
                case 6:
                    suppl.setSupplNegotiated(getInt(s) == 1);
                    break;
                case 7:// entspricht dem Feld Teilstationär in ET und ZP, ist ein Platzhalter, wird nicht benutzt
                    break;
                case 8:
                    suppl.setSupplValidFrom(getDate(s));
                    break;
                case 9:
                    suppl.setSupplValidTo(getDate(s));
                    break;

            }
        }
        return suppl;
    }

    private CPeppCatalog getNewPapp(CPeppCatalog pepp, Set<CPeppCatalog> peppList) {
        CPeppCatalog newPepp = new CPeppCatalog();
        newPepp.setPeppPepp(pepp.getPeppPepp());
        newPepp.setPeppHasClassesFl(pepp.getPeppHasClassesFl());
        newPepp.setPeppYear(pepp.getPeppYear());
        newPepp.setPeppHosIdent(pepp.getPeppHosIdent());
        newPepp.setPeppIsDayCareFl(pepp.getPeppIsDayCareFl());
        newPepp.setPeppIsNegotiatedFl(pepp.getPeppIsNegotiatedFl());
        newPepp.setPeppValidFrom(pepp.getPeppValidFrom());
        newPepp.setPeppValidTo(pepp.getPeppValidTo());
        peppList.add(newPepp);
        return newPepp;
    }

    public boolean checkPeppResults(Map<String, Set<CPeppCatalog>> cat) {
        Set<String> lines = cat.keySet();
        boolean ret = true;
        PEPPComparator peppComparator = new PEPPComparator();
        for (String line : lines) {
            Set<CPeppCatalog> peppList = cat.get(line);

            if (peppList == null || peppList.isEmpty()) {
                LOG.log(Level.WARNING, " f\u00fcr die drgm Zeile {0} kein Set der CPeppCatalog - Entities generiert", line);
                return false;

            }
            List<CPeppCatalog> peppArrList = new ArrayList<>(peppList);
            Collections.sort(peppArrList, peppComparator);

            CPeppCatalog pepp = peppArrList.get(0);
            String retLine = pepp.get2DrgmMapping();
            if (pepp.getPeppHasClassesFl()) {
                retLine += "-1;-1;0.0;-1;-1;0.0;-1;-1;0.0;-1;-1;0.0;1";
            } else {
                int i = 1;
                for (; i < peppArrList.size(); i++) {
                    pepp = peppArrList.get(i);
                    retLine += pepp.getRelation2DrgmMapping();
                }
                if (i < 5) {
                    for (; i < 5; i++) {
                        if (pepp.getPeppIsNegotiatedFl() || line.contains("-1;-1")) {
                            retLine += "-1;-1;0.0;";
                        } else {
                            retLine += "0;0;0.0;";
                        }
                    }
                }
                retLine += "0";
            }
            if (!line.equals(retLine)) {
                LOG.log(Level.INFO, "input: {0}", line);
                LOG.log(Level.INFO, "result: {0}", retLine);
                ret = false;
            }

        }
        return ret;
    }

    public List<CPeppCatalog> getPeppCatalogAllEntities(Map<String, Set<CPeppCatalog>> cat) {
        ArrayList<CPeppCatalog> peppList = new ArrayList<>();
        if (cat == null) {
            return peppList;
        }
        Set<String> lines = cat.keySet();
        for (String line : lines) {
            Set<CPeppCatalog> pepps = cat.get(line);
            if (pepps != null) {
                peppList.addAll(pepps);
            } else {
                LOG.log(Level.INFO, "for {0} there are no pepp objects found", line);
            }
        }
        return peppList;
    }

    public boolean CheckPeppDrgmResults(Map<String, Set<CPeppCatalog>> cat, StringWriter br) {
        Set<String> inputLines = cat.keySet();
        return CheckDrgmResults(inputLines, br);
    }

    public boolean CheckDrgmResults(Set<String> inputLines, StringWriter br) {
        String[] drgmLines = br.toString().split("\r\n");

//        if(drgmLines == null || drgmLines.length != inputLines.size()){
//            fail("for " + inputLines.size() + " lines in the drgm file ware " + drgmLines.length + " generated");
//            return false;
//        }
        List<String> resArray = Arrays.asList(drgmLines);
        int countDifs = 0;
        for (String line : inputLines) {
            if (!resArray.contains(line)) {
                LOG.log(Level.INFO, " for {0}drgm line was not found", line);
                countDifs++;
            }
        }
        return countDifs == 0;
    }

    public List<AbstractCatalogEntity> getCatalogAllEntities(Map<String, AbstractCatalogEntity> cat) {
        ArrayList<AbstractCatalogEntity> entityList = new ArrayList<>();
        if (cat == null) {
            return entityList;
        }
        Set<String> lines = cat.keySet();
        for (String line : lines) {
            AbstractCatalogEntity entity = cat.get(line);
            if (entity != null) {
                entityList.add(entity);
            } else {
                LOG.log(Level.INFO, "for {0} there is no entity objects found", line);
            }
        }
        return entityList;

    }

    private class PEPPComparator implements Comparator<CPeppCatalog> {

        @Override
        public int compare(CPeppCatalog pepp1, CPeppCatalog pepp2) {
            if (pepp1 != null && pepp2 != null) {
                if (pepp1.getPeppPepp().equals(pepp2.getPeppPepp())) {
// ik vergleichen
                    if (pepp1.getPeppHosIdent().equals(pepp2.getPeppHosIdent())) {
// sortiert nach valid from
                        if (pepp1.getPeppValidFrom() == null && pepp2.getPeppValidFrom() == null
                                || pepp1.getPeppValidFrom().equals(pepp2.getPeppValidFrom())) {
                            return Integer.valueOf(pepp1.getPeppRelationNumber()).compareTo(pepp2.getPeppRelationNumber());
                        } else {
                            if (pepp1.getPeppValidFrom() == null) {
                                return -1;
                            } else if (pepp2.getPeppValidFrom() == null) {
                                return 1;
                            } else {
                                return pepp1.getPeppValidFrom().compareTo(pepp2.getPeppValidFrom());
                            }
                        }

                    } else {
                        return pepp1.getPeppHosIdent().compareTo(pepp2.getPeppHosIdent());
                    }

                } else {
                    return pepp1.getPeppPepp().compareTo(pepp2.getPeppPepp());
                }
            } else {
                return -1;
            }
        }

    }

}

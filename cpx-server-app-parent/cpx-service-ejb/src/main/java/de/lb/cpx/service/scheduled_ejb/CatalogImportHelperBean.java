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
package de.lb.cpx.service.scheduled_ejb;

import de.checkpoint.codes.COADRG;
import de.checkpoint.codes.COCode;
import de.checkpoint.codes.COCodeList;
import de.checkpoint.codes.COCodeProc;
import de.checkpoint.codes.CODRG;
import de.checkpoint.codes.COEtZusatz;
import de.checkpoint.codes.COMDC;
import de.checkpoint.codes.COPEPP;
import de.checkpoint.codes.COPEPPStruktur;
import de.checkpoint.codes.COZusatz;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.file.reader.CpxFileReader;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.IdentClassEn;
import de.lb.cpx.model.enums.StateEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commonDB.dao.AbstractCommonDao;
import de.lb.cpx.server.commonDB.dao.CAtcDao;
import de.lb.cpx.server.commonDB.dao.CBaserateDao;
import de.lb.cpx.server.commonDB.dao.CCatalogDao;
import de.lb.cpx.server.commonDB.dao.CCountryDao;
import de.lb.cpx.server.commonDB.dao.CDepartmentDao;
import de.lb.cpx.server.commonDB.dao.CDoctorDao;
import de.lb.cpx.server.commonDB.dao.CDrgCatalogDao;
import de.lb.cpx.server.commonDB.dao.CDrgCw2DepTypeDao;
import de.lb.cpx.server.commonDB.dao.CDrgLosDependencyDao;
import de.lb.cpx.server.commonDB.dao.CFeeCatalogDao;
import de.lb.cpx.server.commonDB.dao.CHospitalDao;
import de.lb.cpx.server.commonDB.dao.CIcdCatalogDao;
import de.lb.cpx.server.commonDB.dao.CIcdThesaurusDao;
import de.lb.cpx.server.commonDB.dao.CIcdTransferCatalogDao;
import de.lb.cpx.server.commonDB.dao.CInsuranceCompanyDao;
import de.lb.cpx.server.commonDB.dao.CMdkDao;
import de.lb.cpx.server.commonDB.dao.COpsAopDao;
import de.lb.cpx.server.commonDB.dao.COpsCatalogDao;
import de.lb.cpx.server.commonDB.dao.COpsThesaurusDao;
import de.lb.cpx.server.commonDB.dao.COpsTransferCatalogDao;
import de.lb.cpx.server.commonDB.dao.CPeppCaseWeightsDao;
import de.lb.cpx.server.commonDB.dao.CPeppCatalogDao;
import de.lb.cpx.server.commonDB.dao.CPznDao;
import de.lb.cpx.server.commonDB.dao.CSupplementaryFeeDao;
import de.lb.cpx.server.commonDB.dao.CTransferCatalogDao;
import de.lb.cpx.server.commonDB.model.CAtc;
import de.lb.cpx.server.commonDB.model.CBaserate;
import de.lb.cpx.server.commonDB.model.CDepartment;
import de.lb.cpx.server.commonDB.model.CDoctor;
import de.lb.cpx.server.commonDB.model.CDrgCatalog;
import de.lb.cpx.server.commonDB.model.CHospital;
import de.lb.cpx.server.commonDB.model.CIcdCatalog;
import de.lb.cpx.server.commonDB.model.CIcdThesaurus;
import de.lb.cpx.server.commonDB.model.CInsuranceCompany;
import de.lb.cpx.server.commonDB.model.CMdk;
import de.lb.cpx.server.commonDB.model.COpsAop;
import de.lb.cpx.server.commonDB.model.COpsCatalog;
import de.lb.cpx.server.commonDB.model.COpsThesaurus;
import de.lb.cpx.server.commonDB.model.CPeppCatalog;
import de.lb.cpx.server.commonDB.model.CPzn;
import de.lb.cpx.server.commonDB.model.CSupplementaryFee;
import de.lb.cpx.server.commonDB.model.rules.CTransferCatalog;
import de.lb.cpx.server.commons.dao.AbstractEntity;

import de.lb.cpx.service.ejb.CatalogExportServiceEJBRemote;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.scheduled_ejb.AopCatalogReader.AopCatalogEntry;
import static de.lb.cpx.str.utils.StrUtils.toBool;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 *
 * @author Dirk Niemeier
 */
@Stateless
@SuppressWarnings("UseOfObsoleteCollectionType")
@SecurityDomain("cpx")
@LocalBean
public class CatalogImportHelperBean {

    private static final Logger LOG = Logger.getLogger(CatalogImportHelperBean.class.getName());

    @EJB
    private CCatalogDao catalogDao;
    @EJB
    private CCountryDao countryDao;
    @EJB
    private CFeeCatalogDao feeCatalogDao;
    //@EJB
    //CMdcSkCatalogDao mdcSkCatalogDao;
    @EJB
    private CDrgCatalogDao drgCatalogDao;
    @EJB
    private CSupplementaryFeeDao supplementaryFeeDao;
    @EJB
    private CPeppCatalogDao peppCatalogDao;
    @EJB
    private CDrgCw2DepTypeDao drgCw2DepTypeDao;
    @EJB
    private CDrgLosDependencyDao drgLosDependencyDao;
    @EJB
    private CPeppCaseWeightsDao peppCaseWeightsDao;
    @EJB
    private CIcdCatalogDao icdCatalogDao;
    @EJB
    private COpsCatalogDao opsCatalogDao;
    @EJB
    private CHospitalDao hospitalDao;
    @EJB
    private CInsuranceCompanyDao insuranceCompanyDao;
    @EJB
    private CDepartmentDao departmentDao;
    @EJB
    private CDoctorDao doctorDao;
    @EJB
    private CAtcDao atcDao;
    @EJB
    private CPznDao pznDao;
    @EJB
    private CMdkDao mdkDao;
    @EJB
    private CBaserateDao baserateDao;
    @EJB
    private CIcdThesaurusDao icdThesaurusDao;
    @EJB
    private COpsThesaurusDao opsThesaurusDao;  
    
    @EJB
    private CIcdTransferCatalogDao icdTransferCatalogDao;
    
    @EJB
    private COpsTransferCatalogDao opsTransferCatalogDao;
    
    @EJB
    private COpsAopDao opsAopDao;
    
    //private CCountry country = null;
    public static final String COUNTRY_SHORT_NAME = "de";
    //private final String country_name = "Germany";
    private final CountryEn countryEn = CountryEn.valueOf(COUNTRY_SHORT_NAME);
    
    @EJB(beanName = "CatalogExportServiceEJB")
    private CatalogExportServiceEJBRemote catalogExport;

    @SuppressWarnings("unchecked")
    public static <E> E unserialize(final GZIPInputStream gis) throws IOException {
        if (gis == null) {
            return null;
        }
        ObjectInputStream ois = new ObjectInputStream(gis);
        E obj = null;
        try {
            obj = (E) ois.readObject();
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Was not able to uncompress data", ex);
        }
        return obj;
    }

//    @PermitAll
//    public static int CatalogTypeEn.detectYear(final File pFile) {
//        int year = 0;
//        if (pFile == null) {
//            return year;
//        }
//        String name = pFile.getName().trim();
//        int pos;
//        if ((pos = name.lastIndexOf('-')) > -1) {
//            name = name.substring(pos + 1);
//            name = name.substring(0, name.indexOf('.'));
//            try {
//                year = Integer.valueOf(name);
//            } catch (NumberFormatException ex) {
//                LOG.log(Level.SEVERE, "Invalid year found in catalog name: " + name, ex);
//            }
//        }
//        return year;
//    }
    public static Integer toInt(final String pValue) {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return null;
        }
        Integer val = null;
        try {
            val = Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            //
        }
        return val;
    }

    public static Long toLong(final String pValue) {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return null;
        }
        Long val = null;
        try {
            val = Long.valueOf(value);
        } catch (NumberFormatException ex) {
            //
        }
        return val;
    }

    public static String toStr(final String pValue) {
        return (pValue == null) ? "" : pValue.trim();
    }

    public static String toStr(final String pValue, final String pDefaultValue) {
        String value = toStr(pValue);
        return (value == null || value.isEmpty()) ? pDefaultValue : pValue;
    }

//    public static boolean toBool(final String pValue) {
//        String value = toStr(pValue);
//        if (value.isEmpty()) {
//            return false;
//        }
//        value = value.toLowerCase();
//        if (value.equalsIgnoreCase("1")) {
//            return true;
//        }
//        if (value.equalsIgnoreCase("t")) {
//            return true;
//        }
//        if (value.equalsIgnoreCase("true")) {
//            return true;
//        }
//        if (value.equalsIgnoreCase("on")) {
//            return true;
//        }
//        if (value.equalsIgnoreCase("enabled")) {
//            return true;
//        }
//        if (value.equalsIgnoreCase("enable")) {
//            return true;
//        }
//        return false;
//    }
    public static Double toDouble(final String pValue) {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return null;
        }
        return Double.parseDouble(value);
    }

    public static Double toDouble(final String pValue, final Double pDefaultValue) {
        try {
            Double value = toDouble(pValue);
            return (value == null) ? pDefaultValue : value;
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, "cannot parse string as double: " + pValue + " (will use default value: " + pDefaultValue + ")", ex);
            return pDefaultValue;
        }
    }

    public static Date toDate(final String pValue) throws ParseException {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return null;
        }
        DateFormat format;
        if (value.contains("-")) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            format = new SimpleDateFormat("yyyyMMdd");
        }
        return format.parse(value);
    }

    public static Date toDate(final String pValue, final Date pDefaultValue) {
        try {
            Date value = toDate(pValue);
            return (value == null) ? pDefaultValue : value;
        } catch (ParseException ex) {
            return pDefaultValue;
        }
    }

    public static long getSize(File pFile) {
        if (pFile == null) {
            throw new IllegalArgumentException("File is null!");
        }
        return pFile.length();
    }

    /**
     * Look at
     * http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
     *
     * @param pFile File
     * @return Checksum
     * @throws IllegalArgumentException Invalid file
     * @throws IOException File could not be opened
     */
    public static String getChecksum(File pFile) throws IOException {
        if (pFile == null) {
            throw new IllegalArgumentException("File is null!");
        }
        MessageDigest complete;
        final String hashType = "SHA-256";
        try (InputStream fis = new FileInputStream(pFile)) {
            try {
                complete = MessageDigest.getInstance(hashType); //MD5, SHA-1, SHA-256
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(CatalogImportHelperBean.class.getName()).log(Level.SEVERE, "Hash type seems to be invalid: " + hashType, ex);
                return "";
            }
            int numRead;
            byte[] buffer = new byte[1024];
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
        }
        byte[] b = complete.digest();

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString().toLowerCase().trim();
    }

    @TransactionAttribute(REQUIRES_NEW)
    @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
    @Asynchronous
    public synchronized Future<CatalogImportResult> importCatalog(final File pFile) throws IOException {
        if (pFile == null) {
            LOG.log(Level.WARNING, "file is null!");
            return new AsyncResult<>(new CatalogImportResult(false, null, null, 0, "", 0, 0, "file is null!"));
        }
        final CatalogTypeEn catalogType = CatalogTypeEn.detectType(pFile);
        final int year = CatalogTypeEn.detectYear(pFile, catalogType);
        final String checksum = checkCheckSum( pFile);
        final long size = getSize(pFile);
        CatalogImportResult result = new CatalogImportResult(false, pFile, catalogType, year, checksum, size, 0, "");

        try {

            LOG.log(Level.INFO, "Starting catalog import of " + pFile.getAbsolutePath() + "...");
            //final String modelId = getDrgModelId(year);
            LOG.log(Level.INFO, "Catalog type: " + catalogType + ", Catalog year " + year);
            //String category = "";
            //String suppType = "";

            if (catalogType != null /* && year > 0 */) {
                switch (catalogType) {
                    case DRG:
                        result = importDrg(pFile, checksum, year);
                        break;
                    case ZE:
                        result = importZusatz(pFile, checksum, year);
                        break;
                    case PEPP:
                        result = importPepp(pFile, checksum, year);
                        break;
                    case ZP:
                        result = importPeppZusatz(pFile, checksum, year);
                        break;
                    case ET:
                        result = importEtZusatz(pFile, checksum, year);
                        break;
                    case ICD:
                        result = importIcd(pFile, checksum, year);
                        break;
                    case OPS:
                        result = importOps(pFile, checksum, year);
                        break;
                    case HOSPITAL:
                        result = importHospital(pFile, checksum);
                        break;
                    case INSURANCE_COMPANY:
                        result = importInsuranceCompany(pFile, checksum);
                        break;
                    case DEPARTMENT:
                        result = importDepartment(pFile, checksum);
                        break;
                    case DOCTOR:
                        result = importDoctor(pFile, checksum);
                        break;
                    case ATC:
                        result = importAtc(pFile, checksum);
                        break;
                    case PZN:
                        result = importPzn(pFile, checksum);
                        break;
                    case MDK:
                        //2017-01-24 DNi: Wird über die WebApp editiert und importiert.
                        //2017-07-05 DNi: Execute first import after new CPX installation
                        result = importMdk(pFile, checksum);
                        break;
                    case BASERATE:
                        //2017-01-05 DNi: Use WebApp to import Baserates catalog from now on!
                        //2017-07-05 DNi: Execute first import after new CPX installation
                        result = importBaserate(pFile, checksum);
                        break;
                    case ICD_THESAURUS:
                        result = importIcdThesaurus(pFile, checksum, year);
                        break;
                    case OPS_THESAURUS:
                        result = importOpsThesaurus(pFile, checksum, year);
                        break;
                    case ICD_TRANSFER:
                    case OPS_TRANSFER:
                         result = importIcdOpsTransfer(pFile,checksum, catalogType);
                        break;
                    case OPS_AOP:
                    {
                        try {
                            result = importOpsAop(pFile, checksum, year);
                        } catch (Exception ex) {
                            Logger.getLogger(CatalogImportHelperBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    default:
                        LOG.log(Level.WARNING, "Cannot process this catalog type: " + catalogType);
                        break;
                }
            } else {
                LOG.log(Level.FINE, "Was not able to detect type of this file (catalogType is null): " + pFile.getAbsolutePath());
            }
            return new AsyncResult<>(result);
            //return true;
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Something went wrong while importing catalog file " + pFile.getAbsolutePath(), ex);
            return new AsyncResult<>(result);
        }
    }

    /*
  @TransactionAttribute(REQUIRES_NEW)
  @PermitAll
  private CCountry getCountry() {
    if (country == null && countryDao != null) {
      CCountry c = countryDao.getCountry(country_short_name);
      if (c == null) {
        c = new CCountry();
        c.setShortName(country_short_name);
        c.setName(country_name);
        countryDao.persist(c);
      }
      country = c;
    }
    return country;
  }
     */
 /*
  @TransactionAttribute(REQUIRES_NEW)
  @PermitAll
  private CCatalog getCatalog(final String pModelId) {
    CCatalog catalog = catalogDao.getCatalog(pModelId, getCountry().getShortName());
    if (catalog == null) {
      catalog = new CCatalog();
      catalog.setCCountry(country);
      catalog.setModelIdEn(String.valueOf(pModelId));
      catalogDao.persist(catalog);
    }
    return catalog;
  }
     */
 /*
  @TransactionAttribute(REQUIRES_NEW)
  @PermitAll
  private CMdcSkCatalog getMdcCatalog(final String pMdcIdent, final String pMdcDescription) {
    CMdcSkCatalog mdcCatalog = mdcSkCatalogDao.getMdcCatalog(pMdcIdent, getCountry().getShortName());
    if (mdcCatalog == null) {
      mdcCatalog = new CMdcSkCatalog();
      mdcCatalog.setCCountry(country);
      mdcCatalog.setMdcSkIdent(pMdcIdent);
      mdcCatalog.setMdcSkDescription(pMdcDescription);
      mdcSkCatalogDao.persist(mdcCatalog);
    }
    return mdcCatalog;
  }
     */
    private static Date getFirstDay(final int pYear) {
        Calendar calValidFrom = Calendar.getInstance();
        calValidFrom.set(Calendar.YEAR, pYear);
        calValidFrom.set(Calendar.MONTH, Calendar.JANUARY);
        calValidFrom.set(Calendar.DAY_OF_MONTH, 1);
        calValidFrom.set(Calendar.HOUR_OF_DAY, 0);
        calValidFrom.set(Calendar.MINUTE, 0);
        calValidFrom.set(Calendar.SECOND, 0);
        calValidFrom.set(Calendar.MILLISECOND, 0);
        return calValidFrom.getTime();
    }

    private static Date getLastDay(final int pYear) {
        Calendar calValidTo = Calendar.getInstance();
        calValidTo.setTime(getFirstDay(pYear + 1));
        calValidTo.add(Calendar.SECOND, -1); //Creates YEAR-12-31 through substraction of 1ms
        return calValidTo.getTime();
    }

    private static int getSize(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof String[]) {
            return ((String[]) obj).length;
        }
        if (obj instanceof String[][]) {
            return ((String[][]) obj).length;
        }
        if (obj instanceof CpxFileReader) {
            try {
                return ((CpxFileReader) obj).getLineCount();
            } catch (IOException ex) {
                LOG.log(Level.FINEST, "Cannot read line count", ex);
                return -1;
            }
        }
        if(obj instanceof AopCatalogReader){
            return ((AopCatalogReader)obj).getEntriesCount();
        }
        if (obj instanceof COCodeList) {
            return ((COCodeList) obj).getCodes().size();
        }
        if (obj instanceof Map) {
            return ((Map) obj).size();
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        return 0;
    }

    private static void startImport(final File pFile, final Object pCatalogData) {
        LOG.log(Level.INFO, getSize(pCatalogData) + " entries found in catalog " + pFile.getName());
    }

    private static void finishImport(final File pFile, final Object pCatalogData) {
        LOG.log(Level.INFO, "Catalog " + pFile.getName() + " successfully imported");
    }

    private static void skipImport(final File pFile) {
        LOG.log(Level.INFO, "Catalog " + pFile.getName() + " already exists, skip file import");
    }

    private static void progressImport(final File pFile, Object pCatalogData, final int pNumber, final AbstractCommonDao<? extends AbstractEntity> pDao) {
        //Print progress information not so frequently!
        int step = 5000;
        if (pNumber % step != 0) {
            return;
        }
        if (pNumber == 0) {
            return;
        }
        //2018-03-02 DNi - Ticket CPX-856: clear hibernate's session cache from time to time! Otherwise all persisted entities will stay in memory!
        if (pDao != null) {
            pDao.flush(); //don't forget this before clearing the cache!
            pDao.clear();
            //pDao.flushAndClear(); might be an alternative!
        }
        LOG.log(Level.INFO, "Import of " + pFile.getName() + " is still running and has imported " + pNumber + " of " + getSize(pCatalogData) + " entries");
    }

    //Import DRGs
    public CatalogImportResult importDrg(final File pFile, final String pChecksum, final int pYear) {
        CatalogTypeEn catalogType = CatalogTypeEn.DRG;
        //if (drgCatalogDao.getEntryCounter(pYear, countryEn) <= 0) {
        //if (!drgCatalogDao.catalogExists(pChecksum)) {
        if (drgCatalogDao.catalogExists(pYear, countryEn)) {
            skipImport(pFile);
// check, whether there is a drgm file on default path
            if (!catalogExport.checkDrgmFileExists(catalogType, pYear)) {
                catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
            }
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        //final String modelId = getDrgModelId(pYear);
        //final String category = "DRG";
        //final String suppType = "0";

        Map<String, COMDC> catalogData = unserialize(pFile);
        if (catalogData == null) {
            LOG.log(Level.SEVERE, "Catalog data is null!");
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog data is null!");
        }
        startImport(pFile, catalogData);

        final Date validFrom = getFirstDay(pYear);
        final Date validTo = getLastDay(pYear);

        for (Map.Entry<String, COMDC> mapEntry : catalogData.entrySet()) {
            //String key = mapEntry.getKey();
            COMDC data = mapEntry.getValue();
            //CCatalog catalog = getCatalog(modelId);

            for (COCode tmp : emptyList(data.getChilds())) {
                COADRG adrg = (COADRG) tmp;

                for (COCode tmp2 : adrg.getChilds()) {
                    CODRG drg = (CODRG) tmp2;
                    CDrgCatalog drgCatalog = new CDrgCatalog();

                    drgCatalog.setCountryEn(countryEn);
                    drgCatalog.setDrgYear(pYear);
                    drgCatalog.setChecksum(pChecksum);
                    drgCatalog.setDrgIsNegotiatedFl(drg.verhandelbar);
                    drgCatalog.setDrgIsDayCareFl(drg.teilstationaer);
                    drgCatalog.setDrgDrg(drg.m_code);
                    drgCatalog.setDrgDescription(drg.getText());
                    drgCatalog.setDrgPartitionEn(DrgPartitionEn.valueOf(String.valueOf(drg.partition)));
                    GrouperMdcOrSkEn grouperMdcOrSkEn = null;
                    String mdc = drg.getMDC();
                    if (mdc != null && !mdc.isEmpty() && !mdc.equalsIgnoreCase("ERR")) {
                        grouperMdcOrSkEn = GrouperMdcOrSkEn.findById(mdc);
                        if (grouperMdcOrSkEn == null) {
                            LOG.log(Level.WARNING, "MDC or SK not found in the enumeration GrouperMdcOrSk: " + mdc);
                        }
                        //try {
                        //    grouperMdcOrSkEn = GrouperMdcOrSkEn.findById(mdc);
                        //} catch (CpxIllegalArgumentException e) {
                        //    LOG.log(Level.WARNING, "SK not found in the enumeration GrouperMdcOrSk: " + mdc, e);
                        //}
                    }
                    drgCatalog.setCMdcSkCatalog(grouperMdcOrSkEn);
                    if (!drg.verhandelbar) {
                        drgCatalog.setDrgValidFrom(validFrom);
                        drgCatalog.setDrgValidTo(validTo);
                    }

                    //Erbringungsart HA
                    drgCatalog.setDrgMd1DeductionDay(drg.HA_ersterTagMitAbschlag);
                    drgCatalog.setDrgMdCwDeduction(BigDecimal.valueOf(drg.HA_cwAbschlagJeTag));
                    drgCatalog.setDrgMd1SurchargeDay(drg.HA_ErsterTagMitZuschlag);
                    drgCatalog.setDrgMdCwSurcharge(BigDecimal.valueOf(drg.HA_cwZuschlagJeTag));
                    drgCatalog.setDrgMdCwTransfDeduct(BigDecimal.valueOf(drg.HA_cwAbchlagVerlegungJeTag));
                    drgCatalog.setDrgMdIsTransferFl(drg.HA_VerlegungsPauschale);
                    drgCatalog.setDrgMdIsReadmFl(drg.HA_WiederaufnahmeAusnahme);
                    drgCatalog.setDrgMdAlos(BigDecimal.valueOf(drg.mittlereVWD));

                    drgCatalog.setDrgMdCw(BigDecimal.valueOf(drg.HA_cw));

                    drgCatalog.setDrgMdMCw(BigDecimal.valueOf(drg.HA_cwHebamme));

                    //Erbringungsart BA
                    drgCatalog.setDrgEo1DeductionDay(drg.BA_ErsterTagMitAbschlag);
                    drgCatalog.setDrgEoCwDeduction(BigDecimal.valueOf(drg.BA_cwAbschlagJeTag));
                    drgCatalog.setDrgEo1SurchargeDay(drg.BA_ErsterTagMitZuschlag);
                    drgCatalog.setDrgEoCwSurcharge(BigDecimal.valueOf(drg.BA_cwZuschlagJeTag));
                    drgCatalog.setDrgEoCwTransfDeduct(BigDecimal.valueOf(drg.BA_cwAbchlagVerlegungJeTag));
                    drgCatalog.setDrgEoIsTransferFl(drg.BA_VerlegungsPauschale);
                    drgCatalog.setDrgEoIsReadmFl(drg.BA_WiederaufnahmeAusnahme);
                    drgCatalog.setDrgEoAlos(BigDecimal.valueOf(drg.mittlereVWD_BA));

                    drgCatalog.setDrgEoCw(BigDecimal.valueOf(drg.BA_cwOperateur));
                    drgCatalog.setDrgEoaCw(BigDecimal.valueOf(drg.BA_cwOperateurAnaesthesist));
                    drgCatalog.setDrgEomCw(BigDecimal.valueOf(drg.BA_cwOperateurHebamme));
                    drgCatalog.setDrgEoamCw(BigDecimal.valueOf(drg.BA_cwOperateurAnaesthesistHebamme));

                    drgCatalog.setDrgNegoDayFee(0D);
                    drgCatalog.setDrgMdMedianCaseCount(drg.HA_medialCaseCount);
                    drgCatalog.setDrgEoMedianCaseCount(drg.BA_medialCaseCount);
                    drgCatalog.setDrgMdCareCwDay(BigDecimal.valueOf(drg.HA_pflegecw_tag));
                    drgCatalog.setDrgEoCareCwDay(drg.BA_pflegecw_tag == 0 && drg.HA_pflegecw_tag != 0?BigDecimal.valueOf(drg.HA_pflegecw_tag):BigDecimal.valueOf(drg.BA_pflegecw_tag));
                    drgCatalogDao.persist(drgCatalog);
                }
            }
        }
        finishImport(pFile, catalogData);
        catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
        return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), catalogData.entrySet().size(), "");
    }

    public int dropZusatz(final int pYear, SupplFeeTypeEn supplType) {
        return supplementaryFeeDao.dropEntries(pYear, countryEn, supplType.name());
    }

    /*
  @PermitAll
  public int getZusatzCount(final int pYear, SupplFeeType supplType) {
    final String suppType = supplType.name();
    return supplementaryFeeDao.getEntryCounter(pYear, countryEn,  suppType);
  }
     */
    //Import DRG Supplementary Fee Keys
    public CatalogImportResult importZusatz(final File pFile, final String pChecksum, final int pYear) {
        CatalogTypeEn catalogType = CatalogTypeEn.ZE;
        final SupplFeeTypeEn suppType = SupplFeeTypeEn.ZE;
        //if (getZusatzCount(pYear, suppType) <= 0) {
        //if (!supplementaryFeeDao.catalogExists(pChecksum)) {
        if (supplementaryFeeDao.catalogExists(pYear, countryEn, suppType.name())) {
            skipImport(pFile);
// check, whether there is a drgm file on default path
            if (!catalogExport.checkDrgmFileExists(catalogType, pYear)) {
                catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
            }
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
//      final String modelId = getDrgModelId(pYear);

//final SupplFeeType suppType = SupplFeeType.ZE;
        Map<String, COZusatz> catalogData = unserialize(pFile);
        if (catalogData == null) {
            LOG.log(Level.SEVERE, "Catalog data is null!");
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog data is null!");
        }
        startImport(pFile, catalogData);

        Date validFrom = getFirstDay(pYear);
        Date validTo = getLastDay(pYear);

        Map<String, Double> values = new HashMap<>();

        for (Map.Entry<String, COZusatz> mapEntry : catalogData.entrySet()) {
            //String key = mapEntry.getKey();
            COZusatz data = mapEntry.getValue();

            //System.out.println(Arrays.toString(catalogData.entrySet().toArray()));
            for (COCode tmp : emptyList(data.getChilds())) {
                COCodeProc proc = (COCodeProc) tmp;

                CSupplementaryFee suppFee = new CSupplementaryFee();
                suppFee.setCountryEn(countryEn);
                suppFee.setSupplTypeEn(suppType);
                suppFee.setSupplYear(pYear);
                suppFee.setChecksum(pChecksum);
                suppFee.setSupplKey(data.m_code);
                suppFee.setSupplDefinition(proc.getText());
                suppFee.setSupplOpsCode(proc.m_code);
                double value = proc.m_value;
                //if (value == 0.0d) {
                if (Double.doubleToRawLongBits(value) == Double.doubleToRawLongBits(0.0D)) {
                    Double valueTmp = values.get(suppFee.getSupplKey());
                    value = (valueTmp == null) ? 0.0D : valueTmp;
                } else {
                    values.put(suppFee.getSupplKey(), value);
                }
                suppFee.setSupplValue(value);
                if (!data.m_isVerhandelt) {
                    suppFee.setSupplValidFrom(validFrom);
                    suppFee.setSupplValidTo(validTo);
                }
                suppFee.setSupplNegotiated(data.m_isVerhandelt);
                supplementaryFeeDao.persist(suppFee);
            }
        }
        finishImport(pFile, catalogData);
        catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
        return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), catalogData.entrySet().size(), "");
    }

    /*
  private void importIcd(final File pFile, final String pModelId, final int pYear, final String pCategory, final String pSuppType) {
    if (!icdCatalogDao.catalogExists(pModelId)) {
      COCodeList catalogData = (COCodeList) unserialize(pFile);
    }
  }

  private void importOps(final File pFile, final String pModelId, final int pYear, final String pCategory, final String pSuppType) {
    if (!opsCatalogDao.catalogExists(pModelId)) {
      COCodeList catalogData = (COCodeList) unserialize(pFile);
    }
  }
     */
    public int dropPepp(final int pYear) {

        return peppCatalogDao.dropEntries(pYear, countryEn);
    }

    /*
  @PermitAll
  public int getPeppCount(final int pYear) {

    return peppCatalogDao.getEntryCounter(pYear, countryEn);
  }
     */
    //Import PEPPs
    public CatalogImportResult importPepp(final File pFile, final String pChecksum, final int pYear) {
        CatalogTypeEn catalogType = CatalogTypeEn.PEPP;
        //if (getPeppCount(pYear) <= 0) {
        //if (!peppCatalogDao.catalogExists(pChecksum)) {
        if (peppCatalogDao.catalogExists(pYear, countryEn)) {
            skipImport(pFile);
// check, whether there is a drgm file on default path
            if (!catalogExport.checkDrgmFileExists(catalogType, pYear)) {
                catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
            }
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        Map<String, COPEPPStruktur> catalogData = unserialize(pFile);
        if (catalogData == null) {
            LOG.log(Level.SEVERE, "Catalog data is null!");
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog data is null!");
        }
        startImport(pFile, catalogData);

        Date validFrom = getFirstDay(pYear);
        Date validTo = getLastDay(pYear);

        for (Map.Entry<String, COPEPPStruktur> mapEntry : catalogData.entrySet()) {
            //String key = mapEntry.getKey();
            COPEPPStruktur data = mapEntry.getValue();

            for (COCode tmp : emptyList(data.getChilds())) {
                COPEPP pepp = (COPEPP) tmp;

                if (data.getType() == null) {
                    continue;
                }
                GrouperMdcOrSkEn grouperMdcOrSkEn = null;
                String mdc = pepp.getPeppSK();
                if (mdc != null && !mdc.isEmpty() && !mdc.equalsIgnoreCase("ERR")) {
                    grouperMdcOrSkEn = GrouperMdcOrSkEn.findById(mdc);
                    if (grouperMdcOrSkEn == null) {
                        LOG.log(Level.WARNING, "MDC or SK not found in the enumeration GrouperMdcOrSk: " + mdc);
                    }
//                            try {
//                                grouperMdcOrSkEn = GrouperMdcOrSkEn.findById(mdc);
//                            } catch (CpxIllegalArgumentException e) {
//                                LOG.log(Level.WARNING, "SK not found in the enumeration GrouperMdcOrSk: " + mdc, e);
//                            }
                }

                Date valFrom = null;
                Date valTo = null;
                if (!pepp.verhandelbar) {
                    valFrom = validFrom;
                    valTo = validTo;
                }
                ArrayList<Double> dayValues = pepp.getDayValues();
                if (pYear > 2014) {
                    if (dayValues != null && !dayValues.isEmpty()) {
                        int i = 1;
                        for (Double dayValue : dayValues) {
                            setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                    i, dayValue, i, i == dayValues.size() ? 0 : i, true);
                            i++;
                        }

                    } else {
// negotiated without data
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                1, 0, -1, -1, true);
                    }
                } else {
//if dayValues isEmpty we have pepp structure with relations
                    if (pepp.vwd1_von < 0) {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                1, pepp.bewertungsrelation1_jetag, -1, -1, false);
                        continue;
                    } else {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                1, pepp.bewertungsrelation1_jetag, pepp.vwd1_von, pepp.vwd1_bis, false);

                    }
                    if (pepp.vwd2_von < 0) {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                2, pepp.bewertungsrelation2_jetag, -1, -1, false);
                        continue;

                    } else {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                2, pepp.bewertungsrelation2_jetag, pepp.vwd2_von, pepp.vwd2_bis, false);
                    }
                    if (pepp.vwd3_von < 0) {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                3, pepp.bewertungsrelation3_jetag, -1, -1, false);
                        continue;
                    } else {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                3, pepp.bewertungsrelation3_jetag, pepp.vwd3_von, pepp.vwd3_bis, false);
                    }
                    if (pepp.vwd4_von < 0) {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                4, pepp.bewertungsrelation4_jetag, -1, -1, false);
                        continue;
                    } else {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                4, pepp.bewertungsrelation4_jetag, pepp.vwd4_von, pepp.vwd4_bis, false);
                    }
                    if (pepp.vwd5_von < 0) {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                5, pepp.bewertungsrelation5_jetag, -1, -1, false);
                    } else {
                        setValues4OnePeppCatalogValue(pepp, valFrom, valTo, pChecksum, pYear, grouperMdcOrSkEn,
                                5, pepp.bewertungsrelation5_jetag, pepp.vwd5_von, pepp.vwd5_bis, false);
                    }

                }
            }
        }
        finishImport(pFile, catalogData);
        catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
        return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), catalogData.entrySet().size(), "");
    }

    private void setValues4OnePeppCatalogValue(COPEPP pepp,
            Date valFrom, Date valTo,
            final String pChecksum, int pYear, GrouperMdcOrSkEn grouperMdcOrSkEn,
            int pos, double cw, int from, int to, boolean hasClasses) {
        CPeppCatalog peppCatalog = new CPeppCatalog();
        peppCatalog.setPeppHasClassesFl(hasClasses);
        peppCatalog.setPeppIsNegotiatedFl(pepp.verhandelbar);
        peppCatalog.setPeppIsDayCareFl(COPEPP.Behandlungsform.TEILSTATIONAER.equals(pepp.behandlungsform));
        peppCatalog.setCountryEn(countryEn);
        peppCatalog.setChecksum(pChecksum);
        peppCatalog.setPeppPepp(pepp.m_code);
        peppCatalog.setPeppValidFrom(valFrom);
        peppCatalog.setPeppValidTo(valTo);
        peppCatalog.setPeppYear(pYear);
        peppCatalog.setPeppDescription(pepp.getText());
        peppCatalog.setCMdcSkCatalog(grouperMdcOrSkEn);
        peppCatalog.setPeppRelationNumber(pos);
        peppCatalog.setPeppRelationCostWeight(BigDecimal.valueOf(cw));
        peppCatalog.setPeppRelationFrom(from);
        peppCatalog.setPeppRelationTo(to);
        peppCatalogDao.persist(peppCatalog);
    }

    public int dropPeppZusatz(final int pYear) {
        return supplementaryFeeDao.dropEntries(pYear, countryEn, SupplFeeTypeEn.ZP.name());
    }

    /*
  @PermitAll
  public int getPeppZusatzCount(final int pYear) {
    return supplementaryFeeDao.getEntryCounter(pYear, countryEn, SupplFeeType.ZP.name());
  }
     */
    //Import PEPP Supplementary Fee Keys
    public CatalogImportResult importPeppZusatz(final File pFile, final String pChecksum, final int pYear) {
        CatalogTypeEn catalogType = CatalogTypeEn.ZP;
        final SupplFeeTypeEn suppType = SupplFeeTypeEn.ZP;
        //if (getZusatzCount(pYear, suppType) <= 0) {
        //if (!supplementaryFeeDao.catalogExists(pChecksum)) {
        if (supplementaryFeeDao.catalogExists(pYear, countryEn, suppType.name())) {
            skipImport(pFile);
// check, whether there is a drgm file on default path
            if (!catalogExport.checkDrgmFileExists(catalogType, pYear)) {
                catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
            }
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        Map<String, COCode> catalogData = unserialize(pFile);
        if (catalogData == null) {
            LOG.log(Level.SEVERE, "Catalog data is null!");
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog data is null!");
        }
        startImport(pFile, catalogData);

        Date validFrom = getFirstDay(pYear);
        Date validTo = getLastDay(pYear);

        for (Map.Entry<String, COCode> mapEntry : catalogData.entrySet()) {
            //String key = mapEntry.getKey();
            if (mapEntry.getValue() instanceof COPEPP) {
                //weighted fees
                COPEPP data = (COPEPP) mapEntry.getValue();

                for (COCode tmp : emptyList(data.getChilds())) {
                    COZusatz zusatz = (COZusatz) tmp;

                    if (zusatz == null) {
                        continue;
                    }

                    for (COCode tmp2 : emptyList(zusatz.getChilds())) {
                        COCodeProc proc = (COCodeProc) tmp2;

                        CSupplementaryFee suppFee = new CSupplementaryFee();
                        suppFee.setSupplTypeEn(suppType);
                        suppFee.setSupplYear(pYear);
                        suppFee.setChecksum(pChecksum);
                        suppFee.setCountryEn(countryEn);
                        suppFee.setSupplKey(zusatz.m_code);
                        suppFee.setSupplDefinition(zusatz.getText());
                        suppFee.setSupplOpsCode(proc.m_code);
                        suppFee.setSupplValue(zusatz.m_value);
                        if (!data.verhandelbar) {
                            suppFee.setSupplValidFrom(validFrom);
                            suppFee.setSupplValidTo(validTo);
                        }
                        suppFee.setSupplNegotiated(data.verhandelbar);
                        supplementaryFeeDao.persist(suppFee);
                    }
                }
            }

            if (mapEntry.getValue() instanceof COZusatz) {
                //unweighted fees
                COZusatz data = (COZusatz) mapEntry.getValue();

                for (COCode tmp : emptyList(data.getChilds())) {
                    if (tmp instanceof COCodeProc) {
                        COCodeProc proc = (COCodeProc) tmp;

                        CSupplementaryFee suppFee = new CSupplementaryFee();

                        suppFee.setSupplTypeEn(suppType);
                        suppFee.setSupplYear(pYear);
                        suppFee.setCountryEn(countryEn);
                        suppFee.setChecksum(pChecksum);
                        suppFee.setSupplKey(data.m_code);
                        suppFee.setSupplDefinition(data.getText());
                        suppFee.setSupplOpsCode(proc.m_code);
                        suppFee.setSupplValue(proc.m_value);
                        if (!data.m_isVerhandelt) {
                            suppFee.setSupplValidFrom(validFrom);
                            suppFee.setSupplValidTo(validTo);
                        }
                        suppFee.setSupplNegotiated(data.m_isVerhandelt);
                        supplementaryFeeDao.persist(suppFee);
                        continue;
                    }
                    COZusatz zusatz = (COZusatz) tmp;

                    for (COCode tmp2 : emptyList(zusatz.getChilds())) {
                        COCodeProc proc = (COCodeProc) tmp2;
                        CSupplementaryFee suppFee = new CSupplementaryFee();

                        suppFee.setSupplTypeEn(suppType);
                        suppFee.setSupplYear(pYear);
                        suppFee.setCountryEn(countryEn);
                        suppFee.setChecksum(pChecksum);
                        suppFee.setSupplKey(zusatz.m_code);
                        suppFee.setSupplDefinition(zusatz.getText());
                        suppFee.setSupplOpsCode(proc.m_code);
                        suppFee.setSupplValue(proc.m_value);
                        if (!data.m_isVerhandelt) {
                            suppFee.setSupplValidFrom(validFrom);
                            suppFee.setSupplValidTo(validTo);
                        }
                        suppFee.setSupplNegotiated(data.m_isVerhandelt);
                        supplementaryFeeDao.persist(suppFee);
                    }
                }
            }
        }
        finishImport(pFile, catalogData);
        catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
        return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), catalogData.entrySet().size(), "");
    }

    public int dropEtZusatz(final int pYear) {
        return supplementaryFeeDao.dropEntries(pYear, countryEn, SupplFeeTypeEn.ET.name());
    }

    public int dropIcd(final int pYear) {
        return icdCatalogDao.dropEntries(pYear, countryEn);
    }

    public int dropOps(final int pYear) {
        return opsCatalogDao.dropEntries(pYear, countryEn);
    }

    /*
  @PermitAll
  public int getEtZusatzCount(final int pYear) {
     return getZusatzCount(pYear,  SupplFeeType.ET);
  }
     */
    //Import Daily Fees Keys
    public CatalogImportResult importEtZusatz(final File pFile, final String pChecksum, final int pYear) {
        CatalogTypeEn catalogType = CatalogTypeEn.ET;
        final SupplFeeTypeEn suppType = SupplFeeTypeEn.ET;
        //if (getEtZusatzCount(pYear) <= 0) {
        //if (!supplementaryFeeDao.catalogExists(pChecksum)) {
        if (supplementaryFeeDao.catalogExists(pYear, countryEn, suppType.name())) {
            skipImport(pFile);
// check, whether there is a drgm file on default path
            if (!catalogExport.checkDrgmFileExists(catalogType, pYear)) {
                catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
            }
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        Map<String, COEtZusatz> catalogData = unserialize(pFile);
        if (catalogData == null) {
            LOG.log(Level.SEVERE, "Catalog data is null!");
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog data is null!");
        }
        startImport(pFile, catalogData);

        Date validFrom = getFirstDay(pYear);
        Date validTo = getLastDay(pYear);

        for (Map.Entry<String, COEtZusatz> mapEntry : catalogData.entrySet()) {
            String key = mapEntry.getKey();
            COEtZusatz data = mapEntry.getValue();

            for (COCode tmp : emptyList(data.getChilds())) {
                if (tmp instanceof COCodeProc) {
                    continue;
                }

                COEtZusatz zusatz = (COEtZusatz) tmp;

                for (COCode tmp2 : zusatz.getChilds()) {
                    COCodeProc proc = (COCodeProc) tmp2;
                    CSupplementaryFee suppFee = new CSupplementaryFee();
                    suppFee.setCountryEn(countryEn);
                    suppFee.setSupplTypeEn(suppType);
                    suppFee.setChecksum(pChecksum);
                    suppFee.setSupplYear(pYear);
                    suppFee.setSupplKey(zusatz.m_code);
                    suppFee.setSupplDefinition(zusatz.getText());
                    suppFee.setSupplOpsCode(proc.m_code);
                    suppFee.setSupplCwValue(proc.m_value);
                    if (!data.m_isVerhandelt) {
                        suppFee.setSupplValidFrom(validFrom);
                        suppFee.setSupplValidTo(validTo);
                    }
                    suppFee.setSupplNegotiated(data.m_isVerhandelt);
                    supplementaryFeeDao.persist(suppFee);
                }
            }
        }
        finishImport(pFile, catalogData);
        catalogExport.exportCatalog(catalogType, pYear, countryEn.name());
        return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), catalogData.entrySet().size(), "");
    }

    @SuppressWarnings("unchecked")
    private <T extends CTransferCatalogDao> T getDao( CatalogTypeEn pType){
        switch(pType){
            case ICD_TRANSFER:
                return (T) icdTransferCatalogDao;
            case OPS_TRANSFER:
                return (T) opsTransferCatalogDao;

            default:
                LOG.log(Level.WARNING, "Unknown catalog type");
                return null;
        }
        
    }
    
    private CatalogImportResult importIcdOpsTransfer(File pFile,String pChecksum, CatalogTypeEn pType)throws IOException {
        
        int[]years = CatalogTypeEn.detectSrcAndDestYears(pFile);
        if(years == null ){
            return new CatalogImportResult(false, pFile, pType, 0, pChecksum, 0, 0, "the years for transfer are not valid");
        }
        CTransferCatalogDao <CTransferCatalog> dao = getDao(pType); 
        if(dao == null){
            return new CatalogImportResult(false, pFile, pType, 0, pChecksum, 0, 0, "could not find dao to catalog type");
        }
        if (dao.catalogExists(years[0], years[1])) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, pType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
       try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            dao.dropEntries(years[0], years[1]); 
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray();
                CTransferCatalog catalog = CTransferCatalog.getTypeInstance(pType);
                if(!catalog.fillFromLine(pChecksum, line, years[0], years[1])){
                    LOG.log(Level.INFO, "line {0} was not imported", i);
                    continue;
                }

                dao.persist(catalog);
                progressImport(pFile, fileReader, i, dao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, pType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import ICDs
    public CatalogImportResult importIcd(final File pFile, final String pChecksum, final int pYear) {
        CatalogTypeEn catalogType = CatalogTypeEn.ICD;
        //if (icdCatalogDao.getEntryCounter(pYear, countryEn) <= 0) {
        //if (!icdCatalogDao.catalogExists(pChecksum)) {
        if (icdCatalogDao.catalogExists(pYear, countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        COCodeList catalogData = unserialize(pFile);
        if (catalogData == null) {
            LOG.log(Level.SEVERE, "Catalog data is null!");
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog data is null!");
        }
        startImport(pFile, catalogData);

        for (COCode entry : catalogData.getCodes()) {
            //String key = mapEntry.getKey();
            //COEtZusatz data = mapEntry.getValue();
            //CCatalog catalog = getCatalog(modelId);
            saveIcdCatalog(pChecksum, pYear, entry, null, 0);
        }
        finishImport(pFile, catalogData);
        return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), catalogData.getCodes().size(), "");
    }

    private void saveIcdCatalog(final String pChecksum, final int pYear, final COCode entry, final CIcdCatalog parentIcd, final int pDepth) {
        if (entry == null || entry.m_code == null || entry.m_code.trim().isEmpty()) {
            return;
        }

        CIcdCatalog icdCatalog = new CIcdCatalog();
        //icdCatalog.setCCatalog(catalog);
        icdCatalog.setCountryEn(countryEn);
        icdCatalog.setIcdYear(pYear);
        icdCatalog.setChecksum(pChecksum);
        icdCatalog.setIcdCode(entry.m_code);
        icdCatalog.setIcdDescription(entry.getText());
        icdCatalog.setIcdExclusion(entry.m_exclusiva);
        icdCatalog.setIcdInclusion(entry.m_inclusiva);
        icdCatalog.setIcdIsCompleteFl(!entry.m_hasChild);
        icdCatalog.setIcdNote(entry.m_hinweis);
        icdCatalog.setCIcdCatalog(parentIcd);
        icdCatalog.setIcdDepth(pDepth);
        icdCatalogDao.persist(icdCatalog);

        if (entry.m_hasChild) {
            for (COCode entry2 : entry.getChilds()) {
                saveIcdCatalog(pChecksum, pYear, entry2, icdCatalog, (pDepth + 1));
            }
        }
    }

    //Import OPS'
    public CatalogImportResult importOps(final File pFile, final String pChecksum, final int pYear) {
        CatalogTypeEn catalogType = CatalogTypeEn.OPS;
        //if (opsCatalogDao.getEntryCounter(pYear, countryEn) <= 0) {
        //if (!opsCatalogDao.catalogExists(pChecksum)) {
        if (opsCatalogDao.catalogExists(pYear, countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        COCodeList catalogData = unserialize(pFile);
        if (catalogData == null) {
            LOG.log(Level.SEVERE, "Catalog data is null!");
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog data is null!");
        }
        startImport(pFile, catalogData);

        for (COCode tmp : catalogData.getCodes()) {
            COCodeProc entry = (COCodeProc) tmp;

            //String key = mapEntry.getKey();
            //COEtZusatz data = mapEntry.getValue();
            //CCatalog catalog = getCatalog(modelId);
            saveOpsCatalog(pChecksum, pYear, entry, null, 0);
        }
        finishImport(pFile, catalogData);
        return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), catalogData.getCodes().size(), "");
    }

    private void saveOpsCatalog(final String pChecksum, final int pYear, final COCodeProc entry, final COpsCatalog parentIcd, final int pDepth) {
        if (entry == null || entry.m_code == null || entry.m_code.trim().isEmpty()) {
            return;
        }

        COpsCatalog opsCatalog = new COpsCatalog();
        //icdCatalog.setCCatalog(catalog);
        opsCatalog.setCountryEn(countryEn);
        opsCatalog.setOpsYear(pYear);
        opsCatalog.setChecksum(pChecksum);
        opsCatalog.setOpsCode(entry.m_code);
        if (entry.m_code.length() > 14) {
            LOG.log(Level.INFO, entry.m_code);

        }
        opsCatalog.setOpsDescription(entry.getText());
        opsCatalog.setOpsExclusion(entry.m_exclusiva);
        opsCatalog.setOpsInclusion(entry.m_inclusiva);
        opsCatalog.setOpsIsCompleteFl(!entry.m_hasChild);
        opsCatalog.setOpsNote(entry.m_hinweis);
        opsCatalog.setCOpsCatalog(parentIcd);
        opsCatalog.setOpsDepth(pDepth);
        opsCatalogDao.persist(opsCatalog);
        if (entry.m_hasChild) {
            for (COCode tmp : entry.getChilds()) {
                COCodeProc entry2 = (COCodeProc) tmp;
                saveOpsCatalog(pChecksum, pYear, entry2, opsCatalog, (pDepth + 1));
            }
        }
    }

    /*
  public String[][] getLinesOfFile(final File pFile) throws IOException {
    List<String[]> list = new LinkedList<>();
    int maxArrSize = 0;
    try(BufferedReader br = new BufferedReader(new FileReader(pFile))) {
      //StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        line = line.trim();
        if (line.isEmpty()) {
          continue;
        }
        String[] tmp = line.split(";");
        for(int i = 0; i < tmp.length; i++) {
          tmp[i] = tmp[i].trim();
        }
        
        if (tmp.length > maxArrSize) {
          maxArrSize = tmp.length;
        }
        
        list.add(tmp);
        line = br.readLine();
      }
      //String everything = sb.toString();
    }
    
    //Make sure, that all entries have to same array length
    for(int i = 0; i < list.size(); i++) {
      String[] tmp = list.get(i);
      if (tmp == null || tmp.length < maxArrSize) {
        String[] tmp2 = new String[maxArrSize];
        Arrays.fill(tmp2, "");
        if (tmp != null) {
          System.arraycopy(tmp, 0, tmp2, 0, tmp.length );
        }
        list.set(i, tmp2);
      }
    }
    
    String[][] array = new String[list.size()][];
    list.toArray(array);
    return array;
  }
     */
    //Import Hospitals
    public CatalogImportResult importHospital(final File pFile, final String pChecksum) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.HOSPITAL;
        //String[][] lines = getLinesOfFile(pFile);
        //if (!hospitalDao.catalogExists(pChecksum)) {
        if (hospitalDao.catalogExists(countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        //if (hospitalDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            hospitalDao.dropEntries(countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArrayRespectingQuotes();
                try {
                    CHospital hospital = new CHospital();
                    hospital.setCountryEn(countryEn);
                    hospital.setChecksum(pChecksum);
                    hospital.setHosAddress(line[6]);
                    hospital.setHosIdent(line[0]);
                    hospital.setHosIdentClassEn(IdentClassEn.getStaticEnum(line[0]));
                    hospital.setHosName(line[3]);
                    hospital.setHosZipCode(line[7]);
                    hospital.setHosCity(line[8]);
                    hospital.setStateEn(line[4]);
                    hospitalDao.persist(hospital);
                } catch (CpxIllegalArgumentException ex) {
                    LOG.log(Level.SEVERE, "Line seems to be corrupted: " + String.join(";", line), ex);
                }
                progressImport(pFile, fileReader, i, hospitalDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import Insurance Companys
    public CatalogImportResult importInsuranceCompany(final File pFile, final String pChecksum) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.INSURANCE_COMPANY;
        //String[][] lines = getLinesOfFile(pFile);
        //if (insuranceCompanyDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!insuranceCompanyDao.catalogExists(pChecksum)) {

        if (insuranceCompanyDao.catalogExists(countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            insuranceCompanyDao.dropEntries(countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArrayRespectingQuotes();
                try {
                    CInsuranceCompany insurance = new CInsuranceCompany();
                    insurance.setCountryEn(CountryEn.findById(line[3]));
                    if (insurance.getCountryEn() == null) {
                        insurance.setCountryEn(countryEn);
                    }
                    insurance.setChecksum(pChecksum);
                    insurance.setInscIdent(line[0]);
                    final String ident = insurance.getInscIdent() == null ? "" : insurance.getInscIdent().trim();
                    insurance.setInscIdentClassEn(IdentClassEn.getStaticEnum(line[0]));
                    insurance.setInscName(line[1]);
                    StateEn state = StateEn.findById(line[11]);
                    if (state == null) {
                        //Okay, no information found, try to detect with the help of 3. and 4. place of IKZ
                        if (ident.trim().length() >= 4) {
                            String ch34 = ident.substring(2, 4);
                            state = StateEn.findById(ch34);
                        }
                    }
                    insurance.setStateEn(state);
                    insurance.setInscAddress(line[2]);
                    insurance.setInscZipCode(line[4]);
                    insurance.setInscCity(line[5]);
                    insurance.setInscPhonePrefix(line[6]);
                    insurance.setInscPhone(line[7]);
                    insurance.setInscFax(line[8]);
                    final Integer changeService = toInt(line[9]); //Änderungsdienst
                    insurance.setInscChangeService(changeService);
                    Integer inscClass = toInt(line[10]);
                    if (inscClass == null) {
                        if (insurance.getInscIdentClassEn() != null) {
                            inscClass = insurance.getInscIdentClassEn().getIdInt();
                        }
                    }
                    insurance.setInscClass(inscClass);
                    Integer inscRegion = toInt(line[12]);
                    if (inscRegion == null) {
                        if (ident.trim().length() >= 3) {
                            String ch3 = ident.substring(2, 3);
                            inscRegion = toInt(ch3);
                        }
                    }
                    insurance.setInscRegion(inscRegion);
                    final Integer inscKbvIndicator = toInt(line[13]);
                    insurance.setInscKbvIndicator(inscKbvIndicator);
                    final Integer inscKbvzIndicator = toInt(line[14]);
                    insurance.setInscKbvzIndicator(inscKbvzIndicator);
                    if (line.length >= 16) {
                        String insShort = toStr(line[15]);
                        if (insShort.isEmpty()
                                && ((insurance.getInscIdentClassEn() != null && "12".equalsIgnoreCase(insurance.getInscIdentClassEn().getId())) || insurance.getInscIdent().trim().startsWith("12"))) { //second condition is somewhat redundant...
                            insShort = "BG"; //Berufsgenossenschaft
                        } else {
                            //insShort = InsShortEn.detectInsShort(insurance.getInscName());
                            insShort = InsShortDetector.detect(insurance.getInscName());
                        }
                        //insurance.setInscShort(InsShortEn.findById(insShort));
                        insurance.setInscShort(insShort);
                    }
                    //insurance.setInscShort(detectInsShort(shortingsMap, insurance));
                    //insurance.setInscAssociation(line[]);
                    insuranceCompanyDao.persist(insurance);
                } catch (CpxIllegalArgumentException ex) {
                    LOG.log(Level.SEVERE, "Line seems to be corrupted: " + String.join(";", line), ex);
                }
                progressImport(pFile, fileReader, i, insuranceCompanyDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import Departments
    public CatalogImportResult importDepartment(final File pFile, final String pChecksum) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.DEPARTMENT;
        //String[][] lines = getLinesOfFile(pFile);
        //if (departmentDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!departmentDao.catalogExists(pChecksum)) {
        if (departmentDao.catalogExists(countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            departmentDao.dropEntries(countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray();
                CDepartment department = new CDepartment();
                department.setCountryEn(countryEn);
                department.setChecksum(pChecksum);
                department.setDepKey301(line[0]);
                department.setDepDescription301(line[1]);
                departmentDao.persist(department);
                progressImport(pFile, fileReader, i, departmentDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import Doctors
    public CatalogImportResult importDoctor(final File pFile, final String pChecksum) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.DOCTOR;
        //String[][] lines = getLinesOfFile(pFile);
        //if (doctorDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!doctorDao.catalogExists(pChecksum)) {
        if (doctorDao.catalogExists(countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            doctorDao.dropEntries(countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray();
                CDoctor doctor = new CDoctor();
                doctor.setCountryEn(countryEn);
                doctor.setChecksum(pChecksum);
                doctor.setDocIdent(line[1]);
                //doctor.setDocIdentClassEn(IdentClassEn.getStaticEnum(line[1]));
                doctorDao.persist(doctor);
                progressImport(pFile, fileReader, i, doctorDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import ATC
    public CatalogImportResult importAtc(final File pFile, final String pChecksum) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.ATC;
        //String[][] lines = getLinesOfFile(pFile);
        //if (atcDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!atcDao.catalogExists(pChecksum)) {
        if (atcDao.catalogExists(countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            atcDao.dropEntries(countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray();
                CAtc atc = new CAtc();
                atc.setCountryEn(countryEn);
                atc.setChecksum(pChecksum);
                atc.setAtcCode1(line[1]);
                if (line.length >= 4) {
                    atc.setAtcCode2(line[3]);
                }
                atc.setAtcDesc(line[2]);
                atcDao.persist(atc);
                progressImport(pFile, fileReader, i, atcDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import PZN
    public CatalogImportResult importPzn(final File pFile, final String pChecksum) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.PZN;
        //String[][] lines = getLinesOfFile(pFile);
        //if (pznDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!pznDao.catalogExists(pChecksum)) {
        if (pznDao.catalogExists(countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            pznDao.dropEntries(countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray();
                CPzn pzn = new CPzn();
                pzn.setCountryEn(countryEn);
                pzn.setChecksum(pChecksum);
                pzn.setPznIdent(line[0]);
                pzn.setPznNormSize(line[1]);
                pzn.setPznDesc(line[2]);
                pznDao.persist(pzn);
                progressImport(pFile, fileReader, i, pznDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import MDK
    public CatalogImportResult importMdk(final File pFile, final String pChecksum) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.MDK;
        //String[][] lines = getLinesOfFile(pFile);
        //if (mdkDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!mdkDao.catalogExists(pChecksum)) {
        if (mdkDao.catalogExists(countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            mdkDao.dropEntries(countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray();
                CMdk mdk = new CMdk();
                mdk.setCountryEn(countryEn);
                mdk.setChecksum(pChecksum);
                mdk.setMdkInternalId(toLong(line[0]));
                mdk.setMdkName(line[1]);
                mdk.setMdkDepartment(line[2]);
                mdk.setMdkDepartmentNo(toInt(line[11]));
                mdk.setMdkStreet(line[3]);
                mdk.setMdkZipCode(line[4]);
                mdk.setMdkCity(line[5]);
                mdk.setMdkPhonePrefix(line[6]);
                mdk.setMdkPhone(line[7]);
                mdk.setMdkFax(line[8]);
                mdk.setMdkComment(line[9]);
                mdk.setMdkNotice(line[10]);
                mdk.setMdkDistrictNo(toInt(line[12]));
                mdk.setMdkValid(toBool(line[13]));
                mdk.setMdkEmail(line[14]);
                mdk.setMdkIdent(line[15]);
                mdkDao.persist(mdk);
                //progressImport(catalogType, pFile, fileReader, i);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import Baserate
    public CatalogImportResult importBaserate(final File pFile, final String pChecksum) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.BASERATE;
        //String[][] lines = getLinesOfFile(pFile);
        //if (mdkDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!baserateDao.catalogExists(pChecksum)) {
        if (baserateDao.catalogExists(countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, 0, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile)) {
            startImport(pFile, fileReader);
            baserateDao.dropEntries(countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray();
                CBaserate baserate = new CBaserate();
                baserate.setCountryEn(countryEn);
                baserate.setChecksum(pChecksum);
                baserate.setBaseHosIdent(line[0]);
                baserate.setBaseFeeKey(line[1]);
                baserate.setBaseFeeValue(toDouble(line[2], null));
                baserate.setBaseCurrency(toStr(line[3], "EUR"));
                baserate.setBaseValidFrom(toDate(line[4], null));
                baserate.setBaseValidTo(toDate(line[5], null));
                baserate.setBaseLos(toDouble(line[6]));
                baserateDao.persist(baserate);
                progressImport(pFile, fileReader, i, baserateDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, 0, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    //Import ICD Thesaurus
    public CatalogImportResult importIcdThesaurus(final File pFile, final String pChecksum, final int pYear) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.ICD_THESAURUS;
        final String referenceDelimiter = " - ";
        //String[][] lines = getLinesOfFile(pFile);
        //if (mdkDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!baserateDao.catalogExists(pChecksum)) {
        if (icdThesaurusDao.catalogExists(pYear, countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile, "UTF-8")) {
            startImport(pFile, fileReader);
            icdThesaurusDao.dropEntries(pYear, countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray("\\|");
                CIcdThesaurus icdThesaurus = new CIcdThesaurus();
                icdThesaurus.setIcdYear(pYear);
                icdThesaurus.setCountryEn(countryEn);
                icdThesaurus.setChecksum(pChecksum);
                icdThesaurus.setTypeOfCode(toInt(line[0]));
//                    if (i == 0) {
//                        //first line -> ignore UTF-8 BOM (first hidden character)
//                        icdThesaurus.setTypeOfCode(line[0].charAt(1));
//                    } else {
//                        icdThesaurus.setTypeOfCode(toInt(line[0]));
//                    }
                icdThesaurus.setDimdiInternalNo(toInt(line[1]));
                icdThesaurus.setPrintFl(toBool(line[2]));
                icdThesaurus.setPrimKeyNo1(line[3]);
                icdThesaurus.setStarKeyNo(line[4]); //.replace("*", "")
                icdThesaurus.setAddKeyNo(line[5]); //.replace("!", "")
                icdThesaurus.setPrimKeyNo2(line[6]);
                String description = line[7];
                int pos = description.indexOf(referenceDelimiter);
                if (pos > -1) {
                    icdThesaurus.setDescription(description.substring(0, pos).trim());
                    icdThesaurus.setReference(description.substring(pos + referenceDelimiter.length()).trim());
                } else {
                    icdThesaurus.setDescription(description);
                }
                icdThesaurusDao.persist(icdThesaurus);
                progressImport(pFile, fileReader, i, icdThesaurusDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    
    public CatalogImportResult importOpsAop(File pFile, String pChecksum, int pYear) throws IOException, Exception {
         CatalogTypeEn catalogType = CatalogTypeEn.OPS_AOP;
         if(pYear < 2019){
             return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "AOP - Catalog year is for 2019");
         }
         if(!pFile.getName().endsWith(".xlsx")){
             new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "AOP - Catalog is not xlsx");
         }
        if (opsAopDao.catalogExists(pYear, countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        } 
        //TODO import excel

        try(AopCatalogReader aopReader = new AopCatalogReader()){
            aopReader.readExcel(pFile.getAbsolutePath(), pYear);
            List<AopCatalogEntry> entries = aopReader.getEntries();
            startImport(pFile, aopReader);
            opsAopDao.dropEntries(pYear, countryEn);
            for(AopCatalogEntry entry: entries){
                COpsAop aop = new COpsAop();
                aop.setOpsYear(pYear);
                aop.setOpsCode(entry.getOps());
                aop.setOpsCategory(entry.getCategory());
                aop.setCatalogSheet(entry.getSheet());
                aop.setChecksum(pChecksum);
                aop.setCountryEn(countryEn);
                opsAopDao.persist(aop);
            }
            finishImport(pFile, aopReader);
            return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), aopReader.getEntriesCount(), "");
        }
    }

    
    //Import OPS Thesaurus
    public CatalogImportResult importOpsThesaurus(final File pFile, final String pChecksum, final int pYear) throws IOException {
        CatalogTypeEn catalogType = CatalogTypeEn.OPS_THESAURUS;
        final String referenceDelimiter = " - ";
        //String[][] lines = getLinesOfFile(pFile);
        //if (mdkDao.getEntryCounter(countryEn) != fileReader.getLineCount()) {
        //if (!baserateDao.catalogExists(pChecksum)) {
        if (opsThesaurusDao.catalogExists(pYear, countryEn)) {
            skipImport(pFile);
            return new CatalogImportResult(false, pFile, catalogType, pYear, pChecksum, getSize(pFile), 0, "Catalog already exists");
        }
        try (CpxFileReader fileReader = new CpxFileReader(pFile, "UTF-8")) {
            startImport(pFile, fileReader);
            opsThesaurusDao.dropEntries(pYear, countryEn);
            for (int i = 0; i < fileReader.getLineCount(); i++) {
                String[] line = fileReader.readLineAsArray("\\|");
                COpsThesaurus opsThesaurus = new COpsThesaurus();
                opsThesaurus.setOpsYear(pYear);
                opsThesaurus.setCountryEn(countryEn);
                opsThesaurus.setChecksum(pChecksum);
                opsThesaurus.setTypeOfCode(toInt(line[0]));
                opsThesaurus.setDimdiInternalNo(toInt(line[1]));
                opsThesaurus.setKeyNo1(line[2]);
                opsThesaurus.setKeyNo2(line[3]);
                String description = line[4];
                int pos = description.indexOf(referenceDelimiter);
                if (pos > -1) {
                    opsThesaurus.setDescription(description.substring(0, pos).trim());
                    opsThesaurus.setReference(description.substring(pos + referenceDelimiter.length()).trim());
                } else {
                    opsThesaurus.setDescription(description);
                }
                opsThesaurusDao.persist(opsThesaurus);
                progressImport(pFile, fileReader, i, opsThesaurusDao);
            }
            finishImport(pFile, fileReader);
            return new CatalogImportResult(true, pFile, catalogType, pYear, pChecksum, getSize(pFile), fileReader.getLineCount(), "");
        }
    }

    /*
  @PermitAll
  private String getDrgModelId(int pYear) {
    String modelId = String.valueOf((pYear-2003)*3);
    if (modelId.length() == 1) {
      modelId = "0" + modelId;
    }
    return modelId;
  }
     */
    public <E> E unserialize(final File pFile) {
        E catalogObj = null;
        int i = 0;
        int maxRetries = 3;
        //Try it more than once, because the exception "Der Prozess kann nicht auf die Datei zugreifen, da sie von einem anderen Prozess verwendet wird" can appear, when windows had not completed copying catalog file
        while (i <= maxRetries) {
            i++;
            try (FileInputStream fileIn = new FileInputStream(pFile); GZIPInputStream gis = new GZIPInputStream(fileIn)) {
                catalogObj = unserialize(gis);
                break;
            } catch (IOException ex) {
                if (i >= maxRetries) {
                    LOG.log(Level.SEVERE, "Cannot extract catalog zip file", ex);
                }
            }
        }
        return catalogObj;
    }


    /*
	public static GZIPInputStream unzip(File pFile) throws IOException {
		//byte[] buffer = new byte[1024];
    final GZIPInputStream gis;
    try(FileInputStream fileIn = new FileInputStream(pFile)) {
      gis = new GZIPInputStream(fileIn);
    }
    return gis;
	} 
     */
//    @PermitAll
//    public CatalogTypeEn CatalogTypeEn.detectType(final File pFile) {
//        if (pFile == null) {
//            return null;
//        }
//        String name = pFile.getName().toUpperCase().trim();
//        if (name.startsWith("ET-ZUSATZ".toUpperCase())) {
//            return CatalogTypeEn.ET;
//        }
//        if (name.startsWith("GDRG".toUpperCase())) {
//            return CatalogTypeEn.DRG;
//        }
//        if (name.startsWith("ICD".toUpperCase())) {
//            return CatalogTypeEn.ICD;
//        }
//        if (name.startsWith("OPS".toUpperCase())) {
//            return CatalogTypeEn.OPS;
//        }
//        if (name.startsWith("PEPP-ZUSATZ".toUpperCase())) {
//            return CatalogTypeEn.ZP;
//        }
//        if (name.startsWith("PEPP".toUpperCase())) {
//            return CatalogTypeEn.PEPP;
//        }
//        if (name.startsWith("ZUSATZ".toUpperCase())) {
//            return CatalogTypeEn.ZE;
//        }
//        if (name.startsWith("KRANKENHAEUSER".toUpperCase())) {
//            return CatalogTypeEn.HOSPITAL;
//        }
//        if (name.startsWith("KRANKENKASSEN".toUpperCase())) {
//            return CatalogTypeEn.INSURANCE_COMPANY;
//        }
//        if (name.startsWith("ABTEILUNGSSCHL".toUpperCase())) {
//            return CatalogTypeEn.DEPARTMENT;
//        }
//        if (name.startsWith("DF_DOCTORS".toUpperCase())) {
//            return CatalogTypeEn.DOCTOR;
//        }
//        if (name.startsWith("ATCCODES".toUpperCase())) {
//            return CatalogTypeEn.ATC;
//        }
//        if (name.startsWith("PZNCODES".toUpperCase())) {
//            return CatalogTypeEn.PZN;
//        }
//        if (name.startsWith("FM_MDKS".toUpperCase())) {
//            return CatalogTypeEn.MDK;
//        }
//        if (name.startsWith("KHBASERATE".toUpperCase())) {
//            return CatalogTypeEn.BASERATE;
//        }
//        return null;
//    }
    public File getCatalogDirectory() {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        String catalogDir = cpxProps.getCpxServerCatalogDir();
        return new File(catalogDir);
    }

    public List<File> getCatalogFileList() {
        List<File> catalogFiles = new LinkedList<>();
        File file = getCatalogDirectory();
        if (file == null) {
            return catalogFiles;
        }
        String catalogDir = file.getAbsolutePath();
        LOG.log(Level.INFO, "CPX Catalog Directory is " + catalogDir);
        file.mkdirs();

        if (!file.exists() || !file.isDirectory()) {
            LOG.log(Level.WARNING, "CPX Catalog Directory does not exist!");
            return catalogFiles;
        }

        if (!file.canRead() || !Files.isReadable(file.toPath())) {
            LOG.log(Level.SEVERE, "No read permissions for CPX Catalog Directory!");
            return catalogFiles;
        }

        File[] listOfFiles = file.listFiles();

        //2017-10-23 DNi: ADD SearchListTypeEn OF STANDARD FILES HERE
        for (File fileCandidate : listOfFiles) {
            if (!fileCandidate.isFile()) {
                continue;
            }
            if (isSerializedCatalog(fileCandidate) || isCoreData(fileCandidate) || isAopData(fileCandidate)) {
                catalogFiles.add(fileCandidate);
            }
        }
        return catalogFiles;
    }

    public boolean isSerializedCatalog(final File pFile) {
        if (pFile == null) {
            return false;
        }
        if (pFile.isDirectory()) {
            return false;
        }
        String name = pFile.getName().toLowerCase().trim();
        return name.endsWith(".ser.zip");
    }

    public boolean isCoreData(final File pFile) {
        if (pFile == null) {
            return false;
        }
        if (pFile.isDirectory()) {
            return false;
        }
        String name = pFile.getName().toLowerCase().trim();
        return name.endsWith(".txt");
    }
    
    private boolean isAopData(File pFile) {
        if (pFile == null) {
            return false;
        }
        if (pFile.isDirectory()) {
            return false;
        }
        String name = pFile.getName().toLowerCase().trim();
        return name.endsWith(".xlsx") && name.startsWith(AopCatalogReader.SHEET_0.toLowerCase());
    }


    private static <T> List<T> emptyList(final List<T> pList) {
        if (pList == null) {
            return new ArrayList<>();
        }
        return pList;
    }

//    @PreDestroy
//    @PermitAll
//    public void destroy() {
//    }

    private String checkCheckSum(final File pFile) throws IOException{
        final CatalogTypeEn catalogType = CatalogTypeEn.detectType(pFile);
        final String checkSum = getChecksum(pFile);
        if(catalogType== null || !catalogType.isAutoUpdate()){
            return checkSum;
        }
        if(!catalogExists(checkSum, catalogType)){
         
            clearTable4Catalog(catalogType, pFile);
        }
        return checkSum;
    }
    public void clearTable4Catalog(final CatalogTypeEn catalogType, final File pFile) {
        try{
            int year = CatalogTypeEn.detectYear(pFile, catalogType);
        switch(catalogType){
            case INSURANCE_COMPANY:
                insuranceCompanyDao.dropEntries(countryEn);
            break;
            case ATC:
                atcDao.dropEntries(countryEn);
            break;
            case PZN:
                pznDao.dropEntries(countryEn);
            break;
            case DEPARTMENT:
                departmentDao.dropEntries(countryEn);
            break;
            case DOCTOR:
                doctorDao.dropEntries(countryEn);
            break;
            case HOSPITAL:
                hospitalDao.dropEntries(countryEn);
            break;
            case ICD_THESAURUS:
                 icdThesaurusDao.dropEntries(year, countryEn);
                 break;
            case OPS_THESAURUS:
                opsThesaurusDao.dropEntries(year, countryEn);
                break;
            case ICD_TRANSFER:
            case OPS_TRANSFER:
                int[]years = CatalogTypeEn.detectSrcAndDestYears(pFile);  
                if(years != null && years.length> 1){
                   getDao(catalogType).dropEntries(years[0], years[1]);
               }
                break;
            case OPS_AOP:
                opsAopDao.dropEntries(year, countryEn); 
                break;
            }
        }catch(Exception ex){
            LOG.log(Level.SEVERE, "could not delete entries from database table for type " + catalogType.name(), ex);
        }
    }

    private  boolean catalogExists(String pCheckSum,  CatalogTypeEn catalogType) {
        if(catalogType == null){
            // we do not want to check any text file that was found in catalog directory and  does not belong to the catalog list
            return true;
        }
        switch(catalogType){
            case INSURANCE_COMPANY:
                return insuranceCompanyDao.catalogExists(pCheckSum, countryEn);
            case ATC:
                 return atcDao.catalogExists(pCheckSum, countryEn);
            case PZN:
                return pznDao.catalogExists(pCheckSum, countryEn);
            case DEPARTMENT:
                return departmentDao.catalogExists(pCheckSum, countryEn);
            case DOCTOR:
                return doctorDao.catalogExists(pCheckSum, countryEn);
            case HOSPITAL:
                return hospitalDao.catalogExists(pCheckSum, countryEn);
            case ICD_THESAURUS:
                return icdThesaurusDao.catalogExists(pCheckSum);
            case OPS_THESAURUS:
                return opsThesaurusDao.catalogExists(pCheckSum);
            case ICD_TRANSFER:
            case OPS_TRANSFER:
                return getDao(catalogType).catalogExists(pCheckSum);
            
        }
        return true;
    }
    
    public boolean fileCanAutoUpdate(File pFile){
        CatalogTypeEn catalogType = CatalogTypeEn.detectType(pFile);
        if(catalogType == null){
            return false;
        }
        return catalogType.isAutoUpdate();
    }

    public List<File> getCatalogFile2Name(String pFileName) {
        List<File> catalogList = getCatalogFileList();
        List<File> retList = new LinkedList<>();
        for(File file: catalogList){
            if(file.getName().equalsIgnoreCase(pFileName)){
                retList.add(file);
                return retList;
            }
        }
        return null;
    }


}

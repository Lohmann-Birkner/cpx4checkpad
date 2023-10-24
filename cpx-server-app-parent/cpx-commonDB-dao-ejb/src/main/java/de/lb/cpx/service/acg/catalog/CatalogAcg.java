/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.acg.catalog;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.query.Query;
import de.lb.cpx.shared.dto.acg.AdgFarbe;
import de.lb.cpx.shared.dto.acg.EdcOrgan;
import de.lb.cpx.shared.dto.acg.Farbe;
import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import de.lb.cpx.shared.dto.acg.IdcAdgEdcMedc;
import de.lb.cpx.shared.dto.acg.Organ;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ucanaccess.jdbc.UcanaccessDriver;

/**
 *
 * @author niemeier
 */
public class CatalogAcg implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(CatalogAcg.class.getName());

    public static final String TABLE_ADG_FARBE = "adg farbe";
    public static final String TABLE_EDC_ORGAN = "edc organ";
    public static final String TABLE_FARBE = "farbe";
    public static final String TABLE_ICD_ADG_EDC_MEDC = "icd adg edc medc";
    public static final String TABLE_ORGAN = "organ";
    //public static final String QUERY_A_ICD_EDC_FARBE_ORGAN = "a_icd_edc_farbe_organ";

    private final Database acgDatabase;
    private final Connection conn;
    private final String pathToAcgDatabase;

    public CatalogAcg() throws IOException, SQLException {
        this(null);
    }

    public CatalogAcg(final String pDatabasePath) throws IOException, SQLException {
        pathToAcgDatabase = pDatabasePath;
        acgDatabase = getAcgDatabase(pDatabasePath);
        conn = createConnection(pDatabasePath);
    }

    public Table getAdgFarbeTable() throws IOException {
        final Table table = getAcgTable(TABLE_ADG_FARBE);
        return table;
    }

    private Integer DoubleToInt(final Double pValue) {
        if (pValue == null) {
            return null;
        }
        int val = pValue.intValue();
        if (val != pValue) {
            throw new IllegalArgumentException("This is not a valid integer: " + pValue);
        }
        return val;
    }

    private String toStr(final String pValue) {
        if (pValue == null) {
            return "";
        }
        return pValue.trim();
    }

    public Set<AdgFarbe> getAdgFarbeSet() throws IOException {
        final Set<AdgFarbe> list = new TreeSet<>(new Comparator<AdgFarbe>() {
            @Override
            public int compare(final AdgFarbe lhs, final AdgFarbe rhs) {
                return Integer.compare(lhs.adg, rhs.adg);
            }
        });
        for (final Row row : getAdgFarbeTable()) {
            final Integer farbeNr = DoubleToInt(row.getDouble("farbe_nr"));
            final Integer adg = DoubleToInt(row.getDouble("adg"));
            final AdgFarbe item = new AdgFarbe(farbeNr, adg);
            if (!item.hasValues()) {
                continue;
            }
            if (!list.add(item)) {
                LOG.log(Level.WARNING, "This item does already exist, maybe it was defined multiple times by accident: " + item.toString());
            }
        }
        return list;
    }

    public Table getEdcOrganTable() throws IOException {
        Table table = getAcgTable(TABLE_EDC_ORGAN);
        return table;
    }

    public Set<EdcOrgan> getEdcOrganSet() throws IOException {
        final Set<EdcOrgan> list = new TreeSet<>(new Comparator<EdcOrgan>() {
            @Override
            public int compare(final EdcOrgan lhs, final EdcOrgan rhs) {
                return Integer.compare(lhs.organNr, rhs.organNr);
            }
        });
        for (final Row row : getEdcOrganTable()) {
            final Integer organNr = DoubleToInt(row.getDouble("organ_nr"));
            final String edc = toStr(row.getString("edc"));
            final String edcText = toStr(row.getString("edc_text"));
            final EdcOrgan item = new EdcOrgan(organNr, edc, edcText);
            if (!item.hasValues()) {
                continue;
            }
            if (!list.add(item)) {
                LOG.log(Level.WARNING, "This item does already exist, maybe it was defined multiple times by accident: " + item.toString());
            }
        }
        return list;
    }

    public Table getFarbeTable() throws IOException {
        final Table table = getAcgTable(TABLE_FARBE);
        return table;
    }

    public Set<Farbe> getFarbeSet() throws IOException {
        final Set<Farbe> list = new TreeSet<>(new Comparator<Farbe>() {
            @Override
            public int compare(final Farbe lhs, final Farbe rhs) {
                return Integer.compare(lhs.farbeNr, rhs.farbeNr);
            }
        });
        for (final Row row : getEdcOrganTable()) {
            final Integer farbeNr = DoubleToInt(row.getDouble("farbe_nr"));
            final String farbeText = toStr(row.getString("farbe_text"));
            final Farbe item = new Farbe(farbeNr, farbeText);
            if (!item.hasValues()) {
                continue;
            }
            if (!list.add(item)) {
                LOG.log(Level.WARNING, "This item does already exist, maybe it was defined multiple times by accident: " + item.toString());
            }
        }
        return list;
    }

    public IcdFarbeOrgan getIcdFarbeOrganByIcd(final String pIcdCode) throws IOException, SQLException {
        final String icdCode = pIcdCode == null ? "" : pIcdCode.trim().toUpperCase();
        //final Query query = getAcgQuery(QUERY_A_ICD_EDC_FARBE_ORGAN);
        IcdFarbeOrgan icdFarbeOrgan = null;

        final String query = "SELECT [icd adg edc medc].icd, [edc organ].edc, [edc organ].edc_text, [icd adg edc medc].medc_code, [adg farbe].adg, [adg farbe].farbe_nr, farbe.farbe_text, organ.organ_nr, organ.organ_text "
                + " FROM ((([icd adg edc medc] INNER JOIN [adg farbe] ON [icd adg edc medc].adg_code = [adg farbe].adg) INNER JOIN farbe ON [adg farbe].farbe_nr = farbe.farbe_nr) INNER JOIN [edc organ] ON [icd adg edc medc].edc_code = [edc organ].edc) INNER JOIN organ ON [edc organ].organ_nr = organ.organ_nr "
                + " WHERE [icd adg edc medc].icd = ? ";
        try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, icdCode);
            try (final ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    if (icdFarbeOrgan != null) {
                        LOG.log(Level.SEVERE, "There is more one entry in ");
                        break;
                    }
                    icdFarbeOrgan = new IcdFarbeOrgan(
                            rs.getString("icd"),
                            //rs.getInt("adg"),
                            //rs.getString("edc"),
                            rs.getString("edc_text"),
                            //rs.getString("medc_code"),
                            rs.getInt("farbe_nr"),
                            //rs.getString("farbe_text"),
                            rs.getInt("organ_nr")
                    //rs.getString("organ_text")
                    );
                }
            }
        }
        if (icdFarbeOrgan == null) {
            LOG.log(Level.FINE, "Cannot find entry for ICD code '" + icdCode + "' in ACG mapping database! I'll create an empty dummy object");
            //icdFarbeOrgan = new IcdFarbeOrgan(icdCode, null, "", "", "", 0, "", 0, "");
            icdFarbeOrgan = new IcdFarbeOrgan(icdCode, "", 0, 0, "");
        }
        return icdFarbeOrgan;
    }

    public Table getIdcAdgEdcMedcTable() throws IOException {
        final Table table = getAcgTable(TABLE_ICD_ADG_EDC_MEDC);
        return table;
    }

    public Set<IdcAdgEdcMedc> getIdcAdgEdcMedcSet() throws IOException {
        final Set<IdcAdgEdcMedc> list = new TreeSet<>(new Comparator<IdcAdgEdcMedc>() {
            @Override
            public int compare(final IdcAdgEdcMedc lhs, final IdcAdgEdcMedc rhs) {
                return lhs.icd.compareToIgnoreCase(rhs.icd);
            }
        });
        for (final Row row : getIdcAdgEdcMedcTable()) {
            final String icd = toStr(row.getString("icd"));
            final Integer adgCode = DoubleToInt(row.getDouble("adg_code"));
            final String edcCode = toStr(row.getString("edc_code"));
            final String medcCode = toStr(row.getString("medc_code"));
            final IdcAdgEdcMedc item = new IdcAdgEdcMedc(icd, adgCode, edcCode, medcCode);
            if (!item.hasValues()) {
                continue;
            }
            if (!list.add(item)) {
                LOG.log(Level.WARNING, "This item does already exist, maybe it was defined multiple times by accident: " + item.toString());
            }
        }
        return list;
    }

    public Table getOrganTable() throws IOException {
        final Table table = getAcgTable(TABLE_ORGAN);
        return table;
    }

    public Set<Organ> getOrganSet() throws IOException {
        final Set<Organ> list = new TreeSet<>(new Comparator<Organ>() {
            @Override
            public int compare(final Organ lhs, final Organ rhs) {
                return lhs.organText.compareToIgnoreCase(rhs.organText);
            }
        });
        for (final Row row : getOrganTable()) {
            final Integer organNr = row.getInt("organ_nr");
            final String organText = toStr(row.getString("organ_text"));
            final Organ item = new Organ(organNr, organText);
            if (!item.hasValues()) {
                continue;
            }
            if (!list.add(item)) {
                LOG.log(Level.WARNING, "This item does already exist, maybe it was defined multiple times by accident: " + item.toString());
            }
        }
        return list;
    }

    public Table getAcgTable(final String pTableName) throws IOException {
        String tableName = pTableName == null ? "" : pTableName.trim();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("table name is null or empty!");
        }
        Table table = acgDatabase.getTable(tableName);
        if (table == null) {
            throw new IllegalArgumentException("Table with the name '" + tableName + "' does not exist!");
        }
        return table;
    }

//    public Query getAcgQuery(final String pQueryName) throws IOException {
//        String queryName = pQueryName == null ? "" : pQueryName.trim();
//        if (queryName.isEmpty()) {
//            throw new IllegalArgumentException("table name is null or empty!");
//        }
//        List<Query> queries = getAcgQueries();
//        if (queries == null || queries.isEmpty()) {
//            throw new IllegalArgumentException("no queries found!");
//        }
//        Iterator<Query> it = queries.iterator();
//        Query query = null;
//        while (it.hasNext()) {
//            Query queryTmp = it.next();
//            query = queryTmp;
//            break;
//        }
//        //Table table = acgDatabase.get (queryName);
//        if (query == null) {
//            throw new IllegalArgumentException("Query with the name '" + queryName + "' does not exist!");
//        }
//        return query;
//    }
    public List<Query> getAcgQueries() throws IOException {
        return acgDatabase.getQueries();
    }

    public final Database getAcgDatabase() throws IOException {
        final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        String databasePath = cpxProps.getCpxServerCatalogAcgFile();
        return getAcgDatabase(databasePath);
    }

    public final Database getAcgDatabase(final String pDatabasePath) throws IOException {
        File file = checkAccessFile(pDatabasePath);
        LOG.log(Level.FINE, "Open MS Access DB: " + file.getAbsolutePath());
        Database database = DatabaseBuilder.open(file);
        return database;
    }

    private File checkAccessFile(final String pDatabasePath) {
        final String databasePath = pDatabasePath == null ? "" : pDatabasePath.trim();
        final File file = new File(databasePath);
        LOG.log(Level.FINE, "Check validity of MS Access database: " + file.getAbsolutePath());
        if (!file.exists()) {
            throw new IllegalArgumentException("This database path does not exist: " + file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("This database path is not a file: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("This database file is not readable: " + file.getAbsolutePath());
        }
        return file;
    }

    private Connection createConnection(final String pDatabasePath) throws SQLException {
        final File file = new File(pDatabasePath);
        String url = createConnectionUrl(file.getAbsolutePath());
        LOG.log(Level.INFO, "Establish jdbc connection to MS Access DB: " + url);
        Connection c = DriverManager.getConnection(url, "sa", "");
        return c;
    }

    private String createConnectionUrl(final String pDatabasePath) {
        String url = UcanaccessDriver.URL_PREFIX + pDatabasePath + ";newDatabaseVersion=V2003";
        return url;
    }

    @Override
    public void close() throws Exception {
        if (acgDatabase != null) {
            try {
                acgDatabase.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Was not able to close MS Access DB: " + pathToAcgDatabase, ex);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Was not able to close jdbc connection to MS Access DB: " + pathToAcgDatabase, ex);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //CpxSystemProperties.setCpxHome("C:\\workspace\\CheckpointX_Repository\\trunk\\WD_CPX_Server\\");
        final String cpxHome = getCpxHome();
        final String databasePath = cpxHome + "\\catalog\\steuerung_acg.mdb";
        try (final CatalogAcg acgCat = new CatalogAcg(databasePath)) {
            final IcdFarbeOrgan icdFarbeOrgan = acgCat.getIcdFarbeOrganByIcd("A04.9");
            LOG.log(Level.INFO, "Result of query by ICD: " + String.valueOf(icdFarbeOrgan));

            int counter;
            LOG.log(Level.INFO, "Read table " + TABLE_ADG_FARBE);
            counter = 0;
            for (AdgFarbe adgFarbe : acgCat.getAdgFarbeSet()) {
                counter++;
                LOG.log(Level.INFO, "Entry #" + counter + ": " + adgFarbe.toString());
            }

            LOG.log(Level.INFO, "Read table " + TABLE_EDC_ORGAN);
            counter = 0;
            for (EdcOrgan edcOrgan : acgCat.getEdcOrganSet()) {
                counter++;
                LOG.log(Level.INFO, "Entry #" + counter + ": " + edcOrgan.toString());
            }

            LOG.log(Level.INFO, "Read table " + TABLE_FARBE);
            counter = 0;
            for (Farbe farbe : acgCat.getFarbeSet()) {
                counter++;
                LOG.log(Level.INFO, "Entry #" + counter + ": " + farbe.toString());
            }

            LOG.log(Level.INFO, "Read table " + TABLE_ICD_ADG_EDC_MEDC);
            counter = 0;
            for (IdcAdgEdcMedc idcAdgEdcMedc : acgCat.getIdcAdgEdcMedcSet()) {
                counter++;
                LOG.log(Level.INFO, "Entry #" + counter + ": " + idcAdgEdcMedc.toString());
            }

            LOG.log(Level.INFO, "Read table " + TABLE_ORGAN);
            counter = 0;
            for (Organ organ : acgCat.getOrganSet()) {
                counter++;
                LOG.log(Level.INFO, "Entry #" + counter + ": " + organ.toString());
            }
        }
    }

    private static String getCpxHome() {
        final String cpxHome = System.getenv("CPX_HOME");
        final String baseDir;
        if (cpxHome.isEmpty()) {
            LOG.log(Level.WARNING, "THERE'S NO CPX_HOME ON THIS SYSTEMS ENV! I TRY A BEST GUESS STRATEGY TO FIND SERVER DIRECTORY (WD_CPX_SERVER?)");
            Properties props = System.getProperties();
            String userDir = props.getProperty("user.dir");
            int pos = userDir.indexOf("cpx-server-app-parent");
            if (pos >= 0) {
                userDir = userDir.substring(0, pos);
            }
            baseDir = userDir + "WD_CPX_Server";
        } else {
            LOG.log(Level.INFO, "Using CPX_HOME from Systems Environment: " + cpxHome);
            baseDir = cpxHome;
        }
        return baseDir;
    }

}

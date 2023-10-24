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
package de.lb.cpx.updatedb;

import de.lb.cpx.server.commons.dao.AbstractDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.jboss.ejb3.annotation.SecurityDomain;
import util.FileManager;

/**
 *
 * @author martin
 */
@Startup
@Singleton
@SecurityDomain("cpx")
@PermitAll
@LocalBean
public class UpdateDbBean {

    private static final Logger LOG = Logger.getLogger(UpdateDbBean.class.getName());

    public static String dateToStr(final Date pDate) {
        if (pDate == null) {
            return "";
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(pDate);
    }

    public static Date strToDate(final String pDateStr) throws ParseException {
        String dateStr = (pDateStr == null) ? "" : pDateStr.trim();
        if (dateStr.isEmpty()) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(dateStr);
        return date;
    }

    public void startDbUpdate(final Connection pConnection, final String pUpdateDbFilename, final String pUpdateDbViewsFile) throws SQLException, ParseException, IOException {
        if (pConnection == null) {
            throw new IllegalArgumentException("No database connection passed!");
        }

        String updateDbFilename = (pUpdateDbFilename == null) ? "" : pUpdateDbFilename.trim();
        if (updateDbFilename.isEmpty()) {
            throw new IllegalArgumentException("No database script file name passed!");
        }

        String url = pConnection.getMetaData().getURL();
        boolean isOracle = AbstractDao.isOracle(url);
        String database = (isOracle) ? "oracle" : "sqlsrv";

        LOG.log(Level.INFO, "Looking for " + database + " updates in database script file " + updateDbFilename + "...");
        Date lastUpdate = getLastUpdate(pConnection);
        LOG.log(Level.INFO, "LAST_UPDATE = " + (lastUpdate == null ? "null" : dateToStr(lastUpdate)));
        Map<Date, List<UpdateQuery>> updates = readUpdateDbFile(database, updateDbFilename, lastUpdate);
        LOG.log(Level.INFO, ((updates == null || updates.isEmpty()) ? "No" : updates.size()) + " update(s) found");
        executeUpdates(pConnection, updates, true);
        boolean isNewDb = false;
        try {
            isNewDb = isNewDb(pConnection);
        } catch (SQLSyntaxErrorException ex) {
            LOG.log(Level.INFO, "Cannot read IS_NEW_DB from IN4MED - does field exist?");
            LOG.log(Level.FINEST, "Field IS_NEW_DB does not exist yet(?)", ex);
        }
        LOG.log(Level.INFO, "IS_NEW_DB = " + (isNewDb ? "yes" : "no"));
        if (isNewDb) {
            installViews(pConnection, pUpdateDbViewsFile, database);
            setNewDb(pConnection, false);
        }
        LOG.log(Level.INFO, "Database update finished!");
    }

//    public void installDb(final Connection pConnection, final String pUpdateDbViewsFile, final String pDatabase) throws SQLException, ParseException, IOException {
//        installViews(pConnection, pUpdateDbViewsFile, pDatabase);
//        //installViews(pConnection, viewFiles, pDatabase);
//        setNewDb(pConnection, false);
//    }
    public void installViews(final Connection pConnection, final String pUpdateDbViewsFile, final String pDatabase) throws SQLException, ParseException, IOException {
        final Date minDate = null;
        final Map<Date, List<UpdateQuery>> viewQueries = readUpdateDbFile(pDatabase, pUpdateDbViewsFile, minDate);
        //final Map<Date, List<UpdateQuery>> viewFiles = getViewQueries(pUpdateDbViewsFile, pDatabase);
        executeUpdates(pConnection, viewQueries, false);
    }

//    public Map<Date, List<UpdateQuery>> getViewQueries(final String pUpdateDbViewsDir, final String pDatabase) throws IOException, ParseException {
//        final Map<Date, List<UpdateQuery>> queries = new LinkedHashMap<>();
//        final List<UpdateQuery> queryList = new ArrayList<>();
//        final Date minDate = null;
//        queries.put(minDate, queryList);
//        String updateDbViewsDir = (pUpdateDbViewsDir == null) ? "" : pUpdateDbViewsDir.trim();
//        if (updateDbViewsDir.isEmpty()) {
//            LOG.log(Level.WARNING, "New database detected (IS_NEW_DB = 1), but no views directory was passed!");
//            return queries;
//        }
//        final File viewDir = new File(updateDbViewsDir);
//        if (!viewDir.exists()) {
//            throw new IllegalArgumentException("Views directory does not exist!");
//        }
//        if (!viewDir.isDirectory()) {
//            throw new IllegalArgumentException("Views directory seems to be a file and not a folder!");
//        }
//        if (!viewDir.canRead()) {
//            throw new IllegalArgumentException("No permission to read from views directory!");
//        }
//        for (File file : viewDir.listFiles()) {
//            String n = file.getName().toLowerCase();
//            int pos = n.lastIndexOf('.');
//            if (pos > -1) {
//                n = n.substring(0, pos).trim();
//            }
//            if (n.contains("_" + pDatabase.toLowerCase())
//                    || !n.contains("_sqlsrv") && !n.contains("_oracle")) {
//                final String viewName = n.replace("_oracle", "").replace("_sqlsrv", "").toUpperCase(); //maybe it can be extracted from query in file?!
//                try (FileInputStream fis = new FileInputStream(file)) {
//                    byte[] data = new byte[(int) file.length()];
//                    fis.read(data);
//
//                    String query = new String(data, "UTF-8").trim();
//                    if (query.isEmpty()) {
//                        LOG.log(Level.WARNING, "View file is empty: " + file.getAbsolutePath());
//                        continue;
//                    }
//                    final int lineFrom = 0;
//                    final int lineTo = 0;
//                    final String strategy = "";
//                    queryList.add(new UpdateQuery("DROP VIEW " + viewName, lineFrom, lineTo, strategy));
//                    queryList.add(new UpdateQuery(query, lineFrom, lineTo, strategy));
//                }
//            }
////            final Map<Date, List<UpdateQuery>> result = readUpdateDbFile(pDatabase, file.getAbsolutePath(), minDate);
////            for (List<UpdateQuery> list : result.values()) {
//////                for(UpdateQuery query: list) {
//////                    queryList.add(query);
//////                }
////                queryList.addAll(list);
////            }
//        }
//        return queries;
//    }
    public void executeUpdates(final Connection pConnection, final Map<Date, List<UpdateQuery>> updates, final boolean setLastUpdate) throws SQLException {
        if (updates == null) {
            throw new IllegalArgumentException("No updates passed!");
        }
        if (pConnection == null) {
            throw new IllegalArgumentException("No database connection given!");
        }
        Iterator<Map.Entry<Date, List<UpdateQuery>>> it = updates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Date, List<UpdateQuery>> entry = it.next();
            Date date = entry.getKey();
            List<UpdateQuery> queries = entry.getValue();
            String dateStr = dateToStr(date);
            LOG.log(Level.INFO, "Will install database update [" + dateStr + "]...");
            executeUpdate(pConnection, date, queries, setLastUpdate);
            LOG.log(Level.INFO, "Database update [" + dateStr + "] successfully installed");
        }
    }

    public void executeUpdate(final Connection pConnection, final Date pDate, final List<UpdateQuery> pQueries, final boolean setLastUpdate) throws SQLException {
        String dateStr = dateToStr(pDate);
        for (int i = 0; i < pQueries.size(); i++) {
            UpdateQuery query = pQueries.get(i);
            if (query == null || query.query.isEmpty()) {
                continue;
            }
            try ( Statement stmt = pConnection.createStatement()) {
                stmt.execute(query.query);
            } catch (SQLException ex) {
                LOG.log(Level.FINER, "Query failed", ex);
                boolean isError;
                if ("abort".equalsIgnoreCase(query.strategy)) {
                    isError = true;
                } else {
                    isError = false;
                }

                String errorText = (ex.getMessage() == null) ? "" : ex.getMessage().trim();
                String msg = "An " + (isError ? "error" : "warning") + " occured in database update [" + dateStr + "]:"
                        + "\n" + (isError ? "Error" : "Warning") + ": " + errorText
                        + "\nAffected query: " + query.print();
                if (isError) {
                    throw new IllegalArgumentException(msg);
                } else {
                    final Level level = "silent".equalsIgnoreCase(query.strategy) ? Level.FINEST : Level.WARNING;
                    LOG.log(level, msg);
                }
            }
        }
        if (setLastUpdate) {
            setLastUpdate(pConnection, pDate);
        }
        pConnection.commit();
    }

    public Map<Date, List<UpdateQuery>> readUpdateDbFile(final String pDatabase, final String pUpdateFilename, final Date pMinDate) throws ParseException, IOException {
        String updateFilename = (pUpdateFilename == null) ? "" : pUpdateFilename.trim();
        String database = (pDatabase == null) ? "" : pDatabase.trim().toLowerCase();
        if (!database.equalsIgnoreCase("oracle") && !database.equalsIgnoreCase("sqlsrv")) {
            throw new IllegalArgumentException("No valid database given (use 'oracle' or 'sqlsrv')!");
        }
        if (updateFilename.isEmpty()) {
            throw new IllegalArgumentException("No update file name given!");
        }
        Date minDate = pMinDate == null ? null : new Date(pMinDate.getTime());

        LOG.log(Level.INFO, "Update directory: " + pUpdateFilename);

        Pattern patDate = Pattern.compile("^(\\[\\d{2}/\\d{2}/\\d{4}])"); //z.B. 12/07/2017
        Pattern patDatabase = Pattern.compile("^(\\[(oracle|sqlsrv|all)\\]\\:\\:(.*\\:\\:)?).*"); //z.B. [oracle]:: oder [sqlsrv]::
        Map<Date, List<UpdateQuery>> updates = new TreeMap<>();

        minDate = strToDate(dateToStr(minDate));

        try ( FileManager fileManager = new FileManager(pUpdateFilename)) {
            try ( BufferedReader br = fileManager.getBufferedReader()) {
                List<UpdateQuery> queries = null;
                String line;
                String query = "";
                int lineCountFrom = -1;
                int lineCountTo = -1;
                String strategy = "";
                Date date = null;
                boolean newQuery = true;
                int lineCount = 0;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    line = line.trim();
                    String lineSearch = line.replace(" ", "").toLowerCase();

                    //System.out.println(line);
                    Matcher matchDate = patDate.matcher(lineSearch);
                    if (matchDate.matches()) {
                        if (!query.isEmpty()) {
                            if (queries == null) {
                                throw new IllegalArgumentException("Something went wrong here, there's an orphaned sql query without an assigned date: " + query + " (occured in " + pUpdateFilename + ", line " + line + ")");
                            }
                            lineCountTo = lineCount - 1;
                            UpdateQuery qry = new UpdateQuery(query, lineCountFrom, lineCountTo, strategy);
                            if (!queries.contains(qry)) {
                                queries.add(qry);
                            }
                            query = "";
                            lineCountFrom = -1;
                            lineCountTo = -1;
                            strategy = "";
                            newQuery = true;
                        }
                        String dateStr = matchDate.group(1);
                        DateFormat format = new SimpleDateFormat("[dd/MM/yyyy]");
                        date = format.parse(dateStr);
                        queries = new ArrayList<>();
                        if (updates.put(date, queries) != null) {
                            throw new IllegalArgumentException("There are two updates with the same date: " + dateStr + " (occured in " + pUpdateFilename + ", line " + line + ")");
                        }
                        continue;
                    }
                    if (date == null || queries == null) {
                        if (lineSearch.isEmpty()) {
                            continue;
                        }
                        throw new IllegalArgumentException("No date defined, header format [dd/MM/yyyy] was not detected (occured in " + pUpdateFilename + ", line " + line + ")");
                    }
                    if (pMinDate != null && !date.after(minDate)) {
                        //Ignore former entries
                        continue;
                    }
                    Matcher matchDatabase = patDatabase.matcher(line.toLowerCase());
                    if (matchDatabase.matches()) {
                        if (!query.isEmpty()) {
                            lineCountTo = lineCount - 1;
                            UpdateQuery qry = new UpdateQuery(query, lineCountFrom, lineCountTo, strategy);
                            if (!queries.contains(qry)) {
                                queries.add(qry);
                            }
                            query = "";
                            lineCountFrom = -1;
                            lineCountTo = -1;
                            strategy = "";
                            newQuery = true;
                        }
                        lineCountFrom = lineCount;
                        String databaseStr = matchDatabase.group(1);
                        String delim = "::";
                        String queryTmp = line.substring(databaseStr.length()).trim();
                        String[] arr = databaseStr.split(delim);
                        if (arr.length > 1) {
                            strategy = arr[1];
                        }
                        if (databaseStr.contains(database) || databaseStr.contains("all")) {
                            query += queryTmp;
                            newQuery = false;
                        }
                        continue;
                    }
                    if (newQuery) {
                        continue;
                    }
                    if (!query.isEmpty()) {
                        query += "\r\n";
                    }
                    query += line;
                }
                if (queries != null && !query.isEmpty()) {
                    lineCountTo = lineCount;
                    UpdateQuery qry = new UpdateQuery(query, lineCountFrom, lineCountTo, strategy);
                    if (!queries.contains(qry)) {
                        queries.add(qry);
                    }
                }
            }
        }
        Iterator<Map.Entry<Date, List<UpdateQuery>>> it = updates.entrySet().iterator();
        int updateNo = 0;
        while (it.hasNext()) {
            Map.Entry<Date, List<UpdateQuery>> entry = it.next();
            Date date = entry.getKey();
            String dateStr = dateToStr(date);
            List<UpdateQuery> queries = entry.getValue();
            if (queries.isEmpty()) {
                it.remove();
                continue;
            }
            updateNo++;
            for (int i = 0; i < queries.size(); i++) {
                UpdateQuery query = queries.get(i);
                LOG.log(Level.INFO, "Update #" + updateNo + " [" + dateStr + "], Query #" + (i + 1) + ": " + query.print());
            }
        }
        return updates;
    }

    public java.sql.Date getLastUpdate(final Connection pConnection) throws SQLException {
        java.sql.Date lastUpdate = null;
        try ( Statement stmt = pConnection.createStatement()) {
            String query = "SELECT LAST_UPDATE FROM IN4MED";
            try ( ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    lastUpdate = rs.getDate("LAST_UPDATE");
                }
            }
        }
        return lastUpdate;
    }

    public boolean isNewDb(final Connection pConnection) throws SQLException {
        boolean isNewDb = false;
        try ( Statement stmt = pConnection.createStatement()) {
            String query = "SELECT IS_NEW_DB FROM IN4MED";
            try ( ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    isNewDb = rs.getBoolean("IS_NEW_DB");
                }
            }
        }
        return isNewDb;
    }

    public boolean setLastUpdate(final Connection pConnection, final Date pDate) throws SQLException {
        java.sql.Date date = pDate == null ? null : new java.sql.Date(pDate.getTime());
        String query = "UPDATE IN4MED SET LAST_UPDATE = ? WHERE ID = 1";
        try ( PreparedStatement pstmt = pConnection.prepareStatement(query)) {
            pstmt.setDate(1, date);
            boolean ret = (pstmt.executeUpdate() == 1);
            if (!ret) {
                query = "INSERT INTO IN4MED (ID, LAST_UPDATE,IS_NEW_DB) VALUES (1, ?, 0)";
                try ( PreparedStatement pstmt2 = pConnection.prepareStatement(query)) {
                    pstmt2.setDate(1, date);
                    ret = (pstmt2.executeUpdate() == 1);
                }
            }
            return ret;
        }
    }

    public boolean setNewDb(final Connection pConnection, final boolean pNewDb) throws SQLException {
        String query = "UPDATE IN4MED SET IS_NEW_DB = ? WHERE ID = 1";
        try ( PreparedStatement pstmt = pConnection.prepareStatement(query)) {
            pstmt.setBoolean(1, pNewDb);
            boolean ret = (pstmt.executeUpdate() == 1);
            if (!ret) {
                query = "INSERT INTO IN4MED (ID, LAST_UPDATE, IS_NEW_DB) VALUES (1, ?, ?)";
                try ( PreparedStatement pstmt2 = pConnection.prepareStatement(query)) {
                    pstmt2.setDate(1, new java.sql.Date(new Date().getTime()));
                    pstmt2.setBoolean(1, pNewDb);
                    ret = (pstmt2.executeUpdate() == 1);
                }
            }
            return ret;
        }
    }

}

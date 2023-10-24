/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package uploader.impl;

import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import de.lb.cpx.str.utils.StrUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import line.AbstractLine;
import line.Field;
import line.LineEntity;
import module.ImportModuleI;
import module.impl.ImportConfig;
import progressor.ProgressorI;
import uploader.CpxUploaderI;
import util.CpxReader;
import util.DbBuilder;

/**
 *
 * @author Dirk Niemeier
 * @param <T> config type
 */
public class CpxUploader<T extends ImportModuleI<? extends CpxJobImportConfig>> implements CpxUploaderI<T> {

    static final Logger LOG = Logger.getLogger(CpxUploader.class.getName());
    long mMeanTime = System.currentTimeMillis();
    final long mStartupTime = mMeanTime;
    private final ProgressorI mProgressor;
    ImportConfig<T> mImportConfig = null;

    public CpxUploader(final ProgressorI pProgressor) {
        mProgressor = pProgressor;
        if (pProgressor == null) {
            throw new IllegalArgumentException("Progressor cannot be null!");
        }
    }

    @Override
    public ProgressorI getProgressor() {
        return mProgressor;
    }

    @Override
    public void uploadImex(final ImportConfig<T> pImportConfig, final Connection pConnection) throws InstantiationException, IllegalAccessException, IOException, SQLException, NoSuchFieldException, ParseException {
        mMeanTime = System.currentTimeMillis();
        mImportConfig = pImportConfig;
        final String directory = pImportConfig.getModule().getOutputDirectory();
        try (CpxReader cpxReader = CpxReader.getInstance(directory)) {
            List<LineEntity> lineEntities = AbstractLine.getLineEntities();
            try (Statement stmt = pConnection.createStatement()) {
                //1st step: drop all (temporary and non-temporary) imex tables
                getProgressor().sendProgress("Lösche temporäre IMEX-Tabellen aus vorherigen Import");
                for (LineEntity lineEntity : lineEntities) {
                    //dropTmpImexTable(lineEntity, stmt, pDatabaseName);
                    //dropImexTable(lineEntity, stmt, pDatabaseName);
                    dropTable(stmt, lineEntity.imexTableName, pImportConfig.isCaseDbOracle());
                    dropTable(stmt, lineEntity.imexTmpTableName, pImportConfig.isCaseDbOracle());
                }
                getProgressor().sendProgress("Erzeuge IMEX-Tabellen");
//2nd step: create all temporary imex tables
                for (LineEntity lineEntity : lineEntities) {
                    createImexTable(stmt, lineEntity.imexTmpTableName, lineEntity);
                }
                //3rd step: fill all temporary imex tables with data
                for (LineEntity lineEntity : lineEntities) {
                    try (BufferedReader br = cpxReader.getReader(lineEntity)) {
                            LOG.log(Level.INFO, "Start bulk insert into " + lineEntity.imexTmpTableName + "...");
                        getProgressor().sendProgress("Fülle IMEX-Tabelle: " + lineEntity.imexTmpTableName);
                        uploadImexTable(lineEntity, br, pConnection, stmt);
                        LOG.log(Level.INFO, "Bulk insert into " + lineEntity.imexTmpTableName + " successful!");
                    }
                }
                //4th step: create temporary imex table indizes
                for (LineEntity lineEntity : lineEntities) {
                    createIndexes(stmt, lineEntity.imexTmpTableName);
                }
                //specific indizes
                getProgressor().sendProgress("Erzeuge Indizes");
                int updatedRows;
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_PATIENT_TMP_PAT ON IMEX_PATIENT_TMP (PATNR) %s", (mImportConfig.isCaseDbOracle() ? "NOLOGGING PARALLEL 5" : "")));
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_DIAGNOSE_REF_NR ON IMEX_DIAGNOSE_TMP (REF_ICD_NR) %s", (mImportConfig.isCaseDbOracle() ? "NOLOGGING PARALLEL 5" : "")));
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_DIAGNOSE_DEP_NR ON IMEX_DIAGNOSE_TMP (DEP_NR) %s", (mImportConfig.isCaseDbOracle() ? "NOLOGGING PARALLEL 5" : "")));
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_DIAGNOSE_WARD_NR ON IMEX_DIAGNOSE_TMP (WARD_NR) %s", (mImportConfig.isCaseDbOracle() ? "NOLOGGING PARALLEL 5" : "")));
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_PROCEDURE_DEP_NR ON IMEX_PROCEDURE_TMP (DEP_NR) %s", (mImportConfig.isCaseDbOracle() ? "NOLOGGING PARALLEL 5" : "")));
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_PROCEDURE_WARD_NR ON IMEX_PROCEDURE_TMP (WARD_NR) %s", (mImportConfig.isCaseDbOracle() ? "NOLOGGING PARALLEL 5" : "")));
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_WARD_DEP_NR ON IMEX_WARD_TMP (DEP_NR) %s", (mImportConfig.isCaseDbOracle() ? "NOLOGGING PARALLEL 5" : "")));
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_FEE_RECHNUNG_NR ON IMEX_FEE_TMP (RECHNUNG_NR) %s", (mImportConfig.isCaseDbOracle() ? "NOLOGGING PARALLEL 5" : "")));
                //5th step: move data from temporary imex tables to non-temporary imex-tables
                /*
          for(LineEntity lineEntity: lineEntities) {
            //moveImexTable(stmt, lineEntity.imexTmpTableName, lineEntity.imexTableName);
            stmt.executeUpdate("ALTER TABLE " + lineEntity.imexTableName + " ADD (FALL_ID INT, PATIENT_ID INT)");
            updatePatientId(stmt, lineEntity.imexTableName);
            updateFallId(stmt, lineEntity.imexTableName);
          }
                 */
 /*
          //5th step: drop temporary imex tables
          for(LineEntity lineEntity: lineEntities) {
            dropTmpImexTable(lineEntity, stmt, pDatabaseName);
          }
                 */
            }
        }
    }

    protected void uploadImexTable(final LineEntity pLineEntity, final BufferedReader pBr, final Connection pConnection, final Statement pStmt) throws IOException, SQLException, ParseException {
        /*
      String imexTmpTableName;
      if (pDatabaseName == null || pDatabaseName.trim().isEmpty()) {
        imexTmpTableName = DbBuilder.getImexTmpTableName(pLineEntity.imexTmpTableName);
      } else {
        imexTmpTableName = DbBuilder.getImexTmpTableName(pDatabaseName, pLineEntity.imexTmpTableName);
      }
         */
        String tableName = pLineEntity.imexTmpTableName;
        //pStmt.execute(DbBuilder.getTableDropSilent(imexTmpTableName));
        //pStmt.execute(DbBuilder.getTmpImexTableDefinition(imexTmpTableName, pLineEntity.fieldSet));

        try (PreparedStatement prepStmt = pConnection.prepareStatement(DbBuilder.getImexPreparedStatement(tableName, pLineEntity.fieldSet))) {
            //pConnection.setAutoCommit(false);

            int counter = 0;
            Field[] fieldSet = pLineEntity.getFieldSet().toArray(new Field[pLineEntity.getFieldSet().size()]);

            String line = pBr.readLine(); //skip 1st line (headline)
            while ((line = pBr.readLine()) != null) {
                counter++;
                String[] sa = AbstractLine.splitLine(line);
                for (int i = 0; i < sa.length; i++) {
                    Field field = fieldSet[i];
                    String value = sa[i];
                    int idx = i + 1;
                    if (value.isEmpty()) {
                        int jdbcType = field.getType().getJdbcType();
                        if (value.isEmpty() && jdbcType == 16) {
                            //BOOLEAN and value==""
                            prepStmt.setNull(idx, Types.VARCHAR); //Probably you have to chose the right data type for some database systems here!
                        } else {
                            prepStmt.setNull(idx, jdbcType); //Probably you have to chose the right data type for some database systems here!
                        }
                        continue;
                    }
                    switch (field.getType()) {
                        case STRING:
                            prepStmt.setString(idx, value);
                            //continue;
                            break;
                        case INT:
                            prepStmt.setInt(idx, Integer.valueOf(value));
                            //continue;
                            break;
                        case FLOAT:
                            prepStmt.setFloat(idx, Float.valueOf(value));
                            //continue;
                            break;
                        case MONEY:
                            prepStmt.setFloat(idx, Float.valueOf(value));
                            //continue;
                            break;
                        case DATETIME:
                            prepStmt.setTimestamp(idx, Field.parseTimestamp(value));
                            //continue;
                            break;
                        case DATE:
                            prepStmt.setDate(idx, Field.parseDate(value));
                            //continue;
                            break;
                        case TIME:
                            prepStmt.setDate(idx, Field.parseTime(value));
                            //continue;
                            break;
                        case BOOLEAN:
                            prepStmt.setBoolean(idx, StrUtils.toBool(value));
                            //continue;
                            break;
                        case LONG:
                            prepStmt.setLong(idx, StrUtils.toLong(value));
                            //continue;
                            break;
                        default:
                            throw new IllegalArgumentException("Cannot decide which datatype to use for prepared statement: " + field.getType().name());
                    }
                }
                prepStmt.addBatch();
                if (counter % 250000 == 0) {
                    prepStmt.executeBatch();
                }
            }

            prepStmt.executeBatch();
        }
    }

    protected void dropTable(final Statement pStmt, final String pTableName, final boolean pIsOracle) throws IOException, SQLException, ParseException {
        pStmt.execute(DbBuilder.getTableDropSilent(pTableName, pIsOracle));
    }

    protected void createImexTable(final Statement pStmt, final String pTableName, final LineEntity pLineEntity) throws IOException, SQLException, ParseException {
        pStmt.execute(DbBuilder.getTmpImexTableDefinition(pTableName, pLineEntity.fieldSet, mImportConfig.isCaseDbOracle()));
        //createIndexes(pStmt, tableName);
    }

    protected void createIndexes(final Statement pStmt, final String pTableName) throws SQLException {
        pStmt.executeUpdate(String.format("CREATE INDEX IDX_%s_FAL ON %s (IKZ, FALLNR) %s", pTableName, pTableName, (mImportConfig.isCaseDbOracle() ? "PARALLEL 5 NOLOGGING" : "")));
        //pStmt.executeUpdate("CREATE INDEX IDX_" + pTableName + "_PAT ON " + pTableName + " (PATNR)");
        //pStmt.executeUpdate("CREATE INDEX IDX_" + pTableName + "_FALL_ID ON " + pTableName + " (FALL_ID)");
        //pStmt.executeUpdate("CREATE INDEX IDX_" + pTableName + "_PAT_ID ON " + pTableName+ " (PATIENT_ID)");
        pStmt.execute(DbBuilder.getImexPkDefinition(pTableName));
    }

    public ImportConfig<T> getImportConfig() {
        return mImportConfig;
    }

    public License getLicense() {
        return mImportConfig == null ? null : mImportConfig.getLicense();
    }

}

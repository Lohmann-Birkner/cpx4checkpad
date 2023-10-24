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
package transformer.impl;

import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.ImportModuleI;
import module.impl.ImportConfig;
import transformer.CpxTransformerI;

/**
 *
 * @author martin
 * @param <T> config type
 */
//@Singleton
public abstract class AbstractCpxTransformerCallback<T extends ImportModuleI<? extends CpxJobImportConfig>> /* implements CpxImportTransformerBeanLocal */ {

    static final Logger LOG = Logger.getLogger(AbstractCpxTransformerCallback.class.getName());
    //long mMeanTime = System.currentTimeMillis();
    //final long mStartupTime = mMeanTime;
    long mStartTime = System.currentTimeMillis();

    //final int mThreadCount = Runtime.getRuntime().availableProcessors();
    public void logTime(final long pStartTime, final String pMessage) {
        final long mEndTime = System.currentTimeMillis();
        final long mDiff = mEndTime - pStartTime;
        final Date startDate = new Date(pStartTime);
        final Date endDate = new Date(mEndTime);
        final Date diffDate = new Date(mDiff);
        final String dateFormat = "yyyy-MM-dd";
        final String timeFormat = "HH:mm:ss";
        final String datetimeFormat = dateFormat + " " + timeFormat;
        final SimpleDateFormat dfStart = new SimpleDateFormat(datetimeFormat);
        dfStart.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        final SimpleDateFormat dfEnd = new SimpleDateFormat(timeFormat);
        dfEnd.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        final SimpleDateFormat dfDiff = new SimpleDateFormat(timeFormat);
        dfDiff.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String startDateStr = dfStart.format(startDate);
        final String endDateStr = dfEnd.format(endDate);
        final String diffDateStr = dfDiff.format(diffDate);
        LOG.log(Level.INFO, "Start " + startDateStr + " - Ende " + endDateStr + " -> Dauer " + diffDateStr + ": " + pMessage);
        //mStartTime = System.currentTimeMillis();
    }

    /*
    public void logTime(final String pMessage) {
      logTime(mMeanTime, pMessage);
      mMeanTime = System.currentTimeMillis();
    }
     */
    public void printTime() {
        printTime("");
    }

    public void printTime(final String pMessage) {
        final long mEndTime = System.currentTimeMillis();
        final long mDiff = mEndTime - mStartTime;
        final Date endDate = new Date(mEndTime);
        final Date diffDate = new Date(mDiff);
        final String dateFormat = "yyyy-MM-dd";
        final String timeFormat = "HH:mm:ss";
        final String datetimeFormat = dateFormat + " " + timeFormat;
        final SimpleDateFormat dfStart = new SimpleDateFormat(datetimeFormat);
        dfStart.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        final SimpleDateFormat dfEnd = new SimpleDateFormat(timeFormat);
        dfEnd.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        final SimpleDateFormat dfDiff = new SimpleDateFormat(timeFormat);
        dfDiff.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String endDateStr = dfEnd.format(endDate);
        final String diffDateStr = dfDiff.format(diffDate);
        LOG.log(Level.INFO, "Zwischenzeit " + endDateStr + " -> Dauer " + diffDateStr + ((pMessage != null && !pMessage.trim().isEmpty()) ? (": " + pMessage) : ""));
    }

    /*
    public static void main(String[] args) throws Exception {
      CpxTransformerFactory bean = new CpxTransformerFactory();
      bean.NewSessionBean();
    }
     */
    //@Schedule(hour = "*", minute = "*", second = "*", persistent = false)
    //@TransactionTimeout(value = 180, unit = TimeUnit.MINUTES)
    public TransformResult transform(final ImportConfig<T> pImportConfig) throws NoSuchMethodException, IllegalArgumentException, IllegalStateException, InvocationTargetException, NoSuchFieldException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, InterruptedException {
        mStartTime = System.currentTimeMillis();
        printTime("Initialisierung abgeschlossen, starte Transformation in CPX-Format...");
        printTime("Input Directory: " + pImportConfig.getInputDirectory());
        printTime("Output Directory: " + pImportConfig.getOutputDirectory());
        TransformResult transformResult = null;
        try (CpxTransformerI<T> transformer = getTransformer(pImportConfig)) {
            //We will decide which concrete transformer class we have to use here...
            //        if (pImportConfig.getTransformer() != null) {
            //            transformer = getTransformer(pImportConfig);
            //        }
            //        if (transformer == null) {
            //            if (pImportConfig.isModuleClass(P21.class)) {
            //                transformer = new P21ToCpxTransformer((ImportConfig<P21>) pImportConfig);
            //            } else if (pImportConfig.isModuleClass(Fdse.class)) {
            //                transformer = new FdseToCpxTransformer((ImportConfig<Fdse>) pImportConfig);
            //            }
            //        }
            if (transformer == null) {
                throw new IllegalArgumentException("There is no transformer for this import available: " + pImportConfig.getModuleName() + ", you can set a Transformer via setTransformer() on ImportConfig object");
            }
            transformResult = transformer.start();
            //logTime("P21-Import abgeschlossen, starte Upload der IMEX-Dateien...");
            printTime("Transformation wurde abgeschlossen");
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalStateException | InvocationTargetException | NoSuchFieldException | IOException | InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException | ParseException | NoSuchAlgorithmException | InterruptedException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Most likely something went wrong in close method! You can maybe ignore this issue and fix this later", ex);
        }
        return transformResult;
    }

    public abstract CpxTransformerI<T> getTransformer(final ImportConfig<T> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, SQLException;


    public  TransformResult getDataFromPath(final ImportConfig<T> pImportConfig, final String inputPath) throws NoSuchMethodException, IllegalArgumentException, IllegalStateException, InvocationTargetException, NoSuchFieldException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, InterruptedException
    {
        return null;
    }

}

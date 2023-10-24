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
package transformer;

import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import module.ImportModuleI;
import module.impl.ImportConfig;
import util.CpxWriter;

/**
 *
 * @author Dirk Niemeier
 * @param <T> config type
 */
public abstract class AbstractCpxTransformer<T extends ImportModuleI<? extends CpxJobImportConfig>> implements CpxTransformerI<T> {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(AbstractCpxTransformer.class.getName());
    private final Set<String> mPatientKeys = new HashSet<>();
    protected final File mOutputDirectory;
    protected final Set<Exception> exceptions = new LinkedHashSet<>();
    protected final AtomicInteger patientCounter = new AtomicInteger();
    protected final AtomicInteger caseCounter = new AtomicInteger();
    protected final ImportConfig<T> mImportConfig;
    protected final CpxWriter mCpxMgr;

    public AbstractCpxTransformer(final ImportConfig<T> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        if (pImportConfig == null) {
            throw new IllegalArgumentException("Import config is null!");
        }
        mOutputDirectory = checkOutputDirectory(pImportConfig.getModule().getOutputDirectory());
        mImportConfig = pImportConfig;
        mCpxMgr = CpxWriter.getInstance(getOutputDirectory().getAbsolutePath());
    }

    @Override
    public void close() throws Exception {
        if (mCpxMgr != null) {
            mCpxMgr.close();
        }
    }

    protected CpxWriter getCpxMgr() {
        return mCpxMgr;
    }

    public Date getAdmissionDateFrom() {
        return getImportConstraint().getAdmissionDateFrom();
    }

    public Date getAdmissionDateTo(final int pDays) {
        return getImportConstraint().getAdmissionDateTo(pDays);
    }

    public Date getAdmissionDateTo() {
        return getImportConstraint().getAdmissionDateTo();
    }

    public Date getDischargeDateFrom() {
        return getImportConstraint().getDischargeDateFrom();
    }

    public Date getDischargeDateTo(final int pDays) {
        return getImportConstraint().getDischargeDateTo(pDays);
    }

    public Date getDischargeDateTo() {
        return getImportConstraint().getDischargeDateTo();
    }

    public Set<String> getCaseNumbers() {
        return getImportConstraint().getCaseNumbers();
    }

    public boolean hasCaseNumbers() {
        return getImportConstraint().hasCaseNumbers();
    }

    public Set<AdmissionCauseEn> getAdmissionCauses() {
        return getImportConstraint().getAdmissionCauses();
    }

    public boolean hasAdmissionCauses() {
        return getImportConstraint().hasAdmissionCauses();
    }

    public Set<AdmissionReasonEn> getAdmissionReasons() {
        return getImportConstraint().getAdmissionReasons();
    }

    public boolean hasAdmissionReasons() {
        return getImportConstraint().hasAdmissionReasons();
    }

    public Set<IcdcTypeEn> getIcdTypes() {
        return getImportConstraint().getIcdTypes();
    }

    public boolean hasIcdTypes() {
        return getImportConstraint().hasIcdTypes();
    }

    @Override
    public final CpxJobConstraints getImportConstraint() {
        return mImportConfig.getImportConstraint();
    }

    @Override
    public ImportConfig<T> getImportConfig() {
        return mImportConfig;
    }

    public final File getOutputDirectory() {
        return mOutputDirectory;
    }

    /**
     * adds patient key to set if it does not exist yet and returns false (it is
     * intended to be an atomic operation so 2 steps are combined in one!)
     *
     * @param pPatKey patient key (patient number in most cases)
     * @return false if does not exist yet
     */
    protected synchronized boolean patientKeyExists(final String pPatKey) {
        return !mPatientKeys.add(pPatKey);
    }

    protected Set<String> getPatientKeys() {
        return new HashSet<>(mPatientKeys);
    }

    public static File checkOutputDirectory(final String pDirectory) {
        File dir = checkDirectory(pDirectory, true);
        if (!dir.canWrite() || !Files.isWritable(dir.toPath())) {
            throw new IllegalArgumentException("No write permission: " + dir.getAbsolutePath());
        }
        return dir;
    }

    public static File checkDirectory(final String pDirectory) {
        return checkDirectory(pDirectory, false);
    }

    public static File checkDirectory(final String pDirectory, final boolean pCreateDirIfMissing) {
        String directory = (pDirectory == null) ? "" : pDirectory.trim();
        if (directory.isEmpty()) {
            throw new IllegalArgumentException("No directory given!");
        }
        File dir = new File(directory);
        if (!dir.exists()) {
            if (pCreateDirIfMissing) {
                if (!dir.mkdirs()) {
                    throw new IllegalArgumentException("Was not able to create directory: " + dir.getAbsolutePath());
                }
            } else {
                throw new IllegalArgumentException("Directory does not exist: " + dir.getAbsolutePath());
            }
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("No directory found, seems to be a file: " + dir.getAbsolutePath());
        }
        return dir;
    }

    public static List<File> getDirectoryFiles(final File pDir) {
        if (pDir == null) {
            throw new IllegalArgumentException("Directory is null!");
        }
        File dir = checkDirectory(pDir.getAbsolutePath(), false);
        List<File> files = Arrays.asList(dir.listFiles());
        return files;
    }

    public static File findFilename(final List<File> pFiles, final String pFilename) {
        String fileName = (pFilename == null) ? "" : pFilename.trim();
        if (fileName.isEmpty()) {
            throw new IllegalArgumentException("No file name given!");
        }
        if (pFiles == null) {
            throw new IllegalArgumentException("No list of files given!");
        }
        if (pFiles.isEmpty()) {
            return null;
        }

        final List<File> files = new ArrayList<>(pFiles);
        final Collator collator = Collator.getInstance(Locale.GERMAN);
        collator.setStrength(Collator.SECONDARY);// a == A, a < Ä
        files.sort((File o1, File o2) -> collator.compare(o1.getName(), o2.getName()));

        final Set<File> filesResultWithExt = new LinkedHashSet<>();
        final Set<File> filesResultWithoutExt = new LinkedHashSet<>();
        //final List<File> filesWithoutExtResult = new ArrayList<>();
        final String fnWithExtLower = fileName.toLowerCase();
        final String fnWithoutExtLower = removeFileExtension(fnWithExtLower);

        Iterator<File> it = files.iterator();
        while (it.hasNext()) {
            File file = it.next();
            final String fnWithExt = file.getName().toLowerCase();
            final String fnWithoutExt = removeFileExtension(file.getName().toLowerCase());
            if (fnWithExt.equalsIgnoreCase(fnWithExtLower)) {
                //awesome, we found exactly the file we expected!
                LOG.log(Level.FINEST, "File name found is exactly equal to '" + fnWithExtLower + "' (including file name extension): " + file.getName());
                return file;
            }
            if (fnWithExt.endsWith(fnWithExtLower) || fnWithExt.startsWith(fnWithExtLower)) {
                //hmmpf, not so ideal, but at least the file name extension (csv) is the same
                filesResultWithExt.add(file);
                continue;
            }
            if (fnWithoutExt.endsWith(fnWithoutExtLower) || fnWithoutExt.startsWith(fnWithoutExtLower)) {
                //arg, there is only little similarity to the file name we expect (even the file name extension is different!)
                filesResultWithoutExt.add(file);
                continue;
            }
        }
        if (filesResultWithExt.isEmpty() && filesResultWithoutExt.isEmpty()) {
            //did not find any files that are somewhat similar
            LOG.log(Level.FINEST, "No file name found that is similar to '" + fnWithExtLower + "' or '" + fnWithoutExtLower + "'");
            return null;
        }
        if (filesResultWithExt.size() == 1 && filesResultWithoutExt.isEmpty()) {
            //found exactly one similar file name with the same file name extension (csv)
            final File file = filesResultWithExt.iterator().next();
            LOG.log(Level.FINE, "Found file name that is similar to '" + fnWithExtLower + "' and that has the same file name extension: " + file.getName());
            return file;
        }
        if (filesResultWithoutExt.size() == 1 && filesResultWithExt.isEmpty()) {
            //found exactly one similar file name but with an unexpected file name extension (csv)
            final File file = filesResultWithoutExt.iterator().next();
            LOG.log(Level.FINE, "Found file name that is similar to '" + fnWithExtLower + "' except from the file name extension: " + file.getName());
            return file;
        }
        //okay, we have a choice of different similar file names -> let's spin the wheel of fortune!
        StringBuilder sb = new StringBuilder();
        final Set<File> filesResult = new LinkedHashSet<>();
        filesResult.addAll(filesResultWithExt);
        filesResult.addAll(filesResultWithoutExt);
        for (File file : filesResult) {
            sb.append("\n         -> " + file.getAbsolutePath());
        }
        LOG.log(Level.WARNING, "Multiple file names found that are similar to '" + fnWithExtLower + "' or '" + fnWithoutExtLower + "'" + sb.toString());
        if (!filesResultWithExt.isEmpty()) {
            final File file = filesResultWithExt.iterator().next();
            LOG.log(Level.FINE, "Ambiguous choice for file name. Will pick first one with same extension then '" + fnWithExtLower + "': " + file.getName());
            return file;
        }
        if (!filesResultWithoutExt.isEmpty()) {
            final File file = filesResultWithoutExt.iterator().next();
            LOG.log(Level.FINE, "Ambiguous choice for file name. Will pick first one with another extension then '" + fnWithExtLower + "': " + file.getName());
            return file;
        }
        return null;
        //return filesResultWithExt.iterator().next();
    }

    public static String getFileExtension(final String pFilename) {
        final int pos = pFilename.lastIndexOf('.');
        final String ext;
        if (pos > -1) {
            ext = pFilename.substring(pos + 1).trim();
        } else {
            ext = pFilename;
        }
        return ext;
    }

    public static String removeFileExtension(final String pFilename) {
        final int pos = pFilename.lastIndexOf('.');
        final String fileNameWithoutExt;
        if (pos > -1) {
            fileNameWithoutExt = pFilename.substring(0, pos).trim();
        } else {
            fileNameWithoutExt = pFilename;
        }
        return fileNameWithoutExt;
    }

    @Override
    public T getModule() {
        return mImportConfig.getModule();
    }

}

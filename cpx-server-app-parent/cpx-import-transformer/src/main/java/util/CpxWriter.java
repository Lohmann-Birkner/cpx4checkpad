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
package util;

import dto.DtoI;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import line.AbstractLine;
import line.LineEntity;
import line.LineI;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxWriter implements AutoCloseable {

    private static CpxWriter sInstance = null;
    private static final Logger LOG = Logger.getLogger(CpxWriter.class.getName());

    public static synchronized CpxWriter getInstance() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        return getInstance(null);
    }

    public static synchronized CpxWriter getInstance(final String pDirectory) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        //if (sInstance == null) {
        sInstance = new CpxWriter(pDirectory);
        //}
        return sInstance;
    }
    private final Map<String, FileManager> mFileManager;
    //final String mDirectory;
    //final Set<String> mStore;

    protected CpxWriter(final String pDirectory) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        String directory = (pDirectory == null) ? "" : pDirectory.trim();
        if (directory.isEmpty()) {
            throw new IllegalArgumentException("No output directory given!");
        }
        File file = new File(directory + File.separator);
        if (file.exists() && !file.isDirectory()) {
            throw new IllegalArgumentException("Path already exists as a file: " + file.getAbsolutePath());
        }
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IllegalArgumentException("Was not able to create directory: " + file.getAbsolutePath());
            }
        }
        directory = file.getAbsolutePath();
        final Map<String, FileManager> mapTmp = new HashMap<>();
        for (LineEntity lineEntity : AbstractLine.getLineEntities()) {
            FileManager fileManager = new FileManager(directory + File.separator + lineEntity.imexTmpFileName);
            mapTmp.put(lineEntity.clazzName, fileManager);
            fileManager.openFile();
            fileManager.writeFile(lineEntity.clazz.getDeclaredConstructor().newInstance().getHeadline());
        }
        //mFileManager = Collections.unmodifiableMap(mapTmp);
        mFileManager = mapTmp;
        //mDirectory = directory;
        //mStore = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public FileManager getFileManager(final LineEntity pLineEntity) {
        if (pLineEntity == null) {
            throw new IllegalArgumentException("No line entity given!");
        }
        return getFileManager(pLineEntity.clazzName);
    }

    public FileManager getFileManager(final Class<? extends LineI> pLineClass) {
        if (pLineClass == null) {
            throw new IllegalArgumentException("No line class given!");
        }
        return getFileManager(pLineClass.getSimpleName());
    }

    public FileManager getFileManager(final String pLineClassName) {
        String lineClassName = (pLineClassName == null) ? "" : pLineClassName.trim();
        if (lineClassName.isEmpty()) {
            throw new IllegalArgumentException("No line class name given!");
        }
        FileManager fileManager = mFileManager.get(lineClassName);
        if (fileManager == null) {
            throw new IllegalArgumentException("Invalid line class name given: " + lineClassName);
        }
        return fileManager;
    }

//  public void writeDeep(final Patient pPatient) throws IOException, InstantiationException, IllegalAccessException, UnsupportedEncodingException, NoSuchAlgorithmException {
//    if (pPatient == null) {
//      throw new CpxIllegalArgumentException("Patient is null!");
//    }
//    write(pPatient.getDtos());
//    //write(pPatient.toLine());
//  }
//
//  public void writeDeep(final Case pCase) throws IOException, InstantiationException, IllegalAccessException, UnsupportedEncodingException, NoSuchAlgorithmException {
//    if (pCase == null) {
//      throw new CpxIllegalArgumentException("Case is null!");
//    }
//    write(pCase.getDtos());
//    pCase.getPatient().removeCase(pCase);
//  }
//  private void write(final Set<DtoI> pDtoSet) throws IOException, InstantiationException, IllegalAccessException, UnsupportedEncodingException, NoSuchAlgorithmException {
//    if (pDtoSet == null) {
//      throw new CpxIllegalArgumentException("DTO Set is null!");
//    }
//    Iterator<DtoI> it = pDtoSet.iterator();
//    while(it.hasNext()) {
//      DtoI dto = it.next();
//      write(dto);
//    }
//  }
    public void write(final DtoI pDto) throws IOException {
        if (pDto == null) {
            throw new IllegalArgumentException("DTO is null!");
        }
        write(pDto.toLine());
        //mFileManager.get(pLine.getClass().getName()).writeFile(pLine.serialize());
    }

    private void write(final LineI pLine) throws IOException {
        if (pLine == null) {
            throw new IllegalArgumentException("line is null!");
        }
        /*
    if (pLine.isLineWritten()) {
      throw new CpxIllegalArgumentException("line was already written to file: " + pLine.toString());
    }
         */
//        final String nr = pLine.getNr();
//        if (!mStore.add(nr)) {
//            throw new IllegalArgumentException("line with the same number '" + pLine.getNr() + "' was already written to file: " + pLine.toString());
//        }
        try {
            String lineClassName = pLine.getClass().getSimpleName();
            String line = pLine.serialize();
            write(lineClassName, line);
        } catch (IOException ex) {
            //mStore.remove(nr);
            throw ex;
        } catch (NoSuchAlgorithmException ex) {
            throw new IOException(ex);
        }
        //pLine.lineWritten();
        //mFileManager.get(pLine.getClass().getName()).writeFile(pLine.serialize());
    }

//  private void write(final LineEntity pLineEntity, final String pLine) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException {
//    if (pLineEntity == null) {
//      throw new CpxIllegalArgumentException("No line entity given!");
//    }
//    write(pLineEntity.clazzName, pLine);
//  }
//  private void write(final Class<? extends LineI> pLineClass, final String pLine) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException {
//    if (pLineClass == null) {
//      throw new CpxIllegalArgumentException("No line class given!");
//    }
//    write(pLineClass.getSimpleName(), pLine);
//  }
    private void write(final String pLineClassName, final String pLine) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException {
        /*
    String line = (pLine == null)?"":pLine.trim();
    if (line.isEmpty()) {
      throw new CpxIllegalArgumentException("No line given!");
    }
         */
        if (pLine == null) {
            throw new IllegalArgumentException("No line given!");
        }
        /*
    String hash = AbstractLine.getLineHash(line);
    if (!mHashStore.add(hash)) {
      //Line already written to CSV file
      return;
    }
         */
 /*
    if (line.replace(AbstractLine.DELIMITER, "").trim().isEmpty()) {
      //Object has no values!
      return;
    }
         */
        FileManager fileManager = getFileManager(pLineClassName);
        fileManager.writeFile(pLine);
    }

    @Override
    public void close() {
        for (Map.Entry<String, FileManager> lEntry : mFileManager.entrySet()) {
            String key = lEntry.getKey();
            FileManager fileManager = lEntry.getValue();
            if (fileManager != null) {
                fileManager.closeFile();
                mFileManager.put(key, null);
                //mFileManager.remove(key);
            }
        }
        mFileManager.clear();
    }

}

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
package de;

import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Lokale Implentierung der FileUtils Klasse getEntryListFromJar() muss
 * projektbezogen angepasst werden, da sonst die Regel und Katalogdaten nicht
 * richtig verarbeitet werden koennen im Grouping-Vorgang Protokoll-Wert fuer
 * dass entpacken ist "vfs" und nicht wie sonst "UTF-8"
 *
 * @author Wilde
 */
public final class FileUtils {

    private static final Logger LOG = Logger.getLogger(FileUtils.class.getName());
    private static final char[] ILLEGAL_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public static boolean copyFile(File pFile, File tempFile) {
        try {
            org.apache.commons.io.FileUtils.copyFile(pFile, tempFile);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean copyFile2Dir(File pFile, File pDir) {
        try {
            org.apache.commons.io.FileUtils.copyFileToDirectory(pFile, pDir);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private FileUtils() {
        //Do you see something here? Nope!
    }

    public static List<String> readTextFromJar(final String s) {
        //LOGGER.log(Level.INFO,"read from path " + s);        
        String line;
        List<String> list = new ArrayList<>();
//       System.out.println(s);
        try {
            try ( InputStream is = FileUtils.class.getResourceAsStream("/" + s)) {
                try ( BufferedReader br = new BufferedReader(new InputStreamReader(is, CpxSystemProperties.DEFAULT_ENCODING))) {
                    while (null != (line = br.readLine())) {
                        list.add(line);
                    }
                }
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "read from path " + s, e);
        }
        return list;
    }

    public static void main(String[] args) {
        List<String> list = FileUtils.readTextFromJar("/datafile1.txt");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            LOG.log(Level.INFO, it.next());
        }

        list = FileUtils.readTextFromJar("/test/datafile2.txt");
        it = list.iterator();
        while (it.hasNext()) {
            LOG.log(Level.INFO, it.next());
        }
    }

//    /**
//     * possible solution for wrong deployment directory (/bin/content). Add
//     * jboss-vfs to dependencies! But suggested solution does not seem to work
//     * out of the box(?!). Found at https://developer.jboss.org/thread/172599
//     *
//     * @param aFilePath virtual path to file(?)
//     * @return real path to file
//     * @throws java.io.IOException error if file is invalid or does not exist
//     */
//    public static String getRealFilePath(String aFilePath) throws IOException {
//        VirtualFile vFile = VFS.getChild(aFilePath);
//        URI fileNameDecodedTmp = VFSUtils.getPhysicalURI(vFile);
//        String path = fileNameDecodedTmp.getPath();
//        return path;
//    }
    /**
     * List directory contents for a resource folder. Not recursive. This is
     * basically a brute-force implementation. Works for regular files and also
     * JARs.
     *
     * @author Greg Briggs
     * @param pClazz Any java class that lives in the same place as the
     * resources you want.
     * @param pPath Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     */
    public static String[] getResourceListing(final Class<?> pClazz, final String pPath) {
        String path = pPath;
        // clazz - the Grouper class:de.checkpoint.javaGrouper.generatedClasses.<className>
        //path - Package  as a directory path

        // get from file path
//        path = getRealFilePath(path);
        LOG.log(Level.INFO, "real path=" + path);

        URL dirURL = pClazz.getClassLoader().getResource(path);
        if (dirURL != null && "file".equals(dirURL.getProtocol())) {
            try {
                /* A file path: easy enough */
                return new File(dirURL.toURI()).list();
            } catch (URISyntaxException ex) {
                LOG.log(Level.SEVERE, "Error in dirURL", ex);
                return new String[0];
            }
        }

        if (dirURL == null) {
            /*
               * In case of a jar file, we can't actually find a directory.
               * Have to assume the same jar as clazz.
             */
            String me = pClazz.getName().replace(".", "/") + ".class";
            dirURL = pClazz.getClassLoader().getResource(me);
        }
        try {
            String[] ret = getEntryListFromJar(dirURL, ".class", path);
            if (ret == null) {
                LOG.log(Level.INFO, "try to read from xml, CPX - Properties");
                CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
                if (cpxProps != null) {
                    // use xml
                    path = path.replace('/', '.');

                    return getClassListFromXml(cpxProps.getCpxServerGrouperDir(), pClazz.getSimpleName(), path);
                } else {
                    throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
                }
            } else {
                return ret;
            }
        } catch (Exception ex) {
            LOG.log(Level.INFO, "try to read from xml, CPX - Properties");
            CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
            if (cpxProps != null) {
                // use xml
                path = path.replace('/', '.');

                return getClassListFromXml(cpxProps.getCpxServerGrouperDir(), pClazz.getSimpleName(), path);
            } else {
                throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
            }

        }

    }

    private static String[] getClassListFromXml(final String pCpxHome, final String pGrouperName, final String pPackageName) {
        LOG.log(Level.INFO, "Load from xml " + pCpxHome + " for " + pGrouperName + ".xml");
        Set<String> result = new HashSet<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            try ( FileInputStream fin = new FileInputStream(pCpxHome + pGrouperName + ".xml")) {
                Document doc = db.parse(fin);
                Element root = doc.getDocumentElement();
                NodeList nl = root.getElementsByTagName("type");
                if (nl != null) {
                    for (int i = 0; i < (nl == null ? 0 : nl.getLength()); i++) {
                        if (nl == null) {
                            continue;
                        }
                        Element el = (Element) nl.item(i);
                        String attr = el.getAttribute("name");
                        if (attr.equals(pPackageName)) {
                            nl = el.getChildNodes();
                            if (nl != null) {
                                for (int j = 0; j < nl.getLength(); j++) {
                                    Element el1 = (Element) nl.item(j);
                                    result.add(el1.getAttribute("name"));
                                }
                            }
                        }
                    }
                }
            }

            return result.toArray(new String[result.size()]);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return result.toArray(new String[result.size()]);
    }

    public static String[] getEntryListFromJar(final URL pDirURL, final String pSuffix, final String pPath) {
        if (pDirURL == null) {
            //throw new CpxIllegalArgumentException("Directory URL is null!");
            LOG.log(Level.SEVERE, "Directory URL is null!");
            return new String[0];
        }
        try {
            LOG.log(Level.INFO, "jar Protocol = " + pDirURL.getProtocol());
            LOG.log(Level.INFO, "jar Path = " + pDirURL.getPath());
            if ("jar".equals(pDirURL.getProtocol())) {
                /* A JAR path */
                String jarPath = pDirURL.getPath().substring(5, pDirURL.getPath().indexOf('!')); //strip out only the JAR file
//       System.out.println("jarPath = " + jarPath);
                Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
                try ( JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
                    Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                    while (entries.hasMoreElements()) {
                        String name = entries.nextElement().getName();
                        if (name.startsWith(pPath)) { //filter according to the path
                            String entry = name.substring(pPath.length() + 1);
                            //            System.out.println(name+ " " + entry);
                            if (entry.endsWith(".class")) {
                                result.add(entry);
                            }
                        }
                    }
                }
                return result.toArray(new String[result.size()]);
            }

            if ("vfs".equals(pDirURL.getProtocol())) {
                /* A JAR path */
                String jarPath = pDirURL.getPath().substring(1, pDirURL.getPath().indexOf(".jar")) + ".jar"; //strip out only the JAR file
                Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
                try ( JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
                    Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                    while (entries.hasMoreElements()) {
                        String name = entries.nextElement().getName();
                        if (!name.equals(pPath) && name.startsWith(pPath)) { //filter according to the path
                            String entry = name.substring(pPath.length() + 1);
                            if (entry.endsWith(".class")) {//suffix)){
                                result.add(entry);
                            }
                        }
                    }
                }
//        String out = "out: ";
//        for(String s : result){
//            out = out.concat(s+",");
//        }
//        System.out.println(out);
                return result.toArray(new String[result.size()]);
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Cannot load from dirURL" + pDirURL, e);
            return null;
        }
        return null; //2017-12-01 DNi: return null; instead?!
    }

    /**
     * Is this file name syntactically correct?
     *
     * @param sFile file to check
     * @return is valid?
     */
    public static String validateFilename(final String sFile) {
        if (sFile == null || sFile.trim().isEmpty()) {
            return sFile;
        }
        final String file = sFile.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
        if (isValidFilename(file)) {
            return file;
        }
        //File file = new File(sFile);
        //final String name = file.getName();
        final char replacement = '_';
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < file.length(); i++) {
            char ch = file.charAt(i);
            final char newCh;
            //if (Character.getType(ch) == Character.CONTROL) {
            if (Character.isISOControl(ch)) {
                //ignore control characters like line breaks
                continue;
            }
            boolean isIllegal = false;
            for (char c : ILLEGAL_CHARACTERS) {
                if (c == ch) {
                    isIllegal = true;
                    break;
                }
            }
            if (isIllegal) {
                newCh = replacement;
            } else {
                newCh = ch;
            }
//            if ((ch >= 'a' && ch <= 'z')
//                    || (ch >= 'A' && ch <= 'Z')
//                    || (ch >= '0' && ch <= '9')
//                    || ch == 'ä'
//                    || ch == 'Ä'
//                    || ch == 'ü'
//                    || ch == 'Ü'
//                    || ch == 'ö'
//                    || ch == 'Ö'
//                    || ch == 'ß'
//                    || ch == '-'
//                    || ch == '.'
//                    || ch == '_'
//                    || ch == ' '
//                    || ch == '#'
//                    || ch == '+'
//                    || ch == '='
//                    || ch == '§'
//                    || ch == '$'
//                    || ch == '&'
//                    || ch == '^'
//                    || ch == '@'
//                    || ch == '('
//                    || ch == ')'
//                    || ch == '~'
//                    || ch == '{'
//                    || ch == '}'
//                    || ch == '\''
//                    || ch == '!'
//                    || ch == '`'
//                    || ch == '%'
//                    ) {
//                newCh = ch;
//            } else {
//                newCh = replacement;
//            }
            sb.append(newCh);
        }
        return sb.toString();
    }

    /**
     * Is this file path syntactically correct?
     *
     * @param sFile file to check
     * @return is valid?
     */
    public static boolean isValidFilename(String sFile) {
        if (sFile == null || sFile.trim().isEmpty()) {
            return false;
        }
        if (!isValidFilepath(sFile)) {
            return false;
        }
        for (char ch : ILLEGAL_CHARACTERS) {
            if (sFile.indexOf(ch) > -1) {
                LOG.log(Level.FINEST, "Found illegal character, so this file name is syntactically incorrect: {0}", sFile);
                return false;
            }
        }
        return true;
    }

    /**
     * Is this file path syntactically correct?
     *
     * @param sFile file to check
     * @return is valid?
     */
    public static boolean isValidFilepath(String sFile) {
        if (sFile == null || sFile.trim().isEmpty()) {
            return false;
        }
        File file = new File(sFile);

        try {
            file.getCanonicalPath();
        } catch (IOException ex) {
            LOG.log(Level.FINEST, "Test failed, this file name is syntactically incorrect: " + sFile, ex);
            return false;
        }
        final String name = file.getName();
        for (char ch : ILLEGAL_CHARACTERS) {
            if (name.indexOf(ch) > -1) {
                LOG.log(Level.FINEST, "Found illegal character, so this file path is syntactically incorrect: {0}", sFile);
                return false;
            }
        }

//        if (name.equalsIgnoreCase(".") || name.equalsIgnoreCase("..")) {
//            LOG.log(Level.FINEST, "This is not a valid file name: " + sFile);
//            return false;
//        }
//        
//        //If you're a very anxious person, than you can also check for reserved names:
//        File file2 = new File(file.getAbsolutePath());
//        final String[] reservedWinNames = new String[]{"CON", "PRN", "AUX",
//            "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7",
//            "COM8", "COM9", "COM0", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5",
//            "LPT6", "LPT7", "LPT8", "LPT9", "LPT0"};
//        while(true) {
//            for (String reservedName : reservedWinNames) {
//                if (file2.getName().equalsIgnoreCase(reservedName)) {
//                    LOG.log(Level.FINEST, "This is not a valid file name, because it contains a reserved windows name: " + sFile);
//                    return false;
//                }
//            }
//            file2 = file2.getParentFile();
//            if (file2 == null) {
//                break;
//            }
//        };
        return true;
    }

    public static boolean isFileLock(final File pFile) {
        boolean isLock = true;
        if (pFile == null) {
            LOG.log(Level.SEVERE, "File is null!");
            return isLock;
        }
        if (!pFile.exists()) {
            LOG.log(Level.WARNING, "File " + pFile.getAbsolutePath() + " does not exist");
            return false;
        }
        try ( RandomAccessFile rf = new RandomAccessFile(pFile, "rw");  FileChannel fileChannel = rf.getChannel()) {

            // try to get a lock. If file already has an exclusive lock by another process
            LOG.log(Level.FINE, "Trying to acquire lock on " + pFile.getAbsolutePath());
            FileLock lock = fileChannel.tryLock();
            if (lock != null) {
                isLock = false;
                lock.release();
            }
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "File seems to be locked: {0}" + pFile.getAbsolutePath());
            LOG.log(Level.FINEST, "Error when checkFileLock: " + ex.getMessage(), ex);
            isLock = true;
        }
        return isLock;
    }

    public static boolean deleteFile(final File pFile, final boolean pDeleteOnExit) {
        if (pFile == null || !pFile.exists()) {
            return true;
        }
        if (pFile.isDirectory()) {
            LOG.log(Level.WARNING, "This is a directory but not a file: " + pFile.getAbsolutePath());
            //throw new IllegalArgumentException("This is a directory but not a file: " + pFile.getAbsolutePath());
            return false;
        }
        boolean deleted = false;
        try {
            Files.delete(pFile.toPath());
            deleted = true;
            LOG.log(Level.FINEST, "deleted file: {0}", pFile.getAbsolutePath());
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "was not able to delete file: " + pFile.getAbsolutePath(), ex);
        }
        if (!deleted && pDeleteOnExit) {
            pFile.deleteOnExit();
            LOG.log(Level.INFO, "file will be deleted on exit: " + pFile.getAbsolutePath());
        }
        return deleted;
    }

    public static boolean deleteFile(final File pFile) {
        return deleteFile(pFile, false);
    }

    public static void zipFiles(final File[] pSourceFiles, final File pTargetZipFile) throws IOException {
//        try {
        // create byte buffer
        byte[] buffer = new byte[1024];
        try ( FileOutputStream fos = new FileOutputStream(pTargetZipFile);  ZipOutputStream zos = new ZipOutputStream(fos)) {
            zos.setLevel(Deflater.BEST_SPEED);
            for (int i = 0; i < pSourceFiles.length; i++) {
                File srcFile = pSourceFiles[i];
                try ( FileInputStream fis = new FileInputStream(srcFile)) {
                    // begin writing a new ZIP entry, positions the stream to the start of the entry data
                    zos.putNextEntry(new ZipEntry(srcFile.getName()));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                }
            }
        }
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Error creating zip file: " + ex);
//            throw ex;
//        }
    }

}

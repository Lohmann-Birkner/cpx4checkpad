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
package de.lb.cpx.service.grouper;

import de.FileUtils;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
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

/**
 * Testklasse zum Test der Implementierung des Entpack-Algorithmus fuer die jars
 *
 * @author Wilde
 */
public class FileSystemUtil_test {

    private static final Logger LOG = Logger.getLogger(FileSystemUtil_test.class.getName());

    public static List<String> readTextFromJar(String s) {
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
            //e.printStackTrace();
            LOG.log(Level.SEVERE, "Something went wrong", e);
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

    /**
     * List directory contents for a resource folder. Not recursive. This is
     * basically a brute-force implementation. Works for regular files and also
     * JARs.
     *
     * @author Greg Briggs
     * @param clazz Any java class that lives in the same place as the resources
     * you want.
     * @param path Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws IOException throws exception when path can not be loaded
     */
    public static String[] getResourceListing(Class<?> clazz, String path) throws IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            /* A file path: easy enough */
            URI uri;
            try {
                uri = dirURL.toURI();
            } catch (URISyntaxException ex) {
                Logger.getLogger(FileSystemUtil_test.class.getName()).log(Level.SEVERE, "Cannot get URI of directory " + path, ex);
                return null;
            }
            return new File(uri).list();
        }

        if (dirURL == null) {
            /*
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
             */
            String me = clazz.getName().replace(".", "/") + ".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }
        return getEntryListFromJar(dirURL, ".class", path);

    }

    public static String[] getEntryListFromJar(URL dirURL, String suffix, String path) throws IOException {
        if (dirURL == null) {
            throw new IllegalArgumentException("Directory URL is null!");
        }
        LOG.log(Level.INFO, "jar Protocol = " + dirURL.getProtocol());
        LOG.log(Level.INFO, "jar Path = " + dirURL.getPath());
        if ("jar".equals(dirURL.getProtocol())) {
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf('!')); //strip out only the JAR file
//       System.out.println("jarPath = " + jarPath);
            Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
            try ( JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path)) { //filter according to the path
                        String entry = name.substring(path.length() + 1);
                        //            System.out.println(name+ " " + entry);
                        if (entry.endsWith(".class")) {
                            result.add(entry);
                        }
                    }
                }
            }
            return result.toArray(new String[result.size()]);
        }
        if ("vfs".equals(dirURL.getProtocol())) {
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(1, dirURL.getPath().indexOf(".jar")) + ".jar"; //strip out only the JAR file
//        System.out.println("jarPath = " + jarPath + " " + path);
            Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
            try ( JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
                //        System.out.println("file exist " + new File(jarPath).exists() + " path " + path + " pathlenght " + path.length());
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (!name.equals(path)) {
                        if (name.startsWith(path)) { //filter according to the path
                            //                    System.out.println("found with path " + name + " namelenght " + name.length());
                            String entry = name.substring(path.length());

                            //                    System.out.println(name+ " " + entry);
                            if (entry.endsWith(suffix)) {
                                result.add(entry);
                                //                         System.out.println(name+ " " + entry);
                            }
                        }
                    }
                }
            }
//        System.out.println("result Size " + result.size());
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }

}

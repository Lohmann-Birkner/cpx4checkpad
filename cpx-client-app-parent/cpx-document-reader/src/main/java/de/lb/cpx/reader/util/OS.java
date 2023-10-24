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
package de.lb.cpx.reader.util;

import java.io.File;

/**
 *
 * @author nandola
 */
public class OS {

    private static boolean inited = false;
    private static boolean isMacOS = false;
    private static boolean isWin32 = false;
    private static boolean isWin64 = false;
    private static boolean isLinux = false;

    public static String getSharedLibraryLoaderPath() {
        if (!inited) {
            init();
        }
        String currentDir = System.getProperty("user.dir");
        if (!currentDir.endsWith(File.separator)) {
            currentDir += File.separator;
        }
        if (isWin32) {
//            return currentDir + "lib" + File.separator + "Win32" + File.separator;
            return currentDir + "dll" + File.separator + "jacob-1.19-x86.dll";
        }
        if (isWin64) {
//            return currentDir + "lib" + File.separator + "Win64" + File.separator;
            return currentDir + "dll" + File.separator + "jacob-1.19-x64.dll";
        }
        if (isLinux) {  // There is no compatible dynamic link libraries (DLLs) or jnilib for Linux and Mac OS.
//            return currentDir + "dll" + File.separator + "Linux" + File.separator;
//            return currentDir + "jnilib" + File.separator + "jacob.jnilib";
        }
        if (isMacOS) {
//            return currentDir + "dll" + File.separator + "MacOSX" + File.separator;
//            return currentDir + "jnilib" + File.separator + "jacob.jnilib";
        }
        return null;
    }

    public static String getSharedLibraryExt() {
        if (!inited) {
            init();
        }
        if (isWin32) {
            return ".dll";
        }
        if (isWin64) {
            return ".dll";
        }
        if (isLinux) {
            return ".jnilib";
        }
        if (isMacOS) {
            return ".jnilib";
        }
        return null;
    }

    public static boolean isWindows() {
        if (!inited) {
            init();
        }
        return isWin32 || isWin64;
    }

    public static boolean isMacOS() {
        if (!inited) {
            init();
        }
        return isMacOS;
    }

    public static boolean isLinux() {
        if (!inited) {
            init();
        }
        return isLinux;
    }

    private static void init() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            if ("x86".equals(System.getProperty("os.arch"))) {
                isWin32 = true;
            } else {
                isWin64 = true;
            }
        } else if (os.contains("mac")) {
            isMacOS = true;
        } else if (os.contains("nux")) {
            isLinux = true;
        }
        inited = true;
    }
}

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
 *    2018  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
//package de.lb.cpx.client.core.util;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.channels.FileChannel;
//import java.nio.channels.FileLock;
//import java.nio.file.Files;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author niemeier
// */
//public class FileUtils {
//
//    private static final Logger LOG = Logger.getLogger(FileUtils.class.getName());
//    private static final char[] ILLEGAL_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
//
//    /**
//     * Is this file name syntactically correct?
//     *
//     * @param sFile file to check
//     * @return is valid?
//     */
//    public static String validateFilename(String sFile) {
//        if (sFile == null || sFile.trim().isEmpty()) {
//            return sFile;
//        }
//        if (isValidFilename(sFile)) {
//            return sFile;
//        }
//        //File file = new File(sFile);
//        //final String name = file.getName();
//        final char replacement = '_';
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < sFile.length(); i++) {
//            char ch = sFile.charAt(i);
//            final char newCh;
//            //if (Character.getType(ch) == Character.CONTROL) {
//            if (Character.isISOControl(ch)) {
//                //ignore control characters like line breaks
//                continue;
//            }
//            boolean isIllegal = false;
//            for (char c : ILLEGAL_CHARACTERS) {
//                if (c == ch) {
//                    isIllegal = true;
//                    break;
//                }
//            }
//            if (isIllegal) {
//                newCh = replacement;
//            } else {
//                newCh = ch;
//            }
////            if ((ch >= 'a' && ch <= 'z')
////                    || (ch >= 'A' && ch <= 'Z')
////                    || (ch >= '0' && ch <= '9')
////                    || ch == 'ä'
////                    || ch == 'Ä'
////                    || ch == 'ü'
////                    || ch == 'Ü'
////                    || ch == 'ö'
////                    || ch == 'Ö'
////                    || ch == 'ß'
////                    || ch == '-'
////                    || ch == '.'
////                    || ch == '_'
////                    || ch == ' '
////                    || ch == '#'
////                    || ch == '+'
////                    || ch == '='
////                    || ch == '§'
////                    || ch == '$'
////                    || ch == '&'
////                    || ch == '^'
////                    || ch == '@'
////                    || ch == '('
////                    || ch == ')'
////                    || ch == '~'
////                    || ch == '{'
////                    || ch == '}'
////                    || ch == '\''
////                    || ch == '!'
////                    || ch == '`'
////                    || ch == '%'
////                    ) {
////                newCh = ch;
////            } else {
////                newCh = replacement;
////            }
//            sb.append(newCh);
//        }
//        return sb.toString();
//    }
//
//    /**
//     * Is this file path syntactically correct?
//     *
//     * @param sFile file to check
//     * @return is valid?
//     */
//    public static boolean isValidFilename(String sFile) {
//        if (sFile == null || sFile.trim().isEmpty()) {
//            return false;
//        }
//        if (!isValidFilepath(sFile)) {
//            return false;
//        }
//        for (char ch : ILLEGAL_CHARACTERS) {
//            if (sFile.indexOf(ch) > -1) {
//                LOG.log(Level.FINEST, "Found illegal character, so this file name is syntactically incorrect: {0}", sFile);
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Is this file path syntactically correct?
//     *
//     * @param sFile file to check
//     * @return is valid?
//     */
//    public static boolean isValidFilepath(String sFile) {
//        if (sFile == null || sFile.trim().isEmpty()) {
//            return false;
//        }
//        File file = new File(sFile);
//
//        try {
//            file.getCanonicalPath();
//        } catch (IOException ex) {
//            LOG.log(Level.FINEST, "Test failed, this file name is syntactically incorrect: " + sFile, ex);
//            return false;
//        }
//        final String name = file.getName();
//        for (char ch : ILLEGAL_CHARACTERS) {
//            if (name.indexOf(ch) > -1) {
//                LOG.log(Level.FINEST, "Found illegal character, so this file path is syntactically incorrect: {0}", sFile);
//                return false;
//            }
//        }
//
////        if (name.equalsIgnoreCase(".") || name.equalsIgnoreCase("..")) {
////            LOG.log(Level.FINEST, "This is not a valid file name: " + sFile);
////            return false;
////        }
////        
////        //If you're a very anxious person, than you can also check for reserved names:
////        File file2 = new File(file.getAbsolutePath());
////        final String[] reservedWinNames = new String[]{"CON", "PRN", "AUX",
////            "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7",
////            "COM8", "COM9", "COM0", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5",
////            "LPT6", "LPT7", "LPT8", "LPT9", "LPT0"};
////        while(true) {
////            for (String reservedName : reservedWinNames) {
////                if (file2.getName().equalsIgnoreCase(reservedName)) {
////                    LOG.log(Level.FINEST, "This is not a valid file name, because it contains a reserved windows name: " + sFile);
////                    return false;
////                }
////            }
////            file2 = file2.getParentFile();
////            if (file2 == null) {
////                break;
////            }
////        };
//        return true;
//    }
//
//    public static boolean isFileLock(final File pFile) {
//        boolean isLock = true;
//        if (pFile == null) {
//            LOG.log(Level.SEVERE, "File is null!");
//            return isLock;
//        }
//        if (!pFile.exists()) {
//            LOG.log(Level.WARNING, "File " + pFile.getAbsolutePath() + " does not exist");
//            return false;
//        }
//        try (RandomAccessFile rf = new RandomAccessFile(pFile, "rw");
//                FileChannel fileChannel = rf.getChannel()) {
//
//            // try to get a lock. If file already has an exclusive lock by another process
//            LOG.log(Level.FINE, "Trying to acquire lock on " + pFile.getAbsolutePath());
//            FileLock lock = fileChannel.tryLock();
//            if (lock != null) {
//                isLock = false;
//                lock.release();
//            }
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Error when checkFileLock: " + ex.getMessage(), ex);
//            isLock = true;
//        }
//        return isLock;
//    }
//
//    public static boolean deleteFile(final File pFile, final boolean pDeleteOnExit) {
//        if (pFile == null || !pFile.exists()) {
//            return true;
//        }
//        if (pFile.isDirectory()) {
//            LOG.log(Level.WARNING, "This is a directory but not a file: " + pFile.getAbsolutePath());
//            //throw new IllegalArgumentException("This is a directory but not a file: " + pFile.getAbsolutePath());
//            return false;
//        }
//        boolean deleted = false;
//        try {
//            Files.delete(pFile.toPath());
//            deleted = true;
//            LOG.log(Level.FINEST, "deleted file: {0}", pFile.getAbsolutePath());
//        } catch (IOException ex) {
//            LOG.log(Level.WARNING, "was not able to delete file: " + pFile.getAbsolutePath(), ex);
//        }
//        if (!deleted && pDeleteOnExit) {
//            pFile.deleteOnExit();
//            LOG.log(Level.INFO, "file will be deleted on exit: " + pFile.getAbsolutePath());
//        }
//        return deleted;
//    }
//
//    public static boolean deleteFile(final File pFile) {
//        return deleteFile(pFile, false);
//    }
//
//}

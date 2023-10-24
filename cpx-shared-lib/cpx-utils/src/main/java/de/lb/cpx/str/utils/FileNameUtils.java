/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.str.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author wilde
 */
public class FileNameUtils {
    private static final String COPY_SUFFIX = " - Kopie";
    private static final String COUNT_SUFFIX = "(cnt)";
    private static final String UNKNOWN = "Unbekannt";
    
    public static boolean isOrginalName(String pFileName){
        pFileName = Objects.requireNonNullElse(pFileName, "");
        pFileName = pFileName.trim();
        return !pFileName.contains(COPY_SUFFIX);
    }
    public static String getOriginalName(String pFileName){
        Objects.requireNonNull(pFileName, "FileName can not be null");
        pFileName = pFileName.trim();
        if(!pFileName.contains(COPY_SUFFIX)){
            return pFileName;
        }
//        String[] split = pFileName.split(COPY_SUFFIX);
//        if(split.length == 1 || split.length == 2){
//            return split[0];
//        }
        int lastIndexOf = pFileName.lastIndexOf(COPY_SUFFIX);
        String substring1 = pFileName.substring(0, lastIndexOf);
//        String substring2 = pFileName.substring(lastIndexOf+1, pFileName.length());
        return substring1;
    }
//    public static String getCopyName(String pFileName,int pCount){
//        Objects.requireNonNull(pFileName, "FileName can not be null");
//        pFileName = pFileName.trim();
//        String orgName = getOriginalName(pFileName);
//        if(pCount == 0){
//            return pFileName;
//        }
//        if(pCount == 1){
//            return new StringBuilder(orgName).append(COPY_SUFFIX).toString();
//        }
//        return new StringBuilder(orgName).append(COPY_SUFFIX).append(COUNT_SUFFIX.replace("cnt", String.valueOf(pCount))).toString();
////        return pFileName;
//    }
    public static String getCopyName(String pFileName,int pCount){
        pFileName = Objects.requireNonNullElse(pFileName, UNKNOWN);
        pFileName = pFileName.trim();
        String orgName =pFileName; //getOriginalName(pFileName);
        if(pCount == 0){
            return pFileName;
        }
        if(pCount == 1){
            return new StringBuilder(orgName).append(COPY_SUFFIX).toString();
        }
        return new StringBuilder(orgName).append(COPY_SUFFIX).append(COUNT_SUFFIX.replace("cnt", String.valueOf(pCount))).toString();
//        return pFileName;
    }
    
    public static String addFileSuffix(final String pFileName, final String pSuffix) {
        final String extension = FilenameUtils.getExtension(pFileName);
        final String name = FilenameUtils.getBaseName(pFileName);
        final String fileName = name + pSuffix + "." + extension;
        return fileName;
    }

    public static boolean isWordDocument(File tempFile) {
        if(tempFile == null){
            return false;
        }
        return FilenameUtils.isExtension(tempFile.getName(), "doc","docx");
    }
}

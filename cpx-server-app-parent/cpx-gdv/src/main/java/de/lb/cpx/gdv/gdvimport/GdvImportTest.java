/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.gdvimport;

import de.lb.cpx.service.information.ConnectionString;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author gerschmann
 */
public class GdvImportTest {

    private static final Logger LOG = Logger.getLogger(GdvImportTest.class.getName());
    private static String gdvDirectory;
    private static String targetDirectory;
    private static String archivDirectory;
    private static String emailFrom;
    private static String emailPassword;
    private static String emailTo;
    private static String connString;

        
    public static void main(String args[]){

        if(args.length < 7){
            LOG.log(Level.INFO, " not enough arguments");
            System.exit(0);
        }
        
        gdvDirectory = args[0];
        targetDirectory = args[1];
        archivDirectory = args[2];
        emailFrom = args[3];
        emailPassword = args[4];
        emailTo = args[5];
        connString = args[6];
        ConnectionString connStr = new ConnectionString (connString);
        if (!connStr.isValidCaseDb()) {
            throw new IllegalArgumentException("No valid case database passed (e.g. dbsys1:CPX) passed (arg 1): " + connString);
        }
        
        GdvImportDocumentProcess process = new GdvImportDocumentProcess();

        process.importGdvImportDocuments(gdvDirectory, targetDirectory, archivDirectory, emailFrom, emailPassword, emailTo);

    }
    
 }

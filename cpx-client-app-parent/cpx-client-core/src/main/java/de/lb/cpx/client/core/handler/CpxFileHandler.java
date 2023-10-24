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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
//package de.lb.cpx.client.core.handler;
//
//import de.lb.cpx.client.core.MainApp;
//import java.io.IOException;
//import java.util.logging.FileHandler;
//
//public class CpxFileHandler extends FileHandler {
//
//    public CpxFileHandler() throws IOException {
//        //super(CpxSystemProperties.getInstance().getCpxClientLogDir() + getLogFile());
//        super(MainApp.LOGFILE_PATH.get());
//    }
//
////    private static String getLogFile() {
//////        Calendar cal = Calendar.getInstance();
//////        final int year = cal.get(Calendar.YEAR);
//////        final int month = cal.get(Calendar.MONTH);
//////        final int day = cal.get(Calendar.DAY_OF_MONTH);
//////        final int hour = cal.get(Calendar.HOUR_OF_DAY);
//////        final int minute = cal.get(Calendar.MINUTE);
//////        final int seconds = cal.get(Calendar.SECOND);
////        final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
////        return cpxProps.getCpxClientLogFile();
////    }
//}

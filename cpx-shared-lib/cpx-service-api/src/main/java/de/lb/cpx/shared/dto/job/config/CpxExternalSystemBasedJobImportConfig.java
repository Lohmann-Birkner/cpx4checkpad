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
package de.lb.cpx.shared.dto.job.config;

import de.checkpoint.drg.GDRGModel;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
public abstract class CpxExternalSystemBasedJobImportConfig extends CpxJobImportConfig {

    private static final long serialVersionUID = 1L;

    private final String server;
    private final int port;
    private final String user;
    private final String password;
    private final String defaultHosIdent;

    public CpxExternalSystemBasedJobImportConfig(
            final String pName,
            final String pTargetDatabase,
            final ImportMode pImportMode,
            final String pServer,
            final int pPort,
            final String pUser,
            final String pPassword,
            final String pDefaultHosIdent,
            final boolean pRebuildIndexes,
            final GDRGModel pGrouperModel,
            final CpxJobConstraints pConstraints,
            final long pTimePeriodValue,
            final ChronoUnit pTimePeriodUnit,
            final Date pBeginDate,
            final Date pEndDate,
            final boolean pActive,
            final String pWhatGroup) {
        super(pName, pTargetDatabase, pImportMode, pRebuildIndexes, pGrouperModel, pConstraints, pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive, pWhatGroup);
        this.server = pServer;
        this.port = pPort;
        this.user = pUser;
        this.password = pPassword;
        this.defaultHosIdent = pDefaultHosIdent;
    }

    public CpxExternalSystemBasedJobImportConfig(String server, int port, String user, String password, String defaultHosIdent, ImportMode pImportMode, boolean pRebuildIndexes, CpxJobConstraints pConstraints) {
        super(pImportMode, pRebuildIndexes, pConstraints);
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
        this.defaultHosIdent = defaultHosIdent;
    }

//    public CpxExternalSystemBasedJobImportConfig() {
//        //needs standard constructor!
//    }
    public String getUser() {
        return user;
    }

//    public CpxExternalSystemBasedJobImportConfig setUser(final String pUser) {
//        this.user = pUser;
//        return this;
//    }
    public String getPassword() {
        return password;
    }

//    public CpxExternalSystemBasedJobImportConfig setPassword(final String pPassword) {
//        this.password = pPassword;
//        return this;
//    }
    public String getServer() {
        return server;
    }

//    public CpxExternalSystemBasedJobImportConfig setServer(final String pServer) {
//        this.server = pServer;
//        return this;
//    }
    public int getPort() {
        return port;
    }

//    public CpxExternalSystemBasedJobImportConfig setPort(final int pPort) {
//        this.port = pPort;
//        return this;
//    }
    public String getDefaultHosIdent() {
        return defaultHosIdent;
    }

//    public CpxJobImportConfig setDefaultHosIdent(final String pDefaultHosIdent) {
//        this.defaultHosIdent = pDefaultHosIdent;
//        return this;
//    }
//    public String serialize() {
//        /*
//    StringWriter writer = new StringWriter();
//    JAXBContext context = JAXBContext.newInstance(UserProperties.class);
//    Marshaller m = context.createMarshaller();
//    //m.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.TRUE);
//    //m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
//    m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
//    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//    m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//    m.setProperty("com.sun.xml.internal.bind.indentString", "  "); //2 instead of 4 spaces for indentation
//    m.marshal(this, writer);    
//    return writer.toString();
//         */
//        return XmlSerializer.serialize(this);
//    }
//
//    public static CpxExternalInterface deserialize(final String pInput) {
//        return XmlSerializer.deserialize(pInput, CpxExternalInterface.class);
//    }
//
//    @Override
//    public String toString() {
//        return serialize();
//    }
}

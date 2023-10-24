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
package transformer.impl;

import dto.impl.Case;
import dto.impl.Department;
import dto.impl.Fee;
import dto.impl.Hauptdiagnose;
import dto.impl.Nebendiagnose;
import dto.impl.Patient;
import dto.types.Erbringungsart;
import dto.types.RefIcdType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import module.Sample;
import module.impl.ImportConfig;

/**
 * This demo module is just for your demonstration how to implement
 *
 * @author Dirk Niemeier
 */
public class SampleToCpxTransformer extends FileToCpxTransformer<Sample> {

    public SampleToCpxTransformer(final ImportConfig<Sample> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        super(pImportConfig);
    }

    @Override
    public TransformResult start() throws IOException, InstantiationException, IllegalAccessException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException {
        //create readers for files in mInputDirectory, for example...
        //try (final FileManager fileManager = new FileManager(pFile.getAbsolutePath())) {
        //    try (BufferedReader br = fileManager.getBufferedReader()) {
        //        while ((line = br.readLine()) != null) {
        //            //do something with line, e.g. String[] sa = AbstractLine.splitLine(line, ";");
        //        }
        //    }
        //}
//        try (final CpxWriter cpxMgr = CpxWriter.getInstance(getOutputDirectory().getAbsolutePath())) {
        //read file content and write information to file
        //Example for some dummy hospital cases from the scratch
        Random rand = new SecureRandom();
        for (int i = 1; i <= 100; i++) {
            final String patientNumber = "Patient No. " + (rand.nextInt(49) + 1);
            final String caseNumber = "Case No. " + i;
            if (!patientKeyExists(patientNumber)) {
                Patient patient = new Patient();
                patient.setPatNr(patientNumber);
                mCpxMgr.write(patient);
                patientCounter.incrementAndGet();
            }
            Case cs = new Case(patientNumber);
            cs.setIkz("260510461");
            cs.setFallNr(caseNumber);
            cs.setFallart("DRG");
            cs.setAufnahmedatum(new Date());
            cs.setAufnahmeanlass("E"); //N
            cs.setAufnahmegrund1("01");
            cs.setAufnahmegrund2("01");
            cs.setAlterInJahren(66);
            cs.setAlterInTagen(0);
            cs.setUrlaubstage(0);
            cs.setVwd(0);
            cs.setVwdSimuliert(0);
            mCpxMgr.write(cs);

            Department dep = new Department(cs);
            dep.setCode("0100");
            dep.setErbringungsart(Erbringungsart.HA);
            dep.setVerlegungsdatum(new Date());
            dep.setEntlassungsdatum(new Date());
            mCpxMgr.write(dep);

            Hauptdiagnose primIcd = new Hauptdiagnose(dep);
            primIcd.setCode("F10.2");
            mCpxMgr.write(primIcd);

            Nebendiagnose sekIcd = new Nebendiagnose(dep);
            sekIcd.setCode("I49.3");
            sekIcd.setRefIcd(primIcd);
            sekIcd.setRefIcdType(RefIcdType.Plus);
            mCpxMgr.write(sekIcd);

            Fee fee1 = new Fee(cs);
            fee1.setAnzahl(3);
            fee1.setEntgeltschluessel("0815");
            mCpxMgr.write(fee1);

            Fee fee2 = new Fee(cs);
            fee2.setAnzahl(7);
            fee2.setEntgeltschluessel("0816");
            mCpxMgr.write(fee2);

            caseCounter.incrementAndGet();
        }
//        }

        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
    }

}

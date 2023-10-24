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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.grouperEvaluation;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.file.writer.CpxFileWriter;
import de.lb.cpx.grouper.model.enums.GrouperEvalResultEn;
import de.lb.cpx.grouper.model.transfer.EvaluationCaseResult;
import de.lb.cpx.service.ejb.GrouperEvaluationRemote;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

/**
 *
 * @author gerschmann
 */
@Stateless
public class GrouperEvaluation implements GrouperEvaluationLocal {

    private static final String DIRECTORY_PATH = "C:\\referenceFiles\\";

    private final ReferenceFileImporter referenceFileImporter = new ReferenceFileImporter();

    @EJB(name = "GrouperEvaluationEJB")
    private GrouperEvaluationRemote grouperEvaluation;

    @Override
//    @TransactionTimeout(1500)
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public String evaluate4Model(String fileName, String model, boolean isLocal, String database) {

        CpxFileWriter writer = new CpxFileWriter();
        Set<String> keys = referenceFileImporter.importReferenceCase(DIRECTORY_PATH + fileName, isLocal);
        if (keys == null) {
            return "no reference cases imported";
        }

        EvaluationCaseResult result = new EvaluationCaseResult();

        GDRGModel md = GDRGModel.getModel2Name(model);
        try {
            grouperEvaluation.initDbUser(database);
        } catch (NoSuchElementException ex) {
            Logger.getLogger(GrouperEvaluation.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: could not login on database " + database;
        }

        Iterator<String> itr = keys.iterator();
        while (itr.hasNext()) {
            long timestamp = System.currentTimeMillis();
            String key = itr.next();
            result.setFullCount();
            String[] parts = key.split("_");
            if (parts.length != 2) {
                result.setErrCount();
                Logger.getLogger(GrouperEvaluation.class.getName()).log(Level.INFO, "key " + key + " is not valid");
                continue;
            }
            result = grouperEvaluation.evaluateCase(parts[0], parts[1], referenceFileImporter.get2key(key), md, result);// change to enum

            GrouperEvalResultEn ret = result.getResult();
            switch (ret) {

                case NotFound:
                    result.setErrCount();
                    writer.add2Content(result.getOneResult());
                    break;
                case OK:
                    result.setGoodCount();
                    writer.add2Content(result.getOneResult());
                    break;
                case Error:
                    result.setBadCount();
                    writer.add2Content(result.getOneResult());
                    break;
            }
            result.setTime4CaseFull(System.currentTimeMillis() - timestamp);
        }
        writer.add2Content(result.toString());

        writer.write(DIRECTORY_PATH + fileName + "_result_" + new Date().toString().replace(" ", "_").replace(":", "") + ".txt");

        return result.getXmlString();
    }

}

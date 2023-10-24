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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TCaseDetailsDao;
import de.lb.cpx.server.dao.TCaseIcdDao;
import de.lb.cpx.server.dao.TCaseOpsDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.service.jms.producer.StatusBroadcastProducer;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.hibernate.Hibernate;

/**
 *
 * @author wilde
 */
@Stateless
public class AddCaseEJB implements AddCaseEJBRemote {

    private static final Logger LOG = Logger.getLogger(AddCaseEJB.class.getName());

    private static final long serialVersionUID = 1L;

    @EJB
    private TPatientDao patientDao;
    @EJB
    private TCaseIcdDao icdDao;
    @EJB
    private TCaseOpsDao opsDao;
    @EJB
    private TCaseDao caseDao;
    @EJB
    private TCaseDetailsDao caseDetailsDao;
    @Inject
    private StatusBroadcastProducer<Long> broadcast;

    @Override
    public List<String> getMatchForPatientNumber(String number) {
        return patientDao.getMatchForPatientNumber(number);
    }

    @Override
    public TPatient getPatient(String patientNumber) {
        return patientDao.getPatientByPatientNumber(patientNumber);
    }

    @Override
    public List<TCaseIcd> getTestIcds(int count) {
        return icdDao.getFirstItems(count);
    }

    @Override
    public List<TCaseOps> getTestOps(int count) {
        return opsDao.getFirstItems(count);
    }

    @Override
    public Long savePatient(TPatient patient) {
        //if patient have a id merge, if not persist
        if (patient.getId() != 0L) {
            patient = patientDao.merge(patient);
        } else {
            patientDao.persist(patient);
        }
        patientDao.flush();
        return patient.getId();
    }

    @Override
    public Long saveCase(TCase hCase) {
        if (hCase.getPatient() != null && hCase.getPatient().getId() == 0L) {
            savePatient(hCase.getPatient());
        }
        //if patient have a id merge, if not persist
        if (hCase.getId() != 0L) {
            hCase = caseDao.merge(hCase);
        } else {
            caseDao.persist(hCase);
        }
        caseDao.flush();
        broadcast.send(BroadcastOriginEn.ADD_CASE, "Der Fall " + hCase.getCaseKey() + " wurde hinzugefügt", null, hCase.id);
        return hCase.getId();
    }

    @Override
    public boolean checkIfCaseExists(String caseNumber, String hospitalIdent) {
        return caseDao.findCaseForCaseNumberAndIdent(caseNumber, hospitalIdent) != null;
    }

    @Override
    public TCase findeCase(String caseNumber, String hospitalIdent) {
        return caseDao.findCaseForCaseNumberAndIdent(caseNumber, hospitalIdent);
    }

    @Override
    public TCase findExistingCase(String caseNumber, String hospitalIdent) {
        TCase hCase = caseDao.findCaseForCaseNumberAndIdent(caseNumber, hospitalIdent);
        Hibernate.initialize(hCase.getPatient().getInsurances());
        Hibernate.initialize(hCase.getPatient().getPatientDetailList());
        Hibernate.initialize(hCase.getPatient().getCases());
        return hCase;
    }

    @Override
    public void updateCase(TCase hCase) {
        int updatedRows = caseDetailsDao.getSession().createNativeQuery("UPDATE T_CASE_DETAILS SET ACTUAL_FL = 0 WHERE ACTUAL_FL = 1 AND T_CASE_ID = " + hCase.getId()).executeUpdate();
        LOG.log(Level.FINEST, "set {0} case details to actual = 0", updatedRows);
        for (TCaseDetails det : hCase.getCaseDetails()) {
            if (/*det.getId() == 0L && */!det.getCsdIsActualFl()) {
                if (det.getId() == 0L) {
                    caseDetailsDao.persist(det);
                } else {
                    caseDetailsDao.merge(det);
                }
            }
        }
        caseDetailsDao.flush();

        for (TCaseDetails det : hCase.getCaseDetails()) {
            if (/*det.getId() == 0L && */det.getCsdIsActualFl()) {
                if (det.getId() == 0L) {
                    caseDetailsDao.persist(det);
                } else {
                    caseDetailsDao.merge(det);
                }
            }
        }
        saveCase(hCase);

    }
}

/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.server.db.OSObject;
import de.checkpoint.server.rmServer.caseManager.RmcPeppWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TCaseMergeMappingDrg;
import de.lb.cpx.model.TCaseMergeMappingPepp;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.Rmc2CpxReadmRulesFieldsEn;
import de.lb.cpx.server.dao.TCaseMergeMappingDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.service.readmission.CheckReadmissionsService;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class ReadmissionServiceBean implements ReadmissionService {

    private static final Logger LOG = Logger.getLogger(ReadmissionServiceBean.class.getName());


    private final CheckReadmissionsService readmissionService = new CheckReadmissionsService();

    @Override
    public ArrayList<RmcWiederaufnahmeIF> checkReadmissions(ArrayList<RmcWiederaufnahmeIF> candidates, boolean isPepp) {
        if (candidates == null) {
            return null;
        }
        readmissionService.performDoRegeln(candidates, candidates.size() - 1, isPepp);
        return candidates;
    }


}

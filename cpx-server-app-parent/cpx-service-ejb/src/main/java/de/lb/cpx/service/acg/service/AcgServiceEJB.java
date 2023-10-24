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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.acg.service;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TAcgDataDao;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.service.acg.catalog.CatalogAcg;
import de.lb.cpx.service.ejb.AcgServiceEJBRemote;
import de.lb.cpx.shared.dto.acg.AcgPatientData;
import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author niemeier
 */
@Stateless
public class AcgServiceEJB implements AcgServiceEJBRemote {

    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    @Inject
    private TCaseDao caseDao;

    @Inject
    private TAcgDataDao acgDataDao;

    private static final Logger LOG = Logger.getLogger(AcgServiceEJB.class.getName());

    @Override
    public Map<Integer, AcgPatientData> getAcgData(String pPatientNumber) {
        return findByPatientId(pPatientNumber);
    }

    public Map<Integer, AcgPatientData> findByPatientId(final String pPatientNr) {
        if (!cpxServerConfig.getCommonHealthStatusVisualization()) {
            LOG.log(Level.FINE, "ACG visualization is disabled in server config");
            return new HashMap<>();
        }
        return acgDataDao.findByPatientId(pPatientNr);
    }

    @Override
    public List<IcdFarbeOrgan> getIcdData(long pCaseId) {
        final TCase cs = caseDao.findById(pCaseId);
        List<IcdFarbeOrgan> result = new ArrayList<>();
        if (cs == null) {
            LOG.log(Level.WARNING, "case with id {0} does not exist", pCaseId);
            return result;
        }
        final String databasePath = CpxSystemProperties.getInstance().getCpxServerCatalogAcgFile();
        try (CatalogAcg catalogAcg = new CatalogAcg(databasePath)) {
            for (TCaseIcd icd : cs.getCurrentLocal().getAllIcds()) {
                final String code = icd.getIcdcCode();
                IcdFarbeOrgan icdFarbeOrgan = catalogAcg.getIcdFarbeOrganByIcd(code);
                //tmpIcdList.add(icdFarbeOrgan);
                result.add(icdFarbeOrgan);
            }
//        } catch (IOException | SQLException ex) {
//            LOG.log(Level.SEVERE, MessageFormat.format("cannot open acg data base from path: {0}", databasePath), ex);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, MessageFormat.format("error occurect when acg data base was opened from path: {0}", databasePath), ex);
        }
        return result;
    }

//    public Map<Integer, AcgPatientData> findByPatientId(final String pPatientNr) throws IOException, SQLException, Exception {
//        if (!cpxServerConfig.getCommonHealthStatusVisualization()) {
//            LOG.log(Level.FINE, "ACG visualization is disabled in server config");
//            return new HashMap<>();
//        }
//        long startTime = System.currentTimeMillis();
//        try (final OutputDataSheet acgSheet = new OutputDataSheet()) {
//            final Map<Integer, AcgPatientData> acgPatientDataMap = acgSheet.findByPatientId(pPatientNr);
//            LOG.log(Level.FINER, "It took " + (System.currentTimeMillis() - startTime) + " ms to retrieve ACG data for patient number " + pPatientNr);
//            return acgPatientDataMap;
//        }
//    }
}

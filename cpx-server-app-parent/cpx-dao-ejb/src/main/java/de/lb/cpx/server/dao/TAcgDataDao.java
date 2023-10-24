/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TAcgData;
import de.lb.cpx.model.TAcgDataInfo;
import de.lb.cpx.model.TAcgDataInfo_;
import de.lb.cpx.model.TAcgData_;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.service.acg.catalog.CatalogAcg;
import de.lb.cpx.shared.dto.acg.AcgPatientData;
import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 * Data access object for domain model class TPatient. Initially generated at
 * 21.01.2016 17:14:39 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class TAcgDataDao extends AbstractCpxDao<TAcgData> {

    private static final Logger LOG = Logger.getLogger(TAcgDataDao.class.getName());

    @Inject
    TPatientDao patientDao;

    /**
     * Creates a new instance.
     */
    public TAcgDataDao() {
        super(TAcgData.class);
    }

    public Map<Integer, AcgPatientData> findByPatientId(final String pPatientNr) {
        TPatient pat = patientDao.findByPatNumber(pPatientNr);
        if (pat == null) {
            LOG.log(Level.WARNING, "unknown patient number: {0}", pPatientNr);
            return new HashMap<>();
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TAcgData> query = criteriaBuilder.createQuery(TAcgData.class);

        Root<TAcgData> from = query.from(TAcgData.class);
        Join<TAcgData, TAcgDataInfo> join = from.join(TAcgData_.acgDataInfo);
        query.where(criteriaBuilder.equal(from.get(TAcgData_.patient), pat));
        query.orderBy(criteriaBuilder.asc(join.get(TAcgDataInfo_.acgYear)), criteriaBuilder.asc(join.get(TAcgDataInfo_.acgPeriod)));

        TypedQuery<TAcgData> criteriaQuery = getEntityManager().createQuery(query);

        final Map<Integer, AcgPatientData> result = new LinkedHashMap<>();
        //final int MIN_DATENJAHR = -5;
        //final int MAX_DATENJAHR = 0;
//        final boolean fillGaps = true;
//
//        if (fillGaps) {
//            for (int jahr = MAX_DATENJAHR; jahr >= MIN_DATENJAHR; jahr--) {
//                //if (!acgPatientDataSet.keySet().contains(jahr)) {
//                result.put(jahr, null);
//                //}
//            }
//        }

        int lineNumber = 0;
//        int datenjahr = MIN_DATENJAHR - 1;
        final List<TAcgData> list = criteriaQuery.getResultList();
        int datenpunkt = 0 - list.size();
        final String databasePath = CpxSystemProperties.getInstance().getCpxServerCatalogAcgFile();
        try (CatalogAcg catalogAcg = new CatalogAcg(databasePath)) {
            for (TAcgData acgData : criteriaQuery.getResultList()) {
                lineNumber++;
                datenpunkt++;
//                datenjahr++;
//            Integer datenjahr = AcgPatientData.toInt(outputDataRow.getResultValue(COLUMN_DATENJAHR));
//            if (datenjahr == null) {
//                LOG.log(Level.SEVERE, "No datenjahr found in line number " + outputDataRow.lineNumber + ", I'll ignore this row in ACG file!");
//                continue;
//            }
//            if (!AcgPatientData.isDatenjahrValidRange(datenjahr)) {
//                LOG.log(Level.SEVERE, "Datenjahr in line number " + outputDataRow.lineNumber + " is out of range (found: " + datenjahr + ", I'll ignore this row in ACG file!");
//                continue;
//            }
//
//            final AcgPatientData acgPatientDataPrev = acgPatientDataMap.get(datenjahr);
//
//            List<IcdFarbeOrgan> icds = new ArrayList<>();
//            List<Integer> lineNumbers = new ArrayList<>();
//            if (acgPatientDataPrev != null) {
//                icds.addAll(acgPatientDataPrev.icd_code);
//                lineNumbers.addAll(acgPatientDataPrev.lineNumbers);
//            }
                List<IcdFarbeOrgan> icds = new ArrayList<>();
                String icd = acgData.getIcdCode();
                String[] sa = (icd == null ? "" : icd).split(" ");
                for (String s : sa) {
                    if (s == null || s.trim().isEmpty()) {
                        continue;
                    }
                    IcdFarbeOrgan icdFarbeOrgan = catalogAcg.getIcdFarbeOrganByIcd(s);
                    //tmpIcdList.add(icdFarbeOrgan);
                    icds.add(icdFarbeOrgan);
                }
//                lineNumbers.add(outputDataRow.lineNumber);

                List<Integer> lineNumbers = new ArrayList<>();
                lineNumbers.add(lineNumber);
                final String label = "";

                AcgPatientData acgPatientData = new AcgPatientData(
                        lineNumbers,
                        pat.getPatNumber(), //outputDataRow.getResultValue(COLUMN_PATIENTENNUMMER),
                        acgData.getAcgDataInfo().getAcgYear() + "", // outputDataRow.getResultValue(COLUMN_JAHR),
                        acgData.getAcgDataInfo().getAcgPeriod() + "",
                        datenpunkt + "",
                        acgData.getAcgDataInfo().getAcgDescription(),
                        Lang.toIsoDate(acgData.getDateOfBirth()),
                        //pat.getPatDateOfBirth(),
                        acgData.getAge() + "",
                        acgData.getSex().name(),
                        icds,
                        acgData.getChronicConditionCount() + "",
                        acgData.getChronicConditionCountAgeGroup() + "",
                        acgData.getAgeRelMacularDegCondition().name(),
                        acgData.getBipolarDisorderCondition().name(),
                        acgData.getCongestiveHeartFlrCondition().name(),
                        acgData.getDepressionCondition().name(),
                        acgData.getDiabetesCondition().name(),
                        acgData.getGlaucomaCondition().name(),
                        acgData.getHumanImdefVirusCondition().name(),
                        acgData.getDisordersOfLipidMCondition().name(),
                        acgData.getHypertensionCondition().name(),
                        acgData.getHypothyroidismCondition().name(),
                        acgData.getImmunoSupprTransCondition().name(),
                        acgData.getIschemicHeartDisCondition().name(), //COLUMN_KORONARE_HERZERKRANKUNG
                        acgData.getOsteoporosisCondition().name(),
                        acgData.getParkinsonsDiseaseCondition().name(),
                        acgData.getPersistentAsthmaCondition().name(),
                        acgData.getRheumatoidArthritisCondition().name(),
                        acgData.getSchizophreniaCondition().name(),
                        acgData.getSeizureDisordersCondition().name(), //ADIPOSITAS
                        acgData.getChronicObstPulDisCondition().name(),
                        acgData.getChronicRenalFlrCondition().name(),
                        acgData.getLowBackPainCondition().name(),
                        //getDeficiencyAnemiaCondition??
                        Lang.toDecimal(acgData.getReferenceRescaledWeight()),
                        Lang.toDecimal(acgData.getReferenceRescaledWeightAgeGroup()),
                        Lang.toDecimal(acgData.getResourceUtilizationBand()),
                        Lang.toDecimal(acgData.getResourceUtilizationBandAgeGroup()),
                        acgData.isFrailtyFlag() ? "1" : "0"
                );

                result.put(acgPatientData.datenjahr, acgPatientData);
            }
        } catch (IOException | SQLException ex) {
            LOG.log(Level.SEVERE, MessageFormat.format("cannot open acg data base from path: {0}", databasePath), ex);
        } catch (Exception ex) {
            LOG.log(Level.FINEST, "was likely not able to close CatalogAcg resource properly", ex);
        }
        return result;
    }

}

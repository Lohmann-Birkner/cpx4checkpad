/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.server.commonDB.model.CBookmarksCustomer;
import de.lb.cpx.server.commonDB.model.CDrafts;
import de.lb.cpx.wm.model.TWmDocument;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author nandola
 */
@Remote
public interface TemplateServiceBeanRemote {

    TCase getEntityForTemplate(long processId);

//    TCaseIcd getDetailsLocal();
//    TCaseIcd getDetailsExtern();
//    TGroupingResults getResultsLocalDao();
//    TGroupingResults getResultsExternDao();
    TGroupingResults getResultsLocal(long id, GDRGModel selectedGrouper);

    TGroupingResults getResultsExtern(long id, GDRGModel selectedGrouper);

    TGroupingResults getResultsLocal(TCase hCase, GDRGModel selectedGrouper);

    TGroupingResults getResultsExtern(TCase hCase, GDRGModel selectedGrouper);

    List<String> getAllTemplateDirectory();

//    List<Lob> getAllTemplateContent();
    List<byte[]> getAllTemplateContent();

    List<CDrafts> getAllTemplates();

    List<CDrafts> findAllTemplatesByCategoryInternalId(List<Long> cat1Id, List<Long> cat2Id, List<Long> cat13d);

//    TGroupingResults getResultsLocal(Session session, TCase hCase, GDRGModel selectedGrouper);
//
//    TGroupingResults getResultsExtern(Session session, TCase hCase, GDRGModel selectedGrouper);
    TWmDocument storeDocument(TWmDocument pDocument);

//    TWmDocument storeDoc(File myFile);
    Double findDrgBaserateFeeValue(String csHospitalIdent, Date csdAdmissionDate /*, String countryCode */);

    Double findCareBaserateFeeValue(String csHospitalIdent, Date csdAdmissionDate /*, String countryCode */);

    List<CBookmarksCustomer> getAllBookmarksCustomerEntries();

    byte[] getDraftContent(long draftId);

}

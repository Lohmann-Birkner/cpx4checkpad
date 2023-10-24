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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commonDB.model.CSearchList;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.service.searchlist.SearchListResult;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author Dirk Niemeier
 */
@Remote
//@SecurityDomain("cpx")
public interface SearchListServiceEJBRemote {

    Long getSelectedSearchListId(final SearchListTypeEn pList);

    boolean setSelectedSearchList(final CSearchList pCSearchList);

    SearchListProperties getSearchListProperties(final Long pSearchListId);

    Map<Long, SearchListResult> getSearchLists();

    Map<Long, SearchListResult> getSearchLists(final Long[] pIds);

    Map<Long, SearchListResult> getSearchLists(final SearchListTypeEn pList);

    Map<Long, SearchListResult> getSearchLists(final SearchListTypeEn pList, final Long[] pIds);

    long saveSearchList(final SearchListResult pSearchListResult, final boolean pIsSelected);

    boolean deleteSearchList(final CSearchList pSearchList);

    boolean setSearchListProperties(final Long pSearchListId, final SearchListProperties pSearchListProperties);

}

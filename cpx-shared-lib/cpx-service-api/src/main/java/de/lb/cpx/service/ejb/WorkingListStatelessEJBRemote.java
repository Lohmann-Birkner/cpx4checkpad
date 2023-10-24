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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import javax.ejb.Remote;

/**
 * Remote Interface of WorkingList Services
 *
 * @author Wilde
 */
@Remote
public interface WorkingListStatelessEJBRemote {

    /**
     * get Count of all available Entries in View VwInputCaseInfo
     *
     * @return ocunt of all available Entries in the View
     */
    Integer getMaxCount();

    /**
     * get Count of canceled cases
     *
     * @return ocunt of all available Entries in the View
     */
    Integer getCanceledCount();

    /**
     * get Count of all patients
     *
     * @return ocunt of all patients
     */
    Integer getMaxPatientCount();

    /**
     * find List of WorkingListItemDTO Objects from View VwInputCaseInfo, List
     * is filtered, by given arguments Filtering of the List of Entries in View
     * VwInputCaseInfo is based of Values in FilterOptions DTO only contains
     * Data if Column is shown in Client
     *
     * @param isLocal Local oder external case details
     * @param grouperModel Grouper Model
     * @param pPage Page
     * @param pFetchSize Amount of results per Page
     * @param options Map which defines Filter behaviour
     * @return List of WorkingListItemDTO Objects according to given FilterMap
     */
    Future<SearchResult<WorkingListItemDTO>> find(final boolean isLocal, GDRGModel grouperModel, final int pPage, final int pFetchSize, java.util.Map<ColumnOption, List<FilterOption>> options);

    List<Long> findAllCaseIds();

    /**
     * find all Case entites in the database for test purpose only
     *
     * @return list of all cases
     */
    List<TCase> findAllCases();

    /**
     * find all Case entites in the database, with its corresponting Patientdata
     * for test purpose only
     *
     * @return list of all cases with patient
     */
    List<TCase> findAllCasesWithPatient();

    /**
     * information about selected database
     *
     * @return database info
     */
    DatabaseInfo getDatabaseInfo();

    /**
     * information about common database
     *
     * @return database info common
     */
    DatabaseInfo getDatabaseInfoCommon();

    /**
     * gets a Case For the DetailPane in the WorkingList
     *
     * @param id long db id of the case
     * @return TCase entity
     */
    TCase getCaseForDetail(long id);

    /**
     * find working list searchResult without the async stuff
     *
     * @param pIsLocal consider local(cpx version) versions if true, false than
     * extern versions are used(his version)
     * @param pGrouperModel gdrg model, for grouping results
     * @param pPage max count of pages that are shown
     * @param pFetchSize size of items to fetch
     * @param pOptions filter options
     * @return search result object
     */
    SearchResult<WorkingListItemDTO> findWorkingList(final boolean pIsLocal, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, final java.util.Map<ColumnOption, List<FilterOption>> pOptions);

    /**
     * prepare for P21 export
     *
     * @param pIsLocal consider local(cpx version) versions if true, false than
     * extern versions are used(his version)
     * @param pGrouperModel gdrg model, for grouping results
     * @param pP21ExportSettings export settings
     * @param pSelectedIds only export entries this this case ids
     * @param pOptions filter options
     * @return execution id
     * @throws java.io.IOException something went horrible wrong...
     */
    long prepareP21Export(final boolean pIsLocal, final GDRGModel pGrouperModel, final P21ExportSettings pP21ExportSettings, final long[] pSelectedIds, java.util.Map<ColumnOption, List<FilterOption>> pOptions) throws IOException;

    /**
     * prepare for P21 export
     *
     * @param pIsLocal consider local(cpx version) versions if true, false than
     * extern versions are used(his version)
     * @param pIsShowAllReminders reminder
     * @param pGrouperModel gdrg model, for grouping results
     * @param pListType list type (workflow list, working list)
     * @param pExportType export type (CSV/EXCEL)
     * @param pSelectedIds only export entries this this case ids
     * @param pOptions filter options
     * @return execution id
     */
    public long prepareListExport(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final SearchListTypeEn pListType, final ExportTypeEn pExportType, final long[] pSelectedIds, final Map<ColumnOption, List<FilterOption>> pOptions);

    /**
     * number of maximum phases
     *
     * @return max. phases
     */
    int getMaxPhases();

    /**
     * stops running P21 export
     *
     * @param pExecutionId execution id for jms communication
     * @return stopped?
     */
    boolean stopExport(final long pExecutionId);

    /**
     * stops running list export
     *
     * @param pExecutionId execution id for jms communication
     * @return stopped?
     */
    boolean stopExport2(final long pExecutionId);
    
    boolean isCaseCreateAnonymized();
}

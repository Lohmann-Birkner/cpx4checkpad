/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  hasse - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.menu.filterlists;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListRuleAttributes;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author hasse
 */
public class RuleListFilterManager {

    private static RuleListFilterManager instance;

    private PoolTypeEn mPoolType = PoolTypeEn.DEV;
    private SearchListResult mSearchListDev;
    private SearchListResult mSearchListProd;
    private boolean hasMessage;

    private RuleListFilterManager() {
        mSearchListDev = new SearchListResult(Session.instance().getCpxUserId(), SearchListTypeEn.RULEWL);
        mSearchListDev.setColumns(getColumnOptions());
        mSearchListProd = new SearchListResult(Session.instance().getCpxUserId(), SearchListTypeEn.RULEWL);
        mSearchListProd.setColumns(getColumnOptions());
    }

    public static RuleListFilterManager getInstance() {
        if (RuleListFilterManager.instance == null) {
            RuleListFilterManager.instance = new RuleListFilterManager();
        }
        return RuleListFilterManager.instance;
    }

    public SearchListResult getSearchList() {
        if (mPoolType.equals(PoolTypeEn.DEV)) {
//            checkOrFillColumns(mSearchListDev);
            return checkOrFillColumns(mSearchListDev);
        } else {
            return checkOrFillColumns(mSearchListProd);
        }
    }

    public void setSearchList(SearchListResult pSearchList) {
        if (mPoolType.equals(PoolTypeEn.DEV)) {
            mSearchListDev = pSearchList;

        } else {
            mSearchListProd = pSearchList;
        }

    }

    public PoolTypeEn getPoolType() {
        return mPoolType;
    }

    public void setPoolType(PoolTypeEn mPoolType) {
        this.mPoolType = mPoolType;

    }

    public final Set<ColumnOption> getColumnOptions() {
//        Set<ColumnOption> list = new HashSet<>();
//        ColumnOption opt1 = new ColumnOption("!", "crgrMessage", true);
//        opt1.setSize(27);
//        opt1.setNumber(1);
//        ColumnOption opt2 = new ColumnOption("Regelnummer", "crgrNumber", true);
//        opt2.setSize(120);
//        opt2.setNumber(2);
//        ColumnOption opt3 = new ColumnOption("Bezeichnung", "crgrCaption", true);
//        opt3.setSize(420);
//        opt3.setNumber(3);
//        ColumnOption opt4 = new ColumnOption("Kategorie", "crgrCategory", true);
//        opt4.setSize(150);
//        opt4.setNumber(4);
//        ColumnOption opt5 = new ColumnOption("Identnr", "crgrIdentifier", true);
//        opt5.setSize(180);
//        opt5.setNumber(5);
//        ColumnOption opt6 = new ColumnOption("Vorschlag", "crgrSuggText", true);
//        opt6.setSize(220);
//        opt6.setNumber(6);
//        ColumnOption opt7 = new ColumnOption("Status", "crgrRuleErrorType", true);
//        opt7.setSize(90);
//        opt7.setNumber(7);
//        ColumnOption opt8 = new ColumnOption("Typ", "crgRuleTypes", true);
//        opt8.setSize(90);
//        opt8.setNumber(8);
//
//        list.add(opt1);
//        list.add(opt2);
//        list.add(opt3);
//        list.add(opt4);
//        list.add(opt5);
//        list.add(opt6);
//        list.add(opt7);
//        list.add(opt8);
//        return list;
        List<String> columns = hasMessage?SearchListRuleAttributes.getMessageColumns():SearchListRuleAttributes.getDefaultColumns();
        return columns.stream().map((t) -> {
            SearchListAttribute att = SearchListRuleAttributes.instance().get(t);
            ColumnOption opt = new ColumnOption(att.getTranslation().getAbbreviation(), att.getKey(), true);
            opt.setSize(att.getSize());
            opt.setNumber(columns.indexOf(t));
            return opt;
        }).collect(Collectors.toSet());
    }

    public void hasMessages(boolean containsErroneousRule) {
        hasMessage = containsErroneousRule;
//        getSearchList().setColumns(getColumnOptions());
        resetColumns();
    }

    void resetColumns() {
        mSearchListDev.getColumns().clear();
        mSearchListProd.getColumns().clear();
    }

    private SearchListResult checkOrFillColumns(SearchListResult pSerchList) {
        if(pSerchList.getColumns().isEmpty()){
            pSerchList.setColumns(getColumnOptions());
        }
        return pSerchList;
    }
}

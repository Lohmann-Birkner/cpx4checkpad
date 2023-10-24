/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.cache;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.searchlist.SearchListResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author wilde
 */
public class MenuCacheSearchListsTest {
    @Test
    public void testGetSearchListOriginalName(){
        SearchListResult sr1 = new SearchListResult(0L, "test", SearchListTypeEn.RULE);
        List<SearchListResult> results = new ArrayList<SearchListResult>();
        results.add(sr1);
        String cName = MenuCache.getMenuCacheSearchLists().getSearchListCopyName(sr1, results);
        Assert.assertEquals("test - Kopie", cName);
        SearchListResult sr2 = new SearchListResult(0L, cName, SearchListTypeEn.RULE);
        results.add(sr2);
        
        String cName2 = MenuCache.getMenuCacheSearchLists().getSearchListCopyName(sr1, results);
        Assert.assertEquals("test - Kopie(2)", cName2);
        SearchListResult sr3 = new SearchListResult(0L, cName2, SearchListTypeEn.RULE);
        results.add(sr3);
        
        String cName3 = MenuCache.getMenuCacheSearchLists().getSearchListCopyName(sr2, results);
        Assert.assertEquals("test - Kopie - Kopie", cName3);
        SearchListResult sr4 = new SearchListResult(0L, cName3, SearchListTypeEn.RULE);
        results.add(sr4);
        
        String cName4 = MenuCache.getMenuCacheSearchLists().getSearchListCopyName(sr3, results);
        Assert.assertEquals("test - Kopie(2) - Kopie", cName4);
        SearchListResult sr5 = new SearchListResult(0L, cName4, SearchListTypeEn.RULE);
        results.add(sr5);
        
        String cName5 = MenuCache.getMenuCacheSearchLists().getSearchListCopyName(sr3, results);
        Assert.assertEquals("test - Kopie(2) - Kopie(2)", cName5);
        SearchListResult sr6 = new SearchListResult(0L, cName5, SearchListTypeEn.RULE);
        results.add(sr6);
        
        String cName6 = MenuCache.getMenuCacheSearchLists().getSearchListCopyName(sr1, results);
        Assert.assertEquals("test - Kopie(3)", cName6);
        SearchListResult sr7 = new SearchListResult(0L, cName6, SearchListTypeEn.RULE);
        results.add(sr7);
    }
}

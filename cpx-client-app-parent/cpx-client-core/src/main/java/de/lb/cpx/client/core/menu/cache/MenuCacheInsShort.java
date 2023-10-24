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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.cache;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * key = insurance identifier (IKZ)
 *
 * @author niemeier
 */
public class MenuCacheInsShort extends MenuCacheEntry<String, String> {

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<String, String> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getInsShortNames();
//        List<String> insuranceListe = bean.get().getInsShortNames();
//        Map<String, String> map = new LinkedHashMap<>();
//        String insName = "";
//        for (String ins : insuranceListe) {
//            if (ins != null) {
//                insName = CpxInsuranceCompanyCatalog.instance().findInsNameByInsuranceNumber(String.valueOf(ins), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//            if (insName != null) {
//                map.put(ins, insName);
//            }
//        }
//        //CPX-994 sort insurance by insName
//        map = sortByComparator((HashMap<String, String>) map);
//        return map;
    }
    
    public List<String> getShortNames() {
        Map<String, String> m = map();
        final Set<String> result = new TreeSet<>();
        Iterator<Map.Entry<String, String>> it = m.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            result.add(entry.getValue());
        }
        return new ArrayList<>(result);
    }

//    // CPX-994 sort a HashMap by Value (not by Key)
//    private HashMap<String, String> sortByComparator(HashMap<String, String> unsortMap) {
//
//        List<Map.Entry<String, String>> list = new LinkedList<>(
//                unsortMap.entrySet());
//
//        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
//            @Override
//            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        });
//
//        HashMap<String, String> sortedMap = new LinkedHashMap<>();
//        for (Map.Entry<String, String> entry : list) {
//            sortedMap.put(entry.getKey(), entry.getValue());
//        }
//        return sortedMap;
//    }

    @Override
    public MenuCacheInsShort getCopy() {
        return (MenuCacheInsShort) super.getCopy(new MenuCacheInsShort());
    }

//    /**
//     * @param insName insurance name
//     * @return ikz of the insurance, null if name is not found in map
//     */
//    public String getInsuranceIkz(String insName) {
//        if (insName == null) {
//            return null;
//        }
//        Iterator<String> it = keySet().iterator();
//        while (it.hasNext()) {
//            String next = it.next();
//            if (insName.equals(get(next))) {
//                return next;
//            }
//        }
//        return null;
//    }

    @Override
    public String getName(final String pKey) {
        return get(pKey);
//        return getInsuranceIkz(pKey);
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.INS_SHORT;
    }

    public String getInsuranceShortForIkz(String ikz) {
        return CpxInsuranceCompanyCatalog.instance().findInsShortNameByInsuranceNumber(ikz, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

}

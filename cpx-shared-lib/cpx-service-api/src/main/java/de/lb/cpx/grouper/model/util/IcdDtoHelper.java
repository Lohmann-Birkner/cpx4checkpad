/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.util;

import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.LocalisationEn;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * class to generate the dto for values TODO: unify helper, thos is doublicated
 * code some functions like "cast" or compare could bemoved to dtos
 *
 * @author wilde
 */
public class IcdDtoHelper {

    private static final Logger LOG = Logger.getLogger(IcdDtoHelper.class.getName());

//    /**
//     * @param versionIcdMap map for id (details id) and all icds for that
//     * details
//     * @return IcdOverviewDTO list to show in simulation
//     */
//    public List<IcdOverviewDTO> computeDtoList(HashMap<Long, List<TCaseIcd>> versionIcdMap) {
//        List<IcdOverviewDTO> overview = new ArrayList<>();
//        int index = 0;
//        do {
//            List<IcdOverviewDTO> tmp = new ArrayList<>();
//            versionLoop:
//            for (long version : versionIcdMap.keySet()) {
//                if (versionIcdMap.get(version).size() <= index) {
//                    continue;
//                }
//                TCaseIcd tmpIcd = versionIcdMap.get(version).get(index);
//                for (IcdOverviewDTO dto : tmp) {
//                    if (dto.getIcdCode().equals(tmpIcd.getIcdcCode())) {
//                        dto.addIcdForVersion(String.valueOf(version), tmpIcd);
//                        dto.getOccurance().add(version);
//                        continue versionLoop;
//                    }
//                }
////                versionIcdMap.get(version).add(index,new TCaseIcd());
//                IcdOverviewDTO newDto = new IcdOverviewDTO(tmpIcd.getIcdcCode(),tmpIcd.getIcdcLocEn(), "");
//                newDto.addIcdForVersion(String.valueOf(version), tmpIcd);
//                newDto.getOccurance().add(version);
//                tmp.add(newDto);
//            }
//            overview.addAll(tmp);
//            index++;
//
//        } while (!checkSize(index, versionIcdMap.values()));
//        overview.sort(Comparator.comparing(IcdOverviewDTO::hasHbxFl).reversed().thenComparing(IcdOverviewDTO::getIcdCode));
//        return overview;
//    }
    /**
     * @param versionIcdMap map for id (details id) and all icds for that
     * details
     * @return list of IcdOverviewDto, for case merging
     */
    public List<IcdOverviewDTO> computeDtoMergeList(HashMap<Long, List<TCaseIcd>> versionIcdMap) {
        List<IcdOverviewDTO> overview = new ArrayList<>();
        Iterator<Long> it = versionIcdMap.keySet().iterator();
        while (it.hasNext()) {
            Long version = it.next();
            List<TCaseIcd> icds = versionIcdMap.get(version);
            icds.sort(Comparator.comparing(TCaseIcd::getIcdcCode));
            Iterator<TCaseIcd> icdIt = icds.iterator();
            while (icdIt.hasNext()) {
//            versionLoop:for(TCaseIcd next : opses){
                TCaseIcd next = icdIt.next();
                for (IcdOverviewDTO dto : overview) {
                    if (dto.getIcdCode().equals(next.getIcdcCode())) {
//                        if(dto.getOccurance().size()<2){
                        dto.addIcdForVersion(String.valueOf(version), next);
                        dto.getOccurance().add(version);
//                            continue versionLoop;
                        icdIt.remove();
                        break;
                    }
//                    }
                }
            }
            if (!icds.isEmpty()) {
                for (TCaseIcd icd : icds) {
                    IcdOverviewDTO newDto = new IcdOverviewDTO(icd.getIcdcCode(), icd.getIcdcLocEn(), "");
                    newDto.addIcdForVersion(String.valueOf(version), icd);
                    newDto.getOccurance().add(version);
                    overview.add(newDto);
                }
            }
        }
        overview.sort(Comparator.comparing(IcdOverviewDTO::hasHbxFl).reversed().thenComparing(IcdOverviewDTO::getIcdCode));
        return overview;
    }
    //Helper-Methode compute if an item has still items that are beyond the current index

//    private boolean checkSize(int index, Collection<List<TCaseIcd>> values) {
//        List<Boolean> hasMore = new ArrayList<>();
//        for (List<TCaseIcd> val : values) {
//            hasMore.add(val.size() <= index);
//
//        }
//        return hasMore.stream().allMatch(new Predicate<Boolean>() {
//            @Override
//            public boolean test(Boolean t) {
//                return t.equals(Boolean.TRUE);
//            }
//        });
//    }
    /**
     * TODO: could refactor alg not to use for-loop in for-loop -&gt; costly to
     * do this kind of thing second algorithm to compute list of dtos to compare
     * versions
     *
     * @param versionIcdMap map of version(tcasedetails) to list of ops
     * @return list of dto objects to compare icd between versions
     */
    public List<IcdOverviewDTO> computeDtoList(HashMap<Long, List<TCaseIcd>> versionIcdMap) {
        long start = System.currentTimeMillis();
        List<IcdOverviewDTO> tmp = new ArrayList<>();
        versionLoop:
        for (long version : versionIcdMap.keySet()) {
            List<TCaseIcd> opsList = versionIcdMap.get(version);
            opsLoop:
            for (TCaseIcd tmpOps : opsList) {
                for (IcdOverviewDTO dto : tmp) {
                    if (compare(tmpOps, dto)) {
                        if (dto.getIcdForVersion(String.valueOf(version)) != null) {
                            continue;
                        }
                        dto.addIcdForVersion(String.valueOf(version), tmpOps);
                        dto.getOccurance().add(version);
                        continue opsLoop;
                    }
                }
                IcdOverviewDTO newDto = new IcdOverviewDTO(tmpOps.getIcdcCode(), tmpOps.getIcdcLocEn(), "");
                newDto.addIcdForVersion(String.valueOf(version), tmpOps);
                newDto.getOccurance().add(version);
                tmp.add(newDto);
            }
        }
        LOG.fine("time to compute ops list: " + (System.currentTimeMillis() - start));
        return tmp;
    }

    private boolean compare(TCaseIcd pIcd, IcdOverviewDTO pDto) {
        if (!pIcd.getIcdcCode().equals(pDto.getIcdCode())) {
            return false;
        }
        if (castLoc(pIcd.getIcdcLocEn()) != castLoc(pDto.getLocalisation())) {
            return false;
        }
        return true;
    }

    private LocalisationEn castLoc(LocalisationEn opscLocEn) {
        if (opscLocEn == null) {
            return LocalisationEn.E;
        }
        return opscLocEn;
    }
}

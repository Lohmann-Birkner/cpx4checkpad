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

import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * helper to compute dto list for ops in simulation
 *
 * @author wilde
 */
public class OpsDtoHelper {

    private static final Logger LOG = Logger.getLogger(OpsDtoHelper.class.getName());

    /**
     * @param versionOpsMap map for id (details id) and all opses for that
     * details
     * @return list of OpsOverviewDTO to show in simulation
     */
    public List<OpsOverviewDTO> computeDtoList(HashMap<Long, List<TCaseOps>> versionOpsMap) {
        List<OpsOverviewDTO> overview = new ArrayList<>();
        int index = 0;
        do {
            List<OpsOverviewDTO> tmp = new ArrayList<>();
            versionLoop:
            for (long version : versionOpsMap.keySet()) {
                if (versionOpsMap.get(version).size() <= index) {
                    continue;
                }
                TCaseOps tmpOps = versionOpsMap.get(version).get(index);
//                Hibernate.initialize(tmpOps.getCaseDepartment());
                for (OpsOverviewDTO dto : tmp) {
                    if (dto.getOpsCode().equals(tmpOps.getOpscCode())) {
                        dto.addOpsForVersion(String.valueOf(version), tmpOps);
                        dto.getOccurance().add(version);
                        continue versionLoop;
                    }
                }
//                versionOpsMap.get(version).add(index,new TCaseOps());
                OpsOverviewDTO newDto = new OpsOverviewDTO(tmpOps.getOpscCode(), "");
                newDto.addOpsForVersion(String.valueOf(version), tmpOps);
                newDto.getOccurance().add(version);
                tmp.add(newDto);
            }
            overview.addAll(tmp);
            index++;

        } while (!checkSize(index, versionOpsMap.values()));

        overview.sort(Comparator.comparing(OpsOverviewDTO::getOpsCode));
        return overview;
    }

    /**
     *
     * @param versionOpsMap map for id (details id) and all opses for that
     * details
     * @return list of OpsOverviewDTO to show in merging
     */
    public List<OpsOverviewDTO> computeDtoMergeList(HashMap<Long, List<TCaseOps>> versionOpsMap) {
        List<OpsOverviewDTO> overview = new ArrayList<>();
//        int index = 0;
//        do{
//            List<OpsOverviewDTO> tmp = new ArrayList<>();
//            versionLoop:for(long version : versionOpsMap.keySet()){
//                if(versionOpsMap.get(version).size()<=index){
//                    continue;
//                }
//                TCaseOps tmpOps = versionOpsMap.get(version).get(index);
////                Hibernate.initialize(tmpOps.getCaseDepartment());
//                for(OpsOverviewDTO dto : tmp){
//                    if(dto.getOpsCode().equals(tmpOps.getOpscCode())){
//                        if(dto.getOccurance().size()<=2){
//                            dto.addOpsForVersion(String.valueOf(version), tmpOps);
//                            dto.getOccurance().add(version);
//                            continue versionLoop;
//                        }
//                    }
//                }
////                versionOpsMap.get(version).add(index,new TCaseOps());
//                OpsOverviewDTO newDto = new OpsOverviewDTO(tmpOps.getOpscCode(), "");
//                newDto.addOpsForVersion(String.valueOf(version), tmpOps);
//                newDto.getOccurance().add(version);
//                tmp.add(newDto);
//            }
//            overview.addAll(tmp);
//            index++;
//            
//        }while (!checkSize(index,versionOpsMap.values()));
//        
////        overview.sort(Comparator.comparing(OpsOverviewDTO::getOpsCode));  
        List<TCaseOps> merged = versionOpsMap.get(0L);
        merged.sort(Comparator.comparing(TCaseOps::getOpscCode));
        for (TCaseOps ops : merged) {
            OpsOverviewDTO newDto = new OpsOverviewDTO(ops.getOpscCode(), "");
            newDto.addOpsForVersion(String.valueOf(0), ops);
            newDto.getOccurance().add(0L);
            overview.add(newDto);
        }
        versionOpsMap.remove(0L);
        Iterator<Long> it = versionOpsMap.keySet().iterator();
        while (it.hasNext()) {
            Long version = it.next();
            List<TCaseOps> opses = versionOpsMap.get(version);
            opses.sort(Comparator.comparing(TCaseOps::getOpscCode));
            Iterator<TCaseOps> opsIt = opses.iterator();
            while (opsIt.hasNext()) {
//            versionLoop:for(TCaseOps next : opses){
                TCaseOps next = opsIt.next();
                for (OpsOverviewDTO dto : overview) {
                    if (dto.getOpsCode().equals(next.getOpscCode())) {
                        if (dto.getOccurance().size() < 2) {
                            dto.addOpsForVersion(String.valueOf(version), next);
                            dto.getOccurance().add(version);
//                            continue versionLoop;
                            opsIt.remove();
                            break;
                        }
                    }
                }
            }
            if (!opses.isEmpty()) {
                for (TCaseOps ops : opses) {
                    OpsOverviewDTO newDto = new OpsOverviewDTO(ops.getOpscCode(), "");
                    newDto.addOpsForVersion(String.valueOf(version), ops);
                    newDto.getOccurance().add(version);
                    overview.add(newDto);
                }
            }
        }
        return overview;
    }

    /**
     * TODO: could refactor alg not to use for-loop in for-loop -&gt; costly to
     * do this kind of thing second algorithm to compute list of dtos to compare
     * versions
     *
     * @param versionOpsMap map of version(tcasedetails) to list of ops
     * @return list of dto objects to compare ops between versions
     */
    public List<OpsOverviewDTO> computeDtoList2(HashMap<Long, List<TCaseOps>> versionOpsMap) {
        long start = System.currentTimeMillis();
        List<OpsOverviewDTO> tmp = new ArrayList<>();
        versionLoop:
        for (long version : versionOpsMap.keySet()) {
            List<TCaseOps> opsList = versionOpsMap.get(version);
            opsLoop:
            for (TCaseOps tmpOps : opsList) {
                for (OpsOverviewDTO dto : tmp) {
                    if (compare(tmpOps, dto)) {
                        if (dto.getOpsForVersion(String.valueOf(version)) != null) {
                            continue;
                        }
                        dto.addOpsForVersion(String.valueOf(version), tmpOps);
                        dto.getOccurance().add(version);
                        continue opsLoop;
                    }
                }
                OpsOverviewDTO newDto = new OpsOverviewDTO(tmpOps.getOpscCode(), tmpOps.getOpscLocEn(), tmpOps.getOpscDatum(), "");
                newDto.addOpsForVersion(String.valueOf(version), tmpOps);
                newDto.getOccurance().add(version);
                tmp.add(newDto);
            }
        }
        tmp.sort(Comparator.comparing(OpsOverviewDTO::getOpsCode)
                .thenComparing(OpsOverviewDTO::getDate, Comparator.nullsFirst(Comparator.naturalOrder())));
        LOG.fine("time to compute ops list: " + (System.currentTimeMillis() - start));
        return tmp;
    }

    public List<TCaseOps> sort(List<TCaseOps> pOpses) {
        pOpses.sort(Comparator.comparing(TCaseOps::getOpscCode)
                .thenComparing(TCaseOps::getId)
                .thenComparing(TCaseOps::getOpscCode));

        return pOpses;
    }

    private boolean compare(TCaseOps pOps, OpsOverviewDTO pDto) {
        if (!pOps.getOpscCode().equals(pDto.getOpsCode())) {
//            LOG.info("code not equal!\n " 
//                +"ops: " + pOps.getOpscCode() + " " + pDto.getLocalisation() + " " + Lang.toDate(pOps.getOpscDatum())+"\n"
//                +"dto: " + pDto.getOpsCode() + " " + pDto.getLocalisation() + " " + Lang.toDate(pDto.getDate()));
            return false;
        }
        if (castLoc(pOps.getOpscLocEn()) != castLoc(pDto.getLocalisation())) {
//            LOG.info("loc not equal!\n" 
//                +"ops: " + pOps.getOpscCode() + " " + pOps.getOpscLocEn() + " " + Lang.toDate(pOps.getOpscDatum())+"\n"
//                +"dto: " + pDto.getOpsCode() + " " + pDto.getLocalisation() + " " + Lang.toDate(pDto.getDate()));
            return false;
        }
        if (!Lang.toDate(pOps.getOpscDatum()).equals(Lang.toDate(pDto.getDate()))) {
//            LOG.info("date not equal!\n " 
//                +"ops: " + pOps.getOpscCode() + " " + pDto.getLocalisation() + " " + Lang.toDate(pOps.getOpscDatum())+"\n"
//                +"dto: " + pDto.getOpsCode() + " " + pDto.getLocalisation() + " " + Lang.toDate(pDto.getDate()));
            return false;
        }
//        LOG.info("compare success!\n " 
//                +"ops: " + pOps.getOpscCode() + " " + pDto.getLocalisation() + " " + Lang.toDate(pOps.getOpscDatum())+"\n"
//                +"dto: " + pDto.getOpsCode() + " " + pDto.getLocalisation() + " " + Lang.toDate(pDto.getDate()));
        return true;
    }

    //Helper-Methode compute if an item has still items that are beyond the current index
    private boolean checkSize(int index, Collection<List<TCaseOps>> values) {
        List<Boolean> hasMore = new ArrayList<>();
        for (List<TCaseOps> val : values) {
            hasMore.add(val.size() <= index);

        }
        return hasMore.stream().allMatch((Boolean t) -> t.equals(Boolean.TRUE));
    }

    private LocalisationEn castLoc(LocalisationEn opscLocEn) {
        if (opscLocEn == null) {
            return LocalisationEn.E;
        }
        return opscLocEn;
    }
}

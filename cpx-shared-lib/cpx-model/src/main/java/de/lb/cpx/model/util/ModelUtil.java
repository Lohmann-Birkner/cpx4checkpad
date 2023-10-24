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
package de.lb.cpx.model.util;

import de.lb.cpx.model.HospitalDevisionIF;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author gerschmann
 */
public class ModelUtil {

    private ModelUtil() {
        //utility class needs no public constructor
    }

    /**
     * Die Methode legt die Liste der geklonnten Icds zu einer Icd - Liste es
     * werden auch die referenzen auf die anderen Icds angepasst und die
     * Referenz auf die Abteilung(TDepartment)/Station(TWard) zu der sie
     * zugewiesen sind
     *
     * @param caseOpses - Liste der Icds
     * @param caseIcds - Liste der Icds
     * @param clone Abteilung(TDepartment)/Station(TWard)
     * @throws CloneNotSupportedException Object cloning failed
     */
    public static final synchronized void cloneAndCheckIcdsOps4Case(Set<TCaseOps> caseOpses, Set<TCaseIcd> caseIcds,
            HospitalDevisionIF clone) throws CloneNotSupportedException {

        Set<TCaseIcd> clonedIcds = new HashSet<>(0);
        Set<TCaseOps> clonedOpses = new HashSet<>(0);
        HashMap<TCaseIcd, TCaseIcd> old2new = new HashMap<>();
        Set<TCaseIcd> icdRefs = new HashSet<>(0);
        for (TCaseIcd icd : caseIcds) {
            TCaseIcd cloneIcd = (TCaseIcd) icd.clone();

            if (clone instanceof TCaseWard) {
                cloneIcd.setCaseWard((TCaseWard) clone);
            } else if (clone instanceof TCaseDepartment) {
                cloneIcd.setCaseDepartment((TCaseDepartment) clone);
            }

            clonedIcds.add(cloneIcd);
            old2new.put(icd, cloneIcd);
            if (icd.getIcdcReftypeEn() != null) {
                icdRefs.add(icd);
            }
        }

        if (!old2new.isEmpty() && !icdRefs.isEmpty()) {
            for (TCaseIcd icd : icdRefs) {
                TCaseIcd clonedIcd = old2new.get(icd);
                if (icd.getRefIcd() != null) {
                    clonedIcd.setRefIcd(old2new.get(icd.getRefIcd()));
                }
                if (!icd.getRefIcds().isEmpty()) {
                    Set<TCaseIcd> clonedRefIcds = new HashSet<>();

                    for (TCaseIcd icd1 : icd.getRefIcds()) {
                        clonedRefIcds.add(old2new.get(icd1));
                    }

                    clonedIcd.setRefIcds(clonedRefIcds);
                }
            }
        }
        clone.setCaseIcds(clonedIcds);

        for (TCaseOps ops : caseOpses) {
            TCaseOps cloneOps = (TCaseOps) ops.clone();
            clonedOpses.add(cloneOps);

            if (clone instanceof TCaseWard) {
                cloneOps.setCaseWard((TCaseWard) clone);
            } else if (clone instanceof TCaseDepartment) {
                cloneOps.setCaseDepartment((TCaseDepartment) clone);
            }
        }
        clone.setCaseOpses(clonedOpses);

    }

    /**
     * Die Methode legt die Liste der geklonnten Icds zu einer Icd - Liste es
     * werden auch die referenzen auf die anderen Icds angepasst und die
     * Referenz auf die Abteilung(TDepartment)/Station(TWard) zu der sie
     * zugewiesen sind
     *
     * @param caseOpses - Liste der Icds
     * @param caseIcds - Liste der Icds
     * @param clone Abteilung(TDepartment)/Station(TWard)
     * @param currentCpxUserId current user
     * @throws CloneNotSupportedException clone exception
     */
    public static final synchronized void cloneWithoutIdAndCheckIcdsOps4Case(Set<TCaseOps> caseOpses, Set<TCaseIcd> caseIcds, 
            HospitalDevisionIF clone, Long currentCpxUserId) throws CloneNotSupportedException {
        HashMap<TCaseOps, TCaseOps> old2newOps = new HashMap<>();
        HashMap<TCaseIcd, TCaseIcd> old2newIcds = new HashMap<>();
        cloneWithoutIdAndCheckOps4Case(caseOpses, clone, currentCpxUserId, old2newOps);
        cloneWithoutIdAndCheckIcds4Case( caseIcds, clone, currentCpxUserId, old2newIcds);
    }

    public static final synchronized void cloneWithoutIdAndCheckOps4Case(Set<TCaseOps> caseOpses, 
            HospitalDevisionIF clone, Long currentCpxUserId,  Map<TCaseOps, TCaseOps> old2new) throws CloneNotSupportedException {


        Set<TCaseOps> clonedOpses = new HashSet<>(0);
        
        for (TCaseOps ops : caseOpses) {
            TCaseOps cloneOps = (TCaseOps) ops.cloneWithoutIds(currentCpxUserId);
            clonedOpses.add(cloneOps);

            if (clone instanceof TCaseWard) {
                cloneOps.setCaseWard((TCaseWard) clone);
            } else if (clone instanceof TCaseDepartment) {
                cloneOps.setCaseDepartment((TCaseDepartment) clone);
            }
            old2new.put(ops, cloneOps);
        }
        
        clone.setCaseOpses(clonedOpses);

    }


    public static final synchronized void cloneWithoutIdAndCheckIcds4Case( Set<TCaseIcd> caseIcds, 
            HospitalDevisionIF clone, Long currentCpxUserId, Map<TCaseIcd, TCaseIcd> old2new) throws CloneNotSupportedException {

        Set<TCaseIcd> clonedIcds = new HashSet<>(0);

        Set<TCaseIcd> icdRefs = new HashSet<>(0);
        for (TCaseIcd icd : caseIcds) {
            TCaseIcd cloneIcd = (TCaseIcd) icd.cloneWithoutIds(currentCpxUserId);
            if (clone instanceof TCaseWard) {
                cloneIcd.setCaseWard((TCaseWard) clone);
            } else if (clone instanceof TCaseDepartment) {
                cloneIcd.setCaseDepartment((TCaseDepartment) clone);
            }

            clonedIcds.add(cloneIcd);
            old2new.put(icd, cloneIcd);
            if (icd.getIcdcReftypeEn() != null) {
                icdRefs.add(icd);
            }
        }

        if (!old2new.isEmpty() && !icdRefs.isEmpty()) {
            icdRefs.forEach((icd) -> {
                TCaseIcd clonedIcd = old2new.get(icd);
                if (icd.getRefIcd() != null) {
                    clonedIcd.setRefIcd(old2new.get(icd.getRefIcd()));
                }
                if (!icd.getRefIcds().isEmpty()) {
                    Set<TCaseIcd> clonedRefIcds = new HashSet<>();

                    icd.getRefIcds().forEach((icd1) -> {
                        clonedRefIcds.add(old2new.get(icd1));
                    });

                    clonedIcd.setRefIcds(clonedRefIcds);
                }
            });
        }
        clone.setCaseIcds(clonedIcds);
        
    }

    /**
     * Vergleicht Zwei Entity - Sets. Die Entities werden mit der Methode
     * equals2object verglichen, damit nur vorgegebenen Inhalte verglichen
     * werden
     *
     * @param my erste Set
     * @param other andere Set
     * @return Ergebnis des Vergleichs
     */
//    public static boolean compareEntitySets(Set my, Set other) {
//// both null or empty        
//        if((my == null || my.isEmpty()) && (other == null || other.isEmpty()))
//            return true;
//// one is null or empty, other not       
//        if((my == null || my.isEmpty()) && (other != null && !other.isEmpty())
//            || (my != null && !my.isEmpty()) && (other == null || other.isEmpty())){
//            return false;
//        }
//// different size       
//        if(my.size() != other.size()){
//            return false;
//        }
//        
//        ArrayList otherCopy = new ArrayList<>(other);
//
//        
//        Iterator itrMy =my.iterator();
//        
//        while(itrMy.hasNext()){
//            Object found = null;
//            Object myObj = itrMy.next();
//            Iterator itrOther = otherCopy.iterator();
//            while(itrOther.hasNext() && found == null){
//                Object otherObj = itrOther.next();
//                if(myObj instanceof AbstractEntity && otherObj instanceof AbstractEntity){
//                    if(((AbstractEntity)myObj).equals2object((AbstractEntity)otherObj)){
//                        found = otherObj;
//                    }
//                }else{
//                   if(myObj.equals(otherObj)){
//                       found = otherObj;
//                   }
//                }
//            }
//            if(found != null){
//                otherCopy.remove(found);
//            }
//            
//        }
//        
//        return otherCopy.size() == 0;
//
//     }
    /**
     * Vergleicht Zwei Entity - Sets.Die Entities werden mit der Methode
     * equals2object verglichen, damit nur vorgegebenen Inhalte verglichen
     * werden
     *
     * @param set1 Set 1
     * @param set2 Set 2
     * @return Ergebnis des Vergleichs
     */
    public static boolean versionSetEquals(Set<? extends AbstractVersionEntity> set1, Set<? extends AbstractVersionEntity> set2) {

        boolean equals = true;
        if (set1 == null) {
            return set2 == null || set2.isEmpty();
        }
        if (set2 == null) {
            return set1.isEmpty();
        }

        if (set1.size() != set2.size()) {
            return false;
        } else {
            for (Object entity1 : set1) {
                boolean contains = false;
                for (Object entity2 : set2) {
                    if (((AbstractVersionEntity) entity1).versionEquals(entity2)) {
                        contains = true;
                        break;
                    }
                }
                equals = contains;
                if (!equals) {
                    break;
                }
            }
        }
        return equals;
    }

}

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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.analyser.tabs;

import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.ruleviewer.analyser.attributes.OpsDataAttributes;
import de.lb.cpx.ruleviewer.analyser.model.RuleAnalyserItem;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.scene.control.Tooltip;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class OpsDataTab extends AnalyserListAttributesTab<TCaseOps, OpsDataAttributes> {

    public static final String NEW_OPS_TEXT = "";

    public OpsDataTab() {
        super("OPS-Daten", OpsDataAttributes.instance());
        setComparator(new Comparator<TCaseOps>() {
            @Override
            public int compare(TCaseOps o1, TCaseOps o2) {
                if (o1 == null || o1.getOpscCode() == null) {
                    return 1;
                }
                if (o2 == null || o2.getOpscCode() == null) {
                    return 1;
                }
                if (NEW_OPS_TEXT.equals(o1.getOpscCode())) {
                    return -1;
                }
                if (NEW_OPS_TEXT.equals(o2.getOpscCode())) {
                    return -1;
                }
                return o1.getOpscCode().compareTo(o2.getOpscCode());
            }
        });
    }

    public List<TCaseOps> getAllOpses(@NotNull TCase pFall) {
        Objects.requireNonNull(pFall, "Case can not be null");
        List<TCaseOps> icds = new ArrayList<>();
        for (TCaseDepartment dep : pFall.getCurrentLocal().getCaseDepartments()) {
            icds.addAll(dep.getCaseOpses());
//            for (TCaseWard ward : dep.getCaseWards()) {
//                icds.addAll(ward.getCaseIcds());
//            }
        }
        icds.sort(getComparator());
        return icds;
    }

    @Override
    public List<TCaseOps> getBeanList(TCase pFall) {
        return getAllOpses(pFall);
    }

    @Override
    public final RuleAnalyserItem getRootItemForEntity(TCaseOps pItem) {
        RuleAnalyserItem root = super.getRootItemForEntity(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTooltip(getToolTip(pItem));
        root.setTitle("OPS");//pItem.getOpscCode());
        return root;
    }

    @Override
    public final MultiAttributesItem getMenuRoot(TCaseOps pItem) {
        MultiAttributesItem root = super.getMenuRoot(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTooltip(getToolTip(pItem));
        root.setTitle("OPS");//pItem.getOpscCode());
        return root;
    }

    public Tooltip getToolTip(TCaseOps pItem) {
        int year = Lang.toYear(getCase().getCurrentLocal().getCsdAdmissionDate());
        String desc = CpxOpsCatalog.instance().getDescriptionByCode(pItem.getOpscCode(), "de", year);
        if (desc == null || desc.isEmpty()) {
            desc = "Ops-Katalog: " + year + " hat kein Eintrag für den Code: " + pItem.getOpscCode();
        }
        BasicTooltip tip = new BasicTooltip(pItem.getOpscCode(), desc);
        return tip;
    }

    @Override
    public boolean removeItemFromEntity(@NotNull TCase pFall, @NotNull TCaseOps pItem) {
        Objects.requireNonNull(pFall, "case can not be null");
        Objects.requireNonNull(pItem, "icd can not be null");
        for (TCaseDepartment dep : pFall.getCurrentLocal().getCaseDepartments()) {
            if (removeIcd(dep, pItem)) {
                return true;
            }
        }
        return false;
    }

    private boolean removeIcd(@NotNull TCaseDepartment pDepartment, @NotNull TCaseOps pItem) {
        Objects.requireNonNull(pDepartment, "case can not be null");
        Objects.requireNonNull(pItem, "ops can not be null");
        for (TCaseOps ops : pDepartment.getCaseOpses()) {
            if (ops.versionEquals(pItem)) {
                pDepartment.getCaseOpses().remove(pItem);
                return true;
            }
        }
        return false;
    }

    private TCaseDepartment getLastDepartment() {
        TCaseDepartment department = getCase().getCurrentLocal().getLastDepartment();
        if (department == null) {
            department = new TCaseDepartment();
            department.setDepKey301(DepartmentDataTab.NEW_DEPARTMENT_TEXT);
            getCase().getCurrentLocal().addDepartment(department);
        }
        return department;

    }

    @Override
    public void createAndAddNewEntity() {
        TCaseOps newOps = new TCaseOps();
        newOps.setOpscCode(NEW_OPS_TEXT);
        TCaseDepartment department = getLastDepartment();
         department.getCaseOpses().add(newOps);
        if (!department.getCaseWards().isEmpty()) {
            TCaseWard ward = department.getCaseWards().iterator().next();
            ward.getCaseOpses().add(newOps);
        }
    }

    @Override
    public boolean deleteRoot(TCaseOps pItem) {
        for (TCaseDepartment department : getCase().getCurrentLocal().getCaseDepartments()) {
            if (deleteOps(department, pItem)) {
                return true;
            }
        }
        return false;
    }

    private boolean deleteOps(TCaseDepartment department, TCaseOps pItem) {
        boolean ret = removeIcd(department, pItem);
        if (ret && !department.getCaseWards().isEmpty()) {
           for (TCaseWard ward : department.getCaseWards()) {
               if (deleteOps(ward.getCaseOpses(), pItem)) {
                    return true;
                }
            }
            
        }
        return ret;
    }

    private boolean deleteOps(Set<TCaseOps> pIcds, TCaseOps pIcd) {
//        for(TCaseIcd icd : pIcds){
//            if(icd.versionEquals(pIcd)){
//                p
//            }
//        }
        Iterator<TCaseOps> it = pIcds.iterator();
        while (it.hasNext()) {
            TCaseOps next = it.next();
            if (next.versionEquals(pIcd)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}

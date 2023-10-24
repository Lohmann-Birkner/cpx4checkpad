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

import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.ruleviewer.analyser.attributes.IcdDataAttributes;
import de.lb.cpx.ruleviewer.analyser.editors.IcdRefTypeEditor;
import de.lb.cpx.ruleviewer.analyser.model.RuleAnalyserItem;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.Editors;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class IcdDataTab extends AnalyserListAttributesTab<TCaseIcd, IcdDataAttributes> {

    public static final String NEW_ICD_TEXT = "Neue Icd";

    public IcdDataTab() {
        super("ICD-Daten", IcdDataAttributes.instance());
        setEditorFactorty(new IcdEditorFactory());
        setComparator(new Comparator<TCaseIcd>() {
            @Override
            public int compare(TCaseIcd o1, TCaseIcd o2) {
                if (o1 == null || o1.getIcdcCode() == null) {
                    return 1;
                }
                if (o2 == null || o2.getIcdcCode() == null) {
                    return 1;
                }
                if (NEW_ICD_TEXT.equals(o1.getIcdcCode())) {
                    return -1;
                }
                if (NEW_ICD_TEXT.equals(o2.getIcdcCode())) {
                    return -1;
                }
                return o1.getIcdcCode().compareTo(o2.getIcdcCode());
            }
        });
    }

    public List<TCaseIcd> getAllIcds(@NotNull TCase pFall) {
        Objects.requireNonNull(pFall, "Case can not be null");
        List<TCaseIcd> icds = new ArrayList<>();
        for (TCaseDepartment dep : pFall.getCurrentLocal().getCaseDepartments()) {
            icds.addAll(dep.getCaseIcds());
//            for (TCaseWard ward : dep.getCaseWards()) {
//                icds.addAll(ward.getCaseIcds());
//            }
        }
        icds.sort(getComparator());//Comparator.comparing(TCaseIcd::getIcdcCode));
        return icds;
    }

    @Override
    public List<TCaseIcd> getBeanList(TCase pFall) {
        return getAllIcds(pFall);
    }

    @Override
    public final RuleAnalyserItem getRootItemForEntity(TCaseIcd pItem) {
        RuleAnalyserItem root = super.getRootItemForEntity(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTooltip(getToolTip(pItem));
//        root.setTitle("ICD ("+AnalyserFormater.getIcdRef(pItem)+")");
//        root.setTitle(AnalyserFormater.getIcdCodeWithRef(pItem));
        root.setTitle(getTitle(pItem));
        return root;
    }

    @Override
    public final MultiAttributesItem getMenuRoot(TCaseIcd pItem) {
        MultiAttributesItem root = super.getMenuRoot(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTooltip(getToolTip(pItem));
        root.setTitle(getTitle(pItem));
        return root;
    }

    public String getTitle(TCaseIcd pIcd) {
//        String ref = AnalyserFormater.getIcdRef(pIcd);
//        if(ref == null || ref.isEmpty()){
        return "ICD";
//        }
//        return "ICD ("+AnalyserFormater.getIcdCodeWithRef(pIcd)+")";
    }

    public BasicTooltip getToolTip(TCaseIcd pItem) {
        int year = Lang.toYear(getCase().getCurrentLocal().getCsdAdmissionDate());
        String desc = CpxIcdCatalog.instance().getDescriptionByCode(pItem.getIcdcCode(), "de", year);
        if (desc == null || desc.isEmpty()) {
            desc = "Icd-Katalog: " + year + " hat kein Eintrag für den Code: " + pItem.getIcdcCode();
        }
        BasicTooltip tip = new BasicTooltip(pItem.getIcdcCode(), desc);
        return tip;
    }

    @Override
    public boolean removeItemFromEntity(@NotNull TCase pFall, @NotNull TCaseIcd pItem) {
        Objects.requireNonNull(pFall, "case can not be null");
        Objects.requireNonNull(pItem, "icd can not be null");
        for (TCaseDepartment dep : pFall.getCurrentLocal().getCaseDepartments()) {
            if (removeIcd(dep, pItem)) {
                return true;
            }
        }
        return false;
    }

    private boolean removeIcd(TCaseDepartment pDepartment, TCaseIcd pItem) {
        Objects.requireNonNull(pDepartment, "case can not be null");
        Objects.requireNonNull(pItem, "icd can not be null");
        for (TCaseIcd icd : pDepartment.getCaseIcds()) {
            if (icd.versionEquals(pItem)) {
                pDepartment.getCaseIcds().remove(pItem);
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
        TCaseIcd newIcd = new TCaseIcd();
        newIcd.setIcdcCode(NEW_ICD_TEXT);
        TCaseDepartment department = getLastDepartment();
        department.getCaseIcds().add(newIcd);
        if (!department.getCaseWards().isEmpty()) {

       
            TCaseWard ward = department.getCaseWards().iterator().next();
            ward.getCaseIcds().add(newIcd);
        }
    }

    @Override
    public boolean deleteRoot(TCaseIcd pItem) {
        for (TCaseDepartment department : getCase().getCurrentLocal().getCaseDepartments()) {
            if (deleteIcd(department, pItem)) {
                return true;
            }
        }
        return false;
    }

    private boolean deleteIcd(TCaseDepartment department, TCaseIcd pItem) {
        boolean ret = removeIcd(department, pItem);
        if (ret && !department.getCaseWards().isEmpty()) {
           for (TCaseWard ward : department.getCaseWards()) {
                if (deleteIcd(ward.getCaseIcds(), pItem)) {
                    return true;
                }
            }
            
        }
        return ret;
    }

    private boolean deleteIcd(Set<TCaseIcd> pIcds, TCaseIcd pIcd) {
//        for(TCaseIcd icd : pIcds){
//            if(icd.versionEquals(pIcd)){
//                p
//            }
//        }
        Iterator<TCaseIcd> it = pIcds.iterator();
        while (it.hasNext()) {
            TCaseIcd next = it.next();
            if (next.versionEquals(pIcd)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    private class IcdEditorFactory extends DefaultPropertyEditorFactory {

        @Override
        public PropertyEditor<?> call(PropertySheet.Item item) {
            if (item.getType() == IcdcRefTypeEn.class) {
                Optional<PropertyEditor<?>> ed = Editors.createCustomEditor(item);
                if (ed.isPresent()) {
                    IcdRefTypeEditor editor = (IcdRefTypeEditor) ed.get();
                    editor.setIcds(getAllIcds(getCase()));
                    return editor;
                }
            }
            return super.call(item); //To change body of generated methods, choose Tools | Templates.
        }

    }
}

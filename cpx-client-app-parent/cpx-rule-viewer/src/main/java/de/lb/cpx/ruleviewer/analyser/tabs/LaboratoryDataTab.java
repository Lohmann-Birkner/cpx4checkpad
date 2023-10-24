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

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TLab;
import de.lb.cpx.ruleviewer.analyser.attributes.LabDataAttributes;
import de.lb.cpx.ruleviewer.analyser.model.RuleAnalyserItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class LaboratoryDataTab extends AnalyserListAttributesTab<TLab, LabDataAttributes> {

    public static final String NEW_LAB_TEXT = "Neuer Labortwert";

    public LaboratoryDataTab() {
        super("Labor-Daten", LabDataAttributes.instance());
        setComparator(new Comparator<TLab>() {
            @Override
            public int compare(TLab o1, TLab o2) {
                if (o1 == null || o1.getLabText() == null) {
                    return 1;
                }
                if (o2 == null || o2.getLabText() == null) {
                    return 1;
                }
                if (NEW_LAB_TEXT.equals(o1.getLabText())) {
                    return -1;
                }
                if (NEW_LAB_TEXT.equals(o2.getLabText())) {
                    return -1;
                }
                return o1.getLabText().compareTo(o2.getLabText());
            }
        });
    }

    public List<TLab> getAllLabs(@NotNull TCase pFall) {
        Objects.requireNonNull(pFall, "Case can not be null");
        ArrayList<TLab> departments = new ArrayList<>(pFall.getCaseLabor());
        departments.sort(getComparator());
        return departments;
    }

    @Override
    public List<TLab> getBeanList(TCase pFall) {
        return getAllLabs(pFall);
    }

    @Override
    public MultiAttributesItem getMenuRoot(TLab pItem) {
        MultiAttributesItem root = super.getMenuRoot(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTitle(pItem.getLabText());
        return root;
    }

    @Override
    public RuleAnalyserItem getRootItemForEntity(TLab pItem) {
        RuleAnalyserItem root = super.getRootItemForEntity(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTitle(pItem.getLabText());//"FA");
        return root;
    }

    @Override
    public boolean removeItemFromEntity(@NotNull TCase pFall, @NotNull TLab pItem) {
        Objects.requireNonNull(pFall, "case can not be null");
        Objects.requireNonNull(pItem, "lab can not be null");
        for (TLab lab : pFall.getCaseLabor()) {
            if (lab.versionEquals(pItem)) {
                return pFall.getCaseLabor().remove(pItem);
            }
        }
        return false;
    }

    @Override
    public void createAndAddNewEntity() {
        TLab newLab = new TLab();
        newLab.setLabText(NEW_LAB_TEXT);
        getCase().getCaseLabor().add(newLab);
    }

    @Override
    public boolean deleteRoot(TLab pItem) {
        return getCase().getCaseLabor().remove(pItem);
    }

//    private class AddWardButton extends AddButton{
//        public AddWardButton(@NotNull TCaseDepartment pDepartment){
//            super();
//            Objects.requireNonNull(pDepartment, "Department can not be null");
//            setTooltip(new Tooltip("Station hinzufügen"));
//            setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                TCaseWard newWard = new TCaseWard();
//                newWard.setWardcIdent("Neue Station");
//                pDepartment.getCaseWards().add(newWard);
//                reload();
//            }
//        });
//        }
//    }
//    private class DeleteWardButton extends DeleteButton{
//        public DeleteWardButton(@NotNull TCaseDepartment pDepartment,@NotNull TCaseWard pWard){
//            super();
//            Objects.requireNonNull(pDepartment, "Department can not be null");
//            Objects.requireNonNull(pWard, "Ward can not be null");
//            setTooltip(new Tooltip("Station löschen"));
//            setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                pDepartment.getCaseWards().remove(pWard);
//                reload();
//            }
//        });
//        }
//    }
}

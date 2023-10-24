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

import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttribute;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserBeanProperty;
import de.lb.cpx.ruleviewer.analyser.attributes.DepartmentDataAttributes;
import de.lb.cpx.ruleviewer.analyser.attributes.WardDataAttributes;
import de.lb.cpx.ruleviewer.analyser.editors.CaseChangedEvent;
import de.lb.cpx.ruleviewer.analyser.model.LabeledDivider;
import de.lb.cpx.ruleviewer.analyser.model.LabeledNode;
import de.lb.cpx.ruleviewer.analyser.model.RuleAnalyserItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class DepartmentDataTab extends AnalyserListAttributesTab<TCaseDepartment, DepartmentDataAttributes> {

    public static final String NEW_DEPARTMENT_TEXT = "Neue Fachabteilung";

    public DepartmentDataTab() {
        super("FAB-Daten", DepartmentDataAttributes.instance());
        setComparator(new Comparator<TCaseDepartment>() {
            @Override
            public int compare(TCaseDepartment o1, TCaseDepartment o2) {
                if (o1 == null || o1.getDepKey301() == null) {
                    return 1;
                }
                if (o2 == null || o2.getDepKey301() == null) {
                    return 1;
                }
                if (NEW_DEPARTMENT_TEXT.equals(o1.getDepKey301())) {
                    return -1;
                }
                if (NEW_DEPARTMENT_TEXT.equals(o2.getDepKey301())) {
                    return -1;
                }
                return o1.getDepKey301().compareTo(o2.getDepKey301());
            }
        });
    }

    public List<TCaseDepartment> getAllDepartments(@NotNull TCase pFall) {
        Objects.requireNonNull(pFall, "Case can not be null");
        TCaseDetails currentLocal = Objects.requireNonNull(pFall.getCurrentLocal(), "local case version can not be null");
        ArrayList<TCaseDepartment> departments = new ArrayList<>(currentLocal.getCaseDepartments());
        departments.sort(getComparator());
        return departments;
    }

    @Override
    public List<TCaseDepartment> getBeanList(TCase pFall) {
        return getAllDepartments(pFall);
    }

    @Override
    public MultiAttributesItem getMenuRoot(TCaseDepartment department) {
        MultiAttributesItem root = super.getMenuRoot(department); //To change body of generated methods, choose Tools | Templates.
        root.setTitle("FAB");//pItem.getDepKey301());//"FAB");
        root.setTooltip(getToolTip(department));
        LabeledDivider lblWard = new LabeledDivider("Stationen");
        lblWard.addToMenu(new AddWardButton(department));
        root.getChildren().add(lblWard);
        if (!department.getCaseWards().isEmpty()) {
            for (TCaseWard ward : department.getCaseWards()) {
                for (AnalyserAttribute att : WardDataAttributes.instance().getAttributes()) {
                    AnalyserBeanProperty beanProp = new AnalyserBeanProperty(ward, att);
                    LabeledNode labeled = new LabeledNode(att.getDisplayName(), beanProp);
                    labeled.addToMenu(new DeleteWardButton(department, ward));
                    root.getChildren().add(labeled);
                }
            }
        }
        return root;
    }

    @Override
    public RuleAnalyserItem getRootItemForEntity(TCaseDepartment pItem) {
        RuleAnalyserItem root = super.getRootItemForEntity(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTitle("FAB");//pItem.getDepKey301());//"FAB");
        root.setTooltip(getToolTip(pItem));
        if (!pItem.getCaseWards().isEmpty()) {
            for (TCaseWard ward : pItem.getCaseWards()) {
                for (AnalyserAttribute att : WardDataAttributes.instance().getAttributes()) {
                    AnalyserBeanProperty beanProp = new AnalyserBeanProperty(ward, att);
                    if (beanProp.getValue() == null) {
                        continue;
                    }
                    RuleAnalyserItem attItem = getRuleAnalyserItemFactory().call(att);
                    attItem.setBeanProperty(beanProp);
                    attItem.setParentItem(root);
                    attItem.getEditor().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
                        @Override
                        public void handle(CaseChangedEvent t) {
                            Event.fireEvent(getContent(), t);
                        }
                    });
                    root.getItems().add(attItem);
                }
            }
        }
        return root;
    }

    public BasicTooltip getToolTip(TCaseDepartment pItem) {
        String desc = CpxDepartmentCatalog.instance().getDepartmentNameWithDesc(pItem.getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);
        if (desc == null || desc.isEmpty()) {
            desc = "Fab-Katalog hat kein Eintrag für den Code: " + pItem.getDepKey301();
        }
        BasicTooltip tip = new BasicTooltip(pItem.getDepKey301(), desc);
        return tip;
    }

    @Override
    public boolean removeItemFromEntity(@NotNull TCase pFall, @NotNull TCaseDepartment pItem) {
        Objects.requireNonNull(pFall, "case can not be null");
        Objects.requireNonNull(pItem, "icd can not be null");
        for (TCaseDepartment department : pFall.getCurrentLocal().getCaseDepartments()) {
            if (department.versionEquals(pItem)) {
                return pFall.getCurrentLocal().getCaseDepartments().remove(pItem);
            }
        }
        return false;
    }

    @Override
    public void createAndAddNewEntity() {
        TCaseDepartment newDepartment = new TCaseDepartment();
        newDepartment.setDepKey301(NEW_DEPARTMENT_TEXT);
        getCase().getCurrentLocal().addDepartment(newDepartment);
    }

    @Override
    public boolean deleteRoot(TCaseDepartment pItem) {
        return getCase().getCurrentLocal().getCaseDepartments().remove(pItem);
    }

    private class AddWardButton extends AddButton {

        public AddWardButton(@NotNull TCaseDepartment pDepartment) {
            super();
            Objects.requireNonNull(pDepartment, "Department can not be null");
            setTooltip(new Tooltip("Station hinzufügen"));
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    TCaseWard newWard = new TCaseWard();
                    newWard.setWardcIdent("Neue Station");
                    pDepartment.getCaseWards().add(newWard);
                    reload();
                }
            });
        }
    }

    private class DeleteWardButton extends DeleteButton {

        public DeleteWardButton(@NotNull TCaseDepartment pDepartment, @NotNull TCaseWard pWard) {
            super();
            Objects.requireNonNull(pDepartment, "Department can not be null");
            Objects.requireNonNull(pWard, "Ward can not be null");
            setTooltip(new Tooltip("Station löschen"));
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pDepartment.getCaseWards().remove(pWard);
                    reload();
                }
            });
        }
    }

}

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
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.ruleviewer.analyser.attributes.FeeDataAttributes;
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
public class FeeDataTab extends AnalyserListAttributesTab<TCaseFee, FeeDataAttributes> {

    public static final String NEW_FEE_TEXT = "Neues Entgelt";

    public FeeDataTab() {
        super("Entgelt-Daten", FeeDataAttributes.instance());
        setComparator(new Comparator<TCaseFee>() {
            @Override
            public int compare(TCaseFee o1, TCaseFee o2) {
                if (o1 == null || o1.getFeecFeekey() == null) {
                    return 1;
                }
                if (o2 == null || o2.getFeecFeekey() == null) {
                    return 1;
                }
                if (NEW_FEE_TEXT.equals(o1.getFeecFeekey())) {
                    return -1;
                }
                if (NEW_FEE_TEXT.equals(o2.getFeecFeekey())) {
                    return -1;
                }
                return o1.getFeecFeekey().compareTo(o2.getFeecFeekey());
            }
        });
    }

    public List<TCaseFee> getAllFees(@NotNull TCase pFall) {
        Objects.requireNonNull(pFall, "Case can not be null");
        List<TCaseFee> fees = new ArrayList<>();
        fees.addAll(pFall.getCurrentLocal().getCaseFees());
        fees.sort(getComparator());//Comparator.comparing(TCaseIcd::getIcdcCode));
        return fees;
    }

    @Override
    public List<TCaseFee> getBeanList(TCase pFall) {
        return getAllFees(pFall);
    }

    @Override
    public final RuleAnalyserItem getRootItemForEntity(TCaseFee pItem) {
        RuleAnalyserItem root = super.getRootItemForEntity(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTitle(getTitle(pItem));
        return root;
    }

    @Override
    public final MultiAttributesItem getMenuRoot(TCaseFee pItem) {
        MultiAttributesItem root = super.getMenuRoot(pItem); //To change body of generated methods, choose Tools | Templates.
        root.setTitle(getTitle(pItem));
        return root;
    }

    public String getTitle(TCaseFee pIcd) {
        return "Entgelt";
    }

    @Override
    public boolean removeItemFromEntity(@NotNull TCase pFall, @NotNull TCaseFee pItem) {
        Objects.requireNonNull(pFall, "case can not be null");
        Objects.requireNonNull(pItem, "icd can not be null");
        return pFall.getCurrentLocal().getCaseFees().remove(pItem);
    }
    @Override
    public void createAndAddNewEntity() {
        TCaseFee newIcd = new TCaseFee();
        newIcd.setFeecFeekey(NEW_FEE_TEXT);
        getCase().getCurrentLocal().getCaseFees().add(newIcd);
    }

    @Override
    public boolean deleteRoot(TCaseFee pItem) {
        return getCase().getCurrentLocal().getCaseFees().remove(pItem);
    }
}

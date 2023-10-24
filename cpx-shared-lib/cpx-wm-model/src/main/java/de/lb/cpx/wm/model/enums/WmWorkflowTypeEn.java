/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model.enums;

import de.lb.cpx.model.enums.AbstractCpxEnumMap;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author husser
 */
public enum WmWorkflowTypeEn implements CpxEnumInterface<Integer> {

    statKH(1, Lang.WORKFLOW_TYPE_STATIONARY),
    keinFall(2, Lang.WORKFLOW_TYPE_NO_CASE);

    private static final Logger LOG = Logger.getLogger(WmWorkflowTypeEn.class.getName());

    private final int id;
    private final String langKey;

    private WmWorkflowTypeEn(int id, String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        return id;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    /*
    @Override
    public String toString(final CpxLanguageInterface cpxLanguage) {
        return this.getViewId() + " - " + cpxLanguage.get(langKey);
    }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        //return String.valueOf(id);
        return name();
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(WorkflowTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static WmWorkflowTypeEn findById(final Integer pId) {
        return WorkflowTypeEnMap.getInstance().get(pId);
    }

    public static WmWorkflowTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find WmWorkflowTypeEn, because this is not a valid integer: " + id, ex);
            return null;
        }
    }

    public static WmWorkflowTypeEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (WmWorkflowTypeEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class WorkflowTypeEnMap extends AbstractCpxEnumMap<WmWorkflowTypeEn, Integer> {

    private static final WorkflowTypeEnMap INSTANCE;

    static {
        INSTANCE = new WorkflowTypeEnMap();
    }

    protected WorkflowTypeEnMap() {
        super(WmWorkflowTypeEn.class);
    }

    public static WorkflowTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public WmWorkflowTypeEn[] getValues() {
        return WmWorkflowTypeEn.values();
    }

}

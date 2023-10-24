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
public enum WmWorkflowStateEn implements CpxEnumInterface<Integer> {

    offen(1, Lang.WORKFLOW_STATE_OPEN),
    //CPX-993
    //    erledigt(2, Lang.WORKFLOW_STATE_DONE),
    geschlossen(2, Lang.WORKFLOW_STATE_CLOSED), //    archiviert(4, Lang.WORKFLOW_STATE_ARCHIVED)
    paused(5,Lang.WORKFLOW_STATE_PAUSED);

    private static final Logger LOG = Logger.getLogger(WmWorkflowStateEn.class.getName());

    private final int id;
    private final String langKey;

    private WmWorkflowStateEn(int id, String langKey) {
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

    public static WmWorkflowStateEn fromInteger(int x) {
        switch (x) {
            case 1:
                return offen;
//            case 2:
//                return erledigt;
            case 2:
                return geschlossen;
//            case 4:
//                return archiviert;
            case 5:
                return paused;
            default:
                LOG.log(Level.WARNING, "Unknown workflow state value: " + x);
        }
        return null;
    }

    /*
    @Override
    public String toString(final CpxLanguageInterface cpxLanguage) {
        return this.getViewId() + " - " + cpxLanguage.get(langKey);
    }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString("", getLangKey());
    }

    @Override
    public String getViewId() {
        //CPX-645 Darstellung der Verte im Filtermenü ist doppelt
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
//            Logger.getLogger(WorkflowStateEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static WmWorkflowStateEn findById(final Integer pId) {
        return WorkflowStateEnMap.getInstance().get(pId);
    }

    public static WmWorkflowStateEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find WmWorkflowStateEn, because this is not a valid integer: " + id, ex);
            return null;
        }
    }

    public static WmWorkflowStateEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (WmWorkflowStateEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class WorkflowStateEnMap extends AbstractCpxEnumMap<WmWorkflowStateEn, Integer> {

    private static final WorkflowStateEnMap INSTANCE;

    static {
        INSTANCE = new WorkflowStateEnMap();
    }

    protected WorkflowStateEnMap() {
        super(WmWorkflowStateEn.class);
    }

    public static WorkflowStateEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public WmWorkflowStateEn[] getValues() {
        return WmWorkflowStateEn.values();
    }

}

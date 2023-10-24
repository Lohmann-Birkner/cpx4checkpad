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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.cache;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.enums.DeadlineTypeEn;
import de.lb.cpx.server.commonDB.model.CDeadline;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class MenuCacheDeadlines extends MenuCacheEntryMenuCacheEntity<CDeadline> {

    private static final Logger LOG = Logger.getLogger(MenuCacheDeadlines.class.getName());

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, CDeadline> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        //setMap(processServiceBean.get().getActionSubjects());
//        Map<Long, CDeadline> m = processServiceBean.get().getDeadlines();
//        setMap(m);
        return bean.get().getDeadlines();

//        deadlineTypes = new HashMap<>();
//        for (Iterator<Map.Entry<Long, CDeadline>> it = m.entrySet().iterator(); it.hasNext();) {
//            Map.Entry<Long, CDeadline> entry = it.next();
//            if (entry != null /* && dl.getDlType() != null */) {
//                DeadlineTypeEn type = entry.getValue().getDlType();
//                List<CDeadline> deadlines = deadlineTypes.get(type);
//                if (deadlines == null) {
//                    deadlines = new ArrayList<>();
//                    deadlineTypes.put(type, deadlines);
//                }
////                deadlines.add(entry.getValue());
//            }
//        }
    }

//    @Override
//    public boolean uninitialize() {
//        if (super.uninitialize()) {
//            deadlineTypes = null;
//            return true;
//        }
//        return false;
//    }
    /**
     * map of deadlines separated by type
     *
     * @param pDate check valid from/to
     * @param pType check deadline type
     * @return deadlines
     */
    private Map<DeadlineTypeEn, List<CDeadline>> getDeadlineTypes(final Date pDate, final DeadlineTypeEn pType) {
        final Map<DeadlineTypeEn, List<CDeadline>> result = new HashMap<>();
        Map<Long, CDeadline> m = map(pDate);
        if (m == null) {
            return result;
        }
        for (Iterator<Map.Entry<Long, CDeadline>> it = m.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Long, CDeadline> entry = it.next();
            if (entry != null && entry.getValue() != null /* && dl.getDlType() != null */) {
                CDeadline deadline = entry.getValue();
                if (pType != null && (deadline.getDlType() == null || !pType.equals(deadline.getDlType()))) {
                    continue;
                }
                if (pDate != null && !deadline.isValid(pDate)) {
                    continue;
                }
//                if (pDate != null) {
//                    if (deadline.getDlValidFrom() != null && deadline.getDlValidFrom().after(pDate)) {
//                        continue;
//                    }
//                    if (deadline.getDlValidTo() != null && deadline.getDlValidTo().before(pDate)) {
//                        continue;
//                    }
//                }
                DeadlineTypeEn type = deadline.getDlType();
                List<CDeadline> deadlines = result.get(type);
                if (deadlines == null) {
                    deadlines = new ArrayList<>();
                    result.put(type, deadlines);
                }
                deadlines.add(deadline);
            }
        }
        return result;
//        return new HashMap<>(deadlineTypes);
    }

    /**
     * map of deadlines separated by type
     *
     * @return deadlines
     */
    public Map<DeadlineTypeEn, List<CDeadline>> getDeadlineTypes() {
        return getDeadlineTypes(null, null);
//        return new HashMap<>(deadlineTypes);
    }

    /**
     * map of deadlines separated by type
     *
     * @param pDate check valid from/to
     * @return deadlines
     */
    public Map<DeadlineTypeEn, List<CDeadline>> getDeadlineTypes(final Date pDate) {
        return getDeadlineTypes(pDate, null);
//        return new HashMap<>(deadlineTypes);
    }

    /**
     * get deadlines with specific type
     *
     * @param dlTypeEn deadline type
     * @return list of deadlines
     */
    public List<CDeadline> getDeadlines(final DeadlineTypeEn dlTypeEn) {
        List<CDeadline> deadlines = getDeadlineTypes(null, dlTypeEn).get(dlTypeEn);
        if (deadlines == null) {
            LOG.log(Level.INFO, "no deadlines found for deadline type {0}", (dlTypeEn == null ? null : dlTypeEn.name()));
            return new ArrayList<>();
        }
        return new ArrayList<>(deadlines);
    }

    /**
     * get deadlines with specific type and date
     *
     * @param dlTypeEn deadline type
     * @param pDate check valid from/to (deadline date)
     * @return list of deadlines
     */
    public List<CDeadline> getDeadlines(final Date pDate, final DeadlineTypeEn dlTypeEn) {
        List<CDeadline> deadlines = getDeadlineTypes(pDate, dlTypeEn).get(dlTypeEn);
        if (deadlines == null) {
            LOG.log(Level.INFO, "no deadlines found for deadline type {0} and date {1}", new Object[]{(dlTypeEn == null ? null : dlTypeEn.name()), pDate});
            return new ArrayList<>();
        }
        return new ArrayList<>(deadlines);
//        final List<CDeadline> list = new ArrayList<>();
//        for (CDeadline dl : getDeadlines(dlTypeEn)) {
//            if (dl != null && dl.isDlValid(pDate)) {
//                list.add(dl);
//            }
//        }
//        return list;
    }

    /**
     * get deadline for specific type
     *
     * @param dlTypeEn deadline type
     * @param pDate check valid from/to (deadline date)
     * @return returns first deadline if multiple deadlines were found
     */
    public CDeadline getDeadline(final Date pDate, final DeadlineTypeEn dlTypeEn) {
        final List<CDeadline> list = getDeadlines(pDate, dlTypeEn);
        if (list.isEmpty()) {
            LOG.log(Level.FINEST, "No deadline found for type {0} and date {1}", new Object[]{(dlTypeEn == null ? null : dlTypeEn.name()), pDate});
            return null;
        }
        if (list.size() > 1) {
            LOG.log(Level.WARNING, "Multiple deadlines found for type {0} and date {1} -> pick first one", new Object[]{(dlTypeEn == null ? null : dlTypeEn.name()), pDate});
        }
        return list.iterator().next();
    }

    /**
     * get deadline for specific type and date
     *
     * @param dlTypeEn deadline type
     * @return returns first deadline if multiple deadlines were found
     */
    public CDeadline getDeadline(final DeadlineTypeEn dlTypeEn) {
        final List<CDeadline> list = getDeadlines(dlTypeEn);
        if (list.isEmpty()) {
            LOG.log(Level.WARNING, "No deadline found for type {0}", (dlTypeEn == null ? null : dlTypeEn.name()));
            return null;
        }
        if (list.size() > 1) {
            LOG.log(Level.WARNING, "Multiple deadlines found for type {0} -> pick first one", (dlTypeEn == null ? null : dlTypeEn.name()));
        }
        return list.iterator().next();
    }

    /**
     * AuditCompletionDeadline (DL1)
     *
     * @param pDate deadline date
     * @return AuditCompletionDeadline (DL1)
     */
    public CDeadline getAcd(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getAcd());
    }

    /**
     * CaseDialogBillCorrectionDeadline (DL10)
     *
     * @param pDate deadline date
     * @return CaseDialogBillCorrectionDeadline (DL10)
     */
    public CDeadline getCbcd(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getCbcd());
    }

    /**
     * BillCorrectionDeadline (DL2)
     *
     * @param pDate deadline date
     * @return BillCorrectionDeadline (DL2)
     */
    public CDeadline getBcd(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getBcd());
    }

    /**
     * DocumentDeliverDeadline (DL3)
     *
     * @param pDate deadline date
     * @return DocumentDeliverDeadline (DL3)
     */
    public CDeadline getDdd(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getDdd());
    }

    /**
     * PreliminaryProceedingsClosedDeadline (DL4)
     *
     * @param pDate deadline date
     * @return PreliminaryProceedingsClosedDeadline (DL4)
     */
    public CDeadline getPpcd(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getPpcd());
    }

    /**
     * ContinuationFeeDeadline (DL5)
     *
     * @param pDate deadline date
     * @return ContinuationFeeDeadline (DL5)
     */
    public CDeadline getCfd(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getCfd());
    }

    /**
     * DataRecordCorrectionDeadline (DL6)
     *
     * @param pDate deadline date
     * @return DataRecordCorrectionDeadline (DL6)
     */
    public CDeadline getDrcd(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getDrcd());
    }

    /**
     * PreliminaryProceedingsAnswerDeadline (DL7)
     *
     * @param pDate deadline date
     * @return PreliminaryProceedingsAnswerDeadline (DL7)
     */
    public CDeadline getPpad(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getPpad());
    }

    /**
     * ProposalSubsequentProceedingsDeadline (DL8)
     *
     * @param pDate deadline date
     * @return ProposalSubsequentProceedingsDeadline (DL8)
     */
    public CDeadline getPspd(final Date pDate) {
        return getDeadline(pDate, DeadlineTypeEn.getPspd());
    }

    /**
     * AuditCompletionDeadline (DL1)
     *
     * @return AuditCompletionDeadline (DL1)
     */
    public CDeadline getAcd() {
        return getDeadline(DeadlineTypeEn.getAcd());
    }

    /**
     * 6WeeksDeadline (DL9)
     *
     * @return 6WeeksDeadline (DL9)
     */
    public CDeadline get6Wd() {
        return getDeadline(DeadlineTypeEn.get6Wd());
    }

    /**
     * CaseDialogBillCorrectionDeadline (DL10)
     *
     * @return CaseDialogBillCorrectionDeadline (DL10)
     */
    public CDeadline getCbcd() {
        return getDeadline(DeadlineTypeEn.getCbcd());
    }

    /**
     * BillCorrectionDeadline (DL2)
     *
     * @return BillCorrectionDeadline (DL2)
     */
    public CDeadline getBcd() {
        return getDeadline(DeadlineTypeEn.getBcd());
    }

    /**
     * DocumentDeliverDeadline (DL3)
     *
     * @return DocumentDeliverDeadline (DL3)
     */
    public CDeadline getDdd() {
        return getDeadline(DeadlineTypeEn.getDdd());
    }

    /**
     * PreliminaryProceedingsClosedDeadline (DL4)
     *
     * @return PreliminaryProceedingsClosedDeadline (DL4)
     */
    public CDeadline getPpcd() {
        return getDeadline(DeadlineTypeEn.getPpcd());
    }

    /**
     * ContinuationFeeDeadline (DL5)
     *
     * @return ContinuationFeeDeadline (DL5)
     */
    public CDeadline getCfd() {
        return getDeadline(DeadlineTypeEn.getCfd());
    }

    /**
     * DataRecordCorrectionDeadline (DL6)
     *
     * @return DataRecordCorrectionDeadline (DL6)
     */
    public CDeadline getDrcd() {
        return getDeadline(DeadlineTypeEn.getDrcd());
    }

    /**
     * PreliminaryProceedingsAnswerDeadline (DL7)
     *
     * @return PreliminaryProceedingsAnswerDeadline (DL7)
     */
    public CDeadline getPpad() {
        return getDeadline(DeadlineTypeEn.getPpad());
    }

    /**
     * ProposalSubsequentProceedingsDeadline (DL8)
     *
     * @return ProposalSubsequentProceedingsDeadline (DL8)
     */
    public CDeadline getPspd() {
        return getDeadline(DeadlineTypeEn.getPspd());
    }

//    /**
//     * calculcates external deadline
//     * @param dlTypeEn deadline type
//     * @param pDate deadline date
//     * @return calculated external deadline
//     */
//    public Date calculcateDeadline(final DeadlineTypeEn dlTypeEn, final Date pDate) {
//        final CDeadline dl = getDeadline(dlTypeEn);
//        if (dl == null) {
//            return null;
//        } else {
//            final Date deadlineDt = dl.calculateDeadline(pDate);
//            return deadlineDt;
//        }
//    }
//
//    /**
//     * calculcates internal deadline
//     * @param dlTypeEn deadline type
//     * @param pDate deadline date
//     * @return calculated internal deadline
//     */
//    public Date calculcateDeadlineIntern(final DeadlineTypeEn dlTypeEn, final Date pDate) {
//        final CDeadline dl = getDeadline(dlTypeEn);
//        if (dl == null) {
//            return null;
//        } else {
//            final Date deadlineDt = dl.calculateDeadlineIntern(pDate);
//            return deadlineDt;
//        }
//    }
//
//    /**
//     * gives the (internal) reminder type
//     * @param dlTypeEn deadline type
//     * @param pDate date
//     * @return reminder type for deadline type and date
//     */
//    public CWmListReminderSubject getReminderTypeIntern(final DeadlineTypeEn dlTypeEn, final Date pDate) {
//        final CDeadline dl = getDeadline(dlTypeEn, pDate);
//        if (dl == null) {
//            return null;
//        } else {
//            return dl.getDlReminderType();
//        }
//    }
//
//    /**
//     * gives the (internal) reminder type
//     * @param dlTypeEn deadline type
//     * @return reminder type for deadline type and date
//     */
//    public CWmListReminderSubject getReminderTypeIntern(final DeadlineTypeEn dlTypeEn) {
//        final CDeadline dl = getDeadline(dlTypeEn);
//        if (dl == null) {
//            return null;
//        } else {
//            return dl.getDlReminderType();
//        }
//    }
    @Override
    public MenuCacheDeadlines getCopy() {
        return (MenuCacheDeadlines) super.getCopy(new MenuCacheDeadlines());
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.DEADLINES;
    }

    @Override
    public String getName(Long pKey) {
        CDeadline obj = get(pKey);
        return obj == null ? null : obj.getDlType().name();
    }

}

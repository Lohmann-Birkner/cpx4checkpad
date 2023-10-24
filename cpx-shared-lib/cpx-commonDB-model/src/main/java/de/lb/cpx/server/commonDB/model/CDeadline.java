/**
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
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.model.enums.DeadlineTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import static java.util.Calendar.getInstance;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Check;

/**
 * CBASERATE initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_DEADLINE: Fristentabelle</p>
 *
 */
@Entity
@Table(name = "C_DEADLINE" /*, uniqueConstraints = @UniqueConstraint(columnNames = {"DL_NUMBER"})*/)
@Check(constraints = "DL_TIME_QUANTITY > 0")
@SuppressWarnings("serial")
public class CDeadline extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(CDeadline.class.getName());

    //private long id;
    //private String dlName;
    //private Integer dlNumber;
    private Date dlValidFrom;
    private Date dlValidTo;
    private DeadlineTypeEn dlType;
    private int dlTimeQuantity;
    private ChronoUnit dlTimeUnit;
    private String dlComment;
    private Integer dlTimeQuantityIntern;
    private ChronoUnit dlTimeUnitIntern;
    private CWmListReminderSubject dlReminderType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_DEADLINE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "DL_TYPE", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    public DeadlineTypeEn getDlType() {
        return this.dlType;
    }

    public void setDlType(final DeadlineTypeEn dlType) {
        this.dlType = dlType;
        if (dlType != null) {
            //Use default values from enumeration if nothing was set yet
            if (getDlTimeQuantity() == 0) {
                setDlTimeQuantity(dlType.getTimeQuantity());
            }
            if (getDlTimeUnit() == null) {
                setDlTimeUnit(dlType.getTimeUnit());
            }
            if (getDlComment() == null) {
                setDlComment(dlType.getCommentTranslation().getValue());
            }
            if (getDlTimeQuantityIntern() == null) {
                setDlTimeQuantityIntern(dlType.getTimeQuantityIntern());
            }
            if (getDlTimeUnitIntern() == null) {
                setDlTimeUnitIntern(dlType.getTimeUnitIntern());
            }
        }
    }

    /**
     * @return dlName: Name der Frist
     */
//  @Column(name="DL_NAME", nullable=false, length=50)
//  public String getDlName() {
//    return dlName;
//  }
//
//  /**
//   * @param dlName the deadName  to set 
//   */
//  public void setDlName(String dlName) {
//    this.dlName = dlName;
//  }
//
//  /**
//   * @return  dlNumber: Nummer der Frist
//   */
//  @Column(name="DL_NUMBER", nullable=false, length=50)
//  public Integer getDlNumber() {
//    return dlNumber;
//  }
//
//  /**
//   * @param dlNumber the dlNumber to set
//   */
//  public void setDlNumber(Integer dlNumber) {
//    this.dlNumber = dlNumber;
//  }
    /**
     * @return dlValidFrom: Anfang der Gültigkeit
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DL_VALID_FROM", nullable = false, length = 11)
    public Date getDlValidFrom() {
        return dlValidFrom == null ? null : new Date(dlValidFrom.getTime());
    }

    /**
     * @param dlValidFrom the dlValidFrom to set
     */
    public void setDlValidFrom(Date dlValidFrom) {
        this.dlValidFrom = dlValidFrom == null ? null : new Date(dlValidFrom.getTime());
    }

    /**
     * @return dlValidTo: Ende der Gültigkeit
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DL_VALID_TO", nullable = false, length = 11)
    public Date getDlValidTo() {
        return dlValidTo == null ? null : new Date(dlValidTo.getTime());
    }

    /**
     * @param dlValidTo the dlValidTo to set
     */
    public void setDlValidTo(Date dlValidTo) {
        this.dlValidTo = dlValidTo == null ? null : new Date(dlValidTo.getTime());
    }

    /**
     * @return dlTimeQuantity: Fristdauer , muss ein positiver Wert eingegeben
     * werden .
     */
    @Column(name = "DL_TIME_QUANTITY", nullable = false)
    public int getDlTimeQuantity() {
        return dlTimeQuantity;
    }

    /**
     * @param dlTimeQuantity the dlTimeQuantity to set
     */
    public void setDlTimeQuantity(int dlTimeQuantity) {
        this.dlTimeQuantity = dlTimeQuantity;
    }

    /**
     * @return dlTimeUnit: Zeiteinheiten
     */
    @Column(name = "DL_TIME_UNIT", nullable = false)
    @Enumerated(EnumType.STRING)
    public ChronoUnit getDlTimeUnit() {
        return dlTimeUnit;
    }

    /**
     * @param dlTimeUnit the deadUnit to set
     */
    public void setDlTimeUnit(ChronoUnit dlTimeUnit) {
        this.dlTimeUnit = dlTimeUnit;
    }

    /**
     * @return dlTimeQuantityIntern: Anzahl Zeiteinheiten - für Kunden intern
     * verwendete Frist werden .
     */
    @Column(name = "DL_TIME_QUANTITY_INTERN", nullable = true)
    public Integer getDlTimeQuantityIntern() {
        return dlTimeQuantityIntern;
    }

    /**
     * @param dlTimeQuantityIntern the dlTimeQuantityIntern to set
     */
    public void setDlTimeQuantityIntern(Integer dlTimeQuantityIntern) {
        this.dlTimeQuantityIntern = dlTimeQuantityIntern;
    }

    /**
     * @return deadUnitIntern: Zeiteinheit - für Kunden intern verwendete Frist
     */
    @Column(name = "DL_TIME_UNIT_INTERN", nullable = true)
    @Enumerated(EnumType.STRING)
    public ChronoUnit getDlTimeUnitIntern() {
        return dlTimeUnitIntern;
    }

    /**
     * @param dlTimeUnitIntern the deadUnit to set
     */
    public void setDlTimeUnitIntern(ChronoUnit dlTimeUnitIntern) {
        this.dlTimeUnitIntern = dlTimeUnitIntern;
    }

    /**
     * @return dlReminderType: Zugeordnete Wiedervorlage zu dieser Frist
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DL_REMINDER_TYPE", nullable = true)
    public CWmListReminderSubject getDlReminderType() {
        return dlReminderType;
    }

    /**
     * @param dlReminderType the dlReminderType to set
     */
    public void setDlReminderType(CWmListReminderSubject dlReminderType) {
        this.dlReminderType = dlReminderType;
    }

    /**
     * @return dlComment: Kommentar (max 1000 Zeichen zur kurzen Erläuterung der
     * Frist, kann vom Nutzer geändert werden)
     */
    @Column(name = "DL_COMMENT", length = 1000)
    public String getDlComment() {
        return dlComment;
    }

    /**
     * @param dlComment the dlComment to set
     */
    public void setDlComment(String dlComment) {
        this.dlComment = dlComment;
    }

//    /**
//     * is this deadline valid/active for the given date?
//     *
//     * @param pDate date
//     * @return is valid deadline for date?
//     */
//    public boolean isDlValid(final Date pDate) {
//        if (pDate == null) {
//            LOG.log(Level.WARNING, "date is null, cannot check if deadline is valid for this date!");
//            return false;
//        }
//        if (dlValidFrom == null && dlValidTo == null) {
//            LOG.log(Level.WARNING, "deadline with " + id + " has no valid from and no valid to, so it is always valid!");
//            return true;
//        }
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(pDate);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        final Date dt = cal.getTime();
//        return ((dlValidFrom == null || dlValidFrom.before(dt) || dlValidFrom.equals(dt))
//                && (dlValidTo == null || dlValidTo.after(dt) || dlValidTo.equals(dt)));
//    }
    /**
     * calculcates deadline
     *
     * @param pDate start date
     * @param pUnit time unit
     * @param pQuantity time quantity
     * @return calculated deadline
     */
    public static Date calculate(final Date pDate, final ChronoUnit pUnit, final Integer pQuantity) {
        if (pDate == null) {
            LOG.log(Level.FINEST, "date is null, cannot calculcate deadline!");
            return null;
        }
        if (pUnit == null) {
            LOG.log(Level.FINEST, "time unit is null, cannot calculcate deadline!");
            return null;
        }
        if (pQuantity == null) {
            LOG.log(Level.FINEST, "quantity is null, cannot calculcate deadline!");
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        cal.add(Calendar.HOUR_OF_DAY, getInstance().get(Calendar.HOUR_OF_DAY));
        cal.add(Calendar.MINUTE, getInstance().get(Calendar.MINUTE));
        cal.add(Calendar.SECOND, getInstance().get(Calendar.SECOND));
        cal.add(Calendar.MILLISECOND, getInstance().get(Calendar.MILLISECOND));
        switch (pUnit) {
            case YEARS:
                cal.add(Calendar.YEAR, pQuantity);
                break;
            case MONTHS:
                cal.add(Calendar.MONTH, pQuantity);
                break;
            case WEEKS:
                cal.add(Calendar.WEEK_OF_YEAR, pQuantity);
                break;
            case DAYS:
                cal.add(Calendar.DAY_OF_YEAR, pQuantity);
                break;
            default:
                LOG.log(Level.WARNING, "unknown time unit found, cannot calculate dead line: {0}", pUnit);
                break;
        }
        final Date newDate = cal.getTime();
        return newDate;
    }

    /**
     * calculcates external deadline
     *
     * @param pDate start date
     * @return calculcated external deadline
     */
    public Date calculateDeadlineExtern(final Date pDate) {
        final Date newDate = calculate(pDate, dlTimeUnit, dlTimeQuantity);
        LOG.log(Level.INFO, "calculated external deadline for type {0} (id {1}) and date {2}: {3}", new Object[]{dlType == null ? null : dlType.name(), id, String.valueOf(pDate), String.valueOf(newDate)});
        return newDate;
    }

    /**
     * calculcates internal deadline
     *
     * @param pDate start date
     * @return calculcated internal deadline
     */
    public Date calculateDeadlineIntern(final Date pDate) {
        final Date newDate = calculate(pDate, dlTimeUnitIntern, dlTimeQuantityIntern);
        LOG.log(Level.INFO, "calculated internal deadline for type {0} (id {1}) and date {2}: {3}", new Object[]{dlType == null ? null : dlType.name(), id, String.valueOf(pDate), String.valueOf(newDate)});
        return newDate;
    }

    /**
     * calculates deadline. Calculates internal deadline first. If there's no
     * internal deadline it will calculcate external deadline.
     *
     * @param pDate start date
     * @return internal deadline (fallback: external deadline)
     */
    public Date calculateDeadline(final Date pDate) {
        final Date deadlineIntern = calculate(pDate, dlTimeUnitIntern, dlTimeQuantityIntern);
        final Date deadline;
        if (deadlineIntern != null) {
            deadline = deadlineIntern;
        } else {
            final Date deadlineExtern = calculate(pDate, dlTimeUnit, dlTimeQuantity);
            deadline = deadlineExtern;
        }
        LOG.log(Level.INFO, "calculated {0} deadline for type {1} (id {2}) and date {3}: {4}", new Object[]{deadlineIntern == null ? "fallback " : "", dlType == null ? null : dlType.name(), id, String.valueOf(pDate), String.valueOf(deadline)});
        return deadline;
    }

    /**
     * AuditCompletionDeadline (DL1)
     *
     * @return AuditCompletionDeadline (DL1)
     */
    @Transient
    public boolean isAcd() {
        return dlType != null && dlType.isAcd();
    }

    /**
     * BillCorrectionDeadline (DL2)
     *
     * @return BillCorrectionDeadline (DL2)
     */
    @Transient
    public boolean isBcd() {
        return dlType != null && dlType.isBcd();
    }

    /**
     * DocumentDeliverDeadline (DL3)
     *
     * @return DocumentDeliverDeadline (DL3)
     */
    @Transient
    public boolean isDdd() {
        return dlType != null && dlType.isDdd();
    }

    /**
     * PreliminaryProceedingsClosedDeadline (DL4)
     *
     * @return PreliminaryProceedingsClosedDeadline (DL4)
     */
    @Transient
    public boolean isPpcd() {
        return dlType != null && dlType.isPpcd();
    }

    /**
     * ContinuationFeeDeadline (DL5)
     *
     * @return ContinuationFeeDeadline (DL5)
     */
    @Transient
    public boolean isCdf() {
        return dlType != null && dlType.isCfd();
    }

    /**
     * DataRecordCorrectionDeadline (DL6)
     *
     * @return DataRecordCorrectionDeadline (DL6)
     */
    @Transient
    public boolean isDrd() {
        return dlType != null && dlType.isDrcd();
    }

    /**
     * PreliminaryProceedingsAnswerDeadline (DL7)
     *
     * @return PreliminaryProceedingsAnswerDeadline (DL7)
     */
    @Transient
    public boolean isPpad() {
        return dlType != null && dlType.isPpad();
    }

    /**
     * ProposalSubsequentProceedingsDeadline (DL8)
     *
     * @return ProposalSubsequentProceedingsDeadline (DL8)
     */
    @Transient
    public boolean isPspd() {
        return dlType != null && dlType.isPspd();
    }

    /**
     * 6WeeksDeadline (DL9)
     *
     * @return 6WeeksDeadline (DL9)
     */
    @Transient
    public boolean is6Wd() {
        return dlType != null && dlType.is6Wd();
    }

    /**
     * CaseDialogBillCorrectionDeadline (DL10)
     *
     * @return CaseDialogBillCorrectionDeadline (DL10)
     */
    @Transient
    public boolean isCbcd() {
        return dlType != null && dlType.isCbcd();
    }

    @Transient
    @Override
    public String getName() {
        String name = dlType == null ? null : dlType.name();
        return MenuCacheEntity.getName(this, name);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return id; //why is there no internal id?
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return dlValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return dlValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return true; //why is there no dlValid field?
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return false; //why is there no dlDeleted field?
    }

    @Transient
    @Override
    public boolean isInActive() {
        return isInActive(new Date());
    }

//    @Transient
    @Override
    public boolean isInActive(final Date pDate) {
        return isDeleted() || !isValid(pDate);
    }

    @Transient
    @Override
    public int getSort() {
        return 0; //does not exist for deadline!
    }

}

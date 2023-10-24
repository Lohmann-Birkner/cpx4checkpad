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
package de.lb.cpx.server.commonDB.model.rules;

import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;

/**
 * CrgRules initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CRG_RULES:Tabelle definiert die
 * Regel </p>
 */
@MappedSuperclass
public class CrgRules extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    protected CrgRuleTypes crgRuleTypes;
    protected CrgRulePools crgRulePools;
    private String crgrIdentifier;
    private String crgrNumber;
    private String crgrCaption;
    private String crgrCategory;
    private String crgrSuggText;
    private String crgrNote;
    private byte[] crgrDefinition;
    private Serializable crgrSuggestion;
    private Date crgrValidFrom;
    private Date crgrValidTo;
    private RuleTypeEn crgrRuleErrorType;
    private String crgrRid;
    protected Set<?> crgRule2Roles = new HashSet<>(0);
    protected Set<?> crgRule2Tables = new HashSet<>(0);
    private boolean isReadOnlyFl;
    private String crgrFeeGroup;
    private byte[] crgrMessage;

    public CrgRules(long id,
            //            CrgRuleTypes type,
            String caption,
            String category,
            String identifier,
            String note,
            String number,
            String rid,
            RuleTypeEn ruleType,
            String suggText,
            Date from,
            Date to,
            String feeGroup,
            byte[] message) {
        this();
        this.id = id;
//        this.crgRuleTypes = type;
        this.crgrCaption = caption;
        this.crgrCategory = category;
        this.crgrIdentifier = identifier;
        this.crgrNote = note;
        this.crgrNumber = number;
        this.crgrRid = rid;
        this.crgrRuleErrorType = ruleType;
        this.crgrSuggText = suggText;
        this.crgrValidFrom = from == null ? null : new Date(from.getTime());
        this.crgrValidTo = to == null ? null : new Date(to.getTime());
        this.crgrFeeGroup = feeGroup;
        this.crgrMessage = message;
    }

    public CrgRules(long id,
            CrgRuleTypes type,
            CrgRulePools pool,
            String caption,
            String category,
            String identifier,
            String note,
            String number,
            String rid,
            RuleTypeEn ruleType,
            String suggText,
            Date from,
            Date to,
            String feeGroup,
            byte[] message) {
        this();
        this.id = id;
        this.crgRuleTypes = type;
        this.crgRulePools = pool;
        this.crgrCaption = caption;
        this.crgrCategory = category;
        this.crgrIdentifier = identifier;
        this.crgrNote = note;
        this.crgrNumber = number;
        this.crgrRid = rid;
        this.crgrRuleErrorType = ruleType;
        this.crgrSuggText = suggText;
        this.crgrValidFrom = from == null ? null : new Date(from.getTime());
        this.crgrValidTo = to == null ? null : new Date(to.getTime());
        this.crgrFeeGroup = feeGroup;
        this.crgrMessage = message;
    }

    public CrgRules(long id,
            CrgRuleTypes type,
            String caption,
            String category,
            String identifier,
            String note,
            String number,
            String rid,
            RuleTypeEn ruleType,
            String suggText,
            Date from,
            Date to,
            String feeGroup,
            byte[] message) {
        this();
        this.id = id;
        this.crgRuleTypes = type;
        this.crgrCaption = caption;
        this.crgrCategory = category;
        this.crgrIdentifier = identifier;
        this.crgrNote = note;
        this.crgrNumber = number;
        this.crgrRid = rid;
        this.crgrRuleErrorType = ruleType;
        this.crgrSuggText = suggText;
        this.crgrValidFrom = from == null ? null : new Date(from.getTime());
        this.crgrValidTo = to == null ? null : new Date(to.getTime());
        this.crgrFeeGroup = feeGroup;
        this.crgrMessage = message;
    }

    public CrgRules() {
    }

    public static CrgRules getTypeInstance(PoolTypeEn type) {
        switch (type) {
            case DEV:
                return new CrgRulesDev();
            default:
                return new CrgRulesProd();

        }
    }

    @Transient
    public static CrgRules getTypeInstance() {
        return getTypeInstance(PoolTypeEn.PROD);
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return crgRuleTypes:Referenz auf die tabelle CRG_RULE_TYPES.
     */
    @Transient
    public CrgRuleTypes getCrgRuleTypes() {
        return this.crgRuleTypes;
    }

    /**
     *
     * @param crgRuleTypes Column CRGT_ID : Referenz auf die tabelle
     * CRG_RULE_TYPES.
     */
    public void setCrgRuleTypes(CrgRuleTypes crgRuleTypes) {
        this.crgRuleTypes = crgRuleTypes;
    }

    /**
     *
     * @return crgRulePools :Referenz auf die Tablle CRG_RULE_POOLS ,d.h auf den
     * Pool in den die Regel gehört.
     */
    @Transient
    public CrgRulePools getCrgRulePools() {
        return this.crgRulePools;
    }

    /**
     *
     * @param crgRulePools Column CRGPL_ID :Referenz auf die Tablle
     * CRG_RULE_POOLS ,d.h auf den Pool in den die Regel gehört.
     */
    public void setCrgRulePools(CrgRulePools crgRulePools) {
        this.crgRulePools = crgRulePools;
    }

    /**
     *
     * @return crgrIdentifier :Identnummer der Regel, z.Z. besteht aus 3 Teilen,
     * die durch den _ von einander gerennt sind: &lt;jahr &gt;_ &lt; id &gt;
     * _&lt;timestamp &gt;, wird beim Anlegen der Regel automatsch generiert.
     */
    @Column(name = "CRGR_IDENTIFIER", nullable = false, length = 50)
    public String getCrgrIdentifier() {
        return this.crgrIdentifier;
    }

    /**
     * Readonly - Flag by Rules set by Import of Checkpoint - Rules or by the
     * administrator
     *
     * @return is readonly?
     */
    @Column(name = "IS_READ_ONLY_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getIsReadOnlyFl() {
        return isReadOnlyFl;
    }

    public void setIsReadOnlyFl(boolean fl) {
        isReadOnlyFl = fl;
    }

    /**
     *
     * @param crgrIdentifier ColumnCRGR_IDENTIFIER :Identnummer der Regel, z.Z.
     * besteht aus 3 Teilen, die durch den _ von einander gerennt sind: &lt;jahr
     * &gt;_ &lt; id &gt; _&lt;timestamp &gt;, wird beim Anlegen der Regel
     * automatsch generiert.
     */
    public void setCrgrIdentifier(String crgrIdentifier) {
        this.crgrIdentifier = crgrIdentifier;
    }

    /**
     *
     * @return crgrNumber : Von Benutzer vergebene Regelnummer.
     */
    @Column(name = "CRGR_NUMBER", nullable = false, length = 50)
    public String getCrgrNumber() {
        return this.crgrNumber;
    }

    /**
     *
     * @param crgrNumber Column CRGR_NUMBER :Von Benutzer vergebene Regelnummer.
     */
    public void setCrgrNumber(String crgrNumber) {
        this.crgrNumber = crgrNumber;
    }

    /**
     *
     * @return
     */
    @Lob
    @Column(name="CRGR_MESSAGE", nullable = true)
    public byte[] getCrgrMessage() {
        return crgrMessage;
    }

    public void setCrgrMessage(byte[] crgrMessage) {
        this.crgrMessage = crgrMessage;
    }

    
    /**
     *
     * @return crgrCaption :Regelbezeichnung.
     */
    @Column(name = "CRGR_CAPTION")
    public String getCrgrCaption() {
        return this.crgrCaption;
    }

    /**
     *
     * @param crgrCaption Column CRGR_CAPTION :Regelbezeichnung.
     */
    public void setCrgrCaption(String crgrCaption) {
        this.crgrCaption = crgrCaption;
    }

    /**
     *
     * @return crgrCategory:Text zu die Regel.
     */
    @Column(name = "CRGR_CATEGORY")
    public String getCrgrCategory() {
        return this.crgrCategory;
    }

    /**
     *
     * @param crgrCategory Column CRGR_CATEGORY :Text zu die Regel.
     */
    public void setCrgrCategory(String crgrCategory) {
        this.crgrCategory = crgrCategory;
    }

    /**
     *
     * @return crgrSuggText: Vorschlagstext.
     */
    @Column(name = "CRGR_SUGG_TEXT")
    public String getCrgrSuggText() {
        return this.crgrSuggText;
    }

    /**
     *
     * @param crgrSuggText Column CRGR_SUGG_TEXT : Vorschlagstext.
     */
    public void setCrgrSuggText(String crgrSuggText) {
        this.crgrSuggText = crgrSuggText;
    }

    /**
     *
     * @return crgrNote:Regelnotiz.
     */
    @Column(name = "CRGR_NOTE", length = 4000)
    public String getCrgrNote() {
        return this.crgrNote;
    }

    /**
     *
     * @param crgrNote Column CRGR_NOTE :Regelnotiz.
     */
    public void setCrgrNote(String crgrNote) {
        this.crgrNote = crgrNote;
    }

    /**
     *
     * @return crgrDefinition : Regeldefinition. Hier wird die vollständige XML
     * - Regel gespeichert, die ausgliederung der einzellnen Regelfelder und
     * Regelvorschlägen ist ertsmal nicht notwendig
     */
    @Lob
    @Column(name = "CRGR_DEFINITION", length = 4000)
//    @Basic(fetch = FetchType.LAZY)
    public byte[] getCrgrDefinition() {
        return crgrDefinition == null ? null : crgrDefinition.clone();
    }

    /**
     *
     * @param crgrDefinition Column CRGR_DEFINITION : Regeldefinition.
     */
    public void setCrgrDefinition(byte[] crgrDefinition) {
        this.crgrDefinition = crgrDefinition == null ? null : crgrDefinition.clone();
    }

    /**
     *
     * @return crgrSuggestion: Regelvorschlag .
     */
    @Column(name = "CRGR_SUGGESTION")
    public Serializable getCrgrSuggestion() {
        return this.crgrSuggestion;
    }

    /**
     *
     * @param crgrSuggestion Column CRGR_SUGGESTION : Regelvorschlag .
     */
    public void setCrgrSuggestion(Serializable crgrSuggestion) {
        this.crgrSuggestion = crgrSuggestion;
    }

    /**
     *
     * @return crgrValidFrom :Anfang des Gültigkeitsraumes.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRGR_VALID_FROM", length = 7)
    public Date getCrgrValidFrom() {
        return crgrValidFrom == null ? null : new Date(crgrValidFrom.getTime());
    }

    public void setCrgrValidFrom(Date crgrValidFrom) {
        this.crgrValidFrom = crgrValidFrom == null ? null : new Date(crgrValidFrom.getTime());
    }

    @Transient
    public Integer getCrgrValidFromYear() {
        Date date = crgrValidFrom;
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.YEAR);
        }
    }

    @Transient
    public Integer getCrgrValidToYear() {
        Date date = crgrValidTo;
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.YEAR);
        }
    }

    /**
     *
     * @return crgrValidTo:Ende der Gültigkeitsraumes.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRGR_VALID_TO", length = 7)
    public Date getCrgrValidTo() {
        return crgrValidTo == null ? null : new Date(crgrValidTo.getTime());
    }

    public void setCrgrValidTo(Date crgrValidTo) {
        this.crgrValidTo = crgrValidTo == null ? null : new Date(crgrValidTo.getTime());
    }

    @Transient
    public Set<CrgRule2Role> getCrgRule2Roles() {
        return (Set<CrgRule2Role>) crgRule2Roles;
    }

    public void setCrgRule2Roles(Set<?> crgRule2Roles) {
        this.crgRule2Roles = crgRule2Roles;
    }

    @Transient
    public Set<CrgRule2Table> getCrgRule2Tables() {
        return (Set<CrgRule2Table>) crgRule2Tables;
    }

    public void setCrgRule2Tables(Set<?> crgRule2Tables) {
        this.crgRule2Tables = crgRule2Tables;
    }

    @Column(name = "CRGR_ERR_TYPE_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public RuleTypeEn getCrgrRuleErrorType() {
        return crgrRuleErrorType;
    }

    public void setCrgrRuleErrorType(RuleTypeEn crgrRuleErrorType) {
        this.crgrRuleErrorType = crgrRuleErrorType;
    }

    @Column(name = "CRGR_RID", nullable = false, length = 25)
    public String getCrgrRid() {
        return crgrRid;
    }

    public void setCrgrRid(String crgrRid) {
        this.crgrRid = crgrRid;
    }

    /**
     *
     * @param crgrFeeGroup ColumnCRGR_FEE_GROUP :Entgeltgruppe(n) der Regel,
     * z.Z. Aufzählung der Entgeltgruppe(n) separiert durch Komma
     */
    public void setCrgrFeeGroup(String crgrFeeGroup) {
        this.crgrFeeGroup = crgrFeeGroup;
    }

    /**
     *
     * @return crgrNumber : Von Benutzer vergebene Regelnummer.
     */
    @Column(name = "CRGR_FEE_GROUP", nullable = true, length = 20)
    public String getCrgrFeeGroup() {
        return this.crgrFeeGroup;
    }

    @Override
    public String toString() {
        return "rid: " + (this.getCrgrRid() == null ? "null" : this.getCrgrRid()) + " caption: " + (this.getCrgrCaption() == null ? "" : getCrgrCaption());
    }

    @Transient
    public long getId() {
        return id;
    }

    public void generateCrgrIdentifier() {
        Date timestamp = this.getCreationDate();
        crgrIdentifier = getCrgRulePools().getCrgplIdentifier() + "_" + getId() + "_"
                + String.valueOf(timestamp == null ? new Date().getTime() : timestamp.getTime());
    }

    public CrgRule2Role getRoleRelation2RoleId(long id) {
        Set<CrgRule2Role> rule2roleSet = getCrgRule2Roles();
        for (CrgRule2Role r2r : rule2roleSet) {
            if (r2r.getCdbUserRoles().getId() == id) {
                return r2r;
            }
        }
        return null;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, crgrNumber);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return id;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return crgrValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return crgrValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return true; //why is there no crgrValid field?
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return false; //why is there no crgrDeleted field?
    }

    @Transient
    @Override
    public boolean isInActive() {
        return isInActive(new Date());
    }

    @Override
    public boolean isInActive(final Date pDate) {
        return isDeleted(); // it is an ungood solution to remove this code: || !isValid(pDate); Use MenuCache.getMenuCacheRules().values(MenuCacheOptionsEn.IGNORE_DELETED) to filter entries on client-side (this is just an example)
    }

    @Transient
    @Override
    public int getSort() {
        return 0; //does not exist for rule!
    }

}

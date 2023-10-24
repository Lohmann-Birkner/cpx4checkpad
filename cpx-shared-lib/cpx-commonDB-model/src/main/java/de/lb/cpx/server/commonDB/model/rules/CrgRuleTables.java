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

import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * CrgRuleTables initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CRG_RULE_TABLES : Regeltabellen
 * </p>
 */
@MappedSuperclass
public class CrgRuleTables extends AbstractEntity implements RuleTablesIF{

    private static final long serialVersionUID = 1L;

//     private long id;
    protected CrgRulePools crgRulePools;
    private String crgtTableName;
    private String crgtContent;
    private String crgtComment;
    private byte[] crgtMessage;
    private RuleTableCategoryEn crgtCategory; 
    protected Set<?> crgRule2Tables = new HashSet<>(0);

    /**
     * we need this constructor for CrgRuleTablesDao
     *
     * @param id id
     * @param tableName table name
     * @param creationUserId creation userId
     * @param creation_date creation date
     * @param message mesage json
     * @param crgtCategory category
     */
    public CrgRuleTables(long id, String tableName, Long creationUserId, Date creation_date,  byte[] message,  RuleTableCategoryEn crgtCategory) {
        this();
        this.id = id;
        this.crgtTableName = tableName;
        this.setCreationUser(creationUserId);
        this.setCreationDate(creation_date);
        this.crgtMessage = message;
        this.crgtCategory = crgtCategory;
    }

    public CrgRuleTables(long id, String tableName, String tableContent, Long creationUserId, Date creation_date, byte[] message, RuleTableCategoryEn crgtCategory) {
        this();
        this.id = id;
        this.crgtTableName = tableName;
        this.crgtContent = tableContent;
        this.setCreationUser(creationUserId);
        this.setCreationDate(creation_date);
        this.crgtMessage = message;
        this.crgtCategory = crgtCategory;
    }

    /**
     * we need this constructor for CrgRuleTablesDao
     *
     * @param id id
     * @param tableName table name
     * @param message mesage json
     * @param crgtCategory category
     */
    public CrgRuleTables(long id, String tableName, byte[] message, RuleTableCategoryEn crgtCategory) {
        this();
        this.id = id;
        this.crgtTableName = tableName;
        this.crgtMessage = message;
        this.crgtCategory = crgtCategory;
    }

    public CrgRuleTables() {

    }

    public static CrgRuleTables getTypeInstance(PoolTypeEn type) {
        switch (type) {
            case DEV:
                return new CrgRuleTablesDev();
            default:
                return new CrgRuleTablesProd();

        }
    }

    @Transient
    public static CrgRuleTables getTypeInstance() {
        return getTypeInstance(PoolTypeEn.PROD);
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @param crgRulePools Column CRGPL_ID :Referenz auf die Tablle
     * CRG_RULE_POOLS(Regelpool)
     */
    public void setCrgRulePools(CrgRulePools crgRulePools) {
        this.crgRulePools = crgRulePools;
    }

    /**
     *
     * @return crgtTableName :Bezeichnung der Tabelle, wie sie in der Regel
     * erscheint
     */
    @Column(name = "CRGT_TABLE_NAME", length = 50)
    public String getCrgtTableName() {
        return this.crgtTableName;
    }

    /**
     *
     * @param crgtTableName Column CRGT_TABLE_NAME : Bezeichnung der Tabelle,
     * wie sie in der Regel erscheint
     */
    public void setCrgtTableName(String crgtTableName) {
        this.crgtTableName = crgtTableName;
    }

    /**
     *
     * @return crgtContent : Inhalt der Tabelle
     */
    @Lob
    @Column(name = "CRGT_CONTENT")
    public String getCrgtContent() {
        return this.crgtContent;
    }

    @Column(name = "CRGT_COMMENT")
    public String getCrgtComment(){
        return this.crgtComment;
    }
    @Lob
    @Column(name="CRGT_MESSAGE", nullable=true)
    public byte[] getCrgtMessage() {
        return crgtMessage;
    }

    public void setCrgtMessage(byte[] crgtMessage) {
        this.crgtMessage = crgtMessage;
    }

    
    @Column(name="CRGT_CATEGORY", length = 50, nullable = true)
    @Enumerated(EnumType.STRING)
    public RuleTableCategoryEn getCrgtCategory() {
        return crgtCategory;
    }

    public void setCrgtCategory(RuleTableCategoryEn crgtCategory) {
        this.crgtCategory = crgtCategory;
    }
    /**
     *
     * @param crgtContent Column CRGT_CONTENT :Inhalt der Tabelle
     */
    public void setCrgtContent(String crgtContent) {
        this.crgtContent = crgtContent;
    }
    
    public void setCrgtComment(String crgtComment){
        this.crgtComment = crgtComment;
    }

    @Transient
    public Set<?> getCrgRule2Tables() {
        return crgRule2Tables;
    }

    public void setCrgRule2Tables(Set<?> crgRule2Tables) {
        this.crgRule2Tables = crgRule2Tables;
    }

    @Transient
    public CrgRulePools getCrgRulePools() {
        return crgRulePools;
    }

    @Transient
    public long getId() {
        return id;
    }

    @Override
    public boolean hasErrorMessage() {
        return getCrgtMessage() != null;
    }

    @Transient
    @Override
    public String getRuleTableName() {
        return getCrgtTableName();
    }
    @Transient
    @Override
    public RuleTableCategoryEn getRuleTableCategory() {
        return getCrgtCategory();
    }


    @Override
    public void setRuleTableMessage(byte[] msg) {
        setCrgtMessage(msg);
    }

    @Transient
    @Override
    public String getRuleTableContent() {
        return getCrgtContent();
    }
}

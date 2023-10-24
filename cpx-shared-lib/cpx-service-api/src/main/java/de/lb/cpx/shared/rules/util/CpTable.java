/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.rules.util;

import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.RuleTablesIF;
import java.util.Objects;

/**
 * Helpclass for import and export of rule tables
 * @author gerschmann
 */
public class CpTable implements RuleTablesIF{
        private String name;
        private String content;
        private String comment;
        private RuleTableCategoryEn category = RuleTableCategoryEn.NOT_SET;
        byte[] message;
        private int srcYear; // used for web validation to set in error xml export
        
 
       public CpTable(String pName, String pContent, String pComment, RuleTableCategoryEn pCategory){
           this(pName, pContent, pComment);
           category = pCategory;
       }
        
        public CpTable(String pName, String pContent, String pComment){
            name = pName;
            content = pContent;
            comment = pComment;

        }

        public String getName() {
            return name;
        }

        public String getContent() {
            return content;
        }

        public String getComment() {
            return comment;
        }

    public RuleTableCategoryEn getCategory() {
        return category;
    }

    public void setCategory(RuleTableCategoryEn category) {
        this.category = category;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    @Override
    public boolean hasErrorMessage() {
        return message != null && message.length > 0;
    }

    @Override
    public String getRuleTableName() {
        return getName();
    }

    @Override
    public RuleTableCategoryEn getRuleTableCategory() {
        return this.getCategory();
    }

    @Override
    public void setRuleTableMessage(byte[] msg) {
        this.setMessage(msg);
    }

    @Override
    public String getRuleTableContent() {
        return getContent();
    }

    public int getSrcYear() {
        return srcYear;
    }

    public void setSrcYear(int srcYear) {
        this.srcYear = srcYear;
    }
    
        @Override
        public boolean equals(Object obj){
            return obj instanceof CpTable && this.hashCode() == obj.hashCode();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + Objects.hashCode(this.name);
            hash = 67 * hash +  Objects.hashCode(this.content);
            hash = 67 * hash + Objects.hashCode(this.comment);
            return hash;
        }

   
}

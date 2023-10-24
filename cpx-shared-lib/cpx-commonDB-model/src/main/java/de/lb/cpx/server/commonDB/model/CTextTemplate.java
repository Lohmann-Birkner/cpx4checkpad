/* 
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang3.StringUtils;

/**
 * This entity is used to create C_TEXT_TEMPLATE table, which can be used to
 * create and configure Text templates
 *
 * @author nandola
 */
@Entity
@Table(name = "C_TEXT_TEMPLATE")
@SuppressWarnings("serial")
public class CTextTemplate extends AbstractEntity implements Comparable<CTextTemplate> {

    private static final long serialVersionUID = 1L;

//    private TextTemplateTypeEn textTemplateTypeEn;
    private String templateName;
    private String templateDescription;
    private String templateContent;
    private int templateSort;

    private Set<CTextTemplate2UserRole> cTextTemplate2UserRoles = new HashSet<>(0);
    private Set<CTextTemplate2Context> cTextTemplate2Context = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_TEXT_TEMPLATE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    @Column(name = "CONTEXT", nullable = false, length = 50)
//    @Enumerated(EnumType.STRING)
//    public TextTemplateTypeEn getTemplateContextType() {
//        return this.textTemplateTypeEn;
//    }
//
//    public void setTemplateContextType(TextTemplateTypeEn textTemplateTypeEn) {
//        this.textTemplateTypeEn = textTemplateTypeEn;
//    }
    /**
     * @return templateName: Name of the text template
     */
    @Column(name = "NAME", length = 50)
    public String getTemplateName() {
        return this.templateName;
    }

    /**
     * @param templateName the Template name to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return templateDescription: Description of the text template (e.g: the
     * use/purpose of this template)
     */
    @Column(name = "DESCRIPTION", length = 500)
    public String getTemplateDescription() {
        return this.templateDescription;
    }

    /**
     * @param templateDescription template Description to set
     */
    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
    }

    /**
     * @return templateContent: return template content itself
     */
    @Lob
    @Column(name = "CONTENT", nullable = false)
    @Basic(fetch = FetchType.LAZY)
    public String getTemplateContent() {
        return templateContent == null ? null : templateContent;
    }

    /**
     * @param templateContent: template Content to set
     */
    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent == null ? null : templateContent;
    }

    /**
     * @return templateSort: return sorting no of the template
     */
    @Column(name = "SORTING_ORDER", nullable = false)
    public int getTemplateSort() {
        return templateSort;
    }

    /**
     * @param templateSort: template sorting no to set
     */
    public void setTemplateSort(int templateSort) {
        this.templateSort = templateSort;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "textTemplate")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<CTextTemplate2UserRole> getCTextTemplate2UserRole() {
        return this.cTextTemplate2UserRoles;
    }

    public void setCTextTemplate2UserRole(Set<CTextTemplate2UserRole> cTextTemplate2UserRoles) {
        this.cTextTemplate2UserRoles = cTextTemplate2UserRoles;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "textTemplate")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<CTextTemplate2Context> getCTextTemplate2Context() {
        return this.cTextTemplate2Context;
    }

    public void setCTextTemplate2Context(Set<CTextTemplate2Context> cTextTemplate2Context) {
        this.cTextTemplate2Context = cTextTemplate2Context;
    }

    @Override
    public int compareTo(final CTextTemplate o) {
        return StringUtils.trimToEmpty(this.getTemplateName())
                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getTemplateName()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
//        hash = 97 * hash + Objects.hashCode(this.textTemplateTypeEn);
        hash = 97 * hash + Objects.hashCode(this.templateName);
        hash = 97 * hash + Objects.hashCode(this.templateDescription);
        hash = 97 * hash + Objects.hashCode(this.templateContent);
        hash = 97 * hash + Objects.hashCode(this.templateSort);
        hash = 97 * hash + Objects.hashCode(this.cTextTemplate2UserRoles);
        hash = 97 * hash + Objects.hashCode(this.cTextTemplate2Context);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CTextTemplate other = (CTextTemplate) obj;
        if (!Objects.equals(this.templateName, other.templateName)) {
            return false;
        }
        if (!Objects.equals(this.templateDescription, other.templateDescription)) {
            return false;
        }
        if (!Objects.equals(this.templateContent, other.templateContent)) {
            return false;
        }
//        if (this.textTemplateTypeEn != other.textTemplateTypeEn) {
//            return false;
//        }
        if (!Objects.equals(this.templateSort, other.templateSort)) {
            return false;
        }
//        if (!Objects.equals(this.cTextTemplate2UserRoles, other.cTextTemplate2UserRoles)) {
//            return false;
//        }
//        if (!Objects.equals(this.cTextTemplate2Context, other.cTextTemplate2Context)) {
//            return false;
//        }
        return true;
    }

}

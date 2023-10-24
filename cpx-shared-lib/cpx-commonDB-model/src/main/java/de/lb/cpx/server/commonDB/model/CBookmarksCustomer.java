/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * to define customer specific Bookmarks
 *
 * @author nandola
 */
@Entity
@Table(name = "C_BOOKMARKS_CUSTOMER")
@SuppressWarnings("serial")
public class CBookmarksCustomer extends AbstractEntity {

    private static final long serialVersionUID = 1L;
//    private static final Logger LOG = Logger.getLogger(CBookmarksCustomer.class.getName());

    private String name;
    private String text;
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_BOOKMARKS_CUSTOMER_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return name: Name of the Bookmark
     */
    @Column(name = "NAME", nullable = false, length = 50)
    public String getBookmarkName() {
        return this.name;
    }

    /**
     * @param name the Bookmark name to set
     */
    public void setBookmarkName(String name) {
        this.name = name;
    }

    /**
     * @return text: text (value) of the Bookmark
     */
    @Column(name = "TEXT", nullable = false, length = 1000)
    public String getBookmarkText() {
        return this.text;
    }

    /**
     * @param text the Bookmark text to set
     */
    public void setBookmarkText(String text) {
        this.text = text;
    }

    /**
     * @return description: Description of the Bookmark (e.g: the use/purpose of
     * this Bookmark)
     */
    @Column(name = "DESCRIPTION", length = 500)
    public String getBookmarkDescription() {
        return this.description;
    }

    /**
     * @param description Bookmark Description to set
     */
    public void setBookmarkDescription(String description) {
        this.description = description;
    }

}

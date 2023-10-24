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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.catalog;

import de.lb.cpx.server.commonDB.model.CIcdCatalog;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxIcd extends CIcdCatalog implements ICpxTreeItem<CpxIcdThesaurus> {

    private static final long serialVersionUID = 1L;

    private long parentId = 0L;
    private ICpxTreeItem<CpxIcdThesaurus> parent = null;
    private final List<ICpxTreeItem<CpxIcdThesaurus>> children = new ArrayList<>();
    private boolean visible = false;

    private final List<CpxIcdThesaurus> thesaurus = new ArrayList<>();
    private String thesaurusString = null;
    private String searchableDescription = "";

    @Override
    public boolean isCompleteFl() {
        return getIcdIsCompleteFl();
    }

    @Override
    public String getCode() {
        return getIcdCode();
    }

    @Override
    public String getDescription() {
        return getIcdDescription();
    }

    @Override
    public String getInclusion() {
        return getIcdInclusion();
    }

    @Override
    public String getExclusion() {
        return getIcdExclusion();
    }

    @Override
    public String getNote() {
        return getIcdNote();
    }

    @Override
    public int getDepth() {
        return getIcdDepth();
    }

    @Override
    public ICpxTreeItem<CpxIcdThesaurus> getRootParent() {
        ICpxTreeItem<CpxIcdThesaurus> item = this;
        while (true) {
            if (item.getParent() == null) {
                return item;
            }
            item = item.getParent();
        }
    }

    @Override
    public boolean like(final List<List<String>> pExprList, final List<List<Integer>> pFixedExprIndexes) {
        if (!this.isCompleteFl()) {
            return false; //Only allowed on parent item!
        }
        //     String expr = (pExpr == null) ? "" : pExpr.toLowerCase().trim();
        //setVisible(false, true, true);
        return ICpxTreeItem.like(this, pExprList, pFixedExprIndexes);
        //setVisible(l, false, false);
        //return l;
    }

    @Override
    public List<CpxIcdThesaurus> getThesaurus() {
        return new ArrayList<>(thesaurus);
    }

    @Override
    public void addThesaurus(CpxIcdThesaurus icdThesaurusObj) {
        thesaurus.add(icdThesaurusObj);
        if (thesaurusString == null) {
            thesaurusString = "";
        }
        thesaurusString += icdThesaurusObj.getDescription() + "\n";
    }

    @Override
    public String getThesaurusString() {
        return thesaurusString;
    }

    @Override
    public String getSearchableDescription() {
        return searchableDescription;
    }

    @Override
    public void setSearchableDescription(String description) {
        this.searchableDescription = description;
    }

    @Override
    public List<ICpxTreeItem<CpxIcdThesaurus>> getLastChildren() {
        List<ICpxTreeItem<CpxIcdThesaurus>> childrenList = new ArrayList<>();
        getChildren(childrenList, this);
        return childrenList;
    }

    private void getChildren(List<ICpxTreeItem<CpxIcdThesaurus>> childrenList, final ICpxTreeItem<CpxIcdThesaurus> pCpxIcd) {
        if (pCpxIcd == null) {
            return;
        }
        if (pCpxIcd.isCompleteFl()) {
            childrenList.add(pCpxIcd);
            return;
        }
        for (ICpxTreeItem<CpxIcdThesaurus> pCpxIcdChildren : pCpxIcd.getChildren()) {
            getChildren(childrenList, pCpxIcdChildren);
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(final boolean pVisible) {
        //setVisible(pVisible, false, false);
        visible = pVisible;
    }

    @Override
    public void setVisible(final boolean pVisible, final boolean pVisibleParent, final boolean pVisibleChildren) {
        ICpxTreeItem.setVisibleStatic(this, pVisible, pVisibleParent, pVisibleChildren);
        //visible = pVisible;
    }

    @Override
    public ICpxTreeItem<CpxIcdThesaurus> setParentId(final Long pParentId) {
        parentId = (pParentId == null) ? 0L : pParentId;
        return this;
    }

    @Override
    public void setParent(final ICpxTreeItem<CpxIcdThesaurus> pCpxIcdParent) {
        parent = pCpxIcdParent;
    }

    @Override
    public long getParentId() {
        return parentId;
    }

    @Override
    public ICpxTreeItem<CpxIcdThesaurus> getParent() {
        //return CpxIcdCatalog.instance().getById(getParentId(), getCountryEn().toString(), getIcdYear());
        return parent;
    }

    @Override
    public void addChildren(final ICpxTreeItem<CpxIcdThesaurus> pCpxIcd) {
        children.add(pCpxIcd);
    }

    @Override
    public List<ICpxTreeItem<CpxIcdThesaurus>> getChildren() {
        return new ArrayList<>(children);
    }

}

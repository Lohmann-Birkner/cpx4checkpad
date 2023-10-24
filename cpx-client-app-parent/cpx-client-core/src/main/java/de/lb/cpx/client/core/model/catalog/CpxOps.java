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

import de.lb.cpx.server.commonDB.model.COpsCatalog;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxOps extends COpsCatalog implements ICpxTreeItem<CpxOpsThesaurus> {

    private static final long serialVersionUID = 1L;

    private long parentId = 0L;
    private ICpxTreeItem<CpxOpsThesaurus> parent = null;
    private final List<ICpxTreeItem<CpxOpsThesaurus>> children = new ArrayList<>();
    private boolean visible = false;

    private final List<CpxOpsThesaurus> thesaurus = new ArrayList<>();
    private CpxOpsAop aop = null;
    private String aopString = null;
    private String thesaurusString = null;
    private String searchableDescription = "";

    @Override
    public boolean isCompleteFl() {
        return getOpsIsCompleteFl();
    }

    @Override
    public String getCode() {
        return getOpsCode();
    }

    @Override
    public String getDescription() {
        return getOpsDescription();
    }

    @Override
    public String getInclusion() {
        return getOpsInclusion();
    }

    @Override
    public String getExclusion() {
        return getOpsExclusion();
    }

    @Override
    public String getNote() {
        return getOpsNote();
    }

    @Override
    public int getDepth() {
        return getOpsDepth();
    }

    @Override
    public ICpxTreeItem<CpxOpsThesaurus> getRootParent() {
        ICpxTreeItem<CpxOpsThesaurus> item = this;
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
    public List<CpxOpsThesaurus> getThesaurus() {
        return new ArrayList<>(thesaurus);
    }

    @Override
    public void addThesaurus(CpxOpsThesaurus icdThesaurusObj) {
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
    public List<ICpxTreeItem<CpxOpsThesaurus>> getLastChildren() {
        List<ICpxTreeItem<CpxOpsThesaurus>> childrenList = new ArrayList<>();
        getChildren(childrenList, this);
        return childrenList;
    }

    private void getChildren(List<ICpxTreeItem<CpxOpsThesaurus>> childrenList, final ICpxTreeItem<CpxOpsThesaurus> pCpxIcd) {
        if (pCpxIcd == null) {
            return;
        }
        if (pCpxIcd.isCompleteFl()) {
            childrenList.add(pCpxIcd);
            return;
        }
        for (ICpxTreeItem<CpxOpsThesaurus> pCpxIcdChildren : pCpxIcd.getChildren()) {
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
    public ICpxTreeItem<CpxOpsThesaurus> setParentId(final Long pParentId) {
        parentId = (pParentId == null) ? 0L : pParentId;
        return this;
    }

    @Override
    public void setParent(final ICpxTreeItem<CpxOpsThesaurus> pCpxIcdParent) {
        parent = pCpxIcdParent;
    }

    @Override
    public long getParentId() {
        return parentId;
    }

    @Override
    public ICpxTreeItem<CpxOpsThesaurus> getParent() {
        //return CpxIcdCatalog.instance().getById(getParentId(), getCountryEn().toString(), getIcdYear());
        return parent;
    }

    @Override
    public void addChildren(final ICpxTreeItem<CpxOpsThesaurus> pCpxIcd) {
        children.add(pCpxIcd);
    }

    @Override
    public List<ICpxTreeItem<CpxOpsThesaurus>> getChildren() {
        return new ArrayList<>(children);
    }

    public void addAop(CpxOpsAop pAop) {
        aop = pAop;
        if(pAop != null){

            aopString = pAop.getCategoryString();
        }
    }

    public String getAopString() {
        return aopString;
    }

 }

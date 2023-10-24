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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.job.fx.casemerging.details;

import de.lb.cpx.client.app.job.fx.casemerging.model.CaseMergeContent;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparablePane;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparablePaneSkin;
import de.lb.cpx.client.core.model.fx.comparablepane.ItemContainer;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javafx.collections.MapChangeListener;
import javafx.scene.layout.Region;

/**
 *
 * @author wilde
 */
public class MergeVersionListener<Z extends Region,E extends ComparableContent<? extends AbstractEntity>> implements MapChangeListener<CaseMergeContent, ItemContainer<Region, CaseMergeContent>> {

    private final ComparablePane<Z, E> content;

    public MergeVersionListener(ComparablePane<Z, E> pContent) {
        content = pContent;
    }

    @Override
    public void onChanged(MapChangeListener.Change<? extends CaseMergeContent, ? extends ItemContainer<Region, CaseMergeContent>> change) {
        if (change.wasAdded()) {
            if (change.getMap().keySet().size() > 1) {
                ((ComparablePaneSkin) content.getSkin()).addInAddVersionPane(change.getValueAdded());
            } else {
                ((ComparablePaneSkin) content.getSkin()).addInBaseVersionPane(change.getValueAdded());
            }

        }
        if (change.wasRemoved()) {
            if (change.getMap().keySet().size() >= 1) {
                ((ComparablePaneSkin) content.getSkin()).removeFromAddVersionPane(change.getValueRemoved());
            } else {
                ((ComparablePaneSkin) content.getSkin()).removeFromBaseVersionPane(change.getValueRemoved());
            }
        }
    }

}

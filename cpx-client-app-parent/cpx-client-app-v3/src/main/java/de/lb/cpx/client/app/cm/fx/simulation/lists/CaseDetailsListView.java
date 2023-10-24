/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.lists;

import de.lb.cpx.client.core.model.fx.labeled.LabeledListView;
import de.lb.cpx.model.TCaseDetails;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;

/**
 * Implementation for the case details ListView in the versionManagement Screen
 * in the case managemnt / simulation TODO: not really useful, does not need
 * some special methodes besides the methodes provided by the base class
 *
 * @author wilde
 */
public class CaseDetailsListView extends LabeledListView<TCaseDetails> {

    /**
     * construct new list view instance with default title "Label"
     */
    public CaseDetailsListView() {
        super(new ListView<>(), new Insets(8.0, 8.0, 8.0, 8.0));
        setTitle("Label");
    }

    /**
     * Construct new list view instance with given title string
     *
     * @param pTitle title to set in the title label
     */
    public CaseDetailsListView(String pTitle) {
        this();
        setTitle(pTitle);
    }
    
}

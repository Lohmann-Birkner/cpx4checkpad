/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filter.tableview;

import de.lb.cpx.client.core.model.fx.tableview.AppendableTableView;
import de.lb.cpx.client.core.model.fx.tableview.FilterBaseTableViewSkin;
import javafx.geometry.Insets;

/**
 * Creates filter tableview skin Manipulate menu height
 *
 * @author wilde
 * @param <T> content of tableview
 */
public class FilterTableViewSkin<T> extends FilterBaseTableViewSkin<T> {

    public FilterTableViewSkin(AppendableTableView<T> skinable) {
        super(skinable);
        //set insets to bottom
        menu.setPadding(new Insets(0, 0, 2, 0));
        //set menuHeight of the tableview
        setMenuHeight(48);
    }
}

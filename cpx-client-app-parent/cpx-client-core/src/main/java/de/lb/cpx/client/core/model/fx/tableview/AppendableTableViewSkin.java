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
package de.lb.cpx.client.core.model.fx.tableview;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollBar;

/**
 * Skin for the appenable table view reacts to scroll to the end and reloads ind
 * skinable
 *
 * @author wilde
 * @param <T> object of the tableview
 */
public class AppendableTableViewSkin<T> extends AsyncTableViewSkin<T> {

    /**
     * creates new instance creates listener on vertical bar value-property, to
     * listen if user scrolles to end TODO: refactor detection if end was hit,
     * disabled appending if user "reaches to fast" the end if value changes
     * from 0 to 1, it is not considert as user input, and the event will be
     * ignored
     *
     * @param skinable object to skin
     */
    public AppendableTableViewSkin(AppendableTableView<T> skinable) {
        super(skinable);
        ScrollBar verticalBar = getVBar();
        if (verticalBar != null) {
            verticalBar.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    //checks new value, if 1 bar hits its end
                    if (Double.doubleToRawLongBits(1d) == Double.doubleToRawLongBits(newValue.doubleValue())) {
                        //check if oldvalue is also 1, to avoid double loading
                        //TODO: could be removed?
                        if (!(Double.doubleToRawLongBits(1d) == Double.doubleToRawLongBits(oldValue.doubleValue()))) {
                            //if scrolling is "to fast" 
                            //input is ignored, change immediately from 0 (beginning) to 1 (ending)
                            //it is ignored, maybe this could be troublesome? but otherwise double loading could happen
                            if (!(Double.doubleToRawLongBits(0.0d) == Double.doubleToRawLongBits(oldValue.doubleValue())
                                    && Double.doubleToRawLongBits(1d) == Double.doubleToRawLongBits(newValue.doubleValue()))) {
//                                if (skinable.getItems().size() < skinable.getMaxCount()) {
                                //AWi-20180928:
                                //removed due to stoploading property
                                //in workflow list, max value could not set properly 
                                //so value from server is used, where it is stored if there are more results
//                                    if(skinable.getItems().size()< skinable.getMaxItems()){
                                if (skinable.isStopLoading()) {
                                    skinable.appendData();
                                }
//                                    }
//                                }
                            }
                        }
                    }
                }
            });
        }
    }

}

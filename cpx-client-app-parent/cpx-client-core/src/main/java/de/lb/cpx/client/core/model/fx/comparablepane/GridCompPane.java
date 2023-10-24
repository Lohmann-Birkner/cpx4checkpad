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
package de.lb.cpx.client.core.model.fx.comparablepane;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javafx.scene.layout.GridPane;

/**
 * horizontal comparable pane based on grid pane
 *
 * @author wilde
 * @param <E> enclosing object
 */
public abstract class GridCompPane<E extends ComparableContent<? extends AbstractEntity>> extends ComparablePane<GridPane, E> {

    public GridCompPane() {
        super();
//        versionList.addListener(new ListChangeListener<E>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends E> c) {
//                while(c.next()){
//                    if(c.wasAdded()){
//                        for(E version : c.getAddedSubList()){
//                            GridPane view = createNewVersionTableView(version);
//                            //bind width to size of the combobox
//                            view.minWidthProperty().bind(version.widthProperty());
//                            view.prefWidthProperty().bind(version.widthProperty());
//                            view.maxWidthProperty().bind(version.widthProperty());
//                            getTableViewToVersion().put(version, view);
//                        }
//                    }
//                    if(c.wasRemoved()){
//                        for(E version : c.getRemoved()){
//                            getTableViewToVersion().remove(version);
//                        }
//                    }
//                }
//           
//            }
//
//        });

        setMinWidthVBar(15);
        getInfo().setMaxWidth(USE_COMPUTED_SIZE);
        getInfo().setMaxHeight(USE_COMPUTED_SIZE);

    }

}

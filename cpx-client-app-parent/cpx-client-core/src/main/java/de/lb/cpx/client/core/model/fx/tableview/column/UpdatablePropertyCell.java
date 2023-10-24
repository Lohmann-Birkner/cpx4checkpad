/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tableview.column;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

/**
 *
 * @author wilde
 * @param <E> content of the tableview
 * @param <T> content of the Cell
 */
public abstract class UpdatablePropertyCell<E, T> extends TableCell<E, T> {

    public UpdatablePropertyCell() {
        tableRowProperty().addListener(new ChangeListener<TableRow<E>>() {
            ChangeListener<E> itmListener = new ChangeListener<E>() {
                @Override
                public void changed(ObservableValue<? extends E> ov, E t, E t1) {
                    if (t1 == null) {
                        removeListener(t, getObservable(t), getListener());
                        if (t != null) {
                            removeListener(t, getObservable(t), getListener());
                        }
                        return;
                    }
                    addListener(t1, getObservable(t1), getListener());
                    if (t != null) {
                        removeListener(t, getObservable(t), getListener());
                    }
                }
            };

            @Override
            public void changed(ObservableValue<? extends TableRow<E>> ov, TableRow<E> t, TableRow<E> t1) {
                if (t1 == null) {
                    cleanUp(t);
                    return;
                }
                removeItemListener(t);
                t1.itemProperty().addListener(itmListener);
                if (t1.getItem() != null) {
                    addListener(t1.getItem(), getObservable(t1.getItem()), getListener());
                }
            }

            private void cleanUp(TableRow<E> t) {
                tableRowProperty().removeListener(this);
                removeItemListener(t);
            }

            private void removeItemListener(TableRow<E> pRow) {
                if (pRow != null) {
                    pRow.itemProperty().removeListener(itmListener);
                }
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void removeListener(E pItem, ObservableValue<? extends Object> pObservable, ChangeListener pListener) {
        pObservable.removeListener(pListener);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addListener(E pItem, ObservableValue<? extends Object> pObservable, ChangeListener pListener) {
        pObservable.addListener(pListener);
    }

    public abstract ObservableValue<? extends Object> getObservable(E pItem);

    public abstract ChangeListener<? extends Object> getListener();
}

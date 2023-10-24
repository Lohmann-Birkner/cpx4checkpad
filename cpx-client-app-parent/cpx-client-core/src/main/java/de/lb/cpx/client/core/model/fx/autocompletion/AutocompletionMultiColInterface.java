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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.autocompletion;

import java.io.Serializable;

/**
 *
 * @author niemeier
 * @param <T> type
 */
public interface AutocompletionMultiColInterface<T extends Serializable> {

    String getText2();

    void selectItem(String[] result, int position);

    void setListener();

    Autocompletion<T, ?> getAutocompletion();

}

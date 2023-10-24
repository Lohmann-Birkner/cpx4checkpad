/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.shared.dto.LockException;

/**
 * Is used to execute code that throws an lock exception in a loop until lock
 * dialog is canceled or all locks are released
 *
 * @author niemeier
 * @param <P> parameter type
 * @param <R> return type
 */
@FunctionalInterface
public interface LockCallback<P, R> {

    /**
     * The <code>call</code> method is called when required, and is given a
     * single argument of type P, with a requirement that an object of type R is
     * returned.
     *
     * @param param The single argument upon which the returned value should be
     * determined.
     * @return An object of type R that may be determined based on the provided
     * parameter value.
     * @throws LockException as long as lock exception is fired the lock dialog
     * opens again and again
     */
    R call(P param) throws LockException;
}

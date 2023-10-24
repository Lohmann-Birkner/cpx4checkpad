/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.helper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class ResponseLoader {

    private static final Logger LOG = Logger.getLogger(ResponseLoader.class.getName());
    
    protected AtomicBoolean stopSignal;
    protected AtomicInteger responseLoaderStopped;
    protected Callback stoppedCb;
    protected boolean stopped = false;



    protected boolean checkStopped() {
        if (stopSignal.get()) {
            responseLoaderStopped.incrementAndGet();
            stopped = true;
            stoppedCb.execute();
            return true;
        }
        return false;
    }
}

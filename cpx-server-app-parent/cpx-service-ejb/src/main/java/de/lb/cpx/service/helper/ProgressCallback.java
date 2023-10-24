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
package de.lb.cpx.service.helper;

/**
 *
 * @author niemeier
 */
public abstract class ProgressCallback {

    public abstract void execute(final long pStartTime, final int pStep, final int pMaxSteps, final String pComment);
    
    public void execute(final long pStartTime, final int pStep, final int pMaxSteps, 
            final int subphase, final int subphases,final String pComment){
        execute(pStartTime, pStep, pMaxSteps, pComment);
    }

}

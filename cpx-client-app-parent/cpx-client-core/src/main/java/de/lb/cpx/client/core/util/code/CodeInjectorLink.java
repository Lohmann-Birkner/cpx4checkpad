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
package de.lb.cpx.client.core.util.code;

/**
 *
 * @author niemeier
 */
public class CodeInjectorLink {

    public final CodeInjectorType type;
    public final int start;
    public final int end;
    public final String text;
    public final CodeInjectorHandler handler;

    public CodeInjectorLink(final CodeInjectorType type, final int pStart,
            final int pEnd, final String pText, final CodeInjectorHandler pHandler) {
        this.type = type;
        this.start = pStart;
        this.end = pEnd;
        this.text = pText;
        this.handler = pHandler;
    }

}

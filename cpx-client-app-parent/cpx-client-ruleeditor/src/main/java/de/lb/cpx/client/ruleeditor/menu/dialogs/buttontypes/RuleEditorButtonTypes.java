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
package de.lb.cpx.client.ruleeditor.menu.dialogs.buttontypes;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author wilde
 */
public class RuleEditorButtonTypes {

    public static final ButtonType SAVE_ALL = new ButtonType("Änderungen speichern", ButtonBar.ButtonData.OTHER);
    public static final ButtonType SAVE_AND_CLOSE = new ButtonType("Änderungen speichern und schließen", ButtonBar.ButtonData.OTHER);
    public static final ButtonType SAVE_AND_CONTINUE = new ButtonType("Änderungen speichern und weiter", ButtonBar.ButtonData.OTHER);
    public static final ButtonType DISCARD = new ButtonType("Änderungen verwerfen", ButtonBar.ButtonData.OTHER);
}

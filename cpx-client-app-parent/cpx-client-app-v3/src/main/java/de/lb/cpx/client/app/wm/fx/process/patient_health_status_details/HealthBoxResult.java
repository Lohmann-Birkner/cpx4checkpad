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
package de.lb.cpx.client.app.wm.fx.process.patient_health_status_details;

import de.lb.cpx.patient.status.organ.HumanPartGraphic;
import java.util.Map;
import javafx.scene.layout.VBox;

/**
 *
 * @author niemeier
 */
public class HealthBoxResult {

    public final VBox healthBox;
    public final Map<Integer, HumanPartGraphic> humanPartList;

    public HealthBoxResult(final VBox pHealthBox, final Map<Integer, HumanPartGraphic> pHumanPartList) {
        healthBox = pHealthBox;
        humanPartList = pHumanPartList;
    }
}

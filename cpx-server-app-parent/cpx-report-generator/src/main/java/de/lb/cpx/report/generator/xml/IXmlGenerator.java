/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.report.generator.xml;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import javax.ejb.Remote;

/**
 *
 * @author nandola
 */
@Remote
public interface IXmlGenerator {

    String generateCaseDataXML(TCase case_obj, GDRGModel act_grouper_model);
}

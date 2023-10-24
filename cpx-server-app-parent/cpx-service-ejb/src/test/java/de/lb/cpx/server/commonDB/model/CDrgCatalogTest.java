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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commonDB.model.TestUtils.TestCatalogBuilder;
import de.lb.cpx.server.commons.dao.AbstractDrgmCatalogEntity;
import de.lb.cpx.service.information.CatalogTypeEn;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author gerschmann
 */
@SuppressWarnings("unchecked")
public class CDrgCatalogTest {

    private static final Logger LOG = Logger.getLogger(CDrgCatalogTest.class.getName());

    /**
     * Test of get2DrgmMapping method, of class CDrgCatalog.
     */
    @Test
    public void testDrgGet2DrgmMapping() {
        TestCatalogBuilder testBuilder = TestCatalogBuilder.getInstance();
        LOG.log(Level.INFO, "get2DrgmMapping for DRG");
        Map<String, AbstractDrgmCatalogEntity> cat = (Map<String, AbstractDrgmCatalogEntity>) testBuilder.getCatalog(CatalogTypeEn.DRG);

        assertTrue(testBuilder.checkResults(cat));
    }

    /**
     * Test of get2DrgmMapping method, of class CDrgCatalog.
     */
    @Test
    public void testDrgSupplGet2DrgmMapping() {
        TestCatalogBuilder testBuilder = TestCatalogBuilder.getInstance();
        LOG.log(Level.INFO, "get2DrgmMapping for ZE");
        Map<String, AbstractDrgmCatalogEntity> cat = (Map<String, AbstractDrgmCatalogEntity>) testBuilder.getCatalog(CatalogTypeEn.ZE);

        assertTrue(testBuilder.checkResults(cat));
    }

    /**
     * Test of get2DrgmMapping method, of class CDrgCatalog.
     */
    @Test
    public void testPeppSupplGet2DrgmMapping() {
        TestCatalogBuilder testBuilder = TestCatalogBuilder.getInstance();
        LOG.log(Level.INFO, "get2DrgmMapping for ZP");
        Map<String, AbstractDrgmCatalogEntity> cat = (Map<String, AbstractDrgmCatalogEntity>) testBuilder.getCatalog(CatalogTypeEn.ZP);

        assertTrue(testBuilder.checkResults(cat));
    }

    /**
     * Test of get2DrgmMapping method, of class CDrgCatalog.
     */
    @Test
    public void testETGet2DrgmMapping() {
        TestCatalogBuilder testBuilder = TestCatalogBuilder.getInstance();
        LOG.log(Level.INFO, "get2DrgmMapping for ET");
        Map<String, AbstractDrgmCatalogEntity> cat = (Map<String, AbstractDrgmCatalogEntity>) testBuilder.getCatalog(CatalogTypeEn.ET);

        assertTrue(testBuilder.checkResults(cat));
    }

    /**
     * Test of get2DrgmMapping method, of class CPeppCatalog.
     */
    @Test
    public void testPeppGet2DrgmMapping() {
        TestCatalogBuilder testBuilder = TestCatalogBuilder.getInstance();
        LOG.log(Level.INFO, "get2DrgmMapping for Pepp old Structure");
        Map<String, Set<CPeppCatalog>> cat = (Map<String, Set<CPeppCatalog>>) testBuilder.getCatalog(CatalogTypeEn.PEPP);
        assertTrue(testBuilder.checkPeppResults(cat));
        LOG.log(Level.INFO, "get2DrgmMapping for Pepp new Structure");
        cat = (Map<String, Set<CPeppCatalog>>) testBuilder.getCatalog(CatalogTypeEn.PEPP, false);
        assertTrue(testBuilder.checkPeppResults(cat));
    }

}

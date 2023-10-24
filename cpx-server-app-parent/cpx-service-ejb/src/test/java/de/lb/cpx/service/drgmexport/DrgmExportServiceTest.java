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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.drgmexport;

import de.lb.cpx.server.commonDB.model.CPeppCatalog;
import de.lb.cpx.server.commonDB.model.TestUtils.TestCatalogBuilder;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import de.lb.cpx.service.information.CatalogTypeEn;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * tests creating of the pepp drgm files with old and new structure
 *
 * @author gerschmann
 */
public class DrgmExportServiceTest {

    private static final Logger LOG = Logger.getLogger(DrgmExportServiceTest.class.getName());

    TestCatalogBuilder testCatalogBuilder = TestCatalogBuilder.getInstance();
    Method saveDrgmPeppFile = null;
    Method saveAnyDrgmFile = null;

    public DrgmExportServiceTest() {
    }

    @Before
    public void setUp() {
        try {
            Class<?>[] args = new Class<?>[2];
            args[0] = Writer.class;
            args[1] = List.class;
            saveDrgmPeppFile = DrgmExportService.class.getDeclaredMethod("saveDrgmPeppFile", args);
            saveDrgmPeppFile.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            fail("error on reflect method saveDrgmPeppFile");
        }

        try {
            Class<?>[] args = new Class<?>[2];
            args[0] = Writer.class;
            args[1] = List.class;
            saveAnyDrgmFile = DrgmExportService.class.getDeclaredMethod("saveAnyDrgmFile", args);
            saveAnyDrgmFile.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            fail("error on reflect method saveAnyDrgmFile");
        }
    }

    /**
     * Test of saveAnyDrgmFile method, of class DrgmExportService.
     */
    @Test
    public void testSaveDrgmDrgFile() {
        LOG.log(Level.INFO, "saveAnyDrgmFile ");
        Map<String, AbstractCatalogEntity> cat = (Map<String, AbstractCatalogEntity>) testCatalogBuilder.getCatalog(CatalogTypeEn.DRG);
        List<AbstractCatalogEntity> pResult = testCatalogBuilder.getCatalogAllEntities(cat);
        DrgmExportService instance = new DrgmExportService();
        StringWriter br = new StringWriter();
        Object[] params = new Object[2];
        params[0] = br;
        params[1] = pResult;
        try {
            Object o = saveAnyDrgmFile.invoke(instance, params);
            assertTrue((boolean) o);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOG.log(Level.SEVERE, null, e);
            fail("error on usage on method saveDrgmDrgFile is not accessible");
        }

        // compare read lines with generated ones
        assertTrue(testCatalogBuilder.CheckDrgmResults(cat.keySet(), br));
    }

    /**
     * Test of saveDrgmPeppFile method, of class DrgmExportService.
     */
    @Test
    public void testSaveDrgmPeppFile() {
        LOG.log(Level.INFO, "saveDrgmPeppFile old structure");
        Map<String, Set<CPeppCatalog>> cat = (Map<String, Set<CPeppCatalog>>) testCatalogBuilder.getCatalog(CatalogTypeEn.PEPP);
        List<CPeppCatalog> pResult = testCatalogBuilder.getPeppCatalogAllEntities(cat);
        DrgmExportService instance = new DrgmExportService();
        StringWriter br = new StringWriter();
        Object[] params = new Object[2];
        params[0] = br;
        params[1] = pResult;
        try {
            Object o = saveDrgmPeppFile.invoke(instance, params);
            assertTrue((boolean) o);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

            fail("error on usage on method saveDrgmPeppFile is not accessible");
        }

        // compare read lines with generated ones
        assertTrue(testCatalogBuilder.CheckPeppDrgmResults(cat, br));
    }

    /**
     * Test of saveDrgmPeppFile method, of class DrgmExportService.
     */
    @Test
    public void testSaveDrgmPeppFileNew() {
        LOG.log(Level.INFO, "saveDrgmPeppFile new structure");
        Map<String, Set<CPeppCatalog>> cat = (Map<String, Set<CPeppCatalog>>) testCatalogBuilder.getCatalog(CatalogTypeEn.PEPP, false);
        List<CPeppCatalog> pResult = testCatalogBuilder.getPeppCatalogAllEntities(cat);
        DrgmExportService instance = new DrgmExportService();
        StringWriter br = new StringWriter();
        Object[] params = new Object[2];
        params[0] = br;
        params[1] = pResult;
        try {
            Object o = saveDrgmPeppFile.invoke(instance, params);
            assertTrue((boolean) o);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

            fail("error on usage on method saveDrgmPeppFile is not accessible");
        }
        // compare read lines with generated ones
        assertTrue(testCatalogBuilder.CheckPeppDrgmResults(cat, br));
    }

}

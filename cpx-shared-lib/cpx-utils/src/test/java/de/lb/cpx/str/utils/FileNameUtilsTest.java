/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.str.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author wilde
 */
public class FileNameUtilsTest {
    private static final String FILE_NAME_1 = "test";
    private static final String FILE_NAME_2 = "test - Kopie";
    private static final String FILE_NAME_3 = "test - Kopie(2)";
    private static final String FILE_NAME_4 = "test - Kopie - Kopie";
    private static final String FILE_NAME_5 = "test - Kopie - Kopie(2)";
    private static final String FILE_NAME_6 = "test - Kopie(2) - Kopie";
    
    @Test
    public void testGetOriginalName(){
        
        Assert.assertEquals("test",FileNameUtils.getOriginalName(FILE_NAME_1));
        Assert.assertEquals("test",FileNameUtils.getOriginalName(FILE_NAME_2));
        Assert.assertEquals("test",FileNameUtils.getOriginalName(FILE_NAME_3));
        Assert.assertEquals("test - Kopie",FileNameUtils.getOriginalName(FILE_NAME_4));
        Assert.assertEquals("test - Kopie",FileNameUtils.getOriginalName(FILE_NAME_5));
        Assert.assertEquals("test - Kopie(2)",FileNameUtils.getOriginalName(FILE_NAME_6));
    }
    @Test
    public void testGetCopyName(){
        
        Assert.assertEquals("test - Kopie",FileNameUtils.getCopyName(FILE_NAME_1,1));
        Assert.assertEquals("test - Kopie - Kopie",FileNameUtils.getCopyName(FILE_NAME_2,1));
        Assert.assertEquals("test - Kopie(2) - Kopie(3)",FileNameUtils.getCopyName(FILE_NAME_3,3));
        Assert.assertEquals("test - Kopie - Kopie - Kopie(2)",FileNameUtils.getCopyName(FILE_NAME_4,2));
        Assert.assertEquals("test - Kopie - Kopie(2) - Kopie",FileNameUtils.getCopyName(FILE_NAME_5,1));
        Assert.assertEquals("test - Kopie(2) - Kopie - Kopie(2)",FileNameUtils.getCopyName(FILE_NAME_6,2));
    }
}

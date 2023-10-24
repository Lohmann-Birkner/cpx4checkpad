/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.textfield;

import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 * @author gerschmann
 */
public class CurrencyTextFieldTest {

    private static final Logger LOG = Logger.getLogger(CurrencyTextFieldTest.class.getName());
    
    public CurrencyTextFieldTest() {
    }

    @Test
    public void testSomeMethod() {
        String intFormat = "(([0-9]{1,2})?)[%]{1}?$";
        Pattern intPat = Pattern.compile(intFormat);
         String newText = "23%";
         assertTrue(newText, intPat.matcher(newText).matches());
       
         intFormat = "([0-9]*)[%]{1}?$";
         intPat = Pattern.compile(intFormat);
         newText = "023%";
         
         assertTrue(newText, intPat.matcher(newText).matches());
       
        String formatStr = "^-?([0-9]+(\\,[0-9]{1,2})?)[€]{1}?$";
        Pattern pattern = Pattern.compile(String.format(formatStr));
        newText = "0,23€";
         assertTrue(newText, pattern.matcher(newText).matches());
         LOG.log(Level.INFO, newText + " OK");
          newText = "27,23€";
         assertTrue(newText, pattern.matcher(newText).matches());
        LOG.log(Level.INFO, newText + " OK");
          newText = "27,235€";
         assertFalse(newText, pattern.matcher(newText).matches());
        LOG.log(Level.INFO, newText + " not OK");
          newText = "27,23a€";
         assertFalse(newText, pattern.matcher(newText).matches());
        LOG.log(Level.INFO, newText + " not OK");
          newText = "27,23";
         assertFalse(newText, pattern.matcher(newText).matches());
        LOG.log(Level.INFO, newText + " not OK");
         newText = "27,,23€";
         assertFalse(newText, pattern.matcher(newText).matches());
        LOG.log(Level.INFO, newText + " not OK");
         newText = "27a,23€";
        assertFalse(newText, pattern.matcher(newText).matches());
        LOG.log(Level.INFO, newText + " not OK");
    }
    
    @Test
    public void testPrecision(){
        Double pDouble = 3532.50 * 2.546;
        LOG.log(Level.INFO,"" +  pDouble);
        double res = Lang.round(pDouble, 2);
        assertTrue(res == 8993.75) ;
    }
    
}

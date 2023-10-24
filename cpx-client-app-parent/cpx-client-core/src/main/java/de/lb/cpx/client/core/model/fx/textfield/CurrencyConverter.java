/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.textfield;

//import de.lb.cpx.shared.lang.Lang;
//import java.text.NumberFormat;
//import java.text.ParseException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author gerschmann
// */
//public class CurrencyConverter extends DoubleConverter {
//
//    private static final Logger LOG = Logger.getLogger(CurrencyConverter.class.getName());
//
//    private final String currencySign; //most likely € (depends on de.properties/en.properties)
//
//    public CurrencyConverter(String pCurrencySign) {
//        super();
//        currencySign = pCurrencySign;
//    }
//
//    @Override
//    public String toString(Double pDouble) {
//        //String retStr = super.toString(pDouble);
//        //return retStr + currencySign;
//        //return String.format("%10.2f", pDouble) + Lang.getCurrencySymbol();
//        NumberFormat formatter = NumberFormat.getCurrencyInstance();
//        return formatter.format(pDouble) + Lang.getCurrencySymbol();
//    }
//
//    @Override
//    public Double fromString(String pString) {
//        NumberFormat formatter = NumberFormat.getCurrencyInstance();
//        pString = pString.replace(Lang.getCurrencySymbol(), "");
//        Number val;
//        try {
//            val = formatter.parse(pString);
//            return val == null ? null : val.doubleValue();
//        } catch (ParseException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//            return null;
//        }
////        if (pString != null && pString.endsWith(currencySign)) {
////            return super.fromString(pString.substring(0, pString.length() - 1));
////        } else {
////            return super.fromString(pString);
////        }
//    }
//
//}

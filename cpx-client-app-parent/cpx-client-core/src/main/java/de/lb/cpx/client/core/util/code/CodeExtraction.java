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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util.code;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to extract a list of icd and ops codes from a free form text.
 *
 * @author wilde
 */
public class CodeExtraction {

    private static final Logger LOG = Logger.getLogger(CodeExtraction.class.getName());

    //ICD code with reference type
    private static final Pattern ICD_PATTERN = Pattern.compile("([A-Z?*][0-9?*]{1,2}((((\\.([0-9?*]{1,2}?(\\*|\\+|!|-)?))|(\\.-))))?(\\*|\\+|!)?)");
    //OPS code with optional part
    private static final Pattern OPS_PATTERN = Pattern.compile("([0-9]-[0-9]{2}[a-z|0-9]?(\\.[a-z|0-9][a-z|0-9]?)?)");
    //OPS code without optional part
    //final private Pattern opsPattern = Pattern.compile("([0-9]-[0-9]{2}[a-z|0-9]?)");

    /**
     * Just for testing and demonstration
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String text = "This is just a shitty example with an icd code A08.-, G12.9 and J53.83. Illegal examples are k20.5 or L3.5.\r\n"
                + "And we have also three procedure examples with 1-247, 1-265.9, 1-26d.9 and 5-784.1g! Illegal examples here are 5-4f3.4 or 1275.9\r\nNow let's have at look at the results...";

        final String[] icdCodes = getIcdCodes(text);
        final String[] opsCodes = getOpsCodes(text);

        LOG.info(MessageFormat.format("Text to extract codes from:\r\n{0}", text));
        LOG.info(MessageFormat.format("Found {0} ICD Codes: {1} ", icdCodes.length, Arrays.toString(icdCodes)));
        LOG.info(MessageFormat.format("Found {0} OPS Codes: {1} ", opsCodes.length, Arrays.toString(opsCodes)));
        LOG.info(MessageFormat.format("First ICD Code is {0} ", getIcdCode(text)));
        LOG.info(MessageFormat.format("First OPS Code is {0} ", getOpsCode(text)));
    }

    /**
     * Extracts first icd code from given text
     *
     * @param pText text to extract icd code from
     * @return first icd from text (returns empty string if no icd code was
     * found)
     */
    public static String getIcdCode(final String pText) {
        final String[] icdCodes = getIcdCodes(pText);
        if (icdCodes.length == 0) {
            return "";
        } else {
            return icdCodes[0];
        }
    }

    /**
     * Extracts icd codes from given text
     *
     * @param pText text to extract icd codes from
     * @return list of detected icd code string (returns empty array if no icd
     * code was found)
     */
    public static String[] getIcdCodes(final String pText) {
        return getCodes(ICD_PATTERN, pText);
    }

    /**
     * Extracts icd codes from given text
     *
     * @param pText text to extract icd codes from
     * @return matcher for iteration
     */
    public static Matcher getIcdCodeMatcher(final String pText) {
        return getCodeMatcher(ICD_PATTERN, pText);
    }

    /**
     * Extracts first ops code from given text
     *
     * @param pText text to extract ops code from
     * @return first ops from text (returns empty string if no ops code was
     * found)
     */
    public static String getOpsCode(final String pText) {
        final String[] opsCodes = getOpsCodes(pText);
        if (opsCodes.length == 0) {
            return "";
        } else {
            return opsCodes[0];
        }
    }

    /**
     * Extracts ops codes from given text
     *
     * @param pText text to extract ops codes from
     * @return list of detected ops code string (returns empty array if no ops
     * code was found)
     */
    public static String[] getOpsCodes(final String pText) {
        return getCodes(OPS_PATTERN, pText);
    }

    /**
     * Extracts ops codes from given text
     *
     * @param pText text to extract ops codes from
     * @return matcher for iteration
     */
    public static Matcher getOpsCodeMatcher(final String pText) {
        return getCodeMatcher(OPS_PATTERN, pText);
    }

    /**
     * Extracts codes from given text
     *
     * @param pPattern regex pattern to detect codes
     * @param pText text to extract codes from
     * @return list of detected code string (returns empty array if nothing was
     * found)
     */
    protected static Matcher getCodeMatcher(final Pattern pPattern, final String pText) {
        final String text = pText == null ? "" : pText;
        if (text.isEmpty() || pPattern == null) {
            return null;
        }
        final Matcher matcher = pPattern.matcher(text);
        return matcher;
//        final List<Matcher> result = new ArrayList<>();
//        while (matcher.find()) {
//            result.add(matcher);
//        }
//        final Matcher[] tmp = new Matcher[result.size()];
//        result.toArray(tmp);
//        return tmp;
    }

    /**
     * Extracts codes from given text
     *
     * @param pPattern regex pattern to detect codes
     * @param pText text to extract codes from
     * @return list of detected code string (returns empty array if nothing was
     * found)
     */
    protected static String[] getCodes(final Pattern pPattern, final String pText) {
//        final String text = pText == null ? "" : pText.trim();
//        if (text.isEmpty() || pPattern == null) {
//            return new String[0];
//        }
        final Matcher matcher = getCodeMatcher(pPattern, pText);
        if (matcher == null) {
            return new String[0];
        }
        final List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group().trim());
        }
        final String[] tmp = new String[result.size()];
        result.toArray(tmp);
        return tmp;
    }

}

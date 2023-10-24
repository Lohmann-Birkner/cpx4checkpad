/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import de.lb.cpx.model.TCaseDetails;
import org.apache.commons.lang3.StringUtils;

/**
 * Helper to handle comment in TCaseDetails -seperates meta data from user input
 * -helps formating the comment In Furture release mayne not necessary - meta
 * data of comment should be seperated from user comment AWI:20170405
 *
 * @author wilde
 */
public class CaseDetailsCommentHelper {
    //seperator for the meta data and user text in the comment of an case details

    private static final String SEPERATOR = "////";

    /**
     * get the static meta data part of the comment
     * ParentVersion/creationUser/date of creation
     *
     * @param pDetails details to get comment from
     * @return meta data text
     */
    public static String getMetaData(TCaseDetails pDetails) {
        return splitDetailsComment(pDetails, 0);
    }

    /**
     * splits details comment
     *
     * @param pDetails details to get comment from
     * @param pIndex index of meta data (starts with 0)
     * @return meta data text (can return null)
     */
    public static String splitDetailsComment(TCaseDetails pDetails, final int pIndex) {
        final String comment = StringUtils.trimToNull(pDetails.getComment());
        if (pIndex < 0 || comment == null || !comment.contains(SEPERATOR)) {
            return null;
        }
        String[] tmp = comment.split(SEPERATOR);
        if (tmp.length > pIndex) {
            return tmp[pIndex].trim();
        }
        return null;
    }

    /**
     * get the comment text by the user from the comment
     *
     * @param pDetails details to get comment from
     * @return comment text (user input)
     *
     */
    public static String getCommentTxt(TCaseDetails pDetails) {
        final String comment = splitDetailsComment(pDetails, 1);
        if (comment == null) {
            //fallback return nothing
            return "";//StringUtils.trimToEmpty(pDetails.getComment());
        } else {
            return comment;
        }
    }

    /**
     * replace user comment in comment in the case details
     *
     * @param pDetails case details to replace comment
     * @param pNewComment comment to set
     * @return updated case details
     */
    public static TCaseDetails replaceUserComment(TCaseDetails pDetails, String pNewComment) {
        //save meta data
        String metaData = StringUtils.trimToEmpty(getMetaData(pDetails));
        String newComment = StringUtils.trimToEmpty(pNewComment);
        if (metaData.isEmpty() || !newComment.contains(metaData)) {
            pDetails.setComment(metaData + SEPERATOR + newComment);
        }
        return pDetails;
    }

    /**
     * format user comment in case details to have no line breaks only for
     * displaying, yet no reverse methode is implemented (20170405)
     *
     * @param pDetails details to fetch comment from
     * @return formated string
     */
    public static String formatUserCommentNoLineBreak(TCaseDetails pDetails) {
        String comment = StringUtils.trimToNull(pDetails.getComment());
        if (comment == null) {
            return "";
        }
        return getCommentTxt(pDetails).replaceAll("\\r\\n|\\r|\\n", " \\ ");
    }

    /**
     * format comment to replace seperator with line break only for displaying,
     * yet no reverse methode is implemented (20170405)
     *
     * @param pDetails case details to fetch comment from
     * @return formated string
     */
    public static String formatCommentNoSeperator(TCaseDetails pDetails) {
        final String comment = StringUtils.trimToNull(pDetails.getComment());
        if (comment == null) {
            return "";
        }
        return comment.replace(SEPERATOR, "\n");
    }

    /**
     * format comment to replace separtor with linebreak and remove all others
     * only for displaying, yet no reverse methode is implemented (20170405)
     *
     * @param pDetails details to fetch comment from
     * @return formated comment
     */
    public static String formatCommentNoSeperatorNoLineBreak(TCaseDetails pDetails) {
        final String comment = StringUtils.trimToNull(pDetails.getComment());
        if (comment == null) {
            return "";
        }
        return comment.replaceAll("\\r\\n|\\r|\\n", " \\ ").replace(SEPERATOR, "\n");
    }
}

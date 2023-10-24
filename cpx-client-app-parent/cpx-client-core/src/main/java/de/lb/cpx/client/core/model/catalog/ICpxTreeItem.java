/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Dirk Niemeier
 * @param <T2> thesaurus type
 */
public interface ICpxTreeItem<T2 extends ICpxThesaurus> extends Serializable {

    String getCode();

    String getDescription();

    String getInclusion();

    String getExclusion();

    String getNote();

    int getDepth();

    String getSearchableDescription();

    void setSearchableDescription(String description);

    void setVisible(final boolean pVisible);

    void setVisible(final boolean pVisible, final boolean pVisibleParent, final boolean pVisibleChildren);

    boolean isVisible();

    List<ICpxTreeItem<T2>> getLastChildren();

    boolean like(final List<List<String>> pExprList, final List<List<Integer>> pFixedExprIndexes);

    ICpxTreeItem<T2> getRootParent();

    ICpxTreeItem<T2> setParentId(final Long pParentId);

    void setParent(final ICpxTreeItem<T2> pParent);

    long getParentId();

    ICpxTreeItem<T2> getParent();

    void addChildren(final ICpxTreeItem<T2> pCpxObj);

    List<ICpxTreeItem<T2>> getChildren();

    boolean isCompleteFl();

    String getThesaurusString();

    void addThesaurus(T2 thesaurus);

    List<T2> getThesaurus();

    public static boolean like(final String pValue,
            final List<List<String>> pExprList, final List<List<Integer>> pFixedExprIndexes) {
        return like(pValue, pExprList, pFixedExprIndexes, false);
    }

    /*
  public static String removeChars(String pValue, String[] chars) {
    String value = (pValue == null)?"":pValue.toLowerCase().trim();
    if (chars != null && chars.length > 0) {
      for(int i = 0; i < chars.length; i++) {
        value = value.replace(chars[i], "");
      }
    }
    return value;
  }
     */
    public static String replaceChars(String pValue, String[][] chars) {
        return StringUtils.replaceEach(pValue, chars[0], chars[1]);

    }

    public static boolean like(final String pValue,
            final List<List<String>> pExprList, final List<List<Integer>> pFixedExprIndexes,
            final boolean pIgnoreFormattingChars) {
        //visible = false;
        //String expr = (pExpr == null) ? "" : pExpr.toLowerCase().trim();
        //String value = (pValue == null) ? "" : pValue.toLowerCase().trim();
        String value = (pValue == null) ? "" : pValue.trim();
        String oValue = (pValue == null) ? "" : pValue;

//        String[][] replaceChars = new String[][]{{"ä", "ö", "ü", "ß"}, {"ae", "oe", "ue", "ss"}};
//        expr = replaceChars(expr, replaceChars);
//        value = replaceChars(value, replaceChars);
//        if (pIgnoreFormattingChars) {
        //          String[][] chars = new String[][]{{".", ",", "-", "/"}, {"", "", "", ""}};
        String[][] chars = new String[][]{{".", "-", "/"}, {"", "", ""}};

        value = replaceChars(value, chars);
        /*String[] chars = new String[] { ".", ",", "-", "/" };
      expr = removeChars(expr, chars);
      value = removeChars(value, chars);*/
        //      }

        //String expr = expr.toLowerCase(); // ignoring locale for now
        /*
    expr = expr.replace(".", "\\."); // "\\" is escaped to "\" (thanks, Alan M)
    // ... escape any other potentially problematic characters here
    expr = expr.replace("?", ".");
    expr = expr.replace("%", ".*");
    if (value.matches("(.*)" + expr + "(.*)")) {
      return true;
    }
    return false;
         */
        //return value.indexOf(expr) > -1;
        //return value.contains(expr);
        //return value.toLowerCase().contains(expr);
        return contains(value, oValue, pExprList, pFixedExprIndexes);
    }

    /**
     * Search with (AND , OR , "") pattern
     *
     * @param pValue Search String with lowecase
     * @param oValue Search String without lowercase
     * @param pExprList expression list
     * @param pFixedExprIndexes fixed expression indexes
     * @return boolean True if match
     */
    public static boolean contains(String pValue, String oValue,
            final List<List<String>> pExprList, final List<List<Integer>> pFixedExprIndexes) {
        for (int i = 0; i < pExprList.size(); i++) {
            boolean match = true;
            for (int j = 0; j < pExprList.get(i).size(); j++) {
                if (!pFixedExprIndexes.isEmpty()) {
                    List<Integer> tempList = new ArrayList<>();
                    tempList.add(i);
                    tempList.add(j);
                    int fixedListIndex = pFixedExprIndexes.indexOf(tempList);
                    if (fixedListIndex != -1) {
                        if (pFixedExprIndexes.get(fixedListIndex).get(0) == i && pFixedExprIndexes.get(fixedListIndex).get(1) == j) {
                            if (!oValue.contains(pExprList.get(i).get(j))) {
                                match = false;
                            }
//                            else {
//                                CatalogDialogFXMLController.fixedExprIndexes.remove(0);
//                            }
                        }
                    } else {
                        //if (!pValue.contains(CatalogDialogFXMLController.nExprList.get(i).get(j).trim())) {
                        if (!containsIgnoreCase(pValue, pExprList.get(i).get(j).trim())) {
                            match = false;
                        }
                    }
                } else {
                    //  if (!pValue.contains(CatalogDialogFXMLController.nExprList.get(i).get(j).trim())) {
                    if (!containsIgnoreCase(pValue, pExprList.get(i).get(j).trim())) {
                        match = false;
                    }
                }
            }
            if (match == true) {

                return match;
            }
        }
        return false;
    }

    /**
     * Fastest way to performance contains is regionMatches. For performance
     * benchmark und details look at
     * https://stackoverflow.com/questions/86780/how-to-check-if-a-string-contains-another-string-in-a-case-insensitive-manner-in#25379180
     *
     * @param src value
     * @param what expression
     * @return value contains expression?
     */
    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0) {
            return true; // Empty string is contained
        }
        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp) {
                continue;
            }

            if (src.regionMatches(true, i, what, 0, length)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param pCpxItem cpx item
     * @param pExprList expression list
     * @param pFixedExprIndexes fixed expression indexes
     * @return match true if match was found
     */
    public static boolean like(final ICpxTreeItem<? extends ICpxThesaurus> pCpxItem,
            final List<List<String>> pExprList, final List<List<Integer>> pFixedExprIndexes) {
        if (pCpxItem == null) {
            return false;
        }
        if (pCpxItem.isVisible()) {
            return true;
        }

//        boolean match = ICpxTreeItem.like(pExpr, pCpxItem.getCode(), true)
//                || ICpxTreeItem.like(pExpr, getChilderensDescription(pCpxItem)+ " " + pCpxItem.getThesaurusString())
//                ;
//        boolean match = ICpxTreeItem.like(pExpr, pCpxItem.getCode(), true)
//                || ICpxTreeItem.like(pExpr, pCpxItem.getDescription() + " " + pCpxItem.getThesaurusString());
//// boolean match = ICpxTreeItem.like(pExpr, pCpxItem.getSearchableDescription() + " " + pCpxItem.getThesaurusString());
        boolean match = false;

        if (pCpxItem.getChildren().isEmpty()) {
            match = ICpxTreeItem.like(pCpxItem.getSearchableDescription() + " " + pCpxItem.getThesaurusString(),
                    pExprList, pFixedExprIndexes);

            if (match) {
                // show the childeren from item when found

                if (pCpxItem.getChildren() != null && !pCpxItem.getChildren().isEmpty()) {
                    ICpxTreeItem.setVisibleStatic(pCpxItem, true, true, true);

                } else {
                    ICpxTreeItem.setVisibleStatic(pCpxItem, true, true, false);
                }
            } else {
////match = like(pCpxItem.getParent(), pExpr);
                match = like(pCpxItem.getParent(), pExprList, pFixedExprIndexes);

            }
        }
        return match;
    }

    public static void setVisibleStatic(final ICpxTreeItem<? extends ICpxThesaurus> pCpxItem, final boolean pVisible, final boolean pVisibleParent, final boolean pVisibleChildren) {
        //visible = pVisible;
        if (pVisibleParent) {
            makeParentVisible(pCpxItem.getParent(), pVisible);
        }
        if (pVisibleChildren) {
            for (ICpxTreeItem<? extends ICpxThesaurus> pCpxChildren : pCpxItem.getChildren()) {
                makeChildrenVisible(pCpxChildren, pVisible);
            }
        }
        pCpxItem.setVisible(pVisible);
    }

    static void makeParentVisible(final ICpxTreeItem<? extends ICpxThesaurus> pCpxItem, final boolean pVisible) {
        if (pCpxItem == null) {
            return;
        }
        if (pVisible == pCpxItem.isVisible()) {
            //Nothing to do
            return;
        }
        setVisibleStatic(pCpxItem, pVisible, false, false);
        makeParentVisible(pCpxItem.getParent(), pVisible);
    }

    static void makeChildrenVisible(final ICpxTreeItem<? extends ICpxThesaurus> pCpxItem, final boolean pVisible) {
        if (pCpxItem == null) {
            return;
        }
        if (pVisible == pCpxItem.isVisible()) {
            //Nothing to do
            return;
        }
        setVisibleStatic(pCpxItem, pVisible, false, false);
        for (ICpxTreeItem<? extends ICpxThesaurus> pCpxChildren : pCpxItem.getChildren()) {
            makeChildrenVisible(pCpxChildren, pVisible);
        }
        //makeParentVisible(pCpxIcd.getParent(), pVisible);
    }

}

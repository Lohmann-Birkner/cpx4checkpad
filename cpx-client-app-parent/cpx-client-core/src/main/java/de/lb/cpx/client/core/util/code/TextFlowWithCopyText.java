/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.util.code;

import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.client.core.util.shortcut.ShortcutHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Pattern;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

/**
 *
 * @author gerschmann
 */
public class TextFlowWithCopyText extends TextFlow implements ShortcutHandler {

private static final String[][] sChars = new String[][]{{".", "-", "/"}, {"", "", ""}};
private final ObjectProperty<Controller<CpxScene> > controller = new SimpleObjectProperty<>();

    public TextFlowWithCopyText(){
        super();
    }
    
    public TextFlowWithCopyText(final String str, final List<List<String>> nExprList, final List<List<Integer>> fixedExprIndexes){
        this();
        getColoredTextFlow(str, nExprList, fixedExprIndexes);
    }
    
    @Override
    public boolean shortcutF1Help(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutF2New(KeyEvent pEvent) {
        return false;
   }

    @Override
    public boolean shortcutF3Find(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutF4Close(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutF5Refresh(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutEnterExecute(KeyEvent pEvent) {
         return false;
   }

    @Override
    public boolean shortcutControlPPrint(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutControlFFind(KeyEvent pEvent) {
         return false;
    }

    @Override
    public boolean shortcutControlWClose(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutControlNNew(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutControlRRefresh(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutControlSSave(KeyEvent pEvent) {
         return false;
    }

    @Override
    public boolean shortcutAltLeftBack(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutAltRightForward(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutControlCCopy(KeyEvent pEvent) {
         StringBuilder copyText = new StringBuilder();
         ObservableList<Node> nodes = getChildren();
         for(Node node: nodes){
             if(node instanceof Text){
                 copyText.append(((Text) node).getText());
             }
             if(node instanceof Hyperlink){
                 copyText.append(((Hyperlink) node).getText());
             }
         }
        
          ClipboardEnabler.copyToClipboard(null, copyText.toString(), copyText.toString().length() > 0);
         return copyText.toString().length() > 0;
    }

    @Override
    public boolean shortcutControlVPaste(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutDelRemove(KeyEvent pEvent) {
        return false;
    }

    @Override
    public boolean shortcutShiftDelRemove(KeyEvent pEvent) {
        return false;
    }
    
    private void getColoredTextFlow(String str, List<List<String>> nExprList, List<List<Integer>> fixedExprIndexes) {
        String oStr = str;

        ConcurrentSkipListMap<Integer, String> pointsList = new ConcurrentSkipListMap<>();
        pointsList.clear();
        if (str != null && !str.isEmpty()) {
            if (!nExprList.isEmpty()) {
                final String startTag = "[[StartTag@";
                final String endTag = "@EndTag]]";
                // Get all special characters indexes in the text and save it in the points List
                for (int i = 0; i < sChars[0].length; i++) {
                    int index = oStr.indexOf(sChars[0][i]);
                    while (index >= 0) {
                        pointsList.put(index, sChars[0][i]);
                        index = oStr.indexOf(sChars[0][i], index + 1);
                    }
                }
                //remove all the special characters from the text
                str = replaceChars(str, sChars);

                boolean firstLoopDone = false;
                //remove the duplicate words from the search text
                for (int i = 0; i < nExprList.size(); i++) {
                    for (int j = 0; j < nExprList.get(i).size(); j++) {
                        for (int k = 0; k < nExprList.size(); k++) {
                            for (int h = 0; h < nExprList.get(k).size(); h++) {
                                if (nExprList.get(i).get(j).contains(nExprList.get(k).get(h)) && nExprList.get(i).get(j).length() > nExprList.get(k).get(h).length() && nExprList.get(k).get(h).length() != 0) {
                                    nExprList.get(k).set(h, "");
                                }
                            }
                        }

                    }
                }

                //add start-Tag and End-Tag before and after the red words
                for (int i = 0; i < nExprList.size(); i++) {
                    for (int j = 0; j < nExprList.get(i).size(); j++) {
                        if (!"".equals(nExprList.get(i).get(j).trim())) {
                            String oSearchElement = replaceChars(nExprList.get(i).get(j), sChars);
                            String searchElement = replaceChars(nExprList.get(i).get(j).trim().toLowerCase(), sChars);
                            if (!firstLoopDone) {
                                if (!fixedExprIndexes.isEmpty()) {
                                    List<Integer> tempList = new ArrayList<>();
                                    tempList.add(i);
                                    tempList.add(j);
                                    int fixedListIndex = fixedExprIndexes.indexOf(tempList);
                                    if (fixedListIndex != -1) {
                                        if (fixedExprIndexes.get(fixedListIndex).get(0) == i && fixedExprIndexes.get(fixedListIndex).get(1) == j) {
                                            str = str.replaceAll(Pattern.quote(oSearchElement), startTag + oSearchElement + endTag);
                                        }
                                    } else {
                                        str = str.replaceAll("(?i)" + Pattern.quote(searchElement), startTag + searchElement + endTag);
                                    }
                                } else {
                                    str = str.replaceAll("(?i)" + Pattern.quote(searchElement), startTag + searchElement + endTag);

                                }

                                firstLoopDone = true;
                            } else {
                                String[] str2 = str.split("\\[\\[StartTag@|@EndTag\\]\\]");
                                boolean isRed = false;

                                for (int k = 0; k < str2.length; k++) {
                                    if (isRed) {
                                        str2[k] = startTag + str2[k] + endTag;
                                    } else {

                                        if (!fixedExprIndexes.isEmpty()) {
                                            List<Integer> tempList = new ArrayList<>();
                                            tempList.add(i);
                                            tempList.add(j);
                                            int fixedListIndex = fixedExprIndexes.indexOf(tempList);
                                            if (fixedListIndex != -1) {
                                                if (fixedExprIndexes.get(fixedListIndex).get(0) == i && fixedExprIndexes.get(fixedListIndex).get(1) == j) {
                                                    str2[k] = str2[k].replaceAll(Pattern.quote(oSearchElement), startTag + oSearchElement + endTag);
                                                }
                                            } else {
                                                str2[k] = str2[k].replaceAll("(?i)" + Pattern.quote(searchElement), startTag + searchElement + endTag);
                                            }
                                        } else {
                                            str2[k] = str2[k].replaceAll("(?i)" + Pattern.quote(searchElement), startTag + searchElement + endTag);
                                        }
                                    }
                                    isRed = !isRed;
                                }
                                str = String.join("", str2);
                            }
                        }
                    }
                }
                //split the text according to the tags (Start-Tag and the End-Tag) 
                while (str.contains(startTag) && str.contains(endTag) && !str.trim().isEmpty()) {
                    int startPos = str.indexOf(startTag);
                    int endPos = str.indexOf(endTag);

                    String substr1 = str.substring(0, startPos);

                    if (!pointsList.isEmpty() && !"".equals(substr1)) {
                        for (Map.Entry<Integer, String> entry : pointsList.entrySet()) {
                            Integer key = entry.getKey();

                            if (key > substr1.length()) {
                                pointsList.put(key - substr1.length(), pointsList.get(key));
                                pointsList.remove(key);
                            } else {
                                String teil1 = "";
                                String teil2 = "";

                                if (key > 0) {
                                    teil1 = substr1.substring(0, key);
                                }
                                if (key < substr1.length()) {
                                    teil2 = substr1.substring(key, substr1.length());
                                }
                                substr1 = teil1 + pointsList.get(key) + teil2;
                                pointsList.remove(key);
                            }
                        }
                    }
                    String substr2 = str.substring(startPos + startTag.length(), endPos);
                    //return the special characters to the text from the pointsList
                    if (!pointsList.isEmpty()) {
                        for (Map.Entry<Integer, String> entry : pointsList.entrySet()) {
                            Integer key = entry.getKey();
                            if (key > substr2.length()) {
                                pointsList.put(key - substr2.length(), pointsList.get(key));
                                pointsList.remove(key);
                            } else {
                                String teil1 = "";
                                String teil2 = "";
                                if (key > 0) {
                                    teil1 = substr2.substring(0, key);
                                }
                                if (key < substr2.length()) {
                                    teil2 = substr2.substring(key, substr2.length());
                                }
                                substr2 = teil1 + pointsList.get(key) + teil2;
                                pointsList.remove(key);
                            }
                        }
                    }

                    str = str.substring(endPos + endTag.length());
                    if (!substr1.isEmpty()) {
                        Text text1 = new Text(substr1);
                        text1.setStyle("-fx-wrap-text: true");
                        getChildren().addAll(text1);
                    }
                    if (oStr.toLowerCase().contains(substr2)) {
                        if ((substr1.isEmpty()) || (!substr1.isEmpty() && substr1.matches(".*[^A-Za-z0-9]$"))) {
                            substr2 = oStr.substring(oStr.toLowerCase().indexOf(substr2), oStr.toLowerCase().indexOf(substr2) + substr2.length());
                        }
                    }
                    Text text2 = new Text(substr2);
                    text2.setStyle("-fx-wrap-text: true ; -fx-font-weight: bold;");
                    text2.setFill(Color.RED);
                    getChildren().addAll(text2);
                }
            }
            if (!str.isEmpty()) {
                //return the special characters to the rest of the text from the pointsList
                if (!pointsList.isEmpty()) {
                    for (Map.Entry<Integer, String> entry : pointsList.entrySet()) {
                        Integer key = entry.getKey();
                        if (key > str.length()) {
                            pointsList.put(key - str.length(), pointsList.get(key));
                            pointsList.remove(key);
                        } else {
                            String teil1 = "";
                            String teil2 = "";
                            if (key > 0) {
                                teil1 = str.substring(0, key);
                            }
                            if (key < str.length()) {
                                teil2 = str.substring(key, str.length());
                            }
                            str = teil1 + pointsList.get(key) + teil2;
                            pointsList.remove(key);
                        }
                    }
                }

                Text text3 = new Text(str);
                text3.setStyle("-fx-wrap-text: true");
                getChildren().addAll(text3);
            }
        }
        setLineSpacing(3);
        
    }


    /**
     * remove the chars like {".", "-", "/"} and duplicate {" ", "&amp;&amp;",
     * ",,"}
     *
     * @param pValue value
     * @param chars characters
     * @return pValue after replacing the Characters
     */
    public static String replaceChars(String pValue, String[][] chars) {
        for (int i = 0; i < chars[0].length; i++) {
            while (pValue.contains(chars[0][i])) {
                pValue = pValue.replace(chars[0][i], chars[1][i]);
            }
        }
        return pValue;
    }

    public Controller<CpxScene> getRedirector() {
        return controller.get();
    }
    
    public void setRedirector(Controller<CpxScene>  pController){
        controller.set(pController);
    }

}

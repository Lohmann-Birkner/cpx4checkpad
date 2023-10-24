/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.easycoder;

import de.lb.cpx.client.core.model.fx.autocompletion.AutocompletionlTextField;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Text;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author halabieh
 */
public class SpellCheckManagerClass {

    private boolean tempDictionary = false;
    private static final Logger LOG = Logger.getLogger(EasyCoderDialogFXMLController.class.getName());

    private AutocompletionlTextField txtSearch;
    private final AutocompletionlTextField diagnosisTxtSearch;
    private final AutocompletionlTextField procedureTxtSearch;
    private AnalyzingInfixSuggester suggester;
    private final AnalyzingInfixSuggester icdSuggester;
    private final AnalyzingInfixSuggester opsSuggester;
    private final SpellChecker icdSpellChecker;
    private final SpellChecker opsSpellChecker;
    private SpellChecker spellChecker;
    private final String pathToTempIndex;
    private final String pathToTempDictionary;
    private final StandardAnalyzer analyzer;

    public SpellCheckManagerClass(AutocompletionlTextField diagnosisTxtSearch,
            AutocompletionlTextField procedureTxtSearch,
            AnalyzingInfixSuggester opsSuggester,
            AnalyzingInfixSuggester icdSuggester,
            SpellChecker opsSpellChecker,
            SpellChecker icdSpellChecker,
            String pathToTempIndex,
            String pathToTempDictionary
    ) {

        this.diagnosisTxtSearch = diagnosisTxtSearch;
        this.procedureTxtSearch = procedureTxtSearch;
        this.opsSuggester = opsSuggester;
        this.icdSuggester = icdSuggester;
        this.icdSpellChecker = icdSpellChecker;
        this.opsSpellChecker = opsSpellChecker;
        this.pathToTempIndex = pathToTempIndex;
        this.pathToTempDictionary = pathToTempDictionary;
        analyzer = new StandardAnalyzer();

    }

    /**
     * the manager will decide whether to use the regulare Dictionary or the
     * temp Dictionary and whether to use autocomplete or correction
     *
     * Halabieh
     *
     * @param fullSearchString full search text
     * @param diagnose icd or ops
     *
     */
    public void spellCheckerManager(String fullSearchString, EasyCoderDialogFXMLController.Diagnose diagnose) {
        String searchString = "";
        //by using ODER function start spellchecker after "," 
        if (fullSearchString.contains(",")) {
            fullSearchString = fullSearchString.substring(fullSearchString.lastIndexOf(',') + 1);
        }
        String[] fullSearchArray = fullSearchString.trim().split(" ");
        //if the search text has more than 2 words
        //start usuing temp Dictionary
        if (fullSearchArray.length >= 2) {
            tempDictionary = true;
            switch (diagnose) {
                case ICD:
                    txtSearch = diagnosisTxtSearch;
                    break;
                case OPS:
                    txtSearch = procedureTxtSearch;
                    break;
            }
        } else {
            tempDictionary = false;
            switch (diagnose) {
                case ICD:
                    txtSearch = diagnosisTxtSearch;
                    suggester = icdSuggester;
                    spellChecker = icdSpellChecker;
                    break;
                case OPS:
                    txtSearch = procedureTxtSearch;
                    suggester = opsSuggester;
                    spellChecker = opsSpellChecker;
                    break;
            }
        }
        txtSearch.hide();
        txtSearch.clearEntries();
        int position = 0;
        if (txtSearch.getCaretPosition() + 1 >= fullSearchString.length()) {
            position = fullSearchString.length();
        } else {
            position = txtSearch.getCaretPosition();
        }
        String[] subText = fullSearchString.substring(0, position).trim().split(" ");
        searchString = fullSearchArray[subText.length - 1];
        //start after writing x  charackter
        //minimum 1 otherwise it would start searching before building the temp dictionary
        if (searchString.length() > 1) {

            if (tempDictionary) {
                //if autoComplete didn't return results start with correction
                if (startAutoCompleteWithTemp(searchString, fullSearchString) == false) {
                    //start correction
                    startCorrectionWithTemp(searchString, fullSearchString);
                }
            } else {
                if (startAutoCompleteWithoutTemp(searchString, fullSearchString, suggester) == false) {
                    startCorrectionWithoutTemp(searchString, fullSearchString, spellChecker);
                }
            }
        }
    }

    /**
     * showPopUp will determin the exact position of the popUp and show it
     *
     * Halabieh
     *
     * @param enteredString the word before correction
     * @param results results array
     * @param fullSearchString full search text
     */
    public void showPopUp(String enteredString, String[] results, String fullSearchString) {
        if (results != null && results.length > 0) {
            txtSearch.setEntries(results);
            String fullText = txtSearch.getText();
            String[] textAsArray = fullText.trim().split(" ");
            int position = 0;
            if (txtSearch.getCaretPosition() + 1 >= fullText.length()) {
                position = fullText.length();
            } else {
                position = txtSearch.getCaretPosition();
            }
            String[] subText = fullText.substring(0, position).trim().split(" ");
            //get the right x position of popUp
            //using Text to get the right width in pixel size
            Text t = new Text(String.join(" ", subText));
            //fix the posision of the List so that it dosn't exceed the size of easyCoder scene
            double listPosition = t.getLayoutBounds().getWidth() + 5;
            if (listPosition > diagnosisTxtSearch.getWidth()) {
                listPosition = 0.0;
            }
            txtSearch.showPopup(textAsArray[subText.length - 1], listPosition, subText.length - 1);
        }
    }

    /**
     * correcting the wrong word by usuing lucene SpellChecker with
     * JaroWinklerDistance algorithm
     *
     * Halabieh
     *
     * @param searchString the wotd that need to be corrected
     * @param fullSearchString full search text
     */
    public void startCorrectionWithTemp(final String searchString, final String fullSearchString) {
        String[] results;
        try ( Directory tempIndex = FSDirectory.open(Paths.get(pathToTempIndex));  SpellChecker tempSpellChecker = new SpellChecker(tempIndex)) {
            tempSpellChecker.indexDictionary(new PlainTextDictionary(Paths.get(pathToTempDictionary)), new IndexWriterConfig(null), true);
            tempSpellChecker.setStringDistance(new JaroWinklerDistance());
            //number of suggesstions = 10
            //maximum number of results is limited to 10 for better performance (limitation is written in the Class AutocompletionlTextField)
            results = tempSpellChecker.suggestSimilar(searchString, 10);
            if (tempSpellChecker.exist(searchString)) {
                return;
            }
            if (results.length > 0) {
                showPopUp(searchString, results, fullSearchString);
            }
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, "can't build spellChecker for temp Dictionary!", ioe);
        }

    }

    /**
     * correcting the wrong word by usuing lucene SpellChecker with
     * JaroWinklerDistance algorithm
     *
     * Halabieh
     *
     * @param searchString the wotd that need to be corrected
     * @param fullSearchString full search text
     * @param spellChecker (icd or ops spellChecker)
     */
    public void startCorrectionWithoutTemp(final String searchString, final String fullSearchString, SpellChecker spellChecker) {
        String[] results;
        if (spellChecker != null) {
            spellChecker.setStringDistance(new JaroWinklerDistance());
            try {
                //number of suggesstions = 10
                //maximum number of results is limited to 10 for better performance (limitation is written in the Class AutocompletionlTextField)
                results = spellChecker.suggestSimilar(searchString, 10);
                if (spellChecker.exist(searchString)) {
                    return;
                }
                if (results.length > 0) {
                    showPopUp(searchString, results, fullSearchString);
                }

            } catch (IOException ioe) {
                LOG.log(Level.SEVERE, "can't get spellChecker results", ioe);
            }
        }

    }

    /**
     *
     * @param searchString the word that need to be auto completed
     * @param fullSearchString full search text
     * @param suggester (icd or ops suggester)
     * @return true if the word can be autocompleted
     */
    public boolean startAutoCompleteWithoutTemp(final String searchString, final String fullSearchString, AnalyzingInfixSuggester suggester) {
        List<Lookup.LookupResult> results = null;
        try {
            //number of suggesstions = 10
            //maximum number of results is limited to 10 for better performance (limitation is written in the Class AutocompletionlTextField)
            results = suggester.lookup(searchString, 10, true, false);

        } catch (IOException ex) {
            Logger.getLogger(EasyCoderDialogFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (results != null && !results.isEmpty()) {
            Set<String> res = new LinkedHashSet<>();
            for (Lookup.LookupResult result : results) {
                String filteredResults = result.toString().substring(0, result.toString().length() - 2);
                //if the searched word already written correctly don't show suggestions
                if (filteredResults.equals(searchString)) {
                    res.clear();
                    break;
                }
                res.add(filteredResults);
            }
            String[] stockArr = new String[res.size()];
            showPopUp(searchString, res.toArray(stockArr), fullSearchString);
            return true;
        } else {
            return false;
        }
    }

    /**
     * autocomplete text usuing Lucene AnalyzingInfixSuggester Halabieh
     *
     * @param searchString the word that need to be auto completed
     * @param fullSearchString full search text
     * @return true if the word can be autocompleted
     */
    public boolean startAutoCompleteWithTemp(final String searchString, final String fullSearchString) {
        List<Lookup.LookupResult> results = null;
        try ( Directory tempDirectory = FSDirectory.open(Paths.get(pathToTempIndex));  AnalyzingInfixSuggester tempSuggester = new AnalyzingInfixSuggester(tempDirectory, analyzer);) {
            tempSuggester.build(new PlainTextDictionary(Paths.get(pathToTempDictionary)));
            //number of suggesstions = 10
            //maximum number of results is limited to 10 for better performance (limitation is written in the Class AutocompletionlTextField)
            results = tempSuggester.lookup(searchString, 10, true, false);
            if (results != null && !results.isEmpty()) {
                Set<String> res = new LinkedHashSet<>();
                for (Lookup.LookupResult result : results) {
                    String filteredResults = result.toString().substring(0, result.toString().length() - 2);
                    //if the searched word already written correctly don't show suggestions
                    if (filteredResults.equals(searchString)) {
                        res.clear();
                        break;
                    }
                    res.add(filteredResults);
                }
                String[] stockArr = new String[res.size()];
                showPopUp(searchString, res.toArray(stockArr), fullSearchString);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "can't build AnalyzingInfixSuggester for temp Dictionary!", e);
        }
        if (results != null && !results.isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

}

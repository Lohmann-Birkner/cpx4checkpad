/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.container;

import de.lb.cpx.sap.dto.SapCase;
import de.lb.cpx.sap.results.SapKainDetailSearchResult;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class FallContainer {

    private static final Logger LOG = Logger.getLogger(FallContainer.class.getName());

    private final Map<String, List<SapKainDetailSearchResult>> kainMessages = new HashMap<>();
    private final Set<SapCase> sapCases = new HashSet<>();

    /**
     *
     */
    public FallContainer() {

    }

    /**
     *
     * @param pSapCase sap case
     */
    public void addSapCase(final SapCase pSapCase) {
        if (pSapCase == null) {
            LOG.log(Level.WARNING, "SapCase is null!");
            return;
        }
        sapCases.add(pSapCase);
    }

    /**
     *
     * @param pSapCases set of sap cases
     */
    public void setSapCases(final Set<SapCase> pSapCases) {
        if (pSapCases == null) {
            LOG.log(Level.WARNING, "SapCases is null!");
            return;
        }
        sapCases.clear();
        for (SapCase sapCase : pSapCases) {
            addSapCase(sapCase);
        }
    }

    /**
     *
     * @return set of sap cases
     */
    public Set<SapCase> getSapCases() {
        return new HashSet<>(sapCases);
    }

    /**
     *
     * @param pCaseIdent hospital case identifier
     * @param pKainMessage kain message
     */
    public void addKainMessages(final String pCaseIdent, final List<SapKainDetailSearchResult> pKainMessage) {
        final String caseIdent = (pCaseIdent == null ? "" : pCaseIdent.trim());
        if (pKainMessage == null) {
            LOG.log(Level.WARNING, "KainMessage is null!");
            return;
        }
        kainMessages.put(caseIdent, pKainMessage);
    }

    /**
     *
     * @param pKainMessages kain messages
     */
    public void setKainMessages(final Map<String, List<SapKainDetailSearchResult>> pKainMessages) {
        if (pKainMessages == null) {
            LOG.log(Level.WARNING, "KainMessages is null!");
            return;
        }
        kainMessages.clear();
        for (Map.Entry<String, List<SapKainDetailSearchResult>> entry : pKainMessages.entrySet()) {
            addKainMessages(entry.getKey(), entry.getValue());
        }
    }

    /**
     *
     * @return map of kain messages
     */
    public Map<String, List<SapKainDetailSearchResult>> getKainMessages() {
        return new HashMap<>(kainMessages);
    }

}

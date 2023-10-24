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
package de.lb.cpx.sap.importer;

import de.lb.cpx.sap.container.FallContainer;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import module.AbstractImportContainerModule;

/**
 * Import module that is needed for the CPX Importer and CPX
 * Transformation/Distribution
 *
 * @author niemeier
 */
public class Sap extends AbstractImportContainerModule<SapJob, FallContainer> {

    private static final long serialVersionUID = 1L;
    private static final String NAME = "SAP";
    //public final FallContainer mFallContainer;

    /**
     * SAP Import object for transformation and distribution in CPX
     *
     * @param pInputConfig SAP Config
     * @param pContainer SAP container
     * @param pOutputDirectory Output directory
     */
    public Sap(final SapJob pInputConfig, final FallContainer pContainer, final String pOutputDirectory) {
        super(NAME, pInputConfig, pContainer, pOutputDirectory);
        //mFallContainer = pFallContainer;
        //mOutputDirectory = toStr(pOutputDirectory);

//        if (mFallContainer == null) {
//            throw new IllegalArgumentException("No input set of SAP cases given!");
//        }
//        if (mOutputDirectory.isEmpty()) {
//            throw new IllegalArgumentException("No output directory given!");
//        }
    }

    /**
     * SAP Import object for transformation and distribution in CPX
     *
     * @param pInputConfig SAP config
     * @param pContainer SAP container
     */
    public Sap(final SapJob pInputConfig, final FallContainer pContainer) {
        super(NAME, pInputConfig, pContainer);
    }

//    /**
//     * Import name
//     *
//     * @return Name of this import (SAP)
//     */
//    @Override
//    public String getName() {
//        return "SAP";
//    }
//    /**
//     * List of filled Cases with data from SAP
//     *
//     * @return SAP Cases
//     */
//    public FallContainer getFallContainer() {
//        return mFallContainer;
//    }
//    /**
//     * Import directory (not used, because SAP is an object and not a file based
//     * import)
//     *
//     * @return import directory (unused!)
//     */
//    @Override
//    public String getInputDirectory() {
//        //return mInputDirectory;
//        return "";
//    }
//    /**
//     * Output directory (temporary files for CPX transformation)
//     *
//     * @return output directory
//     */
//    @Override
//    public String getOutputDirectory() {
//        return mOutputDirectory;
//    }
}

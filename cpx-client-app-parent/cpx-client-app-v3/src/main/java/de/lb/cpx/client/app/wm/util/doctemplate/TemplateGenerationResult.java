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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.util.doctemplate;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * This result object gives you control over word template generating task. You
 * can observe changes and states and you can add additional handlers.
 * Additionally there are same handy information that might be very useful for
 * following actions.
 *
 * @author niemeier
 */
public class TemplateGenerationResult {

    public final BooleanProperty isWordClosed = new SimpleBooleanProperty(false);
    public final BooleanProperty isDocumentOpened = new SimpleBooleanProperty(false);
    private File inOutFile;
    public final CWmListDocumentType type;
    private TWmProcess process;
    private final ProcessServiceFacade facade;
    private TWmDocument wmDocument;
    private CpxTask<File> documentGenerationTask;

    private ActiveXComponent word;
    private Dispatch document;

    public TemplateGenerationResult(final File pInOutFile, final CWmListDocumentType pType,
            final TWmDocument pWmDocument, final TWmProcess pProcess,
            final ProcessServiceFacade pProcessFacade, final CpxTask<File> pDocumentGenerationTask) {
        inOutFile = pInOutFile;
        type = pType;
        wmDocument = pWmDocument;
        process = pProcess;
        facade = pProcessFacade;
        documentGenerationTask = pDocumentGenerationTask;
    }

    /**
     * Interface to word application
     *
     * @param pWord word application interface
     */
    public void setWord(final ActiveXComponent pWord) {
        word = pWord;
    }

    /**
     * Here you have access to the word application, so you can kill or control
     * it for example
     *
     * @return interface to word application (can be null!)
     */
    public ActiveXComponent getWord() {
        return word;
    }

    /**
     * Interface to word document
     *
     * @param pDocument word document interface
     */
    public void setDocument(final Dispatch pDocument) {
        document = pDocument;
    }

    /**
     * Here you have access to the word document, so you can close it for
     * example
     *
     * @return interface to word document (can be null!)
     */
    public Dispatch getDocument() {
        return document;
    }

    public File getInOutFile() {
        return inOutFile;
    }

    public CWmListDocumentType getType() {
        return type;
    }

    public TWmProcess getProcess() {
        return process;
    }

    public ProcessServiceFacade getFacade() {
        return facade;
    }

    public TWmDocument getWmDocument() {
        return wmDocument;
    }
    
    /**
     * This task represents the process to generate a word document from
     * template and open it afterwards. The task terminates successfully when
     * the user closes the word application. You can use this task to react on
     * failures or whatever.
     *
     * @return task
     */
    public CpxTask<File> getDocumentGenerationTask() {
        return documentGenerationTask;
    }

    public void dispose() {
        process = null;
        documentGenerationTask = null;
        document = null;
        wmDocument = null;
    }

}

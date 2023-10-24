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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx;

import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.shared.dto.LockException;
import java.io.IOException;

/**
 * MainFrame Class for the ProcessOverview handles loading and showing of its
 * FxmlController class and its content
 *
 * @author wilde
 */
public final class WmMainFrameScene extends CpxScene {
    //
//    @Override
//    public void setUp() {
//        getController().setProcess((Long) this.valuePorperty.getValue());
//    }

//    public LongProperty processProperty = new SimpleLongProperty();
    public WmMainFrameScene(Long processId) throws IOException, LockException {
        super(CpxFXMLLoader.getLoader(WmMainFrameFXMLController.class));
//        processProperty.bindBidirectional(valuePorperty);
//        processProperty.setValue(processId);
        getController().setProcess(processId);
//        2018-03-01 DNi: Don't display a custom title for any reason        
//        TWmProcess curProcess = getController().getProcess();
//        if (curProcess != null) {
//            //Display workflow number in window title ("Vorgangs-Nr. XY")
//            setSceneTitle(Lang.getProcessNumber() + " " + curProcess.getWorkflowNumber());
//        }
    }

    public WmMainFrameScene() throws IOException, LockException {
        this(null);
    }

    @Override
    public WmMainFrameFXMLController getController() {
        return (WmMainFrameFXMLController) super.getController();
    }

}

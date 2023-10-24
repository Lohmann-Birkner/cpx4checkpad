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
package de.lb.cpx.client.app.wm.fx.process.tab;

import de.lb.cpx.client.app.cm.fx.CaseManagementScene;
import de.lb.cpx.client.app.wm.util.SceneLoader;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Implements closable Case Tab for the ProcessOverview, shows in its content
 * the CaseDetials
 *
 * @author wilde
 */
public final class CaseTab extends TwoLineTab {

    private static final Logger LOG = Logger.getLogger(CaseTab.class.getName());

    private CaseManagementScene contentScene;
    private TCase currentCase;

    public CaseManagementScene getContentScene() {
        return contentScene;
    }

    /**
     * construct a new Tab, loads content dependend on the given id TODO: Set
     * Title, desc and Image
     *
     * @param caseId unique db id of a specific case
     */
    public CaseTab(long caseId) {
        super();
        setDescription(Lang.getCaseData());
        //id, to check is there is already a tab with that case id to avoid double loading
        setId(String.valueOf(caseId));
        //load FMXL and controller class
        contentScene = (CaseManagementScene) SceneLoader.getInstance().loadCaseMangementScene(caseId);

        if (contentScene == null) {
            LOG.warning("Case Scene can not be loaded, maybe locked? Close Tab");
            close();
            return;
        }
//        ((CaseDetailsMainViewFXMLController)contentScene.getController()).initGuiWithCaseId(caseId, false);
//        currentCase = ((CaseDetailsMainViewFXMLController)contentScene.getController()).getCurrentCase();
        currentCase = contentScene.getDisplayedCase();
        setTitle(currentCase != null ? currentCase.getCsCaseNumber() : "");
        setContent(contentScene.getRootWithoutHeader());
        //handle on close, if closed case should be unlocked
        setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
//                CaseTab.super.getOnCloseRequest().handle(event);
                close();
            }
        });
        setGlyph("\uf016");
    }

    public TCase getCase() {
        return currentCase;
    }

    @Override
    public TabType getTabType() {
        return TabType.CASE;
    }

    @Override
    public void close() {
//        ((CaseManagementScene)contentScene.getController()).close();
        super.close();
        if (contentScene != null) {
            contentScene.close();
            setContent(null);
            contentScene = null;
            currentCase = null;
        }
    }
}

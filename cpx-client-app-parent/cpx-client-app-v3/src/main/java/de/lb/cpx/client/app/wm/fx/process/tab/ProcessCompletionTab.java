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
package de.lb.cpx.client.app.wm.fx.process.tab;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.completion.ProcessCompletionScreen;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author nandola
 */
public class ProcessCompletionTab extends TwoLineTab {

    private ProcessCompletionScreen screen;

    public ProcessCompletionTab(long pCaseId) {
        super();
//        root = new HBox();
//        root.setAlignment(Pos.CENTER);
//        root.setSpacing(5.0);
//        labelTitle = new Label(title);
//        root.getChildren().addAll(labelTitle);
        setId(String.valueOf(pCaseId));
        setTitle(Lang.getProcessFinalisation());
        setDescription("");
//        setGlyph("\uf07c");
        setGlyph(ResourceLoader.getGlyph(FontAwesome.Glyph.FLAG));
        try {
            screen = new ProcessCompletionScreen();
            setContent(screen.getRoot());
        } catch (IOException ex) {
            Logger.getLogger(ProcessCompletionTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public TabType getTabType() {
        return TabType.PROCESSCOMPLETION;
    }

    public void initFacade(ProcessServiceFacade serviceFacade) {
        screen.getController().init(serviceFacade);
    }

    public void refresh() {
        screen.getController().refresh();
    }

    @Override
    public void reload() {
        screen.getController().reload();
    }
}

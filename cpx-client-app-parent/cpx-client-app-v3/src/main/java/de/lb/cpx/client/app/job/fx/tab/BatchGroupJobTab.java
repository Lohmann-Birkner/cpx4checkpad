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
package de.lb.cpx.client.app.job.fx.tab;

import de.lb.cpx.client.app.job.fx.batchgroup.JobBatchGroupingScreen;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import javafx.beans.property.ReadOnlyBooleanProperty;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Tab for job overview for Batch grouping
 *
 * @author wilde
 */
public class BatchGroupJobTab extends TwoLineTab {

    private final JobBatchGroupingScreen screen;

    /**
     * creates new instance and load content
     *
     * @throws java.io.IOException when content can not be loaded
     */
    public BatchGroupJobTab() throws IOException {
        setTitle(Lang.getMenuBatchGrouping());
        setDescription("");
        setGlyph(FontAwesome.Glyph.FOLDER_OPEN);
        screen = new JobBatchGroupingScreen();
        setContent(screen.getRoot());
    }

    public ReadOnlyBooleanProperty isTaskRunningProperty() {
        return screen.getController().isTaskRunningProperty();
    }

    @Override
    public TabType getTabType() {
        return TabType.JOBBATCHGROUPING;
    }

    public JobBatchGroupingScreen getScreen() {
        return screen;
    }

    @Override
    public void reload() {
        super.reload();
        screen.reload();
    }

    @Override
    public void close() {
        super.close();
        screen.getController().close();
    }

}

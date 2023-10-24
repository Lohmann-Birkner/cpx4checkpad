/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRequestOther;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Skin;

/**
 * Editor class to edit an TWMInsuranceObject
 *
 * @author wilde
 */
public class OtherRequestEditor extends BasicRequestEditor<TWmRequestOther> {

//    protected ProcessServiceFacade facade;
    public OtherRequestEditor(final ProcessServiceFacade pFacade, final TCase pCase) {
        super(new TWmRequestOther(), pFacade, pCase);
    }
    
    @Override
    public void initCatalogValidation() {
        super.initCatalogValidation();
        getCatalogValidationResult().add(CpxErrorTypeEn.WARNING,
                Lang.getValidationMasterdataNoRequestStatusFound(),
                (Void t) -> MenuCache.getMenuCacheRequestStates().values(MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty());
        getCatalogValidationResult().add(CpxErrorTypeEn.WARNING,
                Lang.getValidationMasterdataNoAuditReasonsFound(),
                (Void t) -> MenuCache.getMenuCacheAuditReasons().values(MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new OtherRequestEditorSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(OtherRequestEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

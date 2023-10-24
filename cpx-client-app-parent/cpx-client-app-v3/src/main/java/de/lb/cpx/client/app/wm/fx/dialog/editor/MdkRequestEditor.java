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
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.catalog.CpxMdk;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequestMdk;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

/**
 * Editor class to edit an TWMRequestMdk Object
 *
 * @author shahin
 */
public class MdkRequestEditor extends BasicRequestEditor<TWmRequestMdk> {

    public MdkRequestEditor(final ProcessServiceFacade pFacade, final TCase pCase) {
        super(new TWmRequestMdk(), pFacade, pCase);
    }
    
    @Override
    public void initCatalogValidation() {
        super.initCatalogValidation();
        getCatalogValidationResult().add(CpxErrorTypeEn.WARNING,
                Lang.getValidationCatalogdataNoMdksFound(),
                (Void t) -> !getMdkCatalog().hasMdks());
        getCatalogValidationResult().add(CpxErrorTypeEn.WARNING,
                Lang.getValidationMasterdataNoRequestStatusFound(),
                (Void t) -> MenuCache.getMenuCacheRequestStates().values(MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty());
        getCatalogValidationResult().add(CpxErrorTypeEn.WARNING,
                Lang.getValidationMasterdataNoAuditReasonsFound(),
                (Void t) -> MenuCache.getMenuCacheAuditReasons().values(MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty());
        getCatalogValidationResult().add(CpxErrorTypeEn.ERROR, Lang.getValidationCatalogdataNoMdkExists(), (t) -> {
            if(getRequest() == null || (getRequest().getId() == 0L)){
                return false;
            }
            return!(getMdk()!=null&&getMdk().getId()!=0L);// getMdkCatalog().getByCode(String.valueOf(getRequest().getMdkInternalId()), AbstractCpxCatalog.DEFAULT_COUNTRY)==null;
        });
    }
    
    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new MdkRequestEditorSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(MdkRequestEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
//    private List<TWmReminder> mdkReminders;
//    
//     public void setMdkReminders(List<TWmReminder> pMdkReminders) {
//        List<TWmReminder> copyMdkReminders = new ArrayList<>(pMdkReminders);
//        this.mdkReminders = copyMdkReminders;
//    }
//
//    public List<TWmReminder> getMdkReminders() {
//        if (mdkReminders != null) {
//            return new ArrayList<>(mdkReminders);
//        } else {
//            return new ArrayList<>();
//        }
//    }
//   
    
//    private ObjectProperty<CpxMdkCatalog> mdkCatalogProperty;
//
//    public ObjectProperty<CpxMdkCatalog> mdkCatalogProperty() {
//        if (mdkCatalogProperty == null) {
//            mdkCatalogProperty = new SimpleObjectProperty<>();
//        }
//        return mdkCatalogProperty;
//    }
//
//    public CpxMdkCatalog getCpxMdkCatalog() {
//        return mdkCatalogProperty().get();
//    }
//
//    public void setCpxMdkCatalog(CpxMdkCatalog pMdk) {
//        mdkCatalogProperty().set(pMdk);
//    }

    public CpxMdkCatalog getMdkCatalog() {
        return CpxMdkCatalog.instance();
    }

    
    private ObjectProperty<CpxMdk> mdkProperty;

    public ObjectProperty<CpxMdk> mdkProperty() {
        if (mdkProperty == null) {
            mdkProperty = new SimpleObjectProperty<>();
        }
        return mdkProperty;
    }

    public CpxMdk getMdk() {
        return mdkProperty().get();
    }

    public void setMdk(CpxMdk pInsuranceCompany) {
        mdkProperty().set(pInsuranceCompany);
    }
}

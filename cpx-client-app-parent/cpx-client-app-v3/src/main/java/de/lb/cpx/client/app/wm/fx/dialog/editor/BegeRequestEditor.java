/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRequestBege;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

/**
 *
 * @author wilde
 */
public class BegeRequestEditor extends BasicRequestEditor<TWmRequestBege>{

    private static final Logger LOG = Logger.getLogger(BegeRequestEditor.class.getName());
    
    public BegeRequestEditor(ProcessServiceFacade pFacade, TCase pCase) {
        super(new TWmRequestBege(), pFacade, pCase);
    }

    @Override
    public void initCatalogValidation() {
        super.initCatalogValidation();
        getCatalogValidationResult().replaceIfExists(CpxErrorTypeEn.WARNING,
                Lang.getValidationCatalogdataNoBegesFound(),
                (Void t) -> !getInsuranceCatalog().hasEntryOfInscClass(12, AbstractCpxCatalog.DEFAULT_COUNTRY));
        getCatalogValidationResult().replaceIfExists(CpxErrorTypeEn.WARNING,
                Lang.getValidationMasterdataNoRequestStatusFound(),
                (Void t) -> MenuCache.getMenuCacheRequestStates().values(MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty());
        getCatalogValidationResult().replaceIfExists(CpxErrorTypeEn.WARNING,
                Lang.getValidationMasterdataNoAuditReasonsFound(),
                (Void t) -> MenuCache.getMenuCacheAuditReasons().values(MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty());
        getCatalogValidationResult().replaceIfExists(CpxErrorTypeEn.ERROR, Lang.getValidationCatalogdataNoBegeExists(), (t) -> {
            if(getRequest() == null || (getRequest().getId() == 0L)){
                return false;
            }
            return !(getInsuranceCompany()!=null&&getInsuranceCompany().getId()!=0L);//!getInsuranceCatalog().hasEntry(getRequest().getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
        });
    }
    
    
    
    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new BegeRequestEditorSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(BegeRequestEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private ObjectProperty<CpxInsuranceCompany> insuranceCompanyProperty;

    public ObjectProperty<CpxInsuranceCompany> insuranceCompanyProperty() {
        if (insuranceCompanyProperty == null) {
            insuranceCompanyProperty = new SimpleObjectProperty<>();
        }
        return insuranceCompanyProperty;
    }

    public CpxInsuranceCompany getInsuranceCompany() {
        return insuranceCompanyProperty().get();
    }

    public void setInsuranceCompany(CpxInsuranceCompany pInsuranceCompany) {
        insuranceCompanyProperty().set(pInsuranceCompany);
    }

    public CpxInsuranceCompanyCatalog getInsuranceCatalog() {
        return CpxInsuranceCompanyCatalog.instance();
    }
    
    protected void disableControls() {
        if(!(getSkin() instanceof BegeRequestEditorSkin)){
            LOG.warning("Can not disable Control - unknown Skin-Class set: " + getSkin().getClass().getName());
            return;
        }
        ((BegeRequestEditorSkin)getSkin()).disableControls(getRequest() != null
                && Session.instance().getCpxUserId() != getRequest().getCreationUser() && !Session.instance().getRoleProperties().isEditRequestOfOtherUserAllowed());
    }

}

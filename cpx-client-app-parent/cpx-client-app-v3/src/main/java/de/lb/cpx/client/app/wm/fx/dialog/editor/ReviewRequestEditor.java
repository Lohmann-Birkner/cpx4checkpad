/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.model.TCase;
import de.lb.cpx.wm.model.TWmRequestReview;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

/**
 *
 * @author gerschmann
 */
public class ReviewRequestEditor extends BasicRequestEditor<TWmRequestReview> {
    
    public ReviewRequestEditor(final ProcessServiceFacade pFacade, final TCase pCase) {
        super(new TWmRequestReview(), pFacade, pCase);
    }
    
        
    @Override
    public void initCatalogValidation() {
        super.initCatalogValidation();
    }
    
    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new ReviewRequestEditorSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(MdkRequestEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

   
    public CpxInsuranceCompanyCatalog getInsuranceCatalog() {
        return CpxInsuranceCompanyCatalog.instance();
    }
 
    public CpxMdkCatalog getMdkCatalog() {
        return CpxMdkCatalog.instance();
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

    
}

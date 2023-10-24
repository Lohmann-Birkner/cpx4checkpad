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

import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.wm.model.TWmRequestMdk;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author wilde
 */
public class ReadOnlyMdkRequestEditorSkin extends MdkRequestEditorSkin{
    
    public ReadOnlyMdkRequestEditorSkin(MdkRequestEditor pSkinnable) throws IOException {
        super(pSkinnable);
    }

    @Override
    protected void setUpValidation() {
        //not needed
    }

    @Override
    protected void setUpAutoCompletion() {
        //not needed
    }

    @Override
    public void setUpdateListeners() {
        if (getSkinnable().getRequest() != null) {
            if (getSkinnable().getRequest().getId() != 0L) {
                updateRequestValues(getSkinnable().getRequest());
            }
        }
        getSkinnable().requestProperty().addListener(new ChangeListener<TWmRequestMdk>(){
            @Override
            public void changed(ObservableValue<? extends TWmRequestMdk> ov, TWmRequestMdk t, TWmRequestMdk t1) {
                if (t1 == null) {
                    return;
                }
                updateRequestValues(t1);
            }
        });
        if (getSkinnable().getRequest() != null) {
            final Long mdkInternalId = getSkinnable().getRequest().getMdkInternalId();
            if (mdkInternalId != null) {
                updateCatalogValues(getSkinnable().getMdkCatalog().getByCode(String.valueOf(mdkInternalId), AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        }
//        getSkinnable().mdkCatalogProperty().addListener((ObservableValue<? extends CpxMdkCatalog> observable, CpxMdkCatalog oldValue, CpxMdkCatalog newValue) -> {
//            if (newValue == null) {
//                return;
//            }
//            updateCatalogValues(newValue.getByCode(String.valueOf(getSkinnable().getRequest().getMdkInternalId()), AbstractCpxCatalog.DEFAULT_COUNTRY));
//        });
    }
    
}

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
package de.lb.cpx.client.app.wm.fx.process.completion.risk;

import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 *
 * @author wilde
 * @param <T> risk entity
 * @param <Z> risk detail entity
 */
public abstract class ProcessCompletionRisk<T extends AbstractEntity,Z extends AbstractEntity> extends Control {

    private static final Logger LOG = Logger.getLogger(ProcessCompletionRisk.class.getName());

    private final T risk;

    public ProcessCompletionRisk(T pRisk) {
        super();
        risk = pRisk;
        setEditable(isRequestFinalisation());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new ProcessCompletionRiskSkin<>(this);
        } catch (IOException ex) {
            Logger.getLogger(ProcessCompletionRisk.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin(); //To change body of generated methods, choose Tools | Templates.
    }

    public T getRisk() {
        return risk;
    }
    private final ObservableList<RiskAreaEn> riskAreas = FXCollections.observableArrayList();

    public final ObservableList<RiskAreaEn> riskAreas() {
        return riskAreas;
    }

    private BooleanProperty editableProperty;

    public BooleanProperty editableProperty() {
        if (editableProperty == null) {
            editableProperty = new SimpleBooleanProperty(Boolean.TRUE);
        }
        return editableProperty;
    }

    public final void setEditable(boolean pEditable) {
        editableProperty().set(pEditable);
    }

    public final boolean isEditable() {
        return editableProperty().get();
    }

    private BooleanProperty showTitleProperty;

    public BooleanProperty showTitleProperty() {
        if (showTitleProperty == null) {
            showTitleProperty = new SimpleBooleanProperty(Boolean.TRUE);
        }
        return showTitleProperty;
    }

    public final void setShowTitle(boolean pShowTitle) {
        showTitleProperty().set(pShowTitle);
    }

    public final boolean isShowTitle() {
        return showTitleProperty().get();
    }
    
    public abstract PlaceOfRegEn getPlaceOfRegEn();

    public boolean addRiskDetail(RiskAreaEn pRiskArea){
        if (pRiskArea == null) {
            LOG.warning("can not add riskdetail, riskarea is null");
            return false;
        }
        if (getRisk() == null) {
            LOG.warning("can not add riskdetail, risk is null");
            return false;
        }
        if (hasRiskArea(pRiskArea)) {
            LOG.warning("can not add riskdetail, riskArea already exists");
            return false;
        }
        return true;
    }
    
    public abstract boolean hasRiskArea(RiskAreaEn pRiskArea);
    public abstract Boolean addRiskDetail(Z pRiskDetail);
    public abstract Boolean removeRiskDetail(Z pRiskDetail);
    private boolean isRequestFinalisation() {
        return PlaceOfRegEn.REQUEST_FINALISATION.equals(getPlaceOfRegEn());
    }

    private static final Callback<RiskAreaEn, Boolean> DEFAULT_DELETE_RISK_AREA = new Callback<RiskAreaEn, Boolean>() {
        @Override
        public Boolean call(RiskAreaEn param) {
            return false;
        }
    };
    private Callback<RiskAreaEn, Boolean> deleteRiskArea;

    public void setDeleteRiskArea(Callback<RiskAreaEn, Boolean> pCallback) {
        deleteRiskArea = pCallback;
    }

    public Callback<RiskAreaEn, Boolean> getDeleteRiskArea() {
        return deleteRiskArea == null ? DEFAULT_DELETE_RISK_AREA : deleteRiskArea;
    }

    private static final Callback<RiskAreaEn, Boolean> DEFAULT_ADD_RISK_AREA = new Callback<RiskAreaEn, Boolean>() {
        @Override
        public Boolean call(RiskAreaEn param) {
            return false;
        }
    };
    private Callback<RiskAreaEn, Boolean> addRiskArea;

    public void setAddRiskArea(Callback<RiskAreaEn, Boolean> pCallback) {
        addRiskArea = pCallback;
    }

    public Callback<RiskAreaEn, Boolean> getAddRiskArea() {
        return addRiskArea == null ? DEFAULT_ADD_RISK_AREA : addRiskArea;
    }

    private BooleanProperty showVBarProperty;

    public BooleanProperty showVBarProperty() {
        if (showVBarProperty == null) {
            showVBarProperty = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return showVBarProperty;
    }

    public final void setShowVBar(boolean pShowVBar) {
        showVBarProperty().set(pShowVBar);
    }

    public final boolean isShowVBar() {
        return showVBarProperty().get();
    }
    private ObjectProperty<RiskDisplayMode> displayModeProperty;

    public ObjectProperty<RiskDisplayMode> displayModeProperty() {
        if (displayModeProperty == null) {
            displayModeProperty = new SimpleObjectProperty<>(RiskDisplayMode.DISPLAY_ALL);
        }
        return displayModeProperty;
    }

    public RiskDisplayMode getDisplayMode() {
        return displayModeProperty().get();
    }

    public void setDisplayMode(RiskDisplayMode pDisplayMode) {
        displayModeProperty().set(pDisplayMode);
    }
    private Callback<T,RiskListCell<Z>> cellFactory;
    public Callback<T,RiskListCell<Z>> getCellFactory(){
        return cellFactory;
    }
    public void setCellFactory(Callback<T,RiskListCell<Z>> pCellFactory){
        cellFactory = pCellFactory;
    }
    
    public abstract void setRiskValueTotal(BigDecimal valueOf);

    public abstract void setRiskPercentTotal(int intValue);

//    public abstract PlaceOfRegEn getRiskPlaceOfReg() ;

    public abstract Number getRiskPercentTotal();

    public abstract Number getRiskValueTotal();

    public abstract boolean hasComment(T risk);

    public abstract String getRiskComment(T risk);
}

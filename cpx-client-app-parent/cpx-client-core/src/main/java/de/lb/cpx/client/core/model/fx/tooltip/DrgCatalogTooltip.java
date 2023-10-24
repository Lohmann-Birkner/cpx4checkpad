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
package de.lb.cpx.client.core.model.fx.tooltip;

import de.lb.cpx.client.core.model.catalog.CpxDrg;
import de.lb.cpx.client.core.model.catalog.CpxDrgCatalog;
import de.lb.cpx.model.enums.AdmissionModeEn;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Implementation of the DRG catalog Tooltip to unify behaviour
 *
 * @author wilde
 */
public class DrgCatalogTooltip extends CatalogTooltip<CpxDrg> {

    private final String catalogCode;
    private final AdmissionModeEn admMode;

    /**
     * creates new Tooltip instance
     *
     * @param pCatalogCode catalog code
     * @param pYearOfValidity year validity for the catalog
     * @param pMode admission mode
     */
    public DrgCatalogTooltip(String pCatalogCode, Integer pYearOfValidity, AdmissionModeEn pMode) {
        super("DrgCatalog: " + pCatalogCode);
        this.catalogCode = pCatalogCode;
        this.admMode = pMode;
        setYearOfValidity(pYearOfValidity);
        setCatalogItem(fetchData());
    }

    @Override
    public CpxDrg fetchData() {
        return CpxDrgCatalog.instance().getByCode(catalogCode, getCountry(), getYearOfValidity());
    }

    @Override
    public Node createContentNode() {
        GridPane pane = new GridPane();
        pane.getStyleClass().add("default-grid");
        CpxDrg item = getCatalogItem();

        Label admModeText = new Label("Admission Mode:");
        Label admModeValue = new Label(admMode.getTranslation().getValue());

        Label first = new Label("1 Tag zusätzlich:");
        Label firstValue = new Label();
        Label second = new Label("Mittlere Verweildauer:");
        Label secondValue = new Label();
        Label third = new Label("1 Tag abschlag:");
        Label thirdValue = new Label();

        Label four = new Label("MDC (-):");
        Label fourValue = new Label("-");

        pane.addRow(0, admModeText, admModeValue);
        pane.addRow(1, first, firstValue);
        pane.addRow(2, second, secondValue);
        pane.addRow(3, third, thirdValue);
        pane.addRow(4, four, fourValue);

        Mode mode = getModeForAdmission(admMode);
        if (mode != null) {
            switch (mode) {
                case EO:
                    firstValue.setText(String.valueOf(item.getDrgEo1SurchargeDay()));
                    secondValue.setText(String.valueOf(item.getDrgEoAlos()));
                    thirdValue.setText(String.valueOf(item.getDrgEo1DeductionDay()));
                    break;
                case MD:
                    firstValue.setText(String.valueOf(item.getDrgMd1SurchargeDay()));
                    secondValue.setText(String.valueOf(item.getDrgMdAlos()));
                    thirdValue.setText(String.valueOf(item.getDrgMd1DeductionDay()));
                    break;
            }
            if (item.getCMdcSkCatalog() != null) {
                four.setText("MDC (" + item.getCMdcSkCatalog().getId() + "):");
                fourValue.setText(item.getCMdcSkCatalog().getTranslation().getValue());
            }
        }
        return pane;
    }

    private Mode getModeForAdmission(AdmissionModeEn pMode) {
        switch (pMode) {
            case Bo:
            case BoBh:
            case BoBaBh:
                return Mode.EO;
            case HA:
            case HaBh:
            case HaBha:
                return Mode.MD;
            default:
                return null;
        }
    }

    private enum Mode {
        MD, EO
    }
}

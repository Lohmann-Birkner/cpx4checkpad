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
package de.lb.cpx.client.core.model.fx.contextmenu;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.catalog.CpxIcd;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Context Menu to use to change ref types of an icd TODO: Refactor class to
 * work outside of tableview as owner
 *
 * @author wilde
 */
public abstract class IcdRefTypeMenu extends CtrlContextMenu<TableView<?>> {

    private final Collection<TCaseIcd> icdSet;
    private Callback<TCaseIcd, Void> onChange;
    private Menu starFrom;
    private Menu crossFrom;
    private Menu addFrom;
    private Menu addTo;
    private Integer yearOfValidity = CpxClientConfig.instance().getSelectedGrouper().getCatalogYear();
    private boolean menusInitialized = false;

    /**
     * creates new context menu to handle reference icds
     *
     * @param pOwner owner control of the context menu
     * @param pCandidates list of candidates who can be shown as references
     * @param pYearOfValidity valid year of catalog if -1 grouper year is set,
     * year as yyyy
     */
    public IcdRefTypeMenu(TableView<?> pOwner, Collection<TCaseIcd> pCandidates, int pYearOfValidity) {
        super(pOwner);
        ClipboardEnabler.installClipboardToScene(this.getScene());
        icdSet = pCandidates == null ? null : new ArrayList<>(pCandidates);
        yearOfValidity = pYearOfValidity != -1 ? pYearOfValidity : CpxClientConfig.instance().getSelectedGrouper().getCatalogYear();
        initMenus();
    }

    private void initMenus() {
        if (menusInitialized) {
            return;
        }
        menusInitialized = true;
        starFrom = createSecDiagnosisMenu(Lang.getDiagnosisRefTypeAsteriskFrom(), IcdcRefTypeEn.Stern);
        crossFrom = createSecDiagnosisMenu(Lang.getDiagnosisRefTypeDaggerFrom(), IcdcRefTypeEn.Kreuz);
        //fix doppelt "Sterndiagnose von"
        addFrom = createSecDiagnosisMenu(Lang.getDiagnosisRefTypeAdditionalFrom(), IcdcRefTypeEn.Zusatz);
        addTo = createSecDiagnosisMenu(Lang.getDiagnosisRefTypeAdditionalTo(), IcdcRefTypeEn.ZusatzZu);
        getItems().addAll(starFrom, crossFrom, addFrom, addTo);
    }

    public IcdRefTypeMenu(TableView<?> pOwner, Collection<TCaseIcd> pCandidates) {
        this(pOwner, pCandidates, -1);
    }

    public void setOnChange(Callback<TCaseIcd, Void> pOnChange) {
        onChange = pOnChange;
    }

    private Menu createSecDiagnosisMenu(String menuTitle, IcdcRefTypeEn refType) {
        Menu menu = new Menu(menuTitle);
        if (icdSet.size() < 1) {
            menu.setDisable(true);
        }
        for (TCaseIcd icd : icdSet) {

            CheckMenuItem checkItem = new CheckMenuItem(getIcdDescription(icd));
            //CPX-642 RSH 08122017 prevent empty ICD during manual input
            if (icd.getIcdcCode() != null) {
                checkItem.setUserData(icd);
                menu.getItems().add(checkItem);
            }
        }

        menu.setOnShowing(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                TCaseIcd rowItem = getItem();//row.getItem();
                for (MenuItem menuItem : menu.getItems()) {
                    TCaseIcd target = (TCaseIcd) menuItem.getUserData();
                    if (!target.equals2object(rowItem)) {
                        for (TCaseIcd refIcd : rowItem.getRefIcds()) {
                            if (refIcd.versionEquals(target) && refType.equals(refIcd.getIcdcReftypeEn())) {//refIcd.getIcdcReftypeEn().equals(refType)) {
                                ((CheckMenuItem) menuItem).setSelected(true);
                            }
                        }
                        //Add Handling to refTypes
                        menuItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if (((CheckMenuItem) menuItem).isSelected()) {
                                    switch (refType) {
                                        case Stern:
                                            target.setIcdcReftypeEn(IcdcRefTypeEn.Kreuz);
                                            target.setRefIcd(rowItem);
                                            if (!target.getRefIcds().contains(rowItem)) {
                                                target.getRefIcds().add(rowItem);
                                            }

                                            rowItem.setIcdcReftypeEn(IcdcRefTypeEn.Stern);
                                            rowItem.setRefIcd(target);
                                            if (!rowItem.getRefIcds().contains(target)) {
                                                rowItem.getRefIcds().add(target);
                                            }
                                            break;
                                        case Kreuz:
//                                            //basically refType Stern, just inverted
                                            target.setIcdcReftypeEn(IcdcRefTypeEn.Stern);
                                            target.setRefIcd(rowItem);
                                            if (!target.getRefIcds().contains(rowItem)) {
                                                target.getRefIcds().add(rowItem);
                                            }
                                            rowItem.setIcdcReftypeEn(IcdcRefTypeEn.Kreuz);
                                            rowItem.setRefIcd(target);
                                            if (!rowItem.getRefIcds().contains(target)) {
                                                rowItem.getRefIcds().add(target);
                                            }
                                            break;
                                        case Zusatz:
                                            target.setIcdcReftypeEn(IcdcRefTypeEn.ZusatzZu);
                                            target.setRefIcd(rowItem);
                                            if (!target.getRefIcds().contains(rowItem)) {
                                                target.getRefIcds().add(rowItem);
                                            }

                                            rowItem.setIcdcReftypeEn(IcdcRefTypeEn.Zusatz);
                                            rowItem.setRefIcd(target);
                                            if (!rowItem.getRefIcds().contains(target)) {
                                                rowItem.getRefIcds().add(target);
                                            }
                                            break;
                                        case ZusatzZu:
                                            //basically refType Zusatz, just inverted
                                            target.setIcdcReftypeEn(IcdcRefTypeEn.Zusatz);
                                            target.setRefIcd(rowItem);
                                            if (!target.getRefIcds().contains(rowItem)) {
                                                target.getRefIcds().add(rowItem);
                                            }

                                            rowItem.setIcdcReftypeEn(IcdcRefTypeEn.ZusatzZu);
                                            rowItem.setRefIcd(target);
                                            if (!rowItem.getRefIcds().contains(target)) {
                                                rowItem.getRefIcds().add(target);
                                            }
                                            break;
                                    }

                                } else {
                                    target.setIcdcReftypeEn(null);
                                    target.setRefIcds(new HashSet<>());

                                    rowItem.setIcdcReftypeEn(null);
                                    rowItem.setRefIcds(new HashSet<>());
                                }
                                //inform items have changed
                                if (onChange != null) {
                                    onChange.call(target);
                                    onChange.call(rowItem);
                                }
                                TableView<?> owner = getOwner();
                                if (owner != null) {
                                    owner.refresh();
                                }
                            }
                        });
                    } else {
                        menuItem.setDisable(true);
                    }
                }
            }
        });

        return menu;
    }

    public abstract TCaseIcd getItem();

    public void disable(boolean pDisable) {
        for (MenuItem item : getItems()) {
            item.setDisable(pDisable);
        }
    }

    private String getIcdDescription(TCaseIcd icd) {
        String desc = icd.getIcdcCode();
        CpxIcd icdCat = null;
        if (icd.getIcdcCode() != null) {
            icdCat = CpxIcdCatalog.instance().getByCode(icd.getIcdcCode(), "de", yearOfValidity);
        }
        if (icdCat == null || icdCat.getDescription() == null) {
            return desc + " - " + Lang.getCatalogIcdError(String.valueOf(yearOfValidity));
        }
        return desc + " - " + icdCat.getDescription();
    }
}

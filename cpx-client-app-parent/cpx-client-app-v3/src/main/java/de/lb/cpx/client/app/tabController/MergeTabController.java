/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.tabController;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.job.fx.casemerging.CaseMergingDetailsScene;
import de.lb.cpx.client.app.job.fx.casemerging.tab.CaseMergingOverviewTab;
import de.lb.cpx.client.core.model.TabController;
import de.lb.cpx.client.core.model.fx.combobox.GrouperModelsCombobox;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 */
public abstract class MergeTabController <T extends MergeParentTabScene> extends TabController <T>{
    
    private BooleanProperty disableGrouperModelBox = new SimpleBooleanProperty(false);
    protected GrouperModelsCombobox cbGrouperModel;
    
    public MergeTabController(){
        super();
    }
    
    protected void initGrouperBox(){
        cbGrouperModel = new GrouperModelsCombobox();
       Callback <GDRGModel, Boolean> onUpdateParent = new Callback<GDRGModel, Boolean> () {
            @Override
            public Boolean call(GDRGModel p) {
                reload4GrouperModel();
                return true;
            }
        };
        cbGrouperModel.setOnUpdateParent(onUpdateParent);
        
        disableGrouperModelBox.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if(oldValue == newValue){
                    return;
                }
                cbGrouperModel.setDisable(newValue);
            }
        });
    }
    
    
     public abstract void reload4GrouperModel();
     
    protected void openMergedDetails(CaseMergingDetailsScene mrgMergeScene, CaseMergingOverviewTab drgTab, CaseMergingOverviewTab peppTab, TabPane tabContainer)
    {
        if (mrgMergeScene == null || getScene().getParentTabPane() == null) {
            return;
        }
        try {

            Tab oldTab = findTab4MergeIdent(mrgMergeScene.getMergeIdent(), getScene().getParentTabPane());
            if(oldTab == null){
                TwoLineTab tab = createNewMergingDetailsTab(mrgMergeScene, drgTab, peppTab, tabContainer);
                getScene().getParentTabPane().getTabs().add(tab);
                getScene().getParentTabPane().getSelectionModel().select(tab);
                disableGrouperModelBox.set(true);
                tab.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                    if(newValue == null || newValue != null && oldValue != null && newValue.equals(oldValue)){
                        return;
                    }
                    disableGrouperModelBox.set(newValue);
                }
            });
            }else{
                getScene().getParentTabPane().getSelectionModel().select(oldTab);
            }
        } catch (IllegalArgumentException ex) {
            MainApp.showErrorMessageDialog(ex);
        }
    }
    
    private Tab findTab4MergeIdent(Integer mergeIdent, TabPane parentPane){
        for(Tab tab: parentPane.getTabs()){
            if(tab instanceof TwoLineTab && ((TwoLineTab)tab).getTabType().equals(TwoLineTab.TabType.CASEMERGING)){
               if(((TwoLineTab)tab).getDescription() != null && ((TwoLineTab)tab).getDescription().equals(Lang.getCaseMergingIdObj().getAbbreviation() + ": " + String.valueOf(mergeIdent))){
                   return tab;
               }
           }
        }
        return null;
    }
    
    protected void closeMergeTabs(TabPane parentPane){
        List<Tab> mergingTabs = new ArrayList<>();
        if(getScene() != null && getScene().getParentTabPane() != null){
            ObservableList<Tab> tabs = getScene().getParentTabPane().getTabs();
            if(tabs != null){
                for(Tab tab: tabs){
                    if(tab instanceof TwoLineTab && ((TwoLineTab)tab).getTabType().equals(TwoLineTab.TabType.CASEMERGING)){
                        mergingTabs.add(tab);
                   }
                }
                tabs.removeAll(mergingTabs);
            }
        }
        
    }

    private TwoLineTab createNewMergingDetailsTab(CaseMergingDetailsScene mergingScene, CaseMergingOverviewTab drgTab, CaseMergingOverviewTab peppTab, TabPane tabContainer ) {
        if (mergingScene == null) {
            return null;
        }

        TwoLineTab tab = new TwoLineTab("", Lang.getCaseMerging(), Lang.getCaseMergingIdObj().getAbbreviation() + ": " + mergingScene.getMergeIdent()) {
            @Override
            public TwoLineTab.TabType getTabType() {
                return TwoLineTab.TabType.CASEMERGING;
            }
        };
        tab.setClosable(true);
        tab.setContent(mergingScene.getRoot());
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                CaseTypeEn caseType = mergingScene.getMergedCase().getCsCaseTypeEn();
                switch (caseType) {
                    case DRG:
                        tabContainer.getSelectionModel().select(drgTab);
                        drgTab.deselect();
                        break;
                    case PEPP:
                        tabContainer.getSelectionModel().select(peppTab);
                        peppTab.deselect();
                        break;
                    default:
                        LOG.log(Level.WARNING, "Unknown case type: " + caseType);
                }
            }
        });
        mergingScene.setOnClose(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tab.close();
                tab.getOnClosed().handle(new Event(EventType.ROOT));
                tabContainer.getTabs().remove(tab);
                if (mergingScene.getMergedCase() == null) {
                    return;
                }
                switch (mergingScene.getMergedCase().getCsCaseTypeEn()) {
                    case DRG:
                        drgTab.reload();
                        break;
                    case PEPP:
                        peppTab.reload();
                        break;
                    default:
                        LOG.warning("Case Type not known, reload nothing");
                }
            }
        });

        return tab;
    }


    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.cm.fx.simulation.tables;

import de.lb.cpx.model.TCaseBill;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;


/**
 *
 * @author gerschmann
 */
public class BillFeeView extends Control{

    private static final Logger LOG = Logger.getLogger(BillFeeView.class.getName());

  private final ObservableList<TCaseBill> billList = FXCollections.observableArrayList();    
    public BillFeeView(){
        super();
    }

    public BillFeeView(List<TCaseBill> caseBills){
        super();
        setBills(caseBills);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new BillFeeViewSkin(this);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }
    
    public void setBills(List<TCaseBill> pBills)    {
        billList.addAll(pBills);
    }
    
    public ObservableList<TCaseBill> getBillList() {
    return billList;
    
    }
    
}

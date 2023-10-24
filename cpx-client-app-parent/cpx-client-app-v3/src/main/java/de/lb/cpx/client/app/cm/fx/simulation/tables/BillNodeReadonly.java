/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.cm.fx.simulation.tables;

import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author gerschmann
 */
public class BillNodeReadonly extends VBox{
    
   private LabeledLabel txtBillNumber = new LabeledLabel();
   private LabeledLabel cbBillType = new LabeledLabel();
   private LabeledLabel dtBillDate = new LabeledLabel();
   private Label lblFees = new Label();
   private CaseFeeTableView tabFeeTable = new CaseFeeTableView();
   private LabeledTextArea txtComment = new LabeledTextArea();
   private final TCaseBill currentBill;
   
   public BillNodeReadonly(TCaseBill pBill){
        super();
        currentBill = pBill;
        setMargin(this, new Insets(5));

        setUpLocalization();
        createLayout();
        setValues(pBill);
   }

    private void setUpLocalization() {
        txtBillNumber.setTitle(Lang.getBillNumber());
        cbBillType.setTitle(Lang.getRulesTxtCritRechnungArtDis());
        dtBillDate.setTitle(Lang.getBillDate());
        lblFees.setText(Lang.getBillPositions());
        txtComment.setTitle(Lang.getComment());
        tabFeeTable.setPlaceholder(new Label(Lang.getFeePlaceholder()));
        txtComment.prefHeightProperty().bind(tabFeeTable.heightProperty().divide(3));
        txtComment.getControl().setEditable(false);
    }
    private void createLayout(){
        setSpacing(12.0);
        setFillWidth(true);
        txtBillNumber.setMaxWidth(200);
        cbBillType.setMaxWidth(200);
        
        HBox firstLine = new HBox(this.txtBillNumber, this.cbBillType, this.dtBillDate);
        firstLine.setSpacing(5.0);
        HBox.setHgrow(txtBillNumber, Priority.ALWAYS);
        cbBillType.setPrefWidth(200);
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(this.tabFeeTable);
        mainPane.setBottom(this.txtComment);
        getChildren().addAll(firstLine, this.lblFees, mainPane);
    }

    public void setValues(){
        setValues(currentBill);
    }
    
    private void setValues(TCaseBill pBill){
        if(pBill == null){
            return;
        }
        txtBillNumber.setText(pBill.getBillcNumber() == null?"":pBill.getBillcNumber());
        cbBillType.setText(pBill.getBillcTypeEn() == null?"":pBill.getBillcTypeEn().toString());
        dtBillDate.setText(pBill.getBillcFrom() == null?"":Lang.toDate(pBill.getBillcFrom()));
        txtComment.setText(pBill.getBillComment()==null?"":pBill.getBillComment());
         List<TCaseFee> caseFees = pBill.getCaseFees() == null?new ArrayList<>():new ArrayList<>(pBill.getCaseFees());
        tabFeeTable.setItems(FXCollections.observableArrayList(caseFees));       
    }

    public TCaseBill getBill() {
        return currentBill;
    }

}

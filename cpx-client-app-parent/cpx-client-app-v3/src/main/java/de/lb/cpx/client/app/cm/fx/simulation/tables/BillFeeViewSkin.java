/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.cm.fx.simulation.tables;

import de.lb.cpx.client.core.model.fx.labeled.LabeledTreeView;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author gerschmann
 */
public final class BillFeeViewSkin  extends SkinBase<BillFeeView>{
   
    private LabeledTreeView<BillNodeReadonly> tvCaseBillNodes;

    private ScrollPane  spBills;    
    private VBox vbBillDetails;
    
    public BillFeeViewSkin(BillFeeView pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(createRoot());
        setBills();
    }
    
    private Parent createRoot() throws IOException{
        GridPane layout = FXMLLoader.load(getClass().getResource("/fxml/BillFeeViewFXML.fxml"));
//        tvCaseBillNodes = (LabeledTreeView)layout.lookup("#tvCaseBillNodes");
        vbBillDetails = (VBox)layout.lookup("#vbBillDetails");
        spBills = (ScrollPane)layout.lookup("#spBills");

        VBox vb = new VBox();
        tvCaseBillNodes = new LabeledTreeView<>();
         tvCaseBillNodes.setTitle(Lang.getAddCaseBills());
         tvCaseBillNodes.getStyleClass().add("stay-selected-tree-view");
        VBox.setVgrow(tvCaseBillNodes, Priority.ALWAYS);
        Separator sep = new Separator();
        sep.setPrefWidth(200);
        vb.getChildren().addAll(tvCaseBillNodes, sep);
        spBills.setContent(vb);

        setupTreeView();

        return layout;
    }
 
   private void setupTreeView() {
        TreeItem<BillNodeReadonly> root = new TreeItem<>();
        tvCaseBillNodes.prefHeightProperty().bind(spBills.heightProperty().subtract(5));
        tvCaseBillNodes.getTreeView().setRoot(root);
        tvCaseBillNodes.getTreeView().setShowRoot(false);

        tvCaseBillNodes.getTreeView().setCellFactory((TreeView<BillNodeReadonly> param)->{ 
            TreeCell<BillNodeReadonly> cell = new TreeCell<BillNodeReadonly>(){
               private final Label label = new Label();

                @Override
                protected void updateItem(BillNodeReadonly myObj, boolean empty) {
                    super.updateItem(myObj, empty);
                    if (empty || myObj == null) {
                        setText(null);
                        setGraphic(null);
                        //reset label
                        label.setText(null);
                        return;
                    }
                    setGraphic(label);
                    if (myObj.getBill().getBillcNumber() != null) {  

                        label.setText("");
                        label.setText(myObj.getBill().getBillcNumber()); 

                    } else {
                        label.setText("");
                        label.setText("####");
                    }
                 }
            };
         cell.setPadding(new Insets(8, 0, 8, 0));     
        
        return cell;
    });
    tvCaseBillNodes.getTreeView().getSelectionModel().getSelectedItems().addListener(treeSelectionListener);    
    }

   private final ListChangeListener<TreeItem<BillNodeReadonly>> treeSelectionListener = c -> {
        if (!c.getList().isEmpty()) {
            TreeItem<BillNodeReadonly> selected = c.getList().get(0);
            showBillDetails(selected.getValue());
            selected.getValue();

        }
   };
    private void showBillDetails(BillNodeReadonly value) {
        vbBillDetails.getChildren().clear();
        vbBillDetails.getChildren().add(value);
        value.setValues();  
    }
           
    public void setBills() {
        ObservableList<TCaseBill > oBills = getSkinnable().getBillList();
        if(oBills != null && !oBills.isEmpty()){

            List<TreeItem<BillNodeReadonly>> items = new ArrayList<>();
            for (TCaseBill bill : oBills) {
                items.add(new TreeItem<>(new BillNodeReadonly( bill)));
            }
            tvCaseBillNodes.getTreeView().getRoot().getChildren().addAll(items);
            tvCaseBillNodes.getTreeView().getSelectionModel().selectFirst();
        }
    }
}

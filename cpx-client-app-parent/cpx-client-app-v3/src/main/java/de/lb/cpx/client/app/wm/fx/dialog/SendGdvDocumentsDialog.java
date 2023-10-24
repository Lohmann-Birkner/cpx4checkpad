/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.client.core.model.fx.button.GlyphIconButton;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledListView;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.listview.cell.ListCellWithButton;
import de.lb.cpx.gdv.model.enums.ResponceTypeEn;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmProcess;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 *
 * @author gerschmann
 */
public class SendGdvDocumentsDialog extends FormularDialog<TWmProcess> {

    private static final Logger LOG = Logger.getLogger(SendGdvDocumentsDialog.class.getName());


    protected LabeledComboBox<ResponceTypeEn> cbResponceType;
    protected LabeledTextArea taComment;
    private ProcessServiceFacade facade;
    private TWmProcess currentProcess;
    private LabeledListView<TWmDocument> docProcessList = new LabeledListView<>();
   private LabeledListView<TWmDocument> doc4sendList = new LabeledListView<>();
    BooleanProperty sendAndClose = new SimpleBooleanProperty(true);
    private ObservableList<TWmDocument> docs2process = FXCollections.observableArrayList();
//    private ObservableList<TWmDocument> docs4send = FXCollections.observableArrayList();
    
    public SendGdvDocumentsDialog(ProcessServiceFacade pFacade, TWmProcess pCurrentProcess) {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, "Dokumente versenden");
        facade = pFacade;
        currentProcess = pCurrentProcess;
        docs2process.addAll(pCurrentProcess.getDocuments());
        createLayout();

    }
    
    @Override
    public TWmProcess onSave() {
        List<TWmDocument> sendItems = doc4sendList.getItems();
        if(sendItems != null && !sendItems.isEmpty() && sendAndClose.getValue()){

                facade.sendGdvResponse(currentProcess, sendItems);
            
        }
        return currentProcess;
    }

    private void createLayout() {
        VBox container = new VBox();
        container.setPrefHeight(600);
        getDialogSkin().getButton(ButtonType.OK).setText("Dokumente versenden");
        cbResponceType = new LabeledComboBox<>("Art der Nachricht");
        cbResponceType.getItems().addAll(ResponceTypeEn.getTypes4send());
        cbResponceType.select(ResponceTypeEn.RECHNUNG);

        docProcessList.getListView().getStyleClass().add("stay-selected-list-view");
        docProcessList.setTitle("Dokumente, die dem Vorgang zugefügt sind");
         docProcessList.setCellFactory(new Callback<ListView<TWmDocument>, ListCell<TWmDocument>>(){
            @Override
            public ListCell<TWmDocument> call(ListView<TWmDocument> param) {
                AddButton btn = new AddButton();
                CellWithDefinedButton cell =  new CellWithDefinedButton(btn);
                cell.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        TWmDocument toRemove = docProcessList.getListView().getSelectionModel().getSelectedItem();
                        if (toRemove == null) {
                            return;
                        }
                        docs2process.remove(toRemove);
//                        docs4send.add(toRemove);
                        doc4sendList.addItem(toRemove);
                        doc4sendList.refresh();
                        docProcessList.refresh();

                    }
                });
               return cell;
            }
        
        });
        
        Bindings.bindContent(docProcessList.getItems(), docs2process);
        VBox.setVgrow(docProcessList, Priority.ALWAYS);
        
        
        doc4sendList.getListView().getStyleClass().add("stay-selected-list-view");
        doc4sendList.setTitle("Dokumente, die versendet weden sollen");
        doc4sendList.setCellFactory(new Callback<ListView<TWmDocument>, ListCell<TWmDocument>>(){
            @Override
            public ListCell<TWmDocument> call(ListView<TWmDocument> param) {
                DeleteButton btn = new DeleteButton();
                CellWithDefinedButton cell =  new CellWithDefinedButton(btn);
                cell.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        TWmDocument toRemove = doc4sendList.getListView().getSelectionModel().getSelectedItem();
                        if (toRemove == null) {
                            return;
                        }
//                        docs4send.remove(toRemove);
                        doc4sendList.removeItem(toRemove);
                        docs2process.add(toRemove);
                        doc4sendList.refresh();

                    }
                });
               return cell;
            }
        
        });
        
//       doc4sendList.getListView().getItems().addListener(new ListChangeListener<TWmDocument>(){
//           
//                @Override
//                public void  onChanged(ListChangeListener.Change<? extends TWmDocument> observable) {
//
//               
//               }
//           
//       });
        ValueExtractor.addObservableValueExtractor(c -> c == doc4sendList,
        c -> ((LabeledListView<TWmDocument>) c).emptyProperty()); 

        validationSupport.registerValidator(doc4sendList, new Validator<Object>(){ 
            @Override
            public ValidationResult apply(Control t, Object u){
                ValidationResult result = new ValidationResult();
                result.addErrorIf(t, "keine Dokumente für Senden gewählt", doc4sendList.getItems().isEmpty() );
                return result;               
            }
        });

//        Bindings.bindContent(doc4sendList.getItems(), docs4send);
        
        VBox.setVgrow(doc4sendList, Priority.ALWAYS);

        taComment = new LabeledTextArea("Kommentar");
        container.getChildren().addAll(cbResponceType, docProcessList, doc4sendList, taComment);
        docProcessList.prefHeightProperty().bind(docProcessList.heightProperty());
        doc4sendList.prefHeightProperty().bind(docProcessList.heightProperty());
        taComment.prefHeightProperty().bind(docProcessList.heightProperty());
        container.setPadding(new Insets(10, 0, 0, 0));
        addControlGroup(container);

    }
    protected boolean additionalCheck(){
         List<TWmDocument> sendItems = doc4sendList.getItems();
        boolean ret = checkAlreadySent(sendItems);
        return ret;
    }

    private boolean checkAlreadySent(List<TWmDocument> sendItems) {

        List<TWmDocument> alreadySent = facade.checkSentDocuments(sendItems);
        if(alreadySent != null && !alreadySent.isEmpty()){
            List<String> names = alreadySent.stream().map(TWmDocument::getName).collect(Collectors.toList());
            String nameStr = String.join("\r\n", names);
             Window window = getDialogPane().getScene().getWindow();
            ConfirmDialog dlg = new ConfirmDialog(window, "Folgende Dokumente wurden schon gesendet:\n" + nameStr + "\n Wollen Sie diese noch Mal verschicken?");
            dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType t) {
                    if (t.equals(ButtonType.NO)) {
                        sendAndClose.set(false);
                    }
                }
            });
        }
        return sendAndClose.getValue();
    }
    
    private class CellWithDefinedButton extends ListCellWithButton<TWmDocument>{
        private final GlyphIconButton glyphButton;

         public CellWithDefinedButton(GlyphIconButton btn) {
             super();
             glyphButton = btn;
             setMenuItems(glyphButton);
         }

         public void setOnAction(EventHandler<ActionEvent> pHandler){
             glyphButton.setOnAction(pHandler);
         }

        @Override
        protected void updateItem(TWmDocument t, boolean bln) {
            super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
            if (t == null || bln) {
                setGraphic(null);
                updateMenuItems(null);
                return;
            }
            title.setText(t.getName());

            updateMenuItems();
            setGraphic(graphic);
        }
    }
    

    
}

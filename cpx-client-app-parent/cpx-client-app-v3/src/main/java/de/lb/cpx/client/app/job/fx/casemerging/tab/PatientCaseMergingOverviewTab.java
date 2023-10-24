/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.job.fx.casemerging.tab;

import de.lb.cpx.client.app.service.facade.CaseMergingFacade;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.button.LinkButton;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.client.core.model.task.GroupPatientCasesTask;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author gerschmann
 */
public class PatientCaseMergingOverviewTab extends CaseMergingOverviewTab{

    private ObjectProperty <TCase>currentCase;
    private ObjectProperty <TPatient>currentPatient;
    private HBox messageContent;
    private static final Logger LOG = Logger.getLogger(PatientCaseMergingOverviewTab.class.getName());
    private LoadingLayout loadingLayout;
    private BooleanProperty isGrouperActive = new SimpleBooleanProperty(false);
    
    public PatientCaseMergingOverviewTab(CaseTypeEn pGrpresType, String pTitle, CaseMergingFacade.MERGELIST_TYPE pMergeListType) throws CpxIllegalArgumentException {
        super(pGrpresType,  pTitle,  pMergeListType);
        init();
    }
    
    public PatientCaseMergingOverviewTab(CaseTypeEn pGrpresType) throws CpxIllegalArgumentException {
        super(pGrpresType);
        init();
    }
    
    private void init(){

        isGrouperActive.addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(oldValue.equals(newValue)){
                        return;
                    }
                if(newValue){
 setLoadingLayout();
//                    Platform.runLater( new Runnable() {
//                         @Override
//                         public void run() {
//                            setLoadingLayout();
//                         }
//                     });
//                }
                }else{
                    
                    reload4GrouperModel();  
                }
            }
        });

    }
    

    public void setCurrentPatient(TPatient pPatient, TCase pCase) {
        setCurrentPatient(pPatient);
        setCurrentCase(pCase);
        facade.setCurrentPatient(pPatient);
        if(!facade.checkDatabaseRequirements(CpxClientConfig.instance().getSelectedGrouper(), 
                grpresType, 
                pPatient.getId())){
            showMessageContent();

        }else{
            mdMerge.setCurrentPatient(pPatient);
            mdMerge.reload();
        }
    }

    protected void showMessageContent()throws CpxIllegalArgumentException {
        if (resultContent != null && contentBox.getChildren().contains(resultContent)) {
            contentBox.getChildren().remove(resultContent);
        }
        if (loadingContent != null && contentBox.getChildren().contains(loadingContent)) {
            contentBox.getChildren().remove(loadingContent);
        }
        if (loadingLayout != null && contentBox.getChildren().contains(loadingLayout)) {
            contentBox.getChildren().remove(loadingLayout);
        }
        if(!contentBox.getChildren().contains(getMessageContent())){
            contentBox.getChildren().add(getMessageContent());
        }
        btnStartStop.setDisable(true);
    }
    
    protected Node getMessageContent(){
        if(messageContent == null){
            LinkButton btn = new LinkButton();
//            btn.setStyle("-fx-padding:0;-fx-border-width: 30 0 0 -30");
            btn.getGlyph().setFontSize(15);
//            btn.setAlignment(Pos.BOTTOM_LEFT);
            
            btn.setTooltip(new Tooltip("Alle Fälle des Patienten " + getCurrentPatient().getPatNumber() + " mit dem aktuellen Grouper prüfen"));
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    performGroup4CurrentCase();
                }
            });

            Text text1 = new Text("Für den Patient " + getCurrentPatient().getPatNumber() + " wurden " + getCurrentPatient().getCases().size() + " Fälle gefunden.\n");

            Node[] text= new Node[]{text1,
            new Text("Da nicht alle Fälle mit dem aktuellen Grouper gegroupt wurden, ist die Anzeige nicht möglich.\n"),
            new Text( "Sie können jetzt alle Fälle des Patienten mit dem aktuellen Grouper prüfen."),
            new Text("\r\n\n"),
            btn};
            TextFlow txt = new TextFlow(text);
//            Label txt = new Label("Für den Patient " + getCurrentPatient().getPatNumber() + " wurden " + getCurrentPatient().getCases().size() + " Fälle gefunden.\n"
//            + "Da nicht alle Fälle mit dem aktuellen Grouper gegroupt wurden, ist die Anzeige nicht möglich.\n"
//            + "Sie können jetzt alle Fälle des Patienten mit dem aktuellen Grouper prüfen.\n");
            txt.getStyleClass().add("cpx-nocontent-label");
            messageContent = new HBox();
            messageContent.setAlignment(Pos.CENTER);
            messageContent.setPadding(new Insets(20,0,0,0));
            messageContent.setPrefWidth(USE_COMPUTED_SIZE);
            messageContent.setMinWidth(USE_COMPUTED_SIZE);
            messageContent.setMaxWidth(USE_COMPUTED_SIZE);
//            messageContent.setPrefHeight(200);
//            messageContent.setMinHeight(200);
//            messageContent.setMaxHeight(200);
            messageContent.setAlignment(Pos.CENTER);
            messageContent.getChildren().add(txt);
//            LinkButton btn = new LinkButton();
            messageContent.getChildren().add(btn);
        }
        return messageContent;
    }
    

    private ObjectProperty<TCase> currentCase(){
        if(currentCase == null){
            currentCase = new SimpleObjectProperty<>();
        }
        return currentCase;
    }
    
    public TCase getCurrentCase(){
        return currentCase().get();
    }
    
    public void setCurrentCase(TCase pCase){
        currentCase().set(pCase);
    }

    private ObjectProperty<TPatient> currentPatient(){
        if(currentPatient == null){
            currentPatient = new SimpleObjectProperty<>();
        }
        return currentPatient;
    }
    
    public TPatient getCurrentPatient(){
        return currentPatient().get();
    }
    
    public void setCurrentPatient(TPatient pPatient){
        currentPatient().set(pPatient);
    }
    
    private void performGroup4CurrentCase(){
        isGrouperActive.set(true);
        CpxTask<List<TGroupingResults>> cpxTask = new GroupPatientCasesTask(getCurrentPatient());
        cpxTask.start();
        List<TGroupingResults> gResult = null;
        try {
            gResult = cpxTask.get();
            isGrouperActive.set(false);
            reload4GrouperModel();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
       
//        reload4GrouperModel();
    }
    
    @Override
    public void reload4GrouperModel() {
//        setCurrentPatient(getCurrentPatient(), getCurrentCase());

        facade.setCurrentPatient(getCurrentPatient());
        if(!facade.checkDatabaseRequirements(CpxClientConfig.instance().getSelectedGrouper(), 
                grpresType, 
                getCurrentPatient().getId())){
            showMessageContent();

        }else{
            mdMerge.setCurrentPatient(getCurrentPatient());
            mdMerge.reload();
            refreshResultContent();
        }
    }
    
    protected void refreshResultContent() throws CpxIllegalArgumentException {
        if (messageContent != null && contentBox.getChildren().contains(messageContent)) {
            contentBox.getChildren().remove(messageContent);
        }
       if (resultContent != null && contentBox.getChildren().contains(resultContent)) {
            contentBox.getChildren().remove(resultContent);
        }
        if (loadingContent != null && contentBox.getChildren().contains(loadingContent)) {
            contentBox.getChildren().remove(loadingContent);
        }
       if (loadingLayout != null && contentBox.getChildren().contains(loadingLayout)) {
            contentBox.getChildren().remove(loadingLayout);
        }
        super.showResultContent();
        btnStartStop.setDisable(false);
    }

    private void setLoadingLayout() {
        if (messageContent != null && contentBox.getChildren().contains(messageContent)) {
            contentBox.getChildren().remove(messageContent);
        }
       if (resultContent != null && contentBox.getChildren().contains(resultContent)) {
            contentBox.getChildren().remove(resultContent);
        }
        if (loadingContent != null && contentBox.getChildren().contains(loadingContent)) {
            contentBox.getChildren().remove(loadingContent);
        }
       if (loadingLayout != null && contentBox.getChildren().contains(loadingLayout)) {
            contentBox.getChildren().remove(loadingLayout);
        }
       loadingLayout = new LoadingLayout();
       contentBox.getChildren().add(loadingLayout);
    }

    private class LoadingLayout extends VBox {
        


        public LoadingLayout() {
            ProgressIndicator pi = new ProgressIndicator(-1);
            setAlignment(Pos.CENTER);
            pi.setMinHeight(40d);
            pi.getStyleClass().add("async-progress-indicator");
            Label status = new Label(Lang.getPleaseWait());
            getChildren().addAll(pi, status);
            setSpacing(10.0);
////            parentProperty().addListener(new ChangeListener<Parent>() {
////                @Override
////                public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
////                    if (newValue != null && newValue instanceof VBox) {
////                        pi.minWidthProperty().bind(((Region) newValue).widthProperty().divide(5));
////                    }
////                }
////            });
        }

    }


}

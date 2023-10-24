/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.job.fx.casemerging.tab;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.job.fx.casemerging.CaseMergingDetailsScene;
import de.lb.cpx.client.app.job.fx.casemerging.tab.sections.MrgOvCaseDataSection;
import de.lb.cpx.client.app.job.fx.casemerging.tab.sections.MrgOvDetectedReasonsSection;
import de.lb.cpx.client.app.service.facade.CaseMergingFacade;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.masterdetail.TableViewMasterDetailPane;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import javax.ejb.EJBException;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author gerschmann
 */
public class CaseMergingMasterDetail  extends TableViewMasterDetailPane<TCaseMergeMapping> {
    private Callback<CaseMergingDetailsScene, Void> openMergeDetailsCallback;
    private static final Logger LOG = Logger.getLogger(CaseMergingMasterDetail.class.getName());
    private final CaseMergingFacade facade;
    private final Map<Integer, MergeObject> mapMrgObj = new HashMap<>();
    private ObjectProperty <TPatient> currentPatientProperty;
    private BooleanProperty disableSimulate = new SimpleBooleanProperty(true);
    private static final Double MERGE_ID_WIDTH = 100.0;
    private  MenuItem itMerge;
    private MenuItem itMergeAndSave;
        //cell factory to handle overrun behavior
    private Callback<TableColumn<TCaseMergeMapping, String>, TableCell<TCaseMergeMapping, String>> overrunCellFactory
            = (TableColumn<TCaseMergeMapping, String> param) -> {
                return new TableCell<TCaseMergeMapping, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                if (item == null || empty) {
                    setGraphic(null);
                    return;
                }
                Label label = new Label(item);
                setGraphic(label);
                OverrunHelper.addInfoTooltip(label);
            }
        };
    };
        
        
    public void setOnOpenMergeDetails(Callback<CaseMergingDetailsScene, Void> pCallback) {
        openMergeDetailsCallback = pCallback;
    }
 

        /**
         * construct new instance ResizePolicy is set to uncontrained
         */
        public CaseMergingMasterDetail(CaseTypeEn pType,  final CaseMergingFacade facade) {
            super(new AsyncTableView<TCaseMergeMapping>() {
                @Override
                public Future<List<TCaseMergeMapping>> getFuture() {
                    try {
                        return new AsyncResult<>(facade.getObservableMergingCases());
                    } catch (CpxIllegalArgumentException ex) {
                        LOG.log(Level.SEVERE, "Can not load merge data, reason: {0}", ex);
                    }
                    return new AsyncResult<>(new ArrayList<>());
                }
            }, TableView.UNCONSTRAINED_RESIZE_POLICY);

            this.facade = facade;
            getSelectedItemProperty().addListener(new ChangeListener<TCaseMergeMapping>() {
                @Override
                public void changed(ObservableValue<? extends TCaseMergeMapping> observable, TCaseMergeMapping oldValue, TCaseMergeMapping newValue) {
                    try {
                        
                        setDetail(createDetailAsync(newValue));

                    } catch (CpxIllegalArgumentException ex) {
                        Logger.getLogger(CaseMergingOverviewTab.class.getName()).log(Level.SEVERE, "Was not able to create detail", ex);
                    }
                }
            });
            getPatientProperty().addListener(new ChangeListener<TPatient>() {
                 @Override
                 public void changed(ObservableValue<? extends TPatient> observable, TPatient oldValue, TPatient newValue) {
                     if (newValue != null ) {
                         try {
                             facade.getObservableMergingCases();
                             reload();
                         } catch (CpxIllegalArgumentException ex) {
                             Logger.getLogger(CaseMergingOverviewTab.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     } 
                 }
             });

            setPlaceholderText(Lang.getCaseMergingTablePlaceholder());
            getTableView().getStyleClass().add("stay-selected-table-view");
            setDetail(createDetailAsync(null));

//            setDividerPosition(0, 0.8);

            switch (pType) {
                case DRG:
                    setDrgColumns();
                    break;
                case PEPP:
                    setPeppColumns();
                    break;
                default:
                    LOG.warning("can not set items in overview list! unknown grprestype " + pType.name());
            }
            itMergeAndSave = new MenuItem(Lang.getCaseMergingDoMergeAndSave());
//            itMergeAndSave.setDisable(true);
            itMergeAndSave.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    if (getSelectedItem() == null) {
                        return;
                    }
                    performMergeAndSave(getSelectedItem().getMrgMergeIdent());
                    getTableView().getSelectionModel().clearSelection();
                }
            });
            
            itMerge = new MenuItem(Lang.getCaseMergingDoMerge());
            itMerge.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    if (getSelectedItem() == null) {
                        disableSimulate.set(true);
                        return;
                    }
                    disableSimulate.set(getSelectedItem().getMrgMergeIdent() == 0);
                    if (openMergeDetailsCallback != null) {
                        try {
                            
                            openMergeDetailsCallback.call(createMergedDetails(getSelectedItem().getMrgMergeIdent()));
                        } catch (CpxIllegalArgumentException ex) {
                            Logger.getLogger(CaseMergingOverviewTab.class.getName()).log(Level.SEVERE, "Was not able to create merged details", ex);
                        }
                    }
                }
            });
            MenuItem copyCaseNumber = new MenuItem("Fallnummer kopieren");
//            copyCaseNumber.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLIPBOARD));
            copyCaseNumber.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    copySelectedCaseNumber();
                }
            });

            MenuItem copyPatientNumber = new MenuItem("Patientennummer kopieren");
//            copyCaseNumber.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLIPBOARD));
            copyPatientNumber.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    copySelectedPatientNumber();
                }
            });

            if(facade.isSaveCaseMergingAllowed()){
                getRowContextMenu().getItems().addAll(copyCaseNumber, copyPatientNumber, itMerge, itMergeAndSave);
            }else{
                 getRowContextMenu().getItems().addAll(copyCaseNumber, copyPatientNumber, itMerge);
            }
            if (getTableView() instanceof AsyncTableView) {
                ((AsyncTableView)getTableView()).setRowContextMenu(getRowContextMenu());
            }
            itMerge.disableProperty().bind(getDisableSimulate());
            itMergeAndSave.disableProperty().bind(getDisableSimulate());
        }
        private Parent createDetailAsync(TCaseMergeMapping pMapping ) throws CpxIllegalArgumentException{
            AsyncPane<Parent> pane = new AsyncPane<>(true) {
                @Override
                public Parent loadContent() {
                    return createDetail(pMapping);
                }
            };
            return pane;
        }
        
        private void copySelectedCaseNumber() {
            if(getSelectedItem() != null && getSelectedItem().getCaseByMergeMemberCaseId() != null){
                ClipboardEnabler.copyToClipboard(null, getSelectedItem().getCaseByMergeMemberCaseId().getCsCaseNumber());
            }
        }

        
        private void copySelectedPatientNumber() {
            if(getSelectedItem() != null && getSelectedItem().getCaseByMergeMemberCaseId() != null && getSelectedItem().getCaseByMergeMemberCaseId().getPatient() != null){
                ClipboardEnabler.copyToClipboard(null, getSelectedItem().getCaseByMergeMemberCaseId().getPatient().getPatNumber());
            }
        }

        private Parent createDetail(TCaseMergeMapping pMapping) throws CpxIllegalArgumentException {
            if (pMapping == null || pMapping.getMrgMergeIdent() == 0) {
                return createMessageBox("Kein Zusammenführungsvorschlag vorhanden");
            }
            long startTotal = System.currentTimeMillis();
            long start = System.currentTimeMillis();
            List<TCaseMergeMapping> mappings = facade.getCaseMergeByMergeId(pMapping.getMrgMergeIdent());
            TCase mrg = null;
            TGroupingResults grpRes = null;
            if (!mapMrgObj.containsKey(pMapping.getMrgMergeIdent())) {
                try {
                    mrg = facade.mergeById(pMapping.getMrgMergeIdent());
                    if (mrg == null) {
                        MainApp.showWarningMessageDialog("Fallzusammenführung ist an dieser Stelle nicht möglich.\nFühren Sie bitte noch einmal 'Fälle ermitteln' durch!");
                         disableSimulate.set(true);
                        return new HBox();
                    }
                    grpRes = facade.getGrouperResult(mrg);
                } catch (IllegalArgumentException ex) {
                     disableSimulate.set(true);
                    MainApp.showErrorMessageDialog(ex + " \n Ident:" + pMapping.getMrgMergeIdent());
                }
                disableSimulate.set(false);
                mapMrgObj.put(pMapping.getMrgMergeIdent(), new MergeObject(mrg, grpRes));
                LOG.info("time to merge and group " + (System.currentTimeMillis() - start) + " ms");
            } else {
                disableSimulate.set(false);
                MergeObject mrgObj = mapMrgObj.get(pMapping.getMrgMergeIdent());
                mrg = mrgObj.getMergedCase();
                grpRes = mrgObj.getGrResults();
            }
            if (mrg == null) {
                return createMessageBox("Keine Fallzusammenführung möglich.\n Die Kodierung der vorgeschlagenen Fälle muss überprüft werden");
            }
            if (grpRes == null) {
               return createMessageBox("Keine Fallzusammenführung möglich.\n Die Kodierung der vorgeschlagenen Fälle muss überprüft werden");
            }
            MrgOvDetectedReasonsSection conLayout = new MrgOvDetectedReasonsSection(pMapping);
            HBox.setHgrow(conLayout.getRoot(), Priority.ALWAYS);

            MrgOvCaseDataSection grLayout = new MrgOvCaseDataSection(pMapping.getMrgMergeIdent(), mrg, grpRes, mappings, facade);
            HBox.setHgrow(grLayout.getRoot(), Priority.ALWAYS);

            HBox content = new HBox(conLayout.getRoot(), grLayout.getRoot());
            VBox.setVgrow(content, Priority.NEVER);

//            content.setFillHeight(true);
            content.setSpacing(10.0);
            
//            content.maxHeightProperty().bind(grLayout.maxHeightProperty());
//            getDetailPane().maxHeightProperty().bind(grLayout.maxHeightProperty());

            LOG.info("render details in " + (System.currentTimeMillis() - startTotal) + " ms");
            return content;
        }
        
        private Parent createMessageBox(String pMessage){
            disableSimulate.set(true);
            Label txt = new Label(pMessage);
            HBox box = new HBox();
            txt.getStyleClass().add("cpx-nocontent-label");
            txt.setAlignment(Pos.CENTER);
            box.getChildren().add(txt);
            box.setPrefWidth(USE_COMPUTED_SIZE);
            box.setMinWidth(USE_COMPUTED_SIZE);
            box.setMaxWidth(USE_COMPUTED_SIZE);
            box.setAlignment(Pos.CENTER);
            return box;
            
        }

        public BooleanProperty getDisableSimulate(){
            return disableSimulate;
            
        }
        public void reload() {
            if (getTableView() instanceof AsyncTableView) {
                ((AsyncTableView) getTableView()).reload();
            }
        }
        
        public void performMergeAndSave(Integer ident) {
        //remove entries from list
        //list linked with table view - resulting in ui update
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // check whether case with cs_number+ _m already exists.
                    String caseNr = facade.checkHasMerged(ident);

                    if(caseNr != null ){
                        ConfirmDialog dialog = new ConfirmDialog(MainApp.getWindow(),
                                "Ein zusammengeführte Fall mit dem führenden Fall " + caseNr + "ist in der Datenbak schon vorhanden."
                                + "\nWenn sie die Zusammenführung noch Mal durchführen wollen, wird der Ergebnis als neue lokale Version zu dem Fall  "
                                + caseNr + "_m zugefügt." );
                        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (ButtonType.YES.equals(t)) {
                                doMerge(ident);
                            }else{
                                return;
                            }
                        }
                        });
                    }else{
                        doMerge(ident);
                    }
                } catch (EJBException ex) {
                    LOG.log(Level.SEVERE, "Case merging failed", ex);
                    MainApp.showErrorMessageDialog("Fallzusammenführung fehlgeschlagen!");
                }
                reload();
            }
        });
        
        
    }

    private void doMerge(int pIdent) throws EJBException{

        facade.removeFromMergeCases(pIdent);
        if( facade.mergeAndPersistById(pIdent)){
            MainApp.showInfoMessageDialog( "Fallzusammenführung für Zusf. ID " + pIdent + " erfolgreich");
        }
        if(getCurrentPatient() != null){
           facade.reloadMergeCaseList();
        }


    }

        private void setDrgColumns() {
            ColorIndicatorColumn colColor = new ColorIndicatorColumn();
            getColumns().add(colColor);
            MergeIdColumn colMergeId = new MergeIdColumn();
            colMergeId.setMinWidth(MERGE_ID_WIDTH);
            colMergeId.setMaxWidth(MERGE_ID_WIDTH);
            getColumns().add(colMergeId);
            CaseNumberColumn colCaseNumber = new CaseNumberColumn();
            getColumns().add(colCaseNumber);
            HospitalColumn colHosNumber = new HospitalColumn();
            getColumns().add(colHosNumber);
            InsuranceColumn colInsComp = new InsuranceColumn();
            getColumns().add(colInsComp);
            DrgColumn colDrgCode = new DrgColumn();
            getColumns().add(colDrgCode);
//TODO: mdc, ogvd, partition    
            MdcColumn colMdcCode = new MdcColumn();
            getColumns().add(colMdcCode);
            HtpColumn colHtp = new HtpColumn();
            getColumns().add(colHtp);
            PartitionColumn colPartitionCode = new PartitionColumn();
            getColumns().add(colPartitionCode);

            AdmissionDateColumn colAdmDate = new AdmissionDateColumn();
            getColumns().add(colAdmDate);
            DischargeDateColumn colDisDate = new DischargeDateColumn();
            getColumns().add(colDisDate);
        
            AdmissionReasonColumn colAdmReason = new AdmissionReasonColumn();
            getColumns().add(colAdmReason);
            colAdmReason.prefWidthProperty().bind(
                    widthProperty()
                            .subtract(colAdmDate.widthProperty())
                            .subtract(colCaseNumber.widthProperty())
                            .subtract(colDisDate.widthProperty())
                            .subtract(colDrgCode.widthProperty())
                            .subtract(colMdcCode.widthProperty())
                            .subtract(colHtp.widthProperty())
                            .subtract(colPartitionCode.widthProperty())
                            .subtract(colHosNumber.widthProperty())
                            .subtract(colInsComp.widthProperty())
                            .subtract(colMergeId.widthProperty())
                            .subtract(colColor.widthProperty())
                            .subtract(5)
            );
        }

        private void setPeppColumns() {
            ColorIndicatorColumn colColor = new ColorIndicatorColumn();
            getColumns().add(colColor);
            MergeIdColumn colMergeId = new MergeIdColumn();
            colMergeId.setMinWidth(MERGE_ID_WIDTH);
            colMergeId.setMaxWidth(MERGE_ID_WIDTH);
            getColumns().add(colMergeId);
            CaseNumberColumn colCaseNumber = new CaseNumberColumn();
            getColumns().add(colCaseNumber);
            HospitalColumn colHosNumber = new HospitalColumn();
            getColumns().add(colHosNumber);
            InsuranceColumn colInsComp = new InsuranceColumn();
            getColumns().add(colInsComp);
            PeppColumn colDrgCode = new PeppColumn();
            getColumns().add(colDrgCode);
            SkColumn colSkCode = new SkColumn();
            getColumns().add(colSkCode);
            AdmissionDateColumn colAdmDate = new AdmissionDateColumn();
            getColumns().add(colAdmDate);
            DischargeDateColumn colDisDate = new DischargeDateColumn();
            getColumns().add(colDisDate);
            AdmissionReasonColumn colAdmReason = new AdmissionReasonColumn();
            getColumns().add(colAdmReason);

            colAdmReason.prefWidthProperty().bind(
                    widthProperty()
                            .subtract(colAdmDate.widthProperty())
                            .subtract(colCaseNumber.widthProperty())
                            .subtract(colDisDate.widthProperty())
                            .subtract(colDrgCode.widthProperty())
                            .subtract(colHosNumber.widthProperty())
                            .subtract(colInsComp.widthProperty())
                            .subtract(colSkCode.widthProperty())
                            .subtract(colColor.widthProperty())
                            .subtract(colMergeId.widthProperty())
                            .subtract(5)
            );
        }

    public TPatient getPatient() {
        return getPatientProperty().get();
    }

        /*
        * Column definition
         */
        private class ColorIndicatorColumn extends TableColumn<TCaseMergeMapping, BRACKETTYPE> {

            ColorIndicatorColumn() {
                super("");
                setSortable(false);
                setResizable(false);
                setMaxWidth(15.0);
                setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseMergeMapping, BRACKETTYPE>, ObservableValue<BRACKETTYPE>>() {
                    @Override
                    public ObservableValue<BRACKETTYPE> call(TableColumn.CellDataFeatures<TCaseMergeMapping, BRACKETTYPE> param) {
                        if (param.getValue() == null || param.getValue().getMrgMergeIdent() == 0) {
                            return null;
                        }

                        ObjectProperty<BRACKETTYPE> typeProperty = new SimpleObjectProperty<>();
                        if (getTableItems().indexOf(param.getValue()) == 0) {
                            typeProperty.setValue(BRACKETTYPE.OPEN);
                            return typeProperty;
                        }
                        if (getTableItems().indexOf(param.getValue()) == getTableItems().size() - 1) {
                            typeProperty.setValue(BRACKETTYPE.CLOSE);
                            return typeProperty;
                        }
                        int index = getTableItems().indexOf(param.getValue());
                        TCaseMergeMapping prev = getTableItems().get(index - 1);
                        TCaseMergeMapping next = getTableItems().get(index + 1);

                        if (Objects.equals(prev.getMrgMergeIdent(), param.getValue().getMrgMergeIdent()) && Objects.equals(next.getMrgMergeIdent(), param.getValue().getMrgMergeIdent())) {
                            typeProperty.setValue(BRACKETTYPE.CONTINUE);
                            return typeProperty;
                        }
                        if (!Objects.equals(prev.getMrgMergeIdent(), param.getValue().getMrgMergeIdent()) && Objects.equals(next.getMrgMergeIdent(), param.getValue().getMrgMergeIdent())) {
                            typeProperty.setValue(BRACKETTYPE.OPEN);
                            return typeProperty;
                        }
                        if (Objects.equals(prev.getMrgMergeIdent(), param.getValue().getMrgMergeIdent()) && !Objects.equals(next.getMrgMergeIdent(), param.getValue().getMrgMergeIdent())) {
                            typeProperty.setValue(BRACKETTYPE.CLOSE);
                            return typeProperty;
                        }
                        return typeProperty;
                    }
                });
                setCellFactory(new Callback<TableColumn<TCaseMergeMapping, BRACKETTYPE>, TableCell<TCaseMergeMapping, BRACKETTYPE>>() {
                    @Override
                    public TableCell<TCaseMergeMapping, BRACKETTYPE> call(TableColumn<TCaseMergeMapping, BRACKETTYPE> p) {
                        TableCell<TCaseMergeMapping, BRACKETTYPE> cell = new TableCell<TCaseMergeMapping, BRACKETTYPE>() {
                            @Override
                            protected void updateItem(BRACKETTYPE t, boolean bln) {
                                super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
                                if (t == null || bln) {
                                    setGraphic(null);
                                    return;
                                }
                                switch (t) {
                                    case OPEN:
                                        setGraphic(new BracketOpen(4.5f, 8));
                                        break;
                                    case CONTINUE:
                                        setGraphic(new BracketContinue(4.5f));
                                        break;
                                    case CLOSE:
                                        setGraphic(new BracketClose(4.5f, 8));
                                        break;
                                    default:
                                        setGraphic(null);
                                }
                            }
                        };
                        return cell;
                    }
                });
            }

        }

    public ObjectProperty<TPatient> getPatientProperty(){
        if(currentPatientProperty == null){
            currentPatientProperty = new SimpleObjectProperty<>();
        }
        return currentPatientProperty;
    }
    
    public TPatient getCurrentPatient(){
        return getPatientProperty().get();
    }
    
    public void setCurrentPatient(TPatient pPatient){
        getPatientProperty().set(pPatient);
    }

        private class MergeIdColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            MergeIdColumn() {
                super(Lang.getCaseMergingIdObj().getAbbreviation());
                setSortable(false);
                setMinWidth(50.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                return pValue.getMrgMergeIdent() == 0?"":String.valueOf(pValue.getMrgMergeIdent());
            }

        }

        private class CaseNumberColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            CaseNumberColumn() {
                super(Lang.getCaseNumber());
                setSortable(false);
                setMinWidth(200.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                return pValue.getCaseByMergeMemberCaseId().getCsCaseNumber();
            }

        }

        private class InsuranceColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            InsuranceColumn() {
                super(Lang.getInsurance());
                setSortable(false);
                setMinWidth(200.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                TPatient pat = facade.loadPatient(pValue.getCaseByMergeMemberCaseId().getPatient().getId());
                return pat.getPatInsuranceActual() != null ? pat.getPatInsuranceActual().getInsInsuranceCompany() : "";
            }

        }

        private class HospitalColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            HospitalColumn() {
                super(Lang.getHospitalIdentifierObj().getAbbreviation());
                setSortable(false);
                setMinWidth(100.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                return String.valueOf(pValue.getCaseByMergeMemberCaseId().getCsHospitalIdent());
            }
        }

        private class AdmissionDateColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            AdmissionDateColumn() {
                super(Lang.getAdmissionDate());
                setSortable(false);
                setMinWidth(100.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                return Lang.toDate(pValue.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmissionDate());
            }
        }

        private class AdmissionReasonColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            AdmissionReasonColumn() {
                super(Lang.getAdmissionReason());
                setSortable(false);
                setMinWidth(55.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                return String.valueOf(pValue.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmReason12En().getTranslation().getValue());
            }
        }

        private class DrgColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            DrgColumn() {
                super(Lang.getDRGObj().getAbbreviation());
                setSortable(false);
                setMinWidth(55.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                if(pValue.getGrpresId() == null){
                    return "";
                }

                return pValue.getGrpresId() == null?"":pValue.getGrpresId().getGrpresCode();
            }
        }

        private class MdcColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            MdcColumn() {
                super("MDC");
                setSortable(false);
                setMinWidth(55.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
             if(pValue.getGrpresId() == null){
                    return "";
                }
                return pValue.getGrpresId() == null?"":(pValue.getGrpresId().getGrpresGroup() == null?"":pValue.getGrpresId().getGrpresGroup().getId());
            }
        }

        private class HtpColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            HtpColumn() {
                super(Lang.getRulesTxtIntervalOgvd());
                setSortable(false);
                setMinWidth(55.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                if(pValue.getGrpresId() == null){
                    return "";
                }

                if(pValue.getGrpresId() instanceof TCaseDrg){
                    return String.valueOf(((TCaseDrg)pValue.getGrpresId()).getDrgcHtp());
                }
                return "";
            }
        }

        private class PartitionColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            PartitionColumn() {
                super("Partition");
                setSortable(false);
                setMinWidth(55.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                if(pValue.getGrpresId() == null){
                    return "";
                }
                if(pValue.getGrpresId() instanceof TCaseDrg){
                    return ((TCaseDrg)pValue.getGrpresId()).getDrgcPartitionEn() == null?"":((TCaseDrg)pValue.getGrpresId()).getDrgcPartitionEn().name();
                }
                return "";
            }
        }

        private class PeppColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            PeppColumn() {
                super(Lang.getPEPPObj().getAbbreviation());
                setSortable(false);
                setMinWidth(55.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                return String.valueOf(pValue.getGrpresId().getGrpresCode());
            }
        }

        private class DischargeDateColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            DischargeDateColumn() {
                super(Lang.getDischargeDate());
                setSortable(false);
                setMinWidth(110.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
                return Lang.toDate(pValue.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdDischargeDate());
            }
        }
 
        private class SkColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {

            SkColumn() {
                super("SK");
                setSortable(false);
                setMinWidth(55.0);
                setCellFactory(overrunCellFactory);
            }

            @Override
            public String extractValue(TCaseMergeMapping pValue) {
             if(pValue.getGrpresId() == null){
                    return "";
                }
                return pValue.getGrpresId() == null?"":(pValue.getGrpresId().getGrpresGroup() == null?"":pValue.getGrpresId().getGrpresGroup().getId());
            }
        }

    private class BracketContinue extends HBox {

        private final Path path = new Path();
        private final VLineTo vline;

        BracketContinue(double pStartX) {
            path.setStroke(Color.BLACK);
            setMinWidth(10);
            setMaxWidth(10);
            setFillHeight(true);
            setAlignment(Pos.CENTER_LEFT);
            MoveTo mt = new MoveTo(pStartX, 0);
            path.getElements().add(mt);
            vline = new VLineTo();
            path.getElements().add(vline);

            vline.yProperty().bind(heightProperty().subtract(2));
            getChildren().add(path);
        }
    }

    private class BracketOpen extends HBox {

        private final Path path = new Path();

        BracketOpen(double pStartX, double pMarginTop) {
            path.setStroke(Color.BLACK);
            setMinWidth(10);
            setMaxWidth(10);
            setFillHeight(true);
            setAlignment(Pos.CENTER_LEFT);
            MoveTo mt = new MoveTo(pStartX + 2, 0);
            path.getElements().add(mt);
            HLineTo topLine = new HLineTo(-2);
            path.getElements().add(topLine);
            VLineTo vline = new VLineTo();
            path.getElements().add(vline);

            vline.yProperty().bind(heightProperty().subtract(2 + pMarginTop));
            HBox.setMargin(path, new Insets(pMarginTop, 0, 0, 0));

            getChildren().add(path);
        }
    }

    private class BracketClose extends HBox {

        private final Path path = new Path();

        BracketClose(double pStartX, double pMarginBottom) {
            setMinWidth(10);
            setMaxWidth(10);
            setFillHeight(true);
            setAlignment(Pos.CENTER_LEFT);
            MoveTo mt = new MoveTo(pStartX, 0); // X-axis parameter starts from half of the square width
            path.getElements().add(mt);
            path.setStroke(Color.BLACK);
            VLineTo vline = new VLineTo();
            path.getElements().add(vline);

            HLineTo bottomLine = new HLineTo(pStartX + 8);
            bottomLine.setAbsolute(true);
            path.getElements().add(bottomLine);
            vline.yProperty().bind(heightProperty().subtract(2 + pMarginBottom));
            HBox.setMargin(path, new Insets(0, 0, pMarginBottom, 0));
            getChildren().add(path);
        }
    }

    
    private CaseMergingDetailsScene createMergedDetails(Integer pMergeId) throws CpxIllegalArgumentException {
        try {
            MergeObject mrgObj = mapMrgObj.get(pMergeId);
            CaseMergingDetailsScene scene = new CaseMergingDetailsScene(facade, pMergeId, mrgObj.getMergedCase(), mrgObj.getGrResults());
            return scene;
        } catch (IOException | IllegalArgumentException ex) {
            MainApp.showErrorMessageDialog(ex);
        }
        return null;
    }
    
    public CaseMergingDetailsScene createMergedDetails() throws CpxIllegalArgumentException {
        return createMergedDetails(getSelectedItem().getMrgMergeIdent());
    }

    private class MergeObject {

        private final TCase mergedCase;
        private final TGroupingResults grResults;

        MergeObject(TCase pMergedCase, TGroupingResults pResults) {
            mergedCase = pMergedCase;
            grResults = pResults;
        }

        public TCase getMergedCase() {
            return mergedCase;
        }

        public TGroupingResults getGrResults() {
            return grResults;
        }

    }

    private enum BRACKETTYPE {
        OPEN, CLOSE, CONTINUE;
    }
}
    


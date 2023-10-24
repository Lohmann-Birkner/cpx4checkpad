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
package de.lb.cpx.client.app.wm.fx.process.completion.gridpane;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.util.CaseDetailsCommentHelper;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessHospitalFinalisation;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.getRowIndex;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * GirdPane to display version data in process completion shows drg results / cw
 * values / supplementary fee / revenue / los / version / comment displays 2
 * diffenrent case details (Case versions)
 *
 * @author wilde
 */
public final class VersionOverviewGridPane extends GridPane {

    private static final Logger LOG = Logger.getLogger(VersionOverviewGridPane.class.getName());
    private final ObjectProperty<TCaseDetails> initialVersionProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<TCaseDetails> finalVersionProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<CaseTypeEn> caseTypeProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<CompletionResult> completionResultProperty = new SimpleObjectProperty<>(new CompletionResult());
    private ProcessServiceFacade serviceFacade;
    
    private static final String INITIAL_ROW_TITLE = Lang.getProcessCompletionInitalValues()+" (Abrechnung)";
    private static final String FINAL_ROW_TITLE = Lang.getProcessCompletionFinalValues()+" (Ergebnis)";
    
    /**
     * creates new instance with basic layout for scene builder
     *
     * @param pType case type to render version overview grid
     */
    public VersionOverviewGridPane(CaseTypeEn pType) {
        this();
        setCaseType(pType);
    }

    /**
     * creates new instance with basic layout for scene builder
     */
    public VersionOverviewGridPane() {
        super();
        setUpGrid();
        caseTypeProperty.addListener(new ChangeListener<CaseTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends CaseTypeEn> observable, CaseTypeEn oldValue, CaseTypeEn newValue) {
                setUpGrid();
            }
        });
    }

    public VersionOverviewGridPane(TCaseDetails pInitial, TCaseDetails pFinal) {
        this();
        setInitialVersion(pInitial);
        setFinalVersion(pFinal);
    }

    /**
     * @param pInitial details set as initial
     */
    public void setInitialVersion(TCaseDetails pInitial) {
        initialVersionProperty.set(pInitial);
    }

    /**
     * @return get the initial version property, as read only
     */
    public ObjectProperty<TCaseDetails> getInitialVersionProperty() {
        return initialVersionProperty;
    }

    /**
     * @return get the initial version property
     */
    public TCaseDetails getInitialVersion() {
        return initialVersionProperty.get();
    }

    /**
     * @param pFinal details set as initial
     */
    public void setFinalVersion(TCaseDetails pFinal) {
        finalVersionProperty.set(pFinal);
    }

    /**
     * @return get the initial version property, as read only
     */
    public ObjectProperty<TCaseDetails> getFinalVersionProperty() {
        return finalVersionProperty;
    }

    /**
     * @return get the initial version property
     */
    public TCaseDetails getFinalVersion() {
        return finalVersionProperty.get();
    }

    public CaseTypeEn getCaseType() {
        return caseTypeProperty.get();
    }

    public void setCaseType(CaseTypeEn pType) {
        caseTypeProperty.set(pType);
    }

    public ObjectProperty<CaseTypeEn> caseTypeProperty() {
        return caseTypeProperty;
    }

    /**
     * @return get the result of the process completion as property
     */
    public ObjectProperty<CompletionResult> getCompletionResultProperty() {
        return completionResultProperty;
    }

    /**
     * @return get the result of the process completion
     */
    public CompletionResult getCompletionResult() {
        return completionResultProperty.get();
    }

    /**
     * @param pServiceFacade facade to access database
     */
    public void initServiceFacade(ProcessServiceFacade pServiceFacade) {
        serviceFacade = pServiceFacade;
    }

    /**
     * tries to fetch item by its row and column contraint
     *
     * @param pRow row of the item
     * @param pColumn column of the item to find
     * @return item or null
     */
    public Label getItem(int pRow, int pColumn) {
        for (Node item : getChildren()) {
            if (getRowIndex(item) == pRow && getColumnIndex(item) == pColumn) {
                return (Label) item;
            }
        }
        return null;
    }

    /**
     * bypass case details based computation of the gridpane and shows values
     * caution: if case details properties are changed, values will be
     * overwritten
     *
     * @param processResult result values to show
     */
    public void fillRowsWithStoredData(TWmProcessHospitalFinalisation processResult) {
        CompletionResult completionObj = completionResultProperty.get();
        completionObj.setFinalComment(processResult.getFinalVersionComment());
        completionObj.setFinalCw(processResult.getCwFinal());
        completionObj.setFinalCareCw(processResult.getCwCareFinal());
        completionObj.setFinalDrg(processResult.getDrgFinal());
        completionObj.setFinalLos(Long.valueOf(processResult.getLosFinal()));
        completionObj.setFinalRevenue(processResult.getRevenueFinal());
        completionObj.setFinalSuppFee(-1.0);
        completionObj.setFinalVersion(processResult.getFinalVersionNumber());
        completionObj.setInitialComment(processResult.getInitialVersionComment());
        completionObj.setInitialCw(processResult.getCwInitial());
        completionObj.setInitialCareCw(processResult.getCwCareInitial());
        completionObj.setInitialDrg(processResult.getDrgInitial());
        completionObj.setInitialLos(Long.valueOf(processResult.getLosInitial()));
        completionObj.setInitialRevenue(processResult.getRevenueInitial());
        completionObj.setInitialSuppFee(-1.0);
        completionObj.setInitialVersion(processResult.getInitialVersionNumber());
        completionObj.setFinalSuppFee(processResult.getFinalSupplementaryFee());
        completionObj.setInitialSuppFee(processResult.getInitialSupplementaryFee());
        clearRow(1);
        setRow(1);
        clearRow(2);
        setRow(2);
        clearRow(3);
        setRow(3);
    }
    //setup layout of the GridPane

    private void setUpGrid() {
        getStyleClass().add("default-grid");
        if (getCaseType() == null) {
            return;
        }
        setHeaderRow();
        setInitialRow();
        initialVersionProperty.addListener(new ChangeListener<TCaseDetails>() {
            @Override
            public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
                setInitialRow();
                setDiffrenceRow();
            }
        });
        setFinalRow();
        finalVersionProperty.addListener(new ChangeListener<TCaseDetails>() {
            @Override
            public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
                setFinalRow();
                setDiffrenceRow();
            }
        });

        setDiffrenceRow();
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.LEFT, true));

    }

    private Label keyLabel(String pText) {
        Label label = new Label(pText);
        label.getStyleClass().add("cpx-detail-label");
        label.setPrefWidth(120);
        return label;
    }

    private void setInitialRow() {
        clearRow(1);
        if (getInitialVersion() == null) {
            addRow(1, getEmptyRow(INITIAL_ROW_TITLE));
//            switch (getCaseType()) {
//                case DRG:
//                    addRow(1, getDrgDefaultRow(Lang.getProcessCompletionInitalValues()));
//                    break;
//                case PEPP:
//                    addRow(1, getPeppDefaultRow(Lang.getProcessCompletionInitalValues()));
//                    break;
//                default:
//                    LOG.warning("Can not compute row 1, unknown type " + getCaseType());
//            }
            return;
        }
        setRow(1);
    }

    private void setFinalRow() {
        clearRow(2);
        if (getFinalVersion() == null) {
            addRow(2, getEmptyRow(FINAL_ROW_TITLE));
//            switch (getCaseType()) {
//                case DRG:
//                    addRow(2, getDrgDefaultRow(Lang.getProcessCompletionFinalValues()));
//                    break;
//                case PEPP:
//                    addRow(2, getPeppDefaultRow(Lang.getProcessCompletionFinalValues()));
//                    break;
//                default:
//                    LOG.warning("Can not compute row 2, unknown type " + getCaseType());
//            }
            return;
        }
        setRow(2);
//        applyStyle(2);
    }

    private void setDiffrenceRow() {
        clearRow(3);
        if (getFinalVersion() == null || getInitialVersion() == null) {
            Label title = keyLabel(Lang.getDifference());
            title.setPrefWidth(USE_COMPUTED_SIZE);
            addRow(3, new HBox[]{new HBox(title), new HBox(new Label())});

//            switch (getCaseType()) {
//                case DRG:
//                    addRow(3, getDrgDefaultRow(Lang.getDifference()));
//                    break;
//                case PEPP:
//                    addRow(3, getPeppDefaultRow(Lang.getDifference()));
//                    break;
//                default:
//                    LOG.warning("Can not compute row 3, unknown type " + getCaseType());
//            }
            return;
        }
        setRow(3);
//        applyStyle(3);
    }

    private Label[] getDrgHeaderLabels() {
        return new Label[]{new Label(""), keyLabel(Lang.getDRGObj().getAbbreviation()), keyLabel(Lang.getCw()),keyLabel("PflegeCW"),
            keyLabel(Lang.getSupplementaryValue()), keyLabel(Lang.getRevenue()), keyLabel(Lang.getDaysStay()), keyLabel(Lang.getVersion()),keyLabel(Lang.getComment())};
    }
    private Label[] getPeppHeaderLabels() {
        return new Label[]{new Label(""), keyLabel(Lang.getPEPP()), keyLabel(Lang.getDaysStayPepp()),
            keyLabel(Lang.getRevenuePepp()), keyLabel(Lang.getRevenuePeppDailyDay()), keyLabel(Lang.getRevenuePeppSupplfee()), keyLabel(Lang.getVersion()), keyLabel(Lang.getComment())};
    }

    private HBox[] getDrgInitialRow(String pTitle) {
        Label comment = new Label(getCompletionResult().getInitialComment());
        comment.setWrapText(true);
        Label title = keyLabel(pTitle);
        title.setPrefWidth(USE_COMPUTED_SIZE);
        return new HBox[]{new HBox(title) //column 1 title 
            ,
             new HBox(new Label(getCompletionResult().getInitialDrg())) //column 2 drg code 
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialCw(), 3))) //column 3 cw value
            ,
             new HBox(new Label(Lang.toDecimal(Objects.requireNonNullElse(getCompletionResult().getInitialCareCw(),0.0d), 4))) //column 4 care cw value
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialSuppFee(), 2) + " " + Lang.getCurrencySymbol())) //column 5 supplementary fee
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 6 revenue
            ,
             new HBox(new Label(String.valueOf(getCompletionResult().getInitialLos()))) //column 7 los
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialVersion()))) //column 8 version
            ,
             new HBox(comment) //column 9 comment
        };
    }

    private HBox[] getPeppInitialRow(String pTitle) {
        Label comment = new Label(getCompletionResult().getInitialComment());
        comment.setWrapText(true);
        Label title = keyLabel(pTitle);
        title.setPrefWidth(USE_COMPUTED_SIZE);
        return new HBox[]{new HBox(title) //column 1 title 
            ,
             new HBox(new Label(getCompletionResult().getInitialDrg())) //column 2 drg code 
            ,
             new HBox(new Label(String.valueOf(getCompletionResult().getInitialLos()))) //column 3 los
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 4 revenue
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialPeppDailyDays(), 2) + " " + Lang.getCurrencySymbol())) //column 5 supplementary fee
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialPeppSuppFee(), 2) + " " + Lang.getCurrencySymbol())) //column 6 supplementary fee
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialVersion()))) //column 7 version
            ,
             new HBox(comment) //column 8 comment
        };
    }

    private HBox[] getDrgFinalRow(String pTitle) {
        Label comment = new Label(getCompletionResult().getFinalComment());
        comment.setWrapText(true);
        Label title = keyLabel(pTitle);
        title.setPrefWidth(USE_COMPUTED_SIZE);
        return new HBox[]{new HBox(title) //column 1 title 
            ,
             new HBox(new Label(getCompletionResult().getFinalDrg())) //column 2 drg code 
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalCw(), 3))) //column 3 cw value
            ,
             new HBox(new Label(Lang.toDecimal(Objects.requireNonNullElse(getCompletionResult().getFinalCareCw(),0.0d), 4))) //column 4 care cw value
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalSuppFee(), 2) + " " + Lang.getCurrencySymbol())) //column 5 supplementary fee
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 6 revenue
            ,
             new HBox(new Label(String.valueOf(getCompletionResult().getFinalLos()))) //column 7 los
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalVersion()))) //column 8 version
            ,
             new HBox(comment) //column 9 comment
        };
    }

    private HBox[] getPeppFinalRow(String pTitle) {
        Label comment = new Label(getCompletionResult().getFinalComment());
        comment.setWrapText(true);
        Label title = keyLabel(pTitle);
        title.setPrefWidth(USE_COMPUTED_SIZE);
        return new HBox[]{new HBox(title) //column 1 title 
            ,
             new HBox(new Label(getCompletionResult().getFinalDrg())) //column 2 drg code 
            ,
             new HBox(new Label(String.valueOf(getCompletionResult().getFinalLos()))) //column 3 los
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 4 revenue
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalPeppDailyDays(), 2) + " " + Lang.getCurrencySymbol())) //column 5 supplementary fee
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalPeppSuppFee(), 2) + " " + Lang.getCurrencySymbol())) //column 6 supplementary fee
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalVersion()))) //column 7 version
            ,
             new HBox(comment) //column 8 comment
        };
    }

    private HBox[] getDrgDifferenceRow(String pTitle) {
        Label title = keyLabel(pTitle);
        title.setPrefWidth(USE_COMPUTED_SIZE);
        return new HBox[]{new HBox(title) //column 1 title 
            ,
             new HBox(new Label("")) //column 2 drg code 
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffCw(), 3))) //column 3 cw value
            ,
             new HBox(new Label(Lang.toDecimal(Objects.requireNonNullElse(getCompletionResult().getDiffCareCw(),0.0d), 4))) //column 4 care cw value
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffSupplementaryFee(), 2) + " " + Lang.getCurrencySymbol())) //column 5 supplementary fee
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 6 revenue
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffLos()))) //column 7 los
            ,
             new HBox(new Label("")) //column 8 version
            ,
             new HBox(new Label("")) //column 9 comment
        };
    }

    private HBox[] getPeppDifferenceRow(String pTitle) {
        Label title = keyLabel(pTitle);
        title.setPrefWidth(USE_COMPUTED_SIZE);
        return new HBox[]{new HBox(title) //column 1 title 
            ,
             new HBox(new Label("")) //column 2 drg code 
            ,
             new HBox(new Label(String.valueOf(getCompletionResult().getDiffLos()))) //column 3 los
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 4 revenue
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffPeppDailyDays(), 2) + " " + Lang.getCurrencySymbol())) //column 5 supplementary fee
            ,
             new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffPeppSuppFee(), 2) + " " + Lang.getCurrencySymbol())) //column 6 supplementary fee
            ,
             new HBox(new Label("")) //column 7 version
            ,
             new HBox(new Label("")) //column 8 comment
        };
    }

    private HBox[] getInitialRow() {
        String title = INITIAL_ROW_TITLE;
        switch (getCaseType()) {
            case DRG:
                return getDrgInitialRow(title);
            case PEPP:
                return getPeppInitialRow(title);
            default:
                return new HBox[]{};
        }
    }

    private HBox[] getFinalRow() {
        String title = FINAL_ROW_TITLE;
        switch (getCaseType()) {
            case DRG:
                return getDrgFinalRow(title);
            case PEPP:
                return getPeppFinalRow(title);
            default:
                return new HBox[]{};
        }
    }

    private HBox[] getDifferenceRow() {
        String title = Lang.getDifference();
        switch (getCaseType()) {
            case DRG:
                return getDrgDifferenceRow(title);
            case PEPP:
                return getPeppDifferenceRow(title);
            default:
                return new HBox[]{};
        }
    }

    private HBox[] getDrgDefaultRow(String pTitle) {
        return new HBox[]{new HBox(keyLabel(pTitle)), new HBox(new Label("----")), new HBox(new Label("----")),
            new HBox(new Label("----")), new HBox(new Label("----")), new HBox(new Label("----")), new HBox(new Label("----")), new HBox(new Label("----")), new HBox(new Label("----"))};
    }

    private HBox[] getPeppDefaultRow(String pTitle) {
        return new HBox[]{new HBox(keyLabel(pTitle)), new HBox(new Label("----")), new HBox(new Label("----")),
            new HBox(new Label("----")), new HBox(new Label("----")), new HBox(new Label("----")), new HBox(new Label("----"))};
    }

    private void clearRow(int pRow) {

        getChildren().removeIf(new Predicate<Node>() {
            @Override
            public boolean test(Node t) {
                Integer index = getRowIndex(t);
                if (index != null && index == pRow) {
                    return index.equals(pRow);
                }
                return false;
            }
        });
//        getRowConstraints().remove(pRow);
    }
//    
//    //somewhat ugly needs to be refactored

    private void setRow(int pRowIndex) {
//        String title = "";
        switch (pRowIndex) {
            case 1:
//                Label initialCommentLabel = new Label(getCompletionResult().getInitialComment());
//                initialCommentLabel.setWrapText(true);
                addRow(pRowIndex, getInitialRow());
//                        new HBox(keyLabel(title)) //column 1 title 
//                        ,
//                         new HBox(new Label(getCompletionResult().getInitialDrg())) //column 2 drg code 
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialCw(), 3))) //column 3 cw value
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialSuppFee(), 2) + " " + Lang.getCurrencySymbol())) //column 4 supplementary fee
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getInitialRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 5 revenue
//                        ,
//                         new HBox(new Label(String.valueOf(getCompletionResult().getInitialLos()))) //column 6 los
//                        ,
//                         new HBox(new Label(getCompletionResult().getInitialVersion())) //column 7 version
//                        ,
//                         new HBox(new Label(getCompletionResult().getInitialComment())));  //column 8 comment
                break;
            case 2:
//                Label finalCommentLabel = new Label(getCompletionResult().getFinalComment());
//                finalCommentLabel.setWrapText(true);
                addRow(pRowIndex, getFinalRow());
//                        new HBox(keyLabel(title)) //column 1 title 
//                        ,
//                         new HBox(new Label(getCompletionResult().getFinalDrg())) //column 2 drg code 
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalCw(), 3))) //column 3 cw value
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalSuppFee(), 2) + " " + Lang.getCurrencySymbol())) //column 4 supplementary fee
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getFinalRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 5 revenue
//                        ,
//                         new HBox(new Label(String.valueOf(getCompletionResult().getFinalLos()))) //column 6 los
//                        ,
//                         new HBox(new Label(getCompletionResult().getFinalVersion())) //column 7 version
//                        ,
//                         new HBox(new Label(getCompletionResult().getFinalComment())));  //column 8 comment
                break;
            case 3:
//                title = Lang.getDifference();
                addRow(pRowIndex, getDifferenceRow());
//                        new HBox(keyLabel(title)) //column 1 title 
//                        ,
//                         new HBox(new Label("")) //column 2 drg code 
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffCw(), 3))) //column 3 cw value
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffSupplementaryFee(), 2) + " " + Lang.getCurrencySymbol())) //column 4 supplementary fee
//                        ,
//                         new HBox(new Label(Lang.toDecimal(getCompletionResult().getDiffRevenue(), 2) + " " + Lang.getCurrencySymbol())) //column 5 revenue
//                        ,
//                         new HBox(new Label(String.valueOf(getCompletionResult().getDiffLos()))) //column 6 los
//                        ,
//                         new HBox(new Label("")) //column 7 version
//                        ,
//                         new HBox(new Label("")));  //column 8 comment
                break;
            default:
                LOG.warning("unknown index! " + pRowIndex);
        }
    }
//
//    //apply style to even and odd rows
//    private void applyStyle(int pRow){
//        if(pRow == 0){
//            for(Node node : getChildren()){
//                if(getRowIndex(node)==pRow){
//                    node.setStyle("-fx-padding: 0 2 0 2;");
//                }
//            }
//            return;
//        }
//        if (pRow % 2 == 0){
//            //even
//            for(Node node : getChildren()){
//                if(getRowIndex(node)==pRow){
//                    node.getStyleClass().add("case-management-grid-even");
//                    node.setStyle("-fx-padding: 0 2 0 2;");
//                }
//            }
//        }
//        else
//        {
//            //odd
//            for(Node node : getChildren()){
//                if(getRowIndex(node)==pRow){
//                    node.getStyleClass().add("case-management-grid-odd");
//                    node.setStyle("-fx-padding: 0 2 0 2;");
//                }
//            }
//        }
//
//    }

    private void setHeaderRow() {
        clearRow(0);
        switch (getCaseType()) {
            case DRG:
                addRow(0, getDrgHeaderLabels());
                break;
            case PEPP:
                addRow(0, getPeppHeaderLabels());
                break;
            default:
                LOG.warning("Can not setup row 0, type unknown: " + getCaseType());
        }
    }

    public void refresh() {
        getChildren().clear();
        setUpGrid();
    }

    public void reload() {
        if (isDisabled()) {
            return;
        }
        //CPX-1447 set new initla and final case version values (update grouping results)
        //may result in loading grouping data twice when only one is necessary
        if (getInitialVersion() == null) {
            return;
        }
        if (getFinalVersion() == null) {
            return;
        }
        getCompletionResult().setInitalVersionValues(getInitialVersion());
        getCompletionResult().setFinalVersionValues(getFinalVersion());
        refresh();
    }

    private HBox[] getEmptyRow(String pTitle) {
        Label title = keyLabel(pTitle);
        title.setPrefWidth(USE_COMPUTED_SIZE);
        HBox titleBox = new HBox(title);
        Label lbl = new Label("Keine relevante Fallversion für diesen Bereich gefunden!");
        lbl.setMinWidth(USE_PREF_SIZE);
        lbl.setMaxWidth(Double.MAX_VALUE);
        HBox content = new HBox(lbl);
        content.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(content, REMAINING);
//        content.minWidthProperty().bind(prefWidthProperty().subtract(title.widthProperty()).subtract(50));
//        content.maxWidthProperty().bind(widthProperty());
        return new HBox[]{titleBox,content};
    }

    public class CompletionResult {

        private TGroupingResults finalGroupingResult;
        private TGroupingResults initialGroupingResult;
        private String finalDrg = "";
        private String initialDrg = "";
        private Double finalCw = 0.0d;
        private Double initialCw = 0.0d;
        private Double finalSf = 0.0d;
        private Double finalSp = 0.0d;
        private Double finalDf = 0.0d;
        private Double initialSf = 0.0d;
        private Double initialSp = 0.0d;
        private Double initialDf = 0.0d;
        private Double finalRevenue = 0.0d;
        private Double initialRevenue = 0.0d;
        private Long finalLos = 0L;
        private Long initialLos = 0L;
        private long finalVersion = 0;
        private long initialVersion = 0;
        private String finalComment = "";
        private String initialComment = "";
        private Double initialCareCw = 0.0d;
        private Double finalCareCw = 0.0d;

        public CompletionResult() {

            getInitialVersionProperty().addListener(new ChangeListener<TCaseDetails>() {
                @Override
                public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
                    setInitalVersionValues(newValue);
                }
            });
            getFinalVersionProperty().addListener(new ChangeListener<TCaseDetails>() {
                @Override
                public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
                    setFinalVersionValues(newValue);
                }
            });

        }

        public String getFinalDrg() {
            return finalDrg;
        }

        public String getInitialDrg() {
            return initialDrg;
        }

        public Double getFinalCw() {
            return finalCw;
        }
        
        public Double getFinalCareCw() {
            return finalCareCw;
        }
        
        public Double getInitialCw() {
            return initialCw;
        }
        
        public Double getInitialCareCw(){
            return initialCareCw;
        }
        public Double getFinalSuppFee() {
            return finalSf;
        }

        public Double getInitialSuppFee() {
            return initialSf;
        }

        public Double getFinalRevenue() {
            return finalRevenue;
        }

        public Double getInitialRevenue() {
            return initialRevenue;
        }

        public Long getFinalLos() {
            return finalLos;
        }

        public Long getInitialLos() {
            return initialLos;
        }

        public long getFinalVersion() {
            return finalVersion;
        }

        public long getInitialVersion() {
            return initialVersion;
        }

        public String getFinalComment() {
            return finalComment;
        }

        public String getInitialComment() {
            return initialComment;
        }

        public Long getDiffLos() {
            return checkLong(finalLos) - checkLong(initialLos);
        }

        public Double getDiffSupplementaryFee() {
            return checkDouble(finalSf) - checkDouble(initialSf);
        }

        public Double getDiffCw() {
            return checkDouble(finalCw) - checkDouble(initialCw);
        }
        
        public Double getDiffCareCw() {
            return checkDouble(finalCareCw) - checkDouble(initialCareCw);
        }
        
        public Double getDiffRevenue() {
            return checkDouble(finalRevenue) - checkDouble(initialRevenue);
        }

        private Double getFinalPeppSuppFee() {
            return finalSp;
        }

        private Double getFinalPeppDailyDays() {
            return finalDf;
        }

        private Double getInitialPeppDailyDays() {
            return initialDf;
        }

        private Double getInitialPeppSuppFee() {
            return initialSp;
        }

        private Double getDiffPeppDailyDays() {
            return checkDouble(finalDf) - checkDouble(initialDf);
        }

        private Double getDiffPeppSuppFee() {
            return checkDouble(finalSp) - checkDouble(initialSp);
        }

        public void setFinalGroupingResult(TGroupingResults finalGroupingResult) {
            this.finalGroupingResult = finalGroupingResult;
        }

        public void setInitialGroupingResult(TGroupingResults initialGroupingResult) {
            this.initialGroupingResult = initialGroupingResult;
        }

        public void setFinalDrg(String finalDrg) {
            this.finalDrg = finalDrg;
        }

        public void setInitialDrg(String initialDrg) {
            this.initialDrg = initialDrg;
        }

        public void setFinalCw(Double finalCw) {
            this.finalCw = finalCw;
        }

        public void setInitialCw(Double initialCw) {
            this.initialCw = initialCw;
        }
        
        public void setFinalCareCw(Double finalCareCw) {
            this.finalCareCw = finalCareCw;
        }

        public void setInitialCareCw(Double initialCareCw) {
            this.initialCareCw = initialCareCw;
        }
        
        public void setFinalSuppFee(Double finalSuppFee) {
            this.finalSf = finalSuppFee;
        }

        public void setInitialSuppFee(Double initialSuppFee) {
            this.initialSf = initialSuppFee;
        }

        public void setFinalRevenue(Double finalRevenue) {
            this.finalRevenue = finalRevenue;
        }

        public void setInitialRevenue(Double initialRevenue) {
            this.initialRevenue = initialRevenue;
        }

        public void setFinalLos(Long finalLos) {
            this.finalLos = finalLos;
        }

        public void setInitialLos(Long initialLos) {
            this.initialLos = initialLos;
        }

        public void setFinalVersion(long finalVersion) {
            this.finalVersion = finalVersion;
        }

        public void setInitialVersion(long initialVersion) {
            this.initialVersion = initialVersion;
        }

        public void setFinalComment(String finalComment) {
            this.finalComment = finalComment;
        }

        public void setInitialComment(String initialComment) {
            this.initialComment = initialComment;
        }

//        private void getFinalPeppSuppFee(Double finalSp) {
//            this.finalSp = finalSp;
//        }
//
//        private void getFinalPeppDailyDays(Double finalDf) {
//            this.finalDf = finalDf;
//        }
//
//        private void getInitialPeppDailyDays(Double initialDf) {
//            this.initialDf = initialDf;
//        }
//
//        private void getInitialPeppSuppFee(Double initialSp) {
//            this.initialSp = initialSp;
//        }
        private void setInitalVersionValues(TCaseDetails pDetails) {
            if(pDetails == null){
                initialComment = "";
                initialLos = 0L;
                initialVersion = 0L;
                initialCw = 0.0d;
                initialCareCw = 0.0d;
                initialRevenue = 0.0d;
                return;
            }
            LOG.info("set grouping infos");
            initialGroupingResult = serviceFacade.getDrgResult(pDetails.getId(), CpxClientConfig.instance().getSelectedGrouper());
            if (initialGroupingResult != null) {
                initialDrg = initialGroupingResult.getGrpresCode();
//                initialSf = serviceFacade.findSupplementaryFee(initialGroupingResult.getId());
                setInitialSupplFees(pDetails.getId());
                CaseTypeEn caseType = pDetails.getHospitalCase().getCsCaseTypeEn();
                switch (caseType) {
                    case DRG:
                        TCaseDrg drgResult = (TCaseDrg) initialGroupingResult;
                        initialCw = drgResult.getDrgcCwEffectiv();
                        initialCareCw = drgResult.getCareCwDays();
                        initialRevenue = drgResult.getRevenue(serviceFacade.getDrgBaseRateFeeValue(pDetails), serviceFacade.getCareBaseRateFeeValue(pDetails));//serviceFacade.getDrgBaseRateFeeValue(pDetails) * initialCw;
                        break;

                    case PEPP:
                        TCasePepp peppResult = (TCasePepp) initialGroupingResult;
                        initialCw = peppResult.getPeppcCwEffectiv();
                        initialRevenue = peppResult.getRevenue();
                        break;

                    default:
                        LOG.log(Level.WARNING, "Unknown case type: " + caseType);
                        break;
                }
            }
            initialComment = CaseDetailsCommentHelper.formatCommentNoSeperatorNoLineBreak(pDetails);
            initialLos = pDetails.getCsdLos();
            initialVersion = pDetails.getCsdVersion();
        }

        private void setFinalVersionValues(TCaseDetails pDetails) {
            if(pDetails == null){
                finalComment = "";
                finalLos = 0L;
                finalVersion = 0L;
                finalCw = 0.0d;
                finalCareCw = 0.0d;
                finalRevenue = 0.0d;
                return;
            }
            finalGroupingResult = serviceFacade.getDrgResult(pDetails.getId(), CpxClientConfig.instance().getSelectedGrouper());
            if (finalGroupingResult != null) {
                finalDrg = finalGroupingResult.getGrpresCode();
//                finalSf = serviceFacade.findSupplementaryFee(finalGroupingResult.getId());
                setFinalSupplFees(pDetails.getId());
                CaseTypeEn caseType = pDetails.getHospitalCase().getCsCaseTypeEn();
                switch (caseType) {
                    case DRG:
                        TCaseDrg drgResult = (TCaseDrg) finalGroupingResult;
                        finalCw = ((TCaseDrg) finalGroupingResult).getDrgcCwEffectiv();
                        finalCareCw = drgResult.getCareCwDays();
                        finalRevenue = drgResult.getRevenue(serviceFacade.getDrgBaseRateFeeValue(pDetails), serviceFacade.getCareBaseRateFeeValue(pDetails));//serviceFacade.getDrgBaseRateFeeValue(pDetails) * finalCw;
                        break;

                    case PEPP:
                        TCasePepp peppResult = (TCasePepp) finalGroupingResult;
                        finalCw = ((TCasePepp) finalGroupingResult).getPeppcCwEffectiv();
                        finalRevenue = peppResult.getRevenue();
                        break;
                    default:
                        LOG.log(Level.WARNING, "Unknown case type: " + caseType);
                        break;
                }
            }
            finalComment = CaseDetailsCommentHelper.formatCommentNoSeperatorNoLineBreak(pDetails);
            finalLos = pDetails.getCsdLos();
            finalVersion = pDetails.getCsdVersion();
        }

        private void setInitialSupplFees(long pVersionId) {
            TCase baseCase = serviceFacade.getCurrentProcess().getMainCase();
            CaseTypeEn caseType = baseCase.getCsCaseTypeEn();
            switch (caseType) {
                case DRG:
                    initialSf = serviceFacade.findSupplementaryFee(pVersionId, SupplFeeTypeEn.ZE);
                    break;
                case PEPP:
                    initialSp = serviceFacade.findSupplementaryFee(pVersionId, SupplFeeTypeEn.ZP);
                    initialDf = serviceFacade.findSupplementaryFee(pVersionId, SupplFeeTypeEn.ET);
                    break;
                default:
                    LOG.log(Level.WARNING, "Unknown case type: " + caseType);
                    break;

            }
        }

        private void setFinalSupplFees(long pVersionId) {
            TCase baseCase = serviceFacade.getCurrentProcess().getMainCase();
            CaseTypeEn caseType = baseCase.getCsCaseTypeEn();
            switch (caseType) {
                case DRG:
                    finalSf = serviceFacade.findSupplementaryFee(pVersionId, SupplFeeTypeEn.ZE);
                    break;
                case PEPP:
                    finalSp = serviceFacade.findSupplementaryFee(pVersionId, SupplFeeTypeEn.ZP);
                    finalDf = serviceFacade.findSupplementaryFee(pVersionId, SupplFeeTypeEn.ET);
                    break;
                default:
                    LOG.log(Level.WARNING, "Unknown case type: " + caseType);
                    break;
            }
        }

        private Double checkDouble(Double pDouble) {
            //check if double is null, return 0.0
            return pDouble != null ? pDouble : 0.0;
        }

        private Long checkLong(Long pLong) {
            //check if double is null, return 0.0
            return pLong != null ? pLong : 0L;
        }

    }
}

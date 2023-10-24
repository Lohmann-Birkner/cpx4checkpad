/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.catalog.layout;

import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 *
 * @author gerschmann
 */
public class SimulIcdLayout  extends VBox {
    private final static String KEY_STYLE = "-fx-text-fill:-black05";
    private final static String VALUE_STYLE = "-fx-text-fill:-text-main";

    private final TGroupingResults groupingResult;
    private List<ColumnConstraints> columnConstraints;
    private int rowCount = 0;
    private long vwd = 0;
    private AdmissionReasonEn admReason = AdmissionReasonEn.ar01;

    
   public SimulIcdLayout(TGroupingResults gr, Long pVwd, AdmissionReasonEn pCsdAdmReasonEn) {
        super();
        vwd = pVwd;
        groupingResult = gr;
        admReason = pCsdAdmReasonEn;
       getChildren().add(createContent());
        //Workaround, add padding.. sometimes for some reason popover do not render top and bottom padding declared in css
       setPadding(new Insets(1));


    }
    private static final Logger LOG = Logger.getLogger(SimulIcdLayout.class.getName());


    private Node createContent() {
        GridPane pane = new GridPane();
        pane.setMinWidth(200);
        pane.setMaxWidth(600);
        pane.getStyleClass().add("default-grid");
        if(groupingResult == null){

            return pane;
        }
        if(groupingResult.getGrpresType() == null){
            return pane;
        }
        Label type = new Label(groupingResult.getGrpresType().name());
        type.setStyle(KEY_STYLE);
                        

        Label code = new Label(groupingResult.getGrpresCode() == null?"": groupingResult.getGrpresCode());
        code.setStyle(VALUE_STYLE);
        pane.addRow(rowCount++, type, code, new Label(""));
        if(groupingResult.getGrpresType().equals(CaseTypeEn.DRG)){
            return createDrgContent(pane);
        }
        if(groupingResult.getGrpresType().equals(CaseTypeEn.PEPP)){
            return createPeppContent(pane);
        }
        
        return pane;
    }

    private List<ColumnConstraints> getColumnConstraints() {

        if (columnConstraints == null) {
            columnConstraints = new ArrayList<>();
            columnConstraints.add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.LEFT, true));
        }
        return columnConstraints;
    }
    
    private Node createDrgContent( GridPane pane){
         TCaseDrg drg = groupingResult.getCaseDrg();
        LOG.log(Level.FINE, " ICD: {0}, DRG: {1} uncorr.CW: {2} corrCw: {3} careDays: {4} careCwDay: {5}",
            new Object[]{"",
                groupingResult.getGrpresCode(), 
                drg.getDrgcCwCatalog(),
                drg.getDrgcCwEffectiv(),
                drg.getDrgcCareDays(),
                drg.getDrgcCareCwDay()
            });
        
        if(groupingResult.getGrpresCode() == null || groupingResult.getGrpresCode().trim().length() == 0
                || groupingResult.getGrpresCode().startsWith("---")){
            Label noDrg = new Label("Es wurde keine DRG ermittelt");
            noDrg.setStyle(KEY_STYLE);
            noDrg.setWrapText(true);
            pane.add(noDrg, 0, rowCount++, 2, 1);

            if(admReason.equals(AdmissionReasonEn.ar03) || admReason.equals(AdmissionReasonEn.ar04)) {
                Label lbAdmReason = new Label("Aufnahmegrund");
                lbAdmReason.setStyle(KEY_STYLE);
                Label admValue = new Label(admReason.toString());
                admValue.setStyle(VALUE_STYLE);
                admValue.setWrapText(true);
                pane.addRow(rowCount++, lbAdmReason, admValue);
            }
            pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
            pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
            return pane;
        } else{

            Label cwCatalogText = new Label("Kostengewicht(DRG) " );
            cwCatalogText.setStyle(KEY_STYLE);

            Label cwCatalogValueTxt = new Label("unkorr. CW ");
            cwCatalogValueTxt.setWrapText(true);
            cwCatalogValueTxt.setStyle(VALUE_STYLE);
            Label cwCatalogValue = new Label(Lang.toDecimal(drg.getDrgcCwCatalog(), 3));
            cwCatalogValue.setStyle(VALUE_STYLE);
            pane.addRow(rowCount++, cwCatalogText,cwCatalogValueTxt, cwCatalogValue);

            Label cwCorrText = new Label();
            cwCorrText.setStyle(KEY_STYLE);

            Label cwCorrValueTxt = new Label("korr. CW ");
            cwCorrValueTxt.setWrapText(true);
            cwCorrValueTxt.setStyle(VALUE_STYLE);

            Label cwCorrValue = new Label(Lang.toDecimal(drg.getDrgcCwEffectiv(), 3));
            cwCorrValue.setWrapText(true);
            cwCorrValue.setStyle(VALUE_STYLE);

            pane.addRow(rowCount++, cwCorrText,cwCorrValueTxt, cwCorrValue);

            if(groupingResult.getModelIdEn().getCatalogYear() > 2019){    
                Label cwCareText = new Label("Kostengewicht(Pflege) ");
                cwCareText.setStyle(KEY_STYLE);

                Label cwCareValueTxt = new Label(drg.getDrgcCareDays() + " Tag(e), enspr. ");
                cwCareValueTxt.setWrapText(true);
                cwCareValueTxt.setStyle(VALUE_STYLE);

                double careCw = drg.getDrgcCareCw() == 0?
                        drg.getDrgcCareDays() * drg.getDrgcCareCwDay():
                        drg.getDrgcCareCw();
                Label cwCareValue = new Label(Lang.toDecimal(careCw, 4));
                cwCareValue.setWrapText(true);
                cwCareValue.setStyle(VALUE_STYLE);

                pane.addRow(rowCount, cwCareText,cwCareValueTxt, cwCareValue);
            }
            pane.getColumnConstraints().addAll(getColumnConstraints());

            pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
            pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
            pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));

           return pane;
        }
    }

    private Node createPeppContent(GridPane pane) {
        TCasePepp pepp = groupingResult.getCasePepp();
            LOG.log(Level.FINE, " ICD: {0}, PEPP: {1} PayClass: {2} corrCw: {3} days: {4} "
                    + "cwDay: " +(pepp.getPeppPayClassCwDay() == null?0:pepp.getPeppPayClassCwDay()),
            new Object[]{"",
                groupingResult.getGrpresCode(), 
                pepp.getPeppcPayClass(),
                pepp.getPeppcCwEffectiv(),
                vwd
                
            });
        Label cwPayClassText = new Label("Vergütungsklasse " );
        cwPayClassText.setStyle(KEY_STYLE);

        Label cwCPayClassValue = new Label(String.valueOf(pepp.getPeppcPayClass()));
        cwCPayClassValue.setStyle(VALUE_STYLE);
        pane.addRow(rowCount++, cwPayClassText,cwCPayClassValue);

        Label cwFullText = new Label("Kostengewicht (PEPP) " );
        cwFullText.setStyle(KEY_STYLE);

        Label cwFullValue = new Label(Lang.toDecimal(pepp.getPeppcCwEffectiv(), 4));
        cwFullValue.setStyle(VALUE_STYLE);
        pane.addRow(rowCount++, cwFullText, cwFullValue);
        
        Label cwFullText1 = new Label(" " );
        cwFullText1.setWrapText(true);
        cwFullText1.setStyle(KEY_STYLE);

        Label cwFullValue1 = new Label(String.valueOf(vwd) + 
                " Tag(e), enspr. " + Lang.toDecimal(pepp.getPeppPayClassCwDay(), 4) + " pro Tag");
        cwFullValue1.setWrapText(true);
        cwFullValue1.setStyle(VALUE_STYLE);
        pane.addRow(rowCount, cwFullText1, cwFullValue1);
        return pane;
    }
}

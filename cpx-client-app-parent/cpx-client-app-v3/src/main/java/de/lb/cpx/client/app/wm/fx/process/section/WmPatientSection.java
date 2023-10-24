/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.patient_health_status_details.AcgVisualisationWeb;
import de.lb.cpx.client.app.wm.fx.process.patient_health_status_details.PatientHealthStatusVisualization;
import de.lb.cpx.client.app.wm.fx.process.section.details.WmPatientDetails;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmPatientOperations;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.webview.LoadingWebView;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.NodeUtil;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Section in Process GUI for the overview of the Patient involved
 *
 * @author wilde
 */
public class WmPatientSection extends WmSection<TPatient> {

    private static final Logger LOG = Logger.getLogger(WmPatientSection.class.getName());

    private final TPatient currentPatient;
    private Label labelPatientNumber;
    private Label labelPatientFullname;
    private Label labelPatientGender;
    private Label labelPatientBirthday;
    private Label labelPatientCases;
    private Label labelPatientInsurance;
    private Label labelPatientInsuranceNumber;
//    private final PatientHealthStatusVisualization phsViz;
    private final AcgVisualisationWeb phsVizWeb;
    private static final SimpleBooleanProperty IS_PHS_VIZ_THUMBNAIL_CLICKED = new SimpleBooleanProperty(false);
    private VBox phsVizThumbnailPane;
     AcgVisualisationWeb SmallAvatarOnly = null;
    

    public WmPatientSection(final ProcessServiceFacade pServiceFacade) {
        super(pServiceFacade);
        long start = System.currentTimeMillis();
        setTitle(Lang.getPatient());
        currentPatient = pServiceFacade.getPatient();
        LOG.finer("done init patient in " + (System.currentTimeMillis() - start) + " ms");
        start = System.currentTimeMillis();
        if (CpxClientConfig.instance().getCommonHealthStatusVisualization()) {
        // TODO: mouseclick
//            phsViz = new PatientHealthStatusVisualization(currentPatient);
            phsVizWeb = new AcgVisualisationWeb(currentPatient.getPatNumber(), 4, 4, 0.65); 
            LOG.finer("done init phsViz in " + (System.currentTimeMillis() - start) + " ms");
            start = System.currentTimeMillis();
        } else {
//            phsViz = null;
            phsVizWeb = null;
        }
        
        setValues();
        LOG.finer("done setValues in " + (System.currentTimeMillis() - start) + " ms");
        start = System.currentTimeMillis();

        //handle Click
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    getRoot().requestFocus();
                    //LOG.log(Level.INFO, "is phsVizThumbnailPane = " + phsVizThumbnailClicked.getValue());
                    invalidateRequestDetailProperty();
                }
            }
        });
        if(!getMenuItems().isEmpty()){
            ContextMenu menu = new CtrlContextMenu<>();
//            MenuItem showPatientItem = new MenuItem(Lang.getContextMenuOpenpatient());
//            showPatientItem.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    //serviceFacade.loadAndShow(TwoLineTab.TabType.PATIENT, m_currentPatient.getId());
//                    openPatient();
//                }
//            });
//            menu.getItems().add(showPatientItem);
            menu.getItems().addAll(getMenuItems().keySet().stream().sorted().map((t) -> {
                WmSectionMenuItem item = getMenuItems().get(t);
                MenuItem menuItem = new MenuItem(item.getText());
                menuItem.setOnAction((ActionEvent t1) -> {
                    item.getHandler().handle(t1);
                });
                return menuItem;
            }).collect(Collectors.toList()));
            getContextMenuProperty().bind(Bindings.when(getArmedProperty()).then(menu).otherwise((ContextMenu) null));
        }
        LOG.finer("done rest in " + (System.currentTimeMillis() - start) + " ms");
        start = System.currentTimeMillis();
    }

    @Override
    public void dispose() {
        setOnMouseClicked(null);
        getContextMenuProperty().unbind();
        getSkin().clearMenu();
        if(phsVizThumbnailPane != null){
            phsVizThumbnailPane.setOnMouseClicked(null);
        }
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void openPatient() {
        //facade.loadAndShow(TwoLineTab.TabType.PATIENT, currentPatient.getId());
        ItemEventHandler eh = new WmPatientOperations(facade).openItem(currentPatient);
        if (eh != null) {
            eh.handle(null);
        }
    }

    @Override
    public Map<String, WmSectionMenuItem> createMenuItems(){
        Map<String, WmSectionMenuItem> map = super.createMenuItems(); //To change body of generated methods, choose Tools | Templates.s
        
        //TODO check connection to acg
        if (CpxClientConfig.instance().getCommonHealthStatusVisualization()) {
                if(phsVizWeb != null && getFacade().checkAcgConnection()){
                map.put(Lang.getContextMenuOpenpatient(), new WmSectionMenuItem(Lang.getContextMenuOpenpatient(), ResourceLoader.getGlyph(FontAwesome.Glyph.EXTERNAL_LINK), new EventHandler<Event>() {
                    @Override
                    public void handle(Event t) {
                        openPatient();
                    }
                }));
            }
        }
        return map;
    }
    
    
    
//    @Override
//    public void setMenu() {
//        PopOver menuOver = new AutoFitPopOver();
//        Button openHealthStatus = new Button(Lang.getContextMenuOpenpatient());
//        openHealthStatus.setMaxWidth(Double.MAX_VALUE);
//        openHealthStatus.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                menuOver.hide();
//                openPatient();
//            }
//        });
//
//        Button menu = new Button();
//        menu.getStyleClass().add("cpx-icon-button");
//        menu.setGraphic(getSkin().getGlyph("\uf142"));
//        menuOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
//        menuOver.setContentNode(openHealthStatus);
//        menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                menuOver.show(menu);
//            }
//        });
//        getSkin().setMenu(menu);
//    }

    @Override
    public TPatient getSelectedItem() {
        return currentPatient;
    }

//    @Override
//    public List<TPatient> getSelectedItems() {
//        final List<TPatient> list = new ArrayList<>();
//        list.add(currentPatient);
//        return list;
//    }
    @Override
    public WmPatientDetails getDetails() {
        TPatient selected = getSelectedItem();
        WmPatientDetails details = new WmPatientDetails(facade, selected);
//        details.setPhsViz(phsViz);
        details.setPhsVizWeb(phsVizWeb);

        return details;
    }

//    @Override
//    public Parent getDetailContent() {
//        TPatient selected = getSelectedItem();
//        WmPatientDetails details = new WmPatientDetails(facade, selected);
//        details.setPhsViz(phsViz);
//        WmDetailSection detailSection = details.getDetailSection();
////        if (phsViz != null) {
////            VBox vbox = phsViz.getCompactContent();
////            vbox.prefWidthProperty().bind(pane.widthProperty());
////            pane.setContent(vbox);
////        }
////        if (phsViz != null && detailContent != null) {
////            TitledPane pane = (TitledPane) detailContent.getScene().lookup("#" + WmPatientDetails.HEALTH_STATUS_VIS_PANE_ID);
////            Platform.runLater(new Runnable() {
////                @Override
////                public void run() {
////                    VBox vbox = phsViz.getCompactContent();
////                    vbox.prefWidthProperty().bind(pane.widthProperty());
////                    pane.setContent(vbox);
////                }
////            });
////        }
//        return detailSection.getRoot();
////        long startTime = System.currentTimeMillis();
////        VBox detailContent = new VBox();
////        detailContent.setSpacing(10.0);
////        TitledPane patientDetails = setUpPatientDetails();
////        TitledPane casePane = setUpCaseTitledPane();
////        TitledPane processPane = setUpProcessTitledPane();
////        TitledPane insPane = setUpInsurancePane();
////        TitledPane healthStatusPane = setUpHealthStatusVisualizationPane();
////
////        Pane leftSpace = new Pane();
////        Pane rightSpace = new Pane();
////        HBox.setHgrow(leftSpace, Priority.ALWAYS);
////        HBox.setHgrow(rightSpace, Priority.ALWAYS);
////        HBox imageViewBox = new HBox();
//////        imageViewBox.getChildren().addAll(leftSpace, detailPatientImage, rightSpace);
////        detailContent.getChildren().addAll(patientDetails, casePane, processPane, insPane, healthStatusPane, imageViewBox);
////
////        WmDetailSection detail = new WmDetailSection();
////        detail.setTitle(Lang.getPatient() + ": " + currentPatient.getPatNumber());
////
////        ScrollPane scrollPane = new ScrollPane(detailContent);
////        scrollPane.setFitToHeight(true);
////        scrollPane.setFitToWidth(true);
////        detail.setContent(scrollPane);
////        LOG.log(Level.FINE, "set data for workflow number " + (serviceFacade == null ? "null" : serviceFacade.getCurrentProcessNumber()) + " loaded in " + (System.currentTimeMillis() - startTime) + " ms");
////        return detail.getRoot();
//    }
    @Override
    protected Parent createContent() {
        long startTime = System.currentTimeMillis();
        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER);
//        contentBox.setMinHeight(230);
        contentBox.setPadding(new Insets(0, 0, 0, 8));
        contentBox.setSpacing(12);
        CpxFXMLLoader.setAnchorsInNode(contentBox);

        GridPane gp = new GridPane();
        gp.getStyleClass().add("default-grid");
        gp.setHgap(10.0);
        gp.setMaxWidth(GridPane.USE_PREF_SIZE);
        labelPatientNumber = new Label();
        Label labelPatientNumberText = new Label(Lang.getPatientNumber());
        labelPatientNumberText.getStyleClass().add("cpx-detail-label");
        Label labelPatientFullnameText = new Label(Lang.getPatientFullName());
        labelPatientFullnameText.getStyleClass().add("cpx-detail-label");
        labelPatientGender = new Label();
        Label labelPatientGenderText = new Label(Lang.getGender());
        labelPatientGenderText.getStyleClass().add("cpx-detail-label");
        labelPatientFullname = new Label();
        Label labelPatientBirthdayText = new Label(Lang.getDateOfBirth() + " - " + Lang.getAge());
        labelPatientBirthdayText.getStyleClass().add("cpx-detail-label");
        labelPatientBirthday = new Label();
        Label labelPatientCasesText = new Label(Lang.getCountCases());
        labelPatientCasesText.getStyleClass().add("cpx-detail-label");
        labelPatientCases = new Label();

        Label labelPatientInsuranceText = new Label(Lang.getInsuranceCompany());
        labelPatientInsuranceText.getStyleClass().add("cpx-detail-label");
        labelPatientInsurance = new Label();
        Label labelPatientInsuranceNumberText = new Label(Lang.getInsuranceNumber());
        labelPatientInsuranceNumberText.getStyleClass().add("cpx-detail-label");
        labelPatientInsuranceNumber = new Label();
        gp.add(labelPatientNumberText, 0, 0);
        gp.add(labelPatientNumber, 1, 0);
        gp.add(labelPatientFullnameText, 0, 1);
        gp.add(labelPatientFullname, 1, 1);
        gp.add(labelPatientGenderText, 0, 2);
        gp.add(labelPatientGender, 1, 2);
        gp.add(labelPatientBirthdayText, 0, 3);
        gp.add(labelPatientBirthday, 1, 3);
        gp.add(labelPatientCasesText, 0, 4);
        gp.add(labelPatientCases, 1, 4);
        gp.add(labelPatientInsuranceText, 0, 5);
        gp.add(labelPatientInsurance, 1, 5);
        gp.add(labelPatientInsuranceNumberText, 0, 6);
        gp.add(labelPatientInsuranceNumber, 1, 6);

//        phsViz = new PatientHealthStatusVisualization(m_currentPatient == null ? "" : m_currentPatient.getPatNumber());
//        StackPane[] phsVizThumbnailPanes = phsViz.getThumbnailContent();
        //StackPane[] phsVizThumbnailPanes = PatientHealthStatusVisualization.getThumbnailContent();
        ScrollPane sPane = new ScrollPane(gp);
        HBox.setMargin(sPane, new Insets(8, 0, 0, 0));

        //AWi: 20161220, Workaround for scrollbar behaviour, sizes of the content of the ScrollPane can differ 
        //by the name of the insurance, therefore no scrollbar-policy of the ScrollPane behaves correctly 
        //workaround should be checked if javafx offer a cleaner way to acomplish the desired bahaviour
        NodeUtil.handleScrollBarPolicies(sPane);
//        sPane.setMinHeight(220);
        HBox.setHgrow(sPane, Priority.ALWAYS);

        //This is just a placeholder for health status thumbnail and will be rendered later!
        if (CpxClientConfig.instance().getCommonHealthStatusVisualization()) {
            phsVizThumbnailPane = new VBox();

            phsVizThumbnailPane.setMinHeight(180);
            phsVizThumbnailPane.setMinWidth(120);
            phsVizThumbnailPane.setMaxHeight(phsVizThumbnailPane.getMinHeight());
            phsVizThumbnailPane.setMaxWidth(phsVizThumbnailPane.getMinWidth());
            phsVizThumbnailPane.autosize();

            contentBox.getChildren().addAll(phsVizThumbnailPane);

            phsVizThumbnailPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    IS_PHS_VIZ_THUMBNAIL_CLICKED.setValue(Boolean.TRUE);
                }
            });
            sPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    IS_PHS_VIZ_THUMBNAIL_CLICKED.setValue(Boolean.FALSE);
                }
            });
        }

        contentBox.getChildren().addAll(sPane);

        LOG.log(Level.FINE, "create content for workflow number {0} loaded in {1} ms", new Object[]{(facade == null ? "null" : facade.getCurrentProcessNumber()), (System.currentTimeMillis() - startTime)});
        return contentBox;
    }

    private void setValues() {
        labelPatientNumber.setText(currentPatient.getPatNumber());
        labelPatientFullname.setText(currentPatient.getPatFullName());
        labelPatientGender.setText(currentPatient.getPatGenderEn() != null ? currentPatient.getPatGenderEn().toString() : "");
////        labelPatientBirthday.setText(currentPatient.getPatDateOfBirth() != null
////                ? Lang.toDate(currentPatient.getPatDateOfBirth()) + " - " + Lang.getElapsedTimeToNow(currentPatient.getPatDateOfBirth()).getYear() : "");
//        labelPatientCity.setText(m_currentPatient.getPatDetailsActual().getPatdCity());
        labelPatientCases.setText(String.valueOf(currentPatient.getCases().size()));
        TCase baseCase = facade.getMainCase(facade.getCurrentProcess());//.getProcess().getMainCase();
        if (baseCase != null) {
            String insuranceCompany = baseCase.getInsuranceIdentifier() == null ? null : baseCase.getInsuranceIdentifier();
            String insuranceNumber = baseCase.getInsuranceNumberPatient() == null ? null : baseCase.getInsuranceNumberPatient();
            CpxInsuranceCompany insCompany = facade.findInsuranceCompanyByIdent(insuranceCompany);
            labelPatientInsurance.setText(insCompany.getInscName());
            labelPatientInsurance.setTooltip(new Tooltip(insCompany.toString()));
            labelPatientInsuranceNumber.setText(insuranceNumber);
            TCaseDetails det = facade.getCurrentLocal(baseCase.getId());
            if(det != null){
                labelPatientBirthday.setText((currentPatient.getPatDateOfBirth() != null
                ? (Lang.toDate(currentPatient.getPatDateOfBirth()) + " - "):"") 
                        + (det.getCsdAgeYears() > 0?(det.getCsdAgeYears() + " " + Lang.getAgeYears()):
                                (det.getCsdAgeDays() + " " + Lang.getAgeDays())));
                labelPatientBirthday.setTooltip(new Tooltip("am " + Lang.toDate(det.getCsdAdmissionDate())));
                
            }else{
                labelPatientBirthday.setText(currentPatient.getPatDateOfBirth() != null
                        ? Lang.toDate(currentPatient.getPatDateOfBirth()) + " - " + Lang.getElapsedTimeToNow(currentPatient.getPatDateOfBirth()).getYear() : "");
                
            }
        }else{
                labelPatientBirthday.setText(currentPatient.getPatDateOfBirth() != null
                        ? Lang.toDate(currentPatient.getPatDateOfBirth()) + " - " + Lang.getElapsedTimeToNow(currentPatient.getPatDateOfBirth()).getYear() : "");
                
        }

//        final Glyph genderGlyph;
//        if (m_currentPatient.getPatGenderEn() == null) {
//            genderGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION);
//            Tooltip.install(genderGlyph, new Tooltip(Lang.getGender() + " " + Lang.getGenderUnknown()));
//        } else {
//            switch (m_currentPatient.getPatGenderEn()) {
//                case M:
//                    genderGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.MARS);
//                    genderGlyph.setStyle("-fx-text-fill: blue;");
//                    Tooltip.install(genderGlyph, new Tooltip(Lang.getGenderMale()));
//                    break;
//                case W:
//                    genderGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.VENUS);
//                    genderGlyph.setStyle("-fx-text-fill: red;");
//                    Tooltip.install(genderGlyph, new Tooltip(Lang.getGenderFemale()));
//                    break;
//                case I:
//                    genderGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.TRANSGENDER);
//                    genderGlyph.setStyle("-fx-text-fill: purple;");
//                    Tooltip.install(genderGlyph, new Tooltip(Lang.getGender() + " " + Lang.getGenderUndefined()));
//                    break;
//                default: //U:
//                    genderGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION);
//                    Tooltip.install(genderGlyph, new Tooltip(Lang.getGender() + " " + Lang.getGenderUnknown()));
//                    break;
//            }
//        }
//        genderGlyph.setPadding(new Insets(0, 0, 0, 3));
//        genderGlyphPane.getChildren().setAll(genderGlyph); web = 
        //Show the most recent health status of this patient as a thumbnail
        if (phsVizThumbnailPane != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(SmallAvatarOnly == null){
                        SmallAvatarOnly = new AcgVisualisationWeb(currentPatient.getPatNumber(), 1, 3, 0.2); 
                    }else{
                        SmallAvatarOnly.reset();
                    }
                        VBox content = SmallAvatarOnly.getContent(getFacade().checkAcgConnection());
                        SmallAvatarOnly.getMouseClick().addListener(new ChangeListener<Boolean> (){
                           @Override
                           public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                IS_PHS_VIZ_THUMBNAIL_CLICKED.setValue(Boolean.TRUE);
                                 invalidateRequestDetailProperty();

                           };
                       }); 

                       content.prefWidthProperty().bind(phsVizThumbnailPane.widthProperty());
                       content.prefHeightProperty().bind(phsVizThumbnailPane.heightProperty());
                       phsVizThumbnailPane.getChildren().add(content);
                    }
//                }
            });
        }
     }

    @Override
    public WmPatientOperations getOperations() {
        return new WmPatientOperations(facade);
    }
    
    public static boolean isPhsVizThumbnailClicked() {
        return IS_PHS_VIZ_THUMBNAIL_CLICKED.get();
    }


}

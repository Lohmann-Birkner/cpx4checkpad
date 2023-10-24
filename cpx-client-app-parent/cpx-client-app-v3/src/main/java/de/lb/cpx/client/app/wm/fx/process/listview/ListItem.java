/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 */
package de.lb.cpx.client.app.wm.fx.process.listview;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddReminderDialog;
import de.lb.cpx.client.app.wm.fx.dialog.UpdateActionDialog;
import de.lb.cpx.client.app.wm.fx.process.section.WmHistorySection;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmActionOperations;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.server.commonDB.model.CMdk;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import de.lb.cpx.wm.model.TWmRequestBege;
import de.lb.cpx.wm.model.TWmRequestInsurance;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRequestOther;
import de.lb.cpx.wm.model.TWmRequestReview;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.VLineTo;
import javafx.scene.transform.Translate;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * To create each listitem (listcell) to form a complete listView.
 *
 * @author nandola
 */
public final class ListItem {
    
    private static final Logger LOG = Logger.getLogger(ListItem.class.getSimpleName());
//    public Boolean isFirstItem = false;
    //private final Label c_date;
    //private final Label c_user;
    //private final Label eventLabel;
    //private final Label eventDetails;
    //private final HBox date_name_menus;
    //private final VBox listitem;
    //private final Button button;
    //private final Path path;
    //private final HBox listItemBox;
    //private TWmEvent m_event;
    private static final CpxInsuranceCompanyCatalog INSURANCE_CATALOG = CpxInsuranceCompanyCatalog.instance();
    private static final CpxMdkCatalog MDK_CATALOG = CpxMdkCatalog.instance();
    private static final EjbProxy<ProcessServiceBeanRemote> PROCESS_SERVICE_BEAN = Session.instance().getEjbConnector().connectProcessServiceBean();
    //private final EjbProxy<ConfigurationServiceEJBRemote> connectConfigurationServiceBean;

    private final String creationDate;
    private final String creationUser;
    private final String eventname;
    private String detailedText;
    private final TWmEvent event;
    private final ProcessServiceFacade facade;
    
    private final Button button = new Button("");
    private final Button buttonMail = new Button("");
    private Label eventLabel;
    private Label user;
    private Label eventDetails;
    private HBox listItemBox;

    /*
    public Boolean setIsFirstItem() {
        isFirstItem = true;
        return isFirstItem;
    }
     */
    /**
     * To create an item in a listView based on event type.
     *
     * @param pCreationDate creation date of an event
     * @param pCreationUser creation user of an event
     * @param pEventName name of an event
     * @param pDetailedText details about an event
     * @param pEvent event
     * @param pFacade facade for access additional data (catalog?)/call dialogs
     * to modify items
     */
    public ListItem(String pCreationDate, String pCreationUser, String pEventName, String pDetailedText, TWmEvent pEvent, ProcessServiceFacade pFacade) {
        super();
        creationUser = pCreationUser;
        creationDate = pCreationDate;
        eventname = pEventName;
        detailedText = pDetailedText;
        event = pEvent;
        facade = pFacade;
    }
    
    public void setOnButtonAction(EventHandler<ActionEvent> pEvent) {
        button.setOnAction(pEvent);
    }

//    public boolean isRootInitialized() {
//        return listItemBox != null;
//    }
//
//    public void resetRoot() {
//        listItemBox = null;
//    }
    // return the root node of the listitem (or listcell).
    public Pane getRoot() {
//        if (isRootInitialized()) {
//            return listItemBox;
//        }
        long startTime = System.currentTimeMillis();
        //connectConfigurationServiceBean = connector.connectConfigurationServiceBean();
        //final TWmEvent m_event = event;
        listItemBox = new HBox();
        AnchorPane root = new AnchorPane();
        //Circle circle = new Circle(5);
        Rectangle r = new Rectangle();
        r.setWidth(9);
        r.setHeight(9);
        
        Translate translateSquare = new Translate();
        translateSquare.setX(0);
        translateSquare.setY(7);
        r.getTransforms().addAll(translateSquare);
        
        if (Session.instance().getCpxUserId() == event.getCreationUser()) {
            r.getStyleClass().add("cpx-history-node-current-user");
        } else {
            r.getStyleClass().add("cpx-history-node");
        }
        
        final Path path = new Path();

        // A square handling in a path.
        MoveTo mt = new MoveTo(4.5f, 0.0f); // X-axis parameter starts from half of the square width
        path.getElements().add(mt);

//        if (isFirstItem) {
//            path.getElements().add(new VLineTo(9.0f));
//        } else {
        path.getElements().add(new VLineTo(0.0f));
//        }
//        path.getElements().add(new VLineTo(0.0f));  // set the value same as square height & width
        path.getStyleClass().add("cpx-history-line");

//        // use following code for the circle.
//        MoveTo mt = new MoveTo(0.0f, 0.0f);
//        path.getElements().add(mt);
//        path.getElements().add(new VLineTo(5.0f));  // set the value same as circle radius
//        path.getStyleClass().add("cpx-history-line");
//        path.setStrokeWidth(1);
        root.getChildren().add(path);
        root.getChildren().add(r); // set either circle or square.

        //button = new Button("");
        button.visibleProperty().bind(Bindings
                .when(button.graphicProperty().isNotNull())
                .then(true)
                .otherwise(false));
        button.autosize();
        button.setStyle("-fx-font-size: 5px;");
        button.setAlignment(Pos.TOP_RIGHT);
        buttonMail.visibleProperty().bind(Bindings
                .when(buttonMail.graphicProperty().isNotNull())
                .then(true)
                .otherwise(false));
        buttonMail.autosize();
        buttonMail.setStyle("-fx-font-size: 5px;");
        buttonMail.setAlignment(Pos.TOP_CENTER);
        buttonMail.setTooltip(new Tooltip("versenden"));
        Tooltip PrTooltip = new Tooltip();
        PrTooltip.setStyle("-fx-font-size: 12px");
        PrTooltip.setWrapText(true);
        PrTooltip.setMaxWidth(500);
        
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PrTooltip.show(button, event.getScreenX() + 10, event.getScreenY() + 10);
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PrTooltip.hide();
            }
        });
        
        switch (event.getEventType()) {
            case documentAdded:
                button.setGraphic(getGlyph("\uf0c6"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getDocumentAddedTooltip());
                if (event.getDocument() == null) {
                    button.setDisable(true);
                }
                break;
            
            case documentRemoved:
                
                button.setGraphic(getGlyph("\uf0c6"));
                button.setDisable(true);
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getDocumentRemovedTooltip());
                break;
            
            case reminderCreated:
                button.setGraphic(getGlyph("\uf0f3"));
//                button.setGraphic(getGlyph("\uf271"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getReminderCreatedTooltip());
                if (event.getReminder() == null) {
                    button.setDisable(true);
                }
                break;

//            case reminderExpires:
//                button.setGraphic(getGlyph("\uf0f3"));
//                button.getStyleClass().add("cpx-icon-button");
//                PrTooltip.setText(Lang.getReminderExpiredTooltip());
//                break;
//
//            case reminderAssigned:
//                button.setGraphic(getGlyph("\uf0f3"));
//                button.getStyleClass().add("cpx-icon-button");
//                PrTooltip.setText(Lang.getReminderAssignedTooltip());
//                if (event.getReminder() == null) {
//                    button.setDisable(true);
//                }
//                break;
            case reminderChanged:
                button.setGraphic(getGlyph("\uf0f3"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getReminderChangedTooltip());
                if (event.getReminder() == null) {
                    button.setDisable(true);
                }
                break;
            
            case reminderRemoved:
                button.setGraphic(getGlyph("\uf0f3"));
                button.setDisable(true);
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getReminderRemovedTooltip());
                break;
            
            case requestCreated:
                button.setGraphic(getGlyph("\uf0ec"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getRequestAddedTooltip());
                if (event.getRequest() == null) {
                    button.setDisable(true);
                }
                break;
            
            case requestUpdated:
                button.setGraphic(getGlyph("\uf0ec"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getRequestAddedTooltip());
                if (event.getRequest() == null) {
                    button.setDisable(true);
                }
                break;
            
            case requestRemoved:
                button.setGraphic(getGlyph("\uf0ec"));
                button.setDisable(true);
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText("Diese Anfrage wird entfernt");
                break;
            
            case actionAdded:
//                button.setGraphic(getGlyph("\uf27a"));
                button.setGraphic(getGlyph("\uf0e5"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText("Aktion ändern");
                buttonMail.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
                buttonMail.getStyleClass().add("cpx-icon-button");
                
                if (event.getAction() == null) {
                    button.setDisable(true);
                    buttonMail.setGraphic(null);
                }
                break;
            
            case actionChanged:
                button.setGraphic(getGlyph("\uf0e5"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText("Aktion ändern");
                buttonMail.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
                buttonMail.getStyleClass().add("cpx-icon-button");
                if (event.getAction() == null) {
                    button.setDisable(true);
                    buttonMail.setGraphic(null);
                }
                break;
            
            case actionRemoved:
                button.setGraphic(getGlyph("\uf0e5"));
                button.setDisable(true);
                button.getStyleClass().add("cpx-icon-button");
//                PrTooltip.setText(Lang.getActionRemovedTooltip());
                break;
            
            case processSubjectChanged:
//                button.setGraphic(getGlyph("\uf016"));
//                button.getStyleClass().add("cpx-icon-button");
//                PrTooltip.setText(Lang.getProcessSubjectChangedTooltip());
                break;
            
            case processUserChanged:
//                button.setGraphic(getGlyph("\uf016"));
//                button.getStyleClass().add("cpx-icon-button");
//                PrTooltip.setText(Lang.getProcessUserChangedTooltip());
                break;
            
            case processClosed:
                button.setGraphic(getGlyph("\uf016"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getProcessClosedTooltip());
                break;
            
            case caseAdded:
                button.setGraphic(getGlyph("\uf016"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getCaseAddedTooltip());
                if (event.getHosCase() == null) {
                    button.setDisable(true);
                }
                break;
            
            case caseRemoved:
                button.setGraphic(getGlyph("\uf016"));
                button.setDisable(true);
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText(Lang.getCaseRemovedTooltip());
                break;
            
            case kainReceived:
                button.setGraphic(getGlyph("\uf122"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText("Antwort (Erstelle Inka)");
                if (event.getKainInka() != null && event.getKainInka().getProcessingRef().equals("76")) {
                    button.setDisable(true);
                }
                break;
            
            case inkaStored:
                button.setGraphic(getGlyph("\uf0c7"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText("Gespeicherte Inka");
                if (event.getKainInka() != null && event.getKainInka().getProcessingRef().equals("76")) {
                    button.setDisable(true);
                }
                
                break;
            
            case inkaUpdated:
                button.setGraphic(getGlyph("\uf0c7"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText("Aktualisiert Inka");
                if (event.getKainInka() != null && event.getKainInka().getProcessingRef().equals("76")) {
                    button.setDisable(true);
                }
                break;
            
            case inkaSent:
                button.setGraphic(getGlyph("\uf2c6"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText("INKA-Nachricht versendet");
                if (event.getKainInka() != null && event.getKainInka().getProcessingRef().equals("76")) {
                    button.setDisable(true);
                }
                break;
            
            case inkaCancelled:
//                button.setGraphic(getGlyph("\uf410"));
                button.setGraphic(getGlyph("\uf05e"));
                button.getStyleClass().add("cpx-icon-button");
                PrTooltip.setText("INKA-Nachricht storniert");
                if (event.getKainInka() != null && event.getKainInka().getProcessingRef().equals("76")) {
                    button.setDisable(true);
                }
                break;
            
            default:
                LOG.log(Level.FINEST, "Unknown Event Type is Found {0}", event.getEventType().name() + "/" + event.getEventType());
                break;
        }
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switch (event.getEventType()) {
                    case documentAdded:
//                            TWmDocument doc = m_event.getDocument();
////                            File file = DocumentManager.loadDocument(doc);
//                            String property = System.getProperty("java.io.tmpdir");  // temp dir on client
//                            String locToCreateFile = property + doc.getName();
//                            byte[] documentContentFromDB = processServiceBean.getDocumentContent(doc.getId());
                        DocumentManager.openDocument(event.getDocument(), facade);
                        break;
                    
                    case reminderCreated:
                    case reminderChanged:
//                    case reminderExpires:
                    case reminderRemoved:
//                    case reminderAssigned:
                        TWmReminder rem_c = event.getReminder();
//                            WmReminderSection WmReminderSection = new WmReminderSection(facade);//new ProcessServiceFacade(rem_c.getProcess().id));
//                            WmReminderSection.editReminderAction(rem_c);
                        AddReminderDialog dialog = new AddReminderDialog(facade, rem_c);
                        dialog.showAndWait();
                        //trigger refresh in history
                        facade.getProperties().put(WmHistorySection.REFRESH_DETAILS, null);
                        break;

                    // with this button created request(e.g. MDK Request) should be open and user can edit that request.    
                    case requestCreated:
                        break;
                    
                    case requestUpdated:
                        break;
                    
                    case requestRemoved:
                        break;
                    
                    case actionAdded:
                        handleChangeAction(getEvent().getAction());
                        break;
                    
                    case actionChanged:
                        handleChangeAction(getEvent().getAction());
                        break;
                    
                    case actionRemoved:
                        break;
                    
                    case processSubjectChanged:
                        TWmProcess prc = event.getProcess();
                        break;
                    
                    case processUserChanged:
                        TWmProcess prc_u = event.getProcess();
                        break;
                    
                    case processClosed:
                        TWmProcess prc_c = event.getProcess();
                        break;
                    
                    case caseAdded:
                        break;
                    
                    case kainReceived:
                        break;
                    
                    case inkaStored:
                        break;
                    
                    case inkaSent:
                        break;
                    
                    case inkaCancelled:
                        break;
                    
                    case caseRemoved:
                        break;
                    
                    case documentRemoved:
                        break;
                    
                    case processReopened:
                        break;
                    
                    default:
                        LOG.log(Level.SEVERE, "Unknown Event Type is Found {0}", event.getEventType());
                        break;
                    
                }
            }
        });
        buttonMail.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switch (event.getEventType()) {
                    
                    case actionAdded:
                    case actionChanged:
                        ItemEventHandler eh = new WmActionOperations(facade).sendMailItem(event.getAction());
                        if (eh != null) {
                            eh.handle(null);
                        }
//                        MailCreator mailCreator = new MailCreator();
//                        mailCreator.sendMail(facade.getCurrentProcess(), event.getAction());
//                        SendMail sendMail = new SendMail();
//                        final String receiverMail = "";
//                        final StringBuilder Cases = new StringBuilder();
//                        facade.getObsProcessCases().forEach((pCase) -> {
//                            String append = Cases.length() != 0 ? ", " : "";
//                            Cases.append(append + pCase.getHosCase().getCsCaseNumber());
//                        });
//                        final String subject = "Vorgang (" + (event.getAction() != null && event.getAction().getActionType() != null ? MenuCache.instance().getActionSubjectsForId(event.getAction().getActionType()) : "") + ") " + Lang.getCaseNumber() + ": " + Cases.toString();
//                        final String messg = (event.getAction() != null && event.getAction().getComment() != null ? String.valueOf(event.getAction().getComment()) : "").replace("\n", "<br>");
//                        final boolean html = true;
//                        try {
//                            sendMail.openDraft(receiverMail, subject, messg, html);
//                        } catch (MessagingException | IOException ex) {
//                            LOG.log(Level.SEVERE, "Unable to create mail draft", ex);
//                            MainApp.showErrorMessageDialog(ex, "Es konnte kein E-Mail-Entwurf erstellt werden");
//                        }
                        break;
                    
                    default:
                        LOG.log(Level.SEVERE, "Not supported yet. {0}", event.getEventType());
                        break;
                    
                }
            }
        }
        );
        HBox.setHgrow(button, Priority.ALWAYS);
        HBox date_name_menus = new HBox();
        HBox.setHgrow(date_name_menus, Priority.ALWAYS);
        date_name_menus.setSpacing(12);
        listItemBox.setPadding(new Insets(0, 10, 0, 0));
        date_name_menus.prefWidthProperty().bind(listItemBox.widthProperty());
        
        VBox listitem = new VBox(3);
//        listitem.setSpacing(3);
        listitem.setStyle("-fx-font-size: 12px;");
        listitem.setPrefHeight(USE_COMPUTED_SIZE);
        listitem.setPrefWidth(USE_COMPUTED_SIZE);
        listitem.setMaxHeight(USE_PREF_SIZE);
        listitem.setMaxWidth(USE_PREF_SIZE);
//        listitem.setMinWidth(378);
        listitem.getStyleClass().add("cpx-history-vbox");

        /* listitem.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 2) {
                    //Fire button on double click on history list item
                    if (button.isVisible() && !button.isDisabled() && !button.isDisable()) {
                        button.fire();
                    }
                }
            }
        });  
         */
        path.getElements().add(mt); // path is based on the listitem element.
        Label c_date = new Label();
        c_date.setText(creationDate);
        
        Tooltip timestampTT = new Tooltip();
//        timestampTT.setStyle("-fx-font-size: 12px");
        timestampTT.setStyle("-fx-font-weight: normal");
//        timestampTT.setStyle("-fx-text-fill: grey"); // In tooltip to set font color
//        timestampTT.getStyleClass().add("basic-tooltip");
        timestampTT.setWrapText(true);
        timestampTT.setPrefWidth(USE_COMPUTED_SIZE);
        timestampTT.setPrefHeight(USE_COMPUTED_SIZE);
        timestampTT.setMaxHeight(USE_PREF_SIZE);
        timestampTT.setMaxWidth(USE_PREF_SIZE);
        timestampTT.setStyle("-fx-padding: 0 0 0 0; -fx-text-fill: grey");
        
        c_date.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                timestampTT.show(c_date, event.getScreenX() + 10, event.getScreenY() + 10);
            }
        });
        c_date.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                timestampTT.hide();
            }
        });
        
        timestampTT.setText(Lang.toIsoTime(event.getCreationDate()));
        HBox.setHgrow(c_date, Priority.ALWAYS);
        
        user = new Label();
        user.setText(creationUser);
        HBox.setHgrow(user, Priority.ALWAYS);
        
        eventLabel = new Label();
        eventLabel.setText(eventname);
        eventLabel.setStyle("-fx-font-size: 14px;");
        eventLabel.setWrapText(true);
        eventLabel.getStyleClass().add("cpx-history-title-label");
        
        eventDetails = new Label();
        eventDetails.setText(detailedText);
        eventDetails.setWrapText(true);
        if (Session.instance().isShowHistoryEventDetails()) {
            //No limitation! Don't show excerpt from details text but whole text!
        } else {
            eventDetails.setMaxHeight(65);
        }
        // set some region between creation user label and a botton.
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox hbButton = new HBox();
        hbButton.setSpacing(2);
        hbButton.getChildren().addAll(buttonMail, button);
        date_name_menus.getChildren().addAll(c_date, user, region, hbButton);
        
        listitem.getChildren().addAll(date_name_menus, eventLabel, eventDetails);
        path.prefHeight(listitem.heightProperty().longValue());
        mt.yProperty().bind((listitem.heightProperty()));   // vertical property (height) of path is binded to the listitem height

        listItemBox.getChildren().addAll(root, listitem);
        listItemBox.setSpacing(12);
        listItemBox.setPrefHeight(USE_COMPUTED_SIZE);
        listItemBox.setPrefWidth(USE_COMPUTED_SIZE);
        listItemBox.setMaxHeight(USE_PREF_SIZE);
        listItemBox.setMaxWidth(USE_PREF_SIZE);
        
        LOG.log(Level.FINEST, "ListItem in " + (System.currentTimeMillis() - startTime) + " ms");
        return listItemBox;
    }
    
    private void handleChangeAction(TWmAction pAction) {
        if (pAction == null) {
            return;
        }
        UpdateActionDialog dialog = new UpdateActionDialog(pAction, facade);
        dialog.showAndWait();
    }

    // return this event
    public TWmEvent getEvent() {
        return event;
    }

    /**
     * sets new Detailed Text in ListItem
     *
     * @param detailText detailed Text
     */
    public void setDetailText(String detailText) {
        eventDetails.setText(detailText);
        detailedText = detailText;
    }
    
    public String getDetailText() {
        return eventDetails.getText();
    }

    /**
     * to update particular Action
     *
     * @param action the action to update
     * @param isNew is this new action
     */
    public void UpdateActionType(TWmAction action, boolean isNew) {
        if (action == null) {
            return;
        }
//        String tooltip = Lang.getEventTypeActionAdded(action.getActionType().getActionType());
        //CWmListActionSubject actionSubjectObject = processServiceBean.get().getActionSubjectById(action.getActionType());
        String actionSubjectName = MenuCache.instance().getActionSubjectName(action.getActionType());
        String tooltip;
        if (isNew) {
            tooltip = Lang.getEventTypeActionAdded(actionSubjectName != null ? actionSubjectName : "----");
        } else {
            tooltip = Lang.getEventTypeActionChanged(actionSubjectName != null ? actionSubjectName : "----");
        }
//        eventname = tooltip;
        eventLabel.setText(tooltip);
    }

    /**
     * to update particular Request
     *
     * @param request the request to update
     * @param isNew is this new Request
     */
    public void UpdateRequestType(TWmRequest request, boolean isNew) {
        String tooltip = null;
        if (request == null) {
//            LOG.log(Level.SEVERE, "Request does not exist obviously, but it is still assigned to process with id " + facade.getCurrentProcessId() + "! Process has request related event but request is null -> maybe you should also delete this event!");
            return;
        }
        if (request.getRequestType() != null) {
            switch (request.getRequestTypeEnum()) {
                case bege:
//                    String InsName = insuranceCatalog.findInsNameByInsuranceNumber(request.getIkNumber(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    TWmRequestBege begeReq = (TWmRequestBege) request;
                    String InsName = INSURANCE_CATALOG.findInsNameByInsuranceNumber(begeReq.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    if (isNew) {
                        tooltip = Lang.getEventTypeRequestCreated(WmRequestTypeEn.bege, InsName);
                    } else {
                        tooltip = Lang.getEventTypeRequestUpdated(WmRequestTypeEn.bege, InsName);
                    }
                    break;
                case mdk:
//                    CMdk mdk = mdkCatalog.getByInternalId(Long.parseLong(request.getIkNumber()), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    TWmRequestMdk mdkReq = (TWmRequestMdk) request;
                    CMdk mdk = MDK_CATALOG.getByInternalId(mdkReq.getMdkInternalId(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    if (isNew) {
                        tooltip = Lang.getEventTypeRequestCreated(WmRequestTypeEn.mdk, mdk.getMdkName());
                    } else {
                        tooltip = Lang.getEventTypeRequestUpdated(WmRequestTypeEn.mdk, mdk.getMdkName());
                    }
                    break;
                case audit:
//                    String InsuName = insuranceCatalog.findInsNameByInsuranceNumber(request.getIkNumber(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    TWmRequestAudit auditReq = (TWmRequestAudit) request;
                    String InsuName = INSURANCE_CATALOG.findInsNameByInsuranceNumber(auditReq.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    if (isNew) {
                        tooltip = Lang.getEventTypeRequestCreated(WmRequestTypeEn.audit, InsuName);
                    } else {
                        tooltip = Lang.getEventTypeRequestUpdated(WmRequestTypeEn.audit, InsuName);
                    }
                    break;
                case insurance:
//                    String InsuName = insuranceCatalog.findInsNameByInsuranceNumber(request.getIkNumber(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    TWmRequestInsurance auditReq2 = (TWmRequestInsurance) request;
                    String insuName2 = INSURANCE_CATALOG.findInsNameByInsuranceNumber(auditReq2.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    if (isNew) {
                        tooltip = Lang.getEventTypeRequestCreated(WmRequestTypeEn.insurance, insuName2);
                    } else {
                        tooltip = Lang.getEventTypeRequestUpdated(WmRequestTypeEn.insurance, insuName2);
                    }
                    break;
                case other:
                    TWmRequestOther auditReq3 = (TWmRequestOther) request;
                    String insuName3 = auditReq3.getInstitutionName();
                    if (isNew) {
                        tooltip = Lang.getEventTypeRequestCreated(WmRequestTypeEn.other, insuName3);
                    } else {
                        tooltip = Lang.getEventTypeRequestUpdated(WmRequestTypeEn.other, insuName3);
                    }
                    break;
                case review:
                    TWmRequestReview auditReq4 = (TWmRequestReview) request;
                    CMdk mdk1 = MDK_CATALOG.getByInternalId(auditReq4.getMdInternalId(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    String insuName1 = INSURANCE_CATALOG.findInsNameByInsuranceNumber(auditReq4.getInsIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    if (isNew) {
                        tooltip = Lang.getEventTypeRequestCreated(WmRequestTypeEn.review, insuName1 + ";" + mdk1.getMdkName());
                    } else {
                        tooltip = Lang.getEventTypeRequestUpdated(WmRequestTypeEn.review, insuName1 + ";" + mdk1.getMdkName());
                    }
                    break;
                default:
                    break;
            }
        }
//        String tooltip = Lang.getEventTypeRequestCreated(request.getRequestType(), request.getIkNumber());
//        eventname = tooltip;
        eventLabel.setText(tooltip);
    }

    /**
     * to filter all events based on user text input.
     *
     * @param filteringText filterText either creation user name or event
     * subject name to get those events
     * @return true or false
     */
    public boolean checkContent(String filteringText) {
//        return (eventLabel != null && eventLabel.getText() != null ? eventLabel.getText().toLowerCase().contains(filteringText.toLowerCase()) : false)
//                || (c_user != null && c_user.getText() != null ? c_user.getText().toLowerCase().contains(filteringText.toLowerCase()) : false);
        return StringUtils.containsIgnoreCase(eventname, filteringText)
                || StringUtils.containsIgnoreCase(creationUser, filteringText)
                || StringUtils.containsIgnoreCase(detailedText, filteringText);
    }

    /**
     * loads glyph from the fontAwesome by its identifier 359 * 360
     *
     * @param glyph identifier of the glyph to load 361
     * @return glyph object from the font 362
     */
    public Glyph getGlyph(String glyph) {
        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        return fontAwesome.create(glyph);
    }
    
    public Button getButton() {
        return button;
    }
    
    public Button getButtonMail() {
        return buttonMail;
    }
}

/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.util.texttemplate;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialogSkin;
import de.lb.cpx.client.core.model.fx.labeled.LabeledListView;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.masterdetail.ListViewMasterDetailPane;
import de.lb.cpx.client.core.model.fx.masterdetail.MasterDetailBorderPane;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.enums.TextTemplateTypeEn;
import de.lb.cpx.server.commonDB.model.CTextTemplate;
import de.lb.cpx.server.commonDB.model.CTextTemplate2Context;
import de.lb.cpx.server.commonDB.model.CTextTemplate2UserRole;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.service.ejb.TextTemplateServiceBeanRemote;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 * Dialog related to Text template (shows template texts based on provided
 * contexts)
 *
 * @author nandola
 */
public class TextTemplateDialog extends FormularDialog<StringBuilder> {

    private static final Logger LOG = Logger.getLogger(TextTemplateDialog.class.getName());
    private static final String LIST_VIEW_STYLE = "stay-selected-list-view";
    private StringBuilder sb;
    private final TextTemplateTypeEn textTemplateContext;

    private final EjbProxy<TextTemplateServiceBeanRemote> textTemplateServiceBean;
//    private final EjbProxy<AuthServiceEJBRemote> AuthService;

    private CheckBox cbAllTextTemplates = new CheckBox("Alle anzeigen");
    private List<CTextTemplate> roleAndContextBasedTextTemplates = new ArrayList<>();
    private List<CTextTemplate> roleBasedTextTemplates = new ArrayList<>();
    private List<CTextTemplate> contextBasedTextTemplates = new ArrayList<>();

    private TextFlow textFlow;
    private final Map<String, String> hm;

    // define ListView with the sorting
    private final LabeledListView<CTextTemplate> textTemplateListView = new LabeledListView<>(new AsyncListView<CTextTemplate>() {
        @Override
        public Future<List<CTextTemplate>> getFuture() {

            //LoggedIn user (active session) information
//            CdbUsers cdbUser = Session.instance().getCdbUser();
            // what if user don't have any role ??
            //this is always empty.....
//            Set<CdbUser2Role> cdbUser2Roles = cdbUser.getCdbUser2Roles();
//            List<CdbUser2Role> listOfUser2Role = textTemplateServiceBean.get().getUser2RoleByUserId(cdbUser.getId());
            /*          List<CdbUser2Role> listOfUser2Role = textTemplateServiceBean.get().getUser2RoleByUserId(Session.instance().getCpxUserId());
            listOfUser2Role.forEach((CdbUser2Role t) -> {
            CdbUserRoles cdbUserRoles = t.getCdbUserRoles();
            cdbUserRoles.getCdburName();
            });
             */
            long cpxActualRoleId = Session.instance().getCpxActualRoleId(); // LoggenIn user actual role id.
            CdbUserRoles cdbUserRole = textTemplateServiceBean.get().findRoleById(cpxActualRoleId);

            List<CTextTemplate2UserRole> allTextTemplate2UserRoleBasedOnRole = textTemplateServiceBean.get().getAllTextTemplate2UserRoleBasedOnRole(cdbUserRole);
            List<CTextTemplate2Context> allTextTemplate2ContextBasedOnContext = textTemplateServiceBean.get().getAllTextTemplate2ContextBasedOnContext(textTemplateContext);

            if (allTextTemplate2UserRoleBasedOnRole != null && !allTextTemplate2UserRoleBasedOnRole.isEmpty()) {
                allTextTemplate2UserRoleBasedOnRole.forEach((CTextTemplate2UserRole t) -> {
                    roleBasedTextTemplates.add(t.getTextTemplate());
                });
            }

            if (allTextTemplate2ContextBasedOnContext != null && !allTextTemplate2ContextBasedOnContext.isEmpty()) {
                allTextTemplate2ContextBasedOnContext.forEach((CTextTemplate2Context t) -> {
                    contextBasedTextTemplates.add(t.getTextTemplate());
                });
            }

            roleAndContextBasedTextTemplates = new ArrayList<>(roleBasedTextTemplates);
            roleAndContextBasedTextTemplates.retainAll(contextBasedTextTemplates);
//
//            Collection intersection = CollectionUtils.intersection(roleBasedTextTemplates, contextBasedTextTemplates);

//            allTextTemplates = textTemplateServiceBean.get().getAllTextTemplates();
//            allTextTemplates = textTemplateServiceBean.get().getAllTextTemplateEntries();   // sorted based on text template name
//            filteredTextTemplates = textTemplateServiceBean.get().getAllTextTemplatesForContext(textTemplateContext);   // context specific text templates
            // sort based on template sorting order.
            if (roleAndContextBasedTextTemplates != null && !roleAndContextBasedTextTemplates.isEmpty()) {
                Collections.sort(roleAndContextBasedTextTemplates, (final CTextTemplate textTemplate1, final CTextTemplate textTemplate2) -> Integer.compare(textTemplate1.getTemplateSort(), textTemplate2.getTemplateSort()));
            }
            return new AsyncResult<>(roleAndContextBasedTextTemplates);
        }
    });

    public TextTemplateDialog(Window pOwner, TextTemplateTypeEn textTemplateContext, Map<String, String> hm) {

        super(pOwner, Modality.APPLICATION_MODAL, "Auswahl Textbausteine" + "\t\t\t" + textTemplateContext.getTextBasedOnContext());

        this.textTemplateContext = textTemplateContext;
        this.hm = hm;

        Session session = Session.instance();
        EjbConnector connector = session.getEjbConnector();
        textTemplateServiceBean = connector.connectTextTemplateServiceBean();
//        processServiceBean = connector.connectProcessServiceBean();

//        textTemplateListView.setTitle("");
        TextTemplateList textTemplateList = new TextTemplateList(textTemplateListView.getListView());

        HBox hbTitel = new HBox();
        hbTitel.setSpacing(100);
        hbTitel.setAlignment(Pos.BOTTOM_LEFT);
        Label lbTitel = new Label("Textvorlagen basierend auf Kontext und Benutzerrolle ");
        lbTitel.getStyleClass().add("cpx-detail-label");
        hbTitel.getChildren().addAll(lbTitel, cbAllTextTemplates);

        VBox vBox = new VBox(hbTitel, textTemplateList);
        vBox.setPrefWidth(600);
        vBox.setPrefHeight(600);
        vBox.setFillWidth(true);
        vBox.setSpacing(FormularDialogSkin.CONTROL_SPACING);

        addControlGroup(vBox, true);

        cbAllTextTemplates.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean prev, Boolean next) -> {
            if (cbAllTextTemplates.isSelected()) {
                textTemplateListView.getItems().clear();
                textTemplateListView.setItems(FXCollections.observableArrayList(roleBasedTextTemplates));
                cbAllTextTemplates.setSelected(true);
            } else {
                textTemplateListView.getItems().clear();
                textTemplateListView.setItems(FXCollections.observableArrayList(roleAndContextBasedTextTemplates));
                cbAllTextTemplates.setSelected(false);
            }
        });

        ((AsyncListView) textTemplateListView.getControl()).reload();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                validationSupport.initInitialDecoration();
                validationSupport.registerValidator(textTemplateListView, true, new Validator<Object>() {
                    @Override
                    public ValidationResult apply(Control t, Object u) {
                        ValidationResult res = new ValidationResult();
                        res.addErrorIf(t, "Bitte wählen Sie eine Vorlage aus", textTemplateListView.getSelectedItem() == null);
                        return res;
                    }
                });
            }
        });
    }

    private class TextTemplateList extends ListViewMasterDetailPane<CTextTemplate> {

        public TextTemplateList(ListView<CTextTemplate> pListView) {
            super(pListView);
//            getListView().setContextMenu(new ContextMenu(new MenuItem("")));
            // allows multiple selection of the Listview items
            getListView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            getListView().getStyleClass().add(LIST_VIEW_STYLE);
            setOrientation(MasterDetailBorderPane.DetailOrientation.BOTTOM);
//            getListView().setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);

//            textTemplateListView.getListView().getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends CTextTemplate> change) -> {
//                ObservableList<CTextTemplate> selectedItems = textTemplateListView.getListView().getSelectionModel().getSelectedItems();
//                
//                selectedItems.size();
//            });
            List<CTextTemplate> selectedOrderedItems = new ArrayList<>();
            textTemplateListView.getListView().getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends CTextTemplate> change) -> {
                ObservableList<CTextTemplate> selectedItems = textTemplateListView.getListView().getSelectionModel().getSelectedItems();
                while (change.next()) {
                    ObservableList<? extends CTextTemplate> list = change.getList();
                    List<CTextTemplate> toAdd = new ArrayList<>();
                    List<CTextTemplate> toRemove = new ArrayList<>();
                    list.forEach(new Consumer<CTextTemplate>() {
                        @Override
                        public void accept(CTextTemplate t) {
                            if (!selectedOrderedItems.contains(t)) {
//                                selectedOrderedItems.add(t);
                                toAdd.add(t);
                            }
                        }
                    });
                    selectedOrderedItems.forEach(new Consumer<CTextTemplate>() {
                        @Override
                        public void accept(CTextTemplate t1) {
                            if (!change.getList().contains(t1)) {
//                                selectedOrderedItems.remove(t1);
                                toRemove.add(t1);
                            }
                        }
                    });
                    selectedOrderedItems.addAll(toAdd);
                    selectedOrderedItems.removeAll(toRemove);
//                    if (change.wasAdded() && selectedItems.size() == 1) {
//                        selectedOrderedItems.clear();
//                    }
//                    if (change.wasAdded()) {
////                        if(textTemplateListView.getListView().getSelectionModel().getSelectedItems().size() == 1){selectedOrderedItems.clear();}
//                        List<? extends CTextTemplate> addedSubList = change.getAddedSubList();
//                        addedSubList.stream().forEach((t) -> {
//                            if (!selectedOrderedItems.contains(t)) {
//                                selectedOrderedItems.add(t);
//                            }
//                        });
//                    }
//                    if (change.wasRemoved()) {
//                        List<? extends CTextTemplate> removed = change.getRemoved();    // sometimes this gives null, why??
//                        change.getList();
//                        removed.stream().forEach((t) -> {
//                            if (selectedOrderedItems.contains(t)) {
//                                selectedOrderedItems.remove(t);
//                            }
//                        });
//                    }
                }

                selectedOrderedItems.size();
//                setDetail(createDetailPane(change));
                setDetail(createDetailPane(selectedOrderedItems));
                LOG.log(Level.FINEST, "listview's detailPane is set with selected items..");
            });
            setDetail(createDetailPane(null));


            /*            getListView().getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change change) -> {
                // do something with the item change listener
                while (change.next()) {
                    if (change.wasAdded() && textTemplateListView.getListView().getSelectionModel().getSelectedItems().size() == 1) {
                        selectedOrderedItems.clear();
                    }
                    if (change.wasAdded()) {
//                        change.getAddedSubList().forEach((CTextTemplate t) -> {
//                            if (!selectedOrderedItems.contains(t)) {
                                selectedOrderedItems.addAll(change.getAddedSubList());
//                            }
//                        });
                    }  if (change.wasRemoved()) {
//                        change.getRemoved().forEach((CTextTemplate t) -> {
//                            if (selectedOrderedItems.contains(t)) {
                                selectedOrderedItems.removeAll(change.getRemoved());
//                            }
//                        });
                    }
                }

                selectedOrderedItems.size();
//                setDetail(createDetailPane(change));
                setDetail(createDetailPane(selectedOrderedItems));
            });
            setDetail(createDetailPane(null));
             */
            //fires only for selection (doesn't fire for the deselection)
//            getSelectedItemProperty().addListener(new ChangeListener<CTextTemplate>() {
//                @Override
//                public void changed(ObservableValue<? extends CTextTemplate> ov, CTextTemplate oldValue, CTextTemplate newValue) {
//                    // do something with the item change listener
//                    setDetail(createDetailPane(oldValue, newValue));
//                }
//            });
//            setDetail(createDetailPane(null, null));
            setCellFactory(new Callback<ListView<CTextTemplate>, ListCell<CTextTemplate>>() {
                @Override
                public ListCell<CTextTemplate> call(ListView<CTextTemplate> param) {
                    return new ListCell<CTextTemplate>() {
                        @Override
                        protected void updateItem(CTextTemplate item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setText("");
                                return;
                            }
//                            setText(item.getTemplateContent());
                            setText(item.getTemplateName());
                            setGraphic(null);
//                            setTooltip(new Tooltip(item.getTemplateDescription() == null ? "" : item.getTemplateDescription()));
                            if (item.getTemplateDescription() != null && !item.getTemplateDescription().isEmpty()) {
                                setTooltip(new Tooltip(item.getTemplateDescription()));
                            }
                        }
                    };
                }
            });

            // double clicks to do ..S.O.M.E.T.H.I.N.G....
            getListView().setOnMouseClicked((MouseEvent event) -> {
                if (MouseButton.PRIMARY == event.getButton()
                        && event.getClickCount() == 2) {
                    // needs something?
                }
            });

        }

//        private Parent createDetailPane(ListChangeListener.Change change) {
        private Parent createDetailPane(List<CTextTemplate> change) {
            VBox box = new VBox();
//            box.setPadding(new Insets(10, 0, 0, 0));
            box.setSpacing(0.0);
            box.setPrefHeight(400);

            SplitPane splitPane = new SplitPane(box);
            splitPane.setOrientation(Orientation.VERTICAL);
            splitPane.setDividerPositions(0.0);
            splitPane.getItems().addAll(box);

            if (change == null) {
                return splitPane;
            }
//            else if (change.wasAdded()) {
//            } else if (change.wasRemoved()) {
//            }

//            textFlow = new TextFlow(new Text(showSelectedTemplateContent() == null ? "" : showSelectedTemplateContent().toString()));
            textFlow = new TextFlow();
            StringBuilder showSelectedTemplateContent = showSelectedTemplateContent(change);
            Text text = new Text((showSelectedTemplateContent == null || showSelectedTemplateContent.toString().isEmpty()) ? "" : showSelectedTemplateContent.toString().replaceAll(LIST_VIEW_STYLE, LIST_VIEW_STYLE));
//            text.setStyle("-fx-font-weight: bold");
//            text.setStyle("-fx-stroke: red");
//            String s = "t is <b> bold </b>";
//            String replace = text.getText().replace("a", s);
//            text.setText(replace);
            textFlow.getChildren().addAll(text);

//            textFlow.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 1px;");
//            textFlow.setTextAlignment(TextAlignment.CENTER); 
//            textFlow.setLineSpacing(2.0);
//            textFlow.setMaxWidth();
//            Label label = new Label(showSelectedTemplateContent().toString());
//            label.setWrapText(true);
//            TextFlow textFlow = new TextFlow(label);
//            textFlow.setBackground(Background.getClassCssMetaData().add(e));
            ScrollPane sp = new ScrollPane(textFlow);
//            sp.setContent(box);
            sp.setFitToWidth(true);
            VBox.setVgrow(sp, Priority.ALWAYS);

            box.getChildren().addAll(sp);
            return splitPane;
        }

        public void reload() {
            ((AsyncListView) getListView()).reload();
        }

    }

    public StringBuilder showSelectedTemplateContent(List<CTextTemplate> selectedItems) {
        //Now, copy selected text templates and paste it to the text area.
//        ObservableList<CTextTemplate> selectedItems = textTemplateListView.getListView().getSelectionModel().getSelectedItems();

        if (selectedItems != null && !selectedItems.isEmpty()) {
//            StringBuilder sb = new StringBuilder();
            sb = new StringBuilder();
            selectedItems.forEach((CTextTemplate t) -> {
                sb.append(t.getTemplateContent()).append("\n");
            });
            return sb = fillBookmarksWithValues(sb);
//            return sb;
        }
        return null;
    }

    public StringBuilder fillBookmarksWithValues(StringBuilder sb) {

        StringBuilder sb2 = new StringBuilder();

        if (sb != null && !sb.toString().isEmpty()) {
            sb2.append(sb.toString()).append("\n");
        }

//      taComment.setText(sb2 == null ? "" : sb2.toString());
        // find bookmark key and replace with its value.
        String pattern_prefix = "{#!";
        String pattern_suffix = "!#}";
        String[] substringsBetween = StringUtils.substringsBetween(sb2.toString(), pattern_prefix, pattern_suffix);
//        StringUtils.startsWithIgnoreCase(sb2, pattern_prefix);
//        StringUtils.endsWith(sb2, pattern_suffix);

        if (substringsBetween != null && substringsBetween.length > 0) {
            for (String substringInBetween : substringsBetween) {
                String str = substringInBetween.trim();
                if (hm.containsKey(str) && hm.get(str) != null) {
                    String stringToBeReplace = pattern_prefix + substringInBetween + pattern_suffix;
                    int index = sb2.indexOf(stringToBeReplace);
                    while (index != -1 && sb2.toString().contains(stringToBeReplace)) {
                        LOG.log(Level.INFO, "Key {0} is replaced with the value {1}", new Object[]{stringToBeReplace, hm.get(str)});
//                        Font f = new Font("LucidaSans", Font.BOLD, 12);
//                        AttributedString as = new AttributedString(hm.get(str).toString());
//                        as.addAttribute(TextAttribute.FONT, f);
//                        sb2.replace(index, index + stringToBeReplace.length(), as.toString());
                        sb2.replace(index, index + stringToBeReplace.length(), hm.get(str));
                        index += hm.get(str).length(); // Move to the end of the replacement
                        index = sb2.indexOf(stringToBeReplace, index);
                    }
//                 String replacedStr = sb2.toString().replace(stringToBeReplace, hm.get(str).toString());
                } else {
                    // HashMap doesn't contains provided key or its value
                    LOG.log(Level.INFO, "HashMap doesn''t contains provided key: {0} or its value", str);
                }
            }
        }
        return sb2;

    }

    @Override
    public StringBuilder onSave() {

//        return showSelectedTemplateContent();
        StringBuilder sb = new StringBuilder();
        if (textFlow != null && textFlow.getChildren() != null) {
            for (Node node : textFlow.getChildren()) {
                if (node instanceof Text) {
                    sb.append(((Text) node).getText());
                }
            }
        }
        return sb;
    }

//    public StringBuilder getSelectedTextTemplates() {
//        return sb;
//    }
}

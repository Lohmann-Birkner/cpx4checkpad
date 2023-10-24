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
package de.lb.cpx.client.core.model.fx.dialog;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.util.UndecoratedWindowHelper;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.controlsfx.control.PopOver;

/**
 * Implements the Skin(Styling) of the Dialog Sets the blurEffect if an owner is
 * set and Modalmode is Modal TODO:refactor the controlContainer to an other
 * class to keep this class as default layout and do net mess with controls
 *
 * @author wilde
 * @param <R> return type of the dialog
 */
public class DialogSkin<R> {

    protected final Dialog<R> dialog;
    //overwritten when setNode(Node node) is called
//    private Node headerGrid;
//    private Node buttonBar;
    private final DialogPaneSkin dialogPaneSkin;

    /**
     * creates new layout for the dialog
     *
     * @param pDialog dialog to skin
     * @param pResizeable if dialog window should be resizeable
     */
    public DialogSkin(Dialog<R> pDialog, Boolean pResizeable) {
        dialog = pDialog;
        dialogPaneSkin = new DialogPaneSkin(dialog.getDialogPane());
        applyStyleClass(dialog, "/styles/cpx-default.css");
        addDefaultButton();
//        getComponents();
        pDialog.setOnShown(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                blurOwner(true);
            }
        });
        pDialog.setOnHidden(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                blurOwner(false);
            }
        });
        pDialog.showingProperty().addListener(blurListener);
        if (!dialog.isShowing()) {
            dialog.initStyle(StageStyle.UNDECORATED);
        }
        if (pResizeable) {
            //TODO: Refactor resizing
//            DialogResizeHelper.addDialogResizeListener(getStage());
            UndecoratedWindowHelper.enableResize(8, getStage());
        }

        UndecoratedWindowHelper.enableDragOn(dialogPaneSkin.getButtonBar(), getStage());
        UndecoratedWindowHelper.enableDragOn(dialogPaneSkin.getHeaderGrid(), getStage());
        //when stage is shown set min height and width for content
        getStage().setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                getStage().sizeToScene();
                //AWi-20190607:change behavior, max height will be computed by magic number 70 to reflect dialog header height 
                //remove static min values to enable resize of dialogs
                //may occure in some strange ui if user reduces width or height
                //behavior for min values should be defined!

//                getStage().setMinWidth(pDialog.getDialogPane().getWidth());
//                getStage().setMinHeight(pDialog.getDialogPane().getHeight());
//                getStage().setMaxHeight(dialog.getOwner().getHeight()-40);
                //ignore popover when compute max height of dialog owner, fall back to height of the main app
                if (dialog.getOwner() != null && !(dialog.getOwner() instanceof PopOver)) {
                    //checks if owner height is not present to avoid flickering dialog
                    if (Double.isNaN(dialog.getOwner().getHeight())) {
                        return;
                    }
                    double heightForOwner = dialog.getOwner().getHeight() - 70;
                    //AWi: add new Exception if height of owner is smaller then content of the dialog
                    if (dialog.getDialogPane().getHeight() > heightForOwner) {
                        return;
                    }
                    dialog.getDialogPane().setMaxHeight(heightForOwner);

                    if (Double.isNaN(dialog.getOwner().getWidth())) {
                        return;
                    }
                    double widthForOwner = dialog.getOwner().getWidth() - 70;
                    //AWi: add new Exception if height of owner is smaller then content of the dialog
                    if (dialog.getDialogPane().getWidth() > widthForOwner) {
                        return;
                    }
                    dialog.getDialogPane().setMaxWidth(widthForOwner);
                } else {
                    //if no owner is set, try to find screen
                    Screen screen = getCurrentScreen();
                    if (screen == null) {
                        return;
                    }
                    dialog.getDialogPane().setMaxHeight(screen.getBounds().getHeight() - 70);
                    dialog.getDialogPane().setMaxWidth(screen.getBounds().getWidth() - 70);
                }
            }
        });
        //CPX-1132
        //center window everytime on Position of BasicMainApp, needed if owner stage is not the BasicMainApp
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    return;
                }
                //remove this window, otherwise dialogs appear not centered in MainWindow
                centerWindow();
            }
        });
//        window.addEventHandler(WindowEvent.WINDOW_SHOWING, new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        long start = System.currentTimeMillis();
//                        double centerXPosition = BasicMainApp.getWindow().getX() + BasicMainApp.getWindow().getWidth() / 2d;
//                        double centerYPosition = BasicMainApp.getWindow().getY() + BasicMainApp.getWindow().getHeight() / 2d;
////                Platform.runLater(new Runnable() {
////                    @Override
////                    public void run() {
//                        window.setX(centerXPosition - window.getWidth() / 2d);
//                        window.setY(centerYPosition - window.getHeight() / 2d);
//                        LOG.info("time to show " + (System.currentTimeMillis() - start));
//                    }
//                });
//            }
//        });

    }

    public void centerWindow() {
        Window window = dialog.getDialogPane().getScene().getWindow();
        if (window == null) {
            return;
        }
        final Window mainWindow = BasicMainApp.getWindow();
        if (mainWindow != null) {
            double centerXPosition = mainWindow.getX() + mainWindow.getWidth() / 2d;
            double centerYPosition = mainWindow.getY() + mainWindow.getHeight() / 2d;
            window.setX(centerXPosition - window.getWidth() / 2d);
            window.setY(centerYPosition - window.getHeight() / 2d);
        }
    }

    public Screen getCurrentScreen() {
        Window window = dialog.getDialogPane().getScene().getWindow();
        if (window == null) {
            //should not occure
            return null;
        }
        Rectangle2D windowRectangle = new Rectangle2D(window.getX(), window.getY(), window.getWidth(), window.getHeight());

        List<Screen> screens = Screen.getScreensForRectangle(windowRectangle);
        //find screen for window bounds, primary screen if no screen is found
        Screen screen = screens.stream()
                .findFirst()
                .orElse(Screen.getPrimary());
        return screen;
    }

    private ChangeListener<Boolean> blurListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            blurOwner(newValue);
        }
    };

    public DialogSkin(Dialog<R> pDialog) {
        this(pDialog, true);
    }

    public Stage getStage() {
        return (Stage) dialog.getDialogPane().getScene().getWindow();
    }
//    //dummy getting of comonents (headerGrid/buttonbar) 
//    //if header is set somewhere else grid will be overwritten
//    //risky and workaround to avoid other dumb shit
//    protected void getComponents(){
//        for(Node node : dialog.getDialogPane().getChildren()){
//            if(node instanceof GridPane){
//                node.setId("headerTextPanel");
//                headerGrid = node;
//                continue;
//            }
//            if(node instanceof ButtonBar){
//                node.setId("buttonBar");
//                buttonBar = node;
//                continue;
//            }
//        }
//    }

    /*
    *blure owner window if set
     */
    public void blurOwner(boolean pBlur) {
        Window owner = dialog.getOwner();
        if (owner != null && !dialog.getModality().equals(Modality.NONE)) {
            dialog.getOwner().getScene().getRoot().setEffect(new ColorAdjust(0.0, 0.0, pBlur ? -0.5 : 0.0, 0.0));
        }
    }

    /**
     * sets the title in the skinnded layout
     *
     * @param pTitle title to set
     */
    public void setTitle(String pTitle) {
        dialog.getDialogPane().setHeaderText(pTitle);
    }

    /**
     * add button types to the layout buttontypes cant be added multiple times
     *
     * @param types button types to add
     */
    public void addButtonTypes(ButtonType... types) {
        for (ButtonType type : types) {
            removeButton(type);
        }
        dialog.getDialogPane().getButtonTypes().addAll(types);
    }

    /**
     * get the currently set button types
     *
     * @return list of buttontypes
     */
    public ObservableList<ButtonType> getButtonTypes() {
        return dialog.getDialogPane().getButtonTypes();
    }

    /**
     * get a specific button by its buttonType null if no button is found
     *
     * @param pType type of button to look up
     * @return button for that buttontype
     */
    public Button getButton(ButtonType pType) {
        return (Button) dialog.getDialogPane().lookupButton(pType);
    }

    /**
     * remove the buttontype from the pane
     *
     * @param pType type to remove
     * @return indicator if remove was successful
     */
    public boolean removeButton(ButtonType pType) {
        if (getButtonTypes().contains(pType)) {
            return getButtonTypes().remove(pType);
        }
        return false;
    }

    /**
     * gets the disableProperty of the save button for binding
     *
     * @return property or null if no safe button is present in the dialog
     */
    public BooleanProperty getSaveButtonDisableProperty() {
        Button saveBtn = getButton(ButtonType.OK);
        if (saveBtn == null) {
            return null;
        }
        return saveBtn.disableProperty();
    }

    /**
     * sets new list of buttontypes replaces the old ones ui may not be updated
     * if dialog is already shown is an empty list is given an CANCEL button is
     * set instead
     *
     * @param pButtonTypes list of new buttontypes if empty a CANCEL button is
     * set
     */
    public void setButtonTypes(ButtonType... pButtonTypes) {
        getButtonTypes().clear();
        getButtonTypes().addAll(pButtonTypes.length == 0 ? new ButtonType[]{ButtonType.CANCEL} : pButtonTypes);
    }

    /**
     * checks if a button with CANCEL_CLOSE is present this button contains the
     * close logic in javafx for dialogs, is no such button present, Dialogs
     * will not accept any close requests
     *
     * @return indicator if an close button is present
     */
    public boolean containsCancelButton() {
        DialogPane dialogPane = dialog.getDialogPane();
        for (ButtonType type : dialogPane.getButtonTypes()) {
            if (type.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param pMinWidht max width of the window
     */
    public void setMinWidth(double pMinWidht) {
        ((Stage) dialog.getDialogPane().getScene().getWindow()).setMinWidth(pMinWidht);
    }

    /**
     * @return max width of the window
     */
    public double getMinWidth() {
        return ((Stage) dialog.getDialogPane().getScene().getWindow()).getMinWidth();
    }

    /**
     * @return min width property
     */
    public DoubleProperty getMinWidthProperty() {
        return ((Stage) dialog.getDialogPane().getScene().getWindow()).minWidthProperty();
    }

    /**
     * @param pMaxWidht max width of the window
     */
    public void setMaxWidth(double pMaxWidht) {
        ((Stage) dialog.getDialogPane().getScene().getWindow()).setMaxWidth(pMaxWidht);
//        dialog.getDialogPane().setMaxWidth(pMaxWidht);
    }

    /**
     * @return max width of the window
     */
    public double getMaxWidth() {
        return ((Stage) dialog.getDialogPane().getScene().getWindow()).getMaxWidth();
    }

    /**
     * @return max width property
     */
    public DoubleProperty getMaxWidthProperty() {
        return ((Stage) dialog.getDialogPane().getScene().getWindow()).maxWidthProperty();
    }

    /**
     * @param pMinHeight min width of the window
     */
    public void setMinHeight(double pMinHeight) {
        ((Stage) dialog.getDialogPane().getScene().getWindow()).setMinHeight(pMinHeight);
    }

    /**
     * @return min width of the window
     */
    public double getMinHeight() {
        return ((Stage) dialog.getDialogPane().getScene().getWindow()).getMinHeight();
    }

    /**
     * @return min height property
     */
    public DoubleProperty getMinHeightProperty() {
        return ((Stage) dialog.getDialogPane().getScene().getWindow()).minHeightProperty();
    }

    /**
     * @param pMaxHeight max height of the window
     */
    public void setMaxHeight(double pMaxHeight) {
        ((Stage) dialog.getDialogPane().getScene().getWindow()).setMaxHeight(pMaxHeight);
    }

    /**
     * @return max height of the window
     */
    public double getMaxHeight() {
        return ((Stage) dialog.getDialogPane().getScene().getWindow()).getMaxHeight();
    }

    /**
     * @return max height property
     */
    public DoubleProperty getMaxHeightProperty() {
        return ((Stage) dialog.getDialogPane().getScene().getWindow()).maxHeightProperty();
    }

    /**
     * Apply style class to dialog
     *
     * @param pDialog dialog to style
     * @param pStyleClass style class, must are put in ressources folder as .css
     * like /styles/cpx-default.css
     */
    public void applyStyleClass(Dialog<R> pDialog, String pStyleClass) {
        pDialog.getDialogPane().getStylesheets().add(DialogSkin.class.getResource(pStyleClass).toExternalForm());
    }

    private void addDefaultButton() {
        if (!dialog.getDialogPane().getButtonTypes().contains(ButtonType.CANCEL)) {
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        }
    }

    public void dispose() {
        dialog.getDialogPane().getButtonTypes().clear();
        dialog.showingProperty().removeListener(blurListener);
        dialogPaneSkin.dispose();
    }
}

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
 * Includes:
 * Copyright (c) 2014, 2015 ControlsFX
 * All rights reserved.
 *
 * Contributors:
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.wizard;

import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.dialog.DialogPaneSkin;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.validation.ValidationSupport;

/**
 * Implementation of the wizard class Basically ControlsFx Wizard from version
 * 8.40.20 But that Wizard do not match the needs, dialog pane is not accessable
 * should switch to that implementation once it is more suitable AWi 20170117
 *
 * @author wilde
 */
public class Wizard {

    private static final Logger LOG = Logger.getLogger(Wizard.class.getName());

    /**
     * ************************************************************************
     *
     * Private fields
     *
     *************************************************************************
     */
    private Dialog<ButtonType> dialog;

//    private final ObservableMap<String, Object> settings = FXCollections.observableHashMap();
    private final Stack<WizardPane> pageHistory = new Stack<>();

    public Optional<WizardPane> currentPage = Optional.empty();

    private final BooleanProperty invalidProperty = new SimpleBooleanProperty(false);

    public static final ButtonType BUTTON_PREVIOUS = new ButtonType(Lang.getMenuWizardBack(), ButtonBar.ButtonData.BACK_PREVIOUS); //$NON-NLS-1$
    private final EventHandler<ActionEvent> buttonPreviousActionHandler = actionEvent -> {
        actionEvent.consume();
        currentPage = Optional.ofNullable(pageHistory.isEmpty() ? null : pageHistory.pop());
        updatePage(dialog, false);
    };

    public Callback<Void, Boolean> onCheckBusy;
    public static final ButtonType BUTTON_NEXT = new ButtonType(Lang.getMenuWizardNext(), ButtonBar.ButtonData.NEXT_FORWARD); //$NON-NLS-1$
    private final EventHandler<ActionEvent> buttonNextActionHandler = actionEvent -> {
        if(!actionEvent.isConsumed()){
            actionEvent.consume();
            boolean busy = onCheckBusy == null?false:onCheckBusy.call(null);
            LOG.log(Level.FINEST, "isBusy: " + String.valueOf(busy));
            if(busy){
                return;
            }
            currentPage.ifPresent(page -> pageHistory.push(page));


            if (currentPage.get() instanceof ValidatedWizardPane) {
                //here some check
                if (!((ValidatedWizardPane) currentPage.get()).validationSupport.isInvalid()
                        ) {
                    currentPage = getFlow().advance(currentPage.orElse(null));
                    updatePage(dialog, true);
                    return;
                }
            }
            currentPage = getFlow().advance(currentPage.orElse(null));
            updatePage(dialog, true);
        }
    };
    private EventHandler<ActionEvent> buttonFinishActionHandler;

    private final StringProperty titleProperty = new SimpleStringProperty();
    // --- flow
    /**
     * The {@link Flow} property represents the flow of pages in the wizard.
     */
    private ObjectProperty<Flow> flow = new SimpleObjectProperty<Flow>(new LinearFlow()) {
        @Override
        protected void invalidated() {
            updatePage(dialog, false);
        }

        @Override
        public void set(Flow flow) {
            super.set(flow);
            pageHistory.clear();
            if (flow != null) {
                currentPage = flow.advance(currentPage.orElse(null));
                updatePage(dialog, true);
            }
        }
    ;

    };
    
    public void setOnCheckBusy(Callback<Void, Boolean> pBusy){
        onCheckBusy = pBusy;
    }
    
        /**
     * Creates an instance of the wizard without an owner.
     */
    public Wizard() {
        this(null);
    }

    /**
     * Creates an instance of the wizard with the given owner.
     *
     * @param owner The object from which the owner window is deduced (typically
     * this is a Node, but it may also be a Scene or a Stage).
     */
    public Wizard(Object owner) {
        this(owner, Modality.WINDOW_MODAL, ""); //$NON-NLS-1$
    }

    /**
     * Creates an instance of the wizard with the given owner and title.
     *
     * @param owner The object from which the owner window is deduced (typically
     * this is a Node, but it may also be a Scene or a Stage).
     * @param pModatity modatity of the dialog
     * @param title The wizard title.
     */
    public Wizard(Object owner, Modality pModatity, String title) {

        invalidProperty.addListener((o, ov, nv) -> validateActionState());

        dialog = new Dialog<>();

        dialog.titleProperty().bind(this.titleProperty);
        setTitle(title);

        Window window = null;
        if (owner instanceof Window) {
            window = (Window) owner;
        } else if (owner instanceof Node) {
            window = ((Node) owner).getScene().getWindow();
        }

        dialog.initOwner(window);
        dialog.initModality(pModatity);
        dialog.headerTextProperty().bindBidirectional(titleProperty);
        ClipboardEnabler.installClipboardToScene(this.getDialog().getDialogPane().getScene());
    }

    /**
     * get the dialog instance
     *
     * @return dialog set in the wizard
     */
    public final Dialog<ButtonType> getDialog() {
        return dialog;
    }

    /**
     * ************************************************************************
     *
     * Public API
     *
     *************************************************************************
     */
//    /**
//     * Shows the wizard but does not wait for a user response (in other words,
//     * this brings up a non-blocking dialog). Users of this API must either
//     * poll the {@link #resultProperty() result property}, or else add a listener
//     * to the result property to be informed of when it is set.
//     */
//    public final void show() {
//        dialog.show();
//    }
    /**
     * Shows the wizard and waits for the user response (in other words, brings
     * up a blocking dialog, with the returned value the users input).
     *
     * @return An {@link Optional} that contains the result.
     */
    public final Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }

    /**
     * @return {@link Dialog#resultProperty()} of the {@link Dialog}
     * representing this {@link Wizard}.
     */
    public final ObjectProperty<ButtonType> resultProperty() {
        return dialog.resultProperty();
    }

//    /**
//     * The settings map is the place where all data from pages is kept once the 
//     * user moves on from the page, assuming there is a {@link ValueExtractor} 
//     * that is capable of extracting a value out of the various fields on the page. 
//     */
//    public final ObservableMap<String, Object> getSettings() {
//        return settings;
//    }
//    
    /**
     * ************************************************************************
     *
     * Properties
     *
     *************************************************************************
     */
    // --- title
    /**
     * Return the titleProperty of the wizard.
     *
     * @return title string property
     */
    public final StringProperty titleProperty() {
        return titleProperty;
    }

    /**
     * Return the title of the wizard.
     *
     * @return title of the wizard
     */
    public final String getTitle() {
        return titleProperty.get();
    }

    /**
     * Change the Title of the wizard.
     *
     * @param title the title
     */
    public final void setTitle(String title) {
        titleProperty.set(title);
    }

    public final ObjectProperty<Flow> flowProperty() {
        return flow;
    }

    /**
     * Returns the currently set {@link Flow}, which represents the flow of
     * pages in the wizard.
     *
     * @return the current flow in the wizard
     */
    public final Flow getFlow() {
        return flow.get();
    }

    /**
     * Sets the {@link Flow}, which represents the flow of pages in the wizard.
     *
     * @param flow set the current flow
     */
    public final void setFlow(Flow flow) {
        this.flow.set(flow);
    }

    /**
     * Sets the value of the property {@code invalid}.
     *
     * @param invalid The new validation state {@link #invalidProperty() }
     */
    public final void setInvalid(boolean invalid) {
        invalidProperty.set(invalid);
    }

    /**
     * Gets the value of the property {@code invalid}.
     *
     * @return The validation state
     * @see #invalidProperty()
     */
    public final boolean isInvalid() {
        return invalidProperty.get();
    }

    /**
     * Property for overriding the individual validation state of this
     * {@link Wizard}. Setting {@code invalid} to true will disable the
     * next/finish Button and the user will not be able to advance to the next
     * page of the {@link Wizard}. Setting {@code invalid} to false will enable
     * the next/finish Button. <br>
     * <br>
     * For example you can use the {@link ValidationSupport#invalidProperty()}
     * of a page and bind it to the {@code invalid} property: <br> null	null
     * null	null	null	null	null	null	null	null null null null null null null
     * null null null null null null null null null null null null null null     {@code
     * wizard.invalidProperty().bind(page.validationSupport.invalidProperty());
     * }
     *
     * @return The validation state property
     */
    public final BooleanProperty invalidProperty() {
        return invalidProperty;
    }

    /**
     * setOnFinishEvent handler to determine behavior if finish is clicked
     *
     * @param pEventHandler handler to set
     */
    public void setOnFinish(EventHandler<ActionEvent> pEventHandler) {
        buttonFinishActionHandler = pEventHandler;
    }

//    
//        /**
//     * Sets the value of the property {@code readSettings}.
//     * 
//     * @param readSettings The new read-settings state
//     * @see #readSettingsProperty()
//     */
//    public final void setReadSettings(boolean readSettings) {
//        readSettingsProperty.set(readSettings);
//    }
//    
//    /**
//     * Gets the value of the property {@code readSettings}.
//     * 
//     * @return The read-settings state
//     * @see #readSettingsProperty()
//     */
//    public final boolean isReadSettings() {
//        return readSettingsProperty.get();
//    }
//    
//    /**
//    * Property for overriding the individual read-settings state of this {@link Wizard}.
//    * Setting {@code readSettings} to true will enable the value extraction for this
//    * {@link Wizard}. Setting {@code readSettings} to false will disable the value
//    * extraction for this {@link Wizard}.
//    *
//    * @return The readSettings state property
//    */
//    public final BooleanProperty readSettingsProperty() {
//        return readSettingsProperty;
//    }

    /**
     * ************************************************************************
     *
     * Private implementation
     *
     *************************************************************************
     */
    private void updatePage(Dialog<ButtonType> dialog, boolean advancing) {
        Flow flow = getFlow();
        if (flow == null) {
            return;
        }

        Optional<WizardPane> prevPage = Optional.ofNullable(pageHistory.isEmpty() ? null : pageHistory.peek());
        prevPage.ifPresent(page -> {
            // if we are going forward in the wizard, we read in the settings 
            // from the page and store them in the settings map.
            // If we are going backwards, we do nothing
            // This is only performed if readSettings is true.
//	        if (advancing && isReadSettings()) {
//	        	readSettings(page);
//	        }

            // give the previous wizard page a chance to update the pages list
            // based on the settings it has received
            page.onExitingPage(this);
        });

        currentPage.ifPresent(currentPage -> {
            // put in default actions
            List<ButtonType> buttons = currentPage.getButtonTypes();
            if (!buttons.contains(BUTTON_PREVIOUS)) {
                buttons.add(BUTTON_PREVIOUS);
                Button button = (Button) currentPage.lookupButton(BUTTON_PREVIOUS);
                button.addEventFilter(ActionEvent.ACTION, buttonPreviousActionHandler);
            }
            if (!buttons.contains(BUTTON_NEXT)) {
                buttons.add(BUTTON_NEXT);
                Button button = (Button) currentPage.lookupButton(BUTTON_NEXT);
                button.addEventFilter(ActionEvent.ACTION, buttonNextActionHandler);
            }
            if (!buttons.contains(ButtonType.FINISH)) {
                buttons.add(ButtonType.FINISH);
                if (buttonFinishActionHandler != null) {
                    Button button = (Button) currentPage.lookupButton(ButtonType.FINISH);
                    button.addEventFilter(ActionEvent.ACTION, buttonFinishActionHandler);
                }
            }
            if (!buttons.contains(ButtonType.CANCEL)) {
                buttons.add(ButtonType.CANCEL);
            }

            // then give user a chance to modify the default actions
            currentPage.onEnteringPage(this);

            // Remove from DecorationPane which has been created by e.g. validation
            if (currentPage.getParent() != null && currentPage.getParent() instanceof Pane) {
                Pane parentOfCurrentPage = (Pane) currentPage.getParent();
                parentOfCurrentPage.getChildren().remove(currentPage);
            }

            // Get current position and size
            double previousX = dialog.getX();
            double previousY = dialog.getY();
            double previousWidth = dialog.getWidth();
            double previousHeight = dialog.getHeight();
            // and then switch to the new pane
            dialog.setDialogPane(currentPage);
            // Resize Wizard to new page
            Window wizard = currentPage.getScene().getWindow();
            wizard.sizeToScene();
            // Center resized Wizard to previous position

//            if (!Double.isNaN(previousX) && !Double.isNaN(previousY)) {
//                double newWidth = dialog.getWidth();
//                double newHeight = dialog.getHeight();
//                int newX = (int) (previousX + (previousWidth / 2.0) - (newWidth / 2.0));
//                int newY = (int) (previousY + (previousHeight / 2.0) - (newHeight / 2.0));
//
//                ObservableList<Screen> screens = Screen.getScreensForRectangle(previousX, previousY, 1, 1);
//                Screen screen = screens.isEmpty() ? Screen.getPrimary() : screens.get(0);
//                Rectangle2D scrBounds = screen.getBounds();
//                int minX = (int)Math.round(scrBounds.getMinX());
//                int maxX = (int)Math.round(scrBounds.getMaxX());
//                int minY = (int)Math.round(scrBounds.getMinY());
//                int maxY = (int)Math.round(scrBounds.getMaxY());
//                if(newX + newWidth > maxX) {
//                    newX = maxX - (int)Math.round(newWidth);
//                }
//                if(newY + newHeight > maxY) {
//                    newY = maxY - (int)Math.round(newHeight);
//                }                
//                if(newX < minX) {
//                    newX = minX;
//                }
//                if(newY < minY) {
//                    newY = minY;
//                }
//
//                dialog.setX(newX);
//                dialog.setY(newY);
//            }
        });

        validateActionState();
    }

    private void validateActionState() {
        final List<ButtonType> currentPaneButtons = dialog.getDialogPane().getButtonTypes();

        if (getFlow().canAdvance(currentPage.orElse(null))) {
            currentPaneButtons.remove(ButtonType.FINISH);
        } else {
            currentPaneButtons.remove(BUTTON_NEXT);
        }

        validateButton(BUTTON_PREVIOUS, () -> pageHistory.isEmpty());
        validateButton(BUTTON_NEXT, new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return invalidProperty.get();
            }
        });
        validateButton(ButtonType.FINISH, () -> invalidProperty.get());

    }

    // Functional design allows to delay condition evaluation until it is actually needed 
    private void validateButton(ButtonType buttonType, BooleanSupplier condition) {
        Button btn = (Button) dialog.getDialogPane().lookupButton(buttonType);
        if (btn != null) {
            Node focusOwner = (btn.getScene() != null) ? btn.getScene().getFocusOwner() : null;
            btn.setDisable(condition.getAsBoolean());
            if (focusOwner != null) {
                focusOwner.requestFocus();
            }
        }
    }

    /**
     * LinearFlow is an implementation of the {@link Wizard.Flow} interface,
     * designed to support the most common type of wizard flow - namely, a
     * linear wizard page flow (i.e. through all pages in the order that they
     * are specified). Therefore, this {@link Flow} implementation simply
     * traverses a collections of {@link WizardPane WizardPanes}.
     *
     * <p>
     * For example of how to use this API, please refer to the {@link Wizard}
     * documentation</p>
     *
     * @see Wizard
     * @see WizardPane
     */
    public static class LinearFlow implements Wizard.Flow {

        private final List<WizardPane> pages;

        /**
         * Creates a new LinearFlow instance that will allow for stepping
         * through the given collection of {@link WizardPane} instances.
         *
         * @param pages pages of the wizard
         */
        public LinearFlow(Collection<WizardPane> pages) {
            this.pages = new ArrayList<>(pages);
        }

        /**
         * Creates a new LinearFlow instance that will allow for stepping
         * through the given varargs array of {@link WizardPane} instances.
         *
         * @param pages pages of the wizard
         */
        public LinearFlow(WizardPane... pages) {
            this(Arrays.asList(pages));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<WizardPane> advance(WizardPane currentPage) {
            int pageIndex = pages.indexOf(currentPage);
            return Optional.ofNullable(pages.get(++pageIndex));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean canAdvance(WizardPane currentPage) {
            int pageIndex = pages.indexOf(currentPage);
            return pages.size() - 1 > pageIndex;
        }

    }

    public class WizardPane extends DialogPane {

        private final DialogPaneSkin skin;

        public WizardPane() {
            super();
            skin = new DialogPaneSkin(this);
        }

        public DialogPaneSkin getSkin() {
            return skin;
        }

        /**
         * Creates an instance of wizard pane.
         *
         * @param pContent content of the wizard pane
         */
        public WizardPane(Parent pContent) {
            this();
            setContent(pContent);
        }

        /**
         * Creates an instance of wizard pane.
         *
         * @param pContent content of the wizard pane
         */
        public WizardPane(CpxScene pContent) {
            this(pContent.getRoot());
//           this.content = pContent.getRoot();
//           content.setStyle("-fx-background-color:red;");
//           setContent(content);
        }

        /**
         * Called on entering a page. This is a good place to read values from
         * wizard settings and assign them to controls on the page
         *
         * @param wizard which page will be used on
         */
        public void onEnteringPage(Wizard wizard) {
            // no-op
        }

        /**
         * Called on existing the page. This is a good place to read values from
         * page controls and store them in wizard settings
         *
         * @param wizard which page was used on
         */
        public void onExitingPage(Wizard wizard) {
            // no-op
        }
    }

    /**
     * Represents the page flow of the wizard. It defines only methods required
     * to move forward in the wizard logic, as backward movement is
     * automatically handled by wizard itself, using internal page history.
     */
    public interface Flow {

        /**
         * Advances the wizard to the next page if possible.
         *
         * @param currentPage The current wizard page
         * @return {@link Optional} value containing the next wizard page.
         */
        Optional<WizardPane> advance(WizardPane currentPage);

        /**
         * Check if advancing to the next page is possible
         *
         * @param currentPage The current wizard page
         * @return true if it is possible to advance to the next page, false
         * otherwise.
         */
        boolean canAdvance(WizardPane currentPage);

    }

    public class ValidatedWizardPane<T extends Controller<CpxScene>> extends WizardPane {

        private EventHandler<ActionEvent> onEnteringPage;
        private EventHandler<ActionEvent> onExitingPage;
        private T controller;
        private ValidationSupport validationSupport;
        private final ReadOnlyBooleanWrapper isVisited = new ReadOnlyBooleanWrapper(false);
        public ValidatedWizardPane(T pController, ValidationSupport sup) {
            this(pController.getScene().getRoot(), sup);
            validationSupport = sup;

        }

        public ValidatedWizardPane(Parent pParent, ValidationSupport sup) {
            super(pParent);
//            super();
            validationSupport = sup;
        }

        @Override
        public void onEnteringPage(Wizard wizard) {
            overwatchValidation(validationSupport);
            if (onEnteringPage != null) {
                onEnteringPage.handle(new ActionEvent());
                isVisited.set(true);
            }
        }

        @Override
        public void onExitingPage(Wizard wizard) {
            if (onExitingPage != null) {
                onExitingPage.handle(new ActionEvent());
            }

        }

        public void setOnEnteringPane(EventHandler<ActionEvent> pEvent) {
            onEnteringPage = pEvent;
        }

        public void setOnExitingPage(EventHandler<ActionEvent> pEvent) {
            onExitingPage = pEvent;
        }

        public T getController() {
            return controller;
        }

        private void overwatchValidation(ValidationSupport pValidationSupport) {
            invalidProperty().unbind();
            invalidProperty().set(true);
            invalidProperty().bind(pValidationSupport.invalidProperty());
        }
        
        public ReadOnlyBooleanProperty isVisitedProperty(){
            return isVisited.getReadOnlyProperty();
        }
        public boolean isVisited(){
            return isVisitedProperty().get();
        }
    }

}

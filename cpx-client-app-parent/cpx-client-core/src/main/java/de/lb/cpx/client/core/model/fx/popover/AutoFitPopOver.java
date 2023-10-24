/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.popover;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.controlsfx.control.PopOver;

/**
 * Basic Implementation of an auto-fit popover Popover Content is placed based
 * on ArrowLocationProperty Sometimes available Space is insufficent for
 * ArrowLocation this class tries to auto fit it's content to new/more propper
 * arrowLocation
 *
 * can specify defaultArrowLocation, on each show it is checked if content would
 * fit on default location if enough space is available than content is
 * shown,otherwise content will be replaced
 *
 * DefaultArrowLocation- TOP-CENTER(content will be displayed beneath owner
 * node)
 *
 * NOTE: Fitting is done in onShowingEvent, it should be overriden with an other
 * setOnShowEvent! If have to use protected event "onShowEvent" to call
 * repositioning
 *
 * TODO: -smarter implemenation of available space for arrowlocation -smarter
 * relocation, if space is insufficent the arrowLocation will simply be inverted
 * - there is not additional check if that is enough to display the content
 * properly
 *
 * @author wilde
 */
public class AutoFitPopOver extends PopOver {

    private final ReadOnlyObjectWrapper<ArrowLocation> defaultArrowLocationProperty = new ReadOnlyObjectWrapper<>(ArrowLocation.TOP_CENTER);

    public ReadOnlyObjectProperty<ArrowLocation> defaultArrowLocation() {
        return defaultArrowLocationProperty.getReadOnlyProperty();
    }

    public ArrowLocation getDefaultArrowLocation() {
        return defaultArrowLocationProperty.get();
    }

    public void setDefaultArrowLocation(ArrowLocation pLocation) {
        if (pLocation == null) {
            pLocation = ArrowLocation.TOP_CENTER;
        }
        defaultArrowLocationProperty.set(pLocation);
    }

    private static final Logger LOG = Logger.getLogger(AutoFitPopOver.class.getName());
    protected EventHandler<WindowEvent> onShowingEvent = new EventHandler<>() {
        @Override
        public void handle(WindowEvent event) {
//            adjustAlignment();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(Orientation.HORIZONTAL.equals(getFitOrientation())){
                        adjustArrowLocationHorizontally();
                    }else{
                        adjustArrowLocationVertically();
                    }
                }
            });
        }
    };
    protected ChangeListener<ArrowLocation> ARROW_LOCATION_LISTENER = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends ArrowLocation> observable, ArrowLocation oldValue, ArrowLocation newValue) {
            setDefaultArrowLocation(newValue);
        }
    };

    public AutoFitPopOver() {
        super();
        ClipboardEnabler.installClipboardToScene(this.getScene());
        setArrowLocation(getDefaultArrowLocation());
        arrowLocationProperty().addListener(ARROW_LOCATION_LISTENER);
        //set Reposition when popover should show
        setOnShowing(onShowingEvent);
    }

    public AutoFitPopOver(Node content) {
        this();
        setContentNode(content);
    }

    public void destory() {
        setOnShowing(null);
        onShowingEvent = null;
        if(ARROW_LOCATION_LISTENER != null){
            arrowLocationProperty().removeListener(ARROW_LOCATION_LISTENER);
        }
        ARROW_LOCATION_LISTENER = null;
    }
    public ArrowLocation getAdjustedLocation(){
        return getAdjustedLocation(getDefaultArrowLocation());
    }
    
    public ArrowLocation getAdjustedLocation(ArrowLocation pDefaultLocation){
        if(Orientation.HORIZONTAL.equals(getFitOrientation())){
            ArrowLocation adjustValue = adjustArrowLocationHorizontal(getContentNode().localToScreen(getContentNode().getBoundsInLocal()));
            if (!adjustValue.equals(getArrowLocation())) {
                return adjustValue;
            }
        }else{
            ArrowLocation adjustValue = adjustArrowLocationVertical(getContentNode().localToScreen(getContentNode().getBoundsInLocal()));
            if (!adjustValue.equals(getArrowLocation())) {
                return adjustValue;
            }
        }
        return pDefaultLocation;
    }
    public void adjustAlignment(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(Orientation.HORIZONTAL.equals(getFitOrientation())){
                    ArrowLocation adjustValue = adjustArrowLocationHorizontal(getContentNode().localToScreen(getContentNode().getBoundsInLocal()));
                    if (!adjustValue.equals(getArrowLocation())) {
                        setArrowLocation(adjustValue);
                    }
                }else{
                    ArrowLocation adjustValue = adjustArrowLocationVertical(getContentNode().localToScreen(getContentNode().getBoundsInLocal()));
                    if (!adjustValue.equals(getArrowLocation())) {
                        setArrowLocation(adjustValue);
                    }
                }
            }
        });
    }
    public void adjustArrowLocationHorizontally() {
        Bounds bounds = getContentNode().localToScreen(getContentNode().getBoundsInLocal());
        if(bounds == null){
            return;
        }
        ArrowLocation adjustValue = adjustArrowLocationHorizontal(bounds);
        if (!adjustValue.equals(getArrowLocation())) {
            LOG.finer("adjust popover position");
            setArrowLocation(adjustValue);
            if(x == null || y == null){
                show(getOwnerNode());
            }else{
                show(getOwnerNode(),x,y);
            }
        }
    }

    public void adjustArrowLocationVertically() {
        ArrowLocation adjustValue = adjustArrowLocationVertical(getContentNode().localToScreen(getContentNode().getBoundsInLocal()));
        if (!adjustValue.equals(getArrowLocation())) {
            LOG.finer("adjust popover position");
            setArrowLocation(adjustValue);
            if(x == null || y == null){
                show(getOwnerNode());
            }else{
                show(getOwnerNode(),x,y);
            }
        }
    }
    private Double x;
    public void setXPos(double pX){
        x = pX;
    }
    private Double y;
    public void setYPos(double pY){
        y = pY;
    }
    protected void blurStage(Stage pStage, boolean pBlur) {
        //Blue like dialog
        pStage.getScene().getRoot().setEffect(new ColorAdjust(0.0, 0.0, pBlur ? -0.5 : 0.0, 0.0));
    }
    
    private ArrowLocation adjustArrowLocationVertical(Bounds pBounds) {
        
        if (checkArrowLocationVertical(getDefaultArrowLocation(), pBounds)) {//getContentNode().getLayoutBounds())) {
            if (!getArrowLocation().equals(getDefaultArrowLocation())) {
                LOG.finer("change vertical arrow location to default");
            }
            LOG.finer("restore vertical default arrow location");
            return getDefaultArrowLocation();
        }
        ArrowLocation computedLocation = invertLocation(getArrowLocation());
        if (checkArrowLocation(computedLocation, getContentNode().getLayoutBounds())) {
            //content do not fit default location 
            //try inverted
            LOG.finer("invert vertical arrowlocation");
            return computedLocation;
        }
        return getArrowLocation();
    }
    private ArrowLocation adjustArrowLocationHorizontal(Bounds pBounds) {
        if (checkArrowLocationHorizontal(getDefaultArrowLocation(), pBounds)) {//getContentNode().getLayoutBounds())) {
            if (!getArrowLocation().equals(getDefaultArrowLocation())) {
                LOG.finer("change horizonal arrow location to default");
            }
            LOG.finer("restore horizontal default arrow location");
            return getDefaultArrowLocation();
        }
        ArrowLocation computedLocation = invertLocationHorizontally(getArrowLocation());
        if (checkArrowLocationHorizontal(computedLocation, getContentNode().getLayoutBounds())) {
            //content do not fit default location 
            //try inverted
            LOG.finer("invert horizontal arrowlocation");
            return computedLocation;
        }else{
            ArrowLocation computedArrowLocation2 = invertLocationHorizontally(computedLocation);
            if(checkArrowLocationHorizontal(computedArrowLocation2, getContentNode().getLayoutBounds())){
                return computedArrowLocation2;
            }
        }
        return getArrowLocation();
    }
    private boolean checkArrowLocation(PopOver.ArrowLocation pLocation, Bounds pBounds) {
        switch (pLocation) {
            case TOP_CENTER:
            case TOP_LEFT:
            case TOP_RIGHT:
                return checkTop(pBounds);
            case LEFT_CENTER:
            case LEFT_TOP:
            case LEFT_BOTTOM:
                return checkLeft(pBounds);
            case RIGHT_CENTER:
            case RIGHT_TOP:
            case RIGHT_BOTTOM:
                return checkRight(pBounds);
            case BOTTOM_CENTER:
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                return checkBottom(pBounds);

        }
        return true;
    }
    
    private boolean checkArrowLocationVertical(PopOver.ArrowLocation pLocation, Bounds pBounds) {
        switch (pLocation) {
            case TOP_CENTER:
            case TOP_LEFT:
            case TOP_RIGHT:
            case RIGHT_TOP:
            case RIGHT_CENTER:
            case LEFT_TOP:
            case LEFT_CENTER:
                return checkTop(pBounds);
            case BOTTOM_CENTER:
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
            case LEFT_BOTTOM:
            case RIGHT_BOTTOM:
                return checkBottom(pBounds);

        }
        return true;
    }
    private boolean checkArrowLocationHorizontal(PopOver.ArrowLocation pLocation, Bounds pBounds) {
        switch (pLocation) {
            case RIGHT_TOP:
            case RIGHT_CENTER:
            case RIGHT_BOTTOM:
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                return checkLeft(pBounds);
            case BOTTOM_CENTER:
            case TOP_CENTER:
                return checkCenterHorizontal(pBounds);
            case LEFT_TOP:
            case LEFT_CENTER:
            case LEFT_BOTTOM:
            case BOTTOM_LEFT:
            case TOP_LEFT:
                return checkRight(pBounds);

        }
        return true;
    }
    private PopOver.ArrowLocation invertLocation(PopOver.ArrowLocation pLocation) {
        switch (pLocation) {
            case TOP_CENTER:
                return ArrowLocation.BOTTOM_CENTER;
            case TOP_LEFT:
                return ArrowLocation.BOTTOM_LEFT;
            case TOP_RIGHT:
                return ArrowLocation.BOTTOM_RIGHT;
            case LEFT_CENTER:
                return ArrowLocation.RIGHT_CENTER;
            case LEFT_TOP:
                return ArrowLocation.RIGHT_TOP;
            case LEFT_BOTTOM:
                return ArrowLocation.RIGHT_BOTTOM;
            case RIGHT_CENTER:
                return ArrowLocation.LEFT_CENTER;
            case RIGHT_TOP:
                return ArrowLocation.LEFT_TOP;
            case RIGHT_BOTTOM:
                return ArrowLocation.LEFT_BOTTOM;
            case BOTTOM_CENTER:
                return ArrowLocation.TOP_CENTER;
            case BOTTOM_LEFT:
                return ArrowLocation.TOP_LEFT;
            case BOTTOM_RIGHT:
                return ArrowLocation.TOP_RIGHT;
            default:
                return ArrowLocation.TOP_CENTER;
        }
    }
    private PopOver.ArrowLocation invertLocationHorizontally(PopOver.ArrowLocation pLocation) {
        switch (pLocation) {
            case TOP_CENTER:
                return ArrowLocation.TOP_LEFT;
            case TOP_LEFT:
                return ArrowLocation.TOP_RIGHT;
            case TOP_RIGHT:
                return ArrowLocation.TOP_CENTER;
            case LEFT_CENTER:
                return ArrowLocation.RIGHT_CENTER;
            case LEFT_TOP:
                return ArrowLocation.RIGHT_TOP;
            case LEFT_BOTTOM:
                return ArrowLocation.RIGHT_BOTTOM;
            case RIGHT_CENTER:
                return ArrowLocation.LEFT_CENTER;
            case RIGHT_TOP:
                return ArrowLocation.LEFT_TOP;
            case RIGHT_BOTTOM:
                return ArrowLocation.LEFT_BOTTOM;
            case BOTTOM_CENTER:
                return ArrowLocation.BOTTOM_RIGHT;
            case BOTTOM_LEFT:
                return ArrowLocation.BOTTOM_CENTER;
            case BOTTOM_RIGHT:
                return ArrowLocation.BOTTOM_LEFT;
            default:
                return ArrowLocation.TOP_CENTER;
        }
    }
    private ObjectProperty<Orientation> fitOrientationProperty;
    public ObjectProperty<Orientation> fitOrientationProperty(){
        if(fitOrientationProperty == null){
            fitOrientationProperty = new SimpleObjectProperty<>(Orientation.VERTICAL);
        }
        return fitOrientationProperty;
    }
    public Orientation getFitOrientation(){
        return fitOrientationProperty().get();
    }
    public void setFitOrientation(Orientation pOrientation){
        fitOrientationProperty().set(Objects.requireNonNullElse(pOrientation,Orientation.VERTICAL));
    }
    private boolean checkTop(Bounds pBounds) {
        double contentHeight = pBounds.getHeight();//getScene().getWindow().getHeight();
        Bounds boundsInScreen = getOwnerNode().localToScreen(getOwnerNode().getBoundsInLocal());
        if(boundsInScreen == null){
            LOG.warning("Bounds is null, can not compute location!");
            return true;
        }
        double windowHeight = BasicMainApp.getWindow().getHeight();
        double spaceY = boundsInScreen.getMaxY();
        checkLeft(pBounds);
        return (windowHeight - spaceY) >= contentHeight + getArrowSize() + 20;
    }

    private boolean checkBottom(Bounds pBounds) {
        double contentHeight = pBounds.getHeight();//getScene().getWindow().getHeight();
        Bounds boundsInScreen = getOwnerNode().localToScreen(getOwnerNode().getBoundsInLocal());
        if(boundsInScreen == null){
            LOG.warning("Bounds is null, can not compute location!");
            return true;
        }
        double spaceY = boundsInScreen.getMinY();
        return (spaceY) >= contentHeight + getArrowSize() + 20;
    }

    private boolean checkLeft(Bounds pBounds) {
        //untested dummy implementation
        //need to check left free space of node
        double contentWidth = pBounds.getWidth();
        Bounds boundsInScreen = getBoundsInScreen();//getOwnerNode().localToScene(getOwnerNode().getBoundsInLocal());
        if(boundsInScreen == null){
            LOG.warning("Bounds is null, can not compute location!");
            return true;
        }
        double spaceX = boundsInScreen.getMinX();
        double windowWidth = BasicMainApp.getWindow().getWidth();
        if(spaceX<(contentWidth/2)){
            return false;
        }
        //is enough free space in left screen area from border to show content
        return true;//(spaceX) >= contentWidth;
    }

    private boolean checkRight(Bounds pBounds) {
        //untested dummy implementation
        //need to check right free space of node
        double contentWidth = pBounds.getWidth();
        Bounds boundsInScreen = getBoundsInScreen(pBounds);//getOwnerNode().localToScene(pBounds);
        if(boundsInScreen == null){
            LOG.warning("Bounds is null, can not compute location!");
            return true;
        }
        double spaceX = boundsInScreen.getMinX();

        //get freespace right side, when x value is substracted from width of screen
        boundsInScreen.getMinX();
       boolean fit = (contentWidth/2)>spaceX+15;
       return fit;
    }

    private boolean checkCenterHorizontal(Bounds pBounds) {
        Bounds columnBounds = getBoundsInScreen();
        if(columnBounds == null){
            return true;
        }
        Bounds contentBounds = getOwnerNode().localToScene(getContentNode().getLayoutBounds());
        Bounds contentBounds2 = BasicMainApp.getWindow().getScene().getRoot().localToScene(getContentNode().getLayoutBounds());
        getContentNode().getLayoutBounds();
        BasicMainApp.getWindow().getScene().getRoot().getBoundsInLocal();
        getCurrentScreen(BasicMainApp.getWindow()).getBounds().getWidth();
        BasicMainApp.getWindow().getScene().getRoot().localToParent(getContentNode().getBoundsInLocal());
        Bounds pContentBounds = getOwnerNode().localToScene(getContentNode().getBoundsInLocal());
        double contentWidth = pBounds.getWidth();
        double windowWidth = BasicMainApp.getWindow().getWidth();
        double spaceX = pBounds.getMinX();
        double halfContent = (contentWidth/2);
        
        double magic16 = 16.0;
        double columnWidth = columnBounds.getWidth();
        double overLabWidth = Math.abs(contentWidth - columnWidth)/2;
        double test = ((columnBounds.getMaxX()-windowWidth)+magic16);
        boolean fit = test > overLabWidth; 
        checkRight(pBounds);
//        if((contentWidth/2)>spaceX+60){
//            return false; //not fit because of left
//        }
//        if((windowWidth-spaceX)<(halfContent+60)){
//            return false; // not fit because of right
//        }
        if(contentBounds.getMaxX() > windowWidth){
            return false; // not fit because of right
        }
        if(halfContent> contentBounds.getMinX() && halfContent > (columnBounds.getWidth()/2)){
            return false; // not fit because of left
        }
        return true;
    }
    private Bounds getBoundsInScreen(){
        if(getOwnerNode() != null){
            return getBoundsInScreen(getOwnerNode().getBoundsInLocal());
        }
        if(getOwnerWindow() != null){
            return getBoundsInScreen(getOwnerWindow().getScene().getRoot().getBoundsInLocal());
        }
        return null;
    }
    private Bounds getBoundsInScreen(Bounds pBounds){
        if(getOwnerNode() != null){
            return getOwnerNode().localToScene(pBounds);
        }
        if(getOwnerWindow() != null){
            return getOwnerWindow().getScene().getRoot().localToScene(pBounds);
        }
        return null;
    }
    public Screen getCurrentScreen(Window pWindow) {
        if (pWindow == null) {
            //should not occure
            return null;
        }
        Rectangle2D windowRectangle = new Rectangle2D(pWindow.getX(), pWindow.getY(), pWindow.getWidth(), pWindow.getHeight());

        List<Screen> screens = Screen.getScreensForRectangle(windowRectangle);
        //find screen for window bounds, primary screen if no screen is found
        Screen screen = screens.stream()
                .findFirst()
                .orElse(Screen.getPrimary());
        return screen;
    }
}

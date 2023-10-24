/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.webview;

import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.pane.LoadingPane;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

/**
 *
 * @author wilde
 */
public class LoadingWebView extends AsyncPane<StackPane>{

    private static final Logger LOG = Logger.getLogger(LoadingWebView.class.getName());

    private final String url;
    private final WebView webView;
    private final LoadingPane<StackPane> loadingPane;
    private StackPane pane;
    private int displayMode = 2;
    private int range = 4;
    private BooleanProperty mouseClicked = new SimpleBooleanProperty(false);
    private BooleanProperty mouseDoubleClicked = new SimpleBooleanProperty(false);


    
    public LoadingWebView(String pPatientNr, int pMode, int pRange, double pZoom, String pHost){
        super();
        displayMode = pMode;
        range = pRange;
        url = this.buildUrl(pPatientNr, pHost);
        webView = new WebView();
        webView.setZoom(pZoom);

        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> ov, State t, State t1) {
                switch(t1){
                    case CANCELLED:
                    case FAILED:
                    case SUCCEEDED:
                        LOG.log(Level.INFO, "URL: {0} was loaded with state: {1}", new Object[]{getUrl(), t1.name()});
                        webView.getEngine().getLoadWorker().stateProperty().removeListener(this);
                        pane.getChildren().remove(loadingPane);
                }
            }
        });
//        webView.getEngine().getLoadWorker().progressProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                LOG.info("loading: " + t1.doubleValue());
//            }
//        });
        loadingPane = new LoadingPane<>(this);
    }
    public String getUrl(){
        return url;
    }
    public WebView getWebView(){
        return webView;
    }
    @Override
    public StackPane loadContent() {
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webView.getEngine().load(getUrl());
            }
        });
//        loadingProperty.set(true);
//        while (loadingProperty.get()) {  
//            
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(LoadingWebView.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return webView;
        webView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 1){
                    mouseClicked.setValue(!mouseClicked.get());
                }else if(event.getClickCount() > 1){
                    mouseDoubleClicked.setValue(!mouseDoubleClicked.get());
                }
            }
           });
        
        pane = new StackPane(webView,loadingPane);
        return pane;
    }
    
    public BooleanProperty getMouseClickProperty(){
        return mouseClicked;
    }
    
   public BooleanProperty getMouseDoubleClickProperty(){
        return mouseDoubleClicked;
    }
    
    
    public final String buildUrl(String pPatNumber, String pHost){
        StringBuilder builder = new StringBuilder("http://" + pHost + ":8085/acg-web-app/?id=");
        builder.append(pPatNumber);
        builder.append("&range=").append(range).append("&mode=").append(displayMode);
        return builder.toString();
    }
}

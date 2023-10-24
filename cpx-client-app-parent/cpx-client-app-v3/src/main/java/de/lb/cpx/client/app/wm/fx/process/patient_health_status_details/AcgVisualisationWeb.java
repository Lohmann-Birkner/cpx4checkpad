/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.process.patient_health_status_details;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.webview.LoadingWebView;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author gerschmann
 */
public class AcgVisualisationWeb {
    private final String patientNumber; 
    private final int range;
    private final int mode;
    private double zoom;
    private static final Logger LOG = Logger.getLogger(AcgVisualisationWeb.class.getName());
    LoadingWebView webView = null;
    BooleanProperty mouseClick = null;
    BooleanProperty mouseDoubleClick = null;

    public AcgVisualisationWeb(String  pPatientNumber, int pRange, int pMode, double pZoom) {
        patientNumber = pPatientNumber;
        range = pRange;
        mode = pMode;
        zoom = pZoom;
        mouseClick = new SimpleBooleanProperty(false);
        mouseDoubleClick = new SimpleBooleanProperty(false);
    }

    
     public VBox getContent() {
         return getContent(checkConnection());
     }
    public VBox getContent(boolean hasAcgConnection) {
        
        final VBox vbox = new VBox();
        vbox.getStyleClass().add("health_status_visualization_box");
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(10.0d); 
        if(hasAcgConnection){
            webView = new LoadingWebView(patientNumber, mode, range, zoom, CpxClientConfig.instance().getServerHost());
            webView.prefWidthProperty().bind(vbox.widthProperty());
            webView.prefHeightProperty().bind(vbox.heightProperty());
            vbox.getChildren().add(webView);

            webView.getMouseClickProperty().addListener(new ChangeListener<Boolean> (){
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(mouseClick != null){
                        mouseClick.set(newValue);
                    }

                };
            });
            webView.getMouseDoubleClickProperty().addListener(new ChangeListener<Boolean> (){
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(mouseDoubleClick != null){
                        mouseDoubleClick.set(newValue);
                    }

                };
            });
        }else{
            
            final Text noDataLabel = new Text(mode == 4?"Keine Verbindung zu ACG Server":"");
            noDataLabel.setTextAlignment(TextAlignment.CENTER);
            vbox.getChildren().add(noDataLabel);
        }
        return vbox;
    }
    
    public BooleanProperty getMouseClick(){
        return mouseClick;
    }
    
    public BooleanProperty getMouseDoubleClick(){
        return mouseDoubleClick;
    }
    
     

    private boolean checkConnection() {
        try{
            // We want to check the current URL
            HttpURLConnection.setFollowRedirects(false);
            URL u = new URL("http://" + CpxClientConfig.instance().getServerHost() + ":8085/acg-web-app/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();

            // We don't need to get data
            httpURLConnection.setRequestMethod("HEAD");
            int responseCode = httpURLConnection.getResponseCode();

           // We check 404 only
           return responseCode != HttpURLConnection.HTTP_NOT_FOUND;    
        } catch(Exception ex){
            LOG.log(Level.SEVERE, "cound not connect with acg", ex);
            return false;
        
        }
    }

    public void reset() {
       webView = null;
       mouseClick = null;
       mouseDoubleClick = null;
    }
}

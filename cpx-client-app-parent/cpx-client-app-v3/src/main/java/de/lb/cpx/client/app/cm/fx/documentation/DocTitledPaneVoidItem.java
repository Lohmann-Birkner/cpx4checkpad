/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.documentation;

import de.lb.cpx.client.core.model.fx.titledpane.AccordionSelectedItem;
import de.lb.cpx.client.core.model.fx.button.LinkButton;
import de.lb.cpx.client.core.model.fx.titledpane.AccordionTitledPane;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author wilde
 * @param <T> Object Type
 */
public class DocTitledPaneVoidItem<T extends AbstractVersionEntity> extends AccordionSelectedItem<T>{

    private TextFlow textFlow;
    private final TCaseDetails suggestion;
    private static final int FONT_SIZE = 15;
    private final AccordionTitledPane.SelectionTarget target;
    public DocTitledPaneVoidItem(String pPlaceholder) {
        this(new Text(pPlaceholder));
    }
    public DocTitledPaneVoidItem(Text... pTexts) {
        this(AccordionTitledPane.SelectionTarget.ACCORDION_ITEM,null,pTexts);
//        flow.setTextAlignment(TextAlignment.CENTER);
    }
    public DocTitledPaneVoidItem(AccordionTitledPane.SelectionTarget pTarget,TCaseDetails pSuggestion,Text... pTexts) {
        super(null);
        updateFontSize(pTexts);
        textFlow = new TextFlow(pTexts);
        suggestion = pSuggestion;
        target = pTarget;
//        flow.setTextAlignment(TextAlignment.CENTER);
    }
    public String getPlaceholder() {
        return toString();
    }
    public TextFlow getTextFlow(){
        return textFlow;
    }

    public TCaseDetails getSuggestion() {
        return suggestion;
    }
    public AccordionTitledPane.SelectionTarget getTarget(){
        return target;
    }
    public Node getGraphic(){
        HBox box = new HBox(5,textFlow);
        box.setAlignment(Pos.BOTTOM_LEFT);
        if(suggestion != null){
            LinkButton btn = new LinkButton();
            btn.setStyle("-fx-padding:0;");
            btn.getGlyph().setFontSize(FONT_SIZE);
            btn.setTooltip(new Tooltip(VersionStringConverter.convertSimple(suggestion) + " öffnen und als 'Zur Risikobeurteilung verwendet' setzen"));
            btn.setOnAction(onLinkAction);
            box.getChildren().add(btn);
        }
        return box;
    }
    private static final Logger LOG = Logger.getLogger(DocTitledPaneVoidItem.class.getName());
    private static final EventHandler<ActionEvent> LINK_DEFAULT_ACTION = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            LOG.info("User clicked on Link-Button!");
        }
    };
    private EventHandler<ActionEvent> onLinkAction = LINK_DEFAULT_ACTION;
    public void setOnLinkAction(EventHandler<ActionEvent> pEvent){
        onLinkAction = Objects.requireNonNullElse(pEvent, LINK_DEFAULT_ACTION);
    }
    public EventHandler<ActionEvent> getOnLinkActtion(){
        return onLinkAction;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        textFlow.getChildren().stream()
                .filter(t -> Text.class.equals(t.getClass()))
                .forEach(t -> sb.append(((Text) t).getText()));
        return sb.toString();
    }

    private void updateFontSize(Text[] pTexts) {
        for(Text text : pTexts){
            text.setFont(Font.font(text.getFont().getFamily(),extractWeight(text),extractPosture(text),FONT_SIZE));
        }
    }
    /**
     * todo: extract posture part in combinded styles
     * @param pText text element to extract posture from
     * @return posture value
     */
    public FontPosture extractPosture(Text pText){
        if(pText == null){
            return FontPosture.REGULAR;
        }
        return FontPosture.findByName(pText.getFont().getStyle());
    }
    /**
     * todo: extract weight part in combinded styles
     * @param pText text element to extract weight from
     * @return white value
     */
    public FontWeight extractWeight(Text pText){
        if(pText == null){
            return FontWeight.NORMAL;
        }
        String stlye = pText.getFont().getStyle();
        return FontWeight.findByName(stlye);
    }
    
    
}

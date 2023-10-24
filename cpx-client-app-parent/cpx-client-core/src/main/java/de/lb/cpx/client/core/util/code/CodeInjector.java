/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util.code;

import static de.lb.cpx.client.core.util.code.CodeExtraction.getIcdCodeMatcher;
import static de.lb.cpx.client.core.util.code.CodeExtraction.getOpsCodeMatcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author niemeier
 */
public class CodeInjector {

    private final CodeInjectorHandler icdCodeHandler;
    private final CodeInjectorHandler opsCodeHandler;

    public CodeInjector(final CodeInjectorHandler pIcdCodeHandler, final CodeInjectorHandler pOpsCodeHandler) {
        this.icdCodeHandler = pIcdCodeHandler;
        this.opsCodeHandler = pOpsCodeHandler;
    }

    public TextFlow addCodeLinks(final String pText) {
        final String text = pText == null ? "" : pText.trim();
        final TextFlow fl = new TextFlowWithCopyText();
        fl.getChildren().add(new Text(text));
        addCodeLinks(fl);
        return fl;
    }

    public void addCodeLinks(final TextFlow pFl) {
        if (pFl == null) {
            return;
        }
        final List<Node> nodes = new ArrayList<>();
        final Iterator<Node> it = pFl.getChildren().iterator();
        while (it.hasNext()) {
            final Node elem = it.next();
            if (elem instanceof Text) {
                //it.remove();
                //final Matcher m = i == 1 ? getIcdCodeMatcher(t) : getOpsCodeMatcher(t);
                Node fl = addCodeLinks((Text) elem);
                if (fl instanceof TextFlow) {
                    nodes.addAll(((Pane) fl).getChildren());
                } else {
                    nodes.add(fl);
                }
            } else {
                nodes.add(elem);
            }
        }
        pFl.getChildren().clear();
        pFl.getChildren().addAll(nodes);
    }

    protected List<CodeInjectorLink> getLinks(final String pText) {
        List<CodeInjectorLink> links = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            final Matcher m;
            final CodeInjectorType type;
            final CodeInjectorHandler handler;
            if (i == 1) {
                //ICD
                if (icdCodeHandler == null) {
                    continue;
                }
                m = getIcdCodeMatcher(pText);
                type = CodeInjectorType.ICD;
                handler = icdCodeHandler;
            } else {
                //OPS
                if (opsCodeHandler == null) {
                    continue;
                }
                m = getOpsCodeMatcher(pText);
                type = CodeInjectorType.OPS;
                handler = opsCodeHandler;
            }
            while (m.find()) {
                final CodeInjectorLink link = new CodeInjectorLink(type, m.start(), m.end(), m.group(), handler);
                links.add(link);
            }
        }
        return links;
    }

    protected Node addCodeLinks(final Text pText) {
        if (pText == null) {
            return pText;
        }
        final String t = pText.getText();
        final List<CodeInjectorLink> links = getLinks(t);
        if (links.isEmpty()) {
            return pText;
        }
        //final String t = pText.getText();
        final TextFlow fl = addCodeLinks(t, links);
        return fl;
    }

    protected TextFlow addCodeLinks(final String pText, final List<CodeInjectorLink> links) {
        final String text = pText == null ? "" : pText;
        final TextFlow fl = new TextFlowWithCopyText();
        fl.setPrefWidth(Region.USE_COMPUTED_SIZE);
        fl.setMinWidth(Region.USE_PREF_SIZE);
        if (text.isEmpty()) {
            Text txt = new Text(text);
            txt.setStyle("-fx-wrap-text: true");
            fl.getChildren().add(txt);
            return fl;
        }
        if (links == null || links.isEmpty()) {
            return fl;
        }
        //Matcher m = CodeExtraction.getIcdCodeMatcher(text);
        int r = 0;
        for (CodeInjectorLink l : links) {
            //TextFlow flt = new TextFlow();
            //fl.getChildren().add(flt);
            if (r <= l.start) {
                final String t = text.substring(r, l.start);
                if (!t.isEmpty()) {
                    Text txt = new Text(t);
                    txt.setStyle("-fx-wrap-text: true");
                    fl.getChildren().add(txt);
                }
            }
            //flt.getChildren().add(txt);
            final String code = text.substring(l.start, l.end);
            Hyperlink link = new Hyperlink(code);
            link.setPadding(new Insets(0));
            fl.getChildren().add(link);

            if (l.handler != null) {
                link.setOnAction((event) -> {
                    l.handler.handle(event, code);
                });
            }

            r = l.end;
            //flt.getChildren().add(link);
        }
        if (r > 0) {
            //TextFlow flt = new TextFlow();
            //fl.getChildren().add(flt);
            Text txt = new Text(text.substring(r));
            txt.setStyle("-fx-wrap-text: true");
            fl.getChildren().add(txt);
            //flt.getChildren().add(txt);
        }
        return fl;
    }

}

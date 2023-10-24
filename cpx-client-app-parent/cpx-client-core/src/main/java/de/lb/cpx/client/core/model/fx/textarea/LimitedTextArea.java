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
package de.lb.cpx.client.core.model.fx.textarea;

/**
 * TextArea with Limited TextLenght, default is 256 Characters
 *
 * @author wilde
 */
//@Deprecated(since="1.05")
//public class LimitedTextArea extends TextArea {
//
//    private static final Logger LOG = Logger.getLogger(LimitedTextArea.class.getName());
//
//    private int charLimit;
//
//    public LimitedTextArea() {
//        super();
//        this.charLimit = 256;
//        setUp();
//    }
//
//    public LimitedTextArea(int limit) {
//        super();
//        this.charLimit = limit;
//        setUp();
//    }
//
//    public void setCharLimit(int limit) {
//        this.charLimit = limit;
//    }
//
//    public int getCharLimit() {
//        return charLimit;
//    }
//
//    private void setUp() {
//        setWrapText(true);
//        lengthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (newValue.intValue() > oldValue.intValue()) {
//                    if (getText().length() >= charLimit) {
//                        setText(getText().substring(0, charLimit));
//                    }
//                }
//            }
//        });
//    }
//
//}

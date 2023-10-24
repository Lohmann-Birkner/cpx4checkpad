/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.editor.state;

import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.model.state.StateManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wilde
 */
public class RuleEditorStateManager extends StateManager<Rule> {

    private static final Logger LOG = Logger.getLogger(RuleEditorStateManager.class.getName());
//    private Rule ORIGINAL;
//    private Rule rule;
    private static final long serialVersionUID = 1L;

    public RuleEditorStateManager() {
    }

    @Override
    public boolean compare(Rule pRule1, Rule pRule2) throws IOException {
        long start = System.currentTimeMillis();
//        LOG.info("serialize...");
//        ByteArrayOutputStream str1 = new ByteArrayOutputStream();
//        ObjectOutputStream oos1 = new ObjectOutputStream(str1);
//        oos1.writeObject(pRule1);
//        byte[] serialized1 = str1.toByteArray();
//        oos1.close();
//
//        ByteArrayOutputStream str2 = new ByteArrayOutputStream();
//        ObjectOutputStream oos2 = new ObjectOutputStream(str2);
//        oos2.writeObject(pRule2);
//        byte[] serialized2 = str2.toByteArray();
//        oos2.close();

        LOG.info("compare");
//        LOG.info(pRule1.toString());
//        LOG.info(pRule2.toString());
        boolean same = pRule1.toString().equals(pRule2.toString());// == serialized2.length;
//        boolean same = Arrays.equals(serialized1, serialized2);
//        LOG.log(Level.INFO, "first " + serialized1.length + " second " + serialized2.length);
        LOG.log(Level.INFO, "compare result: {0} in {1} ms", new Object[]{same, System.currentTimeMillis() - start});
        return same;
    }

//    public void undo() {
//        stackRule.poll();
//        setCurrentRule(stackRule.peek());
//        if(getOnStateChanged() != null){
//            getOnStateChanged().call(getCurrentRule());
//        }
//    }
//
//    public void saveState() {
//        stackRule.push(copyRule(getCurrentRule()));
//    }
//    
//    private final ArrayDeque<Rule> stackRule = new ArrayDeque<>();
//    public void clear() {
//        stackRule.clear();
//        setCurrentRule(null);
//        ORIGINAL = null;
//    }
//    public void setOnStateChanged(Callback<Rule, Void> callback) {
//        stateChanged = callback;
//    }
//    public Callback<Rule,Void> getOnStateChanged(){
//        return stateChanged;
//    }
}

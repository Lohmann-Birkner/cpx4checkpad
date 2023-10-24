/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.analyser.tabs;

import de.lb.cpx.model.TCase;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public interface HistoryDataTabIF {
    public void setSelection4Case(TCase pCase);
    public void setHistoryCases(List<TCase> pCasesFromTreeItem);
    public void reload();
}

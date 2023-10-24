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
package de.lb.cpx.client.ruleeditor.export;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.core.util.export.ExportListMapper;
import de.lb.cpx.client.core.util.export.ListExporter;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.serviceutil.export.ProcessItemCallback;
import de.lb.cpx.shared.filter.ColumnOption;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author wilde
 */
public abstract class RuleListExporter implements ListExporter<CrgRules>{

    private static final Logger LOG = Logger.getLogger(RuleListExporter.class.getName());
    
    private List<ColumnOption> options;
    private List<CrgRules> items;
    private ExportListMapper<CrgRules> mapper;
    private PoolTypeEn poolType;
    private CrgRulePools pool;
    private String fileName;
    
    public RuleListExporter(){
        setOptions(new ArrayList<>());
        setItems(new ArrayList<>());
    }
    public RuleListExporter(List<ColumnOption> pOptions,List<CrgRules> pItems){
        super();
        setOptions(pOptions);
        setItems(pItems);
    }
    public void setPoolType(PoolTypeEn pType){
        poolType = pType;
    }
    public PoolTypeEn getPoolType(){
        return poolType;
    }
    public void setPool(CrgRulePools pPool){
        pool = pPool;
    }
    public CrgRulePools getPool(){
        return pool;
    }
    public final void setOptions(List<ColumnOption> pOptions){
        options = Objects.requireNonNullElse(pOptions, new ArrayList<>());
        setListMapper(new RuleListMapper(pOptions));
    }
    
    public final void setItems(List<CrgRules> pItems){
        items = Objects.requireNonNullElse(pItems, new ArrayList<>());
    }
    protected final List<CrgRules> getItems(){
        return items;
    }

    @Override
    public void setListMapper(ExportListMapper<CrgRules> pMapper) {
        mapper = pMapper;
    }

    @Override
    public ExportListMapper<CrgRules> getListMapper() {
        return mapper;
    }

    @Override
    public String getExportPath() {
        return CpxClientConfig.instance().getUserRecentFileChooserPath(FileChooserFactory.RULE_EXPORT)+"\\";
    }
    public void setDefaultFileName(String pFileName){
        fileName = Objects.requireNonNullElse(pFileName,createDefaultFileName(getPool(), getPoolType()));
    }
    public String getDefaultFileName(){
        if(fileName == null || fileName.trim().isEmpty()){
            fileName = createDefaultFileName(getPool(), getPoolType());
        }
        return fileName;
    }
    public String createDefaultFileName(CrgRulePools pPool, PoolTypeEn pType){
        StringBuilder sb = new StringBuilder("Exportierte Regelliste");
        if(pPool != null || pType != null){
            sb.append(" (");
        }
        if(pPool != null){
            sb.append(pPool.getCrgplIdentifier());
        }
        if(pPool != null && pType != null){
            sb.append(" - ");
        }
        if(pType != null){
            sb.append(pType.getTitle());
        }
        if(pPool != null || pType != null){
            sb.append(")");
        }
        return sb.toString();
    }
    protected <W extends Closeable,T> File process(final String pFileName, final ProcessItemCallback<W, CrgRules> pCallback,List<ColumnOption> pOptions,List<CrgRules> pItems) {
        final File targetFile = new File(getExportPath() + pFileName);
        final int itemCount = pItems.size();
        if (pItems.isEmpty()) {
            return null;
        }
        try (W pWriter = pCallback.createWriter(targetFile)) {
            pCallback.call(pWriter, 0, null);
            for (int itCount = 1; itCount <= pItems.size(); itCount++) {
                CrgRules dto = pItems.get(itCount-1);
                pCallback.call(pWriter, itCount, dto);

                //don't send progress message for each case
                if (itCount % 1000 == 0 || itCount == pItems.size()) {
                    LOG.log(Level.INFO, "Written {0}/{1} cases to csv/excel file", new Object[]{itCount, itemCount});
                }
            }
            pCallback.closeWriter(pWriter);
        } catch (IOException ex) {
            Logger.getLogger(RuleListExporter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (InterruptedException ex) {
            Logger.getLogger(RuleListExporter.class.getName()).log(Level.SEVERE, null, ex);
//             Thread.currentThread().interrupt();
            return null;
        }
        return targetFile;
    }
        
    @Override
    public File export() {
        List<ColumnOption> listOptions = options.stream().filter(ColumnOption::isShouldShow).collect(Collectors.toList());
        final String fileNameTmp = new StringBuilder(getDefaultFileName()).append(".").append(getExportType().getFileExtension()).toString();
        ProcessItemCallback<? extends Closeable, CrgRules> callback = getProcessItemCallback();
        
//        final String date = Lang.toIsoDateTime(new Date()).replace(":", "-");
//        final String userName = Session.instance().getCpxUserName();
//        final String suffix = " erstellt von " + userName + " am " + date;
//        final String fileName = addFileSuffix(fileNameTmp, suffix);
        return process(fileNameTmp, callback, listOptions, getItems());
    }
}

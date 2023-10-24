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
package de.lb.cpx.client.app.wm.util;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.wm.model.TWmRequest;
import java.util.stream.Collectors;
import javafx.scene.control.Label;

/**
 *
 * @author wilde
 */
public class DisplayHelper {
    
    public static String createAuditReasonInitialText(TWmRequest pRequest, String pDivider){
       return createAuditReasonText(pRequest, pDivider, Boolean.FALSE);
    }
    
    public static String createAuditReasonExtendedText(TWmRequest pRequest, String pDivider){
       return createAuditReasonText(pRequest, pDivider, Boolean.TRUE); 
    }
    
    public static String createAuditReasonText(TWmRequest pRequest, String pDivider, Boolean pExtended){
        if(pRequest == null){
            return "-";
        }
        if(pRequest.getAuditReasons().isEmpty()){
            return "-";
        }
        return pRequest.getAuditReasons(pExtended).stream()
                .map(elem -> MenuCache.instance().getAuditReasonForNumber(elem.getAuditReasonNumber()))
                .filter(e -> e != null)
                .collect(Collectors.joining(pDivider));
    }
    
    public static Label createAuditReasonInitialLabel(TWmRequest pRequest, String pDivider){
        return createAuditReasonLabel(pRequest, pDivider, Boolean.FALSE);
    }
    
    public static Label createAuditReasonExtendedLabel(TWmRequest pRequest, String pDivider){
        return createAuditReasonLabel(pRequest, pDivider, Boolean.TRUE);
    }
    
    public static Label createAuditReasonLabel(TWmRequest pRequest, String pDivider, Boolean pExtended){
        Label lblAuditReason = new Label();//
        lblAuditReason.setText(createAuditReasonText(pRequest, pDivider, pExtended));
        lblAuditReason.setWrapText(true);
        return lblAuditReason;
    }
}

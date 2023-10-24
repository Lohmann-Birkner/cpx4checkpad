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
package de.lb.cpx.client.core.util;

import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class IcdHelper {

    public static void setSecIcdReference(@NotNull IcdcRefTypeEn pType, @NotNull TCaseIcd pOrigin, @NotNull TCaseIcd pTarget) {
        Objects.requireNonNull(pType, "RefType can not be null");
        Objects.requireNonNull(pOrigin, "Origin Icd can not be null");
        Objects.requireNonNull(pTarget, "Target Icd can not be null");

        pOrigin.setIcdcReftypeEn(pType);
        switch (pType) {
            //prim
            case Kreuz:
                pTarget.setIcdcReftypeEn(IcdcRefTypeEn.Stern);
                pTarget.setRefIcd(pOrigin);
                pOrigin.getRefIcds().add(pTarget);
                break;
            case Zusatz:
                pTarget.setIcdcReftypeEn(IcdcRefTypeEn.ZusatzZu);
                pTarget.setRefIcd(pOrigin);
                pOrigin.getRefIcds().add(pTarget);
                break;

            //sec
            case Stern:
                pTarget.setIcdcReftypeEn(IcdcRefTypeEn.Kreuz);
                pOrigin.setRefIcd(pTarget);
                pTarget.getRefIcds().add(pOrigin);
                break;
            case ZusatzZu:
                pTarget.setIcdcReftypeEn(IcdcRefTypeEn.Zusatz);
                pOrigin.setRefIcd(pTarget);
                pTarget.getRefIcds().add(pOrigin);
                break;
        }
    }

    public static boolean isPrimRefType(@NotNull IcdcRefTypeEn pType) {
        Objects.requireNonNull(pType, "Type can not be null");
        switch (pType) {
            case Kreuz:
            case Zusatz:
                return true;
            case Stern:
            case ZusatzZu:
                return false;
        }
        return false;
    }

    public static void removeSecIcdReference(TCaseIcd pIcd) {
        if (isPrimRefType(pIcd.getIcdcReftypeEn())) {
            Iterator<TCaseIcd> it = pIcd.getRefIcds().iterator();
            while (it.hasNext()) {
                TCaseIcd secDiag = it.next();
                if (secDiag.getRefIcd() != null && secDiag.getRefIcd().versionEquals(pIcd)) {
                    secDiag.setRefIcd(null);
                    secDiag.setIcdcReftypeEn(null);
                    it.remove();
                }
            }
        } else {
            TCaseIcd ref = pIcd.getRefIcd();
            if (ref != null) {
                ref.setIcdcReftypeEn(null);
                removeIcd(ref.getRefIcds(), pIcd);
            }
        }
        pIcd.setRefIcd(null);
        pIcd.setIcdcReftypeEn(null);
    }

    public static void removeIcd(Set<TCaseIcd> pIcds, TCaseIcd pIcd) {
        Iterator<TCaseIcd> it = pIcds.iterator();
        while (it.hasNext()) {
            TCaseIcd next = it.next();
            if (next.versionEquals(pIcd)) {
                it.remove();
                return;
            }
        }
    }
}

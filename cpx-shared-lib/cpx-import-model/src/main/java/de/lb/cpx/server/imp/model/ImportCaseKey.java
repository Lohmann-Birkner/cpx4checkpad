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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.imp.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author husser
 */
@Embeddable
public class ImportCaseKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ik;
    private String khInternesKennzeichen;

    public ImportCaseKey() {
    }

    public ImportCaseKey(String ik, String khInternesKennzeichen) {
        this.ik = ik;
        this.khInternesKennzeichen = khInternesKennzeichen;
    }

    public String getIk() {
        return ik;
    }

    public void setIk(String ik) {
        this.ik = ik;
    }

    public String getKhInternesKennzeichen() {
        return khInternesKennzeichen;
    }

    public void setKhInternesKennzeichen(String khInternesKennzeichen) {
        this.khInternesKennzeichen = khInternesKennzeichen;
    }

    @Override
    public int hashCode() {
        int hash = 31 * Objects.hashCode(this.ik) + Objects.hashCode(this.khInternesKennzeichen);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImportCaseKey other = (ImportCaseKey) obj;
        if (!Objects.equals(this.ik, other.ik)) {
            return false;
        }
        return Objects.equals(this.khInternesKennzeichen, other.khInternesKennzeichen);
    }

    @Override
    public String toString() {
        return "ImportCaseKey{" + "ik=" + ik + ", khInternesKennzeichen=" + khInternesKennzeichen + '}';
    }

}

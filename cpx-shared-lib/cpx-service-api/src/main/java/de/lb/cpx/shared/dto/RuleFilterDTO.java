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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author nandola
 */
public class RuleFilterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ruleNumber;
    private String ruleDescription;
    private String ruleSuggestion;
    private String ruleIdentNumber;
    private String ruleCategorie;
    private Integer[] years;
    private CrgRulePools[] rulePools;
    private CrgRuleTypes[] ruleTypes;
    private Boolean errorType;
    private Boolean warningType;
    private Boolean informationType;

    /**
     * Empty constructor for the (de-)serialization
     */
    public RuleFilterDTO() {
        this("", "", "", "", "", (Integer[]) null, (CrgRulePools[]) null, (CrgRuleTypes[]) null, true, true, true);
    }

    public RuleFilterDTO(final String ruleNumber,
            final String ruleDescription,
            final String ruleSuggestion,
            final String ruleIdentNumber,
            final String ruleCategorie,
            final Integer[] years,
            final CrgRulePools[] rulePools,
            final CrgRuleTypes[] ruleTypes,
            final Boolean errorType,
            final Boolean warningType,
            final Boolean informationType) {

        setRuleNumber(ruleNumber);
        setRuleDescription(ruleDescription);
        setRuleSuggestion(ruleSuggestion);
        setRuleIdentNumber(ruleIdentNumber);
        setRuleCategorie(ruleCategorie);

        setYears(years);
        setRulePools(rulePools);
        setRuleTypes(ruleTypes);

        setErrorType(errorType);
        setWarningType(warningType);
        setInformationType(informationType);
    }

    public RuleFilterDTO(final String ruleNumber,
            final String ruleDescription,
            final String ruleSuggestion,
            final String ruleIdentNumber,
            final String ruleCategorie,
            final List<Integer> years,
            final List<CrgRulePools> rulePools,
            final List<CrgRuleTypes> ruleTypes,
            final Boolean errorType,
            final Boolean warningType,
            final Boolean informationType) {

        setRuleNumber(ruleNumber);
        setRuleDescription(ruleDescription);
        setRuleSuggestion(ruleSuggestion);
        setRuleIdentNumber(ruleIdentNumber);
        setRuleCategorie(ruleCategorie);

        final Integer[] tmpYears;
        if (years == null || years.isEmpty()) {
            tmpYears = new Integer[0];
        } else {
            tmpYears = new Integer[years.size()];
            years.toArray(tmpYears);
        }
        setYears(tmpYears);

        final CrgRulePools[] tmpRulePools;
        if (rulePools == null || rulePools.isEmpty()) {
            tmpRulePools = new CrgRulePools[0];
        } else {
            tmpRulePools = new CrgRulePools[rulePools.size()];
            rulePools.toArray(tmpRulePools);
        }
        setRulePools(tmpRulePools);

        final CrgRuleTypes[] tmpRuleTypes;
        if (ruleTypes == null || ruleTypes.isEmpty()) {
            tmpRuleTypes = new CrgRuleTypes[0];
        } else {
            tmpRuleTypes = new CrgRuleTypes[ruleTypes.size()];
            ruleTypes.toArray(tmpRuleTypes);
        }
        setRuleTypes(tmpRuleTypes);

        setErrorType(errorType);
        setWarningType(warningType);
        setInformationType(informationType);

    }

    public RuleFilterDTO(final RuleFilterDTO ruleFilterDTO) {
        this(ruleFilterDTO.getRuleNumber() == null ? "" : ruleFilterDTO.getRuleNumber(),
                ruleFilterDTO.getRuleDescription() == null ? "" : ruleFilterDTO.getRuleDescription(),
                ruleFilterDTO.getRuleSuggestion() == null ? "" : ruleFilterDTO.getRuleSuggestion(),
                ruleFilterDTO.getRuleIdentNumber() == null ? "" : ruleFilterDTO.getRuleIdentNumber(),
                ruleFilterDTO.getRuleCategorie() == null ? "" : ruleFilterDTO.getRuleCategorie(),
                ruleFilterDTO.getYears() == null ? null : ruleFilterDTO.getYears(),
                ruleFilterDTO.getRulePools() == null ? null : ruleFilterDTO.getRulePools(),
                ruleFilterDTO.getRuleTypes() == null ? null : ruleFilterDTO.getRuleTypes(),
                Objects.requireNonNullElse(ruleFilterDTO.getErrorType(), true),
                Objects.requireNonNullElse(ruleFilterDTO.getWarningType(), true),
                Objects.requireNonNullElse(ruleFilterDTO.getInformationType(), true)
        );
    }

    @Transient
    public RuleFilterDTO getCopy() {
        return new RuleFilterDTO(this);
    }

    // Setter Methods
    public final RuleFilterDTO setRuleNumber(final String pRuleNumber) {
        ruleNumber = pRuleNumber == null ? "" : pRuleNumber.trim();
        return this;
    }

    public final RuleFilterDTO setRuleDescription(final String pRuleDescription) {
        ruleDescription = pRuleDescription == null ? "" : pRuleDescription.trim();
        return this;
    }

    public final RuleFilterDTO setRuleSuggestion(final String pRuleSuggestion) {
        ruleSuggestion = pRuleSuggestion == null ? "" : pRuleSuggestion.trim();
        return this;
    }

    public final RuleFilterDTO setRuleIdentNumber(final String pRuleIdentNumber) {
        ruleIdentNumber = pRuleIdentNumber == null ? "" : pRuleIdentNumber.trim();
        return this;
    }

    public final RuleFilterDTO setRuleCategorie(final String pRuleCategorie) {
        ruleCategorie = pRuleCategorie == null ? "" : pRuleCategorie.trim();
        return this;
    }

    public final RuleFilterDTO setYears(final Integer[] pYears) {
        if (pYears == null || pYears.length == 0) {
            years = new Integer[0];
        } else {
            //will ignore null values here!
            int size = 0;
            for (Integer year : pYears) {
                if (year != null) {
                    size++;
                }
            }
            Integer[] tmp = new Integer[size];
            int i = 0;
            for (Integer year : pYears) {
                if (year == null) {
                    continue;
                }
                tmp[i++] = year;
            }
            years = tmp;
        }
        return this;
    }

    public final RuleFilterDTO setRulePools(final CrgRulePools[] pRulePools) {
        if (pRulePools == null || pRulePools.length == 0) {
            rulePools = new CrgRulePools[0];
        } else {
            int size = 0;
            for (CrgRulePools rulePool : pRulePools) {
                if (rulePool != null) {
                    size++;
                }
            }
            CrgRulePools[] tmp = new CrgRulePools[size];
            int i = 0;
            for (CrgRulePools rulePool : pRulePools) {
                if (rulePool == null) {
                    continue;
                }
                tmp[i++] = rulePool;
            }
            rulePools = tmp;
        }
        return this;
    }

    public final RuleFilterDTO setRuleTypes(final CrgRuleTypes[] pRuletypes) {
        if (pRuletypes == null || pRuletypes.length == 0) {
            ruleTypes = new CrgRuleTypes[0];
        } else {
            int size = 0;
            for (CrgRuleTypes ruletype : pRuletypes) {
                if (ruletype != null) {
                    size++;
                }
            }
            CrgRuleTypes[] tmp = new CrgRuleTypes[size];
            int i = 0;
            for (CrgRuleTypes ruletype : pRuletypes) {
                if (ruletype == null) {
                    continue;
                }
                tmp[i++] = ruletype;
            }
            ruleTypes = tmp;
        }
        return this;
    }

    public final RuleFilterDTO setErrorType(final Boolean pErrorType) {
        errorType = pErrorType == null ? true : pErrorType;
        return this;
    }

    public final RuleFilterDTO setWarningType(final Boolean pWarningType) {
        warningType = pWarningType == null ? true : pWarningType;
        return this;
    }

    public final RuleFilterDTO setInformationType(final Boolean pInformationType) {
        informationType = pInformationType == null ? true : pInformationType;
        return this;
    }

    // Getter Methods
    @XmlElement(name = "ruleNumber")
    public String getRuleNumber() {
        return ruleNumber == null ? "" : ruleNumber;
    }

    @XmlElement(name = "ruleDescription")
    public String getRuleDescription() {
        return ruleDescription == null ? "" : ruleDescription;
    }

    @XmlElement(name = "ruleSuggestion")
    public String getRuleSuggestion() {
        return ruleSuggestion == null ? "" : ruleSuggestion;
    }

    @XmlElement(name = "ruleIdentNumber")
    public String getRuleIdentNumber() {
        return ruleIdentNumber == null ? "" : ruleIdentNumber;
    }

    @XmlElement(name = "ruleCategorie")
    public String getRuleCategorie() {
        return ruleCategorie == null ? "" : ruleCategorie;
    }

    @XmlElementWrapper(name = "years")
    @XmlElement(name = "year")
    public Integer[] getYears() {
        if (years != null && years.length > 0) {
            Integer[] tmp = new Integer[years.length];
            System.arraycopy(years, 0, tmp, 0, years.length);
            return tmp;
        } else {
            return null;
        }
    }

    @XmlElementWrapper(name = "rulePools")
    @XmlElement(name = "rulePool")
    public CrgRulePools[] getRulePools() {
        if (rulePools != null && rulePools.length > 0) {
            CrgRulePools[] tmp = new CrgRulePools[rulePools.length];
            System.arraycopy(rulePools, 0, tmp, 0, rulePools.length);
            return tmp;
        } else {
            return null;
        }
    }

    @XmlElementWrapper(name = "ruleTypes")
    @XmlElement(name = "ruleType")
    public CrgRuleTypes[] getRuleTypes() {
        if (ruleTypes != null && ruleTypes.length > 0) {
            CrgRuleTypes[] tmp = new CrgRuleTypes[ruleTypes.length];
            System.arraycopy(ruleTypes, 0, tmp, 0, ruleTypes.length);
            return tmp;
        } else {
            return null;
        }
    }

    @XmlElement(name = "errorType")
    public Boolean getErrorType() {
        return Objects.requireNonNullElse(errorType, true);
    }

    @XmlElement(name = "warningType")
    public Boolean getWarningType() {
        return Objects.requireNonNullElse(warningType, true);
    }

    @XmlElement(name = "informationType")
    public Boolean getInformationType() {
        return Objects.requireNonNullElse(informationType, true);
    }

//    @Override
//    public String toString() {
//    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.ruleNumber);
        hash = 29 * hash + Objects.hashCode(this.ruleDescription);
        hash = 29 * hash + Objects.hashCode(this.ruleSuggestion);
        hash = 29 * hash + Objects.hashCode(this.ruleIdentNumber);
        hash = 29 * hash + Objects.hashCode(this.ruleCategorie);
        hash = 29 * hash + Arrays.deepHashCode(this.years);
        hash = 29 * hash + Arrays.deepHashCode(this.rulePools);
        hash = 29 * hash + Arrays.deepHashCode(this.ruleTypes);
        hash = 29 * hash + Objects.hashCode(this.errorType);
        hash = 29 * hash + Objects.hashCode(this.warningType);
        hash = 29 * hash + Objects.hashCode(this.informationType);

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
        final RuleFilterDTO other = (RuleFilterDTO) obj;
        if (!Objects.equals(this.ruleNumber, other.ruleNumber)) {
            return false;
        }
        if (!Objects.equals(this.ruleDescription, other.ruleDescription)) {
            return false;
        }
        if (!Objects.equals(this.ruleSuggestion, other.ruleSuggestion)) {
            return false;
        }
        if (!Objects.equals(this.ruleIdentNumber, other.ruleIdentNumber)) {
            return false;
        }
        if (!Objects.equals(this.ruleCategorie, other.ruleCategorie)) {
            return false;
        }
        if (!Arrays.deepEquals(this.years, other.years)) {
            return false;
        }
        if (!Arrays.deepEquals(this.rulePools, other.rulePools)) {
            return false;
        }
        if (!Arrays.deepEquals(this.ruleTypes, other.ruleTypes)) {
            return false;
        }
        if (!Objects.equals(this.errorType, other.errorType)) {
            return false;
        }
        if (!Objects.equals(this.warningType, other.warningType)) {
            return false;
        }
        if (!Objects.equals(this.informationType, other.informationType)) {
            return false;
        }
        return true;
    }

    public boolean filterHasValues() {
        return !ruleNumber.isEmpty()
                || !ruleDescription.isEmpty()
                || !ruleSuggestion.isEmpty()
                || !ruleIdentNumber.isEmpty()
                || !ruleCategorie.isEmpty()
                || years.length > 0
                || rulePools.length > 0
                || ruleTypes.length > 0
                || errorType
                || warningType
                || informationType;
    }

}

/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package dto;

import dto.impl.Case;
import dto.impl.Patient;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import line.AbstractLine;
import line.LineI;

/**
 *
 * @author Dirk Niemeier
 * @param <T> type
 */
public abstract class AbstractDto<T extends DtoI> implements DtoI {

    protected static final AtomicLong NUMBER_GENERATOR = new AtomicLong(0L);
    private static final long serialVersionUID = 1L;
    private final long nr = getNextNumber();
    //private String ikz = "";
    //private String fallNr = "";
    private String tpSource = "";
    private String tpId = "";

    public static long getNextNumber() {
        return NUMBER_GENERATOR.incrementAndGet();
    }

    public static AbstractLine toLine(final AbstractDto<? extends DtoI> pDtoObject) throws IllegalStateException {
        if (pDtoObject == null) {
            throw new IllegalArgumentException("DTO Object is null");
        }
        AbstractLine line;
        try {
            line = pDtoObject.getLineClass().getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException ex) {
            throw new IllegalStateException("Cannot create object! Ensure that a public standard constructor for line class " + pDtoObject.getLineClass() + " exists!", ex);
        }
        /*
    if (pLine == null) {
    throw new CpxIllegalArgumentException("Line is null");
    }
         */
        line.setNr(pDtoObject.getNr());
        line.setIkz(pDtoObject.getIkz());
        line.setFallNr(pDtoObject.getFallNr());
        line.setPatNr(pDtoObject.getPatNr());
        line.setTpSource(pDtoObject.getTpSource());
        line.setTpId(pDtoObject.getTpId());
        return line;
    }

    public static String[] splitKey(final String pKey) {
        String key = (pKey == null) ? "" : pKey.trim();
        return key.split(":");
    }

    public static String buildCaseKey(final String pIkz, final String pFallNr) {
        String ikz = (pIkz == null) ? "" : pIkz.trim();
        String fallNr = (pFallNr == null) ? "" : pFallNr.trim();
        if (ikz.isEmpty() && fallNr.isEmpty()) {
            return "";
        }
        return ikz + ":" + fallNr;
    }

    public static String buildPatientKey(final String pPatNr) {
        String patNr = (pPatNr == null) ? "" : pPatNr.trim();
        if (patNr.isEmpty()) {
            return "";
        }
        return patNr;
    }

    /*
  public AbstractDto() {
    this(null);
  }
     */
    /**
     * @param pFallNr the fallNr to set
     */
    /*
  public void setFallNr(final String pFallNr) {
    this.fallNr = (pFallNr == null)?"":pFallNr.trim();
  }
     */
    /**
     * @return the tpSource
     */
    public String getTpSource() {
        return tpSource;
    }

    /**
     * set third party source (table name, file name, object name or whatever)
     *
     * @param pTpSource the tpSource to set
     */
    public void setTpSource(final String pTpSource) {
        this.tpSource = (pTpSource == null) ? "" : pTpSource.trim();
    }

    /**
     * get third party source (table name, file name, object name or whatever)
     *
     * @return the tpId
     */
    public String getTpId() {
        return tpId;
    }

    /**
     * set third party id to identify foreign entry
     *
     * @param pTpId the tpId to set
     */
    public void setTpId(final String pTpId) {
        this.tpId = (pTpId == null) ? "" : pTpId.trim();
    }

    /**
     * get third party id to identify foreign entry
     *
     * @param pTpId the tpId to set
     */
    public void setTpId(final Integer pTpId) {
        this.tpId = (pTpId == null) ? "" : pTpId.toString();
    }

    /**
     * @param pTpId the tpId to set
     */
    public void setTpId(final Long pTpId) {
        this.tpId = (pTpId == null) ? "" : pTpId.toString();
    }

    public long getNr() {
        return nr;
    }

    /*
  public static LineI toLine(final AbstractDto pDto) {
    if (pDto == null) {
      throw new CpxIllegalArgumentException("DTO is null");
    }
    
  }
     */
    @Override
    public String toCsv() {
        return toLine().serialize();
    }

    /*
  @Override
  public String getCaseKey() {
    return getIkz() + ":" + getFallNr();
  }
     */
 /*
  public static AbstractDto fromLine(final Class<? extends AbstractDto> pDtoClass, LineI pLine) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (pDtoClass == null) {
      throw new CpxIllegalArgumentException("DTO Class is null");
    }
    if (pLine == null) {
      throw new CpxIllegalArgumentException("Line is null");
    }
    final Long id = Long.valueOf(pLine.getNr());
    final AbstractDto dtoObject = pDtoClass.getDeclaredConstructor(Long.class).newInstance(id);
    dtoObject.setIkz(pLine.getIkz());
    dtoObject.setFallNr(pLine.getFallNr());
    dtoObject.setTpSource(pLine.getTpSource());
    dtoObject.setTpId(pLine.getTpId());
    return dtoObject;
  }
     */
 /*
  public static AbstractDto getInstance(final Class<? extends AbstractDto> pDtoClass, final Long pId) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (pDtoClass == null) {
      throw new CpxIllegalArgumentException("DTO Class is null");
    }
    final AbstractDto dtoObject = pDtoClass.getDeclaredConstructor(Long.class).newInstance(-1L);
    return dtoObject;
  }
  
  public static AbstractDto fromCsv(final Class<? extends AbstractDto> pDtoClass, final String pCsvData) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (pDtoClass == null) {
      throw new CpxIllegalArgumentException("DTO Class is null");
    }
    final AbstractDto dtoObject = getInstance(pDtoClass, -1L);
    LineI line = AbstractLine.unserialize(dtoObject.getLineClass(), pCsvData);
    return fromLine(pDtoClass, line);
  }
     */
 /*
  @Override
  public boolean hasValues() throws InstantiationException, IllegalAccessException {
    return toLine(this).hasValues();
  }
     */
    @Override
    public Set<LineI> getLines() {
        Set<LineI> lineSet = new LinkedHashSet<>();
        Iterator<DtoI> it = getDtos().iterator();
        while (it.hasNext()) {
            DtoI dto = it.next();
            if (dto == null) {
                continue;
            }
            lineSet.add(dto.toLine());
        }
        return lineSet;
    }

    /*
  @Override
  public String getIkz() {
    Case cs = getCase();
    if (cs == null) {
      return "";
    } else {
      return cs.getIkz();
    }
  }

  @Override
  public String getFallNr() {
    Case cs = getCase();
    if (cs == null) {
      return "";
    } else {
      return cs.getFallNr();
    }
  }
     */
    @Override
    public String getPatNr() {
        Patient patient = getPatient();
        if (patient == null) {
            return "";
        } else {
            return patient.getPatNr();
        }
    }

    @Override
    public Patient getPatient() {
        Case cs = getCase();
        if (cs == null) {
            return null;
        }
        return cs.mPatient;
    }

    @Override
    public String getCaseKey() {
        Case cs = getCase();
        if (cs == null) {
            return buildCaseKey(this.getIkz(), this.getFallNr());
        }
        return buildCaseKey(cs.getIkz(), cs.getFallNr());
    }

    @Override
    public String getPatientKey() {
        Patient patient = getPatient();
        if (patient == null) {
            return "";
        }
        return buildPatientKey(patient.getPatNr());
    }

    public abstract void set(T pOther);

}

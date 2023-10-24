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
package de.lb.cpx.sap.kain_inka;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author gerschmann
 */
public final class PvvChildSegment extends Segment implements PvvResultIf {

    private static final Logger LOG = Logger.getLogger(PvvChildSegment.class.getName());

    /**
     *
     */
    public PvvChildSegment() {

    }

    /**
     *
     * @param key key
     */
    public PvvChildSegment(String key) {
        setInformation(key);
        children = null;
    }

    /**
     *
     * @param elem element
     */
    public PvvChildSegment(Element elem) {
        super(elem);
        children = getChildList(elem.getChildNodes(), localName2element);
    }

    /**
     * zuffen der neuen pvv segmenten
     *
     * @param pvv PVV segment
     */
    public void setValue(PvvResultIf pvv) {

        setInformation(pvv.getInformation());
        setBillNumber(pvv.getBillNumber());
        setBillDate(pvv.getBillDate());
        Segment seg_pvts = (Segment) localName2element.get(KainInkaStatics.NAME_SEGMENT_PVT);
        Segment pvt_childs = (Segment) localName2element.get(KainInkaStatics.NAME_SEGMENT_PVT_CHILD);

        if (seg_pvts != null && pvt_childs != null) {
            Element xml = pvt_childs.xmlElement;
            seg_pvts.removeNotPropertes();
            List<PvtResultIf> pvts = pvv.getPVTResultList();

            for (int i = 0, n = pvts.size(); i < n; i++) {
                PvtResultIf pvt = pvts.get(i);
                PvtChildSegment child = new PvtChildSegment(xml);
                seg_pvts.children.add(child);
                child.setValue(pvt);
            }
        }
    }

    /**
     *
     * @param info info
     */
    @Override
    public void setInformation(String info) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_INFORMATION, localName2element, info);
    }

    /**
     *
     * @param nr bill number
     */
    @Override
    public void setBillNumber(String nr) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_BILL_NUMBER, localName2element, nr);
    }

    /**
     *
     * @param date bill date
     */
    @Override
    public void setBillDate(Date date) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_BILL_DATE, localName2element, date);
    }

    /**
     *
     * @param pvt pvt
     */
    @Override
    public void addPVT(PvtResultIf pvt) {
        Segment pvt_childs = (Segment) localName2element.get(KainInkaStatics.NAME_SEGMENT_PVT_CHILD);
        Element xml = pvt_childs.xmlElement;
        //PVTChildSegment child = new PVTChildSegment(xmlElement, logger);
        PvtChildSegment child = new PvtChildSegment(xml);
        localName2element.put(child.getName(), child);
        //localName2element.put(KainInkaStatics.NAME_SEGMENT_PVT, child);
        children.add(child);
        child.setValue(pvt);
    }

    /**
     *
     * @param pvt pvt
     */
    public void addPVT2(PvtResultIf pvt) {
        Segment elem = (Segment) localName2element.get(KainInkaStatics.NAME_SEGMENT_PVT);
        Segment pvt_pattern = (Segment) localName2element.get(KainInkaStatics.NAME_SEGMENT_PVT_CHILD);
        if (pvt_pattern == null) {
            LOG.log(Level.WARNING, "pvt_pattern is null!");
            return;
        }
        Element xml = pvt_pattern.xmlElement;

        PvtChildSegment pvtSeg = new PvtChildSegment(xml);
        elem.children.add(pvtSeg);
        pvtSeg.setValue(pvt);
    }

    /**
     *
     * @return information
     */
    @Override
    public String getInformation() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_INFORMATION, localName2element);
    }

    /**
     *
     * @return bill number
     */
    @Override
    public String getBillNumber() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_BILL_NUMBER, localName2element);
    }

    /**
     *
     * @return bill date
     */
    @Override
    public Date getBillDate() {
        return (Date) getFieldValue(KainInkaStatics.NAME_PROPERTY_BILL_DATE, localName2element);
    }

    /**
     *
     * @return list of pvt results
     */
    @Override
    public List<PvtResultIf> getPVTResultList() {
        //erstmal nicht benutzt
        return null;
    }

    /**
     *
     * @return list of pvt results
     */
    public List<KainInkaElement> getPVTs() {
        Segment pvts = (Segment) localName2element.get(KainInkaStatics.NAME_SEGMENT_PVT);
        return pvts.getChildren();
    }

}

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
package de.lb.cpx.server.catalog.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.grouper.model.util.GrouperConstant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author wilde
 */
public class CatalogUtil {

    private CatalogUtil() {
        //utility class needs no public constructor
    }

    private static final int GROUPER_MIN_YEAR = GrouperConstant.GROUPER_MIN_YEAR; //2013;

    public static Integer getCatalogYearForGrouperModel(Date admissionDate, GDRGModel model) {
        if (model.equals(GDRGModel.AUTOMATIC)) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(admissionDate);
            return calendar.get(Calendar.YEAR);
        }

        return model.getCatalogYear();
    }

    public static GDRGModel[] getActualGrouperModels(boolean addDestGrouper) {
//        Object[] allGrouper = GDRGModel.values();
//        List<Object> actualGrouper = new ArrayList<>();
//        for(Object model : allGrouper){
//            GDRGModel mObj = (GDRGModel) model;
//            if(mObj.isUsed()){
//                actualGrouper.add(model);
//            }
//        }
//        
//        return actualGrouper.toArray();
        int[] allGrouper = GDRGModel.getAvailableModelsFromYear(GROUPER_MIN_YEAR, addDestGrouper);
        List<GDRGModel> actualGrouper = new ArrayList<>();
        for (int model : allGrouper) {
            GDRGModel currModel = GDRGModel.getModel(model);
            actualGrouper.add(currModel);
        }

        GDRGModel[] modelList = new GDRGModel[actualGrouper.size()];
        actualGrouper.toArray(modelList);
        return modelList;
    }

    public static List<GDRGModel> getActualGrouperModelsAsList(boolean addDestGrouper) {

        int[] allGrouper = GDRGModel.getAvailableModelsFromYear(GROUPER_MIN_YEAR, addDestGrouper);
        List<GDRGModel> actualGrouper = new ArrayList<>();
        for (int model : allGrouper) {
            GDRGModel currModel = GDRGModel.getModel(model);
            actualGrouper.add(currModel);
        }

//        GDRGModel[] modelList = new GDRGModel[actualGrouper.size()];
//        actualGrouper.toArray(modelList);
        return actualGrouper;
    }
}

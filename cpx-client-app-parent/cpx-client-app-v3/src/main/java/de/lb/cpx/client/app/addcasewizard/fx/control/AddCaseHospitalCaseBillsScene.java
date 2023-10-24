/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.addcasewizard.fx.control;

import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 *
 * @author gerschmann
 */
public class AddCaseHospitalCaseBillsScene  extends CpxScene {
    
        private final AddCaseServiceFacade serviceFacade;
        
        public AddCaseHospitalCaseBillsScene(AddCaseServiceFacade pFacade) throws IOException{
            super(CpxFXMLLoader.getLoader(AddCaseHospitalCaseBillsFXMLController.class));
            serviceFacade = pFacade;
            getController().init(serviceFacade);
        }
    
        @Override
        public AddCaseHospitalCaseBillsFXMLController getController(){
            return (AddCaseHospitalCaseBillsFXMLController)super.getController();
        }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.addcasewizard.fx.control;

import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;


/**
 *
 * @author gerschmann
 */
public class AddCaseHospitalCaseDetailsScene extends CpxScene {
        private final AddCaseServiceFacade serviceFacade;
        
        public AddCaseHospitalCaseDetailsScene(AddCaseServiceFacade pFacade) throws IOException{
            super(CpxFXMLLoader.getLoader(AddCaseHospitalCaseDetailsFXMLController.class));
            serviceFacade = pFacade;
            getController().init(serviceFacade);
        }
    
        @Override
        public AddCaseHospitalCaseDetailsFXMLController getController(){
            return (AddCaseHospitalCaseDetailsFXMLController)super.getController();
        }
}

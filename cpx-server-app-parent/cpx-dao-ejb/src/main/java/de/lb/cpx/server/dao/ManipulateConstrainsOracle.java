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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

/**
 *
 * @author gerschmann
 */

/*@NamedNativeQueries({
    @NamedNativeQuery(name = "getConstraintsList", query = "SELECT table_name, constraint_name FROM user_constraints WHERE constraint_type = 'R'"),
}
)*/

 /*
@Stateless
public class ManipulateConstrainsOracle implements ManipulateConstraints{
    
    @Inject @CpxEntityManager
    private EntityManager entityManager;


    @Override
    public void deactivateConstraints4Import() {
      //Logger.getLogger(ManipulateConstrainsOracle.class.getName()).log(Level.INFO, "deactivate constraints for Import");
      //List<Object[] >constraint2TableList = getConstraintsList();
      //performAlterConstraintsState(constraint2TableList, "disable");
    }
    
    private List<Object[]> getConstraintsList(){
        Query query = entityManager.createNativeQuery("SELECT table_name, constraint_name FROM user_constraints WHERE constraint_type = 'R'");
        return query.getResultList();
    }


    @Override
    public void deactivateConstraints4Grouping() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void activateConstraintsAfterImport() {
        Logger.getLogger(ManipulateConstrainsOracle.class.getName()).log(Level.INFO, "reactivate constraints after Import");
        
        List<Object[] >constraint2TableList = getConstraintsList();
        performAlterConstraintsState(constraint2TableList, "enable");
    }


    @Override
    public void activateConstraintsAfterGrouping() {

        
    }
    
    private void performAlterConstraintsState(List <Object[] >constraint2TableList, String doWhat){
        try {

            if(constraint2TableList != null){
                for(Object[] oneObj: constraint2TableList){
                    if(oneObj.length == 2){
                        String table_name = (String)oneObj[0];
                        String constraint_name = (String)oneObj[1];
                        String sql = "alter table  " +  table_name + " " + doWhat +" constraint " + constraint_name;
                        Query query = entityManager.createNativeQuery(sql);
                        query.executeUpdate();
                    }
                }
            }

            Logger.getLogger(ManipulateConstrainsOracle.class.getName()).log(Level.INFO, doWhat +" constraints successful");
        } catch (final RuntimeException ex) {
            Logger.getLogger(ManipulateConstrainsOracle.class.getName()).log(Level.INFO, doWhat + " constraints failed", ex);

        }
        
    }

}
 */

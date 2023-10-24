/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.catalog;

import de.lb.cpx.server.commonDB.model.COpsAop;
import de.lb.cpx.shared.lang.Lang;
import javax.validation.constraints.NotNull;

/**
 *
 * @author gerschmann
 */
public class CpxOpsAop extends COpsAop{
  private static final long serialVersionUID = 1L;
  
   private String createCategoryString(@NotNull String pCategory) {
        String ret = "";
        if(pCategory.length() == 0){
            return "";
        }
        if(pCategory.length() == 1){
            return  pCategory + "(" + (pCategory.equals("1")?Lang.getCatalogAopCategoryDescription1():Lang.getCatalogAopCategoryDescription2()) + ")";
        }
        // 1,
        ret = "\r\n";
        String[] parts= pCategory.split("1,");
        if(parts.length == 2){
            ret += parts[0] + "1" + "(" + Lang.getCatalogAopCategoryDescription1() + ")\r\n";
            if(parts[1].endsWith("2")){
                ret += parts[1] + "(" + Lang.getCatalogAopCategoryDescription2() + ")";
            }else {
                String[] parts2 = parts[1].split("2,");
                if(parts2.length== 2){
                    ret += parts2[0] + "2"  + "(" + Lang.getCatalogAopCategoryDescription2() + ")\r\n" + parts2[1];
                }else {
                    ret += parts[1];
                }
            }
        }else{
          parts= pCategory.split("2,");  
          if(parts.length == 2){
              ret += parts[0] + "2"  + "(" + Lang.getCatalogAopCategoryDescription2() + ")\r\n" + parts[1];
          }else{
              ret += parts[0];
          }
        }
        return ret;
    }

    public String getCategoryString() {
        if(getCatalogSheet() < 0){
            return "";
        }
        return ( getCatalogSheet() == 1?Lang.getCatalogAopSheet() + " 1":Lang.getCatalogAopSheet() + " 2")
                    + (getOpsCategory().trim().length() == 0?"":( ", "+ Lang.getCatalogAopCategory() + ": " + createCategoryString(getOpsCategory().trim())));
    }

}

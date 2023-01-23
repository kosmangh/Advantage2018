/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.utils;

import com.sabonay.advantage.ejb.sessionbean.CustomDataSource;
import com.sabonay.advantage.ejb.sessionbean.SHCCRUDManager;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Edwin / Ritchid
 */
public class ds implements Serializable
{
    
    private static SHCCRUDManager sHCCRUDManager;
    private static CustomDataSource customDataSource;

    public static SHCCRUDManager getCommonQry() {
        try {
            
            if(sHCCRUDManager != null)
                return sHCCRUDManager;

            Context c = new InitialContext();
            sHCCRUDManager = (SHCCRUDManager) c.lookup("java:global/shc/SHCCRUDManager!com.sabonay.advantage.ejb.sessionbean.SHCCRUDManager");
            
            if(sHCCRUDManager == null)
            {
                sHCCRUDManager = (SHCCRUDManager) c.lookup(SHCCRUDManager.class.getName());
            }
            
            
            return sHCCRUDManager;

        } catch (NamingException ne)
        {
            Logger.getLogger(ds.class.getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public static CustomDataSource getCustomQry(){
        try {
            Context c = new InitialContext();
            customDataSource= (CustomDataSource) c.lookup("java:global/shc/CustomDataSource!com.sabonay.advantage.ejb.sessionbean.CustomDataSource");

            if(customDataSource == null){
                customDataSource = (CustomDataSource) c.lookup(CustomDataSource.class.getName());
            }


            return customDataSource;

        } catch (NamingException ne){
            Logger.getLogger(ds.class.getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.sessionbean;

import com.sabonay.advantage.common.utils.AdConstants;
import com.sabonay.common.utils.StringUtil;
import com.sabonay.modules.web.jsf.accesscontrol.WebUserData;
import java.io.File;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author crash / Ritchid
 */
@Named
@SessionScoped
public class UserData extends WebUserData implements Serializable{
    
    private String directorSignature;
    private String shcServerPath;
    public static  String DOC_ROOT = System.getProperty("com.sun.aas.instanceRoot")
            + File.separator + "docroot"
            + File.separator + "SHC_RES" + File.separator;
    
    
    /**
     * 
     * We initialize the path to the server docroot for director signature
     */
    public void init(){
        try {
            shcServerPath = DOC_ROOT + File.separator;
            
            directorSignature = shcServerPath + AdConstants.DIR_SIGNATURE + ".png";
            
            directorSignature = StringUtil.ecapeBackSlash(directorSignature);
        } catch (Exception e) {
        }
    }
    
    public String reloadPageWithNewFragment(String fragment) {
        setRequestedPageURL(fragment);
        return "index.xhtml";
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getDirectorSignature() {
        return directorSignature;
    }

    public void setDirectorSignature(String directorSignature) {
        this.directorSignature = directorSignature;
    }

    public String getShcServerPath() {
        return shcServerPath;
    }

    public void setShcServerPath(String shcServerPath) {
        this.shcServerPath = shcServerPath;
    }
    
    public static String getDOC_ROOT() {
        return DOC_ROOT;
    }

    public static void setDOC_ROOT(String DOC_ROOT) {
        UserData.DOC_ROOT = DOC_ROOT;
    }
    //</editor-fold>

}

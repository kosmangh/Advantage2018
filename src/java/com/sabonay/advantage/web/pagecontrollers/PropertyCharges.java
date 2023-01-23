
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.GroundRent;
import com.sabonay.advantage.modules.account.AdvantageUserData;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

/**
 *
 * @author Ritchid
 */
@Named(value = "propertyCharges")
@SessionScoped
public class PropertyCharges implements Serializable{


    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    @Inject
    AdvantageUserData user;
    
    private int grndRntTabIndex;
    private int rntalTabIndex;
    
    private double [][] charges;
    
    private String orient;
    
    private boolean rendered;
    private boolean firstClassRendered;
    
    private List<GroundRent> grndRentList;
    
    private DataModel<GroundRent> grndRentDataModel;
    
    private GroundRent grndRent;
    private static final Logger LOG = Logger.getLogger(PropertyCharges.class.getName());
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Instantiators">
    
    public PropertyCharges() {
        orient = "Ground Rents";
        rendered = false;
        grndRntTabIndex = 0;
        rntalTabIndex = 0;
        charges = new double[3][4];
        
        grndRent = new GroundRent();
    }
    
    @PostConstruct
    public void init(){
        getAllGrndRentCharges();
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Business Logics">
    public void getAllGrndRentCharges(){
        try {
            grndRentList = ds.getCommonQry().groundRentGetAll(false);
            grndRentDataModel = new ListDataModel<>(getGrndRentList());
            
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            JsfUtil.addErrorMessage(null, "Error", "Unable to load ground rent list, try again");
        }
    }
    
    public void saveNewRate(){
        try {
//            List<GroundRent> result = ds.getCommonQry().groundRentFindByAttribute("yearDue", grndRent.getYearDue(), "NUMBER", false);
//            if(!result.isEmpty()){
//                JsfUtil.addErrorMessage(null, "Rate Exist Already", "Select Year rate has been captured already");
//                grndRntTabIndex = 1;
//            } else {
//                System.out.println(charges.length+"-----");
////                List<GroundRent> rates = new ArrayList<>();
////                for(int a = 1; a<= charges.length; a++){
////                    GroundRent gr = new GroundRent();
////                    IDCreator.setGroundRentId(gr);
////                    gr.setYearDue(grndRent.getYearDue());
////                    for(int index = 0; index <= charges[index].length; index ++){
////                        if(index == 0){
////                            
////                        } else if  (index == 1){
////                            
////                        } else if (index == 2){
////                            
////                        }
////                    }
////                }
//            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact the system administrator for solutions");
        }
    }
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="System Logics">
    public void orientationListeners(){
        if("Ground Rents".equals(orient)) {
            rendered = false;
            grndRntTabIndex = 0;
        } else {
            rendered = true;
            rntalTabIndex = 0;
        }
    }
    
    public void refreshGrndRnt(){
        getAllGrndRentCharges();
    }
    
    public void toGrndRntList(){
        grndRent = new GroundRent();
        charges = null;
        grndRntTabIndex = 0;
    }
    
    public  void toNewGrndRnt(){
        charges = new double[3][4];
        grndRent.setYearDue(new Date().getYear());
        grndRntTabIndex = 1;
    }
    
    public void toUpdateGrndRnt(){
        grndRntTabIndex = 2;
    }
    
    public void toRntalList(){
        rntalTabIndex = 0;
    }
    
    public  void toNewRntal(){
        rntalTabIndex = 1;
    }
    
    public void toUpdateRntal(){
        rntalTabIndex = 2;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public String getOrient() {
        return orient;
    }

    public void setOrient(String orient) {
        this.orient = orient;
    }

    public int getGrndRntTabIndex() {
        return grndRntTabIndex;
    }

    public void setGrndRntTabIndex(int grndRntTabIndex) {
        this.grndRntTabIndex = grndRntTabIndex;
    }

    public int getRntalTabIndex() {
        return rntalTabIndex;
    }

    public void setRntalTabIndex(int rntalTabIndex) {
        this.rntalTabIndex = rntalTabIndex;
    }

    public GroundRent getGrndRent() {
        return grndRent;
    }

    public void setGrndRent(GroundRent grndRent) {
        this.grndRent = grndRent;
    }
    
    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
    
    public List<GroundRent> getGrndRentList() {
        return grndRentList;
    }

    public void setGrndRentList(List<GroundRent> grndRentList) {
        this.grndRentList = grndRentList;
    }

    public double[][] getCharges() {
        return charges;
    }

    public void setCharges(double[][] charges) {
        this.charges = charges;
    }

    public DataModel<GroundRent> getGrndRentDataModel() {
        return grndRentDataModel;
    }

    public void setGrndRentDataModel(DataModel<GroundRent> grndRentDataModel) {
        this.grndRentDataModel = grndRentDataModel;
    }
    
    
    //</editor-fold>


}

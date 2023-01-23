
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.modules.account.AdvantageUserData;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.math.NumberUtils;


/**
 * @author Ritchid
 */

@Named
@RequestScoped
public class EstateController implements Serializable {
    
//<editor-fold defaultstate="collapsed" desc="Implemented Variables">
    private String searchItem;
    private String searchString;
    private String totalEstates;
    private static String estateInitial;
    private static String estateName;
    
    private boolean permDelete;
    
    private static int tabIndex;
    
    private Date currentDate;
    private static Estate estEdit;
    private Estate estate;
    private static final Logger LOG = Logger.getLogger(EstateController.class.getName());
    
    private List<Estate> estateList;
    private DataModel<Estate> estateDataModel;
    
    
    @Inject
    AdvantageUserData currentUser;
//</editor-fold>
 
    
//<editor-fold defaultstate="collapsed" desc="Initializers">
    public EstateController() {        
        totalEstates = "0";
        
        estate = new Estate();
        currentDate = new Date();
        
    }
    
    @PostConstruct
    private void init() {
        loadEstates();
    }
//</editor-fold>
    
    
//<editor-fold defaultstate="collapsed" desc="Business Logics">
    
    //This loads the estate table with all estates from the database
    public void loadEstates(){
        estateList = new ArrayList<>(ds.getCommonQry().estateGetAll(false));
        estateDataModel = new ListDataModel<>(estateList);
        totalEstates = String.valueOf(estateList.size()) +" Estate(s) Available";
    }
    
    /**
     * This back bean functionality searches for an estate base on the search item (Name or Location)
     * and the search word (String)
    */
    public void estateSearch() {
        try {
            if("".equals(searchString)){
                loadEstates();
                toEstateList();
                
            }else{
                estateList = ds.getCommonQry().estateFindByAttribute(searchItem, searchString, "STRING", false);
                estateDataModel = new ListDataModel<>(estateList);
                totalEstates = "Found "+estateList.size()+" Estate(s)";
                toEstateList(); 
            }
            
        } catch (Exception e) {
            toEstateList();
            LOG.log(Level.SEVERE, e.toString(), e);
            JsfUtil.addErrorMessage(null, "System Error !", " An error cocurred in searching for estate info");
        }
    }
    
    //Functionality to delete selected estates from estate's table
    public void deleteEstate() {
        try {
            List<Estate> selectedEstList = new ArrayList<>();
            List<Estate> deletingEstates = new ArrayList<>();
            
            estateList.stream().filter((est) -> (est.isSelected())).forEach((est) -> {
                selectedEstList.add(est);
            });
            
            if(selectedEstList.isEmpty()){
                toEstateList();
                permDelete = false;
                JsfUtil.addErrorMessage(null, "No Estate(s) Selected", "No Estate(s) has been selected from the list");
                
            } else {
                for (Estate es : selectedEstList) {
                    if (es.getTotalBlock() > 0) {
                        toEstateList();
                        permDelete = false;
                        JsfUtil.addErrorMessage(null,"Deletion Error", es.getEstateName()+" has been assigned Block(s), remove assigned Block(s) first");
                        deletingEstates.clear();
                        break;
                        
                    } else {
                        es.setLastModifiedBy(currentUser.getCurrentUser().getUserAccountId());
                        es.setLastModifiedDate(new Date());
                        deletingEstates.add(es);
                    }
                }
                
                if(!deletingEstates.isEmpty()){
                    boolean deleted = ds.getCommonQry().batchEstateDelete(deletingEstates, permDelete);

                    permDelete = false;

                    if (deleted == true) {
                        loadEstates();
                        toEstateList();
                        JsfUtil.addInformationMessage("Estate deleted sucessfully ");

                    } else if (deleted == false) {
                        toEstateList();
                        JsfUtil.addErrorMessage(null, "Error Deleting Estates", "An error occured when trying to delete Estate(s). Try again later");
                    }
                }
            }
        } catch (Exception e) {
            permDelete = false;
            LOG.log(Level.SEVERE, e.toString(), e);
            JsfUtil.addErrorMessage(null, "System Error!", "System encountered an error. Please contact system administrator for solutions");
        }
    }
    
    /**
     * This functionality of Advantage saves a new estate into the database
    */
    public void saveNewEstate() {
        try {
            List<Estate> idReslt = ds.getCommonQry().estateFindByAttribute("estateId", estate.getEstateId(), "STRING", false);
            List<Estate> estExist = ds.getCommonQry().estateFindByAttribute("estateName", estate.getEstateName(), "STRING", false);
            
            if(!estExist.isEmpty()){
                JsfUtil.addErrorMessage(null, "Estate Already Exist", "An Estate already exist with this name ( " +estate.getEstateName()+" )");
                
            }else if(!idReslt.isEmpty()) {
                JsfUtil.addErrorMessage(null, "Initials Already Exist", "Specified initials already exist i.e. " +estate.getEstateId());

            }else{
                boolean created = ds.getCommonQry().estateCreate(estate);
            
                if (created == true) {
                    loadEstates();
                    estate = new Estate();
                    JsfUtil.addInformationMessage(null, "New Estate Created Successfully" ); 
                    
                } else {
                    JsfUtil.addErrorMessage(null, "Error", "System unable to create Estate");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.toString(), e);
            JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Contact the System Administrator for solutions");
        }
    }
    
    /** This functionality updates the details of the selected estate and also records the user(thus the one making the
      * update) and time of updating
    */
    public void updateEstate() {
        try {
            if(!NumberUtils.isNumber(String.valueOf(estEdit.getLandSize()))) {
                JsfUtil.addErrorMessage(null,"Land Size Error", "Land Size Must Be Numeric");
            }else{
                System.out.println("Current Estate Initials : "+estateInitial);
                System.out.println("Current Estate Name : "+estateName);
                
                List<Estate> estExist = ds.getCommonQry().estateFindByAttribute("estateName", estEdit.getEstateName(), "STRING", false);
                List<Estate> idReslt = ds.getCommonQry().estateFindByAttribute("estateId", estEdit.getEstateId(), "STRING", false);
                
                if(!estEdit.getEstateId().equalsIgnoreCase(estateInitial)){
                    if(!idReslt.isEmpty()) 
                        JsfUtil.addErrorMessage(null, "Initials Already Exist", "Specified initials already exist i.e. " +estEdit.getEstateId());
                        
                }else if(!estEdit.getEstateName().equalsIgnoreCase(estateName)){
                    if(!estExist.isEmpty())
                        JsfUtil.addErrorMessage(null, "Estate Already Exist", "An Estate already exist with this name ( " +estEdit.getEstateName()+" )");
                    
                }
                
                if((idReslt.isEmpty()) || (estExist.isEmpty())){
                    estEdit.setLastModifiedDate(new Date());
                    estEdit.setLastModifiedBy(currentUser.getCurrentUser().getUserAccountId());
                    
                    boolean updated = ds.getCommonQry().estateUpdate(estEdit);
                    if (updated == true) {
                        loadEstates();
                        JsfUtil.addInformationMessage("Estate details Updated Successfully");
                        
                    } else {
                        JsfUtil.addErrorMessage(null, "Error", "Unable to Update Estate Info");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.toString(), e);
            JsfUtil.addErrorMessage(null, "System Error", "System Encountered an Error !");
        }
    }
    
//</editor-fold>
    
    
//<editor-fold defaultstate="collapsed" desc="System Logics">
    //This functionality switches the estate's tab to New Estate tab
    public void toNewEstate(){
        tabIndex = 1;
    }
    
    //This functionality switches the estate's tab to Estate List tab
    public void toEstateList(){
        estEdit = new Estate();
        estateInitial = null;
        estateName = null;
        tabIndex = 0;
    }
    
    //This functionality switches the estate's tab to Edit Estate tab
    public void toEditEstate(){
        tabIndex = 2;
    }
    
     /**
     * This is a back bean functionality of Advantage that retrieves the row being selected to be edited 
     * from the estate's list (table)
     */
    public void estateRowSelction() {
        try {
            estEdit = (Estate) estateDataModel.getRowData();
            estateInitial = estEdit.getEstateId();
            estateName = estEdit.getEstateName();
            toEditEstate();
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(null, "Error !", "Error occured in selecting estate");
            LOG.log(Level.SEVERE, e.toString(), e);
        }
    }
    
    //This functionality refreshes the estate's list
    public void refreshList(){
        searchString = "";
        loadEstates();
        toEstateList();
    }
//</editor-fold>
    
       
//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public Estate getEstate() {
        return estate;
    }

    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    public boolean isPermDelete() {
        return permDelete;
    }

    public void setPermDelete(boolean permDelete) {
        this.permDelete = permDelete;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public List<Estate> getEstateList() {
        return estateList;
    }

    public void setEstateList(List<Estate> estateList) {
        this.estateList = estateList;
    }

    public DataModel<Estate> getEstateDataModel() {
        return estateDataModel;
    }

    public void setEstateDataModel(DataModel<Estate> estateDataModel) {
        this.estateDataModel = estateDataModel;
    }


    public Estate getEstEdit() {
        return estEdit;
    }

    public void setEstEdit(Estate estEdit) {
        this.estEdit = estEdit;
    }
 
    public String getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getEstateName() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName = estateName;
    }

    public String getTotalEstates() {
        return totalEstates;
    }

    public void setTotalEstates(String totalEstates) {
        this.totalEstates = totalEstates;
    }

    public String getEstateInitial() {
        return estateInitial;
    }

    public void setEstateInitial(String estateInitial) {
        this.estateInitial = estateInitial;
    }
//</editor-fold>
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Edwin/Ritchid
 */
@RequestScoped
@Named(value = "estateBlockController")
public class EstateBlockController implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger LOG = Logger.getLogger(EstateBlockController.class.getName());
    
    @Inject
    AdvantageUserData currentUser;
    
    private String totalBlocks;
    private String searchItem;
    private String searchString;
    private static String dispEstate = "null"; //for displaying selected block's estate name
    
    private boolean permDelete;
    
    private int tabIndex;
    
    private EstateBlock estateBlock;
    private static EstateBlock selectedBlock;
    private Estate selectedEstate;
    
    private List<EstateBlock> estateBlocksList;
    
    private DataModel<EstateBlock> estateBlockDataModel;
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Initializers">
    public EstateBlockController() {
        tabIndex = 0;
        
        totalBlocks = "0";
    }

    @PostConstruct
    private void init() {
        loadBlocks();
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="System Logics">
    
    //This also allows users to select a block from the block's list to view and edit
    public void select() {
        try {
            selectedBlock = (EstateBlock)estateBlockDataModel.getRowData();
            dispEstate = selectedBlock.getEstate().getEstateName();
            tabIndex = 2;
            
        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
        }
    }

    public void toEstateBlkList(){
        tabIndex = 0;
    }
    
    //This method takes the current page to the new estate block page
    public void toNewEstateBlk(){
        selectedEstate = null;
        tabIndex = 1;
    }
    
    public void toUpdEstateBlk(){
        tabIndex = 2;
    }
    
    //This functionality refreshes the Estate Blocks table
    public void refresh(){
        searchString = "";
        loadBlocks();
        tabIndex = 0;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Business Logics">
    
    /**
     * Loads all estate's blocks from the database only if the deleted field is false
     */
    public void loadBlocks(){
        estateBlocksList = new ArrayList(ds.getCommonQry().estateBlockGetAll(false));
        estateBlockDataModel = new ListDataModel<>(estateBlocksList);
        totalBlocks = String.valueOf(estateBlocksList.size());
    }
    
    //This is a functionality of Advantage for searching for an estate block
    public void blockSearch() {
        try {
            estateBlocksList = new ArrayList(ds.getCommonQry().estateBlockFindByAttribute(searchItem, searchString, "STRING", false));
            estateBlockDataModel = new ListDataModel<>(estateBlocksList);
            tabIndex = 0;
            
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    //Action on the Estate Block page to delete one or more Estate(s)
    public void deleteBlks() {
        try {
            List<EstateBlock> selectedBlkList = new ArrayList<>();
            List<EstateBlock> deletingBlocks = new ArrayList<>();
            
            estateBlocksList.stream().filter((blk) -> (blk.getSelected() == true )).forEach((blk) -> {
                selectedBlkList.add(blk);
            });
            
            if(selectedBlkList.isEmpty()){
                permDelete = false;
                JsfUtil.addErrorMessage(null, "No Block Selected", "Select Block(s) from the list to be deleted");
                
            } else {
                for (EstateBlock eb : selectedBlkList) {                        
                    if (eb.getPropertiesSize() > 0) {
                        permDelete = false;
                        JsfUtil.addErrorMessage(null, "Deletion Error", "Selected Block ("+ eb.getBlockName()+ ") of "+eb.getEstate().getEstateName()+" has assigned properties, remove assigned properties before deleting");
                        break;
                        
                    }else{
                        eb.setLastModifiedDate(new Date());
                        eb.setLastModifiedBy(currentUser.getCurrentUser().getUserAccountId());
                        deletingBlocks.add(eb);
                    }
                }

                if(!deletingBlocks.isEmpty()){
                    boolean deleted = ds.getCommonQry().batchEstateBlockDelete(deletingBlocks, permDelete);

                    permDelete = false;
                    if (deleted == true) {
                        loadBlocks();
                        toEstateBlkList();
                        JsfUtil.addInformationMessage(deletingBlocks.size()+" Block(s) Deleted Successfully");
                    } else {
                        loadBlocks();
                        JsfUtil.addErrorMessage(null, "Error deleting block", "An error occured when trying to delete block(s). Try again later");
                    }
                }
            }
        } catch (Exception e) {
            permDelete = false;
            Logger.getLogger(e.getMessage());
            JsfUtil.addErrorMessage(null, "System Error!", "System encountered an error. Please contact system administrator for solutions");
        }
        refresh();
    }
    
     /**
     * Functionality to accept users input on the creation of a new block related to an
     * existing estate
     */
    public void saveBlk() {
        try {           
             //checking for the existence of the estate block
            boolean check = ds.getCustomQry().doesEstateBlockExist(estateBlock.getEstate().getEstateId(), estateBlock.getBlockName());

            if(check == true){
                JsfUtil.addErrorMessage(null, "Block Already Exist", "Block entered already exist for the selected estate; "+estateBlock.getEstate().getEstateName());
            }else{
//                if(estateBlock.getEstate().getLandsizeleft() < estateBlock.getBlkSize()){
//                    JsfUtil.addErrorMessage(null, "Block Size Error", "Block size is larger than Estate size left");
//                } else {
                    IDCreator.setEstateBlockId(estateBlock);
                    boolean created = ds.getCommonQry().estateBlockCreate(estateBlock);

                    if (created == true) {
                        loadBlocks();
                        estateBlock = new EstateBlock();
                        JsfUtil.addInformationMessage("New Block Created Successfully");
                        
                    }else{
                        JsfUtil.addErrorMessage(null, "Error", "Error creating Estate Block. Try again later");
                    }
//                }
            }
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.toString(), e);
            JsfUtil.addErrorMessage(null, "System Error", "System Encountered an Error during operation. Contact system administrator for solutions");
        }
    }
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean isPermDelete() {
        return permDelete;
    }

    public void setPermDelete(boolean permDelete) {
        this.permDelete = permDelete;
    }
    
    public String getTotalBlocks() {
        return totalBlocks;
    }

    public void setTotalBlocks(String totalBlocks) {
        this.totalBlocks = totalBlocks;
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

    public String getDispEstate() {
        return dispEstate;
    }

    public void setDispEstate(String dispEstate) {
        EstateBlockController.dispEstate = dispEstate;
    }

    public EstateBlock getSelectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(EstateBlock selectedBlock) {
        EstateBlockController.selectedBlock = selectedBlock;
    }

    public EstateBlock getEstateBlock() {
        return estateBlock;
    }

    public void setEstateBlock(EstateBlock estateBlock) {
        this.estateBlock = estateBlock;
    }
    
    //</editor-fold>



    
    
    
    private String blockName;
    private boolean rendered = false;
    private boolean infoRendered = false;


    

    public String saveButtonAction() {

        try {
              estateBlock.setEstate(selectedEstate);
        IDCreator.setEstateBlockId(estateBlock);

        boolean created = ds.getCommonQry().estateBlockCreate(estateBlock);   //the ds.getCommonQry() is used when u want to comm with the database
        String dataEntered = estateBlock.getBlockName();
         
        if (created != false) {
            JsfUtil.addInformationMessage("Block Created");
        } //my version of the code
        else if (dataEntered == null) {
            JsfUtil.addErrorMessage("block name cannot be empty");
        } //end my version of code
        else {
            //JsfUtil.addErrorMessage("Choose A Block From The Drop List");
            EstateBlock checkBlock
                 = ds.getCommonQry().estateBlockFind(estateBlock.getEstateBlockId());

            if (checkBlock != null) {
                JsfUtil.addErrorMessage("Block Already exist");
            } else {
                JsfUtil.addErrorMessage("Choose A Block From The Drop List");
            }
        }
        getNewList();
        clearButtonAction();
        return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String editButtionAction() {
        try {
            boolean updated = ds.getCommonQry().estateBlockUpdate(estateBlock);
            if(updated == true){
                JsfUtil.addInformationMessage("Block updated");
            }else{
                JsfUtil.addErrorMessage("Error updating block");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.toString(), e);
        }
        getNewList();
        return null;
    }

    public String deleteButtonAction() {
        try {
            for (EstateBlock eb : estateBlocksList) {
                if (eb.getSelected()) {
                    boolean blockHasEstateProperties = ds.getCustomQry().hasBlockBeenUsed(eb.getEstateBlockId());
                    System.out.println("blockHasEstateProperties " + blockHasEstateProperties);
                    if (blockHasEstateProperties == true) {
                        JsfUtil.addErrorMessage("Selected Block has assigned properties,"
                                + " please remove assigned properties first");
                    }else{
                        boolean deleted = ds.getCommonQry().estateBlockDelete(eb, false);

                        if (deleted == true) {
                            JsfUtil.addInformationMessage("Block Deleted");
                        } else {
                            JsfUtil.addErrorMessage("Error deleting block");
                        } 
                    }
                }
            }
        } catch (Exception e) {
        }
        getNewList();
        return null;
    }

    

    public void clearButtonAction() {
        estateBlock = new EstateBlock();
        rendered = false;

    }

    public void searchButtonAction() {
        try {
            estateBlocksList = ds.getCommonQry().estateBlockFindByAttribute(searchItem, searchString, "STRING", rendered);
            estateBlockDataModel = new ListDataModel<EstateBlock>(estateBlocksList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void triggerDeleteEvent(ValueChangeEvent vce) {
        setInfoRendered((Boolean) vce.getNewValue());
    }
    
    private void getNewList(){
        estateBlocksList = ds.getCommonQry().estateBlockGetAll(false);
        estateBlockDataModel = new ListDataModel<EstateBlock>(estateBlocksList);
    }

//my version of the code
//    public void validate_blockname(FacesContext context, UIComponent componentToValidate, Object value)
//                throws ValidatorException{
//        String dataEntered = estateBlock.getBlockName();
//        dataEntered = ((String)value).toString();
//        if(dataEntered.trim().){
//            FacesMessage message = new FacesMessage("the block name cannot be empty");
//            throw new ValidatorException(message);
//        }
//    }
    //my version of the code
    

    public List<EstateBlock> getEstateBlocksList() {
        return estateBlocksList;
    }

    public void setEstateBlocksList(List<EstateBlock> estateBlocksList) {
        this.estateBlocksList = estateBlocksList;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Estate getSelectedEstate() {
        return selectedEstate;
    }

    public void setSelectedEstate(Estate selectedEstate) {
        this.selectedEstate = selectedEstate;
    }

    public String getDataEntered() {
        return blockName;
    }

    public void setdataEntered(String blockName) {
        this.blockName = blockName;
    }

    public DataModel<EstateBlock> getEstateBlockDataModel() {
        return estateBlockDataModel;
    }

    public void setEstateBlockDataModel(DataModel<EstateBlock> estateBlockDataModel) {
        this.estateBlockDataModel = estateBlockDataModel;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public boolean isInfoRendered() {
        return infoRendered;
    }

    public void setInfoRendered(boolean infoRendered) {
        this.infoRendered = infoRendered;
    }


    
    
}

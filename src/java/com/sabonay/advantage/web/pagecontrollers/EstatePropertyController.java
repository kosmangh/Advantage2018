/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.modules.account.AdvantageUserData;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.formating.NumberFormattingUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.joda.time.DateTime;

/**
 *
 * @author Edwin / Ritchid
 */
@RequestScoped
@Named(value = "estatePropertyController")
public class EstatePropertyController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private EstateProperty estateProperty;
    private EstateProperty upEstateProp;
    private Estate selectedEstate;
    private EstateBlock selectedEstateBlock;

    private DataModel<EstateProperty> estatePropertiesDataModel;

    private List<EstateProperty> estatePropertyList;

    private int tabIndex;

    private String totalProperties;
    private String filterBy;
    private String searchItem;
    private String searchString;

    @Inject
    AdvantageUserData currentUser;

    private static final Logger logg = Logger.getLogger(EstatePropertyController.class.getName());
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Intializers">
    public EstatePropertyController() {
        estateProperty = new EstateProperty();
        totalProperties = "0";
    }

    @PostConstruct
    public void init() {
        loadEstateProperty();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="System Logics">
    public void toNewEstateProp() {
        estateProperty = new EstateProperty();
        selectedEstate = null;
        selectedEstateBlock = null;
        tabIndex = 1;
    }

    public void toPropertyList() {
//        refreshList();
        tabIndex = 0;
    }

    public void toPropertyUpdate() {
        tabIndex = 2;
    }

    public void filterList() {
        List<EstateProperty> filteredList = new ArrayList<>();
        if ("Occupied".equalsIgnoreCase(filterBy)) {
            for (EstateProperty estProp : estatePropertyList) {
                if (estProp.getOccupied().equalsIgnoreCase("Occupied")) {
                    filteredList.add(estProp);
                }
            }
            totalProperties = filteredList.size() + " Occupied";
            estatePropertiesDataModel = null;
            estatePropertiesDataModel = new ListDataModel<>(filteredList);

        } else if ("Unoccupied".equalsIgnoreCase(filterBy)) {
            for (EstateProperty estProp : estatePropertyList) {
                if (estProp.getOccupied().equalsIgnoreCase("Unoccupied")) {
                    filteredList.add(estProp);
                }
            }
            totalProperties = filteredList.size() + " Unoccupied";
            estatePropertiesDataModel = null;
            estatePropertiesDataModel = new ListDataModel<>(filteredList);

        } else if ("House".equalsIgnoreCase(filterBy)) {
            for (EstateProperty estProp : estatePropertyList) {
                if (estProp.getPropertyCategory().equalsIgnoreCase("House")) {
                    filteredList.add(estProp);
                }
            }
            totalProperties = filteredList.size() + " House";
            estatePropertiesDataModel = new ListDataModel<>(filteredList);

        } else if ("Land".equalsIgnoreCase(filterBy)) {
            for (EstateProperty estProp : estatePropertyList) {
                if (estProp.getPropertyCategory().equalsIgnoreCase("Land")) {
                    filteredList.add(estProp);
                }
            }
            totalProperties = filteredList.size() + " Land";
            estatePropertiesDataModel = new ListDataModel<>(filteredList);
        } else {
            refreshList();
        }
    }

    public void refreshList() {
        searchItem = "estatePropertyId";
        filterBy = "All";
        searchString = "";
        loadEstateProperty();
        tabIndex = 0;
    }

    public void rowSelect() {
        try {
            upEstateProp = (EstateProperty) estatePropertiesDataModel.getRowData();
            toPropertyUpdate();

        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyController.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Error occurred in selecting EstateProperty from table ");
        }
    }

    public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
        selectedEstate = (Estate) event.getNewValue();
        if (selectedEstate == null) {
            estateBlockOptions = null;
        } else {
            estateBlockOptions = JsfUtil.createSelectItems(selectedEstate.getEstateBlockList(), true);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Business Logics">
    public void propertySearch() {
        try {
            if (searchString != null) {
                estatePropertyList = new ArrayList<>(ds.getCommonQry().estatePropertyFindByAttribute(searchItem, searchString, "STRING", false));
                estatePropertiesDataModel = new ListDataModel<>(estatePropertyList);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public void loadEstateProperty() {
        estatePropertyList = new ArrayList(ds.getCommonQry().estatePropertyGetAll(false));
        estatePropertiesDataModel = new ListDataModel<>(estatePropertyList);
        totalProperties = String.valueOf(estatePropertyList.size());
    }

    /**
     * Functionality for deleting a property from the system. This feature is
     * functioning if the selected property(s) to be deleted are unoccupied at
     * the moment by any occupant, otherwise, user will need to release a
     * property from occupancy before deletion can be performed
     */
    public void deleteProperty() {
        try {
            List<EstateProperty> selectedProps = new ArrayList<>();
            for (EstateProperty ep : estatePropertyList) {
                if (ep.getSelected()) {
                    selectedProps.add(ep);
                }
            }

            if (selectedProps.isEmpty()) {
                JsfUtil.addErrorMessage(null, "No Property Selected", "No property(s) has been selected from list to be deleted");

            } else {
                List<EstateProperty> deletingProps = new ArrayList<>();
                for (EstateProperty esp : selectedProps) {
                    if (esp.getOccupied().equalsIgnoreCase("Occupied")) {
                        JsfUtil.addErrorMessage(null, "Selected Property Occupied", esp.getPropertyName() + " is currently occupied. Release property from occupancy before deleting");
                        break;
                    } else {
                        esp.setLastModifiedBy(currentUser.getCurrentUser().getUserAccountId());
                        esp.setLastModifiedDate(new Date());
                        deletingProps.add(esp);
                    }
                }

                if (!deletingProps.isEmpty()) {
                    boolean deleted = ds.getCommonQry().batchEstatePropertyDelete(deletingProps, false);

                    if (deleted == true) {
                        refreshList();
                        JsfUtil.addInformationMessage("Property(s) Deleted Successfully");
                    } else {
                        JsfUtil.addErrorMessage(null, "Error", "Failed to Delete EstateProperty. Try again later ");
                    }
                }
            }
        } catch (Exception exp) {
            logg.log(Level.SEVERE, exp.getMessage(), exp);
            JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact the system administrator for solutions");
        }
    }

    public void saveProperty() {
        try {
            estateProperty.setEstateBlock(selectedEstateBlock);
            IDCreator.setEstatePropertyId(estateProperty);

            EstateProperty estatePropertyFind = ds.getCommonQry().estatePropertyFind(estateProperty.getEstatePropertyId());

            if (estatePropertyFind != null) {
                JsfUtil.addErrorMessage(null, "Property Already Exist", "A Property already exist with this Propery Number which is affilliated to " + selectedEstate.getEstateName() + ", " + selectedEstateBlock.getBlockName()+" Block");

            } else {
                boolean result = ds.getCommonQry().estatePropertyCreate(estateProperty);
                if (result == true) {
                    estateProperty = new EstateProperty();
                    selectedEstate = null;
                    selectedEstateBlock = null;
                    refreshList();
                    JsfUtil.addInformationMessage("Estate Property Created Successfully");

                } else {
                    JsfUtil.addErrorMessage(null, "Error", "Estate Property not created. Try again later");
                }
            }
        } catch (Exception exp) {
            logg.log(Level.SEVERE, exp.getMessage(), exp);
            JsfUtil.addErrorMessage("System Error!", "System encountered an error. Kindly contact the system administrator for solutions");
        }
    }
    
    public void updateProperty() {
        try {
            upEstateProp.setLastModifiedDate(new Date());
            upEstateProp.setLastModifiedBy(currentUser.getCurrentUser().getUserAccountId());
            
            boolean updated = ds.getCommonQry().estatePropertyUpdate(upEstateProp);
            
            if(updated == true){
                JsfUtil.addInformationMessage("Property Updated Successfully");
            } else {
                JsfUtil.addErrorMessage(null, "Error", "Unable to apply update. Try again later");
            }
//            boolean updated = ds.getCommonQry().estatePropertyUpdate(estateProperty);
//            if (updated == true) {
//             op = ds.getCommonQry().checkLease(estateProperty.getEstatePropertyId());
//             if(PropOccupationType.Leasehold == op.getOccupationType()){
//                 DateTime calendar = new DateTime();
//                 int year = calendar.getYear();
//                 System.out.println("year: "+ year); 
//                 PaymentType pt = null;
//                 pt = PaymentType.GROUND_RENT;
//                epl =  ds.getCommonQry().getYearsLedger(estateProperty.getEstatePropertyId(), year, DebitCredit.DEBIT, pt);
//                double amtToBeCharge = 0.00;  
//                amtToBeCharge = NumberFormattingUtils.formatDecimalNumberTo_2(getAppropriateGroundRentBill(estateProperty, year));
//                epl.setAmountInvolved(amtToBeCharge);
//                ds.getCommonQry().estatePropertyLedgerUpdate(epl); 
//             }
//                
//                JsfUtil.addInformationMessage("EstateProperty updated sucessfully ");
//            } else if (updated == false) {
//                JsfUtil.addErrorMessage("Failed to Update EstateProperty");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(EstatePropertyController.class.getName()).log(Level.SEVERE, e.getMessage());
            JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact the system administrator for solutions");
        }
    }
    //</editor-fold>

    private SelectItem estateBlockOptions[];
    private boolean rendered = false;
    private boolean infoRendered = false;
    private Occupancy op = new Occupancy();
    private EstatePropertyLedger epl = new EstatePropertyLedger();

    public String saveButtonAction() {
        try {
//            estateProperty.setEstateBlock(selectedEstateBlock);
//            IDCreator.setEstatePropertyId(estateProperty);
//
//            String estatePropertyId = ds.getCommonQry().estatePropertyCreate(estateProperty);
//
//            if (estatePropertyId != null) {
//                if (estatePropertyList == null) {
//                    estatePropertyList = new LinkedList<EstateProperty>();
//                }
//                estatePropertyList.add(estateProperty);
//                JsfUtil.addInformationMessage("EstateProperty created sucessfully ");
//            } else if (estatePropertyId == null) {
//                if (ds.getCommonQry().estatePropertyFind(estateProperty.getEstatePropertyId()) != null) {
//                    JsfUtil.addErrorMessage("Property already exist, ");
//                }
//                JsfUtil.addErrorMessage("Failed to Create new EstateProperty");
//            }

        } catch (Exception exp) {
            exp.printStackTrace();
            Logger.getLogger(EstateProperty.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Error: Failed to Create new EstateProperty");
        }

        clearButtonAction();
        return null;
    }

    private double getAppropriateGroundRentBill(EstateProperty ep, int selectedYear) {

        PropertyUsage ussage = estateProperty.getPropertyUsage();

        if (ussage == null) {
            return 0.0;
        }

//        PaymentType paymentType = estateProperty.supposePaymentType();
//        if(paymentType == null)
        if (ussage == PropertyUsage.Commercial || ussage == PropertyUsage.Mix_Use || ussage == PropertyUsage.Institutional || ussage == PropertyUsage.Residential) {
            return getCommMixedUse(selectedYear);
        } else {
            return ds.getCustomQry().getGroundRentBasicCharge(estateProperty, selectedYear);
        }
    }

    private double getCommMixedUse(int selectedYear) {

        double a = ds.getCustomQry().getGroundRentBasicCharge(estateProperty, selectedYear);

        if (selectedYear < 2008) {
            return a;
        }

        double landSize = estateProperty.getPropertyLandSize();

        if (landSize > 0.25) {
            double newRate = (landSize / 0.25) * a;

            return newRate;
        }

        return a;
    }

    public String editButtonAction() {
        try {
            boolean updated = ds.getCommonQry().estatePropertyUpdate(estateProperty);
            if (updated == true) {
                op = ds.getCommonQry().checkLease(estateProperty.getEstatePropertyId());
                if (PropOccupationType.Leasehold == op.getOccupationType()) {
                    DateTime calendar = new DateTime();
                    int year = calendar.getYear();
                    System.out.println("year: " + year);
                    PaymentType pt = null;
                    pt = PaymentType.GROUND_RENT;
                    epl = ds.getCommonQry().getYearsLedger(estateProperty.getEstatePropertyId(), year, DebitCredit.DEBIT, pt);
                    double amtToBeCharge = 0.00;
                    amtToBeCharge = NumberFormattingUtils.formatDecimalNumberTo_2(getAppropriateGroundRentBill(estateProperty, year));
                    epl.setAmountInvolved(amtToBeCharge);
                    ds.getCommonQry().estatePropertyLedgerUpdate(epl);
                }

                JsfUtil.addInformationMessage("EstateProperty updated sucessfully ");
            } else if (updated == false) {
                JsfUtil.addErrorMessage("Failed to Update EstateProperty");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String clearButtonAction() {
        try {
            estateProperty = new EstateProperty();
            rendered = false;
        } catch (Exception exp) {
            Logger.getLogger(EstateProperty.class
                    .getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage(
                    "Error occurred in clearing EstateProperty form ");
        }

        return null;

    }

    public String deleteButtonAction() {
        try {
            for (EstateProperty ep : estatePropertyList) {

                if (ep.getSelected()) {
                    boolean usedBefore = ds.getCustomQry().hasPropertyBeenUsed(ep.getEstatePropertyId());
                    if (usedBefore == true) {
                        JsfUtil.addInformationMessage("Property to be deleted has been used before. It cannot be deleted");
                        return null;
                    }

                    boolean deleted = ds.getCommonQry().estatePropertyDelete(ep, false);

                    if (deleted == true) {
                        estatePropertyList.remove(ep);
                        clearButtonAction();
                    } else {
                        JsfUtil.addErrorMessage("Failed to Delete EstateProperty");
                        return null;
                    }
                } else {
                    JsfUtil.addErrorMessage("Select a row to delete");
                }
            }

        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyController.class
                    .getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage(
                    "Error: Failed to Delete EstateProperty");
        }

        return null;
    }

    public void loadBlockEstate() {
        try {
            selectedEstateBlock = ds.getCommonQry().estateBlockFind(selectedEstateBlock.getEstateBlockId());
            System.out.println("selectedestateblock " + selectedEstateBlock);
            estatePropertyList = new ArrayList<EstateProperty>(ds.getCustomQry().findEstatePropertiesByBlock(selectedEstateBlock.getEstateBlockId()));

            estatePropertiesDataModel = new ListDataModel<EstateProperty>(estatePropertyList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rowSelectDataButtonAction() {
        try {
            estateProperty = (EstateProperty) estatePropertiesDataModel.getRowData();
            rendered = true;
        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyController.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Error occurred in selecting EstateProperty from table ");
        }
    }

    public void triggerDeleteEvent(ValueChangeEvent vce) {
        setInfoRendered((Boolean) vce.getNewValue());
    }

//    @SearchButtonAction(group = "ep")
//    public String estatePropertyDataTableSearchButtonAction() {
//        try {
//            String searchCriteria = estatePropertyDataPanel.getSearchCriteria();
//            String searchText = estatePropertyDataPanel.getSearchText();
//
//            if (searchCriteria.equalsIgnoreCase("propertyLandSize")) {
//                estatePropertyList = ds.getCommonQry().estatePropertyFindByAttribute(searchCriteria, searchText, "NUMBER", true);
//
//            } else {
//                estatePropertyList = ds.getCommonQry().estatePropertyFindByAttribute(searchCriteria, searchText, "STRING", true);
//            }
//
//        } catch (Exception exp) {
//            Logger.getLogger(EstatePropertyController.class.getName()).log(Level.SEVERE, exp.toString(), exp);
//            JsfUtil.addErrorMessage("Error occurred in selecting EstateProperty from table ");
//        }
//
//        return null;
//    }
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public EstateProperty getEstateProperty() {
        return estateProperty;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public EstateProperty getUpEstateProp() {
        return upEstateProp;
    }

    public void setUpEstateProp(EstateProperty upEstateProp) {
        this.upEstateProp = upEstateProp;
    }

    public String getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(String totalProperties) {
        this.totalProperties = totalProperties;
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

    public DataModel<EstateProperty> getEstatePropertiesDataModel() {
        return estatePropertiesDataModel;
    }

    public void setEstatePropertiesDataModel(DataModel<EstateProperty> estatePropertiesDataModel) {
        this.estatePropertiesDataModel = estatePropertiesDataModel;
    }

    public void setEstateProperty(EstateProperty estateProperty) {
        this.estateProperty = estateProperty;
    }

    public List<EstateProperty> getEstatePropertyList() {
        return estatePropertyList;
    }

    public void setEstatePropertyList(List<EstateProperty> estatePropertyList) {
        this.estatePropertyList = estatePropertyList;
    }

    public SelectItem[] getEstateBlockOptions() {
        return estateBlockOptions;
    }

    public void setEstateBlockOptions(SelectItem[] estateBlockOptions) {
        this.estateBlockOptions = estateBlockOptions;
    }

    public boolean isInfoRendered() {
        return infoRendered;
    }

    public void setInfoRendered(boolean infoRendered) {
        this.infoRendered = infoRendered;
    }

    public Estate getSelectedEstate() {
        return selectedEstate;
    }

    public void setSelectedEstate(Estate selectedEstate) {
        this.selectedEstate = selectedEstate;
    }

    public EstateBlock getSelectedEstateBlock() {
        return selectedEstateBlock;
    }

    public void setSelectedEstateBlock(EstateBlock selectedEstateBlock) {
        this.selectedEstateBlock = selectedEstateBlock;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
    // </editor-fold>
}

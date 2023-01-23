/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.advantage.ejb.entities.Bills;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.collection.comparators.StringValueComparator;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.List;
//import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@javax.faces.view.ViewScoped
@Named(value = "propertySelectionController")
public class PropertySelector implements Serializable {

    private static final String BEAN_NAME = "propertySelectionController";
    private List<EstateProperty> estatePropertiesList = null;
    private Estate selectedEstate;
    private EstateBlock selectedEstateBlock;
    private EstateProperty selectedEstateProperty;
    private String estatePropertyNumber;
    private PropOccupationType propertyLoadingConstraint;
    private SelectItem[] selectedEstateBlocksOptions;
    private SelectItem[] selectedEstatePropertiesOptions;
    private Occupancy selectedOccupancy;
    private Occupancy selectedRentedOccupancy;
    private String occupantName;
    private boolean renderOccupantName;
    private Bills bills;

    public PropertySelector() {
    }

    public static PropertySelector getMgedInstance() {
        PropertySelector psc = (PropertySelector) JsfUtil.getManagedBean(BEAN_NAME);

        if (psc != null) {
            return psc;
        }

        throw new RuntimeException("Unable to create your session");
    }

    
    public void estateChangeListener(ValueChangeEvent changeEvent) {
        if (changeEvent.getNewValue() != null) {
            selectedEstate = (Estate) changeEvent.getNewValue();

            selectedEstateBlocksOptions = JsfUtil.createSelectItems(selectedEstate.getEstateBlockList(), true);

            estatePropertiesList = null;
        }
    }

    public String getSelectedEstateId() {
        if (selectedEstate != null) {
            return selectedEstate.getEstateId();
        }

        return null;
    }

    public void estateBlockPropertyChangeListener(ValueChangeEvent changeEvent) {
        if (changeEvent.getNewValue() != null) {
            try {
                
                selectedEstatePropertiesOptions = null;
                estatePropertiesList = null;

//                if (propertyLoadingConstraint == null) {
//                    propertyLoadingConstraint = PropOccupationType.NONE;
//                }

                selectedEstateBlock = (EstateBlock) changeEvent.getNewValue();

                if (selectedEstateBlock == null) {
                    JsfUtil.addErrorMessage("Please select an Estate Block");
                    return;
                }

//                if (propertyLoadingConstraint == PropOccupationType.NONE) {
//                    estatePropertiesList = selectedEstateBlock.getEstatePropertyList();
//                } else if (propertyLoadingConstraint == PropOccupationType.Leasehold) {
                    estatePropertiesList = ds.getCustomQry().findEstatePropertiesByBlock(selectedEstateBlock.getEstateBlockId());
                    //System.out.println("estatePropertiesList " + estatePropertiesList.size());
//                } else if (propertyLoadingConstraint == PropOccupationType.Rental) {
//                    estatePropertiesList = ds.getCustomQry().currentRentedEstateProperties(selectedEstateBlock.getEstateBlockId());
//                }
                StringValueComparator.sort(estatePropertiesList);

                selectedEstatePropertiesOptions
                        = JsfUtil.createSelectItems(estatePropertiesList, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
    public void estateBlockPropertyChangeListener1(ValueChangeEvent changeEvent) {
        if (changeEvent.getNewValue() != null) {
            try {
                
                selectedEstatePropertiesOptions = null;
                estatePropertiesList = null;

//                if (propertyLoadingConstraint == null) {
//                    propertyLoadingConstraint = PropOccupationType.NONE;
//                }

                selectedEstateBlock = (EstateBlock) changeEvent.getNewValue();

                if (selectedEstateBlock == null) {
                    JsfUtil.addErrorMessage("Please select an Estate Block");
                    return;
                }

//                if (propertyLoadingConstraint == PropOccupationType.NONE) {
//                    estatePropertiesList = selectedEstateBlock.getEstatePropertyList();
//                } else if (propertyLoadingConstraint == PropOccupationType.Leasehold) {
                    estatePropertiesList = ds.getCustomQry().findEstatePropertiesByBlock1(selectedEstateBlock.getEstateBlockId());
                    //System.out.println("estatePropertiesList " + estatePropertiesList.size());
//                } else if (propertyLoadingConstraint == PropOccupationType.Rental) {
//                    estatePropertiesList = ds.getCustomQry().currentRentedEstateProperties(selectedEstateBlock.getEstateBlockId());
//                }
                StringValueComparator.sort(estatePropertiesList);

                selectedEstatePropertiesOptions
                        = JsfUtil.createSelectItems(estatePropertiesList, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
    public void estatePropertyListener(ValueChangeEvent vce){
        
        try {
            if(vce.getNewValue() != null){
                selectedEstateProperty = (EstateProperty) vce.getNewValue();
                occupantName = selectedEstateProperty.getCurrentOccupantName();
                System.out.println("occupantname " + occupantName);
            }
        } catch (Exception e) {
        }
    }

    public Occupancy getPropertyOccupationDetails() {
        if (selectedEstateProperty == null) {
            String msg = "Please there is no selected property";
            JsfUtil.addErrorMessage(msg);
            return null;
        }

         return ds.getCustomQry().getOccupantPropertyByPropertyId(selectedEstateProperty);
        //return ds.getCustomQry().currentOccupantPropertyByPropertyId(selectedEstateProperty.getEstatePropertyId());
    }

    public PaymentType getSelectedProprtPaymentType() {
        Occupancy op = getPropertyOccupationDetails();

        if (op != null) {
            return op.getAppropriatePaymentType();
        }

        return null;
    }

    public PropertyOccupant selectedPropertyOccupant() {
        selectedOccupancy = getPropertyOccupationDetails();

        if (selectedOccupancy != null) {
            return selectedOccupancy.getPropertyOccupant();
        }

        return null;
    }

    public PropOccupationType selectedPropertyOccupedAs() {
        selectedOccupancy = getPropertyOccupationDetails();

        if (selectedOccupancy != null) {
            return selectedOccupancy.getOccupationType();
        }

        return null;
    }
    
    public void clearPropertySelection() {
        selectedEstateProperty = null;
    }

    //<editor-fold defaultstate="collapsed" desc="Load Rented Estate Property">
    public void loadRentedEstatePropertyChangeListener(ValueChangeEvent vce) {
        if (vce.getNewValue() != null) {
            try {
                selectedEstatePropertiesOptions = null;
                estatePropertiesList = null;

                if (propertyLoadingConstraint == null) {
                    propertyLoadingConstraint = PropOccupationType.NONE;
                }

                selectedEstateBlock = (EstateBlock) vce.getNewValue();

                if (selectedEstateBlock == null) {
                    JsfUtil.addErrorMessage("Please select an Estate Block");
                    return;
                }

                estatePropertiesList = ds.getCustomQry().currentRentedEstateProperties(selectedEstateBlock.getEstateBlockId());

                StringValueComparator.sort(estatePropertiesList);
//                System.out.println(estatePropertiesList);

                selectedEstatePropertiesOptions
                        = JsfUtil.createSelectItems(estatePropertiesList, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getter And Setter">
    public Estate getSelectedEstate() {
        return selectedEstate;
    }

    public void setSelectedEstate(Estate selectedEstate) {
        this.selectedEstate = selectedEstate;
    }

    public SelectItem[] getSelectedEstateBlocksOptions() {
        return selectedEstateBlocksOptions;
    }

    public void setSelectedEstateBlocksOptions(SelectItem[] selectedEstateBlocksOptions) {
        this.selectedEstateBlocksOptions = selectedEstateBlocksOptions;
    }

    public EstateBlock getSelectedEstateBlock() {
        return selectedEstateBlock;
    }

    public void setSelectedEstateBlock(EstateBlock selectedEstateBlock) {
        this.selectedEstateBlock = selectedEstateBlock;
    }

    public String getEstatePropertyNumber() {
        return estatePropertyNumber;
    }

    public void setEstatePropertyNumber(String estatePropertyNumber) {
        this.estatePropertyNumber = estatePropertyNumber;
    }

    public SelectItem[] getSelectedEstatePropertiesOptions() {
        return selectedEstatePropertiesOptions;
    }

    public void setSelectedEstatePropertiesOptions(SelectItem[] selectedEstatePropertiesOptions) {
        this.selectedEstatePropertiesOptions = selectedEstatePropertiesOptions;
    }

    public EstateProperty getSelectedEstateProperty() {
        return selectedEstateProperty;
    }

    public void setSelectedEstateProperty(EstateProperty selectedEstateProperty) {
        this.selectedEstateProperty = selectedEstateProperty;
    }

    public String getOccupantName() {
        return occupantName; 
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }
    
    public PropOccupationType getPropertyLoadingConstraint() {
        return propertyLoadingConstraint;
    }

    public void setPropertyLoadingConstraint(PropOccupationType propertyLoadingConstraint) {
        this.propertyLoadingConstraint = propertyLoadingConstraint;
    }
    
//</editor-fold>

    public Bills getBills() {
        return bills;
    }

    public void setBills(Bills bills) {
        this.bills = bills;
    }

}

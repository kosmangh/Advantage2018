/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.refactoring.PropertyOccupation;
import com.sabonay.advantage.common.utils.BillDemandNoteMaker;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.entities.Staff;
import com.sabonay.advantage.ejb.models.OccupiedProperty;
import com.sabonay.advantage.web.reports.avReportManager;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@Named(value = "occupantPropertyController")
@SessionScoped
public class OccupantPropertyController implements Serializable {

    private int occupantPropertiesSearchTotal;
    private EstateProperty selectedEstateProperty;
    private Estate selectedEstate;
    private EstateBlock selectedEstateBlock;
    private PropertyOccupant selectedPropertyOccupant = null;
    private String estateBlockName;
    private String estatePropertyNumber;
    private String occupantName;
    private Occupancy occupancy = new Occupancy();
    private DataModel<PropertyOccupant> pptyOccupantDataModel;
    private DataModel<Occupancy> occupancyDataModel;
    private String searchTerm;
    private String searchCriteria;
    private List<PropertyOccupant> propertyOccupantList;
    private List<Occupancy> occupancyList;
    private boolean rendered = false;
    private boolean infoRendered = false;
    private boolean renderPanel = false;

    public OccupantPropertyController() {
    }

    public void reportOccupantProperties() {
//        occupantPropertyList = EJBGateWay.getCustomQry().findOccupantPropertiesById(propertyOccupant.getPropertyOccupantId());

        List<OccupiedProperty> occupiedPropertiesList = PropertyOccupation.transOccupied(occupancyList);

        //avReportManager.resetParameterToDefault();
        avReportManager.getInstance().addParam("reportTitle", "Occupant Information");

        avReportManager.getInstance().showReport(occupiedPropertiesList, avReportManager.PROPTERTY_OCCUPANT_INFORMATION);
    }

    public String saveButtonAction() {
        try {
            selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
            System.out.println("selected estate property " + selectedEstateProperty);

            if (selectedEstateProperty == null) {
                String msg = "Please select Property";
                JsfUtil.addErrorMessageAndRespond(msg);
                return null;
            }

            if (occupancy.getLastDateOfOccupancy() != null) {
                int year = DateTimeUtils.getYearInDate(occupancy.getLastDateOfOccupancy());

//            List<Object[]> ledgersList = ds.getCustomQry().getLegerSummationForPaymenttype(selectedEstateProperty, PaymentType.GROUND_RENT,year);
                BillDemandNoteMaker inter = new BillDemandNoteMaker(selectedEstateProperty, year);
                inter.process();

                if (inter.getDifference() != 0.0) {
                    String msg = "There is arrears of " + inter.getDifference() + " on the property. "
                            + "Occupant cannot leave property with pending balance";
                    JsfUtil.addErrorMessageAndRespond(msg);

                    return null;
                }

            }

            PropertyOccupant po = ds.getCustomQry().estatePropertyCurrentOccupant(selectedEstateProperty);
            if (po != null) {
                JsfUtil.addErrorMessage("Estate Propety is currently occupied by " + po.getOccupantName());
                JsfUtil.getFacesContext().renderResponse();
                return null;

            }

            if (occupancy.getFirstDateOfOccupancy() == null) {
                String msg = "Please select first date of occupancy";
                JsfUtil.addErrorMessage(msg);
                return null;
            }

            if (occupancy.getOccupationType() == null) {
                String msg = "Please select Property Occupied as";
                JsfUtil.addErrorMessage(msg);
                return null;
            }
            occupancy.setEstateProperty(selectedEstateProperty);
            System.out.println("selectedPropertyOccupant is " + selectedPropertyOccupant);
            occupancy.setPropertyOccupant(selectedPropertyOccupant);
            IDCreator.setOccupantPropertyId(occupancy);

            String created = ds.getCommonQry().occupancyCreate(occupancy);
            if (created != null) {
                occupancyList.add(occupancy);
                JsfUtil.addInformationMessage("Occupant Property Information Saved Successfully");


            } else {
                String msg = "Unable to save  Occupant property information";
                JsfUtil.addErrorMessage(msg);
                return null;
            }
        } catch (Exception e) {
        }
        clearButtonAction();
        return null;
    }

    public String editButtonAction() {
        try {
            boolean update = ds.getCommonQry().occupancyUpdate(occupancy);
            if (update) {
                JsfUtil.addInformationMessage("Occupant property information updated successfully");
            } else {
                JsfUtil.addErrorMessage("Unable to update lesse property information");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearButtonAction();
        return null;
    }

    public String clearButtonAction() {

        occupancy = new Occupancy();
        selectedPropertyOccupant = null;
        estateBlockName = "";
        estatePropertyNumber = "";
        occupantName = "";

        rendered = false;

        return null;
    }

    public String deleteButtonAction() {
        try {
            if (occupancy == null) {
                return null;
            }
            if (occupancy.getSelected() == true) {
                boolean occupantHaveLedgerRecords = ds.getCustomQry().doesOccupantHaveRecordsInLedger(selectedPropertyOccupant.getPropertyOccupantId());

                if (occupantHaveLedgerRecords == true) {
                    JsfUtil.addInformationMessage("Occupant have ledger Records. it cannot be deleted");
                    return null;
                }
                System.out.println("occupantHasRecords = " + occupantHaveLedgerRecords);

                boolean deleted = ds.getCommonQry().occupancyDelete(occupancy, true);

                if (deleted == true) {
                    occupancyList.remove(occupancy);
                    clearButtonAction();

                    JsfUtil.addInformationMessage("Occupant Property Deleted Successfully");
                } else {
                    JsfUtil.addErrorMessage("Failed to Delete Occupant Property");
                    return null;
                }
            }

        } catch (Exception exp) {
            Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Error: Failed to Delete Occupant Property");
        }

        return null;
    }

    public void occupantPropertyDataTableRowSelectionAction() {
        try {
            occupancy = (Occupancy) occupancyDataModel.getRowData();
            
            selectedEstate = occupancy.getEstateProperty().getPropertyEstate();
            selectedEstateBlock = occupancy.getEstateProperty().getEstateBlock();
            estatePropertyNumber = occupancy.getEstateProperty().getPropertyNumber();

            selectedEstateProperty = occupancy.getEstateProperty();
            PropertySelector.getMgedInstance().setSelectedEstateProperty(selectedEstateProperty);
            PropertySelector.getMgedInstance().setSelectedEstate(selectedEstate);
            PropertySelector.getMgedInstance().setSelectedEstateBlock(selectedEstateBlock);
//            PropertySelector.getMgedInstance().setEstatePropertyNumber(estatePropertyNumber);
            selectedPropertyOccupant = occupancy.getPropertyOccupant();
            occupantName = selectedPropertyOccupant.toString();

            rendered = true;
            renderPanel = true;

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error Occured in selecting propertyOccupant property");
            e.printStackTrace();
        }
        
    }

    public void occupantPropertyDataTableSearchButttonAction() {
        try {
            occupancyList = ds.getCommonQry().occupancyFindByAttribute(searchCriteria, searchTerm, "STRING", true);
            occupancyDataModel = new ListDataModel<>(occupancyList);
            occupantPropertiesSearchTotal = occupancyList.size();

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error Occured in performing search for occupant  property");
            e.printStackTrace();

        }
    }

    public void propertyOccupantDataTableRowSelectionAction() {
        try {
            selectedPropertyOccupant = (PropertyOccupant) pptyOccupantDataModel.getRowData();
            occupantName = selectedPropertyOccupant.toString();
            System.out.println("selected propertyOccupant is  " + selectedPropertyOccupant);

            occupancyList = ds.getCustomQry().findOccupantPropertiesById(selectedPropertyOccupant.getPropertyOccupantId());
            occupancyDataModel = new ListDataModel<>(occupancyList);
            infoRendered = true;

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error Occured in selecting propertyOccupant");
            e.printStackTrace();
        }
    }

    //@SearchButtonAction(group = "po")
    public void propertyOccupantDataTableSearchButttonAction() {
        try {
            System.out.println("search intiated for property Occupant " + searchTerm + " by: " + searchCriteria);

            propertyOccupantList = ds.getCommonQry().propertyOccupantFindByAttribute(searchCriteria, searchTerm, "STRING", true);
            pptyOccupantDataModel = new ListDataModel<>(propertyOccupantList);

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error Occured in performing search");
            e.printStackTrace();

        }
    }

    // <editor-fold defaultstate="collapsed" desc="Setters and Getters">
    public String getEstateBlockName() {
        return estateBlockName;
    }

    public void setEstateBlockName(String estateBlockName) {
        this.estateBlockName = estateBlockName;
    }

    public String getEstatePropertyNumber() {
        return estatePropertyNumber;
    }

    public void setEstatePropertyNumber(String estatePropertyNumber) {
        this.estatePropertyNumber = estatePropertyNumber;
    }

    public List<PropertyOccupant> getPropertyOccupantList() {
        return propertyOccupantList;
    }

    public void setPropertyOccupantList(List<PropertyOccupant> propertyOccupantList) {
        this.propertyOccupantList = propertyOccupantList;
    }

    public Occupancy getOccupancy() {
        return occupancy;
    }

    public void setOccupantProperty(Occupancy occupancy) {
        this.occupancy = occupancy;
    }

    public List<Occupancy> getOccupantPropertyList() {
        return occupancyList;
    }

    public void setOccupancyList(List<Occupancy> occupancyList) {
        this.occupancyList = occupancyList;
    }

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public PropertyOccupant getSelectedPropertyOccupant() {
        return selectedPropertyOccupant;
    }

    public void setSelectedPropertyOccupant(PropertyOccupant selectedPropertyOccupant) {
        this.selectedPropertyOccupant = selectedPropertyOccupant;
    }

    public DataModel<PropertyOccupant> getPptyOccupantDataModel() {
        return pptyOccupantDataModel;
    }

    public void setPptyOccupantDataModel(DataModel<PropertyOccupant> pptyOccupantDataModel) {
        this.pptyOccupantDataModel = pptyOccupantDataModel;
    }

    public DataModel<Occupancy> getOccupancyDataModel() {
        return occupancyDataModel;
    }

    public void setOccupancyDataModel(DataModel<Occupancy> occupancyDataModel) {
        this.occupancyDataModel = occupancyDataModel;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
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

    public boolean isRenderPanel() {
        return renderPanel;
    }

    public void setRenderPanel(boolean renderPanel) {
        this.renderPanel = renderPanel;
    }
    

    public int getOccupantPropertiesSearchTotal() {
        return occupantPropertiesSearchTotal;
    }

    public void setOccupantPropertiesSearchTotal(int occupantPropertiesSearchTotal) {
        this.occupantPropertiesSearchTotal = occupantPropertiesSearchTotal;
    }
    // </editor-fold>
}

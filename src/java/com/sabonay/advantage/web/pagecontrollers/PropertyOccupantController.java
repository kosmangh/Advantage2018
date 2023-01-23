package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.refactoring.PropertyOccupation;
import com.sabonay.advantage.common.utils.BillDemandNoteMaker;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.BillDemandNote;
import com.sabonay.advantage.ejb.models.OccupiedProperty;
import com.sabonay.advantage.web.reports.avReportManager;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.collection.CollectionUtils;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.component.HtmlDataPanel;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

/**
 * 
 * @author Ritchid
 */

@Named(value = "propertyOccupantController")
@SessionScoped
public class PropertyOccupantController implements Serializable {

    private PropertyOccupant propertyOccupant = new PropertyOccupant();
    private List<PropertyOccupant> propertyOccupantList;
    private List<PropertyOccupant> listOfSelectOccupants;
    private HtmlDataPanel<PropertyOccupant> leseeDataTable;
    private String selectedLesseeProperties;
    private List<Occupancy> occupanciesList = null;
    private String lname, oname, instName;
    private DataModel<PropertyOccupant> lesseeDataModel;
    private boolean rendered = false;
    private boolean isInstitution = false;
    private String searchCriteria, searchTerm;

    public PropertyOccupantController() {
    }

    public void reportLesseeInformation() {
        for(PropertyOccupant po: propertyOccupantList){
            if(po.getSelected()){
               listOfSelectOccupants = new LinkedList<>();
               listOfSelectOccupants.add(po);
               for(PropertyOccupant occupants : listOfSelectOccupants){
                occupanciesList = ds.getCustomQry().findOccupantPropertiesById(occupants.getPropertyOccupantId());

                CollectionUtils.sortStringValue(occupanciesList);
                List<OccupiedProperty> occupiedPropertiesList = PropertyOccupation.transOccupied(occupanciesList);

                //avReportManager.resetParameterToDefault();
                avReportManager.getInstance().addParam("reportTitle", "Occupant Information");

                avReportManager.getInstance().showReport(occupiedPropertiesList, avReportManager.PROPTERTY_OCCUPANT_INFORMATION);
                   
               }
                
            }
        }
        listOfSelectOccupants.clear();
    }

    public String saveButtonAction() {
        if (isIsInstitution() == true) {
            try {
                propertyOccupant.setGender("INSTITUTION");
                IDCreator.setPropertyOccupantId(propertyOccupant);
                String created;
                created = ds.getCommonQry().propertyOccupantCreate(propertyOccupant);
                System.out.println("propertyoccupant savaed is " + created);
                if (created != null) {
//                        propertyOccupantList.add(propertyOccupant);
                    JsfUtil.addInformationMessage("Property Occupant Information Saved Successfully");
                } else {
                    JsfUtil.addErrorMessage("Unable to save property occupant information");
                }
            } catch (Exception e) {
                JsfUtil.addErrorMessage("Unable to save property occupant information");
                e.printStackTrace();
            }
        } else {
            try {

                IDCreator.setPropertyOccupantId(propertyOccupant);
                String created = null;
                created = ds.getCommonQry().propertyOccupantCreate(propertyOccupant);
                if (created != null) {
//                        propertyOccupantList.add(propertyOccupant);
                    JsfUtil.addInformationMessage("Property Occupant Information Saved Successfully");
                } else {
                    String msg = "Unable to save property Occupant information";
                    JsfUtil.addErrorMessage(msg);
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        clearButtonAction();
        return null;
    }

    public String editButtonAction() {
        try {
            if(isIsInstitution() == true){
                propertyOccupant.setGender("INSTITUTION");
            }
            boolean update = ds.getCommonQry().propertyOccupantUpdate(propertyOccupant);
            if (update) {
                JsfUtil.addInformationMessage("PropertyOccupant information updated successfully");
            } else {
                JsfUtil.addErrorMessage("Unable to update lesse information");
                return null;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String clearButtonAction() {
        propertyOccupant = new PropertyOccupant();
        instName = "";
        rendered = false;
        isInstitution = false;
        return null;
    }

    public void reportDemandNotice() {
        try {
            for(PropertyOccupant po :propertyOccupantList){
                if(po.getSelected()){
                    int currentYear = DateTimeUtils.getCurrentYear();

                    List<EstateProperty> list = ds.getCustomQry().findLesseeEstatePropertyById(po.getPropertyOccupantId());

                    List<BillDemandNote> billDemandNotesList = BillDemandNoteMaker.reportGroundRentDemandNotice(list, currentYear);

                    //avReportManager.resetParameterToDefault();
                    avReportManager.getInstance().addParam("yearOfDemandNotice", currentYear);
                    avReportManager.getInstance().showReport(billDemandNotesList, avReportManager.GROUND_RENT_DEMAND_NOTE);
                }else{
                    JsfUtil.addErrorMessage("Please select a Property Occupant !");
                }
            }
        } catch (Exception e) {
        }

    }

    public String deleteButtonAction() {
        try {
            for (PropertyOccupant po : propertyOccupantList) {
                if (po.getSelected()) {
                    boolean occupantHasRecords = ds.getCustomQry().doesOccupantOccupyAProperty(po.getPropertyOccupantId());

                    System.out.println("occupantHasRecords = " + occupantHasRecords);

                    if (occupantHasRecords == true) {
                        JsfUtil.addErrorMessage("Property Occupant occupies a house, occupant cannot be deleted");
                        return null;
                    }
                    boolean deleted = ds.getCommonQry().propertyOccupantDelete(po, false);

                    if (deleted == true) {
                        JsfUtil.addInformationMessage("Property Occupant deleted successully");
                    } else {
                        JsfUtil.addErrorMessage("Unable to delete property occupant");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        propertyOccupantDataTableSearchButttonAction();
        return null;
    }

    public void propertyOccupantDataTableRowSelectionAction() {
        try {
            propertyOccupant = (PropertyOccupant) lesseeDataModel.getRowData();
            
//            if (propertyOccupant.getGender().equalsIgnoreCase("Institution")) {
//                isInstitution = true;
//                setInstName(propertyOccupant.getOccupantName());
//            } else if(propertyOccupant.getGender().equalsIgnoreCase("(NULL)")){
//                isInstitution = false;
//
//            }
            List<Occupancy> assetProperty = ds.getCommonQry().occupancyFindByAttribute("propertyOccupant.propertyOccupantId", propertyOccupant.getPropertyOccupantId(), "STRING", true);

            selectedLesseeProperties = "";
            if (assetProperty != null) {
                for (Occupancy occupancy : assetProperty) {
                    selectedLesseeProperties += occupancy.getEstateProperty() + " | ";
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error Occured in selecting Property Occupant");
            e.printStackTrace();
        }
        rendered = true;
    }

    public void propertyOccupantDataTableSearchButttonAction() {
        try {
            propertyOccupantList = ds.getCommonQry().propertyOccupantFindByAttribute(searchCriteria, searchTerm, "STRING", false);
            lesseeDataModel = new ListDataModel<>(propertyOccupantList);
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error Occured in performing search");
            e.printStackTrace();

        }
    }

    public void toggleInstitution(ValueChangeEvent vce) {
        try {
            setIsInstitution((Boolean) vce.getNewValue());
        } catch (Exception e) {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Setters and Getters">
    public HtmlDataPanel<PropertyOccupant> getLeseeDataTable() {
        return leseeDataTable;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public DataModel<PropertyOccupant> getLesseeDataModel() {
        return lesseeDataModel;
    }

    public void setLesseeDataModel(DataModel<PropertyOccupant> lesseeDataModel) {
        this.lesseeDataModel = lesseeDataModel;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public void setLeseeDataTable(HtmlDataPanel<PropertyOccupant> leseeDataTable) {
        this.leseeDataTable = leseeDataTable;
    }

    public PropertyOccupant getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(PropertyOccupant propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
    }

    public List<PropertyOccupant> getPropertyOccupantList() {
        return propertyOccupantList;
    }

    public void setPropertyOccupantList(List<PropertyOccupant> propertyOccupantList) {
        this.propertyOccupantList = propertyOccupantList;
    }

    public boolean isIsInstitution() {
        return isInstitution;
    }

    public void setIsInstitution(boolean isInstitution) {
        this.isInstitution = isInstitution;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSelectedLesseeProperties() {
        return selectedLesseeProperties;
    }

    public void setSelectedLesseeProperties(String selectedLesseeProperties) {
        this.selectedLesseeProperties = selectedLesseeProperties;
    }
    // </editor-fold>
}

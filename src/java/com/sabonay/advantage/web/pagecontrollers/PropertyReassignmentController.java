/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.web.tablemodel.PropertyOccupantTableModel;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.api.annotations.DataPanel;
import com.sabonay.modules.web.jsf.api.annotations.DataTableModelList;
import com.sabonay.modules.web.jsf.api.annotations.DataTableRowSelectionAction;
import com.sabonay.modules.web.jsf.api.annotations.SearchButtonAction;
import com.sabonay.modules.web.jsf.component.HtmlDataPanel;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author edwin / Ritchid
 */
@ManagedBean(name="propertyReassignmentController")
@SessionScoped
public class PropertyReassignmentController {
    
    private PropertyOccupant realPropertyOccupant = new PropertyOccupant();
    @DataTableModelList(group = "rpo")
    private List<PropertyOccupant> realPropertyOccupantList = new ArrayList<>();
    private PropertyOccupantTableModel realPropertyOccupantTableModel = new PropertyOccupantTableModel();
    @DataPanel(group = "rpo")
    private HtmlDataPanel<PropertyOccupant> realPropertyOccupantDataTable;



    private PropertyOccupant wrongPropertyOccupant = new PropertyOccupant();
    @DataTableModelList(group = "wpo")
    private List<PropertyOccupant> wrongPropertyOccupantList = new ArrayList<>();
    private PropertyOccupantTableModel wrongPropertyOccupantTableModel = new PropertyOccupantTableModel();
    @DataPanel(group = "wpo")
    private HtmlDataPanel<PropertyOccupant> wrongPropertyOccupantDataTable;

    private EstateProperty estateProperty;

    private String realSelectedLesseeProperties;
    private String wrongSelectedLesseeProperties;

    private List<Occupancy> occupanciesList = null;

    public PropertyReassignmentController()
    {
        realPropertyOccupantDataTable = realPropertyOccupantTableModel.getDataPanel();
        realPropertyOccupantDataTable.autoBindAndBuild(PropertyReassignmentController.class, "rpo");


        wrongPropertyOccupantDataTable = wrongPropertyOccupantTableModel.getDataPanel();
        wrongPropertyOccupantDataTable.autoBindAndBuild(PropertyReassignmentController.class, "wpo");
        

    }

    public void doReassignment(){
        if(wrongPropertyOccupant == null || realPropertyOccupant == null) {
            String msg = "Please make sure all parameters are selected";
            JsfUtil.addErrorMessage(msg);
            return;
        }

        List<Occupancy> assetProperty = ds.getCommonQry(). occupancyFindByAttribute("propertyOccupant.propertyOccupantId", wrongPropertyOccupant.getPropertyOccupantId(), "STRING", true);

        for (Occupancy occupancy : assetProperty){
            Occupancy op = new Occupancy();
            op.setEstateProperty(occupancy.getEstateProperty());
            op.setFirstDateOfOccupancy(occupancy.getFirstDateOfOccupancy());
            op.setOccupationType(occupancy.getOccupationType());
            op.setPropertyOccupant(realPropertyOccupant);

            IDCreator.setOccupantPropertyId(op);

            ds.getCommonQry().occupancyUpdate(op);

        }

        for (Occupancy occupancy : assetProperty)
        {
            ds.getCommonQry().occupancyDelete(occupancy, true);
        }

        List<EstatePropertyLedger> ledgers = wrongPropertyOccupant.getEstatePropertyLedgerList();

        for (EstatePropertyLedger led : ledgers)
        {
            led.setPropertyOccupant(realPropertyOccupant);

            ds.getCommonQry().estatePropertyLedgerUpdate(led);
        }

        ds.getCommonQry().propertyOccupantDelete(wrongPropertyOccupant, true);
    }


    public void assingPropertyAlone() {

        estateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();

        if (estateProperty == null || realPropertyOccupant == null)
        {
            String msg = "Please make sure all parameters are selected";
            JsfUtil.addErrorMessage(msg);
            return;
        }


        Occupancy initOccupantProperty = estateProperty.getOccupancy();

        Occupancy op = new Occupancy();
        op.setEstateProperty(estateProperty);
        op.setFirstDateOfOccupancy(initOccupantProperty.getFirstDateOfOccupancy());
        op.setOccupationType(initOccupantProperty.getOccupationType());
        op.setPropertyOccupant(realPropertyOccupant);

        IDCreator.setOccupantPropertyId(op);

        ds.getCommonQry().occupancyUpdate(op);
        ds.getCommonQry().occupancyDelete(initOccupantProperty, true);


        List<EstatePropertyLedger> ledgers = estateProperty.getEstatePropertyLedgerList();

        for (EstatePropertyLedger led : ledgers) {
            led.setPropertyOccupant(realPropertyOccupant);
            ds.getCommonQry().estatePropertyLedgerUpdate(led);
        }
    }


    public String saveEditButtonAction()
    {
       return null;
    }

    public String clearButtonAction() {
        realPropertyOccupant = new PropertyOccupant();

        return null;
    }

    public String deleteButtonAction()
    {


        return null;
    }

    @DataTableRowSelectionAction(group = "rpo")
    public void realPropertyOccupantDataTableRowSelectionAction() {
        try {
            realPropertyOccupant = realPropertyOccupantDataTable.getRowData();
            System.out.println("selected real = " + realPropertyOccupant);

            List<Occupancy> assetProperty = ds.getCommonQry().occupancyFindByAttribute("propertyOccupant.propertyOccupantId", realPropertyOccupant.getPropertyOccupantId(), "STRING", true);
            realSelectedLesseeProperties = "";

            if(assetProperty != null) {
                for (Occupancy occupancy : assetProperty)
                {
                    realSelectedLesseeProperties += occupancy.getEstateProperty().getEstatePropertyId() + " | ";
                }
            }
        } catch (Exception e)
        {
            JsfUtil.addErrorMessage("Error Occured in selecting Property Occupant");
            e.printStackTrace();
        }
    }

    @SearchButtonAction(group = "rpo")
    public void realPropertyOccupantDataTableSearchButttonAction()
    {
        try
        {
            String searchTerm = realPropertyOccupantDataTable.getSearchText();
            String searchCriteria = realPropertyOccupantDataTable.getSearchCriteria();

            System.out.println("search intiated for propertyOccupant " + searchTerm + " by: " + searchCriteria);

        realPropertyOccupantList = ds.getCommonQry().propertyOccupantFindByAttribute(searchCriteria, searchTerm, "STRING", true);
        } catch (Exception e)
        {
            JsfUtil.addErrorMessage("Error Occured in performing search");
            e.printStackTrace();
            
        }
    }



    @DataTableRowSelectionAction(group = "wpo")
    public void wrongPropertyOccupantDataTableRowSelectionAction()
    {
        try
        {
            wrongPropertyOccupant = wrongPropertyOccupantDataTable.getRowData();

            System.out.println("wrong selected ........... " + wrongPropertyOccupant);

            List<Occupancy> assetProperty = ds.getCommonQry().occupancyFindByAttribute("propertyOccupant.propertyOccupantId", wrongPropertyOccupant.getPropertyOccupantId(), "STRING", true);
            

            wrongSelectedLesseeProperties = "";

            if(assetProperty != null)
            {
                for (Occupancy occupancy : assetProperty)
                    {
                        wrongSelectedLesseeProperties += occupancy.getEstateProperty().getEstatePropertyId() + " | ";
                    }
                
            }


        } catch (Exception e)
        {
            JsfUtil.addErrorMessage("Error Occured in selecting Property Occupant");
            e.printStackTrace();
        }
    }

    @SearchButtonAction(group = "wpo")
    public void wrongPropertyOccupantDataTableSearchButttonAction()
    {
        try
        {
            String searchTerm = wrongPropertyOccupantDataTable.getSearchText();
            String searchCriteria = wrongPropertyOccupantDataTable.getSearchCriteria();

            System.out.println("search intiated for propertyOccupant " + searchTerm + " by: " + searchCriteria);

        wrongPropertyOccupantList = ds.getCommonQry().propertyOccupantFindByAttribute(searchCriteria, searchTerm, "STRING", true);
        } catch (Exception e)
        {
            JsfUtil.addErrorMessage("Error Occured in performing search");
            e.printStackTrace();

        }
    }

    // <editor-fold defaultstate="collapsed" desc="Setters and Getters">




    public List<Occupancy> getOccupanciesList()
    {
        return occupanciesList;
    }

    public void setOccupanciesList(List<Occupancy> occupanciesList)
    {
        this.occupanciesList = occupanciesList;
    }

    public PropertyOccupant getRealPropertyOccupant()
    {
        return realPropertyOccupant;
    }

    public void setRealPropertyOccupant(PropertyOccupant realPropertyOccupant)
    {
        this.realPropertyOccupant = realPropertyOccupant;
    }

    public HtmlDataPanel<PropertyOccupant> getRealPropertyOccupantDataTable()
    {
        return realPropertyOccupantDataTable;
    }

    public void setRealPropertyOccupantDataTable(HtmlDataPanel<PropertyOccupant> realPropertyOccupantDataTable)
    {
        this.realPropertyOccupantDataTable = realPropertyOccupantDataTable;
    }

    public List<PropertyOccupant> getRealPropertyOccupantList()
    {
        return realPropertyOccupantList;
    }

    public void setRealPropertyOccupantList(List<PropertyOccupant> realPropertyOccupantList)
    {
        this.realPropertyOccupantList = realPropertyOccupantList;
    }

    public PropertyOccupant getWrongPropertyOccupant()
    {
        return wrongPropertyOccupant;
    }

    public void setWrongPropertyOccupant(PropertyOccupant wrongPropertyOccupant)
    {
        this.wrongPropertyOccupant = wrongPropertyOccupant;
    }

    public HtmlDataPanel<PropertyOccupant> getWrongPropertyOccupantDataTable()
    {
        return wrongPropertyOccupantDataTable;
    }

    public void setWrongPropertyOccupantDataTable(HtmlDataPanel<PropertyOccupant> wrongPropertyOccupantDataTable)
    {
        this.wrongPropertyOccupantDataTable = wrongPropertyOccupantDataTable;
    }

    public List<PropertyOccupant> getWrongPropertyOccupantList()
    {
        return wrongPropertyOccupantList;
    }

    public void setWrongPropertyOccupantList(List<PropertyOccupant> wrongPropertyOccupantList)
    {
        this.wrongPropertyOccupantList = wrongPropertyOccupantList;
    }

    public String getRealSelectedLesseeProperties()
    {
        return realSelectedLesseeProperties;
    }

    public void setRealSelectedLesseeProperties(String realSelectedLesseeProperties)
    {
        this.realSelectedLesseeProperties = realSelectedLesseeProperties;
    }

    public String getWrongSelectedLesseeProperties()
    {
        return wrongSelectedLesseeProperties;
    }

    public void setWrongSelectedLesseeProperties(String wrongSelectedLesseeProperties)
    {
        this.wrongSelectedLesseeProperties = wrongSelectedLesseeProperties;
    }


    // </editor-fold>

    public EstateProperty getEstateProperty()
    {
        return estateProperty;
    }

    public void setEstateProperty(EstateProperty estateProperty)
    {
        this.estateProperty = estateProperty;
    }

}

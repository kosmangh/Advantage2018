/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.utils.PropertiesUtils;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.models.EstatePropertyOccupantDetail;
import com.sabonay.advantage.web.tablemodel.RecordEntryDetailTableModel;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.collection.comparators.StringValueComparator;
import com.sabonay.modules.web.jsf.api.annotations.DataPanel;
import com.sabonay.modules.web.jsf.api.annotations.DataTableModelList;
import com.sabonay.modules.web.jsf.component.HtmlDataPanel;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@Named(value = "estateCrossCheckController")
@SessionScoped
public class EntryCrossCheckController implements Serializable
{

    @DataTableModelList(group="p")
    private ListDataModel<EstatePropertyOccupantDetail> propertyDetailsList = new ListDataModel<>();;
    
    @DataPanel(group="p")
    private HtmlDataPanel<EstatePropertyOccupantDetail> estatePropertyDetailDataPanel;

    private RecordEntryDetailTableModel detailTableModel = new RecordEntryDetailTableModel();

    private List<EstateProperty> propertyList = null;

    public EntryCrossCheckController() {
        estatePropertyDetailDataPanel = detailTableModel.getDataPanel();
        estatePropertyDetailDataPanel.setInitType(HtmlDataPanel.Init.NO_SEARCH_SELECTION);
        estatePropertyDetailDataPanel.autoBindAndBuild(EntryCrossCheckController.class, "p");
    }

    public void loadEstateBlockProperties() {
        
         List<EstatePropertyOccupantDetail> list = new LinkedList<>();


//         if(PropertySelector.getMgedInstance().getSelectedEstateBlock() == null)
//         {
//             return;
//         }

//         propertyList = PropertySelector.getMgedInstance().getSelectedEstateBlock().getEstatePropertyList();


//        propertyList = PropertySelector.getMgedInstance()
//                .getSelectedEstate().getEstatePropertiesList();
        
//        System.out.println(propertyList);

        propertyList = ds.getCustomQry().unverifiedRecords(PropertySelector.getMgedInstance().getSelectedEstateId());


        if(propertyList == null)
        {
            System.out.println("empty verified found ...... ");
            return;
        }

//        if(true)
//            return;


        for (EstateProperty estateProperty : propertyList)
        {
            try
            {
                if(estateProperty == null)
                continue;

            EstatePropertyOccupantDetail epod = PropertiesUtils.getPropertyDetail(estateProperty.getEstatePropertyId());

//            propertyDetailsList.add(epod);

            if(epod.getVerified() == null)
            {
                list.add(epod);
            }

            else if(!epod.getVerified())
                list.add(epod);
            } catch (Exception e)
            {
                Logger.getLogger(EntryCrossCheckController.class.getName()).log(Level.SEVERE, e.toString());
            }
            
        }

            Collections.sort(list, StringValueComparator.instance());

            propertyDetailsList = new ListDataModel<EstatePropertyOccupantDetail>(list);
            
    }


    public void updateAll()
    {
        for (EstatePropertyOccupantDetail estatePropertyOccupantDetail : propertyDetailsList)
        {
            String estatePropertyId = estatePropertyOccupantDetail.getEstatePropertyId();

            Occupancy occupancy = ds.getCustomQry().currentOccupancyByPropertyId(estatePropertyId);

            if(occupancy == null)
            {
                System.out.println("null occupant property is found ...");
                continue;
            }

            if(occupancy.isPropertyOccupiesAsRental())
            {
                continue;
            }

            System.out.println("update ... " + estatePropertyOccupantDetail);

            estatePropertyOccupantDetail.updateLeesse();
            estatePropertyOccupantDetail.updateArreasRecord();
        }

        loadEstateBlockProperties();
    }

    // <editor-fold defaultstate="collapsed" desc="Setters and Getters">

    

    public RecordEntryDetailTableModel getDetailTableModel()
    {
        return detailTableModel;
    }

    public void setDetailTableModel(RecordEntryDetailTableModel detailTableModel)
    {
        this.detailTableModel = detailTableModel;
    }

    public HtmlDataPanel<EstatePropertyOccupantDetail> getEstatePropertyDetailDataPanel()
    {
        return estatePropertyDetailDataPanel;
    }

    public void setEstatePropertyDetailDataPanel(HtmlDataPanel<EstatePropertyOccupantDetail> estatePropertyDetailDataPanel)
    {
        this.estatePropertyDetailDataPanel = estatePropertyDetailDataPanel;
    }

    public ListDataModel<EstatePropertyOccupantDetail> getPropertyDetailsList()
    {
        return propertyDetailsList;
    }

    public void setPropertyDetailsList(ListDataModel<EstatePropertyOccupantDetail> propertyDetailsList)
    {
        this.propertyDetailsList = propertyDetailsList;
    }



    // </editor-fold>

}

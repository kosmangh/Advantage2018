/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.utils.GroundRentBiller;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Named;

/**
 *
 * @author Edwin
 */
@SessionScoped
@Named(value = "groundRentArreasController")
public class GroundRentArreasController implements Serializable {

    private int selectedYear = DateTimeUtils.getCurrentYear();
    private EstateProperty selectedEstateProperty;
    private DataModel<EstatePropertyLedger> estatePropertyLedgerDataModel;
    private List<EstatePropertyLedger> searchedEstatePropertyLedgerList;

    public GroundRentArreasController() {
    }

    public void chargeGroundRent() {
        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        if (selectedEstateProperty == null) {
            JsfUtil.addErrorMessage("Please Select estate Property");
            return;
        }

//        if(selectedYear >= 2010)
//        {
//            JsfUtil.addErrorMessage("Please you cannot charge ground rent below above 2009");
//            return;
//        }

        GroundRentBiller groundRentBiller = new GroundRentBiller();
        groundRentBiller.setEstateProperty(selectedEstateProperty);
        groundRentBiller.setSelectedYear(selectedYear);

        if (groundRentBiller.chargeGroundRentArreas()) {
            JsfUtil.addInformationMessage("Billing Process Successfully");
        } else {
            JsfUtil.addErrorMessage(groundRentBiller.getProcessingMsg());
        }

        prepareRequestedLedger();

    }

    public void removeGroundRentCharge() {
        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        if (selectedEstateProperty == null) {
            JsfUtil.addErrorMessage("Please Select estate Property");
            return;
        }

//        if(selectedYear >= 2010)
//        {
//            JsfUtil.addErrorMessage("Please you cannot remove ground rent below above 2009");
//            return;
//        }
        GroundRentBiller groundRentBiller = new GroundRentBiller();
        groundRentBiller.setEstateProperty(selectedEstateProperty);
        groundRentBiller.setSelectedYear(selectedYear);

        if (groundRentBiller.removeGroundRent()) {
            JsfUtil.addInformationMessage(groundRentBiller.getProcessingMsg());
        } else {
            JsfUtil.addErrorMessage(groundRentBiller.getProcessingMsg());
        }

        prepareRequestedLedger();
    }

    public void prepareRequestedLedger() {

        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();

        if (selectedEstateProperty == null) {
            String msg = "Please Select an Estate Property Before Performing Search";
            JsfUtil.addInformationMessage(msg);
            return;
        }

        System.out.println("about to seearch ...");

        searchedEstatePropertyLedgerList = ds.getCustomQry()
                .loadLedgerEntriesForPropertyOfCurrentOccupant(selectedEstateProperty, selectedYear);

    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public List<EstatePropertyLedger> getSearchedEstatePropertyLedgerList() {
        return searchedEstatePropertyLedgerList;
    }

    public void setSearchedEstatePropertyLedgerList(List<EstatePropertyLedger> searchedEstatePropertyLedgerList) {
        this.searchedEstatePropertyLedgerList = searchedEstatePropertyLedgerList;
    }
}

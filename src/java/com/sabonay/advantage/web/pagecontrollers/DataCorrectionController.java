/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.OccupantType;
import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.common.utils.GroundRentBiller;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.ArreasRecord;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author edwin / Ritchid
 */
@Named(value = "dataCorrectionController")
@SessionScoped
public class DataCorrectionController implements Serializable {

    private String occupantName;
    private String blockNumber;
    private String propertyNumber;
    private String estateId;

    private EstateProperty estateProperty;
    PropertyOccupant propertyOccupant;// = new PropertyOccupant();
    private int arreasYear;
    private String estateBlockId;
    private PropertyUsage propertyUsage;

    String msg = "";
    private boolean rendered = false;

    public DataCorrectionController() {
    }

    public void forceUpdateAllRecords() {
        estateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();

        if (estateProperty == null) {
            msg = "Please select an estate property";
            JsfUtil.addErrorMessage(msg);
            return;
        }

        boolean occupantSave = saveOccupant();

        if (occupantSave == false) {
            return;
        }

        boolean occpantprops = updateOccunapncy();
        if (occpantprops == false) {
            return;
        }

//        updateArreasChange();
        makeLedgerChanges();
        updateEstateProperty();

//        rebillGroundRent();
        msg = "Changes Completed";
        JsfUtil.addInformationMessage(msg);

        clear();
        rendered = false;
    }

    private boolean saveOccupant() {
        propertyOccupant = new PropertyOccupant();
        propertyOccupant.setOccupantName(occupantName);
        propertyOccupant.setOccupantType(OccupantType.Lessee);

        IDCreator.setPropertyOccupantId(propertyOccupant);

        String created = ds.getCommonQry().propertyOccupantCreate(propertyOccupant);

        if (created == null) {
            msg = "Unable to Save Property Occupant " + occupantName;
            JsfUtil.addErrorMessage(msg);
            return false;
        }

        msg = "Occupant saved " + occupantName;
        JsfUtil.addInformationMessage(msg);
        Logger.getLogger(DataCorrectionController.class.getName()).log(Level.SEVERE, msg);

        return true;

    }
    
    private boolean updateEstateProperty(){
        try {
//            boolean estateExists = ds.getCustomQry().hasPropertyBeenAssigned(estateProperty.getPropertyNumber(), estateProperty.getEstateBlock().getBlockName());
//            if(false == estateExists){
//                JsfUtil.addErrorMessage("Block and property number already exists");
//            }
            EstateProperty ep = ds.getCommonQry().estatePropertyFind(estateProperty.getEstatePropertyId());
            //estateProperty = new EstateProperty();
            ep.setPropertyNumber(propertyNumber);
            ep.setPropertyUsage(propertyUsage);
//            EstateBlock findBlock = ds.getCommonQry().estateBlockFind(estateBlockId);
//            
//            findBlock.setBlockName(blockNumber);
//            ep.setEstateBlock(findBlock);
            ds.getCommonQry().estatePropertyUpdate(ep);
            return true;
        } catch (Exception e) {
            return false;
        }
        
    }
    
    private boolean updateEstateBlock(){
        try {
            //we find the estate property selected and navigate to the block it belongs to
            estateBlockId = estateProperty.getEstateBlock().getEstateBlockId();
            estateId = estateProperty.getEstateBlock().getEstate().getEstateId();
            //we check if block exists for that estate
            boolean blockExist = ds.getCustomQry().doesEstateBlockExist(estateId, blockNumber);
            if(blockExist == true){
                // we create a new block for that estate an
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean updateOccunapncy() {
        Occupancy occupancy = new Occupancy();

        occupancy.setEstateProperty(estateProperty);
        occupancy.setPropertyOccupant(propertyOccupant);

        occupancy.setOccupationType(PropOccupationType.Leasehold);

        occupancy.setFirstDateOfOccupancy(new Date());

        List<Occupancy> list = ds.getCommonQry().occupancyFindByAttribute("estateProperty.estatePropertyId", estateProperty.getEstatePropertyId(), "STRING", true);

        for (Occupancy prosps : list) {
            ds.getCommonQry().occupancyDelete(prosps, true);
        }

        IDCreator.setOccupantPropertyId(occupancy);

        boolean updated = ds.getCommonQry().occupancyUpdate(occupancy);

        if (updated) {
            msg = "Occupant assigned to property";
            JsfUtil.addInformationMessage(msg);

            return true;
        }

        return false;

    }

    public void loadPropertyDetails() {
        estateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        if (estateProperty == null) {
            msg = "Please select an estate property";
            JsfUtil.addErrorMessage(msg);
            return;
        }
        setBlockNumber(estateProperty.getEstateBlock().getBlockName());
        setPropertyNumber(estateProperty.getPropertyNumber());
        setOccupantName(estateProperty.getCurrentOccupantName());
        setEstateBlockId(estateProperty.getEstateBlock().getEstateBlockId());
        setPropertyUsage(estateProperty.getPropertyUsage());
        rendered = true;
    }

    private void updateArreasChange() {
        ArreasRecord arreasRecord = new ArreasRecord();

        arreasRecord.setEstateProperty(estateProperty);
        arreasRecord.setYearArreasStartedFrom(arreasYear);
        arreasRecord.setVerified(Boolean.TRUE);
        IDCreator.setArreasRecordId(arreasRecord);

        ds.getCommonQry().arreasRecordUpdate(arreasRecord);
    }

    private boolean makeLedgerChanges() {
        System.out.println("about to make ledger changes");
        List<EstatePropertyLedger> estatePropertyLedgersList
                = ds.getCustomQry().loadAllLedgerEntriesForProperty(estateProperty, DateTimeUtils.getCurrentYear());

        System.out.println("records found by searching .... " + estatePropertyLedgersList);

        for (EstatePropertyLedger estatePropertyLedger : estatePropertyLedgersList) {

            estatePropertyLedger.setPropertyOccupant(propertyOccupant);
            estatePropertyLedger.setPayeeName(occupantName);
            ds.getCommonQry().estatePropertyLedgerUpdate(estatePropertyLedger);

        }

        return false;

    }

    public void rebillGroundRent() {

        if (arreasYear == 0) {
            return;
        }

        for (int startYear = arreasYear; startYear < DateTimeUtils.getCurrentYear(); startYear++) {
            GroundRentBiller biller = new GroundRentBiller();
            biller.setEstateProperty(estateProperty);
            biller.setSelectedYear(startYear);
            biller.billPropertyGroud();

        }
    }

    public void clear() {
        estateProperty = null;
        occupantName = null;
        arreasYear = 0;

        PropertySelector.getMgedInstance().clearPropertySelection();
    }

    public int getArreasYear() {
        return arreasYear;
    }

    public void setArreasYear(int arreasYear) {
        this.arreasYear = arreasYear;
    }

    public EstateProperty getEstateProperty() {
        return estateProperty;
    }

    public void setEstateProperty(EstateProperty estateProperty) {
        this.estateProperty = estateProperty;
    }

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PropertyOccupant getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(PropertyOccupant propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getEstateBlockId() {
        return estateBlockId;
    }

    public void setEstateBlockId(String estateBlockId) {
        this.estateBlockId = estateBlockId;
    }

    public PropertyUsage getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(PropertyUsage propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
    

}

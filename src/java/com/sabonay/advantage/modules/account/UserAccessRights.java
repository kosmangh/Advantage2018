/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.modules.account;

import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Edwin
 */
@Named(value = "uar")
@SessionScoped
public class UserAccessRights implements Serializable {

    private static final String MANAGED_BEAN_NAME = "uar";
    private String userAccessCode = "";
    private boolean propLedger_Read = false;
    private boolean bill_status_report = false;
    private boolean propLedger_Write = false;
    private boolean propLedger_PrintDemandNote = false;
    private boolean propLedger_PrintLedgerStatement = false;
    private boolean billPayment_Read = false;
    private boolean billPayment_Write = false;
    private boolean occupant_Read = false;
    private boolean occupant_Write = false;
    private boolean estate_Read = false;
    private boolean estate_Write = false;
    private boolean property_Read = false;
    private boolean property_Write = false;
    private boolean block_Read = false;
    private boolean block_Write = false;
    private boolean groundRent_Read = false;
    private boolean groundRent_Write = false;
    private boolean groundRent_BillGroundRent = false;
    private boolean occupProp_Read = false;
    private boolean occupProp_Write = false;
    private boolean genRpt_Read = false;
    private boolean genRpt_Write = false;
    private boolean rck_Read = false;
    private boolean rck_Write = false;
    private boolean staff_Read = false;
    private boolean staff_Write = false;
    private boolean uac_Read = false;
    private boolean uac_Write = false;
    private boolean rentBilling_Read = false;
    private boolean rentBilling_Write = false;
    private boolean grndRentArreas_Read = false;
    private boolean grndRentArreas_Write = false;
    
    //<editor-fold defaultstate="collapsed" desc="Variables that control whether pages are displayed and made editable">
    private boolean estate_info_edit = false;
    private boolean estate_blk_edit = false;
    private boolean occupant_pty_edit = false;
    private boolean pty_occupant_edit = false;
    private boolean grnd_rent_edit = false;
    private boolean rental_bill_edit = false;
    private boolean estate_ptyledger_edit = false;
    private boolean bill_pay_edit = false;
    private boolean grndrent_arrears = false;
    private boolean gen_report_edit = false;

    public boolean isGrndRentArreas_Read() {
        return grndRentArreas_Read;
    }

    public void setGrndRentArreas_Read(boolean grndRentArreas_Read) {
        this.grndRentArreas_Read = grndRentArreas_Read;
    }

    public boolean isGrndRentArreas_Write() {
        return grndRentArreas_Write;
    }

    public void setGrndRentArreas_Write(boolean grndRentArreas_Write) {
        this.grndRentArreas_Write = grndRentArreas_Write;
    }

    public boolean isEstate_info_edit() {
        return estate_info_edit;
    }

    public void setEstate_info_edit(boolean estate_info_edit) {
        this.estate_info_edit = estate_info_edit;
    }

    public boolean isEstate_blk_edit() {
        return estate_blk_edit;
    }

    public void setEstate_blk_edit(boolean estate_blk_edit) {
        this.estate_blk_edit = estate_blk_edit;
    }

    public boolean isOccupant_pty_edit() {
        return occupant_pty_edit;
    }

    public void setOccupant_pty_edit(boolean occupant_pty_edit) {
        this.occupant_pty_edit = occupant_pty_edit;
    }

    public boolean isPty_occupant_edit() {
        return pty_occupant_edit;
    }

    public void setPty_occupant_edit(boolean pty_occupant_edit) {
        this.pty_occupant_edit = pty_occupant_edit;
    }

    public boolean isGrnd_rent_edit() {
        return grnd_rent_edit;
    }

    public void setGrnd_rent_edit(boolean grnd_rent_edit) {
        this.grnd_rent_edit = grnd_rent_edit;
    }

    public boolean isRental_bill_edit() {
        return rental_bill_edit;
    }

    public void setRental_bill_edit(boolean rental_bill_edit) {
        this.rental_bill_edit = rental_bill_edit;
    }

    public boolean isEstate_ptyledger_edit() {
        return estate_ptyledger_edit;
    }

    public void setEstate_ptyledger_edit(boolean estate_ptyledger_edit) {
        this.estate_ptyledger_edit = estate_ptyledger_edit;
    }

    public boolean isBill_pay_edit() {
        return bill_pay_edit;
    }

    public void setBill_pay_edit(boolean bill_pay_edit) {
        this.bill_pay_edit = bill_pay_edit;
    }

    public boolean isGrndrent_arrears() {
        return grndrent_arrears;
    }

    public void setGrndrent_arrears(boolean grndrent_arrears) {
        this.grndrent_arrears = grndrent_arrears;
    }

    public boolean isGen_report_edit() {
        return gen_report_edit;
    }

    public void setGen_report_edit(boolean gen_report_edit) {
        this.gen_report_edit = gen_report_edit;
    }
    
    private void denyAllpages(){
        
    }
    
    //</editor-fold>

    public UserAccessRights() {
    }

    public static UserAccessRights getManagedInstance() {
        UserAccessRights userAccessRights = JsfUtil.getManagedBean(MANAGED_BEAN_NAME);

        if (userAccessRights != null) {
            return userAccessRights;
        }

        throw new RuntimeException("Unable create your user right session");
    }

    public void setUserAccessCode(String userAccessCode) {
        if(userAccessCode == null){
            userAccessCode = "";
        }
        this.userAccessCode = userAccessCode;
    }

   
    public void checkRights() {
        propLedger_Read = userAccessCode.contains("propLedger#Read");
         bill_status_report = userAccessCode.contains("billStatusReport#Read");
        propLedger_Write = userAccessCode.contains("propLedger#Write");
        propLedger_PrintDemandNote = userAccessCode.contains("propLedger#PrintDemandNote");
        propLedger_PrintLedgerStatement = userAccessCode.contains("propLedger#PrintLedgerStatement");
        billPayment_Read = userAccessCode.contains("billPayment#Read");
        billPayment_Write = userAccessCode.contains("billPayment#Write");
        occupant_Read = userAccessCode.contains("occupant#Read");
        occupant_Write = userAccessCode.contains("occupant#Write");
        estate_Read = userAccessCode.contains("estate#Read");
        estate_Write = userAccessCode.contains("estate#Write");
        property_Read = userAccessCode.contains("property#Read");
        property_Write = userAccessCode.contains("property#Write");
        block_Read = userAccessCode.contains("block#Read");
        block_Write = userAccessCode.contains("block#Write");
        groundRent_Read = userAccessCode.contains("groundRent#Read");
        groundRent_Write = userAccessCode.contains("groundRent#Write");
        groundRent_BillGroundRent = userAccessCode.contains("groundRent#BillGroundRent");
        occupProp_Read = userAccessCode.contains("occupProp#Read");
        occupProp_Write = userAccessCode.contains("occupProp#Write");
        genRpt_Read = userAccessCode.contains("genRpt#Read");
        genRpt_Write = userAccessCode.contains("genRpt#Write");
        rck_Read = userAccessCode.contains("rck#Read");
        rck_Write = userAccessCode.contains("rck#Write");
        staff_Read = userAccessCode.contains("staff#Read");
        staff_Write = userAccessCode.contains("staff#Write");
        uac_Read = userAccessCode.contains("uac#Read");
        uac_Write = userAccessCode.contains("uac#Write");
        rentBilling_Read = userAccessCode.contains("rentBilling#Read");
        rentBilling_Write = userAccessCode.contains("rentBilling#Write");
        grndRentArreas_Read = userAccessCode.contains("grndRentArreas#Read");
        grndRentArreas_Write = userAccessCode.contains("grndRentArreas#Write");
    }

    public void grantFullAccess() {
        bill_status_report = true;
        propLedger_Read = true;
        propLedger_Write = true;
        propLedger_PrintDemandNote = true;
        propLedger_PrintLedgerStatement = true;
        billPayment_Read = true;
        billPayment_Write = true;
        occupant_Read = true;
        occupant_Write = true;
        estate_Read = true;
        estate_Write = true;
        property_Read = true;
        property_Write = true;
        block_Read = true;
        block_Write = true;
        groundRent_Read = true;
        groundRent_Write = true;
        groundRent_BillGroundRent = true;
        occupProp_Read = true;
        occupProp_Write = true;
        genRpt_Read = true;
        genRpt_Write = true;
        rck_Read = true;
        rck_Write = true;
        staff_Read = true;
        staff_Write = true;
        uac_Read = true;
        uac_Write = true;
        rentBilling_Read = true;
        rentBilling_Write = true;
        grndRentArreas_Read = true;
        grndRentArreas_Write = true;

    }
    
    public void denyFullAccess(){
        bill_status_report=false;
        propLedger_Read = false;
        propLedger_Write = false;
        propLedger_PrintDemandNote = false;
        propLedger_PrintLedgerStatement = false;
        billPayment_Read = false;
        billPayment_Write = false;
        occupant_Read = false;
        occupant_Write = false;
        estate_Read = false;
        estate_Write = false;
        property_Read = false;
        property_Write = false;
        block_Read = false;
        block_Write = false;
        groundRent_Read = false;
        groundRent_Write = false;
        groundRent_BillGroundRent = false;
        occupProp_Read = false;
        occupProp_Write = false;
        genRpt_Read = false;
        genRpt_Write = false;
        rck_Read = false;
        rck_Write = false;
        staff_Read = false;
        staff_Write = false;
        uac_Read = false;
        uac_Write = false;
        rentBilling_Read = false;
        rentBilling_Write = false;
        grndRentArreas_Read = false;
        grndRentArreas_Write = false;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public boolean isBillPayment_Read() {
        return billPayment_Read;
    }

    public void setBillPayment_Read(boolean billPayment_Read) {
        this.billPayment_Read = billPayment_Read;
    }

    public boolean isBillPayment_Write() {
        return billPayment_Write;
    }

    public void setBillPayment_Write(boolean billPayment_Write) {
        this.billPayment_Write = billPayment_Write;
    }

    public boolean isBlock_Read() {
        return block_Read;
    }

    public void setBlock_Read(boolean block_Read) {
        this.block_Read = block_Read;
    }

    public boolean isBlock_Write() {
        return block_Write;
    }

    public void setBlock_Write(boolean block_Write) {
        this.block_Write = block_Write;
    }

    public boolean isEstate_Read() {
        return estate_Read;
    }

    public void setEstate_Read(boolean estate_Read) {
        this.estate_Read = estate_Read;
    }

    public boolean isEstate_Write() {
        return estate_Write;
    }

    public void setEstate_Write(boolean estate_Write) {
        this.estate_Write = estate_Write;
    }

    public boolean isGenRpt_Read() {
        return genRpt_Read;
    }

    public void setGenRpt_Read(boolean genRpt_Read) {
        this.genRpt_Read = genRpt_Read;
    }

    public boolean isGenRpt_Write() {
        return genRpt_Write;
    }

    public void setGenRpt_Write(boolean genRpt_Write) {
        this.genRpt_Write = genRpt_Write;
    }

    public boolean isGroundRent_BillGroundRent() {
        return groundRent_BillGroundRent;
    }

    public void setGroundRent_BillGroundRent(boolean groundRent_BillGroundRent) {
        this.groundRent_BillGroundRent = groundRent_BillGroundRent;
    }

    public boolean isGroundRent_Read() {
        return groundRent_Read;
    }

    public void setGroundRent_Read(boolean groundRent_Read) {
        this.groundRent_Read = groundRent_Read;
    }

    public boolean isGroundRent_Write() {
        return groundRent_Write;
    }

    public void setGroundRent_Write(boolean groundRent_Write) {
        this.groundRent_Write = groundRent_Write;
    }

    public boolean isOccupProp_Read() {
        return occupProp_Read;
    }

    public void setOccupProp_Read(boolean occupProp_Read) {
        this.occupProp_Read = occupProp_Read;
    }

    public boolean isOccupProp_Write() {
        return occupProp_Write;
    }

    public void setOccupProp_Write(boolean occupProp_Write) {
        this.occupProp_Write = occupProp_Write;
    }

    public boolean isOccupant_Read() {
        return occupant_Read;
    }

    public void setOccupant_Read(boolean occupant_Read) {
        this.occupant_Read = occupant_Read;
    }

    public boolean isOccupant_Write() {
        return occupant_Write;
    }

    public void setOccupant_Write(boolean occupant_Write) {
        this.occupant_Write = occupant_Write;
    }

    public boolean isPropLedger_PrintDemandNote() {
        return propLedger_PrintDemandNote;
    }

    public void setPropLedger_PrintDemandNote(boolean propLedger_PrintDemandNote) {
        this.propLedger_PrintDemandNote = propLedger_PrintDemandNote;
    }

    public boolean isPropLedger_PrintLedgerStatement() {
        return propLedger_PrintLedgerStatement;
    }

    public void setPropLedger_PrintLedgerStatement(boolean propLedger_PrintLedgerStatement) {
        this.propLedger_PrintLedgerStatement = propLedger_PrintLedgerStatement;
    }

    public boolean isPropLedger_Read() {
        return propLedger_Read;
    }

    public void setPropLedger_Read(boolean propLedger_Read) {
        this.propLedger_Read = propLedger_Read;
    }

    public boolean isPropLedger_Write() {
        return propLedger_Write;
    }

    public void setPropLedger_Write(boolean propLedger_Write) {
        this.propLedger_Write = propLedger_Write;
    }

    public boolean isProperty_Read() {
        return property_Read;
    }

    public void setProperty_Read(boolean property_Read) {
        this.property_Read = property_Read;
    }

    public boolean isProperty_Write() {
        return property_Write;
    }

    public void setProperty_Write(boolean property_Write) {
        this.property_Write = property_Write;
    }

    public boolean isRck_Read() {
        return rck_Read;
    }

    public void setRck_Read(boolean rck_Read) {
        this.rck_Read = rck_Read;
    }

    public boolean isRck_Write() {
        return rck_Write;
    }

    public void setRck_Write(boolean rck_Write) {
        this.rck_Write = rck_Write;
    }

    public boolean isRentBilling_Read() {
        return rentBilling_Read;
    }

    public void setRentBilling_Read(boolean rentBilling_Read) {
        this.rentBilling_Read = rentBilling_Read;
    }

    public boolean isRentBilling_Write() {
        return rentBilling_Write;
    }

    public void setRentBilling_Write(boolean rentBilling_Write) {
        this.rentBilling_Write = rentBilling_Write;
    }

    public boolean isStaff_Read() {
        return staff_Read;
    }

    public void setStaff_Read(boolean staff_Read) {
        this.staff_Read = staff_Read;
    }

    public boolean isStaff_Write() {
        return staff_Write;
    }

    public void setStaff_Write(boolean staff_Write) {
        this.staff_Write = staff_Write;
    }

    public boolean isUac_Read() {
        return uac_Read;
    }

    public void setUac_Read(boolean uac_Read) {
        this.uac_Read = uac_Read;
    }

    public boolean isUac_Write() {
        return uac_Write;
    }

    public void setUac_Write(boolean uac_Write) {
        this.uac_Write = uac_Write;
    }
    
    
    
    
    public boolean isBill_status_report() {
        return bill_status_report;
    }

    public void setBill_status_report(boolean bill_status_report) {
        this.bill_status_report = bill_status_report;
    }
//</editor-fold>

}

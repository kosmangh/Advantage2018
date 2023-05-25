/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.constants.DebitCredit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author crash
 */
public class HouseRentBiller {

    private String selectedMonth = "";
    private int selectedYear;
    private static HouseRentBiller rentBiller = new HouseRentBiller();
    List<EstateProperty> listOfEstateProperty = null;
    private double currentAmount = 0.0;
    private double amtCharged = 0.0;
    private EstateProperty estateProperty;
    private boolean billingSuccessful;
    private String processingMsg = "";

    public HouseRentBiller() {
    }

    public boolean billHouseRent() {
        try {
            Occupancy occupancy = estateProperty.getOccupancy();
            if (occupancy == null) {
                processingMsg = "Aborting attempt to bill house rent on non-occupied property";
                System.out.println(processingMsg);


                billingSuccessful = false;
                return false;
            }

            if (occupancy.getOccupationType() != PropOccupationType.Rental) {
                processingMsg = "Aborting attempt to bill house rent on non-rental property";
                billingSuccessful = false;
                return false;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
            propertyLedger.setEstateProperty(estateProperty);
            propertyLedger.setLedgerYear(selectedYear);
            propertyLedger.setAmountInvolved(amtCharged);
            propertyLedger.setDateOfRecordEntry(new Date());
            propertyLedger.setDateOfRecordTransaction(calendar.getTime());
            propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            propertyLedger.setPaymentType(PaymentType.HOUSE_RENT);
            propertyLedger.setPropertyOccupant(estateProperty.getCurrentPropertyOccupant());
            propertyLedger.setPayeeName(AdConstants.SHC);
            propertyLedger.setMediumOfPayment(AdConstants.NONE);
            propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);
            propertyLedger.setPaymentFor("House Rent Bill, " + selectedMonth + "," + selectedYear);

            IDCreator.setEstateLedgerId(propertyLedger, selectedMonth);

            System.out.print(propertyLedger + "\t");

            ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);


            billingSuccessful = true;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public double getAppropriateHouseRent() {

        PropertyUsage usage = estateProperty.getPropertyUsage();
        if (usage == null) {
            return 0.0;
        } else {
            return ds.getCustomQry().getHouseRentBasicCharge(estateProperty, selectedMonth, selectedYear);
        }
    }
    
    public double getCalculated(EstateProperty ep, String month, int year){
        this.selectedMonth = month;
        this.selectedYear = year;
        this.estateProperty = ep;
        
        return getAppropriateHouseRent();
    }

    //<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public static HouseRentBiller getRentBiller() {
        return rentBiller;
    }

    public static void setRentBiller(HouseRentBiller rentBiller) {
        HouseRentBiller.rentBiller = rentBiller;
    }

    public List<EstateProperty> getListOfEstateProperty() {
        return listOfEstateProperty;
    }

    public void setListOfEstateProperty(List<EstateProperty> listOfEstateProperty) {
        this.listOfEstateProperty = listOfEstateProperty;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }
    //</editor-fold> 
    
}

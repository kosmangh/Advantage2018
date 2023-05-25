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
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.formating.NumberFormattingUtils;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author edwin / Ritchid
 */
public class GroundRentBiller {

    public static final double UNIT_SIZE = .25;
    private EstateProperty estateProperty;
    private PropertyOccupant propertyOccupant;
    private int selectedYear = 0;
    private double amtToBeCharge = 0.0;
    private double prevAmtCharged = 0.0;
    private int backTrackYear = 0;
    private double outstandingBalance = 0.0;
    private boolean billingSuccessfull = false;
    private String processingMsg = "";
    private List<EstatePropertyLedger> listOfEstateLedger = null;
    private List<EstatePropertyLedger> listOfDebitLedger = null;
    private int currentYear = DateTimeUtils.getCurrentYear();
    private List<Object[]> estatePropertyObjList = null;
    private PaymentType paymentType;
    private double amountInvolved = 0.0;

    
     int year_current = Calendar.getInstance().get(Calendar.YEAR);
    public boolean billPropertyGroud() {

        backTrackYear = selectedYear;
        paymentType = estateProperty.supposePaymentType();
        if (paymentType == null) {
            processingMsg = "Property is currently unoccupied or "
                    + "Property occupation type cannot be determind";
        }
        Occupancy occupancy = estateProperty.getOccupancy();
        if (occupancy == null) {
            processingMsg = "Aborting attempt to bill ground rent on non-occupied property";
            System.out.println(processingMsg);
            Logger.getLogger(GroundRentBiller.class.getName()).log(Level.INFO, processingMsg);

            billingSuccessfull = false;
            return false;
        }

        if (occupancy.getOccupationType() != PropOccupationType.Leasehold) {
            processingMsg = "Aborting attempt to bill ground rent on non-leasehold property";
            System.out.println(processingMsg);
            Logger.getLogger(GroundRentBiller.class.getName()).log(Level.INFO, processingMsg);

            billingSuccessfull = false;
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, selectedYear);
  

        amtToBeCharge = NumberFormattingUtils.formatDecimalNumberTo_2(getAppropriateGroundRentBill());

        //load ledger entry for selected, if empty create new ledger entry for property
//        listOfEstateLedger = ds.getCustomQry().loadLedgerEntriesForSelectedYear(estateProperty, selectedYear);
//        if (listOfEstateLedger.isEmpty()) {
            EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
            propertyLedger.setEstateProperty(estateProperty);
            propertyLedger.setLedgerYear(selectedYear);
            propertyLedger.setAmountInvolved(amtToBeCharge); 
            propertyLedger.setDateOfRecordEntry(new Date());
            propertyLedger.setDateOfRecordTransaction(calendar.getTime());
            propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            propertyLedger.setPaymentType(PaymentType.GROUND_RENT);
            propertyLedger.setPropertyOccupant(estateProperty.getCurrentPropertyOccupant());
            propertyLedger.setPayeeName(AdConstants.SHC);
            propertyLedger.setMediumOfPayment(AdConstants.NONE);
            propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);
            propertyLedger.setPaymentFor("Ground Rent Bill, " + selectedYear);

            IDCreator.setEstateLedgerId(propertyLedger, null);

            System.out.print(propertyLedger + "\t");

            ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);

//        }
        /**
         * We get previous groundrent charges of for the lessee, we check if the
         * previous is less than the current amount being charged and the lessee
         * hasn't made any payment we update the previous with the current
         * charge
         */
        //we loop through all the ledger payments for an occupant and do updates where necessary
//        double backTrackSum = 0.0;
//        Integer firstYear = ds.getCustomQry().getFirstEstateProperyLedgeryear(estateProperty);
//        double totalCredit = ds.getCustomQry().sumLedgerEntryForProperty(estateProperty, DebitCredit.CREDIT);
//        for (int i = firstYear; i <= backTrackYear; i++) {
//            listOfDebitLedger = ds.getCustomQry().loadLedgerEntriesOnEntryType(estateProperty, i, DebitCredit.DEBIT);
//            for (EstatePropertyLedger epl : listOfDebitLedger) {
//                backTrackSum += epl.getAmountInvolved();
//                if (backTrackSum > totalCredit) {
//                    epl.setAmountInvolved(amtToBeCharge);
//                    ds.getCommonQry().estatePropertyLedgerUpdate(epl);
//                }
//            }
//
//        }
        //we loop through all the ledger payments for an occupant and do updates where necessary
//        while (backTrackYear > 1983) {
//            listOfEstateLedger = ds.getCustomQry().loadLedgerEntriesForSelectedYear(estateProperty, backTrackYear);
//            prevAmtCharged = getCalculated(estateProperty, backTrackYear);
//            prevAmtCharged = NumberFormattingUtils.formatDecimalNumberTo_2(prevAmtCharged);
//            double amountInvolvedForYear = NumberFormattingUtils.formatDecimalNumberTo_2(ds.getCustomQry().getLedgerByYearPaidFor(estateProperty, backTrackYear, DebitCredit.CREDIT));
//            for (EstatePropertyLedger epl : listOfEstateLedger) {
//                if ((prevAmtCharged < amtToBeCharge) && (amountInvolvedForYear == 0)) {
//                    epl.setDebitCreditEntry(DebitCredit.DEBIT);
//                    epl.setAmountInvolved(amtToBeCharge);
//                    ds.getCommonQry().estatePropertyLedgerUpdate(epl);
//                } else if ((prevAmtCharged < amtToBeCharge) && (amountInvolvedForYear < prevAmtCharged)) {
//                    //double penaltyDiff = amtToBeCharge - amountInvolvedForYear;
//                    epl.setDebitCreditEntry(DebitCredit.DEBIT);
//                    epl.setAmountInvolved(amtToBeCharge);
//                    ds.getCommonQry().estatePropertyLedgerUpdate(epl);
//                }
//
//            }
//            backTrackYear = backTrackYear - 1;
//
//        }

        billingSuccessfull = true;
        return true;
    }
    
    public boolean chargeDefaultPenalty(){
        backTrackYear = selectedYear;
        paymentType = estateProperty.supposePaymentType();
        if (paymentType == null) {
            processingMsg = "Property is currently unoccupied or "
                    + "Property occupation type cannot be determind";
        }
        Occupancy occupantProperty = estateProperty.currentOccupantProperty();
        if (occupantProperty == null) {
            processingMsg = "Aborting attempt to bill ground rent on non-occupied property";
            System.out.println(processingMsg);
            Logger.getLogger(GroundRentBiller.class.getName()).log(Level.INFO, processingMsg);

            billingSuccessfull = false;
            return false;
        }

        if (occupantProperty.getOccupationType() != PropOccupationType.Leasehold) {
            processingMsg = "Aborting attempt to bill ground rent on non-leasehold property";
            System.out.println(processingMsg);
            Logger.getLogger(GroundRentBiller.class.getName()).log(Level.INFO, processingMsg);

            billingSuccessfull = false;
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, selectedYear);


        amtToBeCharge = NumberFormattingUtils.formatDecimalNumberTo_2(getAppropriateGroundRentBill());

        //load ledger entry for selected, if empty create new ledger entry for property
        listOfEstateLedger = ds.getCustomQry().loadLedgerEntriesForSelectedYear(estateProperty, selectedYear);
        if (listOfEstateLedger.isEmpty()) {
            EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
            propertyLedger.setEstateProperty(estateProperty);
            propertyLedger.setLedgerYear(selectedYear);
            propertyLedger.setAmountInvolved(amtToBeCharge);
            propertyLedger.setDateOfRecordEntry(new Date());
            propertyLedger.setDateOfRecordTransaction(calendar.getTime());
            propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            propertyLedger.setPaymentType(PaymentType.GROUND_RENT);
            propertyLedger.setPropertyOccupant(estateProperty.getCurrentPropertyOccupant());
            propertyLedger.setPayeeName(AdConstants.SHC);
            propertyLedger.setMediumOfPayment(AdConstants.NONE);
            propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);
            propertyLedger.setPaymentFor("Ground Rent Bill, " + selectedYear);

            IDCreator.setEstateLedgerId(propertyLedger, null);

            System.out.print(propertyLedger + "\t");

            ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);

        }
        /**
         * We get previous groundrent charges of for the lessee, we check if the
         * previous is less than the current amount being charged and the lessee
         * hasn't made any payment we update the previous with the current
         * charge
         */
        //we loop through all the ledger payments for an occupant and do updates where necessary
        double backTrackSum = 0.0;
        Integer firstYear = ds.getCustomQry().getFirstEstateProperyLedgeryear(estateProperty);
        double totalCredit = ds.getCustomQry().sumLedgerEntryForProperty(estateProperty, DebitCredit.CREDIT);
        for (int i = firstYear; i <= backTrackYear; i++) {
            listOfDebitLedger = ds.getCustomQry().loadLedgerEntriesOnEntryType(estateProperty, i, DebitCredit.DEBIT);
            for (EstatePropertyLedger epl : listOfDebitLedger) {
                backTrackSum += epl.getAmountInvolved();
                if (backTrackSum > totalCredit) {
                    epl.setAmountInvolved(amtToBeCharge);
                    ds.getCommonQry().estatePropertyLedgerUpdate(epl);
                }
            }

        }
        billingSuccessfull = true;
        return true;
    }
    
    public boolean chargeHouseRentArrears(){
        try {
            listOfEstateLedger.clear();
            listOfEstateLedger = ds.getCustomQry().loadEstatePropertyLegders(estateProperty);
            //get first ledger year and transaction date
            int firstLedgerYr = listOfEstateLedger.get(0).getLedgerYear();
            Date firstTransDate = listOfEstateLedger.get(0).getDateOfRecordTransaction();
            //convert first transaction date to month
            int firstEntryMonth = DateTimeUtils.getMonth(firstTransDate);
            //convert first ledger entry into calender from
            Calendar firstEntry = Calendar.getInstance();
            firstEntry.set(Calendar.YEAR, firstLedgerYr);
            firstEntry.set(Calendar.MONTH, firstEntryMonth);
            
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(new Date());
            
            //charge months left in arrears
            while(firstEntry.getTime().getYear() >= currentCal.getTime().getYear()){
                for(EstatePropertyLedger ep: listOfEstateLedger){
                    
                    //
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean chargeGroundRentArreas() {
        try {
            backTrackYear = selectedYear;

            Occupancy occupantProperty = estateProperty.currentOccupantProperty();
            if (occupantProperty == null) {
                processingMsg = "Aborting attempt to bill ground rent on non-occupied property";
                System.out.println(processingMsg);
                Logger.getLogger(GroundRentBiller.class.getName()).log(Level.INFO, processingMsg);

                billingSuccessfull = false;
                return false;
            }

            if (occupantProperty.getOccupationType() != PropOccupationType.Leasehold) {
                processingMsg = "Aborting attempt to bill ground rent on non-leasehold property";
                System.out.println(processingMsg);
                Logger.getLogger(GroundRentBiller.class.getName()).log(Level.INFO, processingMsg);

                billingSuccessfull = false;
                return false;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.YEAR, selectedYear);


            amtToBeCharge = getCurrentGroundRentBill();

            //load ledger entry for selected, if empty create new ledger entry for property
            listOfEstateLedger = ds.getCustomQry().loadLedgerEntriesForSelectedYear(estateProperty, selectedYear);
//            if (listOfEstateLedger.isEmpty()) {
            while (backTrackYear <= currentYear) {
                EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
                propertyLedger.setEstateProperty(estateProperty);
                propertyLedger.setLedgerYear(backTrackYear);
                propertyLedger.setAmountInvolved(amtToBeCharge);
                propertyLedger.setDateOfRecordEntry(new Date());
                propertyLedger.setDateOfRecordTransaction(calendar.getTime());
                propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
                propertyLedger.setPaymentType(PaymentType.GROUND_RENT);
                propertyLedger.setPropertyOccupant(estateProperty.getCurrentPropertyOccupant());
                propertyLedger.setPayeeName(AdConstants.SHC);
                propertyLedger.setMediumOfPayment(AdConstants.NONE);
                propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);
                propertyLedger.setPaymentFor("Ground Rent Bill, " + backTrackYear);

                IDCreator.setEstateLedgerId(propertyLedger, null);

                System.out.print(propertyLedger + "\t");

                ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);

                backTrackYear = backTrackYear + 1;

            }

//            }
            /**
             * We get previous groundrent charges of for the lessee, we check if
             * the previous is less than the current amount being charged and
             * the lessee hasn't made any payment we update the previous with
             * the current charge
             */
            //we loop through all the ledger payments for an occupant and do updates where necessary
//            while (backTrackYear <= currentYear) {
//                listOfEstateLedger = ds.getCustomQry().loadLedgerEntriesForSelectedYear(estateProperty, backTrackYear);
//                prevAmtCharged = getCalculated(estateProperty, backTrackYear);
//                prevAmtCharged = NumberFormattingUtils.formatDecimalNumberTo_2(prevAmtCharged);
//                double amountInvolvedForYear = NumberFormattingUtils.formatDecimalNumberTo_2(ds.getCustomQry().getLedgerByYearPaidFor(estateProperty, backTrackYear, DebitCredit.CREDIT));
//                for (EstatePropertyLedger epl : listOfEstateLedger) {
//                    if ((prevAmtCharged < amtToBeCharge) && (amountInvolvedForYear == 0)) {
//                        epl.setDebitCreditEntry(DebitCredit.DEBIT);
//                        epl.setAmountInvolved(amtToBeCharge);
//                        ds.getCommonQry().estatePropertyLedgerUpdate(epl);
//                    } else if ((prevAmtCharged < amtToBeCharge) && (amountInvolvedForYear < prevAmtCharged)) {
//                        //double penaltyDiff = amtToBeCharge - amountInvolvedForYear;
//                        epl.setDebitCreditEntry(DebitCredit.DEBIT);
//                        epl.setAmountInvolved(amtToBeCharge);
//                        ds.getCommonQry().estatePropertyLedgerUpdate(epl);
//                    }
//
//                }
//                backTrackYear = backTrackYear + 1;
//
//            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeGroundRent() {
        try {
            EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
            propertyLedger.setEstateProperty(estateProperty);
            propertyLedger.setLedgerYear(selectedYear);

            propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            propertyLedger.setPaymentType(PaymentType.GROUND_RENT);
            propertyLedger.setPropertyOccupant(estateProperty.getCurrentPropertyOccupant());
            propertyLedger.setPayeeName(AdConstants.SHC);
            propertyLedger.setMediumOfPayment(AdConstants.NONE);
            propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);


            IDCreator.setEstateLedgerId(propertyLedger, null);

            System.out.print(propertyLedger + "\t");

            // the delete problem may be coming form here
            boolean delete = ds.getCommonQry().estatePropertyLedgerDelete(propertyLedger, true);

            if (delete) {
                processingMsg = "Deleted";
                billingSuccessfull = true;
                JsfUtil.addInformationMessage(processingMsg);
            } else {
                processingMsg = "Unable to Delete";
                billingSuccessfull = false;
                JsfUtil.addErrorMessage(processingMsg);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Removing ground rent failed");
        }


        return billingSuccessfull;

    }
    
        private double getAppropriateGroundRentBilll() {
            try {
                
                PropertyUsage ussage = estateProperty.getPropertyUsage();

                if (ussage == null) {
                    return 0.0;
                }

//        PaymentType paymentType = estateProperty.supposePaymentType();
//        if(paymentType == null)

                if (ussage == PropertyUsage.Commercial || ussage == PropertyUsage.Mix_Use || ussage == PropertyUsage.Institutional || ussage == PropertyUsage.Residential) {
                    return getCommMixedUsee();
                } else {
                    return ds.getCustomQry().getGroundRentBasicChargee(estateProperty, selectedYear);
                }
        
        
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0.00;
        }
        
    

    private double getAppropriateGroundRentBill() {

        PropertyUsage ussage = estateProperty.getPropertyUsage();

        if (ussage == null) {
            return 0.0;
        }

//        PaymentType paymentType = estateProperty.supposePaymentType();
//        if(paymentType == null)

        if (ussage == PropertyUsage.Commercial || ussage == PropertyUsage.Mix_Use || ussage == PropertyUsage.Institutional || ussage == PropertyUsage.Residential) {
            return getCommMixedUse();
        } else {
            return ds.getCustomQry().getGroundRentBasicCharge(estateProperty, selectedYear);
        }
    }

    private double getCurrentGroundRentBill() {
        try {
            PropertyUsage usage = estateProperty.getPropertyUsage();
            if (usage == null) {
                return 0.0;
            }
            if (usage == PropertyUsage.Commercial || usage == PropertyUsage.Mix_Use || usage == PropertyUsage.Institutional || usage == PropertyUsage.Residential) {
                return getCurrentForComMixedUse();
            } else {
                return ds.getCustomQry().getGroundRentBasicCharge(estateProperty, currentYear);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    
    
      private double getCommMixedUsee() {

        double a = ds.getCustomQry().getGroundRentBasicChargee(estateProperty, selectedYear);

        if (selectedYear < 2008) {
            return a;
        }

        double landSize = estateProperty.getPropertyLandSize();

        if (landSize > UNIT_SIZE) {
            double newRate = (landSize / UNIT_SIZE) * a;
            return newRate;
        }

        return a;
    }

    private double getCommMixedUse() {

        double a = ds.getCustomQry().getGroundRentBasicCharge(estateProperty, selectedYear);

        if (selectedYear < 2008) {
            return a;
        }

        double landSize = estateProperty.getPropertyLandSize();

        if (landSize > UNIT_SIZE) {
            double newRate = (landSize / UNIT_SIZE) * a;

            return newRate;
        }


        return a;
    }

    private double getCurrentForComMixedUse() {
        double a = ds.getCustomQry().getGroundRentBasicCharge(estateProperty, currentYear);

        if (selectedYear < 2008) {
            return a;
        }

        double landSize = estateProperty.getPropertyLandSize();

        if (landSize > UNIT_SIZE) {
            double newRate = (landSize / UNIT_SIZE) * a;

            return newRate;
        }


        return a;
    }
    private static GroundRentBiller rentBiller = new GroundRentBiller();

    public static GroundRentBiller instance() {

        return rentBiller;
    }
    
      public double getCalculatedd(EstateProperty estateProperty, int year) {
        this.estateProperty = estateProperty;
        this.selectedYear = year;
 
        return getAppropriateGroundRentBilll();
    }

    public double getCalculated(EstateProperty estateProperty, int year) {
        this.estateProperty = estateProperty;
        this.selectedYear = year;
 
        return getAppropriateGroundRentBill();
    }

    public void setEstateProperty(EstateProperty estateProperty) {
        this.estateProperty = estateProperty;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public boolean isBillingSuccessfull() {
        return billingSuccessfull;
    }

    public String getProcessingMsg() {
        return processingMsg;
    }

    public PropertyOccupant getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(PropertyOccupant propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
    }
    
    
    
}
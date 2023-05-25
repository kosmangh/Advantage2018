
package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.models.BillDemandNote;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.collection.CollectionUtils;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.formating.NumberFormattingUtils;
import com.sabonay.common.formating.ObjectFormat;
import com.sabonay.common.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Edwin / Ritchid
 */
public class BillDemandNoteMaker {

    double maxAmountInvolved = 0;
    double totalCredit = 0;
    double totalDebit = 0;
    double penalty = 0;
    double outstandingBalance = 0;
    double penaltyBalance = 0;
    private PaymentType paymentType;
    private String assement = "";
    private String assesmentSummary;
    private String periodOfBalance;
    private Date transactionDate;
    private int paymentUnit;
    int backTackUnit = 0;
    int baseYear = 0;
    int backTrackYear = 0;
    int currentYear = 0;
    String message;
    private Date lastDateOfTransactionInDate = null;
    private EstateProperty estateProperty;
    private Date paymentDeadline;
    private List<Object[]> objEstatePropertyLedgerList = new ArrayList<>();
    private BillDemandNote groundRentDemandNotice = null;
    private static Map<String, ProcessingResult> map = new HashMap<>();
    String timeOfDemandNotice = "";
    String billReviewDate = "";
    String balancePeriodOfDemandNotice = "";
    String billConclusion = "";
    private int currentYr = DateTimeUtils.getCurrentYear() - 1;
    private int selectedYear;
    private double currentCharge;
    //private int selectedYear = DateTimeUtils.getCurrentYear();
    private int initialYr = 0;

    
    
    
    
    
    
    
    
    public  BillDemandNoteMaker(EstateProperty estateProperty, int baseYear) {
        paymentType = estateProperty.supposePaymentType();
        if (paymentType == null) {
            message = "Property is currently not occupied or "
                    + "Occupation type cannot be determined i.e (Lessee / Tenant)";

        } else {
            objEstatePropertyLedgerList = ds.getCustomQry().getLegerSummationForPaymenttype(estateProperty, paymentType, baseYear);
            
            if(objEstatePropertyLedgerList.isEmpty()){
               // objEstatePropertyLedgerList = null;
                return;
            }
            //For calculating our penalty
//            estatePropertyLedgerList = ds.getCustomQry().getLedgerPaymentForType(estateProperty, paymentType, baseYear);
            if (objEstatePropertyLedgerList != null) {
                if (!objEstatePropertyLedgerList.isEmpty()) {
                    lastDateOfTransactionInDate = (Date) objEstatePropertyLedgerList.get(objEstatePropertyLedgerList.size() - 1)[2];
                }
            }
            
//            }else{
//                  
//                boolean equals = objEstatePropertyLedgerList.equals(false);
//                
//             
//                
//                //return;
//            }

            this.baseYear = baseYear;
            this.estateProperty = estateProperty;
        }
               //  return objEstatePropertyLedgerList;
    }
    
    
    
    
    
    
    
    
    
    public List<Object[]> check(EstateProperty ep,int baseYear){
        paymentType = estateProperty.supposePaymentType();
        if(paymentType == null){
            message = "Property is currently not occupied or "
                    + "Occupation type cannot be determined i.e (Lessee / Tenant)";

            return null; 
        }else{
         objEstatePropertyLedgerList = ds.getCustomQry().getLegerSummationForPaymenttype(ep, paymentType, baseYear);
        
         return objEstatePropertyLedgerList;
         
        }
    }
    
    public void processForNonBilled() {
        double amountInvolved = 0;
        double grndRentDiff = 0;

        if (objEstatePropertyLedgerList == null) {
          
      

//        for (Object[] estatePropertyLedger : objEstatePropertyLedgerList) {
//            DebitCredit debitCredit = (DebitCredit) estatePropertyLedger[0];
//
//            amountInvolved = ObjectFormat.getDoubleObject(estatePropertyLedger[1]);
//            transactionDate = (Date) estatePropertyLedger[3];
//
//            if (debitCredit == DebitCredit.DEBIT) {
//                totalDebit += amountInvolved;
//            } else if (debitCredit == DebitCredit.CREDIT) {
//                totalCredit += amountInvolved;
//            }
//        }

//        for (Object[] estatePropetyLedger : estatePropertyLedgerList) {
//            DebitCredit debitCredit = (DebitCredit) estatePropetyLedger[0];
//            double amounts = ObjectFormat.getDoubleObject(estatePropetyLedger[1]);
//            if (debitCredit == DebitCredit.DEBIT) {
//            }
//        }
        outstandingBalance = totalDebit - totalCredit;
        penaltyBalance = totalCredit;

//        System.out.println("outstanding balance after calculation = " + outstandingBalance);
        String key = baseYear + "#" + outstandingBalance + "#"
                + estateProperty.getPropertyEstate().getEstateClass()
                + "#" + estateProperty.getPropertyLandSize()
                + "#" + estateProperty.getPropertyUsage();

        ProcessingResult result = map.get(key);

        System.out.println("checking for key .. " + key + " and resut is " + result);

        if ((paymentType == PaymentType.GROUND_RENT) && (result != null)) {
            periodOfBalance = result.getPeriodOfBalance();
            assement = result.getAssement();
            penalty = result.getPenalty();
            outstandingBalance = result.getDifference();

            assement = "Assesment (" + (estateProperty.getPropertyName()) + ") \n" + assement;
            return;

        } else if (paymentType == PaymentType.HOUSE_RENT) {
            rentAssessment(); 
        } else {
            leaseHoldAssessment();
        }

        ProcessingResult processingResult = new ProcessingResult();
        processingResult.setAssement(assement);
        processingResult.setPenalty(penalty);
        processingResult.setPeriodOfBalance(periodOfBalance);
        processingResult.setDifference(outstandingBalance);

        map.put(key, processingResult);
        }
    }

    public void process() {
        try {
            double amountInvolved = 0;
            double grndRentDiff = 0;

            if (objEstatePropertyLedgerList == null) {
                return;
            }

            for (Object[] estatePropertyLedger : objEstatePropertyLedgerList) {
                DebitCredit debitCredit = (DebitCredit) estatePropertyLedger[0];

                amountInvolved = ObjectFormat.getDoubleObject(estatePropertyLedger[1]);
                transactionDate = (Date) estatePropertyLedger[3];

                if (debitCredit == DebitCredit.DEBIT) {
                    totalDebit += amountInvolved;
                } else if (debitCredit == DebitCredit.CREDIT) {
                    totalCredit += amountInvolved;
                }
            }

//        for (Object[] estatePropetyLedger : estatePropertyLedgerList) {
//            DebitCredit debitCredit = (DebitCredit) estatePropetyLedger[0];
//            double amounts = ObjectFormat.getDoubleObject(estatePropetyLedger[1]);
//            if (debitCredit == DebitCredit.DEBIT) {
//            }
//        }
            outstandingBalance = totalDebit - totalCredit;
            System.out.println(outstandingBalance+" --------");
            penaltyBalance = totalCredit;

//        System.out.println("outstanding balance after calculation = " + outstandingBalance);
        String key = baseYear + "#" + outstandingBalance + "#"
                + estateProperty.getPropertyEstate().getEstateClass()
                + "#" + estateProperty.getPropertyLandSize()
                + "#" + estateProperty.getPropertyUsage();

        ProcessingResult result = map.get(key);

        System.out.println("checking for key .. " + key + " and resut is " + result);

        if ((paymentType == PaymentType.GROUND_RENT) && (result != null)) {
            periodOfBalance = result.getPeriodOfBalance();
            assement = result.getAssement();
            penalty = result.getPenalty();
            outstandingBalance = result.getDifference();

            
            assement = "Assesment (" + (estateProperty.getPropertyName()) + ") \n" + assement;
           // assement = "Assesment/Outstanding (" + (estateProperty.getPropertyName()) + ") \n" + outstandingBalance;
            return;

        } else if (paymentType == PaymentType.HOUSE_RENT) {
            rentAssessment(); 
        } else {
            leaseHoldAssessment();
        }

        ProcessingResult processingResult = new ProcessingResult();
        processingResult.setAssement(assement);
        processingResult.setPenalty(penalty);
        processingResult.setPeriodOfBalance(periodOfBalance);
        processingResult.setDifference(outstandingBalance);

        map.put(key, processingResult);
        
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void rentAssessment() {
        if (outstandingBalance == 0.0) {
            return;
        }

        double backTrackSum = 0.0;
        backTackUnit = baseYear;

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int counter = 0;

        paymentUnit = 0;

        while (backTrackSum < outstandingBalance) {
            double amountCharged = ds.getCustomQry().rentCharges(estateProperty, cal.getTime());
            cal.add(Calendar.MONTH, -1);

            paymentUnit++;

            backTrackSum += amountCharged;

            System.out.println(++counter + " amt ch: " + amountCharged + "  backsum = "
                    + backTrackSum + " of " + outstandingBalance + " on " + cal.getTime());

            if (cal.get(Calendar.YEAR) < 1980) {
                break;
            }
        }

//        Date paymentUnitBackDate = DateTimeUtils.subMonthFromDate(lastDateOfTransactionInDate, paymentUnit);
        String current = DateTimeUtils.formatDate_monthYear(new Date());
//        String current = DateTimeUtils.formatDate_monthYear(lastDateOfTransactionInDate);

        cal.add(Calendar.MONTH, 1);
        String from = DateTimeUtils.formatDate_monthYear(cal.getTime());

        if (paymentUnit == 0) {
            assesmentSummary = "Paid in excess";
        } else {
            assesmentSummary = paymentUnit + " Mths (" + from + " to " + current + ")";
        }

        periodOfBalance = assesmentSummary;

        assement = assesmentSummary;
    }
    
    
    public BillDemandNote demandNoticee(int selectedYear) {
        double yearCharge = 0.0;
        double totalDr = 0.0;
        double totalCr = 0.0;
        currentCharge = 0.0;
        
        List<EstatePropertyLedger> estPropLedger = ds.getCommonQry().estatePropertyLedgerFindByAttribute("estateProperty", estateProperty, "NUMBER", false);
        
        for(EstatePropertyLedger query : estPropLedger){
            
            if(query.getDebitCreditEntry() == DebitCredit.DEBIT){
                if (query.getLedgerYear() == selectedYear) {
                    currentCharge = query.getAmountInvolved();
                    break;
                }
            }
        }
        
        for(EstatePropertyLedger epl : estPropLedger){
            if(epl.getLedgerYear() <= selectedYear){
                if((epl.getDebitCreditEntry() == DebitCredit.CREDIT))
                    totalCr += epl.getAmountInvolved();

                else if ((epl.getDebitCreditEntry() == DebitCredit.DEBIT))
                    totalDr += epl.getAmountInvolved();
            }
        }
        
        

        if (paymentType == PaymentType.GROUND_RENT) {
            yearCharge = currentCharge;
//            yearCharge = GroundRentBiller.instance().getCalculatedd(estateProperty, baseYear);

            System.out.println("GR for selected year is " + yearCharge);
            timeOfDemandNotice = baseYear + "";

            balancePeriodOfDemandNotice = (baseYear - 1) + "";

            billReviewDate = (baseYear + 1) + "";

            billConclusion = "Please take note that the " + paymentType.getPaymentTypeName()
                    + " will be reviewed in the " + paymentType.getPaymentPeriod()
                    + " " + billReviewDate;
            
        } else if (paymentType == PaymentType.HOUSE_RENT) {
            yearCharge = ds.getCustomQry().rentedPropertyLastRentBill(estateProperty.getEstatePropertyId());
            Calendar calender = Calendar.getInstance();  
//            timeOfDemandNotice = DateTimeUtils.formatDate_monthYear(lastDateOfTransactionInDate);
            timeOfDemandNotice = DateTimeUtils.formatDate_monthYear(calender.getTime());
//            Calendar calender = Calendar.getInstance();
//            calender.setTime(lastDateOfTransactionInDate);
            calender.setTime(calender.getTime());
            calender.add(Calendar.MONTH, 1);

            billReviewDate = DateTimeUtils.formatDate_monthYear(calender.getTime());

            calender.add(Calendar.MONTH, -2);
            balancePeriodOfDemandNotice = DateTimeUtils.formatDate_monthYear(calender.getTime());
        }

        outstandingBalance = totalDr - totalCr;
        System.out.println("ground rent for the current year is " + yearCharge);
        System.out.println("outstanding bal in demandNotice() :" + outstandingBalance);

        outstandingBalance = outstandingBalance - yearCharge;

        groundRentDemandNotice = new BillDemandNote();
        groundRentDemandNotice.setDemandNoticeHeading(getDemandNoticeHeading());
        groundRentDemandNotice.setBillIntroNote(getIntro());
        groundRentDemandNotice.setOccupantName(estateProperty.getCurrentOccupantName());
        groundRentDemandNotice.setPropertyOccupantId(estateProperty.getCurrentPropertyOccupant().getPropertyOccupantId());

        groundRentDemandNotice.setGroundRentBill(yearCharge);
        groundRentDemandNotice.setOutstandingBalance(outstandingBalance);
        groundRentDemandNotice.setStatement(assement);
        groundRentDemandNotice.setYearsOwingFrom(backTrackYear);
        groundRentDemandNotice.setEstateProperty(estateProperty.getPropertyName());
        groundRentDemandNotice.setPropertyUsage(estateProperty.getPropertyUsage().getUsageInitials());
        groundRentDemandNotice.setPropertyLocation(estateProperty.getPropertyEstate().getEstateName());

        groundRentDemandNotice.setBillReviewDate(billReviewDate);
        groundRentDemandNotice.setTimeOfDemandNotice(timeOfDemandNotice);
        groundRentDemandNotice.setBalancePeriodOfDemandNotice(balancePeriodOfDemandNotice);
        groundRentDemandNotice.setBillConclusion(billConclusion);

        groundRentDemandNotice.setPenalty(NumberFormattingUtils.formatDecimalNumberTo_2(penalty));
        groundRentDemandNotice.setBillFootnotes(getBillFootNotes());

        return groundRentDemandNotice;
    }

    public BillDemandNote demandNotice() {

        try {
               double yearCharge = 0.0;

        if (paymentType == PaymentType.GROUND_RENT) {
            yearCharge = GroundRentBiller.instance().getCalculatedd(estateProperty, baseYear);

            System.out.println("GR for selected year is " + yearCharge);
            timeOfDemandNotice = baseYear + "";

            balancePeriodOfDemandNotice = (baseYear - 1) + "";

            billReviewDate = (baseYear + 1) + "";

            billConclusion = "Please take note that the " + paymentType.getPaymentTypeName()
                    + " will be reviewed in the " + paymentType.getPaymentPeriod()
                    + " " + billReviewDate;
        } else if (paymentType == PaymentType.HOUSE_RENT) {
            yearCharge = ds.getCustomQry().rentedPropertyLastRentBill(estateProperty.getEstatePropertyId());
            Calendar calender = Calendar.getInstance();  
//            timeOfDemandNotice = DateTimeUtils.formatDate_monthYear(lastDateOfTransactionInDate);
            timeOfDemandNotice = DateTimeUtils.formatDate_monthYear(calender.getTime());
//            Calendar calender = Calendar.getInstance();
//            calender.setTime(lastDateOfTransactionInDate);
            calender.setTime(calender.getTime());
            calender.add(Calendar.MONTH, 1);

            billReviewDate = DateTimeUtils.formatDate_monthYear(calender.getTime());

            calender.add(Calendar.MONTH, -2);
            balancePeriodOfDemandNotice = DateTimeUtils.formatDate_monthYear(calender.getTime());
        }

        System.out.println("ground rent for the current year is " + yearCharge);
        System.out.println("outstanding bal in demandNotice() :" + outstandingBalance);

        outstandingBalance = outstandingBalance - yearCharge;

        groundRentDemandNotice = new BillDemandNote();
       groundRentDemandNotice.setBillIntroNote(getIntro());
       groundRentDemandNotice.setDemandNoticeHeading(getDemandNoticeHeading());
        groundRentDemandNotice.setOccupantName(estateProperty.getCurrentOccupantName());
        groundRentDemandNotice.setPropertyOccupantId(estateProperty.getCurrentPropertyOccupant().getPropertyOccupantId());

        groundRentDemandNotice.setGroundRentBill(yearCharge);
        groundRentDemandNotice.setOutstandingBalance(outstandingBalance);
        groundRentDemandNotice.setStatement(assement);
        groundRentDemandNotice.setYearsOwingFrom(backTrackYear);
        groundRentDemandNotice.setEstateProperty(estateProperty.getPropertyName());
        groundRentDemandNotice.setPropertyUsage(estateProperty.getPropertyUsage().getUsageInitials());
        groundRentDemandNotice.setPropertyLocation(estateProperty.getPropertyEstate().getEstateName());

        groundRentDemandNotice.setBillReviewDate(billReviewDate);
        groundRentDemandNotice.setTimeOfDemandNotice(timeOfDemandNotice);
        groundRentDemandNotice.setBalancePeriodOfDemandNotice(balancePeriodOfDemandNotice);
        groundRentDemandNotice.setBillConclusion(billConclusion);

        groundRentDemandNotice.setPenalty(NumberFormattingUtils.formatDecimalNumberTo_2(penalty));
       groundRentDemandNotice.setBillFootnotes(getBillFootNotes());

        return groundRentDemandNotice;
        
        
        } catch (Exception e) {
            e.printStackTrace();
            
            return null;
        }
    }
    
//       public BillDemandNote demandNoticeNewOccupantNoBilled() {
//
//        double yearCharge = 0.0;
//
//        if (paymentType == PaymentType.GROUND_RENT) {
//          //  yearCharge = GroundRentBiller.instance().getCalculated(estateProperty, baseYear);
//
//            System.out.println("GR for selected year is " + yearCharge);
//            timeOfDemandNotice = baseYear + "";
//
//            balancePeriodOfDemandNotice = (baseYear - 1) + "";
//
//            billReviewDate = (baseYear + 1) + "";
//
//            billConclusion = "Please take note that the " + paymentType.getPaymentTypeName()
//                    + " will be reviewed in the " + paymentType.getPaymentPeriod()
//                    + " " + billReviewDate;
//        } else if (paymentType == PaymentType.HOUSE_RENT) {
//            yearCharge = ds.getCustomQry().rentedPropertyLastRentBill(estateProperty.getEstatePropertyId());
//            Calendar calender = Calendar.getInstance();  
////            timeOfDemandNotice = DateTimeUtils.formatDate_monthYear(lastDateOfTransactionInDate);
//            timeOfDemandNotice = DateTimeUtils.formatDate_monthYear(calender.getTime());
////            Calendar calender = Calendar.getInstance();
////            calender.setTime(lastDateOfTransactionInDate);
//            calender.setTime(calender.getTime());
//            calender.add(Calendar.MONTH, 1);
//
//            billReviewDate = DateTimeUtils.formatDate_monthYear(calender.getTime());
//
//            calender.add(Calendar.MONTH, -2);
//            balancePeriodOfDemandNotice = DateTimeUtils.formatDate_monthYear(calender.getTime());
//        }
//
//        System.out.println("ground rent for the current year is " + yearCharge);
//        System.out.println("outstanding bal in demandNotice() :" + outstandingBalance);
//
//        outstandingBalance = outstandingBalance - yearCharge;
//
//        groundRentDemandNotice = new BillDemandNote();
//        groundRentDemandNotice.setBillIntroNote(getIntro());
//        groundRentDemandNotice.setDemandNoticeHeading(getDemandNoticeHeading());
//        groundRentDemandNotice.setOccupantName(estateProperty.getCurrentOccupantName());
//        groundRentDemandNotice.setPropertyOccupantId(estateProperty.getCurrentPropertyOccupant().getPropertyOccupantId());
//
//        groundRentDemandNotice.setGroundRentBill(yearCharge);
//        groundRentDemandNotice.setOutstandingBalance(outstandingBalance);
//        groundRentDemandNotice.setStatement(assement);
//        groundRentDemandNotice.setYearsOwingFrom(backTrackYear);
//        groundRentDemandNotice.setEstateProperty(estateProperty.getPropertyName());
//        groundRentDemandNotice.setPropertyUsage(estateProperty.getPropertyUsage().getUsageInitials());
//        groundRentDemandNotice.setPropertyLocation(estateProperty.getPropertyEstate().getEstateName());
//
//        groundRentDemandNotice.setBillReviewDate(billReviewDate);
//        groundRentDemandNotice.setTimeOfDemandNotice(timeOfDemandNotice);
//        groundRentDemandNotice.setBalancePeriodOfDemandNotice(balancePeriodOfDemandNotice);
//        groundRentDemandNotice.setBillConclusion(billConclusion);
//
//        groundRentDemandNotice.setPenalty(NumberFormattingUtils.formatDecimalNumberTo_2(penalty));
//        groundRentDemandNotice.setBillFootnotes(getBillFootNotes());
//
//        return groundRentDemandNotice;
//    }

    public String leaseHoldAssessment() {

        double backTrackSum = 0.0;
        currentYear = baseYear;
        paymentUnit = 0;

        if (estateProperty == null) {
            return "";
        }
        Integer firstYear = ds.getCustomQry().getFirstEstateProperyLedgeryear(estateProperty);
        initialYr = firstYear;
        double totalSumOfCredit = totalCredit;

        backTrackYear = baseYear;

        assement = "";
        List<Integer> assessmentYears = new ArrayList<>();
        double yearCharges = 0.0;
        
        
        
        String res = estateProperty.getEstatePropertyId();
       // List yyearCharges = ds.getCommonQry().load_occu_debit_from_ledger(res);
        yearCharges = GroundRentBiller.instance().getCalculated(estateProperty, baseYear);
      //  assement = "  " + "" + "Your Outstanding Assessment Amount:" + "  "+ "" + outstandingBalance;
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>Print debits" + assement);
        
//          while ((backTrackYear >= initialYr) && (backTrackSum < outstandingBalance)) {
//            double amountCharged = GroundRentBiller.instance().getCalculated(estateProperty, backTrackYear);
//            amountCharged = NumberFormattingUtils.formatDecimalNumberTo_2(amountCharged);
//            backTrackSum += amountCharged; 
//            paymentUnit++; 
//                       assement = "  " + backTrackYear + " -- " + amountCharged+ "\n " + assement;
//
////            assement = "  " + backTrackYear + " -- " + yearCharges + "\n " + assement;
//            assessmentYears.add(backTrackYear);
//            backTrackYear = backTrackYear - 1;
//        }
        while ((backTrackYear >= initialYr) && (backTrackSum < outstandingBalance)) {
            //double amountCharged = GroundRentBiller.instance().getCalculated(estateProperty, backTrackYear);
            List <EstatePropertyLedger> epl = ds.getCustomQry().getCalculatedBillForYear(estateProperty, backTrackYear);
            EstatePropertyLedger esp = epl.get(0);
            double amountCharged = esp.getAmountInvolved();
            
            amountCharged = NumberFormattingUtils.formatDecimalNumberTo_2(amountCharged);
            backTrackSum += amountCharged; 
            double excess = backTrackSum - outstandingBalance;
            if(backTrackSum > outstandingBalance){
                amountCharged -= excess;
              amountCharged = NumberFormattingUtils.formatDecimalNumberTo_2(amountCharged);   
            }
            paymentUnit++; 
           assement = "  " + backTrackYear + " -- " + amountCharged+ "\n " + assement;

           // assement = "  " + backTrackYear + " -- " + yearCharges + "\n " + assement;
            assessmentYears.add(backTrackYear);
            backTrackYear = backTrackYear - 1;
        }
        if (paymentUnit == 0) {
            assesmentSummary = "Paid in excess";
        } else {
            int from = assessmentYears.get(0);
            int to = assessmentYears.get(assessmentYears.size() - 1);
            assesmentSummary = paymentUnit + "Yrs (" + to + " to " + from + ")";
        }

        assessmentYears.clear();
        periodOfBalance = backTrackYear + "";
        return assement;
    }

    class ProcessingResult {

        private double maxAmountInvolved = 0;
        private double totalCredit = 0;
        private double totalDebit = 0;
        private double difference = 0;
        private PaymentType paymentType;
        private String assement = "";
        private String assesmentSummary;
        private String periodOfBalance;
        private double penalty = 0;
        private int paymentUnit = 0;
        private int backTackUnit = 0;
        private int baseYear = 0;
        private int backTrackYear = 0;

        public String getAssement() {
            return assement;
        }

        public void setAssement(String assement) {
            this.assement = assement;
        }

        public String getAssesmentSummary() {
            return assesmentSummary;
        }

        public void setAssesmentSummary(String assesmentSummary) {
            this.assesmentSummary = assesmentSummary;
        }

        public int getBackTackUnit() {
            return backTackUnit;
        }

        public void setBackTackUnit(int backTackUnit) {
            this.backTackUnit = backTackUnit;
        }

        public int getBackTrackYear() {
            return backTrackYear;
        }

        public void setBackTrackYear(int backTrackYear) {
            this.backTrackYear = backTrackYear;
        }

        public int getBaseYear() {
            return baseYear;
        }

        public void setBaseYear(int baseYear) {
            this.baseYear = baseYear;
        }

        public double getDifference() {
            return difference;
        }

        public void setDifference(double difference) {
            this.difference = difference;
        }

        public double getMaxAmountInvolved() {
            return maxAmountInvolved;
        }

        public void setMaxAmountInvolved(double maxAmountInvolved) {
            this.maxAmountInvolved = maxAmountInvolved;
        }

        public PaymentType getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(PaymentType paymentType) {
            this.paymentType = paymentType;
        }

        public int getPaymentUnit() {
            return paymentUnit;
        }

        public void setPaymentUnit(int paymentUnit) {
            this.paymentUnit = paymentUnit;
        }

        public String getPeriodOfBalance() {
            return periodOfBalance;
        }

        public void setPeriodOfBalance(String periodOfBalance) {
            this.periodOfBalance = periodOfBalance;
        }

        public double getPenalty() {
            return penalty;
        }

        public void setPenalty(double penalty) {
            this.penalty = penalty;
        }

        public double getTotalCredit() {
            return totalCredit;
        }

        public void setTotalCredit(double totalCredit) {
            this.totalCredit = totalCredit;
        }

        public double getTotalDebit() {
            return totalDebit;
        }

        public void setTotalDebit(double totalDebit) {
            this.totalDebit = totalDebit;
        }

        @Override
        public String toString() {
            return periodOfBalance + " " + difference + estateProperty.getPropertyName();
        }
    }

    private BillDemandNoteMaker(List<Object[]> estatePropertyLedgerList, EstateProperty estateProperty, int baseYear) {
        this.objEstatePropertyLedgerList = estatePropertyLedgerList;
        this.baseYear = baseYear;
        this.estateProperty = estateProperty;
        this.paymentType = estateProperty.getOccupancy().getAppropriatePaymentType();

        if (estatePropertyLedgerList != null) {
            if (!estatePropertyLedgerList.isEmpty()) {
                lastDateOfTransactionInDate = (Date) estatePropertyLedgerList.get(estatePropertyLedgerList.size() - 1)[2];
            }

        }
    }

    public String getBillFootNotes() {
        String footNote = "<html> "
                + "<ol>"
                + "<li> You will have to settle the Amount Due within  the said period at "
                + "the office of the State Housing Company Limited at North  Suntresu Estate</li>   "
                + "<li>If the above stated " + paymentType.getPaymentPeriod() + "ly amount of " + paymentType.getPaymentTypeName() + " "
                + "is not paid in full before date stated above, "
                + "You incur a sum penalty of the difference between the current yearly amount "
                + "and the previous years amount</li>"
                + "<li>If the above-stated " + paymentType.getPaymentPeriod() + "ly amount of " + paymentType.getPaymentTypeName() + "  "
                + "(together with the above stated arrears) is not paid before "
                + "date stated above,  "
                + "Court action for the recovery of same together with cost will be "
                + "taken without  further Notice.</li>   "
                + "<li>In accordance with the lease non-payment of " + paymentType.getPaymentTypeName() + " "
                + "reserved constitutes a breach of covenant whereby the Company is "
                + "entitled forthwith to re-enter and terminate the lease without "
                + "any right of compensation to you</li> "
                + "</ol> </html>";

        return footNote;
    }

    public String getDemandNoticeHeading() {
        
        try {
              return paymentType.getPaymentTypeName() + " Bill Demand Notice";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getIntro() {
        try {
            Calendar calender = Calendar.getInstance();

        calender.set(Calendar.YEAR, baseYear);
        calender.set(Calendar.MONTH, Calendar.JANUARY);
        calender.setFirstDayOfWeek(Calendar.MONDAY);
        calender.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        String formatedPaymentDate = DateTimeUtils.formatDateFully(calender.getTime());
        String demandNoticeHeader = "Please TAKE NOTE that " + paymentType.getPaymentTypeName() + " in "
                + "respect of the property below for the " + paymentType.getPaymentPeriod() + " " + timeOfDemandNotice + " "
                + "is due for payment by " + formatedPaymentDate;

        return demandNoticeHeader;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public String getBillConclusion()
//    {
//        return "Please take note that the"
//    }
    public static List<BillDemandNote> reportGroundRentDemandNotice(List<EstateProperty> estatePropertiesList, int selectedYear) {
        try {
             List<BillDemandNote> billDemandNotesList = new LinkedList<BillDemandNote>();

        CollectionUtils.sortStringValue(estatePropertiesList);

        for (EstateProperty estateProperty : estatePropertiesList) {
            if (estateProperty.getCurrentPropertyOccupant() == null) {
                continue;
            }

            System.out.println("   ....... Starting to process " + estateProperty);

            BillDemandNoteMaker inter = new BillDemandNoteMaker(estateProperty, selectedYear);
            inter.process();

            BillDemandNote demandNotice = inter.demandNotice();

            billDemandNotesList.add(demandNotice);
            System.out.println("\t\t\t... Processing Completed.\n\n\n");
        }

        return billDemandNotesList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
       
    }

    //<editor-fold defaultstate="collapsed" desc="Getter And Setter">
    public double getDifference() {
        return outstandingBalance;
    }

    public double getCurrentCharge() {
        return currentCharge;
    }

    public void setCurrentCharge(double currentCharge) {
        this.currentCharge = currentCharge;
    }

    public double getMaxAmountInvolved() {
        return maxAmountInvolved;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public double getTotalDebit() {
        return totalDebit;
    }

    public String getAssesmentSummary() {
        return assesmentSummary;
    }

    public String getPeriodOfBalance() {
        return periodOfBalance;
    }

    public int getPaymentUnit() {
        return paymentUnit;
    }

    public void setPaymentUnit(int paymentUnit) {
        this.paymentUnit = paymentUnit;
    }

    public Date getLastDateOfTransactionInDate() {
        return lastDateOfTransactionInDate;
    }

    public void setPaymentDeadline(Date paymentDeadline) {
        this.paymentDeadline = paymentDeadline;
    }
    //</editor-fold>
}

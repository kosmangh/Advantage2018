/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.advantage.ejb.models.EstatePropertiesDistribution;
import com.sabonay.advantage.ejb.models.LedgerSheet;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.ejb.entities.Bills;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.models.BiilsPaidDetail;
import com.sabonay.advantage.ejb.models.BillsOccuStatus;
import com.sabonay.advantage.ejb.models.BillsSheet;
import com.sabonay.advantage.ejb.models.PropertyDetail;
import com.sabonay.advantage.web.pagecontrollers.BillPaymentController;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.formating.ObjectFormat;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin
 */
public class EstatesStaticsRunner {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");

    public static List<BiilsPaidDetail> biilsPaidDetailList(List<EstatePropertyLedger> list, String selectReportType, String selectedMonth) {
        List<BiilsPaidDetail> billsPaidDetailList = new ArrayList<BiilsPaidDetail>();
        BiilsPaidDetail billsPaidDetail = new BiilsPaidDetail();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");

        if (selectReportType.equalsIgnoreCase("Monthly Report")) {
            if (selectedMonth.equals("null")) {
                JsfUtil.addErrorMessage("Please select a month");
            } else {

                for (EstatePropertyLedger epl : list) {
                    Date transDate = epl.getDateOfRecordTransaction();
                    String transMonth = dateFormat.format(transDate);

                    if (selectedMonth.contains(transMonth)) {
                        billsPaidDetail.setOccupantName(epl.getPropertyOccupant().getOccupantName());
                        billsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                        billsPaidDetail.setEstateName(epl.getEstateProperty().getPropertyEstate().getEstateName());
                    }
                    billsPaidDetailList.add(billsPaidDetail);
                    billsPaidDetail = new BiilsPaidDetail();
                }
            }
        }

        return billsPaidDetailList;
    }

    public static List<EstatePropertiesDistribution> preparePropertiesDistribution(PropOccupationType occupationType) {
        List<EstatePropertiesDistribution> distributions = new LinkedList<>();
        Map<String, EstatePropertiesDistribution> map = new LinkedHashMap<>();

        List<Object[]> result = ds.getCustomQry().doSearch(occupationType);

        if (result == null) {
            return distributions;
        }

        for (Object[] obj : result) {
            int propertiesCount = ObjectFormat.getObjectAsInt(obj[0]);
//            String estateInitials = ObjectFormat.getStringObject(obj[1]);
            String estateName = ObjectFormat.getStringObject(obj[1]);
            String blockName = ObjectFormat.getStringObject(obj[2]);
            String estateBockId = ObjectFormat.getStringObject(obj[3]);
            PropertyUsage propertyType = (PropertyUsage) obj[4];

            String key = estateName + "#" + estateBockId;

            EstatePropertiesDistribution proDist = null;

            try {
                proDist = map.get(key);

                if (proDist == null) {
                    proDist = new EstatePropertiesDistribution();
                    proDist.setEstateName(estateName);
                    proDist.setBlockName(blockName);
                    proDist.setEstateBlockId(estateBockId);

                    distributions.add(proDist);
                    map.put(key, proDist);
                }

                if (null != propertyType) switch (propertyType) {
                    case Residential:
                        proDist.setResidentialCount(propertiesCount);
                        break;
                    case Mix_Use:
                        proDist.setMixedUseCount(propertiesCount);
                        break;
                    case Commercial:
                        proDist.setCommercialCount(propertiesCount);
                        break;
                    case Institutional:
                        proDist.setIndustrialCount(propertiesCount);
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                Logger.getLogger(EstatesStaticsRunner.class.getName()).log(Level.SEVERE, e.toString());
            }
        }

        for (EstatePropertiesDistribution propertiesDistribution : distributions) {
            String estateBlockId = propertiesDistribution.getEstateBlockId();
            try {
                int numberOfPsu = ds.getCustomQry().numberOfEstatePSUinEstateBlock(estateBlockId);

                propertiesDistribution.setPsuCount(-numberOfPsu);

            } catch (Exception e) {
                Logger.getLogger(EstatesStaticsRunner.class.getName()).log(Level.SEVERE, e.toString());
            }
        }

        return distributions;
    }

    // <editor-fold defaultstate="collapsed" desc="Run Properties Ledger Sheet">
    public static List<LedgerSheet> prepareEstateLedgerSheet(String reportType, String reportParam, int ledgerYear, PaymentType paymentType) {
        List<LedgerSheet> ledgerSheets = new LinkedList<LedgerSheet>();
        Map<String, LedgerSheet> map = new LinkedHashMap<String, LedgerSheet>();

        List<Object[]> exactYearLedger = ds.getCustomQry().prepareEntireLedgerSheet(ledgerYear, false, paymentType);

        if (exactYearLedger == null) {
            return ledgerSheets;
        }

        for (Object[] obj : exactYearLedger) {

            String estateName = ObjectFormat.getStringObject(obj[0]);
            String blockName = ObjectFormat.getStringObject(obj[1]);
            DebitCredit debitCreditEntry = (DebitCredit) obj[2];
            double amountInvolved = ObjectFormat.getDoubleObject(obj[3]);
            int blockCount = ObjectFormat.getObjectAsInt(obj[4]);
            Date transactionDate = (Date) obj[5];

            String transMonth = dateFormat.format(transactionDate);
            int transDate = transactionDate.getMonth();
            String key = estateName + "#" + blockName;

            LedgerSheet ledgerSheet = null;

            try {
                ledgerSheet = map.get(key);

                if (ledgerSheet == null) {
                    ledgerSheet = new LedgerSheet();
                    ledgerSheet.setEstateName(estateName);
                    ledgerSheet.setBlockName(blockName);

                    ledgerSheets.add(ledgerSheet);
                    map.put(key, ledgerSheet);
                }

//                ledgerSheet.setBlockPropertiesCount(blockCount + ledgerSheet.getBlockPropertiesCount());
                ledgerSheet.setBlockPropertiesCount(blockCount);
                if (debitCreditEntry == DebitCredit.DEBIT) {
                    ledgerSheet.setYearBill(amountInvolved);

                }
                if (reportType.equalsIgnoreCase("Monthly Report")) {
                    if (transMonth.equalsIgnoreCase(reportParam)) {
                        if (debitCreditEntry == DebitCredit.CREDIT) {
                            ledgerSheet.setAmountPaid(amountInvolved);
                        }
                    }
                } else if (reportType.equalsIgnoreCase("Quarterly Report")) {
                    if (reportParam.equalsIgnoreCase("1st Quarter")) {
                        if (transDate <= 2) {
                            if (debitCreditEntry == DebitCredit.CREDIT) {
                                ledgerSheet.setAmountPaid(amountInvolved);
                            }
                        }
                    } else if (reportParam.equalsIgnoreCase("2nd Quarter")) {
                        if (transDate > 2 && transDate < 6) {
                            if (debitCreditEntry == DebitCredit.CREDIT) {
                                ledgerSheet.setAmountPaid(amountInvolved);
                            }
                        }
                    } else if (reportParam.equalsIgnoreCase("3rd Quarter")) {
                        if (transDate > 5 && transDate < 9) {
                            if (debitCreditEntry == DebitCredit.CREDIT) {
                                ledgerSheet.setAmountPaid(amountInvolved);
                            }
                        }
                    } else if (reportParam.equalsIgnoreCase("4th Quarter")) {
                        if (transDate > 8 && transDate < 12) {
                            if (debitCreditEntry == DebitCredit.CREDIT) {
                                ledgerSheet.setAmountPaid(amountInvolved);
                            }
                        }
                    }
                } else if (reportType.equalsIgnoreCase("Mid Year Report")) {
                    if (reportParam.equalsIgnoreCase("1st Mid Year")) {
                        if (transDate <= 5) {
                            if (debitCreditEntry == DebitCredit.CREDIT) {
                                ledgerSheet.setAmountPaid(amountInvolved);
                            }
                        }
                    } else if (reportType.equalsIgnoreCase("2nd Mid Year")) {
                        if (transDate > 5 && transDate < 12) {
                            if (debitCreditEntry == DebitCredit.CREDIT) {
                                ledgerSheet.setAmountPaid(amountInvolved);
                            }
                        }
                    }
                } else if (reportType.equalsIgnoreCase("Annual Report")) {
                    if (debitCreditEntry == DebitCredit.CREDIT) {
                        ledgerSheet.setAmountPaid(amountInvolved);
                    }
                }

            } catch (Exception e) {
                Logger.getLogger(EstatesStaticsRunner.class.getName()).log(Level.SEVERE, e.toString());
            }
        }

        //summing preceding year balance
        List<Object[]> precedingYearBalance = ds.getCustomQry().prepareEntireLedgerSheet(ledgerYear - 1, true, paymentType);

        for (Object[] obj : precedingYearBalance) {

            String estateName = ObjectFormat.getStringObject(obj[0]);
            String blockName = ObjectFormat.getStringObject(obj[1]);
            DebitCredit debitCreditEntry = (DebitCredit) obj[2];
            double amountInvolved = ObjectFormat.getDoubleObject(obj[3]);
//            int blockCount = ObjectFormat.getObjectAsInt(obj[4]);

            String key = estateName + "#" + blockName;

            LedgerSheet ledgerSheet = null;

            try {
                ledgerSheet = map.get(key);

                if (ledgerSheet == null) {
                    ledgerSheet = new LedgerSheet();
                    ledgerSheet.setEstateName(estateName);
                    ledgerSheet.setBlockName(blockName);

                    ledgerSheets.add(ledgerSheet);
                    map.put(key, ledgerSheet);
                }

                if (debitCreditEntry == DebitCredit.DEBIT) {
                    ledgerSheet.setDebitBalance(amountInvolved);

                } else if (debitCreditEntry == DebitCredit.CREDIT) {
                    ledgerSheet.setCreditBalance(amountInvolved);
                }

            } catch (Exception e) {
                Logger.getLogger(EstatesStaticsRunner.class.getName()).log(Level.SEVERE, e.toString());
            }
        }

        return ledgerSheets;
    }

    public static List<PropertyDetail> prepareEstatePropertyDetail(int ledgerYear, PaymentType paymentType, Estate selectedEstate) {
        List<PropertyDetail> pptDetailSheets = new LinkedList<>();
        Map<String, PropertyDetail> map = new LinkedHashMap<>();
        List<Object[]> exactYearLedger = ds.getCustomQry().prepareDetailEstateProperty(ledgerYear, paymentType, false, selectedEstate);
        if (exactYearLedger == null) {
            return pptDetailSheets;
        }
        for (Object[] results : exactYearLedger) {
            String blockName = ObjectFormat.getStringObject(results[0]);
            DebitCredit debitCreditEntry = (DebitCredit) results[1];
            double amtTotal = ObjectFormat.getDoubleObject(results[2]);
            String propertyNumber = ObjectFormat.getStringObject(results[4]);
            String estatePropertyId = ObjectFormat.getStringObject(results[3]);
            String estateName = ObjectFormat.getStringObject(results[5]);

            String key = blockName + "#" + propertyNumber;
            PropertyDetail propertyDetail = null;
            try {
                propertyDetail = map.get(key);
                if (propertyDetail == null) {
                    propertyDetail.setBlockName(blockName);
                    propertyDetail.setPropertyNumber(propertyNumber);
                    propertyDetail.setEstateName(estateName);
                    propertyDetail.setPropertyNumber(propertyNumber);
                    propertyDetail.setEstatePropertyId(estatePropertyId);

                    if (debitCreditEntry == DebitCredit.DEBIT) {
                        propertyDetail.setTotalDebit(amtTotal);
                    }
                    if (debitCreditEntry == DebitCredit.CREDIT) {
                        propertyDetail.setTotalCredit(amtTotal);
                    }
                    pptDetailSheets.add(propertyDetail);
                    map.put(key, propertyDetail);
                }
            } catch (Exception e) {
            }
        }

        return pptDetailSheets;
    }
    
    
    //NEW QUERIES ON BILLS
    
    public static List<BillsSheet> preparePaidBillsSheet(String status){
         List<BillsSheet> billsSheets = new LinkedList<BillsSheet>();
        Map<String, BillsSheet> map = new LinkedHashMap<String, BillsSheet>();
        
       // BillPaymentController billPaymentController = BillPaymentController;
        
          List<Object[]> paidBillsLedger = ds.getCommonQry().prepareEntirePaidBillsSheet(status);
      //  List<Object[]> paidBillsLedger = ds.getCommonQry().prepareEntirePaidBillsSheet(status);
        
        if(paidBillsLedger==null){
            return billsSheets ;
        }
        
        for(Object[] object : paidBillsLedger){
            
            String estateProperty = ObjectFormat.getStringObject(object[0]);
            String propertyOccupant = ObjectFormat.getStringObject(object[1]);
            double billAmountPaid = ObjectFormat.getDoubleObject(object[2]);
           // double sumOfAmountPaid = ObjectFormat.getDoubleObject(object[3]);
           // int estateCount = ObjectFormat.getObjectAsInt(object[4]);
            //String dateOfRecordEntry = (String) object[5];
            
            String key = propertyOccupant + "#" + estateProperty;
            
            BillsSheet billSheet = null;
            
            try {
                billSheet = map.get(key);
                
                if(billSheet == null){
                    billSheet = new BillsSheet();
                    billSheet.setPropertyOccupant(propertyOccupant);
                    billSheet.setBillAmountPaid(billAmountPaid);
                    billSheet.setEstateProperty(estateProperty);
                    //billSheet.setDateOfRecordEntry(dateOfRecordEntry);
                   // billSheet.setSumOfAmountPaid(sumOfAmountPaid);
                   // billSheet.setEstateCount(estateCount);
                    billsSheets.add(billSheet);
                    map.put(key, billSheet);
                    
                    
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        return billsSheets;
        
        
    }
    
    
    
       
    public static List<BillsOccuStatus> preparePaidBillsPerOccu(String res){
         List<BillsOccuStatus> billsSheets = new LinkedList<BillsOccuStatus>();
     
        
          List<Bills> paidBillsLedger = ds.getCommonQry().load_bill_report_of_occupant(res);
        System.out.println("Occupant Details Of Bill Status>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>." + paidBillsLedger);
        
        if(paidBillsLedger==null){
            return billsSheets ;
        }
        
        for(Bills object : paidBillsLedger){
            BillsOccuStatus billsOccuStatus = new BillsOccuStatus();
            
            billsOccuStatus.setBillDetail(object.getBillDetail());
            billsOccuStatus.setBillType(object.getBillType());
            billsOccuStatus.setBillYear(object.getBillYear());
            billsOccuStatus.setBillAmount(object.getBillAmount());
            billsOccuStatus.setBillAmountPaid(object.getBillAmountPaid());
            billsOccuStatus.setBillStatus(object.getBillStatus());
            
            
            billsSheets.add(billsOccuStatus);
            
           // String billDetail = object.getBillDetail();
           // String billType = object.getBillType();
           // int billYear = object.getBillYear();
           // double billAmount = object.getBillAmount();
           //  double billAmountPaid = object.getBillAmountPaid();
            // String billStatus = object.getBillStatus();
            
            
//            BillsOccuStatus billSheet = null;
//            
//            try {
//               
//                
//                if(billSheet == null){
//                    billSheet = new BillsOccuStatus();
//                    billSheet.setBillDetail(billDetail);
//                    billSheet.setBillType(billType);
//                    
//                    billSheet.setBillYear(billYear);
//                    billSheet.setBillAmount(billAmount);
//                    
//                   billSheet.setBillAmountPaid(billAmountPaid);
//                   
//                    billSheet.setBillStatus(billStatus);
//                    billsSheets.add(billSheet);
//                    
//                    
//                    
//                    
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            
        }
        
        return billsSheets;
        
        
    }
    
    
    // </editor-fold>
}

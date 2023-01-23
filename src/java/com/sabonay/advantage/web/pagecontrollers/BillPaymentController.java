/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.utils.EstatesStaticsRunner;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.BillPayment;
import com.sabonay.advantage.ejb.entities.Bills;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.GroundRent;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.AccountStatementDetail;
import com.sabonay.advantage.ejb.models.BillsSheet;
import com.sabonay.advantage.modules.account.AdvantageUserData;
import com.sabonay.advantage.web.reports.avReportManager;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@Named(value = "billPaymentController")
@SessionScoped
public class BillPaymentController implements Serializable {

    private static final Logger LOG = Logger.getLogger(BillPaymentController.class.getName());

    SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
    private PaymentType selectedPaymentType;
    private EstateProperty selectedEstateProperty;
    private PaymentType paymentType;
    private PropertyOccupant selectedPropertyOccupant;
    private Occupancy selectedOccupancy;
    private GroundRent groundRent;
    private String propertyOccupantName;
    private String propertyNumber;
    private String receiptNumber;
    private String payeeName;
    private String mediumOfPayment;
    private String mediumOfPaymentNumber;
    private Date dateOfRecordTransaction;
    private int currentYear = DateTimeUtils.getCurrentYear();
    private int selectedYear;
    private Date transactionFrom = null;
    private Date transactionTo = null;
    Calendar calendar = Calendar.getInstance();
    List<EstatePropertyLedger> estatePropertyList;
    private double totalCreditEntry;
    private double totalDebitEntry;
    private double outstandingBalance;
    private EstatePropertyLedger estatePropertyLedger = new EstatePropertyLedger();
    private BillPayment billPayment = new BillPayment();
    
    private Bills bills = new Bills();
    private EstatePropertyLedger payingForEstatePropertyLedger;
//    private EstatePropertyLedgerTableModel estatePropertyLedgerTableModel = new EstatePropertyLedgerTableModel();
//    @DataTableModelList(group = "epl")
    private List<EstatePropertyLedger> estatePropertyLedgerList = null;
    
    
    
     
   
    private List<EstatePropertyLedger> estateBillLedgerList = null;
//    @DataPanel(group = "epl")
//    private HtmlDataPanel<EstatePropertyLedger> estatePropertyLedgerDataPanel = null;
//    @FormControl(group = "epl")
//    private HtmlFormControl billPaymentFormControl = new HtmlFormControl();
    private DataModel<EstatePropertyLedger> estatePropertyLedgerDataModel;
    private boolean rendered = false;
    private int backTrackYear;
    private double amtInvolved;   
    private boolean canView = false;
    private String searchItem, searchString;
    private EstatePropertyLedger billCorrectionLedger = new EstatePropertyLedger();
    
    
    
    //NEW CODES FOR BILLS
    
    private List<Bills> unpaidBillsList = new ArrayList<Bills>();
    private DataModel<Bills> unpaidBillsListDataModel;
    
      private List<Bills> unpaidBillsList_by_occupant = new ArrayList<Bills>();
    
      private DataModel<Bills> unpaidBillsListDataMode_by_occupant;
    
    
    
    private int countPaidOcc;
    private double sumPaidOcc;
    
    private String bill_status = null;

    public BillPaymentController() {
//        estatePropertyLedger.setPaymentFor("Bill payment");
//        estatePropertyLedgerDataPanel = estatePropertyLedgerTableModel.getDataPanel();
//
//        estatePropertyLedgerDataPanel.autoBindAndBuild(BillPaymentController.class, "epl");
//        billPaymentFormControl.autoBindAndBuild(BillPaymentController.class, "epl");
    }

    @PostConstruct
    public void init() {
        
        //unpaidBillsList = new ArrayList<Bills>();
        
       // unpaidBillsList = ds.getCommonQry().load_all_unpaid_bills();
        
       // unpaidBillsListDataModel = new ListDataModel<Bills>(unpaidBillsList);
        
        
    }

    public String saveButtonAction() {
        if (estatePropertyLedger == null) {
            JsfUtil.addErrorMessageAndRespond("Error Occured ....");
            return null;
        }

        if (selectedEstateProperty == null) {
            JsfUtil.addErrorMessageAndRespond("Cannot do bill payment with no estate property selected");
            return null;
        }

        String created = null;
        
        String createdBillEntry = null;
                
        double backTrackSum = 0.0;
        double backTrackAmt = 0.0;
        double debitEntry = 0.0;
        double creditEntry = 0.0;
        double outstandingBal = 0.0;
        Integer firstLedgerYr = ds.getCustomQry().getFirstEstateProperyLedgeryear(selectedEstateProperty);
//        try {
//            if (getAmtInvolved() <= 0.00) {
//                JsfUtil.addErrorMessage("Amount cannot be less than 0.00 GHS");
//            }
//            backTrackAmt = amtInvolved;
//            for (int i = firstLedgerYr; i <= currentYear; i++) {
//                debitEntry = ds.getCustomQry().getLedgerByYearPaidFor(selectedEstateProperty, i, DebitCredit.DEBIT);
//                creditEntry = ds.getCustomQry().getLedgerByYearPaidFor(selectedEstateProperty, i, DebitCredit.CREDIT);
//                double yearBal = creditEntry - debitEntry;
//                List<EstatePropertyLedger> pptyLedger = ds.getCustomQry().loadLedgerEntriesForSelectedYear(selectedEstateProperty, i);
//                for (EstatePropertyLedger epl : pptyLedger) {
//                    if ((yearBal < debitEntry) && (backTrackAmt <= debitEntry) && (backTrackAmt > 0.0)) {
//                        epl.setDebitCreditEntry(DebitCredit.CREDIT);
//                        epl.setLedgerYear(i);
//                        epl.setAmountInvolved(backTrackAmt);
//                        epl.setPaymentFor("Ground Rent Payment, " + i);
//                        epl.setEstateProperty(selectedEstateProperty);
//                        epl.setPaymentType(selectedPaymentType);
//                        epl.setPropertyOccupant(selectedPropertyOccupant);
//                        epl.setDateOfRecordEntry(new Date());
//                        epl.setReceiptNumberIssued(receiptNumber);
//                        epl.setPayeeName(payeeName);
//                        epl.setDateOfRecordTransaction(dateOfRecordTransaction);
//                        epl.setRecordedBy(AdvantageUserData.getManagedInstance().getUserId());
//
//                        IDCreator.setEstateLedgerId(epl);
//                        created = ds.getCommonQry().estatePropertyLedgerCreate(epl);
//                        backTrackAmt = +yearBal;
//                    } else if ((backTrackAmt > debitEntry) && (yearBal > debitEntry) && (backTrackAmt > 0.0)) {
//                        epl.setDebitCreditEntry(DebitCredit.CREDIT);
//                        epl.setLedgerYear(i);
//                        epl.setAmountInvolved(backTrackAmt);
//                        epl.setPaymentFor("Ground Rent Payment, " + i);
//                        epl.setEstateProperty(selectedEstateProperty);
//                        epl.setPaymentType(selectedPaymentType);
//                        epl.setPropertyOccupant(selectedPropertyOccupant);
//                        epl.setDateOfRecordEntry(new Date());
//                        epl.setReceiptNumberIssued(receiptNumber);
//                        epl.setPayeeName(payeeName);
//                        epl.setDateOfRecordTransaction(dateOfRecordTransaction);
//                        epl.setRecordedBy(AdvantageUserData.getManagedInstance().getUserId());
//
//                        IDCreator.setEstateLedgerId(epl);
//                        created = ds.getCommonQry().estatePropertyLedgerCreate(epl);
//                    }
//
//                }
//            }
//            if (created != null) {
//                JsfUtil.addInformationMessage("Payment completed  sucessfully ");
//            } else {
//                JsfUtil.addErrorMessage("Failed to complete payment process");
//            }
//        } catch (Exception e) {
//            Logger.getLogger(EstatePropertyLedger.class.getName()).log(Level.SEVERE, e.toString(), e);
//            JsfUtil.addErrorMessage("Error occured: failed to complete payment process");
//        }
        try {
            if (getAmtInvolved() <= 0.00) {
                JsfUtil.addErrorMessage("Amount cannot be less than 0.00 GHS");
            }
//            backTrackAmt = amtInvolved;
//            backTrackYear = currentYear;
//            while ((firstLedgerYr <= backTrackYear) && (backTrackAmt > 0.00)) {
//                debitEntry = ds.getCustomQry().getLedgerByYearPaidFor(selectedEstateProperty, firstLedgerYr, DebitCredit.DEBIT);
//                creditEntry = ds.getCustomQry().getLedgerByYearPaidFor(selectedEstateProperty, firstLedgerYr, DebitCredit.CREDIT);
//                List<EstatePropertyLedger> pptyLedger = ds.getCustomQry().loadLedgerEntriesForSelectedYear(selectedEstateProperty, firstLedgerYr);
//                outstandingBal = debitEntry - creditEntry;
//                for (EstatePropertyLedger epl : pptyLedger) {
//                    if (outstandingBal > 0.0 && backTrackAmt <= outstandingBal) {
//                        epl.setDebitCreditEntry(DebitCredit.CREDIT);
//                        epl.setLedgerYear(firstLedgerYr);
//                        epl.setAmountInvolved(backTrackAmt);
//                        epl.setPaymentFor("Ground Rent Payment, " + firstLedgerYr);
//                        epl.setEstateProperty(selectedEstateProperty);
//                        epl.setPaymentType(selectedPaymentType);
//                        epl.setPropertyOccupant(selectedPropertyOccupant);
//                        epl.setDateOfRecordEntry(new Date());
//                        epl.setReceiptNumberIssued(receiptNumber);
//                        epl.setPayeeName(payeeName);
//                        epl.setDateOfRecordTransaction(dateOfRecordTransaction);
//                        epl.setRecordedBy(AdvantageUserData.getManagedInstance().getUserId());
//
//                        IDCreator.setEstateLedgerId(epl);
//                        created = ds.getCommonQry().estatePropertyLedgerCreate(epl);
//                    } else if (outstandingBal > 0.0 && backTrackAmt > outstandingBal) {
//                        
//                        epl.setDebitCreditEntry(DebitCredit.CREDIT);
//                        epl.setLedgerYear(firstLedgerYr);
//                        epl.setAmountInvolved(outstandingBal);
//                        epl.setPaymentFor("Ground Rent Payment, " + firstLedgerYr);
//                        epl.setEstateProperty(selectedEstateProperty);
//                        epl.setPaymentType(selectedPaymentType);
//                        epl.setPropertyOccupant(selectedPropertyOccupant);
//                        epl.setDateOfRecordEntry(new Date());
//                        epl.setReceiptNumberIssued(receiptNumber);
//                        epl.setPayeeName(payeeName);
//                        epl.setDateOfRecordTransaction(dateOfRecordTransaction);
//                        epl.setRecordedBy(AdvantageUserData.getManagedInstance().getUserId());
//
//                        IDCreator.setEstateLedgerId(epl);
//                        created = ds.getCommonQry().estatePropertyLedgerCreate(epl);
//                    }
//                }
//                firstLedgerYr = +1;
//                backTrackAmt = -outstandingBal;
//            }

            estatePropertyLedger.setAmountInvolved(amtInvolved);
            estatePropertyLedger.setDebitCreditEntry(DebitCredit.CREDIT);
            estatePropertyLedger.setLedgerYear(currentYear);
            estatePropertyLedger.setPaymentFor(selectedPaymentType + " Payment, " + currentYear);
            estatePropertyLedger.setEstateProperty(selectedEstateProperty);
            estatePropertyLedger.setPaymentType(selectedPaymentType);
            estatePropertyLedger.setPropertyOccupant(selectedPropertyOccupant);
            estatePropertyLedger.setReceiptNumberIssued(receiptNumber);
            setPayeeName(selectedPropertyOccupant.getOccupantName());
            estatePropertyLedger.setPayeeName(payeeName);
            estatePropertyLedger.setDateOfRecordTransaction(dateOfRecordTransaction);
            estatePropertyLedger.setRecordedBy(AdvantageUserData.getManagedInstance().getUserId());
            estatePropertyLedger.setDateOfRecordEntry(new Date());
            IDCreator.setEstateLedgerId(estatePropertyLedger, null);
            
            String getIDCREATORforBill = IDCreator.setEstateLedgerId1(estatePropertyLedger, null);
            
            billPayment.setBillPaymentId(getIDCREATORforBill);
            billPayment.setPropertyOccupant(selectedPropertyOccupant.getPropertyOccupantId());
            billPayment.setReceiptNumber(receiptNumber);
            billPayment.setAmountInvolved(amtInvolved);
          
            

//            UPADATING THE BILLS TABLE
            
//             List<Bills> listunpaidOrpart;
//            
//            double total_amount_owing = 0.0;
//             int yearr = 0;
//            
//            String res = selectedPropertyOccupant.getPropertyOccupantId();
//            
//            double total_amount_paid_by_occupant = ds.getCommonQry().get_bill_amount_paid_by_occu_naitive(res);
//            
//            double total_amount_owing_plus_now_paying = total_amount_paid_by_occupant + amtInvolved;
//            
//            listunpaidOrpart = ds.getCommonQry().load_all_unpaid_part_occu(res);
//            
//          
//            System.out.println("List Of Unpaid Or Part>>>>>>>>>>>>>>>>>>>>>>>>>>..." + listunpaidOrpart);
//            
//            Iterator iterator = listunpaidOrpart.iterator();
//            Bills bills;
//            while (iterator.hasNext()) {
//                bills = (Bills)iterator.next();
//                 if(total_amount_owing_plus_now_paying < bills.getBillAmount()){
//                     
//                     System.out.println("Get Bill Amount>>>>>>>>>>>>>>>>>>>." + bills.getBillAmount());
//                      System.out.println("Bill Year>>>>>>>>>>>>>>>>>>>." + bills.getBillYear());
//                      
//                      System.out.println("Bill Detail>>>>>>>>>>>>>>>>......" + bills.getBillDetail());
//                      
//                      System.out.println("Bill Status>>>>>>>>>>>>>>>>>>>>>>............" + bills.getBillStatus());
//                      
//                      
//                    ds.getCommonQry().update_occ_status_to_part(res, total_amount_owing_plus_now_paying,bills.getBillYear()); 
//                 }else if(total_amount_owing_plus_now_paying == bills.getBillAmount()){
//                    ds.getCommonQry().update_occ_status_to_full_status(res, bills.getBillAmount(),bills.getBillYear()); 
//                 }else{
//                   ds.getCommonQry().update_occ_status_to_full_status(res, bills.getBillAmount(),bills.getBillYear());
//                   double overpaid = total_amount_owing_plus_now_paying - bills.getBillAmount();
//                   if(overpaid > 0) {
//                       
//                       
//                       int getNextYear = bills.getBillYear() + 1;
//                        int year_current = Calendar.getInstance().get(Calendar.YEAR);
//                        
//                         double billAmountForNextYear = ds.getCommonQry().amount_unpaid_part_occu(res, getNextYear);
//                         
//                         double reamain = overpaid - billAmountForNextYear;
//                         
//                         System.out.println("Print Out Remain Before Deducting>>>>>>>>>>>>>>>..." + reamain);
//                         
//                         if (reamain  > billAmountForNextYear) {                           
//                            
//                             ds.getCommonQry().update_occ_status_to_full_status(res, billAmountForNextYear, getNextYear);
//                             
//                             do {   
//                                 
//                                 getNextYear = getNextYear + 1;
//                                  double getNextYearPlusPlusBillAmount = ds.getCommonQry().amount_unpaid_part_occu(res, getNextYear);
//                                   reamain = reamain - getNextYearPlusPlusBillAmount;
//                                   ds.getCommonQry().update_occ_status_to_full_status(res, getNextYearPlusPlusBillAmount, getNextYear);
//                                 
//                                   System.out.println("Print Out The Remaining Whiles Deducting/After Deducting" + reamain);
//                                   
//                             } while (reamain > billAmountForNextYear);
//                             
//                             
//                             //double reaming_deduct_balance = reamain;
//                             //update to the current_year looping.....
//                             
//                             System.out.println("Remain After Deducting..........>>>>>>>>>>>>>" + reamain);
//                            
//                             
//                       
//                             
//                             
//                             
//                             
//                             
//                             
//                       }else{
//                             ds.getCommonQry().update_occ_status_to_part(res, overpaid, getNextYear);
//                         }
//                         
//                        
//                   
//                       
//                       
//                       System.out.println("Overpaid>>>>>>>>>>>>>>>.." + overpaid);
//                       
//                       System.out.println("Check If is next year>>>>>>>>>>>>>>>>>>>>>>>>>>>......" +getNextYear);
//                       
//                       System.out.println("Check amount for next year>>>>>>>>>>>>>>>>>>>>>>...." + bills.getBillAmount());
//                   
//                   }
//                 }
//                
//                
//            }
//            
//            
            
            
            
            
            
            
            
            
            
            
            
            
            
           
            
//            for(int i= 0; i<=listunpaidOrpart.size(); i++){
//                
//                for(Bills bills : listunpaidOrpart){
//                    
//                        if(total_amount_owing_plus_now_paying < bills.getBillAmount()){
//                    ds.getCommonQry().update_occ_status_to_part(res, total_amount_owing_plus_now_paying);
//                }else if (total_amount_owing_plus_now_paying == bills.getBillAmount()) {
//                     ds.getCommonQry().update_occ_status_to_full_status(res, bills.getBillAmount());
//                }else{
//                    
//                    ds.getCommonQry().update_occ_status_to_full_status(res, bills.getBillAmount());
//                    
//                    double overpaid = total_amount_owing_plus_now_paying - bills.getBillAmount();
//                    
//                    if(overpaid > 0){
//                        for(Bills bills1 : listunpaidOrpart){
//                            if(total_amount_owing_plus_now_paying < bills1.getBillAmount()){
//                                ds.getCommonQry().update_occ_status_to_part(res, overpaid);
//                            }
//                        }
//                    }
//                   
//                   
//                    
//                }
//                 
//                  System.out.println("Print amount Owing list..............." + bills.getBillAmount());  
//                }
//                 }
            
//            double total_amount_owing;
//            
//            total_amount_owing = ds.getCommonQry().get_total_owings_of_occupants(res);
//            
//            double total_amount_paid_by_occupant = ds.getCommonQry().get_bill_amount_paid_by_occu(res);
//           
//            double amount_paying_and_total_owing = total_amount_paid_by_occupant + amtInvolved;
//            
//            if(amount_paying_and_total_owing < total_amount_owing){
//                
//                ds.getCommonQry().upadate_occ_status_to_part(res, amount_paying_and_total_owing);
//                
//            }else{
//                double credit_balance = amount_paying_and_total_owing - total_amount_owing;
//                
//                ds.getCommonQry().upadate_occ_status_to_full_status(res, amount_paying_and_total_owing);
//                
//                ds.getCommonQry().upadate_occ_status_to_discount(res, credit_balance);
//                
//                
//            }
            
            
            
 
            
            ds.getCommonQry().estatePropertyLedgerCreate(estatePropertyLedger);
            ds.getCommonQry().record_for_bill_payments(billPayment);
            
            JsfUtil.addInformationMessage("Payment completed  sucessfully");
              
            
        //   System.out.println(">>>>>Before transact: Occupant" + res + ">>>>>>>Amount>>>>> Owing Plus Now Paying" + total_amount_owing_plus_now_paying +  "Bill Year" + yearr );
         //  System.out.println(">>>>>Before transact: Occupant" + res + ">>>>>>>Amount>>>>>Amount paid" + total_amount_paid_by_occupant  );
//           System.out.println(">>>>>Before transact: Occupant" + res + ">>>>>>>Amount>>>>>Accumlated" + amount_paying_and_total_owing  );
//              
 
            
          
        } catch (Exception e) {
            e.printStackTrace();
        }

        clearButtonAction();
        loadSelectedEstatePropertyLedger();
        return null;

    }

    public String editButtonAction() {
        try {
            boolean updated = ds.getCommonQry().estatePropertyLedgerUpdate(estatePropertyLedger);

            if (updated == true) {
                JsfUtil.addInformationMessage("payment record  updated sucessfully ");
            } else if (updated == false) {
                JsfUtil.addErrorMessage("Unable to update payment record");
                return null;
            }
        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyLedger.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Failed: Unable to update payment record");
        }
        return null;
    }

    public String clearButtonAction() {
        try {
            estatePropertyLedger = new EstatePropertyLedger();
            selectedEstateProperty = new EstateProperty();
            dateOfRecordTransaction = null;
            receiptNumber = null;
            payeeName = null;
            mediumOfPayment = null;
            totalCreditEntry = 0.0;
            totalDebitEntry = 0.0;
            outstandingBalance = 0.0;
            propertyNumber = "";
            amtInvolved = 0.00;
            propertyOccupantName = "";
            estatePropertyLedgerList = null;

            //rendered = false;
        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyLedger.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Error occurred in clearing EstatePropertyLedger form ");
        }

        return null;

    }

    public String deleteButtonAction() {
        try {
            if (estatePropertyLedger == null) {
                return null;
            }

            boolean deleted = ds.getCommonQry().estatePropertyLedgerDelete(estatePropertyLedger, false);

            if (deleted == true) {
                clearButtonAction();
            } else {
                JsfUtil.addErrorMessage("Failed to Delete EstatePropertyLedger");
                return null;
            }

        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyLedger.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Failed to Delete EstatePropertyLedger");
        }

        return null;
    }

    public String estatePropertyLedgerDataTableRowSelectionAction() {
        try {
            estatePropertyLedger = (EstatePropertyLedger) estatePropertyLedgerDataModel.getRowData();
            if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.CREDIT) {
                canView = true;
            } else {
                canView = false;
            }

            payingForEstatePropertyLedger = new EstatePropertyLedger();
            payingForEstatePropertyLedger.setYearPaidFor(estatePropertyLedger.getLedgerYear());
            payingForEstatePropertyLedger.setPaymentFor(estatePropertyLedger.getPaymentFor());
            payingForEstatePropertyLedger.setPayeeName(estatePropertyLedger.getPayeeName());
            payingForEstatePropertyLedger.setEstateProperty(estatePropertyLedger.getEstateProperty());
            payingForEstatePropertyLedger.setAmountInvolved(estatePropertyLedger.getAmountInvolved());
            payingForEstatePropertyLedger.setDateOfRecordEntry(estatePropertyLedger.getDateOfRecordEntry());
            payingForEstatePropertyLedger.setReceiptNumberIssued(estatePropertyLedger.getReceiptNumberIssued());
            selectedEstateProperty = estatePropertyLedger.getEstateProperty();
            amtInvolved = estatePropertyLedger.getAmountInvolved();
//            if (estatePropertyLedger != null) {
//                if (estatePropertyLedger.getPayeeName().equalsIgnoreCase("SHC")) {
//                    estatePropertyLedger = new EstatePropertyLedger();
//                    JsfUtil.addErrorMessageAndRespond("Cannot not edit records with payee name SHC");
//                    return null;
//                }
//            }
            rendered = true;

        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyLedger.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Error occurred in selecting EstatePropertyLedger from table ");
        }

        return null;
    }

//    public String estatePropertyLedgerDataTableSearchButtonAction() {
//        try {
//            String searchCriteria = estatePropertyLedgerDataPanel.getSearchCriteria();
//            String searchText = estatePropertyLedgerDataPanel.getSearchText();
//
//            estatePropertyLedgerList = ds.getCommonQry().estatePropertyLedgerFindByAttribute(searchCriteria, searchText, "STRING", true);
//        } catch (Exception exp) {
//            Logger.getLogger(EstatePropertyLedger.class.getName()).log(Level.SEVERE, exp.toString(), exp);
//            JsfUtil.addErrorMessage("Error occurred in selecting EstatePropertyLedger from table ");
//        }
//
//        return null;
//    }
    public void loadSelectedEstatePropertyLedger() {
        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();

        if (selectedEstateProperty == null) {
            String msg = "Please there is no selected property";
            JsfUtil.addInformationMessage(msg);
            return;
        }

        selectedPropertyOccupant = PropertySelector.getMgedInstance().selectedPropertyOccupant();

        System.out.println("property occupant is " + selectedPropertyOccupant);

        if (selectedPropertyOccupant == null) {
            JsfUtil.addErrorMessageAndRespond("Selected Property is not occupied at this time ");
            clearButtonAction();
            return;
        }

        try {
            selectedPaymentType = PropertySelector.getMgedInstance().getPropertyOccupationDetails().getAppropriatePaymentType();
        } catch (Exception e) {
        }
        rendered = true;

        propertyOccupantName = selectedPropertyOccupant.getOccupantName();
        estatePropertyLedger.setPayeeName(propertyOccupantName);

        selectedOccupancy = PropertySelector.getMgedInstance().getPropertyOccupationDetails();

        estatePropertyLedgerList = ds.getCustomQry().loadLedgerEntriesForPropertyOfCurrentOccupant(selectedEstateProperty, currentYear);
        estatePropertyLedgerDataModel = new ListDataModel<>(estatePropertyLedgerList);

        if (estatePropertyLedgerList == null) {
            JsfUtil.addErrorMessageAndRespond("There is no record. An error may have been occured");
            clearButtonAction();
            return;
        }

        totalCreditEntry = 0.0;
        totalDebitEntry = 0.0;
        outstandingBalance = 0.0;

        estatePropertyLedger.setPaymentFor(selectedPaymentType.getPaymentTypeName() + " Payment, " + currentYear);
        estatePropertyLedger.setPayeeName(selectedPropertyOccupant.getOccupantName());
        for (EstatePropertyLedger epl : estatePropertyLedgerList) {
            if (epl.getDebitCreditEntry() == DebitCredit.DEBIT) {
                totalDebitEntry += epl.getAmountInvolved();
            } else if (epl.getDebitCreditEntry() == DebitCredit.CREDIT) {
                totalCreditEntry += epl.getAmountInvolved();
            }
        }

        outstandingBalance = totalCreditEntry - totalDebitEntry;
        
         
       //  bills.setAmountLeft(outstandingBalance);

    }

    public void effectPaymentReversal() {

    }

    public void searchEstatePropertLedger() {
        try {
            List<EstatePropertyLedger> creditEntryList = new ArrayList<>();
            estatePropertyLedgerList = ds.getCommonQry().estatePropertyLedgerFindByAttribute(searchItem, searchString, "STRING", false);

            for (EstatePropertyLedger ep : estatePropertyLedgerList) {
                if (ep.getDebitCreditEntry() == DebitCredit.CREDIT) {
                    creditEntryList.add(ep);
                }
            }
            estatePropertyLedgerDataModel = new ListDataModel<>(creditEntryList);
        } catch (Exception e) {

        }
    }

    public void loadSelectedLedgerForCorrection() {
        try {
            billCorrectionLedger = (EstatePropertyLedger) estatePropertyLedgerDataModel.getRowData();
            canView = true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    public boolean updateSelectedPayment() {
        try {
            billCorrectionLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            billCorrectionLedger.setReceiptNumberIssued(billCorrectionLedger.getReceiptNumberIssued());
            billCorrectionLedger.setAmountInvolved(billCorrectionLedger.getAmountInvolved());
            IDCreator.setEstateLedgerId(billCorrectionLedger, null);
            ds.getCommonQry().estatePropertyLedgerCreate(billCorrectionLedger);
            JsfUtil.addInformationMessage("Selected payment reversed successfully");
            billCorrectionLedger = new EstatePropertyLedger();

            canView = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Failed to correct selected payment");
            return false;
        }
    }

    public String deleteSelectedPayment() {
        try {
            if (billCorrectionLedger == null) {
                JsfUtil.addErrorMessage("Please select a ledger");
                return null;
            }
            boolean deleted = ds.getCommonQry().estatePropertyLedgerDelete(billCorrectionLedger, false);
            if (deleted == true) {
                JsfUtil.addInformationMessage("Selected payment deleted successfully");
                billCorrectionLedger = new EstatePropertyLedger();
                List<EstatePropertyLedger> creditEntryList = new ArrayList<>();
                estatePropertyLedgerList = ds.getCommonQry().estatePropertyLedgerFindByAttribute(searchItem, searchString, "STRING", false);

                for (EstatePropertyLedger ep : estatePropertyLedgerList) {
                    if (ep.getDebitCreditEntry() == DebitCredit.CREDIT) {
                        creditEntryList.add(ep);
                    }
                }
//                estatePropertyLedgerList.stream().filter((ep) -> (ep.getDebitCreditEntry() == DebitCredit.CREDIT)).forEach((ep) -> {
//                    creditEntryList.add(ep);
//                });
                estatePropertyLedgerDataModel = new ListDataModel<>(creditEntryList);
                canView = false;

            } else {
                JsfUtil.addErrorMessage("Failed to delete selected payment");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public void loadAllUnpaidBills(){
        
        
          countPaidOcc = ds.getCommonQry().count_paid_bills();
          sumPaidOcc = ds.getCommonQry().total_paid_bills(bill_status);
        
        
        try {
           unpaidBillsList = new ArrayList<>();
            unpaidBillsList = ds.getCommonQry().load_all_paid_bills(bill_status); 
            System.out.println("list of bills" + unpaidBillsList);
            unpaidBillsListDataModel = new ListDataModel<>(unpaidBillsList);
            
            if(unpaidBillsList==null){
                JsfUtil.addErrorMessage("No record Found,An Error Occured");
                return;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
      public void printReportForSuchOccupant(){
          
          
      avReportManager.getInstance().addParam("billStatus", bill_status);    
            
      List<BillsSheet> listofallsheet = EstatesStaticsRunner.preparePaidBillsSheet(bill_status);  
      
        avReportManager.getInstance().showReport(listofallsheet, avReportManager.getInstance().BILLS_PAID_LEDGER_SHEET);
    }
    
       
    public void loadAllUnpaidBills_by_occupant(){
        
            
        
         // selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        
             String res = selectedPropertyOccupant.getPropertyOccupantId();
             
             String estate = selectedEstateProperty.getEstatePropertyId();
        
        
          //countPaidOcc = ds.getCommonQry().count_paid_bills();
          //sumPaidOcc = ds.getCommonQry().total_paid_bills(bill_status);
             
        
        
        try {
            
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>..RES" + res);
            
//           unpaidBillsList_by_occupant = new ArrayList<Bills>();
         //   unpaidBillsList_by_occupant = ds.getCommonQry().load_status_of_occu(selectedEstateProperty); 
//            System.out.println("list of bills_status>>>>>>>>>>>>>>>>>>>>>>>>....." + unpaidBillsList_by_occupant);
//            
//            System.out.println(">>>>>>>>>>>>>>>...Selected res>>>>>>>>>>>>>>>>>>>>>>>>>." + res);
//            
//            System.out.println(">>>>>>>>>>>>>>>>..Selected estate>>>>>>>>>>>>>>>>>>>>>>>..." + selectedEstateProperty);
//            unpaidBillsListDataMode_by_occupant = new ListDataModel<Bills>(unpaidBillsList_by_occupant);
//            
//            if(unpaidBillsList_by_occupant==null){
//                JsfUtil.addErrorMessage("No record Found,An Error Occured");
//                return;
//            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
  

    //<editor-fold defaultstate="collapsed" desc="For Lessee Statement of Account">
//    public void loadSelectedEstateBillsPaidLedger() {
//
//        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
//
//        if (selectedEstateProperty == null) {
//            String msg = "Please there is no selected property";
//            JsfUtil.addInformationMessage(msg);
//            return;
//        }
//
//        selectedPropertyOccupant = PropertySelector.getMgedInstance().selectedPropertyOccupant();
//
//
//        System.out.println("property occupant is " + selectedPropertyOccupant);
//
//        if (selectedPropertyOccupant == null) {
//            JsfUtil.addErrorMessageAndRespond("Selected Property is not occupied at this time ");
//            clearButtonAction();
//            return;
//        }
//
//
//        try {
//            selectedPaymentType = PropertySelector.getMgedInstance().getPropertyOccupationDetails().getAppropriatePaymentType();
//        } catch (Exception e) {
//        }
//
//
//        propertyOccupantName = selectedPropertyOccupant.getOccupantName();
//        estatePropertyLedger.setPayeeName(propertyOccupantName);
//
//        selectedOccupantProperty = PropertySelector.getMgedInstance().getPropertyOccupationDetails();
//
//        estatePropertyLedgerList = ds.getCustomQry().loadBillsPaidForCurrentOccupant(selectedEstateProperty, selectedYear);
//
//        if (estatePropertyLedgerList == null) {
//            JsfUtil.addErrorMessageAndRespond("There is no record. An error may have been occured");
//            clearButtonAction();
//            return;
//        }
//
//    }
    public void generateAccountStatement() {
        double rentCharges = 0;
        double yearCharge;
        String rentType = null;
        AccountStatementDetail statementDetail = new AccountStatementDetail();

        List<AccountStatementDetail> statementDetailList = new ArrayList<>();
        if (estatePropertyLedgerList == null) {
            JsfUtil.addErrorMessageAndRespond("There is no record. An error may have been occured");
            clearButtonAction();
            return;
        } else {
            estateBillLedgerList = ds.getCustomQry().propertyLedgerForAccount(selectedEstateProperty, transactionFrom, transactionTo);

            yearCharge = ds.getCustomQry().propertyLastRentBill(selectedEstateProperty.getEstatePropertyId());
            paymentType = selectedEstateProperty.supposePaymentType();

            if (paymentType == PaymentType.GROUND_RENT) {
                rentType = "Leasehold";
            } else if (paymentType == PaymentType.HOUSE_RENT) {
                rentType = "Rental";
            }

            totalCreditEntry = 0.0;
            totalDebitEntry = 0.0;
            outstandingBalance = 0.0;

            for (EstatePropertyLedger epl : estateBillLedgerList) {
                if (epl.getDebitCreditEntry() == DebitCredit.DEBIT) {
                    totalDebitEntry += epl.getAmountInvolved();
                } else if (epl.getDebitCreditEntry() == DebitCredit.CREDIT) {
                    totalCreditEntry += epl.getAmountInvolved();
                }
            }

            outstandingBalance = totalCreditEntry - totalDebitEntry;

            for (EstatePropertyLedger epl : estateBillLedgerList) {
                statementDetail.setTransactionDate(dateFormat.format(epl.getDateOfRecordTransaction()));
                statementDetail.setPayeeName(epl.getPayeeName());
                statementDetail.setPaymentType(epl.getPaymentFor());
                if (epl.getDebitCreditEntry().getFullString().equals("com.sabonay.common.constants.DebitCredit.DEBIT")) {

                    statementDetail.setTotalDebit(epl.getAmountInvolved());
                } else if (epl.getDebitCreditEntry().getFullString().equals("com.sabonay.common.constants.DebitCredit.CREDIT")) {

                    statementDetail.setTotalCredit(epl.getAmountInvolved());
                }

                statementDetailList.add(statementDetail);

                statementDetail = new AccountStatementDetail();
            }

            groundRent = ds.getCustomQry().searchPropertyUseage(selectedOccupancy.getEstateProperty(), currentYear);
            selectedOccupancy = PropertySelector.getMgedInstance().getPropertyOccupationDetails();

            avReportManager.getInstance().addParam("occupantName", selectedEstateProperty.getCurrentPropertyOccupant().getOccupantName());
            avReportManager.getInstance().addParam("propertyNumber", selectedEstateProperty.getEstatePropertyId());
            avReportManager.getInstance().addParam("propertyLocation", selectedEstateProperty.getPropertyEstate().getEstateName());
            avReportManager.getInstance().addParam("occupancyDate", dateFormat.format(selectedEstateProperty.currentOccupantProperty().getFirstDateOfOccupancy()));
            avReportManager.getInstance().addParam("rentType", rentType);
            avReportManager.getInstance().addParam("rentCharge", yearCharge);
            avReportManager.getInstance().addParam("totalArrears", outstandingBalance);
            avReportManager.getInstance().addParam("totalCredit", totalCreditEntry);
            avReportManager.getInstance().addParam("totalDebit", totalDebitEntry);
            avReportManager.getInstance().addParam("reportTitle", "Statement of Account from " + dateFormat.format(transactionFrom) + " to " + dateFormat.format(transactionTo));
            avReportManager.getInstance().showReport(statementDetailList, avReportManager.getInstance().ACCOUNT_STATEMENT);
        }
    }

    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public EstatePropertyLedger getBillCorrectionLedger() {
        return billCorrectionLedger;
    }

    public void setBillCorrectionLedger(EstatePropertyLedger billCorrectionLedger) {
        this.billCorrectionLedger = billCorrectionLedger;
    }

    public String getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }

    public double getAmtInvolved() {
        return amtInvolved;
    }

    public void setAmtInvolved(double amtInvolved) {
        this.amtInvolved = amtInvolved;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public EstatePropertyLedger getPayingForEstatePropertyLedger() {
        return payingForEstatePropertyLedger;
    }

    public void setPayingForEstatePropertyLedger(EstatePropertyLedger payingForEstatePropertyLedger) {
        this.payingForEstatePropertyLedger = payingForEstatePropertyLedger;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<EstatePropertyLedger> getEstateBillLedgerList() {
        return estateBillLedgerList;
    }

    public void setEstateBillLedgerList(List<EstatePropertyLedger> estateBillLedgerList) {
        this.estateBillLedgerList = estateBillLedgerList;
    }

    public List<EstatePropertyLedger> getEstatePropertyList() {
        return estatePropertyList;
    }

    public void setEstatePropertyList(List<EstatePropertyLedger> estatePropertyList) {
        this.estatePropertyList = estatePropertyList;
    }

    public GroundRent getGroundRent() {
        return groundRent;
    }

    public void setGroundRent(GroundRent groundRent) {
        this.groundRent = groundRent;
    }

    public Date getTransactionFrom() {
        return transactionFrom;
    }

    public void setTransactionFrom(Date transactionFrom) {
        this.transactionFrom = transactionFrom;
    }

    public Date getTransactionTo() {
        return transactionTo;
    }

    public void setTransactionTo(Date transactionTo) {
        this.transactionTo = transactionTo;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public double getTotalCreditEntry() {
        return totalCreditEntry;
    }

    public void setTotalCreditEntry(int totalCreditEntry) {
        this.totalCreditEntry = totalCreditEntry;
    }

    public double getTotalDebitEntry() {
        return totalDebitEntry;
    }

    public void setTotalDebitEntry(double totalDebitEntry) {
        this.totalDebitEntry = totalDebitEntry;
    }

    public EstatePropertyLedger getEstatePropertyLedger() {
        return estatePropertyLedger;
    }

    public void setEstatePropertyLedger(EstatePropertyLedger estatePropertyLedger) {
        this.estatePropertyLedger = estatePropertyLedger;
    }

    public List<EstatePropertyLedger> getEstatePropertyLedgerList() {
        return estatePropertyLedgerList;
    }

    public void setEstatePropertyLedgerList(List<EstatePropertyLedger> estatePropertyLedgerList) {
        this.estatePropertyLedgerList = estatePropertyLedgerList;
    }

    public String getPropertyOccupantName() {
        return propertyOccupantName;
    }

    public void setPropertyOccupantName(String propertyOccupantName) {
        this.propertyOccupantName = propertyOccupantName;
    }

    public PaymentType getSelectedPaymentType() {
        return selectedPaymentType;
    }

    public void setSelectedPaymentType(PaymentType selectedPaymentType) {
        this.selectedPaymentType = selectedPaymentType;
    }

    public EstateProperty getSelectedEstateProperty() {
        return selectedEstateProperty;
    }

    public void setSelectedEstateProperty(EstateProperty selectedEstateProperty) {
        this.selectedEstateProperty = selectedEstateProperty;
    }

    public PropertyOccupant getSelectedPropertyOccupant() {
        return selectedPropertyOccupant;
    }

    public void setSelectedPropertyOccupant(PropertyOccupant selectedPropertyOccupant) {
        this.selectedPropertyOccupant = selectedPropertyOccupant;
    }

    public DataModel<EstatePropertyLedger> getEstatePropertyLedgerDataModel() {
        return estatePropertyLedgerDataModel;
    }

    public void setEstatePropertyLedgerDataModel(DataModel<EstatePropertyLedger> estatePropertyLedgerDataModel) {
        this.estatePropertyLedgerDataModel = estatePropertyLedgerDataModel;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public Occupancy getSelectedOccupancy() {
        return selectedOccupancy;
    }

    public void setSelectedOccupancy(Occupancy selectedOccupancy) {
        this.selectedOccupancy = selectedOccupancy;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getMediumOfPayment() {
        return mediumOfPayment;
    }

    public void setMediumOfPayment(String mediumOfPayment) {
        this.mediumOfPayment = mediumOfPayment;
    }

    public String getMediumOfPaymentNumber() {
        return mediumOfPaymentNumber;
    }

    public void setMediumOfPaymentNumber(String mediumOfPaymentNumber) {
        this.mediumOfPaymentNumber = mediumOfPaymentNumber;
    }

    public Date getDateOfRecordTransaction() {
        return dateOfRecordTransaction;
    }

    public void setDateOfRecordTransaction(Date dateOfRecordTransaction) {
        this.dateOfRecordTransaction = dateOfRecordTransaction;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }
    
    
    
    
    
    
    
    // </editor-fold>

    //NEW GETTER AND SETTERS FOR BILLS
   

    public List<Bills> getUnpaidBillsList() {
        return unpaidBillsList;
    }

    public void setUnpaidBillsList(List<Bills> unpaidBillsList) {
        this.unpaidBillsList = unpaidBillsList;
    }

    public DataModel<Bills> getUnpaidBillsListDataModel() {
        return unpaidBillsListDataModel;
    }

    public void setUnpaidBillsListDataModel(DataModel<Bills> unpaidBillsListDataModel) {
        this.unpaidBillsListDataModel = unpaidBillsListDataModel;
    }

    public int getCountPaidOcc() {
        return countPaidOcc;
    }

    public void setCountPaidOcc(int countPaidOcc) {
        this.countPaidOcc = countPaidOcc;
    }

    public double getSumPaidOcc() {
        return sumPaidOcc;
    }

    public void setSumPaidOcc(double sumPaidOcc) {
        this.sumPaidOcc = sumPaidOcc;
    }

    public String getBill_status() {
        return bill_status;
    }

    public void setBill_status(String bill_status) {
        this.bill_status = bill_status;
    }

    public List<Bills> getUnpaidBillsList_by_occupant() {
        return unpaidBillsList_by_occupant;
    }

    public void setUnpaidBillsList_by_occupant(List<Bills> unpaidBillsList_by_occupant) {
        this.unpaidBillsList_by_occupant = unpaidBillsList_by_occupant;
    }

    public DataModel<Bills> getUnpaidBillsListDataMode_by_occupant() {
        return unpaidBillsListDataMode_by_occupant;
    }

    public void setUnpaidBillsListDataMode_by_occupant(DataModel<Bills> unpaidBillsListDataMode_by_occupant) {
        this.unpaidBillsListDataMode_by_occupant = unpaidBillsListDataMode_by_occupant;
    }
    
    
    
    
    
    
    
    

    
    
    
    
}

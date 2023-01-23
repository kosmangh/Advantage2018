/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.ContactGroup;
import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.common.utils.AdConstants;
import com.sabonay.advantage.common.utils.GroundRentBiller;
import static com.sabonay.advantage.common.utils.GroundRentBiller.UNIT_SIZE;
import com.sabonay.advantage.common.utils.GroundRentRelatedReport;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.Bills;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.GroundRent;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.GroundRentDetail;
import com.sabonay.advantage.web.reports.avReportManager;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.collection.comparators.StringValueComparator;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@Named(value = "groundRentSetupController")
@SessionScoped
public class GroundRentSetupController implements Serializable {

    private String groundRentApplicationResult;
    private Occupancy occupancy;
    private PropertyOccupant propertyOccupant;
    private int selectedYear;
    private int beginYearForGroundRentCharges;
    private int endYearForGroundRentCharges;
    private ListDataModel<GroundRent> yearGroundRentItemsList;
    private Estate selectedEstate;
    private EstateBlock selectedEstateBlock;
    private SelectItem[] selectedEstateBlocksOptions;
    private SelectItem[] selectedEstatePropertiesOptions;   
    private EstateProperty selectedEstateProperty;
    private List<EstateProperty> estatePropertiesList = null;
    private int currentTab = 0;
    
     int year_current = Calendar.getInstance().get(Calendar.YEAR);
    private ContactGroup contactGroup;
    private boolean renderBlocks = false;
    private boolean renderPropertyNumber = false;
    private boolean renderEstate = false;
    private boolean rendered = false;
    private static GroundRentSetupController groundRentSetupController = new GroundRentSetupController();
    private static final String BEAN_NAME = "groundRentSetupController";
    
   private String estate;
   private String block;
   private String propertyNumber;

    public GroundRentSetupController() {
    }

    public void loadSelectedYearGroundRent() {

        List<GroundRent> groundRentsForYear = new LinkedList<GroundRent>();

        for (PropertyUsage propertyUsage : PropertyUsage.values()) {
            GroundRent temp = new GroundRent();
            temp.setYearDue(selectedYear);
            temp.setPropertyUsage(propertyUsage);

            IDCreator.setGroundRentId(temp);

            GroundRent groundRent
                    = ds.getCommonQry().groundRentFind(temp.getGroundRentId());

            if (groundRent != null) {
                groundRentsForYear.add(groundRent);
            }
            
            //else {
//                groundRentsForYear.add(temp);
//                String newOneCreated = ds.getCommonQry().groundRentCreate(temp);

//                System.out.println("new one created ... " + newOneCreated);
           // }

            yearGroundRentItemsList
                    = new ListDataModel<GroundRent>(groundRentsForYear);

        }
    }

    public void updateGroundRentValues() {
//        System.out.println("sixe is " + yearGroundRentItemsList.getRowCount());
        for (GroundRent groundRent : yearGroundRentItemsList) {
            try {
                boolean updated = ds.getCommonQry().groundRentUpdate(groundRent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        String msg = "Ground Rent Update Succesfully";
        JsfUtil.addInformationMessage(msg);
    }

    public void applyGroundRentForSelectedYear() {
        System.out.println("about to start applying groud rent");

        if (selectedYear > DateTimeUtils.getCurrentYear()) {
            String msg = "You cannot apply Ground Rent for years below " + (DateTimeUtils.getCurrentYear() - 1);

            JsfUtil.addErrorMessage(msg);
            JsfUtil.getFacesContext().renderResponse();
            return;
        }

        try {

            if (selectedEstate == null) {
                JsfUtil.addErrorMessage("Please Select Estate Before Applying Ground Rent");
                return;
            }

           //ds.getCustomQry().currentOccupiedPropertiesOnLeaseHold(selectedEstate.getEstateId())
            List<EstateProperty> estatePropertiesLists = selectedEstate.getEstatePropertiesList();

            GroundRentBiller biller = new GroundRentBiller();
            for (EstateProperty estateProperty : estatePropertiesLists) {
                biller.setEstateProperty(estateProperty);
                biller.setSelectedYear(selectedYear);
                biller.billPropertyGroud();
            }

            JsfUtil.addInformationMessage("Applying Ground Rent For "
                    + selectedEstate.getEstateName() + " for "
                    + selectedYear + " completed");

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error Occured in Applying ground rent " + e.getMessage());
            Logger.getLogger(GroundRentSetupController.class.getName()).log(Level.SEVERE, e.toString(), e);
            e.printStackTrace();
        }

    }
    
    
    
    public void applySingleBillingToOccupantForSelected(){
      System.out.println("about to start applying groud rent");

        if (selectedYear > DateTimeUtils.getCurrentYear()) {
            String msg = "You cannot apply Ground Rent for years above " + (DateTimeUtils.getCurrentYear());

            JsfUtil.addErrorMessage(msg);
            JsfUtil.getFacesContext().renderResponse();
            return;
        }
        
        try {
            
              if(selectedEstate == null) {
                JsfUtil.addErrorMessage("Please Select Estate Before Applying Ground Rent");
                return;
            }else if(selectedEstateBlock==null){
                 JsfUtil.addErrorMessage("Please Select Estate Block Before Applying Ground Rent");
                return;
            }else if (selectedEstateProperty == null){
                 JsfUtil.addErrorMessage("Please Select Estate Block Property Before Applying Ground Rent");
                return;
            }else{
               // Estate estate_by = new Estate();
                
                String estateName = selectedEstate.getEstateId();
                String blockName = selectedEstateBlock.getEstateBlockId();
                String estate_property_id = selectedEstateProperty.getEstatePropertyId();
                String estate_property_usage = selectedEstateProperty.getPropertyUsage().toString();
                 
                
                Double get_selected_landsize = selectedEstateProperty.getPropertyLandSize();
                
                String property_occupant_string_id = (String) ds.getCustomQry().getPropertiOccupantByEstatePropertyID(estate_property_id).getPropertyOccupantId();
               
                
             String property_occupant_string_name = (String) ds.getCustomQry().getPropertiOccupantByEstatePropertyID(estate_property_id).getOccupantName();
                   
             
             Integer get_selected_year = selectedYear;
             
               if(selectedYear < year_current){
                   for(int i = selectedYear; i <= year_current; i++  ){
                           Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, i);
             
             
             Double get_single_bill_amount = getAppropriateSingleGroundRentBill();
             
             
               EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
                   Bills bills = new Bills(); 
//               SAVING INTO THE LEDGER TABLE HERE
               
            propertyLedger.setEstateProperty(selectedEstateProperty);
            propertyLedger.setLedgerYear(i);
            propertyLedger.setAmountInvolved(get_single_bill_amount); 
            propertyLedger.setDateOfRecordEntry(new Date());
            propertyLedger.setDateOfRecordTransaction(calendar.getTime());
            propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            propertyLedger.setPaymentType(PaymentType.GROUND_RENT);
            propertyLedger.setPropertyOccupant(selectedEstateProperty.getCurrentPropertyOccupant());
            propertyLedger.setPayeeName(AdConstants.SHC);
            propertyLedger.setMediumOfPayment(AdConstants.NONE);
            propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);
            propertyLedger.setPaymentFor("Ground Rent Bill, " + i);

            IDCreator.setEstateLedgerId(propertyLedger, null);
            
            String get_id_for_bill = IDCreator.setEstateLedgerId1(propertyLedger, null);
            
            
            
//            SAVING INTO THE BILLS TABLES HERE
            
            
             
                  bills.setEstatePropertyLedgerId(get_id_for_bill);
                  bills.setEstateProperty(estate_property_id);
                  bills.setPropertyOccupant(property_occupant_string_id);
                  bills.setBillDetail("Ground Rent Bill," + i);
                  bills.setBillAmount(get_single_bill_amount);
                  bills.setBillType("GROUND_RENT");
                  bills.setBillYear(i);
                  //bills.setRecordedBy("");
                  bills.setDateOfRecordEntry(new Date());
                  
               
                  
                  
               System.out.print(propertyLedger + "\t");
            
               System.out.print(bills + "\t");
            ds.getCommonQry().recordEntryIntoBills(bills);
            ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);
            
              //boolean message_for_bill =  ds.getCommonQry().recordEntryIntoBills(bills);
               
              // boolean message_for_ledger = ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);
           JsfUtil.addInformationMessage("Occupant:   " + property_occupant_string_name + "    successfully billed for " + i);
//                     
                   }
               }else {
            // if(selectedYear == year_current)
                    
         Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, selectedYear);
             
             
             Double get_single_bill_amount = getAppropriateSingleGroundRentBill();
             
             
               EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
                   Bills bills = new Bills(); 
//               SAVING INTO THE LEDGER TABLE HERE
               
            propertyLedger.setEstateProperty(selectedEstateProperty);
            propertyLedger.setLedgerYear(selectedYear);
            propertyLedger.setAmountInvolved(get_single_bill_amount); 
            propertyLedger.setDateOfRecordEntry(new Date());
            propertyLedger.setDateOfRecordTransaction(calendar.getTime());
            propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            propertyLedger.setPaymentType(PaymentType.GROUND_RENT);
            propertyLedger.setPropertyOccupant(selectedEstateProperty.getCurrentPropertyOccupant());
            propertyLedger.setPayeeName(AdConstants.SHC);
            propertyLedger.setMediumOfPayment(AdConstants.NONE);
            propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);
            propertyLedger.setPaymentFor("Ground Rent Bill, " + selectedYear);

            IDCreator.setEstateLedgerId(propertyLedger, null);
            
            String get_id_for_bill = IDCreator.setEstateLedgerId1(propertyLedger, null);
            
            
            
//            SAVING INTO THE BILLS TABLES HERE
            
            
             
                  bills.setEstatePropertyLedgerId(get_id_for_bill);
                  bills.setEstateProperty(estate_property_id);
                  bills.setPropertyOccupant(property_occupant_string_id);
                  bills.setBillDetail("Ground Rent Bill," + selectedYear);
                  bills.setBillAmount(get_single_bill_amount);
                  bills.setBillType("GROUND_RENT");
                  bills.setBillYear(selectedYear);
                  //bills.setRecordedBy("");
                  bills.setDateOfRecordEntry(new Date());
                  
               
                  
                  
               System.out.print(propertyLedger + "\t");
            
               System.out.print(bills + "\t");
            
            
              boolean message_for_bill =  ds.getCommonQry().recordEntryIntoBills(bills);
               
               boolean message_for_ledger = ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);
           JsfUtil.addInformationMessage("Occupant:   " + property_occupant_string_name + "    successfully billed");
//                  
                   
                   
                   
                   
                   
               } 
                  
//               
//               if(message_for_ledger == true && message_for_bill == true){
//                    JsfUtil.addInformationMessage("Occupant:   " + property_occupant_string_name + "    successfully billed");
//               
//               }else{
//                   
//                    JsfUtil.addErrorMessage("Occupant:   " + property_occupant_string_name + "    failed, Please Try Again");
//               
//               }
         
             
                  
            
             
             
               
             
             
                  System.out.println(get_selected_landsize);
             
                     System.out.println(get_selected_year);
                  
                  System.out.println(estate_property_usage);
                
                  System.out.println(property_occupant_string_name); 
                  System.out.println(property_occupant_string_id);
                
                   System.out.println("Estate ID  " + "  " + estateName);
                        System.out.println("Block Name  " + "  " + blockName);
                             System.out.println("Estate Property ID " + "  " + estate_property_id);
                  System.out.println(selectedEstate+ ">>>>>>" + selectedEstateBlock + ">>>>>" + selectedEstateProperty);
                  
                
                    
                
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
            e.getMessage();
        }
   
        
    }
    
    
    
    public void check_occu_status(){
      System.out.println("about to start applying groud rent");

        if (selectedYear > DateTimeUtils.getCurrentYear()) {
            String msg = "You cannot apply Ground Rent for years above " + (DateTimeUtils.getCurrentYear());

            JsfUtil.addErrorMessage(msg);
            JsfUtil.getFacesContext().renderResponse();
            return;
        }
        
        try {
            
              if(selectedEstate == null) {
                JsfUtil.addErrorMessage("Please Select Estate Before Applying Ground Rent");
                return;
            }else if(selectedEstateBlock==null){
                 JsfUtil.addErrorMessage("Please Select Estate Block Before Applying Ground Rent");
                return;
            }else if (selectedEstateProperty == null){
                 JsfUtil.addErrorMessage("Please Select Estate Block Property Before Applying Ground Rent");
                return;
            }else{
               // Estate estate_by = new Estate();
                
                String estateName = selectedEstate.getEstateId();
                String blockName = selectedEstateBlock.getEstateBlockId();
                String estate_property_id = selectedEstateProperty.getEstatePropertyId();
                String estate_property_usage = selectedEstateProperty.getPropertyUsage().toString();
                 
                
                Double get_selected_landsize = selectedEstateProperty.getPropertyLandSize();
                
                String property_occupant_string_id = (String) ds.getCustomQry().getPropertiOccupantByEstatePropertyID(estate_property_id).getPropertyOccupantId();
               
                
             String property_occupant_string_name = (String) ds.getCustomQry().getPropertiOccupantByEstatePropertyID(estate_property_id).getOccupantName();
                   
             
             Integer get_selected_year = selectedYear;
             
               if(selectedYear < year_current){
                   for(int i = selectedYear; i <= year_current; i++  ){
                           Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, i);
             
             
             Double get_single_bill_amount = getAppropriateSingleGroundRentBill();
             
             
               EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
                   Bills bills = new Bills(); 
//               SAVING INTO THE LEDGER TABLE HERE
               
            propertyLedger.setEstateProperty(selectedEstateProperty);
            propertyLedger.setLedgerYear(i);
            propertyLedger.setAmountInvolved(get_single_bill_amount); 
            propertyLedger.setDateOfRecordEntry(new Date());
            propertyLedger.setDateOfRecordTransaction(calendar.getTime());
            propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            propertyLedger.setPaymentType(PaymentType.GROUND_RENT);
            propertyLedger.setPropertyOccupant(selectedEstateProperty.getCurrentPropertyOccupant());
            propertyLedger.setPayeeName(AdConstants.SHC);
            propertyLedger.setMediumOfPayment(AdConstants.NONE);
            propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);
            propertyLedger.setPaymentFor("Ground Rent Bill, " + i);

            IDCreator.setEstateLedgerId(propertyLedger, null);
            
            String get_id_for_bill = IDCreator.setEstateLedgerId1(propertyLedger, null);
            
            
            
//            SAVING INTO THE BILLS TABLES HERE
            
            
             
                  bills.setEstatePropertyLedgerId(get_id_for_bill);
                  bills.setEstateProperty(estate_property_id);
                  bills.setPropertyOccupant(property_occupant_string_id);
                  bills.setBillDetail("Ground Rent Bill," + i);
                  bills.setBillAmount(get_single_bill_amount);
                  bills.setBillType("GROUND_RENT");
                  bills.setBillYear(i);
                  //bills.setRecordedBy("");
                  bills.setDateOfRecordEntry(new Date());
                  
               
                  
                  
               System.out.print(propertyLedger + "\t");
            
               System.out.print(bills + "\t");
            ds.getCommonQry().recordEntryIntoBills(bills);
            ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);
            
              //boolean message_for_bill =  ds.getCommonQry().recordEntryIntoBills(bills);
               
              // boolean message_for_ledger = ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);
           JsfUtil.addInformationMessage("Occupant:   " + property_occupant_string_name + "    successfully billed for " + i);
//                     
                   }
               }else {
            // if(selectedYear == year_current)
                    
         Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, selectedYear);
             
             
             Double get_single_bill_amount = getAppropriateSingleGroundRentBill();
             
             
               EstatePropertyLedger propertyLedger = new EstatePropertyLedger();
                   Bills bills = new Bills(); 
//               SAVING INTO THE LEDGER TABLE HERE
               
            propertyLedger.setEstateProperty(selectedEstateProperty);
            propertyLedger.setLedgerYear(selectedYear);
            propertyLedger.setAmountInvolved(get_single_bill_amount); 
            propertyLedger.setDateOfRecordEntry(new Date());
            propertyLedger.setDateOfRecordTransaction(calendar.getTime());
            propertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            propertyLedger.setPaymentType(PaymentType.GROUND_RENT);
            propertyLedger.setPropertyOccupant(selectedEstateProperty.getCurrentPropertyOccupant());
            propertyLedger.setPayeeName(AdConstants.SHC);
            propertyLedger.setMediumOfPayment(AdConstants.NONE);
            propertyLedger.setMediumOfPaymentNumber(AdConstants.NONE);
            propertyLedger.setPaymentFor("Ground Rent Bill, " + selectedYear);

            IDCreator.setEstateLedgerId(propertyLedger, null);
            
            String get_id_for_bill = IDCreator.setEstateLedgerId1(propertyLedger, null);
            
            
            
//            SAVING INTO THE BILLS TABLES HERE
            
            
             
                  bills.setEstatePropertyLedgerId(get_id_for_bill);
                  bills.setEstateProperty(estate_property_id);
                  bills.setPropertyOccupant(property_occupant_string_id);
                  bills.setBillDetail("Ground Rent Bill," + selectedYear);
                  bills.setBillAmount(get_single_bill_amount);
                  bills.setBillType("GROUND_RENT");
                  bills.setBillYear(selectedYear);
                  //bills.setRecordedBy("");
                  bills.setDateOfRecordEntry(new Date());
                  
               
                  
                  
               System.out.print(propertyLedger + "\t");
            
               System.out.print(bills + "\t");
            
            
              boolean message_for_bill =  ds.getCommonQry().recordEntryIntoBills(bills);
               
               boolean message_for_ledger = ds.getCommonQry().estatePropertyLedgerUpdate(propertyLedger);
           JsfUtil.addInformationMessage("Occupant:   " + property_occupant_string_name + "    successfully billed");
//                  
                   
                   
                   
                   
                   
               } 
                  
//               
//               if(message_for_ledger == true && message_for_bill == true){
//                    JsfUtil.addInformationMessage("Occupant:   " + property_occupant_string_name + "    successfully billed");
//               
//               }else{
//                   
//                    JsfUtil.addErrorMessage("Occupant:   " + property_occupant_string_name + "    failed, Please Try Again");
//               
//               }
         
             
                  
            
             
             
               
             
             
                  System.out.println(get_selected_landsize);
             
                     System.out.println(get_selected_year);
                  
                  System.out.println(estate_property_usage);
                
                  System.out.println(property_occupant_string_name); 
                  System.out.println(property_occupant_string_id);
                
                   System.out.println("Estate ID  " + "  " + estateName);
                        System.out.println("Block Name  " + "  " + blockName);
                             System.out.println("Estate Property ID " + "  " + estate_property_id);
                  System.out.println(selectedEstate+ ">>>>>>" + selectedEstateBlock + ">>>>>" + selectedEstateProperty);
                  
                
                    
                
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
            e.getMessage();
        }
   
        
    }
    
    private double getAppropriateSingleGroundRentBill(){
     String usage = selectedEstateProperty.getPropertyUsage().toString();
         
            try {
            if (usage == null){
                return 0.0;
            }
            
            if(usage=="COM" || usage=="MIX" || usage=="IND" ||usage=="RES"){
                
                return getCommMixedUseSingleBill();
            }else{
                return ds.getCustomQry().getGroundRentBasicChargeOnSingleBill(selectedEstateProperty);
            
             // return ds.getCustomQry().getGroundRentBasicCharge(selectedEstateProperty, selectedYear);
            
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
         
        
    }
    
     private double getCommMixedUseSingleBill() {

        double a = ds.getCustomQry().getGroundRentBasicChargeOnSingleBill(selectedEstateProperty);
        
        
        // double a = ds.getCustomQry().getGroundRentBasicCharge(selectedEstateProperty, selectedYear);


//        if (selectedYear < 2008) {
//            return a;
//        }

        double landSize =  selectedEstateProperty.getPropertyLandSize();

        if (landSize > UNIT_SIZE) {
            double newRate = (landSize / UNIT_SIZE) * a;

            return newRate;
        }else{
          return a;
     }


       
    }
    
//    private List<EstateProperty> listOfEstateProperties(){
//          try {
//            if (contactGroup == ContactGroup.ALL_OCCUPANTS) {
//                
//
//            }
//            if (contactGroup == ContactGroup.ESTATE) {
//                estatePropertiesList = selectedEstate.getEstatePropertiesList();
//            }
//            if (contactGroup == ContactGroup.BLOCK) {
//                estatePropertiesList = selectedEstateBlock.getEstatePropertyList();
//            }
//            if (contactGroup == ContactGroup.INDIVIDUAL) {
//                estatePropertiesList = ds.getCommonQry().estatePropertyFindByAttribute("estatePropertyId", selectedEstateProperty.getEstatePropertyId(), "STRING", false);
//            }
//            if (estatePropertiesList.isEmpty()) {
//                JsfUtil.addErrorMessage("No estate properties found");
//            } 
//            
//            return listOfEstateProperties();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public String printGroundRentChargesBetweenYears() {
        System.out.println("in a method>>>>>>>>>>>>>>>>>");
        String reportTitle = "Ground Rent Charges Between "
                + beginYearForGroundRentCharges
                + " to "
                + endYearForGroundRentCharges;

        List<GroundRent> groundRentsList
                = ds.getCustomQry().
                findGroundRentBetweenYears(beginYearForGroundRentCharges,
                        endYearForGroundRentCharges);

        System.out.println(" result  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + groundRentsList);

        String estatesDetais = "";

        List<Estate> estatesList = ds.getCommonQry().estateGetAll(true);

        for (Estate estate : estatesList) {
            estatesDetais += estate.getEstateName() + " \t \t - " + estate.getEstateClass() + "\n";
        }

        List<GroundRentDetail> groundRentDetails = GroundRentRelatedReport.getReport(groundRentsList);

        avReportManager.getInstance().addParam("reportTitle", reportTitle);
        avReportManager.getInstance().addParam("estatesClasses", estatesDetais);

        avReportManager.getInstance().showReport(groundRentDetails,
                avReportManager.GROUND_RENT_CHARGES);

        return null;

    }
   
    public void applyDefaultPenaltyCharges(){
        try {
            GroundRentBiller biller = new GroundRentBiller();
            for(EstateProperty ep: estatePropertiesList){
                biller.setEstateProperty(ep);
                biller.setSelectedYear(selectedYear);
                biller.chargeDefaultPenalty();
            }
            JsfUtil.addInformationMessage("Default Penalty charge applied successfully");
        } catch (Exception e) {
            
            JsfUtil.addErrorMessage("Penalty charge application failed");
            e.printStackTrace();
        }
    }
    
    
    public void estateChangeListener(ValueChangeEvent changeEvent) {
        if (changeEvent.getNewValue() != null) {
            selectedEstate = (Estate) changeEvent.getNewValue();
            selectedEstateBlocksOptions = JsfUtil.createSelectItems(selectedEstate.getEstateBlockList(), true);

            estatePropertiesList = null;
        }
    }

    public String getSelectedEstateId() {
        if (selectedEstate != null) {
            return selectedEstate.getEstateId();
        }

        return null;
    }

    public void estateBlockPropertyChangeListener(ValueChangeEvent changeEvent) {
        if (changeEvent.getNewValue() != null) {
            try {

                selectedEstatePropertiesOptions = null;
                estatePropertiesList = null;

                selectedEstateBlock = (EstateBlock) changeEvent.getNewValue();

                if (selectedEstateBlock == null) {
                    JsfUtil.addErrorMessage("Please select an Estate Block");
                    return;
                }
                estatePropertiesList = ds.getCustomQry().findEstatePropertiesByBlock(selectedEstateBlock.getEstateBlockId());
                StringValueComparator.sort(estatePropertiesList);
                selectedEstatePropertiesOptions
                        = JsfUtil.createSelectItems(estatePropertiesList, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void estatePropertyListener(ValueChangeEvent vce) {

        try {
            if (vce.getNewValue() != null) {
                selectedEstateProperty = (EstateProperty) vce.getNewValue();
                estatePropertiesList = ds.getCommonQry().estatePropertyFindByAttribute("estatePropertyId", selectedEstateProperty.getEstatePropertyId(), "STRING", false);
            }
        } catch (Exception e) {
        }
    }
    
    public void selectContactGroupChangeEvent(ValueChangeEvent vce) {
        try {
            if (vce.getNewValue() != null) {
                contactGroup = (ContactGroup) vce.getNewValue();
                if (contactGroup == null) {
                    JsfUtil.addErrorMessage("Please select a contact group");
                    return;
                }
                if (contactGroup == ContactGroup.ALL_OCCUPANTS) {
                    rendered = false;
                    renderBlocks = false;
                    renderEstate = false;
                    renderPropertyNumber = false;

                }
                if (contactGroup == ContactGroup.ESTATE) {
                    rendered = true;
                    renderEstate = true;
                    renderBlocks = false;
                    renderPropertyNumber = false;

                }
                if (contactGroup == ContactGroup.BLOCK) {
                    renderBlocks = true;
                    renderEstate = true;
                    rendered = true;
                    renderPropertyNumber = false;

                }
                if (contactGroup == ContactGroup.INDIVIDUAL) {
                    renderBlocks = true;
                    renderEstate = true;
                    rendered = true;
                    renderPropertyNumber = true;

                }

            }
        } catch (Exception e) {
        }
    }

    public static GroundRentSetupController getInstance(){
        GroundRentSetupController rentSetupController = (GroundRentSetupController)JsfUtil.getManagedBean(BEAN_NAME);
        return rentSetupController;
    }
    
    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public ListDataModel<GroundRent> getYearGroundRentItemsList() {
        return yearGroundRentItemsList;
    }

    public void setYearGroundRentItemsList(ListDataModel<GroundRent> yearGroundRentItemsList) {
        this.yearGroundRentItemsList = yearGroundRentItemsList;
    }

    public int getBeginYearForGroundRentCharges() {
        return beginYearForGroundRentCharges;
    }

    public void setBeginYearForGroundRentCharges(int beginYearForGroundRentCharges) {
        this.beginYearForGroundRentCharges = beginYearForGroundRentCharges;
    }

    public int getEndYearForGroundRentCharges() {
        return endYearForGroundRentCharges;
    }

    public void setEndYearForGroundRentCharges(int endYearForGroundRentCharges) {
        this.endYearForGroundRentCharges = endYearForGroundRentCharges;
    }

    public String getGroundRentApplicationResult() {
        return groundRentApplicationResult;
    }

    public void setGroundRentApplicationResult(String groundRentApplicationResult) {
        this.groundRentApplicationResult = groundRentApplicationResult;
    }

    public Estate getSelectedEstate() {
        return selectedEstate;
    }

    public void setSelectedEstate(Estate selectedEstate) {
        this.selectedEstate = selectedEstate;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    public EstateBlock getSelectedEstateBlock() {
        return selectedEstateBlock;
    }

    public void setSelectedEstateBlock(EstateBlock selectedEstateBlock) {
        this.selectedEstateBlock = selectedEstateBlock;
    }

    public SelectItem[] getSelectedEstateBlocksOptions() {
        return selectedEstateBlocksOptions;
    }

    public void setSelectedEstateBlocksOptions(SelectItem[] selectedEstateBlocksOptions) {
        this.selectedEstateBlocksOptions = selectedEstateBlocksOptions;
    }

    public SelectItem[] getSelectedEstatePropertiesOptions() {
        return selectedEstatePropertiesOptions;
    }

    public void setSelectedEstatePropertiesOptions(SelectItem[] selectedEstatePropertiesOptions) {
        this.selectedEstatePropertiesOptions = selectedEstatePropertiesOptions;
    }

    public EstateProperty getSelectedEstateProperty() {
        return selectedEstateProperty;
    }

    public void setSelectedEstateProperty(EstateProperty selectedEstateProperty) {
        this.selectedEstateProperty = selectedEstateProperty;
    }

    public List<EstateProperty> getEstatePropertiesList() {
        return estatePropertiesList;
    }

    public void setEstatePropertiesList(List<EstateProperty> estatePropertiesList) {
        this.estatePropertiesList = estatePropertiesList;
    }

    public static GroundRentSetupController getGroundRentSetupController() {
        return groundRentSetupController;
    }

    public static void setGroundRentSetupController(GroundRentSetupController groundRentSetupController) {
        GroundRentSetupController.groundRentSetupController = groundRentSetupController;
    }

    public ContactGroup getContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }

    public boolean isRenderBlocks() {
        return renderBlocks;
    }

    public void setRenderBlocks(boolean renderBlocks) {
        this.renderBlocks = renderBlocks;
    }

    public boolean isRenderPropertyNumber() {
        return renderPropertyNumber;
    }

    public void setRenderPropertyNumber(boolean renderPropertyNumber) {
        this.renderPropertyNumber = renderPropertyNumber;
    }

    public boolean isRenderEstate() {
        return renderEstate;
    }

    public void setRenderEstate(boolean renderEstate) {
        this.renderEstate = renderEstate;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public Occupancy getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Occupancy occupancy) {
        this.occupancy = occupancy;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }
    
    

   
    
    
    
    
    
}

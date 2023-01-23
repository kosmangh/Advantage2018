/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.utils.BillDemandNoteMaker;
import com.sabonay.advantage.common.utils.EstatesStaticsRunner;
import com.sabonay.advantage.common.utils.LegerUtils;
import com.sabonay.advantage.common.utils.PrepHttpMessage;
import com.sabonay.advantage.ejb.entities.Bills;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.BillDemandNote;
import com.sabonay.advantage.ejb.models.BillsOccuStatus;
import com.sabonay.advantage.ejb.models.PropertyLedgerDetail;
import com.sabonay.advantage.web.reports.avReportManager;
import com.sabonay.advantage.web.sms.utils.GroundRentProcessor;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.formating.NumberFormattingUtils;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.common.utils.MessagingUtils;
import com.sabonay.modules.web.jsf.api.annotations.SearchButtonAction;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@Named(value = "ledgerController")
@SessionScoped
public class LedgerController implements Serializable {

    private int selectedYear;

    private EstateProperty selectedEstateProperty;

    private String propertyOccupantSearchText;
    private String propertyOccupantSearchCriteria;
    private String occupantName;

    private PropertyOccupant selectedLessee;
    private DataModel<EstatePropertyLedger> estatePropertyLedgerDataModel;
    private DataModel<PropertyOccupant> propertyOccupantDataModel;

    private Date billDemandNotePaymentDay = new Date();
    private List<EstatePropertyLedger> searchedEstatePropertyLedgerList = null;
    private List<EstatePropertyLedger> epl = null;
    private List<PropertyOccupant> searchedLesseeList = null;
    private boolean rendered = false;
    private double totalCredit, totalDebit, outstandingBalance = 0.00;
    
    
    private List<Bills> listBillReport;
    private DataModel<Bills> listBillReportDataModel;
    
    private boolean billReportRender = false;

    public LedgerController() {

    }

    public void prepareRequestedLedger() {
        clearSeachedLedgerItems();
        totalCredit = 0.00;
        totalDebit = 0.00;
        outstandingBalance = 0.00;

        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        System.out.println("selected estate property " + selectedEstateProperty);

        if (selectedEstateProperty == null) {
            String msg = "Please Select an Estate Property Before Performing Search";
            JsfUtil.addInformationMessage(msg);
            return;
        }

        Occupancy occupancy = selectedEstateProperty.getOccupancy();
        if (occupancy == null) {
            JsfUtil.addInformationMessage("Selected Estate Property is unoccupied at this time");
        }

        searchedEstatePropertyLedgerList = ds.getCustomQry().loadLedgerEntriesForPropertyOfCurrentOccupant(selectedEstateProperty, selectedYear);
        epl = ds.getCustomQry().loadLedgerEntriesForPropertyOfCurrentOccupantAsc(selectedEstateProperty, selectedYear);

//      if(searchedEstatePropertyLedgerList.isEmpty() || epl.isEmpty()){
//          JsfUtil.addErrorMessage("Occupant Has Not Being Billed And Has No Record In The Ledger,Please Do So");
//      }else{
          estatePropertyLedgerDataModel = new ListDataModel<>(searchedEstatePropertyLedgerList);
        for(EstatePropertyLedger epl : searchedEstatePropertyLedgerList) {
            if (epl.getDebitCreditEntry() == DebitCredit.CREDIT) {
                totalCredit += NumberFormattingUtils.formatDecimalNumberTo_2(epl.getAmountInvolved(), 2);
            }
            if (epl.getDebitCreditEntry() == DebitCredit.DEBIT) {
                Double debit = epl.getAmountInvolved();
                
                if(debit == null){
                    debit = ds.getCustomQry().lastDebitLedgerEntryForProperty(selectedEstateProperty.getEstatePropertyId()).getAmountInvolved();
                }
                totalDebit += NumberFormattingUtils.formatDecimalNumberTo_2(debit, 2);
            }
        }
        outstandingBalance = NumberFormattingUtils.formatDecimalNumberTo_2(totalCredit - totalDebit, 2);
        rendered = true;
   // }
    }
    
    
    public void prepare_bill_for_select_occu() {         
        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        System.out.println("selected estate property " + selectedEstateProperty);

        if (selectedEstateProperty == null) {
            String msg = "Please Select an Estate Property Before Performing Search";
            JsfUtil.addInformationMessage(msg);
            return;
        }

        Occupancy occupancy = selectedEstateProperty.getOccupancy();
        
         String occupant_name = selectedEstateProperty.getCurrentPropertyOccupant().getOccupantName();
         
         String occupant_res = selectedEstateProperty.getCurrentPropertyOccupant().getPropertyOccupantId();
        if (occupancy == null) {
            JsfUtil.addInformationMessage("Selected Estate Property is unoccupied at this time");
        }
        
        try {
            billReportRender = true;
            listBillReport = new ArrayList<>();
            listBillReport = ds.getCommonQry().load_bill_report_of_occupant(occupant_res);
              
            listBillReportDataModel = new ListDataModel<>(listBillReport);
        } catch (Exception e) {
        }
        
       
        System.out.println("OccupantProperty>>>>>>>>>>>>>>>>>>>>>>" + occupancy);
          
        System.out.println("OccupantName>>>>>>>>>>>>>>>>>>>>>>" + occupant_name);
          
        System.out.println("Occupant_Res_id>>>>>>>>>>>>>>>>>>>>>>" + occupant_res);
 
    }
      
    public void viewSelectedBillItems() { 
        Occupancy occupancy = selectedEstateProperty.getOccupancy();
        
        String occpntNme = selectedEstateProperty.getCurrentPropertyOccupant().getOccupantName();
        String estateName = selectedEstateProperty.getCurrentPropertyOccupant().getEstateRequired();

        String occupant_resString = selectedEstateProperty.getCurrentPropertyOccupant().getPropertyOccupantId();
         
        List<BillsOccuStatus> list = EstatesStaticsRunner.preparePaidBillsPerOccu(occupant_resString);

        System.out.println("Occupant List Bill Breadkdown To Jasper Print>>>>>>>>>>>>>>>>>>>>>>>>>>>.." + list);
         
        //avReportManager.resetParameterToDefault();
        avReportManager.getInstance().addParam("reportTitle", "Property Bill Sheet");
         avReportManager.getInstance().addParam("occupantName", occpntNme);
          avReportManager.getInstance().addParam("occupantProperty", occupant_resString);
        avReportManager.getInstance().showReport(list, avReportManager.PROP_BILL);

//        System.out.println("ledger items are" + searchedEstatePropertyLedgerList);
//        LegerUtils.createLedgerDetails(searchedEstatePropertyLedgerList);
    }

    public void findSelectedOccupantPropertyLedgers() {
        try {
            selectedLessee = propertyOccupantDataModel.getRowData();

            System.out.println("selected propertyOccupant is " + selectedLessee + "  selected year  is " + selectedYear);

            searchedEstatePropertyLedgerList = ds.getCustomQry().findOccupantPropertyLedgersUpToYear(selectedLessee.getPropertyOccupantId(), selectedYear);

            System.out.println(searchedEstatePropertyLedgerList);

        } catch (Exception e) {
            Logger.getLogger(LedgerController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void reportGroundRent() {
        BillDemandNoteMaker inter = new BillDemandNoteMaker(selectedEstateProperty, selectedYear);
        System.out.println("before initialisation");
        inter.setPaymentDeadline(billDemandNotePaymentDay);
        System.out.println("before preparing");
 
        inter.process(); 
        System.out.println("after proocessing");

        BillDemandNote demandNotice = inter.demandNoticee(selectedYear);
        
       // BillDemandNote demandNotice = inter.demandNotice();
        System.out.println("before reporting");
        //avReportManager.resetParameterToDefault();
        avReportManager.getInstance().showReport(demandNotice, avReportManager.GROUND_RENT_DEMAND_NOTE);
            
    }

    public void reportGroundRent_old() {

        if (searchedEstatePropertyLedgerList == null || searchedEstatePropertyLedgerList.isEmpty()) {
            String msg = "Please search for the property ledger entries before generating report";
            JsfUtil.addInformationMessage(msg);
            return;
        }

        Calendar calender = Calendar.getInstance();

        calender.set(Calendar.YEAR, selectedYear);
        calender.set(Calendar.MONTH, Calendar.DECEMBER);
        calender.set(Calendar.DAY_OF_MONTH, 15);

        String formatedPaymentDate = DateTimeUtils.formatDateFully(calender.getTime());

        String demandNoticeHeader = "Please TAKE NOTE that Ground Rent in "
                + "respect of the property below for the year " + selectedYear + " "
                + "is due for payment by " + formatedPaymentDate;

        List<BillDemandNote> list = LegerUtils.prepareDemanNoticeFromLedger(searchedEstatePropertyLedgerList, selectedYear);

//        avReportManager.resetParameterToDefault();
        avReportManager.getInstance().addParam("yearOfDemandNotice", selectedYear);
        avReportManager.getInstance().addParam("demandNoticeHeader", demandNoticeHeader);
        avReportManager.getInstance().showReport(list, avReportManager.GROUND_RENT_DEMAND_NOTE);
    }

    public void viewSelectedLegerItems() { 

        List<PropertyLedgerDetail> list = LegerUtils.prepareEstateLedgerDetails(epl);

        //avReportManager.resetParameterToDefault();
        avReportManager.getInstance().addParam("reportTitle", "Property Ledger Sheet");
        avReportManager.getInstance().showReport(list, avReportManager.PROP_LEDGER);

//        System.out.println("ledger items are" + searchedEstatePropertyLedgerList);
//        LegerUtils.createLedgerDetails(searchedEstatePropertyLedgerList);
    }

    private void clearSeachedLedgerItems() {
        searchedEstatePropertyLedgerList = null;
        totalCredit = 0.00;
        totalDebit = 0.00;
        outstandingBalance = 0.00;
        epl = null;
    }

    //<editor-fold defaultstate="collapsed" desc="GroundRent SMS">
    public String sendDemandNote() {
        String demandNote = "";
        String occupantNumber = MessagingUtils.getInternationalNumber(selectedEstateProperty.getCurrentPropertyOccupant().getTelephoneNumber(), MessagingUtils.SMPP_COUNTRY_CODE_GHA);
        try {
            PrepHttpMessage.getInstance().initialseMessageParameters();
            demandNote = GroundRentProcessor.prepareGroundRentSMS(selectedEstateProperty, selectedYear);
            System.out.println("size of txt " + demandNote.length());
            System.out.println(demandNote);
            String deliveryReport = PrepHttpMessage.getInstance().getTextMessage().sendSMS(demandNote, occupantNumber);
            if (deliveryReport.trim().substring(0, 4).equalsIgnoreCase("1701")) {
                JsfUtil.addInformationMessage("Ground Rent Notice Sent Successfully");
            } else if (deliveryReport.trim().substring(0, 4).equalsIgnoreCase("1706")) {
                JsfUtil.addErrorMessage("Invalid Destination Number");
            } else if (deliveryReport.trim().substring(0, 4).equalsIgnoreCase("1707")) {
                JsfUtil.addErrorMessage("Invalid Sender ID");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("SMS sending failed");
        }
        return null;
    }
    //</editor-fold>

    @SearchButtonAction(group = "propertyOccupant")
    public void searchLesseeByName() {
        searchedLesseeList = null;

//        searchedLesseeList = ds.getCommonQry().
//                propertyOccupantFindByAttribute(propertyOccupantDataTable.getSearchCriteria(),
//                        propertyOccupantDataTable.getSearchText(), "STRING", true);
    }

    public void searchLedgerDataTableRowSelectionAction() {
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>  " + estatePropertyLedgerDataTable.getRowData());
    }

    public void blockSelectedEstateProperty() {
        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        boolean blocked;
        try {
            blocked = ds.getCustomQry().blockLedgerForEstateProperty(selectedEstateProperty);
            if (blocked == true) {
                JsfUtil.addInformationMessage("Selected EstatePropety blocked");
            } else {
                JsfUtil.addErrorMessage("Failed to block selected estate property");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Failed to block selected estate property");
            Logger.getLogger(LedgerController.class.getName()).log(Level.SEVERE, e.getMessage(), e);

        }
    }

    public void unBlockSeletedEstateProperty() {
        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        boolean blocked;
        try {
            blocked = ds.getCustomQry().unBlockLedgerForEstateProperty(selectedEstateProperty);
            if (blocked == true) {
                JsfUtil.addInformationMessage("Selected EstatePropety Ledger unblocked");
            } else {
                JsfUtil.addErrorMessage("Failed to unblock selected estate property ledger");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Failed to unblock selected estate property ledger");
            Logger.getLogger(LedgerController.class.getName()).log(Level.SEVERE, e.getMessage(), e);

        }
    }

    // <editor-fold defaultstate="collapsed" desc="Setters and Getters">
//    public HtmlDataPanel<EstatePropertyLedger> getEstatePropertyLedgerDataTable() {
//        return estatePropertyLedgerDataTable;
//    }
//
//    public void setEstatePropertyLedgerDataTable(HtmlDataPanel<EstatePropertyLedger> estatePropertyLedgerDataTable) {
//        this.estatePropertyLedgerDataTable = estatePropertyLedgerDataTable;
//    }
    public DataModel<EstatePropertyLedger> getEstatePropertyLedgerDataModel() {
        return estatePropertyLedgerDataModel;
    }

    public void setEstatePropertyLedgerDataModel(DataModel<EstatePropertyLedger> estatePropertyLedgerDataModel) {
        this.estatePropertyLedgerDataModel = estatePropertyLedgerDataModel;
    }

    public DataModel<PropertyOccupant> getPropertyOccupantDataModel() {
        return propertyOccupantDataModel;
    }

    public void setPropertyOccupantDataModel(DataModel<PropertyOccupant> propertyOccupantDataModel) {
        this.propertyOccupantDataModel = propertyOccupantDataModel;
    }

//    public HtmlDataPanel<PropertyOccupant> getPropertyOccupantDataTable() {
//        return propertyOccupantDataTable;
//    }
//
//    public void setPropertyOccupantDataTable(HtmlDataPanel<PropertyOccupant> propertyOccupantDataTable) {
//        this.propertyOccupantDataTable = propertyOccupantDataTable;
//    }
    public List<EstatePropertyLedger> getSearchedEstatePropertyLedgerList() {
        return searchedEstatePropertyLedgerList;
    }

    public void setSearchedEstatePropertyLedgerList(List<EstatePropertyLedger> searchedEstatePropertyLedgerList) {
        this.searchedEstatePropertyLedgerList = searchedEstatePropertyLedgerList;
    }

    public List<PropertyOccupant> getSearchedLesseeList() {
        return searchedLesseeList;
    }

    public void setSearchedLesseeList(List<PropertyOccupant> searchedLesseeList) {
        this.searchedLesseeList = searchedLesseeList;
    }

    public String getPropertyOccupantSearchText() {
        return propertyOccupantSearchText;
    }

    public void setPropertyOccupantSearchText(String propertyOccupantSearchText) {
        this.propertyOccupantSearchText = propertyOccupantSearchText;
    }

    public String getPropertyOccupantSearchCriteria() {
        return propertyOccupantSearchCriteria;
    }

    public void setPropertyOccupantSearchCriteria(String propertyOccupantSearchCriteria) {
        this.propertyOccupantSearchCriteria = propertyOccupantSearchCriteria;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public PropertyOccupant getSelectedLessee() {
        return selectedLessee;
    }

    public void setSelectedLessee(PropertyOccupant selectedLessee) {
        this.selectedLessee = selectedLessee;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public EstateProperty getSelectedEstateProperty() {
        return selectedEstateProperty;
    }

    public void setSelectedEstateProperty(EstateProperty selectedEstateProperty) {
        this.selectedEstateProperty = selectedEstateProperty;
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

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getOccupantName() {
        return occupantName = PropertySelector.getMgedInstance().getOccupantName();
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public Date getBillDemandNotePaymentDay() {
        return billDemandNotePaymentDay;
    }

    public void setBillDemandNotePaymentDay(Date billDemandNotePaymentDay) {
        this.billDemandNotePaymentDay = billDemandNotePaymentDay;
    }
    
    
    
    

    // </editor-fold>

    public List<Bills> getListBillReport() {
        return listBillReport;
    }

    public void setListBillReport(List<Bills> listBillReport) {
        this.listBillReport = listBillReport;
    }

    public DataModel<Bills> getListBillReportDataModel() {
        return listBillReportDataModel;
    }

    public void setListBillReportDataModel(DataModel<Bills> listBillReportDataModel) {
        this.listBillReportDataModel = listBillReportDataModel;
    }

    public boolean isBillReportRender() {
        return billReportRender;
    }

    public void setBillReportRender(boolean billReportRender) {
        this.billReportRender = billReportRender;
    }
    
    
    
}

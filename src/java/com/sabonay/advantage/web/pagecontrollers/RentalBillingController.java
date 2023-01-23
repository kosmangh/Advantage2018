/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.utils.AdConstants;
import com.sabonay.advantage.common.utils.HouseRentBiller;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;

import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.RentalOccupantDetail;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.advantage.web.validations.RentalBillingValidator;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.advantage.modules.account.AdvantageUserData;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Edwin / Ritchid
 */
@Named(value = "rentalBillingController")
@SessionScoped
public class RentalBillingController implements Serializable {

    private static final Logger LOG = Logger.getLogger(RentalBillingController.class.getName());
    private EstateProperty selectedRentedEstateProperty;
    private String billingMonth;
    private int selectedYear;
    private RentalOccupantDetail rentalOccupantDetail;
    private List<RentalOccupantDetail> rentalOccupantDetailList;
    private DataModel<RentalOccupantDetail> rentalOccupantDataModel;
//    private DataModel<HouseRent> hseRentDataModel;
    private int currentTabIndex = 0;
    private AdvantageUserData userData = AdvantageUserData.getManagedInstance();
    private PropertySelector propertySelector = PropertySelector.getMgedInstance();
    private PaymentType houseRentPaymentType = PaymentType.HOUSE_RENT;
    private EstateProperty selectedEstateProperty;
    private PropertyOccupant selectedPropertyOccupant;
    private Occupancy selectedOccupancy;
    private EstatePropertyLedger estatePropertyLedger = new EstatePropertyLedger();
//    @DataTableModelList(group = "rb")
    private List<EstatePropertyLedger> estatePropertyLedgersList;
//    @DataPanel(group = "rb")
//    private HtmlDataPanel<EstatePropertyLedger> estatePropertyLedgerDataPanel;
//    private EstatePropertyLedgerTableModel estatePropertyLedgerTableModel = new EstatePropertyLedgerTableModel();
//    private HtmlFormControl formControl = new HtmlFormControl();
//    private HtmlDataTable dataTable = new HtmlDataTable();
    boolean renderRentalPanel = false;
    private boolean renderMessageDisplay = false;
    private DataModel<EstatePropertyLedger> estatePropertyLedgerDataModel;
    private String currentMonth = DateTimeUtils.getCurrentMonth();
    private int currentYear = DateTimeUtils.getCurrentYear();
    private boolean rentalUnitsBilled;
    private String fromMonth;
    private int fromYear;
    private String toMonth;
    private int toYear;
    private double amount;
    Calendar calendar = Calendar.getInstance();

    @PostConstruct
    private void init() {
        rentalUnitsBilled = ds.getCustomQry().isRentalUnitBilledForCurrentMonth(currentMonth, currentYear);

        if (rentalUnitsBilled == true) {
            setRenderMessageDisplay(true);
        }
    }

    public RentalBillingController() {
    }

    public void loadRentedPropertyBill() {
        renderRentalPanel = true;
        List<EstateProperty> rentedPropertyList = ds.getCustomQry().currentRentedEstateProperties("");
        rentalOccupantDetailList = new ArrayList<RentalOccupantDetail>();

        for (EstateProperty estateProperty : rentedPropertyList) {
            estatePropertyLedger = new EstatePropertyLedger();
            rentalOccupantDetail = new RentalOccupantDetail();

            estatePropertyLedger = ds.getCustomQry().lastDebitLedgerEntryForProperty(estateProperty.getEstatePropertyId());

            rentalOccupantDetail.setPropertyNumber(estatePropertyLedger.getEstateProperty().getEstatePropertyId());
            rentalOccupantDetail.setPropertyOccupant(estatePropertyLedger.getEstateProperty().getCurrentOccupantName());
            rentalOccupantDetail.setOldRent(estatePropertyLedger.getAmountInvolved());

            rentalOccupantDetailList.add(rentalOccupantDetail);
        }
        rentalOccupantDataModel = new ListDataModel<RentalOccupantDetail>(rentalOccupantDetailList);
    }

    public void updateRentalCharges() {
        selectedEstateProperty = new EstateProperty();
        estatePropertyLedgersList = new ArrayList<EstatePropertyLedger>();
        try {
            for (RentalOccupantDetail occupantDetail : rentalOccupantDetailList) {

                selectedEstateProperty = ds.getCommonQry().estatePropertyFind(occupantDetail.getPropertyNumber());
                estatePropertyLedgersList = ds.getCustomQry().loadLedgerEntriesForSelectedYear(selectedEstateProperty, selectedYear);

                for (EstatePropertyLedger ledger : estatePropertyLedgersList) {

                    if (ledger.getPaymentFor().contains(billingMonth)) {
                        if (occupantDetail.getNewRent() > 0) {
                            ledger.setAmountInvolved(occupantDetail.getNewRent());
                            ledger.setEstateProperty(selectedEstateProperty);
                            boolean updateSuccess = ds.getCommonQry().estatePropertyLedgerUpdate(ledger);
                            System.out.println("updateSuccess " + updateSuccess);
                        }
                    }
                }
            }
            String msg = "Rent bills updated successfully";
            JsfUtil.addInformationMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Failed to updated rent bill";
            JsfUtil.addErrorMessage(msg);
        }

    }

    public String billPropertyRentAsPreviousMonth() {
        List<EstateProperty> rentedPropertyList = ds.getCustomQry().currentRentedEstateProperties("");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        for (EstateProperty estateProperty : rentedPropertyList) {
            billingMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            estatePropertyLedger = new EstatePropertyLedger();
            PropertyOccupant occupant = ds.getCustomQry().estatePropertyCurrentOccupant(estateProperty);

            if (occupant == null) {
                continue;
            }

            Double lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(estateProperty.getEstatePropertyId());

            if (lastRentCharged == null) {
                continue;
            }

            estatePropertyLedger.setAmountInvolved(lastRentCharged);
            estatePropertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            estatePropertyLedger.setDateOfRecordEntry(new Date());
            estatePropertyLedger.setDateOfRecordTransaction(cal.getTime());
            estatePropertyLedger.setEstateProperty(estateProperty);
            estatePropertyLedger.setLedgerYear(cal.get(Calendar.YEAR));
            estatePropertyLedger.setPaymentType(houseRentPaymentType);
            estatePropertyLedger.setPropertyOccupant(occupant);
            estatePropertyLedger.setRecordedBy(AdConstants.SHC);
            estatePropertyLedger.setMediumOfPayment(AdConstants.NONE);
            estatePropertyLedger.setReceiptNumberIssued(AdConstants.NONE);
            estatePropertyLedger.setPayeeName(AdConstants.SHC);
            estatePropertyLedger.setPaymentFor("Property Rent ," + billingMonth + " " + cal.get(Calendar.YEAR));

            try {
                IDCreator.setEstateLedgerId(estatePropertyLedger, billingMonth);
                ds.getCommonQry().estatePropertyLedgerUpdate(estatePropertyLedger);

                System.out.println("updated rent ..  " + estatePropertyLedger);

            } catch (Exception e) {
                Logger.getLogger(RentalBillingController.class.getName()).log(Level.SEVERE, e.toString());
            }
        }

        String msg = "Last Rent billed rent has been used as this month rent";
        JsfUtil.addInformationMessage(msg);
        init();
        return "";
    }

    public String applyCurrentRentalCharge() {
        try {
            Calendar calendar = Calendar.getInstance();
            List<EstateProperty> listOfRentalProperties = ds.getCustomQry().currentRentedEstateProperties("");
            for (EstateProperty ep : listOfRentalProperties) {
                double currentRentalCharge = HouseRentBiller.getRentBiller().getCalculated(ep, currentMonth, currentYear);
                PropertyOccupant propertyOccupant = ds.getCustomQry().estatePropertyCurrentOccupant(ep);
                if ((propertyOccupant == null) && (currentRentalCharge == 0.0)) {
                    continue;
                }
                estatePropertyLedger = new EstatePropertyLedger();
                estatePropertyLedger.setAmountInvolved(currentRentalCharge);
                estatePropertyLedger.setEstateProperty(ep);
                estatePropertyLedger.setLedgerYear(currentYear);
                estatePropertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
                estatePropertyLedger.setPayeeName(AdConstants.SHC);
                estatePropertyLedger.setDateOfRecordEntry(new Date());
                estatePropertyLedger.setDateOfRecordTransaction(calendar.getTime());
                estatePropertyLedger.setPaymentFor(AdConstants.HSE_RENT_BILL + ", " + currentMonth + "" + currentYear);
                estatePropertyLedger.setPaymentType(houseRentPaymentType);
                IDCreator.setEstateLedgerId(estatePropertyLedger, currentMonth);

                ds.getCommonQry().estatePropertyLedgerCreate(estatePropertyLedger);
            }
            JsfUtil.addInformationMessage("Current House Rent Charge applied successfully");
        } catch (Exception e) {
        }
        return "";
    }

//    @SaveEditButtonAction(group = "epl")
    public String saveButtonAction() {
        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
        System.out.println("selected estate property = " + selectedEstateProperty);

        boolean validated = RentalBillingValidator.validateSaving(this);
        if (validated == false) {
            JsfUtil.addErrorMessage(RentalBillingValidator.msg);
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(estatePropertyLedger.getDateOfRecordTransaction());

        billingMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.UK);

        int ledgerYear = cal.get(Calendar.YEAR);

        try {
            estatePropertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
            estatePropertyLedger.setDateOfRecordEntry(new Date());
            estatePropertyLedger.setEstateProperty(selectedEstateProperty);
            estatePropertyLedger.setLedgerYear(ledgerYear);
            estatePropertyLedger.setPaymentType(houseRentPaymentType);
            estatePropertyLedger.setPropertyOccupant(propertySelector.selectedPropertyOccupant());
            estatePropertyLedger.setRecordedBy(userData.getUserId());
            estatePropertyLedger.setMediumOfPayment(AdConstants.NONE);
            estatePropertyLedger.setReceiptNumberIssued(AdConstants.NONE);
            estatePropertyLedger.setPayeeName(AdConstants.SHC);
            estatePropertyLedger.setPaymentFor("Property Rent ," + billingMonth + " " + ledgerYear);

            IDCreator.setEstateLedgerId(estatePropertyLedger, billingMonth);

            boolean updated = ds.getCommonQry().estatePropertyLedgerUpdate(estatePropertyLedger);

            
                JsfUtil.addInformationMessage("Bill Applied  sucessfully ");
           
                

        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyLedger.class.getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage("Error occured: failed to complete payment process");
        }

        clearButtonAction();

        return null;

    }

    private List<String> getOldLedgerIds(EstateProperty ep, int startYear, int endYear) {
        List<String> oldIds = new ArrayList<>();
        List<EstatePropertyLedger> eplList = ds.getCustomQry().loadLedgerEntriesBtnYears(selectedEstateProperty, fromYear, toYear, DebitCredit.DEBIT);
        for (Iterator<EstatePropertyLedger> iterator = eplList.iterator(); iterator.hasNext();) {
            EstatePropertyLedger next = iterator.next();
            oldIds.add(next.getEstatePropertyLedgerId());

        }
        return oldIds;
    }
    
    public int getIntMonth(String monthName) {
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
            DateTime dateTime = formatter.withLocale(Locale.ENGLISH).parseDateTime(monthName);
            return dateTime.getMonthOfYear();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public void applyBackBilling() throws ParseException {
        try {
            selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
            selectedPropertyOccupant = PropertySelector.getMgedInstance().selectedPropertyOccupant();

            System.out.println("selected property occupant " + selectedEstateProperty.getCurrentPropertyOccupant());
            String newId = null;
            if (amount == 0  ) {
                 JsfUtil.addErrorMessage("Please enter Amount");
            }
            int startMonth = monthInInteger(fromMonth);
            int endMonth = monthInInteger(toMonth);
            DateTime startDate = new DateTime(fromYear, startMonth, 1, 0, 0);
            DateTime endDate = new DateTime(toYear, endMonth, 1, 0, 0);
            Months m = Months.monthsBetween(startDate, endDate);
            int months = m.getMonths() + 1;

            estatePropertyLedger = new EstatePropertyLedger();
            List<EstatePropertyLedger> eplList = new ArrayList<>(ds.getCustomQry().loadLedgerEntriesBtnYears(selectedEstateProperty,
                    fromYear, toYear, DebitCredit.DEBIT));
            System.out.println("estate ledger list between years " + eplList.toString());
            for (int k = 0; k < months; k++) {
                DateTime dt = startDate.plusMonths(k);
                int ledgerYear = dt.getYear();
               String monthName = dt.monthOfYear().getAsText();
                DateTime dateSpe = new DateTime(ledgerYear, getIntMonth(monthName), 1, 0, 0);
                estatePropertyLedger.setEstateProperty(selectedEstateProperty);
                estatePropertyLedger.setPaymentFor("Property Rent ," + monthName + " " + ledgerYear);
                estatePropertyLedger.setPropertyOccupant(selectedEstateProperty.getCurrentPropertyOccupant());
                estatePropertyLedger.setPaymentType(houseRentPaymentType);
                estatePropertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
                estatePropertyLedger.setRecordedBy(userData.getUserId());
                estatePropertyLedger.setDateOfRecordEntry(new Date());
                estatePropertyLedger.setAmountInvolved(amount);
                estatePropertyLedger.setMediumOfPayment(AdConstants.NONE);
                estatePropertyLedger.setReceiptNumberIssued(AdConstants.NONE);
                estatePropertyLedger.setPayeeName(AdConstants.SHC);
                estatePropertyLedger.setLedgerYear(ledgerYear);
                estatePropertyLedger.setDateOfRecordTransaction(dateSpe.toDate());
                newId = IDCreator.setEstateLedgerId1(estatePropertyLedger, monthName);
                EstatePropertyLedger foundLedger = ds.getCommonQry().estatePropertyLedgerFind(newId);
                System.out.println("found ledger for " + monthName + " is " + foundLedger);
//                for (EstatePropertyLedger epl : eplList) {
//                    if (!epl.getEstatePropertyLedgerId().equalsIgnoreCase(newId)) {
//                        estatePropertyLedger.setEstatePropertyLedgerId(newId);
//                        ds.getCommonQry().estatePropertyLedgerCreate(estatePropertyLedger);
//                    }
//                }
                if(foundLedger == null){
                    estatePropertyLedger.setEstatePropertyLedgerId(newId);
                  String save = ds.getCommonQry().estatePropertyLedgerCreate(estatePropertyLedger);
                  if(save == null){
                      JsfUtil.addErrorMessage("Unable to save / create");
                  }
                  else{
                      JsfUtil.addInformationMessage("Saved / Created Successfully");
                      
                  }
                }
            }
                    clearButtonAction();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            JsfUtil.addErrorMessage("Failed: Unable to update payment record");
        }
        return null;
    }

//    @ClearButtonAction(group = "epl")
    public String clearButtonAction() {
        try {
            estatePropertyLedger = new EstatePropertyLedger();
            renderRentalPanel = false;
fromMonth = null;
toMonth = null;
fromYear = 0000;
toYear = 0000;
amount = 0;
//           totalCreditEntry = 0.0;
//           totalDebitEntry = 0.0;
//           outstandingBalance = 0.0;
//           propertyNumber = "";
//           propertyOccupantName = "";
//           estatePropertyLedgerList = null;
//            formControl.setSaveEditButtonTextTo_Save();
        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyLedger.class
                    .getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage(
                    "Error occurred in clearing EstatePropertyLedger form ");
        }

        return null;

    }

//    @DeleteButtonAction(group = "epl")
    public String deleteButtonAction() {
        try {
            if (estatePropertyLedger == null) {
                return null;
            }

            boolean deleted = ds.getCommonQry().estatePropertyLedgerDelete(estatePropertyLedger, true);

            if (deleted == true) {
                clearButtonAction();
                JsfUtil.addInformationMessage("Deletion completed");
            } else {
                JsfUtil.addErrorMessage("Failed to Delete EstatePropertyLedger");
                return null;

            }

        } catch (Exception exp) {
            Logger.getLogger(EstatePropertyLedger.class
                    .getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage(
                    "Failed to Delete EstatePropertyLedger");
        }

        return null;
    }

    public void loadRentalBilling() {
        selectedEstateProperty = propertySelector.getSelectedEstateProperty();

        if (selectedEstateProperty == null) {
            String msg = "No Estate Property Have been selected";
            JsfUtil.addErrorMessage(msg);

            return;
        }

        estatePropertyLedgersList = ds.getCustomQry().loadLedgerEntriesForPropertyOfCurrentOccupant(selectedEstateProperty, selectedYear);
        estatePropertyLedgerDataModel = new ListDataModel<EstatePropertyLedger>(estatePropertyLedgersList);
    }

//    @DataTableRowSelectionAction(group = "rb")
    public void billingRowSelectionAction() {
        try {
            estatePropertyLedger = estatePropertyLedgerDataModel.getRowData();
            renderRentalPanel = true;

        } catch (Exception exp) {
            Logger.getLogger(RentalBillingController.class
                    .getName()).log(Level.SEVERE, exp.toString(), exp);
            JsfUtil.addErrorMessage(
                    "Error occurred in selecting Rental Bill from table ");
        }

    }

    private int monthInInteger(String dateMonth) {
        int month;
        try {
            Date date = new SimpleDateFormat("MMMM", Locale.UK).parse(dateMonth);
            DateTime dt = new DateTime(date);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
            month = dt.getMonthOfYear();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return month;
    }

//    @SearchButtonAction(group = "rb")
    public void billingSearchAction() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public String getToMonth() {
        return toMonth;
    }

    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public boolean isRentalUnitsBilled() {
        return rentalUnitsBilled;
    }

    public void setRentalUnitsBilled(boolean rentalUnitsBilled) {
        this.rentalUnitsBilled = rentalUnitsBilled;
    }

    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    public void setCurrentTabIndex(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }

//    public HtmlDataTable getDataTable() {
//        return dataTable;
//    }
//
//    public void setDataTable(HtmlDataTable dataTable) {
//        this.dataTable = dataTable;
//    }
//
//    public EstatePropertyLedgerTableModel getEstatePropertyLedgerTableModel() {
//        return estatePropertyLedgerTableModel;
//    }
//
//    public void setEstatePropertyLedgerTableModel(EstatePropertyLedgerTableModel estatePropertyLedgerTableModel) {
//        this.estatePropertyLedgerTableModel = estatePropertyLedgerTableModel;
//    }
    public PaymentType getHouseRentPaymentType() {
        return houseRentPaymentType;
    }

    public void setHouseRentPaymentType(PaymentType houseRentPaymentType) {
        this.houseRentPaymentType = houseRentPaymentType;
    }

    public PropertySelector getPropertySelector() {
        return propertySelector;
    }

    public void setPropertySelector(PropertySelector propertySelector) {
        this.propertySelector = propertySelector;
    }

    public boolean isRenderMessageDisplay() {
        return renderMessageDisplay;
    }

    public void setRenderMessageDisplay(boolean renderMessageDisplay) {
        this.renderMessageDisplay = renderMessageDisplay;
    }

    public DataModel<RentalOccupantDetail> getRentalOccupantDataModel() {

        return rentalOccupantDataModel;
    }

    public void setRentalOccupantDataModel(DataModel<RentalOccupantDetail> rentalOccupantDataModel) {
        this.rentalOccupantDataModel = rentalOccupantDataModel;
    }

    public RentalOccupantDetail getRentalOccupantDetail() {
        return rentalOccupantDetail;
    }

    public void setRentalOccupantDetail(RentalOccupantDetail rentalOccupantDetail) {
        this.rentalOccupantDetail = rentalOccupantDetail;
    }

    public List<RentalOccupantDetail> getRentalOccupantDetailList() {
        return rentalOccupantDetailList;
    }

    public void setRentalOccupantDetailList(List<RentalOccupantDetail> rentalOccupantDetailList) {
        this.rentalOccupantDetailList = rentalOccupantDetailList;
    }

    public Occupancy getSelectedOccupancy() {
        return selectedOccupancy;
    }

    public void setSelectedOccupancy(Occupancy selectedOccupancy) {
        this.selectedOccupancy = selectedOccupancy;
    }

    public AdvantageUserData getUserData() {
        return userData;
    }

    public void setUserData(AdvantageUserData userData) {
        this.userData = userData;
    }

//    public HtmlDataPanel<EstatePropertyLedger> getEstatePropertyLedgerDataPanel() {
//        return estatePropertyLedgerDataPanel;
//    }
//
//    public void setEstatePropertyLedgerDataPanel(HtmlDataPanel<EstatePropertyLedger> estatePropertyLedgerDataPanel) {
//        this.estatePropertyLedgerDataPanel = estatePropertyLedgerDataPanel;
//    }
//
//    public HtmlFormControl getFormControl() {
//        return formControl;
//    }
//
//    public void setFormControl(HtmlFormControl formControl) {
//        this.formControl = formControl;
//    }
    public List<EstatePropertyLedger> getEstatePropertyLedgersList() {
        return estatePropertyLedgersList;
    }

    public void setEstatePropertyLedgersList(List<EstatePropertyLedger> estatePropertyLedgersList) {
        this.estatePropertyLedgersList = estatePropertyLedgersList;
    }

    public EstateProperty getSelectedRentedEstateProperty() {
        return selectedRentedEstateProperty;
    }

    public void setSelectedRentedEstateProperty(EstateProperty selectedRentedEstateProperty) {
        this.selectedRentedEstateProperty = selectedRentedEstateProperty;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public EstatePropertyLedger getEstatePropertyLedger() {
        return estatePropertyLedger;
    }

    public void setEstatePropertyLedger(EstatePropertyLedger estatePropertyLedger) {
        this.estatePropertyLedger = estatePropertyLedger;
    }

    public EstateProperty getSelectedEstateProperty() {
        return selectedEstateProperty;
    }

    public void setSelectedEstateProperty(EstateProperty selectedEstateProperty) {
        this.selectedEstateProperty = selectedEstateProperty;
    }

    public boolean isRenderRentalPanel() {
        return renderRentalPanel;
    }

    public void setRenderRentalPanel(boolean renderRentalPanel) {
        this.renderRentalPanel = renderRentalPanel;
    }

    public DataModel<EstatePropertyLedger> getEstatePropertyLedgerDataModel() {
        return estatePropertyLedgerDataModel;
    }

    public void setEstatePropertyLedgerDataModel(DataModel<EstatePropertyLedger> estatePropertyLedgerDataModel) {
        this.estatePropertyLedgerDataModel = estatePropertyLedgerDataModel;
    }

    public PropertyOccupant getSelectedPropertyOccupant() {
        return selectedPropertyOccupant;
    }

    public void setSelectedPropertyOccupant(PropertyOccupant selectedPropertyOccupant) {
        this.selectedPropertyOccupant = selectedPropertyOccupant;
    }
    // </editor-fold>
}

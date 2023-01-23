/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.OccupantType;
import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.common.utils.AdConstants;
import com.sabonay.advantage.common.utils.BillDemandNoteMaker;
import com.sabonay.advantage.common.utils.DateUtils;
import com.sabonay.advantage.common.utils.DemandNote;
import com.sabonay.advantage.common.utils.EstatesStaticsRunner;
import com.sabonay.advantage.common.utils.GroundRentBiller;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.common.utils.PropertiesUtils;
import com.sabonay.advantage.ejb.entities.ArreasRecord;
import com.sabonay.advantage.ejb.entities.Bills;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.BiilsPaidDetail;
import com.sabonay.advantage.ejb.models.BillDemandNote;
import com.sabonay.advantage.ejb.models.BillsSheet;
import com.sabonay.advantage.ejb.models.EstatePropertiesDistribution;
import com.sabonay.advantage.ejb.models.EstatePropertyOccupantDetail;
import com.sabonay.advantage.ejb.models.LedgerSheet;
import com.sabonay.advantage.ejb.models.PropertyDetail;
import com.sabonay.advantage.ejb.models.PropertyLedgerSummary;
import com.sabonay.advantage.ejb.models.RentalDetail;
import com.sabonay.advantage.web.reports.avReportManager;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.collection.CollectionUtils;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.formating.NumberFormattingUtils;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Edwin
 */
@Named(value = "generalStatisticsController")
@SessionScoped
public class GeneralStatisticsController implements Serializable {

//    private OccupantType propertyLoadingConstraint;
    private int selectedYear;
    private String selectReportType;
    private Estate selectedEstate;
    private String resultString = "";
    private PaymentType selectedPaymentType;
    private String reportTitle = "";
    
    
    
    
    private String selectedMonth;
    private String occupantType;
    private OccupantType occupancyType;
    private PropertyUsage propertyUsage;
    private PropOccupationType propertyLoadingConstraint;
    private Date groundRentPaymentDeadline = null, beginDate, endDate;
    private SelectItem[] calenderMonthNamesOptions;
    private List<EstateProperty> estatePropertiesList;
    
     private List<Bills> billsUnpaidList;
    private List<EstatePropertyLedger> estatePropertyList;
    private List<Object[]> objEplList;
    private DemandNote demandNote = new DemandNote();
    List<BiilsPaidDetail> biilsPaidDetailList;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
    SimpleDateFormat df = new SimpleDateFormat("E, dd MMMM yyyy");
    private SelectItem[] quarterlySelectOptions;
    private int currentTab = 0;
    private EstateProperty selectedEstateProperty;
    private double lastRentCharged = 0;
    
    private String billTitle ="";

    /**
     *
     * Assessment summary fields
     */
    private String assessmentSummary;
    private Double outstandingBalance;
    private int assessmentYear;
    private String assessmentMonth;
    private String assesssmentType;
    private int paymentUnit;
    private double assessmentLastRent;
    
    
    
    private String billStatus;

    public GeneralStatisticsController() {
    }

    public void processValueChange() {
//        if (event.getNewValue() != null) {
        //selectReportType = event.getNewValue().toString();
        if (selectReportType.equals("Monthly Report")) {
            calenderMonthNamesOptions = JsfUtil.createSelectItems(DateTimeUtils.getMonthNames(), false);
        } else if (selectReportType.equals("Quarterly Report")) {

            String quarterlyBaseSelectOptions[] = {"1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter"};
            quarterlySelectOptions = JsfUtil.createSelectItems(quarterlyBaseSelectOptions, false);

            calenderMonthNamesOptions = quarterlySelectOptions;
        } else if (selectReportType.equals("Mid Year Report")) {

            String quarterlyBaseSelectOptions[] = {"1st Mid Year", "2nd Mid Year"};
            quarterlySelectOptions = JsfUtil.createSelectItems(quarterlyBaseSelectOptions, false);

            calenderMonthNamesOptions = quarterlySelectOptions;
        } else if (selectReportType.equals("Annual Report")) {
            String quarterlyBaseSelectOptions[] = {"Month Disabled"};
            quarterlySelectOptions = JsfUtil.createSelectItems(quarterlyBaseSelectOptions, false);
            calenderMonthNamesOptions = quarterlySelectOptions;
        }
        // }
    }

    public void generatePaidRentals() {

        PaymentType paymentType = null;
        BiilsPaidDetail biilsPaidDetail = new BiilsPaidDetail();
        biilsPaidDetailList = new ArrayList<BiilsPaidDetail>();
        List<Estate> eps = ds.getCommonQry().estateGetAll(false);

        if (propertyLoadingConstraint == null) {
            JsfUtil.addErrorMessage("Please select the rent type");
            return;
        } else if (propertyLoadingConstraint.toString().equals("Leasehold")) {
            paymentType = PaymentType.GROUND_RENT;
        } else if (propertyLoadingConstraint.toString().equals("Rental")) {
            paymentType = PaymentType.HOUSE_RENT;
        }

        estatePropertyList = ds.getCustomQry().loadBillsPaidForThisPeriod(paymentType, beginDate, endDate);

        for (Estate estate : eps) {
            biilsPaidDetail.setEstateName(estate.getEstateName());
            for (EstatePropertyLedger epl : estatePropertyList) {
                if (epl.getEstateProperty().getEstatePropertyId().contains(estate.getEstateId())) {

                    biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                    biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                    biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                    biilsPaidDetail.setEstateInitials(estate.getEstateId());

                    biilsPaidDetailList.add(biilsPaidDetail);
                    biilsPaidDetail = new BiilsPaidDetail();

                }
            }
        }

        if (biilsPaidDetailList.size() < 1) {
            JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");

        } else {

            avReportManager.getInstance().addParam("reportTitle", "Report On " + paymentType + " From " + df.format(beginDate) + " to " + df.format(endDate));
            avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
        }
    }

    public void generateBillsPaidReport() {
        PaymentType paymentType = null;
        int count = 0;
        BiilsPaidDetail biilsPaidDetail = new BiilsPaidDetail();
        biilsPaidDetailList = new ArrayList<BiilsPaidDetail>();
        List<Estate> eps = ds.getCommonQry().estateGetAll(false);

        if (propertyLoadingConstraint == null) {
            JsfUtil.addErrorMessage("Please select the rent type");
            return;
        } else if (propertyLoadingConstraint.toString().equals("Leasehold")) {
            paymentType = PaymentType.GROUND_RENT;
        } else if (propertyLoadingConstraint.toString().equals("Rental")) {
            paymentType = PaymentType.HOUSE_RENT;
        }
        estatePropertyList = ds.getCustomQry().loadAllBillsPaid(selectedYear, paymentType);

        if (selectReportType.equalsIgnoreCase("Monthly Report")) {
            if (selectedMonth.equals("null")) {
                JsfUtil.addErrorMessage("Please select a month");
            } else {
                for (Estate estate : eps) {
                    biilsPaidDetail.setEstateName(estate.getEstateName());
                    for (EstatePropertyLedger epl : estatePropertyList) {
                        if (epl.getEstateProperty().getEstatePropertyId().contains(estate.getEstateId())) {
                            Date transDate = epl.getDateOfRecordTransaction();
                            if (transDate == null) {
                                JsfUtil.addErrorMessage("Unable to generate report");
                                return;
                            }
                            String transMonth = dateFormat.format(transDate);
                            if (selectedMonth.equalsIgnoreCase(transMonth)) {
                                biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                                biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                                biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                                biilsPaidDetail.setEstateInitials(estate.getEstateId());
                                biilsPaidDetail.setReceiptNumber(epl.getReceiptNumberIssued());
                                biilsPaidDetail.setHseType(epl.getEstateProperty().getPropertyUsage().getUsageName());

                                biilsPaidDetailList.add(biilsPaidDetail);
                                biilsPaidDetail = new BiilsPaidDetail();
                            }
//                            biilsPaidDetail = new BiilsPaidDetail();
                        }
                    }
                }

                if (biilsPaidDetailList.size() < 1) {
                    JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");

                } else {

                    avReportManager.getInstance().addParam("reportTitle", "Monthly Report On " + paymentType + " for the Month " + selectedMonth + "," + selectedYear);
                    avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
                }

            }
        } else if (selectReportType.equalsIgnoreCase("Annual Report")) {
            for (Estate estate : eps) {
                biilsPaidDetail.setEstateName(estate.getEstateName());
                for (EstatePropertyLedger epl : estatePropertyList) {
                    if (epl.getEstateProperty().getEstatePropertyId().contains(estate.getEstateId())) {
                        biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                        biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                        biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                        biilsPaidDetail.setEstateInitials(estate.getEstateId());

                        biilsPaidDetailList.add(biilsPaidDetail);
                        biilsPaidDetail = new BiilsPaidDetail();
                    }
                }
            }
            if (biilsPaidDetailList.size() < 1) {
                JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");

            } else {

                avReportManager.getInstance().addParam("reportTitle", "Annual Report On " + paymentType + " for the Year " + selectedYear);
                avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
            }
        } else if (selectReportType.equalsIgnoreCase("Quarterly Report")) {
            for (Estate estate : eps) {
                biilsPaidDetail.setEstateName(estate.getEstateName());
                for (EstatePropertyLedger epl : estatePropertyList) {
//                    if (epl.getEstateProperty().getEstatePropertyId().contains(estate.getEstateId())) {
                    int transMonth = epl.getDateOfRecordTransaction().getMonth();
                    if (transMonth < 0) {
                        return;
                    }

                    if (selectedMonth.equals("1st Quarter")) {
                        if (transMonth <= 2) {
                            biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                            biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                            biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                            biilsPaidDetail.setEstateInitials(estate.getEstateId());

                            biilsPaidDetailList.add(biilsPaidDetail);
                            biilsPaidDetail = new BiilsPaidDetail();
                        }
                    } else if (selectedMonth.equals("2nd Quarter")) {
                        if (transMonth > 2 && transMonth < 6) {
                            biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                            biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                            biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                            biilsPaidDetail.setEstateInitials(estate.getEstateId());

                            biilsPaidDetailList.add(biilsPaidDetail);
                            biilsPaidDetail = new BiilsPaidDetail();
                        }

                    } else if (selectedMonth.equals("3rd Quarter")) {
                        if (transMonth > 5 && transMonth < 9) {
                            biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                            biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                            biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                            biilsPaidDetail.setEstateInitials(estate.getEstateId());

                            biilsPaidDetailList.add(biilsPaidDetail);
                            biilsPaidDetail = new BiilsPaidDetail();
                        }

                    } else if (selectedMonth.equals("4th Quarter")) {
                        if (transMonth > 8 && transMonth < 12) {
                            biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                            biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                            biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                            biilsPaidDetail.setEstateInitials(estate.getEstateId());

                            biilsPaidDetailList.add(biilsPaidDetail);
                            biilsPaidDetail = new BiilsPaidDetail();
                        }

                    }
//                    }
                }
            }
            if (biilsPaidDetailList.size() < 1) {
                JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");
            } else {
                avReportManager.getInstance().addParam("reportTitle", "Quarterly Report On " + paymentType + " for the " + selectedMonth + " of " + selectedYear);
                avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
            }
        } else if (selectReportType.equalsIgnoreCase("Mid Year Report")) {
            for (Estate estate : eps) {
                biilsPaidDetail.setEstateName(estate.getEstateName());
                for (EstatePropertyLedger epl : estatePropertyList) {
                    if (epl.getEstateProperty().getEstatePropertyId().contains(estate.getEstateId())) {
                        int transMonth = epl.getDateOfRecordTransaction().getMonth();

                        if (selectedMonth.equals("1st Mid Year")) {
                            if (transMonth <= 5) {
                                biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                                biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                                biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                                biilsPaidDetail.setEstateInitials(estate.getEstateId());

                                biilsPaidDetailList.add(biilsPaidDetail);
                                biilsPaidDetail = new BiilsPaidDetail();
                            }
                        } else if (selectedMonth.equals("2nd Mid Year")) {
                            if (transMonth > 5 && transMonth < 12) {
                                biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
                                biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
                                biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
                                biilsPaidDetail.setEstateInitials(estate.getEstateId());

                                biilsPaidDetailList.add(biilsPaidDetail);
                                biilsPaidDetail = new BiilsPaidDetail();
                            }
                        }

                    }
                }
            }
            if (biilsPaidDetailList.size() < 1) {
                JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");

            } else {

                avReportManager.getInstance().addParam("reportTitle", "Mid Year Report On " + paymentType + " for the " + selectedMonth + " of " + selectedYear);
                avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
            }
        }

    }

//    public void generateBillsPaidReport2() {
//        PaymentType paymentType = null;
//        int count = 0;
//        BiilsPaidDetail biilsPaidDetail = new BiilsPaidDetail();
//        biilsPaidDetailList = new ArrayList<BiilsPaidDetail>();
//        List<Estate> eps = ds.getCommonQry().estateGetAll(false);
//
//        if (propertyLoadingConstraint == null) {
//            JsfUtil.addErrorMessage("Please select the rent type");
//            return;
//        } else if (propertyLoadingConstraint.toString().equals("Leasehold")) {
//            paymentType = PaymentType.GROUND_RENT;
//        } else if (propertyLoadingConstraint.toString().equals("Rental")) {
//            paymentType = PaymentType.HOUSE_RENT;
//        }
//        List<Object[]> estatePropertyObjList = ds.getCustomQry().loadAllBill(selectedYear, paymentType);
//
//        if (selectReportType.equalsIgnoreCase("Monthly Report")) {
//            if (selectedMonth.equals("null")) {
//                JsfUtil.addErrorMessage("Please select a month");
//            } else {
//                for (EstateProperty ep : ) {
//                    biilsPaidDetail.setEstateName(estate.getEstateName());
//                    for (Object[] epl : estatePropertyObjList) {
//                        DebitCredit debitCredit = (DebitCredit) epl[3];
//                        EstateProperty ep = (EstateProperty) epl[1];
//                        System.out.println("----found estate properties---" + ep);
////                        if (ep.getEstatePropertyId().contains(estate.getEstateId())) {
//                        Date transDate = (Date) epl[2];
//                        if (transDate == null) {
//                            JsfUtil.addErrorMessage("Unable to generate report");
//                            return;
//                        }
//                        String transMonth = dateFormat.format(transDate);
//                        if (selectedMonth.equalsIgnoreCase(transMonth)) {
//                            biilsPaidDetail.setOccupantName(ep.getCurrentOccupantName());
//                            biilsPaidDetail.setPropertyName(ep.getPropertyName());
//                            if (debitCredit == DebitCredit.CREDIT) {
//                                biilsPaidDetail.setAmountPaid(ObjectFormat.getDoubleObject(epl[0]));
//                            }
//                            if (debitCredit == DebitCredit.DEBIT) {
//                                biilsPaidDetail.setCurrentPropertyRent(ObjectFormat.getDoubleObject(epl[0]));
//                            }
//                            biilsPaidDetail.setEstateInitials(estate.getEstateId());
//
//                            biilsPaidDetail.setHseType(ep.getPropertyUsage().getUsageName());
//
//                            biilsPaidDetailList.add(biilsPaidDetail);
//                            biilsPaidDetail = new BiilsPaidDetail();
//                        }
////                            biilsPaidDetail = new BiilsPaidDetail();
////                        }
//                    }
//                }
//
//                if (biilsPaidDetailList.size() < 1) {
//                    JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");
//
//                } else {
//
//                    avReportManager.getInstance().addParam("reportTitle", "Monthly Report On " + paymentType + " for the Month " + selectedMonth + "," + selectedYear);
//                    avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
//                }
//
//            }
//        } else if (selectReportType.equalsIgnoreCase("Annual Report")) {
//            for (Estate estate : eps) {
//                biilsPaidDetail.setEstateName(estate.getEstateName());
//                for (Object[] epl : estatePropertyObjList) {
//                    EstateProperty ep = (EstateProperty) epl[1];
//                    DebitCredit debitCredit = (DebitCredit) epl[3];
//                    if (ep.getEstatePropertyId().contains(estate.getEstateId())) {
//                        biilsPaidDetail.setOccupantName(ep.getCurrentOccupantName());
//                        biilsPaidDetail.setPropertyName(ep.getPropertyName());
//                        if (debitCredit == DebitCredit.CREDIT) {
//                            biilsPaidDetail.setAmountPaid(ObjectFormat.getDoubleObject(epl[0]));
//                        }
//                        if (debitCredit == DebitCredit.DEBIT) {
//                            biilsPaidDetail.setCurrentPropertyRent(ObjectFormat.getDoubleObject(epl[0]));
//                        }
//                        biilsPaidDetail.setEstateInitials(estate.getEstateId());
//
//                        biilsPaidDetailList.add(biilsPaidDetail);
//                        biilsPaidDetail = new BiilsPaidDetail();
//                    }
//                }
//            }
//            if (biilsPaidDetailList.size() < 1) {
//                JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");
//
//            } else {
//
//                avReportManager.getInstance().addParam("reportTitle", "Annual Report On " + paymentType + " for the Year " + selectedYear);
//                avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
//            }
//        } else if (selectReportType.equalsIgnoreCase("Quarterly Report")) {
//            for (Estate estate : eps) {
//                biilsPaidDetail.setEstateName(estate.getEstateName());
//                for (Object[] epl : estatePropertyObjList) {
//                    EstateProperty ep = (EstateProperty) epl[1];
//                    if (ep.getEstatePropertyId().contains(estate.getEstateId())) {
//                        Date transDate = (Date) epl[2];
//                        DebitCredit debitCredit = (DebitCredit) epl[3];
//                        int transMonth = transDate.getMonth();
//                        if (transMonth < 0) {
//                            return;
//                        }
//
//                        if (selectedMonth.equals("1st Quarter")) {
//                            if (transMonth <= 2) {
//                                biilsPaidDetail.setOccupantName(ep.getCurrentOccupantName());
//                                biilsPaidDetail.setPropertyName(ep.getPropertyName());
//                                if (debitCredit == DebitCredit.CREDIT) {
//                                    biilsPaidDetail.setAmountPaid(ObjectFormat.getDoubleObject(epl[0]));
//                                }
//                                if (debitCredit == DebitCredit.DEBIT) {
//                                    biilsPaidDetail.setCurrentPropertyRent(ObjectFormat.getDoubleObject(epl[0]));
//                                }
//                                biilsPaidDetail.setEstateInitials(estate.getEstateId());
//                                biilsPaidDetailList.add(biilsPaidDetail);
//                                biilsPaidDetail = new BiilsPaidDetail();
//                            }
//                        } else if (selectedMonth.equals("2nd Quarter")) {
//                            if (transMonth > 2 && transMonth < 6) {
//                                biilsPaidDetail.setOccupantName(ep.getCurrentOccupantName());
//                                biilsPaidDetail.setPropertyName(ep.getPropertyName());
//                                if (debitCredit == DebitCredit.CREDIT) {
//                                    biilsPaidDetail.setAmountPaid(ObjectFormat.getDoubleObject(epl[0]));
//                                }
//                                if (debitCredit == DebitCredit.DEBIT) {
//                                    biilsPaidDetail.setCurrentPropertyRent(ObjectFormat.getDoubleObject(epl[0]));
//                                }
//                                biilsPaidDetail.setEstateInitials(estate.getEstateId());
//
//                                biilsPaidDetailList.add(biilsPaidDetail);
//                                biilsPaidDetail = new BiilsPaidDetail();
//                            }
//
//                        } else if (selectedMonth.equals("3rd Quarter")) {
//                            if (transMonth > 5 && transMonth < 9) {
//                                biilsPaidDetail.setOccupantName(ep.getCurrentOccupantName());
//                                biilsPaidDetail.setPropertyName(ep.getPropertyName());
//                                if (debitCredit == DebitCredit.CREDIT) {
//                                    biilsPaidDetail.setAmountPaid(ObjectFormat.getDoubleObject(epl[0]));
//                                }
//                                if (debitCredit == DebitCredit.DEBIT) {
//                                    biilsPaidDetail.setCurrentPropertyRent(ObjectFormat.getDoubleObject(epl[0]));
//                                }
//
//                                biilsPaidDetail.setEstateInitials(estate.getEstateId());
//
//                                biilsPaidDetailList.add(biilsPaidDetail);
//                                biilsPaidDetail = new BiilsPaidDetail();
//                            }
//
//                        } else if (selectedMonth.equals("4th Quarter")) {
//                            if (transMonth > 8 && transMonth < 12) {
//                                biilsPaidDetail.setOccupantName(ep.getCurrentOccupantName());
//                                biilsPaidDetail.setPropertyName(ep.getPropertyName());
//                                if (debitCredit == DebitCredit.CREDIT) {
//                                    biilsPaidDetail.setAmountPaid(ObjectFormat.getDoubleObject(epl[0]));
//                                }
//                                if (debitCredit == DebitCredit.DEBIT) {
//                                    biilsPaidDetail.setCurrentPropertyRent(ObjectFormat.getDoubleObject(epl[0]));
//                                }
//                                biilsPaidDetail.setEstateInitials(estate.getEstateId());
//
//                                biilsPaidDetailList.add(biilsPaidDetail);
//                                biilsPaidDetail = new BiilsPaidDetail();
//                            }
//
//                        }
//
//                    }
//                }
//            }
//            if (biilsPaidDetailList.size() < 1) {
//                JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");
//
//            } else {
//
//                avReportManager.getInstance().addParam("reportTitle", "Quarterly Report On " + paymentType + " for the " + selectedMonth + " of " + selectedYear);
//                avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
//            }
//        } else if (selectReportType.equalsIgnoreCase("Mid Year Report")) {
//            for (Estate estate : eps) {
//                biilsPaidDetail.setEstateName(estate.getEstateName());
//                for (EstatePropertyLedger epl : estatePropertyList) {
//                    if (epl.getEstateProperty().getEstatePropertyId().contains(estate.getEstateId())) {
//                        int transMonth = epl.getDateOfRecordTransaction().getMonth();
//
//                        if (selectedMonth.equals("1st Mid Year")) {
//                            if (transMonth <= 5) {
//                                biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
//                                biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
//                                biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
//                                biilsPaidDetail.setEstateInitials(estate.getEstateId());
//
//                                biilsPaidDetailList.add(biilsPaidDetail);
//                                biilsPaidDetail = new BiilsPaidDetail();
//                            }
//                        } else if (selectedMonth.equals("2nd Mid Year")) {
//                            if (transMonth > 5 && transMonth < 12) {
//                                biilsPaidDetail.setOccupantName(epl.getEstateProperty().getCurrentOccupantName());
//                                biilsPaidDetail.setPropertyName(epl.getEstateProperty().getPropertyName());
//                                biilsPaidDetail.setAmountPaid(epl.getAmountInvolved());
//                                biilsPaidDetail.setEstateInitials(estate.getEstateId());
//
//                                biilsPaidDetailList.add(biilsPaidDetail);
//                                biilsPaidDetail = new BiilsPaidDetail();
//                            }
//                        }
//
//                    }
//                }
//            }
//            if (biilsPaidDetailList.size() < 1) {
//                JsfUtil.addErrorMessage("Unable to generate report because no records were found for this query");
//
//            } else {
//
//                avReportManager.getInstance().addParam("reportTitle", "Mid Year Report On " + paymentType + " for the " + selectedMonth + " of " + selectedYear);
//                avReportManager.getInstance().showReport(biilsPaidDetailList, avReportManager.getInstance().BILLS_PAID_REPORT);
//            }
//        }
//    }
    public void propertyUsageReport() {
        List<EstatePropertiesDistribution> list = EstatesStaticsRunner.preparePropertiesDistribution(propertyLoadingConstraint);
        avReportManager.getInstance().addParam("reportTitle", "Estate Property Distribution - " + propertyLoadingConstraint.name());
        avReportManager.getInstance().showReport(list, avReportManager.getInstance().ESTATE_PROPERTIES_DISTRIBUTION);
    }
    
    
    
//    public void billPaidReport(){
//           
//        billTitle = "Paid Bills Sheet";
//        
//          List<BillsSheet> listofallsheet = EstatesStaticsRunner.preparePaidBillsSheet(billStatus);
//       // List<BillsSheet> listofallsheet = EstatesStaticsRunner.preparePaidBillsSheet(billStatus);
//        System.out.println("......................list "+listofallsheet);
//         //  avReportManager.getInstance().addParam("billStatus", billStatus);
//        avReportManager.getInstance().addParam("billTitle", billTitle);
//        avReportManager.getInstance().showReport(listofallsheet, avReportManager.getInstance().BILLS_PAID_LEDGER_SHEET);
//        
//    }

    public void yearLedgerReport() {
        if (propertyLoadingConstraint == null) {
            JsfUtil.addErrorMessage("Please select the property type");
            return;
        } else if (propertyLoadingConstraint.toString().equals("Leasehold")) {
            selectedPaymentType = PaymentType.GROUND_RENT;
        } else if (propertyLoadingConstraint.toString().equals("Rental")) {
            selectedPaymentType = PaymentType.HOUSE_RENT;
        } else if (propertyLoadingConstraint.toString().equals("NONE")) {
            selectedPaymentType = null;
        }

        if (selectedPaymentType != null) {
            reportTitle = "Estate Ledger Sheet (" + selectedPaymentType.getPaymentTypeName() + "), " + selectedYear;
        }

        if (selectReportType.equalsIgnoreCase("Monthly Report")) {
            if (selectedMonth == null) {
                JsfUtil.addErrorMessage("Please select month");
            }
            reportTitle = "Estate Ledger Sheet-Return Of Arrear For " + selectedMonth + ", " + selectedYear;

            List<LedgerSheet> list = EstatesStaticsRunner.prepareEstateLedgerSheet(selectReportType, selectedMonth, selectedYear, selectedPaymentType);

            avReportManager.getInstance().addParam("reportTitle", reportTitle);
            avReportManager.getInstance().addParam("ledgerYear", selectedYear);
            avReportManager.getInstance().showReport(list, avReportManager.getInstance().ESTATE_LEDGER_SHEET);
        } else if (selectReportType.equalsIgnoreCase("Quarterly Report")) {
            if (selectedMonth == null) {
                JsfUtil.addErrorMessage("Please select a quarter");
            }
            reportTitle = "Estate Ledger Sheet-Return Of Arrear For " + selectedMonth + ", " + selectedYear;
            List<LedgerSheet> list = EstatesStaticsRunner.prepareEstateLedgerSheet(selectReportType, selectedMonth, selectedYear, selectedPaymentType);

            avReportManager.getInstance().addParam("reportTitle", reportTitle);
            avReportManager.getInstance().addParam("ledgerYear", selectedYear);
            avReportManager.getInstance().showReport(list, avReportManager.getInstance().ESTATE_LEDGER_SHEET);
        } else if (selectReportType.equalsIgnoreCase("Mid Year Report")) {
            if (selectedMonth == null) {
                JsfUtil.addErrorMessage("Please select a mid year");
            }
            reportTitle = "Estate Ledger Sheet-Return Of Arrear For " + selectedMonth + ", " + selectedYear;
            List<LedgerSheet> list = EstatesStaticsRunner.prepareEstateLedgerSheet(selectReportType, selectedMonth, selectedYear, selectedPaymentType);

            avReportManager.getInstance().addParam("reportTitle", reportTitle);
            avReportManager.getInstance().addParam("ledgerYear", selectedYear);
            avReportManager.getInstance().showReport(list, avReportManager.getInstance().ESTATE_LEDGER_SHEET);
        } else if (selectReportType.equalsIgnoreCase("Annual Report")) {
            reportTitle = "Estate Ledger Sheet-Return Of Arrear For " + selectedYear;
            List<LedgerSheet> list = EstatesStaticsRunner.prepareEstateLedgerSheet(selectReportType, null, selectedYear, selectedPaymentType);

            avReportManager.getInstance().addParam("reportTitle", reportTitle);
            avReportManager.getInstance().addParam("ledgerYear", selectedYear);
            avReportManager.getInstance().showReport(list, avReportManager.getInstance().ESTATE_LEDGER_SHEET);
        }
    }

    public void detailedEstatePropertyReport() {
        reportTitle = "Detailed Estate Property Sheet - " + selectedEstate.getEstateName() + ", " + selectedYear;
        if (propertyLoadingConstraint == null) {
            JsfUtil.addErrorMessage("Please selected the property type");
        } else if (propertyLoadingConstraint.toString().equalsIgnoreCase("Leasehold")) {
            selectedPaymentType = PaymentType.GROUND_RENT;
        } else if (propertyLoadingConstraint.toString().equalsIgnoreCase("Rental")) {
            selectedPaymentType = PaymentType.HOUSE_RENT;
        }
        List<PropertyDetail> pdList = EstatesStaticsRunner.prepareEstatePropertyDetail(selectedYear, selectedPaymentType, selectedEstate);
        try {
            avReportManager.getInstance().addParam(reportTitle, reportTitle);
            avReportManager.getInstance().showReport(pdList, avReportManager.DETAILED_ESTATE_PTY_SHEET);
        } catch (Exception e) {
        }
    }

    public void propertyOccupants() {
        List<EstatePropertyOccupantDetail> propertyDetailsList = new LinkedList<EstatePropertyOccupantDetail>();

        List<EstateProperty> propertyList = ds.getCustomQry().allEstatesBlock(selectedEstate.getEstateId());

        CollectionUtils.sortStringValue(propertyList);

        for (EstateProperty estateProperty : propertyList) {
            propertyDetailsList.add(PropertiesUtils.getPropertyDetail(estateProperty.getEstatePropertyId()));
        }

        avReportManager.getInstance().addParam("reportTitle", selectedEstate.getEstateName() + " Current Property Occupant");

        avReportManager.getInstance().showReport(propertyDetailsList, avReportManager.getInstance().PROPERTY_OCCUPANT);

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

     
    public DateTime getLastDate(int mon, int year) {
        int daysInMonth = 0;
        if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            daysInMonth = 30;
        } else if (mon == 2) {
            if ((year % 4) == 0) {
                daysInMonth = 29;
            } else {
                daysInMonth = 28;
            }
        } else {
            daysInMonth = 31;
        }
        DateTime dt = new DateTime(selectedYear, mon, daysInMonth, 0, 0);

        return dt;
    }

    public void reportRentalReport() {
        Date endDate = null;
        Date startDate = null; 
        Double totalDebit = 0.00;
        Double totalCredit = 0.00;
        Double outStandingBal = 0.00;
        Double debit = 0.00;
        Double arrears = 0.00;
        List<EstatePropertyLedger> eplObjList = null;
        List<RentalDetail> rentalDetailList = new ArrayList<>();
        try {
            //Get current rented estates
            List<EstateProperty> epList = ds.getCustomQry().currentRentedEstateProperties("");
            if (selectReportType.equalsIgnoreCase("Monthly Report")) {
                if (selectedMonth == null) {
                    JsfUtil.addErrorMessage("Please select a month for report");
                } else {
                    //Iterate over the estate list
//                    System.out.println("list of estate properties  " + epList);    
                    for (EstateProperty ep : epList) {
                        totalCredit = 0.00;
                        totalDebit = 0.00;
                        outStandingBal = 0.00;
                        this.outstandingBalance = 0.00;
                        debit = 0.00;
                        PaymentType paymentType = ep.supposePaymentType();
                        if (paymentType == null) {
                            break;
                        } 
                        RentalDetail rentalDetail = new RentalDetail();
                        //Load the ledger last Rent Bill for occupant
//                        System.out.println("last rent charged");
                        lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(ep.getEstatePropertyId());
//                        System.out.println("bedfore last date");
                        endDate = getLastDate(getIntMonth(selectedMonth), selectedYear).toDate();
//                        System.out.println("filling details");
                        //Set rental detail model properties
                        rentalDetail.setPropertyName(ep.getPropertyName());
                        rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                        rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                        rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                        rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                        rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                        rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                        rentalDetail.setCurrentPropertyRent(lastRentCharged);

                        //load legder between 
                        List<EstatePropertyLedger> startDateList = ds.getCustomQry().loadAllLedgerEntriesForProperty(ep, selectedYear);
//                        System.out.println("loaded all ledgers");
                        if (startDateList.isEmpty()) {
                            break;
                        } else {
                            startDate = startDateList.get(0).getDateOfRecordTransaction();
                            
//                            System.out.println("got your start date" + startDate);
                            eplObjList = ds.getCustomQry().getRentalArrearsForEstate(ep, startDate, endDate);
//                            System.out.println("got arrears for estate");
                        }

                        for (EstatePropertyLedger obj : eplObjList) {
                            if (obj.getDebitCreditEntry() == DebitCredit.DEBIT) {
                                debit = obj.getAmountInvolved();
                                if (debit == null) {
                                    debit = lastRentCharged;
                                }
                                totalDebit += debit;

                            }
                            if (obj.getDebitCreditEntry() == DebitCredit.CREDIT) {
                                totalCredit += obj.getAmountInvolved();

                            }
                        }
                        outStandingBal = totalDebit - totalCredit;
                        System.out.println("estate Property: " + ep.getPropertyName() + "total debit: " + totalDebit + " - total Credit: " + totalCredit + " = outstanding Bal: "
                                + outStandingBal + "lastbillAmount" + lastRentCharged + " enddate : " + endDate + "startdate: " + startDate);
                        rentalDetail.setAmountPaid(totalCredit);
                        try {
                            if (outStandingBal < 0) {
                                assessmentSummary = "Paid in Excess";
                            } else {
                                int startYear = startDateList.get(0).getLedgerYear();
                                Calendar calMonth = Calendar.getInstance();
                                calMonth.setTime(startDate);
                                int startmonth = calMonth.get(Calendar.MONTH) + 1;
                                int numberOfMonthsInArrears = (((selectedYear - startYear) * 12) + (getIntMonth(selectedMonth) - startmonth) + 1);
                                assessmentSummary = numberOfMonthsInArrears + " Mth(s) from " + startYear + "/" + startmonth + " to " + selectedYear + "/" + getIntMonth(selectedMonth);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Calculate rent assessment
                        this.outstandingBalance = outStandingBal;

                        this.selectedEstateProperty = ep;
                        this.assessmentYear = selectedYear;
                        this.assessmentMonth = selectedMonth;
                        this.assesssmentType = selectReportType;
//                        rentYearAssessment();
                        rentalDetail.setAssessmentSummary(assessmentSummary);
                        arrears = NumberFormattingUtils.formatDecimalNumberTo_2(totalDebit); 
                        rentalDetail.setArrears(arrears);
                        

                        //Add detail to list
                        rentalDetailList.add(rentalDetail);
                    }
                }
                avReportManager.getInstance().addParam("reportTitle", "Return Of Arrears " + selectReportType + " On " + PaymentType.HOUSE_RENT + " for " + selectedMonth + "," + selectedYear);
                avReportManager.getInstance().showReport(rentalDetailList, avReportManager.getInstance().RETURN_OF_ARREARS);
            } else if (selectReportType.equalsIgnoreCase("Mid Year Report")) {
                if (selectedMonth == null) {
                    JsfUtil.addErrorMessage("Please select a mid year for report");
                } else if (selectedMonth.equalsIgnoreCase("1st Mid Year")) {
                    String month = "June";
                    for (EstateProperty ep : epList) {
                        totalCredit = 0.00;
                        totalDebit = 0.00;
                        outStandingBal = 0.00;
                        this.outstandingBalance = 0.00;
                        debit = 0.00;
                        RentalDetail rentalDetail = new RentalDetail();
                        //Load the ledger for the estate base on month
                        lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(ep.getEstatePropertyId());
                        //set endDate
                        endDate = getLastDate(getIntMonth(month), selectedYear).toDate();
//                       //Set rental detail model properties
                        rentalDetail.setPropertyName(ep.getPropertyName());
                        rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                        rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                        rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                        rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                        rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                        rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                        rentalDetail.setCurrentPropertyRent(lastRentCharged);

                            List<EstatePropertyLedger> startDateList = ds.getCustomQry().loadAllLedgerEntriesForProperty(ep, selectedYear);
                        if (startDateList.isEmpty()) {
                            break;
                        } else {
                            startDate = startDateList.get(0).getDateOfRecordTransaction();
                            eplObjList = ds.getCustomQry().getRentalArrearsForEstate(ep, startDate, endDate);
                        }
                        for (EstatePropertyLedger obj : eplObjList) {
                            if (obj.getDebitCreditEntry() == DebitCredit.DEBIT) {
                                debit = obj.getAmountInvolved();
                                if (debit == null) {
                                    debit = lastRentCharged;
                                }
                                totalDebit += debit;

                            }
                            if (obj.getDebitCreditEntry() == DebitCredit.CREDIT) {
                                totalCredit += obj.getAmountInvolved();

                            }
                        }

                        outStandingBal = totalDebit - totalCredit;
                        System.out.println("estate Property: " + ep.getPropertyName() + "total debit: " + totalDebit + " - total Credit: " + totalCredit + " = outstanding Bal: "
                                + outStandingBal + "lastbillAmount" + lastRentCharged + " enddate : " + endDate + "startdate: " + startDate);
                        rentalDetail.setAmountPaid(totalCredit);
                        try {
                            if (outStandingBal < 0) {
                                assessmentSummary = "Paid in Excess";
                            } else {
                                int startYear = startDateList.get(0).getLedgerYear();
                                Calendar calMonth = Calendar.getInstance();
                                calMonth.setTime(startDate);
                                int startmonth = calMonth.get(Calendar.MONTH) + 1;
                                int numberOfMonthsInArrears = (((selectedYear - startYear) * 12) + (getIntMonth(month) - startmonth) + 1);
                                assessmentSummary = numberOfMonthsInArrears + " Mth(s) from " + startYear + "/" + startmonth + " to " + selectedYear + "/" + getIntMonth(month);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Calculate rent assessment
                        this.outstandingBalance = outStandingBal;
                        this.selectedEstateProperty = ep;
                        this.assessmentYear = selectedYear;
                        this.assessmentMonth = selectedMonth;
                        this.assesssmentType = selectReportType; 
                        arrears = NumberFormattingUtils.formatDecimalNumberTo_2(totalDebit);
                        rentalDetail.setAssessmentSummary(assessmentSummary);
                        rentalDetail.setArrears(arrears);

                        //Add detail to list
                        rentalDetailList.add(rentalDetail);
                    }
                } else if (selectedMonth.equalsIgnoreCase("2nd Mid Year")) {
                    String month = "December";
                    for (EstateProperty ep : epList) {
                        totalCredit = 0.00;
                        totalDebit = 0.00;
                        outStandingBal = 0.00;
                        this.outstandingBalance = 0.00;
                        debit = 0.00;
                        RentalDetail rentalDetail = new RentalDetail();
                        //Load the ledger for the estate base on month
                        lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(ep.getEstatePropertyId());

                        endDate = getLastDate(getIntMonth(month), selectedYear).toDate();

                        //Set rental detail model properties
                        rentalDetail.setPropertyName(ep.getPropertyName());
                        rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                        rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                        rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                        rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                        rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                        rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                        rentalDetail.setCurrentPropertyRent(lastRentCharged);
 
                        //load legder between 
                        List<EstatePropertyLedger> startDateList = ds.getCustomQry().loadAllLedgerEntriesForProperty(ep, selectedYear);
                        if (startDateList.isEmpty()) {
                            break;
                        } else {
                            startDate = startDateList.get(0).getDateOfRecordTransaction();
                            eplObjList = ds.getCustomQry().getRentalArrearsForEstate(ep, startDate, endDate);
                        }
                        for (EstatePropertyLedger obj : eplObjList) {
                            if (obj.getDebitCreditEntry() == DebitCredit.DEBIT) {
                                debit = obj.getAmountInvolved();
                                if (debit == null) {
                                    debit = lastRentCharged;
                                }
                                totalDebit += debit;

                            }
                            if (obj.getDebitCreditEntry() == DebitCredit.CREDIT) {
                                totalCredit += obj.getAmountInvolved();
                            }
                        }
                        outStandingBal = totalDebit - totalCredit;
                        System.out.println("estate Property: " + ep.getPropertyName() + "total debit: " + totalDebit + " - total Credit: " + totalCredit + " = outstanding Bal: "
                                + outStandingBal + "lastbillAmount" + lastRentCharged + " enddate : " + endDate + "startdate: " + startDate);
                        rentalDetail.setAmountPaid(totalCredit);
                        try {
                            if (outStandingBal < 0) {
                                assessmentSummary = "Paid in Excess";
                            } else {
                                int startYear = startDateList.get(0).getLedgerYear();
                                Calendar calMonth = Calendar.getInstance();
                                calMonth.setTime(startDate);
                                int startmonth = calMonth.get(Calendar.MONTH) + 1;
                                int numberOfMonthsInArrears = (((selectedYear - startYear) * 12) + (getIntMonth(selectedMonth) - startmonth) + 1);
                                assessmentSummary = numberOfMonthsInArrears + " Mth(s) from " + startYear + "/" + startmonth + " to " + selectedYear + "/" + getIntMonth(selectedMonth);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Calculate rent assessment
                        this.outstandingBalance = outStandingBal;
                        this.selectedEstateProperty = ep;
                        this.assessmentYear = selectedYear;
                        this.assessmentMonth = selectedMonth;
                        this.assesssmentType = selectReportType; 
                        rentalDetail.setAssessmentSummary(assessmentSummary);
                        arrears = NumberFormattingUtils.formatDecimalNumberTo_2(totalDebit);
                        rentalDetail.setArrears(arrears);

                        //Add detail to list
                        rentalDetailList.add(rentalDetail);
                    }
                }
                avReportManager.getInstance().addParam("reportTitle", "Return Of Arrears " + selectReportType + " On " + PaymentType.HOUSE_RENT + " for " + selectedMonth + "," + selectedYear);
                avReportManager.getInstance().showReport(rentalDetailList, avReportManager.getInstance().RETURN_OF_ARREARS);
            } else if (selectReportType.equalsIgnoreCase("Quarterly Report")) {
                if (selectedMonth == null) {
                    JsfUtil.addErrorMessage("Please select a quater for the report");
                } else if (selectedMonth.equalsIgnoreCase("1st Quarter")) {
                    String month = "March";
                    for (EstateProperty ep : epList) {
                        totalCredit = 0.00;
                        totalDebit = 0.00;
                        outStandingBal = 0.00;
                        this.outstandingBalance = 0.00;
                        debit = 0.00;
                        RentalDetail rentalDetail = new RentalDetail();
                        //Load the ledger for the estate base on month
                        lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(ep.getEstatePropertyId());
                        endDate = getLastDate(getIntMonth(month), selectedYear).toDate(); 

                        //Set rental detail model properties
                        rentalDetail.setPropertyName(ep.getPropertyName());
                        rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                        rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                        rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                        rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                        rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                        rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                        rentalDetail.setCurrentPropertyRent(lastRentCharged);
 
                        //load legder between 
                        List<EstatePropertyLedger> startDateList = ds.getCustomQry().loadAllLedgerEntriesForProperty(ep, selectedYear);
                        if (startDateList.isEmpty()) {
                            break;
                        } else {
                            startDate = startDateList.get(0).getDateOfRecordTransaction();
                            eplObjList = ds.getCustomQry().getRentalArrearsForEstate(ep, startDate, endDate);
                        }

                        for (EstatePropertyLedger obj : eplObjList) {
                            if (obj.getDebitCreditEntry() == DebitCredit.DEBIT) {
                                debit = obj.getAmountInvolved();
                                if (debit == null) {
                                    debit = lastRentCharged;
                                }
                                totalDebit += debit;

                            }
                            if (obj.getDebitCreditEntry() == DebitCredit.CREDIT) {
                                totalCredit += obj.getAmountInvolved();
                            }
                        }
                        outStandingBal = totalDebit - totalCredit;
                        System.out.println("estate Property: " + ep.getPropertyName() + "total debit: " + totalDebit + " - total Credit: " + totalCredit + " = outstanding Bal: "
                                + outStandingBal + "lastbillAmount" + lastRentCharged + " enddate : " + endDate + "startdate: " + startDate);
                        rentalDetail.setAmountPaid(totalCredit);
                        try {
                            if (outStandingBal < 0) {
                                assessmentSummary = "Paid in Excess";
                            } else {
                                int startYear = startDateList.get(0).getLedgerYear();
                                Calendar calMonth = Calendar.getInstance();
                                calMonth.setTime(startDate);
                                int startmonth = calMonth.get(Calendar.MONTH) + 1;
                                int numberOfMonthsInArrears = (((selectedYear - startYear) * 12) + (getIntMonth(month) - startmonth) + 1);
                                assessmentSummary = numberOfMonthsInArrears + " Mth(s) from " + startYear + "/" + startmonth + " to " + selectedYear + "/" + getIntMonth(month);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Calculate rent assessment
                        rentalDetail.setAmountPaid(totalCredit);
                        this.outstandingBalance = outStandingBal;
                        this.selectedEstateProperty = ep;
                        this.assessmentYear = selectedYear;
                        this.assessmentMonth = month;
                        this.assesssmentType = selectReportType; 
                        rentalDetail.setAssessmentSummary(assessmentSummary);
                        arrears = NumberFormattingUtils.formatDecimalNumberTo_2(totalDebit);
                        rentalDetail.setArrears(arrears);

                        //Add detail to list
                        rentalDetailList.add(rentalDetail);
                    }
                } else if (selectedMonth.equalsIgnoreCase("2nd Quarter")) {
                    String month = "June";
                    for (EstateProperty ep : epList) {
                        totalCredit = 0.00;
                        totalDebit = 0.00;
                        outStandingBal = 0.00;
                        this.outstandingBalance = 0.00;
                        debit = 0.00;
                        RentalDetail rentalDetail = new RentalDetail();
                        //Load the ledger for the estate base on month
                        lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(ep.getEstatePropertyId());
                        endDate = getLastDate(getIntMonth(month), selectedYear).toDate(); 

                        //Set rental detail model properties
                        rentalDetail.setPropertyName(ep.getPropertyName());
                        rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                        rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                        rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                        rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                        rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                        rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                        rentalDetail.setCurrentPropertyRent(lastRentCharged);

                        //If ledger is null get last rent charge and create ledger entry for month
                        //load legder between 
                        List<EstatePropertyLedger> startDateList = ds.getCustomQry().loadAllLedgerEntriesForProperty(ep, selectedYear);
                        if (startDateList.isEmpty()) {
                            break;
                        } else {
                            startDate = startDateList.get(0).getDateOfRecordTransaction();
                            eplObjList = ds.getCustomQry().getRentalArrearsForEstate(ep, startDate, endDate);
                        }

                        for (EstatePropertyLedger obj : eplObjList) {
                            if (obj.getDebitCreditEntry() == DebitCredit.DEBIT) {
                                debit = obj.getAmountInvolved();
                                if (debit == null) {
                                    debit = lastRentCharged;
                                }
                                totalDebit += debit;

                            }
                            if (obj.getDebitCreditEntry() == DebitCredit.CREDIT) {
                                totalCredit += obj.getAmountInvolved();
                            }
                        }
                        outStandingBal = totalDebit - totalCredit;
                        System.out.println("estate Property: " + ep.getPropertyName() + "total debit: " + totalDebit + " - total Credit: " + totalCredit + " = outstanding Bal: "
                                + outStandingBal + "lastbillAmount" + lastRentCharged + " enddate : " + endDate + "startdate: " + startDate);
                        rentalDetail.setAmountPaid(totalCredit);
                        try {
                            if (outStandingBal < 0) {
                                assessmentSummary = "Paid in Excess";
                            } else {
                                int startYear = startDateList.get(0).getLedgerYear();
                                Calendar calMonth = Calendar.getInstance();
                                calMonth.setTime(startDate);
                                int startmonth = calMonth.get(Calendar.MONTH) + 1;
                                int numberOfMonthsInArrears = (((selectedYear - startYear) * 12) + (getIntMonth(month) - startmonth) + 1);
                                assessmentSummary = numberOfMonthsInArrears + " Mth(s) from " + startYear + "/" + startmonth + " to " + selectedYear + "/" + getIntMonth(month);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rentalDetail.setAmountPaid(totalCredit);
                        //Calculate rent assessment
                        this.outstandingBalance = outStandingBal;
                        this.selectedEstateProperty = ep;
                        this.assessmentYear = selectedYear;
                        this.assessmentMonth = month;
                        this.assesssmentType = selectReportType; 
                        rentalDetail.setAssessmentSummary(assessmentSummary);
                        arrears = NumberFormattingUtils.formatDecimalNumberTo_2(totalDebit);
                        rentalDetail.setArrears(arrears);

                        //Add detail to list
                        rentalDetailList.add(rentalDetail);
                    }
                } else if (selectedMonth.equalsIgnoreCase("3rd Quater")) {
                    String month = "September";
                    for (EstateProperty ep : epList) {
                        totalCredit = 0.00;
                        totalDebit = 0.00;
                        outStandingBal = 0.00;
                        this.outstandingBalance = 0.00;
                        debit = 0.00;
                        RentalDetail rentalDetail = new RentalDetail();
                        //Load the ledger for the estate base on month
                        lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(ep.getEstatePropertyId());

                        endDate = getLastDate(getIntMonth(month), selectedYear).toDate();
                        //Set rental detail model properties
                        rentalDetail.setPropertyName(ep.getPropertyName());
                        rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                        rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                        rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                        rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                        rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                        rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                        rentalDetail.setCurrentPropertyRent(lastRentCharged);

                        //load ledger between
                        List<EstatePropertyLedger> startDateList = ds.getCustomQry().loadAllLedgerEntriesForProperty(ep, selectedYear);
                        if (startDateList.isEmpty()) {
                            break;
                        } else {
                            startDate = startDateList.get(0).getDateOfRecordTransaction();
                            eplObjList = ds.getCustomQry().getRentalArrearsForEstate(ep, startDate, endDate);
                        }

                        for (EstatePropertyLedger obj : eplObjList) {
                            if (obj.getDebitCreditEntry() == DebitCredit.DEBIT) {
                                debit = obj.getAmountInvolved();
                                if (debit == null) {
                                    debit = lastRentCharged;
                                }
                                totalDebit += debit;

                            }
                            if (obj.getDebitCreditEntry() == DebitCredit.CREDIT) {
                                totalCredit += obj.getAmountInvolved();
                            }
                        }
                        outStandingBal = totalDebit - totalCredit;
                        System.out.println("estate Property: " + ep.getPropertyName() + "total debit: " + totalDebit + " - total Credit: " + totalCredit + " = outstanding Bal: "
                                + outStandingBal + "lastbillAmount" + lastRentCharged + " enddate : " + endDate + "startdate: " + startDate);
                        rentalDetail.setAmountPaid(totalCredit);
                        try {
                            if (outStandingBal < 0) {
                                assessmentSummary = "Paid in Excess";
                            } else {
                                int startYear = startDateList.get(0).getLedgerYear();
                                Calendar calMonth = Calendar.getInstance();
                                calMonth.setTime(startDate);
                                int startmonth = calMonth.get(Calendar.MONTH) + 1;
                                int numberOfMonthsInArrears = (((selectedYear - startYear) * 12) + (getIntMonth(month) - startmonth) + 1);
                                assessmentSummary = numberOfMonthsInArrears + " Mth(s) from " + startYear + "/" + startmonth + " to " + selectedYear + "/" + getIntMonth(month);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rentalDetail.setAmountPaid(totalCredit);
                        //Calculate rent assessment
                        this.outstandingBalance = outStandingBal;
                        this.selectedEstateProperty = ep;
                        this.assessmentYear = selectedYear;
                        this.assessmentMonth = selectedMonth;
                        this.assesssmentType = selectReportType; 
                        rentalDetail.setAssessmentSummary(assessmentSummary);
                        arrears = NumberFormattingUtils.formatDecimalNumberTo_2(totalDebit);
                        rentalDetail.setArrears(arrears);

                        //Add detail to list
                        rentalDetailList.add(rentalDetail);
                    }
                } else if (selectedMonth.equalsIgnoreCase("4th Quarter")) {
                    String month = "December";
                    for (EstateProperty ep : epList) {
                        RentalDetail rentalDetail = new RentalDetail();
                        //Load the ledger for the estate base on month
                        lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(ep.getEstatePropertyId());
                        endDate = getLastDate(getIntMonth(month), selectedYear).toDate();
                        //Set rental detail model properties
                        rentalDetail.setPropertyName(ep.getPropertyName());
                        rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                        rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                        rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                        rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                        rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                        rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                        rentalDetail.setCurrentPropertyRent(lastRentCharged);

                        //load legder between 
                        List<EstatePropertyLedger> startDateList = ds.getCustomQry().loadAllLedgerEntriesForProperty(ep, selectedYear);
                        if (startDateList.isEmpty()) {
                            break;
                        } else {
                            startDate = startDateList.get(0).getDateOfRecordTransaction();
                            eplObjList = ds.getCustomQry().getRentalArrearsForEstate(ep, startDate, endDate);
                        }

                        for (EstatePropertyLedger obj : eplObjList) {
                            if (obj.getDebitCreditEntry() == DebitCredit.DEBIT) {
                                debit = obj.getAmountInvolved();
                                if (debit == null) {
                                    debit = lastRentCharged;
                                }
                                totalDebit += debit;

                            }
                            if (obj.getDebitCreditEntry() == DebitCredit.CREDIT) {
                                totalCredit += obj.getAmountInvolved();
                            }
                        }
                        outStandingBal = totalDebit - totalCredit;
                        System.out.println("estate Property: " + ep.getPropertyName() + "total debit: " + totalDebit + " - total Credit: " + totalCredit + " = outstanding Bal: "
                                + outStandingBal + "lastbillAmount" + lastRentCharged + " enddate : " + endDate + "startdate: " + startDate);
                        rentalDetail.setAmountPaid(totalCredit);
                        try {
                            if (outStandingBal < 0) {
                                assessmentSummary = "Paid in Excess";
                            } else {
                                int startYear = startDateList.get(0).getLedgerYear();
                                Calendar calMonth = Calendar.getInstance();
                                calMonth.setTime(startDate);
                                int startmonth = calMonth.get(Calendar.MONTH) + 1;
                                int numberOfMonthsInArrears = (((selectedYear - startYear) * 12) + (getIntMonth(month) - startmonth) + 1);
                                assessmentSummary = numberOfMonthsInArrears + " Mth(s) from " + startYear + "/" + startmonth + " to " + selectedYear + "/" + getIntMonth(month);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rentalDetail.setAmountPaid(totalCredit);
                        //Calculate rent assessment
                        this.outstandingBalance = outStandingBal;
                        this.selectedEstateProperty = ep;
                        this.assessmentYear = selectedYear;
                        this.assessmentMonth = selectedMonth;
                        this.assesssmentType = selectReportType; 
                        rentalDetail.setAssessmentSummary(assessmentSummary);
                        rentalDetail.setArrears(arrears);

                        //Add detail to list
                        rentalDetailList.add(rentalDetail);
                    }
                }
                avReportManager.getInstance().addParam("reportTitle", "Return Of Arrears " + selectReportType + " On " + PaymentType.HOUSE_RENT + " for " + selectedMonth + "," + selectedYear);
                avReportManager.getInstance().showReport(rentalDetailList, avReportManager.getInstance().RETURN_OF_ARREARS);
            } else if (selectReportType.equalsIgnoreCase("Annual Report")) {
                String month = "December";
                for (EstateProperty ep : epList) {
                    totalCredit = 0.00;
                    totalDebit = 0.00;
                    outStandingBal = 0.00;
                    this.outstandingBalance = 0.00;
                    debit = 0.00;
                    RentalDetail rentalDetail = new RentalDetail();
                    //Load the ledger for the estate base on month
                    lastRentCharged = ds.getCustomQry().rentedPropertyLastRentBill(ep.getEstatePropertyId());
                    endDate = getLastDate(getIntMonth(month), selectedYear).toDate();
                    //Set rental detail model properties
                    rentalDetail.setPropertyName(ep.getPropertyName());
                    rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                    rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                    rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                    rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                    rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                    rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                    rentalDetail.setCurrentPropertyRent(lastRentCharged);

                    List<EstatePropertyLedger> startDateList = ds.getCustomQry().loadAllLedgerEntriesForProperty(ep, selectedYear);
                    if (startDateList.isEmpty()) {
                        break;
                    } else {
                        startDate = startDateList.get(0).getDateOfRecordTransaction();
                        eplObjList = ds.getCustomQry().getRentalArrearsForEstate(ep, startDate, endDate);
                    }
                    for (EstatePropertyLedger obj : eplObjList) {
                        if (obj.getDebitCreditEntry() == DebitCredit.DEBIT) {
                            debit = obj.getAmountInvolved();
                            if (debit == null) {
                                debit = lastRentCharged;
                            }
                            totalDebit += debit;

                        }
                        if (obj.getDebitCreditEntry() == DebitCredit.CREDIT) {
                            totalCredit += obj.getAmountInvolved();

                        }
                    }
                    outStandingBal = totalDebit - totalCredit;
                    System.out.println("estate Property: " + ep.getPropertyName() + "total debit: " + totalDebit + " - total Credit: " + totalCredit + " = outstanding Bal: "
                            + outStandingBal + "lastbillAmount" + lastRentCharged + " enddate : " + endDate + "startdate: " + startDate);

                    rentalDetail.setAmountPaid(totalCredit);
                    try {
                        if (outStandingBal < 0) {
                            assessmentSummary = "Paid in Excess";
                        } else {
                            int startYear = startDateList.get(0).getLedgerYear();
                            Calendar calMonth = Calendar.getInstance();
                            calMonth.setTime(startDate);
                            int startmonth = calMonth.get(Calendar.MONTH) + 1;
                            int numberOfMonthsInArrears = (((selectedYear - startYear) * 12) + (getIntMonth(month) - startmonth) + 1);
                            assessmentSummary = numberOfMonthsInArrears + " Mth(s) from " + startYear + "/" + startmonth + " to " + selectedYear + "/" + getIntMonth(month);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Calculate rent assessment
                    this.outstandingBalance = outStandingBal;
                    this.selectedEstateProperty = ep;
                    this.assessmentYear = selectedYear;
                    this.assessmentMonth = selectedMonth;
                    this.assesssmentType = selectReportType; 
                    arrears = NumberFormattingUtils.formatDecimalNumberTo_2(totalDebit);
                    rentalDetail.setAssessmentSummary(assessmentSummary);
                    rentalDetail.setArrears(arrears);

                    //Add detail to list
                    rentalDetailList.add(rentalDetail);
                }
                avReportManager.getInstance().addParam("reportTitle", "Return Of Arrears " + selectReportType + " On " + PaymentType.HOUSE_RENT + " for The Year " + selectedYear);
                avReportManager.getInstance().showReport(rentalDetailList, avReportManager.getInstance().RETURN_OF_ARREARS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EstatePropertyLedger createNewEntry(EstateProperty ep, String selectedMonth, Double lastRentCharged) {
        EstatePropertyLedger epl = new EstatePropertyLedger();
        try {
            int month = DateUtils.getIntMonth(selectedMonth);
            Calendar newCal = Calendar.getInstance();
            newCal.set(Calendar.YEAR, selectedYear);
            newCal.set(Calendar.MONTH, month - 1);

            epl = new EstatePropertyLedger();
            epl.setLedgerYear(selectedYear);
            epl.setPaymentType(PaymentType.HOUSE_RENT);
            epl.setDebitCreditEntry(DebitCredit.DEBIT);
            epl.setEstateProperty(ep);
            epl.setDateOfRecordEntry(new Date());
            epl.setDateOfRecordTransaction(newCal.getTime());
            epl.setAmountInvolved(lastRentCharged);
            epl.setPayeeName(AdConstants.SHC);
            epl.setPropertyOccupant(ep.getCurrentPropertyOccupant());
            epl.setRecordedBy(AdConstants.SHC);
            epl.setPaymentFor("Property Rent, " + selectedMonth + " " + selectedYear);
            IDCreator.setEstateLedgerId(epl, DateTimeUtils.getMonthLongName(newCal.getTime()));
            ds.getCommonQry().estatePropertyLedgerCreate(epl);

            return epl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    

     

    public void reportLeaseReport() {
        List<RentalDetail> list = new LinkedList<>();
        Double totalDebit = 0.00;
        Double totalCredit = 0.00;
        Double outStandingBal = 0.00;
        Double amountInvolved = 0.00;
        try {
            if (selectedEstate == null) {
                JsfUtil.addErrorMessage("Please select an estate before viewing ledgers summary");
                return;
            }
            List<EstateProperty> eps = ds.getCustomQry().currentLeaseHoldEstateProperties(selectedEstate.getEstateId());
            CollectionUtils.sortStringValue(eps);
            for (EstateProperty ep : eps) {
                RentalDetail rentalDetail = new RentalDetail();
                this.selectedEstateProperty = ep;
                //outStandingBal = 0.00;
                //estatePropertyList = ds.getCustomQry().loadLedgerEntriesForSelectedYear(estateProperty, selectedYear);
                //objEplList = ds.getCustomQry().getLedgerPaymentForType(ep, PaymentType.GROUND_RENT, selectedYear);
                totalCredit = ds.getCustomQry().generateEstateArrears(ep, selectReportType, selectedMonth, DebitCredit.CREDIT, selectedYear);
                totalDebit = ds.getCustomQry().generateEstateArrears(ep, selectReportType, selectedMonth, DebitCredit.DEBIT, selectedYear);
                Double currentCharge = GroundRentBiller.instance().getCalculated(ep, DateTimeUtils.getCurrentYear());
                rentalDetail.setPropertyName(ep.getPropertyName());
                rentalDetail.setEstateName(ep.getPropertyEstate().getEstateName());
                rentalDetail.setEstateInitials(ep.getPropertyEstate().getEstateId());
                rentalDetail.setBlockName(ep.getEstateBlock().getBlockName());
                rentalDetail.setEstateBlockId(ep.getEstateBlock().getEstateBlockId());
                rentalDetail.setOccupantName(ep.getCurrentOccupantName());
                rentalDetail.setEsatePropertyType(ep.getPropertyUsage().getUsageName());
                rentalDetail.setCurrentPropertyRent(currentCharge);
                if (totalCredit == null) {
                    totalCredit = 0.00;
                }

                BillDemandNoteMaker noteMaker = new BillDemandNoteMaker(ep, selectedYear);
                noteMaker.process();

                rentalDetail.setAssessmentSummary(noteMaker.getAssesmentSummary());
                rentalDetail.setArrears(currentCharge * noteMaker.getPaymentUnit());
                list.add(rentalDetail);
                if (selectReportType.equalsIgnoreCase("Annual Report")) {
                    avReportManager.getInstance().addParam("reportTitle", "Return Of Arrears " + selectReportType + " On " + PaymentType.GROUND_RENT + " for " + selectedYear);
                    avReportManager.getInstance().showReport(list, avReportManager.getInstance().RETURN_OF_ARREARS_LEASE);
                } else {
                    avReportManager.getInstance().addParam("reportTitle", "Return Of Arrears " + selectReportType + " On " + PaymentType.GROUND_RENT + " for " + selectedMonth + "," + selectedYear);
                    avReportManager.getInstance().showReport(list, avReportManager.getInstance().RETURN_OF_ARREARS_LEASE);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void leaseYearAssessment() {
        if (outstandingBalance == 0.00) {
            return;
        }
        double backTrackSum = 0.00;
        paymentUnit = 0;
        while (backTrackSum < outstandingBalance) {
            Double groundRent = ds.getCustomQry().lastDebitLedgerEntryForProperty(selectedEstateProperty.getEstatePropertyId()).getAmountInvolved();
            System.out.println("ground rent " + groundRent);
            paymentUnit++;
            backTrackSum += groundRent;
        }
        if (paymentUnit == 0) {
            assessmentSummary = "Paid in excess";
        } else {
            assessmentSummary = paymentUnit + " Yr(s)";
        }

    }

    public void reportEstateOccupantLedgersSummary() {
        if (selectedEstate == null) {
            JsfUtil.addErrorMessage("Please select an estate before viewing ledgers summary");
            return;
        }
        System.out.println(propertyUsage + "   " + selectedEstate);

        List<PropertyLedgerSummary> ledgerSummarys = new LinkedList<>();
        if (propertyUsage.toString().equalsIgnoreCase("ALL")) {
            propertyUsage = PropertyUsage.ALL;
        } 

        System.out.println("propertyUsage selected " + propertyUsage);

        String estateId = selectedEstate.getEstateId();
 
        estatePropertiesList = ds.getCustomQry().occupiedProperties(estateId, new Date(), PropOccupationType.Leasehold, propertyUsage);
        System.out.println("list of selected estate" + estatePropertiesList);

        CollectionUtils.sortStringValue(estatePropertiesList);

        for (EstateProperty estateProperty : estatePropertiesList) {

//            PaymentType paymentType = estateProperty.currentOccupantProperty().getAppropriatePaymentType();
//            List<Object[]> objectsList = ds.getCustomQry().getLegerSummationForPaymenttype(estateProperty, paymentType,
//                    DateTimeUtils.getCurrentYear());
            BillDemandNoteMaker ledgerInter = new BillDemandNoteMaker(estateProperty, selectedYear);
            ledgerInter.process();  

            PropertyLedgerSummary propertyLedgerSummary = new PropertyLedgerSummary();
            propertyLedgerSummary.setPropertyOccupantName(estateProperty.getCurrentOccupantName());
            propertyLedgerSummary.setAccountBalance(ledgerInter.getDifference());
            propertyLedgerSummary.setPeriodOfBalance(ledgerInter.getPeriodOfBalance());
            propertyLedgerSummary.setPropertyName(estateProperty.getPropertyName());
            ledgerSummarys.add(propertyLedgerSummary);

        }

        avReportManager.getInstance().addParam("reportTitle", selectedEstate.getEstateName() + " Ledger Summary (" + propertyUsage.getUsageName() + " )" + ", " + selectedYear);

        avReportManager.getInstance().showReport(ledgerSummarys, avReportManager.getInstance().CURRENT_Occupant__LEDGER_DETAILS_summary);

    }
    
    public void load_paid_bills_to_jasper(){
    billsUnpaidList = ds.getCustomQry().load_all_paid_bills_to_report();
    }
    
      
    

    public void spreadGroundRentArreas() {
//        List<EstateProperty> propertyList = ds.getCustomQry().currentOccupiedPropertiesOnLeaseHold("");

        if (selectedEstate == null) {
            JsfUtil.addErrorMessage("Please select an estate");
            return;
        }

        List<EstateProperty> propertyList = selectedEstate.getEstatePropertiesList();

        for (EstateProperty estateProperty : propertyList) {

            ArreasRecord arreasRecord = ds.getCommonQry().arreasRecordFind(estateProperty.getEstatePropertyId());

            if (arreasRecord == null) {
                System.out.println("null recored for " + estateProperty);
                continue;
            }

            int yearsOwingFrom = arreasRecord.getYearArreasStartedFrom();

            if (yearsOwingFrom == 0) {
                System.out.println("0 recored for " + estateProperty);
                continue;
            }

            if (!arreasRecord.getVerified()) {
                System.out.println("non verified record for " + estateProperty);
                continue;
            }

            System.out.println("FOUND ......  " + arreasRecord + " for  " + estateProperty);

            GroundRentBiller groundRentBiller = new GroundRentBiller();

            while (yearsOwingFrom < 2010) {

                groundRentBiller.setEstateProperty(estateProperty);
                groundRentBiller.setSelectedYear(yearsOwingFrom);
                groundRentBiller.billPropertyGroud();

                yearsOwingFrom++;

            }

        }

        JsfUtil.addInformationMessage("Spreading of Arreas combleted ");

    }

    public void generateEstateGroundRentDemandNotice() {
        
        try {
             if (selectedEstate == null) {
            JsfUtil.addErrorMessage("Select an Estate to generate demand notice!");
            return;
        }
        if (selectedYear > DateTimeUtils.getCurrentYear()) {
            JsfUtil.addErrorMessage("Demand Notice Cannot be generate for next year");
        }
        estatePropertiesList = selectedEstate.getEstatePropertiesList();
        System.out.println("estate properlist " + estatePropertiesList.toString());
        if (estatePropertiesList == null || estatePropertiesList.isEmpty()) {
            JsfUtil.addErrorMessage("No properties in the selected estate");
        }

        List<BillDemandNote> demandNoteList = new LinkedList<>();
        demandNoteList = BillDemandNoteMaker.reportGroundRentDemandNotice(estatePropertiesList, selectedYear);
        avReportManager.getInstance().addParam("yearOfDemandNotice", selectedYear);
        avReportManager.getInstance().showReport(demandNoteList, avReportManager.getInstance().GROUND_RENT_DEMAND_NOTE);
    
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Gernerate Demand Notice">
    public void reportGroundRentDemandNotice() {
//        if (selectedEstate == null)
//        {
//            JsfUtil.addErrorMessage("Please select an estate before generating ground rent");
//            return;
//        }
//
//         estatePropertiesList = selectedEstate.getEstatePropertiesList();

        EstateBlock selectedEstateBlock = PropertySelector.getMgedInstance().getSelectedEstateBlock();

        if (selectedEstateBlock == null) {
            JsfUtil.addErrorMessage("Please select an estate block before generating ground rent");
            return;
        }

//        estatePropertiesList = selectedEstateBlock.getEstatePropertyList();
        selectedEstateProperty = PropertySelector.getMgedInstance().getSelectedEstateProperty();
//        CollectionUtils.sortStringValue(estatePropertiesList);

        List<BillDemandNote> list = new LinkedList<BillDemandNote>();

//        for (EstateProperty estateProperty : estatePropertiesList) {
        if (selectedEstateProperty.getCurrentPropertyOccupant() == null) {
            JsfUtil.addErrorMessage("Please select property is not occupied at this time");
            return;
        }
        demandNote.setOccupantName(selectedEstateProperty.getCurrentPropertyOccupant().getOccupantName());

        BillDemandNoteMaker inter = new BillDemandNoteMaker(selectedEstateProperty, selectedYear);
        inter.process();
//            inter.
//            inter.

        BillDemandNote demandNotice = inter.demandNotice();
//demandNotice.
        list.add(demandNotice);
        System.out.println("\t\t\t... Processing Completed.\n\n\n");
//        }

        avReportManager.getInstance().addParam("yearOfDemandNotice", selectedYear);
        avReportManager.getInstance().showReport(list, avReportManager.getInstance().GROUND_RENT_DEMAND_NOTE);

    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Estate getSelectedEstate() {
        return selectedEstate;
    }

    public SelectItem[] getCalenderMonthNamesOptions() {
        return calenderMonthNamesOptions;
    }

    public PaymentType getSelectedPaymentType() {
        return selectedPaymentType;
    }

    public void setSelectedPaymentType(PaymentType selectedPaymentType) {
        this.selectedPaymentType = selectedPaymentType;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAssessmentSummary() {
        return assessmentSummary;
    }

    public void setAssessmentSummary(String assessmentSummary) {
        this.assessmentSummary = assessmentSummary;
    }

    public Double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(Double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public PropOccupationType getPropertyLoadingConstraint() {
        return propertyLoadingConstraint;
    }

    public void setPropertyLoadingConstraint(PropOccupationType propertyLoadingConstraint) {
        this.propertyLoadingConstraint = propertyLoadingConstraint;
    }

    public OccupantType getOccupancyType() {
        return occupancyType;
    }

    public void setOccupancyType(OccupantType occupancyType) {
        this.occupancyType = occupancyType;
    }

    public List<BiilsPaidDetail> getBiilsPaidDetailList() {
        return biilsPaidDetailList;
    }

    public void setBiilsPaidDetailList(List<BiilsPaidDetail> biilsPaidDetailList) {
        this.biilsPaidDetailList = biilsPaidDetailList;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getOccupantType() {
        return occupantType;
    }

    public void setOccupantType(String occupantType) {
        this.occupantType = occupantType;
    }

    public List<EstatePropertyLedger> getEstatePropertyList() {
        return estatePropertyList;
    }

    public void setEstatePropertyList(List<EstatePropertyLedger> estatePropertyList) {
        this.estatePropertyList = estatePropertyList;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public void setCalenderMonthNamesOptions(SelectItem[] calenderMonthNamesOptions) {
        this.calenderMonthNamesOptions = calenderMonthNamesOptions;
    }

    public String getSelectReportType() {
        return selectReportType;
    }

    public void setSelectReportType(String selectReportType) {
        this.selectReportType = selectReportType;
    }

    public void setSelectedEstate(Estate selectedEstate) {
        this.selectedEstate = selectedEstate;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public PropertyUsage getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(PropertyUsage propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public List<EstateProperty> getEstatePropertiesList() {
        return estatePropertiesList;
    }

    public void setEstatePropertiesList(List<EstateProperty> estatePropertiesList) {
        this.estatePropertiesList = estatePropertiesList;
    }

    public Date getGroundRentPaymentDeadline() {
        return groundRentPaymentDeadline;
    }

    public void setGroundRentPaymentDeadline(Date groundRentPaymentDeadline) {
        this.groundRentPaymentDeadline = groundRentPaymentDeadline;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public SelectItem[] getQuarterlySelectOptions() {
        return quarterlySelectOptions;
    }

    public void setQuarterlySelectOptions(SelectItem[] quarterlySelectOptions) {
        this.quarterlySelectOptions = quarterlySelectOptions;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public int getAssessmentYear() {
        return assessmentYear;
    }

    public void setAssessmentYear(int assessmentYear) {
        this.assessmentYear = assessmentYear;
    }

    public String getAssessmentMonth() {
        return assessmentMonth;
    }

    public void setAssessmentMonth(String assessmentMonth) {
        this.assessmentMonth = assessmentMonth;
    }

    public String getAssesssmentType() {
        return assesssmentType;
    }

    public void setAssesssmentType(String assesssmentType) {
        this.assesssmentType = assesssmentType;
    }

    public int getPaymentUnit() {
        return paymentUnit;
    }

    public void setPaymentUnit(int paymentUnit) {
        this.paymentUnit = paymentUnit;
    }

    public double getAssessmentLastRent() {
        return assessmentLastRent;
    }

    public void setAssessmentLastRent(double assessmentLastRent) {
        this.assessmentLastRent = assessmentLastRent;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }
    
    
    
    // </editor-fold>

    public List<Bills> getBillsUnpaidList() {
        return billsUnpaidList;
    }

    public void setBillsUnpaidList(List<Bills> billsUnpaidList) {
        this.billsUnpaidList = billsUnpaidList;
    }

    public String getBillTitle() {
        return billTitle;
    }

    public void setBillTitle(String billTitle) {
        this.billTitle = billTitle;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }
    
    
    
    
    
    
}

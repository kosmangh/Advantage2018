/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.ejb.entities.*;
import com.sabonay.advantage.ejb.models.*;
import com.sabonay.advantage.web.reports.avReportManager;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.InputStream;
import java.util.*;

/**
 *
 * @author Edwin
 */
public class LegerUtils {

    // <editor-fold defaultstate="collapsed" desc="Ground Rent">
    public static void prepareGroundRentDemandNotice(List<EstatePropertyLedger> propertyLedgers) {

        double totalCreditEntry = 0.0;
        double totalDebitEntry = 0.0;
        double outstandingBalance = 0.0;
        String estatePropertyId = "";
        int ledgerYear = 0;
        String propertyName;
        String occupantName = "";
        EstateProperty estateProperty = null;

        if (propertyLedgers == null) {
            JsfUtil.addErrorMessage("There are no ledger entries availabe to produce demand notice");
            JsfUtil.getFacesContext().renderResponse();
            return;
        }

        if (propertyLedgers != null) {
            if (!propertyLedgers.isEmpty()) {
                ledgerYear = propertyLedgers.get(0).getLedgerYear();
            }
        }




        InputStream subreportIs = null;
//        System.out.println("pro = "+ propertyLedgers);
//        sm.msg(propertyLedgers.toString());

        List<_DemandNotice> demandNoticesList = new ArrayList<_DemandNotice>();


//        Set<DemandNotice> demandNoticesSet = new LinkedHashSet<DemandNotice>();

        Map<String, _DemandNotice> map = new LinkedHashMap<String, _DemandNotice>();


        System.out.println("total number of ledgers to be processed ....... " + propertyLedgers.size());

        for (EstatePropertyLedger estatePropertyLedger : propertyLedgers) {
            System.out.println("starting to process  " + estatePropertyLedger);
            estateProperty = estatePropertyLedger.getEstateProperty();
            estatePropertyId = estatePropertyLedger.getEstateProperty().getEstatePropertyId();
            _DemandNotice demandNotice = null;

            try {

                demandNotice = map.get(estatePropertyLedger.getPropertyOccupant().getPropertyOccupantId());

                System.out.println(" checkingggg    " + demandNotice == null);

                if (demandNotice == null) {
                    System.out.println("deman notice is null and new ones would be created");

                    occupantName = estatePropertyLedger.getPropertyOccupant().toString();

                    demandNotice = new _DemandNotice();

                    demandNotice.setPropertyOccupantIssuedToId(estatePropertyLedger.getPropertyOccupant().getPropertyOccupantId());
                    demandNotice.setPropertyOccupantIssuedTo(occupantName);
                    demandNotice.setDemandNoticeIntroduction(getLedgerIntro(occupantName));

                    map.put(estatePropertyLedger.getPropertyOccupant().getPropertyOccupantId(),
                            demandNotice);
                }
            } catch (Exception e) {
                System.out.println("could not get demand notice from map and could not create new one");
                e.printStackTrace();
            }

            System.out.println("demand notice is " + demandNotice);

            propertyName = estateProperty.getEstateBlock().getBlockName() + estateProperty.getPropertyNumber();

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> cureent property we are working is ..... " + propertyName);
//            if(propertyName == null)
//                continue;
            if (demandNotice.haveBillForProperty(estateProperty.getEstatePropertyId())) {
                continue;
            }


            String propertyLoc = estatePropertyLedger.
                    getEstateProperty().getEstateBlock().getEstate().getEstateName()
                    + ", Block " + estatePropertyLedger.getEstateProperty().getEstateBlock().getBlockName();

            String usage = estatePropertyLedger.getEstateProperty().getPropertyUsage().getUsageName();

            totalCreditEntry = ds.getCustomQry().sumLedgerEntryPropertyForYear(estatePropertyId, DebitCredit.CREDIT, ledgerYear);
            totalDebitEntry = ds.getCustomQry().sumLedgerEntryPropertyForYear(estatePropertyId, DebitCredit.DEBIT, ledgerYear);

            outstandingBalance = totalCreditEntry - totalDebitEntry;

            PropertyBill propertyBill = new PropertyBill();
            propertyBill.setPropertyNumber(propertyName);
            propertyBill.setPropertyUsage(usage);
            propertyBill.setPropertyLocation(propertyLoc);
            propertyBill.setAmountPayable(outstandingBalance + "");


            demandNotice.getPropertyBillsList().add(propertyBill);


        }


        Set<String> keys = map.keySet();


        for (String lesseId : keys) {
            demandNoticesList.add(map.get(lesseId));
        }

        System.out.println("total number of demand notice = " + demandNoticesList.size());
        System.out.println("details are =========================================== \n");

        for (_DemandNotice demandNotice : demandNoticesList) {
            System.out.println("propertyOccupant: " + demandNotice.getPropertyOccupantIssuedTo() + "  No: " + demandNotice.getPropertyBillsList().size());
        }

        try {
//            subreportIs = JsfUtil.getFacesContext().getExternalContext().getResourceAsStream(avReportManager.PROPPERTY_BILL_4_GR);
//
//            System.out.println("servlet input stream is " + subreportIs);
//
//            System.out.println(new File(".").getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }


//        subreportIs = avReportManager.getInputStreamFor(avReportManager.PROPPERTY_BILL_4_GR);
//        avReportManager.resetParameterToDefault();
//
//
//
//        avReportManager.addParam("groundRentPropertyBill", subreportIs);
//        System.out.println("sub report input steam is " + subreportIs);
//
        avReportManager.getInstance().addParam("nextReviewDate", "2011");
        avReportManager.getInstance().addParam("dateOfIssuance", new Date().toString());
        avReportManager.getInstance().showReport(demandNoticesList,
                avReportManager.GROUND_RENT_DEMAND);
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Ledger Entries">
    public static void //List<EstatePropertyLedgerDetail>
            createLedgerDetails(List<EstatePropertyLedger> propertyLedgersList) {
        List<EstatePropertyLedgerDetail> ledgerDetailsList =
                new LinkedList<EstatePropertyLedgerDetail>();


        String title = "";

        if (propertyLedgersList != null) {
            try {
                title = propertyLedgersList.get(0).getPropertyOccupant().toString();
            } catch (Exception e) {
            }
            for (EstatePropertyLedger estatePropertyLedger : propertyLedgersList) {
                EstatePropertyLedgerDetail detail = new EstatePropertyLedgerDetail();

                detail.setEstatePropertyLedgerId(estatePropertyLedger.getEstatePropertyLedgerId());

                ledgerDetailsList.add(detail);
            }
        }



        try {

            String reportTitle = "Ledger Entries for Estate Property " + title;
            avReportManager.getInstance().addParam("reportTitle", ledgerDetailsList);
            avReportManager.getInstance().showReport(ledgerDetailsList,
                    avReportManager.LEDGER_ENTRIES);

        } catch (Exception e) {
            e.printStackTrace();

        }

//         return ledgerDetailsList;
    }

    // </editor-fold>
    
    public static String getLedgerIntro(String occupantName) {
        String intro = "<html>Please TAKE NOTE that Ground Rent in respect "
                + "of the below-mentioned property, in the name of <u><b>" + occupantName + "</b></u>, for the ending on "
                + "the ....................................... became due on .............................. </html>";

        return intro;
    }

    // <editor-fold defaultstate="collapsed" desc="Run Demand Notice From private List<EstatePropertyLedger> ">
    public static List<BillDemandNote> prepareDemanNoticeFromLedger(List<EstatePropertyLedger> searchedEstatePropertyLedgerList, int selectedYear) {
        List<BillDemandNote> demandNoticesList = new LinkedList<BillDemandNote>();
        Map<String, BillDemandNote> map = new LinkedHashMap<String, BillDemandNote>();


        if (searchedEstatePropertyLedgerList == null) {
            return demandNoticesList;
        }

        for (EstatePropertyLedger propLedger : searchedEstatePropertyLedgerList) {


            System.out.println("processing  " + propLedger);

            if (propLedger.getPropertyOccupant() == null) {
                System.out.println("null for  " + propLedger);
                continue;
            }

            String occupantName = propLedger.getPropertyOccupant().getOccupantName();
            String propertyOccupantId = propLedger.getPropertyOccupant().getPropertyOccupantId();
            String blockName = propLedger.getEstateProperty().getEstateBlock().getBlockName();
            String propertyNumber = propLedger.getEstateProperty().getPropertyNumber();

            DebitCredit debitCreditEntry = propLedger.getDebitCreditEntry();

            double amountInvolved = propLedger.getAmountInvolved();
            int ledgerYear = propLedger.getLedgerYear();


            String propertyID = blockName + propertyNumber;

            String key = propertyOccupantId + "#" + propertyID;

            BillDemandNote demandNote = null;

            try {
                demandNote = map.get(key);

                if (demandNote == null) {

                    String propertyUsageName = propLedger.getEstateProperty().getPropertyUsage().getUsageName();

                    demandNote = new BillDemandNote();
                    demandNote.setPropertyOccupantId(propertyOccupantId);
                    demandNote.setOccupantName(occupantName);
                    demandNote.setEstateProperty(propLedger.getEstateProperty().getPropertyName());
                    demandNote.setPropertyLocation(propLedger.getEstateProperty().getPropertyEstate().getEstateName());
                    demandNote.setPropertyUsage(propertyUsageName);
                    demandNote.setEstatePropertyId(propLedger.getEstateProperty().getEstatePropertyId());
//                    ledgerSheet.setEstateName(estateName);
//                    ledgerSheet.setBlockName(blockName);

                    demandNoticesList.add(demandNote);
                    map.put(key, demandNote);
                }


                if (debitCreditEntry == DebitCredit.CREDIT) {
                    if (selectedYear == ledgerYear) {
                        demandNote.setAmountPaid((amountInvolved + demandNote.getAmountPaid()));
                    } else {
//                        demandNote.addToCreditSum(amountInvolved);
                    }
                } else if (debitCreditEntry == DebitCredit.DEBIT) {
//                    if(selectedYear == ledgerYear)
//                    {
//                        demandNote.setGroundRentBill(amountInvolved);
//                    }
//                     else
//                    {
//                        demandNote.addToDebitSum(-amountInvolved);
//                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (BillDemandNote dn : demandNoticesList) {
            String lessseId = dn.getPropertyOccupantId();
            String stm = "";

            List<String> propsId = new LinkedList<String>();

            for (BillDemandNote notice : demandNoticesList) {
                if (lessseId.equalsIgnoreCase(notice.getPropertyOccupantId())) {
                    propsId.add(notice.getEstatePropertyId());
                }
            }

            for (String estatePropertyId : propsId) {

                EstateProperty estateProperty = ds.getCommonQry().estatePropertyFind(estatePropertyId);

                String arreasStatement = arreasAssessementOfProperty(estateProperty, selectedYear);

                stm += arreasStatement;
            }

            dn.setStatement(stm);
        }

        return demandNoticesList;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Esate Property Ledger Details">
    public static List<PropertyLedgerDetail> prepareEstateLedgerDetails(List<EstatePropertyLedger> propertyLedgersList) {
        List<PropertyLedgerDetail> details = new LinkedList<PropertyLedgerDetail>();
        double debit = 0.0;
        double credit = 0.0;
        Integer firstYr, selectedYr = 0;
        
        
        for (EstatePropertyLedger estatePropertyLedger : propertyLedgersList) { 
            PropertyLedgerDetail detail = new PropertyLedgerDetail();

            if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.DEBIT) {
                debit = estatePropertyLedger.getAmountInvolved();
                detail.setCharge(estatePropertyLedger.getAmountInvolved());
                detail.setAmountInvolved(detail.getCharge());
                detail.setBalance(detail.getAmountInvolved());
                detail.setRecordedBy(estatePropertyLedger.getRecordedBy());
            } else if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.CREDIT) {
                credit = estatePropertyLedger.getAmountInvolved();
                detail.setPayment(estatePropertyLedger.getAmountInvolved());
                detail.setAmountInvolved(detail.getPayment());
                detail.setBalance(-detail.getPayment());
                detail.setRecordedBy(estatePropertyLedger.getRecordedBy());
            }
            
            

            detail.setEntryFor(estatePropertyLedger.getPaymentFor());
            detail.setDateOfTransaction(estatePropertyLedger.getDateOfRecordTransaction());
            detail.setDateOfRecordEntry(estatePropertyLedger.getDateOfRecordEntry());
            detail.setReceiptNumber(estatePropertyLedger.getReceiptNumberIssued());
            detail.setEstateProperty(estatePropertyLedger.getEstateProperty().toString());
            detail.setPropertyUsage(estatePropertyLedger.getEstateProperty().getPropertyUsage().name());
            detail.setOccupantName(estatePropertyLedger.getPropertyOccupant().toString());
            detail.setEstatePropertyId(estatePropertyLedger.getEstateProperty().getEstatePropertyId());
            detail.setLedgerYear(estatePropertyLedger.getLedgerYear());
            detail.setDebitCredit(estatePropertyLedger.getDebitCreditEntry().getInitials());
            


            details.add(detail);
        }


        return details;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Arreas Assessement">
    public static String arreasAssessementOfProperty(EstateProperty estateProperty, int baseYear) {
        if (estateProperty == null) {
            return "";
        }

        List<EstatePropertyLedger> ledgers =
                ds.getCustomQry().loadLedgerEntriesForProperty(
                estateProperty.getPropertyEstate().getEstateId(),
                estateProperty.getEstateBlock().getBlockName(),
                estateProperty.getPropertyNumber(), baseYear);

        double totalDebit = 0.0;
        double totalCredit = 0.0;
        double balance = 0.0;
        double backTrackSum = 0.0;
        int backTrackYear = baseYear;


        String assement = "";

        for (EstatePropertyLedger l : ledgers) {

            if (l.getDebitCreditEntry() == DebitCredit.DEBIT) {
                totalDebit += l.getAmountInvolved();
            } else if (l.getDebitCreditEntry() == DebitCredit.CREDIT) {
                totalCredit += l.getAmountInvolved();
            }
        }

        balance = totalDebit - totalCredit;

        System.out.println("total credit " + totalCredit);
        System.out.println("total debit " + totalDebit);
        System.out.println("balance " + balance);

        while (backTrackSum < balance) {
            double amountCharged = ds.getCustomQry().getGroundRentBasicCharge(estateProperty, backTrackYear);

            backTrackSum += amountCharged;

            assement = "    " + backTrackYear + "  -- " + amountCharged + "\n " + assement;

            backTrackYear = backTrackYear - 1;

        }

//        double currentYearGroundRentBill = EJBGateWay.getCustomQry().groundRentChargeForPropertyInYear(estateProperty, baseYear);
//        assement += "\n    "+baseYear + "  -- " + currentYearGroundRentBill + "\n ";

//        assement = "Assesment ("+(estateProperty.getPropertyName())+") - " + baseYear + " - " + backTrackYear + "\n" + assement;

        assement = "Assesment (" + (estateProperty.getPropertyName()) + ") \n" + assement;

        return assement;

    }
    // </editor-fold>
}

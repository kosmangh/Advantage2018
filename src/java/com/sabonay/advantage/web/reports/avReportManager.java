/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.reports;

import com.sabonay.advantage.ejb.sessionbean.UserData;
import com.sabonay.advantage.web.pagecontrollers.ImageUploadController;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.modules.imageutils.ImageResource;
import com.sabonay.modules.jasperreporting.JasperReportManager;
import com.sabonay.modules.jasperreporting.ReportDesignFileType;
import com.sabonay.modules.jasperreporting.ReportOutputEnvironment;
import com.sabonay.modules.jasperreporting.ReportOutputFileType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Edwin
 */
public class avReportManager extends JasperReportManager implements Serializable {

    private static final String REPORT_BASE_DIR = "/com/sabonay/advantage/web/reports/";
    public static final String GROUND_RENT_DEMAND = REPORT_BASE_DIR + "ground_rent_demand_note.jasper";
    public static final String PROPPERTY_BILL_4_GR = REPORT_BASE_DIR + "property_bill_for_ground_rent.jasper";
    public static final String GROUND_RENT_CHARGES = REPORT_BASE_DIR + "ground_rent_charges.jasper";
    public static final String LEDGER_ENTRIES = REPORT_BASE_DIR + "property_ledger_entries.jasper";
    public static final String ESTATE_PROPERTIES_DISTRIBUTION = REPORT_BASE_DIR + "estates_properties_distribution.jasper";
    public static final String ESTATE_LEDGER_SHEET = REPORT_BASE_DIR + "estates_ledger_sheet.jasper";
    
      public static final String BILLS_PAID_LEDGER_SHEET = REPORT_BASE_DIR + "billd_paid_ledger_sheet.jasper";
    public static final String GROUND_RENT_DEMAND_NOTE = REPORT_BASE_DIR + "demand_notice.jasper";
    public static final String GRND_RENT_DEMO = REPORT_BASE_DIR + "demand_demo.jasper";
    public static final String PROP_BILL = REPORT_BASE_DIR + "property_bill_details.jasper";
    public static final String PROP_LEDGER = REPORT_BASE_DIR + "property_ledger_details.jasper";
    public static final String PROPERTY_OCCUPANT = REPORT_BASE_DIR + "estate_property_occupant.jasper";
    public static final String PROPTERTY_OCCUPANT_INFORMATION = REPORT_BASE_DIR + "property_occupant_information.jasper";
    public static final String rental_properties_balance_sheet = REPORT_BASE_DIR + "rental_properties_balance_sheet.jasper";
    public static final String CURRENT_Occupant__LEDGER_DETAILS_summary = REPORT_BASE_DIR + "estate_occupant_ledger_summary.jasper";
    
    public static final String ALL_PAID_BILLS = REPORT_BASE_DIR + "all_paid_bills.jasper";
    
    
    public static final String BILLS_PAID_REPORT = REPORT_BASE_DIR + "bill_payments.jasper";
    public static final String ACCOUNT_STATEMENT = REPORT_BASE_DIR + "statement_of_account.jasper";
    public static final String GRNDRENT_PROPERTIES_BAL_SHEET = REPORT_BASE_DIR + "leasehold_properties_balance_sheet.jasper";
    public static final String DETAILED_ESTATE_PTY_SHEET = REPORT_BASE_DIR + "estates_property_details.jasper";
    public static final String RETURN_OF_ARREARS = REPORT_BASE_DIR + "return_of_arrears.jasper";
    public static final String RETURN_OF_ARREARS_LEASE = REPORT_BASE_DIR + "return_of_arrears_lease.jasper";
    private static avReportManager reportManager = new avReportManager();
    //private static UserData userdata  = new UserData();

    private avReportManager() {
    }

    static {

        reportManager.addToDefaultParameters("companyName", "State Housing Company Limited");
        reportManager.addToDefaultParameters("zonalOffice", "Kumasi Zone");
        reportManager.addToDefaultParameters("zonalAddress", "P.O. Box 987, KUMASI");
        reportManager.addToDefaultParameters("dateOfReport", DateTimeUtils.formatDateFully(new Date()));
        reportManager.addToDefaultParameters("companyLogo", ImageResource.getResourceImage("/com/sabonay/advantage/common/images/shc_logo.png"));
        reportManager.addToDefaultParameters("directorSignature", ImageUploadController.getInstance().getDirectorSignature());

        reportManager.setReportEnvironment(ReportOutputEnvironment.WEB_APPLICATION);
        reportManager.setReportFileType(ReportDesignFileType.INPUTSTREAM);
        reportManager.setReportOutput(ReportOutputFileType.PDF);
    }

    public static avReportManager getInstance() {
        return reportManager;
    }

//    public static void showReport(Object reportData, String jasperFile)
//    {
//        List<Object> list = new ArrayList<Object>();
//        list.add(reportData);
//        showReport(list, jasperFile);
//    }
//
//
//    public static void showReport(Collection reportData,String jasperFile)
//    {
//        if(AdvantageUserData.getManagedInstance().getReportOutput() == ReportOutputFileType.PDF)
//            reportManager.setReportOutput(ReportOutputFileType.PDF);
//        else if(AdvantageUserData.getManagedInstance().getReportOutput() == ReportOutputFileType.XHTML)
//            reportManager.setReportOutput(ReportOutputFileType.XHTML);
//
//        reportManager.showReport(reportData, jasperFile);
////        showReport(reportData, rptParam, jasperFile, ReportOutputOld.PDF);
//    }
//
//    public static void addParam(String paramKey, Object paramValue)
//    {
//        reportManager.addParam(paramKey, paramValue);
//    }
//
//    public static void resetParameterToDefault()
//    {        
//        reportManager.resetReportParametersToDefault();
//    }
    public static class getInstance {

        public getInstance() {
        }
    }
}

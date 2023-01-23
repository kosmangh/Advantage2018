/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.utils;

/**
 * This class or object holds the url of the various menu tabs/tabs options in Advantage system
 * @author Edwin / Ritchid
 */
public class AppPages {

    private static final String PAGE_BASE_DIR = "pages/";
    private static final String HELP_DIR = "/";
    
    //<editor-fold defaultstate="collapsed" desc="Estate Setup Pages">
    public static final String ESTATE = PAGE_BASE_DIR + "estate.xhtml";
    public static final String ESTATE_BLOCK = PAGE_BASE_DIR + "estate_block.xhtml";
    public static final String ESTAETW_PROPERTY = PAGE_BASE_DIR + "estate_property.xhtml";
    public static final String BLOCK_ESTATE_PPTS = PAGE_BASE_DIR + "block_estate_ppts.xhtml";
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Bill Payment Pages">
    public static final String BILL_PAYMENT = PAGE_BASE_DIR + "part_payment_occupants.xhtml";
    public static final String BILL_PAYMENT_REVERSAL = PAGE_BASE_DIR + "payment_reversal.xhtml";
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Ledger Pages">
    public static final String LED = PAGE_BASE_DIR + "estate_property_ledger.xhtml";
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Occupancy Pages">
    public static final String LESSEE = PAGE_BASE_DIR + "property_occupant.xhtml";
    public static final String OCCUPANE_PROPERTY = PAGE_BASE_DIR + "occupant_property.xhtml";
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Billing Setup Pages">
    public static final String GROUND_RENT_SETUP = PAGE_BASE_DIR + "groundrentsetup.xhtml";
    public static final String RENTAL_BILLING = PAGE_BASE_DIR + "rental_billing.xhtml";
    public static final String ARREAS = PAGE_BASE_DIR + "ground_rent_arreas_charges.xhtml";
    public static final String PROPERTY_CHARGES = PAGE_BASE_DIR + "property_charges.xhtml";
        
    //</editor-fold>

    public static final String MAIN_DATA = PAGE_BASE_DIR + "maindata.xhtml";

    public static final String TEMP_LEDGER = PAGE_BASE_DIR + "assetledger.xhtml";









    public static final String REPORT = PAGE_BASE_DIR + "general_report.xhtml";
    public static final String BILL = PAGE_BASE_DIR + "bill_status.xhtml";
    public static final String RECORED_CROSS_CHECK = PAGE_BASE_DIR + "records_crosscheck.xhtml";

    public static final String SETTINGS = PAGE_BASE_DIR + "settings.xhtml";

    public static final String STAFF = PAGE_BASE_DIR + "staff.xhtml";

//    public static final String USER_ACCOUNT = PAGE_BASE_DIR + "user_account_launcher.xhtml";
    public static final String USER_ACCOUNT = PAGE_BASE_DIR + "user_account.xhtml";


    public static final String DATA_CORRECTION = "correction.xhtml";


    public static final String PPT_REASSIGNMENT = PAGE_BASE_DIR + "property_reassignment.xhtml";

    public static final String LESSEE_ACCOUNT_STATEMENT = PAGE_BASE_DIR + "acc_statement.xhtml";

    public static final String SIGNATURE_UPLOAD = PAGE_BASE_DIR + "dir_signature.xhtml";

    public static final String SMS_BLAST = PAGE_BASE_DIR + "sms_reminder.xhtml";

    public static final String SMS_TOPUP = PAGE_BASE_DIR + "credit_topup.xhtml";

    public static final String SMS_BILL = PAGE_BASE_DIR + "sms_bills.xhtml";

    public static final String SMS_BILL_CHECK = PAGE_BASE_DIR + "check_bill_sms.xhtml";
    
    public static final String SMS_BLAST_CHECK = PAGE_BASE_DIR + "check_reminder_sms.xhtml";
    
    public static final String HELP = "user_manual.xhtml";
    public static final String CORRECT_ESTATE = PAGE_BASE_DIR + "correct_estate_property.xhtml";
    public static final String GENERATE_DEMANDNOTICE = PAGE_BASE_DIR + "grndrent_demand_notice.xhtml";

}

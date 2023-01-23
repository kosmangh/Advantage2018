/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.utils;

/**
 *
 * @author Edwin
 */
public abstract interface PagesCode {

    //<editor-fold defaultstate="collapsed" desc="Estate setup tab">
    public static final String ESTATE = "estate";
    public static final String ESTATE_BLOCK = "block";
    public static final String ESTATE_PRPERTY = "property";
    public static final String BLK_ESTATE_PPT = "blk_estate_ppt";
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Bill Payments">
    public static final String BILL_PAYMENT = "billPayment";
    public static final String PAYMENT_REVERSAL = "payment_correction";
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Ledgers">
    public static final String ESTATE_PROPERTY_LEDGER = "propLedger";
    public static final String GENEREATE_DEMAND_NOTICE = "generate_demand_notice";
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Billing Setup">
    public static final String CHARGES = "charges";
    public static final String GROUND_RENT = "groundRent";
    public static final String RENTAL_BILLING = "rentBilling";
    public static final String ARREARS = "grndRentArreas";
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="General Reports">
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="User Accounts">
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="SMS Blast">
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Help">
    
    //</editor-fold>
    
    
    public static final String ARREAS_RECORD = "arreasRecord";


    //--------------------------------
    //Goes under the setup user actions

    //---------------------------------



    public static final String OCCUPANT_PROPERTY = "occupProp";

    public static final String PAYMENT_TYPE = "paymentType";

    public static final String PROPERTY_DESCRIPTION = "propDesc";

    public static final String PROPERTY_OCCUPANT = "occupant";


    public static final String SETTING = "settings";

    public static final String STAFF = "staff";

    public static final String USER_ACCOUNT = "uac";

    public static final String GENERAL_REPORT = "genRpt";
     public static final String BILL_REPORT = "billRpt";
    public static final String CORRECTION_PAGE = "correction";


    public static final String PPT_ASSIGNMENT = "reAssign";

    public static final String ACC_STATEMENT = "acc_statement";
    public static final String  SIG_IMAGE = "signature_upload";
    public static final String SMS_BLAST = "sms_reminder";
    public static final String SMS_TOPUP = "topupLog";
    public static final String SMS_BILLS = "sms_bills";
    public static final String HELP = "help";
    
    public static final String CORRECT_ESTATE_PPT = "estate_correction";
    
}

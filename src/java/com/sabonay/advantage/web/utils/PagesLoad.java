package com.sabonay.advantage.web.utils;

import java.util.ArrayList;

/**
 *
 * @author Edwin / Ritchid
 */
public class PagesLoad {

    private static ArrayList<MenuItems> menuItemsList = null;
    private static ArrayList<MenuItemActions> adminMenuItemsAction = null;
    private static ArrayList<MenuItemActions> directorMenuItemsAction = null;
    private static ArrayList<MenuItemActions> estateManagerMenuItemsAction = null;
    private static ArrayList<MenuItemActions> financeMenuItemsAction = null;
    private static ArrayList<MenuItemActions> auditorMenuItemsAction = null;

    @SuppressWarnings("FinalStaticMethod")
    public static final ArrayList<MenuItems> setUpMenuItems() {
        if (menuItemsList != null) {
            return menuItemsList;
        }

        menuItemsList = new ArrayList<>();  

        //<editor-fold defaultstate="collapsed" desc="Estate Menu">
        menuItemsList.add(new MenuItems(MenuItem.ESTATE_ITEM, PagesCode.ESTATE, "Estates", AppPages.ESTATE,
              "Estates", GroupNames.ESTATE_SETUP, 0));
       //  menuItemsList.add(new MenuItems(MenuItem.BILL_ITEM, PagesCode.BILL_REPORT, "Bill Report", AppPages.BILL,
        //      "Bill Report", GroupNames.ESTATE_SETUP, 0));
        menuItemsList.add(new MenuItems(MenuItem.ESTATE_BLOCK_ITEM, PagesCode.ESTATE_BLOCK, "Estate Block", AppPages.ESTATE_BLOCK,
                "Estate Block", GroupNames.ESTATE_SETUP, 0));
        menuItemsList.add(new MenuItems(MenuItem.ESTATE_PROPERTY_ITEM, PagesCode.ESTATE_PRPERTY, "Estate Property", AppPages.ESTAETW_PROPERTY,
                "Estate Property Number", GroupNames.ESTATE_SETUP, 0));
        menuItemsList.add(new MenuItems(MenuItem.BLOCK_ESTATEPPT_ITEM, PagesCode.BLK_ESTATE_PPT, "Inactive Property", AppPages.BLOCK_ESTATE_PPTS, 
                "Block Estate Properties", GroupNames.ESTATE_SETUP, 0));
        menuItemsList.add(new MenuItems(MenuItem.CORRECT_ESTATEPPT_ITEM, PagesCode.CORRECT_ESTATE_PPT, "Corrections", 
                AppPages.CORRECT_ESTATE, "Estate Property Correction", GroupNames.ESTATE_SETUP, 0));
        
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Bill Payment Menu">
        menuItemsList.add(new MenuItems(MenuItem.BILL_PAYMENT_ITEM, PagesCode.BILL_PAYMENT, "Bill Payment", AppPages.BILL_PAYMENT,
                "Bill Payment", GroupNames.BILL_PAYMENT, 1));
        menuItemsList.add(new MenuItems(MenuItem.PAYMENT_REVERSAL_ITEM, PagesCode.PAYMENT_REVERSAL, "Payment Reversal", AppPages.BILL_PAYMENT_REVERSAL,
                "Payment Reversal", GroupNames.BILL_PAYMENT, 1));
        
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Estate Property Legder Menu">
        menuItemsList.add(new MenuItems(MenuItem.ESTATE_PROPERTY_LEDGER_ITEM, PagesCode.ESTATE_PROPERTY_LEDGER, "Property Ledger", AppPages.LED,
                "Property Ledger", GroupNames.LEDGERS, 2));
        menuItemsList.add(new MenuItems(MenuItem.GENERATE_DEMANDNOTICE_ITEM, PagesCode.GENEREATE_DEMAND_NOTICE, "Demand Notice", AppPages.GENERATE_DEMANDNOTICE, 
                "Demand Notice", GroupNames.LEDGERS, 2));

        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Lessee Menu">
        menuItemsList.add(new MenuItems(MenuItem.PROPERTY_OCCUPANT_ITEM, PagesCode.PROPERTY_OCCUPANT, "Lessee", AppPages.LESSEE,
                "Lessee", GroupNames.LESSEE, 3));
        menuItemsList.add(new MenuItems(MenuItem.OCCUPANT_PROPERTY_ITEM, PagesCode.OCCUPANT_PROPERTY, "Assign ppty Lessee", AppPages.OCCUPANE_PROPERTY,
                "Assign  ppty Lessee", GroupNames.LESSEE, 3));
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="GroundRent Menu">
//        menuItemsList.add(new MenuItems(MenuItem.CHARGES_ITEM, PagesCode.CHARGES, "Charges", AppPages.PROPERTY_CHARGES, "Charges aplicable on various properties", GroupNames.ESTATE_BILLING, 4));
        menuItemsList.add(new MenuItems(MenuItem.GRND_RENT_SETUP_ITEM, PagesCode.GROUND_RENT, "Ground Rent", AppPages.GROUND_RENT_SETUP, "Ground Rent", GroupNames.ESTATE_BILLING, 4));
        menuItemsList.add(new MenuItems(MenuItem.RENTAL_BILLING_ITEM, PagesCode.RENTAL_BILLING, "Rental Billing", AppPages.RENTAL_BILLING, "Rental Billing", GroupNames.ESTATE_BILLING, 4));
        menuItemsList.add(new MenuItems(MenuItem.GRND_RENT_ARREARS_ITEM, PagesCode.ARREARS, "Ground Rent Arrears", AppPages.ARREAS, "Ground Rent Arrears", GroupNames.ESTATE_BILLING, 4));
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="General Reports Menu">
        menuItemsList.add(new MenuItems(MenuItem.GENERAL_REPORT_ITEM, PagesCode.GENERAL_REPORT, "Reports", AppPages.REPORT,
                "Reports", GroupNames.REPORTS, 5));
         menuItemsList.add(new MenuItems(MenuItem.BILL_STATUS, PagesCode.BILL_REPORT, "Bills Report", AppPages.BILL,
                "Bills Report", GroupNames.REPORTS, 5));
        
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="UserAccounts Menu">
        menuItemsList.add(new MenuItems(MenuItem.STAFF_ITEM, PagesCode.STAFF, "Staff", AppPages.STAFF,
                "Staff", GroupNames.USER_ACCOUNTS, 6));

        menuItemsList.add(new MenuItems(MenuItem.USER_ACCOUNT_ITEM, PagesCode.USER_ACCOUNT, "User Accounts", AppPages.USER_ACCOUNT,
                "User Accounts", GroupNames.USER_ACCOUNTS, 7));
       // menuItemsList.add(new MenuItems(MenuItem.UPLOAD_SIGNATURE_ITEM, PagesCode.SIG_IMAGE, "Signature Upload", AppPages.SIGNATURE_UPLOAD, 
               // "Director Signature", GroupNames.USER_ACCOUNTS, 8));

        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="SMS Menu">
        menuItemsList.add(new MenuItems(MenuItem.SMS_ITEM, PagesCode.SMS_BLAST, "SMS Blast", AppPages.SMS_BLAST, "SMS Blast", GroupNames.SMS_BLAST, 8));
        menuItemsList.add(new MenuItems(MenuItem.SMS_BILL_ITEM, PagesCode.SMS_BILLS, "SMS Bills Blast", AppPages.SMS_BILL, "SMS Bills Blast", GroupNames.SMS_BLAST, 9));
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Settings Menu">
        menuItemsList.add(new MenuItems(MenuItem.SMS_TOPUP_ITEM, PagesCode.SMS_TOPUP, "SMS Top Up", AppPages.SMS_TOPUP, 
                "SMS Top Up", GroupNames.SMS_TOPUP, 10));
//</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="User Manual Menu">
        menuItemsList.add(new MenuItems(MenuItem.HELP, PagesCode.HELP, "Help", AppPages.HELP, 
                "Help", GroupNames.HELP, 11));
//</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Bills Report Menu">
        menuItemsList.add(new MenuItems(MenuItem.BILL_STATUS, PagesCode.BILL_REPORT, "Bill 1", AppPages.BILL, "Bill 1", GroupNames.BILLS_REPORTS, 12));
        menuItemsList.add(new MenuItems(MenuItem.BILL_STATUS, PagesCode.BILL_REPORT, "Bill 2", AppPages.BILL, "Bill 2", GroupNames.BILLS_REPORTS, 13));
        //</editor-fold>
        
        return menuItemsList;
    }

    //<editor-fold defaultstate="collapsed" desc="Menu List And Features">
    
    //<editor-fold defaultstate="collapsed" desc="For Administrator">
    public static String[] adminFeatures = {};

    public static ArrayList<MenuItemActions> initAdminPagesActions() {
        if (adminMenuItemsAction != null) {
            return adminMenuItemsAction;
        }
        adminMenuItemsAction = new ArrayList<>();
        //add menu items to be seen by this role
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.BILL_ITEM, "bill_status_info", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_ITEM, "estate_info_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_BLOCK_ITEM, "estate_blk_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_PROPERTY_ITEM, "estate_pty_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.BLOCK_ESTATEPPT_ITEM, "blk_estate_ppt", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.CORRECT_ESTATEPPT_ITEM, "estate_ppt_correction", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.OCCUPANT_PROPERTY_ITEM, "occupant_pty_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.PROPERTY_OCCUPANT_ITEM, "pty_occupant_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.GRND_RENT_SETUP_ITEM, "grnd_rent_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.RENTAL_BILLING_ITEM, "rental_bill_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.GRND_RENT_ARREARS_ITEM, "grndrent_arrears", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_PROPERTY_LEDGER_ITEM, "estate_ptyledger_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.BILL_PAYMENT_ITEM, "bill_pay_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.PAYMENT_REVERSAL_ITEM, "bill_pay_correction", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.GENERAL_REPORT_ITEM, "gen_report_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.STAFF_ITEM, "staff_info_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.USER_ACCOUNT_ITEM, "user_accounts_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.UPLOAD_SIGNATURE_ITEM, "signature_upload", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.SMS_TOPUP_ITEM, "credit_topup_edit", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.HELP, "help", true));
        adminMenuItemsAction.add(new MenuItemActions(MenuItem.GENERATE_DEMANDNOTICE_ITEM, "demand_notice", true));

        return adminMenuItemsAction;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="For Director">
    public static String[] directorFeatures = {};

    public static ArrayList<MenuItemActions> initDirectorPagesActions() {
        if (directorMenuItemsAction != null) {
            return directorMenuItemsAction;
        }
        directorMenuItemsAction = new ArrayList<>();
        //add menu items to be seen by this role
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_ITEM, "estate_info_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.BILL_ITEM, "bill_status_info", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_BLOCK_ITEM, "estate_blk_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_PROPERTY_ITEM, "estate_pty_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.BLOCK_ESTATEPPT_ITEM, "blk_estate_ppt", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.CORRECT_ESTATEPPT_ITEM, "estate_ppt_correction", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.OCCUPANT_PROPERTY_ITEM, "occupant_pty_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.PROPERTY_OCCUPANT_ITEM, "pty_occupant_edit", true));
//        directorMenuItemsAction.add(new MenuItemActions(MenuItem.CHARGES_ITEM, "Charges_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.GRND_RENT_SETUP_ITEM, "grnd_rent_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.RENTAL_BILLING_ITEM, "rental_bill_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_PROPERTY_LEDGER_ITEM, "estate_ptyledger_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.BILL_PAYMENT_ITEM, "bill_pay_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.PAYMENT_REVERSAL_ITEM, "bill_pay_correction", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.GRND_RENT_ARREARS_ITEM, "grndrent_arrears", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.GENERAL_REPORT_ITEM, "gen_report_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.BILL_STATUS, "bill_report_edit", true));
        
        
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.STAFF_ITEM, "staff_info_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.USER_ACCOUNT_ITEM, "user_accounts_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.SMS_ITEM, "sms_reminder_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.SMS_BILL_ITEM, "sms_bills_edit", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.HELP, "help", true));
        directorMenuItemsAction.add(new MenuItemActions(MenuItem.GENERATE_DEMANDNOTICE_ITEM, "demand_notice", true));
        return directorMenuItemsAction;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="For Estate Manager">
    
    public static String[] estateMgrFeatures = {};

    public static ArrayList<MenuItemActions> initEstateManagerPagesActions() {
        if (estateManagerMenuItemsAction != null) {
            return estateManagerMenuItemsAction;
        }
        estateManagerMenuItemsAction = new ArrayList<>();
        //add menu items to be seen by this role
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_ITEM, "estate_info_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_BLOCK_ITEM, "estate_blk_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_PROPERTY_ITEM, "estate_pty_edit", true));
        //estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.BLOCK_ESTATEPPT_ITEM, "blk_estate_ppt", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.CORRECT_ESTATEPPT_ITEM, "estate_ppt_correction", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.BILL_PAYMENT_ITEM, "bill_pay_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.OCCUPANT_PROPERTY_ITEM, "occupant_pty_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.PROPERTY_OCCUPANT_ITEM, "pty_occupant_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.GRND_RENT_SETUP_ITEM, "grnd_rent_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.RENTAL_BILLING_ITEM, "rental_bill_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.GRND_RENT_ARREARS_ITEM, "grndrent_arrears", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_PROPERTY_LEDGER_ITEM, "estate_ptyledger_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.CHARGES_ITEM, "Charges_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.GENERAL_REPORT_ITEM, "gen_report_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.SMS_ITEM, "sms_reminder_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.SMS_BILL_ITEM, "sms_bills_edit", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.HELP, "help", true));
        estateManagerMenuItemsAction.add(new MenuItemActions(MenuItem.GENERATE_DEMANDNOTICE_ITEM, "demand_notice", true));
        return estateManagerMenuItemsAction;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="For Finanace">
    public static String[] financeFeatures = {};

    public static ArrayList<MenuItemActions> initFinancePagesActions() {
        if (financeMenuItemsAction != null) {
            return financeMenuItemsAction;
        }
        financeMenuItemsAction = new ArrayList<>();
        //add menu items to be seen by this role
        financeMenuItemsAction.add(new MenuItemActions(MenuItem.BILL_PAYMENT_ITEM, "bill_pay_edit", true));
        financeMenuItemsAction.add(new MenuItemActions(MenuItem.PAYMENT_REVERSAL_ITEM, "bill_pay_correction", true));
        financeMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_PROPERTY_LEDGER_ITEM, "estate_ptyledger_edit", true));
        financeMenuItemsAction.add(new MenuItemActions(MenuItem.HELP, "help", true));
//        financeMenuItemsAction.add(new MenuItemActions(MenuItem.GRND_RENT_ARREARS_ITEM, "grndrent_arrears", true));
        return financeMenuItemsAction;
    }
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="For Auditor">
    public static String[] auditorFeatures = {};

    public static ArrayList<MenuItemActions> initAuditorPagesActions() {
        if (auditorMenuItemsAction != null) {
            return auditorMenuItemsAction;
        }
        auditorMenuItemsAction = new ArrayList<>();
        //add menu items to be seen by this role
        auditorMenuItemsAction.add(new MenuItemActions(MenuItem.ESTATE_PROPERTY_LEDGER_ITEM, "estate_ptyledger_edit", true));
        auditorMenuItemsAction.add(new MenuItemActions(MenuItem.GENERAL_REPORT_ITEM, "gen_report_edit", true));
        auditorMenuItemsAction.add(new MenuItemActions(MenuItem.HELP, "help", true));
        return auditorMenuItemsAction;
    }
    
    //</editor-fold>
    //</editor-fold>
}

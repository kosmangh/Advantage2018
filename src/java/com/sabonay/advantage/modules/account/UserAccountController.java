package com.sabonay.advantage.modules.account;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sabonay.advantage.ejb.entities.Staff;
import com.sabonay.advantage.ejb.entities.UserAccount;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.UserRole;
import com.sabonay.common.security.SecurityHash;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@Named(value = "userAccountController")
@RequestScoped
public class UserAccountController implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
        private String totalAccounts;
        private String searchText;
        private String searchOptions;
        private String staffId;
        private String rePswrd;
    
        private static int tabIndex;
        
        private UserAccount upUserAccount;
        private UserAccount userAccount;
        
        private List<UserAccount> userAccountList;
        
        private DataModel<UserAccount> userAccountTableModel;
        
        @Inject
        AdvantageUserData currentUser;
    //</editor-fold>
    
        private UserRole selectAccessRight;
        
    //<editor-fold defaultstate="collapsed" desc="Initiators">
        public UserAccountController() {
            totalAccounts = "0";
            userAccount = new UserAccount();
            upUserAccount = new UserAccount();
            selectAccessRight = new UserRole();
            staffId = null;
            tabIndex = 0;
        }

        @PostConstruct
        private void init(){
            getAllAccounts();
        }

    //</editor-fold>
        
        
    //<editor-fold defaultstate="collapsed" desc="System Logics">
        public void toNewAccount(){
            userAccount = new UserAccount();
            staffId = null;
            rePswrd = null;
            tabIndex = 1;
        }
        
        public void refreshList(){
            searchOptions = null;
            searchText = "";
            toUserAccountList();
            getAllAccounts();
        }
        
        public void toUserAccountList(){
            getAllAccounts();
            tabIndex = 0;
        }
        
        public void toUpdateAccount(){
            upUserAccount = new UserAccount();
            tabIndex = 2;
        }
        
        public void selectAccount(){
            toUpdateAccount();
            upUserAccount = (UserAccount) userAccountTableModel.getRowData();
        }
    //</editor-fold>
        
        
    //<editor-fold defaultstate="collapsed" desc="Business Logics">
        public void getAllAccounts(){
            try {
                userAccountList = ds.getCommonQry().userAccountGetAll(false);
                userAccountTableModel = new ListDataModel<>(userAccountList);
                totalAccounts = String.valueOf(userAccountList.size());
            } catch (Exception e) {
                Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, e.toString());
            }
        }
        
        public void searchAccount() {
            toUserAccountList();
            try {
                if(!searchText.equals("")){
                    userAccountList = ds.getCommonQry().userAccountFindByAttribute(searchOptions, searchText, "STRING", false);
                    userAccountTableModel = new ListDataModel<>(userAccountList);
                }
            } catch (Exception e) {
                Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, e.toString());
            }
        }
        
        public void deleteAccounts() {
            toUserAccountList();
            try {
                List<UserAccount> selectedaccount = new ArrayList<>();
                for(UserAccount account : userAccountList){
                    if(account.getSelected() == true)
                        selectedaccount.add(account);
                }

                if(selectedaccount.isEmpty()){
                    JsfUtil.addErrorMessage(null, "No Account(s) Selected", "No account has been selected from list");

                } else {
                    List<UserAccount> deletingAccounts = new ArrayList<>();
                    for(UserAccount user : selectedaccount){
                        user.setLastModifiedBy(currentUser.getCurrentUser().getUserAccountId());
                        user.setLastModifiedDate(new Date());
                        deletingAccounts.add(user);
                    }

                    boolean deleted = ds.getCommonQry().batchUserAccountDelete(deletingAccounts, false);
                    if(deleted == true){
                        getAllAccounts();
                        JsfUtil.addInformationMessage("Account(s) Deleted succcessfully");
                    } else {
                        getAllAccounts();
                        JsfUtil.addErrorMessage(null, "Error", "Failed to Delete selected Account(s) from the list. Try again later");
                    }
                }
            } catch (Exception e) {
                Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
                JsfUtil.addErrorMessage(null, "System  Error!", "System encountered an error. Contact system administrator for solutions");
            }
        }
        
        public void saveAccount() {
            tabIndex = 1;
            try {
                if(userAccount.getUsername().equals(userAccount.getUserPassword()))
                    JsfUtil.addErrorMessage(null, "Error", "Username cannot be used as account Password");
                
                else if(!userAccount.getUserPassword().equals(rePswrd))
                    JsfUtil.addErrorMessage(null, "Error", "Passwords do not match");
                
                else if (userAccount.getUserPassword().length() < 8)
                    JsfUtil.addErrorMessage(null, "Error", "Password should not be less than eight(8) characters");
                else {
                    userAccount.setStaff(ds.getCommonQry().staffFind(staffId));

                    boolean hasAccount = ds.getCustomQry().doesStaffHaveAccountWithSameAccessRight(userAccount);
                    if (hasAccount == true) {
                        JsfUtil.addErrorMessage(null, "Error", "Staff already has an account with the selected Access Right");
                        
                    }else {
                        IDCreator.setUserAccountId(userAccount);
                        userAccount.setUserPassword(SecurityHash.getInstance().shaHash(userAccount.getUserPassword()));

                        boolean acctSaved = ds.getCommonQry().userAccountCreate(userAccount);
                        if(acctSaved == true){
                            JsfUtil.addInformationMessage("Account Created Successfully");
                            toNewAccount();
                            
                        } else {
                            JsfUtil.addErrorMessage(null, "Error", "Account was unable to be created. Try again later");
                        }
                    }

                }

            } catch (Exception e) {
                Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, e.toString(), e);
                JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact system administrator for solutions");
            }
        }
        
        public void updateAccount() {
            toUpdateAccount();
            try {
                upUserAccount.setUpdated(true);
                upUserAccount.setLastModifiedDate(new Date());
                upUserAccount.setLastModifiedBy(currentUser.getCurrentUser().getUserAccountId());
                boolean update = ds.getCommonQry().userAccountUpdate(upUserAccount);

                if (update == true) {
                    JsfUtil.addInformationMessage("Account updated Successfully");
                    
                } else {
                    JsfUtil.addErrorMessage(null, "Error", "An error occurred trying to update. Try again later");
                }
            } catch (Exception e) {
                Logger.getLogger(UserAccountController.class.getName()).log(Level.INFO, e.toString());
                JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact system administrator for solutions");
            }
        }
    //</editor-fold>
        
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getRePswrd() {
        return rePswrd;
    }

    public void setRePswrd(String rePswrd) {
        this.rePswrd = rePswrd;
    }

    public UserAccount getUpUserAccount() {
        return upUserAccount;
    }

    public void setUpUserAccount(UserAccount upUserAccount) {
        this.upUserAccount = upUserAccount;
    }

    public String getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(String totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        UserAccountController.tabIndex = tabIndex;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchOptions() {
        return searchOptions;
    }

    public void setSearchOptions(String searchOptions) {
        this.searchOptions = searchOptions;
    }
    
    public void setUserAccountList(List<UserAccount> userAccountList) {
        this.userAccountList = userAccountList;
    }

    public List<UserAccount> getUserAccountList() {
        return userAccountList;
    }
    
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
    
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public DataModel<UserAccount> getUserAccountTableModel() {
        return userAccountTableModel;
    }

    public void setUserAccountTableModel(DataModel<UserAccount> userAccountTableModel) {
        this.userAccountTableModel = userAccountTableModel;
    }

    public UserRole getSelectAccessRight() {
        return selectAccessRight;
    }

    public void setSelectAccessRight(UserRole selectAccessRight) {
        this.selectAccessRight = selectAccessRight;
    }

    //</editor-fold>
}

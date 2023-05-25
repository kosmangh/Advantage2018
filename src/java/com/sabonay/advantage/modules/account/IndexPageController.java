/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.modules.account;

import com.sabonay.advantage.common.utils.AdConstants;
import com.sabonay.advantage.ejb.entities.Setting;
import com.sabonay.advantage.ejb.entities.UserAccount;
import com.sabonay.advantage.web.utils.LoadUserPages;
import com.sabonay.advantage.web.utils.PagesLoad;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.security.SecurityHash;
import com.sabonay.modules.web.jsf.accesscontrol.HtmlUserPage;
import com.sabonay.modules.web.jsf.accesscontrol.HtmlUserPageAccessManager;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@Named
@SessionScoped
public class IndexPageController implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Implemented variables">
        private static IndexPageController indexPageController = new IndexPageController();
        private static final Logger LOG = Logger.getLogger(IndexPageController.class.getName());
    
        private String username;
        private String password;
        private String newPassword;
        private String oldPassword;
        private String loginPage;
        
        private LoadUserPages loadPagesMgr;
        private UserAccessRights uar = null;
        private AdvantageUserData userData = null;
        private Setting s;
        
        private boolean canView = false;

        private Double creditLeft = 0.0;
        
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Initiators">
        public IndexPageController() {
            s = new Setting();
        }

        @PostConstruct
        public void init(){
            uar = UserAccessRights.getManagedInstance();
            loadPagesMgr = new LoadUserPages(PagesLoad.setUpMenuItems());
            userData = AdvantageUserData.getManagedInstance();
            creditLeft = ds.getCommonQry().getCreditBalaceLeft();
        }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Business Logics">
        public String userLogin() {
            try {
                initAppUsers();

            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
                JsfUtil.addErrorMessage("System Error, try again later");
            }
            return null;
        }
        
        private String initAppUsers() {
            try {
                HtmlUserPageAccessManager manager = HtmlUserPageAccessManager.getInstance();
                manager.clearAllLoadedPage();
                
                //load features allowed to user
                loadUserFeatures();

                String hashedPassword = SecurityHash.getInstance().shaHash(password);
                UserAccount account = ds.getCustomQry().findUserDetail(username, hashedPassword);
                
                if (account == null) {
                    JsfUtil.addErrorMessage("Incorrect Usename/Password");
                    return null;
                }
                
                if (account.getUserCategory() == null) {
                    JsfUtil.addErrorMessage("Sorry, You've not been assign a role in the institution");
                    return null;
                }

                if (account.getAccountStatus().getId() == false) {
                    JsfUtil.addErrorMessage("Sorry, Account is inactive");
                    return null;
                }
                
                userData.setCurrentUser(account); 

                if (account.getUserCategory() == null) {
                    return null;
                }

                if (account.getUserCategory().getRoleName().equalsIgnoreCase(AdConstants.USER_CATEGORY_ADMINISTRATOR)) {
                    initRoleAdmin(manager, account);
                    canView = true;
                    
                } else if (account.getUserCategory().getRoleName().equalsIgnoreCase(AdConstants.USER_CATEGORY_DIRECTOR)) {
                    initRoleDirector(manager, account);
                    canView = true;
                    
                } else if (account.getUserCategory().getRoleName().equalsIgnoreCase(AdConstants.USER_CATEGORY_ESTATEMGR)) {
                    initRoleEstateMgr(manager, account);
                    
                } else if (account.getUserCategory().getRoleName().equalsIgnoreCase(AdConstants.USER_CATEGORY_ACCOUNTANT)) {
                    initRoleAccountant(manager, account);

                } else if (account.getUserCategory().getRoleName().equalsIgnoreCase(AdConstants.USER_CATEGORY_AUDITOR)) {
                    initRoleAuditor(manager, account);
                    
                } else {
                    return null;
                }

                return null;
                
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
                return null;
            }
        }
        
        public String changePassword(){
            try {
                UserAccount userAccount = ds.getCommonQry().userAccountFind(userData.getCurrentUser().getUserAccountId());
                if(userAccount == null){
                    JsfUtil.addErrorMessage("Useraccount does not exist");
                }
                String hashedPwd = SecurityHash.getInstance().shaHash(oldPassword);
                String oldPwd = userAccount.getUserPassword();
                if(!oldPwd.equalsIgnoreCase(hashedPwd)){
                    JsfUtil.addErrorMessage("Password does not ");
                }
                userAccount.setUserPassword(SecurityHash.getInstance().shaHash(newPassword));
                ds.getCommonQry().userAccountUpdate(userAccount);
                JsfUtil.addInformationMessage("Password changed successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        
        /**
         * Returns the login page after a successful logout and destroy of session
         * @return URL for login page
         */
        public String logout() {
            try {
                destroySession();
                JsfUtil.invalidateSession();
                return "index.xhtml?" + JsfUtil.REQUEST_HELPER; 
                
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
                JsfUtil.addErrorMessage(null, "System Error", "System Encountered an error. Contact system administrator for solutions");
                return null;
            }
        }
        
        
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="System Logics">
        private void loadUserFeatures() {
            String[] featureList = null;
            if (null != featureList) {
                loadPagesMgr.loadFeatures(featureList, uar);
            }
        }
        
        private void destroySession(){
            uar = null;
            userData = null;
            username = null;
            password = null;
        }
        
        public String help(){
            return "Advantage User Manual.html";
        }
        
        public static IndexPageController getInstance(){
            return indexPageController;
        }
    //</editor-fold>
        
        
    //<editor-fold defaultstate="collapsed" desc="User Accounts Pages">
        private void initRoleAdmin(HtmlUserPageAccessManager mgr, UserAccount ua) {
            try {
                //load pages and access rights
                loadPagesMgr.loadPages(mgr, PagesLoad.initAdminPagesActions(), uar);
                loadPagesMgr.loadFeatures(PagesLoad.adminFeatures, uar);
                List<HtmlUserPage> userPages = mgr.getAllRegistredPages();
                userData.setUserAccessPagesList(userPages);
                userData.updateUserPageGrouping();

                String userAccess = ua.getUserAccess();
                userData.setUserAccess(userAccess);
                userData.setHasUserLogin(true);
                uar.setUserAccessCode(userAccess);
                userData.setUserId(ua.getStaff().getStaffId());
                ds.getCommonQry().setCurrentUserID(ua.getStaff().getStaffId());
                uar.grantFullAccess();

            } catch (Exception e) {
            }
        }

        private void initRoleDirector(HtmlUserPageAccessManager mgr, UserAccount ua) {
            try {
                loadPagesMgr.loadPages(mgr, PagesLoad.initDirectorPagesActions(), uar);
                loadPagesMgr.loadFeatures(PagesLoad.directorFeatures, uar);
                List<HtmlUserPage> userPages = mgr.getAllRegistredPages();
                userData.setUserAccessPagesList(userPages);
                userData.updateUserPageGrouping();

                String userAccess = ua.getUserAccess();
                userData.setUserAccess(userAccess);
                userData.setHasUserLogin(true);
                uar.setUserAccessCode(userAccess);
                userData.setUserId(ua.getStaff().getStaffId());
                ds.getCommonQry().setCurrentUserID(ua.getStaff().getStaffId());
                userData.setUserHasAdministrativeRight(true);
            } catch (Exception e) {
            }
        }

        private void initRoleEstateMgr(HtmlUserPageAccessManager mgr, UserAccount ua) {
            try {
                loadPagesMgr.loadPages(mgr, PagesLoad.initEstateManagerPagesActions(), uar);
                loadPagesMgr.loadFeatures(PagesLoad.estateMgrFeatures, uar);
                List<HtmlUserPage> userPages = mgr.getAllRegistredPages();
                userData.setUserAccessPagesList(userPages);
                userData.updateUserPageGrouping();

                String userAccess = ua.getUserAccess();
                userData.setUserAccess(userAccess);
                userData.setHasUserLogin(true);
                uar.setUserAccessCode(userAccess);
                userData.setUserId(ua.getStaff().getStaffId());
                ds.getCommonQry().setCurrentUserID(ua.getStaff().getStaffId());
                userData.setUserHasAdministrativeRight(false);

            } catch (Exception e) {
            }
        }

        private void initRoleAccountant(HtmlUserPageAccessManager mgr, UserAccount ua) {
            try {
                loadPagesMgr.loadPages(mgr, PagesLoad.initFinancePagesActions(), uar);
                loadPagesMgr.loadFeatures(PagesLoad.estateMgrFeatures, uar);
                List<HtmlUserPage> userPages = mgr.getAllRegistredPages();
                userData.setUserAccessPagesList(userPages);
                userData.updateUserPageGrouping();

                String userAccess = ua.getUserAccess();
                userData.setUserAccess(userAccess);
                userData.setHasUserLogin(true);
                uar.setUserAccessCode(userAccess);
                userData.setUserId(ua.getStaff().getStaffId());
                ds.getCommonQry().setCurrentUserID(ua.getStaff().getStaffId());
                userData.setUserHasAdministrativeRight(false);
            } catch (Exception e) {
            }
        }

        private void initRoleAuditor(HtmlUserPageAccessManager mgr, UserAccount ua) {
            try {
                loadPagesMgr.loadPages(mgr, PagesLoad.initAuditorPagesActions(), uar);
                loadPagesMgr.loadFeatures(PagesLoad.estateMgrFeatures, uar);
                List<HtmlUserPage> userPages = mgr.getAllRegistredPages();
                userData.setUserAccessPagesList(userPages);
                userData.updateUserPageGrouping();

                String userAccess = ua.getUserAccess();
                userData.setUserAccess(userAccess);
                userData.setHasUserLogin(true);
                uar.setUserAccessCode(userAccess);
                userData.setUserId(ua.getStaff().getStaffId());
                ds.getCommonQry().setCurrentUserID(ua.getStaff().getStaffId());
                userData.setUserHasAdministrativeRight(false);
            } catch (Exception e) {
            }
        }
    //</editor-fold>
    
    
    private void seehow(){
        s.setSettingsKey("KEY-SET-LOGIN-CLASSsssssssssssssswedontexit");
        ds.getCommonQry().record(s);
    }


    //<editor-fold defaultstate="collapsed" desc="Getter And Setter">
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getCreditLeft() {
        return creditLeft;
    }

    public void setCreditLeft(Double creditLeft) {
        this.creditLeft = creditLeft;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoadUserPages getLoadPagesMgr() {
        return loadPagesMgr;
    }

    public void setLoadPagesMgr(LoadUserPages loadPagesMgr) {
        this.loadPagesMgr = loadPagesMgr;
    }

    public UserAccessRights getUar() {
        return uar;
    }

    public void setUar(UserAccessRights uar) {
        this.uar = uar;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    

    public AdvantageUserData getUserData() {
        return userData;
    }

    public void setUserData(AdvantageUserData userData) {
        this.userData = userData;
    }
    //</editor-fold>
}

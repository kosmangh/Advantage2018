/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.modules.account;

import com.sabonay.advantage.ejb.entities.UserAccount;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.common.utils.StringUtil;
import com.sabonay.modules.jasperreporting.ReportOutputFileType;
import com.sabonay.modules.web.jsf.accesscontrol.HtmlUserPage;
import com.sabonay.modules.web.jsf.accesscontrol.WebUserData;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.File;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
//import org.richfaces.event.ItemChangeEvent;

/**
 *
 * @author Edwin / Ritchid
 */
@Named(value = "userdata")
@SessionScoped
public class AdvantageUserData extends WebUserData implements Serializable {

    private static final String DOC_ROOT = System.getProperty("com.sun.aas.instanceRoot") + File.separator + "docroot" + File.separator + "shc_res" + File.separator;
    private String directorSignature = "";
    private String shcServerPath = "";
    private static final String PAGE_BEAN_NAME = "userdata";
    private String currentPageGroupCode = "General Actions";
    private HtmlUserPage currentUserPage = null;
    private UserAccessRights uar = null;
    private UserAccount currentUser = null;
    private static final String APP_HOME_PAGE = "index.xhtml?" + JsfUtil.REQUEST_HELPER;
    private int ledgerYear = DateTimeUtils.getCurrentYear();
    private ReportOutputFileType reportOutput = ReportOutputFileType.PDF;
    private int currentTab = 0;
    private Double creditLeft = null;

    public AdvantageUserData() {
        init(); 
        setUserRequestedPageURL("default.xhtml");
    }

    private void init() {
        shcServerPath = DOC_ROOT + File.separator;
        directorSignature = DOC_ROOT + "director-signature.png";
        directorSignature = StringUtil.ecapeBackSlash(directorSignature);
        currentUserPage = new HtmlUserPage();
        creditLeft = ds.getCommonQry().getCreditBalaceLeft();
    }

    @Override
    public String setRequestedPageURL(String pageURL) {
        setUserRequestedPageURL(pageURL);
        return "index.xhtml?faces-redirect=true&amp;includeViewParams=true";
    }

    public static AdvantageUserData getManagedInstance() {
        AdvantageUserData data = (AdvantageUserData) JsfUtil.getManagedBean(PAGE_BEAN_NAME);

        if (data != null) {
            return data;
        }

        throw new RuntimeException("Unable to create user session");
    }

    public void switchReportToPDF() {
        reportOutput = ReportOutputFileType.PDF;
    }

    public void switchReportToHTML() {
        reportOutput = ReportOutputFileType.XHTML;
    }

    // <editor-fold defaultstate="collapsed" desc="Setters and Getters">
    public int getLedgerYear() {
        return ledgerYear;
    }

    public void setLedgerYear(int ledgerYear) {
        this.ledgerYear = ledgerYear;
    }

    public ReportOutputFileType getReportOutput() {
        return reportOutput;
    }

    public void setReportOutput(ReportOutputFileType reportOutput) {
        this.reportOutput = reportOutput;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    public String getCurrentPageGroupCode() {
        return currentPageGroupCode;
    }

    public void setCurrentPageGroupCode(String currentPageGroupCode) {
        this.currentPageGroupCode = currentPageGroupCode;
    }

    public HtmlUserPage getCurrentUserPage() {
        return currentUserPage;
    }

    public void setCurrentUserPage(HtmlUserPage currentUserPage) {
        this.currentUserPage = currentUserPage;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserAccount currentUser) {
        this.currentUser = currentUser;
    }

    public UserAccessRights getUar() {
        return uar;
    }

    public void setUar(UserAccessRights uar) {
        this.uar = uar;
    }

    public String getDirectorSignature() {
        return directorSignature;
    }

    public void setDirectorSignature(String directorSignature) {
        this.directorSignature = directorSignature;
    }

    public static String APP_HOME_PAGE() {
        return APP_HOME_PAGE;
    }

    public Double getCreditLeft() {
        return creditLeft;
    }

    public void setCreditLeft(Double creditLeft) {
        this.creditLeft = creditLeft;
    }
    
    public String getShcServerPath() {
        return shcServerPath;
    }

    public void setShcServerPath(String shcServerPath) {
        this.shcServerPath = shcServerPath;
    }
    // </editor-fold>
}

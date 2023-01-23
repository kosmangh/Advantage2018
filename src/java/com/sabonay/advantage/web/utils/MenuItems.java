/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.utils;

/**
 *
 * @author crash
 */
public class MenuItems {

    
    private MenuItem itemId;
    private String pageName;
    private String pageURL;
    private String pageCode;
    private String pageGroupCode;
    private String pageDescription;
    private int currentTab;

    public MenuItems(MenuItem itemId, String pageCode, String pageName, String pageURL, String pageDescription, String pageGroupCode, int currentTab) {
        this.itemId = itemId;
        this.pageCode = pageCode;
        this.pageName = pageName;
        this.pageURL = pageURL;
        this.pageDescription = pageDescription;
        this.pageGroupCode = pageGroupCode;
        this.currentTab = currentTab;
        
    }

    //<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public MenuItem getItemId() {
        return this.itemId;
    }

    public void setItemId(MenuItem itemId) {
        this.itemId = itemId;
    }

    public String getPageName() {
        return this.pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageURL() {
        return this.pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public String getPageCode() {
        return this.pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getPageGroupCode() {
        return this.pageGroupCode;
    }

    public void setPageGroupCode(String pageGroupCode) {
        this.pageGroupCode = pageGroupCode;
    }

    public String getPageDescription() {
        return this.pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }
    
    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }
    //</editor-fold>

}

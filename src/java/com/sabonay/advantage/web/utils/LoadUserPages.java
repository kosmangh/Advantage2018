/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.utils;

import com.sabonay.common.formating.SentenceCases;
import com.sabonay.modules.web.jsf.accesscontrol.HtmlUserPageAccessManager;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author crash
 */
public class LoadUserPages {

    ArrayList<MenuItems> setupmenuitems = null;
    public static final String OK = "OK";

    public LoadUserPages(ArrayList<MenuItems> setupmenuitems) {
        this.setupmenuitems = setupmenuitems;
    }

    public ArrayList<MenuItems> getSetupmenuitems() {
        return this.setupmenuitems;
    }

    public void setSetupmenuitems(ArrayList<MenuItems> setupmenuitems) {
        this.setupmenuitems = setupmenuitems;
    }

    public MenuItemActions isPageViewable(ArrayList<MenuItemActions> mialist, MenuItem menuitemid) {
        if ((mialist == null) || (menuitemid == null)) {
            return null;
        }

        Iterator iter = mialist.iterator();

        while (iter.hasNext()) {
            MenuItemActions pageitem = (MenuItemActions) iter.next();
            if (pageitem.getItemId().equals(menuitemid)) {
                return pageitem;
            }
        }

        return null;
    }

    public String loadPages(HtmlUserPageAccessManager upamgr, ArrayList<MenuItemActions> mialist, Object uar) {
        if (null == mialist) {
            return null;
        }
        Iterator iter = this.setupmenuitems.iterator();

        RunAccessFeatureMethods reflectedmethods = new RunAccessFeatureMethods();

        while (iter.hasNext()) {
            MenuItems pageitem = (MenuItems) iter.next();
            MenuItem menuitemid = pageitem.getItemId();
            MenuItemActions pageactionitem = isPageViewable(mialist, menuitemid);

            if (null != pageactionitem) {
                upamgr.createUserPage(pageitem.getCurrentTab(),pageitem.getPageCode(), pageitem.getPageName(), pageitem.getPageURL(), pageitem.getPageDescription(), pageitem.getPageGroupCode());
                boolean isedit;
                if (pageactionitem.isEditable()) {
                    isedit = true;
                } else {
                    isedit = false;
                }

                if (null != pageactionitem.getEdit_page_name()) {
                    String editmethod = SentenceCases.stringToSetMethodName(pageactionitem.getEdit_page_name());

                    reflectedmethods.runMethod(uar, editmethod, isedit);
                }

            }
        }

        return "OK";
    }

    public String loadFeatures(String[] featuresList, Object uar) {
        if (null == featuresList) {
            return null;
        }

        RunAccessFeatureMethods reflectedmethods = new RunAccessFeatureMethods();

        for (String feature : featuresList) {
            if (null != feature) {
                String featuremethod = SentenceCases.stringToSetMethodName(feature);

                reflectedmethods.runMethod(uar, featuremethod, true);
            }

        }

        return "OK";
    }
}

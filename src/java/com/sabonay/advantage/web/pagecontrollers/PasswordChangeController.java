/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.pagecontroller;

import com.sabonay.modules.web.jsf.component.HtmlPasswordChangePanel;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Edwin
 */
@ManagedBean(name="passwordChangeController")
@SessionScoped
public class PasswordChangeController {


    private HtmlPasswordChangePanel passwordChangePanel = new HtmlPasswordChangePanel();

    public PasswordChangeController() {
    }

    public HtmlPasswordChangePanel getPasswordChangePanel()
    {
        return passwordChangePanel;
    }

    public void setPasswordChangePanel(HtmlPasswordChangePanel passwordChangePanel)
    {
        this.passwordChangePanel = passwordChangePanel;
    }

}

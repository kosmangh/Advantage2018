/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.modules.account;

import com.sabonay.common.api.SystemUser;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;
import com.sabonay.modules.web.jsf.component.HtmlDataPanel;

/**
 *
 * @author Edwin
 */
public class UserAccountTableModel extends AbstractWebTableModel<SystemUser>
{
    private String[] headers = {"User Staff Id","Staff Name"};
    private String[] codes = {"staff","username"};

    public UserAccountTableModel()
    {
        setColumnHeaders(headers);
        setColumnCodes(codes);
    }

    @Override
    public HtmlDataPanel getDataPanel()
    {
        return super.getDataPanel("javax.faces.component.html.HtmlDataTable");
    }


}

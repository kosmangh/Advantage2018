/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.modules.account;


import com.sabonay.common.api.Staff;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;
import com.sabonay.modules.web.jsf.component.HtmlDataPanel;
import javax.faces.component.html.HtmlDataTable;

/**
 *
 * @author Edwin
 */
public class UserCheckTableModel extends AbstractWebTableModel<Staff>
{
    private String[] headers = {"Account ID","Staff Name","Contact Number"};
    private String[] codes = {"staffId","surname","contactNumber"};

    public UserCheckTableModel() {
        setColumnHeaders(headers);
        setColumnCodes(codes);
    }

    @Override
    public HtmlDataPanel getDataPanel()
    {
        return super.getDataPanel("javax.faces.component.html.HtmlDataTable");
    }
}

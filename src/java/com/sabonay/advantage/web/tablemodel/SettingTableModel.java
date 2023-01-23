/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.Setting;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;

/**
 *
 * @author Edwin
 */
public class SettingTableModel  extends AbstractWebTableModel<Setting>
{
    private String columnHeader[] = {"Settings Key","Settings Value"};

    private String columnCodes[] = {"settingsKey","settingsValue","lastModifiedBy","lastModifiedDate","deleted","updated"};

    public SettingTableModel()
    {
        setColumnHeaders(columnHeader);
        setColumnCodes(columnCodes);
    }

    @Override
    public UIComponent getColumnComponent(int columnIndex)
    {
        if(columnIndex == 1)
        {
            HtmlInputText htmlInputText = new HtmlInputText();
            return htmlInputText;

        }
        return super.getColumnComponent(columnIndex);
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;
import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;

/**
 *
 * @author Edwin
 */
public class RecordEntryDetailTableModel extends AbstractWebTableModel<Estate> implements Serializable
{

   private String columnHeader[] = {"Property Number","Property Occupant","Arreas From","Verified"};

    private String columnCodes[] = {"propertyName","propertyOccupant","arrreasFrom","verified","entryDetails","payeeName","mediumOfPayment"};


    public RecordEntryDetailTableModel()
    {
        setColumnHeaders(columnHeader);
        setColumnCodes(columnCodes);
        setVarName("es");
    }

    @Override
    public UIComponent getColumnComponent(int columnIndex)
    {
        if(columnIndex == 1)
        {
            HtmlInputText htmlInputText = new HtmlInputText();
            htmlInputText.setStyle("width:100%;");

            return htmlInputText;
        }
        if(columnIndex == 2)
        {
            HtmlInputText htmlInputText = new HtmlInputText();
            return htmlInputText;
        }
        if(columnIndex == 3)
        {
            HtmlSelectBooleanCheckbox htmlInputText = new HtmlSelectBooleanCheckbox();
            
            return htmlInputText;
        }

        return super.getColumnComponent(columnIndex);
    }








}

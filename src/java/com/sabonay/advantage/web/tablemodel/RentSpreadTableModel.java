/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.models.RentSpread;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;
import javax.faces.component.UIComponent;

/**
 *
 * @author Edwin
 */
public class RentSpreadTableModel extends AbstractWebTableModel<RentSpread>
{

    private String columnHeader[] = {"Estate Property ","property Occupant","current Rent","current Rent Balance"};

    private String columnCodes[] = {"estateProperty","propertyOccupant","currentRent","currentRentBalance"};

    public RentSpreadTableModel()
    {
        setColumnHeaders(columnHeader);
        setColumnCodes(columnCodes);
    }

    @Override
    public UIComponent getColumnComponent(int columnIndex)
    {
        switch(columnIndex)
        {
            case 2:
            case 3: return columTextField();
        }
        return super.getColumnComponent(columnIndex);
    }


}

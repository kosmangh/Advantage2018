/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;

/**
 *
 * @author Edwin
 */
public class PropertyOccupantTableModel extends AbstractWebTableModel<PropertyOccupant>
{
    private String columnHeader[] = {"Property Occupant Name","Type","Home Town / Location","Occupation/Core Buss","National Id","National Id Type","Telephone Number","Gender / Category"};

    private String columnCodes[] = {"occupantName","occupantType","homeTown","occupation","nationalId","nationalIdType","telephoneNumber","gender"};
    
    public PropertyOccupantTableModel()
    {
        setColumnCodes(columnCodes);
        setColumnHeaders(columnHeader);

        setVarName("lee");
    }



}

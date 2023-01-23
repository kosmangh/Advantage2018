/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;

/**
 *
 * @author Edwin / Ritchid
 */
public class OccupantPropertyTableModel extends AbstractWebTableModel<Occupancy>
{
     private String columnHeader[] = {"Occupant Name","Estate Property","Occupant Type","First Date Of Occupancy","Last Date Of Occupancy","Last Modified Date","Last Modified By"};

    private String columnCodes[] = {"propertyOccupant.occupantName","estateProperty.estatePropertyId","occupationType","firstDateOfOccupancy","lastDateOfOccupancy","lastModifiedDate","lastModifiedBy"};


//    private String columnCodes[] = {"propertyOccupant.occupantName","estateProperty.estatePropertyId","propertyOccupant.occupantType","#{commonUtils.formatDate(op.firstDateOfOccupancy)}","#{commonUtils.formatDate(op.lastDateOfOccupancy)}","lastModifiedDate","lastModifiedBy"};



     public OccupantPropertyTableModel()
    {
        setColumnHeaders(columnHeader);
        setColumnCodes(columnCodes);

        setVarName("op");
    }


}

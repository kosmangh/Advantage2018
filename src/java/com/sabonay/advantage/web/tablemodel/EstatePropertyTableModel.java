/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;

/**
 *
 * @author Edwin
 */
public class EstatePropertyTableModel extends AbstractWebTableModel<EstateProperty>
{

    private String columnHeader[] = {"Estate Property Id","Property Usage","Property Category","Property Number","Property Land Size","Estate Block","Property Estate","Property Name"};

    private String columnCodes[] = {"estatePropertyId","propertyUsage","propertyCategory","propertyNumber","propertyLandSize","estateBlock","propertyEstate","propertyName"};

    public EstatePropertyTableModel()
    {
        setColumnHeaders(columnHeader);
        setColumnCodes(columnCodes);
    }
}

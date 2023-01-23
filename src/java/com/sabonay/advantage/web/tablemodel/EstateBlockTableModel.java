/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;

/**
 *
 * @author Edwin
 */
public class EstateBlockTableModel extends AbstractWebTableModel<EstateBlock>{

    private String columnHeader[] = {"Estate Block Id","Block Name","Estate"};

    private String columnCodes[] = {"estateBlockId","blockName","estate"};

    public EstateBlockTableModel()
    {
        setColumnHeaders(columnHeader);
        setColumnCodes(columnCodes);
    }
}

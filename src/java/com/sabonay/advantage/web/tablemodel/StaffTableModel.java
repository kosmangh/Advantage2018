/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.Staff;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;

/**
 *
 * @author Edwin
 */
public class StaffTableModel extends AbstractWebTableModel<Staff>
{

    private String columnHeader[] = {"Staff Id","Staff Surname","Staff Othernames","Staff Phone Number"};

    private String columnCodes[] = {"staffId","surname","othernames","contactNumber"};

    public StaffTableModel()
    {
        setColumnHeaders(columnHeader);
        setColumnCodes(columnCodes);
    }
}

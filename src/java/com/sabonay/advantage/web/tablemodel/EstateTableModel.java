/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;
import java.io.Serializable;

/**
 *
 * @author Edwin
 */
public class EstateTableModel extends AbstractWebTableModel<Estate> implements Serializable
{

    private String columnHeader[] = {"Estate Id","Estate Name","Estate Class","Land Size","Estate Location","Last Modified Date","Last Modified By"};

    private String columnCodes[] = {"estateId","estateName","estateClass","landSize","estateLocation","lastModifiedDate","lastModifiedBy"};


    public EstateTableModel()
    {
        setColumnHeaders(columnHeader);
        setColumnCodes(columnCodes);
        setVarName("es");
    }

//    @Override
//    public HtmlDataPanel getDataPanel()
//    {
//        HtmlDataPanel  hdp =  new HtmlDataPanel()
//        {
//            @Override
//            public UIData getDataTable()
//            {
//                return DataTableProducer.creataeDataTable();
//            }
//
//        };
//
//        hdp.setWebTableModel(this);
//
//        return hdp;
//
//    }


    




}

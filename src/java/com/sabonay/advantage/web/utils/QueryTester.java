/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.utils;

import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import java.util.List;

/**
 *
 * @author crash
 */
public class QueryTester {
    
    public static void main(String [] args){
        List<EstatePropertyLedger> epl = ds.getCustomQry().getLedgerPaymentsForMonth(2014, "January");
        System.out.println(epl.toString());
    }
}

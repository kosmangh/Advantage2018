/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.sms.utils;

import com.sabonay.advantage.common.utils.BillDemandNoteMaker;
import com.sabonay.advantage.common.utils.GroundRentBiller;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.models.BillDemandNote;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.formating.NumberFormattingUtils;

/**
 *
 * @author crash
 */
public class GroundRentProcessor {
    
    public static String prepareGroundRentSMS(EstateProperty estateProperty, int baseYear){
        try {
            String response = "";
            StringBuilder sb = new StringBuilder();
            double currentGrndRent = GroundRentBiller.instance().getCalculated(estateProperty, baseYear);
            BillDemandNoteMaker billDemandNoteMaker = new BillDemandNoteMaker(estateProperty, baseYear);
            billDemandNoteMaker.process();
            BillDemandNote demandNote = billDemandNoteMaker.demandNotice();
            double amtPayable = NumberFormattingUtils.formatDecimalNumberTo_2(demandNote.getOutstandingBalance() + currentGrndRent);
            
            sb.append("Dear ").append(estateProperty.getCurrentOccupantName()).append(" on Property# ").append(estateProperty.getEstatePropertyId()).append(", ").append("Your Ground Rent DemandNote").append(" \n");
           
            sb.append("Arrears as of ").append(demandNote.getBalancePeriodOfDemandNotice()).append(":")
                    .append(demandNote.getOutstandingBalance() ).append(" \n");
            sb.append("Bill for ").append(baseYear).append(":").append(currentGrndRent).append(" \n");
            sb.append("Amt Payable:").append(amtPayable).append(" \n");
            sb.append("Thank you.");
            return response = sb.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String prepareGroundRentSMS(String estatePropertyId, int baseYear){
        try {
            EstateProperty estateProperty = ds.getCommonQry().estatePropertyFind(estatePropertyId);
            String response = "";
            StringBuilder sb = new StringBuilder();
            double currentGrndRent = GroundRentBiller.instance().getCalculated(estateProperty, baseYear);
            BillDemandNoteMaker billDemandNoteMaker = new BillDemandNoteMaker(estateProperty, baseYear);
            billDemandNoteMaker.process();
            BillDemandNote demandNote = billDemandNoteMaker.demandNotice();
            double amtPayable = NumberFormattingUtils.formatDecimalNumberTo_2(demandNote.getOutstandingBalance() + currentGrndRent);
            
            sb.append("Dear ").append(estateProperty.getCurrentOccupantName()).append(" on Property# ").append(estateProperty.getEstatePropertyId()).append(", ").append("Your Ground Rent DemandNote").append(" \n");
           
            sb.append("Arrears as of ").append(demandNote.getBalancePeriodOfDemandNotice()).append(":")
                    .append(demandNote.getOutstandingBalance() ).append(" \n");
            sb.append("Bill for ").append(baseYear).append(":").append(currentGrndRent).append(" \n");
            sb.append("Amt Payable:").append(amtPayable).append(" \n");
            sb.append("Thank you.");
            return response = sb.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

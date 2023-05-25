/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.ejb.entities.GroundRent;
import com.sabonay.advantage.ejb.models.GroundRentDetail;
import com.sabonay.common.collection.comparators.StringValueComparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Edwin
 */
public class GroundRentRelatedReport
{

    public static List<GroundRentDetail> getReport(List<GroundRent> groundRentsList)
    {
        List<GroundRentDetail> groundRentDetailList = new LinkedList<GroundRentDetail>();

        if(groundRentsList == null)
            return groundRentDetailList;

        Double firstClassAmt ,secondClassAmt , thirdClassAmt ;



//        Double amount = null;

        for (GroundRent groundRent : groundRentsList)
        {
            GroundRentDetail rentDetail = new GroundRentDetail();

            if(groundRent == null)
            {
                System.out.println("found null rent for " + groundRent);
                continue;
            }
            
            firstClassAmt = groundRent.getFirstClassEstateAmountCharged();

            secondClassAmt = groundRent.getSecondClassEstateAmountCharged();

            thirdClassAmt = groundRent.getThirdClassEstateAmountCharged();


            rentDetail.setYearDue(groundRent.getYearDue());
            rentDetail.setPropertyUsage(groundRent.getPropertyUsage().getUsageName());
            rentDetail.setFirstClassEstateAmountCharged(firstClassAmt);
            rentDetail.setSecondClassEstateAmountCharged(secondClassAmt);
            rentDetail.setThirdClassEstateAmountCharged(thirdClassAmt);

            groundRentDetailList.add(rentDetail);
        }

        System.out.println("b4  " + groundRentDetailList);
        StringValueComparator.sort(groundRentDetailList);
        System.out.println("af  " + groundRentDetailList);
        return groundRentDetailList;
    }

}

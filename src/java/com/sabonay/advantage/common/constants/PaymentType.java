/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.common.constants;


public enum PaymentType
{

    GROUND_RENT("Ground Rent","GR","year"),
    HOUSE_RENT("Property Rent", "PR","month");


    private PaymentType(String paymentTypeName, String initials,String paymentPeriod )
    {
        this.paymentTypeName = paymentTypeName;
        this.initials = initials;
        this.paymentPeriod = paymentPeriod;
    }



    private String paymentTypeName;

    private String initials;

    private String paymentPeriod;

    public String getFullString()
    {
        return getClass().getCanonicalName()+"."+name();
    }

    public String getInitials()
    {
        return initials;
    }

    public String getPaymentPeriod()
    {
        return paymentPeriod;
    }

    public String getPaymentTypeName()
    {
        return paymentTypeName;
    }
//
//    @Override
//    public String toString()
//    {
//        return paymentTypeName;
//    }



    
}

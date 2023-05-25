/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin / Ritchid
 */
public class PropertyLedgerSummary extends EstatePropertyDetail {

    private String propertyOccupantName;
    private Double accountBalance;
    private String periodOfBalance;

    
    
    
    public Double getAccountBalance()
    {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance)
    {
        this.accountBalance = accountBalance;
    }

    public String getPeriodOfBalance()
    {
        return periodOfBalance;
    }

    public void setPeriodOfBalance(String periodOfBalance)
    {
        this.periodOfBalance = periodOfBalance;
    }

    
    public String getPropertyOccupantName()
    {
        return propertyOccupantName;
    }

    public void setPropertyOccupantName(String propertyOccupantName)
    {
        this.propertyOccupantName = propertyOccupantName;
    }



    
}

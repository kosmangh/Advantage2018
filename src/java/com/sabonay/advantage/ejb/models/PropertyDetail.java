/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

/**
 *
 * @author crash
 */
public class PropertyDetail extends BlockDetail{
    
    private String propertyNumber;
    private String estatePropertyId;
    private double totalDebit;
    private double totalCredit;
    private double amtOutStanding;

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getEstatePropertyId() {
        return estatePropertyId;
    }

    public void setEstatePropertyId(String estatePropertyId) {
        this.estatePropertyId = estatePropertyId;
    }

    public double getAmtOutStanding() {
        amtOutStanding = totalCredit - totalDebit;
        return amtOutStanding;
    }

    public void setAmtOutStanding(double amtOutStanding) {
        this.amtOutStanding = amtOutStanding;
    }

    public double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }
    
    
}

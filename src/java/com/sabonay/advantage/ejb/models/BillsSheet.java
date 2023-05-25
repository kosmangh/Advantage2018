/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

/**
 *
 * @author SEBASTIAN
 */
public class BillsSheet extends BillsDetails{
    
    private double billAmountPaid;
    private double sumOfAmountPaid;
    
    private int estateCount;
   // private double 

    public double getBillAmountPaid() {
        return billAmountPaid;
    }

    public void setBillAmountPaid(double billAmountPaid) {
        this.billAmountPaid = billAmountPaid;
    }

    public double getSumOfAmountPaid() {
        return sumOfAmountPaid;
    }

    public void setSumOfAmountPaid(double sumOfAmountPaid) {
        this.sumOfAmountPaid = sumOfAmountPaid;
    }

    public int getEstateCount() {
        return estateCount;
    }

    public void setEstateCount(int estateCount) {
        this.estateCount = estateCount;
    }
    
    
    
    
    
    
}

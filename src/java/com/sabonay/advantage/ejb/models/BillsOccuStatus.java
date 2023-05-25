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
public class BillsOccuStatus {
    
    private String billDetail;
    private String billType;
    private int billYear;
    private double billAmount;
    private double billAmountPaid;
    private String billStatus;

    public String getBillDetail() {
        return billDetail;
    }

    public void setBillDetail(String billDetail) {
        this.billDetail = billDetail;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public int getBillYear() {
        return billYear;
    }

    public void setBillYear(int billYear) {
        this.billYear = billYear;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public double getBillAmountPaid() {
        return billAmountPaid;
    }

    public void setBillAmountPaid(double billAmountPaid) {
        this.billAmountPaid = billAmountPaid;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }
    
    
    
    
    
            
    
}

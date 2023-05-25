/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

/**
 *
 * @author crash
 */
public class LeaseDetail extends EstatePropertyDetail {
    
    private String periodOfBal;
    private Double groundRentBalance;
    private String occupantName;
    private Double currentGroundRent;
    private Double amountPaid;

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPeriodOfBal() {
        return periodOfBal;
    }

    public void setPeriodOfBal(String periodOfBal) {
        this.periodOfBal = periodOfBal;
    }

    public Double getGroundRentBalance() {
        return groundRentBalance;
    }

    public void setGroundRentBalance(Double groundRentBalance) {
        this.groundRentBalance = groundRentBalance;
    }

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public Double getCurrentGroundRent() {
        return currentGroundRent;
    }

    public void setCurrentGroundRent(Double currentGroundRent) {
        this.currentGroundRent = currentGroundRent;
    }
    
    
    
}

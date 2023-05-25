/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin
 */
public class LedgerSheet extends BlockDetail {

    private double creditBalance;
    private double debitBalance;
    private double yearBill;
    private double amountPaid;
    private double balanceBF;

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(double creditBalance) {
        this.creditBalance = creditBalance;
    }

    public double getDebitBalance() {
        return debitBalance;
    }

    public void setDebitBalance(double debitBalance) {
        this.debitBalance = debitBalance;
    }

    public double getYearBill() {
        return yearBill;
    }

    public void setYearBill(double yearBill) {
        this.yearBill = yearBill;
    }

    public double getBalanceBF() {
        balanceBF = debitBalance - creditBalance;
        return balanceBF;
    }

    public void setBalanceBF(double balanceBF) {
        this.balanceBF = balanceBF;
    }

}

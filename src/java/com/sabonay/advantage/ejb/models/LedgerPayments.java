/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

import java.util.Date;

/**
 *
 * @author crash
 */
public class LedgerPayments {
    private String occupantName;
    private String estateProperty;
    private double amountPaid;
    private Date dateOfTransaction;
    private String receiptNumber;

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public String getEstateProperty() {
        return estateProperty;
    }

    public void setEstateProperty(String estateProperty) {
        this.estateProperty = estateProperty;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
    
    
}

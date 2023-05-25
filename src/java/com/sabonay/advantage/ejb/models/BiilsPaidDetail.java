/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

/**
 *
 * @author ABDULMUMIN
 */
public class BiilsPaidDetail {
    
    public String propertyName;
    public String occupantName;
    public String esatePropertyType;
    public String estateInitials;
    public String estateName;
    public double currentPropertyRent=0;
    public double amountPaid=0;
    public double rentBalance=0;
    public String receiptNumber;
    public String hseType;

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getEstateInitials() {
        return estateInitials;
    }

    public void setEstateInitials(String estateInitials) {
        this.estateInitials = estateInitials;
    }

    public double getCurrentPropertyRent() {
        return currentPropertyRent;
    }

    public void setCurrentPropertyRent(double currentPropertyRent) {
        this.currentPropertyRent = currentPropertyRent;
    }

    public String getEstateName() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName = estateName;
    }

    public String getEsatePropertyType() {
        return esatePropertyType;
    }

    public void setEsatePropertyType(String esatePropertyType) {
        this.esatePropertyType = esatePropertyType;
    }

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public double getRentBalance() {
        return rentBalance;
    }

    public void setRentBalance(double rentBalance) {
        this.rentBalance = rentBalance;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getHseType() {
        return hseType;
    }

    public void setHseType(String hseType) {
        this.hseType = hseType;
    }
    
}

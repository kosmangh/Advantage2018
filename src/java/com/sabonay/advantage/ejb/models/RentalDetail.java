/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin
 */
public class RentalDetail extends EstatePropertyDetail {

    private String occupantName;
    private String periodOfBalance;
    private Double currentPropertyRent;
    private Double rentBalance;
    private Double amountPaid = 0.00;
    private Double totalDebit = 0.00;
    private String assessmentSummary;
    private Double arrears;

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public Double getCurrentPropertyRent() {
        return currentPropertyRent;
    }

    public void setCurrentPropertyRent(Double currentPropertyRent) {
        this.currentPropertyRent = currentPropertyRent;
    }

    public Double getRentBalance() {
        return rentBalance;
    }

    public void setRentBalance(Double rentBalance) {
        this.rentBalance = rentBalance;
    }

    public String getPeriodOfBalance() {
        return periodOfBalance;
    }

    public void setPeriodOfBalance(String periodOfBalance) {
        this.periodOfBalance = periodOfBalance;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getAssessmentSummary() {
        return assessmentSummary;
    }

    public void setAssessmentSummary(String assessmentSummary) {
        this.assessmentSummary = assessmentSummary;
    }

    public Double getArrears() {
        return arrears;
    }

    public void setArrears(Double arrears) {
        this.arrears = arrears;
    }
    

}

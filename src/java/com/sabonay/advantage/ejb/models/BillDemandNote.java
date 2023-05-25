/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin
 */
public class BillDemandNote extends PropertyOccupantDetail {

    private String estatePropertyId = "";
    private String estateProperty = "";
    private double groundRentBill;
    private double amountPaid;
    private double outstandingBalance = 0.0;
    private double penalty = 0.0;
    private String propertyUsage = "";
    private String propertyLocation = "";
    private String billFootnotes = "";
    private String demandNoticeHeading;
    private String billIntroNote;
    private String timeOfDemandNotice = "";
    private String billReviewDate = "";
    private String balancePeriodOfDemandNotice = "";
    private String billConclusion;
    private String statement = "";
    private String statement_header = "";
    private Integer yearsOwingFrom;

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getEstateProperty() {
        return estateProperty;
    }

    public void setEstateProperty(String estateProperty) {
        this.estateProperty = estateProperty;
    }

    public Double getGroundRentBill() {
        return groundRentBill;
    }

    public String getTimeOfDemandNotice() {
        return timeOfDemandNotice;
    }

    public void setTimeOfDemandNotice(String timeOfDemandNotice) {
        this.timeOfDemandNotice = timeOfDemandNotice;
    }

    public void setGroundRentBill(double groundRentBill) {
        this.groundRentBill = groundRentBill;
    }

    public Double getOutstandingBalance() {

        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    public String getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public String getEstatePropertyId() {
        return estatePropertyId;
    }

    public void setEstatePropertyId(String estatePropertyId) {
        this.estatePropertyId = estatePropertyId;
    }

    public String getBalancePeriodOfDemandNotice() {
        return balancePeriodOfDemandNotice;
    }

    public void setBalancePeriodOfDemandNotice(String balancePeriodOfDemandNotice) {
        this.balancePeriodOfDemandNotice = balancePeriodOfDemandNotice;
    }

    public String getBillConclusion() {
        return billConclusion;
    }

    public void setBillConclusion(String billConclusion) {
        this.billConclusion = billConclusion;
    }

    public void appentToStatement(String newStatement) {
        statement += "\n" + newStatement;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Integer getYearsOwingFrom() {
        return yearsOwingFrom;
    }

    public void setYearsOwingFrom(Integer yearsOwingFrom) {
        this.yearsOwingFrom = yearsOwingFrom;
    }

    public String getBillFootnotes() {
        return billFootnotes;
    }

    public void setBillFootnotes(String billFootnotes) {
        this.billFootnotes = billFootnotes;
    }

    public String getDemandNoticeHeading() {
        return demandNoticeHeading;
    }

    public void setDemandNoticeHeading(String demandNoticeHeading) {
        this.demandNoticeHeading = demandNoticeHeading;
    }

    public String getBillIntroNote() {
        return billIntroNote;
    }

    public void setBillIntroNote(String billIntroNote) {
        this.billIntroNote = billIntroNote;
    }

    public String getBillReviewDate() {
        return billReviewDate;
    }

    public void setBillReviewDate(String billReviewDate) {
        this.billReviewDate = billReviewDate;
    }

    public String getStatement_header() {
        return statement_header;
    }

    public void setStatement_header(String statement_header) {
        this.statement_header = statement_header;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }
}

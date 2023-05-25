/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

import java.util.Date;

/**
 *
 * @author Edwin
 */
public class PropertyLedgerDetail {

    private String estateProperty;
    private String occupantName;
    private String propertyUsage;
    private String propertyLocation;
    private String propertyOccupantId;
    private String entryFor;
    private Date dateOfTransaction;
    private String receiptNumber;
    private String estatePropertyId;
    private String estatePropertyLedgerId;
    private Date dateOfRecordEntry;
    private Date dateOfRecordTransaction;
    private String debitCredit;
    private Double amountInvolved;
    private String receiptNumberIssued;
    private String paymentFor;
    private String payeeName;
    private String mediumOfPayment;
    private String mediumOfPaymentNumber;
    private Integer ledgerYear;
    private String recordedBy;
    private String zonalOffice;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private String deleted;
    private String updated;
    private String paymentType;
    private String propertyOccupant;
    private Double charge;
    private Double payment;
    private Double balance;

    public Double getAmountInvolved() {
        return amountInvolved;
    }

    public void setAmountInvolved(Double amountInvolved) {
        this.amountInvolved = amountInvolved;
    }

    public Date getDateOfRecordEntry() {
        return dateOfRecordEntry;
    }

    public void setDateOfRecordEntry(Date dateOfRecordEntry) {
        this.dateOfRecordEntry = dateOfRecordEntry;
    }

    public Date getDateOfRecordTransaction() {
        return dateOfRecordTransaction;
    }

    public void setDateOfRecordTransaction(Date dateOfRecordTransaction) {
        this.dateOfRecordTransaction = dateOfRecordTransaction;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getDebitCredit() {
        return debitCredit;
    }

    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit;
    }

    public String getEntryFor() {
        return entryFor;
    }

    public void setEntryFor(String entryFor) {
        this.entryFor = entryFor;
    }

    public String getEstateProperty() {
        return estateProperty;
    }

    public void setEstateProperty(String estateProperty) {
        this.estateProperty = estateProperty;
    }

    public String getEstatePropertyId() {
        return estatePropertyId;
    }

    public void setEstatePropertyId(String estatePropertyId) {
        this.estatePropertyId = estatePropertyId;
    }

    public String getEstatePropertyLedgerId() {
        return estatePropertyLedgerId;
    }

    public void setEstatePropertyLedgerId(String estatePropertyLedgerId) {
        this.estatePropertyLedgerId = estatePropertyLedgerId;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getLedgerYear() {
        return ledgerYear;
    }

    public void setLedgerYear(Integer ledgerYear) {
        this.ledgerYear = ledgerYear;
    }

    public String getMediumOfPayment() {
        return mediumOfPayment;
    }

    public void setMediumOfPayment(String mediumOfPayment) {
        this.mediumOfPayment = mediumOfPayment;
    }

    public String getMediumOfPaymentNumber() {
        return mediumOfPaymentNumber;
    }

    public void setMediumOfPaymentNumber(String mediumOfPaymentNumber) {
        this.mediumOfPaymentNumber = mediumOfPaymentNumber;
    }

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPaymentFor() {
        return paymentFor;
    }

    public void setPaymentFor(String paymentFor) {
        this.paymentFor = paymentFor;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    public String getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(String propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
    }

    public String getPropertyOccupantId() {
        return propertyOccupantId;
    }

    public void setPropertyOccupantId(String propertyOccupantId) {
        this.propertyOccupantId = propertyOccupantId;
    }

    public String getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getReceiptNumberIssued() {
        return receiptNumberIssued;
    }

    public void setReceiptNumberIssued(String receiptNumberIssued) {
        this.receiptNumberIssued = receiptNumberIssued;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getZonalOffice() {
        return zonalOffice;
    }

    public void setZonalOffice(String zonalOffice) {
        this.zonalOffice = zonalOffice;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    
}

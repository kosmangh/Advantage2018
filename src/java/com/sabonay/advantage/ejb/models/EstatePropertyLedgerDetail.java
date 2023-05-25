/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

import java.io.Serializable;



public class EstatePropertyLedgerDetail implements Serializable {
   
    private String estatePropertyLedgerId;
    private String dateOfRecordEntry;
    private String dateOfRecordTransaction;
    private String debitCreditEntry;
    private String amountInvolved;
    private String receiptNumberIssued;
    private String paymentFor;
    private String paymentTypeDetail;
    private String payeeName;
    private String mediumOfPayment;
    private String mediumOfPaymentNumber;
    private String ledgerYear;
    private String recordedBy;
    private String zonalOffice;
    private String lastModifiedDate;
    private String lastModifiedBy;
    private String estateProperty;
    private String paymentType;
    private String propertyOccupantId;
    private String occupantName;

    public EstatePropertyLedgerDetail()
    {
    }


    
    public String getAmountInvolved()
    {
        return amountInvolved;
    }

    public void setAmountInvolved(String amountInvolved)
    {
        this.amountInvolved = amountInvolved;
    }

    public String getDateOfRecordEntry()
    {
        return dateOfRecordEntry;
    }

    public void setDateOfRecordEntry(String dateOfRecordEntry)
    {
        this.dateOfRecordEntry = dateOfRecordEntry;
    }

    public String getDateOfRecordTransaction()
    {
        return dateOfRecordTransaction;
    }

    public void setDateOfRecordTransaction(String dateOfRecordTransaction)
    {
        this.dateOfRecordTransaction = dateOfRecordTransaction;
    }

    public String getEstateProperty()
    {
        return estateProperty;
    }

    public void setEstateProperty(String estateProperty)
    {
        this.estateProperty = estateProperty;
    }

    public String getEstatePropertyLedgerId()
    {
        return estatePropertyLedgerId;
    }

    public void setEstatePropertyLedgerId(String estatePropertyLedgerId)
    {
        this.estatePropertyLedgerId = estatePropertyLedgerId;
    }

    public String getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy)
    {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLedgerYear()
    {
        return ledgerYear;
    }

    public void setLedgerYear(String ledgerYear)
    {
        this.ledgerYear = ledgerYear;
    }

    public String getPropertyOccupantName()
    {
        return occupantName;
    }

    public void setPropertyOccupantName(String occupantName)
    {
        this.occupantName = occupantName;
    }

    public String getMediumOfPayment()
    {
        return mediumOfPayment;
    }

    public void setMediumOfPayment(String mediumOfPayment)
    {
        this.mediumOfPayment = mediumOfPayment;
    }

    public String getMediumOfPaymentNumber()
    {
        return mediumOfPaymentNumber;
    }

    public void setMediumOfPaymentNumber(String mediumOfPaymentNumber)
    {
        this.mediumOfPaymentNumber = mediumOfPaymentNumber;
    }

    public String getPayeeName()
    {
        return payeeName;
    }

    public void setPayeeName(String payeeName)
    {
        this.payeeName = payeeName;
    }

    public String getPaymentFor()
    {
        return paymentFor;
    }

    public void setPaymentFor(String paymentFor)
    {
        this.paymentFor = paymentFor;
    }

    public String getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getPaymentTypeDetail()
    {
        return paymentTypeDetail;
    }

    public void setPaymentTypeDetail(String paymentTypeDetail)
    {
        this.paymentTypeDetail = paymentTypeDetail;
    }

    public String getReceiptNumberIssued()
    {
        return receiptNumberIssued;
    }

    public void setReceiptNumberIssued(String receiptNumberIssued)
    {
        this.receiptNumberIssued = receiptNumberIssued;
    }

    public String getRecordedBy()
    {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy)
    {
        this.recordedBy = recordedBy;
    }

    public String getDebitCreditEntry()
    {
        return debitCreditEntry;
    }

    public void setDebitCreditEntry(String debitCreditEntry)
    {
        this.debitCreditEntry = debitCreditEntry;
    }

    public String getOccupantName()
    {
        return occupantName;
    }

    public void setOccupantName(String occupantName)
    {
        this.occupantName = occupantName;
    }



    public String getZonalOffice()
    {
        return zonalOffice;
    }

    public void setZonalOffice(String zonalOffice)
    {
        this.zonalOffice = zonalOffice;
    }

    public String getPropertyOccupantId()
    {
        return propertyOccupantId;
    }

    public void setPropertyOccupantId(String propertyOccupantId)
    {
        this.propertyOccupantId = propertyOccupantId;
    }

    


}

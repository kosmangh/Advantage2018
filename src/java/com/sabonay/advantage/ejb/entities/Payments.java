/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SEBASTIAN
 */
@Entity
@Table(name = "payments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payments.findAll", query = "SELECT p FROM Payments p"),
    @NamedQuery(name = "Payments.findByEstatePropertyLedgerId", query = "SELECT p FROM Payments p WHERE p.estatePropertyLedgerId = :estatePropertyLedgerId"),
    @NamedQuery(name = "Payments.findByEstateProperty", query = "SELECT p FROM Payments p WHERE p.estateProperty = :estateProperty"),
    @NamedQuery(name = "Payments.findByPropertyOccupant", query = "SELECT p FROM Payments p WHERE p.propertyOccupant = :propertyOccupant"),
    @NamedQuery(name = "Payments.findByDateOfRecordEntry", query = "SELECT p FROM Payments p WHERE p.dateOfRecordEntry = :dateOfRecordEntry"),
    @NamedQuery(name = "Payments.findByDateOfRecordTransaction", query = "SELECT p FROM Payments p WHERE p.dateOfRecordTransaction = :dateOfRecordTransaction"),
    @NamedQuery(name = "Payments.findByTypeOfEntry", query = "SELECT p FROM Payments p WHERE p.typeOfEntry = :typeOfEntry"),
    @NamedQuery(name = "Payments.findByAmountInvolved", query = "SELECT p FROM Payments p WHERE p.amountInvolved = :amountInvolved"),
    @NamedQuery(name = "Payments.findByReceiptNumberIssued", query = "SELECT p FROM Payments p WHERE p.receiptNumberIssued = :receiptNumberIssued"),
    @NamedQuery(name = "Payments.findByPaymentType", query = "SELECT p FROM Payments p WHERE p.paymentType = :paymentType"),
    @NamedQuery(name = "Payments.findByPaymentFor", query = "SELECT p FROM Payments p WHERE p.paymentFor = :paymentFor"),
    @NamedQuery(name = "Payments.findByPayeeName", query = "SELECT p FROM Payments p WHERE p.payeeName = :payeeName"),
    @NamedQuery(name = "Payments.findByMediumOfPayment", query = "SELECT p FROM Payments p WHERE p.mediumOfPayment = :mediumOfPayment"),
    @NamedQuery(name = "Payments.findByMediumOfPaymentNumber", query = "SELECT p FROM Payments p WHERE p.mediumOfPaymentNumber = :mediumOfPaymentNumber"),
    @NamedQuery(name = "Payments.findByLedgerYear", query = "SELECT p FROM Payments p WHERE p.ledgerYear = :ledgerYear"),
    @NamedQuery(name = "Payments.findByRecordedBy", query = "SELECT p FROM Payments p WHERE p.recordedBy = :recordedBy"),
    @NamedQuery(name = "Payments.findByZonalOffice", query = "SELECT p FROM Payments p WHERE p.zonalOffice = :zonalOffice"),
    @NamedQuery(name = "Payments.findByLastModifiedDate", query = "SELECT p FROM Payments p WHERE p.lastModifiedDate = :lastModifiedDate"),
    @NamedQuery(name = "Payments.findByLastModifiedBy", query = "SELECT p FROM Payments p WHERE p.lastModifiedBy = :lastModifiedBy"),
    @NamedQuery(name = "Payments.findByDeleted", query = "SELECT p FROM Payments p WHERE p.deleted = :deleted"),
    @NamedQuery(name = "Payments.findByUpdated", query = "SELECT p FROM Payments p WHERE p.updated = :updated"),
    @NamedQuery(name = "Payments.findByYearPaidFor", query = "SELECT p FROM Payments p WHERE p.yearPaidFor = :yearPaidFor")})
public class Payments implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "estate_property_ledger_id")
    private String estatePropertyLedgerId;
    @Size(max = 45)
    @Column(name = "estate_property")
    private String estateProperty;
    @Size(max = 100)
    @Column(name = "property_occupant")
    private String propertyOccupant;
    @Column(name = "date_of_record_entry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfRecordEntry;
    @Column(name = "date_of_record_transaction")
    @Temporal(TemporalType.DATE)
    private Date dateOfRecordTransaction;
    @Size(max = 255)
    @Column(name = "type_of_entry")
    private String typeOfEntry;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount_involved")
    private Double amountInvolved;
    @Size(max = 255)
    @Column(name = "receipt_number_issued")
    private String receiptNumberIssued;
    @Size(max = 45)
    @Column(name = "payment_type")
    private String paymentType;
    @Size(max = 255)
    @Column(name = "payment_for")
    private String paymentFor;
    @Size(max = 255)
    @Column(name = "payee_name")
    private String payeeName;
    @Size(max = 255)
    @Column(name = "medium_of_payment")
    private String mediumOfPayment;
    @Size(max = 255)
    @Column(name = "medium_of_payment_number")
    private String mediumOfPaymentNumber;
    @Column(name = "ledger_year")
    private Integer ledgerYear;
    @Size(max = 255)
    @Column(name = "recorded_by")
    private String recordedBy;
    @Size(max = 255)
    @Column(name = "zonal_office")
    private String zonalOffice;
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Size(max = 255)
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    @Size(max = 5)
    @Column(name = "deleted")
    private String deleted;
    @Size(max = 5)
    @Column(name = "updated")
    private String updated;
    @Column(name = "year_paid_for")
    private Integer yearPaidFor;

    public Payments() {
    }

    public Payments(String estatePropertyLedgerId) {
        this.estatePropertyLedgerId = estatePropertyLedgerId;
    }

    public String getEstatePropertyLedgerId() {
        return estatePropertyLedgerId;
    }

    public void setEstatePropertyLedgerId(String estatePropertyLedgerId) {
        this.estatePropertyLedgerId = estatePropertyLedgerId;
    }

    public String getEstateProperty() {
        return estateProperty;
    }

    public void setEstateProperty(String estateProperty) {
        this.estateProperty = estateProperty;
    }

    public String getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(String propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
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

    public String getTypeOfEntry() {
        return typeOfEntry;
    }

    public void setTypeOfEntry(String typeOfEntry) {
        this.typeOfEntry = typeOfEntry;
    }

    public Double getAmountInvolved() {
        return amountInvolved;
    }

    public void setAmountInvolved(Double amountInvolved) {
        this.amountInvolved = amountInvolved;
    }

    public String getReceiptNumberIssued() {
        return receiptNumberIssued;
    }

    public void setReceiptNumberIssued(String receiptNumberIssued) {
        this.receiptNumberIssued = receiptNumberIssued;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentFor() {
        return paymentFor;
    }

    public void setPaymentFor(String paymentFor) {
        this.paymentFor = paymentFor;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
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

    public Integer getLedgerYear() {
        return ledgerYear;
    }

    public void setLedgerYear(Integer ledgerYear) {
        this.ledgerYear = ledgerYear;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getZonalOffice() {
        return zonalOffice;
    }

    public void setZonalOffice(String zonalOffice) {
        this.zonalOffice = zonalOffice;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getYearPaidFor() {
        return yearPaidFor;
    }

    public void setYearPaidFor(Integer yearPaidFor) {
        this.yearPaidFor = yearPaidFor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estatePropertyLedgerId != null ? estatePropertyLedgerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payments)) {
            return false;
        }
        Payments other = (Payments) object;
        if ((this.estatePropertyLedgerId == null && other.estatePropertyLedgerId != null) || (this.estatePropertyLedgerId != null && !this.estatePropertyLedgerId.equals(other.estatePropertyLedgerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.Payments[ estatePropertyLedgerId=" + estatePropertyLedgerId + " ]";
    }
    
}

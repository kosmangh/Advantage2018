/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.entities;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.common.api.Selectable;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.constants._MediumOfPayment;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Edwin
 */
@Entity
@Table(name = "estate_property_ledger")
@NamedQueries({
    @NamedQuery(name = "EstatePropertyLedger.findAll", query = "SELECT e FROM EstatePropertyLedger e")
})
public class EstatePropertyLedger implements Serializable, Selectable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "estate_property_ledger_id")
    private String estatePropertyLedgerId;
    @Column(name = "date_of_record_entry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfRecordEntry;
    @Column(name = "date_of_record_transaction")
    @Temporal(TemporalType.DATE)
    private Date dateOfRecordTransaction;
    @Column(name = "type_of_entry")
    @Enumerated(EnumType.STRING)
    private DebitCredit debitCreditEntry;
    @Column(name = "amount_involved")
    private Double amountInvolved;
    @Column(name = "receipt_number_issued")
    private String receiptNumberIssued;
    @Column(name = "payment_for")
    private String paymentFor;
    @Column(name = "payee_name")
    private String payeeName;
    @Column(name = "medium_of_payment")
    private String mediumOfPayment = _MediumOfPayment.CASH;
    @Column(name = "medium_of_payment_number")
    private String mediumOfPaymentNumber;
    @Column(name = "ledger_year")
    private Integer ledgerYear;
    @Column(name = "recorded_by")
    private String recordedBy;
    @Column(name = "zonal_office")
    private String zonalOffice;
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    @Column(name = "deleted")
    private String deleted;
    @Column(name = "updated")
    private String updated;
    @Column(name = "year_paid_for")
    private Integer yearPaidFor;
    @JoinColumn(name = "estate_property", referencedColumnName = "estate_property_id")
    @ManyToOne
    private EstateProperty estateProperty;
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @JoinColumn(name = "property_occupant", referencedColumnName = "property_occupant_id", nullable = false)
    @ManyToOne
    private PropertyOccupant propertyOccupant;
    @Transient
    private Boolean selected;

    public EstatePropertyLedger() {
    }

    public EstatePropertyLedger(String estatePropertyLedgerId) {
        this.estatePropertyLedgerId = estatePropertyLedgerId;
    }

    public String getEstatePropertyLedgerId() {
        return estatePropertyLedgerId;
    }

    public void setEstatePropertyLedgerId(String estatePropertyLedgerId) {
        this.estatePropertyLedgerId = estatePropertyLedgerId;
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

    public EstateProperty getEstateProperty() {
        return estateProperty;
    }

    public void setEstateProperty(EstateProperty estateProperty) {
        this.estateProperty = estateProperty;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public DebitCredit getDebitCreditEntry() {
        return debitCreditEntry;
    }

    public void setDebitCreditEntry(DebitCredit debitCreditEntry) {
        this.debitCreditEntry = debitCreditEntry;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PropertyOccupant getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(PropertyOccupant propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estatePropertyLedgerId != null ? estatePropertyLedgerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof EstatePropertyLedger)) {
            return false;
        }
        EstatePropertyLedger other = (EstatePropertyLedger) object;
        if ((this.estatePropertyLedgerId == null && other.estatePropertyLedgerId != null) || (this.estatePropertyLedgerId != null && !this.estatePropertyLedgerId.equals(other.estatePropertyLedgerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return estatePropertyLedgerId
                + " Current Occupant: " + getEstateProperty().getCurrentOccupantName()
                + " Amt: " + amountInvolved
                + " pt: " + paymentType
                + " D/C : " + debitCreditEntry;
    }

    @Override
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public Boolean getSelected() {
        return selected;
    }
}

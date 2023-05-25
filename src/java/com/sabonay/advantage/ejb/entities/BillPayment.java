/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "bill_payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BillPayment.findAll", query = "SELECT b FROM BillPayment b"),
    @NamedQuery(name = "BillPayment.findByBillPaymentId", query = "SELECT b FROM BillPayment b WHERE b.billPaymentId = :billPaymentId"),
    @NamedQuery(name = "BillPayment.findByPropertyOccupant", query = "SELECT b FROM BillPayment b WHERE b.propertyOccupant = :propertyOccupant"),
    @NamedQuery(name = "BillPayment.findByReceiptNumber", query = "SELECT b FROM BillPayment b WHERE b.receiptNumber = :receiptNumber"),
    @NamedQuery(name = "BillPayment.findByDatePaid", query = "SELECT b FROM BillPayment b WHERE b.datePaid = :datePaid"),
    @NamedQuery(name = "BillPayment.findByDateOfTransaction", query = "SELECT b FROM BillPayment b WHERE b.dateOfTransaction = :dateOfTransaction"),
    @NamedQuery(name = "BillPayment.findByCashChq", query = "SELECT b FROM BillPayment b WHERE b.cashChq = :cashChq"),
    @NamedQuery(name = "BillPayment.findByAmountInvolved", query = "SELECT b FROM BillPayment b WHERE b.amountInvolved = :amountInvolved")})
public class BillPayment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "bill_payment_id")
    private String billPaymentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "property_occupant")
    private String propertyOccupant;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "receipt_number")
    private String receiptNumber;
    @Column(name = "date_paid")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaid;
    @Column(name = "date_of_transaction")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfTransaction;
    @Size(max = 10)
    @Column(name = "cash_chq")
    private String cashChq;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount_involved")
    private Double amountInvolved;

    public BillPayment() {
    }

    public BillPayment(String billPaymentId) {
        this.billPaymentId = billPaymentId;
    }

    public BillPayment(String billPaymentId, String propertyOccupant, String receiptNumber) {
        this.billPaymentId = billPaymentId;
        this.propertyOccupant = propertyOccupant;
        this.receiptNumber = receiptNumber;
    }

    public String getBillPaymentId() {
        return billPaymentId;
    }

    public void setBillPaymentId(String billPaymentId) {
        this.billPaymentId = billPaymentId;
    }

    public String getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(String propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getCashChq() {
        return cashChq;
    }

    public void setCashChq(String cashChq) {
        this.cashChq = cashChq;
    }

    public Double getAmountInvolved() {
        return amountInvolved;
    }

    public void setAmountInvolved(Double amountInvolved) {
        this.amountInvolved = amountInvolved;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billPaymentId != null ? billPaymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BillPayment)) {
            return false;
        }
        BillPayment other = (BillPayment) object;
        if ((this.billPaymentId == null && other.billPaymentId != null) || (this.billPaymentId != null && !this.billPaymentId.equals(other.billPaymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.BillPayment[ billPaymentId=" + billPaymentId + " ]";
    }
    
}

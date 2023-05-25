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
 * @author SEBASTIAN / Ritchid
 */
@Entity
@Table(name = "bills")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bills.findAll", query = "SELECT b FROM Bills b"),
    @NamedQuery(name = "Bills.findByEstatePropertyLedgerId", query = "SELECT b FROM Bills b WHERE b.estatePropertyLedgerId = :estatePropertyLedgerId"),
    @NamedQuery(name = "Bills.findByBillnumber", query = "SELECT b FROM Bills b WHERE b.billnumber = :billnumber"),
    @NamedQuery(name = "Bills.findByEstateProperty", query = "SELECT b FROM Bills b WHERE b.estateProperty = :estateProperty"),
    @NamedQuery(name = "Bills.findByPropertyOccupant", query = "SELECT b FROM Bills b WHERE b.propertyOccupant = :propertyOccupant"),
    @NamedQuery(name = "Bills.findByDateOfRecordEntry", query = "SELECT b FROM Bills b WHERE b.dateOfRecordEntry = :dateOfRecordEntry"),
    @NamedQuery(name = "Bills.findByBillDetail", query = "SELECT b FROM Bills b WHERE b.billDetail = :billDetail"),
    @NamedQuery(name = "Bills.findByBillAmount", query = "SELECT b FROM Bills b WHERE b.billAmount = :billAmount"),
    @NamedQuery(name = "Bills.findByDefaultAmount", query = "SELECT b FROM Bills b WHERE b.defaultAmount = :defaultAmount"),
    @NamedQuery(name = "Bills.findByBillVat", query = "SELECT b FROM Bills b WHERE b.billVat = :billVat"),
    @NamedQuery(name = "Bills.findByBillDiscount", query = "SELECT b FROM Bills b WHERE b.billDiscount = :billDiscount"),
    @NamedQuery(name = "Bills.findByBillAmountPaid", query = "SELECT b FROM Bills b WHERE b.billAmountPaid = :billAmountPaid"),
    @NamedQuery(name = "Bills.findByBillType", query = "SELECT b FROM Bills b WHERE b.billType = :billType"),
    @NamedQuery(name = "Bills.findByBillYear", query = "SELECT b FROM Bills b WHERE b.billYear = :billYear"),
    @NamedQuery(name = "Bills.findByRecordedBy", query = "SELECT b FROM Bills b WHERE b.recordedBy = :recordedBy"),
    @NamedQuery(name = "Bills.findByLastModifiedBy", query = "SELECT b FROM Bills b WHERE b.lastModifiedBy = :lastModifiedBy"),
    @NamedQuery(name = "Bills.findByDeleted", query = "SELECT b FROM Bills b WHERE b.deleted = :deleted"),
    @NamedQuery(name = "Bills.findByUpdated", query = "SELECT b FROM Bills b WHERE b.updated = :updated"),
    @NamedQuery(name = "Bills.findByBillStatus", query = "SELECT b FROM Bills b WHERE b.billStatus = :billStatus")
})
public class Bills implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "estate_property_ledger_id")
    private String estatePropertyLedgerId;
    
    @Size(max = 25)
    @Column(name = "billnumber")
    private String billnumber;
    
    @Size(max = 45)
    @Column(name = "estate_property")
    private String estateProperty;
    
    @Size(max = 100)
    @Column(name = "property_occupant")
    private String propertyOccupant;
    
    @Column(name = "date_of_record_entry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfRecordEntry;
    
    @Size(max = 255)
    @Column(name = "bill_detail")
    private String billDetail;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "bill_amount")
    private Double billAmount;
    
    @Column(name = "default_amount")
    private Double defaultAmount;
    
    @Column(name = "bill_vat")
    private Double billVat;
    
    @Column(name = "bill_discount")
    private Double billDiscount;
    
    @Column(name = "bill_amount_paid")
    private Double billAmountPaid;
    
    @Size(max = 45)
    @Column(name = "bill_type")
    private String billType;
    
    @Column(name = "bill_year")
    private Integer billYear;
    
    @Size(max = 255)
    @Column(name = "recorded_by")
    private String recordedBy;
    
    @Size(max = 255)
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @Size(max = 5)
    @Column(name = "deleted")
    private String deleted;
    
    @Size(max = 5)
    @Column(name = "updated")
    private String updated;
    
    @Size(max = 45)
    @Column(name = "bill_status")
    private String billStatus;
    
//  
//    
//    @JoinColumn(name = "estate_property", referencedColumnName = "estate_property_id", nullable = false)
//    @ManyToOne
//    private EstateProperty getestateProperty;

    public Bills() {
    }

    public Bills(String estatePropertyLedgerId) {
        this.estatePropertyLedgerId = estatePropertyLedgerId;
    }

    public String getEstatePropertyLedgerId() {
        return estatePropertyLedgerId;
    }

    public void setEstatePropertyLedgerId(String estatePropertyLedgerId) {
        this.estatePropertyLedgerId = estatePropertyLedgerId;
    }

    public String getBillnumber() {
        return billnumber;
    }

    public void setBillnumber(String billnumber) {
        this.billnumber = billnumber;
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

    public String getBillDetail() {
        return billDetail;
    }

    public void setBillDetail(String billDetail) {
        this.billDetail = billDetail;
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public Double getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(Double defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public Double getBillVat() {
        return billVat;
    }

    public void setBillVat(Double billVat) {
        this.billVat = billVat;
    }

    public Double getBillDiscount() {
        return billDiscount;
    }

    public void setBillDiscount(Double billDiscount) {
        this.billDiscount = billDiscount;
    }

    public Double getBillAmountPaid() {
        return billAmountPaid;
    }

    public void setBillAmountPaid(Double billAmountPaid) {
        this.billAmountPaid = billAmountPaid;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public Integer getBillYear() {
        return billYear;
    }

    public void setBillYear(Integer billYear) {
        this.billYear = billYear;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
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

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }
//
//    public EstateProperty getGetestateProperty() {
//        return getestateProperty;
//    }
//
//    public void setGetestateProperty(EstateProperty getestateProperty) {
//        this.getestateProperty = getestateProperty;
//    }
//    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estatePropertyLedgerId != null ? estatePropertyLedgerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bills)) {
            return false;
        }
        Bills other = (Bills) object;
        if ((this.estatePropertyLedgerId == null && other.estatePropertyLedgerId != null) || (this.estatePropertyLedgerId != null && !this.estatePropertyLedgerId.equals(other.estatePropertyLedgerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.Bills[ estatePropertyLedgerId=" + estatePropertyLedgerId + " ]";
    }
    
}

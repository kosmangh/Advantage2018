/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.common.api.Selectable;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Edwin / Ritchid
 */
@Entity
@Table(name = "ground_rent")
@NamedQueries({
    @NamedQuery(name = "GroundRent.findAll", query = "SELECT g FROM GroundRent g")})
public class GroundRent implements Serializable, Selectable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ground_rent_id")
    private String groundRentId;
    
    @Column(name = "year_due")
    private Integer yearDue;
    
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @Column(name = "updated")
    private boolean updated;
    
    @Column(name = "deleted")
    private boolean deleted;
    
//    @Column(name = "activated")
//    private String activated;
    
    @Column(name = "property_usage")
    @Enumerated(EnumType.STRING)
    private PropertyUsage propertyUsage;
    
    @Column(name = "first_class_estate_amount_charged")
    private Double firstClassEstateAmountCharged;
    
    @Column(name = "second_class_estate_amount_charged")
    private Double secondClassEstateAmountCharged;
    
    @Column(name = "third_class_estate_amount_charged")
    private Double thirdClassEstateAmountCharged;
    
    @Transient
    private Boolean selected;

    public GroundRent() {
    }

    public GroundRent(String groundRentId) {
        this.groundRentId = groundRentId;
    }

    public String getGroundRentId() {
        return groundRentId;
    }

    public void setGroundRentId(String groundRentId) {
        this.groundRentId = groundRentId;
    }

    public Integer getYearDue() {
        return yearDue;
    }

    public void setYearDue(Integer yearDue) {
        this.yearDue = yearDue;
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

    public boolean getUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
//
//    public String getActivated() {
//        return activated;
//    }
//
//    public void setActivated(String activated) {
//        this.activated = activated;
//    }
    
    
    

    public PropertyUsage getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(PropertyUsage propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public Double getFirstClassEstateAmountCharged() {
        return firstClassEstateAmountCharged;
    }

    public void setFirstClassEstateAmountCharged(Double firstClassEstateAmountCharged) {
        this.firstClassEstateAmountCharged = firstClassEstateAmountCharged;
    }

    public Double getSecondClassEstateAmountCharged() {
        return secondClassEstateAmountCharged;
    }

    public void setSecondClassEstateAmountCharged(Double secondClassEstateAmountCharged) {
        this.secondClassEstateAmountCharged = secondClassEstateAmountCharged;
    }

    public Double getThirdClassEstateAmountCharged() {
        return thirdClassEstateAmountCharged;
    }

    public void setThirdClassEstateAmountCharged(Double thirdClassEstateAmountCharged) {
        this.thirdClassEstateAmountCharged = thirdClassEstateAmountCharged;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groundRentId != null ? groundRentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroundRent)) {
            return false;
        }
        GroundRent other = (GroundRent) object;
        if ((this.groundRentId == null && other.groundRentId != null) || (this.groundRentId != null && !this.groundRentId.equals(other.groundRentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  groundRentId;
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

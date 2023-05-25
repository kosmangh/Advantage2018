/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.entities;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropOccupationType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Edwin / Ritchid
 */
@Entity
@Table(name = "occupancy")
public class Occupancy implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "occupancy_id")
    private String occupantPropertyId;
    
    @Column(name = "first_date_of_occupancy", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date firstDateOfOccupancy;
    
    @Column(name = "last_date_of_occupancy")
    @Temporal(TemporalType.DATE)
    private Date lastDateOfOccupancy;
    
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @Column(name = "updated")
    private boolean updated;
    
    @Column(name = "deleted")
    private boolean deleted;
    
    @JoinColumn(name = "occupant", referencedColumnName = "property_occupant_id", nullable = false)
    @ManyToOne
    private PropertyOccupant propertyOccupant;
    
    @JoinColumn(name = "estate_property", referencedColumnName = "estate_property_id", nullable = false)
    @OneToOne
    private EstateProperty estateProperty;
    
    @Column(name = "property_occupied_as", nullable = false)
    @Enumerated(EnumType.STRING)
    private PropOccupationType occupationType;
    
    @Transient
    private Boolean selected;

    
    
    
    
    
    
    
    public Occupancy() {
    }

    
    
    
    
    public String getOccupantPropertyId() {
        return occupantPropertyId;
    }

    public void setOccupantPropertyId(String occupantPropertyId) {
        this.occupantPropertyId = occupantPropertyId;
    }

    public PropertyOccupant getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(PropertyOccupant propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
    }

    public Date getFirstDateOfOccupancy() {
        return firstDateOfOccupancy;
    }

    public void setFirstDateOfOccupancy(Date firstDateOfOccupancy) {
        this.firstDateOfOccupancy = firstDateOfOccupancy;
    }

    public Date getLastDateOfOccupancy() {
        return lastDateOfOccupancy;
    }

    public void setLastDateOfOccupancy(Date lastDateOfOccupancy) {
        this.lastDateOfOccupancy = lastDateOfOccupancy;
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

    @Override
    public String toString() {
        if (estateProperty != null) {
            return estateProperty.getEstatePropertyId();
        }
        return "";
    }

    public EstateProperty getEstateProperty() {
        return estateProperty;
    }

    public void setEstateProperty(EstateProperty estateProperty) {
        this.estateProperty = estateProperty;
    }

    public PropOccupationType getOccupationType() {
        return occupationType;
    }

    public void setOccupationType(PropOccupationType occupationType) {
        this.occupationType = occupationType;
    }

    public PaymentType getAppropriatePaymentType() {
        if (getOccupationType() == PropOccupationType.Rental) {
            return PaymentType.HOUSE_RENT;
        } else if (getOccupationType() == PropOccupationType.Leasehold) {
            return PaymentType.GROUND_RENT;
        }

        return null;
    }

    public boolean isPropertyOccupiesAsRental() {
        if (occupationType != null) {
            return occupationType == PropOccupationType.Rental;
        }

        return false;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }
}
